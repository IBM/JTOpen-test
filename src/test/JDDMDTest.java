///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDTest.java
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
// File Name:    JDDMDTest.java
//
// Classes:      JDDMDTest
//
////////////////////////////////////////////////////////////////////////
//
//
////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.JD.JDSetupCollection;
import test.JD.DMD.JDDMDAxx;
import test.JD.DMD.JDDMDCCSID65535;
import test.JD.DMD.JDDMDDxx;
import test.JD.DMD.JDDMDGetBestRowIdentifier;
import test.JD.DMD.JDDMDGetCatalogs;
import test.JD.DMD.JDDMDGetColumnPrivileges;
import test.JD.DMD.JDDMDGetColumns;
import test.JD.DMD.JDDMDGetCrossReference;
import test.JD.DMD.JDDMDGetExportedKeys;
import test.JD.DMD.JDDMDGetFunctionColumns;
import test.JD.DMD.JDDMDGetFunctions;
import test.JD.DMD.JDDMDGetImportedKeys;
import test.JD.DMD.JDDMDGetIndexInfo;
import test.JD.DMD.JDDMDGetPrimaryKeys;
import test.JD.DMD.JDDMDGetProcedureColumns;
import test.JD.DMD.JDDMDGetProcedures;
import test.JD.DMD.JDDMDGetPseudoColumns;
import test.JD.DMD.JDDMDGetSchemas;
import test.JD.DMD.JDDMDGetTablePrivileges;
import test.JD.DMD.JDDMDGetTablePrivileges3;
import test.JD.DMD.JDDMDGetTableTypes;
import test.JD.DMD.JDDMDGetTables;
import test.JD.DMD.JDDMDGetTypeInfo;
import test.JD.DMD.JDDMDGetUDTs;
import test.JD.DMD.JDDMDGetVersionColumns;
import test.JD.DMD.JDDMDGetXxx;
import test.JD.DMD.JDDMDIsXxx;
import test.JD.DMD.JDDMDMisc;
import test.JD.DMD.JDDMDNullXxx;
import test.JD.DMD.JDDMDPerformance;
import test.JD.DMD.JDDMDStoresXxx;
import test.JD.DMD.JDDMDSupportsXxx;
import test.JD.DMD.JDDMDUsesXxx;
import test.JD.DMD.JDDMDWrapper;
import test.JD.DMD.JDDMDXxxAreXxx;



/**
Test driver for the JDBC DatabaseMetaData class.
**/
public class JDDMDTest
      extends JDTestDriver {



   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
// Constants.
   public static  String COLLECTION     = "JDTSTDMD";
   public static  String COLLECTION2    = "JDTSTDMD2";
   public static  String COLLECTIONXX   = "JDTSTDMDXX";
   public static  String COLLECTIONGETTAB   = "JDDMDGTABA";  //@PDA add for cleanup.  

   public static  String SCHEMAS_PERCENT      = "JDTSTDMD%";
   public static  String SCHEMAS_UNDERSCORE   = "JDTSTDMD_";
   public static  String SCHEMAS_LEN128   = "JDTESTDMDB01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789ABCDEFGH"; //@128sch



   // Private data.
   private Connection connection_;



/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.

@exception Exception If an exception occurs.
**/
   public static void main (String args[])
   throws Exception
   {
      runApplication (new JDDMDTest (args));
   }



/**
Constructs an object for applets.

@exception Exception If an exception occurs.
**/
   public JDDMDTest ()
   throws Exception
   {
      super();
   }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.

@exception Exception If an exception occurs.
**/
   public JDDMDTest (String[] args)
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

      connection_ = getConnection (getBaseURL (),
                                   systemObject_.getUserId (), encryptedPassword_);

      if (testLib_ != null) { // @E1A
         COLLECTION = testLib_;
         // Need to set the other two collection names also
         int endIndex = 8;
         if (COLLECTION.length() < 8)
            endIndex = COLLECTION.length() ;
         COLLECTION2  = COLLECTION.substring(0, endIndex) + "2";
         COLLECTIONXX = COLLECTION.substring(0, endIndex) + "XX";
         SCHEMAS_PERCENT      = COLLECTION.substring(0, endIndex) + "%";
         SCHEMAS_UNDERSCORE   = COLLECTION.substring(0, endIndex) + "_";
         SCHEMAS_LEN128 = COLLECTION.substring(0, endIndex) + SCHEMAS_LEN128.substring(endIndex); 
      }

      JDSetupCollection.create (systemObject_, 
                                connection_, COLLECTION);
      JDSetupCollection.create (systemObject_, 
                                connection_, COLLECTION2);
      JDSetupCollection.create (systemObject_, 
                                connection_, COLLECTIONXX);
      if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)  //@128sch 
      {
          JDSetupCollection.create (systemObject_,
                  connection_, SCHEMAS_LEN128);
      }
      
      try { 
        // Ignore any exceptions from commit.  JCC for DB2 V9 now throws exception
        // if commit called on autocommit connection 
        connection_.commit(); // for xa
      } catch (SQLException sqlex) { 
      
      }
   }



