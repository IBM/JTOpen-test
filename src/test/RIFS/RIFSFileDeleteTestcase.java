///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RIFSFileDeleteTestcase.java
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
import com.ibm.as400.resource.RIFSFile;

import test.Testcase;
import test.misc.VIFSSandbox;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Hashtable;



/**
Testcase RIFSFileDeleteTestcase.  This tests the following methods
of the RIFSFile class:

<ul>
<li>delete
</ul>
**/
public class RIFSFileDeleteTestcase
extends Testcase {



    // Constants.



    // Private data.
    private VIFSSandbox     sandbox_;



/**
Constructor.
**/
    public RIFSFileDeleteTestcase (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RIFSFileDeleteTestcase",
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
        sandbox_ = new VIFSSandbox(systemObject_, "RIFSDTC");
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
delete() - when no system or path is specified.
**/
    public void Var001()
    {
        try {
            RIFSFile u = new RIFSFile();
            u.delete();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
delete() - when a bad system is specified.
**/
    public void Var002()
    {
        try {
            AS400 system = new AS400("bogus", "bogus", "bogus");
            RIFSFile u = new RIFSFile(system, "/CNOCK/YO");
            u.delete();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
delete() - when a bad file is specified.
**/
    public void Var003()
    {
        try {
            RIFSFile u = new RIFSFile(systemObject_, "/NOT/EXIST");
            u.delete();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
delete() - when an existing file is specified.
**/
    public void Var004()
    {
        try {
            IFSFile f = sandbox_.createFile("Maine");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            boolean before = ((Boolean)u.getAttributeValue(RIFSFile.EXISTS)).booleanValue();
            u.delete();
            u.refreshAttributeValues();
            boolean after = ((Boolean)u.getAttributeValue(RIFSFile.EXISTS)).booleanValue();
            assertCondition((before == true) && (after == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
delete() - when an existing empty directory is specified.
**/
    public void Var005()
    {
        try {
            IFSFile f = sandbox_.createDirectory("NewHampshire");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            boolean before = ((Boolean)u.getAttributeValue(RIFSFile.EXISTS)).booleanValue();
            u.delete();
            u.refreshAttributeValues();
            boolean after = ((Boolean)u.getAttributeValue(RIFSFile.EXISTS)).booleanValue();
            assertCondition((before == true) && (after == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
delete() - when an existing non-empty directory is specified.
**/
    public void Var006()
    {
        try {
            IFSFile f = sandbox_.createDirectory("Vermont");
            sandbox_.createNumberedDirectoriesAndFiles(f.getName(), 2, 2);
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            boolean before = ((Boolean)u.getAttributeValue(RIFSFile.EXISTS)).booleanValue();
            u.delete();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



}




