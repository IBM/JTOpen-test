///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserGroupTestcase.java
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

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.User;
import com.ibm.as400.access.UserGroup;

import test.Testcase;
import test.UserTest;

/**
 Testcase UserGroupTestcase.  This tests the following methods of the User class:
 <ul>
 <li>constructors
 <li>getMembers()
 </ul>
 **/
public class UserGroupTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserGroupTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
    private UserSandbox sandbox_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "UG", UserTest.COLLECTION.substring(UserTest.COLLECTION.length() - 1));
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        sandbox_.cleanup();
    }

    private boolean testEnumeration(Enumeration<User> enumeration, String[] expectedGroupAndUsers, int expected)
    {
        // Build a Vector with all expected users.  Remove them as they are found.
        // They should all be gone when we are done!
        Vector<String> expectedUsersV = new Vector<String>();
        for (int i = 1; i <= expected; ++i)
        {
            expectedUsersV.addElement(expectedGroupAndUsers[i]);
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
            else
            {
                output_.println("nextElement() returned user " + userName + " which was not expected.");
                return false;
            }
        }

        // Verify that nextElement() throws an exception when the enumeration is empty.
        try
        {
            enumeration.nextElement();
            output_.println("nextElement() did not throw exception when enumeration is done.");
            return false;
        }
        catch (NoSuchElementException e)
        {
        }

        // If there are still elements in the vector, than something that we expected did not come up in the enumeration.
        if (expectedUsersV.size() > 0)
        {
            output_.println(expectedUsersV.size() + " expected users were not included in the enumeration.");
            return false;
        }

        return true;
    }

    /**
     constructor() with 2 parms - Pass null for system.
     **/
    public void Var001()
    {
        try
        {
            UserGroup u = new UserGroup(null, systemObject_.getUserId());
            failed("Didn't throw exception"+u);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 2 parms - Pass null for name.
     **/
    public void Var002()
    {
        try
        {
            UserGroup u = new UserGroup(systemObject_, null);
            failed("Didn't throw exception"+u);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 2 parms - Pass invalid values.  This should work, because the constructor does not check the validity.
     **/
    public void Var003()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus".toCharArray());
            UserGroup u = new UserGroup(bogus, "BadUser");
            assertCondition(u.getSystem() == bogus && u.getName().equals("BADUSER"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 2 parms - Pass valid values.  Verify that the system and name are used.
     **/
    public void Var004()
    {
        try
        {
            String userName = sandbox_.createGroup();
            UserGroup u = new UserGroup(pwrSys_, userName);
            String textDescription = u.getDescription();
            assertCondition(u.getSystem() == pwrSys_ && u.getName().equals(userName) && textDescription.startsWith(userName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getMembers() - On a bad system.
     **/
    public void Var005()
    {
        try
        {
            AS400 system = new AS400("dontnameyoursystemthis", "blah", "blah".toCharArray());
            system.setGuiAvailable(false);
            UserGroup u = new UserGroup(system, "joe");
            Enumeration<?> enumeration = u.getMembers();
            failed("Didn't throw exception"+enumeration);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "UnknownHostException");
        }
    }

    /**
     getMembers() - On a user that does not exist.
     **/
    public void Var006()
    {
        try
        {
            UserGroup u = new UserGroup(pwrSys_, "DISCMAN");
            Enumeration<?> enumeration = u.getMembers();
            failed("Didn't throw exception"+enumeration);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     getMembers() - On a user that is not a group.
     **/
    public void Var007()
    {
        try
        {
            String userName = sandbox_.createUser();
            UserGroup u = new UserGroup(pwrSys_, userName);
            Enumeration<?> enumeration = u.getMembers();
            failed("Didn't throw exception"+enumeration);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     getMembers() - On a group that has no members.
     **/
    public void Var008()
    {
        try
        {
            String[] userAndGroups = sandbox_.createGroupAndUsers(0);
            UserGroup u = new UserGroup(pwrSys_, userAndGroups[0]);
            @SuppressWarnings("unchecked")
            Enumeration<User> enumeration = u.getMembers();
            assertCondition(testEnumeration(enumeration, userAndGroups, 0));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getMembers() - On a group that has 1 member.
     **/
    public void Var009()
    {
        try
        {
            String[] userAndGroups = sandbox_.createGroupAndUsers(1);
            UserGroup u = new UserGroup(pwrSys_, userAndGroups[0]);
            @SuppressWarnings("unchecked")
            Enumeration<User> enumeration = u.getMembers();
            assertCondition(testEnumeration(enumeration, userAndGroups, 1));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getMembers() - On a group that has 2 members.
     **/
    public void Var010()
    {
        try
        {
            String[] userAndGroups = sandbox_.createGroupAndUsers(2);
            UserGroup u = new UserGroup(pwrSys_, userAndGroups[0]);
            @SuppressWarnings("unchecked")
            Enumeration<User> enumeration = u.getMembers();
            assertCondition(testEnumeration(enumeration, userAndGroups, 2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getMembers() - On a group that has 10 members.
     **/
    public void Var011()
    {
        try
        {
            String[] userAndGroups = sandbox_.createGroupAndUsers(10);
            UserGroup u = new UserGroup(pwrSys_, userAndGroups[0]);
            @SuppressWarnings("unchecked")
            Enumeration<User> enumeration = u.getMembers();
            assertCondition(testEnumeration(enumeration, userAndGroups, 10));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
