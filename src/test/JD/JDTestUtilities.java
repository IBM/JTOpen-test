///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDTestUtilities.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.InputStream;
import java.io.OutputStream;
import com.ibm.as400.access.FTP;

import test.JTOpenTestEnvironment;
import test.TestDriver;

/**
This class contains common routines for the JDBC tests.
**/
public class JDTestUtilities
{
    public static final String PROCEDURE_1     = "STPTEST1";
    public static final String PROCEDURE_2     = "STPTEST2";
    public static final String PROCEDURE_3     = "STPTEST3";
    public static final String PROCEDURE_4     = "STPTEST4";
    public static final String PROCEDURE_5     = "STPTEST5";
    public static final String PROCEDURE_6     = "STPTEST6";
    public static final String TEST_LIBRARY    = "JDBCTEST";

    public static final String NLS_TEST_LIBRARY = "JAVANLS";
    public static final String NLS_PROCEDURE_1 =  "NLSPGM1";

    private static boolean[]    procedureInitialized_   = new boolean[8];

   public static char[] etoa = {
     0x00,0x01,0x02,0x03,0x1A,0x09,0x1A,0x7F,0x1A,0x1A,0x1A,0x0B,0x0C,0x0D,0x0E,0x0F,
     0x10,0x11,0x12,0x13,0x1A,0x1A,0x08,0x1A,0x18,0x19,0x1A,0x1A,0x1C,0x1D,0x1E,0x1F,
     0x1A,0x1A,0x1C,0x1A,0x1A,0x0A,0x17,0x1B,0x1A,0x1A,0x1A,0x1A,0x1A,0x05,0x06,0x07,
     0x1A,0x1A,0x16,0x1A,0x1A,0x1E,0x1A,0x04,0x1A,0x1A,0x1A,0x1A,0x14,0x15,0x1A,0x1A,
     0x20,0xA6,0xE1,0x80,0xEB,0x90,0x9F,0xE2,0xAB,0x8B,0x9B,0x2E,0x3C,0x28,0x2B,0x7C,
     0x26,0xA9,0xAA,0x9C,0xDB,0xA5,0x99,0xE3,0xA8,0x9E,0x21,0x24,0x2A,0x29,0x3B,0x5E,
     0x2D,0x2F,0xDF,0xDC,0x9A,0xDD,0xDE,0x98,0x9D,0xAC,0xBA,0x2C,0x25,0x5F,0x3E,0x3F,
     0xD7,0x88,0x94,0xB0,0xB1,0xB2,0xFC,0xD6,0xFB,0x60,0x3A,0x23,0x40,0x27,0x3D,0x22,
     0xF8,0x61,0x62,0x63,0x64,0x65,0x66,0x67,0x68,0x69,0x96,0xA4,0xF3,0xAF,0xAE,0xC5,
     0x8C,0x6A,0x6B,0x6C,0x6D,0x6E,0x6F,0x70,0x71,0x72,0x97,0x87,0xCE,0x93,0xF1,0xFE,
     0xC8,0x7E,0x73,0x74,0x75,0x76,0x77,0x78,0x79,0x7A,0xEF,0xC0,0xDA,0x5B,0xF2,0xF9,
     0xB5,0xB6,0xFD,0xB7,0xB8,0xB9,0xE6,0xBB,0xBC,0xBD,0x8D,0xD9,0xBF,0x5D,0xD8,0xC4,
     0x7B,0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0xCB,0xCA,0xBE,0xE8,0xEC,0xED,
     0x7D,0x4A,0x4B,0x4C,0x4D,0x4E,0x4F,0x50,0x51,0x52,0xA1,0xAD,0xF5,0xF4,0xA3,0x8F,
     /*           S    T    U    V    W    X    Y    Z    */
     0x5C,0xE7,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5A,0xA0,0x85,0x8E,0xE9,0xE4,0xD1,
     /* 0    1    2    3    4    5    6    7    8    9    */
     0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0xB3,0xF7,0xF0,0xFA,0xA7,0xFF,
     
   };


