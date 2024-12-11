///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecCtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2024 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sec;

import java.beans.PropertyVetoException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;
import java.util.Random;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;
import com.ibm.as400.security.auth.DefaultProfileTokenProvider;
import com.ibm.as400.security.auth.ProfileTokenCredential;

import test.JDReflectionUtil;
import test.PasswordVault;
import test.Testcase;

/**
 * Testcase SecCtorTestcase.
 * <p>
 * Test variations for the methods:
 * <ul>
 * <li>AS400::AS400()
 * <li>AS400::AS400(String)
 * <li>AS400::AS400(String, String)
 * <li>AS400::AS400(String, String, String)
 * <li>AS400::AS400(String, String, String, String)
 * <li>AS400::AS400(String, ProfileTokenCredential)
 * <li>AS400::AS400(AS400)
 * </ul>
 **/
public class SecCtorTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "SecCtorTestcase";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.SecTest.main(newArgs);
  }

  // Retrieve and verify correct state.
  private void assertValidState(AS400 system, String failMessage) {
    // Retrieve state.
    Locale locale = system.getLocale();
    boolean connected = system.isConnected();
    boolean mustUseSockets = system.isMustUseSockets();
    boolean showCheckboxes = system.isShowCheckboxes();
    boolean threadUsed = system.isThreadUsed();
    boolean useDefaultUser = system.isUseDefaultUser();
    boolean usePasswordCache = system.isUsePasswordCache();

    // Verify state.
    if (!locale.equals(Locale.getDefault()))
      failMessage += "Incorrect Locale: '" + locale + "'\n";
    if (connected)
      failMessage += "Connected is true.\n";
    if (mustUseSockets)
      failMessage += "Must use sockets is true.\n";
    if (!showCheckboxes)
      failMessage += "Show checkboxes is false.\n";
    if (!threadUsed)
      failMessage += "Thread used is false.\n";
    if (!useDefaultUser)
      failMessage += "Use default user is false.\n";
    if (!usePasswordCache)
      failMessage += "Use password cache is false.\n";

    assertCondition(failMessage.equals(""), "\n" + failMessage);
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400().</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var001() {
    try {
      AS400 system = new AS400();

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (onAS400_) {
        if (!systemName.equals("localhost"))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (!systemName.equals(""))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (local)
          failMessage += "Local is true.\n";
      }
      if (onAS400_) {
        String expectedUser = System.getProperty("user.name").toUpperCase();
        if (!userId.equals(expectedUser))
          failMessage += "Incorrect userId: '" + userId + "' sb '" + expectedUser + "'\n";
      } else {
        if (!userId.equals(""))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      }
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string) with a literal system name.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var002() {
    try {
      AS400 system = new AS400("testSystem");

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("testSystem"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (onAS400_) {
        String expectedUser = System.getProperty("user.name").toUpperCase();
        if (!userId.equals(expectedUser))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      } else {
        if (!userId.equals(""))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      }
      if (local)
        failMessage += "Local is true.\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string) with a literal system name and a
   * literal user ID.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var003() {
    try {
      AS400 system = new AS400("testSystem", "testUid");

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("testSystem"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (!userId.equals("TESTUID"))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (local)
        failMessage += "Local is true.\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string, string) with a literal system name, a
   * literal user ID, and a literal password.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var004() {
    try {
      AS400 system = new AS400("testSystem", "testUid", "testPwd".toCharArray());

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("testSystem"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (!userId.equals("TESTUID"))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (local)
        failMessage += "Local is true.\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
      system.close();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string) with the test driver's system name.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var005() {
    try {
      AS400 system = new AS400(systemName_);

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (isLocal_ || local) {
        if (!systemName.equals("localhost"))
          failMessage += "Incorrect systemName '" + systemName + "' systemName_=" + systemName_ + "\n";
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (!systemName.equals(systemName_))
          failMessage += "Incorrect systemName '" + systemName + "' is not '" + systemName_ + "' systemName_="
              + systemName_ + "\n";
        if (local)
          failMessage += "Local is true.\n";
      }
      if (onAS400_) {
        String expectedUser = System.getProperty("user.name").toUpperCase();
        if (!userId.equals(expectedUser))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      } else {
        if (!userId.equals(""))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      }
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string) with the test driver's system name and
   * the test driver's user ID.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var006() {
    try {
      AS400 system = new AS400(systemName_, userId_);

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (isLocal_ || local) {
        if (!systemName.equals("localhost"))
          failMessage += "Incorrect systemName '" + systemName + "' systemName_=" + systemName_ + "\n";
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (!systemName.equals(systemName_))
          failMessage += "Incorrect systemName '" + systemName + "' systemName_=" + systemName_ + "\n";
        if (local)
          failMessage += "Local is true.\n";
      }
      if (!userId.equals(userId_.toUpperCase()))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string, string) with the test driver's system
   * name, the test driver's user ID, and the test driver's password.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var007() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 system = new AS400(systemName_, userId_, charPassword);
      PasswordVault.clearPassword(charPassword);

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (isLocal_ || local) {
        if (!systemName.equals("localhost"))
          failMessage += "Incorrect systemName '" + systemName + "'  systemName_=" + systemName_ + "\n";
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (!systemName.equals(systemName_))
          failMessage += "Incorrect systemName '" + systemName + "'  systemName_=" + systemName_ + "\n";
        if (local)
          failMessage += "Local is true.\n";
      }
      if (!userId.equals(userId_.toUpperCase()))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string) with the local system's system name.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var008() {
    try {
      String testSystemName = InetAddress.getLocalHost().getHostName();
      AS400 system = new AS400(testSystemName);

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (onAS400_) {
        if (!systemName.equals("localhost"))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (!systemName.equals(testSystemName))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (local)
          failMessage += "Local is true.\n";
      }
      if (onAS400_) {
        String expectedUser = System.getProperty("user.name").toUpperCase();
        if (!userId.equals(expectedUser))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      } else {
        if (!userId.equals(""))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      }
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string) with the local system's system name and
   * a literal user ID.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var009() {
    try {
      String testSystemName = InetAddress.getLocalHost().getHostName();
      AS400 system = new AS400(testSystemName, "testUid");

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (onAS400_) {
        if (!systemName.equals("localhost"))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (!systemName.equals(testSystemName))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (local)
          failMessage += "Local is true.\n";
      }
      if (!userId.equals("TESTUID"))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string, string) with the local system's system
   * name, a literal user ID, and a literal password.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var010() {
    try {
      String testSystemName = InetAddress.getLocalHost().getHostName();
      AS400 system = new AS400(testSystemName, "testUid", "testPwd".toCharArray());

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (onAS400_) {
        if (!systemName.equals("localhost"))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (!systemName.equals(testSystemName))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (local)
          failMessage += "Local is true.\n";
      }
      if (!userId.equals("TESTUID"))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string) with a system name of "localhost".</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var011() {
    try {
      AS400 system = new AS400("localhost");

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("localhost"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (onAS400_) {
        String expectedUser = System.getProperty("user.name").toUpperCase();
        if (!userId.equals(expectedUser))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      } else {
        if (!userId.equals(""))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      }
      if (onAS400_) {
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (local)
          failMessage += "Local is true.\n";
      }
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string) with a system name of "localhost" and a
   * literal user ID.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var012() {
    try {
      AS400 system = new AS400("localhost", "testUid");

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("localhost"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (!userId.equals("TESTUID"))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (onAS400_) {
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (local)
          failMessage += "Local is true.\n";
      }
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string, string) with a system name of
   * "localhost", a literal user ID and a literal password.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var013() {
    try {
      AS400 system = new AS400("localhost", "testUid", "testPwd".toCharArray());

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("localhost"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (!userId.equals("TESTUID"))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (onAS400_) {
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (local)
          failMessage += "Local is true.\n";
      }
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string) with a system name of the empty string
   * ("").</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var014() {
    try {
      AS400 system = new AS400("");

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (onAS400_) {
        if (!systemName.equals("localhost"))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (!systemName.equals(""))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (local)
          failMessage += "Local is true.\n";
      }
      if (onAS400_) {
        String expectedUser = System.getProperty("user.name").toUpperCase();
        if (!userId.equals(expectedUser))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      } else {
        if (!userId.equals(""))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      }
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string) with a system name of the empty string
   * ("") and a literal user ID.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var015() {
    try {
      AS400 system = new AS400("", "testUid");

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (onAS400_) {
        if (!systemName.equals("localhost"))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (!systemName.equals(""))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (local)
          failMessage += "Local is true.\n";
      }
      if (!userId.equals("TESTUID"))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string, string) with a system name of the empty
   * string (""), a literal user ID, and a literal password.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var016() {
    try {
      AS400 system = new AS400("", "testUid", "testPwd".toCharArray());

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (onAS400_) {
        if (!systemName.equals("localhost"))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (!local)
          failMessage += "Local is false.\n";
      } else {
        if (!systemName.equals(""))
          failMessage += "Incorrect systemName '" + systemName + "'\n";
        if (local)
          failMessage += "Local is true.\n";
      }
      if (!userId.equals("TESTUID"))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string) with a literal system name and a user
   * ID of *current.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var017() {
    try {
      AS400 system = new AS400("testSystem", "*current");

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("testSystem"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (onAS400_) {
        String expectedUser = System.getProperty("user.name").toUpperCase();
        if (!userId.equals(expectedUser))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      } else {
        if (!userId.equals("*CURRENT"))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      }
      if (local)
        failMessage += "Local is true.\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string) with a literal system name and a user
   * ID of the empty string ("").</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var018() {
    try {
      AS400 system = new AS400("testSystem", "");

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("testSystem"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (onAS400_) {
        String expectedUser = System.getProperty("user.name").toUpperCase();
        if (!userId.equals(expectedUser))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      } else {
        if (!userId.equals(""))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      }
      if (local)
        failMessage += "Local is true.\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string, string) with a literal system name, a
   * user ID of *current, and a literal password.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var019() {
    try {
      AS400 system = new AS400("testSystem", "*current", "testPwd".toCharArray());

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("testSystem"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (onAS400_) {
        String expectedUser = System.getProperty("user.name").toUpperCase();
        if (!userId.equals(expectedUser))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      } else {
        if (!userId.equals("*CURRENT"))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      }
      if (local)
        failMessage += "Local is true.\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string, string) with a literal system name, a
   * user ID of the empty string (""), and a literal password.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var020() {
    try {
      AS400 system = new AS400("testSystem", "", "testPwd".toCharArray());

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("testSystem"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (onAS400_) {
        String expectedUser = System.getProperty("user.name").toUpperCase();
        if (!userId.equals(expectedUser))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      } else {
        if (!userId.equals(""))
          failMessage += "Incorrect userId: '" + userId + "'\n";
      }
      if (local)
        failMessage += "Local is true.\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string, string) with a literal system name, a
   * literal user ID, and a password of "*current".</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var021() {
    try {
      AS400 system = new AS400("testSystem", "testUid", "*current".toCharArray());

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("testSystem"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (!userId.equals("TESTUID"))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (local)
        failMessage += "Local is true.\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string, string) with a literal system name, a
   * literal user ID, and a password of the empty string ("").</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var022() {
    try {
      AS400 system = new AS400("testSystem", "testUid", "".toCharArray());

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("testSystem"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (!userId.equals("TESTUID"))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (local)
        failMessage += "Local is true.\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(AS400) with an existing AS400 object.</dd>
   * <dt>Result:</dt>
   * <dd>Verify the correct initial state of the object.</dd>
   * </dl>
   **/
  public void Var023() {
    try {
      AS400 testSystem = new AS400("testSystem", "testUid", "testPwd".toCharArray());
      AS400 system = new AS400(testSystem);

      String systemName = system.getSystemName();
      String userId = system.getUserId();
      String proxyServer = system.getProxyServer();
      boolean local = system.isLocal();

      String failMessage = "";
      if (!systemName.equals("testSystem"))
        failMessage += "Incorrect systemName '" + systemName + "'\n";
      if (!userId.equals("TESTUID"))
        failMessage += "Incorrect userId: '" + userId + "'\n";
      if (local)
        failMessage += "Local is true.\n";
      if (!proxyServer.equals(proxy_))
        failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
      assertValidState(system, failMessage);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(null).</dd>
   * <dt>Result:</dt>
   * <dd>Verify that a NullPointerException is thrown.</dd>
   * </dl>
   **/
  public void Var024() {
    AS400 system = null;
    try {
      String systemName = null;
      system = new AS400(systemName);
      failed("No exception." + system);
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "systemName");
    } finally {
      if (system != null)
        system.close();
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(null, string) with a literal user ID.</dd>
   * <dt>Result:</dt>
   * <dd>Verify that a NullPointerException is thrown.</dd>
   * </dl>
   **/
  public void Var025() {
    AS400 system = null;
    try {
      String systemName = null;
      system = new AS400(systemName, "testUid");
      failed("No exception." + system);
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "systemName");
    } finally {
      if (system != null)
        system.close();
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string) with a literal system name and a userid
   * of null.</dd>
   * <dt>Result:</dt>
   * <dd>Verify that a NullPointerException is thrown.</dd>
   * </dl>
   **/
  public void Var026() {
    AS400 system = null;
    try {
      String userId = null;
      system = new AS400("testSystem", userId);
      failed("No exception." + system);
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "userId");
    } finally {
      if (system != null)
        system.close();
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string) with a system name of null, a literal
   * user ID, and a literal password.</dd>
   * <dt>Result:</dt>
   * <dd>Verify that a NullPointerException is thrown.</dd>
   * </dl>
   **/
  public void Var027() {
    AS400 system = null;
    try {
      String systemName = null;
      system = new AS400(systemName, "testUid", "testPwd".toCharArray());
      failed("No exception." + system);
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "systemName");
    } finally {
      if (system != null)
        system.close();
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string, string) with a literal system name, a
   * user ID of null, and a literal password.</dd>
   * <dt>Result:</dt>
   * <dd>Verify that a NullPointerException is thrown.</dd>
   * </dl>
   **/
  public void Var028() {
    AS400 system = null;
    try {
      String userId = null;
      system = new AS400("testSystem", userId, "testPwd".toCharArray());
      failed("No exception." + system);
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "userId");
    } finally {
      if (system != null)
        system.close();
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(string, string, string) with a literal system name, a
   * literal user ID, and a password of null.</dd>
   * <dt>Result:</dt>
   * <dd>Verify that a NullPointerException is thrown.</dd>
   * </dl>
   **/
  public void Var029() {
    AS400 system = null;

    try {
      char[] password = null;
      system = new AS400("testSystem", "testUid", password);
      failed("No exception." + system);
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "password");
    } finally {
      if (system != null)
        system.close();
    }
  }

  /**
   * <dl>
   * <dt>Test:</dt>
   * <dd>Call AS400::AS400(AS400) with a null AS400 object.</dd>
   * <dt>Result:</dt>
   * <dd>Verify that a NullPointerException is thrown.</dd>
   * </dl>
   **/
  public void Var030() {
    AS400 system = null;
    try {
      AS400 testSystem = null;
      system = new AS400(testSystem);
      failed("No exception." + system);
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "system");
    } finally {
      if (system != null)
        system.close();
    }
  }

  // test AS400 constructor with empty profile token
  public void Var031() {
    ProfileTokenCredential profileToken = new ProfileTokenCredential();
    AS400 system = new AS400(systemName_, profileToken);
    assertCondition(system.getUserId().equals(""),
        "User ID(" + system.getUserId() + ") is not empty -- added September 2011");
    system.close();
  }

  // test AS400 constructor with given profile token
  public void Var032() {
    String s = System.getProperty("os.name");
    boolean onI5OS = (s != null && s.equalsIgnoreCase("OS/400")) ? true : false;
    if (!onI5OS) {
      notApplicable();
      return;
    }
    ProfileTokenCredential profileToken = new ProfileTokenCredential();
    AS400 system = new AS400(systemName_);
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      profileToken.setSystem(system);
      profileToken.setTokenExtended(userId_, charPassword);
      PasswordVault.clearPassword(charPassword);

//			profileToken.setTimeoutInterval(3600);
//			profileToken.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
      return;
    }
    try {
      system.connectService(AS400.COMMAND);
      assertCondition(system.getUserId().equals(userId_), "User ID doesn't equal system.getUserId()="
          + system.getUserId() + " userId_=" + userId_ + "  -- added September 2011");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      system.disconnectAllServices();
    }
  }

  // test AS400 constructor with locally generated profile token
  public void Var033() {
    ProfileTokenCredential profileToken = new ProfileTokenCredential();
    byte[] token = new byte[ProfileTokenCredential.TOKEN_LENGTH];
    new Random().nextBytes(token);
    try {
      profileToken.setToken(token);
    } catch (PropertyVetoException e) {
      failed(e, "Unexpected exception. -- added September 2011");
    }
    AS400 system = new AS400(systemName_, profileToken);
    assertCondition(system.getUserId().equals(""), "User ID is not empty  -- added September 2011");
    system.close();
  }

  // test AS400 constructor with profile token from other system
  public void Var034() {
    ProfileTokenCredential profileToken = null;
    try {
      profileToken = systemObject_.getProfileToken(ProfileTokenCredential.TYPE_SINGLE_USE, 3600);
    } catch (Exception e) {
      failed(e,
          "Unexpected exception call getProfileToken using ProfileTokenCredential.TYPE_SINGLE_USE from systemObject_="
              + systemObject_ + ".. -- added September 2011");
      return;
    }
    AS400 system = new AS400(systemName_, profileToken);
    try {
      system.connectService(AS400.COMMAND);
      assertCondition(system.getUserId().equals(systemObject_.getUserId()),
          "User ID doesn't equal  -- added September 2011");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      system.disconnectAllServices();
      system.close();
    }
  }

  // test AS400 constructor with profile token created by provider
  public void Var035() {
    AS400 system = null;

    try {
      String s = System.getProperty("os.name");
      boolean onI5OS = (s != null && s.equalsIgnoreCase("OS/400")) ? true : false;
      if (!onI5OS) {
        notApplicable();
        return;
      }
      DefaultProfileTokenProvider provider = new DefaultProfileTokenProvider();
      provider.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
      provider.setTimeoutInterval(3600);
      provider.setUserId(userId_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

      provider.setPassword(charPassword);

      system = new AS400(systemName_, provider);

      system.connectService(AS400.COMMAND);

      // Because of a bug in DefaultProfileTokenProvider, a copy of the charPassword
      // was not made.
      // Clear it here.
      // TODO: After fix move to after the setPassword method
      PasswordVault.clearPassword(charPassword);

      assertCondition(system.getUserId().equals(userId_), "User ID doesn't equal system.getUserId()="
          + system.getUserId() + " userId_=" + userId_ + "  -- added September 2011");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      if (system != null)
        system.disconnectAllServices();
    }
  }

  public void Var036() {
    notApplicable();
  }

  public void Var037() {
    notApplicable();
  }

  public void Var038() {
    notApplicable();
  }

  public void Var039() {
    notApplicable();
  }

  public void Var040() {
    notApplicable();
  }

  // test AS400 constructor with given profile token
  public void Var041() {
    if (checkAdditionalAuthenticationFactor(systemName_)) {
      try {
        initMfaUser();
        String s = System.getProperty("os.name");
        boolean onI5OS = (s != null && s.equalsIgnoreCase("OS/400")) ? true : false;
        AS400 system;
        if (!onI5OS) {
          char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
          system = new AS400(systemName_, userId_, charPassword);
        } else {
          system = new AS400(systemName_);
        }

        ProfileTokenCredential profileToken = new ProfileTokenCredential();
        system.setGuiAvailable(false);
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        profileToken.setSystem(system);
        profileToken.setTimeoutInterval(3600);
        profileToken.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
        JDReflectionUtil.callMethod_V(profileToken, "setTokenExtended", mfaUserid_, mfaPassword_, mfaFactor_);
        PasswordVault.clearPassword(charPassword);
        try {
          system.connectService(AS400.COMMAND);
          AS400 system2 = new AS400(systemName_, profileToken);
          system2.connectService(AS400.COMMAND);
          assertCondition(system.getUserId().equals(userId_.toUpperCase()) && system2.getUserId().equals(mfaUserid_),
              "User ID mismatch system.getUserId()=" + system.getUserId() + " userId_=" + userId_
                  + " or system2.getUserId()=" + system2.getUserId() + " <> mfaUserid_=" + mfaUserid_
                  + " -- updated December 2024");
          system2.close();
          system.close();
        } catch (Exception e) {
          failed(e, "Unexpected exception.");
        } finally {
          system.disconnectAllServices();
        }
      } catch (Exception e) {
        failed(e, "Unexpected exception.");
        return;
      }
    }
  }

  // test AS400 constructor with profile token created by provider
  public void Var042() {
    AS400 system = null;
    if (checkAdditionalAuthenticationFactor(systemName_)) {
      AS400 remoteSystem = null;
      try {
        String s = System.getProperty("os.name");
        boolean onI5OS = (s != null && s.equalsIgnoreCase("OS/400")) ? true : false;
        DefaultProfileTokenProvider provider = new DefaultProfileTokenProvider();
        if (!onI5OS) {
          char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
          remoteSystem = new AS400(systemName_, userId_, charPassword);
          PasswordVault.clearPassword(charPassword);
          provider.setSystem(remoteSystem);

        }
        initMfaUser();
        provider.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
        provider.setTimeoutInterval(3600);
        provider.setUserId(mfaUserid_);
        provider.setPassword(mfaPassword_);
        JDReflectionUtil.callMethod_V(provider,"setAdditionalAuthenticationFactor",mfaFactor_);
        
        system = new AS400(systemName_, provider);
        system.setGuiAvailable(false);
        system.connectService(AS400.COMMAND);

        // Verify that JDBC also works
        AS400JDBCDriver driver = new AS400JDBCDriver();
        Connection c = driver.connect(system);
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("VALUES CURRENT USER");
        rs.next();
        String jdbcUser = rs.getString(1);
        rs.close();
        stmt.close();
        c.close();
        assertCondition(system.getUserId().equals(mfaUserid_) && jdbcUser.equals(mfaUserid_),
            "User ID doesn't equal system.getUserId()=" + system.getUserId() + " mfaUserid_=" + mfaUserid_
                + "  or jdbcUser=" + jdbcUser + " <> mfaUser=" + mfaUserid_ + " -- added September 2011");
      } catch (Exception e) {
        failed(e, "Unexpected exception.");
      } finally {
        if (system != null) {
          system.disconnectAllServices();
          system.close();
        }
        if (remoteSystem != null) {
          remoteSystem.close();
        }
      }
    }
  }

}
