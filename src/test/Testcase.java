///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  Testcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties; 
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.TimeZone;
import java.util.Arrays;

import javax.transaction.xa.XAException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobLog;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.ObjectLockListEntry;
import com.ibm.as400.access.QueuedMessage;
import com.ibm.as400.access.ReturnCodeException;

import test.JD.JDTestUtilities;

import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.BinaryConverter;
import com.ibm.as400.access.DateTimeConverter;

/**
 * This class represents a testcase.
 **/
public abstract class Testcase {
  // In JDK1.7, the veto behavior changed
  public static boolean isAtLeastJDK17 = false;
  public static boolean isAtLeastJDK18 = false; 
  // Values for run mode.
  public static final int BOTH = 0;
  public static final int UNATTENDED = 1;
  public static final int ATTENDED = 2;

  // Values for connection type.
  public static final int CONN_DEFAULT = 0;
  public static final int CONN_POOLED = 1;
  public static final int CONN_XA = 2;

  // Possible outcomes for variations.
  private static final int FAILED = 0;
  private static final int NOT_APPLICABLE = 1;
  public static final int NOT_ATTEMPTED = 2;
  private static final int SUCCEEDED = 3;

  // Headings for results output.
  private static final String FAILED_HEADING = "FAILED  ";
  // Use extra spaces for name heading so results display nicely
  private static final String NAME_HEADING = "NAME                                    ";
  private static final String NOT_APPLICABLE_HEADING = "NOT APPL  ";
  private static final String NOT_ATTEMPTED_HEADING = "NOT ATT  ";
  private static final String SUCCEEDED_HEADING = "SUCCEEDED  ";
  private static final String TIME_TO_RUN_HEADING = "TIME(S)";

  private static final String CHECKDIR = "/tmp/toolboxLongRunning";

  // For NLS testing.
  private static ResourceBundle resources_;

  public static StringBuffer getSysValTimeStringBuffer = new StringBuffer();

  protected boolean endIfSetupFails_ = true;
  protected boolean isApplet_;
  protected String name_ = null; // Testcase name.
  protected String systemName_ = null;
  private int systemVRM_ = 0;
  protected int release_;
  protected String userId_ = null;
  /* At the testcase level, the password should be passed as a .txt file from */
  /* which the password will be read to get the encrypted password */
  /* In the constructors, a string password will continue to be passed */ 
  protected char[] encryptedPassword_ = null ;
  protected String proxy_ = "";
  protected int runMode_ = BOTH;
  protected AS400 systemObject_ = null;
  protected AS400 pwrSys_ = null; // Access thru a SECADM uid/pwd.
  protected AS400 sameSys_ = null; // system object that shares the same id as
                                   // the current user id
  protected String pwrSysUserID_ = null;
  protected char[] pwrSysEncryptedPassword_ = null;
  protected Vector<String> variationsToRun_ = null;
  protected boolean mustUseSockets_ = false;
  protected boolean isNative_ = false;
  protected boolean isLocal_ = false;
  protected boolean onAS400_ = TestDriver.onAS400_;
  String asp_ = null;

  boolean mfaInitialized = false; 
  
  Object googleAuthenticator_ = null; 
  protected String mfaSecret_; 
  protected String mfaUserid_;
  protected char[] mfaEncryptedPassword_; 
  protected char[] mfaFactor_;
  protected int    mfaIntervalSeconds_; 
  // Any output should be written here.
  protected PrintWriter output_;

  protected boolean interactive_ = false;;

  private int currentVariation_ = 1; // Current variation number.
  protected int totalVariations_ = 0; // Total # of variations in testcase.
  private int failures_ = 0; // # of failed variations.
  private int notApplicables_ = 0; // # of variations that don't apply.
  private int successes_ = 0; // # of successful variations.
  private double timeToRun_ = 0.0; // Time it took to run the testcase.

  int seed = 0; // Seed for random selection

  static int failureSleepSeconds = 0;

  protected String testLib_; 
  protected TestDriverI baseTestDriver_; 

  private long variationStartTime = System.currentTimeMillis(); // Time when
                                                                // variation
                                                                // started
  protected boolean skipCleanup;
  private String lockDtaaraName_;

  protected static boolean debug = false;
  static { 
    if (System.getProperty("test.Testcase.debug") != null) { 
      debug = true; 
    }
  }
  // Constant used in stringToBytes()
  // Note that 0x11 is "undefined".
  private static final byte[] b1_ = { 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
      0x07, 0x08, 0x09, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x0A, 0x0B,
      0x0C, 0x0D, 0x0E, 0x0F, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11,
      0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11 };

  static {

    int jdk = JVMInfo.getJDK();

    if (jdk >= JVMInfo.JDK_17)
      isAtLeastJDK17 = true;

    if (jdk >= JVMInfo.JDK_18)
      isAtLeastJDK18 = true;


    String failureSleepSecondsString = System
        .getProperty("failureSleepSeconds");
    if (failureSleepSecondsString != null) {
      failureSleepSeconds = Integer.parseInt(failureSleepSecondsString);
    }
  }

  protected static final Package TOOLBOX_PACKAGE = Package
      .getPackage("com.ibm.as400.access");

  protected Testcase() {
  }

  /**
   * Constructs a Testcase object.
   * 
   * @param system
   *          The iSeries server.
   * @param name
   *          The testcase name.
   * @param namesAndVars
   *          The list of variations to be run.
   * @param runMode
   *          Which testcases to run (attended, unattended, both).
   * @param fileOutputStream
   *          The output stream to which to write the results.
   **/
  protected Testcase(AS400 system, String name, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream) {
    this(system, name,  namesAndVars.get(name), runMode,
        fileOutputStream);
  }

  /**
   * Constructs a Testcase object.
   * 
   * @param system
   *          The iSeries server.
   * @param name
   *          The testcase name.
   * @param namesAndVars
   *          The list of variations to be run.
   * @param runMode
   *          Which testcases to run (attended, unattended, both).
   * @param fileOutputStream
   *          The output stream to which to write the results.
   * @param password
   *          The user profile password.
   **/
  protected Testcase(AS400 system, String name, Hashtable<String, Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    this(system, name,  namesAndVars.get(name), runMode,
        fileOutputStream,  password);
  }

  /**
   * Constructs a Testcase object.
   * 
   * @param system
   *          The iSeries server.
   * @param name
   *          The testcase name.
   * @param namesAndVars
   *          The list of variations to be run.
   * @param runMode
   *          Which testcases to run (attended, unattended, both).
   * @param fileOutputStream
   *          The output stream to which to write the results.
   * @param password
   *          The user profile password.
   * @param pwrSys
   *          The iSeries server, accessed through a SECADM user profile.
   * @param pwrSysPwd
   *          The power user password.
   **/
  public Testcase(AS400 system, String name, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream,  
      String password, AS400 pwrSys, String pwrSysPwd) {
    this(system, name,  namesAndVars.get(name), runMode,
        fileOutputStream,  password);
    pwrSysEncryptedPassword_ = PasswordVault.getEncryptedPassword(pwrSysPwd);
    pwrSys_ = pwrSys;
    if (pwrSys_ != null) {
      pwrSysUserID_ = pwrSys_.getUserId();
      if (pwrSysUserID_.equalsIgnoreCase(System.getProperty("user.name"))) {
        sameSys_ = pwrSys_;
      }
    }
  }

  /**
   * Constructs a Testcase object.
   * 
   * @param system
   *          The iSeries server.
   * @param name
   *          The testcase name.
   * @param namesAndVars
   *          The list of variations to be run.
   * @param runMode
   *          Which testcases to run (attended, unattended, both).
   * @param fileOutputStream
   *          The output stream to which to write the results.
   * @param pwrSys
   *          The iSeries server, accessed through a SECADM user profile.
   **/
  protected Testcase(AS400 system, String name, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      AS400 pwrSys) {
    this(system, name,  namesAndVars.get(name), runMode,
        fileOutputStream);
    pwrSys_ = pwrSys;
    if (pwrSys_ != null) {
      pwrSysUserID_ = pwrSys_.getUserId();
    }
  }

  /**
   * Constructs a Testcase object.
   * 
   * @param system
   *          The iSeries server.
   * @param name
   *          The testcase name.
   * @param variationsToRun
   *          The list of variations to be run.
   * @param runMode
   *          Which testcases to run (attended, unattended, both).
   * @param fileOutputStream
   *          The output stream to which to write the results.
   **/
  protected Testcase(AS400 system, String name, Vector<String> variationsToRun,
      int runMode, FileOutputStream fileOutputStream) {
    this(system, name, -1, variationsToRun, runMode, fileOutputStream,
         null);
    totalVariations_ = countVariations();
  }

  /**
   * Constructs a Testcase object.
   * 
   * @param system
   *          The iSeries server.
   * @param name
   *          The testcase name.
   * @param variationsToRun
   *          The list of variations to be run.
   * @param runMode
   *          Which testcases to run (attended, unattended, both).
   * @param fileOutputStream
   *          The output stream to which to write the results.
   * @param password
   *          The user profile password.
   **/
  protected Testcase(AS400 system, String name, Vector<String> variationsToRun,
      int runMode, FileOutputStream fileOutputStream,  
      String password) {
    this(system, name, -1, variationsToRun, runMode, fileOutputStream,
         password);
    totalVariations_ = countVariations();
  }

  /**
   * Constructs a Testcase object.
   * 
   * @param system
   *          The iSeries server.
   * @param name
   *          The testcase name.
   * @param totalVariations
   *          The total number of variations in this testcase.
   * @param variationsToRun
   *          The list of variations to be run.
   * @param runMode
   *          Which testcases to run (attended, unattended, both).
   * @param fileOutputStream
   *          The output stream to which to write the results.
   **/
  protected Testcase(AS400 system, String name, int totalVariations,
      Vector<String> variationsToRun, int runMode, FileOutputStream fileOutputStream) {
    this(system, name, totalVariations, variationsToRun, runMode,
        fileOutputStream,  null);
  }

  /**
   * Constructs a Testcase object.
   * 
   * @param system
   *          The iSeries server.
   * @param name
   *          The testcase name.
   * @param totalVariations
   *          The total number of variations in this testcase.
   * @param variationsToRun
   *          The list of variations to be run.
   * @param runMode
   *          Which testcases to run (attended, unattended, both).
   * @param fileOutputStream
   *          The output stream to which to write the results.
   * @param password
   *          The user profile password.
   **/
  protected Testcase(AS400 system, String name, int totalVariations,
      Vector<String> variationsToRun, int runMode, FileOutputStream fileOutputStream,
      String password) {
    this(system, name, totalVariations, variationsToRun, runMode,
        fileOutputStream,  password, null);
  }

  /**
   * Constructs a Testcase object.
   * 
   * @param system
   *          The iSeries server.
   * @param name
   *          The testcase name.
   * @param totalVariations
   *          The total number of variations in this testcase.
   * @param variationsToRun
   *          The list of variations to be run.
   * @param runMode
   *          Which testcases to run (attended, unattended, both).
   * @param fileOutputStream
   *          The output stream to which to write the results.
   * @param password
   *          The user profile password.
   * @param pwrSys
   *          The iSeries server, accessed through a SECADM user profile.
   **/
  protected Testcase(AS400 system, String name, int totalVariations,
      Vector<String> variationsToRun, int runMode, FileOutputStream fileOutputStream,
       String password, AS400 pwrSys) {
    initializeFields(system, name, totalVariations, variationsToRun, runMode,
        fileOutputStream,  password);
    pwrSys_ = pwrSys;
    if (pwrSys_ != null) {
      pwrSysUserID_ = pwrSys_.getUserId();
    }

    String userName = System.getProperty("user.name");

    if (systemObject_.getUserId().equalsIgnoreCase(userName)) {
      sameSys_ = systemObject_;
    } else {
      if (pwrSys_ != null) {

        if (pwrSysUserID_.equalsIgnoreCase(userName)) {
          sameSys_ = pwrSys_;
        }
      }
    }

  }