    /**
    Deletes and re-creates any necessary collections on the 400. Cleans up
    any leftover objects from previous testcase runs.
    **/
    public static void cleanup(AS400 systemObject,
                        String password,
                        String collection)
    {
      // get jdbc necessaries
      registerDriver();
      // String url = "jdbc:as400://" + systemObject.getSystemName ();
      Properties properties = new Properties ();
      properties.put ("user", systemObject.getUserId ());
      properties.put ("password", password);
      properties.put ("naming", "sql");
      // Connection c = null;
      // Statement s = null;
      try
      {
        // c = DriverManager.getConnection (url, properties);
        // s = c.createStatement();
      }
      catch(Exception e)
      {
        System.out.println("Exception occurred while getting connection/statement objects.");
        e.printStackTrace();
      }

      // kill the table
      dropCollection(systemObject, collection);

 

      System.out.println("Cleanup completed for "+collection+".");
    }



    /**
    Clears all rows from a table.
    **/
    public static void clearTable (AS400 systemObject,
                            String password,
                            String tableName,
                            String tableLib)
        throws SQLException
    {
        String url = "jdbc:as400://" + systemObject.getSystemName ();
        String qualifiedTableName = tableLib + "." + tableName;

        Properties properties = new Properties ();
        properties.put ("user", systemObject.getUserId ());
        properties.put ("password", password);
        properties.put ("naming", "sql");

        Connection c = DriverManager.getConnection (url, properties);
        Statement s = c.createStatement ();

        try {
            s.executeUpdate ("DELETE FROM " + qualifiedTableName);
        }
        catch (SQLException e) {
            // Ignore exception.  It just means the table
            // did not have any rows to clear.
        }
    }



    /**
    Creates a procedure.  If the procedure existed previously, it will
    be dropped and recreated.
    **/
    public static void createProcedure (AS400 systemObject,
                                 String password,
                                 String procName,
                                 String procLib,
                                 String procSpec)
        throws SQLException
    {
        String url = "jdbc:as400://" + systemObject.getSystemName ();
        String qualifiedProcName = procLib + "." + procName;

        Properties properties = new Properties ();
        properties.put ("user", systemObject.getUserId ());
        properties.put ("password", password);
        properties.put ("naming", "sql");

        Connection c = DriverManager.getConnection (url, properties);
        Statement s = c.createStatement ();

        try {
            s.executeUpdate ("DROP PROCEDURE " + qualifiedProcName);
        }
        catch (SQLException e) {
            // Ignore exception.  It just means the procedure
            // did not previously exist.
        }

        s.executeUpdate ("CREATE PROCEDURE " + qualifiedProcName + " " + procSpec);
        s.executeUpdate ("COMMENT ON PROCEDURE " + qualifiedProcName + " IS 'PROCEDURE REMARK'");
    }




