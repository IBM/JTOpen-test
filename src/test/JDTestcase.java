///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;
import com.ibm.as400.access.AS400JDBCTimestamp;
import com.ibm.as400.access.IFSFile;

import test.JD.JDTestUtilities;

import java.io.*;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingException;

/**
 * The JDTestcase class is a superclass for all JDBC testcase.
 **/
public class JDTestcase extends Testcase {

  // Common exceptions that we can expect
  //
  // Private data.
  protected Connection connection_;

  public static String[] dataTypeMismatchExceptions = { "Data type mismatch", "Parameter type not valid",
      "Data truncation" };

  public static String[] parameterDoesNotExistExceptions = { "An undefined column name was detected",
      "Descriptor index not valid", };

  public static String[] statementClosedExceptions = { "Function sequence error", "STATEMENT CLOSED", };

  public static String[] parameterTypeNotValidExceptions = { "Parameter type not valid", "Data type mismatch", };

  // Protected data.
  protected String baseURL_;
  protected String system_;
  // protected String userId_;

  // Private data.
  private static int jdbcLevel_;
  private static int toolboxDriverMinorVersion_;

  private boolean largeDecimalPrecisionSupport_;
  private boolean bigintSupport_;
  private boolean xmlSupport_;
  private boolean arraySupport_;
  private boolean timestamp12Support_;
  private boolean booleanSupport_;
  // private boolean collections_;
  private int driver_;
  private int subDriver_;
  private int databaseType_;
  private boolean lobSupport_;
  private boolean datalinkSupport_;
  private boolean savepointSupport_;
  private boolean namedParameterSupport_;
  private boolean cursorHoldabilitySupport_;
  private boolean multipleOpenResultSetSupport_;
  private boolean generatedKeySupport_;
  private boolean updateableLobsSupport_;
  // private String name_;
  private int jdk_ = 0;
  private boolean returnValueSupport_;
  private boolean decFloatSupport_;
  protected JDTestDriver testDriver_;
  protected String collection_;
  private long driverFixLevel_ = 0;
  private long driverFixDate_ = 0;
  static Object timeUnitSeconds = null;
  protected boolean toolboxNative = false;

  protected JDSupportedFeatures supportedFeatures_;

  private boolean isOpenJdk = false;

  private String javaHome = null;

  /**
   * Static initializer.
   **/
  static {
    // Test for JDBC 3.0
    try {
      Class.forName("java.sql.Savepoint");
      jdbcLevel_ = 30;

      // Test for JDBC 4.0
      try {
        Class.forName("java.sql.SQLXML");
        jdbcLevel_ = 40; // Note: for toolbox, we also do a non-static check in
                         // run() for the jt400.jar version

        try {
          // Test for JDBC 4.1
          Class.forName("java.sql.PseudoColumnUsage");
          jdbcLevel_ = 41;
          try {
            // Note java.sql.SQLType is in JDK 1.8, as is java.time.Localtime
            Class.forName("java.sql.SQLType");
            jdbcLevel_ = 42;

          } catch (ClassNotFoundException e) {
            jdbcLevel_ = 41;
          }
        } catch (ClassNotFoundException e) {
          jdbcLevel_ = 40;
        }

      } catch (ClassNotFoundException e) {

        String fakeJDK16 = System.getProperty("jdbc.db2.forceJDK16");
        if (fakeJDK16 != null) {
          jdbcLevel_ = 40;
        } else {

          jdbcLevel_ = 30;
        }
      }

    } catch (ClassNotFoundException e) {
      jdbcLevel_ = 20;
    }
    System.out.println("jdbcLevel: " + jdbcLevel_);
    // System.out.println("java.class.path:
    // "+System.getProperty("java.class.path"));

    try {
      timeUnitSeconds = JDReflectionUtil.getStaticField_O("java.util.concurrent.TimeUnit", "SECONDS");
    } catch (Exception e) {
    }

  }

  /**
   * Constructor.
   **/

  public JDTestcase(AS400 systemObject, String testcaseName, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, String password) {
    super(systemObject, testcaseName, -1,  namesAndVars.get(testcaseName), runMode, fileOutputStream, password);

    initializeFields(testcaseName);

  }

  /**
   * Constructor.
   **/
  public JDTestcase(AS400 systemObject, String testcaseName, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, String password, String pwrSysUid, String pwrSysPwd) {
    super(systemObject, testcaseName, -1, namesAndVars.get(testcaseName), runMode, fileOutputStream, password,
        pwrSysUid, pwrSysPwd);

    initializeFields(testcaseName);

  }

  private void initializeFields(String testcaseName) {
    baseURL_ = null;
    largeDecimalPrecisionSupport_ = false;
    bigintSupport_ = false;
    xmlSupport_ = false;
    arraySupport_ = false;
    timestamp12Support_ = false;
    booleanSupport_ = false;
    // collections_ = true;
    driver_ = JDTestDriver.DRIVER_NONE;
    subDriver_ = JDTestDriver.DRIVER_NONE;
    databaseType_ = JDTestDriver.DB_SYSTEMI;
    endIfSetupFails_ = false;
    release_ = JDTestDriver.RELEASE_NONE;
    lobSupport_ = false;
    datalinkSupport_ = false;
    savepointSupport_ = false;
    namedParameterSupport_ = false;
    cursorHoldabilitySupport_ = false;
    multipleOpenResultSetSupport_ = false;
    generatedKeySupport_ = false;
    updateableLobsSupport_ = false;
    returnValueSupport_ = false;
    decFloatSupport_ = false;
    name_ = testcaseName;
    system_ = systemObject_.getSystemName();
    testDriver_ = null;
    totalVariations_ = countVariations();
    userId_ = systemObject_.getUserId();
    collection_ = null;
    jdk_ = JVMInfo.getJDK();

  }

  protected boolean areLargeDecimalPrecisionsSupported() {
    return largeDecimalPrecisionSupport_;
  }

  protected boolean areBigintsSupported() {
    return bigintSupport_;
  }

  protected boolean areDecfloatsSupported() {
    return decFloatSupport_;
  }

  protected boolean areBooleansSupported() {
    return booleanSupport_;
  }

  protected boolean isXmlSupported() {
    return xmlSupport_;
  }

  protected boolean areArraysSupported() {
    return arraySupport_;
  }

  protected boolean isTimestamp12Supported() {
    return timestamp12Support_;
  }

  /**
   * Indicates if savepoints are supported on the server release
   **/
  protected boolean areSavepointsSupported() {
    return savepointSupport_;
  }

  /**
   * Indicates if named parameters are supported on the server release
   **/
  protected boolean areNamedParametersSupported() {
    return namedParameterSupport_;
  }

  /**
   * Indicates if cursor holdability is supported on the server release
   **/
  protected boolean areCursorHoldabilitySupported() {
    return cursorHoldabilitySupport_;
  }

  /**
   * Indicates if multiple open result sets are supported on the server release
   **/
  protected boolean areMultipleOpenResultSetsSupported() {
    return multipleOpenResultSetSupport_;
  }

  /**
   * Indicates if generated keys are supported on the server release
   **/
  protected boolean areGeneratedKeysSupported() {
    return generatedKeySupport_;
  }

  /**
   * Indicates if updateable lobs are supported on the server release
   **/
  protected boolean areUpdateableLobsSupported() {
    return updateableLobsSupport_;
  }

  /**
   * Indicates if lobs are supported on the server release.
   **/
  protected boolean areLobsSupported() {
    return lobSupport_;
  }

  protected boolean areReturnValuesSupported() {
    return returnValueSupport_;
  }

  /**
   * Checks if JDBC 2.0 is available. Always returns true since the testcases are
   * no long run on older JVMS.
   **/
  // @Deprecated
  protected boolean checkJdbc20() {
    return true;
  }

  /**
   * Checks if JDBC 2.0 Standard Extension is available. Always returns true since
   * the testcases are no long run on older JVMS.
   **/
  // @Deprecated
  protected boolean checkJdbc20StdExt() {
    return true;
  }

  /**
   * Checks if JDBC 3.0 is available. If not, this will report "not applicable".
   **/

  protected boolean checkJdbc30() {
    boolean jdbcLevel3 = (jdbcLevel_ >= 30);
    if (!jdbcLevel3)
      notApplicable("JDBC 3.0 variation.");
    return jdbcLevel3;
  }

  /**
   * Checks if JDBC 4.0 is available. If not, this will report "not applicable".
   **/
  protected boolean checkJdbc40() {
    boolean jdbcLevel4 = (jdbcLevel_ >= 40);
    if (!jdbcLevel4)
      notApplicable("JDBC 4.0 variation.");
    return jdbcLevel4;
  }

  /**
   * Checks if JDBC 4.1 is available. If not, this will report "not applicable".
   **/
  protected boolean checkJdbc41() {
    boolean jdbcLevel4 = (jdbcLevel_ >= 41);
    if (!jdbcLevel4)
      notApplicable("JDBC 4.1 variation.");
    return jdbcLevel4;
  }

  /**
   * Checks if JDBC 4.2 is available. If not, this will report "not applicable".
   **/
  protected boolean checkJdbc42() {
    boolean jdbcLevel42 = (jdbcLevel_ >= 42);
    if (!jdbcLevel42)
      notApplicable("JDBC 4.2 variation.");
    return jdbcLevel42;
  }

  /**
   * Checks to see if the testcase is running on 400
   **/
  protected boolean checkClientOn400() {
    if (JTOpenTestEnvironment.isOS400) {
      return true;
    }
    notApplicable("on400 test: currently on " + JTOpenTestEnvironment.osVersion);
    return false;
  }

  /**
   * Checks if we are using the native driver with a native JVM. If not, this will
   * report "not applicable". B1A
   **/
  public boolean checkNative() {
    if ((driver_ != JDTestDriver.DRIVER_NATIVE) && (driver_ != JDTestDriver.DRIVER_NATIVE_RMI)) {
      notApplicable("JDBC Native Driver variation.");
      return false;
    } else {
      if (javaHome == null) {
        javaHome = System.getProperty("java.home");
        if (javaHome.indexOf("openjdk") >= 0) {
          isOpenJdk = true;
        }
      }
      if (isOpenJdk) {
        notApplicable("JDBC Native Driver variation. Not run for OpenJdk");
        return false;
      } else {
        return true;
      }
    }
  }

  /**
   * Checks if we are using the toolbox driver. If not, this will report "not
   * applicable".
   **/
  protected boolean checkToolbox() {
    if ((driver_ != JDTestDriver.DRIVER_TOOLBOX)) {
      notApplicable("Toolbox JDBC Driver variation.");
      return false;
    } else
      return true;
  }

  /**
   * Checks if we are using the toolbox driver with at least the specified driver
   * fix date. The fix date is a long in the form YYYYMMDD. If not, this will
   * report "not applicable".
   **/
  protected boolean checkToolboxFixDate(long driverFixDate) {
    if ((driver_ != JDTestDriver.DRIVER_TOOLBOX)) {
      notApplicable("Toolbox JDBC Driver variation.");
      return false;
    } else if (getDriverFixDate() < driverFixDate) {
      notApplicable("Toolbox JDBC Driver (fixDate " + getDriverFixDate() + "<" + driverFixDate + ") variation.");
      return false;
    } else {
      // System.out.println("Toolbox JDBC Driver Running (fixDate
      // "+getDriverFixDate()+">="+driverFixDate+") variation.");

      return true;
    }
  }

  protected boolean isToolboxFixDate(long driverFixDate) {
    if ((driver_ != JDTestDriver.DRIVER_TOOLBOX)) {
      return false;
    } else if (getDriverFixDate() < driverFixDate) {
      return false;
    } else {
      // System.out.println("Toolbox JDBC Driver Running (fixDate
      // "+getDriverFixDate()+">="+driverFixDate+") variation.");
      return true;
    }
  }

  /**
   * Checks if we are using the jcc driver. If not, this will report "not
   * applicable".
   **/
  protected boolean checkJcc() {
    if ((driver_ != JDTestDriver.DRIVER_JCC)) {
      notApplicable("Toolbox JDBC Driver variation.");
      return false;
    } else
      return true;
  }

  protected boolean checkPropertySupport(String property) {
    if ((driver_ == JDTestDriver.DRIVER_JCC)) {
      notApplicable("JCC doesn't support property -- " + property);
      return false;
    } else
      return true;
  }

