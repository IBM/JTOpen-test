///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecChangePasswordTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;

/**
 Testcase SecChangePasswordTestcase.
 **/
public class SecChangePasswordTestcase extends Testcase
{
    /**
     Create a default (no system name, no user id) AS400 object, and try to change password.
     An InvalidObjectStateException should be generated.
     **/
    public void Var001()
    {
        try
        {
            AS400 sys = new AS400();
            sys.setMustUseSockets(mustUseSockets_);
            try
            {
                sys.changePassword("oldpasswor".toCharArray(), "newpasswor".toCharArray());
                failed("exception not generated");
            }
            catch (Exception e)
            {
                if (onAS400_ /* && isNative_ */ )
                {
                    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
                    sys.connectService(AS400.COMMAND);
                    sys.disconnectAllServices();
                }
                else
                {
                    assertExceptionIs(e, "ExtendedIllegalStateException");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create an AS400 object with only the system name, and try to change password.
     An InvalidObjectStateException should be generated.
     **/
    public void Var002()
    {
        try
        {
            AS400 sys = new AS400(systemName_);
            sys.setMustUseSockets(mustUseSockets_);
            try
            {
                sys.changePassword("oldpasswor".toCharArray(), "newpasswor".toCharArray());
                failed("exception not generated");
            }
            catch (Exception e)
            {
                if (onAS400_ /* && isNative_ */ )
                {
                    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
                    sys.connectService(AS400.COMMAND);
                    sys.disconnectAllServices();
                }
                else
                {
                    assertExceptionIs(e, "ExtendedIllegalStateException");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try using null for old password.
     A NullPointerException should be generated.
     **/
    public void Var003()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 sys = new AS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            try
            {
                sys.changePassword(null, "newpassword".toCharArray());
                failed("exception not generated");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a AS400 object with system name and user id, and try using null for new password.
     A NullPointerException should be generated.
     **/
    public void Var004()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 sys = new AS400(systemName_, userId_, charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            try
            {
                sys.changePassword(charPassword, null);
                failed("exception not generated");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException");
            }
   PasswordVault.clearPassword(charPassword);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
