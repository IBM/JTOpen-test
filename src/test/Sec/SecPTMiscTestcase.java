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
import java.sql.Connection;
import java.util.Arrays;
import java.util.Random;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.AuthenticationIndicator;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.security.auth.AS400BasicAuthenticationPrincipal;
import com.ibm.as400.security.auth.ProfileTokenCredential;
import com.ibm.as400.security.auth.ProfileTokenCredentialBeanInfo;
import com.ibm.as400.security.auth.ProfileTokenEnhancedInfo;
import com.ibm.as400.security.auth.RetrieveFailedException;
import com.ibm.as400.security.auth.UserProfilePrincipal;

import test.JDJobName;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JTOpenTestEnvironment;
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
  Connection pwrConnection_; 
  
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
    AS400JDBCDriver driver = new AS400JDBCDriver();
    pwrConnection_ = driver.connect(pwrSys_); 
  } 

  public void cleanup() throws Exception {
    SecAuthTest.deleteProfiles(pwrSys_); 
  } 

    /**
     Test serialization and restoration of an uninitialized profile token.
     **/
    @SuppressWarnings({ "unlikely-arg-type", "deprecation" })
    public void Var001()
    {
       StringBuffer sb = new StringBuffer(); 
        try
        {
            boolean passed = true; 
          
            ProfileTokenCredential pt1 = null;
            ProfileTokenCredential pt2 = null;
            ProfileTokenCredential pt3 = null;

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
            
            byte[] dummyBytes = new byte[ProfileTokenCredential.TOKEN_LENGTH];
            pt3 = new ProfileTokenCredential(systemObject_, dummyBytes, ProfileTokenCredential.TYPE_SINGLE_USE, 536);
            
            if (pt1.equals(null)) {
              passed = false; sb.append("\npt1.equals(null)");
            }
            if (!pt1.equals(pt1)) { 
              passed = false; sb.append("\n!pt1.equals(pt1)");
            }
            if (pt1.equals("A_STRING")) {
              passed = false; sb.append("\npt1.equals('A_STRING')");
            }
            
            if (pt1.equals(pt3)) {
              passed = false; sb.append("\npt1.equals(pt3)");
            }
            
            ProfileTokenCredential pt4 = new ProfileTokenCredential(systemObject_, dummyBytes, ProfileTokenCredential.TYPE_SINGLE_USE, 555, "VERIFICATION_ID" , "123.23.23.23",73,"132.23.52.63",88);
            if (pt4.getSystem() == null) { 
              passed = false; sb.append("\npt4.getSystem() = null"); 
            }
            if (pt4.getToken() == null) { 
              passed = false; sb.append("\npt4.getToken() == null"); 
            }
            
            // Test token attributes.
            // assertCondition(pt2.equals(pt1) && pt2.getSystem() == null && pt2.getPrincipal() == null && pt2.getToken() == null, "Unexpected attribute value.");
            if (!pt2.equals(pt1)) { 
              passed = false; sb.append("\n pt1 != pt2");
            }
            if (pt2.getSystem() != null) { 
              passed = false; sb.append("\npt2.getSystem() != null"); 
            }
            if (pt2.getToken() != null) { 
              passed = false; sb.append("\npt2.getToken() != null"); 
            }
            
            
            // More coverage test -- including deprecated. 
            ProfileTokenCredential pt5 = new ProfileTokenCredential();
            // UserProfilePrincipal principal = new UserProfilePrincipal("TEST5"); 
            pt5.setSystem(systemObject_);
            
            UserProfilePrincipal upp = new UserProfilePrincipal(); 
            upp.initialize(SecAuthTest.uid2);
            
            
            
            pt5.initialize(upp, SecAuthTest.pwd2, true /* isPrivate */, false /*isReusable*/, false /*isRenewable */, 111); 
            if (pt5.isRenewable()) {
              passed = false; sb.append("\npt5.isRenewable()");
            }
            if (pt5.isReusable()) {
              passed = false; sb.append("\npt5.isReusable()");
            }
            assertCondition(passed, sb); 

            File fd = new File("ptoken.dat");
            fd.delete();
        }
        catch (Throwable e)
        {
            failed(e,  sb);
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
            Class<?>[] argTypes = new Class<?>[4];
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
          // Create and initialize the principal.
          UserProfilePrincipal upp = new UserProfilePrincipal();
          upp.initialize(SecAuthTest.uid2);
            // Check if system where test profiles were created is local.
            // Create and initialize the credential.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            if (!isLocal_)
            {
              upp.setSystem(systemObject_);
              pt.setSystem(systemObject_);
            }
            
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
                // notApplicable();
                // return;
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
          // Create and initialize the principal.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            // Check if system where test profiles were created is local.
            if (!isLocal_)
            {
              upp.setSystem(systemObject_);
            }
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
         
            ProfileTokenCredential pt;
            if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
              pt = sys.getProfileToken(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray(),null,ProfileTokenCredential.TYPE_SINGLE_USE, 3600, "*NOUSE","*NOUSE");
            } else {
              pt = sys.getProfileToken(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray());
            }
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
            ProfileTokenCredential pt;
            if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
              pt = sys.getProfileToken(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray(),null,ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE, 444, "*NOUSE","*NOUSE");
            } else {
              pt = sys.getProfileToken(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray(), ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE, 444);
            }
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
      cmd.run("QSYS/CRTUSRPRF "+user+" PASSWORD(ASDF23ASD)");

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
          cmd.run("QSYS/DLTUSRPRF "+user);
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
      cmd.run("QSYS/CRTUSRPRF "+user+" PASSWORD(TBXSPTM783)");
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
          cmd.run("QSYS/DLTUSRPRF "+user);
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
            notApplicable("*CURRENT usage");
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
        cmd.run("QSYS/CRTUSRPRF "+user+" PASSWORD(TBXSPTM783)");

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
            cmd.run("QSYS/DLTUSRPRF "+user+" ");
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
        cmd.run("QSYS/CRTUSRPRF "+user+" PASSWORD(TBXSPTM783)");
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
            cmd.run("QSYS/DLTUSRPRF "+user+" ");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        try { sys.close(); } catch (Exception e) {} 
      }
    }

    
    public void Var027() { notApplicable("Future variation");}
    public void Var028() { notApplicable("Future variation");}
    public void Var029() { notApplicable("Future variation");}
    public void Var030() { notApplicable("Future variation");}
    public void Var031() { notApplicable("Future variation");}
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
         char[] mfaPassword = PasswordVault.decryptPassword(mfaEncryptedPassword_);

         initializeProfileToken (pt1, mfaUserid_, mfaPassword, mfaFactor_, 4 /* AuthenticationIndicator.APPLICATION_AUTHENTICATION */ , 
             "", /* verification id  */
             "", 0, /* remote IP and port */
             "", 0, /* local IP and port */
             true, /* private */
             false, /* not reusable */
             false, /* not renewable */
             600); /* timeout */
         Arrays.fill(mfaPassword,' ');
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

   public void Var033() { notApplicable("Future variation"); }
   public void Var034() { notApplicable("Future variation"); }
   public void Var035() { notApplicable("Future variation"); }
   public void Var036() { notApplicable("Future variation"); }
   public void Var037() { notApplicable("Future variation"); }
   public void Var038() { notApplicable("Future variation"); }
   public void Var039() { notApplicable("Future variation"); }
 
   public void initializeProfileToken(ProfileTokenCredential pt, String userid, char[] password, char[] factor, int authenticationIndicator,  
       String verificationId, String remoteIp, int remotePort, String localIp, int localPort, boolean isPrivate, boolean reusable, boolean renewable, int timeoutInterval ) throws Exception {
     UserProfilePrincipal principal = new UserProfilePrincipal(userid);
     Class<?>[] argTypes; 
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
     // pt.initialize(principal, password, factor, authenticationIndicator, isPrivate, reusable, renewable, timeoutInterval, info);
         
         
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
          
           
           char[] mfaPassword = PasswordVault.decryptPassword(mfaEncryptedPassword_);
           
           initializeProfileToken(pt,mfaUserid_, mfaPassword, mfaFactor_,  5 /* AuthenticationIndicator.APPLICATION_AUTHENTICATION */ , 
               "", /* verification id  */
               "", 0, /* remote IP and port */
               "", 0, /* local IP and port */
               true, /* private */
               false, /* not reusable */
               false, /* not renewable */
               timeoutInterval); /* timeout */
           Arrays.fill(mfaPassword,' ');
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
           int minimumExpirationTime = timeoutInterval - 30 ; 
           if (minimumExpirationTime > mfaIntervalSeconds_ - 30 ) {
             minimumExpirationTime = mfaIntervalSeconds_ - 30 ; 
           }
           
           if (pt.getTimeToExpiration() < minimumExpirationTime )  {
             passed = false; 
             sb.append("pt.GetTimeExpiration() = "+pt.getTimeToExpiration()+" sb >=  "+minimumExpirationTime);
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
       try
       {
       if (isLocal_)
       {
           sys = new AS400("localhost", "*CURRENT", "*CURRENT".toCharArray());
       }
       else
       {
           sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
           sys.connectService(AS400.COMMAND);
       }
         sys.setGuiAvailable(false);
         initMfaUser();
         AuthExit.assureExitProgramExists(pwrConnection_, mfaUserid_);
         AuthExit.clearOutputFiles(pwrConnection_); 
         String jobName; 
         
           ProfileTokenCredential pt =  new ProfileTokenCredential();
           pt.setSystem(sys);
           char[] mfaPassword = PasswordVault.decryptPassword(mfaEncryptedPassword_);

           initializeProfileToken ( pt, mfaUserid_,  mfaPassword, mfaFactor_, 5 /* AuthenticationIndicator.APPLICATION_AUTHENTICATION */ , 
               "", /* verification id  */
               "", 0, /* remote IP and port */
               "", 0, /* local IP and port */
               true, /* private */
               true, /*  reusable */
               false, /* not renewable */
               444); /* timeout */
           Arrays.fill(mfaPassword, ' ');
           boolean passed = true; 
           StringBuffer sb = new StringBuffer(); 
           String  expectedLocalPort="Local_Port=8476";
           if (!JTOpenTestEnvironment.isOS400) {
             if (!pt.getSystem().getSystemName().equals(sys.getSystemName())) {
               passed = false;
               sb.append("pt.getSystem():" + pt.getSystem().getSystemName() + " != sys:" + sys.getSystemName() + "\n");
             }
             jobName="QUSER_NC.QZSOSIGN";
           } else { 
             expectedLocalPort="Local_Port=0";
             
             if (isNative_) {
              jobName=JDJobName.getJobName().replace('/','.');
             } else {
              jobName="QUSER_NC.QZRCSRVS";
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
           int expectedExpirationMaximum = 444; 
           int expectedExpirationMinimum = expectedExpirationMaximum - 30; 
           
           int timeToExpiration = pt.getTimeToExpiration();
           if ( expectedExpirationMinimum < expectedExpirationMinimum || timeToExpiration > expectedExpirationMaximum) {
             passed = false; 
             sb.append("pt.GetTimeExpiration() = "+timeToExpiration+" sb >= "+expectedExpirationMinimum+" and <= "+expectedExpirationMaximum);
           }
           
           String  expectedVerificationId="Verification_ID=QIBM_OS400_JT400"; 
           String  expectedRemotePort=null; /* don't check remote port */ 
           String expectedRemoteIp = null; /* don't check remote IP as it can vary and may be different than the local IP address because of network configuration */ 
           String expectedLocalIp = null; /* don't check local ip */ 
             

           if (!AuthExit.checkResult(pwrConnection_, jobName, mfaUserid_, sb, expectedVerificationId,
               expectedRemotePort, expectedLocalPort, expectedRemoteIp, expectedLocalIp)) {
             passed = false;
           }

           if (passed) {
             AuthExit.cleanup(pwrConnection_);
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
   public void Var042() {notApplicable("Future variation"); } 
   public void Var043() {notApplicable("Future variation"); } 
 public void Var044() {notApplicable("Future variation"); } 
 public void Var045() {notApplicable("Future variation"); } 
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
           notApplicable("Local test: Using *CURRENT");
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
       cmd.run("QSYS/CRTUSRPRF "+user+" PASSWORD(TBXSPTM783)");

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
           cmd.run("QSYS/DLTUSRPRF "+user+" ");
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
       cmd.run("QSYS/CRTUSRPRF "+user+" PASSWORD(TBXSPTM783)");
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
           cmd.run("QSYS/DLTUSRPRF "+user+" ");
         } catch (Exception e) {
           e.printStackTrace();
         }
       }
       try { sys.close(); } catch (Exception e) {} 
     }
   }
   }

   
   /**
   Test failed initialization (null token).
   **/
  public void Var057()
  {
      try
      {
          // Create and initialize the principal.
          UserProfilePrincipal upp = new UserProfilePrincipal();
          upp.initialize(SecAuthTest.uid1);
          // Create and initialize the credential.
          ProfileTokenCredential pt = new ProfileTokenCredential();
          try
          {
              // set null token
              pt.setToken(null); 
              assertCondition(false, "exception not thrown for")   ;
              
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
  Test failed initialization (null token).
  **/
 public void Var058()
 {
     try
     {
         // Create and initialize the principal.
         UserProfilePrincipal upp = new UserProfilePrincipal();
         upp.initialize(SecAuthTest.uid1);
         // Create and initialize the credential.
         ProfileTokenCredential pt = new ProfileTokenCredential();
         try
         {
             ProfileTokenEnhancedInfo enhancedProfileInfo = new ProfileTokenEnhancedInfo(); 
            // set null token
             pt.setToken(null, enhancedProfileInfo); 
             assertCondition(false, "exception not thrown for")   ;
             
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
 Test failed initialization (null token).
 **/
public void Var059()
{
    try
    {
        // Create and initialize the principal.
        UserProfilePrincipal upp = new UserProfilePrincipal();
        upp.initialize(SecAuthTest.uid1);
        // Create and initialize the credential.
        ProfileTokenCredential pt = new ProfileTokenCredential();
        try
        {
            ProfileTokenEnhancedInfo enhancedProfileInfo = new ProfileTokenEnhancedInfo(); 
           // set null token
            byte[] badToken = new byte[10]; 
            pt.setToken(badToken, enhancedProfileInfo); 
            assertCondition(false, "exception not thrown for")   ;
            
        }
        catch (Throwable e)
        {
          boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.LENGTH_NOT_VALID;
          if (!passed) e.printStackTrace();
          assertCondition( passed, "Unexpected exception during initialization.");
        }
    }
    catch (Throwable e)
    {
        failed(e, "Unexpected exception.");
    }
}


/**
Test failed initialization (null token).
**/
@SuppressWarnings("deprecation")
public void Var060()
{
   try
   {
       // Create and initialize the principal.
       UserProfilePrincipal upp = new UserProfilePrincipal();
       upp.initialize(SecAuthTest.uid1);
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
      
          // ProfileTokenEnhancedInfo enhancedProfileInfo = new ProfileTokenEnhancedInfo(); 
         
          pt.setSystem(pwrSys_);
          pt.setToken(upp,ProfileTokenCredential.PW_NOPWD ) ;
          
          ProfileTokenCredential pt2 = new ProfileTokenCredential();
          pt2.setSystem(pwrSys_);
          pt2.setTokenExtended(upp, SecAuthTest.pwd1.toCharArray()) ;
             
          ProfileTokenCredential pt3 = new ProfileTokenCredential();
          pt3.setSystem(pwrSys_);
          pt3.setTokenExtended(upp, SecAuthTest.pwd1) ;
          
          ProfileTokenCredential pt4 = new ProfileTokenCredential();
          pt4.setSystem(pwrSys_);
          pt4.setTokenExtended(SecAuthTest.uid1,  SecAuthTest.pwd1 ) ;
          
          ProfileTokenCredential pt5 = new ProfileTokenCredential();
          pt5.setSystem(pwrSys_);
          
          pt5.setLocalIPAddress("1.2.3.4");
          pt5.setRemotePort(33);
          pt5.setLocalPort(6323); 
          pt5.setAuthenticationIndicator(AuthenticationIndicator.APPLICATION_AUTHENTICATION);
          pt5.getAuthenticationIndicator(); 
          pt5.setTokenCreator(ProfileTokenCredential.CREATOR_NATIVE_API);
          pt5.setTokenExtended(SecAuthTest.uid1,  SecAuthTest.pwd1.toCharArray()) ;

          assertCondition(pt != null); 
      
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}


/**
Test failed setToken(null Userid ) 
**/
public void Var061()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           String nullString = null;
           pt.setToken(nullString, ProfileTokenCredential.PW_NOPWDCHK); 
           assertCondition(false, "exception not thrown for")   ;
           
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}

/**
Test failed setToken(userid too long ) 
**/
public void Var062()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setToken("BIG45678901", ProfileTokenCredential.PW_NOPWDCHK); 
           assertCondition(false, "exception not thrown for")   ;
           
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.LENGTH_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}


/**
Test failed setToken(big internal value ) 
**/
public void Var063()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setToken("BIG", 999); 
           assertCondition(false, "exception not thrown for")   ;
           
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}




/**
Test failed setTokenExtended (userid null ) 
**/
public void Var064()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setTokenExtended((String) null,"X".toCharArray(), null);  
           assertCondition(false, "exception not thrown for")   ;
           
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}



/**
Test failed setTokenExtended (userid too long ) 
**/
public void Var065()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setTokenExtended("BIG45678901","X".toCharArray(), null);  
           assertCondition(false, "exception not thrown for")   ;
           
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.LENGTH_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}


/**
Test failed setTokenExtended (password null  ) 
**/
public void Var066()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setTokenExtended("Userid",(char[]) null, null);  
           assertCondition(false, "exception not thrown for")   ;
           
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}



/**
Test failed setVerificationIdd(too big)  
**/
public void Var067()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setVerificationID("BIG4567890123456789012345678901");
           assertCondition(false, "exception not thrown for")   ;
           
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.LENGTH_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}


/**
Test failed setLocalIpAddress(too big)  
**/
public void Var068()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setLocalIPAddress("BIG45678911234567892123456789312345678941234567");
           assertCondition(false, "exception not thrown for pt.setLocalIPAddress(\"BIG45678911234567892123456789312345678941234567\")")   ;
                 }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.LENGTH_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}