    /**
    Creates one of the stored procedures needed for testing.
    **/
    public static void createProcedure (AS400 systemObject,
                                 String password,
                                 int procedureNumber)
        throws SQLException
    {
        // If we keep track of whats already been initialized
        // then we only need to do each one once.
        if (procedureInitialized_[procedureNumber] == true)
            return;
        procedureInitialized_[procedureNumber] = true;

        // Create the requested stored procedure.
        switch (procedureNumber) {

        case 1:
            createProcedure (systemObject, password, PROCEDURE_1,
                TEST_LIBRARY, "(IN P1 INTEGER, OUT P2 INTEGER, "
                + "INOUT P3 INTEGER) EXTERNAL NAME JDBCSTP.STPTEST1 "
                + "LANGUAGE PLI GENERAL WITH NULLS");
            break;

        case 2:
            createProcedure (systemObject, password, PROCEDURE_2,
                TEST_LIBRARY, " RESULT SET 1 EXTERNAL NAME JDBCSTP.STPTEST2 "
                + "LANGUAGE PLI GENERAL ") ;
            break;

        case 3:
            createProcedure (systemObject, password, PROCEDURE_3,
                TEST_LIBRARY, " RESULT SET 3 EXTERNAL NAME JDBCSTP.STPTEST3 "
                + "LANGUAGE PLI GENERAL ") ;
            break;

        case 4:
            // Note: NUMERIC is not allowed in a PLI program,
            // so we use DECIMAL in its place.
            createProcedure (systemObject, password, PROCEDURE_4,
                TEST_LIBRARY, "(OUT P1 SMALLINT, OUT P2 INTEGER, "
                + " OUT P3 REAL, OUT P4 FLOAT, OUT P5 DOUBLE, "
                + " OUT P6 DECIMAL(10,5), OUT P7 DECIMAL(10,5), "
                + " OUT P8 CHAR(20), OUT P9 VARCHAR(20), "
                + " OUT P10 CHAR(20) FOR BIT DATA, "
                + " OUT P11 VARCHAR(20) FOR BIT DATA, "
                + " OUT P12 DATE, OUT P13 TIME, OUT P14 TIMESTAMP) "
                + " EXTERNAL NAME JDBCSTP.STPTEST4 "
                + "LANGUAGE PLI SIMPLE CALL WITH NULLS");
            break;

        case 5:
            // Note: NUMERIC is not allowed in a PLI program,
            // so we use DECIMAL in its place.
            createProcedure (systemObject, password, PROCEDURE_5,
                TEST_LIBRARY, "(OUT P1 SMALLINT, OUT P2 INTEGER, "
                + " OUT P3 REAL, OUT P4 FLOAT, OUT P5 DOUBLE, "
                + " OUT P6 DECIMAL(10,5), OUT P7 DECIMAL(10,5), "
                + " OUT P8 CHAR(20), OUT P9 VARCHAR(20), "
                + " OUT P10 CHAR(20) FOR BIT DATA, "
                + " OUT P11 VARCHAR(20) FOR BIT DATA, "
                + " OUT P12 DATE, OUT P13 TIME, OUT P14 TIMESTAMP) "
                + " EXTERNAL NAME JDBCSTP.STPTEST5 "
                + "LANGUAGE PLI SIMPLE CALL WITH NULLS");
            break;

        case 6:
            createProcedure (systemObject, password, PROCEDURE_6,
                TEST_LIBRARY, "(OUT P1 VARCHAR(20), OUT P2 INTEGER) "
                + "EXTERNAL NAME JDBCSTP.STPTEST6 "
                + "LANGUAGE PLI SIMPLE CALL ");
                 // with nulls was dropped on this procedure to test
                 // isNullable on getProcedureColumns
            break;

        case 7:
            createProcedure (systemObject, password, NLS_PROCEDURE_1,
                NLS_TEST_LIBRARY, "(IN P1 CHAR(4), OUT P2 CHAR(4)) "
                + " EXTERNAL NAME JAVAPRIME.NLSPGM1 "
                + "GENERAL WITH NULLS ");
            break;

        }
    }




    /**
    Creates a table.  If the table existed previously, it will
    be dropped and recreated.
    **/
    public static void createTable (AS400 systemObject,
                             String password,
                             String tableName,
                             String tableLib,
                             String tableSpec)
        throws SQLException
    {
        String url = "jdbc:as400://" + systemObject.getSystemName ();
        String qualifiedTableName = tableLib + "." + tableName;

        Properties properties = new Properties ();
        properties.put ("user", systemObject.getUserId ());
        properties.put ("password", password);
        properties.put ("naming", "sql");

        Connection c = DriverManager.getConnection (url, properties);
        Statement s = c.createStatement ();

        try {
            s.executeUpdate ("DROP TABLE " + qualifiedTableName);
        }
        catch (SQLException e) {
            // Ignore exception.  It just means the table
            // did not previously exist.
        }

        s.executeUpdate ("CREATE TABLE " + qualifiedTableName + " " + tableSpec);
    }




