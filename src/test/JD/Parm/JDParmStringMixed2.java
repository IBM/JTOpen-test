///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringMixed2.java
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
// File Name:    JDParmStringMixed2.java
//
// Classes:      JDParmStringMixed2
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.Parm;

import com.ibm.as400.access.AS400;

import test.JDParmTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDParmStringMixed2.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringMixed2
extends JDTestcase {



    // Private data.
    public Connection connection = null;
    public PreparedStatement col1  = null;
    public PreparedStatement col2  = null;
    public PreparedStatement col3  = null;
    public String largeValue = "";
    boolean isNative = false; 


/**
Constructor.
**/
    public JDParmStringMixed2 (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringMixed2",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        try {
           // Register the JDBC driver.
           Class.forName("com.ibm.db2.jdbc.app.DB2Driver");

           // Get a global connection - choose how you which
           // to get the connection.
           connection = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_); 

           Statement s = connection.createStatement();

           try {
              s.executeUpdate("drop table "+JDParmTest.COLLECTION+".strings");
           } catch (SQLException e) {
	       JDParmHelper.handleDropException(e);
           }

           // Create a table that uses the largest row size the database will
           // allow me to use.
           s.executeUpdate("create table "+JDParmTest.COLLECTION+".strings " + 
                           "(col1 char(4) ccsid 937,   " +
                           " col2 char(20) ccsid 937,  " + 
                           " col3 char (32741) ccsid 937)");


           s.close();

           col1 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col2 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col3 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");


           String shortValue = "abcdefghijklmnopqrstuvwxy";

           for (int i = 0; i < 1309; i++) {
              largeValue = largeValue + shortValue;
           }
           for (int i = 0; i < 16; i++) {
              largeValue = largeValue + "A";
           }

	     if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		 isNative = true; 
	     }

        } catch (Exception e) {
           System.out.println("Caught exception: " + e.getMessage());
           
        }
    }



/**
This is the place to put all cleanup work for the testcase.
**/   
   public void cleanup() {
      try {

         // Close the global connection opened in setup().
         connection.close();

      } catch (Exception e) {
         System.out.println("Caught exception: ");
         
      }
   }



