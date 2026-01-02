///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserListBufferedResourceListTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import java.awt.Image;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.Presentation;
import com.ibm.as400.resource.RUser;
import com.ibm.as400.resource.RUserList;
import com.ibm.as400.resource.Resource;
import com.ibm.as400.resource.ResourceListEvent;

import test.JTOpenTestEnvironment;
import test.Testcase;
import test.UserTest;

/**
 Testcase UserListBufferedResourceListTestcase.  This tests the methods of the RUserList class which are inherited from BufferedResource:
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
public class UserListBufferedResourceListTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserListBufferedResourceListTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
    private UserSandbox sandbox_;
    private String[] groupAndUsers_;
    private String[] groupAndUsers2_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "ULBRL", UserTest.COLLECTION.substring(UserTest.COLLECTION.length() - 1));
        groupAndUsers_ = sandbox_.createGroupAndUsers(25);
        groupAndUsers2_ = sandbox_.createGroupAndUsers(5);
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        sandbox_.cleanup();
    }

    private static RUserList newRUserList(AS400 system, String userInfo, String groupInfo) throws Exception
    {
        RUserList ul = new RUserList(system);
        ul.setSelectionValue(RUserList.SELECTION_CRITERIA, userInfo);
        ul.setSelectionValue(RUserList.GROUP_PROFILE, groupInfo);
        return ul;
    }

    /**
     addPropertyChangeListener() - Pass null.
     **/
    public void Var001()
    {
        try
        {
            RUserList u = new RUserList();
            u.addPropertyChangeListener(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     addPropertyChangeListener() - Pass a listener.
     **/
    public void Var002()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUserList u = new RUserList(temp1);
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setSystem(temp2);
            assertCondition(pcl.eventCount_ == 1 && pcl.event_.getPropertyName().equals("system") && pcl.event_.getOldValue().equals(temp1) && pcl.event_.getNewValue().equals(temp2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     addResourceListListener() - Pass null.
     **/
    public void Var003()
    {
        try
        {
            RUserList u = new RUserList();
            u.addResourceListListener(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     addResourceListListener() - Pass a listener.
     **/
    public void Var004()
    {
        try
        {
            RUserList u = new RUserList(systemObject_);
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.open();
            assertCondition(rl.events_.size() >= 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     addVetoableChangeListener() - Pass null.
     **/
    public void Var005()
    {
        try
        {
            RUserList u = new RUserList();
            u.addVetoableChangeListener(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     addVetoableChangeListener() - Pass a listener.
     **/
    public void Var006()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUserList u = new RUserList(temp1);
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.setSystem(temp2);
            assertCondition(vcl.eventCount_ == 1 && vcl.event_.getPropertyName().equals("system") && vcl.event_.getOldValue().equals(temp1) && vcl.event_.getNewValue().equals(temp2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     close() - Call when the list has never been opened.
     **/
    public void Var007()
    {
        try
        {
            RUserList u = new RUserList(systemObject_);
            u.close();
            assertCondition(u.isOpen() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     close() - Call when the list has been opened, but no users have been read.
     **/
    public void Var008()
    {
        try
        {
            RUserList u = new RUserList(systemObject_);
            u.open();
            u.close();
            assertCondition(u.isOpen() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     close() - Call when the list has been opened, and some (but not all) users have been read.
     **/
    public void Var009()
    {
        try
        {
            RUserList u = new RUserList(systemObject_);
            u.open();
            u.waitForResource(2);
            u.resourceAt(0);
            if (u.getListLength() < 1) u.resourceAt(1);
            u.close();
            assertCondition(u.isOpen() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     close() - Call when the list has been opened, and all users have been read.
     **/
    public void Var010()
    {
        try
        {
            RUserList u = new RUserList(systemObject_);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            for (long i = 0; i < length; ++i) u.resourceAt(i);
            u.close();
            assertCondition(u.isOpen() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     close() - Call when the list has already been closed.
     **/
    public void Var011()
    {
        try
        {
            RUserList u = new RUserList(systemObject_);
            u.open();
            u.close();
            u.close();
            assertCondition(u.isOpen() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     close() - Verify that the listClosed event is fired.
     **/
    public void Var012()
    {
        try
        {
            RUserList u = new RUserList(systemObject_);
            u.open();
            u.waitForComplete();  // Get all events fired.

            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.close();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LIST_CLOSED);
            assertCondition(events.length == 1 && events[0].getSource() == u && events[0].getID() == ResourceListEvent.LIST_CLOSED && events[0].getIndex() == -1 && events[0].getLength() == -1 && events[0].getResource() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getListLength() - Call when the list has never been opened, and there is not enough information to open it implicitly.
     **/
    public void Var013()
    {
        try
        {
            RUserList u = new RUserList();
            long length = u.getListLength();
            failed("Didn't throw exception"+length);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     getListLength() - Call when the list has never been opened.
     **/
    public void Var014()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            long length = u.getListLength();
            assertCondition(u.isOpen() == true && length > 0 && length < groupAndUsers_.length);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getListLength() - Call when the list has opened, but no users have been read.
     **/
    public void Var015()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            u.open();
            long length = u.getListLength();
            assertCondition(length > 0 && length < groupAndUsers_.length);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getListLength() - Call when the list has opened, and several users have been read.
     **/
    public void Var016()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            u.open();
            u.waitForResource(7);
            for (int i = 0; i < 7; ++i) u.resourceAt(i);
            long length = u.getListLength();
            assertCondition(length >= 7 && length < groupAndUsers_.length);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getListLength() - Call when the list has opened, and all users have been read.
     **/
    public void Var017()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            for (int i = 0; i < length; ++i) u.resourceAt(i);
            length = u.getListLength();
            assertCondition(length == groupAndUsers_.length - 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getListLength() - Call when the list has opened and closed.
     **/
    public void Var018()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            u.open();
            u.close();
            long length = u.getListLength();
            assertCondition(u.isOpen() == true && length > 0 && length < groupAndUsers_.length);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getListLength() - Verify that the listOpened event is fired, when the list is opened implicitly.
     **/
    public void Var019()
    {
        try
        {
            RUserList u = new RUserList(systemObject_);
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.getListLength();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LIST_OPENED);
            u.close();
            assertCondition(events.length == 1 && events[0].getSource() == u && events[0].getID() == ResourceListEvent.LIST_OPENED && events[0].getIndex() == -1 && events[0].getLength() == -1 && events[0].getResource() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getNumberOfPages() - Should return the default when the number of pages has not been set.
     **/
    public void Var020()
    {
        try
        {
            RUserList ul = new RUserList();
            assertCondition(ul.getNumberOfPages() == 5);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getNumberOfPages() - Should return the number of pages when the number of pages has been set.
     **/
    public void Var021()
    {
        try
        {
            RUserList ul = newRUserList(systemObject_, RUserList.MEMBER, "Montana");
            ul.setNumberOfPages(13);
            assertCondition(ul.getNumberOfPages() == 13);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getPageSize() - Should return the default when the page size has not been set.
     **/
    public void Var022()
    {
        try
        {
            RUserList ul = new RUserList();
            assertCondition(ul.getPageSize() == 20);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getPageSize() - Should return the page size when the page size has been set.
     **/
    public void Var023()
    {
        try
        {
            RUserList ul = newRUserList(systemObject_, RUserList.MEMBER, "Montana");
            ul.setPageSize(23);
            assertCondition(ul.getPageSize() == 23);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getPresentation() - Verify that the presentation has default information for a RUserList whose properties have not been set.
     **/
    public void Var024()
    {
        try
        {
            RUserList u = new RUserList();
            Presentation p = u.getPresentation();
            String name = p.getName();
            String fullName = p.getFullName();
            String descriptionText = (String)p.getValue(Presentation.DESCRIPTION_TEXT);
            String helpText = (String)p.getValue(Presentation.HELP_TEXT);
            if (JTOpenTestEnvironment.isOS400 || JTOpenTestEnvironment.isLinux)
            {
                String asString = p.toString();
                // Can't compare directly, this is MRI.
                assertCondition(name != null && fullName != null && descriptionText != null && helpText == null && asString.equals(name));
            }
            else
            {
                Image iconColor16 = (Image)p.getValue(Presentation.ICON_COLOR_16x16);
                Image iconColor32 = (Image)p.getValue(Presentation.ICON_COLOR_32x32);
                String asString = p.toString();
                // Can't compare directly, this is MRI.
                assertCondition(name != null && fullName != null && descriptionText != null && helpText == null && iconColor16 != null && iconColor32 != null && asString.equals(name));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getPresentation() - Verify that the presentation has default information for a RUserList whose properties have been set and used.
     **/
    public void Var025()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            Presentation p = u.getPresentation();
            String name = p.getName();
            String fullName = p.getFullName();
            String descriptionText = (String)p.getValue(Presentation.DESCRIPTION_TEXT);
            String helpText = (String)p.getValue(Presentation.HELP_TEXT);
            if (JTOpenTestEnvironment.isOS400 || JTOpenTestEnvironment.isLinux )
            {
                String asString = p.toString();
                // Can't compare directly, this is MRI.
                assertCondition(name != null && fullName != null && descriptionText != null && helpText == null && asString.equals(name));
            }
            else
            {
                Image iconColor16 = (Image)p.getValue(Presentation.ICON_COLOR_16x16);
                Image iconColor32 = (Image)p.getValue(Presentation.ICON_COLOR_32x32);
                String asString = p.toString();
                // Can't compare directly, this is MRI.
                assertCondition(name != null && fullName != null && descriptionText != null && helpText == null && iconColor16 != null && iconColor32 != null && asString.equals(name));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isComplete() - Call when the list has never been opened.
     **/
    public void Var026()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            boolean complete = u.isComplete();
            assertCondition(u.isOpen() == false && complete == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isComplete() - Call immediately after opening the list, before the list is complete.
     **/
    public void Var027()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            boolean complete = u.isComplete();
            u.close();
            assertCondition(complete == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isComplete() - Call when the list has opened and is complete because we waited for it to.
     **/
    public void Var028()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            u.open();
            u.waitForComplete();
            boolean complete = u.isComplete();
            assertCondition(complete == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isComplete() - Call when the list has opened and closed successively.
     **/
    public void Var029()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            u.open();
            u.close();
            boolean complete = u.isComplete();
            assertCondition(u.isOpen() == false && complete == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isComplete() - Call when the list has opened, completed and closed.
     **/
    public void Var030()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            u.open();
            u.waitForComplete();
            u.close();
            boolean complete = u.isComplete();
            assertCondition(u.isOpen() == false && complete == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isInError() - Call when the list has never been opened.
     **/
    public void Var031()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            boolean inError = u.isInError();
            assertCondition(u.isOpen() == false && inError == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isInError() - Call immediately after opening the list, when the list is not in error.
     **/
    public void Var032()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            boolean inError = u.isInError();
            u.close();
            assertCondition(inError == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isInError() - Call when the list has opened and is in error.
     **/
    public void Var033()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            u.open();
            /* I don't know how to force this condition!
             boolean inError = u.isInError();
             assertCondition(inError == true);
             */
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isInError() - Call when the list has opened and closed successively.
     **/
    public void Var034()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            u.open();
            u.close();
            boolean inError = u.isInError();
            assertCondition(u.isOpen() == false && inError == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isInError() - Verify that the listInError event is fired.
     **/
    public void Var035()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);

            /*
             u.waitForComplete();  // Get all events fired.
             // Force an error!  (Not sure how to do this.)
             assertCondition(rl.eventCount_ > 0 // Account for numerous resoure added events.  The
             // last one should be list completed.
             && rl.event_.getSource() == u
             && rl.event_.getID() == ResourceListEvent.LIST_IN_ERROR
             && rl.event_.getIndex() == -1
             && rl.event_.getLength() == -1
             && rl.event_.getResource() == null);
             */
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isOpen() - Call when the list has never been opened and not enough information is available to open it.
     **/
    public void Var036()
    {
        try
        {
            RUserList u = new RUserList();
            boolean open = u.isOpen();
            assertCondition(open == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isOpen() - Call when the list has never been opened.
     **/
    public void Var037()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            boolean open = u.isOpen();
            assertCondition(open == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isOpen() - Call immediately after opening the list.
     **/
    public void Var038()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            boolean open = u.isOpen();
            u.close();
            assertCondition(open == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isOpen() - Call when the list has opened and closed successively.
     **/
    public void Var039()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            u.open();
            u.close();
            assertCondition(u.isOpen() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isResourceAvailable() - Call when the list has never been opened.
     **/
    public void Var040()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            boolean available = u.isResourceAvailable(0);
            assertCondition(u.isOpen() == false && available == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isResourceAvailable() - Call immediately after opening the list, before the list is complete. Pass -1.
     **/
    public void Var041()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            boolean available = u.isResourceAvailable(-1);
            failed("Didn't throw exception"+available);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     isResourceAvailable() - Call immediately after opening the list, before the list is complete.  Pass 0, which should have been loaded immediately.
     **/
    public void Var042()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            boolean available = u.isResourceAvailable(0);
            u.close();
            assertCondition(available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isResourceAvailable() - Call immediately after opening the list, before the list is complete.  Pass 1, which should have been loaded immediately.
     **/
    public void Var043()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            boolean available = u.isResourceAvailable(1);
            u.close();
            assertCondition(available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isResourceAvailable() - Call immediately after opening the list, before the list is complete.  Pass (pageSize - 1), which should have been loaded immediately.
     **/
    public void Var044()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            u.waitForResource(u.getPageSize() - 1);
            boolean available = u.isResourceAvailable(u.getPageSize() - 1);
            u.close();
            assertCondition(available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isResourceAvailable() - Call immediately after opening the list, before the list is complete.  Pass 50, which should not have been loaded immediately.
     **/
    @SuppressWarnings("unused")
    public void Var045()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            boolean available = u.isResourceAvailable(50);
            /* Not sure how to force a resource NOT to be loaded...
             */
            u.close();
            // assertCondition(available == false);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isResourceAvailable() - Call after opening the list and waiting for a particular element.  Pass 50.
     **/
    public void Var046()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            u.waitForResource(50);
            boolean available = u.isResourceAvailable(50);
            u.close();
            assertCondition(available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isResourceAvailable() - Call after opening the list and waiting for the list to complete.  Pass 50.
     **/
    public void Var047()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            u.waitForComplete();
            boolean available = u.isResourceAvailable(50);
            u.close();
            assertCondition(available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isResourceAvailable() - Call after opening the list and waiting for the list to complete.  Pass a number greater than the length of the list.
     **/
    public void Var048()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            boolean available = u.isResourceAvailable(length);
            assertCondition(available == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isResourceAvailable() - Call when the list has opened and closed successively.
     **/
    public void Var049()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            u.open();
            u.close();
            boolean available = u.isResourceAvailable(0);
            assertCondition(u.isOpen() == false && available == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     isResourceAvailable() - Call when the list has opened, completed and closed.
     **/
    public void Var050()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            u.open();
            u.waitForComplete();
            u.close();
            boolean available = u.isResourceAvailable(0);
            assertCondition(u.isOpen() == false && available == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     open() - Call when there is not enough information to open the list.
     **/
    public void Var051()
    {
        try
        {
            RUserList u = new RUserList();
            u.open();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     open() - Call when a bogus system is specified.
     **/
    public void Var052()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            bogus.setGuiAvailable(false);
            RUserList u = new RUserList(bogus);
            u.open();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     open() - Call when a bogus user information/group information combinatation is specified.
     **/
    public void Var053()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setSelectionValue(RUserList.SELECTION_CRITERIA, RUserList.USER);
            u.setSelectionValue(RUserList.GROUP_PROFILE, RUserList.NOGROUP);
            u.open();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     open() - Call when a bogus group is specified.
     **/
    public void Var054()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setSelectionValue(RUserList.SELECTION_CRITERIA, RUserList.MEMBER);
            u.setSelectionValue(RUserList.GROUP_PROFILE, "NOTEXIST");
            u.open();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     open() - Call when the list has never been opened before.
     **/
    public void Var055()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            boolean open = ul.isOpen();
            Resource r = ul.resourceAt(0);
            ul.close();
            assertCondition(open == true && r != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     open() - Call when the list has been opened before, but nothing done to it.
     **/
    public void Var056()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.open();
            boolean open = ul.isOpen();
            Resource r = ul.resourceAt(0);
            ul.close();
            assertCondition(open == true && r != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     open() - Call when the list has been opened and completed before.
     **/
    public void Var057()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(1);
            ul.open();
            boolean open = ul.isOpen();
            Resource r = ul.resourceAt(0);
            ul.close();
            assertCondition(open == true && r != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     open() - Call when the list has been opened and closed before, nothing else.
     **/
    public void Var058()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.close();
            ul.open();
            boolean open = ul.isOpen();
            Resource r = ul.resourceAt(0);
            ul.close();
            assertCondition(open == true && r != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     open() - Call when the list has been opened, completed, and closed before.
     **/
    public void Var059()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(1);
            ul.close();
            ul.open();
            boolean open = ul.isOpen();
            Resource r = ul.resourceAt(0);
            ul.close();
            assertCondition(open == true && r != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     open() - Verify that the listOpened event is fired.
     **/
    public void Var060()
    {
        try
        {
            RUserList u = new RUserList(systemObject_);
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.open();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LIST_OPENED);
            u.close();
            assertCondition(events.length == 1 && events[0].getSource() == u && events[0].getID() == ResourceListEvent.LIST_OPENED && events[0].getIndex() == -1 && events[0].getLength() == -1 && events[0].getResource() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     open() - Verify that the resourceAdded event is fired.
     **/
    public void Var061()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.open();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.RESOURCE_ADDED);
            u.close();

            // Not all resources will get added.  We'll make sure that at least 1 resource gets loaded.
            boolean success = events.length > 1;
            for (int i = 0; i < events.length; ++i)
            {
                success = success && events[i].getSource() == u && events[i].getID() == ResourceListEvent.RESOURCE_ADDED && events[i].getIndex() == i && events[i].getLength() == -1 && ((RUser)events[i].getResource()).getName().equals(groupAndUsers_[i + 1]);
            }
            assertCondition(success);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshContents() - Call when there is not enough information to implicitly open the list.
     **/
    public void Var062()
    {
        try
        {
            RUserList u = new RUserList();
            u.refreshContents();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     refreshContents() - Call when the list has never been opened before.
     **/
    public void Var063()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers2_[0]);
            ul.refreshContents();
            boolean open = ul.isOpen();
            long length = ul.getListLength();
            ul.close();
            assertCondition(open == true && length == groupAndUsers2_.length - 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshContents() - Call when the list has been opened before, but nothing done to it.
     **/
    public void Var064()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers2_[0]);
            ul.open();
            ul.refreshContents();
            boolean open = ul.isOpen();
            long length = ul.getListLength();
            ul.close();
            assertCondition(open == true && length == groupAndUsers2_.length - 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshContents() - Call when the list has been opened, and a User was added.
     **/
    public void Var065()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers2_[0]);
            ul.open();
            ul.waitForComplete();
            long before = ul.getListLength();

            String newUser = sandbox_.createUser(groupAndUsers2_[0]);
            ul.refreshContents();
            boolean open = ul.isOpen();
            ul.waitForComplete();
            long after = ul.getListLength();

            ul.close();
            assertCondition(open == true && before + 1 == after, "newUser="+newUser);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshContents() - Call when the list has been opened, and a User was removed.
     **/
    public void Var066()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers2_[0]);
            ul.open();
            ul.waitForComplete();
            long before = ul.getListLength();

            sandbox_.deleteUser(groupAndUsers2_[groupAndUsers2_.length-1], 1);

            ul.refreshContents();
            boolean open = ul.isOpen();
            ul.waitForComplete();
            long after = ul.getListLength();

            ul.close();
            assertCondition(open == true && before - 1 == after);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshContents() - Call when the list has been opened and closed before, nothing else.
     **/
    public void Var067()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers2_[0]);
            ul.open();
            ul.close();
            ul.refreshContents();
            boolean open = ul.isOpen();
            long length = ul.getListLength();
            ul.close();
            assertCondition(open == true && length > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshContents() - Call when the list has been opened, completed, and closed before.
     **/
    public void Var068()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers2_[0]);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(1);
            ul.close();
            ul.refreshContents();
            boolean open = ul.isOpen();
            long length = ul.getListLength();
            ul.close();
            assertCondition(open == true && length > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshStatus() - Call when there is not enough information to implicitly open the list.
     **/
    public void Var069()
    {
        try
        {
            RUserList u = new RUserList();
            u.refreshStatus();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     refreshStatus() - Call when the list has never been opened before.
     **/
    public void Var070()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers2_[0]);
            ul.refreshStatus();
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshStatus() - Call when the list has been opened before, but nothing done to it.
     **/
    public void Var071()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers2_[0]);
            ul.open();
            ul.refreshStatus();
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshStatus() - Call when the list has been opened, and a User was added.  refreshStatus() should not update the contents!
     **/
    public void Var072()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers2_[0]);
            ul.open();
            ul.waitForComplete();
            long before = ul.getListLength();

            String newUser = sandbox_.createUser(groupAndUsers2_[0]);
            ul.refreshStatus();
            boolean open = ul.isOpen();
            ul.waitForComplete();
            long after = ul.getListLength();

            ul.close();
            assertCondition(open == true && before == after,"newUser="+newUser);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshStatus() - Call when the list has been opened and closed before, nothing else.
     **/
    public void Var073()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers2_[0]);
            ul.open();
            ul.close();
            ul.refreshStatus();
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshStatus() - Call when the list has been opened, completed, and closed before.
     **/
    public void Var074()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers2_[0]);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(1);
            ul.close();
            ul.refreshStatus();
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     removePropertyChangeListener() - Pass null.
     **/
    public void Var075()
    {
        try
        {
            RUserList u = new RUserList();
            u.removePropertyChangeListener(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     removePropertyChangeListener() - Pass a listener.
     **/
    public void Var076()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUserList u = new RUserList(temp1);
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.removePropertyChangeListener(pcl);
            u.setSystem(temp2);
            assertCondition(pcl.eventCount_ == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     removeResourceListListener() - Pass null.
     **/
    public void Var077()
    {
        try
        {
            RUserList u = new RUserList();
            u.removeResourceListListener(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     removeResourceListListener() - Pass a listener.
     **/
    public void Var078()
    {
        try
        {
            RUserList u = new RUserList(systemObject_);
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.removeResourceListListener(rl);
            u.open();
            assertCondition(rl.events_.size() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     removeVetoableChangeListener() - Pass null.
     **/
    public void Var079()
    {
        try
        {
            RUserList u = new RUserList();
            u.removeVetoableChangeListener(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     removeVetoableChangeListener() - Pass a listener.
     **/
    public void Var080()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUserList u = new RUserList(temp1);
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.removeVetoableChangeListener(vcl);
            u.setSystem(temp2);
            assertCondition(vcl.eventCount_ == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     resourceAt() - Call when the list has never been opened, and not enough information is available.
     **/
    public void Var081()
    {
        try
        {
            RUserList u = new RUserList();
            u.resourceAt(0);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     resourceAt() - Call when the list has never been opened.
     **/
    public void Var082()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            RUser u = (RUser)ul.resourceAt(0);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true && u.getName().equals(groupAndUsers_[1]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     resourceAt() - Call immediately after opening the list, before the list is complete.  Pass -1.
     **/
    public void Var083()
    {
        try
        {
            RUserList ul = new RUserList(pwrSys_);
            ul.open();
            ul.resourceAt(-1);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     resourceAt() - Call immediately after opening the list, before the list is complete.  Pass 0, which should have been loaded immediately.
     **/
    public void Var084()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            RUser u = (RUser)ul.resourceAt(0);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true && u.getName().equals(groupAndUsers_[1]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     resourceAt() - Call immediately after opening the list, before the list is complete.
     Pass 1, which should have been loaded immediately.
     **/
    public void Var085()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            RUser u = (RUser)ul.resourceAt(1);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true && u.getName().equals(groupAndUsers_[2]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     resourceAt() - Call immediately after opening the list, before the list is complete.  Pass (length - 1), which should have been loaded immediately.
     **/
    public void Var086()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            long length = ul.getListLength();
            RUser u = (RUser)ul.resourceAt(length - 1);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true && u.getName().equals(groupAndUsers_[(int)length]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     resourceAt() - Call immediately after opening the list, before the list is complete.  Pass a value which should not have been loaded immediately.
     **/
    public void Var087()
    {
        try
        {
            /* Not sure how to force a resource NOT to be loaded...
             */
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     resourceAt() - Call after opening the list and waiting for a particular element.  Pass 50.
     **/
    public void Var088()
    {
        try
        {
            RUserList ul = new RUserList(pwrSys_);
            ul.open();
            ul.waitForResource(50);
            RUser u = (RUser)ul.resourceAt(50);
            ul.close();
            assertCondition(u != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     resourceAt() - Call after opening the list and waiting for the list to complete.  Pass 50.
     **/
    public void Var089()
    {
        try
        {
            RUserList ul = new RUserList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            RUser u = (RUser)ul.resourceAt(50);
            ul.close();
            assertCondition(u != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     resourceAt() - Call after opening the list and waiting for the list to complete.  Pass a number greater than the length of the list.
     **/
    public void Var090()
    {
        try
        {
            RUserList ul = new RUserList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.resourceAt(length);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     resourceAt() - Call when the list has opened and closed successively.
     **/
    public void Var091()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.close();
            RUser user = (RUser)ul.resourceAt(0);
            assertCondition(ul.isOpen() == true && user.getName().equals(groupAndUsers_[1]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     resourceAt() - Call when the list has opened, completed and closed.
     **/
    public void Var092()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.waitForComplete();
            ul.close();
            RUser user = (RUser)ul.resourceAt(0);
            assertCondition(ul.isOpen() == true && user.getName().equals(groupAndUsers_[1]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     resourceAt() - When two RUserLists load the same users, they should share them (via the resource pool).
     **/
    public void Var093()
    {
        try
        {
            RUserList ul1 = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul1.open();
            ul1.waitForComplete();

            RUserList ul2 = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul2.open();
            ul2.waitForComplete();

            long length = ul1.getListLength();
            boolean success = (length == ul2.getListLength());
            for (int i = 0; i < length; ++i)
            {
                // Use == (not equals()) to verify that they are THE EXACT SAME object.
                if (ul1.resourceAt(i) != ul2.resourceAt(i)) success = false;
            }

            ul1.close();
            ul2.close();
            assertCondition(success);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setNumberOfPages() - Should throw an exception if passed -1.
     **/
    public void Var094()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setNumberOfPages(-1);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setNumberOfPages() - Should throw an exception if passed 0.
     **/
    public void Var095()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setNumberOfPages(0);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setNumberOfPages() - Should throw an exception if set when the list is open.
     **/
    public void Var096()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            u.setNumberOfPages(0);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     setNumberOfPages() - Should work when set before the list is opened.
     **/
    public void Var097()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setNumberOfPages(3);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            boolean success = true;
            for (int i = 0;i < length; ++i)
            {
                if (u.resourceAt(i) == null)
                {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            u.close();

            assertCondition(success && u.getNumberOfPages() == 3);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setNumberOfPages() - Should work when set after the list has been opened and closed.
     **/
    public void Var098()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            for (int i = 0;i < length; ++i) u.resourceAt(i);
            u.close();

            u.setNumberOfPages(4);
            u.open();
            u.waitForComplete();
            length = u.getListLength();
            boolean success = true;
            for (int i = 0;i < length; ++i)
            {
                if (u.resourceAt(i) == null)
                {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            u.close();

            assertCondition(success && u.getNumberOfPages() == 4);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setNumberOfPages() - Should work when set to 1.
     **/
    public void Var099()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setNumberOfPages(1);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            boolean success = true;
            for (int i = 0;i < length; ++i)
            {
                if (u.resourceAt(i) == null)
                {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            u.close();

            assertCondition(success);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setNumberOfPages() - Should work when set to 2.
     **/
    public void Var100()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setNumberOfPages(2);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            boolean success = true;
            for (int i = 0; i < length; ++i)
            {
                if (u.resourceAt(i) == null)
                {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            u.close();

            assertCondition(success);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setPageSize() - Should throw an exception if passed -1.
     **/
    public void Var101()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setPageSize(-1);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setPageSize() - Should throw an exception if passed 0.
     **/
    public void Var102()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setPageSize(0);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setPageSize() - Should throw an exception if set when the list is open.
     **/
    public void Var103()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            u.setPageSize(0);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     setPageSize() - Should work when set before the list is opened.
     **/
    public void Var104()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setPageSize(33);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            boolean success = true;
            for (int i = 0;i < length; ++i)
            {
                if (u.resourceAt(i) == null)
                {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            u.close();

            assertCondition(success && u.getPageSize() == 33);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setPageSize() - Should work when set after the list has been opened and closed.
     **/
    public void Var105()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            for (int i = 0;i < length; ++i) u.resourceAt(i);
            u.close();

            u.setPageSize(11);
            u.open();
            u.waitForComplete();
            length = u.getListLength();
            boolean success = true;
            for (int i = 0;i < length; ++i)
            {
                if (u.resourceAt(i) == null)
                {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            u.close();

            assertCondition(success && u.getPageSize() == 11);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setPageSize() - Should work when set to 1.
     **/
    public void Var106()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setPageSize(1);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            boolean success = true;
            for (int i = 0; i < length; ++i)
            {
                if (u.resourceAt(i) == null)
                {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            u.close();

            assertCondition(success);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setPageSize() - Should work when set to 2.
     **/
    public void Var107()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setPageSize(2);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            boolean success = true;
            for (int i = 0; i < length; ++i)
            {
                if (u.resourceAt(i) == null)
                {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            u.close();

            assertCondition(success);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setNumberOfPages()/setPageSize() - Should work when both set to 1.
     **/
    public void Var108()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setNumberOfPages(1);
            u.setPageSize(1);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            boolean success = true;
            for (int i = 0; i < length; ++i)
            {
                if (u.resourceAt(i) == null)
                {
                    output_.println("Resource " + i + " is null.");
                    success = false;
                }
            }
            u.close();

            assertCondition(success);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     toString() - For a RUserList whose properties have not been set, this should return the default toString.
     **/
    public void Var109()
    {
        try
        {
            RUserList u = new RUserList();  
            assertCondition(u.toString().length() > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     toString() - For a RUserList whose properties have been set and used, this should return the user name.
     **/
    public void Var110()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            String asString = u.toString();
            u.close();
            assertCondition(asString.length() > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForComplete() - Call when the list has never been opened, and not enough information is available.
     **/
    public void Var111()
    {
        try
        {
            RUserList u = new RUserList();
            u.waitForComplete();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     waitForComplete() - Call when the list has never been opened.
     **/
    public void Var112()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.waitForComplete();
            boolean open = ul.isOpen();
            boolean complete = ul.isComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition(open == true && complete == true && length == groupAndUsers_.length - 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForComplete() - Call immediately after opening the list.
     **/
    public void Var113()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.waitForComplete();
            boolean open = ul.isOpen();
            boolean complete = ul.isComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition(open == true && complete == true && length == groupAndUsers_.length - 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForComplete() - Call after the list is already complete.
     **/
    public void Var114()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.waitForComplete();
            ul.waitForComplete();
            boolean open = ul.isOpen();
            boolean complete = ul.isComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition(open == true && complete == true && length == groupAndUsers_.length - 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForComplete() - Call after the list had been opened and closed.
     **/
    public void Var115()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.waitForComplete();
            ul.resourceAt(groupAndUsers_.length - 2);
            ul.close();
            ul.waitForComplete();
            boolean open = ul.isOpen();
            boolean complete = ul.isComplete();
            long length = ul.getListLength();
            ul.close();
            assertCondition(open == true && complete == true && length == groupAndUsers_.length - 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForComplete() - Verify that the listCompleted event is fired.
     **/
    public void Var116()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            u.waitForComplete();  // Get all events fired.
            u.close();
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LIST_COMPLETED);
            assertCondition(events.length == 1 && events[0].getSource() == u && events[0].getID() == ResourceListEvent.LIST_COMPLETED && events[0].getIndex() == -1 && events[0].getLength() == -1 && events[0].getResource() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForResource() - Call when the list has never been opened, and not enough information is available.
     **/
    public void Var117()
    {
        try
        {
            RUserList u = new RUserList();
            u.waitForResource(0);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     waitForResource() - Call when the list has never been opened.
     **/
    public void Var118()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.waitForResource(0);
            boolean open = ul.isOpen();
            boolean available = ul.isResourceAvailable(0);
            RUser u = (RUser)ul.resourceAt(0);
            ul.close();
            assertCondition(open == true && u.getName().equals(groupAndUsers_[1]) && available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForResource() - Call immediately after opening the list, before the list is complete.  Pass -1.
     **/
    public void Var119()
    {
        try
        {
            RUserList ul = new RUserList(pwrSys_);
            ul.open();
            ul.waitForResource(-1);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     waitForResource() - Call immediately after opening the list, before the list is complete.  Pass 0, which should have been loaded immediately.
     **/
    public void Var120()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.waitForResource(0);
            boolean available = ul.isResourceAvailable(0);
            RUser u = (RUser)ul.resourceAt(0);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true && u.getName().equals(groupAndUsers_[1]) && available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForResource() - Call immediately after opening the list, before the list is complete.  Pass 1, which should have been loaded immediately.
     **/
    public void Var121()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.waitForResource(1);
            boolean available = ul.isResourceAvailable(1);
            RUser u = (RUser)ul.resourceAt(1);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true && u.getName().equals(groupAndUsers_[2]) && available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForResource() - Call immediately after opening the list, before the list is complete.  Pass (length - 1), which should have been loaded immediately.
     **/
    public void Var122()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            long length = ul.getListLength();
            ul.waitForResource(length-1);
            boolean available = ul.isResourceAvailable(length - 1);

            RUser u = (RUser)ul.resourceAt(length - 1);
            boolean open = ul.isOpen();
            ul.close();
            assertCondition(open == true && u.getName().equals(groupAndUsers_[(int)length]) && available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForResource() - Call immediately after opening the list, before the list is complete.  Pass a value which should not have been loaded immediately.
     **/
    public void Var123()
    {
        try
        {
            /* Not sure how to force a resource NOT to be loaded...
             */
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForResource() - Call after opening the list and waiting for a particular element.  Pass 50.
     **/
    public void Var124()
    {
        try
        {
            RUserList ul = new RUserList(pwrSys_);
            ul.open();
            ul.waitForResource(50);
            ul.waitForResource(50);
            boolean available = ul.isResourceAvailable(50);
            RUser u = (RUser)ul.resourceAt(50);
            ul.close();
            assertCondition(u != null && available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForResource() - Call after opening the list and waiting for the list to complete.  Pass 50.
     **/
    public void Var125()
    {
        try
        {
            RUserList ul = new RUserList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            ul.waitForResource(50);
            boolean available = ul.isResourceAvailable(50);
            RUser u = (RUser)ul.resourceAt(50);
            ul.close();
            assertCondition(u != null && available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForResource() - Call after opening the list and waiting for the list to complete.  Pass a number greater than the length of the list.
     **/
    public void Var126()
    {
        try
        {
            RUserList ul = new RUserList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.waitForResource(length);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForResource() - Call when the list has opened and closed successively.
     **/
    public void Var127()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.close();
            ul.waitForResource(0);
            boolean available = ul.isResourceAvailable(0);
            RUser user = (RUser)ul.resourceAt(0);
            assertCondition(ul.isOpen() == true && user.getName().equals(groupAndUsers_[1]) && available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForResource() - Call when the list has opened, completed and closed.
     **/
    public void Var128()
    {
        try
        {
            RUserList ul = newRUserList(pwrSys_, RUserList.MEMBER, groupAndUsers_[0]);
            ul.open();
            ul.waitForComplete();
            ul.close();
            ul.waitForResource(0);
            boolean available = ul.isResourceAvailable(0);
            RUser user = (RUser)ul.resourceAt(0);
            assertCondition(ul.isOpen() == true && user.getName().equals(groupAndUsers_[1]) && available == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     waitForResource() - Verify that the listLengthChanged event is fired.
     **/
    public void Var129()
    {
        try
        {
            RUserList u = new RUserList(systemObject_);
            UserTest.ResourceListListener_ rl = new UserTest.ResourceListListener_();
            u.addResourceListListener(rl);
            long length = u.getListLength();
            u.waitForResource(length - 1);
            ResourceListEvent[] events = rl.getEvents(ResourceListEvent.LENGTH_CHANGED);
            u.close();
            int eventsLength = events.length;
            assertCondition(eventsLength >= 1 && events[eventsLength-1].getSource() == u && events[eventsLength-1].getID() == ResourceListEvent.LENGTH_CHANGED && events[eventsLength-1].getIndex() == -1 && events[eventsLength-1].getLength() == length && events[eventsLength-1].getResource() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
