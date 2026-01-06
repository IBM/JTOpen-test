///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDStatementTest.java
 //
 // Classes:      JDStatementTest
 //
 ////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.sql.Connection;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.JD.JDSetupCollection;
import test.JD.Statement.JDStatementBatch;
import test.JD.Statement.JDStatementClose;
import test.JD.Statement.JDStatementCloseOnCompletion;
import test.JD.Statement.JDStatementCursorName;
import test.JD.Statement.JDStatementDRDA;
import test.JD.Statement.JDStatementDelimiters;
import test.JD.Statement.JDStatementExceptions;
import test.JD.Statement.JDStatementExecute;
import test.JD.Statement.JDStatementGetGeneratedKeys;
import test.JD.Statement.JDStatementGetGeneratedKeys2;
import test.JD.Statement.JDStatementMisc;
import test.JD.Statement.JDStatementMisc2;
import test.JD.Statement.JDStatementPackageCache;
import test.JD.Statement.JDStatementQueryTimeout;
import test.JD.Statement.JDStatementResults;
import test.JD.Statement.JDStatementScroll;
import test.JD.Statement.JDStatementStressCCSID;
import test.JD.Statement.JDStatementStressTest;
import test.JD.Statement.JDStatementStressTest201;
import test.JD.Statement.JDStatementStressTest202;
import test.JD.Statement.JDStatementStressTest205;
import test.JD.Statement.JDStatementStressTest206;
import test.JD.Statement.JDStatementWarnings;
import test.JD.Statement.JDStatementWrapper;
import test.JD.Statement.JDTableName;



/**
Test driver for the JDBC Statement class.
**/
public class JDStatementTest
extends JDTestDriver
{



    /**
   * 
   */
    // Private data.
    public static  String COLLECTION     = "JDTESTSTMT";




/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.

@exception Exception If an exception occurs.
**/
    public static void main (String args[])
        throws Exception
    {
        runApplication (new JDStatementTest (args));
    }



/**
Constructs an object for applets.

@exception Exception If an exception occurs.
**/
    public JDStatementTest ()
        throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.

@exception Exception If an exception occurs.
**/
    public JDStatementTest (String[] args)
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
        Connection c = getConnection (getBaseURL (),
            userId_, encryptedPassword_);

        if(testLib_ != null) {   // @E1A
            COLLECTION = testLib_;
        }
        JDSetupCollection.create (systemObject_,  c,COLLECTION, out_);
	if (getDriver() != JDTestDriver.DRIVER_JCC) {
	    c.commit(); // for xa
	}
        c.close ();
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
        
     	 
        addTestcase (new JDStatementBatch (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        addTestcase (new JDStatementClose (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        addTestcase (new JDStatementCursorName (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        addTestcase (new JDStatementDRDA (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        addTestcase (new JDStatementExecute (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));


	JDStatementGetGeneratedKeys tcGGK = new JDStatementGetGeneratedKeys (systemObject_,
							  namesAndVars_, runMode_, fileOutputStream_, 
							  password_);
	tcGGK.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, 
                                 proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, 
                                 namesAndVars_, runMode_, fileOutputStream_,  
                                 pwrSysUserID_, pwrSysPassword_);
        tcGGK.setProxy5(proxy5_); 

	addTestcase (tcGGK);


	addTestcase (new JDStatementGetGeneratedKeys2 (systemObject_,
						      namesAndVars_, runMode_, fileOutputStream_, 
						      password_));

        addTestcase (new JDStatementMisc (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, pwrSysUserID_, pwrSysPassword_));

        addTestcase (new JDStatementMisc2 (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        addTestcase (new JDStatementQueryTimeout (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, pwrSysUserID_, pwrSysPassword_));

        addTestcase (new JDStatementResults (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

	JDStatementWarnings jdStatementWarnings = new JDStatementWarnings (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_);
	jdStatementWarnings.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, 
                                 proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, 
                                 namesAndVars_, runMode_, fileOutputStream_,  
                                 pwrSysUserID_, pwrSysPassword_);
	jdStatementWarnings.setProxy5(proxy5_);
        addTestcase (jdStatementWarnings);


        addTestcase( new JDStatementDelimiters(systemObject_,
                                               namesAndVars_, runMode_, fileOutputStream_, 
                                               password_));

	JDStatementStressTest  tcStress = new JDStatementStressTest (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, misc_);

	tcStress.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, 
                                 proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, 
                                 namesAndVars_, runMode_, fileOutputStream_,  
                                 pwrSysUserID_, pwrSysPassword_);
        tcStress.setProxy5(proxy5_);
        addTestcase (tcStress);


	JDStatementStressTest201  tcStress201 = new JDStatementStressTest201 (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, misc_);

	tcStress201.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, 
                                 proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, 
                                 namesAndVars_, runMode_, fileOutputStream_,  
                                 pwrSysUserID_, pwrSysPassword_);
        tcStress201.setProxy5(proxy5_);
        addTestcase (tcStress201);

	JDStatementStressTest202  tcStress202 = new JDStatementStressTest202 (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, misc_);

	tcStress202.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, 
                                 proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, 
                                 namesAndVars_, runMode_, fileOutputStream_,  
                                 pwrSysUserID_, pwrSysPassword_);
        tcStress202.setProxy5(proxy5_);
        addTestcase (tcStress202);


	JDStatementStressTest205  tcStress205 = new JDStatementStressTest205 (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, misc_);

	tcStress205.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, 
                                 proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, 
                                 namesAndVars_, runMode_, fileOutputStream_,  
                                 pwrSysUserID_, pwrSysPassword_);
        tcStress205.setProxy5(proxy5_);

        addTestcase (tcStress205);


	JDStatementStressTest206  tcStress206 = new JDStatementStressTest206 (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, misc_);

	tcStress206.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, 
                                 proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, 
                                 namesAndVars_, runMode_, fileOutputStream_,  
                                 pwrSysUserID_, pwrSysPassword_);
        tcStress206.setProxy5(proxy5_);

        addTestcase (tcStress206);



	JDStatementStressCCSID  tcStressCcsid = new JDStatementStressCCSID (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, misc_);

	tcStressCcsid.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, 
                                 proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, 
                                 namesAndVars_, runMode_, fileOutputStream_,  
                                 pwrSysUserID_, pwrSysPassword_);
        tcStressCcsid.setProxy5(proxy5_);

        addTestcase (tcStressCcsid);



	    addTestcase (new JDTableName (systemObject_,
					  namesAndVars_, runMode_, fileOutputStream_, 
					  password_));
	
        addTestcase (new JDStatementPackageCache (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, pwrSysUserID_, pwrSysPassword_));

	
	    addTestcase (new JDScalarFunctions (systemObject_,
						namesAndVars_, runMode_, fileOutputStream_, 
						password_));
	


        addTestcase (new JDStatementWrapper (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        addTestcase (new JDStatementExceptions (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_, pwrSysUserID_, pwrSysPassword_));

        addTestcase (new JDStatementCloseOnCompletion (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));


        addTestcase (new JDStatementScroll (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));


    }


}


