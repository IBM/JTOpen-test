///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserGetSetNtoQTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.IllegalPathNameException;
import com.ibm.as400.access.User;

import test.Testcase;

/**
 Testcase UserGetSetTestcase.  This tests the get and set methods of the User class.
 **/
public class UserGetSetNtoQTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserGetSetNtoQTestcase";
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
        sandbox_ = new UserSandbox(pwrSys_, "UGSNQT");
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
     <dt>Test:</dt><dd>Call User::isNoPassword() on a profile with password *NONE.</dd>
     <dt>Result:</dt><dd>Verify value true is returned.</dd>
     </dl>
     **/
    public void Var001()
    {
        try
        {
            CommandCall cmd = new CommandCall(pwrSys_, "QSYS/CRTUSRPRF USRPRF(UGSNQTX) PASSWORD(*NONE)");
            cmd.setThreadSafe(false);

            if (!cmd.run())
            {
		 AS400Message[] message = cmd.getMessageList();
		if ((message != null) && 
		    (message.length > 0) && 
		    (message[0].toString().indexOf("CPF2214") > 0)) {
		    System.out.println("Warning "+message[0].toString()); 
		} else { 
		    throw new AS400Exception(message);
		}
            }

            User u = new User(pwrSys_, "UGSNQTX");
            boolean expectedValue = true;
            boolean returnValue = u.isNoPassword();
            assertCondition(returnValue == expectedValue);

            cmd = new CommandCall(pwrSys_, "QSYS/DLTUSRPRF USRPRF(UGSNQTX)");
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
     <dt>Test:</dt><dd>Call User::isNoPassword() on a profile with a password.</dd>
     <dt>Result:</dt><dd>Verify value false is returned.</dd>
     </dl>
     **/
    public void Var002()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            boolean expectedValue = false;
            boolean returnValue = u.isNoPassword();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getObjectAuditingValue() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var003()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*NONE";
            String returnValue = u.getObjectAuditingValue();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setObjectAuditingValue(String) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getObjectAuditingValue() returns *NONE.</dd>
     </dl>
     **/
    public void Var004()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NONE";
            u.setObjectAuditingValue(testValue);
            String returnValue = u.getObjectAuditingValue();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setObjectAuditingValue(String) with a valid parameter of *ALL.</dd>
     <dt>Result:</dt><dd>Verify User::getObjectAuditingValue() returns *ALL.</dd>
     </dl>
     **/
    public void Var005()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*ALL";
            u.setObjectAuditingValue(testValue);
            String returnValue = u.getObjectAuditingValue();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setObjectAuditingValue(String) with a valid parameter of *CHANGE.</dd>
     <dt>Result:</dt><dd>Verify User::getObjectAuditingValue() returns *CHANGE.</dd>
     </dl>
     **/
    public void Var006()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*CHANGE";
            u.setObjectAuditingValue(testValue);
            String returnValue = u.getObjectAuditingValue();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setObjectAuditingValue(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var007()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            try
            {
                u.setObjectAuditingValue(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD22B1 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setObjectAuditingValue(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var008()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            try
            {
                u.setObjectAuditingValue(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPD22B1 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setObjectAuditingValue(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var009()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setObjectAuditingValue(testValue);
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
     <dt>Test:</dt><dd>Call User::setObjectAuditingValue(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var010()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setObjectAuditingValue(testValue);
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
     <dt>Test:</dt><dd>Call User::setObjectAuditingValue(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var011()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setObjectAuditingValue(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "objectAuditingValue");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setObjectAuditingValue(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var012()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*NONE";
	    try
	    {
                u.setObjectAuditingValue(testValue);
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
     <dt>Test:</dt><dd>Call User::setObjectAuditingValue(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var013()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*NONE";
	    try
	    {
                u.setObjectAuditingValue(testValue);
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
     <dt>Test:</dt><dd>Call User::getOutputQueue() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var014()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*WRKSTN";
            String returnValue = u.getOutputQueue();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) with a valid parameter of *WRKSTN.</dd>
     <dt>Result:</dt><dd>Verify User::getOutputQueue() returns *WRKSTN.</dd>
     </dl>
     **/
    public void Var015()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*WRKSTN";
            u.setOutputQueue(testValue);
            String returnValue = u.getOutputQueue();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) with a valid parameter of *DEV.</dd>
     <dt>Result:</dt><dd>Verify User::getOutputQueue() returns *DEV.</dd>
     </dl>
     **/
    public void Var016()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*DEV";
            u.setOutputQueue(testValue);
            String returnValue = u.getOutputQueue();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) with a valid parameter which is a path.</dd>
     <dt>Result:</dt><dd>Verify User::getOutputQueue() returns the path.</dd>
     </dl>
     **/
    public void Var017()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QGPL.LIB/QPRINT.OUTQ";
            u.setOutputQueue(testValue);
            String returnValue = u.getOutputQueue();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) with a valid parameter which is a mixed case path.</dd>
     <dt>Result:</dt><dd>Verify User::getOutputQueue() returns the path.</dd>
     </dl>
     **/
    public void Var018()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/qSYS.lIB/qGPL.lIB/qPRINT.oUTQ";
            String expectedValue = "/QSYS.LIB/QGPL.LIB/QPRINT.OUTQ";
            u.setOutputQueue(testValue);
            String returnValue = u.getOutputQueue();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) with a valid parameter which is a path that does not exist.</dd>
     <dt>Result:</dt><dd>Verify User::getOutputQueue() returns the path.</dd>
     </dl>
     **/
    public void Var019()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/NOTEXIST.LIB/NOTEXIST.OUTQ";
            u.setOutputQueue(testValue);
            String returnValue = u.getOutputQueue();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getOutputQueue() returns the path.</dd>
     </dl>
     **/
    public void Var020()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*WRKSTN";
            u.setOutputQueue(testValue);
            String returnValue = u.getOutputQueue();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getOutputQueue() returns the path.</dd>
     </dl>
     **/
    public void Var021()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*WRKSTN";
            u.setOutputQueue(testValue);
            String returnValue = u.getOutputQueue();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var022()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setOutputQueue(testValue);
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
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var023()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setOutputQueue(testValue);
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
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var024()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QGPL.LIB/QPRINT.MSGQ";
            try
            {
                u.setOutputQueue(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/QGPL.LIB/QPRINT.MSGQ: ", IllegalPathNameException.OBJECT_TYPE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var025()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/ELEVENISBIG.LIB/QPRINT.OUTQ";
            try
            {
                u.setOutputQueue(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/ELEVENISBIG.LIB/QPRINT.OUTQ: ", IllegalPathNameException.LIBRARY_LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var026()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setOutputQueue(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "outputQueue");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var027()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "/QSYS.LIB/QGPL.LIB/QPRINT.OUTQ";
	    try
	    {
                u.setOutputQueue(testValue);
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
     <dt>Test:</dt><dd>Call User::setOutputQueue(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var028()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "/QSYS.LIB/QGPL.LIB/QPRINT.OUTQ";
	    try
	    {
                u.setOutputQueue(testValue);
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
     <dt>Test:</dt><dd>Call User::getOwner() before setting on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var029()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*USRPRF";
            String returnValue = u.getOwner();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getOwner() before setting on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var030()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String expectedValue = "*USRPRF";
            String returnValue = u.getOwner();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getOwner() before setting on a profile with a group and a group authority set.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var031()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            u.setGroupAuthority("*CHANGE");
            String expectedValue = "*USRPRF";
            String returnValue = u.getOwner();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOwner(String) with a valid parameter of *USRPRF on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify User::getOwner() returns *USRPRF.</dd>
     </dl>
     **/
    public void Var032()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USRPRF";
            u.setOwner(testValue);
            String returnValue = u.getOwner();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOwner(String) with a valid parameter of *USRPRF on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify User::getOwner() returns *USRPRF.</dd>
     </dl>
     **/
    public void Var033()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String testValue = "*USRPRF";
            u.setOwner(testValue);
            String returnValue = u.getOwner();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOwner(String) with a valid parameter of *USRPRF on a profile with a group and a group authority set.</dd>
     <dt>Result:</dt><dd>Verify User::getOwner() returns *USRPRF.</dd>
     </dl>
     **/
    public void Var034()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            u.setGroupAuthority("*CHANGE");
            String testValue = "*USRPRF";
            u.setOwner(testValue);
            String returnValue = u.getOwner();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOwner(String) with a valid parameter of *GRPPRF on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify User::getOwner() returns *GRPPRF.</dd>
     </dl>
     **/
    public void Var035()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String testValue = "*GRPPRF";
            u.setOwner(testValue);
            String returnValue = u.getOwner();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOwner(String) with a valid parameter of *GRPPRF on a profile with a group and a group authority set.</dd>
     <dt>Result:</dt><dd>Verify User::getOwner() returns *GRPPRF.</dd>
     </dl>
     **/
    public void Var036()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            u.setGroupAuthority("*CHANGE");
            String testValue = "*GRPPRF";
            u.setOwner(testValue);
            String returnValue = u.getOwner();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOwner(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getOwner() returns *USRPRF.</dd>
     </dl>
     **/
    public void Var037()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*USRPRF";
            u.setOwner(testValue);
            String returnValue = u.getOwner();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOwner(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getOwner() returns *USRPRF.</dd>
     </dl>
     **/
    public void Var038()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*USRPRF";
            u.setOwner(testValue);
            String returnValue = u.getOwner();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOwner(String) with a valid parameter of *GRPPRF on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify User::getOwner() returns *GRPPRF.</dd>
     </dl>
     **/
    public void Var039()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*GRPPRF";
            try
            {
                u.setOwner(testValue);
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
     <dt>Test:</dt><dd>Call User::setOwner(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var040()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setOwner(testValue);
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
     <dt>Test:</dt><dd>Call User::setOwner(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var041()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setOwner(testValue);
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
     <dt>Test:</dt><dd>Call User::setOwner(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var042()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setOwner(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "owner");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setOwner(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var043()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*USRPRF";
	    try
	    {
                u.setOwner(testValue);
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
     <dt>Test:</dt><dd>Call User::setOwner(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var044()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*USRPRF";
	    try
	    {
                u.setOwner(testValue);
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
     <dt>Test:</dt><dd>Call User::getPasswordExpirationInterval() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var045()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int expectedValue = 0;
            int returnValue = u.getPasswordExpirationInterval();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(int) with a valid parameter of -1.</dd>
     <dt>Result:</dt><dd>Verify User::getPasswordExpirationInterval() returns -1.</dd>
     </dl>
     **/
    public void Var046()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = -1;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getPasswordExpirationInterval();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) with a valid parameter of *NOMAX.</dd>
     <dt>Result:</dt><dd>Verify User::getPasswordExpirationInterval() returns -1.</dd>
     </dl>
     **/
    public void Var047()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NOMAX";
            int expectedValue = -1;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getPasswordExpirationInterval();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(int) with a valid parameter of 0.</dd>
     <dt>Result:</dt><dd>Verify User::getPasswordExpirationInterval() returns 0.</dd>
     </dl>
     **/
    public void Var048()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 0;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getPasswordExpirationInterval();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) with a valid parameter of "*SYSVAL".</dd>
     <dt>Result:</dt><dd>Verify User::getPasswordExpirationInterval() returns 0.</dd>
     </dl>
     **/
    public void Var049()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            int expectedValue = 0;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getPasswordExpirationInterval();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(int) with the valid parameters of the range 1-366.</dd>
     <dt>Result:</dt><dd>Verify User::getPasswordExpirationInterval() returns the values.</dd>
     </dl>
     **/
    public void Var050()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            for (int testValue = 1; testValue <= 366; ++testValue)
            {
                u.setPasswordExpirationInterval(testValue);
                int returnValue = u.getPasswordExpirationInterval();
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
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) with the valid parameters of the range 1-366.</dd>
     <dt>Result:</dt><dd>Verify User::getPasswordExpirationInterval() returns the values.</dd>
     </dl>
     **/
    public void Var051()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            for (int testValue = 1; testValue <= 366; ++testValue)
            {
                u.setPasswordExpirationInterval(Integer.toString(testValue));
                int returnValue = u.getPasswordExpirationInterval();
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
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getPasswordExpirationInterval() returns 0.</dd>
     </dl>
     **/
    public void Var052()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            int expectedValue = 0;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getPasswordExpirationInterval();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getPasswordExpirationInterval() returns 0.</dd>
     </dl>
     **/
    public void Var053()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            int expectedValue = 0;
            u.setPasswordExpirationInterval(testValue);
            int returnValue = u.getPasswordExpirationInterval();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(int) with a not valid value parameter of -2.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var054()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = -2;
            try
            {
                u.setPasswordExpirationInterval(testValue);
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
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) with a not valid value parameter of "-2".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var055()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "-2";
            try
            {
                u.setPasswordExpirationInterval(testValue);
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
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) with a not valid value parameter of "-1".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var056()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "-1";
            try
            {
                u.setPasswordExpirationInterval(testValue);
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
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) with a not valid value parameter of "0".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var057()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "0";
            try
            {
                u.setPasswordExpirationInterval(testValue);
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
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(int) with a not valid value parameter of 367.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var058()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 367;
            try
            {
                u.setPasswordExpirationInterval(testValue);
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
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) with a not valid value parameter of "367".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var059()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "367";
            try
            {
                u.setPasswordExpirationInterval(testValue);
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
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) with a not valid value parameter of "asdf".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var060()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "asdf";
            try
            {
                u.setPasswordExpirationInterval(testValue);
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
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var061()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setPasswordExpirationInterval(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "passwordExpirationInterval");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(int) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var062()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            int testValue = 37;
	    try
	    {
                u.setPasswordExpirationInterval(testValue);
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
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var063()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "37";
	    try
	    {
                u.setPasswordExpirationInterval(testValue);
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
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(int) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var064()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            int testValue = 37;
	    try
	    {
                u.setPasswordExpirationInterval(testValue);
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
     <dt>Test:</dt><dd>Call User::setPasswordExpirationInterval(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var065()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "37";
	    try
	    {
                u.setPasswordExpirationInterval(testValue);
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
     <dt>Test:</dt><dd>Call User::isPasswordSetExpire() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var066()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            boolean expectedValue = false;
            boolean returnValue = u.isPasswordSetExpire();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPasswordSetExpire(boolean) with a valid parameter of true.</dd>
     <dt>Result:</dt><dd>Verify User::isPasswordSetExpire() returns true.</dd>
     </dl>
     **/
    public void Var067()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            boolean testValue = true;
            u.setPasswordSetExpire(testValue);
            boolean returnValue = u.isPasswordSetExpire();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPasswordSetExpire(boolean) with a valid parameter of false.</dd>
     <dt>Result:</dt><dd>Verify User::isPasswordSetExpire() returns false.</dd>
     </dl>
     **/
    public void Var068()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            boolean testValue = false;
            u.setPasswordSetExpire(testValue);
            boolean returnValue = u.isPasswordSetExpire();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPasswordSetExpire(boolean) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var069()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            boolean testValue = true;
	    try
	    {
                u.setPasswordSetExpire(testValue);
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
     <dt>Test:</dt><dd>Call User::setPasswordSetExpire(boolean) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var070()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            boolean testValue = true;
	    try
	    {
                u.setPasswordSetExpire(testValue);
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
     <dt>Test:</dt><dd>Call User::getPrintDevice() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var071()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*WRKSTN";
            String returnValue = u.getPrintDevice();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPrintDevice(String) with a valid parameter of *WRKSTN.</dd>
     <dt>Result:</dt><dd>Verify User::getPrintDevice() returns *WRKSTN.</dd>
     </dl>
     **/
    public void Var072()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*WRKSTN";
            u.setPrintDevice(testValue);
            String returnValue = u.getPrintDevice();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPrintDevice(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getPrintDevice() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var073()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            u.setPrintDevice(testValue);
            String returnValue = u.getPrintDevice();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPrintDevice(String) with a valid name parameter.</dd>
     <dt>Result:</dt><dd>Verify User::getPrintDevice() returns the name.</dd>
     </dl>
     **/
    public void Var074()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "MYPRINTER";
            u.setPrintDevice(testValue);
            String returnValue = u.getPrintDevice();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPrintDevice(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getPrintDevice() returns *WRKSTN.</dd>
     </dl>
     **/
    public void Var075()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*WRKSTN";
            u.setPrintDevice(testValue);
            String returnValue = u.getPrintDevice();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPrintDevice(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getPrintDevice() returns *WRKSTN.</dd>
     </dl>
     **/
    public void Var076()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*WRKSTN";
            u.setPrintDevice(testValue);
            String returnValue = u.getPrintDevice();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPrintDevice(String) with a not valid star value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var077()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setPrintDevice(testValue);
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
     <dt>Test:</dt><dd>Call User::setPrintDevice(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var078()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "ELEVENISBIG";
            try
            {
                u.setPrintDevice(testValue);
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
     <dt>Test:</dt><dd>Call User::setPrintDevice(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var079()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setPrintDevice(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "printDevice");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setPrintDevice(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var080()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*WRKSTN";
	    try
	    {
                u.setPrintDevice(testValue);
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
     <dt>Test:</dt><dd>Call User::setPrintDevice(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var081()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*WRKSTN";
	    try
	    {
                u.setPrintDevice(testValue);
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
    <dt>Test:</dt><dd>Call User::getPasswordChangeBlock before setting.</dd>
    <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
    </dl>
    **/
    public void Var082()
    {
    	try
    	{
    		User u = new User(pwrSys_, sandbox_.createUser());
            String pwdChangeBlock = u.getPasswordChangeBlock();
            String expected = "*SYSVAL";
            if(pwrSys_.getVRM() < 0x00050500)
            	assertCondition(pwdChangeBlock == null);
            else
            	assertCondition(pwdChangeBlock.equals(expected));
    	}
    	catch (Exception e)
    	{
    	    failed(e, "Unexpected exception.");
    	}
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with a valid parameter of *SYSVAL.</dd>
    <dt>Result:</dt><dd>Verify User::getPasswordChangeBlock() returns *SYSVAL.</dd>
    </dl>
    **/
    public void Var083()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            u.setPasswordChangeBlock(testValue);
            if(pwrSys_.getVRM() < 0x00050500)
            	failed("No exception.");
            String returnValue = u.getPasswordChangeBlock();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
        	try{
        		if(pwrSys_.getVRM() < 0x00050500)
        			assertExceptionStartsWith(e, "AS400Exception", "CPD0043 ", ErrorCompletingRequestException.AS400_ERROR);
        		else 
        			failed(e, "Unexpected Exception");
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with a valid parameter of *NONE.</dd>
    <dt>Result:</dt><dd>Verify User::getPasswordChangeBlock() returns *NONE.</dd>
    </dl>
    **/
    public void Var084()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NONE";
            u.setPasswordChangeBlock(testValue);
            if(pwrSys_.getVRM() < 0x00050500)
            	failed("No exception.");
            String returnValue = u.getPasswordChangeBlock();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
        	try{
        		if(pwrSys_.getVRM() < 0x00050500)
        			assertExceptionStartsWith(e, "AS400Exception", "CPD0043 ", ErrorCompletingRequestException.AS400_ERROR);
        		else 
        			failed(e, "Unexpected Exception");
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with a valid parameter of 1.</dd>
    <dt>Result:</dt><dd>Verify User::getPasswordChangeBlock() returns 1.</dd>
    </dl>
    **/
    public void Var085()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "1";
            u.setPasswordChangeBlock(testValue);
            if(pwrSys_.getVRM() < 0x00050500)
            	failed("No exception.");
            String returnValue = u.getPasswordChangeBlock();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
        	try{
        		if(pwrSys_.getVRM() < 0x00050500)
        			assertExceptionStartsWith(e, "AS400Exception", "CPD0043 ", ErrorCompletingRequestException.AS400_ERROR);
        		else 
        			failed(e, "Unexpected Exception");
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with a valid parameter of 7.</dd>
    <dt>Result:</dt><dd>Verify User::getPasswordChangeBlock() returns 7.</dd>
    </dl>
    **/
    public void Var086()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "7";
            u.setPasswordChangeBlock(testValue);
            if(pwrSys_.getVRM() < 0x00050500)
            	failed("No exception.");
            String returnValue = u.getPasswordChangeBlock();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
        	try{
        		if(pwrSys_.getVRM() < 0x00050500)
        			assertExceptionStartsWith(e, "AS400Exception", "CPD0043 ", ErrorCompletingRequestException.AS400_ERROR);
        		else 
        			failed(e, "Unexpected Exception");
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with a valid parameter of 67.</dd>
    <dt>Result:</dt><dd>Verify User::getPasswordChangeBlock() returns 67.</dd>
    </dl>
    **/
    public void Var087()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "67";
            u.setPasswordChangeBlock(testValue);
            if(pwrSys_.getVRM() < 0x00050500)
            	failed("No exception.");
            String returnValue = u.getPasswordChangeBlock();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
        	try{
        		if(pwrSys_.getVRM() < 0x00050500)
        			assertExceptionStartsWith(e, "AS400Exception", "CPD0043 ", ErrorCompletingRequestException.AS400_ERROR);
        		else 
        			failed(e, "Unexpected Exception");
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with a valid parameter of 99.</dd>
    <dt>Result:</dt><dd>Verify User::getPasswordChangeBlock() returns 99.</dd>
    </dl>
    **/
    public void Var088()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "99";
            u.setPasswordChangeBlock(testValue);
            if(pwrSys_.getVRM() < 0x00050500)
            	failed("No exception.");
            String returnValue = u.getPasswordChangeBlock();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
        	try{
        		if(pwrSys_.getVRM() < 0x00050500)
        			assertExceptionStartsWith(e, "AS400Exception", "CPD0043 ", ErrorCompletingRequestException.AS400_ERROR);
        		else 
        			failed(e, "Unexpected Exception");
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with a valid parameter of 2.</dd>
    <dt>Result:</dt><dd>Verify User::getPasswordChangeBlock() returns 2.</dd>
    </dl>
    **/
    public void Var089()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "2";
            u.setPasswordChangeBlock(testValue);
            if(pwrSys_.getVRM() < 0x00050500)
            	failed("No exception.");
            String returnValue = u.getPasswordChangeBlock();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
        	try{
        		if(pwrSys_.getVRM() < 0x00050500)
        			assertExceptionStartsWith(e, "AS400Exception", "CPD0043 ", ErrorCompletingRequestException.AS400_ERROR);
        		else 
        			failed(e, "Unexpected Exception");
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with a valid parameter of 98.</dd>
    <dt>Result:</dt><dd>Verify User::getPasswordChangeBlock() returns 98.</dd>
    </dl>
    **/
    public void Var090()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "98";
            u.setPasswordChangeBlock(testValue);
            if(pwrSys_.getVRM() < 0x00050500)
            	failed("No exception.");
            String returnValue = u.getPasswordChangeBlock();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
        	try{
        		if(pwrSys_.getVRM() < 0x00050500)
        			assertExceptionStartsWith(e, "AS400Exception", "CPD0043 ", ErrorCompletingRequestException.AS400_ERROR);
        		else 
        			failed(e, "Unexpected Exception");
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with an invalid parameter of MYVAL.</dd>
    <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
    </dl>
    **/
    public void Var091()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "MYVAL";
            u.setPasswordChangeBlock(testValue);
           	failed("No exception.");
        }
        catch (Exception e)
        {
        	try{
        		assertExceptionStartsWith(e, "AS400Exception", (pwrSys_.getVRM() < 0x00050500) ? "CPD0043" : "CPD0076 ", ErrorCompletingRequestException.AS400_ERROR);
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with an invalid parameter that is too long.</dd>
    <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
    </dl>
    **/
    public void Var092()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "MYBADVALUEISBAD";
            u.setPasswordChangeBlock(testValue);
           	failed("No exception.");
        }
        catch (Exception e)
        {
        	try{
        		assertExceptionStartsWith(e, "AS400Exception", (pwrSys_.getVRM() < 0x00050500) ? "CPD0043" : "CPD0076 ", ErrorCompletingRequestException.AS400_ERROR);
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with an invalid parameter of 0.</dd>
    <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
    </dl>
    **/
    public void Var093()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "0";
            u.setPasswordChangeBlock(testValue);
           	failed("No exception.");
        }
        catch (Exception e)
        {
        	try{
        		assertExceptionStartsWith(e, "AS400Exception", (pwrSys_.getVRM() < 0x00050500) ? "CPD0043" : "CPD0085 ", ErrorCompletingRequestException.AS400_ERROR);
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with an invalid parameter of -5.</dd>
    <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
    </dl>
    **/
    public void Var094()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "-5";
            u.setPasswordChangeBlock(testValue);
           	failed("No exception.");
        }
        catch (Exception e)
        {
        	try{
        		assertExceptionStartsWith(e, "AS400Exception", (pwrSys_.getVRM() < 0x00050500) ? "CPD0043" : "CPD0085 ", ErrorCompletingRequestException.AS400_ERROR);
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with an invalid parameter of 100.</dd>
    <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
    </dl>
    **/
    public void Var095()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "100";
            u.setPasswordChangeBlock(testValue);
           	failed("No exception.");
        }
        catch (Exception e)
        {
        	try{
        		assertExceptionStartsWith(e, "AS400Exception", (pwrSys_.getVRM() < 0x00050500) ? "CPD0043" : "CPD0085 ", ErrorCompletingRequestException.AS400_ERROR);
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with an invalid parameter of 111.</dd>
    <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
    </dl>
    **/
    public void Var096()
    {
    	try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "111";
            u.setPasswordChangeBlock(testValue);
           	failed("No exception.");
        }
        catch (Exception e)
        {
        	try{
        		assertExceptionStartsWith(e, "AS400Exception", (pwrSys_.getVRM() < 0x00050500) ? "CPD0043" : "CPD0085 ", ErrorCompletingRequestException.AS400_ERROR);
        	}catch(Exception e1){
        		failed(e1, "Unexpected Exception");
        	}
        }
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with a null parameter.</dd>
    <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
    </dl>
    **/
    public void Var097()
    {
    	try{
    		User u = new User(pwrSys_, sandbox_.createUser());
    		u.setPasswordChangeBlock(null);
    		failed("No exception.");
    	}
    	catch(Exception e){
    		assertExceptionIs(e, "NullPointerException", "pwdChangeBlock");
    	}
    }
    
    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) before name is set.</dd>
    <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
    </dl>
    **/
   public void Var098()
   {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
           String testValue = "*SYSVAL";
	    try
	    {
           u.setPasswordChangeBlock(testValue);
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
   <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) before system is set.</dd>
   <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
   </dl>
   **/
  public void Var099()
  {
	try
	{
	    User u = new User();
	    u.setName("MYNAME");
        String testValue = "*SYSVAL";
	    try
	    {
          u.setPasswordChangeBlock(testValue);
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
  <dt>Test:</dt><dd>Call User::setPasswordChangeBlock(String) with an invalid parameter of 111222333444.</dd>
  <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
  </dl>
  **/
  public void Var100()
  {
  	try
      {
          User u = new User(pwrSys_, sandbox_.createUser());
          String testValue = "111222333444";
          u.setPasswordChangeBlock(testValue);
         	failed("No exception.");
      }
      catch (Exception e)
      {
      	try{
      		assertExceptionStartsWith(e, "AS400Exception", (pwrSys_.getVRM() < 0x00050500) ? "CPD0043" : "CPD0095 ", ErrorCompletingRequestException.AS400_ERROR);
      	}catch(Exception e1){
      		failed(e1, "Unexpected Exception");
      	}
      }
  }
}
