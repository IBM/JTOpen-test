///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobClob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Lob;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

/**
 * Testcase JDLobClob. This tests the following method of the JDBC Clob class:
 * 
 * <ul>
 * <li>getAsciiStream()
 * <li>getCharacterStream()
 * <li>getSubString()
 * <li>length()
 * <li>position()
 * <li>setString() //@C1A
 * <li>setAsciiStream() //@C1A
 * <li>setCharacterStream() //@C1A
 * <li>truncate() //@C1A
 * </ul>
 **/
public class JDLobClob extends JDTestcase {

  // Private data.
  private Statement statement1_;
  private Statement statement2_;
  private ResultSet rs_;
  StringBuffer sb = new StringBuffer();

  private ResultSet rs2_; // @C1A
  public static String TABLE_ = JDLobTest.COLLECTION + ".CLOB";
  public static final String MEDIUM_ = "A really big object.";
  public static String LARGE_ = null; // final
  public static final int WIDTH_ = 30000;
  public static String SMALL_ = "A really big object.";

  public static String row2_ = null;

  /**
   * Static initializer.
   **/
  static {
    StringBuffer buffer = new StringBuffer(WIDTH_);
    for (int i = 1; i <= WIDTH_; ++i)
      buffer.append("&");
    LARGE_ = buffer.toString();
  }

  /**
   * Constructor.
   **/
  public JDLobClob(AS400 systemObject, Hashtable<String, Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, String password) {
    super(systemObject, "JDLobClob", namesAndVars, runMode, fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {
    TABLE_ = JDLobTest.COLLECTION + ".JDLCCLOB";
    if (isJdbc20()) {
      String url = baseURL_;
      connection_ = testDriver_.getConnection(url, systemObject_.getUserId(), encryptedPassword_);

      statement1_ = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); // @C2C

      statement2_ = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); // @C2C

      initTable(statement1_, TABLE_, "(C_VARCHAR VARCHAR(" + WIDTH_ + "))");

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + TABLE_ + " (C_VARCHAR) VALUES (?)");
      ps.setString(1, "");
      ps.executeUpdate();
      ps.setString(1, MEDIUM_);
      row2_ = MEDIUM_;
      ps.executeUpdate();
      ps.setString(1, LARGE_);
      ps.executeUpdate();
      ps.setString(1, SMALL_);
      ps.executeUpdate();
      ps.close();

      rs_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);
    }

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (isJdbc20()) {
      statement2_.close();
      cleanupTable(statement1_, TABLE_);
      statement1_.close();
      connection_.close();
      connection_ = null; 

    }
  }

  /**
   * getAsciiStream() - When the lob is empty.
   **/
  public void Var001() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        InputStream v = clob.getAsciiStream();

        sb.setLength(0);
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K9
            true) // @K9
          assertCondition(compareBeginsWithBytes(v, "".getBytes("8859_1"), sb), sb); // @K9
        else // @K9
          assertCondition(compare(v, "", "8859_1", sb), sb);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * getAsciiStream() - When the lob is not empty.
   **/
  public void Var002() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        InputStream v = clob.getAsciiStream();
        sb.setLength(0);
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K9
            true) // @K9
          assertCondition(compareBeginsWithBytes(v, MEDIUM_.getBytes("8859_1"), sb), sb); // @K9
        else // @K9
          assertCondition(compare(v, MEDIUM_, "8859_1", sb), sb);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * getAsciiStream() - When the lob is full.
   **/
  public void Var003() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(3);
        Clob clob = rs_.getClob("C_VARCHAR");
        InputStream v = clob.getAsciiStream();
        sb.setLength(0);
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K9
            true) // @K9
          assertCondition(compareBeginsWithBytes(v, LARGE_.getBytes("8859_1"), sb), sb); // @K9
        else // @K9
          assertCondition(compare(v, LARGE_, "8859_1", sb), sb);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - When the lob is empty.
   **/
  public void Var004() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        Reader v = clob.getCharacterStream();
        sb.setLength(0);
        assertCondition(compare(v, "", sb), sb);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - When the lob is not empty.
   **/
  public void Var005() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        Reader v = clob.getCharacterStream();
        sb.setLength(0);
        assertCondition(compare(v, MEDIUM_, sb), sb);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - When the lob is full.
   **/
  public void Var006() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(3);
        Clob clob = rs_.getClob("C_VARCHAR");
        Reader v = clob.getCharacterStream();
        sb.setLength(0);
        assertCondition(compare(v, LARGE_, sb), sb);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when start is less than 0.
   **/
  public void Var007() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        clob.getSubString(-1, 5);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when start is 0.
   **/
  public void Var008() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        clob.getSubString(0, 5);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when start is greater than the
   * length of the lob.
   * 
   * @K6: Native driver extracts with padding for specified length exceeding
   *      actual length but within limits of maxLength
   **/
  public void Var009() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        // 10/15/2013
        // Toolbox now allows this boundary condition
        String output = clob.getSubString(MEDIUM_.length() + 1, 5);
        assertCondition("".equals(output), "Got '" + output + "' sb '' -- updated 10/15/2013");
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception -- Updated 10/02/2013  should not throw expection for requesting at length+1");
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when length is less than 0.
   **/
  public void Var010() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        clob.getSubString(1, -1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when length extends past the end
   * of the lob.
   * 
   * @K6: Native driver extracts with padding for specified length exceeding
   *      actual length but within limits of maxLength
   **/
  public void Var011() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        clob.getSubString(4, MEDIUM_.length() - 2);
        succeeded(); // @K6
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception -- Updated 10/02/2013  should not throw exception for requesting too much");
      }
    }
  }

  /**
   * getSubString() - Should throw an exception on an empty lob.
   * 
   * @CRS - This variation should succeed.
   **/
  public void Var012() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        String v = clob.getSubString(1, 0);
