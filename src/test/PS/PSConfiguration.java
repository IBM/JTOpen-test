///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PSConfiguration.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.PS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ProxyServer;

import test.EndProxyServer;
import test.Testcase;



/**
The PSConfiguration class tests the following methods
of the PS class.
        
<li>getConfiguration()                                                    
<li>setConfiguration()
<li>-configuration command line argument
**/
public class PSConfiguration 
extends Testcase 
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PSConfiguration";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PSTest.main(newArgs); 
   }



    // Private data.



/**
Constructor.
**/
    public PSConfiguration (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "PSConfiguration",
               namesAndVars.get ("PSConfiguration"), 
               runMode, fileOutputStream,
               password);
    }



/**
Creates a configuration file.
**/
    static String createConfiguration (String text)
    {
        return createConfiguration ("test.configuration", new String[] { text });
    }



/**
Creates a configuration file.
**/
    static String createConfiguration (String configuration, String text)
    {
        return createConfiguration (configuration, new String[] { text });
    }



/**
Creates a configuration file.
**/
    static String createConfiguration (String[] text)
    {
        return createConfiguration ("test.configuration", text);
    }



/**
Creates a configuration file.
**/
    static String createConfiguration (String configuration, String[] text)
    {
        try {
            PrintWriter output = new PrintWriter(new FileWriter (configuration));
            for (int i = 0; i < text.length; ++i)
                output.println (text[i]);
            output.close ();
        }
        catch (IOException e) {
            e.printStackTrace ();
        }
        return configuration;
    }



/**
Deletes a configuration file.
**/
    static void deleteConfiguration (String configuration)
    {
        File file = new File (configuration);
        file.delete ();
    }



/**
getConfiguration() - Verify that this returns null
by default.
*/
    public void Var001()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            String configuration = ps.getConfiguration ();
            assertCondition (configuration == null);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
