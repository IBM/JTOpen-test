///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecSignonInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.GregorianCalendar;
import com.ibm.as400.access.AS400;

/**
 Testcase SecSignonInfoTestcase.
 **/
public class SecSignonInfoTestcase extends Testcase
{
    /**
     Get password expiration date without signing on.  Should return date.
     **/
    public void Var001()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 sys = new AS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            GregorianCalendar date = sys.getPasswordExpirationDate();
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
            AS400 sys = new AS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            GregorianCalendar date = sys.getPreviousSignonDate();
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
            AS400 sys = new AS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            GregorianCalendar date = sys.getSignonDate();
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
            AS400 sys = new AS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.connectService(AS400.COMMAND);
            try
            {
                GregorianCalendar date = sys.getPasswordExpirationDate();

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
            AS400 sys = new AS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            GregorianCalendar lastDate = sys.getSignonDate();

            AS400 newsys = new AS400(sys);
            GregorianCalendar prevDate = newsys.getPreviousSignonDate();
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
            AS400 sys = new AS400(systemName_, userId_, charPassword);
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
                AS400 sys = new AS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
                sys.setMustUseSockets(mustUseSockets_);
                if (!onAS400_)
                {
                    output_.println("You should see a password expiration warning.  Press No.");
                }

                int oldVal = AS400.getPasswordExpirationWarningDays();
                try
                {
                    AS400.setPasswordExpirationWarningDays(500);
                    sys.connectService(AS400.COMMAND);
                    sys.disconnectService(AS400.COMMAND);

                    succeeded();
                }
                finally
                {
                    AS400.setPasswordExpirationWarningDays(oldVal);
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
	} else {
	    notApplicable("Attended testcase"); 
        }
    }

    /**
     Verify that get password expiration warning works properly.
     **/
    public void Var008()
    {
        try
        {
            int oldVal = AS400.getPasswordExpirationWarningDays();
            try
            {
                AS400.setPasswordExpirationWarningDays(5);
                assertCondition(AS400.getPasswordExpirationWarningDays() == 5, "password expiration days is incorrect");
            }
            finally
            {
                AS400.setPasswordExpirationWarningDays(oldVal);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
    
    //TODO - Start
    /** 
     * Test getSystemPasswordExpirationWarning Use system value QPWDEXPWRN to return expire warning days.
    **/
    /**
     * 
     */
	public void Var009() {
		if(getSystemVRM()<=VRM_V5R4M0){
			notApplicable("This capability is supported with V6R1M0 and later systems with V6R1M0 5761SS1 PTF SI48808 or V7R1M0 5770SS1 PTF SI48809.");
			return; 
		}
    AS400.setPasswordExpirationWarningDays(7);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
		AS400 sys = new AS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
		sys.setUseSystemPasswordExpirationWarningDays(true);
		AS400.setPasswordExpirationWarningDays(-1);
	
		try {
			int expire = sys.getSystemPasswordExpirationWarningDays();
			int globalExpire = AS400.getPasswordExpirationWarningDays(); 
			assertCondition(globalExpire != expire, "password expiration days is incorrect globalExpire="+globalExpire+" expire="+expire);
		} catch (Exception e) {
			failed(e, "Unexpected exception");
		}

	}
	
	 /** 
     * Use system value QPWDEXPWRN to return expire warning days.
    **/
	public void Var010() {
		if(getSystemVRM()<=VRM_V5R4M0){
			notApplicable("This capability is supported with V6R1M0 and later systems with V6R1M0 5761SS1 PTF SI48808 or V7R1M0 5770SS1 PTF SI48809.");
			return;// Add by Guang Ming Pi/China/IBM. This feature was not supported on v5r4, and return statement was missed.
		}
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
		AS400 sys = new AS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
		sys.setUseSystemPasswordExpirationWarningDays(false);
		AS400.setPasswordExpirationWarningDays(-1);
	
		try {
			int expire = sys.getSystemPasswordExpirationWarningDays();
			assertCondition(AS400.getPasswordExpirationWarningDays() == expire, "password expiration days is incorrect");
		} catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}
	
//	public void Var011() {
//  char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
//		AS400 sys = new AS400(systemName_, userId_, charPassword);
//   PasswordVault.clearPassword(charPassword);
//		AS400.setPasswordExpirationWarningDays(-1);
//		try {
//			boolean is = sys.isInPasswordExpirationWarningDays();
//			assertCondition(is, "password expiration days is incorrect");
//		} catch (Exception e) {
//			failed(e, "Unexpected exception");
//		}
//	}
  
 //TODO - End
}
