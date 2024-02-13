///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDGroupTestInfo.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/**
 * Class to get information about current running group test.
 * Is used by the JDSchedulerServer to determine if JDBC tests
 * can be started. 
 */

package test;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Random;

public class JDGroupTestInfo {

  /**
   * List of subsystem used by group test. If there are more than a certain
   * threshold of number of jobs running in these subsystems, then do not start
   * additional tests.
   */
  public static String[] groupTestSubsystems = { "ASQBATS1", "ASQBATS2", "ASQBATS3", "ASQBATS99", "BATS", "CMPLXSBS",
      "CPGRP0", "CPGRP1", "CPGRP2", "CPGRP3", "CPGRP4", "CPGRP5", "CPGRP6", "CPGRP7", "CPGRP8", "CPGRP9", "CPLCKSBS",
      "CPONESBS", "CPTSTSBS", "DBGRP1", "DBGRP2", "DBGRP3", "DBGRP4", "DBGRP5", "DBGRP6", "DBGRP7", "DBGRP8", "DBGRP9",
      "DBLCKSBS", "DBRTTR64", "DBTSTSBS", "DLFMSBS", "IASPSBSD", "KELSBS540", "KELSBS541", "NQED", "NQESBS520",
      "NQESBSEN", "NQESBSJ", "NQESBSJN", "NSTSBS520", "QQAPAR", "QQAPAR360", "QQB", "QQBSBSD", "QQIXAO", "QQLIB210",
      "QQLIB211", "QQLIB220", "QQLIB230", "QQLIB310", "QQLIB311", "QQLIB320", "QQLIB360", "QQLIB370", "QQLIB41",
      "QQLIB410", "QQLIB420", "QQLIB430", "QQLIB440", "QQLIB450", "QQLIB510", "QQLIBC2", "QQMKF", "QQPRF230",
      "QQPRFNLSS", "QQSBS1", "QQSBS210", "QQSBS211", "QQSBS220", "QQSBS230", "QQSBS230P", "QQSBS310", "QQSBS311",
      "QQSBS320", "QQSBS360", "QQSBS41", "QQSBS410", "QQSBS420", "QQSBS430", "QQSBS440", "QQSBS450", "QQSBS510",
      "QQSBS520", "QQSBS530", "QQSBSAEL", "QQSBSAPAR", "QQSBSBINT", "QQSBSC2", "QQSBSDTAB", "QQSBSESF", "QQSBSEVI",
      "QQSBSG32", "QQSBSGT32", "QQSBSHJ", "QQSBSINI", "QQSBSIXAO", "QQSBSLCHJ", "QQSBSLCP", "QQSBSMKF", "QQSBSNLSS",
      "QQSBSROJ", "QQSBSSS", "QQSBSTSP", "QQSBSUDTF", "QQSBSUNV", "QQSBSUSS", "QQSBSY2K", "QUWATCHSBS", "SQBATSBS",
      "SQBATSW", "SQBCVTW", "SQBSTPW", "SQITSBSD", "SQITSBSDB", "SQLIB520", "SQLIB540", "SQOJNSBS2", "SQPARSBS",
      "SQPARW", "SQSBS510", "SQSBS520", "SQSBSCVL", "SQSBSSQRS", "SQSBSTRG", "SQSCRSBS2", "TESTDLR", "TESTG32",
      "TESTQQ", "TESTSQPAR", "TESTSTRIG", "TESTVE", "TSLCKSBS", "TSTSTSBS",

  };

  static String[] groupTestJobs = { "SBMV7R1SM", "SN", "SOSPR", "XML", "XMLDCMP", };

  static int lastCount = 0;
  static long nextRunTime = 0L;

  private static Hashtable groupTestSubsystemsHashtable = null;
  private static Hashtable groupTestJobsHashtable = null;

  private static boolean isGroupTestJob(String currentSubsystem, String jobName) {
    //
    // Setup if needed
    //
    if (groupTestSubsystemsHashtable == null) {
      groupTestSubsystemsHashtable = new Hashtable();
      for (int i = 0; i < groupTestSubsystems.length; i++) {
        String key = groupTestSubsystems[i];
        String value = key;
        groupTestSubsystemsHashtable.put(key, value);
      }
    }

    if (groupTestJobsHashtable == null) {
      groupTestJobsHashtable = new Hashtable();
      for (int i = 0; i < groupTestJobs.length; i++) {
        String key = groupTestJobs[i];
        String value = key;
        groupTestJobsHashtable.put(key, value);
      }
    }

    if (groupTestSubsystemsHashtable.get(currentSubsystem) != null) {
      return true;
    }
    if (groupTestSubsystemsHashtable.get(jobName) != null) {
      return true;
    }
    return false;
  }

