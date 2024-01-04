///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDTestDriver.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;


import java.util.Hashtable;
import java.util.Properties;
import java.util.Calendar;
import java.util.Locale;

//B3A (all imports below)

import java.sql.SQLException;
// Temporarily added for Native RMI work...
import java.sql.Driver;

/**
 * The JDTestDriver class is a superclass for all JDBC test drivers.
 **/
public abstract class JDTestDriver extends TestDriver {
  /**
   *
   */
  static final long serialVersionUID = 1L;

  public static String DEFAULT_COLLECTION = "JDTESTDRVR";

  static String EXTENDED_DYNAMIC_OPTIONS_MINUS_LIBRARY = ";extended dynamic=true;package add=true;package criteria=select;package=JDTEST;package library=";

  // This field and contants can be used to verify which driver
  // is being tested.
  public static final int DRIVER_NONE = 0;
  public static final int DRIVER_TOOLBOX = 1;
  public static final int DRIVER_NATIVE = 2;
  public static final int DRIVER_NATIVE_RMI = 3;
  public static final int DRIVER_JCC = 4;
  public static final int DRIVER_JTOPENLITE = 5;
  public static final int DRIVER_COUNT = 6;

  public static final int SUBDRIVER_JT400ANDROID = 6;
  public static final int SUBDRIVER_JTOPENCA = 7;
  public static final int SUBDRIVER_JTOPENSF = 8;
  public static final int SUBDRIVER_JTOPENPX = 9;

  public static final int SUB_DRIVER_COUNT = 4;

  public static final String DRIVER_STRING_TOOLBOX = "toolbox";
  public static final String DRIVER_STRING_NATIVE = "native";
  public static final String DRIVER_STRING_JCC = "jcc";
  public static final String DRIVER_STRING_JTOPENLITE = "jtopenlite";
  public static final String DRIVER_STRING_JT400ANDROID = "jt400android";
  public static final String DRIVER_STRING_JTOPENCA = "jtopenCA";
  public static final String DRIVER_STRING_JTOPENSF = "jtopenSF";
  public static final String DRIVER_STRING_JTOPENPX = "jtopenPX";
  // This field and constants can be used to determine which
  // db is being tested.
  // For now, this is taken from a Java property databaseType
  //
  public static final int DB_SYSTEMI = 0;
  public static final int DB_LUW = 1;
  public static final int DB_ZOS = 2;

  // This field and contants can be used to verify which release
  // is being tested.
  public static final int RELEASE_NONE = 0;
  public static final int RELEASE_V4R2M0 = 420;
  public static final int RELEASE_V4R3M0 = 430;
  public static final int RELEASE_V4R4M0 = 440;
  public static final int RELEASE_V4R5M0 = 450; 
  public static final int RELEASE_V5R1M0 = 510; 
  public static final int RELEASE_V5R2M0 = 520; 
  public static final int RELEASE_V5R3M0 = 530;
  public static final int RELEASE_V5R4M0 = 540;
  public static final int RELEASE_V5R5M0 = 550;
  public static final int RELEASE_V6R1M0 = 610;
  public static final int RELEASE_V7R1M0 = 710;
  public static final int RELEASE_V7R2M0 = 720;
  public static final int RELEASE_V7R3M0 = 730;
  public static final int RELEASE_V7R4M0 = 740;
  public static final int RELEASE_V7R5M0 = 750;
  public static final int RELEASE_V7R6M0 = 760;

  public static final String CLIENT_windows = "windows";
  public static final String CLIENT_as400 = "as400";
  public static final String CLIENT_aix = "aix";
  public static final String CLIENT_linux = "linux";

  String jccStripProperties[] = { "lob threshold", "data truncation",
      "date format", "time format", "errors", "maximum precision",
      "maximum scale", "date separator", "extended dynamic", };

  
  // This flag is set to indicate that "secondary URL" should be used
  // when composing the base URL. Note that this only makes sense when
  // proxy server is in use.
  public static boolean useSecondaryUrl_ = false;

  public static String OSName_ = System.getProperty("os.name");

  // For LUW this is not 466
  public static int jccPort = 446;

  public static boolean isLUW() {
    return jccPort != 446;
  }

  public static String jccDatabase = "*LOCAL";

  public static boolean typePrinted = false;

  public static int getDatabaseTypeStatic() {
    int db;
    String dbString = System.getProperty("databaseType");
    if (dbString != null) {
      if (dbString.equals("ZOS")) {
        db = DB_ZOS;
        if (!typePrinted) {
          typePrinted = true;
          System.out.println("JDTestDriver:Database type = ZOS");
        }
      } else if (dbString.equals("LUW")) {
        db = DB_LUW;
        if (!typePrinted) {
          typePrinted = true;
          System.out.println("JDTestDriver:Database type = LUW");
        }
      } else if (dbString.equals("I")) {
        db = DB_SYSTEMI;
        if (!typePrinted) {
          typePrinted = true;
          System.out.println("JDTestDriver:Database type = I");
        }
      } else {
        if (!typePrinted) {
          typePrinted = true;
          System.out.println("JDTestDriver:Database type " + dbString
              + " for databaseType property not recognized");
        }
        db = DB_SYSTEMI;
      }
    } else {
      db = DB_SYSTEMI;
    }
    return db;
  }

  // Private constants.
  private static final String DRIVER_CLASS_TOOLBOX = "com.ibm.as400.access.AS400JDBCDriver";
  private static final String DRIVER_CLASS_NATIVE = "com.ibm.db2.jdbc.app.DB2Driver";
  private static final String DRIVER_CLASS_NATIVE_RMI = "com.ibm.db2.jdbc.app.DB2RMIDriverClient";
  private static final String DRIVER_CLASS_JCC = "com.ibm.db2.jcc.DB2Driver";
  private static final String DRIVER_CLASS_JTOPENLITE = "com.ibm.jtopenlite.database.jdbc.JDBCDriver";

  // Private data.
  private boolean largeDecimalPrecisionSupport_;
  private boolean bigintSupport_; 
  private boolean decfloatSupport_; 
  private boolean xmlSupport_; 
  private boolean arraySupport_; 
  private boolean timestamp12Support_; 
  private boolean booleanSupport_; 
  private int driver_ = DRIVER_NONE;
  private int subDriver_ = DRIVER_NONE;
  private int db_ = DB_SYSTEMI;
  static Class driverManager_ = null;
  private boolean lobSupport_;
  private boolean datalinkSupport_;
  private boolean returnValueSupport_; 
  private boolean savepointSupport_; 
  private boolean namedParameterSupport_; 
  private boolean cursorHoldabilitySupport_; 
  private boolean multipleOpenResultSetSupport_; 
  private boolean generatedKeySupport_; 
  private boolean updateableLobsSupport_; 
  private int release_ = RELEASE_NONE;

  private boolean defSchemaSet = false;
  private String collection_ = null; 
  private boolean driverSetupDone = false; 
  private AS400JDBCDriver as400JdbcDriver_ = new AS400JDBCDriver(); 

  /**
   * Static initializer.
   **/
  static {
    // There are occasions where the DriverManager class
    // itself is getting garbage collected and we are losing
    // our list of JDBC drivers. We will keep a reference
    // to it to ensure that this does not heppen.
    driverManager_ = DriverManager.class;

    String jccPortString = System.getProperty("jccPort");
    if (jccPortString != null) {
      jccPort = Integer.parseInt(jccPortString);
    }
    jccDatabase = System.getProperty("jccDatabase");

    if (jccDatabase == null) {
      jccDatabase = "*LOCAL";
    }

  }

