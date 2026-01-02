///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringVarcharHex.java
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
// File Name:    JDParmStringVarcharHex.java
//
// Classes:      JDParmStringVarcharHex
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDParmStringVarcharHex.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringVarcharHex
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDParmStringVarcharHex";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDParmTest.main(newArgs); 
   }



    // Private data.
    public Connection connection = null;
    public Statement s = null;
    public ResultSet rs = null;


/**
Constructor.
**/
    public JDParmStringVarcharHex (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringVarcharHex",
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

           s = connection.createStatement();

           try {
              s.executeUpdate("drop table "+JDParmTest.COLLECTION+".strings2");
          } catch (SQLException e) {
	       JDParmHelper.handleDropException(e,output_);
          }

          s.executeUpdate("create table "+JDParmTest.COLLECTION+".strings2 (col1 varchar(10))");
          s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings2 values(x'00000000000000000000') ");
          s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings2 values(x'0000000000C100000000') ");
          s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings2 values(x'C1C1C1C1C100C1C1C1C1') ");
          s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings2 values(x'00C1C1C1C1C1C1C1C1C1') ");
          s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings2 values(x'C1C1C1C1C1C1C1C1C100') ");
          s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings2 values(x'0000000000') ");
          s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings2 values(x'0000C10000') ");
          s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings2 values(x'C1C100C1C1') ");
          s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings2 values(x'00C1C1C1C1') ");
          s.executeUpdate("insert into "+JDParmTest.COLLECTION+".strings2 values(x'C1C1C1C100') ");

           rs = s.executeQuery("select * from "+JDParmTest.COLLECTION+".strings2");

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

          if (value.length != 10) 
              success = false;

          
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

          if (value.length != 10) 
              success = false;

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

          if (value.length != 10) 
              success = false;

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

          if (value.length != 10) 
              success = false;

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

          if (value.length != 10) 
              success = false;

          assertCondition(success);

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      }
   }




/**
Test:  char(10) - value is shorter than column - all null chars.
**/
   public void Var006() {
      try {
          boolean success = true;
          rs.next();
          String sValue = rs.getString(1);
          byte[] value = sValue.getBytes();

          for (int i = 0; i < 5; i++) {
              if (value[i] != 0) {
                  success = false;
              }
          }

          if (value.length != 5) 
              success = false;
          
          assertCondition(success);

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      }
   }



/**
Test:  char(10) - value is shorter than column - embedded real char
**/
   public void Var007() {
      try {
          boolean success = true;
          rs.next();
          String sValue = rs.getString(1);
          byte[] value = sValue.getBytes();

          for (int i = 0; i < 5; i++) {
              if (i == 2) {
                  if (value[i] != 'A') {
                      success = false;
                  }
              } else {
                  if (value[i] != 0) {
                      success = false;
                  }
              }
          }

          if (value.length != 5) 
              success = false;

          assertCondition(success);

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      }
   }



/**
Test:  char(10) - value is shorter than column - embedded null char
**/
   public void Var008() {
      try {
          boolean success = true;
          rs.next();
          String sValue = rs.getString(1);
          byte[] value = sValue.getBytes();

          for (int i = 0; i < 5; i++) {
              if (i == 2) {
                  if (value[i] != 0) {
                      success = false;
                  }
              } else {
                  if (value[i] != 'A') {
                      success = false;
                  }
              }
          }

          if (value.length != 5) 
              success = false;

          assertCondition(success);

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      }
   }



/**
Test:  char(10) - value is just right - First char null.
**/
   public void Var009() {
      try {
          boolean success = true;
          rs.next();
          String sValue = rs.getString(1);
          byte[] value = sValue.getBytes();

          for (int i = 0; i < 5; i++) {
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

          if (value.length != 5) 
              success = false;

          assertCondition(success);

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      }
   }




/**
Test:  char(10) - value is just right - last char null
**/
   public void Var010() {
      try {
          boolean success = true;
          rs.next();
          String sValue = rs.getString(1);
          byte[] value = sValue.getBytes();

          for (int i = 0; i < 5; i++) {
              if (i == 4) {
                  if (value[i] != 0) {
                      success = false;
                  }
              } else {
                  if (value[i] != 'A') {
                      success = false;
                  }
              }
          }

          if (value.length != 5) 
              success = false;

          assertCondition(success);

      } catch (SQLException e) {
         failed (e, "Unexpected Exception");
         
      }
   }

}



