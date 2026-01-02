///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCRowSetTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.AS400JDBC;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;
import java.sql.*;

import javax.sql.*;
import javax.naming.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;
import com.ibm.as400.access.AS400JDBCManagedConnectionPoolDataSource;
import com.ibm.as400.access.AS400JDBCManagedDataSource;
import com.ibm.as400.access.AS400JDBCRowSet;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;

import test.PasswordVault;
import test.Testcase;

//import javasoft.sqe.tests.ibm.jdbc.JDSetupCollection;      //@A7A
import com.ibm.as400.access.CommandCall; //@A7A

/**
 * Testcase AS400JDBCRowSetTestcase.
 **/
public class AS400JDBCRowSetTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "AS400JDBCRowSetTestcase";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.AS400JDBCRowSetTest.main(newArgs);
  }
  // Environment variables.
  // private static final int OS_AS400 = 0;
  // private static final int OS_WINDOWS = 1;
  // private int environment_ = -1;

  // JNDI variables.
  private static final String JNDI_FILE = "com.sun.jndi.fscontext.RefFSContextFactory";
  private static final String JNDI_LDAP = "com.ibm.jndi.LDAPCtxFactory";
  private Context context_;
  private String jndiType_ = JNDI_FILE;

  // Logging variables.
  private static final String logDirectory_ = "javatest";
  private static final String logFileName_ = logDirectory_ + "/rowset.log";
  private String jndiName_ = logDirectory_ + "/customer";
  private String managedDataSourceName_ = logDirectory_ + "/managedDataSource";
  private String poolingDataSourceName_ = logDirectory_ + "/poolingDataSource";
  private String traceDS_ = logDirectory_ + "/trace";
  private String collName_ = "MYCOLL";
  private String dbaseName_ = "JTBOX";

  private BufferedReader reader_ = null;
  private PrintWriter writer_ = null;
  private File testFile_;
  private File javatest_; // @A6A
  private long previousFileEnd_;
  // private String tag = null;
  // private boolean rejectChange = false;
  private Random random_;
  // private Blob blob_;
  // private Clob clob_;
  // private Ref ref_;
  // private Array array_;
  private PrintWriter previousWriter_ = null; // @A3A

  // private AS400JDBCRowSet rowset_;
  private AS400JDBCDataSource traceDataSource_;
  private String username_;

  private String authorityUsr_ = null; // @A2A
  private String authorityPwd_ = null; // @A2A

  private Properties env_ = null; // @A8A

  /**
   * Constructor. This is called from the AS400JDBCRowSetTestcase constructor.
   **/
  public AS400JDBCRowSetTestcase(AS400 systemObject, Vector<String> variationsToRun, int runMode,
      FileOutputStream fileOutputStream,

      String password, String jndiType, String authorityUsr, // @A2A
      String authorityPwd) // @A2A
  {
    super(systemObject, "AS400JDBCRowSetTestcase", variationsToRun, runMode, fileOutputStream, password);

    if (jndiType == null)
      throw new NullPointerException("-misc parameter is missing.");

    if (jndiType.equals("file"))
      jndiType_ = JNDI_FILE;
    else if (jndiType.equals("ldap"))
      jndiType_ = JNDI_LDAP;
    else
      output_.println("WARNING... Unknown jndi type '" + jndiType + "' using default of " + jndiType_);
    authorityUsr_ = authorityUsr; // @A2A
    authorityPwd_ = authorityPwd; // @A2A
  }

  /**
   * Checks the trace log for the compare string.
   * 
   * @param compare The compare string.
   **/
  private boolean checkLog(String prefix, String suffix, StringBuffer sb) {
    boolean equal = false;

    try {
      reader_ = new BufferedReader(new FileReader(testFile_));
      reader_.skip(previousFileEnd_);

      String line = reader_.readLine();

      // int i=0;
      while (line != null) {
        // @C2C Lines used to look like: "as400: RowSet : execute()."
        // @C2C Now they look like: "as400: RowSet (531758) : execute()."
        // @C3C Check for the two parts.
        if ((line.indexOf(prefix) == 0) && (line.indexOf(suffix) > 0)) { // @C2A
          equal = true; // @C2A
          reader_.close();
          return equal;
        } // @C2A
        // @C2D if (line.equals(compare))
        // @C2D {
        // @C2D equal = true;
        // @C2D break;
        // @C2D }
        line = reader_.readLine();
      }
      reader_.close();
      if (!equal) {
        sb.append("Did not find " + prefix + " " + suffix + "\n");
      }
    } catch (Exception e) {
      output_.println("Log check failed.");
      e.printStackTrace();
    }
    return equal;
  }

  /**
   * Cleans up objects used by the testcase driver.
   **/
  public void cleanup() {
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400JDBCDataSource ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(),
          charPassword);
      PasswordVault.clearPassword(charPassword);
      Connection c = ds.getConnection();

      Statement s = c.createStatement();
      s.executeUpdate("DROP TABLE " + collName_ + "." + dbaseName_);
      s.close();
      c.close();

      writer_.close();
      // @A3D DriverManager.setLogStream(null);

      if (!false) // @A9A
      {
        if (!testFile_.delete())
          output_.println("WARNING... testcase cleanup could not delete: " + logFileName_);
        if (!javatest_.delete()) // @A6A
          output_.println("WARNING... testcase cleanup could not delete: " + logDirectory_); // @A6A
      }
    } catch (Exception e) {
      output_.println("AS400JDBCRowSetTestcase cleanup FAILED!");
      e.printStackTrace();
    }
  }

  /**
   * Testcase setup.
   **/
  public void setup() {
    if (jndiType_ == JNDI_LDAP) // @A2A
      jndiName_ = "cn=test123datasource, cn=users, ou=rochester,o=ibm,c=us"; // @A2A

    // Determine the environment.
    String os = System.getProperty("os.name");
    output_.println("Environment: " + os);

    {
      // Get the JNDI Initial Context.
      env_ = new Properties(); // @A8C

      if (jndiType_ == JNDI_LDAP) // @A2A
      {
        // @A2A
        env_.put("java.naming.provider.url", "ldap://" + systemObject_.getSystemName()); // @A2A
        env_.put("java.naming.security.principal", "cn=" + authorityUsr_); // @A2A
        env_.put("java.naming.security.credentials", authorityPwd_); // @A2A
      } // @A2A
      else
        env_.put(Context.PROVIDER_URL, "file:.");

      env_.put(Context.INITIAL_CONTEXT_FACTORY, jndiType_);

      // @A8D System.setProperty(Context.INITIAL_CONTEXT_FACTORY, jndiType_); //@A1A

      try {
        context_ = new InitialContext(env_);
      } catch (Exception cxt) {
        output_.println("ERROR: testcase setup failed to initialize the JNDI context.");
        cxt.printStackTrace();
      }
    }

    if (!false) // @A9A
    {
      // Make sure javatest dir exists.
      javatest_ = new File(logDirectory_); // @A6C
      
      if (!javatest_.exists()) {
        output_.println("Setup is creating 'javatest' directory.");

        if (!javatest_.mkdir()) {
          output_.println("WARNING:  Setup could not create the 'javatest' directory.");
        }
      }

      try {
        testFile_ = new File(logFileName_);
        testFile_.createNewFile();

        writer_ = new PrintWriter(new FileWriter(testFile_));
        previousWriter_ = DriverManager.getLogWriter(); // @A5A
        // @A3D DriverManager.setLogStream(writer_);
      } catch (IOException e) {
        output_.println("WARNING... testcase setup could not create log file: " + logFileName_);
        e.printStackTrace();
      }
    }

    {
      registerDataSource();
      registerTraceDataSource();
    }
    username_ = systemObject_.getUserId();
    random_ = new Random();
    createTestDatabase();
  }

  private void registerTraceDataSource() {
    String serverName = systemObject_.getSystemName();
    String description = "Trace Data Source";

    if (jndiType_ == JNDI_LDAP) // @A2A
      traceDS_ = "cn=test121datasource, cn=users, ou=rochester,o=ibm,c=us"; // @A2A

    // Create a data source to the AS/400 database.
    AS400JDBCDataSource dataSource = new AS400JDBCDataSource();
    dataSource.setServerName(serverName);
    // dataSource.setDatabaseName("*LOCAL");
    dataSource.setDataSourceName(traceDS_);
    dataSource.setDescription(description);

    // Register the data source with JNDI.
    try {
      context_.bind(traceDS_, dataSource);
    } catch (NamingException n) {
      try {
        context_.unbind(traceDS_);
        context_.bind(traceDS_, dataSource);
      } catch (NamingException n2) {
        output_.println("Setup error occurred.");
        n2.printStackTrace();
      }
    }

    try {
      traceDataSource_ = (AS400JDBCDataSource) context_.lookup(traceDS_);
    } catch (Exception tds) {
      tds.printStackTrace();
    }
  }

  /**
   * Registers a data source with JNDI.
   **/
  private void registerDataSource() {
    String serverName = systemObject_.getSystemName();
    String description = "Customer Database";

    // Create a data source to the AS/400 database.
    AS400JDBCDataSource dataSource = new AS400JDBCDataSource();
    dataSource.setServerName(serverName);

    // dataSource.setDatabaseName("*LOCAL");

    dataSource.setDataSourceName(jndiName_);
    dataSource.setDescription(description);

    // Register the data source within JNDI.
    try {
      context_.bind(jndiName_, dataSource);
    } catch (NamingException n) {
      try {
        context_.unbind(jndiName_);
        context_.bind(jndiName_, dataSource);
      } catch (NamingException n2) {
        output_.println("Setup error occurred.");
        n2.printStackTrace();
      }
    } catch (IllegalArgumentException n) {
      try {
        Hashtable<?, ?> environment = context_.getEnvironment();
        output_.println("Illegal argument exception encountered"); 
        output_.println("Environment is "); 
        Enumeration<?> keys = environment.keys(); 
        while (keys.hasMoreElements()) {
          Object key = keys.nextElement(); 
          Object value = environment.get(key); 
          output_.println("  "+key+":"+value); 
        }
        
        output_.println("Resetting environment"); 
        if (javatest_.exists()) {
          output_.println("Setup is deleting 'javatest' ("+javatest_+") directory.");
          File[] files = javatest_.listFiles();
          for (int i = 0; i < files.length; i++) { 
            files[i].delete(); 
          }
          javatest_.delete(); 
          if (!javatest_.mkdir()) {
            output_.println("WARNING:  Setup could not create the 'javatest' directory.");
          }
        }

        context_.unbind(jndiName_);
        context_.bind(jndiName_, dataSource);
      } catch (Exception n2) {
        output_.println("Setup error occurred.");
        n2.printStackTrace();
      }
    }

  }

  /**
   * Setup to create a Blob used by the testcases.
   **/
  // void setupCreateBlob()
  // {
  // blob_ = new TestBlob("TEST DATA IN BLOB");
  // }

  class TestBlob implements Blob {
    private String data_;

    public TestBlob(String data) {
      data_ = data;
    }

    public InputStream getBinaryStream() {
      return null;
    }

    public byte[] getBytes(long position, int length) {
      byte[] value = null;
      return value;
    }

    public long length() {
      Integer size = Integer.valueOf(data_.length());
      return size.longValue();
    }

    public long position(Blob pattern, long start) {
      return 0;
    }

    public long position(byte[] pattern, long start) {
      return 0;
    }

    public int setBytes(long pos, byte[] bytes) // @C1A
        throws SQLException // @C1A
    { // @C1A
      // add code to test new methods //@C1A
      return 0; // @C1A
    } // @C1A

    public int setBytes(long pos, byte[] bytes, int offest, int len) // @C1A
        throws SQLException // @C1A
    { // @C1A
      // add code to test new methods //@C1A
      return 0; // @C1A
    } // @C1A

    public OutputStream setBinaryStream(long pos) // @C1A
        throws SQLException // @C1A
    { // @C1A
      // add code to test new methods //@C1A
      return null; // @C1A
    } // @C1A

    public void truncate(long len) // @C1A
        throws SQLException // @C1A
    { // @C1A
      // add code to test new methods //@C1A
    } // @C1A

    public void free() throws SQLException {
      // TODO Auto-generated method stub

    }

    public InputStream getBinaryStream(long arg0, long arg1) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }
  }

  /**
   * Setup to create a java.sql.Clob object used by the testcases.
   **/
  // void setupCreateClob()
  // {
  // clob_ = new TestClob("THIS IS A TEST CLOB WITH SOME MISC INFORMATION.");
  // }
  class TestClob implements Clob {
    private String data_;

    public TestClob(String data) {
      data_ = data;
    }

    public InputStream getAsciiStream() {
      return null;
    }

    public Reader getCharacterStream() {
      return null;
    }

    public String getSubString(long position, int length) {
      return "";
    }

    public long length() {
      Integer size = Integer.valueOf(data_.length());
      return size.longValue();
    }

    public long position(Clob searchString, long start) {
      return 0;
    }

    public long position(String searchString, long start) {
      return 0;
    }

    public int setString(long pos, String str) // @C1A
        throws SQLException // @C1A
    { // @C1A
      return 0; // @C1A
    } // @C1A

    public int setString(long pos, String str, int offest, int len) // @C1A
        throws SQLException // @C1A
    { // @C1A
      // add code to test new methods //@C1A
      return 0; // @C1A
    } // @C1A

    public OutputStream setAsciiStream(long pos) // @C1A
        throws SQLException // @C1A
    { // @C1A
      // add code to test new methods //@C1A
      return null; // @C1A
    } // @C1A

    public Writer setCharacterStream(long pos) // @C1A
    { // @C1A
      // add code to test new methods //@C1A
      return null; // @C1A
    } // @C1A

    public void truncate(long len) // @C1A
        throws SQLException // @C1A
    { // @C1A
      // add code to test new methods //@C1A
    } // @C1A

    public void free() throws SQLException {
      // TODO Auto-generated method stub

    }

    public Reader getCharacterStream(long arg0, long arg1) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

  }

  /**
   * Setup to create a java.sql.Ref object used by the testcases.
   **/
  // void setupCreateRef()
  // {
  // ref_ = new TestRef();
  // }
  class TestRef implements Ref {
    public TestRef() {

    }

    public String getBaseTypeName() {
      return "Test Ref";
    }

    public java.lang.Object getObject(java.util.Map<String,Class<?>> map) // @C1A
        throws SQLException // @C1A
    { // @C1A
      // add code to test new methods //@C1A
      return null; // @C1A
    } // @C1A

    public java.lang.Object getObject() // @C1A
        throws SQLException // @C1A
    { // @C1A
      // add code to test new methods //@C1A
      return null; // @C1A
    } // @C1A

    public void setObject(java.lang.Object value) // @C1A
        throws SQLException // @C1A
    { // @C1A
      // add code to test new methods //@C1A
    } // @C1A

  }

  /**
   * Setup to create an java.sql.Array used by the testcases.
   **/
  // void setupCreateArray()
  // {
  // array_ = new TestArray();
  // }
  class TestArray implements Array {
    public TestArray() {

    }

    public Object getArray() {
      return null;
    }

    public Object getArray(long pos, int count) {
      return null;
    }

    public Object getArray(long pos, int count, Map<String, Class<?>> map) {
      return null;
    }

    public Object getArray(Map<String, Class<?>> map) {
      return null;
    }

    public int getBaseType() {
      return Types.INTEGER;
    }

    public String getBaseTypeName() {
      return "java.lang.Integer";
    }

    public ResultSet getResultSet() {
      return null;
    }

    public ResultSet getResultSet(long pos, int count) {
      return null;
    }

    public ResultSet getResultSet(long pos, int count, Map<String, Class<?>> map) {
      return null;
    }

    public ResultSet getResultSet(Map<String, Class<?>> map) {
      return null;
    }

    public void free() throws SQLException {
      // TODO Auto-generated method stub

    }
  }

  /**
   * Setup to create a test database used by the testcases.
   **/
  private void createTestDatabase() {
    String[] states = { "AL", "AK", "AS", "AZ", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS",
        "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY",
        "OH", "OR", "OK", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY" };

    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      AS400JDBCDataSource ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(),
          charPassword);
      PasswordVault.clearPassword(charPassword);
      Connection c = ds.getConnection();

      Statement s = c.createStatement();

      boolean created = false; // @A7A
      try { // @A7A
        s.executeUpdate("CREATE COLLECTION " + collName_); // @A7A
        created = true; // @A7A
      } // @A7A
      catch (SQLException e) { // @A7A
        // The collection already exists. //@A7A
        created = false; // @A7A
      } // @A7A
      // @A7A
      // We need to grant object authority to the //@A7A
      // collection and the collection's journal so that //@A7A
      // other users may create tables in it. //@A7A
      if (created) { // @A7A
        AS400 authoritySys = new AS400(systemObject_.getSystemName(), // @A7A
            authorityUsr_, authorityPwd_.toCharArray()); // @A7A
        CommandCall cmd = new CommandCall(authoritySys); // @A7A
        cmd.run("QSYS/GRTOBJAUT OBJ(QSYS/" + collName_ + ") OBJTYPE(*LIB) " // @A7A
            + "USER(*PUBLIC) AUT(*ALL)"); // @A7A
        cmd.run("QSYS/GRTOBJAUT OBJ(" + collName_ + "/QSQJRN) OBJTYPE(*JRN) " // @A7A
            + "USER(*PUBLIC) AUT(*ALL)"); // @A7A
      } // @A7A

      // @A7Dtry
      // @A7D{
      // @A7D s.executeUpdate ("CREATE COLLECTION " + collName_);
      // @A7D}
      // @A7Dcatch (SQLException e)
      // @A7D{
      // @A7D // The collection already exists.
      // @A7D}

      s.executeUpdate("CREATE TABLE " + collName_ + "." + dbaseName_
          + " (NAME VARCHAR(10), ID INTEGER, STATE VARCHAR(2), WORTH DOUBLE)");
      s.close();

      PreparedStatement ps = c.prepareStatement(
          "INSERT INTO " + collName_ + "." + dbaseName_ + " (NAME, ID, STATE, WORTH) VALUES (?,?,?,?)");
      for (int i = 0; i < 50; i++) {
        ps.setString(1, "Customer" + i);
        ps.setInt(2, 100000 + i);
        ps.setString(3, states[i]);
        ps.setDouble(4, getPurchases("10000000"));
        ps.executeUpdate();
      }
      ps.close();
      c.close();
    } catch (Exception e) {
      output_.println("AS400JDBCRowSetTestcase setup FAILED!");
      e.printStackTrace();
    }
  }

  private double getPurchases(String account) {
    Integer dollar = Integer.valueOf(random_.nextInt(Integer.valueOf(account).intValue()));
    Integer cents = Integer.valueOf(random_.nextInt(100));

    String purchases = dollar + "." + cents;
    return Double.valueOf(purchases).doubleValue();
  }

  /**
   * Validates that absolute(int) updates the position of the result set as
   * expected.
   **/
  public void Var001() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      assertCondition(rowset.absolute(2) == true);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that afterLast(int) updates the position of the result set as
   * expected.
   **/
  public void Var002() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if (jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      rowset.afterLast();

      assertCondition(rowset.isAfterLast() == true);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that beforeFirst(int) updates the position of the result set as
   * expected.
   **/
  public void Var003() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      rowset.beforeFirst();

      assertCondition(rowset.isBeforeFirst() == true);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that first() updates the position of the result set as expected.
   **/
  public void Var004() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if (jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      rowset.first();

      assertCondition(rowset.isFirst() == true);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that isBeforeFirst() returns the position of the result set as
   * expected.
   **/
  public void Var005() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      assertCondition(rowset.isBeforeFirst() == true);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that isFirst() returns the position of the result set as expected.
   **/
  public void Var006() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if (jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      boolean before = rowset.isFirst();

      rowset.next();
      assertCondition(rowset.isFirst() == true && before == false);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that isLast() returns the position of the result set as expected.
   **/
  public void Var007() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      assertCondition(rowset.isLast() == false);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that isReadOnly() returns the expected value.
   **/
  public void Var008() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowset.setReadOnly(true);
      // @A8D if (jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      assertCondition(rowset.isReadOnly() == true);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that isUseDataSource() returns the expected value.
   **/
  public void Var009() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowset.setUseDataSource(false);

      assertCondition(rowset.isUseDataSource() == false);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that last() returns the expected value.
   **/
  public void Var010() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      rowset.last();

      assertCondition(rowset.isLast() == true);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that next() returns the expected value.
   **/
  public void Var011() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if (jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      boolean step = rowset.next();

      assertCondition(rowset.getRow() == 1, "step = " + step);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that previous() returns the expected value.
   **/
  public void Var012() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      int pos = 2;
      rowset.absolute(pos);

      boolean back = rowset.previous();

      assertCondition(back && rowset.getRow() == (pos - 1));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that relative(int) returns the expected value.
   **/
  public void Var013() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if (jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      rowset.first();
      int before = rowset.getRow();

      int steps = 2;
      boolean move = rowset.relative(steps);

      assertCondition(move && rowset.getRow() == (before + steps));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row set changes.
   **/
  public void Var014() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      MyRowListener listener = new MyRowListener();
      rowset.addRowSetListener(listener);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      assertCondition(listener.isRowSetChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * cursor moves - absolute.
   **/
  public void Var015() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      MyRowListener listener = new MyRowListener();
      rowset.addRowSetListener(listener);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if (jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      rowset.absolute(2);

      assertCondition(listener.isCursorMoved());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * cursor moves - afterLast.
   **/
  public void Var016() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      MyRowListener listener = new MyRowListener();
      rowset.addRowSetListener(listener);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      rowset.afterLast();

      assertCondition(listener.isCursorMoved());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * cursor moves - beforeFirst.
   **/
  public void Var017() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      MyRowListener listener = new MyRowListener();
      rowset.addRowSetListener(listener);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if (jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      rowset.beforeFirst();

      assertCondition(listener.isCursorMoved());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * cursor moves - first.
   **/
  public void Var018() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      MyRowListener listener = new MyRowListener();
      rowset.addRowSetListener(listener);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      rowset.first();

      assertCondition(listener.isCursorMoved());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * cursor moves - last.
   **/
  public void Var019() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      MyRowListener listener = new MyRowListener();
      rowset.addRowSetListener(listener);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if (jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      rowset.last();

      assertCondition(listener.isCursorMoved());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * cursor moves - moveToCurrentRow.
   **/
  public void Var020() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      MyRowListener listener = new MyRowListener();
      rowset.addRowSetListener(listener);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      rowset.moveToCurrentRow();

      assertCondition(listener.isCursorMoved());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * cursor moves - moveToInsertRow.
   **/
  public void Var021() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
      MyRowListener listener = new MyRowListener();
      rowset.addRowSetListener(listener);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT FOR UPDATE");
      // @A8D if (jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      rowset.moveToInsertRow();

      assertCondition(listener.isCursorMoved());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * cursor moves - next.
   **/
  public void Var022() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      MyRowListener listener = new MyRowListener();
      rowset.addRowSetListener(listener);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      rowset.next();

      assertCondition(listener.isCursorMoved());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * cursor moves - previous.
   **/
  public void Var023() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      MyRowListener listener = new MyRowListener();
      rowset.addRowSetListener(listener);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if (jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      rowset.previous();

      assertCondition(listener.isCursorMoved());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * cursor moves - relative.
   **/
  public void Var024() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      MyRowListener listener = new MyRowListener();

      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      rowset.addRowSetListener(listener);
      rowset.relative(2);

      assertCondition(listener.isCursorMoved());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that removeRowSetListener(RowSetListener) behaves as expected.
   **/
  public void Var025() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      MyRowListener listener = new MyRowListener();
      rowset.addRowSetListener(listener);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if(jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      rowset.afterLast();

      boolean before = listener.isCursorMoved();

      listener.reset();

      rowset.removeRowSetListener(listener);

      rowset.first();

      assertCondition(before && !listener.isCursorMoved());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that removeRowSetListener(RowSetListener) behaves as expected.
   **/
  public void Var026() {
    try {
      AS400JDBCRowSet rowset = new AS400JDBCRowSet(jndiName_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      MyRowListener listener = new MyRowListener();
      rowset.addRowSetListener(listener);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT WHERE BALDUE > 100");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      boolean before = listener.isRowSetChanged();

      listener.reset();

      rowset.removeRowSetListener(listener);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT WHERE BALDUE > 105");
      rowset.execute();

      assertCondition(before && !listener.isRowSetChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that next advances to the first record as expected.
   **/
  public void Var027() {
    AS400JDBCRowSet rowset = null;
    // String jndiName = "jdbc/test120";
    // String databaseName = "QCUSTCDT";
    // String serverName = systemObject_.getSystemName();
    // String description = "Customer Database";

    try {
      rowset = new AS400JDBCRowSet();
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setDataSourceName(jndiName_);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if(jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      rowset.next();

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        if (rowset != null)
          rowset.close();
      } catch (Exception e) {

      }
    }
  }

  /**
   * Validates that row set tracing works as expected.
   *
   **/
  public void Var028() {
    StringBuffer sb = new StringBuffer();
    AS400JDBCRowSet rowset = null;
    String command = "SELECT * FROM QIWS.QCUSTCDT";
    try {
      DriverManager.setLogWriter(writer_); // @A3A
      traceDataSource_.setTrace(true);

      rowset = new AS400JDBCRowSet(traceDS_);
      // @A8D if(jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A @A8C

      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);

      rowset.setCommand(command);
      rowset.execute();

      boolean passed = checkLog("as400@setCommand: RowSet", "command = \"" + command + "\".", sb)
          && checkLog("as400: RowSet", "execute().", sb) && checkLog("as400: RowSet", "connect().", sb);
      assertCondition(passed, sb.toString());
    } catch (Exception e) {
      failed(e, "Unexpected exception occurred.");
    } finally {
      traceDataSource_.setTrace(false);
      try {
        if (rowset != null)
          rowset.close();
      } catch (Exception cl) { /* ignore */
      }

      previousFileEnd_ = testFile_.length();
      DriverManager.setLogWriter(previousWriter_); // @A3A
    }
  }

  /**
   * Validates that execute() throws an ExtendedIllegalStateException if the data
   * source name is not found in JNDI.
   **/
  public void Var029() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet("badds");
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        if (rowset != null)
          rowset.close();
      } catch (Exception cl) { /* ignore */
      }
    }
  }

  /**
   * Validates execute() throws an ExtendedIllegalStateException if the
   * dataSourceName is null.
   **/
  public void Var030() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if(jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException n) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        if (rowset != null) {
          rowset.close();
        }
      } catch (Exception cl) { /* ignore */
      }
    }
  }

  /**
   * Validates that addPropertyChangeListener(listener) throws a
   * NullPointerException is the listener is null.
   **/
  public void Var031() {
    AS400JDBCRowSet rowset = null; 
    try {
      rowset = new AS400JDBCRowSet();
      rowset.addPropertyChangeListener(null);

      failed("Unexpected results.");
    } catch (NullPointerException n) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      if (rowset != null) try {
        rowset.close(); 
      } catch (Exception e) { }
    }
  }

  /**
   * Validates that removePropertyChangeListener(listener) throws a
   * NullPointerException is the listener is null.
   **/
  public void Var032() {
    AS400JDBCRowSet rowset = null; 
    try {
      rowset = new AS400JDBCRowSet();
      rowset.removePropertyChangeListener(null);

      failed("Unexpected results.");
    } catch (NullPointerException n) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }finally {
      if (rowset != null) try {
        rowset.close(); 
      } catch (Exception e) { }
    }
  }

  /**
   * Validates that execute() throws an ExtendedIllegalStateException if the url
   * is null.
   **/
  public void Var033() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException n) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        if (rowset != null)
          rowset.close();
      } catch (Exception cl) { /* ignore */
      }
    }
  }

  /**
   * Validates that the url can be used to make a connection.
   **/
  public void Var034() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setUrl("jdbc:as400://" + systemObject_.getSystemName());
      rowset.setUseDataSource(false);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if(jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        if (rowset != null)
          rowset.close();
      } catch (Exception cl) { /* ignore */
      }
    }
  }

  /**
   * Validates that the url with username and password can be used to make a
   * connection.
   **/
  public void Var035() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        if (rowset != null)
          rowset.close();
      } catch (Exception cl) { /* ignore */
      }
    }
  }

  /**
   * Validates that addPropertyChangeListener(listener) notifies the listeners as
   * expected.
   **/
  public void Var036() {
    AS400JDBCRowSet rowset = null; 
    try {
      rowset = new AS400JDBCRowSet();
      MyPropertyListener listener = new MyPropertyListener();
      rowset.addPropertyChangeListener(listener);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");

      assertCondition(listener.isChanged() && listener.getName().equals("command"));
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }finally {
      if (rowset != null) try {
        rowset.close(); 
      } catch (Exception e) { }
    }
  }

  /**
   * Validates that removePropertyChangeListener(listener) works as expected
   **/
  public void Var037() {
    AS400JDBCRowSet rowset = null; 
    try {
      rowset = new AS400JDBCRowSet();
      MyPropertyListener listener = new MyPropertyListener();
      rowset.addPropertyChangeListener(listener);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      boolean before = listener.isChanged();

      listener.reset();

      rowset.removePropertyChangeListener(listener);
      rowset.setCommand("SELECT CUSTID FROM QIWS.QCUSTCDT WHERE CUSTID > 1000");

      assertCondition(
          !listener.isChanged() && rowset.getCommand().equals("SELECT CUSTID FROM QIWS.QCUSTCDT WHERE CUSTID > 1000"),
          "before=" + before);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }finally {
      if (rowset != null) try {
        rowset.close(); 
      } catch (Exception e) { }
    }
  }

  /**
   * Validates that the transaction isolation can be set before the connection is
   * made.
   **/
  public void Var038() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");

      rowset.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
      // @A8D if(jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      assertCondition(rowset.getTransactionIsolation() == Connection.TRANSACTION_SERIALIZABLE);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        if (rowset != null)
          rowset.close();
      } catch (Exception cl) { /* ignore */
      }
    }
  }

  /**
   * Validates that getConcurrency() returns the expected value after an execute
   * is completed.
   **/
  public void Var039() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A

      rowset.execute();

      assertCondition(rowset.getConcurrency() == ResultSet.CONCUR_READ_ONLY);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        if (rowset != null)
          rowset.close();
      } catch (Exception cl) { /* ignore */
      }
    }
  }

  /**
   * Validates that getFetchDirection() returns the expected value from the
   * statement if the result set doesn't exist.
   **/
  public void Var040() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setCommand("CREATE TABLE " + collName_ + ".MYTBLRS (NAME VARCHAR(32), PRICE FLOAT, UNITS INTEGER)");
      // @A8D if(jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C

      rowset.execute();

      assertCondition(rowset.getFetchDirection() == ResultSet.FETCH_FORWARD);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        if (rowset != null) {
          rowset.setCommand("DROP TABLE " + collName_ + ".MYTBLRS");
          rowset.execute();
          rowset.close();
        }
      } catch (Exception cl) { /* ignore */
      }
    }
  }

  /**
   * Validates that getFetchSize() returns the expected value from the statement
   * if the result set doesn't exist.
   **/
  public void Var041() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setCommand("CREATE TABLE " + collName_ + ".MYTBLRS (NAME VARCHAR(32), PRICE FLOAT, UNITS INTEGER)");
        rowset.setContext(context_); // @A2A

      rowset.execute();

      assertCondition(rowset.getFetchSize() == 0);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        if (rowset != null) {
          rowset.setCommand("DROP TABLE " + collName_ + ".MYTBLRS");
          rowset.execute();
          rowset.close();
        }
      } catch (Exception cl) { /* ignore */
      }
    }
  }

  /**
   * Validates that setCommand(String) throws a NullPointerException if the
   * command is null.
   **/
  public void Var042() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setCommand(null);

      failed("Unexpected results.");
    } catch (NullPointerException np) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Validates that setConcurrency(int) throws a ExtendedIllegalArgumentException
   * if the concurrency is invalid.
   **/
  public void Var043() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setConcurrency(-1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalArgumentException np) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Validates that setType(int) works as expected for TYPE_FORWARD_ONLY.
   **/
  public void Var044() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setType(ResultSet.TYPE_FORWARD_ONLY);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if(jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      assertCondition(rowset.getType() == ResultSet.TYPE_FORWARD_ONLY);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Validates that setType(int) throws a ExtendedIllegalArgumentException if the
   * type is invalid.
   **/
  public void Var045() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setType(-1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalArgumentException np) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Validates that setReadOnly(boolean) works as expected after the connection is
   * made.
   **/
  public void Var046() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      rowset.setReadOnly(false);
      assertCondition(rowset.isReadOnly() == false);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Validates that setTypeMap(String) throws a NullPointerException if the type
   * map is null.
   **/
  public void Var047() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setTypeMap(null);

      failed("Unexpected results.");
    } catch (NullPointerException np) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Validates that setTypeMap(String) throws an SQLException.
   **/
  public void Var048() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if(jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      HashMap<?, ?> map = new HashMap<Object, Object>();
      rowset.setTypeMap(map);

      failed("Unexpected results.");
    } catch (SQLException s) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Validates that toString() returns the rowset.
   **/
  public void Var049() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
        rowset.setContext(context_); // @A2A
      rowset.execute();

      assertCondition(rowset.toString() != null);
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Validates that setConcurrency() will trace the property information.
   **/
  public void Var050() {

    AS400JDBCRowSet rowset = null;
    try {
      traceDataSource_.setTrace(true);
      // @A5D if (testFile_.exists())
      // @A5D {
      // @A5D testFile_.delete();
      // @A5D testFile_.createNewFile();
      // @A5D }
      DriverManager.setLogWriter(writer_);

      rowset = new AS400JDBCRowSet(traceDS_);
      // @A8D if(jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C

      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);

      rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowset.execute();
      StringBuffer sb = new StringBuffer();
      // 10/15/2015 added @setConcurrency
      assertCondition(
          checkLog("as400@setConcurrency: RowSet", "concurrency = \"" + ResultSet.CONCUR_UPDATABLE + "\".", sb),
          sb.toString() + " 10/15/2015 added @setConcurrency");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      traceDataSource_.setTrace(false);
      previousFileEnd_ = testFile_.length();
      DriverManager.setLogWriter(previousWriter_); // @A3A
    }
  }

  /**
   * Validates that setType() will trace the property information.
   **/
  public void Var051() {
    // @A9A
    AS400JDBCRowSet rowset = null;
    try {
      traceDataSource_.setTrace(true);
      DriverManager.setLogWriter(writer_);

      rowset = new AS400JDBCRowSet(traceDS_);
        rowset.setContext(context_); // @A2A

      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);

      rowset.setType(ResultSet.TYPE_FORWARD_ONLY);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowset.execute();
      StringBuffer sb = new StringBuffer();
      assertCondition(checkLog("as400@setType: RowSet ", "type = \"" + ResultSet.TYPE_FORWARD_ONLY + "\".", sb),
          sb.toString() + " 10/15/2014 added @setType");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      traceDataSource_.setTrace(false);
      previousFileEnd_ = testFile_.length();
      DriverManager.setLogWriter(previousWriter_); // @A3A
    }
  }

  /**
   * Validates that setUsername(String) throws an ExtendedIllegalStateException if
   * the connection is active.
   **/
  public void Var052() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet(traceDS_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);

      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      // @A8D if(jndiType_ == JNDI_LDAP) //@A2A
      rowset.setEnvironment(env_); // @A2A //@A8C
      rowset.execute();

      rowset.setUsername("myuser");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }finally {
      if (rowset != null) try {
        rowset.close(); 
      } catch (Exception e) { }
    }
  }

  /**
   * Validates that clearParameters() returns if connection is not active.
   **/
  public void Var053() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet(traceDS_);
      rowset.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");

      rowset.clearParameters();

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }finally {
      if (rowset != null) try {
        rowset.close(); 
      } catch (Exception e) { }
    }
  }

  // @A4A
  /**
   * Validates that setContext(Context) throws a NullPointerException if the
   * command is null.
   **/
  public void Var054() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setContext(null);

      failed("Unexpected results.");
    } catch (NullPointerException np) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  // @A8A
  /**
   * Validates that setEnvironment(environment) throws a NullPointerException if
   * the environment is null.
   **/
  public void Var055() {
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), username_, charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setUseDataSource(false);
      rowset.setEnvironment(null);

      failed("Unexpected results.");
    } catch (NullPointerException np) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  public void Var056() {
    StringBuffer sb = new StringBuffer(" -- added 11/14/2016 for CPS AFLIJVT.  Use with AS400ManagedDataSource\n");
    try {

      // Setup the context factory to be used by all classes.
      System.setProperty(Context.INITIAL_CONTEXT_FACTORY, jndiType_);
      Context context = new InitialContext();

      String serverName = systemObject_.getSystemName();
      String description = "Managed Data Source";

      // Create a pooling data source
      sb.append("Creating pooling datasource\n");
      AS400JDBCManagedConnectionPoolDataSource poolingDataSource = new AS400JDBCManagedConnectionPoolDataSource();
      poolingDataSource.setServerName(serverName);
      poolingDataSource.setDataSourceName("datasource1");
      poolingDataSource.setDescription(description);
      sb.append("Register the data source name='" + poolingDataSourceName_ + "'  within JNDI.\n");
      try {
        context.bind(poolingDataSourceName_, poolingDataSource);
      } catch (NamingException n) {
        sb.append(".. Warning: caught NamingException\n");
        printStackTraceToStringBuffer(n, sb);
        context.unbind(poolingDataSourceName_);
        context.bind(poolingDataSourceName_, poolingDataSource);
      }

      // Verify that that datasource is accessible
      try {
        DataSource dataSource = (DataSource) context.lookup(poolingDataSourceName_);
        sb.append("dataSource is " + dataSource.toString() + "\n");
      } catch (Exception tds) {
        tds.printStackTrace();
      }

      // Create a data source to the AS/400 database.
      sb.append("Creating managed datasource\n");
      AS400JDBCManagedDataSource dataSource = new AS400JDBCManagedDataSource();
      dataSource.setServerName(serverName);
      dataSource.setDataSourceName(poolingDataSourceName_);
      dataSource.setDescription(description);
      sb.append("Register the data source  within JNDI.\n");
      try {
        context.bind(managedDataSourceName_, dataSource);
      } catch (NamingException n) {
        context.unbind(managedDataSourceName_);
        context.bind(managedDataSourceName_, dataSource);
      }

      // Verify that that datasource is accessible
      try {
        DataSource getDataSource = (DataSource) context.lookup(managedDataSourceName_);
        sb.append("dataSource is " + getDataSource.toString() + "\n");
      } catch (Exception tds) {
        tds.printStackTrace();
      }

      sb.append("***Testing AS400JDBCRowSet...\n");
      AS400JDBCRowSet rowSet = new AS400JDBCRowSet();
      rowSet.setUsername(username_);
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowSet.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowSet.setDataSourceName(managedDataSourceName_);
      rowSet.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowSet.execute();
      while (rowSet.next()) {
        sb.append("Retrieving " + rowSet.getString(2) + "\n");
      }
      rowSet.close();
      assertCondition(true, sb.toString());

    } catch (Throwable e) {
      failed(e, "Unexpected exception " + sb.toString());
    }
  }

  /**
   * RowSetListener.
   **/
  class MyRowListener implements RowSetListener {
    private boolean cursorMoved_;
    private boolean rowChanged_;
    private boolean rowSetChanged_;

    public MyRowListener() {
      reset();
    }

    public boolean isCursorMoved() {
      return cursorMoved_;
    }

    public boolean isRowChanged() {
      return rowChanged_;
    }

    public boolean isRowSetChanged() {
      return rowSetChanged_;
    }

    public void reset() {
      cursorMoved_ = false;
      rowChanged_ = false;
      rowSetChanged_ = false;
    }

    public void cursorMoved(RowSetEvent event) {
      reset();
      cursorMoved_ = true;
    }

    public void rowChanged(RowSetEvent event) {
      reset();
      rowChanged_ = true;
    }

    public void rowSetChanged(RowSetEvent event) {
      reset();
      rowSetChanged_ = true;
    }
  }

  class MyPropertyListener implements PropertyChangeListener {
    private boolean changed_;
    private String name_1;

    public MyPropertyListener() {
      reset();
    }

    public String getName() {
      return name_1;
    }

    public boolean isChanged() {
      return changed_;
    }

    public void propertyChange(PropertyChangeEvent event) {
      changed_ = true;
      name_1 = event.getPropertyName();
    }

    public void reset() {
      changed_ = false;
      name_1 = "";
    }
  }
}
