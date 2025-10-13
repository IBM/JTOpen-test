///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpecificAttributeTtoZTestcase.java
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
import test.UserTest;

import com.ibm.as400.resource.RUser;

/**
 Testcase UserSpecificAttributeTtoZTestcase.  This tests the following attributes of the User class:
 <ul>
 <li>TELEPHONE_NUMBER_1
 <li>TELEPHONE_NUMBER_2
 <li>TEXT
 <li>TEXT_DESCRIPTION
 <li>USER_ACTION_AUDIT_LEVEL
 <li>USER_ADDRESS
 <li>USER_CLASS
 <li>USER_ID
 <li>USER_ID_NUMBER
 <li>USER_OPTIONS
 <li>USER_PROFILE_NAME
 </ul>
 **/
@SuppressWarnings("deprecation")
public class UserSpecificAttributeTtoZTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserSpecificAttributeTtoZTestcase";
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
        sandbox_ = new UserSandbox(pwrSys_, "USAT", UserTest.COLLECTION.substring(UserTest.COLLECTION.length() - 1));
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
     TELEPHONE_NUMBER_1 - Check the attribute meta data in the entire list.
     **/
    public void Var001()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.TELEPHONE_NUMBER_1, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TELEPHONE_NUMBER_1 - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var002()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.TELEPHONE_NUMBER_1);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.TELEPHONE_NUMBER_1, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TELEPHONE_NUMBER_1 - Get the attribute value without setting it first.
     **/
    public void Var003()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.TELEPHONE_NUMBER_1);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TELEPHONE_NUMBER_1 - Set and get the attribute value.
     **/
    public void Var004()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TELEPHONE_NUMBER_1, "507 253 4127");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.TELEPHONE_NUMBER_1);
            assertCondition(((String)value).equals("507 253 4127"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TELEPHONE_NUMBER_1 - Set and get the attribute value to an empty string.
     **/
    public void Var005()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TELEPHONE_NUMBER_1, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.TELEPHONE_NUMBER_1);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TELEPHONE_NUMBER_1 - Set and get the attribute value to *NONE.
     **/
    public void Var006()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TELEPHONE_NUMBER_1, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.TELEPHONE_NUMBER_1);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TELEPHONE_NUMBER_1 - Set the attribute value to be too long.
     **/
    public void Var007()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TELEPHONE_NUMBER_1, "Has more than 26 characters.");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     TELEPHONE_NUMBER_2 - Check the attribute meta data in the entire list.
     **/
    public void Var008()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.TELEPHONE_NUMBER_2, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TELEPHONE_NUMBER_2 - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var009()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.TELEPHONE_NUMBER_2);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.TELEPHONE_NUMBER_2, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TELEPHONE_NUMBER_2 - Get the attribute value without setting it first.
     **/
    public void Var010()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.TELEPHONE_NUMBER_2);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TELEPHONE_NUMBER_2 - Set and get the attribute value.
     **/
    public void Var011()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TELEPHONE_NUMBER_2, "T/L 553 5329");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.TELEPHONE_NUMBER_2);
            assertCondition(((String)value).equals("T/L 553 5329"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TELEPHONE_NUMBER_2 - Set and get the attribute value to an empty string.
     **/
    public void Var012()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TELEPHONE_NUMBER_2, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.TELEPHONE_NUMBER_2);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TELEPHONE_NUMBER_2 - Set and get the attribute value to *NONE.
     **/
    public void Var013()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TELEPHONE_NUMBER_2, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.TELEPHONE_NUMBER_2);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TELEPHONE_NUMBER_2 - Set the attribute value to be too long.
     **/
    public void Var014()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TELEPHONE_NUMBER_2, "Has more than 26 characters.");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     TEXT - Check the attribute meta data in the entire list.
     **/
    public void Var015()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.TEXT, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TEXT - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var016()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.TEXT);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.TEXT, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TEXT - Get the attribute value without setting it first.
     **/
    public void Var017()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.TEXT);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TEXT - Set and get the attribute value.
     **/
    public void Var018()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TEXT, "Some text for testing");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.TEXT);
            assertCondition(((String)value).equals("Some text for testing"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TEXT - Set and get the attribute value to an empty string.
     **/
    public void Var019()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TEXT, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.TEXT);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TEXT - Set and get the attribute value to *NONE.
     **/
    public void Var020()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TEXT, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.TEXT);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TEXT - Set the attribute value to be too long.
     **/
    public void Var021()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TEXT, "This sentence needs to have more than 50 characters.");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     TEXT_DESCRIPTION - Check the attribute meta data in the entire list.
     **/
    public void Var022()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.TEXT_DESCRIPTION, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TEXT_DESCRIPTION - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var023()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.TEXT_DESCRIPTION);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.TEXT_DESCRIPTION, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TEXT_DESCRIPTION - Get the attribute value without setting it first.
     **/
    public void Var024()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(((String)value).startsWith(user_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TEXT_DESCRIPTION - Set and get the attribute value.
     **/
    public void Var025()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TEXT_DESCRIPTION, "Some more text for testing");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(((String)value).equals("Some more text for testing"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TEXT_DESCRIPTION - Set and get the attribute value to an empty string.
     **/
    public void Var026()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TEXT_DESCRIPTION, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     TEXT_DESCRIPTION - Set the attribute value to be too long.
     **/
    public void Var027()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TEXT_DESCRIPTION, "This sentence needs to have more than 50 characters.");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     TEXT_DESCRIPTION - Get the attribute value using the backward compatibility method.
     **/
    public void Var028()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.TEXT_DESCRIPTION, "Still some more text for testing");
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getDescription();
            assertCondition(value.equals("Still some more text for testing"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Check the attribute meta data in the entire list.
     **/
    public void Var029()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_ACTION_AUDIT_LEVEL, String.class, false, 13, null, true, true));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var030()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.USER_ACTION_AUDIT_LEVEL);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_ACTION_AUDIT_LEVEL, String.class, false, 13, null, true, true));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Get the attribute value without setting it first.
     **/
    public void Var031()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Set and get the attribute value to an empty array.
     **/
    public void Var032()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL, new String[0]);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Set and get the attribute value to a 1 element array.
     **/
    public void Var033()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL, new String[] { RUser.USER_ACTION_AUDIT_LEVEL_COMMAND } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 1 && asArray[0].equals(RUser.USER_ACTION_AUDIT_LEVEL_COMMAND));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Set and get the attribute value to a 2 element array.
     **/
    public void Var034()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL, new String[] { RUser.USER_ACTION_AUDIT_LEVEL_DELETE, RUser.USER_ACTION_AUDIT_LEVEL_CREATE } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 2 && asArray[0].equals(RUser.USER_ACTION_AUDIT_LEVEL_CREATE) && asArray[1].equals(RUser.USER_ACTION_AUDIT_LEVEL_DELETE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Set and get the attribute value to a 5 element array.
     **/
    public void Var035()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL, new String[] { RUser.USER_ACTION_AUDIT_LEVEL_JOB_DATA, RUser.USER_ACTION_AUDIT_LEVEL_SYSTEM_MANAGEMENT, RUser.USER_ACTION_AUDIT_LEVEL_OBJECT_MANAGEMENT, RUser.USER_ACTION_AUDIT_LEVEL_SPOOLED_FILE_DATA, RUser.USER_ACTION_AUDIT_LEVEL_OFFICE_SERVICES } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 5 && asArray[0].equals(RUser.USER_ACTION_AUDIT_LEVEL_JOB_DATA) && asArray[1].equals(RUser.USER_ACTION_AUDIT_LEVEL_OBJECT_MANAGEMENT) && asArray[2].equals(RUser.USER_ACTION_AUDIT_LEVEL_OFFICE_SERVICES) && asArray[3].equals(RUser.USER_ACTION_AUDIT_LEVEL_SPOOLED_FILE_DATA) && asArray[4].equals(RUser.USER_ACTION_AUDIT_LEVEL_SYSTEM_MANAGEMENT));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Set and get the attribute value to another 5 element array.
     **/
    public void Var036()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL, new String[] { RUser.USER_ACTION_AUDIT_LEVEL_OPTICAL, RUser.USER_ACTION_AUDIT_LEVEL_PROGRAM_ADOPTION, RUser.USER_ACTION_AUDIT_LEVEL_SAVE_RESTORE, RUser.USER_ACTION_AUDIT_LEVEL_SECURITY, RUser.USER_ACTION_AUDIT_LEVEL_SERVICE } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 5 && asArray[0].equals(RUser.USER_ACTION_AUDIT_LEVEL_PROGRAM_ADOPTION) && asArray[1].equals(RUser.USER_ACTION_AUDIT_LEVEL_SAVE_RESTORE) && asArray[2].equals(RUser.USER_ACTION_AUDIT_LEVEL_SECURITY) && asArray[3].equals(RUser.USER_ACTION_AUDIT_LEVEL_SERVICE) && asArray[4].equals(RUser.USER_ACTION_AUDIT_LEVEL_OPTICAL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Set and get the attribute value to several element array with one of the elements null.
     **/
    public void Var037()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL, new String[] { RUser.USER_ACTION_AUDIT_LEVEL_PROGRAM_ADOPTION, RUser.USER_ACTION_AUDIT_LEVEL_OFFICE_SERVICES, null, RUser.USER_ACTION_AUDIT_LEVEL_OPTICAL } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Set and get the attribute value to several element array with one of the elements not valid.
     **/
    public void Var038()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL, new String[] { RUser.USER_ACTION_AUDIT_LEVEL_SERVICE, "bogus", RUser.USER_ACTION_AUDIT_LEVEL_SPOOLED_FILE_DATA, RUser.USER_ACTION_AUDIT_LEVEL_SYSTEM_MANAGEMENT } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Set and get the attribute value to several element array with one of the elements specified twice.
     **/
    public void Var039()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL, new String[] { RUser.USER_ACTION_AUDIT_LEVEL_JOB_DATA, RUser.USER_ACTION_AUDIT_LEVEL_SERVICE, RUser.USER_ACTION_AUDIT_LEVEL_JOB_DATA, RUser.USER_ACTION_AUDIT_LEVEL_OPTICAL } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 3 && asArray[0].equals(RUser.USER_ACTION_AUDIT_LEVEL_JOB_DATA) && asArray[1].equals(RUser.USER_ACTION_AUDIT_LEVEL_SERVICE) && asArray[2].equals(RUser.USER_ACTION_AUDIT_LEVEL_OPTICAL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Set and get the attribute value to several element array with all possible values specified.
     **/
    public void Var040()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL, new String[] { RUser.USER_ACTION_AUDIT_LEVEL_COMMAND, RUser.USER_ACTION_AUDIT_LEVEL_CREATE, RUser.USER_ACTION_AUDIT_LEVEL_DELETE, RUser.USER_ACTION_AUDIT_LEVEL_JOB_DATA, RUser.USER_ACTION_AUDIT_LEVEL_OBJECT_MANAGEMENT, RUser.USER_ACTION_AUDIT_LEVEL_OFFICE_SERVICES, RUser.USER_ACTION_AUDIT_LEVEL_OPTICAL, RUser.USER_ACTION_AUDIT_LEVEL_PROGRAM_ADOPTION, RUser.USER_ACTION_AUDIT_LEVEL_SAVE_RESTORE, RUser.USER_ACTION_AUDIT_LEVEL_SECURITY, RUser.USER_ACTION_AUDIT_LEVEL_SERVICE, RUser.USER_ACTION_AUDIT_LEVEL_SPOOLED_FILE_DATA, RUser.USER_ACTION_AUDIT_LEVEL_SYSTEM_MANAGEMENT } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 13 && asArray[0].equals(RUser.USER_ACTION_AUDIT_LEVEL_COMMAND) && asArray[1].equals(RUser.USER_ACTION_AUDIT_LEVEL_CREATE) && asArray[2].equals(RUser.USER_ACTION_AUDIT_LEVEL_DELETE) && asArray[3].equals(RUser.USER_ACTION_AUDIT_LEVEL_JOB_DATA) && asArray[4].equals(RUser.USER_ACTION_AUDIT_LEVEL_OBJECT_MANAGEMENT) && asArray[5].equals(RUser.USER_ACTION_AUDIT_LEVEL_OFFICE_SERVICES) && asArray[6].equals(RUser.USER_ACTION_AUDIT_LEVEL_PROGRAM_ADOPTION) && asArray[7].equals(RUser.USER_ACTION_AUDIT_LEVEL_SAVE_RESTORE) && asArray[8].equals(RUser.USER_ACTION_AUDIT_LEVEL_SECURITY) && asArray[9].equals(RUser.USER_ACTION_AUDIT_LEVEL_SERVICE) && asArray[10].equals(RUser.USER_ACTION_AUDIT_LEVEL_SPOOLED_FILE_DATA) && asArray[11].equals(RUser.USER_ACTION_AUDIT_LEVEL_SYSTEM_MANAGEMENT) && asArray[12].equals(RUser.USER_ACTION_AUDIT_LEVEL_OPTICAL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ACTION_AUDIT_LEVEL - Get the attribute value with the backwards compatibility method.
     **/
    public void Var041()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ACTION_AUDIT_LEVEL, new String[] { RUser.USER_ACTION_AUDIT_LEVEL_CREATE, RUser.USER_ACTION_AUDIT_LEVEL_COMMAND, RUser.USER_ACTION_AUDIT_LEVEL_OPTICAL, RUser.USER_ACTION_AUDIT_LEVEL_PROGRAM_ADOPTION } );
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String[] value = u2.getUserActionAuditLevel();
            assertCondition(value.length == 4 && value[0].equals(RUser.USER_ACTION_AUDIT_LEVEL_COMMAND) && value[1].equals(RUser.USER_ACTION_AUDIT_LEVEL_CREATE) && value[2].equals(RUser.USER_ACTION_AUDIT_LEVEL_PROGRAM_ADOPTION) && value[3].equals(RUser.USER_ACTION_AUDIT_LEVEL_OPTICAL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ADDRESS - Check the attribute meta data in the entire list.
     **/
    public void Var042()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_ADDRESS, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ADDRESS - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var043()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.USER_ADDRESS);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_ADDRESS, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ADDRESS - Get the attribute value.
     **/
    public void Var044()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.USER_ADDRESS);
            if (pwrSys_.isLocal())
            {
                String hostName = java.net.InetAddress.getLocalHost().getHostName();
                int dot = hostName.indexOf('.');
                if (dot >= 0) hostName = hostName.substring(0, dot);
                assertCondition(((String)value).equalsIgnoreCase(hostName));
            }
            else
            {
                assertCondition(((String)value).equalsIgnoreCase(pwrSys_.getSystemName()));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_CLASS - Check the attribute meta data in the entire list.
     **/
    public void Var045()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_CLASS, String.class, false, 5, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_CLASS - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var046()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.USER_CLASS);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_CLASS, String.class, false, 5, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_CLASS - Get the attribute value without setting it first.
     **/
    public void Var047()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.USER_CLASS);
            assertCondition(((String)value).equals(RUser.USER_CLASS_USER));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_CLASS - Set the attribute value to a bogus value.
     **/
    public void Var048()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_CLASS, "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     USER_CLASS - Set and get the attribute value when it is set to security officer.
     **/
    public void Var049()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_CLASS, RUser.USER_CLASS_SECURITY_OFFICER);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_CLASS);
            assertCondition(((String)value).equals(RUser.USER_CLASS_SECURITY_OFFICER));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_CLASS - Set and get the attribute value when it is set to security administrator.
     **/
    public void Var050()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_CLASS, RUser.USER_CLASS_SECURITY_ADMINISTRATOR);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_CLASS);
            assertCondition(((String)value).equals(RUser.USER_CLASS_SECURITY_ADMINISTRATOR));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_CLASS - Set and get the attribute value when it is set to programmer.
     **/
    public void Var051()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_CLASS, RUser.USER_CLASS_PROGRAMMER);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_CLASS);
            assertCondition(((String)value).equals(RUser.USER_CLASS_PROGRAMMER));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_CLASS - Set and get the attribute value when it is set to system operator.
     **/
    public void Var052()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_CLASS, RUser.USER_CLASS_SYSTEM_OPERATOR);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_CLASS);
            assertCondition(((String)value).equals(RUser.USER_CLASS_SYSTEM_OPERATOR));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_CLASS - Set and get the attribute value when it is set to user.
     **/
    public void Var053()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_CLASS, RUser.USER_CLASS_USER);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_CLASS);
            assertCondition(((String)value).equals(RUser.USER_CLASS_USER));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_CLASS - Get the attribute value with the backwards compatibilty method.
     **/
    public void Var054()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_CLASS, RUser.USER_CLASS_PROGRAMMER);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getUserClassName();
            assertCondition(value.equals(RUser.USER_CLASS_PROGRAMMER));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ID - Check the attribute meta data in the entire list.
     **/
    public void Var055()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_ID, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ID - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var056()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.USER_ID);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_ID, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ID - Get the attribute value.
     **/
    public void Var057()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.USER_ID);
            assertCondition(((String)value).equalsIgnoreCase(user_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ID_NUMBER - Check the attribute meta data in the entire list.
     **/
    public void Var058()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_ID_NUMBER , Long.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ID_NUMBER - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var059()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.USER_ID_NUMBER );
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_ID_NUMBER , Long.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ID_NUMBER - Get the attribute value without setting it first.
     **/
    public void Var060()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.USER_ID_NUMBER );
            assertCondition(((Long)value).longValue() > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ID_NUMBER - Set and get the attribute value to a negative value.
     **/
    public void Var061()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ID_NUMBER , Long.valueOf(-1));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     USER_ID_NUMBER - Set and get the attribute value to 0.
     **/
    public void Var062()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ID_NUMBER , Long.valueOf(0));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     USER_ID_NUMBER - Set and get the attribute value to a number that is too high.
     **/
    public void Var063()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ID_NUMBER , Long.valueOf( 4294967294l + 1));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     USER_ID_NUMBER - Set and get the attribute value to a value that is [hopefully] not taken.
     **/
    public void Var064()
    {
        try
        {
            long uid = 94857345L;

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ID_NUMBER , Long.valueOf(uid));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_ID_NUMBER );
            assertCondition(((Long)value).longValue() == uid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ID_NUMBER - Set and get the attribute value that is [hopefully] not taken, and is bigger than an int.
     **/
    public void Var065()
    {
        try
        {
            long uid = 4294967255l;

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ID_NUMBER , Long.valueOf(uid));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_ID_NUMBER );
            assertCondition(((Long)value).longValue() == uid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_ID_NUMBER - Set and get the attribute value that is taken.
     **/
    public void Var066()
    {
        try
        {
            // If the GID is already taken (which should only happen by rare chance, Then flag that differently.
            long uid = 194857483;
            String anotherUser = sandbox_.createUser();
            RUser u2 = new RUser(pwrSys_, anotherUser);
            try
            {
                u2.setAttributeValue(RUser.USER_ID_NUMBER, Long.valueOf(uid));
                u2.commitAttributeChanges();
            }
            catch (Exception e)
            {
                failed("Group ID conflict, setup failed");
            }

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ID_NUMBER , Long.valueOf(uid));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     USER_ID_NUMBER - Get the attribute value with the backward compatibility method.
     **/
    public void Var067()
    {
        try
        {
            long uid = 45894586l;

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_ID_NUMBER , Long.valueOf(uid));
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            int value = u2.getUserIDNumber();
            assertCondition(value == uid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_OPTIONS - Check the attribute meta data in the entire list.
     **/
    public void Var068()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_OPTIONS, String.class, false, 7, null, true, true));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_OPTIONS - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var069()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.USER_OPTIONS);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_OPTIONS, String.class, false, 7, null, true, true));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_OPTIONS - Get the attribute value without setting it first.
     **/
    public void Var070()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.USER_OPTIONS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_OPTIONS - Set and get the attribute value to an empty array.
     **/
    public void Var071()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_OPTIONS, new String[0]);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_OPTIONS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_OPTIONS - Set and get the attribute value to a 1 element array.
     **/
    public void Var072()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_OPTIONS, new String[] { RUser.USER_OPTIONS_KEYWORDS } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_OPTIONS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 1 && asArray[0].equals(RUser.USER_OPTIONS_KEYWORDS));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_OPTIONS - Set and get the attribute value to a 2 element array.
     **/
    public void Var073()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_OPTIONS, new String[] { RUser.USER_OPTIONS_FULL_SCREEN_HELP, RUser.USER_OPTIONS_EXPERT } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_OPTIONS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 2 && asArray[0].equals(RUser.USER_OPTIONS_EXPERT) && asArray[1].equals(RUser.USER_OPTIONS_FULL_SCREEN_HELP));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_OPTIONS - Set and get the attribute value to a 4 element array.
     **/
    public void Var074()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_OPTIONS, new String[] { RUser.USER_OPTIONS_NO_STATUS_MESSAGE, RUser.USER_OPTIONS_ROLL_KEY, RUser.USER_OPTIONS_EXPERT, RUser.USER_OPTIONS_PRINT_COMPLETE_MESSAGE } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_OPTIONS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 4 && asArray[0].equals(RUser.USER_OPTIONS_EXPERT) && asArray[1].equals(RUser.USER_OPTIONS_NO_STATUS_MESSAGE) && asArray[2].equals(RUser.USER_OPTIONS_ROLL_KEY) && asArray[3].equals(RUser.USER_OPTIONS_PRINT_COMPLETE_MESSAGE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_OPTIONS - Set and get the attribute value to a 4 element array, with both status and no status specified.
     **/
    public void Var075()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_OPTIONS, new String[] { RUser.USER_OPTIONS_NO_STATUS_MESSAGE, RUser.USER_OPTIONS_ROLL_KEY, RUser.USER_OPTIONS_STATUS_MESSAGE, RUser.USER_OPTIONS_PRINT_COMPLETE_MESSAGE } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     USER_OPTIONS - Set and get the attribute value to several element array with one of the elements null.
     **/
    public void Var076()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_OPTIONS, new String[] { RUser.USER_OPTIONS_STATUS_MESSAGE, RUser.USER_OPTIONS_EXPERT, null } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     USER_OPTIONS - Set and get the attribute value to several element array with one of the elements not valid.
     **/
    public void Var077()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_OPTIONS, new String[] { "bogus", RUser.USER_OPTIONS_ROLL_KEY, RUser.USER_OPTIONS_KEYWORDS } );
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     USER_OPTIONS - Set and get the attribute value to several element array with one of the elements specified twice.
     **/
    public void Var078()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_OPTIONS, new String[] { RUser.USER_OPTIONS_EXPERT, RUser.USER_OPTIONS_FULL_SCREEN_HELP, RUser.USER_OPTIONS_KEYWORDS, RUser.USER_OPTIONS_KEYWORDS } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_OPTIONS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 3 && asArray[0].equals(RUser.USER_OPTIONS_KEYWORDS) && asArray[1].equals(RUser.USER_OPTIONS_EXPERT) && asArray[2].equals(RUser.USER_OPTIONS_FULL_SCREEN_HELP));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_OPTIONS - Set and get the attribute value to several element array with all possible values specified (except for no status message, because it would conflict with status message).
     **/
    public void Var079()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.USER_OPTIONS, new String[] { RUser.USER_OPTIONS_KEYWORDS, RUser.USER_OPTIONS_EXPERT, RUser.USER_OPTIONS_FULL_SCREEN_HELP, RUser.USER_OPTIONS_STATUS_MESSAGE, RUser.USER_OPTIONS_ROLL_KEY, RUser.USER_OPTIONS_PRINT_COMPLETE_MESSAGE } );
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.USER_OPTIONS);
            String[] asArray = (String[])value;
            assertCondition(asArray.length == 6 && asArray[0].equals(RUser.USER_OPTIONS_KEYWORDS) && asArray[1].equals(RUser.USER_OPTIONS_EXPERT) && asArray[2].equals(RUser.USER_OPTIONS_FULL_SCREEN_HELP) && asArray[3].equals(RUser.USER_OPTIONS_STATUS_MESSAGE) && asArray[4].equals(RUser.USER_OPTIONS_ROLL_KEY) && asArray[5].equals(RUser.USER_OPTIONS_PRINT_COMPLETE_MESSAGE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_PROFILE_NAME - Check the attribute meta data in the entire list.
     **/
    public void Var080()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_PROFILE_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_PROFILE_NAME - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var081()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.USER_PROFILE_NAME);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.USER_PROFILE_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     USER_PROFILE_NAME - Get the attribute value.
     **/
    public void Var082()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.USER_PROFILE_NAME);
            assertCondition(((String)value).equalsIgnoreCase(user_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
