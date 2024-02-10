///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringDbclob.java
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
// File Name:    JDParmStringDbclob.java
//
// Classes:      JDParmStringDbclob
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
Testcase JDParmStringDbclob.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringDbclob
extends JDTestcase {



    // Private data.
    public Connection connection = null;
    public PreparedStatement col1  = null;
    public PreparedStatement col2  = null;
    public PreparedStatement col3  = null;
    public PreparedStatement col4  = null;


/**
Constructor.
**/
    public JDParmStringDbclob (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringDbclob",
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
               // Ignore it.
           }

           // Create a table that uses the largest row size the database will
           // allow me to use.
           s.executeUpdate("create table "+JDParmTest.COLLECTION+".strings " + 
                           "(col1 dbclob(1), " +
                           "col2 dbclob(20), " + 
                           "col3 dbclob (32714), " +
                           "col4 dbclob (2000000) )");


           s.close();

           col1 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col2 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col3 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");
           col4 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col4) values(?)");

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
Test:  dbclob(1) - value is just right.  The value in this case is blah...
**/
   public void Var001() {
      char[] chars = new char[1];
      chars[0] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col1.setString(1, inValue);
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", inValue, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  dbclob(1) - empty string
**/
   public void Var002() {
      try {
         col1.setString(1, "");
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", "", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(1) - null value
**/
   public void Var003() {
      try {
         col1.setString(1, null);
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", null, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(1) - value too big
**/
   public void Var004() {
      char[] chars = new char[2];
      chars[0] = '\uFF21';  // This is wide A.
      chars[1] = '\uFF21';  // This is wide A.
    
      String inValue = new String(chars);
      int size = inValue.length()*2;
      int transferSize = 2;
      if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
      {
          size = inValue.length();  
          transferSize = 0; //not transfered yet
      }
      
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
                 && (dt.getDataSize() == size)
                 && (dt.getTransferSize() == transferSize));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(20) - value is just right
**/
   public void Var005() {
      char[] chars = new char[20];
      for (int i = 0; i < 20; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", inValue, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(20) - value is smaller than column
**/
   public void Var006() {
      char[] chars = new char[5];
      for (int i = 0; i < 5; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", inValue, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  dbclob(20) - value is 1 char long
**/
   public void Var007() {
      char[] chars = new char[1];
          chars[0] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", inValue, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(20) - value is empty string
**/
   public void Var008() {
      try {
         col2.setString(1, "");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(20) - value is null
**/
   public void Var009() {
      try {
         col2.setString(1, null);
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", null, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  dbclob(20) - value too big
**/
   public void Var010() {
      char[] chars = new char[21];
      for (int i = 0; i < 21; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);

      int size = inValue.length()*2;
      int transferSize = 40;
      if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
      {
          size = 2;  //trunc
          transferSize = 0; //not transfered yet
      }
      
      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();
         if (count == 1)
            failed ("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == size)
                 && (dt.getTransferSize() == transferSize));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(32714) - value is just right
**/
   public void Var011() {
      char[] chars = new char[32714];
      for (int i = 0; i < 32714; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);

      try {
         col3.setString(1, inValue);
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", inValue, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(32714) - value is smaller than column
**/
   public void Var012() {
      char[] chars = new char[15000];
      for (int i = 0; i < 15000; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);

      try {
         col3.setString(1, inValue);
         int count = col3.executeUpdate();
         if (count == 1) {
             assertCondition(JDParmHelper.verifyString("col3", inValue, connection));
         }
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  dbclob(32714) - value is 1 char long
**/
   public void Var013() {
      char[] chars = new char[1];
      chars[0] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col3.setString(1, inValue);
         int count = col3.executeUpdate();
         if (count == 1) {
            assertCondition(JDParmHelper.verifyString("col3", inValue, connection));
         }
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(32714) - value is empty string
**/
   public void Var014() {
      try {
         col3.setString(1, "");
         int count = col3.executeUpdate();
         if (count == 1) {
            assertCondition(JDParmHelper.verifyString("col3", "", connection));
         }
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(32714) - value is null
**/
   public void Var015() {
      try {
         col3.setString(1, null);
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", null, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  dbclob(32714) - value too big
**/
   public void Var016() {
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
       {
           notApplicable();
           return;
       }
      char[] chars = new char[32715];
      for (int i = 0; i < 32715; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);

      try {
         col3.setString(1, inValue);
         int count = col3.executeUpdate();
         if (count == 1)
            failed ("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == inValue.length()*2)
                 && (dt.getTransferSize() == 65428));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }




/**
Test:  dbclob(2000000) - value is just right
**/
   public void Var017() {
      char[] chars = new char[2000000];
      for (int i = 0; i < 2000000; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);

      try {
         col4.setString(1, inValue);
         int count = col4.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col4", inValue, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(2000000) - value is smaller than column
**/
   public void Var018() {
      char[] chars = new char[15000];
      for (int i = 0; i < 15000; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);

      try {
         col4.setString(1, inValue);
         int count = col4.executeUpdate();
         if (count == 1) {
             assertCondition(JDParmHelper.verifyString("col4", inValue, connection));
         }
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  dbclob(2000000) - value is 1 char long
**/
   public void Var019() {
      char[] chars = new char[1];
      chars[0] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col4.setString(1, inValue);
         int count = col4.executeUpdate();
         if (count == 1) {
            assertCondition(JDParmHelper.verifyString("col4", inValue, connection));
         }
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(2000000) - value is empty string
**/
   public void Var020() {
      try {
         col4.setString(1, "");
         int count = col4.executeUpdate();
         if (count == 1) {
            assertCondition(JDParmHelper.verifyString("col4", "", connection));
         }
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  dbclob(2000000) - value is null
**/
   public void Var021() {
      try {
         col4.setString(1, null);
         int count = col4.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col4", null, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  dbclob(2000000) - value too big
**/
   public void Var022() {
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
       {
           notApplicable();
           return;
       }
      char[] chars = new char[2000001];
      for (int i = 0; i < 2000001; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);

      try {
         col4.setString(1, inValue);
         int count = col4.executeUpdate();
         if (count == 1)
            failed ("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == inValue.length()*2)
                 && (dt.getTransferSize() == 4000000));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }

}



