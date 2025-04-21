///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCPooledConnectionTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.AS400JDBC;


import java.io.FileOutputStream;
import java.util.Vector;
import java.sql.*;

import javax.sql.PooledConnection;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnectionPoolDataSource;
import com.ibm.as400.access.AS400JDBCPooledConnection;

import test.JDReflectionUtil;
import test.PasswordVault;
import test.Testcase;

/**
  Testcase AS400JDBCPooledConnectionTestcase.
 **/
public class AS400JDBCPooledConnectionTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "AS400JDBCPooledConnectionTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.AS400JDBCConnectionPoolTest.main(newArgs); 
   }
   // private PrintWriter print = null;
   // private String tag = null;
   // private boolean rejectChange = false;

   /**
     Constructor.  This is called from the AS400JDBCDataSourceTest constructor.
    **/
   public AS400JDBCPooledConnectionTestcase(AS400 systemObject,
                                 Vector<String> variationsToRun,
                                 int runMode,
                                 FileOutputStream fileOutputStream,
                                 
                                 String password)
   {
      super(systemObject, "AS400JDBCPooledConnectionTestcase", variationsToRun,
            runMode, fileOutputStream, password);
   }
   
   /**
     Performs setup needed before running testcases.
    @exception  Exception  If an exception occurs.
    **/
   public void setup() throws Exception
   {
   }

   /**
   *  Validate that getConnection() returns a valid connection.
   **/
   public void Var001()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         Connection c = pc.getConnection();

         if (c != null) 
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
   *  Validates that getConnection() returns a usable valid connection.
   **/
   public void Var002()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         Connection c = pc.getConnection();
         
         if (c != null) 
         {
            Statement s = c.createStatement();
            s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
            succeeded();
         }
         else
         {
            failed("Unexpected Results.");
         }
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }

   /**
   *  Validate that close() closes the physical connection.
   **/
   public void Var003()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         Connection c = pc.getConnection();

         pc.close();

         Statement s = c.createStatement();
         failed("Unexpected Results.  Connection is still valid. s="+s );
      }
      catch (SQLException x)      //@A1C
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }

   /**
   *  Validates that the connection.close() invalidates the connection.
   **/
   public void Var004()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();
         Connection c = pc.getConnection();

         c.close();

         Statement s = c.createStatement();
         failed("Unexpected Results. s="+s);
      }
      catch (SQLException e)      //@A1C
      {
         succeeded();
      }
      catch (Exception x)
      {
         failed(x, "Unexpected Exception.");
      }
   }

   /**
   *  Validates that connection.close() allows the pooled connection to be reused.
   **/
   public void Var005()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();
         Connection c = pc.getConnection();

         c.close();

         Connection c2 = pc.getConnection();
         Statement s = c2.createStatement();

         assertCondition(true, "s="+s); 
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }

   /**
   *  Validates that getting a connection set the pooled connection in use.
   **/
   public void Var006()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();
         Connection c = pc.getConnection();

         if (pc.isInUse()) 
            succeeded();
         else
            failed("Unexpected Results. c="+c);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception");
      }
   }

   /**
   *  Validates that connection.close() notifies the pooled connection.
   **/
   public void Var007()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();
         Connection c = pc.getConnection();

         c.close();

         if (!pc.isInUse()) 
            succeeded();
         else
            failed("Unexpected Results.");
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception");
      }
   }

   /**
   *  Validates that getInactivityTime() returns 0 if the pooled connection is closed.
   **/
   public void Var008()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         pc.close();

         if (pc.getInactivityTime() == 0)
            succeeded();
         else 
            failed("Unexpected Results.  " + pc.getInactivityTime());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }

   /**
   *  Validates that getInUseTime() returns 0 if the pooled connection is closed.
   **/
   public void Var009()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         pc.close();

         if (pc.getInUseTime() == 0)
            succeeded();
         else 
            failed("Unexpected Results.  " + pc.getInUseTime());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }

   /**
   *  Validates that getLifeSpan() returns 0 if the pooled connection is closed.
   **/
   public void Var010()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         pc.close();

         if (pc.getLifeSpan() == 0)
            succeeded();
         else 
            failed("Unexpected Results.  " + pc.getLifeSpan());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }

   /**
   *  Validates that getUseCount() returns 0 if the pooled connection is closed.
   **/
   public void Var011()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         pc.close();

         if (pc.getUseCount() == 0)
            succeeded();
         else 
            failed("Unexpected Results.  " + pc.getUseCount());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }

   /**
   *  Validate that the getUseCount() initializes to zero.
   **/
   public void Var012()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         if (pc.getUseCount() == 0) 
            succeeded();
         else
            failed("Unexpected Results.  " + pc.getUseCount());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }


   /**
   *  Validate that the getUseCount() returns the expected value.
   **/
   public void Var013()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         int timesUsed = 3;
         for (int i=0; i< timesUsed; i++) 
         {
            Connection c = pc.getConnection();
            c.close();
         }

         if (pc.getUseCount() == timesUsed) 
            succeeded();
         else
            failed("Unexpected Results.  " + pc.getUseCount());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }


   /**
   *  Validate that the getLifeSpan() returns the expected value.
   **/
   public void Var014()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         Connection c = pc.getConnection();
         Statement s = c.createStatement();
         ResultSet r = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

         if (pc.getLifeSpan() > 0) 
            succeeded();
         else
            failed("Unexpected Results pooled connection lifespan is " + pc.getLifeSpan()+" r="+r);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }


   /**
   *  Validate that the getInUseTime() returns the expected value.
   **/
   public void Var015()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         Connection c = pc.getConnection();
         Statement s = c.createStatement();
         ResultSet r = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");

         if (pc.getInUseTime() > 0) 
            succeeded();
         else
            failed("Unexpected Results poolConnection.getInUseTime returned  " + pc.getInUseTime() +" r="+r);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }


   /**
   *  Validate that the getInactivityTime() returns the expected value.
   **/
   public void Var016()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         Thread.sleep(500);

         if (pc.getInactivityTime() > 0)
         {
            succeeded();
         }
         else
            failed("Unexpected Results.  " + pc.getInactivityTime());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }

   /**
   *  Validates that getConnection() closes an existing open connection.
   **/
   public void Var017()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         Connection c1 = pc.getConnection();
         Connection c2 = pc.getConnection();

         Statement s = c1.createStatement();

         failed("Unexpected Results. got "+s+ " and "+c2);        
      }
      catch (SQLException x)      //@A1C
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }

   /**
   *  Validates that getConnection() returns a valid connection when an open connection already exists.
   **/
   public void Var018()
   {
      try
      {
         AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
         ds.setServerName(systemObject_.getSystemName());   
         ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

         AS400JDBCPooledConnection pc = (AS400JDBCPooledConnection)ds.getPooledConnection();

         Connection c1 = pc.getConnection();
         Connection c2 = pc.getConnection();

         //@B1D if (c2 != null && !c2.isClosed())
         //@B1D    succeeded();
         //@B1D else
            failed("Unexpected Results. c1="+c1+" c2="+c2);
      }
      catch(SQLException x) //@B1A                              
      {                     //@B1A
          succeeded();      //@B1A
      }                     //@B1A
      catch (Exception e)
      {
         failed(e, "Unexpected Exception.");
      }
   }

   // ??? addConnectionEventListener(ConnectionEventListener)

   /* Run a set of commands in the array of two string */
   /* command are set / get / getConnection / close */ 
   public void checkProperties(AS400JDBCConnectionPoolDataSource ds, String[][] commands) {

       StringBuffer sb  = new StringBuffer(); 
       try
       {
	   boolean passed = true;

	   PooledConnection pooledConnection = ds.getPooledConnection();
	   Connection connection = pooledConnection.getConnection();

	   for (int i = 0; i < commands.length; i++) {
	       String thisCommand = commands[i][0];
	       sb.append("Command : "+thisCommand+"\n");
	       if (thisCommand.equals("close")) {
		   connection.close(); 
	       } else if (thisCommand.equals("getConnection")) {
		   connection = pooledConnection.getConnection();
	       } else if (thisCommand.indexOf("get") == 0) {
	         String value = null; 
	         
	         Object object =  JDReflectionUtil.callMethod_O(connection, thisCommand);
	         if (object instanceof String) { 
	           value = (String) object; 
	         } else {
	           value = ""+object.toString(); 
	         }
		     sb.append(" returned "+value+"\n");
		   if (! value.equals(commands[i][1])) {
		       sb.append("   failed, expected :  "+commands[i][1]+"\n");
		       passed =false; 
		   }
         } else if (thisCommand.indexOf("setAutoCommit") == 0) {
           sb.append(" parameter = "+commands[i][1]+"\n");
           connection.setAutoCommit( Boolean.getBoolean(commands[i][1])); 
         } else if (thisCommand.indexOf("setTransactionIsolation") == 0) {
           sb.append(" parameter = "+commands[i][1]+"\n");
           connection.setTransactionIsolation( Integer.parseInt(commands[i][1])); 
         } else if (thisCommand.indexOf("setHoldability") == 0) {
           sb.append(" parameter = "+commands[i][1]+"\n");
           connection.setHoldability( Integer.parseInt(commands[i][1])); 
	       } else if (thisCommand.indexOf("set") == 0) {
		   sb.append(" parameter = "+commands[i][1]+"\n");
		   JDReflectionUtil.callMethod_V(connection, thisCommand, commands[i][1]); 
	       } 

	   } 

	   

	   assertCondition(passed, sb.toString()); 
       }
       catch(SQLException x) //@B1A                              
       {                     //@B1A
	   succeeded();      //@B1A
       }                     //@B1A
       catch (Exception e)
       {
	   failed(e, "Unexpected Exception. " +  sb.toString());
       }

   } 


   // check default settings 
   public void Var019()
   {
       String[][] commands = {
	   {"getSchema", systemObject_.getUserId()},
	   {"getAutoCommit", "true"}, 
	   {"getTransactionIsolation", "1"}, 
	   {"getHoldability", "1"}, 

       }; 
	   AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
	   ds.setServerName(systemObject_.getSystemName());   
	   ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	   ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
	   
	   
	   checkProperties(ds, commands); 
   }


   // check default settings for system naming 
   public void Var020()
   {
       String[][] commands = {
     {"getSchema", "*LIBL"},
     {"getAutoCommit", "true"}, 
     {"getTransactionIsolation", "1"}, 
     {"getHoldability", "1"}, 

       }; 
     AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
     ds.setServerName(systemObject_.getSystemName());   
     ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
     ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
     ds.setNaming("system"); 
     
     checkProperties(ds, commands); 
   }

   // check default settings after changing and getting new connection 
   public void Var021()
   {
       String[][] commands = {
     {"getSchema", systemObject_.getUserId()},
     {"getAutoCommit", "true"}, 
     {"getTransactionIsolation", "1"}, 
     {"getHoldability", "1"}, 
     {"setSchema","QGPL"},
     {"setAutoCommit", "false"},
     {"setTransactionIsolation","2"},
     {"setHoldability","2"},
     {"getSchema","QGPL"},
     {"getAutoCommit", "false"},
     {"getTransactionIsolation","2"},
     {"getHoldability","2"},
     {"close"}, 
     {"getConnection"}, 
     {"getSchema", systemObject_.getUserId()},
     {"getAutoCommit", "true"}, 
     {"getTransactionIsolation", "1"}, 
     {"getHoldability", "1"}, 
     

       }; 
     AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
     ds.setServerName(systemObject_.getSystemName());   
     ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
     ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
     
     
     checkProperties(ds, commands); 
   }

   // check default settings after changing and getting new connection for system naming 
   public void Var022()
   {
       String[][] commands = {
     {"getSchema", "*LIBL"},
     {"getAutoCommit", "true"}, 
     {"getTransactionIsolation", "1"}, 
     {"getHoldability", "1"}, 
     {"setSchema","QGPL"},
     {"setAutoCommit", "false"},
     {"setTransactionIsolation","2"},
     {"setHoldability","2"},
     {"getSchema","QGPL"},
     {"getAutoCommit", "false"},
     {"getTransactionIsolation","2"},
     {"getHoldability","2"},
     {"close"}, 
     {"getConnection"}, 
     {"getSchema", "*LIBL"},
     {"getAutoCommit", "true"}, 
     {"getTransactionIsolation", "1"}, 
     {"getHoldability", "1"}, 
     

       }; 
     AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
     ds.setServerName(systemObject_.getSystemName());   
     ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
     ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
     ds.setNaming("system"); 
     
     checkProperties(ds, commands); 
   }


   // check default settings after changing and getting new connection for system naming
   // After setting libraries property in the data source
   public void Var023()
   {
       String[][] commands = {
     {"getSchema", "*LIBL"},
     {"getAutoCommit", "true"}, 
     {"getTransactionIsolation", "1"}, 
     {"getHoldability", "1"}, 
     {"setSchema","QGPL"},
     {"setAutoCommit", "false"},
     {"setTransactionIsolation","2"},
     {"setHoldability","2"},
     {"getSchema","QGPL"},
     {"getAutoCommit", "false"},
     {"getTransactionIsolation","2"},
     {"getHoldability","2"},
     {"close"}, 
     {"getConnection"}, 
     {"getSchema", "*LIBL"},
     {"getAutoCommit", "true"}, 
     {"getTransactionIsolation", "1"}, 
     {"getHoldability", "1"}, 
     

       }; 
     AS400JDBCConnectionPoolDataSource ds = new AS400JDBCConnectionPoolDataSource();         
     ds.setServerName(systemObject_.getSystemName());   
     ds.setUser(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
     ds.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
     ds.setNaming("system"); 
     ds.setLibraries("QSYSDIR,QSYS"); 
     checkProperties(ds, commands); 
   }


   

}
