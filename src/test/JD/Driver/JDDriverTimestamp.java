///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDriverTimestamp.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.Driver;

import com.ibm.as400.access.AS400;

import test.JDDriverTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;
import java.util.TimeZone;



/**
Testcase JDDriverTimestamp.  This tests the new
Timestamp class that is shipped for V7R2 for the
toolbox and native drivers.

In particular, the interaction with the new JDBC methods is tested.

**/
public class JDDriverTimestamp
extends JDTestcase
{
  public static void main(String args[]) throws Exception { 
    // Note:  reflection is used to get the classname, so this can be pasted easily into other Testcase classes
    String[] newArgs = new String[args.length+2];
    newArgs[0] = "-tc"; 
    newArgs[1] = new Object() { }.getClass().getEnclosingClass().getSimpleName();
    for (int i = 0; i < args.length; i++) { 
      newArgs[2+i]=args[i]; 
    }
    JDDriverTest.main(newArgs); 
  }

    private String timestampClassName;
    String localDateTimeClassName = "java.time.LocalDateTime"; 
    String instantClassName = "java.time.Instant"; 


    /**
    Constructor.
    **/
    public JDDriverTimestamp (AS400 systemObject,
                         Hashtable<String,Vector<String>> namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         String password,
                         String powerUserID,
                         String powerPassword)
    {
        super (systemObject, "JDDriverTimestamp",
               namesAndVars, runMode, fileOutputStream, 
               password);

        systemObject_ = systemObject;

    }



    /**
    Performs setup needed before running variations.

    @exception Exception If an exception occurs.
    **/
  protected void setup() throws Exception {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
        timestampClassName = "com.ibm.db2.jdbc.app.DB2JDBCTimestamp";
      } else {
        timestampClassName = null;
      }
    } else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      timestampClassName = "com.ibm.as400.access.AS400JDBCTimestamp";
    } else {
      timestampClassName = "com.ibm.db2.jdbc.app.DB2JDBCTimestamp";
    }
  }


    /**
    static Timestamp.valueOf(LocalDateTime) method 
    **/
  public void Var001() {
     
    if ( checkDriverTimstampSupport() && checkJdbc42()) {
      try {
        String expected = "2014-02-14 11:22:33.0";  
        Object localDateTime = JDReflectionUtil.callStaticMethod_O(localDateTimeClassName, "of", 2014, 2, 14, 11, 22, 33); 
        
        Object timestamp = JDReflectionUtil.callStaticMethod_O(timestampClassName, "valueOf",
            localDateTime);

        String outString = timestamp.toString(); 
        assertCondition(expected.equals(outString), "\ngot "+outString+
                                                    "\nsb  "+expected);
        
        
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


  
/**
  LocalDateTime toLocalDateTime()
  **/
  public void Var002() {
    if (checkDriverTimstampSupport() && checkJdbc42()) {
      try {
        String input  =   "2014-02-14 11:22:33.123456789012";
        String expected = "2014-02-14T11:22:33.123456789";
        
        Object timestamp = JDReflectionUtil.callStaticMethod_O(timestampClassName, "valueOf",
            input);
        
        Object localDateTime = JDReflectionUtil.callMethod_O(timestamp,"toLocalDateTime");
        
        String outString = localDateTime.toString(); 
        assertCondition(expected.equals(outString), "\ngot "+outString+
                                                    "\nsb  "+expected);
        
        
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }
  
  /**
  LocalDateTime toLocalDateTime()
  **/
  public void Var003() {
    if (checkDriverTimstampSupport() && checkJdbc42()) {
      try {
        String input  =   "2014-02-14 11:22:33.12345678";
        String expected = "2014-02-14T11:22:33.123456780";
        
        Object timestamp = JDReflectionUtil.callStaticMethod_O(timestampClassName, "valueOf",
            input);
        
        Object localDateTime = JDReflectionUtil.callMethod_O(timestamp,"toLocalDateTime");
        
        String outString = localDateTime.toString(); 
        assertCondition(expected.equals(outString), "\ngot "+outString+
                                                    "\nsb  "+expected);
        
        
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }
  
  /**
  LocalDateTime toLocalDateTime()
  **/
  public void Var004() {
    if (checkDriverTimstampSupport() && checkJdbc42()) {
      try {
        String input  =   "2014-02-14 11:22:33.0";
        String expected = "2014-02-14T11:22:33";
        
        Object timestamp = JDReflectionUtil.callStaticMethod_O(timestampClassName, "valueOf",
            input);
        
        Object localDateTime = JDReflectionUtil.callMethod_O(timestamp,"toLocalDateTime");
        
        String outString = localDateTime.toString(); 
        assertCondition(expected.equals(outString), "\ngot "+outString+
                                                    "\nsb  "+expected);
        
        
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }
  
  
  /**
   * test static Timestamp from(Instant instant)
   **/
  public void Var005() {
    if (checkDriverTimstampSupport() && checkJdbc42()) {
      try {

        String expected;
        TimeZone timeZone = TimeZone.getDefault();
        int offset = timeZone.getRawOffset();
        switch (offset) {
        case 0:
          expected = "2014-02-14 12:22:33.0";
          break;
        case -6 * 3600000:  /* CST */ 
          expected = "2014-02-14 06:22:33.0";
          break;
        default:
          throw new Exception("Testcase fix needed for timezone " + timeZone
              + " offset=" + offset);
        }

        String input = "2014-02-14T12:22:33.00Z";

        Object instant = JDReflectionUtil.callStaticMethod_O(instantClassName, "parse", (Object) input); 
        
        Object timestamp = JDReflectionUtil.callStaticMethod_O(timestampClassName, "from",
            instant);

        String outString = timestamp.toString(); 
        assertCondition(expected.equals(outString), "\ngot "+outString+
                                                    "\nsb  "+expected);
        
        
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  
  
  
/**
 * test Instant toInstant()
 */

  public void Var006() {
    if (checkDriverTimstampSupport() && checkJdbc42()) {
      try {
        String input  =   "2014-02-14 12:22:33.123456789012";
        String expected;
        TimeZone timeZone = TimeZone.getDefault();
        int offset = timeZone.getRawOffset();
        switch (offset) {
        case 0:
          expected = "2014-02-14T12:22:33.123456789Z";
          break;
        case -6 * 3600000:  /* CST */ 
          expected = "2014-02-14T18:22:33.123456789Z";
          break;
        default:
          throw new Exception("Testcase fix needed for timezone " + timeZone
              + " offset=" + offset);
        }
        
        Object timestamp = JDReflectionUtil.callStaticMethod_O(timestampClassName, "valueOf",
            input);
        
        Object instant = JDReflectionUtil.callMethod_O(timestamp,"toInstant");
        
        String outString = instant.toString(); 
        assertCondition(expected.equals(outString), "\ngot "+outString+
                                                    "\nsb  "+expected);
        
        
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }



private boolean checkDriverTimstampSupport() {
  if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
      getRelease() <= JDTestDriver.RELEASE_V7R1M0) { 
    notApplicable("Extended timestamp not available for native driver before V7R2"); 
    return false; 
  }
  return true;
}
  
  
  
  

}