  /**
   * Constructs a Testcase object.
   * 
   * @param system
   *          The iSeries server.
   * @param name
   *          The testcase name.
   * @param totalVariations
   *          The total number of variations in this testcase.
   * @param variationsToRun
   *          The list of variations to be run.
   * @param runMode
   *          Which testcases to run (attended, unattended, both).
   * @param fileOutputStream
   *          The output stream to which to write the results.
   * @param password
   *          The user profile password or, if ending in .txt or .rev, the file to read the password from. 
   * @param pwrSysUid
   *          The power user profile.
   * @param pwrSysPwd
   *          The power user password or, if ending in .txt or .rev, the file to read the password from..
   **/
  public Testcase(AS400 system, String name, int totalVariations,
      Vector<String> variationsToRun, int runMode, FileOutputStream fileOutputStream,
       String password, String pwrSysUid, String pwrSysPwd) {
    initializeFields(system, name, totalVariations, variationsToRun, runMode,
        fileOutputStream,  password);
    pwrSysUserID_ = pwrSysUid;
    pwrSysEncryptedPassword_ = PasswordVault.getEncryptedPassword(pwrSysPwd);
    char[] pwrSysPasswordChars = PasswordVault.decryptPassword(pwrSysEncryptedPassword_); 
    try { 
      pwrSys_ = new AS400(systemObject_.getSystemName(), pwrSysUserID_, pwrSysPasswordChars);
    } finally {
      PasswordVault.clearPassword(pwrSysPasswordChars); 
    }

    String userName = System.getProperty("user.name");

    if (systemObject_.getUserId().equalsIgnoreCase(userName)) {
      sameSys_ = systemObject_;
    } else {

      if (pwrSys_ != null) {
        pwrSysUserID_ = pwrSys_.getUserId();

        if (pwrSysUserID_.equalsIgnoreCase(userName)) {
          sameSys_ = pwrSys_;
        }
      }
    }

  }

  private void initializeFields(AS400 system, String name, int totalVariations,
      Vector<String> variationsToRun, int runMode, FileOutputStream fileOutputStream,
       String password) {

    // System.out.println("checking interactive");
    String interactiveString = System.getProperty("interactive");
    if (interactiveString != null) {
      interactive_ = true;
      // System.out.println("Interactive is true");
    } else {
      interactive_ = false;
      // System.out.println("Interactive is false");
    }

    systemObject_ = system;
    if (systemName_ == null) { 
      systemName_ = systemObject_.getSystemName(); 
    }
    if (userId_ == null) { 
      userId_ = systemObject_.getUserId(); 
    }
    name_ = name;
    totalVariations_ = totalVariations;
    variationsToRun_ = variationsToRun != null ? variationsToRun : new Vector<String>();
    runMode_ = runMode;
    OutputStream out;
    if (fileOutputStream != null)
      out = new TestOutput(fileOutputStream);
    else
      out = new TestOutput();
    output_ = new PrintWriter(out, true);
    encryptedPassword_ = PasswordVault.getEncryptedPassword(password);

    testLib_ = null;
    baseTestDriver_ = null;

    // Note: Don't get system VRM yet. Some testcases need an unconnected
    // systemObject_.
    // IF YOU UN-COMMENT THIS, YOU WILL BREAK SOME TESTCASES, SUCH AS
    // SecPortMapperTestcase.Var008
    // try {
    // getSystemVRM();
    // }
    // catch (Exception e) {
    // System.out.println("Unable to determine system VRM.");
    // //e.printStackTrace(System.out);
    // }
  }

  /**
   * Determines if a boolean condition is true. Calls succeeded() if true,
   * otherwise failed() is called.
   * 
   * @param condition
   *          The boolean condition to test.
   **/
  public final void assertCondition(boolean condition) {
    assertCondition(condition, "Assertion failed.");
  }

  public final void assertCondition(boolean condition, StringBuffer sb) {
    String info = "No failure info";
    // Do not call to string unless the testcase failed.
    if (!condition)
      info = sb.toString();
    assertCondition(condition, info);
  }

  /**
   * Determines if a boolean condition is true. Calls succeeded() if true,
   * otherwise failed() is called.
   * 
   * @param condition
   *          The boolean condition to test.
   * @param comment
   *          Additional information concerning the failure.
   **/
  public final void assertCondition(boolean condition, String comment) {
    if (condition) {
      succeeded();
    } else {
      failed(comment);
    }
  }

  public final void assertEqualStrings(String retrieved, String expected) {
    if (retrieved == null) {
      if (expected == null) {
        succeeded();
      } else {
        failed("Got null sb " + expected);
      }
    } else {
      if (retrieved.equals(expected)) {
        succeeded();
      } else {
        failed("Got '" + retrieved + "' sb '" + expected + "'");
      }
    }
  }

  /**
   * Determine if an exception is an SQL exception with the corresponding code,
   * state, and message
   **/

  public final void assertSqlException(Exception e, int expectedSqlcode,
      String expectedSqlstate, String expectedSqlmessage, String comment) {
    if (e instanceof SQLException) {
      SQLException sqlex = (SQLException) e;
      int sqlcode = sqlex.getErrorCode();
      String sqlstate = sqlex.getSQLState();
      if (sqlstate == null) sqlstate="null";
      String sqlmessage = sqlex.getMessage();
      if (sqlmessage == null) sqlmessage = "null"; 
      int messageIndex = sqlmessage.indexOf(expectedSqlmessage);
      boolean condition = (sqlcode == expectedSqlcode)
          && (expectedSqlstate.equals(sqlstate)) && (messageIndex >= 0);
      assertCondition(condition, "Got sqlcode = '" + sqlcode + "' sb '"
          + expectedSqlcode + "' " + (sqlcode == expectedSqlcode) + "\n"
          + "Got sqlstate = " + sqlstate + " sb " + expectedSqlstate + " "
          + (sqlstate.equals(expectedSqlstate)) + " \n" + "Got sqlmessage = '"
          + sqlmessage + "'\n" + "              sb '" + expectedSqlmessage
          + "'\n" + "         index = " + (messageIndex) + "\n" + comment);
      if (!condition) {
        e.printStackTrace(System.out);
      }
    } else {
      failed(e, "Incorrect exception type :  Expected SQLException");
      e.printStackTrace(System.out);
    }

  }

  /**
   * Determines if an exception is a specific type. Calls succeeded() if the
   * exception is the specified type, otherwise failed() is called.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   **/
  public final void assertExceptionIs(Exception exception, String className) {
    if (exceptionIs(exception, className)) {
      succeeded();
    } else {
      failed(exception,
          "Incorrect exception information. Looking for className=" + className);
    }
  }

  /**
   * Determines if an exception is a specific type. Calls succeeded() if the
   * exception is the specified type, otherwise failed() is called.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @param stringBuffer
   *          Additinoal information about the testcase.
   **/
  public final void assertExceptionIs(Exception exception, String className,
      StringBuffer stringBuffer) {
    if (exceptionIs(exception, className)) {
      succeeded();
    } else {
      failed(exception,
          "Incorrect exception information. Looking for className=" + className
              + " info=" + stringBuffer);
    }
  }

  /**
   * Determines if an exception contains a specific message. Calls succeeded()
   * if the exception is the specified type, otherwise failed() is called.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected message in the exception (ie. "data type not valid").
   * @param stringBuffer
   *          Additional information about the testcase.
   **/
  public final void assertExceptionContains(Exception exception,
      String message, StringBuffer stringBuffer) {
    String exceptionMessage = exception.toString();
    if (exceptionMessage.indexOf(message) >= 0) {
      succeeded();
    } else {
      failed(exception, "Incorrect exception information. Looking for message:"
          + message + " info=" + stringBuffer);
    }
  }

  /**
   * Determines if an exception contains a specific message. Calls succeeded()
   * if the exception is the specified type, otherwise failed() is called.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected message in the exception (ie. "data type not valid").
   * @param stringBuffer
   *          Additional information about the testcase.
   **/

  public final void assertExceptionContains(Exception exception,
      String[] messages, String string) {
    StringBuffer messagesText = new StringBuffer();
    String exceptionMessage = exception.toString();
    for (int i = 0; i < messages.length; i++) {
      String message = messages[i];
      if (exceptionMessage.indexOf(message) >= 0) {
        succeeded();
        return;
      } else {
        if (i > 0)
          messagesText.append(",");
        messagesText.append("'");
        messagesText.append(message);
        messagesText.append("'");

      }
    }

    failed(exception, "Incorrect exception information. Looking for messages:"
        + messagesText + " info=" + string);
  }

  /**
   * Determines if an exception is a specific type and contains a specific
   * detail message. Calls succeeded() if the exception is the specified type,
   * otherwise failed() is called.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @param detail
   *          The expected detail message.
   **/
  public final void assertExceptionIs(Exception exception, String className,
      String detail) {
    StringBuffer sb = new StringBuffer(); 
    if (exceptionIs(exception, className, detail, sb)) {
      succeeded();
    } else {
      failed(exception,
          sb);
    }
  }

  /**
   * Determines if a ReturnCodeException contains a specific detail message and
   * return code. Calls succeeded() if the exception is the specified type,
   * otherwise failed() is called.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @param detail
   *          The expected detail message.
   * @param rc
   *          The expected return code.
   **/
  public final void assertExceptionIs(Exception exception, String className,
      String detail, int rc) {
    if (exceptionIs(exception, className, detail, rc)) {
      succeeded();
    } else {
      failed(exception,
          "Incorrect exception information. looking for className=" + className
              + " detail=" + detail + " rc=" + rc);
    }
  }

  /**
   * Determines if a ReturnCodeException is of a specific type and contains a
   * specific return code.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @param rc
   *          The expected return code.
   **/
  public final void assertExceptionIs(Exception exception, String className,
      int rc) {
    if (exceptionIs(exception, className, rc)) {
      succeeded();
    } else {
      failed(exception, "Incorrect exception information, expected instance of "+className);
    }
  }

  /**
   * Determines if an exception is a specific type or a subclass or
   * implementation of the type. Calls succeeded() if the exception is the
   * specified type, otherwise failed() is called.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   **/
  public final void assertExceptionIsInstanceOf(Exception exception,
      String className) {
    if (exceptionIsInstanceOf(exception, className)) {
      succeeded();
    } else {
      failed(exception, "Incorrect exception information, expected instance of "+className);
    }
  }

  /**
   * Determines if an exception is a specific type or a subclass or
   * implementation of the type. Calls succeeded() if the exception is the
   * specified type, otherwise failed() is called.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @param comment
   *          Additional information concerning the failure
   **/
  public final void assertExceptionIsInstanceOf(Exception exception,
      String className, String comment) {
    if (exceptionIsInstanceOf(exception, className)) {
      succeeded();
    } else {
      failed(exception, "Incorrect exception information expected instanceOf "+className+" -- " + comment);
    }
  }

  /**
   * Determines if a ReturnCodeException contains a specific detail message and
   * return code.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @param detail
   *          The expected detail message, this text does not have to equal the
   *          detail message exactly, the detail message must begin with this
   *          string, and can contain more text after that.
   * @param rc
   *          The expected return code.
   **/
  public final void assertExceptionStartsWith(Exception exception,
      String className, String detail, int rc) {
    if (exceptionStartsWith(exception, className, detail, rc)) {
      succeeded();
    } else {
      failed(exception, "Incorrect exception information.");
    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (pwrSys_ != null) { 
      pwrSys_.close(); 
      pwrSys_ = null ; 
    }
    if (systemObject_ != null) { 
      systemObject_.close(); 
      systemObject_ = null; 
    }
  }

  public String deleteLibrary(String command) {
    return TestDriverStatic.deleteLibrary(command, pwrSys_);
  }

  /**
   * Utility method to run commands under -pwrSys authority.
   * 
   * @param commmand
   *          Command to run.
   * @return true if command was successful; false otherwise.
   **/
  public boolean cmdRun(String command) {
    return TestDriverStatic.cmdRun(command, pwrSys_, output_, null);
  }

  public boolean cmdRun(String command, String expectedMessage) {
    return TestDriverStatic.cmdRun(command, pwrSys_, output_, expectedMessage);
  }

  /**
   * Counts the number of variations in the testcase.
   * 
   * @return The number of variations.
   **/
  private int countVariations() {
    int high = 0;
    Method[] methods = this.getClass().getMethods();
    for (int i = 0; i < methods.length; ++i) {
      String name = methods[i].getName();
      if (name.substring(0, 3).equals("Var")) {
        int number = Integer.parseInt(name.substring(3));
        if (number > high) {
          high = number;
        }
      }
    }

    return high;
  }

  public static byte[] createByteArray(int[] values) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    for (int i = 0; i < values.length; i++) {
      bos.write(values[i]);
    }
    return bos.toByteArray();
  }

