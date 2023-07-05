///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecSignonTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.net.InetAddress;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;

/**
 Testcase SecSignonTestcase.
 **/
public class SecSignonTestcase extends Testcase
{
    /**
     Create a default AS400 object and connect.
     A signon prompt should be displayed, with default user enabled and unchecked, and cache password enabled and checked.
     **/
    public void Var001()
    {
        if (runMode_ != UNATTENDED)
        {
            try
            {
                AS400 sys = new AS400();
                sys.setMustUseSockets(mustUseSockets_);

                if (onAS400_ == false)
                {
                    output_.println(" ");
                    output_.println(" ");
                    output_.println("Verify:");
                    output_.println("  - system name is not filled in");
                    output_.println("  - user ID is not filled in");
                    output_.println("  - password is not filled in");
                    output_.println("  - default user check box is enabled");
                    output_.println("  - default user check box is unchecked");
                    output_.println("  - use password cache check box is enabled");
                    output_.println("  - use password cache check box is checked");
                    output_.println("Signon to the AS/400 with a valid signon");
                    output_.println(" ");
                    output_.println(" ");
                }
                try
                {
                    sys.connectService(AS400.COMMAND);
                    assertCondition(onAS400_ == false || isNative_ == true, "No exception thrown");
                }
                catch (Exception e)
                {
                    if (onAS400_ == true && isNative_ == false && exceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET))
                    {
                        succeeded();
                    }
                    else
                    {
                        failed(e, "Unexpected exception");
                    }
                }
                finally
                {
                    sys.disconnectService(AS400.COMMAND);
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
	} else {
	    notApplicable(); 
	} 
    }

    /**
     Create an AS400 object with only system name.
     A password prompt should be displayed.  Verify that the default user check box is enabled and checked, and use cache check box is enabled and checked.
     **/
    public void Var002()
    {
        if (runMode_ != UNATTENDED)
        {
            try
            {
                AS400 sys = new AS400(systemName_);
                sys.setMustUseSockets(mustUseSockets_);

                if (onAS400_ == false)
                {
                    output_.println(" ");
                    output_.println(" ");
                    output_.println("Verify:");
                    output_.println("  - system name is filled in");
                    output_.println("  - user ID is not filled in");
                    output_.println("  - password is not filled in");
                    output_.println("  - default user check box is enabled");
                    output_.println("  - default user check box is checked");
                    output_.println("  - use password cache check box is enabled");
                    output_.println("  - use password cache check box is checked");
                    output_.println("Signon to the AS/400 with a valid signon");
                    output_.println(" ");
                    output_.println(" ");
                }

                try
                {
                    sys.connectService(AS400.COMMAND);
                    assertCondition(onAS400_ == false || isNative_ == true, "No exception thrown");
                }
                catch (Exception e)
                {
                    if (isNative_ == false && onAS400_ == true && exceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET))
                    {
                        succeeded();
                    }
                    else
                    {
                        failed(e, "Unexpected exception");
                    }
                }
                finally
                {
                    sys.disconnectService(AS400.COMMAND);
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
	} else {
	    notApplicable(); 
        }
    }

    /**
     Construct an AS400 object with system name and user id.
     A password prompt should be displayed.  Verify that the default user check box is enabled and checked, and use cache check box is enabled and checked.
     **/
    public void Var003()
    {
        if (runMode_ != UNATTENDED)
        {
            try
            {
                AS400 sys = new AS400(systemName_, userId_);
                sys.setMustUseSockets(mustUseSockets_);

                if (onAS400_ == false)
                {
                    output_.println(" ");
                    output_.println(" ");
                    output_.println("Verify:");
                    output_.println("  - system name is filled in");
                    output_.println("  - user ID is filled in");
                    output_.println("  - password is not filled in");
                    output_.println("  - default user check box is enabled");
                    output_.println("  - default user check box is checked");
                    output_.println("  - use password cache check box is enabled");
                    output_.println("  - use password cache check box is checked");
                    output_.println("Signon to the AS/400 with a valid signon");
                    output_.println(" ");
                    output_.println(" ");
                }

                try
                {
                    sys.connectService(AS400.COMMAND);
                    assertCondition(onAS400_ == false || isNative_ == true, "No exception thrown");
                }
                catch (Exception e)
                {
                    if (isNative_ == false && onAS400_ == true && exceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET))
                    {
                        succeeded();
                    }
                    else
                    {
                        failed(e, "Unexpected exception");
                    }
                }
                finally
                {
                    sys.disconnectService(AS400.COMMAND);
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
	} else {
	    notApplicable(); 
      }
    }

    /**
     Construct an AS400 object with system name and user id, cancel the signon.
     A password prompt should be displayed.  Verify the correct error is received.
     **/
    public void Var004()
    {
        if (runMode_ != UNATTENDED)
        {
            try
            {
                AS400 sys = new AS400(systemName_, userId_);
                sys.setMustUseSockets(mustUseSockets_);

                if (onAS400_ == false)
                {
                    output_.println(" ");
                    output_.println(" ");
                    output_.println("Cancel the signon dialog");
                    output_.println(" ");
                    output_.println(" ");
                }

                try
                {
                    sys.connectService(AS400.COMMAND);
                    assertCondition(onAS400_ == false || isNative_ == true, "No exception thrown");
                }
                catch (Exception e)
                {
                    if (onAS400_ && isNative_ == false)
                    {
                        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET);
                    }
                    else
                    {
                        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.SIGNON_CANCELED);
                    }
                }
                finally
                {
                    sys.disconnectService(AS400.COMMAND);
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
	} else {
	    notApplicable(); 
        }
    }

    /**
     Signon and set the default user.
     Verify that default user is set correctly.
     **/
    public void Var005()
    {
        if (runMode_ != UNATTENDED)
        {
            try
            {
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
                    AS400 sys = new AS400(systemName_, userId_);
                    sys.setMustUseSockets(mustUseSockets_);

                    output_.println(" ");
                    output_.println(" ");
                    output_.println("Sign on to the AS/400");
                    output_.println("Check the default user checkbox");
                    output_.println(" ");
                    output_.println(" ");

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        sys.disconnectService(AS400.COMMAND);

                        String uid = AS400.getDefaultUser(systemName_);
                        assertCondition(uid.equals(userId_.toUpperCase()));
                    }
                    finally
                    {
                        AS400.clearPasswordCache();
                        AS400.removeDefaultUser(systemName_);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
	} else {
	    notApplicable(); 
        }
    }

    /**
     Try to set a default user after it has been set.
     **/
    public void Var006()
    {
        if (runMode_ != UNATTENDED)
        {
            try
            {
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
                    AS400 sys = new AS400(systemName_, userId_);
                    sys.setMustUseSockets(mustUseSockets_);

                    output_.println(" ");
                    output_.println(" ");
                    output_.println("Sign on to the AS/400");
                    output_.println("Check the default user checkbox");

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        sys.disconnectService(AS400.COMMAND);

                        sys = new AS400();
                        output_.println(" ");
                        output_.println(" ");
                        output_.println("Sign on to " + systemName_);
                        output_.println("Check the default user check box");
                        output_.println("Uncheck the cache password cache checkbox");
                        output_.println("A warning message should be displayed");
                        output_.println(" ");
                        output_.println(" ");

                        sys.connectService(AS400.COMMAND);
                        sys.disconnectService(AS400.COMMAND);

                        succeeded();
                    }
                    finally
                    {
                        AS400.clearPasswordCache();
                        AS400.removeDefaultUser(systemName_);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
        } else {
          notApplicable(); 
        }
    }

    /**
     Create an AS400 object with only the system name after the default user has been set.
     Verify that the user ID is set to the default user.
     **/
    public void Var007()
    {
        if (runMode_ != UNATTENDED)
        {
            try
            {
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
                    AS400 sys = new AS400(systemName_, userId_);
                    sys.setMustUseSockets(mustUseSockets_);

                    output_.println(" ");
                    output_.println(" ");
                    output_.println("Testing default user.  There should be two password prompts for this variation.");
                    output_.println("If you don't see a second prompt before the next variation, fail this one.");
                    output_.println("Check the default user checkbox");
                    output_.println("Uncheck the save password checkbox");
                    output_.println("signon");

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        sys.disconnectService(AS400.COMMAND);

                        String uid = AS400.getDefaultUser(systemName_);
                        if (uid.equals(userId_.toUpperCase()))
                        {
                            AS400 sys2 = new AS400(systemName_);
                            sys2.setMustUseSockets(mustUseSockets_);

                            output_.println(" ");
                            output_.println(" ");
                            output_.println("User ID field should be filled in");
                            output_.println("Verify that the user ID matches the previous signon");
                            output_.println("then signon");
                            output_.println(" ");
                            output_.println(" ");

                            sys2.connectService(AS400.COMMAND);
                            sys2.disconnectService(AS400.COMMAND);

                            succeeded();
                        }
                        else
                        {
                            failed("default user not set");
                        }
                    }
                    finally
                    {
                        AS400.clearPasswordCache();
                        AS400.removeDefaultUser(systemName_);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
	} else {
	    notApplicable(); 
        }
    }

    /**
     Connect using the system object.
     Verify that the password cache is not set and the default user is not set.
     **/
    public void Var008()
    {
        if (runMode_ != UNATTENDED)
        {
            try
            {
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                    AS400 sys = new AS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
                    sys.setMustUseSockets(mustUseSockets_);

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        sys.disconnectService(AS400.COMMAND);

                        AS400 sys2 = new AS400(systemName_);
                        sys2.setMustUseSockets(mustUseSockets_);

                        output_.println(" ");
                        output_.println(" ");
                        output_.println("Verify:");
                        output_.println("  - user ID is not set");
                        output_.println("  - default user check box is enabled");
                        output_.println("  - default user check box is checked");
                        output_.println("  - cache password check box is enabled");
                        output_.println("  - cache password check box is checked");
                        output_.println(" ");
                        output_.println(" ");

                        sys2.connectService(AS400.COMMAND);
                        sys2.disconnectService(AS400.COMMAND);

                        succeeded();
                    }
                    finally
                    {
                        AS400.clearPasswordCache();
                        AS400.removeDefaultUser(systemName_);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
	} else {
	    notApplicable(); 
        }
    }

    /**
     Verify that set default user works.
     **/
    public void Var009()
    {
        try
        {
            AS400.setDefaultUser(systemName_, "myUserIDxY");
            try
            {
                String uid = AS400.getDefaultUser(systemName_);

                assertCondition(uid.equals("MYUSERIDXY"), "User ID not correct: " + uid);
            }
            finally
            {
                AS400.removeDefaultUser(systemName_);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify that remove default user works.
     **/
    public void Var010()
    {
        try
        {
            AS400.setDefaultUser(systemName_, userId_);
            AS400.removeDefaultUser(systemName_);

            String uid = AS400.getDefaultUser(systemName_);
            assertCondition(uid == null, "remove default user failed");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify that the password is cached and the user id is the default user.
     **/
    public void Var011()
    {
        if (runMode_ == UNATTENDED) { notApplicable("Attended TC");} else
        {
            try
            {
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
                    AS400 sys = new AS400(systemName_, userId_);
                    sys.setMustUseSockets(mustUseSockets_);

                    output_.println(" ");
                    output_.println(" ");
                    output_.println("Testing password cache.  You should only see one password");
                    output_.println("prompt for this variation.");
                    output_.println("Check the default user check box");
                    output_.println("Check the cache password check box");
                    output_.println("signon");
                    output_.println("If you see another prompt before the next variation, fail this variation,");
                    output_.println("even if this variation says it is successful.");
                    output_.println(" ");
                    output_.println(" ");

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        sys.disconnectService(AS400.COMMAND);

                        AS400 sys2 = new AS400(systemName_);
                        sys2.setMustUseSockets(mustUseSockets_);

                        sys2.connectService(AS400.COMMAND);
                        sys2.disconnectService(AS400.COMMAND);

                        String uid = sys2.getUserId();
                        assertCondition(uid.equals(sys.getUserId()), "user id does not match");
                    }
                    finally
                    {
                        AS400.clearPasswordCache();
                        AS400.removeDefaultUser(systemName_);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
        }
    }

    /**
     Verify that remove cache entry works.
     **/
    public void Var012()
    {
        if (runMode_ == UNATTENDED) { notApplicable("Attended TC");} else
        {
            try
            {
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
                    AS400 sys = new AS400(systemName_, userId_);
                    sys.setMustUseSockets(mustUseSockets_);

                    output_.println(" ");
                    output_.println(" ");
                    output_.println("Testing password cache, there should be two password prompts");
                    output_.println("for this variation.");
                    output_.println("Check the default user check box");
                    output_.println("Check the cache password check box");
                    output_.println("signon");

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        sys.disconnectService(AS400.COMMAND);

                        AS400.removePasswordCacheEntry(systemName_, userId_);

                        AS400 sys2 = new AS400(systemName_, userId_);
                        sys2.setMustUseSockets(mustUseSockets_);

                        output_.println(" ");
                        output_.println(" ");
                        output_.println("There should be a prompt for password right now.");
                        output_.println("If a password prompt is not displayed right now, fail this variation");
                        output_.println(" ");
                        output_.println(" ");

                        sys2.connectService(AS400.COMMAND);
                        sys2.disconnectService(AS400.COMMAND);

                        succeeded();
                    }
                    finally
                    {
                        AS400.clearPasswordCache();
                        AS400.removeDefaultUser(systemName_);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
        }
    }

    /**
     Verify that clear password entry by system works.
     **/
    public void Var013()
    {
        if (runMode_ == UNATTENDED) { notApplicable("Attended TC");} else
        {
            try
            {
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
                    AS400 sys = new AS400(systemName_, userId_);
                    sys.setMustUseSockets(mustUseSockets_);

                    output_.println(" ");
                    output_.println(" ");
                    output_.println("Testing clear password cache.  This variation should show two password");
                    output_.println("prompts.");
                    output_.println("Check the default user check box");
                    output_.println("Check the cache password check box");
                    output_.println("signon");

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        sys.disconnectService(AS400.COMMAND);

                        AS400.clearPasswordCache(systemName_);

                        AS400 sys2 = new AS400(systemName_, userId_);
                        sys2.setMustUseSockets(mustUseSockets_);

                        output_.println(" ");
                        output_.println(" ");
                        output_.println("There should be a prompt for password right now.");
                        output_.println("If one is not displayed right now, fail this variation.");
                        output_.println(" ");
                        output_.println(" ");

                        sys2.connectService(AS400.COMMAND);
                        sys2.disconnectService(AS400.COMMAND);

                        succeeded();
                    }
                    finally
                    {
                        AS400.clearPasswordCache();
                        AS400.removeDefaultUser(systemName_);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
        }
    }

    /**
     Verify that clear password cache for all systems works.
     **/
    public void Var014()
    {
        if (runMode_ == UNATTENDED) { notApplicable("Attended TC");} else
        {
            try
            {
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
                    AS400 sys = new AS400(systemName_, userId_);
                    sys.setMustUseSockets(mustUseSockets_);

                    output_.println(" ");
                    output_.println(" ");
                    output_.println("Check the default user check box");
                    output_.println("Check the cache password check box");
                    output_.println("signon");

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        sys.disconnectService(AS400.COMMAND);

                        AS400.clearPasswordCache();

                        AS400 sys2 = new AS400(systemName_, userId_);
                        sys2.setMustUseSockets(mustUseSockets_);

                        output_.println(" ");
                        output_.println(" ");
                        output_.println("There should be a prompt for password right now.");
                        output_.println(" ");
                        output_.println(" ");

                        sys2.connectService(AS400.COMMAND);
                        sys2.disconnectService(AS400.COMMAND);

                        succeeded();
                    }
                    finally
                    {
                        AS400.clearPasswordCache();
                        AS400.removeDefaultUser(systemName_);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "unexpected exception");
            }
        }
    }

    /**
     Call addPasswordCacheEntry and verify that the password is cached and the user id is correct.
     **/
    public void Var015()
    {
        try
        {
          char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400.addPasswordCacheEntry(systemName_, userId_, charPassword);
            PasswordVault.clearPassword(charPassword);
            try
            {
                AS400 sys = new AS400(systemName_, userId_);
                sys.setMustUseSockets(mustUseSockets_);

                sys.connectService(AS400.COMMAND);
                try
                {
                    assertCondition(sys.isConnected(AS400.COMMAND), "Connect failed");
                }
                finally
                {
                    sys.disconnectService(AS400.COMMAND);
                }
            }
            finally
            {
                AS400.clearPasswordCache();
                AS400.removeDefaultUser(systemName_);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Call addPasswordCacheEntry and verify that remove cache entry works.
     **/
    public void Var016()
    {
        if (runMode_ == UNATTENDED) { notApplicable("Attended TC");} else
        {
            try
            {
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
            char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                    AS400.addPasswordCacheEntry(systemName_, userId_, charPassword);
                      PasswordVault.clearPassword(charPassword);
                    try
                    {
                        AS400.removePasswordCacheEntry(systemName_, userId_);

                        AS400 sys = new AS400(systemName_, userId_);
                        sys.setMustUseSockets(mustUseSockets_);

                        output_.println(" ");
                        output_.println(" ");
                        output_.println("There should be a prompt for password right now.");
                        output_.println("If a password prompt is not displayed right now, fail this variation");
                        output_.println(" ");
                        output_.println(" ");

                        sys.connectService(AS400.COMMAND);
                        sys.disconnectService(AS400.COMMAND);

                        succeeded();
                    }
                    finally
                    {
                        AS400.clearPasswordCache();
                        AS400.removeDefaultUser(systemName_);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
        }
    }

    /**
     Call addPasswordCacheEntry and verify that clear password entry by system works.
     **/
    public void Var017()
    {
        if (runMode_ == UNATTENDED) { notApplicable("Attended TC");} else
        {
            try
            {
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
            char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                    AS400.addPasswordCacheEntry(systemName_, userId_, charPassword);
                      PasswordVault.clearPassword(charPassword);
                    try
                    {
                        AS400.clearPasswordCache(systemName_);

                        AS400 sys = new AS400(systemName_, userId_);
                        sys.setMustUseSockets(mustUseSockets_);

                        output_.println(" ");
                        output_.println(" ");
                        output_.println("There should be a prompt for password right now.");
                        output_.println("If one is not displayed right now, fail this variation.");
                        output_.println(" ");
                        output_.println(" ");

                        sys.connectService(AS400.COMMAND);
                        sys.disconnectService(AS400.COMMAND);

                        succeeded();
                    }
                    finally
                    {
                        AS400.clearPasswordCache();
                        AS400.removeDefaultUser(systemName_);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception");
            }
        }
    }

    /**
     Call addPasswordCacheEntry and verify that clear password cache for all systems works.
     **/
    public void Var018()
    {
        if (runMode_ == UNATTENDED) { notApplicable("Attended TC");} else
        {
            try
            {
                if (onAS400_)
                {
                    succeeded();
                }
                else
                {
            char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                    AS400.addPasswordCacheEntry(systemName_, userId_, charPassword);
                      PasswordVault.clearPassword(charPassword);
                    try
                    {
                        AS400.clearPasswordCache();

                        AS400 sys = new AS400(systemName_, userId_);
                        sys.setMustUseSockets(mustUseSockets_);

                        output_.println(" ");
                        output_.println(" ");
                        output_.println("There should be a prompt for password right now.");
                        output_.println(" ");
                        output_.println(" ");

                        sys.connectService(AS400.COMMAND);
                        sys.disconnectService(AS400.COMMAND);

                        succeeded();
                    }
                    finally
                    {
                        AS400.clearPasswordCache();
                        AS400.removeDefaultUser(systemName_);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "unexpected exception");
            }
        }
    }

    /**
     Call addPasswordCacheEntry with a system name of null.
     Verify NullPointerException is received.
     **/
    public void Var019()
    {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        try
        {
            
            AS400.addPasswordCacheEntry(null, userId_, charPassword);
            failed("No exception.");
        }
        catch(Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "systemName"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
          PasswordVault.clearPassword(charPassword);
    }

    /**
     If on the AS400, call addPasswordCacheEntry using "localhost" as the system name, connect to command.
     **/
    public void Var020()
    {
        try
        {
            if (onAS400_ && isNative_)
            {
            char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400.addPasswordCacheEntry("localhost", userId_, charPassword);
                try
                {
		    // To connect without password, must be current user
		    String currentUser= System.getProperty("user.name").toUpperCase();
                    AS400 sys = new AS400(systemName_, currentUser);
                    sys.setMustUseSockets(mustUseSockets_);

                    sys.connectService(AS400.COMMAND);
                    try
                    {
                        assertCondition(sys.isConnected(AS400.COMMAND), "Connect failed");
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
                 PasswordVault.clearPassword(charPassword);
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call addPasswordCacheEntry using "" as the system name, connect to command.
     **/
    public void Var021()
    {
        try
        {
            if (onAS400_ && isNative_)
            {
            char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400.addPasswordCacheEntry("", userId_, charPassword);
                try
                {
		    String currentUser= System.getProperty("user.name").toUpperCase();

                    AS400 sys = new AS400(systemName_, currentUser);
                    sys.setMustUseSockets(mustUseSockets_);

                    sys.connectService(AS400.COMMAND);
                    try
                    {
                        assertCondition(sys.isConnected(AS400.COMMAND), "Connect failed");
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
                 PasswordVault.clearPassword(charPassword);
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call addPasswordCacheEntry using the local system name, connect to command.
     **/
    public void Var022()
    {
        try
        {
            if (onAS400_ && isNative_)
            {
            char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400.addPasswordCacheEntry(InetAddress.getLocalHost().getHostName(), userId_, charPassword);
  PasswordVault.clearPassword(charPassword);
  try
                {
		    String currentUser= System.getProperty("user.name").toUpperCase();

                    AS400 sys = new AS400(systemName_, currentUser);
                    sys.setMustUseSockets(mustUseSockets_);

                    sys.connectService(AS400.COMMAND);
                    try
                    {
                        assertCondition(sys.isConnected(AS400.COMMAND), "Connect failed");
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Call addPasswordCacheEntry with a user ID of null.
     Verify NullPointerException is received.
     **/
    public void Var023()
    {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        try
        {
            
            AS400.addPasswordCacheEntry(systemName_, null, charPassword);
            failed("No exception.");
        }
        catch(Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "userId"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
        PasswordVault.clearPassword(charPassword);
  
    }

    /**
     If on the AS400, call addPasswordCacheEntry using "" as the user ID, connect to command.
     **/
    public void Var024()
    {
        try
        {
            if (onAS400_ && isNative_)
            {
		String currentUser= System.getProperty("user.name").toUpperCase();
       char[] charPassword;     
		if (currentUser.equalsIgnoreCase(userId_)) {
 charPassword = PasswordVault.decryptPassword(encryptedPassword_);
AS400.addPasswordCacheEntry(systemName_, "", charPassword);
		} else if (currentUser.equalsIgnoreCase(pwrSysUserID_)) {
 charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
AS400.addPasswordCacheEntry(systemName_, "", charPassword);
		} else {
		    failed("Current user must be same as -uid or -pwrSys");
		    return; 
		} 
  PasswordVault.clearPassword(charPassword);
                try
                {

                    AS400 sys = new AS400(systemName_, currentUser);
                    sys.setMustUseSockets(mustUseSockets_);

                    sys.connectService(AS400.COMMAND);
                    try
                    {
                        assertCondition(sys.isConnected(AS400.COMMAND), "Connect failed");
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call addPasswordCacheEntry using "*current" as the user ID, connect to command.
     **/
    public void Var025()
    {
        try
        {
            if (onAS400_ && isNative_)
            {
		String currentUser= System.getProperty("user.name").toUpperCase();
	             
	if (currentUser.equalsIgnoreCase(userId_)) {
	  char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
		    AS400.addPasswordCacheEntry(systemName_, "*current", charPassword);
		      PasswordVault.clearPassword(charPassword);
		} else if (currentUser.equalsIgnoreCase(pwrSysUserID_)) {
		  char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
		    AS400.addPasswordCacheEntry(systemName_, "*current", charPassword);
		      PasswordVault.clearPassword(charPassword);
		} else {
		    failed("Current user must be same as -uid or -pwrSys");
		    return; 
		} 
                try
                {
                    AS400 sys = new AS400(systemName_, currentUser);
                    sys.setMustUseSockets(mustUseSockets_);

                    sys.connectService(AS400.COMMAND);
                    try
                    {
                        assertCondition(sys.isConnected(AS400.COMMAND), "Connect failed");
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Call addPasswordCacheEntry with a password of null.
     Verify NullPointerException is received.
     **/
    public void Var026()
    {
        try
        {
            AS400.addPasswordCacheEntry(systemName_, userId_, (char[])null);
            failed("No exception.");
        }
        catch(Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "password"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     If on the AS400, call addPasswordCacheEntry using "" as the password, connect to command.
     **/
    public void Var027()
    {
        try
        {
            if (onAS400_ && isNative_)
            {
		String currentUser= System.getProperty("user.name").toUpperCase();

                AS400.addPasswordCacheEntry(systemName_, currentUser, "".toCharArray());
                try
                {
                    AS400 sys = new AS400(systemName_, currentUser);
                    sys.setMustUseSockets(mustUseSockets_);

                    sys.connectService(AS400.COMMAND);
                    try
                    {
                        assertCondition(sys.isConnected(AS400.COMMAND), "Connect failed");
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call addPasswordCacheEntry using "*current" as the password, connect to command.
     **/
    public void Var028()
    {
        try
        {
            if (onAS400_ && isNative_)
            {
		String currentUser= System.getProperty("user.name").toUpperCase();

                AS400.addPasswordCacheEntry(systemName_, currentUser, "*current".toCharArray());
                try
                {
                    AS400 sys = new AS400(systemName_, currentUser);
                    sys.setMustUseSockets(mustUseSockets_);

                    sys.connectService(AS400.COMMAND);
                    try
                    {
                        assertCondition(sys.isConnected(AS400.COMMAND), "Connect failed");
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Call clearPasswordCache with a system name of null.
     Verify NullPointerException is received.
     **/
    public void Var029()
    {
        try
        {
            AS400.clearPasswordCache(null);
            failed("No exception.");
        }
        catch(Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "systemName"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     If on the AS400, call clearPasswordCache using "localhost" as the system name, attempt to connect to command.
     Verify AS400SecurityException received.
     **/
    public void Var030()
    {
        try
        {
      if (onAS400_ && !isNative_ && isLocal_) {
        // For this test, don't use the same id as the job. If using
        // the same id as the job, the password information will be
        // cached in the CurrentUserClass
        String userId = userId_;
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

        String currentUser = System.getProperty("user.name").toUpperCase();
        if (currentUser.equalsIgnoreCase(userId)) {
          userId = pwrSysUserID_;

          charPassword = PasswordVault
              .decryptPassword(pwrSysEncryptedPassword_);

        }

        AS400.addPasswordCacheEntry(systemName_, userId, charPassword);
        
          PasswordVault.clearPassword(charPassword);
        try {
          AS400.clearPasswordCache("localhost");
          AS400.clearPasswordCache(systemName_);

                    AS400 sys = new AS400(systemName_, userId);
                    sys.setMustUseSockets(mustUseSockets_);

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        failed("No exception. Connected with "+userId+" to "+systemName_);
                    }
                    catch (Exception e)
                    {
                        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET);
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call clearPasswordCache using "" as the system name, attempt to connect to command.
     Verify AS400SecurityException received.
     **/
    public void Var031()
    {
        try
        {
            if (onAS400_ && !isNative_ && isLocal_)
            {
		// For this test, don't use the same id as the job.  If using
		// the same id as the job, the password information will be
		// cached in the CurrentUserClass
		String userId = userId_;
		char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

		String currentUser= System.getProperty("user.name").toUpperCase();
		if (currentUser.equalsIgnoreCase(userId)) {
		    userId = pwrSysUserID_;
		    charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
		    
		} 

                AS400.addPasswordCacheEntry(systemName_, userId, charPassword);
                  PasswordVault.clearPassword(charPassword);
                try
                {

                    AS400.clearPasswordCache("");
                    AS400.clearPasswordCache(systemName_);

                    AS400 sys = new AS400(systemName_, userId);
                    sys.setMustUseSockets(mustUseSockets_);

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        failed("No exception. Connected with "+userId+" to "+systemName_);
                    }
                    catch (Exception e)
                    {
                        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET);
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call clearPasswordCache using the local system name, attempt to connect to command.
     Verify AS400SecurityException received.
     **/
    public void Var032()
    {
        try
        {
            if (onAS400_ && !isNative_ && isLocal_)
            {

		// For this test, don't use the same id as the job.  If using
		// the same id as the job, the password information will be
		// cached in the CurrentUserClass
		String userId = userId_;
		
		 char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

		String currentUser= System.getProperty("user.name").toUpperCase();
		if (currentUser.equalsIgnoreCase(userId)) {
		    userId = pwrSysUserID_;
		     charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);

		   
		} 

                AS400.addPasswordCacheEntry(systemName_, userId, charPassword);
                  PasswordVault.clearPassword(charPassword);
                try
                {
		    String hostname = InetAddress.getLocalHost().getHostName(); 
                    AS400.clearPasswordCache(hostname);
                    AS400.clearPasswordCache(systemName_);

                    AS400 sys = new AS400(systemName_, userId);
                    sys.setMustUseSockets(mustUseSockets_);

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        failed("No exception. Connected with "+userId+" to "+systemName_+" hostname="+hostname);
                    }
                    catch (Exception e)
                    {
                        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET);
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Call removePasswordCacheEntry with a system name of null.
     Verify NullPointerException is received.
     **/
    public void Var033()
    {
        try
        {
            AS400.removePasswordCacheEntry(null, userId_);
            failed("No exception.");
        }
        catch(Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "systemName"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     If on the AS400, call removePasswordCacheEntry using "localhost" as the system name, attempt to connect to command.
     Verify AS400SecurityException received.
     **/
    public void Var034()
    {
        try
        {
            if (onAS400_ && !isNative_ && isLocal_)
            {
		// For this test, don't use the same id as the job.  If using
		// the same id as the job, the password information will be
		// cached in the CurrentUserClass
		String userId = userId_;

		            char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

		String currentUser= System.getProperty("user.name").toUpperCase();
		if (currentUser.equalsIgnoreCase(userId)) {
		    userId = pwrSysUserID_;
		      PasswordVault.clearPassword(charPassword);
		      charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
		    
		} 

                AS400.addPasswordCacheEntry(systemName_, userId, charPassword);
                  PasswordVault.clearPassword(charPassword);
                try
                {
                    AS400.removePasswordCacheEntry("localhost", userId);

                    AS400 sys = new AS400(systemName_, userId);
                    sys.setMustUseSockets(mustUseSockets_);

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        failed("No exception. connected with "+userId+" to "+systemName_);
                    }
                    catch (Exception e)
                    {
                        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET);
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call removePasswordCacheEntry using "" as the system name, attempt to connect to command.
     Verify AS400SecurityException received.
     **/
    public void Var035()
    {
        try
        {
            if (onAS400_ && !isNative_ && isLocal_)
            {
		// For this test, don't use the same id as the job.  If using
		// the same id as the job, the password information will be
		// cached in the CurrentUserClass
		String userId = userId_;
		            char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

		String currentUser= System.getProperty("user.name").toUpperCase();
		if (currentUser.equalsIgnoreCase(userId)) {
		    userId = pwrSysUserID_;
		   
		              PasswordVault.clearPassword(charPassword);
          charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);

		} 

                AS400.addPasswordCacheEntry(systemName_, userId, charPassword);
                                PasswordVault.clearPassword(charPassword);

                try
                {
                    AS400.removePasswordCacheEntry("", userId);

                    AS400 sys = new AS400(systemName_, userId);
                    sys.setMustUseSockets(mustUseSockets_);

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        failed("No exception. connected with "+userId+" to "+systemName_);
                    }
                    catch (Exception e)
                    {
                        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET);
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call removePasswordCacheEntry using the local system name, attempt to connect to command.
     Verify AS400SecurityException received.
     **/
    public void Var036()
    {
        try
        {
            if (onAS400_ && !isNative_ && isLocal_)
            {
		// For this test, don't use the same id as the job.  If using
		// the same id as the job, the password information will be
		// cached in the CurrentUserClass
		String userId = userId_;
		            char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

		String currentUser= System.getProperty("user.name").toUpperCase();
		if (currentUser.equalsIgnoreCase(userId)) {
		    userId = pwrSysUserID_;
	         PasswordVault.clearPassword(charPassword);
          charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
	} 

                AS400.addPasswordCacheEntry(systemName_, userId, charPassword);
                PasswordVault.clearPassword(charPassword);
                try
                {
                    AS400.removePasswordCacheEntry(InetAddress.getLocalHost().getHostName(), userId);

                    AS400 sys = new AS400(systemName_, userId);
                    sys.setMustUseSockets(mustUseSockets_);

                    try
                    {
                        sys.connectService(AS400.COMMAND);
                        failed("No exception. Connected with "+userId);
                    }
                    catch (Exception e)
                    {
                        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET);
                    }
                    finally
                    {
                        sys.disconnectService(AS400.COMMAND);
                    }
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Call removePasswordCacheEntry with a user ID of null.
     Verify NullPointerException is received.
     **/
    public void Var037()
    {
        try
        {
            AS400.removePasswordCacheEntry(systemName_, null);
            failed("No exception.");
        }
        catch(Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "userId"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     If on the AS400, call removePasswordCacheEntry using "" as the user ID, attempt to connect to command.
     Verify AS400SecurityException received.
     **/
    public void Var038()
    {
        try
        {
            if (onAS400_ && !isNative_)
            {
                          char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

                AS400.addPasswordCacheEntry(systemName_, userId_, charPassword);
                                PasswordVault.clearPassword(charPassword);

                try
                {
                    AS400.removePasswordCacheEntry(systemName_, "");

                    AS400 sys = new AS400(systemName_, userId_);
                    sys.setMustUseSockets(mustUseSockets_);

                    sys.connectService(AS400.COMMAND);
                    succeeded();
                    sys.disconnectService(AS400.COMMAND);
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call removePasswordCacheEntry using "*current" as the user ID, attempt to connect to command.
     Verify AS400SecurityException received.
     **/
    public void Var039()
    {
        try
        {
            if (onAS400_ && !isNative_)
            {
                          char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

                AS400.addPasswordCacheEntry(systemName_, userId_, charPassword);
                                PasswordVault.clearPassword(charPassword);

                try
                {
                    AS400.removePasswordCacheEntry(systemName_, "*current");

                    AS400 sys = new AS400(systemName_, userId_);
                    sys.setMustUseSockets(mustUseSockets_);

                    sys.connectService(AS400.COMMAND);
                    succeeded();
                    sys.disconnectService(AS400.COMMAND);
                }
                finally
                {
                    AS400.clearPasswordCache();
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Call getDefaultUser with a system name of null.
     Verify NullPointerException is received.
     **/
    public void Var040()
    {
        try
        {
            AS400.getDefaultUser(null);
            failed("No exception.");
        }
        catch(Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "systemName"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     If on the AS400, call getDefaultUser using "" as the system name.
     **/
    public void Var041()
    {
        try
        {
            if (onAS400_ && isNative_ && isLocal_)
            {
                AS400.setDefaultUser(systemName_, "myUserIDxY");
                try
                {
                    String uid = AS400.getDefaultUser("");

                    assertCondition(uid.equals("MYUSERIDXY"), "User id not correct: " + uid);
                }
                finally
                {
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call getDefaultUser using "localhost" as the system name.
     **/
    public void Var042()
    {
        try
        {
            if (onAS400_ && isNative_ && isLocal_)
            {
                AS400.setDefaultUser(systemName_, "myUserIDxY");
                try
                {
                    String uid = AS400.getDefaultUser("localhost");

                    assertCondition(uid.equals("MYUSERIDXY"), "User id not correct: " + uid);
                }
                finally
                {
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call getDefaultUser using the local system name.
     **/
    public void Var043()
    {
        try
        {
            if (onAS400_ && isNative_ && isLocal_)
            {
                AS400.setDefaultUser(systemName_, "myUserIDxY");
                try
                {
                    String uid = AS400.getDefaultUser(InetAddress.getLocalHost().getHostName());

                    assertCondition(uid.equals("MYUSERIDXY"), "User id not correct: " + uid);
                }
                finally
                {
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Call removeDefaultUser with a system name of null.
     Verify NullPointerException is received.
     **/
    public void Var044()
    {
        try
        {
            AS400.removeDefaultUser(null);
            failed("No exception.");
        }
        catch(Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "systemName"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     If on the AS400, call removeDefaultUser using "" as the system name.
     **/
    public void Var045()
    {
        try
        {
            if (onAS400_ && isNative_ && isLocal_)
            {
                try
                {
                    AS400.setDefaultUser(systemName_, userId_);
                    AS400.removeDefaultUser("");

                    String uid = AS400.getDefaultUser(systemName_);
                    assertCondition(uid == null, "remove default user failed");
                }
                catch (Exception e)
                {
                    failed(e, "Unexpected exception");
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call removeDefaultUser using "localhost" as the system name.
     **/
    public void Var046()
    {
        try
        {
            if (onAS400_ && isNative_ && isLocal_)
            {
                try
                {
                    AS400.setDefaultUser(systemName_, userId_);
                    AS400.removeDefaultUser("localhost");

                    String uid = AS400.getDefaultUser(systemName_);
                    assertCondition(uid == null, "remove default user failed");
                }
                catch (Exception e)
                {
                    failed(e, "Unexpected exception");
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call removeDefaultUser using the local system name.
     **/
    public void Var047()
    {
        try
        {
            if (onAS400_ && isNative_ && isLocal_)
            {
                try
                {
                    AS400.setDefaultUser(systemName_, userId_);
                    AS400.removeDefaultUser(InetAddress.getLocalHost().getHostName());

                    String uid = AS400.getDefaultUser(systemName_);
                    assertCondition(uid == null, "remove default user failed");
                }
                catch (Exception e)
                {
                    failed(e, "Unexpected exception");
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Call setDefaultUser with a system name of null.
     Verify NullPointerException is received.
     **/
    public void Var048()
    {
        try
        {
            AS400.setDefaultUser(null, "myUserIDxY");
            failed("No exception.");
        }
        catch(Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "systemName"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     If on the AS400, call setDefaultUser using "" as the system name.
     **/
    public void Var049()
    {
        try
        {
            if (onAS400_ && isNative_ && isLocal_)
            {
                AS400.setDefaultUser("", "myUserIDxY");
                try
                {
                    String uid = AS400.getDefaultUser(systemName_);

                    assertCondition(uid.equals("MYUSERIDXY"), "User id not correct: " + uid);
                }
                finally
                {
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call setDefaultUser using "localhost" as the system name.
     **/
    public void Var050()
    {
        try
        {
            if (onAS400_ && isNative_ && isLocal_)
            {
                AS400.setDefaultUser("localhost", "myUserIDxY");
                try
                {
                    String uid = AS400.getDefaultUser(systemName_);

                    assertCondition(uid.equals("MYUSERIDXY"), "User id not correct: " + uid);
                }
                finally
                {
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call setDefaultUser using the local system name.
     **/
    public void Var051()
    {
        try
        {
            if (onAS400_ && isNative_ && isLocal_)
            {
                AS400.setDefaultUser(InetAddress.getLocalHost().getHostName(), "myUserIDxY");
                try
                {
                    String uid = AS400.getDefaultUser(systemName_);

                    assertCondition(uid.equals("MYUSERIDXY"), "User id not correct: " + uid);
                }
                finally
                {
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Call setDefaultUser with a user ID of null.
     Verify NullPointerException is received.
     **/
    public void Var052()
    {
        try
        {
            AS400.setDefaultUser(systemName_, null);
            failed("No exception.");
        }
        catch(Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "userId"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     If on the AS400, call setDefaultUser using "" as the user ID, attempt to connect to command.
     Verify AS400SecurityException received.
     **/
    public void Var053()
    {
        try
        {
            if (onAS400_ && isNative_)
            {
                AS400.setDefaultUser(systemName_, "");
                try
                {
		    String currentUser= System.getProperty("user.name").toUpperCase();

                    String uid = AS400.getDefaultUser(systemName_);

                    assertCondition(uid.equals(currentUser), "User ID not correct: " + uid);
                }
                finally
                {
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     If on the AS400, call setDefaultUser using "*current" as the user ID, attempt to connect to command.
     Verify AS400SecurityException received.
     **/
    public void Var054()
    {
        try
        {
            if (onAS400_ && isNative_)
            {
                AS400.setDefaultUser(systemName_, "*current");
                try
                {
                    String uid = AS400.getDefaultUser(systemName_);

		String currentUser= System.getProperty("user.name").toUpperCase();

                    assertCondition(uid.equals(currentUser), "User ID not correct: " + uid);
                }
                finally
                {
                    AS400.removeDefaultUser(systemName_);
                }
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