/**
Test failed setRemoteIpAddress(too big)  
**/
public void Var069()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setRemoteIPAddress("BIG45678911234567892123456789312345678941234567");
           assertCondition(false,"exception not thrown for pt.setRemoteIPAddress(\"BIG45678911234567892123456789312345678941234567\");");
          
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.LENGTH_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}


/**
Test failed setRemotePort(too small)  
**/
public void Var070()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setRemotePort(-1); 
           assertCondition(false,"exception not thrown for pt.setRemotePort(-1))");

       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}


/**
Test failed setRemotePort(too big)  
**/
public void Var071()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setRemotePort(65536); 
           assertCondition(false,"exception not thrown for pt.setRemotePort(65536)");
   
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}



/**
Test failed setLocalPort(too small)  
**/
public void Var072()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setLocalPort(-1); 
           assertCondition(false,"exception not thrown for pt.setLocalPort(-1)");
           
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}


/**
Test failed setLocalPort(too big)  
**/
public void Var073()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setLocalPort(65536); 
           assertCondition(false,"exception not thrown for pt.setLocalPort(65536)");
           
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}


/**
Test failed setAuthenticationIndicator(too small ) 
**/
public void Var074()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setAuthenticationIndicator(0);
           assertCondition(false,"exception not thrown for pt.setAuthenticationIndicator(0)");
            
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}