  public boolean isToolboxDriver() {
    return driver_ == JDTestDriver.DRIVER_TOOLBOX;
  }

  protected boolean checkSystemI() {
    if ((isToolboxDriver())) {
      return true;
    } else {
      if (JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_SYSTEMI) {
        return true;
      } else {
        notApplicable("System I test only");
        return false;
      }
    }
  }

  protected boolean checkBigintSupport() {
    if (bigintSupport_ == false)
      notApplicable("BIGINT variation (>=V4R5).");
    return bigintSupport_;
  }

  protected boolean checkXmlSupport() /* @G1A */
  {
    if (xmlSupport_ == false)
      notApplicable("XML variation (>=V7R1).");
    return xmlSupport_;
  }

  protected boolean checkArraySupport() /* @G1A */
  {
    if (arraySupport_ == false)
      notApplicable("ARRAY variation (>=V7R1).");
    return arraySupport_;
  }

  protected boolean checkTimestamp12Support() {
    if (timestamp12Support_ == false)
      notApplicable("TIMESTAMP(12) variation (>=V7R1).");
    return timestamp12Support_;
  }

  protected boolean checkBooleanSupport() {
    if (areBooleansSupported() == false)
      notApplicable("BOOLEAN variation (>=V7R5).");
    return areBooleansSupported();
  }

  protected boolean checkLargeDecimalPrecisionSupport() {
    if (largeDecimalPrecisionSupport_ == false)
      notApplicable("Large DECIMAL/NUMERIC precision variation (>=V5R3).");
    return largeDecimalPrecisionSupport_;
  }

  /**
   * Checks if savepoints are supported on the server release. If not, this will
   * automatically report "not applicable".
   **/
  protected boolean checkSavepointSupport() {
    if (savepointSupport_ == false)
      notApplicable("Savepoint variation (>=V5R2).");
    return savepointSupport_;
  }

  /**
   * Checks if named parameters are supported on the server release. If not, this
   * will automatically report "not applicable".
   **/
  protected boolean checkNamedParametersSupport() {
    if (namedParameterSupport_ == false)
      notApplicable("Named Parameter variation (>=V5R2 and nonJcc).");
    return namedParameterSupport_;
  }

  /**
   * Checks if cursor holdability is supported ont the server release. If not,
   * this will automatically report "not applicable."
   **/
  protected boolean checkCursorHoldabilitySupport() {
    if (cursorHoldabilitySupport_ == false)
      notApplicable("Cursor Holdability variation (>=V5R2).");
    return cursorHoldabilitySupport_;
  }

  /**
   * Checks if multiple open result sets are supported on the server release. If
   * not, this will automatically report "not applicable."
   **/
  protected boolean checkMultipleOpenResultSetSupport() {
    if (multipleOpenResultSetSupport_ == false)
      notApplicable("Multiple Open Result Set variation (>=V5R2).");
    return multipleOpenResultSetSupport_;
  }

  /**
   * Checks if generated keys are supported on the server release. If not, this
   * will automatically report "not applicable."
   **/
  protected boolean checkGeneratedKeySupport() {
    if (generatedKeySupport_ == false)
      notApplicable("Generated Key variation (>=V5R2).");
    return generatedKeySupport_;
  }

  /**
   * Checks if updateable lobs are supported on the server release. If not, this
   * will automatically report "not applicable."
   **/
  protected boolean checkUpdateableLobsSupport() {
    if (updateableLobsSupport_ == false)
      notApplicable("Updateable Lobs variation (>=JDK 1.4).");
    return updateableLobsSupport_;
  }

  /**
   * Checks if lobs are supported on the server release. If not, this will
   * automatically report "not applicable".
   **/
  protected boolean checkLobSupport() {
    if (lobSupport_ == false)
      notApplicable("Lob variation (>=V4R4).");
    return lobSupport_;
  }

  /**
   * Checks if datalinks are supported on the server release. If not, this will
   * automatically report "not applicable".
   **/
  protected boolean checkDatalinkSupport() {
    if (datalinkSupport_ == false)
      notApplicable("Datalink variation (>=V4R4).");
    return datalinkSupport_;
  }

  protected boolean checkReturnValueSupport() {
    if (returnValueSupport_ == false)
      notApplicable("Return value variation (>=V4R4).");
    return returnValueSupport_;
  }

  protected boolean checkDecFloatSupport() {
    if (decFloatSupport_ == false)
      notApplicable("Decimal Floating Point variation (>=V5R5).");
    return decFloatSupport_;
  }

  /**
   * dumpBytes() - Utility function used to see the bytes
   **/
  protected static String dumpBytes(byte[] b) {
    String s = "";
    for (int i = 0; i < b.length; i++) {
      String ns = Integer.toHexString(((int) b[i]) & 0xFF);
      if (ns.length() == 1) {
        s += "0" + ns;
      } else {
        s += ns;
      }
    }
    return s;
  }

  protected static boolean compareUnicodeStream(InputStream i, String b, StringBuffer sb)
      throws UnsupportedEncodingException {
    sb.append("Comparing to " + b + "\n");
    return compare(i, b.getBytes("UnicodeBigUnmarked"), sb);
  }

  protected static boolean compareAsciiStream(InputStream i, String b, StringBuffer sb)
      throws UnsupportedEncodingException {
    sb.append("Comparing to " + b + "\n");
    return compare(i, b.getBytes("UTF-8"), sb);
  }

  /**
   * Compares an InputStream with a byte[]. Files the string buffer with the input
   * stream output if there is an error.
   **/
  protected static boolean compare(InputStream i, byte[] b, StringBuffer sb) {
    boolean rc = false;
    try {
      byte[] buf = new byte[b.length];
      int num = i.read(buf, 0, buf.length);
      if (num == -1)
        return b.length == 0;
      int total = num;
      while (num > -1 && total < buf.length) {
        num = i.read(buf, total, buf.length - total);
        total += num;
      }
      if (num == -1)
        --total;

      // For some input streams i.available() will return 1 if data is
      // available.
      // Go beyond the end to make sure.
      int extraBytes = 0;
      while (i.available() > 0) {
        int x = i.read();
        if (x > 0) {
          extraBytes++;
        }
      }

      rc = (total == b.length && extraBytes == 0 && Testcase.areEqual(b, buf));

      if (!rc) {
        sb.append("\ntotal read=" + total + " should be " + b.length);
        sb.append("\nextraBytes=" + extraBytes + " should be 0+\n");
        JDTestUtilities.outputComparison(b/* expected */, buf, sb);
      }

      return rc;
    } catch (IOException e) {
      sb.append("Exception taken: " + e + "\n");

      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      e.printStackTrace(printWriter);
      String exception = stringWriter.toString();
      sb.append(exception);

      return false;
    }
    /*
     * try { byte[] b2 = new byte[b.length]; int l = i.read (b2, 0, b.length); if (l
     * == -1) return(b.length == 0); else return((l == b.length) && (i.available()
     * == 0) && (Testcase.isEqual (b, b2))); } catch (IOException e) { return false;
     * }
     */
  }

  /**
   * Compares an InputStream with a byte[]. Files the string buffer with the input
   * stream output if there is an error.
   **/
  protected static boolean compare(byte[] buf, byte[] b, StringBuffer sb) {
    boolean rc = false;

    rc = (Testcase.areEqual(b, buf));

    if (!rc) {
      JDTestUtilities.outputComparison(b/* expected */, buf, sb);

    }

    return rc;
  }

  /**
   * Compares an InputString with a String representing the bytes
   */
  protected boolean compare(InputStream i, String s, StringBuffer sb) {

    return compare(i, hexAsStringToBytes(s), sb);
  }

  protected byte hexDigit(char a) throws NumberFormatException {
    if (a >= '0' && a <= '9') {
      return (byte) (a - '0');
    } else if (a >= 'a' && a <= 'f') {
      return (byte) (a - 'a' + 10);
    } else {
      throw new NumberFormatException("Could not convert " + a + " to hexDigit");
    }
  }

  protected byte hexDigits(char a, char b) throws NumberFormatException {
    try {
      return (byte) (16 * hexDigit(a) + hexDigit(b));
    } catch (NumberFormatException nfe) {
      throw new NumberFormatException("Could not convert " + a + b + " to hexDigit");
    }

  }

  protected byte[] hexAsStringToBytes(String s) throws NumberFormatException {
    try {
      int length = s.length() / 2;
      byte[] output = new byte[length];

      for (int i = 0; i < length; i++) {
        output[i] = hexDigits(s.charAt(2 * i), s.charAt(2 * i + 1));
      }

      return output;
    } catch (NumberFormatException nfe) {
      throw new NumberFormatException("In " + s + " " + nfe.toString());
    }
  }

