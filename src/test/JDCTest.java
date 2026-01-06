///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//
//  File Name: JDCTest.java
//  Classes:   JDCTest
//
////////////////////////////////////////////////////////////////////////////
//
////////////////////////////////////////////////////////////////////////////
package test;
import test.JD.JDSetupCollection;

import java.sql.Connection;



/**
  Test driver for code coverage tests
  The testcases will be attempted in unattended mode.
  See TestDriver for remaining calling syntax.
  @see TestDriver
 **/
public class JDCTest extends JDTestDriver
{

	public final static int AS_DATABASE = 8471; 
	
  public static  String COLLECTION     = "JDTESTAS";
  
   /**
     Main for running standalone application tests.
    **/
   public static void main(String args[])
   {
     try
      {
         TestDriver.runApplication(new JDCTest(args));
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
   public JDCTest() throws Exception
   {
      super();
   }

   /**
     This ctor used for applications.
     @param  args  the array of command line arguments
     @exception  Exception  Incorrect arguments will cause an exception
    **/
   public JDCTest(String[] args) throws Exception
   {
      super(args);
   }
   
   
   
   /**
   Performs setup needed before running testcases.
   
   @exception Exception If an exception occurs.
   **/
   public void setup ()
   throws Exception
   {
       super.setup();                                                          // @D1A

       Connection c = getConnection (getBaseURL ()
                                     + ";errors=full",
                                     systemObject_.getUserId (), encryptedPassword_);

       if (testLib_ != null) { // @E1A
           COLLECTION = testLib_;
       }
       JDSetupCollection.create (systemObject_, 
                                 c, COLLECTION, out_);

       c.close ();
   }



   /**
   Performs setup needed after running testcases.
   
   @exception Exception If an exception occurs.
   **/
   public void cleanup ()
   throws Exception
   {
	/* Call the garbage collector to free still open Java objects */ 
	System.gc(); 
   }



   /**
   Cleanup - - this does not run automatically - - it is called by JDCleanup.
   **/
   public static void dropCollections(Connection c)
   {
       dropCollection(c, COLLECTION);
   }


   
   

   /**
     Creates Testcase objects for all the testcases in this component.
    **/


  protected void createTestcases2() {

		addTestcase (new JDCCoverage (systemObject_,
				namesAndVars_, runMode_, fileOutputStream_, 
				password_));

		
  }
}

