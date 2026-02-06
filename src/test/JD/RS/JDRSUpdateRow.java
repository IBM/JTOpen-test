///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateRow.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.RS;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestcase;
import test.JD.JDSerializeFile;

/**
 * Testcase JDRSUpdateRow. This tests the following method of the JDBC ResultSet
 * class:
 * 
 * <ul>
 * <li>updateRow()
 * <li>rowUpdated()
 * <li>cancelRowUpdates()
 * </ul>
 **/
public class JDRSUpdateRow extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "JDRSUpdateRow";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.JDRSTest.main(newArgs);
  }

  // Private data.
  private static final String key_ = "JDRSUpdateRow";
  private static final String key2_ = "JDRSUpdateRow2";
  private static String select_ = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;

  private Statement statement_;
  private Statement statement2_;
  private ResultSet rs_;
  private String table1 = "JDTESTRS.RSSUBUPDATE"; // @E1A
  private String table2 = "JDTESTRS.RSSUBUPDATE2"; // @E1A
  private String table3 = "JDTESTRS.RSALIASUPD"; // @E3A

  /**
   * Constructor.
   **/
  public JDRSUpdateRow(AS400 systemObject, Hashtable<String, Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,

      String password) {
    super(systemObject, "JDRSUpdateRow", namesAndVars, runMode, fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {
    select_ = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;

    table1 = JDRSTest.COLLECTION + ".RSSUBUPDATE";
    table2 = JDRSTest.COLLECTION + ".RSSUBUPDATE2";
    table3 = JDRSTest.COLLECTION + ".RSALIASUPD";

    if (isJdbc20()) {
      // SQL400 - driver neutral...
      String url = baseURL_
      // String url = "jdbc:as400://" + systemObject_.getSystemName()

      ;
      connection_ = testDriver_.getConnection(url, systemObject_.getUserId(), encryptedPassword_);
      connection_.setAutoCommit(false); // @C1A
      statement_ = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      statement2_ = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

      statement_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('" + key_ + "')");
      statement_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('" + key2_ + "')");

      subSelectSetup();
      columnAliasSetup();

      connection_.commit(); // @C1A

      rs_ = statement_.executeQuery(select_ + " FOR UPDATE");
    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (isJdbc20()) {
      rs_.close();
      cleanupTable(statement_, table1); // @E1A
      cleanupTable(statement_, table2); // @E1A
      cleanupTable(statement_, table3); // @E3A
      statement_.close();
      connection_.commit(); // @C1A
      connection_.close();
    }
  }

  protected void subSelectSetup() // @E1A
  {
    try {
      Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      initTable(s, table1, " (SSN CHAR(9))");
      initTable(s, table2, " (SSN CHAR(9))");
      s.executeUpdate("INSERT INTO " + table1 + " VALUES ('123456789')");
      s.executeUpdate("INSERT INTO " + table1 + " VALUES ('987654321')");
      s.executeUpdate("INSERT INTO " + table1 + " VALUES ('852963741')");
      s.executeUpdate("INSERT INTO " + table1 + " VALUES ('987456321')");
      s.executeUpdate("INSERT INTO " + table2 + " VALUES ('987456321')");
      s.executeUpdate("INSERT INTO " + table2 + " VALUES ('987654321')");
      s.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void columnAliasSetup() // @E3A
  {
    try {
      Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      initTable(s, table3, " (CITY VARCHAR(50), STATE CHAR(2))");
      s.executeUpdate("INSERT INTO " + table3 + " VALUES ('New York City', 'NY')");
      s.executeUpdate("INSERT INTO " + table3 + " VALUES ('Chicago', 'IL')");
      s.executeUpdate("INSERT INTO " + table3 + " VALUES ('Los Angeles', 'CA')");
      s.close();
      connection_.commit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * updateRow() - Should throw exception when the result set is closed.
   **/
  public void Var001() {
    succeeded();
    /*
     * 
     * if (checkJdbc20 ()) { Statement s = null; ResultSet rs = null; try { s =
     * connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
     * ResultSet.CONCUR_UPDATABLE); rs = s.executeQuery ("SELECT * FROM " +
     * JDRSTest.RSTEST_UPDATE + " FOR UPDATE"); rs.next (); rs.close ();
     * rs.updateRow (); failed ("Didn't throw SQLException"); } catch (Exception e)
     * { assertExceptionIsInstanceOf (e, "java.sql.SQLException"); } if (rs != null)
     * try {rs.close();} catch (Exception e) {} if (s != null) try {s.close();}
     * catch (Exception e) {} }
     */
  }

  /**
   * updateRow() - Should throw exception when the result set is not updatable.
   **/
  public void Var002() {
    if (checkJdbc20()) {
      Statement s = null;
      ResultSet rs = null;
      try {
        s = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_UPDATE);
        rs.next();
        rs.updateRow(); /* exception */
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }

      if (rs != null)
        try {
          rs.close();
        } catch (Exception e) {
        }
      if (s != null)
        try {
          s.close();
        } catch (Exception e) {
        }
    }
  }

  /**
   * updateRow() - Should throw exception when cursor is not pointing to a row.
   **/
  public void Var003() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, null);
        rs_.updateRow(); /* exception */
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
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
   * updateRow() - Should have no effect when no updates are pending.
   **/
  public void Var004() {
    StringBuffer message = new StringBuffer();
    if (checkJdbc20()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        JDRSTest.position(rs_, key_);
        int columnCount = rs_.getMetaData().getColumnCount();
        Object[] before = new Object[columnCount];
        boolean[] beforeNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          before[i] = rs_.getObject(i + 1);
          beforeNulls[i] = rs_.wasNull();
        }

        rs_.updateRow(); /* serialized */

        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        Object[] after = new Object[columnCount];
        boolean[] afterNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          after[i] = rs2.getObject(i + 1);
          afterNulls[i] = rs2.wasNull();
        }
        rs2.close();

        boolean success = true;
        for (int i = 0; i < columnCount; ++i) {
          success = compareColumns(i, before, after, beforeNulls, afterNulls, message) && success;
        }

        assertCondition(success, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * updateRow() - Should update exactly 1 column when updates are pending.
   **/
  public void Var005() {
    StringBuffer message = new StringBuffer();
    if (checkJdbc20()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        JDRSTest.position(rs_, key_);
        int columnCount = rs_.getMetaData().getColumnCount();
        Object[] before = new Object[columnCount];
        boolean[] beforeNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          before[i] = rs_.getObject(i + 1);
          beforeNulls[i] = rs_.wasNull();
        }

        rs_.updateString("C_VARCHAR_50", "Hi Mom!");
        rs_.updateRow();/* serialized */

        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        Object[] after = new Object[columnCount];
        boolean[] afterNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          after[i] = rs2.getObject(i + 1);
          afterNulls[i] = rs2.wasNull();
        }
        int updatedColumn = rs2.findColumn("C_VARCHAR_50") - 1;
        rs2.close();

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
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * updateRow() - Should update exactly 3 columns when updates are pending.
   **/
  public void Var006() {
    StringBuffer message = new StringBuffer();
    if (checkJdbc20()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        JDRSTest.position(rs_, key_);
        int columnCount = rs_.getMetaData().getColumnCount();
        Object[] before = new Object[columnCount];
        boolean[] beforeNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          before[i] = rs_.getObject(i + 1);
          beforeNulls[i] = rs_.wasNull();
        }

        rs_.updateInt("C_INTEGER", 9876);
        rs_.updateNull("C_SMALLINT");
        rs_.updateFloat("C_FLOAT", -4.25f);
        rs_.updateRow();/* serialized */

        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        Object[] after = new Object[columnCount];
        boolean[] afterNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          after[i] = rs2.getObject(i + 1);
          afterNulls[i] = rs2.wasNull();
        }
        int updatedColumn1 = rs2.findColumn("C_INTEGER") - 1;
        int updatedColumn2 = rs2.findColumn("C_SMALLINT") - 1;
        int updatedColumn3 = rs2.findColumn("C_FLOAT") - 1;
        rs2.close();

        boolean success = true;
        for (int i = 0; i < columnCount; ++i) {
          if (i == updatedColumn1) {
            if ((!after[i].equals(Integer.valueOf(9876))) || (afterNulls[i] != false))
              success = false;
          } else if (i == updatedColumn2) {
            if ((after[i] != null) || (afterNulls[i] != true))
              success = false;
          } else if (i == updatedColumn3) {
            if ((!after[i].equals(Double.valueOf(-4.25f))) || (afterNulls[i] != false))
              success = false;
          } else {
            success = compareColumns(i, before, after, beforeNulls, afterNulls, message) && success;
          }
        }

        assertCondition(success, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * updateRow() - Updated row should be reflected in the result set being updated
   * when not repositioned.
   **/
  public void Var007() {
    if (checkJdbc20()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        JDRSTest.position(rs_, key_);
        rs_.getMetaData().getColumnCount();

        rs_.updateString("C_VARCHAR_50", "Hola Mama!");
        rs_.updateRow();/* serialized */

        assertCondition(rs_.getString("C_VARCHAR_50").equals("Hola Mama!"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * updateRow() - Updated row should be reflected in the result set being updated
   * when repositioned.
   **/
  public void Var008() {
    if (checkJdbc20()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        JDRSTest.position(rs_, key_);
        rs_.getMetaData().getColumnCount();

        rs_.updateString("C_VARCHAR_50", "Hola Mama!");
        rs_.updateRow();/* serialized */

        rs_.beforeFirst();
        JDRSTest.position(rs_, key_);
        assertCondition(rs_.getString("C_VARCHAR_50").equals("Hola Mama!"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * updateRow() - Should update appropriately when the result set is generated by
   * a SELECT with correlation names.
   * <P>
   * <B>Note:</B> The JDBC specification states that getting a value from a
   * SMALLINT database column with getObject() will return an java.lang.Integer
   * object, not a java.lang.Short object. This testcase has been changed to match
   * that expectation.
   **/
  public void Var009() {
    StringBuffer message = new StringBuffer();
    if (checkJdbc20()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs1 = s.executeQuery(select_ + " AS TESTCORR FOR UPDATE");

        JDRSTest.position(rs1, key_);
        int columnCount = rs1.getMetaData().getColumnCount();
        Object[] before = new Object[columnCount];
        boolean[] beforeNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          before[i] = rs1.getObject(i + 1);
          beforeNulls[i] = rs1.wasNull();
        }

        rs1.updateShort("C_SMALLINT", (short) -123);
        rs1.updateRow();/* serialized */
        rs1.close();
        s.close();

        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        Object[] after = new Object[columnCount];
        boolean[] afterNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          after[i] = rs2.getObject(i + 1);
          afterNulls[i] = rs2.wasNull();
        }
        int updatedColumn = rs2.findColumn("C_SMALLINT") - 1;
        rs2.close();

        boolean success = true;
        for (int i = 0; i < columnCount; ++i) {
          if (i == updatedColumn) {
            if ((!after[i].equals(Integer.valueOf(-123))) || (afterNulls[i] != false))
              success = false;
          } else {
            success = compareColumns(i, before, after, beforeNulls, afterNulls, message) && success;
          }
        }

        assertCondition(success, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * updateRow() - Columns specified for a previous update should not be carried
   * over to the next..
   **/
  public void Var010() {
    StringBuffer message = new StringBuffer();
    if (checkJdbc20()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        int columnCount = rs2.getMetaData().getColumnCount();
        Object[] before = new Object[columnCount];
        boolean[] beforeNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          before[i] = rs2.getObject(i + 1);
          beforeNulls[i] = rs2.wasNull();
        }
        rs2.close();

        JDRSTest.position(rs_, key2_);
        rs_.updateString("C_VARCHAR_50", "Computer");
        rs_.updateRow();/* serialized */

        JDRSTest.position(rs_, key_);
        rs_.updateInt("C_INTEGER", 3343);
        rs_.updateRow();/* serialized */

        rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        Object[] after = new Object[columnCount];
        boolean[] afterNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          after[i] = rs2.getObject(i + 1);
          afterNulls[i] = rs2.wasNull();
        }
        int updatedColumn = rs2.findColumn("C_INTEGER") - 1;
        rs2.close();

        boolean success = true;
        for (int i = 0; i < columnCount; ++i) {
          if (i == updatedColumn) {
            if ((!after[i].equals(Integer.valueOf(3343))) || (afterNulls[i] != false)) {
              success = false;
              output_.println("failed 1");
            }
          } else {
            success = compareColumns(i, before, after, beforeNulls, afterNulls, message) && success;
          }
        }

        assertCondition(success, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

// @C2A
  /**
   * updateRow() - Test with mixed case cursor name.
   * <P>
   * SQL400 - Today, the native JDBC driver has no chance of handling the cursor
   * name that is mixed case and has a space in it. It appears that the SQ path we
   * end up taking uppercases all cursor names. This will be investigated as there
   * is time.
   * <P>
   * <B>Note:</B> The JDBC specification states that getting a value from a
   * SMALLINT database column with getObject() will return an java.lang.Integer
   * object, not a java.lang.Short object. This testcase has been changed to match
   * that expectation.
   **/
  public void Var011() {
    StringBuffer message = new StringBuffer();
    if (checkJdbc20()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        Statement statement = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        if (isToolboxDriver())
          statement.setCursorName("MiXeD CaSe");
        else
          statement.setCursorName("MiXeDCaSe");
        ResultSet rs = statement.executeQuery(select_ + " FOR UPDATE");
        JDRSTest.position(rs, key_);
        int columnCount = rs.getMetaData().getColumnCount();
        Object[] before = new Object[columnCount];
        boolean[] beforeNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          before[i] = rs.getObject(i + 1);
          beforeNulls[i] = rs.wasNull();
        }

        rs.updateNull("C_INTEGER");
        rs.updateShort("C_SMALLINT", (short) -44);
        rs.updateRow(); /* serialized */

        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        Object[] after = new Object[columnCount];
        boolean[] afterNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          after[i] = rs2.getObject(i + 1);
          afterNulls[i] = rs2.wasNull();
        }
        int updatedColumn1 = rs2.findColumn("C_INTEGER") - 1;
        int updatedColumn2 = rs2.findColumn("C_SMALLINT") - 1;
        rs2.findColumn("C_FLOAT");
        rs2.close();

        boolean success = true;
        for (int i = 0; i < columnCount; ++i) {
          if (i == updatedColumn1) {
            if ((after[i] != null) || (afterNulls[i] != true))
              success = false;
          } else if (i == updatedColumn2) {
            if ((!after[i].equals(Integer.valueOf(-44))) || (afterNulls[i] != false))
              success = false;
          } else {
            success = compareColumns(i, before, after, beforeNulls, afterNulls, message) && success;
          }
        }
        rs.close(); 
        statement.close(); 
        assertCondition(success, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * rowUpdated() - Should return false on all rows in another result set after
   * repositioning.
   **/
  public void Var012() {
    if (checkJdbc20()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        JDRSTest.position(rs_, key_);
        rs_.updateInt("C_INTEGER", 98276);
        rs_.updateNull("C_SMALLINT");
        rs_.updateFloat("C_FLOAT", -4.225f);
        rs_.updateRow();/* serialized */

        ResultSet rs2 = statement2_.executeQuery(select_);
        boolean success = true;
        while (rs2.next())
          if (rs2.rowUpdated() != false)
            success = false;
        rs2.close();

        assertCondition(success);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * rowUpdated() - Should return false on all rows in the updated result set
   * after repositioning.
   **/
  public void Var013() {
    if (checkJdbc20()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        JDRSTest.position(rs_, key_);
        rs_.updateInt("C_INTEGER", 98276);
        rs_.updateNull("C_SMALLINT");
        rs_.updateFloat("C_FLOAT", -4.225f);
        rs_.updateRow();/* serialized */

        rs_.beforeFirst();
        boolean success = true;
        while (rs_.next())
          if (rs_.rowUpdated() != false)
            success = false;

        assertCondition(success);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * cancelRowUpdates() - Should throw exception when the result set is closed.
   **/
  public void Var014() {
    if (checkJdbc20()) {
      try {
        Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
        rs.next();
        rs.close();
        rs.cancelRowUpdates();
        s.close(); 
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * cancelRowUpdates() - Should throw exception when the result set is not
   * updatable.
   **/
  public void Var015() {
    if (checkJdbc20()) {
      try {
        Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_UPDATE);
        rs.next();
        rs.cancelRowUpdates();
        rs.close(); 
        s.close(); 
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * cancelRowUpdates() - Should have no effect when cursor is not pointing to a
   * row.
   **/
  public void Var016() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, null);
        rs_.cancelRowUpdates();
        succeeded();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * cancelRowUpdates() - Should have no effect when no updates are pending.
   **/
  public void Var017() {
    StringBuffer message = new StringBuffer();
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        int columnCount = rs_.getMetaData().getColumnCount();
        Object[] before = new Object[columnCount];
        boolean[] beforeNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          before[i] = rs_.getObject(i + 1);
          beforeNulls[i] = rs_.wasNull();
        }

        rs_.cancelRowUpdates();

        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        Object[] after = new Object[columnCount];
        boolean[] afterNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          after[i] = rs2.getObject(i + 1);
          afterNulls[i] = rs2.wasNull();
        }
        rs2.close();

        boolean success = true;
        for (int i = 0; i < columnCount; ++i) {
          success = compareColumns(i, before, after, beforeNulls, afterNulls, message) && success;
        }

        assertCondition(success, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * cancelRowUpdates() - Should clear any pending updates.
   **/
  public void Var018()

  {
    StringBuffer message = new StringBuffer();
    if (checkJdbc20()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        JDRSTest.position(rs_, key_);
        int columnCount = rs_.getMetaData().getColumnCount();
        Object[] before = new Object[columnCount];
        boolean[] beforeNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          before[i] = rs_.getObject(i + 1);
          beforeNulls[i] = rs_.wasNull();
        }

        rs_.updateInt("C_INTEGER", 9836);
        rs_.updateNull("C_SMALLINT");
        rs_.updateFloat("C_FLOAT", -9.25f);
        rs_.cancelRowUpdates();
        rs_.updateRow();/* serialized */

        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        Object[] after = new Object[columnCount];
        boolean[] afterNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          after[i] = rs2.getObject(i + 1);
          afterNulls[i] = rs2.wasNull();
        }
        rs2.close();

        boolean success = true;
        for (int i = 0; i < columnCount; ++i) {
          success = compareColumns(i, before, after, beforeNulls, afterNulls, message) && success;
        }

        assertCondition(success, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * cancelRowUpdates() - Should not preclude the ability to update afterwards.
   **/
  public void Var019() {
    StringBuffer message = new StringBuffer();
    if (checkJdbc20()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        JDRSTest.position(rs_, key_);
        int columnCount = rs_.getMetaData().getColumnCount();
        Object[] before = new Object[columnCount];
        boolean[] beforeNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          before[i] = rs_.getObject(i + 1);
          beforeNulls[i] = rs_.wasNull();
        }

        rs_.updateInt("C_INTEGER", 376);
        rs_.updateNull("C_SMALLINT");
        rs_.updateFloat("C_FLOAT", -3.553f);
        rs_.cancelRowUpdates();
        rs_.updateInt("C_INTEGER", 444);
        rs_.updateNull("C_SMALLINT");
        rs_.updateFloat("C_FLOAT", 34.53f);
        rs_.updateRow();/* serialized */

        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        Object[] after = new Object[columnCount];
        boolean[] afterNulls = new boolean[columnCount];
        for (int i = 0; i < columnCount; ++i) {
          after[i] = rs2.getObject(i + 1);
          afterNulls[i] = rs2.wasNull();
        }
        int updatedColumn1 = rs2.findColumn("C_INTEGER") - 1;
        int updatedColumn2 = rs2.findColumn("C_SMALLINT") - 1;
        int updatedColumn3 = rs2.findColumn("C_FLOAT") - 1;
        rs2.close();

        boolean success = true;
        for (int i = 0; i < columnCount; ++i) {
          if (i == updatedColumn1) {
            if ((!after[i].equals(Integer.valueOf(444))) || (afterNulls[i] != false))
              success = false;
          } else if (i == updatedColumn2) {
            if ((after[i] != null) || (afterNulls[i] != true))
              success = false;
          } else if (i == updatedColumn3) {
            if ((!after[i].equals(Double.valueOf(34.53f))) || (afterNulls[i] != false))
              success = false;
          } else {
            success = compareColumns(i, before, after, beforeNulls, afterNulls, message) && success;
          }
        }

        assertCondition(success, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * updateRow() - Should throw exception when the result set is closed.
   **/
  public void Var020() {
    if (checkJdbc20()) {
      Statement s = null;
      ResultSet rs = null;
      try {
        s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
        rs.next();
        rs.close();
        rs.updateRow(); /* exception */
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
      if (rs != null)
        try {
          rs.close();
        } catch (Exception e) {
        }
      if (s != null)
        try {
          s.close();
        } catch (Exception e) {
        }
    }
  }

  /**
   * updateRow() - Don't need "for update"
   **/
  public void Var021() {
    if (checkJdbc20()) {
      ResultSet rs2 = null;
      ResultSet rs3 = null;
      Statement s = null;

      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs2 = s.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        rs2.updateShort("C_SMALLINT", (short) 654);
        rs2.updateRow();/* serialized */

        rs3 = s.executeQuery(select_);
        JDRSTest.position(rs3, key_);
        int value = rs3.getInt("C_SMALLINT");
        if (value == 654)
          succeeded();
        else
          failed("expected 654, received: " + value);

        rs2.close();
        rs2 = null;
        rs3.close();
        rs3 = null;
        s.close();
        s = null;
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }

      if (rs2 != null)
        try {
          rs2.close();
        } catch (Exception e) {
        }
      if (rs3 != null)
        try {
          rs3.close();
        } catch (Exception e) {
        }
      if (s != null)
        try {
          s.close();
        } catch (Exception e) {
        }
    }
  }

  /**
   * updateRow() - Don't need "for update"
   **/
  public void Var022() {
    if (checkJdbc20()) {
      ResultSet rs2 = null;
      ResultSet rs3 = null;
      PreparedStatement ps = null;

      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        ps = connection_.prepareStatement("SELECT * FROM " + JDRSTest.RSTEST_UPDATE + " where C_SMALLINT = ?",
            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ps.setShort(1, (short) 654);
        rs2 = ps.executeQuery();
        rs2.next();
        rs2.updateShort("C_SMALLINT", (short) 444);
        rs2.updateRow(); /* serialized */

        rs3 = statement2_.executeQuery(select_ + " where C_SMALLINT = 444");
        if (rs3.next())
          succeeded();
        else
          failed("no row found");

        rs2.close();
        rs2 = null;
        rs3.close();
        rs3 = null;
        ps.close();
        ps = null;
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }

      if (rs2 != null)
        try {
          rs2.close();
        } catch (Exception e) {
        }
      if (rs3 != null)
        try {
          rs3.close();
        } catch (Exception e) {
        }
      if (ps != null)
        try {
          ps.close();
        } catch (Exception e) {
        }
    }
  }

  /**
   * updateRow() - Using a subquery //@E1A
   **/
  public void Var023() {
    if (checkJdbc20()) {
      try {
        Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = s.executeQuery(
            "SELECT * FROM " + table1 + " WHERE SSN IS NOT NULL AND SSN NOT IN (SELECT SSN FROM " + table2 + ")");
        String before = null;
        String after = null;
        String afterRowUpdate = null;
        if (rs.next()) {
          before = rs.getString(1);
          rs.updateString(1, "369258147");
          after = rs.getString(1);
        }
        rs.updateRow(); /* unique table */
        rs.last();
        int numRows = rs.getRow();
        rs.close();
        rs = s.executeQuery(
            "SELECT * FROM " + table1 + " WHERE SSN IS NOT NULL AND SSN NOT IN (SELECT SSN FROM " + table2 + ")");
        if (rs.next())
          afterRowUpdate = rs.getString(1);
        rs.close();
        s.close();

        assertCondition("123456789".equals(before) && "369258147".equals(after) && "369258147".equals(afterRowUpdate)
            && numRows == 2);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateRow() - Using column alias with "extended metadata" connection property
   * set to true //@E3A
   **/
  public void Var024() {

    if (checkJdbc20()) {

      String url = baseURL_

          + ";extended metadata=true";
      Connection c = null;
      String city = "";
      try {

        c = testDriver_.getConnection(url, systemObject_.getUserId(), encryptedPassword_);

        Statement s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = s.executeQuery("SELECT CITY AS TOWN FROM " + table3);

        if (rs.next()) {
          rs.updateString(1, "Manhatten");
          rs.updateRow(); /* unique table */
          rs.close();
          ResultSet rs1 = s.executeQuery("SELECT * FROM " + table3);
          rs1.next();
          city = rs1.getString(1);
          rs1.close();
        } else {
          rs.close(); 
        }

        s.close();
        assertCondition(city.equals("Manhatten"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception-New Testcase added by toolbox to test column alias support 9/17/03");
      } finally {
        try {
          if (c != null) {
            c.close();
          }
        } catch (Exception e) {
        }
      }
    }
  }

  /**
   * updateRow() - Using column alias with "extended metadata" connection property
   * set to false //@E3A
   **/
  public void Var025() {
    if (checkJdbc20()) {
      String url = baseURL_

          + ";extended metadata=false";
      // String city = "";
      Connection c = null;
      try {
        c = testDriver_.getConnection(url, systemObject_.getUserId(), encryptedPassword_);
        Statement s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = s.executeQuery("SELECT CITY AS TOWN FROM " + table3);

        if (rs.next()) {
          rs.updateString(1, "Manhatten");
          rs.updateRow(); /* exception */
        }
        rs.close(); 
        s.close(); 
        failed("Didn't throw SQLException - New Testcase added by toolbox to test column alias support 9/17/03");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } finally {
        try {
          if (c != null) {
            c.close();
          }
        } catch (Exception e) {
        }
      }
    }
  }

  /**
   * updateRow() - Using column alias with "extended metadata" connection property
   * set to true //@E3A
   **/
  public void Var026() {

    if (checkJdbc20()) {
      String url = baseURL_

          + ";extended metadata=true";
      String city = "";
      String state = "";
      Connection c = null;
      try {
        c = testDriver_.getConnection(url, systemObject_.getUserId(), encryptedPassword_);
        Statement s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = s.executeQuery("SELECT CITY AS TOWN,STATE AS LOCATION FROM " + table3);

        if (rs.next()) {
          rs.updateString(1, "Rochester");
          rs.updateString(2, "MN");
          rs.updateRow(); /* unique table */
          rs.close();
          ResultSet rs1 = s.executeQuery("SELECT * FROM " + table3);
          rs1.next();
          city = rs1.getString(1);
          state = rs1.getString(2);
          rs1.close();
        } else {
          rs.close(); 
        }

        s.close();
        c.close();
        assertCondition(city.equals("Rochester") && state.equals("MN"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception-New Testcase added by toolbox to test column alias support 9/17/03");
      } finally {
        try {
          if (c != null) {
            c.close();
          }
        } catch (Exception e) {
        }
      }
    }
  }
}