/**
Test:  char(4) - value is just right
**/
   public void Var001() {
      char[] chars = new char[1];
      chars[0] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col1.setString(1, inValue);
         int count = col1.executeUpdate();

	 //if( getDriver () == JDTestDriver.DRIVER_NATIVE &&		// @A1
	 //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @A1
	 //    for(int i=0; i<3; i++)					// @A1
	 //	 inValue += " ";					// @A1

	 if (count == 1) {
	     String expected = inValue;
	     /* 
	     if (isNative) {
		 expected = "\uFF21"; 
	     }
             */
	    boolean passed = JDParmHelper.verifyString("col1", expected, connection);
            assertCondition(passed ,"col1 = " +col1+ ", inValue = " +inValue+ ", connection = " +connection);
	 } else { 
            failed ("invalid update count");
	 }

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  char(4) - value too big
**/
   public void Var002() {
      char[] chars = new char[2];
      chars[0] = '\uFF21';  // This is wide A.
      chars[1] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col1.setString(1, inValue);
         int count = col1.executeUpdate();
         if (count == 1)
            failed ("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
	  //if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				//@A1
	  //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )				//@A1
	  //    failed("Not expecting a Data Truncation exception but a SQLException "+	//@A1
	  //	     "with state = 22001 and SQLCode = -404");				//@A1
	  // else							 	        //@A1
              int dataSize = 5;  /* really is 6 */ 
              if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
                dataSize = 8; 
              }
	      assertCondition ((dt.getIndex() == 1)
	  		       && (dt.getParameter() == true)
	  		       && (dt.getRead() == false)
	  		       && (dt.getDataSize() == dataSize)
			       && (dt.getTransferSize() == 4),
			       " dt.getIndex()="+ dt.getIndex()+"!=1" +
			       " dt.getParameter()="+dt.getParameter()+"!=true"+
			       " dt.getRead()="+dt.getRead()+"!=false"+
			       " dt.getDataSize()="+dt.getDataSize()+"!="+dataSize+
			       " dt.getTransferSize()="+dt.getTransferSize()+"!=4"	);
      } catch (SQLException e) {
	  // Since we now bind as CHAR, we get again get the 404 exception since we
          // do not detect the error execute Time
	  if( getDriver() == JDTestDriver.DRIVER_NATIVE) {
	      assertCondition( e.getSQLState().equals("22001") &&	
	  		       e.getErrorCode() == -404 );		
	  } else { 
	      failed (e, "Unexpected Exception");
	  }
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  char(4) - value too big - doesn't fit - doesn't start with <shift-out>
**/
   public void Var003() {
      char[] chars = new char[2];
      chars[0] = 'A'; 
      chars[1] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col1.setString(1, inValue);
         int count = col1.executeUpdate();
         if (count == 1)
            failed ("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
	  //if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				//@A1
	  //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )				//@A1
	  //    failed("Not expecting a Data Truncation exception but a SQLException "+	//@A1
	  //	     "with state = 22001 and SQLCode = -404");				//@A1
	  //else
              int dataSize = 5;  /* really is 6 */ 
									//@A1
              if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
                dataSize = 7; 
              }
	      assertCondition ((dt.getIndex() == 1)
			       && (dt.getParameter() == true)
			       && (dt.getRead() == false)
			       && (dt.getDataSize() == dataSize)
			       && (dt.getTransferSize() == 4),
			       " dt.getIndex()="+ dt.getIndex()+"!=1" +
			       " dt.getParameter()="+dt.getParameter()+"!=true"+
			       " dt.getRead()="+dt.getRead()+"!=false"+
			       " dt.getDataSize()="+dt.getDataSize()+"!="+dataSize+
			       " dt.getTransferSize()="+dt.getTransferSize()+"!=4"	);
      } catch (SQLException e) {
	  if( getDriver() == JDTestDriver.DRIVER_NATIVE  ) {				//@A1
	      assertCondition( e.getSQLState().equals("22001") &&			//@A1
	  		       e.getErrorCode() == -404 );				//@A1
	  }  else	{									//@A1 
	      failed (e, "Unexpected Exception");
	  }
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  char(4) - value too big - doesn't fit - starts with <shift-out>
**/
   public void Var004() {
      char[] chars = new char[2];
      chars[0] = '\uFF21';  // This is wide A.
      chars[1] = 'A'; 
      String inValue = new String(chars);

      try {
         col1.setString(1, inValue);
         int count = col1.executeUpdate();
         if (count == 1)
            failed ("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
	  //if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				//@A1
	  //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )				//@A1
	  //    failed("Not expecting a Data Truncation exception but a SQLException "+	//@A1
	  //	     "with state = 22001 and SQLCode = -404");				//@A1
	  // else
              int dataSize = 5;  /* really is 6 */ 
              if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
                dataSize = 7; 
              }
              
									//@A1
	      assertCondition ((dt.getIndex() == 1)
			       && (dt.getParameter() == true)
			       && (dt.getRead() == false)
			       && (dt.getDataSize() == dataSize)
			       && (dt.getTransferSize() == 4),
			       " dt.getIndex()="+ dt.getIndex()+"!=1" +
			       " dt.getParameter()="+dt.getParameter()+"!=true"+
			       " dt.getRead()="+dt.getRead()+"!=false"+
			       " dt.getDataSize()="+dt.getDataSize()+"!="+dataSize+
			       " dt.getTransferSize()="+dt.getTransferSize()+"!=4"	);
      } catch (SQLException e) {
	   if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				//@A1
	      getRelease() >= JDTestDriver.RELEASE_V5R3M0 )				//@A1
	      assertCondition( e.getSQLState().equals("22001") &&			//@A1
	  		       e.getErrorCode() == -404 );				//@A1
	   else										//@A1
	      failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  char(20) - value is just right
**/
   public void Var005() {
      char[] chars = new char[10];
      chars[0] = 'A'; 
      chars[1] = '\uFF21';  // This is wide A.
      chars[2] = '\uFF21';  // This is wide A.
      chars[3] = '\uFF21';  // This is wide A.
      chars[4] = '\uFF21';  // This is wide A.
      chars[5] = '\uFF21';  // This is wide A.
      chars[6] = '\uFF21';  // This is wide A.
      chars[7] = '\uFF21';  // This is wide A.
      chars[8] = '\uFF21';  // This is wide A.
      chars[9] = 'A'; 
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();

	 // if( getDriver () == JDTestDriver.DRIVER_NATIVE &&		// @A1
	 //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @A1
	 //    for(int i=0; i<10; i++)					// @A1
	 //	 inValue += " ";					// @A1

	 if (count == 1) {
	     String expected = inValue;
	    
	     if (isNative && false ) {
		 /* result is CHAR(20) which is translated as GRAPHIC(20) */ 
		 expected = "A\uff21\uff21\uff21\uff21\uff21\uff21\uff21\uff21A          ";  
	     } 
             
            assertCondition(JDParmHelper.verifyString("col2", expected, connection), "col2 = " +col2+ ", inValue = " +inValue+ ", connection = " +connection);
	 } else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  char(20) - value is just right
**/
   public void Var006() {
      char[] chars = new char[14];
      chars[0] = '\uFF21';  // This is wide A.
      chars[1] = 'A'; 
      chars[2] = 'A'; 
      chars[3] = 'A'; 
      chars[4] = 'A'; 
      chars[5] = 'A'; 
      chars[6] = 'A'; 
      chars[7] = 'A'; 
      chars[8] = 'A'; 
      chars[9] = 'A'; 
      chars[10] = 'A'; 
      chars[11] = 'A'; 
      chars[12] = 'A'; 
      chars[13] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();

	 // if( getDriver () == JDTestDriver.DRIVER_NATIVE &&		// @A1
	 //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @A1
	 //    for(int i=0; i<6; i++)					// @A1
	 //	 inValue += " ";					// @A1

	 if (count == 1) {
	     String expected = inValue;
	     if (isNative && false ) {
		 /* result is CHAR(20) which is translated as GRAPHIC(20) */ 
		 expected = "\uff21AAAAAAAAAAAA\uff21      "; 
	     } 

            assertCondition(JDParmHelper.verifyString("col2", expected, connection), "col2 = " +col2+ ", inValue = " +inValue+ ", connection = " +connection);
	 } else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  char(20) - value is just right
**/
   public void Var007() {
      char[] chars = new char[8];
      chars[0] = '\uFF21';  // This is wide A.
      chars[1] = 'A'; 
      chars[2] = '\uFF21';  // This is wide A.
      chars[3] = 'A'; 
      chars[4] = '\uFF21';  // This is wide A.
      chars[5] = 'A'; 
      chars[6] = '\uFF21';  // This is wide A.
      chars[7] = 'A'; 
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();

	 // if( getDriver () == JDTestDriver.DRIVER_NATIVE &&		// @A1
	 //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @A1
	 //    for(int i=0; i<12; i++)					// @A1
	 //	 inValue += " ";					// @A1

	 if (count == 1) {
	     String expected = inValue;
	     if (isNative && false ) {
		 /* result is CHAR(20) which is translated as GRAPHIC(20) */ 
		 expected = "\uff21A\uff21A\uff21A\uff21A            "; 
	     } 

            assertCondition(JDParmHelper.verifyString("col2", expected, connection), "col2 = " +col2+ ", inValue = " +inValue+ ", connection = " +connection);
	 } else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  char(20) - value is just right
**/
   public void Var008() {
      char[] chars = new char[8];
      chars[0] = 'A'; 
      chars[1] = '\uFF21';  // This is wide A.
      chars[2] = 'A'; 
      chars[3] = '\uFF21';  // This is wide A.
      chars[4] = 'A'; 
      chars[5] = '\uFF21';  // This is wide A.
      chars[6] = 'A'; 
      chars[7] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();

	 // if( getDriver () == JDTestDriver.DRIVER_NATIVE &&		// @A1
	 //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @A1
	 //    for(int i=0; i<12; i++)					// @A1
         //	 inValue += " ";					// @A1

	 if (count == 1) {	String expected = inValue; 
	 if (isNative && false ) {
		 /* result is CHAR(20) which is translated as GRAPHIC(20) */ 
	     expected = "A\uff21A\uff21A\uff21A\uff21            "; 
	 } 

	 assertCondition(JDParmHelper.verifyString("col2", expected, connection), "col2 = " +col2+ ", inValue = " +inValue+ ", connection = " +connection);
	 } else
	     failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  char(20) - value is shorter then the column
**/
   public void Var009() {
      char[] chars = new char[3];
      chars[0] = '\uFF21';  // This is wide A.
      chars[1] = '\uFF21';  // This is wide A.
      chars[2] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();

	 // if( getDriver () == JDTestDriver.DRIVER_NATIVE &&		// @A1
	 //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @A1
	 //{								// @A1
	 //    for(int i=0; i<17; i++)					// @A1
	 //	 inValue += " ";					// @A1
	 //    if (count == 1)								  // @A1
	 //	 assertCondition(JDParmHelper.verifyString("col2", inValue, connection),  // @A1
	 //			 "col2 = " +col2+ ", inValue = " +inValue+		  // @A1
	 //			 ", connection = " +connection);			  // @A1
	 //    else									  // @A1
	 //	 failed ("invalid update count");					  // @A1
	 // }										  // @A1
	 // else{
	 if (count == 1)  {
	     String expected = inValue; 
	     if (isNative && false) {
		 /* result is CHAR(20) which is translated as GRAPHIC(20) */ 
		 expected = inValue + "                 "; 
	     }  else {
		 expected = inValue + "            "; 
	     } 

		 assertCondition(JDParmHelper.verifyString("col2", expected , connection), "col2 = " +col2+ ", inValue = " +inValue+ ", connection = " +connection);
	 } else
		 failed ("invalid update count");
	 // }
      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  char(20) - value is shorter then the column
**/
   public void Var010() {
      char[] chars = new char[6];
      chars[0] = '\uFF21';  // This is wide A.
      chars[1] = 'A';
      chars[2] = '\uFF21';  // This is wide A.
      chars[3] = 'A';
      chars[4] = '\uFF21';  // This is wide A.
      chars[5] = 'A';
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();

	 // if( getDriver () == JDTestDriver.DRIVER_NATIVE &&		// @A1
	 //      getRelease() >= JDTestDriver.RELEASE_V5R3M0 )		// @A1
	 // {								// @A1
	 //     for(int i=0; i<14; i++)					// @A1
	// 	 inValue += " ";					// @A1
	//      if (count == 1)								  // @A1
	// 	 assertCondition(JDParmHelper.verifyString("col2", inValue, connection),  // @A1
	// 			 "col2 = " +col2+ ", inValue = " +inValue+		  // @A1
	// 			 ", connection = " +connection);			  // @A1
	//      else									  // @A1
	// 	 failed ("invalid update count");					  // @A1
	//  }										  // @A1
	//  else{

	 if (count == 1) {
	     String expected = inValue;
	     if (isNative && false) {
		 /* result is CHAR(20) which is translated as GRAPHIC(20) */ 
		 expected = "\uff21A\uff21A\uff21A              "; 
	     } else {
		 expected = inValue + "     "; 
	     } 


		 assertCondition(JDParmHelper.verifyString("col2", expected, connection), "col2 = " +col2+ ", inValue = " +inValue+ ", connection = " +connection);
	 } else
		 failed ("invalid update count");
	 // }
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  char(20) - value too big - no room left for the shift in/out chars.
**/
   public void Var011() {
      char[] chars = new char[10];
      chars[0] = '\uFF21';  // This is wide A.
      chars[1] = '\uFF21';  // This is wide A.
      chars[2] = '\uFF21';  // This is wide A.
      chars[3] = '\uFF21';  // This is wide A.
      chars[4] = '\uFF21';  // This is wide A.
      chars[5] = '\uFF21';  // This is wide A.
      chars[6] = '\uFF21';  // This is wide A.
      chars[7] = '\uFF21';  // This is wide A.
      chars[8] = '\uFF21';  // This is wide A.
      chars[9] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();
         if (count == 1)
            failed ("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
	  //if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				//@A1
	  //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )				//@A1
	  //    failed("Not expecting a Data Truncation exception but a SQLException "+	//@A1
	  //	     "with state = 22001 and SQLCode = -404");				//@A1
	  // else
              int dataSize = 21;  /* really is 6 */ 
									//@A1
              if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
                dataSize = 32; 
              }

	      assertCondition ((dt.getIndex() == 1)
			       && (dt.getParameter() == true)
			       && (dt.getRead() == false)
			       && (dt.getDataSize() == dataSize)
			       && (dt.getTransferSize() == 20),
			       " dt.getIndex()="+ dt.getIndex()+"!=1" +
			       " dt.getParameter()="+dt.getParameter()+"!=true"+
			       " dt.getRead()="+dt.getRead()+"!=false"+
			       " dt.getDataSize()="+dt.getDataSize()+"!="+dataSize+
			       " dt.getTransferSize()="+dt.getTransferSize()+"!=20"	);
      } catch (SQLException e) {
	   if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				//@A1
	      getRelease() >= JDTestDriver.RELEASE_V5R3M0 )				//@A1
	      assertCondition( e.getSQLState().equals("22001") &&			//@A1
	  		       e.getErrorCode() == -404 );				//@A1
	   else										//@A1
	      failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  char(20) - value too big - too big by 1 - doesn't start with shift out.
**/
   public void Var012() {
      char[] chars = new char[18];
      chars[0] = 'A';
      chars[1] = 'A';
      chars[2] = 'A';
      chars[3] = 'A';
      chars[4] = 'A';
      chars[5] = 'A';
      chars[6] = 'A';
      chars[7] = 'A';
      chars[8] = 'A';
      chars[9] = 'A';
      chars[10] = 'A';
      chars[11] = 'A';
      chars[12] = 'A';
      chars[13] = 'A';
      chars[14] = 'A';
      chars[15] = 'A';
      chars[16] = 'A';
      chars[17] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();
         if (count == 1)
            failed ("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
	   //if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				//@A1
	   //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )				//@A1
	   //   failed("Not expecting a Data Truncation exception but a SQLException "+	//@A1
	  //	     "with state = 22001 and SQLCode = -404");				//@A1
	  // else										//@A1
	      int dataSize = 21; 
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          dataSize = 23; 
        }

	      assertCondition ((dt.getIndex() == 1)
			       && (dt.getParameter() == true)
			       && (dt.getRead() == false)
			       && (dt.getDataSize() == dataSize)
			       && (dt.getTransferSize() == 20),
			       " dt.getIndex()="+ dt.getIndex()+"!=1" +
			       " dt.getParameter()="+dt.getParameter()+"!=true"+
			       " dt.getRead()="+dt.getRead()+"!=false"+
			       " dt.getDataSize()="+dt.getDataSize()+"!="+dataSize+
			       " dt.getTransferSize()="+dt.getTransferSize()+"!=20"	);
      } catch (SQLException e) {
	  if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				//@A1
	      getRelease() >= JDTestDriver.RELEASE_V5R3M0 )				//@A1
	      assertCondition( e.getSQLState().equals("22001") &&			//@A1
	  		       e.getErrorCode() == -404 );				//@A1
	   else										//@A1
	      failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  char(20) - value too big - too big by 1 - starts with shift out.
**/
   public void Var013() {
      char[] chars = new char[18];
      chars[0] = '\uFF21';  // This is wide A.
      chars[1] = 'A';
      chars[2] = 'A';
      chars[3] = 'A';
      chars[4] = 'A';
      chars[5] = 'A';
      chars[6] = 'A';
      chars[7] = 'A';
      chars[8] = 'A';
      chars[9] = 'A';
      chars[10] = 'A';
      chars[11] = 'A';
      chars[12] = 'A';
      chars[13] = 'A';
      chars[14] = 'A';
      chars[15] = 'A';
      chars[16] = 'A';
      chars[17] = 'A';
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();
         if (count == 1)
            failed ("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
	  //if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				//@A1
	  //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )				//@A1
	  //    failed("Not expecting a Data Truncation exception but a SQLException "+	//@A1
	  //	     "with state = 22001 and SQLCode = -404");				//@A1
	  //else
	      int dataSize = 21;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          dataSize = 23; 
        }

	      assertCondition ((dt.getIndex() == 1)
			       && (dt.getParameter() == true)
			       && (dt.getRead() == false)
			       && (dt.getDataSize() == dataSize)
			       && (dt.getTransferSize() == 20),
			       
			       " dt.getIndex()="+ dt.getIndex()+"!=1" +
			       " dt.getParameter()="+dt.getParameter()+"!=true"+
			       " dt.getRead()="+dt.getRead()+"!=false"+
			       " dt.getDataSize()="+dt.getDataSize()+"!="+dataSize+
			       " dt.getTransferSize()="+dt.getTransferSize()+"!=20"	);
      } catch (SQLException e) {
	  if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				//@A1
	      getRelease() >= JDTestDriver.RELEASE_V5R3M0 )				//@A1
	      assertCondition( e.getSQLState().equals("22001") &&			//@A1
	  		       e.getErrorCode() == -404 );				//@A1
	   else										//@A1
	      failed (e, "Unexpecte	d Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }

}



