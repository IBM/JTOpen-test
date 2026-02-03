///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRowSetPSTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////
//
// File Name:    JDRowSetPSTestcase.java
//
// Classes:      JDRowSetPSTestcase
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;
import com.ibm.as400.access.AS400JDBCRowSet;

import test.JDPSTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTOpenTestEnvironment;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.Hashtable; import java.util.Vector;
import javax.naming.Context;              // JNDI
import javax.naming.InitialContext;       // JNDI
import javax.naming.NamingException;      // JNDI

import javax.sql.DataSource;
import javax.sql.RowSet;
import test.JD.JDSerializeFile;
import java.sql.SQLException;


/**
Testcase JDRowSetPSTestcase.
**/
public class JDRowSetPSTestcase
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRowSetPSTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }
    String jndiName_ = "jdbc/customer";
    private Connection conn_;   //@a1
    private Statement stmt_;    //used by the native driver for do some of the table setup stuff.
/**
Constructor.
**/
    public JDRowSetPSTestcase (AS400 systemObject,
                               Hashtable<String,Vector<String>> namesAndVars,
                               int runMode,
                               FileOutputStream fileOutputStream,
                               
                               String password,
                               String pwrUserId,
                               String pwrPassword)
    {
        super (systemObject, "JDRowSetPSTestcase",
            namesAndVars, runMode, fileOutputStream,
            password,pwrUserId,pwrPassword);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
	super.setup();
	connection_ = testDriver_.getConnection (baseURL_,systemObject_.getUserId(), encryptedPassword_);
	DataSource ds; //@A1  switched to generic object types instead of driver specific ones.
	  if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
	  {

	       char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
       ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId (), charPassword);
  PasswordVault.clearPassword(charPassword);

	
              System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");  //@B1A


	      // Set the root to a writable directory on AIX and Linux
	      if (JTOpenTestEnvironment.isOS400 || JTOpenTestEnvironment.isLinux || JTOpenTestEnvironment.isAIX ) {

		  System.setProperty("java.naming.provider.url", "file:///tmp");

		  java.io.File file = new java.io.File("/tmp/jdbc");
		  try { 
		      file.mkdir();
		  } catch (Exception e) {
		      e.printStackTrace(); 
		  } 

	      }

              Context context = new InitialContext();  
              try
	      {
		  context.bind(jndiName_, ds);
	      }
	      catch(NamingException n)
	      {
		  context.unbind(jndiName_);
		  context.bind(jndiName_, ds);
	      }

	  }
	  else
	  {
	      System.setProperty(Context.INITIAL_CONTEXT_FACTORY,  "com.sun.jndi.fscontext.RefFSContextFactory");
	      jndiName_ = "jdbc";
	      ds = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBDataSource");

			JDReflectionUtil.callMethod_V(ds, "setDatabaseName", systemObject_.getSystemName());
			JDReflectionUtil.callMethod_V(ds, "setUser", systemObject_.getUserId());
			JDReflectionUtil.callMethod_V(ds, "setPassword", PasswordVault.decryptPasswordLeak(encryptedPassword_));
			JDReflectionUtil.callMethod_V(ds, "setDescription", "A simple UDBDataSource");

			Context context = new InitialContext();
			context.rebind(jndiName_,ds);
	      conn_ = testDriver_.getConnection(baseURL_,systemObject_.getUserId (),encryptedPassword_);
	      stmt_ = conn_.createStatement ();

	  }   
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
       super.cleanup();
       connection_.close(); 
    }

    /**
    *  clearParameters() - Verify that executing after a clear throws an exception.
    **/
    public void Var001()
    {
    JDSerializeFile pstestSet = null;                 
        try 
        {
      pstestSet = JDPSTest.getPstestSet(connection_);
	   if (getDriver () == JDTestDriver.DRIVER_NATIVE)
	   {
	       notApplicable();
	       return;
	   }
           AS400JDBCRowSet rowset;//@A1
                   
           rowset = new AS400JDBCRowSet(jndiName_);
           rowset.setUsername(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
           rowset.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
           rowset.setCommand("DELETE FROM " + pstestSet.getName());
           rowset.execute();

           rowset.setCommand ("INSERT INTO " + pstestSet.getName() + " (C_KEY) VALUES (?)");
           rowset.execute();
           
           rowset.setString (1, "Hola");
           rowset.execute();
           rowset.clearParameters ();
           
           rowset.execute();
           rowset.close(); 
           failed ("Unexpected Results.  Didn't throw SQLException");
        }
        catch (Exception e) 
        {
           assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  
    /**
    *  clearParameters() - Verify that this has no effect on a statement with no parameters.
    **/
    public void Var002()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
String userId = null; 
        try 
        {
            RowSet rowset; 
            AS400JDBCRowSet rowset2;//@A1
            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
            {
                rowset2 = new AS400JDBCRowSet(jndiName_);
                rowset = rowset2; 
		userId = systemObject_.getUserId(); 
           rowset.setUsername(userId);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
           rowset2.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
           rowset.setCommand("DELETE FROM " + pstestSet.getName());
           rowset.execute();

           rowset.setCommand("INSERT INTO " + pstestSet.getName() + " (C_KEY) VALUES ('Adios')");
           rowset.execute();


            }
            else
            {
                rowset = (RowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2JdbcRowSet");
                rowset.setDataSourceName(jndiName_);
		userId = systemObject_.getUserId(); 
		rowset.setUsername(userId);
		String password="passwordLeak:JDRowSetPSTestcase";
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
   password = new String(charPassword); 
		rowset.setPassword(password);
   PasswordVault.clearPassword(charPassword);
		stmt_.executeUpdate("DELETE FROM " + pstestSet.getName());
		stmt_.executeUpdate("INSERT INTO " + pstestSet.getName() + " (C_KEY) VALUES ('Adios')");
		stmt_.executeUpdate("INSERT INTO " + pstestSet.getName() + " (C_KEY) VALUES ('Adios')");
		rowset.setCommand("SELECT C_KEY FROM " + pstestSet.getName() + " WHERE C_KEY='Adios'");
            }
           rowset.clearParameters ();
           rowset.execute();
  
           rowset.setCommand("SELECT C_KEY FROM " + pstestSet.getName() + " WHERE C_KEY='Adios'");
           rowset.execute();

           int count = 0;
           while (rowset.next ())
              ++count;
           rowset.close ();

           assertCondition (count == 2);
        }
        catch (Exception e) 
        {
           failed (e, "Unexpected Exception UserId="+userId);
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
