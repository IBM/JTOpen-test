///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecVrmTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sec;

import com.ibm.as400.access.AS400;

import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;

/**
 * Testcase SecVrmTestcase.
 **/
public class SecVrmTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SecVrmTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SecTest.main(newArgs); 
   }

  /* no longer testing to V7R1 */
  /* no longer testing to V7R2 */
  static String V7R3_SYSTEM_NAME = "V7R3_SYSTEM_NAME_NOT_SET";
  static String V7R4_SYSTEM_NAME = "V7R4_SYSTEM_NAME_NOT_SET";
  static String V7R5_SYSTEM_NAME = "V7R5_SYSTEM_NAME_NOT_SET";
  static String VNEXT_SYSTEM_NAME = "VNEXT_SYSTEM_NAME_NOT_SET";

  static {
    try {
      String property;

      property = JTOpenTestEnvironment.systemsProperties.getProperty("V7R3_SYSTEM_NAME");
      if (property != null) {
        V7R3_SYSTEM_NAME = property;
      } else {
        System.out.println("Warning: V7R3_SYSTEM_NAME not found in ini/systems.ini");
      }
      property = JTOpenTestEnvironment.systemsProperties.getProperty("V7R4_SYSTEM_NAME");
      if (property != null) {
        V7R4_SYSTEM_NAME = property;
      } else {
        System.out.println("Warning: V7R3_SYSTEM_NAME not found in ini/systems.ini");
      }

      property = JTOpenTestEnvironment.systemsProperties.getProperty("V7R5_SYSTEM_NAME");
      if (property != null) {
        V7R5_SYSTEM_NAME = property;
      } else {
        System.out.println("Warning: V7R5_SYSTEM_NAME not found in ini/systems.ini");
      }

      property = JTOpenTestEnvironment.systemsProperties.getProperty("VNEXT_SYSTEM_NAME");
      if (property != null) {
        VNEXT_SYSTEM_NAME = property;
      } else {
        System.out.println("Warning: VNEXT_SYSTEM_NAME not found in ini/systems.ini");
      }

    } catch (Exception e) {
      System.out.println("WARNING:  Exception during SecVrmTestcase static init");
      e.printStackTrace();
    }
    System.out.println("SecVrmTestcase read system names from ini/systems.ini " + V7R3_SYSTEM_NAME + ","
        + V7R4_SYSTEM_NAME + "," + V7R5_SYSTEM_NAME);

  }

  /**
   * Signon to a V5R2 system and verify the version.
   **/
  public void Var001() {
    notApplicable("V5R2 no longer supported.");
  }

  /**
   * Signon to a V5R2 system and verify the release.
   **/
  public void Var002() {
    notApplicable("V5R2 no longer supported.");
  }

  /**
   * Signon to a V5R2 system and verify the version.
   **/
  public void Var003() {
    notApplicable("V5R2 no longer supported.");
  }

  /**
   * Signon to a V5R3 system and verify the release.
   **/
  public void Var004() {
    notApplicable("V5R3 no longer supported.");

  }

  /**
   * Signon to a V5R3 system and verify the version.
   **/
  public void Var005() {
    notApplicable("V5R3 no longer supported.");
  }

  /**
   * Signon to a V5R3 system and verify the release.
   **/
  public void Var006() {
    notApplicable("V5R3 no longer supported.");

  }

  /**
   * Signon to a V5R3 system and verify the version.
   **/
  public void Var007() {
    notApplicable("V5R3 no longer supported.");
  }

  /**
   * Signon to a V5R3 system and verify the release.
   **/
  public void Var008() {
    notApplicable("V5R3 no longer supported.");
  }

  /**
   * Signon to a system and verify the version.
   **/
  public void Var009() {
    try {
      output_.println("  Var009: connecting to " + VNEXT_SYSTEM_NAME);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(VNEXT_SYSTEM_NAME, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);
      system.setGuiAvailable(false);
      system.connectService(AS400.FILE);
      try {
        assertCondition(system.getVersion() == 7, "Incorrect version - test runs to " + VNEXT_SYSTEM_NAME);
      } finally {
        system.disconnectService(AS400.FILE);
        system.close(); 
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception - test runs to " + VNEXT_SYSTEM_NAME);
    }
  }

  /**
   * Signon to a V7R2 system and verify the release.
   **/
  public void Var010() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(VNEXT_SYSTEM_NAME, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);
      system.setGuiAvailable(false);
      system.connectService(AS400.PRINT);
     try {
        int release = system.getRelease();
        assertCondition(release == 6, "Incorrect release =" + release + " - test runs to " + VNEXT_SYSTEM_NAME);
      } finally {
        system.disconnectService(AS400.PRINT);
        system.close(); 
        
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception - test runs to " + VNEXT_SYSTEM_NAME);
    }
  }

  /**
   * Signon to a V7R3 system and verify the version.
   **/
  public void Var011() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(V7R3_SYSTEM_NAME, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);
      system.setGuiAvailable(false);
      system.connectService(AS400.FILE);
      try {
        assertCondition(system.getVersion() == 7, "Incorrect version - test runs to " + V7R3_SYSTEM_NAME);
      } finally {
        system.disconnectService(AS400.FILE);
        system.close(); 
        
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception - test runs to " + V7R3_SYSTEM_NAME + " using " + userId_);
    }
  }

  /**
   * Signon to a V7R3 system and verify the release.
   **/
  public void Var012() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(V7R3_SYSTEM_NAME, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);
      system.setGuiAvailable(false);
      system.connectService(AS400.PRINT);
      try {
        assertCondition(system.getRelease() == 3, "Incorrect release - test runs to " + V7R3_SYSTEM_NAME);
      } finally {
        system.disconnectService(AS400.PRINT);
        system.close(); 
        
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception - test runs to " + V7R3_SYSTEM_NAME);
    }
  }

  /**
   * Retrieve the version without signing on. Should return version 6.
   **/
  public void Var013() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(V7R3_SYSTEM_NAME, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setGuiAvailable(false);
      system.setMustUseSockets(mustUseSockets_);
      assertCondition(system.getVersion() == 7, "Incorrect version - test runs to " + V7R3_SYSTEM_NAME);
      system.close(); 
      
    } catch (Exception e) {
      failed(e, "Unexpected exception - test runs to " + V7R3_SYSTEM_NAME);
    }
  }

  /**
   * Retrieve the release without signing on. Should return 1.
   **/
  public void Var014() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(V7R3_SYSTEM_NAME, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setGuiAvailable(false);
      system.setMustUseSockets(mustUseSockets_);
      assertCondition(system.getRelease() == 3, "Incorrect release - test runs to " + V7R3_SYSTEM_NAME);
      system.close(); 
      
    } catch (Exception e) {
      failed(e, "Unexpected exception - test runs to " + V7R3_SYSTEM_NAME);
    }
  }

  /**
   * Retrieve the modification level without signing on. Should return 0.
   **/
  public void Var015() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(V7R3_SYSTEM_NAME, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setGuiAvailable(false);
      system.setMustUseSockets(mustUseSockets_);
      assertCondition(system.getModification() == 0, "Incorrect modification - test runs to " + V7R3_SYSTEM_NAME);
      system.close(); 
      
    } catch (Exception e) {
      failed(e, "Unexpected exception - test runs to " + V7R3_SYSTEM_NAME);
    }
  }

  /**
   * Retrieve the vrm without signing on. Should return 0x00060100.
   **/
  public void Var016() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(V7R3_SYSTEM_NAME, userId_, charPassword);
      system.setGuiAvailable(false);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);
      assertCondition(system.getVRM() == 0x00070300, "VRM not correct - test runs to " + V7R3_SYSTEM_NAME);
      system.close(); 
      
    } catch (Exception e) {
      failed(e, "Unexpected exception - test runs to " + V7R3_SYSTEM_NAME);
    }
  }

  /**
   * Signon to a V7R4 system and verify the version.
   **/
  public void Var017() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(V7R4_SYSTEM_NAME, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);
      system.setMustUseSockets(mustUseSockets_);
      system.setGuiAvailable(false);
      system.connectService(AS400.FILE);
      try {
        assertCondition(system.getVersion() == 7, "Incorrect version - test runs to " + V7R4_SYSTEM_NAME);
      } finally {
        system.disconnectService(AS400.FILE);
        system.close(); 
        
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception - test runs to " + V7R4_SYSTEM_NAME + " using " + userId_);
    }
  }

  /**
   * Signon to a V7R4 system and verify the release.
   **/
  public void Var018() {
    if (checkNotGroupTest())
      try {
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 system = new AS400(V7R4_SYSTEM_NAME, userId_, charPassword);
        PasswordVault.clearPassword(charPassword);
        system.setMustUseSockets(mustUseSockets_);
        system.setGuiAvailable(false);
        system.connectService(AS400.PRINT);
        try {
          assertCondition(system.getRelease() == 4, "Incorrect release - test runs to " + V7R4_SYSTEM_NAME);
        } finally {
          system.disconnectService(AS400.PRINT);
          system.close(); 
          
        }
      } catch (Exception e) {
        failed(e, "Unexpected exception - test runs to " + V7R4_SYSTEM_NAME);
      }
  }

  /**
   * Retrieve the version without signing on. Should return version 6.
   **/
  public void Var019() {
    if (checkNotGroupTest())
      try {
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 system = new AS400(V7R4_SYSTEM_NAME, userId_, charPassword);
        PasswordVault.clearPassword(charPassword);
        system.setMustUseSockets(mustUseSockets_);
        system.setGuiAvailable(false);
        assertCondition(system.getVersion() == 7, "Incorrect version - test runs to " + V7R4_SYSTEM_NAME);
        system.close(); 
        
      } catch (Exception e) {
        failed(e, "Unexpected exception - test runs to " + V7R4_SYSTEM_NAME);
      }
  }

  /**
   * Retrieve the release without signing on. Should return 1.
   **/
  public void Var020() {
    if (checkNotGroupTest())
      try {
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 system = new AS400(V7R4_SYSTEM_NAME, userId_, charPassword);
        PasswordVault.clearPassword(charPassword);
        system.setMustUseSockets(mustUseSockets_);
        system.setGuiAvailable(false);
        assertCondition(system.getRelease() == 4, "Incorrect release - test runs to " + V7R4_SYSTEM_NAME);
        system.close(); 
        
      } catch (Exception e) {
        failed(e, "Unexpected exception - test runs to " + V7R4_SYSTEM_NAME);
      }
  }

  /**
   * Retrieve the modification level without signing on. Should return 0.
   **/
  public void Var021() {
    if (checkNotGroupTest())
      try {
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 system = new AS400(V7R4_SYSTEM_NAME, userId_, charPassword);
        PasswordVault.clearPassword(charPassword);
        system.setMustUseSockets(mustUseSockets_);
        system.setGuiAvailable(false);
        assertCondition(system.getModification() == 0, "Incorrect modification - test runs to " + V7R4_SYSTEM_NAME);
        system.close(); 
        
        
      } catch (Exception e) {
        failed(e, "Unexpected exception - test runs to " + V7R4_SYSTEM_NAME);
      }
  }

  /**
   * Retrieve the vrm without signing on. Should return 0x00060100.
   **/
  public void Var022() {
    if (checkNotGroupTest())
      try {
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 system = new AS400(V7R4_SYSTEM_NAME, userId_, charPassword);
        PasswordVault.clearPassword(charPassword);
        system.setMustUseSockets(mustUseSockets_);
        system.setGuiAvailable(false);
        assertCondition(system.getVRM() == 0x00070400,
            "VRM " + Integer.toHexString(system.getVRM()) + " not correct - test runs to " + V7R4_SYSTEM_NAME);
        system.close(); 
        
      } catch (Exception e) {
        failed(e, "Unexpected exception - test runs to " + V7R4_SYSTEM_NAME);
      }
  }

  /**
   * Generate a VRM and verify it was generated correctly.
   **/
  public void Var023() {
    try {
      int vrm = AS400.generateVRM(4, 5, 6);
      assertCondition(vrm == 0x00040506, "Incorrect VRM generated.");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }
}
