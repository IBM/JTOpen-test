///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringVarbinary.java
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
// File Name:    JDParmStringVarbinary.java
//
// Classes:      JDParmStringVarbinary
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
Testcase JDParmStringVarbinary.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringVarbinary
extends JDTestcase {



    // Private data.
    public Connection connection = null;
    public Connection connection2 = null;
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
    public JDParmStringVarbinary (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringVarbinary",
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
           connection2 = testDriver_.getConnection(baseURL_+";translate binary=true", userId_, encryptedPassword_);

           Statement s = connection.createStatement();

           try {
              s.executeUpdate("drop table "+JDParmTest.COLLECTION+".strings");
           } catch (SQLException e) {
	       JDParmHelper.handleDropException(e);

           }

           // Create a table that uses the largest row size the database will
           // allow me to use.
           s.executeUpdate("create table "+JDParmTest.COLLECTION+".strings " + 
                           "(col1 varchar(1) CCSID 65535, " +
                           "col2 varchar(20) CCSID 65535, " + 
                           "col3 varchar (32714) CCSID 65535)");


           s.close();

           col1 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col2 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col3 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");

           col4 = connection2.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col5 = connection2.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col6 = connection2.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");

           String shortValue = "abcdefghijklmnopqrstuvwxy";

           StringBuffer largeValueSB = new StringBuffer(shortValue);

           while (largeValueSB.length() < 32714) {
               largeValueSB.append(shortValue);
           }
	        largeValue = largeValueSB.toString().substring(0, 32714);


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
          connection2.close();

       } catch (Exception e) {
          System.out.println("Caught exception: ");
          e.printStackTrace();
       }
   }



