///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SSLSignonInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.SSL;

import java.util.GregorianCalendar;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SecureAS400;

import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;

/**
 Testcase SSLSignonInfoTestcase.
 **/
public class SSLSignonInfoTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SSLSignonInfoTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SSLTest.main(newArgs); 
   }
    /**
     Get password expiration date without signing on.  Should return date.
     **/
    public void Var001()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            GregorianCalendar date = sys.getPasswordExpirationDate();
            sys.close(); 
            assertCondition(date != null, "Date is null");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Get the previous signon date without signing on.  Should return date.
     **/
    public void Var002()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            GregorianCalendar date = sys.getPreviousSignonDate();
            sys.close(); 
            assertCondition(date != null, "Date is null");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Get the signon date without signing on.  Should return date.
     **/
    public void Var003()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            GregorianCalendar date = sys.getSignonDate();
            sys.close(); 
            assertCondition(date != null, "Date is null");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Get the password expiration date after signing on.
     **/
    public void Var004()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.COMMAND);
            try
            {
                GregorianCalendar date = sys.getPasswordExpirationDate();
                sys.close(); 

                if (date != null)
                {
                    GregorianCalendar now = new GregorianCalendar();
                    assertCondition(date.after(now), "Invalid password expiration date");
                }
                else
                {
                    failed("Password expiration date is null");
                }
            }
            finally
            {
                sys.disconnectService(AS400.COMMAND);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify the previous signon date.  Signon, save the date, and signon again.
     **/
    public void Var005()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            GregorianCalendar lastDate = sys.getSignonDate();

            AS400 newsys = new AS400(sys);
            GregorianCalendar prevDate = newsys.getPreviousSignonDate();
            newsys.close(); 
            sys.close(); 
            assertCondition(prevDate.equals(lastDate), "Previous signon date not the same");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Get the current signon date.
     **/
    public void Var006()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.DATAQUEUE);
            try
            {
                GregorianCalendar date = sys.getSignonDate();
                assertCondition(date != null, "Did not get a current signon date");
            }
            finally
            {
                sys.disconnectService(AS400.DATAQUEUE);
                sys.close(); 
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the password expiration warning date to a large number.
     We should get the password expiration warning.
     **/
    public void Var007()
    {
        if (runMode_ != UNATTENDED)
        {
            try
            {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
                sys.setMustUseSockets(mustUseSockets_);
                if (!JTOpenTestEnvironment.isOS400)
                {
                    output_.println("You should see a password expiration warning.  Press No.");
                }

                int oldVal = SecureAS400.getPasswordExpirationWarningDays();
                try
                {
                    SecureAS400.setPasswordExpirationWarningDays(500);
                    sys.connectService(AS400.COMMAND);
                    sys.disconnectService(AS400.COMMAND);
                    sys.close(); 
                    succeeded();
                }
                finally
                {
                    SecureAS400.setPasswordExpirationWarningDays(oldVal);
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
        }
    }

    /**
     Verify that get password expiration warning works properly.
     **/
    public void Var008()
    {
        try
        {
            int oldVal = SecureAS400.getPasswordExpirationWarningDays();
            try
            {
                SecureAS400.setPasswordExpirationWarningDays(5);
                assertCondition(SecureAS400.getPasswordExpirationWarningDays() == 5, "password expiration days is incorrect");
            }
            finally
            {
                SecureAS400.setPasswordExpirationWarningDays(oldVal);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
