///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCleanSplfJdbc.java
// 
// Clean spools files using JDBC 
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Hashtable;

import com.ibm.as400.access.CharConverter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class JDCleanSplfJdbc {

  public static int DEFAULT_DAYS = 7;
  public static boolean verbose = false;

  public static void usage() {
    System.out.println("Usage:  java -Dverbose=true/false JDCleanSplf <system> <userid> <password> <days> <ALL>");
    System.out.println("        deletes spool files on the system older than <days>");
    System.out.println("        Only deletes spool files from testcases except if <ALL> is specified");
    System.out.println("        minimum value of days is 1");
    System.out.println("        Default days is " + DEFAULT_DAYS);
  }

  static {
    String verboseString = System.getProperty("verbose");
    if (verboseString != null && verboseString.equalsIgnoreCase("true")) {
      verbose = true;
    }
    String debugString = System.getProperty("debug");
    if (debugString != null) {
      verbose = true;
    }
  }
  public static NumberFormat numberFormat = NumberFormat.getIntegerInstance();

  public static void main(String args[]) {
    try {
      String system = "localhost";
      String userid = null;
      String password = null;
      int days = DEFAULT_DAYS;
      boolean deleteAll = false;

      if (args[0].equals("help") || args[0].equals("-help")) {
        usage();
        System.exit(1);
      }
      if (args.length >= 1) {
        system = args[0];
      }
      if (args.length >= 2) {
        userid = args[1];
        if (userid.equals("null")) {
          userid = null;
        }
      }
      if (args.length >= 3) {
        password = args[2];
        if (password.equals("null")) {
          password = null;
        }
      }

      if (args.length >= 4) {
        days = Integer.parseInt(args[3]);
        if (days == 0) {
          days = 1;
        }
        System.out.println("days is " + days);
      }

      if (args.length >= 5) {
        if ("ALL".equalsIgnoreCase(args[4])) {
          System.out.println("deleting all ");
          deleteAll = true;
        }
      }

      Hashtable<String, String> deleteUserIds = new Hashtable<String, String>();
      deleteUserIds.put("BOSSJUDF", "BOSSJUDF");
      deleteUserIds.put("JAVA", "JAVA");
      deleteUserIds.put("JAVA290", "JAVA290");
      deleteUserIds.put("JDBBDUS2N", "JDBBDUS2N");
      deleteUserIds.put("JDCON500", "JDCON500");
      deleteUserIds.put("JDCON65535", "JDCON65535");
      deleteUserIds.put("JDPWRSYS", "JDPWRSYS");
      deleteUserIds.put("JDSTJARUSR", "JDSTJARUSR");
      deleteUserIds.put("NEWTONJUDF", "NEWTONJUDF");
      deleteUserIds.put("SQLJTEST", "SQLJTEST");

      Connection conn;
      if (userid == null) {
        conn = DriverManager.getConnection("jdbc:as400:" + system);
      } else {
        conn = DriverManager.getConnection("jdbc:as400:"+system, userid, password);
      }
      /* endDateFilter is in the form. CYYMMDD */
      /* run the delete for the last year first */

      long endFilterTime = System.currentTimeMillis() - ((days + 1) * 1000L * 3600 * 24);
      long startFilterTime = endFilterTime - 1L * 365 * 24 * 3600000L;

      System.out.println("RUNNING delete (ALL=" + deleteAll + ") for last year");
      JDCleanSplfJdbcResults results = deleteSpoolFiles(System.out, conn, "", startFilterTime, endFilterTime, deleteAll,
          deleteUserIds);
      System.out.println("Done:  (last year) deleteCount is " + results.deleteCount);
      System.out.println("Done:  (last year) deleteBytes is " + numberFormat.format(results.deleteBytes));
      System.out.println("Done:  (last year) keepCount is " + results.keepCount);
      System.out.println("Done:  (last year) keepBytes is " + numberFormat.format(results.keepBytes));
      System.out.println("Done:  (last year) processedCount is " + results.processedCount);

      /* Run the delete for last 10 years */
      endFilterTime = endFilterTime - 1L * 365 * 24 * 3600000L;
      startFilterTime = endFilterTime - 10L * 365 * 24 * 3600000L;
      System.out.println("RUNNING delete for last 10 years");
      results = deleteSpoolFiles(System.out, conn, "", startFilterTime, endFilterTime, deleteAll, deleteUserIds);
      System.out.println("Done:  (10 year) deleteCount is " + results.deleteCount);
      System.out.println("Done:  (10 year) deleteBytes is " + numberFormat.format(results.deleteBytes));
      System.out.println("Done:  (10 year) keepCount is " + results.keepCount);
      System.out.println("Done:  (10 year) keepBytes is " + numberFormat.format(results.keepBytes));
      System.out.println("Done:  (10 year) processedCount is " + results.processedCount);

      if (!deleteAll) {
        startFilterTime = endFilterTime - 1L * 365 * 24 * 3600000L;
        System.out.println("Running delete of useless for last year");
        JDCleanSplfJdbcResults uselessResults = deleteUselessSpoolFiles(System.out, conn, "", startFilterTime,
            System.currentTimeMillis());
        System.out.println("Done:  deleteCount is    " + results.deleteCount);
        System.out.println("Done:  deleteBytes is " + numberFormat.format(results.deleteBytes));
        System.out.println("Done:  keepCount is    " + results.keepCount);
        System.out.println("Done:  keepBytes is " + numberFormat.format(results.keepBytes));
        System.out.println("Done:  processedCount is " + results.processedCount);
        int hours = (int) (results.processSeconds / 3600);
        int minutes = (int) ((results.processSeconds / 60) % 60);
        int seconds = (int) (results.processSeconds % 60);
        System.out.println("Done:  processTime is    " + hours + ":" + minutes + ":" + seconds);

        System.out.println("Done:  uselessDeleteCount is    " + uselessResults.deleteCount);
        System.out.println("Done:  uselessDeleteBytes is    " + numberFormat.format(uselessResults.deleteBytes));
        System.out.println("Done:  uselessKeepCount is    " + uselessResults.keepCount);
        System.out.println("Done:  uselessKeepBytes is    " + numberFormat.format(uselessResults.keepBytes));
        System.out.println("Done:  uselessProcessedCount is " + uselessResults.processedCount);
        hours = (int) (uselessResults.processSeconds / 3600);
        minutes = (int) ((uselessResults.processSeconds / 60) % 60);
        seconds = (int) (uselessResults.processSeconds % 60);
        System.out.println("Done:  uselessProcessTime is    " + hours + ":" + minutes + ":" + seconds);
      }

      System.out.println("Submitting RCLSPLSTG");
      String command = "SBMJOB CMD(RCLSPLSTG DAYS(*NONE))";
      Statement stmt = conn.createStatement();
      stmt.execute("CALL QSYS2.QCMDEXC('" + command + "')");
      stmt.close();
      System.out.println("DONE");
    } catch (Exception e) {
      System.out.println("Exception while running");
      e.printStackTrace(System.out);
      usage();
    }

  }

  public static JDCleanSplfJdbcResults deleteSpoolFiles(PrintStream out, Connection conn, String nestLevel,
      long startFilterTime, long endFilterTime, boolean deleteAll, Hashtable<String, String> deleteUserIds)
      throws Exception {

    long startTime = System.currentTimeMillis();
    Timestamp startTimestamp = new Timestamp(startFilterTime);
    Timestamp endTimestamp = new Timestamp(endFilterTime);

    out.println(nestLevel + "Attempting deleteSpoolFiles " + startTimestamp + " - " + endTimestamp);
    if (startFilterTime >= endFilterTime) {
      out.println(nestLevel + "**** Warning -- aborting since time filters are the same");
      return new JDCleanSplfJdbcResults(0, 0, 0, 0, 0, 0);
    }

    try {

      return deleteSpoolFilesAttempt(out, conn, nestLevel, startFilterTime, endFilterTime, deleteAll, deleteUserIds);
    } catch (Exception e) {
      long failTime = System.currentTimeMillis();
      String message = e.toString();
      /* CPF34C4 List is too large for user space QNPSLIST */
      /* or connection dropped exception */
      if ((message.indexOf("CPF34C4") >= 0) || (message.indexOf("ConnectionDropped") >= 0)) {
        if (message.indexOf("CPF34C4") >= 0) {
          out.println(nestLevel + "CPF34C4 List is too large for user space QNPSLIST caught ");
        } else {
          out.println(nestLevel + "Connection Dropped caught ");
        }
        long seconds = (failTime - startTime) / 1000;
        out.println(nestLevel + "Error after " + seconds + " seconds for " + startTimestamp + " - " + endTimestamp);

        if (verbose) {
          e.printStackTrace(out);
        }
        // Split the time in quarters and call recursively
        long quarterTime = (endFilterTime - startFilterTime) / 4;
        if (quarterTime < 1000) {
          out.println(" **** Warning *** quarter time too small . aborting");
          return new JDCleanSplfJdbcResults(0, 0, 0, 0, 0, 0);
        }

        JDCleanSplfJdbcResults firstResults = deleteSpoolFiles(out, conn, nestLevel + "a ", startFilterTime,
            startFilterTime + quarterTime, deleteAll, deleteUserIds);
        JDCleanSplfJdbcResults secondResults = deleteSpoolFiles(out, conn, nestLevel + "b ", startFilterTime + quarterTime,
            startFilterTime + 2 * quarterTime, deleteAll, deleteUserIds);
        JDCleanSplfJdbcResults thirdResults = deleteSpoolFiles(out, conn, nestLevel + "c ",
            startFilterTime + 2 * quarterTime, startFilterTime + 3 * quarterTime, deleteAll, deleteUserIds);
        JDCleanSplfJdbcResults fourthResults = deleteSpoolFiles(out, conn, nestLevel + "d ",
            startFilterTime + 3 * quarterTime, endFilterTime, deleteAll, deleteUserIds);

        JDCleanSplfJdbcResults newResults = new JDCleanSplfJdbcResults(firstResults, secondResults, thirdResults,
            fourthResults);
        newResults.processSeconds += seconds; /* include the timeout time */

        long hours = newResults.processSeconds / 3600;
        long minutes = (newResults.processSeconds / 60) % 60;
        seconds = newResults.processSeconds % 60;

        out.println(nestLevel + "DONE         for " + startTimestamp + " - " + endTimestamp + " deleteCount="
            + newResults.deleteCount + " deleteBytes=" + numberFormat.format(newResults.deleteBytes) + " keepCount="
            + newResults.keepCount + " keepBytes=" + numberFormat.format(newResults.keepBytes) + " processCount="
            + newResults.processedCount + " time=" + hours + ":" + minutes + ":" + seconds);

        return newResults;
      } else {
        throw e;
      }
    }
  }

  public static JDCleanSplfJdbcResults deleteUselessSpoolFiles(PrintStream out, Connection conn, String nestLevel,
      long startFilterTime, long endFilterTime) throws Exception {

    Timestamp startTimestamp = new Timestamp(startFilterTime);
    Timestamp endTimestamp = new Timestamp(endFilterTime);
    long startTime = System.currentTimeMillis();
    out.println(nestLevel + "Attempting deleteUselessSpoolFile " + startTimestamp + " - " + endTimestamp);

    try {

      return deleteUselessSpoolFilesAttempt(out, conn, nestLevel, startFilterTime, endFilterTime);
    } catch (Exception e) {
      long failTime = System.currentTimeMillis();
      String message = e.toString();
      /* CPF34C4 List is too large for user space QNPSLIST */
      /* or connection dropped exception */
      if ((message.indexOf("CPF34C4") >= 0) || (message.indexOf("ConnectionDropped") >= 0)) {
        if (message.indexOf("CPF34C4") >= 0) {
          out.println(nestLevel + "CPF34C4 List is too large for user space QNPSLIST caught ");
        } else {
          out.println(nestLevel + "Connection Dropped caught ");
        }
        long seconds = (failTime - startTime) / 1000;
        out.println(nestLevel + "Error after " + seconds + " seconds for " + startTimestamp + " - " + endTimestamp);

        if (verbose) {
          e.printStackTrace(out);
        }

        // Split the time in half and call recursively
        /* try a smaller sizes first starting at 512 days */
        long quarterTime = (endFilterTime - startFilterTime) / 4;
        if (quarterTime < 1000) {
          out.println(" **** Warning *** quarter time too small . aborting");
          return new JDCleanSplfJdbcResults(0, 0, 0, 0, 0, 0);

        } else {
          JDCleanSplfJdbcResults resultsA = deleteUselessSpoolFiles(out, conn, nestLevel + "a ", startFilterTime,
              startFilterTime + quarterTime);
          JDCleanSplfJdbcResults resultsB = deleteUselessSpoolFiles(out, conn, nestLevel + "b ",
              startFilterTime + quarterTime, startFilterTime + 2 * quarterTime);
          JDCleanSplfJdbcResults resultsC = deleteUselessSpoolFiles(out, conn, nestLevel + "c ",
              startFilterTime + 2 * quarterTime, startFilterTime + 3 * quarterTime);
          JDCleanSplfJdbcResults resultsD = deleteUselessSpoolFiles(out, conn, nestLevel + "d ",
              startFilterTime + 3 * quarterTime, endFilterTime);
          JDCleanSplfJdbcResults newResults = new JDCleanSplfJdbcResults(resultsA, resultsB, resultsC, resultsD);
          newResults.processSeconds += seconds; /* Be sure to add the timeout time */
          long hours = newResults.processSeconds / 3600;
          long minutes = (newResults.processSeconds / 60) % 60;
          seconds = newResults.processSeconds % 60;

          out.println(nestLevel + "DONE         for " + startTimestamp + " - " + endTimestamp + " deleteCount="
              + newResults.deleteCount + " deleteBytes=" + numberFormat.format(newResults.deleteBytes) + " keepCount="
              + newResults.keepCount + " keepBytes=" + numberFormat.format(newResults.keepBytes) + " processCount="
              + newResults.processedCount + " time=" + hours + ":" + minutes + ":" + seconds);
          return newResults;
        }
      } else {
        throw e;
      }
    }
  }

  public static void runCleaningQuery(PrintStream out, Statement stmt, String query, JDCleanSplfJdbc clean) throws SQLException {
    out.println("Running query " + query);

    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) { 
      String userName = rs.getString(1); 
      String fileName = rs.getString(2); 
      int fileNumber = rs.getInt(3);
      String allJobName= rs.getString(4); 
      int firstSlashIndex = allJobName.indexOf('/'); 
      int secondSlashIndex = allJobName.indexOf('/',firstSlashIndex+1);
      String fileJobName = allJobName.substring(secondSlashIndex+1); 
      String fileJobUser = allJobName.substring(firstSlashIndex+1,secondSlashIndex); 
      String fileJobNumber = allJobName.substring(0,firstSlashIndex);  
      Timestamp createTimestamp = rs.getTimestamp(5);
      int pageCount = rs.getInt(6); 
      int dataLength = rs.getInt(7) * 1024;  /* Size in Kilobtes */ 
      
      clean.processSplf(userName, fileName, fileNumber, fileJobName, fileJobUser,
        fileJobNumber, createTimestamp, pageCount, dataLength);
    }
    rs.close(); 
  }
  public static JDCleanSplfJdbcResults deleteSpoolFilesAttempt(PrintStream out, Connection c, String nestLevel,
      long startFilterTime, long endFilterTime, boolean deleteAll, Hashtable<String, String> deleteUserIds)
      throws Exception {
    Statement stmt = c.createStatement(); 
    JDCleanSplfJdbc clean = new JDCleanSplfJdbc(out, c, nestLevel, deleteAll, deleteUserIds);
    //
    // Get the list using an SQL query.
    //
    Timestamp startTimestamp = new Timestamp(startFilterTime);
    Timestamp endTimestamp = new Timestamp(endFilterTime);
    long startMillis = System.currentTimeMillis();
    String userInClause=""; 
    Enumeration enumeration = deleteUserIds.keys(); 
    boolean addComma = false; 
    if (deleteAll) { 
      String query = " SELECT USER_NAME, SPOOLED_FILE_NAME, FILE_NUMBER ,JOB_NAME ,  CREATE_TIMESTAMP ,  TOTAL_PAGES, SIZE , USER_DATA    "
          + " FROM QSYS2 . OUTPUT_QUEUE_ENTRIES_BASIC WHERE CREATE_TIMESTAMP >= '"
          + startTimestamp.toString() + "'" + " AND CREATE_TIMESTAMP <= '" + endTimestamp.toString() + "'";
      System.out.println(query);
      runCleaningQuery(out, stmt, query, clean); 
      stmt.close(); 
      
    } else {
      while (enumeration.hasMoreElements()) {
        String userId = (String) enumeration.nextElement();
        if (addComma) {
          userInClause += ",";
        } else {
          addComma = true;
        }
        userInClause += "'" + userId + "'";

      }
      String query = " SELECT USER_NAME, SPOOLED_FILE_NAME, FILE_NUMBER ,JOB_NAME ,  CREATE_TIMESTAMP ,  TOTAL_PAGES, SIZE , USER_DATA    "
          + " FROM QSYS2 . OUTPUT_QUEUE_ENTRIES_BASIC WHERE " + " USER_NAME IN (" + userInClause + ") AND "
          + " CREATE_TIMESTAMP >= '" + startTimestamp.toString() + "'" + " AND CREATE_TIMESTAMP <= '"
          + endTimestamp.toString() + "'";
      out.println(query);
      runCleaningQuery(out,stmt, query, clean);
      stmt.close();
    }
    long finishMillis = System.currentTimeMillis();
    long deleteCount = clean.deleteCount_;
    long deleteBytes = clean.deleteBytes_;
    long keepCount = clean.keepCount_;
    long keepBytes = clean.keepBytes_;
    long processCount = 0;
    int processSeconds = (int) (finishMillis + 500 - startMillis) / 1000;

    return new JDCleanSplfJdbcResults(deleteCount, deleteBytes, keepCount, keepBytes, processCount, processSeconds);
  }

  public static JDCleanSplfJdbcResults deleteUselessSpoolFilesAttempt(PrintStream out, Connection c, String nestLevel,
      long startFilterTime, long endFilterTime) throws Exception {

    JDCleanSplfJdbc clean = new JDCleanSplfJdbc(out, c, nestLevel);

    Timestamp startTimestamp = new Timestamp(startFilterTime);
    Timestamp endTimestamp = new Timestamp(endFilterTime);
    long startMillis = System.currentTimeMillis();
    Statement stmt = c.createStatement(); 
    String query = " SELECT USER_NAME, SPOOLED_FILE_NAME, FILE_NUMBER ,JOB_NAME ,  CREATE_TIMESTAMP ,  TOTAL_PAGES, SIZE , USER_DATA    "
        + " FROM QSYS2 . OUTPUT_QUEUE_ENTRIES_BASIC WHERE  CREATE_TIMESTAMP >= '"
        + startTimestamp.toString() + "'" + " AND CREATE_TIMESTAMP <= '" + endTimestamp.toString() + "'";
    runCleaningQuery(out, stmt, query, clean); 
    stmt.close(); 
    System.out.println("Running query " + query);

    long deleteCount = clean.deleteCount_;
    long deleteBytes = clean.deleteBytes_;
    long keepCount = clean.keepCount_;
    long keepBytes = clean.keepBytes_;

    long processCount = 0;

    int processSeconds = (int) (System.currentTimeMillis() - startMillis) / 1000;
    int hours = processSeconds / 3600;
    int minutes = (processSeconds / 60) % 60;
    int seconds = processSeconds % 60;

    out.println(nestLevel + "DONE         for " + startTimestamp + " - " + endTimestamp + " deleteCount=" + deleteCount
        + " keepCount=" + keepCount + " processCount=" + processCount + " time=" + hours + ":" + minutes + ":"
        + seconds);
    return new JDCleanSplfJdbcResults(deleteCount, deleteBytes, keepCount, keepBytes, processCount, processSeconds);
  }

  String nestLevel_ = "";
  boolean deleteAll_ = false;
  boolean deleteUseless_ = false;
  Hashtable<String, String> deleteUserIds_ = null;
  long deleteCount_ = 0;
  long deleteBytes_ = 0;
  long keepCount_ = 0;
  long keepBytes_ = 0;
  long processCount_ = 0;
  CharConverter charConverter_ = null;
  Connection conn_;
  Statement stmt_;
  private PrintStream out;

  public JDCleanSplfJdbc(PrintStream out, Connection conn, String nestLevel, boolean deleteAll,
      Hashtable<String, String> deleteUserIds) throws SQLException {
    this.conn_ = conn;
    this.stmt_ = conn.createStatement();
    this.nestLevel_ = nestLevel;
    this.deleteAll_ = deleteAll;
    deleteUserIds_ = deleteUserIds;
    deleteCount_ = 0;
    deleteBytes_ = 0;
    keepCount_ = 0;
    keepBytes_ = 0;
    processCount_ = 0;
    this.out = out;
    try {
      charConverter_ = new CharConverter(37);
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public JDCleanSplfJdbc(PrintStream out, Connection conn, String nestLevel) throws SQLException {
    this.conn_ = conn;
    this.stmt_ = conn.createStatement();
    deleteUseless_ = true;
    this.nestLevel_ = nestLevel;
    this.deleteAll_ = false;
    deleteUserIds_ = new Hashtable<String, String>();
    deleteCount_ = 0;
    deleteBytes_ = 0;
    keepCount_ = 0;
    keepBytes_ = 0;
    processCount_ = 0;
    this.out = out;
    try {
      charConverter_ = new CharConverter(37);
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public long getDeleteCount() {
    return deleteCount_;
  }

  public long getDeleteBytes() {
    return deleteBytes_;
  }

  public long getKeepCount() {
    return keepCount_;
  }

  public long getKeepBytes() {
    return keepBytes_;
  }

  public long getProcessCount() {
    return processCount_;
  }

  StringBuffer keepReason = new StringBuffer();

  public void processSplf(String jobUser, String fileName, int fileNumber, String fileJobName, String fileJobUser,
      String fileJobNumber, Timestamp createTimestamp, int pageCount, int dataLength) {
    if (verbose) {
      out.println(nestLevel_ + " Processing :" + jobUser);
    }
    keepReason.setLength(0);
    processCount_++;
    if (processCount_ % 1000 == 0) {
      out.println(
          nestLevel_ + "  processCount=" + processCount_ + " deleteCount=" + deleteCount_ + " keepCount=" + keepCount_);
      out.println(nestLevel_+"    fileInfo = "+createTimestamp+" "+jobUser+" "+fileName+" "+fileNumber +" "+
          fileJobName+" "+fileJobUser+" "+ fileJobNumber);
      /* Check the number of jobs and delete extra if needed */
    }
    boolean doDelete = false;
    if (deleteAll_) {
      doDelete = true;
    } else {
      if (deleteUserIds_.get(jobUser) != null) {
        doDelete = true;
      } else if (deleteUseless_) {
        try {

          if (pageCount <= 100) {
            // Read in the file
            StringBuffer sb = new StringBuffer(); 
            String sql = " SELECT SPOOLED_DATA "
                + "FROM TABLE(SYSTOOLS.SPOOLED_FILE_DATA("
                + "JOB_NAME=>'"+fileJobNumber+"/"+fileJobUser+"/"+fileJobName+"'," 
                + "SPOOLED_FILE_NAME =>'"+fileName+"',"
                + "SPOOLED_FILE_NUMBER=> "+fileNumber+")) ORDER BY ORDINAL_POSITION"; 
            ResultSet rs = stmt_.executeQuery(sql); 
            while (rs.next()) { 
              sb.append(rs.getString(1)); 
              sb.append("\n"); 
            }
            String data = sb.toString(); 
            if (fileName.equals("QPJOBLOG")) {

              // Check for messages indicating a useless spool file
              // CPF2523 No job log information.
              if (data.indexOf("CPF2523") >= 0) {
                doDelete = true;
              } else if (onlyNoiseMessages(data, keepReason)) {
                doDelete = true;
              }

            } else {
              /* Not a job log */
              if (fileName.equals("QPRINT")) {
                if (hasQprintNoise(data)) {
                  doDelete = true;
                } else {
                  int substringLen = data.length();
                  if (substringLen > 80) {
                    substringLen = 80;
                  }
                  keepReason.append("No QPRINT noise: " + data.substring(0, substringLen).replace('\u0000', ' '));
                }
              } else {
                if (hasNoiseFileName(fileName)) {
                  doDelete = true;
                } else {
                  keepReason.append("Not noise file name: " + fileName);
                }
              }
            }
          } else {
            keepReason.append("Page count is " + pageCount);
          } /* not lest than 100 */
        } catch (Exception e1) {
          synchronized (out) {
            out.println("Warning: Exception caught ");
            e1.printStackTrace(out);
          }

        }
      }
    }
    if (doDelete) {
      if (verbose) {
        out.println(nestLevel_ + "Deleting " + createTimestamp + " " + fileName + " " + fileNumber + " " + fileJobName
            + " " + fileJobUser + " " + fileJobNumber);
      }
      try {

        deleteBytes_ += dataLength;
        String sql = "CALL QSYS2.QCMDEXC(" + "'DLTSPLF FILE(" + fileName + ") " + "JOB(" + fileJobNumber + "/"
            + fileJobUser + "/" + fileJobName + ") " + "SPLNBR(" + fileNumber + ")   ')";
        stmt_.executeUpdate(sql);
        deleteCount_++;
      } catch (Exception e1) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String message = e1.toString();
        if ((message.indexOf("no longer in the system") > 0) || (message.indexOf("CPF334") > 0)
            || /* Various spool error messages */
            (message.indexOf("CPF3C40") > 0) || /* Spooled File not found */
            (message.indexOf("CPF3303") > 0)) { /* File not found in job */
          /* Just ignore these messages */
        } else {
          synchronized (out) {
            out.println(nestLevel_ + " " + ts.toString() + " Delete failed");
            e1.printStackTrace(out);
          }
        }
      }
    } else {
      try {
        keepBytes_ += dataLength;
      } catch (Exception e1) {
        synchronized (out) {
          out.println(nestLevel_ + " Size query failed");
          e1.printStackTrace(out);
        }
      }
      keepCount_++;

      if (verbose) {
        out.println(nestLevel_ + "Keeping  " + createTimestamp + " FILE(" + fileName + ")" + " SPLNBR(" + fileNumber
            + ") " + " JOB(" + fileJobNumber + "/" + fileJobUser + "/" + fileJobName + ")  REASON:"
            + keepReason.toString());
      }
    }

  }

  String[] noiseFileNames = { "ALLTYPES", "ARRAYRS", "CACHING", "CACHING2", "CKEYCUST", "CMPLXKEY", "CUST", "DBCSTEST",
      "DDMCC1", "DDMCC2", "DDMCC3", "DDMCCK1", "DDMCCK2", "DDMFIX", "DDMLOCK", "DDMSER", "DECTEST", "F1M1RW", "F2M1RW",
      "FEWROWS", "FILE1", "FILE2", "FILEC", "GCLL", "GETRF", "GMNAME", "JDCSC11PGM", "JDCSC26PGM", "JDCSC27PGM",
      "JDCSC28PGM", "JDCSC29PGM", "JDCSC30PGM", "JDCSC31PGM", "JDCSCBLPGM", "JDCSCLPGM", "JDCSCP2PGM", "JDCSCPGM",
      "JDCSCPPPGM", "JDCSRPGDAT", "JDCSRPGPG2", "JDCSRPGPG3", "JDCSRPGPG4", "JDCSRPGPGM", "JDCSRPGTIM", "JOBSQL",

      "KEYFILE", "KEYSRC", "KEYSRC2", "KEYSRC3", "LOGICAL1", "LONGFIL", "MLTFMT", "MYCUSTCDT", "NODATA", "NOKEY",
      "P9936798", "P9946152", "P9946152B", "PDZ410F1", "PDZ410F2", "QPRTLIBL", "QPDSPJOB", "READKEY1", "READKEY2",
      "READKEY3", "READRN", "RTEST", "SETRF", "SHORTCOL", "SHORTFILE", "SKEYCUST", "SMPLKEY", "TEST", "TEST7036",
      "TESTBIN8", "THDTST0", "THDTST1", "THDTST2", "THDTST3", "UPDATE1", "UPDATE10", "UPDATE11", "UPDATE13", "UPDATE14",
      "UPDATE15", "UPDATE16", "UPDATE17", "UPDATE18", "UPDATE2", "UPDATE20", "UPDATE21", "UPDATE22", "UPDATE23",
      "UPDATE24", "UPDATE25", "UPDATE26", "UPDATE28", "UPDATE29", "UPDATE30", "UPDATE4", "UPDATE5", "UPDATE6",
      "UPDATE8", "UPDATE9", "V107", "V11", "V12", "V15", "V16", "V17", "V19", "V20", "V21", "V22", "V23", "V24", "V25",
      "V26", "V27", "V28", "V29", "V3", "V30", "V31", "V32", "V33", "V34", "V35", "V36", "V37", "V38", "V39", "V4",
      "V40", "V41", "V42", "V43", "V44", "V45", "V46", "V47", "V48", "V49", "V50", "V51", "V52", "V53", "V54", "V55",
      "V56", "V57", "V58", "V59", "V60", "V61", "V62", "V63", "V65", "V7", "V8", "VSQLBUT", "WRITEK14", "WRITEK15",
      "WRITEV14", "WRITEV15", "WRITEV35",

  };

  boolean hasNoiseFileName(String fileName) {
    for (int i = 0; i < noiseFileNames.length; i++) {
      if (fileName.equals(noiseFileNames[i])) {
        return true;
      }
    }

    return false;
  }

  private boolean hasQprintNoise(String data) {
    String[] qprintNoise = { "Listening for transport dt_socket", "added manifest", " A normal program ",
        " loopdead loopdead loopdead ", " start to sleep 10s done", " Internal spool control file not accessible.",
        "THE SQL COMMAND COMPLETED SUCCESSFULLY", "Starting Deployment", "Starting JobQueue", "submitted to job queue",
        "Error found creating directory", " Error found opening file", "Output queue changed to QPRINT",
        "CPF2110:  Library", "CPF7020:  Journal receivers", "CPF2125:  No objects deleted.", "CZS0607:  Module",
        "CPC2201:  Object authority granted.",

    };

    for (int i = 0; i < qprintNoise.length; i++) {
      if (data.indexOf(qprintNoise[i]) >= 0) {
        return true;
      }
    }

    return false;
  }

// Does the data only contain noise messages
  static String[][] prefixesAndNoise = { { "MCH", }, { "SQL", "SQL0104", /* token not valid */
      "SQL0117", /* wrong number of values */
      "SQL0189", /* CCSID not valid */
      "SQL0204", /* not found */
      "SQL0301", /* input variable not valid */
      "SQL0332", /* character conversion not valid */
      "SQL0335", /* conversion resulted in substition */
      "SQL0387", /* no additional result sets */
      "SQL0440", /* Routine not found */
      "SQL0443", /* trigger program detected an error */
      "SQL0595", /* commit level escalated */
      "SQL0601", /* already exists */
      "SQL0843", /* connection to rdb does not exist */
      "SQL0950", /*
                  * database not in RDBDIRE" /* SQL0952 processing of statement ended
                  */
      "SQL5016", /* qualified object name not valid */
      "SQL7022", /* user not same as current user */
      "SQL7908", "SQL792A", /* schema not created */

      }, { "CPF", "CPF0001", /* error found on command */
          "CPF0920", /* prestart jobs ending */
          "CPF1124", /* Job entered system */
          "CPF1164", "CPF1175", /* subsystem cannot autostart job */
          "CPF1275", /* Subsystem &1 cannot allocate device &2. */
          "CPF1301", /* ACGDTA not journaled */
          "CPF1302", "CPF1303", "CPF2103", /* library already exists */
          "CPF2105", /* object not found */
          "CPF2111", /* library already exists */
          "CPF2130", /* Objects duplicated */
          "CPF2204", /* user profile not found */
          "CPF22E2", /* password not correct */
          /* "CPF22E3", user profile is disabled */
          /* "CPF24A3", value fo call stack parameter not valid */
          "CPF3202", /* file in use */
          "CPF3423", /* job queue not release */
          "CPF3485", "CPF338C", /* Internal spool control file not accessible. */
          "CPF3596", /* PTF numbers in select/omit list not permitted */
          "CPF3635", /* PTF superceeded */
          /* CPF3698 dump output directed to spool file */
          /* CPF4404 file already closed */
          "CPF5009", /* duplicate record key */
          /* "CPF5034" Duplicate key on access PATH */
          "CPF5035", /* data mapping error */
          "CPF9162", /* cannot establish DDM connection */
          "CPF9190", /* authority failure on DDM connection attempt */
          "CPF9861", /* Output file &1 created in library &2. */
          "CPF9862", /* member added */
          "CPF9898", /* General escape message */
          "CPFB9C6", /* pase ended */

      }, { "CPC", "CPC0905", /* Subsystem &3 prestart job entry not active */
          "CPI0982", /* changed but may be adjusted */
          "CPC1129", /* job changed */
          "CPC1134", /* shared pool changed */
          "CPC1165", /* sigterm signal sent */
          /* "CPC1166", time limit reached for SIGTERM handler */
          "CPC1207", /* subsystem ending immediately */
          "CPC1221", /* Job &3/&2/&1 submitted */
          /* "CPC1224" Job Ended abnormally */
          "CPC1234", /* ended from job queue */
          "CPC1602", /* Subsystem description changed */
          "CPC2103", /* object changed */
          "CPC2130", /* objects duplicated */
          "CPC2191", /* file deleted */
          "CPC2196", /* library added to library list */
          "CPC2197", /* Library removed from library list */
          "CPC2198", /* Current library changed */
          "CPC221B", /* object changed */
          "CPC2605", /* varyon completed */
          "CPC2609", /* varyon completed */
          "CPC2957", /* no records copied */
          "CPC2958", /* All records copied */
          "CPC2983", /* data in member reorganized */
          "CPI6609", /* Alert processing ended */
          "CPC7301", /* file created */
          "CPC7305", /* member added */
          "CPC9801", /* object created */
          "CPCA083", /* Directory created */
          "CPCA087", /* objects copied */
          "CPCA980", "CPCA981", "CPCA984", /* trace option changed" */
          "CPCA986", /* user trace dumped */
      }, { "CPI", "CPI0952", /* Start of prestart jobs in progress */

          "CPI1125", /* job submitted */
          "CPI2101", /* object create */
          "CPI2218", /* Authority revoked */
          "CPI32E8", /* Trigger was changed */
          "CPI7BC4", /* Alert processing started on &1 at &2. */
          "CPI8911", /* Target display station pass-through */
          "CPI8912", /* Target display station pass-through ser */
          "CPI907F", /* shadow controller not started */
          "CPI9160", /* Database connection started over TCP/IP. */
          "CPI9161", /* Database connection ended over TCP/IP. */
          "CPI9162", /* Target job assigned to handle DDM connection */
          "CPIAD08", /* Host servers communication error */

      },

      { "JVA", "JVAB302", "JVAB578", /* JVM properties loaded from file */
      }, { "QSH", "QSH0005", /* Command ended normally */
      }, { "*NO", "*NONE", },

  };

  boolean onlyNoiseMessages(String data, StringBuffer keepReason1) {
    // Make sure it is a JOBLOG
    for (int i = 0; i < prefixesAndNoise.length; i++) {
      String prefix = prefixesAndNoise[i][0];

      int dataLength = data.length();
      int searchOffset = 0;
      while (searchOffset >= 0) {
        searchOffset = data.indexOf(prefix, searchOffset);
        if (searchOffset > 0) {
          if (searchOffset + 8 < dataLength) {
            String message = extractMessage(data, searchOffset);
            if (message != null) {
              boolean foundMessage = false;
              for (int j = 1; (!foundMessage) && (j < prefixesAndNoise[i].length); j++) {
                if (message.equals(prefixesAndNoise[i][j])) {
                  foundMessage = true;
                }
              }
              if (!foundMessage) {
                // We found a message that was not noise -- bail out right away
                if (verbose) {
                  keepReason1.append("Message " + message + " was not noise");
                  out.println("Message " + message + " was not noise");
                  int lastOffset = searchOffset + 800;
                  if (lastOffset > data.length())
                    lastOffset = data.length();
                  String printString = data.substring(searchOffset, lastOffset).replace('\u0001', '\n');
                  out.println("        " + printString);
                }
                return false;
              }
            }
            searchOffset = searchOffset + 8;
          } else {
            searchOffset = -1;
          }
        }
      }
    }
    return true;
  }

  private String extractMessage(String data, int searchOffset) {
    char prefix = data.charAt(searchOffset - 1);
    if (prefix == ' ' || prefix == 0x0001) {
      if ((data.charAt(searchOffset + 7) == ' ') && isMessageDigit(data.charAt(searchOffset + 3))
          && isMessageDigit(data.charAt(searchOffset + 4)) && isMessageDigit(data.charAt(searchOffset + 5))
          && isMessageDigit(data.charAt(searchOffset + 6))) {
        return data.substring(searchOffset, searchOffset + 7);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  private boolean isMessageDigit(char charAt) {
    if (charAt >= '0' && charAt <= '9')
      return true;
    if (charAt >= 'A' && charAt <= 'Z')
      return true;
    return false;
  }

}

class JDCleanSplfJdbcResults {
  public long deleteCount;
  public long deleteBytes;
  public long keepCount;
  public long keepBytes;

  public long processedCount;
  public long processSeconds;

  public JDCleanSplfJdbcResults(long deleteCount, long deleteBytes, long keepCount, long keepBytes, long processedCount,
      long processSeconds) {
    this.deleteCount = deleteCount;
    this.deleteBytes = deleteBytes;
    this.keepCount = keepCount;
    this.keepBytes = keepBytes;
    this.processedCount = processedCount;
    this.processSeconds = processSeconds;
  }

  public JDCleanSplfJdbcResults(JDCleanSplfJdbcResults a, JDCleanSplfJdbcResults b) {
    this.deleteCount = a.deleteCount + b.deleteCount;
    this.deleteBytes = a.deleteBytes + b.deleteBytes;
    this.keepCount = a.keepCount + b.keepCount;
    this.keepBytes = a.keepBytes + b.keepBytes;
    this.processedCount = a.processedCount + b.processedCount;
    this.processSeconds = a.processSeconds + b.processSeconds;
  }

  public JDCleanSplfJdbcResults(JDCleanSplfJdbcResults a, JDCleanSplfJdbcResults b, JDCleanSplfJdbcResults c, JDCleanSplfJdbcResults d) {
    this.deleteCount = a.deleteCount + b.deleteCount + c.deleteCount + d.deleteCount;
    this.deleteBytes = a.deleteBytes + b.deleteBytes + c.deleteBytes + d.deleteBytes;
    this.keepCount = a.keepCount + b.keepCount + c.keepCount + d.keepCount;
    this.keepBytes = a.keepBytes + b.keepBytes + c.keepBytes + d.keepBytes;
    this.processedCount = a.processedCount + b.processedCount + c.processedCount + d.processedCount;
    this.processSeconds = a.processSeconds + b.processSeconds + c.processSeconds + d.processSeconds;
  }
}

