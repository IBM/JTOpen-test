/////// ////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionBlockSize.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2025 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDTestcase;

/**
 * Testcase JDConnectionBlocksize. This tests the following methods of the JDBC
 * Connection class:
 * 
 * <ul>
 * <li>block size property
 * <li>DriverManager.getConnection()
 * </ul>
 * 
 * 
 *  
 *  
 **/
public class JDConnectionBlockSize extends JDTestcase {

  StringBuffer sb = new StringBuffer();

  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "JDConnectionBlockSize";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.JDConnectionTest.main(newArgs);
  }

  /**
   * Constructor.
   **/
  public JDConnectionBlockSize(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, String password) {
    super(systemObject, "JDConnectionBlockSize", namesAndVars, runMode, fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void cleanup() throws Exception {
   }

  public void testSize(int size) { 
    if (checkToolbox()) {
      try {
        boolean passed = true;
        sb.setLength(0);
        
        String testURL = baseURL_ + ";block size="+size;
        sb.append("\nConnecting with URL=" + testURL);
        Connection c = testDriver_.getConnection(testURL, userId_, encryptedPassword_);
        Statement s = c.createStatement();
        long startTime = System.currentTimeMillis(); 
        int count = 0; 
        ResultSet rs = s.executeQuery("select * from qsys2.systables");
        while(rs.next()) {
          count++; 
        }
        long endTime = System.currentTimeMillis(); 
        double seconds = (endTime - startTime) / 1000.0; 
        output_.println("URL="+testURL+" retrieved "+count+" rows in "+seconds+" seconds"); 
        rs.close();
        s.close();
        c.close();
        
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }
  
 /**
   * verify that size 512 works 
   **/
  public void Var001() { testSize(512);  }
  /**
   * test other sizes  
   **/
  public void Var002() { testSize(1024); }
  public void Var003() { testSize(4096); }
  public void Var004() { testSize(8192); }
  public void Var005() { testSize(16000); }
  
  
}
