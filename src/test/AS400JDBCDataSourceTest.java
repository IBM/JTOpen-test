///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCDataSourceTest.java
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
public class AS400JDBCDataSourceTest extends TestDriver
{

    public static  String COLLECTION     = "JDAS400DST";

   /**
     Main for running standalone application tests.
    **/
   public static void main(String args[])
   {
     try
      {
         TestDriver.runApplication(new AS400JDBCDataSourceTest(args));
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
   public AS400JDBCDataSourceTest() throws Exception
   {
      super();
   }

   /**
     This ctor used for applications.
     @param  args  the array of command line arguments
     @exception  Exception  Incorrect arguments will cause an exception
    **/
   public AS400JDBCDataSourceTest(String[] args) throws Exception
   {
      super(args);
   }



/**
Performs setup needed before running testcases.

@exception Exception If an exception occurs.
**/
   public void setup () throws Exception    {

      super.setup(); 

        if(testLib_ != null)
        {
            COLLECTION = testLib_;
        }

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

      // Get the jndi type from the misc parameter.
      String jndi = null;
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
    		jndi = jndi_;
    	}
    	else
    	{
	        jndi = "file"; 
          	System.out.println("Warning: -jndi option not specified. Defaulting to 'file'.  Other options are ");
		// usage();
    	}
        
     
      //Changed -misc parameter to make all test drivers consistent.
    /* if (misc_ != null)
      {
         try
         {
            StringTokenizer tokenizer = new StringTokenizer (misc_, ",");
            jndi = tokenizer.nextToken();
	    ldapUsr = tokenizer.nextToken();  //@A1A
	    ldapPwd = tokenizer.nextToken();  //@A1A
         }
         catch (Exception ns)
         {
            usage();
         }
      }
      */

      // Repeat the following 'if' block for each testcase.
      if (allTestcases || namesAndVars_.containsKey("AS400JDBCDataSourceTestcase"))
      {
         AS400JDBCDataSourceTestcase tc = new AS400JDBCDataSourceTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCDataSourceTestcase"), runMode_, fileOutputStream_,  password_, jndi, ldapUsr, ldapPwd, getSystemName());	//@A1C //@A2C
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCDataSourceTestcase");
      }

      if (/*allTestcases ||*/ namesAndVars_.containsKey("AS400JDBCDataSourceSerialTestcase"))
      {
         AS400JDBCDataSourceSerialTestcase tc = new AS400JDBCDataSourceSerialTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCDataSourceSerialTestcase"), runMode_, fileOutputStream_,  password_, jndi, ldapUsr, ldapPwd, getSystemName());
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCDataSourceSerialTestcase");
      }

      if (allTestcases || namesAndVars_.containsKey("AS400JDBCDataSourcePropertiesTestcase"))
      {
         AS400JDBCDataSourcePropertiesTestcase tc = new AS400JDBCDataSourcePropertiesTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCDataSourcePropertiesTestcase"), runMode_, fileOutputStream_,  password_);
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCDataSourcePropertiesTestcase");
      }
      if (allTestcases || namesAndVars_.containsKey("AS400JDBCDataSourceBeanInfoTestcase"))
      {
         AS400JDBCDataSourceBeanInfoTestcase tc = new AS400JDBCDataSourceBeanInfoTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCDataSourceBeanInfoTestcase"), runMode_, fileOutputStream_,  password_);
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCDataSourceBeanInfoTestcase");
      }

      if (allTestcases || namesAndVars_.containsKey("AS400JDBCConnectionPoolDataSourceTestcase"))
      {
         AS400JDBCConnectionPoolDataSourceTestcase tc = new AS400JDBCConnectionPoolDataSourceTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCConnectionPoolDataSourceTestcase"), runMode_, fileOutputStream_,  password_, jndi, ldapUsr, ldapPwd, getSystemName());  //@A1C //@A2C
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCConnectionPoolDataSourceTestcase");
      }
      if (allTestcases || namesAndVars_.containsKey("AS400JDBCConnectionPoolDataSourceBeanInfoTestcase"))
      {
         AS400JDBCConnectionPoolDataSourceBeanInfoTestcase tc = new AS400JDBCConnectionPoolDataSourceBeanInfoTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCConnectionPoolDataSourceBeanInfoTestcase"), runMode_, fileOutputStream_,  password_);
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCConnectionPoolDataSourceBeanInfoTestcase");
      }

      // Testcase for AS400JDBCManagedConnectionPoolDataSource.
      if (allTestcases || namesAndVars_.containsKey("AS400JDBCMCPDSTestcase"))
      {
         AS400JDBCMCPDSTestcase tc = new AS400JDBCMCPDSTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCMCPDSTestcase"), runMode_, fileOutputStream_,  password_, jndi, ldapUsr, ldapPwd, getSystemName());  //@A1C //@A2C
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCMCPDSTestcase");
      }

      // Testcase for AS400JDBCManagedDataSource.
      if (allTestcases || namesAndVars_.containsKey("AS400JDBCMDSTestcase"))
      {
         AS400JDBCMDSTestcase tc = new AS400JDBCMDSTestcase(systemObject_, (Vector) namesAndVars_.get("AS400JDBCMDSTestcase"), runMode_, fileOutputStream_,  password_, jndi, ldapUsr, ldapPwd, getSystemName());  //@A1C //@A2C
         testcases_.addElement(tc);
         namesAndVars_.remove("AS400JDBCMDSTestcase");
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
      System.out.println("usage: java test.AS400JDBCDataSourceTest -system systemName -uid userID -pwd password -pwrsys pwrUser,password -jndi type");
      System.exit(0);
   }
}

