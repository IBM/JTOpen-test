///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RIFSFileSpecificAttributeLtoPTestcase.java
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
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.User;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;
import test.misc.VIFSSandbox;

import com.ibm.as400.resource.RIFSFile;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase RIFSFileSpecificAttributeLtoPTestcase.  This tests the following attributes
of the RIFSFile class:

<ul>
<li>LAST_ACCESSED 
<li>LAST_MODIFIED 
<li>LENGTH 
<li>NAME 
<li>PARENT 
<li>PATH 
</ul>
**/
public class RIFSFileSpecificAttributeLtoPTestcase
extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RIFSFileSpecificAttributeLtoPTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.RIFSTest.main(newArgs); 
   }



    // Constants.



    // Private data.
    private AS400           pwrSys_;
    private VIFSSandbox     sandbox_;



/**
Constructor.
**/
    public RIFSFileSpecificAttributeLtoPTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RIFSFileSpecificAttributeLtoPTestcase",
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
    private static boolean diff(Date date1, Date date2)
    {
        return (Math.abs(date1.getTime() - date2.getTime()) <= 1800000);
    }



/**
LAST_ACCESSED - Check the attribute meta data in the entire list.
**/
    public void Var001()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.LAST_ACCESSED, Date.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_ACCESSED - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var002()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.LAST_ACCESSED);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.LAST_ACCESSED, Date.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_ACCESSED - Get the attribute value when the file exists.
