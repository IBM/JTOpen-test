///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDJSQL400.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;

import java.sql.*;
import java.sql.Date;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.zip.CRC32;
import java.net.URL;
import java.net.URLClassLoader;

import javax.sql.XAConnection;

public class JDSQL400 implements Runnable {
  public static String[] usage = {
      "JDSQL400 interprets SQL commands using a JDBC connection.  ",
      "Besides SQL, the following COMMANDS and SUBCOMMANDS are available", "",
      "USAGE                          Displays this information",
      "PREPARE [sql statement]        prepares an sql statement",
      "SETPARM [index],[value]        Sets the parameter for the sql statement",
      "SETPARMFROMVAR [index],[var]   Sets the parameter using a variable",
      "SETRESULTSETTYPE [..]          Sets the results set type for prepare",
      "SETRESULTSETCONCURRENCY [..]   ..", "SETRESULTSETHOLDABILITY [..]   ..",
      "REUSE STATEMENT [true|false]    Should the stmt object be reused",
      "EXECUTEQUERY                   invokes executeQuery on the prepared statement",
      "EXECUTEUPDATE                  invokes executeUpdate on the prepared statement",
      "ECHO [string]                  echos the string",
      "-- [string]                    same as echo",
      "SETQUERYTIMEOUT [number]       sets the query timeout for subsequent statements",
      "RESET CONNECTION PROPERTIES    resets connection properties",
      "ADD CONNECTION PROPERTY        set properties to be used on subsequent connects",
      "REUSE CONNECTION [true|false]  should the connection be reused by connect to",
      "CONNECT TO URL [URL] [USERID=..] [PASSWORD=...]  connect using the URL",
      "CONNECT TO [schema]            connect using the specified schema",
      "STDXACONNECT TO [schema]       connect using stdXA datasource to specified schema",
      "CONNECT RESET                  closes the current connection",
      "CALL ... -- INPARM [p1]....    Calls the stored procedure with the specified parameters",
      "EXISTFILE                      Indicates if the specified file exists (on the client)",
      "GC                             Force the Java garbage collector to run",
      "OUTPUT FORMAT [xml | html]     Set the output format to with XML or HTML",
      "SHOWMIXEDUX [true | false]     Set if mixed UX strings will be displayed",
      "SHOWCHARNAME [true | false]    Set certain char names are to be displayed",
      "SET AUTOCOMMIT [true|false]    Sets the autocommit value",
      "SET TRANSACTIONISOLATION [VALUE] Sets the autocommit value",
      "                                 Supported values are ",
      "                               TRANSACTION_READ_UNCOMMITTED",
      "                               TRANSACTION_READ_COMMITTED",
      "                               TRANSACTION_REPEATABLE_READ",
      "                               TRANSACTION_SERIALIZABLE",
      "DUMPJOBLOG [sqlcode|any|none]  Causes the joblog to be dumped if the sqlcode is encountered",
      "LASTJOBLOG [n]                 If DUMPJOBLOG set, only dump the last n messages",
      "DUMPTRACE [sqlcode|any|none]   Causes the jdbctrace to be dumped if the sqlcode is encountered.",
      "                               Will only work if true is globally turned on",
      "PRINTSTACKTRACE [on|off]       Causes exception stack trace to be printed",
      "GETSERVERJOBNAME               Returns connection.getServerJobName",
      "CLOSESTATEMENTRS [on|off]      Close statement and result set after execution of query default off",
      "MEASUREEXECUTE [on|off]        Measure time to do execute",
      "CHARACTERDETAILS [on|off]      Turn on to see entire character details -- default of off",
      "SHOWLOBTHRESHOLD [value]       Set the value at which lob details will not be printed.  Default is 4096",

      "MANUALFETCH [on|off]           Set if manual fetch operations should be used",
      "RS.NEXT,RS.FIRST, RS.LAST, RS.PREVIOUS, RS.ABSOLUTE pos, RS.RELATIVE pos, RS.BEFOREFIRST, RS.AFTERLAST",
      "                               Call rs.next,... for manually fetching",
      "DMD.GETCOLUMNS catalog, schemaPattern, tableNamePattern, columnNamePattern ",
      "DMD.GETTABLES catalog, schemaPattern, tableNamePattern, type1 | type2",
      "DMD.GETINDEXINFO catalog, schema, table, booleanUnique, booleanApproximate ",
      "DMD.GETSCHEMAS",

      "", "Parameters may be specified in the following formats",
      "UX'....'                       Unicode string (in hexadecimal)",
      "X'....'                        Byte array (in hexademical)",
      "FILEBLOB=<filename>            A Blob retrieved from the named file",
      "FILECLOB=<filename>            A clob retrieved from the named file",
      "SAVEDPARM=<number>             A parameter from a previous CALL statement",
      "GEN_BYTE_ARRAY+<count>         A generated byte array of count bytes",
      "GEN_HEX_STRING+<count>         A generated hex string",
      "GEN_CHAR_ARRAY+<count>C<ccsid> A generated character string",
      "SQLARRAY[TYPE:e1:e2:...]       A JAVA.SQL.ARRAY type",
      "                               Types are String:BigDecimal:Date:Time:Timestamp:Blob:Clob:int:short:long:float:double:byteArray",
      "SQLARRAY[Date:e1 e2 ...]       A JAVA.SQL.ARRAY with data blank sep",
      "SQLARRAY[Timestamp:e1|e2 ...]  A JAVA.SQL.ARRAY with timestamp | sep",

      "", "The following prefixes are available",
      "INVISIBLE:     The command and its results are not echoed",
      "SILENT:        The results of the command are not echoed", "",
      "The following debugger commands are available",
      "DEBUGGER.ATTACH [<port>]         Attaches the debugger, in a separate thread, at port. ",
      "                                 If port is not set then saved parm #2 is used",
      "DEBUGGER.STOP                    Stops the debugger thread",
      "DEBUGGER.SHOWVAR <variablename>  Show the variable name after a breakpoint is hit",
      "DEBUGGER.SHOWLINELOCATIONS <class> Shows the line for the class",
      "DEBUGGER.BREAK <break>           Sets the breakpoint.  When the breakpoint is hit, the",
      "                                 stack and variable will be shown and then the code will continue",
      "DEBUGGER.THREADSTART             Sets a thread start event",
      "DEBUGGER.THREADDEATH             Sets a thread death event",
      "DEBUGGER.METHODENTRY             Sets method entry events",
      "DEBUGGER.METHODEXIT              Sets method exit events",
      "DEBUGGER.UNCAUGHTEXCEPTION       Sets UNCAUGHT exception events",
      "DEBUGGER.SHOWTHREADS [count]     Shows the threads in the JVM",
      "HISTORY.CLEAR                    Clears the stored history",
      "HISTORY.SHOW                     Shows the history of commands",
      "PRESERVE_SEMICOLONS [true|false] Should semicolons be preserved",
      "SETCLITRACE [true|false]         Sets CLI tracing -- valid V5R5 and later",
      "SETDB2TRACE [0|1|2|3|4]          Sets jdbc tracing -- valid V5R5 and later",

      "",
      "SETVAR [VARNAME] = [METHODCALL]  Sets a variable use a method.. i.e. ",
      "                                 SETVAR BLOB = RS.getBlob(1)",
      "SETVAR [VARNAME] [PARAMETER SPECIFICATION] Sets a variable using a parameter specification",
      "SETNEWVAR [VARNAME] = [CONSTRUCTORCALL]  Sets a variable by calling the contructor",
      "                                 SETNEWVAR DS = com.ibm.db2.jdbc.app.UDBDataSource()",
      "SHOWVARMETHODS [VARNAME]         Shows the methods for a variable",

      "CALLMETHOD [METHODCALL]          Calls a method on a variable",
      "  Hint:  To see a result set use CALLMETHOD test.JDSQL400.dispResultSet(RS)",
      "",
      "PRINTCALLMETHODSTACKTRACE [on|off]  Causes exception stack trace from CALLMETHOD to be printed",
      "THREAD [COMMAND]                 Runs a command in its own thread.",
      "THREADPERSIST [THREADNAME]       Create a thread that persist.",
      "THREADEXEC [THREADNAME] [COMMAND] Execute a command in a persistent thread.",
      "REPEAT [NUMBER] [COMMAND]        Repeat a command a number of times.",
      "LEAKVAR                          Causes versions of SETVAR to be stored in Hashtable variable named LEAKVAR",
      "" };

  public static boolean html = false;
  public static boolean xml = false;
  public static boolean echoOff = false;
  public static boolean echoComments = false;
  private static boolean debug = false;
  private static boolean prompt = false;
  private static boolean hideWarnings = false;
  private static boolean jdk14 = false;
  private static boolean jdk16 = false;
  private static boolean characterDetails = false;
  private static String system = "localhost";
  private static String url = "";
  private static String baseUrlArgs = "";
  private static String urlArgs = "";
  public static String schema = null;
  private static String userId = null;
  private static String password = null;
  private static Connection con = null;
  private static PreparedStatement pstmt = null;
  private static CallableStatement cstmt = null;
  private static String cstmtSql = "";
  private static Statement stmt = null;
  private static ResultSet manualResultSet = null;
  private static int manualResultSetNumCols = 0;
  private static int manualResultSetColType[] = null;
  private static String manualResultSetColumnLabel[];
  private static boolean manualFetch = false;
  private static String conName = null;
  private static String conLabel = null;
  private static int conCount = 0;
  private static Vector replaceFrom = new Vector();
  private static Vector replaceTo = new Vector();
  private static Properties properties = null;
  private static Hashtable dumpCodes = null;
  private static boolean dumpAnyError = false;
  private static Hashtable dumpTraceCodes = null;
  private static boolean dumpTraceAnyError = false;
  private static boolean dumpTaken = false;
  private static int dumpNMessages = 0;

  private static char sep = '/';
  private static int queryTimeout = 0;
  private static int showLobThreshold = 4096;
  private static boolean showMixedUX = false;
  private static boolean showCharNames = false;
  private static int stringSampleSize = 256;
  private static String savedStringParm[] = new String[256];
  private static Object debugger = null;
  private static boolean closeStatementRS = true;
  private static boolean measureExecute = false;
  private static boolean printStackTrace = false;
  private static boolean printCallMethodStackTrace = true;
  private static long startTime = 0;
  private static long finishTime = 0;
  private static int resultSetType = ResultSet.TYPE_FORWARD_ONLY;
  private static int resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;
  private static int resultSetHoldability = ResultSet.HOLD_CURSORS_OVER_COMMIT;
  public static boolean preserveSemicolons = false;
  private static boolean silent = false;

  private static boolean toolboxDriver = false;
  private static boolean jccDriver = false;
  //
  // Optimization for using a connection pool
  //
  private static boolean useConnectionPool = false;
  private static boolean reuseStatement = false;
  private static Connection poolConnection = null;
  private static boolean poolConnectionSql0901 = false;
  private static String poolUserId = null;
  private static String poolPassword = null;
  private static String poolUrl = null;
  private static Hashtable connectionPool = new Hashtable();
  private static Hashtable variables = new Hashtable();
  private static Hashtable leakvarHashtable = null;

  private static void putUserVariable(String key, Object value) {

    variables.put(key, value);
    if (key.equals("LEAKVAR")) {
    } else {
      // Leakit
      if (leakvarHashtable != null) {
        leakvarHashtable.put(value, value);
      }
    }
    if (key.equals("CON")) {
      if (value instanceof Connection) {
        con = (Connection) value;
      }
    } else if (key.equals("RS")) {
      if (value instanceof ResultSet) {
        manualResultSet = (ResultSet) value;
      }
    } else if (key.equals("STMT")) {
      if (value instanceof Statement) {
        stmt = (Statement) value;
      }
    } else if (key.equals("PSTMT")) {
      if (value instanceof PreparedStatement) {
        pstmt = (PreparedStatement) value;
      }
    } else if (key.equals("CSTMT")) {
      if (value instanceof CallableStatement) {
        cstmt = (CallableStatement) value;
      }
    }

  }

  private static Vector threads = new Vector();

  private static Vector history = new Vector();

  private static java.sql.Driver toolboxDriverReference = null;

  public static byte[] createByteArray(int size) {
    return new byte[size];
  }

  public static String stringArrayContents(String[] arg) {
    if (arg == null)
      return "null";
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    int size = arg.length;
    for (int i = 0; i < size; i++) {
      sb.append(arg[i]);
      if (i < size - 1) {
        sb.append(",");
      }
    }

    sb.append("]");

    return sb.toString();
  }

  public static void useConnectionPool(boolean value) {
    useConnectionPool = value;
  }

  public static boolean getUseConnectionPool() {
    return useConnectionPool;
  }

  public static void setProperties(Properties p) {
    properties = p;
  }

  public static void setUrl(String newUrl) {
    url = newUrl;
    toolboxDriver = false;
    jccDriver = false;

    if (url.indexOf(":as400:") > 0) {
      toolboxDriver = true;
    }
    if (url.indexOf(":db2://") > 0) {
      jccDriver = true;
    }

  }

  public static void setUserId(String newUserId) {
    if (debug)
      System.out.println("User ID set to " + newUserId);
    userId = newUserId;
  }

  public static void setPassword(String newpassword) {
    password = newpassword;
  }

  //
  // remove properties from the string so that they are not
  // duplicated.
  //
  public static String removeProperty(String url1, String newProperties) {
    int equalsIndex = newProperties.indexOf("=");
    while (equalsIndex > 0) {
      // System.out.println("Removing "+newProperties+" from "+url);
      String property = newProperties.substring(0, equalsIndex).trim();

      //
      // fix url
      //
      int propertyIndex = url1.indexOf(property);
      while (propertyIndex > 0) {
        int semicolonIndex = url1.indexOf(";", propertyIndex);
        if (semicolonIndex > 0) {
          url1 = url1.substring(0, propertyIndex).trim()
              + url1.substring(semicolonIndex + 1).trim();
        } else {
          url1 = url1.substring(0, propertyIndex).trim();
        }
        propertyIndex = url1.indexOf(property);
      }

      // adjust new properties
      int semicolonIndex = newProperties.indexOf(";", equalsIndex);
      if (semicolonIndex > 0) {
        newProperties = newProperties.substring(semicolonIndex + 1);
      } else {
        newProperties = "";
      }
      equalsIndex = newProperties.indexOf("=");
    }
    // if the url ends with a ; remove it
    if (!preserveSemicolons) {
      int urlLength = url1.length();
      while ((urlLength > 0) && (url1.charAt(urlLength - 1) == ';')) {
        url1 = url1.substring(0, urlLength - 1).trim();
        urlLength = url1.length();
      }
    }
    return url1;
  }

  //
  // Static initializer
  //
  static {
    setup();
  }

