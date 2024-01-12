///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserListBackwardsCompatibilityTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

import com.ibm.as400.access.User;
import com.ibm.as400.access.UserList;

import test.Testcase;
import test.UserSandbox;

/**
 Testcase UserListBackwardsCompatibilityTestcase.  This tests the following methods of the UserList class:
 <ul>
 <li>getLength()
 <li>getUsers()
 </ul>
 **/
public class UserListBackwardsCompatibilityTestcase extends Testcase
{
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
        sandbox_ = new UserSandbox(pwrSys_, "ULBCT");

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
    protected void cleanup() throws Exception
    {
        sandbox_.cleanup();
    }

    /**
     Verifies the user list enumeration.
     @param  userList  The user list.
     @param  expectedContents  A list of user names that must appear in the user list.
     @param  expectedNonContents  A list of user names that must not appear in the user list.
     @return  true if the list is correct, false otherwise.
     **/
    private boolean testEnumeration(Enumeration enumeration, String[] expectedContents, String[] expectedNonContents)
    {
        // Build a Vector with all expected users.  Remove them as they are found.
        // They should all be gone when we are done!
        Vector expectedUsersV = new Vector();
        for (int i = 0; i < expectedContents.length; ++i)
        {
            expectedUsersV.addElement(expectedContents[i]);
        }

        // Build a Vector with all non-expected users.
        Vector expectedNonContentsV = new Vector();
        for (int i = 0; i < expectedNonContents.length; ++i)
        {
            expectedNonContentsV.addElement(expectedNonContents[i]);
        }

        // Loop through each element in the enumeration.
        while (enumeration.hasMoreElements())
        {
            User user = (User)enumeration.nextElement();
            String userName = user.getName();

            if (expectedUsersV.contains(userName))
            {
                expectedUsersV.removeElement(userName);
            }

            if (expectedNonContentsV.contains(userName))
            {
                System.out.println("nextElement() returned unexpected element " + userName + ".");
                return false;
            }
        }

        // Verify that nextElement() throws an exception when the enumeration is empty.
        try
        {
            enumeration.nextElement();
            System.out.println("nextElement() did not throw exception when enumeration is done.");
            return false;
        }
        catch (NoSuchElementException e)
        {
        }

        // If there are still elements in the vector, than something that we expected did not come up in the enumeration.
        if (expectedUsersV.size() > 0)
        {
            System.out.println(expectedUsersV.size() + " expected users were not included in the enumeration.");
            return false;
        }

        return true;
    }