  /**
   * Determines if an exception is a specific type.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @return true if the exception is of the expected type, false otherwise.
   **/
  public static boolean exceptionIs(Exception exception, String className) {
    return exceptionIs(exception, className, (StringBuffer) null);
  }

  /**
   * Determines if an exception is a specific type.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @return true if the exception is of the expected type, false otherwise.
   **/
  public static boolean exceptionIs(Exception exception, String className,
      StringBuffer info) {
    boolean equal;
    try {
      String fullyQualifiedName = exception.getClass().getName();
      int index = fullyQualifiedName.lastIndexOf('.');
      String exceptionName;
      if (index != -1) {
        exceptionName = fullyQualifiedName.substring(index + 1);
      } else {
        exceptionName = fullyQualifiedName;
      }
      equal = exceptionName.equals(className);
      if (!equal && info != null) {
        info.append("\nexceptionName=" + exceptionName + " expected="
            + className);
      }
    } catch (Exception e) {
      equal = false;
      if (info != null) {
        info.append("\nException checking exception: " + e.getMessage());
      }
    }

    return equal;
  }

  /**
   * Determines if an exception is a specific type and contains a specific
   * detail message.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @param detail
   *          The expected detail message.
   * @return true if the exception is of the expected type and contains the
   *         expected detail message.
   **/
  public static boolean exceptionIs(Exception exception, String className,
      String detail) {

    boolean equal;
    try {
      if (detail != null) {
        equal = exceptionIs(exception, className)
            && detail.equals(exception.getMessage());
      } else {
        equal = exceptionIs(exception, className)
            && exception.getMessage() == null;
      }
    } catch (Exception e) {
      equal = false;
    }
    return equal;
  }

  /**
   * Determines if an exception is a specific type and contains a specific
   * detail message.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @param detail
   *          The expected detail message.
   * @param info
   *          Information about why it is not true,
   * @return true if the exception is of the expected type and contains the
   *         expected detail message.
   **/
  public static boolean exceptionIs(Exception exception, String className,
      String detail, StringBuffer sb) {
    boolean equal;
    try {
      if (detail != null) {
        equal = true;
        if (!exceptionIs(exception, className, sb)) {
          equal = false;
        }
        String message = exception.getMessage();
        if (!detail.equals(message)) {
          equal = false;
          if (sb != null) {
            sb.append("\nExpected: '" + detail + "'");
            sb.append("\nGot:      '" + message + "'");
          }
        }
      } else {
        equal = exceptionIs(exception, className)
            && exception.getMessage() == null;
      }
    } catch (Exception e) {
      equal = false;
    }
    return equal;
  }

  /**
   * Determines if a ReturnCodeException contains a specific detail message and
   * return code.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @param detail
   *          The expected detail message.
   * @param rc
   *          The expected return code.
   * @return true if the exception contains the expected detail message and
   *         return code.
   **/
  public static boolean exceptionIs(Exception exception, String className,
      String detail, int rc) {
    boolean equal;
    if (exception instanceof ReturnCodeException) {
      int returnCode = ((ReturnCodeException) exception).getReturnCode();
      if (detail != null) {
        equal = exceptionIs(exception, className)
            && detail.equals(exception.getMessage()) && rc == returnCode;
      } else {
        equal = exceptionIs(exception, className)
            && exception.getMessage() == null && rc == returnCode;
      }
    } else {
      equal = false;
    }
    return equal;
  }

  /**
   * Determines if a ReturnCodeException contains a specific detail message and
   * return code.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @param rc
   *          The expected return code.
   * @return true if the exception contains the expected detail message and
   *         return code.
   **/
  public static boolean exceptionIs(Exception exception, String className,
      int rc) {
    boolean equal;

    if (exception instanceof ReturnCodeException) {
      int returnCode = ((ReturnCodeException) exception).getReturnCode();
      equal = exceptionIs(exception, className) && rc == returnCode;
    } else {
      equal = false;
    }

    return equal;
  }

  /**
   * Determines if an exception is a specific type or a subclass or
   * implementation of the type.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @return true if the exception is of the expected type, false otherwise.
   **/
  public static boolean exceptionIsInstanceOf(Exception exception,
      String className) {
    boolean equal;
    try {
      equal = Class.forName(className).isInstance(exception);
    } catch (Exception e) {
      equal = false;
    }

    return equal;
  }

  /**
   * Determines if a ReturnCodeException contains a specific detail message and
   * return code.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @param detail
   *          The expected detail message, this text does not have to equal the
   *          detail message exactly, the detail message must begin with this
   *          string, and can contain more text after that.
   * @param rc
   *          The expected return code.
   * @return true if the exception contains the expected detail message and
   *         return code.
   **/
  public static boolean exceptionStartsWith(Exception exception,
      String className, String detail, int rc) {
    boolean equal;

    if (exception instanceof ReturnCodeException) {
      int returnCode = ((ReturnCodeException) exception).getReturnCode();
      equal = exceptionIs(exception, className)
          && exception.getMessage().indexOf(detail) == 0 && rc == returnCode;
    } else {
      equal = false;
    }

    return equal;
  }

  /**
   * Determines if a ReturnCodeException contains a specific detail message.
   * 
   * @param exception
   *          The exception to test.
   * @param className
   *          The expected name of the exception (ie. "IOException").
   * @param detail
   *          The expected detail message, this text does not have to equal the
   *          detail message exactly, the detail message must begin with this
   *          string, and can contain more text after that.
   * @return true if the exception contains the expected detail message.
   **/
  public static boolean exceptionStartsWith(Exception exception,
      String className, String detail) {
    boolean equal;

    if (exception instanceof ReturnCodeException) {
      equal = exceptionIs(exception, className)
          && exception.getMessage().indexOf(detail) == 0;
    } else {
      equal = false;
    }

    return equal;
  }

  /**
   * Determines if a Exception message contains a specific string.
   * 
   * @param exception
   *          The exception to test.
   * @param detail
   *          The expected detail message in the Exception's message.
   * @return true if the exception contains the string in its detail message.
   **/
  public static boolean exceptionMsgHas(Exception exception, String detail) {
    String msg = exception.getMessage();
    int i = msg.indexOf(detail);
    if (i == -1) {
      return false; // The string is not present in the Exception's message.
    } else {
      return true;
    }
  }

  /**
   * This method is used to indicate failure of the current variation.
   **/
  public final void failed() {
    // Mark this variation as a failure.
    failures_++;

    // Output the message.
    outputVariationStatus(currentVariation_, FAILED, null);

    currentVariation_++;
    if (failureSleepSeconds > 0) {
      try {

        Thread.sleep(1000 * failureSleepSeconds);
      } catch (Exception e) {
      }
    }
  }

  /**
   * This method is used to indicate failure of the current variation.
   * 
   * @param comment
   *          Additional information concerning the failure.
   **/
  public final void failed(String comment) {
    // Mark this variation as a failure.
    failures_++;
    // Check if see if this is ignored for other releases
      try {
        String skippedInfo = JDVariationSkip.skipInformation(
            name_, currentVariation_);
        if (skippedInfo != null) {
          comment += "\nNote:  testcase skipped for the following: " + skippedInfo;
        } else {
          Class<?> skipClass = null; 
          try {
            skipClass = Class.forName("test.JDVariationSkip2");
          } catch (Exception e) {

          }
          // Check if see if this is ignored for other releases
          if (skipClass != null) {
            skippedInfo = (String) JDReflectionUtil.callStaticMethod_O("test.JDVariationSkip2", "skipInformation",
                name_, currentVariation_);
            if (skippedInfo != null) {
              comment += "\nNote:  testcase skipped for the following: " + skippedInfo;
            }
          }
        }

      } catch (Exception e) {
        // Ignore
      }
    // Output the message.
    outputVariationStatus(currentVariation_, FAILED, comment);

    currentVariation_++;

    if (failureSleepSeconds > 0) {
      try {
        Thread.sleep(1000 * failureSleepSeconds);
      } catch (Exception e) {
      }
    }

  }

  /**
   * This method is used to indicate failure of the current variation. It is
   * intended for use when an exception caused the failure.
   * 
   * @param exception
   *          The exception that caused the failure.
   **/
  public final void failed(Throwable exception) {
    failed(exception, "");
  }

  /**
   * This method is used to indicate failure of the current variation. It is
   * intended for use when an exception caused the failure.
   * 
   * @param exception
   *          The exception that caused the failure.
   * @param comment
   *          Additional information concerning the failure.
   **/
  public final void failed(Throwable exception, StringBuffer comment)

  {
    failed(exception, comment.toString());
  }

  /**
   * This method is used to indicate failure of the current variation. It is
   * intended for use when an exception caused the failure.
   * 
   * @param exception
   *          The exception that caused the failure.
   * @param comment
   *          Additional information concerning the failure.
   **/
  public final void failed(Throwable exception, String comment) {
    // Get the stack trace information.
    if (exception instanceof XAException) {
      exception = fixupXAException((XAException) exception);
    }
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    PrintWriter ps = new PrintWriter(os, true);
    exception.printStackTrace(ps);
    String detail = comment + "\n" + os.toString();
    System.out.flush();
    failed(detail);
    System.out.flush();

  }

  private XAException fixupXAException(XAException exception) {
    XAException newException = null;
    String message = exception.getMessage();
    if (message == null)
      message = "";
    if (message.length() == 0) {
      String reasonText = "";
      switch (exception.errorCode) {

      case XAException.XA_RBROLLBACK:
        reasonText = "XA_RBROLLBACK";
        break;
      case XAException.XA_RBCOMMFAIL:
        reasonText = "XA_RBCOMMFAIL";
        break;
      case XAException.XA_RBDEADLOCK:
        reasonText = "XA_RBDEADLOCK";
        break;
      case XAException.XA_RBINTEGRITY:
        reasonText = "XA_RBINTEGRITY";
        break;
      case XAException.XA_RBOTHER:
        reasonText = "XA_RBOTHER";
        break;
      case XAException.XA_RBPROTO:
        reasonText = "XA_RBPROTO";
        break;
      case XAException.XA_RBTIMEOUT:
        reasonText = "XA_RBTIMEOUT";
        break;
      case XAException.XA_RBTRANSIENT:
        reasonText = "XA_RBTRANSIENT";
        break;
      case XAException.XA_NOMIGRATE:
        reasonText = "XA_NOMIGRATE";
        break;
      case XAException.XA_HEURHAZ:
        reasonText = "XA_HEURHAZ";
        break;
      case XAException.XA_HEURCOM:
        reasonText = "XA_HEURCOM";
        break;
      case XAException.XA_HEURRB:
        reasonText = "XA_HEURRB";
        break;
      case XAException.XA_HEURMIX:
        reasonText = "XA_HEURMIX";
        break;
      case XAException.XA_RETRY:
        reasonText = "XA_RETRY";
        break;
      case XAException.XA_RDONLY:
        reasonText = "XA_RDONLY";
        break;
      case XAException.XAER_ASYNC:
        reasonText = "XAER_ASYNC";
        break;
      case XAException.XAER_RMERR:
        reasonText = "XAER_RMERR";
        break;
      case XAException.XAER_NOTA:
        reasonText = "XAER_NOTA";
        break;
      case XAException.XAER_INVAL:
        reasonText = "XAER_INVAL";
        break;
      case XAException.XAER_PROTO:
        reasonText = "XAER_PROTO";
        break;
      case XAException.XAER_RMFAIL:
        reasonText = "XAER_RMFAIL";
        break;
      case XAException.XAER_DUPID:
        reasonText = "XAER_DUPID";
        break;
      case XAException.XAER_OUTSIDE:
        reasonText = "XAER_OUTSIDE";
        break;
      default:
        reasonText = "XARC=" + exception.errorCode;
      }

      newException = new XAException(message + reasonText);
      newException.setStackTrace(exception.getStackTrace());
      newException.errorCode = exception.errorCode;
      return newException;
    } else {
      return exception;
    }
  }

