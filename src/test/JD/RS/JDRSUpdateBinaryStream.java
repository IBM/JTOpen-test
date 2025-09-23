///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateBinaryStream.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.RS;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DataTruncation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDWeirdInputStream;

/**
 * Testcase JDRSUpdateBinaryStream. This tests the following method of the JDBC
 * ResultSet class:
 * 
 * <ul>
 * <li>updateBinaryStream()
 * </ul>
 **/
public class JDRSUpdateBinaryStream extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "JDRSUpdateBinaryStream";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.JDRSTest.main(newArgs);
  }

  // Private data.
  private static final String key_ = "JDRSUpdateBinaryStream";
  private static String select_ = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;

  private Statement statement_;
  private Statement statement2_;
  private ResultSet rs_;

  /**
   * Constructor.
   **/
  public JDRSUpdateBinaryStream(AS400 systemObject, Hashtable<String, Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,

      String password) {
    super(systemObject, "JDRSUpdateBinaryStream", namesAndVars, runMode, fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {
    if (connection_ != null)
      connection_.close();
    select_ = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;

    if (isJdbc20()) {
      // SQL400 - driver neutral...
      String url = baseURL_
          // String url = "jdbc:as400://" + systemObject_.getSystemName()

          + ";data truncation=true";
      connection_ = testDriver_.getConnection(url, systemObject_.getUserId(), encryptedPassword_);
      connection_.setAutoCommit(false); // @C1A
      statement_ = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      statement2_ = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

      statement_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('DUMMY_ROW')");
      statement_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('" + key_ + "')");

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
      statement_.close();
      connection_.commit(); // @C1A
      connection_.close();
    }
  }

  /**
   * updateBinaryStream() - Should throw exception when the result set is closed.
   **/
  public void Var001() {
    if (checkJdbc20()) {
      try {
        Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
        rs.next();
        rs.close();
        rs.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(new byte[] { (byte) 12, (byte) 34 }), 2);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Should throw exception when the result set is not
   * updatable.
   **/
  public void Var002() {
    if (checkJdbc20()) {
      try {
        Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_UPDATE);
        rs.next();
        rs.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(new byte[] { (byte) -12, (byte) -34 }), 2);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Should throw exception when cursor is not pointing to
   * a row.
   **/
  public void Var003() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, null);
        rs_.updateBinaryStream("C_VARBINARY_20",
            new ByteArrayInputStream(new byte[] { (byte) 12, (byte) 34, (byte) 98 }), 3);
        rs_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Should throw an exception when the column is an
   * invalid index.
   **/
  public void Var004() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream(100, new ByteArrayInputStream(new byte[] { (byte) 12, (byte) 34, (byte) 98 }), 3);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Should throw an exception when the column is 0.
   **/
  public void Var005() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream(0, new ByteArrayInputStream(new byte[] { (byte) 10, (byte) 0, (byte) 98 }), 3);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Should throw an exception when the column is -1.
   **/
  public void Var006() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream(-1, new ByteArrayInputStream(new byte[] { (byte) 0 }), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Should work when the column index is valid.
   **/
  public void Var007() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 };
        rs_.updateBinaryStream(18, new ByteArrayInputStream(ba), ba.length);
        rs_.updateRow();
        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        byte[] v = rs2.getBytes("C_VARBINARY_20");
        rs2.close();
        assertCondition(areEqual(v, ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Should throw an exception when the column name is
   * null.
   **/
  public void Var008() {
    if (checkJdbc20()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC throws null pointer exception when column name is null ");
      } else {

        try {
          JDRSTest.position(rs_, key_);
          rs_.updateBinaryStream(null, new ByteArrayInputStream(new byte[] { (byte) 12, (byte) 34, (byte) 98 }), 3);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateBinaryStream() - Should throw an exception when the column name is an
   * empty string.
   **/
  public void Var009() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("", new ByteArrayInputStream(new byte[] { (byte) 12, (byte) 34, (byte) 98 }), 3);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Should throw an exception when the column name is
   * invalid.
   **/
  public void Var010() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("INVALID", new ByteArrayInputStream(new byte[] { (byte) 12, (byte) 34, (byte) 98 }), 3);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Should work when the column name is valid.
   **/
  public void Var011() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) -4, (byte) 98, (byte) 99 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), 3);
        rs_.updateRow();
        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        byte[] v = rs2.getBytes("C_VARBINARY_20");
        rs2.close();
        assertCondition(areEqual(v, ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Should update to SQL NULL when the column value is
   * null.
   **/
  public void Var012() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_VARBINARY_20", null, 0);
        rs_.updateRow();
        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        byte[] v = rs2.getBytes("C_VARBINARY_20");
        boolean wn = rs2.wasNull();
        rs2.close();
        assertCondition((v == null) && (wn == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Should throw an exception when the length is invalid.
   **/
  public void Var013() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_VARBINARY_20",
            new ByteArrayInputStream(new byte[] { (byte) 12, (byte) 34, (byte) 98 }), -1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Should be reflected by get, even if update has not yet
   * been issued (i.e. update is still pending).
   **/
  public void Var014() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) 0, (byte) 1, (byte) -1, (byte) 2, (byte) -2 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), ba.length);
        assertCondition(areEqual(rs_.getBytes("C_VARBINARY_20"), ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Should be reflected by get, after update has been
   * issued, but cursor has not been repositioned.
   **/
  public void Var015() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) 0, (byte) 56, (byte) 1, (byte) -1, (byte) 2, (byte) -2 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), ba.length);
        rs_.updateRow();
        assertCondition(areEqual(rs_.getBytes("C_VARBINARY_20"), ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Should be reflected by get, after update has been
   * issued and cursor has been repositioned.
   **/
  public void Var016() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) 0, (byte) 56, (byte) 1, (byte) -1, (byte) 2, (byte) -2 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), ba.length);
        rs_.updateRow();
        rs_.beforeFirst();
        JDRSTest.position(rs_, key_);
        byte[] v = rs_.getBytes("C_VARBINARY_20");
        assertCondition(areEqual(ba, v));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Should work when the current row is the insert row.
   **/
  public void Var017() {
    if (checkJdbc20()) {
      try {
        rs_.moveToInsertRow();
        rs_.updateString("C_KEY", "JDRSUpdateBinaryStream 1");
        byte[] ba = new byte[] { (byte) 121, (byte) 0, (byte) 56, (byte) 1, (byte) -1, (byte) 2, (byte) -2 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), ba.length);
        rs_.insertRow();
        JDRSTest.position(rs_, "JDRSUpdateBinaryStream 1");
        assertCondition(areEqual(rs_.getBytes("C_VARBINARY_20"), ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Should be reflected by get on insert row, even if
   * insert has not yet been issued (i.e. insert is still pending).
   **/
  public void Var018() {
    if (checkJdbc20()) {
      try {
        rs_.moveToInsertRow();
        byte[] ba = new byte[] { (byte) 121, (byte) 0, (byte) 56, (byte) -1, (byte) 2, (byte) -2 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), ba.length);
        assertCondition(areEqual(rs_.getBytes("C_VARBINARY_20"), ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Should throw an exception on a deleted row.
   **/
  public void Var019() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, "DUMMY_ROW");
        rs_.deleteRow();
        byte[] ba = new byte[] { (byte) 121, (byte) 0, (byte) 2, (byte) -2 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), ba.length);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a SMALLINT.
   **/
  public void Var020() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_SMALLINT", new ByteArrayInputStream(new byte[] { (byte) 5 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update an INTEGER.
   **/
  public void Var021() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_INTEGER", new ByteArrayInputStream(new byte[] { (byte) 50 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a REAL.
   **/
  public void Var022() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_REAL", new ByteArrayInputStream(new byte[] { (byte) 12 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a FLOAT.
   **/
  public void Var023() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_FLOAT", new ByteArrayInputStream(new byte[] { (byte) 47 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a DOUBLE.
   **/
  public void Var024() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_DOUBLE", new ByteArrayInputStream(new byte[] { (byte) 65 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a DECIMAL.
   **/
  public void Var025() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_DECIMAL_105", new ByteArrayInputStream(new byte[] { (byte) 7 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a NUMERIC.
   **/
  public void Var026() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_NUMERIC_105", new ByteArrayInputStream(new byte[] { (byte) 88 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a CHAR.
   **/
  public void Var027() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_CHAR_50", new ByteArrayInputStream(new byte[] { (byte) 65 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a VARCHAR.
   **/
  public void Var028() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_VARCHAR_50", new ByteArrayInputStream(new byte[] { (byte) 65 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a BINARY.
   **/
  public void Var029() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) 44, (byte) 86, (byte) -1, (byte) 10, (byte) 20, (byte) 23, (byte) 77, (byte) 10,
            (byte) -1, (byte) 46, (byte) 11, (byte) 22, (byte) 43, (byte) 98, (byte) -6, (byte) 11, (byte) 11, (byte) 0,
            (byte) 0, (byte) 100 };
        rs_.updateBinaryStream("C_BINARY_20", new ByteArrayInputStream(ba), ba.length);
        rs_.updateRow();
        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        byte[] v = rs2.getBytes("C_BINARY_20");
        rs2.close();
        assertCondition(areEqual(v, ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a BINARY, when the value is too big.
   **/
  public void Var030() {
    if (checkJdbc20()) {
      int expectedColumn = -1;
      try {
        JDRSTest.position(rs_, key_);
        expectedColumn = rs_.findColumn("C_BINARY_1");
        rs_.updateBinaryStream("C_BINARY_1", new ByteArrayInputStream(new byte[] { (byte) 54, (byte) 64, (byte) 0 }),
            3);
        rs_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        DataTruncation dt = (DataTruncation) e;
        assertCondition((dt.getIndex() == expectedColumn) && (dt.getParameter() == false) && (dt.getRead() == false)
            && (dt.getDataSize() == 3) && (dt.getTransferSize() == 1));

      }
    }
  }

  /**
   * updateBinaryStream() - Update a VARBINARY with a "normal" binary stream, and
   * the length equal to the full stream.
   **/
  public void Var031() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) -1, (byte) 86, (byte) 11, (byte) 0, (byte) 0, (byte) 100 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), ba.length);
        rs_.updateRow();
        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        byte[] v = rs2.getBytes("C_VARBINARY_20");
        rs2.close();
        assertCondition(areEqual(v, ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a VARBINARY with a "normal" binary stream, and
   * the length less than the full stream.
   **/
  public void Var032() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) 7, (byte) -1, (byte) 86, (byte) 11, (byte) 0, (byte) 0, (byte) 100 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), 3);
        byte[] expected = new byte[] { (byte) 7, (byte) -1, (byte) 86 };

        byte[] afterUpdate = rs_.getBytes("C_VARBINARY_20");
        assertCondition(areEqual(afterUpdate, expected));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a VARBINARY with a "normal" binary stream, and
   * the length greater than the full stream.
   **/
  public void Var033() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) 12, (byte) -1, (byte) 86, (byte) 0, (byte) 0, (byte) 100 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), 15);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a VARBINARY with a "normal" binary stream, and
   * the length 1.
   **/
  public void Var034() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) 56, (byte) -1, (byte) 68, (byte) 5, (byte) 11, (byte) 0, (byte) 0, (byte) 100 };
        byte[] expected = new byte[] { (byte) 56 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), 1);

        byte[] afterUpdate = rs_.getBytes("C_VARBINARY_20"); // allow updateBinaryStream if length is less than stream
                                                             // length
        assertCondition(areEqual(afterUpdate, expected));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a VARBINARY with a "normal" binary stream, and
   * the length 0.
   **/
  public void Var035() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) -1, (byte) 68, (byte) 5, (byte) 11, (byte) 0, (byte) 0, (byte) 100 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), 0);

        byte[] afterUpdate = rs_.getBytes("C_VARBINARY_20"); // allow updateBinaryStream if length is less than stream
                                                             // length
        assertCondition(afterUpdate.length == 0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a VARBINARY, with an empty array.
   **/
  public void Var036() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[0];
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), 0);
        rs_.updateRow();
        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        byte[] v = rs2.getBytes("C_VARBINARY_20");
        rs2.close();
        assertCondition(areEqual(v, ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a VARBINARY, with single element array.
   **/
  public void Var037() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) 0 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), 1);
        rs_.updateRow();
        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        byte[] v = rs2.getBytes("C_VARBINARY_20");
        rs2.close();
        assertCondition(areEqual(v, ba));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a VARBINARY, when the value is too big.
   **/
  public void Var038() {
    if (checkJdbc20()) {
      int expectedColumn = -1;
      try {
        JDRSTest.position(rs_, key_);
        expectedColumn = rs_.findColumn("C_VARBINARY_20");
        byte[] ba = new byte[] { (byte) 44, (byte) 86, (byte) -1, (byte) 10, (byte) 20, (byte) 23, (byte) 77, (byte) 10,
            (byte) -1, (byte) 46, (byte) 11, (byte) 22, (byte) 43, (byte) 98, (byte) -6, (byte) -23, (byte) 77,
            (byte) 10, (byte) -1, (byte) 46, (byte) 23, (byte) 77, (byte) 10, (byte) -1, (byte) 46, (byte) 11,
            (byte) 11, (byte) 0, (byte) 0, (byte) 100 };
        rs_.updateBinaryStream("C_VARBINARY_20", new ByteArrayInputStream(ba), ba.length);
        rs_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        DataTruncation dt = (DataTruncation) e;
        assertCondition((dt.getIndex() == expectedColumn) && (dt.getParameter() == false) && (dt.getRead() == false)
            && (dt.getDataSize() == 30) && (dt.getTransferSize() == 20));

      }
    }

  }

  /**
   * updateBinaryStream() - Update a VARBINARY parameter to a bad input stream.
   **/
  public void Var039() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);

        class BadInputStream extends InputStream {

          public BadInputStream() {
            super();
          }

          public int available() throws IOException {
            throw new IOException();
          };

          public int read() throws IOException {
            throw new IOException();
          };

          public int read(byte[] buffer) throws IOException {
            throw new IOException();
          };
        }

        InputStream r = new BadInputStream();
        rs_.updateBinaryStream("C_VARBINARY_20", r, 2);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a CLOB.
   **/
  public void Var040() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          JDRSTest.position(rs_, key_);
          rs_.updateBinaryStream("C_CLOB", new ByteArrayInputStream(new byte[] { (byte) 56, (byte) 98 }), 2);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateBinaryStream() - Update a DBCLOB.
   **/
  public void Var041() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          JDRSTest.position(rs_, key_);
          rs_.updateBinaryStream("C_DBCLOB", new ByteArrayInputStream(new byte[] { (byte) -47, (byte) -98 }), 2);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateBinaryStream() - Update a BLOB.
   * 
   * SQL400 - the native driver expects this update to work correctly.
   **/
  public void Var042() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          JDRSTest.position(rs_, key_);
          byte[] ba = new byte[] { (byte) 98, (byte) 1 };
          rs_.updateBinaryStream("C_BLOB", new ByteArrayInputStream(ba), 2);

          // if (getDriver() == JDTestDriver.DRIVER_NATIVE) { //@D1D
          rs_.updateRow();
          ResultSet rs2 = statement2_.executeQuery(select_);
          JDRSTest.position(rs2, key_);
          byte[] v = rs2.getBytes("C_BLOB");
          rs2.close();
          assertCondition(areEqual(v, ba));
          // } else { //@D1D
          // failed ("Didn't throw SQLException"); //@D1D
          // } //@D1D
        } catch (Exception e) {
          // if (getDriver() == JDTestDriver.DRIVER_NATIVE) { //@D1D
          failed(e, "Unexpected Exception");
          // } else { //@D1D
          // assertExceptionIsInstanceOf (e, "java.sql.SQLException"); //@D1D
          // } //@D1D
        }
      }
    }
  }

  /**
   * updateBinaryStream() - Update a DATE.
   **/
  public void Var043() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_DATE", new ByteArrayInputStream(new byte[] { (byte) 19 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a TIME.
   **/
  public void Var044() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_TIME", new ByteArrayInputStream(new byte[] { (byte) 1 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a TIMESTAMP.
   **/
  public void Var045() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_TIMESTAMP", new ByteArrayInputStream(new byte[] { (byte) 2 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateBinaryStream() - Update a DATALINK.
   **/
  public void Var046() {
    if (checkJdbc20()) {
      if (checkDatalinkSupport()) {
        // We do not test updating datalinks, since it is not
        // possible to open a updatable cursor/result set with
        // a datalink column.
        notApplicable("DATALINK update not supported.");
        /*
         * try { JDRSTest.position (rs_, key_); rs_.updateBinaryStream ("C_DATALINK",
         * new ByteArrayInputStream (new byte[] { (byte) 113 }), 1); failed
         * ("Didn't throw SQLException"); } catch (Exception e) {
         * assertExceptionIsInstanceOf (e, "java.sql.SQLException"); }
         */
      }
    }
  }

  /**
   * updateBinaryStream() - Update a DISTINCT.
   **/
  public void Var047() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          JDRSTest.position(rs_, key_);
          rs_.updateBinaryStream("C_DISTINCT", new ByteArrayInputStream(new byte[] { (byte) 44 }), 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateBinaryStream() - Update a BIGINT.
   **/
  public void Var048() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          JDRSTest.position(rs_, key_);
          rs_.updateBinaryStream("C_BIGINT", new ByteArrayInputStream(new byte[] { (byte) 50 }), 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateBinaryStream() - Should work with weird input stream
   **/
  public void Var049() {
    String added = "Added by native driver 10/11/2006 to test input stream that sometimes returns 0 bytes ";

    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36, (byte) 37 };
        InputStream is = new JDWeirdInputStream("0102030");

        // toolbox expects correct size
        try {
          rs_.updateBinaryStream(18, is, 6);
        } catch (SQLException e) {
          if (isToolboxDriver()) {
            succeeded();
            return;
          } else {
            throw e;
          }
        }
        rs_.updateRow();
        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        byte[] v = rs2.getBytes("C_VARBINARY_20");
        rs2.close();
        assertCondition(areEqual(v, ba), "Not equal " + added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + added);
      }
    }
  }

  /**
   * updateBinaryStream() - Should work with weird input stream and reading part
   **/
  public void Var050() {
    String added = "Added by native driver 10/11/2006 to test input stream that sometimes returns 0 bytes ";

    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, key_);
        byte[] ba = new byte[] { (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36 };

        InputStream is = new JDWeirdInputStream("0102030");

        // toolbox expects correct size
        try {
          rs_.updateBinaryStream(18, is, 5);
        } catch (SQLException e) {
          if (isToolboxDriver()) {
            succeeded();
            return;
          } else {
            throw e;
          }
        }
        rs_.updateRow();
        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        byte[] v = rs2.getBytes("C_VARBINARY_20");
        rs2.close();
        assertCondition(areEqual(v, ba), "Not equal " + added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + added);
      }
    }
  }

  /**
   * updateAsciiStream() - Update DFP16:
   **/
  public void Var051() {
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP16 + " FOR UPDATE ");
        rs.next();
        rs.updateBinaryStream(1, new ByteArrayInputStream(new byte[] { (byte) 50 }), 1);
        failed("Didn't throw SQLException ");
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          String message = e.getMessage();
          boolean success = (message.indexOf("Data type mismatch") >= 0);
          if (!success) {
            e.printStackTrace();
          }
          assertCondition(success, "Expected Data type mismatch exception but got " + message);
        } else {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateAsciiStream() - Update DFP34:
   **/

  public void Var052() {
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP34 + " FOR UPDATE ");
        rs.next();
        rs.updateBinaryStream(1, new ByteArrayInputStream(new byte[] { (byte) 50 }), 1);
        failed("Didn't throw SQLException ");
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          String message = e.getMessage();
          boolean success = (message.indexOf("Data type mismatch") >= 0);
          if (!success) {
            e.printStackTrace();
          }
          assertCondition(success, "Expected Data type mismatch exception but got " + message);
        } else {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateBinaryStream() - Update a Boolean
   **/
  public void Var053() {
    if (checkBooleanSupport()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateBinaryStream("C_BOOLEAN", new ByteArrayInputStream(new byte[] { (byte) 50 }), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

}
