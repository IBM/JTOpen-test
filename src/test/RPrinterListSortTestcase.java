///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrinterListSortTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceMetaData;
import com.ibm.as400.resource.RPrinter;
import com.ibm.as400.resource.RPrinterList;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;



/**
Testcase RPrinterListSortTestcase.  This tests the following methods
of the RPrinterList class, some inherited from BufferedResourceList:

<ul>
<li>getSortMetaData() 
<li>getSortMetaData() 
<li>getSortOrder() 
<li>getSortValue() 
<li>setSortOrder() 
<li>setSortValue() 
</ul>
**/
public class RPrinterListSortTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String printerName_;



/**
Constructor.
**/
    public RPrinterListSortTestcase (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys,
                              String misc)
    {
        super (systemObject, "RPrinterListSortTestcase",
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
getSortMetaData() with 0 parameters - Verify that the array contains no sorts.
**/
    public void Var001()
    {
        try {
            RPrinterList u = new RPrinterList();
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
            RPrinterList u = new RPrinterList();
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
            RPrinterList u = new RPrinterList();
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
            RPrinterList u = new RPrinterList(pwrSys_);
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
            RPrinterList u = new RPrinterList(pwrSys_);
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
            RPrinterList u = new RPrinterList(pwrSys_);
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
            RPrinterList u = new RPrinterList(pwrSys_);
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
            RPrinterList u = new RPrinterList(pwrSys_);
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
            RPrinterList u = new RPrinterList(pwrSys_);
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
            RPrinterList u = new RPrinterList(pwrSys_);
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
            RPrinterList u = new RPrinterList(pwrSys_);
            u.setSortValue(new Object[0]);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            for(int i = 0; i < length; ++i)
                u.resourceAt(i);
            assertCondition(length >= 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




