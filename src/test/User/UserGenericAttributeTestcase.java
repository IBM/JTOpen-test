///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserGenericAttributeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import java.util.Date;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceException;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;
import test.UserTest;
import test.UserTest.ResourceListener_;

import com.ibm.as400.resource.RUser;

/**
 Testcase UserGenericAttributeTestcase.  This tests the following methods of the RUser class, inherited from BufferedResource:
 <ul>
 <li>cancelAttributeChanges()
 <li>commitAttributeChanges()
 <li>createResoure()
 <li>delete()
 <li>getAttributeMetaData()
 <li>getAttributeMetaData()
 <li>getAttributeUnchangedValue()
 <li>getAttributeValue()
 <li>hasUncommittedAttributeChanges()
 <li>refreshAttributeValues()
 <li>setAttributeValue()
 </ul>
 **/
public class UserGenericAttributeTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserGenericAttributeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
    private static final String PREFIX2 = "UGAT2";

    private int count_ = 0;
    private Vector toDelete_ = new Vector();
    private UserSandbox sandbox_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "UGAT");
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        sandbox_.cleanup();
    }

    private boolean exists(String userName)
    {
        RUser u = new RUser(pwrSys_, userName);
        try
        {
            u.getAttributeValue(RUser.TEXT_DESCRIPTION);
        }
        catch (ResourceException e)
        {
            return false;
        }
        return true;
    }

    /**
     Checks a particular attribute meta data.
     **/
    public static boolean verifyAttributeMetaData(ResourceMetaData amd, Object attributeID, Class attributeType, boolean readOnly, int possibleValueCount, Object defaultValue, boolean valueLimited, boolean multipleAllowed)
    {
        return amd.areMultipleAllowed() == multipleAllowed && amd.getDefaultValue() == defaultValue && amd.getPossibleValues().length == possibleValueCount && amd.getPresentation() != null && amd.getType() == attributeType && amd.isReadOnly() == readOnly && amd.isValueLimited() == valueLimited && amd.toString().equals(attributeID);
    }

    /**
     Checks a particular attribute meta data.
     **/
    public static boolean verifyAttributeMetaData(ResourceMetaData[] amd, Object attributeID, Class attributeType, boolean readOnly, int possibleValueCount, Object defaultValue, boolean valueLimited, boolean multipleAllowed)
    {
        int found = -1;
        for (int i = 0; i < amd.length && found < 0; ++i)
        {
            if (amd[i].getID() == attributeID) found = i;
        }

        if (found < 0)
        {
            System.out.println("Attribute ID " + attributeID + " not found.");
            return false;
        }

        return verifyAttributeMetaData(amd[found], attributeID, attributeType, readOnly, possibleValueCount, defaultValue, valueLimited, multipleAllowed);
    }

    private String newUserName()
    {
        String userName = PREFIX2 + Integer.toString(++count_);
        toDelete_.addElement(userName);
        return userName;
    }

    /**
     cancelAttributeChanges() - Should do nothing when there is no system or user set.
     **/
    public void Var001()
    {
        try
        {
            RUser u = new RUser();
            u.cancelAttributeChanges();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     cancelAttributeChanges() - Should do nothing when no change has been made.
     **/
    public void Var002()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            u.cancelAttributeChanges();
            String textDescription2 = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(textDescription2.equals(textDescription));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     cancelAttributeChanges() - When 2 changes have been made.
     Verify that the changes are canceled.
     **/
    public void Var003()
    {
        try
        {
            String userName = sandbox_.createUser(true);
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            String company = (String)u.getAttributeValue(RUser.COMPANY);
            u.setAttributeValue(RUser.TEXT_DESCRIPTION, "This is only a test.");
            u.setAttributeValue(RUser.COMPANY, "Sybase");
            u.cancelAttributeChanges();
            String textDescription2 = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            String company2 = (String)u.getAttributeValue(RUser.COMPANY);
            assertCondition(textDescription2.equals(textDescription) && company2.equals(company));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     cancelAttributeChanges() - When 2 changes have been made.
     Verify that the changes are canceled, even when committed.
     **/
    public void Var004()
    {
        try
        {
            String userName = sandbox_.createUser(true);
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            String company = (String)u.getAttributeValue(RUser.COMPANY);
            u.setAttributeValue(RUser.TEXT_DESCRIPTION, "This is still a test.");
            u.setAttributeValue(RUser.COMPANY, "Qualcomm");
            u.cancelAttributeChanges();
            u.commitAttributeChanges();
            String textDescription2 = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            String company2 = (String)u.getAttributeValue(RUser.COMPANY);
            assertCondition(textDescription2.equals(textDescription) && company2.equals(company));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     cancelAttributeChanges() - Verify that a ResourceEvent is fired.
     **/
    public void Var005()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.cancelAttributeChanges();
            assertCondition(rl.eventCount_ == 1 && rl.event_.getID() == ResourceEvent.ATTRIBUTE_CHANGES_CANCELED && rl.event_.getAttributeID() == null && rl.event_.getValue() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     commitAttributeChanges() - No system or user set.
     **/
    public void Var006()
    {
        try
        {
            RUser u = new RUser();
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     commitAttributeChanges() - When the connection is bogus.
     **/
    public void Var007()
    {
        try
        {
            AS400 system = new AS400("Toolbox", "is", "cool");
            system.setGuiAvailable(false);
            RUser u = new RUser(system, "ClifRock");
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_BASIC);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     commitAttributeChanges() - When the user does not exist.
     **/
    public void Var008()
    {
        try
        {
            RUser u = new RUser(pwrSys_, "NOTEXIST");
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_BASIC);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     commitAttributeChanges() - Should do nothing when no change has been made.
     **/
    public void Var009()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            u.commitAttributeChanges();
            String textDescription2 = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(textDescription2.equals(textDescription));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     commitAttributeChanges() - Should commit the change when 2 changes have been made.
     **/
    public void Var010()
    {
        try
        {
            String userName = sandbox_.createUser(true);
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            String company = (String)u.getAttributeValue(RUser.COMPANY);
            u.setAttributeValue(RUser.TEXT_DESCRIPTION, "This is only a test.");
            u.setAttributeValue(RUser.COMPANY, "Oracle");
            u.commitAttributeChanges();
            RUser u2 = new RUser(pwrSys_, userName);
            String textDescription2 = (String)u2.getAttributeValue(RUser.TEXT_DESCRIPTION);
            String company2 = (String)u2.getAttributeValue(RUser.COMPANY);
            assertCondition(textDescription2.equals("This is only a test.") && company2.equalsIgnoreCase("Oracle"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     commitAttributeChanges() - Verify that a ResourceEvent is fired.
     **/
    public void Var011()
    {
        try
        {
            String userName = sandbox_.createUser(true);
            RUser u = new RUser(pwrSys_, userName);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.commitAttributeChanges();
            assertCondition(rl.eventCount_ == 1 && rl.event_.getID() == ResourceEvent.ATTRIBUTE_CHANGES_COMMITTED && rl.event_.getAttributeID() == null && rl.event_.getValue() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     delete() - When the system is not set.
     **/
    public void Var012()
    {
        try
        {
            String userName = sandbox_.createUser(false);
            RUser u = new RUser();
            u.setName(userName);
            u.delete();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     delete() - When the name is not set.
     **/
    public void Var013()
    {
        try
        {
            RUser u = new RUser();
            u.setSystem(pwrSys_);
            u.delete();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     delete() - A user without a directory entry.
     **/
    public void Var014()
    {
        try
        {
            String userName = sandbox_.createUser(false, false);
            RUser u = new RUser(pwrSys_, userName);
            u.delete();
            assertCondition(!exists(userName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     delete() - A user with a directory entry.
     **/
    public void Var015()
    {
        try
        {
            String userName = sandbox_.createUser(true, false);
            RUser u = new RUser(pwrSys_, userName);
            u.delete();
            assertCondition(!exists(userName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeMetaData() with 0 parameters - Verify that the array contains a few selected attributes.  I did not check everyone...we will do that when verifying each attribute.
     **/
    public void Var016()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean found = false;
            // I did not hardcode an exact length...otherwise, we have to change this every time we add a property.
            assertCondition(amd.length > 50 && verifyAttributeMetaData(amd, RUser.ACCOUNTING_CODE, String.class, false, 0, null, false, false) && verifyAttributeMetaData(amd, RUser.MESSAGE_QUEUE_DELIVERY_METHOD, String.class, false, 4, null, true, false) && verifyAttributeMetaData(amd, RUser.USER_PROFILE_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeMetaData() with 1 parameter - Pass null.
     **/
    public void Var017()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     getAttributeMetaData() with 1 parameter - Pass an invalid attribute ID.
     **/
    public void Var018()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(new Date());
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     getAttributeMetaData() with 1 parameter - Pass the first attribute ID.
     **/
    public void Var019()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.ACCOUNTING_CODE);
            assertCondition(verifyAttributeMetaData(amd, RUser.ACCOUNTING_CODE, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeMetaData() with 1 parameter - Pass a middle attribute ID.
     **/
    public void Var020()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.LOCAL_DATA_INDICATOR);
            assertCondition(verifyAttributeMetaData(amd, RUser.LOCAL_DATA_INDICATOR, String.class, true, 2, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeMetaData() with 1 parameter - Pass the last attribute ID.
     **/
    public void Var021()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.USER_PROFILE_NAME);
            assertCondition(verifyAttributeMetaData(amd, RUser.USER_PROFILE_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeMetaData() with 1 parameter - Try each of them.
     **/
    public void Var022()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for (int i = 0; i < amd.length; ++i)
            {
                boolean thisOne = verifyAttributeMetaData(u.getAttributeMetaData(amd[i].getID()), amd[i].getID(), amd[i].getType(), amd[i].isReadOnly(), amd[i].getPossibleValues().length, amd[i].getDefaultValue(), amd[i].isValueLimited(), amd[i].areMultipleAllowed());
                if (!thisOne)
                {
                    System.out.println("Comparison failed for: " + amd[i] + ".");
                    success = false;
                }
            }
            assertCondition(success);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeUnchangedValue() - When there is no connection.
     **/
    public void Var023()
    {
        try
        {
            RUser u = new RUser();
            Object value = u.getAttributeUnchangedValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     getAttributeUnchangedValue() - Pass null.
     **/
    public void Var024()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            Object value = u.getAttributeUnchangedValue(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     getAttributeUnchangedValue() - Pass an invalid attribute ID.
     **/
    public void Var025()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            Object value = u.getAttributeUnchangedValue(new AS400());
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     getAttributeUnchangedValue() - Pass an attribute ID, whose value has not been referenced.
     **/
    public void Var026()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(15));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            Object value = u.getAttributeUnchangedValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 15);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeUnchangedValue() - Pass an attribute ID, whose value has been referenced, but not changed.
     **/
    public void Var027()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(16));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            u.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            Object value = u.getAttributeUnchangedValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 16);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed, but not committed.
     **/
    public void Var028()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(17));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(18));
            Object value = u.getAttributeUnchangedValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 17);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed, not committed, but refreshed.
     **/
    public void Var029()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(17));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(18));
            u.refreshAttributeValues();
            Object value = u.getAttributeUnchangedValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 17);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed and committed.
     **/
    public void Var030()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(19));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(20));
            u.commitAttributeChanges();
            Object value = u.getAttributeUnchangedValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 20);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeValue() - When there is no connection.
     **/
    public void Var031()
    {
        try
        {
            RUser u = new RUser();
            Object value = u.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     getAttributeValue() - When the connection is bogus.
     **/
    public void Var032()
    {
        try
        {
            AS400 system = new AS400("This", "is", "bad");
            system.setGuiAvailable(false);
            RUser u = new RUser(system, "Friend");
            Object value = u.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     getAttributeValue() - When the user does not exist.
     **/
    public void Var033()
    {
        try
        {
            RUser u = new RUser(pwrSys_, "NOTEXIST");
            Object value = u.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     getAttributeValue() - Pass null.
     **/
    public void Var034()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            Object value = u.getAttributeValue(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     getAttributeValue() - Pass an invalid attribute ID.
     **/
    public void Var035()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            Object value = u.getAttributeValue("Yo");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     getAttributeValue() - Pass an attribute ID, whose value has not been referenced.
     **/
    public void Var036()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(22));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            Object value = u.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 22);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeValue() - Pass an attribute ID, whose value has been referenced, but not changed.
     **/
    public void Var037()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(23));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            u.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            Object value = u.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 23);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeValue() - Pass an attribute ID, whose value has been changed, but not committed.
     **/
    public void Var038()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(24));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(25));
            Object value = u.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 25);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeValue() - Pass an attribute ID, whose value has been changed, not committed, but refreshed.
     **/
    public void Var039()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(24));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(25));
            u.refreshAttributeValues();
            Object value = u.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 25);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeValue() - Pass an attribute ID, whose value has been changed and committed.
     **/
    public void Var040()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(26));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(27));
            u.commitAttributeChanges();
            Object value = u.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 27);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeValue() - Pass every attribute ID.
     **/
    public void Var041()
    {
        try
        {
            String userName = sandbox_.createUser(true);
            RUser u = new RUser(pwrSys_, userName);
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for (int i = 0; i < amd.length; ++i)
            {
                Object value = u.getAttributeValue(amd[i].getID());
                Class valueClass = value.getClass();
                Class type = amd[i].getType();

                // Validate the type.
                if (amd[i].areMultipleAllowed())
                {
                    if (!valueClass.isArray())
                    {
                        System.out.println("Error getting attribute " + amd[i] + ".");
                        System.out.println("Type array mismatch: " + valueClass + " is not an array, " + "but multiple values are allowed.");
                        success = false;
                    }
                    else
                    {
                        Class componentType = valueClass.getComponentType();
                        if (!componentType.equals(type))
                        {
                            System.out.println("Error getting attribute " + amd[i] + ".");
                            System.out.println("Type mismatch: " + componentType + " != " + type + ".");
                            success = false;
                        }
                    }
                }
                else if (!valueClass.equals(type))
                {
                    System.out.println("Error getting attribute " + amd[i] + ".");
                    System.out.println("Type mismatch: " + valueClass + " != " + type + ".");
                    success = false;
                }

                // Validate the value.
                if (amd[i].isValueLimited())
                {
                    Object[] possibleValues = amd[i].getPossibleValues();
                    boolean found = false;
                    if (amd[i].areMultipleAllowed())
                    {
                        Object[] asArray = (Object[])value;
                        for (int k = 0; k < asArray.length; ++k)
                        {
                            for (int j = 0; j < possibleValues.length && found == false; ++j)
                                if (possibleValues[j].equals(asArray[k])) found = true;

                            if (! found)
                            {
                                System.out.println("Error getting attribute " + amd[i] + ".");
                                System.out.println("Value: " + asArray[k] + " is not a valid possible value.");
                                success = false;
                            }
                        }
                    }
                    else
                    {
                        for (int j = 0; j < possibleValues.length && found == false; ++j)
                        {
                            if (possibleValues[j].equals(value)) found = true;
                        }

                        if (! found)
                        {
                            System.out.println("Error getting attribute " + amd[i] + ".");
                            System.out.println("Value: " + value + " is not a valid possible value.");
                            success = false;
                        }
                    }
                }
            }
            assertCondition(success);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeValue() - A user without a directory entry.  Get an attribute that does not require a directory entry.
     **/
    public void Var042()
    {
        try
        {
            String userName = sandbox_.createUser(false);
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(textDescription.startsWith(userName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeValue() - A user without a directory entry.  Get an attribute that does require a directory entry.
     **/
    public void Var043()
    {
        try
        {
            String userName = sandbox_.createUser(false);
            RUser u = new RUser(pwrSys_, userName);
            String company = (String)u.getAttributeValue(RUser.COMPANY);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     hasUncommittedAttributeChanges() - When there is no connection.
     **/
    public void Var044()
    {
        try
        {
            RUser u = new RUser();
            boolean pending = u.hasUncommittedAttributeChanges(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(pending == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     hasUncommittedAttributeChanges() - Pass null.
     **/
    public void Var045()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            boolean pending = u.hasUncommittedAttributeChanges(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     hasUncommittedAttributeChanges() - Pass an invalid attribute ID.
     **/
    public void Var046()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            boolean pending = u.hasUncommittedAttributeChanges("Go Toolbox");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has not been referenced.
     **/
    public void Var047()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(5));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            boolean pending = u.hasUncommittedAttributeChanges(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(pending == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been referenced, but not changed.
     **/
    public void Var048()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(6));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            u.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            boolean pending = u.hasUncommittedAttributeChanges(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(pending == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been changed, but not committed.
     **/
    public void Var049()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(7));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(8));
            boolean pending = u.hasUncommittedAttributeChanges(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(pending == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been changed, not committed, but refreshed.
     **/
    public void Var050()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(7));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(8));
            u.refreshAttributeValues();
            boolean pending = u.hasUncommittedAttributeChanges(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(pending == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been changed and committed.
     **/
    public void Var051()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(9));
            u2.commitAttributeChanges();

            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(10));
            u.commitAttributeChanges();
            boolean pending = u.hasUncommittedAttributeChanges(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(pending == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshAttributeValues() - When no system or user set.
     **/
    public void Var052()
    {
        try
        {
            RUser u = new RUser();
            u.refreshAttributeValues();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshAttributeValues() - When no change has been made.
     **/
    public void Var053()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            u.refreshAttributeValues();
            String textDescription2 = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(textDescription2.equals(textDescription));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshAttributeValues() - When 2 changes have been made for this object, but not committed.  Verify that the changes are not cancelled.
     **/
    public void Var054()
    {
        try
        {
            String userName = sandbox_.createUser(true);
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            String company = (String)u.getAttributeValue(RUser.COMPANY);
            u.setAttributeValue(RUser.TEXT_DESCRIPTION, "This is only another test.");
            u.setAttributeValue(RUser.COMPANY, "Sony");
            u.refreshAttributeValues();
            String textDescription2 = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            String company2 = (String)u.getAttributeValue(RUser.COMPANY);
            String textDescription2a = (String)u.getAttributeUnchangedValue(RUser.TEXT_DESCRIPTION);
            String company2a = (String)u.getAttributeUnchangedValue(RUser.COMPANY);
            assertCondition(textDescription2.equals("This is only another test.") && company2.equals("Sony") && textDescription2a.equals(textDescription) && company2a.equals(company));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshAttributeValues() - When 2 changes have been made for another RUser object.  Verify that the changes are reflected.
     **/
    public void Var055()
    {
        try
        {
            String userName = sandbox_.createUser(true);
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            String company = (String)u.getAttributeValue(RUser.COMPANY);

            RUser u2 = new RUser(pwrSys_, userName);
            u2.setAttributeValue(RUser.TEXT_DESCRIPTION, "This is another in a long series of tests.");
            u2.setAttributeValue(RUser.COMPANY, "Technics");
            u2.commitAttributeChanges();

            u.refreshAttributeValues();
            String textDescription2 = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            String company2 = (String)u.getAttributeValue(RUser.COMPANY);
            assertCondition(textDescription2.equals("This is another in a long series of tests.") && company2.equalsIgnoreCase("Technics"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     refreshAttributeValues() - Verify that a ResourceEvent is fired.
     **/
    public void Var056()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.refreshAttributeValues();
            assertCondition(rl.eventCount_ == 1 && rl.event_.getID() == ResourceEvent.ATTRIBUTE_VALUES_REFRESHED && rl.event_.getAttributeID() == null && rl.event_.getValue() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setAttributeValue() - When there is no connection.
     **/
    public void Var057()
    {
        try
        {
            RUser u = new RUser();
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_BASIC);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setAttributeValue() - Pass null for the attribute ID.
     **/
    public void Var058()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(null, new Integer(2));
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setAttributeValue() - Pass null for the value.
     **/
    public void Var059()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setAttributeValue() - Pass an invalid attribute ID.
     **/
    public void Var060()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(u, new Integer(3));
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setAttributeValue() - Set a read only attribute.
     **/
    public void Var061()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.USER_PROFILE_NAME, "egledm");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     setAttributeValue() - Pass a value which is the wrong type.
     **/
    public void Var062()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, new Integer(4));
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setAttributeValue() - Pass a value which is not one of the possible values.
     **/
    public void Var063()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, "Bogus");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setAttributeValue() - Pass an valid attribute ID, commit and verify.
     **/
    public void Var064()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_BASIC);
            u.commitAttributeChanges();
            RUser u2 = new RUser(pwrSys_, userName);
            Object value = u2.getAttributeValue(RUser.ASSISTANCE_LEVEL);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_INTERMEDIATE);
            u.commitAttributeChanges();
            u2.refreshAttributeValues();
            Object value2 = u2.getAttributeValue(RUser.ASSISTANCE_LEVEL);
            assertCondition(value.equals(RUser.ASSISTANCE_LEVEL_BASIC) && value2.equals(RUser.ASSISTANCE_LEVEL_INTERMEDIATE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setAttributeValue() - Pass an valid attribute ID, set twice.  Commit and verify that the second takes effect.
     **/
    public void Var065()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_BASIC);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_ADVANCED);
            u.commitAttributeChanges();
            RUser u2 = new RUser(pwrSys_, userName);
            Object value = u2.getAttributeValue(RUser.ASSISTANCE_LEVEL);
            assertCondition(value.equals(RUser.ASSISTANCE_LEVEL_ADVANCED));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setAttributeValue() - Pass an valid attribute ID, set and committed twice.  Verify that the second takes effect.
     **/
    public void Var066()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_ADVANCED);
            u.commitAttributeChanges();
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_INTERMEDIATE);
            u.commitAttributeChanges();
            RUser u2 = new RUser(pwrSys_, userName);
            Object value = u2.getAttributeValue(RUser.ASSISTANCE_LEVEL);
            assertCondition(value.equals(RUser.ASSISTANCE_LEVEL_INTERMEDIATE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setAttributeValue() - Set 2 attributes the are set using the same CL command.  Commit and verify that the both take effect.
     **/
    public void Var067()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_INTERMEDIATE);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(27));
            u.commitAttributeChanges();
            RUser u2 = new RUser(pwrSys_, userName);
            Object value1 = u2.getAttributeValue(RUser.ASSISTANCE_LEVEL);
            Object value2 = u2.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(value1.equals(RUser.ASSISTANCE_LEVEL_INTERMEDIATE) && ((Integer)value2).intValue() == 27);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setAttributeValue() - Set 2 attributes the are set using different CL commands.  Commit and verify that the both take effect.
     **/
    public void Var068()
    {
        try
        {
            String userName = sandbox_.createUser(true);
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_INTERMEDIATE);
            u.setAttributeValue(RUser.DEPARTMENT, "DEPTB");
            u.commitAttributeChanges();
            RUser u2 = new RUser(pwrSys_, userName);
            Object value1 = u2.getAttributeValue(RUser.ASSISTANCE_LEVEL);
            Object value2 = u2.getAttributeValue(RUser.DEPARTMENT);
            assertCondition(value1.equals(RUser.ASSISTANCE_LEVEL_INTERMEDIATE) && value2.equals("DEPTB"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setAttributeValue() - Verify that a ResourceEvent is fired.
     **/
    public void Var069()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_ADVANCED);
            assertCondition(rl.eventCount_ == 1 && rl.event_.getID() == ResourceEvent.ATTRIBUTE_VALUE_CHANGED && rl.event_.getAttributeID() == RUser.ASSISTANCE_LEVEL && rl.event_.getValue() == RUser.ASSISTANCE_LEVEL_ADVANCED);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setAttributeValue() - Pass every attribute ID.
     **/
    public void Var070()
    {
        try
        {
            String userName = sandbox_.createUser(true);
            RUser u = new RUser(pwrSys_, userName);
            RUser u2 = new RUser(pwrSys_, userName);
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for (int i = 0; i < amd.length; ++i)
            {
                if (!amd[i].isReadOnly())
                {
                    Object originalValue = u.getAttributeValue(amd[i].getID());

                    // First, just try setting the value to what it is already equal.
                    u.setAttributeValue(amd[i].getID(), originalValue);
                    u.commitAttributeChanges();

                    u.setAttributeValue(amd[i].getID(), originalValue);
                }
            }
            u2.commitAttributeChanges();

            assertCondition(success);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setAttributeValue() - A user without a directory entry.  Set an attribute that does not require a directory entry.
     **/
    public void Var071()
    {
        try
        {
            String userName = sandbox_.createUser(false);
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.TEXT_DESCRIPTION, "Testing once again");
            u.commitAttributeChanges();
            RUser u2 = new RUser(pwrSys_, userName);
            String textDescription = (String)u2.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(textDescription.equals("Testing once again"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAttributeValue() - A user without a directory entry.  Get an attribute that does require a directory entry.
     **/
    public void Var072()
    {
        try
        {
            String userName = sandbox_.createUser(false);
            RUser u = new RUser(pwrSys_, userName);
            u.setAttributeValue(RUser.COMPANY, "Western Digital");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }
}