  /**
   * Constructs an object for testing applets.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  public JDTestDriver() throws Exception {
    super();
  }

  /**
   * Constructs an object for testing applications.
   * 
   * @param args
   *          The command line arguments.
   * @exception Exception
   *              If an exception occurs.
   **/
  public JDTestDriver(String[] args) throws Exception {
    super(args);
  }

  /**
   * Adds a testcase.
   **/
  protected void addTestcase(JDTestcase testcase) {
    testcase.setTestDriver(this);
    super.addTestcase(testcase);
  }

  protected boolean areLargeDecimalPrecisionsSupported() {
    return largeDecimalPrecisionSupport_;
  }

  protected boolean areDecfloatsSupported() /* @F4A */
  {
    return decfloatSupport_;
  }

  protected boolean areBigintsSupported() { 
    return bigintSupport_; 
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

  protected boolean areBooleansSupported() {
    return booleanSupport_;
  }

  protected boolean areSavepointsSupported() { 
    return savepointSupport_; 
  } 

  /**
   * Indicates if named parameters are supported on the server release.
   **/
  protected boolean areNamedParametersSupported() { 
    return namedParameterSupport_; 
  } 

  /**
   * Indicated if cursor holdabilty is supported on the server release.
   **/
  protected boolean areCursorHoldabilitySupported() { 
    return cursorHoldabilitySupport_; 
  } 

  /**
   * Indicated if multiple open result sets are supported on the server release.
   **/
  protected boolean areMultipleOpenResultSetsSupported() { 
    return multipleOpenResultSetSupport_; 
  } 

  /**
   * Indicated if generatedKeys are supported on the server release.
   **/
  protected boolean areGeneratedKeysSupported() { 
    return generatedKeySupport_; 
  } 

  /**
   * Indicates if updateable Lobs are supported on the server release.
   **/
  protected boolean areUpdateableLobsSupported() { 
    return updateableLobsSupport_; 
  } 

  /**
   * Indicates if lobs are supported on the server release.
   **/
  protected boolean areLobsSupported() {
    // For now, don't let remote native try to use
    // lobs... it will just mess them up.
    if (driver_ == DRIVER_NATIVE_RMI)
      return false;

    return lobSupport_;
  }

  /**
   * Indicates if datalinks are supported.
   */
  public boolean areDatalinksSupported() {
    return datalinkSupport_;
  }

  protected boolean areReturnValuesSupported() { 
    return returnValueSupport_; 
  } 

  /**
   * Creates the testcases.
   **/
  public void createTestcases() {
    // This extra call is side effect of some changes made
    // to TestDriver. It is no longer needed, but would
    // involve changing every test driver, so I left it.
    createTestcases2();
  }

  /**
   * Creates the testcases.
   **/
  protected abstract void createTestcases2();

  /* Silently drops a table, does not report an error if the table is not found */
  /* This will drop the table without looking at the skipCleanup value */
  public static void dropTable(Statement s, String table) throws SQLException {
    try {
      s.executeUpdate("DROP TABLE " + table);
    } catch (SQLException e) {
      if (e.getErrorCode() == -204
          || (e.getMessage().indexOf("not found") >= 0)) {
        // It is ok if the file is not found
      } else {
        throw e;
      }
    }
  }

  public static void dropProcedure(Statement s, String proc)
      throws SQLException {
    try {
      s.executeUpdate("DROP PROCEDURE " + proc);
    } catch (SQLException e) {
      if (e.getErrorCode() == -204
          || (e.getMessage().indexOf("not found") >= 0)) {
        // It is ok if the file is not found
      } else {
        throw e;
      }
    }

  }

  public static void dropDistinctType(Statement s, String typeName)
      throws SQLException {
    try {
      s.executeUpdate("DROP DISTINCT TYPE " + typeName);
    } catch (SQLException e) {
      if (e.getErrorCode() == -204
          || (e.getMessage().indexOf("not found") >= 0)) {
        // It is ok if the file is not found
      } else {
        System.out
            .println("Unexpected error on DROP DISTINCT TYPE " + typeName);
        System.out.println(e.getMessage());
        e.printStackTrace(System.out);
        throw e;
      }
    }
  }

  /**
   * Drops a collection.
   **/
  public static void dropCollection(Connection c, String collection) {
    try {

      // Make sure the sysreply list is being used
      //
      System.out.println("Dropping collection " + collection + ".");

      Statement s = c.createStatement();
      s.executeUpdate("CALL QSYS2.QCMDEXC('CHGJOB INQMSGRPY(*SYSRPYL)   ')");
      s.executeUpdate("DROP COLLECTION " + collection);
      s.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Returns the base URL for the driver being tested.
   * 
   * @param driver
   *          The driver.
   * @param systemObject
   *          The system object.
   * @return The base URL.
   **/
  public String getBaseURL(int driver, AS400 systemObject, String rdbName) {
    String systemName = "";
    switch (driver) {
    case DRIVER_TOOLBOX:
      systemName = systemObject.getSystemName(); 
      if (systemName.equalsIgnoreCase("localhost")) 
        systemName = systemName_; 
      if (!useSecondaryUrl_) 
        if (proxy_ == null || proxy_.length() == 0 ) { 
          return "jdbc:as400://" + systemName; 
        } else {
          String url ="jdbc:as400://" + systemName +";proxy server="+proxy_; 
          System.out.println("JDTestDriver using "+url); 
          return url;
        }
      else
        return "jdbc:as400:" + ";secondary URL=jdbc:as400://" + systemName + 
            "\\;boogie=woogie\\;user=JAVA\\;password=JTEAM1"; 

    case DRIVER_NATIVE:
      // The toolbox System object is always changing the connection string
      // to localhost when running to the local system. This messes up serveral
      // testcases for us. If we passed in a system name, we want it to be used.
      if (!useSecondaryUrl_) { 
        if (rdbName_ != null)
          return "jdbc:db2:" + rdbName_;
        else if (systemName_ != null)

          return "jdbc:db2:" + systemName_;
        else
          return "jdbc:db2:" + systemObject.getSystemName();
      } else
        return "jdbc:as400:;secondary URL=jdbc:db2:"
            + systemObject.getSystemName(); 

    case DRIVER_JCC:
      if (!useSecondaryUrl_) {
        // load the iSeries driver to keep the native driver from being used for
        // the db2 url
        try {
          Class.forName("com.ibm.db2.jdbc.app.DB2iSeriesDriver");
        } catch (Exception e) {
        }

        if (systemName_ != null)
          return "jdbc:db2://" + systemName_ + ":" + jccPort + "/"
              + jccDatabase + ":";
        else
          return "jdbc:db2://" + systemObject.getSystemName() + ":" + jccPort
              + "/" + jccDatabase + ":";
      } else
        return "jdbc:as400:;secondary URL=jdbc:db2:"
            + systemObject.getSystemName() + "/"; 

    case DRIVER_NATIVE_RMI:
      // The same as the situation above... if the system name was passed in
      // as a parameter, it is not OK to change it to localhost.
      if (systemName_ != null)
        return "jdbc:db2rmi:" + systemName_;
      else
        return "jdbc:db2rmi:" + systemObject.getSystemName();

    case DRIVER_JTOPENLITE:

      systemName = systemObject.getSystemName();
      if (systemName.equalsIgnoreCase("localhost"))
        systemName = systemName_;

      return "jdbc:jtopenlite://" + systemName;

    default:
      return "No driver selected.";
    }
  }

  /**
   * Returns the base URL for the driver being tested.
   * 
   * @return The base URL.
   **/
  public String getBaseURL() {
    return getBaseURL(driver_, systemObject_, rdbName_);
  }

  /**
   * Returns the JDBC driver being tested.
   * 
   * @return The JDBC driver.
   **/
  public int getDriver() {
    if (!driverSetupDone) { 
      driverSetup(); 
    } 
    return driver_;
  }

  public int getSubDriver() {
    return subDriver_;
  }

  public boolean isToolboxDriver() {
    int driver = getDriver();
    if (driver == DRIVER_TOOLBOX) {
      return true;
    } else {
      return false;
    }
  }

  public int getDatabaseType() {
    if (!driverSetupDone) {
      driverSetup();
    }
    return db_;
  }

  /**
   * Returns the AS/400 release being tested.
   * 
   * @return The AS/400 release.
   **/
  public int getRelease() {
    return release_;
  }

  /**
   * @C1A Returns the AS/400 library which is set as the default for the
   *      testcase run.
   * @return The AS/400 library.
   **/
  public String getCollection() {
    return collection_;
  }

  /**
   * Issue an executeUpdate() 'safely', doing only dump of the exception
   * information if a failure occurs. This allows a maximal number of testcases
   * to work when some individual setup operation fails due to one particular
   * problem, and allows all other setup operations to at least be attempted.
   * Return boolean success
   */
  protected static boolean safeExecuteUpdate(Statement stmt, 
      String sql) {
    try {
      stmt.executeUpdate(sql);
      return true;
    } catch (SQLException e) {
      System.out.println("Possible Setup Failure: " + sql);
      e.printStackTrace();
      return false;
    }
  }

  protected static boolean safeExecuteUpdate(Statement stmt, String sql,
      boolean ignoreFailures) {
    try {
      stmt.executeUpdate(sql);

    } catch (SQLException e) {

    }
    return true;
  }

  /**
   * driverSetup --> setup the driver information based on misc argument Moved
   * from setup
   * 
   * @E1A
   */

  protected void driverSetup() {
    // Figure out the JDK level
    // Now done by static initializer in TestDriver.java
    getDatabaseTypeStatic();
    // Parse the -misc argument, if provided.
    if (misc_ != null) {
      StringTokenizer tokenizer = new StringTokenizer(misc_.trim(), ",");
      while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken();

        // Was the toolbox driver specified?
        if (token.equalsIgnoreCase(DRIVER_STRING_TOOLBOX))
          registerDriver(DRIVER_CLASS_TOOLBOX, DRIVER_TOOLBOX);
        else if (token.equalsIgnoreCase(DRIVER_STRING_JT400ANDROID))
          registerSubDriver(DRIVER_CLASS_TOOLBOX, DRIVER_TOOLBOX,
              SUBDRIVER_JT400ANDROID);
        else if (token.equalsIgnoreCase(DRIVER_STRING_JTOPENCA))
          registerSubDriver(DRIVER_CLASS_TOOLBOX, DRIVER_TOOLBOX,
              SUBDRIVER_JTOPENCA);
        else if (token.equalsIgnoreCase(DRIVER_STRING_JTOPENSF))
          registerSubDriver(DRIVER_CLASS_TOOLBOX, DRIVER_TOOLBOX,
              SUBDRIVER_JTOPENSF);
        else if (token.equalsIgnoreCase(DRIVER_STRING_JTOPENPX))
          registerSubDriver(DRIVER_CLASS_TOOLBOX, DRIVER_TOOLBOX,
              SUBDRIVER_JTOPENPX);
        // Was the native driver specified?
        else if (token.equalsIgnoreCase(DRIVER_STRING_NATIVE))
          registerDriver(DRIVER_CLASS_NATIVE, DRIVER_NATIVE);

        else if (token.equalsIgnoreCase(DRIVER_STRING_JCC))
          registerDriver(DRIVER_CLASS_JCC, DRIVER_JCC);

        else if (token.equalsIgnoreCase(DRIVER_STRING_JTOPENLITE))
          registerDriver(DRIVER_CLASS_JTOPENLITE, DRIVER_JTOPENLITE);

        // Was the native RMI driver specified?
        else if (token.equalsIgnoreCase("nativeRMI"))
          registerDriver(DRIVER_CLASS_NATIVE_RMI, DRIVER_NATIVE_RMI);

        // Was a release specified?
        else if (token.equalsIgnoreCase("V4R2M0"))
          release_ = RELEASE_V4R2M0;
        else if (token.equalsIgnoreCase("V4R3M0"))
          release_ = RELEASE_V4R3M0;
        else if (token.equalsIgnoreCase("V4R4M0"))
          release_ = RELEASE_V4R4M0;
        else if (token.equalsIgnoreCase("V4R5M0")) 
          release_ = RELEASE_V4R5M0; 
        else if (token.equalsIgnoreCase("V5R1M0")) 
          release_ = RELEASE_V5R1M0;
        else if (token.equalsIgnoreCase("V5R2M0")) 
          release_ = RELEASE_V5R2M0;
        else if (token.equalsIgnoreCase("V5R3M0"))
          release_ = RELEASE_V5R3M0;
        else if (token.equalsIgnoreCase("V5R4M0"))
          release_ = RELEASE_V5R4M0;
        else if (token.equalsIgnoreCase("V5R5M0"))
          release_ = RELEASE_V5R5M0;
        else if (token.equalsIgnoreCase("V6R1M0"))
          release_ = RELEASE_V6R1M0;
        else if (token.equalsIgnoreCase("V7R1M0"))
          release_ = RELEASE_V7R1M0;
        else if (token.equalsIgnoreCase("V7R2M0"))
          release_ = RELEASE_V7R2M0;
        else if (token.equalsIgnoreCase("V7R3M0"))
          release_ = RELEASE_V7R3M0;
        else if (token.equalsIgnoreCase("V7R4M0"))
          release_ = RELEASE_V7R4M0;
        else if (token.equalsIgnoreCase("V7R5M0"))
          release_ = RELEASE_V7R5M0;
        else if (token.equalsIgnoreCase("V7R6M0"))
          release_ = RELEASE_V7R6M0;

        // Was "secondary URL mode" specified?
        else if (token.equalsIgnoreCase("secondaryURL")) {
          useSecondaryUrl_ = true;
        }
      }
    }

    String dbString = System.getProperty("databaseType");
    if (dbString != null) {
      if (dbString.equals("ZOS")) {
        db_ = DB_ZOS;
      } else if (dbString.equals("LUW")) {
        db_ = DB_LUW;
      } else {
        db_ = DB_SYSTEMI;
      }
    } else {
      db_ = DB_SYSTEMI;
    }

    driverSetupDone = true;

  }

  /**
   * Applet.init().  
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  // Changed from init() to setup(). Otherwise, when run as an
  // applet, we parse before the tester human has even typed in
  // parameters.
  public void setup() 
      throws Exception 
  {

    //
    // If the setJobLogOption property is set, then leave a job log
    //
    String x = System.getProperty("setJobLogOption");
    if (x != null) {
      JDJobName.setJobLogOption();
    }

    if (!driverSetupDone) { 
      driverSetup(); 
    } 

    collection_ = testLib_; 

    // If no driver was specified, then just try to register
    // both drivers and hope that one is in the classpath.
    if (driver_ == DRIVER_NONE) {
      registerDriver(DRIVER_CLASS_NATIVE, DRIVER_NATIVE);
      registerDriver(DRIVER_CLASS_TOOLBOX, DRIVER_TOOLBOX);
    }

    // Report any errors.
    if (driver_ == DRIVER_NONE)
      System.out.println("Error: No JDBC drivers registered.");

    // Determine the release if it was not specified.
    if ((release_ == RELEASE_NONE) && (driver_ == DRIVER_TOOLBOX)) {
      int vrm = 0;
      try {
        vrm = systemObject_.getVRM();
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (vrm == AS400.generateVRM(4, 2, 0))
        release_ = RELEASE_V4R2M0;
      else if (vrm == AS400.generateVRM(4, 3, 0))
        release_ = RELEASE_V4R3M0;
      else if (vrm == AS400.generateVRM(4, 4, 0))
        release_ = RELEASE_V4R4M0;
      else if (vrm == AS400.generateVRM(4, 5, 0)) 
        release_ = RELEASE_V4R5M0; 
      else if (vrm == AS400.generateVRM(5, 1, 0)) 
        release_ = RELEASE_V5R1M0; 
      else if (vrm == AS400.generateVRM(5, 2, 0)) 
        release_ = RELEASE_V5R2M0; 
      else if (vrm == AS400.generateVRM(5, 3, 0))
        release_ = RELEASE_V5R3M0;
      else if (vrm == AS400.generateVRM(5, 4, 0))
        release_ = RELEASE_V5R4M0;
      else if (vrm == AS400.generateVRM(5, 5, 0)) 
        release_ = RELEASE_V5R5M0;
      else if (vrm == AS400.generateVRM(6, 1, 0))
        release_ = RELEASE_V6R1M0;
      else if (vrm == AS400.generateVRM(7, 1, 0))
        release_ = RELEASE_V7R1M0;
      else if (vrm == AS400.generateVRM(7, 2, 0))
        release_ = RELEASE_V7R2M0;
      else if (vrm == AS400.generateVRM(7, 3, 0))
        release_ = RELEASE_V7R3M0;
      else if (vrm == AS400.generateVRM(7, 4, 0))
        release_ = RELEASE_V7R4M0;
      else if (vrm == AS400.generateVRM(7, 5, 0))
        release_ = RELEASE_V7R5M0;
      else if (vrm == AS400.generateVRM(7, 6, 0))
        release_ = RELEASE_V7R6M0;
    }

    // Determine if what is supported.
    lobSupport_ = (release_ >= JDTestDriver.RELEASE_V4R4M0);
    datalinkSupport_ = (release_ >= JDTestDriver.RELEASE_V4R4M0);
    if (db_ == DB_ZOS || db_ == DB_LUW) {
      datalinkSupport_ = false;
    }
    largeDecimalPrecisionSupport_ = (release_ >= JDTestDriver.RELEASE_V5R3M0);
    bigintSupport_ = (release_ >= JDTestDriver.RELEASE_V4R5M0); 
    decfloatSupport_ = (release_ >= JDTestDriver.RELEASE_V5R5M0); 
    xmlSupport_ = (release_ >= JDTestDriver.RELEASE_V7R1M0); 
    arraySupport_ = (release_ >= JDTestDriver.RELEASE_V7R1M0); 
    timestamp12Support_ = (release_ >= JDTestDriver.RELEASE_V7R2M0); 
    returnValueSupport_ = (release_ >= JDTestDriver.RELEASE_V4R4M0); 
    booleanSupport_ = (release_ >= JDTestDriver.RELEASE_V7R5M0); 

    try { 
      Class.forName("java.sql.Savepoint"); 
      savepointSupport_ = (release_ >= JDTestDriver.RELEASE_V5R2M0); 
    } 
    catch (Exception e) 
    {
      
      savepointSupport_ = false; 
    } 

    try { 
      Class.forName("java.sql.Savepoint"); 
      switch (driver_) 
      {
      
      case DRIVER_TOOLBOX: 
        namedParameterSupport_ = (release_ >= JDTestDriver.RELEASE_V4R5M0); 
        break; 
      case DRIVER_NATIVE: 
        namedParameterSupport_ = (release_ >= JDTestDriver.RELEASE_V5R1M0); 
        break; 
      case DRIVER_JCC: 
        // As of 10/3/2011, jcc not supporting named parameter for i
        // com.ibm.db2.jcc.am.SqlFeatureNotSupportedException:
        // [jcc][10416][12706][4.11.69] Method not supported for the target
        // server. ERRORCODE=-4450, SQLSTATE=0A502
        namedParameterSupport_ = false;
        break; 

      } 
    } 
    catch (Exception e) 
    {
      
      namedParameterSupport_ = false; 
    } 

    try { 
      Class.forName("java.sql.Savepoint"); 
      switch (driver_) 
      {
      
      case DRIVER_TOOLBOX: 
        cursorHoldabilitySupport_ = (release_ >= JDTestDriver.RELEASE_V5R2M0); 
        break; 
      case DRIVER_NATIVE: 
        cursorHoldabilitySupport_ = (release_ >= JDTestDriver.RELEASE_V5R1M0); 
        break; 
      case DRIVER_JCC:
        cursorHoldabilitySupport_ = (release_ >= JDTestDriver.RELEASE_V5R1M0);
        break;
      } 
    } 
    catch (Exception e) 
    {
      
      cursorHoldabilitySupport_ = false; 
    } 

    try { 
      Class.forName("java.sql.Savepoint"); 
      switch (driver_) {
      case DRIVER_TOOLBOX: 
        multipleOpenResultSetSupport_ = (release_ > JDTestDriver.RELEASE_V6R1M0); 
        break; 
      case DRIVER_NATIVE: 
        multipleOpenResultSetSupport_ = (release_ >= JDTestDriver.RELEASE_V5R2M0); 
        break; 
      case DRIVER_JCC:
        multipleOpenResultSetSupport_ = (release_ >= JDTestDriver.RELEASE_V5R2M0);
        break;
      }
    } 
    catch (Exception e) 
    {
      
      multipleOpenResultSetSupport_ = false; 
    } 

    try { 
      Class.forName("java.sql.Savepoint"); 
      generatedKeySupport_ = (release_ >= JDTestDriver.RELEASE_V5R2M0); 
    } 
    catch (Exception e) 
    {
      
      generatedKeySupport_ = false; 
    } 

    try { 
      Class.forName("java.sql.Savepoint"); 
      switch (driver_) 
      {
      
      case DRIVER_TOOLBOX: 
        updateableLobsSupport_ = (release_ >= JDTestDriver.RELEASE_V4R5M0); 
        break; 
      case DRIVER_NATIVE: 
        updateableLobsSupport_ = (release_ >= JDTestDriver.RELEASE_V5R1M0); 
        break; 
      case DRIVER_JCC:
        updateableLobsSupport_ = (release_ >= JDTestDriver.RELEASE_V5R1M0);
        break;
      } 
    } 
    catch (Exception e) 
    {
      
      updateableLobsSupport_ = false; 
    }

    try {
      OSName_ = System.getProperty("os.name");
    } catch (Throwable t) {
      updateableLobsSupport_ = false;
    }

    System.out.println("OS/400 Release = " + release_ + ".");
    System.out.println("JDK level = " + JVMInfo.getJavaVersionString());
    // maxMemory only available in JDK 1.4 or later
    if (JVMInfo.getJDK() >= JVMInfo.JDK_14) {
      System.out.println("Max memory = " + Runtime.getRuntime().maxMemory());
    }
    System.out.println("Java home = " + System.getProperty("java.home"));
    System.out.println("JVM Running on " + OSName_);
    Locale locale = Locale.getDefault();
    System.out.println("Default locale = " + locale);
    Calendar defaultCalendar = Calendar.getInstance();
    System.out.println("Default Calendar = " + defaultCalendar);

    System.out.println("Lob support = " + lobSupport_ + ".");
    System.out.println("Large DECIMAL/NUMERIC precision support = "
        + largeDecimalPrecisionSupport_ + ".");
    System.out.println("BIGINT support = " + bigintSupport_ + "."); 
    System.out.println("XML support = " + xmlSupport_ + "."); 
    System.out.println("Array support = " + arraySupport_ + "."); 
    System.out.println("Timestamp12 support = " + timestamp12Support_ + "."); 
    System.out.println("Boolean support = " + booleanSupport_ + ".");
    System.out.println("Savepoint support = " + savepointSupport_ + "."); 
    System.out.println("Named Parameter support = " + namedParameterSupport_
        + "."); 
    System.out.println("Cursor Holdability support = "
        + cursorHoldabilitySupport_ + "."); 
    System.out.println("Multiple Open Result Set support = "
        + multipleOpenResultSetSupport_ + "."); 
    System.out.println("Generated Key support = " + generatedKeySupport_ + "."); 
    System.out.println("Updateable Lobs support = " + updateableLobsSupport_
        + "."); 
    System.out.println("Secondary URL mode = " + useSecondaryUrl_ + "."); 
    System.out.println("Collection = " + collection_ + ".");
    System.out.println("Return value support = " + returnValueSupport_ + "."); 
    System.out.println("Decimal Floating Point support = " + decfloatSupport_
        + "."); 
    switch (driver_) {
    case DRIVER_NATIVE:
      System.out.println("Driver = NATIVE");
      System.out.println("Jobname= " + JDJobName.getJobName());

      break;
    case DRIVER_TOOLBOX:
      switch (subDriver_) {
      case SUBDRIVER_JT400ANDROID:
        System.out.println("Driver = JT400ANDROID");
        break;
      case SUBDRIVER_JTOPENCA:
        System.out.println("Driver = JTOPENCA");
        break;
      case SUBDRIVER_JTOPENSF:
        System.out.println("Driver = JTOPENSF");
        break;
      case SUBDRIVER_JTOPENPX:
        System.out.println("Driver = JTOPENPX");
        break;
      default:
        System.out.println("Driver = TOOLBOX");
      }
      break;

    case DRIVER_JCC:
      System.out.println("Driver = JCC");
      break;
    }

    
    
  }

  /**
   * Registers a JDBC driver.
   * 
   * @param driverName
   *          The JDBC driver name.
   * @param driver
   *          The driver value to set if successful.
   **/
  private void registerDriver(String driverName, int driver) {
    System.out.println("JDTestDriver.registerDriver() called with "
        + driverName);
    try {
      Class clazz = Class.forName(driverName);

      // For now we have to do this the hard way....
      if (driver == DRIVER_NATIVE_RMI)
        DriverManager.registerDriver((Driver) clazz.newInstance());

      driver_ = driver;
      subDriver_ = driver;
    } catch (Throwable e) {
      // Ignore.
      System.out.println("Unable to register " + driverName + ".");
    }
  }

  private void registerSubDriver(String driverName, int driver, int subdriver) {
    registerDriver(driverName, driver);
    subDriver_ = subdriver;
  }

  // B3A The getConnection() methods and the utility function for them

  /**
   * Get a connection. Calls the DriverManager's getConnection() method for
   * default testing. Else returns pooled or XA connection
   * 
   * @param url
   *          The url
   * @exception Exception
   *              If an exception occurs.
   **/
  public Connection getConnection(String url) throws Exception {
    return getConnection(url, (String) null);
  }

  String fixupUrl(String url) {
    if ((subDriver_ == SUBDRIVER_JTOPENCA)
        || (subDriver_ == SUBDRIVER_JTOPENSF)) {
      int enableClientAffinitiesIndex = url
          .indexOf("enableClientAffinitiesList");
      if (enableClientAffinitiesIndex < 0) {
        url = url + ";enableClientAffinitiesList=1";
        /*
         * Only add seamless failover if original URL did not have
         * enableClientAffinitiesList
         */
        if (subDriver_ == SUBDRIVER_JTOPENSF) {
          int seamlessFailoverIndex = url.indexOf("enableSeamlessFailover");
          if (seamlessFailoverIndex < 0) {
            url = url + ";enableSeamlessFailover=1";
          }
        }

        /*
         * Set retryIntervalForClientReroute if retryOptions are not set.
         */
        int retryIntervalIndex = url.indexOf("retryIntervalForClientReroute");
        int maxRetriesIndex = url.indexOf("maxRetriesForClientReroute");
        if ((retryIntervalIndex < 0) && (maxRetriesIndex < 0)) {
          url = url + ";retryIntervalForClientReroute=5";
        }
      }

    }

    return url;
  }

  public Connection getConnection(String url, String testInfo) throws Exception {
    if (testInfo == null)
      testInfo = calculateTestInfoFromStack();
    url = fixupUrl(url);

    Connection connection = null;
    if ((driver_ == DRIVER_NATIVE) || (driver_ == DRIVER_NATIVE_RMI)) {

      if (connType_ == Testcase.CONN_DEFAULT) {
        connection = DriverManager.getConnection(url);
      } else {
        JDTestDriver_NativeDriverMethods nativeDriverMethods = new JDTestDriver_NativeDriverMethods();
        nativeDriverMethods.setDefSchemaSet(defSchemaSet);
        connection = nativeDriverMethods.getConnection(url, driver_, connType_);
        defSchemaSet = nativeDriverMethods.getDefSchemaSet();

        if (connection == null)
          connection = DriverManager.getConnection(url); // default
      }
    } else if (driver_ == DRIVER_JCC) {

      // It appears that to use properties, the userid / password must be passed
      // on the url. For example:.
      // setvar
      // CON=java.sql.DriverManager.getConnection(jdbc:db2:systemname/RDB:446/FOWGAI2:user=testuser;password=xxx;retrieveMessagesFromServerOnGetMessage=true;)
      // The URL must end with a semicolon and a colon separates the DBname from
      // the properties
      //

      // JCC always requires USERID to be passed
      // Also remove userid / password from URL if needed
      // System.out.println("Connecting using jcc to starting with "+url);
      // Add userid / password at the end

      String uid = userId_;
      String pwd = password_;
      {
        int useridIndex = url.indexOf(";user=");
        if (useridIndex > 0) {
          int endIndex = url.indexOf(";", useridIndex + 6);
          if (endIndex < 0)
            endIndex = url.length();
          uid = url.substring(useridIndex + 6, endIndex);
          url = url.substring(0, useridIndex) + url.substring(endIndex);
        }
      }
      int passwordIndex = url.indexOf(";password=");
      while (passwordIndex > 0) {
        int endIndex = url.indexOf(";", passwordIndex + 10);
        if (endIndex < 0)
          endIndex = url.length();
        pwd = url.substring(passwordIndex + 10, endIndex);
        url = url.substring(0, passwordIndex) + url.substring(endIndex);
        passwordIndex = url.indexOf(";password=");
      }
      //
      // Remove properties we know are not supported
      //
      for (int i = 0; i < jccStripProperties.length; i++) {
        String searchString = ";" + jccStripProperties[i] + "=";
        passwordIndex = url.indexOf(searchString);
        while (passwordIndex > 0) {
          int endIndex = url
              .indexOf(";", passwordIndex + searchString.length());
          if (endIndex < 0)
            endIndex = url.length();
          url = url.substring(0, passwordIndex) + url.substring(endIndex);
          passwordIndex = url.indexOf(searchString);
        }
      }

      int removeIndex = url.indexOf(":;");
      if (removeIndex > 0) {
        url = url.substring(0, removeIndex + 1)
            + url.substring(removeIndex + 2);
      }
      while ((url.charAt(url.length() - 1) == ';')) {
        url = url.substring(0, url.length() - 1);
      }

      // Add final properties
      //
      String onlyUrl = url + "user=" + uid + ";password=" + pwd
          + ";retrieveMessagesFromServerOnGetMessage=true;";

      // System.out.println("Connecting using jcc to "+url+" using "+uid+" pwd="+pwd);
      try {
        connection = DriverManager.getConnection(onlyUrl); // jcc
      } catch (Exception e) {
        System.err.flush();
        System.out.flush();
        e.printStackTrace();
        System.err.flush();
        System.out.flush();
        System.out.println("Exception connecting to JCC using only URL "
            + onlyUrl);
        System.out.println("Retrying with old approach");
        connection = DriverManager.getConnection(url, uid, pwd); // jcc

      }
      // System.out.println("Connected\n");
   
    } else {
      // Must be toolbox 
      
      
      if (isExtendedDynamic_) {
        if (url.indexOf("extended dynamic") == -1) {
          String library = collection_;

          grantPackagePermissions(url, library, userId_);

          if (library == null)
            library = DEFAULT_COLLECTION;
          url += EXTENDED_DYNAMIC_OPTIONS_MINUS_LIBRARY + library
              + ";libraries=" + library;
        }
      }
      String portNumber = System.getProperty("JDTestDriver.portNumber");
      if (portNumber != null) {
        url += ";portNumber=" + portNumber;
      }

      if (url.indexOf(":as400:") > 0) {
        Properties info = new Properties();
        setPropertiesFromUrl(info, url);
        String schema = getDefaultSchema(systemObject_, info);
        connection = as400JdbcDriver_.connect(systemObject_, info, schema);

      } else {
        throw new Exception(":as400: not in url: " + url);
      }
    }

    postConnectProcessing(connection, testInfo);
    return connection;

  }

  /* Make sure that the userId has permission to all the packages in the library */

  private void grantPackagePermissions(String url, String library, String userId) {
    if (pwrSysUserID_ != null) {
      if (!pwrSysUserID_.equals(userId)) {
        try {
          Connection changeConnection = DriverManager.getConnection(url,
              pwrSysUserID_, pwrSysPassword_);

          /* Find the packages in the library */
          Statement queryStatement = changeConnection.createStatement();
          Statement grantStatement = changeConnection.createStatement();
          ResultSet rs = queryStatement
              .executeQuery("select package_name from qsys2.syspackage where "
                  + "package_schema = '" + library + "'");

          /* grant permission for the packages */
          while (rs.next()) {
            String packageName = rs.getString(1);

            grantStatement.executeUpdate("GRANT ALL ON " + library + "."
                + packageName + " TO " + userId);

          }

          grantStatement.close();
          queryStatement.close();

        } catch (Exception e) {
          System.out.println("Warning:  exception caught granting " + userId
              + " permssion to packages in " + library);
          e.printStackTrace(System.out);
        }
      }
    }

  }

  /**
   * Get a connection. Calls the DriverManager's getConnection() method for
   * default testing. Else returns pooled or XA connection
   * 
   * @param url
   *          The url
   * @param properties
   *          Properties for the connection
   * @exception Exception
   *              If an exception occurs.
   **/
  public Connection getConnection(String url, Properties info) throws Exception {
    return getConnection(url, info, null);
  }

  public Connection getConnection(String url, Properties info, String testInfo)
      throws Exception {
    if (testInfo == null)
      testInfo = calculateTestInfoFromStack();

    url = fixupUrl(url);

    Connection connection = null;
    if ((driver_ == DRIVER_NATIVE) || (driver_ == DRIVER_NATIVE_RMI)) {
      if (connType_ == Testcase.CONN_DEFAULT) {
        connection = DriverManager.getConnection(url, info);
      } else {
        JDTestDriver_NativeDriverMethods nativeDriverMethods = new JDTestDriver_NativeDriverMethods();
        nativeDriverMethods.setDefSchemaSet(defSchemaSet);
        connection = nativeDriverMethods.getConnection(url, info, driver_,
            connType_);
        defSchemaSet = nativeDriverMethods.getDefSchemaSet();

        if (connection == null) {
          connection = DriverManager.getConnection(url); // default
        }
      }
    } else {
      if (isExtendedDynamic_) {
        if (url.indexOf("extended dynamic") == -1) {
          String library = collection_;
          grantPackagePermissions(url, library, userId_);

          if (library == null)
            library = DEFAULT_COLLECTION;
          url += EXTENDED_DYNAMIC_OPTIONS_MINUS_LIBRARY + library
              + ";libraries=" + library;
        }
      }
      String portNumber = System.getProperty("JDTestDriver.portNumber");
      if (portNumber != null) {
        url += ";portNumber=" + portNumber;
      }

      if (url.indexOf(":as400:")> 0) { 
          if (info == null) {
            info = new Properties(); 
          }
          setPropertiesFromUrl(info, url); 
          String schema=getDefaultSchema(systemObject_, info); 
          connection = as400JdbcDriver_.connect(systemObject_, info, schema);
        
          
          
      } else {
         throw new Exception(":as400: not in url: "+url); 
      }
      
    }
    postConnectProcessing(connection, testInfo);
    return connection;
  }

  private String getDefaultSchema(AS400 systemObject_, Properties info) {
    String schema = info.getProperty("libraries");
    if (schema != null) { 
      int commaIndex = schema.indexOf(','); 
      if (commaIndex > 0) { 
        schema = schema.substring(0,commaIndex); 
        if ("*LIBL".equals(schema)) {
          return null; 
        }
      }
    } else {
      // leave the schema as null 
        // was schema = systemObject_.getUserId();
    }
    return schema;
  }

  private void setPropertiesFromUrl(Properties info, String url) {
    int startIndex = 0; 
    int urlLen = url.length();

    int jdbcAs400Index = url.indexOf("jdbc:as400:");
    String hostnameInfo = null ; 
    startIndex = url.indexOf(';', startIndex); 
    if (startIndex == -1) {
	if (jdbcAs400Index >= 0) {
	    hostnameInfo = url.substring(jdbcAs400Index+11);
	}
      startIndex = urlLen; 
    } else {
	if (jdbcAs400Index >= 0) {
	    hostnameInfo = url.substring(jdbcAs400Index+11, startIndex);
	}
      startIndex++; 
    }

    if (hostnameInfo != null) {
	int colonIndex = hostnameInfo.indexOf(':');
	if (colonIndex >= 0) {
	    String portString = hostnameInfo.substring(colonIndex+1);
	    info.setProperty("portNumber",portString); 
	}
    }



    while (startIndex < urlLen) { 
      int endIndex = url.indexOf(';', startIndex); 
      if (endIndex == -1) {
        endIndex = urlLen; 
      }
      String piece = url.substring(startIndex,endIndex);
      int equalsIndex = piece.indexOf("="); 
      if (equalsIndex > 0) {
         String key = piece.substring(0,equalsIndex); 
         String value = piece.substring(equalsIndex+1);
	 if (key.equals("libraries")) {
	     value=value.replaceAll("  *",",");
	 }
         info.setProperty(key, value);
        
      }
      startIndex = endIndex+1;  
    }
    
  }

  /**
   * Get a connection. Calls the DriverManager's getConnection() method for
   * default testing. Else returns pooled or XA connection
   * 
   * @param url
   *          The url
   * @param uid
   *          The userid to connect with
   * @param pwd
   *          Password for the userid
   * @exception Exception
   *              If an exception occurs.
   **/
  public Connection getConnection(String url, String uid, char[] encryptedPwd)
      throws Exception {
    return getConnection(url, uid, encryptedPwd, null);
  }

  public Connection getConnection(String url, String uid, char[] encryptedPwd,
				  String testInfo) throws Exception {
      if (testInfo == null)
	  testInfo = calculateTestInfoFromStack();

      url = fixupUrl(url);

      Connection connection = null;
      if ((driver_ == DRIVER_NATIVE) || (driver_ == DRIVER_NATIVE_RMI)) {
	  if (connType_ == Testcase.CONN_DEFAULT) {
	      String pwd = PasswordVault.decryptPasswordLeak(encryptedPwd); 
	      try { 
		  connection = DriverManager.getConnection(url, uid, pwd);
	      } catch (SQLException sqlex) {
		  System.out.println("Error connecting with "+url+" "+uid+" "+pwd); 
		  throw sqlex; 
	      }
	  } else {
	    String pwd = PasswordVault.decryptPasswordLeak(encryptedPwd); 
	      JDTestDriver_NativeDriverMethods nativeDriverMethods = new JDTestDriver_NativeDriverMethods();
	      nativeDriverMethods.setDefSchemaSet(defSchemaSet);
	      connection = nativeDriverMethods.getConnection(url, uid, pwd, driver_,
							     connType_);
	      defSchemaSet = nativeDriverMethods.getDefSchemaSet();

	      if (connection == null) {
		  connection = DriverManager.getConnection(url); // default
	      }
	  }
      } else {
	  if (driver_ == DRIVER_JCC) {
	// remove the properties we don't support
	//
	// Remove properties we know are not supported
	//
	      int propertyIndex = 0;
	      for (int i = 0; i < jccStripProperties.length; i++) {
		  String searchString = ";" + jccStripProperties[i] + "=";
		  propertyIndex = url.indexOf(searchString);
		  while (propertyIndex > 0) {
            int endIndex = url.indexOf(";",
				       propertyIndex + searchString.length());
	    if (endIndex < 0)
		endIndex = url.length();
	    url = url.substring(0, propertyIndex) + url.substring(endIndex);
	    propertyIndex = url.indexOf(searchString);
		  }
	      }
	      int removeIndex = url.indexOf(":;");
	      if (removeIndex > 0) {
		  url = url.substring(0, removeIndex + 1)
		    + url.substring(removeIndex + 2);
	      }
              String pwd = PasswordVault.decryptPasswordLeak(encryptedPwd); 

	      System.out.println("Connecting using jcc to " + url + " using " + uid
				 + " pwd=" + pwd);
	      String portNumber = System.getProperty("JDTestDriver.portNumber");
	      if (portNumber != null) {
		  url += ";portNumber=" + portNumber;
	      }

	      connection = DriverManager.getConnection(url, uid, pwd); // toolbox

	  } else {

	      if (isExtendedDynamic_) {
		  if (url.indexOf("extended dynamic") == -1) {
		      String library = collection_;
		      grantPackagePermissions(url, library, userId_);

		      if (library == null)
			  library = DEFAULT_COLLECTION;
		      url += EXTENDED_DYNAMIC_OPTIONS_MINUS_LIBRARY + library
			+ ";libraries=" + library;
		  }
	      }
        String portNumber = System.getProperty("JDTestDriver.portNumber");
        if (portNumber != null) {
          url += ";portNumber=" + portNumber;
        }

        if (url.indexOf(":as400:") > 0) {
          // TODO:  Do not connect using driverManager for native Driver
          char[] pwdChars = PasswordVault.decryptPassword(encryptedPwd);
          connection = as400JdbcDriver_.connect(url, uid, pwdChars);
          PasswordVault.clearPassword(pwdChars); 

        } else {
          throw new Exception(":as400: not in url: " + url);
        }

      }
    }
    postConnectProcessing(connection, testInfo);
    return connection;
  }

  // Look at the stack to get the test information
  public static String calculateTestInfoFromStack() {
    StringBuffer exceptionInfo = new StringBuffer();
    StringBuffer testInfo = new StringBuffer();
    JDTestcase.printStackTraceToStringBuffer(new Exception(), exceptionInfo);
    String exceptionInfoString = exceptionInfo.toString();
    int searchStart = 0;
    for (int i = 0; i < 4; i++) {
      int atIndex = exceptionInfoString.indexOf("at ", searchStart);
      if (atIndex > 0) {
        int parenIndex = exceptionInfoString.indexOf("(", atIndex);
        if (parenIndex > 0) {
          searchStart = parenIndex;
          String methodName = exceptionInfoString.substring(atIndex + 3,
              parenIndex);
          if (methodName.indexOf("JDTestDriver.getConnection") > 0
              || methodName.indexOf("JDTestDriver.calculateTestInfoFromStack") > 0) {
            // Don't print this one
            i--;
          } else {
            if (i > 0)
              testInfo.append("_");
            if (methodName.indexOf("test.") == 0) {
              methodName = methodName.substring(5);
            }
            testInfo.append(methodName);
          }
        }
      }
    }

    return testInfo.toString();
  }

  public void postConnectProcessing(Connection connection, String testInfo)
    throws Exception {
      String dbmon = System.getProperty("dbmon");

      if (testInfo != null) {
      // Just execute the message as an SQL statement.
      // This will result in a token not value message in the job log
	  testInfo = testInfo.replace(' ', '_').replace('.', '_');
	  Statement stmt = connection.createStatement();
	  try {
	      stmt.executeUpdate("select * from INFOINFOINFO." + testInfo);
	  } catch (Exception e) {
	// Just ignore error
	  }
	  stmt.close();
      }
      if (dbmon != null) {
	  String command = "STRDBMON OUTFILE(" + dbmon + ") OUTMBR(*FIRST *ADD)  ";
	  try {
	      runCommand(connection, command, true);
	  } catch (Exception e) {
	      System.out.println("Failed " + e);
	      e.printStackTrace();
	      try {
		  runCommand(connection, command, false);
	      } catch (Exception ex) {
		  System.out.println("Failed " + ex);
		  ex.printStackTrace();
	      }
	  }

      }
      if (isExtendedDynamic_) {
      //
      // Override_qaqqini not available in V5R4
      //
	  if (getRelease() >= RELEASE_V6R1M0) {
	      Statement stmt = connection.createStatement();
	      try {
		  stmt.executeUpdate("call qsys2.override_qaqqini(1, '', '')");
		  stmt.executeUpdate("call qsys2.override_qaqqini(2, 'SQL_STMT_REUSE', '1')");
	      } catch (Exception e) {
		  String exceptionMessage = e.toString();
		  if (exceptionMessage
		      .indexOf("Qualified object name OVERRIDE_QAQQINI not valid") >= 0) {
		      try {
			  stmt.executeUpdate("call qsys2/override_qaqqini(1, '', '')");
			  stmt.executeUpdate("call qsys2/override_qaqqini(2, 'SQL_STMT_REUSE', '1')");

		      } catch (Exception e2) {
			  e2.printStackTrace();
		      }
		  } else {
		      e.printStackTrace();
		  }
	      }
	      stmt.close();
	  }

      }

      
      DatabaseMetaData dmd = connection.getMetaData();
      String databaseProductName = dmd.getDatabaseProductName();
      if (databaseProductName.indexOf("400") >= 0) { 

    // Always change the job to leave a joblog
	  Statement stmt = connection.createStatement();
	  String sql = ""; 
	  try {
	      sql = "call qsys2.qcmdexc('CHGJOB LOG(4 00 *SECLVL)     ')"; 
	      stmt.executeUpdate(sql);
	  } catch (Exception e) {	
	      System.out.println("Warning:  SQL failed -- "+sql);
	      e.printStackTrace(System.out);
	      try {
		  sql = "VALUES CURRENT USER"; 
		  ResultSet rs = stmt.executeQuery(sql); 
		  rs.next(); 
		  System.out.println("... current User is "+rs.getString(1)); 
		  rs.close(); 
	      } catch (SQLException sqlex) { 
		  System.out.println("Warning:  SQL failed -- "+sql);
		  e.printStackTrace(System.out);

	      }
	  }
	  stmt.close();
      }
  }

  void runCommand(Connection connection, String command, boolean SQLNaming)
      throws SQLException {
    String sql = "";
    try {
      Statement statement = connection.createStatement();
      try {
        sql = "CREATE PROCEDURE QCMDEXC(IN  CMDSTR VARCHAR(1024),IN  CMDLENGTH DECIMAL(15,5)) EXTERNAL NAME 'QSYS/QCMDEXC' LANGUAGE C GENERAL";
        statement.executeUpdate(sql);
      } catch (Exception e) {
      }

      System.out.println("Running command: " + command);
      sql = "CALL QCMDEXC(?,?)";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, command);
      pstmt.setInt(2, command.length());
      pstmt.executeUpdate();

      sql = "DROP PROCEDURE QCMDEXC";
      statement.executeUpdate(sql);
      statement.close();
    } catch (Exception e) {
      System.out.println("Exception on " + sql);
      e.printStackTrace();
    }
  }

  /**
   * Returns the platform that jdbc is running from
   * 
   * @return platform
   **/
  static public String getClientOS() {
    String os = System.getProperty("os.name");
    // System.out.println("Environment: " + os);

    if (os.toUpperCase().indexOf("400") >= 0)
      return CLIENT_as400;
    else if (os.toUpperCase().indexOf("WINDOWS") >= 0)
      return CLIENT_windows;
    else if (os.toUpperCase().indexOf("AIX") >= 0)
      return CLIENT_aix;
    else if (os.toUpperCase().indexOf("LINUX") >= 0)
      return CLIENT_linux;
    else
      return "???";

  }

  public static int getDriverFromString(String string) {
    string = string.trim();
    if (string.equals(DRIVER_STRING_TOOLBOX)) {
      return DRIVER_TOOLBOX;
    } else if (string.equals(DRIVER_STRING_JT400ANDROID)) {
      return DRIVER_TOOLBOX;
    } else if (string.equals(DRIVER_STRING_JTOPENPX)) {
      return DRIVER_TOOLBOX;
    } else if (string.equals(DRIVER_STRING_JTOPENCA)) {
      return DRIVER_TOOLBOX;
    } else if (string.equals(DRIVER_STRING_JTOPENSF)) {
      return DRIVER_TOOLBOX;
    } else if (string.equals(DRIVER_STRING_NATIVE)) {
      return DRIVER_NATIVE;
    } else if (string.equals(DRIVER_STRING_JCC)) {
      return DRIVER_JCC;
    } else if (string.equals(DRIVER_STRING_JTOPENLITE)) {
      return DRIVER_JTOPENLITE;
    } else {
      return DRIVER_NONE;
    }

  }

  public static int getSubDriverFromString(String string) {
    string = string.trim();
    if (string.equals(DRIVER_STRING_TOOLBOX)) {
      return DRIVER_TOOLBOX;
    } else if (string.equals(DRIVER_STRING_JT400ANDROID)) {
      return SUBDRIVER_JT400ANDROID;
    } else if (string.equals(DRIVER_STRING_JTOPENCA)) {
      return SUBDRIVER_JTOPENCA;
    } else if (string.equals(DRIVER_STRING_JTOPENSF)) {
      return SUBDRIVER_JTOPENSF;
    } else if (string.equals(DRIVER_STRING_JTOPENPX)) {
      return SUBDRIVER_JTOPENPX;
    } else if (string.equals(DRIVER_STRING_NATIVE)) {
      return DRIVER_NATIVE;
    } else if (string.equals(DRIVER_STRING_JCC)) {
      return DRIVER_JCC;
    } else if (string.equals(DRIVER_STRING_JTOPENLITE)) {
      return DRIVER_JTOPENLITE;
    } else {
      return DRIVER_NONE;
    }

  }

  public void stop() {

  }

  static Hashtable tableDefinitions = new Hashtable();

  static void initTable(Statement s, String tableName, String tableDefinition)
      throws SQLException {
    initTable(s, tableName, tableDefinition, null);
  }

  static void initTable(Statement s, String tableName, String tableDefinition,
      StringBuffer sb) throws SQLException {
    tableDefinition = tableDefinition.trim();
    String oldDefinition = (String) tableDefinitions.get(tableName);
    if (oldDefinition == null) {
      tableDefinitions.put(tableName, tableDefinition);
    } else {
      if (!oldDefinition.equals(tableDefinition)) {
        throw new SQLException("ERROR:  definition of " + tableName + "\n"
            + tableDefinition + "\nCHANGED FROM\n" + oldDefinition);
      }
    }
    String sql;
    try {
      sql = "DELETE FROM " + tableName;
      if (sb != null)
        sb.append(sql + "\n");
      s.execute(sql);
    } catch (SQLException e) {
      if (sb != null)
        sb.append("Exception caught " + e + " deleting rows from table\n");
      if (e.getErrorCode() == -204) {
        // If the file was not found create it.

        sql = "CREATE TABLE " + tableName + tableDefinition;
        if (sb != null)
          sb.append("Running "+sql + "\n");
        try {
          s.executeUpdate(sql);

	  sql = "GRANT ALL ON "+tableName+" TO PUBLIC";
	  if (sb != null)
	      sb.append("Running "+sql + "\n");
	  s.executeUpdate(sql);

        } catch (SQLException e2) {
          int code = e2.getErrorCode();
          if (code == -901) {
            System.out
                .println("SQL0901 found -- trying to recover using RCLDBXREF");
            s.executeUpdate("CALL QSYS2.QCMDEXC(' RCLDBXREF OPTION(*FIX) ')");
            s.executeUpdate(sql);
          } else {
            throw e2;
          }

        }
      } else {
        // otherwise return the error
        throw e;
      }
    }
  }

  void cleanupTable(Statement s, String tableName) {
    if (!skipCleanup) {
      try {
        s.executeUpdate("DROP TABLE " + tableName);
      } catch (SQLException e) {
        if (e.getErrorCode() == -204
            || (e.getMessage().indexOf("not found") >= 0)) {
          // It is ok if the file is not found
        } else {
          System.out.println("Warning:  error dropping table ");
          e.printStackTrace(System.out);

        }

      }
    }
  }

  static Hashtable triggerDefinitions = new Hashtable();

  static void initTrigger(Statement s, String triggerName,
      String triggerDefinition, StringBuffer sb) throws SQLException {
    triggerDefinition = triggerDefinition.trim();
    String oldDefinition = (String) triggerDefinitions.get(triggerName);
    if (oldDefinition == null) {
      triggerDefinitions.put(triggerName, triggerDefinition);
    } else {
      if (!oldDefinition.equals(triggerDefinition)) {
        throw new SQLException("ERROR:  definition of " + triggerName + "\n"
            + triggerDefinition + "\nCHANGED FROM\n" + oldDefinition);
      }
    }
    String sql;
    try {
      sql = "CREATE TRIGGER " + triggerName + " " + triggerDefinition;
      if (sb != null)
        sb.append(sql + "\n");
      s.executeUpdate(sql);
    } catch (SQLException e) {
      if (e.getErrorCode() == -601) {
        // If it already exists, just ignore it
      } else {
        // otherwise return the error
        throw e;
      }
    }
  }

  void cleanupTrigger(Statement s, String triggerName) {
    if (!skipCleanup) {
      try {
        s.executeUpdate("DROP TRIGGER " + triggerName);
      } catch (SQLException e) {
        if (e.getErrorCode() == -204
            || (e.getMessage().indexOf("not found") >= 0)) {
          // It is ok if the file is not found
        } else {
          System.out.println("Warning:  error dropping table ");
          e.printStackTrace(System.out);

        }

      }
    }
  }

}