  /**
   * Compares an InputStream with a byte[].
   **/
  protected static boolean compareRemoved(InputStream i, byte[] b, boolean hexToAscii) {
    try {
      int divisor = 1;
      if (hexToAscii) // IF getAsciiStream was called on the ResultSet
      {
        // The InputStream is in hex so it is twice as big as the byte array.
        divisor = 2;
      } else // If getUnicodeStream was called on the ResultSet
      {
        // There are 4 hex characters to one Unicode character
        divisor = 4;
      }
      byte[] buf = new byte[b.length * divisor];
      int num = i.read(buf, 0, buf.length);
      if (num == -1)
        return b.length == 0;
      int total = num; // total is the length of the InputStream
      while (num > -1 && total < buf.length) {
        num = i.read(buf, total, buf.length - total);
        total += num;
      }
      if (num == -1)
        --total;
      return (total / divisor) == b.length && i.available() == 0 && Testcase.areEqual(b, buf, hexToAscii);
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Compares an InputStream with a byte[].
   **/
  protected static boolean compare(InputStream i, byte[] b, boolean hexToAscii, StringBuffer sb) {
    try {
      int divisor = 1;
      if (hexToAscii) // IF getAsciiStream was called on the ResultSet
      {
        // The InputStream is in hex so it is twice as big as the byte array.
        divisor = 2;
      } else // If getUnicodeStream was called on the ResultSet
      {
        // There are 4 hex characters to one Unicode character
        divisor = 4;
      }
      byte[] buf = new byte[b.length * divisor];
      int num = i.read(buf, 0, buf.length);
      if (num == -1)
        return b.length == 0;
      int total = num; // total is the length of the InputStream
      while (num > -1 && total < buf.length) {
        num = i.read(buf, total, buf.length - total);
        total += num;
      }
      if (num == -1)
        --total;
      boolean passed = true;
      if (!((total / divisor) == b.length)) {
        sb.append("total(" + total + ")+/devisor(" + divisor + ")) != b.length(" + b.length + ")\n");
        passed = false;
      }
      if (!(i.available() == 0)) {
        sb.append("i.available() == " + i.available() + "\n");
        passed = false;
      }
      if (!Testcase.areEqual(b, buf, hexToAscii, sb)) {
        passed = false;
      }
      return passed;
    } catch (IOException e) {
      return false;
    }
  }

  protected static boolean compareBeginsWithBytes(InputStream i, byte[] b, StringBuffer sb)
  // this compare doesn't check if inputStream has more bytes or not beyond
  // containing b
  // unlike JDTestcase.compare which see thats inputStream should contain only b
  // and nothing
  // beyond it.
  // this exclusion is esp. needed for native driver from v5r3m0 onwards
  {
    try {
      byte[] buf = new byte[b.length];
      int num = i.read(buf, 0, buf.length);
      if (num == -1) {
        if (b.length == 0) {
          return true;
        } else {
          sb.append("inputstream bytesRead=" + num + " expectedLength = 0\n");
          return false;
        }
      }
      int total = num;
      while (num > -1 && total < buf.length) {
        num = i.read(buf, total, buf.length - total);
        total += num;
      }
      if (num == -1)
        --total;
      if (total == b.length) {
        return Testcase.areEqual(b, buf, sb);
      } else {
        sb.append("inputstream bytesRead=" + total + " expectedLength = " + b.length + "\n");
        return false;
      }
    } catch (java.io.IOException e) {
      return false;
    }
  }

  /**
   * Compares an InputStream with a String. Return information in an string buffer
   * about the intput
   **/
  protected boolean compare(InputStream i, String s, String encoding, StringBuffer sb) {
    try {
      return (compare(i, s.getBytes(encoding), sb));
    } catch (java.io.UnsupportedEncodingException e) {
      return false;
    }
  }

  /**
   * Compares an InputStream with a String.
   * 
   * @param t if true, getAsciiStream was called, otherwise getUnicodeStream was
   *          called
   **/
  protected boolean compare(InputStream i, String s, String encoding, boolean t, StringBuffer sb) {
    try {
      return (compare(i, s.getBytes(encoding), t, sb));
    } catch (java.io.UnsupportedEncodingException e) {
      return false;
    }
  }

  /**
   * Compares a Reader with a String.
   **/
  protected static boolean compareRemoved(Reader r, String s) {
    try {
      int slength = s.length();
      if (s.length() == 0) {
        int lastRead = r.read();
        return ((lastRead == -1) || (lastRead == 0));
      }
      char[] c2 = new char[slength];
      StringBuffer buffer = new StringBuffer(slength);
      int lastRead = 0;
      while (true) {
        lastRead = r.read(c2);
        if ((lastRead == -1) || (lastRead == 0))
          break;
        buffer.append(new String(c2, 0, lastRead));
      }
      String s2 = buffer.toString();
      boolean passed = s2.equals(s);
      return (passed);
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Compares a Reader with a String.
   **/
  protected static boolean compare(Reader r, String s, StringBuffer sb) {
    try {
      String s2;
      if (r == null) {
        s2 = "null";
      } else {
        int slength = s.length();
        if (s.length() == 0) {
          int lastRead = r.read();
          sb.append("\nlastRead=" + lastRead + " sb -1 or 0");
          return ((lastRead == -1) || (lastRead == 0));
        }
        char[] c2 = new char[slength];
        StringBuffer buffer = new StringBuffer(slength);
        int lastRead = 0;
        while (true) {
          lastRead = r.read(c2);
          if ((lastRead == -1) || (lastRead == 0))
            break;
          buffer.append(new String(c2, 0, lastRead));
        }
        s2 = buffer.toString();
      }
      boolean rc = s2.equals(s);
      if (!rc) {
        sb.append("\nGot      : '" + s2 + "'");
        sb.append("\nExpected : '" + s + "'");

      }
      return (rc);
    } catch (IOException e) {
      printStackTraceToStringBuffer(e, sb);

      return false;
    }
  }

  /**
   * Compares a Blob with a byte[].
   **/
  protected boolean compare(Blob i, byte[] b, StringBuffer sb) throws SQLException {
    if (i == null) {
      if (b != null) {
        sb.append("BLOB is null");
        return false;
      } else {
        // both are null
        return true;
      }
    } else {
      if (b == null) {
        sb.append("BLOB is not null but array is null ");
        return false;
      } else {
        byte[] iBytes = i.getBytes(1, (int) i.length());
        return compare(iBytes, b, sb);
      }
    }
  }

  /**
   * Compares a Blob with a String.
   **/
  protected boolean compare(Blob i, String b, StringBuffer sb) throws SQLException {
    byte[] iBytes = i.getBytes(1, (int) i.length());
    return compare(iBytes, b, sb);
  }

  /**
   * Compares a byte array with a string
   */
  protected boolean compare(byte[] ba, String expectedValue, StringBuffer sb) {
    String returnString = "NULL";
    if (ba != null) {
      returnString = bytesToString(ba);
    }
    if (!returnString.equals(expectedValue)) {
      sb.append("Got " + returnString + " expected " + expectedValue + "\n");
      return false;
    } else {
      return true;
    }

  }

  /**
   * Compares a Reader with a byte array for getCharacterStream.
   **/
  protected boolean compareRemoved(Reader r, byte[] s) {
    try {
      // copy the Reader into a String
      int slength = s.length;
      if (s.length == 0) {
        int lastRead = r.read();
        return ((lastRead == -1) || (lastRead == 0));
      }
      char[] c2 = new char[slength];
      StringBuffer buffer = new StringBuffer(slength);
      int lastRead = 0;
      while (true) {
        lastRead = r.read(c2);
        if ((lastRead == -1) || (lastRead == 0))
          break;
        buffer.append(new String(c2, 0, lastRead));
      }
      String s2 = buffer.toString();
      // Convert the String to bytes
      byte[] b = Testcase.stringToBytes(s2);
      return Testcase.areEqual(b, s);
    } catch (IOException e) {
      return false;
    }
  }

  protected boolean compare(Reader r, byte[] s, StringBuffer sb) {
    try {
      // copy the Reader into a String
      int slength = s.length;
      if (s.length == 0) {
        int lastRead = r.read();
        return ((lastRead == -1) || (lastRead == 0));
      }
      char[] c2 = new char[slength];
      StringBuffer buffer = new StringBuffer(slength);
      int lastRead = 0;
      while (true) {
        lastRead = r.read(c2);
        if ((lastRead == -1) || (lastRead == 0))
          break;
        buffer.append(new String(c2, 0, lastRead));
      }
      String s2 = buffer.toString();
      // Convert the String to bytes
      byte[] b = Testcase.stringToBytes(s2);
      boolean rc = Testcase.areEqual(b, s);
      if (!rc) {
        JDTestUtilities.outputComparison(s/* expected */, b, sb);
      }
      return rc;
    } catch (IOException e) {
      return false;
    }
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
      if (name.substring(0, 3).equalsIgnoreCase("Var")) {
        int number = Integer.parseInt(name.substring(3));
        if (number > high)
          high = number;
      }
    }

    return high;
  }

  /**
   * Returns the JDBC driver being tested.
   * 
   * @return The JDBC driver.
   **/
  public int getDriver() {
    return driver_;
  }

  public int getSubDriver() {
    return subDriver_;
  }

  /**
   * Returns the database type.
   */
  public int getDatabaseType() {
    return databaseType_;
  }

  /**
   * Returns the fix date of driver as a long in the form YYYYMMDD. If not found,
   * then the date is set to 20010101
   * 
   * @return
   */
  public long getDriverFixDate() {
    if (driverFixDate_ == 0) {
      driverFixDate_ = 20010101;
      if (driver_ == JDTestDriver.DRIVER_NATIVE) {

      } else if (driver_ == JDTestDriver.DRIVER_TOOLBOX) {

        try {
          String versionString = (String) JDReflectionUtil.getStaticField_O("com.ibm.as400.access.Copyright",
              "version");

          // System.out.println("JDTestcase.getDriverFixDate
          // versionString="+versionString);
          /*
           * Possible formats are the following. Open Source Software, JTOpen 9.6,
           * codebase 5770-SS1 V7R3M0.00 built=20181031 @X5 Open Source Software, JTOpen
           * 20.0.0-alpha-1 (development build) codebase 5770-SS1 V7R5M0.00
           * built=2023-04-10 21:19:51 (GMT)
           * 
           */
          int builtIndex = versionString.indexOf("built=");
          if (builtIndex > 0) {

            // System.out.println("Date in version string is "+versionString.substring(
            // builtIndex + 6));
            String dateString;
            if (versionString.charAt(builtIndex + 10) == '-') {
              dateString = versionString.substring(builtIndex + 6, builtIndex + 10)
                  + versionString.substring(builtIndex + 11, builtIndex + 13)
                  + versionString.substring(builtIndex + 14, builtIndex + 16);
            } else {
              dateString = versionString.substring(builtIndex + 6, builtIndex + 6 + 8);
            }
            // System.out.println("Date string is "+dateString);
            driverFixDate_ = Long.parseLong(dateString);
          }
          /*
           * 
           * Class cls = Class.forName("com.ibm.as400.access.Copyright"); ClassLoader
           * loader = cls.getClassLoader(); if (loader != null) { String resourceName =
           * "com/ibm/as400/access/Copyright.class"; java.net.URL resourceUrl =
           * loader.getResource(resourceName); if (resourceUrl != null) {
           * System.out.println("JDTestcase loadPath ="+ resourceUrl.getPath()); } }
           * 
           * 
           */

        } catch (Throwable t) {
          System.out.println("Error getting version infomation");
          t.printStackTrace(System.out);
        }
      } else {
        // Keep the default date
      }
    }
    return driverFixDate_;

  }

  /**
   * Returns the fix level of the driver. Returns the PTF number for the valid
   * driver. Returns major * 100 + minor for toolbox version. I.e. JTOpen 7.2 =
   * 702.
   */

  public long getDriverFixLevel() {
    if (driverFixLevel_ == 0) {
      if (driver_ == JDTestDriver.DRIVER_NATIVE) {

        String j9Library = System.getProperty("jdbc.db2.j9.library");
        if (j9Library != null) {
          // Assume the most current level
          driverFixLevel_ = 999999;
          return driverFixLevel_;
        }

        Runtime rt = Runtime.getRuntime();

        String[] cmd = new String[3];
        String library = System.getProperty("jdbc.db2.native.library");
        if (library == null)
          library = "QSYSDIR";
        cmd[0] = "/usr/bin/qsh";
        cmd[1] = "-c";
        cmd[2] = "system dspobjd " + library
            + "/qjvajdbc '*srvpgm detail(*service)'  | grep -i 'PTF number' | sed 's/^.*SI//'";

        // System.out.println("Debug: Running "+cmd[2]);
        try {
          Process p = rt.exec(cmd);
          InputStream iStream = p.getInputStream();
          int readByte = iStream.read();
          while (readByte != -1) {
            // System.out.println(Integer.toHexString((int) readByte));
            if (((readByte >= 0xf0) && (readByte <= 0xf9)) || ((readByte >= 0x30) && (readByte <= 0x39))) {
              int value = readByte & 0xf;
              driverFixLevel_ = 10 * driverFixLevel_ + value;
            }
            readByte = iStream.read();
          }
          iStream.close();

        } catch (Throwable e) {
          e.printStackTrace(System.out);

          // check for spawnp failure
          String message = e.toString();
          if (message.indexOf("spawnp failure") >= 0) {
            System.out.println("Warning.. spawnp failed, setting driverFixLevel to 1");
            driverFixLevel_ = 1;
          }
        }

      } else if (isToolboxDriver()) {
        String versionString = com.ibm.as400.access.Copyright.version;
        int JTOpenIndex = versionString.indexOf("JTOpen");
        if (JTOpenIndex > 0) {
          JTOpenIndex += 7;
          int dotIndex = versionString.indexOf(".", JTOpenIndex);
          if (dotIndex > 0) {
            String majorVersionString = versionString.substring(JTOpenIndex, dotIndex);
            dotIndex += 1;
            int spaceIndex = versionString.indexOf(",", dotIndex);
            if (spaceIndex > 0) {
              String minorVersionString = versionString.substring(dotIndex, spaceIndex);
              int majorVersion = Integer.parseInt(majorVersionString);
              if (majorVersion > 0) {
                // Cleanup minorVersionString
                int cleanupIndex = minorVersionString.indexOf("+");
                if (cleanupIndex > 0) {
                  minorVersionString = minorVersionString.substring(0, cleanupIndex);
                }
                cleanupIndex = minorVersionString.indexOf("-");
                if (cleanupIndex > 0) {
                  minorVersionString = minorVersionString.substring(0, cleanupIndex);
                }

                int minorVersion = Integer.parseInt(minorVersionString);
                if (minorVersion > 0 || minorVersionString.equals("0")) {
                  driverFixLevel_ = majorVersion * 100 + minorVersion;
                } else {
                  System.out.println("Warning.. minorVersion <= 0 from " + minorVersionString + " in " + versionString);
                }
              } else {
                System.out.println("Warning.. majorVersion <= 0 from " + majorVersionString + " in " + versionString);
              }
            } else {
              System.out.println("Warning.. Unable to find , after . after JTOpen in " + versionString);
            }
          } else {
            System.out.println("Warning.. Unable to find . after JTOpen in " + versionString);
          }
        } else {
          System.out.println("Warning.  Unable to find JTOpen in " + versionString);
        }
      }
    }
    if (driverFixLevel_ == 0) {
      /* If not known, set to high value */
      driverFixLevel_ = 999999;
    }
    return driverFixLevel_;
  }

  /**
   * Returns the fix level for CLI. Returns the PTF number for CLI. Returns 99999
   * for other driver.
   */
  int cliFixLevel_ = 0;

  public long getCliFixLevel() {
    if (cliFixLevel_ == 0) {
      if (driver_ == JDTestDriver.DRIVER_NATIVE) {

        Runtime rt = Runtime.getRuntime();

        String[] cmd = new String[3];
        String library = "QSYS";
        cmd[0] = "/usr/bin/qsh";
        cmd[1] = "-c";
        cmd[2] = "system dspobjd " + library
            + "/QSQCLI '*srvpgm detail(*service)'  | grep -i 'PTF number' | sed 's/^.*SI//'";

        // System.out.println("Debug: Running "+cmd[2]);
        try {
          Process p = rt.exec(cmd);
          InputStream iStream = p.getInputStream();
          int readByte = iStream.read();
          while (readByte != -1) {
            // System.out.println(Integer.toHexString((int) readByte));
            if (((readByte >= 0xf0) && (readByte <= 0xf9)) || ((readByte >= 0x30) && (readByte <= 0x39))) {
              int value = readByte & 0xf;
              cliFixLevel_ = 10 * cliFixLevel_ + value;
            }
            readByte = iStream.read();
          }
          iStream.close();

        } catch (Throwable e) {
          e.printStackTrace(System.out);

          // check for spawnp failure
          String message = e.toString();
          if (message.indexOf("spawnp failure") >= 0) {
            System.out.println("Warning.. spawnp failed, setting CliFixLevel to 1");
            cliFixLevel_ = 1;
          }
        }

      } else {
        /* If not known, set to high value */
        cliFixLevel_ = 999999;
      }
    }
    return cliFixLevel_;
  }

  /**
   * Returns the fix level for JDJSTP. Returns the PTF number for sqlejext.
   * Returns 99999 for other driver.
   */
  int jstpFixLevel_ = 0;

  public long getjstpFixLevel() {
    if (jstpFixLevel_ == 0) {
      if (driver_ == JDTestDriver.DRIVER_NATIVE) {

        Runtime rt = Runtime.getRuntime();

        String[] cmd = new String[3];

        cmd[0] = "/usr/bin/qsh";
        cmd[1] = "-c";
        cmd[2] = "system dspobjd QSYS/QSQLEJEXT '*srvpgm detail(*service)'  | grep -i 'PTF number' | sed 's/^.*SI//'";

        // System.out.println("Debug: Running "+cmd[2]);
        try {
          Process p = rt.exec(cmd);
          InputStream iStream = p.getInputStream();
          int readByte = iStream.read();
          while (readByte != -1) {
            // System.out.println(Integer.toHexString((int) readByte));
            if (((readByte >= 0xf0) && (readByte <= 0xf9)) || ((readByte >= 0x30) && (readByte <= 0x39))) {
              int value = readByte & 0xf;
              jstpFixLevel_ = 10 * jstpFixLevel_ + value;
            }
            readByte = iStream.read();
          }
          iStream.close();

        } catch (Throwable e) {
          e.printStackTrace(System.out);

          // check for spawnp failure
          String message = e.toString();
          if (message.indexOf("spawnp failure") >= 0) {
            System.out.println("Warning.. spawnp failed, setting jstpFixLevel to 1");
            jstpFixLevel_ = 1;
          }
        }

      } else {
        /* If not known, set to high value */
        jstpFixLevel_ = 999999;
      }
    }
    return jstpFixLevel_;
  }

  /**
   * Returns the JDBC level that we would expect the JDBC driver being tested to
   * currently support. Note. This only returns the major level
   * 
   * @return The JDBC Specification level the system is running at.
   **/
  public int getJdbcLevel() {
    return jdbcLevel_ / 10;
  }

  public int getJdbcMajorMinorLevel() {
    return jdbcLevel_;
  }

  /**
   * Return the "minor version" for the Toolbox JDBC driver.
   **/
  protected static int getToolboxDriverMinorVersion() {
    if (toolboxDriverMinorVersion_ == 0) {
      String specVersion = getToolboxSpecificationVersion(); // e.g. "6.1.0.4"
      System.out.println("specVersion is "+specVersion);
      String finalDigit = specVersion.substring(1 + specVersion.lastIndexOf('.'));
      try {
        toolboxDriverMinorVersion_ = Integer.parseInt(finalDigit);
      } catch (NumberFormatException e) {
        System.out.println("Warning:  getToolboxSpecificationVersion returned " + specVersion + " from package");
        e.printStackTrace(System.out);
      }
    }
    return toolboxDriverMinorVersion_;
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
   * Returns the JDK level being testing. Possible values are JDTestDriver.JDK_13,
   * JDTestDriver.JDK_14,....
   * 
   * @return the JDK level
   **/
  public int getJDK() {
    return jdk_;
  }

  /**
   * Returns the AS/400 release being tested.
   * 
   * @return The AS/400 release.
   **/
  public int getRelease() {
    return release_;
  }

  public boolean checkRelease750plus(String comment) {
    return checkRelease(JDTestDriver.RELEASE_V7R6M0, comment);
  }

  public boolean checkRelease750(String comment) {
    return checkRelease(JDTestDriver.RELEASE_V7R5M0, comment);
  }

  public boolean checkRelease740(String comment) {
    return checkRelease(JDTestDriver.RELEASE_V7R4M0, comment);
  }

  public boolean checkRelease730() {
    return checkRelease730(null);
  }

  public boolean checkRelease730(String comment) {
    return checkRelease(JDTestDriver.RELEASE_V7R3M0, comment);
  }

  public boolean checkRelease720() {
    return checkRelease720(null);
  }

  public boolean checkRelease720(String comment) {
    return checkRelease(JDTestDriver.RELEASE_V7R2M0, comment);
  }

  public boolean checkRelease710() {
    return checkRelease710(null);
  }

  public boolean checkRelease710(String comment) {
    return checkRelease(JDTestDriver.RELEASE_V7R1M0, comment);
  }

  public boolean checkRelease610() {
    return checkRelease610(null);
  }

  public boolean checkRelease610(String comment) {
    return checkRelease(JDTestDriver.RELEASE_V7R1M0, comment);
  }

  public boolean checkRelease(int release, String comment) {
    if (release_ >= release) {
      return true;
    } else {
      if (comment == null) {
        notApplicable(release + " or later testcase");
      } else {
        notApplicable(release + " or later testcase " + comment);
      }
      return false;
    }
  }

  /**
   * Checks if JDBC 2.0 is available. Always returns true since we are testing on
   * newer JVMS
   **/
  // @Deprecated
  protected boolean isJdbc20() {
    return true;
  }

  /**
   * Checks if JDBC 2.0 Standard Extensions is available. Always returns true
   * since we are testing on newer JVMS
   * 
   **/
  // @Deprecated
  protected boolean isJdbc20StdExt() {
    return true;
  }

  /**
   * Checks if JDBC 4.0 is available.
   **/
  protected boolean isJdbc40() {
    return (jdbcLevel_ >= 40);
  }

  /**
   * Checks if SYSIBM metdata used
   **/

  protected boolean isSysibmMetadata() {
    // as of jdbc40, toolbox is still mainly non-sysibmMetadata.
    if (isToolboxDriver()) {
      return true;
    }

    return isJdbc40() || (getDriver() == JDTestDriver.DRIVER_NATIVE) || (getDriver() == JDTestDriver.DRIVER_JCC);
  }

  /**
   * Runs the variations.
   **/
  public void run() {

    // Set our fields based on the test driver.
    if (testDriver_ != null) {
      baseURL_ = testDriver_.getBaseURL();
      largeDecimalPrecisionSupport_ = testDriver_.areLargeDecimalPrecisionsSupported();
      bigintSupport_ = testDriver_.areBigintsSupported();
      xmlSupport_ = testDriver_.isXmlSupported();
      arraySupport_ = testDriver_.areArraysSupported();
      timestamp12Support_ = testDriver_.isTimestamp12Supported();
      booleanSupport_ = testDriver_.areBooleansSupported();
      driver_ = testDriver_.getDriver();

      subDriver_ = testDriver_.getSubDriver();
      // JDTestUtilities.setDriver(driver_);
      databaseType_ = testDriver_.getDatabaseType();
      release_ = testDriver_.getRelease();

      lobSupport_ = testDriver_.areLobsSupported();
      datalinkSupport_ = testDriver_.areDatalinksSupported();
      savepointSupport_ = testDriver_.areSavepointsSupported();
      namedParameterSupport_ = testDriver_.areNamedParametersSupported();
      cursorHoldabilitySupport_ = testDriver_.areCursorHoldabilitySupported();
      multipleOpenResultSetSupport_ = testDriver_.areMultipleOpenResultSetsSupported();
      generatedKeySupport_ = testDriver_.areGeneratedKeysSupported();
      updateableLobsSupport_ = testDriver_.areUpdateableLobsSupported();
      collection_ = testDriver_.getCollection();
      returnValueSupport_ = testDriver_.areReturnValuesSupported();
      decFloatSupport_ = testDriver_.areDecfloatsSupported();

      supportedFeatures_ = new JDSupportedFeatures(this);
      // Testcase fails in V6R1 for U testcases
      String initials = "";
      int l = collection_.length();
      if (l > 5) {
        initials = collection_.substring(l - 5);
      }
      // System.out.println("initials are "+initials);
      if (initials.length() > 4) {
        char letter = initials.charAt(4);
        if (letter == 'U') {
          toolboxNative = true;
        }
      }
      /*
       * if (initials.equals("614CU") || initials.equals("615CU") ||
       * initials.equals("616CU") ) { toolboxNative = true; }
       */

    }

    // If no release was specified, and the toolbox driver
    // is being tested, then get the release from the
    // AS400 object.
    if ((release_ == JDTestDriver.RELEASE_NONE) && (isToolboxDriver())) {
      try {
        int vrm = systemObject_.getVRM();
        if (vrm == AS400.generateVRM(7, 2, 0))
          release_ = JDTestDriver.RELEASE_V7R2M0;
        else if (vrm == AS400.generateVRM(7, 3, 0))
          release_ = JDTestDriver.RELEASE_V7R3M0;
        else if (vrm == AS400.generateVRM(7, 4, 0))
          release_ = JDTestDriver.RELEASE_V7R4M0;
        else if (vrm == AS400.generateVRM(7, 5, 0))
          release_ = JDTestDriver.RELEASE_V7R5M0;
        else
          release_ = JDTestDriver.RELEASE_V7R6M0;
      } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        e.printStackTrace(System.out);
      }
    }

    // for toolbox, also check for version of jt400.jar (can run with jdk16 and
    // jdbc30 version of jt400.jar)
    if (isToolboxDriver() && jdbcLevel_ >= 40) {
      try {
        // check to see if jt400.jar actually implements the jdbc40 spec
        Class.forName("com.ibm.as400.access.AS400JDBCSQLXML");
      } catch (ClassNotFoundException e) {
        jdbcLevel_ = 30;
      }
    }

    super.run();
  }

  /**
   * Sets the test driver.
   * 
   * @param testDriver The test driver.
   **/
  public void setTestDriver(JDTestDriver testDriver) {
    testDriver_ = testDriver;
  }

  public static void ignoreWarningException(Exception e, String ignoreString) {
    String msg = e.toString();
    if (msg.indexOf(ignoreString) < 0) {
      System.out.println("WARNING:  Unexpected Exception -- expected " + ignoreString);
      e.printStackTrace(System.out);
    }

  }

  public static void ignoreWarningException(Exception e, String ignoreString, String ignoreString2) {
    String msg = e.toString();
    if ((msg.indexOf(ignoreString)) < 0 && (msg.indexOf(ignoreString2) < 0)) {
      System.out
          .println("WARNING:  Unexpected Exception -- expected '" + ignoreString + "' or '" + ignoreString2 + "'");
      e.printStackTrace(System.out);
    }
  }

  /** Convert the string array to a source file on the system */

  public static void stringArrayToSourceFile(Connection connection, String stringArray[], String library, String file)
      throws Exception {

    //
    // Make sure the procedure exists to call CL commands.
    Statement stmt = connection.createStatement();
    try {
      stmt.executeUpdate("create procedure " + library + ".JDCMDEXEC(IN CMDSTR VARCHAR(1024),IN CMDLEN DECIMAL(15,5)) "
          + "External name QSYS.QCMDEXC LANGUAGE C GENERAL");
    } catch (Exception e) {
      // Ignore already exists error
      String message = e.toString();
      if ((message.indexOf("already exists") < 0) && (message.indexOf("-454") < 0)) {
        System.out.println("Warning:  Unrecognized exception");
        e.printStackTrace(System.out);
      }

    }

    CallableStatement cmd = connection.prepareCall("call " + library + ".JDCMDEXEC(?,?)");

    //
    // Make sure the library exists
    //
    try {
      stmt.executeUpdate("Create collection " + library);
    } catch (Exception e) {
      // Only ignorethe error if it already exists
      ignoreWarningException(e, "already exists", "TBD");
    }

    //
    // Make sure the source file exists
    //
    String command = "QSYS/CRTSRCPF " + library + "/" + file;
    cmd.setString(1, command);
    cmd.setInt(2, command.length());
    try {
      cmd.execute();

      command = "QSYS/CRTJRNRCV  " + library + "/QSQJRN1000";
      cmd.setString(1, command);
      cmd.setInt(2, command.length());
      try {
        cmd.execute();
      } catch (Exception ex) {
        ignoreWarningException(ex, "already exists", "TBD");
      }

      command = "QSYS/CRTJRN JRN(" + library + "/QSQJRN) JRNRCV(" + library + "/QSQJRN1000)";
      ;
      cmd.setString(1, command);
      cmd.setInt(2, command.length());
      try {
        cmd.execute();
      } catch (Exception ex) {
        ignoreWarningException(ex, "Error on JRNRCV specifications", "already exists");
      }

      command = "STRJRNPF FILE(" + library + "/" + file + ") JRN(" + library + "/QSQJRN)";
      cmd.setString(1, command);
      cmd.setInt(2, command.length());
      try {
        cmd.execute();
      } catch (Exception ex) {
        ignoreWarningException(ex, "already being journaled", "TBD");
      }

    } catch (Exception e) {
      //
      ignoreWarningException(e, "not created in library", "TBD");

    }

    //
    // Make sure the member exists
    //
    command = "ADDPFM  " + library + "/" + file + " " + file;
    cmd.setString(1, command);
    cmd.setInt(2, command.length());

    try {
      cmd.execute();
    } catch (Exception e) {
      ignoreWarningException(e, "not added to file", "EXTRA");
    }

    //
    // Use SQL to clear the member
    //
    String sql = "DELETE FROM " + library + "." + file;
    try {
      stmt.executeUpdate(sql);
    } catch (Exception e) {
      ignoreWarningException(e, "TBD", "TBD");
    }

    //
    // Use SQL to add the file to the member
    //

    PreparedStatement ps = connection.prepareStatement("insert into " + library + "." + file + " values (?, 0, ?)");

    for (int i = 0; i < stringArray.length; i++) {
      ps.setInt(1, i + 1);
      ps.setString(2, stringArray[i]);
      ps.executeUpdate();
    }

    ps.close();
    stmt.close();
    cmd.close();

  }

  public static String[] fileToStringArray(String file) throws Exception {
    
    StringBuffer sb = new StringBuffer(); 
    InputStream is = JDRunit.loadResourceIfExists(file, sb);
    if (is == null) { 
      System.out.println("Unable to find resource "+file+"\n"+sb.toString()); 
      throw new Exception("Unable to find resource "+file); 
    }
    Vector<String> vector = new Vector<String>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String line = reader.readLine();
    while (line != null) {
      vector.addElement(line);
      line = reader.readLine();
    }

    int length = vector.size();
    String[] output = new String[length];

    for (int i = 0; i < length; i++) {
      output[i] = (String) vector.elementAt(i);
    }
    reader.close();
    return output;
  }

  public static void silentlyDropTable(Statement stmt, String tableName) {
    String sql = "UNSET";
    try {
      sql = "DROP TABLE " + tableName;
      stmt.executeUpdate(sql);
    } catch (Exception e) {
      String message = e.toString().toLowerCase();
      if (message.indexOf("not found") > 0) {
        // OK exception
      } else {
        System.out.println("WARNING: Unexpected exception dropping table using " + sql);
        e.printStackTrace(System.out);
      }
    }
  }

  /**
   * Make sure the schema exists and is set up correctly. In newer releases,
   * sometimes the SYSTABLES view is missing.
   */

  public void validateCollection(Connection c, String collection) {
    Statement s = null;
    boolean newCollection = false;
    boolean tableMissing = false;
    try {
      s = c.createStatement();
      try {
        s.executeUpdate("Create collection " + collection);
        newCollection = true;
      } catch (Exception e) {
        // If the create fails that is ok.
        newCollection = false;
      }

      // Check for the existence of the following tables.
      // If they do not exist, then we will force the creation
      // of the collection.

      String[] tables = { "SYSCOLUMNS", "SYSKEYS", "SYSPACKAGE", "SYSTRIGCOL", "SYSTRIGDEP", "SYSTRIGGERS",
          "SYSTRIGUPD", "SYSCST", "SYSCSTCOL", "SYSCSTDEP", "SYSINDEXES", "SYSKEYCST", "SYSREFCST", "SYSTABLEDEP",
          "SYSTABLES", "SYSVIEWDEP", "SYSVIEWS",

      };

      for (int i = 0; i < tables.length; i++) {
        try {
          s.executeQuery("Select * from " + collection + "." + tables[i] + " fetch first 2 rows only");
        } catch (Exception e) {
          System.out.println("WARNING: " + collection + "." + tables[i] + "  missing -- or other error");
          e.printStackTrace(System.out);
          tableMissing = true;
        }
      }

      if (tableMissing) {
        if (newCollection) {
          System.out.println("WARNING: Tables missing from new collection");
        } else {
          try {

            JDTestDriver.dropCollection(c, collection);
            s.executeUpdate("Create collection " + collection);
          } catch (Exception e) {

            System.out.println("WARNING:  Error recreating collection\n");
            e.printStackTrace(System.out);
          }

        }
      }

      s.close();
    } catch (Exception e) {
      System.out.println("Unexpected error validating collection statement\n");
      e.printStackTrace();
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
    try {
      String reason = JDVariationSkip.skipReason( driver_,
          subDriver_, getRelease(), getName(), getVariation());
      if (reason != null) {
        notApplicable(reason);
        return false;
      } else {

        try {
          Class.forName("test.JDVariationSkip2");
        } catch (Exception e) {
          // If class does not exist then return true
          return true;
        }
        reason = (String) JDReflectionUtil.callStaticMethod_O("test.JDVariationSkip2", "skipReason", driver_, subDriver_,
            getRelease(), getName(), getVariation());
        if (reason != null) {
          notApplicable(reason);
          return false;
        } else {
          return true;
        }
      }
    } catch (Exception e) {
      System.out.println("WARNING:  exception caught in checkVariationApplies");
      e.printStackTrace(System.out);
      System.out.flush();
      return true;
    }
  }

  /**
   * Create an executor for use in a testcase. The following are supported
   * <ul>
   * <li>JDK1.7 java.util.concurrent.ForkJoinPool
   * <li>JDK1.5 java.util.concurrent.ThreadPoolExecutor
   * <li>JDK1.5 java.util.concurrent.ScheduledThreadPoolExecutor
   * </ul>
   * 
   * @param executorClassname Name of the executor class
   * @param sb                StringBuffer for error tracking purposes
   * @return executor
   * @throws Exception
   */
  public Object createExecutor(String executorClassname, StringBuffer sb) throws Exception {
    if (executorClassname.equals("java.util.concurrent.ForkJoinPool")) {
      sb.append("Create " + executorClassname + "\n");
      return JDReflectionUtil.createObject(executorClassname);
    } else if (executorClassname.equals("java.util.concurrent.ThreadPoolExecutor")) {

      // public ThreadPoolExecutor(
      // int corePoolSize,
      // int maximumPoolSize,
      // long keepAliveTime,
      // TimeUnit unit,
      // BlockingQueue<Runnable> workQueue)

      Class<?>[] argTypes = new Class[5];
      argTypes[0] = Integer.TYPE;
      argTypes[1] = Integer.TYPE;
      argTypes[2] = Long.TYPE;
      argTypes[3] = Class.forName("java.util.concurrent.TimeUnit");
      argTypes[4] = Class.forName("java.util.concurrent.BlockingQueue");
      Object[] args = new Object[5];
      args[0] = new Integer(5);
      args[1] = new Integer(20);
      args[2] = new Long(60);
      args[3] = timeUnitSeconds;
      sb.append("Create LinkedBlockingQueue");
      args[4] = JDReflectionUtil.createObject("java.util.concurrent.LinkedBlockingQueue");
      sb.append("Create " + executorClassname + "\n");

      return JDReflectionUtil.createObject(executorClassname, argTypes, args);
    } else if (executorClassname.equals("java.util.concurrent.ScheduledThreadPoolExecutor")) {

      // ScheduledThreadPoolExecutor(int corePoolSize)

      Class<?>[] argTypes = new Class[1];
      argTypes[0] = Integer.TYPE;
      Object[] args = new Object[1];
      args[0] = new Integer(5);
      sb.append("Create " + executorClassname + "\n");

      return JDReflectionUtil.createObject(executorClassname, argTypes, args);
    } else {
      throw new ClassNotFoundException(executorClassname);
    }

  }

  public void setAutoCommit(Connection c, boolean value) {
    try {
      c.setAutoCommit(value);
    } catch (Exception e) {
      System.out.println("Warning:  Caught exception setting autoCommit");
      e.printStackTrace(System.out);
      System.out.flush();

    }
  }

  /*
   * return the fixup string for a particular release and driver
   *
   * The input array looks like this where 54 is V5R4 4 is JDK 1.4 and N is native
   * JDBC driver Object[][] fixupArray = { { "544N", { {"","",""},{"","",""}}} }
   */

  public String[][] getFixup(Object[][] fixupArray, String extraId, String info, StringBuffer sb) {
    String releaseJvmDriver = getReleaseJvmDriver();
    if (extraId != null) {
      releaseJvmDriver += extraId;
    }
    return getFixup(fixupArray, releaseJvmDriver, null, info, sb);
  }

  char previousJVM(char JVM) {
    switch (JVM) {
    case 'C':
      return 'B';
    case 'B':
      return '9';
    case '9':
      return '8';
    case '8':
      return '7';
    case '7':
      return '6';
    case '6':
      return '5';
    default:
      return 'X';
    }
  }

  public String[][] getFixup(Object[][] fixupArray, String releaseJvmDriver, String extraId, String info,
      StringBuffer sb) {
    return getFixup(fixupArray, releaseJvmDriver, extraId, info, sb, false);
  }

  public String[][] getFixup(Object[][] fixupArray, String releaseJvmDriver, String extraId, String info,
      StringBuffer sb, boolean skipPreviousRelease) {

    for (int i = 0; i < fixupArray.length; i++) {
      if (releaseJvmDriver.equals(fixupArray[i][0])) {
        sb.append("Using " + releaseJvmDriver + " entry for " + info + "\n");
        String[][] fixup = ((String[][]) fixupArray[i][1]);
        sb.append("Size of fixup = " + fixup.length + "\n");
        if (fixup.length > 0) {
          sb.append("fixup[0]=" + fixup[0][0] + "," + fixup[0][1] + "," + fixup[0][2] + "\n");
        }
        return fixup;
      }
    }
    sb.append("Did not find " + releaseJvmDriver + " entry for " + info + "\n");
    String currentRelease = releaseJvmDriver.substring(0, 2);

    /* Search for an earlier JVM */
    char JVM = releaseJvmDriver.charAt(2);
    JVM = previousJVM(JVM);
    String[][] answer = null;
    while (JVM != 'X') {
      answer = getFixup(fixupArray, currentRelease + JVM + releaseJvmDriver.substring(3), extraId, info, sb, true);
      if (answer != null) {
        return answer;
      }
      JVM = previousJVM(JVM);
    }

    if (!skipPreviousRelease) {
      /* Search for an earlier release */
      if (currentRelease.equals("76")) {
        sb.append("Trying previous release\n");
        return getFixup(fixupArray, "75" + releaseJvmDriver.substring(2), extraId, info, sb);
      } else if (currentRelease.equals("75")) {
        sb.append("Trying previous release\n");
        return getFixup(fixupArray, "74" + releaseJvmDriver.substring(2), extraId, info, sb);
      } else if (currentRelease.equals("74")) {
        sb.append("Trying previous release\n");
        return getFixup(fixupArray, "73" + releaseJvmDriver.substring(2), extraId, info, sb);
      } else if (currentRelease.equals("73")) {
        sb.append("Trying previous release\n");
        return getFixup(fixupArray, "72" + releaseJvmDriver.substring(2), extraId, info, sb);
      } else if (currentRelease.equals("72")) {
        sb.append("Trying previous release\n");
        return getFixup(fixupArray, "71" + releaseJvmDriver.substring(2), extraId, info, sb);
      } else if (currentRelease.equals("71")) {
        sb.append("Trying previous release\n");
        return getFixup(fixupArray, "61" + releaseJvmDriver.substring(2), extraId, info, sb);
      } else if (currentRelease.equals("61")) {
        sb.append("Trying previous release\n");
        return getFixup(fixupArray, "54" + releaseJvmDriver.substring(2), extraId, info, sb);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  String getReleaseJvmDriver() {
    String value = "";
    switch (getRelease()) {
    case JDTestDriver.RELEASE_V7R1M0:
      value = "71";
      break;
    case JDTestDriver.RELEASE_V7R2M0:
      value = "72";
      break;
    case JDTestDriver.RELEASE_V7R3M0:
      value = "73";
      break;
    case JDTestDriver.RELEASE_V7R4M0:
      value = "74";
      break;
    case JDTestDriver.RELEASE_V7R5M0:
      value = "75";
      break;
    case JDTestDriver.RELEASE_V7R6M0:
      value = "76";
      break;
    default:
      value = "XX";
    }
    int jdk = JVMInfo.getJDK();
    switch (jdk) {
    case JVMInfo.JDK_16:
      value += "6";
      break;
    case JVMInfo.JDK_17:
      value += "7";
      break;
    case JVMInfo.JDK_18:
      value += "8";
      break;
    case JVMInfo.JDK_19:
      value += "9";
      break;
    case JVMInfo.JDK_V11:
      value += "B";
      break;
    case JVMInfo.JDK_V17:
      value += "C";
      break;
    default:
      value += "X";
    }

    switch (getDriver()) {
    case JDTestDriver.DRIVER_NATIVE:
      value += "N";
      break;
    // jt400android should behave like toolbox
    case JDTestDriver.DRIVER_TOOLBOX:
      value += "T";
      break;
    case JDTestDriver.DRIVER_JCC:
      value += "J";
      break;
    case JDTestDriver.DRIVER_JTOPENLITE:
      value += "L";
      break;
    default:
      value += "X";
    }

    return value;
  }

  public String callRsmdMethod(ResultSetMetaData rsmd, String method, int col) throws SQLException {
    String answer = null;
    if (method.equals("isAutoIncrement")) {
      answer = "" + rsmd.isAutoIncrement(col);
    }
    if (method.equals("isCaseSensitive")) {
      answer = "" + rsmd.isCaseSensitive(col);
    }
    if (method.equals("isSearchable")) {
      answer = "" + rsmd.isSearchable(col);
    }
    if (method.equals("isCurrency")) {
      answer = "" + rsmd.isCurrency(col);
    }
    if (method.equals("isNullable")) {
      answer = "" + rsmd.isNullable(col);
    }
    if (method.equals("isSigned")) {
      answer = "" + rsmd.isSigned(col);
    }
    if (method.equals("getColumnDisplaySize")) {
      answer = "" + rsmd.getColumnDisplaySize(col);
    }
    if (method.equals("getColumnLabel")) {
      answer = "" + rsmd.getColumnLabel(col);
    }
    if (method.equals("getColumnName")) {
      answer = "" + rsmd.getColumnName(col);
    }
    if (method.equals("getSchemaName")) {
      answer = "" + rsmd.getSchemaName(col);
    }
    if (method.equals("getPrecision")) {
      answer = "" + rsmd.getPrecision(col);
    }
    if (method.equals("getScale")) {
      answer = "" + rsmd.getScale(col);
    }
    if (method.equals("getTableName")) {
      answer = "" + rsmd.getTableName(col);
    }
    if (method.equals("getCatalogName")) {
      answer = "" + rsmd.getCatalogName(col);
    }
    if (method.equals("getColumnType")) {
      answer = "" + rsmd.getColumnType(col);
    }
    if (method.equals("getColumnTypeName")) {
      answer = "" + rsmd.getColumnTypeName(col);
    }
    if (method.equals("isReadOnly")) {
      answer = "" + rsmd.isReadOnly(col);
    }
    if (method.equals("isWritable")) {
      answer = "" + rsmd.isWritable(col);
    }
    if (method.equals("isDefinitelyWritable")) {
      answer = "" + rsmd.isDefinitelyWritable(col);
    }
    if (method.equals("getColumnClassName")) {
      answer = "" + rsmd.getColumnClassName(col);
    }

    if (answer == null)
      answer = "null";

    return answer;
  }

  protected boolean verifyRsmd(ResultSetMetaData rsmd, String[][] methodTests, String catalog1, int j,
      StringBuffer message, StringBuffer prime) throws SQLException {
    boolean passed = true;
    for (int i = 0; i < methodTests.length; i++) {
      String answer = "NOT SET";
      String method = methodTests[i][0];
      int col = Integer.parseInt(methodTests[i][1]);
      String expected = methodTests[i][2];
      if (expected.equals("LOCALHOST")) {
        expected = catalog1;
      }

      try {
        answer = callRsmdMethod(rsmd, method, col);

        if (answer.equals(expected)) {
          // ok
        } else {
          if (expected.equals("SKIP_SKIP_SKIP")) {
            // Skipping this method call
          } else {
            passed = false;
            message.append("Loop " + j + " Expected: \"" + method + "\",\"" + col + "\",\"" + expected + "\"\n"
                + "Got:      \"" + method + "\",\"" + col + "\",\"" + answer + "\"\n");
            if (j == 0) {
              prime.append("      {\"" + method + "\",\"" + col + "\",\"" + answer + "\"},\n");
            }
          }
        }
      } catch (SQLException sqlex) {
        passed = false;
        message.append(
            "Expected: \"" + method + "\",\"" + col + "\",\"" + expected + "\"\n" + "Got exception " + sqlex + "\n");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        sqlex.printStackTrace(printWriter);
        String exception = stringWriter.toString();
        message.append(exception);
      }
    } /* for i */

    return passed;
  }

  public boolean isRemoteConnection(Connection connection) {
    boolean remoteConnection = false;
    try {
      JDJSTPTestcase.assureGETJOBNAMEisAvailable(connection);

      Statement stmt = connection.createStatement();

      ResultSet rsJobname = stmt
          .executeQuery("SELECT " + JDJSTPTestcase.envLibrary + ".GETJOBNAME() FROM SYSIBM.SYSDUMMY1");
      rsJobname.next();
      String jobnameQusrwrk = rsJobname.getString(1).trim();
      System.out.println("Server job name is " + jobnameQusrwrk);
      if (jobnameQusrwrk.indexOf("QRWT") > 0) {
        remoteConnection = true;
      } else {
        remoteConnection = false;
      }
      stmt.close();
    } catch (Exception e) {
      System.out.println("Warning...exception caught while determining remote connection");
      e.printStackTrace(System.out);
    }
    return remoteConnection;
  }

  /**
   * Test a update method
   * 
   * @param stmt          java.sql.Statement object to use
   * @param setupSql      SQL setup statements.
   * @param sql           SQL statement to execute. Value to get should be first
   *                      column of first row.
   * @param cleanupSql    SQL cleanup statements
   * @param methodName    name of method to call
   * @param expectedValue expected string value. This may also be an expected
   *                      exception (EXCEPTION: ...)
   * @param info          information about when the testcase was added
   */
  public void testUpdate(Statement stmt, String[] setupSql, String sql, String[] cleanupSql, String updateMethodName,
      String getMethodName, String[] expectedValues, String info) {
    StringBuffer sb = new StringBuffer();

    boolean setupFailed = false;
    if (setupSql != null) {
      for (int i = 0; i < setupSql.length; i++) {
        String thisSql = setupSql[i];
        boolean silent = false;
        try {
          if (thisSql.indexOf("SILENT:") == 0) {
            silent = true;
            thisSql = thisSql.substring(7);
          }
          stmt.executeUpdate(thisSql);
        } catch (SQLException ex) {
          if (!silent) {
            setupFailed = true;
            sb.append("Setup failed executing " + thisSql + "\n");
            printStackTraceToStringBuffer(ex, sb);
          }
        }
      }
    }
    if (setupFailed) {
      assertCondition(false, "Setup failed \n" + sb.toString());
    } else {
      testUpdate(stmt, sql, updateMethodName, getMethodName, expectedValues, info);
      if (cleanupSql != null) {
        for (int i = 0; i < cleanupSql.length; i++) {
          String thisSql = cleanupSql[i];
          boolean silent = false;
          try {
            if (thisSql.indexOf("SILENT:") == 0) {
              silent = true;
              thisSql = thisSql.substring(7);
            }
            stmt.executeUpdate(thisSql);
          } catch (SQLException ex) {
            if (!silent) {
              System.out.println("Warning:  cleanup failed ");
              ex.printStackTrace(System.out);
            }
          }
        }
      }

    }
  }

  /**
   * Test a get method
   * 
   * @param stmt          java.sql.Statement object to use
   * @param setupSql      SQL setup statements.
   * @param sql           SQL statement to execute. Value to get should be first
   *                      column of first row.
   * @param cleanupSql    SQL cleanup statements
   * @param methodName    name of method to call
   * @param expectedValue expected string value. This may also be an expected
   *                      exception (EXCEPTION: ...)
   * @param info          information about when the testcase was added
   */
  public void testGet(Statement stmt, String[] setupSql, String sql, String[] cleanupSql, String methodName,
      String expectedValue, String info) {
    StringBuffer sb = new StringBuffer();

    boolean setupFailed = false;
    if (setupSql != null) {
      for (int i = 0; i < setupSql.length; i++) {
        String thisSql = setupSql[i];
        boolean silent = false;
        try {
          if (thisSql.indexOf("SILENT:") == 0) {
            silent = true;
            thisSql = thisSql.substring(7);
          }
          stmt.executeUpdate(thisSql);
        } catch (SQLException ex) {
          if (!silent) {
            setupFailed = true;
            sb.append("Setup failed executing " + thisSql + "\n");
            printStackTraceToStringBuffer(ex, sb);
          }
        }
      }
    }
    if (setupFailed) {
      assertCondition(false, "Setup failed \n" + sb.toString());
    } else {
      testGet(stmt, sql, methodName, expectedValue, info);
      if (cleanupSql != null) {
        for (int i = 0; i < cleanupSql.length; i++) {
          String thisSql = cleanupSql[i];
          boolean silent = false;
          try {
            if (!skipCleanup) {
              if (thisSql.indexOf("SILENT:") == 0) {
                silent = true;
                thisSql = thisSql.substring(7);
              }
              stmt.executeUpdate(thisSql);
            }
          } catch (SQLException ex) {
            if (!silent) {
              System.out.println("Warning:  cleanup failed ");
              ex.printStackTrace(System.out);
            }
          }
        }
      }

    }
  }

  /**
   * Test a get method
   * 
   * @param stmt           java.sql.Statement object to use
   * @param setupSql       SQL setup statements.
   * @param sql            SQL statement to execute. Value to get should be first
   *                       column of first row.
   * @param cleanupSql     SQL cleanup statements
   * @param methodName     name of method to call
   * @param expectedValues expected values array. This may also be an expected
   *                       exception (EXCEPTION: ...)
   * @param info           information about when the testcase was added
   */
  public void testGet(Statement stmt, String[] setupSql, String sql, String[] cleanupSql, String methodName,
      String[] expectedValues, String info) {
    StringBuffer sb = new StringBuffer();

    boolean setupFailed = false;
    if (setupSql != null) {
      for (int i = 0; i < setupSql.length; i++) {
        String thisSql = setupSql[i];
        boolean silent = false;
        try {
          if (thisSql.indexOf("SILENT:") == 0) {
            silent = true;
            thisSql = thisSql.substring(7);
          }
          stmt.executeUpdate(thisSql);
        } catch (SQLException ex) {
          if (!silent) {
            setupFailed = true;
            sb.append("Setup failed executing " + thisSql + "\n");
            printStackTraceToStringBuffer(ex, sb);
          }
        }
      }
    }
    if (setupFailed) {
      assertCondition(false, "Setup failed \n" + sb.toString());
    } else {
      testGet(stmt, sql, methodName, expectedValues, info);
      if (cleanupSql != null && (!skipCleanup)) {
        for (int i = 0; i < cleanupSql.length; i++) {
          String thisSql = cleanupSql[i];
          boolean silent = false;
          try {
            if (thisSql.indexOf("SILENT:") == 0) {
              silent = true;
              thisSql = thisSql.substring(7);
            }
            stmt.executeUpdate(thisSql);
          } catch (SQLException ex) {
            if (!silent) {
              System.out.println("Warning:  cleanup failed ");
              ex.printStackTrace(System.out);
            }
          }
        }
      }

    }
  }

  /**
   * check a getMethod call on a result set
   */
  public boolean checkRsGetMethod(ResultSet rs, String methodName, String expectedValue, StringBuffer sb) {
    String stringValue;
    try {
      if (methodName.equals("getUnicodeStream")) {

        InputStream v = (InputStream) JDReflectionUtil.callMethod_O(rs, "getUnicodeStream", 1);
        boolean check = compareUnicodeStream(v, expectedValue, sb);
        return check;
      } else if (methodName.equals("getURL") || methodName.equals("getBigDecimal") || methodName.equals("getDate")
          || methodName.equals("getNString") || methodName.equals("getObject") || methodName.equals("getRef")
          || methodName.equals("getTime") || methodName.equals("getTimestamp") || methodName.equals("getSQLXML")
          || methodName.equals("getString")) {
        Object outObject = JDReflectionUtil.callMethod_O(rs, methodName, 1);
        if (outObject == null) {
          stringValue = "NULL";
        } else {
          stringValue = outObject.toString();
        }
      } else if (methodName.equals("getBoolean")) {
        stringValue = "" + rs.getBoolean(1);
      } else if (methodName.equals("getByte")) {
        stringValue = "" + rs.getByte(1);
      } else if (methodName.equals("getShort")) {
        stringValue = "" + rs.getShort(1);
      } else if (methodName.equals("getInt")) {
        stringValue = "" + rs.getInt(1);
      } else if (methodName.equals("getLong")) {
        stringValue = "" + rs.getLong(1);
      } else if (methodName.equals("getFloat")) {
        stringValue = "" + rs.getFloat(1);
      } else if (methodName.equals("getDouble")) {
        stringValue = "" + rs.getDouble(1);
      } else if (methodName.equals("getRowId")) {
        Object outObject = JDReflectionUtil.callMethod_O(rs, methodName, 1);
        stringValue = "NULL";
        if (outObject != null) {
          byte[] returnBytes = (byte[]) JDReflectionUtil.callMethod_O(outObject, "getBytes");
          stringValue = bytesToString(returnBytes);
        }

      } else if (methodName.equals("getAsciiStream")) {

        InputStream v = rs.getAsciiStream(1);
        boolean check = compareAsciiStream(v, expectedValue, sb);
        return check;
      } else if (methodName.equals("getBlob")) {
        Blob outBlob = rs.getBlob(1);
        boolean passed = compare(outBlob, expectedValue, sb);
        return passed;
      } else if (methodName.equals("getBytes")) {
        byte[] returnBytes = rs.getBytes(1);
        stringValue = "NULL";
        if (returnBytes != null) {
          stringValue = bytesToString(returnBytes);
        }

      } else if (methodName.equals("getClob")) {
        Object outObject = JDReflectionUtil.callMethod_O(rs, methodName, 1);
        stringValue = "NULL";
        if (outObject != null) {
          int length = (int) JDReflectionUtil.callMethod_L(outObject, "length");
          stringValue = JDReflectionUtil.callMethod_S(outObject, "getSubString", 1L, length);
        }
      } else if (methodName.equals("getCharacterStream")) {
        Reader reader = (Reader) JDReflectionUtil.callMethod_O(rs, methodName, 1);
        boolean passed = compare(reader, expectedValue, sb);
        return passed;

      } else if (methodName.equals("getBinaryStream")) {
        InputStream is = rs.getBinaryStream(1);
        boolean passed = compare(is, expectedValue, sb);
        return passed;
      } else {
        throw new Exception("Method " + methodName + " not supported");
      }
      boolean check = stringValue.equals(expectedValue);
      if (!check) {
        sb.append(" for " + methodName + "\ngot " + stringValue + "\nsb  " + expectedValue + "\n");
      }
      return check;

    } catch (Exception e) {
      if (expectedValue.indexOf("EXCEPTION:") == 0) {
        String searchException = expectedValue.substring(10);
        if (e.toString().indexOf(searchException) < 0) {
          sb.append("Unexpected Exception -- Expected " + searchException + "\n");
          printStackTraceToStringBuffer(e, sb);
          return false;
        } else {
          return true;
        }
      } else {
        sb.append("Unexpected Exception -- \n");
        printStackTraceToStringBuffer(e, sb);
        return false;
      }
    }

  }

  /**
   * Test a get method
   * 
   * @param stmtjava.sql.Statement object to use
   * @param sql                    SQL statement to execute. Value to get should
   *                               be first column of first row.
   * @param methodName             name of method to call
   * @param expectedValue          expected string value. This may also be an
   *                               expected exception (EXCEPTION: ...)
   * @param info                   information about when the testcase was added
   */
  public void testGet(Statement stmt, String sql, String methodName, String expectedValue, String info) {

    StringBuffer sb = new StringBuffer();
    try {
      sb.append("Testing " + sql + " " + methodName + " " + expectedValue);
      ResultSet rs = stmt.executeQuery(sql);
      rs.next();
      boolean check = checkRsGetMethod(rs, methodName, expectedValue, sb);
      assertCondition(check, sb.toString() + info);
    } catch (Exception e) {
      failed(e, sb.toString() + info);
    }

  }

  /**
   * Test a get method
   * 
   * @param stmtjava       .sql.Statement object to use
   * @param sql            SQL statement to execute. Value to get should be first
   *                       column of first row.
   * @param methodName     name of method to call
   * @param expectedValues expected string array. This may also be an expected
   *                       exception (EXCEPTION: ...)
   * @param info           information about when the testcase was added
   */
  public void testGet(Statement stmt, String sql, String methodName, String[] expectedValues, String info) {

    StringBuffer sb = new StringBuffer();
    try {
      sb.append("Testing " + sql + " " + methodName + " ");
      ResultSet rs = stmt.executeQuery(sql);
      boolean check = true;
      for (int i = 0; i < expectedValues.length; i++) {
        sb.append("Test step: Calling rs.next for i = " + i + " expectedValue = " + expectedValues[i] + "\n");
        rs.next();
        if (!checkRsGetMethod(rs, methodName, expectedValues[i], sb)) {
          check = false;
        }
      }
      assertCondition(check, sb.toString() + info);
    } catch (Exception e) {
      failed(e, sb.toString() + info);
    }

  }

  /**
   * Test an update method
   * 
   * @param stmtjava         .sql.Statement object to use
   * @param sql              SQL statement to execute. Value to get should be
   *                         first column of first row.
   * @param updateMethodName name of method to call for the update
   * @param getMethodName    name of method to call for the get
   * @param expectedValues   expected string array. This may also be an expected
   *                         exception (EXCEPTION: ...)
   * @param info             information about when the testcase was added
   */
  public void testUpdate(Statement stmt, String sql, String updateMethodName, String getMethodName,
      String[] expectedValues, String info) {

    StringBuffer sb = new StringBuffer();
    try {
      sb.append("Testing " + sql + " " + updateMethodName + "/" + getMethodName + "\n");
      boolean check = true;
      ResultSet rs = stmt.executeQuery(sql);

      for (int i = 0; i < expectedValues.length; i++) {
        rs.absolute(i + 1);
        Timestamp d;
        String inValue = expectedValues[i];
        if (inValue.length() <= 29) {
          d = Timestamp.valueOf(inValue);
        } else {
          if (driver_ == JDTestDriver.DRIVER_NATIVE) {
            d = (Timestamp) JDReflectionUtil.callStaticMethod_O("com.ibm.db2.jdbc.app.DB2JDBCTimestamp", "valueOf",
                inValue);
          } else {
            d = AS400JDBCTimestamp.valueOf(inValue);
          }
        }
        if (updateMethodName.equals("updateTimestamp")) {
          rs.updateTimestamp(1, d);
        } else {
          throw new Exception("Method :" + updateMethodName + " not supported");
        }
        rs.updateRow();
      }

      /* Rerun the query and check the results */

      rs = stmt.executeQuery(sql);
      for (int i = 0; i < expectedValues.length; i++) {
        sb.append("Calling rs.next for i = " + i + " expectedValue = " + expectedValues[i] + "\n");
        rs.next();
        if (!checkRsGetMethod(rs, getMethodName, expectedValues[i], sb)) {
          check = false;
        }
      }
      assertCondition(check, sb.toString() + info);
    } catch (Exception e) {
      failed(e, sb.toString() + info);
    }

  }

  /**
   * check for a closed exception. This will accept all flavors of closed
   * exceptions
   */

  static String[] expectedClosedExceptions = { "Function sequence error", "STATEMENT CLOSED",
      "Invalid operation: statement closed", /* JCC */
  };

  public void assertClosedException(Exception e, StringBuffer sb) {
    assertExceptionContains(e, expectedClosedExceptions, sb.toString());
  }

  public void assertClosedException(Exception e, String info) {
    assertExceptionContains(e, expectedClosedExceptions, info);
  }

  public boolean checkClosedException(Exception e, StringBuffer sb) {
    String exceptionMessage = e.toString();
    StringBuffer messagesText = new StringBuffer();
    for (int i = 0; i < expectedClosedExceptions.length; i++) {
      String message = expectedClosedExceptions[i];
      if (exceptionMessage.indexOf(message) >= 0) {
        return true;
      } else {
        if (i > 0)
          messagesText.append(",");
        messagesText.append("'");
        messagesText.append(message);
        messagesText.append("'");

      }
    }

    sb.append("Statement was not closed correctly:  Exception " + exceptionMessage + " did not have "
        + messagesText.toString() + "\n");
    return false;

  }

  protected Connection cleanupConnection(Connection connection) throws SQLException {

    Statement stmt = connection.createStatement();
    stmt.execute("CALL QSYS2.QCMDEXC('CHGJOB LOG(4 00 *NOLIST)')");
    stmt.close();
    connection.close();
    return null;
  }

  /**
   * This method is used to indicate failure of the current variation. It is
   * intended for use when an exception caused the failure.
   * 
   * @param connection The JDBC connection being used when the exception occurred.
   * @param exception  The exception that caused the failure.
   * @param comment    Additional information concerning the failure.
   **/
  public final void failed(Connection connection, Throwable exception, String comment) {
    /* Get additional information about the failure */
    if (exception instanceof SQLException) {
      System.out.println("ERROR:  SQLException caught.  Server job log is ");
      dumpServerJobLog(connection);

    }
    failed(exception, comment);
  }

  public final void failed(Connection connection, Throwable exception, StringBuffer sb) {
    /* Get additional information about the failure */
    if (exception instanceof SQLException) {
      System.out.println("ERROR:  SQLException caught.  Server job log is ");
      dumpServerJobLog(connection);

    }
    failed(exception, sb);
  }

  static public void dumpServerJobLog(Connection connection) {
    /* Get the server joblog */
    String sql = null;
    try {
      Statement stmt = connection.createStatement();
      stmt.execute("CALL QSYS2.QCMDEXC('CHGJOB LOG(4 00 *SECLVL) ' )");
      stmt.execute("CALL QSYS2.QCMDEXC('DSPJOBLOG OUTPUT(*PRINT) ')");
      stmt.close();

      String outputFile = "/tmp/failed.joblog." + connection.toString().replace('/', '-') + "." + connection.hashCode()
          + ".txt";
      JDJSTPTestcase.assureSTPJOBLOGisAvailable(connection);
      CallableStatement cs = connection.prepareCall("CALL QGPL.STPJOBLOG('" + outputFile + "')");
      cs.executeUpdate();
      System.out.println("JOBLOG information at " + outputFile);
      if (JTOpenTestEnvironment.isOS400) {
        // No need to get file, it is already there
      } else {
        // Grab the file and place it on the local system
        Statement s = connection.createStatement();
        sql = "call QSYS.QCMDEXC('DLTF QTEMP/JOBLOG                        ',000000040.00000)";
        try {
          s.execute(sql);
        } catch (Exception e) {

        }

        sql = "call QSYS.QCMDEXC('CRTSRCPF FILE(QTEMP/JOBLOG) RCDLEN(150)                     ',000000040.00000)";
        s.execute(sql);
        sql = "call QSYS.QCMDEXC('CPYFRMSTMF FROMSTMF(''" + outputFile
            + "'') TOMBR(''/QSYS.LIB/QTEMP.LIB/JOBLOG.FILE/JOBLOG.MBR'')"
            + "                                                                                  ',000000160.00000)";
        s.execute(sql);

        PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
        sql = "Select  srcdta from qtemp.joblog";
        ResultSet rs = s.executeQuery(sql);
        while (rs.next()) {
          String line = rs.getString(1);
          writer.println(line);
        }
        writer.close();
        rs.close();
        sql = null;
      }
      BufferedReader reader = new BufferedReader(new FileReader(outputFile));
      JDJobLog.filterJobLog(System.out, reader);
      reader.close();
    } catch (Exception e) {
      System.out.println("WARNING:  processing of error information failed");
      if (sql != null) {
        System.out.println("sql = " + sql);
      }
      e.printStackTrace();
    }

  }

  static public void dumpJobLog(Connection connection, String jobname) {
    /* Get the server joblog */
    String sql = null;
    try {
      int hashcode = connection.hashCode();
      if (hashcode < 0)
        hashcode = -hashcode;
      String outputFile = "/tmp/failed.joblog." + jobname.replace('/', '-') + "." + hashcode + ".txt";
      JDJSTPTestcase.assureSTPJOBLOGXisAvailable(connection);
      CallableStatement cs = connection.prepareCall("CALL QGPL.STPJOBLOGX('" + outputFile + "','" + jobname + "')");
      cs.executeUpdate();
      System.out.println("JOBLOG information at " + outputFile);
      if (JTOpenTestEnvironment.isOS400) {
        // No need to get file, it is already there
      } else {
        // Grab the file and place it on the local system
        Statement s = connection.createStatement();
        sql = "call QSYS.QCMDEXC('DLTF QTEMP/JOBLOG                        ',000000040.00000)";
        try {
          s.execute(sql);
        } catch (Exception e) {

        }

        sql = "call QSYS.QCMDEXC('CRTSRCPF FILE(QTEMP/JOBLOG) RCDLEN(150)                     ',000000040.00000)";
        s.execute(sql);
        sql = "call QSYS.QCMDEXC('CPYFRMSTMF FROMSTMF(''" + outputFile
            + "'') TOMBR(''/QSYS.LIB/QTEMP.LIB/JOBLOG.FILE/JOBLOG.MBR'')"
            + "                                                                                  ',000000160.00000)";
        s.execute(sql);

        PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
        sql = "Select  srcdta from qtemp.joblog";
        ResultSet rs = s.executeQuery(sql);
        while (rs.next()) {
          String line = rs.getString(1);
          writer.println(line);
        }
        writer.close();
        rs.close();
        sql = null;
      }
      BufferedReader reader = new BufferedReader(new FileReader(outputFile));
      JDJobLog.filterJobLog(System.out, reader);
      reader.close();
    } catch (Exception e) {
      System.out.println("WARNING:  processing of error information failed");
      if (sql != null) {
        System.out.println("sql = " + sql);
      }
      e.printStackTrace();
    }

  }

  private static String lastSpaceDelimitedString(String rest) {
    rest = rest.trim();
    if (rest.length() == 0) {
      return null;
    }
    int spaceIndex = rest.lastIndexOf(' ');
    if (spaceIndex > 0) {
      return rest.substring(spaceIndex + 1);
    } else {
      return rest;
    }
  }

  private static String removeLastSpaceDelimitedString(String rest) {
    if (rest.length() == 0) {
      return null;
    }
    rest = rest.trim();
    int spaceIndex = rest.lastIndexOf(' ');
    if (spaceIndex > 0) {
      return rest.substring(0, spaceIndex).trim();
    } else {
      return "";
    }
  }

  /* check if the exception is an inuse exception */
  /* If so, gather information about why it is in use */

  public static void checkInUse(Exception e, Statement stmt) {
    String message = e.toString().toUpperCase();
    if (message.indexOf("IN USE") > 0) {
      String library = null;
      String object = null;
      String objectType = null;
      System.out.println("IN USE error found");
      try {
        Connection currentConnection = stmt.getConnection();
        String currentConnectionClassName = currentConnection.getClass().getName();
        if (currentConnectionClassName.indexOf("AS400JDBCConnection") >= 0) {
          System.out
              .println("Current server job is " + ((AS400JDBCConnection) currentConnection).getServerJobIdentifier());
        } else if (currentConnectionClassName.indexOf("DB2Connection") >= 0) {
          System.out
              .println("Current server job is " + JDReflectionUtil.callMethod_S(currentConnection, "getServerJobName"));

        }
      } catch (Exception e1) {
        System.out.println("Warning.. exception getting job name");
        e1.printStackTrace(System.out);
      }
      // Get the object from the message
      // Row or object RSTESTUPD in JDT7277T type *FILE in use
      int inIndex = message.indexOf(" IN ");
      if (inIndex > 0) {
        int spaceIndex = message.lastIndexOf(" ", inIndex - 1);
        if (spaceIndex > 0) {
          object = message.substring(spaceIndex + 1, inIndex);
          spaceIndex = message.indexOf(" ", inIndex + 4);
          if (spaceIndex > 0) {
            int secondSpace = message.indexOf(" ", inIndex + 4);
            if (secondSpace > 0) {
              library = message.substring(inIndex + 4, secondSpace);
              int typeIndex = message.indexOf(" TYPE ");
              if (typeIndex > 0) {
                secondSpace = message.indexOf(" ", typeIndex + 6);
                if (secondSpace > 0) {
                  objectType = message.substring(typeIndex + 6, secondSpace);
                }
              }
            }
          }
        }

      }

      if (library != null && object != null && objectType != null) {
        System.out.println("Checking " + library + "/" + object + " " + objectType);
        String lockInfoTable = "qgpl.lockinfo";
        String sql = null;
        try {
          try {
            sql = "drop table " + lockInfoTable;
            stmt.executeUpdate(sql);
          } catch (Exception e2) {
          }
          sql = "create table " + lockInfoTable + "(srcdta varchar(200))";
          stmt.executeUpdate(sql);

        sql = "CALL QSYS.QCMDEXC('QSH CMD(''system \"QSYS/WRKOBJLCK OBJ(" + library + "/" + object + ") OBJTYPE("
              + objectType + ")  \"" + "| sed \"s/^\\(.*\\)/insert into " + lockInfoTable
              + "(SRCDTA) values(''''\\1'''')/\"  > /tmp/" + lockInfoTable
              + ".txt '')                             ',000000180.00000)";
          stmt.executeUpdate(sql);

          sql = "CALL QSYS.QCMDEXC('QSH CMD(''cat /tmp/" + lockInfoTable
              + ".txt     | db2 -i  '')                                                                                      ',000000080.00000)";
          stmt.executeUpdate(sql);

          sql = "CALL QSYS.QCMDEXC('QSH CMD(''rm /tmp/" + lockInfoTable
              + ".txt'')                                                                                      ',000000050.00000)";
          stmt.executeUpdate(sql);

          sql = " select * from " + lockInfoTable;
          ResultSet rs = stmt.executeQuery(sql);
          while (rs.next()) {
            String line = rs.getString(1);
            System.out.println(line);
            /* Handle held lines of the following forms */
            /* RSTESTUPD QPADEV0004 USERID 993338 MBR *SHRRD HELD *JOB */
            /* QPADEV0004 USERID 993338 *SHRRD HELD *JOB */
            int heldIndex = line.indexOf("HELD");
            if (heldIndex > 0) {
              String rest = line.substring(0, heldIndex).trim();
              rest = removeLastSpaceDelimitedString(rest);
              String heldNumber = lastSpaceDelimitedString(rest);
              if ((heldNumber != null) && (heldNumber.equals("MBR"))) {
                rest = removeLastSpaceDelimitedString(rest);
                heldNumber = lastSpaceDelimitedString(rest);
              }

              if (heldNumber != null) {
                rest = removeLastSpaceDelimitedString(rest);
                String heldUser = lastSpaceDelimitedString(rest);
                if (heldUser != null) {
                  rest = removeLastSpaceDelimitedString(rest);
                  String heldJob = lastSpaceDelimitedString(rest);
                  if (heldJob != null) {
                    String job = heldNumber + "/" + heldUser + "/" + heldJob;
                    System.out.println("***** CULPRIT JOB **** " + job);
                    dumpJobLog(stmt.getConnection(), job);
                  }
                }
              }
            }

          }
          sql = "drop table " + lockInfoTable;
          stmt.executeUpdate(sql);

        } catch (Exception e2) {
          System.out.println("Warning:  SQL error in checkInUse sql=" + sql);
          e2.printStackTrace(System.out);
        }

      }

    }
  }

  public void cleanupBindings(String userid, char[] encryptedPassword) {
    if (checkClientOn400()) {
      // Use the authority of the userid/password to delete the files
      char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword);
      AS400 as400 = new AS400("localhost", userid, passwordChars);
      PasswordVault.clearPassword(passwordChars);
      // Remove /.bindings
      IFSFile ifsFile = new IFSFile(as400, "/.bindings");
      try {
        ifsFile.delete();
      } catch (IOException e) {
      }
      // Remove bindings
      ifsFile = new IFSFile(as400, JTOpenTestEnvironment.testcaseHomeDirectory + "/.bindings");
      try {
        ifsFile.delete();
      } catch (IOException e) {
      }

    } else {

      System.out.println("Warning.  Cannot cleanup bindings on this platform");
    }

  }

  public void rebindContext(Context ctx, String bindName, Object obj) throws NamingException {
    try {
      ctx.rebind(bindName, obj);
    } catch (Exception e) {
      cleanupBindings(pwrSysUserID_, pwrSysEncryptedPassword_);
      ctx.rebind(bindName, obj);
    }
  }

  /* Method to cleanupConnection if cleanup() not called */
  protected void cleanupConnections() throws Exception {
    if (connection_ != null) {
      try {
        connection_.close();
        connection_ = null;
      } catch (Exception e) {
        System.out.println("Warning:  Exception on closing connection\n");
        e.printStackTrace(System.out);
      }
    }
  }

  public static void initTable(Statement s, String tableName, String tableDefinition) throws SQLException {
    StringBuffer sb = new StringBuffer();
    try {
      JDTestDriver.initTable(s, tableName, tableDefinition, sb);
    } catch (SQLException sqlex) {
      System.out.println("Error in init table" + sb.toString());
      System.out.println("Rethrowing exception");
      throw sqlex;
    }
  }

  public static void initTable(Statement s, String tableName, String tableDefinition, StringBuffer sb)
      throws SQLException {
    JDTestDriver.initTable(s, tableName, tableDefinition, sb);
  }

  public static void initTrigger(Statement s, String triggerName, String triggerDefinition) throws SQLException {
    StringBuffer sb = new StringBuffer();
    try {
      JDTestDriver.initTrigger(s, triggerName, triggerDefinition, sb);
    } catch (SQLException sqlex) {
      System.out.println("Error in initTrigger " + sb.toString());
      System.out.println("Rethrowing exception");
      throw sqlex;
    }
  }

  public void cleanupTable(Statement s, String tableName) {
    testDriver_.cleanupTable(s, tableName);

  }

  public void cleanupTrigger(Statement s, String triggerName) {
    testDriver_.cleanupTrigger(s, triggerName);
  }

}