  /**
   * Returns the value of the CommandCall.threadSafe property.
   * 
   * @return the value of the CommandCall.threadSafe property. If the property
   *         is not set, returns null.
   **/
  public static String getCommandCallThreadSafetyProperty() {
    java.util.Properties props = System.getProperties();
    return props.getProperty("com.ibm.as400.access.CommandCall.threadSafe");
  }

  /**
   * Returns the value of the ProgramCall.threadSafe property.
   * 
   * @return the value of the ProgramCall.threadSafe property. If the property
   *         is not set, returns null.
   **/
  public static String getProgramCallThreadSafetyProperty() {
    java.util.Properties props = System.getProperties();
    return props.getProperty("com.ibm.as400.access.ProgramCall.threadSafe");
  }

  /**
   * Returns the number of failed variations for this testcase.
   * 
   * @return The number of failed variations.
   **/
  public final int getFailed() {
    return failures_;
  }

  /**
   * Returns the number of variations that weren't applicable to this testcase.
   * 
   * @return The number of variations that don't apply to this testcase.
   **/
  public final int getNotApplicable() {
    return notApplicables_;
  }

  /**
   * Returns a resource from the appropriate resource bundle.
   * 
   * @param key
   *          The resource key.
   * @return The resource value.
   **/
  public static String getResource(String key) {
    if (resources_ == null) {
      try {
        resources_ = ResourceBundle.getBundle("test.mri.TestMRI");
      } catch (Throwable e) {
        System.out
            .println("Caught a MissingResourceException after getBundle.");
        e.printStackTrace(System.out);
      }

    }
    return resources_.getString(key);
  }

  /**
   * Return the number of successful variations for this testcase.
   * 
   * @return The number of successful variations.
   **/
  public final int getSucceeded() {
    return successes_;
  }

  /**
   * Returns the total time this testcase took to run.
   * 
   * @return The time in seconds.
   **/
  public final double getTimeToRun() {
    return timeToRun_;
  }

  /**
   * Returns the total number of variations in this testcase.
   * 
   * @return The total number of variations.
   **/
  public final int getTotalVariations() {
    return totalVariations_;
  }

  /**
   * Returns the current variation number.
   * 
   * @return The current variation number.
   **/
  public final int getVariation() {
    return currentVariation_;
  }

  /**
   * Returns the testcase name.
   * 
   * @return The testcase name.
   **/
  public String getName() {
    return name_;
  }

  /**
   * Returns the "server" system's time as retrieved via QWCRTVTM
   * 
   * @return Returns the time in milliseconds since the epoch. Returns -1 if an
   *         error occurs retrieving the system value.
   **/
  public static long getSysValTime(AS400 system) {
    try {
      getSysValTimeStringBuffer.setLength(0);
      // ---------------------------------------------------------------
      // Call the QWCRTVTM "Retrieve System Time Information" API
      // It will return the system time in GMT/UTC time. The
      // GMT time will be adjusted based on the client offset, to get
      // the server time into the correct client time.
      // ---------------------------------------------------------------
      int rcvrLength = 100; // More than we need, but that's ok
      final int systemCCSID = system.getCcsid();
      CharConverter conv = new CharConverter(systemCCSID);

      // Construct parameters.
      ProgramParameter[] parameters = new ProgramParameter[] // 6
      {
          // Receiver variable - Output - Char(*).
          new ProgramParameter(rcvrLength),
          // Length of receiver variable - Input - Binary(4).
          new ProgramParameter(BinaryConverter.intToByteArray(rcvrLength)),
          // Format name - Input - Char(8)
          new ProgramParameter(conv.stringToByteArray("RTTM0100")),
          // Number of fields to return - Input - Binary(4).
          new ProgramParameter(BinaryConverter.intToByteArray(1)),
          // Key of fields to return - Input - Array(*) of Binary(4).
          new ProgramParameter(BinaryConverter.intToByteArray(101)), // Request
                                                                     // key(101)
                                                                     // "Coordinated Universal Time CHAR(8)"
          // Error code - I/0 - Char(*).
          new ProgramParameter(new byte[8]) };
      // QWCRTVTM is the API that is used to "Retrieve System Time Information"
      ProgramCall pc = new ProgramCall(system, "/QSYS.LIB/QWCRTVTM.PGM",
          parameters);
      pc.setThreadSafe(true); // The QWCRTVTM API is documented to be
                              // thread-safe

      if (!pc.run()) {
        System.out.println("ERROR: Failure calling QWCRTVTM API.");
        AS400Message[] messageList = pc.getMessageList();
        for (int i = 0; i < messageList.length; i++) {
          System.out.println(messageList[i]);
        }
        return (-1);
      }

      // ---------------------------------------------------------------
      // Extract the data returned from the QWCRTVTM API
      // (http://publib.boulder.ibm.com/infocenter/systems/scope/i5os/topic/apis/qwcrtvtm.htm)
      // ---------------------------------------------------------------
      byte[] data = parameters[0].getOutputData();
      // int bytesReturned = BinaryConverter.byteArrayToInt(data, 0);
      // int bytesAvail = BinaryConverter.byteArrayToInt(data, 4);
      int offsetToKeyFields = BinaryConverter.byteArrayToInt(data, 8);
      // int numberFieldsReturned = BinaryConverter.byteArrayToInt(data, 12);
      // int lengthFieldInfoReturned = BinaryConverter.byteArrayToInt(data,
      // offsetToKeyFields+0);
      // int keyField = BinaryConverter.byteArrayToInt(data,
      // offsetToKeyFields+4);
      // String typeOfData = conv.byteArrayToString(data, offsetToKeyFields+8,
      // 1).trim();
      // int lengthOfData = BinaryConverter.byteArrayToInt(data,
      // offsetToKeyFields+12);

      byte[] dateBytes = new byte[8];
      for (int i = 0; i < 8; ++i) {
        dateBytes[i] = data[offsetToKeyFields + 16 + i];
      }
      // ---------------------------------------------------------------
      // Construct a Date object from the QWCRTVTM API's "Date Time-Stamp"
      // format
      // ---------------------------------------------------------------
      Date qwcrtvtmGMTDate = new DateTimeConverter(system).convert(dateBytes,
          "*DTS");
      getSysValTimeStringBuffer.append("Before UTC Adjust qwcrtvtmGMTDate="
          + qwcrtvtmGMTDate.getTime() + " (" + qwcrtvtmGMTDate + ")\n");

      Date clientNowDate = new Date();
      long clientNowTime = clientNowDate.getTime();
      getSysValTimeStringBuffer.append("clientNow                     ="
          + clientNowTime + " (" + clientNowDate + ")\n");
      DateFormat dateFormat = DateFormat.getDateInstance();
      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
      
      getSysValTimeStringBuffer.append("Client Now GMT String"
          + dateFormat.format(clientNowDate) + "\n");

      
      // =======================================================================
      // If the correct TimeZone offset is not calculated... it could be due to
      // a Java JRE with downlevel timezone info.
      // SUN provides a tool to update it's JREs:
      // (TZUpdater http://java.sun.com/javase/timezones/)
      // IBM provides a tool to update it's JREs:
      // (JTZU http://www.ibm.com/developerworks/java/jdk/dst/index.html)
      // =======================================================================
      TimeZone tz = TimeZone.getDefault();
      int clientGMTOffset = tz.getOffset(clientNowTime);
      // ---------------------------------------------------------------
      // Now create an adjusted Date object which will put the system
      // time into the correct client time-zone time.
      // ---------------------------------------------------------------
      Date qwcrtvtmSysDateAdjusted = new Date(qwcrtvtmGMTDate.getTime()
          + clientGMTOffset);
      long qwcrtvtmSysTimeAdjusted = qwcrtvtmSysDateAdjusted.getTime();
      getSysValTimeStringBuffer
          .append("After adjust qwcrtvtmSysDateAdjusted  ="
              + qwcrtvtmSysDateAdjusted.getTime() + " ("
              + qwcrtvtmSysDateAdjusted + ")\n");

      // ---------------------------------------------------------------
      // Determine the difference in the client time and system time to
      // put out a warning if they are off by more than a couple minutes
      // ---------------------------------------------------------------
      long difference = Math.abs(clientNowTime - qwcrtvtmSysTimeAdjusted);
      if (difference > 120000) {
        System.out
            .println("  WARNING: Client time and curSysValTime difference = '"
                + difference / 1000 + "' seconds");
        getSysValTimeStringBuffer
            .append("  WARNING: Client time and curSysValTime difference = '"
                + difference / 1000 + "' seconds\n");
      }
      return (qwcrtvtmSysTimeAdjusted);
    } catch (Exception e) {
      System.out
          .println("ERROR: Failure calling QWCRTVTM to retrieve system time.");
      e.printStackTrace(System.out);
      return (-1);
    }
  }

  // Values returned by getSystemVRM().
  public final static int VRM_V5R1M0 = 0x00050100;
  public final static int VRM_V5R2M0 = 0x00050200;
  public final static int VRM_V5R3M0 = 0x00050300;
  public final static int VRM_V5R4M0 = 0x00050400;
  public final static int VRM_V6R1M0 = 0x00060100;
  public final static int VRM_V7R1M0 = 0x00070100;
  public final static int VRM_V7R2M0 = 0x00070200;

  /**
   * Returns the system's VRM.
   * 
   * @return The system's VRM.
   **/
  public int getSystemVRM()

  {
    if (systemVRM_ == 0)
      try {
        systemVRM_ = systemObject_.getVRM();
      } catch (Exception e) {
        System.out.println("Warning:  could not get VRM");
        e.printStackTrace(System.out);
      }
    return systemVRM_;
  }

  /**
   * Return the "implementation version" for the Toolbox 'access' package. For
   * example: "JTOpen 6.2".
   **/
  static String getToolboxImplementationVersion() {
    return TestDriverStatic.getToolboxImplementationVersion();
  }

  /**
   * Return the "specification version" for the Toolbox 'access' package. For
   * example: "6.1.0.4" would indicate V6R1M0, PTF 4.
   **/
  static String getToolboxSpecificationVersion() {
    return TestDriverStatic.getToolboxSpecificationVersion();
  }

  /**
   * Invokes a single variation.
   * 
   * @param variation
   *          The variation.
   **/
  protected void invokeVariation(int variation) {
    if ((variation <= 0) || (variation > totalVariations_)) {
      System.out.println("Invalid variation number " + variation + ".");
      return;
    }

    Throwable e = null;
    try {
      String temp = "000" + variation;
      String methodName = "Var" + temp.substring(temp.length() - 3);
      Method method = null;
      try {
        method = this.getClass().getMethod(methodName, new Class[0]);
      } catch (NoSuchMethodException ee) {
        Class<?>[] parms = new Class[1];
        parms[0] = Integer.TYPE;
        method = this.getClass().getMethod(methodName, parms);
      }

      variationStartTime = System.currentTimeMillis();
      setVariation(variation);

      Class<?>[] paramTypes = method.getParameterTypes();
      if (paramTypes.length == 0) {
        if (runMode_ != ATTENDED) {
          if (this.checkVariationApplies()) {
            method.invoke(this, new Object[0]);
          }
        }
      } else {
        if (paramTypes.length > 1) {
          System.out.println("ERROR: Test Variation " + variation
              + " has incorrect parameter count.");
          System.exit(-1);
        }
        if (!paramTypes[0].getName().equals("int")) {
          System.out.println(" < " + method.toString()
              + " > is an invalid test variation signature.");
          System.exit(-1);
        }

        Object[] parms = new Object[1];
        parms[0] = new Integer(runMode_);

        method.invoke(this, parms);
      }
    } catch (IllegalAccessException e2) {
      e = e2;
    } catch (InvocationTargetException e2) {
      e = e2.getTargetException();
    } catch (NoSuchMethodException e2) {
      e = e2;
    }

    if (e != null) {
      outputVariationStatus(variation, NOT_ATTEMPTED, e.toString());
      System.out.println("Error invoking variation " + variation + ".");
      e.printStackTrace(System.out);
    }
  }

