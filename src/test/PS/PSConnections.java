///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PSConnections.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.PS;

import com.ibm.as400.access.*;

import test.EndProxyServer;
import test.PasswordVault;
import test.Testcase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;



/**
The PSConnections class tests the following methods
of the PS class.
        
<li>getActiveConnections()                                                    
<li>getMaxConnections() 
<li>setMaxConnections()
<li>-maxConnections command line argument
<li>maxConnections= in configuration file
**/
public class PSConnections 
extends Testcase 
{



    // Private data.
    private Vector openConnections_ = new Vector ();



/**
Constructor.
**/
    public PSConnections (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "PSConnections",
               (Vector) namesAndVars.get ("PSConnections"), 
               runMode, fileOutputStream,
               password);
    }



/**
Opens some connections to the proxy server.
**/
    void openConnections (int portNumber, int count)
        throws Exception
    {
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        for (int i = 1; i <= count; ++i) {
            AS400 system = new AS400 (systemObject_.getSystemName (), systemObject_.getUserId (), charPassword);
            system.setProxyServer(InetAddress.getLocalHost ().getHostName () + ":" + portNumber);
            CommandCall cc = new CommandCall (system, "PING " + system.getSystemName ());
            cc.run ();
            Object[] systemAndCc = new Object[] { system, cc };
            openConnections_.addElement (systemAndCc);
        }
         PasswordVault.clearPassword(charPassword);
    }



/**
Closes some connections to the proxy server.
**/
    void closeConnections (int count)
        throws Exception
    {
        // The best way to close connections is to get them garbage collected!
        for(int i = 1; i <= count; ++i) {
            Object[] systemAndCc = (Object[])openConnections_.elementAt(0);
            try {
                ((AS400)systemAndCc[0]).disconnectAllServices();
            }
            catch (Exception e) {
                // Ignore this in case the connection is already closed.
            }
            openConnections_.removeElement(systemAndCc);
            systemAndCc = null;
        }
        System.gc();

        // Give the garbage collector time to do its thing.
        Thread.sleep(2000);
    }



/**
Closes all connections to the proxy server.
**/
    void closeAllConnections ()
        throws Exception
    {
        closeConnections (openConnections_.size ());
    }



/**
Counts the number of connections which can be made to the proxy server.
**/
    int countConnections (int portNumber)
        throws Exception
    {
        // Assume unlimited.
        int count = -1;

        for (int i = 1; i <= 15; ++i) {
            try {
                openConnections (portNumber, 1);
            }
            catch (ProxyException e) {
                count = i - 1;
                break;
            }
        }

        closeAllConnections ();
        return count;
    }



/**
getActiveConnections() - Verify that this returns
zero when the proxy server has not been started.
**/
    public void Var001()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            int ac = ps.getActiveConnections ();
            assertCondition (ac == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getActiveConnections() - Verify that this returns
zero when the proxy server has been started,
but no connections have been initiated.
**/
    public void Var002()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17102);
            // ps.setSecurePort(17202);
            ps.start ();
            int ac = ps.getActiveConnections ();
            ps.stop ();
            assertCondition (ac == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getActiveConnections() - Verify that this returns
1 when the proxy server has been started,
and 1 connection has been initiated.
**/
    public void Var003()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17103);
            // ps.setSecurePort(17203);
            ps.start ();
            openConnections (17103, 1);
            int ac = ps.getActiveConnections ();
            closeAllConnections ();
            ps.stop ();
            assertCondition (ac == 1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getActiveConnections() - Verify that this returns
the correct number of connections when the proxy 
server has been started, and many connections
have been initiated.
**/
    public void Var004()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17104);
            // ps.setSecurePort(17204);
            ps.start ();
            openConnections (17104, 7);
            int ac = ps.getActiveConnections ();
            closeAllConnections ();
            ps.stop ();
            assertCondition (ac == 7);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getActiveConnections() - Verify that this returns
