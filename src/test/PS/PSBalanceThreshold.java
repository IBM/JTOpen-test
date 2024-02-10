///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PSBalanceThreshold.java
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
import test.Testcase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;



/**
The PSBalanceThreshold class tests the following methods
of the PS class.
        
<li>getBalanceThreshold()                                                    
<li>setBalanceThreshold()
<li>-balanceThreshold command line argument
<li>balanceThreshold= in configuration file
**/
public class PSBalanceThreshold
extends Testcase 
{



/**
Constructor.
**/
    public PSBalanceThreshold (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "PSBalanceThreshold",
               (Vector) namesAndVars.get ("PSBalanceThreshold"), 
               runMode, fileOutputStream,
               password);
    }



    private int getBalanceThreshold(int port)
        throws IOException
    {
        Socket clientSocket;
        try {
            // Connect to the proxy server.
            clientSocket = new Socket (InetAddress.getLocalHost (), port);
        }
        catch(ConnectException e) {
            return -9999;
        }
    
        // Verify that the proxy server is listening to the port.
        // We can just send a load request.
        InputStream input = clientSocket.getInputStream ();
        OutputStream output = clientSocket.getOutputStream ();

        DataOutputStream dataOutput = new DataOutputStream (output);
        dataOutput.writeShort (11050);      // Load request.
        dataOutput.writeInt (0);            // No subdatastreams.
        dataOutput.writeBoolean (false);    // Not asynchronous.
        dataOutput.writeLong(5784);         // Correlation id.
        dataOutput.flush ();

        DataInputStream dataInput = new DataInputStream (input);
        short type = dataInput.readShort ();    // Load reply type.
        int numberOfSubs = dataInput.readInt(); // Number of subdatastreams.
        type = dataInput.readShort();           // Integer parm type for active connections.
        int active = dataInput.readInt();       // Active connections.
        type = dataInput.readShort();           // Integer parm type for balance threshold.
        int bt = dataInput.readInt();           // Balance threshold.

        input.close ();
        output.close ();
        clientSocket.close ();

        return bt;
    }



