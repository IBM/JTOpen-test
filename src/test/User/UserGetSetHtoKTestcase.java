///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserGetSetHtoKTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.IllegalPathNameException;
import com.ibm.as400.access.User;

import test.Testcase;

/**
 Testcase UserGetSetTestcase.  This tests the get and set methods of the User class.
 **/
public class UserGetSetHtoKTestcase extends Testcase
{
    private UserSandbox sandbox_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "UGSHKT");
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
     <dl>
     <dt>Test:</dt><dd>Call User::getHighestSchedulingPriority() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var001()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int expectedValue = 3;
            int returnValue = u.getHighestSchedulingPriority();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHighestSchedulingPriority(int) with the valid parameters of the range 0-9.</dd>
     <dt>Result:</dt><dd>Verify User::getHighestSchedulingPriority() returns the values.</dd>
     </dl>
     **/
    public void Var002()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            for (int testValue = 0; testValue <= 9; ++testValue)
            {
                u.setHighestSchedulingPriority(testValue);
                int returnValue = u.getHighestSchedulingPriority();
                if (returnValue != testValue)
                {
                    failed("Incorrect value, expected: " + testValue + ", received: " + returnValue);
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHighestSchedulingPriority(int) with a not valid value parameter of -1.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var003()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = -1;
            try
            {
                u.setHighestSchedulingPriority(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0074 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHighestSchedulingPriority(int) with a not valid value parameter of 10.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var004()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 10;
            try
            {
                u.setHighestSchedulingPriority(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0074 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHighestSchedulingPriority(int) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var005()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            int testValue = 3;
	    try
	    {
                u.setHighestSchedulingPriority(testValue);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHighestSchedulingPriority(int) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var006()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            int testValue = 3;
	    try
	    {
                u.setHighestSchedulingPriority(testValue);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalStateException", "name: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getHomeDirectory() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var007()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String expectedValue = "/home/" + userName;
            String returnValue = u.getHomeDirectory();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHomeDirectory(String) with a valid parameter of *USRPRF.</dd>
     <dt>Result:</dt><dd>Verify User::getHomeDirectory() returns the correct string.</dd>
     </dl>
     **/
    public void Var008()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "*USRPRF";
            String expectedValue = "/home/" + userName;
            u.setHomeDirectory(testValue);
            String returnValue = u.getHomeDirectory();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHomeDirectory(String) with a valid parameter of "/qwerty".</dd>
     <dt>Result:</dt><dd>Verify User::getHomeDirectory() returns "/qwerty".</dd>
     </dl>
     **/
    public void Var009()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/qwerty";
            u.setHomeDirectory(testValue);
            String returnValue = u.getHomeDirectory();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHomeDirectory(String) with a valid parameter of "qwerty".</dd>
     <dt>Result:</dt><dd>Verify User::getHomeDirectory() returns "/qwerty".</dd>
     </dl>
     **/
    public void Var010()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "qwerty";
            String expectedValue = "/qwerty";
            u.setHomeDirectory(testValue);
            String returnValue = u.getHomeDirectory();
            // Note: When running natively, the "qwerty" is interpreted as a _relative_ pathname, so it might simply get appended to the current "working directory" value.
            assertCondition(returnValue.endsWith(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHomeDirectory(String) with a valid parameter of empty string "".</dd>
     <dt>Result:</dt><dd>Verify User::getHomeDirectory() returns "".</dd>
     </dl>
     **/
    public void Var011()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "";
            String expectedValue = "/home/" + userName;
            u.setHomeDirectory(testValue);
            String returnValue = u.getHomeDirectory();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHomeDirectory(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getHomeDirectory() returns "".</dd>
     </dl>
     **/
    public void Var012()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "*SAME";
            String expectedValue = "/home/" + userName;
            u.setHomeDirectory(testValue);
            String returnValue = u.getHomeDirectory();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHomeDirectory(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var013()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setHomeDirectory(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "homeDirectory");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHomeDirectory(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var014()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "123xyz";
	    try
	    {
                u.setHomeDirectory(testValue);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setHomeDirectory(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var015()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "123xyz";
	    try
	    {
                u.setHomeDirectory(testValue);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalStateException", "name: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getIASPNames() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var016()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] returnValue = u.getIASPNames();
            if (pwrSys_.getVRM() >= 0x00050100)
            {
                assertCondition(returnValue.length == 0);
            }
            else
            {
                assertCondition(returnValue == null);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getIASPStorageAllowed(String) before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var017()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int expectedValue = -2;
            int returnValue = u.getIASPStorageAllowed("MYIASP");
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getIASPStorageAllowed(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var018()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.getIASPStorageAllowed(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "iaspName");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getIASPStorageUsed(String) before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var019()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int expectedValue = -2;
            int returnValue = u.getIASPStorageUsed("MYIASP");
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getIASPStorageUsed(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var020()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.getIASPStorageUsed(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "iaspName");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getInitialMenu() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var021()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "/QSYS.LIB/%LIBL%.LIB/MAIN.MNU";
            String returnValue = u.getInitialMenu();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) with a valid parameter of *SIGNOFF.</dd>
     <dt>Result:</dt><dd>Verify User::getInitialMenu() returns *SIGNOFF.</dd>
     </dl>
     **/
    public void Var022()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SIGNOFF";
            u.setInitialMenu(testValue);
            String returnValue = u.getInitialMenu();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) with a valid parameter which is a path.</dd>
     <dt>Result:</dt><dd>Verify User::getInitialMenu() returns the path.</dd>
     </dl>
     **/
    public void Var023()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/CMDADD.MNU";
            u.setInitialMenu(testValue);
            String returnValue = u.getInitialMenu();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) with a valid parameter which is a mixed case path.</dd>
     <dt>Result:</dt><dd>Verify User::getInitialMenu() returns the path.</dd>
     </dl>
     **/
    public void Var024()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/qSyS.lIb/CmDaDd.MnU";
            String expectedValue = "/QSYS.LIB/CMDADD.MNU";
            u.setInitialMenu(testValue);
            String returnValue = u.getInitialMenu();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) with a valid parameter which is a path that does not exist.</dd>
     <dt>Result:</dt><dd>Verify User::getInitialMenu() returns the path.</dd>
     </dl>
     **/
    public void Var025()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/MYLIB.LIB/MYMENU.MNU";
            u.setInitialMenu(testValue);
            String returnValue = u.getInitialMenu();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getInitialMenu() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var026()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "/QSYS.LIB/%LIBL%.LIB/MAIN.MNU";
            u.setInitialMenu(testValue);
            String returnValue = u.getInitialMenu();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getInitialMenu() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var027()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "/QSYS.LIB/%LIBL%.LIB/MAIN.MNU";
            u.setInitialMenu(testValue);
            String returnValue = u.getInitialMenu();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var028()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setInitialMenu(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0078 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var029()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setInitialMenu(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0078 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var030()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QEZMAIN.PGM";
            try
            {
                u.setInitialMenu(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/QEZMAIN.PGM: ", IllegalPathNameException.OBJECT_TYPE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var031()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/ELEVENISBIG.LIB/MYPGM.MNU";
            try
            {
                u.setInitialMenu(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/ELEVENISBIG.LIB/MYPGM.MNU: ", IllegalPathNameException.LIBRARY_LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var032()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setInitialMenu(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "initialMenu");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var033()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*SIGNOFF";
	    try
	    {
                u.setInitialMenu(testValue);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialMenu(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var034()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*SIGNOFF";
	    try
	    {
                u.setInitialMenu(testValue);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalStateException", "name: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getInitialProgram() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var035()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*NONE";
            String returnValue = u.getInitialProgram();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getInitialProgram() returns *NONE.</dd>
     </dl>
     **/
    public void Var036()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NONE";
            u.setInitialProgram(testValue);
            String returnValue = u.getInitialProgram();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getInitialProgram() returns *NONE.</dd>
     </dl>
     **/
    public void Var037()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NONE";
            u.setInitialProgram(testValue);
            String returnValue = u.getInitialProgram();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) with a valid parameter which is a path.</dd>
     <dt>Result:</dt><dd>Verify User::getInitialProgram() returns the path.</dd>
     </dl>
     **/
    public void Var038()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QCMD.PGM";
            u.setInitialProgram(testValue);
            String returnValue = u.getInitialProgram();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) with a valid parameter which is a mixed case path.</dd>
     <dt>Result:</dt><dd>Verify User::getInitialProgram() returns the path.</dd>
     </dl>
     **/
    public void Var039()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/Qsys.Lib/Qcmd.Pgm";
            String expectedValue = "/QSYS.LIB/QCMD.PGM";
            u.setInitialProgram(testValue);
            String returnValue = u.getInitialProgram();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) with a valid parameter which is a path that does not exist.</dd>
     <dt>Result:</dt><dd>Verify User::getInitialProgram() returns the path.</dd>
     </dl>
     **/
    public void Var040()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/MYLIB.LIB/MYPGM.PGM";
            u.setInitialProgram(testValue);
            String returnValue = u.getInitialProgram();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getInitialProgram() returns *NONE.</dd>
     </dl>
     **/
    public void Var041()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*NONE";
            u.setInitialProgram(testValue);
            String returnValue = u.getInitialProgram();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getInitialProgram() returns *NONE.</dd>
     </dl>
     **/
    public void Var042()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*NONE";
            u.setInitialProgram(testValue);
            String returnValue = u.getInitialProgram();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var043()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setInitialProgram(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0078 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var044()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setInitialProgram(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0078 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var045()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QEZMAIN.MNU";
            try
            {
                u.setInitialProgram(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/QEZMAIN.MNU: ", IllegalPathNameException.OBJECT_TYPE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var046()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/ELEVENISBIG.LIB/MYPGM.PGM";
            try
            {
                u.setInitialProgram(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/ELEVENISBIG.LIB/MYPGM.PGM: ", IllegalPathNameException.LIBRARY_LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var047()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setInitialProgram(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "initialProgram");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var048()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*NONE";
	    try
	    {
                u.setInitialProgram(testValue);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setInitialProgram(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var049()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*NONE";
	    try
	    {
                u.setInitialProgram(testValue);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalStateException", "name: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getJobDescription() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var050()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "/QSYS.LIB/QGPL.LIB/QDFTJOBD.JOBD";
            String returnValue = u.getJobDescription();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setJobDescription(String) with a valid parameter which is a path.</dd>
     <dt>Result:</dt><dd>Verify User::getJobDescription() returns the path.</dd>
     </dl>
     **/
    public void Var051()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QCTL.JOBD";
            u.setJobDescription(testValue);
            String returnValue = u.getJobDescription();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setJobDescription(String) with a valid parameter which is a mixed case path.</dd>
     <dt>Result:</dt><dd>Verify User::getJobDescription() returns the path.</dd>
     </dl>
     **/
    public void Var052()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSyS.LiB/qCTl.jOBd";
            String expectedValue = "/QSYS.LIB/QCTL.JOBD";
            u.setJobDescription(testValue);
            String returnValue = u.getJobDescription();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setJobDescription(String) with a valid parameter which is a path that does not exist.</dd>
     <dt>Result:</dt><dd>Verify User::getJobDescription() returns the path.</dd>
     </dl>
     **/
    public void Var053()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/MYLIB.LIB/MYJOBD.JOBD";
            u.setJobDescription(testValue);
            String returnValue = u.getJobDescription();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setJobDescription(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getJobDescription() returns "/QSYS.LIB/QGPL.LIB/QDFTJOBD.JOBD".</dd>
     </dl>
     **/
    public void Var054()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "/QSYS.LIB/QGPL.LIB/QDFTJOBD.JOBD";
            u.setJobDescription(testValue);
            String returnValue = u.getJobDescription();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setJobDescription(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getJobDescription() returns "/QSYS.LIB/QGPL.LIB/QDFTJOBD.JOBD".</dd>
     </dl>
     **/
    public void Var055()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "/QSYS.LIB/QGPL.LIB/QDFTJOBD.JOBD";
            u.setJobDescription(testValue);
            String returnValue = u.getJobDescription();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setJobDescription(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var056()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setJobDescription(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0078 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setJobDescription(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var057()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setJobDescription(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0078 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setJobDescription(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var058()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QEZMAIN.MNU";
            try
            {
                u.setJobDescription(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/QEZMAIN.MNU: ", IllegalPathNameException.OBJECT_TYPE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setJobDescription(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var059()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/ELEVENISBIG.LIB/MYJOBD.JOBD";
            try
            {
                u.setJobDescription(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/ELEVENISBIG.LIB/MYJOBD.JOBD: ", IllegalPathNameException.LIBRARY_LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setJobDescription(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var060()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setJobDescription(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "jobDescription");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setJobDescription(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var061()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "/QSYS.LIB/QGPL.LIB/QDFTJOBD.JOBD";
	    try
	    {
                u.setJobDescription(testValue);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setJobDescription(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var062()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "/QSYS.LIB/QGPL.LIB/QDFTJOBD.JOBD";
	    try
	    {
                u.setJobDescription(testValue);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalStateException", "name: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getKeyboardBuffering() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var063()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*SYSVAL";
            String returnValue = u.getKeyboardBuffering();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setKeyboardBuffering(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getKeyboardBuffering() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var064()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            u.setKeyboardBuffering(testValue);
            String returnValue = u.getKeyboardBuffering();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setKeyboardBuffering(String) with a valid parameter of *NO.</dd>
     <dt>Result:</dt><dd>Verify User::getKeyboardBuffering() returns *NO.</dd>
     </dl>
     **/
    public void Var065()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NO";
            u.setKeyboardBuffering(testValue);
            String returnValue = u.getKeyboardBuffering();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setKeyboardBuffering(String) with a valid parameter of *YES.</dd>
     <dt>Result:</dt><dd>Verify User::getKeyboardBuffering() returns *YES.</dd>
     </dl>
     **/
    public void Var066()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*YES";
            u.setKeyboardBuffering(testValue);
            String returnValue = u.getKeyboardBuffering();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setKeyboardBuffering(String) with a valid parameter of *TYPEAHEAD.</dd>
     <dt>Result:</dt><dd>Verify User::getKeyboardBuffering() returns *TYPEAHEAD.</dd>
     </dl>
     **/
    public void Var067()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*TYPEAHEAD";
            u.setKeyboardBuffering(testValue);
            String returnValue = u.getKeyboardBuffering();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setKeyboardBuffering(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getKeyboardBuffering() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var068()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*SYSVAL";
            u.setKeyboardBuffering(testValue);
            String returnValue = u.getKeyboardBuffering();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setKeyboardBuffering(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getKeyboardBuffering() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var069()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*SYSVAL";
            u.setKeyboardBuffering(testValue);
            String returnValue = u.getKeyboardBuffering();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setKeyboardBuffering(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var070()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setKeyboardBuffering(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setKeyboardBuffering(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var071()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setKeyboardBuffering(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setKeyboardBuffering(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var072()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setKeyboardBuffering(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "keyboardBuffering");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setKeyboardBuffering(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var073()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*SYSVAL";
	    try
	    {
                u.setKeyboardBuffering(testValue);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setKeyboardBuffering(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var074()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*SYSVAL";
	    try
	    {
                u.setKeyboardBuffering(testValue);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalStateException", "name: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }
}
