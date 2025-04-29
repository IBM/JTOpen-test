///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSJavaTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.IFS.IFSJavaFileTestcase;

import java.io.IOException;
import java.util.Enumeration;



/**
Test driver for the Message components.
Refer to TestDriver for calling syntax.
**/
public class IFSJavaTest
extends TestDriver
{

/**
Main for running standalone application tests.

@param  args    The command line arguments.
**/
    public static void main (String args[])
    {
        try {
            IFSJavaTest example = new IFSJavaTest (args);
            example.init ();
            example.start ();
            example.stop ();
            example.destroy ();
        }
        catch (Exception e) {
            System.out.println ("Program terminated abnormally.");
            e.printStackTrace ();
        }

           System.exit(0);
    }



/**
This constructor is used for applets.

@exception Exception Initialization errors may cause an exception.
**/
    public IFSJavaTest ()
        throws Exception
    {
        super ();
    }



/**
This constructor is used for applications.

@param      args        The command line arguments.
@exception  Exception   Incorrect arguments will cause an exception.
**/
    public IFSJavaTest (String[] args)
        throws Exception
    {
        super (args);
    }



/**
Creates Testcase objects for all the testcases in this component.
**/
    public void createTestcases ()
    {
    	   	
    	if(TestDriverStatic.pause_)
    	{ 
       	   try 
       	    {						
       		    systemObject_.connectService(AS400.FILE);
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
    			Job[] jobs = systemObject_.getJobs(AS400.FILE);
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
    	
        // Setup the power user if provided.
     //   AS400 pwrSys = null;
            
      	if ((pwrSys_.getUserId()).equals(""))
        {               	
          	System.out.println("Warning: -pwrSys option not specified. Some " +
            "variations may fail.");
            
        }
        
            
        //Changed misc_ to pwrSys_ so all test drivers are consistent.           

//        if (misc_ == null)
//        {
//          misc_ = new String ("z:,javactl,jteam1");
//        }
        


        // Create the testcases.
        addTestcase (new IFSJavaFileTestcase (systemObject_,
            userId_,password_,
                                              namesAndVars_, runMode_,
                                              fileOutputStream_,
                                               pwrSys_));


        // Put out error message for each invalid testcase name.
        for (Enumeration<String> e = namesAndVars_.keys(); e.hasMoreElements(); ) {
            System.out.println ("Testcase " + e.nextElement() + " not found.");
        }
    }
}
