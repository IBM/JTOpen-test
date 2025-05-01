///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SSLValidatePasswordTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.SSL;

import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.SecureAS400;

import test.PasswordVault;
import test.Testcase;

/**
 * Testcase SSLValidatePasswordTestcase.
 **/
public class SSLValidatePasswordTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "SSLValidatePasswordTestcase";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.SSLTest.main(newArgs);
  }

  /**
   * Create a default SecureAS400 object and try to validate the user without
   * having the system set. An InvalidObjectStateException should be thrown.
   * (validateSignon(String, String))
   **/
  public void Var001() {
    try {
      SecureAS400 sys = new SecureAS400();
      sys.setMustUseSockets(mustUseSockets_);

      try {
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        sys.validateSignon(userId_, charPassword);
        PasswordVault.clearPassword(charPassword);
        if (onAS400_) {
          succeeded();
        } else {
          failed("exception not thrown");
        }
      } catch (Exception e) {
        if (onAS400_) {
          failed(e, "Unexpected exception");
        } else {
          assertExceptionIs(e, "ExtendedIllegalStateException");
        }
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Create a default SecureAS400 and try to validate with null for user ID. A
   * NullPointerException should be thrown. (validateSignon(String, String))
   **/
  public void Var002() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_);
      sys.setMustUseSockets(mustUseSockets_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

      try {
        sys.validateSignon(null, charPassword);
        failed("exception not thrown");
      } catch (Exception e) {
        assertExceptionIs(e, "NullPointerException");
      }
      sys.close(); 
      PasswordVault.clearPassword(charPassword);
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Create a default SecureAS400 and try to validate with a null for password. A
   * NullPointerException should be thrown. (validateSignon(String, String))
   **/
  public void Var003() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_);
      sys.setMustUseSockets(mustUseSockets_);

      try {
        sys.validateSignon(userId_, (char[]) null);
        failed("exception not thrown");
      } catch (Exception e) {
        assertExceptionIs(e, "NullPointerException");
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try to validate an invalid user ID. An AS400SecurityException should be
   * thrown, with a return code of USERID_UNKNOWN. (validateSignon(String,
   * String))
   **/
  public void Var004() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_);
      sys.setMustUseSockets(mustUseSockets_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

      try {
        sys.validateSignon("zuserx", charPassword);
        failed("exception not thrown");
      } catch (Exception e) {
        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.USERID_UNKNOWN);
      }
      sys.close(); 
      PasswordVault.clearPassword(charPassword);
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try to validate an invalid password. An AS400SecurityException should be
   * thrown, with a return code of INCORRECT_PASSWORD. (validateSignon(String,
   * String))
   **/
  public void Var005() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_);
      sys.setMustUseSockets(mustUseSockets_);

      try {
        sys.validateSignon(userId_, "abcd".toCharArray());
        failed("exception not thrown");
      } catch (Exception e) {
        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Validate by passing in correct user ID and password. This should validate
   * with success. (validateSignon(String, String))
   **/
  public void Var006() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      sys.validateSignon(userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.close(); 
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Create a default SecureAS400 object and try to validate the user without
   * having the system set. An InvalidObjectStateException should be thrown.
   * (validateSignon(String))
   **/
  public void Var007() {
    try {
      SecureAS400 sys = new SecureAS400();
      sys.setMustUseSockets(mustUseSockets_);

      try {
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        sys.validateSignon(charPassword);
        PasswordVault.clearPassword(charPassword);
        if (onAS400_ && isNative_) {
          succeeded();
        } else {
          failed("exception not thrown");
        }
      } catch (Exception e) {
        if (onAS400_ && isNative_) {
          failed(e, "Unexpected exception");
        } else {
          assertExceptionIs(e, "ExtendedIllegalStateException");
        }
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Create a default SecureAS400 and try to validate with null for user ID. An
   * InvalidObjectStateException should be thrown. (validateSignon(String))
   **/
  public void Var008() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_);
      sys.setMustUseSockets(mustUseSockets_);

      try {
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        sys.validateSignon(charPassword);
        PasswordVault.clearPassword(charPassword);
        if (onAS400_ && isNative_) {
          succeeded();
        } else {
          failed("exception not thrown");
        }
      } catch (Exception e) {
        if (onAS400_ && isNative_) {
          failed(e, "Unexpected exception");
        } else {
          assertExceptionIs(e, "ExtendedIllegalStateException");
        }
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Create a default SecureAS400 and try to validate with a null for password. A
   * NullPointerException should be thrown. (validateSignon(String))
   **/
  public void Var009() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_, userId_);
      sys.setMustUseSockets(mustUseSockets_);

      try {
        sys.validateSignon((char[]) null);
        failed("exception not thrown");
      } catch (Exception e) {
        assertExceptionIs(e, "NullPointerException");
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try to validate an invalid user ID. An AS400SecurityException should be
   * thrown, with a return code of USERID_UNKNOWN. (validateSignon(String))
   **/
  public void Var010() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_, "zuserx");
      sys.setMustUseSockets(mustUseSockets_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

      try {
        sys.validateSignon(charPassword);
        failed("exception not thrown");
      } catch (Exception e) {
        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.USERID_UNKNOWN);
      }
      PasswordVault.clearPassword(charPassword);
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try to validate an invalid password. An AS400SecurityException should be
   * thrown, with a return code of PASSWORD_INCORRECT. (validateSignon(String))
   **/
  public void Var011() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_, userId_);
      sys.setMustUseSockets(mustUseSockets_);

      try {
        sys.validateSignon("abcd".toCharArray());
        failed("exception not thrown");
      } catch (Exception e) {
        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Validate by passing in correct user ID and password. This should validate
   * with success. (validateSignon(String))
   **/
  public void Var012() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_, userId_);
      sys.setMustUseSockets(mustUseSockets_);

      try {
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        sys.validateSignon(charPassword);
        PasswordVault.clearPassword(charPassword);
        succeeded();
      } catch (Exception e) {
        failed(e, "Unexpected exception");
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Create a default SecureAS400 object and try to validate the user without
   * having the system set. An InvalidObjectStateException should be thrown.
   * (validateSignon())
   **/
  public void Var013() {
    try {
      SecureAS400 sys = new SecureAS400();
      sys.setMustUseSockets(mustUseSockets_);

      try {
        sys.validateSignon();
        if (onAS400_ && isNative_) {
          succeeded();
        } else {
          failed("exception not thrown");
        }
      } catch (Exception e) {
        if (onAS400_ && isNative_) {
          failed(e, "Unexpected exception");
        } else {
          assertExceptionIs(e, "ExtendedIllegalStateException");
        }
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Create a default SecureAS400 and try to validate with null for user ID. A
   * NullPointerException should be thrown. (validateSignon())
   **/
  public void Var014() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_);
      sys.setMustUseSockets(mustUseSockets_);

      try {
        sys.validateSignon();
        if (onAS400_ && isNative_) {
          succeeded();
        } else {
          failed("exception not thrown");
        }
      } catch (Exception e) {
        if (onAS400_ && isNative_) {
          failed(e, "Unexpected exception");
        } else {
          assertExceptionIs(e, "ExtendedIllegalStateException");
        }
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Create a default SecureAS400 and try to validate with a null for password. A
   * NullPointerException should be thrown. (validateSignon())
   **/
  public void Var015() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_, userId_);
      sys.setMustUseSockets(mustUseSockets_);

      try {
        sys.validateSignon();
        if (onAS400_ && isNative_) {
          succeeded();
        } else {
          failed("exception not thrown");
        }
      } catch (Exception e) {
        if (onAS400_ && isNative_) {
          failed(e, "Unexpected exception");
        } else {
          assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_NOT_SET);
        }
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try to validate an invalid user ID. An AS400SecurityException should be
   * thrown, with a return code of UNKNWON_USER_ID. (validateSignon())
   **/
  public void Var016() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      SecureAS400 sys = new SecureAS400(systemName_, "zuserx", charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);

      try {
        sys.validateSignon();
        failed("exception not thrown");
      } catch (Exception e) {
        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.USERID_UNKNOWN);
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Try to validate an invalid password. An AS400SecurityException should be
   * thrown, with a return code of INCORRECT_PASSWORD. (validateSignon())
   **/
  public void Var017() {
    try {
      SecureAS400 sys = new SecureAS400(systemName_, userId_, "abcd".toCharArray());
      sys.setMustUseSockets(mustUseSockets_);

      try {
        sys.validateSignon();
        failed("exception not thrown");
      } catch (Exception e) {
        assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);
      }
      sys.close(); 
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * Validate by passing in correct user ID and password. This should validate
   * with success. (validateSignon())
   **/
  public void Var018() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      sys.setMustUseSockets(mustUseSockets_);
      sys.validateSignon();
      sys.close(); 
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }
}