getConfiguration() - Verify that this returns valid filename
if it is set to a valid filename.
*/
    public void Var002()
    {
        try {
            String configuration = createConfiguration("peers=clif");
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            String configuration2 = ps.getConfiguration();
            assertCondition (configuration2.equals(configuration));
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - Set to null.  Verify that an exception
is thrown.
*/
    public void Var003()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setConfiguration (null);
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setConfiguration() - Set to an empty string.  Verify that an exception
is thrown.
*/
    public void Var004()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setConfiguration ("");
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }



/**
setConfiguration() - Set to a filename that does not
exist.  Verify that an exception is thrown.
*/
    public void Var005()
    {
        try {
            ProxyServer ps = new ProxyServer ();
            ps.setConfiguration("ThisFileDoesNotExist");
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.io.FileNotFoundException");
        }
    }



/**
setConfiguration() - Set to a filename that exists in the current directory.  
Verify that the configuration was used.
*/
    public void Var006()
    {
        try {
            String configuration = createConfiguration("ps.config", "maxConnections=19");
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            int mc = ps.getMaxConnections();
            deleteConfiguration(configuration);
            assertCondition(mc == 19);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - Set to a filename that exists in another directory.  
Verify that the configuration was used.
*/
    public void Var007()
    {
        try {
            String configuration = createConfiguration("/ps.config", "maxConnections=8");
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            int mc = ps.getMaxConnections();
            deleteConfiguration(configuration);
            assertCondition(mc == 8);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - Set to a file which is empty.  
Verify that the configuration was used.
*/
    public void Var008()
    {
        try {
            String configuration = createConfiguration(new String[0]);
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            int mc = ps.getMaxConnections();
            assertCondition(mc == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - Set to a file which contains a bogus property.  
*/
    public void Var009()
    {
        try {
            String configuration = createConfiguration(new String[] { "bogus=property" });
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            failed ("Did not throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setConfiguration() - Set to a file which contains only a comment char.  
Verify that the configuration was used.
*/
    public void Var010()
    {
        try {
            String configuration = createConfiguration(new String[] { "#" });
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            int mc = ps.getMaxConnections();
            assertCondition(mc == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - Set to a file which contains only a comment line.  
Verify that the configuration was used.
*/
    public void Var011()
    {
        try {
            String configuration = createConfiguration(new String[] { "# This is a comment line." });
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            int mc = ps.getMaxConnections();
            assertCondition(mc == -1);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - Set to a file which contains only a comment line and a propery.  
Verify that the configuration was used.
*/
    public void Var012()
    {
        try {
            String configuration = createConfiguration(new String[] { "# This is a comment line.",
                                                                             "maxConnections=165" });
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            int mc = ps.getMaxConnections();
            assertCondition(mc == 165);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - Set to a file which contains a propery with leading spaces.  
Verify that the configuration was used.
*/
    public void Var013()
    {
        try {
            String configuration = createConfiguration("    maxConnections=1365");
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            int mc = ps.getMaxConnections();
            assertCondition(mc == 1365);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - Set to a file which contains a propery with spaces before the =.  
Verify that the configuration was used.
*/
    public void Var014()
    {
        try {
            String configuration = createConfiguration("maxConnections =1363");
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            int mc = ps.getMaxConnections();
            assertCondition(mc == 1363);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - Set to a file which contains a propery with spaces after the =.  
Verify that the configuration was used.
*/
    public void Var015()
    {
        try {
            String configuration = createConfiguration("maxConnections= 163");
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            int mc = ps.getMaxConnections();
            assertCondition(mc == 163);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - Set to a file which contains many properties.  
Verify that the configuration was used.
*/
    public void Var016()
    {
        try {
            String configuration = createConfiguration(new String[] { "balanceThreshold=1365",
                                                                             "#THis has many properties",
                                                                             "maxConnections=4949",
                                                                             "peers = clif,chris,robb,jeff,darin,jim" });
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            int mc = ps.getMaxConnections();
            assertCondition(mc == 4949);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - The configuration sets the property twice.  
*/
    public void Var017()
    {
        try {
            String configuration = createConfiguration(new String[] { "maxConnections=2198", "maxConnections=7865" });
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            int mc = ps.getMaxConnections();
            deleteConfiguration(configuration);
            assertCondition(mc == 7865);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - The configuration sets a property that was set with a method before
the configuration was loaded.  Verify that the configuration's value is in effect.
*/
    public void Var018()
    {
        try {
            String configuration = createConfiguration("maxConnections=99");
            ProxyServer ps = new ProxyServer();
            ps.setMaxConnections(4322);
            ps.setConfiguration(configuration);
            int mc = ps.getMaxConnections();
            deleteConfiguration(configuration);
            assertCondition(mc == 99);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
setConfiguration() - The configuration sets a property that was set with a method after
the configuration was loaded.  Verify that the configuration's value is in effect.
*/
    public void Var019()
    {
        try {
            String configuration = createConfiguration("maxConnections=99");
            ProxyServer ps = new ProxyServer();
            ps.setConfiguration(configuration);
            ps.setMaxConnections(4322);
            int mc = ps.getMaxConnections();
            deleteConfiguration(configuration);
            assertCondition(mc == 4322);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-configuration - Do NOT set the configuration.  Verify that
defaults are in effect.
**/
    public void Var020()
    {
        try {
            PSJdbcDrivers.deregisterAllDrivers();
            ProxyServer.main (new String[] { "-port", "7720", "-sp", "7820" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == false);
            EndProxyServer.end(7720);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }





/**
-configuration - Set the configuration to a non-existent file.
**/
    public void Var021()
    {
        try {
            PSJdbcDrivers.deregisterAllDrivers();
            ProxyServer.main (new String[] { "-configuration", "ThisFileStillDoesNotExist","-port", "7721", "-sp", "7821" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == false);
            // Never started, not needed: EndProxyServer.end(7721);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-configuration - Set the configuration to valid file.
**/
    public void Var022()
    {
        try {
            PSJdbcDrivers.deregisterAllDrivers();
            String configuration = createConfiguration("jdbcDrivers=com.ibm.as400.access.AS400JDBCDriver");
            ProxyServer.main (new String[] { "-configuration", configuration,"-port", "7722", "-sp", "7822" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            boolean check1 = enumeration.nextElement().getClass().getName().equals("com.ibm.as400.access.AS400JDBCDriver");
            boolean check2 = (enumeration.hasMoreElements() == false);
            EndProxyServer.end(7722);
            assertCondition (check && check1 && check2);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-configuration - Use the -c abbreviation instead of -configuration.
**/
    public void Var023()
    {
        try {
            PSJdbcDrivers.deregisterAllDrivers();
            String configuration = createConfiguration("jdbcDrivers=test.ProxyServerJdbcDriversSampleDriver");
            ProxyServer.main (new String[] { "-c", configuration,"-port", "7723", "-sp", "7823" });
            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            boolean check1 = enumeration.nextElement().getClass().getName().equals("test.ProxyServerJdbcDriversSampleDriver");
            boolean check2 = (enumeration.hasMoreElements() == false);
            EndProxyServer.end(7723);
            assertCondition (check && check1 && check2);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }

    
}
