///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCMCPDSTestcase.java
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
import javax.naming.*;
import javax.sql.PooledConnection;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCManagedConnectionPoolDataSource;
import com.ibm.as400.access.AS400JDBCPooledConnection;
import com.ibm.as400.access.Trace;

import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;

/**
  Testcase AS400JDBCMCPDSTestcase.
 **/
public class AS400JDBCMCPDSTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "AS400JDBCMCPDSTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.AS400JDBCDataSourceTest.main(newArgs); 
   }
   private PrintWriter writer_ = null;
   private String filename_ = "/tmp/poolds.log";
   // private String tag = null;
   // private boolean rejectChange = false;

   // JNDI variables.
   private static final String JNDI_FILE = "com.sun.jndi.fscontext.RefFSContextFactory";
   private static final String JNDI_LDAP = "com.ibm.jndi.LDAPCtxFactory";

   private Context context_;
   private String jndiType_ = JNDI_FILE;
   private String ldapUsr_ = null;
   private String ldapPwd_ = null;
   private static final String logDirectory_ = "javatest";
   private static File javatest_;
   private String systemName1_ = null;
   private boolean traceValueOld_;  // original value
   private boolean traceJdbcValueOld_;  // original value

   // Environment variables.
   private static final int OS_AS400 = 0;
   private static final int OS_WINDOWS = 1;
   private int environment_ = -1;

   /**
   *  Creates a printwriter.
    @exception  Exception  If an exception occurs.
   **/
   protected void setup() throws Exception
   {
     // Determine the environment.
     String os = System.getProperty("os.name");
     System.out.println("Environment: " + os);

     if (os.indexOf("OS/400") >= 0)
	environment_ = OS_AS400;
     else if (JTOpenTestEnvironment.isWindows)
     	environment_ = OS_WINDOWS;

     if (!isApplet_)
     {
	// Make sure javatest dir exists.
	javatest_ = new File(logDirectory_);
	if (!javatest_.exists())
	{
	    System.out.println("Setup is creating '" + logDirectory_ + "' directory.");

	    if(!javatest_.mkdir())
	    {
		System.out.println("WARNING:  Setup could not create the '" + logDirectory_ + "' directory.");
	    }
	}

	try
	{
	    writer_ = new PrintWriter(new FileWriter(filename_));
	}
	catch (Exception e)
	{
	    System.out.println("Setup failed for "+filename_+".\n");
	    e.printStackTrace();
	}
     }

     if (!isApplet_ || (isApplet_ && jndiType_ != JNDI_FILE))
     {
	// Get the JNDI Initial Context.
	Properties env = new Properties();

	if (jndiType_ == JNDI_LDAP)
	{
	  env.put("java.naming.provider.url", "ldap://" + systemObject_.getSystemName());
	  env.put("java.naming.security.principal", "cn=" + ldapUsr_);
	  env.put("java.naming.security.credentials", ldapPwd_);
	}
	else
    {
     	env.put(Context.PROVIDER_URL,"file:.");  //@pda for linux authority. default is "/" location of file created
    }

	env.put(Context.INITIAL_CONTEXT_FACTORY, jndiType_);
	context_ = new InitialContext(env);
     }
   }

   /**
   *  Returns the contents of the log file.
   *  @param lineNumber The line to be returned (1-based).
   **/
   private String getContents(int lineNumber)
   {
      String line = null;
      try
      {
         BufferedReader reader = new BufferedReader(new FileReader(filename_));

         for (int i=0; i< lineNumber; i++)
            line = reader.readLine();

         reader.close();
      }
      catch (Exception e)
      {
      }
      return line;
   }

   /**
     Constructor.  This is called from the AS400JDBCDataSourceTest constructor.
    **/
   public AS400JDBCMCPDSTestcase(AS400 systemObject,
                                 Vector<String> variationsToRun,
                                 int runMode,
                                 FileOutputStream fileOutputStream,
                                 
                                 String password,
				             String jndiType,
			                  String ldapUsr,
				             String ldapPwd,
                                 String systemName)

   {
      super(systemObject, "AS400JDBCMCPDSTestcase", variationsToRun,
            runMode, fileOutputStream, password);
      if (jndiType.equals("file"))
         jndiType_ = JNDI_FILE;
      else if (jndiType.equals("ldap"))
         jndiType_ = JNDI_LDAP;
      else
         System.out.println("WARNING... Unknown jndi type '" + jndiType + "' using default.");

      ldapUsr_ = ldapUsr;
      ldapPwd_ = ldapPwd;
      systemName1_ = systemName;

      traceValueOld_ = Trace.isTraceOn();
      traceJdbcValueOld_ = Trace.isTraceJDBCOn();
   }

   /**
      Validates that getConnection() returns a valid connection object.

      AS400JDBCManagedConnectionPoolDataSource()
      setServerName()
      setUser()
      setPassword()
      getConnection()
   **/
   @SuppressWarnings("deprecation")
  public void Var001()
   {
      Connection c = null;
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         ds.setServerName(systemObject_.getSystemName());
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         c= ds.getConnection();

         if (c != null)
            succeeded();
         else
            failed("Unexpected Results.");
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         try
         {
            if (c != null) c.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that getConnection() returns a valid connection object.

      AS400JDBCManagedConnectionPoolDataSource(String,String,String)
      getConnection()
   **/
   @SuppressWarnings("deprecation")
   public void Var002()
   {
      Connection c = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
         c= ds.getConnection();

         if (c != null)
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
         try
         {
            if (c != null) c.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that getConnection(String,String) returns a valid connection object.

      AS400JDBCManagedConnectionPoolDataSource()
      setServerName()
      getConnection(String,String)
   **/
   public void Var003()
   {
      Connection c = null;
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         ds.setServerName(systemObject_.getSystemName());

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

         if (c != null)
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
         try
         {
	     if (c != null) c.close();
         }
         catch (Exception s) {}
      }
   }

   /**
      Validates that getConnection(String,String) returns a valid connection object.

      AS400JDBCManagedConnectionPoolDataSource(String,String,String)
      setServerName()
      getConnection(String,String)
   **/
   public void Var004()
   {
      Connection c = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
         c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

         if (c != null)
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
         try
         {
            if (c != null) c.close();
         }
         catch (SQLException s) {}
      }
   }


   /**
      Validates that a second getConnection() returns a valid connection object.

      AS400JDBCManagedConnectionPoolDataSource(String,String,String)
      getConnection()
   **/
   @SuppressWarnings("deprecation")
  public void Var005()
   {
      Connection c = null;
      Connection c2 = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
         c= ds.getConnection();
         c2 = ds.getConnection();
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
         try
         {
            if (c != null) c.close();
            if (c2 != null) c2.close();
         }
         catch (SQLException s) {}
      }

   }

   /**
      Validates that a second getConnection(String,String) returns a valid connection object.

      AS400JDBCManagedConnectionPoolDataSource()
      setServerName()
      getConnection(String,String)
   **/
   public void Var006()
   {
      Connection c = null;
      Connection c2 = null;
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         ds.setServerName(systemObject_.getSystemName());

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         c = ds.getConnection(systemObject_.getUserId(), charPassword);
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
         try
         {
            if (c != null) c.close();
            if (c2 != null) c2.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that a second getConnection(String,String) returns a valid connection object.

      AS400JDBCManagedConnectionPoolDataSource(String,String,String)
      getConnection()
   **/
   @SuppressWarnings("deprecation")
  public void Var007()
   {
      Connection c = null;
      Connection c2 = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
         c= ds.getConnection();
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
         try
         {
            if (c != null) c.close();
            if (c2 != null) c2.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that a second getConnection() returns a valid connection object.

      AS400JDBCManagedConnectionPoolDataSource()
      setServerName()
      getConnection(String,String)
   **/
   @SuppressWarnings("deprecation")
   public void Var008()
   {
      Connection c = null;
      Connection c2 = null;
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         ds.setServerName(systemObject_.getSystemName());

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         c = ds.getConnection(systemObject_.getUserId(), charPassword);

         ds.setUser(systemObject_.getUserId());
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
         c2 = ds.getConnection();

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
         try
         {
            if (c != null) c.close();
            if (c2 != null) c2.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that getConnection() prompts for a system name.

      AS400JDBCManagedConnectionPoolDataSource()
      setUser()
      setPassword()
      getConnection()
   **/
   @SuppressWarnings("deprecation")
  public void Var009(int runMode)
   {
      
       if (runMode != BOTH && runMode != ATTENDED) 
       {
            notApplicable("runMode not SET to run");
            return;
       }

      Connection c = null;
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         c= ds.getConnection();

         succeeded();
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         try
         {
            if (c != null) c.close();
         }
         catch (SQLException s) {}
      }
   }


   /**
      Validates that getConnection() prompts for a user.

      AS400JDBCManagedConnectionPoolDataSource()
      setServerName()
      setPassword()
      getConnection()
   **/
   @SuppressWarnings("deprecation")
  public void Var010(int runMode)
   {
       if (runMode != BOTH && runMode != ATTENDED) 
       {
            notApplicable("runMode not SET to run");
            return;
       }

      Connection c = null;
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         ds.setServerName(systemObject_.getSystemName());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         c= ds.getConnection();

         succeeded();
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         try
         {
            if (c != null) c.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that getConnection() prompts for a password

      AS400JDBCManagedConnectionPoolDataSource()
      setServerName()
      setUser()
      getConnection()
   **/
   @SuppressWarnings("deprecation")
  public void Var011(int runMode)
   {
       if (runMode != BOTH && runMode != ATTENDED) 
       {
            notApplicable("runMode not SET to run");
            return;
       }

      Connection c = null;
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         AS400.clearPasswordCache();
         ds.setUser(systemObject_.getUserId());
         ds.setServerName(systemObject_.getSystemName());

         c= ds.getConnection();

         succeeded();
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         try
         {
            if (c != null) c.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that getConnection() prompts for a user and password.

      AS400JDBCManagedConnectionPoolDataSource()
      setServerName()
      setPassword()
      getConnection()
   **/
   @SuppressWarnings("deprecation")
  public void Var012(int runMode)
   {
       if (runMode != BOTH && runMode != ATTENDED) 
       {
            notApplicable("runMode not SET to run");
            return;
       }

      Connection c = null;
      Connection c2 = null;
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         ds.setServerName(systemObject_.getSystemName());

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

         c2 = ds.getConnection();


         succeeded();
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         try
         {
            if (c != null) c.close();
            if (c2 != null) c2.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
   *  Validates that getAccess() returns the correct default value.
   **/
   public void Var013()
   {
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();

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
      String[] values = {"all", "read call", "read only" };
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
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
   *  Validates that getPooledConnection() returns a valid pooled connection.
   **/
   public void Var015()
   {
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         if (pc != null)
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
   *  Validates that getPooledConnection(String,String) returns a valid pooled connection.
   **/
   public void Var016()
   {
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         ds.setServerName(systemObject_.getSystemName());

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

         if (pc != null)
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
   *  Validates that serialization works on the default object.
   **/
   public void Var017()
   {
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();

         AS400JDBCManagedConnectionPoolDataSource ds2 = serialize(ds);

	 String userName = "";
	 if (environment_ == OS_AS400)
	 {
	     AS400 tmpSystemObject = new AS400(); //allow diff logon-id as TC id
	     userName = tmpSystemObject.getUserId();
	     tmpSystemObject.close();
	 }
	 
         System.out.println(ds2.getServerName() +"; "+ ds2.getUser() +"; "+ ds2.getDatabaseName() +"; "+ ds2.getDataSourceName() +"; "+ ds2.getDescription());

         if ((ds2.getServerName().equals("") || ds2.getServerName().equals("localhost")) &&
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
   *  Validates that serialization works as expected with the serverName set.
   **/
   public void Var018()
   {
      String serverName = "mySystem";
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         ds.setServerName(serverName);

         AS400JDBCManagedConnectionPoolDataSource ds2 = serialize(ds);

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
   public void Var019()
   {
      String user = "myUser";
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         ds.setUser(user);

         AS400JDBCManagedConnectionPoolDataSource ds2 = serialize(ds);

         if ((ds2.getServerName().equals("") || ds2.getServerName().equals("localhost")) &&
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
   public void Var020()
   {
      String databaseName = "myDatabase";
      String dataSourceName = "myDataSource";
      String description = "myDescription";
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         ds.setDatabaseName(databaseName);
         ds.setDataSourceName(dataSourceName);
         ds.setDescription(description);

         AS400JDBCManagedConnectionPoolDataSource ds2 = serialize(ds);

	 String userName = "";
	 if (environment_ == OS_AS400)
	 {
	     AS400 tmpSystemObject = new AS400(); //allow diff logon-id as TC id
         userName = tmpSystemObject.getUserId();
         tmpSystemObject.close();
	 }
	 
         if ((ds2.getServerName().equals("") || ds2.getServerName().equals("localhost")) &&
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
   public void Var021()
   {
      String dateFormat = "ymd";
      boolean packageCache = true;
      try
      {
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource();
         ds.setDateFormat(dateFormat);


         ds.setPackageCache(packageCache);

         AS400JDBCManagedConnectionPoolDataSource ds2 = serialize(ds);

	 String userName = "";
	 if (environment_ == OS_AS400)
	 {
	     AS400 tmpSystemObject = new AS400(); //allow diff logon-id as TC id
	     userName = tmpSystemObject.getUserId();
	     tmpSystemObject.close();
	 }
	 
         if ((ds2.getServerName().equals("") || ds2.getServerName().equals("localhost")) &&
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
   *  Validates that a datasource object can be set into JNDI,
   *  and then instantiated from JNDI.
   **/
   public void Var022()
   {
       if (environment_ == OS_AS400 )
       {
           notApplicable("todo when time permits.");
           return;
       }
     
      if (isApplet_ && jndiType_ == JNDI_FILE)
      {
              notApplicable("Running in applet and jndiType is file");
              return;
      }

      String jndiName = logDirectory_ + "/test120";

      if (jndiType_ == JNDI_LDAP)
	jndiName = "cn=test120datasource, cn=users, ou=rochester,o=ibm,c=us";

      /* databaseName is IASP name */ 
      /* String databaseName = "QCUSTCDT"; */ 
      String serverName = systemObject_.getSystemName();
      String description = "Customer Database";

      try
      {
         // Create a data source to the AS/400 database.
         AS400JDBCManagedConnectionPoolDataSource dataSource = new AS400JDBCManagedConnectionPoolDataSource();
         dataSource.setServerName(serverName);
	 /* dataSource.setDatabaseName(databaseName); */ 
         dataSource.setDescription(description);

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
         AS400JDBCManagedConnectionPoolDataSource jndiDataSource = (AS400JDBCManagedConnectionPoolDataSource) context_.lookup(jndiName);
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

         // cleanup.
         context_.unbind(jndiName);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }


   /**
   *  Validates that a default datasource object can be set into JNDI,
   *  and then instantiated from JNDI.
   **/
   public void Var023()
   {
       if (environment_ == OS_AS400 )
       {
           notApplicable("todo when time permits.");
           return;
       }
      if (isApplet_ && jndiType_ == JNDI_FILE)
      {
              notApplicable("Running in applet and jndiType is file");
              return;
      }

      String jndiName = logDirectory_ + "/test121";

      if (jndiType_ == JNDI_LDAP)
	jndiName = "cn=test121datasource, cn=users, ou=rochester,o=ibm,c=us";

      try
      {
         // Create a data source to the AS/400 database.
         AS400JDBCManagedConnectionPoolDataSource dataSource = new AS400JDBCManagedConnectionPoolDataSource();

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
         AS400JDBCManagedConnectionPoolDataSource jndiDataSource = (AS400JDBCManagedConnectionPoolDataSource) context_.lookup(jndiName);
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

         context_.unbind(jndiName);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that a datasource object with serverName and user can be created set into JNDI,
   *  and then instantiated from JNDI.
   **/
   @SuppressWarnings("deprecation")
  public void Var024()
   {
       if (environment_ == OS_AS400 )
       {
           notApplicable("todo when time permits.");
           return;
       }
      if (isApplet_ && jndiType_ == JNDI_FILE)
      {
              notApplicable("Running in applet and jndiType is file");
              return;
      }

      String jndiName = logDirectory_ + "/test122";

      if (jndiType_ == JNDI_LDAP)
	jndiName = "cn=test122datasource, cn=users, ou=rochester,o=ibm,c=us";

      try
      {
         // Create a data source to the AS/400 database.
         AS400JDBCManagedConnectionPoolDataSource dataSource = new AS400JDBCManagedConnectionPoolDataSource();
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

         // Return an AS400JDBCDataSource object from JNDI and get a connection.
         AS400JDBCManagedConnectionPoolDataSource jndiDataSource = (AS400JDBCManagedConnectionPoolDataSource) context_.lookup(jndiName);
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

         context_.unbind(jndiName);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }


   /**
   *  Validates that a datasource object with serverName and user can be set into JNDI,
   *  and then instantiated from JNDI and finally used to get a connection after changing properies.
   **/
   @SuppressWarnings("deprecation")
   public void Var025()
   {
       if (environment_ == OS_AS400 )
       {
           notApplicable("todo when time permits.");
           return;
       }
      if (isApplet_ && jndiType_ == JNDI_FILE)
      {
              notApplicable("Running in applet and jndiType is file");
              return;
      }

      String jndiName = logDirectory_ + "/test123";

      if (jndiType_ == JNDI_LDAP)
	jndiName = "cn=test123datasource, cn=users, ou=rochester,o=ibm,c=us";

      try
      {
         // Create a data source to the AS/400 database.
         AS400JDBCManagedConnectionPoolDataSource dataSource = new AS400JDBCManagedConnectionPoolDataSource();
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
         AS400JDBCManagedConnectionPoolDataSource jndiDataSource = (AS400JDBCManagedConnectionPoolDataSource) context_.lookup(jndiName);
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

         context_.unbind(jndiName);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }


   /**
   *  Validates that a datasource object with serverName and user can be set into JNDI,
   *  and then instantiated from JNDI and finally used to get a connection after changing properies.
   **/
   public void Var026()
   {
       if (environment_ == OS_AS400 )
       {
           notApplicable("todo when time permits.");
           return;
       }
      if (isApplet_ && jndiType_ == JNDI_FILE)
      {
              notApplicable("Running in applet and jndiType is file");
              return;
      }

      String jndiName = logDirectory_ + "/test124";

      if (jndiType_ == JNDI_LDAP)
	jndiName = "cn=test124datasource, cn=users, ou=rochester,o=ibm,c=us";

      try
      {
         // Create a data source to the AS/400 database.
         AS400JDBCManagedConnectionPoolDataSource dataSource = new AS400JDBCManagedConnectionPoolDataSource();
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
         AS400JDBCManagedConnectionPoolDataSource jndiDataSource = (AS400JDBCManagedConnectionPoolDataSource) context_.lookup(jndiName);
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

         context_.unbind(jndiName);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that the setLogWriter(PrintWriter) works as expected.
   **/
   public void Var027()
   {
      if (isApplet_)
      {
              notApplicable("Running in applet");
              return;
      }
      PooledConnection c = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
         ds.setLogWriter(writer_);

         // Informational msgs don't get logged unless tracing is on.
         Trace.setTraceJDBCOn(true);
         Trace.setTraceOn(true);

         c = ds.getPooledConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

         // Restore original settings.
         Trace.setTraceOn(traceValueOld_);
         Trace.setTraceJDBCOn(traceJdbcValueOld_);

         assertCondition(getContents(2).indexOf("PooledConnection created") != -1);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         try
         {
            if (c != null) c.close();
         }
         catch (Exception cl)
         {
         }
      }
   }

   /**
   *  Validates that the setLogWriter(PrintWriter) works as expected.
   **/
   public void Var028()
   {
      if (isApplet_)
      {
              notApplicable("Running in applet");
              return;
      }
      PooledConnection c = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
         ds.setLogWriter(writer_);

         // Informational msgs don't get logged unless tracing is on.
         Trace.setTraceJDBCOn(true);
         Trace.setTraceOn(true);

         c = ds.getPooledConnection();

         // Restore original settings.
         Trace.setTraceOn(traceValueOld_);
         Trace.setTraceJDBCOn(traceJdbcValueOld_);

         assertCondition(getContents(2).indexOf("PooledConnection created") != -1);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         try
         {
            if (c != null) c.close();
         }
         catch (Exception cl)
         {
         }
      }
   }



   /**
      Validates that getConnection().getMetaData() returns a valid database meta data object.

      AS400JDBCManagedConnectionPoolDataSource(String,String,String)
      getConnection()
   **/
   public void Var029()
   {
      Connection c = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCManagedConnectionPoolDataSource ds = new AS400JDBCManagedConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
         c= ds.getPooledConnection().getConnection();
         DatabaseMetaData dmd = c.getMetaData();
         assertCondition (dmd.getDriverName().equals("AS/400 Toolbox for Java JDBC Driver"));
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         try
         {
            if (c != null) c.close();
         }
         catch (SQLException s) {}
      }
   }


   // @B1A Fix to method, before it was only returning jdbc:as400://. P9951301
   /**
      Validates that getConnection().getMetaData().getURL returns a correct URL.

      AS400JDBCManagedConnectionPoolDataSource(String,String,String)
      getConnection()
   **/
   public void Var030()
   {
        try
        {
            
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
             AS400JDBCManagedConnectionPoolDataSource cpds = new AS400JDBCManagedConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);

             Connection c = cpds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

             String defaultValue = systemName1_;
             
             //if (environment_ == OS_AS400) //@pdd can run from i5 to i5 not localhost
               // defaultValue = "localhost";
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


   /**
     Performs cleanup needed after running testcases.
    @exception  Exception  If an exception occurs.
    **/
   protected void cleanup() throws Exception
   {
      try
      {
	 writer_.close();
         File file = new File(filename_);
         if (!file.delete())
	     System.out.println("WARNING... testcase cleanup could not delete: " + filename_);
	 if (!javatest_.delete())
             System.out.println("WARNING... testcase cleanup could not delete: " + logDirectory_);
      }
      catch (Exception e)
      {
         System.out.println("Cleanup failed:\n");
         e.printStackTrace();
      }
   }

   /**
     Serializes and deserializes the data source object.
    **/
   private AS400JDBCManagedConnectionPoolDataSource serialize (AS400JDBCManagedConnectionPoolDataSource ds) throws Exception
   {
      // Serialize.
      String serializeFileName = "datasource.ser";
      ObjectOutput out = new ObjectOutputStream (new FileOutputStream (serializeFileName));
      out.writeObject (ds);
      out.flush ();

      // Deserialize.
      AS400JDBCManagedConnectionPoolDataSource ds2 = null;
      try {
         ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFileName));
         ds2 = (AS400JDBCManagedConnectionPoolDataSource) in.readObject ();
	 in.close();
      }
      finally {
   // out cannot be null at this location 
	 out.close();
	 File f = new File (serializeFileName);
	 if (!f.delete())
            System.out.println("WARNING... testcase cleanup could not delete: " + serializeFileName);
      }
      return ds2;
   }
}
