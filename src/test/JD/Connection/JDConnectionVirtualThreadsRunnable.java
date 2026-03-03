///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionVirtualThreadsRunnable
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 2025-2025 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionVirtualThreadsRunnable.java
//
// Classes:      JDConnectionVirtualThreadsRunnable
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import test.JDTestDriver;
import test.Testcase;

/**
 * Testcase JDConnectionVirtualThreadsRunnable
 * 
 *  Runnable to do work on a connection. 
 **/
public class JDConnectionVirtualThreadsRunnable implements Runnable {


  
  private Connection connection_;
  public StringBuffer sb = new StringBuffer(); 
  public String threadName_ = null; 

  public JDConnectionVirtualThreadsRunnable(String threadName, JDTestDriver testDriver, String URL, String userId,
      char[] encryptedPassword) throws Exception {
    threadName_ = threadName;
    connection_ = testDriver.getConnection(URL, userId, encryptedPassword);
  }

  public void run() {
    Thread.currentThread().setName(threadName_);; 
    
    String sql = ""; 
    Statement stmt = null; 
      try { 
        stmt = connection_.createStatement(); 
        ResultSet rs = stmt.executeQuery("SELECT * FROM SYSIBM.SQLTABLES fetch first 1000 rows only"); 
        while (rs.next()) { 
          rs.getString(1); 
        }
        rs.close(); 

        rs = stmt.executeQuery("SELECT * FROM SYSIBM.SQLCOLUMNS fetch first 5000 rows only"); 
        while (rs.next()) { 
          rs.getString(1); 
        }
        rs.close(); 
     
    
      } catch (Exception e) { 
        sb.append("Exception encountered on sql="+sql+"\n"); 
        Testcase.printStackTraceToStringBuffer(e, sb); 
      } finally {
        if (stmt != null)
          try {
            stmt.close();
          } catch (SQLException e) {
          } 
        try {
          connection_.close();
        } catch (SQLException e) {
        } 
      }
    
    
  }

  public String getErrorMessage() {
    if (sb.length() == 0) { 
      return null; 
    } else {
      return sb.toString(); 
    }
  }  

  
}
