///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSchedulerServer.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.PrintStream;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Properties;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SystemStatus;

public class JDSchedulerServer extends JDScheduler {
  static String[] nonGroupTestSystems = { "UT32P12", "SQ720", "SQ730",
      "SQ740","SQ750","SQ760" };

  private static final long ONEDAYINMILLIS = 24 * 3600000;
  private static final long CLEANUP_INTERVAL_MILLIS = 3 * 3600000;
  public static String[] serverUsageInfo = {
      "java JDScheduleServer <ID> SERVER ", };
  private static Properties iniProperties;

  public static void usage() {
    for (int i = 0; i < usageInfo.length; i++) {
      System.out.println(usageInfo[i]);
    }
  }

  public static void main(String args[]) {
    try {
	System.out.println("Starting JDSchedulerServer.java");
	System.out.println("java.home is "+System.getProperty("java.home"));

      JDReport.noExit = true;
      // Make sure toolbox does not prompt -- instead should report error
      System.setProperty("com.ibm.as400.access.AS400.guiAvailable", "false");

      Thread.currentThread().setName("JDScheduleServer-main");
      if (args.length < 2) {
        usage();
      } else {

        //
        // Before we start, make sure the CCSID is not 65535
        //
        String osName = System.getProperty("os.name");
        if ("OS/400".equals(osName)) {
          int ccsid = JDJobName.getJobCCSID();
          if (ccsid == 65535) {
            System.out.println("Warning:  CCSID is 65535, changing to 37");
            JDJobName.setIGC(37);
          }
        }

        String id = args[0];
        if (Integer.parseInt(id) == 0) {
          id = defaultId;
        }
        String operation = args[1].toUpperCase();
        if (operation.equals("SERVER")) {
          runServer(System.out, id);
        } else {
          go(args);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      usage();
    }
  }

  /* Run the server. Run each command from the queue using JDRunit */
  public static void runServer(PrintStream out, String serverId) {
    try {

      boolean groupTestSystem = true;
      setupAccess(serverId);
      long emailSentTime = 0;

      String osname = System.getProperty("os.name");
      boolean on400 = (osname.indexOf("400")) > 0;

      AS400 thisAS400 = null;

      if (on400) {
        String jobName = JDJobName.getJobName();
        out.println("JDSchedulerServer running, job name is " + jobName);
        JDJobName.setJobLogOption();
        thisAS400 = new AS400("localhost");

        InetAddress localHost = InetAddress.getLocalHost();
        String localhostname = localHost.getHostName().toUpperCase();

        JDJobName.sendProgramMessage("localhostname is " + localhostname);

        for (int i = 0; i < nonGroupTestSystems.length; i++) {
          if (localhostname.indexOf(nonGroupTestSystems[i]) >= 0) {
            groupTestSystem = false;
          }
        }

      }

      StringBuffer serverLoadHistory = new StringBuffer();
      long nextMessagePrintTime = 0L;

      boolean running = true;
      verifyTables(serverId);
      String scheduleTable = getScheduleTableName(serverId);
      String runTable = getScheduleRunName(serverId);
      Connection c = getCurrentThreadJdbcConnection();

      /* Add the currently running command to the front of the queue */
      /*
       * This will restart the test that was running when the scheduler went
       * down
       */
      {
        PreparedStatement insertScheduleStatement = c.prepareStatement(
            "INSERT INTO " + scheduleTable + " VALUES(?, ?, ?, ?)");
        Statement s = c.createStatement();
        ResultSet rs = s
            .executeQuery("Select ADDED_TS, INITIALS, ACTION from " + runTable);
        while (rs.next()) {
          Timestamp addedTs = rs.getTimestamp(1);
          String initials = rs.getString(2);
          String action = rs.getString(3);
          insertScheduleStatement.setInt(1, 1);
          insertScheduleStatement.setTimestamp(2, addedTs);
          insertScheduleStatement.setString(3, initials);
          insertScheduleStatement.setString(4, action);
          insertScheduleStatement.executeUpdate();
        }
        rs.close();
        // Cleanup the runtable since we just added these to the schedule
        s.executeUpdate("DELETE FROM " + runTable);
        s.close();
        insertScheduleStatement.close();
      }
      boolean sleepMessagePrinted = false;

      int threadCount1 = getThreadCount();
      Thread[] threads = new Thread[threadCount1];
      JDSchedulerRunnable[] runnables = new JDSchedulerRunnable[threadCount1];
      Hashtable initialsToThreadHashtable = new Hashtable();
      for (int i = 0; i < threadCount1; i++) {
        runnables[i] = new JDSchedulerRunnable(scheduleTable, runTable, out,
            serverId, i, initialsToThreadHashtable);
        threads[i] = new Thread(runnables[i]);
        threads[i].start();
      }

      boolean fullMessageSent = false;
      long cleanupTime = System.currentTimeMillis() + CLEANUP_INTERVAL_MILLIS;
      while (running) {

        try {
          //
          // Wait until the system is "NOT BUSY"
          //
          boolean busy = true;
          while (busy) {
            StringBuffer sb = new StringBuffer();
            int groupTestJobCount = 0;
            boolean repeatQuery = true;
            if (groupTestSystem) {
              while (repeatQuery) {
                try {
                  groupTestJobCount = JDGroupTestInfo
                      .getRunningGroupTestJobCount(c, sb);
                  repeatQuery = false;
                } catch (Exception e) {
                  JDJobName.sendProgramMessage("Exception " + e.toString()
                      + " running getRunningGroupTestJobCount");
                  e.printStackTrace(System.out);
                  try {
                    c.close();
                  } catch (Exception e2) {

                  }
                  String exceptionMessage = e.toString().toLowerCase();
                  if (exceptionMessage.indexOf("operation cancelled") >= 0) {
                    Thread.sleep(1000);
                  } else {
                    Thread.sleep(30000);
                  }
                  // Try to recover from SQL0901 or other exception by getting
                  // new
                  // connection
                  c = getCurrentThreadJdbcConnection();
                  System.out.println("Using connection " + c);
                }
              }
            }
            if (groupTestJobCount > 5) {
              java.util.Date nowDate = new java.util.Date();
              String message = nowDate
                  + " Waiting 5 min: group test job count is "
                  + groupTestJobCount;
              if (on400) {
                JDJobName.sendProgramMessage(message);
              }
              System.out.println(message);
              try {
                Thread.sleep(5 * 60 * 1000);
              } catch (Exception e) {
              }
            } else {
              double serverLoad = 0.0;
              while (repeatQuery) {
                try {
                  serverLoad = JDPerformanceUtil.getServerLoad(c);
                  repeatQuery = false;
                } catch (Exception e) {
                  JDJobName.sendProgramMessage(
                      "Exception " + e.toString() + " running getServerLoad");
                  e.printStackTrace(System.out);
                  try {
                    c.close();
                  } catch (Exception e2) {

                  }
                  Thread.sleep(30000);
                  // Try to recover from SQL0901 by getting new connection

                  c = getCurrentThreadJdbcConnection();
                }
              } /* repeatQuery */
              if (serverLoad > 0.50) {
                int intServerLoad = (int) (100 * serverLoad);

                serverLoadHistory.append(intServerLoad + "% ");
                if (System.currentTimeMillis() > nextMessagePrintTime) {
                  nextMessagePrintTime = System.currentTimeMillis() + 300000;
                  java.util.Date nowDate = new java.util.Date();
                  String message = nowDate + " Waiting: server load has been "
                      + serverLoadHistory.toString();
                  if (on400) {
                    JDJobName.sendProgramMessage(message);
                  }
                  System.out.println(message);
                  serverLoadHistory.setLength(0);
                }
                try {
                  Thread.sleep(1000);
                } catch (Exception e) {
                }
              } else {
                busy = false;
              } /* server load > 0.50 */
            } /* group test job count < 5 */

            /* Check to see if SQ development system */
            if (!busy) {

            }

            /* Check to see if system ASP is full */
            /* If so, stay busy */
            if (!busy) {
              if (thisAS400 != null) {
                /* Use QWCRSSTS to determine ASP Usage */
                /* Add to JDPerformanceUtil */

                /* Or use toolbox? -- SystemStatus object */
                SystemStatus systemStatus = new SystemStatus(thisAS400);
                float percentSystemASPUsed = systemStatus
                    .getPercentSystemASPUsed();
                int jobsInSystem = systemStatus.getJobsInSystem();
                if ((percentSystemASPUsed > 90.0) || (jobsInSystem > 400000)) {
                  busy = true;
                  loadIniProperties(null);

                  if (!fullMessageSent) {
                    String localHost = InetAddress.getLocalHost().getHostName()
                        .toUpperCase();
                    int dotIndex = localHost.indexOf(".");
                    if (dotIndex >= 0) {
                      localHost = localHost.substring(0, dotIndex);
                    }

                    java.util.Date nowDate = new java.util.Date();
                    String subject = nowDate + " Holding ASP at "
                        + percentSystemASPUsed + "(90) jobs=" + jobsInSystem
                        + "(400000)";
                    JDJobName.sendProgramMessage(subject);
                    fullMessageSent = true;

                    try {
                      JDJobName.sendProgramMessage("calling JDCleanCore");
                      System.out.println("calling JDCleanCore");
                      JDCleanCore.clean("localhost",
                          iniProperties.getProperty("USERID"),
                          iniProperties.getProperty("PASSWORD"), System.out);

                    } catch (Exception e) {
                      JDJobName.sendProgramMessage(
                          "calling JDCleanCore hit exception " + e);
                      System.out
                          .println("calling JDCleanCore hit exception " + e);
                      e.printStackTrace(System.out);
                    }

                    systemStatus.reset();
                    percentSystemASPUsed = systemStatus
                        .getPercentSystemASPUsed();
                    jobsInSystem = systemStatus.getJobsInSystem();
                    JDJobName.sendProgramMessage(
                        " after JDCleanCore ASP at " + percentSystemASPUsed
                            + "jobsInSystem=" + jobsInSystem);

                    try {
                      // Try to Cleanup
                      JDJobName.sendProgramMessage("calling JDCleanJrnRcv");
                      System.out.println("calling JDCleanJrnRcv");
                      JDCleanJrnRcv.clean("localhost",
                          iniProperties.getProperty("USERID"),
                          PasswordVault.getEncryptedPassword( iniProperties.getProperty("PASSWORD")), System.out);
                      systemStatus.reset();
                      percentSystemASPUsed = systemStatus
                          .getPercentSystemASPUsed();
                      jobsInSystem = systemStatus.getJobsInSystem();
                      JDJobName.sendProgramMessage(
                          " after JDCleanJrnRcv ASP at " + percentSystemASPUsed
                              + " jobsInSystem=" + jobsInSystem);
                      System.out.println(
                          " after JDCleanJrnRcv ASP at " + percentSystemASPUsed
                              + " jobsInSystem=" + jobsInSystem);

                      // If not clean, then delete spool files
                      systemStatus.reset();
                      percentSystemASPUsed = systemStatus
                          .getPercentSystemASPUsed();
                      jobsInSystem = systemStatus.getJobsInSystem();

                      JDJobName.sendProgramMessage(
                          " after JDCleanCore ASP at " + percentSystemASPUsed
                              + " jobsInSystem=" + jobsInSystem);

                      if ((percentSystemASPUsed > 80.0)
                          || (jobsInSystem > 400000)) {
                        String[] cleanArgs = { "localhost" };
                        JDJobName.sendProgramMessage(
                            "calling JDCleanSplf for testcases ");
                        try {
                          JDCleanSplf.main(cleanArgs);
                        } catch (Exception e) {
                          JDJobName.sendProgramMessage(
                              "exception in JDCleanSplf" + e);
                        }

                        systemStatus.reset();
                        percentSystemASPUsed = systemStatus
                            .getPercentSystemASPUsed();
                        jobsInSystem = systemStatus.getJobsInSystem();
                        JDJobName.sendProgramMessage(
                            "back from JDCleanSplf aspUsed = "
                                + percentSystemASPUsed + "jobsInSystem="
                                + jobsInSystem);

                        String[] cleanArgsAll = { "localhost", "null", "null",
                            "10", "ALL" };
                        JDJobName.sendProgramMessage(
                            "calling JDCleanSplf for all splf older than 10 days");
                        try {
                          JDCleanSplf.main(cleanArgsAll);
                        } catch (Exception e) {
                          JDJobName.sendProgramMessage(
                              "exception in JDCleanSplf" + e);
                        }
                        systemStatus.reset();
                        percentSystemASPUsed = systemStatus
                            .getPercentSystemASPUsed();
                        jobsInSystem = systemStatus.getJobsInSystem();
                        JDJobName.sendProgramMessage(
                            "back from JDCleanSplf aspUsed ="
                                + percentSystemASPUsed + " jobsInSystem="
                                + jobsInSystem);

                      }

                    } catch (Exception e) {
                      JDJobName.sendProgramMessage(
                          "calling cleanup hit exception " + e);
                      System.out
                          .println("calling JDCleanCore hit exception " + e);
                      e.printStackTrace(System.out);
                    }
                  }

                  // Check the ASP again and only send the e-mail if needed
                  systemStatus.reset();
                  percentSystemASPUsed = systemStatus.getPercentSystemASPUsed();
                  jobsInSystem = systemStatus.getJobsInSystem();
                  if (((percentSystemASPUsed > 90.0) || (jobsInSystem > 400000))
                      && (emailSentTime < System.currentTimeMillis()
                          - ONEDAYINMILLIS)) {
                    emailSentTime = System.currentTimeMillis();

                    String toAddress = iniProperties.getProperty("EMAIL");
                    String fromAddress = iniProperties.getProperty("EMAIL");
                    String localHost = InetAddress.getLocalHost().getHostName()
                        .toUpperCase();
                    int dotIndex = localHost.indexOf(".");
                    if (dotIndex >= 0) {
                      localHost = localHost.substring(0, dotIndex);
                    }

                    String subject = localHost + " ASP at "
                        + percentSystemASPUsed;

                    StringBuffer body = new StringBuffer();
                    body.append(subject
                        + "\n Please check the system to correct the problem");
                    JDRunit.sendEMail(toAddress, fromAddress, subject, body,iniProperties.getProperty("mail.smtp.host"));

                  } /* resend email */

                  try {
                    Thread.sleep(1000);
                  } catch (Exception e) {
                  }
                }
              } else {

              }
            }
          } /* while busy */

          if (fullMessageSent == true) {
            java.util.Date nowDate = new java.util.Date();
            String subject = nowDate + " Resuming after storage hold";
            JDJobName.sendProgramMessage(subject);
            fullMessageSent = false;
          }
          // Check to see if job cleanup should run
          if (System.currentTimeMillis() >= cleanupTime) {
            try {
              if (thisAS400 != null) {
                CleanupJavaTestJobs.cleanup("localhost", adminUserid,
                    adminPassword);

                CleanupQsqsrvrJobs.cleanup(null, adminUserid, adminPassword);
                CleanupQzdasoinitJobs.cleanup(null, adminUserid, adminPassword);
              }
            } catch (Exception e) {
              // Just ignore any errors we get
              JDJobName.sendProgramMessage(
                  "Exception caught during cleanup: " + e.toString());
              e.printStackTrace(System.out);
            }
            cleanupTime = System.currentTimeMillis() + CLEANUP_INTERVAL_MILLIS;
          }

          fullMessageSent = false;
          serverLoadHistory.setLength(0);
          nextMessagePrintTime = 0L;

          int readyThread = -1;
          boolean workAvailable = false;
          int stalledThread = 0; 
          while (readyThread == -1) {
            // Loop through all the threads, so each has
            // a chance to call go. This is needed if a
            // thread 2 is running test 7263T. No one else can
            // run it.
            for (int i = 0; i < threadCount1; i++) {
              if (runnables[i].ready()) {
                boolean thisWorkAvailable = runnables[i].go();
                if (thisWorkAvailable) {
                  workAvailable = thisWorkAvailable; 
                  sleepMessagePrinted = false;
                } else {
                  stalledThread = i; 
                }
                readyThread = i;
              }
            }
            if (readyThread == -1) {
              try {
                Thread.sleep(1000);
              } catch (Exception e) {
              }
            }
          }

          if (!workAvailable) {
            if (!sleepMessagePrinted) {
              java.util.Date nowDate = new java.util.Date();
              String message = nowDate + ": T"+stalledThread+" Queue empty: Waiting for work";
              if (on400) {
                JDJobName.sendProgramMessage(message);
              }
              System.out.println(message);
              sleepMessagePrinted = true;
            }
            /* Nothing in queue, sleep for 1 second */
            Thread.sleep(1000);
          } /* if work not available */

        } catch (Exception e) {
          String exInfo = e.toString();
          JDJobName.sendProgramMessage(
              "Warning:  Exception caught in outer loop " + exInfo);
          out.println("Warning:  Exception caught in outer loop " + exInfo);
          out.println("Warning:  Exception caught ");
          e.printStackTrace(out);
          // Look for User ID is disabled.:JAVA.
          String disabledMessage = "User ID is disabled.:";
          int disabledIndex = exInfo.indexOf(disabledMessage);
          if (disabledIndex >= 0) {
            int endIndex = exInfo.indexOf(".",
                disabledIndex + disabledMessage.length());
            String id = exInfo
                .substring(disabledIndex + disabledMessage.length(), endIndex);
            loadIniProperties(null);
            String testUserid1 = iniProperties.getProperty("TESTUSERID");
            String testPassword1 = iniProperties.getProperty("TESTPASSWORD");
            String adminUserid1 = iniProperties.getProperty("USERID");
            String adminPassword1 = iniProperties.getProperty("PASSWORD");

            if (id.equalsIgnoreCase(testUserid1)) {
              char[] encryptedAdminPassword = PasswordVault.getEncryptedPassword(adminPassword1);
              char[] encryptedTestPassword = PasswordVault.getEncryptedPassword(testPassword1);
              JDRunit.resetId(jdbcURL, adminUserid1, encryptedAdminPassword,
                  testUserid1, encryptedTestPassword);
            }

          }

          //
          // If we get an SQL0901 assume the connection is bad and get a new
          // one.
          //
          int systemErrorIndex = exInfo.indexOf("SQL system error");
          if (systemErrorIndex >= 0) {
            try {
              c.close();
            } catch (Exception e2) {

            }

            c = getCurrentThreadJdbcConnection();
          }

        }
      } /* while running */
    } catch (Throwable e) {
      JDJobName.sendProgramMessage("Error:  Server failed  " + e.toString());
      out.println("Server failed");
      e.printStackTrace(out);
      System.exit(1);
    }

  }

  public static void loadIniProperties(StringBuffer iniInfo) throws Exception {
    if (iniProperties == null) {
      try {
        iniProperties = JDRunit.getIniProperties(iniInfo);
      } catch (Exception e2) {
        System.out.println("Exception reading ini properties");
        throw e2; 
      }

    }
  }

  static String threadCountProperty = null;
  static int threadCount = 4;

  static int getThreadCount() {

    if (threadCountProperty == null) {
      try {
        threadCountProperty = System
            .getProperty("test.JDSchedulerServer.threadCount");
        if (threadCountProperty == null) {
          threadCountProperty = "" + threadCount;
        } else {
          threadCount = Integer.parseInt(threadCountProperty);
          if (threadCount <= 0) {
            threadCount = 4;
          }
        }
      } catch (Exception e) {
        threadCountProperty = "" + threadCount;
      }

    }
    return threadCount;
  }

  /* If running on IBM i, make sure the current user */
  /* has access to the objects that are being used */

  public static void setupAccess(String id) throws Exception {

    //
    // Get a connection and try some accesses.
    // If anything fails then grant permission
    //
    String connectionUser = System.getProperty("user.name");
    String sql = "";
    try {
      Connection c = getCurrentThreadJdbcConnection();
      if (userid != null)
        connectionUser = userid;
      Statement s = c.createStatement();
      ResultSet rs;
      //
      // 1. Make sure write access to schema COLLECTION exists
      //
      try {
        sql = "DROP TABLE " + COLLECTION + ".CHECKACCESS";
        s.executeUpdate(sql);
      } catch (Exception e) {
      }
      sql = "CREATE TABLE " + COLLECTION + ".CHECKACCESS (C1 INT)";
      s.executeUpdate(sql);
      sql = "DROP TABLE " + COLLECTION + ".CHECKACCESS";
      s.executeUpdate(sql);

      sql = "SELECT * FROM " + getScheduleRunName(id);
      rs = s.executeQuery(sql);
      rs.close();

      sql = "SELECT * FROM " + getScheduleTableName(id);
      rs = s.executeQuery(sql);
      rs.close();

      sql = "SELECT * FROM " + getStatisticsTableName(id);
      rs = s.executeQuery(sql);
      rs.close();

      try {
        sql = "select * from QGPL.JDWRKAJOB";
        rs = s.executeQuery(sql);
        rs.close();

        /* if it exists try to drop it. We should be able to */
        sql = "CALL QSYS.QCMDEXC('DLTF FILE(QGPL/JDWRKAJOB)       ',000000030.00000)";
        s.execute(sql);
      } catch (SQLException e2) {
        String message = e2.toString().toUpperCase();
        if (message.indexOf("NOT FOUND") >= 0) {
          // Doesn't exist -- do not worry about it
        } else {
          throw e2;
        }
      }

    } catch (Exception e) {
      System.out.println("setupAccess() exception caught: sql=" + sql);
      e.printStackTrace(System.out);
      System.out.println("Setting up access");
      String pwrUser = null;
      String pwrPass = null;

      loadIniProperties(null);

      pwrUser = iniProperties.getProperty("USERID");
      pwrPass = iniProperties.getProperty("PASSWORD");

      if ((pwrUser != null) && (pwrPass != null)) {
        try {
          Connection pwrConnection = DriverManager
              .getConnection(jdbcURL + ";prompt = false", pwrUser, pwrPass);

          Statement s = pwrConnection.createStatement();

          // Grant access to the collection
          sql = "CALL QSYS.QCMDEXC('GRTOBJAUT OBJ(" + COLLECTION
              + ") OBJTYPE(*LIB) USER(" + connectionUser
              + ") AUT(*ALL)                                                                                             ',000000100.00000)";
          System.out.println("Granting permission using : " + sql);
          s.executeUpdate(sql);

          // Grant access to all the tables.
          sql = "CALL QSYS.QCMDEXC('GRTOBJAUT OBJ(" + COLLECTION
              + "/*ALL) OBJTYPE(*FILE) USER(" + connectionUser
              + ") AUT(*ALL)                                                                                      ',000000100.00000)";
          System.out.println("Granting permission using : " + sql);
          s.executeUpdate(sql);

          sql = "CALL QSYS.QCMDEXC('GRTOBJAUT OBJ(QGPL/JDWRKAJOB) OBJTYPE(*FILE) USER("
              + connectionUser
              + ") AUT(*ALL)                                                                                      ',000000100.00000)";
          System.out.println("Granting permission using : " + sql);
          s.executeUpdate(sql);

          pwrConnection.close();
        } catch (Exception e2) {
          System.out.println("ERROR.. setupaccess failed: sql=" + sql);
          e2.printStackTrace(System.out);

        }
      } else {
        System.out.println("ERROR... USERID/PASSWORD not set in netrc.ini");
      }

    } /* catch Exception e */

  }

}
