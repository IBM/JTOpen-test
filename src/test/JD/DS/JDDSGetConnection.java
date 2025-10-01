///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDSGetConnection.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDSGetConnection.java
//
// Classes:      JDDSGetConnection
//
////////////////////////////////////////////////////////////////////////

package test.JD.DS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import javax.sql.DataSource;

import java.util.Hashtable;
import java.util.Vector;
import java.sql.*;



/**
Testcase JDDSGetConnection.  This tests the following methods
of the JDBC Driver class:

<ul>
<li>getConnection() 

</ul>
**/
public class JDDSGetConnection
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDSGetConnection";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDSTest.main(newArgs); 
   }



   // Private data.
   private Driver      driver_;
   private DataSource dataSource_;
   private DataSource dataSource2_;


/**
Constructor.
**/
   public JDDSGetConnection (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
   {
      super (systemObject, "JDDSGetConnection",
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
         driver_ = DriverManager.getDriver (baseURL_);
         
         if(getDriver () == JDTestDriver.DRIVER_NATIVE){
             dataSource_ = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2DataSource");
           
             //System.out.println("running with NATIVE");
	     if (true &&
		 getDriver() == JDTestDriver.DRIVER_NATIVE) {
		 dataSource2_ = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2DataSource");
	     } else {
		 dataSource2_ = null ;
	     } 
         }
         else
         {
             dataSource_ = new AS400JDBCDataSource();
             //System.out.println("running with TB");
             dataSource2_ = new AS400JDBCDataSource();
             ((AS400JDBCDataSource)dataSource2_).setPrompt(false);
         }
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
            Connection conn_ = dataSource_.getConnection(); 
            
            // Run a query.
            Statement s = conn_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean found = rs.next ();

            // Close the connection.
            conn_.close ();

            assertCondition (found);
         } catch (Exception e) {
            failed (e, "Unexpected Exception for driver "+driver_);
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
	    JDReflectionUtil.callMethod_V(dataSource_,"setPassword",
	          PasswordVault.decryptPasswordLeak(encryptedPassword_,"JDDSGetConnection.2"));
            Connection conn_ = dataSource_.getConnection();
            failed("Did not throw exception but got "+conn_);
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
            Connection conn_ = dataSource_.getConnection();

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
            JDReflectionUtil.callMethod_V(dataSource_,"setPassword",
                PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDSGetConnection.4"));
            Connection conn_ = dataSource_.getConnection();
            failed("Did not throw exception but got "+conn_);
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
            Connection conn_ = dataSource_.getConnection();
            failed("Did not throw exception but got "+conn_);
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
            JDReflectionUtil.callMethod_V(dataSource_,"setPassword",
                PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDSGetConnection.6"));
            Connection conn_ = dataSource_.getConnection();

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
   getConnection(userid,pwd) - Test various userid/password for new security code to not allow "" or "*current" 
   to connect using current profile id in system i
    **/
   public void Var007 ()
   {

       if(!JDTestDriver.getClientOS().equals( JDTestDriver.CLIENT_as400)){
           notApplicable("i5 platform TC");
           return;
       }

       DataSource d = dataSource2_;
       
       StringBuffer stats =  new StringBuffer() ; 
       try {
      

           //part 0
           //verify that default (whatever they are) will continue to work
           Connection conn1 = null; 
           try{
               conn1 =  d.getConnection( );
           }catch(Exception e){
               stats.append(" part0 failed to connect;  exception is ");
               printStackTraceToStringBuffer(e,  stats); 
               
           }

           //part 1
           try{
               conn1 =  d.getConnection( null, null);
	      
           }catch(Exception e){
               stats.append("part1 failed to connect\n");
               printStackTraceToStringBuffer(e,  stats); 
           }

            

           //part 2
           try{
               conn1 =  d.getConnection( "wrong" ,"wrong");
               stats.append("part2 connected with wrong id/pass;  "+conn1+"\n"); 
           }catch(Exception e){
               //failed as expected
           }

           //part 3
           try{
               conn1 =  d.getConnection( "" ,"");
               stats.append("part3 connected with emptystring id/pass;  "+conn1+"\n"); 
           }catch(Exception e){
               //failed as expected
           }

         //part 4
           try{
               conn1 =  d.getConnection( "*CURRENT" ,"*CURRENT");
               stats.append("part4 connected with *CURRENT id/pass;  "+conn1+"\n"); 
           }catch(Exception e){
              //failed as expected
           }

          //part 5
           try{
               conn1 =  d.getConnection( userId_ ,"*CURRENT");
               stats.append("part5 connected with *CURRENT pass;  "+conn1 +"\n");
           }catch(Exception e){
                //failed as expected
           }

           String errorMessage = stats.toString(); 
           assertCondition(errorMessage.equals(""), errorMessage);
       } catch (Exception e) {
           failed (e, "Unexpected Exception");
       }

   }
   /** same as 007, but explicitly set property to true, 007 uses default true value */
   public void Var008 ()
   {
       if ( getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
           notApplicable("Toolbox only TC");
           return; 
       } 

       try { 
       JDReflectionUtil.callMethod_V(dataSource2_, "setSecureCurrentUser", true); 
       /* ((AS400JDBCDataSource)dataSource2_).setSecureCurrentUser(true); ///!!!set to true */ 
       Var007();
       } catch (Exception e) {
         failed (e, "Unexpected Exception");
     }
       
   }
   /**
   getConnection(userid,pwd) - Test various userid/password for new security code to not allow "" or "*current" 
   to connect using current profile id in system i
   Set property secureCurrentUser = false to get old behavior and !new!
    **/
   public void Var009 ()
   {

       if ( getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
           notApplicable("TOolbox only TC");
           return; 
       } 
       if(!JDTestDriver.getClientOS().equals( JDTestDriver.CLIENT_as400)){
           notApplicable("i5 platform TC");
           return;
       }

       DataSource d = dataSource2_;
       
       String stats = ""; 
       try {
      
         JDReflectionUtil.callMethod_V(d, "setSecureCurrentUser", false); 

         /* ((AS400JDBCDataSource)d).setSecureCurrentUser(false); ///!!!set to false to allow old behavior */ 

           //part 0
           //verify that default (whatever they are) will continue to work
           Connection conn1 = null; 
           try{
               conn1 =  d.getConnection( );
           }catch(Exception e){
               stats += "part0 failed to connect;  conn1="+conn1;
           }

           //part 1
           try{
               conn1 =  d.getConnection( null, null);
          
           }catch(Exception e){
               stats += "part1 failed to connect;  ";
           }

            

           //part 2
           try{
               conn1 =  d.getConnection( "wrong" ,"wrong");
               stats += "part2 connected with wrong id/pass;  "+conn1;
           }catch(Exception e){
               //failed as expected
           }

           //part 3
           try{
               conn1 =  d.getConnection( "" ,""); //this should be ok on old behavior
           }catch(Exception e){
               stats += "part3 failed to connect;  ";
           }

         //part 4
           try{
               conn1 =  d.getConnection( "*CURRENT" ,"*CURRENT");//this should be ok on old behavior
 
           }catch(Exception e){
               stats += "part4 failed to connect;  ";
           }

          //part 5
           try{
               try{
                   conn1 =  d.getConnection( System.getProperty("user.name") ,"*CURRENT"); //this should be ok on old behavior
  
               }catch(Exception e1){
                   conn1 =  d.getConnection( testDriver_.pwrSysUserID_ ,"*CURRENT"); //this should be ok on old behavior
               }
                   
           }catch(Exception e){
               stats += "part5 failed to connect;  ";
           }

 
           assertCondition(stats.equals(""), stats);
       } catch (Exception e) {
           failed (e, "Unexpected Exception");
       }
   }

   /**
   getConnection(userid,pwd) - Test various userid/password for new security code to not allow "" or "*current" 
   to connect using current profile id in system i
   Set property secureCurrentUser = false to get old behavior and !new!
   same as 009, but set sytem jvm property instead of via datasource
    **/
   public void Var010 ()
   {

       if ( getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
           notApplicable("TOolbox only TC");
           return; 
       } 
       if(!JDTestDriver.getClientOS().equals( JDTestDriver.CLIENT_as400)){
           notApplicable("i5 platform TC");
           return;
       }

       DataSource d = dataSource2_;
       
       String stats = ""; 
       try {
         
         JDReflectionUtil.callMethod_V(d, "setSecureCurrentUser", true); 
         /* ((AS400JDBCDataSource)d).setSecureCurrentUser(true); ///!!!set to true */ 
         System.setProperty("com.ibm.as400.access.JDBC.secureCurrentUser", "false");
      

           //part 0
           //verify that default (whatever they are) will continue to work
           Connection conn1; 
           try{
               conn1 =  d.getConnection( );
           }catch(Exception e){
               stats += "part0 failed to connect;  ";
           }

           //part 1
           try{
               conn1 =  d.getConnection( null, null);
          
           }catch(Exception e){
               stats += "part1 failed to connect;  ";
           }

            

           //part 2
           try{
               conn1 =  d.getConnection( "wrong" ,"wrong");
               stats += "part2 connected with wrong id/pass;  "+conn1;
           }catch(Exception e){
               //failed as expected
           }

           //part 3
           try{
               conn1 =  d.getConnection( "" ,""); //this should be ok on old behavior
           }catch(Exception e){
               stats += "part3 failed to connect;  ";
           }

         //part 4
           try{
               conn1 =  d.getConnection( "*CURRENT" ,"*CURRENT");//this should be ok on old behavior
 
           }catch(Exception e){
               stats += "part4 failed to connect;  ";
           }

          //part 5
           try{
               try{
                   conn1 =  d.getConnection( System.getProperty("user.name") ,"*CURRENT"); //this should be ok on old behavior
               }catch(Exception e1){
                   conn1 =  d.getConnection( testDriver_.pwrSysUserID_ ,"*CURRENT"); //this should be ok on old behavior
               }
           }catch(Exception e){
               stats += "part5 failed to connect;  ";
           }

 
           assertCondition(stats.equals(""), stats);
       } catch (Exception e) {
           failed (e, "Unexpected Exception");
       }
   }


   /**
   getConnection(userid,pwd) - Test various userid/password for new security code to not allow "" or "*current" 
   to connect using current profile id in system i
   Set property secureCurrentUser = false to get old behavior and !new!
   same as 009, but set sytem jvm property and property to false
    **/
   public void Var011 ()
   {

       if ( getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
           notApplicable("TOolbox only TC");
           return; 
       } 
       if(!JDTestDriver.getClientOS().equals( JDTestDriver.CLIENT_as400)){
           notApplicable("i5 platform TC");
           return;
       }

       DataSource d = dataSource2_;
       
       String stats = ""; 
       try {
      
         JDReflectionUtil.callMethod_V(d, "setSecureCurrentUser", true); 

         /* ((AS400JDBCDataSource)d).setSecureCurrentUser(false); ///!!!set to false */ 
         System.setProperty("com.ibm.as400.access.JDBC.secureCurrentUser", "false");

           //part 0
           //verify that default (whatever they are) will continue to work
           Connection conn1 = null; 
           try{
               conn1 =  d.getConnection( );
           }catch(Exception e){
               stats += "part0 failed to connect;  conn1= "+conn1;
           }

           //part 1
           try{
               conn1 =  d.getConnection( null, null);
          
           }catch(Exception e){
               stats += "part1 failed to connect;  ";
           }

            

           //part 2
           try{
               conn1 =  d.getConnection( "wrong" ,"wrong");
               stats += "part2 connected with wrong id/pass;  "+conn1;
           }catch(Exception e){
               //failed as expected
           }

           //part 3
           try{
               conn1 =  d.getConnection( "" ,""); //this should be ok on old behavior
           }catch(Exception e){
               stats += "part3 failed to connect;  ";
           }

         //part 4
           try{
               conn1 =  d.getConnection( "*CURRENT" ,"*CURRENT");//this should be ok on old behavior
 
           }catch(Exception e){
               stats += "part4 failed to connect;  ";
           }

          //part 5
           try{
               try{
                   conn1 =  d.getConnection( System.getProperty("user.name") ,"*CURRENT"); //this should be ok on old behavior
               }catch(Exception e1){
                   conn1 =  d.getConnection( testDriver_.pwrSysUserID_  ,"*CURRENT"); //this should be ok on old behavior
               }
               
           }catch(Exception e){
               stats += "part5 failed to connect;  ";
           }

 
           assertCondition(stats.equals(""), stats);
       } catch (Exception e) {
           failed (e, "Unexpected Exception");
       }
   }


}



