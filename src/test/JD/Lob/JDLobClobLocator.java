///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobClobLocator.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Lob;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;

import test.JDLobTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDTestUtilities;
import test.JDLobTest.JDTestClob;

import java.io.FileOutputStream;
import java.io.FileReader; //@C6A
import java.io.FileWriter; //@C6A
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream; //@C2A
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer; //@C2A
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

/**
 * Testcase JDLobClobLocator. This tests the following method of the JDBC Clob
 * class:
 * 
 * <ul>
 * <li>getAsciiStream()
 * <li>getCharacterStream()
 * <li>getSubString()
 * <li>length()
 * <li>position()
 * <li>setString() //@C2A
 * <li>setAsciiStream() //@C2A
 * <li>setCharacterStream() //@C2A
 * <li>truncate() //@C2A
 * <li>free() //@pda
 * </ul>
 **/
public class JDLobClobLocator extends JDTestcase {
  public final String DBCLOB_ADDED = " -- DBCLOB test added 07/07/06 by native driver";
  // The native JDBC driver will free the locators in blocks of 32
  public final static int FREE_LOCATOR_BLOCK_SIZE = 32;

  // Private data.
  Connection connection_;
  Statement statement_;
  Statement statement1_;
  Statement statement2_;
  Statement statement3_;

  ResultSet rs1_;
  ResultSet rs2_; // @C2A
  ResultSet rs3_; // @k2
  ResultSet rs4_; // @k2
  ResultSet rs6_; // @pda

  public String TABLE_ = JDLobTest.COLLECTION + ".CLOBLOC";
  public String TABLE2_ = JDLobTest.COLLECTION + ".CLOBLOC2";
  public String TABLE3_ = JDLobTest.COLLECTION + ".CLOBLOC3";
  public String TABLE4_ = JDLobTest.COLLECTION + ".CLOBLOC4"; // @K5A
  public String TABLE5_ = JDLobTest.COLLECTION + ".CLOBLOC5"; // @K5A
  public String TABLE6_ = JDLobTest.COLLECTION + ".CLOBLOC6"; // @pda
  public String TABLEHUGE_ = JDLobTest.COLLECTION + ".CLOBLOCH";

  public String TABLE120_ = JDLobTest.COLLECTION + ".CLOBLOC120";
  public String TABLE121_ = JDLobTest.COLLECTION + ".CLOBLOC121";

  public String MEDIUM_ = "A really big object.";
  public String LARGE_ = "TBD"; // final
  public String HUGE_ = "TBD";
  public int WIDTH_ = 30000;
  public int HUGE_WIDTH_ = 32000000;
  public int CCSID_ = 37;
  public int DBCLOB_CCSID_ = 13488;

  public String DBCLOB_MEDIUM_ = "TBD";
  public String DBCLOB_MEDIUM_SET_ = "Really round objects";
  public String DBCLOB_LARGE_ = "TBD"; // final
  public String DBCLOB_SMALL_ = "TBD";

  public String SMALL_ = "A really big object.";
  String SMALL_CLOB_APPEND_ = " abcdefgh\n";
  String MEDIUM_CLOB_APPEND_ = " 12345678\n";
  String MEDIUM2_CLOB_APPEND_ = " ijklmnop\n";
  String LARGE_CLOB_APPEND_ = " qrstuvwx\n";

  String ASSIGNMENT1_ = "Really round objects";
  String ASSIGNMENT_CHAR4_ = "cool";
  String ASSIGNMENT_CHAR5_ = "that.";
  String ASSIGNMENT_CHAR7_ = "insect.";

  String ASSIGNMENT_DBCHAR1_ = "B";
  String ASSIGNMENT_DBCHAR4_ = "cool";
  String ASSIGNMENT_DBCHAR7_ = "insect.";
  String ASSIGNMENT_DBCHAR9_ = "IT WORKS!";
  String ASSIGNMENT_DBCHAR17_ = "CLOB WRITER TEST!";
  String ASSIGNMENT_DBCHAR20_ = "How are you doing???";

  String smallClob = null;
  String mediumClob = null;
  String medium2Clob = null;
  String largeClob = null;

  private boolean var077Called_ = false;
  private boolean Var197Called_ = false;
  StringBuffer sb = new StringBuffer();

  String lobThreshold = "1"; /*
                              * Note: Should only be set in the constructors for
                              * the testcase. Then setup will use the common
                              * value
                              */
  String getMethod = "getClob";
  String updateMethod = "updateClob";
  String setMethod = "setClob";
  boolean skipAsciiTests = false;
  boolean requireJdbc40 = false;
  boolean skipCleanup = false;

  /**
   * Constructor.
   **/
  public JDLobClobLocator(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream,
      String password) {
    super(systemObject, "JDLobClobLocator", namesAndVars, runMode,
        fileOutputStream, password);
  }

  public JDLobClobLocator(AS400 systemObject, String testname,
      Hashtable namesAndVars, int runMode, FileOutputStream fileOutputStream, String password) {
    super(systemObject, testname, namesAndVars, runMode, fileOutputStream,
         password);
  }

  void setupTableNames() {
    TABLE_ = JDLobTest.COLLECTION + ".CLOBLOC";
    TABLE2_ = JDLobTest.COLLECTION + ".CLOBLOC2";
    TABLE3_ = JDLobTest.COLLECTION + ".CLOCLOC3"; // @k2
    TABLE4_ = JDLobTest.COLLECTION + ".CLOBLOC4"; // @K5A
    TABLE5_ = JDLobTest.COLLECTION + ".CLOBLOC5";
    TABLE6_ = JDLobTest.COLLECTION + ".CLOBLOC6";
    TABLE120_ = JDLobTest.COLLECTION + ".CLOBLLOC120";
    TABLE121_ = JDLobTest.COLLECTION + ".CLOBLOC121";
    TABLEHUGE_ = JDLobTest.COLLECTION + ".CLOBLOCH";
  }

