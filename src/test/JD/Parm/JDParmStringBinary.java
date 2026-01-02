///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringBinary.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDParmStringBinary.java
//
// Classes:      JDParmStringBinary
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
Testcase JDParmStringBinary.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringBinary
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDParmStringBinary";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDParmTest.main(newArgs); 
   }



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
    public JDParmStringBinary (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringBinary",
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
           connection2 = testDriver_.getConnection(baseURL_+";translate binary=true", userId_, encryptedPassword_);

           Statement s = connection.createStatement();

           try {
              s.executeUpdate("drop table "+JDParmTest.COLLECTION+".strings");
           } catch (SQLException e) {
	       JDParmHelper.handleDropException(e,output_);


           }

           // Create a table that uses the largest row size the database will
           // allow me to use.
           s.executeUpdate("create table "+JDParmTest.COLLECTION+".strings " + 
                           "(col1 char(1) CCSID 65535, " +
                           "col2 char(20) CCSID 65535, " + 
                           "col3 char (32744) CCSID 65535)");


           s.close();

           col1 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col2 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col3 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");

           col4 = connection2.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col5 = connection2.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col6 = connection2.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");

           String shortValue = "abcdefghijklmnopqrstuvwxy";

           for (int i = 0; i < 1309; i++) {
              largeValue = largeValue + shortValue;
           }
           for (int i = 0; i < 19; i++) {
              largeValue = largeValue + "A";
           }

        } catch (Exception e) {
           output_.println("Caught exception: " + e.getMessage());
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
          output_.println("Caught exception: ");
          e.printStackTrace();
       }
   }



