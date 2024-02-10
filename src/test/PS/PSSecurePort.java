///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PSSecurePort.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.PS;

import com.ibm.as400.access.*;

import test.PSTest;
import test.Testcase;

import java.io.IOException;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;



/**
The PSSecurePort class tests the following methods
of the ProxyServer class.
                                                    
<li>getSecurePort() 
<li>setSecurePort()
<li>-securePort command line argument
<li>securePort= in configuration file
**/
public class PSSecurePort 
extends Testcase 
{


                                                     
    // Private data.
    static final int DEFAULT_SECURE_PORT       = 3471;




/**
Constructor.
**/
    public PSSecurePort (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "PSSecurePort",
               (Vector) namesAndVars.get ("PSSecurePort"), 
               runMode, fileOutputStream,
               password);
    }



/**
Pings the proxy server to verify that the proxy server is 
listening to the default port.
**/
    static boolean securePing ()
    {
        return securePing (PSSecurePort.DEFAULT_SECURE_PORT);
    }



/**
Pings the proxy server to verify that the proxy server is 
listening to the specified port.
**/
    static boolean securePing (int port)
    {
        // Connect to the proxy server.
        Socket clientSocket;
        try {            
            clientSocket = createSSLSocket (InetAddress.getLocalHost().toString(), port);
        }
        catch (IOException e) {
            // e.printStackTrace (System.out);
            return false;
        }

        boolean check = PSTest.ping(clientSocket);

        try {
            clientSocket.close();
        }
        catch(IOException e) {
            e.printStackTrace(System.out);
        }

        return check;
    }



/**
Creates an SSL socket.

@param hostName The host name.
@param port     The port.
@return         The socket.

@exception IOException  If an error occurs.
**/
    private static Socket createSSLSocket (String hostName, int port)
        throws IOException
    {
        // SSLContext context = initializeSSLContext();
        // SSLSocket socket = new SSLSocket(hostName, port, context, SSLSocket.CLIENT, null);
        SocketFactory socketFactory = SSLSocketFactory.getDefault(); 
        Socket socket = socketFactory.createSocket(hostName, port); 
        return socket;
    }



/**
Initializes an SSL context.

@return The context.

@exception IOException  If an error occurs.
**/
    /* 
    static SSLContext initializeSSLContext ()
        throws IOException
    {
        // Create the SSL context.
        SSLContext context = new SSLContext ();

        // Load the key ring.        
        SSLightKeyRing keyRing = null;
        try {
            keyRing = (SSLightKeyRing) Class.forName ("com.ibm.as400.access.KeyRing").newInstance ();
            String keyRingData = keyRing.getKeyRingData ();
            context.importKeyRings (keyRingData, "toolbox");
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return context;
    }
*/ 


