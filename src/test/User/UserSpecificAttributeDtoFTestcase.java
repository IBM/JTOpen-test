///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpecificAttributeDtoFTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import java.util.Date;

import com.ibm.as400.access.User;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;

import com.ibm.as400.resource.RUser;

/**
 Testcase UserSpecificAttributeDtoFTestcase.  This tests the following attributes of the User class:
 <ul>
 <li>DATE_PASSWORD_EXPIRES
 <li>DAYS_UNTIL_PASSWORD_EXPIRES
 <li>DEPARTMENT
 <li>DIGITAL_CERTIFICATE_INDICATOR
 <li>DISPLAY_SIGN_ON_INFORMATION
 <li>FAX_TELEPHONE_NUMBER
 <li>FIRST_NAME
 <li>FULL_NAME
 </ul>
 **/
@SuppressWarnings("deprecation")
public class UserSpecificAttributeDtoFTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserSpecificAttributeDtoFTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
    private UserSandbox sandbox_;
    private String user_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "USADF");
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
     DATE_PASSWORD_EXPIRES - Check the attribute meta data in the entire list.
     **/
    public void Var001()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.DATE_PASSWORD_EXPIRES, Date.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DATE_PASSWORD_EXPIRES - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var002()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.DATE_PASSWORD_EXPIRES);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.DATE_PASSWORD_EXPIRES, Date.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DATE_PASSWORD_EXPIRES - Get the attribute value when the password expiration interval is 10 days.
     **/
    public void Var003()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(10));
            u.commitAttributeChanges();

            // Verify that the difference between now and the date password expires is 10 days (864000000 ms).  Allow a 2 days (172800000 ms) error, since the date that comes back is set to midnight of the 9th day.
            Date nowPlusTen = new Date(System.currentTimeMillis() + 864000000);
            Date value = (Date)u.getAttributeValue(RUser.DATE_PASSWORD_EXPIRES);
            long difference = value.getTime() - nowPlusTen.getTime();

            assertCondition(Math.abs(difference) < 172800000);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DATE_PASSWORD_EXPIRES - Get the attribute value when the password does not expire.
     **/
    public void Var004()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(-1));
            u.commitAttributeChanges();

            Date value = (Date)u.getAttributeValue(RUser.DATE_PASSWORD_EXPIRES);
            assertCondition(value.equals(RUser.NO_DATE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DATE_PASSWORD_EXPIRES - Get the attribute value when the password is already expired.
     **/
    public void Var005()
    {
        try
        {
            // Create a new user so as not to expire to other user's password.
            String user = sandbox_.createUser();

            RUser u = new RUser(pwrSys_, user);
            u.setAttributeValue(RUser.SET_PASSWORD_TO_EXPIRE, Boolean.TRUE);
            u.commitAttributeChanges();

            Date value = (Date)u.getAttributeValue(RUser.DATE_PASSWORD_EXPIRES);
            assertCondition(value.equals(RUser.NO_DATE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DATE_PASSWORD_EXPIRES - Get the attribute value  with the backward compatibility method.
     **/
    public void Var006()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(5));
            u.commitAttributeChanges();

            // Verify that the difference between now and the date password expires is 10 days (432000000 ms).  Allow a 2 days (172800000 ms) error, since the date that comes back is set to midnight of the 4th day.
            Date nowPlusFive = new Date(System.currentTimeMillis() + 432000000);
            Date value = (Date)u.getAttributeValue(RUser.DATE_PASSWORD_EXPIRES);
            long difference = value.getTime() - nowPlusFive.getTime();

            assertCondition(Math.abs(difference) < 172800000);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DAYS_UNTIL_PASSWORD_EXPIRES - Check the attribute meta data in the entire list.
     **/
    public void Var007()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.DAYS_UNTIL_PASSWORD_EXPIRES, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DAYS_UNTIL_PASSWORD_EXPIRES - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var008()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.DAYS_UNTIL_PASSWORD_EXPIRES);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.DAYS_UNTIL_PASSWORD_EXPIRES, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DAYS_UNTIL_PASSWORD_EXPIRES - Get the attribute value when the password expiration interval is greater than 7 days.
     **/
    public void Var009()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(10));
            u.commitAttributeChanges();

            Object value = u.getAttributeValue(RUser.DAYS_UNTIL_PASSWORD_EXPIRES);
            assertCondition(((Integer)value).intValue() == -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DAYS_UNTIL_PASSWORD_EXPIRES - Get the attribute value when the password expiration interval is 7 days.
     **/
    public void Var010()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(7));
            u.commitAttributeChanges();

            Object value = u.getAttributeValue(RUser.DAYS_UNTIL_PASSWORD_EXPIRES);
            assertCondition(((Integer)value).intValue() == 7);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DAYS_UNTIL_PASSWORD_EXPIRES - Get the attribute value when the password expiration interval is 1 day.
     **/
    public void Var011()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(1));
            u.commitAttributeChanges();

            Object value = u.getAttributeValue(RUser.DAYS_UNTIL_PASSWORD_EXPIRES);
            assertCondition(((Integer)value).intValue() == 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DAYS_UNTIL_PASSWORD_EXPIRES - Get the attribute value when the password does not expire.
     **/
    public void Var012()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(-1));
            u.commitAttributeChanges();

            Object value = u.getAttributeValue(RUser.DAYS_UNTIL_PASSWORD_EXPIRES);
            assertCondition(((Integer)value).intValue() == -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DAYS_UNTIL_PASSWORD_EXPIRES - Get the attribute value when the password is already expired.
     **/
    public void Var013()
    {
        try
        {
            // Create a new user so as not to expire to other user's password.
            String user = sandbox_.createUser();

            RUser u = new RUser(pwrSys_, user);
            u.setAttributeValue(RUser.SET_PASSWORD_TO_EXPIRE, Boolean.TRUE);
            u.commitAttributeChanges();

            Object value = u.getAttributeValue(RUser.DAYS_UNTIL_PASSWORD_EXPIRES);
            assertCondition(((Integer)value).intValue() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DAYS_UNTIL_PASSWORD_EXPIRES - Get the attribute value with the backward compatibility method.
     **/
    public void Var014()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.PASSWORD_EXPIRATION_INTERVAL, new Integer(4));
            u.commitAttributeChanges();

            int value = ((Integer)u.getAttributeValue(RUser.DAYS_UNTIL_PASSWORD_EXPIRES)).intValue();
            assertCondition(value == 4);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DEPARTMENT - Check the attribute meta data in the entire list.
     **/
    public void Var015()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.DEPARTMENT, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DEPARTMENT - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var016()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.DEPARTMENT);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.DEPARTMENT, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DEPARTMENT - Get the attribute value without setting it first.
     **/
    public void Var017()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.DEPARTMENT);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DEPARTMENT - Set and get the attribute value.
     **/
    public void Var018()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.DEPARTMENT, "New York");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.DEPARTMENT);
            assertCondition(((String)value).equals("New York"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DEPARTMENT - Set and get the attribute value to an empty string.
     **/
    public void Var019()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.DEPARTMENT, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.DEPARTMENT);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DEPARTMENT - Set and get the attribute value to *NONE.
     **/
    public void Var020()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.DEPARTMENT, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.DEPARTMENT);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DEPARTMENT - Set the attribute value to be too long.
     **/
    public void Var021()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.DEPARTMENT, "morethanten");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     DIGITAL_CERTIFICATE_INDICATOR - Check the attribute meta data in the entire list.
     **/
    public void Var022()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.DIGITAL_CERTIFICATE_INDICATOR, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DIGITAL_CERTIFICATE_INDICATOR - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var023()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.DIGITAL_CERTIFICATE_INDICATOR);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.DIGITAL_CERTIFICATE_INDICATOR, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DIGITAL_CERTIFICATE_INDICATOR - Get the attribute value when there are no digitial certificates associated with this user.
     **/
    public void Var024()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.DIGITAL_CERTIFICATE_INDICATOR);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DIGITAL_CERTIFICATE_INDICATOR - Get the attribute value when there are digitial certificates associated with this user.
     **/
    public void Var025()
    {
        /*
         It is not trival to create a digital certificate and associate it
         with a user profile.  Dennis Schroepfer suggested using
         AS400CertificateUserProfileUtil, but it will be a bit involved.

         try
         {
         // How do I associate a digitial cerificate with the user?

         RUser u = new RUser(pwrSys_, user_);
         Object value = u.getAttributeValue(RUser.DIGITAL_CERTIFICATE_INDICATOR);
         assertCondition(((Boolean)value).booleanValue() == true);
         }
         catch (Exception e)
         {
         failed(e, "Unexpected Exception");
         }
         */
        succeeded();
    }

    /**
     DIGITAL_CERTIFICATE_INDICATOR - Get the attribute value  with the backward compatibility method.
     **/
    public void Var026()
    {
        try
        {
            User u = new User(pwrSys_, user_);
            boolean value = u.isWithDigitalCertificates();
            assertCondition(value == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DISPLAY_SIGN_ON_INFORMATION - Check the attribute meta data in the entire list.
     **/
    public void Var027()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.DISPLAY_SIGN_ON_INFORMATION , String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DISPLAY_SIGN_ON_INFORMATION - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var028()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.DISPLAY_SIGN_ON_INFORMATION);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.DISPLAY_SIGN_ON_INFORMATION , String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DISPLAY_SIGN_ON_INFORMATION - Get the attribute value without setting it first.
     **/
    public void Var029()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.DISPLAY_SIGN_ON_INFORMATION);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DISPLAY_SIGN_ON_INFORMATION - Set and get the attribute value to a bogus value.
     **/
    public void Var030()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.DISPLAY_SIGN_ON_INFORMATION , "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     DISPLAY_SIGN_ON_INFORMATION - Set and get the attribute value to system value.
     **/
    public void Var031()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.DISPLAY_SIGN_ON_INFORMATION , RUser.SYSTEM_VALUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.DISPLAY_SIGN_ON_INFORMATION);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DISPLAY_SIGN_ON_INFORMATION - Set and get the attribute value to yes.
     **/
    public void Var032()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.DISPLAY_SIGN_ON_INFORMATION , RUser.YES);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.DISPLAY_SIGN_ON_INFORMATION);
            assertCondition(((String)value).equals(RUser.YES));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DISPLAY_SIGN_ON_INFORMATION - Set and get the attribute value to no.
     **/
    public void Var033()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.DISPLAY_SIGN_ON_INFORMATION , RUser.NO);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.DISPLAY_SIGN_ON_INFORMATION);
            assertCondition(((String)value).equals(RUser.NO));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     DISPLAY_SIGN_ON_INFORMATION - Get the attribute value with the backward compatibility method.
     **/
    public void Var034()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.DISPLAY_SIGN_ON_INFORMATION , RUser.YES);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getDisplaySignOnInformation();
            assertCondition(value.equals(RUser.YES));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FAX_TELEPHONE_NUMBER - Check the attribute meta data in the entire list.
     **/
    public void Var035()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.FAX_TELEPHONE_NUMBER, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FAX_TELEPHONE_NUMBER - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var036()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.FAX_TELEPHONE_NUMBER);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.FAX_TELEPHONE_NUMBER, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FAX_TELEPHONE_NUMBER - Get the attribute value without setting it first.
     **/
    public void Var037()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.FAX_TELEPHONE_NUMBER);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FAX_TELEPHONE_NUMBER - Set and get the attribute value.
     **/
    public void Var038()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FAX_TELEPHONE_NUMBER, "Grand Rapids");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.FAX_TELEPHONE_NUMBER);
            assertCondition(((String)value).equals("Grand Rapids"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FAX_TELEPHONE_NUMBER - Set and get the attribute value to an empty string.
     **/
    public void Var039()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FAX_TELEPHONE_NUMBER, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.FAX_TELEPHONE_NUMBER);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FAX_TELEPHONE_NUMBER - Set and get the attribute value to *NONE.
     **/
    public void Var040()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FAX_TELEPHONE_NUMBER, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.FAX_TELEPHONE_NUMBER);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FAX_TELEPHONE_NUMBER - Set the attribute value to be too long.
     **/
    public void Var041()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FAX_TELEPHONE_NUMBER, "This is more than 32 characters long");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     FIRST_NAME - Check the attribute meta data in the entire list.
     **/
    public void Var042()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.FIRST_NAME, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FIRST_NAME - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var043()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.FIRST_NAME);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.FIRST_NAME, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FIRST_NAME - Get the attribute value without setting it first.
     **/
    public void Var044()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.FIRST_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FIRST_NAME - Set and get the attribute value.
     **/
    public void Var045()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FIRST_NAME, "St. Paul");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.FIRST_NAME);
            assertCondition(((String)value).equals("St. Paul"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FIRST_NAME - Set and get the attribute value to an empty string.
     **/
    public void Var046()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FIRST_NAME, "");
            u.setAttributeValue(RUser.FULL_NAME, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.FIRST_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FIRST_NAME - Set and get the attribute value to *NONE.
     **/
    public void Var047()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FIRST_NAME, "*NONE");
            u.setAttributeValue(RUser.FULL_NAME, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.FIRST_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FIRST_NAME - Set the attribute value to be too long.
     **/
    public void Var048()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FIRST_NAME, "This is more than 20c");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     FULL_NAME - Check the attribute meta data in the entire list.
     **/
    public void Var049()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.FULL_NAME, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FULL_NAME - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var050()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.FULL_NAME);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.FULL_NAME, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FULL_NAME - Get the attribute value without setting it first.
     **/
    public void Var051()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.FULL_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FULL_NAME - Set and get the attribute value.
     **/
    public void Var052()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FULL_NAME, "Eden Prairie");
            u.setAttributeValue(RUser.FIRST_NAME, "Joe");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.FULL_NAME);
            assertCondition(((String)value).equals("Eden Prairie"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FULL_NAME - Set and get the attribute value to an empty string.
     **/
    public void Var053()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FULL_NAME, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.FULL_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FULL_NAME - Set and get the attribute value to *NONE.
     **/
    public void Var054()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FULL_NAME, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.FULL_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FULL_NAME - Set and get the attribute value to *DFT.
     **/
    public void Var055()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FULL_NAME, "*DFT");
            u.setAttributeValue(RUser.LAST_NAME, "Rock");
            u.setAttributeValue(RUser.FIRST_NAME, "Clifton");
            u.setAttributeValue(RUser.MIDDLE_NAME, "Malcolm");
            u.setAttributeValue(RUser.PREFERRED_NAME, "Clif");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.FULL_NAME);
            assertCondition(((String)value).equalsIgnoreCase("Rock, Clifton Malcolm (Clif)"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     FULL_NAME - Set the attribute value to be too long.
     **/
    public void Var056()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.FULL_NAME, "This is more than the 50 characters which is allowed");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }
}
