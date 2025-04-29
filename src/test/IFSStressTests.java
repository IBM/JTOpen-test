///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSStressTests.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;

import com.ibm.as400.access.AS400;            //@A1A
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.IFS.IFSStressTestcase;

import java.io.*;
import java.util.Enumeration;


public class IFSStressTests extends TestDriver
{
  public static void main(String[] args)
  {
    try
    {
      IFSStressTests tests = new IFSStressTests(args);
      tests.init();
      tests.start();
      tests.stop();
      tests.destroy();
    }
    catch(Exception e)
    {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    }

       System.exit(0);
  }

  public IFSStressTests()
    throws Exception
  {
    super();
  }


  public IFSStressTests(String[] args) throws Exception
  {
    super(args);
  }

  public void createTestcases()
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
	  	
	     

	  	if ((pwrSys_.getUserId()).equals(""))
	    {        
	        	
	      	System.out.println("Warning: -pwrSys option not specified. Some " +
	        "variations may fail.");
	      	usage();
	        
	    }
	
	    //Changed misc_ to pwrSys_ so all test drivers are consistent.           


  // Added pwrSys parameter - one variation needs user with more authority          @A1C
    addTestcase (new IFSStressTestcase (systemObject_,userId_ ,password_,
                                      namesAndVars_, runMode_,
                                      fileOutputStream_,  pwrSys_));//@A1C

    for (Enumeration<String> e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + "unknown.");
    }
  }

/**
 * Display usage information for the Testcase.
 * This method never returns.
**/
  public void usage() // @A1A
  {
	  System.out.println ("usage: java test.IFSStressTests -system systemName -uid userID -pwd password -pwrSys userID,password  ");
	  System.out.println ("  e.g. java test.IFSStressTests -system lp03ut12   -uid java   -pwd xxxxxxx  -pwrSys javactl,xxxxxxxx  ");

  }
}







