///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserGetSetRtoSTestcase.java
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
import test.UserTest;

/**
 Testcase UserGetSetTestcase.  This tests the get and set methods of the User class.
 **/
public class UserGetSetRtoSTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserGetSetRtoSTestcase";
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
        sandbox_ = new UserSandbox(pwrSys_, "UGSRS", UserTest.COLLECTION.substring(UserTest.COLLECTION.length() - 1));
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
     <dt>Test:</dt><dd>Call User::getSortSequenceTable() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var001()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*SYSVAL";
            String returnValue = u.getSortSequenceTable();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getSortSequenceTable() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var002()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            u.setSortSequenceTable(testValue);
            String returnValue = u.getSortSequenceTable();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a valid parameter of *HEX.</dd>
     <dt>Result:</dt><dd>Verify User::getSortSequenceTable() returns *HEX.</dd>
     </dl>
     **/
    public void Var003()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*HEX";
            u.setSortSequenceTable(testValue);
            String returnValue = u.getSortSequenceTable();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a valid parameter of *LANGIDUNQ.</dd>
     <dt>Result:</dt><dd>Verify User::getSortSequenceTable() returns *LANGIDUNQ.</dd>
     </dl>
     **/
    public void Var004()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*LANGIDUNQ";
            u.setSortSequenceTable(testValue);
            String returnValue = u.getSortSequenceTable();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a valid parameter of *LANGIDSHR.</dd>
     <dt>Result:</dt><dd>Verify User::getSortSequenceTable() returns *LANGIDSHR.</dd>
     </dl>
     **/
    public void Var005()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*LANGIDSHR";
            u.setSortSequenceTable(testValue);
            String returnValue = u.getSortSequenceTable();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a valid parameter which is a path.</dd>
     <dt>Result:</dt><dd>Verify User::getSortSequenceTable() returns the path.</dd>
     </dl>
     **/
    public void Var006()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE";
            u.setSortSequenceTable(testValue);
            String returnValue = u.getSortSequenceTable();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a valid parameter which is a mixed case path.</dd>
     <dt>Result:</dt><dd>Verify User::getSortSequenceTable() returns the path.</dd>
     </dl>
     **/
    public void Var007()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSyS.LiB/QiWS.LiB/QCuSTCDT.FiLe";
            String expectedValue = "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE";
            u.setSortSequenceTable(testValue);
            String returnValue = u.getSortSequenceTable();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a valid parameter which is a path that does not exist.</dd>
     <dt>Result:</dt><dd>Verify User::getSortSequenceTable() returns the path.</dd>
     </dl>
     **/
    public void Var008()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/NOTEXIST.LIB/NOTEXIST.FILE";
            u.setSortSequenceTable(testValue);
            String returnValue = u.getSortSequenceTable();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getSortSequenceTable() returns the path.</dd>
     </dl>
     **/
    public void Var009()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*SYSVAL";
            u.setSortSequenceTable(testValue);
            String returnValue = u.getSortSequenceTable();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getSortSequenceTable() returns the path.</dd>
     </dl>
     **/
    public void Var010()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*SYSVAL";
            u.setSortSequenceTable(testValue);
            String returnValue = u.getSortSequenceTable();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var011()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setSortSequenceTable(testValue);
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
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var012()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setSortSequenceTable(testValue);
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
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var013()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/QGPL.LIB/QPRINT.MSGQ";
            try
            {
                u.setSortSequenceTable(testValue);
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
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an IllegalPathNameException is thrown.</dd>
     </dl>
     **/
    public void Var014()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "/QSYS.LIB/ELEVENISBIG.LIB/QPRINT.OUTQ";
            try
            {
                u.setSortSequenceTable(testValue);
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
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var015()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setSortSequenceTable(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "sortSequenceTable");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var016()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*SYSVAL";
	    try
	    {
                u.setSortSequenceTable(testValue);
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
     <dt>Test:</dt><dd>Call User::setSortSequenceTable(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var017()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*SYSVAL";
	    try
	    {
                u.setSortSequenceTable(testValue);
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
     <dt>Test:</dt><dd>Call User::getSpecialAuthority() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var018()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] expectedValue = new String[0];
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of *USRCLS.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns a zero length array.</dd>
     </dl>
     **/
    public void Var019()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*USRCLS" };
            String[] expectedValue = new String[0];
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns a zero length array.</dd>
     </dl>
     **/
    public void Var020()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*NONE" };
            String[] expectedValue = new String[0];
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of *ALLOBJ.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns *ALLOBJ.</dd>
     </dl>
     **/
    public void Var021()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*ALLOBJ" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of *SECADM.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns *SECADM.</dd>
     </dl>
     **/
    public void Var022()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SECADM" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of *JOBCTL.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns *JOBCTL.</dd>
     </dl>
     **/
    public void Var023()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*JOBCTL" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of *SPLCTL.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns *SPLCTL.</dd>
     </dl>
     **/
    public void Var024()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SPLCTL" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of *SAVSYS.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns *SAVSYS.</dd>
     </dl>
     **/
    public void Var025()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SAVSYS" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of *SERVICE.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns *SERVICE.</dd>
     </dl>
     **/
    public void Var026()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SERVICE" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of *AUDIT.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns *AUDIT.</dd>
     </dl>
     **/
    public void Var027()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*AUDIT" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of *IOSYSCFG.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns *IOSYSCFG.</dd>
     </dl>
     **/
    public void Var028()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*IOSYSCFG" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of length 2.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns the values.</dd>
     </dl>
     **/
    public void Var029()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SECADM", "*ALLOBJ" };
            String[] expectedValue = new String[] { "*ALLOBJ", "*SECADM" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of length 3.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns the values.</dd>
     </dl>
     **/
    public void Var030()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SAVSYS", "*SPLCTL", "*JOBCTL" };
            String[] expectedValue = new String[] { "*JOBCTL", "*SPLCTL", "*SAVSYS" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of length 4.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns the values.</dd>
     </dl>
     **/
    public void Var031()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SERVICE", "*ALLOBJ", "*IOSYSCFG", "*AUDIT" };
            String[] expectedValue = new String[] { "*ALLOBJ", "*SERVICE", "*AUDIT", "*IOSYSCFG" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of length 4 with a duplicate.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns the 3 unique values.</dd>
     </dl>
     **/
    public void Var032()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SERVICE", "*SECADM", "*SECADM", "*JOBCTL" };
            String[] expectedValue = new String[] { "*SECADM", "*JOBCTL", "*SERVICE" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of length 5.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns the values.</dd>
     </dl>
     **/
    public void Var033()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SPLCTL", "*SAVSYS", "*SERVICE", "*JOBCTL", "*SECADM" };
            String[] expectedValue = new String[] { "*SECADM", "*JOBCTL", "*SPLCTL", "*SAVSYS", "*SERVICE" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of length 6.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns the values.</dd>
     </dl>
     **/
    public void Var034()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*JOBCTL", "*SPLCTL", "*SAVSYS", "*SERVICE", "*ALLOBJ", "*SECADM" };
            String[] expectedValue = new String[] { "*ALLOBJ", "*SECADM", "*JOBCTL", "*SPLCTL", "*SAVSYS", "*SERVICE" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of length 7.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns the values.</dd>
     </dl>
     **/
    public void Var035()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*JOBCTL", "*AUDIT", "*SPLCTL", "*IOSYSCFG", "*SERVICE", "*ALLOBJ", "*SECADM" };
            String[] expectedValue = new String[] { "*ALLOBJ", "*SECADM", "*JOBCTL", "*SPLCTL", "*SERVICE", "*AUDIT", "*IOSYSCFG" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a valid parameter of length 8.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns the values.</dd>
     </dl>
     **/
    public void Var036()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*JOBCTL", "*AUDIT", "*SPLCTL", "*SAVSYS", "*IOSYSCFG", "*SERVICE", "*ALLOBJ", "*SECADM" };
            String[] expectedValue = new String[] { "*ALLOBJ", "*SECADM", "*JOBCTL", "*SPLCTL", "*SAVSYS", "*SERVICE", "*AUDIT", "*IOSYSCFG" };
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a zero length array parameter.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns a zero length array.</dd>
     </dl>
     **/
    public void Var037()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[0];
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns a zero length array.</dd>
     </dl>
     **/
    public void Var038()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "" };
            String[] expectedValue = new String[0];
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialAuthority() returns a zero length array.</dd>
     </dl>
     **/
    public void Var039()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SAME" };
            String[] expectedValue = new String[0];
            u.setSpecialAuthority(testValue);
            String[] returnValue = u.getSpecialAuthority();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a not valid value parameter of *NONE and another value.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var040()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*NONE", "*SECADM" };
            try
            {
                u.setSpecialAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a not valid value parameter of *USRCLS and another value.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var041()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*USRCLS", "*SAVSYS" };
            try
            {
                u.setSpecialAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var042()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*USER" };
            try
            {
                u.setSpecialAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a not valid value parameter as an element of the array.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var043()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SPLCTL", "*SAVSYS", "*USER", "*ALLOBJ", "*SECADM" };
            try
            {
                u.setSpecialAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var044()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*INBETWEEEN" };
            try
            {
                u.setSpecialAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var045()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = null;
            try
            {
                u.setSpecialAuthority(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "specialAuthority");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with an array parameter that is just a null value.</dd>
     <dt>Result:</dt><dd>Verify that a AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var046()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = new String[] { null };
            try
            {
                u.setSpecialAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) with an array parameter that contains a null element.</dd>
     <dt>Result:</dt><dd>Verify that a AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var047()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = new String[] { "*SPLCTL", "*SAVSYS", null, "*ALLOBJ", "*SECADM" };
            try
            {
                u.setSpecialAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var048()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String[] testValue = new String[] { "*USRCLS" };
	    try
	    {
                u.setSpecialAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::setSpecialAuthority(String[]) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var049()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String[] testValue = new String[] { "*USRCLS" };
	    try
	    {
                u.setSpecialAuthority(testValue);
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
     <dt>Test:</dt><dd>Call User::getSpecialEnvironment() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var050()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*SYSVAL";
            String returnValue = u.getSpecialEnvironment();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialEnvironment(String) with a valid parameter of *SYSVAL.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialEnvironment() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var051()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            u.setSpecialEnvironment(testValue);
            String returnValue = u.getSpecialEnvironment();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialEnvironment(String) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialEnvironment() returns *NONE.</dd>
     </dl>
     **/
    public void Var052()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*NONE";
            u.setSpecialEnvironment(testValue);
            String returnValue = u.getSpecialEnvironment();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialEnvironment(String) with a valid parameter of *S36.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialEnvironment() returns *S36.</dd>
     </dl>
     **/
    public void Var053()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*S36";
            u.setSpecialEnvironment(testValue);
            String returnValue = u.getSpecialEnvironment();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialEnvironment(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialEnvironment() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var054()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*SYSVAL";
            u.setSpecialEnvironment(testValue);
            String returnValue = u.getSpecialEnvironment();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialEnvironment(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getSpecialEnvironment() returns *SYSVAL.</dd>
     </dl>
     **/
    public void Var055()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*SYSVAL";
            u.setSpecialEnvironment(testValue);
            String returnValue = u.getSpecialEnvironment();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialEnvironment(String) with a not valid value parameter.</dd>
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
                u.setSpecialEnvironment(testValue);
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
     <dt>Test:</dt><dd>Call User::setSpecialEnvironment(String) with a not valid over length parameter.</dd>
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
                u.setSpecialEnvironment(testValue);
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
     <dt>Test:</dt><dd>Call User::setSpecialEnvironment(String) with a null parameter.</dd>
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
                u.setSpecialEnvironment(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "specialEnvironment");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSpecialEnvironment(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var059()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*SYSVAL";
	    try
	    {
                u.setSpecialEnvironment(testValue);
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
     <dt>Test:</dt><dd>Call User::setSpecialEnvironment(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var060()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*SYSVAL";
	    try
	    {
                u.setSpecialEnvironment(testValue);
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
     <dt>Test:</dt><dd>Call User::getStatus() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var061()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*ENABLED";
            String returnValue = u.getStatus();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setStatus(String) with a valid parameter of *ENABLED.</dd>
     <dt>Result:</dt><dd>Verify User::getStatus() returns *ENABLED.</dd>
     </dl>
     **/
    public void Var062()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*ENABLED";
            u.setStatus(testValue);
            String returnValue = u.getStatus();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setStatus(String) with a valid parameter of *DISABLED.</dd>
     <dt>Result:</dt><dd>Verify User::getStatus() returns *DISABLED.</dd>
     </dl>
     **/
    public void Var063()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*DISABLED";
            u.setStatus(testValue);
            String returnValue = u.getStatus();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setStatus(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getStatus() returns *ENABLED.</dd>
     </dl>
     **/
    public void Var064()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*ENABLED";
            u.setStatus(testValue);
            String returnValue = u.getStatus();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setStatus(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getStatus() returns *ENABLED.</dd>
     </dl>
     **/
    public void Var065()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*ENABLED";
            u.setStatus(testValue);
            String returnValue = u.getStatus();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setStatus(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var066()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            try
            {
                u.setStatus(testValue);
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
     <dt>Test:</dt><dd>Call User::setStatus(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var067()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setStatus(testValue);
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
     <dt>Test:</dt><dd>Call User::setStatus(String) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var068()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String testValue = null;
            try
            {
                u.setStatus(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "status");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setStatus(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var069()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*ENABLED";
	    try
	    {
                u.setStatus(testValue);
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
     <dt>Test:</dt><dd>Call User::setStatus(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var070()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*ENABLED";
	    try
	    {
                u.setStatus(testValue);
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
     <dt>Test:</dt><dd>Call User::getSupplementalGroups() before setting on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var071()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] expectedValue = new String[0];
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getSupplementalGroups() before setting on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var072()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] expectedValue = new String[0];
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of *NONE on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns a zero length array.</dd>
     </dl>
     **/
    public void Var073()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*NONE" };
            String[] expectedValue = new String[0];
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of *NONE on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns a zero length array.</dd>
     </dl>
     **/
    public void Var074()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { "*NONE" };
            String[] expectedValue = new String[0];
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a zero length array parameter on a profile without a group.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns a zero length array.</dd>
     </dl>
     **/
    public void Var075()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[0];
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a zero length array parameter on a profile with a group.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns a zero length array.</dd>
     </dl>
     **/
    public void Var076()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[0];
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Create a profile with supplemental groups, call User::setSupplementalGroups(String[]) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns a zero length array.</dd>
     </dl>
     **/
    public void Var077()
    {
        try
        {
            String group = sandbox_.createGroup();
            String supGroup1 = sandbox_.createGroup();
            String supGroup2 = sandbox_.createGroup();

            CommandCall cmd = new CommandCall(pwrSys_, "QSYS/CRTUSRPRF USRPRF(UGSRSTX1) PASSWORD(*NONE) GRPPRF(" + group + ") SUPGRPPRF(" + supGroup1 + " " + supGroup2 + ")");
            cmd.setThreadSafe(false);

            if (!cmd.run())
            {
  		 AS400Message[] message = cmd.getMessageList();
  		if ((message != null) &&
  		    (message.length > 0) &&
  		    (message[0].toString().indexOf("CPF2214") > 0)) {
  		    output_.println("Warning "+message[0].toString());
  		} else {
  		    throw new AS400Exception(message);
  		}


            }

            User u = new User(pwrSys_, "UGSRSTX1");
            String[] testValue = new String[] { "*NONE" };
            String[] expectedValue = new String[0];
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, expectedValue));

            cmd = new CommandCall(pwrSys_, "QSYS/DLTUSRPRF USRPRF(UGSRSTX1)");
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
     <dt>Test:</dt><dd>Create a profile with supplemental groups, call User::setSupplementalGroups(String[]) with a zero length array parameter.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns a zero length array.</dd>
     </dl>
     **/
    public void Var078()
    {
        try
        {
            String group = sandbox_.createGroup();
            String supGroup1 = sandbox_.createGroup();
            String supGroup2 = sandbox_.createGroup();

            CommandCall cmd = new CommandCall(pwrSys_, "QSYS/CRTUSRPRF USRPRF(UGSRSTX2) PASSWORD(*NONE) GRPPRF(" + group + ") SUPGRPPRF(" + supGroup1 + " " + supGroup2 + ")");
            cmd.setThreadSafe(false);

            if (!cmd.run())
            {
  		 AS400Message[] message = cmd.getMessageList();
  		if ((message != null) &&
  		    (message.length > 0) &&
  		    (message[0].toString().indexOf("CPF2214") > 0)) {
  		    output_.println("Warning "+message[0].toString());
  		} else {
  		    throw new AS400Exception(message);
  		}


            }

            User u = new User(pwrSys_, "UGSRSTX2");
            String[] testValue = new String[0];
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));

            cmd = new CommandCall(pwrSys_, "QSYS/DLTUSRPRF USRPRF(UGSRSTX2)");
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
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of length 1.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns the values.</dd>
     </dl>
     **/
    public void Var079()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { sandbox_.createGroup() };
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of length 2.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns the values.</dd>
     </dl>
     **/
    public void Var080()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { sandbox_.createGroup(), sandbox_.createGroup() };
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of length 3.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns the values.</dd>
     </dl>
     **/
    public void Var081()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup() };
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of length 4.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns the values.</dd>
     </dl>
     **/
    public void Var082()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup() };
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of length 4 with a duplicate.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns the 3 unique values.</dd>
     </dl>
     **/
    public void Var083()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] expectedValue = new String[] { sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup() };
            String[] testValue = new String[] { expectedValue[0], expectedValue[1], expectedValue[1], expectedValue[2] };
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of length 4 with one element the group profile.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns the 4 values.</dd>
     </dl>
     **/
    public void Var084()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { sandbox_.createGroup(), sandbox_.createGroup(), groupAndUsers[0], sandbox_.createGroup() };
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of length 5.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns the values.</dd>
     </dl>
     **/
    public void Var085()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup() };
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of length 6.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns the values.</dd>
     </dl>
     **/
    public void Var086()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup() };
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of length 7.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns the values.</dd>
     </dl>
     **/
    public void Var087()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup() };
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of length 8.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns the values.</dd>
     </dl>
     **/
    public void Var088()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup() };
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of length 15.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns the values.</dd>
     </dl>
     **/
    public void Var089()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup(), sandbox_.createGroup() };
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns a zero length array.</dd>
     </dl>
     **/
    public void Var090()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "" };
            String[] expectedValue = new String[0];
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getSupplementalGroups() returns a zero length array.</dd>
     </dl>
     **/
    public void Var091()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SAME" };
            String[] expectedValue = new String[0];
            u.setSupplementalGroups(testValue);
            String[] returnValue = u.getSupplementalGroups();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a valid parameter of length 1 on a profile not in a group.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var092()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { sandbox_.createGroup() };
            try
            {
                u.setSupplementalGroups(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CD ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a not valid value parameter of *NONE and another value.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var093()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { "*NONE", sandbox_.createGroup() };
            try
            {
                u.setSupplementalGroups(testValue);
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
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a profile that does not exist parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var094()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { "NOTEXIST" };
            try
            {
                u.setSupplementalGroups(testValue);
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
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a profile that does not exist parameter as an element of the array.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var095()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { sandbox_.createGroup(), sandbox_.createGroup(), "NOTEXIST", sandbox_.createGroup(), sandbox_.createGroup() };
            try
            {
                u.setSupplementalGroups(testValue);
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
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var096()
    {
        try
        {
            String[] groupAndUsers = sandbox_.createGroupAndUsers(1);
            User u = new User(pwrSys_, groupAndUsers[1]);
            String[] testValue = new String[] { "ELEVENISBIG" };
            try
            {
                u.setSupplementalGroups(testValue);
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
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var097()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = null;
            try
            {
                u.setSupplementalGroups(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "supplementalGroups");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with an array parameter that is just a null value.</dd>
     <dt>Result:</dt><dd>Verify that a AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var098()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = new String[] { null };
            try
            {
                u.setSupplementalGroups(testValue);
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
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) with an array parameter that contains a null element.</dd>
     <dt>Result:</dt><dd>Verify that a AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var099()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = new String[] { sandbox_.createGroup(), sandbox_.createGroup(), null, sandbox_.createGroup(), sandbox_.createGroup() };
            try
            {
                u.setSupplementalGroups(testValue);
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
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var100()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String[] testValue = new String[] { "*NONE" };
	    try
	    {
                u.setSupplementalGroups(testValue);
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
     <dt>Test:</dt><dd>Call User::setSupplementalGroups(String[]) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var101()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String[] testValue = new String[] { "*NONE" };
	    try
	    {
                u.setSupplementalGroups(testValue);
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
