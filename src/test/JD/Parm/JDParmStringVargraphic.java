///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringVargraphic.java
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
// File Name:    JDParmStringVargraphic.java
//
// Classes:      JDParmStringVargraphic
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.Parm;

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

import test.JDParmTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDParmStringVargraphic.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringVargraphic
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDParmStringVargraphic";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDParmTest.main(newArgs); 
   }



    // Private data.
    public Connection connection = null;
    public PreparedStatement col1  = null;
    public PreparedStatement col2  = null;
    public PreparedStatement col3  = null;
    public String largeValue = "";


/**
Constructor.
**/
    public JDParmStringVargraphic (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringVargraphic",
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
                           "(col1 vargraphic(1) ccsid 835, " + 
                           " col2 vargraphic(20) ccsid 835, " +
                           " col3 vargraphic (16346) ccsid 835)");


           s.close();

           col1 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col2 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col3 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");


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
Test:  vargraphic(1) - value is just right.  The value in this case is blah...
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
Test:  vargraphic(1) - empty string
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
Test:  vargraphic(1) - null value
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
Test:  vargraphic(1) - value too big
**/
   public void Var004() {
      char[] chars = new char[2];
      chars[0] = '\uFF21';  // This is wide A.
      chars[1] = '\uFF21';  // This is wide A.
      String inValue = new String(chars);
      int size = 4;
      int transferSize = 2;
      if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
      {
          size = 2;
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
                 && (dt.getTransferSize() ==transferSize));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  vargraphic(20) - value is just right
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
Test:  vargraphic(20) - value is smaller than column
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
Test:  vargraphic(20) - value is 1 char long
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
Test:  vargraphic(20) - value is empty string
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
Test:  vargraphic(20) - value is null
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
Test:  vargraphic(20) - value too big
**/
   public void Var010() {
      char[] chars = new char[21];
      for (int i = 0; i < 21; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);

      int size = 42;
      int transferSize = 40;
      if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
      {
          size = 21;
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
Test:  vargraphic(16346) - value is just right
**/
   public void Var011() {
      char[] chars = new char[16346];
      for (int i = 0; i < 16346; i++) {
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
Test:  vargraphic(16346) - value is smaller than column
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
Test:  vargraphic(16346) - value is 1 char long
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
Test:  vargraphic(16346) - value is empty string
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
Test:  vargraphic(16346) - value is null
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
Test:  vargraphic(16346) - value too big
**/
   public void Var016() {
      char[] chars = new char[16347];
      for (int i = 0; i < 16346; i++) {
          chars[i] = '\uFF21';  // This is wide A.
      }
      String inValue = new String(chars);
      int size = 32694;
      int transferSize = 32692;
      if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
      {
          size = 16347;
          transferSize = 16346;
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



