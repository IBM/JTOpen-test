///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSTests.java
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

import test.IFS.IFSConnectTestcase;
import test.IFS.IFSCopyTestcase;
import test.IFS.IFSCrtDltTestcase;
import test.IFS.IFSCtorTestcase;
import test.IFS.IFSEventTestcase;
import test.IFS.IFSFileAttrTestcase;
import test.IFS.IFSFileDescriptorTestcase;
import test.IFS.IFSJavaFileTestcase;
import test.IFS.IFSListFilesTestcase;
import test.IFS.IFSLockTestcase;
import test.IFS.IFSMiscTestcase;
import test.IFS.IFSPropertyTestcase;
import test.IFS.IFSReadTestcase;
import test.IFS.IFSSerializeTestcase;
import test.IFS.IFSSystemViewTestcase;
import test.IFS.IFSWriteTestcase;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Random;

public class IFSTests extends TestDriver
{
  public static boolean IsRunningOnOS400 = false;
  public static  String COLLECTION     = "JDIFST";


  /**
   Run the test as an application.  This should be called
   from the test driver's main().

   @param  args        The command line arguments.
   @exception  Exception  If an exception occurs.
   **/
  public static void main (String args[])
    throws Exception
  {
    runApplication (new IFSTests (args));
    System.exit(0);
  }

  public IFSTests()
    throws Exception
  {
    super();
  }


  public IFSTests(String[] args) throws Exception
  {
    super(args);
  }


   public void setup ()
   throws Exception
   {
      super.setup(); 
      if (testLib_ != null) { 
         COLLECTION = testLib_;
      } else {
	  // Create a random name
	  Random random = new Random(); 
	 COLLECTION = COLLECTION+random.nextInt(100); 
      }

	
      IsRunningOnOS400 = JTOpenTestEnvironment.isOS400; 
   

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

    	  
	
      IsRunningOnOS400 = JTOpenTestEnvironment.isOS400;
	
	
	
  	if ((pwrSys_.getUserId()).equals(""))
    {               	
      	System.out.println("Warning: -pwrSys option not specified. Some " +
        "variations may fail.");
      	usage();
        
    }


    addTestcase (new IFSConnectTestcase (systemObject_, userId_, password_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSCtorTestcase (systemObject_, userId_, password_, 
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSCrtDltTestcase (systemObject_, userId_, password_, 
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSLockTestcase (systemObject_, userId_, password_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSReadTestcase (systemObject_, userId_, password_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSWriteTestcase (systemObject_, userId_, password_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSFileAttrTestcase (systemObject_, userId_,password_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSMiscTestcase (systemObject_, userId_, password_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSPropertyTestcase (systemObject_, userId_, password_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSEventTestcase (systemObject_, userId_, password_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSSerializeTestcase (systemObject_, userId_, password_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSFileDescriptorTestcase (systemObject_, userId_, password_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSJavaFileTestcase (systemObject_, userId_, password_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    addTestcase (new IFSListFilesTestcase (systemObject_, userId_, password_,
                                  namesAndVars_, runMode_,
                                  fileOutputStream_,  pwrSys_));
    try {
      Class[] parmTypes = { String.class };
      Class.forName("com.ibm.as400.access.IFSFile").getDeclaredMethod("copyTo", parmTypes);
      addTestcase (new IFSCopyTestcase (systemObject_, userId_, password_,
                                        namesAndVars_, runMode_,
                                        fileOutputStream_,  pwrSys_));
    }
    catch (NoSuchMethodException e) {
      addSkipTestcase("IFSCopyTestcase"); 
      System.out.println("Method IFSFile.copyTo() was not found.  IFSCopyTestcase will be excluded.");
    }
    catch (ClassNotFoundException e) { // this will never happen
      e.printStackTrace();
    }

    
    if (!IsRunningOnOS400)
    {
    
      try {
	    Class.forName("com.ibm.as400.vaccess.IFSFileSystemView");
      }
      catch (ClassNotFoundException e1)
      {
	// e1.printStackTrace(System.out);
	  addSkipTestcase("IFSFileSystemViewTestcase"); 
        System.out.println("Class IFSFileSystemView was not found.  IFSFileSystemViewTestcase will be excluded.");
      }
      catch (Throwable t)
      {
	System.out.println("Exception thrown");
	t.printStackTrace(); 
        System.out.println("No graphical environment available.  IFSFileSystemViewTestcase will be excluded.");
	addSkipTestcase("IFSFileSystemViewTestcase"); 
        try {
          systemObject_.setGuiAvailable(false);
          pwrSys_.setGuiAvailable(false);
        } catch (PropertyVetoException e) {}  // will never happen
      }

      try {
	    Class.forName("com.ibm.as400.access.IFSSystemView");
        addTestcase (new IFSSystemViewTestcase (systemObject_,  userId_, password_,
                                                namesAndVars_, runMode_,
                                                fileOutputStream_,  pwrSys_));
      }
      catch (ClassNotFoundException e1)
      {
        System.out.println("Class IFSSystemView was not found.  IFSSystemViewTestcase will be excluded.");
      }
      catch (Throwable t)
      {
	System.out.println("Exception thrown");
	  t.printStackTrace(); 
        System.out.println("No graphical environment available.  IFSSystemViewTestcase will be excluded.");
        try {
          systemObject_.setGuiAvailable(false);
          pwrSys_.setGuiAvailable(false);
        } catch (PropertyVetoException e) {}  // will never happen
      }
    }

    // Reports invalid testcase names.
    for (Enumeration<String> e = namesAndVars_.keys (); e.hasMoreElements (); ) {
      String name = e.nextElement ();
      System.out.println ("IFSTests: Testcase " + name + " not found.");
    }
  }

/**
 * Display usage information for the Testcase.
 * This method never returns.
**/
  public void usage()
  {
    System.out.println ("usage: java IFSTests -system systemName -uid userID -pwd password -pwrSys userID,password  ");
  }
}
