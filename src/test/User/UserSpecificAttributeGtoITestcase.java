///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpecificAttributeGtoITestcase.java
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
 Testcase UserSpecificAttributeGtoITestcase.  This tests the following attributes of the User class:
 <ul>
 <li>GROUP_AUTHORITY
 <li>GROUP_AUTHORITY_TYPE
 <li>GROUP_ID_NUMBER
 <li>GROUP_MEMBER_INDICATOR
 <li>GROUP_PROFILE_NAME
 <li>HIGHEST_SCHEDULING_PRIORITY
 <li>HOME_DIRECTORY
 <li>INDIRECT_USER
 <li>INITIAL_MENU
 <li>INITIAL_PROGRAM
 </ul>
 **/
@SuppressWarnings("deprecation")
public class UserSpecificAttributeGtoITestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserSpecificAttributeGtoITestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
    private UserSandbox sandbox_;
    private String user_;
    private String userNotInGroup_;
    private String groupWithUsers_;
    private String group_;

    private static final long gid_ = 147823;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "USAG", UserTest.COLLECTION.substring(UserTest.COLLECTION.length() - 1));
        String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
        groupWithUsers_ = groupAndUsers[0];
        user_ = groupAndUsers[1];
        userNotInGroup_ = sandbox_.createUser(true);

        // If the GID is already taken (which should only happen by rare chance),
        group_ = sandbox_.createGroup(gid_);
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
     GROUP_AUTHORITY - Check the attribute meta data in the entire list.
     **/
    public void Var001()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.GROUP_AUTHORITY, String.class, false, 5, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var002()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.GROUP_AUTHORITY);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.GROUP_AUTHORITY, String.class, false, 5, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY - Get the attribute value without setting it first.
     **/
    public void Var003()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.GROUP_AUTHORITY);
            assertCondition(((String)value).equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY - Set and get the attribute value to a bogus value, 1 word.
     **/
    public void Var004()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY, "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     GROUP_AUTHORITY - Set and get the attribute value to a bogus value, 2 words.
     **/
    public void Var005()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY, "bogus words");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     GROUP_AUTHORITY - Set and get the attribute value to all.
     **/
    public void Var006()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY, RUser.GROUP_AUTHORITY_ALL);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.GROUP_AUTHORITY);
            assertCondition(((String)value).equals(RUser.GROUP_AUTHORITY_ALL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY - Set and get the attribute value to change.
     **/
    public void Var007()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY, RUser.GROUP_AUTHORITY_CHANGE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.GROUP_AUTHORITY);
            assertCondition(((String)value).equals(RUser.GROUP_AUTHORITY_CHANGE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY - Set and get the attribute value to use.
     **/
    public void Var008()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY, RUser.GROUP_AUTHORITY_USE);
            u.setAttributeValue(RUser.OWNER, RUser.OWNER_USER_PROFILE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.GROUP_AUTHORITY);
            assertCondition(((String)value).equals(RUser.GROUP_AUTHORITY_USE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY - Set and get the attribute value to exclude.
     **/
    public void Var009()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY, RUser.GROUP_AUTHORITY_EXCLUDE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.GROUP_AUTHORITY);
            assertCondition(((String)value).equals(RUser.GROUP_AUTHORITY_EXCLUDE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY - Set and get the attribute value to none.
     **/
    public void Var010()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY, RUser.NONE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.GROUP_AUTHORITY);
            assertCondition(((String)value).equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY - Set and get the attribute value to none when the user does not belong to a group.
     **/
    public void Var011()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userNotInGroup_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY, RUser.NONE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userNotInGroup_);
            Object value = u2.getAttributeValue(RUser.GROUP_AUTHORITY);
            assertCondition(((String)value).equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY - Set and get the attribute value to something other than none when the user does not belong to a group.
     **/
    public void Var012()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userNotInGroup_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY, RUser.GROUP_AUTHORITY_USE);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     GROUP_AUTHORITY - Get the attribute value with the backward compatibility method.
     **/
    public void Var013()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY, RUser.GROUP_AUTHORITY_EXCLUDE);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getGroupAuthority();
            assertCondition(value.equals(RUser.GROUP_AUTHORITY_EXCLUDE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY_TYPE - Check the attribute meta data in the entire list.
     **/
    public void Var014()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.GROUP_AUTHORITY_TYPE, String.class, false, 2, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY_TYPE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var015()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.GROUP_AUTHORITY_TYPE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.GROUP_AUTHORITY_TYPE, String.class, false, 2, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY_TYPE - Get the attribute value without setting it first.
     **/
    public void Var016()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.GROUP_AUTHORITY_TYPE);
            assertCondition(((String)value).equals(RUser.GROUP_AUTHORITY_TYPE_PRIVATE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY_TYPE - Set and get the attribute value to a bogus value, 1 word.
     **/
    public void Var017()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY_TYPE, "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     GROUP_AUTHORITY_TYPE - Set and get the attribute value to a bogus value, 2 words.
     **/
    public void Var018()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY_TYPE, "bogus words");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     GROUP_AUTHORITY_TYPE - Set and get the attribute value to pgp.
     **/
    public void Var019()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY_TYPE, RUser.GROUP_AUTHORITY_TYPE_PGP);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.GROUP_AUTHORITY_TYPE);
            assertCondition(((String)value).equals(RUser.GROUP_AUTHORITY_TYPE_PGP));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY_TYPE - Set and get the attribute value to private.
     **/
    public void Var020()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY_TYPE, RUser.GROUP_AUTHORITY_TYPE_PRIVATE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.GROUP_AUTHORITY_TYPE);
            assertCondition(((String)value).equals(RUser.GROUP_AUTHORITY_TYPE_PRIVATE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_AUTHORITY_TYPE - Get the attribute value with the backward compatibility method.
     **/
    public void Var021()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_AUTHORITY_TYPE, RUser.GROUP_AUTHORITY_TYPE_PGP);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getGroupAuthorityType();
            assertCondition(value.equals(RUser.GROUP_AUTHORITY_TYPE_PGP));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_ID_NUMBER - Check the attribute meta data in the entire list.
     **/
    public void Var022()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.GROUP_ID_NUMBER, Long.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_ID_NUMBER - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var023()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.GROUP_ID_NUMBER);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.GROUP_ID_NUMBER, Long.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_ID_NUMBER - Get the attribute value without setting it first.
     **/
    public void Var024()
    {
        try
        {
            RUser u = new RUser(pwrSys_, group_);
            Object value = u.getAttributeValue(RUser.GROUP_ID_NUMBER);
            assertCondition(((Long)value).longValue() == gid_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_ID_NUMBER - Set and get the attribute value to a negative value.
     **/
    public void Var025()
    {
        try
        {
            RUser u = new RUser(pwrSys_, group_);
            u.setAttributeValue(RUser.GROUP_ID_NUMBER, Long.valueOf(-2));  // -1 is GROUP_ID_GENERATE now.
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     GROUP_ID_NUMBER - Set and get the attribute value to 0.
     **/
    public void Var026()
    {
        try
        {
            RUser u = new RUser(pwrSys_, group_);
            u.setAttributeValue(RUser.GROUP_ID_NUMBER, Long.valueOf(0));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, group_);
            Object value = u2.getAttributeValue(RUser.GROUP_ID_NUMBER);
            assertCondition(((Long)value).longValue() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_ID_NUMBER - Set and get the attribute value to a number that is too high.
     **/
    public void Var027()
    {
        try
        {
            RUser u = new RUser(pwrSys_, group_);
            u.setAttributeValue(RUser.GROUP_ID_NUMBER, Long.valueOf(4294967294l + 1));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     GROUP_ID_NUMBER - Set and get the attribute value to for an existing group, that is [hopefully] not taken.
     **/
    public void Var028()
    {
        try
        {
            RUser u = new RUser(pwrSys_, group_);
            u.setAttributeValue(RUser.GROUP_ID_NUMBER, Long.valueOf(gid_ + 1));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, group_);
            Object value = u2.getAttributeValue(RUser.GROUP_ID_NUMBER);
            assertCondition(((Long)value).longValue() == (gid_ + 1));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_ID_NUMBER - Set and get the attribute value to for an existing group, that is [hopefully] not taken, and is bigger than an int.
     **/
    public void Var029()
    {
        try
        {
            RUser u = new RUser(pwrSys_, group_);
            u.setAttributeValue(RUser.GROUP_ID_NUMBER, Long.valueOf(4294967200l));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, group_);
            Object value = u2.getAttributeValue(RUser.GROUP_ID_NUMBER);
            assertCondition(((Long)value).longValue() == 4294967200l);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_ID_NUMBER - Set and get the attribute value to for an existing group, that is taken.
     **/
    public void Var030()
    {
        try
        {
            // If the GID is already taken (which should only happen by rare chance, then flag that differently.
            long anotherGid = 194857483;
            String anotherGroup = null;
            try
            {
                anotherGroup = sandbox_.createGroup(anotherGid);
            }
            catch (Exception e)
            {
                failed("Group ID conflict, setup failed");
            }

            RUser u = new RUser(pwrSys_, group_);
            u.setAttributeValue(RUser.GROUP_ID_NUMBER, Long.valueOf(anotherGid));
            u.commitAttributeChanges();
            failed("Didn't throw exception"+anotherGroup);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     GROUP_ID_NUMBER - Set and get the attribute value to for a user (not a group).
     **/
    public void Var031()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_ID_NUMBER, Long.valueOf(gid_));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     GROUP_ID_NUMBER - Get the attribute value with the backward compatibility method.
     **/
    public void Var032()
    {
        try
        {
            RUser u = new RUser(pwrSys_, group_);
            u.setAttributeValue(RUser.GROUP_ID_NUMBER, Long.valueOf(gid_ + 2));
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, group_);
            int value = u2.getGroupIDNumber();
            assertCondition(value == gid_ + 2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_MEMBER_INDICATOR - Check the attribute meta data in the entire list.
     **/
    public void Var033()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.GROUP_MEMBER_INDICATOR, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_MEMBER_INDICATOR - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var034()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.GROUP_MEMBER_INDICATOR);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.GROUP_MEMBER_INDICATOR, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_MEMBER_INDICATOR - Get the attribute value for a user.
     **/
    public void Var035()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.GROUP_MEMBER_INDICATOR);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_MEMBER_INDICATOR - Get the attribute value for a group with no users.
     **/
    public void Var036()
    {
        try
        {
            RUser u = new RUser(pwrSys_, group_);
            Object value = u.getAttributeValue(RUser.GROUP_MEMBER_INDICATOR);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_MEMBER_INDICATOR - Get the attribute value for a group with users.
     **/
    public void Var037()
    {
        try
        {
            RUser u = new RUser(pwrSys_, groupWithUsers_);
            Object value = u.getAttributeValue(RUser.GROUP_MEMBER_INDICATOR);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_MEMBER_INDICATOR - Get the attribute value  with the backward compatibility method.
     **/
    public void Var038()
    {
        try
        {
            User u = new User(pwrSys_, groupWithUsers_);
            boolean value = u.isGroupHasMember();
            assertCondition(value == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_PROFILE_NAME - Check the attribute meta data in the entire list.
     **/
    public void Var039()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.GROUP_PROFILE_NAME, String.class, false, 1, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_PROFILE_NAME - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var040()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.GROUP_PROFILE_NAME);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.GROUP_PROFILE_NAME, String.class, false, 1, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_PROFILE_NAME - Get the attribute value without setting it first, for a user that is in a group.
     **/
    public void Var041()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.GROUP_PROFILE_NAME);
            assertCondition(((String)value).equals(groupWithUsers_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_PROFILE_NAME - Get the attribute value without setting it first, for a user that is not in a group.
     **/
    public void Var042()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userNotInGroup_);
            Object value = u.getAttributeValue(RUser.GROUP_PROFILE_NAME);
            assertCondition(((String)value).equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_PROFILE_NAME - Set and get the attribute value to a bogus value, 1 word.
     **/
    public void Var043()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_PROFILE_NAME, "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     GROUP_PROFILE_NAME - Set and get the attribute value to a bogus value, 2 words.
     **/
    public void Var044()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.GROUP_PROFILE_NAME, "bogus words");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     GROUP_PROFILE_NAME - Set and get the attribute value to change a user from not being in a group to being in a group.
     **/
    public void Var045()
    {
        try
        {
            String userNotInGroup = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userNotInGroup);
            u.setAttributeValue(RUser.GROUP_PROFILE_NAME, groupWithUsers_);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userNotInGroup);
            Object value = u2.getAttributeValue(RUser.GROUP_PROFILE_NAME);
            assertCondition(((String)value).equals(groupWithUsers_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_PROFILE_NAME - Set and get the attribute value to change a user from being in a group to not being in a group.
     **/
    public void Var046()
    {
        try
        {
            String[] groupAndUser = sandbox_.createGroupAndUsers(1);
            String user = groupAndUser[1];
            RUser u = new RUser(pwrSys_, user);
            u.setAttributeValue(RUser.GROUP_PROFILE_NAME, RUser.NONE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user);
            Object value = u2.getAttributeValue(RUser.GROUP_PROFILE_NAME);
            assertCondition(((String)value).equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_PROFILE_NAME - Set and get the attribute value to change a user from being in a group to being in another group.
     **/
    public void Var047()
    {
        try
        {
            String[] groupAndUser = sandbox_.createGroupAndUsers(1);
            String user = groupAndUser[1];
            RUser u = new RUser(pwrSys_, user);
            u.setAttributeValue(RUser.GROUP_PROFILE_NAME, groupWithUsers_);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user);
            Object value = u2.getAttributeValue(RUser.GROUP_PROFILE_NAME);
            assertCondition(((String)value).equals(groupWithUsers_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     GROUP_PROFILE_NAME - Set and get the attribute value to change a group to be in another group.
     **/
    public void Var048()
    {
        try
        {
            String group = sandbox_.createGroup();
            RUser u = new RUser(pwrSys_, group);
            u.setAttributeValue(RUser.GROUP_PROFILE_NAME, groupWithUsers_);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     GROUP_PROFILE_NAME - Get the attribute value with the backward compatibility method.
     **/
    public void Var049()
    {
        try
        {
            User u = new User(pwrSys_, user_);
            String value = u.getGroupProfileName();
            assertCondition(value.equals(groupWithUsers_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     HIGHEST_SCHEDULING_PRIORITY - Check the attribute meta data in the entire list.
     **/
    public void Var050()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.HIGHEST_SCHEDULING_PRIORITY, Integer.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     HIGHEST_SCHEDULING_PRIORITY - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var051()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.HIGHEST_SCHEDULING_PRIORITY);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.HIGHEST_SCHEDULING_PRIORITY, Integer.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     HIGHEST_SCHEDULING_PRIORITY - Get the attribute value without setting it first.
     **/
    public void Var052()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.HIGHEST_SCHEDULING_PRIORITY);
            assertCondition(((Integer)value).intValue() == 3);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     HIGHEST_SCHEDULING_PRIORITY - Set and get the attribute value to a bogus value, -1.
     **/
    public void Var053()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.HIGHEST_SCHEDULING_PRIORITY, Integer.valueOf(-1));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     HIGHEST_SCHEDULING_PRIORITY - Set and get the attribute value to a bogus value, 10.
     **/
    public void Var054()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.HIGHEST_SCHEDULING_PRIORITY, Integer.valueOf(10));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     HIGHEST_SCHEDULING_PRIORITY - Set and get the attribute value to 0.
     **/
    public void Var055()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.HIGHEST_SCHEDULING_PRIORITY, Integer.valueOf(0));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.HIGHEST_SCHEDULING_PRIORITY);
            assertCondition(((Integer)value).intValue() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     HIGHEST_SCHEDULING_PRIORITY - Set and get the attribute value to 9.
     **/
    public void Var056()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.HIGHEST_SCHEDULING_PRIORITY, Integer.valueOf(9));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.HIGHEST_SCHEDULING_PRIORITY);
            assertCondition(((Integer)value).intValue() == 9);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     HIGHEST_SCHEDULING_PRIORITY - Get the attribute value with the backward compatibility method.
     **/
    public void Var057()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.HIGHEST_SCHEDULING_PRIORITY, Integer.valueOf(5));
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            int value = u2.getHighestSchedulingPriority();
            assertCondition(value == 5);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     HOME_DIRECTORY - Check the attribute meta data in the entire list.
     **/
    public void Var058()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.HOME_DIRECTORY, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     HOME_DIRECTORY - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var059()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.HOME_DIRECTORY);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.HOME_DIRECTORY, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     HOME_DIRECTORY - Get the attribute value without setting it first.
     **/
    public void Var060()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.HOME_DIRECTORY);
            assertCondition(((String)value).equals("/home/" + user_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     HOME_DIRECTORY - Set and get the attribute value.
     **/
    public void Var061()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.HOME_DIRECTORY, "/Eden Prairie");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.HOME_DIRECTORY);
            assertCondition(((String)value).equals("/Eden Prairie"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     HOME_DIRECTORY - Set and get the attribute value to an empty string.
     **/
    public void Var062()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.HOME_DIRECTORY, "");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     HOME_DIRECTORY - Get the attribute value with the backward compatibility method.
     **/
    public void Var063()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.HOME_DIRECTORY, "/home/cnock/tempa");
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getHomeDirectory();
            assertCondition(value.equals("/home/cnock/tempa"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INDIRECT_USER - Check the attribute meta data in the entire list.
     **/
    public void Var064()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.INDIRECT_USER, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INDIRECT_USER - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var065()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.INDIRECT_USER);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.INDIRECT_USER, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INDIRECT_USER - Get the attribute value without setting it first.
     **/
    public void Var066()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userNotInGroup_);
            Object value = u.getAttributeValue(RUser.INDIRECT_USER);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INDIRECT_USER - Set and get the attribute value to true.
     **/
    public void Var067()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userNotInGroup_);
            u.setAttributeValue(RUser.INDIRECT_USER, Boolean.TRUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userNotInGroup_);
            Object value = u2.getAttributeValue(RUser.INDIRECT_USER);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INDIRECT_USER - Set and get the attribute value to false.
     **/
    public void Var068()
    {
        try
        {
            RUser u = new RUser(pwrSys_, userNotInGroup_);
            u.setAttributeValue(RUser.INDIRECT_USER, Boolean.FALSE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, userNotInGroup_);
            Object value = u2.getAttributeValue(RUser.INDIRECT_USER);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_MENU - Check the attribute meta data in the entire list.
     **/
    public void Var069()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.INITIAL_MENU, String.class, false, 1, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_MENU - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var070()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.INITIAL_MENU);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.INITIAL_MENU, String.class, false, 1, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_MENU - Get the attribute value without setting it first.
     **/
    public void Var071()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.INITIAL_MENU);
            assertCondition(((String)value).equals("/QSYS.LIB/%LIBL%.LIB/MAIN.MNU"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_MENU - Set and get the attribute value to signoff.
     **/
    public void Var072()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_MENU, RUser.INITIAL_MENU_SIGNOFF);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.INITIAL_MENU);
            assertCondition(((String)value).equals(RUser.INITIAL_MENU_SIGNOFF));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_MENU - Set and get the attribute value to a valid menu.
     **/
    public void Var073()
    {
        try
        {
            String menuName = "/QSYS.LIB/CMDADD.MNU";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_MENU, menuName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.INITIAL_MENU);
            assertCondition(((String)value).equals(menuName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_MENU - Set and get the attribute value to a valid menu, but with mixed case.
     **/
    public void Var074()
    {
        try
        {
            String menuName = "/QsYs.LiB/CmDMnU.MnU";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_MENU, menuName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.INITIAL_MENU);
            assertCondition(((String)value).equalsIgnoreCase(menuName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_MENU - Set the attribute value to be a valid menu name that does not exist.
     **/
    public void Var075()
    {
        try
        {
            String menuName = "/QSYS.LIB/NOTEXIST.MNU";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_MENU, menuName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.INITIAL_MENU);
            assertCondition(((String)value).equals(menuName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_MENU - Set the attribute value to be a invalid menu name.
     **/
    public void Var076()
    {
        try
        {
            String menuName = "//////";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_MENU, menuName);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     INITIAL_MENU - Set the attribute value to be the empty string.
     **/
    public void Var077()
    {
        try
        {
            String menuName = "";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_MENU, menuName);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     INITIAL_MENU - Get the attribute value with the backward compatibility method.
     **/
    public void Var078()
    {
        try
        {
            String menuName = "/QSYS.LIB/CMDCMD.MNU";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_MENU, menuName);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getInitialMenu();
            assertCondition(value.equals(menuName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_PROGRAM - Check the attribute meta data in the entire list.
     **/
    public void Var079()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.INITIAL_PROGRAM, String.class, false, 1, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_PROGRAM - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var080()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.INITIAL_PROGRAM);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.INITIAL_PROGRAM, String.class, false, 1, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_PROGRAM - Get the attribute value without setting it first.
     **/
    public void Var081()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.INITIAL_PROGRAM);
            assertCondition(((String)value).equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_PROGRAM - Set and get the attribute value to none.
     **/
    public void Var082()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_PROGRAM, RUser.NONE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.INITIAL_PROGRAM);
            assertCondition(((String)value).equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_PROGRAM - Set and get the attribute value to a valid program.
     **/
    public void Var083()
    {
        try
        {
            String programName = "/QSYS.LIB/QWTCHGJB.PGM";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_PROGRAM, programName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.INITIAL_PROGRAM);
            assertCondition(((String)value).equals(programName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_PROGRAM - Set and get the attribute value to a valid program, mixed case.
     **/
    public void Var084()
    {
        try
        {
            String programName = "/Qsys.liB/Qcmdexc.pgM";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_PROGRAM, programName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.INITIAL_PROGRAM);
            assertCondition(((String)value).equalsIgnoreCase(programName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_PROGRAM - Set the attribute value to be a valid program name that does not exist.
     **/
    public void Var085()
    {
        try
        {
            String programName = "/QSYS.LIB/NOTEXIST.PGM";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_PROGRAM, programName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.INITIAL_PROGRAM);
            assertCondition(((String)value).equals(programName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     INITIAL_PROGRAM - Set the attribute value to be a invalid program name.
     **/
    public void Var086()
    {
        try
        {
            String programName = "//////";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_PROGRAM, programName);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     INITIAL_PROGRAM - Get the attribute value with the backward compatibility method.
     **/
    public void Var087()
    {
        try
        {
            String programName = "/QSYS.LIB/QCMD.PGM";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.INITIAL_PROGRAM, programName);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getInitialProgram();
            assertCondition(value.equals(programName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
