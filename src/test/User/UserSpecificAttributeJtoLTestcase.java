///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpecificAttributeJtoLTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import com.ibm.as400.access.User;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;
import test.UserSandbox;

import com.ibm.as400.resource.RUser;

/**
 Testcase UserSpecificAttributeJtoLTestcase.  This tests the following attributes of the User class:
 <ul>
 <li>JOB_DESCRIPTION
 <li>JOB_TITLE
 <li>KEYBOARD_BUFFERING
 <li>LANGUAGE_ID
 <li>LAST_NAME
 <li>LIMIT_CAPABILITIES
 <li>LIMIT_DEVICE_SESSIONS
 <li>LOCALE_JOB_ATTRIBUTES
 <li>LOCALE_PATH_NAME
 <li>LOCAL_DATA_INDICATOR
 <li>LOCATION
 </ul>
 **/
public class UserSpecificAttributeJtoLTestcase extends Testcase
{
    private UserSandbox sandbox_;
    private String user_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "USAJL");
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

    private static void dump(String[] asArray)
    {
        for (int i = 0; i < asArray.length; ++i)
        {
            System.out.println("Array[" + i + "]=" + asArray[i] + ".");
        }
    }

    /**
     JOB_DESCRIPTION - Check the attribute meta data in the entire list.
     **/
    public void Var001()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.JOB_DESCRIPTION, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_DESCRIPTION - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var002()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.JOB_DESCRIPTION);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.JOB_DESCRIPTION, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_DESCRIPTION - Get the attribute value without setting it first.
     **/
    public void Var003()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.JOB_DESCRIPTION);
            assertCondition(((String)value).equals("/QSYS.LIB/QGPL.LIB/QDFTJOBD.JOBD"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_DESCRIPTION - Set and get the attribute value to a valid job description.
     **/
    public void Var004()
    {
        try
        {
            String jobdName = "/QSYS.LIB/QCTL.JOBD";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.JOB_DESCRIPTION, jobdName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.JOB_DESCRIPTION);
            assertCondition(((String)value).equals(jobdName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_DESCRIPTION - Set and get the attribute value to a valid job description, but with mixed case.
     **/
    public void Var005()
    {
        try
        {
            String jobdName = "/QsYs.LiB/jAVacTl.joBD";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.JOB_DESCRIPTION, jobdName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.JOB_DESCRIPTION);
            assertCondition(((String)value).equalsIgnoreCase(jobdName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_DESCRIPTION - Set the attribute value to be a valid job description name that does not exist.
     **/
    public void Var006()
    {
        try
        {
            String jobdName = "/QSYS.LIB/NOTEXIST.JOBD";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.JOB_DESCRIPTION, jobdName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.JOB_DESCRIPTION);
            assertCondition(((String)value).equals(jobdName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_DESCRIPTION - Set the attribute value to be a invalid job description name.
     **/
    public void Var007()
    {
        try
        {
            String jobdName = "//////";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.JOB_DESCRIPTION, jobdName);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     JOB_DESCRIPTION - Set the attribute value to be the empty string.
     **/
    public void Var008()
    {
        try
        {
            String jobdName = "";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.JOB_DESCRIPTION, jobdName);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     JOB_DESCRIPTION - Get the attribute value with the backward compatibility method.
     **/
    public void Var009()
    {
        try
        {
            String jobdName = "/QSYS.LIB/QBATCH.JOBD";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.JOB_DESCRIPTION, jobdName);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getJobDescription();
            assertCondition(value.equals(jobdName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_TITLE - Check the attribute meta data in the entire list.
     **/
    public void Var010()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.JOB_TITLE, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_TITLE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var011()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.JOB_TITLE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.JOB_TITLE, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_TITLE - Get the attribute value without setting it first.
     **/
    public void Var012()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.JOB_TITLE);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_TITLE - Set and get the attribute value.
     **/
    public void Var013()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.JOB_TITLE, "Inver Grove Heights");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.JOB_TITLE);
            assertCondition(((String)value).equals("Inver Grove Heights"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_TITLE - Set and get the attribute value to an empty string.
     **/
    public void Var014()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.JOB_TITLE, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.JOB_TITLE);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_TITLE - Set and get the attribute value to *NONE.
     **/
    public void Var015()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.JOB_TITLE, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.JOB_TITLE);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     JOB_TITLE - Set the attribute value to be too long.
     **/
    public void Var016()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.JOB_TITLE, "This entry has more than forty characters.");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     KEYBOARD_BUFFERING - Check the attribute meta data in the entire list.
     **/
    public void Var017()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.KEYBOARD_BUFFERING, String.class, false, 4, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     KEYBOARD_BUFFERING - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var018()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.KEYBOARD_BUFFERING);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.KEYBOARD_BUFFERING, String.class, false, 4, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     KEYBOARD_BUFFERING - Get the attribute value without setting it first.
     **/
    public void Var019()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.KEYBOARD_BUFFERING);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     KEYBOARD_BUFFERING - Set and get the attribute value to yes.
     **/
    public void Var020()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.KEYBOARD_BUFFERING, RUser.YES);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.KEYBOARD_BUFFERING);
            assertCondition(((String)value).equals(RUser.YES));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     KEYBOARD_BUFFERING - Set and get the attribute value to no.
     **/
    public void Var021()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.KEYBOARD_BUFFERING, RUser.NO);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.KEYBOARD_BUFFERING);
            assertCondition(((String)value).equals(RUser.NO));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     KEYBOARD_BUFFERING - Set and get the attribute value to system value.
     **/
    public void Var022()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.KEYBOARD_BUFFERING, RUser.SYSTEM_VALUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.KEYBOARD_BUFFERING);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     KEYBOARD_BUFFERING - Set and get the attribute value to type ahead.
     **/
    public void Var023()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.KEYBOARD_BUFFERING, RUser.KEYBOARD_BUFFERING_TYPE_AHEAD);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.KEYBOARD_BUFFERING);
            assertCondition(((String)value).equals(RUser.KEYBOARD_BUFFERING_TYPE_AHEAD));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     KEYBOARD_BUFFERING - Set the attribute value to be a bogus string.
     **/
    public void Var024()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.KEYBOARD_BUFFERING, "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     LANGUAGE_ID - Check the attribute meta data in the entire list.
     **/
    public void Var025()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LANGUAGE_ID, String.class, false, 1, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LANGUAGE_ID - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var026()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.LANGUAGE_ID);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LANGUAGE_ID, String.class, false, 1, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LANGUAGE_ID - Get the attribute value without setting it first.
     **/
    public void Var027()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.LANGUAGE_ID);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LANGUAGE_ID - Set and get the attribute value to system value.
     **/
    public void Var028()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LANGUAGE_ID, RUser.SYSTEM_VALUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LANGUAGE_ID);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LANGUAGE_ID - Set and get the attribute value to a valid language.
     **/
    public void Var029()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LANGUAGE_ID, "ESP");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LANGUAGE_ID);
            assertCondition(((String)value).equals("ESP"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LANGUAGE_ID - Set the attribute value to be a language that does not exist.
     **/
    public void Var030()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LANGUAGE_ID, "XYZ");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     LANGUAGE_ID - Set the attribute value to be a invalid language name with no spaces.
     **/
    public void Var031()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LANGUAGE_ID, "ThisLanguageNameIsWayTooLong");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     LANGUAGE_ID - Set the attribute value to be a invalid language name with spaces.
     **/
    public void Var032()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LANGUAGE_ID, "This Language Name Is Way Too Long");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     LANGUAGE_ID - Get the attribute value with the backward compatibility method.
     **/
    public void Var033()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LANGUAGE_ID, "DEU");
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getLanguageID();
            assertCondition(value.equals("DEU"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LAST_NAME - Check the attribute meta data in the entire list.
     **/
    public void Var034()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LAST_NAME, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LAST_NAME - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var035()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.LAST_NAME);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LAST_NAME, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LAST_NAME - Get the attribute value without setting it first.
     **/
    public void Var036()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.LAST_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LAST_NAME - Set and get the attribute value.
     **/
    public void Var037()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LAST_NAME, "White Bear Lake");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LAST_NAME);
            assertCondition(((String)value).equals("White Bear Lake"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LAST_NAME - Set and get the attribute value to an empty string.
     **/
    public void Var038()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LAST_NAME, "");
            u.setAttributeValue(RUser.FULL_NAME, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LAST_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LAST_NAME - Set and get the attribute value to *NONE.
     **/
    public void Var039()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LAST_NAME, "*NONE");
            u.setAttributeValue(RUser.FULL_NAME, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LAST_NAME);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LAST_NAME - Set the attribute value to be too long.
     **/
    public void Var040()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LAST_NAME, "This is more than forty characters for a last name.");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     LIMIT_CAPABILITIES - Check the attribute meta data in the entire list.
     **/
    public void Var041()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LIMIT_CAPABILITIES, String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LIMIT_CAPABILITIES - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var042()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.LIMIT_CAPABILITIES);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LIMIT_CAPABILITIES, String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LIMIT_CAPABILITIES - Get the attribute value without setting it first.
     **/
    public void Var043()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.LIMIT_CAPABILITIES);
            assertCondition(((String)value).equals(RUser.NO));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LIMIT_CAPABILITIES - Set and get the attribute value to yes.
     **/
    public void Var044()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LIMIT_CAPABILITIES, RUser.YES);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LIMIT_CAPABILITIES);
            assertCondition(((String)value).equals(RUser.YES));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LIMIT_CAPABILITIES - Set and get the attribute value to no.
     **/
    public void Var045()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LIMIT_CAPABILITIES, RUser.NO);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LIMIT_CAPABILITIES);
            assertCondition(((String)value).equals(RUser.NO));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LIMIT_CAPABILITIES - Set and get the attribute value to partial.
     **/
    public void Var046()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LIMIT_CAPABILITIES, RUser.LIMIT_CAPABILITIES_PARTIAL);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LIMIT_CAPABILITIES);
            assertCondition(((String)value).equals(RUser.LIMIT_CAPABILITIES_PARTIAL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LIMIT_CAPABILITIES - Set the attribute value to be a bogus string.
     **/
    public void Var047()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LIMIT_CAPABILITIES, "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     LIMIT_DEVICE_SESSIONS - Check the attribute meta data in the entire list.
     **/
    public void Var048()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LIMIT_DEVICE_SESSIONS, String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LIMIT_DEVICE_SESSIONS - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var049()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.LIMIT_DEVICE_SESSIONS);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LIMIT_DEVICE_SESSIONS, String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LIMIT_DEVICE_SESSIONS - Get the attribute value without setting it first.
     **/
    public void Var050()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.LIMIT_DEVICE_SESSIONS);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LIMIT_DEVICE_SESSIONS - Set and get the attribute value to yes.
     **/
    public void Var051()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LIMIT_DEVICE_SESSIONS, RUser.YES);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LIMIT_DEVICE_SESSIONS);
            assertCondition(((String)value).equals(RUser.YES));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LIMIT_DEVICE_SESSIONS - Set and get the attribute value to no.
     **/
    public void Var052()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LIMIT_DEVICE_SESSIONS, RUser.NO);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LIMIT_DEVICE_SESSIONS);
            assertCondition(((String)value).equals(RUser.NO));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LIMIT_DEVICE_SESSIONS - Set and get the attribute value to system value.
     **/
    public void Var053()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LIMIT_DEVICE_SESSIONS, RUser.SYSTEM_VALUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LIMIT_DEVICE_SESSIONS);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LIMIT_DEVICE_SESSIONS - Set the attribute value to be a bogus string.
     **/
    public void Var054()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LIMIT_DEVICE_SESSIONS, "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Check the attribute meta data in the entire list.
     **/
    public void Var055()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LOCALE_JOB_ATTRIBUTES, String.class, false, 8, null, true, true));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var056()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.LOCALE_JOB_ATTRIBUTES);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LOCALE_JOB_ATTRIBUTES, String.class, false, 8, null, true, true));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Get the attribute value without setting it first.
     **/
    public void Var057()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 1 && asArray[0].equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Set and get the attribute value to an empty array.
     **/
    public void Var058()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES, new String[0]);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES);
            String[] asArray = (String[])value;
            // An empty array means "none".
            assertCondition(asArray.length == 1 && asArray[0].equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Set and get the attribute value to 1 element array with just NONE.
     **/
    public void Var059()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES, new String[] { RUser.NONE });
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 1 && asArray[0].equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Set and get the attribute value to 1 element array with just system value.
     **/
    public void Var060()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES, new String[] { RUser.SYSTEM_VALUE } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 1 && asArray[0].equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Set and get the attribute value to 1 element array with just CCSID.
     **/
    public void Var061()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES, new String[] { RUser.LOCALE_JOB_ATTRIBUTES_CCSID } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 1 && asArray[0].equals(RUser.LOCALE_JOB_ATTRIBUTES_CCSID));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Set and get the attribute value to include NONE and another value.
     **/
    public void Var062()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES, new String[] { RUser.NONE, RUser.LOCALE_JOB_ATTRIBUTES_CCSID } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Set and get the attribute value to include SYSTEM_VALUE and another value.
     **/
    public void Var063()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES, new String[] { RUser.LOCALE_JOB_ATTRIBUTES_CCSID, RUser.SYSTEM_VALUE } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Set and get the attribute value to several element array with sort sequence (specified twice), time separator and decimal format specified.
     **/
    public void Var064()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES, new String[] { RUser.LOCALE_JOB_ATTRIBUTES_SORT_SEQUENCE, RUser.LOCALE_JOB_ATTRIBUTES_DECIMAL_FORMAT, RUser.LOCALE_JOB_ATTRIBUTES_SORT_SEQUENCE, RUser.LOCALE_JOB_ATTRIBUTES_TIME_SEPARATOR } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 3 && asArray[0].equals(RUser.LOCALE_JOB_ATTRIBUTES_SORT_SEQUENCE) && asArray[1].equals(RUser.LOCALE_JOB_ATTRIBUTES_TIME_SEPARATOR) && asArray[2].equals(RUser.LOCALE_JOB_ATTRIBUTES_DECIMAL_FORMAT));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Set and get the attribute value to several element array with one of the elements null.
     **/
    public void Var065()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES, new String[] { RUser.LOCALE_JOB_ATTRIBUTES_TIME_SEPARATOR, RUser.LOCALE_JOB_ATTRIBUTES_DATE_FORMAT, null, RUser.LOCALE_JOB_ATTRIBUTES_DECIMAL_FORMAT } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Set and get the attribute value to several element array with one of the elements not valid.
     **/
    public void Var066()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES, new String[] { RUser.LOCALE_JOB_ATTRIBUTES_TIME_SEPARATOR, RUser.LOCALE_JOB_ATTRIBUTES_DATE_FORMAT, "bogus", RUser.LOCALE_JOB_ATTRIBUTES_DATE_SEPARATOR } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Set and get the attribute value to several element array with all possible values specified, other than none and system value.
     **/
    public void Var067()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES, new String[] { RUser.LOCALE_JOB_ATTRIBUTES_CCSID, RUser.LOCALE_JOB_ATTRIBUTES_DATE_FORMAT, RUser.LOCALE_JOB_ATTRIBUTES_DATE_SEPARATOR, RUser.LOCALE_JOB_ATTRIBUTES_SORT_SEQUENCE, RUser.LOCALE_JOB_ATTRIBUTES_TIME_SEPARATOR, RUser.LOCALE_JOB_ATTRIBUTES_DECIMAL_FORMAT } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 6 && asArray[0].equals(RUser.LOCALE_JOB_ATTRIBUTES_CCSID) && asArray[1].equals(RUser.LOCALE_JOB_ATTRIBUTES_DATE_FORMAT) && asArray[2].equals(RUser.LOCALE_JOB_ATTRIBUTES_DATE_SEPARATOR) && asArray[3].equals(RUser.LOCALE_JOB_ATTRIBUTES_SORT_SEQUENCE) && asArray[4].equals(RUser.LOCALE_JOB_ATTRIBUTES_TIME_SEPARATOR) && asArray[5].equals(RUser.LOCALE_JOB_ATTRIBUTES_DECIMAL_FORMAT));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_JOB_ATTRIBUTES - Get the attribute value with the backwards compatibility method.
     **/
    public void Var068()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_JOB_ATTRIBUTES, new String[] { RUser.LOCALE_JOB_ATTRIBUTES_SORT_SEQUENCE, RUser.LOCALE_JOB_ATTRIBUTES_DATE_FORMAT, RUser.LOCALE_JOB_ATTRIBUTES_CCSID, RUser.LOCALE_JOB_ATTRIBUTES_DATE_SEPARATOR } );
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String[] value = u2.getLocaleJobAttributes();
            assertCondition(value.length == 4 && value[0].equals(RUser.LOCALE_JOB_ATTRIBUTES_CCSID) && value[1].equals(RUser.LOCALE_JOB_ATTRIBUTES_DATE_FORMAT) && value[2].equals(RUser.LOCALE_JOB_ATTRIBUTES_DATE_SEPARATOR) && value[3].equals(RUser.LOCALE_JOB_ATTRIBUTES_SORT_SEQUENCE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_PATH_NAME - Check the attribute meta data in the entire list.
     **/
    public void Var069()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LOCALE_PATH_NAME, String.class, false, 4, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_PATH_NAME - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var070()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.LOCALE_PATH_NAME);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LOCALE_PATH_NAME, String.class, false, 4, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_PATH_NAME - Get the attribute value without setting it first.
     **/
    public void Var071()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.LOCALE_PATH_NAME);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_PATH_NAME - Set and get the attribute value to C.
     **/
    public void Var072()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_PATH_NAME, RUser.LOCALE_PATH_NAME_C);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCALE_PATH_NAME);
            assertCondition(((String)value).equals(RUser.LOCALE_PATH_NAME_C));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_PATH_NAME - Set and get the attribute value to Posix.
     **/
    public void Var073()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_PATH_NAME, RUser.LOCALE_PATH_NAME_POSIX);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCALE_PATH_NAME);
            assertCondition(((String)value).equals(RUser.LOCALE_PATH_NAME_POSIX));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_PATH_NAME - Set and get the attribute value to none.
     **/
    public void Var074()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_PATH_NAME, RUser.NONE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCALE_PATH_NAME);
            assertCondition(((String)value).equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_PATH_NAME - Set and get the attribute value to system value.
     **/
    public void Var075()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_PATH_NAME, RUser.SYSTEM_VALUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCALE_PATH_NAME);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCALE_PATH_NAME - Set the attribute value to be a bogus string.
     **/
    public void Var076()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_PATH_NAME, "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     LOCALE_PATH_NAME - Get the attribute value with the backwards compatibilty method.
     **/
    public void Var077()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCALE_PATH_NAME, RUser.LOCALE_PATH_NAME_POSIX);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getLocalePathName();
            assertCondition(value.equals(RUser.LOCALE_PATH_NAME_POSIX));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCAL_DATA_INDICATOR - Check the attribute meta data in the entire list.
     **/
    public void Var078()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LOCAL_DATA_INDICATOR, String.class, true, 2, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCAL_DATA_INDICATOR - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var079()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.LOCAL_DATA_INDICATOR);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LOCAL_DATA_INDICATOR, String.class, true, 2, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCAL_DATA_INDICATOR - Get the attribute value when the data is local.
     **/
    public void Var080()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.LOCAL_DATA_INDICATOR);
            assertCondition(((String)value).equals(RUser.LOCAL_DATA_INDICATOR_LOCAL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCAL_DATA_INDICATOR - Set and get the attribute value when the data is shadowed.
     **/
    public void Var081()
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
     LOCATION - Check the attribute meta data in the entire list.
     **/
    public void Var082()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LOCATION, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCATION - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var083()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.LOCATION);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.LOCATION, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCATION - Get the attribute value without setting it first.
     **/
    public void Var084()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.LOCATION);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCATION - Set and get the attribute value.
     **/
    public void Var085()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCATION, "Washington, D.C.");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCATION);
            assertCondition(((String)value).equals("Washington, D.C."));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCATION - Set and get the attribute value to an empty string.
     **/
    public void Var086()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCATION, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCATION);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCATION - Set and get the attribute value to *NONE.
     **/
    public void Var087()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCATION, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.LOCATION);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     LOCATION - Set the attribute value to be too long.
     **/
    public void Var088()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.LOCATION, "This location has more than forty characters.");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }
}