  /**
   * Returns the number of job running group tests for the specified JDBC server
   * connection. A server connection is used so that a client can detect if the
   * server is busy running group test.
   * 
   * @param c
   * @return
   */
  public static int getRunningGroupTestJobCount(Connection c, StringBuffer sb) throws SQLException {

    // Make sure only 1 thread runs this at a time.
    synchronized (groupTestSubsystems) {

      // Do not call this more than every 30 seconds.
      if (System.currentTimeMillis() < nextRunTime) {
        return lastCount;
      }

      int count = 0;

      Random random = new Random();
      String outputFile = "JDWAJ" + (10000 + random.nextInt(89999));

      Statement s = null;
      try {
        s = c.createStatement();
        try {
          s.execute("CALL QSYS2.QCMDEXC('DLTF FILE(QGPL/" + outputFile + ")  ')");
        } catch (Exception e) {

        }
        try {
          s.execute("CALL QSYS2.QCMDEXC('CRTSRCPF FILE(QGPL/" + outputFile + ") RCDLEN(162)          ')");
        } catch (Exception e) {
          System.out.println("Warning.. Unable to create QGPL/" + outputFile + "");
          e.printStackTrace();
        }
        // Run wrkactjob and place the results in dbfile
        String sql = "CALL QSYS2.QCMDEXC('" + "QSH CMD(''system wrkactjob > /qsys.lib/qgpl.lib/" + outputFile + ".file/"
            + outputFile + ".mbr'')   ')";
        // System.out.println("sql is "+sql);
        s.setQueryTimeout(120); /* limit is in seconds */
        try {
          s.execute(sql);
        } catch (Exception e) {
          System.out.println("Error running " + sql);
          e.printStackTrace();
        }

        String currentSubsystem = "";
        ResultSet rs = s.executeQuery("Select * from QGPL." + outputFile + "");
        while (rs.next()) {
          String line = rs.getString(3);
          // System.out.println("line="+line);
          if (!filteredLine(line)) {
            String possibleSubsystem = line.substring(2, 12);
            // System.out.println("sbs =" + possibleSubsystem);
            if (possibleSubsystem.charAt(0) != ' ') {
              currentSubsystem = possibleSubsystem.trim();
            } else {
              String jobName = possibleSubsystem.trim();
              if (isGroupTestJob(currentSubsystem, jobName)) {
                count++;
                sb.append("SBS=" + currentSubsystem + " JOB=" + jobName + "\n");
              }
            }
          }
        }
        // System.out.flush();
        rs.close();

        s.execute("CALL QSYS.QCMDEXC('DLTF FILE(QGPL/" + outputFile + ")       ',000000030.00000)");
        s.close();
        lastCount = count;
        nextRunTime = System.currentTimeMillis() + 30000;
        return count;
      } finally {
        if (s != null)
          s.close();
      }
    } /* synchronized */
  } /* getRunningGroupTestJobCount */

  static String[] filters = { "5770SS1 V", "5761SS1 V", "Work with Active Jobs", "Reset . . . . . . . . . ",
      "Subsystems  . . . . . . ", "CPU Percent Limit . . . ", "Response Time Limit . . ", "Sequence  . . . . . . . ",
      "Job name  . . . . . . . ", "CPU %  . . . ", "--------Elapsed---------",
      "Subsystem/Job  User        Number   User",
      /* Ignore jobs stuck in message wait */
      "MSGW",

  };

  /* Should the line from wrkactjob be filtered (i.e. ignored ) */
  private static boolean filteredLine(String line) {
    for (int i = 0; i < filters.length; i++) {
      if (line.indexOf(filters[i]) >= 0) {
        return true;
      }
    }

    return false;
  }

  public static void main(String args[]) {
    String[] drivers = { "com.ibm.as400.access.AS400JDBCDriver", "com.ibm.db2.jdbc.app.DB2Driver" };

    for (int i = 0; i < drivers.length; i++) {
      try {
        Class.forName(drivers[i]);
      } catch (Exception e) {

      }
    }
    try {
      String url = args[0];
      String userid = null;
      if (args.length >= 2) {
        userid = args[1];
      }
      String password = null;
      if (args.length >= 3) {
        password = args[2];
      }

      Connection c = DriverManager.getConnection(url, userid, password);
      StringBuffer sb = new StringBuffer();
      int jobCount = getRunningGroupTestJobCount(c, sb);

      System.out.println("Count of group test jobs = " + jobCount);
      System.out.println("Jobs are ......");
      System.out.println(sb.toString());
      c.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Usage:  java test.JDGroupTest <JDBCURL> <USERID> <PASSWORD>");
    }

  }

}
