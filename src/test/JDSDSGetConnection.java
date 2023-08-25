///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSDSGetConnection.java
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
// File Name:    JDSDSGetConnection.java
//
// Classes:      JDSDSGetConnection
//
////////////////////////////////////////////////////////////////////////
//
//
// Release     Date        Userid    Comments
//
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.sql.*;
import javax.sql.DataSource;


/**
Testcase JDSDSGetConnection.  This tests the following methods
of the JDBC Driver class:

<ul>
<li>getConnection() 

</ul>
**/
public class JDSDSGetConnection
extends JDTestcase {



   // Private data.
   private DataSource dataSource_;

   // Protected data.
   //protected String system_;
   //protected String userId_;
   //protected String password_;

/**
Constructor.
**/
   public JDSDSGetConnection (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
   {
      super (systemObject, "JDSDSGetConnection",
             namesAndVars, runMode, fileOutputStream,
             password);
      //system_ = systemObject.getSystemName();
      //userId_ = systemObject.getUserId();
      //password_ = password;
   }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
   protected void setup ()
   throws Exception
   {
      if (isJdbc20StdExt ()) {
         dataSource_ = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdDataSource");
      }
   }



   // TO Test:
   // getConnection()
   // getConnection(user, password)


/**
getConnection() - Should work when the databasename is not set.  The 
default database name of *LOCAL will get picked up and the connection 
will get created.
**/
   public void Var001 ()
   {
       if( getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
           notApplicable("Non Toolbox TC");
           return;
       }
      if (checkJdbc20StdExt ()) {
         try {
            Connection conn_ = (Connection) JDReflectionUtil.callMethod_O(dataSource_,"getConnection");

            // Run a query.
            Statement s = conn_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean found = rs.next ();

            // Close the connection.
            conn_.close ();

            assertCondition (found);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }

/**
getConnection() - Should throw an exception when the databasename is set but not valid.
**/
   public void Var002 ()
   {
       if( getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
           notApplicable("Non Toolbox TC");
           return;
       }
      if (checkJdbc20StdExt ()) {
         try {
            JDReflectionUtil.callMethod_V(dataSource_,"setDatabaseName","invaliddatabasename");
	    JDReflectionUtil.callMethod_V(dataSource_,"setUser",userId_);
	    JDReflectionUtil.callMethod_V(dataSource_,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Connection conn_ = (Connection) JDReflectionUtil.callMethod_O(dataSource_,"getConnection");
            failed("Did not throw exception "+conn_);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getConnection() - Should work when the valid databasename is set.
**/
   public void Var003 ()
   {
       if( getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
           notApplicable("Non Toolbox TC");
           return;
       }
      if (checkJdbc20StdExt ()) {
         try {
            JDReflectionUtil.callMethod_V(dataSource_,"setDatabaseName",system_);
            Connection conn_ = (Connection) JDReflectionUtil.callMethod_O(dataSource_,"getConnection");

            // Run a query.
            Statement s = conn_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean found = rs.next ();

            // Close the connection.
            conn_.close ();

            assertCondition (found);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }

/**
getConnection(userid,pwd) - Should throw an exception when the userid is not valid.
**/
   public void Var004 ()
   {
       if( getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
           notApplicable("Non Toolbox TC");
           return;
       }
      if (checkJdbc20StdExt ()) {
         try {
            JDReflectionUtil.callMethod_V(dataSource_,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(dataSource_,"setUser","invalidusername");
            JDReflectionUtil.callMethod_V(dataSource_,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Connection conn_ = (Connection) JDReflectionUtil.callMethod_O(dataSource_,"getConnection");
            failed("Did not throw exception "+conn_);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getConnection(userid,pwd) - Should throw an exception when the password is not valid.
**/
   public void Var005 ()
   {
       if( getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
           notApplicable("Non Toolbox TC");
           return;
       }
      if (checkJdbc20StdExt ()) {
         try {
            JDReflectionUtil.callMethod_V(dataSource_,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(dataSource_,"setUser",userId_);
            JDReflectionUtil.callMethod_V(dataSource_,"setPassword","invalidpassword");
            Connection conn_ = (Connection) JDReflectionUtil.callMethod_O(dataSource_,"getConnection");
            failed("Did not throw exception "+conn_);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getConnection(userid,pwd) - Should work with valid userid,pwd.
**/
   public void Var006 ()
   {
       if( getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
           notApplicable("Non Toolbox TC");
           return;
       }
      if (checkJdbc20StdExt ()) {
         try {
            JDReflectionUtil.callMethod_V(dataSource_,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(dataSource_,"setUser",userId_);
            JDReflectionUtil.callMethod_V(dataSource_,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Connection conn_ = (Connection) JDReflectionUtil.callMethod_O(dataSource_,"getConnection");

            // Run a query.
            Statement s = conn_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean found = rs.next ();

            // Close the connection.
            conn_.close ();

            assertCondition (found);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



}



