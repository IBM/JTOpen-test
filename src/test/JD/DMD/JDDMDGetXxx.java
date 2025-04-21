///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetXxx.java
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
// File Name:    JDDMDGetXxx.java
//
// Classes:      JDDMDGetXxx
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.DMD;

import com.ibm.as400.access.*;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JVMInfo;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;

import java.sql.ResultSet; //@D1A
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * Testcase JDDMDGetXxx. This tests the following methods of the JDBC
 * DatabaseMetaData class:
 * 
 * <ul>
 * <li>getCatalogSeparator()
 * <li>getCatalogTerm()
 * <li>getConnection()
 * <li>getDatabaseProductName()
 * <li>getDatabaseProductVersion()
 * <li>getDefaultTransactionIsolation()
 * <li>getDriverMajorVersion()
 * <li>getDriverMinorVersion()
 * <li>getDriverName()
 * <li>getDriverVersion()
 * <li>getExtraNameCharacters()
 * <li>getIdentifierQuoteString()
 * <li>getMaxBinaryLiteralLength()
 * <li>getMaxCatalogNameLength()
 * <li>getMaxCharLiteralLength()
 * <li>getMaxColumnNameLength()
 * <li>getMaxColumnsInGroupBy()
 * <li>getMaxColumnsInIndex()
 * <li>getMaxColumnsInOrderBy()
 * <li>getMaxColumnsInSelect()
 * <li>getMaxColumnsInTable()
 * <li>getMaxConnections()
 * <li>getMaxCursorNameLength()
 * <li>getMaxIndexLength()
 * <li>getMaxProcedureNameLength()
 * <li>getMaxRowSize()
 * <li>getMaxSchemaNameLength()
 * <li>getMaxStatementLength()
 * <li>getMaxStatements()
 * <li>getMaxTableNameLength()
 * <li>getMaxTablesInSelect()
 * <li>getMaxUserNameLength()
 * <li>getNumericFunctions()
 * <li>getProcedureTerm()
 * <li>getSchemaTerm()
 * <li>getSearchStringEscape()
 * <li>getSQLKeywords()
 * <li>getStringFunctions()
 * <li>getSystemFunctions()
 * <li>getURL()
 * <li>getUserName()
 * <li>getResultSetHoldability() //@D1A
 * </ul>
 **/
