///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSDSTest.java
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
// File Name:    JDSDSTest.java
//
// Classes:      JDSDSTest
//
////////////////////////////////////////////////////////////////////////

package test;



/**
Test driver for the JDBC Driver class.
**/
public class JDSDSTest
      extends JDTestDriver {



/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
   public static void main (String args[])
   throws Exception
   {
      runApplication (new JDSDSTest (args));
   }



/**
Constructs an object for applets.
@exception  Exception  If an exception occurs.
**/
   public JDSDSTest ()
   throws Exception
   {
      super();
   }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
   public JDSDSTest (String[] args)
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
      super.setup();
   }


/**
Creates the testcases.
**/
   public void createTestcases2 ()
   {
      addTestcase (new JDSDSGetConnection (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_));

      addTestcase (new JDSDSGetReference (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_, 
                                         password_));

      addTestcase (new JDSDSProperties (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));

      addTestcase (new JDSDSMisc (systemObject_,
                                 namesAndVars_, runMode_, fileOutputStream_, 
                                 password_));


      addTestcase (new JDSDSWrapper (systemObject_,
                                 namesAndVars_, runMode_, fileOutputStream_, 
                                 password_));

   }

}


