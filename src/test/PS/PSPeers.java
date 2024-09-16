///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PSPeers.java
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
The PSPeers class tests the following methods
of the ProxyServer class.
        
<li>getPeers() 
<li>setPeers()
<li>-peers command line argument
<li>peers= in configuration file
**/
public class PSPeers 
extends Testcase 
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PSPeers";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PSTest.main(newArgs); 
   }



    // Private data.
    private String local;



/**
Constructor.
**/
    public PSPeers (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "PSPeers",
               (Vector) namesAndVars.get ("PSPeers"), 
               runMode, fileOutputStream,
               password);
    }



    /**
     @exception  Exception  If an exception occurs.
     **/
    protected void setup()
        throws Exception
    {
        local = InetAddress.getLocalHost().getHostName();
    }



    // It is assumed that a proxy server for main port has already been started,
    // with max connections = 0.
    private boolean verifyPeers(int mainPort, int[] peerPorts)
        throws Exception
    {
        // Start the peer proxy servers.
        ProxyServer[] ps = new ProxyServer[peerPorts.length];
        for(int i = 0; i < peerPorts.length; ++i) {
            ps[i] = new ProxyServer();
            ps[i].setPort(peerPorts[i]);
            // ps[i].setSecurePort(peerPorts[i]+1000);
            ps[i].setMaxConnections(1);
            ps[i].start();
        }

        // Try some connections.
        boolean check = true;
        for(int i = 0; i < peerPorts.length; ++i) {
           char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), 
                                     charPassword, local + ":" +mainPort);
            PasswordVault.clearPassword(charPassword);
            CommandCall cmd = new CommandCall(system);
            boolean result = cmd.run("PING " + systemObject_.getSystemName());

            // If there are peers, than this should work.  Otherwise, it
            // should not.
            if (peerPorts.length > 0)
                check = check && result;
            else
                check = check && !result;
        }

        // End the peer proxy servers and the main server.
        for(int i = 0; i < ps.length; ++i) 
            ps[i].stop();
        EndProxyServer.end (mainPort);

        return check;
    }