    /**
     getLength() - Call when the list has never been opened, and there is not enough information to open it implicitly.
     **/
    public void Var001()
    {
        try
        {
            UserList u = new UserList();
            int length = u.getLength();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     getLength() - Call when the list has never been opened.
     **/
    public void Var002()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            int length = u.getLength();
            assertCondition(length > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getLength() - Call when the list has opened, but no users have been read.
     **/
    public void Var003()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            u.getUsers();
            int length = u.getLength();
            assertCondition(length > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getLength() - Call when the list has opened, and several users have been read.
     **/
    public void Var004()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            Enumeration enumeration = u.getUsers();
            enumeration.nextElement();
            enumeration.nextElement();
            enumeration.nextElement();
            enumeration.nextElement();
            int length = u.getLength();
            assertCondition(length > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getLength() - Call when the list has opened, and all users have been read.
     **/
    public void Var005()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            Enumeration enumeration = u.getUsers();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement();
            }
            int length = u.getLength();
            assertCondition(length > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getLength() - Call when the list has opened and closed.
     **/
    public void Var006()
    {
        try
        {
            UserList u = new UserList(pwrSys_);
            Enumeration enumeration = u.getUsers();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement();
            }
            u.getUsers();
            int length = u.getLength();
            assertCondition(length > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUsers() - When there is not enough information specified.
     **/
    public void Var007()
    {
        try
        {
            UserList ul = new UserList();
            ul.getUsers();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     getUsers() - Pass valid values with user info = ALL, group info = NONE.
     **/
    public void Var008()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.ALL);
            ul.setGroupInfo(UserList.NONE);
            assertCondition(testEnumeration(ul.getUsers(), new String[] { userNotInGroup_, userInGroup_, groupWithMembers_, groupWithNoMembers_ }, new String[0]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUsers() - Pass valid values with user info = ALL, group info = NOGROUP.
     **/
    public void Var009()
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
     getUsers() - Pass valid values with user info = ALL, group info = a group profile.
     **/
    public void Var010()
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
     getUsers() - Pass valid values with user info = USER, group info = NONE.
     **/
    public void Var011()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.USER);
            ul.setGroupInfo(UserList.NONE);
            assertCondition(testEnumeration(ul.getUsers(), new String[] { userNotInGroup_, userInGroup_ }, new String[] { groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUsers() - Pass valid values with user info = USER, group info = NOGROUP.
     **/
    public void Var012()
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
     getUsers() - Pass valid values with user info = USER, group info = a group profile.
     **/
    public void Var013()
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
     getUsers() - Pass valid values with user info = GROUP, group info = NONE.
     **/
    public void Var014()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.GROUP);
            ul.setGroupInfo(UserList.NONE);
            assertCondition(testEnumeration(ul.getUsers(), new String[] { groupWithMembers_, groupWithNoMembers_ } , new String[] { userNotInGroup_, userInGroup_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUsers() - Pass valid values with user info = GROUP, group info = NOGROUP.
     **/
    public void Var015()
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
     getUsers() - Pass valid values with user info = GROUP, group info = a group profile.
     **/
    public void Var016()
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
     getUsers() - Pass valid values with user info = MEMBER, group info = NONE.
     **/
    public void Var017()
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
     getUsers() - Pass valid values with user info = MEMBER, group info = NOGROUP.
     **/
    public void Var018()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.MEMBER);
            ul.setGroupInfo(UserList.NOGROUP);
            assertCondition(testEnumeration(ul.getUsers(), new String[] { userNotInGroup_ }, new String[] { userInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUsers() - Pass valid values with user info = MEMBER, group info = group profile which does not exist.
     **/
    public void Var019()
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
     getUsers() - Pass valid values with user info = MEMBER, group info = group profile which is really a user profile, not a group profile.
     **/
    public void Var020()
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
     getUsers() - Pass valid values with user info = MEMBER, group info = group profile which has members.
     **/
    public void Var021()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.MEMBER);
            ul.setGroupInfo(groupWithMembers_);
            assertCondition(testEnumeration(ul.getUsers(), new String[] { userInGroup_ }, new String[] { userNotInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUsers() - Pass valid values with user info = MEMBER, group info = group profile which has no members.
     **/
    public void Var022()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.MEMBER);
            ul.setGroupInfo(groupWithNoMembers_);
            assertCondition(testEnumeration(ul.getUsers(), new String[0], new String[] { userNotInGroup_, userInGroup_, groupWithNoMembers_, groupWithMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUsers() - Pass valid values with user info = MEMBER, group info = group profile specified as lower case name.
     **/
    public void Var023()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.MEMBER);
            ul.setGroupInfo(groupWithMembers_.toLowerCase());
            assertCondition(testEnumeration(ul.getUsers(), new String[] { userInGroup_ }, new String[] { userNotInGroup_, groupWithMembers_, groupWithNoMembers_ } ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUsers() - When the list has already been opened, but nothing read.
     **/
    public void Var024()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.ALL);
            ul.setGroupInfo(UserList.NONE);
            ul.getUsers();
            assertCondition(testEnumeration(ul.getUsers(), new String[] { userNotInGroup_, userInGroup_, groupWithMembers_, groupWithNoMembers_ }, new String[0]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUsers() - When the list has already been opened, and all resources read.
     **/
    public void Var025()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.ALL);
            ul.setGroupInfo(UserList.NONE);
            Enumeration enumeration = ul.getUsers();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement();
            }
            assertCondition(testEnumeration(ul.getUsers(), new String[] { userNotInGroup_, userInGroup_, groupWithMembers_, groupWithNoMembers_ }, new String[0]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getUsers() - When the list has already been opened and closed.
     **/
    public void Var026()
    {
        try
        {
            UserList ul = new UserList(pwrSys_);
            ul.setUserInfo(UserList.ALL);
            ul.setGroupInfo(UserList.NONE);
            Enumeration enumeration = ul.getUsers();
            while (enumeration.hasMoreElements())
            {
                enumeration.nextElement();
            }
            assertCondition(testEnumeration(ul.getUsers(), new String[] { userNotInGroup_, userInGroup_, groupWithMembers_, groupWithNoMembers_ }, new String[0]));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
