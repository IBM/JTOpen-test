///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SSLChgPwdDialogTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.SSL;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.SecureAS400;

import test.Testcase;

/**
 Testcase SSLChgPwdDialogTestcase.
 **/
public class SSLChgPwdDialogTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SSLChgPwdDialogTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SSLTest.main(newArgs); 
   }
    CommandCall cmd;

    /**
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        cmd = new CommandCall(pwrSys_);
    }

    /**
     Setup a user profile with an expired password.
     **/
    private void setupUserID()
    {
        try
        {
            if (cmd.run("CRTUSRPRF USRPRF(JAVAEXPIR) PASSWORD(MY1PWD) PWDEXP(*YES) TEXT('IBM Toolbox for Java testing (DX8X)')") == false)
            {
                output_.println("Setup failed: " + cmd.getMessageList()[0].getID() + " " + cmd.getMessageList()[0].getText());
            }
        }
        catch(Exception e)
        {
            output_.println("Setup failed: " + e);
            e.printStackTrace(output_);
        }
    }

    /**
     Perform clean-up.  Delete test user profile.
     **/
    private void cleanupUserID()
    {
        try
        {
            if (cmd.run("DLTUSRPRF USRPRF(JAVAEXPIR) OWNOBJOPT(*DLT)") == false)
            {
                output_.println("Cleanup failed: " + cmd.getMessageList()[0].getID() + " " + cmd.getMessageList()[0].getText());
            }
        }
        catch(Exception e)
        {
            output_.println("Cleanup failed.  Manually delete user profile JAVAEXPIR. " + e);
            e.printStackTrace(output_);
        }
        pwrSys_.disconnectService(AS400.COMMAND);
        pwrSys_.close(); 
    }

    /**
     Create a user with an expired password, invoke the change password dialog, and test the minimize, maximize, focus, etc.  Then cancel before changing the expired password.
     **/
    public void Var001()
    {
        if (runMode_ != UNATTENDED)
        {
            try
            {
                // Attempt to signon and access dialog to change password.
                // Verify dialog cancel button works correctly.
                setupUserID();
                try
                {
                    SecureAS400 sys = new SecureAS400(systemName_, "JAVAEXPIR", "MY1PWD".toCharArray());
                    sys.setMustUseSockets(mustUseSockets_);

                    if (onAS400_ == false)
                    {
                        output_.println(" ");
                        output_.println("1) Verify password is expired message comes up; select Yes to change it.");
                        output_.println("2) Verify 'Change AS/400 Password' dialog comes up.");
                        output_.println("3) Adjust the size of the dialog, and try minimize/maximize menu items.");
                        output_.println("4) Adjust each border of the dialog box by moving it in or out.");
                        output_.println("5) Select Cancel to exit before changing password.");
                        output_.println(" ");
                    }

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        sys.close(); 
                        // Didn't get an exception when one was expected.
                        failed("Failed.  No exception.");
                    }
                    catch (Exception e)
                    {
                        if (onAS400_)
                        {
                            assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_EXPIRED);
                        }
                        else
                        {
                            assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.SIGNON_CANCELED);
                        }
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                        sys.close(); 
                    }
                }
                finally
                {
                    cleanupUserID();
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     Create a user with an expired password, invoke the change password dialog.
     Verify:
     - an error is displayed if old password is incorrect
     - an error is displayed if confirm password wasn't input by user
     - an error is displayed if confirm and new password don't match
     **/
    public void Var002()
    {
        if (runMode_ != UNATTENDED)
        {
            try
            {
                // Attempt to signon and access dialog to change password.
                // Verify dialog cancel button works correctly.
                setupUserID();
                try
                {
                    SecureAS400 sys = new SecureAS400(systemName_, "JAVAEXPIR", "MY1PWD".toCharArray());
                    sys.setMustUseSockets(mustUseSockets_);

                    if (onAS400_ == false)
                    {
                        output_.println(" ");
                        output_.println("1) Verify password is expired message comes up; select Yes to change it.");
                        output_.println("2) Verify 'Change AS/400 Password' dialog comes up.");
                        output_.println("3) Enter 'xxxx' for Old password and select OK.");
                        output_.println("4) Verify 'Missing user ID, old or new password' message comes up; select OK.");
                        output_.println("5) Verify 'Change AS/400 Password' dialog comes up.");
                        output_.println("6) Enter 'MY1PWD' for Old and 'JTEAM1J' for New.");
                        output_.println("   Leave Confirm blank and select OK.");
                        output_.println("7) Verify 'New password and confirm password are not the same.' message comes up; select OK.");
                        output_.println("8) Verify 'Change AS/400 Password' dialog comes up.");
                        output_.println("9) Enter 'MY1PWD' for Old, 'JTEAM1J' for New, and 'JTEAM1JJ'");
                        output_.println("    for Confirm; select OK.");
                        output_.println("10) Verify 'New password and confirm password are not the same.' message comes up; select OK.");
                        output_.println("11) Verify 'Change AS/400 Password' dialog comes up.");
                        output_.println("12) Select Cancel to exit signon.");
                        output_.println(" ");
                    }

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        sys.close(); 
                        // Didn't get an exception when one was expected.
                        failed("Failed.  No exception.");
                    }
                    catch (Exception e)
                    {
                        if (onAS400_)
                        {
                            assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_EXPIRED);
                        }
                        else
                        {
                            assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.SIGNON_CANCELED);
                        }
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                        sys.close(); 
                    }
                }
                finally
                {
                    cleanupUserID();
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     Create a user with an expired password, invoke the change password dialog.
     Verify:
     - password is changed successfully when old, new and confirm fields are input correctly
     **/
    public void Var003()
    {
        if (runMode_ != UNATTENDED)
        {
            try
            {
                // Attempt to signon and access dialog to change password.
                // Verify dialog cancel button works correctly.
                setupUserID();
                try
                {
                    SecureAS400 sys = new SecureAS400(systemName_, "JAVAEXPIR", "MY1PWD".toCharArray());
                    sys.setMustUseSockets(mustUseSockets_);

                    if (onAS400_ == false)
                    {
                        output_.println(" ");
                        output_.println("1) Verify password is expired message comes up; select Yes to change it.");
                        output_.println("2) Verify 'Change AS/400 Password' dialog comes up.");
                        output_.println("3) Enter 'MY1PWD' for Old password, 'JTEAM1J' for New and");
                        output_.println("   Confirm password.  Select OK.");
                        output_.println("4) Password should be successfully changed.");
                        output_.println(" ");
                    }

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        if (onAS400_)
                        {
                            failed("Failed.  No exception.");
                        }
                        else
                        {
                            succeeded();
                        }
                    }
                    catch (Exception e)
                    {
                        if (onAS400_)
                        {
                            assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_EXPIRED);
                        }
                        else
                        {
                            failed(e, "Unexpected exception.");
                        }
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                        sys.close(); 
                    }
                }
                finally
                {
                    cleanupUserID();
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     Change the password to a new one, and then verify that it is correct.
     **/
    public void Var004()
    {
        try
        {
            setupUserID();
            try
            {
                SecureAS400 sys = new SecureAS400(systemName_, "javaexpir", "MY1PWD".toCharArray());
                sys.setMustUseSockets(mustUseSockets_);

                sys.changePassword("MY1PWD".toCharArray(), "JTEAM1J".toCharArray());
                if (sys.validateSignon("JTEAM1J".toCharArray()))
                {
                    succeeded();
                }
                else
                {
                    failed("password validation failed");
                }
                sys.close(); 
            }
            finally
            {
                cleanupUserID();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
