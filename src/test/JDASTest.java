///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//
//  File Name: AS400JDBCAlternateServerTest.java
//  Classes:   AS400JDBCAlternateServerTest
//
////////////////////////////////////////////////////////////////////////////
//
////////////////////////////////////////////////////////////////////////////
package test;

import java.sql.Connection;

import test.JD.*
import test.JD.AS*




/**
  Test driver for the JDBC AlternateServer support. 
  The testcases will be attempted in unattended mode.
  No input parms are required when calling this test driver; they will be ignored if specified.
  See TestDriver for remaining calling syntax.
  @see TestDriver
 **/
public class JDASTest extends JDTestDriver
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
         TestDriver.runApplication(new JDASTest(args));
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
   public JDASTest() throws Exception
   {
      super();
   }

   /**
     This ctor used for applications.
     @param  args  the array of command line arguments
     @exception  Exception  Incorrect arguments will cause an exception
    **/
   public JDASTest(String[] args) throws Exception
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
                                 c, COLLECTION);

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

		addTestcase (new JDASEnableCALTestcase (systemObject_,
				namesAndVars_, runMode_, fileOutputStream_, 
				password_, pwrSysUserID_, pwrSysPassword_));

		addTestcase (new JDASClientReroute1 (systemObject_,
				namesAndVars_, runMode_, fileOutputStream_, 
				password_, pwrSysUserID_, pwrSysPassword_));
    addTestcase (new JDASClientReroute2 (systemObject_,
        namesAndVars_, runMode_, fileOutputStream_, 
        password_, pwrSysUserID_, pwrSysPassword_));
    addTestcase (new JDASClientReroute3 (systemObject_,
        namesAndVars_, runMode_, fileOutputStream_, 
        password_, pwrSysUserID_, pwrSysPassword_));
    addTestcase (new JDASClientReroute4 (systemObject_,
        namesAndVars_, runMode_, fileOutputStream_, 
        password_, pwrSysUserID_, pwrSysPassword_));
    addTestcase (new JDASClientReroute5 (systemObject_,
        namesAndVars_, runMode_, fileOutputStream_, 
        password_, pwrSysUserID_, pwrSysPassword_));
    addTestcase (new JDASClientReroute6 (systemObject_,
        namesAndVars_, runMode_, fileOutputStream_, 
        password_, pwrSysUserID_, pwrSysPassword_));
    addTestcase (new JDASClientReroute7 (systemObject_,
        namesAndVars_, runMode_, fileOutputStream_, 
        password_, pwrSysUserID_, pwrSysPassword_));


		addTestcase (new JDASDefaultRetry (systemObject_,
						    namesAndVars_, runMode_, fileOutputStream_, 
						    password_, pwrSysUserID_, pwrSysPassword_));

		addTestcase (new JDASRetry (systemObject_,
						    namesAndVars_, runMode_, fileOutputStream_, 
						    password_, pwrSysUserID_, pwrSysPassword_));
	  addTestcase (new JDASClientRegisters (systemObject_,
        namesAndVars_, runMode_, fileOutputStream_, 
        password_, pwrSysUserID_, pwrSysPassword_));




    addTestcase (new JDASSeamlessFailover1 (systemObject_,
        namesAndVars_, runMode_, fileOutputStream_, 
        password_, pwrSysUserID_, pwrSysPassword_));
    
    addTestcase (new JDASSeamlessFailover2 (systemObject_,
        namesAndVars_, runMode_, fileOutputStream_, 
        password_, pwrSysUserID_, pwrSysPassword_));
    
    addTestcase (new JDASSeamlessFailover3 (systemObject_,
        namesAndVars_, runMode_, fileOutputStream_, 
        password_, pwrSysUserID_, pwrSysPassword_));
    
    addTestcase (new JDASSeamlessFailover4 (systemObject_,
        namesAndVars_, runMode_, fileOutputStream_, 
        password_, pwrSysUserID_, pwrSysPassword_));
    
    addTestcase (new JDASSeamlessFailover5 (systemObject_,
        namesAndVars_, runMode_, fileOutputStream_, 
        password_, pwrSysUserID_, pwrSysPassword_));

		
		
		
 addTestcase (new JDASSFStatement (systemObject_,
				namesAndVars_, runMode_, fileOutputStream_, 
				password_, pwrSysUserID_, pwrSysPassword_));

addTestcase (new JDASSFPreparedStatement (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));

addTestcase (new JDASSFCallableStatement (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));


addTestcase (new JDASSFConnection (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));


addTestcase (new JDASAffinityFailback (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));


addTestcase (new JDASPreparedStatement (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));


addTestcase (new JDASDSEnableCALTestcase (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));



addTestcase (new JDASDSClientReroute1 (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));
addTestcase (new JDASDSClientReroute2 (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));
addTestcase (new JDASDSClientReroute3 (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));
addTestcase (new JDASDSClientReroute4 (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));
addTestcase (new JDASDSClientReroute5 (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));
addTestcase (new JDASDSClientReroute6 (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));
addTestcase (new JDASDSClientReroute7 (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));

addTestcase (new JDASDSDefaultRetry (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));
addTestcase (new JDASDSRetry (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));



addTestcase (new JDASDSSeamlessFailover1 (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));

addTestcase (new JDASDSSeamlessFailover2 (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));

addTestcase (new JDASDSSeamlessFailover3 (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));

addTestcase (new JDASDSSeamlessFailover4 (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));

addTestcase (new JDASDSSeamlessFailover5 (systemObject_,
    namesAndVars_, runMode_, fileOutputStream_, 
    password_, pwrSysUserID_, pwrSysPassword_));

		
  }
}

