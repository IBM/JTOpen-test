///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PSJdbcDrivers.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.PS;

import java.io.FileOutputStream;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ProxyServer;

import test.EndProxyServer;
import test.Testcase;



/**
The PSJdbcDrivers class tests the following methods
of the PS class.
                                                    
<li>-jdbcDrivers command line argument
<li>jdbcDrivers= in configuration file
**/
public class PSJdbcDrivers
extends Testcase 
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PSJdbcDrivers";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PSTest.main(newArgs); 
   }



    // Private data.

    // We use this to assign a new port for each variation.  This gets by a Sun 
    // bug where connecting from the same client to the same port causes problems.
    private int port_ = 2000;


/**
Constructor.
**/
    public PSJdbcDrivers (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "PSJdbcDrivers",
               namesAndVars.get ("PSJdbcDrivers"), 
               runMode, fileOutputStream,
               password);
    }



/**
Deregisters all drivers from the driver manager.
**/
    static void deregisterAllDrivers ()
        throws SQLException
    {
        Enumeration<Driver> enumeration = DriverManager.getDrivers ();
        while (enumeration.hasMoreElements ())
            DriverManager.deregisterDriver ((Driver) enumeration.nextElement ());
    }



/**
-jdbcDrivers - Do NOT set the jdbcDrivers.  Verify that the
no JDBC drivers are registered.
**/
    public void Var001()
    {
        try {
            deregisterAllDrivers ();
            ProxyServer.main (new String[] { "-port", "4400", "-sp", "4401" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == false);
            EndProxyServer.end (4400);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Set the jdbcDrivers to a number.
Verify that the no JDBC drivers are registered.
**/
    public void Var002()
    {
        try {
            deregisterAllDrivers ();
            int port = ++port_;
            ProxyServer.main (new String[] { "-jdbcDrivers", "1553", "-port", Integer.toString (port) });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Set the jdbcDrivers to a semicolon.
Verify that the no JDBC drivers are registered.
**/
    public void Var003()
    {
        try {
            deregisterAllDrivers ();
            int port = ++port_;
            ProxyServer.main (new String[] { "-jdbcDrivers", ";", "-port", Integer.toString (port), "-sp", "4402" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Set the jdbcDrivers to a driver that is not in the classpath.
Verify that the no JDBC drivers are registered.
**/
    public void Var004()
    {
        try {
            deregisterAllDrivers ();
            int port = ++port_;
            ProxyServer.main (new String[] { "-jdbcDrivers", "com.lame.driver", "-port", Integer.toString (port), "-sp", "4403"  });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Set the jdbcDrivers to one JDBC driver.
Verify that the driver is registered.
**/
    public void Var005()
    {
        try {
            deregisterAllDrivers ();
            int port = ++port_;
            ProxyServer.main (new String[] { "-jdbcDrivers", "com.ibm.as400.access.AS400JDBCDriver", "-port", Integer.toString (port), "-sp", "4404"  });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Set the jdbcDrivers to one JDBC driver that
exists and one that does not (at the end of the list).
Verify that good driver is registered.
**/
    public void Var006()
    {
        try {
            deregisterAllDrivers ();
            int port = ++port_;
            ProxyServer.main (new String[] { "-jdbcDrivers", "com.ibm.as400.access.AS400JDBCDriver;com.lame.driver", "-port", Integer.toString (port), "-sp", "4405" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Set the jdbcDrivers to one JDBC driver that
exists and one that does not (at the beginning of the list).
Verify that good driver is registered.
**/
    public void Var007()
    {
        try {
            deregisterAllDrivers ();
            int port = ++port_;
            ProxyServer.main (new String[] { "-jdbcDrivers", "com.lame.driver;com.ibm.as400.access.AS400JDBCDriver", "-port", Integer.toString (port), "-sp", "4406" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Set the jdbcDrivers to two JDBC drivers.
Verify that the both drivers are registered.
**/
    public void Var008()
    {
        try {
            deregisterAllDrivers ();
            int port = ++port_;
            ProxyServer.main (new String[] { "-jdbcDrivers", "com.ibm.as400.access.AS400JDBCDriver;test.ProxyServerJdbcDriversSampleDriver", "-port", Integer.toString (port), "-sp", "4407" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Set the jdbcDrivers to two JDBC drivers with 
a semi-colon at the beginning of the list.
Verify that both drivers are registered.
**/
    public void Var009()
    {
        try {
            deregisterAllDrivers ();
            int port = ++port_;
            ProxyServer.main (new String[] { "-jdbcDrivers", ";com.ibm.as400.access.AS400JDBCDriver;test.ProxyServerJdbcDriversSampleDriver", "-port", Integer.toString (port), "-sp", "4408" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Set the jdbcDrivers to two JDBC drivers with 
a semi-colon at the end of the list.
Verify that both drivers are registered.
**/
    public void Var010()
    {
        try {
            deregisterAllDrivers ();
            int port = ++port_;
            ProxyServer.main (new String[] { "-jdbcDrivers", "com.ibm.as400.access.AS400JDBCDriver;test.ProxyServerJdbcDriversSampleDriver;", "-port", Integer.toString (port), "-sp", "4409" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Set the jdbcDrivers to two JDBC drivers with 
a space before the semi-colon.
Verify that both drivers are registered.
**/
    public void Var011()
    {
        try {
            deregisterAllDrivers ();
            int port = ++port_;
            ProxyServer.main (new String[] { "-jdbcDrivers", "com.ibm.as400.access.AS400JDBCDriver ;test.ProxyServerJdbcDriversSampleDriver", "-port", Integer.toString (port), "-sp", "4410" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Set the jdbcDrivers to two JDBC drivers with 
a space after the semi-colon.
Verify that both drivers are registered.
**/
    public void Var012()
    {
        try {
            deregisterAllDrivers ();
            int port = ++port_;
            ProxyServer.main (new String[] { "-jdbcDrivers", "com.ibm.as400.access.AS400JDBCDriver; test.ProxyServerJdbcDriversSampleDriver", "-port", Integer.toString (port), "-sp", "4411" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Use the -jd abbreviation instead of -jdbcDrivers.
**/
    public void Var013()
    {
        try {
            deregisterAllDrivers ();
            int port = ++port_;
            ProxyServer.main (new String[] { "-jd", "com.ibm.as400.access.AS400JDBCDriver;test.ProxyServerJdbcDriversSampleDriver", "-port", Integer.toString (port), "-sp", "4412" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
-jdbcDrivers - Use it to reconfigure a running proxy server.
**/
    public void Var014()
    {
        try {
            deregisterAllDrivers ();
            ProxyServer ps = new ProxyServer();
            ps.setPort(9009);
            // ps.setSecurePort(9010);
            ps.start();

            ProxyServer.main(new String[] { "-port", "9009", "-securePort", "9010", 
                                            "-jdbcDrivers",  "com.ibm.as400.access.AS400JDBCDriver;test.ProxyServerJdbcDriversSampleDriver" });

            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            ps.stop(); 
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Leave it blank.  Verify that the
no JDBC drivers are registered.
**/
    public void Var015()
    {
        try {
            deregisterAllDrivers ();
            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=");
            int port = ++port_;
            ProxyServer.main (new String[] { "-configuration", configuration, "-port", Integer.toString (port), "-sp", "4413" });
            Enumeration<Driver> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port);
            PSConfiguration.deleteConfiguration(configuration);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Set the jdbcDrivers to a number.
Verify that the no JDBC drivers are registered.
**/
    public void Var016()
    {
        try {
            deregisterAllDrivers ();
            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=1553");
            int port = ++port_;
            ProxyServer.main (new String[] { "-configuration", configuration, "-port", Integer.toString (port), "-sp", "4414" });
            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port);
            PSConfiguration.deleteConfiguration(configuration);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Set the jdbcDrivers to a semicolon.
Verify that the no JDBC drivers are registered.
**/
    public void Var017()
    {
        try {
            deregisterAllDrivers ();
            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=;");
            int port = ++port_;
            ProxyServer.main (new String[] { "-configuration", configuration, "-port", Integer.toString (port), "-sp", "4415" });
            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port);
            PSConfiguration.deleteConfiguration(configuration);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Set the jdbcDrivers to a driver that is not in the classpath.
Verify that the no JDBC drivers are registered.
**/
    public void Var018()
    {
        try {
            deregisterAllDrivers ();
            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=com.lame.driver");
            int port = ++port_;
            ProxyServer.main (new String[] { "-configuration", configuration, "-port", Integer.toString (port), "-sp", "4416" });
            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port);
            PSConfiguration.deleteConfiguration(configuration);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Set the jdbcDrivers to one JDBC driver.
Verify that the driver is registered.
**/
    public void Var019()
    {
        try {
            deregisterAllDrivers ();
            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=com.ibm.as400.access.AS400JDBCDriver");
            int port = ++port_;
            ProxyServer.main (new String[] { "-configuration", configuration, "-port", Integer.toString (port), "-sp", "4417" });
            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            PSConfiguration.deleteConfiguration(configuration);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Set the jdbcDrivers to one JDBC driver that
exists and one that does not (at the end of the list).
Verify that good driver is registered.
**/
    public void Var020()
    {
        try {
            deregisterAllDrivers ();
            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=com.ibm.as400.access.AS400JDBCDriver;com.lame.driver");
            int port = ++port_;
            ProxyServer.main (new String[] { "-configuration", configuration, "-port", Integer.toString (port), "-sp", "4418" });
            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            PSConfiguration.deleteConfiguration(configuration);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Set the jdbcDrivers to one JDBC driver that
exists and one that does not (at the beginning of the list).
Verify that good driver is registered.
**/
    public void Var021()
    {
        try {
            deregisterAllDrivers ();
            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=com.lame.driver;com.ibm.as400.access.AS400JDBCDriver");
            int port = ++port_;
            ProxyServer.main (new String[] { "-configuration", configuration, "-port", Integer.toString (port), "-sp", "4419" });
            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            PSConfiguration.deleteConfiguration(configuration);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Set the jdbcDrivers to two JDBC drivers.
Verify that the both drivers are registered.
**/
    public void Var022()
    {
        try {
            deregisterAllDrivers ();
            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=com.ibm.as400.access.AS400JDBCDriver;test.ProxyServerJdbcDriversSampleDriver");
            int port = ++port_;
            ProxyServer.main (new String[] { "-configuration", configuration, "-port", Integer.toString (port), "-sp", "4420" });
            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            PSConfiguration.deleteConfiguration(configuration);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Set the jdbcDrivers to two JDBC drivers with 
a semi-colon at the beginning of the list.
Verify that both drivers are registered.
**/
    public void Var023()
    {
        try {
            deregisterAllDrivers ();
            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=;com.ibm.as400.access.AS400JDBCDriver;test.ProxyServerJdbcDriversSampleDriver");
            int port = ++port_;
            ProxyServer.main (new String[] { "-configuration", configuration, "-port", Integer.toString (port), "-sp", "4421" });
            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            PSConfiguration.deleteConfiguration(configuration);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Set the jdbcDrivers to two JDBC drivers with 
a semi-colon at the end of the list.
Verify that both drivers are registered.
**/
    public void Var024()
    {
        try {
            deregisterAllDrivers ();
            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=com.ibm.as400.access.AS400JDBCDriver;test.ProxyServerJdbcDriversSampleDriver;");
            int port = ++port_;
            ProxyServer.main (new String[] { "-configuration", configuration, "-port", Integer.toString (port), "-sp", "4422" });
            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            PSConfiguration.deleteConfiguration(configuration);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Set the jdbcDrivers to two JDBC drivers with 
a space before the semi-colon.
Verify that both drivers are registered.
**/
    public void Var025()
    {
        try {
            deregisterAllDrivers ();
            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=com.ibm.as400.access.AS400JDBCDriver ;test.ProxyServerJdbcDriversSampleDriver");
            int port = ++port_;
            ProxyServer.main (new String[] { "-configuration", configuration, "-port", Integer.toString (port), "-sp", "4423" });
            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            PSConfiguration.deleteConfiguration(configuration);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Set the jdbcDrivers to two JDBC drivers with 
a space after the semi-colon.
Verify that both drivers are registered.
**/
    public void Var026()
    {
        try {
            deregisterAllDrivers ();
            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=com.ibm.as400.access.AS400JDBCDriver; test.ProxyServerJdbcDriversSampleDriver");
            int port = ++port_;
            ProxyServer.main (new String[] { "-configuration", configuration, "-port", Integer.toString (port), "-sp", "4424" });
            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            EndProxyServer.end (port); 
            PSConfiguration.deleteConfiguration(configuration);
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
jdbcDrivers= - Use it to reconfigure a running proxy server.
**/
    public void Var027()
    {
        try {
            deregisterAllDrivers ();
            ProxyServer ps = new ProxyServer();
            ps.setPort(9011);
            // ps.setSecurePort(9012);
            ps.start();

            String configuration = PSConfiguration.createConfiguration("jdbcDrivers=com.ibm.as400.access.AS400JDBCDriver;test.ProxyServerJdbcDriversSampleDriver");
            ProxyServer.main(new String[] { "-port", "9011", "-securePort", "9012", 
                                            "-c",  configuration });

            Enumeration<?> enumeration = DriverManager.getDrivers ();
            boolean check = (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("com.ibm.as400.access.AS400JDBCDriver"));
            check = check && (enumeration.hasMoreElements () == true);
            check = check && (enumeration.nextElement ().getClass ().getName ().equals ("test.ProxyServerJdbcDriversSampleDriver"));
            check = check && (enumeration.hasMoreElements () == false);
            ps.stop(); 
            assertCondition (check == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



}

