///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDReport.java
//
// Classes:      JDReport
//
// Replaces the Report shell script that is used to generate an
// html report of the regression testing.
//
// Runs regression testcases.  Should be run above the test directory
// So that the classpath is correct and all directories are available.
//
// Configuration is read from ini directory.
// Test output if read from the ct diretory
// Output is placed in the ct directory
//
// A web server to run the results exists in the ct directory and
// should be run from the ct directory.
//
////////////////////////////////////////////////////////////////////////
package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import java.net.URL;
import java.net.URLClassLoader;

public class JDReport {

  public static Properties iniProperties;
  public static String AS400 = "";
  public static String hostname = "";
  public static boolean debug = false;
  public static boolean reset = false;
  public static boolean noExit = false;
  static {
    String debugProperty = System.getProperty("debug");
    if (debugProperty != null)
      debug = true;
    String resetProperty = System.getProperty("reset");
    if (resetProperty != null)
      reset = true;
  }

  public static String getReportName(String initials) {
    return "ct/latest" + initials + ".html";
  }

  /*
   * <td>RG<td><a
   * href="out/lp03ut5/runit.2010-10-31-18.01.42">2010-10-31-18.01.42</a><td><a
   * href="out/lp03ut5/runit.2010-11-02-22.46.39">2010-11-02-22.46.39</a><td>
   * lp03ut5<td>JDDriverConnect<td>32<td>1<td>0<td>21<td>0<td>4.566<tr>
   */

  public static String extractRGTestcase(String x) {
    int tdIndex = 0;
    tdIndex = x.indexOf("<td>");
    for (int i = 0; (tdIndex >= 0) && (i < 4); i++) {
      tdIndex = x.indexOf("<td>", tdIndex + 1);
    }
    if (tdIndex > 0) {
      int startIndex = tdIndex + 4;
      tdIndex = x.indexOf("<td>", startIndex);
      if (tdIndex > 0) {
        return x.substring(startIndex, tdIndex);
      } else {
        return "UNK";
      }
    } else {
      return "UNK";
    }

  }

  public static String extractFDTestcase(String x) {
    int tdIndex = 0;
    tdIndex = x.indexOf("<td>");
    for (int i = 0; (tdIndex >= 0) && (i < 3); i++) {
      tdIndex = x.indexOf("<td>", tdIndex + 1);
    }
    if (tdIndex > 0) {
      int startIndex = tdIndex + 4;
      tdIndex = x.indexOf("<td>", startIndex);
      if (tdIndex > 0) {
        return x.substring(startIndex, tdIndex);
      } else {
        return "UNK";
      }
    } else {
      return "UNK";
    }

  }

  /**
   * Return array containing the following information
   *
   * @return String[0] Notatt Count String[1] Regressed Count String[2] Failed
   *         Count String[3] Success Count String[4] Regressed Testcases
   *         String[5] Run minutes String[6] Failed Testcases
   */
  public static String[] getStats(File htmlFile) {
    int iRegressedCount = 0;
    int iFailedCount = 0;

    String[] output = new String[7];
    output[0] = "0";
    output[1] = "Unknown";
    output[2] = "Unknown";
    output[3] = "Unknown";
    output[4] = "";
    output[5] = "Unknown";
    output[6] = "";
    StringBuffer regressedTestcases = new StringBuffer();
    regressedTestcases.append("<table border>");
    StringBuffer failedTestcases = new StringBuffer();
    failedTestcases.append("<table border>");
    int failedTestcasesCount = 0;

    try {

      BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
      String line = reader.readLine();

      while (line != null) {
        int regressedCountIndex = line.indexOf("REGRESSED_COUNT=");
        if (regressedCountIndex > 0) {
          line = line.substring(regressedCountIndex + 16);
          int lessIndex = line.indexOf('<');
          if (lessIndex > 0) {
            output[1] = line.substring(0, lessIndex);
            iRegressedCount = Integer.parseInt(output[1]);
          }
        }

        int notattCountIndex = line.indexOf("NOTATT_COUNT=");
        if (notattCountIndex > 0) {
          line = line.substring(notattCountIndex + 13);
          int lessIndex = line.indexOf('<');
          if (lessIndex > 0) {
            output[0] = line.substring(0, lessIndex);
          }
        }

        int failedCountIndex = line.indexOf("FAILED_COUNT=");
        if (failedCountIndex > 0) {
          line = line.substring(failedCountIndex + 13);
          int lessIndex = line.indexOf('<');
          if (lessIndex > 0) {
            output[2] = line.substring(0, lessIndex);
            iFailedCount = Integer.parseInt(output[2]);
          }
        }

        failedCountIndex = line.indexOf("SUCCESS_COUNT=");
        if (failedCountIndex > 0) {
          line = line.substring(failedCountIndex + 14);
          int lessIndex = line.indexOf('<');
          if (lessIndex > 0) {
            output[3] = line.substring(0, lessIndex);
          }
        }

        failedCountIndex = line.indexOf("RUN_MINUTES=");
        if (failedCountIndex > 0) {
          line = line.substring(failedCountIndex + 12);
          int lessIndex = line.indexOf('<');
          if (lessIndex > 0) {
            output[5] = line.substring(0, lessIndex);
          }
        }

        int rgIndex = line.indexOf("<td>RG<td>");
        if (rgIndex >= 0) {
          String testcase = extractRGTestcase(line);
          regressedTestcases.append("<td>" + testcase + "<td><tr>");
        }

        int fdIndex = line.indexOf("<td>FD<td>");
        if (fdIndex >= 0) {
          String testcase = extractFDTestcase(line);
          failedTestcases.append("<td>" + testcase + "<td><tr>");
          failedTestcasesCount++;
        }

        line = reader.readLine();
      }
      regressedTestcases.append("</table>");
      failedTestcases.append("</table>");

      if (iRegressedCount > 0 && iRegressedCount < 3) {
        output[4] = regressedTestcases.toString();
      }

      String testsLabel = " tests ";
      if (failedTestcasesCount == 1) {
        testsLabel = " test ";
      }
      if (iFailedCount > 0 && failedTestcasesCount < 4) {
        output[6] = " in " + failedTestcasesCount + testsLabel
            + failedTestcases.toString();
      } else {
        output[6] = " in " + failedTestcasesCount + testsLabel;

      }

      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return output;
  }

  public static String formatLine(String next, Timestamp jarTimestamp) {
    File htmlFile = new File("ct/" + next);
    Timestamp changeDate = new Timestamp(htmlFile.lastModified());
    String changeDateString = changeDate.toString();
    String compactDateString = changeDateString.replace(' ', 'x');
    String[] stats = getStats(htmlFile);

    long jarTime = 0;
    if (jarTimestamp != null)
      jarTime = jarTimestamp.getTime();
    long oneDay = System.currentTimeMillis() - 1 * /* days / week */
        24 * /* hours / day */
        60 * /* min / hour */
        60 * /* sec / hour */
        1000; /* ms / sec */
    long threeDays = System.currentTimeMillis() - 3 * /* days / week */
        24 * /* hours / day */
        60 * /* min / hour */
        60 * /* sec / hour */
        1000; /* ms / sec */

    String returnString = "";
    if (jarTime > changeDate.getTime()) {
      returnString = "<tr><td><font color=\"red\">" + changeDateString
          + "</font>";
    } else {
      if (changeDate.getTime() > oneDay) {
        returnString = "<tr><td><font color=\"darkgreen\"><b>"
            + changeDateString + "</b></font>";
      } else if (changeDate.getTime() > threeDays) {
        returnString = "<tr><td><font color=\"blue\">" + changeDateString
            + "</font>";
      } else {
        returnString = "<tr><td>" + changeDateString;
      }
    }
    if ("0".equals(stats[0])) {
      returnString += "<td><a href=\"" + next + "?" + compactDateString + "\">"
          + describeTest(next) + " Test Results</a>";
      if ("0".equals(stats[1])) {
        returnString += "<td>" + stats[0] + "<td><font color=\"darkgreen\"><b>"
            + stats[1] + "</b></font>" + stats[4] + "<td>" + stats[2] + stats[6]
            + "<td>" + stats[3] + "<td>" + stats[5];
      } else {
        returnString += "<td>" + stats[0] + "<td>" + stats[1] + stats[4]
            + "<td>" + stats[2] + stats[6] + "<td>" + stats[3] + "<td>"
            + stats[5];
      }
    } else {
      returnString += "<td><a href=\"" + next + "?" + compactDateString + "\">"
          + describeTest(next)
          + " Test Results</a><td><font color=\"red\">ERROR " + stats[0]
          + "</font><td>" + stats[1] + stats[4] + "<td>" + stats[2] + stats[6]
          + "<td>" + stats[3] + "<td>" + stats[5];
    }

    return returnString;
  }

  public static String formatHeader() {
    return "<th>Date<th>Test<th>NotAtt<th>Regressed<th>Failed<th>Success<th>RunMinutes";
  }

  public static boolean isNativeJDBC(String initials) {
    char c4 = initials.charAt(4);

    switch (c4) {
    case 'M':
      return true;
    case 'N':
      return true;
    case 'K':
      return true;
    case 'D':
      return true;
    }
    return false;
  }

  public static boolean isCli(String initials) {
    char c4 = initials.charAt(4);

    switch (c4) {

    case 'R':
      return true;
    case 'S':
      return true;
    case 'W':
      return true;
    case 'Y':
      return true;
    case 'Z':
      return true;

    }
    return false;
  }

  public static boolean isJavaStoredProcedures(String initials) {
    char c4 = initials.charAt(4);

    switch (c4) {
    case 'O':
      return true;
    }
    return false;
  }

  public static boolean isCoverage(String initials) {
    char c4 = initials.charAt(4);

    switch (c4) {
    case 'Q':
      return true;
    }
    return false;
  }

  public static boolean isSQ(String initials) {
    char c4 = initials.charAt(4);

    switch (c4) {
    case 'Y':
      return true;
    }
    return false;
  }

  public static boolean isToolbox(String initials) {
    char c4 = initials.charAt(4);
    switch (c4) {
    case '1':
      return true;
    case '2':
      return true;
    case 'A':
      return true; // description += "TOOLBOX "; break;
    case 'B':
      return true; // description += "TOOLBOX Native "; break;
    case 'C':
      return true; // description += "TOOLBOX Proxy "; break;
    case 'H':
      return true; // description += "JDBC Toolbox "; break;
    case 'I':
      return true; // description += "JDBC Toolbox "; break;
    case 'T':
      return true; // description += "JDBC Toolbox "; break;
    case 'Q':
      return true; // description += "JDBC Toolbox "; break;
    case 'U':
      return true; // description += "JDBC Toolbox Native"; break;
    case 'V':
      return true; // description += "JDBC Toolbox Proxy"; break;
    case 'X':
      return true; // description += "JDBC Toolbox Extended Dynamic"; break;
    }
    return false;
  }

  public static boolean isJtopenlite(String initials) {
    char c4 = initials.charAt(4);
    switch (c4) {
    case 'L':
      return true;
    }
    return false;
  }

  public static boolean isAndroid(String initials) {
    char c4 = initials.charAt(4);
    switch (c4) {
    case 'G':
      return true;
    }
    return false;
  }

  public static boolean isJava8(String initials) {
    char c2 = initials.charAt(2);
    switch (c2) {
    case '8':
      return true; // description += "JDK 1.7 "; break;
    }
    return false;
  }

  public static boolean isJava6(String initials) {
    char c2 = initials.charAt(2);
    switch (c2) {
    case '6':
      return true; // description += "JDK 1.6 "; break;
    case '7':
      return true; // description += "JDK 1.7 "; break;
    }
    return false;
  }

  public static boolean isV6(String initials) {
    char c1 = initials.charAt(0);
    switch (c1) {
    case '6':
      return true;
    }
    return false;
  }

  public static boolean is400(String initials) {
    char c3 = initials.charAt(3);
    switch (c3) {
    case 'C':
      return true; // description += "Classic "; break;
    case '3':
      return true; // description += "J9 32-bit "; break;
    case '4':
      return true;
    case '6':
      return true; // description += "J9 64-bit "; break;
    case '7':
      return true;
    }
    return false;
  }

  public static String describeTest(String input) {
    String description = "";
    int htmlIndex = input.indexOf(".html");
    if (htmlIndex > 0)
      input = input.substring(0, htmlIndex);

    int latestIndex = input.indexOf("latest");
    if (latestIndex >= 0) {
      input = input.substring(latestIndex + 6);
    }
    char c0 = input.charAt(0);
    char c1 = input.charAt(1);
    if (c0 == '5' && c1 == '4') {
      description = "V5R4 ";
    } else if (c0 == '6' && c1 == '1') {
      description = "V6R1 ";
    } else if (c0 == '7' && c1 == '1') {
      description = "V7R1 ";
    } else if (c0 == '7' && c1 == '2') {
      description = "V7R2 ";
    } else if (c0 == '7' && c1 == '3') {
      description = "V7R3 ";
    } else if (c0 == '7' && c1 == '4') {
      description = "V7R4 ";
    } else if (c0 == '7' && c1 == '5') {
      description = "V7R5 ";
    } else if (c0 == '7' && c1 == '6') {
      description = "V7R6 ";
    } else {
      description = "V?R? ";
    }

    char c2 = input.charAt(2);
    switch (c2) {
    case '3':
      description += "JDK 1.3 ";
      break;
    case '4':
      description += "JDK 1.4 ";
      break;
    case '5':
      description += "JDK 1.5 ";
      break;
    case '6':
      description += "JDK 1.6 ";
      break;
    case '7':
      description += "JDK 1.7 ";
      break;
    case '8':
      description += "JDK 1.8 ";
      break;
    case '9':
      description += "JDK 9 ";
      break;
    case 'B':
      description += "JDK 11 ";
      break;
    case 'C':
      description += "JDK 17 ";
      break;
    case 'P':
      description += "PITT";
      break;
    default:
      description += "JDK=" + c2 + "? ";
      break;
    }

    char c3 = input.charAt(3);
    switch (c3) {
    case 'C':
      description += "Classic ";
      break;
    case 'S':
      description += "Sun ";
      break;
    case 'H':
      description += "Sun Thai ";
      break;
    case 'J':
      description += "Sun Japan ";
      break;
    case 'T':
      description += "Windows J9 ";
      break;
    case '3':
      description += "J9 32-bit  ";
      break;
    case '4':
      description += "J9.1 32-bit  ";
      break;
    case '6':
      description += "J9 64-bit  ";
      break;
    case '7':
      description += "J9.1 64-bit  ";
      break;
    case 'L':
      description += "Linux J9 32-bit ";
      break;
    case 'M':
      description += "Linux Sun 32-bit ";
      break;
    case 'N':
      description += "Linux J9 64-bit ";
      break;
    case 'O':
      description += "Linux OpenJDK 64-bit ";
      break;
    case 'U':
      description += "Linux J9 64-bit mulitenant ";
      break;
    case 'P':
      description += "Linux Sun 64-bit ";
      break;
    case 'Q':
      description += "Linux OpenJDK 32-bit";
      break;
    case 'I':
      description += "";
      break;
    default:
      description += "UnknownJVM=" + c3 + " ";
      break;
    }

    char c4 = input.charAt(4);

    switch (c4) {
    case '1':
      description += "JDBC Toolbox running testcase from last release";
      break;
    case '2':
      description += "JDBC Toolbox running jar from last release";
      break;
    case 'A':
      description += "TOOLBOX ";
      break;
    case 'B':
      description += "TOOLBOX Native ";
      break;
    case 'C':
      description += "TOOLBOX Proxy ";
      break;
    case 'J':
      description += "JDBC JCC ";
      break;
    case 'L':
      description += "JDBC jtopenlite ";
      break;
    case 'H':
      description += "JDBC Toolbox Client Affinities";
      break;
    case 'I':
      description += "JDBC Toolbox Seamless failover";
      break;
    case 'K':
      description += "JDBC Native QIBM_JDBC_PASE_TRANSLATE";
      break;
    case 'M':
      description += "JDBC Native Fast Regression ";
      break;
    case 'N':
      description += "JDBC Native ";
      break;
    case 'O':
      description += "JDBC JSTP Native ";
      break;
    case 'D':
      description += "JDBC Native Debug";
      break;
    case 'T':
      description += "JDBC Toolbox ";
      break;
    case 'Q':
      description += "JDBC Toolbox Code Coverage ";
      break;
    case 'U':
      description += "JDBC Toolbox Native";
      break;
    case 'V':
      description += "JDBC Toolbox Proxy";
      break;
    case 'X':
      description += "JDBC Toolbox Extended Dynamic";
      break;
    case 'G':
      description += "JDBC Toolbox android ";
      break;
    case 'R':
      description += "CLI Regression ";
      break;
    case 'S':
      description += "CLI SYSIBM  ";
      break;
    case 'W':
      description += "CLI WIDE API  ";
      break;
    case 'Y':
      description += "SQ TESTS  ";
      break;
    case 'Z':
      description += "CLI ZOS  ";
      break;

    default:
      description += "UnknownEnv ";
      break;
    }
    /*
     * Did not work return "<a href=\"cgi-pase/JDRunit.acgi?INITIALS="+input+
     * "&TESTCASE=REPORT\">REGEN</a>"+input+" "+description;
     */
    return input + " " + description;
  }

  public static void createIndex() throws Exception {

    String filename = "ct/index.html";
    System.out.println("Creating index file " + filename);

    PrintWriter writer = new PrintWriter(new FileWriter(filename));

    if (hostname == null || hostname.length() == 0) {
      hostname = JDHostName.getHostName();
    }

    writer.print(
        "<head>\n" + "<title>" + hostname + " Regression Test Report</title>\n"
            + "</head>\n" + "\n" + "<body>\n");

    /* Get the list of file from ct */
    File directory = new File("ct");

    File[] files = directory.listFiles();

    TreeSet<String> sortedSet = new TreeSet<String>();
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        String name = files[i].getName();
        if (name.endsWith(".html")) {
          sortedSet.add(name);
        }
      }
    }
    Date date = new Date();
    writer.println(
        "<h1>" + hostname + " Regression Test Report as of  " + date + "</h1>");
    writer.println(
        "<input type=\"button\" value=\"Reload Page\" onClick=\"window.location.reload()\"> ");
    writer.print("<font color=\"darkgreen\"><b>Within 1 day</b></font>  ");
    writer.print("<font color=\"blue\">Within 3 days</font>  ");
    writer.print("<font color=\"red\">OLD</font> ");
    writer.println("<BR>");
    /*
     * writer.
     * println("<a href=\"cgi-pase/ProcessManager.acgi\">Process Manager</a> ");
     */
    writer.println(
        "<a href=\"cgi-pase/SchedulerManager.acgi\">Scheduler Manager</a> ");
    writer
        .println("<a href=\"cgi-pase/WrkIni.acgi\">Work INI configuration</a>");
    writer.println("<a href=\"jvm.html\">JVM view</a> ");
    writer.println("<a href=\"toolbox.html\">toolbox view</a> ");
    writer.println("<a href=\"native.html\">native view</a> ");
    writer.println("<br>");

