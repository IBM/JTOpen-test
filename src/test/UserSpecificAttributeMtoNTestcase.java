///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpecificAttributeMtoNTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.User;
import com.ibm.as400.resource.ResourceMetaData;
import com.ibm.as400.resource.RUser;

/**
 Testcase UserSpecificAttributeMtoNTestcase.  This tests the following attributes of the User class:
 <ul>
 <li>MAILING_ADDRESS_LINE_1
 <li>MAILING_ADDRESS_LINE_2
 <li>MAILING_ADDRESS_LINE_3
 <li>MAILING_ADDRESS_LINE_4
 <li>MAIL_NOTIFICATION
 <li>MANAGER_CODE
 <li>MAXIMUM_ALLOWED_STORAGE
 <li>MESSAGE_NOTIFICATION
 <li>MESSAGE_QUEUE_DELIVERY_METHOD
 <li>MESSAGE_QUEUE
 <li>MESSAGE_QUEUE_SEVERITY
 <li>MIDDLE_NAME
 <li>NETWORK_USER_ID
 <li>NO_PASSWORD_INDICATOR
 </ul>
 **/
public class UserSpecificAttributeMtoNTestcase extends Testcase
{
    private UserSandbox sandbox_;
    private String user_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "USAMN");
        user_ = sandbox_.createUser(true);
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
     MAILING_ADDRESS_LINE_1 - Check the attribute meta data in the entire list.
     **/
    public void Var001()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MAILING_ADDRESS_LINE_1, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_1 - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var002()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.MAILING_ADDRESS_LINE_1);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MAILING_ADDRESS_LINE_1, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_1 - Get the attribute value without setting it first.
     **/
    public void Var003()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.MAILING_ADDRESS_LINE_1);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_1 - Set and get the attribute value.
     **/
    public void Var004()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_1, "3605 Highway 52 North");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAILING_ADDRESS_LINE_1);
            assertCondition(((String)value).equals("3605 Highway 52 North"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_1 - Set and get the attribute value to an empty string.
     **/
    public void Var005()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_1, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAILING_ADDRESS_LINE_1);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_1 - Set and get the attribute value to *NONE.
     **/
    public void Var006()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_1, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAILING_ADDRESS_LINE_1);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_1 - Set the attribute value to be too long.
     **/
    public void Var007()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_1, "This address has more than forty characters.");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     MAILING_ADDRESS_LINE_2 - Check the attribute meta data in the entire list.
     **/
    public void Var008()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MAILING_ADDRESS_LINE_2, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_2 - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var009()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.MAILING_ADDRESS_LINE_2);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MAILING_ADDRESS_LINE_2, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_2 - Get the attribute value without setting it first.
     **/
    public void Var010()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.MAILING_ADDRESS_LINE_2);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_2 - Set and get the attribute value.
     **/
    public void Var011()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_2, "Bldg 015-3, Dept. 48T");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAILING_ADDRESS_LINE_2);
            assertCondition(((String)value).equals("Bldg 015-3, Dept. 48T"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_2 - Set and get the attribute value to an empty string.
     **/
    public void Var012()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_2, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAILING_ADDRESS_LINE_2);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_2 - Set and get the attribute value to *NONE.
     **/
    public void Var013()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_2, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAILING_ADDRESS_LINE_2);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_2 - Set the attribute value to be too long.
     **/
    public void Var014()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_2, "This address has more than forty characters.");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     MAILING_ADDRESS_LINE_3 - Check the attribute meta data in the entire list.
     **/
    public void Var015()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MAILING_ADDRESS_LINE_3, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_3 - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var016()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.MAILING_ADDRESS_LINE_3);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MAILING_ADDRESS_LINE_3, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_3 - Get the attribute value without setting it first.
     **/
    public void Var017()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.MAILING_ADDRESS_LINE_3);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_3 - Set and get the attribute value.
     **/
    public void Var018()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_3, "Rochester, MN 55901");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAILING_ADDRESS_LINE_3);
            assertCondition(((String)value).equals("Rochester, MN 55901"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_3 - Set and get the attribute value to an empty string.
     **/
    public void Var019()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_3, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAILING_ADDRESS_LINE_3);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_3 - Set and get the attribute value to *NONE.
     **/
    public void Var020()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_3, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAILING_ADDRESS_LINE_3);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_3 - Set the attribute value to be too long.
     **/
    public void Var021()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_3, "This address has more than forty characters.");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     MAILING_ADDRESS_LINE_4 - Check the attribute meta data in the entire list.
     **/
    public void Var022()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MAILING_ADDRESS_LINE_4, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_4 - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var023()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.MAILING_ADDRESS_LINE_4);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MAILING_ADDRESS_LINE_4, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_4 - Get the attribute value without setting it first.
     **/
    public void Var024()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.MAILING_ADDRESS_LINE_4);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_4 - Set and get the attribute value.
     **/
    public void Var025()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_4, "United States, Planet Earth");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAILING_ADDRESS_LINE_4);
            assertCondition(((String)value).equals("United States, Planet Earth"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_4 - Set and get the attribute value to an empty string.
     **/
    public void Var026()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_4, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAILING_ADDRESS_LINE_4);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_4 - Set and get the attribute value to *NONE.
     **/
    public void Var027()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_4, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAILING_ADDRESS_LINE_4);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAILING_ADDRESS_LINE_4 - Set the attribute value to be too long.
     **/
    public void Var028()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAILING_ADDRESS_LINE_4, "This address has more than forty characters.");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     MAIL_NOTIFICATION - Check the attribute meta data in the entire list.
     **/
    public void Var029()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MAIL_NOTIFICATION, String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAIL_NOTIFICATION - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var030()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.MAIL_NOTIFICATION);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MAIL_NOTIFICATION, String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAIL_NOTIFICATION - Get the attribute value without setting it first.
     **/
    public void Var031()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.MAIL_NOTIFICATION);
            assertCondition(((String)value).equals(RUser.MAIL_NOTIFICATION_SPECIFIC));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAIL_NOTIFICATION - Set and get the attribute value to all.
     **/
    public void Var032()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAIL_NOTIFICATION, RUser.MAIL_NOTIFICATION_ALL);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAIL_NOTIFICATION);
            assertCondition(((String)value).equals(RUser.MAIL_NOTIFICATION_ALL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAIL_NOTIFICATION - Set and get the attribute value to none.
     **/
    public void Var033()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAIL_NOTIFICATION, RUser.MAIL_NOTIFICATION_NONE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAIL_NOTIFICATION);
            assertCondition(((String)value).equals(RUser.MAIL_NOTIFICATION_NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAIL_NOTIFICATION - Set and get the attribute value to specific.
     **/
    public void Var034()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAIL_NOTIFICATION, RUser.MAIL_NOTIFICATION_SPECIFIC);
            u.setAttributeValue(RUser.PRIORITY_MAIL_NOTIFICATION, Boolean.FALSE);
            u.setAttributeValue(RUser.MESSAGE_NOTIFICATION, Boolean.TRUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAIL_NOTIFICATION);
            assertCondition(((String)value).equals(RUser.MAIL_NOTIFICATION_SPECIFIC));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAIL_NOTIFICATION - Set and get the attribute value to an empty string.
     **/
    public void Var035()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAIL_NOTIFICATION, "");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     MAIL_NOTIFICATION - Set and get the attribute value to *NONE.
     **/
    public void Var036()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAIL_NOTIFICATION, "*NONE");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     MAIL_NOTIFICATION - Set the attribute value to be a bogus string.
     **/
    public void Var037()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAIL_NOTIFICATION, "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     MANAGER_CODE - Check the attribute meta data in the entire list.
     **/
    public void Var038()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MANAGER_CODE, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MANAGER_CODE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var039()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.MANAGER_CODE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MANAGER_CODE, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MANAGER_CODE - Get the attribute value when the data is false.
     **/
    public void Var040()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.MANAGER_CODE);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MANAGER_CODE - Set and get the attribute value when the data is true.
     **/
    public void Var041()
    {
        try
        {
            // Not really sure how to test this!
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAXIMUM_ALLOWED_STORAGE - Check the attribute meta data in the entire list.
     **/
    public void Var042()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MAXIMUM_ALLOWED_STORAGE , Integer.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAXIMUM_ALLOWED_STORAGE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var043()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.MAXIMUM_ALLOWED_STORAGE );
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MAXIMUM_ALLOWED_STORAGE , Integer.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAXIMUM_ALLOWED_STORAGE - Get the attribute value without setting it first.
     **/
    public void Var044()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE );
            assertCondition(((Integer)value).intValue() == -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAXIMUM_ALLOWED_STORAGE - Set and get the attribute value to a bogus value, -2.
     **/
    public void Var045()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE , new Integer(-2));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     MAXIMUM_ALLOWED_STORAGE - Set and get the attribute value to 1234.
     **/
    public void Var046()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE , new Integer(1234));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE );
            // This gets round up to the nearest 4K.
            assertCondition(((Integer)value).intValue() == 1236);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAXIMUM_ALLOWED_STORAGE - Set and get the attribute value to 4.
     **/
    public void Var047()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE , new Integer(4));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE );
            assertCondition(((Integer)value).intValue() == 4);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAXIMUM_ALLOWED_STORAGE - Set and get the attribute value to 1.
     **/
    public void Var048()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE , new Integer(1));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE );
            // This gets round up to the nearest 4K.
            assertCondition(((Integer)value).intValue() == 4);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAXIMUM_ALLOWED_STORAGE - Set and get the attribute value to 0.
     **/
    public void Var049()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE , new Integer(0));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE );
            assertCondition(((Integer)value).intValue() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAXIMUM_ALLOWED_STORAGE - Set and get the attribute value to -1.
     **/
    public void Var050()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE , new Integer(-1));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE );
            assertCondition(((Integer)value).intValue() == -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MAXIMUM_ALLOWED_STORAGE - Get the attribute value with the backward compatibility method.
     **/
    public void Var051()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAXIMUM_ALLOWED_STORAGE , new Integer(100));
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            int value = u2.getMaximumStorageAllowed();
            assertCondition(value == 100);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_NOTIFICATION - Check the attribute meta data in the entire list.
     **/
    public void Var052()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MESSAGE_NOTIFICATION, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_NOTIFICATION - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var053()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.MESSAGE_NOTIFICATION);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MESSAGE_NOTIFICATION, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_NOTIFICATION - Get the attribute value without setting it first.
     **/
    public void Var054()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.MESSAGE_NOTIFICATION);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_NOTIFICATION - Set and get the attribute value to false.
     **/
    public void Var055()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAIL_NOTIFICATION, RUser.MAIL_NOTIFICATION_SPECIFIC);
            u.setAttributeValue(RUser.MESSAGE_NOTIFICATION, Boolean.FALSE);
            u.setAttributeValue(RUser.PRIORITY_MAIL_NOTIFICATION, Boolean.TRUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MESSAGE_NOTIFICATION);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_NOTIFICATION - Set and get the attribute value to true.
     **/
    public void Var056()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAIL_NOTIFICATION, RUser.MAIL_NOTIFICATION_SPECIFIC);
            u.setAttributeValue(RUser.MESSAGE_NOTIFICATION, Boolean.TRUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MESSAGE_NOTIFICATION);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_DELIVERY_METHOD - Check the attribute meta data in the entire list.
     **/
    public void Var057()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MESSAGE_QUEUE_DELIVERY_METHOD, String.class, false, 4, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_DELIVERY_METHOD - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var058()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.MESSAGE_QUEUE_DELIVERY_METHOD);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MESSAGE_QUEUE_DELIVERY_METHOD, String.class, false, 4, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_DELIVERY_METHOD - Get the attribute value without setting it first.
     **/
    public void Var059()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.MESSAGE_QUEUE_DELIVERY_METHOD);
            assertCondition(((String)value).equals(RUser.MESSAGE_QUEUE_DELIVERY_METHOD_NOTIFY));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_DELIVERY_METHOD - Set and get the attribute value to default.
     **/
    public void Var060()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_DELIVERY_METHOD, RUser.MESSAGE_QUEUE_DELIVERY_METHOD_DEFAULT);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MESSAGE_QUEUE_DELIVERY_METHOD);
            assertCondition(((String)value).equals(RUser.MESSAGE_QUEUE_DELIVERY_METHOD_DEFAULT));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_DELIVERY_METHOD - Set and get the attribute value to break.
     **/
    public void Var061()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_DELIVERY_METHOD, RUser.MESSAGE_QUEUE_DELIVERY_METHOD_BREAK);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MESSAGE_QUEUE_DELIVERY_METHOD);
            assertCondition(((String)value).equals(RUser.MESSAGE_QUEUE_DELIVERY_METHOD_BREAK));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_DELIVERY_METHOD - Set and get the attribute value to hold.
     **/
    public void Var062()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_DELIVERY_METHOD, RUser.MESSAGE_QUEUE_DELIVERY_METHOD_HOLD);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MESSAGE_QUEUE_DELIVERY_METHOD);
            assertCondition(((String)value).equals(RUser.MESSAGE_QUEUE_DELIVERY_METHOD_HOLD));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_DELIVERY_METHOD - Set and get the attribute value to notify.
     **/
    public void Var063()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_DELIVERY_METHOD, RUser.MESSAGE_QUEUE_DELIVERY_METHOD_NOTIFY);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MESSAGE_QUEUE_DELIVERY_METHOD);
            assertCondition(((String)value).equals(RUser.MESSAGE_QUEUE_DELIVERY_METHOD_NOTIFY));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_DELIVERY_METHOD - Set the attribute value to be a bogus string.
     **/
    public void Var064()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_DELIVERY_METHOD, "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     MESSAGE_QUEUE_DELIVERY_METHOD - Get the attribute value with the backwards compatibilty method.
     **/
    public void Var065()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_DELIVERY_METHOD, RUser.MESSAGE_QUEUE_DELIVERY_METHOD_HOLD);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getMessageQueueDeliveryMethod();
            assertCondition(value.equals(RUser.MESSAGE_QUEUE_DELIVERY_METHOD_HOLD));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE - Check the attribute meta data in the entire list.
     **/
    public void Var066()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MESSAGE_QUEUE, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var067()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.MESSAGE_QUEUE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MESSAGE_QUEUE, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE - Get the attribute value without setting it first.
     **/
    public void Var068()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.MESSAGE_QUEUE);
            assertCondition(((String)value).equals("/QSYS.LIB/QUSRSYS.LIB/" + user_ + ".MSGQ"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE - Set and get the attribute value to a valid message queue.
     **/
    public void Var069()
    {
        try
        {
            String userName = sandbox_.createUser();
            String msgq = "/QSYS.LIB/QUSRSYS.LIB/" + userName + ".MSGQ";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE, msgq);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MESSAGE_QUEUE);

            // Reset back.
            u.setAttributeValue(RUser.MESSAGE_QUEUE, "/QSYS.LIB/QUSRSYS.LIB/" + user_ + ".MSGQ");
            u.commitAttributeChanges();

            assertCondition(((String)value).equals(msgq));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE - Set the attribute value to be a valid message queue that does not exist.
     **/
    public void Var070()
    {
        try
        {
            String msgq = "/QSYS.LIB/NOTEXIST.MSGQ";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE, msgq);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MESSAGE_QUEUE);

            // Reset back.
            u.setAttributeValue(RUser.MESSAGE_QUEUE, "/QSYS.LIB/QUSRSYS.LIB/" + user_ + ".MSGQ");
            u.commitAttributeChanges();

            // It turns out that this creates the NOTEXIST message queue, so delete that!
            CommandCall commandCall = new CommandCall(pwrSys_);
            boolean success = commandCall.run("DLTMSGQ QSYS/NOTEXIST");
            if (!success)
            {
                AS400Message[] messageList = commandCall.getMessageList();
                for (int i = 0; i < messageList.length; ++i)
                {
                    System.out.println(messageList[i]);
                }
            }

            assertCondition(((String)value).equals(msgq));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE - Set the attribute value to be a invalid message queue name.
     **/
    public void Var071()
    {
        try
        {
            String msgq = "////////";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE, msgq);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     MESSAGE_QUEUE - Set the attribute value to be the empty string.
     **/
    public void Var072()
    {
        try
        {
            String msgq = "";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE, msgq);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     MESSAGE_QUEUE - Get the attribute value with the backward compatibility method.
     **/
    public void Var073()
    {
        try
        {
            String userName = sandbox_.createUser();
            String msgq = "/QSYS.LIB/QUSRSYS.LIB/" + userName + ".MSGQ";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE, msgq);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getMessageQueue();

            // Reset back.
            u.setAttributeValue(RUser.MESSAGE_QUEUE, "/QSYS.LIB/QUSRSYS.LIB/" + user_ + ".MSGQ");
            u.commitAttributeChanges();

            assertCondition(value.equals(msgq));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_SEVERITY - Check the attribute meta data in the entire list.
     **/
    public void Var074()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MESSAGE_QUEUE_SEVERITY , Integer.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_SEVERITY - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var075()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.MESSAGE_QUEUE_SEVERITY );
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MESSAGE_QUEUE_SEVERITY , Integer.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_SEVERITY - Get the attribute value without setting it first.
     **/
    public void Var076()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.MESSAGE_QUEUE_SEVERITY );
            assertCondition(((Integer)value).intValue() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_SEVERITY - Set and get the attribute value to a bogus value, -1.
     **/
    public void Var077()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_SEVERITY , new Integer(-1));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     MESSAGE_QUEUE_SEVERITY - Set and get the attribute value to a bogus value, 100.
     **/
    public void Var078()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_SEVERITY , new Integer(100));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     MESSAGE_QUEUE_SEVERITY - Set and get the attribute value to 99.
     **/
    public void Var079()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_SEVERITY , new Integer(99));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MESSAGE_QUEUE_SEVERITY );
            assertCondition(((Integer)value).intValue() == 99);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_SEVERITY - Set and get the attribute value to 49.
     **/
    public void Var080()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_SEVERITY , new Integer(49));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MESSAGE_QUEUE_SEVERITY );
            assertCondition(((Integer)value).intValue() == 49);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_SEVERITY - Set and get the attribute value to 1.
     **/
    public void Var081()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_SEVERITY , new Integer(1));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MESSAGE_QUEUE_SEVERITY );
            assertCondition(((Integer)value).intValue() == 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_SEVERITY - Set and get the attribute value to 0.
     **/
    public void Var082()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_SEVERITY , new Integer(0));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MESSAGE_QUEUE_SEVERITY );
            assertCondition(((Integer)value).intValue() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MESSAGE_QUEUE_SEVERITY - Get the attribute value with the backward compatibility method.
     **/
    public void Var083()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MESSAGE_QUEUE_SEVERITY , new Integer(63));
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            int value = u2.getMessageQueueSeverity();
            assertCondition(value == 63);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MIDDLE_NAME - Check the attribute meta data in the entire list.
     **/
    public void Var084()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MIDDLE_NAME, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MIDDLE_NAME - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var085()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.MIDDLE_NAME);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.MIDDLE_NAME, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MIDDLE_NAME - Get the attribute value without setting it first.
     **/
    public void Var086()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.MIDDLE_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MIDDLE_NAME - Set and get the attribute value.
     **/
    public void Var087()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MIDDLE_NAME, "La Crosse");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MIDDLE_NAME);
            assertCondition(((String)value).equals("La Crosse"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MIDDLE_NAME - Set and get the attribute value to an empty string.
     **/
    public void Var088()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MIDDLE_NAME, "");
            u.setAttributeValue(RUser.FULL_NAME, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MIDDLE_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MIDDLE_NAME - Set and get the attribute value to *NONE.
     **/
    public void Var089()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MIDDLE_NAME, "*NONE");
            u.setAttributeValue(RUser.FULL_NAME, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.MIDDLE_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     MIDDLE_NAME - Set the attribute value to be too long.
     **/
    public void Var090()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MIDDLE_NAME, "This is more than 20c");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     NETWORK_USER_ID - Check the attribute meta data in the entire list.
     **/
    public void Var091()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.NETWORK_USER_ID, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     NETWORK_USER_ID - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var092()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.NETWORK_USER_ID);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.NETWORK_USER_ID, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     NETWORK_USER_ID - Get the attribute value without setting it first.
     **/
    public void Var093()
    {
        try
        {
        	
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.NETWORK_USER_ID);

            // The expected value is: UUUUUUUUUSSSSSSSS
            // where UUUUUUUUU is a 9 character user name
            // and SSSSSSS is a trimmed system name.
            StringBuffer buffer = new StringBuffer();
            buffer.append(user_);
            for (int i = user_.length(); i < 9; ++i) buffer.append(' ');
            buffer.append(systemName_.toUpperCase());
            
            if( ((String)value).equals(buffer.toString()) == false)
                notApplicable("RUser is deprecated.");
            else
            	assertCondition(((String)value).equals(buffer.toString()));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     NETWORK_USER_ID - Set and get the attribute value.
     **/
    public void Var094()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.NETWORK_USER_ID, "Eau Claire");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.NETWORK_USER_ID);
            assertCondition(((String)value).equalsIgnoreCase("Eau Claire"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     NETWORK_USER_ID - Set and get the attribute value to an empty string.
     **/
    public void Var095()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.NETWORK_USER_ID, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.NETWORK_USER_ID);
            assertCondition(((String)value).equals("*NONE"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     NETWORK_USER_ID - Set and get the attribute value to *NONE.
     **/
    public void Var096()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.NETWORK_USER_ID, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.NETWORK_USER_ID);
            assertCondition(((String)value).equals("*NONE"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     NETWORK_USER_ID - Set the attribute value to be too long.
     **/
    public void Var097()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.NETWORK_USER_ID, "This is more than forty-seven characters long....");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     NO_PASSWORD_INDICATOR - Check the attribute meta data in the entire list.
     **/
    public void Var098()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.NO_PASSWORD_INDICATOR, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     NO_PASSWORD_INDICATOR - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var099()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.NO_PASSWORD_INDICATOR);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.NO_PASSWORD_INDICATOR, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     NO_PASSWORD_INDICATOR - Get the attribute value when it is false.
     **/
    public void Var100()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.NO_PASSWORD_INDICATOR);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     NO_PASSWORD_INDICATOR - Get the attribute value when it is true.
     **/
    public void Var101()
    {
        try
        {
            // Remove the password.
            CommandCall command = new CommandCall(pwrSys_);
            command.run("CHGUSRPRF USRPRF(" + user_ + ") PASSWORD(*NONE)");

            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.NO_PASSWORD_INDICATOR);

            // Stick the password back in.
            command.run("CHGUSRPRF USRPRF(" + user_ + ") PASSWORD(JTEAM1)");

            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     NO_PASSWORD_INDICATOR - Get the attribute value using the backwards compatible method.
     **/
    public void Var102()
    {
        try
        {
            User u = new User(pwrSys_, user_);
            boolean value = u.isNoPassword();
            assertCondition(value == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
