///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  TestDriver.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Date; 
import java.util.jar.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.SecureAS400;
import com.ibm.as400.access.Trace;

import com.ibm.as400.security.auth.ProfileTokenCredential;

/**
 * Represents a test program or applet for a single component. Each component
 * will create a subclass of this class. Test drivers are called in the
 * following manner:
 * <p>
 * Use the following parameter tags and values:
 * 
 * <pre>
 *  -system    - The IBM i server with which to connect.
 *  -file      - The file in which to write results.  Results are always written to standard out, if this tag is specified, results are also written to this file.
 *  -run       - The run mode.  Values are 'u' for unattended variations only, 'a' for attended variations only, or 'b' for both.  The default value is 'u'.
 *  -uid       - The user profile name to use to authenticate to the server.
 *  -pwd       - The user profile password to use to authenticate to the server.
 *  -proxy     - The proxy server system name with which to connect.
 *  -tc        - Testcase to be run.  If not specififed, all testcases will be run.  This token can be specified several times.
 *  -vars      - Variations to be run.  Several values can be specified by separating each value with a comma.  This token must be specified immediately after a -tc token, it applies to the testcase which it follows.  If not specified, all variations will be run.
 *  -misc      - Miscellaneous information that is passed to all testcases.
 *  -trace     - Categories to trace.  Multiple categories can be specified by separating each value with a comma.  The categories are datastream, diagnostic, error, information, jdbc, warning, conversion, pcml.  Category names are not case sensitive.
 *  -lib       - The name of the test library to create files, etc. in.  This option is used (initially) by Record-level Access.  This field can now be specified by the user as the place to put the tables/procs etc.
 *  -ssl       - Connect using secure sockets connections.
 *  -socks     - Set the system object's mustUseSockets property to true.
 *  -pwrSys    - A userID, password combination with *SECOFR authority.
 *  -nothreads - Set the system object's isThreadUsed property to false.
 *  -printer   - The name of the printer to use for the NPPrint testcases.
 *  -profileToken - Generate and use a profile token for authentication.
 *  -serialize - Indicates that the AS400 object is to be deserialized/serialized.  A file named "as400.ser" is sought and, if found, deserialized into systemObject_.  Upon testcase completion, the file is (re)generated.
 *  -kerberos  - Use Kerberos for authentication.
 *  -native    - The native optimizations classes are available.
 *  -local     - The target system is the same system we are running on.
 *  -onAS400   - The client is an iSeries server.
 *  -brief     - Skip long-running variations.
 *  -duration  - Number of seconds to run testcase.
 *  -pause     - Waits for the user to press enter before running the variations to obtain the jobs running on the server, depending on the class type.
 *  -directory - The directory where some test driver variations point to.
 *  -jndi      - The type of jndi used for some jdbc tests.
 *  -cleanup   - NLSTests can have cleanup, this flags indicates if it is needed.
 *  -asp       - Indicates the ASP to use for the test
 *  -extendedDynamic - Indicates that extended dynamic should be used for the toolbox driver
 * Examples:
 *    java ExampleDriver
 *    java ExampleDriver -system rchas57a
 *    java ExampleDriver -system rchas57a -file example.out -run B -uid tester -pwd secret -tc testcase1
 *    java ExampleDriver -system rchas57a -file example.out -run B -tc testcase1 -vars 1,2,3  -tc testcase2 -vars 3,9 -tc testcase3
 *    java ExampleDriver -system rchas57a -tc testcase1 -vars 3,12:25     // runs variations 3 and 12 thru 25.
 * </pre>
 * 
 * If system or uid or pwd are not specified, you will be prompted for a system
 * name or a userid or password, even if running only unattended variations.
 **/
