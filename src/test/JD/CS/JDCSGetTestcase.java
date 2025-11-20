///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetTestcase.java
//
// Classes:      JDCSGetTestcase
//
////////////////////////////////////////////////////////////////////////
// Base class for get testcases
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.io.FileOutputStream;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestcase;

public class JDCSGetTestcase extends JDTestcase {


  CallableStatement csTypes_;
  CallableStatement csTypesB_;
  CallableStatement csTypesNull_;

  CallableStatement csTypesTruncation_;

  /**
   * Constructor.
   **/
  public JDCSGetTestcase(AS400 systemObject, String testname,
      Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, testname, namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception  { 
    setup(null); 
  }
  
  protected void setup(String connectionProperties) throws Exception {
    String url = baseURL_ + ";errors=full";
    if (connectionProperties != null) { 
      url = url+";"+connectionProperties; 
    }
    connection_ = testDriver_.getConnection(url, userId_, encryptedPassword_);

    csTypes_ = JDSetupProcedure.prepare(connection_,
        JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
    JDSetupProcedure.register(csTypes_, JDSetupProcedure.STP_CSTYPESOUT,
        supportedFeatures_, getDriver());
    csTypes_.execute();

    csTypesTruncation_ = JDSetupProcedure.prepare(connection_,
        JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
    JDSetupProcedure.register(csTypesTruncation_,
        JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_, getDriver());
    csTypesTruncation_.execute();

    
    csTypesB_ = JDSetupProcedure.prepare(connection_,
        JDSetupProcedure.STP_CSTYPESOUTB, supportedFeatures_);
    JDSetupProcedure.register(csTypesB_, JDSetupProcedure.STP_CSTYPESOUTB,
        supportedFeatures_, getDriver());
    csTypesB_.execute();

    csTypesNull_ = JDSetupProcedure.prepare(connection_,
        JDSetupProcedure.STP_CSTYPESNULL, supportedFeatures_);
    JDSetupProcedure.register(csTypesNull_, JDSetupProcedure.STP_CSTYPESNULL,
        supportedFeatures_, getDriver());
    csTypesNull_.execute();

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    connection_.close();
    connection_ = null; 

  }
  public void getTestFailed(CallableStatement cstmt, String getMethodName, int parameter, String exceptionInfo, String info) { 
    StringBuffer sb = new StringBuffer(); 
    try {
      Object o = JDReflectionUtil.callMethod_O(cstmt, getMethodName, parameter);
      failed ("Didn't throw SQLException "+o);
   } catch (Exception e) {
     boolean passed = false; 
     sb.append(info); 
     String message = e.getMessage(); 
     if (message.indexOf(exceptionInfo) >= 0) { 
       passed = true; 
     } else {
       passed = false; 
       sb.append("Did not find '"+exceptionInfo+"' in '"+message+"'\n"); 
       printStackTraceToStringBuffer(e, sb);
     }
      assertCondition(passed, sb.toString());
   }
  }
  
  public void getTestFailed(CallableStatement cstmt, String getMethodName, String parameter, String exceptionInfo, String info) { 
    StringBuffer sb = new StringBuffer(); 
    try {
      Object o = JDReflectionUtil.callMethod_O(cstmt, getMethodName, parameter);
      failed ("Didn't throw SQLException "+o);
   } catch (Exception e) {
     boolean passed = false; 
     sb.append(info); 
     String message = e.getMessage(); 
     if (message.indexOf(exceptionInfo) >= 0) { 
       passed = true; 
     } else {
       passed = false; 
       sb.append("Did not find '"+exceptionInfo+"' in '"+message+"'\n"); 
       printStackTraceToStringBuffer(e, sb);
     }
      assertCondition(passed, sb.toString());
   }
  }
  
  
  public void getTestSuccessful(CallableStatement cstmt, String getMethodName,
      int column, String expectedValue, String info) {
    try {
      StringBuffer sb = new StringBuffer();
      String answer = "";
      boolean passedSet = false;
      boolean passed = false;
      if (getMethodName.equals("getBoolean")) {
        boolean p = cstmt.getBoolean(column);
        answer = "" + p;
      } else if (getMethodName.equals("getByte")) {
        answer = "" + cstmt.getByte(column);
      } else if (getMethodName.equals("getDouble")) {
        answer = "" + cstmt.getDouble(column);
      } else if (getMethodName.equals("getString")) {
        answer = "" + cstmt.getString(column);
      } else if (getMethodName.equals("getCharacterStream") ||
          getMethodName.equals("getNCharacterStream") ) {
        Reader reader = (Reader) JDReflectionUtil.callMethod_O(cstmt,
            getMethodName, column);
        passed = compare(reader, expectedValue, sb);
        passedSet = true;
      } else if (getMethodName.equals("getClob")) { 
        Clob c = cstmt.getClob(column); 
        passed = compare(c, expectedValue);
        passedSet=true; 
      } else {
        Object o = JDReflectionUtil.callMethod_O(cstmt, getMethodName, column);
        answer = "" + o;
      }
      if (!passedSet) {
        assertCondition(answer.equals(expectedValue),
            "Got " + answer + " sb " + expectedValue + info);
      } else {
        assertCondition(passed, sb.toString() + info);
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  
  /**
  Compares a Clob with a String.
  **/
     boolean compare (Clob i, String b)
     throws SQLException
     {
         if (i == null) {
       if (b == null) {
           return true; 
       } else {
           System.out.println("JDCSGetTestcase.compare():  clob is null"); 
           return false;
           
       } 
         } 
         String s = i.getSubString (1, (int) i.length ());
         boolean equals = s.equals (b); 
         if (!equals) {
         if (s.length() < 256 ) { 
           System.out.println(s);
         } else {
           System.out.println(s.substring(0,256)+"....");
         }
         if (b.length() < 256 ) { 
           System.out.println(b);
         } else {
           System.out.println(b.substring(0,256)+"....");
         }
         }
         return equals ;    // @B1C
     }


}
