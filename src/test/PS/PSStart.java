///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PSStart.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.PS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.ProxyServer;

import test.PSTest;
import test.Testcase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;



/**
The PSStart class tests the following methods
of the PS class.
        
<li>constructor                                                    
<li>start() 
<li>stop()
<li>isStarted()

<p>It is important to run this testcase twice: once with
SSLight in the classpath, and once without SSLight in the
classpath.
**/
public class PSStart 
extends Testcase 
{



/**
Constructor.
**/
    public PSStart (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "PSStart",
               (Vector) namesAndVars.get ("PSStart"), 
               runMode, fileOutputStream,
               password);
    }



/**
constructor - Verify that this creates a proxy
server without throwing an exception.  Verify
that it does not start.
**/
    public void Var001()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24001);
            // // ps.setSecurePort(24101);
            boolean check = PSTest.ping (24001);
            assertCondition (check == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
start() - Verify that this starts the proxy server
if it has not been started previously.
**/
    public void Var002()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24002);
            // // ps.setSecurePort(24102);
            ps.start ();
            boolean check = PSTest.ping (24002);
            ps.stop ();
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
start() - Verify that this throws an exception
if the proxy server has already been started.
Verify that the proxy server is still running.
**/
    public void Var003()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24003);
            // // ps.setSecurePort(24103);
            ps.start ();
            boolean check1 = false;
            try {
                ps.start ();
            }
            catch (ExtendedIllegalStateException e) {
                check1 = (e.getReturnCode () == ExtendedIllegalStateException.PROXY_SERVER_ALREADY_STARTED);
            }            
            boolean check2 = PSTest.ping (24003);
            ps.stop ();
            assertCondition ((check1 == true) && (check2 == true));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
start() - Verify that this starts the proxy server
if it has been started and stopped previously.
**/
    public void Var004()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24004);
            // // ps.setSecurePort(24104);
            ps.start ();
            ps.stop ();
            ps.start ();
            boolean check = PSTest.ping (24004);
            ps.stop ();
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
start() - Verify that an exception is thrown
if there is already a proxy server listening.
Verify that the original proxy server continues
to listen.
**/
    public void Var005()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24005);
            // // ps.setSecurePort(24105);
            ps.start ();
            ProxyServer ps2 = new ProxyServer ();
            ps2.setPort(24005);
            // ps2.setSecurePort(24105);
            boolean check1 = false;
            try {
                ps2.start ();
            }
            catch (Exception e) { 
                e.printStackTrace ();
                check1 = true;
            }
            boolean check2 = PSTest.ping (24005);
            ps.stop ();
            assertCondition ((check1 == false) && (check2 == true));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
stop() - Verify that this throws an exception
if the proxy server has not been started.
Also verify that the proxy server is not started
after trying to stop it.
**/
    public void Var006()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24006);
            // // ps.setSecurePort(24106);
            boolean check1 = false;
            try {
                ps.stop ();
            }
            catch (ExtendedIllegalStateException e) {
                check1 = (e.getReturnCode () == ExtendedIllegalStateException.PROXY_SERVER_NOT_STARTED);
            }
            boolean check2 = PSTest.ping (24006);
            assertCondition ((check1 == true) && (check2 == false));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
stop() - Verify that stopping a started proxy
server works.
**/
    public void Var007()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24007);
            // // ps.setSecurePort(24107);
            ps.start ();
            ps.stop ();
            boolean check = PSTest.ping (24007);
            assertCondition (check == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
stop() - Verify that stopping a started proxy
server works.  This time give the proxy server
several seconds to get started.
**/
    public void Var008()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24008);
            // // ps.setSecurePort(24108);
            ps.start ();
            Thread.sleep (5000);
            ps.stop ();
            boolean check = PSTest.ping (24008);
            assertCondition (check == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
stop() - Verify that stopping a started proxy
server works.  This time ping the proxy server
first to ensure that it has started some connection
threads.
**/
    public void Var009()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24009);
            // // ps.setSecurePort(24109);
            ps.start ();
            PSTest.ping ();
            ps.stop ();
            boolean check = PSTest.ping (24009);
            assertCondition (check == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
stop() - Verify that stopping a RE-started proxy
server works.
**/
    public void Var010()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24010);
            // // ps.setSecurePort(24110);
            ps.start ();
            ps.stop ();
            ps.start ();
            ps.stop ();
            boolean check = PSTest.ping (24010);
            assertCondition (check == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
isStarted() - Verify that this returns false
if the proxy server has not been started.
**/
    public void Var011()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            boolean check = ps.isStarted ();
            assertCondition (check == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
isStarted() - Verify that this returns true
if the proxy server has been started.
**/
    public void Var012()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24012);
            // // ps.setSecurePort(24112);
            ps.start ();
            boolean check = ps.isStarted ();
            ps.stop ();
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
isStarted() - Verify that this returns false
if the proxy server has been started and stopped.
**/
    public void Var013()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24013);
            // // ps.setSecurePort(24113);
            ps.start ();
            ps.stop ();
            boolean check = ps.isStarted ();
            assertCondition (check == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }




/**
isStarted() - Verify that this returns true
if the proxy server has been RE-started.
**/
    public void Var014()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort(24014);
            // // ps.setSecurePort(24114);
            ps.start ();
            ps.stop ();
            ps.start ();
            boolean check = ps.isStarted ();
            ps.stop ();
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



}
