///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionTrimCharFields
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 2025-2025 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionTrimCharFields.java
//
// Classes:      JDConnectionTrimCharFields
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDTestcase;
import java.util.Hashtable;
import java.util.Vector;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Testcase JDConnectionTrimCharFields Tests the virtual threads property of a
 * JDBC connection.
 **/
public class JDConnectionTrimCharFields extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "JDConnectionTrimCharFields";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.JDConnectionTest.main(newArgs);
  }

  
  /**
   * Constructor.
   **/
  public JDConnectionTrimCharFields(AS400 systemObject, Hashtable<String, Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,

      String password, String pwrUID, 
      String pwrPwd) { 
    super(systemObject, "JDConnectionTrimCharFields", namesAndVars, runMode, fileOutputStream, password, pwrUID,
        pwrPwd);

  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {


  } /* setup */

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void cleanup() throws Exception {
  

  }

  // Run some simple tests to verify that things are truncated as expected 
  public void Var001() {
    if (checkToolbox()) {
      String[][] tests = {
          {"SELECT CAST('ABC' AS CHAR(10)) FROM SYSIBM.SYSDUMMY1","ABC"},  
          {"SELECT CAST(' ABC ' AS CHAR(10)) FROM SYSIBM.SYSDUMMY1"," ABC"},  
          {"SELECT CAST(' ABC X' AS CHAR(6)) FROM SYSIBM.SYSDUMMY1"," ABC X"},  
          {"SELECT CAST(' ABC ' AS VARCHAR(10)) FROM SYSIBM.SYSDUMMY1"," ABC "},  
          {"SELECT CAST('ABC ' AS CHAR(100)) FROM SYSIBM.SYSDUMMY1","ABC"},  
          {"SELECT CAST(' ABC ' AS CLOB(10)) FROM SYSIBM.SYSDUMMY1"," ABC "},  
          
      };
      StringBuffer sb = new StringBuffer(); 
      boolean passed = true; 
      Connection c1  = null ; 
      try { 
        c1  = testDriver_.getConnection (baseURL_ +";trim char fields=true", userId_ , encryptedPassword_ );
        Statement s1 = c1.createStatement();
        for (int i = 0; i < tests.length; i++) { 
          String[] test = tests[i]; 
          String sql = test[0]; 
          String expected = test[1]; 
          sb.append("SQL="+sql+"\n"); 
          ResultSet rs = s1.executeQuery(sql); 
          if (rs.next()) {
            String value = rs.getString(1); 
            if (!expected.equals(value)) { 
              sb.append(" *** ERROR got '"+value+"' sb '"+expected+"'\n");
              passed = false; 
            }
          } else {
            sb.append(" *** ERROR no row returned from query \n"); 
            passed = false; 
          }
          rs.close(); 
        }
        s1.close(); 
        assertCondition(passed, sb); 
      } catch (Exception e) { 
        failed(e, sb); 
      } finally { 
        if (c1 != null)
          try {
            c1.close();
          } catch (SQLException e) {
            e.printStackTrace();
          } 
      }
    }
  }


}
