///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSResults.java
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
// File Name:    JDPSResults.java
//
// Classes:      JDPSResults
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

/**
 * Testcase JDPSResults. This tests the following methods of the JDBC
 * PreparedStatement class:
 * 
 * <ul>
 * <li>getResultSet()</li>
 * <li>getUpdateCount()</li>
 * <li>getMoreResults()</li>
 * </ul>
 **/
public class JDPSResults extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSResults";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }

  // Private data.
  private static String table_ = JDPSTest.COLLECTION + ".JDPSR"; // @B2C

  private Connection connection_;

  /**
   * Constructor.
   **/
  public JDPSResults(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDPSResults", namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
    table_ = JDPSTest.COLLECTION + ".JDPSR";

    Statement s = connection_.createStatement();
    s.executeUpdate(
        "CREATE TABLE " + table_ + " (NAME VARCHAR(10), ID INT, SCORE INT)");
    s.executeUpdate(
        "INSERT INTO " + table_ + " (NAME, ID) VALUES ('cnock', 1)");
    s.executeUpdate(
        "INSERT INTO " + table_ + " (NAME, ID) VALUES ('murch', 2)");
    s.executeUpdate(
        "INSERT INTO " + table_ + " (NAME, ID) VALUES ('joshvt', 3)");
    s.close();

    JDSetupProcedure.create(systemObject_,connection_,
        JDSetupProcedure.STP_RS0, supportedFeatures_, collection_);
    JDSetupProcedure.create(systemObject_,  connection_,
        JDSetupProcedure.STP_RS1, supportedFeatures_, collection_);
    JDSetupProcedure.create(systemObject_,  connection_,
        JDSetupProcedure.STP_RS3, supportedFeatures_, collection_);
    connection_.commit(); // for xa
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    Statement s = connection_.createStatement();
    s.executeUpdate("DROP TABLE " + table_);
    s.close();
    connection_.commit(); // for xa
    connection_.close();
  }

  /**
   * getResultSet() - Call on a closed statement. Should throw an exception.
   **/
  public void Var001() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ps.close();
      ResultSet rs = ps.getResultSet();
      failed("Didn't throw SQLException " + rs);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getResultSet() - Should return null when nothing has been done with the
   * statement.
   **/
  public void Var002() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ResultSet rs = ps.getResultSet();
      ps.close();
      assertCondition(rs == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return null when an update was run using
   * executeUpdate().
   **/
  public void Var003() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=2 WHERE ID > 1");
      ps.executeUpdate();
      ResultSet rs = ps.getResultSet();
      ps.close();
      assertCondition(rs == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return null when an update was run using execute().
   **/
  public void Var004() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=3 WHERE ID > 1");
      ps.execute();
      ResultSet rs = ps.getResultSet();
      ps.close();
      assertCondition(rs == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return null when a stored procedure with no result
   * sets was run using executeUpdate().
   **/
  public void Var005() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS0);
      ps.executeUpdate();
      ResultSet rs = ps.getResultSet();
      ps.close();
      assertCondition(rs == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return null when a stored procedure with no result
   * sets was run using execute().
   **/
  public void Var006() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS0);
      ps.execute();
      ResultSet rs = ps.getResultSet();
      ps.close();
      assertCondition(rs == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return the result set when a query is run using
   * executeQuery().
   **/
  public void Var007() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ps.executeQuery();
      ResultSet rs = ps.getResultSet();
      boolean success = rs.next();
      rs.close();
      ps.close();
      assertCondition(success);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return the result set when a query is run using
   * execute().
   **/
  public void Var008() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ps.execute();
      ResultSet rs = ps.getResultSet();
      boolean success = rs.next();
      rs.close();
      ps.close();
      assertCondition(success);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return the result set when a stored procedure with
   * 1 result set was run using executeQuery().
   **/
  public void Var009() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS1);
      ps.executeQuery();
      ResultSet rs = ps.getResultSet();
      boolean success = rs.next();
      rs.close();
      ps.close();
      assertCondition(success);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return the result set when a stored procedure with
   * 1 result set was run using execute().
   **/
  public void Var010() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS1);
      ps.execute();
      ResultSet rs = ps.getResultSet();
      boolean success = rs.next();
      rs.close();
      ps.close();
      assertCondition(success);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return the result set when a stored procedure with
   * 3 result sets was run using executeQuery().
   **/
  public void Var011() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS3);
      ps.executeQuery();
      ResultSet rs = ps.getResultSet();
      boolean success = rs.next();
      rs.close();
      ps.close();
      assertCondition(success);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return the result set when a stored procedure with
   * 3 result sets was run using execute().
   **/
  public void Var012() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS3);
      ps.execute();
      ResultSet rs = ps.getResultSet();
      boolean success = rs.next();
      rs.close();
      ps.close();
      assertCondition(success);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return the result set when a stored procedure with
   * 3 result sets was run using executeQuery() and getMoreResults() is used to
   * get the 2nd and 3rd result setps.
   **/
  public void Var013() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS3);
      ps.executeQuery();

      ResultSet rs1 = ps.getResultSet();
      boolean success1 = rs1.next();
      rs1.close();

      ps.getMoreResults();
      ResultSet rs2 = ps.getResultSet();
      boolean success2 = rs2.next();
      rs2.close();

      ps.getMoreResults();
      ResultSet rs3 = ps.getResultSet();
      boolean success3 = rs3.next();
      rs3.close();

      ps.getMoreResults();
      ResultSet rs4 = ps.getResultSet();
      boolean success4 = (rs4 == null);

      ps.close();
      assertCondition(success1 && success2 && success3 && success4);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return the result set when a stored procedure with
   * 3 result sets was run using executeQuery() and getMoreResults() is used to
   * get the 2nd and 3rd result setps.
   **/
  public void Var014() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS3);
      ps.execute();

      ResultSet rs1 = ps.getResultSet();
      boolean success1 = rs1.next();
      rs1.close();

      ps.getMoreResults();
      ResultSet rs2 = ps.getResultSet();
      boolean success2 = rs2.next();
      rs2.close();

      ps.getMoreResults();
      ResultSet rs3 = ps.getResultSet();
      boolean success3 = rs3.next();
      rs3.close();

      ps.getMoreResults();
      ResultSet rs4 = ps.getResultSet();
      boolean success4 = (rs4 == null);

      ps.close();
      assertCondition(success1 && success2 && success3 && success4);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Call on a closed statement. Should throw an exception.
   **/
  public void Var015() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + table_ + " (NAME) VALUES ('Smith')");
      ps.close();
      int updateCount = ps.getUpdateCount();
      failed("Didn't throw SQLException " + updateCount);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getUpdateCount() - Should return -1 when nothing has been done with the
   * statement.
   **/
  public void Var016() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + table_ + " (NAME) VALUES ('Wiedrich')");
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == -1);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return 0 when an update was run that does not
   * update any rows using executeUpdate().
   **/
  public void Var017() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 10");
      ps.executeUpdate();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return 0 when an update was run that does not
   * update any rows using execute().
   **/
  public void Var018() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 10");
      ps.execute();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return the correct count when an update was run
   * that does update rows using executeUpdate().
   **/
  public void Var019() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 1");
      ps.executeUpdate();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == 2,
          "updateCount is " + updateCount + " Should be 2");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return the correct count when an update was run
   * that does update rows using execute().
   **/
  public void Var020() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 1");
      ps.execute();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == 2,
          "updateCount is " + updateCount + " Should be 2");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return 0 when a stored procedure with no result
   * sets was run using executeUpdate().
   **/
  public void Var021() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS0);
      ps.executeUpdate();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return 0 when a stored procedure with no result
   * sets was run using execute().
   **/
  public void Var022() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS0);
      ps.execute();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return -1 when a query is run using
   * executeQuery().
   **/
  public void Var023() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ps.executeQuery();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == -1,
          "update count from SELECT is " + updateCount);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return -1 when a query is run using execute().
   **/
  public void Var024() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ps.execute();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == -1,
          "update count from SELECT is " + updateCount);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return -1 when a stored procedure with 1 result
   * set was run using executeQuery().
   **/
  public void Var025() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS1);
      ps.executeQuery();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == -1, "update count from CALL "
          + JDSetupProcedure.STP_RS1 + " is " + updateCount);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return -1 when a stored procedure with 1 result
   * set was run using execute().
   **/
  public void Var026() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS1);
      ps.execute();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == -1, "update count from CALL "
          + JDSetupProcedure.STP_RS1 + " is " + updateCount);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return -1 when a stored procedure with 3 result
   * sets was run usgin executeQuery().
   **/
  public void Var027() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS3);
      ps.executeQuery();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == -1, "update count from CALL "
          + JDSetupProcedure.STP_RS3 + " is " + updateCount);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return -1 when a stored procedure with 3 result
   * sets was run usgin execute().
   **/
  public void Var028() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS3);
      ps.execute();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == -1, "update count from CALL "
          + JDSetupProcedure.STP_RS3 + " is " + updateCount);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return -1 when an update was run that does update
   * rows using executeUpdate() and getMoreResults() is called.
   **/
  public void Var029() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 1");
      ps.executeUpdate();
      ps.getMoreResults();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == -1);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return -1 when an update was run that does update
   * rows using execute() and getMoreResults() is called.
   **/
  public void Var030() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 1");
      ps.execute();
      ps.getMoreResults();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == -1);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMoreResults() - Call on a closed statement. Should throw an exception.
   **/
  public void Var031() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS3);
      ps.close();
      boolean moreResults = ps.getMoreResults();
      failed("Didn't throw SQLException " + moreResults);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getMoreResults() - Should return false when nothing has been done with the
   * statement.
   **/
  public void Var032() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS3);
      boolean moreResults = ps.getMoreResults();
      ps.close();
      assertCondition(moreResults == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMoreResults() - Should return false when an update was run using
   * executeUpdate().
   **/
  public void Var033() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=2 WHERE ID > 1");
      ps.executeUpdate();
      boolean moreResults = ps.getMoreResults();
      ps.close();
      assertCondition(moreResults == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMoreResults() - Should return false when an update was run using
   * execute().
   **/
  public void Var034() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=3 WHERE ID > 1");
      ps.execute();
      boolean moreResults = ps.getMoreResults();
      ps.close();
      assertCondition(moreResults == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMoreResults() - Should return false when a stored procedure with no
   * result sets was run using executeUpdate().
   **/
  public void Var035() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS0);
      ps.executeUpdate();
      boolean moreResults = ps.getMoreResults();
      ps.close();
      assertCondition(moreResults == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMoreResults() - Should return false when a stored procedure with no
   * result sets was run using execute().
   **/
  public void Var036() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS0);
      ps.execute();
      boolean moreResults = ps.getMoreResults();
      ps.close();
      assertCondition(moreResults == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMoreResults() - Should return false when a query is run using
   * executeQuery().
   **/
  public void Var037() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ps.executeQuery();
      boolean moreResults = ps.getMoreResults();
      ps.close();
      assertCondition(moreResults == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMoreResults() - Should return false when a query is run using execute().
   **/
  public void Var038() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ps.execute();
      ps.getResultSet();
      boolean moreResults = ps.getMoreResults();
      ps.close();
      assertCondition(moreResults == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMoreResults() - Should return false when a stored procedure with 1
   * result set was run using executeQuery().
   **/
  public void Var039() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS1);
      ps.executeQuery();
      boolean moreResults = ps.getMoreResults();
      ps.close();
      assertCondition(moreResults == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMoreResults() - Should return false when a stored procedure with 1
   * result set was run using execute().
   **/
  public void Var040() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS1);
      ps.execute();
      boolean moreResults = ps.getMoreResults();
      ps.close();
      assertCondition(moreResults == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMoreResults() - Should return true when a stored procedure with 3 result
   * sets was run using executeQuery(), until the last result set has been
   * retrieved.
   **/
  public void Var041() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS3);
      ps.executeQuery();
      int count = 0;
      while (ps.getMoreResults() == true)
        ++count;
      ps.close();
      assertCondition(count == 2);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getMoreResults() - Should return true when a stored procedure with 3 result
   * sets was run using execute(), until the last result set has been retrieved.
   **/
  public void Var042() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS3);
      ps.execute();
      int count = 0;
      while (ps.getMoreResults() == true)
        ++count;
      ps.close();
      assertCondition(count == 2);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getResultSet() - Should return the result set when a query is run using
   * executeQuery(). Repeated multiple times.
   **/
  public void Var043() {
    try {
      ResultSet rs;
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ps.executeQuery();
      rs = ps.getResultSet();
      boolean success1 = rs.next();
      ps.getMoreResults();
      rs.close();

      ps.executeQuery();
      rs = ps.getResultSet();
      boolean success2 = rs.next();
      ps.getMoreResults();
      rs.close();

      ps.executeQuery();
      rs = ps.getResultSet();
      boolean success3 = rs.next();
      ps.getMoreResults();
      rs.close();

      ps.close();
      assertCondition(success1 && success2 && success3,
          "success1=" + success1 + "success2=" + success2 + "success3="
              + success3 + "Added by native driver 08/03/05");
    } catch (Exception e) {
      failed(e, "Unexpected Exception -- Added by native driver 08/03/05");
    }
  }

  /**
   * getResultSet() - Should return the result set when a stored procedure with
   * 3 result sets was run using executeQuery() and getMoreResults() is used to
   * get the 2nd and 3rd result setps. Should work if the process is repeated.
   **/
  public void Var044() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS3);
      ps.executeQuery();

      ResultSet rs1 = ps.getResultSet();
      boolean success1 = rs1.next();
      rs1.close();

      ps.getMoreResults();
      ResultSet rs2 = ps.getResultSet();
      boolean success2 = rs2.next();
      rs2.close();

      ps.getMoreResults();
      ResultSet rs3 = ps.getResultSet();
      boolean success3 = rs3.next();
      rs3.close();

      ps.getMoreResults();
      ResultSet rs4 = ps.getResultSet();
      boolean success4 = (rs4 == null);

      ps.executeQuery();

      ResultSet rs5 = ps.getResultSet();
      boolean success5 = rs5.next();
      rs5.close();

      ps.getMoreResults();
      ResultSet rs6 = ps.getResultSet();
      boolean success6 = rs6.next();
      rs6.close();

      ps.getMoreResults();
      ResultSet rs7 = ps.getResultSet();
      boolean success7 = rs7.next();
      rs7.close();

      ps.getMoreResults();
      ResultSet rs8 = ps.getResultSet();
      boolean success8 = (rs8 == null);

      ps.close();
      assertCondition(
          success1 && success2 && success3 && success4 && success5 && success6
              && success7 && success8,
          "success1=" + success1 + "success2=" + success2 + "success3="
              + success3 + "success4=" + success4 + "success5=" + success5
              + "success6=" + success6 + "success7=" + success7 + "success8="
              + success8 + "-- added by native Driver 06/08/2005");
    } catch (Exception e) {
      failed(e, "Unexpected Exception -- added by native Driver 06/08/2005");
    }
  }

  /**
   * getMoreResults() - Should return false when an update was run using
   * executeUpdate(). Should be able to repeat the process.
   **/
  public void Var045() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=2 WHERE ID > 1");
      ps.executeUpdate();
      boolean moreResults1 = ps.getMoreResults();

      ps.executeUpdate();
      boolean moreResults2 = ps.getMoreResults();

      ps.close();
      assertCondition(moreResults1 == false && moreResults2 == false,
          "Added by native driver 08/03/05 TC fixed by native driver 08/10/06");
    } catch (Exception e) {
      failed(e, "Unexpected Exception -- Added by native driver 08/03/05");
    }
  }

  /**
   * getMoreResults() - Should return false when an update was run using
   * execute(). And the process is repeated.
   **/
  public void Var046() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=3 WHERE ID > 1");
      ps.execute();
      boolean moreResults1 = ps.getMoreResults();

      ps.execute();
      boolean moreResults2 = ps.getMoreResults();

      ps.close();
      assertCondition(moreResults1 == false && moreResults2 == false,
          "Added by native driver 08/03/05");
    } catch (Exception e) {
      failed(e, "Unexpected Exception Added by native driver 08/03/05");
    }
  }

  /**
   * getMoreResults() - Should return false when a stored procedure with no
   * result sets was run using executeUpdate(). The process should be repeatable
   **/
  public void Var047() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS0);
      ps.executeUpdate();
      boolean moreResults1 = ps.getMoreResults();

      ps.executeUpdate();
      boolean moreResults2 = ps.getMoreResults();

      assertCondition(moreResults1 == false && moreResults2 == false,
          "Added by native driver 08/03/05");
      ps.close();

    } catch (Exception e) {
      failed(e, "Unexpected Exception Added by native driver 08/03/05");
    }
  }

  /**
   * getMoreResults() - Should return false when a stored procedure with no
   * result sets was run using execute(). Process should be repeatable
   **/
  public void Var048() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS0);
      ps.execute();
      boolean moreResults1 = ps.getMoreResults();

      ps.execute();
      boolean moreResults2 = ps.getMoreResults();

      ps.close();
      assertCondition(moreResults1 == false && moreResults2 == false,
          "Added by native driver 08/03/05");
    } catch (Exception e) {
      failed(e, "Unexpected Exception Added by native driver 08/03/05");
    }
  }

  /**
   * getMoreResults() - Should return false when a query is run using
   * executeQuery(). And should be repeatable
   **/
  public void Var049() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ps.executeQuery();
      boolean moreResults1 = ps.getMoreResults();

      ps.executeQuery();
      boolean moreResults2 = ps.getMoreResults();

      ps.close();
      assertCondition(moreResults1 == false && moreResults2 == false,
          "Added by native driver 08/03/05");
    } catch (Exception e) {
      failed(e, "Unexpected Exception  Added by native driver 08/03/05");
    }
  }

  /**
   * getMoreResults() - Should return false when a query is run using execute().
   * -- should be repeatable
   **/
  public void Var050() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ps.execute();
      ResultSet rs = ps.getResultSet();
      boolean rsOk1 = (rs != null);
      boolean moreResults1 = ps.getMoreResults();

      ps.execute();
      ResultSet rs2 = ps.getResultSet();
      boolean rsOk2 = (rs2 != null);
      boolean moreResults2 = ps.getMoreResults();

      ps.execute();
      ResultSet rs3 = ps.getResultSet();
      boolean rsOk3 = (rs3 != null);
      boolean moreResults3 = ps.getMoreResults();

      ps.close();
      assertCondition(
          rsOk1 && moreResults1 == false && rsOk2 && moreResults2 == false
              && rsOk3 && moreResults3 == false,
          "rsOk1=" + rsOk1 + " moreResults1=" + moreResults1 + "rsOk2=" + rsOk2
              + " moreResults2=" + moreResults2 + "rsOk3=" + rsOk3
              + " moreResults3=" + moreResults3
              + " Added by native driver 08/03/05");
    } catch (Exception e) {
      failed(e, "Unexpected Exception -- Added by native driver 08/03/05");
    }
  }

  /**
   * getMoreResults() - Should return false when a stored procedure with 1
   * result set was run using executeQuery().
   * 
   **/
  public void Var051() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS1);
      ps.executeQuery();
      boolean moreResults1 = ps.getMoreResults();

      ps.executeQuery();
      boolean moreResults2 = ps.getMoreResults();

      ps.close();
      assertCondition(moreResults1 == false && moreResults2 == false,
          "Added by native driver 08/03/05");
    } catch (Exception e) {
      failed(e, "Unexpected Exception Added by native driver 08/03/05");
    }
  }

  /**
   * getMoreResults() - Should return false when a stored procedure with 1
   * result set was run using execute(). should be repeatable
   **/
  public void Var052() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS1);
      ps.execute();
      boolean moreResults1 = ps.getMoreResults();

      ps.execute();
      boolean moreResults2 = ps.getMoreResults();

      ps.execute();
      boolean moreResults3 = ps.getMoreResults();

      ps.close();
      assertCondition(moreResults1 == false && moreResults2 == false
          && moreResults3 == false, "Added by native driver 08/03/05");
    } catch (Exception e) {
      failed(e, "Unexpected Exception Added by native driver 08/03/05");
    }
  }

  /**
   * getMoreResults() - Should return true when a stored procedure with 3 result
   * sets was run using executeQuery(), until the last result set has been
   * retrieved. Should be repeatable
   **/
  public void Var053() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("CALL " + JDSetupProcedure.STP_RS3);
      ps.executeQuery();
      int count = 0;
      while (ps.getMoreResults() == true)
        ++count;

      ps.executeQuery();
      while (ps.getMoreResults() == true)
        ++count;

      ps.executeQuery();
      while (ps.getMoreResults() == true)
        ++count;

      ps.close();
      assertCondition(count == 6, "Added by native driver 08/03/05");
    } catch (Exception e) {
      failed(e, "Unexpected Exception Added by native driver 08/03/05");
    }
  }

  /**
   * getResultSet() - Should return the result set when a query is run using
   * executeQuery(). Repeated multiple times. This flavor reads the result set
   * and closes it before calling rs.next()
   **/
  public void Var054() {
    try {
      ResultSet rs;
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ps.execute();
      rs = ps.getResultSet();
      boolean success1 = rs.next();
      if (success1) {
        while (rs.next())
          ;
      }
      rs.close();
      ps.getMoreResults();

      ps.execute();
      rs = ps.getResultSet();
      boolean success2 = rs.next();
      if (success2) {
        while (rs.next())
          ;
      }
      rs.close();
      ps.getMoreResults();

      ps.execute();
      rs = ps.getResultSet();
      boolean success3 = rs.next();
      if (success3) {
        while (rs.next())
          ;
      }
      rs.close();
      ps.getMoreResults();

      ps.close();
      assertCondition(success1 && success2 && success3,
          "success1=" + success1 + "success2=" + success2 + "success3="
              + success3 + "Added by native driver 07/18/06");
    } catch (Exception e) {
      failed(e, "Unexpected Exception -- Added by native driver 07/18/06");
    }
  }

  /**
   * getResultSet() - Should return the result set when a query is run using
   * executeQuery(). Repeated multiple times. This flavor reads the result set
   * and closes it before calling rs.next(). This also retrieves the column by
   * name.
   **/
  public void Var055() {
    try {
      ResultSet rs;
      PreparedStatement ps = connection_
          .prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
      ps.execute();
      rs = ps.getResultSet();
      boolean success1 = rs.next();
      if (success1) {
        while (rs.next()) {
          rs.getString("CUSNUM");
        }
      }
      rs.close();
      ps.getMoreResults();

      ps.execute();
      rs = ps.getResultSet();
      boolean success2 = rs.next();
      if (success2) {
        while (rs.next()) {
          rs.getString("CUSNUM");
        }
      }
      rs.close();
      ps.getMoreResults();

      ps.execute();
      rs = ps.getResultSet();
      boolean success3 = rs.next();
      if (success3) {
        while (rs.next()) {
          rs.getString("CUSNUM");
        }
      }
      rs.close();
      ps.getMoreResults();

      ps.close();
      assertCondition(success1 && success2 && success3,
          "success1=" + success1 + "success2=" + success2 + "success3="
              + success3 + "Added by native driver 08/07/06");
    } catch (Exception e) {
      failed(e, "Unexpected Exception -- Added by native driver 08/07/06");
    }
  }

  /**
   * getMoreResults() - Should return false when no more results are available.
   * Should also not cause a subsequent execute to fail when used in conjunction
   * with rollback. For CPS 896M2P
   **/
  public void Var056() {
    String added = " -- added 3/24/2011 by native driver for CPS896M2P. getMoreResults should also not cause a subsequent execute to fail when used in conjunction with rollback.   ";
    try {
      connection_.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
      PreparedStatement ps = connection_
          .prepareStatement("select * from sysibm.sysdummy1");
      ResultSet rs = ps.executeQuery();
      connection_.rollback();
      boolean moreResults = ps.getMoreResults();
      rs = ps.executeQuery();
      ps.close();
      assertCondition(moreResults == false,
          "moreResults=" + moreResults + " sb false rs=" + rs + " " + added);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + added);
    }
  }

  /**
   * getMoreResults() - Should return false when no more results are available.
   * Should also not cause a subsequent execute to fail when used in conjunction
   * with commit. For CPS 896M2P
   **/
  public void Var057() {
    String added = " -- added 3/24/2011 by native driver for CPS896M2P";
    try {
      connection_.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
      PreparedStatement ps = connection_
          .prepareStatement("select * from sysibm.sysdummy1");
      ResultSet rs = ps.executeQuery();
      connection_.commit();
      boolean moreResults = ps.getMoreResults();
      rs = ps.executeQuery();
      ps.close();
      assertCondition(moreResults == false,
          "moreResults=" + moreResults + " sb false rs=" + rs + " " + added);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + added);
    }
  }

  /**
   * getUpdateCount() - Should return the correct count when an update was run
   * that does update rows using executeUpdate() and autogenerated keys are
   * being used.
   **/
  public void Var058() {
    if (checkJdbc30()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "UPDATE " + table_ + " SET SCORE=7 WHERE ID > 1",
            Statement.RETURN_GENERATED_KEYS);
        ps.executeUpdate();
        int updateCount = ps.getUpdateCount();
        ps.close();
        assertCondition(updateCount == 2,
            "updateCount is " + updateCount + " Should be 2");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUpdateCount() - Should return the correct count when an update was run
   * that does update rows using executeUpdate().
   **/
  public void Var059() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 1");
      ps.executeUpdate();
      int updateCount = ps.getUpdateCount();
      ps.close();
      assertCondition(updateCount == 2,
          "updateCount is " + updateCount + " Should be 2");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUpdateCount() - Should return the correct count when create table as
   * select is run. See issue 46717. Depends on the following SQ PTFs. 7.1
   * SI44489 SI44490 6.1 SI44487 SI44488
   **/
  public void Var060() {
    if (checkRelease610()) {
      String added = " -- getUpdateCount from create table from select added 11/7/2011\nSee issue 46717.  Depends on the following SQ PTFs.\n"
          + "7.1  SI44489 SI44490\n" + "6.1  SI44487 SI44488\n";
      try {
        Statement s = connection_.createStatement();
        String tableFrom = JDPSTest.COLLECTION + ".JDPSRFROM";
        String tableTo = JDPSTest.COLLECTION + ".JDPSRTO";
        try {
          s.executeUpdate("DROP TABLE " + tableFrom);
        } catch (Exception e) {
        }
        try {
          s.executeUpdate("DROP TABLE " + tableTo);
        } catch (Exception e) {
        }

        s.executeUpdate("Create TABLE " + tableFrom + "(c1 int)");
        s.executeUpdate("insert into " + tableFrom + " values(1)");
        s.executeUpdate("insert into " + tableFrom + " values(2)");

        PreparedStatement ps = connection_.prepareStatement("create table "
            + tableTo + " as (select * from " + tableFrom + ") with data");

        ps.executeUpdate();
        int updateCount = ps.getUpdateCount();
        ps.close();

        ps = connection_.prepareStatement("DROP TABLE " + tableFrom);

        ps.executeUpdate();
        int dropUpdateCount = ps.getUpdateCount();
        ps.close();
        s.executeUpdate("DROP TABLE " + tableTo);

        s.close();

        assertCondition(updateCount == 2 && dropUpdateCount == 0,
            "updateCount=" + updateCount + " sb 2 dropUpdateCount="
                + dropUpdateCount + " sb 0 " + added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + added);
      }
    }

  }

  public void Var061() {
    StringBuffer sb = new StringBuffer();
    boolean passed = true;
    sb.append(
        "-- added 12/11/2019 to test CPS BHPGRR -- closed cursor on commit problem\n");
    try {
      sb.append("getting connection\n");
      Connection con = testDriver_
          .getConnection(baseURL_ + ";cursor hold=false", userId_, encryptedPassword_);

      con.setAutoCommit(false);
      sb.append("preparing statement\n");
      PreparedStatement ps = con.prepareStatement(
          "select * from sysibm.sqlcolumns", ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);

      // Run the priming loop
      for (int i = 0; i < 30; i++) {
        sb.append("i=" + i + " executeQuery");
        ResultSet rs = ps.executeQuery();
        rs.next();
        rs.close();
        sb.append("i=" + i + " commit");

        con.commit();
      }

      // Now do the problem sequence

      sb.append("preparing statement\n");

      ps = con.prepareStatement("select * from sysibm.sqlcolumns",
          ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY,
          ResultSet.CLOSE_CURSORS_AT_COMMIT);
      sb.append("ps.executeQuery\n");
      ResultSet rs = ps.executeQuery();
      sb.append("rs.next\n");
      rs.next();
      sb.append("con.commit\n");

      con.commit();
      sb.append("ps.getMoreResults\n");

      ps.getMoreResults();
      sb.append("rs.close\n");
      rs.close();

      sb.append("ps.executeQuery\n");
      rs = ps.executeQuery();
      rs.next();
      sb.append("con.commit\n");
      con.commit();
      sb.append("rs.close\n");
      con.close();
      assertCondition(passed, sb.toString());

    } catch (Exception e) {
      failed(e, "Unexpected Exception " + sb.toString());
    }

  }

  
    /**
   * getResultSet() - Should return the result set with a boolean parameter.
   **/
  public void Var062() {
    if (checkBooleanSupport()) {
      String sql = "";
      try {
        String procName = collection_ + ".JDPSRES62";
        String procedureCreate = "CREATE OR REPLACE PROCEDURE " + procName
            + "() RESULT SETS 1 LANGUAGE "
            + "SQL BEGIN DECLARE c1 cursor for select CAST('true' AS BOOLEAN) from sysibm.sysdummy1;"
            + " open c1; set result sets cursor c1; end";

        Statement s = connection_.createStatement();
        sql = procedureCreate;
        s.executeUpdate(sql);

        sql = "CALL " + procName + "()";
        PreparedStatement ps = connection_.prepareStatement(sql);
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();
        boolean success = rs.next();
        boolean answer = rs.getBoolean(1);
        sql = "DROP PROCEDURE "+procName;
        s.execute(sql); 
        rs.close();
        ps.close();
        s.close();
        assertCondition(success && answer,
            "rs.next returned " + success + " and answer=" + answer);
      } catch (Exception e) {
        failed(e, "Unexpected Exception sql=" + sql);
      }
    }
  }
  

      /**
   * getResultSet() - Should return the result set with a boolean parameter.
   **/
  public void Var063() {
    if (checkBooleanSupport()) {
      String sql = "";
      try {
        sql = "SELECT CAST('true' AS BOOLEAN) from sysibm.sysdummy1";
        PreparedStatement ps = connection_.prepareStatement(sql);
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();
        boolean success = rs.next();
        boolean answer = rs.getBoolean(1);
        rs.close();
        ps.close();
        assertCondition(success && answer,
            "rs.next returned " + success + " and answer=" + answer);
      } catch (Exception e) {
        failed(e, "Unexpected Exception sql=" + sql);
      }
    }
  }
  
  

  
  
  
}