    /**
    Drops the table and closes the connection (which should in turn close
    any open statements and result sets.
    **/
    public static void dropAndClose (Connection connection,
                              String tableName,
                              String tableLib)
        throws SQLException
    {
        if (connection == null) {
          System.out.println("JDTestUtilities.dropAndClose: " +
                             "Specified connection is null.");
          return;
        }
        Statement s = connection.createStatement ();

        String qualifiedTableName = tableLib + connection.getMetaData().getCatalogSeparator() + tableName;
        try {
            s.executeUpdate ("DROP TABLE " + qualifiedTableName);
        }
        catch (SQLException e) {
            // Ignore exception.  It just means the table
            // did not previously exist.
        }

        s.close ();
        connection.close ();
    }



    /**
    Deletes a collection on the 400 without having to use a screen to reply
    to the standard message that appears in the message queue.
    **/
    public static void dropCollection(AS400 systemObject,
                               String collection)
    {
      CommandCall cmd = new CommandCall(systemObject);
      try
      {
        // End journaling for all files in the collection to close the journal.
        System.out.println("  Ending journaling for collection "+collection+"...");
        cmd.setCommand("ENDJRNPF FILE(*ALL) JRN("+collection+"/QSQJRN)");
        cmd.run();
        cmd.setCommand("ENDJRNAP FILE(*ALL) JRN("+collection+"/QSQJRN)");
        cmd.run();

        // Delete the SQL Journal that gets created when a "CREATE COLLECTION" is done.
        System.out.println("  Deleting journal...");
        cmd.setCommand("DLTJRN "+collection+"/Q*");
        cmd.run();

        // Delete the temporary save file.
        // This prevents the notorious "...already contains data..." message from
        // appearing in the message queue.
        System.out.println("  Deleting pre-existing temporary save file...");
        cmd.setCommand("DLTF "+collection+"/TEMP");
        cmd.run();

        // Create a temporary save file into which the objects in the collection
        // will be saved.
        System.out.println("  Re-creating temporary save file...");
        cmd.setCommand("CRTSAVF "+collection+"/TEMP");
        cmd.run();

        // Save the objects to the save file.
        // This prevents the notorious "...not fully saved..." message from
        // appearing in the message queue.
        System.out.println("  Saving all objects in collection "+collection+"...");
        String str = "SAVOBJ OBJ(*ALL) LIB("+collection+") DEV(*SAVF) ";
        str += "SAVF("+collection+"/TEMP)";
        cmd.setCommand(str);
        cmd.run();

        // Delete the collection.
        System.out.println("  Deleting collection "+collection+"...");
	TestDriver.deleteLibrary(cmd,collection);

      }
      catch(Exception e)
      {
        System.out.println("Exception occurred while dropping collection object "+collection+".");
        e.printStackTrace();
      }
    }



    /**
    Takes a userid and grants authority for all objects currently existing
    in the specified library. Specify a user of *PUBLIC for all users.
    This will allow the currently running testcase to cleanup the leftover
    objects that were created during a previous run by a different user.
    **/
    public static void grantAuthority(AS400 systemObject,
                               String library,
                               String user)
    {
      if (user == null)
        user = systemObject.getUserId();

      CommandCall cmd = new CommandCall(systemObject);
      String str1 = "GRTOBJAUT OBJ("+library;
      String str2 = str1+"/*ALL";
      str1 += ") OBJTYPE(*ALL) USER("+user+") AUT(*ALL)";
      str2 += ") OBJTYPE(*ALL) USER("+user+") AUT(*ALL)";
      try
      {
        System.out.println("  Granting authority to "+user+" for library "+library+"...");
        cmd.setCommand(str1);
        cmd.run();
      }
      catch(Exception e)
      {
        System.out.println("Failed to grant library authority.");
      }
      try
      {
        System.out.println("  Granting authority to "+user+" for all objects in "+library+"...");
        cmd.setCommand(str2);
        cmd.run();
      }
      catch(Exception e)
      {
        System.out.println("Failed to grant object authorities.");
      }
    }



    /**
    Primes the package with a particular statement.  This ensures
    that the next connect with package caching on will cause the statement
    to be in the cache.
    **/
    public static void primePackage (AS400 systemObject,
                              String password,
                              String packageName,
                              String packageLib,
                              String statement)
        throws SQLException
    {
        primePackage (systemObject, password, packageName, packageLib,
            statement, "");
    }



