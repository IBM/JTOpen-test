///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringVarmixed2.java
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
// File Name:    JDParmStringVarmixed2.java
//
// Classes:      JDParmStringVarmixed2
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDParmStringVarmixed2.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringVarmixed2
extends JDTestcase {



    // Private data.
    public Connection connection = null;
    public PreparedStatement col1  = null;
    public PreparedStatement col2  = null;
    public PreparedStatement col3  = null;
    public String largeValue = "";


/**
Constructor.
**/
    public JDParmStringVarmixed2 (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringVarmixed2",
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
                           "(col1 varchar(4) ccsid 937,   " +
                           " col2 varchar(20) ccsid 937,  " + 
                           " col3 varchar (32711) ccsid 937)");


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

        } catch (Exception e) {
           System.out.println("Caught exception: " + e.getMessage());
          e.printStackTrace(); 
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
         e.printStackTrace();
      }
   }



/**
Test:  char(4) - value is just right
**/
   public void Var001() {
      char[] chars = new char[1];
      chars[0] = '\uFF21';  // This is wide A.
       System.out.println(chars[0]);
       String inValue = new String(chars);

      System.out.println(chars[0]);
      System.out.println(chars);
      System.out.println(inValue);


      try {
         col1.setString(1, inValue);
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", inValue, connection), "col1 = " +col1+ ", inValue = " +inValue+ ", connection = " +connection);
         else
            failed ("invalid update count");

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
	  int dataSize = 6; 
	  if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
	      // Native JDBC is estimate
	      dataSize = 5; 
	  }
	      
	      // Toolbox changed Oct 2012 to throw the truncation
	      // DataSize and Transfer size are different than native
	      assertCondition ((dt.getIndex() == 1)
			       && (dt.getParameter() == true)
			       && (dt.getRead() == false)
			       && (dt.getDataSize() == dataSize)
			       && (dt.getTransferSize() == 4),
			       "\ndt.getIndex()="+dt.getIndex()+" sb 1"+
			       "\ndt.getParameter()="+dt.getParameter()+" sb true"+
			       "\ndt.getRead()="+dt.getRead()+" sb false"+
			       "\ndt.getDataSize()="+dt.getDataSize()+" sb "+dataSize+
			       "\ndt.getTransferSize()="+dt.getTransferSize()+" sb 4");

      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("length")!= -1)
              succeeded();
	  else {
	      if( getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  assertCondition( e.getSQLState().equals("22001") &&	
				   e.getErrorCode() == -404 );		
	      } else { 

		  failed (e, "Unexpected Exception");
	      }
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
	      assertCondition ((dt.getIndex() == 1)
			       && (dt.getParameter() == true)
			       && (dt.getRead() == false)
			       // Get datasize is the number of bytes
                               // that should have been transferred 
			       && (dt.getDataSize() == 5)
			       && (dt.getTransferSize() == 4),
             "\ndt.getIndex()="+dt.getIndex()+" sb 1"+
             "\ndt.getParameter()="+dt.getParameter()+" sb true"+
             "\ndt.getRead()="+dt.getRead()+" sb false"+
             "\ndt.getDataSize()[Bytes that should have been transferred]="+dt.getDataSize()+" sb 5\n"+
             "\ndt.getTransferSize()="+dt.getTransferSize()+" sb 2" );
      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("length")!= -1)
              succeeded();
          else
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
	  int dataSize = 5;
	  if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
	      /* native provides an estimate */
	      dataSize = 5;
	  }
	  assertCondition ((dt.getIndex() == 1)
			       && (dt.getParameter() == true)
			       && (dt.getRead() == false)
			       && (dt.getDataSize() == dataSize)
			       && (dt.getTransferSize() == 4),
             "\ndt.getIndex()="+dt.getIndex()+" sb 1"+
             "\ndt.getParameter()="+dt.getParameter()+" sb true"+
             "\ndt.getRead()="+dt.getRead()+" sb false"+
             "\ndt.getDataSize()="+dt.getDataSize()+" sb "+dataSize+"\n"+
             "\ndt.getTransferSize()="+dt.getTransferSize()+" sb 4");
      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("length")!= -1)
              succeeded();
          else
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
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", inValue, connection), "col2 = " +col2+ ", inValue = " +inValue+ ", connection = " +connection);
         else
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
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", inValue, connection), "col2 = " +col2+ ", inValue = " +inValue+ ", connection = " +connection);
         else
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
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", inValue, connection), "col2 = " +col2+ ", inValue = " +inValue+ ", connection = " +connection);
         else
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
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", inValue, connection), "col2 = " +col2+ ", inValue = " +inValue+ ", connection = " +connection);
         else
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
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", inValue, connection), "col2 = " +col2+ ", inValue = " +inValue+ ", connection = " +connection);
         else
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
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", inValue, connection), "col2 = " +col2+ ", inValue = " +inValue+ ", connection = " +connection);
         else
            failed ("invalid update count");

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

	  int dataSize = 22;
	  if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
	      /* native provides an estimate */
	      dataSize = 21;
	  }

	    	      assertCondition ((dt.getIndex() == 1)
			       && (dt.getParameter() == true)
			       && (dt.getRead() == false)
			       && (dt.getDataSize() == dataSize)
			       && (dt.getTransferSize() == 20),
			       "\ndt.getIndex()="+dt.getIndex()+" sb 1"+
			       "\ndt.getParameter()="+dt.getParameter()+" sb true"+
			       "\ndt.getRead()="+dt.getRead()+" sb false"+
			       "\ndt.getDataSize()="+dt.getDataSize()+" sb "+ dataSize +
			       "\ndt.getTransferSize()="+dt.getTransferSize()+" sb 20"); 


      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("length")!= -1)
              succeeded();
          else
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
	  int dataSize = 21;

	      assertCondition ((dt.getIndex() == 1)
			       && (dt.getParameter() == true)
			       && (dt.getRead() == false)
			       && (dt.getDataSize() == dataSize)
			       && (dt.getTransferSize() == 20),
             "\ndt.getIndex()="+dt.getIndex()+" sb 1"+
             "\ndt.getParameter()="+dt.getParameter()+" sb true"+
             "\ndt.getRead()="+dt.getRead()+" sb false"+
             "\ndt.getDataSize()="+dt.getDataSize()+" sb "+dataSize+"\n"+
             "\ndt.getTransferSize()="+dt.getTransferSize()+" sb 20");
      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("length")!= -1)
              succeeded();
          else
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
	  int dataSize = 21; 
	  if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
	      /* native provides an estimate */
	      dataSize = 21;
	  }

	      assertCondition ((dt.getIndex() == 1)
			       && (dt.getParameter() == true)
			       && (dt.getRead() == false)
			       && (dt.getDataSize() == dataSize)
			       && (dt.getTransferSize() == 20),
             "\ndt.getIndex()="+dt.getIndex()+" sb 1"+
             "\ndt.getParameter()="+dt.getParameter()+" sb true"+
             "\ndt.getRead()="+dt.getRead()+" sb false"+
             "\ndt.getDataSize()="+dt.getDataSize()+" sb "+dataSize+" \n"+
             "\ndt.getTransferSize()="+dt.getTransferSize()+" sb 20");
      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("length")!= -1)
              succeeded();
          else
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

}



