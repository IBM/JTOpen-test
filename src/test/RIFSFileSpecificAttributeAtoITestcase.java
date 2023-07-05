///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RIFSFileSpecificAttributeAtoITestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.resource.ResourceMetaData;
import com.ibm.as400.resource.RIFSFile;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Hashtable;



/**
Testcase RIFSFileSpecificAttributeAtoITestcase.  This tests the following attributes
of the RIFSFile class:

<ul>
<li>ABSOLUTE_PATH
<li>CANONICAL_PATH
<li>CAN_READ
<li>CAN_WRITE
<li>CCSID
<li>CREATED
<li>EXISTS
<li>IS_ABSOLUTE
<li>IS_DIRECTORY
<li>IS_FILE
<li>IS_HIDDEN
<li>IS_READ_ONLY
</ul>
**/
public class RIFSFileSpecificAttributeAtoITestcase
extends Testcase {



    // Constants.



    // Private data.
    private AS400           pwrSys_;
    private VIFSSandbox     sandbox_;



/**
Constructor.
**/
    public RIFSFileSpecificAttributeAtoITestcase (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RIFSFileSpecificAttributeAtoITestcase",
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
        sandbox_ = new VIFSSandbox(systemObject_, "RIFSFSAAI");
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
Indicates if 2 dates are within 30 minutes of each other.  This
takes into account a difference between AS/400 time and client time.
**/
    static boolean diff(Date date1, Date date2)
    {
        return (Math.abs(date1.getTime() - date2.getTime()) <= 1800000);
    }



/**
ABSOLUTE_PATH - Check the attribute meta data in the entire list.
**/
    public void Var001()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.ABSOLUTE_PATH, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ABSOLUTE_PATH - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var002()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.ABSOLUTE_PATH);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.ABSOLUTE_PATH, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ABSOLUTE_PATH - Get the attribute value when the file is off the root.
**/
    public void Var003()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One");
            Object value = f.getAttributeValue(RIFSFile.ABSOLUTE_PATH);
            assertCondition(((String)value).equals("/One"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ABSOLUTE_PATH - Get the attribute value when the file is one off the root.
**/
    public void Var004()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One/Two");
            Object value = f.getAttributeValue(RIFSFile.ABSOLUTE_PATH);
            assertCondition(((String)value).equals("/One/Two"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ABSOLUTE_PATH - Get the attribute value when the file is several off the root.
**/
    public void Var005()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One/Two/Three/Four Five/Six");
            Object value = f.getAttributeValue(RIFSFile.ABSOLUTE_PATH);
            assertCondition(((String)value).equals("/One/Two/Three/Four Five/Six"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CANONICAL_PATH - Check the attribute meta data in the entire list.
**/
    public void Var006()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.CANONICAL_PATH, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CANONICAL_PATH - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var007()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.CANONICAL_PATH);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.CANONICAL_PATH, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CANONICAL_PATH - Get the attribute value when the file is off the root.
**/
    public void Var008()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One");
            Object value = f.getAttributeValue(RIFSFile.CANONICAL_PATH);
            assertCondition(((String)value).equals("/One"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CANONICAL_PATH - Get the attribute value when the file is one off the root.
**/
    public void Var009()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One/Two");
            Object value = f.getAttributeValue(RIFSFile.CANONICAL_PATH);
            assertCondition(((String)value).equals("/One/Two"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CANONICAL_PATH - Get the attribute value when the file is several off the root.
**/
    public void Var010()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One/Two/Three/Four Five/Six");
            Object value = f.getAttributeValue(RIFSFile.CANONICAL_PATH);
            assertCondition(((String)value).equals("/One/Two/Three/Four Five/Six"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CAN_READ - Check the attribute meta data in the entire list.
**/
    public void Var011()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.CAN_READ, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CAN_READ - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var012()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.CAN_READ);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.CAN_READ, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CAN_READ - Get the attribute value when the file is readable.
**/
    public void Var013()
    {
        try {
            IFSFile f = sandbox_.createFile("canread");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.CAN_READ);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CAN_READ - Get the attribute value when the file is not readable.
**/
    public void Var014()
    {
        try {
            IFSFile f = sandbox_.createFile("cantread");

            // Open the file with another AS/400 object - this will lock it.
            IFSFileInputStream i = new IFSFileInputStream(pwrSys_, f.getPath(), IFSFileInputStream.SHARE_NONE);

            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.CAN_READ);

            i.close();

            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CAN_READ - Get the attribute value when the file is a directory.
**/
    public void Var015()
    {
        try {
            IFSFile f = sandbox_.createDirectory("canreaddir");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.CAN_READ);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CAN_READ - Get the attribute value when the file does not exist.
**/
    public void Var016()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = f1.getAttributeValue(RIFSFile.CAN_READ);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CAN_WRITE - Check the attribute meta data in the entire list.
**/
    public void Var017()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.CAN_WRITE, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CAN_WRITE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var018()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.CAN_WRITE);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.CAN_WRITE, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CAN_WRITE - Get the attribute value when the file is writeable.
**/
    public void Var019()
    {
        try {
            IFSFile f = sandbox_.createFile("canwrite");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.CAN_WRITE);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CAN_WRITE - Get the attribute value when the file is not writeable.
**/
    public void Var020()
    {
        try {
            IFSFile f = sandbox_.createFile("cantwrite");

            // Open the file with another AS/400 object - this will lock it.
            IFSFileInputStream i = new IFSFileInputStream(pwrSys_, f.getPath(), IFSFileInputStream.SHARE_NONE);

            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.CAN_WRITE);

            i.close();

            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CAN_WRITE - Get the attribute value when the file is a directory.
**/
    public void Var021()
    {
        try {
            IFSFile f = sandbox_.createDirectory("canwritedir");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.CAN_WRITE);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CAN_WRITE - Get the attribute value when the file does not exist.
**/
    public void Var022()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = f1.getAttributeValue(RIFSFile.CAN_WRITE);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CCSID - Check the attribute meta data in the entire list.
**/
    public void Var023()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.CCSID, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CCSID - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var024()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.CCSID);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.CCSID, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CCSID - Get the attribute value when the file exists.
**/
    public void Var025()
    {
        try {
            IFSFile f = sandbox_.createFile("checkccsid");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.CCSID);
            assertCondition(((Integer)value).intValue() == f.getCCSID());
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CCSID - Get the attribute value when the file is a directory.
**/
    public void Var026()
    {
        try {
            IFSFile f = sandbox_.createDirectory("ccsiddir");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.CCSID);
            assertCondition(((Integer)value).intValue() > 0, "Change April 2017 -- CCSID expected, but got "+ ((Integer)value).intValue());
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CCSID - Get the attribute value when the file does not exist.
**/
    public void Var027()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = f1.getAttributeValue(RIFSFile.CCSID);
            assertCondition(((Integer)value).intValue() == -1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CREATED - Check the attribute meta data in the entire list.
**/
    public void Var028()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.CREATED, Date.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CREATED - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var029()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.CREATED);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.CREATED, Date.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CREATED - Get the attribute value when the file exists.
**/
    public void Var030()
    {
        try {
            IFSFile f = sandbox_.createFile("created");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.CREATED);
            long curSysValTime = getSysValTime(systemObject_); //Testcase.getSysValTime()
            Date createTime = (Date)value;
            long difference = Math.abs(createTime.getTime() - curSysValTime);
            assertCondition(difference < 10000, "The difference is "+difference); // 10 Seconds
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CREATED - Get the attribute value when the file is a directory.
**/
    public void Var031()
    {
        try {
            IFSFile f = sandbox_.createDirectory("createddir");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.CREATED);
            long curSysValTime = getSysValTime(systemObject_);//Testcase.getSysValTime()
            Date createTime = (Date)value;
            long difference = Math.abs(createTime.getTime() - curSysValTime);
            assertCondition(difference < 10000, "The difference is "+difference ); // 10 Seconds
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CREATED - Get the attribute value when the file does not exist.
**/
    public void Var032()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = f1.getAttributeValue(RIFSFile.CREATED);
            assertCondition(((Date)value).getTime() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
EXISTS - Check the attribute meta data in the entire list.
**/
    public void Var033()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.EXISTS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
EXISTS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var034()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.EXISTS);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.EXISTS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
EXISTS - Get the attribute value when the file exists.
**/
    public void Var035()
    {
        try {
            IFSFile f = sandbox_.createFile("created");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.EXISTS);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
EXISTS - Get the attribute value when the file is a directory.
**/
    public void Var036()
    {
        try {
            IFSFile f = sandbox_.createDirectory("createddir");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.EXISTS);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
EXISTS - Get the attribute value when the file does not exist.
**/
    public void Var037()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = f1.getAttributeValue(RIFSFile.EXISTS);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_ABSOLUTE - Check the attribute meta data in the entire list.
**/
    public void Var038()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.IS_ABSOLUTE, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_ABSOLUTE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var039()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.IS_ABSOLUTE);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.IS_ABSOLUTE, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_ABSOLUTE - Get the attribute value when the file is absolute and has only one node.
**/
    public void Var040()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "/One");
            Object value = f1.getAttributeValue(RIFSFile.IS_ABSOLUTE);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_ABSOLUTE - Get the attribute value when the file is absolute and has two nodes.
**/
    public void Var041()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "/One/Two");
            Object value = f1.getAttributeValue(RIFSFile.IS_ABSOLUTE);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_ABSOLUTE - Get the attribute value when the file is absolute and has multiple nodes.
**/
    public void Var042()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "/One/Two/Three/Four/Five Six");
            Object value = f1.getAttributeValue(RIFSFile.IS_ABSOLUTE);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_ABSOLUTE - Get the attribute value when the file is relative and has only one node.
**/
    public void Var043()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "One");
            Object value = f1.getAttributeValue(RIFSFile.IS_ABSOLUTE);
            assertCondition(((Boolean)value).booleanValue() == true); // IFS forces absolute now.
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_ABSOLUTE - Get the attribute value when the file is absolute and has two nodes.
**/
    public void Var044()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "One/Two");
            Object value = f1.getAttributeValue(RIFSFile.IS_ABSOLUTE);
            assertCondition(((Boolean)value).booleanValue() == true); // IFS forces absolute now.
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_ABSOLUTE - Get the attribute value when the file is absolute and has multiple nodes.
**/
    public void Var045()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "One/Two/Three/Four/Five Six");
            Object value = f1.getAttributeValue(RIFSFile.IS_ABSOLUTE);
            assertCondition(((Boolean)value).booleanValue() == true); // IFS forces absolute now.
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_DIRECTORY - Check the attribute meta data in the entire list.
**/
    public void Var046()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.IS_DIRECTORY, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_DIRECTORY - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var047()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.IS_DIRECTORY);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.IS_DIRECTORY, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_DIRECTORY - Get the attribute value when the file exists.
**/
    public void Var048()
    {
        try {
            IFSFile f = sandbox_.createFile("isdir");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.IS_DIRECTORY);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_DIRECTORY - Get the attribute value when the file is a directory.
**/
    public void Var049()
    {
        try {
            IFSFile f = sandbox_.createDirectory("isdir2");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.IS_DIRECTORY);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_DIRECTORY - Get the attribute value when the file does not exist.
**/
    public void Var050()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = f1.getAttributeValue(RIFSFile.IS_DIRECTORY);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_FILE - Check the attribute meta data in the entire list.
**/
    public void Var051()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.IS_FILE, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_FILE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var052()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.IS_FILE);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.IS_FILE, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_FILE - Get the attribute value when the file exists.
**/
    public void Var053()
    {
        try {
            IFSFile f = sandbox_.createFile("isfile");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.IS_FILE);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_FILE - Get the attribute value when the file is a directory.
**/
    public void Var054()
    {
        try {
            IFSFile f = sandbox_.createDirectory("isfile2");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.IS_FILE);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_FILE - Get the attribute value when the file does not exist.
**/
    public void Var055()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = f1.getAttributeValue(RIFSFile.IS_FILE);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_HIDDEN - Check the attribute meta data in the entire list.
**/
    public void Var056()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.IS_HIDDEN, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_HIDDEN - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var057()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.IS_HIDDEN);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.IS_HIDDEN, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_HIDDEN - Get the attribute value when the file exists and is not hidden.
**/
    public void Var058()
    {
        try {
            IFSFile f = sandbox_.createFile("isnothidden");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_HIDDEN - Get the attribute value when the file exists and is hidden.
**/
    public void Var059()
    {
        try {
            IFSFile f = sandbox_.createFile("ishidden");
            f.setHidden(true);
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_HIDDEN - Get the attribute value when the file is a directory that is not hidden.
**/
    public void Var060()
    {
        try {
            IFSFile f = sandbox_.createDirectory("isnothidden2");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_HIDDEN - Get the attribute value when the file is a directory that is hidden.
**/
    public void Var061()
    {
        try {
            IFSFile f = sandbox_.createDirectory("ishidden2");
            f.setHidden(true);
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_HIDDEN - Get the attribute value when the file does not exist.
**/
    public void Var062()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = f1.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_HIDDEN - Set the attribute value for a file to true.
**/
    public void Var063()
    {
        try {
            IFSFile f = sandbox_.createFile("ishidden3");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            f1.setAttributeValue(RIFSFile.IS_HIDDEN, Boolean.TRUE);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Object value = f2.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_HIDDEN - Set the attribute value for a file to false.
**/
    public void Var064()
    {
        try {
            IFSFile f = sandbox_.createFile("ishidden3a");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            f1.setAttributeValue(RIFSFile.IS_HIDDEN, Boolean.FALSE);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Object value = f2.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_HIDDEN - Set the attribute value for a directory to true.
**/
    public void Var065()
    {
        try {
            IFSFile f = sandbox_.createDirectory("ishidden4");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            f1.setAttributeValue(RIFSFile.IS_HIDDEN, Boolean.TRUE);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Object value = f2.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_HIDDEN - Set the attribute value for a directory to false.
**/
    public void Var066()
    {
        try {
            IFSFile f = sandbox_.createDirectory("ishidden4a");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            f1.setAttributeValue(RIFSFile.IS_HIDDEN, Boolean.FALSE);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Object value = f2.getAttributeValue(RIFSFile.IS_HIDDEN);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_HIDDEN - Set the attribute value when the file does not exist.
**/
    public void Var067()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            f1.setAttributeValue(RIFSFile.IS_HIDDEN, Boolean.TRUE);
            f1.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
IS_READ_ONLY - Check the attribute meta data in the entire list.
**/
    public void Var068()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.IS_READ_ONLY, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_READ_ONLY - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var069()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.IS_READ_ONLY);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.IS_READ_ONLY, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_READ_ONLY - Get the attribute value when the file exists and is not read only.
**/
    public void Var070()
    {
        try {
            IFSFile f = sandbox_.createFile("isnotread only");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.IS_READ_ONLY);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_READ_ONLY - Get the attribute value when the file exists and is read only.
**/
    public void Var071()
    {
        try {
            IFSFile f = sandbox_.createFile("isread only");
            f.setReadOnly(true);
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.IS_READ_ONLY);
            assertCondition(((Boolean)value).booleanValue() == true, "is readonly returned true");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_READ_ONLY - Get the attribute value when the file is a directory that is not read only.
**/
    public void Var072()
    {
        try {
            IFSFile f = sandbox_.createDirectory("isnotread only2");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.IS_READ_ONLY);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_READ_ONLY - Get the attribute value when the file is a directory that is read only.
**/
    public void Var073()
    {
        try {
            IFSFile f = sandbox_.createDirectory("isread only2");
            f.setReadOnly(true);
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.IS_READ_ONLY);
            assertCondition(((Boolean)value).booleanValue() == true, "RIFSFile.IS_READ_ONLY returned false");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_READ_ONLY - Get the attribute value when the file does not exist.
**/
    public void Var074()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = f1.getAttributeValue(RIFSFile.IS_READ_ONLY);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_READ_ONLY - Set the attribute value for a file to true.
**/
    public void Var075()
    {
        try {
            IFSFile f = sandbox_.createFile("isread only3");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            f1.setAttributeValue(RIFSFile.IS_READ_ONLY, Boolean.TRUE);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Object value = f2.getAttributeValue(RIFSFile.IS_READ_ONLY);
            assertCondition(((Boolean)value).booleanValue() == true, "RIFSFile.IS_READ_ONLY returned false");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_READ_ONLY - Set the attribute value for a file to false.
**/
    public void Var076()
    {
        try {
            IFSFile f = sandbox_.createFile("isread only3a");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            f1.setAttributeValue(RIFSFile.IS_READ_ONLY, Boolean.FALSE);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Object value = f2.getAttributeValue(RIFSFile.IS_READ_ONLY);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_READ_ONLY - Set the attribute value for a directory to true.
**/
    public void Var077()
    {
        try {
            IFSFile f = sandbox_.createDirectory("isread only4");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            f1.setAttributeValue(RIFSFile.IS_READ_ONLY, Boolean.TRUE);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Object value = f2.getAttributeValue(RIFSFile.IS_READ_ONLY);
            assertCondition(((Boolean)value).booleanValue() == true, "RIFSFile.IS_READ_ONLY returned false");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_READ_ONLY - Set the attribute value for a directory to false.
**/
    public void Var078()
    {
        try {
            IFSFile f = sandbox_.createDirectory("isread only4a");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            f1.setAttributeValue(RIFSFile.IS_READ_ONLY, Boolean.FALSE);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Object value = f2.getAttributeValue(RIFSFile.IS_READ_ONLY);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
IS_READ_ONLY - Set the attribute value when the file does not exist.
**/
    public void Var079()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            f1.setAttributeValue(RIFSFile.IS_READ_ONLY, Boolean.TRUE);
            f1.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



}




