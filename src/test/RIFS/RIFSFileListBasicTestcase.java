///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RIFSFileListBasicTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RIFS;

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.resource.RIFSFile;
import com.ibm.as400.resource.RIFSFileList;

import test.PasswordVault;
import test.RIFSTest;
import test.Testcase;
import test.UserTest;
import test.misc.VIFSSandbox;



/**
Testcase RIFSFileListBasicTestcase.  This tests the following methods
of the RIFSFileList class:

<ul>
<li>constructors
<li>serialization
<li>getPath()
<li>getSystem()
<li>setPath()
<li>setSystem()
</ul>
**/
@SuppressWarnings("deprecation")
public class RIFSFileListBasicTestcase
extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RIFSFileListBasicTestcase";
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
    public RIFSFileListBasicTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RIFSFileListBasicTestcase",
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
        sandbox_ = new VIFSSandbox(systemObject_, "RIFSFLBT");
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
constructor() with 0 parms - Should work.
**/
    public void Var001()
    {
        try {
            RIFSFileList ul = new RIFSFileList();
            assertCondition ((ul.getSystem() == null) && (ul.getPath() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
constructor() with 1 parm - Pass null for file.
**/
    public void Var002()
    {
        try {
            RIFSFileList ul = new RIFSFileList(null);
            failed ("Didn't throw exception"+ul);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 1 parm - Should work.
**/
    public void Var003()
    {
        try {            
            IFSFile f = sandbox_.createNumberedDirectoriesAndFiles("Alaska", 3, 6);
            RIFSFile f2 = new RIFSFile(systemObject_, f.getPath());
            RIFSFileList ul = new RIFSFileList(f2);
            ul.waitForComplete();
            long length = ul.getListLength();
            assertCondition((ul.getSystem() == systemObject_) 
                   && (ul.getPath().equals(f.getPath()))
                   && (length == 9));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
constructor() with 2 parms - Pass null for system.
**/
    public void Var004()
    {
        try {
            RIFSFileList ul = new RIFSFileList(null, "aPath");
            failed ("Didn't throw exception"+ul);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 2 parms - Pass null for path.
**/
    public void Var005()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, null);
            failed ("Didn't throw exception"+ul);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 2 parms - Should work.
**/
    public void Var006()
    {
        try {            
            IFSFile f = sandbox_.createNumberedDirectoriesAndFiles("Hawaii", 7, 5);
            RIFSFileList ul = new RIFSFileList(systemObject_, f.getPath());
            ul.waitForComplete();
            long length = ul.getListLength();
            assertCondition((ul.getSystem() == systemObject_) 
                   && (ul.getPath().equals(f.getPath()))
                   && (length == 12));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
serialization - Verify that the properties are set and verify that its usable.
**/
    public void Var007()
    {
        try {
            IFSFile f = sandbox_.createNumberedDirectoriesAndFiles("Washington", 2, 2);
            RIFSFileList ul1 = new RIFSFileList(systemObject_, f.getPath());
            RIFSFileList ul2 = (RIFSFileList)RIFSTest.serialize(ul1);
            char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ul2.getSystem().setPassword(charPassword);
            PasswordVault.clearPassword(charPassword);
            ul2.waitForComplete();
            long length = ul2.getListLength();
            assertCondition ((ul2.getSystem().getSystemName().equals(systemObject_.getSystemName()))
                    && (ul2.getSystem().getUserId().equals(systemObject_.getUserId()))
                    && (ul2.getPath().equals(f.getPath()))
                    && (length == 4));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPath() - Should return "" when the path has not been set.
**/
    public void Var008()
    {
        try {
            RIFSFileList ul = new RIFSFileList();
            assertCondition (ul.getPath() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPath() - Should return the path when the path has been set.
**/
    public void Var009()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, "/The/Path");
            assertCondition (ul.getPath().equals("/The/Path"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSystem() - Should return null when the system has not been set.
**/
    public void Var010()
    {
        try {
            RIFSFileList ul = new RIFSFileList();
            assertCondition (ul.getSystem() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSystem() - Should return the system when the system has been set.
**/
    public void Var011()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, "/A/Path/For/You");
            assertCondition (ul.getSystem() == systemObject_);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setPath() - Should throw an exception if null is passed.
**/
    public void Var012()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, "/Another/Path/For/You");
            u.setPath(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setPath() - Set to an invalid path.  Should be reflected by getPath(),
since the validity is not checked here.
**/
    public void Var013()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, "/Yet/Another/Path/For/You");
            u.setPath("/flkjgdfkl               re\\r\\re\\re\\re\\");
            assertCondition ((u.getPath().equals("/flkjgdfkl               re\\r\\re\\re\\re\\")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setPath() - Set to a valid name.  Should be reflected by getPath() and
verify that it is used.
**/
    public void Var014()
    {
        try {
            IFSFile f = sandbox_.createNumberedDirectoriesAndFiles("Oregon", 1, 13);
            RIFSFileList u = new RIFSFileList();
            u.setSystem(systemObject_);
            u.setPath(f.getPath());
            u.open();
            u.waitForComplete();
            long l  = u.getListLength();
            RIFSFile file = (RIFSFile)u.resourceAt(0);
            u.close();
            assertCondition ((u.getPath().equals(f.getPath()))
                    && (file != null)
                    && (l == 14));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setPath() - Set to a valid name after the RIFSFile object has made a connection.
**/
    public void Var015()
    {
        try {
            IFSFile f = sandbox_.createNumberedDirectoriesAndFiles("California", 0,0);
            RIFSFileList u = new RIFSFileList(systemObject_, f.getPath());
            u.open();
            u.setPath(f.getPath());
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setPath() - Should fire a property change event.
**/
    public void Var016()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, "/Before/Path");
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setPath("/After/Path");
            assertCondition ((pcl.eventCount_ == 1)
                    && (pcl.event_.getPropertyName().equals("path"))
                    && (pcl.event_.getOldValue().equals("/Before/Path"))
                    && (pcl.event_.getNewValue().equals("/After/Path")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Should throw an exception if null is passed.
**/
    public void Var017()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, "/This/Paths/For/You");
            u.setSystem(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setSystem() - Set to an invalid system.  Should be reflected by getSystem(),
since the validity is not checked here.
**/
    public void Var018()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RIFSFileList u = new RIFSFileList(systemObject_, "/Paths/Are/Fun");
            u.setSystem(bogus);
            assertCondition ((u.getSystem().getSystemName().equals("bogus"))
                    && (u.getSystem().getUserId().equalsIgnoreCase("bogus")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Set to a valid name.  Should be reflected by getSystem() and
verify that it is used.
**/
    public void Var019()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            IFSFile f = sandbox_.createNumberedDirectoriesAndFiles("Idaho", 1,0);
            RIFSFileList u = new RIFSFileList(bogus, f.getPath());
            u.setSystem(systemObject_);
            u.open();
            RIFSFile file = (RIFSFile)u.resourceAt(0);
            u.waitForComplete();
            long len = u.getListLength();
            u.close();
            assertCondition ((u.getSystem().getSystemName().equals(systemObject_.getSystemName()))
                    && (u.getSystem().getUserId().equals(systemObject_.getUserId()))
                    && (file != null)
                    && (len == 1));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Set to a valid name after the RIFSFile object has made a connection.
**/
    public void Var020()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, "/Still/Another/Made/Up/Path");
            u.open();
            u.setSystem(systemObject_);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setSystem() - Should fire a property change event.
**/
    public void Var021()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RIFSFileList u = new RIFSFileList(temp1, "/This/Path/Does/Nothing");
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setSystem(temp2);
            assertCondition ((pcl.eventCount_ == 1)
                    && (pcl.event_.getPropertyName().equals("system"))
                    && (pcl.event_.getOldValue().equals(temp1))
                    && (pcl.event_.getNewValue().equals(temp2)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Should fire a vetoable change event.
**/
    public void Var022()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RIFSFileList u = new RIFSFileList(temp1, "/Hola");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.setSystem(temp2);
            assertCondition ((vcl.eventCount_ == 1)
                    && (vcl.event_.getPropertyName().equals("system"))
                    && (vcl.event_.getOldValue().equals(temp1))
                    && (vcl.event_.getNewValue().equals(temp2)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Should throw a PropertyVetoException if the change is vetoed.
**/
    public void Var023()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RIFSFileList u = new RIFSFileList(temp1, "/Muchas/Gracias");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_(true);
            u.addVetoableChangeListener(vcl);
            u.setSystem(temp2);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.beans.PropertyVetoException");
        }
    }



}




