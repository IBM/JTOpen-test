///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpecificAttributeOtoQTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import java.util.Date;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.User;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;

import com.ibm.as400.resource.RUser;

/**
 Testcase UserSpecificAttributeOtoQTestcase.  This tests the following attributes of the User class:
 <ul>
 <li>OBJECT_AUDITING_VALUE
 <li>OFFICE
 <li>OR_NAME
 <li>OUTPUT_QUEUE
 <li>OWNER
 <li>PASSWORD_CHANGE_DATE
 <li>PASSWORD_EXPIRATION_INTERVAL
 <li>PREFERRED_NAME
 <li>PREVIOUS_SIGN_ON
 <li>PRINT_COVER_PAGE
 <li>PRINT_DEVICE
 <li>PRIORITY_MAIL_NOTIFICATION
 </ul>
 **/
@SuppressWarnings("deprecation")
public class UserSpecificAttributeOtoQTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserSpecificAttributeOtoQTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
    private UserSandbox sandbox_;
    private String user_;
    private String userInGroup_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "USAOQ");
        user_ = sandbox_.createUser(true);
        String[] groupAndUser = sandbox_.createGroupAndUsers(1);
        userInGroup_ = groupAndUser[1];
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
     OBJECT_AUDITING_VALUE - Check the attribute meta data in the entire list.
     **/
    public void Var001()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.OBJECT_AUDITING_VALUE, String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OBJECT_AUDITING_VALUE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var002()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.OBJECT_AUDITING_VALUE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.OBJECT_AUDITING_VALUE, String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OBJECT_AUDITING_VALUE - Get the attribute value without setting it first.
     **/
    public void Var003()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.OBJECT_AUDITING_VALUE);
            assertCondition(((String)value).equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OBJECT_AUDITING_VALUE - Set the attribute value to a bogus value.
     **/
    public void Var004()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OBJECT_AUDITING_VALUE, "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     OBJECT_AUDITING_VALUE - Set and get the attribute value when it is set to change.
     **/
    public void Var005()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OBJECT_AUDITING_VALUE, RUser.OBJECT_AUDITING_VALUE_CHANGE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.OBJECT_AUDITING_VALUE);
            assertCondition(((String)value).equals(RUser.OBJECT_AUDITING_VALUE_CHANGE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OBJECT_AUDITING_VALUE - Set and get the attribute value when it is set to all.
     **/
    public void Var006()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OBJECT_AUDITING_VALUE, RUser.OBJECT_AUDITING_VALUE_ALL);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.OBJECT_AUDITING_VALUE);
            assertCondition(((String)value).equals(RUser.OBJECT_AUDITING_VALUE_ALL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OBJECT_AUDITING_VALUE - Set and get the attribute value when it is set to none.
     **/
    public void Var007()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OBJECT_AUDITING_VALUE, RUser.NONE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.OBJECT_AUDITING_VALUE);
            assertCondition(((String)value).equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OBJECT_AUDITING_VALUE - Get the attribute value with the backwards compatibilty method.
     **/
    public void Var008()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OBJECT_AUDITING_VALUE, RUser.OBJECT_AUDITING_VALUE_ALL);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getObjectAuditingValue();
            assertCondition(value.equals(RUser.OBJECT_AUDITING_VALUE_ALL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OFFICE - Check the attribute meta data in the entire list.
     **/
    public void Var009()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.OFFICE, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OFFICE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var010()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.OFFICE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.OFFICE, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OFFICE - Get the attribute value without setting it first.
     **/
    public void Var011()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.OFFICE);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OFFICE - Set and get the attribute value.
     **/
    public void Var012()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OFFICE, "Oklahoma City");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.OFFICE);
            assertCondition(((String)value).equals("Oklahoma City"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OFFICE - Set and get the attribute value to an empty string.
     **/
    public void Var013()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OFFICE, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.OFFICE);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OFFICE - Set and get the attribute value to *NONE.
     **/
    public void Var014()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OFFICE, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.OFFICE);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OFFICE - Set the attribute value to be too long.
     **/
    public void Var015()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OFFICE, "More than sixteen");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     OR_NAME - Check the attribute meta data in the entire list.
     **/
    public void Var016()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.OR_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OR_NAME - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var017()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.OR_NAME);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.OR_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OR_NAME - Get the attribute value.
     **/
    public void Var018()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.OR_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OUTPUT_QUEUE - Check the attribute meta data in the entire list.
     **/
    public void Var019()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.OUTPUT_QUEUE, String.class, false, 2, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OUTPUT_QUEUE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var020()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.OUTPUT_QUEUE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.OUTPUT_QUEUE, String.class, false, 2, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OUTPUT_QUEUE - Get the attribute value without setting it first.
     **/
    public void Var021()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.OUTPUT_QUEUE);
            assertCondition(((String)value).equals(RUser.OUTPUT_QUEUE_WORK_STATION));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OUTPUT_QUEUE - Set and get the attribute value to device.
     **/
    public void Var022()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OUTPUT_QUEUE, RUser.OUTPUT_QUEUE_DEVICE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.OUTPUT_QUEUE);
            assertCondition(((String)value).equals(RUser.OUTPUT_QUEUE_DEVICE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OUTPUT_QUEUE - Set and get the attribute value to workstation.
     **/
    public void Var023()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OUTPUT_QUEUE, RUser.OUTPUT_QUEUE_WORK_STATION);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.OUTPUT_QUEUE);
            assertCondition(((String)value).equals(RUser.OUTPUT_QUEUE_WORK_STATION));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OUTPUT_QUEUE - Set and get the attribute value to a valid output queue .
     **/
    public void Var024()
    {
        try
        {
            String outq = "/QSYS.LIB/QPRINT.OUTQ";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OUTPUT_QUEUE, outq);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.OUTPUT_QUEUE);
            assertCondition(((String)value).equals(outq));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OUTPUT_QUEUE - Set the attribute value to be a valid output queue name that does not exist.
     **/
    public void Var025()
    {
        try
        {
            String outq = "/QSYS.LIB/NOTEXIST.OUTQ";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OUTPUT_QUEUE, outq);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.OUTPUT_QUEUE);
            assertCondition(((String)value).equals(outq));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OUTPUT_QUEUE - Set the attribute value to be a invalid output queue name.
     **/
    public void Var026()
    {
        try
        {
            String outq = "//////";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OUTPUT_QUEUE, outq);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     OUTPUT_QUEUE - Set the attribute value to be the empty string.
     **/
    public void Var027()
    {
        try
        {
            String outq = "";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OUTPUT_QUEUE, outq);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     OUTPUT_QUEUE - Get the attribute value with the backward compatibility method.
     **/
    public void Var028()
    {
        try
        {
            String outq = "/QSYS.LIB/JAVACTL.OUTQ";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OUTPUT_QUEUE, outq);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getOutputQueue();
            assertCondition(value.equals(outq));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OWNER - Check the attribute meta data in the entire list.
     **/
    public void Var029()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.OWNER , String.class, false, 2, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OWNER - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var030()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.OWNER );
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.OWNER , String.class, false, 2, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OWNER - Get the attribute value without setting it first.
     **/
    public void Var031()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.OWNER );
            assertCondition(((String)value).equals(RUser.OWNER_USER_PROFILE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OWNER - Set and get the attribute value to a bogus value, 1 word.
     **/
    public void Var032()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OWNER , "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     OWNER - Set and get the attribute value to a bogus value, 2 words.
     **/
    public void Var033()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OWNER , "bogus words");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     OWNER - Set and get the attribute value to group profile, when the user does not belong to a group.
     **/
    public void Var034()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OWNER , RUser.OWNER_GROUP_PROFILE);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     OWNER - Set and get the attribute value to group profile, when the user does belong to a group.
     **/
    public void Var035()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            u.setAttributeValue(RUser.OWNER , RUser.OWNER_GROUP_PROFILE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userInGroup_);
            Object value = u2.getAttributeValue(RUser.OWNER );
            assertCondition(((String)value).equals(RUser.OWNER_GROUP_PROFILE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OWNER - Set and get the attribute value to user profile, when the user does not belong to a group.
     **/
    public void Var036()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.OWNER , RUser.OWNER_USER_PROFILE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.OWNER );
            assertCondition(((String)value).equals(RUser.OWNER_USER_PROFILE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OWNER - Set and get the attribute value to user profile, when the user does belong to a group.
     **/
    public void Var037()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            u.setAttributeValue(RUser.OWNER , RUser.OWNER_USER_PROFILE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userInGroup_);
            Object value = u2.getAttributeValue(RUser.OWNER );
            assertCondition(((String)value).equals(RUser.OWNER_USER_PROFILE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     OWNER - Get the attribute value with the backward compatibility method.
     **/
    public void Var038()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            u.setAttributeValue(RUser.OWNER , RUser.OWNER_GROUP_PROFILE);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, userInGroup_);
            String value = u2.getOwner();
            assertCondition(value.equals(RUser.OWNER_GROUP_PROFILE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_CHANGE_DATE - Check the attribute meta data in the entire list.
     **/
    public void Var039()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PASSWORD_CHANGE_DATE, Date.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_CHANGE_DATE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var040()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.PASSWORD_CHANGE_DATE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PASSWORD_CHANGE_DATE, Date.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_CHANGE_DATE - Get the attribute value when the password has not changed since it was created.
     **/
    public void Var041()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.PASSWORD_CHANGE_DATE);

            // Verify that the difference between now and the password change
            // date is 3 hours (10800000 ms).  This accounts for slow machines
            // with time zone differences.
            long now = System.currentTimeMillis();
            long then = ((Date)value).getTime();
            long difference = now - then;
            assertCondition(Math.abs(difference) < 10800000, "Time difference is too large.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_CHANGE_DATE - Get the attribute value when the password has been changed since it was created.
     **/
    public void Var042()
    {
        try
        {
            CommandCall command = new CommandCall(pwrSys_);
            command.run("QSYS/CHGUSRPRF USRPRF(" + user_ + ") PASSWORD(JTEAM1)");

            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.PASSWORD_CHANGE_DATE);

            // Verify that the difference between now and the password change
            // date is 3 hours (10800000 ms).  This accounts for slow machines
            // with time zone differences.
            long now = System.currentTimeMillis();
            long then = ((Date)value).getTime();
            long difference = now - then;
            assertCondition(Math.abs(difference) < 10800000, "Time difference is too large.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_CHANGE_DATE - Get the attribute value with the backward compatibility method.
     **/
    public void Var043()
    {
        try
        {
            CommandCall command = new CommandCall(pwrSys_);
            command.run("QSYS/CHGUSRPRF USRPRF(" + user_ + ") PASSWORD(JTEAM1)");


            User u = new User(pwrSys_, user_);
            Date value = u.getPasswordLastChangedDate();

            // Verify that the difference between now and the password change
            // date is 3 hours (10800000 ms).  This accounts for slow machines
            // with time zone differences.
            long now = System.currentTimeMillis();
            long then = value.getTime();
            long difference = now - then;
            assertCondition(Math.abs(difference) < 10800000, "Time difference is too large. now="+now+" then="+then+" difference="+difference+" passwordLastChangedDate="+value);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_EXPIRATION_INTERVAL - Check the attribute meta data in the entire list.
     **/
    public void Var044()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PASSWORD_EXPIRATION_INTERVAL, Integer.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_EXPIRATION_INTERVAL - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var045()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PASSWORD_EXPIRATION_INTERVAL, Integer.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_EXPIRATION_INTERVAL - Get the attribute value without setting it first.
     **/
    public void Var046()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_EXPIRATION_INTERVAL - Set and get the attribute value to -2 (too low).
     **/
    public void Var047()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, Integer.valueOf(-2));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     PASSWORD_EXPIRATION_INTERVAL - Set and get the attribute value to -1.
     **/
    public void Var048()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, Integer.valueOf(-1));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_EXPIRATION_INTERVAL - Set and get the attribute value to 0.
     **/
    public void Var049()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, Integer.valueOf(0));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_EXPIRATION_INTERVAL - Set and get the attribute value to 1.
     **/
    public void Var050()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, Integer.valueOf(1));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_EXPIRATION_INTERVAL - Set and get the attribute value to 100.
     **/
    public void Var051()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, Integer.valueOf(100));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 100);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_EXPIRATION_INTERVAL - Set and get the attribute value to 366.
     **/
    public void Var052()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, Integer.valueOf(366));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL);
            assertCondition(((Integer)value).intValue() == 366);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PASSWORD_EXPIRATION_INTERVAL - Set and get the attribute value to 367 (too high).
     **/
    public void Var053()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, Integer.valueOf(367));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     PASSWORD_EXPIRATION_INTERVAL - Get the attribute value with the backward compatibility method.
     **/
    public void Var054()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, Integer.valueOf(14));
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            int value = u2.getPasswordExpirationInterval();
            assertCondition(value == 14);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PREFERRED_NAME - Check the attribute meta data in the entire list.
     **/
    public void Var055()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PREFERRED_NAME, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PREFERRED_NAME - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var056()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.PREFERRED_NAME);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PREFERRED_NAME, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PREFERRED_NAME - Get the attribute value without setting it first.
     **/
    public void Var057()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.PREFERRED_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PREFERRED_NAME - Set and get the attribute value.
     **/
    public void Var058()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PREFERRED_NAME, "New York");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PREFERRED_NAME);
            assertCondition(((String)value).equals("New York"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PREFERRED_NAME - Set and get the attribute value to an empty string.
     **/
    public void Var059()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PREFERRED_NAME, "");
            u.setAttributeValue(RUser.FIRST_NAME, "Fred");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PREFERRED_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PREFERRED_NAME - Set and get the attribute value to *NONE.
     **/
    public void Var060()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PREFERRED_NAME, "*NONE");
            u.setAttributeValue(RUser.FIRST_NAME, "Fred");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PREFERRED_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PREFERRED_NAME - Set the attribute value to be too long.
     **/
    public void Var061()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PREFERRED_NAME, "EvenMorethan25charactersplease");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     PREVIOUS_SIGN_ON - Check the attribute meta data in the entire list.
     **/
    public void Var062()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PREVIOUS_SIGN_ON, Date.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PREVIOUS_SIGN_ON - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var063()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.PREVIOUS_SIGN_ON);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PREVIOUS_SIGN_ON, Date.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PREVIOUS_SIGN_ON - Get the attribute value when the user has not signed on since it was created.
     **/
    public void Var064()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.PREVIOUS_SIGN_ON);
            Date asDate = (Date) value;
            assertCondition(asDate.equals(RUser.NO_DATE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PREVIOUS_SIGN_ON - Get the attribute value when the user has signed on since it was created.
     **/
    public void Var065()
    {
        try
        {
            // Sign-on with the user.
            AS400 system = new AS400(pwrSys_.getSystemName(), user_, "JTEAM1");
            system.setMustUseSockets(true);
            system.connectService(AS400.SIGNON);
            system.disconnectAllServices();

            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.PREVIOUS_SIGN_ON);

            // Verify that the difference between now and the password change
            // date is 3 hours (10800000 ms).  This accounts for slow machines
            // with time zone differences.
            long now = System.currentTimeMillis();
            long then = ((Date)value).getTime();
            long difference = now - then;
            system.close(); 
            assertCondition(Math.abs(difference) < 10800000);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PREVIOUS_SIGN_ON - Get the attribute value  with the backward compatibility method.
     **/
    public void Var066()
    {
        try
        {
            // Sign-on with the user.
            AS400 system = new AS400(pwrSys_.getSystemName(), user_, "JTEAM1");
            system.setMustUseSockets(true);
            system.connectService(AS400.SIGNON);
            system.disconnectAllServices();

            User u = new User(pwrSys_, user_);
            Date value = u.getPreviousSignedOnDate();

            // Verify that the difference between now and the password change
            // date is 3 hours (10800000 ms).  This accounts for slow machines
            // with time zone differences.
            long now = System.currentTimeMillis();
            long then = value.getTime();
            long difference = now - then;
            system.close();
            assertCondition(Math.abs(difference) < 10800000, "Time difference is too large.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_COVER_PAGE - Check the attribute meta data in the entire list.
     **/
    public void Var067()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PRINT_COVER_PAGE, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_COVER_PAGE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var068()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.PRINT_COVER_PAGE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PRINT_COVER_PAGE, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_COVER_PAGE - Get the attribute value without setting it first.
     **/
    public void Var069()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.PRINT_COVER_PAGE);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_COVER_PAGE - Set and get the attribute value to true.
     **/
    public void Var070()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PRINT_COVER_PAGE, Boolean.TRUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PRINT_COVER_PAGE);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_COVER_PAGE - Set and get the attribute value to false.
     **/
    public void Var071()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PRINT_COVER_PAGE, Boolean.FALSE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PRINT_COVER_PAGE);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_DEVICE - Check the attribute meta data in the entire list.
     **/
    public void Var072()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PRINT_DEVICE, String.class, false, 2, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_DEVICE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var073()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.PRINT_DEVICE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PRINT_DEVICE, String.class, false, 2, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_DEVICE - Get the attribute value without setting it first.
     **/
    public void Var074()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.PRINT_DEVICE);
            assertCondition(((String)value).equals(RUser.PRINT_DEVICE_WORK_STATION));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_DEVICE - Set and get the attribute value to system value.
     **/
    public void Var075()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PRINT_DEVICE, RUser.SYSTEM_VALUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PRINT_DEVICE);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_DEVICE - Set and get the attribute value to workstation.
     **/
    public void Var076()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PRINT_DEVICE, RUser.PRINT_DEVICE_WORK_STATION);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PRINT_DEVICE);
            assertCondition(((String)value).equals(RUser.PRINT_DEVICE_WORK_STATION));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_DEVICE - Set and get the attribute value to a valid print device.
     **/
    public void Var077()
    {
        try
        {
            String prtd = "PRT01";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PRINT_DEVICE, prtd);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PRINT_DEVICE);
            assertCondition(((String)value).equals(prtd));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_DEVICE - Set the attribute value to be a valid print device name that does not exist.
     **/
    public void Var078()
    {
        try
        {
            String prtd = "NOTEXIST";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PRINT_DEVICE, prtd);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PRINT_DEVICE);
            assertCondition(((String)value).equals(prtd));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRINT_DEVICE - Set the attribute value to be a invalid print device name.
     **/
    public void Var079()
    {
        try
        {
            String prtd = "This_is_a_great_variation";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PRINT_DEVICE, prtd);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     PRINT_DEVICE - Get the attribute value with the backward compatibility method.
     **/
    public void Var080()
    {
        try
        {
            String prtd = "PRT02";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PRINT_DEVICE, prtd);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getPrintDevice();
            assertCondition(value.equals(prtd));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRIORITY_MAIL_NOTIFICATION - Check the attribute meta data in the entire list.
     **/
    public void Var081()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PRIORITY_MAIL_NOTIFICATION, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRIORITY_MAIL_NOTIFICATION - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var082()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.PRIORITY_MAIL_NOTIFICATION);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.PRIORITY_MAIL_NOTIFICATION, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRIORITY_MAIL_NOTIFICATION - Get the attribute value without setting it first.
     **/
    public void Var083()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.PRIORITY_MAIL_NOTIFICATION);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRIORITY_MAIL_NOTIFICATION - Set and get the attribute value to false.
     **/
    public void Var084()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAIL_NOTIFICATION, RUser.MAIL_NOTIFICATION_SPECIFIC);
            u.setAttributeValue(RUser.PRIORITY_MAIL_NOTIFICATION, Boolean.FALSE);
            u.setAttributeValue(RUser.MESSAGE_NOTIFICATION, Boolean.TRUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PRIORITY_MAIL_NOTIFICATION);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     PRIORITY_MAIL_NOTIFICATION - Set and get the attribute value to true.
     **/
    public void Var085()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.MAIL_NOTIFICATION, RUser.MAIL_NOTIFICATION_SPECIFIC);
            u.setAttributeValue(RUser.PRIORITY_MAIL_NOTIFICATION, Boolean.TRUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.PRIORITY_MAIL_NOTIFICATION);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
