///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDXMLTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDXMLTest.java
//
// Classes:      JDXMLTest
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;



/**
Test driver for the JDBC Lob classes.
**/
public class JDXMLTest
extends JDTestDriver {



    // Constants.
    public static  String COLLECTION     = "JDTESTXML";



    /**
    Run the test as an application.  This should be called
    from the test driver's main().
    
    @param  args        The command line arguments.
    
    @exception Exception If an exception occurs.
    **/
    public static void main (String args[])
    throws Exception
    {
        runApplication (new JDXMLTest (args));
    }



    /**
    Constructs an object for applets.
    
    @exception Exception If an exception occurs.
    **/
    public JDXMLTest ()
    throws Exception
    {
        super();
    }



    /**
    Constructs an object for testing applications.
    
    @param      args        The command line arguments.
    
    @exception Exception If an exception occurs.
    **/
    public JDXMLTest (String[] args)
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
        super.setup(); // @D1A

        // Initialization.
        Connection connection = DriverManager.getConnection (getBaseURL (),
                                                             systemObject_.getUserId (), password_);

        if (testLib_ != null) {  // @E1A
            COLLECTION = testLib_;
        }

        JDSetupCollection.create (systemObject_, 
                                  connection, COLLECTION);
    }



    /**
    Performs setup needed after running testcases.
    
    @exception Exception If an exception occurs.
    **/
    public void cleanup ()
    throws Exception
    {
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



        addTestcase (new JDXMLBlob (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

        addTestcase (new JDXMLBlobLocator (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));


        addTestcase (new JDXMLClob (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

        addTestcase (new JDXMLClobLocator (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        
        addTestcase (new JDXMLXML (systemObject_,
                namesAndVars_, runMode_, fileOutputStream_, 
                password_));


        
/* 


        addTestcase (new JDXMLInputStream (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

        addTestcase (new JDXMLReader (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_, 
                                      password_));

        addTestcase (new JDXMLGraphicData(systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));


	addTestcase (new JDXMLClobLocator8 (systemObject_,
					       namesAndVars_, runMode_, fileOutputStream_, 
					       password_));


	addTestcase (new JDXMLClobLocator5035 (systemObject_,
					       namesAndVars_, runMode_, fileOutputStream_, 
					       password_));

	addTestcase (new JDXMLClob5035 (systemObject_,
					       namesAndVars_, runMode_, fileOutputStream_, 
					       password_));

	addTestcase (new JDXMLClob8 (systemObject_,
					       namesAndVars_, runMode_, fileOutputStream_, 
					       password_));

*/ 


    }

}