/**
Performs setup needed after running testcases.

@exception Exception If an exception occurs.
**/
   public void cleanup ()
   throws Exception
   {
      connection_.close ();
   }



/**
Cleanup - - this does not run automatically - - it is called by JDCleanup.
**/
   public static void dropCollections(Connection c)
   {
      dropCollection(c, COLLECTION);
      dropCollection(c, COLLECTION2);
      dropCollection(c, COLLECTIONXX);
      dropCollection(c, COLLECTIONGETTAB);
      dropCollection(c, SCHEMAS_LEN128); //@128sch

   }



/**
Checks the column names for a result set.

@exception SQLException If an exception occurs.
**/
   public static boolean checkColumnNames (ResultSetMetaData rsmd,
                                           String[] expectedNames,
                                           StringBuffer message)
   throws SQLException
   {
      boolean rc = true; 
      int count = rsmd.getColumnCount ();
      if(count != expectedNames.length)	{		// @F1
          message.append("columdCount="+count+" sb "+expectedNames.length); 
	  rc = false; 
      }
      
      for (int i = 0; i < count; ++i) {
	  if (i >= expectedNames.length) {
		  message.append("column["+(i+1)+"] = "+rsmd.getColumnName(i+1)+" UNEXPECTED\n"); 
	  } else { 
	  //System.out.println ("Column name " + (i+1) + ":"
	    //  + rsmd.getColumnName (i+1) + "==" + expectedNames[i]
	      //+ ":");
	      if (! rsmd.getColumnName (i+1).equals (expectedNames[i])) {
		  rc = false;
		  message.append("column["+(i+1)+"] = "+rsmd.getColumnName(i+1)+" sb "+expectedNames[i]+"\n"); 
	      }
	  }
      }
      return rc;
   }



