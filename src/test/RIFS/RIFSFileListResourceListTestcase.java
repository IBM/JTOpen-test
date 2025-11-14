///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RIFSFileListResourceListTestcase.java
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
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.resource.Presentation;
import com.ibm.as400.resource.RIFSFile;
import com.ibm.as400.resource.RIFSFileList;
import com.ibm.as400.resource.Resource;
import com.ibm.as400.resource.ResourceListEvent;

import test.JTOpenTestEnvironment;
import test.Testcase;
import test.UserTest;
import test.misc.VIFSSandbox;



/**
Testcase RIFSFileListResourceListTestcase.  This tests the methods
of the RIFSFileList class which are inherited from ResourceList:

<ul>
<li>addPropertyChangeListener(PropertyChangeListener) 
<li>addResourceListListener(ResourceListListener) 
<li>addVetoableChangeListener(VetoableChangeListener) 
<li>close() 
<li>getListLength() 
<li>getPresentation() 
<li>isComplete() 
<li>isInError() 
<li>isOpen() 
<li>isResourceAvailable(long) 
<li>open() 
<li>refreshContents() 
<li>refreshStatus() 
<li>removePropertyChangeListener(PropertyChangeListener) 
<li>removeResourceListListener(ResourceListListener) 
<li>removeVetoableChangeListener(VetoableChangeListener) 
<li>resourceAt(long) 
<li>toString() 
<li>waitForComplete() 
<li>waitForResource(long) 
</ul>
**/
@SuppressWarnings("deprecation")
public class RIFSFileListResourceListTestcase
extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RIFSFileListResourceListTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.RIFSTest.main(newArgs); 
   }



    // Constants.



    // Private data.
    private IFSFile         f5_;
    private IFSFile         f25_;
    private String          dir5_;
    private String          dir25_;
    private String[]        pathNames_;
    private VIFSSandbox     sandbox_;
    private boolean         runningOnOS400_;