    /**
    Primes the package with a particular statement.  This ensures
    that the next connect with package caching on will cause the statement
    to be in the cache.
    **/
    public static void primePackage (AS400 systemObject,
                              String password,
                              String packageName,
                              String packageLib,
                              String statement,
                              String urlProperties)
        throws SQLException
    {
        String url = "jdbc:as400://" + systemObject.getSystemName ()
            + ";" + urlProperties;

        Properties properties = new Properties ();
        properties.put ("user", systemObject.getUserId ());
        properties.put ("password", password);
        properties.put ("extended dynamic", "true");
        properties.put ("package", packageName);
        properties.put ("package library", packageLib);
        properties.put ("package cache", "false");

        Connection c = DriverManager.getConnection (url, properties);
        PreparedStatement ps = c.prepareStatement (statement);
        ps.close ();
        c.close ();
    }



    /**
    Reconnects the connection only if the properties are different.
    This helps minimize initializing new connections, and doing so
    only when necessary.

    @return     the new connection or null if reconnecting was
                not necessary.
    **/
    public static Connection reconnect (AS400 systemObject,
                                 String password,
                                 Connection connection,
                                 String currentProperties,
                                 String newProperties)
        throws SQLException
    {
        // Determine if a reconnect is necessary.
        boolean reconnectNecessary = false;
        if (connection == null)
            reconnectNecessary = true;
        else {
            reconnectNecessary = ((! currentProperties.equals (newProperties))
                || (connection.isClosed ()));
        }

        // Reconnect if needed.
        if (reconnectNecessary) {

            if (connection != null)
                connection.close ();

            String url = "jdbc:as400://" + systemObject.getSystemName () + ";" + newProperties;

            Properties properties = new Properties ();
            properties.put ("user", systemObject.getUserId ());
            properties.put ("password", password);

            return DriverManager.getConnection (url, properties);
        }

        return null;
    }



    /**
    Registers the appropriate JDBC driver(s) for testing.
    **/
    public static void registerDriver ()
    {
        // Register the AS/400 Toolbox for Java driver.
        try {
            DriverManager.registerDriver (new com.ibm.as400.access.AS400JDBCDriver ());
        }
        catch (SQLException e) {
        }

        // Register the native driver.
        //try {
        //    DriverManager.registerDriver (new com.ibm.as400.DB2Driver ());
        //}
        //catch(SQLException e) {
        //}
    }


