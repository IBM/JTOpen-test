///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SSLChangePasswordTestcase.java
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
import com.ibm.as400.access.SecureAS400;

import test.PasswordVault;
import test.Testcase;

/**
 Testcase SSLChangePasswordTestcase.
 **/
public class SSLChangePasswordTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SSLChangePasswordTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SSLTest.main(newArgs); 
   }
    /**
     Create a default (no system name, no user id) SecureAS400 object, and try to change password.
     An InvalidObjectStateException should be generated.
     **/
    public void Var001()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            try
            {
                sys.changePassword("oldpasswor", "newpasswor");
                failed("exception not generated");
            }
            catch (Exception e)
            {
                if (onAS400_ && isNative_)
                {
                    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
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
     Create a SecureAS400 object with only the system name, and try to change password.
     An InvalidObjectStateException should be generated.
     **/
    public void Var002()
    {
        try
        {
            SecureAS400 sys = new SecureAS400(systemName_);
            sys.setMustUseSockets(mustUseSockets_);
            try
            {
                sys.changePassword("oldpasswor", "newpasswor");
                failed("exception not generated");
            }
            catch (Exception e)
            {
                if (onAS400_ && isNative_)
                {
                    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
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
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            try
            {
                sys.changePassword(null, "newpassword");
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
     Create a SecureAS400 object with system name and user id, and try using null for new password.
     A NullPointerException should be generated.
     **/
    public void Var004()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   
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
