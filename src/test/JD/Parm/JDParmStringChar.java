///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringChar.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDParmStringChar.java
//
// Classes:      JDParmStringChar
//
////////////////////////////////////////////////////////////////////////

package test.JD.Parm;

import com.ibm.as400.access.AS400;

import test.JDParmTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.DataTruncation;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDParmStringChar.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringChar
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDParmStringChar";
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
    public JDParmStringChar (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringChar",
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

              // Ignore it.
           }

           // Create a table that uses the largest row size the database will
           // allow me to use.
           s.executeUpdate("create table "+JDParmTest.COLLECTION+".strings " + 
                           "(col1 char(1), col2 char(20), col3 char (32744))");


           s.close();

           col1 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col2 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col3 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");


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

      } catch (Exception e) {
         output_.println("Caught exception: ");
         e.printStackTrace();
      }
   }



/**
Test:  char(1) - value is just right
**/
   public void Var001() {
      try {
         col1.setString(1, "X");
         int count = col1.executeUpdate();
         if (count == 1)
            // assertCondition(JDParmHelper.verifyString("col1", "X", connection));
             assertCondition(JDParmHelper.verifyString("col1", "X", connection,output_));
         else
             failed ("invalid update count");

      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }


/**
Test:  char(1) - empty string
**/
   public void Var002() {
      try {
         col1.setString(1, "");
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", " ", connection,output_));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(1) - null value
**/
   public void Var003() {
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
Test:  char(1) - value too big
**/
   public void Var004() {
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
                 && (dt.getDataSize() == value.length()
		 )
                 && (dt.getTransferSize() == 1), "dataSize="+dt.getDataSize()+" SB "+value.length()+ "\n " + dt.getTransferSize()+" SB 1"); // Added more display information - @KKASHIR
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(20) - value is just right
**/
   public void Var005() {
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
   public void Var006() {
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
   public void Var007() {
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
   public void Var008() {
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
   public void Var009() {
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
   public void Var010() {
       String value = "012345678901234567890123";
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
                 && (dt.getDataSize() == value.length())
                 && (dt.getTransferSize() == 20),dt.getDataSize()+" SB "+value.length()+ "\n" + dt.getTransferSize()+" SB 20"); // Added more display information - @KKASHIR
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }



/**
Test:  char(32744) - value is just right
**/
   public void Var011() {
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
Test:  char(32744) - value is smaller than column
**/
   public void Var012() {
      try {
         col3.setString(1, "0123456789012345678901234567890123456789");
         int count = col3.executeUpdate();
         if (count == 1) {
             StringBuffer expected = new StringBuffer("0123456789012345678901234567890123456789");
             for (; 32744 - expected.length() > 0; ) {
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
Test:  char(32744) - value is 1 char long
**/
   public void Var013() {
      try {
         col3.setString(1, "0");
         int count = col3.executeUpdate();
         if (count == 1) {
            StringBuffer expected = new StringBuffer("0");
            for (; 32744 - expected.length() > 0; ) {
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
Test:  char(32744) - value is empty string
**/
   public void Var014() {
      try {
         col3.setString(1, "");
         int count = col3.executeUpdate();
         if (count == 1) {
            StringBuffer expected = new StringBuffer();
            for (int i = 0; i < 32744; i++) {
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
Test:  char(32744) - value is null
**/
   public void Var015() {
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
Test:  char(32744) - value too big
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
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection,output_);
      }
   }
/**
 * Test all valid CCSIDs
 */
   public void Var017() {
       String value = "12345";
       boolean passed = true; 
       StringBuffer sb = new StringBuffer(); 
       try {

	   for (int i = 1; i < 65535; i++) {
	     if (getDriver() == JDTestDriver.DRIVER_TOOLBOX &&
	         ( i == 1175 ||
	           i == 1377 || 
	           i == 61175 )) {           /* CCSID 61175 not support in DBTables -- see issue 66267 */ 
	       output_.println("Skipping CCSID "+i+" for toolbox driver"); 
	     } else if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
           ( i == 1047  ||   // Latin open sys 
             i == 1166  || // Cyrillic Multilingual -- Kazakhstan
             i == 1175  || 
             i == 1371  || // Chinese mixed
             i ==  1377 ||  // Mixed host HKSCS growing
             i ==  5233 || 
             i ==  57777 || // SAP codepage
             i ==  61175 ||   /* CCSID 61175 not support in DBTables -- see issue 66267 */ 
             i ==  62211 || 
             i ==  62224 ||
             i ==  62235 ||
             i ==  62245 ||
             i ==  62251 )) {
	       /* Skips CCSIDs missing iconv support */ 
       output_.println("Skipping CCSID "+i+" for native driver"); 
	     } else if ( (i < 2000) ||
           (  4000 < i  && i <  6000) ||
           (  8000 < i  && i < 10000) ||
           ( 12000 < i  && i < 14000) ||
           ( 28000 < i  && i < 29000) ||
           ( 57000 < i && i < 63000 )) {
	       try {
		   PreparedStatement ps = connection.prepareStatement("SELECT CAST(? AS CHAR(4) CCSID "+i+" ) FROM SYSIBM.SYSDUMMY1"); 
		   try {
		       output_.println("Testing CCSID "+i); 
		       ps.setString(1,value);
		       SQLWarning warning = ps.getWarnings(); 
		       ps.executeQuery();
		       if (warning == null) {
		         warning = ps.getWarnings(); 
		       } 
		       if (warning == null) { 
		         passed = false; 
		         sb.append("For CCSID "+i+" did not get warning\n"); 
		       }
		       if (warning instanceof DataTruncation) {
			   DataTruncation dt = (DataTruncation) warning;

			   if (dt.getIndex() != 1) {
			       passed =false;
			       sb.append("For CCSID "+i+" dt.getIndex()="+dt.getIndex()+" sb 1\n"); 
			   } 
			   if (dt.getParameter() != true) {
			       passed =false;
			       sb.append("For CCSID "+i+" dt.getParameter()="+dt.getParameter()+" sb true\n"); 
			   }
			   if (dt.getRead() != false) {
			       passed =false;
			       sb.append("For CCSID "+i+" dt.getRead()="+dt.getRead()+" sb false\n"); 
			   }
			   if (dt.getDataSize() != 5) {
			       passed =false;
			       sb.append("For CCSID "+i+" dt.getDataSize()="+dt.getDataSize()+" sb 5 \n"); 
			   }
			   if (dt.getTransferSize() != 4) {
			       passed =false;
			       sb.append("For CCSID "+i+" dt.getTransferSize()="+dt.getTransferSize()+" sb 4 \n"); 

			   }

		       }
		   } catch (Exception e) {
		       passed = false;
		       sb.append("Unexpected exception for CCSID "+i);
		       printStackTraceToStringBuffer(e, sb); 
		   } 

	       } catch (Exception e) {
		   // Ignore this CCSID 
	       }
       }
	   } /* for i */
	   assertCondition(passed,sb);
       } catch (Exception e) {
	   failed(e, "unexpected exceptionn "+sb.toString()); 
       }

       
   }


}



