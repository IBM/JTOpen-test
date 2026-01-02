///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetRowTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.JDTestDriver;
import test.TestDriverStatic;
import test.JD.JDSetupCollection;



/**
Test driver for the JDBC ResultSet class.
**/
public class JDRSGetRowTest
      extends JDTestDriver {



   /**
   * 
   */
  Connection connection_; 
  // Constants.
   public static  String COLLECTION     = "JDTESTRS";

   public static String RSTEST_GETROW          = COLLECTION + ".RSTESTGETR";
   public static String RSTEST_GETROW_SMALLER  = COLLECTION + ".RSTESTGET2";
   public static int    RSTEST_GETROW_ROWCOUNT = 137;
   public static int    RSTEST_GETROW_SMALLER_ROWCOUNT = 83;

   public static byte[] BLOB_MEDIUM;
   public static String CLOB_MEDIUM;

   public static byte[] BLOB_FULL;
   public static String CLOB_FULL;
   public static String DBCLOB_FULL;


   // Private data.
   private Statement   statement_;





/**
Constructs an object for applets.

@exception Exception If an exception occurs.
**/
   public JDRSGetRowTest ()
   throws Exception
   {
      super();
   }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.

@exception Exception If an exception occurs.
**/
   public JDRSGetRowTest (String[] args)
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
      super.setup(); // @D2A

      if (connection_ != null) connection_.close();
      // Initialization.
      connection_ = getConnection (getBaseURL (),
                                   systemObject_.getUserId (), encryptedPassword_);

      if (testLib_ != null) 
      {
         COLLECTION = testLib_;
         RSTEST_GETROW         = COLLECTION + ".RSTESTGETR";
         RSTEST_GETROW_SMALLER = COLLECTION + ".RSTESTGET2";
      }

      JDSetupCollection.create (systemObject_, 
                                connection_, COLLECTION, out_);

      statement_ = connection_.createStatement ();

      // The native driver does not do escape processing by default
      // so we need to explicitly turn it on.
      statement_.setEscapeProcessing (true);

        
      initTable(statement_, RSTEST_GETROW, " ( C_INTEGER         INTEGER ,C_VARCHAR_1024      VARCHAR(1024)  ) ");

         for (int i = 0; i < RSTEST_GETROW_ROWCOUNT; i++)
         {
            statement_.executeUpdate ("INSERT INTO " + RSTEST_GETROW
                                     + " ( C_INTEGER, C_VARCHAR_1024 )"        // @D0A
                                     + " VALUES ( " + (i + 1) + " , 'Hi Mom')");      
         }                            

        
      // 
      // Setup a smaller table for getRow.  A table with 83 rows will be created
      //
      initTable(statement_, RSTEST_GETROW_SMALLER, " ( C_INTEGER         INTEGER,C_VARCHAR_1024      VARCHAR(1024)  ) ");

         for (int i = 0; i < RSTEST_GETROW_SMALLER_ROWCOUNT; i++)
         {
            statement_.executeUpdate ("INSERT INTO " + RSTEST_GETROW_SMALLER
                                     + " ( C_INTEGER, C_VARCHAR_1024 )"        // @D0A
                                     + " VALUES ( " + (i + 1) + " , 'Hi Mom')");      
         }                            


   }



/**
Cleanup - - this does not run automatically - - it is called by JDCleanup.
**/
   public static void dropCollections(Connection c)
   {
      dropCollection(c, COLLECTION);
   }



/**
Positions the cursor on a particular row and return the
result set.

@param  rs      The result set.
@param  key     The key.  If null, positions to before the
                first row.

@exception SQLException If an exception occurs.
**/
   public  void position (ResultSet rs, String key)
   throws SQLException
   {
      rs.beforeFirst ();
      position0 (rs, key);
   }



/**
Positions the cursor on a particular row and return the
result set.

@param  rs      The result set.
@param  key     The key.  If null, positions to before the
                first row.

@exception SQLException If an exception occurs.
**/
   public  void position0 (ResultSet rs, String key)
   throws SQLException
   {
      if (key != null) {
         while (rs.next ()) {
            String s = rs.getString ("C_KEY");
            if (s != null)
               if (s.equals (key))
                  return;
         }

         if (key != null)
            out_.println ("Warning: Key " + key + " not found.");
      }
      return ;
   }



/**
Performs setup needed after running testcases.

@exception Exception If an exception occurs.
**/
   public void cleanup ()
   throws Exception
   {
      cleanupTable(statement_, RSTEST_GETROW);
      cleanupTable(statement_, RSTEST_GETROW_SMALLER);

      statement_.close ();
      connection_.close ();
      connection_ = null;
      super.cleanup(); 
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
	     	    out_.println("Host Server job(s): ");

	     	    	for(int i = 0 ; i< jobs.length; i++)
	     	    	{   	    	
	     	    		out_.println(jobs[i]);
	     	    	}    	    
	     	 }
	     	 catch(Exception exc){}
	     	    
	     	 try 
	     	 {
	     	    	out_.println ("Toolbox is paused. Press ENTER to continue.");
	     	    	System.in.read ();
	     	 } 
	     	 catch (Exception exc) {};   	   
	   } 
	   
	   
	   
      addTestcase (new JDRSGetRow_Absolute(
                                   systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));

      addTestcase (new JDRSGetRow_Relative(
                                   systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));

      addTestcase (new JDRSGetRow_Previous(
                                   systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));

      addTestcase (new JDRSGetRow_BeforeFirst(
                                   systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));

      addTestcase (new JDRSGetRow_AfterLast(
                                   systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));


      addTestcase (new JDRSGetRow_First(
                                   systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));


      addTestcase (new JDRSGetRow_Last(
                                   systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));

   }
}




