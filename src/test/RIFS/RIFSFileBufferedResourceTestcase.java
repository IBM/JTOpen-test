///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RIFSFileBufferedResourceTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RIFS;

import java.awt.Image;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.resource.Presentation;
import com.ibm.as400.resource.RIFSFile;
import com.ibm.as400.resource.ResourceEvent;

import test.RIFSTest;
import test.Testcase;
import test.misc.VIFSSandbox;



/**
Testcase RIFSFileBufferedResourceTestcase.  This tests the following methods
of the RIFSFile class, inherited from BufferedResource:

<ul>
<li>addPropertyChangeListener() 
<li>addResourceListener() 
<li>addVetoableChangeListener() 
<li>equals() 
<li>getPresentation() 
<li>getResourceKey() 
<li>removePropertyChangeListener() 
<li>removeResourceListener() 
<li>removeVetoableChangeListener() 
<li>toString() 
</ul>
**/
@SuppressWarnings("deprecation")
public class RIFSFileBufferedResourceTestcase
extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RIFSFileBufferedResourceTestcase";
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
    public RIFSFileBufferedResourceTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RIFSFileBufferedResourceTestcase",
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
    String sandboxLib_ = "RIFSFBRT";
    protected void setup ()
    throws Exception
    {
      if (testLib_ != null ) { 
        int len = testLib_.length(); 
        if (len >= 5) { 
          sandboxLib_ = "RIFBR"+testLib_.substring(len-5); 
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
addPropertyChangeListener() - Pass null.
**/
    public void Var001()
    {
        try {
            RIFSFile u = new RIFSFile();
            u.addPropertyChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
addPropertyChangeListener() - Pass a listener.
**/
    public void Var002()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RIFSFile u = new RIFSFile(temp1, "washington");
            RIFSTest.PropertyChangeListener_ pcl = new RIFSTest.PropertyChangeListener_();
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
addResourceListener() - Pass null.
**/
    public void Var003()
    {
        try {
            RIFSFile u = new RIFSFile();
            u.addResourceListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
addResourceListener() - Pass a listener.
**/
    public void Var004()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            RIFSFile u = new RIFSFile(temp1, "oregon");
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, new Date());
            RIFSTest.ResourceListener_ rl = new RIFSTest.ResourceListener_();
            u.addResourceListener(rl);
            Date newDate = new Date();
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, newDate);
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_VALUE_CHANGED)
                    && (rl.event_.getAttributeID().equals(RIFSFile.LAST_MODIFIED))
                    && (rl.event_.getValue().equals(newDate)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
addVetoableChangeListener() - Pass null.
**/
    public void Var005()
    {
        try {
            RIFSFile u = new RIFSFile();
            u.addVetoableChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
addVetoableChangeListener() - Pass a listener.
**/
    public void Var006()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RIFSFile u = new RIFSFile(temp1, "oregon");
            RIFSTest.VetoableChangeListener_ vcl = new RIFSTest.VetoableChangeListener_();
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
equals() - Verify that a RIFSFile is not equal to null.
**/
    public void Var007()
    {
        try {
            IFSFile f = sandbox_.createFile("Bill");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            assertCondition(u.equals(null) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RIFSFile is not equal to something that is not a RIFSFile.
**/
    public void Var008()
    {
        try {
            IFSFile f = sandbox_.createFile("Susan");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            assertCondition(u.equals(new Date()) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RIFSFile is not equal to another RIFSFile that it should not be.
**/
    public void Var009()
    {
        try {
            IFSFile f = sandbox_.createFile("Rod");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            RIFSFile u2 = new RIFSFile(systemObject_, "FRANK");
            assertCondition(u.equals(u2) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RIFSFile is equal to itself.
**/
    public void Var010()
    {
        try {
            IFSFile f = sandbox_.createFile("Terry");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            assertCondition(u.equals(u) == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RIFSFile is equal to another RIFSFile representing the same file,
before either has been used.  The resource keys have not been set, so this should
be false.
**/
    public void Var011()
    {
        try {
            IFSFile f = sandbox_.createFile("Sue");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            assertCondition(u.equals(u2) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
equals() - Verify that a RIFSFile is equal to another RIFSFile representing the same file,
after both have been used.  The resource keys should be set, and this should be
true.
**/
    public void Var012()
    {
        try {
            IFSFile f = sandbox_.createFile("Chris");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());            
            RIFSFile u2 = new RIFSFile(systemObject_, f.getPath());
            u.getAttributeValue(RIFSFile.LENGTH);
            u2.getAttributeValue(RIFSFile.LENGTH);
            assertCondition(u.equals(u2) == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPresentation() - Verify that the presentation has default information
for a RIFSFile whose properties have not been set.
**/
    public void Var013()
    {
      if (checkGui()) { 
        try {
            RIFSFile u = new RIFSFile();            
            Presentation p = u.getPresentation();
            String name = p.getName();
            String fullName = p.getFullName();
            String descriptionText = (String)p.getValue(Presentation.DESCRIPTION_TEXT);
            String helpText = (String)p.getValue(Presentation.HELP_TEXT);
            Image iconColor16 = (Image)p.getValue(Presentation.ICON_COLOR_16x16);
            Image iconColor32 = (Image)p.getValue(Presentation.ICON_COLOR_32x32);
            String asString = p.toString();
            assertCondition((name.equals(""))
                   && (fullName.equals(""))
                   && (descriptionText != null) // Can't compare directly, this is MRI.
                   && (helpText == null)
                   && (iconColor16 != null)
                   && (iconColor32 != null)
                   && (asString.equals("")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getPresentation() - Verify that the presentation has default information
for a RIFSFile whose properties have been set and used.
**/
    public void Var014()
    {
      if (checkGui()) { 
        if (checkAttended())
        try {
            IFSFile f = sandbox_.createFile("Bill");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Long length = (Long)u.getAttributeValue(RIFSFile.LENGTH);
            Presentation p = u.getPresentation();
            String name = p.getName();
            String fullName = p.getFullName();
            String descriptionText = (String)p.getValue(Presentation.DESCRIPTION_TEXT);
            String helpText = (String)p.getValue(Presentation.HELP_TEXT);
            Image iconColor16 = (Image)p.getValue(Presentation.ICON_COLOR_16x16);
            Image iconColor32 = (Image)p.getValue(Presentation.ICON_COLOR_32x32);
            String asString = p.toString();
            assertCondition((name.equals(f.getName()))
                   && (fullName.equals(f.getPath()))
                   && (length.longValue() == f.length())
                   && (helpText == null)
                   && (iconColor16 != null)
                   && (iconColor32 != null)
                   && (asString.equals(f.getName())), "descriptionText="+descriptionText);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getPresentation() - Verify that passing null to the Presentation
throws an exception.
**/
    public void Var015()
    {
      if (checkGui()) { 
        try {
            RIFSFile u = new RIFSFile();            
            Presentation p = u.getPresentation();
            p.getValue(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }
    }


/**
getPresentation() - Verify that asking the Presentation for
bogus information throws an exception.
**/
    public void Var016()
    {
      if (checkGui()) { 
        try {
            RIFSFile u = new RIFSFile();            
            Presentation p = u.getPresentation();
            assertCondition(p.getValue("THIS IS NOT RIGHT") == null);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getResourceKey() - For a RIFSFile whose properties have not been set,
the resource key should be null.
**/
    public void Var017()
    {
        try {
            RIFSFile u = new RIFSFile();  
            assertCondition(u.getResourceKey() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResourceKey() - For a RIFSFile whose properties have been set and used,
the resource key should be set.
**/
    public void Var018()
    {
        try {
            IFSFile f = sandbox_.createFile("Dave");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Long length = (Long)u.getAttributeValue(RIFSFile.LENGTH);

            StringBuffer buffer = new StringBuffer();
            buffer.append(RIFSFile.class);
            buffer.append(':');
            buffer.append(systemObject_.getSystemName());
            buffer.append(':');
            buffer.append(systemObject_.getUserId());
            buffer.append(':');
            buffer.append(f.getPath());
            String expected = buffer.toString();

            assertCondition(u.getResourceKey().equals(expected), "length="+length);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removePropertyChangeListener() - Pass null.
**/
    public void Var019()
    {
        try {
            RIFSFile u = new RIFSFile();
            u.removePropertyChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removePropertyChangeListener() - Pass a listener that had never been added.
**/
    public void Var020()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RIFSFile u = new RIFSFile(temp1, "california");
            RIFSTest.PropertyChangeListener_ pcl = new RIFSTest.PropertyChangeListener_();
            u.removePropertyChangeListener(pcl);
            u.setSystem(temp2);
            assertCondition (pcl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removePropertyChangeListener() - Pass a listener ihat had been added previously.
**/
    public void Var021()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RIFSFile u = new RIFSFile(temp1, "nevada");
            RIFSTest.PropertyChangeListener_ pcl = new RIFSTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.removePropertyChangeListener(pcl);
            u.setSystem(temp2);
            assertCondition (pcl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
removeResourceListener() - Pass null.
**/
    public void Var022()
    {
        try {
            RIFSFile u = new RIFSFile();
            u.removeResourceListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removeResourceListener() - Pass a listener that had never been added.
**/
    public void Var023()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RIFSFile u = new RIFSFile(temp1, "california");
            RIFSTest.ResourceListener_ rl = new RIFSTest.ResourceListener_();
            u.removeResourceListener(rl);
            u.setSystem(temp2);
            assertCondition (rl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removeResourceListener() - Pass a listener that had been added previously.
**/
    public void Var024()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            RIFSFile u = new RIFSFile(temp1, "idaho");
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, new Date());
            RIFSTest.ResourceListener_ rl = new RIFSTest.ResourceListener_();
            u.addResourceListener(rl);
            u.removeResourceListener(rl);
            u.setAttributeValue(RIFSFile.LAST_MODIFIED, new Date());
            assertCondition (rl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
removeVetoableChangeListener() - Pass null.
**/
    public void Var025()
    {
        try {
            RIFSFile u = new RIFSFile();
            u.removeVetoableChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removeVetoableChangeListener() - Pass a listener that had never been added.
**/
    public void Var026()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RIFSFile u = new RIFSFile(temp1, "california");
            RIFSTest.VetoableChangeListener_ vcl = new RIFSTest.VetoableChangeListener_();
            u.removeVetoableChangeListener(vcl);
            u.setSystem(temp2);
            assertCondition (vcl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removePropertyChangeListener() - Pass a listener ihat had been added previously.
**/
    public void Var027()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RIFSFile u = new RIFSFile(temp1, "nevada");
            RIFSTest.VetoableChangeListener_ vcl = new RIFSTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.removeVetoableChangeListener(vcl);
            u.setSystem(temp2);
            assertCondition (vcl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - For a RIFSFile whose properties have not been set,
this should return the default toString.
**/
    public void Var028()
    {
        try {
            RIFSFile u = new RIFSFile();  
            assertCondition(u.toString() != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - For a RIFSFile whose properties have been set and used,
this should return the path name.
**/
    public void Var029()
    {
        try {
            IFSFile f = sandbox_.createFile("Dave");
            RIFSFile u = new RIFSFile(systemObject_, f.getPath());
            Long length = (Long)u.getAttributeValue(RIFSFile.LENGTH);
            assertCondition(u.toString().equals(f.getPath()), "length="+length);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