0 when the proxy server has been started and stopped
without any connections.
**/
    public void Var005()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17107);
            // ps.setSecurePort(17207);
            ps.start ();
            ps.stop ();
            int ac = ps.getActiveConnections ();
            assertCondition (ac == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getActiveConnections() - Verify that this returns
0 when the proxy server has been started, many 
connections have been initiated, and the proxy
server stops.
**/
    public void Var006()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17108);
            // ps.setSecurePort(17208);
            ps.start ();
            openConnections (17108, 9);
            ps.stop ();
            int ac = ps.getActiveConnections ();
            assertCondition (ac == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getMaxConnections() - Verify that this returns -1
by default.
*/
    public void Var007()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17110);
            // ps.setSecurePort(17210);
            int mc = ps.getMaxConnections ();
            assertCondition (mc == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getMaxConnections() - Verify that this returns -1
if it is set to -1.
*/
    public void Var008()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17111);
            // ps.setSecurePort(17211);
            ps.setMaxConnections (-1);
            int mc = ps.getMaxConnections ();
            assertCondition (mc == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getMaxConnections() - Verify that this returns 0
if it is set to 0.
*/
    public void Var009()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17112);
            // ps.setSecurePort(17212);
            ps.setMaxConnections (0);
            int mc = ps.getMaxConnections ();
            assertCondition (mc == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getMaxConnections() - Verify that this returns non-zero
if it is set to non-zero.
*/
    public void Var010()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17113);
            // ps.setSecurePort(17213);
            ps.setMaxConnections (56);
            int mc = ps.getMaxConnections ();
            assertCondition (mc == 56);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setMaxConnections() - Set to -2.  Verify that an exception
is thrown.
*/
    public void Var011()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17114);
            // ps.setSecurePort(17214);
            ps.setMaxConnections (-2);
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }



/**
setMaxConnections() - Set to -1.  Verify that 
several connections can be made.
*/
    public void Var012()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17115);
            // ps.setSecurePort(17215);
            ps.setMaxConnections (-1);
            ps.start ();
            int mc = countConnections (17115);
            ps.stop ();
            assertCondition (mc == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }




/**
setMaxConnections() - Set to 0.  Verify that 
no connections can be made.
*/
    public void Var013()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17116);
            // ps.setSecurePort(17216);
            ps.setMaxConnections (0);
            ps.start ();
            int mc = countConnections (17116);
            ps.stop ();
            assertCondition (mc == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }




/**
setMaxConnections() - Set to 1.  Verify that 
only 1 connection can be made.
*/
    public void Var014()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17117);
            // ps.setSecurePort(17217);
            ps.setMaxConnections (1);
            ps.start ();
            int mc = countConnections (17117);
            ps.stop ();
            assertCondition (mc == 1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }




/**
setMaxConnections() - Set to 10.  Verify that 
only 10 connections can be made.
*/
    public void Var015()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17118);
            // ps.setSecurePort(17218);
            ps.setMaxConnections (10);
            ps.start ();
            int mc = countConnections (17118);
            ps.stop ();
            assertCondition (mc == 10);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setMaxConnections() - Set to 5 after the 
proxy server has started, but less than
5 connections have been made. 
*/
    public void Var016()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17119);
            // ps.setSecurePort(17219);
            ps.start ();
            openConnections (17119, 2);
            ps.setMaxConnections (5);
            int mc = countConnections (17119);
            ps.stop ();
            assertCondition (mc == 3);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setMaxConnections() - Set to 5 after the 
proxy server has started, and more than
5 connections have been made. Verify
that no more can be made.
*/
    public void Var017()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(17120);
            // ps.setSecurePort(17220);
            ps.start ();
            openConnections (17120, 7);
            ps.setMaxConnections (5);
            int ac = ps.getActiveConnections ();
            int mc = countConnections (17120);
            ps.stop ();
            assertCondition ((ac == 7) && (mc == 0));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-maxConnections - Do NOT set the max connections.  Verify that the