/**
Constructor.
**/
    public RIFSFileListResourceListTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RIFSFileListResourceListTestcase",
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
    String sandboxLib_ = "RIFSFLRLT";

    protected void setup ()
    throws Exception
    {
      if (testLib_ != null ) { 
        int len = testLib_.length(); 
        if (len >= 5) { 
          sandboxLib_ = "RIFBT"+testLib_.substring(len-5); 
        }
      }
        sandbox_ = new VIFSSandbox(systemObject_, sandboxLib_);
        f25_ = sandbox_.createDirectory("DIR25");
        f5_ = sandbox_.createDirectory("DIR5");

        sandbox_.createNumberedDirectoriesAndFiles(f25_.getName(), 18, 7);
        sandbox_.createNumberedDirectoriesAndFiles(f5_.getName(), 1, 4);

        dir25_ = f25_.getPath();
        dir5_ = f5_.getPath();

        pathNames_ = f25_.list();

        // See if we're running on OS/400.
        if (JTOpenTestEnvironment.isOS400) {
          runningOnOS400_ = true;
        }

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
            RIFSFileList u = new RIFSFileList();
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
            RIFSFileList u = new RIFSFileList(temp1, dir5_);
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
addResourceListListener() - Pass null.
**/
    public void Var003()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            u.addResourceListListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
addResourceListListener() - Pass a listener.
**/
    public void Var004()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.open();
            assertCondition (rl.events_.size() >= 1);
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
            RIFSFileList u = new RIFSFileList();
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
            RIFSFileList u = new RIFSFileList(temp1, dir25_);
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
close() - Call when the list has never been opened.
**/
    public void Var007()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.close();
            assertCondition(u.isOpen() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
close() - Call when the list has been opened, but no users have been read.
**/
    public void Var008()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.close();
            assertCondition(u.isOpen() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
close() - Call when the list has been opened, and some (but not all) users have been read.
**/
    public void Var009()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.resourceAt(0);
            u.resourceAt(1);
            u.close();
            assertCondition(u.isOpen() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
close() - Call when the list has been opened, and all users have been read.
**/
    public void Var010()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            for(long i = 0; i < length; ++i)
                u.resourceAt(i);
            u.close();
            assertCondition(u.isOpen() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
close() - Call when the list has already been closed.
**/
    public void Var011()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.close();
            u.close();
            assertCondition(u.isOpen() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
close() - Verify that the listClosed event is fired.
**/
    public void Var012()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.waitForComplete();  // Get all events fired.
            
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.close();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LIST_CLOSED);
            assertCondition((events.length == 1)
                   && (events[0].getSource() == u)
                   && (events[0].getID() == ResourceListEvent.LIST_CLOSED)
                   && (events[0].getIndex() == -1)
                   && (events[0].getLength() == -1)
                   && (events[0].getResource() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getListLength() - Call when the list has never been opened, and there
is not enough information to open it implicitly.
**/
    public void Var013()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            long length = u.getListLength();
            failed ("Didn't throw exception"+length);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
getListLength() - Call when the list has never been opened.
**/
    public void Var014()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            long length = u.getListLength();
            assertCondition((u.isOpen() == true) && (length == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getListLength() - Call when the list has opened, but no users have been read.
**/
    public void Var015()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            long length = u.getListLength();
            assertCondition(length == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getListLength() - Call when the list has opened, and several users have been read.
**/
    public void Var016()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.waitForResource(7);
            for(int i = 0; i < 7; ++i)
                u.resourceAt(i);
            long length = u.getListLength();
            assertCondition((length >= 7) && (length < 25));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getListLength() - Call when the list has opened, and all users have been read.
**/
    public void Var017()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            for(int i = 0; i < length; ++i)
                u.resourceAt(i);
            length = u.getListLength();
            assertCondition(length == 25);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getListLength() - Call when the list has opened and closed.
**/
    public void Var018()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.close();
            long length = u.getListLength();
            assertCondition((u.isOpen() == true) && (length == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getListLength() - Verify that the listOpened event is fired, when the
list is opened implicitly.
**/
    public void Var019()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);           
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.getListLength();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LIST_OPENED);
            u.close();
            assertCondition((events.length == 1)
                   && (events[0].getSource() == u)
                   && (events[0].getID() == ResourceListEvent.LIST_OPENED)
                   && (events[0].getIndex() == -1)
                   && (events[0].getLength() == -1)
                   && (events[0].getResource() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPresentation() - Verify that the presentation has default information
for a RIFSFileList whose properties have not been set.
**/
    public void Var020()
    {
      if (checkGui()) { 
        try {
            RIFSFileList u = new RIFSFileList();            
            Presentation p = u.getPresentation();
            String name = p.getName();
            String fullName = p.getFullName();
            String descriptionText = (String)p.getValue(Presentation.DESCRIPTION_TEXT);
            String helpText = (String)p.getValue(Presentation.HELP_TEXT);
            Image iconColor16 = (Image)p.getValue(Presentation.ICON_COLOR_16x16);
            Image iconColor32 = (Image)p.getValue(Presentation.ICON_COLOR_32x32);
            String asString = p.toString();

            assertCondition((name != null)               // Can't compare directly, this is MRI
                   && (fullName != null)        // Can't compare directly, this is MRI
                   && (descriptionText != null) // Can't compare directly, this is MRI.
                   && (helpText == null)
                   && (runningOnOS400_ || iconColor16 != null) // fewer gif's in jt400Native
                   && (runningOnOS400_ || iconColor32 != null)
                   && (asString.equals(name)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getPresentation() - Verify that the presentation has default information
for a RIFSFileList whose properties have been set and used.
**/
    public void Var021()
    {
      if (checkGui()) { 
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            Presentation p = u.getPresentation();
            String name = p.getName();
            String fullName = p.getFullName();
            String descriptionText = (String)p.getValue(Presentation.DESCRIPTION_TEXT);
            String helpText = (String)p.getValue(Presentation.HELP_TEXT);
            Image iconColor16 = (Image)p.getValue(Presentation.ICON_COLOR_16x16);
            Image iconColor32 = (Image)p.getValue(Presentation.ICON_COLOR_32x32);
            String asString = p.toString();

            assertCondition((name != null)               // Can't compare directly, this is MRI
                   && (fullName != null)        // Can't compare directly, this is MRI
                   && (descriptionText != null) // Can't compare directly, this is MRI.
                   && (helpText == null)
                   && (runningOnOS400_ || iconColor16 != null) // fewer gif's in jt400Native
                   && (runningOnOS400_ || iconColor32 != null)
                   && (asString.equals(name)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }



/**
isComplete() - Call when the list has never been opened.
**/
    public void Var022()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            boolean complete = u.isComplete();
            assertCondition((u.isOpen() == false) && (complete == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isComplete() - Call immediately after opening the list, before the list is complete.
**/
    public void Var023()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            boolean complete = u.isComplete();
            u.close();
            assertCondition(complete == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isComplete() - Call when the list has opened and is complete because we waited
for it to.
**/
    public void Var024()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.waitForComplete();
            boolean complete = u.isComplete();
            assertCondition(complete == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isComplete() - Call when the list has opened and closed successively.
**/
    public void Var025()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.close();
            boolean complete = u.isComplete();
            assertCondition((u.isOpen() == false) && (complete == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isComplete() - Call when the list has opened, completed and closed.
**/
    public void Var026()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.waitForComplete();
            u.close();
            boolean complete = u.isComplete();
            assertCondition((u.isOpen() == false) && (complete == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isInError() - Call when the list has never been opened.
**/
    public void Var027()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            boolean inError = u.isInError();
            assertCondition((u.isOpen() == false) && (inError == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isInError() - Call immediately after opening the list, when the list is not in error.
**/
    public void Var028()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            boolean inError = u.isInError();
            u.close();
            assertCondition(inError == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isInError() - Call when the list has opened and is in error.
**/
    public void Var029()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            /* I don't know how to force this condition!
            boolean inError = u.isInError();
            assertCondition(inError == true);
            */
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isInError() - Call when the list has opened and closed successively.
**/
    public void Var030()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.close();
            boolean inError = u.isInError();
            assertCondition((u.isOpen() == false) && (inError == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isInError() - Verify that the listInError event is fired.
**/
    public void Var031()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);

            /*
            u.waitForComplete();  // Get all events fired.            
            // Force an error!  (Not sure how to do this.)
            assertCondition((rl.eventCount_ > 0) // Account for numerous resoure added events.  The
                                        // last one should be list completed.
                   && (rl.event_.getSource() == u)
                   && (rl.event_.getID() == ResourceListEvent.LIST_IN_ERROR)
                   && (rl.event_.getIndex() == -1)
                   && (rl.event_.getLength() == -1)
                   && (rl.event_.getResource() == null));
             */
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isOpen() - Call when the list has never been opened and not enough
information is available to open it.
**/
    public void Var032()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            boolean open = u.isOpen();
            assertCondition(open == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isOpen() - Call when the list has never been opened.
**/
    public void Var033()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            boolean open = u.isOpen();
            assertCondition(open == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isOpen() - Call immediately after opening the list.
**/
    public void Var034()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            boolean open = u.isOpen();
            u.close();
            assertCondition(open == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isOpen() - Call when the list has opened and closed successively.
**/
    public void Var035()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.close();
            assertCondition(u.isOpen() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call when the list has never been opened.
**/
    public void Var036()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            boolean available = u.isResourceAvailable(0);
            assertCondition((u.isOpen() == false) && (available == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call immediately after opening the list, before the list is complete.
Pass -1.
**/
    public void Var037()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            boolean available = u.isResourceAvailable(-1);
            failed ("Didn't throw exception"+available);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
isResourceAvailable() - Call immediately after opening the list, before the list is complete.
Pass 0.
**/
    public void Var038()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            boolean available = u.isResourceAvailable(0);
            u.close();
            assertCondition(available == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call immediately after opening the list, before the list is complete.
Pass 1.
**/
    public void Var039()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            boolean available = u.isResourceAvailable(1);
            u.close();
            assertCondition(available == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call after opening the list and waiting for a particular element.
Pass 12.
**/
    public void Var040()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.waitForResource(12);
            boolean available = u.isResourceAvailable(12);
            u.close();
            assertCondition(available == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call after opening the list and waiting for the list to complete.
Pass 14.
**/
    public void Var041()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.waitForComplete();
            boolean available = u.isResourceAvailable(14);
            u.close();
            assertCondition(available == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call after opening the list and waiting for the list to complete.
Pass a number greater than the length of the list.
**/
    public void Var042()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            boolean available = u.isResourceAvailable(length);
            assertCondition(available == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call when the list has opened and closed successively.
**/
    public void Var043()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.close();
            boolean available = u.isResourceAvailable(0);
            assertCondition((u.isOpen() == false) && (available == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call when the list has opened, completed and closed.
**/
    public void Var044()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            u.waitForComplete();
            u.close();
            boolean available = u.isResourceAvailable(0);
            assertCondition((u.isOpen() == false) && (available == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
open() - Call when there is not enough information to open the list.
**/
    public void Var045()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            u.open();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
open() - Call when a bogus system is specified.
**/
    public void Var046()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            bogus.setGuiAvailable(false);
            RIFSFileList u = new RIFSFileList(bogus, dir25_);
            u.open();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }




/**
open() - Call when a bogus path is specified.
**/
    public void Var047()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, "/Not/Exist");
            u.open();
            u.waitForComplete();
            assertCondition((u.isOpen() == true) && (u.getListLength() == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
open() - Call when the list has never been opened before.
**/
    public void Var048()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            boolean open = ul.isOpen();
            Resource r = ul.resourceAt(0);
            ul.close();
            assertCondition((open == true) && (r != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
open() - Call when the list has been opened before, but nothing done to it.
**/
    public void Var049()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.open();
            boolean open = ul.isOpen();
            Resource r = ul.resourceAt(0);
            ul.close();
            assertCondition((open == true) && (r != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
open() - Call when the list has been opened and completed before.
**/
    public void Var050()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(1);
            ul.open();
            boolean open = ul.isOpen();
            Resource r = ul.resourceAt(0);
            ul.close();
            assertCondition((open == true) && (r != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
open() - Call when the list has been opened and closed before, nothing else.
**/
    public void Var051()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.close();
            ul.open();
            boolean open = ul.isOpen();
            Resource r = ul.resourceAt(0);
            ul.close();
            assertCondition((open == true) && (r != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
open() - Call when the list has been opened, completed, and closed before.
**/
    public void Var052()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(1);
            ul.close();
            ul.open();
            boolean open = ul.isOpen();
            Resource r = ul.resourceAt(0);
            ul.close();
            assertCondition((open == true) && (r != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
open() - Verify that the listOpened event is fired.
**/
    public void Var053()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);           
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.open();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LIST_OPENED);
            u.close();
            assertCondition((events.length == 1)
                   && (events[0].getSource() == u)
                   && (events[0].getID() == ResourceListEvent.LIST_OPENED)
                   && (events[0].getIndex() == -1)
                   && (events[0].getLength() == -1)
                   && (events[0].getResource() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
open() - Verify that the resourceAdded event is fired.
**/
    public void Var054()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);           
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.open();
            u.waitForComplete();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.RESOURCE_ADDED);
            u.close();

            // Not all resources will get added.  We'll make sure that at least
            // 1 resource gets loaded.
            boolean success = (events.length > 1);
            for(int i = 0; i < events.length; ++i) {
                success = success
                   && (events[i].getSource() == u)
                   && (events[i].getID() == ResourceListEvent.RESOURCE_ADDED)
                   && (events[i].getIndex() == i)
                   && (events[i].getLength() == -1)
                   && (((RIFSFile)events[i].getResource()).getPath().endsWith(pathNames_[i]));
            }
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshContents() - Call when there is not enough information to implicitly open the list.
**/
    public void Var055()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            u.refreshContents();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
refreshContents() - Call when the list has never been opened before.
**/
    public void Var056()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.refreshContents();
            boolean open = ul.isOpen();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (length == 25));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshContents() - Call when the list has been opened before, but nothing done to it.
**/
    public void Var057()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.refreshContents();
            boolean open = ul.isOpen();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (length == 25));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshContents() - Call when the list has been opened, and a file was added.
**/
    public void Var058()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir5_);
            ul.open();
            ul.waitForComplete();
            long before = ul.getListLength();

            sandbox_.createNumberedDirectories(f5_.getName(), 1);
            ul.refreshContents();
            boolean open = ul.isOpen();
            ul.waitForComplete();
            long after = ul.getListLength();

            ul.close();
            assertCondition((open == true) && (before + 1 == after));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshContents() - Call when the list has been opened, and a User was removed.
**/
    public void Var059()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir5_);
            ul.open();
            ul.waitForComplete();
            long before = ul.getListLength();

            String name = (String)((RIFSFile)ul.resourceAt(3)).getAttributeValue(RIFSFile.NAME);
            sandbox_.deleteFile(f5_.getName() + "/" + name);

            ul.refreshContents();
            boolean open = ul.isOpen();
            ul.waitForComplete();
            long after = ul.getListLength();

            ul.close();
            assertCondition((open == true) && (before - 1 == after));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshContents() - Call when the list has been opened and closed before, nothing else.
**/
    public void Var060()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.close();
            ul.refreshContents();
            boolean open = ul.isOpen();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (length == 25));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshContents() - Call when the list has been opened, completed, and closed before.
**/
    public void Var061()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(1);
            ul.close();
            ul.refreshContents();
            boolean open = ul.isOpen();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (length == 25));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshStatus() - Call when there is not enough information to implicitly open the list.
**/
    public void Var062()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            u.refreshStatus();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
refreshStatus() - Call when the list has never been opened before.
**/
    public void Var063()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.refreshStatus();
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshStatus() - Call when the list has been opened before, but nothing done to it.
**/
    public void Var064()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.refreshStatus();
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshStatus() - Call when the list has been opened, and a User was added.
refreshStatus() should not update the contents!
**/
    public void Var065()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir5_);
            ul.open();
            ul.waitForComplete();
            long before = ul.getListLength();

            sandbox_.createNumberedDirectories(dir5_, 1);
            ul.refreshStatus();
            boolean open = ul.isOpen();
            ul.waitForComplete();
            long after = ul.getListLength();

            ul.close();
            assertCondition((open == true) && (before == after));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshStatus() - Call when the list has been opened and closed before, nothing else.
**/
    public void Var066()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.close();
            ul.refreshStatus();
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshStatus() - Call when the list has been opened, completed, and closed before.
**/
    public void Var067()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(1);
            ul.close();
            ul.refreshStatus();
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removePropertyChangeListener() - Pass null.
**/
    public void Var068()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            u.removePropertyChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removePropertyChangeListener() - Pass a listener.
**/
    public void Var069()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RIFSFileList u = new RIFSFileList(temp1, dir25_);
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
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
removeResourceListListener() - Pass null.
**/
    public void Var070()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            u.removeResourceListListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removeResourceListListener() - Pass a listener.
**/
    public void Var071()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.removeResourceListListener(rl);
            u.open();
            assertCondition (rl.events_.size() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
removeVetoableChangeListener() - Pass null.
**/
    public void Var072()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            u.removeVetoableChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removeVetoableChangeListener() - Pass a listener.
**/
    public void Var073()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RIFSFileList u = new RIFSFileList(temp1, dir25_);
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
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
resourceAt() - Call when the list has never been opened, and not enough
information is available.
**/
    public void Var074()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            u.resourceAt(0);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }




/**
resourceAt() - Call when the list has never been opened.
**/
    public void Var075()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            RIFSFile u = (RIFSFile)ul.resourceAt(0);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getPath().endsWith(pathNames_[0])));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call immediately after opening the list, before the list is complete.
Pass -1.
**/
    public void Var076()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.resourceAt(-1);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
resourceAt() - Call immediately after opening the list, before the list is complete.
Pass 0, which should have been loaded immediately.
**/
    public void Var077()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            RIFSFile u = (RIFSFile)ul.resourceAt(0);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getPath().endsWith(pathNames_[0])));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call immediately after opening the list, before the list is complete.
Pass 1, which should have been loaded immediately.
**/
    public void Var078()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            RIFSFile u = (RIFSFile)ul.resourceAt(1);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getPath().endsWith(pathNames_[1])));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call immediately after opening the list, before the list is complete.
Pass 5, which should not have been loaded immediately.
**/
    public void Var079()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            RIFSFile u = (RIFSFile)ul.resourceAt(5);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getPath().endsWith(pathNames_[5])));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call after opening the list and waiting for a particular element.
Pass 16.
**/
    public void Var080()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForResource(16);
            RIFSFile u = (RIFSFile)ul.resourceAt(16);
            ul.close();
            assertCondition(u.getPath().endsWith(pathNames_[16]));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call after opening the list and waiting for the list to complete.
Pass 21.
**/
    public void Var081()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            RIFSFile u = (RIFSFile)ul.resourceAt(21);
            ul.close();
            assertCondition(u.getPath().endsWith(pathNames_[21]));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call after opening the list and waiting for the list to complete.
Pass a number greater than the length of the list.
**/
    public void Var082()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.resourceAt(length);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
resourceAt() - Call when the list has opened and closed successively.
**/
    public void Var083()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.close();
            RIFSFile f = (RIFSFile)ul.resourceAt(0);
            assertCondition(f.getPath().endsWith(pathNames_[0]));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call when the list has opened, completed and closed.
**/
    public void Var084()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            ul.close();
            RIFSFile f = (RIFSFile)ul.resourceAt(0);
            assertCondition(f.getPath().endsWith(pathNames_[0]));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - For a RIFSFileList whose properties have not been set,
this should return the default toString.
**/
    public void Var085()
    {
        try {
            RIFSFileList u = new RIFSFileList();  
            assertCondition(u.toString().length() > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - For a RIFSFileList whose properties have been set and used,
this should return the user name.
**/
    public void Var086()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            String asString = u.toString();
            u.close();
            assertCondition(asString.length() > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForComplete() - Call when the list has never been opened, and not enough
information is available.
**/
    public void Var087()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            u.waitForComplete();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }




/**
waitForComplete() - Call when the list has never been opened.
**/
    public void Var088()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.waitForComplete();
            boolean open = ul.isOpen();
            boolean complete = ul.isComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (complete == true) && (length == 25));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForComplete() - Call immediately after opening the list.
**/
    public void Var089()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            boolean open = ul.isOpen();
            boolean complete = ul.isComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (complete == true) && (length == 25));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForComplete() - Call after the list is already complete.
**/
    public void Var090()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            ul.waitForComplete();
            boolean open = ul.isOpen();
            boolean complete = ul.isComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (complete == true) && (length == 25));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForComplete() - Call after the list had been opened and closed.
**/
    public void Var091()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(25 - 2);
            ul.close();
            ul.waitForComplete();
            boolean open = ul.isOpen();
            boolean complete = ul.isComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (complete == true) && (length == 25));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForComplete() - Verify that the listCompleted event is fired.
**/
    public void Var092()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);
            u.open();
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.waitForComplete();  // Get all events fired.            
            u.close();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LIST_COMPLETED);
            assertCondition((events.length == 1) 
                   && (events[0].getSource() == u)
                   && (events[0].getID() == ResourceListEvent.LIST_COMPLETED)
                   && (events[0].getIndex() == -1)
                   && (events[0].getLength() == -1)
                   && (events[0].getResource() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call when the list has never been opened, and not enough
information is available.
**/
    public void Var093()
    {
        try {
            RIFSFileList u = new RIFSFileList();
            u.waitForResource(0);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }




/**
waitForResource() - Call when the list has never been opened.
**/
    public void Var094()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.waitForResource(0);
            boolean open = ul.isOpen();
            boolean available = ul.isResourceAvailable(0);
            RIFSFile u = (RIFSFile)ul.resourceAt(0);
            ul.close();
            assertCondition((open == true) && (u.getPath().endsWith(pathNames_[0])) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call immediately after opening the list, before the list is complete.
Pass -1.
**/
    public void Var095()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForResource(-1);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
waitForResource() - Call immediately after opening the list, before the list is complete.
Pass 0, which should have been loaded immediately.
**/
    public void Var096()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForResource(0);
            boolean available = ul.isResourceAvailable(0);
            RIFSFile u = (RIFSFile)ul.resourceAt(0);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getPath().endsWith(pathNames_[0])) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call immediately after opening the list, before the list is complete.
Pass 1, which should have been loaded immediately.
**/
    public void Var097()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForResource(1);
            boolean available = ul.isResourceAvailable(1);
            RIFSFile u = (RIFSFile)ul.resourceAt(1);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getPath().endsWith(pathNames_[1])) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call immediately after opening the list, before the list is complete.
Pass (length - 1), which should have been loaded immediately.
**/
    public void Var098()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForResource(24);
            boolean available = ul.isResourceAvailable(24);

            RIFSFile u = (RIFSFile)ul.resourceAt(24);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getPath().endsWith(pathNames_[24])) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call immediately after opening the list, before the list is complete.
Pass a value which should not have been loaded immediately.
**/
    public void Var099()
    {
        try {
            /* Not sure how to force a resource NOT to be loaded...
            */
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call after opening the list and waiting for a particular element.
Pass 11.
**/
    public void Var100()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForResource(11);
            ul.waitForResource(11);
            boolean available = ul.isResourceAvailable(11);
            RIFSFile u = (RIFSFile)ul.resourceAt(11);
            ul.close();
            assertCondition((u.getPath().endsWith(pathNames_[11])) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call after opening the list and waiting for the list to complete.
Pass 17.
**/
    public void Var101()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            ul.waitForResource(17);
            boolean available = ul.isResourceAvailable(17);
            RIFSFile u = (RIFSFile)ul.resourceAt(17);
            ul.close();
            assertCondition((u.getPath().endsWith(pathNames_[17])) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call after opening the list and waiting for the list to complete.
Pass a number greater than the length of the list.
**/
    public void Var102()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.waitForResource(length);
            succeeded();
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call when the list has opened and closed successively.
**/
    public void Var103()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.close();
            ul.waitForResource(0);
            boolean available = ul.isResourceAvailable(0);
            RIFSFile f = (RIFSFile)ul.resourceAt(0);
            assertCondition((f.getPath().endsWith(pathNames_[0])) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call when the list has opened, completed and closed.
**/
    public void Var104()
    {
        try {
            RIFSFileList ul = new RIFSFileList(systemObject_, dir25_);
            ul.open();
            ul.waitForComplete();
            ul.close();
            ul.waitForResource(0);
            boolean available = ul.isResourceAvailable(0);
            RIFSFile f = (RIFSFile)ul.resourceAt(0);
            assertCondition((f.getPath().endsWith(pathNames_[0])) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
waitForResource() - Verify that the listLengthChanged event is fired.
**/
    public void Var105()
    {
        try {
            RIFSFileList u = new RIFSFileList(systemObject_, dir25_);           
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.waitForResource(9);
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LENGTH_CHANGED);
            u.close();
            int eventsLength = events.length;
            assertCondition((eventsLength >= 1)
                   && (events[eventsLength-1].getSource() == u)
                   && (events[eventsLength-1].getID() == ResourceListEvent.LENGTH_CHANGED)
                   && (events[eventsLength-1].getIndex() == -1)
                   && (events[eventsLength-1].getLength() == 10)
                   && (events[eventsLength-1].getResource() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




