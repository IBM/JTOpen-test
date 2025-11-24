///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCleanupTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDCleanupTest.java
 //
 // Classes:      JDCleanupTest
 //

package test;


/**
Cleans up after all of the JDBC tests.
**/
public class JDCleanupTest
extends JDTestDriver
{

  /**
   * 
   */
  public static String COLLECTION = null; 


/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
    public static void main (String args[])
        throws Exception
    {
        runApplication (new JDCleanupTest (args));
    }



/**
Constructs an object for applets.
@exception  Exception  If an exception occurs.
**/
    public JDCleanupTest ()
        throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
    public JDCleanupTest (String[] args)
        throws Exception
    {
        super (args);
    }

/**
Performs setup needed before running testcases.

@exception Exception If an exception occurs.
**/
   public void setup() throws Exception {
    super.setup(); 
    if (testLib_ != null) { 
      COLLECTION = testLib_;
    }
   }


/**
Creates the testcases.
**/
    public void createTestcases2 ()
    {
        addTestcase (new JDCleanup (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));
    }

}


