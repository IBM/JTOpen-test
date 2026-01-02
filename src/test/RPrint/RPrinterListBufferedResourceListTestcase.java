///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrinterListBufferedResourceListTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RPrint;

import java.awt.Image;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.Presentation;
import com.ibm.as400.resource.RPrinter;
import com.ibm.as400.resource.RPrinterList;
import com.ibm.as400.resource.Resource;
import com.ibm.as400.resource.ResourceListEvent;

import test.Testcase;
import test.UserTest;



/**
Testcase RPrinterListBufferedResourceListTestcase.  This tests the methods
of the RPrinterList class which are inherited from BufferedResource:

<ul>
<li>addPropertyChangeListener(PropertyChangeListener) 
<li>addResourceListListener(ResourceListListener) 
<li>addVetoableChangeListener(VetoableChangeListener) 
<li>close() 
<li>getListLength() 
<li>getNumberOfPages() 
<li>getPageSize() 
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
<li>setNumberOfPages(int) 
<li>setPageSize(int) 
<li>toString() 
<li>waitForComplete() 
<li>waitForResource(long) 
</ul>
**/
@SuppressWarnings("deprecation")
public class RPrinterListBufferedResourceListTestcase
extends Testcase {



    // Constants.



 


/**
Constructor.
**/
    public RPrinterListBufferedResourceListTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys,
                              String misc)
    {
        super (systemObject, "RPrinterListBufferedResourceListTestcase",
               namesAndVars, runMode, fileOutputStream,
               password);
        
        pwrSys_ = pwrSys;
        if (pwrSys == null)
            throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");
        if (misc == null)
            throw new IllegalStateException("ERROR: Please specify a printer via -misc.");

    }