**/
    public void Var003()
    {
        try {
            IFSFile f = sandbox_.createFile("lastaccessed");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.LAST_ACCESSED);
            long curSysValTime = getSysValTime(systemObject_);//Testcase.getSysValTime()
            Date accessTime = (Date)value;
            long difference = Math.abs(accessTime.getTime() - curSysValTime);
            assertCondition(difference < 10000, "Difference between accessTime and system value time is "+difference+" seconds: last accessed date is "+accessTime+" getSysValTime log="+getSysValTimeStringBuffer.toString()); // 10 Seconds

        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_ACCESSED - Get the attribute value when the file is a directory.
**/
    public void Var004()
    {
        try {
            IFSFile f = sandbox_.createDirectory("lastaccessed1");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.LAST_ACCESSED);
            long curSysValTime = getSysValTime(systemObject_);//Testcase.getSysValTime()
            Date accessTime = (Date)value;
            long difference = Math.abs(accessTime.getTime() - curSysValTime);
            assertCondition(difference < 10000, "Difference between time is "+difference); // 10 Seconds
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_ACCESSED - Get the attribute value when the file does not exist.
**/
    public void Var005()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = f1.getAttributeValue(RIFSFile.LAST_ACCESSED);
            assertCondition(((Date)value).getTime() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_ACCESSED - Verify that the attribute value gets updated when the file gets touched.
**/
    public void Var006()
    {
        try {
            IFSFile f = sandbox_.createFile("lastaccessed2");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Date value1 = (Date)f1.getAttributeValue(RIFSFile.LAST_ACCESSED);
            Thread.sleep(1000);
            InputStream fi = new IFSFileInputStream(systemObject_, f.getPath());
            fi.read();
            fi.close();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Date value2 = (Date)f2.getAttributeValue(RIFSFile.LAST_ACCESSED);
            assertCondition((diff((Date)value1, (Date)value2))
                   && (value2.getTime() > value1.getTime()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_MODIFIED - Check the attribute meta data in the entire list.
**/
    public void Var007()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.LAST_MODIFIED, Date.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_MODIFIED - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var008()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.LAST_MODIFIED);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.LAST_MODIFIED, Date.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_MODIFIED - Get the attribute value when the file exists.
**/
    public void Var009()
    {
        try {
            IFSFile f = sandbox_.createFile("lastmodified");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.LAST_MODIFIED);
            long curSysValTime = getSysValTime(systemObject_);//Testcase.getSysValTime()
            Date modifyTime = (Date)value;
            long difference = Math.abs(modifyTime.getTime() - curSysValTime);
            assertCondition(difference < 10000, "The difference is "+difference+" ms "); // 10 Seconds
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_MODIFIED - Get the attribute value when the file is a directory.
**/
    public void Var010()
    {
        try {
            IFSFile f = sandbox_.createDirectory("lastmodified1");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.LAST_MODIFIED);
            long curSysValTime = getSysValTime(systemObject_);//Testcase.getSysValTime()
            Date modifyTime = (Date)value;
            long difference = Math.abs(modifyTime.getTime() - curSysValTime);
            assertCondition(difference < 10000, "The difference is "+difference+" ms"); // 10 Seconds
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_MODIFIED - Get the attribute value when the file does not exist.
**/
    public void Var011()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = f1.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(((Date)value).getTime() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_MODIFIED - Verify that the attribute value does not get updated when the file gets touched.
**/
    public void Var012()
    {
        try {
            IFSFile f = sandbox_.createFile("lastmod2");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Date value1 = (Date)f1.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Thread.sleep(100);
            InputStream f2 = new IFSFileInputStream(systemObject_, f.getPath());
            f2.read();
            f2.close();
            RIFSFile f3 = new RIFSFile(systemObject_, f.getPath());
            Date value2 = (Date)f3.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition((diff((Date)value1, (Date)value2))
                   && (value2.getTime() == value1.getTime()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_MODIFIED - Verify that the attribute value gets updated when the file gets modified.
**/
    public void Var013()
    {
        try {
            IFSFile f = sandbox_.createFile("lastmod3");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Date value1 = (Date)f1.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Thread.sleep(1000);
            OutputStream f2 = new IFSFileOutputStream(systemObject_, f.getPath());
            f2.write((byte)0x45);
            f2.close();
            RIFSFile f3 = new RIFSFile(systemObject_, f.getPath());
            Date value2 = (Date)f3.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition((diff((Date)value1, (Date)value2))
                   && (value2.getTime() > value1.getTime()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LAST_MODIFIED - Set the attribute value when the file exists.
**/
    public void Var014()
    {
        try {
            IFSFile f = sandbox_.createFile("lastmod4");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date now = new Date();
            f1.setAttributeValue(RIFSFile.LAST_MODIFIED, now);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Date now2 = (Date)f2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(now.equals(now2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
LAST_MODIFIED - Set the attribute value when the file is a directory.
**/
    public void Var015()
    {
        try {
            IFSFile f = sandbox_.createDirectory("lastmod5");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date now = new Date();
            f1.setAttributeValue(RIFSFile.LAST_MODIFIED, now);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Date now2 = (Date)f2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(now.equals(now2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
LAST_MODIFIED - Set the attribute value when the file does not exist.
**/
    public void Var016()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            f1.setAttributeValue(RIFSFile.LAST_MODIFIED, new Date());
            f1.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
   }



/**
LAST_MODIFIED - Set the attribute value to date earlier than 1900.
**/
    public void Var017()
    {
        try {
            IFSFile f = sandbox_.createFile("lastmod6");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date now = new Date(-32443784723l);
            f1.setAttributeValue(RIFSFile.LAST_MODIFIED, now);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Date now2 = (Date)f2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }




/**
LAST_MODIFIED - Set the attribute value to date earlier than 1970.
**/
    public void Var018()
    {
        try {
            IFSFile f = sandbox_.createFile("lastmod6");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date now = new Date(-43523);
            f1.setAttributeValue(RIFSFile.LAST_MODIFIED, now);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Date now2 = (Date)f2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }




/**
LAST_MODIFIED - Set the attribute value to 0.
**/
    public void Var019()
    {
        try {
            IFSFile f = sandbox_.createFile("lastmod6");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Date original = (Date)f1.getAttributeValue(RIFSFile.LAST_MODIFIED);
            Thread.sleep(1000);
            Date now = new Date(0);
            f1.setAttributeValue(RIFSFile.LAST_MODIFIED, now);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Date now2 = (Date)f2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(original.equals(now2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
LAST_MODIFIED - Set the attribute value to date after than 1970.
**/
    public void Var020()
    {
        try {
            IFSFile f = sandbox_.createFile("lastmod6");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date now = new Date(System.currentTimeMillis() - 10000000);
            f1.setAttributeValue(RIFSFile.LAST_MODIFIED, now);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Date now2 = (Date)f2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(now.equals(now2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
LAST_MODIFIED - Set the attribute value to date in the future.
**/
    public void Var021()
    {
        try {
            IFSFile f = sandbox_.createFile("lastmod6");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Thread.sleep(1000);
            Date now = new Date(System.currentTimeMillis() + 10000000);
            f1.setAttributeValue(RIFSFile.LAST_MODIFIED, now);
            f1.commitAttributeChanges();
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            Date now2 = (Date)f2.getAttributeValue(RIFSFile.LAST_MODIFIED);
            assertCondition(now.equals(now2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
LENGTH - Check the attribute meta data in the entire list.
**/
    public void Var022()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.LENGTH, Long.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LENGTH - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var023()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.LENGTH);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.LENGTH, Long.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LENGTH - Get the attribute value when the file exists and is length 0.
**/
    public void Var024()
    {
        try {
            IFSFile f = sandbox_.createFile("length0");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.LENGTH);
            assertCondition(((Long)value).longValue() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LENGTH - Get the attribute value when the file exists and is length 1.
**/
    public void Var025()
    {
        try {
            IFSFile f = sandbox_.createFile("length1");
            sandbox_.appendFile(f, 1);
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.LENGTH);
            assertCondition(((Long)value).longValue() == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LENGTH - Get the attribute value when the file exists and is length 10000.
**/
    public void Var026()
    {
        try {
            IFSFile f = sandbox_.createFile("length10000");
            sandbox_.appendFile(f, 10000);
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.LENGTH);
            assertCondition(((Long)value).longValue() == 10000);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LENGTH - Get the attribute value when the file is a directory.
**/
    public void Var027()
    {
        try {
            IFSFile f = sandbox_.createDirectory("lengthdir");
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.LENGTH);
            assertCondition(((Long)value).longValue() > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LENGTH - Get the attribute value when the file does not exist.
**/
    public void Var028()
    {
        try {
            RIFSFile f1 = new RIFSFile(systemObject_, "NOTEXIST");
            Object value = f1.getAttributeValue(RIFSFile.LENGTH);
            assertCondition(((Long)value).longValue() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NAME - Check the attribute meta data in the entire list.
**/
    public void Var029()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NAME - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var030()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.NAME);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NAME - Get the attribute value when the file is off the root.
**/
    public void Var031()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One");
            Object value = f.getAttributeValue(RIFSFile.NAME);
            assertCondition(((String)value).equals("One"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NAME - Get the attribute value when the file is one off the root.
**/
    public void Var032()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One/Two");
            Object value = f.getAttributeValue(RIFSFile.NAME);
            assertCondition(((String)value).equals("Two"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NAME - Get the attribute value when the file is several off the root.
**/
    public void Var033()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One/Two/Three/Four Five/Six");
            Object value = f.getAttributeValue(RIFSFile.NAME);
            assertCondition(((String)value).equals("Six"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PARENT - Check the attribute meta data in the entire list.
**/
    public void Var034()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.PARENT, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PARENT - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var035()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.PARENT);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.PARENT, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PARENT - Get the attribute value when the file is off the root.
**/
    public void Var036()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One");
            Object value = f.getAttributeValue(RIFSFile.PARENT);
            assertCondition(((String)value).equals("/"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PARENT - Get the attribute value when the file is one off the root.
**/
    public void Var037()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One/Two");
            Object value = f.getAttributeValue(RIFSFile.PARENT);
            assertCondition(((String)value).equals("/One"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PARENT - Get the attribute value when the file is several off the root.
**/
    public void Var038()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One/Two/Three/Four Five/Six");
            Object value = f.getAttributeValue(RIFSFile.PARENT);
            assertCondition(((String)value).equals("/One/Two/Three/Four Five"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PATH - Check the attribute meta data in the entire list.
**/
    public void Var039()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.PATH, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PATH - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var040()
    {
        try {
            RIFSFile u = new RIFSFile();
            ResourceMetaData amd = u.getAttributeMetaData(RIFSFile.PATH);
            assertCondition(RIFSFileGenericAttributeTestcase.verifyAttributeMetaData(amd, RIFSFile.PATH, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PATH - Get the attribute value when the file is off the root.
**/
    public void Var041()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One");
            Object value = f.getAttributeValue(RIFSFile.PATH);
            assertCondition(((String)value).equals("/One"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PATH - Get the attribute value when the file is one off the root.
**/
    public void Var042()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One/Two");
            Object value = f.getAttributeValue(RIFSFile.PATH);
            assertCondition(((String)value).equals("/One/Two"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PATH - Get the attribute value when the file is several off the root.
**/
    public void Var043()
    {
        try {
            RIFSFile f = new RIFSFile(systemObject_, "/One/Two/Three/Four Five/Six");
            Object value = f.getAttributeValue(RIFSFile.PATH);
            assertCondition(((String)value).equals("/One/Two/Three/Four Five/Six"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OWNERID - Get the attribute value when the file exists.
**/
    public void Var044()
    {
        try {
            IFSFile f = sandbox_.createFile("ownerid");
            sandbox_.appendFile(f, 1);
            RIFSFile f1 = new RIFSFile(systemObject_, f.getPath());
            Object value = f1.getAttributeValue(RIFSFile.OWNERID);
            User user = new User(systemObject_, systemObject_.getUserId());
            int uid = user.getUserIDNumber();
            int valueInt = ((Integer)value).intValue();
            assertCondition(valueInt==uid);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OWNERID - Get the attribute value when the file does not exist.
**/
    public void Var045()
    {
        try {
            ///IFSFile f = sandbox_.createFile("ownerid");
            ///sandbox_.appendFile(f, 1);
            RIFSFile f1 = new RIFSFile(systemObject_, "boogiewoogie");
            Object value = f1.getAttributeValue(RIFSFile.OWNERID);
            int valueInt = ((Integer)value).intValue();
            assertCondition(valueInt==-1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