//                failed ("Didn't throw SQLException");                           // @B1C
        assertCondition("".equals(v), "Expected empty string but got '" + v + "'");
      } // @B1C
      catch (Exception e) { // @B1C
//                assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @B1C
        failed(connection_, e, "Unexpected exception");
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve all of a non-empty lob.
   **/
  public void Var013() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        String v = clob.getSubString(1, MEDIUM_.length());
        assertCondition(v.equals(MEDIUM_));
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve all of a full lob.
   **/
  public void Var014() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(3);
        Clob clob = rs_.getClob("C_VARCHAR");
        String v = clob.getSubString(1, WIDTH_);
        assertCondition(v.equals(LARGE_));
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve the first part of a non-empty lob.
   **/
  public void Var015() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        String v = clob.getSubString(1, 6);
        assertCondition(v.equals(MEDIUM_.substring(0, 6)));
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve the middle part of a non-empty lob.
   **/
  public void Var016() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        String v = clob.getSubString(6, 7);
        assertCondition(v.equals(MEDIUM_.substring(5, 12)));
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve the last part of a non-empty lob.
   **/
  public void Var017() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        String v = clob.getSubString(10, MEDIUM_.length() - 9);
        assertCondition(v.equals(MEDIUM_.substring(9)));
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * length() - When the lob is empty.
   **/
  public void Var018() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        assertCondition(clob.length() == 0);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * length() - When the lob is not empty.
   **/
  public void Var019() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        assertCondition(clob.length() == MEDIUM_.length());
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * length() - When the lob is full.
   **/
  public void Var020() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(3);
        Clob clob = rs_.getClob("C_VARCHAR");
        assertCondition(clob.length() == LARGE_.length());
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(String,long) - Should throw an exception when the pattern is null.
   **/
  public void Var021() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        clob.position((String) null, (long) 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * position(String,long) - Should throw an exception when start is less than 0.
   **/
  public void Var022() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        clob.position("Test", (long) -1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * position(String,long) - Should throw an exception when start is 0.
   **/
  public void Var023() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        clob.position("Test", (long) 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * position(String,long) - Should throw an exception when start is greater than
   * the length of the lob.
   **/
  public void Var024() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        clob.position("Test", (long) MEDIUM_.length() + 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * position(String,long) - Should return -1 when the pattern is not found at
   * all.
   **/
  public void Var025() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position("Test", (long) 1);
        assertCondition(v == -1, "position=" + v + " sb -1");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(String,long) - Should return -1 when the pattern is not found after
   * the starting position, although it does appear before the starting position.
   **/
  public void Var026() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position("eal", (long) 11);
        assertCondition(v == -1, "position=" + v + " sb -1");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(String,long) - Should return 0 when the pattern is found at the
   * beginning of the lob.
   **/
  public void Var027() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position("A reall", (long) 1);
        assertCondition(v == 1, "v = " + v + " SB 1");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(String,long) - Should return the position when the pattern is found
   * in the middle of the lob, and start is 1.
   **/
  public void Var028() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position("big", (long) 1);
        assertCondition(v == 10, "v = " + v + " SB 10");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(String,long) - Should return the position when the pattern is found
   * in the middle of the lob, and start is before where the pattern appears.
   **/
  public void Var029() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position("big ", (long) 7);
        assertCondition(v == 10, "v = " + v + " SB 10");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }

  }

  /**
   * position(String,long) - Should return the position when the pattern is found
   * in the middle of the lob, and start is right where the pattern appears.
   **/
  public void Var030() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(" big", (long) 9);
        assertCondition(v == 9, "v = " + v + " SB 9");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }

  }

  /**
   * position(String,long) - Should return the position when the pattern is found
   * at the end of the lob, and start is 1.
   **/
  public void Var031() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position("ject.", (long) 1);
        assertCondition(v == 16, "v = " + v + " SB 16");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }

  }

  /**
   * position(String,long) - Should return the position when the pattern is found
   * at the end of the lob, and start is right where the pattern occurs.
   **/
  public void Var032() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(".", (long) 20);
        assertCondition(v == 20, "v = " + v + " SB 20");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(String,long) - Should return 0 when the pattern is the empty string
   * and start is 1.
   **/
  public void Var033() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position("", (long) 1);
        assertCondition(v == 1, "v = " + v + " SB 1");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(String,long) - Should return start when the pattern is the empty
   * string and start is in the middle.
   **/
  public void Var034() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position("", (long) 9);
        assertCondition(v == 9, "v = " + v + " SB 9");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(String,long) - Should return length-1 when the pattern is the empty
   * string and start is the length-1.
   **/
  public void Var035() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position("", (long) MEDIUM_.length());
        assertCondition(v == (long) MEDIUM_.length(), "v = " + v + " " + (long) MEDIUM_.length());
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(Clob,long) - Should throw an exception when the pattern is null.
   **/
  public void Var036() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        clob.position((Clob) null, (long) 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * position(Clob,long) - Should throw an exception when start is less than 0.
   **/
  public void Var037() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        clob.position(new JDLobTest.JDTestClob("Test"), (long) -1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * position(Clob,long) - Should throw an exception when start is 0.
   **/
  public void Var038() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        clob.position(new JDLobTest.JDTestClob("Test"), (long) 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * position(Clob,long) - Should throw an exception when start is greater than
   * the length of the lob.
   **/
  public void Var039() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        clob.position(new JDLobTest.JDTestClob("Test"), (long) MEDIUM_.length() + 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * position(Clob,long) - Should return -1 when the pattern is not found at all.
   **/
  public void Var040() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(new JDLobTest.JDTestClob("This is only a test"), (long) 1);
        assertCondition(v == -1);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(Clob,long) - Should return -1 when the pattern is not found after
   * the starting position, although it does appear before the starting position.
   **/
  public void Var041() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(new JDLobTest.JDTestClob("ally"), (long) 12);
        assertCondition(v == -1);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(Clob,long) - Should return 0 when the pattern is found at the
   * beginning of the lob.
   **/
  public void Var042() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(new JDLobTest.JDTestClob("A really big"), (long) 1);
        assertCondition(v == 1, "v = " + v + " SB 1");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found in
   * the middle of the lob, and start is 1.
   **/
  public void Var043() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(new JDLobTest.JDTestClob("bje"), (long) 1);
        assertCondition(v == 15, "v = " + v + " SB 15");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found in
   * the middle of the lob, and start is before where the pattern appears.
   **/
  public void Var044() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(new JDLobTest.JDTestClob("big "), (long) 7);
        assertCondition(v == 10, "v = " + v + " SB 10");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found in
   * the middle of the lob, and start is right where the pattern appears.
   **/
  public void Var045() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(new JDLobTest.JDTestClob(" big"), (long) 9);
        assertCondition(v == 9, "v = " + v + " SB 9");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found at
   * the end of the lob, and start is 1.
   **/
  public void Var046() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(new JDLobTest.JDTestClob("."), (long) 1);
        assertCondition(v == 20, "v = " + v + " SB 20");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found at
   * the end of the lob, and start is right where the pattern occurs.
   **/
  public void Var047() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(new JDLobTest.JDTestClob("ect."), (long) 15);
        assertCondition(v == 17, "v = " + v + " SB 17");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(Clob,long) - Should return 0 when the pattern is the empty lob and
   * start is 1.
   **/
  public void Var048() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(new JDLobTest.JDTestClob(""), (long) 1);
        assertCondition(v == 1, "v = " + v + " SB 1");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(Clob,long) - Should return start when the pattern is the empty lob
   * and start is in the middle.
   **/
  public void Var049() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(new JDLobTest.JDTestClob(""), (long) 9);
        assertCondition(v == 9, "v = " + v + " SB 9");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * position(Clob,long) - Should return length-1 when the pattern is the empty
   * lob and start is the length-1.
   **/
  public void Var050() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(2);
        Clob clob = rs_.getClob("C_VARCHAR");
        long v = clob.position(new JDLobTest.JDTestClob(""), (long) MEDIUM_.length());
        assertCondition(v == (long) MEDIUM_.length(), "v = " + v + " " + (long) MEDIUM_.length());
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Should throw an exception when start is less than
   * 0.
   **/
  public void Var051() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.setString(-1, "Really small objects");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString(long, String) - Should throw an exception when start is 0.
   **/
  public void Var052() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.setString(0, "Really small object.");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString(long, String) - Should throw an exception when start is greater
   * than the length of the lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the max
   *      column size will it be an exception.
   **/
  public void Var053() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.setString(MEDIUM_.length() + 1, "Really round objects");
//                failed ("Didn't throw SQLException");                                   
        succeeded();
      } catch (Exception e) {
//                try{                                                                    
//                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");           
//                    rs2_.close();                                                       
//                }
//                catch (Exception s) {                                                   
        failed(e, "Unexpected Exception");
//                }
      }
    }
  }

  // @C2C Changed to not expect an exception
  /**
   * setString(long, String) - Changed to not expect an exception when length of
   * string to insert is greater than length of lob.
   **/
  public void Var054() {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        String expected = new String("Really really small objects");
        Clob clob = rs2_.getClob("C_VARCHAR");
        int written = clob.setString(1, expected);
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        String output = clob1.getSubString(1, expected.length());
        assertCondition(output.equals(expected) && written == expected.length(), "output(" + output + ") != expected("
            + expected + ") or written(" + written + ") != expected.length(" + expected.length() + ")");
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Should throw an exception on an empty lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the max
   *      column size will it be an exception.
   **/
  public void Var055() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(1);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.setString(1, "Really small objects");
//                failed ("Didn't throw SQLException");                                   
        succeeded();
      } catch (Exception e) {
//                try{                                                                    
//                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");           
//                    rs2_.close();                                                       
//                }
//                catch (Exception s) {                                                   
        failed(e, "Unexpected Exception");
//                }
      }
    }
  }

  /**
   * setString(long, String) - Should work to set all of a non-empty lob.
   **/
  public void Var056() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        int written = clob.setString(1, "Really huge objects.");
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        assertCondition(clob1.getSubString(1, MEDIUM_.length()).equals("Really huge objects.") && written == 20);
        rs2_.close();
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setString(long, String) - Should work to set the first part of a non-empty
   * lob.
   **/
  public void Var057() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        int written = clob.setString(1, "Really small");
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        assertCondition(clob1.getSubString(1, 12).equals("Really small") && written == 12);
        rs2_.close();
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setString(long, String) - Should work to set the middle part of a non-empty
   * lob.
   **/
  public void Var058() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        int written = clob.setString(5, "cool");
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        assertCondition(clob1.getSubString(5, 4).equals("cool") && written == 4);
        rs2_.close();
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setString(long, String) - Should work to set the last part of a non-empty
   * lob.
   **/
  public void Var059() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        int written = clob.setString(14, "insect.");
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        assertCondition(clob1.getSubString(14, 7).equals("insect.") && written == 7);
        rs2_.close();
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when start is
   * less than 0.
   **/
  public void Var060() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.setString(-1, "Really green objects", 0, 20); // @C4
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when start is
   * 0.
   **/
  public void Var061() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.setString(0, "Really shiny object.", 0, 20); // @C4
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when start is
   * greater than the length of the lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the max
   *      column size will it be an exception.
   **/
  public void Var062() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.setString(MEDIUM_.length() + 1, "Really shiny objects", 0, 20); // @C4
