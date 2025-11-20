///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCRowSetCtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.AS400JDBC;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.sql.*;

import javax.naming.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;
import com.ibm.as400.access.AS400JDBCRowSet;
import com.ibm.as400.access.ExtendedIllegalStateException;

import test.PasswordVault;
import test.Testcase;

//import javasoft.sqe.tests.ibm.jdbc.JDSetupCollection;    //@A5A
import com.ibm.as400.access.CommandCall; //@A5A

/**
 * Testcase AS400JDBCRowSetTestcase.
 **/
public class AS400JDBCRowSetCtorTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "AS400JDBCRowSetCtorTestcase";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.AS400JDBCRowSetTest.main(newArgs);
  }

  private static final String JNDI_FILE = "com.sun.jndi.fscontext.RefFSContextFactory";
  private static final String JNDI_LDAP = "com.ibm.jndi.LDAPCtxFactory";
  private static final String TEST_FILE_NAME = "/tmp/AS400JDBCRowSetCtorTestcase.file";
  private AS400JDBCDataSource dataSource_;
  private static final String logDirectory_ = "javatest"; // @A3A
  private String dataSourceName_ = logDirectory_ + "/myds"; // @A3C //"/jdbc/myds"
  private String dbaseName_ = collection_ + ".JTBOX"; // "QGPL.JTBOX";
  private Context context_;
  private File testFile_;
  private File javatest_; // @A4A
  private Random random_;
  private Blob blob_;
  private Clob clob_;
  private Ref ref_;
  private Array array_;
  private String jndiType_ = JNDI_FILE;
  private String authorityUsr_ = null; // @A2A
  private String authorityPwd_ = null; // @A2A
  boolean bypassTodo = true;

  /**
   * Constructor. This is called from the AS400JDBCRowSetTestcase constructor.
   **/
  public AS400JDBCRowSetCtorTestcase(AS400 systemObject, Vector<String> variationsToRun, int runMode,
      FileOutputStream fileOutputStream,

      String password, String jndiType, String authorityUsr, // @A2A
      String authorityPwd) // @A2A
  {
    super(systemObject, "AS400JDBCRowSetCtorTestcase", variationsToRun, runMode, fileOutputStream, password);
    if (jndiType == null)
      throw new NullPointerException("-misc parameter is missing.");

    if (jndiType.equals("file"))
      jndiType_ = JNDI_FILE;
    else if (jndiType.equals("ldap"))
      jndiType_ = JNDI_LDAP;
    else
      System.out.println("WARNING... Unknown jndi type '" + jndiType + "' using default.");
    authorityUsr_ = authorityUsr; // @A2A
    authorityPwd_ = authorityPwd; // @A2A
  }

  public void cleanup() {
    try {
      System.gc(); // @A4A Garbage collection so that file can be deleted.

      if (!testFile_.delete()) // @A4C
        System.out.println("WARNING... testcase cleanup could not delete: " + TEST_FILE_NAME); // @A4A
      if (!javatest_.delete()) // @A4A
        System.out.println("WARNING... testcase cleanup could not delete: " + logDirectory_); // @A4A
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      dataSource_ = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
      PasswordVault.clearPassword(charPassword);
      Connection c = dataSource_.getConnection();

      Statement s = c.createStatement();
      s.executeUpdate("DROP TABLE " + dbaseName_);
      s.close();
      c.close();
    } catch (Exception e) {
      System.out.println("AS400JDBCRowSetTestcase cleanup FAILED!");
      e.printStackTrace();
    }
  }

  /**
   * Testcase setup.
   **/
  public void setup() {
    // Make sure javatest dir exists. //@A3A
    javatest_ = new File(logDirectory_); // A4C //@A3A
    if (!javatest_.exists()) // @A3A
    { // @A3A
      System.out.println("Setup is creating '" + logDirectory_ + "' directory."); // @A3A
      // @A3A
      if (!javatest_.mkdir()) // @A3A
      { // @A3A
        System.out.println("WARNING:  Setup could not create the '" + logDirectory_ + "' directory."); // @A3A
      } // @A3A
    }

    // Get the JNDI Initial Context.
    Properties env = new Properties();

    if (jndiType_ == JNDI_LDAP) // @A2A
    { // @A2A
      env.put("java.naming.provider.url", "ldap://" + systemObject_.getSystemName()); // @A2A
      env.put("java.naming.security.principal", "cn=" + authorityUsr_); // @A2A
      env.put("java.naming.security.credentials", authorityPwd_); // @A2A
    } // @A2A
    else {
      env.put(Context.PROVIDER_URL, "file:."); // @pda for linux authority. default is "/" location of file created
      System.setProperty(Context.PROVIDER_URL, "file:."); // @pda for linux authority. default is "/" location of file
                                                          // created
    }

    env.put(Context.INITIAL_CONTEXT_FACTORY, jndiType_);
    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, jndiType_); // @A1A

    try {
      context_ = new InitialContext(env);
    } catch (Exception cxt) {
      System.out.println("ERROR: testcase setup failed to initialize the JNDI context.");
      cxt.printStackTrace();
    }

    random_ = new Random();
    createTestDatabase();

    try {
      testFile_ = new File(TEST_FILE_NAME);
      testFile_.createNewFile();
    } catch (IOException e) {
      System.out.println("Exception creating " + TEST_FILE_NAME);
      e.printStackTrace();
    }
  }

  /**
   * Setup to create a Blob used by the testcases.
   **/
  private void setupCreateBlob() {
    blob_ = new TestBlob("TEST DATA IN BLOB");
  }

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

    public int setBytes(long pos, byte[] bytes) // @B1A
        throws SQLException // @B1A
    { // @B1A
      // add code to test new methods //@B1A
      return 0; // @B1A
    } // @B1A

    public int setBytes(long pos, byte[] bytes, int offest, int len) // @B1A
        throws SQLException // @B1A
    { // @B1A
      // add code to test new methods //@B1A
      return 0; // @B1A
    } // @B1A

    public OutputStream setBinaryStream(long pos) // @B1A
        throws SQLException // @B1A
    { // @B1A
      // add code to test new methods //@B1A
      return null; // @B1A
    } // @B1A

    public void truncate(long len) // @B1A
        throws SQLException // @B1A
    { // @B1A
      // add code to test new methods //@B1A
    } // @B1A

    public void free() throws SQLException {
      // TODO Auto-generated method stub

    }

    public InputStream getBinaryStream(long pos, long length) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

  }

  /**
   * Setup to create a java.sql.Clob object used by the testcases.
   **/
  private void setupCreateClob() {
    clob_ = new TestClob("THIS IS A TEST CLOB WITH SOME MISC INFORMATION.");
  }

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

    public int setString(long pos, String str) // @B1A
        throws SQLException // @B1A
    { // @B1A
      return 0; // @B1A
    } // @B1A

    public int setString(long pos, String str, int offest, int len) // @B1A
        throws SQLException // @B1A
    { // @B1A
      // add code to test new methods //@B1A
      return 0; // @B1A
    } // @B1A

    public OutputStream setAsciiStream(long pos) // @B1A
        throws SQLException // @B1A
    { // @B1A
      // add code to test new methods //@B1A
      return null; // @B1A
    } // @B1A

    public Writer setCharacterStream(long pos) // @B1A
    { // @B1A
      // add code to test new methods //@B1A
      return null; // @B1A
    } // @B1A

    public void truncate(long len) // @B1A
        throws SQLException // @B1A
    { // @B1A
      // add code to test new methods //@B1A
    } // @B1A

    public void free() throws SQLException {
      // TODO Auto-generated method stub

    }

    public Reader getCharacterStream(long pos, long length) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }
  }

  /**
   * Setup to create a java.sql.Ref object used by the testcases.
   **/
  private void setupCreateRef() {
    ref_ = new TestRef();
  }

  class TestRef implements Ref {
    public TestRef() {

    }

    public String getBaseTypeName() {
      return "Test Ref";
    }

    @SuppressWarnings("rawtypes")
    public java.lang.Object getObject(java.util.Map map) // @B1A
        throws SQLException // @B1A
    { // @B1A
      // add code to test new methods //@B1A
      return null; // @B1A
    } // @B1A

    public java.lang.Object getObject() // @B1A
        throws SQLException // @B1A
    { // @B1A
      // add code to test new methods //@B1A
      return null; // @B1A
    } // @B1A

    public void setObject(java.lang.Object value) // @B1A
        throws SQLException // @B1A
    { // @B1A
      // add code to test new methods //@B1A
    } // @B1A
  }

  /**
   * Setup to create an java.sql.Array used by the testcases.
   **/
  private void setupCreateArray() {
    array_ = new TestArray();
  }

  class TestArray implements Array {
    public TestArray() {

    }

    public Object getArray() {
      return null;
    }

    public Object getArray(long pos, int count) {
      return null;
    }

    @SuppressWarnings("rawtypes")
    public Object getArray(long pos, int count, Map map) {
      return null;
    }

    @SuppressWarnings("rawtypes")
    public Object getArray(Map map) {
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

    @SuppressWarnings("rawtypes")
    public ResultSet getResultSet(long pos, int count, Map map) {
      return null;
    }

    @SuppressWarnings("rawtypes")
    public ResultSet getResultSet(Map map) {
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
      dataSource_ = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
      PasswordVault.clearPassword(charPassword);

      if (jndiType_ == JNDI_LDAP) // @A2A
        dataSourceName_ = "cn=test123datasource, cn=users, ou=rochester,o=ibm,c=us"; // @A2A
      dataSource_.setDataSourceName(dataSourceName_);

      try {
        context_.bind(dataSourceName_, dataSource_);
      } catch (Exception be) {
        context_.unbind(dataSourceName_);
        try {
          context_.bind(dataSourceName_, dataSource_);
        } catch (Exception bee) {
          System.out.println("Data source binding error.");
          bee.printStackTrace();
        }
      }
      Connection c = dataSource_.getConnection();
      Statement s = c.createStatement();

      boolean created = false; // @A5A
      try { // @A5A
        s.executeUpdate("CREATE COLLECTION " + collection_); // @A5A
        created = true; // @A5A
      } // @A5A
      catch (SQLException e) { // @A5A
        // The collection already exists. //@A5A
        created = false; // @A5A
      } // @A5A
      // @A5A
      // We need to grant object authority to the //@A5A
      // collection and the collection's journal so that //@A5A
      // other users may create tables in it. //@A5A
      if (created) { // @A5A
        AS400 authoritySys = new AS400(systemObject_.getSystemName(), // @A5A
            authorityUsr_, authorityPwd_.toCharArray()); // @A5A
        CommandCall cmd = new CommandCall(authoritySys); // @A5A
        cmd.run("QSYS/GRTOBJAUT OBJ(QSYS/" + collection_ + ") OBJTYPE(*LIB) " // @A5A
            + "USER(*PUBLIC) AUT(*ALL)"); // @A5A
        cmd.run("QSYS/GRTOBJAUT OBJ(" + collection_ + "/QSQJRN) OBJTYPE(*JRN) " // @A5A
            + "USER(*PUBLIC) AUT(*ALL)"); // @A5A
      } // @A5A

      // @A5Dtry
      // @A5D{
      // @A5D s.executeUpdate("CREATE COLLECTION " + collection_);
      // @A5D}
      // @A5Dcatch (SQLException cc)
      // @A5D{
      // @A5D /* ignore */
      // @A5D}
      s.executeUpdate("CREATE TABLE " + dbaseName_ + " (NAME VARCHAR(10), ID INTEGER, STATE VARCHAR(2), WORTH DOUBLE)");
      s.close();

      PreparedStatement ps = c
          .prepareStatement("INSERT INTO " + dbaseName_ + " (NAME, ID, STATE, WORTH) VALUES (?,?,?,?)");
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
      System.out.println("AS400JDBCRowSetCtorTestcase setup FAILED!"); // @A5C
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
   * Validates that AS400JDBCRowSet takes a JDBC url and can update the database.
   *
   * AS400JDBCRowSet(String) AS400JDBCRowSet.setCommand()
   * AS400JDBCRowSet.setConcurrency() AS400JDBCRowSet.execute()
   * AS400JDBCRowSet.next() AS400JDBCRowSet.getString()
   * AS400JDBCRowSet.getDouble() AS400JDBCRowSet.updateDouble()
   * AS400JDBCRowSet.updateRow()
   **/
  public void Var001() {
    if (bypassTodo) {
      notApplicable("Broken again... todo when time permits.");
      return;
    }
    AS400JDBCRowSet rowset = null;
    try {
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName(), systemObject_.getUserId(),
          charPassword);
      PasswordVault.clearPassword(charPassword);
      if (jndiType_ == JNDI_LDAP) // @A2A
        rowset.setContext(context_); // @A2A

      // Set the command used to populate the list.
      rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
      rowset.setCommand("SELECT * FROM " + dbaseName_ + " FOR UPDATE");

      // Populate the rowset.
      rowset.execute();

      // Update the customer balances.
      while (rowset.next()) {
        double newBalance = rowset.getDouble("WORTH") + getPurchases(rowset.getString("ID"));
        rowset.updateDouble("WORTH", newBalance);
        rowset.updateRow();
      }
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        rowset.close();
      } catch (SQLException sq) {
        sq.printStackTrace();
      }
    }
  }

  /**
   * Validates that the RowSet can take a dataSource and execute a
   * PreparedStatement.
   *
   * AS400JDBCRowSet() AS400JDBCRowSet.setDataSource()
   * AS400JDBCRowSet.setCommand() AS400JDBCRowSet.setString()
   * AS400JDBDRowSet.setDouble() AS400JDBCRowSet.execute()
   **/
  public void Var002() {
    if (bypassTodo) {
      notApplicable("Broken again... todo when time permits.");
      return;
    }
    double MINIMUM_LIMIT = 1000.00;

    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      if (jndiType_ == JNDI_LDAP) // @A2A
        rowset.setContext(context_); // @A2A
      rowset.setDataSourceName(dataSourceName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);

      // Set the prepared statement and initialize the parameters.
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT WHERE STATE = ? AND BALDUE > ?");
      rowset.setString(1, "MN");
      rowset.setDouble(2, MINIMUM_LIMIT);

      // Populate the rowset.
      rowset.execute();

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (SQLException sq) {
        sq.printStackTrace();
      }
    }
  }

  /**
   * Validates that absolute(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var003() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.absolute(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that afterLast() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var004() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.afterLast();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that beforeFirst() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var005() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.beforeFirst();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that cancelRowUpdates() throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var006() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.cancelRowUpdates();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getWarnings() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var007() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getWarnings();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that clearWarnings() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var008() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.clearWarnings();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that close() does not throw an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var009() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.close();

      succeeded(); // @A1C Changed to not expect an exception.
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Validates that deleteRow() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var010() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.deleteRow();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that findColumn(String) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var011() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      if (jndiType_ == JNDI_LDAP) // @A2A
        rowset.setContext(context_); // @A2A
      rowset.findColumn("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that first() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var012() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      if (jndiType_ == JNDI_LDAP) // @A2A
        rowset.setContext(context_); // @A2A
      rowset.first();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getArray(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var013() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getArray(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getArray(String) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var014() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getArray("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getAsciiStream(int) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var015() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getAsciiStream(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getAsciiStream(String) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var016() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getAsciiStream("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getBigDecimal(int) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var017() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getBigDecimal(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getBigDecimal(String) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var018() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getBigDecimal("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getBigDecimal(int,int) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  @SuppressWarnings("deprecation")
  public void Var019() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getBigDecimal(1, 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getBigDecimal(String,int) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  @SuppressWarnings("deprecation")
  public void Var020() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getBigDecimal("A", 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getBinaryStream(int) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var021() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getBinaryStream(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getBinaryStream(String) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var022() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getBinaryStream("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getBlob(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var023() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getBlob(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getBlob(String) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var024() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getBlob("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getBoolean(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var025() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getBoolean(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getBoolean(String) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var026() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getBoolean("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getByte(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var027() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getByte(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getByte(String) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var028() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getByte("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getBytes(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var029() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getBytes(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getBytes(String) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var030() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getBytes("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getCharacterStream(int) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var031() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getCharacterStream(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getCharacterStream(String) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var032() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getCharacterStream("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getClob(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var033() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getClob(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getClob(String) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var034() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getClob("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getCursorName() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var035() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getCursorName();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getDate(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var036() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getDate(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getDate(String) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var037() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getDate("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getDate(int,Calendar) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var038() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getDate(1, Calendar.getInstance());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getDate(String,Calendar) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var039() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getDate("A", Calendar.getInstance());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getDouble(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var040() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getDouble(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getDouble(String) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var041() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getDouble("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getFloat(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var042() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getFloat(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getFloat(String) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var043() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getFloat("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getInt(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var044() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getInt(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getInt(String) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var045() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getInt("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getLong(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var046() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getLong(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getLong(String) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var047() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getLong("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getMetaData() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var048() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getMetaData();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getObject(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var049() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getObject(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getObject(String) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var050() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getObject("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getObject(int,Map) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  @SuppressWarnings("rawtypes")
  public void Var051() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getObject(1, new Hashtable());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      if (rowset != null)
        try {
          rowset.close();
        } catch (SQLException e) {
        }
    }
  }

  /**
   * Validates that getObject(String, Map) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  @SuppressWarnings("rawtypes")
  public void Var052() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getObject("A", new Hashtable());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getRef(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var053() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getRef(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getRef(String) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var054() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getRef("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getRow() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var055() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getRow();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that insertRow() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var056() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.insertRow();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getShort(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var057() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getShort(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getShort(String) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var058() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getShort("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getStatement() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var059() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getStatement();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getString(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var060() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getString(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getString(String) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var061() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getString("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getTime(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var062() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getTime(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getTime(String) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var063() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getTime("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getTime(int,Calendar) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var064() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getTime(1, Calendar.getInstance());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getTime(String,Calendar) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var065() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getTime("A", Calendar.getInstance());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getTimestamp(int) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var066() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getTimestamp(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getTimestamp(String) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var067() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getTimestamp("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getTimestamp(int,Calendar) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var068() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getTimestamp(1, Calendar.getInstance());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getTimestamp(String,Calendar) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var069() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getTimestamp("A", Calendar.getInstance());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getUnicodeStream(int) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  @SuppressWarnings("deprecation")
  public void Var070() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getUnicodeStream(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getUnicodeStream(String) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  @SuppressWarnings("deprecation")
  public void Var071() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getUnicodeStream("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that isAfterLast() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var072() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.isAfterLast();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that isBeforeFirst() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var073() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.isBeforeFirst();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that isFirst() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var074() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.isFirst();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that isLast() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var075() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.isLast();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that last() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var076() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.last();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that moveToCurrentRow() throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var077() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.moveToCurrentRow();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that moveToInsertRow() throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var078() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.moveToInsertRow();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that next() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var079() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.next();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that previous() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var080() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.previous();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that refreshRow() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var081() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.refreshRow();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that relative(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var082() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.relative(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that rowDeleted() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var083() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.rowDeleted();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that rowInserted() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var084() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.rowInserted();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that rowUpdated() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var085() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.rowUpdated();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateAsciiStream(int,InputStream,int) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var086() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateAsciiStream(1, new FileInputStream(testFile_), 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateAsciiStream(String,InputStream,int) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var087() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateAsciiStream("A", new FileInputStream(testFile_), 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateBigDecimal(int,BigDecimal) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var088() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateBigDecimal(1, new BigDecimal("10"));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateBigDecimal(String,BigDecimal) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var089() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateBigDecimal("A", new BigDecimal("10"));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateBinaryStream(int,InputStream,int) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var090() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateBinaryStream(1, new FileInputStream(testFile_), 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateBinaryStream(String,InputStream,int) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var091() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateBinaryStream("A", new FileInputStream(testFile_), 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateBoolean(int,boolean) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var092() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateBoolean(1, true);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateBoolean(String,boolean) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var093() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateBoolean("A", false);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateByte(int,byte) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var094() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateByte(1, (byte) 0x05);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateByte(String,byte) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var095() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateByte("A", (byte) 0x09);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateBytes(int,byte[]) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var096() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      byte[] b = { 4, 5, 6, 7 };
      rowset.updateBytes(1, b);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateBytes(String,byte[]) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var097() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      byte[] b = { 4, 5, 6, 7 };
      rowset.updateBytes("A", b);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateCharacterStream(int,Reader,int) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var098() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateCharacterStream(1, new BufferedReader(new FileReader(testFile_)), 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateCharacterStream(String,Reader,int) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var099() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateCharacterStream("A", new BufferedReader(new FileReader(testFile_)), 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateDate(int,Date) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var100() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateDate(1, new java.sql.Date(1));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateDate(String,Date) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var101() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateDate("A", new java.sql.Date(1));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateDouble(int,double) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var102() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateDouble(1, 10.25);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateDouble(String,double) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var103() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateDouble("A", 10.33);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateFloat(int,float) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var104() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateFloat(1, Float.valueOf((float)10.25).floatValue());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateFloat(String,float) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var105() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateFloat("A", Float.valueOf((float)10.33).floatValue());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateInt(int,int) throws an ExtendedIllegalStateException if
   * the statement has not been executed.
   **/
  public void Var106() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateInt(1, 10);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateInt(String,int) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var107() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateInt("A", 10);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateLong(int,long) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var108() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateLong(1, 10);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateLong(String,long) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var109() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateLong("A", 10);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateNull(int) throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var110() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateNull(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateNull(String,long) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var111() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateNull("A");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateObject(int,Object) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var112() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateObject(1, new String(""));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateObject(String,Object) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var113() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateObject("A", new String(""));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateObject(int,Object,int) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var114() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateObject(1, new String(""), 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateObject(String,Object,int) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var115() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateObject("A", new String(""), 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateRow() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var116() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateRow();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateShort(int,short) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var117() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateShort(1, Short.valueOf("1").shortValue());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateShort(String,short) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var118() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateShort("A", Short.valueOf("1").shortValue());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateString(int,short) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var119() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateString(1, "test");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateString(String,short) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var120() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateString("A", "test");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateTime(int,Time) throws an ExtendedIllegalStateException
   * if the statement has not been executed.
   **/
  public void Var121() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateTime(1, new Time(1));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateTime(String,Time) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var122() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateTime("A", new Time(1));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateTimestamp(int,Timestamp) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var123() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateTimestamp(1, new Timestamp(1));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that updateTimestamp(String,Timestamp) throws an
   * ExtendedIllegalStateException if the statement has not been executed.
   **/
  public void Var124() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.updateTimestamp("A", new Timestamp(1));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that wasNull() throws an ExtendedIllegalStateException if the
   * statement has not been executed.
   **/
  public void Var125() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.wasNull();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setArray(int,Array) throws an ExtendedIllegalStateException if
   * the statement has not been created.
   **/
  public void Var126() {
    if (array_ == null)
      setupCreateArray();

    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setArray(1, array_);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setAsciiStream(int,InputStream,int) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var127() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setAsciiStream(1, new FileInputStream(testFile_), 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setBigDecimal(int,BigDecimal) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var128() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setBigDecimal(1, new BigDecimal("10"));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setBinaryStream(int,InputStream,int) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var129() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setBinaryStream(1, new FileInputStream(testFile_), 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setBlob(int,Blob) throws an ExtendedIllegalStateException if
   * the statement has not been created.
   **/
  public void Var130() {
    if (blob_ == null)
      setupCreateBlob();

    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setBlob(1, blob_);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setBoolean(int,boolean) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var131() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setBoolean(1, false);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setByte(int,byte) throws an ExtendedIllegalStateException if
   * the statement has not been created.
   **/
  public void Var132() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setByte(1, (byte) 0x32);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setBytes(int,byte[]) throws an ExtendedIllegalStateException
   * if the statement has not been created.
   **/
  public void Var133() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      byte[] bytes = { (byte) 0x01, (byte) 0x02, (byte) 0x03 };
      rowset.setBytes(1, bytes);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setCharacterStream(int,Reader,int) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var134() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setCharacterStream(1, new BufferedReader(new FileReader(testFile_)), 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setClob(int,Clob) throws an ExtendedIllegalStateException if
   * the statement has not been created.
   **/
  public void Var135() {
    if (clob_ == null)
      setupCreateClob();

    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setClob(1, clob_);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setDate(int,Date) throws an ExtendedIllegalStateException if
   * the statement has not been created.
   **/
  public void Var136() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setDate(1, new java.sql.Date(1));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setDate(int,Date,Calendar) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var137() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setDate(1, new java.sql.Date(1), Calendar.getInstance());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setDouble(int,double) throws an ExtendedIllegalStateException
   * if the statement has not been created.
   **/
  public void Var138() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setDouble(1, 10.25);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setEscapeProcessing(boolean) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var139() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setEscapeProcessing(true);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setFetchDirection(int) throws an ExtendedIllegalStateException
   * if the statement has not been created.
   **/
  public void Var140() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setFetchDirection(ResultSet.FETCH_FORWARD);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setFetchSize(int) throws an ExtendedIllegalStateException if
   * the statement has not been created.
   **/
  public void Var141() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setFetchSize(1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setFloat(int,float) throws an ExtendedIllegalStateException if
   * the statement has not been created.
   **/
  public void Var142() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setFloat(1, Double.valueOf(10.25).floatValue());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setInt(int,int) throws an ExtendedIllegalStateException if the
   * statement has not been created.
   **/
  public void Var143() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setInt(1, 11);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setLong(int,Long) throws an ExtendedIllegalStateException if
   * the statement has not been created.
   **/
  public void Var144() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setLong(1, Long.valueOf("10000").longValue());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setMaxFieldSize(int) throws an ExtendedIllegalStateException
   * if the statement has not been created.
   **/
  public void Var145() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setMaxFieldSize(100);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setMaxRows(int) throws an ExtendedIllegalStateException if the
   * statement has not been created.
   **/
  public void Var146() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setMaxRows(100);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setNull(int,int) throws an ExtendedIllegalStateException if
   * the statement has not been created.
   **/
  public void Var147() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setNull(1, Types.INTEGER);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setNull(int,int,String) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var148() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setNull(1, Types.INTEGER, "java.lang.Integer");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setObject(int,Object) throws an ExtendedIllegalStateException
   * if the statement has not been created.
   **/
  public void Var149() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setObject(1, new String("test"));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setObject(int,Object,int) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var150() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setObject(1, new String("test"), Types.CLOB);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setObject(int,Object,int,int) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var151() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setObject(1, new String("test"), Types.CLOB, 1);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setQueryTimeout(int) throws an ExtendedIllegalStateException
   * if the statement has not been created.
   **/
  public void Var152() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setQueryTimeout(100);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setRef(int,Ref) throws an ExtendedIllegalStateException if the
   * statement has not been created.
   **/
  public void Var153() {
    if (ref_ == null)
      setupCreateRef();

    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setRef(1, ref_);

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setShort(int,short) throws an ExtendedIllegalStateException if
   * the statement has not been created.
   **/
  public void Var154() {
    if (ref_ == null)
      setupCreateRef();

    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setShort(1, Short.valueOf("1").shortValue());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setString(int,String) throws an ExtendedIllegalStateException
   * if the statement has not been created.
   **/
  public void Var155() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setString(1, "test");

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setTime(int,Time) throws an ExtendedIllegalStateException if
   * the statement has not been created.
   **/
  public void Var156() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setTime(1, new Time(1));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setTime(int,Time,Calendar) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var157() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setTime(1, new Time(1), Calendar.getInstance());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setTimestamp(int,Timestamp) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var158() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setTimestamp(1, new Timestamp(1));

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that setTimestamp(int,Timestamp,Calendar) throws an
   * ExtendedIllegalStateException if the statement has not been created.
   **/
  public void Var159() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.setTimestamp(1, new Timestamp(1), Calendar.getInstance());

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that execute() throws an ExtendedIllegalStateException if the
   * statement has not been created.
   **/
  public void Var160() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.execute();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getCommand() return null if the command parameter is not set.
   **/
  public void Var161() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getCommand() == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getConcurrency() initially returns ResultSet.CONCUR_READ_ONLY.
   **/
  public void Var162() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getConcurrency() == ResultSet.CONCUR_READ_ONLY);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getDataSourceName() return an empty String if the data source
   * parameter is not set.
   **/
  public void Var163() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getDataSourceName() == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getEscapeProcessing() initially returns true.
   **/
  public void Var164() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getEscapeProcessing() == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getFetchDirection() initially returns ResultSet.FETCH_FORWARD.
   **/
  public void Var165() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getFetchDirection() == ResultSet.FETCH_FORWARD);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getFetchSize() initially returns zero.
   **/
  public void Var166() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getFetchSize() == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getMaxFieldSize() initially returns zero.
   **/
  public void Var167() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getMaxFieldSize() == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getMaxRows() initially returns zero.
   **/
  public void Var168() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getMaxRows() == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getPassword() initially returns an empty String.
   **/
  public void Var169() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getPassword().equals(""));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getQueryTimeout() throws an ExtendedIllegalStateException if
   * the statement has not been created.
   **/
  public void Var170() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      rowset.getQueryTimeout();

      failed("Unexpected results.");
    } catch (ExtendedIllegalStateException is) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getTransactionIsolation() initially returns
   * Connection.TRANSACTION_READ_UNCOMMITTED.
   **/
  public void Var171() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getType() initially returns ResultSet.TYPE_FORWARD_ONLY.
   **/
  public void Var172() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getType() == ResultSet.TYPE_FORWARD_ONLY);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getTypeMap() initially returns null.
   **/
  public void Var173() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getTypeMap() == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getUrl() initially returns null.
   **/
  public void Var174() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getUrl() == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that getUsername() initially returns null.
   **/
  public void Var175() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.getUsername() == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that isReadOnly() initially returns false.
   **/
  public void Var176() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.isReadOnly() == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that isUseDataSource() initially returns true.
   **/
  public void Var177() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet();
      assertCondition(rowset.isUseDataSource() == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that AS400JDBCRowSet(AS400JDBCDataSource) throws a
   * NullPointerException if the data source property is null.
   **/
  public void Var178() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet(null);

      failed("Unexpected results.");
    } catch (NullPointerException np) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that AS400JDBCRowSet(AS400JDBCDataSource) sets the data source.
   * property as expected.
   **/
  public void Var179() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet(dataSourceName_);

      assertCondition(rowset.getDataSourceName().equals(dataSourceName_));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that isUseDataSource() returns true if the data source is set on
   * the constructor.
   **/
  public void Var180() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet(dataSourceName_);

      assertCondition(rowset.isUseDataSource() == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that AS400JDBCRowSet(String,String,String) throws a
   * NullPointerException if the url parameter is null.
   **/
  public void Var181() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet(null, "user", "pwd");

      failed("Unexpected Results.");
    } catch (NullPointerException np) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that AS400JDBCRowSet(String,String,String) throws a
   * NullPointerException if the username parameter is null.
   **/
  public void Var182() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet("jdbc:as400:", null, "pwd");

      failed("Unexpected Results.");
    } catch (NullPointerException np) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that AS400JDBCRowSet(String,String,String) throws a
   * NullPointerException if the password parameter is null.
   **/
  public void Var183() {
    AS400JDBCRowSet rowset = null;
    try {
      rowset = new AS400JDBCRowSet("jdbc:as400:", "user", (char[]) null);

      failed("Unexpected Results.");
    } catch (NullPointerException np) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that AS400JDBCRowSet(String,String,String) sets the url and
   * username as expected.
   **/
  public void Var184() {
    AS400JDBCRowSet rowset = null;
    try {
      String url = "jdbc:as400:";
      String username = "user";
      rowset = new AS400JDBCRowSet(url, username, "pwd");

      assertCondition(rowset.getUsername().equalsIgnoreCase(username) && rowset.getUrl().equals(url));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Validates that isUseDataSource() returns false if the constuctor
   * AS400JDBCRowSet(String,String,String) is used.
   **/
  public void Var185() {
    AS400JDBCRowSet rowset = null;
    try {
      String url = "jdbc:as400:";
      String username = "user";
      rowset = new AS400JDBCRowSet(url, username, "pwd");

      assertCondition(rowset.isUseDataSource() == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        rowset.close();
      } catch (Exception e) {
      }
    }
  }
}
