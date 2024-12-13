///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCDataSourceSerialTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.AS400JDBC;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import java.io.PrintWriter;
import java.util.Vector;
import java.util.Properties;
import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;
import com.ibm.as400.access.ExtendedIllegalArgumentException;


import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;

/**
  Testcase AS400JDBCDataSourceSerialTestcase.
 **/
public class AS400JDBCDataSourceSerialTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "AS400JDBCDataSourceSerialTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.AS400JDBCDataSourceTest.main(newArgs); 
   }
    // Environment variables.
    private static final int OS_AS400 = 0;
    private static final int OS_WINDOWS = 1;
    private int environment_ = -1;

    // JNDI variables.
    private static final String JNDI_FILE = "com.sun.jndi.fscontext.RefFSContextFactory";
    private static final String JNDI_LDAP = "com.ibm.jndi.LDAPCtxFactory";

    private Context context_;
    private String jndiType_ = JNDI_FILE;
    private String ldapUsr_ = null;
    private String ldapPwd_ = null;
    private String systemNameLocal_ = null;

    // Logging variables.
    private PrintWriter writer_ = null;
    private File testFile_;
    private File javatest_;
    private static final String logDirectory_ = "javatest";
    private static final String logFileName_ = logDirectory_ + "/datasource.log";
    private PrintWriter previousPrintWriter_ = null;

    // Misc variables.
    private AS400JDBCDataSource dataSource_;
    private AS400JDBCDataSource dataSourceClone_;
    private static final String SERIAL_FILE_NAME = "datasource.ser";

    private StringBuffer logBuffer = new StringBuffer(); 
    /**
      Constructor.  This is called from the AS400JDBCDataSourceTest constructor.
     **/
    public AS400JDBCDataSourceSerialTestcase(AS400 systemObject,
                                       Vector variationsToRun,
                                       int runMode,
                                       FileOutputStream fileOutputStream,
                                       
                                       String password,
                                       String jndiType,
                                       String ldapUsr,
                                       String ldapPwd,
                                       String systemName)
    {
        super(systemObject, "AS400JDBCDataSourceSerialTestcase", variationsToRun,
              runMode, fileOutputStream, password);

        if (jndiType.equals("file"))
            jndiType_ = JNDI_FILE;
        else if (jndiType.equals("ldap"))
            jndiType_ = JNDI_LDAP;
        else
            System.out.println("WARNING... Unknown jndi type '" + jndiType + "' using default.");
        ldapUsr_ = ldapUsr;
        ldapPwd_ = ldapPwd;
        systemNameLocal_ = systemName;

        // See if there's a serialized object; if so, deserialize it and use it.
        File serFile = new File(SERIAL_FILE_NAME);
        if (serFile.exists()) {
          System.out.println("Deserializing the datasource object from file \"" + SERIAL_FILE_NAME + "\"");
          try {
            FileInputStream in = new FileInputStream(serFile);
            ObjectInputStream s2 = new ObjectInputStream(in);
            dataSource_ = (AS400JDBCDataSource)s2.readObject();
            try {
              if (!(dataSource_.isSavePasswordWhenSerialized()) &&
                  encryptedPassword_ != null) {
                 char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                dataSource_.setPassword(charPassword);
                 PasswordVault.clearPassword(charPassword);
              }
            }
            catch (NoSuchMethodError e) {}
            s2.close(); 
          }
          catch (Exception e) {
            System.out.println("ERROR: Failed to read file: " + SERIAL_FILE_NAME);
            e.printStackTrace();
          }
        }
        else {
          System.out.println("Serialized file \""+SERIAL_FILE_NAME+"\" not found.  Creating a new DataSource object.");
          dataSource_ = new AS400JDBCDataSource();
          dataSource_.setServerName(systemNameLocal_);
          dataSource_.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
          dataSource_.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
          try {
            dataSource_.setSavePasswordWhenSerialized(true);
          }
          catch (NoSuchMethodError e) {}
        }

        // Save away a clone for use when restoring to original state.
        dataSourceClone_ = cloneDataSource(dataSource_, encryptedPassword_);

    }


    // Clone a data source object.
    private static AS400JDBCDataSource cloneDataSource(AS400JDBCDataSource original, char[] encryptedPassword)
    {
      // Make a clone for restoring the state.
      AS400JDBCDataSource ds = new AS400JDBCDataSource();
      copyFromTo(original, ds, encryptedPassword); 
      return ds;
    }

    private static void copyFromTo(AS400JDBCDataSource original, AS400JDBCDataSource target, char[] encryptedPassword)
    {
      try
      {
        AS400JDBCDataSource ds = target;
        AS400JDBCDataSource ds0 = original;

        if (ds0.getLogWriter() == null) {
          System.out.println("WARNING: Original DataSource has null-valued LogWriter.");
        }
        else {
          ds.setLogWriter(ds0.getLogWriter());
        }

        ds.setServerName(ds0.getServerName());
        ds.setUser(ds0.getUser());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword);

        ds.setPassword(charPassword);
       PasswordVault.clearPassword(charPassword);

        ds.setAccess(ds0.getAccess());
        ds.setBigDecimal(ds0.isBigDecimal());
        ds.setBlockCriteria(ds0.getBlockCriteria());
        ds.setBlockSize(ds0.getBlockSize());
        ds.setCursorHold(ds0.isCursorHold());
        ds.setDatabaseName(ds0.getDatabaseName());
        ds.setDataCompression(ds0.isDataCompression());
        ds.setDataSourceName(ds0.getDataSourceName());
        ds.setDataTruncation(ds0.isDataTruncation());
        ds.setDateFormat(ds0.getDateFormat());
        ds.setDateSeparator(ds0.getDateSeparator());
        ds.setDecimalSeparator(ds0.getDecimalSeparator());
        ds.setDescription(ds0.getDescription());
        ds.setDriver(ds0.getDriver());
        ds.setErrors(ds0.getErrors());
        ds.setExtendedDynamic(ds0.isExtendedDynamic());
        ds.setLazyClose(ds0.isLazyClose());
        ds.setLibraries(ds0.getLibraries());
        ds.setLobThreshold(ds0.getLobThreshold());
        ds.setNaming(ds0.getNaming());
        ds.setPackage(ds0.getPackage());
        ds.setPackageAdd(ds0.isPackageAdd());
        ds.setPackageCache(ds0.isPackageCache());
        ds.setPackageCriteria(ds0.getPackageCriteria());
        ds.setPackageError(ds0.getPackageError());
        ds.setPackageLibrary(ds0.getPackageLibrary());
        ds.setPrefetch(ds0.isPrefetch());
        ds.setPrompt(ds0.isPrompt());
        ds.setProxyServer(ds0.getProxyServer());
        ds.setRemarks(ds0.getRemarks());
        try {
          ds.setSavePasswordWhenSerialized(ds0.isSavePasswordWhenSerialized());
        }
        catch (NoSuchMethodError e) {}
        ds.setSecondaryUrl(ds0.getSecondaryUrl());
        ds.setSecure(ds0.isSecure());
        ds.setSort(ds0.getSort());
        ds.setSortLanguage(ds0.getSortLanguage());
        ds.setSortTable(ds0.getSortTable());
        ds.setSortWeight(ds0.getSortWeight());
        ds.setThreadUsed(ds0.isThreadUsed());
        ds.setTimeFormat(ds0.getTimeFormat());
        ds.setTimeSeparator(ds0.getTimeSeparator());
        ds.setTrace(ds0.isTrace());
        ds.setTransactionIsolation(ds0.getTransactionIsolation());
        ds.setTranslateBinary(ds0.isTranslateBinary());
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }


    /**
    *  Checks the log for the specified String.
    *  @param test The compare String.
    *  @return true if the compare string exists in the log; false otherwise.
    **/
    public boolean checkLog(String test)
    {
        boolean exists = false;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(testFile_));
	    logBuffer.setLength(0);  
            String line = reader.readLine();
            while (line != null)
            {
		logBuffer.append(line); 
                if (line.indexOf(test) != -1)
                {
                    exists = true;
                    break;
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch (Exception e)
        {
            System.out.println("Log check failed.");
            e.printStackTrace();
        }

        return exists;
    }

    /**
    *  Cleanup the resources created by the testcase.
     @exception  Exception  If an exception occurs.
    **/
    public void cleanup() throws Exception
    {

        writer_.close();

        // Serialize the datasource object.
        System.out.println("Serializing the datasource object to file \"" + SERIAL_FILE_NAME + "\"");
        File file = new File(SERIAL_FILE_NAME);
        if (file.exists()) file.delete();
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutput s  =  new ObjectOutputStream(f);
        s.writeObject(dataSource_);
        s.flush();
        s.close(); 
        File cleanupFile = new File("log.out");
        if (!cleanupFile.delete())
            System.out.println("WARNING... \"log.out\" could not be deleted.");

        if (!testFile_.delete())
            System.out.println("WARNING... \"" + logFileName_ + "\" could not be deleted.");
        if (!javatest_.delete())
            System.out.println("WARNING... testcase cleanup could not delete: " + logDirectory_);
    }

    /**
      Performs setup needed before running testcases.
     @exception  Exception  If an exception occurs.
     **/
    public void setup() throws Exception
    {
        if (!isApplet_)
        {
            // Make sure javatest dir exists.
            javatest_ = new File(logDirectory_);
            if (!javatest_.exists())
            {
                System.out.println("Setup is creating 'javatest' directory.");

                if (!javatest_.mkdir())
                {
                    System.out.println("WARNING:  Setup could not create the 'javatest' directory.");
                }
            }

            // Make sure the log file exists.
            testFile_ = new File(logFileName_);
            try
            {
                testFile_.createNewFile();
            }
            catch (Exception e)
            {
                System.out.println("WARNING... testcase setup could not create the log file: " + logFileName_);
                e.printStackTrace();
            }

            writer_ = new PrintWriter(new FileWriter(testFile_));
        }

        // Determine the environment.
        String os = System.getProperty("os.name");
        System.out.println("Environment: " + os);

        if (os.indexOf("OS/400") >= 0)
            environment_ = OS_AS400;
        else if (JTOpenTestEnvironment.isWindows)
            environment_ = OS_WINDOWS;

        if (!isApplet_ || (isApplet_ && jndiType_ != JNDI_FILE))
        {
            // Get the JNDI Initial Context.
            Properties env = new Properties();

            if (jndiType_ == JNDI_LDAP)
            {
                env.put("java.naming.provider.url", "ldap://" + systemNameLocal_);
                env.put("java.naming.security.principal", "cn=" + ldapUsr_);
                env.put("java.naming.security.credentials", ldapPwd_);
            }

            env.put(Context.INITIAL_CONTEXT_FACTORY, jndiType_);
            context_ = new InitialContext(env);
        }
    }

    /**
       Validates that getConnection() returns a valid connection object.

       AS400JDBCDataSource()
       setServerName()
       setUser()
       setPassword()
       getConnection()
    **/
    public void Var001()
    {
        Connection c = null;
        try
        {
            AS400JDBCDataSource ds = dataSource_;

            c = ds.getConnection();

            assertCondition(c != null && !c.isClosed());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                if (c != null)
                    c.close();
            }
            catch (SQLException sq)
            {
            }
        }
    }

    /**
       Validates that getConnection() returns a valid connection object.

       AS400JDBCDataSource(String,String,String)
       getConnection()
    **/
    public void Var002()
    {
        Connection c = null;
        try
        {
            AS400JDBCDataSource ds = dataSource_;

            c = ds.getConnection();

            assertCondition(c != null && !c.isClosed());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                if (c != null)
                    c.close();
            }
            catch (SQLException sq)
            {
            }
        }
    }

    /**
       Validates that getConnection(String,String) returns a valid connection object.

       AS400JDBCDataSource()
       setServerName()
       getConnection(String,String)
    **/
    public void Var003()
    {
        Connection c = null;
        try
        {
            AS400JDBCDataSource ds = dataSource_;

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            assertCondition(c != null && !c.isClosed());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                if (c != null)
                    c.close();
            }
            catch (SQLException sq)
            {
            }
        }
    }

    /**
       Validates that getConnection(String,String) returns a valid connection object.

       AS400JDBCDataSource(String,String,String)
       setServerName()
       getConnection(String,String)
    **/
    public void Var004()
    {
        Connection c = null;
        try
        {
            AS400JDBCDataSource ds = dataSource_;

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            assertCondition(c != null && !c.isClosed());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                if (c != null)
                    c.close();
            }
            catch (SQLException sq)
            {
            }
        }
    }


    /**
       Validates that a second getConnection() returns a valid connection object.

       AS400JDBCDataSource(String,String,String)
       getConnection()
    **/
    public void Var005()
    {
        Connection c = null, c2 = null;
        try
        {
            AS400JDBCDataSource ds = dataSource_;
            c = ds.getConnection();
            c2 = ds.getConnection();

            assertCondition (c != null && c2 != null && c != c2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                if (c != null)
                    c.close();
            }
            catch (SQLException sq)
            {
            }
            try
            {
                if (c2 != null)
                    c2.close();
            }
            catch (SQLException sq)
            {
            }
        }
    }

    /**
       Validates that a second getConnection(String,String) returns a valid connection object.

       AS400JDBCDataSource()
       setServerName()
       getConnection(String,String)
    **/
    public void Var006()
    {
        Connection c = null, c2 = null;
        try
        {
            AS400JDBCDataSource ds = dataSource_;

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            c = ds.getConnection(systemObject_.getUserId(), charPassword);
            c2 = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            assertCondition(c != null && c2 != null && c != c2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                if (c != null)
                    c.close();
            }
            catch (SQLException sq)
            {
            }
            try
            {
                if (c2 != null)
                    c2.close();
            }
            catch (SQLException sq)
            {
            }
        }
    }

    /**
       Validates that a second getConnection(String,String) returns a valid connection object.

       AS400JDBCDataSource(String,String,String)
       getConnection()
    **/
    public void Var007()
    {
        Connection c = null, c2 = null;
        try
        {
            AS400JDBCDataSource ds = dataSource_;

            c= ds.getConnection();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            c2 = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            assertCondition (c != null && c2 != null && c != c2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                if (c != null)
                    c.close();
            }
            catch (SQLException sq)
            {
            }
            try
            {
                if (c2 != null)
                    c2.close();
            }
            catch (SQLException sq)
            {
            }
        }
    }

    /**
       Validates that a second getConnection() returns a valid connection object.

       AS400JDBCDataSource()
       setServerName()
       getConnection(String,String)
    **/
    public void Var008()
    {
        Connection c = null, c2 = null;
        try
        {
            AS400JDBCDataSource ds = dataSource_;

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            c = ds.getConnection(systemObject_.getUserId(), charPassword);

            ds.setUser(systemObject_.getUserId());
            ds.setPassword(charPassword);
            c2 = ds.getConnection();
   PasswordVault.clearPassword(charPassword);

            assertCondition(c != null && c2 != null && c != c2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                if (c != null)
                    c.close();
            }
            catch (SQLException sq)
            {
            }
            try
            {
                if (c2 != null)
                    c2.close();
            }
            catch (SQLException sq)
            {
            }
        }
    }

    /**
       Validates that getConnection() prompts for a system name.
       Attended testcase.
       AS400JDBCDataSource()
       setUser()
       setPassword()
       getConnection()
    **/
    public void Var009(int runMode)
    {
	if (runMode != BOTH && runMode != ATTENDED)  {
	    notApplicable("ATTENDED TEST"); 
	    return;
	} 

        try
        {
            AS400JDBCDataSource ds = dataSource_;

            Connection c= ds.getConnection();

            succeeded();
            c.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
       Validates that getConnection() prompts for a user.
       Attended testcase.
       AS400JDBCDataSource()
       setServerName()
       setPassword()
       getConnection()
    **/
    public void Var010(int runMode)
    {

	if (runMode != BOTH && runMode != ATTENDED)  {
	    notApplicable("ATTENDED TEST"); 
	    return;
	} 
        try
        {
            AS400JDBCDataSource ds = dataSource_;

            Connection c= ds.getConnection();

            succeeded();
            c.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
       Validates that getConnection() prompts for a password
       Attended testcase.
       AS400JDBCDataSource()
       setServerName()
       setUser()
       getConnection()
    **/
    public void Var011(int runMode)
    {
	if (runMode != BOTH && runMode != ATTENDED)  {
	    notApplicable("ATTENDED TEST"); 
	    return;
	} 
        try
        {
            AS400JDBCDataSource ds = dataSource_;

            Connection c= ds.getConnection();

            assertCondition(true, "Connect is "+c); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
       Validates that getConnection() prompts for a user and password.
       Attended testcase.
       AS400JDBCDataSource()
       setServerName()
       setPassword()
       getConnection()
    **/
    public void Var012(int runMode)
    {
	if (runMode != BOTH && runMode != ATTENDED)  {
	    notApplicable("ATTENDED TEST"); 
	    return;
	} 

        try
        {
            AS400JDBCDataSource ds = dataSource_;

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            Connection c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Connection c2 = ds.getConnection();


            assertCondition(true, "c="+c+" c2="+c2); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getAccess() returns the correct default value.
    **/
    public void Var013()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.getAccess().equalsIgnoreCase("all"))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setAccess(String) sets the database access level.
    **/
    public void Var014()
    {
        String[] values = {"all", "read call", "read only"};
        String accessOld = null;
        try
        {
            accessOld = dataSource_.getAccess();
            AS400JDBCDataSource ds = dataSource_;

            boolean failed = false;
            for (int i=0; i< values.length; i++)
            {
                ds.setAccess(values[i]);
                if (!ds.getAccess().equals(values[i]))
                    failed = true;
            }

            if (!failed)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally {
          if (accessOld != null) {
            try { dataSource_.setAccess(accessOld); } catch (Exception e) {}
          }
        }
    }

    /**
    *  Validates that setAccess(String) throws an ExtendedIllegalArgumentException if the property value is invalid.
    **/
    public void Var015()
    {
        try
        {
            AS400JDBCDataSource ds = dataSource_;

            ds.setAccess("BADVALID");
            failed("Unexpected Results.");
        }
        catch (ExtendedIllegalArgumentException l)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that isBigDecimal() returns the correct default value.
    **/
    public void Var016()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.isBigDecimal())
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setBigDecimal(String) correctly set whether a big decimal value is used.
    **/
    public void Var017()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            ds.setBigDecimal(false);

            if (!ds.isBigDecimal())
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getBlockCriteria() returns the correct default value.
    **/
    public void Var018()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.getBlockCriteria() == 2)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setBlockCriteria(int) sets the criteria for retrieving data in blocks of records.
    **/
    public void Var019()
    {
        int[] value = { 0, 1, 2};
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i<value.length; i++)
            {
                ds.setBlockCriteria(value[i]);
                if (! (ds.getBlockCriteria() == value[i]))
                    failed = true;
            }

            if (!failed)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setBlockCriteria(int) throws an ExtendedIllegalArgumentException if the value is invalid.
    **/
    public void Var020()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setBlockCriteria(-1);

            failed("Unexpected results");
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
    /**
    *  Validates that getBlockSize() returns the correct default value.
    **/
    public void Var021()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.getBlockSize() == 32)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setBlockSize(int) sets the block size as expected.
    **/
    public void Var022()
    {
        int[] values = {0,8,16,32,64,128,256,512};
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< values.length; i++)
            {
                ds.setBlockSize(values[i]);
                if (! (ds.getBlockSize() == values[i]) )
                {
                    failed = true;
                }
            }

            if (failed)
                failed("Unexpected Results.");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setBlockSize(int) throws an ExtendedIllegalArgumentException if the value is invalid.
    **/
    public void Var023()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setBlockSize(100);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getDatabaseName() returns the correct default value.
    **/
    public void Var024()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.getDatabaseName().equals(""))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setDatabaseName(String) sets the database name as expected.
    **/
    public void Var025()
    {
        String value = "myDatabase";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setDatabaseName(value);

            if (ds.getDatabaseName().equals(value))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that isDataCompression() returns the correct default value.
    **/
    public void Var026()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.isDataCompression() == true)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setDataCompression(boolean) sets the value as expected.
    **/
    public void Var027()
    {
        boolean value = false;
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setDataCompression(value);

            if (ds.isDataCompression() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getDataSourceName() returns the correct default value.
    **/
    public void Var028()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.getDataSourceName().equals(""))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setDataSourceName(String) sets the database name as expected.
    **/
    public void Var029()
    {
        String value = "myDataSource";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setDataSourceName(value);

            if (ds.getDataSourceName().equals(value))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that isDataTruncation() returns the correct default value.
    **/
    public void Var030()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.isDataTruncation() == true)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setDataTruncation(boolean) sets the value as expected.
    **/
    public void Var031()
    {
        boolean value = true;
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setDataTruncation(value);

            if (ds.isDataTruncation() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that setDateFormat(String) sets the date format as expected.
    **/
    public void Var032()
    {
        String[] values = {"mdy", "dmy", "ymd", "usa", "iso", "eur", "jis", "julian"};
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< values.length; i++)
            {
                ds.setDateFormat(values[i]);
                if (!ds.getDateFormat().equals(values[i]))
                {
                    failed = true;
                }
            }

            if (failed)
                failed("Unexpected Results.");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setDateFormat(String) throws an ExtendedIllegalArgumentException if the value is invalid.
    **/
    public void Var033()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setDateFormat("u");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that setDateSeparator(String) sets the date separator as expected.
    **/
    public void Var034()
    {
        String[] values = { "/", "-", ".", ",", "b"};
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< values.length; i++)
            {
                ds.setDateSeparator(values[i]);
                if (!ds.getDateSeparator().equals(values[i]))
                {
                    failed = true;
                    break;
                }
            }

            if (failed)
                failed("Unexpected Results. " + ds.getDateSeparator());
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setDateSeparator(String) throws an ExtendedIllegalArgumentException for an invalid value.
    **/
    public void Var035()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setDateSeparator("X");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Wrong exception info.");
        }
    }

    /**
    *  Validates that setDecimalSeparator(String) sets the decimal separator as expected.
    **/
    public void Var036()
    {
        String[] values = { ",", "."};
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< values.length; i++)
            {
                ds.setDecimalSeparator(values[i]);
                if (!ds.getDecimalSeparator().equals(values[i]))
                {
                    failed = true;
                }
            }

            if (failed)
                failed("Unexpected Results.");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setDecimalSeparator(String) throws an ExtendedIllegalArgumentException if the value is invalid.
    **/
    public void Var037()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setDecimalSeparator("X");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that getDescription() returns the correct default value.
    **/
    public void Var038()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.getDescription().equals(""))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setDescription(String) sets the database name as expected.
    **/
    public void Var039()
    {
        String value = "myDescription";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setDescription(value);

            if (ds.getDescription().equals(value))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getDriver() returns the correct default value.
    **/
    public void Var040()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            assertCondition(ds.getDriver().equals("toolbox"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setDriver(String) sets the driver as expected.
    **/
    public void Var041()
    {
        String value = "toolbox";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setDriver(value);

            if (ds.getDriver().equals(value))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getErrors() returns the correct default value.
    **/
    public void Var042()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.getErrors().equalsIgnoreCase("basic"))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setErrors(String) sets the error detail amount as expected.
    **/
    public void Var043()
    {
        String[] values = { "full", "basic"};
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< values.length; i++)
            {
                ds.setErrors(values[i]);
                if (!ds.getErrors().equals(values[i]))
                {
                    failed = true;
                }
            }

            if (failed)
                failed("Unexpected Results.");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setErrors(String) throws an ExtendedIllegalArgumentException if the value is invalid.
    **/
    public void Var044()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setErrors("XXXX");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that isExtendedDynamic() returns the correct default value.
    **/
    public void Var045()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.isExtendedDynamic() == false)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setExtendedDynamic(boolean) sets the value as expected.
    **/
    public void Var046()
    {
        boolean value = true;
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setExtendedDynamic(value);

            if (ds.isExtendedDynamic() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that isLazyClose() returns the correct default value.
    **/
    public void Var047()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.isLazyClose() == false)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setLazyClose(boolean) sets the value as expected.
    **/
    public void Var048()
    {
        boolean value = false;
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setLazyClose(value);

            if (ds.isLazyClose() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getLibraries() returns the correct default value.
    **/
    public void Var049()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.getLibraries().equals(""))
                succeeded();
            else
                failed("Unexpected Results. " + ds.getLibraries());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setLibraries(String) sets the database name as expected.
    **/
    public void Var050()
    {
        String value = "myLibraries";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setLibraries(value);

            if (ds.getLibraries().equals(value))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getLobThreshold() returns the correct default value.
    **/
    public void Var051()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.getLobThreshold() == 32768)
                succeeded();
            else
                failed("Unexpected Results. " + ds.getLobThreshold());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setLobThreshold(int) sets the lob threshold as expected.
    **/
    public void Var052()
    {
        int value = 256000;

        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setLobThreshold(value);

            if (ds.getLobThreshold() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setLobThreshold(int) throws an exception.
    **/
    public void Var053()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setLobThreshold(-1);

            failed("Unexpected Results. " + ds.getLobThreshold());
        }
        catch (ExtendedIllegalArgumentException a)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Wrong exception info.");
        }
    }

    /**
    *  Validates that setLobThreshold(int) throws an exception.
    **/
    public void Var054()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setLobThreshold(16777217);

            failed("Unexpected Results.");
        }
        catch (ExtendedIllegalArgumentException a)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Wrong exception info.");
        }
    }

    /**
    *  Validates that getLoginTimeout() throws an SQLException - Function not supported.
    **/
    public void Var055()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            assertCondition(ds.getLoginTimeout() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getLogWriter() returns null.
    **/
    public void Var056()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            assertCondition(ds.getLogWriter() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getNaming() returns the correct default value.
    **/
    public void Var057()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.getNaming().equalsIgnoreCase("sql"))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setNaming(String) sets the SQL package base name as expected.
    **/
    public void Var058()
    {
        String[] values = { "system", "sql"};
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< values.length; i++)
            {
                ds.setNaming(values[i]);
                if (!ds.getNaming().equals(values[i]))
                {
                    failed = true;
                }
            }

            if (failed)
                failed("Unexpected Results.");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setNaming(String) throws an ExtendedIllegalArgumentException if the value is invalid.
    **/
    public void Var059()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setNaming("XXXX");

            failed("Unexpected Results. " + ds.getNaming());
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Exception Exception info.");
        }
    }


    /**
    *  Validates that getPackage() returns the correct default value.
    **/
    public void Var060()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.getPackage().equals(""))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setPackage(String) sets the SQL package name as expected.
    **/
    public void Var061()
    {
        String value = "myPackage";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setPackage(value);

            if (ds.getPackage().equals(value))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that isPackageAdd() returns the correct default value.
    **/
    public void Var062()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.isPackageAdd() == true)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setPackageAdd(boolean) sets the value as expected.
    **/
    public void Var063()
    {
        boolean value = false;
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setPackageAdd(value);

            if (ds.isPackageAdd() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that isPackageCache() returns the correct default value.
    **/
    public void Var064()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.isPackageCache() == false)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setPackageCache(boolean) sets the value as expected.
    **/
    public void Var065()
    {
        boolean value = true;
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setPackageCache(value);

            if (ds.isPackageCache() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that isPackageClear() returns the correct default value.
    **/
    public void Var066()
    {
      assertCondition(true, "ignore deprecated interfaces"); 
    }

    /**
    *  Validates that setPackageClear(boolean) sets the value as expected.
    **/
    public void Var067()
    {
	notApplicable("setPackageClear has been deprecated");
    }
    /**
    *  Validates that getPackageCriteria() returns the correct default value.
    **/
    public void Var068()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.getPackageCriteria().equals("default"))
                succeeded(   );
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setPackageCriteria(String) sets the expected value.
    **/
    public void Var069()
    {
        String[] value = { "select", "default"};
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< value.length; i++)
            {
                ds.setPackageCriteria(value[i]);

                if (!ds.getPackageCriteria().equals(value[i]))
                {
                    failed = true;
                    break;
                }
            }

            if (!failed)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }
    /**
    *  Validates that setPackageCriteria(String) throws an ExtendedIllegalArgumentException if the value is invalid.
    **/
    public void Var070()
    {
        String value = "myCriteria";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setPackageCriteria(value);

            failed("Unexpected Results. " + ds.getPackageCriteria());
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getPackageError() returns the correct default value.
    **/
    public void Var071()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.getPackageError().equals("warning"))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setPackageError(String) sets the expected value.
    **/
    public void Var072()
    {
        String[] value = { "exception", "none", "warning"};

        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;

            for (int i=0; i< value.length; i++)
            {
                ds.setPackageError(value[i]);

                if (!ds.getPackageError().equals(value[i]) )
                {
                    failed = true;
                    break;
                }
            }

            if (!failed)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setPackageError(String) throws an ExtendedIllegalArgumentException if the value is invalid.
    **/
    public void Var073()
    {
        String value = "myErrors";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setPackageError(value);

            failed("Unexpected Results. " + ds.getPackageError());
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that getPackageLibrary() returns the correct default value.
    **/
    public void Var074()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.getPackageLibrary().equals(""))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setPackageLibrary(String) sets the value as expected.
    **/
    public void Var075()
    {
        String value = "myLibrary";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setPackageLibrary(value);

            if (ds.getPackageLibrary() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that isPrefetch() returns the correct default value.
    **/
    public void Var076()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.isPrefetch() == true)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setPrefetch(boolean) sets the value as expected.
    **/
    public void Var077()
    {
        boolean value = false;
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setPrefetch(value);

            if (ds.isPrefetch() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that isPrompt() returns the correct default value.
    **/
    public void Var078()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (!ds.isPrompt())
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setPrompt(boolean) sets the value as expected.
    **/
    public void Var079()
    {
        boolean value = false;
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setPrompt(value);

            if (ds.isPrompt() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getProxyServer() returns the correct default value.
    **/
    public void Var080()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.getProxyServer().equals(""))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setProxyServer(String) sets the proxy server as expected.
    **/
    public void Var081()
    {
        String value = "myProxy";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setProxyServer(value);

            if (ds.getProxyServer().equals(value))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that getRemarks() returns the correct default value.
    **/
    public void Var082()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.getRemarks().equals("system"))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that setRemarks(String) sets the remarks as expected.
    **/
    public void Var083()
    {
        String[] values = { "system", "sql"};
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< values.length; i++)
            {
                ds.setRemarks(values[i]);
                if (!ds.getRemarks().equals(values[i]))
                {
                    failed = true;
                }
            }

            if (failed)
                failed("Unexpected Results.");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setRemarks(String) throws an ExtendedIllegalArgumentException if the value is invalid.
    **/
    public void Var084()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setRemarks("XXXX");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that getSecondaryUrl() returns the correct default value.
    **/
    public void Var085()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.getSecondaryUrl().equals(""))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setSecondaryUrl(String) sets the secondary url as expected.
    **/
    public void Var086()
    {
        String value = "http://www.myUrl.com";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setSecondaryUrl(value);

            if (ds.getSecondaryUrl().equals(value))
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that isSecure() returns the correct default value.
    **/
    public void Var087()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            if (ds.isSecure() == false)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setSecure(boolean) sets the value as expected.
    **/
    public void Var088()
    {
        boolean value = true;
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setSecure(value);

            if (ds.isSecure() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getServerName() returns the correct default value.
    **/
    public void Var089()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            String defaultValue = "";
            if (environment_ == OS_AS400)
                defaultValue = "localhost";

            if (ds.getServerName().equals(defaultValue))
                succeeded();
            else
                failed("Unexpected Results. " + ds.getServerName());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setServerName(String) sets the expected server name.
    **/
    public void Var090()
    {
        String value = "myAS00";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setServerName(value);

            if (ds.getServerName() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that getSort() returns the correct default value.
    *  As of 9/28/2010 the default is "hex" because "job" no longer
    *  exists. 
    **/
    public void Var091()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.getSort() == "hex")
                succeeded();
            else
                failed("Unexpected Results. Got ds.getSort()="+ds.getSort()+" expected job");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setSort(String) sets the expected server name.
    **/
    public void Var092()
    {
	StringBuffer sb = new StringBuffer(); 
        String[] value = { "hex", "language", "table"};
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< value.length; i++)
            {
                ds.setSort(value[i]);

		if (!ds.getSort().equals(value[i])) {
		    sb.append("ds.getSort()="+ds.getSort()+ " sb "+value[i]); 
		    failed = true;
		}
            }

            if (!failed)
                succeeded();
            else
                failed("Unexpected Results."+sb.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that setSortLanguage(String) sets the expected sort language.
    **/
    public void Var093()
    {
        String value = "usa";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setServerName(value);

            if (ds.getServerName() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getSortTable() returns the correct default value.
    **/
    public void Var094()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.getSortTable().equals(""))
                succeeded();
            else
                failed("Unexpected Results. " + ds.getSortTable());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setSortTable(String) sets the expected sort table.
    **/
    public void Var095()
    {
        String value = "myTable";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setSortTable(value);

            if (ds.getSortTable() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that getSortWeight() returns the correct default value.
    **/
    public void Var096()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.getSortWeight() == "shared")
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setSortWeight(String) sets the expected sort weight.
    **/
    public void Var097()
    {
        String[] value = { "unique", "shared"};
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< value.length; i++)
            {
                ds.setSortWeight(value[i]);

                if (!ds.getSortWeight().equals(value[i]))
                    failed = true;
            }

            if (!failed)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setSortWeight(String) throws an UnexpectedIllegalArgumentException if the value is invalid.
    **/
    public void Var098()
    {
        String value = "myWeight";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setSortWeight(value);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that isThreadUsed() returns the correct default value.
    **/
    public void Var099()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.isThreadUsed())
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setThreadUsed(boolean) correctly set whether a thread is used.
    **/
    public void Var100()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            ds.setThreadUsed(false);

            if (!ds.isThreadUsed())
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setTimeFormat(String) sets the time format as expected.
    **/
    public void Var101()
    {
        String[] value = { "hms", "usa", "iso", "eur", "jis"};
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< value.length; i++)
            {
                ds.setTimeFormat(value[i]);

                if (!ds.getTimeFormat().equals(value[i]))
                {
                    failed = true;
                    break;
                }
            }

            if (!failed)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that setTimeFormat(String) throws an ExtendedIllegalArgumentException if the value is invalid.
    **/
    public void Var102()
    {
        String value = "myFormat";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setTimeFormat(value);

            failed("Unexpected Results. " + ds.getTimeFormat());
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getTimeSeparator() returns the expected default.
    **/
    public void Var103()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.getTimeSeparator().equals(""))
                succeeded();
            else
                failed("Unexpected Results. " + ds.getTimeSeparator());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setTimeSeparator(String) sets the expected value.
    **/
    public void Var104()
    {
        String[] value = {":", ".", ",", "b"};

        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< value.length; i++)
            {
                ds.setTimeSeparator(value[i]);

                if (!ds.getTimeSeparator().equals(value[i]))
                {
                    failed = true;
                    System.out.println(i);
                    break;
                }
            }
            if (!failed)
                succeeded();
            else
                failed("Unexpected Results. " + ds.getTimeSeparator());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setTimeSeparator(String) uses the default value is invalid.
    **/
    public void Var105()
    {
        String value = "mySeparator";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setTimeSeparator(value);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
    *  Validates that getTransactionIsolation() returns the expected default.
    **/
    public void Var106()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.getTransactionIsolation().equals("read uncommitted"))
                succeeded();
            else
                failed("Unexpected Results. " + ds.getTransactionIsolation());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setTransactionIsolation(String) sets the expected value.
    **/
    public void Var107()
    {
        String[] value = {"NONE", "read committed", "repeatable read", "serializable" , "read uncommitted"};

        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            boolean failed = false;
            for (int i=0; i< value.length; i++)
            {
                ds.setTransactionIsolation(value[i]);

                if (!ds.getTransactionIsolation().equals(value[i]))
                {
                    failed = true;
                    System.out.println(i);
                    break;
                }
            }
            if (!failed)
                succeeded();
            else
                failed("Unexpected Results. " + ds.getTransactionIsolation());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setTransactionIsolation(String) throws an ExtendedIllegalArgumentException if the value is invalid.
    **/
    public void Var108()
    {
        String value = "mySeparator";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setTransactionIsolation(value);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalArgumentException o)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getUser() returns the correct default value.
    **/
    public void Var109()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            String userName = "";
            if (environment_ == OS_AS400)
            {
		// the  data source has the id of the current current
		// not the systemObject_, which used a userid / password
                // userName = systemObject_.getUserId();
		userName = System.getProperty("user.name"); 
            }

            if (ds.getUser().equalsIgnoreCase(userName))
                succeeded();
            else
                failed("Unexpected Results. ds.getUser()= " + ds.getUser()+ " expected "+userName);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setUser(String) sets the expected value.
    **/
    public void Var110()
    {
        String value = "myUser";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setUser(value);

            if (ds.getUser().equalsIgnoreCase(value))
                succeeded();
            else
                failed("Unexpected Results. " + ds.getUser());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
       Validates that getConnection(String,String) returns a valid connection object.

       AS400JDBCDataSource(String,String,String)
       setServerName()
       getConnection(String,String)
    **/
    public void Var111()
    {
        try
        {
            AS400JDBCDataSource ds = dataSource_;

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            Connection c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

            if (rs != null)
                succeeded();
            else
                failed("Unexpected results.");

            c.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
       Validates that getConnection(String,String) returns a valid connection object.

       AS400JDBCDataSource(String,String,String)
       setServerName()
       getConnection(String,String)
    **/
    public void Var112()
    {
        String collection = "dstest";
        String table = "dstable";
        Statement s = null;
        Connection c = null;
        String oldNaming = null;

        try
        {
            AS400JDBCDataSource ds = dataSource_;
            oldNaming = ds.getNaming();
            ds.setNaming("system");

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
            s = c.createStatement();
            try
            {
                s.executeUpdate("CREATE COLLECTION " + collection);
            }
            catch (SQLException cc)
            { /* ignore */
            }
            try
            {
                s.executeUpdate("CREATE TABLE " + collection + "/" + table + " (ACCOUNT INTEGER, NAME VARCHAR(25), BALANCE DOUBLE)");
            }
            catch (SQLException ct)
            {
                if (ct.getMessage().indexOf("SQL0601") != -1)
                { /* ignore if already exists. */
                }
                else
                    throw new SQLException(ct.getMessage());
            }
            PreparedStatement insert = c.prepareStatement("INSERT INTO " + collection + "/" + table + " (ACCOUNT, NAME, BALANCE) VALUES (?,?,?)");
            for (int i=0; i< 2000; i++)
            {
                insert.setInt(1, 1000+i);
                insert.setString(2, "Customer " + i);
                insert.setDouble(3, new Double("100.01").doubleValue());
                insert.executeUpdate();
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                s.executeUpdate("DROP TABLE " + collection + "/" + table);
                s.close();
                c.close();
            }
            catch (SQLException close)
            {
                System.out.println("WARNING... testcase cleanup failed.  Could not drop table: " + collection + "/" + table);
                close.printStackTrace();
            }
            if (oldNaming != null)
              try { dataSource_.setNaming(oldNaming); } catch (Exception e) {}
        }
    }

    /**
    *  Validates that setTrace(true) starts the JDBC tracing.
    **/
    public void Var113()
    {
        if (isApplet_)
        {

            notApplicable("Running in applet");
            return;
        }
        Connection c = null;

        AS400JDBCDataSource ds = new AS400JDBCDataSource();
        try
        {
            previousPrintWriter_ = DriverManager.getLogWriter();
            ds.setTrace(true);

            writer_ = new PrintWriter(new FileWriter(testFile_));
            DriverManager.setLogWriter(writer_);

            ds.setServerName(systemNameLocal_);
            ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds.setPassword(charPassword);

            c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Statement s = c.createStatement();
            s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
            writer_.flush(); 
	    boolean passwordSet = checkLog("password is set");
	    boolean connectionCreated = checkLog("connection created");
            assertCondition (passwordSet && connectionCreated,
			     "Did not find 'password is set'="+passwordSet+" or 'connection created'="+connectionCreated+" in log\n"+logBuffer.toString());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            ds.setTrace(false);
            try
            {
                if (c != null && !c.isClosed())
                {
                    c.close();
                }
            }
            catch (SQLException sql)
            {
                sql.printStackTrace();
            }
            DriverManager.setLogWriter(previousPrintWriter_);
        }
    }


    /**
    *  Validates that serialization works on the default object.
    **/
    public void Var114()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            AS400JDBCDataSource ds2 = serialize(ds);

            String defaultValue = "";
            String userName = "";
            if (environment_ == OS_AS400)
            {
                defaultValue = "localhost";

		// the  data source has the id of the current current
		// not the systemObject_, which used a userid / password
                // userName = systemObject_.getUserId();
		userName = System.getProperty("user.name"); 

            }

            assertCondition(ds2.getServerName().equals(defaultValue) &&
                            ds2.getUser().equalsIgnoreCase(userName) &&
                            ds2.getDatabaseName().equals("") &&
                            ds2.getDataSourceName().equals("") &&
                            ds2.getDescription().equals(""),
	                    "ds2.getServerName()="+ds2.getServerName()+" sb "+defaultValue+"\n"+
                            "ds2.getUser()="+ds2.getUser()+" sb "+userName+"\n"+
			    "ds2.getDatabaseName()="+ds2.getDatabaseName()+" sb ''\n"+
			    "ds2.getDataSourceName()="+ds2.getDataSourceName()+" sb ''\n" +
			    "ds2.getDescription()="+ds2.getDescription()+" sb ''\n"); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that serialization works as expected with the serverName set.
    **/
    public void Var115()
    {
        String serverName = "mySystem";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setServerName(serverName);

            AS400JDBCDataSource ds2 = serialize(ds);

            String userName = "";
            if (environment_ == OS_AS400)
            {
		// the  data source has the id of the current current
		// not the systemObject_, which used a userid / password
                // userName = systemObject_.getUserId();
		userName = System.getProperty("user.name"); 

             }

            if (ds2.getServerName().equalsIgnoreCase(serverName) &&
                ds2.getUser().equalsIgnoreCase(userName) &&
                ds2.getDatabaseName().equals("") &&
                ds2.getDataSourceName().equals("") &&
                ds2.getDescription().equals(""))
                succeeded();
            else
                failed("Unexpected results. \n"+
		       "ds2.getServerName()="+ds2.getServerName()+" sb "+serverName+"\n"+
		       "ds2.getUser()="+ds2.getUser()+" sb "+userName+"\n"+
		       "ds2.getDatabaseName()="+ds2.getDatabaseName()+" sb ''\n"+
		       "ds2.getDataSourceName()="+ds2.getDataSourceName()+" sb ''\n" +
		       "ds2.getDescription()="+ds2.getDescription() +" sb ''\n"); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that serialization works as expected with the user set.
    **/
    public void Var116()
    {
        String user = "myUser";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setUser(user);

            AS400JDBCDataSource ds2 = serialize(ds);

            String defaultValue = "";
            if (environment_ == OS_AS400)
                defaultValue = "localhost";

            if (ds2.getServerName().equals(defaultValue) &&
                ds2.getUser().equalsIgnoreCase(user) &&
                ds2.getDatabaseName().equals("") &&
                ds2.getDataSourceName().equals("") &&
                ds2.getDescription().equals(""))
                succeeded();
            else
                failed("Unexpected results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that serialization works as expected with the databaseName, dataSourceName, and description set.
    **/
    public void Var117()
    {
        String databaseName = "myDatabase";
        String dataSourceName = "myDataSource";
        String description = "myDescription";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setDatabaseName(databaseName);
            ds.setDataSourceName(dataSourceName);
            ds.setDescription(description);

            AS400JDBCDataSource ds2 = serialize(ds);

            String defaultValue = "";
            String userName = "";
            if (environment_ == OS_AS400)
            {
                defaultValue = "localhost";
		// the  data source has the id of the current current
		// not the systemObject_, which used a userid / password
                // userName = systemObject_.getUserId();
		userName = System.getProperty("user.name"); 

             }

            if (ds2.getServerName().equals(defaultValue) &&
                ds2.getUser().equalsIgnoreCase(userName) &&
                ds2.getDatabaseName().equals(databaseName) &&
                ds2.getDataSourceName().equals(dataSourceName) &&
                ds2.getDescription().equals(description))
                succeeded();
            else
                failed("Unexpected results.\n"+
		       "ds2.getServerName()="+ds2.getServerName()+" sb "+defaultValue+"\n"+
		       "ds2.getUser()="+ds2.getUser()+" sb "+userName+"\n"+
		       "ds2.getDatabaseName()="+ds2.getDatabaseName()+" sb "+databaseName+"\n"+
		       "ds2.getDataSourceName()="+ds2.getDataSourceName()+" sb "+dataSourceName+"\n" +
		       "ds2.getDescription()="+ds2.getDescription() +" sb "+description+"\n");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
    *  Validates that serialization works as expected after setting a JDBCProperty.
    **/
    public void Var118()
    {
        String dateFormat = "ymd";
	StringBuffer sb = new StringBuffer(); 
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setDateFormat(dateFormat);

            AS400JDBCDataSource ds2 = serialize(ds);

            String defaultValue = "";
            String userName = "";
            if (environment_ == OS_AS400)
            {
                defaultValue = "localhost";
		// the  data source has the id of the current current
		// not the systemObject_, which used a userid / password
                // userName = systemObject_.getUserId();
		userName = System.getProperty("user.name"); 

            }
	    boolean passed = true;


	    if (!ds2.getServerName().equals(defaultValue)) {
		passed=false;
		sb.append("ds2.getServerName()="+ds2.getServerName()+" sb "+defaultValue+"\n"); 
	    } 
	    if (!ds2.getUser().equalsIgnoreCase(userName)) {
		passed=false;
		sb.append("ds2.getUser()="+ds2.getUser()+" sb "+userName+"\n"); 
	    }
	    if (!ds2.getDatabaseName().equals("")){
		passed=false;
		sb.append("ds2.getDatabaseName()="+ds2.getDatabaseName()+" sb ''\n"); 
	    }
	    if (!ds2.getDataSourceName().equals("")) {
		passed=false;
		sb.append("ds2.getDataSourceName()="+ds2.getDataSourceName()+" sb ''"); 
	    }
	    if (!ds2.getDescription().equals("")) {
		sb.append("ds2.getDescription()="+ds2.getDescription()+" sb ''");
		passed=false;
	    }
	    if (!ds2.getDateFormat().equals(dateFormat)) {
		sb.append("ds2.getDateFormat()="+ds2.getDateFormat()+" sb "+dateFormat+"\n"); 
		passed=false;
	    }

	    assertCondition(passed, sb.toString()); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setLoginTimeout(int) throws an SQLException - Function not supported.
    *  09/28/2010 setLoginTimeout now supported. 
    **/
    public void Var119()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setLoginTimeout(120);

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setLogWriter(PrintWriter) throws a NullPointerException.
    **/
    public void Var120()
    {
        if (!TOOLBOX_PACKAGE.isCompatibleWith("5.2.0"))  // pre-V5R2M0
        {
            notApplicable("Toolbox level is pre-Mod5.");
            return;
        }
        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter("log.out"));
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setLogWriter(null);

            assertCondition(ds.getLogWriter() == null, "writer was "+writer);
            writer.close(); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that toString() returns an empty string.
    **/
    public void Var121()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.toString().equals(""))
                succeeded();
            else
                failed("Unexpected results. " + ds.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that toString() returns the dataSourceName.
    **/
    public void Var122()
    {
        String dataSourceName = "myDataSource";
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setDataSourceName(dataSourceName);

            if (ds.toString().equals(dataSourceName))
                succeeded();
            else
                failed("Unexpected results. " + ds.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that a datasource object can be set into JNDI,
    *  and then instantiated from JNDI.
    **/
    public void Var123()
    {
        if (dataSource_ != null)
        {
            notApplicable("Using serialized datasource");
            return;
        }
        if (isApplet_ && jndiType_ == JNDI_FILE)
        {
            notApplicable("Running in applet and jndiType is file");
            return;
        }

        String jndiName = "javatest/test123";

        if (jndiType_ == JNDI_LDAP)
            jndiName = "cn=test123datasource, cn=users, ou=rochester,o=ibm,c=us";

        String databaseName = "QCUSTCDT";
        String serverName = systemNameLocal_; 
        String description = "Customer Database";

        try
        {
            // Create a data source to the AS/400 database.
            AS400JDBCDataSource dataSource = new AS400JDBCDataSource();
            dataSource.setServerName(serverName);
            dataSource.setDatabaseName(databaseName);
            dataSource.setDescription(description);

            // Bind the data source into JNDI.
            try
            {
                context_.bind(jndiName, dataSource);
            }
            catch (NameAlreadyBoundException n)
            {
                context_.unbind(jndiName);
                context_.bind(jndiName, dataSource);
            }

            // Return an AS400JDBCDataSource object from JNDI and get a connection.
            AS400JDBCDataSource jndiDataSource = (AS400JDBCDataSource) context_.lookup(jndiName);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            Connection connection = jndiDataSource.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

            if (rs.next() &&
                jndiDataSource.getServerName().equals(serverName) &&
                jndiDataSource.getDatabaseName().equals(databaseName) &&
                jndiDataSource.getDescription().equals(description))
                succeeded();
            else
                failed("Unexpected results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                context_.unbind(jndiName);              // Cleanup JNDI.
            }
            catch (Exception cxt)
            {
            }
        }
    }


    /**
    *  Validates that a default datasource object can be set into JNDI,
    *  and then instantiated from JNDI.
    **/
    public void Var124()
    {
        if (dataSource_ != null)
        {
            notApplicable("Using serialized datasource");
            return;
        }
        if (isApplet_ && jndiType_ == JNDI_FILE)
        {
            notApplicable("Running in applet and jndiType is file");
            return;
        }

        String jndiName = "javatest/test124";

        if (jndiType_ == JNDI_LDAP)
            jndiName = "cn=test124datasource, cn=users, ou=rochester,o=ibm,c=us";

        try
        {
            // Create a data source to the AS/400 database.
            AS400JDBCDataSource dataSource = new AS400JDBCDataSource();

            // Bind the data source into JNDI.
            try
            {
                context_.bind(jndiName, dataSource);
            }
            catch (NameAlreadyBoundException n)
            {
                context_.unbind(jndiName);
                context_.bind(jndiName, dataSource);
            }

            // Return an AS400JDBCDataSource object from JNDI and get a connection.
            AS400JDBCDataSource jndiDataSource = (AS400JDBCDataSource) context_.lookup(jndiName);
            jndiDataSource.setServerName(systemNameLocal_);

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            Connection connection = jndiDataSource.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

            if (rs.next())
                succeeded();
            else
                failed("Unexpected results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                context_.unbind(jndiName);              // Cleanup JNDI.
            }
            catch (Exception cxt)
            {
            }
        }
    }

    /**
    *  Validates that a datasource object with serverName and user can be created set into JNDI,
    *  and then instantiated from JNDI.
    **/
    public void Var125()
    {
        if (dataSource_ != null)
        {
            notApplicable("Using serialized datasource");
            return;
        }
        if (isApplet_ && jndiType_ == JNDI_FILE)
        {
            notApplicable("Running in applet and jndiType is file");
            return;
        }

        String jndiName = "javatest/test125";

        if (jndiType_ == JNDI_LDAP)
            jndiName = "cn=test125datasource, cn=users, ou=rochester,o=ibm,c=us";

        try
        {
            // Create a data source to the AS/400 database.
            AS400JDBCDataSource dataSource = new AS400JDBCDataSource();
            dataSource.setServerName(systemNameLocal_);
            dataSource.setUser(systemObject_.getUserId());

            // Register the datasource with the Java Naming and Directory Interface (JNDI).
            try
            {
                context_.bind(jndiName, dataSource);
            }
            catch (NameAlreadyBoundException n)
            {
                context_.unbind(jndiName);
                context_.bind(jndiName, dataSource);
            }

            // Return an AS400JDBCDataSource object from JNDI and get a connection.
            AS400JDBCDataSource jndiDataSource = (AS400JDBCDataSource) context_.lookup(jndiName);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            jndiDataSource.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

            Connection connection = jndiDataSource.getConnection();

            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

            if (rs.next() &&
                jndiDataSource.getServerName().equals(systemNameLocal_) &&
                jndiDataSource.getUser().equals(systemObject_.getUserId()))
                succeeded();
            else
            {
                StringBuffer details = new StringBuffer("\n");
                if (!jndiDataSource.getServerName().equals(systemNameLocal_))
                    details.append("serverName: " + jndiDataSource.getServerName() + "\n");

                if (!jndiDataSource.getUser().equals(systemObject_.getUserId()))
                    details.append("user: " + jndiDataSource.getUser());

                failed("Unexpected results. " + new String(details));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                context_.unbind(jndiName);              // Cleanup JNDI.
            }
            catch (Exception cxt)
            {
            }
        }
    }


    /**
    *  Validates that a datasource object with serverName and user can be set into JNDI,
    *  and then instantiated from JNDI and finally used to get a connection after changing properies.
    **/
    public void Var126()
    {
        if (dataSource_ != null)
        {
            notApplicable("Using serialized datasource");
            return;
        }
        if (isApplet_ && jndiType_ == JNDI_FILE)
        {
            notApplicable("Running in applet and jndiType is file");
            return;
        }

        String jndiName = "javatest/test126";

        if (jndiType_ == JNDI_LDAP)
            jndiName = "cn=test126datasource, cn=users, ou=rochester,o=ibm,c=us";

        try
        {
            // Create a data source to the AS/400 database.
            AS400JDBCDataSource dataSource = new AS400JDBCDataSource();
            dataSource.setServerName("mySystem");
            dataSource.setUser("myUser");

            // Register the datasource with the Java Naming and Directory Interface (JNDI).
            try
            {
                context_.bind(jndiName, dataSource);
            }
            catch (NameAlreadyBoundException n)
            {
                context_.unbind(jndiName);
                context_.bind(jndiName, dataSource);
            }

            // Return an AS400JDBCDataSource object from JNDI and get a connection.
            AS400JDBCDataSource jndiDataSource = (AS400JDBCDataSource) context_.lookup(jndiName);
            jndiDataSource.setServerName(systemNameLocal_);
            jndiDataSource.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            jndiDataSource.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

            Connection connection = jndiDataSource.getConnection();

            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

            if (rs.next() &&
                jndiDataSource.getServerName().equals(systemNameLocal_) &&
                jndiDataSource.getUser().equals(systemObject_.getUserId()))
                succeeded();
            else
                failed("Unexpected results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                context_.unbind(jndiName);              // Cleanup JNDI.
            }
            catch (Exception cxt)
            {
            }
        }
    }


    /**
    *  Validates that a datasource object with serverName and user can be set into JNDI,
    *  and then instantiated from JNDI and finally used to get a connection after changing properies.
    **/
    public void Var127()
    {
        if (dataSource_ != null)
        {
            notApplicable("Using serialized datasource");
            return;
        }
        if (isApplet_ && jndiType_ == JNDI_FILE)
        {
            notApplicable("Running in applet and jndiType is file");
            return;
        }

        String jndiName = "javatest/test127";

        if (jndiType_ == JNDI_LDAP)
            jndiName = "cn=test127datasource, cn=users, ou=rochester,o=ibm,c=us";

        try
        {
            // Create a data source to the AS/400 database.
            AS400JDBCDataSource dataSource = new AS400JDBCDataSource();
            dataSource.setExtendedDynamic(true);
            String dateFormat = "ymd";
            dataSource.setDateFormat(dateFormat);

            // Register the datasource with the Java Naming and Directory Interface (JNDI).
            try
            {
                context_.bind(jndiName, dataSource);
            }
            catch (NameAlreadyBoundException n)
            {
                context_.unbind(jndiName);
                context_.bind(jndiName, dataSource);
            }

            // Return an AS400JDBCDataSource object from JNDI and get a connection.
            AS400JDBCDataSource jndiDataSource = (AS400JDBCDataSource) context_.lookup(jndiName);
            jndiDataSource.setServerName(systemNameLocal_);

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            Connection connection = jndiDataSource.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

            if (rs.next() &&
                jndiDataSource.getDateFormat().equals(dateFormat) &&
                jndiDataSource.isExtendedDynamic())
                succeeded();
            else
                failed("Unexpected results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                context_.unbind(jndiName);              // Cleanup JNDI.
            }
            catch (Exception cxt)
            {
            }
        }
    }

    /**
    *  Validates that setLogWriter(PrintWriter) works as expected
    **/
    public void Var128()
    {
        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter("log.out"));
            AS400JDBCDataSource ds = dataSource_;
            ds.setLogWriter(writer);

            ds.setServerName(systemNameLocal_);
            ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            Connection c = ds.getConnection();

            ds.setAccess("all");
            ds.setBigDecimal(true);
            ds.setBlockCriteria(0);
            ds.setBlockSize(32);
            ds.setCursorHold(false);
            ds.setDatabaseName("customer");
            ds.setDataCompression(true);
            ds.setDataSourceName("dsName");
            ds.setDataTruncation(false);
            ds.setDateFormat("usa");
            ds.setDateSeparator("-");
            ds.setDecimalSeparator(",");
            ds.setDescription("mydb");
            ds.setDriver("native");
            ds.setErrors("full");
            ds.setExtendedDynamic(true);
            ds.setLazyClose(false);
            ds.setLibraries("LIBL");
            ds.setLobThreshold(2084);
            ds.setNaming("system");
            ds.setPackage("pk1");
            ds.setPackageAdd(false);
            ds.setPackageCache(true);
            ds.setPackageCriteria("select");
            ds.setPackageError("warning");
            ds.setPackageLibrary("QGPL");
            ds.setPrefetch(true);
            ds.setPrompt(true);
            ds.setProxyServer("test");
            ds.setRemarks("sql");
            ds.setSecondaryUrl("rchasxxx");
            ds.setSecure(false);
            ds.setSort("language");
            ds.setSortLanguage("enu");
            ds.setSortTable("");
            ds.setSortWeight("unique");
            ds.setThreadUsed(true);
            ds.setTimeFormat("usa");
            ds.setTimeSeparator(".");
            ds.setTrace(false);
            ds.setTransactionIsolation("read committed");
            ds.setTranslateBinary(false);

            assertCondition(ds.getLogWriter() == writer, "Connection was "+c);
            writer.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally {
          copyFromTo(dataSourceClone_, dataSource_, encryptedPassword_);
        }
    }

    /**
    *  Validates that the constuctor AS400JDBCDataSource(String) can be used to get
    *  a usable connection.
    **/
    public void Var129()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource(systemNameLocal_);
            ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

            Connection c = ds.getConnection();
            c.close();

            assertCondition (c != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    *  Validates that a datasource object with null data
    *  and then instantiated from JNDI and finally used to get a connection after changing properies.
    **/
    public void Var130()
    {
        if (dataSource_ != null)
        {
            notApplicable("Using serialized datasource");
            return;
        }
        if (isApplet_ && jndiType_ == JNDI_FILE)
        {
            notApplicable("Running in applet and jndiType is file");
            return;
        }

        String jndiName = "javatest/test130";

        if (jndiType_ == JNDI_LDAP)
            jndiName = "cn=test130datasource, cn=users, ou=rochester,o=ibm,c=us";

        try
        {
            // Create a data source to the AS/400 database.
            AS400JDBCDataSource dataSource = new AS400JDBCDataSource();
            dataSource.setExtendedDynamic(true);
            String dateFormat = "ymd";
            dataSource.setDateFormat(dateFormat);

            // Register the datasource with the Java Naming and Directory Interface (JNDI).
            try
            {
                context_.bind(jndiName, dataSource);
            }
            catch (NameAlreadyBoundException n)
            {
                context_.unbind(jndiName);
                context_.bind(jndiName, dataSource);
            }

            // Return an AS400JDBCDataSource object from JNDI and get a connection.
            AS400JDBCDataSource jndiDataSource = (AS400JDBCDataSource) context_.lookup(jndiName);
            jndiDataSource.setServerName(systemNameLocal_);

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            Connection connection = jndiDataSource.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

            if (rs.next() &&
                jndiDataSource.getDateFormat().equals(dateFormat) &&
                jndiDataSource.isExtendedDynamic())
                succeeded();
            else
                failed("Unexpected results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                context_.unbind(jndiName);              // Cleanup JNDI.
            }
            catch (Exception cxt)
            {
            }
        }
    }

    /**
    *  Validates that getBidiStringType() returns the correct default value.
    **/
    public void Var131()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
	    // With new BIDI changes, this is now 5
            if (ds.getBidiStringType() == 5)
                succeeded();
            else
                failed("Unexpected Results for getBidiStringType() sb 5 but got. " + ds.getBidiStringType());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setBidiStringType(String) sets the expected sort table.
    **/
    public void Var132()
    {
        int value = 4;
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setBidiStringType(value);

            if (ds.getBidiStringType() == value)
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that setBidiStringType(int) throws an ExtendedIllegalArgumentException if the property value is invalid.
    **/
    public void Var133()
    {
        try
        {
            AS400JDBCDataSource ds = dataSource_;

            ds.setBidiStringType(3);
            failed("Unexpected Results.");
        }
        catch (ExtendedIllegalArgumentException l)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    // @W1 new method
    /**
    *  Validates that getFullOpen() returns the correct default value.
    **/
    public void Var134()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.isFullOpen())
                failed("Full open default should be false");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    // @W1 new method
    /**
    *  Validates that setFullOpen(boolean) works
    **/
    public void Var135()
    {
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setFullOpen(true);

            if (ds.isFullOpen())
                succeeded();
            else
                failed("Full Open Not True.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    // @W1 new method
    /**
    *  Run a query is fill open set.
    **/
    public void Var136()
    {
        try
        {
            AS400JDBCDataSource ds = dataSource_;
            ds.setFullOpen(true);

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            Connection c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

            if (rs != null)
                succeeded();
            else
                failed("Unexpected results.");

            c.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    // @C1A Make sure method returns a valid object.  P9950596 
    /**
    *  Run a query is fill open set.
    **/
    public void Var137()
    {
        try
        {
            AS400JDBCDataSource ds = dataSource_;
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            Connection c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            assertCondition (c.getMetaData() != null);

            c.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    // @C1A Fix to method, before it was only returning jdbc:as400://. P9951301  
    /**
    *  Run a query is fill open set.
    **/
    public void Var138()
    {
        if (!TOOLBOX_PACKAGE.isCompatibleWith("5.2.0"))  // pre-V5R2M0
        {
            notApplicable("Toolbox level is pre-Mod5.");
            return;
        }
        try
        {
            AS400JDBCDataSource ds = dataSource_;
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            Connection c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            String defaultSystemName = systemNameLocal_;
            if (environment_ == OS_AS400)
                defaultSystemName = "localhost";

            assertCondition (c.getMetaData().getURL().equals("jdbc:as400://" + defaultSystemName), "URL is '"+c.getMetaData().getURL()+"' sb 'jdbc:as400://" + defaultSystemName +"'");
            c.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }  


    // @C2A Fix setPrompt method when getConnection(String, String) is called.
    /**
    *  Validates that getConnection(user, incorrectPassword) fails with exception
    *  when prompting is off.
    **/
    public void Var139()
    {
        if (!TOOLBOX_PACKAGE.isCompatibleWith("5.2.0"))  // pre-V5R2M0
        {
            notApplicable("Toolbox level is pre-Mod5.");
            return;
        }
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource(systemNameLocal_,
                                                             "garfield",
                                                             "garfield".toCharArray());
            ds.setPrompt(false);

            ds.getConnection("garfield", "garfield".toCharArray());
            failed("Did not throw exception");           
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
    }


    // @C2A Fix setPrompt method when getConnection() is called.
    /**
    *  Validates that getConnection() fails with exception when prompting is off.
    **/
    public void Var140()
    {
        if (!TOOLBOX_PACKAGE.isCompatibleWith("5.2.0"))  // pre-V5R2M0
        {
            notApplicable("Toolbox level is pre-Mod5.");
            return;
        }
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource(systemNameLocal_,
                                                             "garfield",
                                                             "garfield".toCharArray());
            ds.setPrompt(false);

            ds.getConnection();
            failed("Did not throw exception");           
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
    }


    // @C2A Fix setPrompt method when getConnection() is called.
    /**
    *  Validates that getConnection(user, incorrectPassword) fails with exception 
    *  when prompting is off.
    **/
    public void Var141()
    {
        if (!TOOLBOX_PACKAGE.isCompatibleWith("5.2.0"))  // pre-V5R2M0
        {
            notApplicable("Toolbox level is pre-Mod5.");
            return;
        }
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setServerName(systemNameLocal_);
            ds.setUser("garfield");
            ds.setPassword("garfield".toCharArray());
            ds.setPrompt(false);

            ds.getConnection("garfield", "garfield".toCharArray());
            failed("Did not throw exception");           
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
    }


    // @C2A Fix setPrompt method when getConnection() is called.
    /**
    *  Validates that getConnection() fails with exception when prompting is off.
    **/
    public void Var142()
    {
        if (!TOOLBOX_PACKAGE.isCompatibleWith("5.2.0"))  // pre-V5R2M0
        {
            notApplicable("Toolbox level is pre-Mod5.");
            return;
        }
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setServerName(systemNameLocal_);
            ds.setUser("garfield");
            ds.setPassword("garfield".toCharArray());
            ds.setPrompt(false);

            ds.getConnection();
            failed("Did not throw exception");           
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
    }


    /**
    *  Validates that setSecure(boolean) sets the value as expected.
    **/
    public void Var143()
    {
        if (dataSource_ != null)
        {
            notApplicable("Using serialized datasource");
            return;
        }
        try
        {
            try
            {
                Class.forName("com.ibm.sslight.SSLightKeyRing");
            }
            catch (Exception e)
            {
                notApplicable("SSL classes not in classpath");
                return;
            }

               char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

            AS400JDBCDataSource ds = new AS400JDBCDataSource(systemNameLocal_,
                                                             systemObject_.getUserId(),
                                                             charPassword);
   PasswordVault.clearPassword(charPassword);
            ds.setSecure(true);

            Connection c = ds.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT CUSNUM, LSTNAM, INIT, STREET, CITY, STATE, ZIPCOD, CDTLMT, CHGCOD, BALDUE, CDTDUE FROM QIWS.QCUSTCDT WHERE CUSNUM = 392859 ORDER BY STREET", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = ps.executeQuery();
            assertCondition(true, "rs = "+rs); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
   *  Validates that setSecure(boolean) sets the value as expected -- fix bug with 
   *  getConnection(String, String).
   **/
    public void Var144()
    {
        if (dataSource_ != null)
        {
            notApplicable("Using serialized datasource");
            return;
        }
        try
        {
            try
            {
                Class.forName("com.ibm.sslight.SSLightKeyRing");
            }
            catch (Exception e)
            {
                notApplicable("SSL classes not in classpath");
                return;
            }
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

            AS400JDBCDataSource ds = new AS400JDBCDataSource(systemNameLocal_,
                                                             systemObject_.getUserId(),
                                                             charPassword);
            ds.setSecure(true);

            Connection c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
            PreparedStatement ps = c.prepareStatement("SELECT CUSNUM, LSTNAM, INIT, STREET, CITY, STATE, ZIPCOD, CDTLMT, CHGCOD, BALDUE, CDTDUE FROM QIWS.QCUSTCDT WHERE CUSNUM = 392859 ORDER BY STREET", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = ps.executeQuery();
            assertCondition(true, "rs ="+rs); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    // @C3 new method
    /**
    *  Validates that getExtendedMetaData() returns the correct default value.
    **/
    public void Var145()
    {
        if (!TOOLBOX_PACKAGE.isCompatibleWith("5.2.0"))  // pre-V5R2M0
        {
            notApplicable("Toolbox level is pre-Mod5.");
            return;
        }
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();

            if (ds.isExtendedMetaData())
                failed("Extended metadata default should be false");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    // @C3 new method
    /**
    *  Validates that setExtendedMetaData(boolean) works
    **/
    public void Var146()
    {
        if (!TOOLBOX_PACKAGE.isCompatibleWith("5.2.0"))  // pre-V5R2M0
        {
            notApplicable("Toolbox level is pre-Mod5.");
            return;
        }
        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setExtendedMetaData(true);

            if (ds.isExtendedMetaData())
                succeeded();
            else
                failed("Full Open Not True.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    // @W1 new method
    /**
    *  Run a query with extended metadata property set on.
    **/
    public void Var147()
    {
        if (!TOOLBOX_PACKAGE.isCompatibleWith("5.2.0"))  // pre-V5R2M0
        {
            notApplicable("Toolbox level is pre-Mod5.");
            return;
        }
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCDataSource ds = new AS400JDBCDataSource(systemNameLocal_, systemObject_.getUserId(), charPassword);

            ds.setExtendedMetaData(true);

            Connection c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

            if (rs != null)
                succeeded();
            else
                failed("Unexpected results.");

            c.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
    *  Validates that serialization saves the password when told to do so
    **/
    public void Var148()
    {       
        if (!TOOLBOX_PACKAGE.isCompatibleWith("5.2.0"))  // pre-V5R2M0
        {
            notApplicable("Toolbox level is pre-Mod5.");
            return;
        }
        Connection c1  = null;
        Connection c2  = null;
        Statement  s1  = null;
        Statement  s2  = null;
        ResultSet  rs1 = null;
        ResultSet  rs2 = null;
        

        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setServerName(systemNameLocal_);
            ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            try {
              ds.setSavePasswordWhenSerialized(true);
            }             
            catch (NoSuchMethodError e) {}
            ds.setPrompt(false);

            c1  = ds.getConnection();
            s1  = c1.createStatement();
            rs1 = s1.executeQuery("select * from qiws.qcustcdt");

            if (rs1.next())
            {
               try { rs1.close(); } catch (Exception e) {}
               try { s1.close(); } catch (Exception e) {}
               try { c1.close(); } catch (Exception e) {} 
               rs1 = null;
               s1  = null;
               c1  = null;

               AS400JDBCDataSource ds2 = serialize(ds);
               
               if (ds2 != null)
               {
                  c2 = ds2.getConnection();
                  s2 = c2.createStatement();
                  rs2 = s2.executeQuery("select * from qiws.qcustcdt");
                  if (rs2.next())
                     succeeded();
                  else
                     failed("second query failed");
               }                                
               else
                  failed("could not serialize");
            }
            else
               failed("could not query database");
            
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }                       
        
        if (rs1 != null) try { rs1.close(); } catch (Exception e) {}
        if (rs2 != null) try { rs2.close(); } catch (Exception e) {}
        if (s1 != null) try { s1.close(); } catch (Exception e) {}
        if (s2 != null) try { s2.close(); } catch (Exception e) {}
        if (c1 != null) try { c1.close(); } catch (Exception e) {}
        if (c2 != null) try { c2.close(); } catch (Exception e) {}
    }




    /**
    *  Validates that by default the password is not serialized.
    **/
    public void Var149()
    {       
        // testcase does not work when on AS/400 the password for th current user is
        // automatically allocated .
	System.out.println("osVersion is "+JTOpenTestEnvironment.osVersion); 
        if (JTOpenTestEnvironment.isOS400)
        {                            
           succeeded();
           return;
        }
      
        Connection c1  = null;
        Connection c2  = null;
        Statement  s1  = null;
        ResultSet  rs1 = null;
        

        try
        {
            AS400JDBCDataSource ds = new AS400JDBCDataSource();
            ds.setServerName(systemNameLocal_);
            ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            ds.setPrompt(false);

            c1  = ds.getConnection();
            s1  = c1.createStatement();
            rs1 = s1.executeQuery("select * from qiws.qcustcdt");

            if (rs1.next())
            {
               try { rs1.close(); } catch (Exception e) {}
               try { s1.close(); } catch (Exception e) {}
               try { c1.close(); } catch (Exception e) {} 
               rs1 = null;
               s1  = null;
               c1  = null;

               AS400JDBCDataSource ds2 = serialize(ds);
               
               if (ds2 != null)
               {                                       
                  ds2.setPrompt(false);
                  try
                  {
                     c2 = ds2.getConnection();
                     failed("no exception");
                  }
                  catch (Exception e2)
                  {
                     String errorMessage = e2.getMessage().toUpperCase();
                     if (errorMessage.indexOf("PASSWORD") < 0)
                        failed("wrong exception: " + errorMessage);
                     else
                        succeeded();
                  }
               }                                
               else
                  failed("could not serialize");
            }
            else
               failed("could not query database");
            
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }                       
        
        if (rs1 != null) try { rs1.close(); } catch (Exception e) {}
        if (s1 != null) try { s1.close(); } catch (Exception e) {}
        if (c1 != null) try { c1.close(); } catch (Exception e) {}
        if (c2 != null) try { c2.close(); } catch (Exception e) {}
    }








    /**
      Serializes and deserializes the data source object.
     **/
    private AS400JDBCDataSource serialize (AS400JDBCDataSource ds) throws Exception
    {
        // Serialize.
        String serializeFileName = logDirectory_ + "/datasource.ser";
        ObjectOutput out = new ObjectOutputStream (new FileOutputStream (serializeFileName));
        out.writeObject (ds);
        out.flush ();
        out.close();

        // Deserialize.
        AS400JDBCDataSource ds2 = null;
        try
        {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFileName));
            ds2 = (AS400JDBCDataSource) in.readObject ();
            in.close();
        }
        catch (Exception e)
        {
            System.out.println("ERROR... deserialization failed for: \"" + serializeFileName);
            e.printStackTrace();
        }
        finally
        {
            File f = new File (serializeFileName);
            if (!f.delete())
                System.out.println("WARNING... serialization file: \"" + serializeFileName + "\" could not be deleted.");
        }
        return ds2;
    }
}