    long oneWeekAgoTime = System.currentTimeMillis() - 7 * /* days / week */
        24 * /* hours / day */
        60 * /* min / hour */
        60 * /* sec / hour */
        1000; /* ms / sec */

    StringBuffer sb = new StringBuffer();
    sb.append("<h2>MUSTRUN RESULTS </h2>\n");
    sb.append("<table border>\n");
    sb.append(formatHeader());
    sb.append("\n");
    double totalRunMinutes = 0.0;
    Iterator<String> iterator = sortedSet.iterator();
    while (iterator.hasNext()) {
      String next = (String) iterator.next();
      if (isMustRun(next)) {
        String line = formatLine(next, null);
        totalRunMinutes += getRunMinutesFromLine(line);
        sb.append(line);
        sb.append("\n");
      }
    }
    sb.append("<tr><td><td>TotalRunMinutes=" + totalRunMinutes
        + " TotalRunHours=" + (totalRunMinutes / 60.0));
    sb.append("</table>\n");
    writer.println(sb.toString());

    totalRunMinutes = 0.0;

    String[] cliSuffixes = { "R.html", "S.html", "W.html", "Y.html", "Z.html" };
    totalRunMinutes += addSection(writer, sortedSet, "CLI TESTING", cliSuffixes,
        null);

    String[] jccSuffixes = { "J.html" };
    totalRunMinutes += addSection(writer, sortedSet, "JCC TESTING ",
        jccSuffixes, null);

    Timestamp nativeTimestamp = null;
    String nativeInfo = "db2_classes.jar:";
    try {
      File nativeJarFile = new File(
          "/QIBM/Proddata/OS400/Java400/ext/db2_classes.jar");
      long nativeJarFileLastModified = nativeJarFile.lastModified();
      nativeTimestamp = new Timestamp(nativeJarFileLastModified);
      Timestamp nativeBuildTimestamp = getBuildTimestamp(nativeJarFile);
      nativeInfo = nativeInfo + nativeTimestamp.toString() + " built="
          + nativeBuildTimestamp;

      if (oneWeekAgoTime > nativeJarFileLastModified) {
        nativeTimestamp = new Timestamp(oneWeekAgoTime);
      }

    } catch (Exception e) {
    }

    String[] jdbcNativeSuffixes = { "N.html", "M.html", "K.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JDBC NATIVE TESTING " + nativeInfo, jdbcNativeSuffixes,
        nativeTimestamp);

    nativeTimestamp = null;
    nativeInfo = "/home/jdbcsrcj/com/ibm/db2/jdbc/app/DB2Driver.class:";
    try {
      File nativeJarFile = new File(nativeInfo);
      long nativeJarFileLastModified = nativeJarFile.lastModified();
      nativeTimestamp = new Timestamp(nativeJarFileLastModified);
      nativeInfo = nativeInfo + nativeTimestamp.toString();

      if (oneWeekAgoTime > nativeJarFileLastModified) {
        nativeTimestamp = new Timestamp(oneWeekAgoTime);
      }

    } catch (Exception e) {
    }

    String[] jdbcNativeDebugSuffixes = { "D.html" };
    addSection(writer, sortedSet, "JDBC NATIVE DEBUG TESTING " + nativeInfo,
        jdbcNativeDebugSuffixes, nativeTimestamp);

    Timestamp jstpTimestamp = null;
    String jstpInfo = "db2_classes.jar:";
    try {
      File jstpJarFile = new File(
          "/QIBM/Proddata/OS400/Java400/ext/db2_classes.jar");
      long jstpJarFileLastModified = jstpJarFile.lastModified();
      jstpTimestamp = new Timestamp(jstpJarFileLastModified);

      jstpInfo = jstpInfo + jstpTimestamp.toString();

      if (oneWeekAgoTime > jstpJarFileLastModified) {
        jstpTimestamp = new Timestamp(oneWeekAgoTime);
      }

    } catch (Exception e) {
    }

    String[] jstpSuffixes = { "O.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JAVA STORED PROCEDURES TESTING " + jstpInfo, jstpSuffixes,
        jstpTimestamp);

    Timestamp toolboxTimestamp = null;
    Timestamp toolboxBuildTimestamp = null;
    String toolboxInfo = "No Jar Info";
    File toolboxFile;
    toolboxFile = new File("/qibm/proddata/http/public/jt400/lib/jt400.jar");
    if (!toolboxFile.exists())
      toolboxFile = new File("/qibm/proddata/os400/jt400/lib/jt400.jar");
    if (!toolboxFile.exists())
      toolboxFile = new File("jars/jt400.jar");
    if (toolboxFile.exists()) {
      toolboxBuildTimestamp = getBuildTimestamp(toolboxFile);
      long toolboxFileTime = toolboxFile.lastModified();

      toolboxTimestamp = new Timestamp(toolboxFileTime);
      toolboxInfo = toolboxFile.getPath() + "=" + toolboxTimestamp + " built="
          + toolboxBuildTimestamp;

      if (oneWeekAgoTime > toolboxFileTime) {
        toolboxTimestamp = new Timestamp(oneWeekAgoTime);
      }
    }

    Timestamp jtopenliteTimestamp = null;
    Timestamp jtopenliteBuildTimestamp = null;
    String jtopenliteInfo = "No Jar Info";
    File jtopenliteFile;
    jtopenliteFile = new File("jars/jtopenlite.jar");
    if (jtopenliteFile.exists()) {
      jtopenliteBuildTimestamp = getBuildTimestamp(jtopenliteFile);
      long jtopenliteFileTime = jtopenliteFile.lastModified();
      jtopenliteTimestamp = new Timestamp(jtopenliteFileTime);
      jtopenliteInfo = jtopenliteFile.getPath() + "=" + jtopenliteTimestamp
          + " built=" + jtopenliteBuildTimestamp;

      if (oneWeekAgoTime > jtopenliteFileTime) {
        jtopenliteTimestamp = new Timestamp(oneWeekAgoTime);
      }

    }

    Timestamp jt400androidTimestamp = null;
    Timestamp jt400androidBuildTimestamp = null;

    String jt400androidInfo = "No Jar Info";
    File jt400androidFile;
    jt400androidFile = new File("jars/jt400android.jar");
    if (jt400androidFile.exists()) {
      jt400androidBuildTimestamp = getBuildTimestamp(jt400androidFile);
      long jt400androidFileTime = jt400androidFile.lastModified();
      jt400androidTimestamp = new Timestamp(jt400androidFileTime);
      jt400androidInfo = jt400androidFile.getPath() + "="
          + jt400androidTimestamp + " built=" + jt400androidBuildTimestamp;

      if (oneWeekAgoTime > jt400androidFileTime) {
        jt400androidTimestamp = new Timestamp(oneWeekAgoTime);
      }

    }

    /*
    Timestamp jccTimestamp = null;
    Timestamp jccBuildTimestamp = null;
    String jccInfo = "No Jar Info";
    File jccFile;
    jccFile = new File("jars/db2jcc.jar");
    if (jccFile.exists()) {
      jccBuildTimestamp = getBuildTimestamp(jccFile);
      long jccFileLastModified = jccFile.lastModified();
      jccTimestamp = new Timestamp(jccFileLastModified);
      jccInfo = jccFile.getPath() + "=" + jccTimestamp + " built="
          + jccBuildTimestamp;

      if (oneWeekAgoTime > jccFileLastModified) {
        jccTimestamp = new Timestamp(oneWeekAgoTime);
      }

    }
    */

    String[] jdbcToolboxSuffixes = { "T.html", "X.html", "I.html", "H.html",
        "Q.html", "1.html", "2.html", "P.html", };
    totalRunMinutes += addSection(writer, sortedSet,
        "JDBC TOOLBOX TESTING " + toolboxInfo, jdbcToolboxSuffixes,
        toolboxTimestamp);

    String[] jdbcToolboxNativeSuffixes = { "U.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JDBC TOOLBOX NATIVE TESTING " + toolboxInfo, jdbcToolboxNativeSuffixes,
        toolboxTimestamp);

   
    
    String[] toolboxProxySuffixes = { "V.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JDBC TOOLBOX PROXY TESTING " + toolboxInfo, toolboxProxySuffixes,
        toolboxTimestamp);

    String[] toolboxSuffixes = { "A.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "TOOLBOX TESTING " + toolboxInfo, toolboxSuffixes, toolboxTimestamp);

    String[] toolboxNativeSuffixes = { "B.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "TOOLBOX NATIVE TESTING " + toolboxInfo, toolboxNativeSuffixes,
        toolboxTimestamp);

    String[] jtopenliteSuffixes = { "L.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JDBC jtopenlite TESTING " + jtopenliteInfo, jtopenliteSuffixes,
        jtopenliteTimestamp);

    String[] androidSuffixes = { "G.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JDBC toolbox android TESTING " + jt400androidInfo, androidSuffixes,
        jt400androidTimestamp);

    
    String[] java11Strings = { "B6","BO" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JAVA 11 TESTING " + toolboxInfo+ " " + nativeInfo, java11Strings,
        toolboxTimestamp);

    String[] java17Strings = { "C6" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JAVA 17 TESTING " + toolboxInfo+ " " + nativeInfo, java17Strings,
        toolboxTimestamp);


    writer.println("<hr>Total Run Minutes=" + totalRunMinutes
        + " Total run hours=" + (totalRunMinutes / 60) + " Total run days="
        + (totalRunMinutes / 1440) + "\n");
    writer.println(
        "<hr>Note:  This report is regenerated when any of the individual reports are generated<br>");
    writer.println(
        "The source for this report generator can be found under /as400/v7r1m0t.jacl/cur/cmvc/java.pgm/yjac.jacl<br>");

    writer.println("</body>");
    writer.println("</html>");

    writer.close();

  }