    /**
    Automates the setup described in jdreadme.txt.
    The AS400password should be the password that correlates to the AS/400
    userid that's part of the systemObject.
    The AFSuserid should have read access to the family and component products
    at the cur level in CMVC.

    Currently, the JDSetupTest driver is used to call this method and setup
    JDBC on a particular 400. It can be called multiple times, if necessary;
    for example, to re-setup a system if the current setup was corrupted somehow.

    Note: Certain times I have run this method and it still hangs in the middle
    because it is waiting for a response from the user. If it does hang, sign on
    to the 400 and do a "DSPMSG QSYSOPR". Reply to the "never fully saved" message
    by typing an "I" and pressing enter. This should allow the setup to continue.

    **/
    public static void setupJDBC(AS400 systemObject,
                          String AS400password,
                          String rs6000,
                          String AFSuserid,
                          String AFSpassword)
    {
      CommandCall cmd = new CommandCall(systemObject);

      System.out.println("Cleaning up JDBCLIB...");
      cleanup(systemObject, AS400password, "JDBCLIB");
      System.out.println("Cleaning up JDBCTEST...");
      cleanup(systemObject, AS400password, "JDBCTEST");
      System.out.println("Cleaning up JDDMDLIB...");
      cleanup(systemObject, AS400password, "JDDMDLIB");
      System.out.println("Cleaning up JDDMDTST...");
      cleanup(systemObject, AS400password, "JDDMDTST");
      System.out.println("Cleaning up JDBCSTP...");
      cleanup(systemObject, AS400password, "JDBCSTP");

      System.out.println("Creating save files...");
      try
      {
        cmd.setCommand("CRTSAVF QGPL/JDBCSTP");
        cmd.run();
        cmd.setCommand("CRTSAVF QGPL/JDDMDLIB");
        cmd.run();
        cmd.setCommand("CRTSAVF QGPL/JDDMDTST");
        cmd.run();
      }
      catch(Exception e)
      {
        System.out.println("Exception occurred while creating save files.");
        e.printStackTrace();
        return;
      }

      System.out.println("Starting ftp...");
      try
      {
/*@B0D
        String port = null;
        FTPConnection rsftp = new FTPConnection();
        FTPConnection asftp = new FTPConnection();

        rsftp.openConnection(rs6000, AFSuserid, AFSpassword);
        rsftp.cwd("/as400/v3r2m0t.jacl/cur/cmvc/java.pgm/yjac.jacl/test");
        rsftp.setBinary();

        asftp.openConnection(systemObject.getSystemName(), systemObject.getUserId(), AS400password);
        asftp.cwd("QGPL");
        asftp.setBinary();

        port = asftp.receive("jdbcstp.savf");
        rsftp.send(port, "jdbcstp.savf");
        asftp.readResponse();
        asftp.readResponse();

        port = asftp.receive("jddmdlib.savf");
        rsftp.send(port, "jddmdlib.savf");
        asftp.readResponse();
        asftp.readResponse();

        port = asftp.receive("jddmdtst.savf");
        rsftp.send(port, "jddmdtst.savf");
        asftp.readResponse();
        asftp.readResponse();

        rsftp.sendCommand("quit");
        asftp.sendCommand("quit");
*/

      FTP aix = new FTP(rs6000, AFSuserid, AFSpassword);
      aix.cd("/as400/v5r2m0t.jacl/cur/cmvc/java.pgm/yjac.jacl/test");
      aix.setDataTransferType(FTP.BINARY);
      FTP os400 = new FTP(systemObject.getSystemName(), systemObject.getUserId(), AS400password);
      os400.cd("QGPL");
      os400.setDataTransferType(FTP.BINARY);

      InputStream is = aix.get("jdbcstp.savf");
      OutputStream os = os400.put("jdbcstp.savf");
      byte[] buf = new byte[4096];
      int i = is.read(buf);
      while (i != -1)
      {
        os.write(buf, 0, i);
        i = is.read(buf);
      }
      is.close();
      os.close();

      is = aix.get("jddmdlib.savf");
      os = os400.put("jddmdlib.savf");
      i = is.read(buf);
      while (i != -1)
      {
        os.write(buf, 0, i);
        i = is.read(buf);
      }
      is.close();
      os.close();

      is = aix.get("jddmdtst.savf");
      os = os400.put("jddmdtst.savf");
      i = is.read(buf);
      while (i != -1)
      {
        os.write(buf, 0, i);
        i = is.read(buf);
      }
      is.close();
      os.close();

      aix.disconnect();
      os400.disconnect();


      }
      catch(Exception e)
      {
        System.out.println("Exception occurred while ftp-ing save files.");
        e.printStackTrace();
        return;
      }

      cmd = new CommandCall(systemObject);
      System.out.println("Restoring objects and libraries...");

      try
      {
        String str1 = "RSTOBJ OBJ(*ALL) SAVLIB(JDBCSTP) DEV(*SAVF) OBJTYPE(*ALL) ";
        str1 += "SAVF(QGPL/JDBCSTP)";
        cmd.setCommand(str1);
        cmd.run();
        cmd.setCommand("RSTLIB SAVLIB(JDDMDLIB) DEV(*SAVF) SAVF(QGPL/JDDMDLIB)");
        cmd.run();
        cmd.setCommand("RSTLIB SAVLIB(JDDMDTST) DEV(*SAVF) SAVF(QGPL/JDDMDTST)");
        cmd.run();
      }
      catch(Exception e)
      {
        System.out.println("Exception occurred while restoring save files.");
        e.printStackTrace();
        return;
      }

      System.out.println("\n");
      System.out.println("Setup of JDBC on "+systemObject.getSystemName()+" is complete.");
    }


