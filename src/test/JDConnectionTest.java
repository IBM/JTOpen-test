///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionTest.java
//
// Classes:      JDConnectionTest
//
////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.sql.Connection;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;



/**
Test driver for the JDBC Connection class.
**/
public class JDConnectionTest
      extends JDTestDriver {



   /**
   * 
   */
  public static final long serialVersionUID = 1L;
  // Private data.
   public static  String COLLECTION     = "JDTESTCONN";
   public static  String SCHEMAS_LEN128   = "JDTESTCOTHISISAREALLYLONGSCHEMANAMEOFLENGTH128AB0123456789THISISAREALLYLONGSCHEMANAMEOFLENGTH128AB0123456789THISISAREALLYLONGSAB"; //@128sch





/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.

@exception Exception If an exception occurs.
**/
   public static void main (String args[])
   throws Exception
   {
      runApplication (new JDConnectionTest (args));
   }



/**
Constructs an object for applets.

@exception Exception If an exception occurs.
**/
   public JDConnectionTest ()
   throws Exception
   {
      super();
   }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.

@exception Exception If an exception occurs.
**/
   public JDConnectionTest (String[] args)
   throws Exception
   {
      super (args);
   }



/**
Performs setup needed before running testcases.

@exception Exception If an exception occurs.
**/
   public void setup ()
   throws Exception
   {
      super.setup();   // @D1A
      Connection c = getConnection (getBaseURL (),  // D2C
                                    userId_, encryptedPassword_);
      if (testLib_ != null) { // @E1A
         COLLECTION = testLib_;
      }
      JDSetupCollection.create (systemObject_,  c,
                                COLLECTION);
      
      if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)  //@128sch 
      {
          JDSetupCollection.create (systemObject_, 
                  c, SCHEMAS_LEN128);
      }
      
      
      JDSupportedFeatures supportedFeatures_= new JDSupportedFeatures(this);
      JDSetupProcedure.create (systemObject_,                              //@F2A
                                 c, JDSetupProcedure.STP_RS1, supportedFeatures_,
                                 COLLECTION);                                         //@F2A
      c.close ();
   }



/**
Cleanup - - this does not run automatically - - it is called by JDCleanup.
**/
   public static void dropCollections(Connection c)
   {
      dropCollection(c, COLLECTION);
    
      dropCollection(c, SCHEMAS_LEN128); //@128sch
   }



/**
Performs setup needed after running testcases.

@exception Exception If an exception occurs.
**/
   public void cleanup ()
   throws Exception
   {
   }



/**
Creates the testcases.
**/
   public void createTestcases2 ()
   {
	   if(TestDriverStatic.pause_)
	   { 
  		  	try 
  		  	{						
  		  		systemObject_.connectService(AS400.DATABASE);
  			}
  	     	catch (AS400SecurityException e) 
  	     	{
  				e.printStackTrace();
  			} 
  	     	catch (IOException e) 
  	     	{
  	     	    e.printStackTrace();
  			}
  				 	 	   
  	     	try
  	     	{
  	     	    Job[] jobs = systemObject_.getJobs(AS400.DATABASE);
  	     	    System.out.println("Host Server job(s): ");

  	     	    	for(int i = 0 ; i< jobs.length; i++)
  	     	    	{   	    	
  	     	    		System.out.println(jobs[i]);
  	     	    	}    	    
  	     	 }
  	     	 catch(Exception exc){}
  	     	    
  	     	 try 
  	     	 {
  	     	    	System.out.println ("Toolbox is paused. Press ENTER to continue.");
  	     	    	System.in.read ();
  	     	 } 
  	     	 catch (Exception exc) {};   	   
	   } 
	   
	   
	   
	   
	  addTestcase (new JDConnectionClose (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_, pwrSysUserID_, pwrSysPassword_));

	  addTestcase (new JDConnectionClose10 (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_, pwrSysUserID_, pwrSysPassword_));

	  addTestcase (new JDConnectionClose11 (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_, pwrSysUserID_, pwrSysPassword_));

	  addTestcase (new JDConnectionClose12 (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_, pwrSysUserID_, pwrSysPassword_));

	  addTestcase (new JDConnectionClose13 (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_, pwrSysUserID_, pwrSysPassword_));

      addTestcase (new JDConnectionCommit (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

      addTestcase (new JDConnectionCreateStatement (systemObject_,
                                                    namesAndVars_, runMode_, fileOutputStream_, 
                                                    password_));

      addTestcase (new JDConnectionCursorHold (systemObject_,
                                               namesAndVars_, runMode_, fileOutputStream_, 
                                               password_));

      addTestcase (new JDConnectionCursorHoldability (systemObject_,                                    // @F1A
                                               namesAndVars_, runMode_, fileOutputStream_,      // @F1A
                                               password_));                                            

      addTestcase (new JDConnectionFormat (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

      addTestcase (new JDConnectionLibraries (systemObject_,
                                              namesAndVars_, runMode_, fileOutputStream_, 
                                              password_, pwrSysUserID_, pwrSysPassword_));      

      addTestcase (new JDConnectionMisc (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_, 
                                         password_, pwrSysUserID_, pwrSysPassword_));         

      addTestcase (new JDConnectionNaming (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

      addTestcase (new JDConnectionNativeSQL (systemObject_,
                                              namesAndVars_, runMode_, fileOutputStream_, 
                                              password_));

      addTestcase (new JDConnectionReadOnly (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));

      addTestcase (new JDConnectionSort (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_, 
                                         password_));

      addTestcase (new JDConnectionTransactionIsolation (systemObject_,
                                                         namesAndVars_, runMode_, fileOutputStream_, 
                                                         password_));

      addTestcase (new JDConnectionWarnings (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));

      addTestcase (new JDConnectionTranslateHex (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));


      addTestcase (new JDConnectionWrapper (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));


      addTestcase (new JDConnectionCreateXXX (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));

      addTestcase (new JDConnectionClientInfo (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_, pwrSysUserID_, pwrSysPassword_));   


      addTestcase (new JDConnectionDecfloatRoundingMode (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));


      addTestcase (new JDConnectionCreateArrayOf (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));


      addTestcase (new JDConnectionSchema (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));



	  addTestcase (new JDConnectionAbort (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_));

	  addTestcase (new JDConnectionNetworkTimeout (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_));


      addTestcase (new JDConnectionStress (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_, 
                                         password_, pwrSysUserID_, pwrSysPassword_));          

      addTestcase (new JDConnectionCCSID (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_, 
                                         password_, pwrSysUserID_, pwrSysPassword_));    

      addTestcase (new JDConnectionQueryRplParm (systemObject_,
                                              namesAndVars_, runMode_, fileOutputStream_, 
                                              password_, pwrSysUserID_, pwrSysPassword_));    

      addTestcase (new JDConnectionNumericRangeError (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));

      addTestcase (new JDConnectionCharacterTruncation (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));


   }



}