/**
getPeers() - Verify that this returns
an empty array when no peers have been specified.
**/
    public void Var001()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            String[] peers = ps.getPeers();
            assertCondition (peers.length == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getPeers()/setPeers() - Verify that this returns
an empty array when an empty array has been specified.
**/
    public void Var002()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPeers(new String[0]);
            String[] peers = ps.getPeers();
            assertCondition (peers.length == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getPeers()/setPeers() - Verify that this returns
1 peer when 1 has been set.
**/
    public void Var003()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPeers(new String[] { "Bill" });
            String[] peers = ps.getPeers();
            assertCondition ((peers.length == 1) && (peers[0].equals("Bill")));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getPeers()/setPeers() - Verify that this returns
1 peer when 1 has been set, with a port number.
**/
    public void Var004()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPeers(new String[] { "JEff:34535" });
            String[] peers = ps.getPeers();
            assertCondition ((peers.length == 1) && (peers[0].equals("JEff:34535")));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getPeers()/setPeers() - Verify that this returns
10 peer when 10 have been set.
**/
    public void Var005()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPeers(new String[] { "Bob:2243", "George:212", "Ronald", "Jimmy", "Gerald:1",
                                       "Richard",  "Lyndon:1", "John", "Harry", "Dwight" });
            String[] peers = ps.getPeers();
            assertCondition ((peers.length == 10) 
                    && (peers[0].equals("Bob:2243"))
                    && (peers[1].equals("George:212"))
                    && (peers[2].equals("Ronald"))
                    && (peers[3].equals("Jimmy"))
                    && (peers[4].equals("Gerald:1"))
                    && (peers[5].equals("Richard"))
                    && (peers[6].equals("Lyndon:1"))
                    && (peers[7].equals("John"))
                    && (peers[8].equals("Harry"))
                    && (peers[9].equals("Dwight")));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setPeers() - Pass null.
**/
    public void Var006()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setPeers(null);
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
-peers - Don't specify anything.  No peers defined.
**/
    public void Var007()
    {
        try {
            ProxyServer.main(new String[] { "-port", "2001", "-securePort", "2002", 
                                            "-mc", "0", "-peers" });
            assertCondition(verifyPeers(2001, new int[0]));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-peers - Specify only a semicolon.  No peers defined.
**/
    public void Var008()
    {
        try {
            ProxyServer.main(new String[] { "-port", "2003", "-securePort", "2004", 
                                            "-mc", "0", "-peers", ";" });
            assertCondition(verifyPeers(2003, new int[0]));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-peers - Specify a peer that does not exist.
**/
    public void Var009()
    {
        try {
            ProxyServer.main(new String[] { "-port", "2037", "-securePort", "2038", 
                                            "-mc", "0", "-peers", "nopeer" });
            assertCondition(verifyPeers(2037, new int[0]));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-peers - Specify a single peer.
**/
    public void Var010()
    {
        try {
            ProxyServer.main(new String[] { "-port", "2005", "-securePort", "2006", 
                                            "-mc", "0", "-peers", local + ":1987" });
            assertCondition(verifyPeers(2005, new int[] { 1987 }));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-peer - Specify a single peer with a leading semicolon.
**/
    public void Var011()
    {
        try {
            ProxyServer.main(new String[] { "-port", "2007", "-securePort", "2008", 
                                            "-mc", "0", "-peers", ";" + local + ":1988" });
            assertCondition(verifyPeers(2007, new int[] { 1988 }));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-peers - Specify a single peer with a trailing semicolon.
**/
    public void Var012()
    {
        try {
            ProxyServer.main(new String[] { "-port", "2009", "-securePort", "2010", 
                                            "-mc", "0", "-peers", local + ":1989;" });
            assertCondition(verifyPeers(2009, new int[] { 1989 }));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-peers - Specify a two peers.
**/
    public void Var013()
    {
        try {
            ProxyServer.main(new String[] { "-port", "2011", "-securePort", "2012", 
                                            "-mc", "0", "-peers", local + ":1990;" + local + ":1991" });
            assertCondition(verifyPeers(2011, new int[] { 1990, 1991 }));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-peers - Specify a two peers with a space before the semicolon.
**/
    public void Var014()
    {
        try {
            ProxyServer.main(new String[] { "-port", "2013", "-securePort", "2014", 
                                            "-mc", "0", "-peers", local + ":1992 ;" + local + ":1993" });
            assertCondition(verifyPeers(2013, new int[] { 1992, 1993 }));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-peers - Specify a two peers with a space after the semicolon.
**/
    public void Var015()
    {
        try {
            ProxyServer.main(new String[] { "-port", "2015", "-securePort", "2016", 
                                            "-mc", "0", "-peers", local + ":1994; " + local + ":1995" });
            assertCondition(verifyPeers(2015, new int[] { 1994, 1995 }));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-peers - Use -pe instead of -peers.
**/
    public void Var016()
    {
        try {
            ProxyServer.main(new String[] { "-port", "2017", "-securePort", "2018", 
                                            "-mc", "0", "-pe", local + ":1996" });
            assertCondition(verifyPeers(2017, new int[] { 1996 }));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-peers - Use it to reconfigure a running proxy server.
**/
    public void Var017()
    {
        try {
            ProxyServer ps = new ProxyServer();
            ps.setPeers(new String[] { "Java1", "Java2", "Java3" });
            ps.setPort(9001);
            // // ps.setSecurePort(9002);
            ps.start();

            ProxyServer.main(new String[] { "-port", "9001", "-securePort", "9002", 
                                            "-peers",  "Java4;Java5" });
            Thread.sleep(1000); // Let configuration change take effect.
            String[] peers = ps.getPeers();
            ps.stop();
            assertCondition((peers.length == 2) && (peers[0].equals("Java4")) 
                   && (peers[1].equals("Java5")));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
peers= - Don't specify anything.  No peers defined.
**/
    public void Var018()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("peers=");
            ProxyServer.main(new String[] { "-port", "2019", "-securePort", "2020", 
                                            "-mc", "0", "-c", configuration });
            assertCondition(verifyPeers(2019, new int[0]));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
peers= - Specify only a semicolon.  No peers defined.
**/
    public void Var019()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("peers=;");
            ProxyServer.main(new String[] { "-port", "2021", "-securePort", "2022", 
                                            "-mc", "0", "-c", configuration });
            assertCondition(verifyPeers(2021, new int[0]));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
peers= - Specify a peer that does not exist.
**/
    public void Var020()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("peers=badpeer;");
            ProxyServer.main(new String[] { "-port", "2035", "-securePort", "2036", 
                                            "-mc", "0", "-c", configuration });
            assertCondition(verifyPeers(2035, new int[0]));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
peers= - Specify a single peer.
**/
    public void Var021()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("peers=" + local + ":9001");
            ProxyServer.main(new String[] { "-port", "2023", "-securePort", "2024", 
                                            "-mc", "0", "-c", configuration });
            assertCondition(verifyPeers(2023, new int[] { 9001 }));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }


/**
peer= - Specify a single peer with a leading semicolon.
**/
    public void Var022()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("peers=;" + local + ":9002");
            ProxyServer.main(new String[] { "-port", "2025", "-securePort", "2026", 
                                            "-mc", "0", "-c", configuration });
            assertCondition(verifyPeers(2025, new int[] { 9002 }));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
peers= - Specify a single peer with a trailing semicolon.
**/
    public void Var023()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("peers=" + local + ":9003;");
            ProxyServer.main(new String[] { "-port", "2027", "-securePort", "2028", 
                                            "-mc", "0", "-c", configuration });
            assertCondition(verifyPeers(2027, new int[] { 9003 }));
         }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
peers= - Specify a two peers.
**/
    public void Var024()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("peers=" + local + ":9004;" 
                                                                        + local + ":9005");
            ProxyServer.main(new String[] { "-port", "2029", "-securePort", "2030", 
                                            "-mc", "0", "-c", configuration });
            assertCondition(verifyPeers(2029, new int[] { 9004 }));
         }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
peers= - Specify a two peers with a space before the semicolon.
**/
    public void Var025()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("peers=" + local + ":9006 ;" 
                                                                        + local + ":9007");
            ProxyServer.main(new String[] { "-port", "2031", "-securePort", "2032", 
                                            "-mc", "0", "-c", configuration });
            assertCondition(verifyPeers(2031, new int[] { 9006 }));
         }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
peers= - Specify a two peers with a space after the semicolon.
**/
    public void Var026()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("peers=" + local + ":9008; " 
                                                                        + local + ":9009");
            ProxyServer.main(new String[] { "-port", "2033", "-securePort", "2034", 
                                            "-mc", "0", "-c", configuration });
            assertCondition(verifyPeers(2033, new int[] { 9008 }));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
peers= - Use it to reconfigure a running proxy server.
**/
    public void Var027()
    {
        try {
            ProxyServer ps = new ProxyServer();
            ps.setPeers(new String[] { "Java9", "Java10" });
            ps.setPort(9003);
            // // ps.setSecurePort(9004);
            ps.start();

            String configuration = PSConfiguration.createConfiguration("peers=Java6;Java7;Java8");
            ProxyServer.main(new String[] { "-port", "9003", "-securePort", "9004", 
                                            "-c",  configuration });

            Thread.sleep(1000); // Let configuration change take effect.
            String[] peers = ps.getPeers();
            ps.stop();
            assertCondition((peers.length == 3) && (peers[0].equals("Java6")) 
                   && (peers[1].equals("Java7"))
                   && (peers[2].equals("Java8")));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



}
