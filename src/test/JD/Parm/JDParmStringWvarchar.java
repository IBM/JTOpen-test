///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringWvarchar.java
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
// File Name:    JDParmStringWvarchar.java
//
// Classes:      JDParmStringWvarchar
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
Testcase JDParmStringWvarchar.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringWvarchar
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDParmStringWvarchar";
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
    public PreparedStatement col7  = null;
    public PreparedStatement col8  = null;
    public PreparedStatement col9  = null;

public String largeValue = "";


/**
Constructor.
**/
    public JDParmStringWvarchar (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringWvarchar",
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

	   try {
	       s.executeUpdate("drop table "+JDParmTest.COLLECTION+".stringsVarchar");
           } catch (SQLException e) {
	       String message = e.toString();
	       if (message.indexOf("not found") > 0 &&
		   message.indexOf("type *FILE") > 0) {
	       } else {
		   System.out.println("Warning .. could not delete strings");
		   e.printStackTrace();

	       }
            }

	   try { 
	      s.executeUpdate("drop table "+JDParmTest.COLLECTION+".stringsVargraphic");
           } catch (SQLException e) {
	       String message = e.toString();
	       if (message.indexOf("not found") > 0 &&
		   message.indexOf("type *FILE") > 0) {
	       } else {
		   System.out.println("Warning .. could not delete strings");
		   e.printStackTrace();

	       }
            }

           // Create a table that uses the largest row size the database will
           // allow me to use.
           s.executeUpdate("create table "+JDParmTest.COLLECTION+".strings " + 
                           "(col1 vargraphic(1) CCSID 13488, " +
                           " col2 vargraphic(20) CCSID 13488, " + 
                           " col3 vargraphic(16346) CCSID 13488)");

	   if (true) {	
	       s.executeUpdate("create table "+JDParmTest.COLLECTION+".stringsVarchar " + 
			       "(col4 varchar(1) CCSID 1208, " +
			       " col5 varchar(20) CCSID 1208, " + 
			       " col6 varchar(16346) CCSID 1208)"); 

	       s.executeUpdate("create table "+JDParmTest.COLLECTION+".stringsVargraphic " + 
			       "(col7 vargraphic(1) CCSID 1200, " +
			       " col8 vargraphic(20) CCSID 1200, " + 
			       " col9 vargraphic(16346) CCSID 1200)");
	   }
           s.close();

           col1 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col2 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col3 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");
	   if (true) {	
	       col4 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".stringsVarchar (col4) values(?)");
	       col5 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".stringsVarchar (col5) values(?)");
	       col6 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".stringsVarchar (col6) values(?)");
	       col7 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".stringsVargraphic (col7) values(?)");
	       col8 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".stringsVargraphic (col8) values(?)");
	       col9 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".stringsVargraphic (col9) values(?)");
	   }
           String shortValue = "abcdefghijklmnopqrstuvwxy";

           StringBuffer largeValueSB = new StringBuffer(shortValue);

           while (largeValueSB.length() < 16346) {
               largeValueSB.append(shortValue);
           }

	   largeValue = largeValueSB.toString().substring(0, 16346);

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
Test:  wvarchar(1) - value is just right
**/
   public void Var001() {
      try {
         col1.setString(1, "X");
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", "X", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  wvarchar(1) - empty string
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
Test:  wvarchar(1) - null value
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
Test:  wvarchar(1) - value too big
**/
   public void Var004() {
       String value = "TOO BIG";
       
       int size = (value.length()*2);
       int transferSize = 2;
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
       {
           size = (value.length());
           transferSize = 1;
       }
       
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
                 && (dt.getDataSize() == size)
                 && (dt.getTransferSize() == transferSize));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  wvarchar(20) - value is just right
**/
   public void Var005() {
      try {
         col2.setString(1, "01234567890123456789");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "01234567890123456789", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  wvarchar(20) - value is smaller than column
**/
   public void Var006() {
      try {
         col2.setString(1, "0123456789");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "0123456789", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  wvarchar(20) - value is 1 char long
**/
   public void Var007() {
      try {
         col2.setString(1, "0");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "0", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  wvarchar(20) - value is empty string
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
Test:  wvarchar(20) - value is null
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
Test:  wvarchar(20) - value too big
**/
   public void Var010() {
       String value = "012345678901234567890123";
       
       int size = (value.length()*2);
       int transferSize = 40;
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
       {
           size = (value.length());
           transferSize = 20;
       }
       
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
                 && (dt.getDataSize() == size)
                 && (dt.getTransferSize() == transferSize));
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  wvarchar(16346) - value is just right
**/
   public void Var011() {
      try {
         col3.setString(1, largeValue);
         int count = col3.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col3", largeValue, connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  wvarchar(16346) - value is smaller than column
**/
   public void Var012() {
      try {
         col3.setString(1, "0123456789012345678901234567890123456789");
         int count = col3.executeUpdate();
         if (count == 1) {
             assertCondition(JDParmHelper.verifyString("col3", "0123456789012345678901234567890123456789", connection));
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
Test:  wvarchar(16346) - value is 1 char long
**/
   public void Var013() {
      try {
         col3.setString(1, "0");
         int count = col3.executeUpdate();
         if (count == 1) {
            assertCondition(JDParmHelper.verifyString("col3", "0", connection));
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
Test:  wvarchar(16346) - value is empty string
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
Test:  wvarchar(16346) - value is null
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
Test:  wvarchar(16346) - value too big
**/
   public void Var016() {
       String value = largeValue + "01234567890";
       int size = (value.length()*2);
       int transferSize = 32692;
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
       {
           size = (value.length());
           transferSize = 16346;
       }
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
                  && (dt.getDataSize() == size)
                  && (dt.getTransferSize() == transferSize));
       } catch (SQLException e) {
           failed (e, "Unexpected Exception");
       } finally {
          JDParmHelper.purgeStringsTable(connection);
       }
   }

// NERYKAL changed..........
/**
Test:  wvarchar(1) - value is just right
**/
   public void Var017() {
       try {
           col4.setString(1, "X");
           int count = col4.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col4", "X", connection, JDParmTest.COLLECTION+".stringsVarchar"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }


/**
Test:  wvarchar(1) - empty string
**/
   public void Var018() {
       try {
           col4.setString(1, "");
           int count = col4.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col4", "", connection, JDParmTest.COLLECTION+".stringsVarchar"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }



/**
Test:  wvarchar(1) - null value
**/
   public void Var019() {
       try {
           col4.setString(1, null);
           int count = col4.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col4", null, connection, JDParmTest.COLLECTION+".stringsVarchar"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }



/**
Test:  wvarchar(1) - value too big
**/
   public void Var020() {
       String value = "TOO BIG";
       try {
           col4.setString(1, value);
           int count = col4.executeUpdate();
           if (count == 1)
         failed ("inserted a value without truncation");
           else
         failed("no exception - just an update count");

       } catch (DataTruncation dt) {
           if(getDriver() == JDTestDriver.DRIVER_NATIVE)			// @A2
         assertCondition ((dt.getIndex() == 1)			// @A2
      		    && (dt.getParameter() == true)		// @A2
      		    && (dt.getRead() == false)			// @A2
      		    && (dt.getDataSize() == value.length())	// @A2
      		    && (dt.getTransferSize() == 1));		// @A2
           else								// @A2
         assertCondition ((dt.getIndex() == 1)
      		    && (dt.getParameter() == true)
      		    && (dt.getRead() == false)
      		    && (dt.getDataSize() == value.length())
      		    && (dt.getTransferSize() == 1));
       } catch (SQLException e) {
           failed (e, "Unexpected Exception");
       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }



/**
Test:  wvarchar(20) - value is just right
**/
   public void Var021() {
       try {
           col5.setString(1, "01234567890123456789");
           int count = col5.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col5", "01234567890123456789", connection, JDParmTest.COLLECTION+".stringsVarchar"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }



/**
Test:  wvarchar(20) - value is smaller than column
**/
   public void Var022() {
       try {
           col5.setString(1, "0123456789");
           int count = col5.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col5", "0123456789", connection, JDParmTest.COLLECTION+".stringsVarchar"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }


/**
Test:  wvarchar(20) - value is 1 char long
**/
   public void Var023() {
       try {
           col5.setString(1, "0");
           int count = col5.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col5", "0", connection, JDParmTest.COLLECTION+".stringsVarchar"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }



/**
Test:  wvarchar(20) - value is empty string
**/
   public void Var024() {
       try {
           col5.setString(1, "");
           int count = col5.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col5", "", connection, JDParmTest.COLLECTION+".stringsVarchar"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }



/**
Test:  wvarchar(20) - value is null
**/
   public void Var025() {
       try {
           col5.setString(1, null);
           int count = col5.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col5", null, connection, JDParmTest.COLLECTION+".stringsVarchar"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }


/**
Test:  wvarchar(20) - value too big
**/
   public void Var026() {
       String value = "012345678901234567890123";
       try {
           col5.setString(1, value);
           int count = col5.executeUpdate();
           if (count == 1)
         failed ("inserted a value without truncation");
           else
         failed("no exception - just an update count");

       } catch (DataTruncation dt) {
           if(getDriver() == JDTestDriver.DRIVER_NATIVE)			// @A2
         assertCondition ((dt.getIndex() == 1)			// @A2
      		    && (dt.getParameter() == true)		// @A2
      		    && (dt.getRead() == false)			// @A2
      		    && (dt.getDataSize() == value.length())	// @A2
      		    && (dt.getTransferSize() == 20));		// @A2
           else								// @A2
         assertCondition ((dt.getIndex() == 1)
      		    && (dt.getParameter() == true)
      		    && (dt.getRead() == false)
      		    && (dt.getDataSize() == value.length())
      		    && (dt.getTransferSize() == 20));
       } catch (SQLException e) {
           failed (e, "Unexpected Exception");
       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }



/**
Test:  wvarchar(16346) - value is just right
**/
   public void Var027() {
       try {
           col6.setString(1, largeValue);
           int count = col6.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col6", largeValue, connection, JDParmTest.COLLECTION+".stringsVarchar"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }



/**
Test:  wvarchar(16346) - value is smaller than column
**/
   public void Var028() {
       try {
           col6.setString(1, "0123456789012345678901234567890123456789");
           int count = col6.executeUpdate();
           if (count == 1) {
         assertCondition(JDParmHelper.verifyString("col6", "0123456789012345678901234567890123456789", connection, JDParmTest.COLLECTION+".stringsVarchar"));
           }
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }


/**
Test:  wvarchar(16346) - value is 1 char long
**/
   public void Var029() {
       try {
           col6.setString(1, "0");
           int count = col6.executeUpdate();
           if (count == 1) {
         assertCondition(JDParmHelper.verifyString("col6", "0", connection, JDParmTest.COLLECTION+".stringsVarchar"));
           }
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }



/**
Test:  wvarchar(16346) - value is empty string
**/
   public void Var030() {
       try {
           col6.setString(1, "");
           int count = col6.executeUpdate();
           if (count == 1) {
         assertCondition(JDParmHelper.verifyString("col6", "", connection, JDParmTest.COLLECTION+".stringsVarchar"));
           }
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }



/**
Test:  wvarchar(16346) - value is null
**/
   public void Var031() {
       try {
           col6.setString(1, null);
           int count = col6.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col6", null, connection, JDParmTest.COLLECTION+".stringsVarchar"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }


/**
Test:  wvarchar(16346) - value too big
**/
   public void Var032() {
       String value = largeValue + "01234567890";
       try {
           col6.setString(1, value);
           int count = col6.executeUpdate();
           if (count == 1)
         failed ("inserted a value without truncation");
           else
         failed("no exception - just an update count");

       } catch (DataTruncation dt) {
           if(getDriver() == JDTestDriver.DRIVER_NATIVE)			// @A2
         assertCondition ((dt.getIndex() == 1)			// @A2
      		    && (dt.getParameter() == true)		// @A2
      		    && (dt.getRead() == false)			// @A2
      		    && (dt.getDataSize() == value.length())	// @A2
      		    && (dt.getTransferSize() == 16346));	// @A2
           else								// @A2
         assertCondition ((dt.getIndex() == 1)
      		    && (dt.getParameter() == true)
      		    && (dt.getRead() == false)
      		    && (dt.getDataSize() == value.length())
      		    && (dt.getTransferSize() == 16346));
       } catch (SQLException e) {
           failed (e, "Unexpected Exception");
       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVarchar");
       } 

   }

/**
Test:  wvarchar(1) - value is just right
**/
   public void Var033() {
       try {
           col7.setString(1, "X");
           int count = col7.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col7", "X", connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }


/**
Test:  wvarchar(1) - empty string
**/
   public void Var034() {
       try {
           col7.setString(1, "");
           int count = col7.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col7", "", connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }



/**
Test:  wvarchar(1) - null value
**/
   public void Var035() {
       try {
           col7.setString(1, null);
           int count = col7.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col7", null, connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }



/**
Test:  wvarchar(1) - value too big
**/
   public void Var036() {
       String value = "TOO BIG";
       
       int size = (value.length()*2);
         int transferSize = 2;
         if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
         {
             size = (value.length());
             transferSize = 1;
         }
         
       try {
           col7.setString(1, value);
           int count = col7.executeUpdate();
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
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }



/**
Test:  wvarchar(20) - value is just right
**/
   public void Var037() {
       try {
           col8.setString(1, "01234567890123456789");
           int count = col8.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col8", "01234567890123456789", connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }



/**
Test:  wvarchar(20) - value is smaller than column
**/
   public void Var038() {
       try {
           col8.setString(1, "0123456789");
           int count = col8.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col8", "0123456789", connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }


/**
Test:  wvarchar(20) - value is 1 char long
**/
   public void Var039() {
       try {
           col8.setString(1, "0");
           int count = col8.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col8", "0", connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }



/**
Test:  wvarchar(20) - value is empty string
**/
   public void Var040() {
       try {
           col8.setString(1, "");
           int count = col8.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col8", "", connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }



/**
Test:  wvarchar(20) - value is null
**/
   public void Var041() {
       try {
           col8.setString(1, null);
           int count = col8.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col8", null, connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }


/**
Test:  wvarchar(20) - value too big
**/
   public void Var042() {
       String value = "012345678901234567890123";
       
       int size = (value.length()*2);
         int transferSize = 40;
         if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
         {
             size = (value.length());
             transferSize = 20;
         }
         
       try {
           col8.setString(1, value);
           int count = col8.executeUpdate();
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
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }



/**
Test:  wvarchar(16346) - value is just right
**/
   public void Var043() {
       try {	
           col9.setString(1, largeValue);
           int count = col9.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col9", largeValue, connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }



/**
Test:  wvarchar(16346) - value is smaller than column
**/
   public void Var044() {
       try {
           col9.setString(1, "0123456789012345678901234567890123456789");
           int count = col9.executeUpdate();
           if (count == 1) {
         assertCondition(JDParmHelper.verifyString("col9", "0123456789012345678901234567890123456789", connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           }
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }


/**
Test:  wvarchar(16346) - value is 1 char long
**/
   public void Var045() {
       try {
           col9.setString(1, "0");
           int count = col9.executeUpdate();
           if (count == 1) {
         assertCondition(JDParmHelper.verifyString("col9", "0", connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           }
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }



/**
Test:  wvarchar(16346) - value is empty string
**/
   public void Var046() {
       try {
           col9.setString(1, "");
           int count = col9.executeUpdate();
           if (count == 1) {
         assertCondition(JDParmHelper.verifyString("col9", "", connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           }
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 

   }



/**
Test:  wvarchar(16346) - value is null
**/
   public void Var047() {
       try {
           col9.setString(1, null);
           int count = col9.executeUpdate();
           if (count == 1)
         assertCondition(JDParmHelper.verifyString("col9", null, connection, JDParmTest.COLLECTION+".stringsVargraphic"));
           else
         failed ("invalid update count");

       } catch (SQLException e) {
           failed (e, "Unexpected Exception");

       } finally {
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 
   }


/**
Test:  wvarchar(16346) - value too big
**/
   public void Var048() {
       String value = largeValue + "01234567890";
         
         int size = (value.length()*2);
         int transferSize = 32692;
         if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
         {
             size = (value.length());
             transferSize = 16346;
         }
         
       try {
           col9.setString(1, value);
           int count = col9.executeUpdate();
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
           JDParmHelper.purgeStringsTable(connection, JDParmTest.COLLECTION+".stringsVargraphic");
       } 
   }
}
