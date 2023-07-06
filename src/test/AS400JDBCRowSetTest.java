///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCRowSetTest.java
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
  Test driver for the JDBC row set classes.
  The testcases will be attempted in unattended mode.
  No input parms are required when calling this test driver; they will be ignored if specified.
  See TestDriver for remaining calling syntax.
  @see TestDriver
 **/
public class AS400JDBCRowSetTest extends TestDriver
{

   /**
     Main for running standalone application tests.
    **/
   public static void main(String args[])
   {
     try
      {
         TestDriver.runApplication(new AS400JDBCRowSetTest(args));
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
   public AS400JDBCRowSetTest() throws Exception
   {
      super();
   }

   /**
     This ctor used for applications.
     @param  args  the array of command line arguments
     @exception  Exception  Incorrect arguments will cause an exception
   **/
   public AS400JDBCRowSetTest(String[] args) throws Exception
   {
      super(args);
   }

   /**
   *  Creates Testcase objects for all the testcases in this component.
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

      	// Get the jndi type from the misc parameter.
      	String jndiType = null;
      	String ldapUsr = null;  //@A1A
      	String ldapPwd = null;  //@A1A
      
      
  		if (pwrSys_ != null)
  		{               	
  			ldapUsr =  pwrSysUserID_;
  			ldapPwd =  pwrSysPassword_;          
  		}
  		else
  		{
  			System.out.println("Warning: -pwrSys option not specified. Some " +
  			"variations may fail.");
  			usage();
  		}
	
  	
  		if (jndi_ != null)
  		{
  			jndiType = jndi_;
  		}
  		else
  		{
		        jndiType = "file"; 
  			System.out.println("Warning: -jndi option not specified. Defaulting to file");
  		}
    
 
  		//Changed -misc parameter to make all test drivers consistent.
      
    /*  if (misc_ != null)
      {
         try
         {
            StringTokenizer tokenizer = new StringTokenizer (misc_, ",");
            jndiType = tokenizer.nextToken();
	    ldapUsr = tokenizer.nextToken();  //@A1A
	    ldapPwd = tokenizer.nextToken();  //@A1A
         }
         catch (Exception ns)
         {
            usage();
         }
      }*/

      // Repeat the following 'if' block for each testcase.
      if (allTestcases || namesAndVars_.containsKey("AS400JDBCRowSetTestcase"))
      {
         AS400JDBCRowSetTestcase tc = new AS400JDBCRowSetTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCRowSetTestcase"), runMode_, fileOutputStream_,  password_, jndiType, ldapUsr, ldapPwd); //@A1C
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCRowSetTestcase");
      }

      // Repeat the following 'if' block for each testcase.
      if (allTestcases || namesAndVars_.containsKey("AS400JDBCRowSetCtorTestcase"))
      {
         AS400JDBCRowSetCtorTestcase tc = new AS400JDBCRowSetCtorTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCRowSetCtorTestcase"), runMode_, fileOutputStream_,  password_, jndiType, ldapUsr, ldapPwd); //@A1C
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCRowSetCtorTestcase");
      }

      // Repeat the following 'if' block for each testcase.
      if (allTestcases || namesAndVars_.containsKey("AS400JDBCRowSetBeanInfoTestcase"))
      {
         AS400JDBCRowSetBeanInfoTestcase tc = new AS400JDBCRowSetBeanInfoTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCRowSetBeanInfoTestcase"), runMode_, fileOutputStream_,  password_);
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCRowSetBeanInfoTestcase");
      }

      // Put out error message for each invalid testcase name.
      for (Enumeration e = namesAndVars_.keys(); e.hasMoreElements();)
      {
         System.out.println("Testcase " + e.nextElement() + " not found.");
      }
   }

   /**
   *  Displays usage information for running the testcase.
   **/
   public void usage()
   {
      System.out.println("usage: java test.AS400JDBCRowSetTest -system systemName -uid userID -pwd password -pwrsys pwrUser,password -jndi jndiType");
      System.exit(0);
   }
}


