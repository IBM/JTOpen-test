///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringClob.java
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
// File Name:    JDParmStringClob.java
//
// Classes:      JDParmStringClob
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
Testcase JDParmStringClob.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringClob
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDParmStringClob";
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
    public String largeValue = "";
    public String hugeValue = "";


/**
Constructor.
**/
    public JDParmStringClob (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringClob",
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
                           "(col1 clob(1), " +
                           "col2 clob(20), " + 
                           "col3 clob (32714), " +
                           "col4 clob (2000000) )");


           s.close();


           col1 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col1) values(?)");
           col2 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col2) values(?)");
           col3 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col3) values(?)");
           col4 = connection.prepareStatement("insert into "+JDParmTest.COLLECTION+".strings (col4) values(?)");

           String shortValue = "abcdefghijklmnopqrstuvwxy";

           StringBuffer largeValueSB = new StringBuffer(shortValue);

           while (largeValueSB.length() < 2000000) {
               largeValueSB.append(shortValue);
           }

	   largeValue = largeValueSB.substring(0, 32714);
	   hugeValue = largeValueSB.substring(0, 2000000);


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
Test:  clob(1) - value is just right
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
Test:  clob(1) - empty string
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
Test:  clob(1) - null value
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
Test:  clob(1) - value too big
**/
    public void Var004() {
        String value = "TOO BIG";
        int overflow = value.length();
        int transferSize = 1;
        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
        {
            overflow = 6; //7-1=6
            transferSize = 0; //not transfered yet
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
                   && (dt.getDataSize() == overflow)
                   && (dt.getTransferSize() == transferSize), dt.getIndex()+" SB 1 \n "+dt.getParameter()+" SB  true \n" +dt.getRead()+" SB false \n"+ dt.getDataSize()+" = "+overflow+"\n "+dt.getTransferSize()+" SB 1 \n");
        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
        } finally {
           JDParmHelper.purgeStringsTable(connection);
        }
    }



/**
Test:  clob(20) - value is just right
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
Test:  clob(20) - value is smaller than column
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
Test:  clob(20) - value is 1 char long
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
Test:  clob(20) - value is empty string
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
Test:  clob(20) - value is null
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
Test:  clob(20) - value too big
**/
    public void Var010() {
        String value = "012345678901234567890123";
        
        int overflow = value.length();
        int transferSize = 20;
        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
        {
            overflow = 4;  
            transferSize = 0; //not transfered yet
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
                   && (dt.getDataSize() == overflow)
                   && (dt.getTransferSize() == transferSize),dt.getIndex()+" SB 1 \n "+dt.getParameter()+" SB  true \n" +dt.getRead()+" SB false \n"+ overflow +" = "+value.length()+"\n "+dt.getTransferSize()+" SB 20 \n");
        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
        } finally {
           JDParmHelper.purgeStringsTable(connection);
        }
    }



/**
Test:  clob(32714) - value is just right
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
Test:  clob(32714) - value is smaller than column
**/
    public void Var012() {
        try {
            col3.setString(1, "0123456789012345678901234567890123456789");
            int count = col3.executeUpdate();
            if (count == 1) {
                assertCondition(JDParmHelper.verifyString("col3", "0123456789012345678901234567890123456789", connection));
            } else
                failed ("invalid update count");

        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
            
        } finally {
            JDParmHelper.purgeStringsTable(connection);
        }
    }


/**
Test:  clob(32714) - value is 1 char long
**/
    public void Var013() {
        try {
            col3.setString(1, "0");
            int count = col3.executeUpdate();
            if (count == 1) {
                assertCondition(JDParmHelper.verifyString("col3", "0", connection));
            } else
                failed ("invalid update count");

        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
            
        } finally {
            JDParmHelper.purgeStringsTable(connection);
        }
    }



/**
Test:  clob(32714) - value is empty string
**/
    public void Var014() {
        try {
            col3.setString(1, "");
            int count = col3.executeUpdate();
            if (count == 1) {
                assertCondition(JDParmHelper.verifyString("col3", "", connection));
            } else
                failed ("invalid update count");

        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
            
        } finally {
            JDParmHelper.purgeStringsTable(connection);
        }
    }



/**
Test:  clob(32714) - value is null
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
Test:  clob(32714) - value too big
**/
    public void Var016() {
        String value = largeValue + "01234567890";
       
        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
        {
           notApplicable();
           return;
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
                   && (dt.getDataSize() == value.length())
                   && (dt.getTransferSize() == 32714),dt.getIndex()+" SB 1 \n "+dt.getParameter()+" SB  true \n" +dt.getRead()+" SB false \n"+ dt.getDataSize()+" = "+value.length()+"\n "+dt.getTransferSize()+" SB 32714 \n");
        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
        } finally {
           JDParmHelper.purgeStringsTable(connection);
        }
    }



/**
Test:  clob(2000000) - value is just right
**/
    public void Var017() {
        try {
            col4.setString(1, hugeValue);
            int count = col4.executeUpdate();
            if (count == 1)
                assertCondition(JDParmHelper.verifyString("col4", hugeValue, connection));
            else
                failed ("invalid update count");

        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
            
        } finally {
            JDParmHelper.purgeStringsTable(connection);
        }
    }



/**
Test:  clob(2000000) - value is smaller than column
**/
    public void Var018() {
        try {
            col4.setString(1, "0123456789012345678901234567890123456789");
            int count = col4.executeUpdate();
            if (count == 1) {
                assertCondition(JDParmHelper.verifyString("col4", "0123456789012345678901234567890123456789", connection));
            } else
                failed ("invalid update count");

        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
            
        } finally {
            JDParmHelper.purgeStringsTable(connection);
        }
    }


/**
Test:  clob(2000000) - value is 1 char long
**/
    public void Var019() {
        try {
            col4.setString(1, "0");
            int count = col4.executeUpdate();
            if (count == 1) {
                assertCondition(JDParmHelper.verifyString("col4", "0", connection));
            } else
                failed ("invalid update count");

        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
            
        } finally {
            JDParmHelper.purgeStringsTable(connection);
        }
    }



/**
Test:  clob(2000000) - value is empty string
**/
    public void Var020() {
        try {
            col4.setString(1, "");
            int count = col4.executeUpdate();
            if (count == 1) {
                assertCondition(JDParmHelper.verifyString("col4", "", connection));
            } else
                failed ("invalid update count");

        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
            
        } finally {
            JDParmHelper.purgeStringsTable(connection);
        }
    }



/**
Test:  clob(2000000) - value is null
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
Test:  clob(2000000) - value too big
**/
    public void Var022() {
        String value = hugeValue + "01234567890";
        try {
            col4.setString(1, value);
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
                   && (dt.getTransferSize() == 2000000),dt.getIndex()+" SB 1 \n "+dt.getParameter()+" SB  true \n" +dt.getRead()+" SB false \n"+ dt.getDataSize()+" = "+value.length()+"\n "+dt.getTransferSize()+" SB 2000000 \n");
        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
        } finally {
           JDParmHelper.purgeStringsTable(connection);
        }
    }

}



