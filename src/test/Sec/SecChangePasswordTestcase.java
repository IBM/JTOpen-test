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

package test.Sec;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;

import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;

/**
 * Testcase SecChangePasswordTestcase.
 **/
public class SecChangePasswordTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "SecChangePasswordTestcase";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.SecTest.main(newArgs);
  }

  /**
   * Create a default (no system name, no user id) AS400 object, and try to change
   * password. An InvalidObjectStateException should be generated.
   **/
  public void Var001() {
    try {
      AS400 sys = new AS400();
      sys.setMustUseSockets(mustUseSockets_);
      try {
        sys.changePassword("oldpasswor".toCharArray(), "newpasswor".toCharArray());
        sys.close();
        failed("exception not generated");
      } catch (Exception e) {
        if (onAS400_ /* && isNative_ */ && !JTOpenTestEnvironment.isOS400open) {
          sys.connectService(AS400.COMMAND);
          sys.disconnectAllServices();
          sys.close();
          assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
        } else {
          sys.close();
          assertExceptionIs(e, "ExtendedIllegalStateException");
        }
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Create an AS400 object with only the system name, and try to change password.
   * An InvalidObjectStateException should be generated.
   **/
  public void Var002() {
    try {
      AS400 sys = new AS400(systemName_);
      sys.setMustUseSockets(mustUseSockets_);
      try {
        sys.changePassword("oldpasswor".toCharArray(), "newpasswor".toCharArray());
        sys.close();
        failed("exception not generated");
      } catch (Exception e) {
        if (onAS400_ /* && isNative_ */ && !JTOpenTestEnvironment.isOS400open) {
          sys.connectService(AS400.COMMAND);
          sys.disconnectAllServices();
          sys.close();
          assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
        } else {
          sys.close();
          assertExceptionIs(e, "ExtendedIllegalStateException");
        }
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try using null for old password. A NullPointerException should be generated.
   **/
  public void Var003() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      try {
        sys.changePassword(null, "newpassword".toCharArray());
        sys.close();
        failed("exception not generated");
      } catch (Exception e) {
        sys.close();
        assertExceptionIs(e, "NullPointerException");
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Create a AS400 object with system name and user id, and try using null for
   * new password. A NullPointerException should be generated.
   **/
  public void Var004() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 sys = new AS400(systemName_, userId_, charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      try {
        sys.changePassword(charPassword, null);
        sys.close();
        failed("exception not generated");
      } catch (Exception e) {
        sys.close();
        assertExceptionIs(e, "NullPointerException");
      }
      PasswordVault.clearPassword(charPassword);
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }
}
