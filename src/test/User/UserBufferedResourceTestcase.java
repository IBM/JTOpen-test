///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserBufferedResourceTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import java.awt.Image;
import java.util.Date;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.Presentation;
import com.ibm.as400.resource.RUser;
import com.ibm.as400.resource.ResourceEvent;

import test.JTOpenTestEnvironment;
import test.Testcase;
import test.UserTest;

/**
 Testcase UserBufferedResourceTestcase.  This tests the following methods of the RUser class, inherited from BufferedResource:
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
public class UserBufferedResourceTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserBufferedResourceTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
    private UserSandbox sandbox_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {     

        sandbox_ = new UserSandbox(pwrSys_, "UBR", UserTest.COLLECTION.substring(UserTest.COLLECTION.length() - 1));
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        sandbox_.cleanup();
    }

    /**
     addPropertyChangeListener() - Pass null.
     **/
    public void Var001()
    {
        try
        {
            RUser u = new RUser();
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
            RUser u = new RUser(temp1, "washington");
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
     addResourceListener() - Pass null.
     **/
    public void Var003()
    {
        try
        {
            RUser u = new RUser();
            u.addResourceListener(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     addResourceListener() - Pass a listener.
     **/
    public void Var004()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            RUser u = new RUser(temp1, "oregon");
            u.setAttributeValue(RUser.COMPANY, "Microsoft");
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.setAttributeValue(RUser.COMPANY, "Sun");
            assertCondition(rl.eventCount_ == 1 && rl.event_.getID() == ResourceEvent.ATTRIBUTE_VALUE_CHANGED && rl.event_.getAttributeID().equals(RUser.COMPANY) && rl.event_.getValue().equals("Sun"));
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
            RUser u = new RUser();
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
            RUser u = new RUser(temp1, "oregon");
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
     equals() - Verify that a RUser is not equal to null.
     **/
    public void Var007()
    {
        try
        {
            RUser u = new RUser(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(null) == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     equals() - Verify that a RUser is not equal to something that is not a RUser.
     **/
    public void Var008()
    {
        try
        {
            RUser u = new RUser(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(new Date()) == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     equals() - Verify that a RUser is not equal to another RUser that it should not be.
     **/
    public void Var009()
    {
        try
        {
            RUser u = new RUser(systemObject_, systemObject_.getUserId());
            RUser u2 = new RUser(systemObject_, "FRANK");
            assertCondition(u.equals(u2) == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     equals() - Verify that a RUser is equal to itself.
     **/
    public void Var010()
    {
        try
        {
            RUser u = new RUser(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(u) == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     equals() - Verify that a RUser is equal to another RUser representing the same user, before either has been used.  The resource keys have not been set, so this should be false.
     **/
    public void Var011()
    {
        try
        {
            RUser u = new RUser(systemObject_, systemObject_.getUserId());
            RUser u2 = new RUser(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(u2) == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     equals() - Verify that a RUser is equal to another RUser representing the same user, after both have been used.  The resource keys should be set, and this should be true.
     **/
    public void Var012()
    {
        try
        {
            RUser u = new RUser(systemObject_, systemObject_.getUserId());
            RUser u2 = new RUser(systemObject_, systemObject_.getUserId());
            u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            u2.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(u.equals(u2) == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getPresentation() - Verify that the presentation has default information for a RUser whose properties have not been set.
     **/
    public void Var013()
    {
        try
        {
            RUser u = new RUser();
            Presentation p = u.getPresentation();
            String name = p.getName();
            String fullName = p.getFullName();
            String descriptionText = (String)p.getValue(Presentation.DESCRIPTION_TEXT);
            String helpText = (String)p.getValue(Presentation.HELP_TEXT);
            if (JTOpenTestEnvironment.isOS400 || JTOpenTestEnvironment.isLinux)
            {
                String asString = p.toString();
                assertCondition(name.equals("") && fullName.equals("") && descriptionText != null && helpText == null && asString.equals(""));
            }
            else
            {
                Image iconColor16 = (Image)p.getValue(Presentation.ICON_COLOR_16x16);
                Image iconColor32 = (Image)p.getValue(Presentation.ICON_COLOR_32x32);
                String asString = p.toString();
                assertCondition(name.equals("") && fullName.equals("") && descriptionText != null && helpText == null && iconColor16 != null && iconColor32 != null && asString.equals(""));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getPresentation() - Verify that the presentation has default information for a RUser whose properties have been set and used.
     **/
    public void Var014()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            Presentation p = u.getPresentation();
            String name = p.getName();
            String fullName = p.getFullName();
            String descriptionText = (String)p.getValue(Presentation.DESCRIPTION_TEXT);
            String helpText = (String)p.getValue(Presentation.HELP_TEXT);
            if (JTOpenTestEnvironment.isOS400 || JTOpenTestEnvironment.isLinux)
            {
                String asString = p.toString();
                assertCondition(name.equals(userName) && fullName.equals(userName) && descriptionText != null && helpText == null && asString.equals(userName), 
                    "textDescription="+textDescription);
            }
            else
            {
                Image iconColor16 = (Image)p.getValue(Presentation.ICON_COLOR_16x16);
                Image iconColor32 = (Image)p.getValue(Presentation.ICON_COLOR_32x32);
                String asString = p.toString();
                assertCondition(name.equals(userName) && fullName.equals(userName) && descriptionText != null && helpText == null && iconColor16 != null && iconColor32 != null && asString.equals(userName));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getPresentation() - Verify that passing null to the Presentation throws an exception.
     **/
    public void Var015()
    {
        try
        {
            RUser u = new RUser();
            Presentation p = u.getPresentation();
            p.getValue(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     getPresentation() - Verify that asking the Presentation for bogus information throws an exception.
     **/
    public void Var016()
    {
        try
        {
            RUser u = new RUser();
            Presentation p = u.getPresentation();
            assertCondition(p.getValue("THIS IS NOT RIGHT") == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getResourceKey() - For a RUser whose properties have not been set, the resource key should be null.
     **/
    public void Var017()
    {
        try
        {
            RUser u = new RUser();
            assertCondition(u.getResourceKey() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getResourceKey() - For a RUser whose properties have been set and used, the resource key should be set.
     **/
    public void Var018()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);

            StringBuffer buffer = new StringBuffer();
            buffer.append(RUser.class);
            buffer.append(':');
            buffer.append(pwrSys_.getSystemName());
            buffer.append(':');
            buffer.append(pwrSys_.getUserId());
            buffer.append(':');
            buffer.append(userName);
            String expected = buffer.toString();

            assertCondition(u.getResourceKey().equals(expected), 
                "textDescription="+textDescription);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     removePropertyChangeListener() - Pass null.
     **/
    public void Var019()
    {
        try
        {
            RUser u = new RUser();
            u.removePropertyChangeListener(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     removePropertyChangeListener() - Pass a listener that had never been added.
     **/
    public void Var020()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUser u = new RUser(temp1, "california");
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
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
     removePropertyChangeListener() - Pass a listener ihat had been added previously.
     **/
    public void Var021()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUser u = new RUser(temp1, "nevada");
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
     removeResourceListener() - Pass null.
     **/
    public void Var022()
    {
        try
        {
            RUser u = new RUser();
            u.removeResourceListener(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     removeResourceListener() - Pass a listener that had never been added.
     **/
    public void Var023()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUser u = new RUser(temp1, "california");
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.removeResourceListener(rl);
            u.setSystem(temp2);
            assertCondition(rl.eventCount_ == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     removeResourceListener() - Pass a listener that had been added previously.
     **/
    public void Var024()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            RUser u = new RUser(temp1, "idaho");
            u.setAttributeValue(RUser.COMPANY, "Microsoft");
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.removeResourceListener(rl);
            u.setAttributeValue(RUser.COMPANY, "Sun");
            assertCondition(rl.eventCount_ == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     removeVetoableChangeListener() - Pass null.
     **/
    public void Var025()
    {
        try
        {
            RUser u = new RUser();
            u.removeVetoableChangeListener(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     removeVetoableChangeListener() - Pass a listener that had never been added.
     **/
    public void Var026()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUser u = new RUser(temp1, "california");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
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
     removePropertyChangeListener() - Pass a listener ihat had been added previously.
     **/
    public void Var027()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUser u = new RUser(temp1, "nevada");
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
     toString() - For a RUser whose properties have not been set, this should return the default toString.
     **/
    public void Var028()
    {
        try
        {
            RUser u = new RUser();  
            assertCondition(u.toString() != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     toString() - For a RUser whose properties have been set and used,
     this should return the user name.
     **/
    public void Var029()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(u.toString().equals(userName), 
                "textDescription="+textDescription);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