several connections can be made.
**/
    public void Var018()
    {
        try {
            ProxyServer.main (new String[] { "-po", "42218", "-sp", "42318" });
            int mc = countConnections (42218);
            EndProxyServer.end(42218);
            assertCondition (mc == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }





/**
-maxConnections - Set the max connections to 0.  Verify that
no connections can be made.
**/
    public void Var019()
    {
        try {
            ProxyServer.main (new String[] { "-maxConnections", "0","-po", "42219", "-sp", "42319"});
            int mc = countConnections (42219);
            EndProxyServer.end(42219);
            assertCondition (mc == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-maxConnections - Set the max connections to 1.  Verify that
only 1 connection can be made.
**/
    public void Var020()
    {
        try {
            ProxyServer.main (new String[] { "-maxConnections", "1","-po", "42220", "-sp", "42320"});
            int mc = countConnections (42220);
            EndProxyServer.end(42220);
            assertCondition (mc == 1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-maxConnections - Set the max connections to 11.  Verify that
11 connections can be made.
**/
    public void Var021()
    {
        try {
            ProxyServer.main (new String[] { "-maxConnections", "11","-po", "42221", "-sp", "42321" });
            int mc = countConnections (42221);
            EndProxyServer.end(42221);
            assertCondition (mc == 11);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-maxConnections - Use the -mc abbreviation instead of -maxConnections.
**/
    public void Var022()
    {
        try {
            ProxyServer.main (new String[] { "-mc", "4","-po", "42222", "-sp", "42322"  });
            int mc = countConnections (42222);
            EndProxyServer.end(42222);
            assertCondition (mc == 4);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-maxConnections - Use it to reconfigure a running proxy server.
**/
    public void Var023()
    {
        try {
            ProxyServer ps = new ProxyServer();
            ps.setMaxConnections(654);
            ps.setPort(18001);
            // ps.setSecurePort(18002);
            ps.start();

            ProxyServer.main(new String[] { "-port", "18001", "-securePort", "18002", 
                                            "-maxConnections",  "8865" });
            Thread.sleep(1000); // Let configuration change take effect.

            int mc = ps.getMaxConnections();
            ps.stop();
            assertCondition(mc == 8865);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
maxConnections= - Set the max connections to -1.  Verify that
several connections can be made.
**/
    public void Var024()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("maxConnections=-1");
            ProxyServer.main (new String[] { "-c", configuration,"-po", "42224", "-sp", "42324" });
            int mc = countConnections (42224);
            EndProxyServer.end(42224);
            assertCondition (mc == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
maxConnections= - Set the max connections to 0.  Verify that
no connections can be made.
**/
    public void Var025()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("maxConnections=0");
            ProxyServer.main (new String[] { "-c", configuration,"-po", "42225", "-sp", "42325" });
            int mc = countConnections (42225);
            EndProxyServer.end(42225);
            assertCondition (mc == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-maxConnections - Set the max connections to 1.  Verify that
only 1 connection can be made.
**/
    public void Var026()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("maxConnections=1");
            ProxyServer.main (new String[] { "-c", configuration,"-po", "42226", "-sp", "42326" });
            int mc = countConnections (42226);
            EndProxyServer.end(42226);
            assertCondition (mc == 1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
maxConnections= - Set the max connections to 15.  Verify that
15 connections can be made.
**/
    public void Var027()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("maxConnections=10");
            ProxyServer.main (new String[] { "-c", configuration,"-po", "42227", "-sp", "42327" });
            int mc = countConnections (42227);
            EndProxyServer.end(42227);
            assertCondition (mc == 10);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
maxConnections= - Use it to reconfigure a running proxy server.
**/
    public void Var028()
    {
        try {
            ProxyServer ps = new ProxyServer();
            ps.setMaxConnections(86432);
            ps.setPort(18003);
            // ps.setSecurePort(18004);
            ps.start();

            String configuration = PSConfiguration.createConfiguration("maxConnections=23");
            ProxyServer.main(new String[] { "-port", "18003", "-securePort", "18004", 
                                            "-c",  configuration });

            Thread.sleep(1000); // Let configuration change take effect.
            int mc = ps.getMaxConnections();
            ps.stop();
            assertCondition(mc == 23);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



}
