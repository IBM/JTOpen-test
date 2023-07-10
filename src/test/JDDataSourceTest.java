///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDataSourceTest.java
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
// File Name:    JDDataSourceTest.java
//
// Classes:      JDDataSourceTest
//
////////////////////////////////////////////////////////////////////////
//
//
//                 v5r2m0t   07/24/2001  kcoover   Created.
//
//
////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.sql.Connection;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;  




/**
Test driver for the ConnectionPoolDataSource and DataSource class.
**/
public class JDDataSourceTest
      extends JDTestDriver {



   // Constants.
   public static  String COLLECTION     = "JDTESTDATASOURCE";

   public static String DATASOURCETEST_GET     = COLLECTION + ".DATASOURCETESTGET";


   // Private data.
   private Connection  connection_;



/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.

@exception Exception If an exception occurs.
**/
   public static void main (String args[])
   throws Exception
   {
      runApplication (new JDDataSourceTest (args));
   }



/**
Constructs an object for applets.

@exception Exception If an exception occurs.
**/
   public JDDataSourceTest ()
   throws Exception
   {
      super();
   }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.

@exception Exception If an exception occurs.
**/
   public JDDataSourceTest (String[] args)
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
      super.setup(); 

      // Initialization.
      connection_ = getConnection (getBaseURL (),
                                   systemObject_.getUserId (), encryptedPassword_);
      if (testLib_ != null) { 
         COLLECTION = testLib_;
         
         DATASOURCETEST_GET     = COLLECTION + ".DATASOURCETESTGET";
      }
      
      JDSetupCollection.create (systemObject_, 
                                connection_, COLLECTION);

      connection_.commit(); // needed for XA testing
   }



/**
Cleanup - - this does not run automatically - - it is called by JDCleanup.
**/
   public static void dropCollections(Connection c)
   {
      dropCollection(c, COLLECTION);
   }



/**
Performs setup needed after running testcases.

@exception Exception If an exception occurs.
**/
   public void cleanup ()
   throws Exception
   {
      connection_.close ();
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
  	     		// TODO Auto-generated catch block
  				e.printStackTrace();
  			} 
  	     	catch (IOException e) 
  	     	{
  				// TODO Auto-generated catch block
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
	   
      addTestcase (new JDConnectionPoolDataSource  (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

      addTestcase (new JDDataSource  (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

      addTestcase (new JDDataSourceSocketOptionsTestcase(systemObject_,
                                                         namesAndVars_, runMode_, fileOutputStream_, 
                                                         password_));

   }
}




