///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PSTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.ProxyServer;

import test.PS.PSBalanceThreshold;
import test.PS.PSConfiguration;
import test.PS.PSConnections;
import test.PS.PSJdbcDrivers;
import test.PS.PSMisc;
import test.PS.PSPeers;
import test.PS.PSPort;
import test.PS.PSStart;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;



/**
The PSTest class tests the PS class.
**/
public class PSTest
extends TestDriver
{



/**
Run the test as an application.

@param  args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public static void main (String args[])
        throws Exception
    {
        runApplication (new PSTest (args));

          System.exit(0);
    }



/**
Constructs an object for testing applets.
 @exception  Exception  If an exception occurs.
**/
    public PSTest ()
        throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public PSTest (String[] args)
        throws Exception
    {
        super (args);
    }



/**
Creates the testcases.
**/
    public void createTestcases ()
    {
        addTestcase (new PSBalanceThreshold (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_,
            password_));
        addTestcase (new PSConfiguration (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_,
            password_));
        addTestcase (new PSConnections (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_,
            password_));
        addTestcase (new PSJdbcDrivers (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_,
            password_));
        addTestcase (new PSMisc (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_,
            password_));
        addTestcase (new PSPeers (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_,
            password_));
        addTestcase (new PSPort (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_,
            password_));
//        addTestcase (new PSSecurePort (systemObject_,
//            namesAndVars_, runMode_, fileOutputStream_,
//            password_));
        addTestcase (new PSStart (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_,
            password_));
    }



/**
Pings the proxy server to verify that the proxy server is
listening to the default port.
**/
    public static boolean ping ()
    {
        return ping (PSPort.DEFAULT_PORT);
    }



/**
Pings the proxy server to verify that the proxy server is
listening to the specified port.
**/
    public static boolean ping (int port)
    {
        // Connect to the proxy server.
        Socket clientSocket;
        try {
            clientSocket = new Socket (InetAddress.getLocalHost (), port);
        }
        catch (IOException e) {
            // e.printStackTrace (System.out);
            return false;
        }

        boolean check = ping(clientSocket);

        try {
            clientSocket.close();
        }
        catch(IOException e) {
            e.printStackTrace(System.out);
        }

        return check;
    }


    public static boolean ping(Socket clientSocket)
    {
        // Verify that the proxy server is listening to the port.
        // We can just send a load request.
        try {
            InputStream input = clientSocket.getInputStream ();
            OutputStream output = clientSocket.getOutputStream ();

            DataOutputStream dataOutput = new DataOutputStream (output);
            dataOutput.writeShort (11050);      // Load request.
            dataOutput.writeInt (0);            // No subdatastreams.
            dataOutput.writeBoolean (false);    // Not asynchronous.
            dataOutput.writeLong(18943);        // Correlation id.
            dataOutput.flush ();

            DataInputStream dataInput = new DataInputStream (input);
            short type = dataInput.readShort ();   // Load reply.
            boolean check = (type == 18030);

            input.close ();
            output.close ();
            clientSocket.close ();

            return check;
        }
        catch (IOException e) {
            e.printStackTrace (System.out);
            return false;
        }
    }



}

