///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecPTMiscTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sec;

import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.security.auth.AS400BasicAuthenticationPrincipal;
import com.ibm.as400.security.auth.ProfileTokenCredential;
import com.ibm.as400.security.auth.ProfileTokenCredentialBeanInfo;
import com.ibm.as400.security.auth.RetrieveFailedException;
import com.ibm.as400.security.auth.UserProfilePrincipal;

import test.JDReflectionUtil;
import test.PasswordVault;
import test.SecAuthTest;
import test.Testcase;

/**
 Testcase SecPTMiscTestcase contains miscellaneous tests for the ProfileTokenCredentail object.
 <p>Test variations cover the following:
 <ul>
 <li>serialization.
 <li>bean behavior.
 <li>JAAS initialize methods.
 <li>alternate ctors.
 </ul>
 **/
public class SecPTMiscTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SecPTMiscTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SecAuthTest.main(newArgs); 
   }
  
  
  public void setup() throws Exception {
    SecAuthTest.createProfiles(pwrSys_); 
  } 

  public void cleanup() throws Exception {
    SecAuthTest.deleteProfiles(pwrSys_); 
  } 

    /**
     Test serialization and restoration of an uninitialized profile token.
     **/
    public void Var001()
    {
        try
        {
            ProfileTokenCredential pt1 = null;
            ProfileTokenCredential pt2 = null;

            // Create a single-use ProfileTokenCredential with a 60 second timeout.
            pt1 = new ProfileTokenCredential();

            // Serialize the token.
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ptoken.dat"));
            out.writeObject(pt1);
            out.close();

            // Deserialize the token.
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("ptoken.dat"));
            pt2 = (ProfileTokenCredential)in.readObject();
            in.close();

            // Test token attributes.
            assertCondition(pt2.equals(pt1) && pt2.getSystem() == null && pt2.getPrincipal() == null && pt2.getToken() == null, "Unexpected attribute value.");

            File fd = new File("ptoken.dat");
            fd.delete();
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test serialization and restoration of an active profile token.
     **/
    public void Var002()
    {
        AS400 sys = null;
        if (isLocal_)
        {
            sys = new AS400("localhost", "*CURRENT", "*CURRENT".toCharArray());
        }
        else
        {
            sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray());
        }
        try
        {
          sys.setGuiAvailable(false);
            ProfileTokenCredential pt1 = null;
            ProfileTokenCredential pt2 = null;

            // Create a single-use ProfileTokenCredential with a 10 min timeout.
            pt1 = new ProfileTokenCredential();
            pt1.setSystem(sys);
            pt1.setTimeoutInterval(600);
            pt1.setTokenType(ProfileTokenCredential.TYPE_SINGLE_USE);
            pt1.setTokenExtended(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray());

            // Test token validity; retrieve the token time to expiration.
            pt1.getTimeToExpiration();

            // Serialize the token.
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ptoken.dat"));
            out.writeObject(pt1);
            out.close();

            // Deserialize the token.
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("ptoken.dat"));
            pt2 = (ProfileTokenCredential)in.readObject();
            in.close();

            File fd = new File("ptoken.dat");
            fd.delete();

            // Reset the system user and password (not serialized).
            if (isLocal_)
            {
                pt2.getSystem().setUserId("*CURRENT");
                pt2.getSystem().setPassword("*CURRENT".toCharArray());
            }
            else
            {
                pt2.getSystem().setUserId(SecAuthTest.uid2);
                pt2.getSystem().setPassword(SecAuthTest.pwd2.toCharArray());
            }

            // Test token validity; compare the token time to expiration.
            assertCondition(pt1.equals(pt2) && pt2.getTimeToExpiration() >= pt1.getTimeToExpiration(), "Unexpected time to expiration.");

            try { sys.close(); } catch (Exception e) {} 
            pt2.getSystem().disconnectAllServices();

            SecAuthTest.removeToken(pwrSys_, pt1.getToken());
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify the list of bean info events.
     **/
    public void Var003()
    {
        try
        {
            String[] names =
            {
                "propertyChange",
                "propertyChange",
                "as400Credential"
            };
            EventSetDescriptor[] descs = new ProfileTokenCredentialBeanInfo().getAdditionalBeanInfo()[0].getEventSetDescriptors();
            assertCondition(SecAuthTest.verifyDescriptors(names, descs), "Expected event not found.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify the list of bean info properties.
     **/
    public void Var004()
    {
        try
        {
            String[] names1 =
            {
                "current",
                "destroyed",
                "renewable",
                "timed",
                "timeToExpiration",
                "system",
                "principal"
            };
            PropertyDescriptor[] descs1 = new ProfileTokenCredentialBeanInfo().getAdditionalBeanInfo()[0].getPropertyDescriptors();
            String[] names2 =
            {
                "token",
                "tokenType",
                "timeoutInterval"
            };
            PropertyDescriptor[] descs2 = new ProfileTokenCredentialBeanInfo().getPropertyDescriptors();

            assertCondition(SecAuthTest.verifyDescriptors(names1, descs1) && SecAuthTest.verifyDescriptors(names2, descs2), "Expected property not found.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test ctor allowing system, token, and timeout values.
     **/
    public void Var005()
    {
        try
        {
          /* ProfileTokenCredential pt = new ProfileTokenCredential(systemObject_, tBytes, ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE, 444); */
          
          // Create a bogus token value.
            byte[] tBytes = new byte[ProfileTokenCredential.TOKEN_LENGTH];
            new Random().nextBytes(tBytes);
            Class[] argTypes = new Class[4];
            Object[] args = new Object[4]; 
            args[0] = systemObject_; 
            argTypes[0] = args[0].getClass(); 
            args[1]=tBytes; 
            argTypes[1] = args[1].getClass(); 
            args[2] = new Integer(ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE);
            argTypes[2] = Integer.TYPE; 
            args[3] = new Integer(444); 
            argTypes[3] = Integer.TYPE; 
            
            // Create the token object.
            ProfileTokenCredential pt  = (ProfileTokenCredential) JDReflectionUtil.createObject("com.ibm.as400.security.auth.ProfileTokenCredential",argTypes, args); 
            // Test the values.
            assertCondition(pt.getSystem().equals(systemObject_) && SecAuthTest.compareBytes(pt.getToken(), tBytes) && pt.getTokenType() == ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE && pt.getTimeoutInterval() == 444, "Values specified on constructor not assigned.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful initialization.
     **/
    public void Var006()
    {
        try
        {
            // Check if system where test profiles were created is local.
            if (!isLocal_)
            {
                notApplicable();
                return;
            }
            // Create and initialize the principal.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            upp.initialize(SecAuthTest.uid2);
            // Create and initialize the credential.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.initialize(upp, SecAuthTest.pwd2.toCharArray(), false, true, true, 444);
            // Test the initialized values.
            assertCondition(pt.getPrincipal().equals(upp) && pt.getToken() != null && pt.getTimeoutInterval() == 444 && pt.getSystem() != null && pt.isReusable() && pt.isRenewable() && !pt.isPrivate(), "Incorrect initialized value.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed initialization (parm mismatch).
     **/
    public void Var007()
    {
        try
        {
            // Create and initialize the principal.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            upp.initialize(SecAuthTest.uid1);
            // Not applicable when running on client.
            if (!isNative_)
            {
                notApplicable();
                return;
            }
            // Create and initialize the credential.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            try
            {
                // Specify reusable but not renewable (not valid for tokens).
                pt.initialize(upp, "XXBADPWDXX".toCharArray(), true, false, true, 3600);
            }
            catch (Throwable e)
            {
                assertCondition(e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID, "Unexpected exception during initialization.");
            }
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed initialization (bad password).
     **/
    public void Var008()
    {
        try
        {
            // Check if system where test profiles were created is local.
            if (!isLocal_)
            {
                notApplicable();
                return;
            }
            // Create and initialize the principal.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            upp.initialize(SecAuthTest.uid1);
            // Create and initialize the credential.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            try
            {
                pt.initialize(upp, "XXBADPWDXX".toCharArray(), true, false, false, 3600);
            }
            catch (RetrieveFailedException e)
            {
                    succeeded();
            }
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful creation of a profile token for a signed-on system and verify the same token is not returned on subsequent calls.
     **/
    public void Var009()
    {
        AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        try
        {
          sys.setGuiAvailable(false);
            ProfileTokenCredential pt = sys.getProfileToken(ProfileTokenCredential.TYPE_SINGLE_USE, 5);
            // Verify a unique token is returned.
            assertCondition(pt != null && pt != sys.getProfileToken(ProfileTokenCredential.TYPE_SINGLE_USE, 5), "Unique token not returned.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { try { sys.close(); } catch (Exception e) {}  } catch (Exception e) {} 
        }
    }

    /**
     Test successful creation of a profile token for a signed-on system based only on user ID and password authentication criteria.
     **/
    public void Var010()
    {
        AS400 sys = null;
        if (isLocal_)
        {
            sys = new AS400("localhost", "*CURRENT", "*CURRENT".toCharArray());
        }
        else
        {
            sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        }
        try
        {
          sys.setGuiAvailable(false);
            ProfileTokenCredential pt = sys.getProfileToken(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray());
            assertCondition(pt.getSystem().equals(sys) && pt.getToken() != null && pt.getTokenType() == ProfileTokenCredential.TYPE_SINGLE_USE && pt.getTimeToExpiration() > 3000, "Unexpected profile token properties.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { sys.close(); } catch (Exception e) {} 
        }
    }

    /**
     Test successful creation of a profile token for a signed-on system based on specific authentication criteria.
     **/
    public void Var011()
    {
        AS400 sys = null;
        if (isLocal_)
        {
            sys = new AS400("localhost", "*CURRENT", "*CURRENT".toCharArray());
        }
        else
        {
            sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        }
        try
        {
          sys.setGuiAvailable(false);
            ProfileTokenCredential pt = sys.getProfileToken(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray(), ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE, 444);
            assertCondition(pt.getSystem().equals(sys) && pt.getToken() != null && pt.getTokenType() == ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE && pt.getTimeToExpiration() >= 300 && pt.getTimeToExpiration() <= 444, "Unexpected profile token properties.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { sys.close(); } catch (Exception e) {} 
        }
    }

    /**
     Test successful authentication (token-based).
     **/
    public void Var012()
    {
        AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        try
        {
          sys.setGuiAvailable(false);
            sys.authenticate(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray());
            succeeded();
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { sys.close(); } catch (Exception e) {} 
        }
    }

    /**
     Test failed authentication (token-based).
     **/
    public void Var013()
    {
        AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        try
        {
          sys.setGuiAvailable(false);
            sys.authenticate("QSECXXXXXX", "ZZZZZZZZZZ".toCharArray());
            failed("Authentication did not fail as expected.");
        }
        catch (AS400SecurityException ase)
        {
            assertCondition(ase.getReturnCode() == AS400SecurityException.USERID_UNKNOWN ||  ase.getReturnCode() == AS400SecurityException.PASSWORD_INCORRECT , "Unexpected return code on security exception >> " + ase.getReturnCode());
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { sys.close(); } catch (Exception e) {} 
        }
    }

  /**
   * Test failed authentication (token-based) for unsupported password parm.
   **/
  public void Var014() {
    String user = "" ; 
    char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 sys = new AS400(systemObject_.getSystemName(), pwrSysUserID_,
        charPassword);
    PasswordVault.clearPassword(charPassword);    
    CommandCall cmd = null;
    try {
      sys.setGuiAvailable(false);
      user =  generateClientUser("TBX14");
      cmd = new CommandCall(sys);
      cmd.run("CRTUSRPRF "+user+" PASSWORD(ASDF23ASD)");

      sys.authenticate(user, "*NOPWD".toCharArray());
      failed("Authentication did not fail as expected.");
    } catch (AS400SecurityException iae) {
      StringBuffer sb = new StringBuffer();
      printStackTraceToStringBuffer(iae, sb);
      assertCondition(
          ((iae.getReturnCode() == AS400SecurityException.SIGNON_CHAR_NOT_VALID) || 
              (iae.getReturnCode() == AS400SecurityException.PASSWORD_INCORRECT)),
          "Unexpected return code on exception >> " + iae.getReturnCode()
              + "\n" + sb.toString());
    } catch (Throwable e) {
      failed(e, "Unexpected exception.");
    } finally {
      if (cmd != null) {
        try {
          cmd.run("DLTUSRPRF "+user);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      try { sys.close(); } catch (Exception e) {} 
    }
  }

  /**
   * Test failed authentication (token-based) for unsupported password parm.
   **/
  public void Var015() {
   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
      
     AS400 sys = new AS400(systemObject_.getSystemName(), pwrSysUserID_,
        charPassword);
     PasswordVault.clearPassword(charPassword);  
    CommandCall cmd = null;
    String user = ""; 
    try {
      sys.setGuiAvailable(false);
      user = generateClientUser("TBX15"); 
      cmd = new CommandCall(sys);
      cmd.run("CRTUSRPRF "+user+" PASSWORD(TBXSPTM783)");
      sys.authenticate(user, "*NOPWDCHK".toCharArray());
      failed("Authentication did not fail as expected.");
    } catch (AS400SecurityException iae) {
      StringBuffer sb = new StringBuffer();
      printStackTraceToStringBuffer(iae, sb);
      assertCondition(
          ((iae.getReturnCode() == AS400SecurityException.SIGNON_CHAR_NOT_VALID) || 
              (iae.getReturnCode() == AS400SecurityException.PASSWORD_INCORRECT)),
          "Unexpected return code on exception >> " + iae.getReturnCode()
              + "\n" + sb.toString());
    } catch (Throwable e) {
      failed(e, "Unexpected exception.");
    } finally {
      if (cmd != null) {
        try {
          cmd.run("DLTUSRPRF "+user);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      try { sys.close(); } catch (Exception e) {} 
    }
  }

    /**
     Test failed creation of a profile token due to null user parm.
     **/
    public void Var016()
    {
        AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        try
        {
          sys.setGuiAvailable(false);
            sys.getProfileToken(null, "PWD".toCharArray());
            failed("Token create did not fail as expected.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "userId");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { sys.close(); } catch (Exception e) {} 
        }
    }

    /**
     Test failed creation of a profile token due to null password parm.
     **/
    public void Var017()
    {
        AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        try
        {
          sys.setGuiAvailable(false);
            sys.getProfileToken("UID", (char[]) null);
            failed("Token create did not fail as expected.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "password");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { sys.close(); } catch (Exception e) {} 
        }
    }

    /**
     Test failed creation of a profile token due to illegal user parm.
     **/
    public void Var018()
    {
        AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        try
        {
          sys.setGuiAvailable(false);
            sys.getProfileToken("ABCDEFGHIJK", "PWD".toCharArray());
            failed("Token create did not fail as expected.");
        }
        catch (ExtendedIllegalArgumentException iae)
        {
            assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.LENGTH_NOT_VALID, "Unexpected return code on exception >> " + iae.getReturnCode());
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { sys.close(); } catch (Exception e) {} 
        }
    }

    /**
     Test failed creation of a profile token due to illegal password parm.
     **/
    public void Var019()
    {
        AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        try
        {
          sys.setGuiAvailable(false);
            sys.getProfileToken("UID", "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXY1234".toCharArray());
            failed("Token create did not fail as expected.");
        }
        catch (ExtendedIllegalArgumentException iae)
        {
            assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.LENGTH_NOT_VALID, "Unexpected return code on exception >> " + iae.getReturnCode());
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { sys.close(); } catch (Exception e) {} 
            try { sys.close(); } catch (Exception e) {}  
        }
    }

    /**
     Test failed creation of a profile token for a signed-on system by specifying password *CURRENT.
     **/
    public void Var020()
    {
        if (!isLocal_)
        {
            notApplicable();
            return;
        }
        try
        {
            AS400 system = new AS400("localhost", "*CURRENT", "*CURRENT".toCharArray());
            // Connect to command, forces pre-resolution of any user/password info.
            system.setGuiAvailable(false);
            system.connectService(AS400.COMMAND);
            try
            {
                // Attempt to generate the token.
                ProfileTokenCredential pt = system.getProfileToken(ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE, 444);
                failed("Token create did not fail as expected. pt="+pt);
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET);
            }
            catch (Throwable e)
            {
              failed(e, "Unexpected exception.");
            }
            finally
            {
                system.disconnectAllServices();
                system.close(); 
            }
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful creation of a profile token for a signed-on system based on specific authentication criteria.
     **/
    public void Var021()
    {
        AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        try
        {
          sys.setGuiAvailable(false);
            ProfileTokenCredential pt = sys.getProfileToken(ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE, 444);
            assertCondition(pt.getSystem().equals(sys) && pt.getToken() != null && pt.getTokenType() == ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE && pt.getTimeToExpiration() >= 300 && pt.getTimeToExpiration() <= 444, "Unexpected profile token properties.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { sys.close(); } catch (Exception e) {} 
        }
    }

    /**
     Test failed creation of a profile token due to illegal type parm.
     **/
    public void Var022()
    {
        AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        try
        {
          sys.setGuiAvailable(false);
            sys.getProfileToken(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1);
            failed("Token create did not fail as expected.");
        }
        catch (AS400SecurityException ase)
        {
            assertCondition(ase.getReturnCode() == AS400SecurityException.REQUEST_NOT_SUPPORTED, "Unexpected return code on exception >> " + ase.getReturnCode());
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { sys.close(); } catch (Exception e) {} 
        }
    }


    /**
     Test failed authentication (token-based) for unsupported password parm.
     Original var 14
     **/
    public void Var023()
    {
        AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        try
        {
          sys.setGuiAvailable(false);
            sys.authenticate("QSECXXXXXX", "*NOPWD".toCharArray());
            failed("Authentication did not fail as expected.");
        }
        catch (AS400SecurityException iae)
        {
	    StringBuffer sb = new StringBuffer();
	    printStackTraceToStringBuffer(iae, sb); 
            assertCondition(((iae.getReturnCode() == AS400SecurityException.SIGNON_CHAR_NOT_VALID) ||
                            (iae.getReturnCode() == AS400SecurityException.USERID_UNKNOWN)) , "Unexpected return code on exception >> " + iae.getReturnCode()+"\n"+sb.toString());
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { sys.close(); } catch (Exception e) {} 
        }
    }

    /**
     Test failed authentication (token-based) for unsupported password parm.
     Original Var 15
     **/
    public void Var024()
    {
        AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        try
        {
          sys.setGuiAvailable(false);
            sys.authenticate("QSECXXXXXX", "*NOPWDCHK".toCharArray());
            failed("Authentication did not fail as expected.");
        }
        catch (AS400SecurityException iae)
        {
	    StringBuffer sb = new StringBuffer();
	    printStackTraceToStringBuffer( iae, sb);
            assertCondition(((iae.getReturnCode() == AS400SecurityException.SIGNON_CHAR_NOT_VALID) 
                || (iae.getReturnCode() == AS400SecurityException.USERID_UNKNOWN)), 
                "Unexpected return code on exception >> " + iae.getReturnCode()+"\n"+sb.toString());
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try { sys.close(); } catch (Exception e) {} 
        }
    }

    /**
     * Test failed authentication (token-based) for unsupported password parm.
     * Another variant var014 with incorrect character
     **/
    public void Var025() {
	char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
	AS400 sys = new AS400(systemObject_.getSystemName(), pwrSysUserID_,
			      charPassword);
	PasswordVault.clearPassword(charPassword);    
      CommandCall cmd = null;
    String user = "USER"; 

      try {
        sys.setGuiAvailable(false);
        user = generateClientUser("TBX25"); 
        cmd = new CommandCall(sys);
        cmd.run("CRTUSRPRF "+user+" PASSWORD(TBXSPTM783)");

        sys.authenticate(user, "*NOPWD\u00c0".toCharArray());
        failed("Authentication did not fail as expected.");
      } catch (AS400SecurityException iae) {
        StringBuffer sb = new StringBuffer();
        printStackTraceToStringBuffer(iae, sb);
        assertCondition(
            ((iae.getReturnCode() == AS400SecurityException.SIGNON_CHAR_NOT_VALID)),
            "Unexpected return code on exception >> " + iae.getReturnCode()
                + "\n" + sb.toString());
      } catch (Throwable e) {
        failed(e, "Unexpected exception.");
      } finally {
        if (cmd != null) {
          try {
            cmd.run("DLTUSRPRF "+user+" ");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        try { sys.close(); } catch (Exception e) {} 
      }
    }

    /**
     * Test failed authentication (token-based) for unsupported password parm.
     * Another variant of var015 with incorrect character
     **/
    public void Var026() {
      AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1,
          SecAuthTest.pwd1.toCharArray());
      CommandCall cmd = null;
    String user = "USER"; 

      try {
        sys.setGuiAvailable(false);
        user = generateClientUser("TBX26"); 
        cmd = new CommandCall(sys);
        cmd.run("CRTUSRPRF "+user+" PASSWORD(TBXSPTM783)");
        sys.authenticate(user, "*NOPWDCHK\u00c0".toCharArray());
        failed("Authentication did not fail as expected.");
      } catch (AS400SecurityException iae) {
        StringBuffer sb = new StringBuffer();
        printStackTraceToStringBuffer(iae, sb);
        assertCondition(
            ((iae.getReturnCode() == AS400SecurityException.SIGNON_CHAR_NOT_VALID)),
            "Unexpected return code on exception >> " + iae.getReturnCode()
                + "\n" + sb.toString());
      } catch (Throwable e) {
        failed(e, "Unexpected exception.");
      } finally {
        if (cmd != null) {
          try {
            cmd.run("DLTUSRPRF "+user+" ");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        try { sys.close(); } catch (Exception e) {} 
      }
    }

    
    public void Var027() { notApplicable();}
    public void Var028() { notApplicable();}
    public void Var029() { notApplicable();}
    public void Var030() { notApplicable();}
    public void Var031() { notApplicable();}
    /* Tests for Additional Authentication Factor (duplicated from above) */ 

   /**
    Test serialization and restoration of an active profile token.
    **/
   public void Var032() {
     if (checkAdditionalAuthenticationFactor(systemName_)) {
       AS400 sys = null;
       if (isLocal_) {
         sys = new AS400("localhost", "*CURRENT", "*CURRENT".toCharArray());
       } else {
         sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray());
       }
       try {
         sys.setGuiAvailable(false);
         initMfaUser();
         ProfileTokenCredential pt1 = null;
         ProfileTokenCredential pt2 = null;

         // Create a single-use ProfileTokenCredential with a 10 min timeout.

         pt1 = new ProfileTokenCredential();
         pt1.setSystem(sys);
         initializeProfileToken (pt1, mfaUserid_, mfaPassword_, mfaFactor_, 4 /* AuthenticationIndicator.APPLICATION_AUTHENTICATION */ , 
             "", /* verification id  */
             "", 0, /* remote IP and port */
             "", 0, /* local IP and port */
             true, /* private */
             false, /* not reusable */
             false, /* not renewable */
             600); /* timeout */

         // Test token validity; retrieve the token time to expiration.
         pt1.getTimeToExpiration();

         // Serialize the token.
         ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ptoken.dat"));
         out.writeObject(pt1);
         out.close();

         // Deserialize the token.
         ObjectInputStream in = new ObjectInputStream(new FileInputStream("ptoken.dat"));
         pt2 = (ProfileTokenCredential) in.readObject();
         in.close();

         File fd = new File("ptoken.dat");
         fd.delete();

         // Reset the system user and password (not serialized).
         if (isLocal_) {
           pt2.getSystem().setUserId("*CURRENT");
           pt2.getSystem().setPassword("*CURRENT".toCharArray());
         } else {
           pt2.getSystem().setUserId(SecAuthTest.uid2);
           pt2.getSystem().setPassword(SecAuthTest.pwd2.toCharArray());
         }

         // Test token validity; compare the token time to expiration.
         assertCondition(pt1.equals(pt2) && pt2.getTimeToExpiration() >= pt1.getTimeToExpiration(),
             "Unexpected time to expiration.");

         try { sys.close(); } catch (Exception e) {} 
         pt2.getSystem().disconnectAllServices();

         SecAuthTest.removeToken(pwrSys_, pt1.getToken());
       } catch (Throwable e) {
         failed(e, "Unexpected exception.");
       }
     }
   }

   public void Var033() { notApplicable(); }
   public void Var034() { notApplicable(); }
   public void Var035() { notApplicable(); }
   public void Var036() { notApplicable(); }
   public void Var037() { notApplicable(); }
   public void Var038() { notApplicable(); }
   public void Var039() { notApplicable(); }
 
   public void initializeProfileToken(ProfileTokenCredential pt, String userid, char[] password, char[] factor, int authenticationIndicator,  
       String verificationId, String remoteIp, int remotePort, String localIp, int localPort, boolean isPrivate, boolean reusable, boolean renewable, int timeoutInterval ) throws Exception {
     UserProfilePrincipal principal = new UserProfilePrincipal(userid);
     Class[] argTypes; 
     Object[] args; 
     Object info; 

     argTypes = new Class[5];
     args = new Object[5]; 
     args[0] = verificationId; argTypes[0] = args[0].getClass();
     args[1] = remoteIp;   argTypes[1] = args[1].getClass(); 
     args[2] = new Integer(remotePort);  argTypes[2] = Integer.TYPE; 
     args[3] = localIp; argTypes[3] = args[3].getClass(); 
     args[4] = new Integer(localPort);  argTypes[4] = Integer.TYPE; 
     info = JDReflectionUtil.createObject("com.ibm.as400.security.auth.ProfileTokenEnhancedInfo", argTypes, args);
     
     argTypes = new Class[9];
     args = new Object[9]; 
     args[0] = principal;   argTypes[0] = AS400BasicAuthenticationPrincipal.class; 
     args[1] = password;    argTypes[1] = args[1].getClass();
     args[2] = factor;      argTypes[2] = args[2].getClass();
     args[3] = new Integer(authenticationIndicator);  argTypes[3] = Integer.TYPE; 
     args[4] = new Boolean(isPrivate);   argTypes[4] = Boolean.TYPE;
     args[5] = new Boolean(reusable);  argTypes[5] = Boolean.TYPE;
     args[6] = new Boolean(renewable);  argTypes[6] = Boolean.TYPE;
     args[7] = new Integer(timeoutInterval); argTypes[7] = Integer.TYPE; 
     args[8] = info; argTypes[8] = info.getClass();   
  
 
     JDReflectionUtil.callMethod_V(pt, "initialize",  argTypes, args);
     
     // com.ibm.as400.security.auth.ProfileTokenEnhancedInfo info2 = new com.ibm.as400.security.auth.ProfileTokenEnhancedInfo(verificationId, remoteIp, remotePort, localIp, localPort);
     // pt.initialize(principal, password, factor, authenticationIndicator, isPrivate, reusable, renewable, timeoutInterval, info2);
         
         
   }
   /**
    Test successful creation of a profile token for a signed-on system based only on user ID and password authentication criteria.
    **/
   public void Var040()
   {
     if (checkAdditionalAuthenticationFactor(systemName_)) {
       AS400 sys = null;
       if (isLocal_)
       {
           sys = new AS400("localhost", "*CURRENT", "*CURRENT".toCharArray());
       }
       else
       {
           sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
       }
       try
       {
         sys.setGuiAvailable(false);
         initMfaUser();
           int timeoutInterval = 3600; 
           ProfileTokenCredential pt =  new ProfileTokenCredential();
           pt.setSystem(sys);
          
           
           initializeProfileToken(pt,mfaUserid_, mfaPassword_, mfaFactor_,  5 /* AuthenticationIndicator.APPLICATION_AUTHENTICATION */ , 
               "", /* verification id  */
               "", 0, /* remote IP and port */
               "", 0, /* local IP and port */
               true, /* private */
               false, /* not reusable */
               false, /* not renewable */
               timeoutInterval); /* timeout */
           boolean passed = true; 
           StringBuffer sb = new StringBuffer(); 
           if (!isLocal_) {
             if (!pt.getSystem().getSystemName().equals(sys.getSystemName())) {
               passed = false;
               sb.append("pt.getSystem():" + pt.getSystem().getSystemName() + " != sys:" + sys.getSystemName() + "\n");
             }
           }
           if ( pt.getToken() == null) {
             passed =false; 
             sb.append("pt.getToken() is null\n"); 
           }
           if (pt.getTokenType() != ProfileTokenCredential.TYPE_SINGLE_USE) {
             passed = false; 
             sb.append("pt.getTokenType():"+pt.getTokenType()+" != ProfileTokenCredential.TYPE_SINGLE_USE:"+ProfileTokenCredential.TYPE_SINGLE_USE+"\n"); 
           }
           if (pt.getTimeToExpiration() < 3000 )  {
             passed = false; 
             sb.append("pt.GetTimeExpiration() = "+pt.getTimeToExpiration()+" sb >= 3000 ");
           }

           
           assertCondition(passed, sb); 
             }
       catch (Throwable e)
       {
           failed(e, "Unexpected exception.");
       }
       finally
       {
           try { sys.close(); } catch (Exception e) {} 
       }
     }
   }

   /**
    Test successful creation of a profile token for a signed-on system based on specific authentication criteria.
    **/
   public void Var041()
   {
     if (checkAdditionalAuthenticationFactor(systemName_)) {
       AS400 sys = null;
       if (isLocal_)
       {
           sys = new AS400("localhost", "*CURRENT", "*CURRENT".toCharArray());
       }
       else
       {
           sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
       }
       try
       {
         sys.setGuiAvailable(false);
         initMfaUser();
         
   
           ProfileTokenCredential pt =  new ProfileTokenCredential();
           pt.setSystem(sys);
           initializeProfileToken ( pt, mfaUserid_,  mfaPassword_, mfaFactor_, 5 /* AuthenticationIndicator.APPLICATION_AUTHENTICATION */ , 
               "", /* verification id  */
               "", 0, /* remote IP and port */
               "", 0, /* local IP and port */
               true, /* private */
               true, /*  reusable */
               false, /* not renewable */
               444); /* timeout */

           boolean passed = true; 
           StringBuffer sb = new StringBuffer(); 
           if (!isLocal_) {
             if (!pt.getSystem().getSystemName().equals(sys.getSystemName())) {
               passed = false;
               sb.append("pt.getSystem():" + pt.getSystem().getSystemName() + " != sys:" + sys.getSystemName() + "\n");
             }
           }
           if ( pt.getToken() == null) {
             passed =false; 
             sb.append("pt.getToken() is null\n"); 
           }
           if (pt.getTokenType() != ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE) {
             passed = false; 
             sb.append("pt.getTokenType():"+pt.getTokenType()+" != ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE:"+ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE+"\n"); 
           }
           int timeToExpiration = pt.getTimeToExpiration();
           if ( timeToExpiration < 300 || timeToExpiration >  444) {
             passed = false; 
             sb.append("pt.GetTimeExpiration() = "+timeToExpiration+" sb >= 300 and <= 444");
           }
           assertCondition( passed, sb); 
           
       }
       catch (Throwable e)
       {
           failed(e, "Unexpected exception.");
       }
       finally
       {
           try { sys.close(); } catch (Exception e) {} 
       }
   }
   }
   public void Var042() {notApplicable(); } 
   public void Var043() {notApplicable(); } 
 public void Var044() {notApplicable(); } 
 public void Var045() {notApplicable(); } 
   /**
    Test failed creation of a profile token due to null user parm.
    **/
   public void Var046()
   {
     if (checkAdditionalAuthenticationFactor(systemName_)) {
       AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
       try
       {
         sys.setGuiAvailable(false);
           sys.getProfileToken(null, "PWD".toCharArray());
           failed("Token create did not fail as expected.");
       }
       catch (Exception e)
       {
           assertExceptionIs(e, "NullPointerException", "userId");
       }
       catch (Throwable e)
       {
           failed(e, "Unexpected exception.");
       }
       finally
       {
           try { sys.close(); } catch (Exception e) {} 
       }
   }
   }
   /**
    Test failed creation of a profile token due to null password parm.
    **/
   public void Var047()
   {
     if (checkAdditionalAuthenticationFactor(systemName_)) {

       AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
       try
       {
         sys.setGuiAvailable(false);
           sys.getProfileToken("UID", (char[]) null);
           failed("Token create did not fail as expected.");
       }
       catch (Exception e)
       {
           assertExceptionIs(e, "NullPointerException", "password");
       }
       catch (Throwable e)
       {
           failed(e, "Unexpected exception.");
       }
       finally
       {
           try { sys.close(); } catch (Exception e) {} 
       }
   }
   }
   /**
    Test failed creation of a profile token due to illegal user parm.
    **/
   public void Var048()
   {
     if (checkAdditionalAuthenticationFactor(systemName_)) {

       AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
       try
       {
         sys.setGuiAvailable(false);
           sys.getProfileToken("ABCDEFGHIJK", "PWD".toCharArray());
           failed("Token create did not fail as expected.");
       }
       catch (ExtendedIllegalArgumentException iae)
       {
           assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.LENGTH_NOT_VALID, "Unexpected return code on exception >> " + iae.getReturnCode());
       }
       catch (Throwable e)
       {
           failed(e, "Unexpected exception.");
       }
       finally
       {
           try { sys.close(); } catch (Exception e) {} 
       }
   }
   }
   /**
    Test failed creation of a profile token due to illegal password parm.
    **/
   public void Var049()
   {
     if (checkAdditionalAuthenticationFactor(systemName_)) {
       AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
       try
       {
         sys.setGuiAvailable(false);
           sys.getProfileToken("UID", "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXY1234".toCharArray());
           failed("Token create did not fail as expected.");
       }
       catch (ExtendedIllegalArgumentException iae)
       {
           assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.LENGTH_NOT_VALID, "Unexpected return code on exception >> " + iae.getReturnCode());
       }
       catch (Throwable e)
       {
           failed(e, "Unexpected exception.");
       }
       finally
       {
           try { sys.close(); } catch (Exception e) {} 
       }
   }
   }
   /**
    Test failed creation of a profile token for a signed-on system by specifying password *CURRENT.
    **/
   public void Var050()
   {
     if (checkAdditionalAuthenticationFactor(systemName_)) {
       if (!isLocal_)
       {
           notApplicable();
           return;
       }
       try
       {
           AS400 system = new AS400("localhost", "*CURRENT", "*CURRENT".toCharArray());
           // Connect to command, forces pre-resolution of any user/password info.
           system.connectService(AS400.COMMAND);
           try
           {
             system.setGuiAvailable(false);
               // Attempt to generate the token.
               ProfileTokenCredential pt = system.getProfileToken(ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE, 444);
               failed("Token create did not fail as expected. pt="+pt);
           }
           catch (Exception e)
           {
               assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET);
           }
           catch (Throwable e)
           {
             failed(e, "Unexpected exception.");
           }
           finally
           {
               system.disconnectAllServices();
               system.close(); 
           }
       }
       catch (Throwable e)
       {
           failed(e, "Unexpected exception.");
       }
   }
   }
   /**
    Test successful creation of a profile token for a signed-on system based on specific authentication criteria.
    **/
   public void Var051()
   {
     if (checkAdditionalAuthenticationFactor(systemName_)) {

       AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
       try
       {
         sys.setGuiAvailable(false);
           ProfileTokenCredential pt = sys.getProfileToken(ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE, 444);
           assertCondition(pt.getSystem().equals(sys) && pt.getToken() != null && pt.getTokenType() == ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE && pt.getTimeToExpiration() >= 300 && pt.getTimeToExpiration() <= 444, "Unexpected profile token properties.");
       }
       catch (Throwable e)
       {
           failed(e, "Unexpected exception.");
       }
       finally
       {
           try { sys.close(); } catch (Exception e) {} 
       }
   }
   }
   /**
    Test failed creation of a profile token due to illegal type parm.
    **/
   public void Var052()
   {
     if (checkAdditionalAuthenticationFactor(systemName_)) {

       AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
       try
       {
         sys.setGuiAvailable(false);
           sys.getProfileToken(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 1);
           failed("Token create did not fail as expected.");
       }
       catch (AS400SecurityException ase)
       {
           assertCondition(ase.getReturnCode() == AS400SecurityException.REQUEST_NOT_SUPPORTED, "Unexpected return code on exception >> " + ase.getReturnCode());
       }
       catch (Throwable e)
       {
           failed(e, "Unexpected exception.");
       }
       finally
       {
           try { sys.close(); } catch (Exception e) {} 
       }
   }
   }

   /**
    Test failed authentication (token-based) for unsupported password parm.
    Original var 14
    **/
   public void Var053()
   {
     if (checkAdditionalAuthenticationFactor(systemName_)) {
       AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
       try
       {
         sys.setGuiAvailable(false);
           sys.authenticate("QSECXXXXXX", "*NOPWD".toCharArray());
           failed("Authentication did not fail as expected.");
       }
       catch (AS400SecurityException iae)
       {
           StringBuffer sb = new StringBuffer();
           printStackTraceToStringBuffer(iae, sb); 
           assertCondition(((iae.getReturnCode() == AS400SecurityException.SIGNON_CHAR_NOT_VALID) ||
                           (iae.getReturnCode() == AS400SecurityException.USERID_UNKNOWN)) , "Unexpected return code on exception >> " + iae.getReturnCode()+"\n"+sb.toString());
       }
       catch (Throwable e)
       {
           failed(e, "Unexpected exception.");
       }
       finally
       {
           try { sys.close(); } catch (Exception e) {} 
       }
   }
   }
   /**
    Test failed authentication (token-based) for unsupported password parm.
    Original Var 15
    **/
   public void Var054()
   {
     if (checkAdditionalAuthenticationFactor(systemName_)) {

       AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
       try
       {
         sys.setGuiAvailable(false);
           sys.authenticate("QSECXXXXXX", "*NOPWDCHK".toCharArray());
           failed("Authentication did not fail as expected.");
       }
       catch (AS400SecurityException iae)
       {
           StringBuffer sb = new StringBuffer();
           printStackTraceToStringBuffer( iae, sb);
           assertCondition(((iae.getReturnCode() == AS400SecurityException.SIGNON_CHAR_NOT_VALID) 
               || (iae.getReturnCode() == AS400SecurityException.USERID_UNKNOWN)), 
               "Unexpected return code on exception >> " + iae.getReturnCode()+"\n"+sb.toString());
       }
       catch (Throwable e)
       {
           failed(e, "Unexpected exception.");
       }
       finally
       {
           try { sys.close(); } catch (Exception e) {} 
       }
   }
   }
   /**
    * Test failed authentication (token-based) for unsupported password parm.
    * Another variant var014 with incorrect character
    **/
   public void Var055() {
     if (checkAdditionalAuthenticationFactor(systemName_)) {
       char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
       AS400 sys = new AS400(systemObject_.getSystemName(), pwrSysUserID_,
                             charPassword);
       PasswordVault.clearPassword(charPassword);    
     CommandCall cmd = null;
   String user = "USER"; 

     try {
       sys.setGuiAvailable(false);
       user = generateClientUser("TBX25"); 
       cmd = new CommandCall(sys);
       cmd.run("CRTUSRPRF "+user+" PASSWORD(TBXSPTM783)");

       sys.authenticate(user, "*NOPWD\u00c0".toCharArray());
       failed("Authentication did not fail as expected.");
     } catch (AS400SecurityException iae) {
       StringBuffer sb = new StringBuffer();
       printStackTraceToStringBuffer(iae, sb);
       assertCondition(
           ((iae.getReturnCode() == AS400SecurityException.SIGNON_CHAR_NOT_VALID)),
           "Unexpected return code on exception >> " + iae.getReturnCode()
               + "\n" + sb.toString());
     } catch (Throwable e) {
       failed(e, "Unexpected exception.");
     } finally {
       if (cmd != null) {
         try {
           cmd.run("DLTUSRPRF "+user+" ");
         } catch (Exception e) {
           e.printStackTrace();
         }
       }
       try { sys.close(); } catch (Exception e) {} 
     }
   }
   }
   /**
    * Test failed authentication (token-based) for unsupported password parm.
    * Another variant of var015 with incorrect character
    **/
   public void Var056() {
     if (checkAdditionalAuthenticationFactor(systemName_)) {
     AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1,
         SecAuthTest.pwd1.toCharArray());
     CommandCall cmd = null;
   String user = "USER"; 

     try {
       sys.setGuiAvailable(false);
       user = generateClientUser("TBX26"); 
       cmd = new CommandCall(sys);
       cmd.run("CRTUSRPRF "+user+" PASSWORD(TBXSPTM783)");
       sys.authenticate(user, "*NOPWDCHK\u00c0".toCharArray());
       failed("Authentication did not fail as expected.");
     } catch (AS400SecurityException iae) {
       StringBuffer sb = new StringBuffer();
       printStackTraceToStringBuffer(iae, sb);
       assertCondition(
           ((iae.getReturnCode() == AS400SecurityException.SIGNON_CHAR_NOT_VALID)),
           "Unexpected return code on exception >> " + iae.getReturnCode()
               + "\n" + sb.toString());
     } catch (Throwable e) {
       failed(e, "Unexpected exception.");
     } finally {
       if (cmd != null) {
         try {
           cmd.run("DLTUSRPRF "+user+" ");
         } catch (Exception e) {
           e.printStackTrace();
         }
       }
       try { sys.close(); } catch (Exception e) {} 
     }
   }
   }


}
