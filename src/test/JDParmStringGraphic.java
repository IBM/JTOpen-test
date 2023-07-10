///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringGraphic.java
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
// File Name:    JDParmStringGraphic.java
//
// Classes:      JDParmStringGraphic
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test;

/*
Some notes to myself...
 
create table "+JDParmTest.COLLECTION+".unicode (col1 graphic(10) ccsid 13488)
create table "+JDParmTest.COLLECTION+".mixed (col1 char(10) ccsid 937)       
create table "+JDParmTest.COLLECTION+".char(col1 char(10) ccsid 37)          
create table "+JDParmTest.COLLECTION+".graphic (col1 graphic(10) ccsid 835)  

space char:  
    unicode  0020
    char     40
    mixed    40
    graphic  FEFE

*/

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDParmStringGraphic.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringGraphic
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
    public JDParmStringGraphic (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringGraphic",
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
              // Ignore it.
           }

           // Create a table that uses the largest row size the database will
           // allow me to use.
           s.executeUpdate("create table "+JDParmTest.COLLECTION+".strings " + 
                           "(col1 graphic(1) ccsid 835, " + 
                           " col2 graphic(20) ccsid 835, " +
                           " col3 graphic (16361) ccsid 835)");


           s.close();

           col1 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col2 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col3 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");

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
Test:  graphic(1) - value is just right.  The value in this case is blah...
**/
   public void Var001() {
      char[] chars = new char[1];
      chars[0] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col1.setString(1, inValue);
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyGraphic("col1", inValue, 1, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  graphic(1) - empty string
**/
   public void Var002() {
      try {
         col1.setString(1, "");
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyGraphic("col1", "", 1, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  graphic(1) - null value
**/
   public void Var003() {
      try {
         col1.setString(1, null);
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyGraphic("col1", null, 0, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  graphic(1) - value too big
**/
   public void Var004() {
      char[] chars = new char[2];
      chars[0] = '\uFF21';  // This is wide A.
      chars[1] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);
      
      int size=inValue.length()*2;
      int transferSize=2;
      if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
      {
          size = inValue.length();
          transferSize = 1;
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
Test:  graphic(20) - value is just right
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
            assertCondition(JDParmHelper.verifyGraphic("col2", inValue, 20, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  graphic(20) - value is smaller than column
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
            assertCondition(JDParmHelper.verifyGraphic("col2", inValue, 20, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  graphic(20) - value is 1 char long
**/
   public void Var007() {
      char[] chars = new char[1];
          chars[0] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col2.setString(1, inValue);
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyGraphic("col2", inValue, 20, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  graphic(20) - value is empty string
**/
   public void Var008() {
      try {
         col2.setString(1, "");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyGraphic("col2", "", 20, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  graphic(20) - value is null
**/
   public void Var009() {
      try {
         col2.setString(1, null);
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyGraphic("col2", null, 0, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  graphic(20) - value too big
**/
   public void Var010() {
      char[] chars = new char[21];
      for (int i = 0; i < 21; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);
      
      int size=inValue.length()*2;
      int transferSize=40;
      if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
      {
          size = inValue.length();
          transferSize = 20;
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
Test:  graphic(16361) - value is just right
**/
   public void Var011() {
      char[] chars = new char[16361];
      for (int i = 0; i < 16361; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);

      try {
         col3.setString(1, inValue);
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyGraphic("col3", inValue, 16361, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  graphic(16361) - value is smaller than column
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
             assertCondition(JDParmHelper.verifyGraphic("col3", inValue, 16361, connection));
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
Test:  graphic(16361) - value is 1 char long
**/
   public void Var013() {
      char[] chars = new char[1];
      chars[0] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);

      try {
         col3.setString(1, inValue);
         int count = col3.executeUpdate();
         if (count == 1) {
            assertCondition(JDParmHelper.verifyGraphic("col3", inValue, 16361, connection));
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
Test:  graphic(16361) - value is empty string
**/
   public void Var014() {
      try {
         col3.setString(1, "");
         int count = col3.executeUpdate();
         if (count == 1) {
            assertCondition(JDParmHelper.verifyGraphic("col3", "", 16361, connection));
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
Test:  graphic(16361) - value is null
**/
   public void Var015() {
      try {
         col3.setString(1, null);
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyGraphic("col3", null, 0, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  graphic(16361) - value too big
**/
   public void Var016() {
      char[] chars = new char[16362];
      for (int i = 0; i < 16362; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);

      int size=inValue.length()*2;
      int transferSize=32722;
      if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
      {
          size = inValue.length();
          transferSize = 16361;
      }
      
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
                 && (dt.getDataSize() == size)
                 && (dt.getTransferSize() == transferSize));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }

}



