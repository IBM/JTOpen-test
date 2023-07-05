//////////////////////////////////////////////////////////////////////
//
//////////////////////////////////////////////////////////////////////
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

package test; 

import com.ibm.as400.access.AS400JDBCDriver;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;



public class ShowJDBCMRI
{


    public static void main (String[] args)
    {
        Driver driver = new AS400JDBCDriver ();

        // This code displays the translated descriptions
        // for each of the JDBC driver properties:
        DriverPropertyInfo[] dpi = null;
        try {
            dpi = driver.getPropertyInfo ("jdbc:as400://placeholder", new Properties());

            System.out.println("\nJDBC Properties");
            System.out.println(  "---------------");
        }
        catch (SQLException e) {
            e.printStackTrace ();
        }



        for (int i = 0; i < dpi.length; ++i)
        {
            System.out.print   ("\nEnglish:    ");

            switch (i)
            {
               case 0:
                  System.out.println ("Specifies the level of database access for the connection.");
                  break;

               case 1:
                  System.out.println ("Specifies the block size (number of records) to retrieve from the AS/400 server and cache on the client.");
                  break;

               case 2:
                  System.out.println ("Specifies the criteria for retrieving data from the AS/400 server in blocks of records.");
                  break;

               case 3:
                  System.out.println ("Specifies the date format used in date literals within SQL statements.");
                  break;

               case 4:
                  System.out.println ("Specifies the date separator used in date literals within SQL statements.");
                  break;

               case 5:
                  System.out.println ("Specifies the decimal separator used in numeric constants within SQL statements.");
                  break;

               case 6:
                  System.out.println ("Specifies the amount of detail to be returned in the message for errors that occur on the AS/400 server");
                  break;

               case 7:
                  System.out.println ("Specifies whether to use extended dynamic support.");
                  break;

               case 8:
                  System.out.println ("Specifies the AS/400 libraries to add to the server job's library list.");
                  break;

               case 9:
                  System.out.println ("Specifies the naming convention used when referring to tables.");
                  break;

               case 10:
                  System.out.println ("Specifies the name of the SQL package.");
                  break;

               case 11:
                  System.out.println ("Specifies whether to add statements to an existing SQL package.");
                  break;

               case 12:
                  System.out.println ("Specifies whether to cache SQL packages in memory.");
                  break;

               case 13:
                  System.out.println ("Specifies whether to clear SQL packages when they become full.");
                  break;

               case 14:
                  System.out.println ("Specifies the action to take when SQL packages occur.");
                  break;

               case 15:
                  System.out.println ("Specifies the library for the SQL package.");
                  break;

               case 16:
                  System.out.println ("Specifies the password for connecting to the AS/400 server.");
                  break;

               case 17:
                  System.out.println ("Specifies whether to prefetch data when running a SELECT statement.");
                  break;

               case 18:
                  System.out.println ("Specifies whether the user should be prompted if a user name or password is needed to connect to the AS/400 server.");
                  break;

               case 19:
                  System.out.println ("Specifies the source of the text for REMARKS columns in ResultSet objects returned by DatabaseMetaData methods.");
                  break;

               case 20:
                  System.out.println ("Specifies how the server sorts records before sending them to the client.");
                  break;

               case 21:
                  System.out.println ("Specifies a 3-character language id to use for selection of a sort sequence.");
                  break;

               case 22:
                  System.out.println ("Specifies the library and file name of a sort sequence table stored on the AS/400 server.");
                  break;

               case 23:
                  System.out.println ("Specifies how the server treats case while sorting records.");
                  break;

               case 24:
                  System.out.println ("Specifies the time format used in time literals within SQL statements.");
                  break;

               case 25:
                  System.out.println ("Specifies the time separator used in time literals within SQL statements.");
                  break;

               case 26:
                  System.out.println ("Specifies whether trace messages should be logged.");
                  break;

               case 27:
                  System.out.println ("Specifies the default transaction isolation.");
                  break;

               case 28:
                  System.out.println ("Specifies whether binary data is translated.");
                  break;

               case 29:
                  System.out.println ("Specifies the user name for connecting to the AS/400 server.");
                  break;

               case 30:
                  System.out.println (".");
                  break;

               case 31:
                  System.out.println (".");
                  break;

            }


            System.out.println ("Translated: " + dpi[i].description);
        }
    }




}
