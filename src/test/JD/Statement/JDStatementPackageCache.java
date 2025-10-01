///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementPackageCache.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementPackageCache.java
//
// Classes:      JDStatementPackageCache
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.*;

import test.JDReflectionUtil;
import test.JDStatementTest;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import java.io.*;

/**
 * Testcase JDStatementPackageCache. This tests the following method of the JDBC
 * Statement class:
 * 
 * <ul>
 * <li>addBatch()</li>
 * <li>clearBatch()</li>
 * <li>executeBatch()</li>
 * </ul>
 **/
public class JDStatementPackageCache extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDStatementPackageCache";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDStatementTest.main(newArgs); 
   }

  // -----------------------------------------------------------------------
  // The strategy of this testcase is to run various kinds of statements
  // to see what goes in the SQL package. They are run twice. Once
  // with default properties. Once with the 'package all select'
  // property set.
  //
  // DBMon is started because DBMon tracks which statements go into
  // the package.
  //
  // Each statement is run against its own table. Then we look at
  // the DBMon output. If a record for that table is in the output
  // then we know the statement was packaged.
  // -----------------------------------------------------------------------

  // Private data.
  private String collection = JDStatementTest.COLLECTION;

  private String powerUserID_;

  private char[] encryptedPowerPassword_;

  private String url_;

  private AS400 system;

  private boolean setupComplete = false;

  private boolean setupCompleteAll = false;

  // set up assuming all failed
  private boolean parameterMarkers = false;

  private boolean parameterMarkersWithUpdate = false;

  private boolean parameterMarkersWithDelete = false;

  private boolean parameterMarkersWithIsCurrentOf = false;

  private boolean insertWithSubSelect = false;

  private boolean selectWithForUpdate = false;

  // private boolean declare = false;
  // private boolean selectWithExtraFlag = false;
  private boolean ordinarySelect = false;

  private boolean ordinarySelectWithConstant = false;

  private boolean ordinaryInsert = false;

  private boolean insertWithPM = false;

  private boolean createProc = false;

  /**
   * Constructor.
   **/
  public JDStatementPackageCache(AS400 systemObject, Hashtable<String, Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String powerUserID, String powerPassword) {
    super(systemObject, "JDStatementPackageCache", namesAndVars, runMode,
        fileOutputStream, password);

    powerUserID_ = powerUserID;
    encryptedPowerPassword_ = PasswordVault.getEncryptedPassword(powerPassword);

    if ((powerUserID_ != null) && (powerPassword != null)) {
       char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject.getSystemName(), powerUserID_,
          charPassword);
       PasswordVault.clearPassword(charPassword);
       
    }
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    // Reset the collection because it could have been specified on the command
    // line
    collection = JDStatementTest.COLLECTION;

    CommandCall cc = new CommandCall(system);
    cc.run("QSYS/DLTF "+collection+"/QAQQINI"); 

    
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (url_ == null)
      return;

    try {
      Connection connection = testDriver_.getConnection (url_,systemObject_.getUserId(), encryptedPassword_);

      Statement s = connection.createStatement();
        executeUpdateCatchException(s,"drop table " + collection + ".PkgTest01");
        executeUpdateCatchException(s,"drop table " + collection + ".PkgTest02");
        executeUpdateCatchException(s,"drop table " + collection + ".PkgTest03");
        executeUpdateCatchException(s,"drop table " + collection + ".PkgTest04");
        executeUpdateCatchException(s,"drop table " + collection + ".PkgTest05");
        executeUpdateCatchException(s,"drop table " + collection + ".PkgTest06");
        executeUpdateCatchException(s,"drop table " + collection + ".PkgTest07");
        executeUpdateCatchException(s,"drop table " + collection + ".PkgTest08");
        executeUpdateCatchException(s,"drop procedure " + collection + ".PkgTest09");
        executeUpdateCatchException(s,"drop table " + collection + ".PkgTest10");
        executeUpdateCatchException(s,"drop table " + collection + ".PkgTest11");
        executeUpdateCatchException(s,"drop table " + collection + ".PkgTest12");

      s.close();
      connection.commit(); // for xa
      connection.close();
    } catch (Exception e) {
    }
  }

  private void doSetup(boolean all) {

    parameterMarkers = false;
    parameterMarkersWithUpdate = false;
    parameterMarkersWithDelete = false;
    parameterMarkersWithIsCurrentOf = false;
    insertWithSubSelect = false;
    selectWithForUpdate = false;
    // declare = false;

    ordinarySelect = false;
    ordinarySelectWithConstant = false;
    ordinaryInsert = false;
    insertWithPM = false;
    createProc = false;

    url_ = baseURL_ ;

    String connect_string = url_ + ";extended dynamic=true;"
        + "package=dawpkg1;" + "package library=" + collection + ";";
    if (all)
      connect_string = connect_string + "package criteria=select;";

    String setup_string = url_;

    Connection connection = null;
    Statement statement = null;
    Statement statement2 = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
      // first, clean up the old package
      try {
        CommandCall cc = new CommandCall(system);
        cc.run("QSYS/DLTSQLPKG SQLPKG(" + collection + "/DAWPKG9899)"); // is current
                                                                   // package
                                                                   // ending
        AS400Message[] ml = cc.getMessageList();
        if (ml != null) {
          /* CPF2015 = Object &1 in &2 type *&3 not found. */
          /* CPC2192 = Object &1 in &2 type *&3 deleted. */
          if ((!ml[0].getID().equals("CPF2105"))
              && (!ml[0].getID().equals("CPC2191"))) {
            System.out.println("   Message: " + ml[0]);
            System.out
                .println("   Warning, delete of old package failed.  Tried to delete package DAWPKG9899.");
            System.out
                .println("   If package name wrong we could be using old data.");
          }
        }
      } catch (Exception e) {
        System.out.println("Warning, error deleting old package");
        e.printStackTrace();
      }

      // clean up tables if they are around from a previous run
      Connection c = testDriver_.getConnection(setup_string,systemObject_.getUserId(), encryptedPassword_);
      Statement s = c.createStatement();

      
      // Create the tables we will use for this testcase
      try {
        initTable(s, collection
            + ".PkgTest01"," (p1_int integer, p2_char varchar(20), p3_char varchar(10))");
      } catch (Exception e) {
	  e.printStackTrace(); 
      }
      try {
	  initTable(s, collection
            + ".PkgTest02"," (p1_int integer, p2_char varchar(20), p3_char varchar(10))");
      } catch (Exception e) {
 	  e.printStackTrace(); 
     }
      try {
	  initTable(s, collection
            + ".PkgTest03"," (p1_int integer, p2_char varchar(20), p3_char varchar(10))");
      } catch (Exception e) {
	  e.printStackTrace(); 
      }
      try {
	  initTable(s, collection
            + ".PkgTest04"," (p1_int integer, p2_char varchar(20), p3_char varchar(10))");
      } catch (Exception e) {
	  e.printStackTrace(); 
      }
      try {
	  initTable(s, collection
            + ".PkgTest05"," (p1_int integer, p2_char varchar(20), p3_char varchar(10))");
      } catch (Exception e) {
 	  e.printStackTrace(); 
     }
      try {
	  initTable(s, collection
            + ".PkgTest06"," (p1_int integer, p2_char varchar(20), p3_char varchar(10))");
      } catch (Exception e) {
	  e.printStackTrace(); 
      }
      try {
	  initTable(s, collection
            + ".PkgTest07"," (p1_int integer, p2_char varchar(20), p3_char varchar(10))");
      } catch (Exception e) {
	  e.printStackTrace(); 
      }
      try {
	  initTable(s, collection
            + ".PkgTest08"," (p1_int integer, p2_char varchar(20), p3_char varchar(10))");
      } catch (Exception e) {
	  e.printStackTrace(); 
      }
      try {
	  initTable(s, collection
            + ".PkgTest10"," (p1_int integer, p2_char varchar(20), p3_char varchar(10))");
      } catch (Exception e) {
	  e.printStackTrace(); 
      }
      try {
	  initTable(s, collection
            + ".PkgTest11"," (p1_int integer, p2_char varchar(20), p3_char varchar(10))");
      } catch (Exception e) {
      }
      try {
	  initTable(s, collection
            + ".PkgTest12"," (p1_int integer, p2_char varchar(20), p3_char varchar(10))");
      } catch (Exception e) {
	  e.printStackTrace(); 
      }

      // Add rows to those tables

        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest01 values ( 1, 'one',   'aaa' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest01 values ( 2, 'two',   'bbb' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest01 values ( 3, 'three', 'ccc' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest02 values ( 1, 'one',   'aaa' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest02 values ( 2, 'two',   'bbb' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest02 values ( 3, 'three', 'ccc' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest03 values ( 1, 'one',   'aaa' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest03 values ( 2, 'two',   'bbb' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest03 values ( 3, 'three', 'ccc' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest04 values ( 1, 'one',   'aaa' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest04 values ( 2, 'two',   'bbb' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest04 values ( 3, 'three', 'ccc' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest05 values ( 1, 'one',   'aaa' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest05 values ( 2, 'two',   'bbb' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest05 values ( 3, 'three', 'ccc' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest06 values ( 1, 'one',   'aaa' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest06 values ( 2, 'two',   'bbb' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest06 values ( 3, 'three', 'ccc' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest07 values ( 1, 'one',   'aaa' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest07 values ( 2, 'two',   'bbb' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest07 values ( 3, 'three', 'ccc' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest08 values ( 1, 'one',   'aaa' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest08 values ( 2, 'two',   'bbb' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest08 values ( 3, 'three', 'ccc' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest10 values ( 1, 'one',   'aaa' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest10 values ( 2, 'two',   'bbb' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest10 values ( 3, 'three', 'ccc' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest11 values ( 1, 'one',   'aaa' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest11 values ( 2, 'two',   'bbb' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest11 values ( 3, 'three', 'ccc' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest12 values ( 1, 'one',   'aaa' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest12 values ( 2, 'two',   'bbb' )");
        executeUpdateCatchException(s,"insert into " + collection
            + ".PkgTest12 values ( 3, 'three', 'ccc' )");

        s.close();
        c.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      // force these selects into the sql package (v6r1 has a threshold for sql
      // use count before it is added)
      // //////1 2 3 on same connection
      System.out.println("Connecting with "+connect_string); 
      connection = testDriver_.getConnection(connect_string,systemObject_.getUserId(), encryptedPassword_);

      for (int x = 0; x < 3; x++) {

        statement = connection.createStatement();
        statement2 = connection.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        rs = statement.executeQuery("SELECT * FROM " + collection
            + ".PkgTest01 ");
        rs.close();

        rs = statement.executeQuery("SELECT * FROM " + collection
            + ".PkgTest02 WHERE p1_int = 1 ");
        rs.close();

        ps = connection.prepareStatement("SELECT * FROM " + collection
            + ".PkgTest03 WHERE p2_char = ? ");
        ps.setString(1, "two");
        rs = ps.executeQuery();
        rs.close();

        ps = connection.prepareStatement("SELECT * FROM " + collection
            + ".PkgTest04 WHERE p3_char = 'ccc'");
        rs = ps.executeQuery();
        rs.close();

        statement.executeUpdate("INSERT INTO " + collection
            + ".PkgTest05 (p1_int, p2_char) values ( 4, 'four')  ");

        ps = connection.prepareStatement("INSERT INTO " + collection
            + ".PkgTest06 values ( ?, ?, ? )");
        ps.setInt(1, 5);
        ps.setString(2, "five");
        ps.setString(3, "eee");
        ps.executeUpdate();
        rs.close();

        rs = statement2.executeQuery("SELECT * FROM " + collection
            + ".PkgTest07 ");

        if (rs.next()) {
          String cursorName = rs.getCursorName();
          PreparedStatement ps2 = connection.prepareStatement("UPDATE "
              + collection + ".PkgTest07 SET p1_int = ? where current of "
              + cursorName);
          ps2.setInt(1, 55);
          ps2.executeUpdate();
          ps2.close();
        }
        rs.close();

        ps = connection.prepareStatement("SELECT * FROM " + collection
            + ".PkgTest08 FOR UPDATE");
        rs = ps.executeQuery();

        try {
          statement.executeUpdate("DROP PROCEDURE "+collection+".PkgTest09"); 
        } catch (Exception e) { 
          String message = e.toString(); 
          if (message.indexOf("not found") < 0 ) { 
             System.out.println("WARNING:  Unexpected exception dropping procedure"); 
             e.printStackTrace(System.out);
          }
        }
        String proc = "CREATE PROCEDURE " + collection
            + ".PkgTest09 (V_NM VARCHAR(20)) " + "RESULT SET 1 LANGUAGE SQL "
            + "BEGIN  " + "   DECLARE C3 CURSOR FOR SELECT * FROM "
            + collection + ".PkgTest08 ; " + "   OPEN C3 ; "
            + "   SET RESULT SETS CURSOR C3 ; " + "END  ";

        
       statement.executeUpdate(proc);

        statement.executeUpdate("INSERT INTO " + collection
            + ".PkgTest10 SELECT * from " + collection + ".PkgTest08 ");

      }

      connection.close();

      // //////////4 I don't know why we have to close and reopen conn here, but
      // we do
      connection = testDriver_.getConnection(connect_string,systemObject_.getUserId(), encryptedPassword_);

      statement = connection.createStatement();
      statement2 = connection.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

      rs = statement
          .executeQuery("SELECT * FROM " + collection + ".PkgTest01 ");
      rs.close();

      rs = statement.executeQuery("SELECT * FROM " + collection
          + ".PkgTest02 WHERE p1_int = 1 ");
      rs.close();

      ps = connection.prepareStatement("SELECT * FROM " + collection
          + ".PkgTest03 WHERE p2_char = ? ");
      ps.setString(1, "two");
      rs = ps.executeQuery();
      rs.close();

      ps = connection.prepareStatement("SELECT * FROM " + collection
          + ".PkgTest04 WHERE p3_char = 'ccc'");
      rs = ps.executeQuery();
      rs.close();

      statement.executeUpdate("INSERT INTO " + collection
          + ".PkgTest05 (p1_int, p2_char) values ( 4, 'four')  ");

      ps = connection.prepareStatement("INSERT INTO " + collection
          + ".PkgTest06 values ( ?, ?, ? )");
      ps.setInt(1, 5);
      ps.setString(2, "five");
      ps.setString(3, "eee");
      ps.executeUpdate();
      rs.close();

      rs = statement2.executeQuery("SELECT * FROM " + collection
          + ".PkgTest07 ");

      if (rs.next()) {
        String cursorName = rs.getCursorName();
        PreparedStatement ps2 = connection.prepareStatement("UPDATE "
            + collection + ".PkgTest07 SET p1_int = ? where current of "
            + cursorName);
        ps2.setInt(1, 55);
        ps2.executeUpdate();
        ps2.close();
      }
      rs.close();

      ps = connection.prepareStatement("SELECT * FROM " + collection
          + ".PkgTest08 FOR UPDATE");
      rs = ps.executeQuery();
      /*
       * String proc = "CREATE PROCEDURE " + collection +
       * ".PkgTest09 (V_NM VARCHAR(20)) "
       * 
       * + "RESULT SET 1 LANGUAGE SQL " + "BEGIN  " +
       * "   DECLARE C3 CURSOR FOR SELECT * FROM " + collection +
       * ".PkgTest08 ; " + "   OPEN C3 ; " + "   SET RESULT SETS CURSOR C3 ; " +
       * "END  ";
       */
      // statement.executeUpdate(proc);

      statement.executeUpdate("INSERT INTO " + collection
          + ".PkgTest10 SELECT * from " + collection + ".PkgTest08 ");

      connection.close();

      // Start DBMon. That will trace statements being run. Later we
      // will look at the output from DBMon to see what was added to the
      // sql cache.
      connection = testDriver_.getConnection(connect_string,systemObject_.getUserId(), encryptedPassword_);

      // use reflection for proxy server
      String job = JDReflectionUtil.callMethod_S(connection,
          "getServerJobIdentifier");

      String DBName1 = "QUSRSYS/QJT" + job.substring(20);
      String DBName2 = "QUSRSYS.QJT" + job.substring(20);
      runCommand(connection, "QSYS/STRDBMON OUTFILE(" + DBName1
          + ") JOB(*) TYPE(*DETAIL)", true);

      statement = connection.createStatement();
      statement2 = connection.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

      // with v6r1, things are only put in package after run x number of times
      // (x is 3 last I heard)

      rs = statement
          .executeQuery("SELECT * FROM " + collection + ".PkgTest01 ");
      rs.close();

      rs = statement.executeQuery("SELECT * FROM " + collection
          + ".PkgTest02 WHERE p1_int = 1 ");
      rs.close();

      ps = connection.prepareStatement("SELECT * FROM " + collection
          + ".PkgTest03 WHERE p2_char = ? ");
      ps.setString(1, "two");
      rs = ps.executeQuery();
      rs.close();

      ps = connection.prepareStatement("SELECT * FROM " + collection
          + ".PkgTest04 WHERE p3_char = 'ccc'");
      rs = ps.executeQuery();
      rs.close();

      statement.executeUpdate("INSERT INTO " + collection
          + ".PkgTest05 (p1_int, p2_char) values ( 4, 'four')  ");

      ps = connection.prepareStatement("INSERT INTO " + collection
          + ".PkgTest06 values ( ?, ?, ? )");
      ps.setInt(1, 5);
      ps.setString(2, "five");
      ps.setString(3, "eee");
      ps.executeUpdate();
      rs.close();

      rs = statement2.executeQuery("SELECT * FROM " + collection
          + ".PkgTest07 ");

      if (rs.next()) {
        String cursorName = rs.getCursorName();
        PreparedStatement ps2 = connection.prepareStatement("UPDATE "
            + collection + ".PkgTest07 SET p1_int = ? where current of "
            + cursorName);
        ps2.setInt(1, 55);
        ps2.executeUpdate();
        ps2.close();
      }
      rs.close();

      ps = connection.prepareStatement("SELECT * FROM " + collection
          + ".PkgTest08 FOR UPDATE");
      rs = ps.executeQuery();

      /*
       * proc = "CREATE PROCEDURE " + collection +
       * ".PkgTest09 (V_NM VARCHAR(20)) " + "RESULT SET 1 LANGUAGE SQL " +
       * "BEGIN  " + "   DECLARE C3 CURSOR FOR SELECT * FROM " + collection +
       * ".PkgTest08 ; " + "   OPEN C3 ; " + "   SET RESULT SETS CURSOR C3 ; " +
       * "END  ";
       */
      // statement.executeUpdate(proc);

      statement.executeUpdate("INSERT INTO " + collection
          + ".PkgTest10 SELECT * from " + collection + ".PkgTest08 ");

      ps = connection.prepareStatement("UPDATE " + collection
          + ".PkgTest11 SET p1_int = ?");
      ps.setInt(1, 5);
      ps.executeUpdate();
      rs.close();

      ps = connection.prepareStatement("DELETE FROM " + collection
          + ".PkgTest12 WHERE p1_int = ?");
      ps.setInt(1, 5);
      ps.executeUpdate();
      rs.close();

      // now stop the db monitor and see what was added to the cache
      runCommand(connection, "QSYS/ENDDBMON", true);

      String DBMonQuery = "select qqc103 as pkgname, qqc21 as sqlop, qq1000 as sqlstmt from "
          + DBName2 + " where qqrid=1000 and qqc103 <> ''";
      /*
       * select qqc103 as pkgname, qqc21 as sqlop, qq1000 as sqlstmt from
       * qusrsys/qjt207853 where qqrid=1000 and qqc103 <> ''
       */

      rs = statement.executeQuery(DBMonQuery);

      while (rs.next()) {
        // System.out.println(rs.getString(1) + " " + rs.getString(3));
        String data = rs.getString(1) + " " + rs.getString(3);
        data = data.toUpperCase();

        if (data.indexOf("DAWPKG") >= 0) {
          if (data.indexOf("PKGTEST01") >= 0) {
            System.out.println("Found " + data);
            ordinarySelect = true;
          }

          if (data.indexOf("PKGTEST02") >= 0) {
            System.out.println("Found " + data);
            ordinarySelectWithConstant = true;
          }

          if (data.indexOf("PKGTEST03") >= 0) {
            System.out.println("Found " + data);
            parameterMarkers = true;
          }

          if (data.indexOf("PKGTEST04") >= 0) {
            System.out.println("Found " + data);
            ordinarySelectWithConstant = true;
          }

          if (data.indexOf("PKGTEST05") >= 0) {
            System.out.println("Found " + data);
            ordinaryInsert = true;
          }

          if (data.indexOf("PKGTEST06") >= 0) {
            System.out.println("Found " + data);
            insertWithPM = true;
          }

          if (data.indexOf("PKGTEST07") >= 0) {
            System.out.println("Found " + data);
            insertWithSubSelect = true;
          }

          if (data.indexOf("PKGTEST08") >= 0) {
            System.out.println("Found " + data);
            selectWithForUpdate = true;
          }

          if (data.indexOf("PKGTEST09") >= 0) {
            System.out.println("Found " + data);
            createProc = true;
          }

          if (data.indexOf("PKGTEST10") >= 0) {
            System.out.println("Found " + data);
            insertWithSubSelect = true;
          }

          if (data.indexOf("PKGTEST11") >= 0) {
            System.out.println("Found " + data);
            insertWithSubSelect = true;
          }

          if (data.indexOf("PKGTEST12") >= 0) {
            System.out.println("Found " + data);
            insertWithSubSelect = true;
          }
        }
      } /* RS.next */

      // Delete the output file.
      runCommand(connection, "QSYS/DLTF FILE(" + DBName1 + ") ", true);

    } catch (Exception e) {
      e.printStackTrace();
    }

    if (rs != null)
      try {
        rs.close();
      } catch (Exception e1) {
      }
    if (statement != null)
      try {
        statement.close();
      } catch (Exception e1) {
      }
    if (statement2 != null)
      try {
        statement2.close();
      } catch (Exception e1) {
      }
    if (connection != null)
      try {
        connection.close();
      } catch (Exception e1) {
      }

    if (all)
      setupCompleteAll = true;
    else
      setupComplete = true;
  }

  private void executeUpdateCatchException(Statement s, String string) {
    try { 
      s.executeUpdate(string); 
    } catch (Exception e) {
      System.out.println("Exception on "+string); 
      e.printStackTrace(System.out); 
    }
  }

  void runCommand(Connection connection, String command, boolean SQLNaming)
      throws SQLException {
    Statement statement = connection.createStatement();

    // We run commands via the QCMDEXC stored procedure. That procedure
    // requires the length of the command be included with the command
    // specified in precision 15, scale 5 format. That is,
    // "CALL QSYS.QCMDEXC('command-to-run', 000000nnnn.00000)"
    String commandLength = "0000000000" + command.length();
    commandLength = commandLength.substring(commandLength.length() - 10)
        + ".00000";

    String commandPreface;

    if (SQLNaming)
      commandPreface = "CALL QSYS.QCMDEXC('";
    else
      commandPreface = "CALL QSYS/QCMDEXC('";

    String SQLCommand = commandPreface + command + "', " + commandLength + ")";

    // System.out.println("Running command: " + SQLCommand);

    statement.executeUpdate(SQLCommand);
    statement.close();
  }

  /**
**/
  public void Var001() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupComplete)
        doSetup(false);

      if (parameterMarkers)
        succeeded();
      else
        failed("statment with parameter markers not added to SQL package");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  /**

**/
  public void Var002() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupComplete)
        doSetup(false);

      if (insertWithSubSelect)
        succeeded();
      else
        failed("insert with subselect not added to SQL package");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  /**
**/
  public void Var003() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupComplete)
        doSetup(false);

      if (selectWithForUpdate)
        succeeded();
      else
        failed("select with updatable cursor not added to SQL package");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  /**
**/
  public void Var004() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupComplete)
        doSetup(false);

      if (!parameterMarkersWithUpdate)
        succeeded();
      else
        failed("update statement with parameter markers incorrectly added to SQL package");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  /**
**/
  public void Var005() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupComplete)
        doSetup(false);

      if (!parameterMarkersWithDelete)
        succeeded();
      else
        failed("delete statement with parameter markers incorrectly added to SQL package");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  /**
**/
  public void Var006() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupComplete)
        doSetup(false);

      if (!parameterMarkersWithIsCurrentOf)
        succeeded();
      else
        failed("update with 'where current of' incorrectly added to SQL package");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var007() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupComplete)
        doSetup(false);

      if (!ordinarySelect)
        succeeded();
      else
        failed("ordinary select incorrectly added to SQL package (flag is off)");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var008() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupComplete)
        doSetup(false);

      if (!ordinarySelectWithConstant)
        succeeded();
      else
        failed("ordinary select with constant incorrectly added to SQL package (flag is off)");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var009() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupComplete)
        doSetup(false);

      if (!ordinaryInsert)
        succeeded();
      else
        failed("ordinary insert with constant incorrectly added to SQL package (flag is off)");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var010() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupComplete)
        doSetup(false);

      if (insertWithPM)
        succeeded();
      else
        failed("insert with parameter markers noy in SQL package (flag is off)");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var011() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupComplete)
        doSetup(false);

      if (!createProc)
        succeeded();
      else
        failed("Create procedure incorrectly added to SQL package (flag is off)");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  // --------------------------------------------------
  // Now run them again with the cache-all property
  // --------------------------------------------------

  public void Var012() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      if (parameterMarkers)
        succeeded();
      else
        failed("statment with parameter markers not added to SQL package");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var013() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      if (insertWithSubSelect)
        succeeded();
      else
        failed("insert with subselect not added to SQL package");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var014() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      if (selectWithForUpdate)
        succeeded();
      else
        failed("select with updatable cursor not added to SQL package");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var015() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      if (!parameterMarkersWithUpdate)
        succeeded();
      else
        failed("update statement with parameter markers incorrectly added to SQL package");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var016() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      if (!parameterMarkersWithDelete)
        succeeded();
      else
        failed("delete statement with parameter markers incorrectly added to SQL package");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var017() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      if (!parameterMarkersWithIsCurrentOf)
        succeeded();
      else
        failed("update with 'where current of' incorrectly added to SQL package");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var018() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      if (ordinarySelect)
        succeeded();
      else
        failed("ordinary select not added to SQL package when flag is on)");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var019() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      if (ordinarySelectWithConstant)
        succeeded();
      else
        failed("ordinary select with constant not added to SQL package when flag is on");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var020() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      if (!ordinaryInsert)
        succeeded();
      else
        failed("ordinary insert with constant incorrectly added to SQL package (flag is off)");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var021() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      if (insertWithPM)
        succeeded();
      else
        failed("insert with parameter markers noy in SQL package (flag is off)");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  public void Var022() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      if (!createProc)
        succeeded();
      else
        failed("Create procedure incorrectly added to SQL package (flag is off)");
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  /*
   * TC for hostserver bug package cache: 1 Prepare of statement1 handle1 ->
   * statement was found in local cache, so no flow was sent to server 2 Bind of
   * parameter for statement 1 -> Change RPB flows package statement name for
   * Execute 3 Execute of statement1 handle1 -> Execute of statement using
   * statement name from Package as input 4 FreeStatement (close) -> Does not
   * delete RPB 5 Prepare of statement2 handle1 -> statement was found in local
   * cache, so no flow was sent to server 6 Bind of parameter for statement 2 7
   * Bind of 2nd parameter for statement 2 -> Parameter marker descriptor sent
   * to server -> Change RPB flows package statement name for Execute 8 Execute
   * of statement2 handle1 -> Execute of statement causes a -313 error, since
   * the cached SQL descriptor is re-used when it shouldn't be 9 FreeStatement
   * (close) -> Does not delete RPB
   */
  public void Var023() {
    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      try {
         char[] charPassword = PasswordVault.decryptPassword(encryptedPowerPassword_);
        AS400JDBCDataSource d = new AS400JDBCDataSource(system_, powerUserID_,
            charPassword);
        d.setExtendedDynamic(true);
        d.setPackageLibrary(collection);
        d.setPackage("v23ca");
        d.setPackageCache(true);
        
        Connection conn = d.getConnection(powerUserID_, charPassword);
        String tab = collection + ".PkgTest01";
        Statement stmt = conn.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        // "create table " + collection +
        // ".PkgTest01 (p1_int integer, p2_char varchar(20), p3_char varchar(10))");
        // } catch (Exception e) { }

        String stmtStr = "select * from " + tab + " where p1_int = ?";
        String stmtStr2 = "select * from " + tab
            + " where p1_int = ? and p2_char = ?";
        // put in package if not there already
        PreparedStatement ps = conn.prepareStatement(stmtStr);
        ps.setInt(1, 1);
        ResultSet rs = ps.executeQuery();
        rs.close();
        ps.close();
        conn.close();

        conn = d.getConnection(powerUserID_, charPassword);
         PasswordVault.clearPassword(charPassword);
        
        // 1
        PreparedStatement ps2 = conn.prepareStatement(stmtStr);
        // 2
        ps2.setInt(1, 1);
        // 3
        ResultSet rs2 = ps2.executeQuery();
        // 4
        rs2.close();
        ps2.close();

        // 5
        ps2 = conn.prepareStatement(stmtStr2);

        // 6
        ps2.setInt(1, 1);
        // 7
        ps2.setString(2, "aaa");
        // 8
        rs2 = ps2.executeQuery(); // use package
        // 9
        rs2.close();
        ps2.close();

        assertCondition(true, "stmt was " + stmt);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  // 78ARTZ tc to test if the statement contains a lob, then we must re-issue
  // the prepare
  // testing the parms of a prepared statement
  public void Var024() {
    // Not working for V5R4 and on AS/400. Ignoring

    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      if (!setupCompleteAll)
        doSetup(true);

      String tableName = collection + ".JDSPCtest24";
      try {
        char[] charPassword = PasswordVault.decryptPassword(encryptedPowerPassword_);

        AS400JDBCDataSource d = new AS400JDBCDataSource(system_, powerUserID_,
            charPassword);

        d.setLobThreshold(1);
        d.setExtendedDynamic(true);
        d.setPackage("v23ca");
        d.setPackageLibrary(collection);
        d.setPackageCache(true);
        Connection conn = d.getConnection(powerUserID_, charPassword);

         
        try {

          Statement stmt = conn.createStatement();
          PreparedStatement ps = null;
          try {
	      initTable(stmt, tableName
                , " (  col1 blob(1048576), col2 clob(12), col3  blob(13), col4 clob(14))");// col1
                                                                                           // blob(1048576))");//0x100000
                                                                                           // or
                                                                                           // so
                                                                                           // in
                                                                                           // size

          } catch (Exception e) {
            System.out.println(e);
          }

          String s = "insert into " + tableName + " values (?, ?,?, ?)";

          ps = conn.prepareStatement(s);
          byte[] ba = new byte[]
            { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

          ps.setBytes(1, ba);
          ps.setString(2, "abc");
          ps.setBytes(3, ba);
          ps.setString(4, "abcd");

          ps.executeUpdate();

          conn = d.getConnection();

          ba = new byte[]
            { 1 };
          String s2 = "select col1, col2, col3, col4 from " + tableName
              + " where col1 != ?";
          ps = conn.prepareStatement(s2);
          ps.setBytes(1, ba);
          ResultSet rs = ps.executeQuery();

          conn = d.getConnection(powerUserID_, charPassword);
 PasswordVault.clearPassword(charPassword);
          // prepare from sql cache
          conn.prepareStatement(s2);
          rs = ps.executeQuery();
          conn.prepareStatement(s2);
          ps.setString(1, "c");
          rs = ps.executeQuery();
          conn.prepareStatement(s2);
          ps.setString(1, "d");
          rs = ps.executeQuery();

          rs.next();
          Blob bl = rs.getBlob(1);
          byte[] b = bl.getBytes(1, 10);

          String outputStr = new String(b);
          // System.out.println(outputStr);

          // do an insert via the cache
          s = "insert into " + tableName + " values (?, ?,?, ?)";

          ps = conn.prepareStatement(s);
          ba = new byte[]
            { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

          ps.setBytes(1, ba);
          ps.setString(2, "abc");
          ps.setBytes(3, ba);
          ps.setString(4, "abcd");

          ps.executeUpdate();

          assertCondition(true, "outputStr was " + outputStr);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }

    } else
      notApplicable("Testcase applies only to Toolbox");
  }

  // Test to verify that DBMON states that stuff is coming from the package.
  // The first set of tests does not specify a sort sequence.
  //

  public void Var025() {
      boolean passed = true;
      StringBuffer sb = new StringBuffer();

    if (isToolboxDriver()) {
      if (system == null) {
        failed("power userid and password not specified");
        return;
      }

      try {

	  if (!setupComplete)
	      doSetup(false);
	  String packageName="JDSP25";
	  String dbUrl = url_ + ";extended dynamic=true;"
	    + "package="+packageName+";" + "package library=" + collection + ";";

	  int i=0; 
	  int xi=0;
	  int j=0;
	  int iterations=20;


	  BufferedReader is = null;
	  if (interactive_) { 
	      is = new BufferedReader (new InputStreamReader(System.in)); 
	  }
	  int reuseCount = 5;
	  Random random = new Random(); 
	  int randomInt = random.nextInt(9999);
	  String qcustFile="QCUST"+randomInt; 
	  String ptFile="PKGTST"+randomInt;

	  /////////////////////////////// 
	  // Do setup before running test
	  ///////////////////////////////
	  t25setup(dbUrl, qcustFile, ptFile, reuseCount, sb);

	  //
	  // TODO:  Make sure that the package does not exist
	  //
	  Connection cDrop =  testDriver_.getConnection(url_,systemObject_.getUserId(), encryptedPassword_);

    String deletepkg = "CALL QSYS.QCMDEXC('DLTSQLPKG "+collection+"/"+packageName+"9899',0000000029.00000)";
    try { 
      Statement s = cDrop.createStatement(); 
      s.executeUpdate(deletepkg); 
    } catch (Exception e) {
      sb.append("Drop failed\n"); 
    }
    
	  cDrop.close(); 

	  String sql; 
		String colName2;
		String colType2;
		boolean callRs;

		  Connection c =  testDriver_.getConnection(dbUrl,systemObject_.getUserId(), encryptedPassword_);

		  
		  {
		      String ji = ((com.ibm.as400.access.AS400JDBCConnection)c).getServerJobIdentifier();
		      String jobname = ji.substring(20).trim()+"/"+ji.substring(10,20).trim()+"/"+ji.substring(0,10).trim(); 
		      sb.append(" Server Job Identifier-" + ji+"\n");
		      sb.append(" Server Job Name      -" + jobname+"\n");

		  }

		  if (is != null) {
		      System.out.println("Interactive:  press enter to continue");
		      is.readLine(); 
		  } 
		  Statement x = c.createStatement(); 

		  PreparedStatement cs;  

		  PreparedStatement cs2; 

		  PreparedStatement cs3;

		  PreparedStatement cc5;  

		  String deleteStatement="DELETE FROM "+ptFile+" WHERE C1 = ? AND C2 = 2 AND C3 = ?";
		  String insertStatement="INSERT INTO "+ptFile+" (C1,C2,C3) VALUES ('THIS IS A LONG LONG STRING VALUE FOR COLUMN C1',2,?)"; 
		  String updateStatement="UPDATE "+ptFile+" SET C1 = ?, C2 = ? , C3 = 3 WHERE C1 = ? AND C2 = 2 AND C3 = 3";
		  String selectForUpdateStatement="SELECT LSTNAM FROM "+collection+"."+qcustFile+" WHERE LSTNAM = "+
					       " ?  FOR UPDATE";
		  String selectStatement="SELECT LSTNAM FROM "+collection+"."+qcustFile+" WHERE LSTNAM = "+
					       " ?"; 

		  x.execute("SET SCHEMA "+collection); 
		  x.execute("CALL QSYS.QCMDEXC('CHGQRYA QRYOPTLIB("+collection+")',0000000027.00000)");
		  x.execute("CALL QSYS.QCMDEXC('STRDBMON  OUTFILE("+collection+"/DBMON25)           ',0000000042.00000)");
		  sql = "CALL QSYS.QCMDEXC('FRCQRYI NOMASK(*YES)               ',0000000030.00000)";
		  sb.append("Executing:"+sql+"\n"); 
		  x.execute(sql);
		  sb.append("Running " + iterations + " iterations"+"\n");
		  for (i=1;i<=iterations;i++) 
		  {
		      sb.append("Running iteration " + i +"\n");
		    //                                                               1234567890123456789012345678901234567890123456
		      sb.append("Prepare SQL Statement : "+insertStatement+"\n"); 
		      cs = c.prepareStatement(insertStatement); 
		      cs.setInt(1,3);
		      sb.append("Execute SQL Statement : "+insertStatement+"\n"); 
		      sb.append(" "+"\n");
		      cs.executeUpdate();
		      sb.append("Prepare SQL Statement : "+updateStatement+"\n"); 
		      cs2 = c.prepareStatement(updateStatement);
		      cs2.setString(1,"A");
		      cs2.setInt(2,2);
		      cs2.setString(3,"A");
		      sb.append("Execute SQL Statement : "+updateStatement+"\n"); 
		      sb.append(" "+"\n");
		      cs2.executeUpdate();
		      sb.append("Prepare select for update"+"\n"); 
		      cc5 = c.prepareStatement(selectForUpdateStatement);
		      cc5.setString(1,"Henning");
		      sb.append("Execute select for update"+"\n"); 
		      callRs = cc5.execute();                                  // execute the query
		      if (callRs)                                     // result set exists - process it 
		      {
			  ResultSet crs = cc5.getResultSet();
			  ResultSetMetaData crsmd = crs.getMetaData();
			  int cNumberofCols = crsmd.getColumnCount();
			  Vector<String> callVector = new Vector<String>();
			  sb.append("Result Set from Query"+"\n");
			  for (xi=1;xi<cNumberofCols;xi++)
			  {
			      colName2 = crsmd.getColumnName(xi);    // Get column name
			      colType2 = crsmd.getColumnTypeName(xi); // Get column data type
			      sb.append("Column (" + xi + ") " + colName2 + 
						 " is data type " + colType2+"\n");
			  }
			  j = 0;
			  while (crs.next())
			  {
			      for (xi=1;xi<=cNumberofCols;xi++)
			      {
				  callVector.addElement(crs.getString(xi));
				  sb.append("Column " + xi +" value : " + callVector.get(j*cNumberofCols+xi-1)+"\n");
			      }
			      j++; 
			  }
			  sb.append(" Rows Fetched : " + j +"\n");


			  j=0; 
			  cc5.close();
		      }

		      sb.append("Prepare select (no update)"+"\n"); 
		      cc5 = c.prepareStatement(selectStatement);
		      cc5.setString(1,"Henning");
		      sb.append("Execute select (no update)"+"\n");        
		      callRs = cc5.execute();                                  // execute the query
		      if (callRs)                                     // result set exists - process it 
		      {
			  ResultSet crs = cc5.getResultSet();
			  ResultSetMetaData crsmd = crs.getMetaData();
			  int cNumberofCols = crsmd.getColumnCount();
			  Vector<String> callVector = new Vector<String>();
			  sb.append("Result Set from Query"+"\n");
			  for (xi=1;xi<cNumberofCols;xi++)
			  {
			      colName2 = crsmd.getColumnName(xi);    // Get column name
			      colType2 = crsmd.getColumnTypeName(xi); // Get column data type
			      sb.append("Column (" + xi + ") " + colName2 + 
						 " is data type " + colType2+"\n");
			  }
			  j = 0;
			  while (crs.next())
			  {
			      for (xi=1;xi<=cNumberofCols;xi++)
			      {
				  callVector.addElement(crs.getString(xi));
				  sb.append("Column " + xi +" value : " + callVector.get(j*cNumberofCols+xi-1)+"\n");
			      }
			      j++; 
			  }
			  sb.append(" Rows Fetched : " + j +"\n");


			  j=0; 
			  cc5.close();
		      }


		      cs3 = c.prepareStatement(deleteStatement);
		      cs3.setString(1,"a");
		      cs3.setInt(2,3); 
		      sb.append("SQL Statement : "+deleteStatement+"\n"); 
		      sb.append(" "+"\n");
		      cs3.executeUpdate();
		  } /* end for loop */ 
		  x.execute("CALL QSYS.QCMDEXC('ENDDBMON        ',0000000010.00000)");


		  PreparedStatement ps = c.prepareStatement("select distinct count(qqrid) as count, qqrid, qqc21, qqc12, qvc18, QQ1000  from "+collection+".dbmon25 WHERE qqc21 != 'CL' group by qqrid, qqc21, qqc12, qvc18, QQ1000 order by QQ1000,qqc12,qvc18");
		  sb.append("RUN 1"+"\n"); 
		  sb.append("COUNT,QQRID,StatementOP,StatementType,DynamicSqlType,Text"+"\n"); 
		  ResultSet rs = ps.executeQuery();
		  while (rs.next()) {
		      for (i = 1; i <= 6;i++) {
			  if (i > 1) sb.append(",");
			  sb.append(rs.getString(i)); 
		      }
		      sb.append(""+"\n"); 

		      //String count=rs.getString(1);
		      //String qqrid=rs.getString(2);
		      //String statementOp=rs.getString(3);
		      //String statementType=rs.getString(4);
		      String dynamicSqlType=rs.getString(5);
		      String text = rs.getString(6);
		      if (text == null) {
		        text = "null"; 
		      }
                      /* L means local prepared statement S means system wide statement cache */ 
		      if (text.indexOf(insertStatement) >= 0) {
			  if ((!"L".equals(dynamicSqlType)) && (!"S".equals(dynamicSqlType))) {
			      sb.append("Error: RUN 1: Type="+dynamicSqlType+"!=L for "+text+"\n");
			      passed=false; 
			  } 
		      } else if (text.indexOf(deleteStatement) >= 0) {
			  if ((!"L".equals(dynamicSqlType)) && (!"S".equals(dynamicSqlType))) {
			      sb.append("Error: RUN 1: Type="+dynamicSqlType+"!=L for "+text+"\n");
			      passed=false; 
			  }
		      } else if (text.indexOf(updateStatement) >= 0) {
			  if ((!"L".equals(dynamicSqlType)) && (!"S".equals(dynamicSqlType))) {
			      sb.append("Error: RUN 1: Type="+dynamicSqlType+"!=L for "+text+"\n");
			      passed=false; 
			  }
		      } else if (text.indexOf(selectForUpdateStatement) >= 0) {
			  if ((!"L".equals(dynamicSqlType)) && (!"S".equals(dynamicSqlType))){
			      sb.append("Error: RUN 1: Type="+dynamicSqlType+"!=L for "+text+"\n");
			      passed=false; 
			  }
		      } else if (text.indexOf(selectStatement) >= 0) {
			  if ((!"L".equals(dynamicSqlType)) && (!"S".equals(dynamicSqlType))){
			      sb.append("Error: RUN 1: Type="+dynamicSqlType+"!=L for "+text+"\n");
			      passed=false; 
			  }
		      } else if (text.length() < 10) {
		      } else if (text.indexOf("CALL QSYS") >= 0)  {
		      } else if (text.indexOf("HARD CLOSE") >= 0)  {
		      } else if (text.indexOf("C1 ASC") >= 0)  {
		      } else {
			  sb.append("RUN1 : Did not recognize "+text+"\n"); 
		      }

		  } 

		  ps.close(); 
		  c.close(); 
		  
		  // Reconnect and run again.  This time, it should come from the package. 
      c = testDriver_.getConnection(dbUrl,systemObject_.getUserId(), encryptedPassword_);

      {
          String ji = ((com.ibm.as400.access.AS400JDBCConnection)c).getServerJobIdentifier();
          String jobname = ji.substring(20).trim()+"/"+ji.substring(10,20).trim()+"/"+ji.substring(0,10).trim(); 
          sb.append(" Server Job Identifier-" + ji+"\n");
          sb.append(" Server Job Name      -" + jobname+"\n");

      }

      if (is != null) {
	  System.out.println("Interactive:  press enter to continue");
	  is.readLine(); 
      } 

      x = c.createStatement(); 


      x.execute("SET SCHEMA "+collection); 
      x.execute("CALL QSYS.QCMDEXC('CHGQRYA QRYOPTLIB("+collection+")',0000000027.00000)");
      x.execute("CALL QSYS.QCMDEXC('STRDBMON  OUTFILE("+collection+"/DBMON25)           ',0000000042.00000)");
      sql = "CALL QSYS.QCMDEXC('FRCQRYI NOMASK(*YES)               ',0000000030.00000)";
      sb.append("Executing:"+sql+"\n"); 
      x.execute(sql);
      sb.append("Running " + iterations + " iterations"+"\n");
      for (i=1;i<=iterations;i++) 
      {
          sb.append("Running iteration " + i +"\n");
        //                                                               1234567890123456789012345678901234567890123456
          sb.append("Prepare SQL Statement : "+insertStatement+"\n"); 
          cs = c.prepareStatement(insertStatement); 
          cs.setInt(1,3);
          sb.append("Execute SQL Statement : "+insertStatement+"\n"); 
          sb.append(" "+"\n");
          cs.executeUpdate();
          sb.append("Prepare SQL Statement : "+updateStatement+"\n"); 
          cs2 = c.prepareStatement(updateStatement);
          cs2.setString(1,"A");
          cs2.setInt(2,2);
          cs2.setString(3,"A");
          sb.append("Execute SQL Statement : "+updateStatement+"\n"); 
          sb.append(" "+"\n");
          cs2.executeUpdate();
          sb.append("Prepare select for update"+"\n"); 
          cc5 = c.prepareStatement(selectForUpdateStatement);
          cc5.setString(1,"Henning");
          sb.append("Execute select for update"+"\n"); 
          callRs = cc5.execute();                                  // execute the query
          if (callRs)                                     // result set exists - process it 
          {
        ResultSet crs = cc5.getResultSet();
        ResultSetMetaData crsmd = crs.getMetaData();
        int cNumberofCols = crsmd.getColumnCount();
        Vector<String> callVector = new Vector<String>();
        sb.append("Result Set from Query"+"\n");
        for (xi=1;xi<cNumberofCols;xi++)
        {
            colName2 = crsmd.getColumnName(xi);    // Get column name
            colType2 = crsmd.getColumnTypeName(xi); // Get column data type
            sb.append("Column (" + xi + ") " + colName2 + 
             " is data type " + colType2+"\n");
        }
        j = 0;
        while (crs.next())
        {
            for (xi=1;xi<=cNumberofCols;xi++)
            {
          callVector.addElement(crs.getString(xi));
          sb.append("Column " + xi +" value : " + callVector.get(j*cNumberofCols+xi-1)+"\n");
            }
            j++; 
        }
        sb.append(" Rows Fetched : " + j +"\n");


        j=0; 
        cc5.close();
          }

          sb.append("Prepare select (no update)"+"\n"); 
          cc5 = c.prepareStatement(selectStatement);
          cc5.setString(1,"Henning");
          sb.append("Execute select (no update)"+"\n");        
          callRs = cc5.execute();                                  // execute the query
          if (callRs)                                     // result set exists - process it 
          {
        ResultSet crs = cc5.getResultSet();
        ResultSetMetaData crsmd = crs.getMetaData();
        int cNumberofCols = crsmd.getColumnCount();
        Vector<String> callVector = new Vector<String>();
        sb.append("Result Set from Query"+"\n");
        for (xi=1;xi<cNumberofCols;xi++)
        {
            colName2 = crsmd.getColumnName(xi);    // Get column name
            colType2 = crsmd.getColumnTypeName(xi); // Get column data type
            sb.append("Column (" + xi + ") " + colName2 + 
             " is data type " + colType2+"\n");
        }
        j = 0;
        while (crs.next())
        {
            for (xi=1;xi<=cNumberofCols;xi++)
            {
          callVector.addElement(crs.getString(xi));
          sb.append("Column " + xi +" value : " + callVector.get(j*cNumberofCols+xi-1)+"\n");
            }
            j++; 
        }
        sb.append(" Rows Fetched : " + j +"\n");


        j=0; 
        cc5.close();
          }


          cs3 = c.prepareStatement(deleteStatement);
          cs3.setString(1, "a");
          cs3.setInt(2, 3);
          sb.append("SQL Statement : " + deleteStatement+"\n");
          sb.append(" "+"\n");
          cs3.executeUpdate();
        } /* end for loop */
        x.execute("CALL QSYS.QCMDEXC('ENDDBMON        ',0000000010.00000)");

        ps = c
            .prepareStatement("select distinct count(qqrid) as count, qqrid, qqc21, qqc12, qvc18, QQ1000  from "
                + collection
                + ".dbmon25 WHERE qqc21 != 'CL' group by qqrid, qqc21, qqc12, qvc18, QQ1000 order by QQ1000,qqc12,qvc18");
        sb.append("RESULT from second RUN"+"\n");
        sb.append("COUNT,QQRID,StatementOP,StatementType,DynamicSqlType,Text\n");
        rs = ps.executeQuery();
        while (rs.next()) {
          for (i = 1; i <= 6; i++) {
            if (i > 1)
              sb.append(",");
            sb.append(rs.getString(i));
          }
          sb.append(""+"\n");

          //String count = rs.getString(1);
          //String qqrid = rs.getString(2);
          //String statementOp = rs.getString(3);
          //String statementType = rs.getString(4);
          String dynamicSqlType = rs.getString(5);
          String text = rs.getString(6);
          if (text == null) {
            text = "null";
          }
          if (text.indexOf(insertStatement) >= 0) {
            if (!"E".equals(dynamicSqlType)) {
              sb.append("Error: RUN2: Type=" + dynamicSqlType + "!=E for " + text + "\n");
              passed = false;
            }
          } else if (text.indexOf(deleteStatement) >= 0) {
            if (!"E".equals(dynamicSqlType)) {
              sb.append("Error: RUN2: Type=" + dynamicSqlType + "!=E for " + text + "\n");
              passed = false;
            }
          } else if (text.indexOf(updateStatement) >= 0) {
            if (!"E".equals(dynamicSqlType)) {
              sb.append("Error: RUN2: Type=" + dynamicSqlType + "!=E for " + text + "\n");
              passed = false;
            }
          } else if (text.indexOf(selectForUpdateStatement) >= 0) {
            if ((!"S".equals(dynamicSqlType))  && (!"L".equals(dynamicSqlType)) &&( !"E".equals(dynamicSqlType))) {
              sb.append("Error: RUN2: Type=" + dynamicSqlType + "!=(S or L or E) for " + text + "\n");
              passed = false;
            }
          } else if (text.indexOf(selectStatement) >= 0) {
            if (!"E".equals(dynamicSqlType)) {
              sb.append("Error: RUN2: Type=" + dynamicSqlType + "!=E for " + text + "\n");
              passed = false;
            }
          } else if (text.length() < 10) {
          } else if (text.indexOf("CALL QSYS") >= 0)  {
	  } else if (text.indexOf("HARD CLOSE") >= 0)  {
	  } else if (text.indexOf("C1 ASC") >= 0)  {
          } else {
            sb.append(" RUN2:Did not recognize " + text + "\n");
          }

        }

      ps.close(); 

		  
		  
		  
		  
		  
		  
	      sb.append(" Rows Fetched : " + j +"\n");
	      sb.append("Test over"+"\n");


	      // Now get another connection and make sure that the DBMON has that
	      // the statements are coming from the cache. 
	      

	      t25cleanup(dbUrl,qcustFile,ptFile);




          assertCondition(passed,sb); 

	      } catch (Exception e) {
        failed(e, "Unexpected Exception "+sb.toString());
      }

    } else { 
      notApplicable("Testcase applies only to Toolbox");
    }
  }





    void t25setup(String dbUrl, String qcustFile, String ptFile, int reuseCount, StringBuffer sb) throws Exception
    {
        String createtable = "CREATE TABLE "+collection+"."+ptFile+" (C1 CHAR(60), C2 INTEGER, C3 INTEGER)";


        String createtableas = "CREATE TABLE "+collection+"."+qcustFile+" AS (SELECT * FROM QIWS.QCUSTCDT) WITH DATA";
	String insertIntoTable = "INSERT INTO "+collection+"."+qcustFile+" SELECT * FROM QIWS.QCUSTCDT"; 

        String inifile =   "CALL QSYS.QCMDEXC('CRTDUPOBJ QAQQINI QSYS *FILE "+collection+"',0000000037.00000)";
        String iniinsert = "INSERT INTO "+collection+".QAQQINI (QQPARM,QQVAL) VALUES('SQL_STMT_REUSE'," + reuseCount + ")";
        String iniinsert2 = "INSERT INTO "+collection+".QAQQINI (QQPARM,QQVAL) VALUES('PARAMETER_MARKER_CONVERSION','*NO')";
        String drop =  "DROP TABLE "+collection+"."+ptFile+"";
	// Changed from delete to drop 
        String drop4 = "DELETE FROM "+collection+"."+qcustFile+"";

	CommandCall cc = new CommandCall(system);
	cc.run("QSYS/DLTF "+collection+"/QAQQINI"); 
        cc.run("QSYS/CHGAUT OBJ('/qsys.lib/qaqqini.file') USER(*PUBLIC) DTAAUT(*RX) OBJAUT(*OBJEXIST *OBJREF *OBJMGT) "); 

        Connection c = testDriver_.getConnection(dbUrl,systemObject_.getUserId(), encryptedPassword_);

        Statement s = c.createStatement();

        try
        {
	    sb.append(drop+"\n"); 
            s.execute(drop); 
        }
             catch (Exception e)
        {
        }
        try
        {
	    sb.append(drop+"\n"); 
         s.execute(drop4);
        } catch (Exception e)
        {
        }
     //   try
     //   {
     //    s.execute(deletepkg);
     //           } catch (Exception e)
     //           {
     //           }

        sb.append(createtable+"\n");
        s.execute(createtable);
        sb.append(createtableas+"\n");
	try { 
	    s.execute(createtableas);
	} catch (Exception e) {
	    sb.append("Create table failed.. inserting"+"\n");
	    sb.append(insertIntoTable+"\n"); 
	    s.execute(insertIntoTable ); 
	} 
        sb.append(inifile+"\n"); 
        s.execute(inifile);
        sb.append(iniinsert+"\n");
        s.execute(iniinsert);
        sb.append(iniinsert2+"\n");
        s.execute(iniinsert2);      
        //sb.append(createtable2+"\n");
        //s.execute(createtable2);
        //sb.append(sql+"\n");
        //s.execute(sql+"\n");
        c.commit();

        s.close();
        c.close();
    }

    void t25cleanup(String dbUrl,String qcustFile, String ptFile)
    {
        try
        {
            Connection c = testDriver_.getConnection(dbUrl,systemObject_.getUserId(), encryptedPassword_);

            Statement s = c.createStatement();
             s.execute("DROP TABLE "+collection+"."+ptFile+"");
             s.execute("DROP TABLE "+collection+"."+qcustFile+"");
            s.close();
            c.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }





}

