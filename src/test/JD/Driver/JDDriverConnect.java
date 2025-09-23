///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDriverConnect.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDriverConnect.java
//
// Classes:      JDDriverConnect
//
////////////////////////////////////////////////////////////////////////

package test.JD.Driver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Hashtable; import java.util.Vector;
import java.util.Properties;
import java.util.Random;

import com.ibm.as400.access.AS400;
// Converted to use reflection to test
import com.ibm.as400.access.AS400JDBCDriver; //@A1A
import com.ibm.as400.access.IFSSystemView;
import com.ibm.as400.access.SecureAS400; //@A2A

import test.JDDriverTest;
import test.JDJobName;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTOpenTestEnvironment;
import test.JVMInfo;
import test.PasswordVault;
import test.Sec.AuthExit;

/**
 * Testcase JDDriverConnect. This tests the following method of the JDBC Driver
 * class:
 * 
 * <ul>
 * <li>connect()
 * <li>"errors" property
 * <li>"secure" property
 * <li>"trace" property
 * <li>"driver" property
 * </ul>
 **/
public class JDDriverConnect extends JDTestcase {
  public static void main(String args[]) throws Exception {
    // Note: reflection is used to get the classname, so this can be pasted easily
    // into other Testcase classes
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = new Object() {
    }.getClass().getEnclosingClass().getSimpleName();
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    JDDriverTest.main(newArgs);
  }

  // Private data.
  private Driver driver_;
  String letter_;
  int jdk_;
  boolean exitProgramChecked_ = false;
  boolean exitProgramEnabled_ = false;
  Connection pwrConnection_ = null;
  private boolean skipExitCleanup = true;

  /**
   * Constructor.
   **/

