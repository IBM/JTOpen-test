///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCConnectionPoolDataSourceTestcase.java
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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.sql.PooledConnection;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;
import com.ibm.as400.access.AS400JDBCConnectionPoolDataSource;
import com.ibm.as400.access.AS400JDBCPooledConnection;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;

/**
  Testcase AS400JDBCConnectionPoolDataSourceTestcase.
 **/
public class AS400JDBCConnectionPoolDataSourceTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "AS400JDBCConnectionPoolDataSourceTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.AS400JDBCDataSourceTest.main(newArgs); 
   }
   private PrintWriter writer_ = null;
   private String filename_ = "/tmp/poolds.log";

   // JNDI variables.
   private static final String JNDI_FILE = "com.sun.jndi.fscontext.RefFSContextFactory"; //@A2A
   private static final String JNDI_LDAP = "com.ibm.jndi.LDAPCtxFactory";                //@A2A

   private Context context_;		  //@A2A
   private String jndiType_ = JNDI_FILE;  //@A2A
   private String ldapUsr_ = null;        //@A2A
   private String ldapPwd_ = null;        //@A2A
   private static final String logDirectory_ = "javatest"; //@A3A
   private static File javatest_;         //@A4A 
   private String systemName_ = null;     //@B1A
   
   // Environment variables.
   private static final int OS_AS400 = 0;    //@A7A
   private static final int OS_WINDOWS = 1;  //@A7A
   private int environment_ = -1;	     //@A7A      

   /**
   *  Creates a printwriter.
    @exception  Exception  If an exception occurs.
   **/
   protected void setup() throws Exception
   {
     // Determine the environment.
     String os = System.getProperty("os.name");	  //@A7A
     System.out.println("Environment: " + os);	  //@A7A

     if (os.indexOf("OS/400") >= 0)        //@A7A
	environment_ = OS_AS400;	   //@A7A
     else if (JTOpenTestEnvironment.isWindows)  //@A7A
     	environment_ = OS_WINDOWS;	   //@A7A

     if (true)	 //@A5A
     {
	// Make sure javatest dir exists.						//@A3A
	javatest_ = new File(logDirectory_); //@A4C                                     //@A3A
	if (!javatest_.exists())                                                        //@A3A
	{                                                                               //@A3A
	    System.out.println("Setup is creating '" + logDirectory_ + "' directory.");  //@A3A
										      //@A3A
	    if(!javatest_.mkdir())                                                       //@A3A
	    {                                                                            //@A3A
		System.out.println("WARNING:  Setup could not create the '" + logDirectory_ + "' directory.");//@A3A
	    }                                                                            //@A3A
	}                                                                               //@A3A

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

     if (true)		 //@A5A
     {
	// Get the JNDI Initial Context.
	Properties env = new Properties();                                                  //@A2A

	if (jndiType_ == JNDI_LDAP)                                                         //@A2A
	{                                                                                   //@A2A
	  env.put("java.naming.provider.url", "ldap://" + systemObject_.getSystemName());   //@A2A
	  env.put("java.naming.security.principal", "cn=" + ldapUsr_);                      //@A2A
	  env.put("java.naming.security.credentials", ldapPwd_);                            //@A2A
	}                                                                                   //@A2A
	 else
     {
     	env.put(Context.PROVIDER_URL,"file:.");  //@pda for linux authority. default is "/" location of file created
     }

	env.put(Context.INITIAL_CONTEXT_FACTORY, jndiType_);                                //@A2A
	context_ = new InitialContext(env);                                                 //@A2A
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
   public AS400JDBCConnectionPoolDataSourceTestcase(AS400 systemObject,
                                 Vector<String> variationsToRun,
                                 int runMode,
                                 FileOutputStream fileOutputStream,
                                 
                                 String password,
				             String jndiType,   //@A2A
			                  String ldapUsr,    //@A2A
				             String ldapPwd,    //@A2A
                                 String systemName) //@B1A
                                                    
   {
      super(systemObject, "AS400JDBCConnectionPoolDataSourceTestcase", variationsToRun,
            runMode, fileOutputStream, password);
      if (jndiType.equals("file"))          //@A2A
         jndiType_ = JNDI_FILE;	            //@A2A
      else if (jndiType.equals("ldap"))     //@A2A
         jndiType_ = JNDI_LDAP;             //@A2A
      else
         System.out.println("WARNING... Unknown jndi type '" + jndiType + "' using default of FILE.");  //@A2A

      ldapUsr_ = ldapUsr;  //@A2A
      ldapPwd_ = ldapPwd;  //@A2A
      systemName_ = systemName;  //@B1A
   }

   /**
      Validates that getConnection() returns a valid connection object.
      
      AS400JDBCConnectionPoolDataSource()
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
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
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
            c.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that getConnection() returns a valid connection object.
      
      AS400JDBCConnectionPoolDataSource(String,String,String)
      getConnection()
   **/
   public void Var002()
   {
      Connection c = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
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
            c.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that getConnection(String,String) returns a valid connection object.
      
      AS400JDBCConnectionPoolDataSource()
      setServerName()
      getConnection(String,String)
   **/
   public void Var003()
   {
      Connection c = null;
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();
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
            c.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that getConnection(String,String) returns a valid connection object.
      
      AS400JDBCConnectionPoolDataSource(String,String,String)
      setServerName()
      getConnection(String,String)
   **/
   public void Var004()
   {
      Connection c = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

    charPassword = PasswordVault.decryptPassword(encryptedPassword_);
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
            c.close();
         }
         catch (SQLException s) {}
      }
   }


   /**
      Validates that a second getConnection() returns a valid connection object.
      
      AS400JDBCConnectionPoolDataSource(String,String,String)
      getConnection()
   **/
   public void Var005()
   {
      Connection c = null;
      Connection c2 = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
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
            c.close();
            c2.close();
         }
         catch (SQLException s) {}
      }

   }

   /**
      Validates that a second getConnection(String,String) returns a valid connection object.
      
      AS400JDBCConnectionPoolDataSource()
      setServerName()
      getConnection(String,String)
   **/
   public void Var006()
   {
      Connection c = null;
      Connection c2 = null;
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();
         ds.setServerName(systemObject_.getSystemName());

   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         c = ds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

    charPassword = PasswordVault.decryptPassword(encryptedPassword_);
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
            c.close();
            c2.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that a second getConnection(String,String) returns a valid connection object.
      
      AS400JDBCConnectionPoolDataSource(String,String,String)
      getConnection()
   **/
   public void Var007()
   {
      Connection c = null;
      Connection c2 = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
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
            c.close();
            c2.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that a second getConnection() returns a valid connection object.
      
      AS400JDBCConnectionPoolDataSource()
      setServerName()
      getConnection(String,String)
   **/
   public void Var008()
   {
      Connection c = null;
      Connection c2 = null;
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();
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
            c.close();
            c2.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that getConnection() prompts for a system name.
      
      AS400JDBCConnectionPoolDataSource()
      setUser()
      setPassword()
      getConnection()
   **/
   public void Var009(int runMode)
   {
       if (runMode != BOTH && runMode != ATTENDED) {
	   notApplicable("Attended run mode"); 
	   return;
       }

      Connection c = null;
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
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
            c.close();
         }
         catch (SQLException s) {}
      }
   }


   /**
      Validates that getConnection() prompts for a user.
      
      AS400JDBCConnectionPoolDataSource()
      setServerName()
      setPassword()
      getConnection()
   **/
   public void Var010(int runMode)
   {
       if (runMode != BOTH && runMode != ATTENDED) {
	   notApplicable("Attended run mode"); 
	   return;
       }
      Connection c = null;
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
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
            c.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that getConnection() prompts for a password
      
      AS400JDBCConnectionPoolDataSource()
      setServerName()
      setUser()
      getConnection()
   **/
   public void Var011(int runMode)
   {
       if (runMode != BOTH && runMode != ATTENDED) {
	   notApplicable("Attended run mode"); 
	   return;
       }
      Connection c = null;
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
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
            c.close();
         }
         catch (SQLException s) {}
      }
   }

   /**
      Validates that getConnection() prompts for a user and password.
      
      AS400JDBCConnectionPoolDataSource()
      setServerName()
      setPassword()
      getConnection()
   **/
   public void Var012(int runMode)
   {
       if (runMode != BOTH && runMode != ATTENDED) {
	   notApplicable("Attended run mode"); 
	   return;
       }
      Connection c = null;
      Connection c2 = null;
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
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
            c.close();
            c2.close();
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
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();
         
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
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
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
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
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
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();
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
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();
         
         AS400JDBCConnectionPoolDataSource ds2 = serialize(ds);

	 String userName = "";	        	  //@A7A
	 if (environment_ == OS_AS400)  	  //@A7A
	 {
	     AS400 tmpSystemObject = new AS400(); //allow diff logon-id as TC id
         userName = tmpSystemObject.getUserId(); //@A7A
         tmpSystemObject.close();
	 }
	 
         if ((ds2.getServerName().equals("") || ds2.getServerName().equals("localhost")) &&  //@A2C
             ds2.getUser().equals(userName) &&	    //@A7C
             ds2.getDatabaseName().equals("") &&    //@A1C
             ds2.getDataSourceName().equals("") &&  //@A1C
             ds2.getDescription().equals(""))       //@A1C
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
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();
         ds.setServerName(serverName);

         AS400JDBCConnectionPoolDataSource ds2 = serialize(ds);

	 String userName = "";	        	  //@A7A
	 if (environment_ == OS_AS400)  	  //@A7A
	 {
	     AS400 tmpSystemObject = new AS400(); //allow diff logon-id as TC id
         userName = tmpSystemObject.getUserId(); //@A7A
         tmpSystemObject.close();
	 }
	 
         if (ds2.getServerName().equalsIgnoreCase(serverName) &&
             ds2.getUser().equals(userName) &&	    //@A7C
             ds2.getDatabaseName().equals("") &&    //@A1C
             ds2.getDataSourceName().equals("") &&  //@A1C
             ds2.getDescription().equals(""))       //@A1C
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
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();
         ds.setUser(user);

         AS400JDBCConnectionPoolDataSource ds2 = serialize(ds);

         if ((ds2.getServerName().equals("") || ds2.getServerName().equals("localhost")) &&  //@A2C
             ds2.getUser().equalsIgnoreCase(user) &&
             ds2.getDatabaseName().equals("") &&      //@A1C
             ds2.getDataSourceName().equals("") &&    //@A1C
             ds2.getDescription().equals(""))         //@A1C
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
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();
         ds.setDatabaseName(databaseName);
         ds.setDataSourceName(dataSourceName);
         ds.setDescription(description);

         AS400JDBCConnectionPoolDataSource ds2 = serialize(ds);

	 String userName = "";	        	  //@A7A
	 if (environment_ == OS_AS400)  	  //@A7A
	 {
	     AS400 tmpSystemObject = new AS400(); //allow diff logon-id as TC id
         userName = tmpSystemObject.getUserId(); //@A7A
         tmpSystemObject.close();
	 }
	 
         if ((ds2.getServerName().equals("") || ds2.getServerName().equals("localhost")) &&  //@A2C
             ds2.getUser().equals(userName) &&	  //@A7C
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
      boolean packageCache = true; //@B3C
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();
         ds.setDateFormat(dateFormat);
         //@B3  setPackageClear was deprecated
         //@B3D ds.setPackageClear(packageClear);
         ds.setPackageCache(packageCache); //@B3A

         AS400JDBCConnectionPoolDataSource ds2 = serialize(ds);

	 String userName = "";	        	  //@A7A
	 if (environment_ == OS_AS400)  	  //@A7A
	 {
	     AS400 tmpSystemObject = new AS400(); //allow diff logon-id as TC id
         userName = tmpSystemObject.getUserId(); //@A7A
         tmpSystemObject.close();
	 }
	 
         if ((ds2.getServerName().equals("") || ds2.getServerName().equals("localhost")) &&   //@A2C
             ds2.getUser().equals(userName) &&	      //@A7C
             ds2.getDatabaseName().equals("") &&      //@A1C
             ds2.getDataSourceName().equals("") &&    //@A1C
             ds2.getDescription().equals("") &&       //@A1C
             ds2.getDateFormat().equals(dateFormat) &&
             ds2.isPackageCache()) //@B3C
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

//       if(JDTestDriver.getClientOS() == JDTestDriver.CLIENT_as400)
//       {
//           notApplicable("non i5 TC");
//           return;
//       }

      String jndiName = logDirectory_ + "/test120";   //@A3C  //"jdbc/test120"

      if (jndiType_ == JNDI_LDAP)					       //@A2A
	jndiName = "cn=test120datasource, cn=users, ou=rochester,o=ibm,c=us";  //@A2A

      // Database is IASP name.  Dont set it 
      // String databaseName = "QCUSTCDT";
      String serverName = systemObject_.getSystemName();
      String description = "Customer Database";

      try
      {
         // Create a data source to the AS/400 database.
         AS400JDBCConnectionPoolDataSource dataSource = new AS400JDBCConnectionPoolDataSource();
         dataSource.setServerName(serverName);
         // dataSource.setDatabaseName(databaseName);
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
         AS400JDBCConnectionPoolDataSource jndiDataSource = (AS400JDBCConnectionPoolDataSource) context_.lookup(jndiName);
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
//       if(JDTestDriver.getClientOS() == JDTestDriver.CLIENT_as400)
//       {
//           notApplicable("non i5 TC");
//           return;
//       }

      String jndiName = logDirectory_ + "/test121";    //@A3C  //"jdbc/test121"

      if (jndiType_ == JNDI_LDAP)					       //@A2A
	jndiName = "cn=test121datasource, cn=users, ou=rochester,o=ibm,c=us";  //@A2A

      try
      {
         // Create a data source to the AS/400 database.
         AS400JDBCConnectionPoolDataSource dataSource = new AS400JDBCConnectionPoolDataSource();

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
         AS400JDBCConnectionPoolDataSource jndiDataSource = (AS400JDBCConnectionPoolDataSource) context_.lookup(jndiName);
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
   public void Var024()
   {
       if(JDTestDriver.getClientOS() == JDTestDriver.CLIENT_as400)
       {
           // notApplicable("non i5 TC");
           // return;
       }

      String jndiName = logDirectory_ + "/test122";   //@A3C  //"jdbc/test122"

      if (jndiType_ == JNDI_LDAP)					       //@A2A
	jndiName = "cn=test122datasource, cn=users, ou=rochester,o=ibm,c=us";  //@A2A

      try
      {
         // Create a data source to the AS/400 database.
         AS400JDBCConnectionPoolDataSource dataSource = new AS400JDBCConnectionPoolDataSource();
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
         AS400JDBCConnectionPoolDataSource jndiDataSource = (AS400JDBCConnectionPoolDataSource) context_.lookup(jndiName);
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
   public void Var025()
   {
       if(JDTestDriver.getClientOS() == JDTestDriver.CLIENT_as400)
       {
           // notApplicable("non i5 TC");
           // return;
       }

      String jndiName = logDirectory_ + "/test123";    //@A3C  //"jdbc/test123"

      if (jndiType_ == JNDI_LDAP)					       //@A2A
	jndiName = "cn=test123datasource, cn=users, ou=rochester,o=ibm,c=us";  //@A2A

      try
      {
         // Create a data source to the AS/400 database.
         AS400JDBCConnectionPoolDataSource dataSource = new AS400JDBCConnectionPoolDataSource();
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
         AS400JDBCConnectionPoolDataSource jndiDataSource = (AS400JDBCConnectionPoolDataSource) context_.lookup(jndiName);
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
       if(JDTestDriver.getClientOS() == JDTestDriver.CLIENT_as400)
       {
           // notApplicable("non i5 TC");
           // return;
       }

      String jndiName = logDirectory_ + "/test124";   //@A3C  //"jdbc/test124"

      if (jndiType_ == JNDI_LDAP)					       //@A2A
	jndiName = "cn=test124datasource, cn=users, ou=rochester,o=ibm,c=us";  //@A2A

      try
      {
         // Create a data source to the AS/400 database.
         AS400JDBCConnectionPoolDataSource dataSource = new AS400JDBCConnectionPoolDataSource();
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
         AS400JDBCConnectionPoolDataSource jndiDataSource = (AS400JDBCConnectionPoolDataSource) context_.lookup(jndiName);
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
      PooledConnection c = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
         ds.setLogWriter(writer_);

         c = ds.getPooledConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

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
            c.close();
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
      PooledConnection c = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
         ds.setLogWriter(writer_);

         c = ds.getPooledConnection();

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
            c.close();
         }
         catch (Exception cl) 
         {
         }
      }
   }


   //@A8A  Fix for WebSphere.  P9950596
   /**
      Validates that getConnection().getMetaData() returns a valid database meta data object.
      
      AS400JDBCConnectionPoolDataSource(String,String,String)
      getConnection()
   **/
   public void Var029()
   {
      Connection c = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
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
            c.close();
         }
         catch (SQLException s) {}
      }
   }


   // @B1A Fix to method, before it was only returning jdbc:as400://. P9951301  
   /**
      Validates that getConnection().getMetaData().getURL returns a correct URL.
      
      AS400JDBCConnectionPoolDataSource(String,String,String)
      getConnection()
   **/
   public void Var030()
   {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
             AS400JDBCConnectionPoolDataSource cpds = new AS400JDBCConnectionPoolDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
                AS400JDBCConnection c = (AS400JDBCConnection)cpds.getConnection(systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

             String defaultValue = systemName_;  //@B2A

             //if (environment_ == OS_AS400)     //@B2A //@pdd can run from i5 to diff i5 which is not localhost
               // defaultValue = "localhost";    //@B2A
             if (environment_ == OS_AS400) 
                 defaultValue = c.getSystem().getSystemName();//systemName_;//not always localhost on i5 "localhost";   //@C6A
             //ok, if running on i5, and using the hostname of something other than localhost, but still running
             //to the local i5, then you still get back "localhost", so use systemname from as400 object
          
             assertCondition (c.getMetaData().getURL().equals("jdbc:as400://" + defaultValue)); //@B2C
             c.close();
        }
        catch (Exception e)
        {
             failed(e, "Unexpected exception.");
        }
   }



   /**
   *  Validates that a datasource object (with secure=true)
   *  can be set into JNDI,
   *  and then instantiated from JNDI.
   **/
   public void Var031()
   {


      String jndiName = logDirectory_ + "/test120";   //@A3C  //"jdbc/test120"

      if (jndiType_ == JNDI_LDAP)					       //@A2A
	jndiName = "cn=test120datasource, cn=users, ou=rochester,o=ibm,c=us";  //@A2A

      // Database is IASP name.  Dont set it 
      // String databaseName = "QCUSTCDT";
      String serverName = systemObject_.getSystemName();
      String description = "Customer Database";

      try
      {
         // Create a data source to the AS/400 database.
         AS400JDBCConnectionPoolDataSource dataSource = new AS400JDBCConnectionPoolDataSource();
         dataSource.setServerName(serverName);
         // dataSource.setDatabaseName(databaseName);
         dataSource.setDescription(description);
	 dataSource.setSecure(true);

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
         AS400JDBCConnectionPoolDataSource jndiDataSource = (AS400JDBCConnectionPoolDataSource) context_.lookup(jndiName);
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
            failed("Unexpected results. -- testcase added 5/4/2015 to test secure=true and datasource ");

         // cleanup.
         context_.unbind(jndiName);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception. -- testcase added 5/4/2015 to test secure=true and datasource ");
      }
   }


   public void Var032()
   {
      Connection c = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	 AS400 as400 = new AS400(systemObject_.getSystemName(),systemObject_.getUserId(),charPassword); 
   PasswordVault.clearPassword(charPassword);
	 
	  AS400JDBCConnectionPoolDataSource ds = (AS400JDBCConnectionPoolDataSource) 
	      JDReflectionUtil.createObject("com.ibm.as400.access.AS400JDBCConnectionPoolDataSource",
					    "com.ibm.as400.access.AS400", as400);
    /* AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource(as400); */          

         c= ds.getConnection();

	 if (c != null) {

	     Statement s = c.createStatement();
	     ResultSet rs = s.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
	     rs.next();
	     rs.close();
	     s.close();

            succeeded();
	 } else {
            failed("Did not get connection.");
	 }
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         try
         {
	     if (c != null) { 
		 c.close();
		 c=null;
	     }
         }
         catch (SQLException s) {}
      }
   }

   /**
   Validates that getConnection() returns a valid connection object when password is set using a properties object
   
   AS400JDBCConnectionPoolDataSource()
   setServerName()
   setProperties()
   getConnection()
**/
public void Var033()
{
   Connection c = null;
   if (checkPasswordLeak()) {
   try
   {
      AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();       
      Properties properties = new Properties(); 
      
      ds.setServerName(systemObject_.getSystemName());   
      properties.put("user",systemObject_.getUserId());
      properties.put("password",  PasswordVault.decryptPasswordLeak(encryptedPassword_));
      ds.setProperties(properties); 
      
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
}



   /**
     Performs cleanup needed after running testcases.
    @exception  Exception  If an exception occurs.
    **/
   protected void cleanup() throws Exception
   {
      try
      {
	 writer_.close();  //@A4A Close writer so file can be deleted below.
         File file = new File(filename_);
         if (!file.delete()) //@A4C
	     System.out.println("WARNING... testcase cleanup could not delete: " + filename_); //@A4A
	 if (!javatest_.delete()) //@A4A
             System.out.println("WARNING... testcase cleanup could not delete: " + logDirectory_); //@A4A         
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
   private AS400JDBCConnectionPoolDataSource serialize (AS400JDBCConnectionPoolDataSource ds) throws Exception
   {
      // Serialize.
      String serializeFileName = "datasource.ser";
      ObjectOutput out = new ObjectOutputStream (new FileOutputStream (serializeFileName));
      out.writeObject (ds);
      out.flush ();
      
      // Deserialize.
      AS400JDBCConnectionPoolDataSource ds2 = null;
      try {
         ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFileName));
         ds2 = (AS400JDBCConnectionPoolDataSource) in.readObject ();
	 in.close(); //@A4C Close input stream so file can be cleaned up below.
      }
      finally {
	 out.close(); //@A4C Close output stream so file can be cleaned up below.
	 File f = new File (serializeFileName);
	 if (!f.delete()) //@A4C
            System.out.println("WARNING... testcase cleanup could not delete: " + serializeFileName); //@A4A
      }
      return ds2;
   }
}
