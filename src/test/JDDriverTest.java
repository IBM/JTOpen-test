///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDriverTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDriverTest.java
//
// Classes:      JDDriverTest
//
////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.sql.Connection;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;



/**
Test driver for the JDBC Driver class.
**/
public class JDDriverTest
extends JDTestDriver
{

    /**
   * 
   */
  private static final long serialVersionUID = 1L;
    public static  String COLLECTION     = "JDTESTDRVR";

    /**
    Run the test as an application.  This should be called
    from the test driver's main().
    
    @param  args        The command line arguments.
    @exception  Exception  If an exception occurs.
    **/
    public static void main(String args[])
    throws Exception
    {
        runApplication(new JDDriverTest(args));
    }



    /**
    Constructs an object for applets.
    @exception  Exception  If an exception occurs.
    **/
    public JDDriverTest()
    throws Exception
    {
        super();
    }



    /**
    Constructs an object for testing applications.
    
    @param      args        The command line arguments.
    @exception  Exception  If an exception occurs.
    **/
    public JDDriverTest(String[] args)
    throws Exception
    {
        super(args);
    }

    /**
    Performs setup needed before running testcases.
    @exception Exception If an exception occurs.
    **/
    public void setup()
    throws Exception
    {
        super.setup();
        Connection c = getConnection(getBaseURL(), userId_, encryptedPassword_);

        if(testLib_ != null)
        {
            COLLECTION = testLib_;
        }
        JDSetupCollection.create(systemObject_,  c, COLLECTION);
        try { 
        c.commit(); // for xa
        } catch (Exception e) {} 
        c.close();
    }

    /**
    Cleanup - - this does not run automatically - - it is called by JDCleanup.
**/
    public static void dropCollections(Connection c)
    {
        dropCollection(c, COLLECTION);
    }



    /**
    Creates the testcases.
    **/
    public void createTestcases2()
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
   	 
        addTestcase(new JDDriverAcceptsURL(systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

        
        addTestcase(new JDDriverGetPropertyInfo(systemObject_,
                                                namesAndVars_, runMode_, fileOutputStream_, 
                                                password_));

        
        JDDriverMisc jdDriverMisc =new JDDriverMisc(systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, pwrSysUserID_, pwrSysPassword_) ;
        System.out.println("DEBUG: JDDriverTest proxy is "+proxy_); 
        // Make sure the proxy value is set
         
        jdDriverMisc.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, 
                                 proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, 
                                 namesAndVars_, runMode_, fileOutputStream_, 
                                 pwrSysUserID_, pwrSysPassword_);
                                  
        addTestcase(jdDriverMisc);


        JDDriverTimestamp jdDriverTimestamp =new JDDriverTimestamp(systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, pwrSysUserID_, pwrSysPassword_) ;
        System.out.println("DEBUG: JDDriverTest proxy is "+proxy_); 
        // Make sure the proxy value is set
         
        jdDriverTimestamp.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, 
                                 proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, 
                                 namesAndVars_, runMode_, fileOutputStream_, 
                                 pwrSysUserID_, pwrSysPassword_);
                                  
        addTestcase(jdDriverTimestamp);
        


        addTestcase(new JDDriverPerformance(systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_, pwrSysUserID_, pwrSysPassword_));

	// Put JDDriverConnect at the end of the list.  

        JDDriverConnect jdDriverConnect = new JDDriverConnect(systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, pwrSysUserID_, pwrSysPassword_); 
        jdDriverConnect.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, 
            proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, 
            namesAndVars_, runMode_, fileOutputStream_, 
            pwrSysUserID_, pwrSysPassword_);
        addTestcase(jdDriverConnect);




    }

}