  static void setup() {

    //
    // look for jdk1.4
    //
    String version = System.getProperty("java.version");
    if (version != null) {
      if (version.charAt(0) == '1' && version.charAt(1) == '.'
          && version.charAt(2) < '4') {
        jdk14 = false;
        jdk16 = false;
      } else {
        if (version.charAt(0) == '1' && version.charAt(1) == '.'
            && version.charAt(2) < '6') {
          jdk14 = true;
          jdk16 = false;
        } else {
          jdk14 = true;
          jdk16 = true;

        }

      }
    }

    String sepString = System.getProperty("file.separator");
    sep = sepString.charAt(0);

    //
    // Look for a debug property
    //
    String propertyString;
    try {
      propertyString = System.getProperty("debug");
      if (propertyString != null) {
        propertyString = propertyString.toUpperCase().trim();
        if (propertyString.equals("TRUE")) {
          debug = true;
        } else {
          if (propertyString.equals("FALSE")) {
            debug = false;
          }
        }
      }
    } catch (Exception dontCare) {
    }

    try {
      propertyString = System.getProperty("preserveSemicolons");
      if (propertyString != null) {
        propertyString = propertyString.toUpperCase().trim();
        if (propertyString.equals("TRUE")) {
          preserveSemicolons = true;
        } else {
          if (propertyString.equals("FALSE")) {
            preserveSemicolons = false;
          }
        }
      }
    } catch (Exception dontCare) {
    }

    try {
      propertyString = System.getProperty("showLobThreshold");
      if (propertyString != null) {
        showLobThreshold = Integer.parseInt(propertyString);
      }
    } catch (Exception dontCare) {
    }

    try {
      propertyString = System.getProperty("showMixedUX");
      if (propertyString != null) {
        propertyString = propertyString.toUpperCase().trim();
        if (propertyString.equals("TRUE")) {
          showMixedUX = true;
        } else {
          if (propertyString.equals("FALSE")) {
            showMixedUX = false;
          }
        }
      }
    } catch (Exception dontCare) {
    }

    try {
      propertyString = System.getProperty("showCharNames");
      if (propertyString != null) {
        propertyString = propertyString.toUpperCase().trim();
        if (propertyString.equals("TRUE")) {
          showCharNames = true;
        } else {
          if (propertyString.equals("FALSE")) {
            showCharNames = false;
          }
        }
      }
    } catch (Exception dontCare) {
    }

    // See what drivers are available..
    if (debug) {
      Enumeration drivers = DriverManager.getDrivers();
      System.out.println("The available drivers are");
      System.out.println("----------------------------");
      while (drivers.hasMoreElements()) {
        System.out.println(drivers.nextElement().getClass().getName());
      }
      System.out.println("----------------------------");
    }

    // Load the jdbc-odbc bridge driver if not on 400. Activating this on the
    // 400 causes the JVM to crash.
    String os = System.getProperty("os.name");

    if (!os.equals("OS/400")) {
      try {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        url = "jdbc:odbc:" + system;
        //
        // For NT, this is the name of the default source.. I used the default
        // one -sample
        // java sql400 sample jeber password
        //
      } catch (Throwable e) {
        if (debug)
          System.out.println("sun.jdbc.odbc.JdbcOdbcDriver not found");
      }
    }

    try {
      Class.forName("com.ibm.db2.jcc.DB2Driver");
      String jccPort = System.getProperty("jccPort");
      if (jccPort == null)
        jccPort = "446";
      String jccDatabase = System.getProperty("jccDatabase");
      if (jccDatabase == null)
        jccDatabase = "*LOCAL";
      url = "jdbc:db2://" + system + ":" + jccPort + "/" + jccDatabase;
      System.out.println("URL set for jcc = " + url);
      jccDriver = true;
      toolboxDriver = false;
      urlArgs = "";
      baseUrlArgs = urlArgs;

    } catch (Throwable e) {
      if (debug)
        System.out.println("com.ibm.db2.jcc.DB2Driver not found ");
    }

    String noNative = null;
    try {
      noNative = System.getProperty("noNative");
    } catch (java.lang.SecurityException secEx) {
      // Ignore
    }

    try {
      Class.forName("com.ibm.as400.access.AS400JDBCDriver");
      url = "jdbc:as400://" + system;
      urlArgs = "; date format=iso; time format=iso";
      baseUrlArgs = urlArgs;
      toolboxDriver = true;
    } catch (Throwable e) {
      if (debug) {
        System.out
            .println("Debug:  com.ibm.as400.access.AS400JDBCDriver not found ");
        e.printStackTrace();
      }

      if (noNative != null) {
        if (debug) {
          System.out.println("Debug: Attempting to load toolbox driver");
        }
        // Force the toolbox driver to be loaded anyway
        // This way, I don't need to remember the path..
        // This doens't seem to work. The driver manager doesn't like the
        // class that is loaded from another classloader. Think about this
        //

        if (os.equals("OS/400")) {
          String[] jarLocations = {
              "/qibm/proddata/http/public/jt400/lib/jt400.jar" };
          String[] jarLocations16 = {
              "/QIBM/proddata/OS400/jt400/lib/java6/jt400.jar",
              "/qibm/proddata/http/public/jt400/lib/jt400.jar" };
          if (jdk16) {
            jarLocations = jarLocations16;
          }

          URL[] urls = new URL[1];
          for (int i = 0; i < jarLocations.length && urls[0] == null; i++) {
            File tryFile = new File(jarLocations[i]);
            if (tryFile.exists()) {
              try {
                urls[0] = new URL("file:" + jarLocations[i]);
              } catch (Exception e3) {
                e3.printStackTrace();
              }
            }
          }

          ClassLoader loader = new URLClassLoader(urls);
          try {
            Class c = loader.loadClass("com.ibm.as400.access.AS400JDBCDriver");
            if (debug) {
              System.out.println("Debug: Class loaded " + c);
            }
            url = "jdbc:as400://" + system;
            urlArgs = "; date format=iso; time format=iso";
            baseUrlArgs = urlArgs;
            toolboxDriver = true;
            toolboxDriverReference = (java.sql.Driver) c.newInstance();
            DriverManager.registerDriver(toolboxDriverReference);
          } catch (Exception e2) {
            System.out.println(
                "Error.. unable to load toolbox driver when runnong on OS/400 \n"
                    + "Urls=" + urls[0] + "\n" + "jarLocations="
                    + jarLocations[0] + "\n");
            e2.printStackTrace();
          }
        } /* OS/400 */
      } /* nonative != null */
    } /* driver not found */

    //
    // Don't load this if running on a PC
    //
    String osname = System.getProperty("os.name");
    if (debug) {
      System.out.println("osname is " + osname);
    }

    if (osname.equals("Windows XP") || (noNative != null)
        || osname.equals("AIX")) {
      // Don't even try loading on other platforms
    } else {
      try {
        Class.forName("com.ibm.db2.jdbc.app.DB2Driver");
        if (debug)
          System.out.println("com.ibm.db2.jdbc.app.DB2Driver loaded");
        url = "jdbc:db2:" + system;
        toolboxDriver = false;
      } catch (Throwable e) {
        if (debug)
          System.out.println("com.ibm.db2.jdbc.app.DB2Driver not found ");
        if (debug)
          e.printStackTrace();
      }
    }
    try {
      Class.forName("com.ibm.db2.jdbc.net.DB2Driver");
    } catch (Throwable e) {
      if (debug)
        System.out.println("com.ibm.db2.jdbc.net.DB2Driver not found ");
    }

    try {
      if ("true".equals(
          System.getProperty("jdbc.db2.restricted.local.connection.only"))) {
        url = "jdbc:default:connection";
      }
    } catch (java.lang.SecurityException secEx) {
      // Ignore
    }

    Enumeration drivers = DriverManager.getDrivers();
    if (debug) {
      System.out.println("The available drivers are");
      System.out.println("----------------------------");
    }
    while (drivers.hasMoreElements()) {
      Driver driver = (Driver) drivers.nextElement();
      String driverName = driver.getClass().getName();
      if (driverName.equals("com.ibm.db2.jdbc.app.DB2ErrorDriver")) {
        System.out.println(
            "ERROR:  DB2ErrorDriver found -- leaving as valid driver ");
        try {
          // DriverManager.deregisterDriver(driver);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (debug) {
        System.out.println(driverName);
      }
    }
    if (debug) {
      System.out.println("----------------------------");
    }

  }

  public static void setConName(String inFile) {
    conCount = 0;

    int sepIndex = inFile.lastIndexOf(sep);
    if (sepIndex >= 0) {
      conName = inFile.substring(sepIndex + 1);
    } else {
      conName = inFile;
    }
    //
    // Get rid of .clp
    //
    int dotIndex = conName.lastIndexOf('.');
    if (dotIndex >= 0) {
      conName = conName.substring(0, dotIndex);
    }

    // try to shorten the name

    if (conName.length() > 10) {
      int i;
      //
      // Delete .v
      //
      i = conName.indexOf(".v");
      if (i > 0) {
        conName = conName.substring(0, i) + conName.substring(i + 2);
      }

      //
      // delete uick
      //
      i = conName.indexOf("uick");
      if (i > 0) {
        conName = conName.substring(0, i) + conName.substring(i + 4);
      }

    }

  }

  //
  // Run a test script from an input file and output to an ouptut file
  //
  public static void run(String inFile, String outFile) {
    prompt = false;

    if (sep != '/') {
      inFile = inFile.replace('/', sep);
      outFile = outFile.replace('/', sep);
      if (inFile.charAt(0) == sep) {
        inFile = "C:" + inFile;
      }
      if (outFile.charAt(0) == sep) {
        outFile = "C:" + outFile;
      }

    }

    try {
      //
      // Open a print stream for the output file
      //

      PrintStream out = new PrintStream(new FileOutputStream(outFile));

      //
      // Open a input stream for the input
      //

      InputStream in = new FileInputStream(inFile);

      //
      // Use the default values
      // userId = null;
      // password = null;
      setConName(inFile);

      run(in, out);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void run(String inFile, String outFile, String inUserId,
      String inPassword) {
    prompt = false;

    try {

      //
      // Open a print stream for the output file
      //

      PrintStream out = new PrintStream(new FileOutputStream(outFile));

      //
      // Open a input stream for the input
      //

      InputStream in = new FileInputStream(inFile);

      userId = inUserId;
      password = inPassword;

      setConName(inFile);

      run(in, out);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void run(InputStream in, PrintStream out) {

    boolean running = true;
    String query;

    try {
      //
      // Set up the replace properties. They all start with REPLACE_
      //
      replaceFrom = new Vector();
      replaceTo = new Vector();

      Properties systemProperties = System.getProperties();
      Enumeration enumeration = systemProperties.propertyNames();
      while (enumeration.hasMoreElements()) {
        String key = (String) enumeration.nextElement();
        if (key.indexOf("REPLACE_") == 0) {
          String value = systemProperties.getProperty(key);
          String fromString = key.substring(8);
          replaceFrom.addElement(fromString);
          replaceTo.addElement(value);
        }
      }

      if (properties != null) {
        enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
          String key = (String) enumeration.nextElement();
          if (key.indexOf("REPLACE_") == 0) {
            String value = properties.getProperty(key);
            String fromString = key.substring(8);
            replaceFrom.addElement(fromString);
            replaceTo.addElement(value);
          }
        }

      }

      BufferedReader input = new BufferedReader(new InputStreamReader(in));

      if (prompt)
        out.print("sql400>");
      query = input.readLine();
      if (query == null) {
        query = "exit";
      }
      //
      // remove semicolons if it exists and line does not contain \
      //

      // Join lines together
      while (query.endsWith("\\")) {
        query = query.substring(0, query.length() - 1);
        query = query + " " + input.readLine();
      }

      if (!preserveSemicolons) {
        if (query.indexOf("\\;") == -1) {
          query = query.replace(';', ' ').trim();
        } else {
          query = query.replace('\\', ' ').trim();
        }
      }
      dumpCodes = new Hashtable();
      dumpAnyError = false;
      dumpNMessages = 0;
      dumpTraceCodes = new Hashtable();
      dumpTraceAnyError = false;
      putUserVariable("OUT", out);

      while (running) {
        running = executeCommand(query, out);
        if (running) {
          if (prompt)
            out.print("sql400>");
          query = input.readLine();
          if (query != null) {

            while (query.endsWith("\\")) {
              query = query.substring(0, query.length() - 1);
              String newLine = input.readLine();
              if (newLine != null) {
                query = query + " " + newLine;
              }
            }

            //
            // remove semicolons if it exists and line does not contain \
            //
            int position = 0;
            if (!preserveSemicolons) {
              int index = query.indexOf(';', position);

              while (index > 0) {
                if (query.charAt(index - 1) == '\\') {
                  // remove / and keep semicolon
                  query = query.substring(0, index - 1) + " "
                      + query.substring(index);
                  position = index + 1;
                } else {
                  // remove ;
                  query = query.substring(0, index) + " "
                      + query.substring(index + 1);
                  position = index + 1;
                }
                index = query.indexOf(';', position);
              }
            }

            query = query.trim();

          } else {
            // EOF found
            running = false;
          }
        }
      }
      //
      // Be sure to clean up
      //
      if (con != null) {
        JDJSTPTestcase.connectionDisconnect(con, conLabel);
        if (con == poolConnection) {
          // Dont close our one poolConnection
          // It may be reused on the next run
        } else {
          con.close();
          stmt = null;
          cstmt = null;
          cstmtSql = "";
        }
        con = null;
      }
    } catch (Exception e) {
      e.printStackTrace(out);
    } catch (java.lang.UnknownError jlu) {
      jlu.printStackTrace(out);
    }

  }

  public static void setManualResultSetColType(ResultSetMetaData rsmd)
      throws SQLException {
    manualResultSetColType = new int[manualResultSetNumCols + 1];
    int i;
    for (i = 1; i <= manualResultSetNumCols; i++) {
      manualResultSetColType[i] = rsmd.getColumnType(i);
    }

  }

  //
  // Job log filter
  //
  public static String filter1[] = {
      "*NONE      Information                  ", /* 0 */
      "From user . . . . . . . . . :", /* 1 */
      "From module . . . . . . . . :", /* 2 */
      "From procedure  . . . . . . :", /* 3 */
      "Statement . . . . . . . . . :", /* 4 */
      "To module . . . . . . . . . :", /* 5 */
      "To procedure  . . . . . . . :", /* 6 */
      "Statement . . . . . . . . . :", /* 7 */
      "Thread  . . . . :", /* 8 */
      "THISWILLNEVERMATCHBECAUSEITDOESNTEXIST" /* 9 */
  };

  public static String filter2[] = { "Display Job Log", /* 0 */
      "Job name . . . . . . . . . . :", /* 1 */
      "Job description  . . . . . . :", /* 2 */
      "MSGID      TYPE                    SEV  DATE      TIME", /* 3 */
      "THISWILLNEVERMATCHBECAUSEITDOESNTEXIST", /* 4 */
      "THISWILLNEVERMATCHBECAUSEITDOESNTEXIST", /* 5 */
      "THISWILLNEVERMATCHBECAUSEITDOESNTEXIST", /* 6 */
      "THISWILLNEVERMATCHBECAUSEITDOESNTEXIST", /* 7 */
      "THISWILLNEVERMATCHBECAUSEITDOESNTEXIST", /* 8 */
      "THISWILLNEVERMATCHBECAUSEITDOESNTEXIST" /* 9 */
  };

  public static String[] filters[] = { filter1, filter2, null };

  public static Connection getPooledConnection(String thisConnectUserId,
      String thisConnectPassword, String connectUrl, PrintStream out)
      throws SQLException {
    //
    // if an sql0901 occurred while pooled connection was being used, discard
    // it.
    //
    if (poolConnectionSql0901) {
      try {
        poolConnection.close();
      } catch (Exception e) {
      }
      poolConnection = null;
      poolConnectionSql0901 = false;
    }
    if ((poolConnection != null) && thisConnectUserId.equals(poolUserId)
        && thisConnectPassword.equals(poolPassword)
        && (connectUrl.equals(poolUrl))) {
      con = poolConnection;
      if (con != null) {
        variables.put("CON", con);
      }
      dumpTaken = false;
    } else {
      if (poolConnection != null) {
        String key = poolUserId + "." + poolPassword + "." + poolUrl;
        // return to the pool
        connectionPool.put(key, poolConnection);
        if (debug)
          out.println("Added connection to pool for " + key);
      }
      // Try to get one from the pool
      String key = thisConnectUserId + "." + thisConnectPassword + "."
          + connectUrl;
      con = (Connection) connectionPool.get(key);
      dumpTaken = false;
      if (con != null) {
        variables.put("CON", con);
        if (debug)
          out.println("Retrieved connection from pool for " + key);
        connectionPool.remove(key);
      } else {
        if (debug)
          out.println("Didn't retrieve connection from pool for " + key);
        if (thisConnectUserId.equals("null")
            && thisConnectPassword.equals("null")) {
          con = DriverManager.getConnection(connectUrl);
        } else {
          con = DriverManager.getConnection(connectUrl, thisConnectUserId,
              thisConnectPassword);
        }
        variables.put("CON", con);

      }
      poolConnection = con;
      poolUserId = thisConnectUserId;
      poolPassword = thisConnectPassword;
      poolUrl = connectUrl;

    }
    return con;
  }

  public static String getServerJobName(Connection con1, PrintStream out) {
    String jobName = "";

    try {
      jobName = JDReflectionUtil.callMethod_S(con1, "getServerJobName");
    } catch (java.lang.NoSuchMethodException nsme) {
      try {
        // Check for toolbox Driver
        DatabaseMetaData dmd = con1.getMetaData();
        String driverName = dmd.getDriverName();
        if (driverName.indexOf("Toolbox") >= 0
            || driverName.indexOf("jtopenlite") >= 0) {
          try {
            jobName = JDReflectionUtil.callMethod_S(con1,
                "getServerJobIdentifier");
            // Reformat the job name it comes in as QZDASOINITQUSER 364288
            if (jobName.length() >= 26) {
              jobName = jobName.substring(20).trim() + "/"
                  + jobName.substring(10, 20).trim() + "/"
                  + jobName.substring(0, 10).trim();
            }

          } catch (Exception e) {
            try {

              /* JDReflectionUtil.callMethod_V( */
              JDJSTPTestcase.assureGETJOBNAMEisAvailable(con1);
              Statement s = con1.createStatement();
              ResultSet rs = s
                  .executeQuery("select " + JDJSTPTestcase.envLibrary
                      + ".GETJOBNAME() from " + "SYSIBM.SYSDUMMY1");
              rs.next();
              jobName = rs.getString(1);
	      s.close(); 
            } catch (Exception e2) {
              out.println("getServerJobName:4 failed with 3 exceptions jobname="
                  + jobName);
              e2.printStackTrace(out);
              e.printStackTrace(out);
              nsme.printStackTrace(out);

            }
          }

        } else if (con1.getClass().getName().indexOf("jcc") > 0) {
          jobName = "UNKNOWN QRWTSRVR";
        } else {
          out.println("getServerJobName:3 failed with exception " + nsme
              + " for driver " + driverName);
          nsme.printStackTrace(out);
        }

      } catch (Exception e) {
        out.println("getServerJobName:2 failed with 2 exceptions ");
        nsme.printStackTrace(out);
        e.printStackTrace(out);
      }
    } catch (Exception e) {
      out.println("getServerJobName:1 failed with exception " + e);
      e.printStackTrace(out);
    }
    return jobName;
  }

  //
  // Execute the command and send the output to the PrintStream
  //
  // Suppported commands are the following
  //
  // echo
  // add connection property
  // reset connection properties
  // connect to
  // connect reset -- should output DB20000I The SQL command completed
  // successfully.
  // terminate -- ignored -- should output DB20000I The TERMINATE command
  // completed successfully.
  // select
  // SILENT:
  // existfile -- displays YES if file exists or NO if it does not
  // PREPARE
  // SETPARM
  // EXECUTEQUERY
  // EXECUTEUPDATE
  // quit
  // exit
  // return false to quit
  //
  public static boolean executeCommand(String command, PrintStream out) {
    boolean returnCode = true;
    silent = false;

    // Make OUT avaiable to reflective calls
    variables.put("OUT", out);

    //
    // Do any needed replacements
    //
    for (int i = 0; i < replaceFrom.size(); i++) {
      String replaceString = (String) replaceFrom.elementAt(i);
      int index = command.indexOf(replaceString);
      while (index >= 0) {
        command = command.substring(0, index) + (String) replaceTo.elementAt(i)
            + command.substring(index + replaceString.length());
        index = command.indexOf(replaceString);
      }
    }

    //
    // Strip ; and extra whitespace
    // -- Should have been stripped above
    // -- Changed 10/28/02 to just strip end of line
    //
    command = command.trim();
    int commandLength = command.length();
    if (commandLength > 0) {
      if (command.charAt(commandLength - 1) == ';') {
        command = command.substring(0, commandLength - 1);
      }
    }
    // OLD CODE -- command = command.replace(';', ' ').trim();

    //
    // Strip the invisible if it exists

    if (command.toUpperCase().startsWith("INVISIBLE:")) {
      silent = true;
      command = command.substring(10).trim();
    } else {
      if (!echoOff) {
        out.println(command);
        if (html)
          out.println("<BR>");
      }
    }

    //
    // Strip the silent if it exists
    //
    if (command.toUpperCase().startsWith("SILENT:")) {
      silent = true;
      command = command.substring(7).trim();
    }

    try {
      //
      // Figure out the command
      //
      String upcaseCommand = command.toUpperCase();
      if (upcaseCommand.startsWith("SELECT")
          || upcaseCommand.startsWith("VALUES")
          || upcaseCommand.startsWith("WITH ")) {
        history.addElement(command);
        if (con != null) {
          if (stmt != null && reuseStatement) {
            // dont do anything -- reuse it
          } else {
            if (stmt != null) {
              stmt.close();
            }
            if (jdk14) {
              stmt = con.createStatement(resultSetType, resultSetConcurrency,
                  resultSetHoldability);
            } else {
              stmt = con.createStatement();
            }
            variables.put("STMT", stmt);
          }

        }
        if (queryTimeout != 0) {
          stmt.setQueryTimeout(queryTimeout);
        }
        if (stmt != null) {
          // Submit a query, creating a ResultSet object
          if (measureExecute) {
            startTime = System.currentTimeMillis();
          }

          ResultSet rs = stmt.executeQuery(command);
          if (measureExecute) {
            finishTime = System.currentTimeMillis();
            out.println("TIME: " + (finishTime - startTime) + " ms");
          }

          SQLWarning warning = stmt.getWarnings();
          stmt.clearWarnings();
          // Display all columns and rows from the result set
          if (manualFetch) {
            ResultSetMetaData rsmd = rs.getMetaData();
            manualResultSetNumCols = rsmd.getColumnCount();
            setManualResultSetColType(rsmd);
            manualResultSet = rs;
            variables.put("RS", manualResultSet);
            manualResultSetColumnLabel = dispColumnHeadings(out, rs, rsmd,
                false, manualResultSetNumCols);
          } else {
            dispResultSet(out, rs, false);
            // Display any warnings
            if (warning != null) {
              if (!silent) {
                dispWarning(out, warning);
              }
            }
            // Close the result set
            if (closeStatementRS) {
              rs.close();
            }
          }
        } else {
          out.println("UNABLE to EXECUTE SELECT because not connected");
        }
      } else if (upcaseCommand.startsWith("PREPARE")) {
        history.addElement(command);
        command = command.substring(7).trim();
        if (pstmt != null) {
          if (closeStatementRS) {
            pstmt.close();
          }
        }
        if (jdk14) {
          pstmt = con.prepareStatement(command, resultSetType,
              resultSetConcurrency, resultSetHoldability);
        } else {
          pstmt = con.prepareStatement(command);
        }
        variables.put("PSTMT", pstmt);

      } else if (upcaseCommand.startsWith("SETRESULTSETTYPE")) {
        history.addElement(command);
        command = command.substring(16).trim();
        if (command.indexOf("FORWARD_ONLY") >= 0) {
          resultSetType = ResultSet.TYPE_FORWARD_ONLY;
        } else if (command.indexOf("SCROLL_INSENSITIVE") >= 0) {
          resultSetType = ResultSet.TYPE_SCROLL_INSENSITIVE;
        } else if (command.indexOf("SCROLL_SENSITIVE") >= 0) {
          resultSetType = ResultSet.TYPE_SCROLL_SENSITIVE;
        } else {
          out.println("Value of '" + command + " not valid use");
          out.println(
              "     FORWARD_ONLY, SCROLL_INSENSITIVE, or SCROLL_SENSITIVE");
        }
      } else if (upcaseCommand.startsWith("SETRESULTSETCONCURRENCY")) {
        history.addElement(command);
        command = command.substring(15).trim();
        if (command.indexOf("READ_ONLY") >= 0) {
          resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;
        } else if (command.indexOf("UPDATABLE") >= 0) {
          resultSetConcurrency = ResultSet.CONCUR_UPDATABLE;
        } else {
          out.println("Value of '" + command + " not valid. Use");
          out.println(" CONCUR_READ_ONLY or CONCUR_UPDATABLE ");
        }
      } else if (upcaseCommand.startsWith("SETRESULTSETHOLDABILITY")) {
        history.addElement(command);
        command = command.substring(15).trim();
        if (command.indexOf("HOLD") >= 0) {
          resultSetHoldability = ResultSet.HOLD_CURSORS_OVER_COMMIT;
        } else if (command.indexOf("CLOSE") >= 0) {
          resultSetHoldability = ResultSet.CLOSE_CURSORS_AT_COMMIT;
        } else {
          out.println("Value of '" + command + " not valid. Use");
          out.println(" HOLD_CURSORS_OVER_COMMIT or CLOSE_CURSORS_AT_COMMIT");
        }
      } else if (upcaseCommand.startsWith("EXECUTEQUERY")) {
        history.addElement(command);
        if (pstmt != null) {
          if (measureExecute) {
            startTime = System.currentTimeMillis();
          }

          ResultSet rs = pstmt.executeQuery();
          if (measureExecute) {
            finishTime = System.currentTimeMillis();
            out.println("TIME: " + (finishTime - startTime) + " ms");
          }
          if (manualFetch) {
            ResultSetMetaData rsmd = rs.getMetaData();
            manualResultSetNumCols = rsmd.getColumnCount();
            setManualResultSetColType(rsmd);
            manualResultSet = rs;
            variables.put("RS", manualResultSet);
            manualResultSetColumnLabel = dispColumnHeadings(out, rs, rsmd,
                false, manualResultSetNumCols);
          } else {

            // Display all columns and rows from the result set
            dispResultSet(out, rs, false);
            // Display any warnings
            SQLWarning warning = pstmt.getWarnings();
            pstmt.clearWarnings();
            if (warning != null) {
              if (!silent) {
                dispWarning(out, warning);
              }
            }
            // Close the result set
            if (closeStatementRS) {
              rs.close();
            }
          }
        } else {
          out.println(
              "UNABLE to EXECUTE QUERY because prepared statement does not exist");
        }
      } else if (upcaseCommand.startsWith("EXECUTEUPDATE")) {
        history.addElement(command);
        if (pstmt != null) {
          if (measureExecute) {
            startTime = System.currentTimeMillis();
          }
          pstmt.executeUpdate();
          if (measureExecute) {
            finishTime = System.currentTimeMillis();
            out.println("TIME: " + (finishTime - startTime) + " ms");
          }

          // Display any warnings
          SQLWarning warning = pstmt.getWarnings();
          pstmt.clearWarnings();
          if (warning != null) {
            if (!silent) {
              dispWarning(out, warning);
            }
          }
        } else {
          out.println(
              "UNABLE to EXECUTE UPDATE because prepared statement does not exist");
        }
      } else if (upcaseCommand.startsWith("SETPARMFROMVAR")) {
        history.addElement(command);
        if (pstmt != null) {
          command = command.substring(14).trim();
          int commaIndex = command.indexOf(",");
          if (commaIndex > 0) {
            String indexString = command.substring(0, commaIndex).trim();
            int index = Integer.parseInt(indexString);
            String parmString = command.substring(commaIndex + 1).trim();
            Object varObject = variables.get(parmString);
            if (varObject != null) {
              pstmt.setObject(index, varObject);
              SQLWarning warning = pstmt.getWarnings();
              pstmt.clearWarnings();
              if (warning != null) {
                if (!silent) {
                  dispWarning(out, warning);
                }
              }

            } else {
              out.println("Unable to find object for variable " + parmString);
              showValidVariables(out);
            }
          } else {
            out.println(
                "UNABLE to find comma for SETPARM  --> SETPARM [index],[value]");
          }
        } else {
          out.println(
              "UNABLE to SETPARM because prepared statement does not exist");
        }
      } else if (upcaseCommand.startsWith("SETPARM")) {
        history.addElement(command);
        if (pstmt != null) {
          command = command.substring(7).trim();
          int commaIndex = command.indexOf(",");
          if (commaIndex > 0) {
            String indexString = command.substring(0, commaIndex).trim();
            int index = Integer.parseInt(indexString);
            String parmString = command.substring(commaIndex + 1).trim();
            setParameter(pstmt, parmString, index, out);
          } else {
            out.println(
                "UNABLE to find comma for SETPARM  --> SETPARM [index],[value]");
          }
        } else {
          out.println(
              "UNABLE to SETPARM because prepared statement does not exist");
        }
      } else if (upcaseCommand.startsWith("ECHO")
          || upcaseCommand.startsWith("--") || upcaseCommand.startsWith("//")
          || upcaseCommand.startsWith("/*")) {

        history.addElement(command);

        if (echoComments) {
          out.println(command);
          if (html)
            out.println("<BR>");
        }

        // Already echoed, don't do anything
      } else if (upcaseCommand.startsWith("SETQUERYTIMEOUT")) {
        history.addElement(command);
        String arg = command.substring(16).trim();
        try {
          queryTimeout = Integer.parseInt(arg);
          out.println("-->Query timeout set to " + queryTimeout);
        } catch (Exception e) {
          out.println("Unable to parse (" + arg + ")");
        }

        // Already echoed, don't do anything
      } else if (upcaseCommand.startsWith("RESET CONNECTION PROPERTIES")) {
        history.addElement(command);
        urlArgs = baseUrlArgs;
      } else if (upcaseCommand.startsWith("ADD CONNECTION PROPERTY")) {
        history.addElement(command);
        String newProperty = command.substring(23).trim();
        urlArgs = removeProperty(urlArgs, newProperty);
        urlArgs += "; " + newProperty;
      } else if (upcaseCommand.startsWith("STDXACONNECT TO")) {
        history.addElement(command);
        if (con != null) {
          try {
            JDJSTPTestcase.connectionDisconnect(con, conLabel);
            con.close();
            con = null;
            stmt = null;
            cstmt = null;
            cstmtSql = "";
          } catch (Exception e) {
            // Ignore any error from the close
          }
        }

        //
        // Create a new DB2StdXADataSource using reflection
        //
        try {
          Class xadsClass = Class
              .forName("com.ibm.db2.jdbc.app.DB2StdXADataSource");
          Class[] cparms = new Class[0];
          java.lang.reflect.Constructor constructor = xadsClass
              .getConstructor(cparms);
          Object[] parms = new Object[0];
          Object xads = constructor.newInstance(parms);
          JDReflectionUtil.callMethod_V(xads, "setDateFormat", "jis");
          JDReflectionUtil.callMethod_V(xads, "setTimeFormat", "jis");
          JDReflectionUtil.callMethod_V(xads, "setAutoCommit", true);
          variables.put("DATASOURCE", xads);

          String schema1 = command.substring(15).trim();
          if (schema1.length() > 0) {
            JDReflectionUtil.callMethod_V(xads, "setDefaultLibrary", schema1);
          }

          if (userId != null) {
            if (debug)
              out.println("Connecting using " + userId + ", " + password);
            JDReflectionUtil.callMethod_V(xads, "setUser", userId);
            JDReflectionUtil.callMethod_V(xads, "setPassword", password);
          }

          XAConnection xac = (XAConnection) JDReflectionUtil.callMethod_O(xads,
              "getXAConnection");
          con = xac.getConnection();
          dumpTaken = false;
          variables.put("CON", con);
          conLabel = conName;
          if (conCount > 0)
            conLabel = conLabel + conCount;
          conCount++;
          JDJSTPTestcase.connectionConnect(con, conLabel,
              System.getProperty("java.home"));

          //
          // Need to set path so that stuff can be found
          //
          // set current path so that Java STP is found when called thru JDBC
          if (schema1.length() > 0) {
            int slashIndex = schema1.lastIndexOf("/");
            if (slashIndex >= 0) {
              schema1 = schema1.substring(slashIndex + 1);
            }
            PreparedStatement pStmt = con.prepareStatement(
                "SET CURRENT PATH " + schema1 + ", SYSTEM PATH");
            pStmt.execute();
            pStmt.close();
	    
          }
        } catch (Exception e) {
          e.printStackTrace(out);
        }
      } else if (upcaseCommand.startsWith("CONNECT TO URL")) {
        history.addElement(command);
        if (con != null) {
          JDJSTPTestcase.connectionDisconnect(con, conLabel);
          if (con == poolConnection) {
            // Dont close our one poolConnection
            // It may be reused on the next run
          } else {
            con.close();
            stmt = null;
            cstmt = null;
            cstmtSql = "";
          }
        }

        String connectUrl = command.substring(14).trim();
        String thisConnectUserId = null;
        String thisConnectPassword = null;
        boolean error = false;
        int userIdIndex = connectUrl.indexOf("USERID=");
        if (userIdIndex < 0) {
          // connectUrl remains same
        } else {
          thisConnectUserId = connectUrl.substring(userIdIndex + 7).trim();
          connectUrl = connectUrl.substring(0, userIdIndex).trim();
          int spaceIndex = thisConnectUserId.indexOf(" ");
          if (spaceIndex >= 0) {
            thisConnectUserId = thisConnectUserId.substring(0, spaceIndex);
          }

          int passwordIndex = command.indexOf("PASSWORD=");
          if (passwordIndex < 0) {
            // user null password
          } else {
            thisConnectPassword = command.substring(passwordIndex + 9).trim();
            spaceIndex = thisConnectPassword.indexOf(" ");
            if (spaceIndex > 0) {
              thisConnectPassword = thisConnectPassword.substring(0,
                  spaceIndex);
            }
          } /* processing password */
        } /* processing userid */
        if (debug)
          out.println("Connecting using " + userId + ", " + password + " to "
              + connectUrl);
        Driver iSeriesDriver = null;
        try {

          if (connectUrl.indexOf("jdbc:db2://") >= 0) {
            jccDriver = true;
            toolboxDriver = false;
            if (debug)
              out.println("Loading jcc driver");
            Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
            Driver currentDriver = DriverManager.getDriver(url);
            if (currentDriver.getClass().getName()
                .equals("com.ibm.db2.jdbc.app.DB2Driver")) {
              if (debug)
                out.println("removing native driver");
              iSeriesDriver = currentDriver;
              DriverManager.deregisterDriver(iSeriesDriver);
            }

          }

          if (useConnectionPool) {
            con = getPooledConnection(thisConnectUserId, thisConnectPassword,
                connectUrl, out);
            if (con != null) {
              variables.put("CON", con);
            }

          } else {
            con = DriverManager.getConnection(connectUrl, thisConnectUserId,
                thisConnectPassword);
            variables.put("CON", con);
            SQLWarning warning = con.getWarnings();
            con.clearWarnings();
            if (warning != null) {
              if (!silent) {
                dispWarning(out, warning);
              }
            }

          }
          dumpTaken = false;

        } catch (Exception e) {
          e.printStackTrace(out);
          error = true;
        }
        if (iSeriesDriver != null) {
          DriverManager.registerDriver(iSeriesDriver);
        }
        conLabel = conName;
        if (conCount > 0)
          conLabel = conLabel + conCount;
        conCount++;
        JDJSTPTestcase.connectionConnect(con, conLabel,
            System.getProperty("java.home"));

        if (error) {
          out.println(
              "Usage:  CONNECT TO URL [URL] [USERID=XXXX] [PASSWORD=YYYY]");
          out.println(
              "  i.e.  CONNECT TO URL jdbc:db2:localhost USERID=USER PASSWORD=XXXXX");
          out.println(
              "        CONNECT TO URL jdbc:db2://localhost/*LOCAL USERID=USER PASSWORD=XXXXX");
          out.println(
              "        CONNECT TO URL jdbc:db2:SAMPLE\\;transaction isolation=serializable USERID=USER PASSWORD=XXXXXXX");
        }

      } else if (upcaseCommand.startsWith("CONNECT TO")) {
        history.addElement(command);
        if (con != null) {
          JDJSTPTestcase.connectionDisconnect(con, conLabel);
          if (con == poolConnection) {
            // Dont close our one poolConnection
            // It may be reused on the next run
          } else {
            con.close();
            stmt = null;
            cstmt = null;
            cstmtSql = "";
          }
          con = null;
        }

        String connectUrl;
        String schema1 = command.substring(10).trim();
        boolean doSetSchema = false;
        if (schema1.length() > 0) {

          //
          // Determine if a system name was specified..
          //
          int slashIndex = schema1.indexOf('/');
          if (slashIndex >= 0) {
            int colonIndex = url.indexOf(":");
            colonIndex = url.indexOf(":", colonIndex + 1);

            String baseUrl = url.substring(0, colonIndex + 1);
            connectUrl = baseUrl + schema1 + urlArgs;
          } else {
            // If JCC, do not add schema or the URL args
            if (url.indexOf("jdbc:db2://") >= 0) {
              jccDriver = true;
              toolboxDriver = false;

              doSetSchema = true;
              connectUrl = url;
            } else {
              connectUrl = url + "/" + schema1 + urlArgs;
            }
          }

        } else {
          connectUrl = url + urlArgs;
        }
        //
        // Cleanup URL if needed
        //
        {
          int cleanIndex = connectUrl.indexOf(" ;");
          while (cleanIndex > 0) {
            connectUrl = connectUrl.substring(0, cleanIndex)
                + connectUrl.substring(cleanIndex + 1);
            cleanIndex = connectUrl.indexOf(" ;");
          }
        }
        if (userId != null) {
          if (debug)
            out.println("Connecting using " + userId + ", " + password + " to "
                + connectUrl);
          try {
            if (useConnectionPool) {
              con = getPooledConnection(userId, password, connectUrl, out);
            } else {
              con = DriverManager.getConnection(connectUrl, userId, password);
              SQLWarning warning = con.getWarnings();
              con.clearWarnings();
              if (warning != null) {
                if (!silent) {
                  dispWarning(out, warning);
                }
              }

            }

          } catch (Exception e) {
            String exMessage = e.toString();
            if (exMessage.indexOf("Authorization failure") >= 0) {
              out.println("Authorization failue connecting using " + userId
                  + "," + password);
            }
            throw e;
          }

          dumpTaken = false;
          if (con != null) {
            variables.put("CON", con);
          }

        } else {
          if (debug)
            out.println(
                "Connecting using default id and password to " + connectUrl);
          con = null;
          if (useConnectionPool) {
            con = getPooledConnection("null", "null", connectUrl, out);

            // Make sure connection is alive
            Statement testStmt = con.createStatement();
            try {
              testStmt.executeQuery("Select * from sysibm.sysdummy1");
            } catch (Exception e) {
              String exMessage = e.toString();
              if (exMessage.indexOf("connection does not exist") > 0) {
                // ignore
              } else {
                out.println(
                    "Warning:  Exception caught checking pooled connection");
                e.printStackTrace(out);
              }
              // Just ignore error and get connection the old way
              con = null;
            }
	    testStmt.close(); 
          }
          if (con == null) {
            con = DriverManager.getConnection(connectUrl);
            SQLWarning warning = con.getWarnings();
            con.clearWarnings();
            if (warning != null) {
              if (!silent) {
                dispWarning(out, warning);
              }
            }

          }
          if (con != null) {
            variables.put("CON", con);
          }
          dumpTaken = false;
        }

        conLabel = conName;
        if (conCount > 0)
          conLabel = conLabel + conCount;
        conCount++;
        JDJSTPTestcase.connectionConnect(con, conLabel,
            System.getProperty("java.home"));

        //
        // Need to set path so that stuff can be found
        //
        // set current path so that Java STP is found when called thru JDBC
        int semicolonIndex = schema1.indexOf(';');
        if (semicolonIndex > 0) {
          schema1 = schema1.substring(0, semicolonIndex).trim();
        }
        if (schema1.length() > 0) {
          int slashIndex = schema1.lastIndexOf("/");
          if (slashIndex >= 0) {
            schema1 = schema1.substring(slashIndex + 1);
          }
          PreparedStatement pStmt = con.prepareStatement(
              "SET CURRENT PATH " + schema1 + ", SYSTEM PATH");
          pStmt.execute();
          pStmt.close();
        }
        if (doSetSchema) {
          PreparedStatement pStmt = con
              .prepareStatement("SET SCHEMA " + schema1);
          pStmt.execute();
          pStmt.close();

        }
      } else if (upcaseCommand.startsWith("CONNECT RESET")) {
        history.addElement(command);
        if (con != null) {
          JDJSTPTestcase.connectionDisconnect(con, conLabel);
          if (con == poolConnection) {
            // Dont close our one poolConnection
            // It may be reused on the next run
          } else {
            con.close();
            stmt = null;
            cstmt = null;
            cstmtSql = "";
          }
          con = null;
        }

      } else if (upcaseCommand.startsWith("CHARACTERDETAILS")) {
        history.addElement(command);
        String arg = command.substring(16).trim().toUpperCase();
        if (arg.equals("TRUE")) {
          characterDetails = true;
        } else if (arg.equals("ON")) {
          characterDetails = true;
        } else if (arg.equals("FALSE")) {
          characterDetails = false;
        } else if (arg.equals("OFF")) {
          characterDetails = false;
        } else {
          out.println("Invalid arg '" + arg + "' for CHARACTERDETAILS");
        }
      } else if (upcaseCommand.startsWith("SHOWLOBTHRESHOLD")) {
        history.addElement(command);
        String arg = command.substring(16).trim().toUpperCase();
        int newValue = Integer.parseInt(arg);
        if (newValue > 0) {
          showLobThreshold = newValue;
        } else {
          out.println("SHOWLOBTHRESHOLD value " + arg + " invalid");
        }

      } else if (upcaseCommand.startsWith("CLOSESTATEMENTRS")) {
        history.addElement(command);
        String arg = command.substring(16).trim().toUpperCase();
        if (arg.equals("TRUE")) {
          closeStatementRS = true;
        } else if (arg.equals("ON")) {
          closeStatementRS = true;
        } else if (arg.equals("FALSE")) {
          closeStatementRS = false;
        } else if (arg.equals("OFF")) {
          closeStatementRS = false;
        } else {
          out.println("Invalid arg '" + arg + "' for closeStatementRS");
        }
      } else if (upcaseCommand.startsWith("MEASUREEXECUTE")) {
        history.addElement(command);
        String arg = command.substring(14).trim().toUpperCase();
        if (arg.equals("TRUE")) {
          measureExecute = true;
        } else if (arg.equals("ON")) {
          measureExecute = true;
        } else if (arg.equals("FALSE")) {
          measureExecute = false;
        } else if (arg.equals("OFF")) {
          measureExecute = false;
        } else {
          out.println("Invalid arg '" + arg + "' for measureExecute");
        }
      } else if (upcaseCommand.startsWith("PRINTSTACKTRACE")) {
        history.addElement(command);
        String arg = command.substring(15).trim().toUpperCase();
        if (arg.equals("TRUE")) {
          printStackTrace = true;
        } else if (arg.equals("ON")) {
          printStackTrace = true;
        } else if (arg.equals("FALSE")) {
          printStackTrace = false;
        } else if (arg.equals("OFF")) {
          printStackTrace = false;
        } else {
          out.println("Invalid arg '" + arg + "' for printStackTrace");
        }
      } else if (upcaseCommand.startsWith("PRINTCALLMETHODSTACKTRACE")) {
        history.addElement(command);
        String arg = command.substring(25).trim().toUpperCase();
        if (arg.equals("TRUE")) {
          printCallMethodStackTrace = true;
        } else if (arg.equals("ON")) {
          printCallMethodStackTrace = true;
        } else if (arg.equals("FALSE")) {
          printCallMethodStackTrace = false;
        } else if (arg.equals("OFF")) {
          printCallMethodStackTrace = false;
        } else {
          out.println(
              "Invalid arg '" + arg + "' for printCallMethodStackTrace");
        }
      } else if (upcaseCommand.startsWith("CALL ")) {
        history.addElement(command);
        if (con != null) {
          //
          // See if input parameters are passed.
          // If so, they are comma separated
          // A unicode string may be specified using UX'dddd'
          // A byte array may be specified using X'dddd'
          // or GEN_BYTE_ARRAY+'nnnn'
          //
          int parmIndex = command.indexOf("-- INPARM");
          String parms = null;
          if (parmIndex > 0) {
            parms = command.substring(parmIndex + 9).trim();
            command = command.substring(0, parmIndex);
          }

          if (command.equals(cstmtSql)) {
            // Already prepared.. Keep existing
          } else {
            if (cstmt != null) {
              try {
                cstmt.close();
                cstmt = null;
                cstmtSql = "";
              } catch (Exception e) {
              }
            }
            if (jdk14) {
              cstmt = con.prepareCall(command, resultSetType,
                  resultSetConcurrency, resultSetHoldability);
            } else {
              cstmt = con.prepareCall(command);
            }
            cstmtSql = command;
            variables.put("CSTMT", cstmt);
          }

          if (jdk14) {
            //
            // If JDK 1.4 is available then use metadata
            // to set parameters
            //
            ParameterMetaData pmd = cstmt.getParameterMetaData();
            int parmCount = pmd.getParameterCount();
            for (int parm = 1; parm <= parmCount; parm++) {
              int mode = pmd.getParameterMode(parm);
              if (mode == ParameterMetaData.parameterModeOut
                  || mode == ParameterMetaData.parameterModeInOut) {

                // Register the output parameter as the correct type
                // For most of the types, we will register as VARCHAR
                // since we will be used getString() to get the
                // output.

                int type = pmd.getParameterType(parm);
                switch (type) {
                case Types.BLOB:
                case Types.BINARY:
                case Types.VARBINARY:
                case Types.LONGVARBINARY:
                case -8: /* ROWID */
                case Types.ARRAY:
                case Types.BOOLEAN:
                  cstmt.registerOutParameter(parm, type);
                  break;
                default:
                  cstmt.registerOutParameter(parm, Types.VARCHAR);

                }
              }

              if (mode == ParameterMetaData.parameterModeIn
                  || mode == ParameterMetaData.parameterModeInOut) {
                String thisParm = parms;
                if (parms != null) {
                  parmIndex = parms.indexOf(",");
                  if (parmIndex >= 0) {
                    thisParm = parms.substring(0, parmIndex).trim();
                    parms = parms.substring(parmIndex + 1).trim();
                  }
                }
                if (thisParm != null) {
                  setParameter(cstmt, thisParm, parm, out);
                } else {
                  out.println("Warning:  thisParm is null");
                  out.println("--INPARM not found but num param > 0 ");
                }

              }
            }
          } else {
            //
            // If there is a question mark, assume that parameter markers were
            // used and
            // throw an exception
            //
            if (command.indexOf("?") >= 0) {
              throw new SQLException(
                  "Use of parameter markers in call statement only supported in JDK 1.4 -- statement was "
                      + command);
            }
          }

          boolean resultSetAvailable = cstmt.execute();
          // Display any warnings
          SQLWarning warning = cstmt.getWarnings();
          cstmt.clearWarnings();
          if (warning != null) {
            if (!silent) {
              dispWarning(out, warning);
            }
            if (html) {
              out.println("Statement was " + command);
            }
          }

          if (jdk14) {
            //
            // If JDK 1.4 is available then use metadata
            // to get parameters
            //
            ParameterMetaData pmd = cstmt.getParameterMetaData();
            int parmCount = pmd.getParameterCount();
            for (int parm = 1; parm <= parmCount; parm++) {
              int mode = pmd.getParameterMode(parm);
              if (mode == ParameterMetaData.parameterModeOut
                  || mode == ParameterMetaData.parameterModeInOut) {

                int type = pmd.getParameterType(parm);

                switch (type) {
                case Types.BLOB:
                case Types.BINARY:
                case Types.VARBINARY:
                case Types.LONGVARBINARY:
                case -8: /* ROWID */

                {
                  out.print("Parameter " + parm + " returned ");
                  byte[] bytes = cstmt.getBytes(parm);
                  if (bytes == null) {
                    out.println("null");
                  } else {
                    if (bytes.length < showLobThreshold) {
                      out.print("X'");
                      for (int i = 0; i < bytes.length; i++) {
                        int unsignedInt = 0xFF & bytes[i];
                        if (unsignedInt < 0x10) {
                          out.print("0" + Integer.toHexString(unsignedInt));
                        } else {
                          out.print(Integer.toHexString(unsignedInt));
                        }
                      }
                      out.println("'");
                    } else {
                      CRC32 checksum = new CRC32();
                      checksum.update(bytes);
                      out.println("ARRAY[size=" + bytes.length + ",CRC32="
                          + checksum.getValue() + "]");
                    }
                  }
                }
                  break;
                case Types.BOOLEAN:
                  out.print("Parameter " + parm + " returned ");
                  boolean bool = cstmt.getBoolean(parm);
                  if (cstmt.wasNull()) {
                    out.println("null");
                  } else if (bool == true) {
                    out.println("true");
                  } else {
                    out.println("false");
                  }

                  break;
                case Types.ARRAY:
                  out.print("Parameter " + parm + " returned ARRAY ");
                  printArray(out, cstmt.getArray(parm));
                  out.println();

                  break;
                default:
                  out.print("Parameter " + parm + " returned ");
                  savedStringParm[parm] = cstmt.getString(parm);
                  printUnicodeString(out, savedStringParm[parm]);
                  out.println();
                }
              }
            }
          }

          if (resultSetAvailable) {
            ResultSet rs = cstmt.getResultSet();
            if (rs != null) {
              if (manualFetch) {
                ResultSetMetaData rsmd = rs.getMetaData();
                manualResultSetNumCols = rsmd.getColumnCount();
                setManualResultSetColType(rsmd);
                manualResultSet = rs;
                variables.put("RS", manualResultSet);
                manualResultSetColumnLabel = dispColumnHeadings(out, rs, rsmd,
                    false, manualResultSetNumCols);
              } else {

                dispResultSet(out, rs, false);
                if (closeStatementRS) {
                  rs.close();
                  rs = null;
                }
              }
              // Look for more result tests
              if (!manualFetch) {
                while (cstmt.getMoreResults()) {
                  out.println("<<<< NEXT RESULT SET >>>>>>>");
                  rs = cstmt.getResultSet();
                  dispResultSet(out, rs, false);
                  if (closeStatementRS) {
                    rs.close();
                    rs = null;
                  }
                }
              }
            }
          }
          if (!manualFetch && closeStatementRS) {
            // Do not close any more. We'll attempt to reuse
            // cstmt.close();
          }
        } else {
          out.println("UNABLE to EXECUTE CALL because not connected");
        }
      } else if (upcaseCommand.startsWith("EXISTFILE")) {
        history.addElement(command);
        String filename = command.substring(9).trim();
        try {
          File testFile = new File(filename);
          if (testFile.exists()) {
            out.println("EXISTFILE " + filename + ": YES");
          } else {
            out.println("EXISTFILE " + filename + ": NO");
          }
        } catch (Exception e) {
          e.printStackTrace(out);
        }
      } else if (upcaseCommand.startsWith("GC")) {
        history.addElement(command);
        startTime = System.currentTimeMillis();
        System.gc();
        finishTime = System.currentTimeMillis();

        out.println("GC ran in " + (finishTime - startTime) + " milliseconds");

      } else if (upcaseCommand.startsWith("OUTPUT FORMAT ")) {
        history.addElement(command);
        String format = command.substring(14).trim().toUpperCase();
        if (format.equals("XML")) {
          xml = true;
          html = false;
        } else if (format.equals("HTML")) {
          html = true;
          xml = false;
        } else {
          out.println(
              "Error.  Did not recognize output format '" + format + "'");
        }
      } else if (upcaseCommand.startsWith("SHOWMIXEDUX ")) {
        history.addElement(command);
        String format = command.substring(11).trim().toUpperCase();
        if (format.equals("TRUE")) {
          showMixedUX = true;
        } else if (format.equals("FALSE")) {
          showMixedUX = false;
        } else {
          out.println(
              "Error.  Did not recognize SHOWMIXEDUX value  '" + format + "'");
        }
      } else if (upcaseCommand.startsWith("SHOWCHARNAMES ")) {
        history.addElement(command);
        String format = command.substring(14).trim().toUpperCase();
        if (format.equals("TRUE")) {
          showCharNames = true;
        } else if (format.equals("FALSE")) {
          showCharNames = false;
        } else {
          out.println("Error.  Did not recognize SHOWCHARNAMES value  '"
              + format + "'");
        }
      } else if (upcaseCommand.startsWith("QUIT")
          || upcaseCommand.startsWith("EXIT")) {
        returnCode = false;

      } else if (upcaseCommand.startsWith("SET AUTOCOMMIT")) {
        history.addElement(command);
        String setting = command.substring(14).trim().toLowerCase();
        if (setting.startsWith("true")) {
          con.setAutoCommit(true);
        } else if (setting.startsWith("false")) {
          con.setAutoCommit(false);
        } else {
          out.println("SET AUTOCOMMIT:  Didn't understand \"" + setting + "\"");
          out.println("  Usage:  SET AUTOCOMMIT true");
          out.println("          SET AUTOCOMMIT false");

        }

      } else if (upcaseCommand.startsWith("REUSE CONNECTION")) {
        history.addElement(command);
        String setting = command.substring(16).trim().toLowerCase();
        if (setting.startsWith("true")) {
          useConnectionPool = true;
        } else if (setting.startsWith("false")) {
          useConnectionPool = false;
        } else if (setting.startsWith("ignore")) {
          // Don't change the setting, use the the existing
        } else {
          out.println(
              "REUSE CONNECTION:  Didn't understand \"" + setting + "\"");
          out.println("  Usage:  REUSE CONNECTION true");
          out.println("          REUSE CONNECTION false");
        }

      } else if (upcaseCommand.startsWith("REUSE STATEMENT")) {
        history.addElement(command);
        String setting = command.substring(16).trim();
        if (setting.startsWith("true")) {
          reuseStatement = true;
          if (stmt != null) {
            try {
              stmt.close();
            } catch (Exception e) {
            }
            if (jdk14) {
              stmt = con.createStatement(resultSetType, resultSetConcurrency,
                  resultSetHoldability);
            } else {
              stmt = con.createStatement();
            }
            variables.put("STMT", stmt);

          }
        } else if (setting.startsWith("false")) {
          reuseStatement = false;
        } else {
          out.println(
              "REUSE STATEMENT:  Didn't understand \"" + setting + "\"");
          out.println("  Usage:  REUSE STATEMENT true");
          out.println("          REUSE STATEMENT false");
        }

      } else if (upcaseCommand.startsWith("PRESERVE_SEMICOLONS")) {

        history.addElement(command);
        boolean b = false;
        boolean ok = false;
        String setting = command.substring(19).trim();
        if (setting.startsWith("true")) {
          b = true;
          ok = true;
        } else if (setting.startsWith("false")) {
          b = false;
          ok = true;
        }

        if (ok) {
          preserveSemicolons = b;
        } else {
          out.println(
              "PRESERVE_SEMICOLONS:  Didn't understand \"" + setting + "\"");
          out.println("  Usage:  PRESERVE_SEMICOLONS true");
          out.println("          PRESERVE_SEMICOLONS false");
        }

      } else if (upcaseCommand.startsWith("SETCLITRACE")) {
        history.addElement(command);
        boolean b = false;
        boolean ok = false;
        String setting = command.substring(11).trim();
        if (setting.startsWith("true")) {
          b = true;
          ok = true;
        } else if (setting.startsWith("false")) {
          b = false;
          ok = true;
        }

        if (ok) {
          try {
            Class traceClass = Class.forName("com.ibm.db2.jdbc.app.T");
            Class[] argClasses = new Class[1];
            argClasses[0] = Boolean.TYPE;
            java.lang.reflect.Method method = traceClass
                .getMethod("setCliTrace", argClasses);
            Object[] args = new Object[1];
            args[0] = new Boolean(b);
            method.invoke(null, args);

          } catch (Exception e) {
            out.println("Exception while setting cli trace");
            e.printStackTrace(out);
          }

        } else {
          out.println("SETCLITRACE:  Didn't understand \"" + setting + "\"");
          out.println("  Usage:  SETCLITRACE true");
          out.println("          SETCLITRACE false");
        }

      } else if (upcaseCommand.startsWith("SETDB2TRACE")) {
        history.addElement(command);

        try {
          String setting = command.substring(11).trim();

          Class traceClass = Class.forName("com.ibm.db2.jdbc.app.T");
          Class[] argClasses = new Class[1];
          argClasses[0] = Integer.TYPE;
          java.lang.reflect.Method method = traceClass.getMethod("setDb2Trace",
              argClasses);
          Object[] args = new Object[1];
          if (setting.startsWith("true")) {
            args[0] = new Integer(3);
          } else if (setting.startsWith("false")) {
            args[0] = new Integer(0);
          } else {
            args[0] = new Integer(Integer.parseInt(setting));
          }
          method.invoke(null, args);

        } catch (Exception e) {
          out.println("Exception while setting cli trace");
          e.printStackTrace(out);
        }

      } else if (upcaseCommand.startsWith("SET TRANSACTIONISOLATION")) {
        history.addElement(command);
        String setting = command.substring(24).trim();

        if (setting.startsWith("TRANSACTION_READ_UNCOMMITTED")) {
          con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        } else if (setting.startsWith("TRANSACTION_READ_COMMITTED")) {
          con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        } else if (setting.startsWith("TRANSACTION_REPEATABLE_READ")) {
          con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        } else if (setting.startsWith("TRANSACTION_SERIALIZABLE")) {
          con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        } else {
          out.println("SET TRANSACTIONISOLATION:  Didn't understand \""
              + setting + "\"");
          out.println(
              "  Usage:  SET TRANSACTIONISOLATION TRANSACTION_READ_UNCOMMITTED");
          out.println(
              "          SET TRANSACTIONISOLATION TRANSACTION_READ_COMMITTED");
          out.println(
              "          SET TRANSACTIONISOLATION TRANSACTION_REPEATABLE_READ");
          out.println(
              "          SET TRANSACTIONISOLATION TRANSACTION_SERIALIZABLE");

        }

      } else if (upcaseCommand.startsWith("DUMPJOBLOG")) {
        history.addElement(command);
        //
        // This commands means to dump the job log when the particular error
        // code
        // is encountered. Combining this with SILENT: provides a good way to
        // get
        // additional information when an error case occurs
        //
        String sqlCode = command.substring(10).trim();
        // System.out.println("Putting \""+sqlCode+"\"");
        if (sqlCode.toUpperCase().equals("ANY")
            || sqlCode.toUpperCase().equals("ALL")) {
          dumpAnyError = true;
          dumpTaken = false;
        } else if (sqlCode.toUpperCase().equals("NONE")) {
          dumpCodes = new Hashtable();
          dumpAnyError = false;
          dumpTaken = false;
          dumpNMessages = 0;
        } else {
          dumpCodes.put(sqlCode, sqlCode);
          dumpTaken = false;
        }

      } else if (upcaseCommand.startsWith("LASTJOBLOG")) {
        history.addElement(command);
        String number = command.substring(10).trim();
        dumpNMessages = Integer.parseInt(number);

      } else if (upcaseCommand.startsWith("DUMPTRACE")) {
        history.addElement(command);
        //
        // This commands means to dump the trace when the particular error code
        // is encountered. Combining this with SILENT: provides a good way to
        // get
        // additional information when an error case occurs
        //
        String sqlCode = command.substring(10).trim();
        // System.out.println("Putting \""+sqlCode+"\"");
        if (sqlCode.toUpperCase().equals("ANY")) {
          dumpTraceAnyError = true;
        } else if (sqlCode.toUpperCase().equals("NONE")) {
          dumpTraceCodes = new Hashtable();
          dumpTraceAnyError = false;
        } else {
          dumpTraceCodes.put(sqlCode, sqlCode);
        }

      } else if (upcaseCommand.startsWith("USAGE")
          || upcaseCommand.startsWith("HELP")) {
        for (int u = 0; u < usage.length; u++) {
          out.println(usage[u]);
        }

      } else if (upcaseCommand.startsWith("GETSERVERJOBNAME")) {
        history.addElement(command);
        String jobName = getServerJobName(con, out);
        out.println("getServerJobName returned " + jobName);

      } else if (upcaseCommand.startsWith("DMD.GETCOLUMNS")) {
        history.addElement(command);
        try {
          String catalog = null;
          String schemaPattern = null;
          String tableNamePattern = null;
          String columnNamePattern = null;

          DatabaseMetaData dmd = con.getMetaData();

          String args = command.substring(14).trim();
          int commaIndex;
          commaIndex = args.indexOf(",");
          if (commaIndex > 0) {
            catalog = args.substring(0, commaIndex).trim();
            if (catalog.equals("null"))
              catalog = null;
            args = args.substring(commaIndex + 1);
            commaIndex = args.indexOf(",");
            if (commaIndex > 0) {
              schemaPattern = args.substring(0, commaIndex).trim();
              if (schemaPattern.equals("null"))
                schemaPattern = null;
              args = args.substring(commaIndex + 1);
              commaIndex = args.indexOf(",");
              if (commaIndex > 0) {
                tableNamePattern = args.substring(0, commaIndex).trim();
                if (tableNamePattern.equals("null"))
                  tableNamePattern = null;
                columnNamePattern = args.substring(commaIndex + 1).trim();
                if (columnNamePattern.equals("null"))
                  columnNamePattern = null;
              }
            }
          }
          out.println("Calling dmd.getColumns(" + catalog + ", " + schemaPattern
              + ", " + tableNamePattern + ", " + columnNamePattern + ")");
          ResultSet rs = dmd.getColumns(catalog, schemaPattern,
              tableNamePattern, columnNamePattern);

          if (rs != null) {
            dispResultSet(out, rs, false);
            rs.close();
          }

        } catch (Exception e) {
          out.println("databaseMetaData.getColumns failed with exception " + e);
          e.printStackTrace(out);
        }
      } else if (upcaseCommand.startsWith("DMD.GETTABLES")) {
        history.addElement(command);
        try {
          String catalog = null;
          String schemaPattern = null;
          String tableNamePattern = null;
          String typePattern = null;
          String[] types = null;

          DatabaseMetaData dmd = con.getMetaData();

          String args = command.substring(14).trim();
          int commaIndex;
          commaIndex = args.indexOf(",");
          if (commaIndex > 0) {
            catalog = args.substring(0, commaIndex).trim();
            if (catalog.equals("null"))
              catalog = null;
            args = args.substring(commaIndex + 1);
            commaIndex = args.indexOf(",");
            if (commaIndex > 0) {
              schemaPattern = args.substring(0, commaIndex).trim();
              if (schemaPattern.equals("null"))
                schemaPattern = null;
              args = args.substring(commaIndex + 1);
              commaIndex = args.indexOf(",");
              if (commaIndex > 0) {
                tableNamePattern = args.substring(0, commaIndex).trim();
                if (tableNamePattern.equals("null"))
                  tableNamePattern = null;
                typePattern = args.substring(commaIndex + 1).trim();
                if (typePattern.equals("null")) {
                  typePattern = null;
                } else {
                  Vector vectorList = new Vector();
                  int barIndex = typePattern.indexOf('|');
                  while (barIndex > 0) {
                    String thisType = typePattern.substring(0, barIndex);
                    vectorList.add(thisType);

                    typePattern = typePattern.substring(1 + barIndex);
                    barIndex = typePattern.indexOf('|');
                  }
                  vectorList.add(typePattern);

                  int size = vectorList.size();
                  types = new String[size];
                  for (int i = 0; i < size; i++) {
                    types[i] = (String) vectorList.elementAt(i);
                  }
                }
              }
            }
          }
          out.println("Calling dmd.getTables(" + catalog + ", " + schemaPattern
              + ", " + tableNamePattern + ", " + typePattern + "="
              + stringArrayContents(types) + ")");
          ResultSet rs = dmd.getTables(catalog, schemaPattern, tableNamePattern,
              types);

          if (rs != null) {
            dispResultSet(out, rs, false);
            rs.close();
          }

        } catch (Exception e) {
          out.println("databaseMetaData.getTables failed with exception " + e);
          e.printStackTrace(out);
        }

      } else if (upcaseCommand.startsWith("DMD.GETINDEXINFO")) {
        history.addElement(command);
        try {
          String catalog = null;
          String schema1 = null;
          String table = null;
          boolean booleanUnique = false;
          ;
          boolean booleanApproximate = false;

          DatabaseMetaData dmd = con.getMetaData();

          String args = command.substring(16).trim();
          int commaIndex;
          commaIndex = args.indexOf(",");
          if (commaIndex > 0) {
            catalog = args.substring(0, commaIndex).trim();
            if (catalog.equals("null"))
              catalog = null;
            args = args.substring(commaIndex + 1);
            commaIndex = args.indexOf(",");
            if (commaIndex > 0) {
              schema1 = args.substring(0, commaIndex).trim();
              if (schema1.equals("null"))
                schema1 = null;
              args = args.substring(commaIndex + 1);
              commaIndex = args.indexOf(",");
              if (commaIndex > 0) {
                table = args.substring(0, commaIndex).trim();
                if (table.equals("null"))
                  table = null;
                args = args.substring(commaIndex + 1);
                commaIndex = args.indexOf(",");
                if (commaIndex > 0) {
                  String unique = args.substring(0, commaIndex).trim();
                  booleanUnique = unique.equalsIgnoreCase("true");
                  args = args.substring(commaIndex + 1);
                  String approximate = args;
                  booleanApproximate = approximate.equalsIgnoreCase("true");

                }
              }
            }
          }
          out.println("Calling dmd.getIndexInfo(" + catalog + ", " + schema1
              + ", " + table + ", " + booleanUnique + "," + booleanApproximate
              + ")");
          ResultSet rs = dmd.getIndexInfo(catalog, schema1, table,
              booleanUnique, booleanApproximate);

          if (rs != null) {
            dispResultSet(out, rs, false);
            rs.close();
          }

        } catch (Exception e) {
          out.println(
              "databaseMetaData.getIndexInfo failed with exception " + e);
          e.printStackTrace(out);
        }

      } else if (upcaseCommand.startsWith("DMD.GETSCHEMAS")) {
        history.addElement(command);
        try {

          DatabaseMetaData dmd = con.getMetaData();

          out.println("Calling dmd.getSchemas()");
          ResultSet rs = dmd.getSchemas();

          if (rs != null) {
            dispResultSet(out, rs, false);
            rs.close();
          }

        } catch (Exception e) {
          out.println("databaseMetaData.getSchemas failed with exception " + e);
          e.printStackTrace(out);
        }
      } else if (upcaseCommand.startsWith("DEBUGGER.ATTACH")) {
        history.addElement(command);
        try {
          String portString = command.substring(15).trim();
          if (portString.length() == 0) {
            portString = savedStringParm[2];
          }

          int portNumber = Integer.parseInt(portString);
          out.println("Starting debugger on port " + portNumber + " at "
              + (new java.util.Date()).toString());
          if (debugger != null) {
            JDReflectionUtil.callMethod_V(debugger,  "stopDebugger");
          }
          JDReflectionUtil.createObject("test.JDDebugger", portNumber, out);
          
          JDReflectionUtil.callMethod_V(debugger,  "start"); 
          out.println("DEBUGGER attached");
        } catch (Exception e) {
          out.println("DEBUGGER.ATTACH failed with exception " + e + " at "
              + (new java.util.Date()).toString());
          e.printStackTrace(out);
          dumpAnyError = true;
          throw new SQLException("DEBUGGER.ATTACH failed.. Dumping job log");
        }

      } else if (upcaseCommand.startsWith("DEBUGGER.STOP")) {
        history.addElement(command);
        try {
          out.println("Stopping debugger ");
          if (debugger != null) {
        	  JDReflectionUtil.callMethod_V(debugger,  "stopDebugger");
            
          }
          debugger = null;
          System.gc();
          out.println("DEBUGGER stopped");
        } catch (Exception e) {
          out.println("DEBUGGER.STOP failed with exception " + e);
          e.printStackTrace(out);
        }
      } else if (upcaseCommand.startsWith("DEBUGGER.THREADSTART")) {
        history.addElement(command);
        try {
          if (debugger != null) {
        	  JDReflectionUtil.callMethod_V(debugger,"setThreadStartRequest");
          }
        } catch (Exception e) {
          out.println("DEBUGGER.THREADSTART failed with exception " + e);
          e.printStackTrace(out);
        }
      } else if (upcaseCommand.startsWith("DEBUGGER.THREADDEATH")) {
        history.addElement(command);
        try {
          if (debugger != null) {
        	  JDReflectionUtil.callMethod_V(debugger,"setThreadDeathRequest");
          }
        } catch (Exception e) {
          out.println("DEBUGGER.THREADDEATH failed with exception " + e);
          e.printStackTrace(out);
        }
      } else if (upcaseCommand.startsWith("DEBUGGER.METHODENTRY")) {
        history.addElement(command);
        try {
          if (debugger != null) {
        	  JDReflectionUtil.callMethod_V(debugger,"setMethodEntry");
          }
        } catch (Exception e) {
          out.println("DEBUGGER.METHODENTRY failed with exception " + e);
          e.printStackTrace(out);
        }
      } else if (upcaseCommand.startsWith("DEBUGGER.METHODEXIT")) {
        history.addElement(command);
        try {
          if (debugger != null) {
        	  JDReflectionUtil.callMethod_V(debugger,"setMethodExit");
          }
        } catch (Exception e) {
          out.println("DEBUGGER.METHODEXIT failed with exception " + e);
          e.printStackTrace(out);
        }
      } else if (upcaseCommand.startsWith("DEBUGGER.UNCAUGHTEXCEPTION")) {
        history.addElement(command);
        try {
          if (debugger != null) {
        	  JDReflectionUtil.callMethod_V(debugger,"setUncaughtException");
          }
        } catch (Exception e) {
          out.println("DEBUGGER.UNCAUGHEXCEPTION failed with exception " + e);
          e.printStackTrace(out);
        }

      } else if (upcaseCommand.startsWith("DEBUGGER.SHOWTHREADS")) {
        history.addElement(command);

        try {
          if (debugger != null) {
            int count;
            String threadCountArg = command.substring(20).trim();
            if (threadCountArg.length() > 0) {
              try {
                count = Integer.parseInt(threadCountArg);
                if (count == 0)
                  count = 20;
              } catch (Exception e) {
                count = 20;
              }
            } else {
              count = 20;
            }
            JDReflectionUtil.callMethod_V(debugger,"showThreads",count);
          }
        } catch (Exception e) {
          out.println("DEBUGGER.SHOWTHREADS failed with exception " + e);
          e.printStackTrace(out);
        }

      } else if (upcaseCommand.startsWith("DEBUGGER.BREAK")) {
        history.addElement(command);
        try {
          String breakArgs = command.substring(14).trim();
          if (debugger != null) {
            JDReflectionUtil.callMethod_V(debugger,"setBreakpoint",breakArgs);
            out.println("BREAK SET");
          } else {
            out.println("ERROR:  DEBUGGER not started");
          }
        } catch (Exception e) {
          out.println("DEBUGGER.BREAK failed with exception " + e);
          e.printStackTrace(out);
        }

      } else if (upcaseCommand.startsWith("DEBUGGER.SHOWLINELOCATIONS")) {
        history.addElement(command);
        try {
          String breakArgs = command.substring(26).trim();
          if (debugger != null) {
            out.println("LINES START");
            JDReflectionUtil.callMethod_V(debugger,"showLineLocations",breakArgs);
            out.println("LINES FINISH");
          } else {
            out.println("ERROR:  DEBUGGER not started");
          }
        } catch (Exception e) {
          out.println("DEBUGGER.BREAK failed with exception " + e);
          e.printStackTrace(out);
        }

      } else if (upcaseCommand.startsWith("HISTORY.CLEAR")) {
        history.clear();
      } else if (upcaseCommand.startsWith("HISTORY.SHOW")) {
        Enumeration enumeration = history.elements();
        while (enumeration.hasMoreElements()) {
          String info = (String) enumeration.nextElement();
          out.println(info);
        }
      } else if (upcaseCommand.startsWith("MANUALFETCH")) {
        history.addElement(command);
        String arg = command.substring(11).trim().toUpperCase();
        if (arg.equals("TRUE")) {
          manualFetch = true;
        } else if (arg.equals("ON")) {
          manualFetch = true;
        } else if (arg.equals("FALSE")) {
          manualFetch = false;
        } else if (arg.equals("OFF")) {
          manualFetch = false;
        } else {
          out.println("Invalid arg '" + arg + "' for MANUALFETCH");
        }
      } else if (upcaseCommand.startsWith("RS.NEXT")) {
        history.addElement(command);
        boolean ok = manualResultSet.next();
        if (ok) {
          dispRow(out, manualResultSet, false, manualResultSetNumCols,
              manualResultSetColType, manualResultSetColumnLabel, null);
        } else {
          out.println("rs.next returned false");
        }
      } else if (upcaseCommand.startsWith("RS.FIRST")) {
        history.addElement(command);
        boolean ok = manualResultSet.first();
        if (ok) {
          dispRow(out, manualResultSet, false, manualResultSetNumCols,
              manualResultSetColType, manualResultSetColumnLabel, null);
        } else {
          out.println("rs.first returned false");
        }
      } else if (upcaseCommand.startsWith("RS.BEFOREFIRST")) {
        history.addElement(command);
        manualResultSet.beforeFirst();
        out.println("rs.beforeFirst called");
      } else if (upcaseCommand.startsWith("RS.AFTERLAST")) {
        history.addElement(command);
        manualResultSet.afterLast();
        out.println("rs.afterLast called");
      } else if (upcaseCommand.startsWith("RS.LAST")) {
        history.addElement(command);
        boolean ok = manualResultSet.last();
        if (ok) {
          dispRow(out, manualResultSet, false, manualResultSetNumCols,
              manualResultSetColType, manualResultSetColumnLabel, null);
        } else {
          out.println("rs.last returned false");
        }
      } else if (upcaseCommand.startsWith("RS.PREVIOUS")) {
        history.addElement(command);
        boolean ok = manualResultSet.previous();
        if (ok) {
          dispRow(out, manualResultSet, false, manualResultSetNumCols,
              manualResultSetColType, manualResultSetColumnLabel, null);
        } else {
          out.println("rs.previous returned false");
        }
      } else if (upcaseCommand.startsWith("RS.ABSOLUTE")) {
        history.addElement(command);
        String arg = command.substring(11).trim().toUpperCase();
        int pos = Integer.parseInt(arg);
        boolean ok = manualResultSet.absolute(pos);
        if (ok) {
          dispRow(out, manualResultSet, false, manualResultSetNumCols,
              manualResultSetColType, manualResultSetColumnLabel, null);
        } else {
          out.println("rs.absolute returned false");
        }
      } else if (upcaseCommand.startsWith("RS.RELATIVE")) {
        history.addElement(command);
        String arg = command.substring(11).trim().toUpperCase();
        int pos = Integer.parseInt(arg);
        boolean ok = manualResultSet.relative(pos);
        if (ok) {
          dispRow(out, manualResultSet, false, manualResultSetNumCols,
              manualResultSetColType, manualResultSetColumnLabel, null);
        } else {
          out.println("rs.relative returned false");
        }
      } else if (upcaseCommand.startsWith("LEAKVAR")) {
        leakvarHashtable = new Hashtable();
        putUserVariable("LEAKVAR", leakvarHashtable);

      } else if (upcaseCommand.startsWith("SETVAR")) {
        history.addElement(command);
        try {
          String left = command.substring(6).trim();
          int equalsIndex = left.indexOf("=");
          if (equalsIndex > 0) {
            String variableName = left.substring(0, equalsIndex).trim();
            left = left.substring(equalsIndex + 1);
            Object variable = callMethod(left, out);

            if (variable != null) {
              putUserVariable(variableName, variable);
              out.println(variableName + "=" + variable.toString());
            } else {
              out.println("ERROR:  Method not found or output is null");
            }
          } else {
            /* Check to see if we can set like a parameter */
            /* JWE */
            int spaceIndex = left.indexOf(" ");
            if (spaceIndex > 0) {
              String variableName = left.substring(0, spaceIndex).trim();
              left = left.substring(spaceIndex + 1);

              Object variable = getParameterObject(left, out);

              if (variable != null) {
                putUserVariable(variableName, variable);
                out.println(variableName + "=" + variable.toString());
              } else {
                out.println("ERROR:  Unable to get parameter ");
              }

            } else {
              out.println("ERROR:  '=' or ' ' not found after SETVAR [VAR]");
            }

          }

        } catch (Exception e) {
          e.printStackTrace(out);
        }
      } else if (upcaseCommand.startsWith("SETNEWVAR")) {
        history.addElement(command);
        try {
          String left = command.substring(9).trim();
          int equalsIndex = left.indexOf("=");
          if (equalsIndex > 0) {
            String variableName = left.substring(0, equalsIndex).trim();
            left = left.substring(equalsIndex + 1);
            Object variable = callNewMethod(left, out);

            if (variable != null) {
              putUserVariable(variableName, variable);
              out.println(variableName + "=" + variable.toString());
            } else {
              out.println("ERROR:  Method not found or output is null");
            }
          } else {
            out.println("line missing =");
          }

        } catch (Exception e) {
          e.printStackTrace(out);
        }
      } else if (upcaseCommand.startsWith("SHOWVARMETHODS")) {
        history.addElement(command);
        String left = command.substring(14).trim();
        showMethods(left, out);
      } else if (upcaseCommand.startsWith("THREAD ")) {
        history.addElement(command);
        String newcommand = command.substring(7).trim();
        out.println("Starting thread for " + newcommand);
        JDSQL400 runnable = new JDSQL400(newcommand, out);
        Thread t = new Thread(runnable);
        t.start();
        threads.add(t);
      } else if (upcaseCommand.startsWith("THREADPERSIST ")) {
        history.addElement(command);
        String threadName = command.substring(14).trim();
        out.println("Starting thread " + threadName);
        String newcommand = "PERSIST";
        JDSQL400 runnable = new JDSQL400(newcommand, out);
        variables.put(threadName, runnable);
        Thread t = new Thread(runnable);
        t.setName(threadName);
        t.setDaemon(true);
        t.start();
        threads.add(t);
        // Sleep to allow thread to do output before continuing
        try {
          Thread.sleep(100);
        } catch (Exception e) {
        }
      } else if (upcaseCommand.startsWith("THREADEXEC ")) {
        history.addElement(command);
        String remaining = command.substring(11).trim();
        int spaceIndex = remaining.indexOf(' ');
        if (spaceIndex > 0) {
          String threadName = remaining.substring(0, spaceIndex);
          String threadCommand = remaining.substring(spaceIndex + 1);
          JDSQL400 runnable = (JDSQL400) variables.get(threadName);
          if (runnable != null) {
            runnable.setCommand(threadCommand);

            // Sleep to allow thread to do output before continuing
            try {
              Thread.sleep(100);
            } catch (Exception e) {
            }

          } else {
            out.println("ERROR: Unable to find thread " + threadName);
          }
        } else {
          out.println("ERROR:  THREADEXEC: no space after thread name");
        }
      } else if (upcaseCommand.startsWith("REPEAT ")) {
        history.addElement(command);
        String left = command.substring(7).trim();
        int spaceIndex = left.indexOf(" ");
        if (spaceIndex > 0) {
          int repeatCount = Integer.parseInt(left.substring(0, spaceIndex));
          if (repeatCount > 0) {
            String newCommand = left.substring(spaceIndex).trim();
            int beginCount = repeatCount;
            int iteration = 1;
            while (repeatCount > 0) {
              if (!silent)
                out.println("Iteration " + iteration + " of " + beginCount);
              iteration++;
              executeCommand(newCommand, out);
              repeatCount--;
            }
          } else {
            out.println("Error.. invalid repeat count "
                + left.substring(0, spaceIndex));
          }
        } else {
          out.println("Error.  No count for repeat");
        }

      } else if (upcaseCommand.startsWith("CALLMETHOD")) {
        history.addElement(command);
        String left = command.substring(10).trim();

        Object obj;
        if (!silent) {
          obj = callMethod(left, out);
        } else {
          obj = callMethod(left, null);
        }
        if (!silent) {
          out.println("Call returned " + obj);
          if ((obj != null) && (obj instanceof InputStream)) {
            out.println("InputStream[ ");
            InputStream is = (InputStream) obj;
            int val = is.read();
            while (val > 0) {
              out.print(" " + Integer.toHexString(val));
              val = is.read();
            }
            out.println("]");
            is.close();
          }
          if ((obj != null) && (obj.getClass().isArray())) {
            int arrayLength = java.lang.reflect.Array.getLength(obj);
            out.println("  .. Array of size " + arrayLength);
            for (int i = 0; i < arrayLength; i++) {
              Object obj2 = java.lang.reflect.Array.get(obj, i);
              if (obj2 instanceof DriverPropertyInfo) {
                DriverPropertyInfo info = (DriverPropertyInfo) obj2;
                out.println("[" + i + "]=" + info.name + " " + info.value + " "
                    + info.description);
              } else if (obj2 instanceof java.lang.Byte) {
                int value = 0xff & ((java.lang.Byte) obj2).intValue();
                out.println("[" + i + "]=0x" + Integer.toHexString(value)
                    + " a[" + asciiChar(value) + "]" + " e[" + ebcdicChar(value)
                    + "]");
              } else {
                out.println(
                    "[" + i + "][" + obj2.getClass().getName() + "]=" + obj2);
              }
            }
          }
        } /* silent */
      } else {
        //
        // If not a blank line
        //
        if (upcaseCommand.length() != 0) {
          //
          // just attempt to execute the statement
          //
          if (con != null) {
            if (con != null) {
              if (stmt != null && reuseStatement) {
                // dont do anything -- reuse it
              } else {
		  if (stmt != null) {
		      stmt.close();
		      stmt = null; 
		  } 
                if (jdk14) {
                  stmt = con.createStatement(resultSetType,
                      resultSetConcurrency, resultSetHoldability);
                } else {
                  stmt = con.createStatement();
                }
                variables.put("STMT", stmt);
              }
            }
            if (queryTimeout != 0) {
              stmt.setQueryTimeout(queryTimeout);
            }
            if (measureExecute) {
              startTime = System.currentTimeMillis();
            }
            stmt.executeUpdate(command);
            history.addElement(command);
            if (measureExecute) {
              finishTime = System.currentTimeMillis();
              out.println("TIME: " + (finishTime - startTime) + " ms");
            }

            //
            // Don't forget to check for warnings...
            //
            SQLWarning warning = stmt.getWarnings();
            stmt.clearWarnings();
            if (warning != null) {
              if (!silent) {
                dispWarning(out, warning);
              }
            }

          } else {
            out.println("UNABLE to EXECUTE because not connected");
          }

        }
      }

    } catch (SQLException ex) {

      // A SQLException was generated. Catch it and
      // display the error information. Note that there
      // could be multiple error objects chained
      // together
      if (!silent) {
        out.println("\n*** SQLException caught ***");
        out.println("Statement was " + command);
        Throwable t = ex;
        while (t != null) {
          if (t instanceof SQLException) {
            ex = (SQLException) t;
            out.println("SQLState: " + ex.getSQLState());
            String exMessage;
            exMessage = cleanupMessage(ex);
            out.println("Message:  " + exMessage);
            out.println("Vendor:   " + ex.getErrorCode());
            if (debug | printStackTrace)
              ex.printStackTrace(out);
            if (poolConnection != null && ex.getErrorCode() == -901) {
              poolConnectionSql0901 = true;
            }

            String thisCode = Integer.toString(ex.getErrorCode());

            // System.out.println("Looking for \""+thisCode+"\"");
            Object avail = dumpCodes.get(thisCode);
            if ((dumpAnyError || avail != null) && (!dumpTaken)) {
              dumpTaken = true;
              try {
                //
                // We hit an error.. dump the server job log
                //
                // Check for CCSID 290/5026
                // If so, change the CCSID to 37
                //

                // Verify that the connection is usable. If rollback needed then
                // issue it
                try {
                  Statement stmtx = con.createStatement();
                  ResultSet rsx = stmtx
                      .executeQuery("select * from sysibm.sysdummy1");
                  stmtx.close();
                } catch (Exception e) {
                  out.println("Error checking connection");
                  e.printStackTrace(out);
                  if (e.toString().indexOf("ROLLBACK") >= 0) {
                    con.rollback();
                  }
                }

                String filename = "/tmp/jdsql400." + userId + ".out";
                // System.out.println("Dumping to "+filename);
                CallableStatement cstmt1 = con
                    .prepareCall("CALL QGPL.STPJOBLOG('" + filename + "')");
                cstmt1.execute();

                Hashtable mirrorJobs = new Hashtable();

                // System.out.println("Creating file");
                File file = new File(filename);
                if (!file.exists()) {
                  getFileFromSystem(con, filename);
                  file = new File(filename);
                }
                if (file.exists()) {
		    out.println("************************* BEGIN JOBLOG ************************"); 
                  int filterRow = 0;
                  int nestedRow = 0;
                  // System.out.println("creating reader");
                  FileReader fileReader = new FileReader(file);
                  // System.out.println("creating buffered reader");
                  BufferedReader bufferedReader = new BufferedReader(
                      fileReader);
                  // System.out.println("Reading line");
                  String oldMessage = "";
                  String line = bufferedReader.readLine();

                  while (line != null) {

                    // Check to see if we need to get the job log from the
                    // mirror job
                    int qdbmsrvrIndex = line.indexOf("QDBMSRVR");
                    if (qdbmsrvrIndex > 0) {
                      int lastSlashIndex = qdbmsrvrIndex - 1;
                      if ((lastSlashIndex > 1)
                          && (line.charAt(lastSlashIndex) == '/')) {
                        int firstSlashIndex = lastSlashIndex - 1;
                        while ((firstSlashIndex > 0)
                            && (line.charAt(firstSlashIndex) != '/')) {
                          firstSlashIndex--;
                        }
                        if (firstSlashIndex >= 6) {
                          int beginNumberIndex = firstSlashIndex - 6;
                          String jobname = line.substring(beginNumberIndex,
                              qdbmsrvrIndex + 8);
                          mirrorJobs.put(jobname, jobname);

                        } else {
                          out.println(
                              " ******************************************* ");
                          out.println(
                              "Warning: Did not find second / before QDBMSRVR in "
                                  + line);
                          out.println(
                              " ******************************************* ");
                        }
                      } else {
                        out.println(
                            " ******************************************* ");
                        out.println(
                            "Warning: Did not find / before QDBMSRVR in "
                                + line);
                        out.println(
                            " ******************************************* ");
                      }

                    } /* if qdbmsrvrIndex > 0 */

                    boolean filterFound = false;
                    for (int i = 0; filters[i] != null && !filterFound; i++) {
                      if (line.indexOf(filters[i][filterRow]) >= 0) {
                        filterRow++;
                        filterFound = true;
                      } else {
                        //
                        // Check for nesting -- only two levels supported
                        //
                        if (line.indexOf(filters[i][0]) >= 0) {
                          nestedRow = filterRow;
                          filterRow = 1;
                          filterFound = true;
                        } else {
                          //
                          // Check for return from nesting
                          //
                          if (line.indexOf(filters[i][nestedRow]) > 0) {
                            filterRow = nestedRow + 1;
                            filterFound = true;
                            nestedRow = 0;
                          }
                        }
                      }
                    } /* filters[i] loop */
                    //
                    // Filter out non useful stuff
                    //
                    if (!filterFound) {
                      if (dumpNMessages <= 0) {
                        // Print out the line
			  if (line.trim().length() > 0) { 
			      out.println(line);
			  }
                      } else {
                        if (messageStart(line)) {
                          if (oldMessage.length() > 0) {
                            saveMessage(oldMessage);
                          }
                          oldMessage = line + "\n";
                        } else {
                          oldMessage = oldMessage + line + "\n";
                        }
                      }
                      filterRow = 0;
                      nestedRow = 0;
                    }
                    line = bufferedReader.readLine();

                  } /* while line != null */
                  fileReader.close();
                  bufferedReader.close();
                  if (dumpNMessages > 0) {
                    // Save the last message
                    saveMessage(oldMessage);

                    dumpMessages(out);
                    // Allow errors to be dump again
                    dumpTaken = false;
                  }

                  Enumeration keys = mirrorJobs.keys();
                  while (keys.hasMoreElements()) {
                    String jobname = (String) keys.nextElement();
                    out.println(
                        "************************************************");
                    out.println("Attempting to dump job log for " + jobname);
                    out.println(
                        "************************************************");
                    try {
                      Connection mirrorCmdConn = DriverManager
                          .getConnection("jdbc:db2:DB2M_RMT", userId, password);

                      mirrorCmdConn.setTransactionIsolation(
                          Connection.TRANSACTION_READ_UNCOMMITTED);
                      String filename2 = "/tmp/jdsql400." + userId + ".out2";
                      CallableStatement cstmt2 = mirrorCmdConn
                          .prepareCall("CALL QGPL.STPJOBLOGX('" + filename2
                              + "','" + jobname + "')");
                      cstmt2.execute();
                      getFileFromSystem(mirrorCmdConn, filename2);
                      File file2 = new File(filename2);
                      FileReader fileReader1 = new FileReader(file2);
                      BufferedReader bufferedReader1 = new BufferedReader(
                          fileReader1);
                      line = bufferedReader1.readLine();
                      while (line != null) {
                        line = bufferedReader1.readLine();
			if (line.trim().length() > 0)  {
			    out.println(line);
			}
                      }
                      file2.delete();
                      mirrorCmdConn.close();
                      out.println(
                          "************************************************");
                      out.println(" End of joblog for " + jobname);
                      out.println(
                          "************************************************");
                    } catch (Exception e) {
                      out.println(
                          "************************************************");
                      out.println("Exception processing mirror job log ");
                      out.println(
                          "************************************************");
                      e.printStackTrace(out);
                      out.println(
                          "************************************************");
                    }

                  }

		  out.println("************************* END JOBLOG ************************"); 

		} /* if file exists */
                // if (cstmt1 != null)
                cstmt1.close();
                file.delete();
              } catch (Exception e) {
                out.println(
                    "Unable to get exception information using current connection");

                e.printStackTrace(out);
              }
            } // avail != null

            //
            // New code...
            //

            avail = dumpTraceCodes.get(thisCode);
            if (dumpTraceAnyError || avail != null) {
              dumpTraceInfo(out);
            } // avail != null

            t = ex.getNextException();
            if (t == null) {
              try {
                // get cause added in JDK 1.4 ignore if not there
                t = ex.getCause();
              } catch (Throwable t2) {

              }
            }
            out.println("");
          } else {
            // if (t != null) {
            t.printStackTrace(out);

            try {
              // get cause added in JDK 1.4 ignore if not there
              t = t.getCause();
            } catch (Throwable t2) {

            }

            // }
          }

        } // while
      }
    } catch (Exception e) {
      out.println("\n*** exception caught *** " + e);
      out.println("Statement was " + command);
      e.printStackTrace(out);
    } catch (java.lang.UnknownError jlu) {
      out.println("\n*** java.lang.UnknownError caught ***" + jlu);
      out.println("Statement was " + command);
      jlu.printStackTrace(out);

    } // catch
    finally {
      if (stmt != null) {
        try {
          if (!reuseStatement) {
            /*
             * This will be close the next time the statment is attempted to be
             * reused if (!manualFetch && closeStatementRS) { stmt.close(); stmt
             * = null; variables.remove("STMT"); }
             */
          }
        } catch (Exception e) {
          e.printStackTrace(out);
        }
      }
    }

    return returnCode;
  }

  private static void getFileFromSystem(Connection con1, String filename) {
    try {
      Statement stmt1 = con1.createStatement();
      ResultSet rs = stmt1.executeQuery("select get_clob_from_file('" + filename
          + "') from sysibm.sysdummy1");
      rs.next();
      Clob clob = rs.getClob(1);
      String data = clob.getSubString(1, (int) clob.length());
      PrintWriter writer = new PrintWriter(new FileWriter(filename));
      writer.println(data);
      writer.close();
      stmt1.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static Vector savedMessages = null;

  public static void saveMessage(String oldMessage) {
    if (savedMessages == null) {
      savedMessages = new Vector();
    }
    savedMessages.add(oldMessage);
    while (savedMessages.size() > dumpNMessages) {
      savedMessages.remove(0);
    }
  }

  // Is the line a start of a messages
  public static boolean messageStart(String line) {
    if (line.length() > 20) {
      String piece = line.substring(0, 20).trim();
      return piece.length() > 5;
    } else {
      return false;
    }
  }

  private static void dumpMessages(PrintStream out) {
    Enumeration enumeration = savedMessages.elements();
    while (enumeration.hasMoreElements()) {
      out.print(enumeration.nextElement());
    }

    savedMessages.clear();
  }

  public static void dumpTraceInfo(PrintStream out) {
    //
    // This dumps out the stored procedure trace information.
    //

    try {
      //
      // Execute the command to dump the trace to a QTEMP file.
      //

      Statement stmt1 = con.createStatement();
      try {
        stmt1.executeUpdate("DROP TABLE QTEMP.QAP0ZDMP");
      } catch (Exception e) {
      }

      stmt1.executeUpdate("CALL QSYS.QCMDEXC('DMPUSRTRC  ', 0000000010.00000)");

      //
      // Query the file and return the result.
      //
      String command = "SELECT CAST(QAP0ZDMP AS CHAR(512) CCSID 37) FROM QTEMP.QAP0ZDMP";
      ResultSet rs = stmt1.executeQuery(command);
      dispResultSet(out, rs, true);
      rs.close();
      stmt1.close(); 
    } catch (Exception e) {
      out.println("Exception encountered dumping SQJAVA trace information");
      e.printStackTrace(out);
    }

  }

  public static void main(String args[]) {
    prompt = true;

    String os = System.getProperty("os.name");

    if ((!os.equals("OS/400")) && (args.length < 3)) {
      System.out.println("Usage:  java sql400 <system> <userid> <password>");
    } else {
      System.out.println("os.version = " + os);

      if (args.length > 0) {
        system = args[0];
      }
      if (args.length > 1) {
        userId = args[1];
      }
      if (args.length > 2) {
        password = args[2];
      }

      // setup after setting system name
      setup();

      //
      // Look for a prompt property
      //

      String propertyString;
      prompt = true;
      try {
        propertyString = System.getProperty("prompt");
        if (propertyString != null) {
          propertyString = propertyString.toUpperCase().trim();
          if (propertyString.equals("FALSE")) {
            prompt = false;
          }
        }
      } catch (Exception dontCare) {
      }

      //
      // Look for a html property
      //

      html = false;
      try {
        propertyString = System.getProperty("html");
        if (propertyString != null) {
          propertyString = propertyString.toUpperCase().trim();
          if (propertyString.equals("TRUE")) {
            html = true;
          }
        }
      } catch (Exception dontCare) {
      }

      echoOff = false;
      try {
        propertyString = System.getProperty("echoOff");
        if (propertyString != null) {
          propertyString = propertyString.toUpperCase().trim();
          if (propertyString.equals("TRUE")) {
            echoOff = true;
          }
        }
      } catch (Exception dontCare) {
      }

      echoComments = false;
      try {
        propertyString = System.getProperty("echoComments");
        if (propertyString != null) {
          propertyString = propertyString.toUpperCase().trim();
          if (propertyString.equals("TRUE")) {
            echoComments = true;
          }
        }
      } catch (Exception dontCare) {
      }

      hideWarnings = false;
      try {
        propertyString = System.getProperty("hideWarnings");
        if (propertyString != null) {
          propertyString = propertyString.toUpperCase().trim();
          if (propertyString.equals("TRUE")) {
            hideWarnings = true;
          }
        }
      } catch (Exception dontCare) {
      }

      if (prompt)
        System.out.println("This test runs jdbc queries on " + system);

      String query = "SELECT * FROM SYSIBM.SYSDUMMY1";

      try {

        String osname = System.getProperty("os.name");
        if (debug)
          System.out.println("os.name is " + osname);
        String jdbclog = "jdbclog.out";
        if (osname.equals("AIX")) {
          jdbclog = "/tmp/" + System.getProperty("user.name") + ".jdbclog.out";
        }
        if (debug)
          System.out.println("log file is " + jdbclog);
        // PrintStream logOutput = new PrintStream(new
        // FileOutputStream(jdbclog));
        // DriverManager.setLogStream(logOutput);

        conName = "SQL400";

        //
        // Adjust the URL if needed
        //
        int localhostIndex = url.indexOf("localhost");
        if (system != "localhost" && localhostIndex > 0) {
          url = url.substring(0, localhostIndex) + system
              + url.substring(localhostIndex + 9);

        }

        // Attempt to connect to a driver. Each one
        // of the registered drivers will be loaded until
        // one is found that can process this URL
        if (debug)
          System.out.println("Attempting to connect to " + url);

        con = null;
        if (password != null) {
          con = DriverManager.getConnection(url, userId, password);
        } else {
          con = DriverManager.getConnection(url);
        }
        dumpTaken = false;
        variables.put("CON", con);

        conLabel = conName;
        if (conCount > 0)
          conLabel = conLabel + conCount;
        conCount++;
        JDJSTPTestcase.connectionConnect(con, conLabel,
            System.getProperty("java.home"));

        // If we were unable to connect, an exception
        // would have been thrown. So, if we get here,
        // we are successfully connected to the URL

        // Check for, and display and warnings generated
        // by the connect.

        checkForWarning(con.getWarnings());
        con.clearWarnings();

        // Get the DatabaseMetaData object and display
        // some information about the connection

        DatabaseMetaData dma = con.getMetaData();

        if (debug)
          System.out.println("\nConnected to " + dma.getURL());
        if (debug)
          System.out.println("Driver       " + dma.getDriverName());
        if (debug)
          System.out.println("Version      " + dma.getDriverVersion());
        if (debug)
          System.out.println("");

        if (prompt)
          System.out.println(
              "Welcome... Please enter sql statement, enter exit or quit to exit");
        if (prompt)
          System.out.println("Example: " + query);

        run(System.in, System.out);

        // Close the connection
        if (con != null) {
          JDJSTPTestcase.connectionDisconnect(con, conLabel);
          con.close();
          stmt = null;
          cstmt = null;
          cstmtSql = "";

        }
        if (poolConnection != null) {
          if (poolConnection != con) {
            poolConnection.close();
            cstmt = null;
            cstmtSql = "";

          }
        }

      } catch (SQLException ex) {

        // A SQLException was generated. Catch it and
        // display the error information. Note that there
        // could be multiple error objects chained
        // together
        if (html)
          System.out.println("<pre>");

        System.out.println("\n*** SQLException caught ***\n");

        while (ex != null) {
          System.out.println("SQLState: " + ex.getSQLState());
          String exMessage;
          exMessage = cleanupMessage(ex);
          System.out.println("Message:  " + exMessage);
          System.out.println("Vendor:   " + ex.getErrorCode());
          if (debug)
            ex.printStackTrace();
          ex = ex.getNextException();
          System.out.println("");
        }
        if (html)
          System.out.println("</pre>");

      } catch (java.lang.Exception ex) {

        // Got some other type of exception. Dump it.
        ex.printStackTrace();
      }
    } // not enough arguments
  } // main

  private static Object callMethod(String left, PrintStream out) {
    try {
      Object variable = null;
      int paramIndex = left.indexOf("(");
      if (paramIndex > 0) {
        int dotIndex = left.lastIndexOf(".", paramIndex);
        if (dotIndex > 0) {
          String callVariable = left.substring(0, dotIndex).trim();
          Object callObject = variables.get(callVariable);
          Class callClass = null;
          left = left.substring(dotIndex + 1).trim();
          paramIndex = left.indexOf("(");
          String methodName = left.substring(0, paramIndex).trim();
          left = left.substring(paramIndex + 1);
          if (callObject == null) {
            // Try to find the variable as a class
            try {
              callClass = Class.forName(callVariable);
            } catch (Exception e) {
            }
          }
          if (callObject != null || callClass != null) {

            if (paramIndex > 0) {
              Method[] methods = null;

              // getMethods does not work on connection object for
              // pre JDK 1.4

              if ((callObject instanceof Connection) && (!jdk14)) {
                if (methodName.equals("commit")) {

                  methods = new Method[1];
                  methods[0] = callObject.getClass().getMethod(methodName,
                      new Class[0]);
                } else {
                  // Try calling zero argument method
                  methods = new Method[1];
                  methods[0] = callObject.getClass().getMethod(methodName,
                      new Class[0]);
                }
              } else {
                if (callObject != null) {
                  methods = callObject.getClass().getMethods();
                } else {
                  // Note: callClass cannot be null because of callObject !=
                  // null || callClass != null condition above
                  if (callClass != null) {
                    methods = callClass.getMethods();
                  }
                }
              }
              boolean methodFound = false;
              boolean anyMethodFound = false;
              if (methods == null) {
                throw new Exception("Error:  methods is null");
              }
              /* Look for method and attempt to call it */
              /*
               * Some method invocations may fail. Record the errors and report
               * at the end
               */
              StringBuffer methodInvocationErrorsSb = new StringBuffer();
              for (int m = 0; !methodFound && (m < methods.length)
                  && (variable == null); m++) {
                int p = 0;
                int methodParameterCount = 0;

                if (methods[m].getName().equals(methodName)) {
                  Class[] parameterTypes = methods[m].getParameterTypes();
                  String argsLeft = left;
                  Object[] parameters = new Object[parameterTypes.length];
                  methodFound = true;
                  anyMethodFound = true;
                  methodParameterCount = parameterTypes.length;

                  String methodParameters = "";
                  for (p = 0; p < parameterTypes.length; p++) {
                    // System.methodInvocationErrorsSb.append("\n"+"Args left is
                    // "+argsLeft);
                    // Handle double quote delimited parameters strings
                    int argStartIndex = 0;
                    int argEndIndex = 0;
                    int nextArgIndex = 0;
                    if ((argsLeft.length() > 1) && ((argsLeft.charAt(0) == '"')
                        || (argsLeft.charAt(0) == '\''))) {
                      argStartIndex = 1;

                      argEndIndex = argsLeft.indexOf(argsLeft.charAt(0), 1);
                      if (argEndIndex > 0) {
                        // check for "," or ")"
                        if ((argsLeft.charAt(argEndIndex + 1) == ',')
                            || (argsLeft.charAt(argEndIndex + 1) == ')')) {
                          nextArgIndex = argEndIndex + 2;
                        } else {
                          methodInvocationErrorsSb
                              .append("\n[,)] does not follow #"
                                  + argsLeft.charAt(0) + "#");
                          argEndIndex = -1;
                        }
                      }
                    } else {
                      argEndIndex = argsLeft.indexOf(",");
                      if (argEndIndex < 0) {
                        argEndIndex = argsLeft.indexOf(")");
                      }
                      if (argEndIndex >= 0) {
                        nextArgIndex = argEndIndex + 1;
                      }
                    }
                    if (argEndIndex < 0) {
                      methodFound = false;
                      methodInvocationErrorsSb
                          .append("\nUnable to find arg with remaining args "
                              + argsLeft);
                      methodInvocationErrorsSb.append(
                          "\nNumber of parameters is " + parameterTypes.length);
                      methodFound = false;
                    } else {
                      if (argStartIndex != argEndIndex) {
                        String arg = argsLeft
                            .substring(argStartIndex, argEndIndex).trim();

                        argsLeft = argsLeft.substring(nextArgIndex);

                        //
                        // If the arg refers to a variable try to use it
                        //
                        Object argObject = variables.get(arg);
                        if (argObject != null) {
                          parameters[p] = argObject;
                        } else {

                          //
                          // Now convert the arg from a string into something
                          // else
                          //
                          String parameterTypeName = parameterTypes[p]
                              .getName();
                          methodParameters += parameterTypeName + " ";
                          if (arg.equals("null")) {
                            parameters[p] = null;
                          } else if (parameterTypeName
                              .equals("java.lang.String")
                              || parameterTypeName.equals("java.lang.Object")) {
                            parameters[p] = arg;
                          } else if (parameterTypeName.equals("int")) {
                            try {
                              parameters[p] = new Integer(arg);
                            } catch (Exception e) {
                              methodInvocationErrorsSb.append(
                                  "\nCould not parse " + arg + " as integer");
                              methodFound = false;
                            }
                          } else if (parameterTypeName.equals("boolean")) {
                            try {
                              parameters[p] = new Boolean(arg);
                            } catch (Exception e) {
                              methodInvocationErrorsSb.append(
                                  "\nCould not parse " + arg + " as integer");
                              methodFound = false;
                            }
                          } else if (parameterTypeName.equals("long")) {
                            try {
                              parameters[p] = new Long(arg);
                            } catch (Exception e) {
                              methodInvocationErrorsSb.append(
                                  "\nCould not parse " + arg + " as long");
                              methodFound = false;
                            }
                          } else if (parameterTypeName
                              .equals("[Ljava.lang.String;")) {
                            if (arg.charAt(0) == '[') {
                              String arrayString = arg.substring(1);
                              int len = arrayString.length();
                              int arrayCount = 1;
                              for (int i = 0; i < len; i++) {
                                if (arrayString.charAt(i) == '+')
                                  arrayCount++;
                              }
                              String[] a = new String[arrayCount];
                              parameters[p] = a;

                              int arrayIndex = 0;
                              int startIndex = 0;
                              int endIndex;
                              endIndex = arrayString.indexOf('+', startIndex);
                              if (endIndex < 0)
                                endIndex = arrayString.indexOf(']', startIndex);
                              while (endIndex > 0) {
                                if (arrayIndex < arrayCount) {
                                  a[arrayIndex] = arrayString
                                      .substring(startIndex, endIndex);
                                  arrayIndex++;
                                }
                                startIndex = endIndex + 1;
                                if (startIndex >= len) {
                                  endIndex = -1;
                                } else {
                                  endIndex = arrayString.indexOf('+',
                                      startIndex);
                                  if (endIndex < 0)
                                    endIndex = arrayString.indexOf(']',
                                        startIndex);
                                }
                              }

                            } else {
                              methodInvocationErrorsSb
                                  .append("\nCould not parse " + arg
                                      + " as String array .. try [A+B+C]");
                              methodFound = false;
                            }
                          } else if (parameterTypeName.equals("double")) {
                            try {
                              if (arg.equals("POSITIVE_INFINITY")) {
                                parameters[p] = new Double(
                                    Double.POSITIVE_INFINITY);
                              } else if (arg.equals("NEGATIVE_INFINITY")) {
                                parameters[p] = new Double(
                                    Double.NEGATIVE_INFINITY);
                              } else if (arg.equals("NaN")) {
                                parameters[p] = new Double(Double.NaN);
                              } else {
                                parameters[p] = new Double(arg);
                              }
                            } catch (Exception e) {
                              methodInvocationErrorsSb.append(
                                  "\nCould not parse " + arg + " as double ");
                              methodFound = false;
                            }

                          } else if (parameterTypeName.equals("[I")) {
                            if (arg.charAt(0) == '[') {
                              String arrayString = arg.substring(1);
                              int len = arrayString.length();
                              int arrayCount = 1;
                              for (int i = 0; i < len; i++) {
                                if (arrayString.charAt(i) == '+')
                                  arrayCount++;
                              }
                              int[] a = new int[arrayCount];
                              parameters[p] = a;

                              String piece = "";
                              try {
                                int arrayIndex = 0;
                                int startIndex = 0;
                                int endIndex;
                                endIndex = arrayString.indexOf('+', startIndex);
                                if (endIndex < 0)
                                  endIndex = arrayString.indexOf(']',
                                      startIndex);
                                while (endIndex > 0) {
                                  if (arrayIndex < arrayCount) {
                                    a[arrayIndex] = Integer.parseInt(arrayString
                                        .substring(startIndex, endIndex));
                                    arrayIndex++;
                                  }
                                  startIndex = endIndex + 1;
                                  if (startIndex >= len) {
                                    endIndex = -1;
                                  } else {
                                    endIndex = arrayString.indexOf('+',
                                        startIndex);
                                    if (endIndex < 0)
                                      endIndex = arrayString.indexOf(']',
                                          startIndex);
                                  }
                                }
                              } catch (Exception e) {
                                methodInvocationErrorsSb.append(
                                    "\nException " + e + " piece = " + piece);
                                methodInvocationErrorsSb
                                    .append("\nCould not parse " + arg
                                        + " as Integer.. try [1+2+3]");
                                methodFound = false;
                              }

                            } else {
                              methodInvocationErrorsSb
                                  .append("\nCould not parse " + arg
                                      + " as Integer.. try [1+2+3]");
                              methodFound = false;
                            }
                          } else {
                            methodInvocationErrorsSb
                                .append("\nDid not handle parameter with class "
                                    + parameterTypeName);
                            methodFound = false;
                          }
                        } /* parameter was a variable */
                      }
                    } /* unable to find args */
                  }
                  if (methodFound) {
                    if (p == methodParameterCount) {
                      if ((argsLeft.trim().equals(")"))
                          || (argsLeft.trim().length() == 0)) {
                        try {
                          variable = methods[m].invoke(callObject, parameters);
                        } catch (Exception e) {
                          methodInvocationErrorsSb.append("\n");
                          if (printCallMethodStackTrace) {
                            printStackTrace(methodInvocationErrorsSb, e);
                          } else {
                            methodInvocationErrorsSb.append(
                                "Exception caught " + e.toString() + "\n");
                            Throwable cause = e.getCause();
                            while (cause != null) {
                              methodInvocationErrorsSb.append(
                                  "  caused by " + cause.toString() + "\n");
                              cause = cause.getCause();
                            }

                          }
                          methodInvocationErrorsSb
                              .append("Calling method " + methodName + " with "
                                  + methodParameters + " failed");
                          methodFound = false;
                        }
                      } else {
                        methodInvocationErrorsSb.append("\nNot calling method "
                            + methodName + " with " + methodParameters
                            + " because argsLeft = " + argsLeft);
                        methodFound = false;
                      }
                    } else {
                      methodInvocationErrorsSb.append("\nNot calling method "
                          + methodName + " with " + methodParameters
                          + " because parsed parameter count = " + p);
                    }
                  } else {
                    methodInvocationErrorsSb
                        .append("\nMethod not found " + methodName);
                  }
                } /* method name matches */
              } /* looping through methods */
              if (!anyMethodFound) {
                if (out != null) {
                  out.println("ERROR:  Method not found " + methodName
                      + " errors are \n");
                  out.println(methodInvocationErrorsSb.toString());
                }
              } else {
                if (!methodFound) {
                  if (out != null) {
                    out.println("ERROR:  Could not call " + methodName + "\n");
                    out.println(methodInvocationErrorsSb.toString());
                  }
                }
              }

            } else {
              if (out != null) {
                out.println("ERROR:  could find ( in " + left);
              }
            }
          } else {
            if (out != null) {
              out.println(
                  "ERROR:  could not find variable or class " + callVariable);
              showValidVariables(out);
            }

          }
        } else {
          if (out != null) {
            out.println("ERROR:  could find . in " + left);
          }
        }
      } else {
        if (out != null) {
          out.println("ERROR:  could find ( in " + left);
        }
      }
      return variable;
    } catch (Exception e) {
      if (out != null) {
        out.println("Unexpected exception");
        e.printStackTrace(out);
      }
      return null;
    } catch (NoClassDefFoundError ncdfe) {
      if (out != null) {
        out.println("NoClassDefFoundError");
        ncdfe.printStackTrace(out);
      }
      return null;
    }
  }

  private static void printStackTrace(StringBuffer w, Exception e) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter p = new PrintWriter(stringWriter);
    e.printStackTrace(p);
    w.append(stringWriter.toString());
  }

  private static Object callNewMethod(String left, PrintStream out) {
    try {
      Object variable = null;
      int paramIndex = left.indexOf("(");
      if (paramIndex > 0) {
        String newClassName = left.substring(0, paramIndex).trim();
        Class newClass = null;
        left = left.substring(paramIndex + 1);
        // Try to find the variable as a class
        try {
          newClass = Class.forName(newClassName);
        } catch (Exception e) {
        }

        if (newClass != null) {
          if (paramIndex > 0) {
            Constructor[] constructors;
            constructors = newClass.getConstructors();
            boolean methodFound = false;
            StringBuffer methodInvocationErrorsSb = new StringBuffer();
            for (int m = 0; !methodFound && (m < constructors.length)
                && (variable == null); m++) {

              Class[] parameterTypes = constructors[m].getParameterTypes();
              String argsLeft = left;
              Object[] parameters = new Object[parameterTypes.length];
              methodFound = true;
              String methodParameters = "";
              for (int p = 0; p < parameterTypes.length; p++) {
                // out.println("Args left is "+argsLeft);
                // Handle double quote delimited parameters strings
                int argStartIndex = 0;
                int argEndIndex = 0;
                int nextArgIndex = 0;
                if ((argsLeft.length() > 1) && (argsLeft.charAt(0) == '"')) {
                  argStartIndex = 1;
                  argEndIndex = argsLeft.indexOf('"', 1);
                  if (argEndIndex > 0) {
                    // check for "," or ")"
                    if ((argsLeft.charAt(argEndIndex + 1) == ',')
                        || (argsLeft.charAt(argEndIndex + 1) == ')')) {
                      nextArgIndex = argEndIndex + 2;
                    } else {
                      methodInvocationErrorsSb
                          .append("\n[,)] does not follow ");
                      argEndIndex = -1;
                    }
                  }
                } else {
                  argEndIndex = argsLeft.indexOf(",");
                  if (argEndIndex < 0) {
                    argEndIndex = argsLeft.indexOf(")");
                  }
                  if (argEndIndex >= 0) {
                    nextArgIndex = argEndIndex + 1;
                  }
                }
                if (argEndIndex < 0) {
                  methodFound = false;
                  methodInvocationErrorsSb
                      .append("\nUnable to find arg in " + argsLeft);
                  methodFound = false;
                } else {
                  String arg = argsLeft.substring(argStartIndex, argEndIndex)
                      .trim();

                  argsLeft = argsLeft.substring(nextArgIndex);

                  //
                  // If the arg refers to a variable try to use it
                  //
                  Object argObject = variables.get(arg);
                  if (argObject != null) {
                    parameters[p] = argObject;
                  } else {

                    //
                    // Now convert the arg from a string into something else
                    //
                    String parameterTypeName = parameterTypes[p].getName();
                    methodParameters += parameterTypeName + " ";
                    if (arg.equals("null")) {
                      parameters[p] = null;
                    } else if (parameterTypeName.equals("java.lang.String")) {
                      parameters[p] = arg;
                    } else if (parameterTypeName.equals("int")) {
                      try {
                        parameters[p] = new Integer(arg);
                      } catch (Exception e) {
                        methodInvocationErrorsSb
                            .append("\nCould not parse " + arg + " as integer");
                        methodFound = false;
                      }
                    } else if (parameterTypeName.equals("boolean")) {
                      try {
                        parameters[p] = new Boolean(arg);
                      } catch (Exception e) {
                        methodInvocationErrorsSb
                            .append("\nCould not parse " + arg + " as integer");
                        methodFound = false;
                      }
                    } else if (parameterTypeName.equals("long")) {
                      try {
                        parameters[p] = new Long(arg);
                      } catch (Exception e) {
                        methodInvocationErrorsSb
                            .append("\nCould not parse " + arg + " as long");
                        methodFound = false;
                      }
                    } else if (parameterTypeName
                        .equals("[Ljava.lang.String;")) {
                      if (arg.charAt(0) == '[') {
                        String arrayString = arg.substring(1);
                        int len = arrayString.length();
                        int arrayCount = 1;
                        for (int i = 0; i < len; i++) {
                          if (arrayString.charAt(i) == '+')
                            arrayCount++;
                        }
                        String[] a = new String[arrayCount];
                        parameters[p] = a;

                        int arrayIndex = 0;
                        int startIndex = 0;
                        int endIndex;
                        endIndex = arrayString.indexOf('+', startIndex);
                        if (endIndex < 0)
                          endIndex = arrayString.indexOf(']', startIndex);
                        while (endIndex > 0) {
                          if (arrayIndex < arrayCount) {
                            a[arrayIndex] = arrayString.substring(startIndex,
                                endIndex);
                            arrayIndex++;
                          }
                          startIndex = endIndex + 1;
                          if (startIndex >= len) {
                            endIndex = -1;
                          } else {
                            endIndex = arrayString.indexOf('+', startIndex);
                            if (endIndex < 0)
                              endIndex = arrayString.indexOf(']', startIndex);
                          }
                        }

                      } else {
                        methodInvocationErrorsSb.append("\nCould not parse "
                            + arg + " as String array .. try [A+B+C]");
                        methodFound = false;
                      }
                    } else if (parameterTypeName.equals("[I")) {
                      if (arg.charAt(0) == '[') {
                        String arrayString = arg.substring(1);
                        int len = arrayString.length();
                        int arrayCount = 1;
                        for (int i = 0; i < len; i++) {
                          if (arrayString.charAt(i) == '+')
                            arrayCount++;
                        }
                        int[] a = new int[arrayCount];
                        parameters[p] = a;

                        String piece = "";
                        try {
                          int arrayIndex = 0;
                          int startIndex = 0;
                          int endIndex;
                          endIndex = arrayString.indexOf('+', startIndex);
                          if (endIndex < 0)
                            endIndex = arrayString.indexOf(']', startIndex);
                          while (endIndex > 0) {
                            if (arrayIndex < arrayCount) {
                              a[arrayIndex] = Integer.parseInt(
                                  arrayString.substring(startIndex, endIndex));
                              arrayIndex++;
                            }
                            startIndex = endIndex + 1;
                            if (startIndex >= len) {
                              endIndex = -1;
                            } else {
                              endIndex = arrayString.indexOf('+', startIndex);
                              if (endIndex < 0)
                                endIndex = arrayString.indexOf(']', startIndex);
                            }
                          }
                        } catch (Exception e) {
                          methodInvocationErrorsSb
                              .append("\nException " + e + " piece = " + piece);
                          methodInvocationErrorsSb.append("\nCould not parse "
                              + arg + " as Integer.. try [1+2+3]");
                          methodFound = false;
                        }

                      } else {
                        methodInvocationErrorsSb.append("\nCould not parse "
                            + arg + " as Integer.. try [1+2+3]");
                        methodFound = false;
                      }
                    } else {
                      methodInvocationErrorsSb
                          .append("\nDid not handle parameter with class "
                              + parameterTypeName);
                      methodFound = false;
                    }
                  } /* parameter was not a variable */
                } /* unable to find args */
              } /* looping through parameter types */
              if (methodFound) {
                if ((argsLeft.trim().equals(")"))
                    || (argsLeft.trim().length() == 0)) {
                  try {
                    variable = constructors[m].newInstance(parameters);
                  } catch (Exception e) {
                    methodInvocationErrorsSb.append("\n");
                    if (printCallMethodStackTrace) {
                      printStackTrace(methodInvocationErrorsSb, e);
                    } else {
                      methodInvocationErrorsSb
                          .append("Exception caught " + e.toString() + "\n");
                      Throwable cause = e.getCause();
                      while (cause != null) {
                        methodInvocationErrorsSb
                            .append("  caused by " + cause.toString() + "\n");
                        cause = cause.getCause();
                      }

                    }
                    methodInvocationErrorsSb.append("Creating object  with "
                        + methodParameters + " failed");
                    methodFound = false;
                  }
                } else {
                  methodInvocationErrorsSb.append(
                      "\nNot calling constructor " + " with " + methodParameters
                          + " because argsLeft = " + argsLeft);
                  methodFound = false;
                }
              } /* method not found */
            } /* for loop for constructors */
            if (!methodFound) {
              out.println("ERROR:  Could not call constructor\n");
              out.println(methodInvocationErrorsSb.toString());
            }

          } else {
            out.println("ERROR:  could find ( in " + left);
          }
        } else {
          out.println(
              "ERROR:  could not find variable or class " + newClassName);

        }
      } else {
        out.println("ERROR:  could find ( in " + left);
      }
      return variable;
    } catch (Exception e) {
      out.println("Unexpected exception");
      e.printStackTrace(out);
      return null;
    } catch (NoClassDefFoundError ncdfe) {
      out.println("NoClassDefFoundError");
      ncdfe.printStackTrace(out);
      return null;
    }
  }

  private static void showValidVariables(PrintStream out) {
    out.println("Valid variables are the following");
    Enumeration keys = variables.keys();
    while (keys.hasMoreElements()) {
      out.println(keys.nextElement());
    }
  }

  private static void showMethods(String left, PrintStream out)
      throws Exception {
    String callVariable = left.trim();
    Object callObject = variables.get(callVariable);
    Class callClass = null;
    if (callObject == null) {
      try {
        callClass = Class.forName(callVariable);
      } catch (Exception e) {
      }
    }
    if (callObject != null || callClass != null) {
      Method[] methods = null;
      ;
      if (callObject != null) {
        methods = callObject.getClass().getMethods();
      } else {
        if (callClass != null) {
          methods = callClass.getMethods();
        } else {
          throw new Exception("callClass is null");
        }
      }
      for (int m = 0; (m < methods.length); m++) {
        String methodInfo;
        Class returnType = methods[m].getReturnType();
        if (returnType != null) {
          methodInfo = returnType.getName() + " " + methods[m].getName();
        } else {
          methodInfo = "void " + methods[m].getName();
        }
        Class[] parameterTypes = methods[m].getParameterTypes();
        methodInfo += "(";
        for (int p = 0; p < parameterTypes.length; p++) {
          String parameterTypeName = parameterTypes[p].getName();
          if (p > 0)
            methodInfo += ",";
          methodInfo += parameterTypeName;
        }
        methodInfo += ")";
        out.println(methodInfo);
      }
    } else {
      out.println("Could not find variable " + callVariable);
      showValidVariables(out);
    }
  }

  // -------------------------------------------------------------------
  // checkForWarning
  // Checks for and displays warnings. Returns true if a warning
  // existed
  // -------------------------------------------------------------------

  private static boolean checkForWarning(SQLWarning warn) throws SQLException {
    boolean rc = false;

    // If a SQLWarning object was given, display the
    // warning messages. Note that there could be
    // multiple warnings chained together

    if (warn != null) {
      System.out.println("\n *** Warning ***\n");
      rc = true;
      while (warn != null) {
        System.out.println("SQLState: " + warn.getSQLState());
        String exMessage;
        exMessage = cleanupMessage(warn);
        System.out.println("Message:  " + exMessage);
        System.out.println("Vendor:   " + warn.getErrorCode());
        System.out.println("");
        warn = warn.getNextWarning();
      }
    }
    return rc;
  }

  private static String[] dispColumnHeadings(PrintStream out, ResultSet rs,
      ResultSetMetaData rsmd, boolean trim, int numCols) throws SQLException {
    int i;
    // Display column headings

    // Build up the output so it can be sent as a single System.out.println()
    StringBuffer output = new StringBuffer();

    if (html) {
      output.append("<table border>\n");
    }

    String[] columnLabel = new String[numCols + 1];

    for (i = 1; i <= numCols; i++) {
      columnLabel[i] = rsmd.getColumnLabel(i);
      if (html) {
        output.append("<th>" + columnLabel[i].replace('_', ' '));
      } else {
        if (!xml) {
          if (i > 1)
            output.append(",");
          output.append(columnLabel[i]);
        }
      }
    }
    if (html)
      output.append("<tr>\n");
    if (xml) {
      output.append("<table>");
      out.println(output.toString());
    } else {
      output.append("");
      out.println(output.toString());
    }

    return columnLabel;
  }

  /* @SuppressWarnings("fallthrough") */
  private static void dispRow(PrintStream out, ResultSet rs, boolean trim,
      int numCols, int colType[], String columnLabel[], String format[])
      throws SQLException {
    int i;
    StringBuffer output = new StringBuffer();
    if (xml)
      output.append("<row>\n");
    for (i = 1; i <= numCols; i++) {
      if (html) {
        output.append("<td>");
      } else if (xml) {
        output.append("   <" + columnLabel[i] + ">");
      } else {
        if (i > 1)
          output.append(",");
      }
      //
      // Handle blob and binary types...
      //

      switch (colType[i]) {

      case 2004: // Types.BLOB
      // case Types.BLOB:
      {
        Blob blob = rs.getBlob(i);
        if (blob != null) {
          if (blob.getClass().getName()
              .equals("com.ibm.db2.jdbc.app.DB2BlobLocator")) {
            try {
              int loc = JDReflectionUtil.callMethod_I(blob, "getLocator");
              output.append("L#" + loc + ":");
            } catch (Exception e) {
              // just ignore
            }
          }
        }
      }
      // Fall through
      case Types.BINARY:
      case Types.VARBINARY:
      case Types.LONGVARBINARY:
      case -8: // rowId
        byte bytes[] = rs.getBytes(i);
        if (bytes == null) {
          output.append(rs.getString(i));
        } else {
          if (bytes.length < showLobThreshold) {
            for (int j = 0; j < bytes.length; j++) {
              int showInt = bytes[j] & 0xFF;
              if (showInt >= 0x10) {
                output.append(Integer.toHexString(showInt));
              } else {
                output.append("0" + Integer.toHexString(showInt));
              }
            }
          } else {
            CRC32 checksum = new CRC32();
            checksum.update(bytes);
            output.append("ARRAY[size=" + bytes.length + ",CRC32="
                + checksum.getValue() + "]");
          }
        }
        break;
      case Types.BOOLEAN:
        boolean bool = rs.getBoolean(i);
        if (rs.wasNull()) {
          output.append("null");
        } else if (bool == true) {
          output.append("true");
        } else {
          output.append("false");
        }
        break;
      default: {
        String outString = rs.getString(i);
        if (trim && outString != null)
          outString = outString.trim();
        if (format != null && (i - 1) < format.length
            && format[i - 1] != null) {
          outString = formatString(outString, format[i - 1]);
        }
        appendUnicodeString(output, outString);
      }
        break;
      }
      if (xml) {
        output.append("</" + columnLabel[i] + ">\n");
      }
    } /* for i */
    if (html) {
      out.println(output.toString() + "<tr>");
    } else if (xml) {
      out.println(output.toString() + "</row>");
    } else {
      out.println(output.toString());
    }
    // Make the row immediately visible
    out.flush();

  }
  // -------------------------------------------------------------------
  // dispResultSet
  // Displays all columns and rows in the given result set
  // -------------------------------------------------------------------

  private static String stripTS(String s) {
    s = s.replace(' ', '-');
    s = s.replace(':', '.');
    int i = s.indexOf(".000000");
    if (i > 0) {
      s = s.substring(0, i);
    }
    return s;
  }

  private static String formatString(String outString, String format) {
    if (format != null) {
      Timestamp compareTimestamp = null;
      int newTimestampIndex = format.indexOf("{NEWERTS-");
      if (newTimestampIndex == 0) {
        int braceIndex = format.indexOf("}");
        if (braceIndex > 0) {
          String ts = format.substring(9, braceIndex);
          compareTimestamp = Timestamp.valueOf(ts);
          format = format.substring(braceIndex + 1);
        }
      }
      int tsdifIndex = format.indexOf("{TSDIF.MIN}");
      if (tsdifIndex >= 0) {
        String before = "";
        if (tsdifIndex > 0) {
          before = formatString(outString, format.substring(0, tsdifIndex));
        }

        int value = (int) Double.parseDouble(outString);
        /* DDHHMMSS. */
        int days = value / 1000000;
        int hours = value / 10000 % 100;
        int minutes = value / 100 % 100;
        int seconds = value % 100;
        double calculatedMinutes = days * 24 * 60 + hours * 60.0 + minutes
            + seconds / 60.0;
        return before + calculatedMinutes
            + formatString(outString, format.substring(tsdifIndex + 11));

      }

      int replaceIndex = format.indexOf("{STUFF}");
      if (replaceIndex > 0) {
        return formatString(outString, format.substring(0, replaceIndex))
            + outString
            + formatString(outString, format.substring(replaceIndex + 7));
      } else if (replaceIndex == 0) {
        return outString
            + formatString(outString, format.substring(replaceIndex + 7));
      } else {
        replaceIndex = format.indexOf("{STRIPPEDTS}");
        if (replaceIndex >= 0) {
          String pre = "";
          String post = "";
          if (compareTimestamp != null) {
            Timestamp thisTimestamp = Timestamp.valueOf(outString);
            if (thisTimestamp.getTime() < compareTimestamp.getTime()) {
              pre = "<font color=\"red\">OLD";
              post = "</font>";
            }
          }
          if (replaceIndex > 0) {
            return pre
                + formatString(outString, format.substring(0, replaceIndex))
                + stripTS(outString)
                + formatString(outString, format.substring(replaceIndex + 12))
                + post;
          } else if (replaceIndex == 0) {
            return pre + stripTS(outString)
                + formatString(outString, format.substring(replaceIndex + 12))
                + post;
          } else {
            // Not reachable, but compiler doesn't know
            // Wil fall to end and outString will be returned

          }
        } else {

          replaceIndex = format.indexOf("{PART");
          if (replaceIndex >= 0) {
            int endBrace = format.indexOf("}", replaceIndex);
            int length = endBrace - replaceIndex + 1;
            int number = format.charAt(replaceIndex + 5) - '0';
            String separator = format.substring(replaceIndex + 9, endBrace);
            String part = getNthItem(outString, separator, number);
            if (replaceIndex > 0) {
              return formatString(outString, format.substring(0, replaceIndex))
                  + part + formatString(outString,
                      format.substring(replaceIndex + length));
            } else { /* index must be zero */
              return part + formatString(outString,
                  format.substring(replaceIndex + length));
            }
          } else {
            return format;
          }
        } /* not STRIPPEDTS */
      } /* not STUFF */
    } else {
      return outString;
    }
    return outString;
  }

  private static String getNthItem(String outString, String separator,
      int number) {
    String rest = outString;
    int separatorLength = separator.length();
    int separatorIndex = rest.indexOf(separator);
    int count = 1;
    while (separatorIndex > 0) {
      if (count == number) {
        return rest.substring(0, separatorIndex);
      }
      rest = rest.substring(separatorIndex + separatorLength);
      separatorIndex = rest.indexOf(separator);
      count++;
    }
    return rest;
  }

  public static void runNewQuery(Connection c, String sql) {
    String threadName = Thread.currentThread().getName();
    try { 
     int count = 0; 
     Statement stmt = c.createStatement(); 
     long startMillis = System.currentTimeMillis(); 
     System.out.println("Thread "+threadName+" running "+sql); 
     ResultSet rs = stmt.executeQuery(sql); 
     while (rs.next()) {
       count++; 
     }
     rs.close(); 
     stmt.close(); 
     System.out.println("Thread "+threadName+" read "+count+" rows after "+ (System.currentTimeMillis() - startMillis) +" ms"); 
    } catch (Exception e) { 
      synchronized(System.out) { 
        System.out.println("Thread "+threadName+" caught exception ");
        e.printStackTrace(System.out);
      }
    }
  }
  public static void dispResultSet(ResultSet rs) throws SQLException {
    dispResultSet(System.out, rs, false, null);
  }

  public static void dispResultSet(PrintStream out, ResultSet rs)
      throws SQLException {
    dispResultSet(out, rs, false, null);
  }

  static void dispResultSet(PrintStream out, ResultSet rs, boolean trim)
      throws SQLException {
    dispResultSet(out, rs, trim, null);
  }

  static void dispResultSet(PrintStream out, ResultSet rs, boolean trim,
      String[] format) throws SQLException {
    int i;

    // Get the ResultSetMetaData. This will be used for
    // the column headings

    ResultSetMetaData rsmd = rs.getMetaData();

    // Get the number of columns in the result set

    int numCols = rsmd.getColumnCount();

    String[] columnLabel = dispColumnHeadings(out, rs, rsmd, trim, numCols);

    //
    // figure out column types
    //
    int colType[] = new int[numCols + 1];
    for (i = 1; i <= numCols; i++) {
      colType[i] = rsmd.getColumnType(i);
      // if (false) System.out.println("Type of column "+i+" is "+colType[i]);
    }

    // Display data, fetching until end of the result set

    boolean more = false;
    more = rs.next();
    while (more) {

      // Loop through each column, getting the
      // column data and displaying
      dispRow(out, rs, trim, numCols, colType, columnLabel, format);

      //
      // Check for warnings.
      //
      SQLWarning warning = rs.getWarnings();
      rs.clearWarnings();
      if (warning != null) {
        dispWarning(out, warning);
      }

      // Fetch the next result set row
      more = rs.next();
    }

    if (html)
      out.println("</table>");
    if (xml)
      out.println("</table>");

  }

  private static void dispWarning(PrintStream out, SQLWarning warning) {
    if (hideWarnings) {
      return;
    }
    if (warning != null) {
      out.println("\n *** Warning ***\n");
      if (html)
        System.out.println("<br>");
      while (warning != null) {
        out.println("SQLState: " + warning.getSQLState());
        if (html)
          System.out.println("<br>");
        out.println("Message:  " + cleanupMessage(warning));
        if (html)
          System.out.println("<br>");
        out.println("Vendor:   " + warning.getErrorCode());
        if (html)
          System.out.println("<br>");
        out.println("");
        if (html)
          System.out.println("<br>");
        warning = warning.getNextWarning();
      }

    }
  }

  private static String cleanupMessage(SQLException ex) {
    String message = ex.getMessage();
    boolean invalidCharacter = false;
    //
    // Check to see if invalid character
    //
    char chars[] = message.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      if ((chars[i] < 0x20) || (chars[i] > 0x7e)) {
        invalidCharacter = true;
        chars[i] = '*';
      }
    }
    if (invalidCharacter) {
      message = new String(chars);
    }

    //
    // Check for JCC message and cleanup if needed
    //
    if (db2DiagResolved == false) {
      try {
        db2DiagClass = Class.forName("com.ibm.db2.jcc.DB2Diagnosable");
      } catch (Exception e) {
        if (debug) {
          e.printStackTrace();
        }
      }
      db2DiagResolved = true;
    }

    if (db2DiagClass != null) {
      try {
        Class objectClass = ex.getClass();
        if (debug) {
          System.out.println("Class of exception is " + objectClass);
        }
        if (db2DiagClass.isAssignableFrom(objectClass)) {
          // DB2Diagnosable db2e = (DB2Diagnosable)e
          // DB2Sqlca sqlca = db2e.getSqlca();
          java.lang.reflect.Method method;
          Class[] argTypes = new Class[0];
          Object[] args = new Object[0];
          method = objectClass.getMethod("getSqlca", argTypes);
          Object sqlca = method.invoke(ex, args);
          if (sqlca != null) {
            // sqlca.getMessage()
            Class sqlcaClass = sqlca.getClass();
            method = sqlcaClass.getMethod("getMessage", argTypes);
            String sqlcaMessage = (String) method.invoke(sqlca, args);

            message += " : jccSQLCA[" + sqlcaMessage + "]";
          } else {
            message += " : jccSQLCA[noSQLCA]";
          }

        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return message;

  }

  private static boolean db2DiagResolved = false;
  private static Class db2DiagClass = null;

  private static void printArray(PrintStream out, Array outArray)
      throws SQLException {
    if (outArray == null) {
      out.print("nullArray");
    } else {
      Object[] array = (Object[]) outArray.getArray();
      if (array == null) {
        out.print("XXXX_null_returned_from_outArray.getArray");
      } else {
        String componentTypeName = array.getClass().getComponentType()
            .getName();
        out.print(componentTypeName + "[" + array.length + "]=[");
        for (int i = 0; i < array.length; i++) {
          if (i > 0)
            out.print(",");
          if (array[i] == null) {
            out.print("null");
          } else {
            if (componentTypeName.equals("[B")) {
              out.print(dumpBytes((byte[]) array[i]));
            } else if (array[i] instanceof java.sql.Blob) {
              java.sql.Blob blob = (java.sql.Blob) array[i];
              long length = blob.length();
              out.print(dumpBytes(blob.getBytes(1, (int) length)));
            } else if (array[i] instanceof java.sql.Clob) {
              java.sql.Clob clob = (java.sql.Clob) array[i];
              long length = clob.length();
	      String outString = clob.getSubString(1, (int) length);
              printUnicodeString(out, outString);
            } else if (array[i] instanceof String) {
              printUnicodeString(out, (String) array[i]);
            } else {
              out.print(array[i].toString());
            }
          }
        }
        out.print("]");
      }
    }

  }

  static void printArray(PrintWriter out, Array outArray) throws SQLException {
    if (outArray == null) {
      out.print("nullArray");
    } else {
      Object[] array = (Object[]) outArray.getArray();
      if (array == null) {
        out.print("XXXX_null_returned_from_outArray.getArray");
      } else {
        String componentTypeName = array.getClass().getComponentType()
            .getName();
        out.print(componentTypeName + "[" + array.length + "]=[");
        for (int i = 0; i < array.length; i++) {
          if (i > 0)
            out.print(",");
          if (array[i] == null) {
            out.print("null");
          } else {
            if (componentTypeName.equals("[B")) {
              out.print(dumpBytes((byte[]) array[i]));
            } else if (array[i] instanceof java.sql.Blob) {
              java.sql.Blob blob = (java.sql.Blob) array[i];
              long length = blob.length();
              out.print(dumpBytes(blob.getBytes(1, (int) length)));
            } else if (array[i] instanceof java.sql.Clob) {
              java.sql.Clob clob = (java.sql.Clob) array[i];
              long length = clob.length();
              printUnicodeString(out, clob.getSubString(1, (int) length));
            } else if (array[i] instanceof String) {
              printUnicodeString(out, (String) array[i]);
            } else {
              out.print(array[i].toString());
            }
          }
        }
        out.print("]");
      }
    }

  }

  static void printUnicodeString(PrintStream out, String outString) {
    //
    // Check length
    //
    if (outString != null) {
      int length = outString.length();
      if (length > showLobThreshold) {
        out.print("CHARARRAY[size=" + length + ",CRC32=" + getCRC32(outString)
            + "]->");
        if (!characterDetails) {
          outString = outString.substring(0, stringSampleSize);
        }
      }
    }
    //
    // See if all the characters are 7 bit ASCII.. If so just print
    //
    if (outString != null) {
      char chars[] = outString.toCharArray();
      boolean nonAsciiFound = false;
      for (int i = 0; !nonAsciiFound && i < chars.length; i++) {
        if (chars[i] != 0x0d && chars[i] != 0x0a && chars[i] != 0x09
            && (chars[i] >= 0x7F || chars[i] < 0x20)) {
          nonAsciiFound = true;
        }
      }
      if (!nonAsciiFound) {
        out.print(outString);
      } else {
        if (showCharNames) {
          showCharNamesOutput(out, chars);
        } else if (showMixedUX) {
          showMixedUXOutput(out, chars);
        } else {
          out.print("U'");
          for (int i = 0; i < chars.length; i++) {
            int showInt = chars[i] & 0xFFFF;

            if ((showInt > 0xFF00 && showInt < 0xFF5F) || (showInt == 0x3000)) {
              //
              // Show fat characters - right now don't worry that its in the U'
              //
              if (showInt == 0x3000) {
                out.print("| ");
              } else {
                out.print('|');
                out.print((char) (showInt - 0xFEE0));
              }
            } else {
              String showString = Integer.toHexString(showInt);
              if (showInt >= 0x1000) {
                out.print(showString);
              } else if (showInt >= 0x0100) {
                out.print("0" + showString);
              } else if (showInt >= 0x0010) {
                out.print("00" + showString);
              } else {
                out.print("000" + showString);
              }
            }
          }
          out.print("'");
        }
      }
    } else {
      out.print(outString);
    }
  }

  static void printUnicodeString(PrintWriter out, String outString) {
    //
    // Check length
    //
    if (outString != null) {
      int length = outString.length();
      if (length > showLobThreshold) {
        out.print("CHARARRAY[size=" + length + ",CRC32=" + getCRC32(outString)
            + "]->");
        if (!characterDetails) {
          outString = outString.substring(0, stringSampleSize);
        }
      }
    }
    //
    // See if all the characters are 7 bit ASCII.. If so just print
    //
    if (outString != null) {
      char chars[] = outString.toCharArray();
      boolean nonAsciiFound = false;
      for (int i = 0; !nonAsciiFound && i < chars.length; i++) {
        if (chars[i] != 0x0d && chars[i] != 0x0a && chars[i] != 0x09
            && (chars[i] >= 0x7F || chars[i] < 0x20)) {
          nonAsciiFound = true;
        }
      }
      if (!nonAsciiFound) {
        out.print(outString);
      } else {
        if (showCharNames) {
          showCharNamesOutput(out, chars);
        } else if (showMixedUX) {
          showMixedUXOutput(out, chars);
        } else {
          out.print("U'");
          for (int i = 0; i < chars.length; i++) {
            int showInt = chars[i] & 0xFFFF;

            if ((showInt > 0xFF00 && showInt < 0xFF5F) || (showInt == 0x3000)) {
              //
              // Show fat characters - right now don't worry that its in the U'
              //
              if (showInt == 0x3000) {
                out.print("| ");
              } else {
                out.print('|');
                out.print((char) (showInt - 0xFEE0));
              }
            } else {
              String showString = Integer.toHexString(showInt);
              if (showInt >= 0x1000) {
                out.print(showString);
              } else if (showInt >= 0x0100) {
                out.print("0" + showString);
              } else if (showInt >= 0x0010) {
                out.print("00" + showString);
              } else {
                out.print("000" + showString);
              }
            }
          }
          out.print("'");
        }
      }
    } else {
      out.print(outString);
    }
  }

  private static void appendUnicodeString(StringBuffer sb, String outString) {
    //
    // Check length
    //
    if (outString != null) {
      int length = outString.length();
      if (length > showLobThreshold) {
        sb.append("CHARARRAY[size=" + length + ",CRC32=" + getCRC32(outString)
            + "]->");
        if (!characterDetails) {
          outString = outString.substring(0, stringSampleSize);
        }
      }
    }
    //
    // See if all the characters are 7 bit ASCII.. If so just print
    //
    if (outString != null) {
      char chars[] = outString.toCharArray();
      boolean nonAsciiFound = false;
      for (int i = 0; !nonAsciiFound && i < chars.length; i++) {
        if (chars[i] != 0x0d && chars[i] != 0x0a && chars[i] != 0x09
            && (chars[i] >= 0x7F || chars[i] < 0x20)) {
          nonAsciiFound = true;
        }
      }
      if (!nonAsciiFound) {
        sb.append(outString);
      } else {
        if (showCharNames) {
          appendCharNamesOutput(sb, chars);
        } else if (showMixedUX) {
          appendMixedUXOutput(sb, chars);
        } else {
          sb.append("U'");
          for (int i = 0; i < chars.length; i++) {
            int showInt = chars[i] & 0xFFFF;

            if ((showInt > 0xFF00 && showInt < 0xFF5F) || (showInt == 0x3000)) {
              //
              // Show fat characters - right now don't worry that its in the U'
              //
              if (showInt == 0x3000) {
                sb.append("| ");
              } else {
                sb.append('|');
                sb.append((char) (showInt - 0xFEE0));
              }
            } else {
              String showString = Integer.toHexString(showInt);
              if (showInt >= 0x1000) {
                sb.append(showString);
              } else if (showInt >= 0x0100) {
                sb.append("0" + showString);
              } else if (showInt >= 0x0010) {
                sb.append("00" + showString);
              } else {
                sb.append("000" + showString);
              }
            }
          }
          sb.append("'");
        }
      }
    } else {
      sb.append(outString);
    }
  }

  public static void showMixedUXOutput(PrintStream out, char[] chars) {
    boolean inUX = false;
    for (int i = 0; i < chars.length; i++) {
      int showInt = chars[i] & 0xFFFF;

      if ((showInt == 0x0d) || (showInt == 0x0a)
          || (showInt >= 0x20 && showInt < 0x7F)) {
        if (inUX) {
          out.print("''");
          inUX = false;
        }
        out.print(chars[i]);
      } else {
        if (!inUX) {
          inUX = true;
          out.print("UX''");
        }

        String showString = Integer.toHexString(showInt);

        if (showInt >= 0x1000) {
          out.print(showString);
        } else if (showInt >= 0x0100) {
          out.print("0" + showString);
        } else if (showInt >= 0x0010) {
          out.print("00" + showString);
        } else {
          out.print("000" + showString);
        }
      }
    } /* for */
    if (inUX) {
      out.print("''");
    }
  }

  public static void showMixedUXOutput(PrintWriter out, char[] chars) {
    boolean inUX = false;
    for (int i = 0; i < chars.length; i++) {
      int showInt = chars[i] & 0xFFFF;

      if (showInt == 0x0d || showInt == 0x0a
          || (showInt >= 0x20 && showInt < 0x7F)) {
        if (inUX) {
          out.print("''");
          inUX = false;
        }
        out.print(chars[i]);
      } else {
        if (!inUX) {
          inUX = true;
          out.print("UX''");
        }

        String showString = Integer.toHexString(showInt);

        if (showInt >= 0x1000) {
          out.print(showString);
        } else if (showInt >= 0x0100) {
          out.print("0" + showString);
        } else if (showInt >= 0x0010) {
          out.print("00" + showString);
        } else {
          out.print("000" + showString);
        }
      }
    } /* for */
    if (inUX) {
      out.print("''");
    }
  }

  public static void appendMixedUXOutput(StringBuffer sb, char[] chars) {
    boolean inUX = false;
    for (int i = 0; i < chars.length; i++) {
      int showInt = chars[i] & 0xFFFF;

      if (showInt == 0x0d || showInt == 0x0a
          || (showInt >= 0x20 && showInt < 0x7F)) {
        if (inUX) {
          sb.append("''");
          inUX = false;
        }
        sb.append(chars[i]);
      } else {
        if (!inUX) {
          inUX = true;
          sb.append("UX''");
        }

        String showString = Integer.toHexString(showInt);

        if (showInt >= 0x1000) {
          sb.append(showString);
        } else if (showInt >= 0x0100) {
          sb.append("0" + showString);
        } else if (showInt >= 0x0010) {
          sb.append("00" + showString);
        } else {
          sb.append("000" + showString);
        }
      }
    } /* for */
    if (inUX) {
      sb.append("''");
    }
  }

  static Hashtable hexToName = null;
  static String[][] hexToNameArray = { { "1a", "SUB" }, { "a1", "INVEXCL" },
      { "a2", "CENT" }, { "a3", "POUND" }, { "a4", "CURRENT" }, { "a5", "YEN" },
      { "a6", "BBAR" }, { "a7", "SECTION" }, { "a8", "DIAERESIS" },
      { "a9", "(C)" }, { "aa", "_a_" }, { "ab", "<<" }, { "ac", "NOT" },
      { "ad", "-" }, { "ae", "(R)" }, { "af", "MACRON" }, { "b0", "DEG" },
      { "b1", "+-" }, { "b2", "^2" }, { "b3", "^3" }, { "b4", "ACCUTE" },
      { "b5", "MICRON" }, { "b6", "PP" }, { "b7", "DOT" }, { "b8", "CEDILLA" },
      { "b9", "^1" }, { "ba", "^o" }, { "bb", ">>" }, { "bc", "1/4" },
      { "bd", "1/2" }, { "be", "3/4" }, { "bf", "INV?" }, { "c0", "A`" },
      { "c1", "A'" }, { "c2", "A^" }, { "c3", "A~" }, { "c4", "A:" },
      { "c5", "Ao" }, { "c6", "AE" }, { "c7", "C," }, { "c8", "E`" },
      { "c9", "E'" }, { "ca", "E^" }, { "cb", "E:" }, { "cc", "I`" },
      { "cd", "I'" }, { "ce", "I^" }, { "cf", "I:" }, { "d0", "D-" },
      { "d1", "N~" }, { "d2", "O`" }, { "d3", "O'" }, { "d4", "O^" },
      { "d5", "O~" }, { "d6", "O:" }, { "d7", "X" }, { "d8", "0/" },
      { "d9", "U`" }, { "da", "U'" }, { "db", "U^" }, { "dc", "U:" },
      { "dd", "Y'" }, { "de", "THORN" }, { "df", "ss" }, { "e0", "a`" },
      { "e1", "a'" }, { "e2", "a^" }, { "e3", "a~" }, { "e4", "a:" },
      { "e5", "ao" }, { "e6", "ae" }, { "e7", "c," }, { "e8", "e`" },
      { "e9", "e'" }, { "ea", "e^" }, { "eb", "e:" }, { "ec", "i`" },
      { "ed", "i'" }, { "ee", "i^" }, { "ef", "i:" }, { "f0", "d" },
      { "f1", "n~" }, { "f2", "o`" }, { "f3", "o'" }, { "f4", "o^" },
      { "f5", "o~" }, { "f6", "o:" }, { "f7", "DIV" }, { "f8", "o/" },
      { "f9", "u`" }, { "fa", "u'" }, { "fb", "u^" }, { "fc", "u:" },
      { "fd", "y'" }, { "fe", "pp" }, { "ff", "y:" }, { "fffd", "SUB" },

  };

  static void initializeHexToName() {
    if (hexToName == null) {
      hexToName = new Hashtable();
      for (int i = 0; i < hexToNameArray.length; i++) {
        hexToName.put(hexToNameArray[i][0], hexToNameArray[i][1]);
      }
    }
  }

  public static void showCharNamesOutput(PrintStream out, char[] chars) {
    if (hexToName == null)
      initializeHexToName();

    for (int i = 0; i < chars.length; i++) {
      int showInt = chars[i] & 0xFFFF;

      if (showInt == 0x0d || showInt == 0x0a
          || (showInt >= 0x20 && showInt < 0x7F)) {
        out.print(chars[i]);
      } else {

        String showString = Integer.toHexString(showInt);

        String outString = (String) hexToName.get(showString);
        if (outString != null) {
          out.print("[" + outString + "]");
        } else {
          out.print("[" + showString + "]");
        }
      }
    } /* for */
  }

  public static void showCharNamesOutput(PrintWriter out, char[] chars) {
    if (hexToName == null)
      initializeHexToName();
    for (int i = 0; i < chars.length; i++) {
      int showInt = chars[i] & 0xFFFF;

      if (showInt == 0x0d || showInt == 0x0a
          || (showInt >= 0x20 && showInt < 0x7F)) {
        out.print(chars[i]);
      } else {

        String showString = Integer.toHexString(showInt);

        String outString = (String) hexToName.get(showString);
        if (outString != null) {
          out.print("[" + outString + "]");
        } else {
          out.print("[" + showString + "]");
        }

      }
    } /* for */
  }

  public static void appendCharNamesOutput(StringBuffer sb, char[] chars) {
    for (int i = 0; i < chars.length; i++) {
      if (hexToName == null)
        initializeHexToName();
      int showInt = chars[i] & 0xFFFF;

      if (showInt == 0x0d || showInt == 0x0a
          || (showInt >= 0x20 && showInt < 0x7F)) {
        sb.append(chars[i]);
      } else {

        String showString = Integer.toHexString(showInt);
        String outString = (String) hexToName.get(showString);
        if (outString != null) {
          sb.append("[" + outString + "]");
        } else {
          sb.append("[" + showString + "]");
        }

      }
    } /* for */
  }

  /* Get the parameter object from the parameter string */
  public static Object getParameterObject(String thisParm, PrintStream out) {

    if (thisParm.indexOf("UX'") == 0) {
      int len = thisParm.length();
      thisParm = thisParm.substring(3, len - 1);
      if (thisParm.indexOf("null") >= 0 || thisParm.indexOf("NULL") >= 0) {
        return null;
      } else {
        String stuffString = null;
        try {
          // HANDLE a unicode string
          len = len - 4;
          char[] stuff = new char[len / 4];
          for (int i = 0; i < stuff.length; i++) {
            String piece = thisParm.substring(i * 4, i * 4 + 4);
            stuff[i] = (char) Integer.parseInt(piece, 16);
          }
          stuffString = new String(stuff);
        } catch (Exception e) {
          out.println("Processing of " + thisParm + " failed");
          e.printStackTrace(out);
        }
        return stuffString;
      }
    } else if (thisParm.indexOf("X'") == 0) {
      int len = thisParm.length();
      thisParm = thisParm.substring(2, len - 1);
      if (thisParm.indexOf("null") >= 0) {
        return null;
      } else {
        byte[] stuff = null;
        try {
          // HANDLE a byte array
          len = len - 3;
          stuff = new byte[len / 2];
          for (int i = 0; i < stuff.length; i++) {
            String piece = thisParm.substring(i * 2, i * 2 + 2);
            stuff[i] = (byte) Integer.parseInt(piece, 16);
          }
        } catch (Exception e) {
          out.println("Processing of " + thisParm + " failed");
          e.printStackTrace(out);
        }
        return stuff;
      }

    } else if (thisParm.indexOf("FILEBLOB=") == 0) {
      java.sql.Blob blob = null;
      try {
        String filename = thisParm.substring(9).trim();
        // Read the file into a byte array and create a lob
        byte[] stuff = null;

        File file = new File(filename);
        int length = (int) file.length();
        stuff = new byte[length];
        FileInputStream inputStream = new FileInputStream(filename);
        inputStream.read(stuff);
        inputStream.close();

        blob = new JDTestBlob(stuff);

      } catch (Exception e) {
        out.println("Processing of " + thisParm + " failed because of " + e);
        e.printStackTrace(out);
      }
      return blob;
    } else if (thisParm.indexOf("FILECLOB=") == 0) {
      java.sql.Clob clob = null;
      try {
        String filename = thisParm.substring(9).trim();
        // Read the file into a byte array and create a lob
        char[] stuff = null;
        File file = new File(filename);
        int length = (int) file.length();
        stuff = new char[length];
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        reader.read(stuff, 0, length);

        clob = new JDTestClob(new String(stuff));
        reader.close();
      } catch (Exception e) {
        out.println("Processing of " + thisParm + " failed because of " + e);
        e.printStackTrace(out);
      }
      return clob;
    } else if (thisParm.indexOf("SAVEDPARM=") == 0) {
      System.out.println("ERROR:  SAVEDPARM not supported yet");
    } else if (thisParm.indexOf("SQLARRAY[") == 0) {
      System.out.println("ERROR:  SQLARRAY not supported yet");
      /* handleSqlarrayParm(cstmt, thisParm, parm, out); */
    } else if (thisParm.indexOf("GEN_BYTE_ARRAY+") == 0) {
      return getGenByteArrayParm(thisParm, out);
    } else if (thisParm.indexOf("GEN_HEX_STRING+") == 0) {
      System.out.println("ERROR:  GEN_HEX_STRING+ not supported yet");
      /* handleGenHexStringParm(cstmt, thisParm, parm, out); */
    } else if (thisParm.indexOf("GEN_CHAR_ARRAY+") == 0) {
      System.out.println("ERROR:  GEN_CHAR_ARRAY+ not supported yet");
    }
    /* Otherwise, just return the string */
    return thisParm;
  }

  public static void setParameter(PreparedStatement cstmt, String thisParm,
      int parm, PrintStream out) throws SQLException {

    if (thisParm.indexOf("UX'") == 0) {
      handleUnicodeStringParm(cstmt, thisParm, parm, out);
    } else if (thisParm.indexOf("X'") == 0) {
      handleByteArrayParm(cstmt, thisParm, parm, out);
    } else if (thisParm.indexOf("FILEBLOB=") == 0) {
      handleFileBlobParm(cstmt, thisParm, parm, out);
    } else if (thisParm.indexOf("FILECLOB=") == 0) {
      handleFileClobParm(cstmt, thisParm, parm, out);
    } else if (thisParm.indexOf("SAVEDPARM=") == 0) {
      handleSavedParm(cstmt, thisParm, parm, out);
    } else if (thisParm.indexOf("SQLARRAY[") == 0) {
      handleSqlarrayParm(cstmt, thisParm, parm, out);
    } else if (thisParm.indexOf("GEN_BYTE_ARRAY+") == 0) {
      handleGenByteArrayParm(cstmt, thisParm, parm, out);
    } else if (thisParm.indexOf("GEN_HEX_STRING+") == 0) {
      handleGenHexStringParm(cstmt, thisParm, parm, out);
    } else if (thisParm.indexOf("GEN_CHAR_ARRAY+") == 0) {
      String stuffString = null;

      int specifiedLength = -1; // the user specified length
      try {
        String specifiedLengthString = null;
        String charSetString = null;
        int charSet = -1; // ccsid indentification
        int indexC = -1; // to hold index of delimitor ('C')

        // get my delimited numbers
        String chopped = (thisParm.substring(15)).trim();
        // System.out.println("chopped = "+chopped);
        indexC = chopped.indexOf("C");
        // System.out.println("indexC = "+indexC);
        specifiedLengthString = chopped.substring(0, indexC);
        // System.out.println("specifiedLengthString = "+specifiedLengthString);
        charSetString = (chopped.substring(indexC + 1)).trim();
        // System.out.println("charSetString = "+charSetString);
        specifiedLength = Integer.parseInt(specifiedLengthString);
        // System.out.println("specifiedLength = "+specifiedLength);
        charSet = Integer.parseInt(charSetString);
        // System.out.println("charSet = "+charSet);

        char[] chars37 = { 'a', 'b', 'c', 'd' };
        char[] chars1208 = { '\u00c0', '\u35c0', '\ub5a0', '\u3055', '\u31ff',
            '\u3066' };
        char[] chars13488 = { '\u3055', '\u31ff', '\u3066' };
        char[] chars1200 = { '\u00c0', '\u35c0', '\ub5a0', '\u3055', '\u31ff',
            '\u3066' };
        char[] chars835 = { '\u5e03', '\u5f17', '\u672b', '\u5378', '\u59cb',
            '\u8679', '\u500c', '\u89f4', '\u9853', '\u8271', '\u8f44',
            '\u977e', '\u52f1' };
        char[] chars424 = { '\u05e1', '\u05d5', '\u05db', '\u05df', '\u0020',
            '\u05dc', '\u05dc', '\u05d0', '\u0020', '\u05e9', '\u05d9',
            '\u05d5', '\u05da', '\u0020', '\u05d7', '\u05ea', '\u05dd',
            '\u002d', '\u05d0', '\u05d7', '\u05e8', '\u05d9', '\u05dd' };
        char[] baseChars = new char[specifiedLength];
        char[] stuff = new char[specifiedLength];

        switch (charSet) {
        case 37:
          baseChars = chars37;
          break;
        case 424:
          baseChars = chars424;
          break;
        case 835:
          baseChars = chars835;
          break;
        case 1200:
          baseChars = chars1200;
          break;
        case 1208:
          baseChars = chars1208;
          break;
        case 13488:
          baseChars = chars13488;
          break;
        default:
          baseChars = chars37;
          break;
        }

        for (int i = 0; i < specifiedLength; i++) {

          // System.out.println(i);
          stuff[i] = (char) baseChars[i % baseChars.length];

        }
        // System.out.println("after the switch for loop");
        stuffString = new String(stuff);
      } catch (Exception e) {
        out.println("Processing of " + thisParm + " failed");
        e.printStackTrace(out);
        stuffString = "";
      }
      cstmt.setString(parm, stuffString);
      out.println("CHARARRAY[size=" + specifiedLength + ",CRC32="
          + getCRC32(stuffString) + ",CRC32xor1=" + getCRC32xor1(stuffString)
          + "]");
      SQLWarning warning = cstmt.getWarnings();
      cstmt.clearWarnings();
      if (warning != null) {
        if (!silent) {
          dispWarning(out, warning);
        }
      }

    } else {

      // String surrounding quotes
      if (thisParm.indexOf("'") == 0) {
        int lastQuote = thisParm.indexOf("'", 1);
        if (lastQuote > 0) {
          thisParm = thisParm.substring(1, lastQuote);
        } else {
          thisParm = thisParm.substring(1);
        }
      }

      cstmt.setString(parm, thisParm);
      SQLWarning warning = cstmt.getWarnings();
      cstmt.clearWarnings();
      if (warning != null) {
        if (!silent) {
          dispWarning(out, warning);
        }
      }

    }
  }

  static String getCRC32(String input) {
    int length = input.length();
    if (length > 16000000) {
      System.gc();
    }
    byte[] byteArray = new byte[2 * length];
    for (int i = 0; i < length; i++) {
      int c = (int) input.charAt(i);
      byteArray[2 * i] = (byte) ((c & 0xFF00) >> 16);
      byteArray[2 * i + 1] = (byte) (c & 0xFF);
    }
    CRC32 checksum = new CRC32();
    checksum.update(byteArray);
    byteArray = null;
    return "" + checksum.getValue();
  }

  static String getCRC32xor1(String input) {
    int length = input.length();
    if (length > 16000000) {
      System.gc();
    }

    byte[] byteArray = new byte[2 * length];
    for (int i = 0; i < length; i++) {
      int c = (int) input.charAt(i);
      byteArray[2 * i] = (byte) ((c & 0xFF00) >> 16);
      if (byteArray[2 * i] == 0xD8) {
        // Don't xor unicod D8XX
        byteArray[2 * i + 1] = (byte) ((c) & 0xFF);
      } else {
        byteArray[2 * i + 1] = (byte) ((c ^ 1) & 0xFF);
      }
    }
    CRC32 checksum = new CRC32();
    checksum.update(byteArray);
    return "" + checksum.getValue();
  }

  static void handleUnicodeStringParm(PreparedStatement cstmt1, String thisParm,
      int parm, PrintStream out) throws SQLException {

    String stuffString = (String) getParameterObject(thisParm, out);
    cstmt1.setString(parm, stuffString);
    SQLWarning warning = cstmt1.getWarnings();
    cstmt1.clearWarnings();
    if (warning != null) {
      if (!silent) {
        dispWarning(out, warning);
      }
    }

  }

  static void handleByteArrayParm(PreparedStatement cstmt1, String thisParm,
      int parm, PrintStream out) throws SQLException {
    byte[] stuff = (byte[]) getParameterObject(thisParm, out);
    cstmt1.setBytes(parm, stuff);
    SQLWarning warning = cstmt1.getWarnings();
    cstmt1.clearWarnings();
    if (warning != null) {
      if (!silent) {
        dispWarning(out, warning);
      }
    }
  }

  static byte[] getGenByteArrayParm(String thisParm, PrintStream out) {
    byte[] stuff = null;
    try {
      String lengthString = thisParm.substring(15);
      int length = Integer.parseInt(lengthString);
      stuff = new byte[length];
      for (int i = 0; i < length; i++) {
        stuff[i] = (byte) (i & 0xFF);
      }
    } catch (Exception e) {
      out.println("Processing of " + thisParm + " failed");
      e.printStackTrace(out);
    }
    return stuff;
  }

  static void handleGenByteArrayParm(PreparedStatement cstmt1, String thisParm,
      int parm, PrintStream out) throws SQLException {
    byte[] stuff = getGenByteArrayParm(thisParm, out);
    cstmt1.setBytes(parm, stuff);

    CRC32 checksum = new CRC32();
    checksum.update(stuff);
    out.println("GEN_BYTE_ARRAY generated array of size = " + stuff.length
        + " with checksum of " + checksum.getValue());
  }

  static void handleSavedParm(PreparedStatement cstmt1, String thisParm,
      int parm, PrintStream out) throws SQLException {

    int number = 0;
    try {
      String parmNumber = thisParm.substring(10);
      number = Integer.parseInt(parmNumber);
    } catch (Exception e) {
      out.println("Processing of " + thisParm + " failed");
      e.printStackTrace(out);
    }
    cstmt1.setString(parm, savedStringParm[number]);
    out.println("SAVEDPARM set(" + parm + "," + savedStringParm[number]
        + " from saved " + number);
    SQLWarning warning = cstmt1.getWarnings();
    cstmt1.clearWarnings();
    if (warning != null) {
      if (!silent) {
        dispWarning(out, warning);
      }
    }

  }

  static void appendDigit(StringBuffer buffer, int digit) {
    switch (digit) {
    case 0:
      buffer.append('0');
      break;
    case 1:
      buffer.append('1');
      break;
    case 2:
      buffer.append('2');
      break;
    case 3:
      buffer.append('3');
      break;
    case 4:
      buffer.append('4');
      break;
    case 5:
      buffer.append('5');
      break;
    case 6:
      buffer.append('6');
      break;
    case 7:
      buffer.append('7');
      break;
    case 8:
      buffer.append('8');
      break;
    case 9:
      buffer.append('9');
      break;
    case 10:
      buffer.append('a');
      break;
    case 11:
      buffer.append('b');
      break;
    case 12:
      buffer.append('c');
      break;
    case 13:
      buffer.append('d');
      break;
    case 14:
      buffer.append('e');
      break;
    case 15:
      buffer.append('f');
      break;
    }
  }

  static void handleGenHexStringParm(PreparedStatement cstmt1, String thisParm,
      int parm, PrintStream out) throws SQLException {
    StringBuffer stuff = null;
    byte[] bytes = null;
    try {
      String lengthString = thisParm.substring(15);
      int length = Integer.parseInt(lengthString);
      stuff = new StringBuffer(2 * length);
      bytes = new byte[length];
      for (int i = 0; i < length; i++) {
        bytes[i] = (byte) (i & 0xFF);
        appendDigit(stuff, (i & 0xF0) >> 4);
        appendDigit(stuff, i & 0x0F);
      }

    } catch (Exception e) {
      out.println("Processing of " + thisParm + " failed");
      stuff = new StringBuffer("Processing of " + thisParm + " failed");
      bytes = new byte[0];
      e.printStackTrace(out);
    }

    cstmt1.setString(parm, stuff.toString());

    CRC32 checksum = new CRC32();
    checksum.update(bytes);
    out.println("GEN_BYTE_ARRAY generated array of size = " + bytes.length
        + " with checksum of " + checksum.getValue());

    SQLWarning warning = cstmt1.getWarnings();
    cstmt1.clearWarnings();
    if (warning != null) {
      if (!silent) {
        dispWarning(out, warning);
      }
    }

  }

  static void handleFileBlobParm(PreparedStatement cstmt1, String thisParm,
      int parm, PrintStream out) throws SQLException {
    java.sql.Blob blob = (java.sql.Blob) getParameterObject(thisParm, out);
    cstmt1.setBlob(parm, blob);

  }

  static void handleFileClobParm(PreparedStatement cstmt1, String thisParm,
      int parm, PrintStream out) throws SQLException {
    java.sql.Clob clob = (java.sql.Clob) getParameterObject(thisParm, out);
    cstmt1.setClob(parm, clob);
  }

  public static Array makeArray(Connection connection, Object parameter,
      String arrayType) throws Exception {
    Object[] objectArray = new Object[0];
    Class argTypes[] = new Class[2];
    argTypes[0] = "".getClass();
    argTypes[1] = objectArray.getClass();
    Array arrayParameter = (Array) JDReflectionUtil.callMethod_O(connection,
        "createArrayOf", argTypes, arrayType, parameter);
    return arrayParameter;
  }

  /*
   * Creates a native or sqlArray from TYPE:element:element... ]
   */

  static Object getNativeOrSqlArray(Connection connection, String left,
      boolean returnSqlArray) throws Exception {
    Object returnArray;
    int colonIndex = left.indexOf(":");
    boolean emptyArray = false;
    if (colonIndex == -1) {
      colonIndex = left.indexOf("]");
      if (colonIndex > 0) {
        emptyArray = true;
      }
    }

    if (colonIndex > 0) {
      String typename = left.substring(0, colonIndex);
      if (emptyArray) {
        left = left.substring(colonIndex);
      } else {
        left = left.substring(colonIndex + 1);
      }
      // Put the string parameters into a vector
      Vector parameterVector = new Vector();
      String arraySep = ":";
      if (typename.equals("Time")) {
        arraySep = " ";
      }
      if (typename.equals("Timestamp")) {
        arraySep = "|";
      }

      colonIndex = left.indexOf(arraySep);
      while (colonIndex >= 0) {
        String piece = left.substring(0, colonIndex);
        parameterVector.addElement(piece);
        left = left.substring(colonIndex + 1);
        colonIndex = left.indexOf(arraySep);
      }

      int braceIndex = left.indexOf("]");
      if (braceIndex >= 0) {
        if (!emptyArray) {
          parameterVector.addElement(left.substring(0, braceIndex));
        }
        int arrayCardinality = parameterVector.size();

        String validTypes = "String:BigDecimal:Date:Time:Timestamp:Blob:Clob:int:boolean:short:long:float:double:byteArray";
        if (typename.equals("String")) {
          String[] parameter = new String[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {
              if (s.indexOf("UX'") == 0) {
                int len = s.length();
                len = len - 4;
                char[] stuff = new char[len / 4];
                for (int j = 0; j < stuff.length; j++) {
                  String piece = s.substring(3 + j * 4, 3 + j * 4 + 4);
                  stuff[j] = (char) Integer.parseInt(piece, 16);
                }
                parameter[i] = new String(stuff);
              } else {
                parameter[i] = s;
              }
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "VARCHAR");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("Clob")) {
          Clob[] parameter = new Clob[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {
              parameter[i] = new JDTestClob(s);
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "CLOB");
          } else {
            returnArray = parameter;
          }

        } else if (typename.equals("BigDecimal")) {
          BigDecimal[] parameter = new BigDecimal[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {
              parameter[i] = new BigDecimal(s);
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "DECIMAL");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("Date")) {
          Date[] parameter = new Date[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {
              parameter[i] = Date.valueOf(s);
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "DATE");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("Time")) {
          Time[] parameter = new Time[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {
              parameter[i] = Time.valueOf(s);
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "TIME");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("Timestamp")) {
          Timestamp[] parameter = new Timestamp[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {
              parameter[i] = Timestamp.valueOf(s);
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "TIMESTAMP");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("Integer")) {
          Integer[] parameter = new Integer[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {
              parameter[i] = new Integer(Integer.parseInt(s));
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "INTEGER");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("int")) {
          int[] parameter = new int[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = 0;
            } else {
              parameter[i] = Integer.parseInt(s);
            }
          }
          if (returnSqlArray) {
            // Toolbox does not handle native types on convert
            Integer[] newParameter = new Integer[arrayCardinality];
            for (int i = 0; i < arrayCardinality; i++) {
              newParameter[i] = new Integer(parameter[i]);
            }
            returnArray = makeArray(connection, newParameter, "INTEGER");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("Short")) {
          Short[] parameter = new Short[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {
              parameter[i] = new Short((short) Integer.parseInt(s));
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "SMALLINT");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("short")) {
          short[] parameter = new short[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = 0;
            } else {
              parameter[i] = (short) Integer.parseInt(s);
            }
          }
          if (returnSqlArray) {
            // Toolbox does not handle native types on convert
            Short[] newParameter = new Short[arrayCardinality];
            for (int i = 0; i < arrayCardinality; i++) {
              newParameter[i] = new Short(parameter[i]);
            }

            returnArray = makeArray(connection, newParameter, "SMALLINT");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("Boolean")) {
          Boolean[] parameter = new Boolean[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {
              parameter[i] = new Boolean(Boolean.parseBoolean(s));
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "BOOLEAN");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("boolean")) {
          boolean[] parameter = new boolean[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = false;
            } else {
              parameter[i] = Boolean.parseBoolean(s);
            }
          }
          if (returnSqlArray) {
            // Toolbox does not handle native types on convert
            Boolean[] newParameter = new Boolean[arrayCardinality];
            for (int i = 0; i < arrayCardinality; i++) {
              newParameter[i] = new Boolean(parameter[i]);
            }

            returnArray = makeArray(connection, newParameter, "BOOLEAN");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("Long")) {
          Long[] parameter = new Long[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {
              parameter[i] = new Long(Long.parseLong(s));
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "BIGINT");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("long")) {
          long[] parameter = new long[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = 0;
            } else {
              parameter[i] = Long.parseLong(s);
            }
          }
          if (returnSqlArray) {
            // Toolbox does not handle native types on convert
            Long[] newParameter = new Long[arrayCardinality];
            for (int i = 0; i < arrayCardinality; i++) {
              newParameter[i] = new Long(parameter[i]);
            }

            returnArray = makeArray(connection, newParameter, "BIGINT");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("Float")) {
          Float[] parameter = new Float[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {
              parameter[i] = new Float((float) Double.parseDouble(s));
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "REAL");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("float")) {
          float[] parameter = new float[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = 0;
            } else {
              parameter[i] = (float) Double.parseDouble(s);
            }
          }
          if (returnSqlArray) {
            // Toolbox does not handle native types on convert
            Float[] newParameter = new Float[arrayCardinality];
            for (int i = 0; i < arrayCardinality; i++) {
              newParameter[i] = new Float(parameter[i]);
            }

            returnArray = makeArray(connection, newParameter, "REAL");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("Double")) {
          Double[] parameter = new Double[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {
              parameter[i] = new Double(Double.parseDouble(s));
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "DOUBLE");
          } else {
            returnArray = parameter;
          }

        } else if (typename.equals("double")) {
          double[] parameter = new double[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = 0;
            } else {
              parameter[i] = Double.parseDouble(s);
            }
          }
          if (returnSqlArray) {
            // Toolbox does not handle native types on convert
            Double[] newParameter = new Double[arrayCardinality];
            for (int i = 0; i < arrayCardinality; i++) {
              newParameter[i] = new Double(parameter[i]);
            }

            returnArray = makeArray(connection, newParameter, "DOUBLE");
          } else {
            returnArray = parameter;
          }
        } else if (typename.equals("byteArray")) {
          byte[][] parameter = new byte[arrayCardinality][];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {

              byte stuff[] = new byte[s.length() / 2];
              for (int j = 0; j < stuff.length; j++) {
                String piece = s.substring(j * 2, j * 2 + 2);
                stuff[j] = (byte) Integer.parseInt(piece, 16);
              }

              parameter[i] = stuff;
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "BINARY");
          } else {
            returnArray = parameter;
          }

        } else if (typename.equals("Blob")) {
          Blob[] parameter = new Blob[arrayCardinality];
          for (int i = 0; i < arrayCardinality; i++) {
            String s = (String) parameterVector.get(i);
            ;
            if ("null".equals(s)) {
              parameter[i] = null;
            } else {

              byte stuff[] = new byte[s.length() / 2];
              for (int j = 0; j < stuff.length; j++) {
                String piece = s.substring(j * 2, j * 2 + 2);
                stuff[j] = (byte) Integer.parseInt(piece, 16);
              }

              parameter[i] = new JDTestBlob(stuff);
            }
          }
          if (returnSqlArray) {
            returnArray = makeArray(connection, parameter, "BLOB");
          } else {
            returnArray = parameter;
          }

        } else if (typename.equalsIgnoreCase("null")) {
          // returnArray is already set to null
          returnArray = null;
        } else {
          throw new Exception(
              "Type [" + typename + "] not valid: valid types=" + validTypes);
        }

      } else {
        throw new Exception(
            "Unable to find ending brace for SQLARRAY[TYPE:e1:e2:...]");
      }
    } else {
      throw new Exception("TYPE not found for SQLARRAY[TYPE:e1:e2:...]");
    }
    return returnArray;
  }

  static void handleSqlarrayParm(PreparedStatement cstmt1, String thisParm,
      int parm, PrintStream out) throws SQLException {
    try {
      // Format SQLARRAY[TYPE:e1:e2:...]
      // Strip off the SQLARRAY[
      String left = thisParm.substring(9).trim();

      if (toolboxDriver || jccDriver) {
        cstmt1.setArray(parm, (Array) getNativeOrSqlArray(con, left, true));
      } else {
        cstmt1.setObject(parm, getNativeOrSqlArray(con, left, false));
      }

    } catch (Exception e) {
      out.println("Processing of ARRAYPARAMETER '" + thisParm
          + "' failed because of " + e);
      if (e instanceof SQLException) {
        throw (SQLException) e;
      } else {
        e.printStackTrace(out);
      }
    }

  }

  public static char asciiChar(int value) {
    if (value < 0x20 || value > 0x7e) {
      return '.';
    } else {
      return (char) value;
    }
  }

  public static char[] etoa = { 0x00, 0x01, 0x02, 0x03, 0x1A, 0x09, 0x1A, 0x7F,
      0x1A, 0x1A, 0x1A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13,
      0x1A, 0x1A, 0x08, 0x1A, 0x18, 0x19, 0x1A, 0x1A, 0x1C, 0x1D, 0x1E, 0x1F,
      0x1A, 0x1A, 0x1C, 0x1A, 0x1A, 0x0A, 0x17, 0x1B, 0x1A, 0x1A, 0x1A, 0x1A,
      0x1A, 0x05, 0x06, 0x07, 0x1A, 0x1A, 0x16, 0x1A, 0x1A, 0x1E, 0x1A, 0x04,
      0x1A, 0x1A, 0x1A, 0x1A, 0x14, 0x15, 0x1A, 0x1A, 0x20, 0xA6, 0xE1, 0x80,
      0xEB, 0x90, 0x9F, 0xE2, 0xAB, 0x8B, 0x9B, 0x2E, 0x3C, 0x28, 0x2B, 0x7C,
      0x26, 0xA9, 0xAA, 0x9C, 0xDB, 0xA5, 0x99, 0xE3, 0xA8, 0x9E, 0x21, 0x24,
      0x2A, 0x29, 0x3B, 0x5E, 0x2D, 0x2F, 0xDF, 0xDC, 0x9A, 0xDD, 0xDE, 0x98,
      0x9D, 0xAC, 0xBA, 0x2C, 0x25, 0x5F, 0x3E, 0x3F, 0xD7, 0x88, 0x94, 0xB0,
      0xB1, 0xB2, 0xFC, 0xD6, 0xFB, 0x60, 0x3A, 0x23, 0x40, 0x27, 0x3D, 0x22,
      0xF8, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x96, 0xA4,
      0xF3, 0xAF, 0xAE, 0xC5, 0x8C, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F, 0x70,
      0x71, 0x72, 0x97, 0x87, 0xCE, 0x93, 0xF1, 0xFE, 0xC8, 0x7E, 0x73, 0x74,
      0x75, 0x76, 0x77, 0x78, 0x79, 0x7A, 0xEF, 0xC0, 0xDA, 0x5B, 0xF2, 0xF9,
      0xB5, 0xB6, 0xFD, 0xB7, 0xB8, 0xB9, 0xE6, 0xBB, 0xBC, 0xBD, 0x8D, 0xD9,
      0xBF, 0x5D, 0xD8, 0xC4, 0x7B, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47,
      0x48, 0x49, 0xCB, 0xCA, 0xBE, 0xE8, 0xEC, 0xED, 0x7D, 0x4A, 0x4B, 0x4C,
      0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52, 0xA1, 0xAD, 0xF5, 0xF4, 0xA3, 0x8F,
      /* S T U V W X Y Z */
      0x5C, 0xE7, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5A, 0xA0, 0x85,
      0x8E, 0xE9, 0xE4, 0xD1,
      /* 0 1 2 3 4 5 6 7 8 9 */
      0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0xB3, 0xF7,
      0xF0, 0xFA, 0xA7, 0xFF,

  };

  public static char ebcdicChar(int value) {
    if (value < 0x40 || value > 0xff) {
      return '.';
    } else {
      return (char) etoa[value];
    }
  }

  public static String dumpBytes(byte[] bytes) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < bytes.length; i++) {
      int unsignedInt = 0xFF & bytes[i];
      if (unsignedInt < 0x10) {
        sb.append("0");
      }
      sb.append(Integer.toHexString(unsignedInt));
    }
    return sb.toString();
  }

  //
  // Threaded run information
  //
  String command;
  PrintStream out;

  public JDSQL400(String command, PrintStream out) {
    this.command = command;
    this.out = out;
  }

  public void run() {
    Thread thisThread = Thread.currentThread();
    out.println("Thread " + thisThread + " running " + command);
    String upcaseCommand = command.toUpperCase();
    if (upcaseCommand.startsWith("PERSIST")) {
      command = null;
      while (command == null) {
        synchronized (this) {
          while (command == null) {
            try {
              wait(1000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
        upcaseCommand = command.toUpperCase().trim();
        if (upcaseCommand.equals("QUIT") || upcaseCommand.equals("EXIT")) {
          // leave command set so we exit loop
        } else {
          out.println("Thread " + thisThread + " running " + command);
          executeCommand(command, out);
          // Null command so we keep running
          command = null;

        }
      } /* while */

    } else {
      executeCommand(command, out);
    }
    out.println("Thread " + thisThread + " ending");
  }

  synchronized public void setCommand(String command) {
    this.command = command;
    notify();
  }

}
