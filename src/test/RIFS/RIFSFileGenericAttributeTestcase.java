///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RIFSFileGenericAttributeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RIFS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceMetaData;

import test.RIFSTest;
import test.Testcase;
import test.misc.VIFSSandbox;

import com.ibm.as400.resource.RIFSFile;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase RIFSFileGenericAttributeTestcase.  This tests the following methods
of the RIFSFile class, inherited from BufferedResource:

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
public class RIFSFileGenericAttributeTestcase
extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RIFSFileGenericAttributeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.RIFSTest.main(newArgs); 
   }



    // Constants.



    // Private data.
    private VIFSSandbox     sandbox_;



/**
Constructor.
**/
    public RIFSFileGenericAttributeTestcase (AS400 systemObject,
        Hashtable<String, Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RIFSFileGenericAttributeTestcase",
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
    String sandboxLib_ = "RIFSFGAT";
    protected void setup ()
    throws Exception
    {
      if (testLib_ != null ) { 
        int len = testLib_.length(); 
        if (len >= 5) { 
          sandboxLib_ = "RIFGA"+testLib_.substring(len-5); 
        }
      }

        sandbox_ = new VIFSSandbox(systemObject_, sandboxLib_);
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        sandbox_.cleanup();
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
no system or path set.
**/
    public void Var001()
    {
        try {
            RIFSFile u = new RIFSFile();
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
            IFSFile f = sandbox_.createFile("Susan");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date lastModified = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            u.cancelAttributeChanges();
            Date lastModified2 = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(lastModified2.equals(lastModified));
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
            IFSFile f = sandbox_.createFile("Jeff");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date lastModified = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Boolean isHidden = (Boolean)u.getAttributeValue(RIFSFile.IS_HIDDEN);
            Date date1 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u.setAttributeValue(RIFSFile.IS_HIDDEN, Boolean.valueOf(!isHidden.booleanValue()));
            u.cancelAttributeChanges();
            Date lastModified2 = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Boolean isHidden2 = (Boolean)u.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition((lastModified2.equals(lastModified))
                   && (isHidden2.equals(isHidden)));
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
            IFSFile f = sandbox_.createFile("Jenn");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date lastModified = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Boolean isHidden = (Boolean)u.getAttributeValue(RIFSFile.IS_HIDDEN);
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, new Date());
            u.setAttributeValue(RIFSFile.IS_HIDDEN, Boolean.valueOf(!isHidden.booleanValue()));
            u.cancelAttributeChanges();
            u.commitAttributeChanges();
            Date lastModified2 = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Boolean isHidden2 = (Boolean)u.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition((lastModified2.equals(lastModified))
                   && (isHidden2.equals(isHidden)));
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
            IFSFile f = sandbox_.createFile("Darin");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            RIFSTest.ResourceListener_ rl = new RIFSTest.ResourceListener_();
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
no system or path set.
**/
    public void Var006()
    {
        try {
            RIFSFile u = new RIFSFile();
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
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
            RIFSFile u = new RIFSFile(system, "ClifLock");
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, new Date());
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
commitAttributeChanges() - When the path does not exist.
**/
    public void Var008()
    {
        try {
            RIFSFile u = new RIFSFile(systemObject_, "NOTEXIST");
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, new Date());
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
            IFSFile f = sandbox_.createFile("Schuman");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date lastModified = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            u.commitAttributeChanges();
            Date lastModified2 = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(lastModified2.equals(lastModified));
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
            IFSFile f = sandbox_.createFile("Jim");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            // Date lastModified = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            // Boolean isHidden = (Boolean)u.getAttributeValue(RIFSFile.IS_HIDDEN);
            Date date1 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u.setAttributeValue(RIFSFile.IS_HIDDEN, Boolean.TRUE);
            u.commitAttributeChanges();
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date lastModified2 = (Date)u2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Boolean isHidden2 = (Boolean)u2.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition((lastModified2.equals(date1))
                   && (isHidden2.equals(Boolean.TRUE)));
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
            IFSFile f = sandbox_.createFile("Rick");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            RIFSTest.ResourceListener_ rl = new RIFSTest.ResourceListener_();
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
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            // I did not hardcode an exact length...otherwise, we
            // have to change this every time we add a property.
            assertCondition((amd.length > 10) 
                   && (verifyAttributeMetaData(amd, RIFSFile.PARENT, String.class, true, 0, null, false, false))
                   && (verifyAttributeMetaData(amd, RIFSFile.IS_READ_ONLY, Boolean.class, false, 0, null, false, false))
                   && (verifyAttributeMetaData(amd, RIFSFile.CCSID, Integer.class, true, 0, null, false, false)));
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
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(null);
            failed ("Didn't throw exception for "+amd);
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
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(new Date());
            failed ("Didn't throw exception for "+amd);
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
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.ABSOLUTE_PATH);
            assertCondition(verifyAttributeMetaData(amd, RIFSFile.ABSOLUTE_PATH, String.class, true, 0, null, false, false));
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
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.EXISTS);
            assertCondition(verifyAttributeMetaData(amd, RIFSFile.EXISTS, Boolean.class, true, 0, null, false, false));
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
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.PATH);
            assertCondition(verifyAttributeMetaData(amd, RIFSFile.PATH, String.class, true, 0, null, false, false));
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
            RIFSFile u = new RIFSFile();
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
            RIFSFile u = new RIFSFile();
            Object value = u.getAttributeUnchangedValue(RIFSFile.LAST_MODIFIED);
            failed ("Didn't throw exception for "+value );
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
            IFSFile f = sandbox_.createFile("Rich");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Object value = u.getAttributeUnchangedValue(null);
            failed ("Didn't throw exception for "+value);
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
            IFSFile f = sandbox_.createFile("Susan");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Object value = u.getAttributeUnchangedValue(new AS400());
            failed ("Didn't throw exception for "+value);
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
            IFSFile f = sandbox_.createFile("Keith");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Object value = u.getAttributeUnchangedValue(RIFSFile.LAST_MODIFIED);
            assertCondition(value.equals(date1));
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
            IFSFile f = sandbox_.createFile("Doug");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Object value = u.getAttributeUnchangedValue(RIFSFile.LAST_MODIFIED);
            assertCondition(value.equals(date1));
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
            IFSFile f = sandbox_.createFile("Rod");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date date2 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date2);
            Object value = u.getAttributeUnchangedValue(RIFSFile.LAST_MODIFIED);
            assertCondition(value.equals(date1));
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
            IFSFile f = sandbox_.createFile("Terry");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date date2 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date2);
            u.refreshAttributeValues();
            Object value = u.getAttributeUnchangedValue(RIFSFile.LAST_MODIFIED);
            assertCondition(value.equals(date1));
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
            IFSFile f = sandbox_.createFile("Sue");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date date2 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date2);
            u.commitAttributeChanges();
            Object value = u.getAttributeUnchangedValue(RIFSFile.LAST_MODIFIED);
            assertCondition(date2.equals(value));
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
            RIFSFile u = new RIFSFile();
            Object value = u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            failed ("Didn't throw exception for "+value);
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
            RIFSFile u = new RIFSFile(system, "Friend");
            Object value = u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            failed ("Didn't throw exception for "+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
getAttributeValue() - When the path does not exist.
**/
    public void Var029()
    {
        try {
            RIFSFile u = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = u.getAttributeValue(RIFSFile.LENGTH);
            assertCondition(((Long)value).longValue() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass null.
**/
    public void Var030()
    {
        try {
            IFSFile f = sandbox_.createFile("Pat");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Object value = u.getAttributeValue(null);
            failed ("Didn't throw exception for "+value);
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
            IFSFile f = sandbox_.createFile("Doug");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Object value = u.getAttributeValue("Yo");
            failed ("Didn't throw exception for "+value);
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
            IFSFile f = sandbox_.createFile("Brian");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Object value = u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(value.equals(date1));
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
            IFSFile f = sandbox_.createFile("Joe");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Object value = u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(value.equals(date1));
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
            IFSFile f = sandbox_.createFile("John");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date date2= new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date2);
            Object value = u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(value.equals(date2));
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
            IFSFile f = sandbox_.createFile("Randy");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date date2= new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date2);
            u.refreshAttributeValues();
            Object value = u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(value.equals(date2));
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
            IFSFile f = sandbox_.createFile("Ulrich");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date date2 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date2);
            u.commitAttributeChanges();
            Object value = u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(value.equals(date2));
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
            IFSFile f = sandbox_.createFile("Fred");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for(int i = 0; i < amd.length; ++i) {
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
    public void Var038()
    {
        try {
            RIFSFile u = new RIFSFile();
            boolean pending = u.hasUncommittedAttributeChanges(RIFSFile.LAST_MODIFIED);
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
            IFSFile f = sandbox_.createFile("Brent");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            boolean pending = u.hasUncommittedAttributeChanges(null);
            failed ("Didn't throw exception "+pending);
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
            IFSFile f = sandbox_.createFile("Warren");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            boolean pending = u.hasUncommittedAttributeChanges("Go Toolbox");
            failed ("Didn't throw exception "+pending);
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
            IFSFile f = sandbox_.createFile("Jeremy");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            boolean pending = u.hasUncommittedAttributeChanges(RIFSFile.LAST_MODIFIED);
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
            IFSFile f = sandbox_.createFile("Greg");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            boolean pending = u.hasUncommittedAttributeChanges(RIFSFile.LAST_MODIFIED);
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
            IFSFile f = sandbox_.createFile("Kin");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date date2= new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date2);
            boolean pending = u.hasUncommittedAttributeChanges(RIFSFile.LAST_MODIFIED);
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
            IFSFile f = sandbox_.createFile("Jim");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date date2 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date2);
            u.refreshAttributeValues();
            boolean pending = u.hasUncommittedAttributeChanges(RIFSFile.LAST_MODIFIED);
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
            IFSFile f = sandbox_.createFile("Susan");
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.commitAttributeChanges();

            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date date2 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date2);
            u.commitAttributeChanges();
            boolean pending = u.hasUncommittedAttributeChanges(RIFSFile.LAST_MODIFIED);
            assertCondition(pending == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - When no system or path set.
**/
    public void Var046()
    {
        try {
            RIFSFile u = new RIFSFile();
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
            IFSFile f = sandbox_.createFile("Robb");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date lastModified = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            u.refreshAttributeValues();
            Date lastModified2 = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(lastModified2.equals(lastModified));
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
            IFSFile f = sandbox_.createFile("Darin");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());            
            Date lastModified = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Boolean isHidden = (Boolean)u.getAttributeValue(RIFSFile.IS_HIDDEN);
            Thread.sleep(1000);
            Date date1 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u.setAttributeValue(RIFSFile.IS_HIDDEN, Boolean.TRUE);
            u.refreshAttributeValues();
            Date lastModified2 = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Boolean isHidden2 = (Boolean)u.getAttributeValue(RIFSFile.IS_HIDDEN);
            Date lastModified2a = (Date)u.getAttributeUnchangedValue(RIFSFile.LAST_MODIFIED);
            Boolean isHidden2a = (Boolean)u.getAttributeUnchangedValue(RIFSFile.IS_HIDDEN);
            assertCondition((lastModified2.equals(date1))
                   && (isHidden2.equals(Boolean.TRUE))
                   && (lastModified2a.equals(lastModified))
                   && (isHidden2a.equals(isHidden)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - When 2 changes have been made for another
RIFSFile object.  Verify that the changes are reflected.
**/
    public void Var049()
    {
        try {
            IFSFile f = sandbox_.createFile("Bill");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            // Date lastModified = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            // Boolean isHidden = (Boolean)u.getAttributeValue(RIFSFile.IS_HIDDEN);

            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date date1 = new Date();
            u2.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u2.setAttributeValue(RIFSFile.IS_HIDDEN, Boolean.TRUE);
            u2.commitAttributeChanges();

            u.refreshAttributeValues();
            Date lastModified2 = (Date)u.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Boolean isHidden2 = (Boolean)u.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition((lastModified2.equals(date1))
                   && (isHidden2.equals(Boolean.TRUE)));
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
            IFSFile f = sandbox_.createFile("Sue");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            RIFSTest.ResourceListener_ rl = new RIFSTest.ResourceListener_();
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
setAttributeValue() - Should work.
**/
    public void Var051()
    {
        try {
            RIFSFile u = new RIFSFile();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, new Date());
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
            IFSFile f = sandbox_.createFile("Jim");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            u.setAttributeValue(null, Integer.valueOf(2));
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
            IFSFile f = sandbox_.createFile("Jeff");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, null);
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
            IFSFile f = sandbox_.createFile("Terry");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            u.setAttributeValue(u, Integer.valueOf(3));
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
            IFSFile f = sandbox_.createFile("Rod");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            u.setAttributeValue(RIFSFile.CCSID, Integer.valueOf(37));
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
            IFSFile f = sandbox_.createFile("Laurel");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            u.setAttributeValue(RIFSFile.LAST_ACCESSED, Integer.valueOf(4));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
setAttributeValue() - Pass a value which is not one of the possible values.
**/
    public void Var057()
    {
        try {
            IFSFile f = sandbox_.createFile("Tony");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            u.setAttributeValue(RIFSFile.LAST_ACCESSED, "Bogus");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
setAttributeValue() - Pass an valid attribute ID, commit and verify.
**/
    public void Var058()
    {
        try {
            IFSFile f = sandbox_.createFile("Bill");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u.commitAttributeChanges();
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Object value = u2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Thread.sleep(1000);
            Date date2 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date2);
            u.commitAttributeChanges();
            u2.refreshAttributeValues();
            Object value2 = u2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition((value.equals(date1)) && (value2.equals(date2)));
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
            IFSFile f = sandbox_.createFile("Dave");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            Thread.sleep(1000);
            Date date2 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date2);
            u.commitAttributeChanges();
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Object value = u2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(value.equals(date2));
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
            IFSFile f = sandbox_.createFile("Schuman");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u.commitAttributeChanges();
            Thread.sleep(1000);
            Date date2 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date2);
            u.commitAttributeChanges();
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Object value = u2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(value.equals(date2));
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
            IFSFile f = sandbox_.createFile("Norman");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u.setAttributeValue(RIFSFile.IS_HIDDEN, Boolean.TRUE);
            u.commitAttributeChanges();
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Object value1 = u2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Object value2 = u2.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition((value1.equals(date1))
                   && (((Boolean)value2).booleanValue() == true));
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
            IFSFile f = sandbox_.createFile("Jon");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u.setAttributeValue(RIFSFile.IS_HIDDEN, Boolean.TRUE);
            u.commitAttributeChanges();
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Object value1 = u2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Object value2 = u2.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition((value1.equals(date1))
                   && (value2.equals(Boolean.TRUE)));
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
            IFSFile f = sandbox_.createFile("Pete");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            RIFSTest.ResourceListener_ rl = new RIFSTest.ResourceListener_();
            u.addResourceListener(rl);
            Date date1 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_VALUE_CHANGED)
                    && (rl.event_.getAttributeID() == RIFSFile.LAST_MODIFIED)
                    && (rl.event_.getValue() == date1));
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
            IFSFile f = sandbox_.createFile("Dan");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for(int i = 0; i < amd.length; ++i) {
                if (! amd[i].isReadOnly()) {
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
    }



/**
setAttributeValue() - A user without a directory entry.  Set an attribute
that does not require a directory entry.
**/
    public void Var065()
    {
        try {
            IFSFile f = sandbox_.createFile("Paul");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Date date1 = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, date1);
            u.commitAttributeChanges();
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            Date lastModified = (Date)u2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(lastModified.equals(date1));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