  /**
   * Indicates whether the contents of two arrays match.
   * 
   * @param array1
   *          The first array.
   * @param array2
   *          The second array.
   * @return true if the arrays have the same length and their contents match;
   *         false otherwise.
   * @deprecated As of Mod5, replaced by {@link #areEqual(byte[],byte[])}.
   **/
  public static boolean isEqual(byte[] array1, byte[] array2) {
    return areEqual(array1, array2);
  }

  /**
   * Indicates whether the contents of two arrays match.
   * 
   * @param array1
   *          The first array.
   * @param array2
   *          The second array.
   * @return true if the arrays have the same length and their contents match;
   *         false otherwise.
   **/
  public static boolean areEqual(byte[] array1, byte[] array2) {
    boolean answer = array1.length == array2.length;
    if (answer) {
      for (int i = 0; i < array1.length; ++i) {
        if (array1[i] != array2[i]) {
          answer = false;
          break;
        }
      }
    }
    return answer;
  }

  /**
   * Indicates whether the contents of two arrays match.
   * 
   * @param array1
   *          The first array.
   * @param array2
   *          The second array.
   * @return true if the arrays have the same length and their contents match;
   *         false otherwise.
   **/
  public static boolean areEqual(byte[] array1, byte[] array2, StringBuffer sb) {
    boolean answer = array1.length == array2.length;
    if (answer) {
      for (int i = 0; i < array1.length; ++i) {
        if (array1[i] != array2[i]) {
          answer = false;
          sb.append("array1[" + i + "]=0x"
              + Integer.toHexString(0xFF & array1[i]) + " != array2[" + i
              + "]=0x" + Integer.toHexString(0xFF & array2[i]) + "\n");
        }
      }
    } else {
      sb.append("Length not equal\n");
      sb.append("array1:");
      dumpByteArray(sb, array1);
      sb.append("\n");
      sb.append("array2:");
      dumpByteArray(sb, array2);
      sb.append("\n");
    }
    return answer;
  }

  
  /**
   * Indicates whether the contents of two arrays match.
   * 
   * @param array1
   *          The first array.
   * @param array2
   *          The second array.
   * @param t
   *          if true getAsciiStream, otherwise getUnicodeStream was called on
   *          the ResultSet
   * @return true if the arrays have the same length and their contents match;
   *         false otherwise.
   **/
  public static boolean areEqual(byte[] array1, byte[] array2, boolean t) {
    boolean answer = true;
    if (t) {
      answer = array1.length * 2 == array2.length;
      if (answer) {
        // bytesToString - Converts array1 which is in Decimal to Ascii
        // characters
        // Note: Each decimal number corresponds to two Ascii characters
        // The ASCII characters are hexadecimal digits 0-9 or A-F
        byte[] v = stringToAsciiDecimal(bytesToString(array1), array1.length);
        for (int i = 0; i < v.length; ++i) {
          if (v[i] != array2[i]) {
            answer = false;
            break;
          }
        }
      }
    } else {
      answer = array1.length * 4 == array2.length;
      if (answer) {
        byte[] v = stringToAsciiDecimal(bytesToString(array1), array1.length);
        for (int i = 0; i < v.length; ++i) {
          if (v[i] != array2[2 * i + 1]) {
            answer = false;
            break;
          }
        }
      }
    }
    return answer;
  }

  /**
   * Indicates whether the contents of two arrays match.
   * 
   * @param array1
   *          The first array.
   * @param array2
   *          The second array.
   * @param t
   *          if true getAsciiStream, otherwise getUnicodeStream was called on
   *          the ResultSet
   * @return true if the arrays have the same length and their contents match;
   *         false otherwise.
   **/
  public static boolean areEqual(byte[] array1, byte[] array2, boolean t,
      StringBuffer sb) {
    boolean answer = true;
    if (t) {
      answer = array1.length * 2 == array2.length;
      if (!answer) {
        sb.append("array1.length*2(" + array1.length * 2
            + ") != array2.length(" + array2.length + ")\n");
      }
      if (answer) {
        // bytesToString - Converts array1 which is in Decimal to Ascii
        // characters
        // Note: Each decimal number corresponds to two Ascii characters
        // The ASCII characters are hexadecimal digits 0-9 or A-F
        byte[] v = stringToAsciiDecimal(bytesToString(array1), array1.length);
        for (int i = 0; i < v.length; ++i) {
          if (v[i] != array2[i]) {
            answer = false;
            break;
          }
        }
        if (answer == false) {
          sb.append("Comparision failed\n");
          sb.append(bytesToString(v) + "\n");
          sb.append(bytesToString(array2) + "\n");
        }
      }
    } else {
      answer = array1.length * 4 == array2.length;
      if (!answer) {
        sb.append("array1.length*4(" + array1.length * 4
            + ") != array2.length(" + array2.length + ")\n");
      }
      if (answer) {
        byte[] v = stringToAsciiDecimal(bytesToString(array1), array1.length);
        for (int i = 0; i < v.length; ++i) {
          if (v[i] != array2[2 * i + 1]) {
            answer = false;
            break;
          }
        }
        if (answer == false) {
          sb.append("Comparision failed\n");
          sb.append(bytesToString(v) + "\n");
          sb.append(bytesToString(array2) + "\n");
        }
      }
    }
    return answer;
  }

  /**
   * This method is used to indicate that the current variation does not apply.
   **/
  public final void notApplicable() {
    // Mark this varaition as not applicable.
    notApplicables_++;
    // Output the message.
    outputVariationStatus(currentVariation_, NOT_APPLICABLE, null);
    currentVariation_++;
  }

  /**
   * This method is used to indicate that the current variation does not apply.
   **/
  public final void notApplicable(String comment) {
    // Mark this variation as not applicable.
    notApplicables_++;
    // Output the message.
    outputVariationStatus(currentVariation_, NOT_APPLICABLE, comment);
    currentVariation_++;
  }

  /**
   * Outputs testcase status summary.
   **/
  public final void outputResults() {
    // String value;

    // Generate the heading. Determine how much space is needed for the testcase
    // name.
    int column1Offset = 0;
    int column2Offset = name_.length() + 2 > NAME_HEADING.length() ? name_
        .length() + 2 : NAME_HEADING.length();
    int column3Offset = column2Offset + SUCCEEDED_HEADING.length();
    int column4Offset = column3Offset + FAILED_HEADING.length();
    int column5Offset = column4Offset + NOT_APPLICABLE_HEADING.length();
    int column6Offset = column5Offset + NOT_ATTEMPTED_HEADING.length();
    // int headingLength = column6Offset + TIME_TO_RUN_HEADING.length();
    StringBuffer heading2 = new StringBuffer();
    heading2.append(NAME_HEADING);
    // Append enough spaces for the testcase name if longer than heading.
    if (name_.length() + 2 > NAME_HEADING.length()) {
      for (int i = 0; i < (name_.length() + 2 - NAME_HEADING.length()); ++i) {
        heading2.append(' ');
      }
    }
    heading2.append(SUCCEEDED_HEADING);
    heading2.append(FAILED_HEADING);
    heading2.append(NOT_APPLICABLE_HEADING);
    heading2.append(NOT_ATTEMPTED_HEADING);
    heading2.append(TIME_TO_RUN_HEADING);

    StringBuffer heading1 = new StringBuffer();
    int length = heading2.length();
    for (int i = 0; i < length; ++i) {
      heading1.append('_');
    }

    // Generate the succeeded, failed, not applicable, and not attempted
    // strings.
    String succeededStr = String.valueOf(successes_);
    String failedStr = String.valueOf(failures_);
    String notApplicableStr = String.valueOf(notApplicables_);
    String notAttemptedStr = totalVariations_ != 0 ? String
        .valueOf(totalVariations_ - successes_ - failures_ - notApplicables_)
        : "?";
    String timeToRunStr = String.valueOf(timeToRun_);

    // Generate the line of status information.
    StringBuffer status = new StringBuffer();
    int dataLength = name_.length() + succeededStr.length()
        + failedStr.length() + notApplicableStr.length()
        + notAttemptedStr.length() + timeToRunStr.length();
    int lineLength = column6Offset + 1 + timeToRunStr.length() + 1;
    int padLength = lineLength - dataLength; // How many spaces in line.
    if (padLength < 1)
      padLength = 1; // For bulletproofing.
    status.setLength(padLength); // Preload the spaces into the StringBuffer.
    for (int i = 0; i < status.length(); ++i) {
      status.setCharAt(i, ' ');
    }
    status.insert(column1Offset, name_);
    status.insert(column2Offset + 4 - succeededStr.length() / 2, succeededStr);
    status.insert(column3Offset + 3 - failedStr.length() / 2, failedStr);
    status.insert(column4Offset + 4 - notApplicableStr.length() / 2,
        notApplicableStr);
    status.insert(column5Offset + 3 - notAttemptedStr.length() / 2,
        notAttemptedStr);
    status.insert(column6Offset + 1, timeToRunStr);

    // Output status.
    output_.println(heading1);
    output_.println(heading2);
    output_.println(status);
    output_.println(heading1);
  }

  private void outputVariationStatus(int variation, int status, String detail) {
    String variationStr = String.valueOf(variation);
    while (variationStr.length() < 3)
      variationStr += ' ';

    String resultStr;
    if (status == NOT_APPLICABLE)
      resultStr = "NOT APPLICABLE";
    else if (status == FAILED)
      resultStr = "FAILED";
    else if (status == NOT_ATTEMPTED)
      resultStr = "NOT ATTEMPTED";
    else
      resultStr = "SUCCESSFUL";

    String variationStatus = name_ + "  " + variationStr + "  " + resultStr;
    if (detail != null) {
      String newDetail = JDTestUtilities.getMixedString(detail);
      if (newDetail.equals(detail)) {
        variationStatus += " " + detail;
      } else {
        variationStatus += " UXDetail=" + newDetail;
      }
    }

    long variationEndTime = System.currentTimeMillis();
    variationStatus += " time " + (variationEndTime - variationStartTime)
        + " ms";
    /*
     * Set the variation start time for those testcases which do not use invoke
     * variation
     */
    variationStartTime = variationEndTime;
    String endTime = (new Date()).toString();
    variationStatus += " ended at " + endTime;
    // Output status.
    output_.flush();
    output_.println(variationStatus);
    output_.flush();
  }

  // Logs data from a byte array starting at offset for the length specified.
  // Output sixteen bytes per line, two hexadecimal digits per byte, one space
  // between bytes.
  public void printByteArray(byte[] data) {
    if (data == null)
      printByteArray(output_, data, 0, 0);
    else
      printByteArray(output_, data, 0, data.length);
  }

  public void printByteArray(String description, byte[] data) {
    output_.println(description);
    printByteArray(data);
  }

  public static void printByteArray(PrintWriter pw_, byte[] data, int offset,
      int length) {
    if (data == null) {
      pw_.println("null");
    } else {
      for (int i = 0; i < length; i++, offset++) {
        int leftDigitValue = (data[offset] >>> 4) & 0x0F;
        int rightDigitValue = data[offset] & 0x0F;
        // 0x30 = '0', 0x41 = 'A'
        char leftDigit = leftDigitValue < 0x0A ? (char) (0x30 + leftDigitValue)
            : (char) (leftDigitValue - 0x0A + 0x41);
        char rightDigit = rightDigitValue < 0x0A ? (char) (0x30 + rightDigitValue)
            : (char) (rightDigitValue - 0x0A + 0x41);
        pw_.print(leftDigit);
        pw_.print(rightDigit);
        pw_.print(" ");

        if ((i & 0x0F) == 0x0F) {
          pw_.println();
        }
      }
      if (((length - 1) & 0x0F) != 0x0F) {
        // Finish the line of data.
        pw_.println();
      }
    }
    pw_.flush();
  }

