///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CommandHelpTest.java
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

import test.TestDriver;
import test.Cmd.CommandBeanInfoTestcase;
import test.Cmd.CommandHelpRetrieverTestcase;
import test.Cmd.CommandListBeanInfoTestcase;
import test.Cmd.CommandListTestcase;
import test.Cmd.CommandTestcase;

/**
  Test driver for the Command Help components.
  The testcases will be attempted in unattended mode.
  No input parms are required when calling this test driver.  They will be ignored if specified.
  See TestDriver for remaining calling syntax.
  @see TestDriver
 **/
public class CommandHelpTest extends TestDriver
{

    /**
      Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new CommandHelpTest(args));
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
    public CommandHelpTest() throws Exception
    {
        super();
    }

    /**
      This ctor used for applications.
      @param  args  the array of command line arguments
      @exception  Exception  Incorrect arguments will cause an exception
     **/
    public CommandHelpTest(String[] args) throws Exception
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
        boolean allTestcases = (namesAndVars_.size() == 0);

        // Repeat the following 'if' block for each testcase.
        if (allTestcases || namesAndVars_.containsKey("CommandTestcase"))
        {
            CommandTestcase tc = new CommandTestcase(systemObject_, (Vector) namesAndVars_.get("CommandTestcase"), runMode_, fileOutputStream_ );
            testcases_.addElement(tc);
            namesAndVars_.remove("CommandTestcase");
        }

        if (allTestcases || namesAndVars_.containsKey("CommandBeanInfoTestcase"))
        {
            CommandBeanInfoTestcase tc = new CommandBeanInfoTestcase(systemObject_, (Vector) namesAndVars_.get("CommandBeanInfoTestcase"), runMode_, fileOutputStream_ );
            testcases_.addElement(tc);
            namesAndVars_.remove("CommandBeanInfoTestcase");
        }

        if (allTestcases || namesAndVars_.containsKey("CommandListTestcase"))
        {
            CommandListTestcase tc = new CommandListTestcase(systemObject_, (Vector) namesAndVars_.get("CommandListTestcase"), runMode_, fileOutputStream_ );
            testcases_.addElement(tc);
            namesAndVars_.remove("CommandListTestcase");
        }

        if (allTestcases || namesAndVars_.containsKey("CommandListBeanInfoTestcase"))
        {
            CommandListBeanInfoTestcase tc = new CommandListBeanInfoTestcase(systemObject_, (Vector) namesAndVars_.get("CommandListBeanInfoTestcase"), runMode_, fileOutputStream_ );
            testcases_.addElement(tc);
            namesAndVars_.remove("CommandListBeanInfoTestcase");
        }


        if (allTestcases || namesAndVars_.containsKey("CommandHelpRetrieverTestcase"))
        {
            CommandHelpRetrieverTestcase tc = new CommandHelpRetrieverTestcase(systemObject_, (Vector) namesAndVars_.get("CommandHelpRetrieverTestcase"), runMode_, fileOutputStream_ );
            testcases_.addElement(tc);
            namesAndVars_.remove("CommandHelpRetrieverTestcase");
        }


        // Put out error message for each invalid testcase name.
        for (Enumeration e = namesAndVars_.keys(); e.hasMoreElements();)
        {
            System.out.println("Testcase " + e.nextElement() + " not found.");
        }
    }
}

