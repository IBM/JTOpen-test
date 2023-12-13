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

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDriverConnect.java
//
// Classes:      JDDriverConnect
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import java.awt.TextArea;
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
import java.util.Hashtable;
import java.util.Properties;
import java.util.Random;

// Converted to use reflection to test
import com.ibm.as400.access.AS400JDBCDriver; //@A1A
import com.ibm.as400.access.IFSSystemView;
import com.ibm.as400.access.SecureAS400; //@A2A

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

  // Private data.
  private Driver driver_;
  String letter_;
  int jdk_;
  private String      powerUserID_;
  private String      powerPassword_;
  /**
   * Constructor.
   **/
 
    public JDDriverConnect (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         String password,
                         String powerUserID,
                         String powerPassword)
    {
        super (systemObject, "JDDriverMisc",
               namesAndVars, runMode, fileOutputStream, 
               password);

        systemObject_ = systemObject;

        powerUserID_   = powerUserID;
        powerPassword_ = powerPassword;
    }



  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    jdk_ = JVMInfo.getJDK();
    driver_ = DriverManager.getDriver(baseURL_);

    letter_ = JDDriverTest.COLLECTION
        .substring(JDDriverTest.COLLECTION.length() - 1);
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
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC Driver does not permit null USERID");
    } else {
      try {
        String url = baseURL_ + ";user=" + userId_ + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.3");
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

  /**
   * connect() - When a valid URL is specified, and user and password are passed
   * in the properties object, then we can run statements.
   **/
  public void Var004() {
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

  /**
   * connect() - When a valid URL is specified, and user and password are passed
   * in the URL, then we can run statements.
   **/
  public void Var005() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC Driver does not permit null USERID");
    } else {

      try {
        String url = baseURL_ + ";user=" + userId_ + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.5");
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

  /**
   * connect() - When a valid URL is specified, and user and password are passed
   * in both the URL and properties, then we can run statements. Verify that the
   * user and password that are used come from the URL.
   **/
  public void Var006() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC Driver does not permit null USERID");
    } else {
      try {
        String url = baseURL_ + ";user=" + userId_ + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.6");
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

  /**
   * connect() - When a valid URL is specified, but the system is not a valid
   * system, then an exception should be thrown.
   **/
  public void Var007() {
    try {
      String url = (isToolboxDriver()) ? "jdbc:as400://BAD_SYSTEM"
          : "jdbc:db2://BAD_SYSTEM";
      if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
        url = "jdbc:jtopenlite://BAD_SYSTEM";
      }
      Properties p = new Properties();
      p.put("user", userId_);
      p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.7"));

      /// failed ("This variation is deactivated until the AS400 class is
      /// serializable. -jlee 04/20/99");
      Connection c = driver_.connect(url, p);

      failed("Did not throw exception. " + c);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * connect() - When a valid URL is specified, but the user is not a valid
   * user, then an exception should be thrown.
   **/
  public void Var008() {
    try {
      String url = baseURL_;
      Properties p = new Properties();
      p.put("user", "MRNOBODY");
      p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.8"));
      p.put("prompt", "false");

      Connection c = driver_.connect(url, p);

      failed(
          "Did not throw exception.  This variation is expected to fail if a secondary URL was specified. "
              + c);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
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

      failed(
          "Did not throw exception.  This variation is expected to fail if a secondary URL was specified. "
              + c);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * connect() - When a valid URL is specified but it contains an unrecognized
   * property, then a warning is posted, but we can still run statements.
   **/
  public void Var010() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC Driver does not permit null USERID");
    } else {

      try {
        String url = baseURL_ + ";user=" + userId_ + ";himom=look at me"
            + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.10");
        Properties p = new Properties();

        Connection c = driver_.connect(url, p);
        SQLWarning w = c.getWarnings();

        // Run a query.
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
        boolean found = rs.next();

        // Close the connection.
        c.close();

        assertCondition((found) && (w != null),
            "found=" + found + " sb true warning is " + w);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * "errors" property - Set to "basic", verify that only first level text is
   * returned.
   **/
  public void Var011() {
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

      assertCondition(messageText.indexOf(". . .") == -1,
          "Did not find '. . .' in '" + messageText + "'");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * "errors" property - Set to "full", verify that first and second level text
   * is returned.
   **/
  public void Var012() {
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

  /**
   * "secure" property - Verify that secure connections work.
   * 
   * <p>
   * You need the certificate (e.g. KeyRing.class) in the classpath to get this
   * to work. Also need the com.ibm.sslight package in the classpath.
   * 
   * <p>
   * Also, the following command needs to be run once (on any client machine)
   * for each AS/400 that you're testing to. Note that once this command has
   * been run from *any* client machine for a particular AS/400, that AS/400 is
   * good until the next scratch-install.
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
    if (checkNotGroupTest()) {
      if (jdk_ <= JVMInfo.JDK_142) {
        // System.out.println("The SSL testcase does not work for JDK 1.4 or
        // lower");
        assertCondition(true);
        return;
      }
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        notApplicable("'secure' property not supported.");
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

        if (getRelease() >= JDTestDriver.RELEASE_V4R4M0) {
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
        } else {
          try {
            Connection c = driver_.connect(url, p);
            failed("Did not throw exception. " + c);
          } catch (Exception e) {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          }
        }
      } catch (Exception e) {
        failed(e, "SSL not configured. "
            + "If you get a certificate chaining error, you need "
            + "to get the CA certificate and install it using "
            + "keytool -import -keystore /QOpenSys/QIBM/ProdData/JavaVM/jdk60/32bit/jre/lib/security/cacerts -file /tmp/eberhard.ca2 -storepass changeit");
      }
    }
  }

  /**
   * "trace" property - Set to "false", verify that driver manager tracing is
   * not turned on.
   **/
  public void Var014() {
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

  /**
   * "trace" property - Set to "true", verify that driver manager tracing is
   * turned on.
   */
  public void Var015() {

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

  /**
   * "driver" property - Set to "toolbox", verify that the connection works.
   */
  public void Var016() {
    try {
      if (isToolboxDriver()) {
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
      } else
        notApplicable();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * "driver" property - Set to "native", verify that the connection works.
   **/
  public void Var017() {
    try {
      if ((isToolboxDriver())) // pdd native driver should load and throw exc if
                               // on windows &&
                               // (getOS().equalsIgnoreCase("os/400")))
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
      } else
        notApplicable();
    } catch (Exception e) {
      failed(e, "Unexpected Exception connecting with native and "+userId_);
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
      AS400 o = new AS400(systemObject_.getSystemName(),
          systemObject_.getUserId(), charPassword);
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
      AS400 o = new AS400(systemObject_.getSystemName(),
          systemObject_.getUserId(), charPassword);
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
    if (!isToolboxDriver()) {
      notApplicable("connect(AS400) variation");
      return;
    }

    try {
      AS400JDBCDriver d = new AS400JDBCDriver();
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_); 
      AS400 o = new AS400(systemObject_.getSystemName(),
          systemObject_.getUserId(), charPassword);
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

  /**
   * connect(String, Properties, AS400) - Passing valid objects and setting
   * "driver" property to "toolbox" should complete successfully.
   **/
  public void Var022() {
    if (!isToolboxDriver()) {
      notApplicable("connect(AS400) variation");
      return;
    }

    try {
      AS400JDBCDriver d = new AS400JDBCDriver();
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_); 
     AS400 o = new AS400(systemObject_.getSystemName(),
          systemObject_.getUserId(), charPassword);
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

  /**
   * connect(AS400, Properties, String) - Passing a null Properties object
   * should throw a null pointer exception.
   **/
  public void Var023() {
    if (!isToolboxDriver()) {
      notApplicable("connect(AS400) variation");
      return;
    }

    try {
      AS400JDBCDriver d = new AS400JDBCDriver();
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_); 
      AS400 o = new AS400(systemObject_.getSystemName(),
          systemObject_.getUserId(), charPassword);
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
   * connect(AS400, Properties, String) - Passing a null AS400 object should
   * throw null pointer exception.
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
      if (jdk_ <= JVMInfo.JDK_142) {
        // System.out.println("The SSL testcase does not work for JDK 1.4 or
        // lower");
        assertCondition(true);
        return;
      }
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
       SecureAS400 o = new SecureAS400(systemObject_.getSystemName(),
            systemObject_.getUserId(), charPassword);
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
      if (jdk_ <= JVMInfo.JDK_142) {
        // System.out.println("The SSL testcase does not work for JDK 1.4 or
        // lower");
        assertCondition(true);
        return;
      }
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

      SecureAS400 o = new SecureAS400(systemObject_.getSystemName(),
            systemObject_.getUserId(), charPassword);
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
   * connect(String, Properties, SecureAS400) - Passing valid objects and
   * setting "driver" property to "native" should complete successfully.
   **/
  public void Var028() {
    if (checkNotGroupTest()) {
      if (jdk_ <= JVMInfo.JDK_142) {
        // System.out.println("The SSL testcase does not work for JDK 1.4 or
        // lower");
        assertCondition(true);
        return;
      }
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
      SecureAS400 o = new SecureAS400(systemObject_.getSystemName(),
            systemObject_.getUserId(), charPassword);
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

  /**
   * connect(String, Properties, SecureAS400) - Passing valid objects and
   * setting "driver" property to "toolbox" should complete successfully.
   **/
  public void Var029() {
    if (checkNotGroupTest()) {
      if (jdk_ <= JVMInfo.JDK_142) {
        // System.out.println("The SSL testcase does not work for JDK 1.4 or
        // lower");
        assertCondition(true);
        return;
      }
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
      SecureAS400 o = new SecureAS400(systemObject_.getSystemName(),
            systemObject_.getUserId(), charPassword);
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
     SecureAS400 o = new SecureAS400(systemObject_.getSystemName(),
          systemObject_.getUserId(), charPassword);
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
   * connect(SecureAS400, Properties, String) - Passing a null AS400 object
   * should throw null pointer exception.
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
   * connect() - When a valid URL is specified, but the password is empty, then
   * an exception should be thrown.
   **/
  public void Var032() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "QSECOFR");
        p.put("password", "");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed(
            "Did not throw exception when passing empty password but passing userId   = "
                + getUser(c) + "-- native driver added 8/19/2003");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * connect() - When a valid URL is specified, but the password is not set,
   * then an exception should be thrown.
   **/
  public void Var033() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "QSECOFR");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed(
            "Did not throw exception when not passing password but passing userId  = "
                + getUser(c) + " -- native driver added 8/19/2003");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * connect() - When a valid URL is specified, but the password is *NONE, then
   * an exception should be thrown.
   **/
  public void Var034() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "QSECOFR");
        p.put("password", "*NONE");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed(
            "Did not throw exception when passing *NONE password but passing userId = "
                + getUser(c) + "  -- native driver added 8/19/2003");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * connect() - When a valid URL is specified, but the password is **********,
   * then an exception should be thrown.
   **/
  public void Var035() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "QSECOFR");
        p.put("password", "**********");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed(
            "Did not throw exception when passing ******** password but passing userId = "
                + getUser(c) + "  -- native driver added 8/19/2003");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * Conenct using db2j as the subprotocol, an exception should be thrown since
   * db2j is the Cloudscape driver. Code in DB2Driver.java was changed to check
   * the subprotocol using equals instead of startsWith to account for this.
   **/
  public void Var036() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {

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
    } else {
      notApplicable(
          "connect Native Driver V5R3 variation -- native driver added 3/3/2004");
    }
  }

  /**
   * connect() - When a valid URL is specified, but the user and id are empty,
   * then an exception should be thrown.
   **/
  public void Var037() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "");
        p.put("password", "");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed(
            "Did not throw exception when passing userid and password as blank -- native driver added 12/11/2007"
                + c);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are
   * *CURRENT, then an exception should be thrown.
   **/
  public void Var038() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {

      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "*CURRENT");
        p.put("password", "*CURRENT");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);

        failed(
            "Did not throw exception when passing userid and password as *CURRENT -- native driver added 12/11/2007 "
                + c);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are empty,
   * then an exception should be thrown.
   **/
  public void Var039() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {

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
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are
   * *CURRENT, then an exception should be thrown.
   **/
  public void Var040() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {

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
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are
   * *CURRENT, then an exception should be thrown.
   **/
  public void Var041() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {

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
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  public void enableWorkaround() {
    try {
      String url = baseURL_ + ";user=" + userId_ + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.workaround");
      Connection c = driver_.connect(url, null);
      Statement s = c.createStatement();
      s.executeUpdate(
          "CALL QSYS.QCMDEXC(' CRTDTAARA DTAARA(QSYS/QSQCLICON) TYPE(*CHAR) LEN(7)  VALUE(*PRVCHK)        ',   0000000070.00000)");
      s.close();
      c.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void disableWorkaround() {

    try {
      String url = baseURL_ + ";user=" + userId_ + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.disable");
      Connection c = driver_.connect(url, null);
      Statement s = c.createStatement();
      s.executeUpdate(
          "CALL QSYS.QCMDEXC(' DLTDTAARA DTAARA(QSYS/QSQCLICON)                  ',   0000000040.00000)");
      s.close();
      c.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are empty,
   * then an exception should be not be thrown when workaround enabled.
   **/
  public void Var042() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {
      enableWorkaround();
      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "");
        p.put("password", "");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);
        c.close();
        if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
          assertCondition(true);
        } else {
          failed(
              "Did not throw exception when passing userid and password as blanks in V7R1 with dataarea created ");

        }

      } catch (Exception e) {
        if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
          failed(e,
              "Threw exception when passing userid and password as blank when workaround enabled -- native driver added 1/2/2008");
        } else {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }

      }
      disableWorkaround();
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * Passing *CURRENT when workaround enabled.
   **/
  public void Var043() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {

      enableWorkaround();
      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "*CURRENT");
        p.put("password", "*CURRENT");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);
        c.close();
        if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
          assertCondition(true);
        } else {
          failed(
              "Did not throw exception when passing userid and password as *CURRENT in V7R1 with dataarea created ");

        }

      } catch (Exception e) {
        if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
          failed(e,
              "With workaround, threw exception when passing userid and password as *CURRENT -- native driver added 1/2/2008");
        } else {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }

      }
      disableWorkaround();
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are empty,
   * then an exception should be thrown.
   **/
  public void Var044() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {
      enableWorkaround();
      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "       ");
        p.put("password", "       ");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);
        c.close();

        if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
          assertCondition(true);
        } else {
          failed(
              "Did not throw exception when passing userid and password as blanks in V7R1 with dataarea created ");

        }

      } catch (Exception e) {
        if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
          failed(
              "With workaround, threw exception when passing userid and password as many blanks -- native driver added 1/2/2008");
        } else {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
      disableWorkaround();
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are
   * *CURRENT, then an exception should be thrown.
   **/
  public void Var045() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {

      enableWorkaround();
      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "         *CURRENT");
        p.put("password", "*CURRENT");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);
        c.close();

        if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
          assertCondition(true);
        } else {
          failed(
              "Did not throw exception when passing userid and password as *CURRENT in V7R1 with dataarea created ");

        }

      } catch (Exception e) {

        if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
          failed(e,
              "With workaround, threw exception when passing userid and password as '   *CURRENT' -- native driver added 1/2/2008");
        } else {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }

      }
      disableWorkaround();
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * connect() - When a valid URL is specified, but the user and id are
   * *CURRENT, then an exception should be thrown.
   **/
  public void Var046() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {
      enableWorkaround();
      try {
        String url = baseURL_;
        Properties p = new Properties();
        p.put("user", "*cUrReNt");
        p.put("password", "*CURRENT");
        p.put("prompt", "false");

        Connection c = driver_.connect(url, p);
        c.close();
        if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
          assertCondition(true);
        } else {
          failed(
              "Did not throw exception when passing userid and password as *CURRENT in V7R1 with dataarea created ");

        }

      } catch (Exception e) {
        if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {

          failed(
              "With workaround, threw  exception when passing userid and password as '   *cUrReNt' -- native driver added 1/2/2008");
        } else {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }

      }
      disableWorkaround();
    } else {
      notApplicable("connect Native Driver  V5R3 variation ");
      return;
    }

  }

  /**
   * connect() - Should throw exception when id/pass are "" or *CURRENT With no
   * user/password in url properties
   **/
  public void Var047() {
    Connection conn1;
    if (!JDTestDriver.getClientOS().equals(JDTestDriver.CLIENT_as400)) {
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
        sb.append("***** part0 failed to connect ** url="+url+ " Stack trace is\n");
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
        sb.append(
            "part4 connected with *CURRENT id/pass to " + conn1 + ";  \n");
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
   * connect() - Should throw exception when id/pass are "" or *CURRENT With
   * some user/password in url properties
   **/
  public void Var048() {
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
        String urlB = url + ";user=" + userId_ + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.48");// should
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
        String urlB = url + ";user=" + userId_ + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.48");// should
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
        String urlB = url + ";user=" + userId_ + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.48");// should
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

  /**
   * connect() - Should throw exception when id/pass are "" or *CURRENT With
   * user/password in properties object
   **/
  public void Var049() {
    if (!JDTestDriver.getClientOS().equals(JDTestDriver.CLIENT_as400)) {
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

      Connection pwrConn = DriverManager.getConnection(url,
          testDriver_.pwrSysUserID_, testDriver_.pwrSysPassword_);// use current

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
        conn1 = DriverManager.getConnection(urlB, userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.49"));

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
        conn1 = DriverManager.getConnection(urlB, userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.49"));

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
          conn1 = DriverManager.getConnection(urlB, userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.49"));
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
      String sql = "CALL QSYS2.QCMDEXC('CHGUSRPRF USRPRF(" + userId_
          + ") STATUS(*ENABLED)   ')";
      stmt.executeUpdate(sql);

      // part 5a make sure we still can connect with userid password
      try {
        String urlB = baseURL_ + ";errors=full";
        conn1 = DriverManager.getConnection(urlB, userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.49"));

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
    if (!JDTestDriver.getClientOS().equals(JDTestDriver.CLIENT_as400)) {
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
          conn1 = DriverManager.getConnection(url, testDriver_.pwrSysUserID_,
              "*CURRENT");// use current
	  stats += "part5 connected with *CURRENT password"; 
        } catch (Exception e1) {
          conn1 = DriverManager.getConnection(url,
              System.getProperty("user.name"), "*CURRENT");// use current
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
   * connect() - Should throw exception when id/pass are "" or *CURRENT With
   * some user/password in url properties copy of 048 but with secure current
   * user = false
   **/
  public void Var051() {
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

          String url2 = url + "user=" + testDriver_.pwrSysUserID_
              + ";password=*CURRENT";
          conn1 = DriverManager.getConnection(url2);
 	stats += "part4 connected with *CURRENT password;  "; 
       } catch (Exception e1) {

          String url2 = url + "user=" + System.getProperty("user.name")
              + ";password=*CURRENT";
          conn1 = DriverManager.getConnection(url2);
	  stats += "part4 connected with *CURRENT password;  "; 
        }

      } catch (Exception e) {
 
      }

      // part 6 (code update 2)
      try {
        String urlB = url + ";user=" + userId_ + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.51");// should
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
        String urlB = url + ";user=" + userId_ + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverConnect.51");// should
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

  /**
   * connect() - Should throw exception when id/pass are "" or *CURRENT With
   * user/password in properties object * copy of 049 but with secure current
   * user = false
   **/
  public void Var052() {
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
     * This testcase tries to use the GUI. When running on the 400, the
     * following error is seen. 5/5/2009
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
      notApplicable(
          "Test attempts to use GUI.  Not Applicable when running on 400");
      return;
    }

    if (JDTestDriver.getClientOS().equals(JDTestDriver.CLIENT_linux)) {
      notApplicable(
          "Test attempts to use GUI.  Not Applicable when running on linux");
      return;
    }

    try {
      // use an AS400 object
      AS400JDBCDriver d = new AS400JDBCDriver();
       char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_); 
     AS400 o = new AS400(systemObject_.getSystemName(),
          systemObject_.getUserId(), charPassword);
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

  public void testBadProfile(String goodUser, String goodPasswd, String badUser,
      String badPasswd, String expectedBadConnectException,
      String expectedBadConnectException2, String expectedBadConnectException3,
      String added) {
    String badConnectException = "NO EXCEPTION";
    try {

      // Grab a bunch of connection to improve the likelihood of the
      // next line failing

      Connection[] c2 = new Connection[20];
      String[] jobnames = new String[20];
      try {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < c2.length; i++) {
          c2[i] = DriverManager.getConnection(baseURL_ + ";errors=full",
              badUser, badPasswd);
          if (c2[i] instanceof com.ibm.as400.access.AS400JDBCConnection) {
            com.ibm.as400.access.AS400JDBCConnection c = (com.ibm.as400.access.AS400JDBCConnection) c2[i];
            String jobname = c.getServerJobIdentifier();
            sb.append("Job[" + i + "]=" + jobname + "\n");
            jobnames[i] = jobname;
          } else if (JDReflectionUtil.instanceOf(c2[i],"com.ibm.db2.jdbc.app.DB2Driver")) {
            Object c =  c2[i];
            String jobname = JDReflectionUtil.callMethod_S(c,
                "getServerJobName");
            sb.append("Job[" + i + "]=" + jobname + "\n");
            jobnames[i] = jobname;
          }

          Statement s = c2[i].createStatement();
          ResultSet rs = s.executeQuery("select * from sysibm.sysdummy1");
          while (rs.next()) {

          }
          rs.close();
          s.close();

        }

        System.out.println(
            "Error did not get exception after connecting with pad proflie .  Sleeping for 120 seconds.");
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

        badConnectException = "NO EXCEPTION after " + c2.length
            + " attempts with " + badUser;
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

      for (int j = 0; j < 2; j++) {
        for (int i = 0; i < c3[j].length; i++) {
          try {
            attemptCount++;
            System.out.println("[" + i + "] Connecting with " + goodUser);
            c3[j][i] = DriverManager.getConnection(baseURL_ + ";errors=full",
                goodUser, goodPasswd);
            c3[j][i].createStatement();
          } catch (SQLException ex) {
            exceptionCount++;
            thrownException = ex;
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
       * "DLTUSRPRF "+badUser+"                               ',"+
       * "0000000040.00000 ) ");
       */

      s2.close();

      for (int i = 0; i < c3.length; i++) {
        for (int j = 0; j < c3[i].length; j++) {
          c3[i][j].close();
        }
      }

      assertCondition(
          badConnectException.indexOf(expectedBadConnectException) >= 0

              || badConnectException.indexOf(expectedBadConnectException2) >= 0
              || badConnectException.indexOf(expectedBadConnectException3) >= 0,
          "Expected '" + expectedBadConnectException + "' or '"
              + expectedBadConnectException2.replace(' ', '_') + "' or '"
              + expectedBadConnectException3.replace(' ', '_') + "' got '"
              + badConnectException + "'" + added);

    } catch (Exception e) {
      failed(e, "Unexpected Exception" + added + "  badConnectionException="
          + badConnectException);
    }

  }

  public void Var054() { notApplicable(); }

  public void executeStatements(Statement s1, String[] commands)
      throws SQLException {
    for (int i = 0; i < commands.length; i++) {
      String command = commands[i];
      s1.executeUpdate(command);
    }
  }

  public void executeStatementsIgnoreErrors(Statement s1, String[] commands,
      String[] expectedErrors) {
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
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
        && "OS/400".equals(System.getProperty("os.name"))) {
      try {
        Connection c = DriverManager
            .getConnection("jdbc:as400:localhost;driver=native");

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
		  System.out.println("..Running leak test with user id "+userId_); 
		  Connection connection = testDriver_.getConnection (baseURL_,userId_, encryptedPassword_);

                  /* Run for 6 seconds in 10 sec batches */
		  for (int i = 0; i < 6  && passed ; i++) { 
	
		      Connection [] connections = new Connection[20];
		      long endtime = System.currentTimeMillis() + 10000; 
		      while ( System.currentTimeMillis() < endtime ) {
			  int index  = random.nextInt(connections.length);
			  if (connections[index] != null) {
			  // Let the garbage collector close
			  // sb.append("Closing connection at index "+index+"\n"); 
			  // connections[index].close();
			  }
			  sb.append("Opening  connection at index "+index+"\n"); 
			  connections[index] = testDriver_.getConnection (baseURL_,userId_, encryptedPassword_); 
		      }
		      for (int index = 0; index < connections.length; index++) {
			  if (connections[index] != null) {
			      sb.append("Closing connection at index "+index+"\n"); 
			      connections[index].close();
			  }
		      } 

		      Statement s = connection.createStatement();

		      ResultSet rs = s.executeQuery("select JOB_NAME  from table( qsys2.active_job_info  ()) A, TABLE(qsys2.STACK_INFO(JOB_NAME)) B where JOB_STATUS='DEQW' and JOB_NAME like '%QUSER%' and PROCEDURE_NAME='takedescriptor'" ); 
		      while (rs.next()) {
			  passed = false;
			  sb.append("ERROR:  Found job with takedescriptor in stack "+rs.getString(1)+"\n"); 
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

  /* Var059 Check that a connection can be established using an additional authentication factor */ 
  public void Var059() {
	  if (checkToolbox()) {
		  String systemName = systemObject_.getSystemName(); 
		  if (checkAdditionalAuthenticationFactor(systemName)) {
			  try { 
				 initMfaUser(); 
				 String mfaFactorString = new String(mfaFactor_); 
				 Connection c = DriverManager.getConnection(
						 "jdbc:as400:"+ systemName+";additionalAuthenticationFactor="+mfaFactorString,
						 mfaUserid_, 
						 new String(mfaPassword_)); 
				 Statement s = c.createStatement();
				 ResultSet rs = s.executeQuery("VALUES CURRENT USER"); 
				 rs.next();
				 String currentUser = rs.getString(1); 
				 System.out.println("current MFA user is "+currentUser); 
				 assertCondition(c != null && mfaUserid_.equalsIgnoreCase(currentUser), 
						 "currentUser="+currentUser+" MFAUserID="+mfaUserid_);
			  } catch (Exception e) {
				  failed(e, "unexpected exception");
		      }
		  }
	  }
  }
  public void Var060() { notApplicable();}
  public void Var061() { notApplicable();}
  public void Var062() { notApplicable();}
  public void Var063() { notApplicable();}
  public void Var064() { notApplicable();}
  public void Var065() { notApplicable();}
  public void Var066() { notApplicable();}
  public void Var067() { notApplicable();}
  public void Var068() { notApplicable();}
  public void Var069() { notApplicable();}
  public void Var070() { notApplicable();}



  /*
   * Connect after connecting with a bad profile. See CPS 8ASS3J
   */

  public void Var071() {
    if (checkNotGroupTest()) {
      // If we are running with the toolbox driver and the jt400Native.jar
      // then this testcase will fail prior to V7R2. Mark as successful
      // if prior to V7R2.
      if ((getRelease() < JDTestDriver.RELEASE_V7R2M0)
          && (System.getProperty("java.class.path").toLowerCase()
              .indexOf("jt400native") > 0)) {
        System.out.println(
            "Connect with bad profile not applicable for jt400Native.jar and before V7R2.  See issue 45361");
        assertCondition(true);
        return;
      }

      // If we are running with the toolbox driver in V6R1, we don't get an
      // error
      if ((isToolboxDriver())
          && (getRelease() == JDTestDriver.RELEASE_V6R1M0)) {
        notApplicable("Toolbox in V6R1 does not throw error");
        return;
      }

      // Not fixed for V5R4

      if ((getDriver() == JDTestDriver.DRIVER_NATIVE)
          && (getRelease() == JDTestDriver.RELEASE_V5R4M0)) {
        System.out.println("PTF for APAR SE45716 not created to V5R4");
        assertCondition(true);
        return;
      }

      /* Each profile has the last letter of the library which is usually */
      /* the last letter of the test. JDRunit will not have two versions */
      /* of a testcase running with the same letter */

      String added = " -- added 11/2/2010 connect after connect with bad profile native CPS 8ASS3J:SE45716  PTFs 61:SI41813,71:SI42110 ";
      String goodUser = "JDBGDUSR" + letter_;
      String goodPasswd = "xyz123zyz";
      String badUser = "JDBBDUSR" + letter_;
      String badPasswd = "xyz123zyz";
      String expectedBadConnectException = "Error occurred in SQL Call Level Interface";
      // V5R4 shows -- Connection to relational database ?LP03UT5 does not
      // exist.
      String expectedBadConnectException2 = "does not exist";
      String expectedBadConnectException3 = "Processing of the SQL statement ended";
      Connection c1 = null;
      Statement s1 = null;

      try {

        if (isToolboxDriver()) {
          expectedBadConnectException = "Communication link failure";
          /*
           * if (getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
           * expectedBadConnectException ="General security error"; }
           */
        }

        if (getDriver() == JDTestDriver.DRIVER_NATIVE
            && getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
          expectedBadConnectException = "Processing of the SQL statement ended";
        }

        c1 = DriverManager.getConnection(baseURL_ + ";errors=full", powerUserID_, powerPassword_);


        // Delete and recreate the bad profile
        s1 = c1.createStatement();

        try {
          s1.executeUpdate("CALL QSYS2.QCMDEXC('" + "DLTUSRPRF " + badUser
			   + "  OWNOBJOPT(*CHGOWN JAVA) '  ) ");
        } catch (Exception e) {
          if (e.toString().indexOf("not found") >= 0) {
            // Just ignore */
          } else {
            System.out.println("Unexpected exception deleting old profile");
            e.printStackTrace();
          }
        }

        s1.executeUpdate("CALL QSYS.QCMDEXC('" + "CRTUSRPRF USRPRF(" + badUser
            + ") PASSWORD(" + badPasswd + ")                               ',"
            + "0000000070.00000 ) ");

        s1.executeUpdate("CALL QSYS.QCMDEXC('"
            + "GRTOBJAUT OBJ(QGPL/QDFTJOBD) OBJTYPE(*JOBD) USER(" + badUser
            + ") AUT(*EXCLUDE)                                        ',"
            + "0000000080.00000 ) ");

        try {

          s1.executeUpdate("CALL QSYS2.QCMDEXC('" + "DLTUSRPRF " + goodUser
              + "     OWNOBJOPT(*CHGOWN JAVA) ') ");
        } catch (Exception e) {
          if (e.toString().indexOf("not found") >= 0) {
            // Just ignore */
          } else {
            System.out.println("Unexpected exception deleting old profile");
            e.printStackTrace();
          }
        }

        s1.executeUpdate("CALL QSYS.QCMDEXC('" + "CRTUSRPRF USRPRF(" + goodUser
            + ") PASSWORD(" + goodPasswd + ")                               ',"
            + "0000000070.00000 ) ");
      } catch (Exception e) {
        failed(e, "Error during testcase setup " + added);
        return;
      }

      testBadProfile(goodUser, goodPasswd, badUser, badPasswd,
          expectedBadConnectException, expectedBadConnectException2,
          expectedBadConnectException3, added);

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

  public void Var072() {
    if (checkNotGroupTest()) {
      String added = " -- added 01/03/2012 connect after connect with bad profile (because of bad library list entry) CPS 8P6TF6  PTFs 61:SI45605,71:SI45604 ";

      // If we are running with the toolbox driver and the jt400Native.jar
      // then this testcase will fail prior to V7R2. Mark as successful
      // if prior to V7R2.
      if ((getRelease() < JDTestDriver.RELEASE_V7R2M0)
          && (System.getProperty("java.class.path").toLowerCase()
              .indexOf("jt400native") > 0)) {
        System.out.println(
            "Connect with bad profile not applicable for jt400Native.jar and before V7R2.  See issue 45361");
        assertCondition(true);
        return;
      }

      // If we are running with the toolbox driver in V6R1, we don't get an
      // error
      if ((isToolboxDriver())
          && (getRelease() == JDTestDriver.RELEASE_V6R1M0)) {
        notApplicable("Toolbox in V6R1 does not throw error");
        return;
      }

      // Not fixed for V5R4

      if ((getDriver() == JDTestDriver.DRIVER_NATIVE)
          && (getRelease() == JDTestDriver.RELEASE_V5R4M0)) {
        System.out.println("PTF for APAR SE45716 not created to V5R4");
        assertCondition(true);
        return;
      }

      String goodUser = "JDBGDUS2" + letter_;
      String goodPasswd = "xyz123zyz";
      String badUser = "JDBBDUS2" + letter_;
      String badPasswd = "xyz123zyz";
      String expectedBadConnectException = "reason code is 0";
      // V5R4 shows -- Connection to relational database ?LP03UT5 does not
      // exist.
      String expectedBadConnectException2 = "does not exist";
      String expectedBadConnectException3 = "does not exist";
      Connection c1 = null;
      Statement s1 = null;

      try {

        if (isToolboxDriver()) {
          expectedBadConnectException = "Communication link failure";
          /*
           * if (getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
           * expectedBadConnectException ="General security error"; }
           */

        }
        c1 = DriverManager.getConnection(baseURL_ + ";errors=full", powerUserID_, powerPassword_);


        // Delete and recreate the bad profile
        s1 = c1.createStatement();

        String deleteCommands[] = {
            "CALL QSYS2.QCMDEXC('DLTUSRPRF " + badUser + " OWNOBJOPT(*CHGOWN JAVA)  ') ",
            "CALL QSYS.QCMDEXC('CHGJOB INQMSGRPY(*SYSRPYL)      ',0000000030.00000 ) ",
            "CALL QSYS.QCMDEXC('DLTLIB JDBADLIB         ',0000000020.00000 ) ",
            "CALL QSYS.QCMDEXC('DLTJOBD JOBD(QGPL/JDBADLIBL)  ', 0000000030.00000 )",
            "CALL QSYS2.QCMDEXC('DLTUSRPRF " + goodUser    + "  OWNOBJOPT(*CHGOWN JAVA)  ') ", };
        String expectedErrors[] = { "not found" };
        executeStatementsIgnoreErrors(s1, deleteCommands, expectedErrors);

        String createCommands[] = {
            "CALL QSYS.QCMDEXC('CRTLIB JDBADLIB      ',0000000020.00000 ) ",
            "CALL QSYS.QCMDEXC('CRTJOBD JOBD(QGPL/JDBADLIBL) JOBQ(QINTER) INLLIBL(JDBADLIB)                                    ',"
                + "0000000080.00000 ) ",
            "CALL QSYS.QCMDEXC('CRTUSRPRF USRPRF(" + badUser + ") PASSWORD("
                + badPasswd
                + ") JOBD(QGPL/JDBADLIBL)                                    ',"
                + "0000000090.00000 ) ",
            "CALL QSYS.QCMDEXC('GRTOBJAUT OBJ(QSYS/JDBADLIB) OBJTYPE(*LIB) USER("
                + badUser
                + ") AUT(*EXCLUDE)                                                                                           ',"
                + "0000000080.00000 ) ",
            "CALL QSYS.QCMDEXC('CRTUSRPRF USRPRF(" + goodUser + ") PASSWORD("
                + goodPasswd + ")                               ',"
                + "0000000070.00000 ) ", };
        executeStatements(s1, createCommands);

      } catch (Exception e) {
        failed(e, "Error during testcase setup " + added);
        return;
      }

      testBadProfile(goodUser, goodPasswd, badUser, badPasswd,
          expectedBadConnectException, expectedBadConnectException2,
          expectedBadConnectException3, added);

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
