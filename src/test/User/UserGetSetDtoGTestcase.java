///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserGetSetDtoGTestcase.java
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
import com.ibm.as400.access.User;

import test.Testcase;
import test.UserTest;

/**
 Testcase UserGetSetTestcase.  This tests the get and set methods of the User class.
 **/
public class UserGetSetDtoGTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserGetSetDtoGTestcase";
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
 

        sandbox_ = new UserSandbox(pwrSys_, "UGSD", UserTest.COLLECTION.substring(UserTest.COLLECTION.length() - 1));
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
     <dt>Test:</dt><dd>Call User::getDaysUntilPasswordExpire() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var001()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int expectedValue = -1;
            int returnValue = u.getDaysUntilPasswordExpire();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Setup user profile with expired password.</dd>
     <dt>Result:</dt><dd>Verify User::getDaysUntilPasswordExpire() returns 0.</dd>
     </dl>
     **/
    public void Var002()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int expectedValue = 0;
            u.setPasswordSetExpire(true);
            int returnValue = u.getDaysUntilPasswordExpire();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Setup user profile with password which will expire in 1 day.</dd>
     <dt>Result:</dt><dd>Verify User::getDaysUntilPasswordExpire() returns 1.</dd>
     </dl>
     **/
    public void Var003()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 1;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getDaysUntilPasswordExpire();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Setup user profile with password which will expire in 2 day.</dd>
     <dt>Result:</dt><dd>Verify User::getDaysUntilPasswordExpire() returns 2.</dd>
     </dl>
     **/
    public void Var004()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 2;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getDaysUntilPasswordExpire();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Setup user profile with password which will expire in 3 day.</dd>
     <dt>Result:</dt><dd>Verify User::getDaysUntilPasswordExpire() returns 3.</dd>
     </dl>
     **/
    public void Var005()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 3;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getDaysUntilPasswordExpire();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Setup user profile with password which will expire in 4 day.</dd>
     <dt>Result:</dt><dd>Verify User::getDaysUntilPasswordExpire() returns 4.</dd>
     </dl>
     **/
    public void Var006()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 4;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getDaysUntilPasswordExpire();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Setup user profile with password which will expire in 5 day.</dd>
     <dt>Result:</dt><dd>Verify User::getDaysUntilPasswordExpire() returns 5.</dd>
     </dl>
     **/
    public void Var007()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 5;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getDaysUntilPasswordExpire();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Setup user profile with password which will expire in 6 day.</dd>
     <dt>Result:</dt><dd>Verify User::getDaysUntilPasswordExpire() returns 6.</dd>
     </dl>
     **/
    public void Var008()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 6;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getDaysUntilPasswordExpire();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Setup user profile with password which will expire in 7 day.</dd>
     <dt>Result:</dt><dd>Verify User::getDaysUntilPasswordExpire() returns 7.</dd>
     </dl>
     **/
    public void Var009()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 7;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getDaysUntilPasswordExpire();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Setup user profile with password which will expire in 8 day.</dd>
     <dt>Result:</dt><dd>Verify User::getDaysUntilPasswordExpire() returns -1.</dd>
     </dl>
     **/
    public void Var010()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 8;
            int expectedValue = -1;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getDaysUntilPasswordExpire();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Setup user profile with password which will expire in 366 day.</dd>
     <dt>Result:</dt><dd>Verify User::getDaysUntilPasswordExpire() returns -1.</dd>
     </dl>
     **/
    public void Var011()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 366;
            int expectedValue = -1;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getDaysUntilPasswordExpire();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Setup user profile with password which will not expire.</dd>
     <dt>Result:</dt><dd>Verify User::getDaysUntilPasswordExpire() returns -1.</dd>
     </dl>
     **/
    public void Var012()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int expectedValue = -1;
            u.setPasswordExpirationInterval("*NOMAX");
            int returnValue = u.getDaysUntilPasswordExpire();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getDescription() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var013()
    {
        try
        {

	    try {
		CommandCall cmd = new CommandCall(pwrSys_, "QSYS/DLTUSRPRF USRPRF(UGSDFT)");
		cmd.setThreadSafe(false);

		if (!cmd.run()) {
		    com.ibm.as400.access.AS400Message[] messages = cmd.getMessageList();
		    StringBuffer message = new StringBuffer();
		    for (int i = 0; i < messages.length; i++) {
			message.append(messages[i]);
			message.append("\n"); 
		    }
		    String messageString = message.toString();
		    if (messageString.indexOf("User profile UGSDFT not found") >= 0) {
			// Just ignore
		    } else { 
			output_.println("Messages from DLTUSRPRF "+message);
		    }
		}
	    } catch (Exception e) {
		output_.println("Exception on DLTUSRPRF");
		e.printStackTrace(output_); 
	    }
            CommandCall cmd = new CommandCall(pwrSys_, "QSYS/CRTUSRPRF USRPRF(UGSDFT) PASSWORD(*NONE)");
            cmd.setThreadSafe(false);

            if (!cmd.run())
            {
                throw new AS400Exception(cmd.getMessageList());
            }

            User u = new User(pwrSys_, "UGSDFT");
            String expectedValue = "";
            String returnValue = u.getDescription();
            assertCondition(returnValue.equals(expectedValue));

            cmd = new CommandCall(pwrSys_, "QSYS/DLTUSRPRF USRPRF(UGSDFT)");
            cmd.setThreadSafe(false);

            if (!cmd.run())
            {
                throw new AS400Exception(cmd.getMessageList());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDescription(String) with a valid parameter of *BLANK.</dd>
     <dt>Result:</dt><dd>Verify User::getDescription() returns an empty string.</dd>
     </dl>
     **/
    public void Var014()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*BLANK";
            String expectedValue = "";
            u.setDescription(testValue);
            String returnValue = u.getDescription();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDescription(String) with a valid parameter of "123xyz".</dd>
     <dt>Result:</dt><dd>Verify User::getDescription() returns "123xyz".</dd>
     </dl>
     **/
    public void Var015()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "123xyz";
            u.setDescription(testValue);
            String returnValue = u.getDescription();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDescription(String) with a valid parameter of empty string "".</dd>
     <dt>Result:</dt><dd>Verify User::getDescription() returns "".</dd>
     </dl>
     **/
    public void Var016()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            u.setDescription(testValue);
            String returnValue = u.getDescription();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDescription(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getDescription() returns "".</dd>
     </dl>
     **/
    public void Var017()
    {
        try
        {
            CommandCall cmd = new CommandCall(pwrSys_, "QSYS/CRTUSRPRF USRPRF(UGSDFT) PASSWORD(*NONE)");
            cmd.setThreadSafe(false);

            if (!cmd.run())
            {
                throw new AS400Exception(cmd.getMessageList());
            }

            User u = new User(pwrSys_, "UGSDFT");
            String testValue = "*SAME";
            String expectedValue = "";
            u.setDescription(testValue);
            String returnValue = u.getDescription();
            assertCondition(returnValue.equals(expectedValue));

            cmd = new CommandCall(pwrSys_, "QSYS/DLTUSRPRF USRPRF(UGSDFT)");
            cmd.setThreadSafe(false);

            if (!cmd.run())
            {
                throw new AS400Exception(cmd.getMessageList());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDescription(String) with a valid max length parameter of "abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxy".</dd>
     <dt>Result:</dt><dd>Verify User::getDescription() returns "abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxy".</dd>
     </dl>
     **/
    public void Var018()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxy";
            u.setDescription(testValue);
            String returnValue = u.getDescription();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDescription(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var019()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxy";
            try
            {
                u.setDescription(testValue);
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
     <dt>Test:</dt><dd>Call User::setDescription(String) with a null parameter.</dd>
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
                u.setDescription(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "description");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDescription(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var021()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "123xyz";
	    try
	    {
                u.setDescription(testValue);
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
     <dt>Test:</dt><dd>Call User::setDescription(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var022()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "123xyz";
	    try
	    {
                u.setDescription(testValue);
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
     <dt>Test:</dt><dd>Call User::getDisplaySignOnInformation() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var023()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*SYSVAL";
            String returnValue = u.getDisplaySignOnInformation();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDisplaySignOnInformation(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getDisplaySignOnInformation() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var024()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            u.setDisplaySignOnInformation(testValue);
            String returnValue = u.getDisplaySignOnInformation();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDisplaySignOnInformation(String) with a valid parameter of *NO.</dd>
     <dt>Result:</dt><dd>Verify User::getDisplaySignOnInformation() returns *NO.</dd>
     </dl>
     **/
    public void Var025()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NO";
            u.setDisplaySignOnInformation(testValue);
            String returnValue = u.getDisplaySignOnInformation();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDisplaySignOnInformation(String) with a valid parameter of *YES.</dd>
     <dt>Result:</dt><dd>Verify User::getDisplaySignOnInformation() returns *YES.</dd>
     </dl>
     **/
    public void Var026()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*YES";
            u.setDisplaySignOnInformation(testValue);
            String returnValue = u.getDisplaySignOnInformation();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDisplaySignOnInformation(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getDisplaySignOnInformation() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var027()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*SYSVAL";
            u.setDisplaySignOnInformation(testValue);
            String returnValue = u.getDisplaySignOnInformation();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDisplaySignOnInformation(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getDisplaySignOnInformation() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var028()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*SYSVAL";
            u.setDisplaySignOnInformation(testValue);
            String returnValue = u.getDisplaySignOnInformation();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDisplaySignOnInformation(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var029()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setDisplaySignOnInformation(testValue);
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
     <dt>Test:</dt><dd>Call User::setDisplaySignOnInformation(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var030()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setDisplaySignOnInformation(testValue);
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
     <dt>Test:</dt><dd>Call User::setDisplaySignOnInformation(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var031()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setDisplaySignOnInformation(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "displaySignOnInformation");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setDisplaySignOnInformation(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var032()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*SYSVAL";
	    try
	    {
                u.setDisplaySignOnInformation(testValue);
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
     <dt>Test:</dt><dd>Call User::setDisplaySignOnInformation(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var033()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*SYSVAL";
	    try
	    {
                u.setDisplaySignOnInformation(testValue);
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
     <dt>Test:</dt><dd>Call User::getGroupAuthority() before setting on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var034()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*NONE";
            String returnValue = u.getGroupAuthority();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getGroupAuthority() before setting on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var035()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String expectedValue = "*NONE";
            String returnValue = u.getGroupAuthority();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a valid parameter of *NONE on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthority() returns *NONE.</dd>
     </dl>
     **/
    public void Var036()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NONE";
            u.setGroupAuthority(testValue);
            String returnValue = u.getGroupAuthority();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a valid parameter of *NONE on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthority() returns *NONE.</dd>
     </dl>
     **/
    public void Var037()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String testValue = "*NONE";
            u.setGroupAuthority(testValue);
            String returnValue = u.getGroupAuthority();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a valid parameter of *ALL.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthority() returns *ALL.</dd>
     </dl>
     **/
    public void Var038()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String testValue = "*ALL";
            u.setGroupAuthority(testValue);
            String returnValue = u.getGroupAuthority();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a valid parameter of *CHANGE.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthority() returns *CHANGE.</dd>
     </dl>
     **/
    public void Var039()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String testValue = "*CHANGE";
            u.setGroupAuthority(testValue);
            String returnValue = u.getGroupAuthority();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a valid parameter of *USE.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthority() returns *USE.</dd>
     </dl>
     **/
    public void Var040()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String testValue = "*USE";
            u.setGroupAuthority(testValue);
            String returnValue = u.getGroupAuthority();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a valid parameter of *EXCLUDE.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthority() returns *EXCLUDE.</dd>
     </dl>
     **/
    public void Var041()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String testValue = "*EXCLUDE";
            u.setGroupAuthority(testValue);
            String returnValue = u.getGroupAuthority();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthority() returns *NONE.</dd>
     </dl>
     **/
    public void Var042()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*NONE";
            u.setGroupAuthority(testValue);
            String returnValue = u.getGroupAuthority();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthority() returns *NONE.</dd>
     </dl>
     **/
    public void Var043()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*NONE";
            u.setGroupAuthority(testValue);
            String returnValue = u.getGroupAuthority();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a parameter of *ALL on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var044()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*ALL";
            try
            {
                u.setGroupAuthority(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF2261 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a parameter of *CHANGE on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var045()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*CHANGE";
            try
            {
                u.setGroupAuthority(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF2261 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a parameter of *USE on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var046()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USE";
            try
            {
                u.setGroupAuthority(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF2261 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a parameter of *EXCLUDE on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var047()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*EXCLUDE";
            try
            {
                u.setGroupAuthority(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF2261 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var048()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setGroupAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var049()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setGroupAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var050()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setGroupAuthority(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "groupAuthority");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var051()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*NONE";
	    try
	    {
                u.setGroupAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupAuthority(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var052()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*NONE";
	    try
	    {
                u.setGroupAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::getGroupAuthorityType() before setting on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var053()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*PRIVATE";
            String returnValue = u.getGroupAuthorityType();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getGroupAuthorityType() before setting on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var054()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String expectedValue = "*PRIVATE";
            String returnValue = u.getGroupAuthorityType();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getGroupAuthorityType() before setting on a profile with a group and a group authority set.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var055()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            u.setGroupAuthority("*CHANGE");
            String expectedValue = "*PRIVATE";
            String returnValue = u.getGroupAuthorityType();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) with a valid parameter of *PRIVATE on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthorityType() returns *PRIVATE.</dd>
     </dl>
     **/
    public void Var056()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*PRIVATE";
            u.setGroupAuthorityType(testValue);
            String returnValue = u.getGroupAuthorityType();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) with a valid parameter of *PRIVATE on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthorityType() returns *PRIVATE.</dd>
     </dl>
     **/
    public void Var057()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String testValue = "*PRIVATE";
            u.setGroupAuthorityType(testValue);
            String returnValue = u.getGroupAuthorityType();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) with a valid parameter of *PRIVATE on a profile with a group and a group authority set.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthorityType() returns *PRIVATE.</dd>
     </dl>
     **/
    public void Var058()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            u.setGroupAuthority("*CHANGE");
            String testValue = "*PRIVATE";
            u.setGroupAuthorityType(testValue);
            String returnValue = u.getGroupAuthorityType();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) with a valid parameter of *PGP on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthorityType() returns *PGP.</dd>
     </dl>
     **/
    public void Var059()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*PGP";
            u.setGroupAuthorityType(testValue);
            String returnValue = u.getGroupAuthorityType();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) with a valid parameter of *PGP on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthorityType() returns *PGP.</dd>
     </dl>
     **/
    public void Var060()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String testValue = "*PGP";
            u.setGroupAuthorityType(testValue);
            String returnValue = u.getGroupAuthorityType();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) with a valid parameter of *PGP on a profile with a group and a group authority set.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthorityType() returns *PGP.</dd>
     </dl>
     **/
    public void Var061()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            u.setGroupAuthority("*CHANGE");
            String testValue = "*PGP";
            u.setGroupAuthorityType(testValue);
            String returnValue = u.getGroupAuthorityType();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthorityType() returns *PRIVATE.</dd>
     </dl>
     **/
    public void Var062()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*PRIVATE";
            u.setGroupAuthorityType(testValue);
            String returnValue = u.getGroupAuthorityType();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getGroupAuthorityType() returns *PRIVATE.</dd>
     </dl>
     **/
    public void Var063()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*PRIVATE";
            u.setGroupAuthorityType(testValue);
            String returnValue = u.getGroupAuthorityType();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var064()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setGroupAuthorityType(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var065()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setGroupAuthorityType(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var066()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setGroupAuthorityType(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "groupAuthorityType");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var067()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*PRIVATE";
	    try
	    {
                u.setGroupAuthorityType(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupAuthorityType(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var068()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*PRIVATE";
	    try
	    {
                u.setGroupAuthorityType(testValue);
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
     <dt>Test:</dt><dd>Call User::isGroupHasMember() on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify value false is returned.</dd>
     </dl>
     **/
    public void Var069()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            boolean expectedValue = false;
            boolean returnValue = u.isGroupHasMember();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::isGroupHasMember() on a group profile with no members.</dd>
     <dt>Result:</dt><dd>Verify value false is returned.</dd>
     </dl>
     **/
    public void Var070()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            boolean expectedValue = false;
            boolean returnValue = u.isGroupHasMember();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::isGroupHasMember() on a group profile with members.</dd>
     <dt>Result:</dt><dd>Verify value true is returned.</dd>
     </dl>
     **/
    public void Var071()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[0]);
            boolean expectedValue = true;
            boolean returnValue = u.isGroupHasMember();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getGroupID() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var072()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long expectedValue = 0;
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getGroupID() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var073()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int expectedValue = 0;
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a valid parameter of 0.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 0.</dd>
     </dl>
     **/
    public void Var074()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 0;
            u.setGroupID(testValue);
            long returnValue = u.getGroupID();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a valid parameter of 0.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 0.</dd>
     </dl>
     **/
    public void Var075()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 0;
            u.setGroupID(testValue);
            long returnValue = u.getGroupID();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 0.</dd>
     </dl>
     **/
    public void Var076()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NONE";
            long expectedValue = 0;
            u.setGroupID(testValue);
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 0.</dd>
     </dl>
     **/
    public void Var077()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NONE";
            int expectedValue = 0;
            u.setGroupID(testValue);
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a valid parameter of *GEN.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns a number is the valid range.</dd>
     </dl>
     **/
    public void Var078()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*GEN";
            u.setGroupID(testValue);
            long returnValue = u.getGroupID();
            assertCondition(returnValue >= 1 && returnValue <= 4294967294l);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a valid parameter of 147823.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 147823.</dd>
     </dl>
     **/
    public void Var079()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 147823;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == testValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a valid parameter of 147824.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 147824.</dd>
     </dl>
     **/
    public void Var080()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 147824;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == testValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a valid parameter of "147825".</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 147825.</dd>
     </dl>
     **/
    public void Var081()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "147825";
            long expectedValue = 147825;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a valid parameter of "147826".</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 147826.</dd>
     </dl>
     **/
    public void Var082()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "147826";
            int expectedValue = 147826;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a valid parameter of 147827 on a group profile.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 147827.</dd>
     </dl>
     **/
    public void Var083()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            long testValue = 147827;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == testValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a valid parameter of 147828 on a group profile.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 147828.</dd>
     </dl>
     **/
    public void Var084()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            long testValue = 147828;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == testValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a valid parameter of "147829" on a group profile.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 147829.</dd>
     </dl>
     **/
    public void Var085()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            String testValue = "147829";
            long expectedValue = 147829;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a valid parameter of "147830" on a group profile.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 147830.</dd>
     </dl>
     **/
    public void Var086()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            String testValue = "147830";
            int expectedValue = 147830;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a valid parameter of 1.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 1.</dd>
     </dl>
     **/
    public void Var087()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 1;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == testValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a valid parameter of 1.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 1.</dd>
     </dl>
     **/
    public void Var088()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 1;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == testValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a valid parameter of "1".</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 1.</dd>
     </dl>
     **/
    public void Var089()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "1";
            long expectedValue = 1;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a valid parameter of "1".</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 1.</dd>
     </dl>
     **/
    public void Var090()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "1";
            int expectedValue = 1;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a valid parameter of 4294967294.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 4294967294.</dd>
     </dl>
     **/
    public void Var091()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 4294967294l;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == testValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a valid parameter of 4294967294.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns -2.</dd>
     </dl>
     **/
    public void Var092()
    
    {
      assertCondition(true, "tested depreciated 32-bit API"); 
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a valid parameter of "4294967294".</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 4294967294.</dd>
     </dl>
     **/
    public void Var093()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "4294967294";
            long expectedValue = 4294967294l;
            try
            {
                u.setGroupID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
            u.setGroupID("*NONE");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a valid parameter of "4294967294".</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns -2.</dd>
     </dl>
     **/
    public void Var094()
    {
          assertCondition(true, "tested depreciated 32-bit API"); 
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 0.</dd>
     </dl>
     **/
    public void Var095()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            long expectedValue = 0;
            u.setGroupID(testValue);
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getGroupID() returns 0.</dd>
     </dl>
     **/
    public void Var096()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            long expectedValue = 0;
            u.setGroupID(testValue);
            long returnValue = u.getGroupID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a not valid value parameter that is already taken.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var097()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            long testValue = u.getGroupID();
            User uu = new User(pwrSys_, sandbox_.createGroup());
            try
            {
                uu.setGroupID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a not valid value parameter that is already taken.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var098()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            String testValue = Long.toString(u.getGroupID());
            User uu = new User(pwrSys_, sandbox_.createGroup());
            try
            {
                uu.setGroupID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a not valid value parameter of -1.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var099()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = -1;
            try
            {
                u.setGroupID(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a not valid value parameter of "-1".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var100()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "-1";
            try
            {
                u.setGroupID(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a not valid value parameter of "0".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var101()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "0";
            try
            {
                u.setGroupID(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupID(long) with a not valid value parameter of 4294967295.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var102()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 4294967295l;
            try
            {
                u.setGroupID(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a not valid value parameter of "4294967295".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var103()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "4294967295";
            try
            {
                u.setGroupID(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a not valid value parameter of "asdf".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var104()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "asdf";
            try
            {
                u.setGroupID(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupID(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var105()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setGroupID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "groupID");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupID(long) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var106()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            long testValue = 37;
	    try
	    {
                u.setGroupID(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupID(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var107()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "37";
	    try
	    {
                u.setGroupID(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupID(long) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var108()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            long testValue = 37;
	    try
	    {
                u.setGroupID(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupID(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var109()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "37";
	    try
	    {
                u.setGroupID(testValue);
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
     <dt>Test:</dt><dd>Call User::getGroupProfileName() before setting on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var110()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*NONE";
            String returnValue = u.getGroupProfileName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getGroupProfileName() before setting on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var111()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String expectedValue = groupAndUsers[0];
            String returnValue = u.getGroupProfileName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) with a valid parameter of *NONE on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupProfileName() returns *NONE.</dd>
     </dl>
     **/
    public void Var112()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NONE";
            u.setGroupProfileName(testValue);
            String returnValue = u.getGroupProfileName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) with a valid parameter of *NONE on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupProfileName() returns *NONE.</dd>
     </dl>
     **/
    public void Var113()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String testValue = "*NONE";
            u.setGroupProfileName(testValue);
            String returnValue = u.getGroupProfileName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) with a valid name parameter on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupProfileName() returns the name.</dd>
     </dl>
     **/
    public void Var114()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = sandbox_.createGroup();
            u.setGroupProfileName(testValue);
            String returnValue = u.getGroupProfileName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) with a valid name parameter on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupProfileName() returns the name.</dd>
     </dl>
     **/
    public void Var115()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String testValue = sandbox_.createGroup();
            u.setGroupProfileName(testValue);
            String returnValue = u.getGroupProfileName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getGroupProfileName() returns *NONE.</dd>
     </dl>
     **/
    public void Var116()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*NONE";
            u.setGroupProfileName(testValue);
            String returnValue = u.getGroupProfileName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getGroupProfileName() returns *NONE.</dd>
     </dl>
     **/
    public void Var117()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*NONE";
            u.setGroupProfileName(testValue);
            String returnValue = u.getGroupProfileName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) with a parameter of a name that does not exist.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var118()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "NOTEXIST";
            try
            {
                u.setGroupProfileName(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF2259 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) on a group profile.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var119()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            String testValue = sandbox_.createGroup();
            try
            {
                u.setGroupProfileName(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF2264 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) with a not valid star value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var120()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setGroupProfileName(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var121()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "ELEVENISBIG";
            try
            {
                u.setGroupProfileName(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var122()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setGroupProfileName(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "groupProfileName");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var123()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*NONE";
	    try
	    {
                u.setGroupProfileName(testValue);
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
     <dt>Test:</dt><dd>Call User::setGroupProfileName(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var124()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*NONE";
	    try
	    {
                u.setGroupProfileName(testValue);
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
