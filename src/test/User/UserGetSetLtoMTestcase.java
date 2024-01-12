///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserGetSetLtoMTestcase.java
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

import test.JDTestDriver;
import test.Testcase;
import test.UserSandbox;

/**
 Testcase UserGetSetTestcase.  This tests the get and set methods of the User class.
 **/
public class UserGetSetLtoMTestcase extends Testcase
{
    private UserSandbox sandbox_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "UGSLMT");
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
     <dt>Test:</dt><dd>Call User::getLanguageID() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var001()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*SYSVAL";
            String returnValue = u.getLanguageID();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLanguageID(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getLanguageID() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var002()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            u.setLanguageID(testValue);
            String returnValue = u.getLanguageID();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLanguageID(String) with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify User::getLanguageID() returns the parameter.</dd>
     </dl>
     **/
    public void Var003()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "ROM";
            u.setLanguageID(testValue);
            String returnValue = u.getLanguageID();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLanguageID(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getLanguageID() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var004()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*SYSVAL";
            u.setLanguageID(testValue);
            String returnValue = u.getLanguageID();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLanguageID(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getLanguageID() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var005()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*SYSVAL";
            u.setLanguageID(testValue);
            String returnValue = u.getLanguageID();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLanguageID(String) with a not valid star value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var006()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setLanguageID(testValue);
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
     <dt>Test:</dt><dd>Call User::setLanguageID(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var007()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "JIT";
            try
            {
                u.setLanguageID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF3FC0 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLanguageID(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var008()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "ThisNameIsWayTooLong";
            try
            {
                u.setLanguageID(testValue);
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
     <dt>Test:</dt><dd>Call User::setLanguageID(String) with a not valid over length with spaces parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var009()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "This Name Is Way Too Long";
            try
            {
                u.setLanguageID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0047 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLanguageID(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var010()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setLanguageID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "languageID");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLanguageID(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var011()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*SYSVAL";
	    try
	    {
                u.setLanguageID(testValue);
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
     <dt>Test:</dt><dd>Call User::setLanguageID(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var012()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*SYSVAL";
	    try
	    {
                u.setLanguageID(testValue);
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
     <dt>Test:</dt><dd>Call User::getLimitCapabilities() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var013()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*NO";
            String returnValue = u.getLimitCapabilities();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitCapabilities(String) with a valid parameter of *NO.</dd>
     <dt>Result:</dt><dd>Verify User::getLimitCapabilities() returns *NO.</dd>
     </dl>
     **/
    public void Var014()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NO";
            u.setLimitCapabilities(testValue);
            String returnValue = u.getLimitCapabilities();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitCapabilities(String) with a valid parameter of *YES.</dd>
     <dt>Result:</dt><dd>Verify User::getLimitCapabilities() returns *YES.</dd>
     </dl>
     **/
    public void Var015()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*YES";
            u.setLimitCapabilities(testValue);
            String returnValue = u.getLimitCapabilities();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitCapabilities(String) with a valid parameter of *PARTIAL.</dd>
     <dt>Result:</dt><dd>Verify User::getLimitCapabilities() returns *PARTIAL.</dd>
     </dl>
     **/
    public void Var016()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*PARTIAL";
            u.setLimitCapabilities(testValue);
            String returnValue = u.getLimitCapabilities();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitCapabilities(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getLimitCapabilities() returns *NO.</dd>
     </dl>
     **/
    public void Var017()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*NO";
            u.setLimitCapabilities(testValue);
            String returnValue = u.getLimitCapabilities();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitCapabilities(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getLimitCapabilities() returns *NO.</dd>
     </dl>
     **/
    public void Var018()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*NO";
            u.setLimitCapabilities(testValue);
            String returnValue = u.getLimitCapabilities();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitCapabilities(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var019()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setLimitCapabilities(testValue);
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
     <dt>Test:</dt><dd>Call User::setLimitCapabilities(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var020()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setLimitCapabilities(testValue);
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
     <dt>Test:</dt><dd>Call User::setLimitCapabilities(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var021()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setLimitCapabilities(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "limitCapabilities");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitCapabilities(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var022()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*NO";
	    try
	    {
                u.setLimitCapabilities(testValue);
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
     <dt>Test:</dt><dd>Call User::setLimitCapabilities(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var023()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*NO";
	    try
	    {
                u.setLimitCapabilities(testValue);
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
     <dt>Test:</dt><dd>Call User::getLimitDeviceSessions() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var024()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*SYSVAL";
            String returnValue = u.getLimitDeviceSessions();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var025()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            u.setLimitDeviceSessions(testValue);
            String returnValue = u.getLimitDeviceSessions();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a valid parameter of *NO.</dd>
     <dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns *NO.</dd>
     </dl>
     **/
    public void Var026()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NO";
            u.setLimitDeviceSessions(testValue);
            String returnValue = u.getLimitDeviceSessions();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a valid parameter of *YES.</dd>
     <dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns *YES.</dd>
     </dl>
     **/
    public void Var027()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*YES";
            u.setLimitDeviceSessions(testValue);
            String returnValue = u.getLimitDeviceSessions();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var028()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*SYSVAL";
            u.setLimitDeviceSessions(testValue);
            String returnValue = u.getLimitDeviceSessions();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var029()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*SYSVAL";
            u.setLimitDeviceSessions(testValue);
            String returnValue = u.getLimitDeviceSessions();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var030()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setLimitDeviceSessions(testValue);
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
     <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var031()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setLimitDeviceSessions(testValue);
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
     <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a null parameter.</dd>
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
                u.setLimitDeviceSessions(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "limitDeviceSessions");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var033()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*SYSVAL";
	    try
	    {
                u.setLimitDeviceSessions(testValue);
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
     <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var034()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*SYSVAL";
	    try
	    {
                u.setLimitDeviceSessions(testValue);
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

    // Convenience method to check equality of String arrays.
    private boolean checkArray(String[] returnValue, String[] expectedValue)
    {
        if (returnValue.length != expectedValue.length) return false;
        for (int i = 0; i < returnValue.length; ++i)
        {
            if (!returnValue[i].equals(expectedValue[i])) return false;
        }
        return true;
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getLocaleJobAttributes() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var035()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] expectedValue = new String[] { "*SYSVAL" };
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var036()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SYSVAL" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns *NONE.</dd>
     </dl>
     **/
    public void Var037()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*NONE" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of *CCSID.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns *CCSID.</dd>
     </dl>
     **/
    public void Var038()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*CCSID" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of *DATFMT.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns *DATFMT.</dd>
     </dl>
     **/
    public void Var039()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DATFMT" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of *DATSEP.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns *DATSEP.</dd>
     </dl>
     **/
    public void Var040()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DATSEP" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of *DECFMT.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns *DECFMT.</dd>
     </dl>
     **/
    public void Var041()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DECFMT" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of *SRTSEQ.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns *SRTSEQ.</dd>
     </dl>
     **/
    public void Var042()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SRTSEQ" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of *TIMSEP.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns *TIMSEP.</dd>
     </dl>
     **/
    public void Var043()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*TIMSEP" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of length 2.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns the values.</dd>
     </dl>
     **/
    public void Var044()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DATFMT", "*CCSID" };
            String[] expectedValue = new String[] { "*CCSID", "*DATFMT" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of length 3.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns the values.</dd>
     </dl>
     **/
    public void Var045()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DATSEP", "*DECFMT", "*SRTSEQ" };
            String[] expectedValue = new String[] { "*DATSEP", "*SRTSEQ", "*DECFMT" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of length 4.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns the values.</dd>
     </dl>
     **/
    public void Var046()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*TIMSEP", "*CCSID", "*DATFMT", "*DATSEP" };
            String[] expectedValue = new String[] { "*CCSID", "*DATFMT", "*DATSEP", "*TIMSEP" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of length 4 with a duplicate.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns the 3 unique values.</dd>
     </dl>
     **/
    public void Var047()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*TIMSEP", "*DATFMT", "*DATFMT", "*DATSEP" };
            String[] expectedValue = new String[] { "*DATFMT", "*DATSEP", "*TIMSEP" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of length 5.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns the values.</dd>
     </dl>
     **/
    public void Var048()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DECFMT", "*SRTSEQ", "*TIMSEP", "*CCSID", "*DATFMT" };
            String[] expectedValue = new String[] { "*CCSID", "*DATFMT", "*SRTSEQ", "*TIMSEP", "*DECFMT" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a valid parameter of length 6.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns the values.</dd>
     </dl>
     **/
    public void Var049()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DATSEP", "*DECFMT", "*SRTSEQ", "*TIMSEP", "*CCSID", "*DATFMT" };
            String[] expectedValue = new String[] { "*CCSID", "*DATFMT", "*DATSEP", "*SRTSEQ", "*TIMSEP", "*DECFMT" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a zero length array parameter.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns *NONE.</dd>
     </dl>
     **/
    public void Var050()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[0];
            String[] expectedValue = new String[] { "*NONE" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var051()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "" };
            String[] expectedValue = new String[] { "*SYSVAL" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getLocaleJobAttributes() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var052()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SAME" };
            String[] expectedValue = new String[] { "*SYSVAL" };
            u.setLocaleJobAttributes(testValue);
            String[] returnValue = u.getLocaleJobAttributes();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a not valid value parameter of *NONE and another value.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var053()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*NONE", "*DATFMT" };
            try
            {
                u.setLocaleJobAttributes(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0094 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a not valid value parameter of *SYSVAL and another value.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var054()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SYSVAL", "*SRTSEQ" };
            try
            {
                u.setLocaleJobAttributes(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0094 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var055()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*USER" };
            try
            {
                u.setLocaleJobAttributes(testValue);
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
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a not valid value parameter as an element of the array.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var056()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DECFMT", "*SRTSEQ", "*USER", "*CCSID", "*DATFMT" };
            try
            {
                u.setLocaleJobAttributes(testValue);
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
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var057()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*INBETWEEEN" };
            try
            {
                u.setLocaleJobAttributes(testValue);
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
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var058()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = null;
            try
            {
                u.setLocaleJobAttributes(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "localeJobAttributes");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with an array parameter that is just a null value.</dd>
     <dt>Result:</dt><dd>Verify that a AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var059()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = new String[] { null };
            try
            {
                u.setLocaleJobAttributes(testValue);
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
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) with an array parameter that contains a null element.</dd>
     <dt>Result:</dt><dd>Verify that a AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var060()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = new String[] { "*DECFMT", "*SRTSEQ", null, "*CCSID", "*DATFMT" };
            try
            {
                u.setLocaleJobAttributes(testValue);
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
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var061()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String[] testValue = new String[] { "*SYSVAL" };
	    try
	    {
                u.setLocaleJobAttributes(testValue);
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
     <dt>Test:</dt><dd>Call User::setLocaleJobAttributes(String[]) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var062()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String[] testValue = new String[] { "*SYSVAL" };
	    try
	    {
                u.setLocaleJobAttributes(testValue);
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
     <dt>Test:</dt><dd>Call User::getLocalePathName() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var063()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String expectedValue = "*SYSVAL";
            String returnValue = u.getLocalePathName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getLocalePathName() returns the correct string.</dd>
     </dl>
     **/
    public void Var064()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "*SYSVAL";
            u.setLocalePathName(testValue);
            String returnValue = u.getLocalePathName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getLocalePathName() returns the correct string.</dd>
     </dl>
     **/
    public void Var065()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "*NONE";
            u.setLocalePathName(testValue);
            String returnValue = u.getLocalePathName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) with a valid parameter of *C.</dd>
     <dt>Result:</dt><dd>Verify User::getLocalePathName() returns the correct string.</dd>
     </dl>
     **/
    public void Var066()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "*C";
            u.setLocalePathName(testValue);
            String returnValue = u.getLocalePathName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) with a valid parameter of *POSIX.</dd>
     <dt>Result:</dt><dd>Verify User::getLocalePathName() returns the correct string.</dd>
     </dl>
     **/
    public void Var067()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "*POSIX";
            u.setLocalePathName(testValue);
            String returnValue = u.getLocalePathName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) with a valid parameter of "/qwerty".</dd>
     <dt>Result:</dt><dd>Verify User::getLocalePathName() returns "/qwerty".</dd>
     </dl>
     **/
    public void Var068()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/qwerty";
            u.setLocalePathName(testValue);
            String returnValue = u.getLocalePathName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) with a valid parameter of "qwerty".</dd>
     <dt>Result:</dt><dd>Verify User::getLocalePathName() returns "/qwerty".</dd>
     </dl>
     **/
    public void Var069()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/qwerty";
            String expectedValue = "/qwerty";
            u.setLocalePathName(testValue);
            String returnValue = u.getLocalePathName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) with a valid parameter of empty string "".</dd>
     <dt>Result:</dt><dd>Verify User::getLocalePathName() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var070()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "";
            String expectedValue = "*SYSVAL";
            u.setLocalePathName(testValue);
            String returnValue = u.getLocalePathName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getLocalePathName() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var071()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "*SAME";
            String expectedValue = "*SYSVAL";
            u.setLocalePathName(testValue);
            String returnValue = u.getLocalePathName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) with a null parameter.</dd>
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
                u.setLocalePathName(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "localePathName");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) with a not valid star value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var073()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setLocalePathName(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0186 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var074()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setLocalePathName(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0186 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var075()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*SYSVAL";
	    try
	    {
                u.setLocalePathName(testValue);
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
     <dt>Test:</dt><dd>Call User::setLocalePathName(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var076()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*SYSVAL";
	    try
	    {
                u.setLocalePathName(testValue);
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
     <dt>Test:</dt><dd>Call User::isLocalPasswordManagement() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var077()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            boolean expectedValue = pwrSys_.getVRM() < 0x00050300 ? false : true;
            boolean returnValue = u.isLocalPasswordManagement();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalPasswordManagement(boolean) with a valid parameter of true.</dd>
     <dt>Result:</dt><dd>Verify User::isLocalPasswordManagement() returns true.</dd>
     </dl>
     **/
    public void Var078()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            boolean testValue = true;
	    try
	    {
                u.setLocalPasswordManagement(testValue);
                if (pwrSys_.getVRM() < 0x00050300)
                {
                    failed("No exception.");
                }
                else
                {
                    boolean returnValue = u.isLocalPasswordManagement();
                    assertCondition(returnValue == testValue);
                }
	    }
	    catch (Exception e)
	    {
                if (pwrSys_.getVRM() < 0x00050300)
                {
                    assertExceptionStartsWith(e, "AS400Exception", "CPD0043 ", ErrorCompletingRequestException.AS400_ERROR);
                }
                else
                {
                    failed(e, "Unexpected Exception");
                }
	    }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalPasswordManagement(boolean) with a valid parameter of false.</dd>
     <dt>Result:</dt><dd>Verify User::isLocalPasswordManagement() returns false.</dd>
     </dl>
     **/
    public void Var079()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            boolean testValue = false;
	    try
	    {
                u.setLocalPasswordManagement(testValue);
                if (pwrSys_.getVRM() < 0x00050300)
                {
                    failed("No exception.");
                }
                else
                {
                    boolean returnValue = u.isLocalPasswordManagement();
                    assertCondition(returnValue == testValue);
                }
	    }
	    catch (Exception e)
	    {
                if (pwrSys_.getVRM() < 0x00050300)
                {
                    assertExceptionStartsWith(e, "AS400Exception", "CPD0043 ", ErrorCompletingRequestException.AS400_ERROR);
                }
                else
                {
                    failed(e, "Unexpected Exception");
                }
	    }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setLocalPasswordManagement(boolean) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var080()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            boolean testValue = true;
	    try
	    {
                u.setLocalPasswordManagement(testValue);
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
     <dt>Test:</dt><dd>Call User::setLocalPasswordManagement(boolean) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var081()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            boolean testValue = true;
	    try
	    {
                u.setLocalPasswordManagement(testValue);
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
     <dt>Test:</dt><dd>Call User::getMaximumStorageAllowed() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var082()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int expectedValue = -1;
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(int) with a valid parameter of -1.</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns -1.</dd>
     </dl>
     **/
    public void Var083()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = -1;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a valid parameter of *NOMAX.</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns -1.</dd>
     </dl>
     **/
    public void Var084()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NOMAX";
            int expectedValue = -1;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(int) with a valid parameter of 0.</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns 0.</dd>
     </dl>
     **/
    public void Var085()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 0;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a valid parameter of "0".</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns 0.</dd>
     </dl>
     **/
    public void Var086()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "0";
            int expectedValue = 0;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(int) with a valid parameter of 1.</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns 4.</dd>
     </dl>
     **/
    public void Var087()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 0;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a valid parameter of "1".</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns 4.</dd>
     </dl>
     **/
    public void Var088()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "1";
            int expectedValue = 4;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(int) with a valid parameter of 4000.</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns 4000.</dd>
     </dl>
     **/
    public void Var089()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 4000;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a valid parameter of "4000".</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns 4000.</dd>
     </dl>
     **/
    public void Var090()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "4000";
            int expectedValue = 4000;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(int) with a valid parameter of 2147483644.</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns 2147483644.</dd>
     </dl>
     **/
    public void Var091()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 2147483644;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a valid parameter of "2147483644".</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns 2147483644.</dd>
     </dl>
     **/
    public void Var092()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "2147483644";
            int expectedValue = 2147483644;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(int) with a valid parameter of 2147483645.</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns -1.</dd>
     </dl>
     **/
    public void Var093()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 2147483645;
            int expectedValue = -1;
	    if (getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
		expectedValue = -2;
	    } 
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == expectedValue,"maximumStorageAllowed="+returnValue+" sb "+expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a valid parameter of "2147483645".</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns -1.</dd>
     </dl>
     **/
    public void Var094()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "2147483645";
            int expectedValue = -1;
	    if (getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
		expectedValue = -2;
	    } 
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == expectedValue, "maximumStorageAllowed="+returnValue+" sb "+expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(int) with a valid parameter of 2147483647.</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns -1.</dd>
     </dl>
     **/
    public void Var095()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 2147483647;
            int expectedValue = -1;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a valid parameter of "2147483647".</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns -1.</dd>
     </dl>
     **/
    public void Var096()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "2147483647";
            int expectedValue = -1;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns -1.</dd>
     </dl>
     **/
    public void Var097()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            int expectedValue = -1;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getMaximumStorageAllowed() returns -1.</dd>
     </dl>
     **/
    public void Var098()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            int expectedValue = -1;
            u.setMaximumStorageAllowed(testValue);
            int returnValue = u.getMaximumStorageAllowed();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(int) with a not valid value parameter of -2.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var099()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = -2;
            try
            {
                u.setMaximumStorageAllowed(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0088 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a not valid value parameter of "-2".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var100()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "-2";
            try
            {
                u.setMaximumStorageAllowed(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0088 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a not valid value parameter of "-1".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var101()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "-1";
            try
            {
                u.setMaximumStorageAllowed(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0088 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a not valid value parameter of "2147483648".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var102()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "2147483648";
            try
            {
                u.setMaximumStorageAllowed(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0095 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a not valid value parameter of "asdf".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var103()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "asdf";
            try
            {
                u.setMaximumStorageAllowed(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0076 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var104()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setMaximumStorageAllowed(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "maximumStorageAllowed");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(int) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var105()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            int testValue = 37;
	    try
	    {
                u.setMaximumStorageAllowed(testValue);
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
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var106()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "37";
	    try
	    {
                u.setMaximumStorageAllowed(testValue);
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
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(int) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var107()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            int testValue = 37;
	    try
	    {
                u.setMaximumStorageAllowed(testValue);
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
     <dt>Test:</dt><dd>Call User::setMaximumStorageAllowed(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var108()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "37";
	    try
	    {
                u.setMaximumStorageAllowed(testValue);
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
     <dt>Test:</dt><dd>Call User::getMessageQueue() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var109()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String expectedValue = "/QSYS.LIB/QUSRSYS.LIB/" + userName + ".MSGQ";
            String returnValue = u.getMessageQueue();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) with a valid parameter of *USRPRF.</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueue() returns *USRPRF.</dd>
     </dl>
     **/
    public void Var110()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "*USRPRF";
            String expectedValue = "/QSYS.LIB/QUSRSYS.LIB/" + userName + ".MSGQ";
            u.setMessageQueue(testValue);
            String returnValue = u.getMessageQueue();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) with a valid parameter which is a path.</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueue() returns the path.</dd>
     </dl>
     **/
    public void Var111()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QUSRSYS.LIB/" + userName + ".MSGQ";
            u.setMessageQueue(testValue);
            String returnValue = u.getMessageQueue();

            u.setMessageQueue("*USRPRF");

            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) with a valid parameter which is a mixed case path.</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueue() returns the path.</dd>
     </dl>
     **/
    public void Var112()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSyS.LiB/QUSRSYS.lib/" + userName + ".msgq";
            String expectedValue = "/QSYS.LIB/QUSRSYS.LIB/" + userName + ".MSGQ";
            u.setMessageQueue(testValue);
            String returnValue = u.getMessageQueue();

            u.setMessageQueue("*USRPRF");

            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) with a valid parameter which is a path that does not exist.</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueue() returns the path.</dd>
     </dl>
     **/
    public void Var113()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "/QSYS.LIB/QUSRSYS.LIB/" + userName + "X.MSGQ";
            u.setMessageQueue(testValue);
            String returnValue = u.getMessageQueue();

            u.setMessageQueue("*USRPRF");
            CommandCall cmd = new CommandCall(pwrSys_, "QSYS/DLTMSGQ QUSRSYS/" + userName + "X");
            cmd.setThreadSafe(false);

            if (!cmd.run())
            {
                throw new AS400Exception(cmd.getMessageList());
            }

            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueue() returns the path.</dd>
     </dl>
     **/
    public void Var114()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "";
            String expectedValue = "/QSYS.LIB/QUSRSYS.LIB/" + userName + ".MSGQ";
            u.setMessageQueue(testValue);
            String returnValue = u.getMessageQueue();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueue() returns the path.</dd>
     </dl>
     **/
    public void Var115()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String testValue = "*SAME";
            String expectedValue = "/QSYS.LIB/QUSRSYS.LIB/" + userName + ".MSGQ";
            u.setMessageQueue(testValue);
            String returnValue = u.getMessageQueue();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var116()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setMessageQueue(testValue);
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
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var117()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setMessageQueue(testValue);
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
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var118()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QUSRSYS.LIB/USERNAME.MSGD";
            try
            {
                u.setMessageQueue(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/QUSRSYS.LIB/USERNAME.MSGD: ", IllegalPathNameException.OBJECT_TYPE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var119()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/ELEVENISBIG.LIB/USERNAME.MSGQ";
            try
            {
                u.setMessageQueue(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/ELEVENISBIG.LIB/USERNAME.MSGQ: ", IllegalPathNameException.LIBRARY_LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var120()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setMessageQueue(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "messageQueue");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var121()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "/QSYS.LIB/QUSRSYS.LIB/BOB.MSGQ";
	    try
	    {
                u.setMessageQueue(testValue);
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
     <dt>Test:</dt><dd>Call User::setMessageQueue(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var122()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "/QSYS.LIB/QUSRSYS.LIB/BOB.MSGQ";
	    try
	    {
                u.setMessageQueue(testValue);
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
     <dt>Test:</dt><dd>Call User::getMessageQueueDeliveryMethod() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var123()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*NOTIFY";
            String returnValue = u.getMessageQueueDeliveryMethod();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueueDeliveryMethod(String) with a valid parameter of *NOTIFY.</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueueDeliveryMethod() returns *NOTIFY.</dd>
     </dl>
     **/
    public void Var124()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NOTIFY";
            u.setMessageQueueDeliveryMethod(testValue);
            String returnValue = u.getMessageQueueDeliveryMethod();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueueDeliveryMethod(String) with a valid parameter of *HOLD.</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueueDeliveryMethod() returns *HOLD.</dd>
     </dl>
     **/
    public void Var125()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*HOLD";
            u.setMessageQueueDeliveryMethod(testValue);
            String returnValue = u.getMessageQueueDeliveryMethod();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueueDeliveryMethod(String) with a valid parameter of *BREAK.</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueueDeliveryMethod() returns *BREAK.</dd>
     </dl>
     **/
    public void Var126()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*BREAK";
            u.setMessageQueueDeliveryMethod(testValue);
            String returnValue = u.getMessageQueueDeliveryMethod();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueueDeliveryMethod(String) with a valid parameter of *DFT.</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueueDeliveryMethod() returns *DFT.</dd>
     </dl>
     **/
    public void Var127()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*DFT";
            u.setMessageQueueDeliveryMethod(testValue);
            String returnValue = u.getMessageQueueDeliveryMethod();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueueDeliveryMethod(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueueDeliveryMethod() returns *NOTIFY.</dd>
     </dl>
     **/
    public void Var128()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*NOTIFY";
            u.setMessageQueueDeliveryMethod(testValue);
            String returnValue = u.getMessageQueueDeliveryMethod();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueueDeliveryMethod(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueueDeliveryMethod() returns *NOTIFY.</dd>
     </dl>
     **/
    public void Var129()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*NOTIFY";
            u.setMessageQueueDeliveryMethod(testValue);
            String returnValue = u.getMessageQueueDeliveryMethod();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueueDeliveryMethod(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var130()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setMessageQueueDeliveryMethod(testValue);
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
     <dt>Test:</dt><dd>Call User::setMessageQueueDeliveryMethod(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var131()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setMessageQueueDeliveryMethod(testValue);
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
     <dt>Test:</dt><dd>Call User::setMessageQueueDeliveryMethod(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var132()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setMessageQueueDeliveryMethod(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "messageQueueDeliveryMethod");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueueDeliveryMethod(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var133()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*NOTIFY";
	    try
	    {
                u.setMessageQueueDeliveryMethod(testValue);
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
     <dt>Test:</dt><dd>Call User::setMessageQueueDeliveryMethod(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var134()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*NOTIFY";
	    try
	    {
                u.setMessageQueueDeliveryMethod(testValue);
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
     <dt>Test:</dt><dd>Call User::getMessageQueueSeverity() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var135()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int expectedValue = 0;
            int returnValue = u.getMessageQueueSeverity();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueueSeverity(int) with the valid parameters of the range 0-99.</dd>
     <dt>Result:</dt><dd>Verify User::getMessageQueueSeverity() returns the values.</dd>
     </dl>
     **/
    public void Var136()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            for (int testValue = 0; testValue <= 99; ++testValue)
            {
                u.setMessageQueueSeverity(testValue);
                int returnValue = u.getMessageQueueSeverity();
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
     <dt>Test:</dt><dd>Call User::setMessageQueueSeverity(int) with a not valid value parameter of -1.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var137()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = -1;
            try
            {
                u.setMessageQueueSeverity(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0085 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueueSeverity(int) with a not valid value parameter of 100.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var138()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 100;
            try
            {
                u.setMessageQueueSeverity(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD0085 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setMessageQueueSeverity(int) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var139()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            int testValue = 3;
	    try
	    {
                u.setMessageQueueSeverity(testValue);
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
     <dt>Test:</dt><dd>Call User::setMessageQueueSeverity(int) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var140()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            int testValue = 3;
	    try
	    {
                u.setMessageQueueSeverity(testValue);
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
    <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a parameter of "0".</dd>
    <dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns 0.</dd>
    </dl>
    **/
   public void Var141()
   {
       try
       {
    	   User u = new User(pwrSys_, sandbox_.createUser());
           String testValue = "0";
           String expectedValue = "0";
           u.setLimitDeviceSessions(testValue);
           if (pwrSys_.getVRM() < 0x00050500)
           {
               failed("No exception.");
           }
           String returnValue = u.getLimitDeviceSessions();
           assertCondition(returnValue.equals(expectedValue), "Expected " + expectedValue + " received " + returnValue);
       }
       catch (Exception e)
       {
    	   try{
    		   if (pwrSys_.getVRM() < 0x00050500)
    		   {
    			   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
    		   }
    		   else
    			   failed(e, "Unexpected Exception");
    	   }catch(Exception e1){
    		   failed(e1, "Unexpected Exception");
    	   }
       }
   }
   
   /**
   <dl>
   <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a parameter of "1".</dd>
   <dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns 1.</dd>
   </dl>
   **/
   public void Var142()
   {
      try
      {
   	   User u = new User(pwrSys_, sandbox_.createUser());
          String testValue = "1";
          String expectedValue = "1";
          u.setLimitDeviceSessions(testValue);
          if (pwrSys_.getVRM() < 0x00050500)
          {
              failed("No exception.");
          }
          String returnValue = u.getLimitDeviceSessions();
          assertCondition(returnValue.equals(expectedValue), "Expected " + expectedValue + " received " + returnValue);
      }
      catch (Exception e)
      {
   	   try{
   		   if (pwrSys_.getVRM() < 0x00050500)
   		   {
   			   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
   		   }
   		   else
   			   failed(e, "Unexpected Exception");
   	   }catch(Exception e1){
   		   failed(e1, "Unexpected Exception");
   	   }
      }
   }
  
  /**
  <dl>
  <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a parameter of "2".</dd>
  <dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns 2.</dd>
  </dl>
  **/
   public void Var143()
   {
     try
     {
  	   User u = new User(pwrSys_, sandbox_.createUser());
         String testValue = "2";
         String expectedValue = "2";
         u.setLimitDeviceSessions(testValue);
         if (pwrSys_.getVRM() < 0x00050500)
         {
             failed("No exception.");
         }
         String returnValue = u.getLimitDeviceSessions();
         assertCondition(returnValue.equals(expectedValue), "Expected " + expectedValue + " received " + returnValue);
     }
     catch (Exception e)
     {
  	   try{
  		   if (pwrSys_.getVRM() < 0x00050500)
  		   {
  			   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
  		   }
  		   else
  			   failed(e, "Unexpected Exception");
  	   }catch(Exception e1){
  		   failed(e1, "Unexpected Exception");
  	   }
     }
   }
 
 /**
 <dl>
 <dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a parameter of "3".</dd>
 <dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns 3.</dd>
 </dl>
 **/
   public void Var144()
   {
    try
    {
 	   User u = new User(pwrSys_, sandbox_.createUser());
        String testValue = "3";
        String expectedValue = "3";
        u.setLimitDeviceSessions(testValue);
        if (pwrSys_.getVRM() < 0x00050500)
        {
            failed("No exception.");
        }
        String returnValue = u.getLimitDeviceSessions();
        assertCondition(returnValue.equals(expectedValue), "Expected " + expectedValue + " received " + returnValue);
    }
    catch (Exception e)
    {
 	   try{
 		   if (pwrSys_.getVRM() < 0x00050500)
 		   {
 			   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
 		   }
 		   else
 			   failed(e, "Unexpected Exception");
 	   }catch(Exception e1){
 		   failed(e1, "Unexpected Exception");
 	   }
    }
   }

/**
<dl>
<dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a parameter of "4".</dd>
<dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns 4.</dd>
</dl>
**/
public void Var145()
{
   try
   {
	   User u = new User(pwrSys_, sandbox_.createUser());
       String testValue = "4";
       String expectedValue = "4";
       u.setLimitDeviceSessions(testValue);
       if (pwrSys_.getVRM() < 0x00050500)
       {
           failed("No exception.");
       }
       String returnValue = u.getLimitDeviceSessions();
       assertCondition(returnValue.equals(expectedValue), "Expected " + expectedValue + " received " + returnValue);
   }
   catch (Exception e)
   {
	   try{
		   if (pwrSys_.getVRM() < 0x00050500)
		   {
			   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
		   }
		   else
			   failed(e, "Unexpected Exception");
	   }catch(Exception e1){
		   failed(e1, "Unexpected Exception");
	   }
   }
}

/**
<dl>
<dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a parameter of "5".</dd>
<dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns 5.</dd>
</dl>
**/
public void Var146()
{
   try
   {
	   User u = new User(pwrSys_, sandbox_.createUser());
       String testValue = "5";
       String expectedValue = "5";
       u.setLimitDeviceSessions(testValue);
       if (pwrSys_.getVRM() < 0x00050500)
       {
           failed("No exception.");
       }
       String returnValue = u.getLimitDeviceSessions();
       assertCondition(returnValue.equals(expectedValue), "Expected " + expectedValue + " received " + returnValue);
   }
   catch (Exception e)
   {
	   try{
		   if (pwrSys_.getVRM() < 0x00050500)
		   {
			   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
		   }
		   else
			   failed(e, "Unexpected Exception");
	   }catch(Exception e1){
		   failed(e1, "Unexpected Exception");
	   }
   }
}

/**
<dl>
<dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a parameter of "6".</dd>
<dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns 6.</dd>
</dl>
**/
public void Var147()
{
   try
   {
	   User u = new User(pwrSys_, sandbox_.createUser());
       String testValue = "6";
       String expectedValue = "6";
       u.setLimitDeviceSessions(testValue);
       if (pwrSys_.getVRM() < 0x00050500)
       {
           failed("No exception.");
       }
       String returnValue = u.getLimitDeviceSessions();
       assertCondition(returnValue.equals(expectedValue), "Expected " + expectedValue + " received " + returnValue);
   }
   catch (Exception e)
   {
	   try{
		   if (pwrSys_.getVRM() < 0x00050500)
		   {
			   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
		   }
		   else
			   failed(e, "Unexpected Exception");
	   }catch(Exception e1){
		   failed(e1, "Unexpected Exception");
	   }
   }
}

/**
<dl>
<dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a parameter of "7".</dd>
<dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns 7.</dd>
</dl>
**/
public void Var148()
{
   try
   {
	   User u = new User(pwrSys_, sandbox_.createUser());
       String testValue = "7";
       String expectedValue = "7";
       u.setLimitDeviceSessions(testValue);
       if (pwrSys_.getVRM() < 0x00050500)
       {
           failed("No exception.");
       }
       String returnValue = u.getLimitDeviceSessions();
       assertCondition(returnValue.equals(expectedValue), "Expected " + expectedValue + " received " + returnValue);
   }
   catch (Exception e)
   {
	   try{
		   if (pwrSys_.getVRM() < 0x00050500)
		   {
			   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
		   }
		   else
			   failed(e, "Unexpected Exception");
	   }catch(Exception e1){
		   failed(e1, "Unexpected Exception");
	   }
   }
}

/**
<dl>
<dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a parameter of "8".</dd>
<dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns 8.</dd>
</dl>
**/
public void Var149()
{
   try
   {
	   User u = new User(pwrSys_, sandbox_.createUser());
       String testValue = "8";
       String expectedValue = "8";
       u.setLimitDeviceSessions(testValue);
       if (pwrSys_.getVRM() < 0x00050500)
       {
           failed("No exception.");
       }
       String returnValue = u.getLimitDeviceSessions();
       assertCondition(returnValue.equals(expectedValue), "Expected " + expectedValue + " received " + returnValue);
   }
   catch (Exception e)
   {
	   try{
		   if (pwrSys_.getVRM() < 0x00050500)
		   {
			   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
		   }
		   else
			   failed(e, "Unexpected Exception");
	   }catch(Exception e1){
		   failed(e1, "Unexpected Exception");
	   }
   }
}

/**
<dl>
<dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a parameter of "9".</dd>
<dt>Result:</dt><dd>Verify User::getLimitDeviceSessions() returns 9.</dd>
</dl>
**/
public void Var150()
{
   try
   {
	   User u = new User(pwrSys_, sandbox_.createUser());
       String testValue = "9";
       String expectedValue = "9";
       u.setLimitDeviceSessions(testValue);
       if (pwrSys_.getVRM() < 0x00050500)
       {
           failed("No exception.");
       }
       String returnValue = u.getLimitDeviceSessions();
       assertCondition(returnValue.equals(expectedValue), "Expected " + expectedValue + " received " + returnValue);
   }
   catch (Exception e)
   {
	   try{
		   if (pwrSys_.getVRM() < 0x00050500)
		   {
			   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
		   }
		   else
			   failed(e, "Unexpected Exception");
	   }catch(Exception e1){
		   failed(e1, "Unexpected Exception");
	   }
   }
}

/**
<dl>
<dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a non-valid parameter of "-1".</dd>
<dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
</dl>
**/
public void Var151()
{
   try
   {
	   User u = new User(pwrSys_, sandbox_.createUser());
       String testValue = "-1";
       u.setLimitDeviceSessions(testValue);
       failed("No exception.");
   }
   catch (Exception e)
   {
	   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
   }
}

/**
<dl>
<dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a non-valid parameter of "10".</dd>
<dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
</dl>
**/
public void Var152()
{
   try
   {
	   User u = new User(pwrSys_, sandbox_.createUser());
       String testValue = "10";
       u.setLimitDeviceSessions(testValue);
       failed("No exception.");
   }
   catch (Exception e)
   {
	   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
   }
}

/**
<dl>
<dt>Test:</dt><dd>Call User::setLimitDeviceSessions(String) with a non-valid parameter of "567".</dd>
<dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
</dl>
**/
public void Var153()
{
   try
   {
	   User u = new User(pwrSys_, sandbox_.createUser());
       String testValue = "567";
       u.setLimitDeviceSessions(testValue);
       failed("No exception.");
   }
   catch (Exception e)
   {
	   assertExceptionStartsWith(e, "AS400Exception", "CPD0084 ", ErrorCompletingRequestException.AS400_ERROR);
   }
}
}