  public JDDriverConnect(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
      String password, String powerUserID, String powerPassword) {
    super(systemObject, "JDDriverMisc", namesAndVars, runMode, fileOutputStream, password, powerUserID, powerPassword);

    systemObject_ = systemObject;

  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {
    jdk_ = JVMInfo.getJDK();
    driver_ = DriverManager.getDriver(baseURL_);

    letter_ = JDDriverTest.COLLECTION.substring(JDDriverTest.COLLECTION.length() - 1);

    Properties properties = new Properties();
    properties.put("user", testDriver_.pwrSysUserID_);
    properties.put("prompt", "false");
    char[] charPassword = PasswordVault.decryptPassword(testDriver_.pwrSysEncryptedPassword_);
    Class<?>[] argTypes = new Class[3];
    argTypes[0] = baseURL_.getClass();
    argTypes[1] = properties.getClass();
    argTypes[2] = charPassword.getClass();
    ;
    try { 
       pwrConnection_ = (Connection) JDReflectionUtil.callMethod_O(driver_, "connect", argTypes, baseURL_, properties,
        charPassword);
    } finally { 
       Arrays.fill(charPassword, ' ');
    }

  }

  protected void cleanup() throws Exception {
    // If this testcase ends with connections still open,
    // The following may be seen in the QSQSRVR jobs.
    //
    // Processing of the SQL statement ended. Reason code 1.
    // Function check. SQL0952 unmonitored by QSYRTVUP at statement *N,
    // instruction X'0324'.
    //
    // When this happens, the locks my still be held.
    //
    if (!skipExitCleanup) {
      AuthExit.cleanupExitProgram(pwrConnection_);
      AuthExit.cleanupExitProgramFiles(pwrConnection_);
    }
    pwrConnection_.close();
    System.gc();
    try {
      Thread.sleep(10000);
    } catch (Exception e) {
    }
  }

  /**
   * connect() - Should return null when the url is null.
   **/
  public void Var001() {
    try {
      Connection c = driver_.connect(null, new Properties());
      assertCondition(c == null);
    } catch (Exception e) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        if (e instanceof NullPointerException) {
          succeeded("JCC Driver returns NPE on null URL");
        } else {
          failed(e, "Unexpected exception");
        }
      } else {
        failed(e, "Unexpected exception");
      }
    }
  }

  /**
   * connect() - Should return null when the url is not ours.
   **/
  public void Var002() {
    try {
      Connection c = driver_.connect("jdbc:oracle:rchaslkb", new Properties());
      assertCondition(c == null);
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * connect() - Should return a connection when the properties are null.
   **/
  public void Var003() {
    if (checkPasswordLeak()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC Driver does not permit null USERID");
      } else {
        try {
          String url = baseURL_ + ";user=" + userId_ + ";password="
              + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.3");
          Connection c = driver_.connect(url, null);
          boolean success = (c != null);

          // Close the connection.
          if (c != null) {
            c.close();
          }

          assertCondition(success);
        } catch (Exception e) {
          failed(e, "Unexpected exception");
        }
      }
    }
  }

  /**
   * connect() - When a valid URL is specified, and user and password are passed
   * in the properties object, then we can run statements.
   **/
  public void Var004() {
    if (checkPasswordLeak()) {
      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", userId_);
        p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.4"));

        Connection c = driver_.connect(url, p);

        // Run a query.
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
        boolean found = rs.next();

        // Close the connection.
        c.close();

        assertCondition(found, "DID NOT FIND ROW IN SELECT * FROM QIWS.QCUSTCDT");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * connect() - When a valid URL is specified, and user and password are passed
   * in the URL, then we can run statements.
   **/
  public void Var005() {
    if (checkPasswordLeak()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC Driver does not permit null USERID");
      } else {

        try {
          String url = baseURL_ + ";user=" + userId_ + ";password="
              + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.5");
          Properties p = new Properties();

          Connection c = driver_.connect(url, p);

          // Run a query.
          Statement s = c.createStatement();
          ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          boolean found = rs.next();

          // Close the connection.
          c.close();

          assertCondition(found);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * connect() - When a valid URL is specified, and user and password are passed
   * in both the URL and properties, then we can run statements. Verify that the
   * user and password that are used come from the URL.
   **/
  public void Var006() {
    if (checkPasswordLeak()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC Driver does not permit null USERID");
      } else {
        try {
          String url = baseURL_ + ";user=" + userId_ + ";password="
              + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.6");
          Properties p = new Properties();
          p.put("user", "NOTUSED1");
          p.put("password", "NOTUSED2");

          Connection c = driver_.connect(url, p);

          // Run a query.
          Statement s = c.createStatement();
          ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          boolean found = rs.next();

          // Close the connection.
          c.close();

          assertCondition(found);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * connect() - When a valid URL is specified, but the system is not a valid
   * system, then an exception should be thrown.
   **/
  public void Var007() {
    if (checkPasswordLeak()) {
      try {
        String url = (isToolboxDriver()) ? "jdbc:as400://BAD_SYSTEM" : "jdbc:db2://BAD_SYSTEM";
        if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
          url = "jdbc:jtopenlite://BAD_SYSTEM";
        }
        Properties p = new Properties();
        p.put("user", userId_);
        p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.7"));

        Connection c = driver_.connect(url, p);

        failed("Did not throw exception. " + c);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * connect() - When a valid URL is specified, but the user is not a valid user,
   * then an exception should be thrown.
   **/
  public void Var008() {
    if (checkPasswordLeak()) {
      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "MRNOBODY");
        p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.8"));
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed("Did not throw exception.  This variation is expected to fail if a secondary URL was specified. " + c);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * connect() - When a valid URL is specified, but the password is not correct,
   * then an exception should be thrown.
   **/
  public void Var009() {
    try {
      String url = baseURL_;
      Properties p = new Properties();
      p.put("user", userId_);
      p.put("password", "NOTMYPWD");
      p.put("prompt", "false");

      Connection c = driver_.connect(url, p);

      failed("Did not throw exception.  This variation is expected to fail if a secondary URL was specified. " + c);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * connect() - When a valid URL is specified but it contains an unrecognized
   * property, then a warning is posted, but we can still run statements.
   **/
  public void Var010() {
    if (checkPasswordLeak()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC Driver does not permit null USERID");
      } else {

        try {
          String url = baseURL_ + ";user=" + userId_ + ";himom=look at me" + ";password="
              + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.10");
          Properties p = new Properties();

          Connection c = driver_.connect(url, p);
          SQLWarning w = c.getWarnings();

          // Run a query.
          Statement s = c.createStatement();
          ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          boolean found = rs.next();

          // Close the connection.
          c.close();

          assertCondition((found) && (w != null), "found=" + found + " sb true warning is " + w);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * "errors" property - Set to "basic", verify that only first level text is
   * returned.
   **/
  public void Var011() {
    if (checkPasswordLeak()) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", userId_);
        p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.11"));
        p.put("errors", "basic");

        Connection c = driver_.connect(url, p);

        // Run a query.
        Statement s = c.createStatement();
        String messageText = null;
        try {
          s.executeQuery("SELECT * FROM QIWS.BADNAME");
          messageText = "QUERY:  SELECT * FROM QIWS.BADNAME worked";
        } catch (SQLException e) {
          messageText = e.getMessage();
        }
        s.close();

        // Close the connection.
        c.close();

        assertCondition(messageText.indexOf(". . .") == -1, "Did not find '. . .' in '" + messageText + "'");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * "errors" property - Set to "full", verify that first and second level text is
   * returned.
   **/
  public void Var012() {
    if (checkPasswordLeak()) {

      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC Driver does not have 'errors' property");
      } else {

        try {
          String url = baseURL_;
          Properties p = new Properties();
          p.put("user", userId_);
          p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.12"));
          p.put("errors", "full");

          Connection c = driver_.connect(url, p);

          // Run a query.
          Statement s = c.createStatement();
          String messageText = null;
          try {
            s.executeQuery("SELECT * FROM QIWS.BADNAME");
            messageText = "ERROR:  query worked";
          } catch (SQLException e) {
            messageText = e.getMessage();
          }
          s.close();

          // Close the connection.
          c.close();

          assertCondition(messageText.indexOf(". . .") > 0);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * "secure" property - Verify that secure connections work.
   * 
   * <p>
   * You need the certificate (e.g. KeyRing.class) in the classpath to get this to
   * work. Also need the com.ibm.sslight package in the classpath.
   * 
   * <p>
   * Also, the following command needs to be run once (on any client machine) for
   * each AS/400 that you're testing to. Note that once this command has been run
   * from *any* client machine for a particular AS/400, that AS/400 is good until
   * the next scratch-install.
   * 
   * java com.ibm.sslight.nlstools.keyrng com.ibm.as400.access.KeyRing connect
   * rchasxxx:9475
   * 
   * (The 'nlstools' package can be found in the SSLTools.zip file.)
   * 
   * SQL400 - The native driver does not support the secure property today. I
   * don't think it would make sense to add it given what I understand of it
   * today. There is no network layer to the native driver.
   **/
  public void Var013() {
    if (checkPasswordLeak()) {

      if (checkNotGroupTest()) {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          notApplicable("'secure' property not supported for Native JDBC driver.");
          return;
        }

        if (proxy_ != null && (!proxy_.equals(""))) {
          notApplicable("'secure' property does not work for proxy driver");
          return;
        }

        try {
          String url = baseURL_;
          Properties p = new Properties();
          p.put("user", userId_);
          p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.13"));
          p.put("secure", "true");

          Connection c = driver_.connect(url, p);

          // Run a query.
          Statement s = c.createStatement();
          ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          int rows = 0;
          while (rs.next())
            ++rows;
          rs.close();
          s.close();

          // Close the connection.
          c.close();

          assertCondition(rows >= 10);
        } catch (Exception e) {
          failed(e, "SSL not configured. " + "If you get a certificate chaining error, you need "
              + "to get the CA certificate and install it using "
              + "keytool -import -keystore /QOpenSys/QIBM/ProdData/JavaVM/jdk60/32bit/jre/lib/security/cacerts -file /tmp/cert.ca2 -storepass changeit");
        }
      }
    }
  }

  /**
   * "trace" property - Set to "false", verify that driver manager tracing is not
   * turned on.
   **/
  public void Var014() {
    if (checkPasswordLeak()) {

      // @A1C Changed variation because if user has set tracing on
      // @A1C previous to this call, the code no longer turns it off.
      if (isToolboxDriver() && DriverManager.getLogWriter() != null) // @A1A @A3C
      {
        notApplicable("variation is no longer applicable"); // @A1A
        return; // @A1A
      } // @A1A
      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", userId_);
        p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.14"));
        p.put("trace", "false");

        Connection c = driver_.connect(url, p);

        boolean trace = (DriverManager.getLogWriter() != null);

        // Close the connection.
        c.close();

        assertCondition(trace == false);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * "trace" property - Set to "true", verify that driver manager tracing is
   * turned on.
   */
  public void Var015() {
    if (checkPasswordLeak()) {

      PrintWriter before = DriverManager.getLogWriter();
      if (isToolboxDriver() || getDriver() == JDTestDriver.DRIVER_NATIVE) {
        // If there is a log writer or log stream in the DriverManager, then
        // according to the DriverManager javadoc, all JDBC drivers should be
        // logging. If there is not one set (e.g. set to null), all drivers
        // should not be logging. However, since we have our own Toolbox tracing,
        // we are
        // nicer than that. We let the user turn off Toolbox JDBC tracing without
        // turning off DriverManager tracing. And vice-versa: If Toolbox JDBC
        // tracing is
        // turned on via the Trace class, we will log our JDBC traces there... we
        // don't
        // muck with the DriverManager logging until someone sets a log writer or
        // log
        // stream into it.
        DriverManager.setLogWriter(new PrintWriter(System.out));
      }
      Properties p = new Properties();
      String url = baseURL_;

      try {
        p.put("user", userId_);
        p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.15"));
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          File file = new File("/tmp/jcc.trace");
          if (file.exists()) {
            file.delete();
          }
          p.put("traceLevel", "255");
          p.put("traceFile", "/tmp/jcc.trace");
        } else if (getDriver() == JDTestDriver.DRIVER_JCC) {
          p.put("debug", "true");
        } else {
          p.put("trace", "true");

        }
        output_.println("");
        output_.println("Note to tester:");
        output_.println("Some trace information may appear here.");
        output_.println("This is okay, since this variation is");
        output_.println("testing that JDBC tracing works correctly.");
        output_.println("");

        Connection c = driver_.connect(url, p);

        boolean trace;
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          File file = new File("/tmp/jcc.trace");
          trace = file.exists();
        } else {
          trace = (DriverManager.getLogWriter() != null);
        }
        // Close the connection.
        c.close();

        assertCondition(trace == true, "Error File does not exist");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        /* if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) //@A3A */// @B1A
        // If it's anything BUT the native driver, use the properties.
        // If it's the native driver, then you MUST set the logstream to null.
        // if (getDriver() != JDTestDriver.DRIVER_NATIVE) //@B1A
        // {
        // Turn off tracing using command in reverse.
        // try
        // {
        // p.put("trace", "false"); //@A1A
        // driver_.connect(url, p); //@A1A
        // }
        // catch (Exception e)
        // {
        // failed(e,"Unexpected Exception");
        // }
        // }
        // else
        DriverManager.setLogWriter(before);
      }
    }
  }

  /**
   * "driver" property - Set to "toolbox", verify that the connection works.
   */
  public void Var016() {
    if (checkPasswordLeak()) {

      try {
        if (checkToolbox()) {
          String url = baseURL_;
          Properties p = new Properties();
          p.put("user", userId_);
          p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.16"));
          p.put("driver", "toolbox");

          Connection c = driver_.connect(url, p);
          if (c != null) {
            c.close();
          }
          assertCondition(c != null);
        } 
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * "driver" property - Set to "native", verify that the connection works.
   **/
  public void Var017() {
    if (checkPasswordLeak()) {

      try {
        if (checkToolbox()) 
        {
          String url = baseURL_;
          Properties p = new Properties();
          p.put("user", userId_);
          p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.17"));
          p.put("driver", "native");

          Connection c = driver_.connect(url, p);
          if (c != null) {
            c.close();
          }
          assertCondition(c != null);
        } 
      } catch (Exception e) {
        failed(e, "Unexpected Exception connecting with native and " + userId_);
      }
    }
  }

  /**
   * connect(AS400) - Passing a valid AS400 object should complete successfully.
   **/
  public void Var018() {
    if (!isToolboxDriver()) {
      notApplicable("connect(AS400) variation");
      return;
    }

    try {
      AS400JDBCDriver d = new AS400JDBCDriver();
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 o = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
      PasswordVault.clearPassword(charPassword);

      Connection c = d.connect(o);

      // Run a query.
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
      int rows = 0;
      while (rs.next())
        ++rows;
      rs.close();
      s.close();

      // Close the connection.
      c.close();

      assertCondition(rows >= 10);
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * connect(AS400) - Passing a null AS400 object should throw null pointer
   * exception.
   **/
  public void Var019() {
    if (!isToolboxDriver()) {
      notApplicable("connect(AS400) variation");
      return;
    }
    try {
      AS400JDBCDriver d = new AS400JDBCDriver();
      AS400 o = null;

      Connection c = d.connect(o);
      failed("Did not throw exception. " + c);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * connect(String, Properties, AS400) - Passing valid objects should complete
   * successfully.
   **/
  public void Var020() {
    if (!isToolboxDriver()) {
      notApplicable("connect(AS400) variation");
      return;
    }

    try {
      AS400JDBCDriver d = new AS400JDBCDriver();
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 o = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
      PasswordVault.clearPassword(charPassword);
      String schema = null;
      Properties p = new Properties();

      Connection c = d.connect(o, p, schema);

      // Run a query.
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
      int rows = 0;
      while (rs.next())
        ++rows;
      rs.close();
      s.close();

      // Close the connection.
      c.close();

      assertCondition(rows >= 10);
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /**
   * connect(String, Properties, AS400) - Passing valid objects and setting
   * "driver" property to "native" should complete successfully.
   **/
  public void Var021() {
    if (checkPasswordLeak()) {

      if (!isToolboxDriver()) {
        notApplicable("connect(AS400) variation");
        return;
      }

      try {
        AS400JDBCDriver d = new AS400JDBCDriver();
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 o = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
        PasswordVault.clearPassword(charPassword);
        String schema = null;
        Properties p = new Properties();

        p.put("user", userId_);
        p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.21"));
        p.put("driver", "native");

        Connection c = d.connect(o, p, schema);

        // Run a query.
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
        int rows = 0;
        while (rs.next())
          ++rows;
        rs.close();
        s.close();

        // Close the connection.
        c.close();

        assertCondition(rows >= 10);
      } catch (Exception e) {
        failed(e, "Unexpected exception");
      }
    }
  }

  /**
   * connect(String, Properties, AS400) - Passing valid objects and setting
   * "driver" property to "toolbox" should complete successfully.
   **/
  public void Var022() {
    if (checkPasswordLeak()) {

      if (!isToolboxDriver()) {
        notApplicable("connect(AS400) variation");
        return;
      }

      try {
        AS400JDBCDriver d = new AS400JDBCDriver();
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        AS400 o = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
        PasswordVault.clearPassword(charPassword);
        String schema = null;
        Properties p = new Properties();

        p.put("user", userId_);
        p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.22"));
        p.put("driver", "toolbox");

        Connection c = d.connect(o, p, schema);

        // Run a query.
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
        int rows = 0;
        while (rs.next())
          ++rows;
        rs.close();
        s.close();

        // Close the connection.
        c.close();

        assertCondition(rows >= 10);
      } catch (Exception e) {
        failed(e, "Unexpected exception");
      }
    }
  }

  /**
   * connect(AS400, Properties, String) - Passing a null Properties object should
   * throw a null pointer exception.
   **/
  public void Var023() {
    if (!isToolboxDriver()) {
      notApplicable("connect(AS400) variation");
      return;
    }

    try {
      AS400JDBCDriver d = new AS400JDBCDriver();
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 o = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
      PasswordVault.clearPassword(charPassword);
      Properties p = null;
      String schema = null;

      Connection c = d.connect(o, p, schema);
      failed("Did not throw exception. " + c);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * connect(AS400, Properties, String) - Passing a null AS400 object should throw
   * null pointer exception.
   **/
  public void Var024() {
    if (!isToolboxDriver()) {
      notApplicable("connect(AS400) variation");
      return;
    }

    try {
      AS400JDBCDriver d = new AS400JDBCDriver();
      AS400 o = null;
      String schema = null;
      Properties p = new Properties();

      Connection c = d.connect(o, p, schema);
      failed("Did not throw exception. " + c);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * connect(SecureAS400) - Passing a valid SecureAS400 object should complete
   * successfully.
   **/
  public void Var025() {
    if (checkNotGroupTest()) {
      if (!isToolboxDriver()) {
        notApplicable("connect(AS400) variation");
        return;
      }

      if (proxy_ != null && (!proxy_.equals(""))) {
        notApplicable("SecureAS400 object does not work for proxy driver");
        return;
      }

      try {
        AS400JDBCDriver d = new AS400JDBCDriver();
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        SecureAS400 o = new SecureAS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
        PasswordVault.clearPassword(charPassword);

        Connection c = d.connect(o);

        // Run a query.
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
        int rows = 0;
        while (rs.next())
          ++rows;
        rs.close();
        s.close();

        // Close the connection.
        c.close();

        assertCondition(rows >= 10);
      } catch (Exception e) {
        failed(e, "SSL not configured.");
      }
    }
  }

  /**
   * connect(SecureAS400) - Passing a null SecureAS400 object should throw null
   * pointer exception.
   **/
  public void Var026() {
    if (!isToolboxDriver()) {
      notApplicable("connect(AS400) variation");
      return;
    }
    try {
      AS400JDBCDriver d = new AS400JDBCDriver();
      SecureAS400 o = null;

      Connection c = d.connect(o);
      failed("Did not throw exception. " + c);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * connect(String, Properties, SecureAS400) - Passing valid objects should
   * complete successfully.
   **/
  public void Var027() {
    if (checkNotGroupTest()) {
      if (!isToolboxDriver()) {
        notApplicable("connect(AS400) variation");
        return;
      }

      if (proxy_ != null && (!proxy_.equals(""))) {
        notApplicable("SecureAS400 object does not work for proxy driver");
        return;
      }

      try {
        AS400JDBCDriver d = new AS400JDBCDriver();
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

        SecureAS400 o = new SecureAS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
        PasswordVault.clearPassword(charPassword);
        String schema = null;
        Properties p = new Properties();

        Connection c = d.connect(o, p, schema);

        // Run a query.
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
        int rows = 0;
        while (rs.next())
          ++rows;
        rs.close();
        s.close();

        // Close the connection.
        c.close();

        assertCondition(rows >= 10);
      } catch (Exception e) {
        failed(e, "SSL not configured.");
      }
    }
  }

  /**
   * connect(String, Properties, SecureAS400) - Passing valid objects and setting
   * "driver" property to "native" should complete successfully.
   **/
  public void Var028() {
    if (checkPasswordLeak()) {

      if (checkNotGroupTest()) {
        if (!isToolboxDriver()) {
          notApplicable("connect(AS400) variation");
          return;
        }

        if (proxy_ != null && (!proxy_.equals(""))) {
          notApplicable("SecureAS400 object does not work for proxy driver");
          return;
        }

        // @B2A

        try {
          AS400JDBCDriver d = new AS400JDBCDriver();
          char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
          SecureAS400 o = new SecureAS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
          PasswordVault.clearPassword(charPassword);
          String schema = null;
          Properties p = new Properties();

          p.put("user", userId_);
          p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.28"));
          p.put("driver", "native");

          Connection c = d.connect(o, p, schema);

          // Run a query.
          Statement s = c.createStatement();
          ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          int rows = 0;
          while (rs.next())
            ++rows;
          rs.close();
          s.close();

          // Close the connection.
          c.close();

          assertCondition(rows >= 10);
        } catch (Exception e) {
          failed(e, "SSL not configured.");
        }
      }
    }
  }

  /**
   * connect(String, Properties, SecureAS400) - Passing valid objects and setting
   * "driver" property to "toolbox" should complete successfully.
   **/
  public void Var029() {
    if (checkPasswordLeak()) {

      if (checkNotGroupTest()) {
        if (!isToolboxDriver()) {
          notApplicable("connect(AS400) variation");
          return;
        }

        if (proxy_ != null && (!proxy_.equals(""))) {
          notApplicable("SecureAS400 object does not work for proxy driver");
          return;
        }

        try {
          AS400JDBCDriver d = new AS400JDBCDriver();
          char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
          SecureAS400 o = new SecureAS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
          PasswordVault.clearPassword(charPassword);
          String schema = null;
          Properties p = new Properties();

          p.put("user", userId_);
          p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.29"));
          p.put("driver", "toolbox");

          Connection c = d.connect(o, p, schema);

          // Run a query.
          Statement s = c.createStatement();
          ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          int rows = 0;
          while (rs.next())
            ++rows;
          rs.close();
          s.close();

          // Close the connection.
          c.close();

          assertCondition(rows >= 10);
        } catch (Exception e) {
          failed(e, "SSL not configured.");
        }
      }
    }
  }

  /**
   * connect(SecureAS400, Properties, String) - Passing a null Properties object
   * should throw a null pointer exception.
   **/
  public void Var030() {
    if (!isToolboxDriver()) {
      notApplicable("connect(AS400) variation");
      return;
    }

    try {
      AS400JDBCDriver d = new AS400JDBCDriver();
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      SecureAS400 o = new SecureAS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
      PasswordVault.clearPassword(charPassword);
      Properties p = null;
      String schema = null;

      Connection c = d.connect(o, p, schema);
      failed("Did not throw exception. " + c);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * connect(SecureAS400, Properties, String) - Passing a null AS400 object should
   * throw null pointer exception.
   **/
  public void Var031() {
    if (!isToolboxDriver()) {
      notApplicable("connect(AS400) variation");
      return;
    }

    try {
      AS400JDBCDriver d = new AS400JDBCDriver();
      SecureAS400 o = null;
      String schema = null;
      Properties p = new Properties();

      Connection c = d.connect(o, p, schema);
      failed("Did not throw exception. " + c);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

  /**
   * Get the user for the connection
   */
  public String getUser(Connection c) {
    String user = "NOT AVAILABLE";
    try {
      Statement stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("select user from qsys2.qsqptabl");
      rs.next();
      user = rs.getString(1);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return user;
  }

  /**
   * connect() - When a valid URL is specified, but the password is empty, then an
   * exception should be thrown.
   **/
  public void Var032() {
    if (checkNative()) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "QSECOFR");
        p.put("password", "");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed("Did not throw exception when passing empty password but passing userId   = " + getUser(c)
            + "-- native driver added 8/19/2003");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * connect() - When a valid URL is specified, but the password is not set, then
   * an exception should be thrown.
   **/
  public void Var033() {
    if (checkNative()) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "QSECOFR");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed("Did not throw exception when not passing password but passing userId  = " + getUser(c)
            + " -- native driver added 8/19/2003");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * connect() - When a valid URL is specified, but the password is *NONE, then an
   * exception should be thrown.
   **/
  public void Var034() {
    if (checkNative()) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "QSECOFR");
        p.put("password", "*NONE");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed("Did not throw exception when passing *NONE password but passing userId = " + getUser(c)
            + "  -- native driver added 8/19/2003");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * connect() - When a valid URL is specified, but the password is **********,
   * then an exception should be thrown.
   **/
  public void Var035() {
    if (checkNative()) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "QSECOFR");
        p.put("password", "**********");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed("Did not throw exception when passing ******** password but passing userId = " + getUser(c)
            + "  -- native driver added 8/19/2003");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * Conenct using db2j as the subprotocol, an exception should be thrown since
   * db2j is the Cloudscape driver. Code in DB2Driver.java was changed to check
   * the subprotocol using equals instead of startsWith to account for this.
   **/
  public void Var036() {
    if (checkPasswordLeak()) {

      if (checkNative()) {

        try {
          String url = "jdbc:db2j:localhost";
          Properties p = new Properties();
          p.put("user", userId_);
          p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.36"));

          Connection c = driver_.connect(url, p);
          assertCondition(c == null,
              "Connection was not null when using db2j as subprotocol -- native driver added 3/3/2004");
        } catch (Exception e) {
          failed(e, "Unexpected Exception -- added by native 3/3/2004");
        }
      }
    }
  }

  /**
   * connect() - When a valid URL is specified, but the user and id are empty,
   * then an exception should be thrown.
   **/
  public void Var037() {
    if (checkNative()) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "");
        p.put("password", "");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed(
            "Did not throw exception when passing userid and password as blank -- native driver added 12/11/2007" + c);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are *CURRENT,
   * then an exception should be thrown.
   **/
  public void Var038() {
    if (checkNative()) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "*CURRENT");
        p.put("password", "*CURRENT");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed("Did not throw exception when passing userid and password as *CURRENT -- native driver added 12/11/2007 "
            + c);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are empty,
   * then an exception should be thrown.
   **/
  public void Var039() {
    if (checkNative()) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "       ");
        p.put("password", "       ");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed(
            "Did not throw exception when passing userid and password as many blanks -- native driver added 12/11/2007 "
                + c);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are *CURRENT,
   * then an exception should be thrown.
   **/
  public void Var040() {
    if (checkNative()) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "         *CURRENT");
        p.put("password", "*CURRENT");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed(
            "Did not throw exception when passing userid and password as '   *CURRENT' -- native driver added 12/11/2007 "
                + c);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are *CURRENT,
   * then an exception should be thrown.
   **/
  public void Var041() {
    if (checkNative()) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "*cUrReNt");
        p.put("password", "*CURRENT");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed(
            "Did not throw exception when passing userid and password as '   *cUrReNt' -- native driver added 12/11/2007 "
                + c);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are empty,
   * then an exception should be thrown.
   **/
  public void Var042() {
    if (checkPasswordLeak()) {

      if (checkNative()) {
        try {
          String url = baseURL_;
          Properties p = new Properties();
          p.put("user", "");
          p.put("password", "");
          p.put("prompt", "false");

          Connection c = driver_.connect(url, p);
          c.close();
          failed("Did not throw exception when passing userid and password as blanks  ");

        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * Passing *CURRENT
   **/
  public void Var043() {
    if (checkPasswordLeak()) {

      if (checkNative()) {

        try {
          String url = baseURL_;
          Properties p = new Properties();
          p.put("user", "*CURRENT");
          p.put("password", "*CURRENT");
          p.put("prompt", "false");

          Connection c = driver_.connect(url, p);
          c.close();

        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");

        }
      }

    }
  }

  /**
   * connect() - When a valid URL is specified, but the user and id are empty,
   * then an exception should be thrown.
   **/
  public void Var044() {
    if (checkPasswordLeak()) {

      if (checkNative()) {
        try {
          String url = baseURL_;
          Properties p = new Properties();
          p.put("user", "       ");
          p.put("password", "       ");
          p.put("prompt", "false");

          Connection c = driver_.connect(url, p);
          c.close();

          failed("Did not throw exception when passing userid and password as blanks  ");

        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * connect() - When a valid URL is specified, but the user and id are *CURRENT,
   * then an exception should be thrown.
   **/
  public void Var045() {
    if (checkPasswordLeak()) {

      if (checkNative()) {

        try {
          String url = baseURL_;
          Properties p = new Properties();
          p.put("user", "         *CURRENT");
          p.put("password", "*CURRENT");
          p.put("prompt", "false");

          Connection c = driver_.connect(url, p);
          c.close();

          failed("Did not throw exception when passing userid and password as *CURRENT  ");

        } catch (Exception e) {

          assertExceptionIsInstanceOf(e, "java.sql.SQLException");

        }
      }
    }
  }

  /**
   * connect() - When a valid URL is specified, but the user and id are *CURRENT,
   * then an exception should be thrown.
   **/
  public void Var046() {
    if (checkPasswordLeak()) {

      if (checkNative()) {
        try {
          String url = baseURL_;
          Properties p = new Properties();
          p.put("user", "*cUrReNt");
          p.put("password", "*CURRENT");
          p.put("prompt", "false");

          Connection c = driver_.connect(url, p);
          c.close();
          failed("Did not throw exception when passing userid and password as *CURRENT ");

        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");

        }
        return;
      }
    }
  }

  /**
   * connect() - Should throw exception when id/pass are "" or *CURRENT With no
   * user/password in url properties
   **/
  public void Var047() {
    Connection conn1;
    if (!JTOpenTestEnvironment.isOS400 || JTOpenTestEnvironment.isOS400open) {
      notApplicable("i5 platform TC");
      return;
    }
    if (proxy_ != null && (!proxy_.equals(""))) {
      notApplicable("*CURRENT does not work for proxy driver");
      return;
    }
    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    try {
      String url = baseURL_ + ";prompt=false"; // no user or password in
                                               // properties

      // part 0
      try {
        conn1 = DriverManager.getConnection(url);
      } catch (Exception e) {
        sb.append("***** part0 failed to connect ** url=" + url + " Stack trace is\n");
        passed = false;
        printStackTraceToStringBuffer(e, sb);

      }

      // part 1;
      try {
        conn1 = DriverManager.getConnection(url, null, null);
      } catch (Exception e) {
        sb.append("part1 failed to connect;  \n");
        passed = false;
        printStackTraceToStringBuffer(e, sb);
      }

      // part 2
      try {
        conn1 = DriverManager.getConnection(url, "wrong", "wrong");
        sb.append("part2 connected with wrong id/pass;  \n");
        passed = false;
      } catch (Exception e) {
        // failed as expected
      }

      // part 3
      try {
        conn1 = DriverManager.getConnection(url, "", "");// use current?
        sb.append("part3 connected with emptystring id/pass;  \n");
        passed = false;
      } catch (Exception e) {
        // failed as expected
      }

      // part 4
      try {
        conn1 = DriverManager.getConnection(url, "*CURRENT", "*CURRENT");// use
                                                                         // current?
        sb.append("part4 connected with *CURRENT id/pass to " + conn1 + ";  \n");
        passed = false;
      } catch (Exception e) {
        // failed as expected
      }

      // part 5
      try {
        conn1 = DriverManager.getConnection(url, "javactl", "*CURRENT");// use
                                                                        // current?
        sb.append("part5 connected with *CURRENT pass;  \n");
        passed = false;
      } catch (Exception e) {
        // failed as expected
      }

      assertCondition(passed, sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception" + sb.toString());
    }

  }

  /**
   * connect() - Should throw exception when id/pass are "" or *CURRENT With some
   * user/password in url properties
   **/
  public void Var048() {
    if (checkPasswordLeak()) {

      /*
       * if(!JDTestDriver.getClientOS().equals( JDTestDriver.CLIENT_as400)){
       * notApplicable("i5 platform TC"); return; }
       */
      if (toolboxNative) {
        notApplicable("toolbox native allows connect");
        return;
      }
      try {
        String url = baseURL_ + ";prompt=false;";

        String stats = "";
        Connection conn1;
        // part 0
        try {
          String url2 = url + "user=;password=";
          conn1 = DriverManager.getConnection(url2);
          if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX)
              && (JDTestDriver.getClientOS().equals(JDTestDriver.CLIENT_as400))) {
            // Toolbox local allows this to work
          } else {
            stats += "part0 connected with empty user/pass in url;  ";
          }
        } catch (Exception e) {
          // failed as expected
        }

        // part 1;
        try {
          String url2 = url + "user=;password=";
          conn1 = DriverManager.getConnection(url2, null, null);
          if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX)
              && (JDTestDriver.getClientOS().equals(JDTestDriver.CLIENT_as400))) {
            // Toolbox local allows this to work
          } else {

            stats += "part1 connected with empty user/pass in url;  ";
          }
        } catch (Exception e) {
          // failed as expected
        }

        // part 2
        try {
          String url2 = url + "user=wrong;password=wrong";
          conn1 = DriverManager.getConnection(url2);
          stats += "part2 connected with wrong id/pass to " + conn1 + ";  ";
        } catch (Exception e) {
          // failed as expected
        }

        // part 3
        try {
          String url2 = url + "user=;password=";
          conn1 = DriverManager.getConnection(url2, "", "");
          stats += "part3 connected with emptystring id/pass;  ";
        } catch (Exception e) {
          // failed as expected
        }

        // part 4
        try {
          String url2 = url + "user=*CURRENT;password=*CURRENT";
          conn1 = DriverManager.getConnection(url2);
          stats += "part4 connected with *CURRENT id/pass;  ";
        } catch (Exception e) {
          // failed as expected
        }

        // part 4a make sure we still can connect with userid password
        try {
          String urlB = url + ";user=" + userId_ + ";password="
              + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.48");// should
          // override
          // ""
          // parms
          conn1 = DriverManager.getConnection(urlB, "", "");

        } catch (Exception e) {
          stats += "part4a failed to connect;  ";
        }

        // part 5
        try {
          String url2 = url + "user=javactl;password=*CURRENT";
          conn1 = DriverManager.getConnection(url2);
          stats += "part5 connected with *CURRENT pass;  ";
        } catch (Exception e) {
          // failed as expected
        }

        // part 6 (code update 2)
        try {
          String urlB = url + ";user=" + userId_ + ";password="
              + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.48");// should
          // override
          // ""
          // parms
          conn1 = DriverManager.getConnection(urlB, "", "");

        } catch (Exception e) {
          stats += "part6 failed to connect;  ";
        }

        // part 7 (code update 2)
        try {
          Properties props = new Properties();
          props.put("user", "");
          props.put("password", "");
          String urlB = url + ";user=" + userId_ + ";password="
              + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.48");// should
          // override
          // ""
          // parms
          conn1 = DriverManager.getConnection(urlB, props);

        } catch (Exception e) {
          stats += "part7 failed to connect;  ";
        }

        assertCondition(stats.equals(""), stats);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * connect() - Should throw exception when id/pass are "" or *CURRENT With
   * user/password in properties object
   **/
  public void Var049() {
    if (checkPasswordLeak()) {

      if (!JTOpenTestEnvironment.isOS400 || JTOpenTestEnvironment.isOS400open) {
        notApplicable("i5 platform TC");
        return;
      }
      if (proxy_ != null && (!proxy_.equals(""))) {
        notApplicable("*CURRENT does not work for proxy driver");
        return;
      }

      try {

        String url = baseURL_;
        Connection conn1 = null;
        String passwordLeak = PasswordVault.decryptPasswordLeak(testDriver_.pwrSysEncryptedPassword_,
            "JDDriverConnect Var049");
        Connection pwrConn = DriverManager.getConnection(url, testDriver_.pwrSysUserID_, passwordLeak);// use current

        String stats = "";

        // part 0
        try {
          Properties p = new Properties();
          /// p.put ("user", "*cUrReNt");
          // p.put ("password", "*CURRENT");
          p.put("prompt", "false");

          String url2 = url;
          conn1 = DriverManager.getConnection(url2, p);

        } catch (Exception e) {
          stats += "part0 failed to connect;  ";
        }

        // part 1;
        try {
          Properties p = new Properties();
          p.put("user", "");
          p.put("password", "");
          p.put("prompt", "false");
          String url2 = url;
          conn1 = DriverManager.getConnection(url2, p);
          stats += "part1 connected with empty user/pass in url;  ";
        } catch (Exception e) {
          // failed as expected
        }

        // part 1a; Always do a good connect after a bad one to keep the profile
        // from being disabled.
        try {
          String urlB = baseURL_ + ";errors=full";
          conn1 = DriverManager.getConnection(urlB, userId_,
              PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.49"));

        } catch (Exception e) {
          System.err.println("Exception");
          e.printStackTrace();
          System.err.flush();
          stats += "part2a failed to connect;  ";
        }

        // part 2
        try {
          Properties p = new Properties();
          p.put("user", "wrong");
          p.put("password", "wrong");
          p.put("prompt", "false");
          String url2 = url;
          conn1 = DriverManager.getConnection(url2, p);
          stats += "part2 connected with wrong id/pass " + conn1 + ";  ";
        } catch (Exception e) {
          // failed as expected
        }

        // part 2a; Always do a good connect after a bad one to keep the profile
        // from being disabled.
        try {
          String urlB = baseURL_ + ";errors=full";
          conn1 = DriverManager.getConnection(urlB, userId_,
              PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.49"));

        } catch (Exception e) {
          System.err.println("Exception");
          e.printStackTrace();
          System.err.flush();
          stats += "part2a failed to connect;  ";
        }

        // part 4
        try {
          Properties p = new Properties();
          p.put("user", "*current");
          p.put("password", "*current");
          p.put("prompt", "false");
          String url2 = url;
          conn1 = DriverManager.getConnection(url2, p);
          stats += "part4 connected with *CURRENT id/pass;  ";
        } catch (Exception e) {
          // failed as expected
        }

        for (int i = 0; i < 20; i++) {
          // part 4a make sure we still can connect with userid password
          try {
            String urlB = baseURL_ + ";errors=full";
            conn1 = DriverManager.getConnection(urlB, userId_,
                PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.49"));
            conn1.close();
          } catch (Exception e) {
            System.err.println("Exception");
            e.printStackTrace();
            System.err.flush();
            stats += "part4a failed to connect;  ";
          }
        }

        // part 5
        try {
          Properties p = new Properties();
          p.put("user", userId_);
          p.put("password", "*current");
          p.put("prompt", "false");
          String url2 = url;
          conn1 = DriverManager.getConnection(url2, p);
          stats += "part5 connected with *CURRENT pass;  ";
        } catch (Exception e) {
          // failed as expected
        }

        //
        // Part 5 sometimes disables the profile... Re-enable it
        //
        Statement stmt = pwrConn.createStatement();
        String sql = "CALL QSYS2.QCMDEXC('CHGUSRPRF USRPRF(" + userId_ + ") STATUS(*ENABLED)   ')";
        stmt.executeUpdate(sql);

        // part 5a make sure we still can connect with userid password
        try {
          String urlB = baseURL_ + ";errors=full";
          conn1 = DriverManager.getConnection(urlB, userId_,
              PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.49"));

        } catch (Exception e) {
          System.err.println("Exception");
          e.printStackTrace();
          System.err.flush();
          System.out.flush();
          stats += "part5a failed to connect;  ";
        }

        pwrConn.close();
        assertCondition(stats.equals(""), stats);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * connect() - Should throw exception when id/pass are "" or *CURRENT With no
   * user/password in url properties copy of 047 but with secure current user =
   * false
   **/
  public void Var050() {
    if (!isToolboxDriver()) {
      notApplicable("Toolbox TC");
      return;
    }
    if (!JTOpenTestEnvironment.isOS400 || JTOpenTestEnvironment.isOS400open) {
      notApplicable("i5 platform TC");
      return;
    }
    if (proxy_ != null && (!proxy_.equals(""))) {
      notApplicable("*CURRENT does not work for proxy driver");
      return;
    }

    try {
      String url = baseURL_ + ";prompt=false;secure current user=false"; // no
                                                                         // user
                                                                         // or
                                                                         // password
                                                                         // in
                                                                         // properties

      String stats = "";
      Connection conn1;
      // part 0
      try {
        conn1 = DriverManager.getConnection(url);
      } catch (Exception e) {
        stats += "part0 failed to connect;  ";
      }

      // part 1;
      try {
        conn1 = DriverManager.getConnection(url, null, null);
      } catch (Exception e) {
        stats += "part1 failed to connect;  ";
      }

      // part 2
      try {
        conn1 = DriverManager.getConnection(url, "wrong", "wrong");
        stats += "part2 connected with wrong id/pass " + conn1 + ";  ";
      } catch (Exception e) {
        // failed as expected
      }

      // part 3
      try {
        conn1 = DriverManager.getConnection(url, "", "");// use current
        stats += "part3 connected with blank userid/password;   ";
      } catch (Exception e) {
        // Should now fail, Jan 2023

      }

      // part 4
      try {
        conn1 = DriverManager.getConnection(url, "*CURRENT", "*CURRENT");// use
                                                                         // current
        stats += "part4 connected *CURRENT *CURRENT ;  ";
      } catch (Exception e) {
        // should now fail , Jan 2023
      }

      // part 5
      try {
        // try connecting with either "java" or "javactl"...
        try {
          conn1 = DriverManager.getConnection(url, testDriver_.pwrSysUserID_, "*CURRENT");// use current
          stats += "part5 connected with *CURRENT password";
        } catch (Exception e1) {
          conn1 = DriverManager.getConnection(url, System.getProperty("user.name"), "*CURRENT");// use current
          stats += "part5 connected with *CURRENT password";
        }
      } catch (Exception e) {
        // Should fail, Jan 2023

      }

      assertCondition(stats.equals(""), stats);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }

  }

  /**
   * connect() - Should throw exception when id/pass are "" or *CURRENT With some
   * user/password in url properties copy of 048 but with secure current user =
   * false
   **/
  public void Var051() {
    if (checkPasswordLeak()) {

      if (!isToolboxDriver()) {
        notApplicable("Toolbox TC");
        return;
      }
      if (!JDTestDriver.getClientOS().equals(JDTestDriver.CLIENT_as400)) {
        notApplicable("i5 platform TC");
        return;
      }
      if (proxy_ != null && (!proxy_.equals(""))) {
        notApplicable("*CURRENT does not work for proxy driver");
        return;
      }

      try {
        String url = baseURL_ + ";prompt=false;secure current user=false;";

        String stats = "";
        Connection conn1;

        // part 0
        try {
          String url2 = url + "user=;password=";
          conn1 = DriverManager.getConnection(url2);
          stats += "part0 conneced with blank userid /password;  ";
        } catch (Exception e) {

        }

        // part 1;
        try {
          String url2 = url + "user=;password=";
          conn1 = DriverManager.getConnection(url2, null, null);
          stats += "part1 conneced with blank userid /password;  ";
        } catch (Exception e) {

        }

        // part 2
        try {
          String url2 = url + "user=wrong;password=wrong";
          conn1 = DriverManager.getConnection(url2);
          stats += "part2 connected with wrong id/pass " + conn1 + " ;  ";
        } catch (Exception e) {
          // failed as expected
        }

        // part 3
        try {
          String url2 = url + "user=;password=";
          conn1 = DriverManager.getConnection(url2, "", "");
          stats += "part3 connected with blank userid/password;  ";
        } catch (Exception e) {

        }

        // part 4
        try {
          String url2 = url + "user=*CURRENT;password=*CURRENT";
          conn1 = DriverManager.getConnection(url2);
          stats += "part4 connected with *CURRENT userid/password;  ";

        } catch (Exception e) {

        }

        // part 5
        try {
          // try connecting with either "java" or "javactl"...
          try {

            String url2 = url + "user=" + testDriver_.pwrSysUserID_ + ";password=*CURRENT";
            conn1 = DriverManager.getConnection(url2);
            stats += "part4 connected with *CURRENT password;  ";
          } catch (Exception e1) {

            String url2 = url + "user=" + System.getProperty("user.name") + ";password=*CURRENT";
            conn1 = DriverManager.getConnection(url2);
            stats += "part4 connected with *CURRENT password;  ";
          }

        } catch (Exception e) {

        }

        // part 6 (code update 2)
        try {
          String urlB = url + ";user=" + userId_ + ";password="
              + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.51");// should
          // override
          // ""
          // parms
          conn1 = DriverManager.getConnection(urlB, "", "");

        } catch (Exception e) {
          stats += "part6 failed to connect;  ";
        }

        // part 7 (code update 2)
        try {
          Properties props = new Properties();
          props.put("user", "");
          props.put("password", "");
          String urlB = url + ";user=" + userId_ + ";password="
              + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.51");// should
          // override
          // ""
          // parms
          conn1 = DriverManager.getConnection(urlB, props);

        } catch (Exception e) {
          stats += "part7 failed to connect;  ";
        }

        assertCondition(stats.equals(""), stats);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }

    }
  }

  /**
   * connect() - Should throw exception when id/pass are "" or *CURRENT With
   * user/password in properties object * copy of 049 but with secure current user
   * = false
   **/
  public void Var052() {
    if (!isToolboxDriver()) {
      notApplicable("Toolbox TC");
      return;
    }
    if (!JTOpenTestEnvironment.isOS400 || JTOpenTestEnvironment.isOS400open) {
      notApplicable("i5 platform TC");
      return;
    }
    if (proxy_ != null && (!proxy_.equals(""))) {
      notApplicable("*CURRENT does not work for proxy driver");
      return;
    }

    try {
      String url = baseURL_;

      Connection conn1;

      String stats = "";

      // part 0
      try {
        Properties p = new Properties();
        p.put("prompt", "false");
        p.put("secure current user", "false");
        String url2 = url;
        conn1 = DriverManager.getConnection(url2, p);

      } catch (Exception e) {
        stats += "part0 failed to connect with no userid/passsword; ";
      }

      // part 1;
      try {
        Properties p = new Properties();
        p.put("user", "");
        p.put("password", "");
        p.put("prompt", "false");
        p.put("secure current user", "false");
        String url2 = url;
        conn1 = DriverManager.getConnection(url2, p);
        stats += "part1 connected with blank userid/password; ";
      } catch (Exception e) {

      }

      // part 2
      try {
        Properties p = new Properties();
        p.put("user", "wrong");
        p.put("password", "wrong");
        p.put("prompt", "false");
        p.put("secure current user", "false");
        String url2 = url;
        conn1 = DriverManager.getConnection(url2, p);
        stats += "part2 connected with wrong id/pass " + conn1 + ";  ";
      } catch (Exception e) {
        // failed as expected
      }

      // part 4
      try {
        Properties p = new Properties();
        p.put("user", "*current");
        p.put("password", "*current");
        p.put("prompt", "false");
        p.put("secure current user", "false");
        String url2 = url;
        conn1 = DriverManager.getConnection(url2, p);
        stats += "part4 connected with *CURRENT/*CURRENT;  ";
      } catch (Exception e) {

      }

      // part 5
      try {
        try {
          Properties p = new Properties();
          p.put("user", userId_);
          p.put("password", "*current");
          p.put("prompt", "false");
          p.put("secure current user", "false");
          String url2 = url;
          conn1 = DriverManager.getConnection(url2, p);
          stats += "part4 connected with *CURRENT password ;  ";
        } catch (Exception e1) {

          Properties p = new Properties();
          p.put("user", System.getProperty("user.name"));
          p.put("password", "*current");
          p.put("prompt", "false");
          p.put("secure current user", "false");
          String url2 = url;
          conn1 = DriverManager.getConnection(url2, p);
          stats += "part4 connected with *CURRENT password ;  ";
        }

      } catch (Exception e) {

      }

      assertCondition(stats.equals(""), stats);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }

  }

  /**
   * connect() - Test AS400JDBCDriver.getConnection(clone=false)
   **/
  public void Var053() {
    if (!isToolboxDriver()) {
      notApplicable("Toolbox TC");
      return;
    }

    /**
     * This testcase tries to use the GUI. When running on the 400, the following
     * error is seen. 5/5/2009
     *
     * java.awt.AWTError: AWT class or API used without specifying property
     * os400.awt.native=true or java.awt.headless=true at
     * java.lang.Throwable.<init>(Throwable.java:196) at
     * java.lang.Error.<init>(Error.java:49) at
     * java.awt.AWTError.<init>(AWTError.java:30) at
     * com.ibm.as400.system.NoGraphicsEnvironment.<init>(NoGraphicsEnvironment.java:48)
     * at
     * sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
     * at
     * sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
     * at java.lang.reflect.Constructor.newInstance(Constructor.java:494) at
     * java.lang.Class.newInstance0(Class.java:375) at
     * java.lang.Class.newInstance(Class.java:328) at
     * java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment(GraphicsEnvironment.java:68)
     * at sun.awt.motif.MToolkit.<clinit>(MToolkit.java:113) at
     * java.lang.Class.forName(Class.java:189) at
     * java.awt.Toolkit$2.run(Toolkit.java:821) at
     * java.awt.Toolkit.getDefaultToolkit(Toolkit.java:804) at
     * javax.swing.UIManager.maybeInitialize(UIManager.java:1245) at
     * javax.swing.UIManager.getString(UIManager.java:659) at
     * com.ibm.as400.access.IFSSystemView.<clinit>(IFSSystemView.java:65) at
     * test.JDDriverConnect.Var053(JDDriverConnect.java:2565)
     * 
     */

    if (JDTestDriver.getClientOS().equals(JDTestDriver.CLIENT_as400)) {
      notApplicable("Test attempts to use GUI.  Not Applicable when running on 400");
      return;
    }

    if (JTOpenTestEnvironment.isLinux) {
      notApplicable("Test attempts to use GUI.  Not Applicable when running on linux");
      return;
    }

    if (JTOpenTestEnvironment.isAIX) {
      notApplicable("Test attempts to use GUI.  Not Applicable when running on AIX");
      return;
    }

    try {
      // use an AS400 object
      AS400JDBCDriver d = new AS400JDBCDriver();
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400 o = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
      PasswordVault.clearPassword(charPassword);
      IFSSystemView v = new IFSSystemView(o);
      File[] flist = v.getFiles(new File("c:\\"), true);

      // next reuse AS400 object
      Connection c = d.connect(o, false);
      c.close();

      // next reuse AS400 object again
      DriverManager.setLoginTimeout(3);
      c = d.connect(o, false);
      c.close();

      // next reuse AS400 object again
      c = d.connect(o, true);
      c.close();

      assertCondition(true, flist.toString());
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } catch (NoClassDefFoundError e2) {
      failed(e2, "Unexpected Exception");
    }

  }

  public void testBadProfile(String goodUser, String goodPasswd, String badUser, String badPasswd,
      String expectedBadConnectException, String expectedBadConnectException2, String expectedBadConnectException3,
      String added) {
    String badConnectException = "NO EXCEPTION";
    StringBuffer sb = new StringBuffer(); 
    try {

      // Grab a bunch of connection to improve the likelihood of the
      // next line failing

      Connection[] c2 = new Connection[20];
      String[] jobnames = new String[20];
      try {
        for (int i = 0; i < c2.length; i++) {
          c2[i] = DriverManager.getConnection(baseURL_ + ";errors=full", badUser, badPasswd);
          if (c2[i] instanceof com.ibm.as400.access.AS400JDBCConnection) {
            com.ibm.as400.access.AS400JDBCConnection c = (com.ibm.as400.access.AS400JDBCConnection) c2[i];
            String jobname = c.getServerJobIdentifier();
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis()) ;
            sb.append(currentTimestamp+": Job[" + i + "]=" + jobname + "\n");
            jobnames[i] = jobname;
          } else if (JDReflectionUtil.instanceOf(c2[i], "com.ibm.db2.jdbc.app.DB2Driver")) {
            Object c = c2[i];
            String jobname = JDReflectionUtil.callMethod_S(c, "getServerJobName");
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis()) ;
            sb.append(currentTimestamp+": Job[" + i + "]=" + jobname + "\n");
            jobnames[i] = jobname;
          }

          Statement s = c2[i].createStatement();
          ResultSet rs = s.executeQuery("select * from sysibm.sysdummy1");
          while (rs.next()) {

          }
          rs.close();
          s.close();

        }

        System.out
            .println("Error did not get exception after connecting with bad proflie .  Sleeping for 120 seconds.");
        System.out.println("Please check the following jobs " + sb.toString());
        try {
          Thread.sleep(120000);
        } catch (Exception e) {
          e.printStackTrace();
        }
        for (int i = 0; i < c2.length; i++) {
          if (c2[i] != null) {
            c2[i].close();
            c2[i] = null;
          }
        }

        badConnectException = "NO EXCEPTION after " + c2.length + " attempts with " + badUser;
      } catch (Exception e) {
        badConnectException = e.toString();
      }

      // Loop and connect. Loop several times to cleanup any bad connections out
      // there.
      // This uses a different user because in the error case, this could
      // disable a user profile.
      Exception thrownException = null;
      int exceptionCount = 0;
      int attemptCount = 0;
      Connection[][] c3 = new Connection[2][c2.length];
      boolean keepLooping = true;
      for (int j = 0; j < 2 && keepLooping; j++) {
        for (int i = 0; i < c3[j].length && keepLooping; i++) {
          try {
            attemptCount++;
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis()) ;
            sb.append(currentTimestamp+" : [" + i + "] Connecting with " + goodUser +"\n");
            c3[j][i] = DriverManager.getConnection(baseURL_ + ";errors=full", goodUser, goodPasswd);
            c3[j][i].createStatement();
          } catch (SQLException ex) {
            exceptionCount++;
            thrownException = ex;
            String tracing = System.getProperty("com.ibm.as400.access.Trace.category");
            if (tracing != null) {
              keepLooping = false;
            }
          }
        }
      }
      if (thrownException != null) {
        System.out.println("Attempt count is " + attemptCount);
        System.out.println("Exception count is " + exceptionCount);
        throw thrownException;
      }

      Statement s2 = c3[0][0].createStatement();
      /*
       * s2.executeUpdate("CALL QSYS.QCMDEXC('"+
       * "QSYS/DLTUSRPRF "+badUser+"                               ',"+
       * "0000000040.00000 ) ");
       */

      s2.close();

      for (int i = 0; i < c3.length; i++) {
        for (int j = 0; j < c3[i].length; j++) {
          c3[i][j].close();
        }
      }

      assertCondition(badConnectException.indexOf(expectedBadConnectException) >= 0

          || badConnectException.indexOf(expectedBadConnectException2) >= 0
          || badConnectException.indexOf(expectedBadConnectException3) >= 0,
          "Expected '" + expectedBadConnectException + "' or '" + expectedBadConnectException2.replace(' ', '_')
              + "' or '" + expectedBadConnectException3.replace(' ', '_') + "' got '" + badConnectException + "'"
               + sb.toString() + added);

    } catch (Exception e) {
      failed(e, "Unexpected Exception" + added + "  badConnectionException=" + badConnectException+" "+sb.toString());
    }

  }

  public void Var054() {
    notApplicable();
  }

  public void executeStatements(Statement s1, String[] commands) throws SQLException {
    for (int i = 0; i < commands.length; i++) {
      String command = commands[i];
      s1.executeUpdate(command);
    }
  }

  public void executeStatementsIgnoreErrors(Statement s1, String[] commands, String[] expectedErrors) {
    for (int i = 0; i < commands.length; i++) {
      String command = commands[i];
      try {
        s1.executeUpdate(command);
      } catch (Exception e) {
        boolean found = false;
        String errorMessage = e.toString();
        for (int j = 0; j < expectedErrors.length; j++) {
          if (errorMessage.indexOf(expectedErrors[j]) >= 0) {
            found = true;
          }
        }
        if (!found) {
          System.out.println("Unexpected exception executing " + command);
          e.printStackTrace();
        }
      }
    }
  }

  public void Var055() {
    notApplicable();
  }

  public void Var056() {
    assertCondition(true);
  }

  /*
   * test the driver=native property for toolbox
   */

  public void Var057() {
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && JTOpenTestEnvironment.isOS400 && (!JTOpenTestEnvironment.isOS400open)) {
      try {
        Connection c = DriverManager.getConnection("jdbc:as400:localhost;driver=native");

        String className = c.getClass().getName();
        String expected = "DB2Connection";
        assertCondition(className.indexOf(expected) >= 0,
            "For driver=native expected " + expected + " but got " + className);

      } catch (Exception e) {
        failed(e, "unexpected exception");
      }
    } else {
      notApplicable("Toolbox testcase on IBM i");
    }

  }

  /*
   * test the leaking of server jobs in DEQW state on take descript
   */

  public void Var058() {
    if (checkToolbox()) {
      if (toolboxNative) {
        StringBuffer sb = new StringBuffer();
        try {
          Random random = new Random();
          boolean passed = true;
          sb.append("Getting base connection\n");
          System.out.println("..Running leak test with user id " + userId_);
          Connection connection = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

          /* Run for 6 seconds in 10 sec batches */
          for (int i = 0; i < 6 && passed; i++) {

            Connection[] connections = new Connection[20];
            long endtime = System.currentTimeMillis() + 10000;
            while (System.currentTimeMillis() < endtime) {
              int index = random.nextInt(connections.length);
              if (connections[index] != null) {
                // Let the garbage collector close
                // sb.append("Closing connection at index "+index+"\n");
                // connections[index].close();
              }
              sb.append("Opening  connection at index " + index + "\n");
              connections[index] = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
            }
            for (int index = 0; index < connections.length; index++) {
              if (connections[index] != null) {
                sb.append("Closing connection at index " + index + "\n");
                connections[index].close();
              }
            }

            Statement s = connection.createStatement();

            ResultSet rs = s.executeQuery(
                "select JOB_NAME  from table( qsys2.active_job_info  ()) A, TABLE(qsys2.STACK_INFO(JOB_NAME)) B where JOB_STATUS='DEQW' and JOB_NAME like '%QUSER%' and PROCEDURE_NAME='takedescriptor'");
            while (rs.next()) {
              passed = false;
              sb.append("ERROR:  Found job with takedescriptor in stack " + rs.getString(1) + "\n");
            }
            s.close();
          }
          connection.close();
          assertCondition(passed, sb);

        } catch (Exception e) {
          failed(e, "unexpected exception");
        }
      } else {
        notApplicable("Toolbox native test");
      }
    } /* checkToolbox */
  } /* Var058 */

  /*
   * Var059 Check that a connection can be established using an additional
   * authentication factor
   */
  public void Var059() {
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX || getDriver() == JDTestDriver.DRIVER_NATIVE) {
      if (checkPasswordLeak()) {
      String systemName = systemObject_.getSystemName();
      if (checkAdditionalAuthenticationFactor(systemName)) {
        try {
          initMfaUser();
          String mfaFactorString = new String(mfaFactor_);
          String url;
          if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            url = "jdbc:as400:" + systemName + ";additionalAuthenticationFactor=" + mfaFactorString;
          } else {
            url = "jdbc:db2:localhost;additionalAuthenticationFactor=" + mfaFactorString;
          }
          String mfaPassword = new String(PasswordVault.decryptPassword(mfaEncryptedPassword_));
          Connection c = DriverManager.getConnection(url, mfaUserid_, mfaPassword);
          
          Statement s = c.createStatement();
          ResultSet rs = s.executeQuery("VALUES CURRENT USER");
          rs.next();
          String currentUser = rs.getString(1);
          System.out.println("current MFA user is " + currentUser);
          assertCondition(c != null && mfaUserid_.equalsIgnoreCase(currentUser),
              "currentUser=" + currentUser + " MFAUserID=" + mfaUserid_);
        } catch (Exception e) {
          failed(e, "unexpected exception");
        }
      }
      }
    } else {
      notApplicable("TOOLBOX or NATIVE variation");
    }
  }

  /*
   * Var060 Check that a connection is not established for MFA user without
   * additional authentication factor
   */
  public void Var060() {
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX || getDriver() == JDTestDriver.DRIVER_NATIVE) {
      if (checkPasswordLeak()) {
      String systemName = systemObject_.getSystemName();
      if (checkAdditionalAuthenticationFactor(systemName)) {
        try {
          initMfaUser();
          String url;
          if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            url = "jdbc:as400:" + systemName + ";prompt=false";
          } else {
            url = "jdbc:db2:localhost";
          }
          try {
            String mfaPassword = new String(PasswordVault.decryptPassword(mfaEncryptedPassword_));
            
            Connection c = DriverManager.getConnection(url, mfaUserid_, mfaPassword);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("VALUES CURRENT USER");
            rs.next();
            String currentUser = rs.getString(1);
            System.out.println("current MFA user is " + currentUser);
            assertCondition(false, "Able to connect as MFS user without mfaFactor currentUser=" + currentUser
                + " MFAUserID=" + mfaUserid_);
          } catch (SQLException e) {
            String[] expected = { "Password is incorrect", "Authorization failure" };
            assertExceptionContains(e, expected, "Connecting via MFA user with additional factor");
          }
        } catch (Exception e) {
          failed(e, "unexpected exception");
        }
      }
      }
    } else {
      notApplicable("TOOLBOX or NATIVE variation");
    }
  }

  /*
   * Check new Driver based connection methods. Connect with USERID and password
   */
  public void Var061() {
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX || getDriver() == JDTestDriver.DRIVER_NATIVE) {
      Driver driver;
      try {
        String url;
        String expectedJobName;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          driver = new AS400JDBCDriver();
          String systemName = systemObject_.getSystemName();
          url = "jdbc:as400:" + systemName + ";prompt=false";
          expectedJobName = "QZDASOINIT";
        } else {
          driver = (Driver) JDReflectionUtil.callStaticMethod_O("com.ibm.db2.jdbc.app.DB2Driver", "getDriver");
          url = "jdbc:db2:localhost";
          expectedJobName = "QSQSRVR";
        }
        Properties properties = new Properties();
        properties.put("user", userId_);
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        Class<?>[] argTypes = new Class[3];
        argTypes[0] = url.getClass();
        argTypes[1] = properties.getClass();
        argTypes[2] = charPassword.getClass();
        ;
        Connection c = (Connection) JDReflectionUtil.callMethod_O(driver, "connect", argTypes, url, properties,
            charPassword);
        Arrays.fill(charPassword, ' ');

        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("VALUES JOB_NAME");
        rs.next();
        String jobName = rs.getString(1);
        rs.close();
        s.close();
        c.close();

        assertCondition((jobName.indexOf(expectedJobName) >= 0),
            "jobName " + jobName + " does not contain " + expectedJobName);

      } catch (Exception e) {
        failed(e, "unexpected exception");
      }

    } else {
      notApplicable("Native and toolbox Driver connection tests");
    }
  }

  /*
   * Check new Driver based connection methods Connect with MFA parameter
   */
  public void Var062() {
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX || getDriver() == JDTestDriver.DRIVER_NATIVE) {
      String systemName = systemObject_.getSystemName();
      if (checkAdditionalAuthenticationFactor(systemName)) {

        Driver driver;
        try {
          String url;
          String expectedJobName;
          initMfaUser();

          if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            driver = new AS400JDBCDriver();
            url = "jdbc:as400:" + systemName + ";prompt=false";
            expectedJobName = "QZDASOINIT";
          } else {
            driver = (Driver) JDReflectionUtil.callStaticMethod_O("com.ibm.db2.jdbc.app.DB2Driver", "getDriver");
            url = "jdbc:db2:localhost";

            expectedJobName = "QSQSRVR";
          }
          Properties properties = new Properties();
          properties.put("user", mfaUserid_);
          char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
          char[] mfaPassword =    PasswordVault.decryptPassword(mfaEncryptedPassword_);

          Class<?>[] argTypes = new Class[4];
          argTypes[0] = url.getClass();
          argTypes[1] = properties.getClass();
          argTypes[2] = mfaPassword.getClass();
          ;
          argTypes[3] = mfaFactor_.getClass();
          Connection c = (Connection) JDReflectionUtil.callMethod_O(driver, "connect", argTypes, url, properties,
              charPassword, mfaFactor_);
          Arrays.fill(charPassword, ' ');
          Arrays.fill(mfaPassword, ' ');

          Statement s = c.createStatement();
          ResultSet rs = s.executeQuery("select JOB_NAME, CURRENT USER from sysibm.sysdummy1");
          rs.next();
          String jobName = rs.getString(1);
          String currentUser = rs.getString(2);
          rs.close();
          s.close();
          c.close();

          assertCondition((jobName.indexOf(expectedJobName) >= 0) && currentUser.equalsIgnoreCase(mfaUserid_),
              "jobName " + jobName + " does not contain " + expectedJobName + " or user=" + currentUser + " is not "
                  + mfaUserid_);

        } catch (Exception e) {
          failed(e, "unexpected exception connecting using Driver.connect method MFA password");
        }

      }

    } else {
      notApplicable("Native and toolbox Driver connection tests");
    }
  }

  /*
   * Check new Driver based connection methods Connect with MFA in URL
   */
  public void Var063() {
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX || getDriver() == JDTestDriver.DRIVER_NATIVE) {
      String systemName = systemObject_.getSystemName();
      if (checkAdditionalAuthenticationFactor(systemName)) {

        Driver driver;
        try {
          String url;
          String expectedJobName;
          initMfaUser();

          if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            driver = new AS400JDBCDriver();
            url = "jdbc:as400:" + systemName + ";prompt=false;additionalAuthenticationFactor=" + new String(mfaFactor_);
            expectedJobName = "QZDASOINIT";
          } else {
            driver = (Driver) JDReflectionUtil.callStaticMethod_O("com.ibm.db2.jdbc.app.DB2Driver", "getDriver");
            url = "jdbc:db2:localhost;additionalAuthenticationFactor=" + new String(mfaFactor_);
            expectedJobName = "QSQSRVR";
          }
          Properties properties = new Properties();
          properties.put("user", mfaUserid_);
          char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
          char[] mfaPassword =    PasswordVault.decryptPassword(mfaEncryptedPassword_);

          Class<?>[] argTypes = new Class[3];
          argTypes[0] = url.getClass();
          argTypes[1] = properties.getClass();
          argTypes[2] = mfaPassword.getClass();
          Connection c = (Connection) JDReflectionUtil.callMethod_O(driver, "connect", argTypes, url, properties,
              charPassword);
          Arrays.fill(charPassword, ' ');
          Arrays.fill(mfaPassword, ' ');

          Statement s = c.createStatement();
          ResultSet rs = s.executeQuery("select JOB_NAME, CURRENT USER from sysibm.sysdummy1");
          rs.next();
          String jobName = rs.getString(1);
          String currentUser = rs.getString(2);
          rs.close();
          s.close();
          c.close();

          assertCondition((jobName.indexOf(expectedJobName) >= 0) && currentUser.equalsIgnoreCase(mfaUserid_),
              "jobName " + jobName + " does not contain " + expectedJobName + " or user=" + currentUser + " is not "
                  + mfaUserid_);

        } catch (Exception e) {
          failed(e, "unexpected exception connecting using Driver.connect method MFA password");
        }

      }

    } else {
      notApplicable("Native and toolbox Driver connection tests");
    }
  }

  public void Var064() {
    notApplicable();
  }

  public void Var065() {
    notApplicable();
  }

  public void Var066() {
    notApplicable();
  }

  public void Var067() {
    notApplicable();
  }

  public void Var068() {
    notApplicable();
  }

  public void Var069() {
    notApplicable();
  }

  public void Var070() {
    notApplicable();
  }

  /* Tests for connecting to system with exit program */
  /* Return false if the system does not support the exit program. */
  /*
   * If the correct exit program exists then return true. Make sure the MFA user
   * uses the exit program
   */
  /*
   * If an exit program does not exist, create the exit program and make sure the
   * MFA users users the exit program
   */
  /* If a different exit program exists, then throw an SQLException */

  public boolean checkExitProgram() throws Exception {
    if (!exitProgramChecked_) {
      String systemName = systemObject_.getSystemName();
      if (checkAdditionalAuthenticationFactor(systemName)) {
        initMfaUser();
        AuthExit.assureExitProgramExists(pwrConnection_, mfaUserid_);
        exitProgramChecked_ = true; 
        exitProgramEnabled_ = true;
        skipExitCleanup = false; 
      } else {
        exitProgramChecked_ = true;
        exitProgramEnabled_ = false;
        return false; /* notApplicable already issued */
      }
    } /* exitProgramChecked_ */
    if (!exitProgramEnabled_) {
      notApplicable("Exit program not possible on this system");
    }
    return exitProgramEnabled_;
  }

  /*
   * Test that the exit program information is correct for native and for toolbox
   * non-secure
   */
  public void Var071() {
    try {

      StringBuffer sb = new StringBuffer();
      boolean successful = true;
      if (checkExitProgram()) {
        if (checkPasswordLeak()) {
        String jobName;
        // Create a simple MFA connection and check the exit information.
        initMfaUser();
        String mfaFactorString = new String(mfaFactor_);
        String url;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          url = "jdbc:as400:" + systemObject_.getSystemName() + ";secure=false;additionalAuthenticationFactor="
              + mfaFactorString;
        } else {
          url = "jdbc:db2:localhost;additionalAuthenticationFactor=" + mfaFactorString;
        }
        String mfaPassword = new String(PasswordVault.decryptPassword(mfaEncryptedPassword_));

        Connection c = DriverManager.getConnection(url, mfaUserid_, mfaPassword);
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT CURRENT USER, JOB_NAME FROM SYSIBM.SYSDUMMY1");
        rs.next();
        String currentUser = rs.getString(1);
        System.out.println("current MFA user is " + currentUser);
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          jobName = rs.getString(2).replace('/', '.');
        } else {
          jobName = JDJobName.getJobName().replace('/', '.');
        }
        System.out.println("Job with exit information is " + jobName);
        rs.close();
        if (!mfaUserid_.equalsIgnoreCase(currentUser)) {
          successful = false;
          sb.append("currentUser=" + currentUser + " MFAUserID=" + mfaUserid_ + "\n");
        }
        /* Read the output file using IFS_READ */
        boolean foundProfileName = false;
        boolean foundVerificationId = false;
        boolean foundRemotePort = false;
        boolean foundLocalPort = false;
        boolean foundRemoteIp = false;
        boolean foundLocalIp = false;
        String expectedVerificationId;
        String expectedRemotePort = "NOTSET";
        String expectedLocalPort = "NOTSET";
        String expectedRemoteIp = "NOTSET";
        String expectedLocalIp = "NOTSET";

        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {

          expectedVerificationId = "Verification_ID=QIBM_OS400_QZBS_SVR_DATABASE";

          if (isNative_) {
            expectedRemotePort = "Remote_Port=0";
            expectedLocalPort = "Local_Port=0";
            expectedRemoteIp = "Remote_IPAddress=";
            expectedLocalIp = "Local_IPAddress=";
          } else {
            String sql = "select CLIENT_IP_ADDRESS, CLIENT_PORT_NUMBER, SERVER_IP_ADDRESS, SERVER_PORT_NUMBER from QSYS2.TCPIP_INFO";
            rs = s.executeQuery(sql);
            if (rs.next()) {
              expectedRemoteIp = "Remote_IPAddress=" + rs.getString(1);
              expectedRemotePort = "Remote_Port=" + rs.getString(2);
              expectedLocalIp = "Local_IPAddress=" + rs.getString(3);
              expectedLocalPort = "Local_Port=" + rs.getString(4);
            }
            rs.close();
          }
        } else {
          expectedVerificationId = "Verification_ID=QIBM_QJDB_JDBC";
          expectedRemotePort = "Remote_Port=0";
          expectedLocalPort = "Local_Port=0";
          expectedRemoteIp = "Remote_IPAddress=";
          expectedLocalIp = "Local_IPAddress=";
        }
        c.close();

        Statement pwrStmt = pwrConnection_.createStatement();
        String sql = "select LINE from TABLE(QSYS2.IFS_READ_UTF8('/tmp/authexit/" + jobName + ".txt'))";
        rs = pwrStmt.executeQuery(sql);
        sb.append("/tmp/authexit/" + jobName + ".txt contains \n");
        sb.append("-------------------------------------------\n");

        while (rs.next()) {
          String line = rs.getString(1).trim();
          sb.append(line);
          sb.append("\n");
          if (line.equals("User_Profile_Name=" + mfaUserid_)) {
            foundProfileName = true;
          }
          if (line.equals(expectedVerificationId))
            foundVerificationId = true;
          if (line.equals(expectedLocalIp))
            foundLocalIp = true;
          if (line.equals(expectedLocalPort))
            foundLocalPort = true;
          if (line.equals(expectedRemoteIp))
            foundRemoteIp = true;
          if (line.equals(expectedRemotePort))
            foundRemotePort = true;
        }
        sb.append("-------------------------------------------\n");

        rs.close();
        pwrStmt.close();
        if (!foundProfileName) {
          successful = false;
          sb.append("Did not find USER PROFILE in /tmp/authexit/" + jobName + ".txt\n");
        }
        if (!foundVerificationId) {
          successful = false;
          sb.append("Did not find verification id:" + expectedVerificationId + "\n");
        }
        if (!foundLocalIp) {
          successful = false;
          sb.append("Did not find expected:" + expectedLocalIp + "\n");
        }
        if (!foundLocalPort) {
          successful = false;
          sb.append("Did not find expected:" + expectedLocalPort + "\n");
        }
        if (!foundRemoteIp) {
          successful = false;
          sb.append("Did not find expected:" + expectedRemoteIp + "\n");
        }
        if (!foundRemotePort) {
          successful = false;
          sb.append("Did not find expected:" + expectedRemotePort + "\n");
        }
        if (!successful)
          skipExitCleanup = true;
        assertCondition(successful, sb);
        }
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /* Test that the exit program information is correct for toolbox non-secure */
  public void Var072() {
    try {
      StringBuffer sb = new StringBuffer();
      boolean successful = true;
      if (checkToolbox() && checkExitProgram() && checkPasswordLeak()) {
        // Create a simple MFA connection and check the exit information.
        initMfaUser();
        AuthExit.cleanupExitProgramFiles(pwrConnection_);
        String mfaFactorString = new String(mfaFactor_);
        String url;
        url = "jdbc:as400:" + systemObject_.getSystemName() + ";secure=true;additionalAuthenticationFactor="
            + mfaFactorString;
        String mfaPassword = new String(PasswordVault.decryptPassword(mfaEncryptedPassword_));

        Connection c = DriverManager.getConnection(url, mfaUserid_, mfaPassword);
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT CURRENT USER, JOB_NAME FROM SYSIBM.SYSDUMMY1");
        rs.next();
        String currentUser = rs.getString(1);
        System.out.println("current MFA user is " + currentUser);
        if (!mfaUserid_.equalsIgnoreCase(currentUser)) {
          successful = false;
          sb.append("currentUser=" + currentUser + " MFAUserID=" + mfaUserid_ + "\n");
        }
        /* Read the output file using IFS_READ */
        boolean foundProfileName = false;
        boolean foundVerificationId = false;
        boolean foundLocalPort = false;
        boolean foundRemoteIp = false;
        boolean foundLocalIp = false;
        String expectedVerificationId;
        String expectedLocalPort = "NOTSET";
        String expectedRemoteIp = "NOTSET";
        String expectedLocalIp = "NOTSET";

        expectedVerificationId = "Verification_ID=QIBM_QZBS_SVR_HOSTCNN";

        String sql = "select CLIENT_IP_ADDRESS, CLIENT_PORT_NUMBER, SERVER_IP_ADDRESS, SERVER_PORT_NUMBER from QSYS2.TCPIP_INFO";
        rs = s.executeQuery(sql);
        if (rs.next()) {
          expectedRemoteIp = "Remote_IPAddress=" + rs.getString(1);
          expectedLocalIp = "Local_IPAddress=" + rs.getString(3);
          expectedLocalPort = "Local_Port=9480";
        }
        rs.close();

        c.close();

        Statement pwrStmt = pwrConnection_.createStatement();
        String pathname = null;
        sql = "select LINE,PATH_NAME from " + "table(qsys2.IFS_OBJECT_STATISTICS('/tmp/authexit')), "
            + "TABLE(QSYS2.IFS_READ_UTF8(PATH_NAME)) WHERE OBJECT_TYPE='*STMF'";
        rs = pwrStmt.executeQuery(sql);
        while (rs.next()) {
          String line = rs.getString(1).trim();
          pathname = rs.getString(2).trim();
          sb.append(line);
          sb.append("\n");
          if (line.equals("User_Profile_Name=" + mfaUserid_)) {
            foundProfileName = true;
          }
          if (line.equals(expectedVerificationId))
            foundVerificationId = true;
          if (line.equals(expectedLocalIp))
            foundLocalIp = true;
          if (line.equals(expectedLocalPort))
            foundLocalPort = true;
          if (line.equals(expectedRemoteIp))
            foundRemoteIp = true;
        }
        rs.close();
        pwrStmt.close();
        if (!foundProfileName) {
          successful = false;
          sb.append("Did not find USER PROFILE in " + pathname + "\n");
        }
        if (!foundVerificationId) {
          successful = false;
          sb.append("Did not find verification id:" + expectedVerificationId + "\n");
        }
        if (!foundLocalIp) {
          successful = false;
          sb.append("Did not find expected:" + expectedLocalIp + "\n");
        }
        if (!foundLocalPort) {
          successful = false;
          sb.append("Did not find expected:" + expectedLocalPort + "\n");
        }
        if (!foundRemoteIp) {
          successful = false;
          sb.append("Did not find expected:" + expectedRemoteIp + "\n");
        }
        if (!successful)
          skipExitCleanup = true;
        assertCondition(successful, sb);
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  /* Test user supplied settings are passed for native driver */
  public void Var073() {
    try {

      StringBuffer sb = new StringBuffer();
      boolean successful = true;
      if (checkNative() && checkExitProgram() && checkPasswordLeak()) {
        String jobName;
        // Create a simple MFA connection and check the exit information.
        initMfaUser();
        String mfaFactorString = new String(mfaFactor_);
        String url;
        url = "jdbc:db2:localhost;additionalAuthenticationFactor=" + mfaFactorString
            + ";authenticationVerificationId=MYAPP_SUPER_SERVER" + ";authenticationLocalIP=1.2.3.4"
            + ";authenticationLocalPort=80" + ";authenticationRemoteIP=5.6.7.8" + ";authenticationRemotePort=2134";
        sb.append("Connecting using URL " + url + "\n");
        String mfaPassword = new String(PasswordVault.decryptPassword(mfaEncryptedPassword_));

        Connection c = DriverManager.getConnection(url, mfaUserid_, mfaPassword);
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT CURRENT USER, JOB_NAME FROM SYSIBM.SYSDUMMY1");
        rs.next();
        String currentUser = rs.getString(1);
        System.out.println("current MFA user is " + currentUser);
        jobName = JDJobName.getJobName().replace('/', '.');
        System.out.println("Job with exit information is " + jobName);
        rs.close();
        if (!mfaUserid_.equalsIgnoreCase(currentUser)) {
          successful = false;
          sb.append("currentUser=" + currentUser + " MFAUserID=" + mfaUserid_ + "\n");
        }
        /* Read the output file using IFS_READ */
        boolean foundProfileName = false;
        boolean foundVerificationId = false;
        boolean foundRemotePort = false;
        boolean foundLocalPort = false;
        boolean foundRemoteIp = false;
        boolean foundLocalIp = false;

        String expectedVerificationId = "Verification_ID=MYAPP_SUPER_SERVER";
        String expectedRemotePort = "Remote_Port=2134";
        String expectedLocalPort = "Local_Port=80";
        String expectedRemoteIp = "Remote_IPAddress=5.6.7.8";
        String expectedLocalIp = "Local_IPAddress=1.2.3.4";
        c.close();

        Statement pwrStmt = pwrConnection_.createStatement();
        String sql = "select LINE from TABLE(QSYS2.IFS_READ_UTF8('/tmp/authexit/" + jobName + ".txt'))";
        rs = pwrStmt.executeQuery(sql);
        sb.append("/tmp/authexit/" + jobName + ".txt contains \n");
        sb.append("-------------------------------------------\n");

        while (rs.next()) {
          String line = rs.getString(1).trim();
          sb.append(line);
          sb.append("\n");
          if (line.equals("User_Profile_Name=" + mfaUserid_)) {
            foundProfileName = true;
          }
          if (line.equals(expectedVerificationId))
            foundVerificationId = true;
          if (line.equals(expectedLocalIp))
            foundLocalIp = true;
          if (line.equals(expectedLocalPort))
            foundLocalPort = true;
          if (line.equals(expectedRemoteIp))
            foundRemoteIp = true;
          if (line.equals(expectedRemotePort))
            foundRemotePort = true;
        }
        sb.append("-------------------------------------------\n");

        rs.close();
        pwrStmt.close();
        if (!foundProfileName) {
          successful = false;
          sb.append("Did not find USER PROFILE in /tmp/authexit/" + jobName + ".txt\n");
        }
        if (!foundVerificationId) {
          successful = false;
          sb.append("Did not find verification id:" + expectedVerificationId + "\n");
        }
        if (!foundLocalIp) {
          successful = false;
          sb.append("Did not find expected:" + expectedLocalIp + "\n");
        }
        if (!foundLocalPort) {
          successful = false;
          sb.append("Did not find expected:" + expectedLocalPort + "\n");
        }
        if (!foundRemoteIp) {
          successful = false;
          sb.append("Did not find expected:" + expectedRemoteIp + "\n");
        }
        if (!foundRemotePort) {
          successful = false;
          sb.append("Did not find expected:" + expectedRemotePort + "\n");
        }
        if (!successful)
          skipExitCleanup = true;
        assertCondition(successful, sb);
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  
  
  /* Test that the exit program information is correct for toolbox secure non-MFA users */
  public void Var074() {
    try {
      StringBuffer sb = new StringBuffer();
      boolean successful = true;
      if (checkToolbox() && checkExitProgram() && checkPasswordLeak()) {
        // Create a simple MFA connection and check the exit information.
        AuthExit.cleanupExitProgramFiles(pwrConnection_);
        AuthExit.enableUser(pwrConnection_, userId_ );
        String mfaFactorString = "1234567891123456789212345678931234567894123456789512345678961234";
        String url;
        url = "jdbc:as400:" + systemObject_.getSystemName() + ";secure=true;additionalAuthenticationFactor="
            + mfaFactorString;;
        String password = PasswordVault.decryptPasswordLeak(encryptedPassword_);
        Connection c = DriverManager.getConnection(url, userId_, password);
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT CURRENT USER, JOB_NAME FROM SYSIBM.SYSDUMMY1");
        rs.next();
        String currentUser = rs.getString(1);
        if (!userId_.equalsIgnoreCase(currentUser)) {
          successful = false;
          sb.append("currentUser=" + currentUser + " userID=" + userId_ + "\n");
        }
        /* Read the output file using IFS_READ */
        boolean foundProfileName = false;
        boolean foundVerificationId = false;
        boolean foundLocalPort = false;
        boolean foundRemoteIp = false;
        boolean foundLocalIp = false;
        boolean foundAAlen = false;
        boolean foundAFccsid = false; 
        boolean foundAFbytes = false; 
        String expectedVerificationId;
        String expectedLocalPort = "NOTSET";
        String expectedRemoteIp = "NOTSET";
        String expectedLocalIp = "NOTSET";
        
        String expectedAFlen = "Additional_Factor_Length=64";
        String expectedAFccsid = "Additional_Factor_CCSID=1208";
        String expectedAFbytes = "Additional_FactorBytes=31323334353637383931313233343536373839323132333435363738393331323334353637383934313233343536373839353132333435363738393631323334"; 


        expectedVerificationId = "Verification_ID=QIBM_QZBS_SVR_HOSTCNN";

        String sql = "select CLIENT_IP_ADDRESS, CLIENT_PORT_NUMBER, SERVER_IP_ADDRESS, SERVER_PORT_NUMBER from QSYS2.TCPIP_INFO";
        rs = s.executeQuery(sql);
        if (rs.next()) {
          expectedRemoteIp = "Remote_IPAddress=" + rs.getString(1);
          expectedLocalIp = "Local_IPAddress=" + rs.getString(3);
          expectedLocalPort = "Local_Port=9480";
        }
        rs.close();

        c.close();

        Statement pwrStmt = pwrConnection_.createStatement();
        String pathname = null;
        sql = "select LINE,PATH_NAME from " + "table(qsys2.IFS_OBJECT_STATISTICS('/tmp/authexit')), "
            + "TABLE(QSYS2.IFS_READ_UTF8(PATH_NAME)) WHERE OBJECT_TYPE='*STMF'";
        rs = pwrStmt.executeQuery(sql);
        while (rs.next()) {
          String line = rs.getString(1).trim();
          pathname = rs.getString(2).trim();
          sb.append(line);
          sb.append("\n");
          if (line.equals("User_Profile_Name=" + userId_.toUpperCase())) {
            foundProfileName = true;
          }
          if (line.equals(expectedVerificationId))
            foundVerificationId = true;
          if (line.equals(expectedLocalIp))
            foundLocalIp = true;
          if (line.equals(expectedLocalPort))
            foundLocalPort = true;
          if (line.equals(expectedRemoteIp))
            foundRemoteIp = true;
          if (line.equals(expectedAFlen ))
              foundAAlen = true; 
          if (line.equals(expectedAFccsid ))
            foundAFccsid = true; 
          if (line.equals(expectedAFbytes ))
            foundAFbytes = true; 
        }
        rs.close();
        pwrStmt.close();
        if (!foundProfileName) {
          successful = false;
          sb.append("Did not find USER PROFILE ("+userId_.toUpperCase()+") in " + pathname + "\n");
        }
        if (!foundVerificationId) {
          successful = false;
          sb.append("Did not find verification id:" + expectedVerificationId + "\n");
        }
        if (!foundLocalIp) {
          successful = false;
          sb.append("Did not find expected:" + expectedLocalIp + "\n");
        }
        if (!foundLocalPort) {
          successful = false;
          sb.append("Did not find expected:" + expectedLocalPort + "\n");
        }
        if (!foundRemoteIp) {
          successful = false;
          sb.append("Did not find expected:" + expectedRemoteIp + "\n");
        }
        if (!foundAAlen) { 
          successful=false; 
          sb.append("Did not find expected:" + expectedAFlen+"\n"); 
        }
        if (!foundAFccsid) { 
          successful=false; 
          sb.append("Did not find expected:" + expectedAFccsid+"\n"); 
        }
        if (!foundAFbytes) { 
          successful=false; 
          sb.append("Did not find expected:" + expectedAFbytes+"\n"); 
        }

        
        AuthExit.disableUser(pwrConnection_, userId_ );

        if (!successful)
          skipExitCleanup = true;
        assertCondition(successful, sb);
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  
  
  /* Test that the exit program information is correct for toolbox non-secure non-MFA users */
  public void Var075() {
    try {
      StringBuffer sb = new StringBuffer();
      boolean successful = true;
      if (checkToolbox() && checkExitProgram() && checkPasswordLeak()) {
        // Create a simple MFA connection and check the exit information.
        AuthExit.cleanupExitProgramFiles(pwrConnection_);
        AuthExit.enableUser(pwrConnection_, userId_ );
        String mfaFactorString = "1234567891123456789212345678931234567894123456789512345678961234";
        String url;
        url = "jdbc:as400:" + systemObject_.getSystemName() + ";additionalAuthenticationFactor="
            + mfaFactorString;;
        String password = PasswordVault.decryptPasswordLeak(encryptedPassword_);
        Connection c = DriverManager.getConnection(url, userId_, password);
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT CURRENT USER, JOB_NAME FROM SYSIBM.SYSDUMMY1");
        rs.next();
        String currentUser = rs.getString(1);
        if (!userId_.equalsIgnoreCase(currentUser)) {
          successful = false;
          sb.append("currentUser=" + currentUser + " userID=" + userId_ + "\n");
        }
        /* Read the output file using IFS_READ */
        boolean foundProfileName = false;
        boolean foundVerificationId = false;
        boolean foundLocalPort = false;
        boolean foundRemoteIp = false;
        boolean foundLocalIp = false;
        boolean foundAAlen = false;
        boolean foundAFccsid = false; 
        boolean foundAFbytes = false; 
        String expectedVerificationId;
        String expectedLocalPort = "NOTSET";
        String expectedRemoteIp = "NOTSET";
        String expectedLocalIp = "NOTSET";
        
        String expectedAFlen = "Additional_Factor_Length=64";
        String expectedAFccsid = "Additional_Factor_CCSID=1208";
        String expectedAFbytes = "Additional_FactorBytes=31323334353637383931313233343536373839323132333435363738393331323334353637383934313233343536373839353132333435363738393631323334"; 


        expectedVerificationId = "Verification_ID=QIBM_OS400_QZBS_SVR_DATABASE";
        String sql; 
        if (toolboxNative) {
          expectedRemoteIp = "Remote_IPAddress=";
          expectedLocalIp = "Local_IPAddress=";
          expectedLocalPort = "Local_Port=0";
     
        } else {
          sql = "select CLIENT_IP_ADDRESS, CLIENT_PORT_NUMBER, SERVER_IP_ADDRESS, SERVER_PORT_NUMBER from QSYS2.TCPIP_INFO";
          rs = s.executeQuery(sql);
          if (rs.next()) {
            expectedRemoteIp = "Remote_IPAddress=" + rs.getString(1);
            expectedLocalIp = "Local_IPAddress=" + rs.getString(3);
            expectedLocalPort = "Local_Port=8471";
          }
          rs.close();
        }

        c.close();

        Statement pwrStmt = pwrConnection_.createStatement();
        String pathname = null;
        sql = "select LINE,PATH_NAME from " + "table(qsys2.IFS_OBJECT_STATISTICS('/tmp/authexit')), "
            + "TABLE(QSYS2.IFS_READ_UTF8(PATH_NAME)) WHERE OBJECT_TYPE='*STMF'";
        rs = pwrStmt.executeQuery(sql);
        while (rs.next()) {
          String line = rs.getString(1).trim();
          pathname = rs.getString(2).trim();
          sb.append(line);
          sb.append("\n");
          if (line.equals("User_Profile_Name=" + userId_.toUpperCase())) {
            foundProfileName = true;
          }
          if (line.equals(expectedVerificationId))
            foundVerificationId = true;
          if (line.equals(expectedLocalIp))
            foundLocalIp = true;
          if (line.equals(expectedLocalPort))
            foundLocalPort = true;
          if (line.equals(expectedRemoteIp))
            foundRemoteIp = true;
          if (line.equals(expectedAFlen ))
              foundAAlen = true; 
          if (line.equals(expectedAFccsid ))
            foundAFccsid = true; 
          if (line.equals(expectedAFbytes ))
            foundAFbytes = true; 
        }
        rs.close();
        pwrStmt.close();
        if (!foundProfileName) {
          successful = false;
          sb.append("Did not find USER PROFILE ("+userId_.toUpperCase()+") in " + pathname + "\n");
        }
        if (!foundVerificationId) {
          successful = false;
          sb.append("Did not find verification id:" + expectedVerificationId + "\n");
        }
        if (!foundLocalIp) {
          successful = false;
          sb.append("Did not find expected:" + expectedLocalIp + "\n");
        }
        if (!foundLocalPort) {
          successful = false;
          sb.append("Did not find expected:" + expectedLocalPort + "\n");
        }
        if (!foundRemoteIp) {
          successful = false;
          sb.append("Did not find expected:" + expectedRemoteIp + "\n");
        }
        if (!foundAAlen) { 
          successful=false; 
          sb.append("Did not find expected:" + expectedAFlen+"\n"); 
        }
        if (!foundAFccsid) { 
          successful=false; 
          sb.append("Did not find expected:" + expectedAFccsid+"\n"); 
        }
        if (!foundAFbytes) { 
          successful=false; 
          sb.append("Did not find expected:" + expectedAFbytes+"\n"); 
        }

        
        AuthExit.disableUser(pwrConnection_, userId_ );

        if (!successful)
          skipExitCleanup = true;
        assertCondition(successful, sb);
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }


  /* Test behavior when AAF is too long and exit program is not registered */
  public void Var076() {
    StringBuffer sb = new StringBuffer();
    try {
      boolean successful = true;
      if (checkToolbox() && checkExitProgram() && checkPasswordLeak()) {
        // Create a simple MFA connection and check the exit information.
        AuthExit.cleanupExitProgramFiles(pwrConnection_);
        String mfaFactorString = "12345678911234567892123456789312345678941234567895123456789612345";
        String url;
        url = "jdbc:as400:" + systemObject_.getSystemName() + ";additionalAuthenticationFactor="
            + mfaFactorString;;
        String password = PasswordVault.decryptPasswordLeak(encryptedPassword_);
        Connection c = DriverManager.getConnection(url, userId_, password);
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT CURRENT USER, JOB_NAME FROM SYSIBM.SYSDUMMY1");
        rs.next();
        String currentUser = rs.getString(1);
        if (!userId_.equalsIgnoreCase(currentUser)) {
          successful = false;
          sb.append("currentUser=" + currentUser + " userID=" + userId_ + "\n");
        }
        successful = false; 
        sb.append("Did not throw error for big MFA "+mfaFactorString);
        assertCondition(successful, sb);
      }
    } catch (Exception e) {
      /* e.printStackTrace(System.out);  */ 
      assertExceptionContains(e, "additionalAuthFactor: Length is not valid", sb); 
    }
  }



  
  public void Var077() {    notApplicable();  }
  public void Var078() {    notApplicable();  }
  public void Var079() {    notApplicable();  }
  public void Var080() {    notApplicable();  }
  public void Var081() {    notApplicable();  }
  public void Var082() {    notApplicable();  }
  public void Var083() {    notApplicable();  }
  public void Var084() {    notApplicable();  }
  public void Var085() {    notApplicable();  }
  public void Var086() {    notApplicable();  }
  public void Var087() {    notApplicable();  }
  public void Var088() {    notApplicable();  }
  public void Var089() {    notApplicable();  }
  public void Var090() {    notApplicable();  }
  public void Var091() {    notApplicable();  }
  public void Var092() {    notApplicable();  }
  public void Var093() {    notApplicable();  }
  public void Var094() {    notApplicable();  }
  public void Var095() {    notApplicable();  }
  public void Var096() {    notApplicable();  }
  public void Var097() {    notApplicable();  }
  public void Var098() {    notApplicable();  }
  public void Var099() {    notApplicable();  }
  public void Var100() {    notApplicable();  }



  /*
   * Connect after connecting with a bad profile.
   * These tests are at the end of the variation as then tend to cause
   * problems with other variations. 
   */

  public void Var101() {
    if (checkPasswordLeak() && checkNotGroupTest()) {

      /* Each profile has the last letter of the library which is usually */
      /* the last letter of the test. JDRunit will not have two versions */
      /* of a testcase running with the same letter */

      String added = " -- added 11/2/2010 connect after connect with bad profile native  PTFs 61:SI41813,71:SI42110 ";
      String goodUser = "JDBGDUSR" + letter_;
      String goodPasswd = "xyz123zyz";
      String badUser = "JDBBDUSR" + letter_;
      String badPasswd = "xyz123zyz";
      String expectedBadConnectException = "Error occurred in SQL Call Level Interface";
      String expectedBadConnectException2 = "does not exist";
      String expectedBadConnectException3 = "Processing of the SQL statement ended";
      Connection c1 = null;
      Statement s1 = null;

      
      try {

        if (isToolboxDriver()) {
          expectedBadConnectException = "Communication link failure";
        }

        if (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
          expectedBadConnectException = "Processing of the SQL statement ended";
        }
        String powerPassword = PasswordVault.decryptPasswordLeak(pwrSysEncryptedPassword_);
        c1 = DriverManager.getConnection(baseURL_ + ";errors=full", pwrSysUserID_, powerPassword);

        // Delete and recreate the bad profile
        s1 = c1.createStatement();

        try {
          s1.executeUpdate("CALL QSYS2.QCMDEXC('" + "QSYS/DLTUSRPRF " + badUser + "  OWNOBJOPT(*CHGOWN JAVA) '  ) ");
        } catch (Exception e) {
          if (e.toString().indexOf("not found") >= 0) {
            // Just ignore */
          } else {
            System.out.println("Unexpected exception deleting old profile");
            e.printStackTrace();
          }
        }

        s1.executeUpdate("CALL QSYS.QCMDEXC('" + "QSYS/CRTUSRPRF USRPRF(" + badUser + ") PASSWORD(" + badPasswd
            + ")                               '," + "0000000070.00000 ) ");

        s1.executeUpdate("CALL QSYS.QCMDEXC('" + "QSYS/GRTOBJAUT OBJ(QGPL/QDFTJOBD) OBJTYPE(*JOBD) USER(" + badUser
            + ") AUT(*EXCLUDE)                                        '," + "0000000080.00000 ) ");

        try {

          s1.executeUpdate("CALL QSYS2.QCMDEXC('" + "QSYS/DLTUSRPRF " + goodUser + "     OWNOBJOPT(*CHGOWN JAVA) ') ");
        } catch (Exception e) {
          if (e.toString().indexOf("not found") >= 0) {
            // Just ignore */
          } else {
            System.out.println("Unexpected exception deleting old profile");
            e.printStackTrace();
          }
        }

        s1.executeUpdate("CALL QSYS.QCMDEXC('" + "QSYS/CRTUSRPRF USRPRF(" + goodUser + ") PASSWORD(" + goodPasswd
            + ")                               '," + "0000000070.00000 ) ");
      } catch (Exception e) {
        failed(e, "Error during testcase setup " + added);
        return;
      }

      testBadProfile(goodUser, goodPasswd, badUser, badPasswd, expectedBadConnectException,
          expectedBadConnectException2, expectedBadConnectException3, added);

      System.gc();
      try {
        // if (s1 != null)
        s1.close();

        // if (c1 != null)
        c1.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void Var102() {
    if (checkNotGroupTest() && checkPasswordLeak()) {
      String added = " -- added 01/03/2012 connect after connect with bad profile (because of bad library list entry) CPS 8P6TF6  PTFs 61:SI45605,71:SI45604 ";

      String goodUser = "JDBGDUS2" + letter_;
      String goodPasswd = "xyz123zyz";
      String badUser = "JDBBDUS2" + letter_;
      String badPasswd = "xyz123zyz";
      String expectedBadConnectException = "reason code is 0";
      String expectedBadConnectException2 = "does not exist";
      String expectedBadConnectException3 = "does not exist";
      Connection c1 = null;
      Statement s1 = null;

      try {

        if (isToolboxDriver()) {
          expectedBadConnectException = "Communication link failure";

        }
        String powerPassword = PasswordVault.decryptPasswordLeak(pwrSysEncryptedPassword_);
        c1 = DriverManager.getConnection(baseURL_ + ";errors=full", pwrSysUserID_, powerPassword);

        // Delete and recreate the bad profile
        s1 = c1.createStatement();

        String deleteCommands[] = { "CALL QSYS2.QCMDEXC('DLTUSRPRF " + badUser + " OWNOBJOPT(*CHGOWN JAVA)  ') ",
            "CALL QSYS.QCMDEXC('CHGJOB INQMSGRPY(*SYSRPYL)      ',0000000030.00000 ) ",
            "CALL QSYS.QCMDEXC('DLTLIB JDBADLIB         ',0000000020.00000 ) ",
            "CALL QSYS.QCMDEXC('DLTJOBD JOBD(QGPL/JDBADLIBL)  ', 0000000030.00000 )",
            "CALL QSYS2.QCMDEXC('DLTUSRPRF " + goodUser + "  OWNOBJOPT(*CHGOWN JAVA)  ') ", };
        String expectedErrors[] = { "not found" };
        executeStatementsIgnoreErrors(s1, deleteCommands, expectedErrors);

        String createCommands[] = { "CALL QSYS.QCMDEXC('CRTLIB JDBADLIB      ',0000000020.00000 ) ",
            "CALL QSYS.QCMDEXC('CRTJOBD JOBD(QGPL/JDBADLIBL) JOBQ(QINTER) INLLIBL(JDBADLIB)                                    ',"
                + "0000000080.00000 ) ",
            "CALL QSYS.QCMDEXC('CRTUSRPRF USRPRF(" + badUser + ") PASSWORD(" + badPasswd
                + ") JOBD(QGPL/JDBADLIBL)                                    '," + "0000000090.00000 ) ",
            "CALL QSYS.QCMDEXC('GRTOBJAUT OBJ(QSYS/JDBADLIB) OBJTYPE(*LIB) USER(" + badUser
                + ") AUT(*EXCLUDE)                                                                                           ',"
                + "0000000080.00000 ) ",
            "CALL QSYS.QCMDEXC('CRTUSRPRF USRPRF(" + goodUser + ") PASSWORD(" + goodPasswd
                + ")                               '," + "0000000070.00000 ) ", };
        executeStatements(s1, createCommands);

      } catch (Exception e) {
        failed(e, "Error during testcase setup " + added);
        return;
      }

      testBadProfile(goodUser, goodPasswd, badUser, badPasswd, expectedBadConnectException,
          expectedBadConnectException2, expectedBadConnectException3, added);

      System.gc();

      try {
        if (s1 != null)
          s1.close();

        // if (c1 != null)
        c1.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  
  

}