/**
Test:  binary(1) - value is just right
**/
   public void Var001() {
      try {
         col1.setString(1, "X");
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", "X", connection,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("Data type mismatch") != -1) 
              succeeded();//TB does not support X as a number
          else
              failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }


/**
Test:  binary(1) - empty string
**/
   public void Var002() {
      try {
         col1.setString(1, "");
         int count = col1.executeUpdate();
         String expected = "\0";
         
         if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) 
             expected = "\u0030\u0030";
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", expected, connection,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(1) - null value
**/
   public void Var003() {
      try {
         col1.setString(1, null);
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", null, connection,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(1) - value too big
**/
   public void Var004() {
       String value = "TOO BIG";
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

         output_.println("index is " + dt.getIndex());
         output_.println("parameter is " + dt.getParameter());
         output_.println("read is " + dt.getRead());
         output_.println("data size is " + dt.getDataSize());
         output_.println("transfer size is " + dt.getTransferSize());
      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("Data type mismatch") != -1) 
              succeeded();//TB does not support X as a number
          else
              failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(20) - value is just right
**/
   public void Var005() {
      try {
         col2.setString(1, "01234567890123456789");
         int count = col2.executeUpdate();
         String expected = "01234567890123456789";
         if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) 
             expected = "0123456789012345678900000000000000000000";
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", expected, connection,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(20) - value is smaller than column
**/
   public void Var006() {
      try {
         col2.setString(1, "0123456789");
         int count = col2.executeUpdate();
         String expected = "0123456789\0\0\0\0\0\0\0\0\0\0";
         if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) 
             expected = "0123456789000000000000000000000000000000";
         
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", expected, connection, output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }


/**
Test:  binary(20) - value is 1 char long
**/
   public void Var007() {
      try {
         col2.setString(1, "0");
         int count = col2.executeUpdate();
         String expected = "0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
         if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) 
             expected = "0000000000000000000000000000000000000000";
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", expected, connection, output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(20) - value is empty string
**/
   public void Var008() {
      try {
         col2.setString(1, "");
         int count = col2.executeUpdate();
         String expected = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
         if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) 
             expected = "0000000000000000000000000000000000000000";
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", expected, connection, output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(20) - value is null
**/
   public void Var009() {
      try {
         col2.setString(1, null);
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", null, connection,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }


/**
Test:  binary(20) - value too big
**/
   public void Var010() {
       String value = "012345678901234567890123";
       int valLen = value.length();
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) 
       {
           value =  "012345678901234567890123012345678901234567890123"; //tb does hex string to bytes (each two digits is one byte)
       }
       
      try {
         col2.setString(1, value);
         int count = col2.executeUpdate();
         if (count == 1)
            failed("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == valLen)
                 && (dt.getTransferSize() == 20));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(32744) - value is just right
**/
   public void Var011() {

      try {
         col3.setString(1, largeValue);
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", largeValue, connection,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("Data type mismatch") != -1) 
              succeeded(); 
          else
             failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(32744) - value is smaller than column
**/
   public void Var012() {
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
       {
           notApplicable();
           return;
       }
      try {
         col3.setString(1, "0123456789012345678901234567890123456789");
         int count = col3.executeUpdate();
         
         if (count == 1) {
             StringBuffer expected = new StringBuffer("0123456789012345678901234567890123456789");
             
             for (; 32744 - expected.length() > 0; ) {
                expected.append("\0");
             }
             assertCondition(JDParmHelper.verifyString("col3", expected.toString(), connection,output_));
         }
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }


/**
Test:  binary(32744) - value is 1 char long
**/
   public void Var013() {
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
       {
           notApplicable();
           return;
       }
      try {
         col3.setString(1, "0");
         int count = col3.executeUpdate();
         if (count == 1) {
            StringBuffer expected = new StringBuffer("0");
            for (; 32744 - expected.length() > 0; ) {
               expected.append("\0");
            }
            assertCondition(JDParmHelper.verifyString("col3", expected.toString(), connection,output_));
         }
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(32744) - value is empty string
**/
   public void Var014() {
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
       {
           notApplicable();
           return;
       }
      try {
         col3.setString(1, "");
         int count = col3.executeUpdate();
         if (count == 1) {
            StringBuffer expected = new StringBuffer();
            for (int i = 0; i < 32744; i++) {
               expected.append("\0");
            }
            assertCondition(JDParmHelper.verifyString("col3", expected.toString(), connection,output_));
         }
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(32744) - value is null
**/
   public void Var015() {
      try {
         col3.setString(1, null);
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", null, connection,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }


/**
Test:  binary(32744) - value too big
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
                 && (dt.getTransferSize() == 32744));
      } catch (SQLException e) {
          if (getDriver () == JDTestDriver.DRIVER_TOOLBOX && e.getMessage().indexOf("Data type mismatch") != -1) 
              succeeded(); 
          else
              failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(1) - value is just right with translateBinary=true
**/
   public void Var017() {
      try {
         col4.setString(1, "X");
         int count = col4.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", "X", connection2,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }


/**
Test:  binary(1) - empty string with translateBinary=true
**/
   public void Var018() {
      try {
         col4.setString(1, "");
         int count = col4.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", " ", connection2,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }



/**
Test:  binary(1) - null value with translateBinary=true
**/
   public void Var019() {
      try {
         col4.setString(1, null);
         int count = col4.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", null, connection2,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }



/**
Test:  binary(1) - value too big with translateBinary=true
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
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(20) - value is just right with translateBinary=true
**/
   public void Var021() {
      try {
         col5.setString(1, "01234567890123456789");
         int count = col5.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "01234567890123456789", connection2,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }



/**
Test:  binary(20) - value is smaller than column with translateBinary=true
**/
   public void Var022() {
      try {
         col5.setString(1, "0123456789");
         int count = col5.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "0123456789          ", connection2,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }


/**
Test:  binary(20) - value is 1 char long with translateBinary=true
**/
   public void Var023() {
      try {
         col5.setString(1, "0");
         int count = col5.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "0                   ", connection2,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }



/**
Test:  binary(20) - value is empty string with translateBinary=true
**/
   public void Var024() {
      try {
         col5.setString(1, "");
         int count = col5.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "                    ", connection2,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }



/**
Test:  binary(20) - value is null with translateBinary=true
**/
   public void Var025() {
      try {
         col5.setString(1, null);
         int count = col5.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", null, connection2,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }


/**
Test:  binary(20) - value too big with translateBinary=true
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
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  binary(32744) - value is just right with translateBinary=true
**/
   public void Var027() {
      try {
         col6.setString(1, largeValue);
         int count = col6.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", largeValue, connection2,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }



/**
Test:  binary(32744) - value is smaller than column with translateBinary=true
**/
   public void Var028() {
      try {
         col6.setString(1, "0123456789012345678901234567890123456789");
         int count = col6.executeUpdate();
         if (count == 1) {
             StringBuffer expected = new StringBuffer("0123456789012345678901234567890123456789");
             for (; 32744 - expected.length() > 0; ) {
                expected.append(" ");
             }
             assertCondition(JDParmHelper.verifyString("col3", expected.toString(), connection2,output_));
         }
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }


/**
Test:  binary(32744) - value is 1 char long with translateBinary=true
**/
   public void Var029() {
      try {
         col6.setString(1, "0");
         int count = col6.executeUpdate();
         if (count == 1) {
            StringBuffer expected = new StringBuffer("0");
            for (; 32744 - expected.length() > 0; ) {
               expected.append(" ");
            }
            assertCondition(JDParmHelper.verifyString("col3", expected.toString(), connection2,output_));
         }
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }



/**
Test:  binary(32744) - value is empty string with translateBinary=true
**/
   public void Var030() {
      try {
         col6.setString(1, "");
         int count = col6.executeUpdate();
         if (count == 1) {
            StringBuffer expected = new StringBuffer();
            for (int i = 0; i < 32744; i++) {
               expected.append(" ");
            }
            assertCondition(JDParmHelper.verifyString("col3", expected.toString(), connection2,output_));
         }
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }



/**
Test:  binary(32744) - value is null with translateBinary=true
**/
   public void Var031() {
      try {
         col6.setString(1, null);
         int count = col6.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", null, connection2,output_));
         else
            failed("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection2,output_);
      }
   }


/**
Test:  binary(32744) - value too big with translateBinary=true
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
                 && (dt.getTransferSize() == 32744));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }

}



