///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRowSetRSTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.RS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;
import com.ibm.as400.access.AS400JDBCRowSet;

import test.JDRSTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;
import test.JD.JDSerializeFile;
import test.JD.DataSource.JDDatabaseOverride;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
// import java.security.interfaces.DSAKey;
import java.sql.*;
import javax.sql.*;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

//native classes for Rowsets & DataSources
import javax.sql.DataSource;
import javax.sql.RowSet;
import java.util.Map;

/**
 * Testcase JDRowSetRSTestcase.
 **/
public class JDRowSetRSTestcase extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "JDRowSetRSTestcase";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.JDRSTest.main(newArgs);
  }

  private String jndiName_ = "jdbc/customer";
  static final String key2_ = "JDRowSetRSTestcase2";
  private static String select_ = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;
  private static final byte[] eleven = { (byte) 'E', (byte) 'l', (byte) 'e', (byte) 'v', (byte) 'e', (byte) 'n',
      (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
      (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ' };
  private static final byte[] twelve = { (byte) 'T', (byte) 'w', (byte) 'e', (byte) 'l', (byte) 'v', (byte) 'e' };

  AS400JDBCRowSet updateRowset_;
  AS400JDBCRowSet urlRowset_;
  AS400JDBCDataSource dataSource_;

  private Connection conn_; // @a1
  private Statement stmt_; // used by the native driver for do some of the table setup stuff.
  private Connection conn2_; // @pda for creatign Nclobs
  StringBuffer sb = new StringBuffer();

  /**
   * Constructor.
   **/
  public JDRowSetRSTestcase(AS400 systemObject, Hashtable<String, Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,

      String password, String pwrUserId, String pwrPassword) {
    super(systemObject, "JDRowSetRSTestcase", namesAndVars, runMode, fileOutputStream, password, pwrUserId,
        pwrPassword);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {
    super.setup();
    DataSource ds; // @A1 switched to generic object types instead of driver specific ones.

    // Make sure this is set after JDRSTest.RSTEST_UPDATE has been updated
    select_ = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;

    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {

      char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
      ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), passwordChars);
      PasswordVault.clearPassword(passwordChars);
      System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory"); // @C1A

      // Set the root to a writable directory on AIX and Linux
      if (JTOpenTestEnvironment.isOS400 || JTOpenTestEnvironment.isWindows || JTOpenTestEnvironment.isLinux
          || JTOpenTestEnvironment.isAIX) {

        System.setProperty("java.naming.provider.url", "file:///tmp");

        java.io.File file = new java.io.File("/tmp/jdbc");
        try {
          file.mkdir();
        } catch (Exception e) {
          e.printStackTrace();
        }

      } else {
        output_.println("Unexpected osVersion=" + JTOpenTestEnvironment.osVersion);
      }

      Context context = new InitialContext();
      try {
        output_.println("binding " + jndiName_ + " with " + ds);
        // output_.println("Reference for ds is
        // "+((AS400JDBCDataSource)ds).getReference());
        // Note: If this blows up with a Null Pointer Exception, this refers to the
        // following bug as documented (valid 5/2/2007) at
        // http://archives.java.sun.com/cgi-bin/wa?A2=ind0003&L=jndi-interest&D=0&P=3248
        //
        // To fix this you need the fscontext.jar from fscontext-1_2-beta3
        //
        try {
          context.bind(jndiName_, ds);
        } catch (NullPointerException e) {
          e.printStackTrace();
          output_.println(" Note:  If this blows up with a Null Pointer Exception, this refers to the");
          output_.println("following bug as documented (valid 5/2/2007) at ");
          output_.println("http://archives.java.sun.com/cgi-bin/wa?A2=ind0003&L=jndi-interest&D=0&P=3248 ");

          output_.println("To fix this you need the fscontext.jar from fscontext-1_2-beta3");
        }

      } catch (NamingException n) {
        context.unbind(jndiName_);
        context.bind(jndiName_, ds);
      }

    } else {
      System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
      jndiName_ = "jdbc";
      ds = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBDataSource");

      JDReflectionUtil.callMethod_V(ds, "setDatabaseName", systemObject_.getSystemName());
      JDReflectionUtil.callMethod_V(ds, "setUser", systemObject_.getUserId());
      JDReflectionUtil.callMethod_V(ds, "setPassword", PasswordVault.decryptPasswordLeak(encryptedPassword_));

      Context context = new InitialContext();
      try {
        context.rebind(jndiName_, ds);
      } catch (Exception e) {
        cleanupBindings(pwrSysUserID_, pwrSysEncryptedPassword_);
        context.rebind(jndiName_, ds);
      }

      //
      // Must connect to the specified system.. Needed for IASP to
      // work
      //
      conn_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
      stmt_ = conn_.createStatement();
    }

    conn2_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_, "JDRowSetRSTestcase");

    Statement s2 = conn2_.createStatement();
    s2.execute("delete from " + JDRSTest.RSTEST_UPDATE); // make sure table is empty
    s2.close();
  }

  void cleanUpdateTable() throws Exception {
    stmt_.executeUpdate("DELETE FrOM "+JDRSTest.RSTEST_UPDATE );
  }
  
  RowSet updater() throws Exception {
    RowSet update;
    cleanUpdateTable();
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      update = new AS400JDBCRowSet(jndiName_);
      update.setUsername(systemObject_.getUserId());

      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      ((AS400JDBCRowSet) update).setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      update.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      update.setConcurrency(ResultSet.CONCUR_UPDATABLE);
      update.setCommand("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('DUMMY_ROW')");
      update.execute();
      return update;
    } else { // @A1 The native driver's rowset can not execute a command that does not return
             // a result set.
             // so we use a statement to do the setup stuff.
      update = (RowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2JdbcRowSet");
      update.setDataSourceName(jndiName_);
      update.setUsername(systemObject_.getUserId());
      update.setPassword(PasswordVault.decryptPasswordLeak(encryptedPassword_));
      update.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      update.setConcurrency(ResultSet.CONCUR_UPDATABLE);
      stmt_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('DUMMY_ROW')");
      return update;
    }
  }

  private RowSet updater(String key) throws Exception {
    RowSet rowset;// @A1
    AS400JDBCRowSet rowset2;
    cleanUpdateTable();
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      rowset2 = new AS400JDBCRowSet(jndiName_);
      rowset = rowset2;
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      rowset2.setPassword(charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
      rowset.setCommand("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('DUMMY_ROW')");
      rowset.execute();
      rowset.setCommand("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('" + key + "')");
      rowset.execute();
    } else// @A1
    {
      rowset = (RowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2JdbcRowSet");
      rowset.setDataSourceName(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      String password = "passwordLeak.JDRowSetRSTestcase";
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      password = new String(charPassword);
      rowset.setPassword(password);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
      stmt_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('DUMMY_ROW')");
      stmt_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('" + key + "')");
    }

    rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
    rowset.execute();

    return rowset;

  }

  private RowSet validater() throws Exception {
    RowSet validater;// @A1
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      validater = new AS400JDBCRowSet(jndiName_);
    } else {
      validater = (RowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2JdbcRowSet");

      validater.setDataSourceName(jndiName_);
    }
    validater.setUsername(systemObject_.getUserId());
    validater.setPassword(PasswordVault.decryptPasswordLeak(encryptedPassword_));
    validater.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
    validater.setConcurrency(ResultSet.CONCUR_READ_ONLY);
    validater.setCommand("SELECT * FROM " + JDRSTest.RSTEST_UPDATE);
    validater.execute();
    return validater;
  }

  // @A1
  private RowSet getRowSet(String s) throws Exception {
    RowSet rowset = null;
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      rowset = new AS400JDBCRowSet(s);
    } else {
      rowset = (RowSet) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2JdbcRowSet");

      rowset.setDataSourceName(s);
    }
    return rowset;
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      stmt_.close();
      conn_.close();
    }
  }

  /**
   * Positions the cursor on a particular row and return the row set.
   * 
   * @param rs  The row set.
   * @param key The key. If null, positions to before the first row.
   * 
   * @exception SQLException If an exception occurs.
   **/
  public void position(RowSet rs, String key) throws SQLException {
    rs.beforeFirst();
    position0(rs, key);
  }

  /**
   * Positions the cursor on a particular row and return the row set.
   * 
   * @param rs  The row set.
   * @param key The key. If null, positions to before the first row.
   * 
   * @exception SQLException If an exception occurs.
   **/
  public void position0(RowSet rs, String key) throws SQLException {
    if (key != null) {
      while (rs.next()) {
        String s = rs.getString("C_KEY");
        if (s != null) {
          if (s.equals(key))
            return;
        }
      }
      output_.println("Warning: Key " + key + " not found.");
    }
  }

  /**
   * Forces a single warning to be posted to the statement.
   * 
   * @param rs The RowSet object.
   * 
   * @exception Exception If an exception occurs.
   **/
  @SuppressWarnings("deprecation")
  public void forceWarning(RowSet rs) throws Exception { // @A1
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      position(rs, "NUMBER_POS"); // @C1A
      // This should force the warning "data truncation".
      ((AS400JDBCRowSet) rs).getBigDecimal("C_NUMERIC_105", 0); // @C1A
      // ((AS400JDBCRowSet)rs).updateString ("VALUE", "Lots more than 25
      // characters."); //@C1D
    } else {
      rs.updateString("VALUE", "Lots more than 25 characters.");
    }
  }

  /**
   * Compares a Blob with a byte[].
   **/
  private boolean compareBlobToByteArray(Blob i, byte[] b) throws SQLException {
    byte[] iBytes = i.getBytes(1, (int) i.length()); // @B1C
    return areEqual(iBytes, b);
  }

  // takes NClob and string as args
  private boolean compareNClobToString(Object i, String b) throws SQLException {
    try {
      // same as compareClobToString, but first checks to be sure it is of type NClob
      // (not just Clob)
      if (i.getClass().getName().indexOf("NClob") == -1)
        return false;

      return compareClobToString((Clob) i, b);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Compares a Clob with a String.
   * 
   * @exception SQLException If an exception occurs.
   **/
  private boolean compareClobToString(Clob i, String b) throws SQLException {
    String s = i.getSubString(1, (int) i.length()); // @B1C
    return s.equals(b);
  }

  protected static boolean compareBeginsWithBytes(InputStream i, byte[] b) // @K2
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
      if (num == -1)
        return b.length == 0;
      int total = num;
      while (num > -1 && total < buf.length) {
        num = i.read(buf, total, buf.length - total);
        total += num;
      }
      if (num == -1)
        --total;
      return total == b.length && Testcase.areEqual(b, buf);
    } catch (java.io.IOException e) {
      return false;
    }
  }

  public boolean compareColumns(int i, Object[] before, Object[] after, boolean[] beforeNulls, boolean[] afterNulls,
      StringBuffer message) {
    boolean success = true;
    if (before[i] == null) {
      if (after[i] != null) {
        message.append("For column " + i + " before = " + before[i] + " after = " + after[i] + "\n");
        success = false;
      }
    } else {
      if (!before[i].equals(after[i])) {
        if (before[i] instanceof byte[]) {
          byte[] beforeBytes = (byte[]) before[i];
          byte[] afterBytes = (byte[]) after[i];
          if (afterBytes != null) {
            if (afterBytes.length == beforeBytes.length) {
              boolean ok = true;
              for (int j = 0; ok && j < beforeBytes.length; j++) {
                if (beforeBytes[j] != afterBytes[j]) {
                  ok = false;
                  ;
                }
              }
              if (!ok) {
                message.append("\n");
                message.append("beforeBytes: ");
                for (int j = 0; j < beforeBytes.length; j++) {
                  message.append(Integer.toHexString(0xFF & (int) beforeBytes[j]));
                }
                message.append("\n");
                message.append("afterBytes:  ");
                for (int j = 0; j < afterBytes.length; j++) {
                  message.append(Integer.toHexString(0xFF & (int) afterBytes[j]));
                }
                message.append("\n");

                message.append("For column " + i + " before = " + before[i] + " after = " + after[i] + "\n");
                success = false;
              }
            } else {
              message.append("For column " + i + " before = " + before[i] + " l=" + beforeBytes.length + " after = "
                  + after[i] + " l= " + afterBytes.length + "\n");
              success = false;
            }
          } else {
            message.append("For byte array column " + i + " before = " + before[i] + " after = " + after[i] + "\n");
            success = false;
          }
        } else {
          message.append("For column " + i + " before = " + before[i] + " class(" + before[i].getClass().getName()
              + ") after = " + after[i] + "\n");
          success = false;
        }
      }
    }

    if (beforeNulls[i] != afterNulls[i]) {
      message.append("For column " + i + " beforeNulls = " + beforeNulls[i] + " afterNulls = " + afterNulls[i] + "\n");
      success = false;
    }

    return success;
  }

  /**
   * updateRow() - Should update exactly 1 column when updates are pending.
   **/
  public void Var001() {
    StringBuffer message = new StringBuffer();
    String key = "JDRowSetRSTestcase";
    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key);
        rowset.setCommand(select_ + " FOR UPDATE");
        rowset.execute();
        position(rowset, key);

        int columnCount = rowset.getMetaData().getColumnCount();
        Object[] before = new Object[columnCount];
        boolean[] beforeNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          before[i] = rowset.getObject(i + 1);
          beforeNulls[i] = rowset.wasNull();
        }

        rowset.updateString("C_VARCHAR_50", "Hi Mom!");
        rowset.updateRow();
        rowset.close();

        RowSet validate = validater();
        position(validate, key);
        Object[] after = new Object[columnCount];
        boolean[] afterNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          after[i] = validate.getObject(i + 1);
          afterNulls[i] = validate.wasNull();
        }
        int updatedColumn = validate.findColumn("C_VARCHAR_50") - 1;

        validate.close();

        boolean success = true;
        for (int i = 0; i < columnCount; ++i) {
          if (i == updatedColumn) {
            if ((!after[i].equals("Hi Mom!")) || (afterNulls[i] != false))
              success = false;
          } else {
            success = compareColumns(i, before, after, beforeNulls, afterNulls, message) && success;
          }
        }

        assertCondition(success, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * cancelRowUpdates() - Should have no effect when no updates are pending.
   **/
  public void Var002() {
    StringBuffer message = new StringBuffer();
    String key = "JDRowSetRSTestcase";
    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key);
        position(rowset, key);

        int columnCount = rowset.getMetaData().getColumnCount();
        Object[] before = new Object[columnCount];
        boolean[] beforeNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          before[i] = rowset.getObject(i + 1);
          beforeNulls[i] = rowset.wasNull();
        }

        rowset.cancelRowUpdates();
        rowset.close();

        RowSet validate = validater();
        position(validate, key);

        Object[] after = new Object[columnCount];
        boolean[] afterNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          after[i] = validate.getObject(i + 1);
          afterNulls[i] = validate.wasNull();
        }
        validate.close();

        boolean success = true;
        for (int i = 0; i < columnCount; ++i) {
          success = compareColumns(i, before, after, beforeNulls, afterNulls, message) && success;
        }

        assertCondition(success, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * cancelRowUpdates() - Should clear any pending updates.
   **/
  public void Var003() {
    StringBuffer message = new StringBuffer();
    String key = "JDRowSetRSTestcase";
    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key);
        position(rowset, key);

        int columnCount = rowset.getMetaData().getColumnCount();
        Object[] before = new Object[columnCount];
        boolean[] beforeNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          before[i] = rowset.getObject(i + 1);
          beforeNulls[i] = rowset.wasNull();
        }

        rowset.updateInt("C_INTEGER", 9836);
        rowset.updateNull("C_SMALLINT");
        rowset.updateFloat("C_FLOAT", -9.25f);
        rowset.cancelRowUpdates();
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validate = validater();
        position(validate, key);
        Object[] after = new Object[columnCount];
        boolean[] afterNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          after[i] = validate.getObject(i + 1);
          afterNulls[i] = validate.wasNull();
        }
        validate.close();

        boolean success = true;
        for (int i = 0; i < columnCount; ++i) {
          success = compareColumns(i, before, after, beforeNulls, afterNulls, message) && success;
        }

        assertCondition(success, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * clearWarnings() - Clears warnings after 1 has been posted.
   **/
  public void Var004() {
    String name = "jdbc/var004";
    RowSet rowset = null;
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      notApplicable("Native directory not set up correctly for this test");
      return;
    }

    if (checkJdbc20()) {
      String systemName = systemObject_.getSystemName().trim();
      String databaseName = "UNSET";
      try {
        databaseName = JDDatabaseOverride.getDatabaseNameFromSystemName(systemName, output_);
        DataSource ds;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {

          ds = new AS400JDBCDataSource(systemName, systemObject_.getUserId(), encryptedPassword_);
          ((AS400JDBCDataSource) ds).setDatabaseName(databaseName);
          ((AS400JDBCDataSource) ds).setDataTruncation(true);
        } else {
          name = "jdbc";
          ds = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBDataSource");

          JDReflectionUtil.callMethod_V(ds, "setDatabaseName", name);
          JDReflectionUtil.callMethod_V(ds, "setUser", systemObject_.getUserId());
          JDReflectionUtil.callMethod_V(ds, "setPassword", PasswordVault.decryptPasswordLeak(encryptedPassword_));
          JDReflectionUtil.callMethod_V(ds, "setDataTruncation", true);
        }
        Context context = new InitialContext();
        try {
          context.bind(name, ds);
        } catch (NamingException n) {
          context.unbind(name);
          context.bind(name, ds);
        }

        rowset = getRowSet(name);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
          rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        else
          rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_POS + " FOR UPDATE");

        rowset.execute();
        if (rowset.next()) {
          // force a warning...
          forceWarning(rowset); // @C1A
          // rowset.updateString ("VALUE", "Here is a lot more than 25 characters.");
          // //@C1D

          SQLWarning w1 = rowset.getWarnings();
          rowset.clearWarnings();

          SQLWarning w2 = rowset.getWarnings();
          assertCondition((w1 != null) && (w2 == null), "systemName=" + systemName + " databaseName=" + databaseName);
        } else
          failed("Unexpected Results.systemName=" + systemName + " databaseName=" + databaseName);
      } catch (Exception e) {
        failed(e, "Unexpected Exception systemName=" + systemName + " databaseName=" + databaseName);
      } finally {
        
      }
    }
  }

  /**
   * getWarning() - Returns the first warning when 1 warning has been reported.
   **/
  public void Var005() {
    String name = "jdbc/var005";
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      notApplicable("Native directory not set up correctly for this test");
      return;
    }
    if (checkJdbc20()) {
      try {
        DataSource dataSource;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {

          String systemName = systemObject_.getSystemName();
          String databaseName = JDDatabaseOverride.getDatabaseNameFromSystemName(systemName, output_);
          dataSource = new AS400JDBCDataSource(systemName, systemObject_.getUserId(), encryptedPassword_);
          ((AS400JDBCDataSource) dataSource).setDatabaseName(databaseName);
          ((AS400JDBCDataSource) dataSource).setDataTruncation(true);
        } else {
          name = "jdbc";

          dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBDataSource");

          JDReflectionUtil.callMethod_V(dataSource, "setDatabaseName", name);
          JDReflectionUtil.callMethod_V(dataSource, "setUser", systemObject_.getUserId());
          JDReflectionUtil.callMethod_V(dataSource, "setPassword",
              PasswordVault.decryptPasswordLeak(encryptedPassword_));
          JDReflectionUtil.callMethod_V(dataSource, "setDataTruncation", true);

        }

        Context context = new InitialContext();
        try {
          context.bind(name, dataSource);
        } catch (NamingException n) {
          context.unbind(name);
          context.bind(name, dataSource);
        }

        RowSet rowset = getRowSet(name);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) // @C1A
          rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET); // @C1A
        else // @C1A
          rowset.setCommand("SELECT ID, VALUE FROM " + JDRSTest.RSTEST_POS + " FOR UPDATE");
        rowset.setMaxFieldSize(25);

        rowset.execute();
        rowset.next();
        rowset.clearWarnings();

        forceWarning(rowset);
        SQLWarning w1 = rowset.getWarnings();

        if (w1 == null) {
          rowset.close();
          failed("Testcase setup failed.");
        } else {
          SQLWarning w2 = w1.getNextWarning();
          rowset.close();
          assertCondition((w1 != null) && (w2 == null));
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * Validates that close() works as expected.
   **/
  public void Var006() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowset.execute();

      if (rowset.next()) {
        rowset.close();

        rowset.next();
      }
      failed("Unexpected Results.");
      rowset.close();
    } catch (SQLException sq) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that deleteRow works as expected.
   **/
  public void Var007() {
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      // notApplicable("This testcase fails for some and not for others. Get back to
      // it when we have free time.");
      // return;
      // Note: Paul can never get this to fail...not sure why others see it fail...
    }
    sb.setLength(0);
    String key = "AA.JDRowSet";
    
    try {
      
      stmt_.executeUpdate("DELETE FROM " + JDRSTest.RSTEST_UPDATE + " WHERE C_KEY LIKE '" + key + "%'");
      RowSet rowset = updater(key);
      for (int i = 1; i < 198; ++i) { // @A1
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          rowset.setCommand("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('" + key + i + "')");
          rowset.execute();
        } else {
          stmt_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('" + key + i + "')");
        }
      }

      rowset.setCommand(select_ + " ORDER BY C_KEY FOR UPDATE");
      rowset.execute();

      rowset.beforeFirst();
      int rowCountBefore = 0;
      Vector<String> keysBefore = new Vector<String>();
      while (rowset.next()) {
        ++rowCountBefore;
        keysBefore.addElement(rowset.getString("C_KEY"));
      }

      rowset.first();
      rowset.deleteRow();
      rowset.close();

      // Verify results.
      RowSet rowset2 = getRowSet(jndiName_);
      rowset2.setUsername(systemObject_.getUserId());
      rowset2.setPassword(PasswordVault.decryptPasswordLeak(encryptedPassword_));
      rowset2.setCommand(select_ + " ORDER BY C_KEY");
      rowset2.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
      rowset2.setConcurrency(ResultSet.CONCUR_READ_ONLY);
      rowset2.execute();

      int rowCountAfter = 0;
      Vector<String> keysAfter = new Vector<String>();
      while (rowset2.next()) {
        ++rowCountAfter;
        keysAfter.addElement(rowset2.getString("C_KEY"));
      }
      rowset2.close();

      boolean success = true;
      for (int i = 0; i < rowCountAfter; ++i) { // @K1C changed from rowCountBefore to rowCountAfter
        // if (i < deletedRow) //Should never be here, i starts as 0 @K1D
        // if (! keysBefore.elementAt (i).equals (keysAfter.elementAt (i))) //@K1D
        // success = false; //@K1D
        // } //@K1D
        // else if (i > deletedRow) { //Should always be here //@K1D
        try // put the try catch in to count for entries with that are null, this occurs if
            // you run //@K1A
        { // the entire ResultSet testcases. //@K1A
          if (!keysBefore.elementAt(i + 1).equals(keysAfter.elementAt(i))) {
            success = false;
            sb.append("\nfor i=" + i + " keysBefore.elementAt (i+1)='" + keysBefore.elementAt(i + 1)
                + "' keysAfter.elementAt(i)=' " + keysAfter.elementAt(i) + "'");
          }
        } // @K1A
        catch (Exception e) // @K1A
        { // @K1A
          if (e instanceof NullPointerException) // @K1A
          { // @K1A
            if (!((keysBefore.elementAt(i + 1) == null) && (keysAfter.elementAt(i) == null))) { // @K1A
              success = false; // @K1A
              sb.append("\nfor i=" + i + " keysBefore.elementAt (i+1)='" + keysBefore.elementAt(i + 1)
                  + "' keysAfter.elementAt(i)=' " + keysAfter.elementAt(i) + "'");

            }
          } // @K1A
        } // @K1A
        // } @K1D
      }

      assertCondition(success && (rowCountAfter == (rowCountBefore - 1)),
          sb.toString() + "\nsuccess = " + success + " rowCountAfter=" + rowCountAfter + " sb " + (rowCountBefore - 1));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      
    }
  }

  /**
   * Validates that findColumn(String) works as expected.
   **/
  public void Var008() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT CUSNUM,LSTNAM,INIT FROM QIWS.QCUSTCDT");
      rowset.execute();

      int i = rowset.findColumn("INIT");
      rowset.close();
      assertCondition(i == 3);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates first() works as expected.
   **/
  public void Var009() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_POS);
      rowset.execute();

      boolean success = rowset.first();
      int id1 = rowset.getInt(1);
      rowset.close();
      assertCondition((success == true) && (id1 == 1));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getArray() - Get from a INTEGER.
   **/
  public void Var010() {
    RowSet rowset = null;

    if (checkJdbc20()) {
      try {
        rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "NUMBER_POS");
        Array v = rowset.getArray("C_INTEGER");
        failed("Didn't throw SQLException but got " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } finally {
        
      }
    }
  }

  /**
   * getAsciiStream() - Should work when the column index is valid.
   **/
  public void Var011() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "CHAR_FULL");
      InputStream v = rowset.getAsciiStream(12);
      sb.setLength(0);
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          true) // @K2
        assertCondition(
            compareBeginsWithBytes(v, "Toolbox for Java                                  ".getBytes("8859_1"), sb), sb); // @K2
      else // @K2
        assertCondition(compare(v, "Toolbox for Java                                  ", "8859_1", sb), sb);
      v.close();
      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getAsciiStream() - Should work when the column name is valid.
   **/
  public void Var012() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "CHAR_FULL");
      InputStream v = rowset.getAsciiStream("C_CHAR_50");
      sb.setLength(0);
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          true) // @K2
        assertCondition(
            compareBeginsWithBytes(v, "Toolbox for Java                                  ".getBytes("8859_1"), sb), sb); // @K2
      else // @K2
        assertCondition(compare(v, "Toolbox for Java                                  ", "8859_1", sb), sb);
      v.close();
      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBinaryStream() - Should work when the column index is valid.
   **/
  public void Var013() {
    sb.setLength(0);
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "BINARY_NOTRANS");
      InputStream v = rowset.getBinaryStream(18);
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          true) // @K2
        assertCondition(compareBeginsWithBytes(v, twelve)); // @K2
      else // @K2
        assertCondition(compare(v, twelve, sb), sb);
      v.close();
      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBinaryStream() - Should work when the column name is valid.
   **/
  public void Var014() {
    sb.setLength(0);

    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "BINARY_NOTRANS");
      InputStream v = rowset.getBinaryStream("C_BINARY_20");
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          true) // @K2
        assertCondition(compareBeginsWithBytes(v, eleven)); // @K2
      else // @K2
        assertCondition(compare(v, eleven, sb), sb);
      v.close();
      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBigDecimal() - Should work when the column index is valid.
   **/
  public void Var015() {
    if (checkJdbc20()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "NUMBER_POS");
        BigDecimal v = rowset.getBigDecimal(8);
        assertCondition(v.doubleValue() == 8.8888);

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBigDecimal() - Should work when the column name is valid.
   **/
  public void Var016() {
    if (checkJdbc20()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "NUMBER_POS");
        BigDecimal v = rowset.getBigDecimal("C_NUMERIC_105");
        assertCondition(v.doubleValue() == 10.10105);

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBigDecimal() with 2 parameters - Should work when the column index is
   * valid.
   **/
  @SuppressWarnings("deprecation")
  public void Var017() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "NUMBER_POS");
      BigDecimal v = rowset.getBigDecimal(8, 3);
      assertCondition(v.doubleValue() == 8.889);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBoolean() - Should work when the column index is valid.
   **/
  public void Var018() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "NUMBER_POS");
      boolean v = rowset.getBoolean(2);
      assertCondition(v == true);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBoolean() - Should work when the column name is valid.
   **/
  public void Var019() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "NUMBER_POS");
      boolean v = rowset.getBoolean("C_INTEGER");
      assertCondition(v == true);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Should work when the column index is valid.
   **/
  public void Var020() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "BINARY_NOTRANS");
      byte[] v = rowset.getBytes(18);
      assertCondition(areEqual(v, twelve));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Should work when the column name is valid.
   **/
  public void Var021() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "BINARY_NOTRANS");
      byte[] v = rowset.getBytes("C_BINARY_20");
      assertCondition(areEqual(v, eleven));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getByte() - Should work when the column index is valid.
   **/
  public void Var022() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      // Changed 3/6/2007 -- a value of 198 doesn't fit in a
      // signed byte, so we retrieve from different row.
      // position0 (rowset, "NUMBER_POS");
      // byte v = rowset.getByte (2);
      // assertCondition (v == (byte) 198);

      position0(rowset, "NUMBER_0");
      byte v = rowset.getByte(2);
      assertCondition(v == (byte) 0, "got " + (int) v + " sb 0");

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getByte() - Should work when the column name is valid.
   **/
  public void Var023() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "NUMBER_0");
      byte v = rowset.getByte("C_SMALLINT");
      assertCondition(v == (byte) 0);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBlob() - Should work when the column index is valid.
   **/
  public void Var024() {
    if (checkJdbc20()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "BINARY_NOTRANS");
        Blob v = rowset.getBlob(18);
        assertCondition(compareBlobToByteArray(v, twelve));

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBlob() - Should work when the column name is valid.
   **/
  public void Var025() {
    if (checkJdbc20()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "BINARY_NOTRANS");
        Blob v = rowset.getBlob("C_BINARY_20");
        assertCondition(compareBlobToByteArray(v, eleven));

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Should work when the column index is valid.
   **/
  public void Var026() {
    if (checkJdbc20()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "CHAR_FULL");
        Reader v = rowset.getCharacterStream(12);
        sb.setLength(0);
        assertCondition(compare(v, "Toolbox for Java                                  ", sb), sb);
        v.close();
        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Should work when the column name is valid.
   **/
  public void Var027() {
    if (checkJdbc20()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "CHAR_FULL");
        Reader v = rowset.getCharacterStream("C_CHAR_50");
        sb.setLength(0);
        assertCondition(compare(v, "Toolbox for Java                                  ", sb), sb);
        v.close();
        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Should work when the column index is valid.
   **/
  public void Var028() {
    if (checkJdbc20()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "CHAR_FULL");
        Clob v = rowset.getClob(14);
        assertCondition(compareClobToString(v, "Java Toolbox"));

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Should work when the column name is valid.
   **/
  public void Var029() {
    if (checkJdbc20()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "CHAR_FULL");
        Clob v = rowset.getClob("C_VARCHAR_50");
        assertCondition(compareClobToString(v, "Java Toolbox"));

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getDate() - Should work when the column index is valid.
   **/
  public void Var030() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "DATE_2000");
      Date v = rowset.getDate(19);
      assertCondition(v.toString().equals("2000-02-21"));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDate() - Should work when the column name is valid.
   **/
  public void Var031() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "DATE_1998");
      Date v = rowset.getDate("C_DATE");
      assertCondition(v.toString().equals("1998-04-08"));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDouble() - Should work when the column index is valid.
   **/
  public void Var032() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "NUMBER_POS");
      double v = rowset.getDouble(6);
      assertCondition(v == 6.666);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getDouble() - Should work when the column name is valid.
   **/
  public void Var033() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      JDRSTest.position0(rowset, "NUMBER_POS");
      double v = rowset.getDouble("C_INTEGER");
      assertCondition(v == 98765);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getFloat() - Should work when the column index is valid.
   **/
  public void Var034() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      JDRSTest.position0(rowset, "NUMBER_POS");
      float v = rowset.getFloat(5);
      assertCondition(v == 5.55f);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getFloat() - Should work when the column name is valid.
   **/
  public void Var035() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "NUMBER_POS");
      float v = rowset.getFloat("C_INTEGER");
      assertCondition(v == 98765);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getInt() - Should work when the column index is valid.
   **/
  public void Var036() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "NUMBER_POS");
      int v = rowset.getInt(3);
      assertCondition(v == 98765);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getInt() - Should work when the column name is valid.
   **/
  public void Var037() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "NUMBER_POS");
      int v = rowset.getInt("C_INTEGER");
      assertCondition(v == 98765);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getLong() - Should work when the column index is valid.
   **/
  public void Var038() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "NUMBER_POS");
      long v = rowset.getLong(3);
      assertCondition(v == 98765);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getLong() - Should work when the column name is valid.
   **/
  public void Var039() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "NUMBER_POS");
      long v = rowset.getLong("C_INTEGER");
      assertCondition(v == 98765);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getObject() - Should work when the column index is valid.
   **/
  public void Var040() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "DATE_1998");
      Object s = rowset.getObject(1);
      assertCondition(s.equals("DATE_1998"));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getObject() - Should work when the column name is valid.
   **/
  public void Var041() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "DATE_1998");
      Object s = rowset.getObject("C_KEY");
      assertCondition(s.equals("DATE_1998"));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getRef() - Should throw an exception when the column index is valid.
   **/
  public void Var042() {
    RowSet rowset = null;
    if (checkJdbc20()) {
      try {
        rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "NUMBER_POS");
        Ref v = rowset.getRef(3);
        failed("Didn't throw SQLException but got " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } finally {
        
      }
    }
  }

  /**
   * getRef() - Should throw an exception when the column name is valid.
   **/
  public void Var043() {
    RowSet rowset = null;
    if (checkJdbc20()) {
      try {
        rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "NUMBER_POS");
        Ref v = rowset.getRef("C_INTEGER");
        failed("Didn't throw SQLException but got " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
      try {
        rowset.close();
      } catch (SQLException sq) {
        /* ignore */ }
    }
  }

  /**
   * getRow() - Should return 0 on an empty result set.
   **/
  public void Var044() {
    if (checkJdbc20()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        int row = rowset.getRow();
        rowset.close();
        assertCondition(row == 0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getRow() - Should work on a 1 row result set.
   **/
  public void Var045() {
    if (checkJdbc20()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_POS + " WHERE ID = 1");
        rowset.execute();

        boolean success = rowset.first();
        int row = rowset.getRow();
        rowset.close();
        assertCondition(row == 1, "row=" + row + " sb success=" + success);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getShort() - Should work when the column index is valid.
   **/
  public void Var046() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "NUMBER_POS");
      short v = rowset.getShort(2);
      assertCondition(v == 198);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getShort() - Should work when the column name is valid.
   **/
  public void Var047() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "NUMBER_POS");
      short v = rowset.getShort("C_SMALLINT");
      assertCondition(v == 198);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * last() - Should return false on an empty result set.
   **/
  public void Var048() {
    if (checkJdbc20()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_POS + " WHERE ID = -1");
        rowset.execute();

        boolean success = rowset.last();
        rowset.close();
        assertCondition(success == false);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * last() - Should work on a large result set.
   **/
  public void Var049() {
    if (checkJdbc20()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_POS);
        rowset.execute();

        boolean success = rowset.last();
        int id1 = rowset.getInt(1);
        rowset.close();
        assertCondition((success == true) && (id1 == 99));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * insertRow() - Inserted row should be reflected in the result set being
   * updated when not repositioned.
   **/
  public void Var050() {
    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
        rowset.execute();

        JDRSTest.position(rowset, "JDRowSetInsertRow");
        int columnCount = rowset.getMetaData().getColumnCount();

        rowset.moveToInsertRow();
        rowset.updateString("C_VARCHAR_50", "Space people");
        rowset.insertRow();

        assertCondition(rowset.getString("C_VARCHAR_50").equals("Space people"),
            "Did not get expected string column count=" + columnCount);
        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * getUnicodeStream() - Should work when the column index is valid.
   **/
  public void Var051() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "CHAR_FULL");
      Reader v = rowset.getCharacterStream(12);
      sb.setLength(0);
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          true) // @K2
        assertCondition(compare(v, "Toolbox for Java                                  ", sb), sb); // @K2
      else // @K2
        assertCondition(compare(v, "Toolbox for Java                                  ", sb), sb); // @B0C
      v.close();
      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUnicodeStream() - Should work when the column name is valid.
   **/
  public void Var052() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "CHAR_FULL");
      Reader v = rowset.getCharacterStream("C_CHAR_50");
      sb.setLength(0);
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          true) // @K2
        assertCondition(compare(v, "Toolbox for Java                                  ", sb), sb); // @K2
      else // @K2
        assertCondition(compare(v, "Toolbox for Java                                  ", sb), sb); // @B0C
      v.close();
      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTimestamp() - Should work when the column index is valid.
   **/
  public void Var053() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "DATE_2000");
      Timestamp v = rowset.getTimestamp(21);
      assertCondition(v.toString().equals("2000-06-25 10:30:12.345676"));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTimestamp() - Should work when the column name is valid.
   **/
  public void Var054() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "DATE_1998");
      Timestamp v = rowset.getTimestamp("C_TIMESTAMP");
      assertCondition(v.toString().equals("1998-11-18 03:13:42.987654"));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTime() - Should work when the column index is valid.
   **/
  public void Var055() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "DATE_2000");
      Time v = rowset.getTime(20);
      assertCondition(v.toString().equals("14:04:55"));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTime() - Should work when the column name is valid.
   **/
  public void Var056() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "DATE_1998");
      Time v = rowset.getTime("C_TIME");
      assertCondition(v.toString().equals("08:14:03"));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getString() - Should work when the column index is valid.
   **/
  public void Var057() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "DATE_1998");
      String s = rowset.getString(1);
      assertCondition(s.equals("DATE_1998"));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getString() - Should work when the column name is valid.
   **/
  public void Var058() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      position0(rowset, "DATE_1998");
      String s = rowset.getString("C_KEY");
      assertCondition(s.equals("DATE_1998"));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getCommand returns as expected.
   **/
  public void Var059() {
    String command = "SELECT * FROM QIWS.QCUSTCDT";
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand(command);
      String command2 = rowset.getCommand();
      rowset.close();
      assertCondition(command2.equals(command));

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getConcurrency returns as expected before a result set is
   * created.
   **/
  public void Var060() {
    int concurrency = ResultSet.CONCUR_UPDATABLE;
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setConcurrency(concurrency);
      int resultConcurrency = rowset.getConcurrency();
      rowset.close();
      assertCondition(resultConcurrency == concurrency);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getConcurrency returns as expected.
   **/
  public void Var061() {
    int concurrency = ResultSet.CONCUR_UPDATABLE;
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setConcurrency(concurrency);
      int resultCurrency = rowset.getConcurrency();
      rowset.close();
      assertCondition(resultCurrency == concurrency);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getCursorName returns as expected.
   **/
  public void Var062() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowset.execute();

      assertCondition(rowset.getCursorName() != null);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getDataSourceName returns as expected.
   **/
  public void Var063() {
    try {
      RowSet rowset; // @C1A
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) // @C1A
        rowset = new AS400JDBCRowSet(); // @C1A
      else // @C1A
        rowset = getRowSet(null); // @C1M
      rowset.setDataSourceName(jndiName_);
      String datasourceName = rowset.getDataSourceName();
      rowset.close();
      assertCondition(datasourceName.equals(jndiName_));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getEscapeProcessing returns false as expected.
   **/
  public void Var064() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowset.setEscapeProcessing(false);
      boolean resultEscapeProcessing = rowset.getEscapeProcessing();
      rowset.close();
      assertCondition(resultEscapeProcessing == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getFetchDirection returns as expected.
   **/
  public void Var065() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowset.execute();

      rowset.setFetchDirection(ResultSet.FETCH_REVERSE);
      assertCondition(rowset.getFetchDirection() == ResultSet.FETCH_REVERSE);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getFetchDirection returns as expected.
   **/
  public void Var066() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      int fetchDirection = rowset.getFetchDirection();
      rowset.close();
      assertCondition(fetchDirection == ResultSet.FETCH_FORWARD);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getFetchDirection returns as expected.
   **/
  public void Var067() {
    RowSet rowset = null;
    try {
      rowset = getRowSet(jndiName_);
      int fetchDirection = rowset.getFetchDirection();
      rowset.close();
      assertCondition(fetchDirection == ResultSet.FETCH_FORWARD);
    } catch (Exception e) {
      if (rowset != null)
        try {
          rowset.close();
        } catch (SQLException e1) {
        }
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getFetchSize returns as expected.
   **/
  public void Var068() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowset.execute();

      int size = 64;
      rowset.setFetchSize(size);

      assertCondition(rowset.getFetchSize() == size);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getFetchSize returns as expected.
   **/
  public void Var069() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      int fetchSize = rowset.getFetchSize();
      rowset.close();
      assertCondition(fetchSize == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getFetchSize returns as expected if a statement has been
   * created.
   **/
  public void Var070() {
    RowSet rowset = null;
    try {
      rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      // @A1
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        rowset.setCommand("CREATE TABLE QGPL.TESTTBLE (I INTEGER)");
        rowset.execute();
      } else {
        stmt_.executeUpdate("CREATE TABLE QGPL.TESTTBLE (I INTEGER)");
      }

      assertCondition(rowset.getFetchSize() == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        // @A1
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          rowset.setCommand("DROP TABLE QGPL.TESTTBLE");
          rowset.execute();
        } else {
          stmt_.executeUpdate("DROP TABLE QGPL.TESTTBLE");
        }
      } catch (Exception e) {
        /* ignore errors. */
      }
      try {
        rowset.close();
      } catch (Exception f) {
      }
    }
  }

  /**
   * Validates that getMaxFieldSize returns as expected.
   **/
  public void Var071() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      int size = rowset.getMaxFieldSize();
      rowset.close();
      assertCondition(size == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getMaxFieldSize() returns as expected.
   **/
  public void Var072() {
    RowSet rowset = null;
    try {
      rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");

      int size = 25;
      rowset.setMaxFieldSize(size);

      assertCondition(rowset.getMaxFieldSize() == size);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      
    }
  }

  /**
   * Validates that getMaxRows() returns as expected.
   **/
  public void Var073() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      int maxrows = rowset.getMaxRows();
      rowset.close();
      assertCondition(maxrows == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getMaxRows() returns as expected.
   **/
  public void Var074() {
    RowSet rowset = null;
    try {
      rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");

      int size = 25;
      rowset.setMaxRows(size);

      assertCondition(rowset.getMaxRows() == size);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      
    }
  }

  /**
   * Validates that getMetaData() returns as expected.
   **/
  public void Var075() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rowset.execute();

      assertCondition(rowset.getMetaData() != null);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that getPassword() returns an empty string.
   **/
  public void Var076() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setPassword("MYPWD");
      String password = rowset.getPassword();
      rowset.close();
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        assertCondition(password.equals(""));
      } else {
        assertCondition(password.equals("MYPWD"));
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Validates that getQueryTimeout() returns as expected.
   **/
  public void Var077() {
    int expected = 1000;
    RowSet rowset = null;
    try {
      rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowset.setQueryTimeout(expected);

      assertCondition(rowset.getQueryTimeout() == expected);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      
    }
  }

  /**
   * Validates that getStatement() returns as expected.
   **/
  public void Var078() {
    RowSet rowset = null;
    try {
      rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowset.execute();
      Statement statement = rowset.getStatement();
      statement.close();
      assertCondition(statement != null);
    } catch (Exception e) {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        assertCondition(e.getMessage().indexOf("NOT SUPPORTED") >= 0);
      } else
        failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that getTransactionIsolation() returns as expected.
   **/
  public void Var079() {
    try {
      int isolation = Connection.TRANSACTION_READ_COMMITTED;
      RowSet rowset = getRowSet(jndiName_);
      rowset.setTransactionIsolation(isolation);
      int outIsolation = rowset.getTransactionIsolation();
      rowset.close();
      assertCondition(outIsolation == isolation);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that getTransactionIsolation() returns as expected.
   **/
  public void Var080() {
    
    try (RowSet rowset = getRowSet(jndiName_);){
      
      
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      // @A1
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        rowset.setCommand("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('test')");
        rowset.execute();
      } else {
        stmt_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('test')");
      }
      int isolation = Connection.TRANSACTION_REPEATABLE_READ;
      rowset.setTransactionIsolation(isolation);

      assertCondition(rowset.getTransactionIsolation() == isolation);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that getType() returns as expected.
   **/
  public void Var081() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);

      int type = ResultSet.TYPE_SCROLL_INSENSITIVE;
      rowset.setType(type);

      rowset.execute();
      int outType = rowset.getType();
      rowset.close();
      assertCondition(outType == type);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that getType() returns as expected.
   **/
  public void Var082() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      int type = ResultSet.TYPE_SCROLL_SENSITIVE;
      rowset.setType(type);
      int outType = rowset.getType();
      rowset.close();
      assertCondition(outType == type);
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that getTypeMap() returns the expected value.
   **/
  public void Var083() {
    RowSet rowset = null;
    try {
      rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT");
      rowset.setMaxRows(25);

      Map<String, Class<?>> m = rowset.getTypeMap();
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        assertCondition(m == null);
        return;
      }
      failed("No SQLException thrown");
    } catch (SQLException sq) {
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      if (rowset != null) {
        try {
          rowset.close();
        } catch (SQLException e) {
        }
      }
    }
  }

  /**
   * Validates that getUrl() returns the expected value.
   **/
  public void Var084() {
    String url = "jdbc:as400:";
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUrl(url);
      String outUrl = rowset.getUrl();
      rowset.close();
      assertCondition(outUrl.equals(url));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that getUsername() returns the expected value.
   **/
  public void Var085() {
    String name = "MYUSER";
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(name);
      String outUsername = rowset.getUsername();
      rowset.close();
      assertCondition(outUsername.equals(name));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }
  }

  /**
   * Validates that rowDeleted() - Should return true on the deleted row when
   * called immediately without repositioning.
   **/
  public void Var086() {
    String key = "JDRowSetRowDeleted";
    
    try {
      
      RowSet rowset = updater(key);
      rowset.next();
      rowset.deleteRow();

      assertCondition(rowset.rowDeleted() == true);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      
    }

  }

  /**
   * Validates that rowInserted() - Should return false on all rows in the updated
   * result set after repositioning.
   **/
  public void Var087() {
    String key = "JDRowSetRowInserted";
    
    try {
      
      RowSet rowset = updater(key);
      rowset.moveToInsertRow();
      rowset.updateInt("C_INTEGER", 68290);
      rowset.updateNull("C_SMALLINT");
      rowset.updateString("C_CHAR_1", ")");
      rowset.insertRow();

      rowset.beforeFirst();
      boolean success = true;
      while (rowset.next()) {
        if (rowset.rowInserted() != false)
          success = false;
      }
      assertCondition(success);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      
    }
  }

  /**
   * Validates that rowUpdated() - Should return false on all rows in the updated
   * result set after repositioning.
   **/
  public void Var088() {
    String key = "JDRowSetRowUpdated";
    
    try {
      
      RowSet rowset = updater(key);
      rowset.next();
      rowset.updateInt("C_INTEGER", 98276);
      rowset.updateNull("C_SMALLINT");
      rowset.updateFloat("C_FLOAT", -4.225f);
      rowset.updateRow();

      rowset.beforeFirst();
      boolean success = true;
      while (rowset.next()) {
        if (rowset.rowUpdated() != false)
          success = false;
      }
      assertCondition(success);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      
    }
  }

  /**
   * Validates that refreshRow() - Should work on a 1 row result set.
   **/
  public void Var089() {
    RowSet rowset = null;
    try {
      rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_POS + " WHERE ID = 1");
      rowset.execute();

      rowset.next();
      rowset.refreshRow();
      int id = rowset.getInt("ID");
      boolean success = rowset.next();

      assertCondition((success == false) && (id == 1));

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      
    }

  }

  /**
   * Validates that moveToCurrentRow() - Should return to the current row after a
   * moveToInsertRow(), when the current row is positioned using first().
   **/
  public void Var090() {
    String key = "JDRowSetMoveToCurrentRow";
    
    try {
      
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
      rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
      rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_POS + " FOR UPDATE");
      rowset.execute();

      rowset.first();
      rowset.moveToInsertRow();
      rowset.moveToCurrentRow();

      int id = rowset.getInt(1);
      rowset.close();

      assertCondition(id == 1);
    } catch (Exception e) {
      failed(e, "Unexpected Exception for key=" + key);
    } finally {
      
    }
  }

  /**
   * Returns an input stream containing the ASCII bytes for a string.
   **/
  private InputStream stringToAsciiStream(String s) throws UnsupportedEncodingException {
    return new ByteArrayInputStream(s.getBytes("ISO8859_1"));
  }

  /**
   * Validate that updateAsciiStream() - Should work when the column index is
   * valid.
   **/
  public void Var091() {
    String key_ = "JDRowSUpdAsciiStream";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);
        position(rowset, key_);
        InputStream asciiStream = stringToAsciiStream("Dublin");
        rowset.updateAsciiStream(14, asciiStream, 6);
        asciiStream.close();
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();

        assertCondition(v.equals("Dublin"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * Validates that updateAsciiStream() - Should work when the column name is
   * valid.
   **/
  public void Var092() {
    String key_ = "JDRowSUpdAsciiStream";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);
        // Update
        position(rowset, key_);
        InputStream asciiStream = stringToAsciiStream("Ibiza92");
        rowset.updateAsciiStream("C_VARCHAR_50", asciiStream, 7);
        rowset.updateRow();
        asciiStream.close();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Ibiza92"), "Got " + v + " sb Ibiza92");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * Compares a BigDecimal with a double, and allows a little rounding error.
   **/
  private boolean compareBigDecimal(BigDecimal bd, double d) {
    return (Math.abs(bd.doubleValue() - d) < 0.001);
  }

  /**
   * updateBigDecimal() - Should work when the column index is valid.
   **/
  public void Var093() {
    String key_ = "JDRowSUpdBigDecimal";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        // Update.
        position(rowset, key_);
        rowset.updateBigDecimal(8, new BigDecimal("1.22"));
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        BigDecimal v = validater.getBigDecimal("C_DECIMAL_105");
        validater.close();

        assertCondition(compareBigDecimal(v, 1.22));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateBigDecimal() - Should work when the column name is valid.
   **/
  public void Var094() {
    String key_ = "JDRowSUpdBigDecimal";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        // Update.
        position(rowset, "JDRowSUpdBigDecimal");
        rowset.updateBigDecimal("C_NUMERIC_105", new BigDecimal("-5.3322"));
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, "JDRowSUpdBigDecimal");
        BigDecimal v = validater.getBigDecimal("C_NUMERIC_105");
        validater.close();

        assertCondition(v.doubleValue() == -5.3322);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * Validates that updateBinaryStream() - Should work when the column index is
   * valid.
   **/
  public void Var095() {
    String key_ = "JDRowSUpdBinaryStream";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        // Update.
        position(rowset, key_);
        byte[] ba = new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 };
        rowset.updateBinaryStream(18, new ByteArrayInputStream(ba), ba.length);
        rowset.updateRow();
        rowset.close();

        // Validate
        RowSet validater = validater();
        position(validater, key_);
        byte[] v = validater.getBytes("C_VARBINARY_20");
        validater.close();

        assertCondition(areEqual(v, ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * Validates that updateBinaryStream() - Should work when the column name is
   * valid.
   **/
  public void Var096() {
    String key_ = "JDRowSUpdBinaryStream";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        // Update.
        position(rowset, key_);
        byte[] ba = new byte[] { (byte) -4, (byte) 98, (byte) 99 };
        rowset.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), 3);
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        byte[] v = validater.getBytes("C_VARBINARY_20");
        validater.close();

        assertCondition(areEqual(v, ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateBoolean() - Should work when the column index is valid.
   **/
  public void Var097() {
    String key_ = "JDRowSUpdBoolean";
    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateBoolean(2, true);
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        boolean v = validater.getBoolean("C_SMALLINT");
        validater.close();

        assertCondition(v == true);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateBoolean() - Should work when the column name is valid.
   **/
  public void Var098() {
    String key_ = "JDRowSUpdBoolean";
    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateBoolean("C_SMALLINT", false);
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        boolean v = validater.getBoolean("C_SMALLINT");
        validater.close();
        assertCondition(v == false);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateByte() - Should work when the column index is valid.
   **/
  public void Var099() {
    String key_ = "JDRowSUpdByte";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateByte(2, (byte) 45);
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        byte v = validater.getByte("C_SMALLINT");
        validater.close();

        assertCondition(v == 45);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateByte() - Should work when the column name is valid.
   **/
  public void Var100() {
    String key_ = "JDRowSUpdByte";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateByte("C_SMALLINT", (byte) -59);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);

        byte v = validater.getByte("C_SMALLINT");
        validater.close();
        assertCondition(v == -59);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateBytes() - Should work when the column index is valid.
   **/
  public void Var101() {
    String key_ = "JDRowSUpdBytes";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        byte[] ba = new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 };
        rowset.updateBytes(18, ba);
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);

        byte[] v = validater.getBytes("C_VARBINARY_20");
        validater.close();
        assertCondition(areEqual(v, ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateBytes() - Should work when the column name is valid.
   **/
  public void Var102() {
    String key_ = "JDRowSUpdBytes";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        byte[] ba = new byte[] { (byte) -4, (byte) 98, (byte) 99 };
        rowset.updateBytes("C_VARBINARY_20", ba);
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);

        byte[] v = validater.getBytes("C_VARBINARY_20");
        validater.close();
        assertCondition(areEqual(v, ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateCharacterStream() - Should work when the column index is valid.
   **/
  public void Var103() {
    String key_ = "JDRowSUpdCharacterStream";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateCharacterStream(14, new StringReader("Fargo"), 5);
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);

        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Fargo"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateCharacterStream() - Should work when the column name is valid.
   **/
  public void Var104() {
    String key_ = "JDRowSUpdCharacterStream";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateCharacterStream("C_VARCHAR_50", new StringReader("Gillette"), 8);
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);

        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Gillette"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateDouble() - Should work when the column index is valid.
   **/
  public void Var105() {
    String key_ = "JDRowSUpdDouble";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateDouble(6, 545);
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);

        double v = validater.getDouble("C_DOUBLE");
        validater.close();
        assertCondition(v == 545);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateDouble() - Should work when the column name is valid.
   **/
  public void Var106() {
    String key_ = "JDRowSUpdDouble";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateDouble("C_DOUBLE", -599);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);

        double v = validater.getDouble("C_DOUBLE");
        validater.close();
        assertCondition(v == -599);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateDate() - Should work when the column index is valid.
   **/
  public void Var107() {
    String key_ = "JDRowSUpdDate";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        Date d = new Date(1922400000);
        rowset.updateDate(19, d);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);

        Date v = validater.getDate("C_DATE");
        validater.close();
        // SQL400 - changed to compare only the date.
        assertCondition(v.toString().equals(d.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateDate() - Should work when the column name is valid.
   **/
  public void Var108() {
    String key_ = "JDRowSUpdDate";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        Date d = new Date(108000000);
        rowset.updateDate("C_DATE", d);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);

        Date v = validater.getDate("C_DATE");
        validater.close();
        // SQL400 - changed to compare only the dates.
        assertCondition(v.toString().equals(d.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateInt() - Should work when the column index is valid.
   **/
  public void Var109() {
    String key_ = "JDRowSUpdInt";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateInt(3, 545);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);

        int v = validater.getInt("C_INTEGER");
        validater.close();
        assertCondition(v == 545);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateInt() - Should work when the column name is valid.
   **/
  public void Var110() {
    String key_ = "JDRowSUpdInt";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateInt("C_INTEGER", -599);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);

        int v = validater.getInt("C_INTEGER");
        validater.close();
        assertCondition(v == -599);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateFloat() - Should work when the column index is valid.
   **/
  public void Var111() {
    String key_ = "JDRowSUpdFloat";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateFloat(5, 545);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);

        float v = validater.getFloat("C_FLOAT");
        validater.close();
        assertCondition(v == 545);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateFloat() - Should work when the column name is valid.
   **/
  public void Var112() {
    String key_ = "JDRowSUpdFloat";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateFloat("C_FLOAT", -599);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        float v = validater.getFloat("C_FLOAT");
        validater.close();
        assertCondition(v == -599);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateLong() - Should work when the column index is valid.
   **/
  public void Var113() {
    String key_ = "JDRowSUpdLong";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateLong(3, 545);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        long v = validater.getLong("C_INTEGER");
        validater.close();
        assertCondition(v == 545);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateLong() - Should work when the column name is valid.
   **/
  public void Var114() {
    String key_ = "JDRowSUpdLong";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateLong("C_INTEGER", -599);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        long v = validater.getLong("C_INTEGER");
        validater.close();
        assertCondition(v == -599);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateNull() - Should work when the column index is valid.
   **/
  public void Var115() {
    String key_ = "JDRowSUpdNull";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateNull(14);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        boolean wn = validater.wasNull();
        validater.close();
        assertCondition(wn == true, "wn is not true v=" + v);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateNull() - Should work when the column name is valid.
   **/
  public void Var116() {
    String key_ = "JDRowSUpdNull";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateNull("C_VARCHAR_50");
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        boolean wn = validater.wasNull();
        validater.close();
        assertCondition(wn == true, "wn is not true string =" + v);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateObject() - Should work when the column index is valid.
   **/
  public void Var117() {
    String key_ = "JDRowSUpdObject";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateObject(14, "Rhode Island");
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Rhode Island"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateObject() - Should work when the column name is valid.
   **/
  public void Var118() {
    String key_ = "JDRowSUpdObject";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateObject("C_VARCHAR_50", "District of Columbia");
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("District of Columbia"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateShort() - Should work when the column index is valid.
   **/
  public void Var119() {
    String key_ = "JDRowSUpdShort";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateShort(2, (short) 45);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        int v = validater.getInt("C_SMALLINT");
        validater.close();
        assertCondition(v == 45);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateShort() - Should work when the column name is valid.
   **/
  public void Var120() {
    String key_ = "JDRowSUpdShort";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateShort("C_SMALLINT", (short) -59);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        short v = validater.getShort("C_SMALLINT");
        validater.close();
        assertCondition(v == -59);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateString() - Should work when the column index is valid.
   **/
  public void Var121() {
    String key_ = "JDRowSUpdString";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateString(14, "Queries");
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Queries"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateString() - Should work when the column name is valid.
   **/
  public void Var122() {
    String key_ = "JDRowSUpdString";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateString("C_VARCHAR_50", "Inner joins");
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Inner joins"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateTime() - Should work when the column index is valid.
   **/
  public void Var123() {
    String key_ = "JDRowSUpdTime";
    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        Time d = Time.valueOf("19:22:40");
        rowset.updateTime(20, d);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        Time v = validater.getTime("C_TIME");
        validater.close();
        assertCondition(v.toString().equals(d.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateTime() - Should work when the column name is valid.
   **/
  public void Var124() {
    String key_ = "JDRowSUpdTime";
    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        Time d = Time.valueOf("10:08:00");
        rowset.updateTime("C_TIME", d);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        Time v = validater.getTime("C_TIME");
        validater.close();
        assertCondition(v.toString().equals(d.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateTimestamp() - Should work when the column index is valid.
   **/
  public void Var125() {
    String key_ = "JDRowSUpdTimestamp";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);

        Timestamp d = new Timestamp(19342240);
        rowset.updateTimestamp(21, d);
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        Timestamp v = validater.getTimestamp("C_TIMESTAMP");
        validater.close();
        assertCondition(v.equals(d));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateTimestamp() - Should work when the column name is valid.
   **/
  public void Var126() {
    String key_ = "JDRowSUpdTimestamp";

    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        Timestamp d = new Timestamp(1084440);
        rowset.updateTimestamp("C_TIMESTAMP", d);
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        Timestamp v = validater.getTimestamp("C_TIMESTAMP");
        validater.close();

        assertCondition(v.equals(d));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * Validates that close() works as expected after and execute without a result
   * set.
   **/
  public void Var127() {
    try {
      RowSet rowset = getRowSet(jndiName_);
      rowset.setUsername(systemObject_.getUserId());
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
      PasswordVault.clearPassword(charPassword);
      // @A1
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        rowset.setCommand("CREATE TABLE QGPL.TESTTBLE (G INTEGER)");
        rowset.execute();
      } else {
        stmt_.executeUpdate("CREATE TABLE QGPL.TESTTBLE (G INTEGER)");
      }

      rowset.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      try {
        RowSet rowset = getRowSet(jndiName_);
        // @A1
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          rowset.setUsername(systemObject_.getUserId()); // @K1A
          char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
          JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword); // @K1A //@K1A
          PasswordVault.clearPassword(charPassword);
          rowset.setCommand("DROP TABLE QGPL.TESTTBLE"); // @K1A
          rowset.execute();
        } else {
          stmt_.executeUpdate("DROP TABLE QGPL.TESTTBLE");
        }
        rowset.close();
      } catch (Exception all) {
        /* ignore */ }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateAsciiStream(int,AsciiStream).
   **/
  public void Var128() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);
      InputStream asciiStream = stringToAsciiStream("Dublin");
      rowset.updateAsciiStream(14, asciiStream, 6);
      boolean changed = listener.isRowChanged();
      asciiStream.close();
      rowset.close();
      assertCondition(changed);

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateAsciiStream(String,AsciiStream).
   **/
  public void Var129() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);
      InputStream asciiStream = stringToAsciiStream("Ibiza129");
      rowset.updateAsciiStream("C_VARCHAR_50", asciiStream, 8);
      asciiStream.close();
      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateBigDecimal(int,BigDecimal).
   **/
  public void Var130() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateBigDecimal(8, new BigDecimal("1.22"));

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateBigDecimal(String,BigDecimal).
   **/
  public void Var131() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateBigDecimal("C_NUMERIC_105", new BigDecimal("-5.3322"));

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateBinaryStream(int,BinaryStream).
   **/
  public void Var132() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      byte[] ba = new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 };
      rowset.updateBinaryStream(18, new ByteArrayInputStream(ba), ba.length);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateBinaryStream(String,BinaryStream).
   **/
  public void Var133() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      byte[] ba = new byte[] { (byte) -4, (byte) 98, (byte) 99 };
      rowset.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), 3);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateBoolean(int,boolean).
   **/
  public void Var134() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateBoolean(2, true);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateBoolean(int,boolean).
   **/
  public void Var135() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateBoolean(2, true);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateBoolean(String,boolean).
   **/
  public void Var136() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateBoolean("C_SMALLINT", false);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateByte(int,byte).
   **/
  public void Var137() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateByte(2, (byte) 45);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateByte(String,byte).
   **/
  public void Var138() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateByte("C_SMALLINT", (byte) -59);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateBytes(int,byte[]).
   **/
  public void Var139() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      byte[] ba = new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 };
      rowset.updateBytes(18, ba);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateBytes(String,bytes[]).
   **/
  public void Var140() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      byte[] ba = new byte[] { (byte) -4, (byte) 98, (byte) 99 };
      rowset.updateBytes("C_VARBINARY_20", ba);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateCharacterStream(int,CharacterStream).
   **/
  public void Var141() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateCharacterStream(14, new StringReader("Fargo"), 5);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateCharacterStream(String,CharacterStream).
   **/
  public void Var142() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateCharacterStream("C_VARCHAR_50", new StringReader("Gillette"), 8);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateDouble(int,double).
   **/
  public void Var143() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateDouble(6, 545);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateDouble(String,double).
   **/
  public void Var144() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateDouble("C_DOUBLE", -599);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateDate(int,Date).
   **/
  public void Var145() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      Date d = new Date(1922400000);
      rowset.updateDate(19, d);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateDate(String,Date).
   **/
  public void Var146() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      Date d = new Date(108000000);
      rowset.updateDate("C_DATE", d);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateInt(int,int).
   **/
  public void Var147() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateInt(3, 545);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateInt(String,int).
   **/
  public void Var148() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateInt("C_INTEGER", -599);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateFloat(int,float).
   **/
  public void Var149() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateFloat(5, 545);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateFloat(String,float).
   **/
  public void Var150() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateFloat("C_FLOAT", -599);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateLong(int,long).
   **/
  public void Var151() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateLong(3, 545);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateLong(String,long).
   **/
  public void Var152() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateLong("C_INTEGER", -599);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateNull(int).
   **/
  public void Var153() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateNull(14);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateNull(String).
   **/
  public void Var154() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateNull("C_VARCHAR_50");

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateObject(int,Object).
   **/
  public void Var155() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateObject(14, "Rhode Island");

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateObject(String,Object).
   **/
  public void Var156() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateObject("C_VARCHAR_50", "District of Columbia");

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateShort(int,short).
   **/
  public void Var157() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateShort(2, (short) 45);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateShort(int,short).
   **/
  public void Var158() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateShort(2, (short) 45);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateShort(String,short).
   **/
  public void Var159() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateShort("C_SMALLINT", (short) -59);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateString(int,String).
   **/
  public void Var160() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateString(14, "Queries");

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateString(String,String).
   **/
  public void Var161() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      rowset.updateString("C_VARCHAR_50", "Inner joins");

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateTime(int,Time).
   **/
  public void Var162() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      Time d = Time.valueOf("19:22:40");
      rowset.updateTime(20, d);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateTime(String,Time).
   **/
  public void Var163() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      Time d = Time.valueOf("10:08:00");
      rowset.updateTime("C_TIME", d);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateTimestamp(int,Timestamp).
   **/
  public void Var164() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      Timestamp d = new Timestamp(19342240);
      rowset.updateTimestamp(21, d);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateTimestamp(String,Timestamp).
   **/
  public void Var165() {
    String key_ = "RowSetListener";
    
    try {
      
      MyRowListener listener = new MyRowListener();

      RowSet rowset = updater(key_);
      rowset.addRowSetListener(listener);
      position(rowset, key_);

      Timestamp d = new Timestamp(1084440);
      rowset.updateTimestamp("C_TIMESTAMP", d);

      assertCondition(listener.isRowChanged());

      rowset.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    } finally {
      
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

  /**
   * getNCharacterStream() - Should work when the column index is valid.
   **/
  public void Var166() {
    if (checkJdbc40()) {
      RowSet rowset = null;
      try {
        rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "CHAR_FULL");
        // Reader v = rowset.getNCharacterStream (12);
        Reader v = (Reader) JDReflectionUtil.callMethod_O(rowset, "getNCharacterStream", 12);
        sb.setLength(0);
        assertCondition((v != null) && compare(v, "Toolbox for Java                                  ", sb), sb);
        rowset.close();
      } catch (Exception e) {
        String rowsetClass = "NOT SET";
        if (rowset != null) {
          rowsetClass = rowset.getClass().toString();
          try {
            rowset.close();
          } catch (SQLException e1) {
          }
        }

        failed(e, "Unexpected Exception rowSetClass=" + rowsetClass);
      }
    }
  }

  /**
   * getNCharacterStream() - Should work when the column name is valid.
   **/
  public void Var167() {
    if (checkJdbc40()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "CHAR_FULL");
        // Reader v = rowset.getCharacterStream ("C_CHAR_50");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rowset, "getNCharacterStream", "C_CHAR_50");
        sb.setLength(0);
        assertCondition(compare(v, "Toolbox for Java                                  ", sb), sb);
        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getNClob() - Should work when the column index is valid.
   **/
  public void Var168() {
    if (checkJdbc40()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_O(rowset, "getNClob", 14);
        assertCondition(compareNClobToString(v, "Java Toolbox"));

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getNClob() - Should work when the column name is valid.
   **/
  public void Var169() {
    if (checkJdbc40()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "CHAR_FULL");
        // Clob v = rowset.getClob ("C_VARCHAR_50");
        Object v = JDReflectionUtil.callMethod_OS(rowset, "getNClob", "C_VARCHAR_50");
        assertCondition(compareNClobToString(v, "Java Toolbox"));

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getNString() - Should work when the column index is valid.
   **/
  public void Var170() {
    if (checkJdbc40()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position0(rowset, "DATE_1998");
        // String s = rowset.getString (1);
        String s = (String) JDReflectionUtil.callMethod_O(rowset, "getNString", 1);
        assertCondition(s.equals("DATE_1998"));

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getNString() - Should work when the column name is valid.
   **/
  public void Var171() {
    if (checkJdbc40()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position0(rowset, "DATE_1998");
        // String s = rowset.getString ("C_KEY");
        String s = (String) JDReflectionUtil.callMethod_OS(rowset, "getNString", "C_KEY");
        assertCondition(s.equals("DATE_1998"));

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getRowId() - Should work when the column index is valid.
   **/
  public void Var172() {
    if (checkJdbc40()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "BINARY_NOTRANS"); // any row
        // RowId v = rowset.getRowId(28);
        Object v = JDReflectionUtil.callMethod_O(rowset, "getRowId", 28);

        // for now, just verify we got a rowid back
        if (v != null)
          assertCondition(true);
        else
          assertCondition(false);

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getRowId() - Should work when the column name is valid.
   **/
  public void Var173() {
    if (checkJdbc40()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        position(rowset, "BINARY_NOTRANS"); // any row
        // RowId v = rowset.getRowId(CRowod);
        Object v = JDReflectionUtil.callMethod_OS(rowset, "getRowId", "C_ROWID");
        // for now, just verify we got a rowid back
        if (v != null)
          assertCondition(true);
        else
          assertCondition(false);

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSQLXML() - Should work when the column index is valid.
   **/
  public void Var174() {
    if (checkJdbc40()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        rowset.execute();

        position(rowset, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_O(rowset, "getSQLXML", 14);
        String xmlString = (String) JDReflectionUtil.callMethod_O(v, "getString");
        String expected = "<d>Java Toolbox</d>";
        assertCondition(xmlString.equals(expected), "got " + xmlString + " sb " + expected);

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSQLXML() - Should work when the column name is valid.
   **/
  public void Var175() {
    if (checkJdbc40()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        rowset.execute();

        position(rowset, "CHAR_FULL");

        Object v = JDReflectionUtil.callMethod_OS(rowset, "getSQLXML", "C_VARCHAR_50");
        String xmlString = (String) JDReflectionUtil.callMethod_O(v, "getString");
        String expected = "<d>Java Toolbox</d>";
        assertCondition(xmlString.equals(expected), "got " + xmlString + " sb " + expected);

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * isClosed()
   **/
  public void Var176() {
    if (checkJdbc40()) {
      try {
        RowSet rowset = getRowSet(jndiName_);
        rowset.setUsername(systemObject_.getUserId());
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
        JDReflectionUtil.callMethod_V(rowset, "setPassword", charPassword);
        PasswordVault.clearPassword(charPassword);
        rowset.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset.execute();

        boolean isClosed1 = JDReflectionUtil.callMethod_B(rowset, "isClosed");
        rowset.close();
        boolean isClosed2 = JDReflectionUtil.callMethod_B(rowset, "isClosed");

        assertCondition((isClosed1 == false) && (isClosed2 == true),
            "isClosed1=" + isClosed1 + " isClosed2=" + isClosed2 + " object is " + rowset.getClass().getName());

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed -
   * 
   * updateAsciiStream(int,AsciiStream).
   **/
  public void Var177() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateAsciiStream (14, stringToAsciiStream ("Dublin"), 6L);
        InputStream is = stringToAsciiStream("Dublin");
        JDReflectionUtil.callMethod_V(rowset, "updateAsciiStream", 14, is, 6L);
        is.close();
        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed -
   * 
   * updateAsciiStream(String,AsciiStream).
   **/
  public void Var178() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Ibiza"), 5L);
        InputStream is = stringToAsciiStream("Ibiza178");
        JDReflectionUtil.callMethod_V_IS(rowset, "updateAsciiStream", "C_VARCHAR_50", is, 8L);
        is.close();

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed -
   * 
   * updateBinaryStream(int,BinaryStream).
   **/
  public void Var179() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        byte[] ba = new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 };
        // rowset.updateBinaryStream (16, new ByteArrayInputStream (ba), ba.length);
        JDReflectionUtil.callMethod_V(rowset, "updateBinaryStream", 18, new ByteArrayInputStream(ba),
            (Integer.valueOf(ba.length)).longValue());

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed -
   * 
   * updateBinaryStream(String,BinaryStream).
   **/
  public void Var180() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        byte[] ba = new byte[] { (byte) -4, (byte) 98, (byte) 99 };
        // rowset.updateBinaryStream ("C_VARBINARY_20", new ByteArrayInputStream (ba),
        // 3);
        JDReflectionUtil.callMethod_V_IS(rowset, "updateBinaryStream", "C_VARBINARY_20", new ByteArrayInputStream(ba),
            3L);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateBlob().
   */
  public void Var181() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        byte[] ba = new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 };
        // rowset.updateBlob (16, new ByteArrayInputStream (ba),
        // ba.length);
        JDReflectionUtil.callMethod_V(rowset, "updateBlob", 18, new ByteArrayInputStream(ba),
            (Integer.valueOf(ba.length)).longValue());

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateBlob().
   */
  public void Var182() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        byte[] ba = new byte[] { (byte) -4, (byte) 98, (byte) 99 };
        // rowset.updateBlob ("C_VARBINARY_20", new ByteArrayInputStream (ba), 3);
        JDReflectionUtil.callMethod_V_IS(rowset, "updateBlob", "C_VARBINARY_20", new ByteArrayInputStream(ba), 3L);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed -
   * 
   * updateCharacterStream().
   **/
  public void Var183() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateCharacterStream (14, new StringReader ("Fargo"), 5L);
        JDReflectionUtil.callMethod_V(rowset, "updateCharacterStream", 14, new StringReader("Fargo"), 5L);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateCharacterStream().
   */
  public void Var184() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateCharacterStream ("C_VARCHAR_50", new StringReader ("Gillette"),
        // 8);
        JDReflectionUtil.callMethod_V(rowset, "updateCharacterStream", "C_VARCHAR_50", new StringReader("Gillette"),
            8L);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateClob().
   **/
  public void Var185() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateClob (14, new StringReader ("Fargo"), 5L);
        JDReflectionUtil.callMethod_V(rowset, "updateClob", 14, new StringReader("Fargo"), 5L);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateClob().
   **/
  public void Var186() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateClob ("C_VARCHAR_50", new StringReader ("Gillette"), 8);
        JDReflectionUtil.callMethod_V(rowset, "updateClob", "C_VARCHAR_50", new StringReader("Gillette"), 8L);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed -
   * 
   * updateNCharacterStream().
   **/
  public void Var187() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateNCharacterStream (14, new StringReader ("Fargo"), 5L);
        JDReflectionUtil.callMethod_V(rowset, "updateNCharacterStream", 14, new StringReader("Fargo"), 5L);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateNCharacterStream().
   */
  public void Var188() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateNCharacterStream ("C_VARCHAR_50", new StringReader ("Gillette"),
        // 8);
        JDReflectionUtil.callMethod_V(rowset, "updateNCharacterStream", "C_VARCHAR_50", new StringReader("Gillette"),
            8L);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateNClob().
   **/
  public void Var189() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateNClob (14, new StringReader ("Fargo"), 5L);

        Object nClob = JDReflectionUtil.callMethod_O(conn2_, "createNClob");

        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "FargoNclob");
        JDReflectionUtil.callMethod_V(rowset, "updateNClob", 14, nClob);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateNClob().
   **/
  public void Var190() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateNClob ("C_VARCHAR_50", new StringReader ("Gillette"), 8);

        Object nClob = JDReflectionUtil.callMethod_O(conn2_, "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "GilletteNclob");
        JDReflectionUtil.callMethod_V(rowset, "updateNClob", "C_VARCHAR_50", nClob);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateNClob2().
   **/
  public void Var191() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateNClob (14, new StringReader ("Fargo"), 5L);
        JDReflectionUtil.callMethod_V(rowset, "updateNClob", 14, new StringReader("Fargo"), 5L);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateNClob2().
   **/
  public void Var192() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateNClob ("C_VARCHAR_50", new StringReader ("Gillette"), 8);
        JDReflectionUtil.callMethod_V(rowset, "updateNClob", "C_VARCHAR_50", new StringReader("Gillette"), 8L);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed -
   * 
   * updateNString(int,String).
   **/
  public void Var193() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateNString (14, "Queries");
        JDReflectionUtil.callMethod_V(rowset, "updateNString", 14, "Queries");
        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed -
   * 
   * updateNString(String,String).
   **/
  public void Var194() {
    if (checkJdbc40()) {

      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // rowset.updateNString ("C_VARCHAR_50", "Inner joins");
        JDReflectionUtil.callMethod_V(rowset, "updateNString", "C_VARCHAR_50", "Inner joins");
        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateRowId().
   **/
  public void Var195() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      RowSet rowset = null;
      
      try {
        
        MyRowListener listener = new MyRowListener();

        rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        // RowId ri = rowset.getRowId(28);
        Object ri = JDReflectionUtil.callMethod_O(rowset, "getRowId", 28);

        JDReflectionUtil.callMethod_V(rowset, "updateRowId", 29, ri);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          assertCondition(e.getMessage().indexOf("mismatch") != -1);
          try {
            rowset.close();
          } catch (Exception e1) {
          }
          return;
        }
        failed(e, "Unexpected Exception.");
        if (rowset != null) {
          try {
            rowset.close();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateRowId().
   **/
  public void Var196() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      MyRowListener listener = new MyRowListener();
      
      try (RowSet rowset = updater(key_)) {
        

        rowset.addRowSetListener(listener);
        position(rowset, key_);

        Object ri = JDReflectionUtil.callMethod_O(rowset, "getRowId", 28);

        JDReflectionUtil.callMethod_V(rowset, "updateRowId", "C_VARBINARY_40", ri);

        assertCondition(listener.isRowChanged());

      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          assertCondition(e.getMessage().indexOf("mismatch") != -1);
          return;
        }
        failed(e, "Unexpected Exception.");

      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateSQLXML().
   **/
  public void Var197() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try {
        
        MyRowListener listener = new MyRowListener();

        RowSet rowset = updater(key_);
        rowset.addRowSetListener(listener);
        position(rowset, key_);

        Object sqlXML = JDReflectionUtil.callMethod_O(conn2_, "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlXML, "setString", "FargoXML");
        JDReflectionUtil.callMethod_V(rowset, "updateSQLXML", 23, sqlXML);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * Validates that addRowSetListener(RowSetListener) notifies listeners when the
   * row is changed - updateSQLXML().
   **/
  public void Var198() {
    if (checkJdbc40()) {
      String key_ = "RowSetListener";
      
      try (RowSet rowset = updater(key_);) {
        
        MyRowListener listener = new MyRowListener();

        rowset.addRowSetListener(listener);
        position(rowset, key_);

        Object sqlXML = JDReflectionUtil.callMethod_O(conn2_, "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlXML, "setString", "FargoXML");
        JDReflectionUtil.callMethod_V(rowset, "updateSQLXML", "C_CLOB", sqlXML);

        assertCondition(listener.isRowChanged());

        rowset.close();
      } catch (Exception e) {

        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateAsciiStream(int,AsciiStream).
   **/
  public void Var199() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdAsciiStream";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        // rowset.updateAsciiStream (14, stringToAsciiStream ("Dublin"), 6L);
        InputStream is = stringToAsciiStream("Dublin2");
        JDReflectionUtil.callMethod_V(rowset, "updateAsciiStream", 14, is, 7L);
        is.close();
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString(14);
        validater.close();
        assertCondition(v.equals("Dublin2"), "Expected Dublin2 got " + v);

      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateAsciiStream(int,AsciiStream).
   **/
  public void Var200() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdAsciiStream";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        // rowset.updateAsciiStream (14, stringToAsciiStream ("Dublin"), 6L);
        InputStream is = stringToAsciiStream("Dublin3");
        JDReflectionUtil.callMethod_V_IS(rowset, "updateAsciiStream", "C_VARCHAR_50", is, 7L);
        rowset.updateRow();
        is.close();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Dublin3"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateBinaryStream().
   **/
  public void Var201() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdBinaryStream";
      
      try {
        
        sb.setLength(0);

        RowSet rowset = updater(key_);
        position(rowset, key_);

        byte[] ba = new byte[] { (byte) 11, (byte) 4, (byte) 98, (byte) -22 };
        // rowset.updateBinaryStream (16, new ByteArrayInputStream (ba), ba.length);
        JDReflectionUtil.callMethod_V(rowset, "updateBinaryStream", 18, new ByteArrayInputStream(ba),
            (Integer.valueOf(ba.length)).longValue());

        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        InputStream v = validater.getBinaryStream(18);
        validater.close();
        assertCondition(JDTestcase.compare(v, ba, sb), sb);
        v.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateBinaryStream().
   **/
  public void Var202() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdBinaryStream";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        byte[] ba = new byte[] { (byte) 12, (byte) 44, (byte) 98, (byte) -2 };
        // rowset.updateBinaryStream (18, new ByteArrayInputStream (ba), ba.length);
        JDReflectionUtil.callMethod_V_IS(rowset, "updateBinaryStream", "C_VARBINARY_20", new ByteArrayInputStream(ba),
            (Integer.valueOf(ba.length)).longValue());

        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        InputStream v = validater.getBinaryStream("C_VARBINARY_20");
        validater.close();
        sb.setLength(0);

        assertCondition(JDTestcase.compare(v, ba, sb), sb);
        v.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateBlob().
   **/
  public void Var203() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdBlob";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        byte[] ba = new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4 };
        // rowset.updateBinaryStream (18, new ByteArrayInputStream (ba), ba.length);
        JDReflectionUtil.callMethod_V_IS(rowset, "updateBlob", "C_VARBINARY_20", new ByteArrayInputStream(ba),
            (Integer.valueOf(ba.length)).longValue());

        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        InputStream v = validater.getBinaryStream("C_VARBINARY_20");
        validater.close();
        sb.setLength(0);
        assertCondition(JDTestcase.compare(v, ba, sb), sb);
        v.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateBlob().
   **/
  public void Var204() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdBlob";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        byte[] ba = new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4 };
        // rowset.updateBinaryStream (18, new ByteArrayInputStream (ba), ba.length);
        JDReflectionUtil.callMethod_V(rowset, "updateBlob", 18, new ByteArrayInputStream(ba),
            (Integer.valueOf(ba.length)).longValue());

        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        InputStream v = validater.getBinaryStream(18);
        validater.close();
        sb.setLength(0);

        assertCondition(JDTestcase.compare(v, ba, sb), sb);
        v.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateCharacterStream().
   **/
  public void Var205() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdCharacterStream";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        // rowset.updateAsciiStream (14, stringToAsciiStream ("Dublin"), 6L);
        JDReflectionUtil.callMethod_V(rowset, "updateCharacterStream", 14, new StringReader("Dublin4"), 7L);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString(14);
        validater.close();
        assertCondition(v.equals("Dublin4"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateCharacterStream().
   **/
  public void Var206() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdCharacterStream";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        // rowset.updateAsciiStream (14, stringToAsciiStream ("Dublin"), 6L);
        JDReflectionUtil.callMethod_V(rowset, "updateCharacterStream", "C_VARCHAR_50", new StringReader("Dublin5"), 7L);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Dublin5"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateClob().
   **/
  public void Var207() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdClob";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        // rowset.updateAsciiStream (14, stringToAsciiStream ("Dublin"), 6L);
        JDReflectionUtil.callMethod_V(rowset, "updateClob", 14, new StringReader("Dublin6"), 7L);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString(14);
        validater.close();
        assertCondition(v.equals("Dublin6"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateClob().
   **/
  public void Var208() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdClob";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        // rowset.updateAsciiStream (14, stringToAsciiStream ("Dublin"), 6L);
        JDReflectionUtil.callMethod_V(rowset, "updateClob", "C_VARCHAR_50", new StringReader("Dublin7"), 7L);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Dublin7"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateNCharacterStream().
   **/
  public void Var209() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdCharacterStream";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        // rowset.updateAsciiStream (14, stringToAsciiStream ("Dublin"), 6L);
        JDReflectionUtil.callMethod_V(rowset, "updateNCharacterStream", 14, new StringReader("Dublin8"), 7L);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString(14);
        validater.close();
        assertCondition(v.equals("Dublin8"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateNCharacterStream().
   **/
  public void Var210() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdCharacterStream";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        // rowset.updateAsciiStream (14, stringToAsciiStream ("Dublin"), 6L);
        JDReflectionUtil.callMethod_V(rowset, "updateNCharacterStream", "C_VARCHAR_50", new StringReader("Dublin9"),
            7L);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Dublin9"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateNClob().
   **/
  public void Var211() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdClob";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        Object nClob = JDReflectionUtil.callMethod_O(conn2_, "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Dubl211");

        JDReflectionUtil.callMethod_V(rowset, "updateNClob", 14, nClob);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString(14);
        validater.close();
        assertCondition(v.equals("Dubl211"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateNClob().
   **/
  public void Var212() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdClob";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        Object nClob = JDReflectionUtil.callMethod_O(conn2_, "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Dubl212");

        JDReflectionUtil.callMethod_V(rowset, "updateNClob", "C_VARCHAR_50", nClob);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Dubl212"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateNClob().
   **/
  public void Var213() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdCharacterStream";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        // rowset.updateAsciiStream (14, stringToAsciiStream ("Dublin"), 6L);
        JDReflectionUtil.callMethod_V(rowset, "updateNClob", 14, new StringReader("Dubl213"), 7L);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString(14);
        validater.close();
        assertCondition(v.equals("Dubl213"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateNClob().
   **/
  public void Var214() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdCharacterStream";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        // rowset.updateAsciiStream (14, stringToAsciiStream ("Dublin"), 6L);
        JDReflectionUtil.callMethod_V(rowset, "updateNClob", "C_VARCHAR_50", new StringReader("Dubl214"), 7L);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Dubl214"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateNString() - Should work when the column index is valid.
   **/
  public void Var215() {
    String key_ = "JDRowSUpdString";

    if (checkJdbc40()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);

        JDReflectionUtil.callMethod_V(rowset, "updateNString", 14, "Quer215");
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Quer215"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateNString() - Should work when the column index is valid.
   **/
  public void Var216() {
    String key_ = "JDRowSUpdString";

    if (checkJdbc40()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);

        JDReflectionUtil.callMethod_V(rowset, "updateNString", "C_VARCHAR_50", "Quer215");
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_VARCHAR_50");
        validater.close();
        assertCondition(v.equals("Quer215"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

  /**
   * updateRowId().
   **/
  public void Var217() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdBlob";
      
      try (RowSet rowset2 = getRowSet(jndiName_)) {// get any valid rowid from the get table
        

        rowset2.setUsername(systemObject_.getUserId());
        rowset2.setPassword(PasswordVault.decryptPasswordLeak(encryptedPassword_));
        rowset2.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset2.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset2.execute();
        position(rowset2, "CHAR_FULL"); // any row
        Object ri2 = JDReflectionUtil.callMethod_O(rowset2, "getRowId", 28);
        rowset2.close();

        try (RowSet rowset = updater(key_)) {
          position(rowset, key_);

          /* Object ri = */ JDReflectionUtil.callMethod_O(rowset, "getRowId", 28);

          JDReflectionUtil.callMethod_V(rowset, "updateRowId", 29, ri2);

          rowset.updateRow();
        }

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        Object v = JDReflectionUtil.callMethod_O(validater, "getRowId", 29);
        validater.close();
        assertCondition(ri2.toString().equals(v.toString()));
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          assertCondition(e.getMessage().indexOf("mismatch") != -1);
          return;
        }
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateRowId().
   **/
  public void Var218() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdBlob";
      
      try (RowSet rowset2 = getRowSet(jndiName_)) {// get any valid rowid from the get table
        

        rowset2.setUsername(systemObject_.getUserId());
        rowset2.setPassword(PasswordVault.decryptPasswordLeak(encryptedPassword_));
        rowset2.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
        rowset2.setCommand("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rowset2.execute();
        position(rowset2, "CHAR_FULL"); // any row
        Object ri2 = JDReflectionUtil.callMethod_O(rowset2, "getRowId", 28);
        rowset2.close();

        try (RowSet rowset = updater(key_)) {
          position(rowset, key_);

          /* Object ri = */ JDReflectionUtil.callMethod_O(rowset, "getRowId", 28);
          // note, that as of today, i cannot update the rowid with new values (error from
          // host server)
          JDReflectionUtil.callMethod_V(rowset, "updateRowId", "C_VARBINARY_40", ri2);

          rowset.updateRow();
        }

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        Object v = JDReflectionUtil.callMethod_O(validater, "getRowId", 29);
        validater.close();

        assertCondition(ri2.equals(v), "\nretrieved='" + v + "'" + "\nexpected ='" + ri2 + "'");
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {

          assertCondition(e.getMessage().indexOf("mismatch") != -1);
          return;
        }
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateSQLXML().
   **/
  public void Var219() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdClob";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        Object xmlObj = JDReflectionUtil.callMethod_O(conn2_, "createSQLXML");
        JDReflectionUtil.callMethod_V(xmlObj, "setString", "Dubl219");

        JDReflectionUtil.callMethod_V(rowset, "updateSQLXML", 23, xmlObj);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString(23);
        validater.close();
        assertCondition(v.equals("Dubl219"), "Retrieved='" + v + "' expected='Dubl219'");
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateSQLXML().
   **/
  public void Var220() {
    if (checkJdbc40()) {
      String key_ = "JDRowSUpdClob";
      
      try {
        

        RowSet rowset = updater(key_);
        position(rowset, key_);

        Object xmlObj = JDReflectionUtil.callMethod_O(conn2_, "createSQLXML");
        JDReflectionUtil.callMethod_V(xmlObj, "setString", "Dubl220");

        JDReflectionUtil.callMethod_V(rowset, "updateSQLXML", "C_CLOB", xmlObj);
        rowset.updateRow();
        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        String v = validater.getString("C_CLOB");
        validater.close();
        assertCondition(v.equals("Dubl220"), "Retrieved='" + v + "' expected='Dubl220'");
      } catch (Exception e) {
        failed(e, "Unexpected Exception.");
      } finally {
        
      }
    }
  }

  /**
   * updateBoolean() - Should work when the column index is valid on a BOOLEAN
   * column
   **/
  public void Var221() {
    String key_ = "JDRowSet221";
    if (checkJdbc20()) {
      
      try {
        
        RowSet rowset = updater(key_);

        position(rowset, key_);
        rowset.updateBoolean("C_BOOLEAN", true);
        rowset.updateRow();

        rowset.close();

        // Validate.
        RowSet validater = validater();
        position(validater, key_);
        boolean v = validater.getBoolean("C_BOOLEAN");
        validater.close();
        cleanUpdateTable();
        assertCondition(v == true, "got " + v + " sb true");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        
      }
    }
  }

}