/**
Checks the column types for a result set.
@exception  SQLException  If an exception occurs.
**/
   public static boolean checkColumnTypes (ResultSetMetaData rsmd,
                                           int[] expectedTypes, 
                                           StringBuffer message)
   throws SQLException
   {
       boolean rc = true; 
      int count = rsmd.getColumnCount ();
      if(count != expectedTypes.length)	{		// @F1
           message.append("columnCount="+count+" sb "+expectedTypes.length); 
            rc = false; 
      }
      for (int i = 0; i < count; ++i) {

	  if (i >= expectedTypes.length) {
		  message.append("column["+(i+1)+"] = "+rsmd.getColumnType(i+1)+" UNEXPECTED\n"); 


	  } else { 

	 // System.out.println ("Column type " + (i+1) + ":"
	 //     + rsmd.getColumnType (i+1) + "==" + expectedTypes[i]
	 //     + ":");
	      if (rsmd.getColumnType (i+1) != expectedTypes[i]) {
		  message.append("column["+(i+1)+"] = "+rsmd.getColumnType(i+1)+" sb "+expectedTypes[i]+"\n"); 

		  rc=false; 
	      }
	  }
            
      }
      return rc;
   }


   
   /**
    *  Check the results 
    * @param result               Result returned 
    * @param expected             Expected result
    * @param messageColumnName    Column name being testing 
    * @param info                 More debugging information
    * @param message              The string buffer to append the message to 
    * @return
    */
   public static boolean check(String result, String expected, String messageColumnName, String info, StringBuffer message, boolean success) {
        boolean checkOk; 
        if (result == null) {
            checkOk = ( expected == null); 
        } else {
	    int underscoreIndex = expected.indexOf('_');
	    if (underscoreIndex > 0) {
		String checkString  = expected.substring(0,underscoreIndex);
		checkOk = result.indexOf(checkString) >= 0; 
	    } else { 
		checkOk = result.equals(expected);
	    }
        } 
        if (!checkOk) {
            message.append(info+"=\""+result+"\" sb \""+expected+"\" "+messageColumnName+"\n"); 
        } 
        return checkOk && success; 
 } 

   public static boolean check(int result, int expected, String messageColumnName, String info, StringBuffer message, boolean success) {
     boolean checkOk; 
     checkOk = result == expected; 
     if (!checkOk) {
         message.append(info+":"+messageColumnName+"=\""+result+"\" sb \""+expected+"\"\n"); 
     } 
     return checkOk && success; 
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
	   

      addTestcase (new JDDMDAxx (systemObject_,
                                 namesAndVars_, runMode_, fileOutputStream_, 
                                 password_));

      addTestcase (new JDDMDDxx (systemObject_,
                                 namesAndVars_, runMode_, fileOutputStream_, 
                                 password_));

      addTestcase (new JDDMDGetBestRowIdentifier (systemObject_,
                                                  namesAndVars_, runMode_, fileOutputStream_, 
                                                  password_));

      addTestcase (new JDDMDGetCatalogs (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_, 
                                         password_));

      addTestcase (new JDDMDGetColumnPrivileges (systemObject_,
                                                 namesAndVars_, runMode_, fileOutputStream_, 
                                                 password_));

      addTestcase (new JDDMDGetColumns (systemObject_,
                                        namesAndVars_, runMode_, fileOutputStream_, 
                                        password_));

      addTestcase (new JDDMDGetCrossReference (systemObject_,
                                               namesAndVars_, runMode_, fileOutputStream_, 
                                               password_));

      addTestcase (new JDDMDGetExportedKeys (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));
      
      addTestcase (new JDDMDGetFunctions (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));
      
      addTestcase (new JDDMDGetFunctionColumns (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));


      addTestcase (new JDDMDGetImportedKeys (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));

      addTestcase (new JDDMDGetIndexInfo (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_));

      addTestcase (new JDDMDGetPrimaryKeys (systemObject_,
                                            namesAndVars_, runMode_, fileOutputStream_, 
                                            password_));

      addTestcase (new JDDMDGetProcedureColumns (systemObject_,
                                                 namesAndVars_, runMode_, fileOutputStream_, 
                                                 password_));

      addTestcase (new JDDMDGetProcedures (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

      addTestcase (new JDDMDGetSchemas (systemObject_,
                                        namesAndVars_, runMode_, fileOutputStream_, 
                                        password_));

      addTestcase (new JDDMDGetTablePrivileges (systemObject_,
                                                namesAndVars_, runMode_, fileOutputStream_, 
                                                password_));

      addTestcase (new JDDMDGetTablePrivileges3 (systemObject_,
                                                namesAndVars_, runMode_, fileOutputStream_, 
                                                password_));

      addTestcase (new JDDMDGetTables (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));

      addTestcase (new JDDMDGetTableTypes (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

      addTestcase (new JDDMDGetTypeInfo (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_, 
                                         password_));

      addTestcase (new JDDMDGetUDTs (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

      addTestcase (new JDDMDGetVersionColumns (systemObject_,
                                               namesAndVars_, runMode_, fileOutputStream_, 
                                               password_));
      addTestcase (new JDDMDGetPseudoColumns (systemObject_,
                                               namesAndVars_, runMode_, fileOutputStream_, 
                                               password_));

      addTestcase (new JDDMDGetXxx (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDDMDIsXxx (systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));

      addTestcase (new JDDMDMisc (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_, 
                                  password_));

      addTestcase (new JDDMDNullXxx (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

      addTestcase (new JDDMDStoresXxx (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));

      addTestcase (new JDDMDSupportsXxx (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_, 
                                         password_));

      addTestcase (new JDDMDUsesXxx (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

      addTestcase (new JDDMDXxxAreXxx (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));
      addTestcase (new JDDMDWrapper(systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));
      addTestcase (new JDDMDPerformance(systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));

      addTestcase (new JDDMDCCSID65535(systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));

     

   }
}


