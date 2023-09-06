///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASPTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDASPTest.java
//
// Classes:      JDASPTest
//
////////////////////////////////////////////////////////////////////////

package test;

import java.sql.Connection;



/**
Test driver for the JDBC ResultSet class.
**/
public class JDASPTest
      extends JDTestDriver 
{


/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.

@exception Exception If an exception occurs.
**/
   public static void main (String args[])
   throws Exception
   {
      runApplication (new JDASPTest (args));
   }



/**
Constructs an object for applets.

@exception Exception If an exception occurs.
**/
   public JDASPTest ()
   throws Exception
   {
      super();
   }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.

@exception Exception If an exception occurs.
**/
   public JDASPTest (String[] args)
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
      // going to do setup in first testcase
      super.setup(); // @D2A
   }



/**
Cleanup - - this does not run automatically - - it is called by JDCleanup.
**/
   public static void dropCollections(Connection c)
   {
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
Creates the testcases.
**/
   protected void createTestcases2 ()
   {                                                
      String iasp = null;
      String powerUID = null;
      String powerPwd = null;

      if (asp_ != null)
      {    
            iasp = asp_; 
      }          

      
      if ((asp_ == null) || (iasp == null))
         usage();

      powerUID = pwrSysUserID_; 
      powerPwd = pwrSysPassword_; 

      
      addTestcase (new JDASPTestcase(systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_, iasp, powerUID, powerPwd));
   }                                      
   
   public void usage()
   {                 
       System.out.println();       
       System.out.println("ASPName must be specified via -asp  ...");
       System.out.println("java test.JDASPTest -system systemName -uid xx -pwd xx -misc asp");
       System.exit(0);
   }
}