    public static void getCmdMessages(CommandCall cmd)
    {
      try
      {
          AS400Message[] messagelist = cmd.getMessageList();
          for (int i=0; i < messagelist.length; i++)
          {
            System.out.println( messagelist[i].getText() );
          }
      }
      catch(Exception e)
      {
        System.out.println("Exception while getting messages.");
        e.printStackTrace();
      }
    }


/**
dumpBytes() - Utility function used to see the bytes
**/
    public static String dumpBytes(byte[] b){
	StringBuffer s = new StringBuffer(); 
	for(int i = 0; i < b.length; i++){
	    String ns = Integer.toHexString(((int) b[i]) & 0xFF);
	    if(ns.length() == 1){
		s.append("0");
	    }
	    s.append(ns);
	}
	return s.toString();
    }


/**
dumpBytes() - Utility function used to see the bytes
**/
   public static String dumpBytesAsAscii(byte[] b){
       String s = "";
       for(int i = 0; i < b.length; i++){
	   char c = (char) b[i]; 
	   s += c + " ";

       }
       return s;
   }


/**
dumpBytes() - Utility function used to see the bytes
**/
   public static String dumpBytesAsEbcdic(byte[] b){
       String s = "";
       for(int i = 0; i < b.length; i++){
	   char c = (char) etoa[ 0xff & b[i]]; 
	   s += c + " ";

       }
       return s;
   }





   /**
   dumpBytes() - Utility function used to see the bytes
   **/
      public static String dumpBytes(String in){
        
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < in.length(); i++){
          String ns = Integer.toHexString(0xFFFF & in.charAt(i));
          for (int j = 0; j < 4 - ns.length(); j++) { 
            sb.append("0");  
          }
          sb.append(ns); 
        }
        return sb.toString();
      }


   
   
