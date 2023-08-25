///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSBatchTruncation.java
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
// File Name:    JDPSBatchTruncation.java
//
// Classes:      JDPSBatchTruncation
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.BinaryConverter;
import com.ibm.as400.access.Copyright;
import com.ibm.as400.access.Trace;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;



/**
Testcase JDPSBatchTruncation.
This test problem with mixed CCSID truncation.
Problem reported Nov 2018 via CPGASSG
input compression setting for block update. 
**/
public class JDPSBatchTruncation
extends JDTestcase {
  

      Connection connection_;
      String formatProperties=";time format=jis;date format=jis";
      String compressionProperties=";variable field compression=all;use block update=true";   
      String nocompressionProperties=";variable field compression=true";   
      Connection connectionNoCompress_;
      String characterTruncationProperties=";character truncation=none;variable field compression=all;use block update=true";   
      Connection connectionCharacterTruncation_; 
      

/**
Constructor.
**/
    public JDPSBatchTruncation (AS400 systemObject,
                      Hashtable namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
			      String password,
			      String powerUserID,
			      String powerPassword)
    {
        super (systemObject, "JDPSBatchTruncation",
               namesAndVars, runMode, fileOutputStream,
               password,powerUserID, powerPassword);
    }


    public JDPSBatchTruncation (AS400 systemObject,
		      String testname, 
                      Hashtable namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password,
                            String powerUserID,
                            String powerPassword)
    {
        super (systemObject, testname,
               namesAndVars, runMode, fileOutputStream,
               password,powerUserID, powerPassword);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        String url = baseURL_;
        connection_ = testDriver_.getConnection (
		url + formatProperties+compressionProperties,
		userId_, encryptedPassword_);

        connectionNoCompress_ = testDriver_.getConnection (
            url + formatProperties+nocompressionProperties,
            userId_, encryptedPassword_);

    
        connectionCharacterTruncation_ = testDriver_.getConnection (
            url + formatProperties+characterTruncationProperties,
            userId_, encryptedPassword_);

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        connection_.close ();
        connectionNoCompress_.close(); 
        connectionCharacterTruncation_.close(); 
    }



  /* Use batch insert to attempt to insert truncated data into a CCSID 5035 column */ 
  /* This ends up looping for toolbox before November 2018 */ 
    
  public void Var001() {
    testDefaultInsertTruncated("VARCHAR"); 
  }
  public void testDefaultInsertTruncated(String sqlType) {
    StringBuffer sb = new StringBuffer();
    sb.append(" -- Testcase added 11/8/2018 for CPS B6ASSG\n");
    boolean passed = true; 
    long fixDate = getDriverFixDate(); 
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
      if (fixDate < 20181108) {
        failed("Need toolbox driver after 20181108 to pass test fixDate="+fixDate); 
        return; 
      }
    }
    
    try {
      String sql; 
      String tablename = JDPSTest.COLLECTION +".JDPSBTN1"; 
      Statement stmt = connection_.createStatement(); 
      sql = "DROP TABLE "+tablename; 
      try {
        stmt.execute(sql); 
      } catch (Exception e) { 
        sb.append("Ignoring exception from "+sql); 
        printStackTraceToStringBuffer(e, sb); 
      }
      
      sql="create table "+tablename+"(c1 "+sqlType+"(15) CCSID 5035)";
      stmt.executeUpdate(sql);
      sql = "insert into "+tablename+" values(?)"; 
      PreparedStatement ps = connection_.prepareStatement(sql); 
      try { 
        ps.setString(1, "\u0031\u0036\u0035\u0031\u0030\u002D\u0038\u0031\u0034\u0032\u0030\u002D\u0030\u0030\u3042");
        ps.addBatch(); 
        ps.executeBatch();
      } catch (SQLException sqlex) { 
        String message = sqlex.toString(); 
        if ((message.indexOf("truncation") < 0) 
            && (message.indexOf("too long") < 0)
            && (message.indexOf("Length in varying-length, LOB, or XML host variable not valid") < 0)) {
          
          passed = false; 
          sb.append("Expected to see truncation but got "+message+"\n");
          printStackTraceToStringBuffer(sqlex, sb);
        }
      }

      
      
      sql = "DROP TABLE "+tablename; 
      stmt.execute(sql); 

      stmt.close();
      ps.close(); 
      
      assertCondition(passed, sb); 
    } catch (Exception e) {
      failed(e, sb); 
    }
  }

  
  
  
  /* Use batch insert to attempt to insert truncated data into a CCSID 5035 column */ 
  /* Use a non-compress connection which will work. */ 
    
  public void Var002() {
    testNocompressInsertTruncated("VARCHAR"); 
  }

  public void testNocompressInsertTruncated(String sqlType) {
    StringBuffer sb = new StringBuffer();
    sb.append(" -- Testcase added 11/8/2018 for CPS B6ASSG\n");
    boolean passed = true; 
    
    try {
      String sql; 
      String tablename = JDPSTest.COLLECTION +".JDPSBTN2"; 
      Statement stmt = connectionNoCompress_.createStatement(); 
      sql = "DROP TABLE "+tablename; 
      try {
        stmt.execute(sql); 
      } catch (Exception e) { 
        sb.append("Ignoring exception from "+sql); 
        printStackTraceToStringBuffer(e, sb); 
      }
      
      sql="create table "+tablename+"(c1 "+sqlType+"(15) CCSID 5035)";
      stmt.executeUpdate(sql);
      sql = "insert into "+tablename+" values(?)"; 
      PreparedStatement ps = connectionNoCompress_.prepareStatement(sql); 
      try { 
        ps.setString(1, "\u0031\u0036\u0035\u0031\u0030\u002D\u0038\u0031\u0034\u0032\u0030\u002D\u0030\u0030\u3042");
        ps.addBatch(); 
        ps.executeBatch();
      } catch (SQLException sqlex) { 
        String message = sqlex.toString(); 
        if ((message.indexOf("truncation") < 0) 
            && (message.indexOf("too long") < 0)
            && (message.indexOf("Length in varying-length, LOB, or XML host variable not valid") < 0)) {
          passed = false; 
          sb.append("Expected to see truncation but got "+message+"\n");
          printStackTraceToStringBuffer(sqlex, sb);
        }
      }

      
      
      sql = "DROP TABLE "+tablename; 
      stmt.execute(sql); 

      stmt.close();
      ps.close(); 
      
      assertCondition(passed, sb); 
    } catch (Exception e) {
      failed(e, sb); 
    }
  }

  /* Run the test with character truncation enabled */
  
  public void Var003() {
    testCharacterTruncationInsertTruncated("VARCHAR"); 
  }

  public void testCharacterTruncationInsertTruncated(String sqltype ) {
    if (checkToolbox()) {
    StringBuffer sb = new StringBuffer();
    sb.append(" -- Testcase added 11/8/2018 for CPS B6ASSG\n");
    boolean passed = true; 
    long fixDate = getDriverFixDate(); 
    if (sqltype.toUpperCase().equals("VARCHAR")) { 
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
      if (fixDate < 20181108) {
        failed("Need toolbox driver after 20181108 to pass test fixDate="+fixDate ); 
        return; 
      }
    }
    }
    try {
      String sql; 
      String tablename = JDPSTest.COLLECTION +".JDPSBTN3"; 
      Statement stmt = connectionCharacterTruncation_.createStatement(); 
      sql = "DROP TABLE "+tablename; 
      try {
        stmt.execute(sql); 
      } catch (Exception e) { 
        sb.append("Ignoring exception from "+sql); 
        printStackTraceToStringBuffer(e, sb); 
      }
      
      sql="create table "+tablename+"(c1 "+sqltype+"(15) CCSID 5035)";
      stmt.executeUpdate(sql);
      sql = "insert into "+tablename+" values(?)"; 
      PreparedStatement ps = connectionCharacterTruncation_.prepareStatement(sql); 
      ps.setString(1, "\u0031\u0036\u0035\u0031\u0030\u002D\u0038\u0031\u0034\u0032\u0030\u002D\u0030\u0030\u3042");
      ps.addBatch(); 
      ps.executeBatch();

      sql = "select * from "+tablename; 
      sb.append("Executing "+sql+"\n"); 
      ResultSet rs = stmt.executeQuery(sql); 
      rs.next(); 
      String value = rs.getString(1); 
      String expected = "\u0031\u0036\u0035\u0031\u0030\u002D\u0038\u0031\u0034\u0032\u0030\u002D\u0030\u0030"; 
      if (sqltype.equals("CHAR")) {
        expected = "\u0031\u0036\u0035\u0031\u0030\u002D\u0038\u0031\u0034\u0032\u0030\u002D\u0030\u0030\u0020"; 
      }
      if (!expected.equals(value)) { 
        passed =false; 
        sb.append("Expected "+expected+" got "+value); 
      }
      
      
      sql = "DROP TABLE "+tablename; 
      stmt.execute(sql); 

      stmt.close();
      ps.close(); 
      
      assertCondition(passed, sb); 
    } catch (Exception e) {
      failed(e, sb); 
    }
    }
  }



  public void Var004() {
    testDefaultInsertTruncated("CHAR"); 
  }

  public void Var005() {
    testDefaultInsertTruncated("CLOB"); 
  }
  
  public void Var006() {
    testNocompressInsertTruncated("CHAR"); 
  }
  public void Var007() {
    testNocompressInsertTruncated("CLOB"); 
  }
  

  public void Var008() {
    testCharacterTruncationInsertTruncated("CHAR"); 
  }

  public void Var009() {
    testCharacterTruncationInsertTruncated("CLOB"); 
  }

 
}


