///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCMDSTestcase.java
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
import com.ibm.as400.access.AS400JDBCManagedDataSource;
import com.ibm.as400.access.AS400JDBCManagedConnectionPoolDataSource;
import com.ibm.as400.access.ExtendedIllegalArgumentException;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;

/**
  Testcase AS400JDBCMDSTestcase.
 **/
public class AS400JDBCMDSTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "AS400JDBCMDSTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.AS400JDBCDataSourceTest.main(newArgs); 
   }

    public static String logFileName = "/tmp/AJCTS.log"; 
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
    private PrintWriter previousWriter_ = null;


    /**
      Constructor.  This is called from the AS400JDBCDataSourceTest constructor.
     **/
    public AS400JDBCMDSTestcase(AS400 systemObject,
                                       Vector<String> variationsToRun,
                                       int runMode,
                                       FileOutputStream fileOutputStream,
                                       
                                       String password,
                                       String jndiType,
                                       String ldapUsr,
                                       String ldapPwd,
                                       String systemName)  //C1A
    {
        super(systemObject, "AS400JDBCMDSTestcase", variationsToRun,
              runMode, fileOutputStream, password);

        if (jndiType != null) {
            if (jndiType.equals("file"))
                jndiType_ = JNDI_FILE;
            else if (jndiType.equals("ldap"))
                jndiType_ = JNDI_LDAP;
            else
                System.out.println("WARNING... Unknown jndi type '" + jndiType + "' using default.");
        }
        else
            System.out.println("WARNING... jndi type not specified.  Using default.");

        ldapUsr_ = ldapUsr;
        ldapPwd_ = ldapPwd;
        systemNameLocal_ = systemName;
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

            String line = reader.readLine();
            while (line != null)
            {
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

        File cleanupFile = new File(logFileName);
        if (!cleanupFile.delete())
            System.out.println("WARNING... \""+logFileName+"\" could not be deleted.");

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
        if (!false)
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

        if (JTOpenTestEnvironment.isOS400)
            environment_ = OS_AS400;
        else if (JTOpenTestEnvironment.isWindows)
            environment_ = OS_WINDOWS;

        {
            // Get the JNDI Initial Context.
            Properties env = new Properties();

            if (jndiType_ == JNDI_LDAP)
            {
                env.put("java.naming.provider.url", "ldap://" + systemObject_.getSystemName());
                env.put("java.naming.security.principal", "cn=" + ldapUsr_);
                env.put("java.naming.security.credentials", ldapPwd_);
            }	else
            {
             	env.put(Context.PROVIDER_URL,"file:.");  //@pda for linux authority. default is "/" location of file created
            }

            env.put(Context.INITIAL_CONTEXT_FACTORY, jndiType_);
            context_ = new InitialContext(env);
        }
    }

    /**
       Validates that getConnection() returns a valid connection object.

       AS400JDBCManagedDataSource()
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setServerName(systemObject_.getSystemName());
            ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

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

       AS400JDBCManagedDataSource(String,String,String)
       getConnection()
    **/
    public void Var002()
    {
        Connection c = null;
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
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

       AS400JDBCManagedDataSource()
       setServerName()
       getConnection(String,String)
    **/
    public void Var003()
    {
        Connection c = null;
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setServerName(systemObject_.getSystemName());

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

       AS400JDBCManagedDataSource(String,String,String)
       setServerName()
       getConnection(String,String)
    **/
    public void Var004()
    {
        Connection c = null;
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
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

       AS400JDBCManagedDataSource(String,String,String)
       getConnection()
    **/
    public void Var005()
    {
        Connection c = null, c2 = null;
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
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

       AS400JDBCManagedDataSource()
       setServerName()
       getConnection(String,String)
    **/
    public void Var006()
    {
        Connection c = null, c2 = null;
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setServerName(systemObject_.getSystemName());

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

       AS400JDBCManagedDataSource(String,String,String)
       getConnection()
    **/
    public void Var007()
    {
        Connection c = null, c2 = null;
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);

            c= ds.getConnection();
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

       AS400JDBCManagedDataSource()
       setServerName()
       getConnection(String,String)
    **/
    public void Var008()
    {
        Connection c = null, c2 = null;
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setServerName(systemObject_.getSystemName());

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            c = ds.getConnection(systemObject_.getUserId(), charPassword);

            ds.setUser(systemObject_.getUserId());
            ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            c2 = ds.getConnection();

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
       AS400JDBCManagedDataSource()
       setUser()
       setPassword()
       getConnection()
    **/
    public void Var009(int runMode)
    {
        if (runMode != BOTH && runMode != ATTENDED) 
        {
             notApplicable("runMode not SET to run");
             return;
        }

        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

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
       AS400JDBCManagedDataSource()
       setServerName()
       setPassword()
       getConnection()
    **/
    public void Var010(int runMode)
    {
        if (runMode != BOTH && runMode != ATTENDED) 
            if (runMode != BOTH && runMode != ATTENDED) 
            {
                 notApplicable("runMode not SET to run");
                 return;
            }
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setServerName(systemObject_.getSystemName());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

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
       AS400JDBCManagedDataSource()
       setServerName()
       setUser()
       getConnection()
    **/
    public void Var011(int runMode)
    {
        if (runMode != BOTH && runMode != ATTENDED) 
            if (runMode != BOTH && runMode != ATTENDED) 
            {
                 notApplicable("runMode not SET to run");
                 return;
            }
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            AS400.clearPasswordCache();
            ds.setUser(systemObject_.getUserId());
            ds.setServerName(systemObject_.getSystemName());

            Connection c= ds.getConnection();

            assertCondition(true, "Connection created "+c); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
       Validates that getConnection() prompts for a user and password.
       Attended testcase.
       AS400JDBCManagedDataSource()
       setServerName()
       setPassword()
       getConnection()
    **/
    public void Var012(int runMode)
    {
        if (runMode != BOTH && runMode != ATTENDED) 
            if (runMode != BOTH && runMode != ATTENDED) 
            {
                 notApplicable("runMode not SET to run");
                 return;
            }
        
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setServerName(systemObject_.getSystemName());

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            Connection c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Connection c2 = ds.getConnection();

            assertCondition(true, "connections created "+c+" "+c2); 
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

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
    }

    /**
    *  Validates that setAccess(String) throws an ExtendedIllegalArgumentException if the property value is invalid.
    **/
    public void Var015()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
    @SuppressWarnings("deprecation")
    public void Var066()
    {
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            if (ds.isPackageClear() == false)
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
    *  Validates that setPackageClear(boolean) sets the value as expected.
    **/
    @SuppressWarnings("deprecation")
    public void Var067()
    {

        boolean value = true;
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setPackageClear(value);

            if (ds.isPackageClear() != value)
                succeeded();


        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }
    /**
    *  Validates that getPackageCriteria() returns the correct default value.
    **/
    public void Var068()
    {
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
    **/
    public void Var091()
    {
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

            if (ds.getSort() == "hex")  //@A1C
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
    *  Validates that setSort(String) sets the expected server name.
    **/
    public void Var092()
    {
        String[] value = { "hex", "language", "table"}; //@A1C
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

            boolean failed = false;
            for (int i=0; i< value.length; i++)
            {
                ds.setSort(value[i]);

                if (!ds.getSort().equals(value[i]))
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
    *  Validates that setSortLanguage(String) sets the expected sort language.
    **/
    public void Var093()
    {
        String value = "usa";
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

            String userName = "";
            if (environment_ == OS_AS400)
            {
                AS400 tmpSystemObject = new AS400(); //allow diff logon-id as TC id
                userName = tmpSystemObject.getUserId();
                tmpSystemObject.close();
            }

            if (ds.getUser().equals(userName))
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
    *  Validates that setUser(String) sets the expected value.
    **/
    public void Var110()
    {
        String value = "myUser";
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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

       AS400JDBCManagedDataSource(String,String,String)
       setServerName()
       getConnection(String,String)
    **/
    public void Var111()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
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

       AS400JDBCManagedDataSource(String,String,String)
       setServerName()
       getConnection(String,String)
    **/
    public void Var112()
    {
      
        String collection = "JDDS";
        if (testLib_ != null && testLib_.length() > 4) { 
          collection += testLib_.substring(testLib_.length() - 4); 
        }
        String table = "dstable";
        Statement s = null;
        Connection c = null;

        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
            ds.setNaming("system");

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
            try {
              s.executeUpdate("GRANT ALL ON SCHEMA " + collection + " TO USER " + userId_);
            } catch (SQLException e) {
              System.out.println("Warning: error on GRANT");
              e.printStackTrace(System.out);
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
                insert.setDouble(3, Double.valueOf("100.01").doubleValue());
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
        }
    }

    /**
    *  Validates that setTrace(true) starts the JDBC tracing.
    **/
    public void Var113()
    {
        Connection c = null;

        AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
        try
        {
            previousWriter_ = DriverManager.getLogWriter();
            ds.setTrace(true);
            writer_ = new PrintWriter(new FileWriter(testFile_));

            DriverManager.setLogWriter(writer_);

            ds.setServerName(systemObject_.getSystemName());
            ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds.setPassword(charPassword);
            c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Statement s = c.createStatement();
            s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

	    writer_.flush(); 
            assertCondition (checkLog("password is set") &&
                             checkLog("connection created"));

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
            DriverManager.setLogWriter(previousWriter_);
        }
    }


    /**
    *  Validates that serialization works on the default object.
    **/
    public void Var114()
    {
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

            AS400JDBCManagedDataSource ds2 = serialize(ds);

            String defaultValue = "";
            String userName = "";
            if (environment_ == OS_AS400)
            {
                AS400 tmpSystemObject = new AS400(); //allow diff logon-id as TC id
                defaultValue = "localhost";
                userName = tmpSystemObject.getUserId();
                tmpSystemObject.close();
           }

            assertCondition(ds2.getServerName().equals(defaultValue) &&
                            ds2.getUser().equals(userName) &&
                            ds2.getDatabaseName().equals("") &&
                            ds2.getDataSourceName().equals("") &&
                            ds2.getDescription().equals(""));
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setServerName(serverName);

            AS400JDBCManagedDataSource ds2 = serialize(ds);

            String userName = "";
            if (environment_ == OS_AS400)
            {
                AS400 tmpSystemObject = new AS400(); //allow diff logon-id as TC id
                userName = tmpSystemObject.getUserId();
                tmpSystemObject.close();
            }

            if (ds2.getServerName().equalsIgnoreCase(serverName) &&
                ds2.getUser().equals(userName) &&
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
    *  Validates that serialization works as expected with the user set.
    **/
    public void Var116()
    {
        String user = "myUser";
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setUser(user);

            AS400JDBCManagedDataSource ds2 = serialize(ds);

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setDatabaseName(databaseName);
            ds.setDataSourceName(dataSourceName);
            ds.setDescription(description);

            AS400JDBCManagedDataSource ds2 = serialize(ds);

            String defaultValue = "";
            String userName = "";
            if (environment_ == OS_AS400)
            {
                defaultValue = "localhost";
                AS400 tmpSystemObject = new AS400(); //allow diff logon-id as TC id
                userName = tmpSystemObject.getUserId();
                tmpSystemObject.close();
            }

            if (ds2.getServerName().equals(defaultValue) &&
                ds2.getUser().equals(userName) &&
                ds2.getDatabaseName().equals(databaseName) &&
                ds2.getDataSourceName().equals(dataSourceName) &&
                ds2.getDescription().equals(description))
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
    *  Validates that serialization works as expected after setting a JDBCProperty.
    **/
    public void Var118()
    {
        String dateFormat = "ymd";
        boolean packageCache = true;
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setDateFormat(dateFormat);

            ds.setPackageCache(packageCache);

            AS400JDBCManagedDataSource ds2 = serialize(ds);

            String defaultValue = "";
            String userName = "";
            if (environment_ == OS_AS400)
            {
                defaultValue = "localhost";
                AS400 tmpSystemObject = new AS400(); //allow diff logon-id as TC id
                userName = tmpSystemObject.getUserId();
                tmpSystemObject.close();
            }

            if (ds2.getServerName().equals(defaultValue) &&
                ds2.getUser().equals(userName) &&
                ds2.getDatabaseName().equals("") &&
                ds2.getDataSourceName().equals("") &&
                ds2.getDescription().equals("") &&
                ds2.getDateFormat().equals(dateFormat) &&
                ds2.isPackageCache())
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
    *  Validates that setLoginTimeout(int) throws an SQLException - Function not supported.
    **/
    public void Var119()
    {
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setLoginTimeout(120);
            if(ds.getLoginTimeout() == 120)
                succeeded();
            else
                failed("Unexpected results. " + ds.getLoginTimeout());
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
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setLogWriter(null);

            assertCondition(ds.getLogWriter() == null);
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
        if(JDTestDriver.getClientOS() == JDTestDriver.CLIENT_as400)
        {
            notApplicable("non i5 TC");
            return;
        }
        String jndiName = "javatest/test123";

        if (jndiType_ == JNDI_LDAP)
            jndiName = "cn=test123datasource, cn=users, ou=rochester,o=ibm,c=us";

	/* String databaseName = "QCUSTCDT"; */ 
        String serverName = systemObject_.getSystemName();
        String description = "Customer Database";

        try
        {
            // Create a data source to the AS/400 database.
            AS400JDBCManagedDataSource dataSource = new AS400JDBCManagedDataSource();
            dataSource.setServerName(serverName);
            /* dataSource.setDatabaseName(databaseName); */ 
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

            // Return an AS400JDBCManagedDataSource object from JNDI and get a connection.
            AS400JDBCManagedDataSource jndiDataSource = (AS400JDBCManagedDataSource) context_.lookup(jndiName);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            Connection connection = jndiDataSource.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

            if (rs.next() &&
                jndiDataSource.getServerName().equals(serverName) &&
                /* jndiDataSource.getDatabaseName().equals(databaseName) && */ 
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
        if(JDTestDriver.getClientOS() == JDTestDriver.CLIENT_as400)
        {
            notApplicable("non i5 TC");
            return;
        }
        String jndiName = "javatest/test124";

        if (jndiType_ == JNDI_LDAP)
            jndiName = "cn=test124datasource, cn=users, ou=rochester,o=ibm,c=us";

        try
        {
            // Create a data source to the AS/400 database.
            AS400JDBCManagedDataSource dataSource = new AS400JDBCManagedDataSource();

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

            // Return an AS400JDBCManagedDataSource object from JNDI and get a connection.
            AS400JDBCManagedDataSource jndiDataSource = (AS400JDBCManagedDataSource) context_.lookup(jndiName);
            jndiDataSource.setServerName(systemObject_.getSystemName());

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
        if(JDTestDriver.getClientOS() == JDTestDriver.CLIENT_as400)
        {
            notApplicable("non i5 TC");
            return;
        }
        String jndiName = "javatest/test125";

        if (jndiType_ == JNDI_LDAP)
            jndiName = "cn=test125datasource, cn=users, ou=rochester,o=ibm,c=us";

        try
        {
            // Create a data source to the AS/400 database.
            AS400JDBCManagedDataSource dataSource = new AS400JDBCManagedDataSource();
            dataSource.setServerName(systemObject_.getSystemName());
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

            // Return an AS400JDBCManagedDataSource object from JNDI and get a connection.
            AS400JDBCManagedDataSource jndiDataSource = (AS400JDBCManagedDataSource) context_.lookup(jndiName);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            jndiDataSource.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

            Connection connection = jndiDataSource.getConnection();

            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

            if (rs.next() &&
                jndiDataSource.getServerName().equals(systemObject_.getSystemName()) &&
                jndiDataSource.getUser().equals(systemObject_.getUserId()))
                succeeded();
            else
            {
                StringBuffer details = new StringBuffer("\n");
                if (!jndiDataSource.getServerName().equals(systemObject_.getSystemName()))
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
        if(JDTestDriver.getClientOS() == JDTestDriver.CLIENT_as400)
        {
            notApplicable("non i5 TC");
            return;
        }
        String jndiName = "javatest/test126";

        if (jndiType_ == JNDI_LDAP)
            jndiName = "cn=test126datasource, cn=users, ou=rochester,o=ibm,c=us";

        try
        {
            // Create a data source to the AS/400 database.
            AS400JDBCManagedDataSource dataSource = new AS400JDBCManagedDataSource();
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

            // Return an AS400JDBCManagedDataSource object from JNDI and get a connection.
            AS400JDBCManagedDataSource jndiDataSource = (AS400JDBCManagedDataSource) context_.lookup(jndiName);
            jndiDataSource.setServerName(systemObject_.getSystemName());
            jndiDataSource.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            jndiDataSource.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

            Connection connection = jndiDataSource.getConnection();

            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

            if (rs.next() &&
                jndiDataSource.getServerName().equals(systemObject_.getSystemName()) &&
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
        if(JDTestDriver.getClientOS() == JDTestDriver.CLIENT_as400)
        {
            notApplicable("non i5 TC");
            return;
        }
        String jndiName = "javatest/test127";

        if (jndiType_ == JNDI_LDAP)
            jndiName = "cn=test127datasource, cn=users, ou=rochester,o=ibm,c=us";

        try
        {
            // Create a data source to the AS/400 database.
            AS400JDBCManagedDataSource dataSource = new AS400JDBCManagedDataSource();
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

            // Return an AS400JDBCManagedDataSource object from JNDI and get a connection.
            AS400JDBCManagedDataSource jndiDataSource = (AS400JDBCManagedDataSource) context_.lookup(jndiName);
            jndiDataSource.setServerName(systemObject_.getSystemName());

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
    @SuppressWarnings("deprecation")
    public void Var128()
    {
        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter(logFileName));
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setLogWriter(writer);

            ds.setServerName(systemObject_.getSystemName());
            ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            ds.getConnection();

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
            ds.setPackageClear(true);
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

            assertCondition(ds.getLogWriter() == writer);
            writer.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that the constuctor AS400JDBCManagedDataSource(String) can be used to get
    *  a usable connection.
    **/
    public void Var129()
    {
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName());
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
        if(JDTestDriver.getClientOS() == JDTestDriver.CLIENT_as400)
        {
            notApplicable("non i5 TC");
            return;
        }
        String jndiName = "javatest/test130";

        if (jndiType_ == JNDI_LDAP)
            jndiName = "cn=test130datasource, cn=users, ou=rochester,o=ibm,c=us";

        try
        {
            // Create a data source to the AS/400 database.
            AS400JDBCManagedDataSource dataSource = new AS400JDBCManagedDataSource();
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

            // Return an AS400JDBCManagedDataSource object from JNDI and get a connection.
            AS400JDBCManagedDataSource jndiDataSource = (AS400JDBCManagedDataSource) context_.lookup(jndiName);
            jndiDataSource.setServerName(systemObject_.getSystemName());

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

            if (ds.getBidiStringType() == 5) //gregory changed to 5
                succeeded();
            else
                failed("Unexpected Results. " + ds.getBidiStringType());
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);

            ds.setFullOpen(true);

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
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
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
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
            Connection c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            String defaultValue = systemNameLocal_;

            //if (environment_ == OS_AS400) @pdd can run from i5 to i5
              //  defaultValue = "localhost";
            if (environment_ == OS_AS400) 
                defaultValue = systemObject_.getSystemName();//systemName_;//not always localhost on i5 "localhost";   //@C6A
            //ok, if running on i5, and using the hostname of something other than localhost, but still running
            //to the local i5, then you still get back "localhost", so use systemname from as400 object
         
            assertCondition (c.getMetaData().getURL().equals("jdbc:as400://" + defaultValue));
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
        try
        {
	    AS400JDBCManagedDataSource ds =
	      new AS400JDBCManagedDataSource(systemObject_.getSystemName(),

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
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(),
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
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setServerName(systemObject_.getSystemName());
            ds.setUser("garfield");
            ds.setPassword("garfield");
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
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setServerName(systemObject_.getSystemName());
            ds.setUser("garfield");
            ds.setPassword("garfield");
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
	notApplicable("SSLight Testcase");
    }

    /**
   *  Validates that setSecure(boolean) sets the value as expected -- fix bug with 
   *  getConnection(String, String).
   **/
    public void Var144()
    {
	notApplicable("SSLight Testcase");
    }

    // @C3 new method
    /**
    *  Validates that getExtendedMetaData() returns the correct default value.
    **/
    public void Var145()
    {
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
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
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);

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
        Connection c1  = null;
        Connection c2  = null;
        Statement  s1  = null;
        Statement  s2  = null;
        ResultSet  rs1 = null;
        ResultSet  rs2 = null;

        

        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setServerName(systemObject_.getSystemName());
            ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            ds.setSavePasswordWhenSerialized(true);              
            ds.setPrompt(false);

            c1  = ds.getConnection();
            s1  = c1.createStatement();
            rs1 = s1.executeQuery("select * from qiws.qcustcdt");

            if (rs1.next())
            {
                try { 
                    rs1.close();
                }
                catch (Exception e) {
                }
                try { 
                    s1.close();
                }
                catch (Exception e) {
                }
                try { 
                    c1.close();
                }
                catch (Exception e) {
                }
                rs1 = null;
                s1  = null;
                c1  = null;

                AS400JDBCManagedDataSource ds2 = serialize(ds);

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

        if (rs1 != null) try { 
                rs1.close();
            }
            catch (Exception e) {
            }
        if (rs2 != null) try { 
                rs2.close();
            }
            catch (Exception e) {
            }
        if (s1 != null) try { 
                s1.close();
            }
            catch (Exception e) {
            }
        if (s2 != null) try { 
                s2.close();
            }
            catch (Exception e) {
            }
        if (c1 != null) try { 
                c1.close();
            }
            catch (Exception e) {
            }
        if (c2 != null) try { 
                c2.close();
            }
            catch (Exception e) {
            }
    }




    /**
    *  Validates that by default the password is not serialized.
    **/
    public void Var149()
    {       
        // testcase does not work when on AS/400 because
        // the password is grabbed for the current job if we don't
        // specify it.  Just move on.
        //if (JDTestDriver.OSName_.equalsIgnoreCase("OS/400"))
        if (environment_ == OS_AS400) 
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
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setServerName(systemObject_.getSystemName());
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
                try { 
                    rs1.close();
                }
                catch (Exception e) {
                }
                try { 
                    s1.close();
                }
                catch (Exception e) {
                }
                try { 
                    c1.close();
                }
                catch (Exception e) {
                }
                rs1 = null;
                s1  = null;
                c1  = null;

                AS400JDBCManagedDataSource ds2 = serialize(ds);

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

        if (rs1 != null) try { 
                rs1.close();
            }
            catch (Exception e) {
            }
        if (s1 != null) try { 
                s1.close();
            }
            catch (Exception e) {
            }
        if (c1 != null) try { 
                c1.close();
            }
            catch (Exception e) {
            }
        if (c2 != null) try { 
                c2.close();
            }
            catch (Exception e) {
            }
    }








    /**
    *  @K1A
    *  Validates that setToolboxTraceCategory("jdbc") starts the JDBC tracing.
    **/
    public void Var150()
    {
        Connection c = null;

        AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
        try
        {
            previousWriter_ = DriverManager.getLogWriter();  
            ds.setToolboxTraceCategory("jdbc");
            writer_ = new PrintWriter(new FileWriter(testFile_));
            DriverManager.setLogWriter(writer_);

            ds.setServerName(systemObject_.getSystemName());
            ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds.setPassword(charPassword);

            c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            Statement s = c.createStatement();
            s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
	    writer_.flush();

            assertCondition (checkLog("password is set") &&     
                             checkLog("connection created"));   
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception:  Added 3/10/2003 by Toolbox to check toolbox trace property.");
        }
        finally
        {
            ds.setToolboxTraceCategory("none");
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
            DriverManager.setLogWriter(previousWriter_);
        }
    }


    /**
    *  Validates that getMinimumDivideScale() returns the correct default value.
    **/
    public void Var151()
    {
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

            if (ds.getMinimumDivideScale() == 0)  
                succeeded();
            else
                failed("Unexpected Results. " + ds.getMinimumDivideScale());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test setMinimumDivideScale(int).");
        }
    }


    /**
    *  Validates that getMinimimDivideScale(int) works.
    **/
    public void Var152()
    {
        int value = 4; 
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setMinimumDivideScale(value);

            if (ds.getMinimumDivideScale() == value) 
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getMinimumDivideScale().");
        }
    }


    /**
    *  Validates that setMinimumDivideScale(int) throws an ExtendedIllegalArgumentException if the property value is invalid.
    **/
    public void Var153()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            ds.setMinimumDivideScale(11);
            failed("Unexpected Results.");
        }
        catch (ExtendedIllegalArgumentException l)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMinimumDivideScale(int).");
        }
    }


    /**
    *  Validates that getMaximumPrecision() returns the correct default value.
    **/
    public void Var154()
    {
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

            if (ds.getMaximumPrecision() == 31)  
                succeeded();
            else
                failed("Unexpected Results. " + ds.getMinimumDivideScale());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getMaximumPrecision().");
        }
    }


    /**
    *  Validates that getMaximumPrecision() works.
    **/
    public void Var155()
    {
        int value = 63; 
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setMaximumPrecision(value);

            if (ds.getMaximumPrecision() == value) 
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getMaximumPrecision().");
        }
    }


    /**
    *  Validates that setMaximumPrecision(int) throws an ExtendedIllegalArgumentException if the property value is invalid.
    **/
    public void Var156()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            ds.setMaximumPrecision(11);
            failed("Unexpected Results.");
        }
        catch (ExtendedIllegalArgumentException l)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMaximumPrecision(int).");
        }
    }


    /**
    *  Validates that getMaximumPrecision() works.
    **/
    public void Var157()
    {
        int value = 31; 
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setMaximumPrecision(value);

            if (ds.getMaximumPrecision() == value) 
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getMaximumPrecision().");
        }
    }


    /**
    *  Validates that getMaximumScale() returns the correct default value.
    **/
    public void Var158()
    {
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

            if (ds.getMaximumScale() == 31)  
                succeeded();
            else
                failed("Unexpected Results. " + ds.getMinimumDivideScale());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getMaximumScale().");
        }
    }


    /**
    *  Validates that getMaximumScale() works.
    **/
    public void Var159()
    {
        int value = 63; 
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setMaximumScale(value);

            if (ds.getMaximumScale() == value) 
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getMaximumScale().");
        }
    }


    /**
    *  Validates that setMaximumScale(int) throws an ExtendedIllegalArgumentException if the property value is invalid.
    **/
    public void Var160()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            ds.setMaximumScale(111);
            failed("Unexpected Results.");
        }
        catch (ExtendedIllegalArgumentException l)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMaximumScale(int).");
        }
    }


    /**
    *  Validates that getMaximumScale() works.
    **/
    public void Var161()
    {
        int value = 5; 
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setMaximumScale(value);

            if (ds.getMaximumScale() == value) 
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getMaximumScale().");
        }
    }


    /**
       Validates that getConnection() returns a valid connection object.

       AS400JDBCManagedDataSource(AS400)
       setServerName()
       setUser()
       setPassword()
       getConnection()
    **/
    public void Var162()
    {
      notApplicable();
//      Connection c = null;
//      try
//      {
//        AS400 as400 = new AS400();
//        AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(as400);
//        ds.setServerName(systemObject_.getSystemName());
//        ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
//        ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
//
//        c = ds.getConnection();
//
//        assertCondition(c != null && !c.isClosed());
//      }
//      catch (Exception e)
//      {
//        failed(e, "Unexpected exception.");
//      }
//      finally
//      {
//        try
//        {
//          if (c != null)
//            c.close();
//        }
//        catch (SQLException sq)
//        {
//        }
//      }
    }


    /**
       Validates that getConnection() returns a valid connection object.

       AS400JDBCManagedDataSource()
       getConnection()
    **/
    public void Var163()
    {
        notApplicable();
//        Connection c = null;
//        try
//        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
//            AS400 as400 = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
//            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(as400);
//            c = ds.getConnection();
//
//            assertCondition(c != null && !c.isClosed());
//        }
//        catch (Exception e)
//        {
//            failed(e, "Unexpected exception.");
//        }
//        finally
//        {
//            try
//            {
//                if (c != null)
//                    c.close();
//            }
//            catch (SQLException sq)
//            {
//            }
//        }
    }


    /**
       Validates that getConnection(String,String) returns a valid connection object.

       AS400JDBCManagedDataSource(AS400)
       setServerName()
       getConnection(String,String)
    **/
    public void Var164()
    {
        notApplicable();
//        Connection c = null;
//        try
//        {
//            AS400 as400 = new AS400();
//            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(as400);
//            ds.setServerName(systemObject_.getSystemName());
//
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
//            c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
//
//            assertCondition(c != null && !c.isClosed());
//        }
//        catch (Exception e)
//        {
//            failed(e, "Unexpected exception.");
//        }
//        finally
//        {
//            try
//            {
//                if (c != null)
//                    c.close();
//            }
//            catch (SQLException sq)
//            {
//            }
//        }
    }


    /**
       Validates that getConnection(String,String) returns a valid connection object.

       AS400JDBCManagedDataSource(AS400)
       setServerName()
       getConnection(String,String)
    **/
    public void Var165()
    {
        notApplicable();
//        Connection c = null;
//        try
//        {
//   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
//            AS400 as400 = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
//            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(as400);
//
//            c = ds.getConnection(systemObject_.getUserId(), charPassword);
//   PasswordVault.clearPassword(charPassword);
//
//            assertCondition(c != null && !c.isClosed());
//        }
//        catch (Exception e)
//        {
//            failed(e, "Unexpected exception.");
//        }
//        finally
//        {
//            try
//            {
//                if (c != null)
//                    c.close();
//            }
//            catch (SQLException sq)
//            {
//            }
//        }
    }



    /**
       Validates that a second getConnection() returns a valid connection object.

       AS400JDBCManagedDataSource(AS400)
       getConnection()
    **/
    public void Var166()
    {
        notApplicable();
//        Connection c = null, c2 = null;
//        try
//        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
//            AS400 as400 = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
//            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(as400);
//            c = ds.getConnection();
//            c2 = ds.getConnection();
//
//            assertCondition (c != null && c2 != null && c != c2);
//        }
//        catch (Exception e)
//        {
//            failed(e, "Unexpected exception.");
//        }
//        finally
//        {
//            try
//            {
//                if (c != null)
//                    c.close();
//            }
//            catch (SQLException sq)
//            {
//            }
//            try
//            {
//                if (c2 != null)
//                    c2.close();
//            }
//            catch (SQLException sq)
//            {
//            }
//        }
    }


    /**
       Validates that a second getConnection(String,String) returns a valid connection object.

       AS400JDBCManagedDataSource(AS400)
       setServerName()
       getConnection(String,String)
    **/
    public void Var167()
    {
        notApplicable();
//        Connection c = null, c2 = null;
//        try
//        {
//            AS400 as400 = new AS400();
//            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(as400);
//            ds.setServerName(systemObject_.getSystemName());
//
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
//            c = ds.getConnection(systemObject_.getUserId(), charPassword);
//            c2 = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
//
//            assertCondition(c != null && c2 != null && c != c2);
//        }
//        catch (Exception e)
//        {
//            failed(e, "Unexpected exception.");
//        }
//        finally
//        {
//            try
//            {
//                if (c != null)
//                    c.close();
//            }
//            catch (SQLException sq)
//            {
//            }
//            try
//            {
//                if (c2 != null)
//                    c2.close();
//            }
//            catch (SQLException sq)
//            {
//            }
//        }
    }


    /**
    *  Validates that getPackageCCSID() returns the correct default value.
    **/
    public void Var168()
    {
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

            if (ds.getPackageCCSID() == 13488)  
                succeeded();
            else
                failed("Unexpected Results. " + ds.getPackageCCSID());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getPackageCCSID().");
        }
    }


    /**
    *  Validates that getPackageCCSID() works.
    **/
    public void Var169()
    {
        int value = 1200; 
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setPackageCCSID(value);

            if (ds.getPackageCCSID() == value) 
                succeeded();
            else
                failed("Unexpected Results." + ds.getPackageCCSID());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getPackageCCSID().");
        }
    }


    /**
    *  Validates that setPackageCCSID(int) throws an ExtendedIllegalArgumentException if the property value is invalid.
    **/
    public void Var170()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            ds.setPackageCCSID(111);
           succeeded("ok with new BIDI support");
        }
        
        catch (Exception e)
        {
            failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setPackageCCSID(int).");
        }
    }


    /**
    *  Validates that getPackageCCSID() works.
    **/
    public void Var171()
    {
        int value = 13488; 
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setPackageCCSID(value);

            if (ds.getPackageCCSID() == value) 
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getPackageCCSID().");
        }
    }


    /**
    *  Validates that getTranslateHex() returns the correct default value.
    **/
    public void Var172()
    {
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

            if (ds.getTranslateHex().equals("character"))  
                succeeded();
            else
                failed("Unexpected Results. " + ds.getTranslateHex());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getTranslateHex().");
        }
    }


    /**
    *  Validates that getTranslateHex() works.
    **/
    public void Var173()
    {
        String value = "binary"; 
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setTranslateHex(value);

            if (ds.getTranslateHex().equals(value)) 
                succeeded();
            else
                failed("Unexpected Results." + ds.getTranslateHex());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getTranslateHex().");
        }
    }


    /**
    *  Validates that setTranslateHex(String) throws an ExtendedIllegalArgumentException if the property value is invalid.
    **/
    public void Var174()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            ds.setTranslateHex("hello");
            failed("Unexpected Results.");
        }
        catch (ExtendedIllegalArgumentException l)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setPackageCCSID(int).");
        }
    }


    /**
    *  Validates that getTranslateHex() works.
    **/
    public void Var175()
    {
        String value = "character"; 
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setTranslateHex(value);

            if (ds.getTranslateHex().equals(value)) 
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getTranslateHex()");
        }
    }

    /**
    *  Validates that setLoginTimeout(int) works.
    **/
    public void Var176()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            ds.setLoginTimeout(5);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setPackageCCSID(int).");
        }
    }

    /**
    *  Validates that getLoginTimeout() works.
    **/
    public void Var177()
    {
        int value = 6; 
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setLoginTimeout(value);

            if (ds.getLoginTimeout() == value) 
                succeeded();
            else
                failed("Unexpected Results.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getTranslateHex()");
        }
    }

    /**
    *  Validates that setLoginTimeout sets the soTimeout property.  Now separate props
    **/
    public void Var178()
    {
        int value = 6; 
        try
        {
            AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
            ds.setLoginTimeout(value);

            if (ds.getLoginTimeout() == value && ds.getSoTimeout() != value*1000) 
                succeeded();
            else
                failed("Unexpected Results. value is " + value + " and sb 6, soTimeout is " + ds.getSoTimeout() + " and should be 6000");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception:  Added 3/26/2003 by Toolbox to test getTranslateHex()");
        }
    }
    


    /**
    *  setSort() to test toolbox ptf to not throw exception if sort=job, but to use default
    *  
    **/
    public void Var179()
    {
        String added = " toolbox ptf test for setSort() added 02/12/2007";

        try
        {
           
                AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
               
                ds.setSort("job");

                succeeded();
            
        } catch (Exception e)
        {
            failed(e, "Unexpected Exception " + added);
        }
        
    }

   /**
      Validates that a getConnection() when no pooled connections are available and the 'enforceMaxPoolSize' property is not set, creates a new connection.

      AS400JDBCManagedConnectionPoolDataSource()
      setServerName()
      setMaxPoolSize()
      getConnection(String,String)
   **/
   public void Var180()
   {


     Connection c = null;
     Connection c2 = null;
     AS400JDBCManagedDataSource mds0 = null;

     try
     {
       AS400JDBCManagedConnectionPoolDataSource cpds0 = new AS400JDBCManagedConnectionPoolDataSource();

       // Set general datasource properties.  Note that both CPDS and MDS have these properties, and they might have different values.
       cpds0.setServerName(systemObject_.getSystemName());
       // Don't set the database name.  This is the IASP name 
       // cpds0.setDatabaseName(systemObject_.getSystemName());
       cpds0.setUser(systemObject_.getUserId());
       char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
       cpds0.setPassword(charPassword);



       cpds0.setSavePasswordWhenSerialized(true);  // eliminates the password prompt

       // Set connection pooling-specific properties.
       cpds0.setInitialPoolSize(1);
       cpds0.setMinPoolSize(1);
       cpds0.setMaxPoolSize(1);
       //JDReflectionUtil.callMethod_V(cpds0,"setEnforceMaxPoolSize",false);  // don't enforce size limitation of pool

       // Set the initial context factory to use.
       System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
       System.setProperty(Context.PROVIDER_URL,"file:.");  // for linux authority. default is "/" location of file created

       // Get the JNDI Initial Context.
       Context ctx = new InitialContext();
       ctx.rebind("mydatasource", cpds0);  // We can now do lookups on cpds, by the name "mydatasource".

       // Create a standard DataSource object that references it.

       mds0 = new AS400JDBCManagedDataSource();
       mds0.setDescription("DataSource supporting statement pooling");
       mds0.setDataSourceName("mydatasource"); // point it to cpds0
       ctx.rebind("StatementPoolingDataSource", mds0);

       AS400JDBCManagedDataSource ds = (AS400JDBCManagedDataSource)ctx.lookup("StatementPoolingDataSource");

       // Get a connection from the pool.
       c = ds.getConnection(systemObject_.getUserId(), charPassword);

       // Try to get another connection from the pool.
       c2 = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

       if (c != null && c2 != null)
         succeeded();
       else
         failed("Unexpected results.");
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }
     finally
     {
       if (c != null) {
         try { c.close(); } catch (Exception e) { e.printStackTrace(); }
       }
       if (c2 != null) {
         try { c2.close(); } catch (Exception e) { e.printStackTrace(); }
       }
       if (mds0 != null) {
         try { mds0.closePool(); } catch (Exception e) { e.printStackTrace(); }
       }
     }
   }

   /**
      Validates that a getConnection() when no pooled connections are available and the 'enforceMaxPoolSize' property is set, throws an exception.

      AS400JDBCManagedConnectionPoolDataSource()
      setServerName()
      setMaxPoolSize()
      setEnforceMaxPoolSize()
      getConnection(String,String)
   **/
   public void Var181()
   {


     Connection c = null;
     Connection c2 = null;
     Connection c3 = null;
     AS400JDBCManagedDataSource mds0 = null;
     boolean ok = true;

     try
     {
       AS400JDBCManagedConnectionPoolDataSource cpds0 = new AS400JDBCManagedConnectionPoolDataSource();

       // Set general datasource properties.  Note that both CPDS and MDS have these properties, and they might have different values.
       cpds0.setServerName(systemObject_.getSystemName());
       // The database is the IASP name.  Don't set it. 
       // cpds0.setDatabaseName(systemObject_.getSystemName());
       cpds0.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
       cpds0.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

       cpds0.setSavePasswordWhenSerialized(true);  // eliminates the password prompt

       // Set connection pooling-specific properties.
       cpds0.setInitialPoolSize(2);
       cpds0.setMinPoolSize(2);
       cpds0.setMaxPoolSize(2);
       JDReflectionUtil.callMethod_V(cpds0,"setEnforceMaxPoolSize",true);  // enforce size limitation of pool

       // Set the initial context factory to use.
       System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
       System.setProperty(Context.PROVIDER_URL,"file:.");  // for linux authority. default is "/" location of file created

       // Get the JNDI Initial Context.
       Context ctx = new InitialContext();
       ctx.rebind("mydatasource", cpds0);  // We can now do lookups on cpds, by the name "mydatasource".

       // Create a standard DataSource object that references it.

       mds0 = new AS400JDBCManagedDataSource();
       mds0.setDescription("DataSource supporting statement pooling");
       mds0.setDataSourceName("mydatasource"); // point it to cpds0
       ctx.rebind("StatementPoolingDataSource", mds0);

       AS400JDBCManagedDataSource ds =(AS400JDBCManagedDataSource)ctx.lookup("StatementPoolingDataSource");

       // Get a connection from the pool.
       c = ds.getConnection(systemObject_.getUserId(), charPassword);

       c2 = ds.getConnection(systemObject_.getUserId(), charPassword);

       // Try to get another connection from the pool.
       try {
         c3 = ds.getConnection(systemObject_.getUserId(), charPassword);
         System.out.println("Failed to throw exception.");
         ok = false;
       }
       catch(Exception e) {
         if (!(e instanceof SQLException)) {
           System.out.println("Exception is not an SQLException: " + e.getMessage());
           ok = false;
         }
       }

       // Cleanup third connection if needed.
       if (c3 != null) {
         try { c2.close(); } catch (Exception e) { e.printStackTrace(); }
         c3 = null;
       }

       // Return the first connection to the pool.
       c.close();
       c = null; 

       c3 = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

       if (ok && c3 != null)
         succeeded();
       else
         failed("Unexpected results.");
     }
     catch(Exception e)
     {
       failed(e, "Unexpected exception.");
     }
     finally
     {
       if (c != null) {
         try { c.close(); } catch (Exception e) { e.printStackTrace(); }
       }
       if (c3 != null) {
         try { c3.close(); } catch (Exception e) { e.printStackTrace(); }
       }
       if (c2 != null) {
         try { c2.close(); } catch (Exception e) { e.printStackTrace(); }
       }
       if (mds0 != null) {
         try { mds0.closePool(); } catch (Exception e) { e.printStackTrace(); }
       }
     }
   }


   /**
   *  Validates that setBlockSize(int) sets the block size as expected.
   **/
   public void Var182()
   {
       int[] values = {513,600,800,1000,8000,10000,16000};
       try
       {
           AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();

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
   public void Var183()
   {
       try
       {
           AS400JDBCManagedDataSource ds = new AS400JDBCManagedDataSource();
           ds.setBlockSize(16001);

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
      Serializes and deserializes the data source object.
     **/
    private AS400JDBCManagedDataSource serialize (AS400JDBCManagedDataSource ds) throws Exception
    {
        // Serialize.
        String serializeFileName = logDirectory_ + "/datasource.ser";
        ObjectOutput out = new ObjectOutputStream (new FileOutputStream (serializeFileName));
        out.writeObject (ds);
        out.flush ();
        out.close();

        // Deserialize.
        AS400JDBCManagedDataSource ds2 = null;
        try
        {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFileName));
            ds2 = (AS400JDBCManagedDataSource) in.readObject ();
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