/**
getBalanceThreshold() - Verify that this returns -1
by default.
*/
    public void Var001()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            int bt = ps.getBalanceThreshold ();
            assertCondition (bt == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getBalanceThreshold() - Verify that this returns -1
if it is set to -1.
*/
    public void Var002()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setBalanceThreshold(-1);
            int bt = ps.getBalanceThreshold();
            assertCondition (bt == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getBalanceThreshold() - Verify that this returns 0
if it is set to 0.
*/
    public void Var003()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setBalanceThreshold (0);
            int bt = ps.getBalanceThreshold ();
            assertCondition (bt == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getBalanceThreshold() - Verify that this returns non-zero
if it is set to non-zero.
*/
    public void Var004()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setBalanceThreshold (656);
            int bt = ps.getBalanceThreshold ();
            assertCondition (bt == 656);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setBalanceThreshold() - Set to -2.  Verify that an exception
is thrown.
*/
    public void Var005()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setBalanceThreshold (-2);
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }



/**
setBalanceThreshold() - Set to -1.  Verify that 
several connections can be made.
*/
    public void Var006()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setBalanceThreshold (-1);
            ps.setPort(47000);
            // ps.setSecurePort(47100);
            ps.start ();
            int bt = getBalanceThreshold (47000);
            ps.stop ();
            assertCondition (bt == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }




/**
setBalanceThreshold() - Set to 0.  Verify that 
no connections can be made.
*/
    public void Var007()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setBalanceThreshold (0);
            ps.setPort(47001);
            // ps.setSecurePort(47101);
            ps.start ();
            int bt = getBalanceThreshold (47001);
            ps.stop ();
            assertCondition (bt == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }




/**
setBalanceThreshold() - Set to 1.  Verify that 
only 1 connection can be made.
*/
    public void Var008()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setBalanceThreshold (1);
            ps.setPort(47008);
            // ps.setSecurePort(47108);
            ps.start ();
            int bt = getBalanceThreshold (47008);
            ps.stop ();
            assertCondition (bt == 1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }




/**
setBalanceThreshold() - Set to 10.  Verify that 
only 10 connections can be made.
*/
    public void Var009()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setBalanceThreshold (10);
            ps.setPort(47009);
            // ps.setSecurePort(47109);
            ps.start ();
            int bt = getBalanceThreshold (47009);
            ps.stop ();
            assertCondition (bt == 10);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-balanceThreshold - Do NOT set the balance threshold.  Verify that the
several connections can be made.
**/
    public void Var010()
    {
        try {
            ProxyServer.main (new String[] { "-port", "47010", "-sp", "47110" });
            int bt = getBalanceThreshold (47010);            
            EndProxyServer.end (47010);
            assertCondition (bt == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



// Note: There is no way to specify -1 or -2 here (which would be nice to test).
//       The reason is that they start with a dash, and the parser thinks that
//       they are new options.



/**
-balanceThreshold - Set the balance threshold to 0.  Verify that
no connections can be made.
**/
    public void Var011()
    {
        try {
            ProxyServer.main (new String[] { "-balanceThreshold", "0", "-port", "47011", "-sp", "47111" });
            int bt = getBalanceThreshold (47011);            
            EndProxyServer.end (47011);
            assertCondition (bt == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-balanceThreshold - Set the balance threshold to 1.  Verify that
only 1 connection can be made.
**/
    public void Var012()
    {
        try {
            ProxyServer.main (new String[] { "-balanceThreshold", "1", "-port", "47012", "-sp", "47112" });
            int bt = getBalanceThreshold (47012);            
            EndProxyServer.end (47012);
            assertCondition (bt == 1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-balanceThreshold - Set the balance threshold to 11.  Verify that
11 connections can be made.
**/
    public void Var013()
    {
        try {
            ProxyServer.main (new String[] { "-balanceThreshold", "11", "-port", "47013", "-sp", "47113" });
            int bt = getBalanceThreshold (47013);            
            EndProxyServer.end (47013);
            assertCondition (bt == 11);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-balanceThreshold - Set the balance threshold to something that
is not a number.  An exception should be thrown.
**/
    public void Var014()
    {
        try {
            ProxyServer.main (new String[] { "-balanceThreshold", "Fairbanks", "-port", "47014", "-sp", "47114" });
            int bt = getBalanceThreshold (47014);            
            // Not needed (proxy server never starts): EndProxyServer.end (47014);
            assertCondition (bt == -9999);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }




/**
-balanceThreshold - Specify -balanceThreshold without an argument.  
**/
    public void Var015()
    {
        try {
            ProxyServer.main (new String[] { "-balanceThreshold", "-port", "47015", "-sp", "47115" });
            int bt = getBalanceThreshold (47015);            
            // Not needed (proxy server never starts): EndProxyServer.end (47015);
            assertCondition (bt == -9999);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-balanceThreshold - Use the -bt abbreviation instead of -balanceThreshold.
**/
    public void Var016()
    {
        try {
            ProxyServer.main (new String[] { "-bt", "4", "-port", "47016", "-sp", "47116" });
            int bt = getBalanceThreshold (47016);            
            EndProxyServer.end (47016);
            assertCondition (bt == 4);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-balanceThreshold - Use it to reconfigure a running proxy server.
**/
    public void Var017()
    {
        try {
            ProxyServer ps = new ProxyServer();
            ps.setBalanceThreshold(885);
            ps.setPort(9005);
            // ps.setSecurePort(9006);
            ps.start();

            ProxyServer.main(new String[] { "-port", "9005", "-securePort", "9006", 
                                            "-balanceThreshold",  "9332" });
            Thread.sleep(1000); // Let configuration change take effect.
            int bt = ps.getBalanceThreshold();
            ps.stop();
            assertCondition(bt == 9332);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
balanceThreshold= - Set the balance threshold to -2.
Should throw an exception.
**/
    public void Var018()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("balanceThreshold=-2");
            ProxyServer.main (new String[] { "-c", configuration, "-port", "47018", "-sp", "47118" });
            int bt = getBalanceThreshold (47018);
            // Not needed (proxy server never starts): EndProxyServer.end (47018);
            assertCondition (bt == -9999);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NumberFormatException");
        }       
    }



/**
balanceThreshold= - Set the balance threshold to -1.  Verify that
several connections can be made.
**/
    public void Var019()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("balanceThreshold=-1");
            ProxyServer.main (new String[] { "-c", configuration });
            ProxyServer.main (new String[] { "-c", configuration, "-port", "47019", "-sp", "47119" });
            int bt = getBalanceThreshold (47019);
            EndProxyServer.end (47019);
            assertCondition (bt == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
balanceThreshold= - Set the balance threshold to 0.  Verify that
no connections can be made.
**/
    public void Var020()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("balanceThreshold=0");
            ProxyServer.main (new String[] { "-c", configuration, "-port", "47020", "-sp", "47120" });
            int bt = getBalanceThreshold (47020);
            EndProxyServer.end (47020);
            assertCondition (bt == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-balanceThreshold - Set the balance threshold to 1.  Verify that
only 1 connection can be made.
**/
    public void Var021()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("balanceThreshold=1");
            ProxyServer.main (new String[] { "-c", configuration, "-port", "47021", "-sp", "47121" });
            int bt = getBalanceThreshold (47021);
            EndProxyServer.end (47021);
            assertCondition (bt == 1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
balanceThreshold= - Set the balance threshold to 15.  Verify that
15 connections can be made.
**/
    public void Var022()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("balanceThreshold=15");
            ProxyServer.main (new String[] { "-c", configuration, "-port", "47022", "-sp", "47122" });
            int bt = getBalanceThreshold (47022);
            EndProxyServer.end (47022);
            assertCondition (bt == 15);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
balanceThreshold= - Set the balance threshold to something that
is not a number.  An exception should be thrown.
**/
    public void Var023()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("balanceThreshold=Rochester");
            ProxyServer.main (new String[] { "-c", configuration, "-port", "47023", "-sp", "47123" });
            int bt = getBalanceThreshold (47023);
            // Not needed (proxy server never starts): EndProxyServer.end (47023);
            assertCondition (bt == -9999);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }




/**
balanceThreshold= - Specify balanceThreshold= without an argument.  Unlimited
connections should be allowed.
**/
    public void Var024()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("balanceThreshold=");
            ProxyServer.main (new String[] { "-c", configuration, "-port", "47024", "-sp", "47124" });
            int bt = getBalanceThreshold (47024);
            // Not needed (proxy server never starts): EndProxyServer.end (47024);
            assertCondition (bt == -9999);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
balanceThreshold= - Use it to reconfigure a running proxy server.
**/
    public void Var025()
    {
        try {
            ProxyServer ps = new ProxyServer();
            ps.setBalanceThreshold(332);
            ps.setPort(9907);
            // ps.setSecurePort(9908);
            ps.start();

            String configuration = PSConfiguration.createConfiguration("balanceThreshold=7");
            ProxyServer.main(new String[] { "-port", "9907", "-securePort", "9908", 
                                            "-c",  configuration });
            Thread.sleep(1000); // Let configuration change take effect.
            int bt = ps.getBalanceThreshold();
            ps.stop();
            assertCondition(bt == 7);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }


}
