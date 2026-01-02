///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringMixed.java
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
// File Name:    JDParmStringMixed.java
//
// Classes:      JDParmStringMixed
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
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDParmStringMixed.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringMixed
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDParmStringMixed";
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
    public JDParmStringMixed (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringMixed",
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
	       JDParmHelper.handleDropException(e,output_);

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

      } catch (Exception e) {
         output_.println("Caught exception: ");
         e.printStackTrace();
      }
   }



/**
Test:  char(4) - value is just right
**/
   public void Var001() {
      try {
         col1.setString(1, "XXXX");
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", "XXXX", connection,output_));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }


/**
Test:  char(4) - smaller than the column
**/
   public void Var002() {
      try {
         col1.setString(1, "XX");
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", "XX  ", connection,output_));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }


/**
Test:  char(4) - empty string
**/
   public void Var003() {
      try {
         col1.setString(1, "");
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", "    ", connection,output_));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(4) - null value
**/
   public void Var004() {
      try {
         col1.setString(1, null);
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", null, connection,output_));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(4) - value too big
**/
   public void Var005() {
       String value = "TOO BIG";
      try {
         col1.setString(1, value);
         int count = col1.executeUpdate();
         if (count == 1)
            failed ("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 4),
		" dt.getIndex()="+ dt.getIndex()+"!=1" +
	        " dt.getParameter()="+dt.getParameter()+"!=true"+
                " dt.getRead()="+dt.getRead()+"!=false"+
                " dt.getDataSize()="+dt.getDataSize()+"!="+value.length()+
	        " dt.getTransferSize()="+dt.getTransferSize()+"!=4"	  );
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(20) - value is just right
**/
   public void Var006() {
      try {
         col2.setString(1, "01234567890123456789");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "01234567890123456789", connection,output_));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(20) - value is smaller than column
**/
   public void Var007() {
      try {
         col2.setString(1, "0123456789");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "0123456789          ", connection,output_));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }


/**
Test:  char(20) - value is 1 char long
**/
   public void Var008() {
      try {
         col2.setString(1, "0");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "0                   ", connection,output_));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(20) - value is empty string
**/
   public void Var009() {
      try {
         col2.setString(1, "");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "                    ", connection,output_));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(20) - value is null
**/
   public void Var010() {
      try {
         col2.setString(1, null);
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", null, connection,output_));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }


/**
Test:  char(20) - value too big
**/
   public void Var011() {
       String value = "012345678901234567890123";
      try {
         col2.setString(1, value);
         int count = col2.executeUpdate();
         if (count == 1)
            failed ("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 20),
		" dt.getIndex()="+ dt.getIndex()+"!=1" +
	        " dt.getParameter()="+dt.getParameter()+"!=true"+
                " dt.getRead()="+dt.getRead()+"!=false"+
                " dt.getDataSize()="+dt.getDataSize()+"!="+value.length()+
	        " dt.getTransferSize()="+dt.getTransferSize()+"!=20"	  );

      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(32741) - value is just right
**/
   public void Var012() {
      try {
         col3.setString(1, largeValue);
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", largeValue, connection,output_));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(32741) - value is smaller than column
**/
   public void Var013() {
      try {
         col3.setString(1, "0123456789012345678901234567890123456789");
         int count = col3.executeUpdate();
         if (count == 1) {
             StringBuffer expected = new StringBuffer("0123456789012345678901234567890123456789");
             for (; 32741 - expected.length() > 0; ) {
                expected.append(" ");
             }
             assertCondition(JDParmHelper.verifyString("col3", expected.toString(), connection,output_));
         }
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }


/**
Test:  char(32741) - value is 1 char long
**/
   public void Var014() {
      try {
         col3.setString(1, "0");
         int count = col3.executeUpdate();
         if (count == 1) {
            StringBuffer expected = new StringBuffer("0");
            for (; 32741 - expected.length() > 0; ) {
               expected.append(" ");
            }
            assertCondition(JDParmHelper.verifyString("col3", expected.toString(), connection,output_));
         }
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(32741) - value is empty string
**/
   public void Var015() {
      try {
         col3.setString(1, "");
         int count = col3.executeUpdate();
         if (count == 1) {
            StringBuffer expected = new StringBuffer();
            for (int i = 0; i < 32741; i++) {
               expected.append(" ");
            }
            assertCondition(JDParmHelper.verifyString("col3", expected.toString(), connection,output_));
         }
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(32741) - value is null
**/
   public void Var016() {
      try {
         col3.setString(1, null);
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", null, connection,output_));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }


/**
Test:  char(32741) - value too big
**/
   public void Var017() {
       String value = largeValue + "01234567890";
      try {
         col3.setString(1, value);
         int count = col3.executeUpdate();
         if (count == 1)
            failed ("inserted a value without truncation");
         else
            failed("no exception - just an update count");

      } catch (DataTruncation dt) {
         assertCondition ((dt.getIndex() == 1)
                 && (dt.getParameter() == true)
                 && (dt.getRead() == false)
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 32741),
		" dt.getIndex()="+ dt.getIndex()+"!=1" +
	        " dt.getParameter()="+dt.getParameter()+"!=true"+
                " dt.getRead()="+dt.getRead()+"!=false"+
                " dt.getDataSize()="+dt.getDataSize()+"!="+value.length()+
	        " dt.getTransferSize()="+dt.getTransferSize()+"!=32741"	);
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }
}



