///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDatabaseOverride.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDatabaseOverride.java
//
// Classes:      JDDatabaseOverride
//
////////////////////////////////////////////////////////////////////////

package test.JD.DataSource;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;



/**
Testcase JDDatabaseOverride.


**/
public class JDDatabaseOverride {

    /* Returns the database name from the system name */
    /* Uses the information from ini/databaseOverride.ini if needed */
    /* This is needed because some systems (notably mirror */
    /*  systems) do not have an RDB the matches the system name */

    private static Properties databaseOverride = null;

    public static String getDatabaseNameFromSystemName(String systemName, java.io.PrintWriter output_) throws UnknownHostException  {
    if (systemName.equals("localhost")) {
      systemName = getDatabaseNameFromSystemName(InetAddress.getLocalHost()
          .getHostName(), output_);

    }
    String databaseName = systemName.toUpperCase();
    int dotIndex = databaseName.indexOf(".");
    if (dotIndex > 0) {
      databaseName = databaseName.substring(0, dotIndex);
    }

    if (databaseOverride == null) {
      databaseOverride = new Properties();
      try { 
      FileInputStream inputStream = new FileInputStream("ini/databaseOverride.ini"); 
      databaseOverride.load(inputStream);
      } catch (Exception e) { 
        output_.println("Warning loading ini/databaseOverride.ini "); 
        e.printStackTrace(output_);
      }
    } 
    
    return databaseOverride.getProperty(databaseName, databaseName); 
    }

    




}
