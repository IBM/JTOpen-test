///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSchedulerRunnable.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/**
 * Thread to run a testcase
 */
package test;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Set;

import test.JTA.JTACleanupTx;

public class JDSchedulerRunnable implements Runnable {

  PrintStream out;
  String serverId;
  boolean on400 = false;
  private int threadId;
  Hashtable<String, JDSchedulerRunnable> initialsToThreadHashtable;
  String runningInitials = "";
  String scheduleTable;
  String runTable;
  boolean runOneTest = false;
  boolean running = false;
  String initials = "TBD";
  String action = "TBD";
  String outputFile = "TBD";
  String priority = "TBD";
  String selectQuery = "TBD"; 
  Timestamp addedTs = null;
  int requestCount = 0;
  boolean debug = false;

  Connection baseConnection = null;
  private PreparedStatement queryInitialsStatement;
  String queryInitialsSql="";

  public JDSchedulerRunnable(String scheduleTable, String runTable,
      PrintStream out, String serverId, int threadId,
      Hashtable<String, JDSchedulerRunnable> initialsToThreadHashtable)

  throws Exception {
    this.out = out;
    this.serverId = serverId;
    this.threadId = threadId;
    this.scheduleTable = scheduleTable;
    this.initialsToThreadHashtable = initialsToThreadHashtable;

    runningInitials = "";
    this.runTable = runTable;

    on400 = JTOpenTestEnvironment.isOS400; 

  }

