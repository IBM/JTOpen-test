///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSetupPackage.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 //////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDSetupPackage.java
 //
 // Classes:      JDSetupPackage
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;



/**
The JDSetupPackage class primes SQL packages for the JDBC test
drivers and testcases.
**/
public class JDSetupPackage
{



    /**
    Primes the package with a particular statement.  This ensures
    that the next connect with package caching on will cause the statement
    to be in the cache.
    **/
    static void prime (AS400 systemObject,
                              String packageName,
                              String packageLib,
                              String sql)
        throws SQLException
    {
        prime (systemObject, packageName, packageLib,
            sql, new Properties());
    }



    /**
    Primes the package with a particular statement.  This ensures
    that the next connect with package caching on will cause the statement
    to be in the cache.
    **/
    static void prime (AS400 systemObject,
                              String packageName,
                              String packageLib,
                              String sql,
                              Properties properties)
        throws SQLException
    {
 
        
        properties.put ("extended dynamic", "true");
        properties.put ("package", packageName);
        properties.put ("package library", packageLib);
        properties.put ("package cache", "false");
   
        AS400JDBCDriver driver = new AS400JDBCDriver(); 
        Connection c = driver.connect(systemObject, properties, packageLib);
        PreparedStatement ps = c.prepareStatement (sql);
        ps.close ();
        c.close ();
    }

    /**
    Primes the package with a particular statement.  This ensures
    that the next connect with package caching on will cause the statement
    to be in the cache.

    <p>This is a real bad work around to allow the Native driver to call the
    same function as above.
    **/
    static void prime (AS400 systemObject,
                              char[] encryptedPassword,
                              String packageName,
                              String packageLib,
                              String sql,
                              String urlProperties,
                              int    driver)
        throws SQLException
    {
        // SQL400 - make work with both drivers... 
        String url;
        if (driver == JDTestDriver.DRIVER_TOOLBOX)
        {
            url = "jdbc:as400://" + systemObject.getSystemName ()
                + ";" + urlProperties;
        }
        else  if (driver == JDTestDriver.DRIVER_NATIVE)
        {
            url = "jdbc:db2://" + systemObject.getSystemName ()
                + ";" + urlProperties;
        }
        else  if (driver == JDTestDriver.DRIVER_JTOPENLITE)
        {
            url = "jdbc:jtopenlite://" + systemObject.getSystemName ()
                + ";" + urlProperties;
        }
	else {
	    System.out.println("Warning.  Not priming package for driver "+driver); 
	    return; 
	} 

        Properties properties = new Properties ();
        properties.put ("user", systemObject.getUserId ());
        String password = "passwordLeak.JDSetupPackage"; 
        password = PasswordVault.decryptPasswordLeak(encryptedPassword); 
 
        properties.put ("password", password);
        properties.put ("extended dynamic", "true");
        properties.put ("package", packageName);
        properties.put ("package library", packageLib);
        properties.put ("package cache", "false");

        
        Connection c = DriverManager.getConnection (url, properties);
        PreparedStatement ps = c.prepareStatement (sql);
        ps.close ();
        c.close ();
    }


}


