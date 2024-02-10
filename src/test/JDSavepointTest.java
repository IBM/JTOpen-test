///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSavepointTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDSavepointTest.java
//
// Classes:      JDSavepointTest
//
////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.JD.JDSetupCollection;

/**
Test driver for the JDBC SavePoints class.
**/
public class JDSavepointTest
      extends JDTestDriver {



   // Constants.
   public static  String COLLECTION = "JDTESTSVPT";
   public static  String TABLE = "SAVEPT";

   public static String SAVEPOINTTEST_GET = COLLECTION + "." + TABLE;


   // Private data.
   private Connection  connection_;
   private Statement   statement_;



/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.

@exception Exception If an exception occurs.
**/
   public static void main (String args[])
   throws Exception
   {
      runApplication (new JDSavepointTest (args));
   }



/**
Constructs an object for applets.

@exception Exception If an exception occurs.
**/
   public JDSavepointTest ()
   throws Exception
   {
      super();
   }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.

@exception Exception If an exception occurs.
**/
   public JDSavepointTest (String[] args)
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
      boolean tableCreated;
      super.setup(); 

      // Initialization.
      connection_ = getConnection (getBaseURL (),
                                   systemObject_.getUserId (), encryptedPassword_);
      if (testLib_ != null) { 
         COLLECTION = testLib_;
         
         SAVEPOINTTEST_GET     = COLLECTION + "." + TABLE;
      }
      
      JDSetupCollection.create (systemObject_, 
                                connection_, COLLECTION);

      statement_ = connection_.createStatement ();

      // The native driver does not do escape processing by default
      // so we need to explicitly turn it on.
      statement_.setEscapeProcessing (true);

      // Setup SAVEPOINTTEST_GET table.

      tableCreated = safeExecuteUpdate(statement_, "CREATE TABLE " + SAVEPOINTTEST_GET + "(C_INTEGER INTEGER)");

      if (tableCreated) {
         
         statement_.executeUpdate ("INSERT INTO " + SAVEPOINTTEST_GET
                                   + " (C_INTEGER)"
                                   + " VALUES (1)");
      } // tableCreated SAVEPOINTTEST_GET
      
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
      statement_.executeUpdate ("DROP TABLE " + SAVEPOINTTEST_GET);
      statement_.close ();
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
	 	
      addTestcase (new JDSavepointsTestcase  (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

   }
}




