///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringCharHex.java
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
// File Name:    JDParmStringCharHex.java
//
// Classes:      JDParmStringCharHex
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDParmStringCharHex.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringCharHex
extends JDTestcase {



    // Private data.
    public Connection connection = null;
    public Statement s = null;
    public ResultSet rs = null;


/**
Constructor.
**/
    public JDParmStringCharHex (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringCharHex",
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

           s = connection.createStatement();

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

           s.executeUpdate("create table "+JDParmTest.COLLECTION+".strings (col1 char(10)) ");

           s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings values(x'00000000000000000000') ");
           s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings values(x'0000000000C100000000') ");
           s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings values(x'C1C1C1C1C100C1C1C1C1') ");
           s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings values(x'00C1C1C1C1C1C1C1C1C1') ");
           s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings values(x'C1C1C1C1C1C1C1C1C100') ");

           rs = s.executeQuery("select * from "+JDParmTest.COLLECTION+".strings");

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
Test:  char(10) - value is just right - all null chars.
**/
   public void Var001() {
      try {
          boolean success = true;
          rs.next();
          String sValue = rs.getString(1);
          byte[] value = sValue.getBytes();

          for (int i = 0; i < 10; i++) {
              if (value[i] != 0) {
                  success = false;
              }
          }

          assertCondition(success);

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      }
   }


/**
Test:  char(10) - value is just right - embedded real char
**/
   public void Var002() {
      try {
          boolean success = true;
          rs.next();
          String sValue = rs.getString(1);
          byte[] value = sValue.getBytes();

          for (int i = 0; i < 10; i++) {
              if (i == 5) {
                  if (value[i] != 'A') {
                      success = false;
                  }
              } else {
                  if (value[i] != 0) {
                      success = false;
                  }
              }
          }

          assertCondition(success);

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      }
   }



/**
Test:  char(10) - value is just right - embedded null char
**/
   public void Var003() {
      try {
          boolean success = true;
          rs.next();
          String sValue = rs.getString(1);
          byte[] value = sValue.getBytes();

          for (int i = 0; i < 10; i++) {
              if (i == 5) {
                  if (value[i] != 0) {
                      success = false;
                  }
              } else {
                  if (value[i] != 'A') {
                      success = false;
                  }
              }
          }

          assertCondition(success);

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      }
   }



/**
Test:  char(10) - value is just right - First char null.
**/
   public void Var004() {
      try {
          boolean success = true;
          rs.next();
          String sValue = rs.getString(1);
          byte[] value = sValue.getBytes();

          for (int i = 0; i < 10; i++) {
              if (i == 0) {
                  if (value[i] != 0) {
                      success = false;
                  }
              } else {
                  if (value[i] != 'A') {
                      success = false;
                  }
              }
          }

          assertCondition(success);

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      }
   }




/**
Test:  char(10) - value is just right - last char null
**/
   public void Var005() {
      try {
          boolean success = true;
          rs.next();
          String sValue = rs.getString(1);
          byte[] value = sValue.getBytes();

          for (int i = 0; i < 10; i++) {
              if (i == 9) {
                  if (value[i] != 0) {
                      success = false;
                  }
              } else {
                  if (value[i] != 'A') {
                      success = false;
                  }
              }
          }

          assertCondition(success);

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      }
   }

}



