///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASSeamlessFailover5.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//
//  File Name: AS400JDBCSeamlessFailover.java
//  Classes:   AS400JDBCSeamlessFailover
//
////////////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////////////
package test.JD.AS;


import java.io.FileOutputStream;
import java.util.Hashtable;
import java.sql.*;

import com.ibm.as400.access.AS400;


/**
 * Testcase JDASSeamlessFailover
 **/
public class JDASSeamlessFailover5 extends JDASSeamlessFailover {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDASSeamlessFailover5";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDASTest.main(newArgs); 
   }

  /**
   * Constructor.
   **/
  public JDASSeamlessFailover5(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASSeamlessFailover5", namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }


  

  /* These are the same test as in JDASClientReroute */
  /* We run them again to make sure they work with seamless */

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Blob parameters.
   **/
  public void Var049() {
    testCSTypeParametersSwitch(csBlobTransactions, csBlobParms, JAVA_BYTEARRAY);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Date parameters.
   **/
  public void Var050() {
    testCSTypeParametersSwitch(csDateTransactions, csDateParms, JAVA_DATE);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Date parameters.
   **/
  public void Var051() {
    testCSTypeParametersSwitch(csTimeTransactions, csTimeParms, JAVA_TIME);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Date parameters.
   **/
  public void Var052() {
    testCSTypeParametersSwitch(csTimestampTransactions, csTimestampParms,
        JAVA_TIMESTAMP);
  }

  public void Var053() {
    notApplicable();
  }

  public void Var054() {
    notApplicable();
  }

  public void Var055() {
    notApplicable();
  }

  public void Var056() {
    notApplicable();
  }

  public void Var057() {
    notApplicable();
  }

  public void Var058() {
    notApplicable();
  }

  public void Var059() {
    notApplicable();
  }

  public void Var060() {
    notApplicable();
  }

  /* Do coverage testing for statement objects */
  public void Var061() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {

        sb.append("**** Testing executeQuery three ways ****************\n");
        Statement stmt = autocommitConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "select * from " + table;
        sb.append("Executing: " + sql + "\n");
        ResultSet rs = stmt.executeQuery(sql);
        sb.append("Ending active connections ");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + "\n");
        rs = stmt.executeQuery(sql);
        rs.close();
        stmt.close();

        sb.append("--Using transactional connection\n");
        stmt = transactionalConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        transactionalConnection.commit();
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "select * from " + table;
        sb.append("Executing: " + sql + "\n");
        rs = stmt.executeQuery(sql);
        rs.next();
        int val = rs.getInt(1);
        if (val != 60) {
          passed = false;
          sb.append("FAILED:  expected 60 but got " + val);
        }
        rs.close();
        sb.append("Ending active connections ");
        transactionalSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          rs = stmt.executeQuery(sql);
          passed = false;
          sb.append("Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        sb.append("Executing: " + sql + "\n");
        rs = stmt.executeQuery(sql);
        stmt.close();
        transactionalConnection.commit();

        sb.append("--Using fail connection \n");
        stmt = failConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        failSocketProxyPair.resetCounts();
        sql = "select * from " + table;
        sb.append("Executing: " + sql + "\n");
        rs = stmt.executeQuery(sql);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = failSocketProxyPair.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        failSocketProxyPair.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        failSocketProxyPair.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          rs = stmt.executeQuery(sql);
          passed = false;
          sb.append("Should have exception when enable to swtich after retries");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        failSocketProxyPair.endAfterByteCount(Long.MAX_VALUE, Long.MAX_VALUE);
        sb.append("Executing final: " + sql + "\n");
        rs = stmt.executeQuery(sql);
        stmt.close();

        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    }
  }

  public void Var062() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {

        sb.append("**** Testing executeUpdate three ways ****************\n");
        Statement stmt = autocommitConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        sb.append("Ending active connections ");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        stmt.close();

        sb.append("--Using transactional connection\n");
        stmt = transactionalConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        transactionalConnection.commit();
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        sb.append("Ending active connections ");
        transactionalSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.executeUpdate(sql);
          passed = false;
          sb.append("Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        stmt.close();
        transactionalConnection.commit();

        sb.append("--Using fail connection \n");
        stmt = failConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        failSocketProxyPair.resetCounts();
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = failSocketProxyPair.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        failSocketProxyPair.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        failSocketProxyPair.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.executeUpdate(sql);
          passed = false;
          sb.append("Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        failSocketProxyPair.endAfterByteCount(Long.MAX_VALUE, Long.MAX_VALUE);
        sb.append("Executing final: " + sql + "\n");
        stmt.executeUpdate(sql);
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        stmt.close();

        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    }
  }

  public void Var063() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {

        sb.append("**** Testing execute three ways ****************\n");
        Statement stmt = autocommitConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "insert into " + table + " (C1) values(63)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sb.append("Ending active connections ");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        stmt.close();

        sb.append("--Using transactional connection\n");
        stmt = transactionalConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        transactionalConnection.commit();
        sql = "insert into " + table + " (C1) values(63)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sb.append("Ending active connections ");
        transactionalSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.execute(sql);
          passed = false;
          sb.append("Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        stmt.close();
        transactionalConnection.commit();

        sb.append("--Using fail connection \n");
        stmt = failConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "insert into " + table + " (C1) values(63)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        failSocketProxyPair.resetCounts();
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = failSocketProxyPair.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        failSocketProxyPair.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        failSocketProxyPair.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.execute(sql);
          passed = false;
          sb.append("Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        failSocketProxyPair.endAfterByteCount(Long.MAX_VALUE, Long.MAX_VALUE);
        sb.append("Executing final: " + sql + "\n");
        stmt.execute(sql);
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        stmt.close();

        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    }
  }

  public void Var064() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {

        sb.append("**** Testing close three ways ****************\n");
        Statement stmt = autocommitConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "insert into " + table + " (C1) values(64)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "select C1, 64 from " + table;
        sb.append("Executing: " + sql + "\n");
        ResultSet rs = stmt.executeQuery(sql);
        rs.next(); 
        sb.append("Ending active connections ");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + "\n");
        stmt.close();

        sb.append("--Using transactional connection\n");
        stmt = transactionalConnection.createStatement();
        Statement stmt2 = transactionalConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        transactionalConnection.commit();
        sql = "insert into " + table + " (C1) values(64)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "select C1, '64' from " + table;
        sb.append("Executing: " + sql + "\n");
        rs = stmt.executeQuery(sql);
        sql = "select C1, '64' from " + table;
        ResultSet rs2 = stmt2.executeQuery(sql);
        rs2.next(); 
        sb.append("Ending active connections ");
        transactionalSocketProxyPair.endActiveConnections();
        sb.append("Executing: close -- should have exception \n");
        try {
          stmt.close();
          passed = false;
          sb.append("Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        sb.append("Executing: " + sql + "\n");
        stmt2.close();
        transactionalConnection.commit();

        sb.append("--Using fail connection \n");
        // No way to test this scenario. 
        // After the first close fails, then any subsequent
        // closes should not noops, because there are no
        // resources yet associated with the newly reinitialize statement. 

        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
    }
  }

  public void Var065() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {

        /* Test executeUpdate(sql, Statement.RETURN_GENERATED_KEYS) */
        sb.append("**** Testing executeUpdate(sql, Statement.RETURN_GENERATED_KEYS) three ways ****************\n");
        Statement stmt = autocommitConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        sql = "insert into " + table + " (C1) values(65)";
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        sb.append("Ending active connections ");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.close();

        sb.append("--Using transactional connection\n");
        stmt = transactionalConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        transactionalConnection.commit();
        sql = "insert into " + table + " (C1) values(65)";
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        sb.append("Ending active connections ");
        transactionalSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
          passed = false;
          sb.append("FAILED:  Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("FAILED:  Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.close();
        transactionalConnection.commit();

        sb.append("--Using fail connection \n");
        stmt = failConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        sql = "insert into " + table + " (C1) values(65)";
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        failSocketProxyPair.resetCounts();
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = failSocketProxyPair.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 

        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        failSocketProxyPair.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        failSocketProxyPair.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
          passed = false;
          sb.append("FAILED: Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("FAILED: Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        failSocketProxyPair.endAfterByteCount(Long.MAX_VALUE, Long.MAX_VALUE);
        sb.append("Executing final: " + sql + "\n");
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        stmt.close();

        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }
  }

  public void Var066() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {

        /* Test executeUpdate(sql, columnIndexes) */
        int[] columnIndexes = { 2 };
        sb.append("**** Testing executeUpdate(sql, columnIndexes) three ways ****************\n");
        Statement stmt = autocommitConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnIndexes);
        sb.append("Ending active connections ");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnIndexes);
        stmt.close();

        sb.append("--Using transactional connection\n");
        stmt = transactionalConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        transactionalConnection.commit();
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnIndexes);
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnIndexes);
        sb.append("Ending active connections ");
        transactionalSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.executeUpdate(sql, columnIndexes);
          passed = false;
          sb.append("FAILED: Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("FAILED: Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnIndexes);
        stmt.close();
        transactionalConnection.commit();

        sb.append("--Using fail connection \n");
        stmt = failConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnIndexes);
        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        failSocketProxyPair.resetCounts();
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnIndexes);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = failSocketProxyPair.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        failSocketProxyPair.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        failSocketProxyPair.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.executeUpdate(sql, columnIndexes);
          passed = false;
          sb.append("FAILED: Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("FAILED: Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        failSocketProxyPair.endAfterByteCount(Long.MAX_VALUE, Long.MAX_VALUE);
        sb.append("Executing final: " + sql + "\n");
        stmt.executeUpdate(sql, columnIndexes);
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        stmt.close();

        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }
  }

  public void Var067() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {

        /* Test executeUpdate(sql, columnNames) */
        String[] columnNames = { "GENID" };
        sb.append("**** Testing executeUpdate(sql, columnNames) three ways ****************\n");
        Statement stmt = autocommitConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnNames);
        sb.append("Ending active connections ");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnNames);
        stmt.close();

        sb.append("--Using transactional connection\n");
        stmt = transactionalConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        transactionalConnection.commit();
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnNames);
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnNames);
        sb.append("Ending active connections ");
        transactionalSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.executeUpdate(sql, columnNames);
          passed = false;
          sb.append("FAILED: Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("FAILED: Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnNames);
        stmt.close();
        transactionalConnection.commit();

        sb.append("--Using fail connection \n");
        stmt = failConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnNames);
        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        failSocketProxyPair.resetCounts();
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql, columnNames);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = failSocketProxyPair.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        failSocketProxyPair.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        failSocketProxyPair.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.executeUpdate(sql, columnNames);
          passed = false;
          sb.append("FAILED: Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("FAILED: Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        failSocketProxyPair.endAfterByteCount(Long.MAX_VALUE, Long.MAX_VALUE);
        sb.append("Executing final: " + sql + "\n");
        stmt.executeUpdate(sql, columnNames);
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.executeUpdate(sql);
        stmt.close();

        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }
  }

  public void Var068() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {

        /* Test executeUpdate(sql, Statement.RETURN_GENERATED_KEYS) */
        sb.append("**** Testing executeUpdate(sql, Statement.RETURN_GENERATED_KEYS) three ways ****************\n");
        Statement stmt = autocommitConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
        sb.append("Ending active connections ");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.close();

        sb.append("--Using transactional connection\n");
        stmt = transactionalConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        transactionalConnection.commit();
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
        sb.append("Ending active connections ");
        transactionalSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
          passed = false;
          sb.append("FAILED: Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("FAILED: Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.close();
        transactionalConnection.commit();

        sb.append("--Using fail connection \n");
        stmt = failConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        failSocketProxyPair.resetCounts();
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = failSocketProxyPair.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        failSocketProxyPair.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        failSocketProxyPair.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
          passed = false;
          sb.append("FAILED: Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("FAILED: Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        failSocketProxyPair.endAfterByteCount(Long.MAX_VALUE, Long.MAX_VALUE);
        sb.append("Executing final: " + sql + "\n");
        stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        stmt.close();

        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }
  }

  public void Var069() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {

        /* Test execute(sql, columnIndexes) */
        int[] columnIndexes = { 1 };
        sb.append("**** Testing executeUpdate(sql, columnIndexes) three ways ****************\n");
        Statement stmt = autocommitConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnIndexes);
        sb.append("Ending active connections ");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnIndexes);
        stmt.close();

        sb.append("--Using transactional connection\n");
        stmt = transactionalConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        transactionalConnection.commit();
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnIndexes);
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnIndexes);
        sb.append("Ending active connections ");
        transactionalSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.execute(sql, columnIndexes);
          passed = false;
          sb.append("FAILED: Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("FAILED: Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnIndexes);
        stmt.close();
        transactionalConnection.commit();

        sb.append("--Using fail connection \n");
        stmt = failConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnIndexes);
        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        failSocketProxyPair.resetCounts();
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnIndexes);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = failSocketProxyPair.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        failSocketProxyPair.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        failSocketProxyPair.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.execute(sql, columnIndexes);
          passed = false;
          sb.append("FAILED: Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("FAILED: Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        failSocketProxyPair.endAfterByteCount(Long.MAX_VALUE, Long.MAX_VALUE);
        sb.append("Executing final: " + sql + "\n");
        stmt.execute(sql, columnIndexes);
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        stmt.close();

        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }
  }

  public void Var070() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {

        /* Test execute(sql, columnNames) */
        String[] columnNames = { "GENID" };
        sb.append("**** Testing executeUpdate(sql, columnNames) three ways ****************\n");
        Statement stmt = autocommitConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnNames);
        sb.append("Ending active connections ");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnNames);
        stmt.close();

        sb.append("--Using transactional connection\n");
        stmt = transactionalConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        transactionalConnection.commit();
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnNames);
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnNames);
        sb.append("Ending active connections ");
        transactionalSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.execute(sql, columnNames);
          passed = false;
          sb.append("FAILED: Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("FAILED: Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnNames);
        stmt.close();
        transactionalConnection.commit();

        sb.append("--Using fail connection \n");
        stmt = failConnection.createStatement();
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        sql = "insert into " + table + " (C1) values(60)";
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnNames);
        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        failSocketProxyPair.resetCounts();
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql, columnNames);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = failSocketProxyPair.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        failSocketProxyPair.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        failSocketProxyPair.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        failSocketProxyPair.endActiveConnections();
        sb.append("Executing: " + sql + " -- should have exception \n");
        try {
          stmt.execute(sql, columnNames);
          passed = false;
          sb.append("FAILED: Should have exception when switching connection");
        } catch (SQLException sqlex) {
          int sqlCode = sqlex.getErrorCode();
          if (sqlCode != -4498) {
            passed = false;
            sb.append("FAILED: Did not get error code of -4498\n");
            printStackTraceToStringBuffer(sqlex, sb);
          }
        }
        failSocketProxyPair.endAfterByteCount(Long.MAX_VALUE, Long.MAX_VALUE);
        sb.append("Executing final: " + sql + "\n");
        stmt.execute(sql, columnNames);
        sql = "delete from " + table;
        sb.append("Executing: " + sql + "\n");
        stmt.execute(sql);
        stmt.close();

        /* Other methods tested by ..... */
        /* i.e. some regression test with seamless failover */

        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }
  }
  
  // Test that parameters are preserve when a switch occurs. 
  public void Var071() {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {
        sql = "SELECT CAST(? AS INTEGER), CAST(? AS SMALLINT), CAST(? as BIGINT)," +
        		" CAST(? AS DECIMAL(12,2)), CAST(? AS NUMERIC (12,2)), CAST(? AS DECFLOAT)," +
        		" CAST(? AS DOUBLE), CAST(? AS REAL), CAST(? AS FLOAT) from sysibm.sysdummy1"; 
        sb.append("Preparing "+sql+"\n"); 
        PreparedStatement pstmt = autocommitConnection.prepareStatement(sql); 
        pstmt.setInt(1, 1); 
        pstmt.setInt(2, 2); 
        pstmt.setInt(3, 3); 
        pstmt.setString(4, "4.44"); 
        pstmt.setString(5,"5.55"); 
        pstmt.setString(6, "6.66"); 
        pstmt.setDouble(7, 7.0); 
        pstmt.setDouble(8, 8.0); 
        pstmt.setDouble(9, 9.0); 
        sb.append("Running query\n"); 
        ResultSet rs = pstmt.executeQuery(); 
        String expected = "1,2,3,4.44,5.55,6.66,7.0,8.0,9.0";
        passed = passed & checkResultSet(rs, expected, sb); 
        sb.append("Ending active connections \n");
        autocommitSocketProxyPair.endActiveConnections();
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb); 
        pstmt.close(); 
        
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }    
    
  }

  
  // Test that parameters are preserved when a switch occurs. 
  public void Var072() {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {
        sql = "SELECT CAST(? AS TIME), CAST(? AS DATE), CAST(? AS TIMESTAMP)," +
            " CAST(? AS CHAR(2)), CAST(? AS VARCHAR(10)), CAST(? AS CLOB(100)), "+
            " CAST(? AS GRAPHIC(2) CCSID 1200), CAST(? AS VARGRAPHIC(10) CCSID 1200)," +
            " CAST(? AS DBCLOB(100) CCSID 1200) " +
        		"  from sysibm.sysdummy1"; 
        sb.append("Preparing "+sql+"\n"); 
        PreparedStatement pstmt = autocommitConnection.prepareStatement(sql); 
        pstmt.setString(1, "11:11:11");
        pstmt.setString(2, "2022-02-02");
        pstmt.setString(3, "2033-03-03 03:33:33.333333"); 
        pstmt.setString(4, "44"); 
        pstmt.setString(5, "5"); 
        pstmt.setString(6, "6"); 
        pstmt.setString(7,"77"); 
        pstmt.setString(8,"8"); 
        pstmt.setString(9,"9");
        ResultSet rs = pstmt.executeQuery(); 
        String expected = "11:11:11,2022-02-02,2033-03-03 03:33:33.333333,44,5,6,77,8,9";
        passed = passed & checkResultSet(rs, expected, sb); 
        sb.append("Ending active connections \n");
        autocommitSocketProxyPair.endActiveConnections();
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb); 
        pstmt.close(); 
        
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }    
    
  }

  
  /* Test large CLOBS  */ 
  // Test that parameters are preserve when a switch occurs. 
  public void Var073() {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {
        sql = "SELECT CAST(? AS CLOB(1M))  from sysibm.sysdummy1"; 
        sb.append("Preparing "+sql+"\n"); 
        PreparedStatement pstmt = autocommitConnection.prepareStatement(sql); 
        pstmt.setString(1, "1234567890");
        sb.append("Running query\n"); 
        ResultSet rs = pstmt.executeQuery(); 
        String expected = "1234567890"; 
        passed = passed & checkResultSet(rs, expected, sb);
        sb.append("Running query again before ending connection \n"); 
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb);
        
        sb.append("Ending active connections \n");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Running query again\n"); 
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb); 
        pstmt.close(); 
        
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }    
    
  }

  /* Test large DBCLOBS CCSID 13488  */ 
  // Test that parameters are preserve when a switch occurs. 
  public void Var074() {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {
        sql = "SELECT CAST(? AS DBCLOB(1M) CCSID 13488)  from sysibm.sysdummy1"; 
        sb.append("Preparing "+sql+"\n"); 
        PreparedStatement pstmt = autocommitConnection.prepareStatement(sql); 
        pstmt.setString(1, "1234567890");
        sb.append("Running query\n"); 
        ResultSet rs = pstmt.executeQuery(); 
        String expected = "1234567890"; 
        passed = passed & checkResultSet(rs, expected, sb);
        sb.append("Running query again before ending connection \n"); 
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb);
        
        sb.append("Ending active connections \n");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Running query again\n"); 
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb); 
        pstmt.close(); 
        
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }    
    
  }
  

  /* Test large NCLOBS CCSID 13488  */ 
  // Test that parameters are preserve when a switch occurs. 
  public void Var075() {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {
        sql = "SELECT CAST(? AS NCLOB(1M) )  from sysibm.sysdummy1"; 
        sb.append("Preparing "+sql+"\n"); 
        PreparedStatement pstmt = autocommitConnection.prepareStatement(sql); 
        pstmt.setString(1, "1234567890");
        sb.append("Running query\n"); 
        ResultSet rs = pstmt.executeQuery(); 
        String expected = "1234567890"; 
        passed = passed & checkResultSet(rs, expected, sb);
        sb.append("Running query again before ending connection \n"); 
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb);
        
        sb.append("Ending active connections \n");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Running query again\n"); 
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb); 
        pstmt.close(); 
        
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }    
    
  }
  
  /* Test large XML  */ 
  // Test that parameters are preserved when a switch occurs. 
  public void Var076() {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {
        sql = "SELECT CAST(? AS XML)  from sysibm.sysdummy1"; 
        sb.append("Preparing "+sql+"\n"); 
        PreparedStatement pstmt = autocommitConnection.prepareStatement(sql); 
        pstmt.setString(1, "<a>b</a>");
        sb.append("Running query\n"); 
        ResultSet rs = pstmt.executeQuery(); 
        String expected = "<a>b</a>"; 
        passed = passed & checkResultSet(rs, expected, sb);
        sb.append("Running query again before ending connection \n"); 
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb);
        
        sb.append("Ending active connections \n");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Running query again\n"); 
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb); 
        pstmt.close(); 
        
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }    
    
  }
  


  // Test that parameters are preserved when a switch occurs. 
  public void Var077() {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {
        sql = "SELECT CAST(? AS BINARY(2)), CAST(? AS VARBINARY(2)), CAST(? AS BLOB(100)) " +
            "  from sysibm.sysdummy1"; 
        sb.append("Preparing "+sql+"\n"); 
        PreparedStatement pstmt = autocommitConnection.prepareStatement(sql); 
        pstmt.setString(1, "ebe7");
        pstmt.setString(2, "ebe7");
        pstmt.setString(3, "ebe7"); 
        ResultSet rs = pstmt.executeQuery(); 
        String expected = "EBE7,EBE7,EBE7";
        passed = passed & checkResultSet(rs, expected, sb); 
        sb.append("Ending active connections \n");
        autocommitSocketProxyPair.endActiveConnections();
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb); 
        pstmt.close(); 
        
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }    
    
  }

  /* Test large blob  */ 
  // Test that parameters are preserved when a switch occurs. 
  public void Var078() {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql;
      try {
        sql = "SELECT CAST(? AS BLOB(250M) )  from sysibm.sysdummy1"; 
        sb.append("Preparing "+sql+"\n"); 
        PreparedStatement pstmt = autocommitConnection.prepareStatement(sql); 
        pstmt.setString(1, "ebe7");
        sb.append("Running query\n"); 
        ResultSet rs = pstmt.executeQuery(); 
        String expected = "EBE7"; 
        passed = passed & checkResultSet(rs, expected, sb);
        sb.append("Running query again before ending connection \n"); 
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb);
        
        sb.append("Ending active connections \n");
        autocommitSocketProxyPair.endActiveConnections();
        sb.append("Running query again\n"); 
        rs = pstmt.executeQuery(); 
        passed = passed & checkResultSet(rs, expected, sb); 
        pstmt.close(); 
        
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }    
    
  }
  
  
  
  
  
}