  public static void createToolboxIndex() throws Exception {

    String filename = "ct/toolbox.html";
    System.out.println("Creating index file " + filename);

    PrintWriter writer = new PrintWriter(new FileWriter(filename));

    if (hostname == null || hostname.length() == 0) {
      hostname =  JDHostName.getHostName();
      int dotIndex = hostname.indexOf('.');
      if (dotIndex > 0) {
        hostname = hostname.substring(0, dotIndex);
      }
    }

    writer.print("<head>\n" + "<title>" + hostname
        + " Toolbox Regression Test Report</title>\n" + "</head>\n" + "\n"
        + "<body>\n");

    /* Get the list of file from ct */
    File directory = new File("ct");

    File[] files = directory.listFiles();

    TreeSet<String> sortedSet = new TreeSet<String>();

    for (int i = 0; i < files.length; i++) {
      String name = files[i].getName();
      if (name.endsWith(".html")) {
        sortedSet.add(name);
      }
    }
    Date date = new Date();
    writer.println("<h1>" + hostname + " Toolbox Regression Test Report as of  "
        + date + "</h1>");
    writer.println(
        "<input type=\"button\" value=\"Reload Page\" onClick=\"window.location.reload()\"> ");
    writer.print("<font color=\"darkgreen\"><b>Within 1 day</b></font>  ");
    writer.print("<font color=\"blue\">Within 3 days</font>  ");
    writer.print("<font color=\"red\">OLD</font> ");
    writer.println("<BR>");
    /*
     * writer.
     * println("<a href=\"cgi-pase/ProcessManager.acgi\">Process Manager</a> ");
     */
    writer.println(
        "<a href=\"cgi-pase/SchedulerManager.acgi\">Scheduler Manager</a> ");
    writer.println("<a href=\"jvm.html\">JVM view</a> ");
    writer.println("<a href=\"native.html\">Native view</a> ");
    writer.println("<a href=\"index.html\">base view</a> ");
    writer.println("<br>");

    long oneWeekAgoTime = System.currentTimeMillis() - 7 * /* days / week */
        24 * /* hours / day */
        60 * /* min / hour */
        60 * /* sec / hour */
        1000; /* ms / sec */

    double totalRunMinutes = 0.0;

    Timestamp toolboxTimestamp = null;
    String toolboxInfo = "No Jar Info";
    File toolboxFile;
    toolboxFile = new File("/qibm/proddata/http/public/jt400/lib/jt400.jar");
    if (!toolboxFile.exists())
      toolboxFile = new File("/qibm/proddata/os400/jt400/lib/jt400.jar");
    if (!toolboxFile.exists())
      toolboxFile = new File("jars/jt400.jar");
    if (toolboxFile.exists()) {
      long toolboxFileTime = toolboxFile.lastModified();

      toolboxTimestamp = new Timestamp(toolboxFileTime);
      toolboxInfo = toolboxFile.getPath() + "=" + toolboxTimestamp;

      if (oneWeekAgoTime > toolboxFileTime) {
        toolboxTimestamp = new Timestamp(oneWeekAgoTime);
      }

    }

    Timestamp jtopenliteTimestamp = null;
    String jtopenliteInfo = "No Jar Info";
    File jtopenliteFile;
    jtopenliteFile = new File("jars/jtopenlite.jar");
    if (jtopenliteFile.exists()) {
      long jtopenliteFileTime = jtopenliteFile.lastModified();
      jtopenliteTimestamp = new Timestamp(jtopenliteFileTime);
      jtopenliteInfo = jtopenliteFile.getPath() + "=" + jtopenliteTimestamp;

      if (oneWeekAgoTime > jtopenliteFileTime) {
        jtopenliteTimestamp = new Timestamp(oneWeekAgoTime);
      }

    }

    Timestamp jt400androidTimestamp = null;
    String jt400androidInfo = "No Jar Info";
    File jt400androidFile;
    jt400androidFile = new File("jars/jt400android.jar");
    if (jt400androidFile.exists()) {
      long jt400androidFileTime = jt400androidFile.lastModified();
      jt400androidTimestamp = new Timestamp(jt400androidFileTime);
      jt400androidInfo = jt400androidFile.getPath() + "="
          + jt400androidTimestamp;

      if (oneWeekAgoTime > jt400androidFileTime) {
        jt400androidTimestamp = new Timestamp(oneWeekAgoTime);
      }

    }

    String[] jdbcToolboxSuffixes = { "T.html", "X.html", "Q.html", "H.html",
        "I.html", "1.html", "2.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JDBC TOOLBOX TESTING " + toolboxInfo, jdbcToolboxSuffixes,
        toolboxTimestamp);

    String[] jdbcToolboxNativeSuffixes = { "U.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JDBC TOOLBOX NATIVE TESTING " + toolboxInfo, jdbcToolboxNativeSuffixes,
        toolboxTimestamp);

    String[] toolboxProxySuffixes = { "V.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JDBC TOOLBOX PROXY TESTING " + toolboxInfo, toolboxProxySuffixes,
        toolboxTimestamp);

    String[] toolboxSuffixes = { "A.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "TOOLBOX TESTING " + toolboxInfo, toolboxSuffixes, toolboxTimestamp);

    String[] toolboxNativeSuffixes = { "B.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "TOOLBOX NATIVE TESTING " + toolboxInfo, toolboxNativeSuffixes,
        toolboxTimestamp);

    String[] jtopenliteSuffixes = { "L.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JDBC jtopenlite TESTING " + jtopenliteInfo, jtopenliteSuffixes,
        jtopenliteTimestamp);

    String[] androidSuffixes = { "G.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JDBC toolbox android TESTING " + jt400androidInfo, androidSuffixes,
        jt400androidTimestamp);

    writer.println("<hr>Total Run Minutes=" + totalRunMinutes
        + " Total run hours=" + (totalRunMinutes / 60) + " Total run days="
        + (totalRunMinutes / 1440) + "\n");
    writer.println(
        "<hr>Note:  This report is regenerated when any of the individual reports are generated<br>");
    writer.println(
        "The source for this report generator can be found under /as400/v7r1m0t.jacl/cur/cmvc/java.pgm/yjac.jacl<br>");

    writer.println("</body>");
    writer.println("</html>");

    writer.close();

  }

  public static void createNativeIndex() throws Exception {

    String filename = "ct/native.html";
    System.out.println("Creating index file " + filename);

    PrintWriter writer = new PrintWriter(new FileWriter(filename));

    if (hostname == null || hostname.length() == 0) {
      hostname = JDHostName.getHostName();
      int dotIndex = hostname.indexOf('.');
      if (dotIndex > 0) {
        hostname = hostname.substring(0, dotIndex);
      }
    }

    writer.print("<head>\n" + "<title>" + hostname
        + " Native Regression Test Report</title>\n" + "</head>\n" + "\n"
        + "<body>\n");

    /* Get the list of file from ct */
    File directory = new File("ct");

    File[] files = directory.listFiles();

    TreeSet<String> sortedSet = new TreeSet<String>();

    for (int i = 0; i < files.length; i++) {
      String name = files[i].getName();
      if (name.endsWith(".html")) {
        sortedSet.add(name);
      }
    }
    Date date = new Date();
    writer.println("<h1>" + hostname + " Native Regression Test Report as of  "
        + date + "</h1>");
    writer.println(
        "<input type=\"button\" value=\"Reload Page\" onClick=\"window.location.reload()\"> ");
    writer.print("<font color=\"darkgreen\"><b>Within 1 day</b></font>  ");
    writer.print("<font color=\"blue\">Within 3 days</font>  ");
    writer.print("<font color=\"red\">OLD</font> ");
    writer.println("<BR>");
    /*
     * writer.
     * println("<a href=\"cgi-pase/ProcessManager.acgi\">Process Manager</a> ");
     */
    writer.println(
        "<a href=\"cgi-pase/SchedulerManager.acgi\">Scheduler Manager</a> ");
    writer.println("<a href=\"jvm.html\">JVM view</a> ");
    writer.println("<a href=\"toolbox.html\">toolbox view</a> ");
    writer.println("<a href=\"index.html\">base view</a> ");
    writer.println("<br>");

    long oneWeekAgoTime = System.currentTimeMillis() - 7 * /* days / week */
        24 * /* hours / day */
        60 * /* min / hour */
        60 * /* sec / hour */
        1000; /* ms / sec */

    double totalRunMinutes = 0.0;

    Timestamp nativeTimestamp = null;
    String nativeInfo = "db2_classes.jar:";
    try {
      File nativeJarFile = new File(
          "/QIBM/Proddata/OS400/Java400/ext/db2_classes.jar");
      long nativeJarFileLastModified = nativeJarFile.lastModified();
      nativeTimestamp = new Timestamp(nativeJarFileLastModified);
      Timestamp nativeBuildTimestamp = getBuildTimestamp(nativeJarFile);
      nativeInfo = nativeInfo + nativeTimestamp.toString() + " built="
          + nativeBuildTimestamp;

      if (oneWeekAgoTime > nativeJarFileLastModified) {
        nativeTimestamp = new Timestamp(oneWeekAgoTime);
      }

    } catch (Exception e) {
    }

    String[] jdbcNativeSuffixes = { "N.html", "P.html", "M.html" };
    totalRunMinutes += addSection(writer, sortedSet,
        "JDBC NATIVE TESTING " + nativeInfo, jdbcNativeSuffixes,
        nativeTimestamp);

    writer.println("<hr>Total Run Minutes=" + totalRunMinutes
        + " Total run hours=" + (totalRunMinutes / 60) + " Total run days="
        + (totalRunMinutes / 1440) + "\n");
    writer.println(
        "<hr>Note:  This report is regenerated when any of the individual reports are generated<br>");
    writer.println(
        "The source for this report generator can be found under /as400/v7r1m0t.jacl/cur/cmvc/java.pgm/yjac.jacl<br>");

    writer.println("</body>");
    writer.println("</html>");

    writer.close();

  }

  public static void createJvmIndex() throws Exception {

    String filename = "ct/jvm.html";
    System.out.println("Creating index file " + filename);

    PrintWriter writer = new PrintWriter(new FileWriter(filename));

    if (hostname == null || hostname.length() == 0) {
      hostname = JDHostName.getHostName();
      int dotIndex = hostname.indexOf('.');
      if (dotIndex > 0) {
        hostname = hostname.substring(0, dotIndex);
      }
    }

    writer.print("<head>\n" + "<title>" + hostname
        + " Regression Test Report By JVM </title>\n" + "</head>\n" + "\n"
        + "<body>\n");

    /* Get the list of file from ct */
    File directory = new File("ct");

    File[] files = directory.listFiles();

    TreeSet<String> sortedSet = new TreeSet<String>();
    TreeSet<String> jvmSortedSet = new TreeSet<String>();

    for (int i = 0; (files != null) && (i < files.length); i++) {
      String name = files[i].getName();
      if (name.endsWith(".html")) {
        sortedSet.add(name);
        // System.out.println("adding name "+name );
        int latestIndex = name.indexOf("latest");
        if (latestIndex >= 0) {
          String remaining = name.substring(latestIndex + 6);
          if (remaining.length() > 4) {
            jvmSortedSet.add(remaining.substring(2, 4));
            // System.out.println("Adding "+remaining.substring(2,4));
          } else {
            // System.out.println("Did not add "+name);
          }
        }
      }
    }
    Date date = new Date();
    String as400desc = (String) iniProperties
        .get("INFO_" + hostname.toUpperCase());
    if (as400desc == null) {
      as400desc = "";
    } else {
      as400desc = " (" + as400desc + ") ";
    }

    writer.println("<h1>" + hostname + as400desc
        + " Regression Test Report as of  " + date + "</h1>");
    writer.println(
        "<input type=\"button\" value=\"Reload Page\" onClick=\"window.location.reload()\"> ");
    writer.print("<font color=\"darkgreen\"><b>Within 1 day</b></font>  ");
    writer.print("<font color=\"blue\">Within 3 days</font>  ");
    writer.print("<font color=\"red\">OLD</font> ");
    writer.println("<BR>");
    /*
     * writer.
     * println("<a href=\"cgi-pase/ProcessManager.acgi\">Process Manager</a> ");
     */
    writer.println(
        "<a href=\"cgi-pase/SchedulerManager.acgi\">Scheduler Manager</a><br>");
    writer.println("<a href=\"native.html\">native view</a> ");
    writer.println("<a href=\"toolbox.html\">toolbox view</a> ");
    writer.println("<a href=\"index.html\">base view</a> ");
    writer.println("<br>");

    Iterator<String> iterator = jvmSortedSet.iterator();
    while (iterator.hasNext()) {

      String jvm = (String) iterator.next();
      // System.out.println("Processing JVM "+jvm);
      addJvmSection(writer, sortedSet, "JVM " + jvm, jvm, null);

    }

    writer.println(
        "<hr>Note:  This report is regenerated when any of the individual reports are generated<br>");
    writer.println(
        "The source for this report generator can be found under /as400/v7r1m0t.jacl/cur/cmvc/java.pgm/yjac.jacl<br>");

    writer.println("</body>");
    writer.println("</html>");

    writer.close();

  }

  // return the run minutes
  public static double addJvmSection(PrintWriter writer, TreeSet<String> sortedSet,
      String header, String jvm, Timestamp jarTimestamp) {
    int sectionCount = 0;
    StringBuffer sb = new StringBuffer();
    double totalRunMinutes = 0.0;
    sb.append("<h2>" + header + "</h2>\n");
    sb.append("<table border>\n");
    sb.append(formatHeader());
    sb.append("\n");
    Iterator<String> iterator = sortedSet.iterator();
    while (iterator.hasNext()) {
      String next = (String) iterator.next();
      // System.out.println("... looking at "+next);
      int latestIndex = next.indexOf("latest");
      if (latestIndex >= 0) {
        if (next.length() > latestIndex + 10) {
          String thisJvm = next.substring(latestIndex + 8, latestIndex + 10);
          // System.out.println("This JVM ="+thisJvm+" compare to "+jvm);
          if (thisJvm.equals(jvm)) {
            String line = formatLine(next, jarTimestamp);
            sb.append(line);
            totalRunMinutes += getRunMinutesFromLine(line);

            sb.append("\n");
            sectionCount++;
          }
        }
      }
    }
    double totalRunHours = ((long) (100 * totalRunMinutes / 60.0)) / 100.0;
    sb.append("<tr><td><td>TotalRunMinutes=" + totalRunMinutes
        + " TotalRunHours=" + totalRunHours);

    sb.append("</table>\n");

    if (sectionCount > 0) {
      writer.println(sb.toString());
    }
    return totalRunMinutes;
  }

  // return the run minutes
  public static double addSection(PrintWriter writer, TreeSet<String> sortedSet,
      String header, String[] suffixes, Timestamp jarTimestamp) {
    int sectionCount = 0;
    StringBuffer sb = new StringBuffer();
    double totalRunMinutes = 0.0;
    sb.append("<h2>" + header + "</h2>\n");
    sb.append("<table border>\n");
    sb.append(formatHeader());
    sb.append("\n");
    Iterator<String> iterator = sortedSet.iterator();
    while (iterator.hasNext()) {
      String next = (String) iterator.next();
      for (int i = 0; i < suffixes.length; i++) {
        if ((next.indexOf(suffixes[i]) > 0)) {
          String line = formatLine(next, jarTimestamp);
          sb.append(line);
          totalRunMinutes += getRunMinutesFromLine(line);

          sb.append("\n");
          sectionCount++;
        }
      }
    }
    double totalRunHours = ((long) (100 * totalRunMinutes / 60.0)) / 100.0;
    sb.append("<tr><td><td>TotalRunMinutes=" + totalRunMinutes
        + " TotalRunHours=" + totalRunHours);

    sb.append("</table>\n");

    if (sectionCount > 0) {
      writer.println(sb.toString());
    }
    return totalRunMinutes;
  }

  private static double getRunMinutesFromLine(String line) {
    int lastTd = line.lastIndexOf("<td>");
    if (lastTd > 0) {
      String stringValue = line.substring(lastTd + 4);
      try {
        return Double.parseDouble(stringValue);
      } catch (NumberFormatException nfe) {
        return 0.0;
      }
    }
    return 0;
  }

  private static Properties mustRunProperties = null;

  /**
   * next is either latestXXXXX.zzz or XXXXX
   */
  public static boolean isMustRun(String next) {
    if (mustRunProperties == null) {
      String filename = "ini/mustrun.ini";
      Properties newMustRunProperties = null;
      try {
        newMustRunProperties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(filename);
        newMustRunProperties.load(fileInputStream);
        fileInputStream.close();
      } catch (Exception e) {
        if (newMustRunProperties == null) {
          newMustRunProperties = new Properties();
        }
      }

      mustRunProperties = newMustRunProperties;
    }
    // remove beginning "latest"
    if (next.indexOf("latest") == 0) {
      next = next.substring(6);
    }
    int dotIndex = next.indexOf('.');
    if (dotIndex > 0) {
      next = next.substring(0, dotIndex);
    }
    boolean rc = (mustRunProperties.get(next) != null);
    /* System.out.println("Debug: Checking "+next+" rc="+rc); */

    return rc;
  }

  public static void usage() {
    System.out.println(
        "Usage:  java test.JDReport <INITIALS> [RESET_REGRESSION <DAYS>]");
  }

  public static void exUpIE(Statement s, String sql) {
    try {
      if (debug)
        System.out.println("exUpIE: " + sql);
      s.executeUpdate(sql);
    } catch (Exception e) {
      if (debug) {
        System.out.println("Caught ignored exception");
        System.out.flush();
        e.printStackTrace();
        System.err.flush();
      }
    }
  }

  public static void exUp(Statement s, String sql) throws SQLException {
    try {
      if (debug)
        System.out.println("exUpIE: " + sql);
      s.executeUpdate(sql);
    } catch (SQLException e) {
      if (debug) {
        System.out.println("Caught exception");
        System.out.flush();
        e.printStackTrace();
        System.err.flush();
      }
      throw e;
    }
  }

  public static ResultSet exQ(PrintStream writer, Statement s, String sql)
      throws SQLException {
    try {
      outputQuery(writer, sql);
      if (debug)
        System.out.println("exQ: " + sql);
      return s.executeQuery(sql);
    } catch (SQLException e) {
      if (debug) {
        System.out.println("Caught exception");
        System.out.flush();
        e.printStackTrace();
        System.err.flush();
      }
      throw e;
    }
  }

  public static int nextNonSpace(String line, int spaceIndex) {
    int len = line.length();
    while (spaceIndex < len && line.charAt(spaceIndex) == ' ') {
      spaceIndex++;
    }
    return spaceIndex;
  }

  static int queryCount = 0;

  public static void outputQuery(PrintStream writer, String query) {
    queryCount++;
    writer.println("<div id=\"query" + queryCount
        + "\" style=\"overflow:hidden;display:none\">");
    query = query.replace('\n', ' ');
    writer.println("<b>QUERY=</b>" + query);
    writer.println("</div>");
  }

  public static void readIni(String initials) throws Exception {

	StringBuffer sb = new StringBuffer(); 
    iniProperties = JDRunit.getIniProperties(initials, sb);


    AS400 = iniProperties.getProperty("AS400");
    if (AS400 == null) {
      if (JTOpenTestEnvironment.isOS400) {
        AS400 = JDHostName.getHostName();
      } else {
        AS400 = iniProperties.getProperty(initials);
        if (AS400 == null) {
          AS400 = iniProperties.getProperty(initials.substring(0, 4));
          if (AS400 == null) {
            AS400 = iniProperties.getProperty(initials.substring(0, 2));
          }
        }
      }
    }
    if (AS400 == null) {
      AS400 = "UNKNOWN -- checkin runit" + initials + "ini and systems.ini for "
          + initials;
    } else {
      AS400 = AS400.toLowerCase();
    }

  }

  public static Timestamp getBuildTimestamp(File inJarFile) throws IOException {
    JarFile jarfile = new JarFile(inJarFile);
    Enumeration<?> entries = jarfile.entries();
    long maxTime = 0;
    while (entries.hasMoreElements()) {
      JarEntry entry = (JarEntry) entries.nextElement();
      long entryTime = entry.getTime();
      if (entryTime > maxTime)
        maxTime = entryTime;
    }
    jarfile.close(); 
    return new Timestamp(maxTime);
  }

  public static void main(String args[]) {
    PrintStream writer = null;
    queryCount = 0;
    try {
      boolean resetRegression = false;
      long regressionDays = 0;
      long regressionEarliestTime = 0;
      Vector<String> newRunitOut = null;
      ClassLoader loader = null;
      Driver driver = null;
      boolean on400 = false;
      String initials = args[0];
      readIni(initials);
      if (args.length >= 3) {
        if (args[1].equals("RESET_REGRESSION")) {
          resetRegression = true;
          regressionDays = Integer.parseInt(args[2]);
          if (regressionDays <= 0) {
            throw new Exception(
                "regressionDays of '" + args[2] + "' is invalid");
          }
          long currentTime = System.currentTimeMillis();
          System.out.println("Current Date is " + new Date(currentTime));
          System.out.println("Current ts   is " + new Timestamp(currentTime));
          regressionEarliestTime = currentTime
              - regressionDays * 24 * 60 * 60 * 1000;
          Timestamp ts = new Timestamp(regressionEarliestTime);
          System.out.println("Keeping older than " + ts);
          newRunitOut = new Vector<String>();
        } else {
          throw new Exception("Unrecognized option " + args[1]);
        }
      }

      String USERID = iniProperties.getProperty("USERID");
      char[] encryptedPassword; 
      {
      String PASSWORD = JDRunit.getPropertyPassword(USERID); 
      if (PASSWORD == null) PASSWORD = iniProperties.getProperty("PASSWORD");
      encryptedPassword = PasswordVault.getEncryptedPassword(PASSWORD);
      }
      
      String MASTERUSERID = iniProperties.getProperty("MASTERUSERID");
      char[] encryptedMasterPassword; 
      {
        String MASTERPASSWORD = JDRunit.getPropertyPassword(MASTERUSERID); 
        if (MASTERPASSWORD == null) MASTERPASSWORD = iniProperties.getProperty("MASTERPASSWORD");
        encryptedMasterPassword = PasswordVault.getEncryptedPassword(MASTERPASSWORD); 
      } 
      String description = (String) iniProperties.get("description");
      if (description == null) {
        description = "Description not set in runit" + initials + ".ini";
      }
      String outputFilename = getReportName(initials);
      String outfile = "ct/runit" + initials + ".out";
      System.out.println("Creating " + outputFilename);

      writer = new PrintStream(new FileOutputStream(outputFilename));

      //
      // Print the header information
      //
      writer.println("Content-Type: text/html");
      writer.println("");
      String info;
      String as400desc = (String) iniProperties
          .get("INFO_" + AS400.toUpperCase());
      if (as400desc == null) {
        as400desc = "";
      } else {
        as400desc = " -- " + as400desc;
      }

      info = initials + " " + description + " --  CT Status for " + AS400
          + as400desc + " --  " + (new Date()).toString();
      writer.println("<HEAD>" + "<script type=\"text/javascript\">\n"
          + "function expandQueries(x) {\n" + " for (i = 1 ;i < 30; i++) {\n"
          + "   var expandable = document.getElementById('query'+i);\n"
          + "   if (expandable != null) { \n"
          + "    expandable.style.display = x;\n" + "   }\n" + " }\n" + "}\n"
          + "</script>\n");

      writer.println("  <TITLE>" + info + " </TITLE>");
      writer.println("</HEAD>");
      writer.println("  <H1>" + info + " </H1>");
      writer.println("  <a href=\"..\">UP</a> ");
      writer.println(
          "<input type=\"button\" value=\"Reload Page\" onClick=\"window.location.reload()\"> ");
      writer.println("  <a href=\"cgi-pase/JDReport.acgi?INITIALS=" + initials
          + "&DAYS=0\">REGENERATE</a>  ");
      writer.println("  <a href=\"cgi-pase/JDReport.acgi?INITIALS=" + initials
          + "&DAYS=14\">REGENERATE14</a> ");
      writer.println(
          "  <a href=\"cgi-pase/QueryByTestcase.acgi\">QUERY_BY_TESTCASE</a> ");
      writer.println(
          "  <a href=\"javascript:expandQueries('block')\">Expand Queries</a>");
      writer.println(
          "  <a href=\"javascript:expandQueries('none')\">Collapse Queries</a>");
      writer.println("<br>");
      /*
       * writer.
       * println("<a href=\"cgi-pase/ProcessManager.acgi\">Process Manager</a>"
       * );
       */
      writer.println(
          "<a href=\"cgi-pase/SchedulerManager.acgi\">Scheduler Manager</a>");

      writer.println("<a href=\"cgi-pase/JDRunit.acgi?PRIORITY=1&INITIALS="
          + initials + "&TESTCASE=REGRESSIONPLUS\">Submit REGRESSIONPLUS</a> ");

      writer.println("<a href=\"cgi-pase/JDRunit.acgi?PRIORITY=1&INITIALS="
          + initials + "&TESTCASE=REGRESSION\">Submit REGRESSION</a> ");
      writer.println("<a href=\"cgi-pase/JDRunit.acgi?PRIORITY=2&INITIALS="
          + initials + "&TESTCASE=REPORT\">Submit REPORT</a> ");
      writer.println(
          "<a href=\"cgi-pase/JDRunit.acgi?PRIORITY=3&INITIALS=" + initials
              + "&TESTCASE=RERUNFAILEDPLUS\">Submit RERUNFAILEDPLUS</a> ");
      writer.println("<a href=\"cgi-pase/JDRunit.acgi?PRIORITY=3&INITIALS="
          + initials + "&TESTCASE=RERUNFAILED\">Submit RERUNFAILED</a> ");
      writer.println("<a href=\"cgi-pase/JDRunit.acgi?PRIORITY=4&INITIALS="
          + initials + "&TESTCASE=REPORT\">Submit REPORT</a> ");
      writer.println("<a href=\"cgi-pase/JDRunit.acgi?PRIORITY=5&INITIALS="
          + initials + "&TESTCASE=EMAIL\">Submit EMAIL</a> ");

      writer.println("<br>");

        on400 = JTOpenTestEnvironment.isOS400;

      Connection connection = null;

      if (on400) {
        Class.forName("com.ibm.db2.jdbc.app.DB2Driver");
        String PASSWORD = PasswordVault.decryptPasswordLeak(encryptedPassword) ; 
        try {
          connection = DriverManager.getConnection("jdbc:db2:*LOCAL", USERID,
              PASSWORD);
        } catch (Exception e) {
          System.out.println("Failed to connect using "+USERID+","+PASSWORD); 
          String eMessage = e.toString().toLowerCase();
          if (eMessage.indexOf("authorization failure") < 0) {
            writer.close(); 
            throw e;
          } else {
            JDRunit.resetId("jdbc:db2:*LOCAL", MASTERUSERID, encryptedMasterPassword,
                USERID, encryptedPassword);
            /* try again to get connection */
            connection = DriverManager.getConnection("jdbc:db2:*LOCAL", USERID,
                PASSWORD);
          }
        }
      } else {
        try {
          Class.forName("com.ibm.as400.access.AS400JDBCDriver");

        } catch (ClassNotFoundException cnfe) {
          // Load it manually

          // Add a classloader to the system to find the classes..
          // Looks for the classes in known locations
          URL[] urls = new URL[1];
          // Look for activation.jar
          String[] jt400JarLocations = { JTOpenTestEnvironment.testcaseHomeDirectory+"/jars/jt400.jar",
              "jars/jt400.jar", };
          for (int i = 0; i < jt400JarLocations.length
              && urls[0] == null; i++) {
            File tryFile = new File(jt400JarLocations[i]);
            if (tryFile.exists()) {
              urls[0] = new URL("file:" + jt400JarLocations[i]);
            }
          }

          if (urls[0] == null) {
            System.out.println("Error:  Unable to find jar files. Checked ");
            for (int i = 0; i < jt400JarLocations.length; i++) {
              System.out.println(jt400JarLocations[i]);
            }
          }

          loader = new URLClassLoader(urls);
          Class<?> driverClass = loader
              .loadClass("com.ibm.as400.access.AS400JDBCDriver");

          Class<?>[] parameterTypes = new Class<?>[0];

          Constructor<?> driverConstructor = driverClass
              .getConstructor(parameterTypes);

          Object[] parameters = new Object[0];
          System.out.println("Getting Driver from " + urls[0]);
          driver = (Driver) driverConstructor.newInstance(parameters);

          System.out.println("Registering driver " + driver);
          DriverManager.registerDriver(driver);
          System.out.println("Registered drivers");
          Enumeration<?> enumeration = DriverManager.getDrivers();
          while (enumeration.hasMoreElements()) {
            Object x = enumeration.nextElement();
            System.out.println(x);
          }
          System.out.println("-------------");
        }
        String PASSWORD = PasswordVault.decryptPasswordLeak(encryptedPassword) ; 

        try {
          System.out.println("Connecting to " + AS400);
          // Use keep alive=true for the toolbox driver. This will be ignored by
          // the native driver
          connection = DriverManager.getConnection(
              "jdbc:as400:" + AS400
                  + ";keep alive=true;thread used=false;prompt=false",
              USERID, PASSWORD);
        } catch (SQLException sqlex) {
          String message = sqlex.toString();
          if (message.indexOf("No suitable") >= 0) {
            System.out.println("Unable to find driver");
            sqlex.printStackTrace(System.out);
            System.out.println("loaded drivers are ");
            Enumeration<?> enumeration = DriverManager.getDrivers();
            while (enumeration.hasMoreElements()) {
              Object x = enumeration.nextElement();
              System.out.println(x);
            }
          } else {
            sqlex.printStackTrace(System.out);
          }
          if (driver != null) {
            Properties properties = new Properties();
            properties.put("user", USERID);
            properties.put("password", PASSWORD);
            String CONNECTNAME = AS400;
            try {
              connection = driver.connect("jdbc:as400:" + CONNECTNAME
                  + ";keep alive=true;thread used=false", properties);
            } catch (Exception e) {
              CONNECTNAME = AS400 + ".rch.stglabs.ibm.com";
              try {
                connection = driver.connect("jdbc:as400:" + CONNECTNAME
                    + ";keep alive=true;thread used=false", properties);
              } catch (Exception e2) {
                System.out
                    .println("Error:  Unable to connect to " + CONNECTNAME);

              }

            }
          }

        }

      }

      //
      // Call setNetworkTimeout via reflection if possible
      //
      if ((connection != null)
          && connection.getClass().getName().indexOf("AS400JDBC") > 0) {
        try {
          JDReflectionUtil.callMethod_V(connection, "setNetworkTimeout",
              3600000);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      String sql;
      Statement s = null;
      if (connection != null) {
        s = connection.createStatement();
      }
      ResultSet rs;

      String TESTSYSTEM = AS400;
      int dotIndex = AS400.indexOf(".");
      if (dotIndex > 0) {
        TESTSYSTEM = AS400.substring(0, dotIndex);
      }
      if (connection == null) {
        writer.close();
        throw new Exception("connection is null");

      }

      Timestamp compareTimestamp = null;
      String query = null;
      if (true) {

        System.out.print(
            "Generating information -- STEP 1 --  Checking PTF levels on System ");

        // String JDBCMAXPTF = "2020-12-31 23:49:00";

        exUp(s,
            "create table qtemp.ptfinfo( \"COMP\" varchar(40), \"PTF ID\" varchar(40), \"Change Date\" varchar(40) , \"Change Time\" varchar(40) )");

        if (isToolbox(initials)) {
          exUp(s,
              "call QSYS.QCMDEXC('DSPOBJD OBJ(QSYS/QZDASRV) OBJTYPE(*SRVPGM) DETAIL(*SERVICE) OUTPUT(*OUTFILE) OUTFILE(QGPL/QJVAJDBCI)      ' , 0000000106.00000)");
          exUp(s, "GRANT ALL ON  QGPL.QJVAJDBCI TO PUBLIC WITH GRANT OPTION");
          exUp(s,
              "INSERT INTO QTEMP.PTFINFO SELECT 'ZDA  ' ,  ODPTFN , '20' || SUBSTRING(ODLDAT,5,2) || '-' || SUBSTRING(ODLDAT,1,2) || '-' || SUBSTRING(ODLDAT,3,2)  , SUBSTRING(ODLTIM,1,2) || ':' || SUBSTRING(ODLTIM,3,2) || ':' || SUBSTRING(ODLTIM,5,2)   FROM qgpl.qjvajdbci    ");

          /* only rebuild the DSPPTF file if older than 1 day */

          boolean runDspptf = true;
          boolean fileExists = false;
          sql = "select LAST_ALTERED_TIMESTAMP from qsys2.systables where TABLE_NAME='DSPPTF' and TABLE_SCHEMA='JDTESTINFO'";
          rs = s.executeQuery(sql);

          Timestamp ts = null;
          if (rs.next()) {
            fileExists = true;
            ts = rs.getTimestamp(1);
            long tsMillis = ts.getTime();
            if (tsMillis > System.currentTimeMillis() - 24 * 3600000) {
              runDspptf = false;
            }
          }
          boolean doInsert = true; 
          if (runDspptf) {
            System.out.println("Running DSPPTF to create JDTESTINFO/DSPPTF");
            if (fileExists)
              exUp(s, "call qsys2.qcmdexc('DLTF JDTESTINFO/DSPPTF')");
            try {
              exUp(s,
                  "call qsys2.qcmdexc('DSPPTF LICPGM(5770SS1) OUTPUT(*OUTFILE) OUTFILE(JDTESTINFO/DSPPTF)')");
            } catch (Exception e) {
              String message = e.getMessage();
              if (message.indexOf("No PTF activity exists") < 0) {
                writer.close(); 
                throw e;
              } else {
                doInsert = false; 
              }
            }
          } else {
            System.out.println(
                "Not running DSPPTF to create JDTESTINFO/DSPPTF since ts="
                    + ts);
          }
          if (doInsert) 
            exUp(s,
              "insert into qtemp.ptfinfo   select 'TBX', a.SCPTFID,'20' || substring(a.scstdate,7,2) || '-' || substring(a.scstdate,1,2) || '-' || substring(a.scstdate,4,2),a.scsttime from JDTESTINFO.dspptf a, JDTESTINFO.dspptf b where (b.SCPTFID='SI69695' or b.SCPTFID='SI61258' or b.SCPTFID='SI61255' or b.SCPTFID='SI79326' ) and a.SCPTFID=b.SCSPTF");

        }
        if (isNativeJDBC(initials)) {
          exUp(s,
              "call QSYS.QCMDEXC('DSPOBJD OBJ(QSYSDIR/QJVAJDBC) OBJTYPE(*SRVPGM) DETAIL(*SERVICE) OUTPUT(*OUTFILE) OUTFILE(QGPL/QJVAJDBCI)      ' , 0000000106.00000)");
          exUp(s, "GRANT ALL ON  QGPL.QJVAJDBCI TO PUBLIC WITH GRANT OPTION");
          exUp(s,
              "INSERT INTO QTEMP.PTFINFO SELECT 'JDBC ' ,  ODPTFN , '20' || SUBSTRING(ODLDAT,5,2) || '-' || SUBSTRING(ODLDAT,1,2) || '-' || SUBSTRING(ODLDAT,3,2)  , SUBSTRING(ODLTIM,1,2) || ':' || SUBSTRING(ODLTIM,3,2) || ':' || SUBSTRING(ODLTIM,5,2)   FROM qgpl.qjvajdbci    ");
        }

        if (isNativeJDBC(initials) || isCli(initials) || isJavaStoredProcedures(initials)) {

          exUp(s,
              "call QSYS.QCMDEXC('DSPOBJD OBJ(QSYS/QSQCLI) OBJTYPE(*SRVPGM) DETAIL(*SERVICE) OUTPUT(*OUTFILE) OUTFILE(QGPL/QSQCLII)          ' , 0000000106.00000)");
          exUp(s, "GRANT ALL ON QGPL.QSQCLII TO PUBLIC WITH GRANT OPTION");
          exUp(s,
              "INSERT INTO QTEMP.PTFINFO SELECT 'CLI ' , ODPTFN , '20' || SUBSTRING(ODLDAT,5,2) || '-' || SUBSTRING(ODLDAT,1,2) || '-' || SUBSTRING(ODLDAT,3,2)  , SUBSTRING(ODLTIM,1,2) || ':' || SUBSTRING(ODLTIM,3,2) || ':' || SUBSTRING(ODLTIM,5,2)  FROM qgpl.qsqclii    ");
        }

        if (isSQ(initials)) {
          exUp(s,
              "call QSYS2.QCMDEXC('DSPOBJD OBJ(QSYS/QSQXML1) OBJTYPE(*SRVPGM) DETAIL(*SERVICE) OUTPUT(*OUTFILE) OUTFILE(QGPL/QSQXML1I) ' )");
          exUp(s, "GRANT ALL ON QGPL.QSQXML1I TO PUBLIC WITH GRANT OPTION");
          exUp(s,
              "INSERT INTO QTEMP.PTFINFO SELECT 'QSQXML1 ' , ODPTFN , '20' || SUBSTRING(ODLDAT,5,2) || '-' || SUBSTRING(ODLDAT,1,2) || '-' || SUBSTRING(ODLDAT,3,2)  , SUBSTRING(ODLTIM,1,2) || ':' || SUBSTRING(ODLTIM,3,2) || ':' || SUBSTRING(ODLTIM,5,2)  FROM qgpl.QSQXML1i   ");


          try {
            exUp(s,
                "call QSYS2.QCMDEXC('DSPOBJD OBJ(QSYS/QSQJSON2) OBJTYPE(*SRVPGM) DETAIL(*SERVICE) OUTPUT(*OUTFILE) OUTFILE(QGPL/QSQJSON2I) ' )");
            exUp(s, "GRANT ALL ON QGPL.QSQJSON2I TO PUBLIC WITH GRANT OPTION");
            exUp(s,
                "INSERT INTO QTEMP.PTFINFO SELECT 'QSQJSON2 ' , ODPTFN , '20' || SUBSTRING(ODLDAT,5,2) || '-' || SUBSTRING(ODLDAT,1,2) || '-' || SUBSTRING(ODLDAT,3,2)  , SUBSTRING(ODLTIM,1,2) || ':' || SUBSTRING(ODLTIM,3,2) || ':' || SUBSTRING(ODLTIM,5,2)  FROM qgpl.QSQJSON2i   ");
          } catch (Exception e) {
            /* IGNORE ERRORS -- QSQJSON2 does not exist on 7.1 */
          }

        }

        if (isJavaStoredProcedures(initials)) {
          exUp(s,
              "call QSYS.QCMDEXC('DSPOBJD OBJ(QSYS/QSQLEJEXT) OBJTYPE(*SRVPGM) DETAIL(*SERVICE) OUTPUT(*OUTFILE) OUTFILE(QGPL/QSQLEJEXTI)      ' , 0000000106.00000)");
          exUp(s, "GRANT ALL ON QGPL.QSQLEJEXTI TO PUBLIC WITH GRANT OPTION");
          exUp(s,
              "INSERT INTO QTEMP.PTFINFO SELECT 'JSTP ' , ODPTFN , '20' || SUBSTRING(ODLDAT,5,2) || '-' || SUBSTRING(ODLDAT,1,2) || '-' || SUBSTRING(ODLDAT,3,2)  , SUBSTRING(ODLTIM,1,2) || ':' || SUBSTRING(ODLTIM,3,2) || ':' || SUBSTRING(ODLTIM,5,2)  FROM qgpl.qsqlejexti   ");
        }

        /*
         * exUp(
         * s,"call QSYS.QCMDEXC('DSPPTF LICPGM($JC1) OUTPUT(*OUTFILE) OUTFILE(QGPL/P5722JC1)                                           ', 0000000080.00000)"
         * ); exUp(
         * s,"INSERT INTO QTEMP.PTFINFO SELECT 'TOOLBOX ' , SCPTFID ,  '20' ||  substring(SCSTDATE,7,2) || '-' ||  substring(SCSTDATE,1,2) || '-' ||  substring(SCSTDATE,4,2)  AS DT, SCSTTIME as \"Change time\" FROM qgpl.p5722jc1 order by DT desc  fetch first 1 row only"
         * );
         */

        if (isCoverage(initials)) {
          writer.println(" <h2> Code Coverage Information</h2> ");

          writer.println(" <a href=\"jacoco/" + initials + "/\">" + initials
              + "</a> FROM jacoco/" + initials + "<br>");

          // TODO: Run the command to generate the information
          try {
            StringBuffer sb = new StringBuffer();
            sb.append("cd "+JTOpenTestEnvironment.testcaseHomeDirectory+";\n");
            // Get the dir (i.e. jacoco/java6 )
            String runtimeDir = getJoCoCoRuntimeDir(initials);

            sb.append("java -jar "+JTOpenTestEnvironment.testcaseHomeDirectory+"/jacoco/lib/jacococli.jar "
                + "report jacoco/" + initials + "/* " + "--classfiles "
                + runtimeDir + "/jt400.jar " + "--html ct/jacoco/" + initials
                + " " + "--sourcefiles " + runtimeDir + ";\n");
            sb.append("exit;\n");
            System.out.println("---- Running ----- ");
            System.out.println(sb.toString());

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("/qopensys/usr/bin/sh");
            OutputStream stdin = process.getOutputStream();
            byte[] asciiInput = sb.toString().getBytes("UTF-8");
            stdin.write(asciiInput);
            stdin.close();

            InputStream stdout = process.getInputStream();
            BufferedReader stdoutReader = new BufferedReader(
                new InputStreamReader(stdout));
            System.out.println("-------stdout-------------");
            String line = stdoutReader.readLine();
            while (line != null) {
              System.out.println(line);
              line = stdoutReader.readLine();
            }

            InputStream stderr = process.getErrorStream();
            BufferedReader stderrReader = new BufferedReader(
                new InputStreamReader(stderr));
            System.out.println("-------stderr-------------");
            line = stderrReader.readLine();
            while (line != null) {
              System.out.println(line);
              line = stderrReader.readLine();
            }

            process.exitValue();

          } catch (Exception e) {
            System.out.println("Exception generating coverage report");
            e.printStackTrace(System.out);
          }

        }

        writer.println(" <h2> PTF information</h2> ");

        query = "SELECT * FROM QTEMP.PTFINFO order by \"Change Date\" desc";

        rs = exQ(writer, s, query);
        JDSQL400.html = true;

        String[] formatPtf = { null, /* COMP */
            "<a href=\"https://build.rch.stglabs.ibm.com/fixprocess/viewfixes.wss?continueButton=viaLink&selectedTab=fixgeneral&fixNumber={STUFF}\">{STUFF}</a>", /*
                                                                                                                                                                   * PTFID
                                                                                                                                                                   */
            null, /* change date */
            null, /* change time */
        };

        JDSQL400.dispResultSet(writer, rs, true, formatPtf);

        // Run query again to get the latest time
        rs = exQ(writer, s, query);
        while (rs.next()) {
          String dateString = rs.getString(3);
          String timeString = rs.getString(4);
          // Format is yyyy-[m]m-[d]d hh:mm:ss[.f...]
          Timestamp thisTs = Timestamp.valueOf(dateString + " " + timeString);
          if (compareTimestamp == null) {
            compareTimestamp = thisTs;
          } else if (compareTimestamp.getTime() < thisTs.getTime()) {
            compareTimestamp = thisTs;
          }
        }

      }

      String jarFilename = null;

      // If toolbox, use the jar file to get the compareTimestamp
      if (isToolbox(initials)) {
        if (is400(initials)) {
	    if (isJava8(initials)) {
          jarFilename = "/qibm/proddata/os400/jt400/lib/java8/jt400.jar";
	    } else if (isJava6(initials)) {
            jarFilename = "/qibm/proddata/os400/jt400/lib/java6/jt400.jar";
          } else {
            if (isV6(initials)) {
              jarFilename = "/qibm/proddata/http/public/jt400/lib/jt400.jar";
            } else {
              jarFilename = "/qibm/proddata/os400/jt400/lib/jt400.jar";
            }
          }
        } else {
	  // Not on 400, using jar file locally
	    if (isJava8(initials)) {
		jarFilename = "jars/java8/jt400.jar";
	    } else  if (isJava6(initials)) {
		jarFilename = "jars/java6/jt400.jar";
	    } else {
		jarFilename = "jars/jt400.jar";
	    }
	}

      } else if (isJtopenlite(initials)) {
        jarFilename = "jars/jtopenlite.jar";
      } else if (isAndroid(initials)) {
        jarFilename = "jars/jt400android.jar";
      }
      if (jarFilename != null) {
        File jarFile = new File(jarFilename);
        if (jarFile.exists()) {
          compareTimestamp = new Timestamp(jarFile.lastModified());

          Timestamp buildTimestamp = getBuildTimestamp(jarFile);

          writer.println("<h2> JAR information</h2> ");
          writer.println("  <table border>");
          writer.println(
              "    <TR><TD>" + jarFilename + "</TD><TD>" + compareTimestamp
                  + "</TD><TD> built " + buildTimestamp + "</TD></TR>");
          writer.println("  </table>");
        } else {
          writer.println(" <h2> JAR information</h2> ");
          writer.println("   <table border>");
          writer.println(
              "     <TR><TD>" + jarFilename + "</TD><TD>NOT FOUND</TD></TR>");
          writer.println("   </table>");
        }

      }

      System.out
          .println("Generating information -- STEP 2 -- generate SQL commands");
      writer.println(
          "Generating information -- STEP 2 -- generate SQL commands<br>");

      String testname = description;
      String rawfile = "RO" + initials;
      String regressed = "RG" + initials;
      String worst = "WR" + initials;
      String jlatest = "LT" + initials;
      String finishtime = "FT" + initials;
      String latestfull = "LF" + initials;
      String finishtimefull = "FF" + initials;

      Random random = new Random();

      String unique = "" + ((0x7FFFFFFF) & random.nextInt());

      // String inserted="ct/runit"+initials+".inserted."+AS400;
      // String lowsev="ct/lowsev.txt";
      String RUNONE = "java test.JDRunit " + initials + " ";
      String notes = "notes" + initials;

      String SCHEMA = "JDTESTINFO";

      String rawindex = rawfile + "II1";
      String regressedIndex = regressed + "II1";
      String rawindex2 = rawfile + "II2";
      String rawindex3 = rawfile + "II3";
      String worstIndex1 = worst + "II1";
      String worstIndex2 = worst + "II2";
      String worstIndex3 = worst + "II3";
      String worstIndex4 = worst + "II4";
      String jlatestIndex = jlatest + "II";
      String jlatestIndex2 = jlatest + "II2";
      String jlatestIndex3 = jlatest + "II3";
      String jlatestIndex4 = jlatest + "II4";
      String jlatestIndex5 = jlatest + "II5";

      /* Make sure all the tables have been created */

      /* exUpIE means execute update ignore exception */
      exUpIE(s, "CREATE COLLECTION " + SCHEMA);
      if (reset || resetRegression) {
        exUpIE(s, "DROP TABLE " + SCHEMA + "." + rawfile);
      }
      exUpIE(s, "CREATE TABLE " + SCHEMA + "." + rawfile
          + "(FINISHTIME TIMESTAMP, SYSTEM vargraphic(15) ccsid 13488 , TESTCASE vargraphic(60) ccsid 13488, SUCCEEDED INT, FAILED INT, NOTAPPL INT, NOTATT INT, TIME DOUBLE )");

      exUpIE(s, "GRANT ALL ON  " + SCHEMA + "." + rawfile
          + " TO PUBLIC WITH GRANT OPTION");
      exUpIE(s, "CREATE INDEX " + SCHEMA + "." + rawindex + " ON " + SCHEMA
          + "." + rawfile + " (FINISHTIME ASC ) WITH 0 DISTINCT VALUES");
      exUpIE(s, "CREATE INDEX " + SCHEMA + "." + rawindex2 + " ON " + SCHEMA
          + "." + rawfile
          + " (SYSTEM ASC , FINISHTIME ASC , TESTCASE ASC ) WITH 0 DISTINCT VALUES");
      exUpIE(s, "CREATE INDEX " + SCHEMA + "." + rawindex3 + " ON " + SCHEMA
          + "." + rawfile
          + " (NOTATT ASC , SYSTEM ASC , TESTCASE ASC ) WITH 0 DISTINCT VALUES");

      /* Cleanup up the raw file if aliases exist for the current system */ 
      String[] aliases = JDHostName.getAliases(AS400);
      for (int i = 0; i < aliases.length; i++) { 
        String updateAlias = "UPDATE "+SCHEMA + "." + rawfile+" SET SYSTEM='"+AS400+"' WHERE SYSTEM='"+aliases[i]+"'";
        System.out.println("Compensating for aliases: "+updateAlias); 
        exUpIE(s,updateAlias); 
      }
      
      
      /* read the current list of inserted entries from rawfile */
      Hashtable<String, Hashtable<Timestamp, Timestamp>> insertedHashtable = new Hashtable<String, Hashtable<Timestamp, Timestamp>>();

      rs = exQ(writer, s,
          "select FINISHTIME, TESTCASE from " + SCHEMA + "." + rawfile);
      while (rs.next()) {
        Timestamp ts = rs.getTimestamp(1);
        String testcase = rs.getString(2);

        Hashtable<Timestamp, Timestamp> h2 = (Hashtable<Timestamp, Timestamp>) insertedHashtable.get(testcase);
        if (h2 == null) {
          h2 = new Hashtable<Timestamp, Timestamp>();
          insertedHashtable.put(testcase, h2);
        }
        h2.put(ts, ts);
      }

      /* creates the notes table */
      System.out.println("Creating the notes table ");

      exUpIE(s, "drop table " + SCHEMA + "." + notes);
      exUp(s, "create table " + SCHEMA + "." + notes
          + " (tablename varchar(80), testcase varchar(80), note varchar(400))");

      File file = new File("ct/" + notes);
      if (file.exists()) {
        PreparedStatement ps = connection.prepareStatement(
            "insert into " + SCHEMA + "." + notes + " values(?,?,?)");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
          line = line.trim();
          int spaceIndex = line.indexOf(" ");
          if (spaceIndex > 0) {
            String testcase = line.substring(0, spaceIndex).trim();
            String note = line.substring(spaceIndex + 1).trim();
            ps.setString(1, notes);
            ps.setString(2, testcase);
            ps.setString(3, note);
            ps.executeUpdate();

          }

          line = reader.readLine();
        }
        ps.close();
        reader.close();
      } else {
        if (debug) {
          String canonicalPath = file.getCanonicalPath();
          System.out.println("Notes file " + canonicalPath + " does not exist");
        }
        // Create the notes file anyway
        // This will allow the file to be found by the UPDATE NOTE cgi
        file.createNewFile();
      }

      System.out.println("Reading the output file and inserting new values");
      {
        String insertSql = " INSERT INTO " + SCHEMA + "." + rawfile
            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertStatement = connection
            .prepareStatement(insertSql);
        BufferedReader reader = new BufferedReader(new FileReader(outfile));
        String line = reader.readLine();
        while (line != null) {
          /* get the pieces of the line */
          /* 2010-04-28 08:14:40 lp08ut23 JDCLIWGPosZ 50 0 948 0 56.504 */
          String finishtimeString;
          String system;
          String testcase;
          String succeeded;
          String failed;
          String notAppl;
          String notAtt;
          String runtime;

          String originalLine = line;
          line = line.trim();
          int start = 0;
          int spaceIndex = line.indexOf(" ");
          if (spaceIndex > 0) {
            spaceIndex = line.indexOf(" ", spaceIndex + 1);
            if (spaceIndex > 0) {
              finishtimeString = line.substring(start, spaceIndex);
              start = nextNonSpace(line, spaceIndex);
              spaceIndex = line.indexOf(" ", start);
              if (spaceIndex > 0) {
                system = line.substring(start, spaceIndex);
                start = nextNonSpace(line, spaceIndex);
                spaceIndex = line.indexOf(" ", start);
                if (spaceIndex > 0) {
                  testcase = line.substring(start, spaceIndex).trim();
                  start = nextNonSpace(line, spaceIndex);
                  spaceIndex = line.indexOf(" ", start);
                  if (spaceIndex > 0) {
                    succeeded = line.substring(start, spaceIndex);
                    start = nextNonSpace(line, spaceIndex);
                    spaceIndex = line.indexOf(" ", start);
                    if (spaceIndex > 0) {
                      failed = line.substring(start, spaceIndex);
                      start = nextNonSpace(line, spaceIndex);
                      spaceIndex = line.indexOf(" ", start);
                      if (spaceIndex > 0) {
                        notAppl = line.substring(start, spaceIndex);
                        start = nextNonSpace(line, spaceIndex);
                        spaceIndex = line.indexOf(" ", start);
                        if (spaceIndex > 0) {
                          notAtt = line.substring(start, spaceIndex);
                          start = nextNonSpace(line, spaceIndex);
                          runtime = line.substring(start);

                          if (debug)
                            System.out.println("Processing line :" + line);

                          Timestamp ts;
                          try {
                            ts = Timestamp.valueOf(finishtimeString);
                          } catch (Exception e) {
                            System.out.println("Error processing timestamp "
                                + finishtimeString + " in line:" + line);
                            ts = new Timestamp(0);
                          }
                          if (resetRegression
                              && (ts.getTime() < regressionEarliestTime)) {
                            if (debug) {
                              System.out.println("Skipping " + originalLine);
                              System.out.println(
                                  "finishtimeString=" + finishtimeString);
                              System.out.println("ts = " + ts);
                              System.out
                                  .println("ts.getTime() = " + ts.getTime());
                              System.out.println(
                                  "earliestTime = " + regressionEarliestTime);
                            }
                          } else {
                            if (resetRegression) {
                              if (newRunitOut != null) {
                                newRunitOut.addElement(originalLine);
                              }
                            }
                            boolean found = false;
                            Hashtable<Timestamp, Timestamp> ht = (Hashtable<Timestamp,Timestamp>) insertedHashtable
                                .get(testcase);
                            if (ht != null) {
                              Object x = ht.get(ts);
                              if (x != null) {
                                if (debug) {
                                  System.out.println("Hash " + testcase + "+"
                                      + ts + " is " + x);
                                }
                                found = true;
                              }
                            } else {
                              ht = new Hashtable<Timestamp, Timestamp>();
                              insertedHashtable.put(testcase, ht);
                            }
                            if (!found) {
                              try {
                                insertStatement.setTimestamp(1, ts);
                                insertStatement.setString(2, system);
                                insertStatement.setString(3, testcase.trim());
                                insertStatement.setString(4, succeeded);
                                insertStatement.setString(5, failed);
                                insertStatement.setString(6, notAppl);
                                insertStatement.setString(7, notAtt);
                                insertStatement.setString(8, runtime);
                                insertStatement.executeUpdate();
                                ht.put(ts, ts);

                                if (debug)
                                  System.out.println("Inserted "
                                      + finishtimeString + " " + testcase);
                              } catch (Exception e) {
                                StringBuffer sb = new StringBuffer();

                                sb.append("Error inserting into " + SCHEMA + "."
                                    + rawfile + "\n");
                                sb.append(insertSql);
                                sb.append(" 1 '" + ts + "'\n");
                                sb.append(" 2 '" + system + "'\n");
                                sb.append(" 3 '" + testcase + "'("
                                    + testcase.length() + ")\n");
                                sb.append(" 4 '" + succeeded + "'\n");
                                sb.append(" 5 '" + failed + "'\n");
                                sb.append(" 6 '" + notAppl + "'\n");
                                sb.append(" 7 '" + notAtt + "'\n");
                                sb.append(" 8 '" + runtime + "'\n");
                                System.out.println(sb.toString());
                                writer.println(
                                    "<pre>Exception " + e + " processing "
                                        + sb.toString() + "</pre>");
                                e.printStackTrace();
                              }

                            } else {
                              if (debug)
                                System.out.println("Skipping "
                                    + finishtimeString + " " + testcase);
                            }
                          } /* not resetRegression */
                        } else {
                          System.out.println(
                              "Unable to find eighth space in line:" + line);
                        }

                      } else {
                        System.out.println(
                            "Unable to find seventh space in line:" + line);
                      }

                    } else {
                      System.out.println(
                          "Unable to find sixth space in line:" + line);
                    }

                  } else {
                    System.out
                        .println("Unable to find fifth space in line:" + line);
                  }

                } else {
                  System.out
                      .println("Unable to find fourth space in line:" + line);
                }
              } else {
                System.out
                    .println("Unable to find third space in line:" + line);
              }
            } else {
              System.out.println("Unable to find second space in line:" + line);
            }
          } else {
            System.out.println("Unable to find first space in line:" + line);
          }

          line = reader.readLine();
        }

        insertStatement.close();
        reader.close();
      }

      if (resetRegression) {
        PrintWriter fileWriter = new PrintWriter(
            new FileWriter(outfile + ".new"));
        if (newRunitOut != null) {
          Enumeration<String> enumeration = newRunitOut.elements();
          while (enumeration.hasMoreElements()) {
            fileWriter.println(enumeration.nextElement());
          }
        }
        fileWriter.close();
        File oldFile = new File(outfile);
        oldFile.renameTo(new File(outfile + "." + System.currentTimeMillis()));
        File newFile = new File(outfile + ".new");
        newFile.renameTo(new File(outfile));
      }

      if (rawfile.endsWith("A") || rawfile.endsWith("B")) { 
        System.out.println("Removing HTML and servlet tests"); 

        StringBuffer iniInfo = new StringBuffer(); 
        InputStream fileInputStream = JDRunit.loadResource("ini/testbase.ini" , iniInfo);
        Properties testBaseProperties = new Properties();
        testBaseProperties.load(fileInputStream);
        fileInputStream.close();

        String deleteSql = " DELETE FROM " + SCHEMA + "." + rawfile +" WHERE TESTCASE=?";
        PreparedStatement psDelete = connection.prepareStatement(deleteSql); 
        Enumeration<Object> enumeration = testBaseProperties.keys(); 
        while (enumeration.hasMoreElements()) { 
          String key = (String) enumeration.nextElement(); 
          String base = testBaseProperties.getProperty(key); 
          if (isServletHtmlTestbase(base)) {
            int lastDotIndex = key.lastIndexOf('.'); 
            if (lastDotIndex > 0) { 
              key = key.substring(lastDotIndex+1); 
            }
            psDelete.setString(1,key); 
            psDelete.executeUpdate(); 
          }
        }
        psDelete.close(); 
      
      }

      
      if ( rawfile.endsWith("B")) { 
        System.out.println("Removing Visual tests"); 

        StringBuffer iniInfo = new StringBuffer(); 
        InputStream fileInputStream = JDRunit.loadResource("ini/testbase.ini" , iniInfo);
        Properties testBaseProperties = new Properties();
        testBaseProperties.load(fileInputStream);
        fileInputStream.close();

        String deleteSql = " DELETE FROM " + SCHEMA + "." + rawfile +" WHERE TESTCASE=?";
        PreparedStatement psDelete = connection.prepareStatement(deleteSql); 
        Enumeration<Object> enumeration = testBaseProperties.keys(); 
        while (enumeration.hasMoreElements()) { 
          String key = (String) enumeration.nextElement(); 
          String base = testBaseProperties.getProperty(key); 
          if (isVisualTestbase(base)) {
            int lastDotIndex = key.lastIndexOf('.'); 
            if (lastDotIndex > 0) { 
              key = key.substring(lastDotIndex+1); 
            }
            psDelete.setString(1,key); 
            psDelete.executeUpdate(); 
          }
        }
        psDelete.close(); 
      
      }

      
      
      System.out.println("Running the query");
      writer.println(
          "Generating information -- STEP 3 -- running SQL commands on "
              + TESTSYSTEM + "<br>");

      //
      // Write the query to obtain the latest results
      //

      // echo 'create view '$SCHEMA'.'$jlatest' as select * from
      // '$SCHEMA'.'$rawfile' r where r.finishtime = (select max(s.finishtime)
      // from '$SCHEMA'.'$rawfile' s where s.testcase = r.testcase and s.system
      // = r.system);'

      // Create a new table for the latest instead -- this is faster...

      // echo 'DROP VIEW '$SCHEMA'.'$jlatest';'
      exUpIE(s, "DROP TABLE " + SCHEMA + "." + jlatest);
      exUpIE(s, "DROP  TABLE " + SCHEMA + "." + finishtime);

      // echo '/* CREATING FINISHTIME /'

      exUp(s, "CREATE TABLE " + SCHEMA + "." + finishtime
          + " (FINISHTIME TIMESTAMP, SYSTEM vargraphic(15) ccsid 13488 , TESTCASE vargraphic(60) ccsid 13488)");
      exUp(s, "GRANT ALL ON  " + SCHEMA + "." + finishtime
          + " TO PUBLIC WITH GRANT OPTION");
      exUp(s,
          "insert into  " + SCHEMA + "." + finishtime
              + " SELECT max(finishtime), system, testcase FROM " + SCHEMA + "."
              + rawfile + " group by system, testcase");

      // echo '/* CREATING LATEST * /'
      // echo 'select current_timestamp as timestamp from qsys2.qsqptabl;'

      exUp(s, "CREATE TABLE " + SCHEMA + "." + jlatest
          + "(FINISHTIME TIMESTAMP, SYSTEM vargraphic(15) ccsid 13488 , TESTCASE vargraphic(60) ccsid 13488, SUCCEEDED INT, FAILED INT, NOTAPPL INT, NOTATT INT, TIME DOUBLE )");
      exUp(s, "GRANT ALL ON  " + SCHEMA + "." + jlatest
          + " TO PUBLIC WITH GRANT OPTION");

      exUp(s, "CREATE INDEX " + SCHEMA + "." + jlatestIndex + " ON " + SCHEMA
          + "." + jlatest
          + " (SYSTEM ASC , FINISHTIME ASC , TESTCASE ASC ) WITH 0 DISTINCT VALUES");
      exUp(s,
          "CREATE INDEX " + SCHEMA + "." + jlatestIndex2 + " ON " + SCHEMA + "."
              + jlatest
              + " (SYSTEM ASC , TESTCASE ASC ) WITH 0 DISTINCT VALUES");
      exUp(s, "CREATE INDEX " + SCHEMA + "." + jlatestIndex3 + " ON " + SCHEMA
          + "." + jlatest + " (TESTCASE ASC ) WITH 0 DISTINCT VALUES");
      exUp(s, "CREATE INDEX " + SCHEMA + "." + jlatestIndex4 + " ON " + SCHEMA
          + "." + jlatest
          + " (SYSTEM ASC , FAILED ASC , TESTCASE ASC ) WITH 0 DISTINCT VALUES");
      exUp(s,
          "CREATE INDEX " + SCHEMA + "." + jlatestIndex5 + " ON " + SCHEMA + "."
              + jlatest
              + " (SYSTEM ASC , FINISHTIME ASC ) WITH 0 DISTINCT VALUES");
      exUp(s, "insert into " + SCHEMA + "." + jlatest
          + " select r.FINISHTIME, r.SYSTEM  , r.TESTCASE, r.SUCCEEDED, r.FAILED, r.NOTAPPL, r.NOTATT, r.TIME  from "
          + SCHEMA + "." + rawfile + " r, " + SCHEMA + "." + finishtime
          + " s where r.finishtime = s.finishtime and s.testcase = r.testcase and s.system = r.system");

      exUp(s, "drop table " + SCHEMA + "." + finishtime);

      //
      // Create a table without not attempted
      //
      exUpIE(s, "drop table " + SCHEMA + "." + finishtimefull);
      exUp(s, "CREATE TABLE " + SCHEMA + "." + finishtimefull
          + " (FINISHTIME TIMESTAMP, SYSTEM vargraphic(15) ccsid 13488 , TESTCASE vargraphic(60) ccsid 13488)");
      exUp(s, "GRANT ALL ON  " + SCHEMA + "." + finishtimefull
          + " TO PUBLIC WITH GRANT OPTION");
      exUp(s,
          "insert into  " + SCHEMA + "." + finishtimefull
              + " SELECT max(finishtime), system, testcase FROM " + SCHEMA + "."
              + rawfile + " where notatt=0 group by system, testcase");

      exUpIE(s, "DROP TABLE " + SCHEMA + "." + latestfull);
      exUp(s, "CREATE TABLE " + SCHEMA + "." + latestfull
          + "(FINISHTIME TIMESTAMP, SYSTEM vargraphic(15) ccsid 13488 , TESTCASE vargraphic(60) ccsid 13488, SUCCEEDED INT, FAILED INT, NOTAPPL INT, NOTATT INT, TIME DOUBLE)");
      exUp(s, "GRANT ALL ON  " + SCHEMA + "." + latestfull
          + " TO PUBLIC WITH GRANT OPTION");

      exUp(s, "insert into " + SCHEMA + "." + latestfull
          + " select r.FINISHTIME, r.SYSTEM  , r.TESTCASE, r.SUCCEEDED, r.FAILED, r.NOTAPPL, r.NOTATT, r.TIME  from "
          + SCHEMA + "." + rawfile + " r, " + SCHEMA + "." + finishtimefull
          + " s where r.finishtime = s.finishtime and s.testcase = r.testcase and s.system = r.system");

      exUp(s, "drop table " + SCHEMA + "." + finishtimefull);

      //
      // Write a query to see the places where we have regressed
      //
      //
      // Note.. Using tables for temporary results performs much better than
      // using views.
      //
      // echo '/* CREATING REGRESSED * /'
      // echo 'select current_timestamp as timestamp from qsys2.qsqptabl;'
      exUpIE(s, "drop table " + SCHEMA + "." + regressed);

      /*
       * When regressed defined as more failed
       * exUp(s,"create table "+SCHEMA+"."+
       * regressed+" ( old_finish_time TIMESTAMP, new_finish_time TIMESTAMP, system vargraphic(15) ccsid 13488, testcase vargraphic(60) CCSID 13488, succceeded int , new_failed_count int , old_failed_count int, NOTAPPL int , NOTATT int , TIME double )"
       * );
       */

      /* when regressed defined as less passed */
      exUp(s, "create table " + SCHEMA + "." + regressed
          + " ( old_finish_time TIMESTAMP, new_finish_time TIMESTAMP, system vargraphic(15) ccsid 13488, testcase vargraphic(60) CCSID 13488, succceeded int , new_failed_count int, new_passed_count int , old_failed_count int, old_passed_count int, NOTAPPL int , NOTATT int , TIME double )");

      exUp(s, "GRANT ALL ON  " + SCHEMA + "." + regressed
          + " TO PUBLIC WITH GRANT OPTION");

      exUp(s,
          "CREATE INDEX " + SCHEMA + "." + regressedIndex + " ON " + SCHEMA
              + "." + regressed
              + " (SYSTEM ASC , TESTCASE ASC ) WITH 0 DISTINCT VALUES");

      /*
       * When regressed defined as more failed exUp(s,
       * "insert into "+SCHEMA+"."+
       * regressed+" select t.FINISHTIME as old_finish_time, " +
       * "r.FINISHTIME as new_finish_time, r.SYSTEM, r.TESTCASE, " +
       * "r.SUCCEEDED, r.FAILED as new_failed_count, t.FAILED as old_failed_count, "
       * + "r.NOTAPPL, r.NOTATT, r.time " +
       * "from "+SCHEMA+"."+jlatest+" r, "+SCHEMA+"."+rawfile+" t " +
       * "where r.testcase = t.testcase and r.failed > t.failed and r.system = t.system and r.system='"
       * +AS400+"' and t.notatt=0");
       * 
       */

      //
      // Define as failed being more more
      //
      // echo "insert into $SCHEMA.$regressed select t.FINISHTIME as
      // old_finish_time, r.FINISHTIME as new_finish_time, r.SYSTEM, r.TESTCASE,
      // r.SUCCEEDED, r.FAILED as new_failed_count, t.FAILED as
      // old_failed_count, r.NOTAPPL, r.NOTATT, r.time from $SCHEMA.$jlatest r,
      // $SCHEMA.$rawfile t where r.testcase = t.testcase and r.failed >
      // t.failed and r.system = t.system and r.system='"$TESTSYSTEM"' and
      // t.notatt=0;"

      //
      // Define as passed being less
      //
      // r is the latest result
      //

      exUp(s, "insert into " + SCHEMA + "." + regressed
          + " select t.FINISHTIME as old_finish_time, "
          + "r.FINISHTIME as new_finish_time, " + "r.SYSTEM, " + "r.TESTCASE, "
          + "r.SUCCEEDED, " + "r.FAILED as new_failed_count, "
          + "r.SUCCEEDED as new_passed_count, "
          + "t.FAILED as old_failed_count, "
          + "t.SUCCEEDED as old_passed_count, " + "r.NOTAPPL, " + "r.NOTATT, "
          + "r.time " + "from " + SCHEMA + "." + jlatest + " r, " + SCHEMA + "."
          + rawfile + " t " + "where r.testcase = t.testcase and "
          + "r.SUCCEEDED < t.SUCCEEDED and " + "r.system = t.system and "
          + "r.system='" + TESTSYSTEM + "' and " + "t.notatt=0");

      // echo '/* CREATING WORST * /'
      // echo 'select current_timestamp as timestamp from qsys2.qsqptabl;'
      exUpIE(s, "drop  table " + SCHEMA + "." + worst);

      /*
       * Old def using failed to determine regression exUp(s,
       * "create table "+SCHEMA+"."+
       * worst+" ( old_finish_time TIMESTAMP, new_finish_time TIMESTAMP, system vargraphic(15) ccsid 13488, testcase vargraphic(60) CCSID 13488, succceeded int , new_failed_count int , old_failed_count int, NOTAPPL int , NOTATT int , TIME double )"
       * );
       */

      /* definition using lower passed count to determine regression */
      exUp(s, "create table " + SCHEMA + "." + worst
          + " ( old_finish_time TIMESTAMP, new_finish_time TIMESTAMP, system vargraphic(15) ccsid 13488, testcase vargraphic(60) CCSID 13488, succceeded int , new_failed_count int , new_passed_count int,  old_failed_count int, old_passed_count int, NOTAPPL int , NOTATT int , TIME double )");

      exUp(s, "GRANT ALL ON  " + SCHEMA + "." + worst
          + " TO PUBLIC WITH GRANT OPTION");

      /* Old with more failed */
      /*
       * exUp(s, "CREATE INDEX "+SCHEMA+"."+worstIndex1+" ON "+SCHEMA+"."+
       * worst+" (NEW_FAILED_COUNT ASC ) WITH 0 DISTINCT VALUES");
       */

      /* new way with passed count */
      exUp(s, "CREATE INDEX " + SCHEMA + "." + worstIndex1 + " ON " + SCHEMA
          + "." + worst + " (NEW_PASSED_COUNT ASC ) WITH 0 DISTINCT VALUES");

      exUp(s, "CREATE INDEX " + SCHEMA + "." + worstIndex2 + " ON " + SCHEMA
          + "." + worst + " (OLD_FINISH_TIME ASC ) WITH 0 DISTINCT VALUES");

      exUp(s,
          "CREATE INDEX " + SCHEMA + "." + worstIndex3 + " ON " + SCHEMA + "."
              + worst + " (SYSTEM ASC , TESTCASE ASC ) WITH 0 DISTINCT VALUES");

      exUp(s, "CREATE INDEX " + SCHEMA + "." + worstIndex4 + " ON " + SCHEMA
          + "." + worst + " (NEW_FAILED_COUNT DESC ) WITH 0 DISTINCT VALUES");

      /* Old way with more failed */
      /*
       * exUp(s,"insert into "+SCHEMA+"."+worst+" select * from "+SCHEMA+"."+
       * regressed+" as a where old_failed_count = (select min(old_failed_count) from "
       * +SCHEMA+"."+
       * regressed+" as b where a.testcase=b.testcase and a.system=b.system )");
       */

      /* New way with less passed */
      exUp(s, "insert into " + SCHEMA + "." + worst + " select * from " + SCHEMA
          + "." + regressed
          + " as a where old_passed_count = (select max(old_passed_count) from "
          + SCHEMA + "." + regressed
          + " as b where a.testcase=b.testcase and a.system=b.system )");

      //
      // This is the right query, but it ran really slow when regressed amd
      // worst were views ..
      //
      writer.println("<br><a href=\"#failing\">Go to FAILING</a> ");
      writer.println(" <h2> Places where tests have regressed on " + TESTSYSTEM
          + " </h2> ");
      // echo 'select current_timestamp as timestamp from qsys2.qsqptabl;'
      //
      // Changed 7/13/2004 to select max(old_finish_time) instead of
      // min(old_finish_time) .
      //

      String regressionQuery = "select 'RG' AS X, OLD_FINISH_TIME, NEW_FINISH_TIME, SYSTEM, TESTCASE, SUCCCEEDED AS SUCC, NEW_FAILED_COUNT AS \"NEW<BR>FAILED<BR>COUNT\", OLD_FAILED_COUNT AS \"OLD<BR>FAILED<BR>COUNT\", NOTAPPL AS \"NOT<br>APPL\", NOTATT AS \"NOT<br>ATT\", TIME   from "
          + SCHEMA + "." + worst
          + " as a where old_finish_time= (select max(old_finish_time) from "
          + SCHEMA + "." + worst
          + " as b where a.testcase=b.testcase and a.system=b.system) order by new_failed_count desc";

      regressionQuery = "select 'RG' AS X, OLD_FINISH_TIME, NEW_FINISH_TIME, SYSTEM, a.TESTCASE, SUCCCEEDED AS SUCC, NEW_FAILED_COUNT AS \"NEW<BR>FAILED<BR>COUNT\", OLD_FAILED_COUNT AS \"OLD<BR>FAILED<BR>COUNT\", NOTAPPL AS \"NOT<br>APPL\", NOTATT AS \"NOT<br>ATT\", TIME, a.testcase || 'NOTE' || coalesce( note, ' ' ) as notes    from "
          + SCHEMA + "." + worst + " as a left outer join " + SCHEMA + "."
          + notes
          + " as c on a.testcase=c.testcase  where old_finish_time= (select max(old_finish_time) from "
          + SCHEMA + "." + worst
          + " as b where a.testcase=b.testcase and a.system=b.system) order by new_failed_count desc";

      /* New regression query */

      regressionQuery = "select 'RG' AS X, OLD_FINISH_TIME, NEW_FINISH_TIME, SYSTEM, a.TESTCASE, SUCCCEEDED AS SUCC, NEW_FAILED_COUNT AS \"NEW<BR>FAILED<BR>COUNT\", NEW_PASSED_COUNT AS \"NEW<BR>PASSED<BR>COUNT\", OLD_FAILED_COUNT AS \"OLD<BR>FAILED<BR>COUNT\", OLD_PASSED_COUNT AS \"OLD<BR>PASSED<BR>COUNT\", NOTAPPL AS \"NOT<br>APPL\", NOTATT AS \"NOT<br>ATT\", TIME, a.testcase || 'NOTE' || coalesce( note, ' ' ) as notes    from "
          + SCHEMA + "." + worst + " as a left outer join " + SCHEMA + "."
          + notes
          + " as c on a.testcase=c.testcase  where old_finish_time= (select max(old_finish_time) from "
          + SCHEMA + "." + worst
          + " as b where a.testcase=b.testcase and a.system=b.system) order by new_failed_count desc";

      rs = exQ(writer, s, regressionQuery);
      JDSQL400.html = true;
      String[] formatRegressed = { null,
          "<a href=\"out/" + initials
              + "/runit.{STRIPPEDTS}\">{STRIPPEDTS}</a>", /* old finish time */
          "<a href=\"out/" + initials
              + "/runit.{STRIPPEDTS}\">{STRIPPEDTS}</a>", /* new finish time */
          null, /* system */
          "<a href=\"cgi-pase/JDRunit.acgi?INITIALS="
              + initials + "&TESTCASE={STUFF}&UNIQUE=" + unique
              + "\">RUN</a> {STUFF}", /* testcase */
          null, /* succeeded */
          null, /* new failed count */
          null, /* new passed count */
          null, /* old failed count */
          null, /* old passed count */
          null, /* not appl */
          null, /* not att */
          null, /* time */
          "<a href=\"cgi-pase/updateNote.acgi?FILE=" + notes
              + "&TEST={PART1SEPNOTE}&NOTE={PART2SEPNOTE}\">__{PART2SEPNOTE}</a>", /*
                                                                                    * note
                                                                                    */
      };
      if (compareTimestamp != null) {
        String[] formatWithCompare = { null, "{NEWERTS-"
            + compareTimestamp.toString() + "}<a href=\"out/" + initials
            + "/runit.{STRIPPEDTS}\">{STRIPPEDTS}</a>", /* old finish time */
            "{NEWERTS-"
                + compareTimestamp.toString() + "}<a href=\"out/" + initials
                + "/runit.{STRIPPEDTS}\">{STRIPPEDTS}</a>", /*
                                                             * new finish time
                                                             */
            null, /* system */
            "<a href=\"cgi-pase/JDRunit.acgi?INITIALS=" + initials
                + "&TESTCASE={STUFF}&UNIQUE=" + unique
                + "\">RUN</a> {STUFF}", /* testcase */
            null, /* succeeded */
            null, /* new failed count */
            null, /* new passed count */
            null, /* old failed count */
            null, /* old passed count */
            null, /* not appl */
            null, /* not att */
            null, /* time */
            "<a href=\"cgi-pase/updateNote.acgi?FILE=" + notes
                + "&TEST={PART1SEPNOTE}&NOTE={PART2SEPNOTE}\">__{PART2SEPNOTE}</a>", /*
                                                                                      * note
                                                                                      */
        };
        formatRegressed = formatWithCompare;

      }
      JDSQL400.dispResultSet(writer, rs, true, formatRegressed);

      rs = exQ(writer, s,
          "select 'REGRESSED_COUNT=' || char(count(time)) from " + SCHEMA + "."
              + worst + "  as a "
              + "where old_finish_time= (select max(old_finish_time) from "
              + SCHEMA + "." + worst + " as b "
              + "where a.testcase=b.testcase and a.system=b.system)");
      JDSQL400.dispResultSet(writer, rs, true);

      //
      // See non attempted testcases
      //
      String releaseSystem = TESTSYSTEM;

      // echo 'select current_timestamp as timestamp from qsys2.qsqptabl;'
      writer.println(" <h2> Not attempted testcases for " + testname + " ON "
          + releaseSystem + " </h2> ");

      rs = exQ(writer, s,
          "select 'NAT' AS X, FINISHTIME,SYSTEM,TESTCASE,SUCCEEDED AS SUCC,FAILED,NOTAPPL,NOTATT,TIME from "
              + SCHEMA + "." + jlatest + " where system='" + releaseSystem
              + "' and notatt > 0 order by testcase");
      String[] formatNotAttempted = {
          null, "<a href=\"out/" + initials
              + "/runit.{STRIPPEDTS}\">{STUFF}</a>", /* finish time */
          null, /* system */
          null, /* testcase */
          null, /* SUC */
          null, /* FAIL */
          null, /* NOTAPPL */
          null, /* NOTATT */
          null, /* TIME */
      };
      if (compareTimestamp != null) {
        String[] formatWithCompare = { null,
            "{NEWERTS-"
                + compareTimestamp.toString() + "}<a href=\"out/" + initials
                + "/runit.{STRIPPEDTS}\">{STUFF}</a>", /* finish time */
            null, /* system */
            null, /* testcase */
            null, /* SUC */
            null, /* FAIL */
            null, /* NOTAPPL */
            null, /* NOTATT */
            null, /* TIME */
        };
        formatNotAttempted = formatWithCompare;
      }

      JDSQL400.dispResultSet(writer, rs, true, formatNotAttempted);

      rs = exQ(writer, s,
          "select 'NOTATT_COUNT=' || char(count(time)) from " + SCHEMA + "."
              + jlatest + " where system='" + releaseSystem
              + "' and notatt > 0");
      JDSQL400.dispResultSet(writer, rs, true);

      writer.println(" <h2> Rerun commands</h2> ");
      writer.println(" <pre>cd "+JTOpenTestEnvironment.testcaseHomeDirectory+"\n");
      writer.println("java test.JDRunit " + initials + " RERUNFAILED </pre>");
      // echo "select '$RUNONE ' || testcase from $SCHEMA."$worst' as a where
      // old_finish_time= (select max(old_finish_time) from "+SCHEMA+"."+worst'
      // as b where a.testcase=b.testcase and a.system=b.system) order by
      // new_failed_count desc '

      //
      //
      //
      query = "select '" + RUNONE + "' || testcase from " + SCHEMA + "." + worst
          + " as a "
          + "where old_finish_time= (select max(old_finish_time) from " + SCHEMA
          + "." + worst + " as b "
          + "where a.testcase=b.testcase and a.system=b.system)  order by testcase  ";
      if (System.getProperty("workaround") != null) {
        s = connection.createStatement();
        query = "select testcase from " + SCHEMA + "." + worst + " as a "
            + "where old_finish_time= (select max(old_finish_time) from "
            + SCHEMA + "." + worst + " as b "
            + "where a.testcase=b.testcase and a.system=b.system)  order by testcase  ";
      } else {
      }

      rs = exQ(writer, s, query);
      JDSQL400.dispResultSet(writer, rs, true);

      exUp(s, "drop table " + SCHEMA + "." + worst);

      //
      // Write a query to see what the totals are for the systems.
      //
      // echo 'select current_timestamp as timestamp from qsys2.qsqptabl;'
      writer.println(" <h2> Per system totals from " + jlatest + " </h2>  ");
      rs = exQ(writer, s, "select system, "
          + "max(finishtime) as max_finish_time, "
          + "min(finishtime) as min_finish_time, "
          + "sum(succeeded)+sum(failed)+sum(notatt) as total, "
          + "sum(succeeded) as succ, " + "sum(failed) as fail ,  "
          + "round((double(100 * sum(succeeded)) / (sum(succeeded)+sum(failed))), 2) as success_Rate, "
          + "sum(notatt) as not_attempted, "
          + "INT(sum(time)) as total_seconds, "
          + "INT((sum(time) / 60)) as total_minutes, "
          + "(max(finishtime) - min(finishtime)) as calc_diff_HHMMSS " + "from "
          + SCHEMA + "." + jlatest
          + " group by system order by max_finish_time");

      String[] formatPerSystem = { null, /* system */
          null, /* max finish time */
          null, /* min finish time */
          null, /* total */
          null, /* SUC */
          null, /* FAIL */
          null, /* SUCCESS RATE */
          null, /* NOTATT */
          null, /* TOTAL SECONDS */
          null, /* TOTAL MINUTES */
          "{STUFF}={TSDIF.MIN} min", /* CALC DIFF */
      };

      JDSQL400.dispResultSet(writer, rs, true, formatPerSystem);

      //
      // Determined the last run test.
      //
      sql = "select max(finishtime) as m from " + SCHEMA + "." + jlatest
          + " where system='" + releaseSystem + "'";
      rs = exQ(writer, s, sql);
      rs.next();
      Timestamp maxFinishTimestamp = rs.getTimestamp(1);
      System.out
          .println("DEBUG:  maximum finish timestamp is " + maxFinishTimestamp);
      if (maxFinishTimestamp == null) {
        System.out.println("Query was " + sql);

      }

      // writer.println(" <h2> Per system totals from "+jlatest+" without JTA
      // </h2> * /'
      // echo 'select system, max(finishtime) as max_finish_time,
      // min(finishtime) as min_finish_time,
      // sum(succeeded)+sum(failed)+sum(notatt) as total, sum(succeeded) as
      // successful, sum(failed) as failed , round((double(100 * sum(succeeded))
      // / (sum(succeeded)+sum(failed))), 2) as successRate, sum(notatt) as
      // not_attempted, sum(time) total_seconds, (sum(time) / 60) as
      // total_minutes from "+SCHEMA+"."+jlatest+" WHERE TESTCASE NOT IN
      // '"('JTABasic', 'JTABasic2', 'JTAConn', 'JTAConnCommit', 'JTAConnProp',
      // 'JTACrash1', 'JTADMDGetXxx', 'JTADelete', 'JTAInsert', 'JTALocal',
      // 'JTAMisc', 'JTAResource', 'JTAResource2', 'JTAStdBasic',
      // 'JTAStdBasic2', 'JTAStdConn', 'JTAStdConnCommit', 'JTAStdConnProp',
      // 'JTAStdCrash1', 'JTAStdCrash2', 'JTAStdDelete', 'JTAStdInsert',
      // 'JTAStdLocal', 'JTAStdMisc', 'JTAStdThread', 'JTAStdThread2',
      // 'JTAStdTransOrder', 'JTAStdUpdate', 'JTATest', 'JTAThread',
      // 'JTAThread2', 'JTATransOrder', 'JTAUpdate')"' group by system order by
      // max_finish_time;'


      writer.println(" --> </table>                                   <!-- ");
      writer.println(" --> ");

      //
      // See failing testcases...
      //
      // echo 'select current_timestamp as timestamp from qsys2.qsqptabl;'
      writer.println(" <a name=\"failing\"> <h2> Failing testcases for "
          + testname + " ON " + releaseSystem + " </h2></a>");
      // echo "/* select * from $SCHEMA.$jlatest where system='$releaseSystem'
      // and ( failed > 0 or notatt > 0 ) order by failed desc, testcase * /"

      // echo "select * from $SCHEMA.$jlatest where system='$releaseSystem' and
      // ( failed > 0 or notatt > 0 ) order by failed desc, testcase;"
      // echo "select sum(failed) as failed_sum from $SCHEMA.$jlatest where
      // system='$releaseSystem' and ( failed > 0 or notatt > 0 );"

      // echo "/* select FINISHTIME, SYSTEM, A.TESTCASE, SUCCEEDED, FAILED,
      // NOTAPPL, NOTATT, TIME, a.testcase || 'NOTE' || note as notes from
      // $SCHEMA.$jlatest as a left outer join $SCHEMA.$notes as b on
      // a.testcase=b.testcase where system='$releaseSystem' and ( failed > 0 or
      // notatt > 0 ) order by failed desc, a.testcase; * / "

      query = "select 'FD' AS X, FINISHTIME, SYSTEM, A.TESTCASE, SUCCEEDED AS SUC, FAILED AS FAIL, NOTATT,   a.testcase || 'NOTE' || coalesce( note, ' ' ) as notes   from "
          + SCHEMA + "." + jlatest + " as a left outer join " + SCHEMA + "."
          + notes + " as b on a.testcase=b.testcase " + "where system='"
          + releaseSystem
          + "' and ( failed > 0 or notatt > 0 ) order by failed desc, a.testcase";
      if (System.getProperty("workaround") != null) {
        s = connection.createStatement();
        query = "select FINISHTIME, SYSTEM, A.TESTCASE, SUCCEEDED AS SUC, FAILED AS FAIL, NOTATT   from "
            + SCHEMA + "." + jlatest + " as a left outer join " + SCHEMA + "."
            + notes + " as b on a.testcase=b.testcase " + "where system='"
            + releaseSystem
            + "' and ( failed > 0 or notatt > 0 ) order by failed desc, a.testcase";
      } else {
      }

      rs = exQ(writer, s, query);
      String[] formatFailed = {
          null, "<a href=\"out/" + initials
              + "/runit.{STRIPPEDTS}\">{STUFF}</a>", /* finish time */
          null, /* system */
          "<a href=\"cgi-pase/JDRunit.acgi?INITIALS="
              + initials + "&TESTCASE={STUFF}&UNIQUE=" + unique
              + "\">RUN</a> {STUFF}", /* testcase */
          null, /* SUC */
          null, /* FAIL */
          null, /* NOT ATT */
          "<a href=\"cgi-pase/updateNote.acgi?FILE=" + notes
              + "&TEST={PART1SEPNOTE}&NOTE={PART2SEPNOTE}\">__{PART2SEPNOTE}</a>", /*
                                                                                    * Notes
                                                                                    */
          null, null, };

      if (compareTimestamp != null) {
        String[] formatWithCompare = { null,
            "{NEWERTS-"
                + compareTimestamp.toString() + "}<a href=\"out/" + initials
                + "/runit.{STRIPPEDTS}\">{STRIPPEDTS}</a>",
            null, /* system */
            "<a href=\"cgi-pase/JDRunit.acgi?INITIALS=" + initials
                + "&TESTCASE={STUFF}&UNIQUE=" + unique
                + "\">RUN</a> {STUFF}", /* testcase */
            null, /* SUC */
            null, /* FAIL */
            null, /* NOT ATT */
            "<a href=\"cgi-pase/updateNote.acgi?FILE=" + notes
                + "&TEST={PART1SEPNOTE}&NOTE={PART2SEPNOTE}\">__{PART2SEPNOTE}</a>", /*
                                                                                      * Notes
                                                                                      */
            null, null, };
        formatFailed = formatWithCompare;
      }

      JDSQL400.dispResultSet(writer, rs, true, formatFailed);

      rs = exQ(writer, s,
          "select 'FAILED_COUNT=' || sum(failed) as failed_sum from " + SCHEMA
              + "." + jlatest + " where system='" + releaseSystem
              + "' and ( failed >= 0 or notatt > 0 )");
      JDSQL400.dispResultSet(writer, rs, true);

      rs = exQ(writer, s,
          "select 'SUCCESS_COUNT=' || sum(SUCCEEDED) as succeeded_sum from "
              + SCHEMA + "." + jlatest + " where system='" + releaseSystem
              + "' and ( failed >= 0 or notatt > 0 )");
      JDSQL400.dispResultSet(writer, rs, true);

      rs = exQ(writer, s,
          "select 'RUN_MINUTES=' || CAST( (sum(time) / 60) AS DECIMAL(10,2)) as total_minutes from "
              + SCHEMA + "." + jlatest + " where system='" + releaseSystem
              + "' and ( failed >= 0 or notatt > 0 )");
      JDSQL400.dispResultSet(writer, rs, true);

      if (System.getProperty("workaround") != null) {
        s = connection.createStatement();
      } else {
      }

      writer.println(" <h2> Rerun commands</h2> ");
      rs = exQ(writer, s, "select '" + RUNONE + " ' || testcase from " + SCHEMA
          + "." + jlatest + " where system='" + releaseSystem
          + "' and ( failed > 0 or notatt > 0 ) order by failed desc, testcase");
      JDSQL400.dispResultSet(writer, rs, true);

      if (System.getProperty("workaround") != null) {
        s = connection.createStatement();
      } else {
      }

      writer
          .println(" <h2> Testcases not running on " + TESTSYSTEM + " </h2> ");
      rs = exQ(writer, s,
          "select testcase, max(system) as system, max(succeeded) as succ  from "
              + SCHEMA + "." + jlatest
              + " where testcase not in (select testcase from " + SCHEMA + "."
              + jlatest + " where system='" + TESTSYSTEM
              + "' ) group by testcase order by testcase");
      JDSQL400.dispResultSet(writer, rs, true);

      rs = exQ(writer, s,
          "select sum(succ) from (select testcase, max(system) as system, max(succeeded) as succ  from "
              + SCHEMA + "." + jlatest
              + " where testcase not in (select testcase from " + SCHEMA + "."
              + jlatest + " where system='" + TESTSYSTEM
              + "' ) group by testcase ) as x");
      JDSQL400.dispResultSet(writer, rs, true);

      //
      // See the latest results
      //
      // echo 'select current_timestamp as timestamp from qsys2.qsqptabl;'
      writer.println(" <h2> Latest results for " + testname + " ON "
          + releaseSystem + " </h2>");
      rs = exQ(writer, s,
          "select finishtime, system, a.testcase, succeeded as succ, failed, notappl, notatt, time,  a.testcase || 'NOTE' || coalesce( note, ' ' ) as notes  from "
              + SCHEMA + "." + jlatest + " as a left outer join " + SCHEMA + "."
              + notes + " as b on a.testcase=b.testcase where a.system='"
              + releaseSystem + "' order by a.testcase");
      String[] formatLatest = {
          "<a href=\"out/" + initials
              + "/runit.{STRIPPEDTS}\">{STRIPPEDTS}</a>", /* finish time */
          null,
          "<a href=\"cgi-pase/JDRunit.acgi?INITIALS="
              + initials + "&TESTCASE={STUFF}&UNIQUE=" + unique
              + "\">RUN</a> {STUFF}", /* testcase */
          null, null, null, null, null,
          "<a href=\"cgi-pase/updateNote.acgi?FILE=" + notes
              + "&TEST={PART1SEPNOTE}&NOTE={PART2SEPNOTE}\">__{PART2SEPNOTE}</a>", /*
                                                                                    * Notes
                                                                                    */
          null, null, };

      if (compareTimestamp != null) {
        String[] formatWithCompare = {
            "{NEWERTS-"
                + compareTimestamp.toString() + "}<a href=\"out/" + initials
                + "/runit.{STRIPPEDTS}\">{STRIPPEDTS}</a>", /* finish time */
            null,
            "<a href=\"cgi-pase/JDRunit.acgi?INITIALS=" + initials
                + "&TESTCASE={STUFF}&UNIQUE=" + unique
                + "\">RUN</a> {STUFF}", /* testcase */
            null, null, null, null, null,
            "<a href=\"cgi-pase/updateNote.acgi?FILE=" + notes
                + "&TEST={PART1SEPNOTE}&NOTE={PART2SEPNOTE}\">__{PART2SEPNOTE}</a>", /*
                                                                                      * Notes
                                                                                      */
            null, null, };
        formatLatest = formatWithCompare;
      }

      JDSQL400.dispResultSet(writer, rs, true, formatLatest);

      writer.println(" <h2> Latest results for " + testname + " ON "
          + releaseSystem + " </h2>");
      rs = exQ(writer, s,
          "select min(finishtime) as finishtime, system, 'SUM' as testcase, (sum(succeeded) + sum(failed) + sum(notatt)) as total, "
              + "sum(succeeded) as succ, sum(failed) as failed,  "
              + "round((double(100 * sum(succeeded)) / (sum(succeeded)+sum(failed))), 2) as successRate, "
              + "sum(notappl) as notappl, sum(notatt) as notatt from " + SCHEMA
              + "." + latestfull + " where system='" + releaseSystem
              + "'  group by system");
      JDSQL400.dispResultSet(writer, rs, true);

      //
      // Show the 20 longest running tests.
      //

      if (System.getProperty("workaround") != null) {
        s = connection.createStatement();
      } else {
      }

      writer.println(" <h2> Longest running tests for " + testname + " ON "
          + releaseSystem + " </h2> ");
      sql = "select * from " + SCHEMA + "." + jlatest + " where system='"
          + releaseSystem + "' order by time desc fetch first 20 rows only";
      writer.println(sql);
      rs = exQ(writer, s, sql);

      String[] formatLongestRunning = { "<a href=\"out/" + initials
          + "/runit.{STRIPPEDTS}\">{STUFF}</a>", /* finish time */
          null, /* system */
          null, /* testcase */
          null, /* SUC */
          null, /* FAIL */
          null, /* NOTAPPL */
          null, /* NOTATT */
          null, /* TIME */
      };

      JDSQL400.dispResultSet(writer, rs, true, formatLongestRunning);

      //
      // see testcases that haven't been run for over a week
      //
      writer
          .println(" <h2> Testcases that have not run for over one week</h2> ");
      sql = "select * from " + SCHEMA + "." + jlatest + " where system='"
          + releaseSystem
          + "' and CURRENT_TIMESTAMP - 7 days > finishtime order by finishtime";

      writer.println(sql);
      rs = exQ(writer, s, sql);

      String[] formatNotRun = { "<a href=\"out/" + initials
          + "/runit.{STRIPPEDTS}\">{STUFF}</a>", /* finish time */
          null, /* system */
          null, /* testcase */
          null, /* SUC */
          null, /* FAIL */
          null, /* NOTAPPL */
          null, /* NOTATT */
          null, /* TIME */
      };

      JDSQL400.dispResultSet(writer, rs, true, formatNotRun);

      writer.println(" <hr>REPORT created using<br><pre>cd "+JTOpenTestEnvironment.testcaseHomeDirectory+"\n");
      writer.println("java test.JDRunit " + initials + " REPORT </pre>");



      writer.close();
      writer = null;

      //
      // Set the date on the HTML file based on the
      // maximum date from the tests: maxFinishTimestamp
      //
      if (maxFinishTimestamp != null) {
        File touchFile = new File(outputFilename);
        touchFile.setLastModified(maxFinishTimestamp.getTime());
      }

      connection.close();

      System.out.println("Creating index");
      createIndex();
      createJvmIndex();
      createToolboxIndex();
      createNativeIndex();

      if (!noExit) {
        System.out.println("JDReport calling System.exit(0)");
        System.exit(0);
      }
    } catch (Exception e) {

      if (writer != null) {
        writer.println("<BR><BR>Exception occurred<BR>");
        writer.println("<PRE>");
        e.printStackTrace(writer);
        writer.println("</PRE>");
        writer.close();
      }

      System.out.flush();
      e.printStackTrace();
      System.err.flush();
      usage();
      if (!noExit) {
        System.out.println("JDReport calling System.exit(1)");
        System.exit(1);
      }
    }

  }

  private static boolean isServletHtmlTestbase(String base) {
    String[] possibleBases = {
        "test.util.html.FormInputTest",
        "test.util.html.HTMLFormTest",
        "test.util.html.HTMLHyperlinkTest",
        "test.util.html.HTMLTableTest",
        "test.util.html.HTMLTest",
        "test.util.html.HTMLTextTest",
        "test.util.html.LabelFormElementTest",
        "test.util.html.LayoutFormPanelTest",
        "test.util.html.RadioFormInputGroupTest",
        "test.util.html.SelectFormElementTest",
        "test.util.html.SelectOptionTest",
        "test.util.html.TextAreaFormElementTest",
        "test.util.html.URLEncoderTest",
        "test.util.servlet.HTMLConverterTest",
        "test.util.servlet.RowDataTest",
        "test.util.servlet.RowMetaDataTest",
        "test.util.servlet.ServletHyperlinkTest",
        
    }; 
    for (int i = 0; i < possibleBases.length; i++) { 
      if (possibleBases[i].equals(base)) {
        return true; 
      }
    }
    return false; 
  }

  private static boolean isVisualTestbase(String base) {
    String[] possibleBases = {
        "VAS400Test",
        "VCmdTest",
        "VDQTest",
        "VIFSTest",
        "VJavaPgmCallTest",
        "VJobTest",
        "VMessageTest",
        "VPgmTest",
        "VRLATest",
        "VResourceTest",
        "VSQLQueryTest",
        "VSQLTest",
        "VSystemStatusTestDriver",
        "VUserAndGroupTestDriver",
        "VUserTest",
        
    }; 
    for (int i = 0; i < possibleBases.length; i++) { 
      if (possibleBases[i].equals(base)) {
        return true; 
      }
    }
    return false; 
  }

  
  
  private static String getJoCoCoRuntimeDir(String initials) {
    switch (initials.charAt(2)) {
    case '6':
    case '7':
      return "jacoco/java6";
    case '8':
      return "jacoco/java8";
    case '9':
      return "jacoco/java9";
    default:
      return "jacoco/java0";
    }
  }

}
