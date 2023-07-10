///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringWchar.java
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
// File Name:    JDParmStringWchar.java
//
// Classes:      JDParmStringWchar
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
Testcase JDParmStringWchar.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringWchar
extends JDTestcase {



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
    public JDParmStringWchar (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringWchar",
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
	       String message = e.toString();
	       if (message.indexOf("not found") > 0 &&
		   message.indexOf("type *FILE") > 0) {
	       } else {
		   System.out.println("Warning .. could not delete strings");
		   e.printStackTrace();

	       }
           }

	   try {
	      s.executeUpdate("drop table "+JDParmTest.COLLECTION+".stringsChar1");
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
	      s.executeUpdate("drop table "+JDParmTest.COLLECTION+".stringsGraphic1");
	   } catch (SQLException e) {
	       JDParmHelper.handleDropException(e);
           }

           // Create a table that uses the largest row size the database will
           // allow me to use.
           s.executeUpdate("create table "+JDParmTest.COLLECTION+".strings " + 
                           "(col1 graphic(1) CCSID 13488, " +
                           " col2 graphic(20) CCSID 13488, " + 
                           " col3 graphic(16361) CCSID 13488)");

	   if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	       s.executeUpdate("create table "+JDParmTest.COLLECTION+".stringsChar1 " + 
			       "(col4 char(1) CCSID 1208, " +
			       " col5 char(20) CCSID 1208, " + 
			       " col6 char(16361) CCSID 1208)");

	       s.executeUpdate("create table "+JDParmTest.COLLECTION+".stringsGraphic1" + 
			       "(col7 graphic(1) CCSID 1200, " +
			       " col8 graphic(20) CCSID 1200, " + 
			       " col9 graphic(16361) CCSID 1200)");
	   }
	   s.close();

           col1 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col2 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col3 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");
	   if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	       col4 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".stringsChar1 (col4) values(?)");
	       col5 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".stringsChar1 (col5) values(?)");
	       col6 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".stringsChar1 (col6) values(?)");
	       col7 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".stringsGraphic1 (col7) values(?)");
	       col8 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".stringsGraphic1 (col8) values(?)");
	       col9 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".stringsGraphic1 (col9) values(?)");
	   }
           String shortValue = "abcdefghijklmnopqrstuvwxy";

           StringBuffer largeValueSB = new StringBuffer(shortValue);

           while (largeValueSB.length() < 16361) {
               largeValueSB.append(shortValue);
           }
	   largeValue = largeValueSB.toString().substring(0, 16361);

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
         e.printStackTrace();
      }
   }

