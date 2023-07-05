///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserGetSetAtoCTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.IllegalPathNameException;
import com.ibm.as400.access.User;

/**
 Testcase UserGetSetTestcase.  This tests the get and set methods of the User class.
 **/
public class UserGetSetAtoCTestcase extends Testcase
{
    private UserSandbox sandbox_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "UGSACT");
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
     <dt>Test:</dt><dd>Call User::getAccountingCode() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var001()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "";
            String returnValue = u.getAccountingCode();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAccountingCode(String) with a valid parameter of *BLANK.</dd>
     <dt>Result:</dt><dd>Verify User::getAccountingCode() returns an empty string.</dd>
     </dl>
     **/
    public void Var002()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*BLANK";
            String expectedValue = "";
            u.setAccountingCode(testValue);
            String returnValue = u.getAccountingCode();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAccountingCode(String) with a valid parameter of "123xyz".</dd>
     <dt>Result:</dt><dd>Verify User::getAccountingCode() returns "123xyz".</dd>
     </dl>
     **/
    public void Var003()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "123xyz";
            u.setAccountingCode(testValue);
            String returnValue = u.getAccountingCode();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAccountingCode(String) with a valid parameter of empty string "".</dd>
     <dt>Result:</dt><dd>Verify User::getAccountingCode() returns "".</dd>
     </dl>
     **/
    public void Var004()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            u.setAccountingCode(testValue);
            String returnValue = u.getAccountingCode();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAccountingCode(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getAccountingCode() returns "".</dd>
     </dl>
     **/
    public void Var005()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "";
            u.setAccountingCode(testValue);
            String returnValue = u.getAccountingCode();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAccountingCode(String) with a valid max length parameter of "abcdefghijlmnop".</dd>
     <dt>Result:</dt><dd>Verify User::getAccountingCode() returns "abcdefghijlmnop".</dd>
     </dl>
     **/
    public void Var006()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "abcdefghijlmnop";
            u.setAccountingCode(testValue);
            String returnValue = u.getAccountingCode();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAccountingCode(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var007()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "abcdefghijlmnopq";
            try
            {
                u.setAccountingCode(testValue);
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
     <dt>Test:</dt><dd>Call User::setAccountingCode(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var008()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setAccountingCode(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "accountingCode");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAccountingCode(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var009()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "123xyz";
	    try
	    {
                u.setAccountingCode(testValue);
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
     <dt>Test:</dt><dd>Call User::setAccountingCode(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var010()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "123xyz";
	    try
	    {
                u.setAccountingCode(testValue);
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
     <dt>Test:</dt><dd>Call User::getAssistanceLevel() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var011()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*SYSVAL";
            String returnValue = u.getAssistanceLevel();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAssistanceLevel(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getAssistanceLevel() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var012()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            u.setAssistanceLevel(testValue);
            String returnValue = u.getAssistanceLevel();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAssistanceLevel(String) with a valid parameter of *BASIC.</dd>
     <dt>Result:</dt><dd>Verify User::getAssistanceLevel() returns *BASIC.</dd>
     </dl>
     **/
    public void Var013()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*BASIC";
            u.setAssistanceLevel(testValue);
            String returnValue = u.getAssistanceLevel();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAssistanceLevel(String) with a valid parameter of *INTERMED.</dd>
     <dt>Result:</dt><dd>Verify User::getAssistanceLevel() returns *INTERMED.</dd>
     </dl>
     **/
    public void Var014()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INTERMED";
            u.setAssistanceLevel(testValue);
            String returnValue = u.getAssistanceLevel();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAssistanceLevel(String) with a valid parameter of *ADVANCED.</dd>
     <dt>Result:</dt><dd>Verify User::getAssistanceLevel() returns *ADVANCED.</dd>
     </dl>
     **/
    public void Var015()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*ADVANCED";
            u.setAssistanceLevel(testValue);
            String returnValue = u.getAssistanceLevel();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAssistanceLevel(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getAssistanceLevel() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var016()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*SYSVAL";
            u.setAssistanceLevel(testValue);
            String returnValue = u.getAssistanceLevel();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAssistanceLevel(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getAssistanceLevel() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var017()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*SYSVAL";
            u.setAssistanceLevel(testValue);
            String returnValue = u.getAssistanceLevel();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAssistanceLevel(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var018()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setAssistanceLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::setAssistanceLevel(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var019()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setAssistanceLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::setAssistanceLevel(String) with a null parameter.</dd>
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
                u.setAssistanceLevel(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "assistanceLevel");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAssistanceLevel(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var021()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*ADVANCED";
	    try
	    {
                u.setAssistanceLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::setAssistanceLevel(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var022()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*ADVANCED";
	    try
	    {
                u.setAssistanceLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::getAttentionKeyHandlingProgram() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var023()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*SYSVAL";
            String returnValue = u.getAttentionKeyHandlingProgram();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getAttentionKeyHandlingProgram() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var024()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            u.setAttentionKeyHandlingProgram(testValue);
            String returnValue = u.getAttentionKeyHandlingProgram();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getAttentionKeyHandlingProgram() returns *NONE.</dd>
     </dl>
     **/
    public void Var025()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NONE";
            u.setAttentionKeyHandlingProgram(testValue);
            String returnValue = u.getAttentionKeyHandlingProgram();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a valid parameter of *ASSIST.</dd>
     <dt>Result:</dt><dd>Verify User::getAttentionKeyHandlingProgram() returns "/QSYS.LIB/QEZMAIN.PGM".</dd>
     </dl>
     **/
    public void Var026()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*ASSIST";
            String expectedValue = "/QSYS.LIB/QEZMAIN.PGM";
            u.setAttentionKeyHandlingProgram(testValue);
            String returnValue = u.getAttentionKeyHandlingProgram();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a valid parameter which is a path.</dd>
     <dt>Result:</dt><dd>Verify User::getAttentionKeyHandlingProgram() returns the path.</dd>
     </dl>
     **/
    public void Var027()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QCMD.PGM";
            u.setAttentionKeyHandlingProgram(testValue);
            String returnValue = u.getAttentionKeyHandlingProgram();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a valid parameter which is a mixed case path.</dd>
     <dt>Result:</dt><dd>Verify User::getAttentionKeyHandlingProgram() returns the path.</dd>
     </dl>
     **/
    public void Var028()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QsyS.LiB/QcmD.PgM";
            String expectedValue = "/QSYS.LIB/QCMD.PGM";
            u.setAttentionKeyHandlingProgram(testValue);
            String returnValue = u.getAttentionKeyHandlingProgram();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a valid parameter which is a path that does not exist.</dd>
     <dt>Result:</dt><dd>Verify User::getAttentionKeyHandlingProgram() returns the path.</dd>
     </dl>
     **/
    public void Var029()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/MYLIB.LIB/MYPGM.PGM";
            u.setAttentionKeyHandlingProgram(testValue);
            String returnValue = u.getAttentionKeyHandlingProgram();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getAttentionKeyHandlingProgram() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var030()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*SYSVAL";
            u.setAttentionKeyHandlingProgram(testValue);
            String returnValue = u.getAttentionKeyHandlingProgram();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getAttentionKeyHandlingProgram() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var031()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*SYSVAL";
            u.setAttentionKeyHandlingProgram(testValue);
            String returnValue = u.getAttentionKeyHandlingProgram();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var032()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setAttentionKeyHandlingProgram(testValue);
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
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var033()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setAttentionKeyHandlingProgram(testValue);
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
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var034()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QEZMAIN.MNU";
            try
            {
                u.setAttentionKeyHandlingProgram(testValue);
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
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var035()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/ELEVENISBIG.LIB/MYPGM.PGM";
            try
            {
                u.setAttentionKeyHandlingProgram(testValue);
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
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var036()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setAttentionKeyHandlingProgram(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "attentionKeyHandlingProgram");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var037()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*SYSVAL";
	    try
	    {
                u.setAttentionKeyHandlingProgram(testValue);
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
     <dt>Test:</dt><dd>Call User::setAttentionKeyHandlingProgram(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var038()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*SYSVAL";
	    try
	    {
                u.setAttentionKeyHandlingProgram(testValue);
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
     <dt>Test:</dt><dd>Call User::getCCSID() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var039()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int expectedValue = -2;
            int returnValue = u.getCCSID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(int) with a valid parameter of -2.</dd>
     <dt>Result:</dt><dd>Verify User::getCCSID() returns -2.</dd>
     </dl>
     **/
    public void Var040()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = -2;
            u.setCCSID(testValue);
            int returnValue = u.getCCSID();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getCCSID() returns -2.</dd>
     </dl>
     **/
    public void Var041()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            int expectedValue = -2;
            u.setCCSID(testValue);
            int returnValue = u.getCCSID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(int) with a valid parameter of 65535.</dd>
     <dt>Result:</dt><dd>Verify User::getCCSID() returns 65535.</dd>
     </dl>
     **/
    public void Var042()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 65535;
            u.setCCSID(testValue);
            int returnValue = u.getCCSID();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(String) with a valid parameter of *HEX.</dd>
     <dt>Result:</dt><dd>Verify User::getCCSID() returns 65535.</dd>
     </dl>
     **/
    public void Var043()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*HEX";
            int expectedValue = 65535;
            u.setCCSID(testValue);
            int returnValue = u.getCCSID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(String) with a valid parameter of "65535".</dd>
     <dt>Result:</dt><dd>Verify User::getCCSID() returns 65535.</dd>
     </dl>
     **/
    public void Var044()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "65535";
            int expectedValue = 65535;
            u.setCCSID(testValue);
            int returnValue = u.getCCSID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(int) with a valid parameter of 37.</dd>
     <dt>Result:</dt><dd>Verify User::getCCSID() returns 37.</dd>
     </dl>
     **/
    public void Var045()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 37;
            u.setCCSID(testValue);
            int returnValue = u.getCCSID();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(String) with a valid parameter of "37".</dd>
     <dt>Result:</dt><dd>Verify User::getCCSID() returns 37.</dd>
     </dl>
     **/
    public void Var046()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "37";
            int expectedValue = 37;
            u.setCCSID(testValue);
            int returnValue = u.getCCSID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getCCSID() returns -2.</dd>
     </dl>
     **/
    public void Var047()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            int expectedValue = -2;
            u.setCCSID(testValue);
            int returnValue = u.getCCSID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getCCSID() returns -2.</dd>
     </dl>
     **/
    public void Var048()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            int expectedValue = -2;
            u.setCCSID(testValue);
            int returnValue = u.getCCSID();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(int) with a not valid value parameter of -1.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var049()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = -1;
            try
            {
                u.setCCSID(testValue);
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
     <dt>Test:</dt><dd>Call User::setCCSID(String) with a not valid value parameter of "-1".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var050()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "-1";
            try
            {
                u.setCCSID(testValue);
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
     <dt>Test:</dt><dd>Call User::setCCSID(int) with a not valid value parameter of 0.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var051()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 0;
            try
            {
                u.setCCSID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF22F1 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(String) with a not valid value parameter of "0".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var052()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "0";
            try
            {
                u.setCCSID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF22F1 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(int) with a not valid value parameter of 1.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var053()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 1;
            try
            {
                u.setCCSID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF22F1 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(String) with a not valid value parameter of "1".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var054()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "1";
            try
            {
                u.setCCSID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF22F1 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(int) with a not valid value parameter of 65536.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var055()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = 65536;
            try
            {
                u.setCCSID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF22F1 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(String) with a not valid value parameter of "65536".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var056()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "65536";
            try
            {
                u.setCCSID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF22F1 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(String) with a not valid value parameter of "asdf".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var057()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "asdf";
            try
            {
                u.setCCSID(testValue);
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
     <dt>Test:</dt><dd>Call User::setCCSID(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var058()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setCCSID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "ccsid");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCCSID(int) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var059()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            int testValue = 37;
	    try
	    {
                u.setCCSID(testValue);
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
     <dt>Test:</dt><dd>Call User::setCCSID(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var060()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "37";
	    try
	    {
                u.setCCSID(testValue);
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
     <dt>Test:</dt><dd>Call User::setCCSID(int) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var061()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            int testValue = 37;
	    try
	    {
                u.setCCSID(testValue);
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
     <dt>Test:</dt><dd>Call User::setCCSID(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var062()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "37";
	    try
	    {
                u.setCCSID(testValue);
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
     <dt>Test:</dt><dd>Call User::getCHRIDControl() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var063()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*SYSVAL";
            String returnValue = u.getCHRIDControl();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCHRIDControl(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getCHRIDControl() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var064()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            u.setCHRIDControl(testValue);
            String returnValue = u.getCHRIDControl();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCHRIDControl(String) with a valid parameter of *DEVD.</dd>
     <dt>Result:</dt><dd>Verify User::getCHRIDControl() returns *DEVD.</dd>
     </dl>
     **/
    public void Var065()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*DEVD";
            u.setCHRIDControl(testValue);
            String returnValue = u.getCHRIDControl();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCHRIDControl(String) with a valid parameter of *JOBCCSID.</dd>
     <dt>Result:</dt><dd>Verify User::getCHRIDControl() returns *JOBCCSID.</dd>
     </dl>
     **/
    public void Var066()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*JOBCCSID";
            u.setCHRIDControl(testValue);
            String returnValue = u.getCHRIDControl();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCHRIDControl(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getCHRIDControl() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var067()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*SYSVAL";
            u.setCHRIDControl(testValue);
            String returnValue = u.getCHRIDControl();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCHRIDControl(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getCHRIDControl() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var068()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*SYSVAL";
            u.setCHRIDControl(testValue);
            String returnValue = u.getCHRIDControl();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCHRIDControl(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var069()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setCHRIDControl(testValue);
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
     <dt>Test:</dt><dd>Call User::setCHRIDControl(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var070()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setCHRIDControl(testValue);
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
     <dt>Test:</dt><dd>Call User::setCHRIDControl(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var071()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setCHRIDControl(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "chridControl");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCHRIDControl(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var072()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*SYSVAL";
	    try
	    {
                u.setCHRIDControl(testValue);
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
     <dt>Test:</dt><dd>Call User::setCHRIDControl(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var073()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*SYSVAL";
	    try
	    {
                u.setCHRIDControl(testValue);
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
     <dt>Test:</dt><dd>Call User::getCountryID() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var074()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*SYSVAL";
            String returnValue = u.getCountryID();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCountryID(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getCountryID() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var075()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            u.setCountryID(testValue);
            String returnValue = u.getCountryID();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCountryID(String) with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify User::getCountryID() returns the parameter.</dd>
     </dl>
     **/
    public void Var076()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "GB";
            u.setCountryID(testValue);
            String returnValue = u.getCountryID();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCountryID(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getCountryID() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var077()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*SYSVAL";
            u.setCountryID(testValue);
            String returnValue = u.getCountryID();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCountryID(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getCountryID() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var078()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*SYSVAL";
            u.setCountryID(testValue);
            String returnValue = u.getCountryID();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCountryID(String) with a not valid star value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var079()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setCountryID(testValue);
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
     <dt>Test:</dt><dd>Call User::setCountryID(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var080()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "JT";
            try
            {
                u.setCountryID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF3FC1 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCountryID(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var081()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "ThisCountryNameIsWayTooLong";
            try
            {
                u.setCountryID(testValue);
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
     <dt>Test:</dt><dd>Call User::setCountryID(String) with a not valid over length with spaces parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var082()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "This Country Name Is Way Too Long";
            try
            {
                u.setCountryID(testValue);
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
     <dt>Test:</dt><dd>Call User::setCountryID(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var083()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setCountryID(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "countryID");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCountryID(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var084()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*SYSVAL";
	    try
	    {
                u.setCountryID(testValue);
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
     <dt>Test:</dt><dd>Call User::setCountryID(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var085()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*SYSVAL";
	    try
	    {
                u.setCountryID(testValue);
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
     <dt>Test:</dt><dd>Call User::getCurrentLibraryName() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var086()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*CRTDFT";
            String returnValue = u.getCurrentLibraryName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCurrentLibraryName(String) with a valid parameter of *CRTDFT.</dd>
     <dt>Result:</dt><dd>Verify User::getCurrentLibraryName() returns *CRTDFT.</dd>
     </dl>
     **/
    public void Var087()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*CRTDFT";
            u.setCurrentLibraryName(testValue);
            String returnValue = u.getCurrentLibraryName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCurrentLibraryName(String) with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify User::getCurrentLibraryName() returns the parameter.</dd>
     </dl>
     **/
    public void Var088()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "QIWS";
            u.setCurrentLibraryName(testValue);
            String returnValue = u.getCurrentLibraryName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCurrentLibraryName(String) with a valid varying case parameter.</dd>
     <dt>Result:</dt><dd>Verify User::getCurrentLibraryName() returns the parameter.</dd>
     </dl>
     **/
    public void Var089()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "QiWs";
            String expectedValue = "QIWS";
            u.setCurrentLibraryName(testValue);
            String returnValue = u.getCurrentLibraryName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCurrentLibraryName(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getCurrentLibraryName() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var090()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*CRTDFT";
            u.setCurrentLibraryName(testValue);
            String returnValue = u.getCurrentLibraryName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCurrentLibraryName(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getCurrentLibraryName() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var091()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*CRTDFT";
            u.setCurrentLibraryName(testValue);
            String returnValue = u.getCurrentLibraryName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCurrentLibraryName(String) with a not valid star value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var092()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setCurrentLibraryName(testValue);
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
     <dt>Test:</dt><dd>Call User::setCurrentLibraryName(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var093()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "ELEVENISBIG";
            try
            {
                u.setCurrentLibraryName(testValue);
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
     <dt>Test:</dt><dd>Call User::setCurrentLibraryName(String) with a not valid over length with spaces parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var094()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "This Name Is Way Too Long";
            try
            {
                u.setCurrentLibraryName(testValue);
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
     <dt>Test:</dt><dd>Call User::setCurrentLibraryName(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var095()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setCurrentLibraryName(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "currentLibraryName");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setCurrentLibraryName(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var096()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*SYSVAL";
	    try
	    {
                u.setCurrentLibraryName(testValue);
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
     <dt>Test:</dt><dd>Call User::setCurrentLibraryName(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var097()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*SYSVAL";
	    try
	    {
                u.setCurrentLibraryName(testValue);
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
