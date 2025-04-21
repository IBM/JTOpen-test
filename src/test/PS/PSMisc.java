///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PSMisc.java
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
The PSMisc class tests the following methods
of the ProxtServer class.
                                                    
<li>main()
<li>isVerbose()
<li>setVerbose()
<li>-verbose command line argument
<li>verbose= in configuration file
<li>-help command line argument
<li>configuation file
**/
public class PSMisc 
extends Testcase 
{



/**
Constructor.
**/
    public PSMisc (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "PSMisc",
               namesAndVars.get ("PSMisc"), 
               runMode, fileOutputStream,
               password);
    }



/**
main() - Verify that an exception is thrown for null.
**/
    public void Var001()
    {
        try {
            ProxyServer.main (null);
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
main() - Verify that it works if no arguments are passed.
**/
    public void Var002()
    {
        try {
            ProxyServer.main (new String[0]);
            boolean check = PSTest.ping (3470);
            EndProxyServer.end2 ();
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
main() - Verify that it works, but outputs a warning
if bad arguments are passed.
**/
    public void Var003()
    {
        try {
            ProxyServer.main (new String[] { "-clif" });
            boolean check = PSTest.ping (3470);
            EndProxyServer.end2 ();
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
main() - Verify that it works, but outputs a warning
if bad arguments are passed before good arguments.
**/
    public void Var004()
    {
        try {
            ProxyServer.main (new String[] { "-clif", "-port", "875", "-sp", "1875" });
            boolean check = PSTest.ping (875);
            EndProxyServer.end (875);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
main() - Verify that it works, but outputs a warning
if bad arguments are passed after good arguments.
**/
    public void Var005()
    {
        try {
            ProxyServer.main (new String[] { "-port", "876", "-sp", "1876", "-nock" });
            boolean check = PSTest.ping (876);
            EndProxyServer.end (876);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
main() - Try with all command line arguments specified (except -help).
**/
    public void Var006()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("#");
            ProxyServer.main (new String[] { "-balanceThreshold", "3",
                                             "-configuration", configuration,
                                             "-jdbcDrivers", "com.ibm.as400.access.AS400JDBCDriver;test.PSJdbcDriversSampleDriver",
                                             "-maxConnections", "10",
                                             "-peers", "london;manchester;cmurch",
                                             "-port", "877",
                                             "-securePort", "878",
                                             "-verbose", "false" });
            EndProxyServer.end (877);
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
isVerbose() - Should return false (default) if not set.
**/
    public void Var007()
    {
        try {
            ProxyServer ps = new ProxyServer();
            assertCondition(ps.isVerbose() == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
isVerbose()/setVerbose() - Should return false if set to false.
**/
    public void Var008()
    {
        try {
            ProxyServer ps = new ProxyServer();
            ps.setVerbose(false);
            assertCondition(ps.isVerbose() == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
isVerbose()/setVerbose() - Should return true if set to true.
**/
    public void Var009()
    {
        try {
            ProxyServer ps = new ProxyServer();
            ps.setVerbose(true);
            assertCondition(ps.isVerbose() == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-verbose command line argument - Specify a bad value.
**/
    public void Var010()
    {
        try {
            ProxyServer.main (new String[] { "-verbose", "information" });
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-verbose command line argument - Don't specify a value.
**/
    public void Var011()
    {
        try {
            ProxyServer.main (new String[] { "-verbose" });
            EndProxyServer.end2 ();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-verbose command line argument - Specify false.
**/
    public void Var012()
    {
        try {
            ProxyServer.main (new String[] { "-verbose", "false" });
            EndProxyServer.end2 ();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-verbose command line argument - Specify true.
**/
    public void Var013()
    {
        try {
            ProxyServer.main (new String[] { "-verbose", "true" });
            EndProxyServer.end2 ();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-verbose command line argument - Use the -v abbreviation.
**/
    public void Var014()
    {
        try {
            ProxyServer.main (new String[] { "-v", "false" });
            EndProxyServer.end2 ();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-verbose - Use it to reconfigure a running proxy server.
**/
    public void Var015()
    {
        try {
            ProxyServer ps = new ProxyServer();
            ps.setVerbose(false);
            ps.setPort(19001);
            // ps.setSecurePort(19002);
            ps.start();

            ProxyServer.main(new String[] { "-port", "19001", "-securePort", "19002", 
                                            "-verbose",  "true" });

            ps.stop();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
verbose= in configuration file - Specify a bad value.
**/
    public void Var016()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("verbose=information");
            ProxyServer.main (new String[] { "-c", configuration });
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
verbose= in configuration file - Don't specify a value.
**/
    public void Var017()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("verbose=");
            ProxyServer.main (new String[] { "-c", configuration });
            EndProxyServer.end2 ();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
verbose= in configuration file - Specify false.
**/
    public void Var018()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("verbose=false");
            ProxyServer.main (new String[] { "-c", configuration });
            EndProxyServer.end2 ();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
verbose= in configuration file - Specify true.
**/
    public void Var019()
    {
        try {
            String configuration = PSConfiguration.createConfiguration("verbose=true");
            ProxyServer.main (new String[] { "-c", configuration });
            EndProxyServer.end2 ();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
verbose= - Use it to reconfigure a running proxy server.
**/
    public void Var020()
    {
        try {
            ProxyServer ps = new ProxyServer();
            ps.setVerbose(true);
            ps.setPort(19003);
            // ps.setSecurePort(19004);
            ps.start();

            String configuration = PSConfiguration.createConfiguration("verbose=false");
            ProxyServer.main(new String[] { "-port", "19003", "-securePort", "19004", 
                                            "-c",  configuration });

            ps.stop();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-help command line argument - Specify with a value.
**/
    public void Var021()
    {
        try {
            ProxyServer.main (new String[] { "-help", "information" });
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-help command line argument - Specify without a value.
**/
    public void Var022()
    {
        try {
            ProxyServer.main (new String[] { "-help" });
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



}
