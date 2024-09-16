///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserGetSetTtoZTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.DateTimeConverter;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.User;

import test.JDTestDriver;
import test.Testcase;

/**
 Testcase UserGetSetTestcase.  This tests the get and set methods of the User class.
 **/
public class UserGetSetTtoZTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserGetSetTtoZTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
    private UserSandbox sandbox_;
    private int twoYears;
    
    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "UGSTZT");
        
        // Add two to the current year to get the expiration date
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        twoYears = year + 2 ; 

    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        sandbox_.cleanup();
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
     <dt>Test:</dt><dd>Call User::getUserActionAuditLevel() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var001()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] expectedValue = new String[0];
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns a zero length array.</dd>
     </dl>
     **/
    public void Var002()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*NONE" };
            String[] expectedValue = new String[0];
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *CMD.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *CMD.</dd>
     </dl>
     **/
    public void Var003()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*CMD" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *CREATE.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *CREATE.</dd>
     </dl>
     **/
    public void Var004()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*CREATE" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *DELETE.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *DELETE.</dd>
     </dl>
     **/
    public void Var005()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DELETE" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *JOBDTA.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *JOBDTA.</dd>
     </dl>
     **/
    public void Var006()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*JOBDTA" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *OBJMGT.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *OBJMGT.</dd>
     </dl>
     **/
    public void Var007()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*OBJMGT" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *OFCSRV.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *OFCSRV.</dd>
     </dl>
     **/
    public void Var008()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*OFCSRV" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *PGMADP.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *PGMADP.</dd>
     </dl>
     **/
    public void Var009()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*PGMADP" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *SAVRST.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *SAVRST.</dd>
     </dl>
     **/
    public void Var010()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SAVRST" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *SECURITY.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *SECURITY.</dd>
     </dl>
     **/
    public void Var011()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SECURITY" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *SERVICE.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *SERVICE.</dd>
     </dl>
     **/
    public void Var012()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SERVICE" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *SPLFDTA.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *SPLFDTA.</dd>
     </dl>
     **/
    public void Var013()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SPLFDTA" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *SYSMGT.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *SYSMGT.</dd>
     </dl>
     **/
    public void Var014()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SYSMGT" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of *OPTICAL.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns *OPTICAL.</dd>
     </dl>
     **/
    public void Var015()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*OPTICAL" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of length 2.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns the values.</dd>
     </dl>
     **/
    public void Var016()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*CREATE", "*CMD" };
            String[] expectedValue = new String[] { "*CMD", "*CREATE" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of length 3.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns the values.</dd>
     </dl>
     **/
    public void Var017()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*OBJMGT", "*JOBDTA", "*DELETE" };
            String[] expectedValue = new String[] { "*DELETE", "*JOBDTA", "*OBJMGT" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of length 4.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns the values.</dd>
     </dl>
     **/
    public void Var018()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SERVICE", "*CMD", "*PGMADP", "*OFCSRV" };
            String[] expectedValue = new String[] { "*CMD", "*OFCSRV", "*PGMADP", "*SERVICE" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of length 4 with a duplicate.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns the 3 unique values.</dd>
     </dl>
     **/
    public void Var019()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SERVICE", "*CREATE", "*CREATE", "*DELETE" };
            String[] expectedValue = new String[] { "*CREATE", "*DELETE", "*SERVICE" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of length 5.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns the values.</dd>
     </dl>
     **/
    public void Var020()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*JOBDTA", "*OBJMGT", "*SERVICE", "*DELETE", "*CREATE" };
            String[] expectedValue = new String[] { "*CREATE", "*DELETE", "*JOBDTA", "*OBJMGT", "*SERVICE" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of length 6.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns the values.</dd>
     </dl>
     **/
    public void Var021()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DELETE", "*JOBDTA", "*OBJMGT", "*SERVICE", "*CMD", "*CREATE" };
            String[] expectedValue = new String[] { "*CMD", "*CREATE", "*DELETE", "*JOBDTA", "*OBJMGT", "*SERVICE" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of length 7.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns the values.</dd>
     </dl>
     **/
    public void Var022()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DELETE", "*OFCSRV", "*JOBDTA", "*PGMADP", "*SERVICE", "*CMD", "*CREATE" };
            String[] expectedValue = new String[] { "*CMD", "*CREATE", "*DELETE", "*JOBDTA", "*OFCSRV", "*PGMADP", "*SERVICE" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of length 8.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns the values.</dd>
     </dl>
     **/
    public void Var023()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DELETE", "*OFCSRV", "*JOBDTA", "*OBJMGT", "*PGMADP", "*SERVICE", "*CMD", "*CREATE" };
            String[] expectedValue = new String[] { "*CMD", "*CREATE", "*DELETE", "*JOBDTA", "*OBJMGT", "*OFCSRV", "*PGMADP", "*SERVICE" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a valid parameter of length 13.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns the values.</dd>
     </dl>
     **/
    public void Var024()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*DELETE", "*OPTICAL", "*OFCSRV", "*SAVRST", "*JOBDTA", "*OBJMGT", "*SPLFDTA", "*PGMADP", "*SERVICE", "*SYSMGT", "*CMD", "*CREATE", "*SECURITY" };
            String[] expectedValue = new String[] { "*CMD", "*CREATE", "*DELETE", "*JOBDTA", "*OBJMGT", "*OFCSRV", "*PGMADP", "*SAVRST", "*SECURITY", "*SERVICE", "*SPLFDTA", "*SYSMGT", "*OPTICAL" };
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a zero length array parameter.</dd>
     <dt>Result:</dt><dd>Verify User::getUserActionAuditLevel() returns a zero length array.</dd>
     </dl>
     **/
    public void Var025()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[0];
            u.setUserActionAuditLevel(testValue);
            String[] returnValue = u.getUserActionAuditLevel();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var026()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "" };
            try
            {
                u.setUserActionAuditLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var027()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SAME" };
            try
            {
                u.setUserActionAuditLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a not valid value parameter of *NONE and another value.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var028()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*NONE", "*CREATE" };
            try
            {
                u.setUserActionAuditLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var029()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*USER" };
            try
            {
                u.setUserActionAuditLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a not valid value parameter as an element of the array.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var030()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*JOBDTA", "*OBJMGT", "*USER", "*CMD", "*CREATE" };
            try
            {
                u.setUserActionAuditLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var031()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*INBETWEEEN" };
            try
            {
                u.setUserActionAuditLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var032()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = null;
            try
            {
                u.setUserActionAuditLevel(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "userActionAuditLevel");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with an array parameter that is just a null value.</dd>
     <dt>Result:</dt><dd>Verify that a AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var033()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = new String[] { null };
            try
            {
                u.setUserActionAuditLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) with an array parameter that contains a null element.</dd>
     <dt>Result:</dt><dd>Verify that a AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var034()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = new String[] { "*JOBDTA", "*OBJMGT", null, "*CMD", "*CREATE" };
            try
            {
                u.setUserActionAuditLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var035()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String[] testValue = new String[] { "*NONE" };
	    try
	    {
                u.setUserActionAuditLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserActionAuditLevel(String[]) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var036()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String[] testValue = new String[] { "*NONE" };
	    try
	    {
                u.setUserActionAuditLevel(testValue);
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
     <dt>Test:</dt><dd>Call User::getUserClassName() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var037()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String expectedValue = "*USER";
            String returnValue = u.getUserClassName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserClassName(String) with a valid parameter of *USER.</dd>
     <dt>Result:</dt><dd>Verify User::getUserClassName() returns *USER.</dd>
     </dl>
     **/
    public void Var038()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*USER";
            u.setUserClassName(testValue);
            String returnValue = u.getUserClassName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserClassName(String) with a valid parameter of *SYSOPR.</dd>
     <dt>Result:</dt><dd>Verify User::getUserClassName() returns *SYSOPR.</dd>
     </dl>
     **/
    public void Var039()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSOPR";
            u.setUserClassName(testValue);
            String returnValue = u.getUserClassName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserClassName(String) with a valid parameter of *PGMR.</dd>
     <dt>Result:</dt><dd>Verify User::getUserClassName() returns *PGMR.</dd>
     </dl>
     **/
    public void Var040()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*PGMR";
            u.setUserClassName(testValue);
            String returnValue = u.getUserClassName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserClassName(String) with a valid parameter of *SECADM.</dd>
     <dt>Result:</dt><dd>Verify User::getUserClassName() returns *SECADM.</dd>
     </dl>
     **/
    public void Var041()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SECADM";
            u.setUserClassName(testValue);
            String returnValue = u.getUserClassName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserClassName(String) with a valid parameter of *SECOFR.</dd>
     <dt>Result:</dt><dd>Verify User::getUserClassName() returns *SECOFR.</dd>
     </dl>
     **/
    public void Var042()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SECOFR";
            u.setUserClassName(testValue);
            String returnValue = u.getUserClassName();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserClassName(String) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getUserClassName() returns *USER.</dd>
     </dl>
     **/
    public void Var043()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "";
            String expectedValue = "*USER";
            u.setUserClassName(testValue);
            String returnValue = u.getUserClassName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserClassName(String) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getUserClassName() returns *USER.</dd>
     </dl>
     **/
    public void Var044()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SAME";
            String expectedValue = "*USER";
            u.setUserClassName(testValue);
            String returnValue = u.getUserClassName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserClassName(String) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var045()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*SYSVAL";
            try
            {
                u.setUserClassName(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserClassName(String) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var046()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String testValue = "*INBETWEEEN";
            try
            {
                u.setUserClassName(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserClassName(String) with a null parameter.</dd>
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
                u.setUserClassName(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "userClassName");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserClassName(String) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var048()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String testValue = "*USER";
	    try
	    {
                u.setUserClassName(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserClassName(String) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var049()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String testValue = "*USER";
	    try
	    {
                u.setUserClassName(testValue);
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
     <dt>Test:</dt><dd>Call User::getUserID() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var050()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long returnValue = u.getUserID();
            assertCondition(returnValue >= 1 && returnValue <= 4294967294l);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::getUserIDNumber() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var051()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int returnValue = u.getUserIDNumber();
            assertCondition(returnValue != 0 && returnValue != -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserID(long) with a valid parameter of 147823.</dd>
     <dt>Result:</dt><dd>Verify User::getUserID() returns 147823.</dd>
     </dl>
     **/
    public void Var052()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 147823;
            try
            {
                u.setUserID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getUserID();
            assertCondition(returnValue == testValue);
            sandbox_.deleteUser(u.getName(), 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserID(long) with a valid parameter of 147824.</dd>
     <dt>Result:</dt><dd>Verify User::getUserIDNumber() returns 147824.</dd>
     </dl>
     **/
    public void Var053()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 147824;
            try
            {
                u.setUserID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            int returnValue = u.getUserIDNumber();
            assertCondition(returnValue == testValue);
            sandbox_.deleteUser(u.getName(), 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserID(long) with a valid parameter of 147827 on a group profile.</dd>
     <dt>Result:</dt><dd>Verify User::getUserID() returns 147827.</dd>
     </dl>
     **/
    public void Var054()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            long testValue = 147827;
            try
            {
                u.setUserID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getUserID();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserID(long) with a valid parameter of 147828 on a group profile.</dd>
     <dt>Result:</dt><dd>Verify User::getUserIDNumber() returns 147828.</dd>
     </dl>
     **/
    public void Var055()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createGroup());
            long testValue = 147828;
            try
            {
                u.setUserID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            int returnValue = u.getUserIDNumber();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserID(long) with a valid parameter of 1.</dd>
     <dt>Result:</dt><dd>Verify User::getUserID() returns 1.</dd>
     </dl>
     **/
    public void Var056()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 1;
            try
            {
                u.setUserID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getUserID();
            assertCondition(returnValue == testValue);
            sandbox_.deleteUser(u.getName(), 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserID(long) with a valid parameter of 1.</dd>
     <dt>Result:</dt><dd>Verify User::getUserIDNumber() returns 1.</dd>
     </dl>
     **/
    public void Var057()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 1;
            try
            {
                u.setUserID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            int returnValue = u.getUserIDNumber();
            assertCondition(returnValue == testValue);
            sandbox_.deleteUser(u.getName(), 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserID(long) with a valid parameter of 4294967294.</dd>
     <dt>Result:</dt><dd>Verify User::getUserID() returns 4294967294.</dd>
     </dl>
     **/
    public void Var058()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 4294967294l;
            try
            {
                u.setUserID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            long returnValue = u.getUserID();
            assertCondition(returnValue == testValue);
            sandbox_.deleteUser(u.getName(), 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserID(long) with a valid parameter of 4294967294.</dd>
     <dt>Result:</dt><dd>Verify User::getUserIDNumber() returns -2.</dd>
     </dl>
     **/
    public void Var059()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 4294967294l;
            int expectedValue = -2;
            try
            {
                u.setUserID(testValue);
            }
            catch (AS400Exception e)
            {
                // Will end up here if GID is already taken.
                assertExceptionStartsWith(e, "AS400Exception", "CPF22CE ", ErrorCompletingRequestException.AS400_ERROR);
                return;
            }
            int returnValue = u.getUserIDNumber();
            assertCondition(returnValue == expectedValue);
            sandbox_.deleteUser(u.getName(), 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserID(long) with a not valid value parameter that is already taken.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var060()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = u.getUserID();
            User uu = new User(pwrSys_, sandbox_.createUser());
            try
            {
                uu.setUserID(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserID(long) with a not valid value parameter of -1.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var061()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            int testValue = -1;
            try
            {
                u.setUserID(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserID(long) with a not valid parameter of 0.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var062()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 0;
            try
            {
                u.setUserID(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserID(long) with a not valid value parameter of 4294967295.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var063()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            long testValue = 4294967295l;
            try
            {
                u.setUserID(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserID(long) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var064()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            long testValue = 37;
	    try
	    {
                u.setUserID(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserID(long) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var065()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            long testValue = 37;
	    try
	    {
                u.setUserID(testValue);
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
     <dt>Test:</dt><dd>Call User::getUserOptions() before setting.</dd>
     <dt>Result:</dt><dd>Verify correct initial value is returned.</dd>
     </dl>
     **/
    public void Var066()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] expectedValue = new String[0];
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of *NONE.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns a zero length array.</dd>
     </dl>
     **/
    public void Var067()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*NONE" };
            String[] expectedValue = new String[0];
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of *CLKWD.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns *CLKWD.</dd>
     </dl>
     **/
    public void Var068()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*CLKWD" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of *EXPERT.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns *EXPERT.</dd>
     </dl>
     **/
    public void Var069()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*EXPERT" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of *HLPFULL.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns *HLPFULL.</dd>
     </dl>
     **/
    public void Var070()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*HLPFULL" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of *STSMSG.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns *STSMSG.</dd>
     </dl>
     **/
    public void Var071()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*STSMSG" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of *NOSTSMSG.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns *NOSTSMSG.</dd>
     </dl>
     **/
    public void Var072()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*NOSTSMSG" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of *ROLLKEY.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns *ROLLKEY.</dd>
     </dl>
     **/
    public void Var073()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*ROLLKEY" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of *PRTMSG.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns *PRTMSG.</dd>
     </dl>
     **/
    public void Var074()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*PRTMSG" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of length 2.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns the values.</dd>
     </dl>
     **/
    public void Var075()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*EXPERT", "*CLKWD" };
            String[] expectedValue = new String[] { "*CLKWD", "*EXPERT" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of length 3.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns the values.</dd>
     </dl>
     **/
    public void Var076()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*ROLLKEY", "*STSMSG", "*HLPFULL" };
            String[] expectedValue = new String[] { "*HLPFULL", "*STSMSG", "*ROLLKEY" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of length 4.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns the values.</dd>
     </dl>
     **/
    public void Var077()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*ROLLKEY", "*CLKWD", "*HLPFULL", "*PRTMSG" };
            String[] expectedValue = new String[] { "*CLKWD", "*HLPFULL", "*ROLLKEY", "*PRTMSG" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of length 4 with a duplicate.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns the 3 unique values.</dd>
     </dl>
     **/
    public void Var078()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*ROLLKEY", "*EXPERT", "*EXPERT", "*HLPFULL" };
            String[] expectedValue = new String[] { "*EXPERT", "*HLPFULL", "*ROLLKEY" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of length 5.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns the values.</dd>
     </dl>
     **/
    public void Var079()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*EXPERT", "*NOSTSMSG", "*ROLLKEY", "*HLPFULL", "*CLKWD" };
            String[] expectedValue = new String[] { "*CLKWD", "*EXPERT", "*HLPFULL", "*NOSTSMSG", "*ROLLKEY" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a valid parameter of length 6.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns the values.</dd>
     </dl>
     **/
    public void Var080()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*HLPFULL", "*STSMSG", "*PRTMSG", "*ROLLKEY", "*CLKWD", "*EXPERT" };
            String[] expectedValue = new String[] { "*CLKWD", "*EXPERT", "*HLPFULL", "*STSMSG", "*ROLLKEY", "*PRTMSG" };
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a zero length array parameter.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns a zero length array.</dd>
     </dl>
     **/
    public void Var081()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[0];
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a parameter of empty string.</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns a zero length array.</dd>
     </dl>
     **/
    public void Var082()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "" };
            String[] expectedValue = new String[0];
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a parameter of "*SAME".</dd>
     <dt>Result:</dt><dd>Verify User::getUserOptions() returns a zero length array.</dd>
     </dl>
     **/
    public void Var083()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*SAME" };
            String[] expectedValue = new String[0];
            u.setUserOptions(testValue);
            String[] returnValue = u.getUserOptions();
            assertCondition(checkArray(returnValue, expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a not valid value parameter of *STSMSG and *NOSTSMSG.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var084()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*STSMSG", "*NOSTSMSG" };
            try
            {
                u.setUserOptions(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF22E1 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a not valid value parameter of *STSMSG and *NOSTSMSG as part of all 7 options.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var085()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*CLKWD", "*EXPERT", "*HLPFULL", "*STSMSG", "*NOSTSMSG", "*ROLLKEY", "*PRTMSG" };
            try
            {
                u.setUserOptions(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400Exception", "CPF22E1 ", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a not valid value parameter of *NONE and another value.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var086()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*NONE", "*EXPERT" };
            try
            {
                u.setUserOptions(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a not valid value parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var087()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*USER" };
            try
            {
                u.setUserOptions(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a not valid value parameter as an element of the array.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var088()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*HLPFULL", "*NOSTSMSG", "*USER", "*CLKWD", "*EXPERT" };
            try
            {
                u.setUserOptions(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a not valid over length parameter.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var089()
    {
        try
        {
            User u = new User(pwrSys_, sandbox_.createUser());
            String[] testValue = new String[] { "*INBETWEEEN" };
            try
            {
                u.setUserOptions(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var090()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = null;
            try
            {
                u.setUserOptions(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "userOptions");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with an array parameter that is just a null value.</dd>
     <dt>Result:</dt><dd>Verify that a AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var091()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = new String[] { null };
            try
            {
                u.setUserOptions(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) with an array parameter that contains a null element.</dd>
     <dt>Result:</dt><dd>Verify that a AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var092()
    {
        try
        {
            User u = new User(pwrSys_, "BOB");
            String[] testValue = new String[] { "*HLPFULL", "*NOSTSMSG", null, "*CLKWD", "*EXPERT" };
            try
            {
                u.setUserOptions(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) before system is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var093()
    {
	try
	{
	    User u = new User();
	    u.setName("BOB");
            String[] testValue = new String[] { "*NONE" };
	    try
	    {
                u.setUserOptions(testValue);
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
     <dt>Test:</dt><dd>Call User::setUserOptions(String[]) before name is set.</dd>
     <dt>Result:</dt><dd>Verify that a ExtendedIllegalStateException is thrown.</dd>
     </dl>
     **/
    public void Var094()
    {
	try
	{
	    User u = new User();
	    u.setSystem(pwrSys_);
            String[] testValue = new String[] { "*NONE" };
	    try
	    {
                u.setUserOptions(testValue);
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
    <dt>Test:</dt><dd>Call User::getUserExpirationAction .</dd>
    <dt>Result:</dt><dd>Should be successful and return null.</dd>
    </dl>
    **/
    public void Var095()
    {
    	try
    	{
    		if (systemObject_.getVersion() >= 7)
    		{
    			User u = new User(systemObject_, "fakeuser");

    			try
    			{
    				if (u.getUserExpirationAction()== null)
    				succeeded();

    				else
    				failed("This should return null.");

    			}
    			catch (Exception e)
    			{
    				failed("Unexpected Exception.");
    			}
    		}
    		else
    		{
    			notApplicable("Only for V7R1 and later");
    		}
	      }
	      catch (Exception e)
	      {
	         	failed(e, "Unexpected exception.");
	      }

     }

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::getUserExpirationAction .</dd>
    <dt>Result:</dt><dd>Should be successful and return *DELETE.</dd>
    </dl>
    **/
    public void Var096()
    {
    	try
    	{
    		if (systemObject_.getVersion() >= 7)
    		{

    			CommandCall cmd = new CommandCall(pwrSys_);
    			cmd.run("CRTUSRPRF USRPRF(JTEST) PASSWORD(a1s2d3f4)");
    			cmd.run("CHGEXPSCDE USRPRF(JTEST) EXPDATE('07/01/"+twoYears+"') ACTION(*DELETE)");

    			User u = new User(pwrSys_, "JTEST");

    			try
    			{
			    String userExpirationAction = u.getUserExpirationAction(); 
    				if ("*DELETE".equals(userExpirationAction))
    					succeeded();
    				else
    					failed("userExpirationAction="+userExpirationAction+"This should return *DELETE.");

    			}
    			catch (Exception e)
    			{
	         	failed(e, "Unexpected exception.");
    			}

    			finally{

    				try
    				{
    					cmd.run("DLTUSRPRF   USRPRF(JTEST) OWNOBJOPT(*DLT)");
    				}
    				catch(Exception e)
    				{
    					System.out.println("Unable to delete user JTEST");
    				}
    			}
    		}
    		else
    		{
    			notApplicable("Only for V7R1 and later");
    		}
	  }
	  catch (Exception e)
	  {
	      failed(e, "Unexpected exception.");
	  }
     }

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::getUserExpirationAction .</dd>
    <dt>Result:</dt><dd>Should be successful and return *DISABLE.</dd>
    </dl>
    **/
    public void Var097()
    {
    	try
    	{
    		if (systemObject_.getVersion() >= 7)
    		{

    			CommandCall cmd = new CommandCall(pwrSys_);
    			cmd.run("CRTUSRPRF USRPRF(JTEST)  PASSWORD(a1s2d3f4)");
    			cmd.run("CHGEXPSCDE USRPRF(JTEST) EXPDATE('07/01/"+twoYears+"') ACTION(*DISABLE)");

    			User u = new User(pwrSys_, "JTEST");

    			try
    			{
			    String userExpirationAction = u.getUserExpirationAction(); 
    				if ("*DISABLE".equals(userExpirationAction))
    					succeeded();
    				else
    					failed("userExpirationAction="+userExpirationAction+".  This should return *DISABLE");

    			}
    			catch (Exception e)
    			{
	         	failed(e, "Unexpected exception.");
    			}

    			finally{

    				try
    				{
    					cmd.run("DLTUSRPRF   USRPRF(JTEST) OWNOBJOPT(*DLT)");
    				}
    				catch(Exception e)
    				{
    					System.out.println("Unable to delete user JTEST");
    				}
    			}
    		}
    		else
    		{
    			notApplicable("Only for V7R1 and later");
    		}
	  }
	  catch (Exception e)
	  {
	      failed(e, "Unexpected exception.");
	  }
     }

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::getUserExpirationAction .</dd>
    <dt>Result:</dt><dd>Should be successful and return *NONE.</dd>
    </dl>
    **/
    public void Var098()
    {
    	try
    	{
    		if (systemObject_.getVersion() >= 7)
    		{

    			CommandCall cmd = new CommandCall(pwrSys_);
    			cmd.run("CRTUSRPRF USRPRF(JTEST)  PASSWORD(a1s2d3f4)");
    			cmd.run("CHGEXPSCDE USRPRF(JTEST) EXPDATE('07/01/"+twoYears+"') ACTION(*NONE)");

    			User u = new User(pwrSys_, "JTEST");

    			try
    			{
			    String userExpirationAction = u.getUserExpirationAction(); 
    				if ("*NONE".equals(userExpirationAction))
    					succeeded();
    				else
    					failed("Got userExpirationAction of "+userExpirationAction+".  This should return *NONE");
    			}
    			catch (Exception e)
    			{
	         	failed(e, "Unexpected exception.");
    			}

    			finally{

    				try
    				{
    					cmd.run("DLTUSRPRF   USRPRF(JTEST) OWNOBJOPT(*DLT)");
    				}
    				catch(Exception e)
    				{
    					System.out.println("Unable to delete user JTEST");
    				}
    			}
    		}
    		else
    		{
    			notApplicable("Only for V7R1 and later");
    		}
	  }
	  catch (Exception e)
	  {
	      failed(e, "Unexpected exception.");
	  }
     }

   /**
    <dl>
    <dt>Test:</dt><dd>Call User::setUserExpirationDate.</dd>
    <dt>Result:</dt><dd>Calling getUserExpirationDate, should return the same date.</dd>
    </dl>
    **/
    public void Var099()
    {
      DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      try
      {
 
        Date a =        dfm.parse(""+twoYears+"-05-01 00:00:00 " );
        Date expected = dfm.parse(""+twoYears+"-05-01 00:00:00 " );  


        if (systemObject_.getVersion() >= 7)
        {

          try
          {
            User u = new User(pwrSys_, sandbox_.createUser());

            u.setUserExpirationDate(a);
            Date got = u.getUserExpirationDate();

            if (got.equals(expected))
              succeeded();
            else
            {
              System.out.println("Expected: |" + expected.toString() + "|");
              System.out.println("Got:      |" + got.toString() + "|");
              failed("Dates should be the same. ");
            }

          }
          catch (Exception e)
          {
            failed(e, "Unexpected exception.");
          }
        }
        else
        {
          notApplicable("Only for V7R1 and later");
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception ");
        e.printStackTrace();
      }


    }

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setUserExpirationDate .</dd>
    <dt>Result:</dt><dd>Calling getUserExpirationDate, should return the same date.</dd>
    </dl>
    **/
    public void Var100()
    {
      DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

      try
      {
        String tz = DateTimeConverter.timeZoneForSystem(pwrSys_).getDisplayName(); // for example, "GMT-05:00"
        //Tue Jan 28 00:00:00 CST 2014
        Date a = dfm.parse(twoYears+"-01-28 00:00:00 " + tz);
        Date expected = dfm.parse(twoYears+"-01-28 00:00:00 " + tz);

        if (systemObject_.getVersion() >= 7)
        {

          try
          {
            User u = new User(pwrSys_, sandbox_.createUser());

            u.setUserExpirationDate(a);
            Date got = u.getUserExpirationDate();

            if (got.equals(expected))
              succeeded();
            else
            {
              System.out.println("Expected: |" + dfm.format(expected) + "|");
              System.out.println("Got:      |" + dfm.format(got) + "|");
              failed("Dates should be the same.");
            }

          }
          catch (Exception e)
          {
            failed(e, "Unexpected exception.");
          }
        }

        else
        {
          notApplicable("Only for V7R1 and later");
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception ");
        e.printStackTrace();
      }
    }

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setUserExpirationDate .</dd>
    <dt>Result:</dt><dd>Should throw an exception since a bad date is used.</dd>
    </dl>
    **/
    public void Var101()
    {

    	DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date a;

	    	try {
				a = dfm.parse("2007-01-28 00:00:00");
		     	System.out.println(a);

	    		if (systemObject_.getVersion() >= 7)
	    		{

	    		    try
	    		      {
	    		         User u = new User(pwrSys_, sandbox_.createUser());

	    		         u.setUserExpirationDate(a);
	    		         failed("Should throw an exception.");

	    		      }
	    		      catch (Exception e)
	    		      {
	    		    	  assertExceptionIs(e, "AS400Exception", "CPF2251 The expiration date parameter is not valid.");
	    		      }
	    		}

	    		else
	    		{
	    			notApplicable("Only for V7R1 and later");
	    		}



			}
	    	catch (Exception e)
			{
				failed(e, "Unexpected exception ");
				e.printStackTrace();
			}
     }


    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setUserExpirationDate .</dd>
    <dt>Result:</dt><dd>Should throw an exception.</dd>
    </dl>
    **/
    public void Var102()
    {

    	DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date a;

	    	try {

				a = dfm.parse("2007-01-28 00:00:00");
		     	System.out.println(a);

	        	AS400 system = new AS400(systemObject_.getSystemName(), "JAVAX12345", "password");
	        	system.setGuiAvailable(false);

	    		if (systemObject_.getVersion() >= 7)
	    		{

	    		    try
	    		      {
	    		         User u = new User(system, sandbox_.createUser());

	    		         u.setUserExpirationDate(a);
	    		         failed("Should throw an exception.");

	    		      }
	    		      catch (Exception e)
			      {
				  // Updated 9/4/2017 ot user id is not known

				  if (getRelease() > JDTestDriver.RELEASE_V7R4M0)
				      assertExceptionIs(e,"AS400SecurityException", "Password is incorrect.:JAVAX12345");
				  else
				      assertExceptionIs(e,"AS400SecurityException", "User ID is not known.:JAVAX12345");
			      }
	    		}

	    		else
	    		{
	    			notApplicable("Only for V7R1 and later");
	    		}

			}
	    	catch (Exception e)
			{
				failed(e, "Unexpected exception ");
				e.printStackTrace();
			}
     }

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::u.setUserExpirationDate().</dd>
    <dt>Result:</dt><dd>Should throw an exception.</dd>
    </dl>
    **/
    public void Var103()
    {

		try {

			if (systemObject_.getVersion() < 7)
			{
					try
					{
						DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    	Date a = null;
						User u = new User(systemObject_, sandbox_.createUser());

						u.setUserExpirationDate(a);
						a = dfm.parse("2007-01-28 00:00:00");

						failed("Should throw an exception.");

					}
					catch (Exception e)
					{
						 assertExceptionIs(e, "RequestNotSupportedException");
					}
			}
			else
			{
				notApplicable("Only for V6R1 and earlier");
			}
		}
		catch (Exception e)
		{
			failed("Unexpected exception.");
			e.printStackTrace();
		}
    }

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::getUserExpirationDate .</dd>
    <dt>Result:</dt><dd>Calling getUserExpirationDate, should return null.</dd>
    </dl>
    **/
    public void Var104()
    {

	    try {

			if (systemObject_.getVersion() >= 7)
			{
				try
				{
					User u = new User(pwrSys_,"JAVA");

					if ((u.getUserExpirationDate()) == null)
						succeeded();
				    else
				        failed("This should return null");

				}
				catch (Exception e)
				{
					failed(e, "Unexpected exception.");
				}
			}
			else
			{
				notApplicable("Only for V7R1 and later");
			}
		}
	    catch (Exception e)
	    {
			failed(e, "Unexpected exception.");
		}

     }

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setUserExpirationInterval .</dd>
    <dt>Result:</dt><dd>Calling getUserExpirationInterval should return the set interval.</dd>
    </dl>
    **/
    public void Var105()
    {
	    try {

			if (systemObject_.getVersion() >= 7)
			{
				try
				{
					User u = new User(pwrSys_, sandbox_.createUser());
					u.setUserExpirationInterval(210);

					if(u.getUserExpirationInterval() == 210)
						succeeded();
					else
					    failed("The returned expiration interval should be 210");

				}
				catch (Exception e)
				{
					failed(e, "Unexpected exception");
				}
			}
			else
			{
				notApplicable("Only for V7R1 and later");
			}
		}
	    catch (Exception e)
	    {
			failed(e, "Unexpected exception.");
		}

     }

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setUserExpirationInterval .</dd>
    <dt>Result:</dt><dd>Should throw an exception.</dd>
    </dl>
    **/
    public void Var106()
    {

		try {

			if (systemObject_.getVersion() >= 7)
			{
				   try
				   {
					   User u = new User(pwrSys_, sandbox_.createUser());
					   u.setUserExpirationInterval(0);

				   	   failed("Should throw an exception.");

			    	}
			    	catch (Exception e)
			    	{
			    		 assertExceptionIs(e, "ExtendedIllegalArgumentException", "expirationInterval (0): The parameter value is out of the allowed range.");
			    	}
			    }
			    else
			    {
			    	notApplicable("Only for V7R1 and later");
			    }
		}
		catch (Exception e)
		{

			failed(e, "Unexpected exception.");
		}

	}

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setUserExpirationInterval .</dd>
    <dt>Result:</dt><dd>Should throw an exception.</dd>
    </dl>
    **/
    public void Var107()
    {

		try {

			if (systemObject_.getVersion() >= 7)
			{
				   try
				   {
					   User u = new User(pwrSys_, sandbox_.createUser());
					   u.setUserExpirationInterval(367);

				   	   failed("Should throw an exception.");

			    	}
			    	catch (Exception e)
			    	{
			    		 assertExceptionIs(e, "ExtendedIllegalArgumentException", "expirationInterval (367): The parameter value is out of the allowed range.");
			    	}
			    }
			    else
			    {
			    	notApplicable("Only for V7R1 and later");
			    }
		}
		catch (Exception e)
		{

			failed(e, "Unexpected exception.");
		}
	}

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setUserExpirationInterval .</dd>
    <dt>Result:</dt><dd>Should throw an exception.</dd>
    </dl>
    **/
    public void Var108()
    {
	    try {

			if (systemObject_.getVersion() >= 7)
			{
				try
				{
					User u = new User(systemObject_, sandbox_.createUser());

					u.setUserExpirationInterval(210);
					failed("Should throw an exception.  Verify that user profile " + systemObject_.getUserId() + " doesn't have *SECADM authority.");

				}
				catch (Exception e)
				{
				    String isavldck = "CPD0161 Not authorized to program ISAVLDCK in QSYS.";
				    String exceptionString = e.getMessage();
				    // System.out.println("exceptionString is "+exceptionString); 
				    // This exception may appear on some system
				    if (exceptionString.indexOf(isavldck) >= 0) {
					assertExceptionIs(e, "AS400Exception", isavldck);
				    } else { 
					 assertExceptionIs(e, "AS400Exception", "CPF2292 *SECADM required to create or change user profiles.");
				    }
				}
			}
			else
			{
				notApplicable("Only for V7R1 and later");
			}
		}
	    catch (Exception e)
	    {
			failed(e, "Unexpected exception.");
		}

     }

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setUserExpirationInterval .</dd>
    <dt>Result:</dt><dd>Should throw an exception.</dd>
    </dl>
    **/
    public void Var109()
    {
	    try {

			AS400 system = new AS400(systemObject_.getSystemName(), "JAVAX12345", "password");
			system.setGuiAvailable(false);

			if (systemObject_.getVersion() >= 7)
			{
				try
				{
					User u = new User(system, sandbox_.createUser());

					u.setUserExpirationInterval(210);
					failed("Should throw an exception.");

				}
				catch (Exception e)
				{
				    if (getRelease() > JDTestDriver.RELEASE_V7R4M0)
					assertExceptionIs(e,"AS400SecurityException", "Password is incorrect.:JAVAX12345");
				    else
					 assertExceptionIs(e, "AS400SecurityException", "User ID is not known.:JAVAX12345"); 
				}
			}
			else
			{
				notApplicable("Only for V7R1 and later");
			}
		}
	    catch (Exception e)
	    {
			failed(e, "Unexpected exception.");
		}

     }

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::setUserExpirationInterval .</dd>
    <dt>Result:</dt><dd>Should throw an exception.</dd>
    </dl>
    **/
    public void Var110()
    {

		try {
			if (systemObject_.getVersion() < 7)
			{
					try
					{
						User u = new User(systemObject_, sandbox_.createUser());

						u.setUserExpirationInterval(210);
						failed("Should throw an exception.");

					}
					catch (Exception e)
					{
						 assertExceptionIs(e, "RequestNotSupportedException");
					}
			}
			else
			{
				notApplicable("Only for V6R1 and earlier");
			}
		}
		catch (Exception e)
		{
			failed("Unexpected exception.");
			e.printStackTrace();
		}
    }

    /**
    <dl>
    <dt>Test:</dt><dd>Call User::isUserEntitlementRequired.</dd>
    <dt>Result:</dt><dd>Calling isUserEntitlementRequired should be successful.</dd>
    </dl>
    **/
    public void Var111()
    {

    	try
    	{
			User u = new User(systemObject_, sandbox_.createUser());
			u.isUserEntitlementRequired();
			succeeded();
		}
	    catch (Exception e)
	    {
			failed(e, "Unexpected exception.");
		}

     }


}