/**
Test failed setAuthenticationIndicator(too big ) 
**/
public void Var075()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setAuthenticationIndicator(6);
           assertCondition(false,"exception not thrown for pt.setAuthenticationIndicator(6)");
           
           
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}


/**
Test failed setTokenCreator(too small ) 
**/
public void Var076()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setTokenCreator(-1);
           assertCondition(false,"exception not thrown for pt.setTokenCreator(0)");
           
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.RANGE_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}

/**
Test failed setTokenCreator(too big ) 
**/
public void Var077()
{
   try
   {
       // Create and initialize the principal.
       // Create and initialize the credential.
       ProfileTokenCredential pt = new ProfileTokenCredential();
       pt.setSystem(systemObject_);
       try
       {
           
           pt.setTokenCreator(3);
           assertCondition(false,"exception not thrown for pt.setTokenCreator(3)");
                     
       }
       catch (Throwable e)
       {
         boolean passed = e instanceof ExtendedIllegalArgumentException && ((ExtendedIllegalArgumentException)e).getReturnCode() == ExtendedIllegalArgumentException.RANGE_NOT_VALID;
         if (!passed) e.printStackTrace();
         assertCondition( passed, "Unexpected exception during initialization.");
       }
   }
   catch (Throwable e)
   {
       failed(e, "Unexpected exception.");
   }
}










}