/**
getSecurePort() - Verify that the default port is returned
when none have been set, and the server has not been
started.
**/
    public void Var001()
    {
        notApplicable();
        /*
        try {
            ProxyServer ps = new ProxyServer ();
            assertCondition (ps.getSecurePort () == DEFAULT_SECURE_PORT);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }



/**
getSecurePort() - Verify that the correct port is returned
when it has been set to non-zero using setSecurePort(),
and the server has not been started.
**/
    public void Var002()
    {
        notApplicable();
        /*
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setSecurePort (823);
            assertCondition (ps.getSecurePort () == 823);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }



/**
getSecurePort() - Verify that the correct port is returned
when it has been set to zero using setSecurePort(),
and the server has not been started.
**/
    public void Var003()
    {
        notApplicable();
        /*
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setSecurePort (0);
            assertCondition (ps.getSecurePort () == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }



/**
getSecurePort() - Verify that the default port is returned
when none have been set, and the server has been
started.
**/
    public void Var004()
    {
        notApplicable();
        /*
        try {
            ProxyServer ps = new ProxyServer ();
            ps.start ();
            assertCondition (ps.getSecurePort () == DEFAULT_SECURE_PORT);
            ps.stop ();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }



/**
getSecurePort() - Verify that the correct port is returned
when it has been set to non-zero using setSecurePort(),
and the server has been started.
**/
    public void Var005()
    {
        notApplicable();
        /*
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setSecurePort (4823);
            ps.start ();
            assertCondition (ps.getSecurePort () == 4823);
            ps.stop ();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }



/**
getSecurePort() - Verify that the correct port is returned
when it has been set to zero using setSecurePort(),
and the server has been started.
**/
    public void Var006()
    {
        notApplicable();
        /*
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setSecurePort (0);
            ps.start ();
            int actualPort = ps.getSecurePort ();
            ps.stop ();
            assertCondition (actualPort > 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }



/**
setSecurePort() - Do NOT call setSecurePort().  Verify that the
default port is being used.
**/
    public void Var007()
    {
        notApplicable();
        /*
        try {
            ProxyServer ps = new ProxyServer ();
            ps.start ();
            boolean check = securePing();
            ps.stop ();
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }





/**
setSecurePort() - Set the port to a negative number.
Should throw an exception.
**/
    public void Var008()
    {
        notApplicable();
        /*
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setSecurePort (-1);
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
        */
    }



/**
setSecurePort() - Set the port to 0.
Verify that the reported port is being
listened to.
**/
    public void Var009()
    {
        notApplicable();
        /*
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setSecurePort (0);
            ps.start ();
            int actualPort = ps.getSecurePort ();
            boolean check1 = securePing (actualPort);
            boolean check2 = securePing ();
            ps.stop ();
            assertCondition ((actualPort > 0) && (check1 == true) && (check2 == false));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }



/**
setSecurePort() - Set the port to a valid port number
other than the default.  Verify that the port
is being used instead of the default.
**/
    public void Var010()
    {
        notApplicable();
        /*
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setSecurePort (4637);
            ps.start ();
            boolean check1 = securePing (4637);
            boolean check2 = securePing ();
            ps.stop ();
            assertCondition ((check1 == true) && (check2 == false));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }



/**
setSecurePort() - Set the port too high.  An exception
should be thrown.
**/
    public void Var011()
    {
        notApplicable();
        /*
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setSecurePort (65536);
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
        */
    }



/**
setSecurePort() - Set the port after the proxy server
has been started.  An exception should be thrown.
**/
    public void Var012()
    {
        notApplicable();
        /*
        ProxyServer ps = null;
        try {
            ps = new ProxyServer ();
            ps.start ();            
            ps.setSecurePort (435);
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
        if (ps != null)
            ps.stop ();
        */
    }




/**
-securePort - Do NOT set the port.  Verify that the
default port is being used.
**/
    public void Var013()
    {
        notApplicable();
        /*
        try {
            ProxyServer.main (new String[0]);
            boolean check = securePing (DEFAULT_SECURE_PORT);
            EndProxyServer.end2 ();
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }





/**
-securePort - Set the port to a valid port number
other than the default.  Verify that the port
is being used instead of the default.
**/
    public void Var014()
    {
        notApplicable();
        /*
        try {
            ProxyServer.main (new String[] { "-securePort", "6839" });
            boolean check = securePing (6839);
            EndProxyServer.end (6839);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }



/**
-securePort - Set the port too high.  An exception
should be thrown.
**/
    public void Var015()
    {
        notApplicable();
        /*
        try {
            ProxyServer.main (new String[] { "-securePort", "775435523" });
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
        */
    }



/**
-securePort - Set the port to something that
is not a number.  An exception should be thrown.
**/
    public void Var016()
    {
        notApplicable();
        /*
        try {
            ProxyServer.main (new String[] { "-securePort", "jt400Proxy" });
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NumberFormatException");
        }
        */
    }




/**
-securePort - Specify -securePort without an argument.
**/
    public void Var017()
    {
        notApplicable();
        /*
        try {
            ProxyServer.main (new String[] { "-securePort" });
            boolean check = securePing (DEFAULT_SECURE_PORT);
            EndProxyServer.end2 ();
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }



/**
-securePort - Use the -sp abbreviation instead of -securePort.
**/
    public void Var018()
    {
        notApplicable();
        /*
        try {
            ProxyServer.main (new String[] { "-sp", "839" });
            boolean check = securePing (839);
            EndProxyServer.end (839);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
        */
    }




}