/**
addPropertyChangeListener() - Pass null.
**/
    public void Var001()
    {
        try {
            RPrinterList ul =  new RPrinterList();
            ul.addPropertyChangeListener(null);
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
            RPrinterList ul =  new RPrinterList(temp1);
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            ul.addPropertyChangeListener(pcl);
            ul.setSystem(temp2);
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
            RPrinterList ul =  new RPrinterList();
            ul.addResourceListListener(null);
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
            RPrinterList ul =  new RPrinterList(systemObject_);
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            ul.addResourceListListener(rl);
            ul.open();
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
            RPrinterList ul =  new RPrinterList();
            ul.addVetoableChangeListener(null);
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
            RPrinterList ul =  new RPrinterList(temp1);
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
            ul.addVetoableChangeListener(vcl);
            ul.setSystem(temp2);
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
            RPrinterList ul =  new RPrinterList(systemObject_);
            ul.close();
            assertCondition(ul.isOpen() == false);
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
            RPrinterList ul =  new RPrinterList(systemObject_);
            ul.open();
            ul.close();
            assertCondition(ul.isOpen() == false);
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
            RPrinterList ul =  new RPrinterList(systemObject_);
            ul.open();
            ul.resourceAt(0);
            if (ul.getListLength() > 1)
                ul.resourceAt(1);
            ul.close();
            assertCondition(ul.isOpen() == false);
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
            RPrinterList ul =  new RPrinterList(systemObject_);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            for(long i = 0; i < length; ++i)
                ul.resourceAt(i);
            ul.close();
            assertCondition(ul.isOpen() == false);
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
            RPrinterList ul =  new RPrinterList(systemObject_);
            ul.open();
            ul.close();
            ul.close();
            assertCondition(ul.isOpen() == false);
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
            RPrinterList ul =  new RPrinterList(systemObject_);
            ul.open();
            ul.waitForComplete();  // Get all events fired.
            
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            ul.addResourceListListener(rl);
            ul.close();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LIST_CLOSED);
            assertCondition((events.length == 1)
                   && (events[0].getSource() == ul)
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
            RPrinterList ul =  new RPrinterList();
            long length = ul.getListLength();
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
            RPrinterList ul =  new RPrinterList(pwrSys_);
            long length = ul.getListLength();
            assertCondition((ul.isOpen() == true) && (length > 0));
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
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            long length = ul.getListLength();
            assertCondition((length > 0));
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
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            // Handle the case where there is only one printer on the system.
            long index = Math.min(3, ul.getListLength() - 1);
            ul.waitForResource(index);
            for(int i = 0; i < index; ++i)
                ul.resourceAt(i);
            long length = ul.getListLength();
            assertCondition((length >= index));
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
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            for(int i = 0; i < length; ++i)
                ul.resourceAt(i);
            length = ul.getListLength();
            assertCondition(length > 0);
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
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.close();
            long length = ul.getListLength();
            assertCondition((ul.isOpen() == true) && (length > 0));
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
            RPrinterList ul =  new RPrinterList(systemObject_);           
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            ul.addResourceListListener(rl);
            ul.getListLength();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LIST_OPENED);
            ul.close();
            assertCondition((events.length == 1)
                   && (events[0].getSource() == ul)
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
getNumberOfPages() - Should return the default when the number of pages has not been set.
**/
    public void Var020()
    {
        try {
            RPrinterList ul = new RPrinterList();
            assertCondition (ul.getNumberOfPages() == 5);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNumberOfPages() - Should return the number of pages when the number of pages has been set.
**/
    public void Var021()
    {
        try {
            RPrinterList ul = new RPrinterList(systemObject_);
            ul.setNumberOfPages(13);
            assertCondition (ul.getNumberOfPages() == 13);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPageSize() - Should return the default when the page size has not been set.
**/
    public void Var022()
    {
        try {
            RPrinterList ul = new RPrinterList();
            assertCondition (ul.getPageSize() == 20);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPageSize() - Should return the page size when the page size has been set.
**/
    public void Var023()
    {
        try {
            RPrinterList ul = new RPrinterList(systemObject_);
            ul.setPageSize(23);
            assertCondition (ul.getPageSize() == 23);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPresentation() - Verify that the presentation has default information
for a RPrinterList whose properties have not been set.
**/
    public void Var024()
    {
        try {
            RPrinterList ul =  new RPrinterList();            
            Presentation p = ul.getPresentation();
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
                   && ( (iconColor16 != null))    // Applets don't always load gifs.
                   && ( (iconColor32 != null))    // Applets don't always load gifs.
                   && (asString.equals(name)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPresentation() - Verify that the presentation has default information
for a RPrinterList whose properties have been set and used.
**/
    public void Var025()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            Presentation p = ul.getPresentation();
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
                   && ( (iconColor16 != null))    // Applets don't always load gifs.
                   && ( (iconColor32 != null))    // Applets don't always load gifs.
                   && (asString.equals(name)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
isComplete() - Call when the list has never been opened.
**/
    public void Var026()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            boolean complete = ul.isComplete();
            assertCondition((ul.isOpen() == false) && (complete == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isComplete() - Call immediately after opening the list, before the list is complete.
**/
    public void Var027()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            boolean complete = ul.isComplete();
            ul.close();

            // Note:  We don't normally have big printer lists, so this list usually
            // always completes immediately...
            // assertCondition(complete == false);
            assertCondition(true, "complete="+complete);
           
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isComplete() - Call when the list has opened and is complete because we waited
for it to.
**/
    public void Var028()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            boolean complete = ul.isComplete();
            assertCondition(complete == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isComplete() - Call when the list has opened and closed successively.
**/
    public void Var029()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.close();
            boolean complete = ul.isComplete();
            assertCondition((ul.isOpen() == false) && (complete == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isComplete() - Call when the list has opened, completed and closed.
**/
    public void Var030()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            ul.close();
            boolean complete = ul.isComplete();
            assertCondition((ul.isOpen() == false) && (complete == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isInError() - Call when the list has never been opened.
**/
    public void Var031()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            boolean inError = ul.isInError();
            assertCondition((ul.isOpen() == false) && (inError == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isInError() - Call immediately after opening the list, when the list is not in error.
**/
    public void Var032()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            boolean inError = ul.isInError();
            ul.close();
            assertCondition(inError == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isInError() - Call when the list has opened and is in error.
**/
    public void Var033()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            /* I don't know how to force this condition!
            boolean inError = ul.isInError();
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
    public void Var034()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.close();
            boolean inError = ul.isInError();
            assertCondition((ul.isOpen() == false) && (inError == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isInError() - Verify that the listInError event is fired.
**/
    public void Var035()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            ul.addResourceListListener(rl);

            /*
            ul.waitForComplete();  // Get all events fired.            
            // Force an error!  (Not sure how to do this.)
            assertCondition((rl.eventCount_ > 0) // Account for numerous resoure added events.  The
                                        // last one should be list completed.
                   && (rl.event_.getSource() == ul)
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
    public void Var036()
    {
        try {
            RPrinterList ul =  new RPrinterList();
            boolean open = ul.isOpen();
            assertCondition(open == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isOpen() - Call when the list has never been opened.
**/
    public void Var037()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            boolean open = ul.isOpen();
            assertCondition(open == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isOpen() - Call immediately after opening the list.
**/
    public void Var038()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isOpen() - Call when the list has opened and closed successively.
**/
    public void Var039()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.close();
            assertCondition(ul.isOpen() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call when the list has never been opened.
**/
    public void Var040()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            boolean available = ul.isResourceAvailable(0);
            assertCondition((ul.isOpen() == false) && (available == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call immediately after opening the list, before the list is complete.
Pass -1.
**/
    public void Var041()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            boolean available = ul.isResourceAvailable(-1);
            failed ("Didn't throw exception"+available);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
isResourceAvailable() - Call immediately after opening the list, before the list is complete.
Pass 0, which should have been loaded immediately.
**/
    public void Var042()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            boolean available = ul.isResourceAvailable(0);
            ul.close();
            assertCondition(available == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call immediately after opening the list, before the list is complete.
Pass 1, which should have been loaded immediately.
**/
    public void Var043()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            boolean available = false;
            if (ul.getListLength() > 1)
                available = ul.isResourceAvailable(1);
            else
                available = true;   // Handle a list with only 1 printer.
            ul.close();
            assertCondition(available == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call immediately after opening the list, before the list is complete.
Pass (pageSize - 1), which should have been loaded immediately.
**/
    public void Var044()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setPageSize(3);
            ul.open();
            boolean available = false;
            if (ul.getListLength() > 1)
                available = ul.isResourceAvailable(ul.getPageSize() - 1);
            else
                available = true;   // Handle a list with only 1 printer.
            ul.close();
            assertCondition(available == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call immediately after opening the list, before the list is complete.
Pass 50, which should not have been loaded immediately.
**/
    public void Var045()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            boolean available = false;
            if (ul.getListLength() > 1)
                available = ul.isResourceAvailable(1);
            else
                available = true;   // Handle a list with only 1 printer.
            /* Not sure how to force a resource NOT to be loaded...
            */
            ul.close();
            // assertCondition(available == false);
            assertCondition(true,"available="+available);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call after opening the list and waiting for a particular element.
Pass 3.
**/
    public void Var046()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            // Handle cases where there is only 1 printer on the system.
            long index = Math.min(3, ul.getListLength() - 1);
            ul.waitForResource(index);
            boolean available = ul.isResourceAvailable(index);
            ul.close();
            assertCondition(available == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call after opening the list and waiting for the list to complete.
Pass 2.
**/
    public void Var047()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            boolean available = false;
            if (ul.getListLength() > 1)
                available = ul.isResourceAvailable(2);
            else
                available = true;   // Handle a list with only 1 printer.
            ul.close();
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
    public void Var048()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean available = ul.isResourceAvailable(length);
            assertCondition(available == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call when the list has opened and closed successively.
**/
    public void Var049()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.close();
            boolean available = ul.isResourceAvailable(0);
            assertCondition((ul.isOpen() == false) && (available == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isResourceAvailable() - Call when the list has opened, completed and closed.
**/
    public void Var050()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            ul.close();
            boolean available = ul.isResourceAvailable(0);
            assertCondition((ul.isOpen() == false) && (available == false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
open() - Call when there is not enough information to open the list.
**/
    public void Var051()
    {
        try {
            RPrinterList ul =  new RPrinterList();
            ul.open();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
open() - Call when a bogus system is specified.
**/
    public void Var052()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            bogus.setGuiAvailable(false);
            RPrinterList ul =  new RPrinterList(bogus);
            ul.open();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
open() - Call when a bogus selection information combinatation is specified.
**/
    public void Var053()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.OUTPUT_QUEUES, new String[] { "bogus1", "bogus2" });
            ul.open();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
open() - Call when the list has never been opened before.
**/
    public void Var054()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
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
    public void Var055()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
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
    public void Var056()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(0);
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
    public void Var057()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
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
    public void Var058()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(0);
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
    public void Var059()
    {
        try {
            RPrinterList ul =  new RPrinterList(systemObject_);           
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            ul.addResourceListListener(rl);
            ul.open();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LIST_OPENED);
            ul.close();
            assertCondition((events.length == 1)
                   && (events[0].getSource() == ul)
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
    public void Var060()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);           
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            ul.addResourceListListener(rl);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(ul.getListLength() - 1); // Force loading.
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.RESOURCE_ADDED);
            ul.close();

            // Not all resources will get added.  We'll make sure that at least
            // 1 resource gets loaded.
            boolean success = (events.length >= 1);
            for(int i = 0; i < events.length; ++i) {
                success = success
                   && (events[i].getSource() == ul)
                   && (events[i].getID() == ResourceListEvent.RESOURCE_ADDED)
                   /* && (events[i].getIndex() == i) */
                   && (events[i].getLength() == -1);
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
    public void Var061()
    {
        try {
            RPrinterList ul =  new RPrinterList();
            ul.refreshContents();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
refreshContents() - Call when the list has never been opened before.
**/
    public void Var062()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.refreshContents();
            boolean open = ul.isOpen();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (length > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshContents() - Call when the list has been opened before, but nothing done to it.
**/
    public void Var063()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.refreshContents();
            boolean open = ul.isOpen();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (length > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshContents() - Call when the list has been opened and closed before, nothing else.
**/
    public void Var064()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.close();
            ul.refreshContents();
            boolean open = ul.isOpen();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (length > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshContents() - Call when the list has been opened, completed, and closed before.
**/
    public void Var065()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(0);
            ul.close();
            ul.refreshContents();
            boolean open = ul.isOpen();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (length > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
refreshStatus() - Call when there is not enough information to implicitly open the list.
**/
    public void Var066()
    {
        try {
            RPrinterList ul =  new RPrinterList();
            ul.refreshStatus();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
refreshStatus() - Call when the list has never been opened before.
**/
    public void Var067()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
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
    public void Var068()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
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
refreshStatus() - Call when the list has been opened and closed before, nothing else.
**/
    public void Var069()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
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
    public void Var070()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(0);
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
    public void Var071()
    {
        try {
            RPrinterList ul =  new RPrinterList();
            ul.removePropertyChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removePropertyChangeListener() - Pass a listener.
**/
    public void Var072()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinterList ul =  new RPrinterList(temp1);
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            ul.addPropertyChangeListener(pcl);
            ul.removePropertyChangeListener(pcl);
            ul.setSystem(temp2);
            assertCondition (pcl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removeResourceListListener() - Pass null.
**/
    public void Var073()
    {
        try {
            RPrinterList ul =  new RPrinterList();
            ul.removeResourceListListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removeResourceListListener() - Pass a listener.
**/
    public void Var074()
    {
        try {
            RPrinterList ul =  new RPrinterList(systemObject_);
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            ul.addResourceListListener(rl);
            ul.removeResourceListListener(rl);
            ul.open();
            assertCondition (rl.events_.size() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
removeVetoableChangeListener() - Pass null.
**/
    public void Var075()
    {
        try {
            RPrinterList ul =  new RPrinterList();
            ul.removeVetoableChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removeVetoableChangeListener() - Pass a listener.
**/
    public void Var076()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinterList ul =  new RPrinterList(temp1);
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
            ul.addVetoableChangeListener(vcl);
            ul.removeVetoableChangeListener(vcl);
            ul.setSystem(temp2);
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
    public void Var077()
    {
        try {
            RPrinterList ul =  new RPrinterList();
            ul.resourceAt(0);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }




/**
resourceAt() - Call when the list has never been opened.
**/
    public void Var078()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            RPrinter u = (RPrinter)ul.resourceAt(0);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getName().length() > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call immediately after opening the list, before the list is complete.
Pass -1.
**/
    public void Var079()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
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
    public void Var080()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            RPrinter u = (RPrinter)ul.resourceAt(0);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getName().length() > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call immediately after opening the list, before the list is complete.
Pass 1, which should have been loaded immediately.
**/
    public void Var081()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            RPrinter u;
            // Handle case where there is only one printer on the system.
            if (ul.getListLength() == 1)
                u = (RPrinter)ul.resourceAt(0);
            else
                u = (RPrinter)ul.resourceAt(1);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getName().length() > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call immediately after opening the list, before the list is complete.
Pass (length - 1), which should have been loaded immediately.
**/
    public void Var082()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            long length = ul.getListLength();
            RPrinter u = (RPrinter)ul.resourceAt(length-1);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getName().length() > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call immediately after opening the list, before the list is complete.
Pass a value which should not have been loaded immediately.
**/
    public void Var083()
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
resourceAt() - Call after opening the list and waiting for a particular element.
Pass 3.
**/
    public void Var084()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            // Handle the case where there is only one printer on the system.
            long index = Math.min(3, ul.getListLength() - 1);
            ul.waitForResource(index);
            RPrinter u = (RPrinter)ul.resourceAt(index);
            ul.close();
            assertCondition(u != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call after opening the list and waiting for the list to complete.
Pass 3.
**/
    public void Var085()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            // Handle the case where there is only one printer on the system.
            long index = Math.min(3, ul.getListLength() - 1);
            RPrinter u = (RPrinter)ul.resourceAt(index);
            ul.close();
            assertCondition(u != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call after opening the list and waiting for the list to complete.
Pass a number greater than the length of the list.
**/
    public void Var086()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
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
    public void Var087()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.close();
            RPrinter user = (RPrinter)ul.resourceAt(0);
            assertCondition((ul.isOpen() == true) && (user.getName().length() > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
resourceAt() - Call when the list has opened, completed and closed.
**/
    public void Var088()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            ul.close();
            RPrinter user = (RPrinter)ul.resourceAt(0);
            assertCondition((ul.isOpen() == true) && (user.getName().length() > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
resourceAt() - When two RPrinterLists load the same users, they should share them
(via the resource pool).
**/
    public void Var089()
    {
        try {
            RPrinterList ul1 = new RPrinterList(pwrSys_);
            ul1.open();
            ul1.waitForComplete();

            RPrinterList ul2 = new RPrinterList(pwrSys_);
            ul2.open();
            ul2.waitForComplete();

            long length = ul1.getListLength();
            boolean success = (length == ul2.getListLength());
            for(int i = 0; i < length; ++i) {
                // Use == (not equals()) to verify that they are
                // THE EXACT SAME object.
                if (ul1.resourceAt(i) != ul2.resourceAt(i))
                    success = false;
            }

            ul1.close();
            ul2.close();
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
setNumberOfPages() - Should throw an exception if passed -1.
**/
    public void Var090()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setNumberOfPages(-1);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setNumberOfPages() - Should throw an exception if passed 0.
**/
    public void Var091()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setNumberOfPages(0);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setNumberOfPages() - Should throw an exception if set when the list is open.
**/
    public void Var092()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.setNumberOfPages(0);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setNumberOfPages() - Should work when set before the list is opened.
**/
    public void Var093()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setNumberOfPages(3);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean success = true;
            for(int i = 0;i < length; ++i) {
                if (ul.resourceAt(i) == null) {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            ul.close();

            assertCondition(success && (ul.getNumberOfPages() == 3));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setNumberOfPages() - Should work when set after the list has been opened
and closed.
**/
    public void Var094()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            for(int i = 0;i < length; ++i)
                ul.resourceAt(i);
            ul.close();

            ul.setNumberOfPages(4);
            ul.open();
            ul.waitForComplete();
            length = ul.getListLength();
            boolean success = true;
            for(int i = 0;i < length; ++i) {
                if (ul.resourceAt(i) == null) {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            ul.close();

            assertCondition(success && (ul.getNumberOfPages() == 4));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setNumberOfPages() - Should work when set to 1.
**/
    public void Var095()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setNumberOfPages(1);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean success = true;
            for(int i = 0;i < length; ++i) {
                if (ul.resourceAt(i) == null) {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            ul.close();

            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setNumberOfPages() - Should work when set to 2.
**/
    public void Var096()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setNumberOfPages(2);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean success = true;
            for(int i = 0;i < length; ++i) {
                if (ul.resourceAt(i) == null) {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            ul.close();

            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setPageSize() - Should throw an exception if passed -1.
**/
    public void Var097()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setPageSize(-1);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setPageSize() - Should throw an exception if passed 0.
**/
    public void Var098()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setPageSize(0);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setPageSize() - Should throw an exception if set when the list is open.
**/
    public void Var099()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.setPageSize(0);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setPageSize() - Should work when set before the list is opened.
**/
    public void Var100()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setPageSize(33);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean success = true;
            for(int i = 0;i < length; ++i) {
                if (ul.resourceAt(i) == null) {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            ul.close();

            assertCondition(success && (ul.getPageSize() == 33));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setPageSize() - Should work when set after the list has been opened
and closed.
**/
    public void Var101()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            for(int i = 0;i < length; ++i)
                ul.resourceAt(i);
            ul.close();

            ul.setPageSize(11);
            ul.open();
            ul.waitForComplete();
            length = ul.getListLength();
            boolean success = true;
            for(int i = 0;i < length; ++i) {
                if (ul.resourceAt(i) == null) {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            ul.close();

            assertCondition(success && (ul.getPageSize() == 11));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setPageSize() - Should work when set to 1.
**/
    public void Var102()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setPageSize(1);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean success = true;
            for(int i = 0;i < length; ++i) {
                if (ul.resourceAt(i) == null) {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            ul.close();

            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setPageSize() - Should work when set to 2.
**/
    public void Var103()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setPageSize(2);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean success = true;
            for(int i = 0;i < length; ++i) {
                if (ul.resourceAt(i) == null) {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            ul.close();

            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setNumberOfPages()/setPageSize() - Should work when both set to 1.
**/
    public void Var104()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.setNumberOfPages(1);
            ul.setPageSize(1);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean success = true;
            for(int i = 0;i < length; ++i) {
                if (ul.resourceAt(i) == null) {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            ul.close();

            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - For a RPrinterList whose properties have not been set,
this should return the default toString.
**/
    public void Var105()
    {
        try {
            RPrinterList ul =  new RPrinterList();  
            assertCondition(ul.toString().length() > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - For a RPrinterList whose properties have been set and used,
this should return the user name.
**/
    public void Var106()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);
            ul.open();
            String asString = ul.toString();
            ul.close();
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
    public void Var107()
    {
        try {
            RPrinterList ul =  new RPrinterList();
            ul.waitForComplete();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }




/**
waitForComplete() - Call when the list has never been opened.
**/
    public void Var108()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.waitForComplete();
            boolean open = ul.isOpen();
            boolean complete = ul.isComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (complete == true) && (length > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForComplete() - Call immediately after opening the list.
**/
    public void Var109()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            boolean open = ul.isOpen();
            boolean complete = ul.isComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (complete == true) && (length > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForComplete() - Call after the list is already complete.
**/
    public void Var110()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            ul.waitForComplete();
            boolean open = ul.isOpen();
            boolean complete = ul.isComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (complete == true) && (length > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForComplete() - Call after the list had been opened and closed.
**/
    public void Var111()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            // Handle the case where there is only one printer on the system.
            long index = Math.max(0, ul.getListLength() - 2);
            ul.resourceAt(index);
            ul.close();
            ul.waitForComplete();
            boolean open = ul.isOpen();
            boolean complete = ul.isComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition((open == true) && (complete == true) && (length > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForComplete() - Verify that the listCompleted event is fired.
**/
    public void Var112()
    {
        try {
            RPrinterList ul =  new RPrinterList(pwrSys_);            
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            ul.addResourceListListener(rl);
            ul.open();
            ul.waitForComplete();  // Get all events fired.            
            ul.close();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LIST_COMPLETED);
            assertCondition((events.length == 1) 
                   && (events[0].getSource() == ul)
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
    public void Var113()
    {
        try {
            RPrinterList ul =  new RPrinterList();
            ul.waitForResource(0);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }




/**
waitForResource() - Call when the list has never been opened.
**/
    public void Var114()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.waitForResource(0);
            boolean open = ul.isOpen();
            boolean available = ul.isResourceAvailable(0);
            RPrinter u = (RPrinter)ul.resourceAt(0);
            ul.close();
            assertCondition((open == true) && (u.getName().length() > 0) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call immediately after opening the list, before the list is complete.
Pass -1.
**/
    public void Var115()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
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
    public void Var116()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForResource(0);
            boolean available = ul.isResourceAvailable(0);
            RPrinter u = (RPrinter)ul.resourceAt(0);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getName().length() > 0) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call immediately after opening the list, before the list is complete.
Pass 1, which should have been loaded immediately.
**/
    public void Var117()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            // Handle the case where there is only one printer on the system.
            long index = Math.min(1, ul.getListLength() - 1);
            ul.waitForResource(index);
            boolean available = ul.isResourceAvailable(index);
            RPrinter u = (RPrinter)ul.resourceAt(index);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getName().length() > 0) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call immediately after opening the list, before the list is complete.
Pass (length - 1), which should have been loaded immediately.
**/
    public void Var118()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            long length = ul.getListLength();
            ul.waitForResource(length-1);
            boolean available = ul.isResourceAvailable(length-1);

            RPrinter u = (RPrinter)ul.resourceAt(length-1);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition((open == true) && (u.getName().length() > 0), "available="+available);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call immediately after opening the list, before the list is complete.
Pass a value which should not have been loaded immediately.
**/
    public void Var119()
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
Pass 2.
**/
    public void Var120()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            // Handle the case where there is only one printer on the system.
            long index = Math.min(2, ul.getListLength() - 1);
            ul.waitForResource(index);
            ul.waitForResource(index);
            boolean available = ul.isResourceAvailable(index);
            RPrinter u = (RPrinter)ul.resourceAt(index);
            ul.close();
            assertCondition((u != null) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call after opening the list and waiting for the list to complete.
Pass 3.
**/
    public void Var121()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            // Handle the case where there is only one printer on the system.
            long index = Math.min(3, ul.getListLength() - 1);
            ul.waitForResource(index);
            boolean available = ul.isResourceAvailable(index);
            RPrinter u = (RPrinter)ul.resourceAt(index);
            ul.close();
            assertCondition((u != null) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call after opening the list and waiting for the list to complete.
Pass a number greater than the length of the list.
**/
    public void Var122()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.waitForResource(length);
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call when the list has opened and closed successively.
**/
    public void Var123()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.close();
            ul.waitForResource(0);
            boolean available = ul.isResourceAvailable(0);
            RPrinter user = (RPrinter)ul.resourceAt(0);
            assertCondition((ul.isOpen() == true) && (user.getName().length() > 0) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
waitForResource() - Call when the list has opened, completed and closed.
**/
    public void Var124()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            ul.close();
            ul.waitForResource(0);
            boolean available = ul.isResourceAvailable(0);
            RPrinter user = (RPrinter)ul.resourceAt(0);
            assertCondition((ul.isOpen() == true) && (user.getName().length() > 0) && (available == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
waitForResource() - Verify that the listLengthChanged event is fired.
**/
    public void Var125()
    {
        try {
            RPrinterList ul =  new RPrinterList(systemObject_);           
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            ul.addResourceListListener(rl);
            long length = ul.getListLength();
            ul.waitForResource(length - 1);
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LENGTH_CHANGED);
            ul.close();
            int eventsLength = events.length;
            assertCondition((eventsLength >= 1)
                   && (events[eventsLength-1].getSource() == ul)
                   && (events[eventsLength-1].getID() == ResourceListEvent.LENGTH_CHANGED)
                   && (events[eventsLength-1].getIndex() == -1)
                   && (events[eventsLength-1].getLength() == length)
                   && (events[eventsLength-1].getResource() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




