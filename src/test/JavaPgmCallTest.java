///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JavaPgmCallTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JavaPgmCallTest.java
 //
 ////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import com.ibm.as400.access.*;

/**
The JavaPgmCallTest class provides the test driver for the testcase files.
<p>The test driver contains two testcase files :
<ul>
<li>JavaAppTestcase
<li>RunJavaAppTestcase
<li>RunJavaAppAttendedTestcase
<li>VJavaAppTestcase
<li>VJavaAppAttendedTestcase
<li>JavaAppBeanInfoTestcase
<li>VJavaAppBeanInfoTestcase
</ul>
**/
public class JavaPgmCallTest extends TestDriver
{
/**
Main for running standalone application tests.
**/
  public static void main(String args[])
  {
    try
    {
      System.out.println("");
      System.out.println("Welcome to the JavaProgramCall test cases.  There are ");
      System.out.println("only two requirements for these test cases:");
      System.out.println("   1. The i5/OS server must have a JVM");
      System.out.println("   2. Directory /home/JavaTest on the i5/OS server must have test programs.");
      System.out.println("");
      System.out.println("The test programs are in file JavaPgmCallTestPrograms.zip in the ");
      System.out.println("test directory.  Copy these programs to the i5/OS server before running the ");
      System.out.println("test cases.");
      System.out.println("");

      JavaPgmCallTest example = new JavaPgmCallTest(args);
      example.init();
      example.start();
      example.stop();
      example.destroy();
    }
    catch (Exception e)
    {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    }

       System.exit(0);
  }

/**
This ctor used for applets.
@exception Exception Initialization errors may cause an exception.
**/
  public JavaPgmCallTest()
       throws Exception
  {
    super();
  }

/**
This ctor used for applications.
@param args the array of command line arguments
@exception Exception Incorrect arguments will cause an exception
**/
  public JavaPgmCallTest(String[] args)
       throws Exception
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
		  		systemObject_.connectService(AS400.COMMAND);
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
	     	    Job[] jobs = systemObject_.getJobs(AS400.COMMAND);
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
    // boolean allTestcases = (namesAndVars_.size() == 0);
    try
    {
         systemObject_.connectService(AS400.COMMAND);
    }
    catch(Exception e)
    {
        System.out.println(" connect error");
    }
    // Setup the power user if provided.

    // $$$ TO DO $$$
    
    Testcase[] testcases =
    {
        new JavaAppTestcase(),
        new JavaAppBeanInfoTestcase()
    };

    for (int i = 0; i < testcases.length; ++i)
    {
        testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_,  pwrSysUserID_, pwrSysPassword_);
        addTestcase(testcases[i]);
    }
  }
}