  // Logs data from a byte array starting at offset for the length specified.
  // Output sixteen bytes per line, two hexadecimal digits per byte, one space
  // between bytes.
  public static void dumpByteArray(StringBuffer sb, byte[] data) {
    if (data == null)
      dumpByteArray(sb, data, 0, 0);
    else
      dumpByteArray(sb, data, 0, data.length);
  }

  public static void dumpByteArray(StringBuffer sb, String description,
      byte[] data) {
    sb.append(description + "\n");
    dumpByteArray(sb, data);
  }

  public static void dumpByteArray(StringBuffer sb, byte[] data, int offset,
      int length) {
    if (data == null) {
      sb.append("null\n");
    } else {
      for (int i = 0; i < length; i++, offset++) {
        int leftDigitValue = (data[offset] >>> 4) & 0x0F;
        int rightDigitValue = data[offset] & 0x0F;
        // 0x30 = '0', 0x41 = 'A'
        char leftDigit = leftDigitValue < 0x0A ? (char) (0x30 + leftDigitValue)
            : (char) (leftDigitValue - 0x0A + 0x41);
        char rightDigit = rightDigitValue < 0x0A ? (char) (0x30 + rightDigitValue)
            : (char) (rightDigitValue - 0x0A + 0x41);
        sb.append(leftDigit);
        sb.append(rightDigit);
        sb.append(" ");

        if ((i & 0x0F) == 0x0F) {
          sb.append("\n");
        }
      }
      if (((length - 1) & 0x0F) != 0x0F) {
        // Finish the line of data.
        sb.append("\n");
      }
    }

  }

  /**
   * Print text to output_.
   **/
  public final void print(String text) {
    output_.print(text);
  }

  /**
   * Print text to output_.
   **/
  public final void println(String text) {
    output_.println(text);
  }

  /**
   * Print a newline to output_.
   **/
  public final void println() {
    output_.println();
  }

  /**
   * Output a "Starting variation at <time>" message.
   **/
  public final void printVariationStartTime() {
    output_.println("Starting " + getName() + " var " + getVariation());
    output_.println("on " + (new Date()).toString());
  }

  /**
   * Runs the specified variations for this testcase.
   **/
  public void run() {
    boolean allVariations = (variationsToRun_.size() == 0);

    if (baseTestDriver_ != null) {
      testLib_ = baseTestDriver_.getTestLib();
    }

    String skipCleanupString = System.getProperty("test.skipCleanup");
    if (skipCleanupString == null) {
      skipCleanup = false;
    } else {
      skipCleanup = true;
    }

    try {
      int vrm = systemObject_.getVRM();
      if (vrm == AS400.generateVRM(5, 4, 0)) {
        release_ = JDTestDriver.RELEASE_V7R1M0;
      } else if (vrm == AS400.generateVRM(6, 1, 0)) {
        release_ = JDTestDriver.RELEASE_V7R1M0;
      } else if (vrm == AS400.generateVRM(7, 1, 0)) {
        release_ = JDTestDriver.RELEASE_V7R1M0;
      } else if (vrm == AS400.generateVRM(7, 2, 0)) {
        release_ = JDTestDriver.RELEASE_V7R2M0;
      } else if (vrm == AS400.generateVRM(7, 3, 0)) {
        release_ = JDTestDriver.RELEASE_V7R3M0;
      } else if (vrm == AS400.generateVRM(7, 4, 0)) {
        release_ = JDTestDriver.RELEASE_V7R4M0;
      } else if (vrm == AS400.generateVRM(7, 5, 0)) {
        release_ = JDTestDriver.RELEASE_V7R5M0;
      } else if (vrm == AS400.generateVRM(7, 6, 0)) {
        release_ = JDTestDriver.RELEASE_V7R5M0_PLUS;
      } else {
        System.out.println("***************************");
        System.out.println(" WARNING .. release not set");
        System.out.println("***************************");
        Exception e = new Exception("Release not Set");
        e.printStackTrace(System.out);
        System.out.println("***************************");
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace(System.out);
    }

    try {
      setup();
    } catch (Exception e) {
      System.out.println("Testcase setup error: " + e.getMessage());
      e.printStackTrace(System.out);
      if (endIfSetupFails_ == true) {
        return;
      }
    }

    int number = 0;
    int variationCount = 0;
    Method[] methods = this.getClass().getMethods();

    //
    // Sort the methods so that they run in order
    //
    Arrays.sort(methods, new MethodComparator());

    for (int index = 0; index < methods.length; ++index) {
      String methodName = methods[index].getName();

      if (methodName.substring(0, 3).equals("Var")) {
        ++variationCount;
        number = new Integer(methodName.substring(3)).intValue();
        String stringNumber = "" + number;
        if (allVariations || variationsToRun_.contains(stringNumber)) {
          invokeVariation(number);
          variationsToRun_.remove(stringNumber);
          // Repeat the variations
          while (variationsToRun_.contains(stringNumber)) {
            invokeVariation(number);
            variationsToRun_.remove(stringNumber);
          }
        }
      }
    }

    if (variationCount != totalVariations_) {
      System.out.println("Variation count is inconsistent: " + variationCount
          + " != " + totalVariations_);
    }

    try {
      if (!skipCleanup) {
        cleanup();
      } else {
        System.out.println("test.skipCleanup set so Testcase skipping cleanup");
        if (this instanceof JDTestcase) {
          ((JDTestcase) this).cleanupConnections();
        }

      }

    } catch (Exception e) {
      System.out.println("Testcase cleanup error: " + e.getMessage());
      e.printStackTrace(System.out);
    }
  }

  public void setTestcaseParameters(AS400 system, AS400 pwrSys,
      String systemName, String userId, String password, String proxy,
      boolean mustUseSockets, boolean isNative, boolean isLocal,
      boolean onAS400, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream) {
    systemObject_ = system;
    pwrSys_ = pwrSys;
    if (pwrSys_ != null && pwrSysUserID_ == null) {
      pwrSysUserID_ = pwrSys_.getUserId();
    }

    String userName = System.getProperty("user.name");

    if (systemObject_.getUserId().equalsIgnoreCase(userName)) {
      sameSys_ = systemObject_;
    } else {
      if (pwrSys_ != null) {
        pwrSysUserID_ = pwrSys_.getUserId();

        if (pwrSysUserID_.equalsIgnoreCase(userName)) {
          sameSys_ = pwrSys_;
        }
      }
    }

    systemName_ = systemName;
    userId_ = userId;
    encryptedPassword_ = PasswordVault.getEncryptedPassword(password);
    proxy_ = proxy;
    mustUseSockets_ = mustUseSockets;
    isNative_ = isNative;
    isLocal_ = isLocal;
    onAS400_ = onAS400;

    // Note: Don't get system VRM yet. Some testcases need an unconnected
    // systemObject_.
    // IF YOU UN-COMMENT THIS, YOU WILL BREAK SOME TESTCASES, SUCH AS
    // SecPortMapperTestcase.Var008
    // try {
    // getSystemVRM();
    // }
    // catch (Exception e) {
    // System.out.println("Unable to determine system VRM.");
    // }

    String fullyQualifiedName = this.getClass().getName();
    int index = fullyQualifiedName.lastIndexOf('.');
    name_ = (index != -1) ? fullyQualifiedName.substring(index + 1)
        : fullyQualifiedName;

    totalVariations_ = countVariations();

    variationsToRun_ = namesAndVars.get(name_);
    if (variationsToRun_ == null)
      variationsToRun_ = new Vector<String>();
    runMode_ = runMode;
    OutputStream out;
    if (fileOutputStream != null) {
      out = new TestOutput(fileOutputStream);
    } else {
      out = new TestOutput();
    }
     output_ = new PrintWriter(out, true);
 
  }

  public void setTestcaseParameters(AS400 system, AS400 pwrSys,
      String systemName, String userId, String password, String proxy,
      boolean mustUseSockets, boolean isNative, boolean isLocal,
      boolean onAS400, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String pwrSysUserID, String pwrSysPassword) {
    setTestcaseParameters(system, pwrSys, systemName, userId, password, proxy,
        mustUseSockets, isNative, isLocal, onAS400, namesAndVars, runMode,
        fileOutputStream);
    pwrSysUserID_ = pwrSysUserID;
    pwrSysEncryptedPassword_ = PasswordVault.getEncryptedPassword(pwrSysPassword);

  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
  }

  /**
   * This method is used to skip the current variation.
   **/
  public final void setVariation(int variation) {
    currentVariation_ = variation;
  }

  /**
   * This method sets the time it took to run the testcase.
   **/
  public final void setTimeToRun(double time) {
    timeToRun_ = time;
  }

  /**
   * This method is used to indicate success of the current variation.
   **/
  public final void succeeded() {
    // Mark this variation as successful.
    ++successes_;
    // Output the message.
    outputVariationStatus(currentVariation_, SUCCEEDED, null);
    ++currentVariation_;
  }

  /**
   * This method is used to indicate success of the current variation.
   * 
   * @param comment
   *          Additional information concerning the success.
   **/
  public final void succeeded(String comment) {
    // Mark this variation as successful.
    ++successes_;
    // Output the message.
    outputVariationStatus(currentVariation_, SUCCEEDED, comment);
    ++currentVariation_;
  }

  public static final String inputStreamToString(InputStream in) throws Exception {
    byte[] buffer = new byte[1024];
    StringBuffer sb = new StringBuffer();
    int bytesRead;
    bytesRead = in.read(buffer);
    while (bytesRead >= 0) {
      if (bytesRead > 0) {
        sb.append(bytesToString(buffer, 0, bytesRead));
      }
      bytesRead = in.read(buffer);
    }
    return sb.toString();
  }

  public static final String readerToString(Reader reader) throws Exception {
    StringBuffer sb = new StringBuffer();
    char[] buffer = new char[1024];
    int charsRead;
    charsRead = reader.read(buffer);
    while (charsRead >= 0) {
      if (charsRead > 0) {
        sb.append(buffer, 0, charsRead);
      }
      charsRead = reader.read(buffer);
    }
    return sb.toString();
  }

  
  public  static final String bytesToString(final byte[] b) {
    if (b == null)
      return "null";
    return bytesToString(b, 0, b.length);
  }

  
  static final String bytesToString(final byte[] b, int offset, int length) {
    if (b == null)
      return "null";
    char[] c = new char[length * 2];
    int num = bytesToString(b, offset, length, c, 0);
    return new String(c, 0, num);
  }

  
  // Helper method to convert a byte array into its hex string representation.
  // This is faster than calling Integer.toHexString(...)
  public static final int bytesToString(final byte[] b, int offset, int length,
      final char[] c, int coffset) {
    for (int i = 0; i < length; ++i) {
      final int j = i * 2;
      final byte hi = (byte) ((b[i + offset] >>> 4) & 0x0F);
      final byte lo = (byte) ((b[i + offset] & 0x0F));
      c[j + coffset] = c_[hi];
      c[j + coffset + 1] = c_[lo];
    }
    return length * 2;
  }

  
  // Takes each ASCII character and converts it to its corresponding Decimal
  // number
  // For example A becomes 65
  // @return the String array in Decimal
  public static final byte[] stringToAsciiDecimal(String s, int length) {
    // Convert the String to a character array to process each ASCII character
    char[] b = s.toCharArray();
    // Each ASCII character is two digits when converted to DECIMAL
    byte[] t = new byte[length * 2];
    for (int i = 0; i < b.length; i++) {
      for (int k = 0; k < c_.length; k++) {
        if (b[i] == c_[k]) {
          t[i] = b_[k];
          continue;
        }
      }

    }

    return t;

  }

  
  private static final char[] c_ = { '0', '1', '2', '3', '4', '5', '6', '7',
      '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

  
  private static final byte[] b_ = stringToBytes("30313233343536373839414243444546");

  
  public static final byte[] stringToBytes(String s) {
    char[] c = s.toCharArray();
    return stringToBytes(c, 0, c.length);
  }

  
  public static final byte[] stringToBytes(char[] hex, int offset, int length) {
    if (hex.length == 0)
      return new byte[0];
    byte[] buf = new byte[length / 2];
    int num = stringToBytes(hex, offset, length, buf, 0);
    if (num < buf.length) {
      byte[] temp = buf;
      buf = new byte[num];
      System.arraycopy(temp, 0, buf, 0, num);
    }
    return buf;
  }

  
  // Helper method to convert a String in hex into its corresponding byte array.
  public static final int stringToBytes(char[] hex, int offset, int length,
      final byte[] b, int boff) {
    if (hex.length == 0)
      return 0;
    if (hex[offset] == '0'
        && (hex.length > offset + 1 && (hex[offset + 1] == 'X' || hex[offset + 1] == 'x'))) {
      offset += 2;
      length -= 2;
    }
    for (int i = 0; i < b.length; ++i) {
      final int j = i * 2;
      final int c1 = 0x00FFFF & hex[j + offset];
      final int c2 = 0x00FFFF & hex[j + offset + 1];
      if (c1 > 255 || c2 > 255) // out of range
      {
        // b[i+boff] = 0x00;
        throw new NumberFormatException();
      } else {
        final byte b1 = b1_[c1];
        final byte b2 = b1_[c2];
        if (b1 == 0x11 || b2 == 0x11) // out of range
        {
          // b[i+boff] = 0x00;
          throw new NumberFormatException();
        } else {
          final byte hi = (byte) (b1 << 4);
          b[i + boff] = (byte) (hi + b2);
        }
      }
    }
    return b.length;
  }

  protected boolean isProxy() {
    return (proxy_ != null && (!proxy_.equals("")));
  }

  protected void validateCheckdir() {
    File directory = new File(CHECKDIR);
    if (!directory.exists()) {
      directory.mkdirs();
    }
  }

  //
  // Checks to see if a long running test should be run.
  // A test should be run based on the probability and the age
  // A probability of 6 means a 1/6 chance of being run if the
  // test has not been run within the age in days.
  // A test that previouly failed should be run.
  //
  // Returns true if the test should be run.
  // Returns false if the test should be run and the testcase is marked as
  // successful after printing out a warning message.
  //
  // All test that use checkLongRunning should also use finishLongRunning to
  // indicate if the test passed or failed.

  public boolean checkLongRunning(String testname, int variation, int probability,
      int ageInDays) {
    validateCheckdir();
    File checkFile = new File(CHECKDIR + "/" + testname + "." + variation
        + ".failed");
    if (checkFile.exists()) {
      checkFile.delete();
      return true;
    }

    File runFile = new File(CHECKDIR + "/" + testname + "." + variation
        + ".run");
    if (runFile.exists()) {
      long runFileTime = runFile.lastModified();
      if (runFileTime > System.currentTimeMillis()
          - (ageInDays * 24 * 3600000L)) {
        System.out.println("Skipping: Variation " + testname + "." + variation
            + " last ran more than " + ageInDays + " days ago");
        assertCondition(true);
        return false;
      }
    }

    if (seed == 0) {
      seed = (int) (System.currentTimeMillis() / 60000);
    }

    if (((variation + seed) % probability) == 0) {
      return true;
    } else {
      System.out.println("Skipping: Variation " + testname + "." + variation
          + " probability = 1/" + probability);
      assertCondition(true);
      return false;
    }

  }

  
  // Check to make sure the tests is not on a group test System
  static String hostname = null; 
  public boolean isNotGroupTest() { 
    if (hostname == null) { 
      try { 
      hostname = JDHostName.getHostName().toUpperCase();
      } catch (Exception e) { 
        System.out.println("Warning:  checkNotGroupTest had failure");
        e.printStackTrace(System.out); 
        hostname="UNKNOWN"; 
      }
    }
    if (hostname.indexOf("GT73P2")>= 0) {
      return false;
    } else if (hostname.indexOf("Z1235P1")>= 0) {
      return false;
    } else if (hostname.indexOf("UT52P16")>= 0) {
      return false;
    } else {
      return true;
    }
  }
  public boolean checkNotGroupTest() {
    if (isNotGroupTest()) {
      System.out.println("System ("+hostname+") is not group test system");
      return true; 
    } else { 
      succeeded("Not running test because system is group test system");
      return false;
    }  
  }
  public boolean checkNative() { 
    
	  if (isNative_) {
		  return true; 
	  } else {
		  notApplicable("Native test"); 
		  return false; 
	  }
  }

  public boolean check750plus() {
    if (release_ > JDTestDriver.RELEASE_V7R5M0) {
      return true;
    } else {
      notApplicable("Test for release after 7.5");
      return false;
    }
  }

  public void createFile(File file) {
    try {
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(0xeb);
      fos.close();
    } catch (Exception e) {
      System.out.println("WARNING:  unexpected exception");
      e.printStackTrace(System.out);
    }

  }

  public void finishLongRunning(String testname, int variation, boolean passed) {
    validateCheckdir();
    File checkFile = new File(CHECKDIR + "/" + testname + "." + variation
        + ".failed");
    File runFile = new File(CHECKDIR + "/" + testname + "." + variation
        + ".run");
    if (passed) {
      createFile(runFile);
    } else {
      createFile(checkFile);
    }

  }

  class MethodComparator implements Comparator<Method> {
    /*
     * Compares its two arguments for order. Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to, or
     * greater than the second.
     */
    public int compare(Method m1, Method m2) {
        String name1 = m1.getName();
        String name2 = m2.getName();
        return name1.compareTo(name2);
    }
  }

  /**
   * Does the variation apply. This provides a way to testcases to determine if
   * the variation does not apply. Primarily used by JDTestcase and the jtopen
   * lite driver. Note: The current variation can be found using the following
   * methods getVariation() -- variation number (int) getName() -- testcase name
   * (String)
   * 
   */
  public boolean checkVariationApplies() {
    return true;
  }

  /**
   * Returns the AS/400 release being tested.
   * 
   * @return The AS/400 release.
   **/
  public int getRelease() {
    return release_;
  }

  public String showStringAsHex(String x) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < x.length(); i++) {
      int c = 0xFFFF & x.charAt(i);
      String s = Integer.toHexString(c);
      int sLength = s.length();
      while (sLength < 4) {
        sb.append("0");
        sLength++;
      }
      sb.append(s);
    }

    return sb.toString();

  }

