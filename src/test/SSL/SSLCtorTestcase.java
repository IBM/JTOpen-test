///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SSLCtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.SSL;

import java.net.InetAddress;
import java.util.Locale;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SecureAS400;

import test.PasswordVault;
import test.Testcase;

/**
 Testcase SSLCtorTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>SecureAS400::SecureAS400()
 <li>SecureAS400::SecureAS400(String)
 <li>SecureAS400::SecureAS400(String, String)
 <li>SecureAS400::SecureAS400(String, String, String)
 <li>SecureAS400::SecureAS400(String, String, String, String)
 <li>SecureAS400::SecureAS400(String, ProfileTokenCredential)
 <li>SecureAS400::SecureAS400(AS400)
 </ul>
 **/
public class SSLCtorTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SSLCtorTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SSLTest.main(newArgs); 
   }
    // Retrieve and verify correct state.
    private void assertValidState(SecureAS400 system, String failMessage)
    {
        // Retrieve state.
        Locale locale = system.getLocale();
        boolean connected = system.isConnected();
        boolean guiAvailable = system.isGuiAvailable();
        boolean mustUseSockets = system.isMustUseSockets();
        boolean showCheckboxes = system.isShowCheckboxes();
        boolean threadUsed = system.isThreadUsed();
        boolean useDefaultUser = system.isUseDefaultUser();
        boolean usePasswordCache = system.isUsePasswordCache();

        // Verify state.
        if (!locale.equals(Locale.getDefault())) failMessage += "Incorrect Locale: '" + locale + "'\n";
        if (connected) failMessage += "Connected is true.\n";
        if (onAS400_)
        {
            if (guiAvailable) failMessage += "GUI available is true.\n";
        }
        else
        {
            if (!guiAvailable) failMessage += "GUI available is false.\n";
        }
        if (mustUseSockets) failMessage += "Must use sockets is true.\n";
        if (!showCheckboxes) failMessage += "Show checkboxes is false.\n";
        if (!threadUsed) failMessage += "Thread used is false.\n";
        if (!useDefaultUser) failMessage += "Use default user is false.\n";
        if (!usePasswordCache) failMessage += "Use password cache is false.\n";

        assertCondition(failMessage.equals(""), "\n" + failMessage);
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400().</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var001()
    {
        try
        {
            SecureAS400 system = new SecureAS400();

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (onAS400_)
            {
                if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (!systemName.equals("")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (local) failMessage += "Local is true.\n";
            }
            if (isNative_)
            {
                if (!userId.equals(userId_.toUpperCase())) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            else
            {
                if (!userId.equals("")) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string) with a literal system name.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var002()
    {
        try
        {
            SecureAS400 system = new SecureAS400("testSystem");

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("testSystem")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (isNative_)
            {
                if (!userId.equals(userId_.toUpperCase())) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            else
            {
                if (!userId.equals("")) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            if (local) failMessage += "Local is true.\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string) with a literal system name and a literal user ID.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var003()
    {
        try
        {
            SecureAS400 system = new SecureAS400("testSystem", "testUid");

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("testSystem")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (!userId.equals("TESTUID")) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (local) failMessage += "Local is true.\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string, string) with a literal system name, a literal user ID, and a literal password.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var004()
    {
        try
        {
            SecureAS400 system = new SecureAS400("testSystem", "testUid", "testPwd".toCharArray());

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("testSystem")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (!userId.equals("TESTUID")) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (local) failMessage += "Local is true.\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string) with the test driver's system name.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var005()
    {
        try
        {
            SecureAS400 system = new SecureAS400(systemName_);

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (isLocal_)
            {
                if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (!systemName.equals(systemName_)) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (local) failMessage += "Local is true.\n";
            }
            if (isNative_)
            {
                if (!userId.equals(userId_.toUpperCase())) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            else
            {
                if (!userId.equals("")) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string) with the test driver's system name and the test driver's user ID.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var006()
    {
        try
        {
            SecureAS400 system = new SecureAS400(systemName_, userId_);

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (isLocal_)
            {
                if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (!systemName.equals(systemName_)) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (local) failMessage += "Local is true.\n";
            }
            if (!userId.equals(userId_.toUpperCase())) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string, string) with the test driver's system name, the test driver's user ID, and the test driver's password.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var007()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (isLocal_)
            {
                if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (!systemName.equals(systemName_)) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (local) failMessage += "Local is true.\n";
            }
            if (!userId.equals(userId_.toUpperCase())) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string) with the local system's system name.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var008()
    {
        try
        {
            String testSystemName = InetAddress.getLocalHost().getHostName();
            SecureAS400 system = new SecureAS400(testSystemName);

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (onAS400_)
            {
                if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (!systemName.equals(testSystemName)) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (local) failMessage += "Local is true.\n";
            }
            if (isNative_)
            {
                if (!userId.equals(userId_.toUpperCase())) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            else
            {
                if (!userId.equals("")) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string) with the local system's system name and a literal user ID.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var009()
    {
        try
        {
            String testSystemName = InetAddress.getLocalHost().getHostName();
            SecureAS400 system = new SecureAS400(testSystemName, "testUid");

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (onAS400_)
            {
                if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (!systemName.equals(testSystemName)) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (local) failMessage += "Local is true.\n";
            }
            if (!userId.equals("TESTUID")) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string, string) with the local system's system name, a literal user ID, and a literal password.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var010()
    {
        try
        {
            String testSystemName = InetAddress.getLocalHost().getHostName();
            SecureAS400 system = new SecureAS400(testSystemName, "testUid", "testPwd".toCharArray());

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (onAS400_)
            {
                if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (!systemName.equals(testSystemName)) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (local) failMessage += "Local is true.\n";
            }
            if (!userId.equals("TESTUID")) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string) with a system name of "localhost".</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var011()
    {
        try
        {
            SecureAS400 system = new SecureAS400("localhost");

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (isNative_)
            {
                if (!userId.equals(userId_.toUpperCase())) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            else
            {
                if (!userId.equals("")) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            if (onAS400_)
            {
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (local) failMessage += "Local is true.\n";
            }
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string) with a system name of "localhost" and a literal user ID.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var012()
    {
        try
        {
            SecureAS400 system = new SecureAS400("localhost", "testUid");

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (!userId.equals("TESTUID")) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (onAS400_)
            {
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (local) failMessage += "Local is true.\n";
            }
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string, string) with a system name of "localhost", a literal user ID and a literal password.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var013()
    {
        try
        {
            SecureAS400 system = new SecureAS400("localhost", "testUid", "testPwd".toCharArray());

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (!userId.equals("TESTUID")) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (onAS400_)
            {
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (local) failMessage += "Local is true.\n";
            }
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string) with a system name of the empty string ("").</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var014()
    {
        try
        {
            SecureAS400 system = new SecureAS400("");

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (onAS400_)
            {
                if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (!systemName.equals("")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (local) failMessage += "Local is true.\n";
            }
            if (isNative_)
            {
                if (!userId.equals(userId_.toUpperCase())) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            else
            {
                if (!userId.equals("")) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string) with a system name of the empty string ("") and a literal user ID.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var015()
    {
        try
        {
            SecureAS400 system = new SecureAS400("", "testUid");

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (onAS400_)
            {
                if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (!systemName.equals("")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (local) failMessage += "Local is true.\n";
            }
            if (!userId.equals("TESTUID")) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string, string) with a system name of the empty string (""), a literal user ID, and a literal password.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var016()
    {
        try
        {
            SecureAS400 system = new SecureAS400("", "testUid", "testPwd".toCharArray());

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (onAS400_)
            {
                if (!systemName.equals("localhost")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (!local) failMessage += "Local is false.\n";
            }
            else
            {
                if (!systemName.equals("")) failMessage += "Incorrect systemName '" + systemName + "'\n";
                if (local) failMessage += "Local is true.\n";
            }
            if (!userId.equals("TESTUID")) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string) with a literal system name and a user ID of *current.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var017()
    {
        try
        {
            SecureAS400 system = new SecureAS400("testSystem", "*current");

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("testSystem")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (isNative_)
            {
                if (!userId.equals(userId_.toUpperCase())) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            else
            {
                if (!userId.equals("*CURRENT")) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            if (local) failMessage += "Local is true.\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string) with a literal system name and a user ID of the empty string ("").</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var018()
    {
        try
        {
            SecureAS400 system = new SecureAS400("testSystem", "");

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("testSystem")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (isNative_)
            {
                if (!userId.equals(userId_.toUpperCase())) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            else
            {
                if (!userId.equals("")) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            if (local) failMessage += "Local is true.\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string, string) with a literal system name, a user ID of *current, and a literal password.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var019()
    {
        try
        {
            SecureAS400 system = new SecureAS400("testSystem", "*current", "testPwd".toCharArray());

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("testSystem")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (isNative_)
            {
                if (!userId.equals(userId_.toUpperCase())) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            else
            {
                if (!userId.equals("*CURRENT")) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            if (local) failMessage += "Local is true.\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string, string) with a literal system name, a user ID of the empty string (""), and a literal password.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var020()
    {
        try
        {
            SecureAS400 system = new SecureAS400("testSystem", "", "testPwd".toCharArray());

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("testSystem")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (isNative_)
            {
                if (!userId.equals(userId_.toUpperCase())) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            else
            {
                if (!userId.equals("")) failMessage += "Incorrect userId: '" + userId + "'\n";
            }
            if (local) failMessage += "Local is true.\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string, string) with a literal system name, a literal user ID, and a password of "*current".</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var021()
    {
        try
        {
            SecureAS400 system = new SecureAS400("testSystem", "testUid", "*current".toCharArray());

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("testSystem")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (!userId.equals("TESTUID")) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (local) failMessage += "Local is true.\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string, string) with a literal system name, a literal user ID, and a password of the empty string ("").</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var022()
    {
        try
        {
            SecureAS400 system = new SecureAS400("testSystem", "testUid", "".toCharArray());

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("testSystem")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (!userId.equals("TESTUID")) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (local) failMessage += "Local is true.\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(AS400) with an existing SecureAS400 object.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var023()
    {
        try
        {
            SecureAS400 testSystem = new SecureAS400("testSystem", "testUid", "testPwd".toCharArray());
            SecureAS400 system = new SecureAS400(testSystem);

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("testSystem")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (!userId.equals("TESTUID")) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (local) failMessage += "Local is true.\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(null).</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var024()
    {
        try
        {
            String systemName = null;
            SecureAS400 system = new SecureAS400(systemName);
            system.close(); 
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "systemName");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(null, string) with a literal user ID.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var025()
    {
        try
        {
            String systemName = null;
            SecureAS400 system = new SecureAS400(systemName, "testUid");
            system.close(); 
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "systemName");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string) with a literal system name and a userid of null.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var026()
    {
        try
        {
            String userId = null;
            SecureAS400 system = new SecureAS400("testSystem", userId);
            system.close(); 
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "userId");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string) with a system name of null, a literal user ID, and a literal password.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var027()
    {
        try
        {
            String systemName = null;
            SecureAS400 system = new SecureAS400(systemName, "testUid", "testPwd".toCharArray());
            system.close(); 
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "systemName");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string, string) with a literal system name, a user ID of null, and a literal password.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var028()
    {
        try
        {
            String userId = null;
            SecureAS400 system = new SecureAS400("testSystem", userId, "testPwd".toCharArray());
            system.close(); 
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "userId");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(string, string, string) with a literal system name, a literal user ID, and a password of null.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var029()
    {
        try
        {
            char[] password = null;
            SecureAS400 system = new SecureAS400("testSystem", "testUid", password);
            system.close(); 
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "password");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(AS400) with a null SecureAS400 object.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var030()
    {
        try
        {
            SecureAS400 testSystem = null;
            SecureAS400 system = new SecureAS400(testSystem);
            system.close(); 
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(AS400) with a null AS400 object.</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var031()
    {
        try
        {
            AS400 testSystem = null;
            SecureAS400 system = new SecureAS400(testSystem);
            system.close(); 
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "system"))
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
     <dl>
     <dt>Test:</dt><dd>Call SecureAS400::SecureAS400(AS400) with an existing AS400 object.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var032()
    {
        try
        {
            AS400 testSystem = new AS400("testSystem", "testUid", "testPwd".toCharArray());
            SecureAS400 system = new SecureAS400(testSystem);

            String systemName = system.getSystemName();
            String userId = system.getUserId();
            String proxyServer = system.getProxyServer();
            boolean local = system.isLocal();

            String failMessage = "";
            if (!systemName.equals("testSystem")) failMessage += "Incorrect systemName '" + systemName + "'\n";
            if (!userId.equals("TESTUID")) failMessage += "Incorrect userId: '" + userId + "'\n";
            if (local) failMessage += "Local is true.\n";
            if (!proxyServer.equals(proxy_)) failMessage += "Incorrect proxy server: '" + proxyServer + "'\n";
            assertValidState(system, failMessage);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