/**
Test:  wchar(1) - value is just right
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
Test:  wchar(1) - empty string
**/
   public void Var002() {
      try {
         col1.setString(1, "");
         int count = col1.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col1", " ", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  wchar(1) - null value
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
Test:  wchar(1) - value too big
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
                 && (dt.getTransferSize() == transferSize), 
                 "dt.getIndex()="+dt.getIndex()+" sb 1 "+
                 "dt.getParameter()="+dt.getParameter()+" sb true "+
                 "dt.getRead()="+dt.getRead()+" sb false "+
                 "dt.getDataSize()="+dt.getDataSize()+" sb "+size+" "+
                 "dt.getTransferSize()="+dt.getTransferSize()+" sb 2 ");
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  wchar(20) - value is just right
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
Test:  wchar(20) - value is smaller than column
**/
   public void Var006() {
      try {
         col2.setString(1, "0123456789");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "0123456789          ", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }


/**
Test:  wchar(20) - value is 1 char long
**/
   public void Var007() {
      try {
         col2.setString(1, "0");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "0                   ", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  wchar(20) - value is empty string
**/
   public void Var008() {
      try {
         col2.setString(1, "");
         int count = col2.executeUpdate();
         if (count == 1)
            assertCondition(JDParmHelper.verifyString("col2", "                    ", connection));
         else
            failed ("invalid update count");

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  wchar(20) - value is null
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
Test:  wchar(20) - value too big
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
                 && (dt.getTransferSize() == transferSize), 
                 "dt.getIndex()="+dt.getIndex()+" sb 1 "+
                 "dt.getParameter()="+dt.getParameter()+" sb true "+
                 "dt.getRead()="+dt.getRead()+" sb false "+
                 "dt.getDataSize()="+dt.getDataSize()+" sb "+size+" "+
                 "dt.getTransferSize()="+dt.getTransferSize()+" sb 40 ");

      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }



/**
Test:  wchar(16361) - value is just right
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
Test:  wchar(16361) - value is smaller than column
**/
   public void Var012() {
      try {
         col3.setString(1, "0123456789012345678901234567890123456789");
         int count = col3.executeUpdate();
         if (count == 1) {
             StringBuffer expected = new StringBuffer("0123456789012345678901234567890123456789");
             for (; 16361 - expected.length() > 0; ) {
                expected.append(" ");
             }
             assertCondition(JDParmHelper.verifyString("col3", expected.toString(), connection));
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
Test:  wchar(16361) - value is 1 char long
**/
   public void Var013() {
      try {
         col3.setString(1, "0");
         int count = col3.executeUpdate();
         if (count == 1) {
            StringBuffer expected = new StringBuffer("0");
            for (; 16361 - expected.length() > 0; ) {
               expected.append(" ");
            }
            assertCondition(JDParmHelper.verifyString("col3", expected.toString(), connection));
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
Test:  wchar(16361) - value is empty string
**/
   public void Var014() {
      try {
         col3.setString(1, "");
         int count = col3.executeUpdate();
         if (count == 1) {
            StringBuffer expected = new StringBuffer();
            for (int i = 0; i < 16361; i++) {
               expected.append(" ");
            }
            assertCondition(JDParmHelper.verifyString("col3", expected.toString(), connection));
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
Test:  wchar(16361) - value is null
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
Test:  wchar(16361) - value too big
**/
   public void Var016() {
       String value = largeValue + "01234567890";
       int size = (value.length()*2);
       int transferSize = 32722;
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
       {
           size = (value.length());
           transferSize = 16361;
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
                 && (dt.getTransferSize() == transferSize), 
                 "dt.getIndex()="+dt.getIndex()+" sb 1 "+
                 "dt.getParameter()="+dt.getParameter()+" sb true "+
                 "dt.getRead()="+dt.getRead()+" sb false "+
                 "dt.getDataSize()="+dt.getDataSize()+" sb "+size+" "+
                 "dt.getTransferSize()="+dt.getTransferSize()+" sb 32722 ");
      } catch (SQLException e) {
          failed (e, "Unexpected Exception");
      } finally {
         JDParmHelper.purgeStringsTable(connection);
      }
   }

//NERYKAL Changes .............
/**
Test:  wchar(1) - value is just right
**/
   public void Var017() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col4.setString(1, "X");
	       int count = col4.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col4", "X", connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 Version"); 
       } 
   }


/**
Test:  wchar(1) - empty string
**/
   public void Var018() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col4.setString(1, "");
	       int count = col4.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col4", " ", connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(1) - null value
**/
   public void Var019() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col4.setString(1, null);
	       int count = col4.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col4", null, connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(1) - value too big
**/
   public void Var020() {
       
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
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
				    && (dt.getTransferSize() == 1), 
                                         "dt.getIndex()="+dt.getIndex()+" sb 1 "+
                                         "dt.getParameter()="+dt.getParameter()+" sb true "+
                                         "dt.getRead()="+dt.getRead()+" sb false "+
                                         "dt.getDataSize()="+dt.getDataSize()+" sb "+(value.length()*2)+" "+
                                         "dt.getTransferSize()="+dt.getTransferSize()+" sb 2 ");
	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");
	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(20) - value is just right
**/
   public void Var021() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col5.setString(1, "01234567890123456789");
	       int count = col5.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col5", "01234567890123456789", connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(20) - value is smaller than column
**/
   public void Var022() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col5.setString(1, "0123456789");
	       int count = col5.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col5", "0123456789          ", connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }


/**
Test:  wchar(20) - value is 1 char long
**/
   public void Var023() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {
	   try {
	       col5.setString(1, "0");
	       int count = col5.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col5", "0                   ", connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(20) - value is empty string
**/
   public void Var024() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {
	   try {
	       col5.setString(1, "");
	       int count = col5.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col5", "                    ", connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(20) - value is null
**/
   public void Var025() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {
	   try {
	       col5.setString(1, null);
	       int count = col5.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col5", null, connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }


/**
Test:  wchar(20) - value too big
**/
   public void Var026() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {
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
				    && (dt.getTransferSize() == 20), 
                                         "dt.getIndex()="+dt.getIndex()+" sb 1 "+
                                         "dt.getParameter()="+dt.getParameter()+" sb true "+
                                         "dt.getRead()="+dt.getRead()+" sb false "+
                                         "dt.getDataSize()="+dt.getDataSize()+" sb "+(value.length()*2)+" "+
                                         "dt.getTransferSize()="+dt.getTransferSize()+" sb 40 ");
	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");
	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(16361) - value is just right
**/
   public void Var027() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col6.setString(1, largeValue);
	       int count = col6.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col6", largeValue, connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(16361) - value is smaller than column
**/
   public void Var028() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col6.setString(1, "0123456789012345678901234567890123456789");
	       int count = col6.executeUpdate();
	       if (count == 1) {
		   StringBuffer expected = new StringBuffer("0123456789012345678901234567890123456789");
		   for (; 16361 - expected.length() > 0; ) {
		       expected.append(" ");
		   }
		   assertCondition(JDParmHelper.verifyString("col6", expected.toString(), connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       }
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }


/**
Test:  wchar(16361) - value is 1 char long
**/
   public void Var029() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col6.setString(1, "0");
	       int count = col6.executeUpdate();
	       if (count == 1) {
		   StringBuffer expected = new StringBuffer("0");
		   for (; 16361 - expected.length() > 0; ) {
		       expected.append(" ");
		   }
		   assertCondition(JDParmHelper.verifyString("col6", expected.toString(), connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       }
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(16361) - value is empty string
**/
   public void Var030() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col6.setString(1, "");
	       int count = col6.executeUpdate();
	       if (count == 1) {
		   StringBuffer expected = new StringBuffer();
		   for (int i = 0; i < 16361; i++) {
		       expected.append(" ");
		   }
		   assertCondition(JDParmHelper.verifyString("col6", expected.toString(), connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       }
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(16361) - value is null
**/
   public void Var031() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col6.setString(1, null);
	       int count = col6.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col6", null, connection, ""+JDParmTest.COLLECTION+".stringsChar1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }


/**
Test:  wchar(16361) - value too big
**/
   public void Var032() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
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
				    && (dt.getTransferSize() == 16361));	// @A2
	       else								// @A2
		   assertCondition ((dt.getIndex() == 1)
				    && (dt.getParameter() == true)
				    && (dt.getRead() == false)
				    && (dt.getDataSize() == value.length())
				    && (dt.getTransferSize() == 16361), 
                                         "dt.getIndex()="+dt.getIndex()+" sb 1 "+
                                         "dt.getParameter()="+dt.getParameter()+" sb true "+
                                         "dt.getRead()="+dt.getRead()+" sb false "+
                                         "dt.getDataSize()="+dt.getDataSize()+" sb "+(value.length()*2)+" "+
                                         "dt.getTransferSize()="+dt.getTransferSize()+" sb 32722 ");
	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");
	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsChar1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }

/**
Test:  wchar(1) - value is just right
**/
   public void Var033() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col7.setString(1, "X");
	       int count = col7.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col7", "X", connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }


/**
Test:  wchar(1) - empty string
**/
   public void Var034() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col7.setString(1, "");
	       int count = col7.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col7", " ", connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(1) - null value
**/
   public void Var035() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col7.setString(1, null);
	       int count = col7.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col7", null, connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(1) - value too big
**/
   public void Var036() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
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
				&& (dt.getTransferSize() == transferSize), 
                                 "dt.getIndex()="+dt.getIndex()+" sb 1 "+
                                 "dt.getParameter()="+dt.getParameter()+" sb true "+
                                 "dt.getRead()="+dt.getRead()+" sb false "+
                                 "dt.getDataSize()="+dt.getDataSize()+" sb "+(value.length()*2)+" "+
                                 "dt.getTransferSize()="+dt.getTransferSize()+" sb 2 ");
	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");
	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(20) - value is just right
**/
   public void Var037() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col8.setString(1, "01234567890123456789");
	       int count = col8.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col8", "01234567890123456789", connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(20) - value is smaller than column
**/
   public void Var038() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col8.setString(1, "0123456789");
	       int count = col8.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col8", "0123456789          ", connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }


/**
Test:  wchar(20) - value is 1 char long
**/
   public void Var039() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col8.setString(1, "0");
	       int count = col8.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col8", "0                   ", connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(20) - value is empty string
**/
   public void Var040() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col8.setString(1, "");
	       int count = col8.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col8", "                    ", connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(20) - value is null
**/
   public void Var041() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col8.setString(1, null);
	       int count = col8.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col8", null, connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }


/**
Test:  wchar(20) - value too big
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
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
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
				&& (dt.getTransferSize() == transferSize), 
                                 "dt.getIndex()="+dt.getIndex()+" sb 1 "+
                                 "dt.getParameter()="+dt.getParameter()+" sb true "+
                                 "dt.getRead()="+dt.getRead()+" sb false "+
                                 "dt.getDataSize()="+dt.getDataSize()+" sb "+(value.length()*2)+" "+
                                 "dt.getTransferSize()="+dt.getTransferSize()+" sb 40 ");
	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");
	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(16361) - value is just right
**/
   public void Var043() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col9.setString(1, largeValue);
	       int count = col9.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col9", largeValue, connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(16361) - value is smaller than column
**/
   public void Var044() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col9.setString(1, "0123456789012345678901234567890123456789");
	       int count = col9.executeUpdate();
	       if (count == 1) {
		   StringBuffer expected = new StringBuffer("0123456789012345678901234567890123456789");
		   for (; 16361 - expected.length() > 0; ) {
		       expected.append(" ");
		   }
		   assertCondition(JDParmHelper.verifyString("col9", expected.toString(), connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       }
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }


/**
Test:  wchar(16361) - value is 1 char long
**/
   public void Var045() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col9.setString(1, "0");
	       int count = col9.executeUpdate();
	       if (count == 1) {
		   StringBuffer expected = new StringBuffer("0");
		   for (; 16361 - expected.length() > 0; ) {
		       expected.append(" ");
		   }
		   assertCondition(JDParmHelper.verifyString("col9", expected.toString(), connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       }
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(16361) - value is empty string
**/
   public void Var046() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col9.setString(1, "");
	       int count = col9.executeUpdate();
	       if (count == 1) {
		   StringBuffer expected = new StringBuffer();
		   for (int i = 0; i < 16361; i++) {
		       expected.append(" ");
		   }
		   assertCondition(JDParmHelper.verifyString("col9", expected.toString(), connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       }
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }



/**
Test:  wchar(16361) - value is null
**/
   public void Var047() {
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
	   try {
	       col9.setString(1, null);
	       int count = col9.executeUpdate();
	       if (count == 1)
		   assertCondition(JDParmHelper.verifyString("col9", null, connection, ""+JDParmTest.COLLECTION+".stringsGraphic1"));
	       else
		   failed ("invalid update count");

	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");

	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 

   }


/**
Test:  wchar(16361) - value too big
**/
   public void Var048() {
       String value = largeValue + "01234567890";
       
       int size = (value.length()*2);
       int transferSize = 32722;
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
       {
           size = (value.length());
           transferSize = 16361;
       }
       
       if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {	
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
				&& (dt.getTransferSize() == transferSize), 
                                 "dt.getIndex()="+dt.getIndex()+" sb 1 "+
                                 "dt.getParameter()="+dt.getParameter()+" sb true "+
                                 "dt.getRead()="+dt.getRead()+" sb false "+
                                 "dt.getDataSize()="+dt.getDataSize()+" sb "+(value.length()*2)+" "+
                                 "dt.getTransferSize()="+dt.getTransferSize()+" sb 32722 ");
	   } catch (SQLException e) {
	       failed (e, "Unexpected Exception");
	   } finally {
	       JDParmHelper.purgeStringsTable(connection, ""+JDParmTest.COLLECTION+".stringsGraphic1");
	   }
       } else {
	   notApplicable("V5R3 CCSID 1200/1208 variation"); 
       } 
   }

}