//                failed ("Didn't throw SQLException");                                   
        succeeded();
      } catch (Exception e) {
//                try{                                                                    
//                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");           
//                    rs2_.close();                                                       
//                }
//                catch (Exception s) {                                                   
        failed(e, "Unexpected Exception");
//                }
      }
    }
  }

  // @K2 removed +1 in assertCondition; because the parameter expected is length
  // and not the lastIndex
  // @K2 commented parts of code so that exception is not expected
  // @C2C Changed to not expect exception
  /**
   * setString(long, String, int, int) - Should not throw an exception when length
   * of string to insert is greater than length of lob.
   **/
  public void Var063() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        String expected = "Really really scary objects";
        Clob clob = rs2_.getClob("C_VARCHAR");
        int written = clob.setString(1, expected, 0, 27);// @C4
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        assertCondition(
            clob1.getSubString(1, expected.length()/* +1 */).equals(expected) && written == expected.length()); // @K2
        // failed ("Didn't throw SQLException"); @K2
      } catch (Exception e) {
//                try{                     @K2                                  
//                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");     @K2    
//                    rs2_.close();     @K2                                                 
//                }
//                catch (Exception s) {   @K2                                                 
        failed(e, "Unexpected Exception");
//               }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception on an empty
   * lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the max
   *      column size will it be an exception.
   **/
  public void Var064() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(1);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.setString(1, "Really small objects", 0, 20); // @C4
//                failed ("Didn't throw SQLException");                                   
        succeeded();
      } catch (Exception e) {
//                try{                                                                    
//                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");           
//                    rs2_.close();                                                       
//                }
//                catch (Exception s) {                                                   
        failed(e, "Unexpected Exception");
//                }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should work to set all of a non-empty
   * lob.
   **/
  public void Var065() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        int written = clob.setString(1, "Really tiny objects.", 0, 20);// @C4
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        assertCondition(clob1.getSubString(1, MEDIUM_.length()).equals("Really tiny objects.") && written == 20);
        rs2_.close();
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should work to set the first part of a
   * non-empty lob.
   **/
  public void Var066() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        int written = clob.setString(1, "Really small", 0, 12); // @C4
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        assertCondition(clob1.getSubString(1, 12).equals("Really small") && written == 12);
        rs2_.close();
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should work to set the middle part of a
   * non-empty lob.
   **/
  public void Var067() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        int written = clob.setString(5, "cool", 0, 4);// @C4
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        assertCondition(clob1.getSubString(5, 4).equals("cool") && written == 4);
        rs2_.close();
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should work to set the last part of a
   * non-empty lob.
   **/
  public void Var068() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        int written = clob.setString(14, "insect.", 0, 7); // @C4
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        assertCondition(clob1.getSubString(14, 7).equals("insect.") && written == 7);
        rs2_.close();
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * truncate() - Should throw an exception if you try to truncate to a length
   * less than 0.
   **/
  public void Var069() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.truncate(-1);
        failed("Didn't throw SQLException");
        rs2_.close();
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * truncate() - Should throw an exception if you try to truncate to a length
   * greater than the length of the lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the max
   *      column size will it be an exception.
   **/
  public void Var070() {

    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.truncate(25);
        rs2_.close();
        succeeded();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * truncate() - Should work on a non-empty lob.
   **/
  public void Var071() {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.truncate(2);
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        assertCondition(clob1.length() == 2, "length of clob = " + clob.length());
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setAsciiStream() - When the lob is not empty starting at position 1.
   **/
  public void Var072() {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        OutputStream w = clob.setAsciiStream(1);
        byte[] b = new byte[] { (byte) 1, (byte) 2, (byte) 3 };
        w.write(b);
        w.close();
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        InputStream i = clob1.getAsciiStream();
        sb.setLength(0);
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K9
            true) // @K9
          assertCondition(compareBeginsWithBytes(i, b, sb), sb); // @K9
        else // @K9
          assertCondition(compare(i, b, sb), sb); // @C3C
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setAsciiStream() - When the lob is not empty starting at a position other
   * than 1.
   **/
  public void Var073() {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        OutputStream w = clob.setAsciiStream(2);
        byte[] b = new byte[] { (byte) 4, (byte) 5, (byte) 6 };
        w.write(b);
        w.close();
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        InputStream i = clob1.getAsciiStream();
        // @C3A Must read into a new byte array because there are now 4 bytes
        // @C3A in the array since we started at position 2 this time.
        byte[] newBytes = new byte[] { (byte) 1, (byte) 4, (byte) 5, (byte) 6 }; // @C3A
        // @C3A We put (byte) 1 into the first position in testcase 72.
        sb.setLength(0);
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K9
            true) // @K9
          assertCondition(compareBeginsWithBytes(i, newBytes, sb), sb); // @K9
        else // @K9
          assertCondition(compare(i, newBytes, sb), sb); // @C3A
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setAsciiStream() - Should throw an exception if you try to set a stream that
   * starts after the lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the max
   *      column size will it be an exception.
   **/
  public void Var074() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        OutputStream w = clob.setAsciiStream(24);
//                failed("Didn't throw SQLException");                            
        rs2_.close();
        assertCondition(true, "Passed and output stream is " + w);
      } catch (Exception e) {
//                try {                                                           
//                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");   
//                    rs2_.close();                                                 
//                }
//                catch (Exception s) {                                           
        failed(e, "Unexpected Exception");
//                }
      }
    }
  }

  /**
   * setAsciiStream() - Should throw an exception if you try to set a stream that
   * starts at 0.
   **/
  public void Var075() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        OutputStream w = clob.setAsciiStream(0);
        failed("Didn't throw SQLException " + w);
        rs2_.close();
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setAsciiStream() - Should throw an exception if you try to set a stream that
   * starts before 0.
   **/
  public void Var076() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        OutputStream w = clob.setAsciiStream(-1);
        failed("Didn't throw SQLException " + w);
        rs2_.close();
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   **/
  public void Var077() {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        Writer w = clob.setCharacterStream(1);
        String cbuf = "How are you doing???";
        w.write(cbuf);
        w.close();
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        assertCondition(compare(r, "How are you doing???", sb), sb);
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at a position other
   * than 1.
   **/
  public void Var078() {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        Writer w = clob.setCharacterStream(2);
        String cbuf = "How are you today???";
        w.write(cbuf);
        w.close();
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        Reader r = clob1.getCharacterStream();
        // @C3A We started at position 2 this time, so we still have one
        // @C3A character left over from variation 77, which is a letter 'H'.
        // @C3D assertCondition ( compare( r, "How are you today???"));
        sb.setLength(0);
        assertCondition(compare(r, "HHow are you today???", sb), sb); // @C3A
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - Should throw an exception if you try to set a stream
   * that starts after the lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the max
   *      column size will it be an exception.
   **/
  public void Var079() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        Writer w = clob.setCharacterStream(24);
//                failed("Didn't throw SQLException");                            
        rs2_.close();
        assertCondition(true, "Passed, writer is " + w);
      } catch (Exception e) {
//                try {                                                           
//                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");   
//                    rs2_.close();                                                 
//                }
//                catch (Exception s) {                                           
        failed(e, "Unexpected Exception");
//                }
      }
    }
  }

  /**
   * setCharacterStream() - Should throw an exception if you try to set a stream
   * that starts at 0.
   **/
  public void Var080() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        Writer w = clob.setCharacterStream(0);
        failed("Didn't throw SQLException " + w);
        rs2_.close();
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setCharacterStream() - Should throw an exception if you try to set a stream
   * that starts before 0.
   **/
  public void Var081() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        Writer w = clob.setCharacterStream(-1);
        failed("Didn't throw SQLException " + w);
        rs2_.close();
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when string
   * offset is less than 0.
   **/
  public void Var082() // @C4
  {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.setString(MEDIUM_.length() + 1, "Really shiny objects", -1, 20); // @C4
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when the string
   * is null
   **/
  public void Var083() // @C4
  {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.setString(MEDIUM_.length() + 1, null, 0, 20); // @C4
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when string
   * offset is greater than string length.
   **/
  public void Var084() // @C4
  {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.setString(MEDIUM_.length() + 1, "Really shiny objects", "Really shiny objects".length() + 1, 20); // @C4
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  // @k1A
  /**
   * setString(long, String) - Should work to set all of a non-empty lob.
   **/
  public void Var085() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        long length1 = clob.length();
        int written = clob.setString(1, "Really huge objects.");
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "length1 = " + length1 + " SB " + length2 + " written = " + written);
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  // @k1A
  /**
   * setString(long, String) - Should work to set the first part of a non-empty
   * lob.
   **/
  public void Var086() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        long length1 = clob.length();
        int written = clob.setString(1, "Really small");
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "length1 = " + length1 + " SB " + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  // @k1A
  /**
   * setString(long, String) - Should work to set the middle part of a non-empty
   * lob.
   **/
  public void Var087() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        long length1 = clob.length();
        int written = clob.setString(5, "cool");
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "length1 = " + length1 + " SB " + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  // @K1A
  /**
   * setString(long, String) - Should work to set the last part of a non-empty
   * lob.
   **/
  public void Var088() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        long length1 = clob.length();
        int written = clob.setString(14, "insect.");
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        long length2 = clob1.length();
        assertCondition(length1 == length2, " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  // @k1A
  /**
   * setString(long, String, int, int) - Should work to set all of a non-empty
   * lob.
   **/
  public void Var089() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        long length1 = clob.length();
        int written = clob.setString(1, "Really tiny objects.", 0, 20);// @C4
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "length1 = " + length1 + " SB " + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  // @k1A
  /**
   * setString(long, String, int, int) - Should work to set the first part of a
   * non-empty lob.
   **/
  public void Var090() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        long length1 = clob.length();
        int written = clob.setString(1, "Really small", 0, 12); // @C4
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "length1 = " + length1 + " SB " + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  // @k1A
  /**
   * setString(long, String, int, int) - Should work to set the middle part of a
   * non-empty lob.
   **/
  public void Var091() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        long length1 = clob.length();
        int written = clob.setString(5, "cool", 0, 4);// @C4
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "length1 = " + length1 + " SB " + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  // @K1A
  /**
   * setString(long, String, int, int) - Should work to set the last part of a
   * non-empty lob.
   **/
  public void Var092() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        long length1 = clob.length();
        int written = clob.setString(14, "insect.", 0, 7); // @C4
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        long length2 = clob1.length();
        assertCondition(length1 == length2, " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

//@K3
  /**
   * setString(long, String, int, int) - test where offset is not 0 setString(POS
   * in Clob, STR in mind, OFFSET into str, LEN chars to be written)
   **/
  public void Var093() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(2);
        Clob clob = rs2_.getClob("C_VARCHAR");
        String str1 = clob.getSubString(14, 2);
        int written = clob.setString(14, "xyzxyz", 3, 2); // @C4
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        row2_ = row2_.substring(0, 13) + "xy" + row2_.substring(15);
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(2);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        String str2 = clob1.getSubString(14, 2);
        assertCondition(str1.equalsIgnoreCase("ob") && str2.equalsIgnoreCase("xy") && written == 2,
            "str1 = " + str1 + " str2 = " + str2 + " written = " + written + " SB 2");
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * truncate() - 0
   **/
  public void Var094() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.truncate(0);
        rs2_.updateClob("C_VARCHAR", clob); // @K4
        rs2_.updateRow(); // @K4
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_); // @K4
        rs2_.absolute(4); // @K4
        Clob clob1 = rs2_.getClob("C_VARCHAR"); // @K4
        rs2_.close(); // @K4
        assertCondition(clob1.length() == 0); // @K4
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * truncate() - 1
   **/
  public void Var095() {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_); // @K4
        rs2_.absolute(4); // @K4
        Clob clob_ = rs2_.getClob("C_VARCHAR"); // @K4
        clob_.setString(1, "ABCD"); // @K4
        rs2_.updateClob("C_VARCHAR", clob_); // @K4
        rs2_.updateRow(); // @K4
        rs2_.close(); // @K4
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.truncate(1);
        rs2_.updateClob("C_VARCHAR", clob); // @K4
        rs2_.updateRow(); // @K4
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_); // @K4
        rs2_.absolute(4); // @K4
        Clob clob1 = rs2_.getClob("C_VARCHAR"); // @K4
        rs2_.close(); // @K4
        assertCondition(clob1.length() == 1); // @K4
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1. and
   * trying to write an int
   **/
  public void Var096() // @K4: added this var
  {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        Writer w = clob.setCharacterStream(1);
        w.write(65); // ASCII value of 'A'
        w.close();
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        assertCondition(compare(r, "A", sb), clob1.getSubString(1, (int) clob1.length()) + " SB 'A' " + sb);
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1. and
   * trying to write an array of characters
   **/
  public void Var097() // @K4: added this var
  {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        Writer w = clob.setCharacterStream(1);
        char c[] = { 'I', 'T', ' ', 'W', 'O', 'R', 'K', 'S', '!' };
        w.write(c);
        w.close();
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        assertCondition(compare(r, "IT WORKS!", sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB \"IT WORKS!\" " + sb);
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1. and
   * trying to write an ARRAY OF CHARACTERS with an offset specified as 0
   **/
  public void Var098() // @K4: added this var
  {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        Writer w = clob.setCharacterStream(1);
        char c[] = { 'H', 'U', 'R', 'R', 'A', 'Y', '!' };
        w.write(c, 0, c.length);
        w.close();
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        assertCondition(compare(r, "HURRAY!S!", sb), clob1.getSubString(1, (int) clob1.length()) + " SB \"HURRAY!S!\"");
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1. and
   * trying to write an ARRAY OF CHARACTERS with an offset specified not as 0
   **/
  public void Var099() // @K4: added this var
  {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        Writer w = clob.setCharacterStream(1);
        char c[] = { 'H', 'U', 'R', 'R', 'A', 'Y', '!' };
        w.write(c, 2, c.length - 2);
        w.close();
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        sb.setLength(0);
        assertCondition(compare(r, "RRAY!Y!S!", sb), clob1.getSubString(1, (int) clob1.length()) + " SB \"RRAY!Y!S!\"");
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1. and
   * trying to write an ARRAY OF CHARACTERS with an offset specified LESS than 0
   **/
  public void Var100() // @K4: added this var
  {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        Writer w = clob.setCharacterStream(1);
        char c[] = { 'H', 'U', 'R', 'R', 'A', 'Y', '!' };
        w.write(c, -1, c.length);
        w.close();
        rs2_.close();
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) // @K5A
          failed("Didn't throw an ExtendedIllegalArgumentException"); // @K5A
        else // @K5A
          failed("Didn't throw an IndexOutOfBoundsException");
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) // @K5A
          assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException"); // @K5A
        else // @K5A
          assertExceptionIsInstanceOf(e, "java.lang.IndexOutOfBoundsException");
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1. and
   * trying to write a string with offset specified other than 0
   **/
  public void Var101() // @K4: added this var
  {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        Writer w = clob.setCharacterStream(1);
        String str = "CLOB WRITER TEST!";
        w.write(str, 5, str.length() - 5);
        w.close();
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        assertCondition(compare(r, "WRITER TEST!", sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB \"WRITER TEST!\"");
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1. and
   * trying to write a string with offset specified other than 0
   **/
  public void Var102() // @K4: added this var
  {
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        Writer w = clob.setCharacterStream(1);
        String str = "CLOB WRITER TESTING!";
        w.write(str, 1, str.length() - 2);
        w.close();
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        assertCondition(compare(r, "LOB WRITER TESTING", sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB \"LOB WRITER TESTING\"");
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when length exceeds maxLength_ of
   * clob 10/02/03 -- no longer throw exception if length is too long
   **/
  public void Var103() // @K4: added this var
  {
    if (checkJdbc20()) {
      try {
        // Reopen to pick up the update from var 93
        rs_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);
        rs_.absolute(2); // Note: 2 was updated by var 93
        Clob clob = rs_.getClob("C_VARCHAR");
        String expected = row2_;
        String outString = clob.getSubString(1, (int) Math.pow(2, 25));
        assertCondition(expected.equals(outString),
            "got '" + outString + "' sb '" + expected + "' -- updated 10/2/2103");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception should not throw exception for long length -- updated 10/2/2013");
      }
    }
  }

  /**
   * setString(long, String) - prePadding required for data if it is past the
   * length but less than the maximum possible length of the clob
   **/
  public void Var104() // @K6: added this var
  {
    if (checkUpdateableLobsSupport()) {
      try {
// Some cleaning for the testcase to work
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        clob.truncate(2);
        long l1 = clob.length();
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        clob = rs2_.getClob("C_VARCHAR");
        long written = clob.setString(5, "msp");
        rs2_.updateClob("C_VARCHAR", clob);
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        assertCondition(clob1.getSubString(3, (int) clob1.length() - 2).equals("  msp") && written == 3, "l1 = " + l1);
        rs2_.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Padding done at the end upon extra extraction??
   **/
  public void Var105() // @K6: added this var
  {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");
        long length = clob.length();
        String res = clob.getSubString(4, (int) length); // 1-based
        rs2_.close();
        String extracted = res.substring((int) length - 3, (int) length); // 0-based
//		    output_.println("length of res = "+res.length()+" sb "+length);
//		    output_.println("res is '"+extracted+"' sb 3 spaces");
        failed("Didn't throw SQLException but got " + extracted);
      } catch (Exception e) {
        // Updated 10/2/2013 -- toolbox allows extraction beyond length of lob.
        assertExceptionIsInstanceOf(e, "java.lang.StringIndexOutOfBoundsException");

      }
    }
  }

  /**
   * setString(long, String) - Incoming Data Truncation case handled ?
   **/
  public void Var106() // @K6: added this var
  {
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob = rs2_.getClob("C_VARCHAR");

        String str = "a";
        for (int i = 1; i < 32000; i *= 2)
          str += str;

        long written = clob.setString(1, str);
        rs2_.updateClob("C_VARCHAR", clob);
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
          failed("Didn't throw exception");
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_VARCHAR");
        assertCondition(clob1.length() == 30000 && written == 30000
            && clob1.getSubString(1, (int) clob1.length()).equals(str.substring(0, 30000)));
        rs2_.close();
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
          assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
        else
          failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() free() -- Shouldn't throw exception
   **/
  public void Var107() {
    if (checkJdbc40()) {

      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        JDReflectionUtil.callMethod_V(clob, "free");
        JDReflectionUtil.callMethod_V(clob, "free");
        assertCondition(true);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() length() -- Make sure another method throws an exception after free
   **/
  public void Var108() {
    if (checkJdbc40()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          clob.length();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() getAsciiStream() -- Make sure another method throws an exception after
   * free
   **/
  public void Var109() {
    if (checkJdbc40()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          clob.getAsciiStream();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() getCharacterStream() -- Make sure another method throws an exception
   * after free
   **/
  public void Var110() {
    if (checkJdbc40()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          clob.getCharacterStream();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() getSubString() -- Make sure another method throws an exception after
   * free
   **/
  public void Var111() {
    if (checkJdbc40()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          clob.getSubString(1, 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() position() -- Make sure another method throws an exception after free
   **/
  public void Var112() {
    if (checkJdbc40()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        Clob clob2 = rs_.getClob("C_VARCHAR");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          clob.position(clob2, 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() position() -- Make sure another method throws an exception after free
   **/
  public void Var113() {
    if (checkJdbc40()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          clob.position("S", 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() setAsciiStream() -- Make sure another method throws an exception after
   * free
   **/
  public void Var114() {
    if (checkJdbc40()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          clob.setAsciiStream(1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() setCharacterStream() -- Make sure another method throws an exception
   * after free
   **/
  public void Var115() {
    if (checkJdbc40()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          clob.setCharacterStream(1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() setString() -- Make sure another method throws an exception after free
   **/
  public void Var116() {
    if (checkJdbc40()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          clob.setString(1, "S");
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() setString() -- Make sure another method throws an exception after free
   **/
  public void Var117() {
    if (checkJdbc40()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          clob.setString(1, "S", 1, 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() truncate() -- Make sure another method throws an exception after free
   **/
  public void Var118() {
    if (checkJdbc40()) {
      try {
        rs_.absolute(1);
        Clob clob = rs_.getClob("C_VARCHAR");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          clob.truncate(10);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

}