  void setupTestStringValues() {

    SMALL_ = "A really big object.";

    //
    // Note. This should end with a unique character. In this case a period.
    //
    MEDIUM_ = "A really big object.";

    WIDTH_ = 30000;
    StringBuffer buffer = new StringBuffer(WIDTH_);
    int actualLength = WIDTH_ - 2;
    for (int i = 1; i <= actualLength; ++i)
      buffer.append("&");
    LARGE_ = buffer.toString();

    CCSID_ = 37;
    DBCLOB_CCSID_ = 13488;
    SMALL_CLOB_APPEND_ = " abcdefgh\n";
    MEDIUM_CLOB_APPEND_ = " 12345678\n";
    MEDIUM2_CLOB_APPEND_ = " ijklmnop\n";
    LARGE_CLOB_APPEND_ = " qrstuvwx\n";

    ASSIGNMENT1_ = "Really round objects";
    ASSIGNMENT_CHAR4_ = "cool";
    ASSIGNMENT_CHAR5_ = "that.";
    ASSIGNMENT_CHAR7_ = "insect.";

    ASSIGNMENT_DBCHAR4_ = "cool";

    DBCLOB_MEDIUM_SET_ = "Really round objects";

  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    setupTableNames();
    setupTestStringValues();

    if ("TBD".equals(DBCLOB_MEDIUM_)) {
      DBCLOB_MEDIUM_ = MEDIUM_;
    }
    if ("TBD".equals(DBCLOB_LARGE_)) {
      DBCLOB_LARGE_ = LARGE_;
    }
    if ("TBD".equals(DBCLOB_SMALL_)) {
      DBCLOB_SMALL_ = SMALL_;
    }

    if (isJdbc20()) {
      String sql=""; 
      
      try {

      if (areLobsSupported()) {
        String url = baseURL_ +  ";lob threshold=" + lobThreshold;

        sql = "connecting to "+url; 
        connection_ = testDriver_.getConnection (url, systemObject_.getUserId(), encryptedPassword_);

        connection_.setAutoCommit(false);
        connection_
            .setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

        statement_ = connection_.createStatement();

        String dbmoncol = JDLobTest.COLLECTION;
        try {
          statement_.executeUpdate("DROP TABLE " + dbmoncol + ".JDLOBMON");
        } catch (Exception e) {

        }
        connection_.commit();
        String strdbmonCommand = "call QSYS.QCMDEXC(  'STRDBMON OUTFILE("
            + dbmoncol
            + "/JDLOBMON) TYPE(*BASIC)                        ',000000065.00000)";
        System.out.println("Starting DBMON using " + strdbmonCommand);
        statement_.executeUpdate(strdbmonCommand);

        connection_.commit();

        sql = "creating statement1,2,3"; 
        statement1_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); // @C2C

        statement2_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        statement3_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        //
        // Do some cleanup first
        //

        silentlyDropTable(statement_, TABLE_);
        silentlyDropTable(statement_, TABLE2_);
        silentlyDropTable(statement_, TABLE3_);
        silentlyDropTable(statement_, TABLE4_);
        silentlyDropTable(statement_, TABLE5_);
        silentlyDropTable(statement_, TABLE6_);
        try {
          statement_.executeUpdate("drop FUNCTION " + JDLobTest.COLLECTION
              + ".CLOBLOCINF");
        } catch (Exception e) {
        }

        silentlyDropTable(statement_, TABLEHUGE_);

        connection_.commit();

        //
        // Create the tables
        //
        sql = "CREATE TABLE " + TABLE_ + "(C_CLOB CLOB("
            + WIDTH_ + ") CCSID " + CCSID_ + ")";
        
        statement_.executeUpdate(sql);

        sql = "CREATE TABLE " + TABLE2_
            + "(C_CLOB CLOB(110000) CCSID " + CCSID_ + ")"; 
        statement_.executeUpdate(sql);
        sql = "CREATE TABLE " + TABLE3_
            + "(C_DBCLOB DBCLOB(" + WIDTH_ + ") CCSID " + DBCLOB_CCSID_ + ")"; 
        statement_.executeUpdate(sql);
        sql = "CREATE TABLE " + TABLE4_ + "(C_CLOB CLOB("
            + WIDTH_ + ") CCSID " + CCSID_ + ")"; 
        statement_.executeUpdate(sql);

        sql = "CREATE TABLE " + TABLE5_
            + "(C_DBCLOB DBCLOB(" + WIDTH_ + ") CCSID " + DBCLOB_CCSID_ + ")"; 
        statement_.executeUpdate(sql);

        sql = "CREATE TABLE " + TABLE6_ + "(C_CLOB CLOB("
            + WIDTH_ + ") CCSID " + CCSID_ + ")"; 
        statement_.executeUpdate(sql); // @pda

        sql = "INSERT INTO "
            + TABLE_ + " (C_CLOB) VALUES (?)"; 
        PreparedStatement ps = connection_.prepareStatement(sql);
        ps.setString(1, "");
        ps.executeUpdate();
        ps.setString(1, MEDIUM_);
        ps.executeUpdate();
        ps.setString(1, LARGE_);
        ps.executeUpdate();
        ps.setString(1, SMALL_); // @C2A
        ps.executeUpdate(); // @C2A
        ps.close();

        connection_.commit();

        sql = "CREATE TABLE " + TABLEHUGE_ + "(C_CLOB CLOB("
            + HUGE_WIDTH_ + ") CCSID 1208)"; 
        statement_.executeUpdate(sql);

        try {
          ps = connection_.prepareStatement("INSERT INTO " + TABLEHUGE_
              + " (C_CLOB) VALUES (?)");

          StringReader hugeReader = new StringReader(HUGE_);
          System.out.println(this + " setting HUGE via setCharacterStream");
          try {
            ps.setCharacterStream(1, hugeReader, HUGE_.length());
          } catch (Throwable e) {
            System.out.println("Error seeting up huge");
            System.out.println("HUGE_.length()=" + HUGE_.length());
            System.out.println("CCSID 1208 HUGE_WIDTH_ = " + HUGE_WIDTH_);
            byte[] utf8bytes = HUGE_.getBytes("UTF-8");
            System.out.println("utf8 length is " + utf8bytes.length);
            e.printStackTrace();
          }
          System.out.println(this + " inserting HUGE");
          try {
            ps.executeUpdate();
          } catch (java.lang.OutOfMemoryError t) {
            System.out.println("WARNING:  Out of Memory Error setting up HUGE");
          }
          System.out.println(this + " done inserting HUGE");
          ps.close();
        } catch (Exception e) {

          System.out.println("Error in setup");
          Throwable t = e;
          while (t != null) {
            System.err.println("-------------------------------------");
            t.printStackTrace();
            t = t.getCause();
          }

          if (connection_ instanceof AS400JDBCConnection) {
            AS400JDBCConnection as400connection = (AS400JDBCConnection) connection_;
            System.out.println("Server job is "
                + as400connection.getServerJobIdentifier());
          }
        }

        sql = "SELECT * FROM " + TABLE_; 
        rs1_ = statement1_.executeQuery(sql);

        sql = "INSERT INTO " + TABLE2_
            + " (C_CLOB) VALUES (?)"; 
        ps = connection_.prepareStatement(sql);

        sb.setLength(0);
        for (int i = 1; i < 20; i++)
          sb = sb.append(i + SMALL_CLOB_APPEND_);
        sb.setLength(110);
        smallClob = new String(sb);
        ps.setString(1, smallClob);
        ps.executeUpdate();

        sb = new StringBuffer();
        for (int i = 1; i < 2048; i++)
          sb = sb.append(i + MEDIUM_CLOB_APPEND_);
        sb.setLength(10 * 1024);
        mediumClob = new String(sb);
        ps.setString(1, mediumClob);
        ps.executeUpdate();

        sb = new StringBuffer();
        for (int i = 1; i < 5 * 1024; i++)
          sb = sb.append(i + MEDIUM2_CLOB_APPEND_);
        sb.setLength(30 * 1024);
        medium2Clob = new String(sb);
        ps.setString(1, medium2Clob);
        ps.executeUpdate();

        sb = new StringBuffer();
        for (int i = 1; i < 15 * 1024; i++)
          sb = sb.append(i + LARGE_CLOB_APPEND_);
        // Assumes that the length of LARGE_CLOB_APPEND_ is 10 in bytes
        // for the CCSID_ encoding
        sb.setLength(10 * 1024 * LARGE_CLOB_APPEND_.length());
        largeClob = new String(sb);
        ps.setString(1, largeClob);
        ps.executeUpdate();

        ps.close();

        sql = "SELECT * FROM " + TABLE_; 
        rs1_ = statement1_.executeQuery(sql);

        // @k2
        sql="INSERT INTO "
            + TABLE3_ + " (C_DBCLOB) VALUES (?)"; 
        PreparedStatement ps3 = connection_.prepareStatement(sql);
        ps3.setString(1, "");
        ps3.executeUpdate();
        ps3.setString(1, DBCLOB_MEDIUM_);
        ps3.executeUpdate();
        ps3.setString(1, DBCLOB_LARGE_);
        ps3.executeUpdate();
        ps3.setString(1, DBCLOB_SMALL_); // @C2A
        ps3.executeUpdate(); // @C2A
        ps3.close();

        sql = "call QSYS.QCMDEXC('ENDDBMON                ',000000010.00000)"; 
        statement_
            .executeUpdate(sql);

        sql = "SELECT * FROM " + TABLE3_; 
        rs3_ = statement3_.executeQuery(sql);

        sql = "INSERT INTO "
            + TABLE4_ + " (C_CLOB) VALUES (?)"; 
        PreparedStatement ps4 = connection_.prepareStatement(sql); // @K5A
        for (int i = 0; i < 9; i++) // @K5A insert the same string 9 times for
                                    // variations 108-116
        {
          ps4.setString(1, SMALL_);
          ps4.executeUpdate();
        }
        ps4.close(); // @K5A

        sql = "INSERT INTO "
            + TABLE5_ + " (C_DBCLOB) VALUES (?)"; 
        PreparedStatement ps5 = connection_.prepareStatement(sql);
        for (int i = 0; i < 9; i++) {
          ps5.setString(1, DBCLOB_SMALL_);
          ps5.executeUpdate();
        }
        ps5.close(); // @K5A

        sql = "INSERT INTO "
            + TABLE6_ + " (C_CLOB) VALUES (?)"; 
        PreparedStatement ps6 = connection_.prepareStatement(sql); // @pda
        for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {// @pda
          ps6.setString(1, SMALL_);// @pda
          ps6.executeUpdate();// @pda
        }// @pda
        ps6.close(); // @pda

      }

      // Create the UDF to test the locator
      String cProgram[] = {
          "   /* File cloblocinf.c created  on Thu Feb 22 2007. */",
          "",
          "  /*",
          "  * UDF to get info about a clob from a locator",
          "",
          "",
          "    compile using the following ",
          "   CRTCMOD MODULE(CLOBLOCINF) DBGVIEW(*ALL)   ",
          "  CRTSRVPGM CLOBLOCINF export(*all)",
          "",
          "   CREATE FUNCTION CLOBLOCINF (int) RETURNS VARCHAR(200) ",
          "   LANGUAGE C EXTERNAL NAME ",
          "   'QGPL/CLOBLOCINF(CLOBLOCINF)', PARAMETER STYLE SQL",
          "",
          "   To test",
          "",
          "    select CLOBLOCINF(256) from qsys2.qsqptabl",
          "",
          "",
          "",
          "",
          "  */",
          "",
          "#include <stdlib.h>",
          "#include <stdio.h>",
          "#include <string.h>",
          "#include <sqludf.h>",
          "",
          "void  CLOBLOCINF (int * locator,",
          "                  char * output,",
          "                  int * ind0,",
          "                  int * ind1,",
          "                  char * sqlstate,",
          "                  char * functionName,",
          "                  char * specificName,",
          "                  char * messageText",
          "                 ) {",
          "     int rc; ",
          "     long length;",
          "     udf_locator loc;",
          "",
          "     loc = *locator; ",
          "     rc = sqludf_length(&loc, &length);",
          "     if (rc == 0) {",
          "         sprintf(output, \"length for locator %d is %d\", loc, length); ",
          "     } else {",
          "         sprintf(output, \"ERROR sqludf_length for %d  returned %d\", loc, rc); ",
          "     } ", "", "                    }", };


        String url2 = baseURL_ ;

        Connection noneConnection  = testDriver_.getConnection (url2, systemObject_.getUserId(), encryptedPassword_);
       stringArrayToSourceFile(noneConnection, cProgram, JDLobTest.COLLECTION,
            "CLOBLOCINF");
        noneConnection.close();
        
        sql = "call QGPL.JDCMDEXEC(?,?)";
        CallableStatement cmd = connection_.prepareCall(sql);
        String command = "CRTCMOD MODULE(" + JDLobTest.COLLECTION
            + "/CLOBLOCINF) " + " SRCFILE(" + JDLobTest.COLLECTION
            + "/CLOBLOCINF)   ";

        sql = sql + " command="+command; 
        cmd.setString(1, command);
        cmd.setInt(2, command.length());
        try {
          cmd.execute();
        } catch (Exception e) {
          System.out.println("Error calling " + command);
          e.printStackTrace();
        }

        command = "CRTSRVPGM SRVPGM(" + JDLobTest.COLLECTION
            + "/CLOBLOCINF) MODULE(" + JDLobTest.COLLECTION
            + "/CLOBLOCINF) EXPORT(*ALL)  ";
        cmd.setString(1, command);
        cmd.setInt(2, command.length());
        try {
          cmd.execute();
        } catch (Exception e) {
          System.out.println("Error calling " + command);
          e.printStackTrace();
        }

        sql = " CREATE FUNCTION " + JDLobTest.COLLECTION
            + ".CLOBLOCINF (int) RETURNS VARCHAR(200) "
            + "LANGUAGE C EXTERNAL NAME " + "'" + JDLobTest.COLLECTION
            + "/CLOBLOCINF(CLOBLOCINF)' PARAMETER STYLE SQL";

        Statement stmt = connection_.createStatement();
        try {
          stmt.executeUpdate("drop FUNCTION " + JDLobTest.COLLECTION
              + ".CLOBLOCINF");
        } catch (Exception e) {
        }
        try {
          stmt.executeUpdate(sql);
        } catch (Exception e) {
          System.out.println("Error running " + sql);
          e.printStackTrace();
        }
        stmt.close();
      } catch (Exception e) {
        System.out.println("Error running " + sql);
        e.printStackTrace();
      }

    }

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (isJdbc20()) {
      connection_.commit();
      statement2_.close();
      if (areLobsSupported()) {
        if (!skipCleanup) {
          try {
            statement_.executeUpdate("DROP TABLE " + TABLE_);
          } catch (Exception e) {
          }
          try {
            statement_.executeUpdate("DROP TABLE " + TABLE2_);
          } catch (Exception e) {
          }
          try {
            statement_.executeUpdate("DROP TABLE " + TABLE3_);
          } catch (Exception e) {
          }
          try {
            statement_.executeUpdate("DROP TABLE " + TABLE4_);
          } catch (Exception e) {
          }
          try {
            statement_.executeUpdate("DROP TABLE " + TABLEHUGE_);
          } catch (Exception e) {
          }
          try {
            statement_.executeUpdate("drop FUNCTION " + JDLobTest.COLLECTION
                + ".CLOBLOCINF");
          } catch (Exception e) {
          }

          try {
            statement_.executeUpdate("DROP TABLE " + TABLE6_);
          } catch (Exception e) {
          }
        }
        statement_.close();
        statement3_.close();
        connection_.commit();
        connection_.close();
      }
    }
  }

  public String getClob(Object clob) throws Exception {
    int length = (int) JDReflectionUtil.callMethod_L(clob, "length");
    return (String) JDReflectionUtil.callMethod_O(clob, "getSubString", 1,
        length);
  }

  /**
   * getAsciiStream() - When the lob is empty.
   **/
  public void Var001() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (skipAsciiTests) {
      notApplicable("getAsciiStreamTest");
      return;
    }
    try {
      rs1_.absolute(1);
      Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
      InputStream v = (InputStream) JDReflectionUtil.callMethod_O(clob,
          "getAsciiStream");
      sb.setLength(0);
      assertCondition(compare(v, "", "8859_1", sb), sb);
    } catch (Exception e) {
      failed(connection_, e, "Unexpected Exception");
    }
  }

  /**
   * getAsciiStream() - When the lob is not empty.
   **/
  public void Var002() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (skipAsciiTests) {
      notApplicable("getAsciiStreamTest");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          InputStream v = (InputStream) JDReflectionUtil.callMethod_O(clob,
              "getAsciiStream");
          sb.setLength(0);
          assertCondition(compare(v, MEDIUM_, "8859_1", sb), sb);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getAsciiStream() - When the lob is full.
   **/
  public void Var003() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (skipAsciiTests) {
      notApplicable("getAsciiStreamTest");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(3);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          InputStream v = (InputStream) JDReflectionUtil.callMethod_O(clob,
              "getAsciiStream");
          sb.setLength(0);
          assertCondition(compare(v, LARGE_, "8859_1", sb), sb);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getCharacterStream() - When the lob is empty.
   **/
  public void Var004() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(1);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          Reader v = (Reader) JDReflectionUtil.callMethod_O(clob,
              "getCharacterStream");
          sb.setLength(0);
          assertCondition(compare(v, "", sb), sb);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getCharacterStream() - When the lob is not empty.
   **/
  public void Var005() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          Reader v = (Reader) JDReflectionUtil.callMethod_O(clob,
              "getCharacterStream");
          sb.setLength(0);
          assertCondition(compare(v, MEDIUM_, sb), sb);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getCharacterStream() - When the lob is full.
   **/
  public void Var006() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(3);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          Reader v = (Reader) JDReflectionUtil.callMethod_O(clob,
              "getCharacterStream");
          sb.setLength(0);
          assertCondition(compare(v, LARGE_, sb), sb);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when start is less than 0.
   **/
  public void Var007() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          JDReflectionUtil.callMethod_O(clob, "getSubString", (long) -1,
              (int) 5);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when start is 0.
   **/
  public void Var008() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          JDReflectionUtil
              .callMethod_O(clob, "getSubString", (long) 0, (int) 5);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when start is greater than the
   * max length of the lob.
   **/
  public void Var009() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          JDReflectionUtil.callMethod_O(clob, "getSubString",
              MEDIUM_.length() + 2, 5);
          failed("Didn't throw SQLException for start belong length of lob ");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when length is less than 0.
   **/
  public void Var010() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          JDReflectionUtil.callMethod_O(clob, "getSubString", (long) 2,
              (int) -1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getSubString() - Should work when length extends past the end of the lob.
   * 
   * SQL400 - I made this consistant with JDLobBlobLocator var008. Which was
   * should we go on this?
   **/
  public void Var011() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          String s = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", (long) 4, (int) MEDIUM_.length() + 2);

          // The latest javadoc implies that this should work.
          // Testcase changed 6/17/2013.
          String expected = MEDIUM_.substring(3);
          assertCondition(s.equals(expected),
              "Changed 6/17/2013 -- getting a longer length should work"
                  + "\nGot '" + s + "'" + "\nExp '" + expected + "'");
        } catch (Exception e) {
          failed(
              connection_,
              e,
              "Changed 6/17/2013 -- getting a longer length starting at 4 should work.  MEDIUM_.length()="
                  + MEDIUM_.length());
        }
      }
    }
  }

  /**
   * getSubString() - Should work on an empty lob.
   * 
   * SQL400 - The native driver expects this to be an error condition. You are
   * starting at position 1 of a lob with a length of 0. I have talked to Clif
   * about this and he was planning to change the Toolbox behavior to mimic
   * ours.
   * 
   * @C3 - Changed behavior to match native driver.
   * @CRS - This testcase should not throw an exception.
   **/
  public void Var012() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(1);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          String v = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", (long) 1, (int) 0);
          assertCondition("".equals(v), "expected \"\" got \"" + v
              + "\" -- testcase fixed 06/22/2006");
        } catch (Exception e) {
          failed(connection_, e, "Unexpected exception");
        }
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve all of a non-empty lob.
   **/
  public void Var013() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          String v = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", (long) 1, (int) MEDIUM_.length());
          assertCondition(v.equals(MEDIUM_),
              "Got '" + JDTestUtilities.getMixedString(v) + "'\n" + "sb  '"
                  + JDTestUtilities.getMixedString(MEDIUM_) + "'");
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve all of a full lob.
   * 
   * SQL400 - This is a testcase problem in that the size of the value put into
   * the lob was only 29998, not 30000. That is all you can expect to get back
   * out.
   **/
  public void Var014() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(3);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          String v = null;

          // Testcase bug.
          v = (String) JDReflectionUtil.callMethod_O(clob, "getSubString",
              (long) 1, (int) LARGE_.length());

          assertCondition(v.equals(LARGE_));
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve the first part of a non-empty lob.
   **/
  public void Var015() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          String v = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", (long) 1, (int) 6);
          assertCondition(v.equals(MEDIUM_.substring(0, 6)), "Got '"
              + JDTestUtilities.getMixedString(v) + "'\n" + "sb  '"
              + JDTestUtilities.getMixedString(MEDIUM_.substring(0, 6)) + "'");
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve the middle part of a non-empty
   * lob.
   **/
  public void Var016() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          String v = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", (long) 6, (int) 7);
          assertCondition(v.equals(MEDIUM_.substring(5, 12)), "Got '"
              + JDTestUtilities.getMixedString(v) + "'\n" + "sb  '"
              + JDTestUtilities.getMixedString(MEDIUM_.substring(5, 12)) + "'");
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve the last part of a non-empty lob.
   **/
  public void Var017() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          String v = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", (long) 10, (int) MEDIUM_.length() - 9);
          assertCondition(v.equals(MEDIUM_.substring(9)), "Got '"
              + JDTestUtilities.getMixedString(v) + "'\n" + "sb  '"
              + JDTestUtilities.getMixedString(MEDIUM_.substring(9)) + "'");
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * length() - When the lob is empty.
   * 
   * SQL400 - The length of a lob is expected to be the length of the signficant
   * data in the lob. The Native driver returns 0 here as the lob is empty.
   **/
  public void Var018() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(1);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          assertCondition(JDReflectionUtil.callMethod_L(clob, "length") == 0);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * length() - When the lob is not empty.
   * 
   * SQL400 - The length of a lob is expected to be the length of the signficant
   * data in the lob. The Native driver returns 20 here as that is the number of
   * significant characters in this lob. empty.
   **/
  public void Var019() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");

          if (CCSID_ == 5035 && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            notApplicable("09/13/07: Toolbox issue 30040 opened for hostserver.  They say it is a permanant restriction and do not plan to fix it.  ");
            return;
          }

          assertCondition(
              JDReflectionUtil.callMethod_L(clob, "length") == MEDIUM_.length(),
              "clob.length=" + JDReflectionUtil.callMethod_L(clob, "length")
                  + " sb " + MEDIUM_.length());

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * length() - When the lob is full.
   * 
   * SQL400 - This test should expect the value to be width -2 as that is what
   * the setup created.
   **/
  public void Var020() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(3);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");

          if (CCSID_ == 5035 && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            notApplicable("09/13/07: Toolbox issue 30040 opened for hostserver.  They say it is a permanant restriction and do not plan to fix it.  ");
            return;
          }

          assertCondition(JDReflectionUtil.callMethod_L(clob, "length") == LARGE_
              .length());

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(String,long) - Should throw an exception when the pattern is null.
   **/
  public void Var021() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              (String) null, (long) 0);

          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * position(String,long) - Should throw an exception when start is less than
   * 0.
   **/
  public void Var022() {
    if (checkJdbc20()) {
      if (requireJdbc40 && (!isJdbc40())) {
        notApplicable("JDBC 4.0 testcase");
        return;
      }
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position", "Test",
              (long) -1);

          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * position(String,long) - Should throw an exception when start is 0.
   **/
  public void Var023() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position", "Test",
              (long) 0);

          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * position(String,long) - Should throw an exception when start is greater
   * than the length of the lob.
   **/
  public void Var024() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position", "Test",
              (long) MEDIUM_.length() + 1);

          if (CCSID_ == 5035 && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            notApplicable("09/13/07: Toolbox issue 30040 opened for hostserver.  They say it is a permanant restriction and do not plan to fix it.  ");
            return;
          }

          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * position(String,long) - Should return -1 when the pattern is not found at
   * all.
   **/
  public void Var025() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position", "NONO",
              (long) 1);
          assertCondition(v == -1, "v = " + v + " SB -1 clob.getClass()="
              + clob.getClass());
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(String,long) - Should return -1 when the pattern is not found
   * after the starting position, although it does appear before the starting
   * position.
   **/
  public void Var026() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              MEDIUM_.substring(1, 4), (long) 11);
          assertCondition(v == -1, "v = " + v + " SB -1 clob.getClass()="
              + clob.getClass());
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(String,long) - Should return 1 when the pattern is found at the
   * beginning of the lob.
   **/
  public void Var027() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    String searchString = MEDIUM_.substring(0, 7);
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              searchString, (long) 1);

          boolean condition = (v == 1);

          if (!condition) {
            System.out.println("object.position('" + searchString
                + "',1) returned " + v + " for " + clob);
          }
          assertCondition(condition);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(String,long) - Should return the position when the pattern is
   * found in the middle of the lob, and start is 1.
   **/
  public void Var028() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        String searchString = MEDIUM_.substring(9, 12);
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              searchString, (long) 1);

          assertCondition(v == 10, "v = " + v + " SB 10 searchString="
              + searchString);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(String,long) - Should return the position when the pattern is
   * found in the middle of the lob, and start is before where the pattern
   * appears.
   **/
  public void Var029() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          String searchString = MEDIUM_.substring(9, 12);
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              searchString, (long) 7);

          assertCondition(v == 10, "v = " + v + " SB 10 searchString="
              + searchString);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(String,long) - Should return the position when the pattern is
   * found in the middle of the lob, and start is right where the pattern
   * appears.
   **/
  public void Var030() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          String searchString = MEDIUM_.substring(8, 12);
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              searchString, (long) 9);

          assertCondition(v == 9, "v = " + v + " SB 9");

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(String,long) - Should return the position when the pattern is
   * found at the end of the lob, and start is 1.
   **/
  public void Var031() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          int length = MEDIUM_.length();
          String searchString = MEDIUM_.substring(length - 5, length);
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              searchString, (long) 1);

          assertCondition(v == length - 4, "v = " + v + " SB " + (length - 4));

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(String,long) - Should return the position when the pattern is
   * found at the end of the lob, and start is right where the pattern occurs.
   **/
  public void Var032() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    long v = 0;
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          int length = MEDIUM_.length();
          String searchString = MEDIUM_.substring(length - 1, length);
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          v = JDReflectionUtil.callMethod_L(clob, "position", searchString,
              (long) length);

          assertCondition(v == length, "v = " + v + " SB " + length);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception: v=" + v);
        }
      }
    }
  }

  /**
   * position(String,long) - Should return 0 when the pattern is the empty
   * string and start is 1.
   **/
  public void Var033() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil
              .callMethod_L(clob, "position", "", (long) 1);

          assertCondition(v == 1);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(String,long) - Should return start when the pattern is the empty
   * string and start is in the middle.
   **/
  public void Var034() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil
              .callMethod_L(clob, "position", "", (long) 9);

          assertCondition(v == 9);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(String,long) - Should return length-1 when the pattern is the
   * empty string and start is the length-1.
   **/
  public void Var035() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          int length = MEDIUM_.length();
          long v = JDReflectionUtil.callMethod_L(clob, "position", "",
              (long) length);

          assertCondition(v == length);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should throw an exception when the pattern is null.
   **/
  public void Var036() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          Class[] argClasses = new Class[2];
          argClasses[0] = Clob.class;
          argClasses[1] = Long.TYPE;
          Object[] args = new Object[2];
          args[0] = null;
          args[1] = new Long(1);
          long v = JDReflectionUtil.callMethod_L(clob, "position", argClasses,
              args);

          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should throw an exception when start is less than 0.
   **/
  public void Var037() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob("Test"), (long) -1);

          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should throw an exception when start is 0.
   **/
  public void Var038() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob("Test"), (long) 0);

          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should throw an exception when start is greater than
   * the length of the lob.
   **/
  public void Var039() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob("Test"), (long) MEDIUM_.length() + 1);

          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return -1 when the pattern is not found at
   * all.
   **/
  public void Var040() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob("This is only a test"), (long) 1);
          assertCondition(v == -1, "v = " + v + " SB -1");
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return -1 when the pattern is not found after
   * the starting position, although it does appear before the starting
   * position.
   **/
  public void Var041() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          String searchString = MEDIUM_.substring(1, 5);
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(searchString), (long) 12);
          assertCondition(v == -1, "v = " + v + " SB -1");
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return 1 when the pattern is found at the
   * beginning of the lob. Lob indexes are 1 based.
   **/
  public void Var042() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          String searchString = MEDIUM_.substring(0, 10);
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(searchString), (long) 1);

          assertCondition(v == 1);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found
   * in the middle of the lob, and start is 1.
   **/
  public void Var043() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          String findString = MEDIUM_.substring(14, 17);
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(findString), (long) 1);

          assertCondition(v == 15, "v = " + v + " SB 15");

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found
   * in the middle of the lob, and start is before where the pattern appears.
   **/
  public void Var044() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          String findString = MEDIUM_.substring(9, 13);
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(findString), (long) 7);

          assertCondition(v == 10, "v = " + v + " SB 10");

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found
   * in the middle of the lob, and start is right where the pattern appears.
   **/
  public void Var045() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          String findString = MEDIUM_.substring(8, 12);
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(findString), (long) 9);

          assertCondition(v == 9, "v=" + v + " sb 9");

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found
   * at the end of the lob, and start is 1.
   **/
  public void Var046() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          int length = MEDIUM_.length();
          String findString = MEDIUM_.substring(length - 1, length);
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(findString), (long) 1);

          assertCondition(v == length, "position returned " + v + " sb "
              + length + " for " + findString + " in " + MEDIUM_);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found
   * at the end of the lob, and start is right where the pattern occurs.
   **/
  public void Var047() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          int length = MEDIUM_.length();
          String findString = MEDIUM_.substring(length - 4, length);
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(findString), (long) length - 6);

          assertCondition(v == length - 3, "v = " + v + " SB " + (length - 3));

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return 0 when the pattern is the empty lob and
   * start is 1.
   **/
  public void Var048() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(""), (long) 1);

          assertCondition(v == 1);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return start when the pattern is the empty lob
   * and start is in the middle.
   **/
  public void Var049() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(""), (long) 9);

          assertCondition(v == 9);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return length-1 when the pattern is the empty
   * lob and start is the length-1.
   **/
  public void Var050() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs1_.absolute(2);
          Object clob = JDReflectionUtil
              .callMethod_O(rs1_, getMethod, "C_CLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(""), (long) MEDIUM_.length());

          assertCondition(v == MEDIUM_.length());

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * Verify Clobs live beyond the life of the row.
   **/
  public void Var051() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        Statement s = null;
        try {
          s = connection_.createStatement();
          ResultSet rs = statement2_.executeQuery("SELECT * FROM " + TABLE2_);

          rs.absolute(2);
          Object clob1 = JDReflectionUtil.callMethod_O(rs, getMethod, "C_CLOB");
          rs.next();
          Object clob2 = JDReflectionUtil.callMethod_O(rs, getMethod, "C_CLOB");
          rs.next();
          Object clob3 = JDReflectionUtil.callMethod_O(rs, getMethod, "C_CLOB");

          if ((JDLobGraphicData.checkResults(mediumClob,
              (String) JDReflectionUtil.callMethod_O(clob1, "getSubString",
                  (long) 1,
                  (int) JDReflectionUtil.callMethod_L(clob1, "length"))))
              && (JDLobGraphicData.checkResults(medium2Clob,
                  (String) JDReflectionUtil.callMethod_O(clob2, "getSubString",
                      (long) 1,
                      (int) JDReflectionUtil.callMethod_L(clob2, "length"))))
              && (JDLobGraphicData.checkResults(largeClob,
                  (String) JDReflectionUtil.callMethod_O(clob3, "getSubString",
                      (long) 1,
                      (int) JDReflectionUtil.callMethod_L(clob3, "length")))))
            assertCondition(true);
          else
            failed("data mismatch");

          // On 10/12/2009 Moved close to down here.
          try {
            rs.close();
            connection_.commit();
            rs1_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);

          } catch (Exception e) {
            e.printStackTrace(System.out);
          }

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
        if (s != null)
          try {
            s.close();
          } catch (Exception e) {
          }
      }
    }
  }

  /**
   * Verify Clobs live beyond the life of the row.
   **/
  public void Var052() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        Statement s = null;
        try {
          s = connection_.createStatement();
          ResultSet rs = statement2_.executeQuery("SELECT * FROM " + TABLE2_);

          rs.absolute(2);
          Object clob1 = JDReflectionUtil.callMethod_O(rs, getMethod, "C_CLOB");
          rs.next();
          Object clob2 = JDReflectionUtil.callMethod_O(rs, getMethod, "C_CLOB");
          rs.next();
          Object clob3 = JDReflectionUtil.callMethod_O(rs, getMethod, "C_CLOB");

          Reader resultReader1 = (Reader) JDReflectionUtil.callMethod_O(clob1,
              "getCharacterStream");
          Reader resultReader2 = (Reader) JDReflectionUtil.callMethod_O(clob2,
              "getCharacterStream");
          Reader resultReader3 = (Reader) JDReflectionUtil.callMethod_O(clob3,
              "getCharacterStream");

          int c;
          StringBuffer buffer = new StringBuffer();

          while (0 <= (c = resultReader1.read()))
            buffer.append((char) c);
          String s1 = new String(buffer);

          buffer.setLength(0);
          while (0 <= (c = resultReader2.read()))
            buffer.append((char) c);
          String s2 = new String(buffer);

          buffer.setLength(0);
          while (0 <= (c = resultReader3.read()))
            buffer.append((char) c);
          String s3 = new String(buffer);

          if ((JDLobGraphicData.checkResults(mediumClob, s1))
              && (JDLobGraphicData.checkResults(medium2Clob, s2))
              && (JDLobGraphicData.checkResults(largeClob, s3)))
            assertCondition(true);
          else
            failed("data mismatch");

          // On 10/12/2009 Moved close to down here.
          try {
            rs.close();
            connection_.commit();
            rs1_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);

          } catch (Exception e) {
            e.printStackTrace(System.out);
          }

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
        if (s != null)
          try {
            s.close();
          } catch (Exception e) {
          }
      }
    }
  }

  /**
   * Verify Clobs live beyond the life of the row.
   **/
  public void Var053() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (skipAsciiTests) {
      notApplicable("getAsciiStreamTest");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        Statement s = null;
        try {
          s = connection_.createStatement();
          ResultSet rs = statement2_.executeQuery("SELECT * FROM " + TABLE2_);

          rs.absolute(2);
          Object clob1 = JDReflectionUtil.callMethod_O(rs, getMethod, "C_CLOB");
          rs.next();
          Object clob2 = JDReflectionUtil.callMethod_O(rs, getMethod, "C_CLOB");
          rs.next();
          Object clob3 = JDReflectionUtil.callMethod_O(rs, getMethod, "C_CLOB");

          InputStream in1 = (InputStream) JDReflectionUtil.callMethod_O(clob1,
              "getAsciiStream");
          InputStream in2 = (InputStream) JDReflectionUtil.callMethod_O(clob2,
              "getAsciiStream");
          InputStream in3 = (InputStream) JDReflectionUtil.callMethod_O(clob3,
              "getAsciiStream");

          InputStreamReader resultReader1 = new InputStreamReader(in1);
          InputStreamReader resultReader2 = new InputStreamReader(in2);
          InputStreamReader resultReader3 = new InputStreamReader(in3);

          int c;
          StringBuffer buffer = new StringBuffer();

          while (0 <= (c = resultReader1.read()))
            buffer.append((char) c);
          String s1 = new String(buffer);

          buffer.setLength(0);
          while (0 <= (c = resultReader2.read()))
            buffer.append((char) c);
          String s2 = new String(buffer);

          buffer.setLength(0);
          while (0 <= (c = resultReader3.read()))
            buffer.append((char) c);
          String s3 = new String(buffer);

          if ((JDLobGraphicData.checkResults(mediumClob, s1))
              && (JDLobGraphicData.checkResults(medium2Clob, s2))
              && (JDLobGraphicData.checkResults(largeClob, s3)))
            assertCondition(true);
          else
            failed("data mismatch");

          // On 10/12/2009 Moved close to down here.
          try {
            rs.close();
            connection_.commit();
            rs1_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);

          } catch (Exception e) {
            e.printStackTrace(System.out);
          }

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
        if (s != null)
          try {
            s.close();
          } catch (Exception e) {
          }
      }
    }
  }

  /**
   * setString(long, String) - Should throw an exception when start is less than
   * 0.
   **/
  public void Var054() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "setString", (long) -1,
            "Really small objects");
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
  public void Var055() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "setString", (long) 0,
            "Really small object.");
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
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var056() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "setString",
            (long) MEDIUM_.length() + 1, ASSIGNMENT1_);
        succeeded();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  // @C2C Changed to not expect an exception
  /**
   * setString(long, String) - Changed to not expect an exception when length of
   * string to insert is greater than length of lob.
   **/
  public void Var057() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        String expected = new String(ASSIGNMENT1_);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, expected);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        // Checking method
        Clob clob1 = rs2_.getClob("C_CLOB");
        assertCondition(clob1.getSubString(1, expected.length()).equals(
            expected)
            && written == expected.length());
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Should throw an exception on an empty lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var058() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil
            .callMethod_I(clob, "setString", (long) 1, ASSIGNMENT1_);
        succeeded();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Should work to set all of a non-empty lob.
   **/
  public void Var059() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, ASSIGNMENT1_);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");

        if (CCSID_ == 5035 && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          notApplicable("09/13/07: Toolbox issue 30040 opened for hostserver.  They say it is a permanant restriction and do not plan to fix it.  ");
          return;
        }

        String info = clob1.getSubString(1, ASSIGNMENT1_.length());
        assertCondition(
            info.equals(ASSIGNMENT1_) && written == ASSIGNMENT1_.length(),
            "Got '" + info + "' sb '" + ASSIGNMENT1_ + "' written=" + written
                + " sb " + ASSIGNMENT1_.length());
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Should work to set the first part of a non-empty
   * lob.
   **/
  public void Var060() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, ASSIGNMENT1_);
        int length = ASSIGNMENT1_.length();
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        assertCondition(clob1.getSubString(1, length).equals(ASSIGNMENT1_)
            && written == length);
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Should work to set the middle part of a non-empty
   * lob.
   **/
  public void Var061() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, ASSIGNMENT_CHAR4_);
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K3
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length(); // @K3
        assertCondition(clob1.getSubString(5, 4).equals(ASSIGNMENT_CHAR4_)
            && written == 4 && length1 == length2); // @K3 : added
                                                    // length1==length2 as we do
                                                    // not expect any
                                                    // truncations !
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Should fail since var. 61 truncated lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception. Also, variations should not
   *      be dependent upon each other, since a testcase can execute an
   *      arbitrary number of its variations.
   **/
  public void Var062() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 10, ASSIGNMENT_CHAR5_);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        assertCondition(written == 5, "written=" + written + " sb 5");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected exception");
      }
    }
  }

  /**
   * setString(long, String) - Should work to set the last part of a non-empty
   * lob.
   **/
  public void Var063() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 8, ASSIGNMENT_CHAR7_);
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K4
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length(); // @K4
        String substring = clob1.getSubString(8, 7);
        assertCondition(substring.equals(ASSIGNMENT_CHAR7_) && written == 7
            && length1 == length2, "!substring(" + substring + ").equals('"
            + ASSIGNMENT_CHAR7_ + "') or written(" + written
            + ")!=7 or length1(" + length1 + ")!= length2(" + length2 + ")"); // @K4
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when start is
   * less than 0.
   **/
  public void Var064() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_I(clob, "setString", (long) -1,
            "Really green objects", 0, 20); /* @C5 */
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
  public void Var065() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_I(clob, "setString", (long) 0,
            "Really shiny object.", 0, 20); /* @C5 */
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
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var066() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_I(clob, "setString",
            (long) MEDIUM_.length() + 1, "Really shiny objects", 0, 20); /* @C5 */
        succeeded();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  // @C2C Changed to not expect exception
  /**
   * setString(long, String, int, int) - Should not throw an exception when
   * length of string to insert is greater than length of lob.
   **/
  public void Var067() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        String expected = "Really really scary objects";
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, expected, 0, 27);/* @C5 */
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        assertCondition(clob1.getSubString(1, expected.length()).equals(
            expected) // @C3C
            && written == expected.length());
      } catch (Exception e) {
        try {
          failed(connection_, e, "Unexpected Exception");
          rs2_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception on an empty
   * lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception. And if you think about it,
   *      this variation doesn't make sense... if setting data into an empty LOB
   *      is supposed to throw an exception, how will I ever be able to get a
   *      LOB that has data in it?
   **/
  public void Var068() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_I(clob, "setString", (long) 1,
            "Really small objects", 0, 20); /* @C5 */
        // failed ("Didn't throw SQLException");
        succeeded();
      } catch (Exception e) {
        // try{
        // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        // rs2_.close();
        // }
        // catch (Exception s) {
        failed(connection_, e, "Unexpected Exception");
        // }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should work to set all of a non-empty
   * lob.
   **/
  public void Var069() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {

      try {
        String expected = "Really tiny objects.";
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, expected, 0, 20); /* @C5 */
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        String answer = clob1.getSubString(1, MEDIUM_.length());
        assertCondition(answer.equals(expected) && written == 20, "got '"
            + answer + "' sb '" + expected + "' written=" + written + " sb 20");
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should work to set the first part of a
   * non-empty lob.
   **/
  public void Var070() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, "Really small", 0, 12); /* @C5 */
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K4
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = JDReflectionUtil.callMethod_L(clob, "length"); // @K4
        String substring = clob1.getSubString(1, 12);
        assertCondition(substring.equals("Really small") && written == 12
            && length1 == length2, "!substring(" + substring
            + ").equals('Really small.') or written(" + written
            + ")!=12 or length1(" + length1 + ")!= length2(" + length2 + ")"); // @K4
                                                                               // :
                                                                               // added
                                                                               // length1
                                                                               // ==
                                                                               // length2
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should work to set the middle part of a
   * non-empty lob.
   **/
  public void Var071() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, ASSIGNMENT_CHAR4_, 0, 4); /* @C5 */
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K4
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length(); // @K4
        String substring = clob1.getSubString(5, 4);
        assertCondition(substring.equals(ASSIGNMENT_CHAR4_) && written == 4
            && length1 == length2, "!substring(" + substring
            + ").equals('cool') or written(" + written + ")!=4 or length1("
            + length1 + ")!= length2(" + length2 + ")"); // @K4
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Should fail since var. 71 truncated lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var072() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 10, ASSIGNMENT_CHAR7_);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        assertCondition(written == 7, "written=" + written + " sb 7");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected exception");
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should work to set the last part of a
   * non-empty lob.
   **/
  public void Var073() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 8, ASSIGNMENT_CHAR7_, 0, 7); /* @C5 */
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K3
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length(); // @K3
        String substring = clob1.getSubString(8, 7);
        assertCondition(substring.equals(ASSIGNMENT_CHAR7_) && written == 7
            && length1 == length2, "!substring(" + substring
            + ").equals('insect.') or written(" + written + ")!=7 or length1("
            + length1 + ")!= length2(" + length2 + ")"); // @K3
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * truncate() - Should throw an exception if you try to truncate to a length
   * less than 0.
   **/
  public void Var074() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "truncate", (long) -1);
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
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var075() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "truncate", (long) 25);
        rs2_.close();
        succeeded();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * truncate() - Should work on a non-empty lob.
   **/
  public void Var076() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "truncate", (long) 2);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        assertCondition(clob1.length() == 2);
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setAsciiStream() - When the lob is not empty starting at position 1.
   **/
  public void Var077() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (skipAsciiTests) {
      notApplicable("getAsciiStreamTest");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        OutputStream w = (OutputStream) JDReflectionUtil.callMethod_O(clob,
            "setAsciiStream", (long) 1);
        byte[] b = new byte[] { (byte) 65, (byte) 66, (byte) 67 };
        w.write(b);
        w.close();
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        InputStream i = clob1.getAsciiStream();
        sb.setLength(0);
        assertCondition(compare(i, b, sb), sb); // @C4C
        rs2_.close();
        var077Called_ = true;
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setAsciiStream() - When the lob is not empty starting at a position other
   * than 1.
   **/
  public void Var078() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (skipAsciiTests) {
      notApplicable("getAsciiStreamTest");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        OutputStream w = (OutputStream) JDReflectionUtil.callMethod_O(clob,
            "setAsciiStream", (long) 2);
        byte[] b = new byte[] { (byte) 32, (byte) 33, (byte) 34 };
        w.write(b);
        w.close();
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        InputStream i = clob1.getAsciiStream();
        // @C4A Since we added bytes to position 2 here, we must read back 4
        // bytes
        // @C4A and expect a (byte) 1 in the first position since that's what
        // @C4A variation 77 put there.
        // @C4Dint read = i.read(b, 0, 3);
        byte[] newBytes = null;
        if (var077Called_) {
          newBytes = new byte[] { (byte) 65, (byte) 32, (byte) 33, (byte) 34 }; // @C4A
        } else {
          newBytes = b;
        }
        sb.setLength(0);
        assertCondition(compare(i, newBytes, sb), sb); // @C4C
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setAsciiStream() - Should throw an exception if you try to set a stream
   * that starts after the lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var079() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        OutputStream w = (OutputStream) JDReflectionUtil.callMethod_O(clob,
            "setAsciiStream", (long) 24);
        w.close();
        rs2_.close();

        succeeded();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setAsciiStream() - Should throw an exception if you try to set a stream
   * that starts at 0.
   **/
  public void Var080() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        OutputStream w = (OutputStream) JDReflectionUtil.callMethod_O(clob,
            "setAsciiStream", (long) 0);
        failed("Didn't throw SQLException" + w);
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
   * setAsciiStream() - Should throw an exception if you try to set a stream
   * that starts before 0.
   **/
  public void Var081() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        OutputStream w = (OutputStream) JDReflectionUtil.callMethod_O(clob,
            "setAsciiStream", (long) -1);
        failed("Didn't throw SQLException" + w);
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
  public void Var082() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        String cbuf = "How are you doing???";
        w.write(cbuf);
        w.close();
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        assertCondition(compare(r, "How are you doing???", sb), sb);
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at a position
   * other than 1.
   **/
  public void Var083() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 2);
        String cbuf = "How are you today???";
        w.write(cbuf);
        w.close();
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        Reader r = clob1.getCharacterStream();
        // Since we started at position 2 this time, must expect an extra letter
        // 'H' in the first position from testcase 82.
        sb.setLength(0);
        assertCondition(compare(r, "HHow are you today???", sb), sb); // @C4C
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - Should throw an exception if you try to set a stream
   * that starts after the lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var084() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 24);
        w.close();
        rs2_.close();
        succeeded();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - Should throw an exception if you try to set a stream
   * that starts at 0.
   **/
  public void Var085() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 0);
        failed("Didn't throw SQLException" + w);
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
  public void Var086() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) -1);
        failed("Didn't throw SQLException" + w);
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
   * setString(long, String) - Should update clob with an unchanged clob.
   **/
  public void Var087() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(2);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(2);
        Clob clob1 = rs2_.getClob("C_CLOB");
        String v = clob1.getSubString(1, MEDIUM_.length());
        assertCondition(v.equals(MEDIUM_), "got '" + v + "' sb '" + MEDIUM_
            + "'");
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateClob(String, clob) - Should not change lob if updateRow() is not
   * called.
   **/
  public void Var088() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(2);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(2);
        Clob clob1 = rs2_.getClob("C_CLOB");
        String v = clob1.getSubString(1, MEDIUM_.length());
        assertCondition(v.equals(MEDIUM_));
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Make sure lob in database is not changed if
   * updateRow() was not called.
   **/
  public void Var089() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(2);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 2, "New updates");
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(2);
        Clob clob1 = rs2_.getClob("C_CLOB");
        String v = clob1.getSubString(1, MEDIUM_.length());
        assertCondition(v.equals(MEDIUM_), "wrote " + written + " characters ");
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Should work to set all of a non-empty lob with
   * multiple updates to the same position.
   **/
  public void Var090() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K4
        String expected = new String("february");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, expected);
        String expected2 = new String("march");
        int written2 = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, expected2);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length(); // @K4
        assertCondition(clob1.getSubString(5, expected2.length()).equals(
            expected2)
            && written == expected.length()
            && written2 == expected2.length()
            && clob1.getSubString(5 + expected2.length(), 3).equals("ary") // @K4
            && length1 == length2); // @K4
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Should work to set strings to a non-empty lob
   * with multiple updates to the different positions.
   **/
  public void Var091() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K4
        String expected = new String("april");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, expected);
        String expected2 = new String("may");
        int written2 = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 9, expected2);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length(); // @K4
        assertCondition(clob1.getSubString(5,
            expected.length() + expected2.length() - 1).equals("aprimay") // @C3C
            && written == expected.length()
            && written2 == expected2.length()
            && length1 == length2);

        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Should work to set strings to a non-empty lob
   * with multiple updates to the same position with updateClob called between
   * updates.
   **/
  public void Var092() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        String expected = new String("february");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, expected);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        String expected2 = new String("march");
        int written2 = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, expected2);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        assertCondition(clob1.getSubString(5, expected2.length()).equals(
            expected2)
            && written == expected.length() && written2 == expected2.length());
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String) - Should work to set strings to a non-empty lob
   * with multiple updates to the different positions with updateClob called
   * between updates.
   **/
  public void Var093() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        String expected = new String("april");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, expected);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        String expected2 = new String("may");
        int written2 = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 9, expected2);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        assertCondition(clob1.getSubString(5,
            expected.length() + expected2.length() - 1).equals("aprimay")
            && written == expected.length() && written2 == expected2.length());
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when string
   * offset is less than 0.
   **/
  public void Var094() // @C5
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_I(clob, "setString",
            (long) MEDIUM_.length() + 1, "Really shiny objects", -1, 20); // @C4
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
   * setString(long, String, int, int) - Should throw an exception when the
   * string is null
   **/
  public void Var095() // @C5
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_I(clob, "setString",
            (long) MEDIUM_.length() + 1, null, 0, 20); // @C4
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
  public void Var096() // @C5
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_I(clob, "setString",
            (long) MEDIUM_.length() + 1, "Really shiny objects",
            "Really shiny objects".length() + 1, 20); // @C4
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

  // @C6A
  /**
   * DBClobs- Make sure that RLE compression bug with more than 32K of one
   * repeating character is fixed.
   **/
  public void Var097() {
    String filename = JDLobTest.COLLECTION + ".JDLCL97";

    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      if (isJdbc20()) {
        ResultSet rs = null;
        try {
          FileWriter f = new FileWriter("mydbclob.dat");
          for (int i = 0; i < 38000; i++) {
            f.write('S');
          }
          f.flush();
          f.close();
          FileReader dbclobReader = new FileReader("mydbclob.dat");
          // Make sure it is gone
          try {
            statement2_.executeUpdate("DROP TABLE " + filename);
          } catch (Exception e) {
          }

          statement2_.executeUpdate("CREATE TABLE " + filename
              + " (id INTEGER, str DBCLOB(38000) CCSID 13488)");

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + filename + " VALUES (?,?)");
          ps.setInt(1, 1);
          ps.setCharacterStream(2, dbclobReader, 38000);
          ps.executeUpdate();
          dbclobReader.close();

          PreparedStatement ps2 = connection_.prepareStatement("SELECT * FROM "
              + filename + "");
          rs = ps2.executeQuery();
          rs.next();
          Object clob = JDReflectionUtil.callMethod_O(rs, getMethod, 2);
          long length = JDReflectionUtil.callMethod_L(clob, "length");
          String myString = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", (long) 1, 10);
          String myString2 = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", (long) 37991, 10);
          rs.close();
          statement2_.executeUpdate("DROP TABLE " + filename);
          assertCondition(myString.equals("SSSSSSSSSS")
              && myString2.equals("SSSSSSSSSS") && (length == 38000));
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
        try {
          connection_.commit();
          rs1_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);

        } catch (Exception e) {
          e.printStackTrace(System.out);
        }

      }
    } else
      notApplicable("Toolbox variation only");
  }

  // @C6A
  /**
   * Clobs- Make sure that RLE compression bug with more than 32K of one
   * repeating character is fixed.
   **/
  public void Var098() {

    String filename = JDLobTest.COLLECTION + ".JDLCL98";

    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      if (isJdbc20()) {
        ResultSet rs = null;
        try {
          FileWriter f = new FileWriter("mydbclob.dat");
          for (int i = 0; i < 38000; i++) {
            f.write('S');
          }
          f.flush();
          f.close();
          FileReader dbclobReader = new FileReader("mydbclob.dat");

          try {
            statement2_.executeUpdate("DROP TABLE " + filename);
          } catch (Exception e) {
          }
          statement2_.executeUpdate("CREATE TABLE " + filename
              + " (id INTEGER, str CLOB(38000))");

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + filename + " VALUES (?,?)");
          ps.setInt(1, 1);
          ps.setCharacterStream(2, dbclobReader, 38000);
          ps.executeUpdate();
          dbclobReader.close();

          PreparedStatement ps2 = connection_.prepareStatement("SELECT * FROM "
              + filename + "");
          rs = ps2.executeQuery();
          rs.next();
          Object clob = JDReflectionUtil.callMethod_O(rs, getMethod, 2);
          long length = JDReflectionUtil.callMethod_L(clob, "length");
          String myString = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", (long) 1, 10);
          String myString2 = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", (long) 37991, 10);
          rs.close();
          statement2_.executeUpdate("DROP TABLE " + filename + "");
          assertCondition(myString.equals("SSSSSSSSSS")
              && myString2.equals("SSSSSSSSSS") && (length == 38000));
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
        try {
          connection_.commit();
          rs1_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);

        } catch (Exception e) {
          e.printStackTrace(System.out);
        }
      }
    } else
      notApplicable("Toolbox variation only");
  }

  // @k1A
  /**
   * setString(long, String) - Should work to set all of a non-empty lob.
   **/
  public void Var099() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, "Really huge objects.");
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation");
      }
    }
  }

  // @k1A
  /**
   * setString(long, String) - Should work to set the first part of a non-empty
   * lob.
   **/
  public void Var100() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, "Really small");
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation");
      }
    }
  }

  // @k1A
  /**
   * setString(long, String) - Should work to set the middle part of a non-empty
   * lob.
   **/
  public void Var101() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, ASSIGNMENT_CHAR4_);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation");
      }
    }
  }

  // @K1A
  /**
   * setString(long, String) - Should work to set the last part of a non-empty
   * lob.
   **/
  public void Var102() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 14, ASSIGNMENT_CHAR7_);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation");
      }
    }
  }

  // @k1A
  /**
   * setString(long, String, int, int) - Should work to set all of a non-empty
   * lob.
   **/
  public void Var103() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, "Really tiny objects.", 0, 20);// @C4
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation");
      }
    }
  }

  // @k1A
  /**
   * setString(long, String, int, int) - Should work to set the first part of a
   * non-empty lob.
   **/
  public void Var104() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, "Really small", 0, 12); // @C4
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation");
      }
    }
  }

  // @k1A
  /**
   * setString(long, String, int, int) - Should work to set the middle part of a
   * non-empty lob.
   **/
  public void Var105() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, ASSIGNMENT_CHAR4_, 0, 4);// @C4
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation");
      }
    }
  }

  // @K1A
  /**
   * setString(long, String, int, int) - Should work to set the last part of a
   * non-empty lob.
   **/
  public void Var106() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 14, ASSIGNMENT_CHAR7_, 0, 7); // @C4
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written);
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation");
      }
    }
  }

  // @k2 DBCLOB
  /**
   * position(String,long) - Should return 1 when the pattern is found at the
   * beginning of the lob.
   **/
  public void Var107() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          statement_.executeQuery("Select 'VAR107' FROM SYSIBM.SYSDUMMY1");
          rs3_ = statement3_.executeQuery("SELECT * FROM " + TABLE3_);
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String beginningString = DBCLOB_MEDIUM_.substring(0, 7);
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              beginningString, (long) 1);

          boolean condition = (v == 1);

          if (!condition) {
            skipCleanup = true;
            rs3_ = statement3_.executeQuery("SELECT * FROM " + TABLE3_);
            rs3_.absolute(2);
            String data = rs3_.getString("C_DBCLOB");

            System.out.println("clob.position('" + beginningString
                + "',1) returned " + v + " for " + data + " clobClass="
                + clob.getClass().getName());
            System.out.println("clob.position('" + beginningString
                + "',1) returned " + v + " for "
                + JDTestUtilities.getMixedString(data) + " clobClass="
                + clob.getClass().getName());

            System.out.println("Hex search: "
                + JDTestUtilities.dumpBytes(beginningString));
            System.out
                .println("Hex value : " + JDTestUtilities.dumpBytes(data));
          }
          assertCondition(condition);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * truncate() - 0
   **/
  public void Var108() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        statement_.executeQuery("Select 'VAR108' FROM SYSIBM.SYSDUMMY1");
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE4_); // @K5C
                                                                     // changed
                                                                     // so
                                                                     // variations
                                                                     // aren't
                                                                     // dependent
                                                                     // on each
                                                                     // other
        rs2_.absolute(1); // @K5C
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "truncate", 0L);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE4_); // @K5C
        rs2_.absolute(1); // @K5C
        Clob clob1 = rs2_.getClob("C_CLOB");
        rs2_.close();
        assertCondition(clob1.length() == 0);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * truncate() - 1
   **/
  public void Var109() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE4_); // @K5C
        rs2_.absolute(2); // @K5C
        Object clob_ = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_I(clob_, "setString", 1, "ABCD");
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob_);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE4_); // @K5C
        rs2_.absolute(2); // @K5C
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "truncate", (long) 1);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE4_); // @K5C
        rs2_.absolute(2); // @K5C
        Clob clob1 = rs2_.getClob("C_CLOB");
        rs2_.close();
        assertCondition(clob1.length() == 1);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write an int
   **/
  public void Var110() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE4_; // @K5C
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(3); // @K5C
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        w.write(66); // ASCII value of 'B' //@K5C
        w.close();
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(3); // @K5C
        Clob clob1 = rs2_.getClob("C_CLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        String expected = "B" + SMALL_.substring(1);
        assertCondition(compare(r, expected, sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB '" + expected
                + "'"); // @K5C
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }

  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write an array of characters
   **/
  public void Var111() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE4_; // @K5C
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        char c[] = { 'I', 'T', ' ', 'W', 'O', 'R', 'K', 'S', '!' };
        w.write(c);
        w.close();
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        String expected = "IT WORKS!" + SMALL_.substring(9);
        assertCondition(compare(r, expected, sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB \"" + expected
                + "\""); // @K5A
        // @K5D assertCondition(compare (r, "IT WORKS!"), clob1.getSubString(1,
        // (int)clob1.length())+" SB \"IT WORKS!\"");
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }

  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write an ARRAY OF CHARACTERS with an offset specified as 0
   **/
  public void Var112() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE4_; // @K5C
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(5); // @K5C
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        char c[] = { 'H', 'U', 'R', 'R', 'A', 'Y', '!' };
        w.write(c, 0, c.length);
        w.close();
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(5); // @K5C
        Clob clob1 = rs2_.getClob("C_CLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        String expected = "HURRAY!" + SMALL_.substring(7);
        assertCondition(compare(r, expected, sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB \"" + expected
                + "\""); // @K5A

        // @K5D assertCondition(compare (r, "HURRAY!S!"), clob1.getSubString(1,
        // (int)clob1.length())+" SB \"HURRAY!S!\"");
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }

  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write an ARRAY OF CHARACTERS with an offset specified not as
   * 0
   **/
  public void Var113() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE4_; // @K5C
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(6); // @K5C
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        char c[] = { 'H', 'U', 'R', 'R', 'A', 'Y', '!' };
        w.write(c, 2, c.length - 2);
        w.close();
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(6); // @K5C
        Clob clob1 = rs2_.getClob("C_CLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        String expected = "RRAY!" + SMALL_.substring(5);
        assertCondition(compare(r, expected, sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB \"" + expected
                + "\""); // @K5A

        // @K5D assertCondition(compare (r, "RRAY!Y!S!"), clob1.getSubString(1,
        // (int)clob1.length())+" SB \"RRAY!Y!S!\"");
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }

  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write an ARRAY OF CHARACTERS with an offset specified LESS
   * than 0
   **/
  public void Var114() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE4_; // @K5C
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(7); // @K5C
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        char c[] = { 'H', 'U', 'R', 'R', 'A', 'Y', '!' };
        w.write(c, -1, c.length);
        w.close();
        rs2_.close();
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) // @K5A
          failed("Didn't throw an ExtendedIllegalArgumentException"); // @K5A
        else
          // @K5A
          failed("Didn't throw an INDEXOUTOFCOUNDSEXCEPTION!!");
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) // @K5A
          assertExceptionIsInstanceOf(e,
              "com.ibm.as400.access.ExtendedIllegalArgumentException"); // @K5A
        else
          // @K5A
          assertExceptionIsInstanceOf(e, "java.lang.IndexOutOfBoundsException");
      }
    }

  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write a string with offset specified other than 0
   **/
  public void Var115() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE4_; // @K5C
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(8); // @K5C
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        String str = "CLOB WRITER TEST!";
        w.write(str, 5, str.length() - 5);
        w.close();
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(8); // @K5C
        Clob clob1 = rs2_.getClob("C_CLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);

        String expected = "WRITER TEST!" + SMALL_.substring(12);
        assertCondition(compare(r, expected, sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB \"" + expected
                + "\""); // @K5A
        // @K5D assertCondition(compare (r, "WRITER TEST!"),
        // clob1.getSubString(1, (int)clob1.length())+" SB \"WRITER TEST!\"");
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write a string with offset specified other than 0
   **/
  public void Var116() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE4_; // @K5C
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(9); // @K5C
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        String str = "CLOB WRITER TESTING!";
        w.write(str, 1, str.length() - 2);
        w.close();
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(9); // @K5C
        Clob clob1 = rs2_.getClob("C_CLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        String expected = "LOB WRITER TESTING" + SMALL_.substring(18);
        assertCondition(compare(r, expected, sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB \"" + expected
                + "\""); // @K5A

        /*
         * assertCondition(compare (r, "LOB WRITER TESTINGt.",sb),
         * clob1.getSubString(1,
         * (int)clob1.length())+" SB \"LOB WRITER TESTINGt.\""); //@K5A
         */
        // @K5Ds assertCondition(compare (r, "LOB WRITER TESTING"),
        // clob1.getSubString(1,
        // (int)clob1.length())+" SB \"LOB WRITER TESTING\"");
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when length passed exceeds
   * maxLength_ of clob
   **/
  public void Var117() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      try {
        rs1_.absolute(2);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        // Updated 9/30/2013 -- there is no way to exceed the maximum
        // size of the lob..
        JDReflectionUtil.callMethod_O(clob, "getSubString", 1, 0x7FFFFFFF);
        String v = (String) JDReflectionUtil.callMethod_O(clob, "getSubString",
            1, MEDIUM_.length());
        assertCondition(v.equals(MEDIUM_),
            "Updated 9/30/2013 allow exceed max length of the current clob");
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception Updated 9/30/2013. Allow exceed of maxlength of clob");
      }

    }
  }

  /**
   * setString(long, int) - Is data (to be set) truncated if it exceeds the max.
   * length of the clob
   **/
  public void Var118() // @K6: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      if (checkUpdateableLobsSupport()) {
        try {
          String statement = "SELECT * FROM " + TABLE_;
          rs2_ = statement2_.executeQuery(statement);
          rs2_.absolute(4);
          String str = "m";
          Object clob = JDReflectionUtil
              .callMethod_O(rs2_, getMethod, "C_CLOB");
          for (int i = 1; i < 32760; i *= 2)
            str += str;

          long written = JDReflectionUtil.callMethod_I(clob, "setString",
              (long) 1, str);

          long length1 = JDReflectionUtil.callMethod_L(clob, "length");
          JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
          rs2_.updateRow();
          rs2_.close();
          rs2_ = statement2_.executeQuery(statement);
          rs2_.absolute(4);
          Clob clob1 = rs2_.getClob("C_CLOB");
          long length2 = clob1.length();
          assertCondition(written == 30000 && length1 == length2, "written = "
              + written + " SB 30000" + " length1 = " + length1
              + " SB same as length2 = " + length2);

          rs2_.close();

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    } else {
      notApplicable("Native driver variation from v5r3m0 onwards!");
    }
  }

  /**
   * setString(long, int) = will prePadding be done for incoming Data?
   **/
  public void Var119() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE_;
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Object clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "truncate", (long) 2);
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        clob = JDReflectionUtil.callMethod_O(rs2_, getMethod, "C_CLOB");
        long written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, "Minnesota");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        JDReflectionUtil.callMethod_V(rs2_, updateMethod, "C_CLOB", clob);
        rs2_.updateRow();
        rs2_.close();
        rs2_ = statement2_.executeQuery(statement);
        rs2_.absolute(4);
        Clob clob1 = rs2_.getClob("C_CLOB");
        long length2 = clob1.length();
        assertCondition(written == 9 && length1 == length2, "written = "
            + written + " SB 9" + " length1 = " + length1
            + " SB same as length2 = " + length2);
        rs2_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception");
      }
    }

  }

  // Repeat testcases for DBCLOB!!!!

  /**
   * getAsciiStream() - When the lob is empty.
   **/
  public void Var120() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (skipAsciiTests) {
      notApplicable("getAsciiStreamTest");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(1);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          InputStream v = (InputStream) JDReflectionUtil.callMethod_O(clob,
              "getAsciiStream");
          sb.setLength(0);
          assertCondition(compare(v, "", "8859_1", sb), DBCLOB_ADDED + " " + sb);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getAsciiStream() - When the lob is not empty.
   **/
  public void Var121() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (skipAsciiTests) {
      notApplicable("getAsciiStreamTest");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          statement_.executeQuery("Select 'VAR121' FROM SYSIBM.SYSDUMMY1");
          sb.setLength(0);
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          InputStream v = (InputStream) JDReflectionUtil.callMethod_O(clob,
              "getAsciiStream");
          boolean passed = compare(v, DBCLOB_MEDIUM_, "8859_1", sb);
          assertCondition(passed, sb.toString() + DBCLOB_ADDED);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getAsciiStream() - When the lob is full.
   **/
  public void Var122() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (skipAsciiTests) {
      notApplicable("getAsciiStreamTest");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          statement_.executeQuery("Select 'VAR122' FROM SYSIBM.SYSDUMMY1");
          rs3_.absolute(3);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          InputStream v = (InputStream) JDReflectionUtil.callMethod_O(clob,
              "getAsciiStream");
          sb.setLength(0);
          boolean passed = compare(v, DBCLOB_LARGE_, "8859_1", sb);
          assertCondition(passed, sb.toString() + DBCLOB_ADDED);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  public void Var123() {
    notApplicable();
  }

  /**
   * getCharacterStream() - When the lob is empty.
   **/
  public void Var124() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {

          rs3_.absolute(1);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          Reader v = (Reader) JDReflectionUtil.callMethod_O(clob,
              "getCharacterStream");
          sb.setLength(0);
          assertCondition(compare(v, "", sb), DBCLOB_ADDED + sb);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getCharacterStream() - When the lob is not empty.
   **/
  public void Var125() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          statement_.executeQuery("Select 'VAR125' FROM SYSIBM.SYSDUMMY1");
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          Reader v = (Reader) JDReflectionUtil.callMethod_O(clob,
              "getCharacterStream");
          sb.setLength(0);
          assertCondition(compare(v, DBCLOB_MEDIUM_, sb), DBCLOB_ADDED + sb);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getCharacterStream() - When the lob is full.
   **/
  public void Var126() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          statement_.executeQuery("Select 'VAR125' FROM SYSIBM.SYSDUMMY1");
          rs3_.absolute(3);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          Reader v = (Reader) JDReflectionUtil.callMethod_O(clob,
              "getCharacterStream");
          sb.setLength(0);
          assertCondition(compare(v, DBCLOB_LARGE_, sb), DBCLOB_ADDED + sb);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when start is less than 0.
   **/
  public void Var127() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          JDReflectionUtil.callMethod_O(clob, "getSubString", -1, 5);
          failed("Didn't throw SQLException" + DBCLOB_ADDED);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when start is 0.
   **/
  public void Var128() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          JDReflectionUtil.callMethod_O(clob, "getSubString", 0, 5);
          failed("Didn't throw SQLException" + DBCLOB_ADDED);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when start is greater than the
   * max length of the lob.
   **/
  public void Var129() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          JDReflectionUtil.callMethod_O(clob, "getSubString",
              DBCLOB_MEDIUM_.length() + 2, 5);
          failed("Didn't throw SQLException" + DBCLOB_ADDED);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getSubString() - Should throw an exception when length is less than 0.
   **/
  public void Var130() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          JDReflectionUtil.callMethod_O(clob, "getSubString", 2, -1);
          failed("Didn't throw SQLException" + DBCLOB_ADDED);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getSubString() - Should work when length extends past the end of the lob.
   **/
  public void Var131() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");

          String s = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", 4, WIDTH_ + 2);

          // The latest javadoc implies that this should work.

          // Testcase changed 9/30/2013.
          String expected = DBCLOB_MEDIUM_.substring(3);
          assertCondition(s.equals(expected),
              "Changed 9/30/2013 -- getting a longer length should work"
                  + "\nGot '" + s + "'" + "\nExp '" + expected + "'");
        } catch (Exception e) {
          failed(
              connection_,
              e,
              "Changed 6/17/2013 -- getting a longer length starting at 4 should work.  MEDIUM_.length()="
                  + DBCLOB_MEDIUM_.length());
        }

      }
    }
  }

  /**
   * getSubString() - Should work on an empty lob.
   * 
   * SQL400 - The native driver expects this to be an error condition. You are
   * starting at position 1 of a lob with a length of 0. I have talked to Clif
   * about this and he was planning to change the Toolbox behavior to mimic
   * ours.
   **/
  public void Var132() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(1);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String v = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", 1, 0);
          assertCondition("".equals(v), "Expected \"\" got \"" + v + "\""
              + DBCLOB_ADDED);
        } catch (Exception e) {

          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve all of a non-empty lob.
   **/
  public void Var133() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String v = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", 1, DBCLOB_MEDIUM_.length());
          assertCondition(v.equals(DBCLOB_MEDIUM_), DBCLOB_ADDED);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve all of a full lob.
   * 
   * SQL400 - This is a testcase problem in that the size of the value put into
   * the lob was only 29998, not 30000. That is all you can expect to get back
   * out.
   **/
  public void Var134() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(3);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String v = null;

          v = (String) JDReflectionUtil.callMethod_O(clob, "getSubString", 1,
              DBCLOB_LARGE_.length());

          assertCondition(v.equals(DBCLOB_LARGE_), "got '" + v + "' sb '"
              + DBCLOB_LARGE_ + "' " + DBCLOB_ADDED);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve the first part of a non-empty lob.
   **/
  public void Var135() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String v = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", 1, 6);
          assertCondition(v.equals(DBCLOB_MEDIUM_.substring(0, 6)),
              DBCLOB_ADDED);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve the middle part of a non-empty
   * lob.
   **/
  public void Var136() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String v = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", 6, 7);
          assertCondition(v.equals(DBCLOB_MEDIUM_.substring(5, 12)),
              DBCLOB_ADDED);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * getSubString() - Should work to retrieve the last part of a non-empty lob.
   **/
  public void Var137() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String v = (String) JDReflectionUtil.callMethod_O(clob,
              "getSubString", 10, DBCLOB_MEDIUM_.length() - 9);
          assertCondition(v.equals(DBCLOB_MEDIUM_.substring(9)), DBCLOB_ADDED);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * length() - When the lob is empty.
   * 
   * SQL400 - The length of a lob is expected to be the length of the signficant
   * data in the lob. The Native driver returns 0 here as the lob is empty.
   **/
  public void Var138() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(1);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");

          assertCondition(JDReflectionUtil.callMethod_L(clob, "length") == 0,
              DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * length() - When the lob is not empty.
   * 
   * SQL400 - The length of a lob is expected to be the length of the signficant
   * data in the lob. The Native driver returns 20 here as that is the number of
   * significant characters in this lob. empty.
   **/
  public void Var139() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");

          assertCondition(
              JDReflectionUtil.callMethod_L(clob, "length") == DBCLOB_MEDIUM_
                  .length(),
              DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * length() - When the lob is full.
   * 
   * SQL400 - This test should expect the value to be width -2 as that is what
   * the setup created.
   **/
  public void Var140() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(3);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");

          assertCondition(
              JDReflectionUtil.callMethod_L(clob, "length") == DBCLOB_LARGE_
                  .length(),
              DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should throw an exception when the pattern is null.
   */
  public void Var141() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              (String) null, (long) 0);

          failed("Didn't throw SQLException " + v + DBCLOB_ADDED);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should throw an exception when start is less than
   * 0.
   */
  public void Var142() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position", "Test",
              (long) -1);

          failed("Didn't throw SQLException " + v + DBCLOB_ADDED);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should throw an exception when start is 0.
   **/
  public void Var143() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position", "Test",
              (long) 0);

          failed("Didn't throw SQLException " + v + DBCLOB_ADDED);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should throw an exception when start is greater
   * than the length of the lob.
   **/
  public void Var144() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position", "Test",
              (long) DBCLOB_MEDIUM_.length() + 1);
          failed("Didn't throw SQLException " + v + DBCLOB_ADDED);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should return -1 when the pattern is not found at
   * all.
   **/
  public void Var145() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position", "Test",
              (long) 1);
          assertCondition(v == -1,
              "v = " + v + " SB -1 Looking for \"Test\". Clob.getClass() is "
                  + clob.getClass() + " Clob is \"" + getClob(clob) + "\""
                  + DBCLOB_ADDED);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should return -1 when the pattern is not found
   * after the starting position, although it does appear before the starting
   * position.
   **/
  public void Var146() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String searchString = DBCLOB_MEDIUM_.substring(1, 4);
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              searchString, (long) 11);
          assertCondition(v == -1, "v = " + v + " SB -1 clog.getClass() = "
              + clob.getClass() + DBCLOB_ADDED);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should return 1 when the pattern is found at the
   * beginning of the lob.
   **/
  public void Var147() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String searchString = DBCLOB_MEDIUM_.substring(0, 7);
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              searchString, (long) 1);

          boolean condition = (v == 1);

          if (!condition) {
            System.out.println("clob.position('" + searchString
                + "',1) returned " + v + " for " + getClob(clob));
          }
          assertCondition(condition, DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should return the position when the pattern is
   * found in the middle of the lob, and start is 1.
   **/
  public void Var148() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          // if( clob instanceof com.ibm.db2.jdbc.app.DB2ClobLocator)
          // System.out.println("yes");
          String searchString = DBCLOB_MEDIUM_.substring(9, 12);
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              searchString, (long) 1);

          assertCondition(v == 10, "v = " + v + " SB 10 for " + getClob(clob)
              + DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should return the position when the pattern is
   * found in the middle of the lob, and start is before where the pattern
   * appears.
   **/
  public void Var149() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String searchString = DBCLOB_MEDIUM_.substring(9, 12);
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              searchString, (long) 7);

          assertCondition(v == 10, "v = " + v + " SB 10  for " + getClob(clob)
              + DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should return the position when the pattern is
   * found in the middle of the lob, and start is right where the pattern
   * appears.
   **/
  public void Var150() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String searchString = DBCLOB_MEDIUM_.substring(8, 12);
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              searchString, (long) 9);

          assertCondition(v == 9, "v = " + v + " SB 9  for " + getClob(clob)
              + DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should return the position when the pattern is
   * found at the end of the lob, and start is 1.
   **/
  public void Var151() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          int searchOffset = DBCLOB_MEDIUM_.length() - 5;
          String searchString = DBCLOB_MEDIUM_.substring(searchOffset);
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              searchString, (long) 1);

          assertCondition(v == searchOffset + 1, "v = " + v + " SB "
              + (searchOffset + 1) + " for " + getClob(clob) + DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should return the position when the pattern is
   * found at the end of the lob, and start is right where the pattern occurs.
   **/
  public void Var152() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    long v = 0;
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");

          int length = DBCLOB_MEDIUM_.length();
          String searchString = DBCLOB_MEDIUM_.substring(length - 1);

          v = JDReflectionUtil.callMethod_L(clob, "position", searchString,
              (long) length);

          assertCondition(v == length, "v = " + v + " SB " + length + "  for "
              + getClob(clob) + DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception: " + v + " sb 20");
        }
      }
    }
  }

  /**
   * position(String,long) - Should return 0 when the pattern is the empty
   * string and start is 1.
   **/
  public void Var153() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil
              .callMethod_L(clob, "position", "", (long) 1);

          assertCondition(v == 1, DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should return start when the pattern is the empty
   * string and start is in the middle.
   **/
  public void Var154() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil
              .callMethod_L(clob, "position", "", (long) 9);

          assertCondition(v == 9, DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(String,long) - Should return length-1 when the pattern is the
   * empty string and start is the length-1.
   **/
  public void Var155() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position", "",
              (long) DBCLOB_MEDIUM_.length() - 1);

          assertCondition(v == (DBCLOB_MEDIUM_.length() - 1), DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should throw an exception when the pattern is null.
   **/
  public void Var156() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          Class[] argClasses = new Class[2];
          argClasses[0] = Clob.class;
          argClasses[1] = Long.TYPE;
          Object[] args = new Object[2];
          args[0] = null;
          args[1] = new Long(1);
          long v = JDReflectionUtil.callMethod_L(clob, "position", argClasses,
              args);

          failed("Didn't throw SQLException " + v + DBCLOB_ADDED);

        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);

        }
      }
    }
  }

  /**
   * position(Clob,long) - Should throw an exception when start is less than 0.
   **/
  public void Var157() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob("Test"), (long) -1);

          failed("Didn't throw SQLException" + v + DBCLOB_ADDED);
        } catch (Exception e) {

          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should throw an exception when start is 0.
   **/
  public void Var158() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob("Test"), (long) 0);

          // if (getDriver () == JDTestDriver.DRIVER_NATIVE)
          failed("Didn't throw SQLException" + v + DBCLOB_ADDED);
          // else
          // assertCondition (v == -1);
        } catch (Exception e) {
          // if (getDriver () == JDTestDriver.DRIVER_NATIVE)
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
          // else
          // failed (connection_, e, "Unexpected Exception"+DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should throw an exception when start is greater than
   * the length of the lob.
   **/
  public void Var159() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob("Test"), (long) MEDIUM_.length() + 1);

          // if (getDriver () == JDTestDriver.DRIVER_NATIVE)
          failed("Didn't throw SQLException" + v + DBCLOB_ADDED);
          // else
          // assertCondition (v == -1);
        } catch (Exception e) {
          // if (getDriver () == JDTestDriver.DRIVER_NATIVE)
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
          // else
          // failed (connection_, e, "Unexpected Exception"+DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return -1 when the pattern is not found at
   * all.
   **/
  public void Var160() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob("This is only a test"), (long) 1);
          assertCondition(v == -1, "v = " + v + " SB -1" + DBCLOB_ADDED);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return -1 when the pattern is not found after
   * the starting position, although it does appear before the starting
   * position.
   **/
  public void Var161() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String searchString = DBCLOB_MEDIUM_.substring(3, 7);
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(searchString), (long) 12);
          assertCondition(v == -1, "v = " + v + " SB -1" + DBCLOB_ADDED);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return 1 when the pattern is found at the
   * beginning of the lob. Lob indexes are 1 based.
   **/
  public void Var162() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          String searchString = DBCLOB_MEDIUM_.substring(0, 12);
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(searchString), (long) 1);

          assertCondition(v == 1, "found at " + v + " sb 1  seaching for '"
              + searchString + "' in '" + getClob(clob) + "'" + DBCLOB_ADDED);
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found
   * in the middle of the lob, and start is 1.
   **/
  public void Var163() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          int length = DBCLOB_MEDIUM_.length();
          String searchString = DBCLOB_MEDIUM_.substring(length / 2,
              length / 2 + 3);
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(searchString), (long) 1);

          // if (getDriver () == JDTestDriver.DRIVER_NATIVE)
          assertCondition(v == (length / 2 + 1), "v = " + v + " SB "
              + (length / 2 + 1) + " searchstring is '" + searchString
              + "' DBCLOB_MEDIUM_ is '" + DBCLOB_MEDIUM_ + "'" + DBCLOB_ADDED);
          // else
          // assertCondition (v == -1);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found
   * in the middle of the lob, and start is before where the pattern appears.
   **/
  public void Var164() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");

          int length = DBCLOB_MEDIUM_.length();
          String searchString = DBCLOB_MEDIUM_.substring((length / 2),
              (length / 2) + 3);

          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(searchString), (long) length / 2 - 3);

          // if (getDriver () == JDTestDriver.DRIVER_NATIVE)
          assertCondition(v == (length / 2) + 1, "v = " + v + " SB "
              + ((length / 2) + 1) + " searchString = '" + searchString
              + "' DBCLOB_MEDIUM='" + DBCLOB_MEDIUM_ + "'" + DBCLOB_ADDED);
          // else
          // assertCondition (v == -1);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found
   * in the middle of the lob, and start is right where the pattern appears.
   **/
  public void Var165() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          int length = DBCLOB_MEDIUM_.length();
          String searchString = DBCLOB_MEDIUM_.substring((length / 2),
              (length / 2) + 3);

          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(searchString), (long) (length / 2) + 1);

          // if (getDriver () == JDTestDriver.DRIVER_NATIVE)
          assertCondition(v == (length / 2 + 1), "got " + v + " sb "
              + (length / 2 + 1) + "  for finding '" + searchString + "' in "
              + getClob(clob) + DBCLOB_ADDED);
          // else
          // assertCondition (v == -1);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found
   * at the end of the lob, and start is 1.
   **/
  public void Var166() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          int length = DBCLOB_MEDIUM_.length();
          String searchString = DBCLOB_MEDIUM_.substring(length - 1);

          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(searchString), (long) 1);

          assertCondition(v == length, "v is " + v + " sb " + (length - 1)
              + DBCLOB_ADDED);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return the position when the pattern is found
   * at the end of the lob, and start is right where the pattern occurs.
   **/
  public void Var167() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          int length = DBCLOB_MEDIUM_.length();
          String searchString = DBCLOB_MEDIUM_.substring(length - 4);

          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(searchString), (long) (length - 3));

          // if (getDriver () == JDTestDriver.DRIVER_NATIVE)
          assertCondition(v == (length - 3), "v = " + v + " SB " + (length - 3)
              + DBCLOB_ADDED);
          // else
          // assertCondition (v == -1);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return 0 when the pattern is the empty lob and
   * start is 1.
   **/
  public void Var168() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(""), (long) 1);

          // if (getDriver () == JDTestDriver.DRIVER_NATIVE)
          assertCondition(v == 1, DBCLOB_ADDED);
          // else
          // assertCondition (v == -1);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return start when the pattern is the empty lob
   * and start is in the middle.
   **/
  public void Var169() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(""), (long) 9);

          // if (getDriver () == JDTestDriver.DRIVER_NATIVE)
          assertCondition(v == 9, DBCLOB_ADDED);
          // else
          // assertCondition (v == -1);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * position(Clob,long) - Should return length-1 when the pattern is the empty
   * lob and start is the length-1.
   **/
  public void Var170() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs3_.absolute(2);
          Object clob = JDReflectionUtil.callMethod_O(rs3_, getMethod,
              "C_DBCLOB");
          long v = JDReflectionUtil.callMethod_L(clob, "position",
              new JDLobTest.JDTestClob(""), (long) DBCLOB_MEDIUM_.length() - 1);

          // if (getDriver () == JDTestDriver.DRIVER_NATIVE)
          assertCondition(v == DBCLOB_MEDIUM_.length() - 1, "Got v=" + v
              + " sb " + (DBCLOB_MEDIUM_.length() - 1) + DBCLOB_ADDED);
          // else
          // assertCondition (v == -1);

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  public void Var171() {
    notApplicable();
  }

  public void Var172() {
    notApplicable();
  }

  public void Var173() {
    notApplicable();
  }

  /**
   * setString(long, String) - Should throw an exception when start is less than
   * 0.
   **/
  public void Var174() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs3_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs3_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_I(clob, "setString", (long) -1,
            "Really small objects");
        failed("Didn't throw SQLException" + DBCLOB_ADDED);
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * setString(long, String) - Should throw an exception when start is 0.
   **/
  public void Var175() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs3_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs3_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_I(clob, "setString", (long) 0,
            "Really small object.");
        failed("Didn't throw SQLException" + DBCLOB_ADDED);
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * setString(long, String) - Should throw an exception when start is greater
   * than the length of the lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var176() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");

        JDReflectionUtil.callMethod_I(clob, "setString",
            (long) DBCLOB_MEDIUM_.length() + 1, DBCLOB_MEDIUM_SET_);
        // failed ("Didn't throw SQLException"+DBCLOB_ADDED);
        succeeded();
      } catch (Exception e) {
        /*
         * try{ assertExceptionIsInstanceOf (e, "java.sql.SQLException",
         * DBCLOB_ADDED); rs4_.close(); } catch (Exception s) {
         */
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        // }
      }
    }
  }

  // @C2C Changed to not expect an exception
  /**
   * setString(long, String) - Changed to not expect an exception when length of
   * string to insert is greater than length of lob.
   **/
  public void Var177() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE3_;
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(4);
        String expected = DBCLOB_MEDIUM_SET_;
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, expected);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        String retrievedClob = clob1.getSubString(1, expected.length());
        assertCondition(
            retrievedClob.equals(expected) && written == expected.length(),
            "got " + retrievedClob + " sb " + expected + " written=" + written
                + " " + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String) - Should throw an exception on an empty lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var178() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(1);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_I(clob, "setString", (long) 1,
            "Really small objects");
        // failed ("Didn't throw SQLException"+DBCLOB_ADDED);
        succeeded();
      } catch (Exception e) {
        // try{
        // assertExceptionIsInstanceOf (e, "java.sql.SQLException",
        // DBCLOB_ADDED);
        // rs4_.close();
        // }
        // catch (Exception s) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        // }
      }
    }
  }

  /**
   * setString(long, String) - Should work to set all of a non-empty lob.
   **/
  public void Var179() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        String expected = DBCLOB_MEDIUM_SET_;
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, expected);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        String output = clob1.getSubString(1, DBCLOB_MEDIUM_SET_.length());
        assertCondition(output.equals(expected)
            && written == DBCLOB_MEDIUM_SET_.length(), "got '" + output
            + "' sb '" + expected + "' written=" + written + " sb "
            + DBCLOB_MEDIUM_SET_.length() + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String) - Should work to set the first part of a non-empty
   * lob.
   **/
  public void Var180() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        String expected = DBCLOB_MEDIUM_SET_.substring(0, 5);
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, expected);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        assertCondition(clob1.getSubString(1, 5).equals(expected)
            && written == expected.length(), DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String) - Should work to set the middle part of a non-empty
   * lob.
   **/
  public void Var181() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, ASSIGNMENT_DBCHAR4_);
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K3
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length(); // @K3
        assertCondition(clob1.getSubString(5, 4).equals(ASSIGNMENT_DBCHAR4_)
            && written == 4 && length1 == length2, DBCLOB_ADDED); // @K3 : added
                                                                  // length1==length2
                                                                  // as we do
                                                                  // not expect
                                                                  // any
                                                                  // truncations
                                                                  // !
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String) - Should fail since var. 61 truncated lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception. Also, variations should not
   *      be dependent upon each other, since a testcase can execute an
   *      arbitrary number of its variations.
   **/
  public void Var182() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 10, ASSIGNMENT_CHAR5_);

        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        assertCondition(true, "written=" + written);

      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);

      }
    }
  }

  /**
   * setString(long, String) - Should work to set the last part of a non-empty
   * lob.
   **/
  public void Var183() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 8, ASSIGNMENT_DBCHAR7_);
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K4
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length(); // @K4
        String substring = clob1.getSubString(8, 7);
        assertCondition(substring.equals(ASSIGNMENT_DBCHAR7_) && written == 7
            && length1 == length2, "!substring(" + substring + ").equals('"
            + ASSIGNMENT_DBCHAR7_ + "') or written(" + written
            + ")!=7 or length1(" + length1 + ")!= length2(" + length2 + ")"
            + DBCLOB_ADDED); // @K4
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when start is
   * less than 0.
   **/
  public void Var184() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_I(clob, "setString", (long) -1,
            "Really green objects", 0, 20); /* @C5 */
        failed("Didn't throw SQLException" + DBCLOB_ADDED);
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
          rs4_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when start is
   * 0.
   **/
  public void Var185() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_I(clob, "setString", (long) 0,
            "Really shiny object.", 0, 20); /* @C5 */
        failed("Didn't throw SQLException" + DBCLOB_ADDED);
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
          rs4_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when start is
   * greater than the length of the lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var186() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_I(clob, "setString",
            (long) MEDIUM_.length() + 1, "Really shiny objects", 0, 20); /* @C5 */
        succeeded();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  // @C2C Changed to not expect exception
  /**
   * setString(long, String, int, int) - Should not throw an exception when
   * length of string to insert is greater than length of lob.
   **/
  public void Var187() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        String expected = DBCLOB_MEDIUM_SET_;
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, expected, 0, expected.length());/* @C5 */
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        assertCondition(
            clob1.getSubString(1, expected.length()).equals(expected) // @C3C
                && written == expected.length(), DBCLOB_ADDED);
      } catch (Exception e) {
        try {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
          rs4_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception on an empty
   * lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception. And if you think about it,
   *      this variation doesn't make sense... if setting data into an empty LOB
   *      is supposed to throw an exception, how will I ever be able to get a
   *      LOB that has data in it?
   **/
  public void Var188() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(1);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_I(clob, "setString", (long) 1,
            "Really small objects", 0, 20); /* @C5 */
        // failed ("Didn't throw SQLException"+DBCLOB_ADDED);
        succeeded();
      } catch (Exception e) {
        // try{
        // assertExceptionIsInstanceOf (e, "java.sql.SQLException",
        // DBCLOB_ADDED);
        // rs4_.close();
        // }
        // catch (Exception s) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        // }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should work to set all of a non-empty
   * lob.
   **/
  public void Var189() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        String expected = DBCLOB_MEDIUM_SET_;
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, expected, 0, expected.length()); /* @C5 */
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        assertCondition(
            clob1.getSubString(1, expected.length()).equals(expected)
                && written == expected.length(), DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should work to set the first part of a
   * non-empty lob.
   **/
  public void Var190() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, ASSIGNMENT_DBCHAR7_, 0, 7); /* @C5 */
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K4
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = JDReflectionUtil.callMethod_L(clob, "length"); // @K4
        String substring = clob1.getSubString(1, 7);
        assertCondition(substring.equals(ASSIGNMENT_DBCHAR7_) && written == 7
            && length1 == length2, "!substring(" + substring + ").equals('"
            + ASSIGNMENT_DBCHAR7_ + "') or written(" + written
            + ")!=8 or length1(" + length1 + ")!= length2(" + length2 + ")"
            + DBCLOB_ADDED); // @K4 : added length1 == length2
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should work to set the middle part of a
   * non-empty lob.
   **/
  public void Var191() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, ASSIGNMENT_DBCHAR4_, 0, 4); /* @C5 */
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K4
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length(); // @K4
        String substring = clob1.getSubString(5, 4);
        assertCondition(substring.equals(ASSIGNMENT_DBCHAR4_) && written == 4
            && length1 == length2, "!substring(" + substring
            + ").equals('cool') or written(" + written + ")!=4 or length1("
            + length1 + ")!= length2(" + length2 + ")" + DBCLOB_ADDED); // @K4
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String) - Should fail since var. 71 truncated lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var192() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 10, ASSIGNMENT_CHAR7_);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        assertCondition(true, "written=" + written);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should work to set the last part of a
   * non-empty lob.
   **/
  public void Var193() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 8, ASSIGNMENT_DBCHAR7_, 0, 7); /* @C5 */
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K3
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length(); // @K3
        String substring = clob1.getSubString(8, 7);
        assertCondition(substring.equals(ASSIGNMENT_DBCHAR7_) && written == 7
            && length1 == length2, "!substring(" + substring + ").equals('"
            + ASSIGNMENT_DBCHAR7_ + "') or written(" + written
            + ")!=7 or length1(" + length1 + ")!= length2(" + length2 + ")"
            + DBCLOB_ADDED); // @K3
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * truncate() - Should throw an exception if you try to truncate to a length
   * less than 0.
   **/
  public void Var194() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_V(clob, "truncate", (long) -1);
        failed("Didn't throw SQLException" + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
          rs4_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * truncate() - Should throw an exception if you try to truncate to a length
   * greater than the length of the lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var195() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_V(clob, "truncate", (long) 25);
        rs4_.close();
        succeeded();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * truncate() - Should work on a non-empty lob.
   **/
  public void Var196() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE3_;
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_V(clob, "truncate", (long) 2);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        assertCondition(clob1.length() == 2, DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setAsciiStream() - When the lob is not empty starting at position 1.
   **/
  public void Var197() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (skipAsciiTests) {
      notApplicable("getAsciiStreamTest");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE3_;
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        OutputStream w = (OutputStream) JDReflectionUtil.callMethod_O(clob,
            "setAsciiStream", (long) 1);
        byte[] b = new byte[] { (byte) 65, (byte) 66, (byte) 67 };
        w.write(b);
        w.close();
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        InputStream i = clob1.getAsciiStream();
        sb.setLength(0);
        boolean passed = compare(i, b, sb);
        assertCondition(passed, sb.toString() + DBCLOB_ADDED); // @C4C
        rs4_.close();
        Var197Called_ = true;
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setAsciiStream() - When the lob is not empty starting at a position other
   * than 1.
   **/
  public void Var198() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (skipAsciiTests) {
      notApplicable("getAsciiStreamTest");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE3_;
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        OutputStream w = (OutputStream) JDReflectionUtil.callMethod_O(clob,
            "setAsciiStream", (long) 2);
        byte[] b = new byte[] { (byte) 32, (byte) 33, (byte) 34 };
        w.write(b);
        w.close();
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        InputStream i = clob1.getAsciiStream();
        // @C4A Since we added bytes to position 2 here, we must read back 4
        // bytes
        // @C4A and expect a (byte) 1 in the first position since that's what
        // @C4A variation 77 put there.
        // @C4Dint read = i.read(b, 0, 3);
        byte[] newBytes = null;
        if (Var197Called_) {
          newBytes = new byte[] { (byte) 65, (byte) 32, (byte) 33, (byte) 34 }; // @C4A
        } else {
          newBytes = b;
        }

        sb.setLength(0);
        boolean passed = compare(i, newBytes, sb);

        assertCondition(passed, sb.toString() + DBCLOB_ADDED); // @C4C
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setAsciiStream() - Should throw an exception if you try to set a stream
   * that starts after the lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var199() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        OutputStream w = (OutputStream) JDReflectionUtil.callMethod_O(clob,
            "setAsciiStream", (long) 24);
        rs4_.close();
        assertCondition(true, "w=" + w);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setAsciiStream() - Should throw an exception if you try to set a stream
   * that starts at 0.
   **/
  public void Var200() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        OutputStream w = (OutputStream) JDReflectionUtil.callMethod_O(clob,
            "setAsciiStream", (long) 0);
        failed("Didn't throw SQLException" + w + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
          rs4_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * setAsciiStream() - Should throw an exception if you try to set a stream
   * that starts before 0.
   **/
  public void Var201() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        OutputStream w = (OutputStream) JDReflectionUtil.callMethod_O(clob,
            "setAsciiStream", (long) -1);
        failed("Didn't throw SQLException" + w + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
          rs4_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   **/
  public void Var202() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE3_;
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        String cbuf = ASSIGNMENT_DBCHAR20_;
        w.write(cbuf);
        w.close();
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        assertCondition(compare(r, ASSIGNMENT_DBCHAR20_, sb), DBCLOB_ADDED + sb);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at a position
   * other than 1.
   **/
  public void Var203() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE3_;
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 2);
        String cbuf = ASSIGNMENT_DBCHAR20_;
        String firstChar = ASSIGNMENT_DBCHAR20_.substring(0, 1);
        w.write(cbuf);
        w.close();
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        Reader r = clob1.getCharacterStream();
        // Since we started at position 2 this time, must expect an extra letter
        // 'H' in the first position from testcase 82.
        sb.setLength(0);
        boolean passed = compare(r, firstChar + ASSIGNMENT_DBCHAR20_, sb);
        assertCondition(passed, DBCLOB_ADDED + sb); // @C4C
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setCharacterStream() - Should throw an exception if you try to set a stream
   * that starts after the lob.
   * 
   * @CRS - This variation should succeed. Only when you try to write past the
   *      max column size will it be an exception.
   **/
  public void Var204() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 24);
        rs4_.close();
        assertCondition(true, "w=" + w);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setCharacterStream() - Should throw an exception if you try to set a stream
   * that starts at 0.
   **/
  public void Var205() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 0);
        failed("Didn't throw SQLException" + w + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
          rs4_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * setCharacterStream() - Should throw an exception if you try to set a stream
   * that starts before 0.
   **/
  public void Var206() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) -1);
        failed("Didn't throw SQLException" + w + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
          rs4_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * setString(long, String) - Should update clob with an unchanged clob.
   **/
  public void Var207() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(2);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(2);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        String v = clob1.getSubString(1, DBCLOB_MEDIUM_.length());
        assertCondition(v.equals(DBCLOB_MEDIUM_), DBCLOB_ADDED);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * updateClob(String, clob) - Should not change lob if updateRow() is not
   * called.
   **/
  public void Var208() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(2);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(2);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        String v = clob1.getSubString(1, DBCLOB_MEDIUM_.length());
        assertCondition(v.equals(DBCLOB_MEDIUM_), DBCLOB_ADDED);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String) - Make sure lob in database is not changed if
   * updateRow() was not called.
   **/
  public void Var209() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(2);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 2, "New updates");
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(2);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        String v = clob1.getSubString(1, DBCLOB_MEDIUM_.length());
        assertCondition(v.equals(DBCLOB_MEDIUM_), "" + " written=" + written
            + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String) - Should work to set all of a non-empty lob with
   * multiple updates to the same position.
   **/
  public void Var210() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K4
        String expected = new String(ASSIGNMENT_DBCHAR7_);
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, expected);
        String afterString = ASSIGNMENT_DBCHAR7_.substring(4);
        String expected2 = new String(ASSIGNMENT_DBCHAR4_);
        int written2 = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, expected2);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length(); // @K4
        assertCondition(
            clob1.getSubString(5, expected2.length()).equals(expected2)
                && written == expected.length()
                && written2 == expected2.length()
                && clob1.getSubString(5 + expected2.length(), 3).equals(
                    afterString) // @K4
                && length1 == length2, DBCLOB_ADDED); // @K4
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String) - Should work to set strings to a non-empty lob
   * with multiple updates to the different positions.
   **/
  public void Var211() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length"); // @K4
        String expected = ASSIGNMENT_DBCHAR20_.substring(0, 5);
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, expected);
        String expected2 = ASSIGNMENT_DBCHAR20_.substring(10, 13);
        int written2 = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 9, expected2);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length(); // @K4
        String finalExpected = expected.substring(0, 4) + expected2;
        assertCondition(
            clob1.getSubString(5, expected.length() + expected2.length() - 1)
                .equals(finalExpected) // @C3C
                && written == expected.length()
                && written2 == expected2.length() && length1 == length2,
            DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String) - Should work to set strings to a non-empty lob
   * with multiple updates to the same position with updateClob called between
   * updates.
   **/
  public void Var212() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        String expected = ASSIGNMENT_DBCHAR20_.substring(0, 8);
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, expected);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        String expected2 = ASSIGNMENT_DBCHAR20_.substring(10, 15);
        int written2 = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, expected2);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        assertCondition(
            clob1.getSubString(5, expected2.length()).equals(expected2)
                && written == expected.length()
                && written2 == expected2.length(), DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String) - Should work to set strings to a non-empty lob
   * with multiple updates to the different positions with updateClob called
   * between updates.
   **/
  public void Var213() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {

        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        String expected = ASSIGNMENT_DBCHAR20_.substring(0, 5);
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, expected);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        String expected2 = ASSIGNMENT_DBCHAR20_.substring(10, 13);
        int written2 = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 9, expected2);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        String finalExpected = expected.substring(0, 4) + expected2;
        assertCondition(
            clob1.getSubString(5, expected.length() + expected2.length() - 1)
                .equals(finalExpected)
                && written == expected.length()
                && written2 == expected2.length(), DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when string
   * offset is less than 0.
   **/
  public void Var214() // @C5
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_I(clob, "setString",
            (long) MEDIUM_.length() + 1, "Really shiny objects", -1, 20); // @C4
        failed("Didn't throw SQLException" + DBCLOB_ADDED);
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
          rs4_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when the
   * string is null
   **/
  public void Var215() // @C5
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_I(clob, "setString",
            (long) MEDIUM_.length() + 1, null, 0, 20); // @C4
        failed("Didn't throw SQLException" + DBCLOB_ADDED);
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
          rs4_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  /**
   * setString(long, String, int, int) - Should throw an exception when string
   * offset is greater than string length.
   **/
  public void Var216() // @C5
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_I(clob, "setString",
            (long) MEDIUM_.length() + 1, "Really shiny objects",
            "Really shiny objects".length() + 1, 20); // @C4
        failed("Didn't throw SQLException" + DBCLOB_ADDED);
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", DBCLOB_ADDED);
          rs4_.close();
        } catch (Exception s) {
          failed(s, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    }
  }

  public void Var217() {
    notApplicable();
  }

  public void Var218() {
    notApplicable();
  }

  public void Var219() {
    notApplicable();
  }

  /**
   * setString(long, String) - Should work to set the first part of a non-empty
   * lob.
   **/
  public void Var220() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, "Really small");
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation"
                + DBCLOB_ADDED);
      }
    }
  }

  // @k1A
  /**
   * setString(long, String) - Should work to set the middle part of a non-empty
   * lob.
   **/
  public void Var221() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, ASSIGNMENT_DBCHAR4_);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation"
                + DBCLOB_ADDED);
      }
    }
  }

  // @K1A
  /**
   * setString(long, String) - Should work to set the last part of a non-empty
   * lob.
   **/
  public void Var222() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 14, ASSIGNMENT_CHAR7_);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation"
                + DBCLOB_ADDED);
      }
    }
  }

  // @k1A
  /**
   * setString(long, String, int, int) - Should work to set all of a non-empty
   * lob.
   **/
  public void Var223() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, "Really tiny objects.", 0, 20);// @C4
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation"
                + DBCLOB_ADDED);
      }
    }
  }

  // @k1A
  /**
   * setString(long, String, int, int) - Should work to set the first part of a
   * non-empty lob.
   **/
  public void Var224() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 1, "Really small", 0, 12); // @C4
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation"
                + DBCLOB_ADDED);
      }
    }
  }

  // @k1A
  /**
   * setString(long, String, int, int) - Should work to set the middle part of a
   * non-empty lob.
   **/
  public void Var225() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, ASSIGNMENT_DBCHAR4_, 0, 4);// @C4
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation"
                + DBCLOB_ADDED);
      }
    }
  }

  // @K1A
  /**
   * setString(long, String, int, int) - Should work to set the last part of a
   * non-empty lob.
   **/
  public void Var226() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        int written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 14, ASSIGNMENT_CHAR7_, 0, 7); // @C4
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE3_);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length();
        assertCondition(length1 == length2, "l1 = " + length1 + " sb l2 = "
            + length2 + " written=" + written + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception: - Added by toolbox to test host server truncation"
                + DBCLOB_ADDED);
      }
    }
  }

  public void Var227() {
    notApplicable();
  }

  /**
   * truncate() - 0
   **/
  public void Var228() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE5_); // @K5C
                                                                     // changed
                                                                     // so
                                                                     // variations
                                                                     // aren't
                                                                     // dependent
                                                                     // on each
                                                                     // other
        rs4_.absolute(1); // @K5C
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_V(clob, "truncate", (long) 0);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE5_); // @K5C
        rs4_.absolute(1); // @K5C
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        rs4_.close();
        assertCondition(clob1.length() == 0, DBCLOB_ADDED);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * truncate() - 1
   **/
  public void Var229() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE5_); // @K5C
        rs4_.absolute(2); // @K5C
        Object clob_ = JDReflectionUtil.callMethod_O(rs4_, getMethod,
            "C_DBCLOB");
        JDReflectionUtil.callMethod_I(clob_, "setString", 1, "ABCD");
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob_);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE5_); // @K5C
        rs4_.absolute(2); // @K5C
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_V(clob, "truncate", (long) 1);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery("SELECT * FROM " + TABLE5_); // @K5C
        rs4_.absolute(2); // @K5C
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        rs4_.close();
        assertCondition(clob1.length() == 1, DBCLOB_ADDED);
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write an int
   **/
  public void Var230() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE5_; // @K5C
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(3); // @K5C
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        w.write(ASSIGNMENT_DBCHAR1_.charAt(0));
        w.close();
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(3); // @K5C
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        String expected = ASSIGNMENT_DBCHAR1_.charAt(0)
            + DBCLOB_SMALL_.substring(1);

        assertCondition(compare(r, expected, sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB '" + expected
                + "'" + DBCLOB_ADDED); // @K5C
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write an array of characters
   **/
  public void Var231() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE5_; // @K5C
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        char c[] = ASSIGNMENT_DBCHAR9_.toCharArray();
        w.write(c);
        w.close();
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        String expected = ASSIGNMENT_DBCHAR9_ + DBCLOB_SMALL_.substring(9);
        assertCondition(compare(r, expected, sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB '" + expected
                + "'" + DBCLOB_ADDED); // @K5A
        // @K5D assertCondition(compare (r, "IT WORKS!"), clob1.getSubString(1,
        // (int)clob1.length())+" SB \"IT WORKS!\"");
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write an ARRAY OF CHARACTERS with an offset specified as 0
   **/
  public void Var232() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE5_; // @K5C
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(5); // @K5C
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        char c[] = ASSIGNMENT_DBCHAR7_.toCharArray();
        w.write(c, 0, c.length);
        w.close();
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(5); // @K5C
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        String expected = ASSIGNMENT_DBCHAR7_ + DBCLOB_SMALL_.substring(7);
        assertCondition(compare(r, expected, sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB \"" + expected
                + "\"" + DBCLOB_ADDED); // @K5A
        // @K5D assertCondition(compare (r, "HURRAY!S!"), clob1.getSubString(1,
        // (int)clob1.length())+" SB \"HURRAY!S!\"");
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write an ARRAY OF CHARACTERS with an offset specified not as
   * 0
   **/
  public void Var233() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE5_; // @K5C
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(6); // @K5C
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        char c[] = ASSIGNMENT_DBCHAR7_.toCharArray();
        w.write(c, 2, c.length - 2);
        w.close();
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(6); // @K5C
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        String expected = ASSIGNMENT_DBCHAR7_.substring(2)
            + DBCLOB_SMALL_.substring(5);
        assertCondition(compare(r, expected, sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB '" + expected
                + "'" + DBCLOB_ADDED); // @K5A
        // @K5D assertCondition(compare (r, "RRAY!Y!S!"), clob1.getSubString(1,
        // (int)clob1.length())+" SB \"RRAY!Y!S!\"");
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write an ARRAY OF CHARACTERS with an offset specified LESS
   * than 0
   **/
  public void Var234() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE5_; // @K5C
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(7); // @K5C
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        char c[] = ASSIGNMENT_DBCHAR7_.toCharArray();
        w.write(c, -1, c.length);
        w.close();
        rs4_.close();
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) // @K5A
          failed("Didn't throw an ExtendedIllegalArgumentException"
              + DBCLOB_ADDED); // @K5A
        else
          // @K5A
          failed("Didn't throw an INDEXOUTOFCOUNDSEXCEPTION!!" + DBCLOB_ADDED);
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) // @K5A
          assertExceptionIsInstanceOf(e,
              "com.ibm.as400.access.ExtendedIllegalArgumentException",
              DBCLOB_ADDED); // @K5A
        else
          // @K5A
          assertExceptionIsInstanceOf(e, "java.lang.IndexOutOfBoundsException",
              DBCLOB_ADDED);
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write a string with offset specified other than 0
   **/
  public void Var235() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE5_; // @K5C
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(8); // @K5C
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        String str = ASSIGNMENT_DBCHAR17_;
        w.write(str, 5, str.length() - 5);
        w.close();
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(8); // @K5C
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        String expected = str.substring(5) + DBCLOB_SMALL_.substring(12);
        assertCondition(compare(r, expected, sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB '" + expected
                + "'" + DBCLOB_ADDED); // @K5A
        // @K5D assertCondition(compare (r, "WRITER TEST!"),
        // clob1.getSubString(1, (int)clob1.length())+" SB \"WRITER TEST!\"");
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * setCharacterStream() - When the lob is not empty starting at position 1.
   * and trying to write a string with offset specified other than 0
   **/
  public void Var236() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE5_; // @K5C
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(9); // @K5C
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        Writer w = (Writer) JDReflectionUtil.callMethod_O(clob,
            "setCharacterStream", (long) 1);
        String str = ASSIGNMENT_DBCHAR20_;
        w.write(str, 1, str.length() - 2);
        w.close();
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(9); // @K5C
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        Reader r = clob1.getCharacterStream();
        sb.setLength(0);
        String expected = str.substring(1, str.length() - 1)
            + DBCLOB_SMALL_.substring(18);
        assertCondition(compare(r, expected, sb),
            clob1.getSubString(1, (int) clob1.length()) + " SB '" + expected
                + "'" + DBCLOB_ADDED); // @K5A
        // @K5D assertCondition(compare (r, "LOB WRITER TESTING"),
        // clob1.getSubString(1,
        // (int)clob1.length())+" SB \"LOB WRITER TESTING\"");
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  public void Var237() {
    notApplicable();
  }

  /**
   * setString(long, int) - Is data (to be set) truncated if it exceeds the max.
   * length of the clob
   **/
  public void Var238() // @K6: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V5R3M0) {
      if (checkUpdateableLobsSupport()) {
        try {
          String statement = "SELECT * FROM " + TABLE3_;
          rs4_ = statement2_.executeQuery(statement);
          rs4_.absolute(4);
          String str = "m";
          Object clob = JDReflectionUtil.callMethod_O(rs4_, getMethod,
              "C_DBCLOB");
          for (int i = 1; i < 32760; i *= 2)
            str += str;

          long written = JDReflectionUtil.callMethod_I(clob, "setString",
              (long) 1, str);

          long length1 = JDReflectionUtil.callMethod_L(clob, "length");
          JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
          rs4_.updateRow();
          rs4_.close();
          rs4_ = statement2_.executeQuery(statement);
          rs4_.absolute(4);
          Clob clob1 = rs4_.getClob("C_DBCLOB");
          long length2 = clob1.length();
          assertCondition(written == 30000 && length1 == length2, "written = "
              + written + " SB 30000" + " length1 = " + length1
              + " SB same as length2 = " + length2 + DBCLOB_ADDED);

          rs4_.close();

        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
        }
      }
    } else {
      notApplicable("Native driver variation from v5r3m0 onwards!");
    }
  }

  /**
   * setString(long, int) = will prePadding be done for incoming Data?
   **/
  public void Var239() // @K4: added this var
  {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkUpdateableLobsSupport()) {
      try {
        String statement = "SELECT * FROM " + TABLE3_;
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(4);
        Object clob = JDReflectionUtil
            .callMethod_O(rs4_, getMethod, "C_DBCLOB");
        JDReflectionUtil.callMethod_V(clob, "truncate", (long) 2);
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();

        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(4);
        clob = JDReflectionUtil.callMethod_O(rs4_, getMethod, "C_DBCLOB");
        long written = JDReflectionUtil.callMethod_I(clob, "setString",
            (long) 5, "Minnesota");
        long length1 = JDReflectionUtil.callMethod_L(clob, "length");
        JDReflectionUtil.callMethod_V(rs4_, updateMethod, "C_DBCLOB", clob);
        rs4_.updateRow();
        rs4_.close();
        rs4_ = statement2_.executeQuery(statement);
        rs4_.absolute(4);
        Clob clob1 = rs4_.getClob("C_DBCLOB");
        long length2 = clob1.length();
        assertCondition(written == 9 && length1 == length2, "written = "
            + written + " SB 9" + " length1 = " + length1
            + " SB same as length2 = " + length2 + DBCLOB_ADDED);
        rs4_.close();
      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception" + DBCLOB_ADDED);
      }
    }
  }

  /**
   * free() Test select then free make sure locator is not accessible
   **/
  public void Var240() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    String lastSql = "";
    if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
      notApplicable("v5r5 variation");
      return;
    }
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
        && getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("native variation for now");
      return;
    }

    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {

          connection_.setAutoCommit(false);
          lastSql = "SELECT * FROM " + TABLE6_;
          rs6_ = statement2_.executeQuery(lastSql);
          rs6_.next();
          Object[] clob = new Object[FREE_LOCATOR_BLOCK_SIZE];
          int[] locator = new int[FREE_LOCATOR_BLOCK_SIZE];
          String[] answer1 = new String[FREE_LOCATOR_BLOCK_SIZE];
          String[] answer2 = new String[FREE_LOCATOR_BLOCK_SIZE];

          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            clob[i] = JDReflectionUtil.callMethod_O(rs6_, getMethod, "C_CLOB");
            locator[i] = JDReflectionUtil.callMethod_I(clob[i], "getLocator");
            rs6_.next();
          }
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            // Check the locator using DB locator value (not using the CLOB
            // object)
            lastSql = "select " + JDLobTest.COLLECTION + ".CLOBLOCINF("
                + locator[i] + ")" + " from qsys2.qsqptabl";
            ResultSet rs = statement2_.executeQuery(lastSql);
            rs.next();
            answer1[i] = rs.getString(1);
            rs.close();
          }

          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            JDReflectionUtil.callMethod_V(clob[i], "free");
          }

          // Check the locator using DB locator value (not using the CLOB
          // object)
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            lastSql = "select " + JDLobTest.COLLECTION + ".CLOBLOCINF("
                + locator[i] + ")" + " from qsys2.qsqptabl";
            ResultSet rs = statement2_.executeQuery(lastSql);
            rs.next();
            answer2[i] = rs.getString(1);
            rs.close();
          }
          assertCondition(
              (answer1[0].indexOf("ERROR") < 0)
                  && (answer2[0].indexOf("ERROR") >= 0)
                  && (answer1[FREE_LOCATOR_BLOCK_SIZE - 1].indexOf("ERROR") < 0)
                  && (answer2[FREE_LOCATOR_BLOCK_SIZE - 1].indexOf("ERROR") >= 0),
              "Locator should have been freed:  Check before said "
                  + answer1[0] + "Check after said " + answer2[0]);

        } catch (Exception e) {
          System.out.println(e);
          failed(connection_, e, "Unexpected Exception lastSql=" + lastSql);
        }
      }
    }
  }

  /**
   * free() Test insert and close then free
   **/
  public void Var241() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
      notApplicable("v5r5 variation");
      return;
    }
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
        && getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("native variation for now");
      return;
    }

    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {

          rs6_ = statement2_.executeQuery("SELECT * FROM " + TABLE6_);
          Object[] clob = new Object[FREE_LOCATOR_BLOCK_SIZE];
          rs6_.next();

          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            clob[i] = JDReflectionUtil.callMethod_O(rs6_, getMethod, "C_CLOB");
            rs6_.next();
          }
          connection_.setAutoCommit(false);
          PreparedStatement ps6 = connection_.prepareStatement("INSERT INTO "
              + TABLE6_ + " (C_CLOB) VALUES (?)"); // @pda
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            JDReflectionUtil.callMethod_V(ps6, setMethod, 1, clob[i]);
            ps6.executeUpdate();
          }
          ps6.close();
          // Save the locators before calling free
          int[] locator = new int[FREE_LOCATOR_BLOCK_SIZE];
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            locator[i] = JDReflectionUtil.callMethod_I(clob[i], "getLocator");

            JDReflectionUtil.callMethod_V(clob[i], "free");
          }

          String[] answer2 = new String[FREE_LOCATOR_BLOCK_SIZE];
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            // Check the locator using DB locator value (not using the CLOB
            // object)

            ResultSet rs = statement2_.executeQuery("select "
                + JDLobTest.COLLECTION + ".CLOBLOCINF(" + locator[i] + ")"
                + " from qsys2.qsqptabl");
            rs.next();
            answer2[i] = rs.getString(1);
            rs.close();
          }
          assertCondition((answer2[0].indexOf("ERROR") >= 0)
              && (answer2[FREE_LOCATOR_BLOCK_SIZE - 1].indexOf("ERROR") >= 0),
              "Locator should have been freed: Check after said " + answer2[0]);

        }

        catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * free() Test insert without close
   **/
  public void Var242() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
      notApplicable("v5r5 variation");
      return;
    }
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
        && getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("native variation for now");
      return;
    }

    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {

          rs6_ = statement2_.executeQuery("SELECT * FROM " + TABLE6_);
          Object[] clob = new Object[FREE_LOCATOR_BLOCK_SIZE];
          rs6_.next();
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            clob[i] = JDReflectionUtil.callMethod_O(rs6_, getMethod, "C_CLOB");
            rs6_.next();
          }
          connection_.setAutoCommit(false);
          PreparedStatement ps6 = connection_.prepareStatement("INSERT INTO "
              + TABLE6_ + " (C_CLOB) VALUES (?)"); // @pda
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            JDReflectionUtil.callMethod_V(clob[i], "setString", (long) 1, "a");
            JDReflectionUtil.callMethod_V(ps6, "setClob", 1, clob[i]);
            ps6.executeUpdate();
          }
          int[] locator = new int[FREE_LOCATOR_BLOCK_SIZE];
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            locator[i] = JDReflectionUtil.callMethod_I(clob[i], "getLocator");
            JDReflectionUtil.callMethod_V(clob[i], "free");
          }
          String[] answer2 = new String[FREE_LOCATOR_BLOCK_SIZE];
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {

            // Check the locator using DB locator value (not using the CLOB
            // object)

            ResultSet rs = statement2_.executeQuery("select "
                + JDLobTest.COLLECTION + ".CLOBLOCINF(" + locator[i] + ")"
                + " from qsys2.qsqptabl");
            rs.next();
            answer2[i] = rs.getString(1);
            rs.close();
          }
          connection_.commit();
          rs1_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);

          assertCondition((answer2[1].indexOf("ERROR") >= 0)
              && (answer2[FREE_LOCATOR_BLOCK_SIZE - 1].indexOf("ERROR") >= 0),
              "Locator should have been freed: Check after said " + answer2);

        }

        catch (Exception e) {
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * free() Test close then free with autocommit on. This is a case where lob is
   * already freed on host (toolbox ignores returncode back from host in this
   * case)
   **/
  public void Var243() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
      notApplicable("v5r5 variation");
      return;
    }
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
        && getRelease() <= JDTestDriver.RELEASE_V7R4M0) {
      notApplicable("native variation for now");
      return;
    }

    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          rs6_ = statement2_.executeQuery("SELECT * FROM " + TABLE6_);
          Object[] clob = new Object[FREE_LOCATOR_BLOCK_SIZE];
          rs6_.next();
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            clob[i] = JDReflectionUtil.callMethod_O(rs6_, getMethod, "C_CLOB");
          }
          rs6_.close();
          statement2_.close();

          // Open another statement to take its place
          statement2_ = connection_.createStatement(
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

          PreparedStatement ps6 = connection_.prepareStatement("INSERT INTO "
              + TABLE6_ + " (C_CLOB) VALUES (?)"); // @pda
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            JDReflectionUtil.callMethod_V(clob[i], "setString", (long) 1, "a");
            JDReflectionUtil.callMethod_V(ps6, setMethod, 1, clob[i]);
            ps6.executeUpdate();
          }
          ps6.close();
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {
            JDReflectionUtil.callMethod_V(clob[i], "free");
          }
          String[] answer2 = new String[FREE_LOCATOR_BLOCK_SIZE];
          for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) {

            // Check the locator using DB locator value (not using the CLOB
            // object)
            int locator = JDReflectionUtil.callMethod_I(clob[i], "getLocator");

            ResultSet rs = statement2_.executeQuery("select "
                + JDLobTest.COLLECTION + ".CLOBLOCINF(" + locator + ")"
                + " from qsys2.qsqptabl");
            rs.next();
            answer2[i] = rs.getString(1);
            rs.close();
          }
          connection_.commit();
          rs1_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);

          assertCondition((answer2[0].indexOf("ERROR") >= 0)
              && (answer2[FREE_LOCATOR_BLOCK_SIZE - 1].indexOf("ERROR") >= 0),
              "Locator should have been freed: Check after said " + answer2[0]);

        }

        catch (Exception e) {
          failed(
              connection_,
              e,
              "Unexpected Exception-This is a case where lob is already freed on host (toolbox ignores returncode back from host in this case)");
        }
      }
    }
  }

  /**
   * free() free() -- Shouldn't throw exception
   **/
  public void Var244() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc40()) {

      try {
        rs1_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "free");
        JDReflectionUtil.callMethod_V(clob, "free");
        assertCondition(true);
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
          assertCondition(e.getMessage().indexOf("not support") != -1);
          return;
        }
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() length() -- Make sure another method throws an exception after free
   **/
  public void Var245() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc40()) {
      try {
        rs1_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          JDReflectionUtil.callMethod_L(clob, "length");
          failed("Didn't throw SQLException after free for "
              + clob.getClass().getName());
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
          assertCondition(e.getMessage().indexOf("not support") != -1);
          return;
        }
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() getAsciiStream() -- Make sure another method throws an exception
   * after free
   **/
  public void Var246() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (skipAsciiTests) {
      notApplicable("getAsciiStreamTest");
      return;
    }
    if (checkJdbc40()) {
      try {
        rs1_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          JDReflectionUtil.callMethod_O(clob, "getAsciiStream");
          failed("Didn't throw SQLException after free for "
              + clob.getClass().getName());
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
          assertCondition(e.getMessage().indexOf("not support") != -1);
          return;
        }
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() getCharacterStream() -- Make sure another method throws an exception
   * after free
   **/
  public void Var247() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc40()) {
      try {
        rs1_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          JDReflectionUtil.callMethod_O(clob, "getCharacterStream");
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
          assertCondition(e.getMessage().indexOf("not support") != -1);
          return;
        }
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() getSubString() -- Make sure another method throws an exception after
   * free
   **/
  public void Var248() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc40()) {
      try {
        rs1_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          JDReflectionUtil.callMethod_O(clob, "getSubString", (long) 1, 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
          assertCondition(e.getMessage().indexOf("not support") != -1);
          return;
        }
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() position() -- Make sure another method throws an exception after
   * free
   **/
  public void Var249() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc40()) {
      try {
        rs1_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        Object clob2 = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          JDReflectionUtil.callMethod_L(clob, "position", clob2, 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
          assertCondition(e.getMessage().indexOf("not support") != -1);
          return;
        }
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() position() -- Make sure another method throws an exception after
   * free
   **/
  public void Var250() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc40()) {
      try {
        rs1_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          JDReflectionUtil.callMethod_L(clob, "position", "S", (long) 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
          assertCondition(e.getMessage().indexOf("not support") != -1);
          return;
        }
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() setAsciiStream() -- Make sure another method throws an exception
   * after free
   **/
  public void Var251() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc40()) {
      try {
        rs1_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          JDReflectionUtil.callMethod_O(clob, "setAsciiStream", (long) 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
          assertCondition(e.getMessage().indexOf("not support") != -1);
          return;
        }
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() setCharacterStream() -- Make sure another method throws an exception
   * after free
   **/
  public void Var252() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc40()) {
      try {
        rs1_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          JDReflectionUtil.callMethod_O(clob, "setCharacterStream", (long) 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
          assertCondition(e.getMessage().indexOf("not support") != -1);
          return;
        }
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() setString() -- Make sure another method throws an exception after
   * free
   **/
  public void Var253() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc40()) {
      try {
        rs1_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          JDReflectionUtil.callMethod_I(clob, "setString", (long) 1, "S");
          failed("Didn't throw SQLException for setString after free was called ");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
          assertCondition(e.getMessage().indexOf("not support") != -1);
          return;
        }
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() setString() -- Make sure another method throws an exception after
   * free
   **/
  public void Var254() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc40()) {
      try {
        rs1_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          JDReflectionUtil.callMethod_I(clob, "setString", (long) 1, "S", 1, 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
          assertCondition(e.getMessage().indexOf("not support") != -1);
          return;
        }
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * free() truncate() -- Make sure another method throws an exception after
   * free
   **/
  public void Var255() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (checkJdbc40()) {
      try {
        rs1_.absolute(1);
        Object clob = JDReflectionUtil.callMethod_O(rs1_, getMethod, "C_CLOB");
        JDReflectionUtil.callMethod_V(clob, "free");
        try {
          JDReflectionUtil.callMethod_V(clob, "truncate", (long) 10);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
            && getRelease() < JDTestDriver.RELEASE_V5R5M0) {
          assertCondition(e.getMessage().indexOf("not support") != -1);
          return;
        }
        failed(connection_, e, "Unexpected Exception");
      }
    }
  }

  /**
   * Make sure locator is not accessible after rs.close();
   **/
  public void Var256() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
      notApplicable("v7r3 or later  variation -- make sure locator not accessible after rs.close() ");
      return;
    }

    if (Integer.parseInt(lobThreshold) > 10000) {
      notApplicable("Lob locator test\n");
      return;
    } else {
      System.out.println("info:  lobThreshold is " + lobThreshold);
    }
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {

          connection_.setAutoCommit(false);
          rs6_ = statement2_.executeQuery("SELECT * FROM " + TABLE6_);
          rs6_.next();

          Object clob = JDReflectionUtil
              .callMethod_O(rs6_, getMethod, "C_CLOB");

          if (clob instanceof com.ibm.as400.access.AS400JDBCClob) {
            notApplicable("non locator LOB used");
          } else {
            int locator = JDReflectionUtil.callMethod_I(clob, "getLocator");

            // Check the locator using DB locator value (not using the CLOB
            // object)

            ResultSet rs = statement2_.executeQuery("select "
                + JDLobTest.COLLECTION + ".CLOBLOCINF(" + locator + ")"
                + " from qsys2.qsqptabl");
            rs.next();
            String answer1 = rs.getString(1);
            rs.close();

            rs6_.close();

            // Check the locator using DB locator value (not using the CLOB
            // object)

            rs = statement2_.executeQuery("select " + JDLobTest.COLLECTION
                + ".CLOBLOCINF(" + locator + ")" + " from qsys2.qsqptabl");
            rs.next();
            String answer2 = rs.getString(1);
            rs.close();
            connection_.commit();
            rs1_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);

            assertCondition(
                (answer1.indexOf("ERROR") < 0)
                    && (answer2.indexOf("ERROR") >= 0),
                "Check before said '"
                    + answer1
                    + "' Check after said '"
                    + answer2
                    + "'both should have returned ERROR because a locator should not be accessible after rs.close().  New var for v7r1");
          }

        } catch (Exception e) {
          System.out.println(e);
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * Make sure locator is not accessible after commit
   **/
  public void Var257() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }
    if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
      notApplicable("v5r5 variation");
      return;
    }
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
        && getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("native variation for now");
      return;
    }

    if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
      if (checkLobSupport()) {
        try {

          connection_.setAutoCommit(false);
          rs6_ = statement2_.executeQuery("SELECT * FROM " + TABLE6_);
          rs6_.next();

          Object clob = JDReflectionUtil
              .callMethod_O(rs6_, getMethod, "C_CLOB");

          int locator = JDReflectionUtil.callMethod_I(clob, "getLocator");

          // Check the locator using DB locator value (not using the CLOB
          // object)

          ResultSet rs = statement2_.executeQuery("select "
              + JDLobTest.COLLECTION + ".CLOBLOCINF(" + locator + ")"
              + " from qsys2.qsqptabl");
          rs.next();
          String answer1 = rs.getString(1);
          rs.close();

          connection_.commit();
          rs1_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);

          // Check the locator using DB locator value (not using the CLOB
          // object)

          rs = statement2_.executeQuery("select " + JDLobTest.COLLECTION
              + ".CLOBLOCINF(" + locator + ")" + " from qsys2.qsqptabl");
          rs.next();
          String answer2 = rs.getString(1);
          rs.close();
          connection_.commit();
          rs1_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);

          assertCondition(
              (answer1.indexOf("ERROR") < 0) && (answer2.indexOf("ERROR") >= 0),
              "Check before said " + answer1 + "Check after said " + answer2);

        } catch (Exception e) {
          System.out.println(e);
          failed(connection_, e, "Unexpected Exception");
        }
      }
    }
  }

  public void Var258() {
    notApplicable("Reserved for expansion");
  }

  public void Var259() {
    notApplicable("Reserved for expansion");
  }

  public void Var260() {
    notApplicable("reserved for expansion");
  }

  public void Var261() {
    notApplicable("reserved for expansion");
  }

  public void Var262() {
    notApplicable("reserved for expansion");
  }

  public void Var263() {
    notApplicable("reserved for expansion");
  }

  public void Var264() {
    notApplicable("reserved for expansion");
  }

  public void Var265() {
    notApplicable("reserved for expansion");
  }

  public void Var266() {
    notApplicable("reserved for expansion");
  }

  public void Var267() {
    notApplicable("reserved for expansion");
  }

  public void Var268() {
    notApplicable("reserved for expansion");
  }

  public void Var269() {
    notApplicable("reserved for expansion");
  }

  public void Var270() {
    notApplicable("reserved for expansion");
  }

  public void Var271() {
    notApplicable("reserved for expansion");
  }

  public void Var272() {
    notApplicable("reserved for expansion");
  }

  public void Var273() {
    notApplicable("reserved for expansion");
  }

  public void Var274() {
    notApplicable("reserved for expansion");
  }

  public void Var275() {
    notApplicable("reserved for expansion");
  }

  public void Var276() {
    notApplicable("reserved for expansion");
  }

  public void Var277() {
    notApplicable("reserved for expansion");
  }

  public void Var278() {
    notApplicable("reserved for expansion");
  }

  public void Var279() {
    notApplicable("reserved for expansion");
  }

  public void Var280() {
    notApplicable("reserved for expansion");
  }

  public void Var281() {
    notApplicable("reserved for expansion");
  }

  public void Var282() {
    notApplicable("reserved for expansion");
  }

  public void Var283() {
    notApplicable("reserved for expansion");
  }

  public void Var284() {
    notApplicable("reserved for expansion");
  }

  public void Var285() {
    notApplicable("reserved for expansion");
  }

  public void Var286() {
    notApplicable("reserved for expansion");
  }

  public void Var287() {
    notApplicable("reserved for expansion");
  }

  public void Var288() {
    notApplicable("reserved for expansion");
  }

  public void Var289() {
    notApplicable("reserved for expansion");
  }

  public void Var290() {
    notApplicable("reserved for expansion");
  }

  public void Var291() {
    notApplicable("reserved for expansion");
  }

  public void Var292() {
    notApplicable("reserved for expansion");
  }

  public void Var293() {
    notApplicable("reserved for expansion");
  }

  public void Var294() {
    notApplicable("reserved for expansion");
  }

  public void Var295() {
    notApplicable("reserved for expansion");
  }

  public void Var296() {
    notApplicable("reserved for expansion");
  }

  public void Var297() {
    notApplicable("reserved for expansion");
  }

  public void Var298() {
    notApplicable("reserved for expansion");
  }

  public void Var299() {
    notApplicable("reserved for expansion");
  }

  // Check the use of free locator.
  public void Var300() {
    if (requireJdbc40 && (!isJdbc40())) {
      notApplicable("JDBC 4.0 testcase");
      return;
    }

    int rows = 250000;

    // Make sure the table has the number of rows
    {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s
            .executeQuery("select count(*) from SYSIBM.SQLCOLUMNS ");
        rs.next();
        int rowCount = rs.getInt(1);
        s.close();
        if (rowCount < rows) {
          System.out.println("Warning: number of columns(" + rowCount + ") < "
              + rows);
          System.out
              .println("Please increase the number of columns on the system");

          rows = rowCount - 25;
        }
      } catch (Exception e) {
        System.out.println("Exception getting row count\n");
        e.printStackTrace(System.out);
      }
    }

    // If not running on AS/400 this takes too long.
    if (rows <= 25000 || checkClientOn400()) {

      if (checkRelease710()) {
        if (checkJdbc40()) {
          try {
            StringBuffer sb1 = new StringBuffer();
            sb1.append("Test the use of " + rows + " rows of locators\n");
            boolean passed = true;

            int rowCount = 0;
            Statement s = connection_.createStatement();
            ResultSet rs = s
                .executeQuery("select CAST('<a>a</a>' AS CLOB(1000) CCSID "
                    + CCSID_ + " ) from SYSIBM.SQLCOLUMNS FETCH FIRST " + rows
                    + " ROWS ONLY");
            long nextMillis = System.currentTimeMillis() + 60000;
            while (rs.next()) {
              Object o = JDReflectionUtil.callMethod_O(rs, getMethod, 1);
              JDReflectionUtil.callMethod_V(o, "free");
              rowCount++;
              long nowMillis = System.currentTimeMillis();
              if ((nowMillis > nextMillis) || (rowCount % 2500 == 0)) {
                java.sql.Timestamp now = new java.sql.Timestamp(nowMillis);
                System.out.println("On row " + rowCount + "/" + rows + " "
                    + now);
                nextMillis = System.currentTimeMillis() + 60000;
              }
            }
            rs.close();
            s.close();
            passed = (rowCount >= rows);
            if (!passed)
              sb1.append("Error rowCount=" + rowCount + " sb >= " + rows);
            connection_.commit();
            rs1_ = statement1_.executeQuery("SELECT * FROM " + TABLE_);

            assertCondition(passed, sb1.toString());

          } catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
  }

  public void Var301() {
    notApplicable("Used by JDLobClobLocator8");
  }

  public void Var302() {
    notApplicable("Used by JDLobClobLocator8");
  }

  public void Var303() {
    notApplicable("Used by JDLobClobLocator8");
  }

  public void Var304() {
    notApplicable("Used by JDLobClobLocator8");
  }

  public void Var305() {
    notApplicable("Used by JDLobClobLocator8");
  }

  public void Var306() {
    notApplicable("Used by JDLobClobLocator8");
  }

  public void Var307() {
    notApplicable("Used by JDLobClobLocator8");
  }

  public void Var308() {
    notApplicable("Used by JDLobClobLocator8");
  }

  public void Var309() {
    notApplicable("Used by JDLobClobLocator8");
  }

}