/*
 * isIasp -- returns true if the current connection is an IASP
 */
   public static boolean isIasp (Connection c) {
       int iaspNumber; 
       try {
           String catalog = c.getCatalog();
	   Statement stmt = c.createStatement();
	   ResultSet rs = stmt.executeQuery("SELECT CATALOG_ASPNUM FROM QSYS2.SYSCATALOGS WHERE CATALOG_NAME='"+catalog.toUpperCase()+"'");
	   rs.next();
	  
	   iaspNumber = rs.getInt(1);

	   stmt.close(); 

	   return (iaspNumber > 0); 
       } catch (Exception e) {
	   e.printStackTrace();
	   return false; 
       } 

   } 


   /**
 * getMixedString
 * returns a unicode string with UX'' syntax for unicode portions 
 */
   public static String getMixedString(String x) {
       StringBuffer sb = new StringBuffer();
       char[] chars = x.toCharArray();

	boolean inUX = false; 
	for (int i = 0; i < chars.length; i++) {
	    int showInt = chars[i] & 0xFFFF;

	    if (showInt==0x0a || showInt==0x09 || showInt >= 0x20 && showInt < 0x7F) {
		if (inUX) {
		    sb.append("'");
		    inUX = false; 
		} 
		sb.append(chars[i]); 
	    } else { 
		if (!inUX) {
		    inUX=true;
		    sb.append("UX'"); 
		} 

		String showString = Integer.toHexString(showInt);

		if (showInt >= 0x1000) {
		    sb.append(showString); 
		} else if (showInt >= 0x0100) {
		    sb.append("0"+showString);
		} else if (showInt >= 0x0010) {
		    sb.append("00"+showString);
		} else {
		    sb.append("000"+showString);
		}
	    }
	} /* for */
	if (inUX) {
	    sb.append("'");
	}
	return sb.toString(); 
    } 



   public static String stripXmlDeclaration(String s) {

      /* if(driver_ == JDTestDriver.DRIVER_TOOLBOX){
         //declaration starts with "<?xml " and ends with "?>"
           if(s.length() <= 6)  
               return s;      
           if(s.substring(0, 7).indexOf("<?xml") != -1) //avoid having to search whole 2 gig 
           {
               int end = s.indexOf("?>") + 2; //if start is xml, then it will have a valid ending since hostserver created it!
               if(end == 1)
                   return s;  //JDError.throwSQLException(JDError.EXC_XML_PARSING_ERROR); //signal that decl starts, but does not end
              
               //next skip to start of xml (ie skip newline)
               int nextStart = s.indexOf("<", end);
               if(nextStart == -1)
                   nextStart = end;
               
               return s.substring(nextStart);
           }
           else
               return s;
       }
       */
       //toolbox does same as native
       int i = 0;
       int len = s.length();
       while (i < len && ( s.charAt(i) == '\ufeff' || Character.isWhitespace(s.charAt(i)))) {
           i++;
       }
       if ((i+1)<len && s.charAt(i) == '<' && s.charAt(i + 1) == '?') {
           i += 2;
           while ((i+1) < len && (s.charAt(i) != '?' || s.charAt(i + 1) != '>')) {
               i++;
           }
           if ((i+1) < len && s.charAt(i) == '?' && s.charAt(i + 1) == '>') {
               return s.substring(i + 2);
           }

       }


       return s;



   } 


   public static void outputComparison(byte[] expected, byte[] actual, StringBuffer sb) {
       sb.append("Byte comparision\n");
       String expectedBytes = JDTestUtilities.dumpBytes(expected);
       String actualBytes   = JDTestUtilities.dumpBytes(actual);
       outputComparison(32,"",expectedBytes, actualBytes, sb);

       sb.append("Ascii comparision\n");
       String expectedAscii = JDTestUtilities.dumpBytesAsAscii(expected);
       String actualAscii   = JDTestUtilities.dumpBytesAsAscii(actual);
       outputComparison(15,"", expectedAscii, actualAscii, sb);

   }

   public static void outputComparison(int charsPerLine, String pad, String expected, String actual, StringBuffer sb) {
       int expectedLength = expected.length();
       int actualLength   = actual.length();
       int length = expectedLength;
       if (actualLength > length) {
	   length = actualLength; 
       } 
       for (int i = 0; i < length; i+= charsPerLine) {
	   sb.append("\nExpected ("+Integer.toHexString(i)+") :");
	   outputChars(expected, i, charsPerLine, pad, sb); 
	   sb.append("\nGot      ("+Integer.toHexString(i)+") :");
	   outputChars(actual,   i, charsPerLine, pad, sb); 
       }
       sb.append("\n"); 
   }

   public static void outputChars(String value, int startIndex, int charsPerLine, String pad, StringBuffer sb) {
       int valueLength = value.length();
       int padLength = pad.length(); 
       for (int i = 0; i < charsPerLine; i++) {
	   if (padLength > 0) sb.append(pad);	
	   if (i+startIndex < valueLength) {
	       sb.append(value.charAt(i+startIndex)); 
	   } else {
	       sb.append(" "); 
	   } 
       } 
   }

   /**
    * Get information about the local RDB. This is designed to be used
    * for informational (debugging) purposes
    
    */ 
   public static String getLocalRDBInformation(String RDB) {
       try { 
	   if (JTOpenTestEnvironment.isOS400) {
	       String returnString; 
	       Connection c = DriverManager.getConnection("jdbc:db2:localhost");
	       PreparedStatement ps = c.prepareStatement("select DBXRDBN,DBTXRLC,DBTPORT from  QSYS.QADBXRDBD WHERE DBXRDBN=?");
	       ps.setString(1,RDB.toUpperCase()); 
	       ResultSet rs = ps.executeQuery(); 
	       boolean found = rs.next();
	       if (found) {
		   returnString="RDB="+rs.getString(1)+":"+rs.getString(2)+":"+rs.getString(3);
	       } else {
		   returnString="Did not find RDB="+RDB; 
	       }

	       c.close();
	       return returnString; 
	   } else {
	       return "Unable to get RDBINFO for osName="+JTOpenTestEnvironment.osVersion; 
	   }
       } catch (Exception e) {
	   return "Caught exception "+e; 
       } 
       
   } 


}