  public void runNext() throws Exception {
    int failedCount = 0; ; 
    int successfulCount = 0; 

    if (initials != null) {
      long startTime = System.currentTimeMillis();
      java.util.Date d = new java.util.Date(System.currentTimeMillis());
      String info = d.toString() + " T" + threadId + " Running " + initials
          + " " + action;
      if (on400) {
        JDJobName.sendProgramMessage(info);
      }
      out.println("SELECTED BY: "+selectQuery); 
      out.println(info);

      /* Run the test and wait for the reply */
      if (action.equals("REPORT")) {
        try {
          String[] reportArgs = new String[1];
          reportArgs[0] = initials;
          JDReport.noExit = true;
          JDReport.main(reportArgs);
        } catch (Throwable e) {
          out.println("JDSchedulerRunnable:  Error on REPORT");
          e.printStackTrace(out);
        }

      } else if (action.equals("EMAIL")) {
        try {
          JDRunit.email(initials, null);
        } catch (Throwable e) {
          out.println("JDSchedulerRunnable:  Error on EMAIL");
          e.printStackTrace(out);
        }
      } else if (action.equals("REGRESSION")) {
        try {
          int newPriority = 0;
          try {
            newPriority = Integer.parseInt(priority);
          } catch (Exception e) {
          }

          newPriority = newPriority
              - JDScheduler.PRIORITY_DIFFERENCE_RERUNFAILED;
          if (newPriority < 0) {
            newPriority = 1;
          }
          JDRunit.setJDPriority(newPriority);

          JDRunit.setJDSchedulerId(serverId);
          JDRunit.runRegression(initials);
          JDRunit.setJDPriority(-1);

        } catch (Throwable e) {
          out.println("JDSchedulerRunnable:  Error on REGRESSION");
          e.printStackTrace(out);
        }

      } else if (action.equals("RERUNFAILED")) {
        try {

          int newPriority = 0;
          try {
            newPriority = Integer.parseInt(priority);
          } catch (Exception e) {
          }

          newPriority = newPriority
              - JDScheduler.PRIORITY_DIFFERENCE_RERUNFAILED;
          if (newPriority < 0) {
            newPriority = 1;
          }
          JDRunit.setJDPriority(newPriority);
          JDRunit.setJDSchedulerId(serverId);
          JDRunit.rerunFailed(initials, false);
          JDRunit.setJDPriority(-1);
        } catch (Throwable e) {
          out.println("JDSchedulerRunnable:  Error on RERUNFAILED");
          e.printStackTrace(out);
        }

      } else {
        if (action.indexOf("java") == 0) {
          try {

            String lowerLine = action.toLowerCase();
            if (lowerLine.indexOf("java test.bsoauthenticate") >= 0) {
              // BSO no longer needed
              // String[] mainArgs = new String[0];
              // BSOAuthenticate.main(mainArgs);

            } else if (lowerLine.indexOf("jtacleanuptx") >= 0) {
              String[] cleanupArgs = new String[3];
              cleanupArgs[0] = "localhost";
              cleanupArgs[1] = "ThisIsDangerous";
              cleanupArgs[2] = "all";
              // Run multiple times to make sure it is all clean
              try {
                JTACleanupTx.main(cleanupArgs);
                JTACleanupTx.main(cleanupArgs);
                JTACleanupTx.main(cleanupArgs);
                JTACleanupTx.main(cleanupArgs);
              } catch (Throwable t) {
                System.out.println("JDSchedulerRunnable:  JTACleanupTx error");
                t.printStackTrace(System.out);
              }
            } else {
              System.out
                  .println("JDSchedulerRunnable:  Error.. Did not understand "
                      + action);
            }
          } catch (Throwable e) {
            out.println("JDSchedulerRunnable:  Error on java");
            e.printStackTrace(out);
          }
        } else {
          try {
            boolean runUntilFail = false;
            String runAction = action;
            if (action.indexOf("RUF:") == 0) {
              runAction = action.substring(4).trim();
              runUntilFail = true;
            }
            JDRunit runit = new JDRunit(initials, runAction, null, outputFile);
            JDRunitGoOutput goOutput = runit.go();
            failedCount = goOutput.getFailedCount(); 
            successfulCount = goOutput.getSuccessfulCount(); 
            
            if (runUntilFail) {
              if (failedCount == 0) {
                // Reschedule the test
                int intPriority = Integer.parseInt(priority);
                if (intPriority == 0) {
                  intPriority = 20;
                }
                JDScheduler
                    .add(out, serverId, intPriority, initials, runAction);
              } else {
                String subject = "RUF:" + initials + ":" + runAction
                    + " FAILED";
                StringBuffer body = new StringBuffer();
                body.append("Run until failed: " + initials + " " + runAction
                    + " FAILED\n");
                body.append("Check the system for the failed results\n");
                body.append("\n");
                JDRunit.sendEMail(initials, subject, body);
              }
            }
          } catch (Throwable e) {
            out.println("JDSchedulerRunnable:  Error on JDRunit");
            e.printStackTrace(out);
          }
        }
      }
      String sql = "";
      try {
        Connection c = null;
        while (c == null) {
          try {
            c = JDScheduler.getCurrentThreadJdbcConnection();
          } catch (Exception e) {
            out.println("JDSchedulerRunnable:  Error getting jdbc connection");
            e.printStackTrace(out);

            try {
              Thread.sleep(300000);
            } catch (Exception e2) {
            }

          }
        }
        /* Only update statistics for a run where more than half were successfule  */ 
        if (failedCount < (successfulCount + failedCount) / 2 ) {
        synchronized (c) {
          PreparedStatement deleteRunStatement = c
              .prepareStatement("DELETE from " + runTable
                  + " WHERE ADDED_TS=? AND INITIALS=? and ACTION=? ");

          deleteRunStatement.setTimestamp(1, addedTs);
          deleteRunStatement.setString(2, initials);
          deleteRunStatement.setString(3, action);
          deleteRunStatement.executeUpdate();
          deleteRunStatement.close(); 

          /* Only update statistics for a run where more than half were successfule  */ 

          if (failedCount < (successfulCount + failedCount) / 2 ) {

          long endTime = System.currentTimeMillis();
          String statsQuery = "SELECT COUNT, AVERAGE_SECONDS, RECENT_AVERAGE_SECONDS from "
              + JDScheduler.getStatisticsTableName(serverId)
              + " WHERE INITIALS = ? and ACTION = ?";
          sql = statsQuery;
          PreparedStatement statsPs = c.prepareStatement(statsQuery);
          statsPs.setString(1, initials);
          statsPs.setString(2, action);
          int count = 0;
          double lastSeconds = (endTime - startTime) / 1000.0;
          double averageSeconds = 0.0;
          double recentAverageSeconds = 0.0;
          ResultSet statsRs = statsPs.executeQuery();
          if (statsRs.next()) {
            count = statsRs.getInt(1);
            averageSeconds = statsRs.getDouble(2);
            recentAverageSeconds = statsRs.getDouble(3);
          }
          int newCount = count + 1;
          double newAverageSeconds = (count * averageSeconds + lastSeconds)
              / newCount;
          double newRecentAverageSeconds = 0.0;
          if (count < 5) {
            newRecentAverageSeconds = newAverageSeconds;
          } else {
            newRecentAverageSeconds = ((recentAverageSeconds * 4) + lastSeconds) / 5;
          }
          statsPs.close();
          sql = "DELETE FROM " + JDScheduler.getStatisticsTableName(serverId)
              + " WHERE INITIALS = ? and ACTION = ?";
          PreparedStatement deleteStatsPs = c.prepareStatement(sql);
          deleteStatsPs.setString(1, initials);
          deleteStatsPs.setString(2, action);
          deleteStatsPs.executeUpdate();
          deleteStatsPs.close();
          sql = "INSERT INTO " + JDScheduler.getStatisticsTableName(serverId)
              + " VALUES(?,?,?,?,?,?,CURRENT TIMESTAMP)";
          PreparedStatement insertStatsPs;
          try {
            insertStatsPs = c.prepareStatement(sql);
          } catch (Exception e) {
            // Fall back and insert without timestamp
            sql = "INSERT INTO " + JDScheduler.getStatisticsTableName(serverId)
                + " VALUES(?,?,?,?,?,?)";
            insertStatsPs = c.prepareStatement(sql);

          }

          insertStatsPs.setString(1, initials);
          insertStatsPs.setString(2, action);
          insertStatsPs.setInt(3, newCount);
          insertStatsPs.setDouble(4, newAverageSeconds);
          insertStatsPs.setDouble(5, newRecentAverageSeconds);
          insertStatsPs.setDouble(6, lastSeconds);
          insertStatsPs.executeUpdate();
          insertStatsPs.close();
        } /* if enough successful */ 
        
        } /* synchronized c */
      } catch (Throwable e) {
        out.println("JDSchedulerRunnable:  Warning Status update failed sql="
            + sql);
        e.printStackTrace(out);
      }
    } /* if found */

  }

  /**/
  /* */

  public void run() {
  
    while (true) {
      try {

        synchronized (this) {
          while (!runOneTest) {
            running = false;
            /* wait 10 seconds */
            Thread.currentThread().setName(
                "JDScheduler-" + threadId + "-waiting");
            wait(10000);
          }
        }
        synchronized (this) {
          running = true;
        }
        requestCount++;
        Thread.currentThread().setName(
            "JDScheduler-" + threadId + "#" + requestCount);
        runNext();
        synchronized (this) {
          running = false;
          runOneTest = false;
        }

      } catch (Throwable e) {
        System.out.println("JDSchedulerRunnable: error in run");
        e.printStackTrace(out);
        synchronized (this) {
          running = false;
          runOneTest = false;
        }
      }
    }
  }

  synchronized public boolean ready() {
    return ((running == false) && (runOneTest == false));
  }


  static void addAlreadyRunningConditions(StringBuffer querySb, Object[] keys) {
      for (int i = 0; i < keys.length; i++) {
	  if (i > 0)
	      querySb.append(" AND ");
	  String keyPattern = keys[i].toString();
	  
	  if (keyPattern.length() == 5) {
	      char testType = keyPattern.charAt(4);
	      String searchPattern = keyPattern.substring(0, 2) + "%"
		+ testType;
	      querySb.append(" INITIALS NOT LIKE '" + searchPattern
			     + "' ");

	      switch (testType) {
		  case 'A':
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "B' ");
		      break;
		  case 'B':
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "A' ");
		      break;
		  case 'N':
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "K' ");
		      break;
		  case 'K':
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "N' ");
		      break;
		  case 'T':
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "U' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "H' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "I' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "Q' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "1' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "2' ");
		      break;
		  case 'U':
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "T' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "H' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "I' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "Q' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "1' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "2' ");
		      break;

		  case 'H':
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "T' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "U' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "I' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "Q' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "1' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "2' ");
		      break;

		  case 'I':
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "T' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "H' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "U' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "Q' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "1' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "2' ");
		      break;

		  case 'Q':
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "T' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "U' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "H' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "I' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "1' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "2' ");
		      break;
		  case '1':
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "T' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "U' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "H' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "I' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "Q' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "2' ");
		      break;

		  case '2':
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "T' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "U' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "H' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "I' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "1' ");
		      querySb.append(" AND INITIALS NOT LIKE '"
				     + keyPattern.substring(0, 4) + "Q' ");
		      break;

	      } /* end switch */

	  } else { /* Length is not 5 */
	      querySb
		.append(" INITIALS NOT LIKE '" + keyPattern + "' ");
	  }

      }


  }
  /* returns true if able to start work */

  synchronized public boolean go() throws Exception {

    boolean found = false;
    boolean connecting = true;
    while (connecting) {
      Connection c1 = JDScheduler.getCurrentThreadJdbcConnection();
      synchronized (c1) {
        Connection c2 = JDScheduler.getCurrentThreadJdbcConnection();
        if (c1 == c2) {

          if (c1 != baseConnection) {
	    queryInitialsSql = "select * from "
                + scheduleTable + "  where INITIALS=? "
                + JDScheduler.SCHEDULE_ORDERING + " fetch first 1 rows only";
	    if (queryInitialsStatement != null) { 
	      try { 
	         queryInitialsStatement.close(); 
	      } catch (SQLException e) {
	      }
	    }
            queryInitialsStatement = c1.prepareStatement(queryInitialsSql);

            baseConnection = c1;
          }

          connecting = false;
          priority = null;
          addedTs = null;
          initials = null;
          action = null;
	  selectQuery=""; 
          outputFile = null;

          // Look for highest priority task that superceeds current task
          // and is not running in another thread.
          // select * from JDTestINFO.SCHED4 WHERE INITIALS NOT LIKE '61%A' AND
          // INITIALS NOT LIKE '61%T' AND PRIORITY <= 5
          {
            Set<String> keyset = null;
            Object[] keys = null;
            // We need to keep this locked until we are done with the query
            synchronized (initialsToThreadHashtable) {

              keyset = initialsToThreadHashtable.keySet();
              keys = keyset.toArray();
              StringBuffer querySb = new StringBuffer();
              querySb.append("select * from " + scheduleTable);
              if (keys.length > 0) {
                querySb.append(" where PRIORITY <= 5 AND ");
		addAlreadyRunningConditions(querySb, keys);

              } /* keys.length > 0 */
              querySb.append(" " + JDScheduler.SCHEDULE_ORDERING
                  + "  fetch first 1 rows only");

              String sql = querySb.toString();
              synchronized (System.out) {
                // System.out.println("JDSchedulerRunnable: Looking using " +
                // sql);
              }
              PreparedStatement queryStatement = c1.prepareStatement(sql);

              ResultSet rs = queryStatement.executeQuery();
              if (rs.next()) {
                priority = rs.getString(1);
                addedTs = rs.getTimestamp(2);
                initials = rs.getString(3);
                action = rs.getString(4);
		selectQuery= sql; 
                outputFile = JDRunit.TMP + "/runit" + initials + ".out."
                    + JDRunit.pid + "." + JDRunit.nextRunNumber();
                initialsToThreadHashtable.remove(runningInitials);
                runningInitials = initials;

              }
              rs.close();
              queryStatement.close(); 
            } /* synchronized initialsToThreadHashtable */
          } /* Look for high priority task */

          Set<String> keyset = null;
          Object[] keys = null;
          // We need to keep this locked until the query is done
          // and the reuslts are added back.
          synchronized (initialsToThreadHashtable) {

            keyset = initialsToThreadHashtable.keySet();
            keys = keyset.toArray();

            if (initials == null) {

              // Check to see if can run with current initials;

              queryInitialsStatement.setString(1, runningInitials);
              ResultSet rs = queryInitialsStatement.executeQuery();
              if (rs.next()) {
                priority = rs.getString(1);
                addedTs = rs.getTimestamp(2);
                initials = rs.getString(3);
                action = rs.getString(4);
		selectQuery = queryInitialsSql +" ?="+runningInitials; 
                outputFile = JDRunit.TMP + "/runit" + initials + ".out."
                    + JDRunit.pid + "." + JDRunit.nextRunNumber();
              } else {
                initialsToThreadHashtable.remove(runningInitials);
                runningInitials = "";
              }
              rs.close();

              if (initials == null) {

                // Build query that looks like this.
                // select * from JDTestINFO.SCHED4 WHERE INITIALS NOT LIKE
                // '61%A'
                // AND INITIALS NOT LIKE '61%T'

                // Find a scheduled entry that is not already running
                StringBuffer querySb = new StringBuffer();
                querySb.append("select * from " + scheduleTable);
                if (keys.length > 0) {
                  querySb.append(" where ");
		  addAlreadyRunningConditions(querySb, keys);

                }
                querySb.append(" " + JDScheduler.SCHEDULE_ORDERING
                    + "  fetch first 1 rows only");

                String sql = querySb.toString();
                if (debug)
                  System.out.println("JDSchedulerRunnable: Looking using "
                      + sql);
                PreparedStatement queryStatement = c1.prepareStatement(sql);

                rs = queryStatement.executeQuery();
                if (rs.next()) {
                  priority = rs.getString(1);
                  addedTs = rs.getTimestamp(2);
                  initials = rs.getString(3);
                  action = rs.getString(4);
		  selectQuery=sql; 
                  outputFile = JDRunit.TMP + "/runit" + initials + ".out."
                      + JDRunit.pid + "." + JDRunit.nextRunNumber();

                }
                rs.close();
                queryStatement.close(); 
              }
            }
            if (initials != null) {
              initialsToThreadHashtable.put(initials, this);
              runningInitials = initials;
              PreparedStatement deleteStatement = c1
                  .prepareStatement("DELETE from " + scheduleTable
                      + " where ADDED_TS=? and INITIALS=? and ACTION=?");

              deleteStatement.setTimestamp(1, addedTs);
              deleteStatement.setString(2, initials);
              deleteStatement.setString(3, action);
              deleteStatement.executeUpdate();
              deleteStatement.close(); 

              PreparedStatement insertRunStatement = c1
                  .prepareStatement("INSERT INTO " + runTable
                      + " VALUES(?, ?, ?, ?, CURRENT TIMESTAMP, ?)");

              insertRunStatement.setString(1, priority);
              insertRunStatement.setTimestamp(2, addedTs);
              insertRunStatement.setString(3, initials);
              insertRunStatement.setString(4, action);
              insertRunStatement.setString(5, outputFile);
              insertRunStatement.executeUpdate();
              insertRunStatement.close(); 
              found = true;
              runOneTest = true;
              notify();

            } else {
              found = false;
            }
          } /* synchronized initialsToThreadHashtable */
        } else { /* c1 == c2 */
          /* try to connect again */
          connecting = true;
        } /* c1 == c2 */
      } /* synchronized */
    } /* while connecting */

    return found;

  }

}
