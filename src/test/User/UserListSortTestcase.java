///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserListSortTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import java.util.Date;

import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;
import test.UserTest;

import com.ibm.as400.resource.RUserList;

/**
 Testcase UserListSortTestcase.  This tests the following methods of the UserList class, some inherited from BufferedResourceList:
 <ul>
 <li>getSortMetaData()
 <li>getSortMetaData()
 <li>getSortOrder()
 <li>getSortValue()
 <li>setSortOrder()
 <li>setSortValue()
 </ul>
 **/
@SuppressWarnings("deprecation")
public class UserListSortTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserListSortTestcase";
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
        sandbox_ = new UserSandbox(pwrSys_, "ULSO", UserTest.COLLECTION.substring(UserTest.COLLECTION.length() - 1));
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
     getSortMetaData() with 0 parameters - Verify that the array contains no sorts.
     **/
    public void Var001()
    {
        try
        {
            RUserList u = new RUserList();
            ResourceMetaData[] smd = u.getSortMetaData();
            assertCondition(smd.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSortMetaData() with 1 parameter - Pass null.
     **/
    public void Var002()
    {
        try
        {
            RUserList u = new RUserList();
            ResourceMetaData smd = u.getSortMetaData(null);
            failed("Didn't throw exception"+smd);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     getSortMetaData() with 1 parameter - Pass an invalid attribute ID.
     **/
    public void Var003()
    {
        try
        {
            RUserList u = new RUserList();
            ResourceMetaData smd = u.getSortMetaData(new Date());
            failed("Didn't throw exception"+smd);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     getSortOrder() - Pass null.
     **/
    public void Var004()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.getSortOrder(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     getSortOrder() - Pass an invalid sort ID.
     **/
    public void Var005()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.getSortOrder("Yo");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     getSortValue() - Try it.
     **/
    public void Var006()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            Object[] sortValue = u.getSortValue();
            assertCondition(sortValue.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSortOrder() - Pass null for the sort ID.
     **/
    public void Var007()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setSortOrder(null, true);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setSortOrder() - Pass an invalid sort ID.
     **/
    public void Var008()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setSortOrder(u, false);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setSortValue() - Pass null.
     **/
    public void Var009()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setSortValue(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setSortOrder() - Pass an invalid sort ID.
     **/
    public void Var010()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setSortValue(new Object[] { "Yo" } );
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setSortOrder() - Pass an empty array.
     **/
    public void Var011()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setSortValue(new Object[0]);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            for (int i = 0; i < length; ++i) u.resourceAt(i);
            assertCondition(length > 20);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
