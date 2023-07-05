///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecValidatePasswordTestcase.java
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
 Testcase SecValidatePasswordTestcase.
 **/
public class SecValidatePasswordTestcase extends Testcase
{
    /**
     Create a default AS400 object and try to validate the user without having the system set.
     An InvalidObjectStateException should be thrown.
     (validateSignon(String, String))
     **/
    public void Var001()
    {
        try
        {
            AS400 sys = new AS400();
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                sys.validateSignon(userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
                    failed("exception not thrown");
                }
            }
            catch (Exception e)
            {
                if (onAS400_)
                {
                    failed(e, "Unexpected exception");
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
     Create a default AS400 and try to validate with null for user ID.
     A NullPointerException should be thrown.
     (validateSignon(String, String))
     **/
    public void Var002()
    {
        try
        {
            AS400 sys = new AS400(systemName_);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                sys.validateSignon(null, charPassword);
   PasswordVault.clearPassword(charPassword);
                failed("exception not thrown");
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
     Create a default AS400 and try to validate with a null for password.
     A NullPointerException should be thrown.
     (validateSignon(String, String))
     **/
    public void Var003()
    {
        try
        {
            AS400 sys = new AS400(systemName_);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
                sys.validateSignon(userId_, (char[]) null);
                failed("exception not thrown");
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
     Try to validate an invalid user ID.
     An AS400SecurityException should be thrown, with a return code of USERID_UNKNOWN.
     (validateSignon(String, String))
     **/
    public void Var004()
    {
        try
        {
            AS400 sys = new AS400(systemName_);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                sys.validateSignon("zuserx", charPassword);
   PasswordVault.clearPassword(charPassword);
                failed("exception not thrown");
            }
            catch (Exception e)
            {
		/* in 7.5 all error go to incorrect password */ 
		if (getRelease() >= JDTestDriver.RELEASE_V7R5M0  ) {
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
		} else { 
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.USERID_UNKNOWN);
		}
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try to validate an invalid password.
     An AS400SecurityException should be thrown, with a return code of PASSWORD_INCORRECT.
     (validateSignon(String, String))
     **/
    public void Var005()
    {
        try
        {
            AS400 sys = new AS400(systemName_);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
                sys.validateSignon(userId_, "abcd".toCharArray());
                failed("exception not thrown");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Validate by passing in correct user ID and password.
     This should validate with success.
     (validateSignon(String, String))
     **/
    public void Var006()
    {
        try
        {
            AS400 sys = new AS400(systemName_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            sys.validateSignon(userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a default AS400 object and try to validate the user without having the system set.
     An InvalidObjectStateException should be thrown.
     (validateSignon(String))
     **/
    public void Var007()
    {
        try
        {
            AS400 sys = new AS400();
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
               char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
		if (onAS400_ /* && isNative_ */ ) { 
		    String currentUser= System.getProperty("user.name").toUpperCase();
		    if (currentUser.equalsIgnoreCase(userId_)) {
			    // alreadset 
		    } else if (currentUser.equalsIgnoreCase(pwrSysUserID_)) {
		      charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
		    } else {
			failed("Current user must be same as -uid or -pwrSys");
			return; 
		    } 
		}
                sys.validateSignon(charPassword);
                 PasswordVault.clearPassword(charPassword);
                if (onAS400_ /* && isNative_ */ )
                {
                    succeeded();
                }
                else
                {
                    failed("exception not thrown");
                }
            }
            catch (Exception e)
            {
                if (onAS400_ && isNative_)
                {
                    failed(e, "Unexpected exception");
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
     Create a default AS400 and try to validate with null for user ID.
     An InvalidObjectStateException should be thrown.
     (validateSignon(String))
     **/
    public void Var008()
    {
        try
        {
            AS400 sys = new AS400(systemName_);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
               char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
		if (onAS400_ /*  && isNative_ */ ) { 
		    String currentUser= System.getProperty("user.name").toUpperCase();
		    if (currentUser.equalsIgnoreCase(userId_)) {
		    } else if (currentUser.equalsIgnoreCase(pwrSysUserID_)) {
			  charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
		    } else {
			failed("Current user must be same as -uid or -pwrSys");
			return; 
		    } 
		}

                sys.validateSignon(charPassword);
                 PasswordVault.clearPassword(charPassword);
                if (onAS400_ /* && isNative_ */ )
                {
                    succeeded();
                }
                else
                {
                    failed("exception not thrown");
                }
            }
            catch (Exception e)
            {
                if (onAS400_ && isNative_)
                {
                    failed(e, "Unexpected exception");
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
     Create a default AS400 and try to validate with a null for password.
     A NullPointerException should be thrown.
     (validateSignon(String))
     **/
    public void Var009()
    {
        try
        {
            AS400 sys = new AS400(systemName_, userId_);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
                sys.validateSignon((char[]) null);
                failed("exception not thrown");
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
     Try to validate an invalid user ID.
     An AS400SecurityException should be thrown, with a return code of USERID_UNKNOWN.
     (validateSignon(String))
     **/
    public void Var010()
    {
        try
        {
            AS400 sys = new AS400(systemName_, "zuserx");
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                sys.validateSignon(charPassword);
   PasswordVault.clearPassword(charPassword);
                failed("exception not thrown");
            }
            catch (Exception e)

            {
		/* in 7.5 all error go to incorrect password */ 
		if (getRelease() >= JDTestDriver.RELEASE_V7R5M0  ) {
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
		} else { 

		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.USERID_UNKNOWN);
		}
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try to validate an invalid password.
     An AS400SecurityException should be thrown, with a return code of PASSWORD_INCORRECT.
     (validateSignon(String))
     **/
    public void Var011()
    {
        try
        {
            AS400 sys = new AS400(systemName_, userId_);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
                sys.validateSignon("abcd".toCharArray());
                failed("exception not thrown");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Validate by passing in correct user ID and password.
     This should validate with success.
     (validateSignon(String))
     **/
    public void Var012()
    {
        try
        {
            AS400 sys = new AS400(systemName_, userId_);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                sys.validateSignon(charPassword);
   PasswordVault.clearPassword(charPassword);
                succeeded();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a default AS400 object and try to validate the user without having the system set.
     An InvalidObjectStateException should be thrown.
     (validateSignon())
     **/
    public void Var013()
    {
        try
        {
            AS400 sys = new AS400();
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
                sys.validateSignon();
                if (onAS400_ /*  && isNative_ */ )
                {
                    succeeded();
                }
                else
                {
                    failed("exception not thrown");
                }
            }
            catch (Exception e)
            {
                if (onAS400_ /* && isNative_ */ )
                {
                    failed(e, "Unexpected exception");
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
     Create a default AS400 and try to validate with null for user ID.
     A NullPointerException should be thrown.
     (validateSignon())
     **/
    public void Var014()
    {
        try
        {
            AS400 sys = new AS400(systemName_);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
                sys.validateSignon();
                if (onAS400_ /* && isNative_ */ )
                {
                    succeeded();
                }
                else
                {
                    failed("exception not thrown");
                }
            }
            catch (Exception e)
            {
                if (onAS400_ /* && isNative_ */ )
                {
                    failed(e, "Unexpected exception");
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
     Create a default AS400 and try to validate with a null for password.
     A NullPointerException should be thrown.
     (validateSignon())
     **/
    public void Var015()
    {
        try
        {
	    String userId = userId_; 
	    if (onAS400_ /* && isNative_ */ ) { 
		userId =  System.getProperty("user.name").toUpperCase();
	    }

            AS400 sys = new AS400(systemName_, userId);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
                sys.validateSignon();
                if (onAS400_ /* && isNative_ */ )
                {
                    succeeded();
                }
                else
                {
                    failed("exception not thrown");
                }
            }
            catch (Exception e)
            {
                if (onAS400_ /* && isNative_ */ )
                {
                    failed(e, "Unexpected exception");
                }
                else
                {
                    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET);
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try to validate an invalid user ID.
     An AS400SecurityException should be thrown, with a return code of UNKNWON_USER_ID.
     (validateSignon())
     **/
    public void Var016()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 sys = new AS400(systemName_, "zuserx", charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
                sys.validateSignon();
                failed("exception not thrown");
            }
            catch (Exception e)
            {
		/* in 7.5 all error go to incorrect password */ 
		if (getRelease() >= JDTestDriver.RELEASE_V7R5M0  ) {
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
		} else { 

		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.USERID_UNKNOWN);
		}
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Try to validate an invalid password.
     An AS400SecurityException should be thrown, with a return code of PASSWORD_INCORRECT.
     (validateSignon())
     **/
    public void Var017()
    {
        try
        {
            AS400 sys = new AS400(systemName_, userId_, "abcd".toCharArray());
            sys.setMustUseSockets(mustUseSockets_);

            try
            {
                sys.validateSignon();
                failed("exception not thrown");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Validate by passing in correct user ID and password.
     This should validate with success.
     (validateSignon())
     **/
    public void Var018()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 sys = new AS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            sys.validateSignon();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
