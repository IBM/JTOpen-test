///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserListBasicTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import java.util.Enumeration;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.User;
import com.ibm.as400.access.UserList;
import com.ibm.as400.resource.RUser;
import com.ibm.as400.resource.RUserList;
import com.ibm.as400.resource.ResourceException;

import test.Testcase;
import test.UserTest;

/**
 Testcase UserListBasicTestcase.  This tests the following methods of the UserList class:
 <ul>
 <li>constructors
 <li>serialization
 <li>getGroupInfo()
 <li>getSystem()
 <li>getUserInfo()
 <li>setGroupInfo()
 <li>setSystem()
 <li>setUserInfo()
 </ul>
 <p>And the following methods of the RUserList class:
 <ul>
 <li>constructors
 <li>serialization
 <li>getSystem()
 <li>setSystem()
 </ul>
 **/
@SuppressWarnings("deprecation")
public class UserListBasicTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserListBasicTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
    private UserSandbox sandbox_;

    private String groupWithMembers_;
    private String groupWithNoMembers_;
    private String userNotInGroup_;
    private String userInGroup_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "ULB", UserTest.COLLECTION.substring(UserTest.COLLECTION.length() - 1));

        userNotInGroup_ = sandbox_.createUser();
        groupWithNoMembers_ = sandbox_.createGroup();
        String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
        groupWithMembers_ = groupAndUsers[0];
        userInGroup_ = groupAndUsers[1];
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup()
      throws Exception
    {
        sandbox_.cleanup();
    }

    // Verifies the user list.
    // @param  userList  The user list.
    // @param  expectedContents  A list of user names that must appear in the user list.
    // @param  expectedNonContents  A list of user names that must not appear in the user list.
    // @return  true if the list is correct, false otherwise.
    private boolean verifyList(UserList userList, String[] expectedContents, String[] expectedNonContents) throws Exception
    {
        // Verify the system.
        if (userList.getSystem() != pwrSys_)
        {
            System.out.println("System object did not match expected.");
            return false;
        }

        Enumeration<User> enumeration = userList.getUsers();

        // Check the elements of the list.
        Vector<String> checkList = new Vector<String>();
        for (int i = 0; i < expectedContents.length; ++i)
        {
            checkList.addElement(expectedContents[i]);
        }

        while (enumeration.hasMoreElements())
        {
            User u = (User)enumeration.nextElement();
            String name = u.getName();
            if (checkList.contains(name))
            {
                checkList.removeElement(name);
            }
            else
            {
                for (int j = 0; j < expectedNonContents.length; ++j)
                {
                    if (name.equals(expectedNonContents[j]))
                    {
                        System.out.println("List contained unexpected name: " + name + ".");
                        return false;
                    }
                }
            }
        }

        if (checkList.size() > 0)
        {
            System.out.println("Expected name " + checkList.elementAt(0) + " was not found.");
            return false;
        }

        return true;
    }

    /**
     constructor() with 0 parms - Should work.
     **/
    public void Var001()
    {
        try
        {
            UserList ul = new UserList();
            assertCondition(ul.getSystem() == null && ul.getUserInfo() == UserList.ALL && ul.getGroupInfo() == UserList.NONE);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 1 parm - Pass null for system.
     **/
    public void Var002()
    {
        try
        {
            UserList ul = new UserList(null);
            failed("Didn't throw exception"+ul);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 1 parm - Should work.
     **/
    public void Var003()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            assertCondition(ul.getSystem() == pwrSys_ && ul.getUserInfo() == UserList.ALL && ul.getGroupInfo() == UserList.NONE && verifyList(ul, new String[] { userNotInGroup_, userInGroup_, groupWithMembers_, groupWithNoMembers_ }, new String[0]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 3 parms - Pass null for system.
     **/
    public void Var004()
    {
        try
        {
            UserList ul = new UserList(null, UserList.ALL, UserList.NONE);
            failed("Didn't throw exception"+ul);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 3 parms - Pass null for user info.
     **/
    public void Var005()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, null, UserList.NONE);
            failed("Didn't throw exception"+ul);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 3 parms - Pass null for group info.
     **/
    public void Var006()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.ALL, null);
            failed("Didn't throw exception"+ul);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 3 parms - Pass invalid system.  This should work, because the constructor does not check the validity.
     **/
    public void Var007()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus".toCharArray());
            UserList ul = new UserList(bogus, UserList.MEMBER, "AGroup");
            assertCondition(ul.getSystem() == bogus && ul.getUserInfo() == UserList.MEMBER && ul.getGroupInfo().equals("AGroup"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 3 parms - Pass invalid value for user info.
     **/
    public void Var008()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, "JoeBlow", UserList.NONE);
            failed("Didn't throw exception"+ul);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = ALL, group info = NONE.
     **/
    public void Var009()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.ALL, UserList.NONE);
            assertCondition(verifyList(ul, new String[] { userNotInGroup_, userInGroup_, groupWithMembers_, groupWithNoMembers_ }, new String[0]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = ALL, group info = NOGROUP.
     **/
    public void Var010()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.ALL, UserList.NOGROUP);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = ALL, group info = a group profile.
     **/
    public void Var011()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.ALL, groupWithMembers_);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = USER, group info = NONE.
     **/
    public void Var012()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.USER, UserList.NONE);
            assertCondition(verifyList(ul, new String[] { userNotInGroup_, userInGroup_ }, new String[] { groupWithMembers_, groupWithNoMembers_} ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = USER, group info = NOGROUP.
     **/
    public void Var013()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.USER, UserList.NOGROUP);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = USER, group info = a group profile.
     **/
    public void Var014()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.USER, groupWithMembers_);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = GROUP, group info = NONE.
     **/
    public void Var015()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.GROUP, UserList.NONE);
            assertCondition(verifyList(ul, new String[] { groupWithMembers_, groupWithNoMembers_ }, new String[] { userNotInGroup_, userInGroup_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = GROUP, group info = NOGROUP.
     **/
    public void Var016()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.GROUP, UserList.NOGROUP);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = GROUP, group info = a group profile.
     **/
    public void Var017()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.GROUP, groupWithMembers_);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = MEMBER, group info = NONE.
     **/
    public void Var018()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, UserList.NONE);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = MEMBER, group info = NOGROUP.
     **/
    public void Var019()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, UserList.NOGROUP);
            assertCondition(verifyList(ul, new String[] { userNotInGroup_ }, new String[] { userInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = MEMBER, group info = group profile which does not exist.
     **/
    public void Var020()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, "NOTEXIST");
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = MEMBER, group info = group profile which is really a user profile, not a group profile.
     **/
    public void Var021()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, userInGroup_);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = MEMBER, group info = group profile which has members.
     **/
    public void Var022()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, groupWithMembers_);
            ul.getUsers();
            int length = ul.getLength();
            assertCondition(length == 1 && verifyList(ul, new String[] { userInGroup_ }, new String[] { userNotInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = MEMBER, group info = group profile which has no members.
     **/
    public void Var023()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, groupWithNoMembers_);
            ul.getUsers();
            int length = ul.getLength();
            assertCondition(length == 0 && verifyList(ul, new String[0], new String[] { userNotInGroup_, userInGroup_, groupWithNoMembers_, groupWithMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 3 parms - Pass valid values with user info = MEMBER, group info = group profile specified as lower case name.
     **/
    public void Var024()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, groupWithMembers_.toLowerCase());
            assertCondition(verifyList(ul, new String[] { userInGroup_ }, new String[] { userNotInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 4 parms - Pass null for system.
     **/
    public void Var025()
    {
        try
        {
            UserList ul = new UserList(null, UserList.ALL, UserList.NONE, UserList.ALL);
            failed("Didn't throw exception"+ul);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 4 parms - Pass null for user info.
     **/
    public void Var026()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, null, UserList.NONE, UserList.ALL);
            failed("Didn't throw exception"+ul);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 4 parms - Pass null for group info.
     **/
    public void Var027()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.ALL, null, UserList.ALL);
            failed("Didn't throw exception"+ul);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 4 parms - Pass null for user profile.
     **/
    public void Var028()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.ALL, UserList.NONE, null);
            failed("Didn't throw exception"+ul);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 4 parms - Pass invalid system.  This should work, because the constructor does not check the validity.
     **/
    public void Var029()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus".toCharArray());
            UserList ul = new UserList(bogus, UserList.MEMBER, "AGroup", UserList.ALL);
            assertCondition(ul.getSystem() == bogus && ul.getUserInfo() == UserList.MEMBER && ul.getGroupInfo().equals("AGroup") && ul.getUserProfile() == UserList.ALL);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 4 parms - Pass invalid value for user info.
     **/
    public void Var030()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, "JoeBlow", UserList.NONE, UserList.ALL);
            failed("Didn't throw exception"+ul);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = ALL, group info = NONE.
     **/
    public void Var031()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.ALL, UserList.NONE, UserList.ALL);
            assertCondition(verifyList(ul, new String[] { userNotInGroup_, userInGroup_, groupWithMembers_, groupWithNoMembers_ }, new String[0]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = ALL, group info = NOGROUP.
     **/
    public void Var032()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.ALL, UserList.NOGROUP, UserList.ALL);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = ALL, group info = a group profile.
     **/
    public void Var033()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.ALL, groupWithMembers_, UserList.ALL);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = USER, group info = NONE.
     **/
    public void Var034()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.USER, UserList.NONE, UserList.ALL);
            assertCondition(verifyList(ul, new String[] { userNotInGroup_, userInGroup_ }, new String[] { groupWithMembers_, groupWithNoMembers_} ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = USER, group info = NOGROUP.
     **/
    public void Var035()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.USER, UserList.NOGROUP, UserList.ALL);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = USER, group info = a group profile.
     **/
    public void Var036()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.USER, groupWithMembers_, UserList.ALL);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = GROUP, group info = NONE.
     **/
    public void Var037()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.GROUP, UserList.NONE, UserList.ALL);
            assertCondition(verifyList(ul, new String[] { groupWithMembers_, groupWithNoMembers_ }, new String[] { userNotInGroup_, userInGroup_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = GROUP, group info = NOGROUP.
     **/
    public void Var038()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.GROUP, UserList.NOGROUP, UserList.ALL);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = GROUP, group info = a group profile.
     **/
    public void Var039()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.GROUP, groupWithMembers_, UserList.ALL);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = MEMBER, group info = NONE.
     **/
    public void Var040()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, UserList.NONE, UserList.ALL);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = MEMBER, group info = NOGROUP.
     **/
    public void Var041()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, UserList.NOGROUP, UserList.ALL);
            assertCondition(verifyList(ul, new String[] { userNotInGroup_ }, new String[] { userInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = MEMBER, group info = group profile which does not exist.
     **/
    public void Var042()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, "NOTEXIST", UserList.ALL);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = MEMBER, group info = group profile which is really a user profile, not a group profile.
     **/
    public void Var043()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, userInGroup_, UserList.ALL);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = MEMBER, group info = group profile which has members.
     **/
    public void Var044()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, groupWithMembers_, UserList.ALL);
            ul.getUsers();
            int length = ul.getLength();
            assertCondition(length == 1 && verifyList(ul, new String[] { userInGroup_ }, new String[] { userNotInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = MEMBER, group info = group profile which has no members.
     **/
    public void Var045()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, groupWithNoMembers_, UserList.ALL);
            ul.getUsers();
            int length = ul.getLength();
            assertCondition(length == 0 && verifyList(ul, new String[0], new String[] { userNotInGroup_, userInGroup_, groupWithNoMembers_, groupWithMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 4 parms - Pass valid values with user info = MEMBER, group info = group profile specified as lower case name.
     **/
    public void Var046()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.MEMBER, groupWithMembers_.toLowerCase(), UserList.ALL);
            assertCondition(verifyList(ul, new String[] { userInGroup_ }, new String[] { userNotInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 4 parms - Pass invalid value for user profile.
     **/
    public void Var047()
    {
        try
        {
            UserList ul = new UserList(pwrSys_, UserList.ALL, UserList.NONE, "ELEVENISBIG");
            failed("Didn't throw exception"+ul);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     serialization - Verify that the properties are set and verify that its usable.
     **/
    public void Var048()
    {
        try
        {
            UserList ul1 = new UserList(pwrSys_, UserList.MEMBER, userInGroup_);
            UserList ul2 = (UserList)UserTest.serialize(ul1);
            assertCondition(ul2.getSystem().getSystemName().equals(pwrSys_.getSystemName()) && ul2.getSystem().getUserId().equals(pwrSys_.getUserId()) && ul2.getUserInfo().equals(UserList.MEMBER) && ul2.getGroupInfo().equals(userInGroup_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getGroupInfo() - Should return the default when the group info has not been set.
     **/
    public void Var049()
    {
        try
        {
            UserList ul = new UserList();
            assertCondition(ul.getGroupInfo().equals(UserList.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getGroupInfo() - Should return the group info when the group info has been set.
     **/
    public void Var050()
    {
        try
        {
            UserList ul = new UserList(systemObject_, UserList.MEMBER, "Montana");
            assertCondition(ul.getGroupInfo().equals("Montana"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSystem() - Should return null when the system has not been set.
     **/
    public void Var051()
    {
        try
        {
            UserList ul = new UserList();
            assertCondition(ul.getSystem() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSystem() - Should return the system when the system has been set.
     **/
    public void Var052()
    {
        try
        {
            UserList ul = new UserList(systemObject_, UserList.MEMBER, "NDakota");
            assertCondition(ul.getSystem() == systemObject_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUserInfo() - Should return the default when the user info has not been set.
     **/
    public void Var053()
    {
        try
        {
            UserList ul = new UserList();
            assertCondition(ul.getUserInfo().equals(UserList.ALL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUserInfo() - Should return the group info when the group info has been set.
     **/
    public void Var054()
    {
        try
        {
            UserList ul = new UserList(systemObject_, UserList.USER, UserList.NONE);
            assertCondition(ul.getUserInfo().equals(UserList.USER));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUserProfile() - Should return the default when the user profile has not been set.
     **/
    public void Var055()
    {
        try
        {
            UserList ul = new UserList();
            assertCondition(ul.getUserProfile().equals(UserList.ALL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUserProfile() - Should return the group info when the group profile has been set.
     **/
    public void Var056()
    {
        try
        {
            UserList ul = new UserList(systemObject_, UserList.USER, UserList.NONE, "JAVA*");
            assertCondition(ul.getUserProfile().equals("JAVA*"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setGroupInfo() - Should throw an exception if null is passed.
     **/
    public void Var057()
    {
        try
        {
            UserList u = new UserList();
            u.setGroupInfo(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setGroupInfo() - Set to an invalid name (too long).
     **/
    public void Var058()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            u.setGroupInfo("sjdjdjdsjkdsljlkdsfjlkfdsjklfdsjlkfdsjlkfdsdsfk");
            u.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     setGroupInfo() - Set to an invalid name (empty string).
     **/
    public void Var059()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            u.setGroupInfo("");
            u.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     setGroupInfo() - Set to an invalid name (a group that does not exist).
     **/
    public void Var060()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            u.setGroupInfo("NOTEXIST");
            u.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     setGroupInfo() - Should fire a property change event.
     **/
    public void Var061()
    {
        try
        {
            UserList u = new UserList(systemObject_, UserList.MEMBER, "Utah");
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setGroupInfo("Kansas");
            assertCondition(pcl.eventCount_ == 1 && pcl.event_.getPropertyName().equals("groupInfo") && pcl.event_.getOldValue().equals("Utah") && pcl.event_.getNewValue().equals("Kansas"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setGroupInfo() - Should fire a vetoable change event.
     **/
    public void Var062()
    {
        try
        {
            UserList u = new UserList(systemObject_, UserList.MEMBER, "Oklahoma");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.setGroupInfo("Texas");
            assertCondition(vcl.eventCount_ == 1 && vcl.event_.getPropertyName().equals("groupInfo") && vcl.event_.getOldValue().equals("Oklahoma") && vcl.event_.getNewValue().equals("Texas"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setGroupInfo() - Should throw a PropertyVetoException if the change is vetoed.
     **/
    public void Var063()
    {
        try
        {
            UserList u = new UserList(systemObject_, UserList.MEMBER, "Missouri");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_(true);
            u.addVetoableChangeListener(vcl);
            u.setGroupInfo("Arkansas");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.beans.PropertyVetoException");
        }
    }

    /**
     setUserInfo() - Should throw an exception if null is passed.
     **/
    public void Var064()
    {
        try
        {
            UserList u = new UserList();
            u.setUserInfo(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setUserInfo() - Set to an invalid name (empty string).
     **/
    public void Var065()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            u.setUserInfo("");
            u.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setUserInfo() - Set to an invalid name (an invalid option).
     **/
    public void Var066()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            u.setUserInfo("NOTEXIST");
            u.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setUserInfo() - Should fire a property change event.
     **/
    public void Var067()
    {
        try
        {
            UserList u = new UserList(systemObject_, UserList.USER, UserList.NONE);
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setUserInfo(UserList.GROUP);
            assertCondition(pcl.eventCount_ == 1 && pcl.event_.getPropertyName().equals("userInfo") && pcl.event_.getOldValue().equals(UserList.USER) && pcl.event_.getNewValue().equals(UserList.GROUP));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setUserInfo() - Should fire a vetoable change event.
     **/
    public void Var068()
    {
        try
        {
            UserList u = new UserList(systemObject_, UserList.MEMBER, "Florida");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.setUserInfo(UserList.USER);
            assertCondition(vcl.eventCount_ == 1 && vcl.event_.getPropertyName().equals("userInfo") && vcl.event_.getOldValue().equals(UserList.MEMBER) && vcl.event_.getNewValue().equals(UserList.USER));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setUserInfo() - Should throw a PropertyVetoException if the change is vetoed.
     **/
    public void Var069()
    {
        try
        {
            UserList u = new UserList(systemObject_, UserList.GROUP, UserList.NONE);
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_(true);
            u.addVetoableChangeListener(vcl);
            u.setUserInfo(UserList.ALL);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.beans.PropertyVetoException");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = ALL, group info = NONE.
     **/
    public void Var070()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.ALL);
            ul.setGroupInfo(UserList.NONE);
            assertCondition(verifyList(ul, new String[] { userNotInGroup_, userInGroup_, groupWithMembers_, groupWithNoMembers_ }, new String[0]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = ALL, group info = NOGROUP.
     **/
    public void Var071()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.ALL);
            ul.setGroupInfo(UserList.NOGROUP);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = ALL, group info = a group profile.
     **/
    public void Var072()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.ALL);
            ul.setGroupInfo(groupWithMembers_);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = USER, group info = NONE.
     **/
    public void Var073()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.USER);
            ul.setGroupInfo(UserList.NONE);
            assertCondition(verifyList(ul, new String[] { userNotInGroup_, userInGroup_ }, new String[] { groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = USER, group info = NOGROUP.
     **/
    public void Var074()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.USER);
            ul.setGroupInfo(UserList.NOGROUP);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = USER, group info = a group profile.
     **/
    public void Var075()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.USER);
            ul.setGroupInfo(groupWithMembers_);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = GROUP, group info = NONE.
     **/
    public void Var076()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.GROUP);
            ul.setGroupInfo(UserList.NONE);
            assertCondition(verifyList(ul, new String[] { groupWithMembers_, groupWithNoMembers_ }, new String[] { userNotInGroup_, userInGroup_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = GROUP, group info = NOGROUP.
     **/
    public void Var077()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.GROUP);
            ul.setGroupInfo(UserList.NOGROUP);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = GROUP, group info = a group profile.
     **/
    public void Var078()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.GROUP);
            ul.setGroupInfo(groupWithMembers_);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = MEMBER, group info = NONE.
     **/
    public void Var079()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.MEMBER);
            ul.setGroupInfo(UserList.NONE);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = MEMBER, group info = NOGROUP.
     **/
    public void Var080()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.MEMBER);
            ul.setGroupInfo(UserList.NOGROUP);
            assertCondition(verifyList(ul, new String[] { userNotInGroup_ }, new String[] { userInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = MEMBER, group info = group profile which does not exist.
     **/
    public void Var081()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.MEMBER);
            ul.setGroupInfo("NOTEXIST");
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = MEMBER, group info = group profile which is really a user profile, not a group profile.
     **/
    public void Var082()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.MEMBER);
            ul.setGroupInfo(userInGroup_);
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = MEMBER, group info = group profile which has members.
     **/
    public void Var083()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.MEMBER);
            ul.setGroupInfo(groupWithMembers_);
            ul.getUsers();
            int length = ul.getLength();
            assertCondition(length == 1 && verifyList(ul, new String[] { userInGroup_ }, new String[] { userNotInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = MEMBER, group info = group profile which has no members.
     **/
    public void Var084()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.MEMBER);
            ul.setGroupInfo(groupWithNoMembers_);
            ul.getUsers();
            int length = ul.getLength();
            assertCondition(length == 0 && verifyList(ul, new String[0], new String[] { userNotInGroup_, userInGroup_, groupWithNoMembers_, groupWithMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setGroupInfo()/setUserInfo() - Pass valid values with user info = MEMBER, group info = group profile specified as lower case name.
     **/
    public void Var085()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.MEMBER);
            ul.setGroupInfo(groupWithMembers_.toLowerCase());
            assertCondition(verifyList(ul, new String[] { userInGroup_ }, new String[] { userNotInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Should throw an exception if null is passed.
     **/
    public void Var086()
    {
        try
        {
            UserList u = new UserList(systemObject_);
            u.setSystem(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setSystem() - Set to an invalid system.  Should be reflected by getSystem(), since the validity is not checked here.
     **/
    public void Var087()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus".toCharArray());
            UserList u = new UserList(systemObject_);
            u.setSystem(bogus);
            assertCondition(u.getSystem().getSystemName().equals("bogus") && u.getSystem().getUserId().equalsIgnoreCase("bogus"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Set to a valid name.  Should be reflected by getSystem() and verify that it is used.
     **/
    public void Var088()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus".toCharArray());
            UserList u = new UserList(bogus);
            u.setSystem(pwrSys_);
            Enumeration<User> enumeration = u.getUsers();
            User user = (User)enumeration.nextElement();
            assertCondition(u.getSystem().getSystemName().equals(pwrSys_.getSystemName()) && u.getSystem().getUserId().equals(pwrSys_.getUserId()) && user != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Set to a valid name after the User object has made a connection.
     **/
    public void Var089()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            u.getUsers();
            u.setSystem(systemObject_);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     setSystem() - Should fire a property change event.
     **/
    public void Var090()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1".toCharArray());
            AS400 temp2 = new AS400("temp2", "temp2", "temp2".toCharArray());
            UserList u = new UserList(temp1);
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
     setSystem() - Should fire a vetoable change event.
     **/
    public void Var091()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1".toCharArray());
            AS400 temp2 = new AS400("temp2", "temp2", "temp2".toCharArray());
            UserList u = new UserList(temp1);
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
     setSystem() - Should throw a PropertyVetoException if the change is vetoed.
     **/
    public void Var092()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1".toCharArray());
            AS400 temp2 = new AS400("temp2", "temp2", "temp2".toCharArray());
            UserList u = new UserList(temp1);
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_(true);
            u.addVetoableChangeListener(vcl);
            u.setSystem(temp2);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.beans.PropertyVetoException");
        }
    }

    /**
     setUserProfile() - Should throw an exception if null is passed.
     **/
    public void Var093()
    {
        try
        {
            UserList u = new UserList();
            u.setUserProfile(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setUserProfile() - Set to an invalid name (too long).
     **/
    public void Var094()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            u.setUserProfile("sjdjdjdsjkdsljlkdsfjlkfdsjklfdsjlkfdsjlkfdsdsfk");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     setUserProfile() - Set to an invalid name (empty string).
     **/
    public void Var095()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            u.setUserProfile("");
            u.getUsers();
            assertCondition(u.getLength() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setUserProfile() - Set to an invalid name (a profile that does not exist).
     **/
    public void Var096()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            u.setUserProfile("NOTEXIST");
            u.getUsers();
            assertCondition(u.getLength() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setUserProfile() - Pass valid values with user profile = ALL.
     **/
    public void Var097()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.ALL);
            ul.setGroupInfo(UserList.NONE);
            ul.setUserProfile(UserList.ALL);
            assertCondition(verifyList(ul, new String[] { userNotInGroup_, userInGroup_, groupWithMembers_, groupWithNoMembers_ }, new String[0]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setUserProfile() - Pass valid values with specific user profile.
     **/
    public void Var098()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.ALL);
            ul.setGroupInfo(UserList.NONE);
            ul.setUserProfile(userNotInGroup_);
            assertCondition(verifyList(ul, new String[] { userNotInGroup_}, new String[] { userInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setUserProfile() - Pass valid values with wildcard user profile.
     **/
    public void Var099()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.ALL);
            ul.setGroupInfo(UserList.NONE);
            ul.setUserProfile("ULB*");
            assertCondition(verifyList(ul, new String[] { userNotInGroup_, userInGroup_, groupWithMembers_, groupWithNoMembers_ }, new String[0]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Verifies the user list.
     @param  userList  The user list.
     @param  expectedContents  A list of user names that must appear in the user list.
     @param  expectedNonContents  A list of user names that must not appear in the user list.
     @return  true if the list is correct, false otherwise.
     **/
    private boolean verifyList(RUserList userList, String[] expectedContents, String[] expectedNonContents) throws ResourceException
    {
        // Verify the system.
        if (userList.getSystem() != pwrSys_)
        {
            System.out.println("System object did not match expected.");
            return false;
        }

        // Open the list, if not already.
        if (!userList.isOpen()) userList.open();

        try
        {
            userList.waitForComplete();
            int length = (int)userList.getListLength();

            // Check the elements of the list.
            Vector<String> checkList = new Vector<String>(length);
            for (int i = 0; i < expectedContents.length; ++i)
            {
                checkList.addElement(expectedContents[i]);
            }

            for (int i = 0; i < length; ++i)
            {
                RUser u = (RUser)userList.resourceAt(i);
                String name = u.getName();
                if (checkList.contains(name))
                {
                    checkList.removeElement(name);
                }
                else
                {
                    for (int j = 0; j < expectedNonContents.length; ++j)
                    {
                        if (name.equals(expectedNonContents[j]))
                        {
                            System.out.println("List contained unexpected name: " + name + ".");
                            return false;
                        }
                    }
                }
            }

            if (checkList.size() > 0)
            {
                System.out.println("Expected name " + checkList.elementAt(0) + " was not found.");
                return false;
            }

            return true;
        }
        finally
        {
            userList.close();
        }
    }

    /**
     constructor() with 0 parms - Should work.
     **/
    public void Var100()
    {
        try
        {
            RUserList ul = new RUserList();
            assertCondition(ul.getSystem() == null && ul.getSelectionValue(RUserList.SELECTION_CRITERIA) == RUserList.ALL && ul.getSelectionValue(RUserList.GROUP_PROFILE) == RUserList.NONE);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 1 parm - Pass null for system.
     **/
    public void Var101()
    {
        try
        {
            RUserList ul = new RUserList(null);
            failed("Didn't throw exception"+ul);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 1 parm - Should work.
     **/
    public void Var102()
    {
        try
        {
            RUserList ul = new RUserList(pwrSys_);
            assertCondition(ul.getSystem() == pwrSys_ && ul.getSelectionValue(RUserList.SELECTION_CRITERIA) == RUserList.ALL && ul.getSelectionValue(RUserList.GROUP_PROFILE) == RUserList.NONE && verifyList(ul, new String[] { userNotInGroup_, userInGroup_, groupWithMembers_, groupWithNoMembers_ }, new String[0]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     serialization - Verify that the properties are set and verify that its usable.
     **/
    public void Var103()
    {
        try
        {
            RUserList ul1 = new RUserList(pwrSys_);
            ul1.setSelectionValue(RUserList.SELECTION_CRITERIA, RUserList.MEMBER);
            ul1.setSelectionValue(RUserList.GROUP_PROFILE, userInGroup_);
            RUserList ul2 = (RUserList)UserTest.serialize(ul1);
            assertCondition(ul2.getSystem().getSystemName().equals(pwrSys_.getSystemName()) && ul2.getSystem().getUserId().equals(pwrSys_.getUserId()) && ul2.getSelectionValue(RUserList.SELECTION_CRITERIA).equals(RUserList.MEMBER) && ul2.getSelectionValue(RUserList.GROUP_PROFILE).equals(userInGroup_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSystem() - Should return null when the system has not been set.
     **/
    public void Var104()
    {
        try
        {
            RUserList ul = new RUserList();
            assertCondition(ul.getSystem() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSystem() - Should return the system when the system has been set.
     **/
    public void Var105()
    {
        try
        {
            RUserList ul = new RUserList(systemObject_);
            assertCondition(ul.getSystem() == systemObject_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Should throw an exception if null is passed.
     **/
    public void Var106()
    {
        try
        {
            RUserList u = new RUserList(systemObject_);
            u.setSystem(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setSystem() - Set to an invalid system.  Should be reflected by getSystem(), since the validity is not checked here.
     **/
    public void Var107()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RUserList u = new RUserList(systemObject_);
            u.setSystem(bogus);
            assertCondition(u.getSystem().getSystemName().equals("bogus") && u.getSystem().getUserId().equalsIgnoreCase("bogus"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Set to a valid name.  Should be reflected by getSystem() and verify that it is used.
     **/
    public void Var108()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RUserList u = new RUserList(bogus);
            u.setSystem(pwrSys_);
            u.open();
            RUser user = (RUser)u.resourceAt(0);
            u.close();
            assertCondition(u.getSystem().getSystemName().equals(pwrSys_.getSystemName()) && u.getSystem().getUserId().equals(pwrSys_.getUserId()) && user != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Set to a valid name after the RUser object has made a connection.
     **/
    public void Var109()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.open();
            u.setSystem(systemObject_);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     setSystem() - Should fire a property change event.
     **/
    public void Var110()
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
     setSystem() - Should fire a vetoable change event.
     **/
    public void Var111()
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
     setSystem() - Should throw a PropertyVetoException if the change is vetoed.
     **/
    public void Var112()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUserList u = new RUserList(temp1);
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_(true);
            u.addVetoableChangeListener(vcl);
            u.setSystem(temp2);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.beans.PropertyVetoException");
        }
    }
}
