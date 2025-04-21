///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PSPort.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.PS;

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ProxyServer;

import test.EndProxyServer;
import test.PSTest;
import test.Testcase;



/**
The PSPort class tests the following methods
of the ProxtServer class.
                                                    
<li>getPort() 
<li>setPort()
<li>-port command line argument
<li>port= in configuration file
**/
public class PSPort 
extends Testcase 
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PSPort";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PSTest.main(newArgs); 
   }



    // Private data.
    public static final int DEFAULT_PORT       = 3470;



/**
Constructor.
**/
    public PSPort (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "PSPort",
               namesAndVars.get ("PSPort"), 
               runMode, fileOutputStream,
               password);
    }



/**
getPort() - Verify that the default port is returned
when none have been set, and the server has not been
started.
**/
    public void Var001()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            assertCondition (ps.getPort () == DEFAULT_PORT);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getPort() - Verify that the correct port is returned
when it has been set to non-zero using setPort(),
and the server has not been started.
**/
    public void Var002()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort (823);
            assertCondition (ps.getPort () == 823);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getPort() - Verify that the correct port is returned
when it has been set to zero using setPort(),
and the server has not been started.
**/
    public void Var003()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort (0);
            assertCondition (ps.getPort () == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getPort() - Verify that the default port is returned
when none have been set, and the server has been
started.
**/
    public void Var004()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.start ();
            assertCondition (ps.getPort () == DEFAULT_PORT);
            ps.stop ();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getPort() - Verify that the correct port is returned
when it has been set to non-zero using setPort(),
and the server has been started.
**/
    public void Var005()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort (4823);
            ps.start ();
            assertCondition (ps.getPort () == 4823);
            ps.stop ();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getPort() - Verify that the correct port is returned
when it has been set to zero using setPort(),
and the server has been started.
**/
    public void Var006()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort (0);
            ps.start ();
            int actualPort = ps.getPort ();
            ps.stop ();
            assertCondition (actualPort > 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setPort() - Do NOT call setPort().  Verify that the
default port is being used.
**/
    public void Var007()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.start ();
            boolean check = PSTest.ping ();
            ps.stop ();
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }





/**
setPort() - Set the port to a negative number.
Should throw an exception.
**/
    public void Var008()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort (-1);
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }



/**
setPort() - Set the port to 0.
Verify that the reported port is being
listened to.
**/
    public void Var009()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort (0);
            ps.start ();
            int actualPort = ps.getPort ();
            boolean check1 = PSTest.ping (actualPort);
            boolean check2 = PSTest.ping ();
            ps.stop ();
            assertCondition ((actualPort > 0) && (check1 == true) && (check2 == false));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setPort() - Set the port to a valid port number
other than the default.  Verify that the port
is being used instead of the default.
**/
    public void Var010()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort (4637);
            ps.start ();
            boolean check1 = PSTest.ping (4637);
            boolean check2 = PSTest.ping ();
            ps.stop ();
            assertCondition ((check1 == true) && (check2 == false));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setPort() - Set the port too high.  An exception
should be thrown.
**/
    public void Var011()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPort (65536);
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }



/**
setPort() - Set the port after the proxy server
has been started.  An exception should be thrown.
**/
    public void Var012()
    {
        ProxyServer ps = null;
        try {
            ps = new ProxyServer ();
            ps.start ();            
            ps.setPort (435);
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
        if (ps != null)
            ps.stop ();
    }




/**
-port - Do NOT set the port.  Verify that the
default port is being used.
**/
    public void Var013()
    {
        try {
            ProxyServer.main (new String[0]);
            boolean check = PSTest.ping (DEFAULT_PORT);
            EndProxyServer.end2 ();
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }





/**
-port - Set the port to a valid port number
other than the default.  Verify that the port
is being used instead of the default.
**/
    public void Var014()
    {
        try {
            ProxyServer.main (new String[] { "-port", "6839" });
            boolean check = PSTest.ping (6839);
            EndProxyServer.end (6839);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-port - Set the port too high.  An exception
should be thrown.
**/
    public void Var015()
    {
        try {
            ProxyServer.main (new String[] { "-port", "775435523" });
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }



/**
-port - Set the port to something that
is not a number.  An exception should be thrown.
**/
    public void Var016()
    {
        try {
            ProxyServer.main (new String[] { "-port", "jt400Proxy" });
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NumberFormatException");
        }
    }




/**
-port - Specify -port without an argument.
**/
    public void Var017()
    {
        try {
            ProxyServer.main (new String[] { "-port" });
            boolean check = PSTest.ping (DEFAULT_PORT);
            EndProxyServer.end2 ();
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-port - Use the -po abbreviation instead of -port.
**/
    public void Var018()
    {
        try {
            ProxyServer.main (new String[] { "-po", "839" });
            boolean check = PSTest.ping (839);
            EndProxyServer.end (839);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



}
