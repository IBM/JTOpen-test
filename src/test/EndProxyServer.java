///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  EndProxyServer.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test; 

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Vector;
import com.ibm.as400.access.*; 

/**
 <p>The EndProxyServer class is used to end another proxy server remotely.
 <p>Options:
 <dl>
 <dt><b><code>-proxyServer </b></code><var>hostname[:port]</var></dt>
 <dd>Specifies the proxy server.  This option may be abbreviated <code>-ps</code>.</dd>
 <dt><b><code>-verbose</b></code> [true|false]</dt>
 <dd>Specifies whether to print status and connection information to System.out.  This option may be abbreviated <code>-v</code>.  The default is not to print status and connection information.</dd>
 <dt><b><code>-help</b></code></dt>
 <dd>Prints usage information to System.out.  This option may be abbreviated <code>-h</code> or <code>-?</code>.  The default is not to print usage information.</dd>
 </dl>
 <p>Example usage:
 <pre>java com.ibm.as400.access.EndProxyServer -proxyServer mySystem</pre>
 **/
//
// Implementation note:
//
// This application is not intended to be shipped!  We are just using it to help automate testing.  Consequently,  we do not need to use MRI in this class.
//
public class EndProxyServer extends PxClientConnectionAdapter
{
    // Private data.
    private static final PrintStream errors_ = System.err;

    /**
     Constructs a EndProxyServer object on the current host and default port.
     **/
    public EndProxyServer() throws UnknownHostException
    {
        this(ProxyConstants.PORT_NUMBER);
    }

    /**
     Constructs a EndProxyServer object on the current host.
     @param  proxyServerPort  The proxy server port.
     **/
    public EndProxyServer(int proxyServerPort) throws UnknownHostException
    {
        this(InetAddress.getLocalHost().getHostName(), proxyServerPort);
    }

    /**
     Constructs a EndProxyServer object.
     @param  proxyServerName  The proxy server name.
     @param  proxyServerPort  The proxy server port.
     **/
    public EndProxyServer(String proxyServerName, int proxyServerPort)
    {
        this(proxyServerName + ":" + proxyServerPort);
    }

    /**
     Constructs a EndProxyServer object.
     @param  proxyServerName  The proxy server name.
     **/
    public EndProxyServer(String proxyServer)
    {
        super(proxyServer, null);
    }

    /**
     Ends the proxy server.
     **/
    public void end()
    {
        end(false);
    }

    private void end(boolean endJVM)
    {
        PxEndReqCV request = new PxEndReqCV(endJVM);
        send(request);
        close();
    }

    /**
     Ends the proxy server.
     **/
    public static void end2() throws UnknownHostException
    {
        EndProxyServer endProxyServer = new EndProxyServer();
        endProxyServer.end();
    }

    /**
     Ends the proxy server.
     @param  portNumber  The proxy server port.
     **/
    public static void end(int portNumber) throws UnknownHostException
    {
        EndProxyServer endProxyServer = new EndProxyServer(portNumber);
        endProxyServer.end();
    }

    /**
     Ends the proxy server.
     @param  hostName  The proxy server name.
     @param  portNumber  The proxy server port.
     **/
    public static void end(String hostName, int portNumber)
    {
        EndProxyServer endProxyServer = new EndProxyServer(hostName, portNumber);
        endProxyServer.end();
    }

    /**
     Runs the proxy server as an application.
     @param  args  The command line arguments.
     **/
    public static void main(String args[])
    {
        // Expection options for the EndProxyServer application.
        Vector<String> expectedOptions = new Vector<String>();
        expectedOptions.addElement("-proxyServer");
        expectedOptions.addElement("-verbose");
        expectedOptions.addElement("-help");

        // Shortcuts for the EndProxyServer application.
        Hashtable<String, String> shortcuts = new Hashtable<String, String>();
        shortcuts.put("-ps", "-proxyServer");
        shortcuts.put("-v", "-verbose");
        shortcuts.put("-h", "-help");
        shortcuts.put("-?", "-help");

        // Parse the command line arguments.
        CommandLineArguments cla = new CommandLineArguments(args, expectedOptions, shortcuts);

        if (cla.getOptionValue("-help") != null)
        {
            usage(System.err);
            return;
        }

        if (cla.getOptionValue("-verbose") != null)
        {
            Verbose.setVerbose(true);
        }

        String proxyServer = cla.getOptionValue("-proxyServer");
        if (proxyServer == null)
        {
            errors_.println("Option not set: -proxyServer");
            return;
        }

        // Send the request.
        EndProxyServer endProxyServer = new EndProxyServer(proxyServer);
        endProxyServer.end(true);
        Verbose.println("Proxy server " + proxyServer + " was ended successfully.");
    }

    /**
     Prints the application usage information.
     @param  usage  The print stream for usage information.
     **/
    private static void usage(PrintStream usage)
    {
        errors_.println("The EndProxyServer application is used to");
        errors_.println("end another proxy server remotely.");
        errors_.println();
        errors_.println("Options:");
        errors_.println();
        errors_.println("-proxyServer hostname[:port]");
        errors_.println("     Specifies the proxy server.  This option");
        errors_.println("     may be abbreviated -ps.");
        errors_.println();
        errors_.println("-verbose [true|false]");
        errors_.println("     Specifies whether to print status and");
        errors_.println("     connection information to System.out. This");
        errors_.println("     option may be abbreviated -v.  The default");
        errors_.println("     is not to print status and connection");
        errors_.println("     information.");
        errors_.println();
        errors_.println("-help");
        errors_.println("     Prints usage information to System.out.");
        errors_.println("     This option may be abbreviated -h or -?.");
        errors_.println("     The default is not to print usage information.");
    }
}