  /**
   * Sets the test driver.
   * 
   * @param testDriver
   *          The test driver.
   **/
  public void setBaseTestDriver(TestDriverI baseTestDriver) {
    baseTestDriver_ = baseTestDriver;
  }

  public void setAsp(String asp) {
    asp_ = asp;
  }

  public String getAsp() {
    return asp_;
  }

  public static String deleteLibrary(CommandCall cmd, String library) {
    return TestDriver.deleteLibrary(cmd, library);
  }

  public static String dumpFile(String filename) {
    StringBuffer sb = new StringBuffer();
    try {
      sb.append("Contents of " + filename + "\n");
      sb.append("-----------------------------\n");
      FileReader fr = new FileReader(filename);
      BufferedReader br = new BufferedReader(fr);
      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        sb.append("\n");
        line = br.readLine();
      }
      br.close();

      sb.append("-----------------------------\n");

    } catch (Exception e) {
      sb.append("...dumpFile(" + filename + ") caught exception " + e + "\n");
    }

    return sb.toString();
  }

  public static String getLoadPath(Object thisObject) {
    return getLoadPath(thisObject.getClass());
  }

  public static String getLoadPath(Class<? extends Object> thisClass) {

    String loadPath = null;
    try {
      String className = thisClass.getName();
      ClassLoader loader = thisClass.getClassLoader();
      if (loader == null) {
        loadPath = "bootstrapClassLoader:";
        // Should have been the bootstrap classloader
        loader = ClassLoader.getSystemClassLoader();
      } else {
        loadPath = loader + ":";
      }
      if (loader != null) {
        String resourceName = className.replace('.', '/') + ".class";
        java.net.URL resourceUrl = loader.getResource(resourceName);
        if (resourceUrl != null) {
          loadPath += resourceUrl.getPath();
          // Note: The following logic strips the entry name from the end of the
          // path.
          // int delimiterPos = loadPath.lastIndexOf('!');
          // if (delimiterPos != -1) loadPath = loadPath.substring(0,
          // delimiterPos);
        } else {
          loadPath += "ResourceNotFound";
        }
      }
    } catch (Throwable t) {
      System.out
          .println("Warning: test.Testcase:  unable to determine load path for class "
              + thisClass);
    }

    return loadPath;

  }

  public static void printStackTraceToStringBuffer(Exception e, StringBuffer sb) {
    printStackTraceToStringBuffer((Throwable) e, sb);
  }

  public static void printStackTraceToStringBuffer(Throwable e, StringBuffer sb) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    e.printStackTrace(printWriter);
    String exception = stringWriter.toString();
    sb.append(exception);

  }

  /* Generate a key given a 5 character base */
  public static String generateClientUser(String base) throws Exception {
    if (base.length() > 5) {
      throw new Exception("base='" + base + "' must be less than 6 characters");
    }
    int intKey1 = InetAddress.getLocalHost().hashCode() % 60466175;
    String stringKey1 = Integer.toString(intKey1, 36);
    return base + stringKey1;
  }

  /* lock the system so that the testcase can only be run by one client */
  String getDtaaraName() {
    String[][] shortenString = { { "Permission", "PERM" }, };

    String userSpaceName = "LK" + name_;
    for (int i = 0; i < shortenString.length; i++) {
      String[] thisCheck = shortenString[i];
      String from = thisCheck[0];
      int fromIndex = userSpaceName.indexOf(from);
      if (fromIndex >= 0) {
        userSpaceName = userSpaceName.substring(0, fromIndex) + thisCheck[1]
            + userSpaceName.substring(fromIndex + from.length());
      }
    }
    return userSpaceName.substring(0, 10);
  }

  public void lockSystem(int lockSeconds) throws Exception {
    /* Get name for user space */
    String dtaaraName = getDtaaraName();
    lockSystem(dtaaraName, lockSeconds);

  }

  public void lockSystem(String lockDtaaraName, int lockSeconds)
      throws Exception {
    lockDtaaraName_ = lockDtaaraName;
    AS400 system;
    if (pwrSys_ != null) {
      system = pwrSys_;
    } else if (systemObject_ != null) {
      system = systemObject_;
    } else {
      throw new Exception("system object not defined for testcase");
    }

    CommandCall commandCall = new CommandCall(system);

    /* Make sure that the data area exists */
    IFSFile dataAreaFile = new IFSFile(system, "/QSYS.LIB/JDTESTINFO.LIB/"
        + lockDtaaraName_ + ".DTAARA");
    boolean dataAreaFileExists = false;
    boolean retry = true;
    long endTime = System.currentTimeMillis() + lockSeconds * 1000;
    while (retry) {

      retry = false;
      // Note: dataAreaFile.exists throws "File in use." error
      try {
        dataAreaFileExists = dataAreaFile.exists();
      } catch (Exception e) {
        String message = e.toString();
        if (message.indexOf("in use") >= 0) {
          if (System.currentTimeMillis() < endTime) {
            Thread.sleep(5000);
            retry = true;
          } else {
            throw e;
          }
        } else {
          throw e;
        }
      }
    }

    if (!dataAreaFileExists) {
      /* need to create collection */
      String command = "CRTDTAARA JDTESTINFO/" + lockDtaaraName_
          + "  TYPE(*CHAR) LEN(400)  ";
      boolean success = commandCall.run(command);
      if (!success) {
        AS400Message[] messageList = commandCall.getMessageList();
        if (messageList != null & messageList.length > 0) {
          throw new Exception("Command " + command + " failed with "
              + messageList[0]);
        } else {
          throw new Exception("Command " + command
              + " failed with no error message");
        }
      } else {
    	  /* Need to grant authority */ 
    	  command = "GRTOBJAUT OBJ(JDTESTINFO/" + lockDtaaraName_
    	  		+  ") OBJTYPE(*DTAARA) USER(*PUBLIC) AUT(*ALL)"; 
    	  success = commandCall.run(command);
    	  AS400Message[] messageList = commandCall.getMessageList();
          if (messageList != null & messageList.length > 0) {
            throw new Exception("Command " + command + " failed with "
                + messageList[0]);
          } else {
            throw new Exception("Command " + command
                + " failed with no error message");
          }
      }
    }

    long lockEndTime = System.currentTimeMillis() + lockSeconds * 1000;
    boolean success = false;
    String command = ""; 
    while (!success && System.currentTimeMillis() < lockEndTime) { 
       /* Lock the user space */
       command = " ALCOBJ OBJ((JDTESTINFO/" + lockDtaaraName_
          + " *DTAARA *EXCLRD)) WAIT(1)";
    
       success = commandCall.run(command);
       
       if (!success) { 
    	   AS400Message[] messageList = commandCall.getMessageList(); 
    	   
    	   if (messageList.length > 0) {
    		   if (messageList[0].getID().equals("CPF0991")) {  /* authority error */ 
    			 throw new Exception("Unable to access JDTESTINFO/"+lockDtaaraName_+". Please check permissions");    
    		   }
    	   }
           // Find the job holding the lock and delete it if the job 
           // is more than an hour old. 
           ObjectDescription od = new ObjectDescription(system,"/QSYS.LIB/JDTESTINFO.LIB/"+lockDtaaraName_+".DTAARA");  
           ObjectLockListEntry[] objectLockList = od.getObjectLockList(); 
           for (int i = 0; i < objectLockList.length; i++) {
             ObjectLockListEntry entry = objectLockList[i]; 
             if (entry.getLockStatus() == ObjectLockListEntry.LOCK_STATUS_LOCK_HELD) { 
                String jobname=entry.getJobNumber()+"/"+entry.getJobUserName()+"/"+entry.getJobName();
                System.out.println("/QSYS.LIB/JDTESTINFO.LIB/"+lockDtaaraName_+".DTAARA locked by "+jobname); 
                Job job = new Job(system, entry.getJobName(), entry.getJobUserName(), entry.getJobNumber());
                JobLog joblog = job.getJobLog(); 
                QueuedMessage[] messages = joblog.getMessages(-1, 999999); 

                QueuedMessage lastMessage = messages[messages.length-1]; 
                
                Calendar date = lastMessage.getDate();
                Timestamp ts = new Timestamp(date.getTime().getTime()); 
                System.out.println("lastMessage date = "+ts); 
                Calendar signonDate = system.getSignonDate(); 
                Timestamp signonTs = new Timestamp(signonDate.getTime().getTime()); 
                
                System.out.println("signonDate = "+signonTs); 
                long sentMillis = date.getTimeInMillis(); 
                long signonMillis = signonDate.getTimeInMillis();
                long elapsedMillis = signonMillis  - sentMillis; 
                if ( elapsedMillis > 3600 * 1000 ) {
                  // If longer than 1 hour -- Let's kill the job 
                  job.end(0);   
                  i=objectLockList.length; 
                }
                Thread.sleep(10000);
             }
           }
         
       }
    }
    if (!success) {

      // QSYSObjectPathName path = new QSYSObjectPathName("JDTESTINFO",	lockDtaaraName_, "DTAARA");
      // CharacterDataArea dataArea = new CharacterDataArea(system, path.getPath());
      // String data = dataArea.read().trim();
      CharacterDataArea dataArea = new CharacterDataArea(systemObject_, "/QSYS.LIB/JDTESTINFO.LIB/"+lockDtaaraName_+".DTAARA");
      // Read from the data area.
      String data = JDReflectionUtil.callMethod_S(dataArea, "read").trim();
      System.out.println(" DATA AREA JDTESTINFO/" + lockDtaaraName
			 + " contains " + data);

      AS400Message[] messageList = commandCall.getMessageList();
      if (messageList != null & messageList.length > 0) {
	  String info = messageList[0].toString();
	  if (info.indexOf("CPF1002") < 0) { 
	      throw new Exception("Command " + command + " failed with "
				  + messageList[0]);
	  } else {
	      // Keep trying 
	  }
      } else {
	  throw new Exception("Command " + command
			      + " failed with no error message");
      }
    } else {
      try {
        command = "TC_" + this.getClass().getName();
        commandCall.run(command);
      } catch (Exception e) {
        // ignore
      }
      try {

        command = "CHGDTAARA DTAARA(JDTESTINFO/" + lockDtaaraName_
            + " (1 50)) VALUE('" + command + "')";
        // System.out.println("Attempting "+command);
        commandCall.run(command);
      } catch (Exception e) {
        // ignore
      }
      try {

        command = "CHGDTAARA DTAARA(JDTESTINFO/" + lockDtaaraName_
            + " (51 100)) VALUE('" + testLib_ + "')";
        // System.out.println("Attempting "+command);
        commandCall.run(command);
      } catch (Exception e) {
        // ignore
      }

      try {
        command = "ON_" + JDHostName.getHostName();
        commandCall.run(command);
      } catch (Exception e) {
        // ignore
      }
      try {
        command = "CHGDTAARA DTAARA(JDTESTINFO/" + lockDtaaraName_
            + " (101 150)) VALUE('" + command + "')";
        // System.out.println("Attempting "+command);
        commandCall.run(command);
      } catch (Exception e) {
        // ignore
      }

      try {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        command = "CHGDTAARA DTAARA(JDTESTINFO/" + lockDtaaraName_
            + " (151 200)) VALUE('AT_" + currentTimestamp + "')";
        // System.out.println("Attempting "+command);
        commandCall.run(command);
      } catch (Exception e) {
        // ignore
      }

    }

  }

  public void unlockSystem() throws Exception {
    AS400 system;
    if (pwrSys_ != null) {
      system = pwrSys_;
    } else if (systemObject_ != null) {
      system = systemObject_;
    } else {
      throw new Exception("system object not defined for testcase");
    }

    CommandCall commandCall = new CommandCall(system);

    String command;

    try {
      command = "CHGDTAARA DTAARA(JDTESTINFO/" + lockDtaaraName_
          + " (1 200)) VALUE('UNUSED')";
      commandCall.run(command);
    } catch (Exception e) {
      // ignore
    }
    command = " DLCOBJ OBJ((JDTESTINFO/" + lockDtaaraName_
        + " *DTAARA *EXCL)) ";
    boolean success = commandCall.run(command);
    if (!success) {
      AS400Message[] messageList = commandCall.getMessageList();
      if (messageList != null & messageList.length > 0) {
        throw new Exception("Command " + command + " failed with "
            + messageList[0]);
      } else {
        throw new Exception("Command " + command
            + " failed with no error message");
      }
    }
  }
  
  /** 
   * Check to see if the test is running in attended mode
   */
  protected boolean checkAttended() { 
    if (runMode_ == UNATTENDED) {
      notApplicable("ATTENDED TESTCASE");
      return false; 
    } else { 
      return true; 
    }
  }
  /**
   * Checks if the system supports the additional authentication factor. If not, this will report "not
   * applicable".
 * @throws AS400SecurityException 
 * @throws IOException 
   **/
  protected boolean checkAdditionalAuthenticationFactor(String systemname)  {
	boolean supported = false; 
	try {
		supported = JDReflectionUtil.callStaticMethod_B("com.ibm.as400.access.AS400", "isAdditionalAuthenticationFactorAccepted", systemname);
	} catch (Exception e) {
		System.out.println("Unexpected exception"); 
		e.printStackTrace(System.out);
	} 
    if (!supported) {
      notApplicable("Additional Authentication Factor variation.");
      return false;
    } else
      return true;
  }

  /** Check if password leak testing if being done.  This is used by testcases that leak passwords to Strings
   * 
   */
  protected boolean checkPasswordLeak() { 
	 if (TestDriver.checkPasswordLeak) {
	      notApplicable("Password Leak Testing In Progress.");
	      return false;
	 } else {
		 return true; 
	 }
  }
  public void initMfaUser() throws Exception { 
	  if (!mfaInitialized) {
		  // For this to be used,  googleauth-1.5.0.jar and commons-codec-1.16.0.jar must in the classpath.
		  // The authentication information is read from ini/netrc.ini and must match the configuration
		  // on the system. 
	    try { 
		  googleAuthenticator_ = JDReflectionUtil.createObject("com.warrenstrange.googleauth.GoogleAuthenticator");
	    } catch (Exception e) { 
	       // On the IBM i the default SUN:SHA1PRNG generator required by GoogleAuthenticator is not available.
	      // switch to the default one.
	      String strongAlgorithms = java.security.Security.getProperty("securerandom.strongAlgorithms"); 
	      if (strongAlgorithms != null) { 
	        int commaIndex = strongAlgorithms.indexOf(","); 
	        if (commaIndex >= 0) {
	          strongAlgorithms = strongAlgorithms.substring(0,commaIndex);
	        }
	        // Get the algorithm / provider combination if possible
	        int colonIndex = strongAlgorithms.indexOf(":"); 
	        if (colonIndex >= 0) { 
	          String provider = strongAlgorithms.substring(colonIndex+1); 
	          System.setProperty("com.warrenstrange.googleauth.rng.algorithmProvider", provider);
	          strongAlgorithms = strongAlgorithms.substring(0,colonIndex); 
	        }
	        System.setProperty("com.warrenstrange.googleauth.rng.algorithm", strongAlgorithms);
	      }
	      // Try to create again
	      googleAuthenticator_ = JDReflectionUtil.createObject("com.warrenstrange.googleauth.GoogleAuthenticator");
	    }
		  StringBuffer iniInfo = new StringBuffer();
		  Properties properties = new Properties(); 
	      InputStream fileInputStream = JDRunit.loadResource("ini/netrc.ini", iniInfo);
	      properties.load(fileInputStream);
	      fileInputStream.close();

	      mfaUserid_ = properties.getProperty("MFAUSERID"); 
	      String password  = properties.getProperty("MFAPASSWORD");
	      if (password != null) { 
	    	  mfaEncryptedPassword_ = PasswordVault.getEncryptedPassword(password); 
	      } else {
	    	  mfaEncryptedPassword_ = null; 
	      }
	      mfaSecret_ = properties.getProperty("MFASECRET");
	      
	      if (mfaUserid_ == null)   throw new Exception("MFAUSERID not in netrc.ini"); 
	      if (mfaEncryptedPassword_ == null) throw new Exception("MFAPASSWORD not in netrc.ini"); 
	      if (mfaSecret_ == null)   throw new Exception("MFASECRET not in netrc.ini"); 

		  mfaInitialized=true; 
	  }
	  // Make sure that MFA is reset 
	  if (systemName_ == null) { 
		  systemName_ = systemObject_.getSystemName(); 
	  }
	  AS400 authAs400 = new AS400(systemName_,pwrSysUserID_, PasswordVault.decryptPassword(pwrSysEncryptedPassword_)); 
	  CommandCall cc = new CommandCall(authAs400); 
	  String command = "CHGUSRPRF "+mfaUserid_+" TOTPOPTITV(*NONE)   ";
	  cc.run(command); 
	  command = "CHGUSRPRF "+mfaUserid_+" TOTPOPTITV(1)   ";
	  mfaIntervalSeconds_ = 60; 
	  cc.run(command); 
	  String mfaFactorString = ""+JDReflectionUtil.callMethod_I(googleAuthenticator_,  "getTotpPassword", mfaSecret_);
	  while (mfaFactorString.length() < 6) {
		  mfaFactorString = "0"+mfaFactorString; 
	  }
	  mfaFactor_ = mfaFactorString.toCharArray(); 
	  
  }
}