public class JDDMDGetXxx extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDGetXxx";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }

  // Private data.
  private Connection connection_;
  private Connection closedConnection_;
  private Connection sqlConnection_;
  private DatabaseMetaData dmd_;
  private DatabaseMetaData dmd2_;
  private DatabaseMetaData dmd3_;
  private Driver driver_;
  private String url_;

  private static boolean jdk16 = false;
  private static boolean jdk18 = false;
  private static boolean jdk19 = false;

  private static final String[] numericFunctions610_ = { "abs", "acos", "asin",
      "atan", "atan2", "ceiling", "character_length", "cos", "cot", "degrees",
      "exp", "floor", "log", "log10", "mod", "octet_length", "pi", "power",
      "position", "radians", "rand", "round", "sign", "sin", "sqrt", "tan",
      "truncate" };

  private static final String[] numericFunctionsForNative530_ = { "abs", "acos",
      "asin", "atan", "atan2", "ceiling", "cos", "cot", "degrees", "exp",
      "floor", "log", "log10", "mod", "pi", "power", "radians", "rand", "round",
      "sign", "sin", "sqrt", "tan", "truncate" };

  private static final String[] sqlKeywords_ = { "AFTER", "ALIAS", "ALLOW",
      "APPLICATION", "ASSOCIATE", "ASUTIME", "AUDIT", "AUX", "AUXILIARY",
      "BEFORE", "BINARY", "BUFFERPOOL", "CACHE", "CALL", "CALLED", "CAPTURE",
      "CARDINALITY", "CCSID", "CLUSTER", "COLLECTION", "COLLID", "COMMENT",
      "CONCAT", "CONDITION", "CONTAINS", "COUNT_BIG", "CURRENT_LC_CTYPE",
      "CURRENT_PATH", "CURRENT_SERVER", "CURRENT_TIMEZONE", "CYCLE", "DATA",
      "DATABASE", "DAYS", "DB2GENERAL", "DB2GENRL", "DB2SQL", "DBINFO",
      "DEFAULTS", "DEFINITION", "DETERMINISTIC", "DISALLOW", "DO", "DSNHATTR",
      "DSSIZE", "DYNAMIC", "EACH", "EDITPROC", "ELSEIF", "ENCODING",
      "END-EXEC1", "ERASE", "EXCLUDING", "EXIT", "FENCED", "FIELDPROC", "FILE",
      "FINAL", "FREE", "FUNCTION", "GENERAL", "GENERATED", "GRAPHIC", "HANDLER",
      "HOLD", "HOURS", "IF", "INCLUDING", "INCREMENT", "INDEX", "INHERIT",
      "INOUT", "INTEGRITY", "ISOBID", "ITERATE", "JAR", "JAVA", "LABEL",
      "LC_CTYPE", "LEAVE", "LINKTYPE", "LOCALE", "LOCATOR", "LOCATORS", "LOCK",
      "LOCKMAX", "LOCKSIZE", "LONG", "LOOP", "MAXVALUE", "MICROSECOND",
      "MICROSECONDS", "MINUTES", "MINVALUE", "MODE", "MODIFIES", "MONTHS",
      "NEW", "NEW_TABLE", "NOCACHE", "NOCYCLE", "NODENAME", "NODENUMBER",
      "NOMAXVALUE", "NOMINVALUE", "NOORDER", "NULLS", "NUMPARTS", "OBID", "OLD",
      "OLD_TABLE", "OPTIMIZATION", "OPTIMIZE", "OUT", "OVERRIDING", "PACKAGE",
      "PARAMETER", "PART", "PARTITION", "PATH", "PIECESIZE", "PLAN", "PRIQTY",
      "PROGRAM", "PSID", "QUERYNO", "READS", "RECOVERY", "REFERENCING",
      "RELEASE", "RENAME", "REPEAT", "RESET", "RESIGNAL", "RESTART", "RESULT",
      "RESULT_SET_LOCATOR", "RETURN", "RETURNS", "ROUTINE", "ROW", "RRN", "RUN",
      "SAVEPOINT", "SCRATCHPAD", "SECONDS", "SECQTY", "SECURITY", "SENSITIVE",
      "SIGNAL", "SIMPLE", "SOURCE", "SPECIFIC", "SQLID", "STANDARD", "START",
      "STATIC", "STAY", "STOGROUP", "STORES", "STYLE", "SUBPAGES", "SYNONYM",
      "SYSFUN", "SYSIBM", "SYSPROC", "SYSTEM", "TABLESPACE", "TRIGGER", "TYPE",
      "UNDO", "UNTIL", "VALIDPROC", "VARIABLE", "VARIANT", "VCAT", "VOLUMES",
      "WHILE", "WLM", "YEARS" };

  private static final String[] sqlKeywords720_JDBC40_ = { "ACCORDING",
      "ACCTNG", "ACTION", "ACTIVATE", "ALIAS", "ALLOW", "APPEND", "APPLNAME",
      "ARRAY_AGG", "ASC", "ASSOCIATE", "ATTRIBUTES", "AUTONOMOUS", "BEFORE",
      "BIND", "BIT", "BUFFERPOOL", "CACHE", "CARDINALITY", "CCSID", "CL",
      "CLUSTER", "COLLECT", "COLLECTION", "COMMENT", "COMPACT", "COMPRESS",
      "CONCAT", "CONCURRENT", "CONNECT_BY_ROOT", "CONNECTION", "CONSTANT",
      "CONTAINS", "CONTENT", "COPY", "COUNT", "COUNT_BIG", "CURRENT_SCHEMA",
      "CURRENT_SERVER", "CURRENT_TIMEZONE", "DATA", "DATABASE",
      "DATAPARTITIONNAME", "DATAPARTITIONNUM", "DAYS", "DBINFO",
      "DBPARTITIONNAME", "DBPARTITIONNUM", "DB2GENERAL", "DB2GENRL", "DB2SQL",
      "DEACTIVATE", "DEFAULTS", "DEFER", "DEFINE", "DEFINITION", "DELETING",
      "DENSERANK", "DENSE_RANK", "DESC", "DESCRIPTOR", "DIAGNOSTICS", "DISABLE",
      "DISALLOW", "DOCUMENT", "ENABLE", "ENCRYPTION", "ENDING", "ENFORCED",
      "EVERY", "EXCEPTION", "EXCLUDING", "EXCLUSIVE", "EXTEND", "EXTRACT",
      "FENCED", "FIELDPROC", "FILE", "FINAL", "FREEPAGE", "GBPCACHE", "GENERAL",
      "GENERATED", "GO", "GOTO", "GRAPHIC", "HASH", "HASHED_VALUE", "HINT",
      "HOURS", "ID", "IGNORE", "IMPLICITLY", "INCLUDE", "INCLUDING",
      "INCLUSIVE", "INCREMENT", "INDEX", "INDEXBP", "INF", "INFINITY",
      "INHERIT", "INSERTING", "INTEGRITY", "ISOLATION", "JAVA", "KEEP", "KEY",
      "LABEL", "LEVEL2", "LINKTYPE", "LOCALDATE", "LOCATION", "LOCATOR", "LOCK",
      "LOCKSIZE", "LOG", "LOGGED", "LONG", "MAINTAINED", "MASK", "MATCHED",
      "MATERIALIZED", "MAXVALUE", "MICROSECOND", "MICROSECONDS", "MINPCTUSED",
      "MINUTES", "MINVALUE", "MIXED", "MODE", "MONTHS", "NAMESPACE", "NAN",
      "NEW_TABLE", "NEXTVAL", "NOCACHE", "NOCYCLE", "NODENAME", "NODENUMBER",
      "NOMAXVALUE", "NOMINVALUE", "NOORDER", "NORMALIZED", "NULLS", "NVARCHAR",
      "OBID", "OLD_TABLE", "OPTIMIZE", "OPTION", "ORDINALITY", "ORGANIZE",
      "OVERRIDING", "PACKAGE", "PADDED", "PAGE", "PAGESIZE", "PART",
      "PARTITIONED", "PARTITIONING", "PARTITIONS", "PASSING", "PASSWORD",
      "PATH", "PCTFREE", "PERMISSION", "PIECESIZE", "PLAN", "POSITION",
      "PREVVAL", "PRIOR", "PRIQTY", "PRIVILEGES", "PROGRAM", "PROGRAMID",
      "QUERY", "RANK", "RCDFMT", "READ", "RECOVERY", "REFRESH", "RENAME",
      "RESET", "RESTART", "RESULT_SET_LOCATOR", "RID", "ROUTINE", "ROWNUMBER",
      "ROW_NUMBER", "RRN", "RUN", "SBCS", "SCHEMA", "SCRATCHPAD", "SECONDS",
      "SECQTY", "SECURED", "SEQUENCE", "SESSION", "SIMPLE", "SKIP", "SNAN",
      "SOURCE", "SQLID", "STACKED", "STARTING", "STATEMENT", "STOGROUP",
      "SUBSTRING", "SUMMARY", "SYNONYM", "TABLESPACE", "TABLESPACES",
      "THREADSAFE", "TRANSACTION", "TRANSFER", "TRIM", "TRIM_ARRAY", "TRUNCATE",
      "TYPE", "UNIT", "UPDATING", "URI", "USAGE", "USE", "USERID", "VARIABLE",
      "VARIANT", "VCAT", "VERSION", "VIEW", "VOLATILE", "WAIT", "WRAPPED",
      "WRITE", "WRKSTNNAME", "XMLAGG", "XMLATTRIBUTES", "XMLCAST", "XMLCOMMENT",
      "XMLCONCAT", "XMLDOCUMENT", "XMLELEMENT", "XMLFOREST", "XMLGROUP",
      "XMLNAMESPACES", "XMLPARSE", "XMLPI", "XMLROW", "XMLSERIALIZE",
      "XMLTABLE", "XMLTEXT", "XMLVALIDATE", "XSLTRANSFORM", "XSROBJECT",
      "YEARS", "YES" };

  private static final String[] stringFunctionsForNative530_ = { "concat",
      "difference", "insert", "lcase", "left", "length", "locate", "ltrim",
      "repeat", "replace", "right", // @kkashir : added repeat and replace
      "rtrim", "soundex", "space", "substring", "ucase" };

  // @pda add char
  private static final String[] stringFunctions530_ = { "char", "concat",
      "difference", "insert", "lcase", "left", "length", "locate", "ltrim",
      "repeat", "replace", "right", "rtrim", "soundex", "space", "substring",
      "ucase" };

  private static final String[] stringFunctions610_ = { "ascii", "char",
      "char_length", "character_length", "concat", "difference", "insert",
      "lcase", "left", "length", "locate", "ltrim", "octet_length", "position",
      "repeat", "replace", "right", "rtrim", "soundex", "space", "substring",
      "ucase" };

  private static final String[] systemFunctions_ = { "database", "ifnull",
      "user" };

  private static final String[] timeDateFunctions610_ = { "current_date",
      "current_time", "current_timestamp", "curdate", "curtime", "dayname",
      "dayofmonth", "dayofweek", "dayofyear", "extract", "hour", "minute",
      "month", "monthname", "now", "quarter", "second", "timestampdiff", "week",
      "year" };

  private static final String[] timeDateFunctionsForNative530_ = { "curdate",
      "curtime", "dayname", "dayofmonth", "dayofweek", "dayofyear", "hour",
      "minute", "month", "monthname", "now", "quarter", "second",
      "timestampdiff", "week", "year" };

  /**
   * Constructor.
   **/
  public JDDMDGetXxx(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDDMDGetXxx", namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {

    //
    // look for jdk1.6
    //
    int jdk = JVMInfo.getJDK();
    if (jdk < JVMInfo.JDK_16) {
      jdk16 = false;
      jdk18 = false;
    } else {
      jdk16 = true;
      if (jdk < JVMInfo.JDK_18) {
        jdk18 = false;
      } else {
        jdk18 = true;
        if (jdk < JVMInfo.JDK_19) {
          jdk19 = false;
        } else {
          jdk19 = true;

        }

      }

    }

    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
      url_ = "jdbc:as400://" + systemObject_.getSystemName() ;
    else if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE)
      url_ = "jdbc:jtopenlite://" + systemObject_.getSystemName() ;

    else
      url_ = "jdbc:db2://" + systemObject_.getSystemName() ;

    driver_ = DriverManager.getDriver(url_);

    if (getDriver() == JDTestDriver.DRIVER_JCC) {

      url_ = "jdbc:db2://" + systemObject_.getSystemName() + ":"
          + JDTestDriver.jccPort + "/" + JDTestDriver.jccDatabase;
      connection_ = testDriver_.getConnection(url_, systemObject_.getUserId(),
          encryptedPassword_);
      dmd_ = connection_.getMetaData();

      closedConnection_ = testDriver_.getConnection(url_,
          systemObject_.getUserId(), encryptedPassword_);

    } else {
      connection_ = testDriver_.getConnection (url_ + ";naming=system",systemObject_.getUserId(), encryptedPassword_);
      dmd_ = connection_.getMetaData();

      closedConnection_ = testDriver_.getConnection (url_,systemObject_.getUserId(), encryptedPassword_);
    }
    dmd2_ = closedConnection_.getMetaData();
    closedConnection_.close();

    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      sqlConnection_ = testDriver_.getConnection(url_,
          systemObject_.getUserId(), encryptedPassword_);
    } else {
      sqlConnection_ = testDriver_.getConnection (url_ + ";naming=sql",systemObject_.getUserId(), encryptedPassword_);
    }

    dmd3_ = sqlConnection_.getMetaData();
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    connection_.close();
    sqlConnection_.close();
  }

  /**
   * Compares a comma delimited string with an array of strings.
   **/
  private boolean compare(String[] array, String cds) {
    boolean[] found = new boolean[array.length];
    StringTokenizer tokenizer = new StringTokenizer(cds, ",");
    int count = 0;
    while (tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken();
      ++count;
      for (int i = 0; i < array.length; ++i) {
        if (token.equals(array[i])) {
          found[i] = true;
          break;
        }
      }
    }

    for (int i = 0; i < found.length; ++i)
      if (!found[i])
        return false;

    if (count != array.length)
      return false;

    return true;
  }

  /**
   * Returns the release as a string.
   **/
  private String getReleaseAsString() {
    String release;
    switch (getRelease()) {
    case JDTestDriver.RELEASE_V7R1M0:
      release = "07.01.0000 V7R1m0";
      break;
    case JDTestDriver.RELEASE_V7R2M0:
      release = "07.02.0000 V7R2m0";
      break;
    case JDTestDriver.RELEASE_V7R3M0:
      release = "07.03.0000 V7R3m0";
      break;
    case JDTestDriver.RELEASE_V7R4M0:
      release = "07.04.0000 V7R4m0";
      break;
    case JDTestDriver.RELEASE_V7R5M0:
      release = "07.05.0000 V7R5m0";
      break;
    case JDTestDriver.RELEASE_V7R6M0:
      release = "07.06.0000 V7R6m0";
      break;
    default:
      release = "UNKNOWN RELEASE";
      Exception e = new Exception(release);
      e.printStackTrace();
      break;
    }
    return release;
  }

  /**
   * getCatalogSeparator() - Should return "." when the naming convention is
   * "sql".
   **/
  public void Var001() {
    try {
      assertCondition(dmd3_.getCatalogSeparator().equals("."));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getCatalogSeparator() - Should return "/" when the naming convention is
   * "system".
   **/
  public void Var002() {
    if (JDTestDriver.isLUW()) {
      notApplicable("System naming not applicable for luw");
    } else {
      try {
        assertCondition(dmd_.getCatalogSeparator().equals("/"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCatalogSeparator() - Should return the correct separator when the
   * connection is closed.
   **/
  public void Var003() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        assertCondition(dmd2_.getCatalogSeparator().equals("."));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCatalogTerm() - Should return the correct term on an open connection.
   **/
  public void Var004() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getDriverFixLevel() < 47828) {
      notApplicable("Fix for native in PTF SI47828");
      return;
    }
    try {
      assertCondition("Database".equals(dmd_.getCatalogTerm()),
          "catalogTerm = '" + dmd_.getCatalogTerm()
              + "' sb Database -- changed Aug 2012 Fixed in V7R1 PTF SI47828 fix level="
              + getDriverFixLevel());
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getCatalogTerm() - Should return the correct term on a closed connection.
   **/
  public void Var005() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getDriverFixLevel() < 47828) {
      notApplicable("Fix for native in PTF SI47828");
      return;
    }

    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getCatalogTerm().equals("Database"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getConnection() - Should return the connection on an open connection.
   **/
  public void Var006() {
    if (checkJdbc20()) {
      try {
        assertCondition(dmd_.getConnection() == connection_);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getConnection() - Should return the connection on a closed connection.
   **/
  public void Var007() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      if (checkJdbc20()) {
        try {
          assertCondition(dmd2_.getConnection() == closedConnection_);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getDatabaseProductName() - Should return the correct name on an open
   * connection.
   **/
  public void Var008() {
    String expectedName = "DB2 UDB for AS/400";
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      expectedName = "AS";
      System.out.println(
          "JCC expects '" + expectedName + "' as the databaseProductName");
    }

    try {
      assertCondition(dmd_.getDatabaseProductName().equals(expectedName),
          "databaseProductName is " + dmd_.getDatabaseProductName());
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDatabaseProductName() - Should return the correct name on a closed
   * connection.
   **/
  public void Var009() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(
            dmd2_.getDatabaseProductName().equals("DB2 UDB for AS/400"),
            "databaseProductName is " + dmd_.getDatabaseProductName());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getDatabaseProductVersion() - Should return the correct version on an open
   * connection.
   * 
   * Note: There is a difference in the layout of the string returned by the
   * Toolbox JDBC driver and the native JDBC Driver.
   **/
  public void Var010() {
    try {

      String expectedVersion = System.getProperty("os.version");
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        expectedVersion = "QSQ05050";
        System.out.println("JCC expects '" + expectedVersion
            + "' as the databaseProductVersion");
      }

      System.out.println("Version = " + dmd_.getDatabaseProductVersion());
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
          || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
        // toolbox version looks like 05.05.0000 V5R5m0
        assertCondition(
            dmd_.getDatabaseProductVersion().equals(getReleaseAsString()),
            "Product Version=" + dmd_.getDatabaseProductVersion() + " sb "
                + getReleaseAsString());
      } else {
        if (System.getProperty("java.home").indexOf("openjdk") >= 0) {
          expectedVersion = "V" + expectedVersion.charAt(0) + "R"
              + expectedVersion.charAt(2) + "M0";
        }
        assertCondition(
            dmd_.getDatabaseProductVersion().equals(expectedVersion),
            "Product Version=" + dmd_.getDatabaseProductVersion() + " sb "
                + expectedVersion);
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDatabaseProductVersion() - Should throw an exception when the connection
   * is closed.
   * 
   * Note: The native JDBC driver will not throw an exception when the
   * connection is closed and this method is called. The same value that would
   * be expected on an open connection is returned.
   **/
  public void Var011() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {

        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          dmd2_.getDatabaseProductVersion();
          failed("Didn't throw SQLException");
        }
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          String databaseProductVersion = dmd_.getDatabaseProductVersion();
          String expectedVersion = System.getProperty("os.version");
          if (System.getProperty("java.home").indexOf("openjdk") >= 0) {
            expectedVersion = "V" + expectedVersion.charAt(0) + "R"
                + expectedVersion.charAt(2) + "M0";
          }
          assertCondition(databaseProductVersion.equals(expectedVersion),
              "databaseProductVersion=" + databaseProductVersion + " sb "
                  + expectedVersion);
        } else {
          notApplicable("DRIVER not recognized");
        }
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getDefaultTransactionIsolation() - Should return the correct value when the
   * connection is open, and the "transaction isolation" property is not
   * specified.
   */
  public void Var012() {
    try {
      assertCondition(dmd_
          .getDefaultTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED,
          "getDefaultTransactionIsolation="
              + dmd_.getDefaultTransactionIsolation() + " sb "
              + Connection.TRANSACTION_READ_UNCOMMITTED
              + "=TRANSACTION_READ_UNCOMMITTED");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDefaultTransactionIsolation() - Should return the correct value when the
   * connection is open, and the "transaction isolation" property is set to a
   * bogus value.
   **/
  public void Var013() {
    try {
      Connection c;
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        c = testDriver_.getConnection(url_, userId_, encryptedPassword_);

      } else {
        c = testDriver_.getConnection(url_ + ";transaction isolation=bogus",
            systemObject_.getUserId(), encryptedPassword_);
      }
      DatabaseMetaData dmd = c.getMetaData();
      int dti = dmd.getDefaultTransactionIsolation();
      c.close();
      assertCondition(dti == Connection.TRANSACTION_READ_UNCOMMITTED,
          "getDefaultTransactionIsolation=" + dti + " sb "
              + Connection.TRANSACTION_READ_UNCOMMITTED
              + "=TRANSACTION_READ_UNCOMMITTED");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDefaultTransactionIsolation() - Should return the correct value when the
   * connection is open, and the "transaction isolation" property is set to
   * "none".
   * <P>
   * SQL400 - The native driver will never report back a transaction isolation
   * level lower than read uncommitted. This is because a level of none can be
   * taken to mean that transaction are not supported on other platforms.
   * Setting the level to none is valid, you just can't see that you did.
   **/
  public void Var014() {
    try {
      Connection c;
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        c = testDriver_.getConnection(url_, userId_, encryptedPassword_);

      } else {

        c = testDriver_.getConnection(url_ + ";transaction isolation=none",
            systemObject_.getUserId(), encryptedPassword_);
      }
      DatabaseMetaData dmd = c.getMetaData();
      int dti = dmd.getDefaultTransactionIsolation();
      c.close();
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
        assertCondition(dti == Connection.TRANSACTION_NONE);
      else
        assertCondition(dti == Connection.TRANSACTION_READ_UNCOMMITTED,
            "getDefaultTransactionIsolation=" + dti + " sb "
                + Connection.TRANSACTION_READ_UNCOMMITTED
                + "=TRANSACTION_READ_UNCOMMITTED");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDefaultTransactionIsolation() - Should return the correct value when the
   * connection is open, and the "transaction isolation" property is set to
   * "read uncommitted".
   */
  public void Var015() {
    try {
      Connection c;
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        c = testDriver_.getConnection(url_, userId_, encryptedPassword_);

      } else {

        c = testDriver_.getConnection(
            url_ + ";transaction isolation=read uncommitted",
            systemObject_.getUserId(), encryptedPassword_);
      }
      DatabaseMetaData dmd = c.getMetaData();
      int dti = dmd.getDefaultTransactionIsolation();
      c.close();
      assertCondition(dti == Connection.TRANSACTION_READ_UNCOMMITTED,
          "getDefaultTransactionIsolation=" + dti + " sb "
              + Connection.TRANSACTION_READ_UNCOMMITTED
              + "=TRANSACTION_READ_UNCOMMITTED");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDefaultTransactionIsolation() - Should return the correct value when the
   * connection is open, and the "transaction isolation" property is set to
   * "read committed".
   **/
  public void Var016() {
    try {
      Connection c;
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        c = testDriver_.getConnection(url_, userId_, encryptedPassword_);

      } else {

        c = testDriver_.getConnection(
            url_ + ";transaction isolation=read committed",
            systemObject_.getUserId(), encryptedPassword_);
      }

      DatabaseMetaData dmd = c.getMetaData();
      int dti = dmd.getDefaultTransactionIsolation();
      c.close();
      assertCondition(dti == Connection.TRANSACTION_READ_COMMITTED,
          "getDefaultTransactionIsolation=" + dti + " sb "
              + Connection.TRANSACTION_READ_COMMITTED
              + "=TRANSACTION_READ_COMMITTED");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDefaultTransactionIsolation() - Should return the correct value when the
   * connection is open, and the "transaction isolation" property is set to
   * "repeatable read".
   **/
  public void Var017() {
    try {
      Connection c;
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        c = testDriver_.getConnection(url_, userId_, encryptedPassword_);

      } else {

        c = testDriver_.getConnection(
            url_ + ";transaction isolation=repeatable read",
            systemObject_.getUserId(), encryptedPassword_);
      }
      DatabaseMetaData dmd = c.getMetaData();
      int dti = dmd.getDefaultTransactionIsolation();
      c.close();
      assertCondition(dti == Connection.TRANSACTION_REPEATABLE_READ);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDefaultTransactionIsolation() - Should return the correct value when the
   * connection is open, and the "transaction isolation" property is set to
   * "serializable".
   **/
  public void Var018() {
    try {
      Connection c;
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        c = testDriver_.getConnection(url_, userId_, encryptedPassword_);

      } else {

        c = testDriver_.getConnection(
            url_ + ";transaction isolation=serializable",
            systemObject_.getUserId(), encryptedPassword_);
      }
      DatabaseMetaData dmd = c.getMetaData();
      int dti = dmd.getDefaultTransactionIsolation();
      c.close();
      assertCondition(dti == Connection.TRANSACTION_SERIALIZABLE);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDefaultTransactionIsolation() - Should return the correct value when the
   * connection is closed.
   **/
  public void Var019() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_
            .getDefaultTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getDriverMajorVersion() - Should return the same value as
   * Driver.getMajorVersion() when the connection is open.
   **/
  public void Var020() {
    try {
      System.out
          .println("Driver major version is " + dmd_.getDriverMajorVersion());
      assertCondition(
          dmd_.getDriverMajorVersion() == driver_.getMajorVersion());
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDriverMajorVersion() - Should return the same value as
   * Driver.getMajorVersion() when the connection is closed.
   **/
  public void Var021() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(
            dmd2_.getDriverMajorVersion() == driver_.getMajorVersion());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getDriverMinorVersion() - Should return the same value as
   * Driver.getMinorVersion() when the connection is open.
   **/
  public void Var022() {
    /// if (getRelease() == JDTestDriver.RELEASE_V5R4M0 && getDriver() ==
    /// JDTestDriver.DRIVER_TOOLBOX) {
    /// notApplicable("Not working on V5R4 (on 5/3/2007)");
    /// return;
    /// }
    try {
      System.out
          .println("Driver minor version is " + dmd_.getDriverMinorVersion());
      assertCondition(dmd_.getDriverMinorVersion() == driver_.getMinorVersion(),
          " dmd_.getDriverMinorVersion ()=" + dmd_.getDriverMinorVersion()
              + " driver_.getMinorVersion ()=" + driver_.getMinorVersion());
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDriverMinorVersion() - Should return the same value as
   * Driver.getMinorVersion() when the connection is closed.
   **/
  public void Var023() {
    /// if (getRelease() == JDTestDriver.RELEASE_V5R4M0 && getDriver() ==
    /// JDTestDriver.DRIVER_TOOLBOX) {
    /// notApplicable("Not working on V5R4 (on 5/3/2007)");
    /// return;
    /// }

    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(
            dmd2_.getDriverMinorVersion() == driver_.getMinorVersion(),
            " dmd2_.getDriverMinorVersion ()=" + dmd2_.getDriverMinorVersion()
                + " driver_.getMinorVersion ()=" + driver_.getMinorVersion());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getDriverName() - Should return the correct name on an open connection.
   **/
  public void Var024() {
    try {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        assertCondition(
            dmd_.getDriverName().equals("AS/400 Toolbox for Java JDBC Driver"));
      } else if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
        assertCondition(dmd_.getDriverName().equals("jtopenlite JDBC Driver"),
            "Name is " + dmd_.getDriverName());
      } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        assertCondition(
            dmd_.getDriverName().equals("DB2 for OS/400 JDBC Driver"),
            "Name is " + dmd_.getDriverName());
      } else {
        assertCondition(false, "Driver not recognized");
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDriverName() - Should return the correct name on a closed connection.
   **/
  public void Var025() {
    try {

      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        assertCondition(dmd2_.getDriverName()
            .equals("AS/400 Toolbox for Java JDBC Driver"));
      } else if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
        assertCondition(dmd2_.getDriverName().equals("jtopenlite JDBC Driver"),
            "Name is " + dmd_.getDriverName());
      } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        assertCondition(
            dmd2_.getDriverName().equals("DB2 for OS/400 JDBC Driver"),
            "Name is " + dmd_.getDriverName());
      } else {
        assertCondition(false, "Driver not recognized");
      }

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDriverVersion() - Should return the same value as reflected by
   * Driver.getMajorVersion() and Driver.getMinorVersion() when the connection
   * is open.
   **/
  public void Var026() {
    /// if (getRelease() == JDTestDriver.RELEASE_V5R4M0 && getDriver() ==
    /// JDTestDriver.DRIVER_TOOLBOX) {
    /// notApplicable("Not working on V5R4 (on 5/3/2007)");
    /// return;
    /// }
    try {
      String expected = driver_.getMajorVersion() + "."
          + driver_.getMinorVersion();
      assertCondition(dmd_.getDriverVersion().equals(expected),
          "dmd_.getDriverVersion()='" + dmd_.getDriverVersion() + "' sb '"
              + expected + "'");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDriverVersion() - Should return the same value as reflected by
   * Driver.getMajorVersion() and Driver.getMinorVersion() when the connection
   * is closed.
   **/
  public void Var027() {
    /// if (getRelease() == JDTestDriver.RELEASE_V5R4M0 && getDriver() ==
    /// JDTestDriver.DRIVER_TOOLBOX) {
    /// notApplicable("Not working on V5R4 (on 5/3/2007)");
    /// return;
    /// }
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        String expected = driver_.getMajorVersion() + "."
            + driver_.getMinorVersion();
        assertCondition(dmd2_.getDriverVersion().equals(expected),
            "dmd2_.getDriverVersion()='" + dmd2_.getDriverVersion() + "' sb '"
                + expected + "'");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getExtraNameCharacters() - Should return the correct characters on an open
   * connection.
   **/
  public void Var028() {
    try {
      assertCondition(dmd_.getExtraNameCharacters().equals("$@#"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getExtraNameCharacters() - Should return the correct characters on an
   * closed connection.
   **/
  public void Var029() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getExtraNameCharacters().equals("$@#"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getIdentifierQuoteString() - Should return the correct string on an open
   * connection.
   **/
  public void Var030() {
    try {
      assertCondition(dmd_.getIdentifierQuoteString().equals("\""));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getIdentifierQuoteString() - Should return the correct string on an closed
   * connection.
   **/
  public void Var031() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getIdentifierQuoteString().equals("\""));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxBinaryLiteralLength() - Should return the correct value on an open
   * connection.
   **/
  public void Var032() {
    try {
      assertCondition(dmd_.getMaxBinaryLiteralLength() == 32739);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxBinaryLiteralLength() - Should return the correct value on a closed
   * connection.
   **/
  public void Var033() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getMaxBinaryLiteralLength() == 32739);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxCatalogNameLength() - Should return the correct value on an open
   * connection.
   **/
  public void Var034() {
    try {
      assertCondition(dmd_.getMaxCatalogNameLength() == 10);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxCatalogNameLength() - Should return the correct value on a closed
   * connection.
   **/
  public void Var035() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getMaxCatalogNameLength() == 10);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxCharLiteralLength() - Should return the correct value on an open
   * connection.
   **/
  public void Var036() {
    try {
      assertCondition(dmd_.getMaxCharLiteralLength() == 32739);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxCharLiteralLength() - Should return the correct value on a closed
   * connection.
   **/
  public void Var037() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getMaxCharLiteralLength() == 32739);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxColumnNameLength() - Should return the correct value on an open
   * connection.
   **/
  public void Var038() {
    try {
      if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) // @F1A
        assertCondition(dmd_.getMaxColumnNameLength() == 30);
      else // @F1A
        assertCondition(dmd_.getMaxColumnNameLength() == 128); // @F1A
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxColumnNameLength() - Should return the correct value on a closed
   * connection.
   **/
  public void Var039() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) // @F1A
          assertCondition(dmd2_.getMaxColumnNameLength() == 30);
        else // @F1A
          assertCondition(dmd2_.getMaxColumnNameLength() == 128); // @F1A
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxColumnsInGroupBy() - Should return the correct value on an open
   * connection.
   **/
  public void Var040() {
    try {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) { // @G1A
        int maxColumnsInGroupBy = dmd_.getMaxColumnsInGroupBy();
        assertCondition(maxColumnsInGroupBy == 8000,
            "maxColumnsInGroupBy is " + maxColumnsInGroupBy + " sb 8000");// @G1A
      } else // @G1A
        assertCondition(dmd_.getMaxColumnsInGroupBy() == 120);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxColumnsInGroupBy() - Should return the correct value on a closed
   * connection.
   **/
  public void Var041() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @G1A
          assertCondition(dmd2_.getMaxColumnsInGroupBy() == 8000); // @G1A
        else // @G1A
          assertCondition(dmd2_.getMaxColumnsInGroupBy() == 120);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxColumnsInIndex() - Should return the correct value on an open
   * connection.
   **/
  public void Var042() {
    try {
      assertCondition(dmd_.getMaxColumnsInIndex() == 120);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxColumnsInIndex() - Should return the correct value on a closed
   * connection.
   **/
  public void Var043() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getMaxColumnsInIndex() == 120);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxColumnsInOrderBy() - Should return the correct value on an open
   * connection.
   **/
  public void Var044() {
    try {
      assertCondition(dmd_.getMaxColumnsInOrderBy() == 10000);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxColumnsInOrderBy() - Should return the correct value on a closed
   * connection.
   **/
  public void Var045() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getMaxColumnsInOrderBy() == 10000);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxColumnsInSelect() - Should return the correct value on an open
   * connection.
   **/
  public void Var046() {
    try {
      assertCondition(dmd_.getMaxColumnsInSelect() == 8000);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxColumnsInSelect() - Should return the correct value on a closed
   * connection.
   **/
  public void Var047() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getMaxColumnsInSelect() == 8000);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxColumnsInTable() - Should return the correct value on an open
   * connection.
   **/
  public void Var048() {
    try {
      assertCondition(dmd_.getMaxColumnsInTable() == 8000);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxColumnsInTable() - Should return the correct value on a closed
   * connection.
   **/
  public void Var049() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getMaxColumnsInTable() == 8000);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxConnections() - Should return the correct value on an open
   * connection.
   **/
  public void Var050() {
    try {
      assertCondition(dmd_.getMaxConnections() == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxConnections() - Should return the correct value on a closed
   * connection.
   **/
  public void Var051() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getMaxConnections() == 0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxCursorNameLength() - Should return the correct value on an open
   * connection.
   **/
  public void Var052() {
    try {
      if (getRelease() < JDTestDriver.RELEASE_V7R1M0) // @G2A
        assertCondition(dmd_.getMaxCursorNameLength() == 18);
      else // @G2A
        assertCondition(dmd_.getMaxCursorNameLength() == 128); // @G2A
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxCursorNameLength() - Should return the correct value on a closed
   * connection.
   **/
  public void Var053() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        if (getRelease() < JDTestDriver.RELEASE_V7R1M0) // @G2A
          assertCondition(dmd2_.getMaxCursorNameLength() == 18);
        else // @G2A
          assertCondition(dmd2_.getMaxCursorNameLength() == 128); // @G2A
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * getMaxIndexLength() - Should return the correct value on an open
   * connection.
   **/
  public void Var054() {
    try {
      assertCondition(dmd_.getMaxIndexLength() == 2000);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxIndexLength() - Should return the correct value on a closed
   * connection.
   **/
  public void Var055() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getMaxIndexLength() == 2000);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxProcedureNameLength() - Should return the correct value on an open
   * connection.
   **/
  public void Var056() {
    try {
      assertCondition(dmd_.getMaxProcedureNameLength() == 128);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxProcedureNameLength() - Should return the correct value on a closed
   * connection.
   **/
  public void Var057() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        assertCondition(dmd2_.getMaxProcedureNameLength() == 128);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxRowSize() - Should return the correct value on an open connection.
   **/
  public void Var058() {
    try {
      assertCondition(dmd_.getMaxRowSize() == 32766);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxRowSize() - Should return the correct value on a closed connection.
   **/
  public void Var059() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        assertCondition(dmd2_.getMaxRowSize() == 32766);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxSchemaNameLength() - Should return the correct value on an open
   * connection.
   **/
  public void Var060() {
    try {

      int maxSchemaNameLength = dmd_.getMaxSchemaNameLength();
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        assertCondition(maxSchemaNameLength == 128,
            "Max schema length is " + maxSchemaNameLength + " sb 128 for V7R1");
      } else {
        assertCondition(maxSchemaNameLength == 10,
            "Max schema length is " + maxSchemaNameLength + " sb 128 for V7R1");
      }

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxSchemaNameLength() - Should return the correct value on a closed
   * connection.
   **/
  public void Var061() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {

        int maxSchemaNameLength = dmd_.getMaxSchemaNameLength();
        if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
          assertCondition(maxSchemaNameLength == 128, "Max schema length is "
              + maxSchemaNameLength + " sb 128 for V7R1");
        } else {
          assertCondition(maxSchemaNameLength == 10, "Max schema length is "
              + maxSchemaNameLength + " sb 128 for V7R1");
        }

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxStatementLength() - Should return the correct value on an open
   * connection.
   */
  public void Var062() {
    try {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @F2A
        assertCondition(dmd_.getMaxStatementLength() == 1048576,
            "maxStatementLength = " + dmd_.getMaxStatementLength()
                + " sb 1048576 for V5R4 and beyond"); // @F2A
      else // @F2A
        assertCondition(dmd_.getMaxStatementLength() == 32767);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxStatementLength() - Should return the correct value on a closed
   * connection.
   **/
  public void Var063() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @F2A
          assertCondition(dmd_.getMaxStatementLength() == 1048576,
              "maxStatementLength = " + dmd_.getMaxStatementLength()
                  + " sb 1048576 for V5R4 and beyond"); // @F2A
        else // @F2A
          assertCondition(dmd2_.getMaxStatementLength() == 32767);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxStatements() - Should return the correct value on an open connection.
   * 
   * Note: The handle limit for the native JDBC driver differs from that of the
   * toolbox JDBC driver. The native driver does not have a hard limit on the
   * number of statements and will always return a 0.
   **/
  public void Var064() {
    try {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
          || getDriver() == JDTestDriver.DRIVER_JTOPENLITE)
        assertCondition(dmd_.getMaxStatements() == 9999); // @C1C
      else
        assertCondition(dmd_.getMaxStatements() == 0); // @D3C
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxStatements() - Should return the correct value on a closed
   * connection.
   * 
   * Note: The handle limit for the native JDBC driver differs from that of the
   * toolbox JDBC driver. The native driver does not have a hard limit on the
   * number of statements and will always return a 0.
   **/
  public void Var065() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      try {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            || getDriver() == JDTestDriver.DRIVER_JTOPENLITE)
          assertCondition(dmd2_.getMaxStatements() == 9999); // @C1C
        else
          assertCondition(dmd2_.getMaxStatements() == 0); // @D3C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxTableNameLength() - Should return the correct value on an open
   * connection.
   **/
  public void Var066() {
    try {
      assertCondition(dmd_.getMaxTableNameLength() == 128);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxTableNameLength() - Should return the correct value on a closed
   * connection.
   **/
  public void Var067() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        assertCondition(dmd2_.getMaxTableNameLength() == 128);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxTablesInSelect() - Should return the correct value on an open
   * connection.
   **/
  public void Var068() {
    try {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        assertCondition(dmd_.getMaxTablesInSelect() == 1000,
            "The maximum tables for V5R4 should be 1000, but was reported as "
                + dmd_.getMaxTablesInSelect()
                + " -- changed by native driver 08/10/2005");
      } else {
        assertCondition(dmd_.getMaxTablesInSelect() == 32);
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxTablesInSelect() - Should return the correct value on a closed
   * connection.
   **/
  public void Var069() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
          assertCondition(dmd_.getMaxTablesInSelect() == 1000,
              "The maximum tables for V5R4 should be 1000, but was reported as "
                  + dmd_.getMaxTablesInSelect()
                  + " -- changed by native driver 08/10/2005");
        } else {
          assertCondition(dmd2_.getMaxTablesInSelect() == 32,
              "maxTablesInSelect = " + dmd_.getMaxTablesInSelect() + " sb 32");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getMaxUserNameLength() - Should return the correct value on an open
   * connection.
   **/
  public void Var070() {
    try {
      assertCondition(dmd_.getMaxUserNameLength() == 10);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMaxUserNameLength() - Should return the correct value on a closed
   * connection.
   **/
  public void Var071() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        assertCondition(dmd2_.getMaxUserNameLength() == 10);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getNumericFunctions() - Should return the correct value on an open
   * connection.
   * 
   * Note: The native JDBC driver supports a couple numeric functions that the
   * Toolbox does not support. The return values are pretty close to the same.
   **/
  public void Var072() {
    try {
      String[] expected;
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        expected = numericFunctions610_;
      } else {
        expected = numericFunctionsForNative530_;
      }
      boolean condition = compare(expected, dmd_.getNumericFunctions());
      if (!condition) {
        System.out.println("numeric functions do not match");
        System.out.println("actual  : " + dmd_.getNumericFunctions());
        System.out.print("expected: ");
        for (int i = 0; i < expected.length; i++)
          System.out.print(expected[i] + " ");
        System.out.println();
      }

      assertCondition(condition,
          "condition = " + condition + " and should be true");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getNumericFunctions() - Should return the correct value on a closed
   * connection.
   * 
   * Note: The native JDBC driver supports a couple numeric functions that the
   * Toolbox does not support. The return values are pretty close to the same.
   **/
  public void Var073() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        String[] expected;

        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          expected = numericFunctions610_;

        } else {
          expected = numericFunctionsForNative530_;
        }
        boolean condition = compare(expected, dmd2_.getNumericFunctions());
        if (!condition) {
          System.out.println("numeric functions do not match");
          System.out.println("actual  : " + dmd2_.getNumericFunctions());
          System.out.print("expected: ");
          for (int i = 0; i < expected.length; i++)
            System.out.print(expected[i] + " ");
          System.out.println();
        }

        assertCondition(condition,
            "condition = " + condition + " and should be true");

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getProcedureTerm() - Should return the correct term on an open connection.
   **/
  public void Var074() {
    try {
      assertCondition(dmd_.getProcedureTerm().equals("Procedure"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getProcedureTerm() - Should return the correct term on a closed connection.
   **/
  public void Var075() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        assertCondition(dmd2_.getProcedureTerm().equals("Procedure"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSchemaTerm() - Should return the correct term on an open connection.
   **/
  public void Var076() {
    try {
      assertCondition(dmd_.getSchemaTerm().equals("Library"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getSchemaTerm() - Should return the correct term on a closed connection.
   **/
  public void Var077() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        assertCondition(dmd2_.getSchemaTerm().equals("Library"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSearchStringEscape() - Should return the correct value on an open
   * connection.
   **/
  public void Var078() {
    try {
      assertCondition(dmd_.getSearchStringEscape().equals("\\"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getSearchStringEscape() - Should return the correct value on a closed
   * connection.
   **/
  public void Var079() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        assertCondition(dmd2_.getSearchStringEscape().equals("\\"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSQLKeywords() - Should return the correct value on an open connection.
   **/
  public void Var080() {
    try {
      String[] expectedKeywords;
      String keywords = dmd_.getSQLKeywords();
      boolean condition;
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && jdk16) {
        expectedKeywords = sqlKeywords720_JDBC40_;
      } else {
        expectedKeywords = sqlKeywords_;
      }
      condition = compare(expectedKeywords, keywords);
      if (!condition) {
        System.out.println("sqlKeywords do not match");
        System.out.println("actual  : " + keywords);
        System.out.print("expected: ");
        for (int i = 0; i < expectedKeywords.length; i++)
          System.out.print(expectedKeywords[i] + " ");
        System.out.println();
      }
      assertCondition(condition,
          "condition = " + condition + " and should be true");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getSQLKeywords() - Should return the correct value on a closed connection.
   **/
  public void Var081() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {

        String keywords = dmd2_.getSQLKeywords();
        boolean condition;
        String[] expectedKeywords;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && jdk16) {
          expectedKeywords = sqlKeywords720_JDBC40_;
        } else {
          expectedKeywords = sqlKeywords_;
        }
        condition = compare(expectedKeywords, keywords);
        if (!condition) {
          System.out.println("sqlKeywords do not match");
          System.out.println("actual  : " + keywords);
          System.out.print("expected: ");
          for (int i = 0; i < expectedKeywords.length; i++)
            System.out.print(expectedKeywords[i] + " ");
          System.out.println();
        }

        assertCondition(condition,
            "condition = " + condition + " and should be true");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * getStringFunctions() - Should return the correct value on an open
   * connection.
   **/
  public void Var082() {
    try {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        assertCondition(compare(stringFunctions610_, dmd_.getStringFunctions()),
            " Got " + dmd_.getStringFunctions());
      } else if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {

        assertCondition(
            compare(stringFunctions530_, dmd_.getStringFunctions()));

      } else {
        String[] expected;
        expected = stringFunctionsForNative530_;
        boolean condition;
        String s = dmd_.getStringFunctions();
        condition = compare(expected, s);

        if (!condition) {
          System.out.println("StringFunctions do not match");
          System.out.println("actual  : " + s);
          System.out.print("expected: ");
          for (int i = 0; i < expected.length; i++)
            System.out.print(expected[i] + " ");
          System.out.println();
        }
        assertCondition(condition,
            "condition = " + condition + " and should be true");
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getStringFunctions() - Should return the correct value on a closed
   * connection.
   **/
  public void Var083() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
          assertCondition(
              compare(stringFunctions610_, dmd2_.getStringFunctions()));

        } else {
          boolean condition;
          String s = dmd2_.getStringFunctions();
          String[] expected;
          expected = stringFunctionsForNative530_;

          condition = compare(expected, s);

          if (!condition) {
            System.out.println("StringFunctions do not match");
            System.out.println("actual  : " + s);
            System.out.print("expected: ");
            for (int i = 0; i < expected.length; i++)
              System.out.print(expected[i] + " ");
            System.out.println();
          }
          assertCondition(condition,
              "condition = " + condition + " and should be true");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSystemFunctions() - Should return the correct value on an open
   * connection.
   **/
  public void Var084() {
    try {
      assertCondition(compare(systemFunctions_, dmd_.getSystemFunctions()));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getSystemFunctions() - Should return the correct value on a closed
   * connection.
   **/
  public void Var085() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        assertCondition(compare(systemFunctions_, dmd2_.getSystemFunctions()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getTimeDateFunctions() - Should return the correct value on an open
   * connection.
   * 
   * Note: The native JDBC driver supports a couple timedate functions that the
   * Toolbox does not support. The return values are pretty close to the same.
   **/
  public void Var086() {
    try {
      String expected[];
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        expected = timeDateFunctions610_;

      } else {
        expected = timeDateFunctionsForNative530_;
      }

      boolean condition = compare(expected, dmd_.getTimeDateFunctions());
      if (!condition) {
        System.out.println("time date functions do not match");
        System.out.println("actual  : " + dmd_.getTimeDateFunctions());
        System.out.print("expected: ");
        for (int i = 0; i < expected.length; i++)
          System.out.print(expected[i] + " ");
        System.out.println();
      }

      assertCondition(condition,
          "condition = " + condition + " and should be true");

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTimeDateFunctions() - Should return the correct value on a closed
   * connection.
   * 
   * Note: The native JDBC driver supports a couple numeric functions that the
   * Toolbox does not support. The return values are pretty close to the same.
   **/
  public void Var087() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        String[] expected;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          expected = timeDateFunctions610_;

        } else {
          expected = timeDateFunctionsForNative530_;
        }

        boolean condition = compare(expected, dmd2_.getTimeDateFunctions());
        if (!condition) {
          System.out.println("time date functions do not match");
          System.out.println("actual  : " + dmd2_.getTimeDateFunctions());
          System.out.print("expected: ");
          for (int i = 0; i < expected.length; i++)
            System.out.print(expected[i] + " ");
          System.out.println();
        }

        assertCondition(condition,
            "condition = " + condition + " and should be true");

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getURL() - Should return the correct value on an open connection.
   **/
  public void Var088() {
    try {
      String expectedUrl = testDriver_.fixupUrl(url_ + ";naming=system");

      assertCondition(dmd_.getURL().equals(expectedUrl),
          "got '" + dmd_.getURL() + "' sb '" + expectedUrl + "'");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getURL() - Should throw an exception on a closed connection.
   * 
   * Note: The native driver will return the url what was last used to connect
   * on a closed connection.
   **/
  public void Var089() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          dmd2_.getURL();
          failed("Didn't throw SQLException");
        } else
          assertCondition(dmd2_.getURL().equals(url_),
              "got " + dmd2_.getURL() + " expected " + url_);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getUserName() - Should return the correct value on an open connection.
   **/
  public void Var090() {
    try {
      assertCondition(dmd_.getUserName().equals(systemObject_.getUserId()));
      ;
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUserName() - Should return the correct value on a closed connection.
   **/
  public void Var091() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      try {
        dmd2_.getUserName();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getUserName() - Should return the correct value on an open connection even
   * if the user didn't pass a user id when they connected.
   **/
  public void Var092() {
    // Test only for the Native driver today... Toolbox guys can decide whether
    // or not
    // to add it for their bucket later.
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      try {
        Connection c = DriverManager
            .getConnection("jdbc:db2://" + systemObject_.getSystemName());
        DatabaseMetaData dmd = c.getMetaData();
        String user = dmd.getUserName();
        String currentUser = System.getProperty("user.name");
        assertCondition(user.equalsIgnoreCase(currentUser),
            "User id does not match -- md=" + user + " currentUser="
                + currentUser);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    } else {
      notApplicable("Native driver variation");
    }
  }

  /**
   * getResultSetHoldability() - Should return the holdability on an open
   * connection.
   **/
  public void Var093() {
    if (areCursorHoldabilitySupported()) {
      try {
        assertCondition(dmd_
            .getResultSetHoldability() == ResultSet.HOLD_CURSORS_OVER_COMMIT);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    } else {
      notApplicable("Cursor holdability (need v5r2 and 1.4)");
    }
  }

  /**
   * getResultSetHoldability() - Should return the holdability on a closed
   * connection.
   **/
  public void Var094() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      if (areCursorHoldabilitySupported()) {
        try {
          assertCondition(dmd2_
              .getResultSetHoldability() == ResultSet.HOLD_CURSORS_OVER_COMMIT);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      } else {
        notApplicable("Cursor holdability (need v5r2 and 1.4)");
      }
    }
  }

  /**
   * getJDBCMajorVersion() - Should return the JDBCMajorVersion on a open
   * connection
   **/
  public void Var095() {
    if (checkJdbc30()) {

      try {
        assertCondition(dmd_.getJDBCMajorVersion() == getJdbcLevel(),
            " Major version returned " + dmd_.getJDBCMajorVersion()
                + " expected " + getJdbcLevel()
                + " New test added by Native driver 11/2002");
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception - New test added by Native driver 11/2002");
      }
    }

  }

  /**
   * getJDBCMajorVersion() - Should return the JDBCMajorVersion on a closed
   * connection
   **/
  public void Var096() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {

      if (checkJdbc30()) {

        try {
          assertCondition(dmd2_.getJDBCMajorVersion() == getJdbcLevel(),
              " Major version returned " + dmd_.getJDBCMajorVersion()
                  + " expected " + getJdbcLevel()
                  + "New test added by Native driver 11/2002");
        } catch (Exception e) {
          failed(e,
              "Unexpected Exception - New test added by Native driver 11/2002");
        }
      }
    }
  }

  /**
   * getJDBCMinorVersion() - Should return the JDBCMinorVersion on a open
   * connection
   **/
  public void Var097() {

    if (checkJdbc30()) {

      try {
        int minorVersion = dmd_.getJDBCMinorVersion();
        int expectedMinorVersion = 0;
        if (jdk18)
          expectedMinorVersion = 2;
        if (jdk19)
          expectedMinorVersion = 3;
        assertCondition(minorVersion == expectedMinorVersion,
            "minorVersion=" + minorVersion + " expected " + expectedMinorVersion
                + " New test added by Native driver 11/2002"); // We will need a
                                                               // better way to
                                                               // do
      } // this in the future
      catch (Exception e) {
        failed(e,
            "Unexpected Exception - New test added by Native driver 11/2002");
      }
    }
  }

  /**
   * getJDBCMinorVersion() - Should return the JDBCMinorVersion on a closed
   * connection
   **/
  public void Var098() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      if (checkJdbc30()) {

        try {

          int minorVersion = dmd2_.getJDBCMinorVersion();
          int expectedMinorVersion = 0;
          if (jdk18)
            expectedMinorVersion = 2;
          if (jdk19)
            expectedMinorVersion = 3;
          assertCondition(minorVersion == expectedMinorVersion,
              "minorVersion=" + minorVersion + " expected "
                  + expectedMinorVersion
                  + " New test added by Native driver 11/2002");
        } catch (Exception e) {
          failed(e,
              "Unexpected Exception - New test added by Native driver 11/2002");
        }
      }
    }
  }

  /**
   * getDatabaseMajorVersion() - Should return the DatabaseMajorVersion on a
   * open connection
   **/
  public void Var099() {
    if (checkJdbc30()) {
      int expectedMajorVersion = getRelease() / 100;
      // There is no such thing as 550, but native driver tests using 550
      if (getRelease() == JDTestDriver.RELEASE_V7R1M0)
        expectedMajorVersion = 6;
      try {
        assertCondition(dmd_.getDatabaseMajorVersion() == expectedMajorVersion,
            "New test added by Native driver 11/2002 Expected major version "
                + expectedMajorVersion + " got "
                + dmd_.getDatabaseMajorVersion());
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception - New test added by Native driver 11/2002");
      }
    }
  }

  /**
   * getDatabaseMajorVersion() - Should return the DatabaseMajorVersion on a
   * closed connection
   **/
  public void Var100() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      if (checkJdbc30()) {
        int expectedMajorVersion = getRelease() / 100;
        // There is no such thing as 550, but native driver tests using 550
        if (getDriver() == JDTestDriver.DRIVER_NATIVE
            && getRelease() == JDTestDriver.RELEASE_V7R1M0)
          expectedMajorVersion = 6;
        try {

          if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE)
            expectedMajorVersion = 0;

          if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
            assertCondition(
                dmd2_.getDatabaseMajorVersion() == 0
                    || dmd2_.getDatabaseMajorVersion() == expectedMajorVersion,
                "dmd2_.getDatabaseMajorVersion()="
                    + dmd2_.getDatabaseMajorVersion() + " sb 0 or "
                    + expectedMajorVersion
                    + "(toolbox return value on unconnected DB).  New test added by Native driver 11/2002"); // toolbox
                                                                                                             // returns
                                                                                                             // 0
                                                                                                             // on
                                                                                                             // unconnected
          else
            assertCondition(
                dmd2_.getDatabaseMajorVersion() == expectedMajorVersion,
                "Expect major version " + expectedMajorVersion + " got "
                    + dmd2_.getDatabaseMajorVersion()
                    + " New test added by Native driver 11/2002");
        } catch (Exception e) {
          failed(e,
              "Unexpected Exception - New test added by Native driver 11/2002");
        }
      }
    }
  }

  /**
   * getDatabaseMinorVersion() - Should return the DatabaseMinorVersion on a
   * open connection
   **/
  public void Var101() {
    if (checkJdbc30()) {

      try {
        int expectedVersion = (getRelease() % 100) / 10;
        // There is no such thing as 550, but native driver tests using 550
        if (getRelease() == JDTestDriver.RELEASE_V7R1M0)
          expectedVersion = 1;


        assertCondition(dmd_.getDatabaseMinorVersion() == expectedVersion,
            "New test added by Native driver 11/2002 -> minor version reported = "
                + dmd_.getDatabaseMinorVersion() + " expected = "
                + expectedVersion + " from class " + dmd_.getClass().getName());

      } catch (Exception e) {
        failed(e,
            "Unexpected Exception - New test added by Native driver 11/2002");
      }
    }
  }

  /**
   * getJDBCDatabaseVersion() - Should return the DatabaseMinorVersion on a
   * closed connection
   **/
  public void Var102() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      if (checkJdbc30()) {

        try {
          int expectedVersion = (getRelease() % 100) / 10;
          // There is no such thing as 550, but native driver tests using 550
          if (getDriver() == JDTestDriver.DRIVER_NATIVE
              && getRelease() == JDTestDriver.RELEASE_V7R1M0)
            expectedVersion = 1;
          if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
            expectedVersion = 0;
          }

          if (getDriver() == JDTestDriver.DRIVER_NATIVE)
            assertCondition(dmd2_.getDatabaseMinorVersion() == expectedVersion,
                "New test added by Native driver 11/2002 -> minor version reported = "
                    + dmd_.getDatabaseMinorVersion() + " expected = "
                    + expectedVersion);
          else {
            if (dmd2_.getDatabaseMinorVersion() == 0) // Toolbox returns 0 on
                                                      // closed connection
              succeeded();
            else
              failed(
                  "Unexpected Results:  Expected 0 for getDatabaseMinorVersion() recieved "
                      + dmd_.getDatabaseMinorVersion());
          }
        } catch (Exception e) {
          failed(e,
              "Unexpected Exception - New test added by Native driver 11/2002");
        }
      }
    }
  }

  /**
   * getSuperTypes(String catalog, String schemaPattern, String typeNamePattern)
   * - The iSeries has no concept of a type hierarchy so the results of
   * executing this method will always be an empty ResultSet. -open connection
   **/
  public void Var103() {
    if (checkJdbc30()) {

      try {
        ResultSet rs = dmd_.getSuperTypes("", "", "");
        assertCondition(!rs.next(), "New test added by Native driver 11/2002"); // should
                                                                                // be
                                                                                // an
                                                                                // empty
                                                                                // set
        rs.close();
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception - New test added by Native driver 11/2002");
      }
    }
  }

  /**
   * getSuperTables(String catalog, String schemaPattern, String
   * tableNamePattern) - The iSeries has no concept of a table hierarchy so the
   * results of executing this method will always be an empty ResultSet. -open
   * connection
   **/
  public void Var104() {
    if (checkJdbc30()) {

      try {
        ResultSet rs = dmd_.getSuperTables("", "", "");
        assertCondition(!rs.next(), "New test added by Native driver 11/2002"); // should
                                                                                // be
                                                                                // an
                                                                                // empty
                                                                                // set
        rs.close();
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception - New test added by Native driver 11/2002");
      }
    }
  }

  /**
   * getAttributes(String catalog, String schemaPattern, String typeNamePattern,
   * String attributeNamePattern) - The iSeries does not support structured
   * types at this time, an empty ResultSet will always be returned for calls to
   * this method. -open connection
   **/
  public void Var105() {
    if (checkJdbc30()) {

      try {
        ResultSet rs = dmd_.getAttributes("", "", "", "");
        assertCondition(!rs.next(), "New test added by Native driver 11/2002"); // should
                                                                                // be
                                                                                // an
                                                                                // empty
                                                                                // set
        rs.close();
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception - New test added by Native driver 11/2002");
      }
    }
  }

  /**
   * getSQLStateType() - for the native driver we always return sqlState99, I am
   * not sure if toolbox does the same -open connection
   **/
  public void Var106() {
    if (checkJdbc30()) {
      try {
        assertCondition(dmd_.getSQLStateType() == 2,
            "New test added by Native driver 11/2002"); // sqlStateSQL99 == 2
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception - New test added by Native driver 11/2002");
      }
    }
  }

  /**
   * getSQLStateType() - for the native driver we always return sqlState99, I am
   * not sure if toolbox does the same -closed connection
   **/
  public void Var107() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      if (checkJdbc30()) {

        try {
          assertCondition(dmd2_.getSQLStateType() == 2,
              "New test added by Native driver 11/2002"); // sqlStateSQL99 == 2
        } catch (Exception e) {
          failed(e,
              "Unexpected Exception - New test added by Native driver 11/2002");
        }
      }
    }
  }

  /**
   * getMaxLogicalLobSize() - -open connection
   **/
  public void Var108() {
    if (checkJdbc42()) {
      try {
        long maxLogicalLobSize = JDReflectionUtil.callMethod_L(dmd_,
            "getMaxLogicalLobSize");
        long expectedSize = 2147483647;
        assertCondition(maxLogicalLobSize == expectedSize,
            "Got " + maxLogicalLobSize + " sb " + expectedSize);
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception - New test added by Native driver 02/2014");
      }
    }
  }

  /**
   * getMaxLogicalLobSize() - -closed connection
   **/
  public void Var109() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      if (checkJdbc42()) {

        try {
          long maxLogicalLobSize = JDReflectionUtil.callMethod_L(dmd2_,
              "getMaxLogicalLobSize");
          long expectedSize = 2147483647;
          assertCondition(maxLogicalLobSize == expectedSize,
              "Got " + maxLogicalLobSize + " sb " + expectedSize);

        } catch (Exception e) {
          failed(e,
              "Unexpected Exception - New test added by Native driver 02/2014");
        }
      }
    }
  }

  /**
   * supportsRefCursors() - -open connection
   **/
  public void Var110() {
    if (checkJdbc42()) {
      try {
        boolean value = JDReflectionUtil.callMethod_B(dmd_,
            "supportsRefCursors");
        boolean expected = false;
        assertCondition(value == expected, "Got " + value + " sb " + expected);
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception - New test added by Native driver 02/2014");
      }
    }
  }

  /**
   * supportsRefCursors() - -closed connection
   **/
  public void Var111() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC doesn't permit call on closed connection");
    } else {
      if (checkJdbc42()) {

        try {
          boolean value = JDReflectionUtil.callMethod_B(dmd2_,
              "supportsRefCursors");
          boolean expected = false;
          assertCondition(value == expected,
              "Got " + value + " sb " + expected);

        } catch (Exception e) {
          failed(e,
              "Unexpected Exception - New test added by Native driver 02/2014");
        }
      }
    }
  }

  /**
   * getDatabaseProductVersion() - Should return the correct version on an open
   * connection on a toolbox connection when the port number is specified. 
    **/
  public void Var112() {
    if (checkToolbox()) {
      try {
        String urlWithPort = "jdbc:as400://" + systemObject_.getSystemName() + ":8471";

        Connection connection = testDriver_.getConnection(urlWithPort, systemObject_.getUserId(), encryptedPassword_);
        DatabaseMetaData dmd = connection.getMetaData();
        assertCondition(dmd.getDatabaseProductVersion().equals(getReleaseAsString()),
            "Product Version=" + dmd.getDatabaseProductVersion() + " sb " + getReleaseAsString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

}
