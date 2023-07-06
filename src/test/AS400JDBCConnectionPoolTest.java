///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCConnectionPoolTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

/**
  Test driver for the JDBC Data Source classes.
  The testcases will be attempted in unattended mode.
  No input parms are required when calling this test driver; they will be ignored if specified.
  See TestDriver for remaining calling syntax.
  @see TestDriver
 **/
public class AS400JDBCConnectionPoolTest extends TestDriver
{

   /**
     Main for running standalone application tests.
    **/
   public static void main(String args[])
   {
     try
      {
         TestDriver.runApplication(new AS400JDBCConnectionPoolTest(args));
      }
     catch (Exception e)
     {
           System.out.println("Program terminated abnormally.");
         e.printStackTrace();
        }

     // Needed to make the virtual machine quit.
        System.exit(0);
   }

   /**
     This ctor used for applets.
     @exception  Exception  Initialization errors may cause an exception.
    **/
   public AS400JDBCConnectionPoolTest() throws Exception
   {
      super();
   }

   /**
     This ctor used for applications.
     @param  args  the array of command line arguments
     @exception  Exception  Incorrect arguments will cause an exception
    **/
   public AS400JDBCConnectionPoolTest(String[] args) throws Exception
   {
      super(args);
   }

   /**
     Creates Testcase objects for all the testcases in this component.
    **/

   public void createTestcases()
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
	
   	
      // Instantiate all testcases to be run.
      boolean allTestcases = (namesAndVars_.size() == 0);

      // Repeat the following 'if' block for each testcase.
      if (allTestcases || namesAndVars_.containsKey("ConnectionPoolPropertiesTestcase"))
      {
         ConnectionPoolPropertiesTestcase tc = new ConnectionPoolPropertiesTestcase(systemObject_, (Vector) namesAndVars_.get("ConnectionPoolPropertiesTestcase"), runMode_, fileOutputStream_,  password_);
         testcases_.addElement(tc);
         namesAndVars_.remove("ConnectionPoolPropertiesTestcase");
      }
      //@A1D if (allTestcases || namesAndVars_.containsKey("ConnectionPoolPropertiesBeanInfoTestcase"))
      //@A1D {
      //@A1D    ConnectionPoolPropertiesBeanInfoTestcase tc = new ConnectionPoolPropertiesBeanInfoTestcase(systemObject_, (Vector) namesAndVars_.get("ConnectionPoolPropertiesBeanInfoTestcase"), runMode_, fileOutputStream_, output_, password_);
      //@A1D    testcases_.addElement(tc);
      //@A1D    namesAndVars_.remove("ConnectionPoolPropertiesBeanInfoTestcase");
      //@A1D }

      if (allTestcases || namesAndVars_.containsKey("AS400JDBCPooledConnectionTestcase"))
      {
         AS400JDBCPooledConnectionTestcase tc = new AS400JDBCPooledConnectionTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCPooledConnectionTestcase"), runMode_, fileOutputStream_,  password_);
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCPooledConnectionTestcase");
      }
      if (allTestcases || namesAndVars_.containsKey("AS400JDBCConnectionPoolTestcase"))
      {
         AS400JDBCConnectionPoolTestcase tc = new AS400JDBCConnectionPoolTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCConnectionPoolTestcase"), runMode_, fileOutputStream_,  password_);
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCConnectionPoolTestcase");
      }

 
      if (allTestcases || namesAndVars_.containsKey("AS400JDBCConnectionPoolBeanInfoTestcase"))
      {
         AS400JDBCConnectionPoolBeanInfoTestcase tc = new AS400JDBCConnectionPoolBeanInfoTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCConnectionPoolBeanInfoTestcase"), runMode_, fileOutputStream_,  password_);
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCConnectionPoolBeanInfoTestcase");
      }
      if (allTestcases || namesAndVars_.containsKey("ConnectionPoolBeanInfoTestcase"))
      {
         ConnectionPoolBeanInfoTestcase tc = new ConnectionPoolBeanInfoTestcase(systemObject_, (Vector) namesAndVars_.get("ConnectionPoolBeanInfoTestcase"), runMode_, fileOutputStream_,  password_);
         testcases_.addElement(tc);
         namesAndVars_.remove("ConnectionPoolBeanInfoTestcase");
      }

      if (allTestcases || namesAndVars_.containsKey("AS400JDBCManagedConnectionPoolTestcase"))
      {
         AS400JDBCManagedConnectionPoolTestcase tc = new AS400JDBCManagedConnectionPoolTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCManagedConnectionPoolTestcase"), runMode_, fileOutputStream_,  password_, pwrSys_, pwrSysPassword_);///, duration_);
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCManagedConnectionPoolTestcase");
      }

      if (allTestcases || namesAndVars_.containsKey("AS400JDBCManagedConnectionPool2Testcase"))
      {
         AS400JDBCManagedConnectionPool2Testcase tc = new AS400JDBCManagedConnectionPool2Testcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCManagedConnectionPool2Testcase"), runMode_, fileOutputStream_,  password_, pwrSys_, pwrSysPassword_);///, duration_);
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCManagedConnectionPool2Testcase");
      }

      // Put out error message for each invalid testcase name.
      for (Enumeration e = namesAndVars_.keys(); e.hasMoreElements();)
      {
         System.out.println("Testcase " + e.nextElement() + " not found.");
      }
   }
}

