///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RIFSFileListSelectionTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RIFS;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileFilter;
import com.ibm.as400.resource.RIFSFile;
import com.ibm.as400.resource.RIFSFileList;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;
import test.misc.VIFSSandbox;



/**
Testcase RIFSFileListSelectionTestcase.  This tests the following methods
of the RIFSFileList class, some inherited from ResourceList:

<ul>
<li>getSelectionMetaData() 
<li>getSelectionMetaData() 
<li>getSelectionValue() 
<li>setSelectionValue() 
</ul>
**/
@SuppressWarnings("deprecation")
public class RIFSFileListSelectionTestcase
extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RIFSFileListSelectionTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.RIFSTest.main(newArgs); 
   }



    // Constants.



    // Private data.
    private IFSFileFilter   dirFilter_;
    private IFSFile         f_;
    private IFSFileFilter   fileFilter_;
    private String          path_;
    private VIFSSandbox     sandbox_;



/**
Constructor.
**/
    public RIFSFileListSelectionTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RIFSFileListSelectionTestcase",
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
        sandbox_ = new VIFSSandbox(systemObject_, "RIFSFLST");
        f_ = sandbox_.createDirectory("MILWAUKEE");

        sandbox_.createNumberedDirectoriesAndFiles(f_.getName(), 25, 25);
        path_ = f_.getPath();

        // This creates 25 directories, D0, D1, ... D24
        // and 25 files, F0, F1, ... F24.

        dirFilter_ = new IFSFileFilter() {
                public boolean accept(IFSFile f) {
                    try {
                        return f.isDirectory();
                    }
                    catch(Exception e) {
                        return false;
                    }
                }
            };

            fileFilter_ = new IFSFileFilter() {
                    public boolean accept(IFSFile f) {
                        try {
                            return f.isFile();
                        }
                        catch(Exception e) {
                            return false;
                        }
                    }
                };
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
Checks a particular selection meta data.
**/
    static boolean verifySelectionMetaData(ResourceMetaData smd, 
                                            Object attributeID, 
                                            Class<?> attributeType,
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
                                            Class<?> attributeType,
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
            RIFSFileList u = new RIFSFileList();
            ResourceMetaData[] smd = u.getSelectionMetaData();
            assertCondition((smd.length == 2) 
                   && (verifySelectionMetaData(smd, RIFSFileList.FILTER, IFSFileFilter.class, false, 0, null, false, false))
                   && (verifySelectionMetaData(smd, RIFSFileList.PATTERN, String.class, false, 0, null, false, false)));
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
            RIFSFileList u = new RIFSFileList();
            ResourceMetaData smd = u.getSelectionMetaData(null);
            failed ("Didn't throw exception"+smd);
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
            RIFSFileList u = new RIFSFileList();
            ResourceMetaData smd = u.getSelectionMetaData(new Date());
            failed ("Didn't throw exception"+smd);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getSelectionMetaData() with 1 parameter - Pass FILTER.
**/
    public void Var004()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            ResourceMetaData smd = u.getSelectionMetaData(RIFSFileList.FILTER);
            assertCondition(verifySelectionMetaData(smd, RIFSFileList.FILTER, IFSFileFilter.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
getSelectionMetaData() with 1 parameter - Pass PATTERN.
**/
    public void Var005()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            ResourceMetaData smd = u.getSelectionMetaData(RIFSFileList.PATTERN);
            assertCondition(verifySelectionMetaData(smd, RIFSFileList.PATTERN, String.class, false, 0, null, false, false));
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
            RIFSFileList u = new RIFSFileList();
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
            RIFSFileList u = new RIFSFileList();
            Object filter = u.getSelectionValue(RIFSFileList.FILTER);
            Object pattern = u.getSelectionValue(RIFSFileList.PATTERN);
            assertCondition((filter == null) && (pattern == null));
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
            RIFSFileList u = new RIFSFileList(system, path_);
            Object filter = u.getSelectionValue(RIFSFileList.FILTER);
            Object pattern = u.getSelectionValue(RIFSFileList.PATTERN);
            assertCondition((filter == null) && (pattern == null));
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
            RIFSFileList u = new RIFSFileList(systemObject_, path_);
            Object value = u.getSelectionValue(null);
            failed ("Didn't throw exception"+value);
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
            RIFSFileList u = new RIFSFileList(systemObject_, path_);
            Object value = u.getSelectionValue("Yo");
            failed ("Didn't throw exception"+value);
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
            RIFSFileList u = new RIFSFileList(systemObject_, path_);
            Object filter = u.getSelectionValue(RIFSFileList.FILTER);
            Object pattern = u.getSelectionValue(RIFSFileList.PATTERN);
            assertCondition((filter == null) && (pattern == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSelectionValue() - Pass an selection ID, whose value has been changed using
the new methods.
**/
    public void Var012()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, path_);
            u.setSelectionValue(RIFSFileList.FILTER, dirFilter_);
            u.setSelectionValue(RIFSFileList.PATTERN, "D1*");

            Object filter = u.getSelectionValue(RIFSFileList.FILTER);
            Object pattern = u.getSelectionValue(RIFSFileList.PATTERN);
            assertCondition((filter.equals(dirFilter_) && (pattern.equals("D1*"))));
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
            RIFSFileList u = new RIFSFileList(systemObject_, path_);
            u.setSelectionValue(RIFSFileList.FILTER, dirFilter_);
            u.setSelectionValue(RIFSFileList.PATTERN, "*");
            ResourceMetaData[] smd = u.getSelectionMetaData();
            boolean success = true;
            for(int i = 0; i < smd.length; ++i) {
                // System.out.println("Getting selection " + smd[i] + ".");
                Object value = u.getSelectionValue(smd[i].getID());
                Class<?> valueClass = value.getClass();
                Class<?> type = smd[i].getType();

                // Validate the type.
                if (smd[i].areMultipleAllowed()) {
                    if (!valueClass.isArray()) {
                        System.out.println("Error getting selection " + smd[i] + ".");
                        System.out.println("Type array mismatch: " + valueClass + " is not an array, "
                                           + "but multiple values are allowed.");
                        success = false;
                    }
                    else {
                        Class<?> componentType = valueClass.getComponentType();
                        if (!componentType.equals(type)) {
                            System.out.println("Error getting selection " + smd[i] + ".");
                            System.out.println("Type mismatch: " + componentType + " != " + type + ".");
                            success = false;
                        }
                    }
                }
                else if (!type.isAssignableFrom(valueClass)) {
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
            RIFSFileList u = new RIFSFileList();
            u.setSelectionValue(RIFSFileList.FILTER, fileFilter_);
            u.setSelectionValue(RIFSFileList.PATTERN, "F1*");
            assertCondition((u.getSelectionValue(RIFSFileList.FILTER).equals(fileFilter_)) 
                   && (u.getSelectionValue(RIFSFileList.PATTERN).equals("F1*")));
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
            RIFSFileList u = new RIFSFileList(systemObject_, path_);
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
            RIFSFileList u = new RIFSFileList(systemObject_, path_);
            // Set to something else first.
            u.setSelectionValue(RIFSFileList.FILTER, dirFilter_);
            u.setSelectionValue(RIFSFileList.PATTERN, "D1*");

            u.setSelectionValue(RIFSFileList.FILTER, null);
            u.setSelectionValue(RIFSFileList.PATTERN, null);
            assertCondition((u.getSelectionValue(RIFSFileList.FILTER) == null) 
                   && (u.getSelectionValue(RIFSFileList.PATTERN) == null));
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
            RIFSFileList u = new RIFSFileList(systemObject_, path_);
            u.setSelectionValue(u, "This is a test");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setSelectionValue() - Set a read only selection.
**/
    public void Var018()
    {
            /* There are no read only selections in RIFSFileList! */
            succeeded();
    }



/**
setSelectionValue() - Pass a value which is the wrong type.
**/
    public void Var019()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, path_);
            u.setSelectionValue(RIFSFileList.PATTERN, new Integer(4));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setSelectionValue() - Pass a value which is not one of the possible values.
**/
    public void Var020()
    {
        /* There are no selections with possible values in RIFSFileList! */
        succeeded();
    }



/**
setSelectionValue() - When the list is already open.  This also tests setting
both a filter and a pattern.
**/
    public void Var021()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, path_);

            ul.open();
            ul.waitForComplete();
            long before = ul.getListLength();

            ul.setSelectionValue(RIFSFileList.FILTER, fileFilter_);
            ul.setSelectionValue(RIFSFileList.PATTERN, "F2*");

            ul.refreshContents();
            ul.waitForComplete();
            long after = ul.getListLength();

            RIFSFile f2 = (RIFSFile)ul.resourceAt(0);
            RIFSFile f20 = (RIFSFile)ul.resourceAt(1);
            RIFSFile f21 = (RIFSFile)ul.resourceAt(2);
            RIFSFile f22 = (RIFSFile)ul.resourceAt(3);
            RIFSFile f23 = (RIFSFile)ul.resourceAt(4);
            RIFSFile f24 = (RIFSFile)ul.resourceAt(5);

            assertCondition((before == 50) && (after == 6)
                   && (((String)f2.getAttributeValue(RIFSFile.NAME)).equals("F2"))
                   && (((String)f20.getAttributeValue(RIFSFile.NAME)).equals("F20"))
                   && (((String)f21.getAttributeValue(RIFSFile.NAME)).equals("F21"))
                   && (((String)f22.getAttributeValue(RIFSFile.NAME)).equals("F22"))
                   && (((String)f23.getAttributeValue(RIFSFile.NAME)).equals("F23"))
                   && (((String)f24.getAttributeValue(RIFSFile.NAME)).equals("F24")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - When the list is not already open.  This also tests setting
a filter but no pattern.
**/
    public void Var022()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, path_);

            ul.setSelectionValue(RIFSFileList.FILTER, dirFilter_);

            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();

            boolean success = true;
            for(int i = 0; i < 25; ++i) {
                RIFSFile f = (RIFSFile)ul.resourceAt(i);
                if (!((String)f.getAttributeValue(RIFSFile.NAME)).equals("D" + i))
                    success = false;
            }

            assertCondition((length == 25) && (success == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - When the list is not already open.  This also tests setting
a pattern but no filter.
**/
    public void Var023()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, path_);

            ul.setSelectionValue(RIFSFileList.PATTERN, "?14");

            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();

            RIFSFile d14 = (RIFSFile)ul.resourceAt(0);
            RIFSFile f14 = (RIFSFile)ul.resourceAt(1);

            assertCondition((length == 2)
                   && (((String)f14.getAttributeValue(RIFSFile.NAME)).equals("F14"))
                   && (((String)d14.getAttributeValue(RIFSFile.NAME)).equals("D14")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




