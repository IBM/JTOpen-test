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

package test.Sec;

import java.util.GregorianCalendar;
import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;

/**
 * Testcase SecSignonInfoTestcase.
 **/
public class SecSignonInfoTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "SecSignonInfoTestcase";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.SecTest.main(newArgs);
  }

  /**
   * Get password expiration date without signing on. Should return date.
   **/
  public void Var001() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      GregorianCalendar date = sys.getPasswordExpirationDate();
      sys.close();
      assertCondition(date != null, "Date is null");
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Get the previous signon date without signing on. Should return date.
   **/
  public void Var002() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      GregorianCalendar date = sys.getPreviousSignonDate();
      sys.close();
      assertCondition(date != null, "Date is null");
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Get the signon date without signing on. Should return date.
   **/
  public void Var003() {
    AS400 sys = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      GregorianCalendar date = sys.getSignonDate();
      assertCondition(date != null, "Date is null");
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    } finally {
      if (sys != null) {
        try {
          sys.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Get the password expiration date after signing on.
   **/
  public void Var004() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      sys.connectService(AS400.COMMAND);
      try {
        GregorianCalendar date = sys.getPasswordExpirationDate();

        if (date != null) {
          GregorianCalendar now = new GregorianCalendar();
          assertCondition(date.after(now), "Invalid password expiration date");
        } else {
          failed("Password expiration date is null");
        }
      } finally {
        sys.disconnectService(AS400.COMMAND);
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Verify the previous signon date. Signon, save the date, and signon again.
   **/
  public void Var005() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      GregorianCalendar lastDate = sys.getSignonDate();

      AS400 newsys = new AS400(sys);
      GregorianCalendar prevDate = newsys.getPreviousSignonDate();
      newsys.close();
      assertCondition(prevDate.equals(lastDate), "Previous signon date not the same");
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Get the current signon date.
   **/
  public void Var006() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      sys.connectService(AS400.DATAQUEUE);
      try {
        GregorianCalendar date = sys.getSignonDate();
        assertCondition(date != null, "Did not get a current signon date");
      } finally {
        sys.disconnectService(AS400.DATAQUEUE);
        sys.close();
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }
  

  /**
   * Set the password expiration warning date to a large number. We should get the
   * password expiration warning.
   **/
  public void Var007() {
    if (runMode_ != UNATTENDED) {
      try {
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 sys = new AS400(systemName_, userId_, charPassword);
        PasswordVault.clearPassword(charPassword);
        sys.setMustUseSockets(mustUseSockets_);
        if (!JTOpenTestEnvironment.isOS400) {
          output_.println("You should see a password expiration warning.  Press No.");
        }

        int oldVal = AS400.getPasswordExpirationWarningDays();
        try {
          AS400.setPasswordExpirationWarningDays(500);
          sys.connectService(AS400.COMMAND);
          sys.disconnectService(AS400.COMMAND);

          succeeded();
        } finally {
          AS400.setPasswordExpirationWarningDays(oldVal);
          sys.close();
        }
      } catch (Exception e) {
        failed(e, "Unexpected exception");
      }
    } else {
      notApplicable("Attended testcase");
    }
  }

  /**
   * Verify that get password expiration warning works properly.
   **/
  public void Var008() {
    try {
      int oldVal = AS400.getPasswordExpirationWarningDays();
      try {
        AS400.setPasswordExpirationWarningDays(5);
        assertCondition(AS400.getPasswordExpirationWarningDays() == 5, "password expiration days is incorrect");
      } finally {
        AS400.setPasswordExpirationWarningDays(oldVal);
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  // TODO - Start
  /**
   * Test getSystemPasswordExpirationWarning Use system value QPWDEXPWRN to return
   * expire warning days.
   **/
  /**
   * 
   */
  public void Var009() {
    if (getSystemVRM() <= VRM_V5R4M0) {
      notApplicable(
          "This capability is supported with V6R1M0 and later systems with V6R1M0 5761SS1 PTF SI48808 or V7R1M0 5770SS1 PTF SI48809.");
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
      // System.out.println(".. globalExpire="+globalExpire+" expire="+expire);
      // Values seen for working case are -1 and 7
      if (isNative_) {
        // For the native case, they will be the same, since the value is not cached.
        assertCondition(globalExpire == expire,
            "password expiration days is incorrect (should be same for nativet) globalExpire=" + globalExpire
                + " expire=" + expire);

      } else {
        assertCondition(globalExpire != expire,
            "password expiration days is incorrect (should be different) globalExpire=" + globalExpire + " expire="
                + expire);
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
    try {
      sys.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Use system value QPWDEXPWRN to return expire warning days.
   **/
  public void Var010() {
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
    try {
      sys.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Test isAdditionalAuthenticationFactorAccepted
   **/
  public void Var011() {

    try {
      boolean expectedValue = false;
      if (getRelease() > JDTestDriver.RELEASE_V7R5M0)
        expectedValue = true;

      boolean accepted = JDReflectionUtil.callStaticMethod_B("com.ibm.as400.access.AS400",
          "isAdditionalAuthenticationFactorAccepted", systemName_);
      assertCondition(accepted == expectedValue,
          "AS400.isAdditionalAuthenticationFactorAccepted=" + accepted + " sb " + expectedValue);
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

}