/**
Test:  varbinary(1) - value is just right
**/
   public void Var001() {
      try {
         col1.setString(1, "X");
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", "X", connection));
         else
            failed("invalid update count");

      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("Data type mismatch") != -1) 
              succeeded();
          else
             failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  varbinary(1) - empty string
**/
   public void Var002() {
      try {
         col1.setString(1, "");
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", "", connection));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(1) - null value
**/
   public void Var003() {
      try {
         col1.setString(1, null);
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", null, connection));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(1) - value too big
**/
   public void Var004() {
       String value  = "TOO BIG";
      try {
         col1.setString(1, value);
         int count = col1.executeUpdate();
         if (count == 1)
            failed("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 1));
      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("Data type mismatch") != -1) 
              succeeded();
          else
              failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(20) - value is just right
**/
   public void Var005() {
      try {
         col2.setString(1, "01234567890123456789");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "01234567890123456789", connection));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(20) - value is smaller than column
**/
   public void Var006() {
      try {
         col2.setString(1, "0123456789");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "0123456789", connection));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  varbinary(20) - value is 1 char long
**/
   public void Var007() {
      try {
          String expected = "0";
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) 
              expected = "\u0030\u0030";
         col2.setString(1, "0");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", expected, connection));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(20) - value is empty string
**/
   public void Var008() {
      try {
         col2.setString(1, "");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "", connection));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(20) - value is null
**/
   public void Var009() {
      try {
         col2.setString(1, null);
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", null, connection));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  varbinary(20) - value too big
**/
   public void Var010() {
       String value = "012345678901234567890123";
      try {
         col2.setString(1, value);
         int count = col2.executeUpdate();
         if (getDriver () == JDTestDriver.DRIVER_TOOLBOX &&  count == 1) 
         {
                 succeeded();
                 return;
         }
         if (count == 1)
            failed("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 20));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(32714) - value is just right
**/
   public void Var011() {
      try {
         col3.setString(1, largeValue);
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", largeValue, connection));
         else
            failed("invalid update count");

      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("Data type mismatch") != -1) 
              succeeded();
          else
              failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(32714) - value is smaller than column
**/
   public void Var012() {
      try {
         col3.setString(1, "0123456789012345678901234567890123456789");
         int count = col3.executeUpdate();
         if (count == 1) {
             assertCondition(JDParmHelper.verifyString("col3", "0123456789012345678901234567890123456789", connection));
         }
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  varbinary(32714) - value is 1 char long
**/
   public void Var013() {
      try {
          String expected = "0";
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) 
              expected = "\u0030\u0030";
          
         col3.setString(1, "0");
         int count = col3.executeUpdate();
         if (count == 1) {
            assertCondition(JDParmHelper.verifyString("col3", expected, connection));
         }
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(32714) - value is empty string
**/
   public void Var014() {
      try {
         col3.setString(1, "");
         int count = col3.executeUpdate();
         if (count == 1) {
            assertCondition(JDParmHelper.verifyString("col3", "", connection));
         }
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(32714) - value is null
**/
   public void Var015() {
      try {
         col3.setString(1, null);
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", null, connection));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  varbinary(32714) - value too big
**/
   public void Var016() {
       String value = largeValue + "01234567890";
      try {
         col3.setString(1, value);
         int count = col3.executeUpdate();
         if (count == 1)
            failed("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 32714));
      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("Data type mismatch") != -1) 
              succeeded();
          else
              failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(1) - value is just right with translateBinary=true
**/
   public void Var017() {
      try {
         col4.setString(1, "X");
         int count = col4.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", "X", connection2));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  varbinary(1) - empty string with translateBinary=true
**/
   public void Var018() {
      try {
         col4.setString(1, "");
         int count = col4.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", "", connection2));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2);
      }
   }



/**
Test:  varbinary(1) - null value with translateBinary=true
**/
   public void Var019() {
      try {
         col4.setString(1, null);
         int count = col4.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", null, connection2));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2);
      }
   }



/**
Test:  varbinary(1) - value too big with translateBinary=true
**/
   public void Var020() {
       String value = "TOO BIG";
      try {
         col4.setString(1, value);
         int count = col4.executeUpdate();
         if (count == 1)
            failed("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 1));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(20) - value is just right with translateBinary=true
**/
   public void Var021() {
      try {
         col5.setString(1, "01234567890123456789");
         int count = col5.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "01234567890123456789", connection2));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2);
      }
   }



/**
Test:  varbinary(20) - value is smaller than column with translateBinary=true
**/
   public void Var022() {
      try {
         col5.setString(1, "0123456789");
         int count = col5.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "0123456789", connection2));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2);
      }
   }


/**
Test:  varbinary(20) - value is 1 char long with translateBinary=true
**/
   public void Var023() {
      try {
         col5.setString(1, "0");
         int count = col5.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "0", connection2));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2);
      }
   }



/**
Test:  varbinary(20) - value is empty string with translateBinary=true
**/
   public void Var024() {
      try {
         col5.setString(1, "");
         int count = col5.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "", connection2));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2);
      }
   }



/**
Test:  varbinary(20) - value is null with translateBinary=true
**/
   public void Var025() {
      try {
         col5.setString(1, null);
         int count = col5.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", null, connection2));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2);
      }
   }


/**
Test:  varbinary(20) - value too big with translateBinary=true
**/
   public void Var026() {
       String value = "012345678901234567890123";
      try {
         col5.setString(1, value);
         int count = col5.executeUpdate();
         if (count == 1)
            failed("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 20));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  varbinary(32714) - value is just right with translateBinary=true
**/
   public void Var027() {
      try {
         col6.setString(1, largeValue);
         int count = col6.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", largeValue, connection2));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2);
      }
   }



/**
Test:  varbinary(32714) - value is smaller than column with translateBinary=true
**/
   public void Var028() {
      try {
         col6.setString(1, "0123456789012345678901234567890123456789");
         int count = col6.executeUpdate();
         if (count == 1) {
             assertCondition(JDParmHelper.verifyString("col3", "0123456789012345678901234567890123456789", connection2));
         }
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2);
      }
   }


/**
Test:  varbinary(32714) - value is 1 char long with translateBinary=true
**/
   public void Var029() {
      try {
         col6.setString(1, "0");
         int count = col6.executeUpdate();
         if (count == 1) {
            assertCondition(JDParmHelper.verifyString("col3", "0", connection2));
         }
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2);
      }
   }



/**
Test:  varbinary(32714) - value is empty string with translateBinary=true
**/
   public void Var030() {
      try {
         col6.setString(1, "");
         int count = col6.executeUpdate();
         if (count == 1) {
            assertCondition(JDParmHelper.verifyString("col3", "", connection2));
         }
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2);
      }
   }



/**
Test:  varbinary(32714) - value is null with translateBinary=true
**/
   public void Var031() {
      try {
         col6.setString(1, null);
         int count = col6.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", null, connection2));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2);
      }
   }


/**
Test:  varbinary(32714) - value too big with translateBinary=true
**/
   public void Var032() {
       String value = largeValue + "01234567890";
      try {
         col6.setString(1, value);
         int count = col6.executeUpdate();
         if (count == 1)
            failed("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 32714));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }




}



