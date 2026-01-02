///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSRSDateTime.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSRSDateTime.java
//
// Classes:      JDCSRSDateTime
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Testcase JDCSRSDateTime. This tests the following methods of the JDBC
 * ResultSet class to ensure that the date and time returned from a stored
 * procedure result set are properly formatted:
 * 
 * <ul>
 * <li>getDate()
 * <li>getTime()
 * </ul>
 */
public class JDCSRSDateTime extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "JDCSRSDateTime";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.JDCSTest.main(newArgs);
  }
  // Private data.

  private Connection connectionMDYHMS_;

  private Connection connectionDMYISO_;

  private Connection connectionYMDUSA_;

  private Connection connectionJULEUR_;

  private Connection connectionISOJIS_;

  private Connection connectionUSAUSA_;

  private Connection connectionEUREUR_;

  private Connection connectionJISJIS_;

  private CallableStatement csTypes_;

  /**
   * Constructor.
   */
  public JDCSRSDateTime(AS400 systemObject, Hashtable<String, Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, String password) {
    super(systemObject, "JDCSRSDateTime", namesAndVars, runMode, fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception If an exception occurs.
   */
  protected void setup() throws Exception {
    connection_ = testDriver_.getConnection(baseURL_ + ";errors=full", userId_, encryptedPassword_);

    // Create a Table with Date/Time
    Statement s = connection_.createStatement();
    s.execute("CREATE TABLE " + JDCSTest.COLLECTION + ".CSRSDT (DATE1 DATE, TIME1 TIME)");
    s.executeUpdate("INSERT INTO " + JDCSTest.COLLECTION + ".CSRSDT VALUES('2007-06-25', '11:15:25')");
    s.close();

    // CREATE CONNECTIONS with various date and time formats
    connectionMDYHMS_ = testDriver_.getConnection(baseURL_ + ";date format=mdy;time format=hms", userId_,
        encryptedPassword_);
    connectionDMYISO_ = testDriver_.getConnection(baseURL_ + ";date format=dmy;time format=iso", userId_,
        encryptedPassword_);
    connectionYMDUSA_ = testDriver_.getConnection(baseURL_ + ";date format=ymd;time format=usa", userId_,
        encryptedPassword_);
    connectionJULEUR_ = testDriver_.getConnection(baseURL_ + ";date format=julian;time format=eur", userId_,
        encryptedPassword_);
    connectionISOJIS_ = testDriver_.getConnection(baseURL_ + ";date format=iso;time format=jis", userId_,
        encryptedPassword_);
    connectionUSAUSA_ = testDriver_.getConnection(baseURL_ + ";date format=usa;time format=usa", userId_,
        encryptedPassword_);
    connectionEUREUR_ = testDriver_.getConnection(baseURL_ + ";date format=eur;time format=eur", userId_,
        encryptedPassword_);
    connectionJISJIS_ = testDriver_.getConnection(baseURL_ + ";date format=jis;time format=jis", userId_,
        encryptedPassword_);

    // Create stored procedures using the various connections
    String sql;
    Statement s1 = connectionMDYHMS_.createStatement();
    sql = "create procedure " + JDCSTest.COLLECTION + ".DTMDYHMS() language sql result sets 2 " + "	begin"
        + " declare c1 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;"
        + " declare c2 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;" + " open c1;" + " open c2;"
        + " set result sets cursor c1, cursor c2;" + " end";
    try {
      s1.execute(sql);
    } catch (Exception e) {
      output_.println("Error creating: " + sql);
      e.printStackTrace();
    }
    s1.close();

    Statement s2 = connectionDMYISO_.createStatement();
    sql = "create procedure " + JDCSTest.COLLECTION + ".DTDMYISO() language sql result sets 2 " + "	begin"
        + " declare c1 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;"
        + " declare c2 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;" + " open c1;" + " open c2;"
        + " set result sets cursor c1, cursor c2;" + " end";
    try {
      s2.execute(sql);
    } catch (Exception e) {
      output_.println("Error creating: " + sql);
      e.printStackTrace();
    }
    s2.close();
    Statement s3 = connectionYMDUSA_.createStatement();
    sql = "create procedure " + JDCSTest.COLLECTION + ".DTYMDUSA() language sql result sets 2 " + "	begin"
        + " declare c1 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;"
        + " declare c2 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;" + " open c1;" + " open c2;"
        + " set result sets cursor c1, cursor c2;" + " end";
    try {
      s3.execute(sql);
    } catch (Exception e) {
      output_.println("Error creating: " + sql);
      e.printStackTrace();
    }
    s3.close();
    Statement s4 = connectionJULEUR_.createStatement();
    sql = "create procedure " + JDCSTest.COLLECTION + ".DTJULEUR() language sql result sets 2 " + "	begin"
        + " declare c1 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;"
        + " declare c2 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;" + " open c1;" + " open c2;"
        + " set result sets cursor c1, cursor c2;" + " end";
    try {
      s4.execute(sql);
    } catch (Exception e) {
      output_.println("Error creating: " + sql);
      e.printStackTrace();
    }
    s4.close();
    Statement s5 = connectionISOJIS_.createStatement();
    sql = "create procedure " + JDCSTest.COLLECTION + ".DTISOJIS() language sql result sets 2 " + "	begin"
        + " declare c1 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;"
        + " declare c2 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;" + " open c1;" + " open c2;"
        + " set result sets cursor c1, cursor c2;" + " end";
    try {
      s5.execute(sql);
    } catch (Exception e) {
      output_.println("Error creating: " + sql);
      e.printStackTrace();
    }
    s5.close();
    Statement s6 = connectionUSAUSA_.createStatement();
    sql = "create procedure " + JDCSTest.COLLECTION + ".DTUSAUSA() language sql result sets 2 " + "	begin"
        + " declare c1 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;"
        + " declare c2 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;" + " open c1;" + " open c2;"
        + " set result sets cursor c1, cursor c2;" + " end";
    try {
      s6.execute(sql);
    } catch (Exception e) {
      output_.println("Error creating: " + sql);
      e.printStackTrace();
    }
    s6.close();
    Statement s7 = connectionEUREUR_.createStatement();
    sql = "create procedure " + JDCSTest.COLLECTION + ".DTEUREUR() language sql result sets 2 " + "	begin"
        + " declare c1 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;"
        + " declare c2 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;" + " open c1;" + " open c2;"
        + " set result sets cursor c1, cursor c2;" + " end";
    try {
      s7.execute(sql);
    } catch (Exception e) {
      output_.println("Error creating: " + sql);
      e.printStackTrace();
    }
    s7.close();
    Statement s8 = connectionJISJIS_.createStatement();
    sql = "create procedure " + JDCSTest.COLLECTION + ".DTJISJIS() language sql result sets 2 " + "	begin"
        + " declare c1 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;"
        + " declare c2 cursor for select * from " + JDCSTest.COLLECTION + ".CSRSDT;" + " open c1;" + " open c2;"
        + " set result sets cursor c1, cursor c2;" + " end";
    try {
      s8.execute(sql);
    } catch (Exception e) {
      output_.println("Error creating: " + sql);
      e.printStackTrace();
    }
    s8.close();

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception If an exception occurs.
   */
  protected void cleanup() throws Exception {
    try {
      csTypes_.close();
    } catch (Exception e) {
    }
    Statement s = connection_.createStatement();
    try {
      s.execute("DROP PROCEDURE " + JDCSTest.COLLECTION + ".DTMDYHMS");
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      s.execute("DROP PROCEDURE " + JDCSTest.COLLECTION + ".DTDMYISO");
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      s.execute("DROP PROCEDURE " + JDCSTest.COLLECTION + ".DTYMDUSA");
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      s.execute("DROP PROCEDURE " + JDCSTest.COLLECTION + ".DTJULEUR");
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      s.execute("DROP PROCEDURE " + JDCSTest.COLLECTION + ".DTISOJIS");
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      s.execute("DROP PROCEDURE " + JDCSTest.COLLECTION + ".DTUSAUSA");
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      s.execute("DROP PROCEDURE " + JDCSTest.COLLECTION + ".DTEUREUR");
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      s.execute("DROP PROCEDURE " + JDCSTest.COLLECTION + ".DTJISJIS");
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      s.execute("DROP TABLE " + JDCSTest.COLLECTION + ".CSRSDT");
    } catch (Exception e) {
      e.printStackTrace();
    }
    s.close();
    connection_.close();
    connection_ = null;

    connectionMDYHMS_.close();
    connectionDMYISO_.close();
    connectionYMDUSA_.close();
    connectionJULEUR_.close();
    connectionISOJIS_.close();
    connectionUSAUSA_.close();
    connectionEUREUR_.close();
    connectionJISJIS_.close();
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy.
   */
  public void Var001() {

    try {
      String expected = "06/25/07";
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      java.sql.Date d = rs.getDate(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals(expected),
          "Got '" + date + "' sb '" + expected + "' d is '" + d + "' -- Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy.
   */
  public void Var002() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      String expected = "25/06/07";
      assertCondition(date.equals(expected),
          "got " + date + " sb " + expected + " connection=DMY STP=MDY-- Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd.
   */
  public void Var003() {
    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();

      String expected = "07/06/25";
      assertCondition(date.equals(expected),
          "got " + date + " sb " + expected + " connection=YMD STP=MDY-- Added by Toolbox 1/5/2007");

    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian.
   */
  public void Var004() {
    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();

      String expected = "07/176";
      assertCondition(date.equals(expected),
          "got " + date + " sb " + expected + " connection=JUL STP=MDY-- Added by Toolbox 1/5/2007");

    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso.
   */
  public void Var005() {
    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa.
   */
  public void Var006() {
    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur.
   */
  public void Var007() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis.
   */
  public void Var008() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy.
   */
  public void Var009() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy.
   */
  public void Var010() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd.
   */
  public void Var011() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian.
   */
  public void Var012() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso.
   */
  public void Var013() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa.
   */
  public void Var014() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur.
   */
  public void Var015() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis.
   */
  public void Var016() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy.
   */
  public void Var017() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy.
   */
  public void Var018() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd.
   */
  public void Var019() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian.
   */
  public void Var020() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso.
   */
  public void Var021() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa.
   */
  public void Var022() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur.
   */
  public void Var023() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis.
   */
  public void Var024() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy.
   */
  public void Var025() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy.
   */
  public void Var026() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd.
   */
  public void Var027() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian.
   */
  public void Var028() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso.
   */
  public void Var029() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa.
   */
  public void Var030() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur.
   */
  public void Var031() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis.
   */
  public void Var032() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy.
   */
  public void Var033() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy.
   */
  public void Var034() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd.
   */
  public void Var035() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian.
   */
  public void Var036() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso.
   */
  public void Var037() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa.
   */
  public void Var038() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur.
   */
  public void Var039() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis.
   */
  public void Var040() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy.
   */
  public void Var041() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy.
   */
  public void Var042() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd.
   */
  public void Var043() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian.
   */
  public void Var044() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso.
   */
  public void Var045() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa.
   */
  public void Var046() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur.
   */
  public void Var047() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis.
   */
  public void Var048() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy.
   */
  public void Var049() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy.
   */
  public void Var050() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd.
   */
  public void Var051() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian.
   */
  public void Var052() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso.
   */
  public void Var053() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa.
   */
  public void Var054() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur.
   */
  public void Var055() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis.
   */
  public void Var056() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy.
   */
  public void Var057() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy.
   */
  public void Var058() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd.
   */
  public void Var059() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian.
   */
  public void Var060() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso.
   */
  public void Var061() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa.
   */
  public void Var062() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur.
   */
  public void Var063() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis.
   */
  public void Var064() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms.
   */
  public void Var065() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso.
   */
  public void Var066() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var067() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var068() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var069() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var070() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var071() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var072() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms.
   */
  public void Var073() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso.
   */
  public void Var074() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var075() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var076() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var077() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var078() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var079() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var080() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms.
   */
  public void Var081() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:00"), "Time = " + time + " sb 11:15:00 Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso.
   */
  public void Var082() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var083() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var084() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var085() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var086() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var087() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var088() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms.
   */
  public void Var089() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso.
   */
  public void Var090() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var091() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var092() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var093() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var094() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var095() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var096() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms.
   */
  public void Var097() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso.
   */
  public void Var098() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var099() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var100() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var101() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var102() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var103() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var104() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms.
   */
  public void Var105() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso.
   */
  public void Var106() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var107() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var108() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var109() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var110() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var111() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var112() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms.
   */
  public void Var113() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso.
   */
  public void Var114() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var115() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var116() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var117() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var118() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var119() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var120() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms.
   */
  public void Var121() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso.
   */
  public void Var122() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var123() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var124() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var125() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa.
   */
  public void Var126() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur.
   */
  public void Var127() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis.
   */
  public void Var128() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy. Retrieve the date from the 2nd result set.
   */
  public void Var129() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy. Retrieve the date from the 2nd result set.
   */
  public void Var130() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd. Retrieve the date from the 2nd result set.
   */
  public void Var131() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian. Retrieve the date from the 2nd result set.
   */
  public void Var132() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var133() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var134() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var135() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with MDY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var136() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy. Retrieve the date from the 2nd result set.
   */
  public void Var137() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy. Retrieve the date from the 2nd result set.
   */
  public void Var138() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd. Retrieve the date from the 2nd result set.
   */
  public void Var139() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian. Retrieve the date from the 2nd result set.
   */
  public void Var140() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var141() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var142() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var143() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with DMY
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var144() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy. Retrieve the date from the 2nd result set.
   */
  public void Var145() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy. Retrieve the date from the 2nd result set.
   */
  public void Var146() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd. Retrieve the date from the 2nd result set.
   */
  public void Var147() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian. Retrieve the date from the 2nd result set.
   */
  public void Var148() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var149() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var150() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var151() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with YMD
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var152() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy. Retrieve the date from the 2nd result set.
   */
  public void Var153() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy. Retrieve the date from the 2nd result set.
   */
  public void Var154() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd. Retrieve the date from the 2nd result set.
   */
  public void Var155() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian. Retrieve the date from the 2nd result set.
   */
  public void Var156() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var157() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var158() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var159() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JUL
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var160() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy. Retrieve the date from the 2nd result set.
   */
  public void Var161() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy. Retrieve the date from the 2nd result set.
   */
  public void Var162() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd. Retrieve the date from the 2nd result set.
   */
  public void Var163() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian. Retrieve the date from the 2nd result set.
   */
  public void Var164() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var165() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var166() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var167() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with ISO
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var168() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy. Retrieve the date from the 2nd result set.
   */
  public void Var169() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy. Retrieve the date from the 2nd result set.
   */
  public void Var170() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd. Retrieve the date from the 2nd result set.
   */
  public void Var171() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian. Retrieve the date from the 2nd result set.
   */
  public void Var172() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var173() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var174() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var175() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with USA
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var176() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy. Retrieve the date from the 2nd result set.
   */
  public void Var177() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy. Retrieve the date from the 2nd result set.
   */
  public void Var178() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd. Retrieve the date from the 2nd result set.
   */
  public void Var179() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian. Retrieve the date from the 2nd result set.
   */
  public void Var180() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var181() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var182() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var183() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with EUR
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var184() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is mdy. Retrieve the date from the 2nd result set.
   */
  public void Var185() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is dmy. Retrieve the date from the 2nd result set.
   */
  public void Var186() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25/06/07"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is ymd. Retrieve the date from the 2nd result set.
   */
  public void Var187() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/06/25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is julian. Retrieve the date from the 2nd result set.
   */
  public void Var188() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("07/176"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var189() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var190() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("06/25/2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var191() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("25.06.2007"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a date from a stored procedure result set created with JIS
   * date format is correctly displayed when it is retrieved with a connection
   * whose date format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var192() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String date = rs.getString(1);
      rs.close();
      csTypes_.close();
      assertCondition(date.equals("2007-06-25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms. Retrieve the date from the 2nd result set.
   */
  public void Var193() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var194() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var195() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var196() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var197() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var198() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var199() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with HMS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var200() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTMDYHMS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms. Retrieve the date from the 2nd result set.
   */
  public void Var201() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var202() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var203() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var204() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var205() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var206() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var207() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with ISO
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var208() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTDMYISO");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms. Retrieve the date from the 2nd result set.
   */
  public void Var209() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:00"), "Time = " + time + " sb 11:15:00 Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var210() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var211() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var212() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var213() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var214() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var215() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var216() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTYMDUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms. Retrieve the date from the 2nd result set.
   */
  public void Var217() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var218() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var219() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var220() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var221() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var222() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var223() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with USA
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var224() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTUSAUSA");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:00"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms. Retrieve the date from the 2nd result set.
   */
  public void Var225() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var226() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var227() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var228() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var229() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var230() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var231() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var232() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJULEUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms. Retrieve the date from the 2nd result set.
   */
  public void Var233() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var234() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var235() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var236() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var237() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var238() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var239() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with EUR
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var240() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTEUREUR");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms. Retrieve the date from the 2nd result set.
   */
  public void Var241() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var242() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var243() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var244() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var245() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var246() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var247() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var248() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTISOJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is hms. Retrieve the date from the 2nd result set.
   */
  public void Var249() {

    try {
      csTypes_ = connectionMDYHMS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is iso. Retrieve the date from the 2nd result set.
   */
  public void Var250() {

    try {
      csTypes_ = connectionDMYISO_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var251() {

    try {
      csTypes_ = connectionUSAUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var252() {

    try {
      csTypes_ = connectionEUREUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var253() {

    try {
      csTypes_ = connectionJISJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is usa. Retrieve the date from the 2nd result set.
   */
  public void Var254() {

    try {
      csTypes_ = connectionYMDUSA_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15 AM"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is eur. Retrieve the date from the 2nd result set.
   */
  public void Var255() {

    try {
      csTypes_ = connectionJULEUR_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11.15.25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }

  /**
   * getDate() - Verify a time from a stored procedure result set created with JIS
   * time format is correctly displayed when it is retrieved with a connection
   * whose time format is jis. Retrieve the date from the 2nd result set.
   */
  public void Var256() {

    try {
      csTypes_ = connectionISOJIS_.prepareCall("CALL " + JDCSTest.COLLECTION + ".DTJISJIS");
      csTypes_.execute();
      ResultSet rs = csTypes_.getResultSet();
      csTypes_.getMoreResults();
      rs = csTypes_.getResultSet();
      rs.next();
      String time = rs.getString(2);
      rs.close();
      csTypes_.close();
      assertCondition(time.equals("11:15:25"), "Added by Toolbox 1/5/2007");
    } catch (Exception e) {
      failed(e, "Unexpected Exception.  Added by Toolbox 1/5/2007.");
    }
  }
}