public abstract class TestDriver implements TestDriverI, Runnable,
    ActionListener {


  /**
   *
   */
  static final long serialVersionUID = 1L;

  static TestDriverTimeoutThread timeoutThread = null;

  protected String outputFileName_ = null;
  // Set of Testcase objects for this component. This is filled in by the
  // createTestcases() method.
  protected Vector<Testcase> testcases_ = new Vector<Testcase>();
  protected Vector<String> skipTestcases_ = new Vector<String>();
  Vector<String[]> testcaseResults = new Vector<String[]>();
  static Vector<String[]> staticTestcaseResults = new Vector<String[]>();
  // The following set of variables are passed to each testcase when running it.
  protected FileOutputStream fileOutputStream_ = null;
  protected String misc_ = null;
  protected String asp_ = null;
  // Testcase names and variations to run. Key is the testcase name, elements
  // are an array of variations to run.
  protected Hashtable<String, Vector<String>> namesAndVars_ = new Hashtable<String, Vector<String>>();
  protected String systemName_ = null;
  // RDB to use for remote access testing
  protected String rdbName_ = null;
  protected String userId_ = null;
  protected String password_ = null;
  protected char[]  encryptedPassword_ = null;
  protected static String proxy_;
  public static boolean checkPasswordLeak = false; 
  static {
    String propVal = System
        .getProperty("com.ibm.as400.access.AS400.proxyServer");
    if (propVal != null && propVal.length() != 0) {
      proxy_ = propVal;
    } else
      proxy_ = "";

    // Check for a leaked password
    String checkPasswordLeakString = System.getenv("JavaPasswordLeakCheck");
    if (checkPasswordLeakString == null) { 
      checkPasswordLeakString = System.getProperty("JavaPasswordLeakCheck"); 
    }
    if (checkPasswordLeakString != null) {
    	checkPasswordLeak = true; 
    }

  } 
  
  protected int runMode_;
  protected AS400 systemObject_ = null;
  protected String testLib_ = null;
  protected int connType_;

  protected String[] argsToSave_ = null; 
  protected PrintWriter out_;

  private Thread runner = null;
  private long start_ = 0; // Start time for a testcase.
  private long time_ = 0; // Time to run for a testcase.
  protected boolean useSSL_ = false;
  protected boolean mustUseSockets_ = false;
  protected AS400 pwrSys_ = null;
  protected static AS400 pwrSysStatic_ = null;
  public String pwrSysUserID_ = null;
  protected String pwrSysPassword_ = null;
  public char[] pwrSysEncryptedPassword_ = null;
  protected boolean noThreads_ = false;
  protected String printer_ = null;
  protected boolean serializeSystemObject_ = false;
  private static String SERIAL_FILE_NAME = "as400.ser";
  protected boolean useProfileToken_ = false;
  protected boolean useKerberos_ = false;
  protected boolean isNative_ = false;
  protected boolean isExtendedDynamic_ = false;
  protected boolean isLocal_ = false;
  protected static boolean onAS400_ = JTOpenTestEnvironment.isOS400; 
  static {
    try {
      TestDriverStatic.init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  protected String directory_ = null;
  protected String jndi_ = null;
  protected boolean cleanup_ = false;

  private boolean allTestcases_ = false;

  // Numbers for the bucket (e.g. JDConnectionTest).
  protected int totalVars_ = 0; // Total # of variations in bucket.
  protected int totalFail_ = 0; // Total # of failed variations.
  protected int totalNotAp_ = 0; // Total # of variations that don't apply.
  protected int totalSuc_ = 0; // Total # of successful variations.

  protected double totalTime_ = 0.0;

  protected boolean skipCleanup = false;

  /**
   * Constructs a TestDriver object. It is assumed that this is only called for
   * applets.
   * 
   * @exception Exception
   *              Initialization errors may cause an exception.
   **/
  public TestDriver() throws Exception {
    init(); 
  }

  /**
   * Constructs a TestDriver object. It is assumed that this is only called for
   * stand alone applications.
   * 
   * @param args
   *          The array of command line arguments.
   * @exception Exception
   *              Incorrect arguments will cause an exception.
   **/
  public TestDriver(String args[]) throws Exception {
    init(); 
    parseParms(args);
    // See if there's a serialized system object; if so, deserialize it and use
    // it.
    if (serializeSystemObject_) {
      File serFile = new File(SERIAL_FILE_NAME);
      if (serFile.exists()) {
        out_.println("Deserializing the AS400 object from file "
            + SERIAL_FILE_NAME + "; useSSL_ == " + useSSL_);
        FileInputStream in = new FileInputStream(serFile);
        ObjectInputStream s2 = new ObjectInputStream(in);
        if (useSSL_) {
          systemObject_ = (SecureAS400) s2.readObject();
        } else {
          systemObject_ = (AS400) s2.readObject();
        } /* useSSL_ */ 
        if (encryptedPassword_ != null)
          systemObject_.setPassword(PasswordVault.decryptPassword(encryptedPassword_));
        s2.close();
      } else {
        out_.println("Serialized file not found: " + SERIAL_FILE_NAME);
      }
    }
  }

  /**
   * This method is invoked when an action occurs.
   * 
   * @param event
   *          The action event.
   **/
  public void actionPerformed(ActionEvent event) {
    if (event.getActionCommand().equals("Run")) {
      // Desensitize the Run button so that the user can't start multiple
      // testcase runs simultaneously.

      // Start a new thread in which to run the testcases.
      if (runner == null) {
        runner = new Thread(this);
        try {
          runner.setPriority(Thread.MIN_PRIORITY);
        } catch (Exception e) {
          out_.println("Caught exception for setPriority:");
          e.printStackTrace(out_);
        }
      }
      runner.start();
    } else {
      out_.println("Action command not handled: " + event.getActionCommand());
    }
  }

  /**
   * Adds a testcase.
   * 
   * @param testcase
   *          The testcase object.
   **/
  public void addTestcase(Testcase testcase) {
      if (asp_ != null) {
	  testcase.setAsp(asp_); 
      } 
    String testcaseName = testcase.getName();
    testcase.setBaseTestDriver(this);
    if (allTestcases_ || namesAndVars_.containsKey(testcaseName)) {
      testcases_.addElement(testcase);
      namesAndVars_.remove(testcaseName);
    }
  }

  /**
   * Adds a skip testcase.
   * 
   * @param string
   *          testcase name
   */

  public void addSkipTestcase(String testcaseName) {
    if (allTestcases_ || namesAndVars_.containsKey(testcaseName)) {
      skipTestcases_.addElement(testcaseName);
      namesAndVars_.remove(testcaseName);
    }
  }

  /**
   * Performs cleanup needed after running testcases.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  public void cleanup() throws Exception {
  }

  /**
   * Fills in the testcases_ array.
   **/
  public abstract void createTestcases();

  /**
   * Applet destroy.
   **/
  public void destroy() {
    if (fileOutputStream_ != null) {
      try {
        fileOutputStream_.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
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
   * Return the system name before it changes into "localhost".
   **/
  public String getSystemName() {
    return systemName_;
  }


  /**
   * Performs any initialization work.
   **/
  public void init() {
    String skipCleanupString = System.getProperty("test.skipCleanup");
    if (skipCleanupString != null) { 
      skipCleanup = true; 
    } else {
      skipCleanup = false; 
    }
  }

  /**
   * Parse parameters.
   * 
   * @exception Exception
   *              Incorrect arguments will cause an exception.
   **/
  public void parseParms(String args[]) throws Exception {
    // Reset values to defaults. (Needed since several runs can be done using
    // different parameters in applet testing.)
    argsToSave_ = args; 
    allTestcases_ = false;
    runMode_ = Testcase.UNATTENDED;
    systemObject_ = null;
    useSSL_ = false;
    mustUseSockets_ = false;
    pwrSys_ = null;
    pwrSysUserID_ = null;
    pwrSysPassword_ = null;
    pwrSysEncryptedPassword_ = null;
    namesAndVars_ = new Hashtable<String, Vector<String>>();
    testcases_ = new Vector<Testcase>();
    skipTestcases_ = new Vector<String>();
    outputFileName_ = null;
    fileOutputStream_ = null;
    noThreads_ = false;
    connType_ = Testcase.CONN_DEFAULT;
    printer_ = null;
    useProfileToken_ = false;
    useKerberos_ = false;
    isNative_ = false;
    isExtendedDynamic_ = false;
    isLocal_ = false;
    // onAS400_ = false;
    TestDriverStatic.brief_ = false;
    TestDriverStatic.duration_ = 0;
    TestDriverStatic.pause_ = false;
    directory_ = null;
    jndi_ = null;
    cleanup_ = false;

    // Parse the command line parameters.
    String name = null;
    Vector<String> variations = null;
    StringTokenizer vars = null;
    int state = START;
    for (int i = 0; i < args.length; ++i) {
      // Get the next arg.
      String arg = args[i];

      if (arg.startsWith("-h") || arg.startsWith("-H")) {
        usage();
        continue;
      }

      switch (state) {
      case START:
        if (arg.equalsIgnoreCase("-system"))
          state = PARSE_SYSTEM;
        else if (arg.equalsIgnoreCase("-rdb"))
          state = PARSE_RDB;
        else if (arg.equalsIgnoreCase("-file"))
          state = PARSE_FILE;
        else if (arg.equalsIgnoreCase("-tc"))
          state = PARSE_TESTCASE;
        else if (arg.equalsIgnoreCase("-run"))
          state = PARSE_RUN_MODE;
        else if (arg.equalsIgnoreCase("-vars") || arg.equalsIgnoreCase("-var"))
          throw new Exception(
              "Syntax error.  '-vars' must be immediately preceeded by a testcase specification.");
        else if (arg.equalsIgnoreCase("-uid"))
          state = PARSE_USERID;
        else if (arg.equalsIgnoreCase("-pwd"))
          state = PARSE_PASSWORD;
        else if (arg.equalsIgnoreCase("-proxy"))
          state = PARSE_PROXY;
        else if (arg.equalsIgnoreCase("-misc"))
          state = PARSE_MISCELLANEOUS;
        else if (arg.equalsIgnoreCase("-asp"))
          state = PARSE_ASP;
        else if (arg.equalsIgnoreCase("-trace"))
          state = PARSE_TRACE_CATEGORY;
        else if (arg.equalsIgnoreCase("-ssl"))
          useSSL_ = true;
        else if (arg.equalsIgnoreCase("-socks"))
          mustUseSockets_ = true;
        else if (arg.equalsIgnoreCase("-pwrsys"))
          state = PARSE_PWR_SYS;
        else if (arg.equalsIgnoreCase("-nothreads"))
          noThreads_ = true;
        else if (arg.equalsIgnoreCase("-ctype"))
          state = PARSE_CONN_TYPE;
        else if (arg.equalsIgnoreCase("-printer"))
          state = PARSE_PRINTER;
        else if (arg.equalsIgnoreCase("-lib"))
          state = PARSE_LIB_CATEGORY;
        else if (arg.equalsIgnoreCase("-servlet"))
          TestDriverStatic.servlet_ = true;
        else if (arg.equalsIgnoreCase("-serialize"))
          serializeSystemObject_ = true;
        else if (arg.equalsIgnoreCase("-profiletoken"))
          useProfileToken_ = true;
        else if (arg.equalsIgnoreCase("-kerberos"))
          useKerberos_ = true;
        else if (arg.equalsIgnoreCase("-native"))
          isNative_ = true;
        else if (arg.equalsIgnoreCase("-extendedDynamic"))
          isExtendedDynamic_ = true;
        else if (arg.equalsIgnoreCase("-local"))
          isLocal_ = true;
        else if (arg.equalsIgnoreCase("-onas400"))
          onAS400_ = true;
        else if (arg.equalsIgnoreCase("-brief"))
          TestDriverStatic.brief_ = true;
        else if (arg.equalsIgnoreCase("-duration"))
          state = PARSE_DURATION;
        else if (arg.equalsIgnoreCase("-pause"))
          TestDriverStatic.pause_ = true;
        else if (arg.equalsIgnoreCase("-directory"))
          state = PARSE_DIRECTORY;
        else if (arg.equalsIgnoreCase("-jndi"))
          state = PARSE_JNDI;
        else if (arg.equalsIgnoreCase("-cleanup"))
          cleanup_ = true;
        else
          throw new Exception("Invalid token: " + arg);
        break;
      case PARSE_SYSTEM:
        systemName_ = arg;
        state = START;
        break;
      case PARSE_RDB:
        rdbName_ = arg;
        state = START;
        break;

      case PARSE_FILE:
        outputFileName_ = arg;
        state = START;
        break;
      case PARSE_RUN_MODE:
        if (arg.equalsIgnoreCase("u"))
          runMode_ = Testcase.UNATTENDED;
        else if (arg.equalsIgnoreCase("a"))
          runMode_ = Testcase.ATTENDED;
        else if (arg.equalsIgnoreCase("b"))
          runMode_ = Testcase.BOTH;
        else
          throw new Exception("Unknown run mode: '" + arg + "'");
        state = START;
        break;
      case PARSE_TESTCASE:
        name = args[i]; // Want mixed case.
        state = CHECK_FOR_VARIATIONS;
        break;
      case CHECK_FOR_VARIATIONS:
        if (arg.equalsIgnoreCase("-vars") || arg.equalsIgnoreCase("-var")) {
          state = PARSE_VARIATIONS;
        } else {
          // Add last testcase with no vars.
          namesAndVars_.put(name, new Vector<String>());
          state = START;
          --i; // Reparse this token.
        }
        break;
      case PARSE_VARIATIONS:
        variations = new Vector<String>();
        vars = new StringTokenizer(arg, ",");
        while (vars.hasMoreTokens()) {
          String token = vars.nextToken();
          StringTokenizer range = new StringTokenizer(token, ":");
          if (range.countTokens() == 2) {
            int start = new Integer(range.nextToken()).intValue();
            int end = new Integer(range.nextToken()).intValue();
            if (end < start)
              end = start;
            for (int num = start; num <= end; ++num) {
              variations.addElement(new Integer(num).toString());
            }
          } else {
            variations.addElement(token);
          }
        }
        namesAndVars_.put(name, variations);
        state = START;
        break;
      case PARSE_USERID:
        userId_ = arg;
        state = START;
        break;
      case PARSE_PASSWORD:
        password_ = arg;
	encryptedPassword_ = PasswordVault.getEncryptedPassword(password_); 
        state = START;
        break;
      case PARSE_PROXY:
        proxy_ = arg;
        state = START;
        break;
      case PARSE_MISCELLANEOUS:
        misc_ = args[i];
        state = START;
        break;

      case PARSE_ASP:
        asp_ = args[i];
        state = START;
        break;

      case PARSE_TRACE_CATEGORY:
        Trace.setTraceOn(true);
        StringTokenizer cats = new StringTokenizer(arg, ",");
        while (cats.hasMoreTokens()) {
          String category = cats.nextToken();
          if (category.equalsIgnoreCase("datastream"))
            Trace.setTraceDatastreamOn(true);
          else if (category.equalsIgnoreCase("diagnostic"))
            Trace.setTraceDiagnosticOn(true);
          else if (category.equalsIgnoreCase("error"))
            Trace.setTraceErrorOn(true);
          else if (category.equalsIgnoreCase("information"))
            Trace.setTraceInformationOn(true);
          else if (category.equalsIgnoreCase("jdbc")) {
            Trace.setTraceJDBCOn(true);
          } else if (category.equalsIgnoreCase("proxy"))
            Trace.setTraceProxyOn(true);
          else if (category.equalsIgnoreCase("warning"))
            Trace.setTraceWarningOn(true);
          else if (category.equalsIgnoreCase("conversion"))
            Trace.setTraceConversionOn(true);
          else if (category.equalsIgnoreCase("thread"))
            Trace.setTraceThreadOn(true);
          else if (category.equalsIgnoreCase("pcml")) {
            Trace.setTracePCMLOn(true);; 
          } else if (category.equalsIgnoreCase("all"))
            Trace.setTraceAllOn(true);
          else
            throw new Exception("Unknown trace category: '" + category + "'");
        }
        state = START;
        break;
      case PARSE_LIB_CATEGORY:
        testLib_ = arg.toUpperCase();
        state = START;
        break;
      case PARSE_PWR_SYS:
        StringTokenizer pwrSysTokenizer = new StringTokenizer(arg, ",");
        int numberOfTokens = pwrSysTokenizer.countTokens();
        if (numberOfTokens != 2) {
          throw new Exception("Wrong number of -pwrSys parameters: "
              + numberOfTokens);
        }
        pwrSysUserID_ = pwrSysTokenizer.nextToken();
        pwrSysPassword_ = pwrSysTokenizer.nextToken();
        pwrSysEncryptedPassword_ = PasswordVault.getEncryptedPassword(pwrSysPassword_); 
        state = START;
        break;
      case PARSE_CONN_TYPE:
        if (arg.equalsIgnoreCase("default"))
          connType_ = Testcase.CONN_DEFAULT;
        else if (arg.equalsIgnoreCase("pooled"))
          connType_ = Testcase.CONN_POOLED;
        else if (arg.equalsIgnoreCase("trans"))
          connType_ = Testcase.CONN_XA;
        else
          throw new Exception("Unknown connection type: '" + arg + "'");
        state = START;
        break;
      case PARSE_MAPPED_DRIVE:
        System.out.println("Warning: Mapped Drive no longer used"); 
        state = START;
        break;
      case PARSE_PRINTER:
        printer_ = arg;
        state = START;
        break;
      case PARSE_DURATION:
        try {
          TestDriverStatic.duration_ = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
          throw new Exception("Invalid duration: '" + arg + "'");
        }
        state = START;
        break;
      case PARSE_DIRECTORY:
        directory_ = arg;
        state = START;
      case PARSE_JNDI:
        jndi_ = arg;
        state = START;
        break;

      default:
        System.out
            .println("ERROR: Unanticipated state when parsing testcase names/variations: "
                + state); // should never happen
        throw new Exception("Internal error.  Unknown state.");

      }
    } // 'for' loop

    // Throw an exception if an argument specification is incomplete.
    switch (state) {
    case CHECK_FOR_VARIATIONS:
      namesAndVars_.put(name, new Vector<String>()); // Add last testcase with no vars.
      break;
    case PARSE_SYSTEM:
      throw new Exception("Incomplete system name specification.");
    case PARSE_FILE:
      throw new Exception("Incomplete file specification.");
    case PARSE_RUN_MODE:
      throw new Exception("Incomplete run mode specification.");
    case PARSE_TESTCASE:
      throw new Exception("Incomplete testcase name specification.");
    case PARSE_VARIATIONS:
      throw new Exception("Incomplete variation specification.");
    case PARSE_USERID:
      throw new Exception("Incomplete user ID specification.");
    case PARSE_PASSWORD:
      throw new Exception("Incomplete password specification.");
    case PARSE_PROXY:
      throw new Exception("Incomplete proxy specification.");
    case PARSE_TRACE_CATEGORY:
      throw new Exception("Incomplete trace category specification.");
    case PARSE_PWR_SYS:
      throw new Exception("Incomplete power system specification.");
    case PARSE_CONN_TYPE:
      throw new Exception("Incomplete connection type specification.");
    case PARSE_MAPPED_DRIVE:
      throw new Exception("Incomplete mapped drive specification.");
    case PARSE_PRINTER:
      throw new Exception("Incomplete printer specification.");
    case PARSE_DURATION:
      throw new Exception("Incomplete duration specification.");
    case PARSE_DIRECTORY:
      throw new Exception("Incomplete directory specification.");
    case PARSE_JNDI:
      throw new Exception("Incomplete jndi specification.");
    }

    // Get the default values as needed 
    StringBuffer sb = new StringBuffer(); 
    
    if (systemName_ == null) { 
      systemName_ = getDefault("system",sb);
    }
    if (userId_ == null) {
      userId_ = getDefault("uid",sb); 
    }
    if (password_ == null) { 
      /* Note: If using defaults, we will leak the password as a string.  That is ok for testcase code. */ 
      password_ = getDefault("pwd",sb); 
      encryptedPassword_ = PasswordVault.getEncryptedPassword(password_); 
    }
    if (pwrSysUserID_ == null) {
      String pwrSys = getDefault("pwrSys", sb);
      if (pwrSys != null) {
        StringTokenizer pwrSysTokenizer = new StringTokenizer(pwrSys, ",");
        int numberOfTokens = pwrSysTokenizer.countTokens();
        if (numberOfTokens != 2) {
          throw new Exception("Wrong number of -pwrSys parameters from default store: " + numberOfTokens);
        }
        pwrSysUserID_ = pwrSysTokenizer.nextToken();
        pwrSysPassword_ = pwrSysTokenizer.nextToken();
        pwrSysEncryptedPassword_ = PasswordVault.getEncryptedPassword(pwrSysPassword_);
      }

    }
    if (testLib_ == null) { 
      testLib_ = getDefault("lib", sb); 
      if (testLib_ == null) { 
        testLib_ = "JTOPENLIB";
        sb.append("-lib defaulted to "+testLib_+"\n" ); 
      }
    }

    if (directory_ == null) { 
      directory_ = getDefault("directory", sb); 
      if (directory_ == null) { 
        directory_ = "/";
        sb.append("-directory defaulted to "+directory_ +"\n"); 
      }
    }

    if (misc_ == null) { 
      misc_ = getDefault("misc", sb); 
      if (misc_ == null) { 
        misc_ = "toolbox,v7r5m0";
        sb.append("-misc defaulted to "+misc_ +"\n"); 
      }
    }

    // Instantiate the system object.
    if (useSSL_ == true) {
      systemObject_ = new SecureAS400();
      pwrSys_ = new SecureAS400();
    } else {
      systemObject_ = new AS400();
      pwrSys_ = new AS400();
    }
    if (systemName_ != null) {
	if (systemName_.equals("DB2M_LCL")) {
	    systemName_ = "localhost"; 
	} 
      systemObject_.setSystemName(systemName_);
      pwrSys_.setSystemName(systemName_);
    }
    if (userId_ != null) {
      systemObject_.setUserId(userId_);
    }
    if (pwrSysUserID_ != null) {
      pwrSys_.setUserId(pwrSysUserID_);
    }
    if (password_ != null) {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_); 
      systemObject_.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword); 

    }
    if (pwrSysPassword_ != null) {
      char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_); 
      pwrSys_.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword); 
    }
    if (!proxy_.equals("")) {
      systemObject_.setProxyServer(proxy_);
      pwrSys_.setProxyServer(proxy_);
    }

    // Generate profile token if asked.
    if (useProfileToken_) {
      systemObject_.setProfileToken(systemObject_.getProfileToken(
          ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE, 3600));
      pwrSys_.setProfileToken(pwrSys_.getProfileToken(
          ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE, 3600));
    }

    if (mustUseSockets_) {
      systemObject_.setMustUseSockets(true);
      pwrSys_.setMustUseSockets(true);
    }
    if (noThreads_) {
      systemObject_.setThreadUsed(false);
      pwrSys_.setThreadUsed(false);
    }

    // Prepare the output file for access.
    if (outputFileName_ != null) {
      try {
        fileOutputStream_ = new FileOutputStream(outputFileName_);
      } catch (Exception e) {
      }
      if (Trace.isTraceOn()) {
        Trace.setFileName(outputFileName_);
      }
    }
    if (fileOutputStream_ != null) {
      out_ = new PrintWriter(new TestOutput(fileOutputStream_), true);
    } else {
      out_ = new PrintWriter(System.out, true);
    }
    if (sb.length()>0) { 
      out_.println(sb.toString()); 
    }
    allTestcases_ = (namesAndVars_.size() == 0);
    pwrSysStatic_ = pwrSys_; 
    // Fill in the testcase_ array.
    createTestcases();

    // Report invalid testcase names.
    for (Enumeration<String> e = namesAndVars_.keys(); e.hasMoreElements();) {
      out_.println("TestDriver:  Testcase " + e.nextElement() + " not found.");
    }

    // Double check the setting of -system, -uid, -password
    boolean exitError =false; 
    if (systemName_ == null) { 
    	exitError = true; 
    	System.out.println("Error: -system not set");
    }
    
    if (userId_ == null) { 
    	exitError = true; 
    	System.out.println("Error: -uid not set"); 
    }
    if (password_ == null) {
    	exitError = true; 
    	System.out.println("Error:  -pwd not set"); 
    }
    if (exitError)  {
    	usage(); 
    }
  }

  /** 
   * Get a default setting. 
   * The default settings are first obtained from JTOPEN_setting environment variable.  
   * Next ini/defaults.ini file is check for JTOPEN_setting
   */ 
  static boolean iniDefaultsChecked = false; 
  static Properties iniDefaults = null; 
  private static String getDefault(String setting, StringBuffer sb) {
    String property = "JTOPEN_"+setting; 
    String envvar = System.getenv(property); 
    if (envvar != null) { 
      sb.append("Setting: "+setting+" retrieved from envvar "+property+"\n");
      return envvar; 
    }
    if (!iniDefaultsChecked) { 
      iniDefaultsChecked = true; 
      try {
        FileInputStream input = new FileInputStream("ini/defaults.ini");
        iniDefaults = new Properties();
        iniDefaults.load(input); 
      } catch (FileNotFoundException e) {
        iniDefaults = null; 
        sb.append("Warning:  ini/defaults.ini not found in "+System.getProperty("user.dir")+"\n"); 
      } catch (IOException e) {
        iniDefaults = null; 
        sb.append("Warning: exception -- "+e.toString()+" -- loading ini/defaults.ini in "+System.getProperty("user.dir")+"\n");
      } 
    }
    if (iniDefaults != null) { 
      String value = iniDefaults.getProperty(property); 
      if (value != null) { 
        sb.append("Setting: "+setting+" retrieved from property "+property+" in ini/defaults.ini\n");
        return value; 
      }
      sb.append("WARNING: Not able to read "+property+" as envvar and from ini/defaults.ini in "+System.getProperty("user.dir")+"\n");
    } else {
       sb.append("WARNING: Not able to read "+property+" as envvar and ini/defaults.ini does not exist\n");
    }
    return null;
  }

  /**
   * Run the tests.
   **/
  public void run() {
    String loggingText = "*SECLVL";

    // If applet, get parms from the text box.

    // If on a 400, print the testcase information in the job log
    // Save the current logging level
    // change the job so it will log
    if (onAS400_) {
      String thisClass = this.getClass().getName();
      String argString = obfuscatePasswords(argsToSave_);
      Date d = new Date();
      JDJobName.sendProgramMessage(d.toString() + " Running " + thisClass + " "
          + argString);
      loggingText = JDJobName.getLoggingText();
      if (!"*SECLVL".equals(loggingText)) {
        String command = " CHGJOB LOG(4 00 *SECLVL)  ";
        try {
          JDJobName.system(command);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      out_.println("Job is " + JDJobName.getJobName());

      String outputFile = System.getProperty("test.outputFile");
      if (outputFile != null) {
        JDJobName.sendProgramMessage("Temporary output file is " + outputFile);
      }
      String saveFile = System.getProperty("test.saveFile");
      if (saveFile != null) {
        JDJobName.sendProgramMessage("On success, final file is " + saveFile);
      }

      String parentJob = System.getProperty("test.parentJob");
      if (parentJob != null) {
        JDJobName.sendProgramMessage("Parent job is  " + parentJob);
      }

    }

    // Perform test driver setup.
    try {
      setup();
    } catch (Exception e) {
      out_.println("Test driver setup error:");
      e.printStackTrace(out_);
    }

    // Run each testcase.
    try {
      for (Enumeration<Testcase> e = testcases_.elements(); e.hasMoreElements();) {
        Testcase tc = (Testcase) e.nextElement();
        start_ = System.currentTimeMillis();
        tc.run();
        time_ = System.currentTimeMillis() - start_;
        tc.setTimeToRun((double) time_ / 1000);
        if (timeoutThread != null)
          timeoutThread.reset();
      }
    } catch (Exception e) {
      out_.println("Test driver error while running testcases:");
      e.printStackTrace(out_);
    }

    // Perform test driver cleanup.
    try {
      if (!skipCleanup) { 
         cleanup();
      } else {
        out_.println("test.skipCleanup set so TestDriver skipping cleanup"); 
      }

      // See if the user wants us to serialize the system object.
      if (serializeSystemObject_) {
        out_.println("Serializing the system object to file "
            + SERIAL_FILE_NAME);
        File file = new File(SERIAL_FILE_NAME);
        if (file.exists())
          file.delete();
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutput s = new ObjectOutputStream(f);
        s.writeObject(systemObject_);
        s.flush();
        s.close();
      }
    } catch (Exception e) {
      out_.println("Test driver cleanup error:");
      e.printStackTrace(out_);
    }

    // Output status for each testcase.
    for (Enumeration<Testcase> e = testcases_.elements(); e.hasMoreElements();) {
      Testcase tc = (Testcase) e.nextElement();
      tc.outputResults();
      // Also add the number from each testcase (e.g JDConnectionClose) to the
      // numbers for the bucket (e.g. JDConnectionTest).
      totalTime_ += tc.getTimeToRun();
      totalVars_ += tc.getTotalVariations();
      totalSuc_ += tc.getSucceeded();
      totalFail_ += tc.getFailed();
      totalNotAp_ += tc.getNotApplicable();

      // Also save the results
      String[] results = new String[6];
      results[0] = tc.getName();
      results[1] = "" + tc.getSucceeded();
      results[2] = "" + tc.getFailed();
      results[3] = "" + tc.getNotApplicable();
      results[4] = "" + tc.getTimeToRun();

      testcaseResults.add(results);

    }

    if (checkPasswordLeak) {
      int leakCount = 0; 
      int leakFreeCount = 0; 
      String filename = "/tmp/passwordLeakCoreDump8.bin";
      long startMillis = System.currentTimeMillis(); 
      PasswordLeakTool.forceDump(filename);
      String [] passwords ;
      String password = new String(PasswordVault.decryptPassword(encryptedPassword_));
      String pwrPassword = new String(PasswordVault.decryptPassword(pwrSysEncryptedPassword_));
      if (password.equals(pwrPassword)) {
        passwords = new String[1]; 
        passwords[0]=password;
      } else {
        passwords = new String[2]; 
        passwords[0]=password;
        passwords[1]=pwrPassword;
      }
      String leakInfo = PasswordLeakTool.scanDumpForPasswords(filename, passwords );
      if (leakInfo == null) { 
        leakFreeCount++; 
        System.out.println("NO PASSWORD LEAK DETECTED"); 
      } else {
        leakCount++; 
        System.out.println("PASSWORD LEAK DETECTED "+leakInfo); 
      }


      long leakCheckMillis = System.currentTimeMillis() - startMillis; 
            // Also save the results
      String[] results = new String[6];
      results[0] = "PASSWORD_LEAK_CHECK";
      results[1] = "" + leakFreeCount;
      results[2] = "" + leakCount;
      results[3] = "0" ;
      results[4] = "" + (leakCheckMillis / 1000.0);

      totalVars_ += leakFreeCount+leakCount;
      totalSuc_ += leakFreeCount;
      totalFail_ += leakCount;

      testcaseResults.add(results);

      
    } 

    // Record the skipped testcases

    for (Enumeration<String> e = skipTestcases_.elements(); e.hasMoreElements();) {
      String tc = (String) e.nextElement();

      totalTime_ += 0;
      totalVars_ += 1;
      totalSuc_ += 0;
      totalFail_ += 0;
      totalNotAp_ += 1;

      System.out.println("TestDriver recording: " + tc);
      StringBuffer sb = new StringBuffer(tc);
      while (sb.length() < 44) {
        sb.append(" ");
      }
      sb.append(" 0         0        1        0      0.1");
      System.out
          .println("_____________________________________________________________________________________");
      System.out
          .println("NAME                                    SUCCEEDED  FAILED  NOT APPL  NOT ATT  TIME(S)");

      System.out.println(sb.toString());
      System.out
          .println("_____________________________________________________________________________________");

      // Also save the results
      String[] results = new String[6];
      results[0] = tc;
      results[1] = "0";
      results[2] = "0";
      results[3] = "1"; /* Not applicable */
      results[4] = "0";

      testcaseResults.add(results);

    }

    outputSummary();

    // If on a 400, change the logging level back
    if (onAS400_) {
      if (!"*SECLVL".equals(loggingText)) {
        String command = " CHGJOB LOG(4 00 " + loggingText + ")  ";
        try {
          JDJobName.system(command);
        } catch (Exception e) {
          e.printStackTrace();
        }

      }

    }

    // Error message if no testcases to run.
    if (!(testcases_.elements().hasMoreElements())) {
      out_.println("No testcases to run!");
    } else {
    }

  }

  /**
   * Run the test as an application. This should be called from the test
   * driver's main().
   * 
   * @param testDriver
   *          A test driver object.
   **/
  public static void runApplication(TestDriver testDriver) {

    try {
      String timeout = System.getProperty("timeout");
      if (timeout != null) {
        try {
          long longTimeout = Long.parseLong(timeout);
          if (longTimeout > 0) {
            System.out.println("Starting timeout thread for " + longTimeout
                + " ms ");
            timeoutThread = new TestDriverTimeoutThread(longTimeout);
            timeoutThread.start();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
      /* CHANGES TO USE PEX... on Front end */
      String pex = System.getProperty("pex");
      if (pex != null) {
        System.out.println("Starting PEX " + JDJobName.startPex());
      }

      testDriver.init();
      testDriver.start();
      testDriver.stop();
      staticTestcaseResults = testDriver.getTestcaseResults();
      if (pex != null) {
        System.out.println("Ending PEX " + JDJobName.endPex());
      }

      testDriver.destroy();
    } catch (Exception e) {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    } finally {

      if (timeoutThread != null) {
        timeoutThread.markDone();
      }

    }

    if (!TestDriverStatic.servlet_ && !TestDriverStatic.systemExitDisabled_) {
      TestDriverStatic.restoreSecurityManager();
      System.exit(0);
    }
  }

  /**
   * Set a flag that tells the runApplication method above whether or not to
   * call System.exit().
   **/
  public static void setSystemExitDisabled(boolean systemExit) {
    TestDriverStatic.setSystemExitDisabled(systemExit);
  }

  /**
   * Performs setup needed before running testcases.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  public void setup() throws Exception {
  }

  /**
   * Runs all (all that were specified) testcases in this test program.
   **/
  public void start() {
    // If we are an applet, wait for user to press button to run the tests.
    // If we are an application, can run immediately.
    run();

  }

	public static String[] usageInfo = { 
			"The following options may be provided\n",
			"  -system    - The IBM i server with which to connect.",
			"  -uid       - The user profile name to use to authenticate to the server.",
			"  -pwd       - The user profile password to use to authenticate to the server.",
			"  -lib       - The name of the test library to create files, etc. in.  This option is used (initially) by Record-level Access.  This field can now be specified by the user as the place to put the tables/procs etc.",
			"  -file      - The file in which to write results.  Results are always written to standard out, if this tag is specified, results are also written to this file.",
			"  -run       - The run mode.  Values are 'u' for unattended variations only, 'a' for attended variations only, or 'b' for both.  The default value is 'u'.",
			"  -proxy     - The proxy server system name with which to connect.",
			"  -tc        - Testcase to be run.  If not specififed, all testcases will be run.  This token can be specified several times.",
			"  -vars      - Variations to be run.  Several values can be specified by separating each value with a comma.  This token must be specified immediately after a -tc token, it applies to the testcase which it follows.  If not specified, all variations will be run.",
			"  -misc      - Miscellaneous information that is passed to all testcases.",
			"  -trace     - Categories to trace.  Multiple categories can be specified by separating each value with a comma.  The categories are datastream, diagnostic, error, information, jdbc, warning, conversion, pcml.  Category names are not case sensitive.",
			"  -ssl       - Connect using secure sockets connections.",
			"  -socks     - Set the system object's mustUseSockets property to true.",
			"  -pwrSys    - A userID, password combination with SECOFR authority.",
			"  -nothreads - Set the system object's isThreadUsed property to false.",
			"  -printer   - The name of the printer to use for the NPPrint testcases.",
			"  -profileToken - Generate and use a profile token for authentication.",
			"  -serialize - Indicates that the AS400 object is to be deserialized/serialized.  A file named \"as400.ser\" is sought and, if found, deserialized into systemObject_.  Upon testcase completion, the file is (re)generated.",
			"  -kerberos  - Use Kerberos for authentication.",
			"  -native    - The native optimizations classes are available.",
			"  -local     - The target system is the same system we are running on.",
			"  -onAS400   - The client is an iSeries server.", "  -brief     - Skip long-running variations.",
			"  -duration  - Number of seconds to run testcase.",
			"  -pause     - Waits for the user to press enter before running the variations to obtain the jobs running on the server, depending on the class type.",
			"  -directory - The directory where some test driver variations point to.",
			"  -jndi      - The type of jndi used for some jdbc tests.",
			"  -cleanup   - NLSTests can have cleanup, this flags indicates if it is needed.",
			"  -asp       - Indicates the ASP to use for the test",
			"  -extendedDynamic - Indicates that extended dynamic should be used for the toolbox driver", " Examples:",
			"    java ExampleDriver", "    java ExampleDriver -system rchas57a",
			"    java ExampleDriver -system rchas57a -file example.out -run B -uid tester -pwd secret -tc testcase1",
			"    java ExampleDriver -system rchas57a -file example.out -run B -tc testcase1 -vars 1,2,3  -tc testcase2 -vars 3,9 -tc testcase3",
			"    java ExampleDriver -system rchas57a -tc testcase1 -vars 3,12:25     // runs variations 3 and 12 thru 25.",

	};

	  /**
   * Display usage information for the Testcase. This method never returns.
   **/
  public void usage() {
    System.out
        .println("For usage guidance, refer to the class-level javadocs for the TestDriver class.");
    System.out.println();
    for (int i = 0; i < usageInfo.length; i++) { 
    	System.out.println(usageInfo[i]);
    }
    System.exit(1); 
  }

  /**
   * Output results summary for the test bucket.
   **/
  public void outputSummary() {
    String serverVRM = null;
    try {
      serverVRM = "V" + systemObject_.getVersion() + "R"
          + systemObject_.getRelease() + "M" + systemObject_.getModification();
    } catch (Exception e) {
      serverVRM = "";
    }

    StringBuffer heading1 = new StringBuffer();
    for (int i = 0; i < 50; i++) {
      heading1.append('_');
    }

    // Start of changes for @A1A
    out_.println(heading1);
    out_.println("JAVA VERSION     = " + System.getProperty("java.version"));
    out_.println("JAVA VENDOR      = " + System.getProperty("java.vm.vendor"));
    out_.println("JAVA HOME        = " + System.getProperty("java.home"));
    // out_.println("JAVA CLASS PATH  = " +
    // System.getProperty("java.class.path"));
    String clientMachineName = null;
    try {
      clientMachineName = JDHostName.getHostName();
    } catch (Exception e) {
    }
    out_.println("CLIENT NAME      = " + clientMachineName);
    out_.println("CLIENT OS/VERSION= " + JTOpenTestEnvironment.osVersion); 
    out_.println("CLIENT USER NAME = " + System.getProperty("user.name"));
    String sysName = systemName_;
    if (sysName != null && sysName.equalsIgnoreCase("localhost")) {
      sysName = sysName + " (" + clientMachineName + ")"; // running on i
    }
    out_.println("SERVER NAME/VRM  = " + sysName + " " + serverVRM);
    if (rdbName_ != null) {
      out_.println("RDB NAME         = " + rdbName_);
    }
    out_.println("TOOLBOX VERSION  = " + getToolboxSpecificationVersion()
        + " (" + getToolboxImplementationVersion() + ")");

    // Get info about the Toolbox jar (jt400.jar or jt400Native.jar).
    File toolboxJar = getLoadSource("com.ibm.as400.access.AS400");
    if (toolboxJar != null) {
      out_.println("TOOLBOX JAR LOCATION = " + toolboxJar.getAbsolutePath());
      // Date jarDate = new Date(toolboxJar.lastModified());
      // out_.println("TOOLBOX JAR TIMESTAMP= " +
      // timeStampFormatter_.format(jarDate));
      try {
        JarFile jarFile = new JarFile(toolboxJar, false);
        JarEntry jarEntry = jarFile
            .getJarEntry("com/ibm/as400/access/AS400.class");
        Date classDate = new Date(jarEntry.getTime());
        out_.println("TOOLBOX BUILD DATE   = "
            + TestDriverStatic.timeStampFormatter_.format(classDate));
        jarFile.close(); 
      } catch (Exception e) {
      }
    }

    try {
      Class<?> driver = Class.forName("com.ibm.as400.access.AS400JDBCDriver");
      Field majorVersion = driver.getDeclaredField("JDBC_MAJOR_VERSION_");
      Field minorVersion = driver.getDeclaredField("JDBC_MINOR_VERSION_");
      out_.println("JDBC VERSION         = " + majorVersion.getInt(null) + "."
          + minorVersion.getInt(null));
    } catch (Exception e) {
      // We're running JTOpen 6.1 or earlier, so JDBC 4.0 is not supported yet.
    }

    // Get info about the jt400Test.jar.
    File testJar = getLoadSource("test.Testcase");
    if (testJar != null) {
      out_.println("TEST JAR LOCATION    = " + testJar.getAbsolutePath());
      // Date jarDate = new Date(testJar.lastModified());
      // out_.println("TEST JAR TIMESTAMP   = " +
      // timeStampFormatter_.format(jarDate));
      try {
        JarFile jarFile = new JarFile(testJar, false);
        JarEntry jarEntry = jarFile.getJarEntry("test/Testcase.class");
        Date classDate = new Date(jarEntry.getTime());
        out_.println("TEST JAR BUILD DATE  = "
            + TestDriverStatic.timeStampFormatter_.format(classDate));
        jarFile.close(); 
      } catch (Exception e) {
      }
    }
    String argString = arrayToString(argsToSave_); // don't obfuscate for now

    if (argString.indexOf("native,") > 0) {
      // Get info about the Native jar (jt400.jar or jt400Native.jar).
      File nativeJar = getLoadSource("com.ibm.db2.jdbc.app.DB2Driver");
      if (nativeJar != null) {
        out_.println("NATIVE JAR LOCATION = " + nativeJar.getAbsolutePath());
        try {
          if (nativeJar.isDirectory()) {
            File classFile = new File(nativeJar.getAbsolutePath()
                + "/com/ibm/db2/jdbc/app/DB2Driver.class");
            Date classDate = new Date(classFile.lastModified());
            out_.println("NATIVE BUILD DATE   = "
                + TestDriverStatic.timeStampFormatter_.format(classDate));

          } else {
            JarFile jarFile = new JarFile(nativeJar, false);
            JarEntry jarEntry = jarFile
                .getJarEntry("com/ibm/db2/jdbc/app/DB2Driver.class");
            Date classDate = new Date(jarEntry.getTime());
            out_.println("NATIVE BUILD DATE   = "
                + TestDriverStatic.timeStampFormatter_.format(classDate));
            jarFile.close(); 
          }
        } catch (Exception e) {

        }
      } else {
        out_.println("NATIVE JAR LOCATION = null");
      }

      try {
        Class<?> driver = Class.forName("com.ibm.db2.jdbc.app.DB2Driver");
        Field releaseString = driver.getDeclaredField("RELEASE_STRING");
        out_.println("NATIVE JDBC VERSION = " + releaseString.get(null));
      } catch (Exception e) {
        // We're running JTOpen 6.1 or earlier, so JDBC 4.0 is not supported
        // yet.
        out_.println("NATIVE JDBC VERSION = Exception ( " + e.toString() + " ");
      }

    }

    String thisClass = this.getClass().getName();
    // Added back obfuscation 01/16/2013
    argString = obfuscatePasswords(argsToSave_);
    out_.println("TC DRIVER        = " + thisClass);
    out_.println("TC ARGS          = " + argString);
    // out_.println("INVOCATION       = java " + thisClass + " " + argString);
    // // we don't know how to get the JVM directives, e.g. "-mx1024m"
    out_.println("");
    // End of changes for @A1A

    out_.println("Summary for the bucket");
    int hours = (int) (totalTime_ / 3600);
    int minutes = (int) ((totalTime_ / 60) - (hours * 60));
    int seconds = (int) (totalTime_ - (hours * 3600) - (minutes * 60));
    out_.println("END DATE/TIME    = " + (new Date()).toString()); 
    String timeStr = (hours < 10 ? "0" + hours : "" + hours) + ":"
        + (minutes < 10 ? "0" + minutes : "" + minutes) + ":"
        + (seconds < 10 ? "0" + seconds : "" + seconds) + " sec";
    out_.println("TOTAL TIME       = " + timeStr);
    out_.println("TOTAL VARIATIONS = " + totalVars_);
    out_.println("SUCCESS          = " + totalSuc_);
    out_.println("FAILURE          = " + totalFail_);
    out_.println("NOT APPLICABLE   = " + totalNotAp_);
    int totalNotAtt_ = totalVars_ - totalSuc_ - totalFail_ - totalNotAp_;
    out_.println("NOT ATTEMPTED    = " + totalNotAtt_);
    out_.println(heading1);
    if (totalFail_ == 0) {
    	out_.println("NO FAILURES DETECTED DURING THIS RUN"); 
    }
  }

  static String arrayToString(String[] strings) {
    if (strings == null)
      return "null";
    if (strings.length == 0)
      return "";
    StringBuffer buf = new StringBuffer(strings[0]);
    for (int i = 1; i < strings.length; i++) {
      buf.append(" " + strings[i]);
    }
    return buf.toString();
  }

  // Replace passwords by "xxxxxxxx".
  static String obfuscatePasswords(String args[]) {
    boolean priorArgWasPwdOption = false;
    boolean priorArgWasPwrSysOption = false;
    StringBuffer newArgs = new StringBuffer();
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      if (priorArgWasPwdOption) {
        arg = "xxxxxxxx";
        priorArgWasPwdOption = false;
      } else if (priorArgWasPwrSysOption) {
        int commaPos = arg.indexOf(',');
        if (commaPos != -1) {
          arg = arg.substring(0, commaPos + 1) + "xxxxxxxx";
        }
        priorArgWasPwrSysOption = false;
      } else {
        if (arg.equalsIgnoreCase("-pwd")) {
          priorArgWasPwdOption = true;
        } else if (arg.equalsIgnoreCase("-pwrSys")) {
          priorArgWasPwrSysOption = true;
        }
      }
      newArgs.append(arg);
      newArgs.append(' ');
    }
    return newArgs.toString();
  }

  public static String deleteLibrary(CommandCall cmd, String library) {
    // Make sure the sysrpyl is used.
    try {
      cmd.run("CHGJOB INQMSGRPY(*SYSRPYL)");
      boolean result = cmd.run("DLTLIB " + library);
      if (result == false) {
        return cmd.getMessageList()[0].getID();
      } else {
        return null;
      }

    } catch (Exception e) {
      e.printStackTrace(System.out);
      return e.toString();
    }

  }

  public static File getLoadSource(String className) {
    if (className == null)
      throw new NullPointerException("className");
    File loadedFromFile = null;
    try {
      ClassLoader loader = null;
      try {
        loader = Class.forName(className).getClassLoader();
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (loader == null) {
        loader = ClassLoader.getSystemClassLoader();
      }
      if (loader != null) {
        String fileName = className.replace('.', '/') + ".class";
        URL jarUrl = loader.getResource(fileName);
        if (jarUrl != null) {
          String classLoadedFromPath = jarUrl.getPath();
          if (classLoadedFromPath != null) {
            String jarDirPath = classLoadedFromPath.substring(0,
                classLoadedFromPath.length() - fileName.length()); // strip
                                                                   // filename
                                                                   // from end
                                                                   // of path
            if (jarDirPath.startsWith("file:")) {
              jarDirPath = jarDirPath.substring(5); // strip "file:" prefix
            }
            if (jarDirPath.endsWith("/") || jarDirPath.endsWith("\\")) {
              jarDirPath = jarDirPath.substring(0, jarDirPath.length() - 1); // strip
                                                                             // final
                                                                             // char
            }
            if (jarDirPath.endsWith("!") || jarDirPath.endsWith("|")) {
              jarDirPath = jarDirPath.substring(0, jarDirPath.length() - 1); // strip
                                                                             // final
                                                                             // char
            }
            if (jarDirPath.length() != 0) {
              loadedFromFile = new File(jarDirPath);
            }
          } else {
            System.out
                .println("...Warning..getLoadSource():  classLoadedFromPath is null");
          }
	} else {
          System.out.println("...Warning..getLoadSource():  jarUrl is null");
        }
      } else {
        System.out.println("...Warning..getLoadSource():  loader is null");
      }
    } catch (Exception e) {
      System.out.println("Exception determining jar source");
      e.printStackTrace(System.out);
    }
    return loadedFromFile;
  }

  /**
   * Returns the results for the bucket in an integer array. Used by
   * JDBCNativeTest for counting the results from the entire CRT.
   **/
  public int[] getResults() {
    int[] results = new int[4];
    results[0] = totalVars_;
    results[1] = totalSuc_;
    results[2] = totalFail_;
    results[3] = totalNotAp_;
    return results;
  }

  /**
   * Utility method to run commands under -pwrSys authority.
   * 
   * @param commmand
   *          Command to run.
   * @return true if command was successful; false otherwise.
   **/

  public boolean cmdRun(String command) {
    return cmdRun(command, pwrSys_, out_, null);
  }

  public boolean cmdRun(String command, String expectedMessage) {
    return cmdRun(command, pwrSys_, out_, expectedMessage);
  }

  public String deleteLibrary(String library) {
    CommandCall cmd = new CommandCall(pwrSys_);
    return deleteLibrary(cmd, library);
  }

  public static boolean cmdRun(String command, AS400 system, PrintWriter out,
      String expectedMessage) {
    try {
      CommandCall cmd = new CommandCall(system);
      boolean ret = cmd.run(command);
      if (!ret) {
        AS400Message[] msg = cmd.getMessageList();
        if (expectedMessage != null && msg.length == 1
            && expectedMessage.equals(msg[0].getID())) {
          return true;
        }
        out.println("Error running command: " + command);
        for (int i = 0; i < msg.length; ++i) {
          out.println(msg[i].getID() + " " + msg[i].getText());
        }
      }
      return ret;
    } catch (Throwable e) {
      out.println("Exception running command: " + command);
      e.printStackTrace(out);
    }
    return false;
  }

  public static AS400 getPwrSys() {
    return pwrSysStatic_;
  }

  public boolean isExtendedDynamic() {
    return isExtendedDynamic_;
  }

  public Vector<String[]> getTestcaseResults() {
    return testcaseResults;
  }

  /**
   * 
   * @return A vector containing the results. Each result is a String[] with
   *         0-Name, 1-success, 2-failed, 3-notappli, 4-time.
   */
  static public Vector<String[]> getStaticTestcaseResults() {
    return staticTestcaseResults;
  }

  /**
   * @C1A Returns the AS/400 library which is set as the default for the testcase
   *      run.
   * @return The AS/400 library.
   **/
  public String getTestLib() {
    return testLib_;
  }

  public void stop() {

  }


  /**
   * Return the rdbname -- null means RDB was not set
   **/
  public String getRdbName() {
    return rdbName_;
  }



}
