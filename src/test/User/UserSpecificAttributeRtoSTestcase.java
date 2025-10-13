///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpecificAttributeRtoSTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.User;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;
import test.UserTest;

import com.ibm.as400.resource.RUser;

/**
 Testcase UserSpecificAttributeRtoSTestcase.  This tests the following attributes of the User class:
 <ul>
 <li>SET_PASSWORD_TO_EXPIRE
 <li>SIGN_ON_ATTEMPTS_NOT_VALID
 <li>SMTP_DOMAIN
 <li>SMTP_ROUTE
 <li>SMTP_USER_ID
 <li>SORT_SEQUENCE_TABLE
 <li>SPECIAL_AUTHORITIES
 <li>SPECIAL_ENVIRONMENT
 <li>STATUS
 <li>STORAGE_USED
 <li>SUPPLEMENTAL_GROUPS
 </ul>
 **/
@SuppressWarnings("deprecation")
public class UserSpecificAttributeRtoSTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserSpecificAttributeRtoSTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
    private UserSandbox sandbox_;
    private String user_;
    private String userInGroup_;
    private String group_;
    private String group1_;
    private String group2_;
    private String group3_;
    private String group4_;
    private String group5_;
    private String group6_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup()
      throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "USAR", UserTest.COLLECTION.substring(UserTest.COLLECTION.length() - 1));
        user_ = sandbox_.createUser(true);
        String[] groupAndUser = sandbox_.createGroupAndUsers(1);
        group_ = groupAndUser[0];
        userInGroup_ = groupAndUser[1];
        group1_ = sandbox_.createGroup();
        group2_ = sandbox_.createGroup();
        group3_ = sandbox_.createGroup();
        group4_ = sandbox_.createGroup();
        group5_ = sandbox_.createGroup();
        group6_ = sandbox_.createGroup();
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
     SET_PASSWORD_TO_EXPIRE - Check the attribute meta data in the entire list.
     **/
    public void Var001()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SET_PASSWORD_TO_EXPIRE, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SET_PASSWORD_TO_EXPIRE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var002()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.SET_PASSWORD_TO_EXPIRE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SET_PASSWORD_TO_EXPIRE, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SET_PASSWORD_TO_EXPIRE - Get the attribute value without setting it first.
     **/
    public void Var003()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.SET_PASSWORD_TO_EXPIRE);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SET_PASSWORD_TO_EXPIRE - Set and get this value to true.
     **/
    public void Var004()
    {
        try
        {
            // Create a new user so we don't expire the other one.
            String user = sandbox_.createUser();

            RUser u = new RUser(pwrSys_, user);
            u.setAttributeValue(RUser.SET_PASSWORD_TO_EXPIRE, Boolean.TRUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user);
            Object value = u2.getAttributeValue(RUser.SET_PASSWORD_TO_EXPIRE);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SET_PASSWORD_TO_EXPIRE - Set and get this value to false.
     **/
    public void Var005()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SET_PASSWORD_TO_EXPIRE, Boolean.FALSE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SET_PASSWORD_TO_EXPIRE);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SET_PASSWORD_TO_EXPIRE - Get the attribute value with the backwards compatibilty method.
     **/
    public void Var006()
    {
        try
        {
            // Create a new user so we don't expire the other one.
            String user = sandbox_.createUser();

            RUser u = new RUser(pwrSys_, user);
            u.setAttributeValue(RUser.SET_PASSWORD_TO_EXPIRE, Boolean.TRUE);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user);
            boolean value = u2.isPasswordSetExpire();
            assertCondition(value == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SIGN_ON_ATTEMPTS_NOT_VALID - Check the attribute meta data in the entire list.
     **/
    public void Var007()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SIGN_ON_ATTEMPTS_NOT_VALID, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SIGN_ON_ATTEMPTS_NOT_VALID - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var008()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.SIGN_ON_ATTEMPTS_NOT_VALID);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SIGN_ON_ATTEMPTS_NOT_VALID, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SIGN_ON_ATTEMPTS_NOT_VALID - Get the attribute value when there are no sign on attempts.
     **/
    public void Var009()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.SIGN_ON_ATTEMPTS_NOT_VALID);
            assertCondition(((Integer)value).intValue() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SIGN_ON_ATTEMPTS_NOT_VALID - Get the attribute value when there is a valid sign on attempt.
     **/
    public void Var010()
    {
        try
        {
            // A valid sign on attempt.
            AS400 system = new AS400(pwrSys_.getSystemName(), user_, "JTEAM1");
            system.connectService(AS400.COMMAND);
            system.disconnectAllServices();

            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.SIGN_ON_ATTEMPTS_NOT_VALID);
            system.close();
            assertCondition(((Integer)value).intValue() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SIGN_ON_ATTEMPTS_NOT_VALID - Get the attribute value when there is an invalid sign on attempt.
     **/
    public void Var011()
    {
        try
        {
            String user = sandbox_.createUser();

            // An invalid sign on attempt.
            AS400 system = new AS400(pwrSys_.getSystemName(), user, "JTEAM2");
            system.setGuiAvailable(false);
            try
            {
                system.connectService(AS400.COMMAND);
                System.out.println("The sign-on did not fail like we need it to.");
            }
            catch (Exception e)
            {
                // Good.  This means that the sign on failed.
            }

            RUser u = new RUser(pwrSys_, user);
            Object value = u.getAttributeValue(RUser.SIGN_ON_ATTEMPTS_NOT_VALID);
            system.close();
            assertCondition(((Integer)value).intValue() == 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SIGN_ON_ATTEMPTS_NOT_VALID - Get the attribute value when there are 2 invalid sign attempts.
     **/
    public void Var012()
    {
        try
        {
            String user = sandbox_.createUser();

            // 2 invalid sign on attempts.
            for (int i = 1; i <= 2; ++i)
            {
                AS400 system = new AS400(pwrSys_.getSystemName(), user, "JTEAM2");
                system.setGuiAvailable(false);
                try
                {
                    system.connectService(AS400.COMMAND);
                    System.out.println("The sign-on did not fail like we need it to.");
                }
                catch (Exception e)
                {
                    // Good.  This means that the sign on failed.
                }
                system.close();
            }

            RUser u = new RUser(pwrSys_, user);
            Object value = u.getAttributeValue(RUser.SIGN_ON_ATTEMPTS_NOT_VALID);
            assertCondition(((Integer)value).intValue() == 2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SIGN_ON_ATTEMPTS_NOT_VALID - Get the attribute value when there are invalid and valid sign on attempts.
     **/
    public void Var013()
    {
        try
        {
            String user = sandbox_.createUser();

            // An invalid sign on attempt.
            AS400 system = new AS400(pwrSys_.getSystemName(), user, "JTEAM2");
            system.setGuiAvailable(false);
            try
            {
                system.connectService(AS400.COMMAND);
                System.out.println("The sign-on did not fail like we need it to.");
            }
            catch (Exception e)
            {
                // Good.  This means that the sign on failed.
            }
            system.close();
            // A valid sign on attempt.
            AS400 system2 = new AS400(pwrSys_.getSystemName(), user, "JTEAM1");
            system2.connectService(AS400.COMMAND);
            system2.disconnectAllServices();
            system2.close();
            RUser u = new RUser(pwrSys_, user);
            Object value = u.getAttributeValue(RUser.SIGN_ON_ATTEMPTS_NOT_VALID);
            assertCondition(((Integer)value).intValue() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SIGN_ON_ATTEMPTS_NOT_VALID - Get the attribute value with the backwards compatibilty method.
     **/
    public void Var014()
    {
        try
        {
            String user = sandbox_.createUser();

            // An invalid sign on attempt.
            AS400 system = new AS400(pwrSys_.getSystemName(), user, "JTEAM2");
            system.setGuiAvailable(false);
            try
            {
                system.connectService(AS400.COMMAND);
                System.out.println("The sign-on did not fail like we need it to.");
            }
            catch (Exception e)
            {
                // Good.  This means that the sign on failed.
            }

            User u = new User(pwrSys_, user);
            int value = u.getSignedOnAttemptsNotValid();
            system.close();
            assertCondition(value == 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SMTP_DOMAIN - Check the attribute meta data in the entire list.
     **/
    public void Var015()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SMTP_DOMAIN, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SMTP_DOMAIN - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var016()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.SMTP_DOMAIN);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SMTP_DOMAIN, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SMTP_DOMAIN - Get the attribute value when there is no SMTP domain.
     **/
    public void Var017()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.SMTP_DOMAIN);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SMTP_ROUTE - Check the attribute meta data in the entire list.
     **/
    public void Var018()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SMTP_ROUTE, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SMTP_ROUTE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var019()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.SMTP_ROUTE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SMTP_ROUTE, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SMTP_ROUTE - Get the attribute value when there is no SMTP domain.
     **/
    public void Var020()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.SMTP_ROUTE);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SMTP_USER_ID - Check the attribute meta data in the entire list.
     **/
    public void Var021()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SMTP_USER_ID, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SMTP_USER_ID - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var022()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.SMTP_USER_ID);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SMTP_USER_ID, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SMTP_USER_ID - Get the attribute value when there is no SMTP domain.
     **/
    public void Var023()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.SMTP_USER_ID);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SORT_SEQUENCE_TABLE - Check the attribute meta data in the entire list.
     **/
    public void Var024()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SORT_SEQUENCE_TABLE, String.class, false, 4, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SORT_SEQUENCE_TABLE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var025()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.SORT_SEQUENCE_TABLE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SORT_SEQUENCE_TABLE, String.class, false, 4, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SORT_SEQUENCE_TABLE - Get the attribute value without setting it first.
     **/
    public void Var026()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.SORT_SEQUENCE_TABLE);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SORT_SEQUENCE_TABLE - Set and get the attribute value to hex.
     **/
    public void Var027()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SORT_SEQUENCE_TABLE, RUser.SORT_SEQUENCE_TABLE_HEX);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SORT_SEQUENCE_TABLE);
            assertCondition(((String)value).equals(RUser.SORT_SEQUENCE_TABLE_HEX));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SORT_SEQUENCE_TABLE - Set and get the attribute value to unique.
     **/
    public void Var028()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SORT_SEQUENCE_TABLE, RUser.SORT_SEQUENCE_TABLE_UNIQUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SORT_SEQUENCE_TABLE);
            assertCondition(((String)value).equals(RUser.SORT_SEQUENCE_TABLE_UNIQUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SORT_SEQUENCE_TABLE - Set and get the attribute value to shared.
     **/
    public void Var029()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SORT_SEQUENCE_TABLE, RUser.SORT_SEQUENCE_TABLE_SHARED);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SORT_SEQUENCE_TABLE);
            assertCondition(((String)value).equals(RUser.SORT_SEQUENCE_TABLE_SHARED));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SORT_SEQUENCE_TABLE - Set and get the attribute value to system value.
     **/
    public void Var030()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SORT_SEQUENCE_TABLE, RUser.SYSTEM_VALUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SORT_SEQUENCE_TABLE);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SORT_SEQUENCE_TABLE - Set and get the attribute value to a valid file.
     **/
    public void Var031()
    {
        try
        {
            String tableName = "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SORT_SEQUENCE_TABLE, tableName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SORT_SEQUENCE_TABLE);
            assertCondition(((String)value).equals(tableName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SORT_SEQUENCE_TABLE - Set the attribute value to be a valid file name that does not exist.
     **/
    public void Var032()
    {
        try
        {
            String tableName = "/QSYS.LIB/NOTEXIST.FILE";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SORT_SEQUENCE_TABLE, tableName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SORT_SEQUENCE_TABLE);
            assertCondition(((String)value).equals(tableName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SORT_SEQUENCE_TABLE - Set the attribute value to be a invalid file name.
     **/
    public void Var033()
    {
        try
        {
            String tableName = "//////";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SORT_SEQUENCE_TABLE, tableName);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     SORT_SEQUENCE_TABLE - Set the attribute value to be the empty string.
     **/
    public void Var034()
    {
        try
        {
            String tableName = "";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SORT_SEQUENCE_TABLE, tableName);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     SORT_SEQUENCE_TABLE - Get the attribute value with the backward compatibility method.
     **/
    public void Var035()
    {
        try
        {
            String tableName = "/QSYS.LIB/QSQPTABLE.FILE";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SORT_SEQUENCE_TABLE, tableName);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getSortSequenceTable();
            assertCondition(value.equals(tableName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_AUTHORITIES - Check the attribute meta data in the entire list.
     **/
    public void Var036()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SPECIAL_AUTHORITIES, String.class, false, 8, null, true, true));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_AUTHORITIES - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var037()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.SPECIAL_AUTHORITIES);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SPECIAL_AUTHORITIES, String.class, false, 8, null, true, true));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_AUTHORITIES - Get the attribute value without setting it first.
     **/
    public void Var038()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.SPECIAL_AUTHORITIES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_AUTHORITIES - Set and get the attribute value to an empty array.
     **/
    public void Var039()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_AUTHORITIES, new String[0]);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SPECIAL_AUTHORITIES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_AUTHORITIES - Set and get the attribute value to a 1 element array.
     **/
    public void Var040()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_AUTHORITIES, new String[] { RUser.SPECIAL_AUTHORITIES_ALL_OBJECT } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SPECIAL_AUTHORITIES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 1 && asArray[0].equals(RUser.SPECIAL_AUTHORITIES_ALL_OBJECT));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_AUTHORITIES - Set and get the attribute value to a 2 element array.
     **/
    public void Var041()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_AUTHORITIES, new String[] { RUser.SPECIAL_AUTHORITIES_SECURITY_ADMINISTRATOR, RUser.SPECIAL_AUTHORITIES_JOB_CONTROL } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SPECIAL_AUTHORITIES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 2 && asArray[0].equals(RUser.SPECIAL_AUTHORITIES_SECURITY_ADMINISTRATOR) && asArray[1].equals(RUser.SPECIAL_AUTHORITIES_JOB_CONTROL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_AUTHORITIES - Set and get the attribute value to a 5 element array.
     **/
    public void Var042()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_AUTHORITIES, new String[] { RUser.SPECIAL_AUTHORITIES_SERVICE, RUser.SPECIAL_AUTHORITIES_SAVE_SYSTEM, RUser.SPECIAL_AUTHORITIES_AUDIT, RUser.SPECIAL_AUTHORITIES_SPOOL_CONTROL, RUser.SPECIAL_AUTHORITIES_IO_SYSTEM_CONFIGURATION } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SPECIAL_AUTHORITIES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 5 && asArray[0].equals(RUser.SPECIAL_AUTHORITIES_SPOOL_CONTROL) && asArray[1].equals(RUser.SPECIAL_AUTHORITIES_SAVE_SYSTEM) && asArray[2].equals(RUser.SPECIAL_AUTHORITIES_SERVICE) && asArray[3].equals(RUser.SPECIAL_AUTHORITIES_AUDIT) && asArray[4].equals(RUser.SPECIAL_AUTHORITIES_IO_SYSTEM_CONFIGURATION));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_AUTHORITIES - Set and get the attribute value to several element array with one of the elements null.
     **/
    public void Var043()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_AUTHORITIES, new String[] { RUser.SPECIAL_AUTHORITIES_AUDIT, null, RUser.SPECIAL_AUTHORITIES_SECURITY_ADMINISTRATOR } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     SPECIAL_AUTHORITIES - Set and get the attribute value to several element array with one of the elements not valid.
     **/
    public void Var044()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_AUTHORITIES, new String[] { RUser.SPECIAL_AUTHORITIES_IO_SYSTEM_CONFIGURATION, RUser.SPECIAL_AUTHORITIES_ALL_OBJECT, "bogus", RUser.SPECIAL_AUTHORITIES_JOB_CONTROL } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     SPECIAL_AUTHORITIES - Set and get the attribute value to several element array with one of the elements specified twice.
     **/
    public void Var045()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_AUTHORITIES, new String[] { RUser.SPECIAL_AUTHORITIES_SAVE_SYSTEM, RUser.SPECIAL_AUTHORITIES_AUDIT, RUser.SPECIAL_AUTHORITIES_AUDIT, RUser.SPECIAL_AUTHORITIES_IO_SYSTEM_CONFIGURATION } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SPECIAL_AUTHORITIES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 3 && asArray[0].equals(RUser.SPECIAL_AUTHORITIES_SAVE_SYSTEM) && asArray[1].equals(RUser.SPECIAL_AUTHORITIES_AUDIT) && asArray[2].equals(RUser.SPECIAL_AUTHORITIES_IO_SYSTEM_CONFIGURATION));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_AUTHORITIES - Set and get the attribute value to several element array with all possible values specified.
     **/
    public void Var046()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_AUTHORITIES, new String[] { RUser.SPECIAL_AUTHORITIES_ALL_OBJECT, RUser.SPECIAL_AUTHORITIES_SECURITY_ADMINISTRATOR, RUser.SPECIAL_AUTHORITIES_JOB_CONTROL, RUser.SPECIAL_AUTHORITIES_SPOOL_CONTROL, RUser.SPECIAL_AUTHORITIES_SAVE_SYSTEM, RUser.SPECIAL_AUTHORITIES_SERVICE, RUser.SPECIAL_AUTHORITIES_AUDIT, RUser.SPECIAL_AUTHORITIES_IO_SYSTEM_CONFIGURATION } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SPECIAL_AUTHORITIES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 8 && asArray[0].equals(RUser.SPECIAL_AUTHORITIES_ALL_OBJECT) && asArray[1].equals(RUser.SPECIAL_AUTHORITIES_SECURITY_ADMINISTRATOR) && asArray[2].equals(RUser.SPECIAL_AUTHORITIES_JOB_CONTROL) && asArray[3].equals(RUser.SPECIAL_AUTHORITIES_SPOOL_CONTROL) && asArray[4].equals(RUser.SPECIAL_AUTHORITIES_SAVE_SYSTEM) && asArray[5].equals(RUser.SPECIAL_AUTHORITIES_SERVICE) && asArray[6].equals(RUser.SPECIAL_AUTHORITIES_AUDIT) && asArray[7].equals(RUser.SPECIAL_AUTHORITIES_IO_SYSTEM_CONFIGURATION));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_AUTHORITIES - Get the attribute value with the backwards compatibility method.
     **/
    public void Var047()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_AUTHORITIES, new String[] { RUser.SPECIAL_AUTHORITIES_IO_SYSTEM_CONFIGURATION, RUser.SPECIAL_AUTHORITIES_SERVICE, RUser.SPECIAL_AUTHORITIES_SECURITY_ADMINISTRATOR });
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String[] value = u2.getSpecialAuthority();
            assertCondition(value.length == 3 && value[0].equals(RUser.SPECIAL_AUTHORITIES_SECURITY_ADMINISTRATOR) && value[1].equals(RUser.SPECIAL_AUTHORITIES_SERVICE) && value[2].equals(RUser.SPECIAL_AUTHORITIES_IO_SYSTEM_CONFIGURATION));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_ENVIRONMENT - Check the attribute meta data in the entire list.
     **/
    public void Var048()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SPECIAL_ENVIRONMENT , String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_ENVIRONMENT - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var049()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.SPECIAL_ENVIRONMENT);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SPECIAL_ENVIRONMENT , String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_ENVIRONMENT - Get the attribute value without setting it first.
     **/
    public void Var050()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.SPECIAL_ENVIRONMENT);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_ENVIRONMENT - Set and get the attribute value to a bogus value.
     **/
    public void Var051()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_ENVIRONMENT , "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     SPECIAL_ENVIRONMENT - Set and get the attribute value to System 36.
     **/
    public void Var052()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_ENVIRONMENT , RUser.SPECIAL_ENVIRONMENT_SYSTEM_36);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SPECIAL_ENVIRONMENT);
            assertCondition(((String)value).equals(RUser.SPECIAL_ENVIRONMENT_SYSTEM_36));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_ENVIRONMENT - Set and get the attribute value to system value.
     **/
    public void Var053()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_ENVIRONMENT , RUser.SYSTEM_VALUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SPECIAL_ENVIRONMENT);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_ENVIRONMENT - Set and get the attribute value to none.
     **/
    public void Var054()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_ENVIRONMENT , RUser.NONE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.SPECIAL_ENVIRONMENT);
            assertCondition(((String)value).equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SPECIAL_ENVIRONMENT - Get the attribute value with the backward compatibility method.
     **/
    public void Var055()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SPECIAL_ENVIRONMENT , RUser.SPECIAL_ENVIRONMENT_SYSTEM_36);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getSpecialEnvironment();
            assertCondition(value.equals(RUser.SPECIAL_ENVIRONMENT_SYSTEM_36));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     STATUS - Check the attribute meta data in the entire list.
     **/
    public void Var056()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.STATUS , String.class, false, 2, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     STATUS - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var057()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.STATUS);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.STATUS , String.class, false, 2, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     STATUS - Get the attribute value without setting it first.
     **/
    public void Var058()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.STATUS);
            assertCondition(((String)value).equals(RUser.STATUS_ENABLED));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     STATUS - Set and get the attribute value to a bogus value.
     **/
    public void Var059()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.STATUS , "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     STATUS - Set and get the attribute value to disabled.
     **/
    public void Var060()
    {
        try
        {
            // Create a new user so we don't disable the other one.
            String user = sandbox_.createUser();

            RUser u = new RUser(pwrSys_, user);
            u.setAttributeValue(RUser.STATUS , RUser.STATUS_NOT_ENABLED);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user);
            Object value = u2.getAttributeValue(RUser.STATUS);
            assertCondition(((String)value).equals(RUser.STATUS_NOT_ENABLED));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     STATUS - Set and get the attribute value to enabled.
     **/
    public void Var061()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.STATUS , RUser.STATUS_ENABLED);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.STATUS);
            assertCondition(((String)value).equals(RUser.STATUS_ENABLED));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     STATUS - Get the attribute value with the backward compatibility method.
     **/
    public void Var062()
    {
        try
        {
            // Create a new user so we don't disable the other one.
            String user = sandbox_.createUser();

            RUser u = new RUser(pwrSys_, user);
            u.setAttributeValue(RUser.STATUS , RUser.STATUS_NOT_ENABLED);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user);
            String value = u2.getStatus();
            assertCondition(value.equals(RUser.STATUS_NOT_ENABLED));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     STORAGE_USED - Check the attribute meta data in the entire list.
     **/
    public void Var063()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.STORAGE_USED, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     STORAGE_USED - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var064()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.STORAGE_USED);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.STORAGE_USED, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     STORAGE_USED - Get the attribute value.
     **/
    public void Var065()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.STORAGE_USED);
            assertCondition(((Integer)value).intValue() > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     STORAGE_USED - Get the attribute value with the backwards compatibilty method.
     **/
    public void Var066()
    {
        try
        {
            User u = new User(pwrSys_, user_);
            int value = u.getStorageUsed();
            assertCondition(value > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Check the attribute meta data in the entire list.
     **/
    public void Var067()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SUPPLEMENTAL_GROUPS, String[].class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var068()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.SUPPLEMENTAL_GROUPS);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.SUPPLEMENTAL_GROUPS, String[].class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Get the attribute value without setting it first.
     **/
    public void Var069()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            Object value = u.getAttributeValue(RUser.SUPPLEMENTAL_GROUPS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Set and get the attribute value to an empty array.
     **/
    public void Var070()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            u.setAttributeValue(RUser.SUPPLEMENTAL_GROUPS, new String[0]);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userInGroup_);
            Object value = u2.getAttributeValue(RUser.SUPPLEMENTAL_GROUPS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Set and get the attribute value to an empty array assuming there was already supplemental groups assigned.
     **/
    public void Var071()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            u.setAttributeValue(RUser.SUPPLEMENTAL_GROUPS, new String[] { group1_ });
            u.commitAttributeChanges();

            RUser u3 = new RUser(pwrSys_, userInGroup_);
            u3.setAttributeValue(RUser.SUPPLEMENTAL_GROUPS, new String[0]);
            u3.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userInGroup_);
            Object value = u2.getAttributeValue(RUser.SUPPLEMENTAL_GROUPS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Set and get the attribute value to a 1 element array.
     **/
    public void Var072()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            u.setAttributeValue(RUser.SUPPLEMENTAL_GROUPS, new String[] { group2_ });
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userInGroup_);
            Object value = u2.getAttributeValue(RUser.SUPPLEMENTAL_GROUPS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 1 && asArray[0].equals(group2_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Set and get the attribute value to a 1 element array on a user that does not have a group profile.
     **/
    public void Var073()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.SUPPLEMENTAL_GROUPS, new String[] { group2_ });
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Set and get the attribute value to a 2 element array.
     **/
    public void Var074()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            u.setAttributeValue(RUser.SUPPLEMENTAL_GROUPS, new String[] { group2_, group1_ } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userInGroup_);
            Object value = u2.getAttributeValue(RUser.SUPPLEMENTAL_GROUPS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 2 && asArray[0].equals(group2_) && asArray[1].equals(group1_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Set and get the attribute value to a 6 element array.
     **/
    public void Var075()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            u.setAttributeValue(RUser.SUPPLEMENTAL_GROUPS, new String[] { group1_, group3_, group6_, group2_, group5_, group4_ } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userInGroup_);
            Object value = u2.getAttributeValue(RUser.SUPPLEMENTAL_GROUPS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 6 && asArray[0].equals(group1_) && asArray[1].equals(group3_) && asArray[2].equals(group6_) && asArray[3].equals(group2_) && asArray[4].equals(group5_) && asArray[5].equals(group4_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Set and get the attribute value to several element array with one of the elements null.
     **/
    public void Var076()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            u.setAttributeValue(RUser.SUPPLEMENTAL_GROUPS, new String[] { group1_, null, group3_, group5_ } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Set and get the attribute value to several element array with one of the elements not valid.
     **/
    public void Var077()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            u.setAttributeValue(RUser.SUPPLEMENTAL_GROUPS, new String[] { group1_, "bogus", group2_ } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Set and get the attribute value to several element array with one of the elements being the user's group profile.
     **/
    public void Var078()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            // This is the user's group profile.
            u.setAttributeValue(RUser.SUPPLEMENTAL_GROUPS, new String[] { group1_, group_, group2_ } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userInGroup_);
            Object value = u2.getAttributeValue(RUser.SUPPLEMENTAL_GROUPS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 3 && asArray[0].equals(group1_) && asArray[1].equals(group_) && asArray[2].equals(group2_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Set and get the attribute value to several element array with one of the elements specified twice.
     **/
    public void Var079()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            u.setAttributeValue(RUser.SUPPLEMENTAL_GROUPS, new String[] { group6_, group5_, group2_, group6_ } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userInGroup_);
            Object value = u2.getAttributeValue(RUser.SUPPLEMENTAL_GROUPS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 3 && asArray[0].equals(group6_) && asArray[1].equals(group5_) && asArray[2].equals(group2_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     SUPPLEMENTAL_GROUPS - Get the attribute value with the backwards compatibility method.
     **/
    public void Var080()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userInGroup_);
            u.setAttributeValue(RUser.SUPPLEMENTAL_GROUPS, new String[] { group4_, group6_, group1_ } );
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, userInGroup_);
            String[] value = u2.getSupplementalGroups();
            assertCondition(value.length == 3 && value[0].equals(group4_) && value[1].equals(group6_) && value[2].equals(group1_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
