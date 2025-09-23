///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringDecimal.java
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
// File Name:    JDParmStringDecimal.java
//
// Classes:      JDParmStringDecimal
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
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDParmStringDecimal.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringDecimal
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDParmStringDecimal";
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
    public PreparedStatement col4  = null;
    public PreparedStatement col5  = null;
    public PreparedStatement col6  = null;
    public String largeValue = "";


/**
Constructor.
**/
    public JDParmStringDecimal (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringDecimal",
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
              // Ignore it.
           }

           // Create a table that uses the largest row size the database will
           // allow me to use.
           s.executeUpdate("create table "+JDParmTest.COLLECTION+".strings " + 
                           "(col1 Decimal (1,  0),  " +
                           " col2 Decimal (5,  0),  " + 
                           " col3 Decimal (10, 0),  " +
                           " col4 Decimal (5,  5),  " +
                           " col5 Decimal (1,  1) )" );


           s.close();

           col1 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col2 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col3 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");
           col4 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col4) values(?)");
           col5 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col5) values(?)");


           String shortValue = "abcdefghijklmnopqrstuvwxy";

           for (int i = 0; i < 1309; i++) {
              largeValue = largeValue + shortValue;
           }
           for (int i = 0; i < 19; i++) {
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
Test:  Decimal(1, 0) - value is just right.
**/
   public void Var001() {
      try {
         col1.setString(1, "1");
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", "1", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  Decimal(1, 0) - empty string.  This is an error condition - an empty string is not 
    automatically treated as some default value.
**/
   public void Var002() {
      try {
         col1.setString(1, "");
         failed("value set was not valid.");
      } catch (SQLException e) {
          // SQL state should be 07006.  Vendor code should be -999999 and message text should
          // have the words incorrect conversion in it.
          // TODO:  Look this stuff up in the real SQL reference....
          assertCondition(true);
          //System.out.println(e.getMessage());
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(1, 0) - empty string.  This is an error condition - an empty string is not 
    automatically treated as some default value.
**/
   public void Var003() {
      try {
         col1.setString(1, "A");
         failed("value set was not valid.");
      } catch (SQLException e) {
          // SQL state should be 07006.  Vendor code should be -999999 and message text should
          // have the words incorrect conversion in it.
          // TODO:  Look this stuff up in the real SQL reference....
          assertCondition(true);
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(1, 0) - null value.  This is legal.
**/
   public void Var004() {
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
Test:  Decimal(1, 0) - value too big.  This is expected to throw a data truncation exception
    and doesn't today.
**/
   public void Var005() {
       String value = "12";
      try {
         col1.setString(1, value);
         int count = col1.executeUpdate();
         failed("inserted a value without truncation count="+count);
      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 1));
      } catch (SQLException e) {
	  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox ");


      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(1, 0) - value with a decimal point - this is expected to work with the decimal
    point and trailing data being removed automatically.
**/
   public void Var006() {
       try {
          col1.setString(1, "1.2");
          int count = col1.executeUpdate();
          if (count == 1)
             assertCondition(JDParmHelper.verifyString("col1", "1", connection));
          else
             failed ("invalid update count");

       } catch (SQLException e) {
          failed (e, "Unexpected Exception");
          
       } finally {
          JDParmHelper.purgeStringsTable(connection);
       }
   }



/**
Test:  Decimal(5, 0) - value is just right.
**/
   public void Var007() {
      try {
         col2.setString(1, "12345");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "12345", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(5, 0) - value is a single digit.
**/
   public void Var008() {
      try {
         col2.setString(1, "1");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "1", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  Decimal(5, 0) - empty string.  This is an error condition - an empty string is not 
    automatically treated as some default value.
**/
   public void Var009() {
      try {
         col2.setString(1, "");
         failed("insert into a Decimal field without a Decimal value.");
      } catch (SQLException e) {
          // SQL state should be 07006.  Vendor code should be -999999 and message text should
          // have the words incorrect conversion in it.
          // TODO:  Look this stuff up in the real SQL reference....
          assertCondition(true);
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(5, 0) - null value.  This is legal.
**/
   public void Var010() {
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
Test:  Decimal(5, 0) - value too big.  This is expected to throw a data truncation exception
    and doesn't today.
**/
   public void Var011() {
       String value = "123456";
      try {
         col2.setString(1, value);
         int count = col2.executeUpdate();
         failed("inserted a value without truncation "+count);
      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 5));
      } catch (SQLException e) {
	  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox ");

      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(5, 0) - value with a decimal point - this is expected to work with the decimal
    point and trailing data being removed automatically.
    
    TODO:  Change this to work the way that UDB does and then test the performance of the 
           old and new versions.
**/
   public void Var012() {
       try {
          col2.setString(1, "12345.6");
          int count = col2.executeUpdate();
          if (count == 1)
             assertCondition(JDParmHelper.verifyString("col2", "12345", connection));
          else
             failed ("invalid update count");

       } catch (SQLException e) {
          failed (e, "Unexpected Exception");
          
       } finally {
          JDParmHelper.purgeStringsTable(connection);
       }
   }



/**
Test:  Decimal(10, 0) - value is just right.
**/
   public void Var013() {
      try {
         col3.setString(1, "1234567890");
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", "1234567890", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(10, 0) - value is a single digit.
**/
   public void Var014() {
      try {
         col3.setString(1, "1");
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", "1", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  Decimal(10, 0) - empty string.  This is an error condition - an empty string is not 
    automatically treated as some default value.
**/
   public void Var015() {
      try {
         col3.setString(1, "");
         failed("insert into a Decimal field without a Decimal value.");
      } catch (SQLException e) {
          // SQL state should be 07006.  Vendor code should be -999999 and message text should
          // have the words incorrect conversion in it.
          // TODO:  Look this stuff up in the real SQL reference....
          assertCondition(true);
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(10, 0) - null value.  This is legal.
**/
   public void Var016() {
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
Test:  Decimal(10, 0) - value too big.  This is expected to throw a data truncation exception
    and doesn't today.
**/
   public void Var017() {
       String value = "12345678901";
      try {
         col3.setString(1, value);
         int count = col3.executeUpdate();
         failed("inserted a value without truncation "+count);
         assertCondition(JDParmHelper.verifyString("col3", "12345", connection));

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 10));
      } catch (SQLException e) {
          	  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox ");

      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(10, 0) - value with a decimal point - this is expected to work with the decimal
    point and trailing data being removed automatically.
**/
   public void Var018() {
       try {
          col3.setString(1, "1234567890.1");
          int count = col3.executeUpdate();
          if (count == 1)
             assertCondition(JDParmHelper.verifyString("col3", "1234567890", connection));
          else
             failed ("invalid update count");

       } catch (SQLException e) {
          failed (e, "Unexpected Exception");
          
       } finally {
          JDParmHelper.purgeStringsTable(connection);
       }
   }



/**
Test:  Decimal(5, 5) - value is just right (has leading 0).
**/
   public void Var019() {
      try {
         col4.setString(1, "0.12345");
         int count = col4.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col4", "0.12345", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(5, 5) - value is just right (no leading 0).
**/
   public void Var020() {
      try {
         col4.setString(1, ".12345");
         int count = col4.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col4", "0.12345", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(5, 5) - value is a single digit.

Note:  All Decimal and decimal values will have at least one digit in front
       of the decimal point, even when it is from an implied 0.
Note:  Decimal and decimal are 0 padded to the precision when they are 
       returned as strings.
**/
   public void Var021() {
      try {
         col4.setString(1, ".1");
         int count = col4.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col4", "0.10000", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  Decimal(5, 5) - empty string.  This is an error condition - an empty string is not 
    automatically treated as some default value.
**/
   public void Var022() {
      try {
         col4.setString(1, "");
         failed("insert into a Decimal field without a Decimal value.");
      } catch (SQLException e) {
          // SQL state should be 07006.  Vendor code should be -999999 and message text should
          // have the words incorrect conversion in it.
          // TODO:  Look this stuff up in the real SQL reference....
          assertCondition(true);
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(5, 5) - null value.  This is legal.
**/
   public void Var023() {
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
Test:  Decimal(5, 5) - value too big.  This is expected to work.  The extra precision is truncated.
**/
   public void Var024() {
      try {
         col4.setString(1, ".123456");
         int count = col4.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col4", "0.12345", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  Decimal(5, 5) - value with a integer component - this is expected to fail as there is significant
    truncation that happens.
**/
   public void Var025() {
       String value = "1.12345";
       int size=6;
       int transferSize=5;
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
       {
           size = 6;
           transferSize = 5;
       }
       try {
          col4.setString(1, value);
          int count = col4.executeUpdate();
          failed("inserted a value without truncation "+count);

       } catch (DataTruncation dt) {
	   if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 49160) {
	       notApplicable("Native driver fix in PTF V7R1 SI49160" );
	       return; 
	   }

          assertCondition ((dt.getIndex() == 1)
                  && (dt.getParameter() == true)
                  && (dt.getRead() == false)
                  && (dt.getDataSize() == size)
                  && (dt.getTransferSize() == transferSize), "Updated 1/18/2013 for V7R1 fix");
       } catch (SQLException e) {
	   assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox ");

       } finally {
          JDParmHelper.purgeStringsTable(connection);
       }
   }


/**
Test:  Decimal(1, 1) - Deal with constrained space and tricky strings.
**/
   public void Var026() {
       try {
          col5.setString(1, "0.");
          int count = col5.executeUpdate();
          if (count == 1)
             assertCondition(JDParmHelper.verifyString("col5", "0.0", connection));
          else
             failed ("invalid update count");

       } catch (SQLException e) {
          failed (e, "Unexpected Exception");
          
       } finally {
          JDParmHelper.purgeStringsTable(connection);
       }
   }



/**
Test:  Decimal(1, 1) - Deal with constrained space and tricky strings.
**/
   public void Var027() {
       try {
          col5.setString(1, "0.2");
          int count = col5.executeUpdate();
          if (count == 1)
             assertCondition(JDParmHelper.verifyString("col5", "0.2", connection));
          else
             failed ("invalid update count");

       } catch (SQLException e) {
          failed (e, "Unexpected Exception");
          
       } finally {
          JDParmHelper.purgeStringsTable(connection);
       }
   }


/**
Test:  Decimal(1, 1) - Deal with constrained space and tricky strings.
**/
   public void Var028() {
       try {
          col5.setString(1, "000000.");
          int count = col5.executeUpdate();
          if (count == 1)
             assertCondition(JDParmHelper.verifyString("col5", "0.0", connection));
          else
             failed ("invalid update count");

       } catch (SQLException e) {
          failed (e, "Unexpected Exception");
          
       } finally {
          JDParmHelper.purgeStringsTable(connection);
       }
   }

}



