///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RIFSFileListSortTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RIFS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;

import com.ibm.as400.resource.RIFSFile;
import com.ibm.as400.resource.RIFSFileList;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase RIFSFileListSortTestcase.  This tests the following methods
of the RIFSFileList class, some inherited from BufferedResourceList:

<ul>
<li>getSortMetaData() 
<li>getSortMetaData() 
<li>getSortOrder() 
<li>getSortValue() 
<li>setSortOrder() 
<li>setSortValue() 
</ul>
**/
public class RIFSFileListSortTestcase
extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RIFSFileListSortTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.RIFSTest.main(newArgs); 
   }



    // Constants.



    // Private data.



/**
Constructor.
**/
    public RIFSFileListSortTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RIFSFileListSortTestcase",
               namesAndVars, runMode, fileOutputStream,
               password);
        pwrSys_ = pwrSys;

        if (pwrSys == null)
            throw new IllegalStateException("ERROR: Please specify a power system.");
    }



/**
getSortMetaData() with 0 parameters - Verify that the array contains no sorts.
**/
    public void Var001()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            ResourceMetaData[] smd = u.getSortMetaData();
            assertCondition(smd.length == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSortMetaData() with 1 parameter - Pass null.
**/
    public void Var002()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            ResourceMetaData smd = u.getSortMetaData(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getSortMetaData() with 1 parameter - Pass an invalid attribute ID.
**/
    public void Var003()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            ResourceMetaData smd = u.getSortMetaData(new Date());
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getSortOrder() - Pass null.
**/
    public void Var004()
    {
        try {
            RIFSFileList u = new RIFSFileList(pwrSys_, "dummy");
            u.getSortOrder(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getSortOrder() - Pass an invalid sort ID.
**/
    public void Var005()
    {
        try {
            RIFSFileList u = new RIFSFileList(pwrSys_, "dummy");
            u.getSortOrder("Yo");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getSortValue() - Try it.
**/
    public void Var006()
    {
        try {
            RIFSFileList u = new RIFSFileList(pwrSys_, "dummy");
            Object[] sortValue = u.getSortValue();
            assertCondition(sortValue.length == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSortOrder() - Pass null for the sort ID.
**/
    public void Var007()
    {
        try {
            RIFSFileList u = new RIFSFileList(pwrSys_, "dummy");
            u.setSortOrder(null, true);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setSortOrder() - Pass an invalid sort ID.
**/
    public void Var008()
    {
        try {
            RIFSFileList u = new RIFSFileList(pwrSys_, "dummy");
            u.setSortOrder(u, false);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setSortValue() - Pass null.
**/
    public void Var009()
    {
        try {
            RIFSFileList u = new RIFSFileList(pwrSys_, "dummy");
            u.setSortValue(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setSortOrder() - Pass an invalid sort ID.
**/
    public void Var010()
    {
        try {
            RIFSFileList u = new RIFSFileList(pwrSys_, "dummy");
            u.setSortValue(new Object[] { "Yo" });
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setSortOrder() - Pass an empty array.
**/
    public void Var011()
    {
        try {
            RIFSFileList u = new RIFSFileList(pwrSys_, "/QIBM");
            u.setSortValue(new Object[0]);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            for(int i = 0; i < length; ++i)
                u.resourceAt(i);
            assertCondition(length > 3);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




