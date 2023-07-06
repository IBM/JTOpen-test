///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRunit.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDRunit.java
//
// Classes:      JDRunit
//
// Replaces the runit shell script that is used to run testcases and
// record the results.
//
// Runs regression testcases.  Should be run above the test directory
// So that the classpath is correct and all directories are available.
//
// Configuration is stored in the ini directory.
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
import java.io.StringBufferInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;
import java.util.Vector;
import java.sql.*;

public class JDRunit {

  public static final String TMP = "/tmp";
  static String testcaseCode = "/home/jdbctest";

  static Hashtable rdbToCreatedUserid = new Hashtable();
  static Hashtable rdbToCreatedPassword = new Hashtable();
  static boolean debug = false;
  static ServerSocket socket;
  static Runtime runtime;
  static int pid = 0;
  static volatile StringBuffer iniInfo = new StringBuffer();
  static String propertyInfo[][] = {
      /* "PROPERTY", "REQUIRED/OPTIONAL", "DESCRIPTION" */
      { "javaHome", "REQUIRED", "JAVA_HOME environment variable" },
      { "classpath", "OPTIONAL", "addition classpath to be set" },
      { "release", "REQUIRED", "Release being tested" },
      { "shell", "REQUIRED", "Shell used to run the test, i.e. qsh, ssh, sh" },
      { "shellArgs", "OPTIONAL", "Arguments passed to the shell" },
      { "defaultLibrary", "REQUIRED", "Library used for the test " },
      { "defaultSecondary", "OPTIONAL", "default secondary system" },
      { "otherSecondary", "OPTIONAL", "other secondary system to use" },
      { "driver", "OPTIONAL", "jdbc driver to used, default is native " },
      { "javaArgs", "OPTIONAL", "extra arguments to pass to the java command " },
      { "finalArgs", "OPTIONAL",
          "extra arguments to pass at the end of arguments " },
      { "jccPort", "OPTIONAL",
          "port number to use for jcc driver -- default 50002" },
      { "jccDatabase", "OPTIONAL",
          "jccDatabase name  -- AS400 means used AS400 system  " },
      { "debugDriverPath", "OPTIONAL",
          "Path from which to load debuggable code" },
      { "sunBoot", "OPTIONAL", "SUNBOOT classpath: Needed by debug code tests" },
      { "toolsJar", "OPTIONAL", "Location of tools jar file " },
      { "toolboxJar", "OPTIONAL", "Location of toolboxJar file " },
      { "oldToolboxJar", "OPTIONAL", "Location of toolboxJar file from previous release"},
      { "jtopenliteJar", "OPTIONAL", "Location of jtopenliteJar file " },
      { "AS400", "OPTIONAL",
          "System to test.  Defaults to local system if running on IBM i" },
      { "extraCommand", "OPTIONAL",
          "Extra shell command to run, i.e. QIBM_CHILD_JOB_SNDINQMSG=1" },
      {
          "USERID",
          "REQUIRED",
          "Userid to use as both user and pwrUser. Overridden from ini/dropAuthority.ini for user testcases." },
      { "PASSWORD", "REQUIRED", "Password to use" },
      { "RDB", "OPTIONAL", "RDB to use for tests -- default is AS400 property" },

      { "CLIWHDSN", "OPTIONAL",
          "Homogeneous Data source name for CLI tests -- default MEMEMEM" },
      { "CLIWHUID", "OPTIONAL",
          "Homogeneous userid for CLI tests -- default DB2TEST" },
      { "CLIWHPWD", "OPTIONAL",
          "Homogeneous userid for CLI tests -- default PASS2DB" },

      { "CLIWGDSN", "OPTIONAL",
          "Homogeneous V5R4 Data source name for CLI tests -- default MEMEMEM" },
      { "CLIWGUID", "OPTIONAL",
          "Homogeneous V5R4 userid for CLI tests -- default DB2TEST" },
      { "CLIWGPWD", "OPTIONAL",
          "Homogeneous V5R4 userid for CLI tests -- default PASS2DB" },

      { "CLIWLDSN", "OPTIONAL",
          "LUW Data source name for CLI tests -- default CLIDB" },
      { "CLIWLUID", "OPTIONAL", "LUW userid for CLI tests -- default DB2TEST" },
      { "CLIWLPWD", "OPTIONAL", "LUW userid for CLI tests -- default PASS2DB" },
      { "CLIZLDSN", "OPTIONAL",
          "Z/OS Data source name for CLI tests -- default STLEC2" },
      { "CLIZLUID", "OPTIONAL", "Z/OS userid for CLI tests -- default admf001" },
      { "CLIZLPWD", "OPTIONAL", "Z/OS userid for CLI tests -- default n1cetest" },

      { "CLIENTOS", "OPTIONAL", "operating system of client -- i.e. WINDOWS" },
      { "SQLLIB", "OPTIONAL", "For jcc tests, SQLLIB location" },
      { "jccJars", "OPTIONAL", "For jcc tests, location of jar files" },

  };

  static String[] inheritedProperties = { "jdbc.db2.", "com.ibm.as400.access.",
      "com.ibm.jtopenlite.", "jta.", "test.", "JDJSTP.", "debug",
      "interactive", "os400.stdio", "com.ibm.cacheLocalHost", "java.net.",
      "com.sun.management.", "https.", "jdk.", };

  static String[] convertToXProperties = { "runjdwp:", "debug", "rs",
      "healthcenter", };

  static String[] hangMessages = {
      "Type=Segmentation error",
      "java.lang.Unknown",
      "java.lang.OutOfMemoryError",
      "FATAL ERROR",
      "JVMDUMP",
      "Exception in thread \"main\" java.lang.NoClassDefFoundError:",
      "IncompatibleClassChangeError",
      "connection to \":9.0\" refused by server", };

  static String[] hangMessagesException = { "User requested", };

  static long globalTimeout = 3600000;

  static boolean on400 = false;
  static boolean onLinux = false;

  static {
    System.out.println("JDRunit: checking properties");
    String property = System.getProperty("timeout");
    if (property != null) {
      globalTimeout = Integer.parseInt(property);
      if (globalTimeout == 0)
        globalTimeout = 3600000;
    }

    property = System.getProperty("debug");
    if (property != null) {
      debug = true;
      System.out.println("JDRunit: debug is on");
    }
    if (debug) {
      System.out.println("JDRunit:  file.encoding is "
          + System.getProperty("file.encoding"));
    }
    String osname = System.getProperty("os.name");

    on400 = (osname.indexOf("400")) > 0;
    onLinux = (osname.indexOf("Linux")) >= 0;
    
    property = System.getProperty("useTestJar"); 
    if (property != null && (property.toUpperCase().indexOf('N')< 0)) {
      testcaseCode = "jt400Test.jar"; 
    }
  }

  //
  // Prevent same date from being used
  //
  static Object getTestDateLock = new Object();
  static long nextTime = 0;

  public static Date getTestDate() {
    synchronized (getTestDateLock) {
      long thisTime = System.currentTimeMillis();
      while (thisTime < nextTime) {
        long sleepTime = 1 + nextTime - thisTime;
        if (sleepTime < 10)
          sleepTime = 10;
        try {
          Thread.sleep(sleepTime);
        } catch (Exception e) {
        }
        thisTime = System.currentTimeMillis();
      }
      nextTime = thisTime + 1000;
      return new Date(thisTime);
    }
  }

  public static String twoDigitNumber(int i) {
    if (i < 10) {
      return "0" + i;
    } else {
      return "" + i;
    }
  }

  public static void usage() {
    System.out
        .println("Usage:  java test.JDRunit <INITIALS> <TEST> [<VARIATIONS>]");
    System.out
        .println("    This will run the testcase or test specified by the initials");
    System.out
        .println("    If a testcase is run then the variations are also run");
    System.out.println("    and stored the output in runit<INITIALS>.out");
    System.out
        .println("    The configuration for initials are read from test/runit<INITIALS>.ini or from runit<RR>xxx.ini,runitxx<VM>x.ini,runitxxxx<T>.ini");
    System.out
        .println("    If the REPORT environment variable is set, this will also generate the report"); 
    System.out.println("Usage:  java SHELL");
    System.out.println("    Runs as an interpret to start testcases");
    System.out.println("Usage:  java  <INITIALS> REGRESSION");
    System.out
        .println("     Run the regression bucket for the configuration.  Runs the testcases in ini/regression<INITIALS>.ini or regressionBASE<T>.ini");
    System.out.println("Usage:  java <INITIALS> REPORT");
    System.out.println("     Generate the report for the configuration");
    System.out.println("Usage:  java  <INITIALS> RERUNFAILED");
    System.out.println("     Rerun failed tests as shown in the report");
    System.out.println("Usage:  java  <INITIALS> RERUNFAILEDSTEPREPORT");
    System.out
        .println("     Rerun failed tests as shown in the report and generate new report after each test");

    System.out.println("Usage:  java <INITIALS> EMAIL");
    System.out
        .println("     E-mail a summary of the report to the EMAIL listed in ini/notifications.ini");
    System.out
        .println("Usage:  java <INITIALS> EMAILTO-<testrunner@us.ibm.com>");
    System.out
        .println("     E-mail a summary of the report to the EMAIL to the specified e-mail address");

  }

  public static void initialize() {
    if (pid == 0) {
      try {
        // Since there is no easy way to get a PID, just open a port and use
        // that port number

        socket = new ServerSocket(0);

        pid = socket.getLocalPort();

      } catch (Exception e) {
        Random random = new Random();
        pid = random.nextInt() & 0x0FFFFF;
      }

      // Initialize everything
      runtime = Runtime.getRuntime();

    }
  }

  public static void runInterpreter(PrintStream out, InputStream in,
      String defaultRun) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    out.print("Enter <INITIALS> <TEST> [<VARIATIONS>]");
    out.flush();

    String line;
    String extraJavaArgs = null;
    try {
      line = reader.readLine();
    } catch (IOException e1) {
      e1.printStackTrace();
      line = null;
    }
    String initials = null;
    if (defaultRun != null) {
      initials = defaultRun;
    }
    while (line != null && !"EXIT".equalsIgnoreCase(line)) {
      line = line.trim();
      try {
        String lowerLine = line.toLowerCase();
        if (lowerLine.indexOf("help") == 0) {
          out.println("Additional commands");
          out.println("JAVA_ARGS=...");
        } else if (lowerLine.indexOf("java_args=") == 0) {
          extraJavaArgs = line.substring(10).trim();

        } else if (lowerLine.indexOf("#") == 0) {
          // Skip comment

        } else if (lowerLine.indexOf("jtacleanuptx") >= 0) {
          if (jdSchedulerId == null) {

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
              t.printStackTrace();
            }
          } else {
            int priority = JDScheduler.PRIORITY_REGRESSION_TC;
            if (jdPriority > 0)
              priority = jdPriority;

            JDScheduler.add(out, jdSchedulerId, priority, initials,
                "java test.JTACleanupTX");

          }

        } else if (lowerLine.length() == 0) {
          // Skip empty line

        } else {
          int afterSpace = 0;
          initials = null;
          if (defaultRun != null) {
            initials = defaultRun;
            afterSpace = 0;
          } else {
            int spaceIndex = line.indexOf(' ');
            if (spaceIndex > 0) {
              initials = line.substring(0, spaceIndex);
              if (initials.indexOf("runone") == 0) {
                initials = initials.substring(6);
              }
              if (initials.indexOf("runit") == 0) {
                initials = initials.substring(5);
              }
              afterSpace = spaceIndex + 1;
            } else {
              // redundant assigment: initials = null;
            }
          }
          if (initials != null) {
            String test;
            String variations;
            line = line.substring(afterSpace).trim();
            int spaceIndex = line.indexOf(' ');
            if (spaceIndex > 0) {
              test = line.substring(0, spaceIndex);
              variations = line.substring(spaceIndex + 1).trim();
            } else {
              test = line;
              variations = null;
            }
            if (test.equals("REPORT")) {
              if (jdSchedulerId == null) {
                String[] reportArgs = new String[1];
                reportArgs[0] = initials;
                JDReport.main(reportArgs);
              } else {
                // Schedule the test to run
                int priority = JDScheduler.PRIORITY_REGRESSION_TC;
                if (jdPriority > 0)
                  priority = jdPriority;

                JDScheduler.add(out, jdSchedulerId, priority, initials,
                    "REPORT");
              }

            } else {
              if (jdSchedulerId == null) {
                String outputFile = TMP + "/runit" + initials + ".out." + pid
                    + "." + nextRunNumber();
                JDRunit runit = new JDRunit(initials, test, variations,
                    outputFile);
                if (extraJavaArgs != null) {
                  runit.setExtraJavaArgs(extraJavaArgs);
                }
                //
                // Update the runit as needed
                //
                runit.go();

              } else {
                if (test.equals("java")) {
                  int priority = JDScheduler.PRIORITY_REGRESSION_TC;
                  if (jdPriority > 0)
                    priority = jdPriority;

                  JDScheduler.add(out, jdSchedulerId, priority, initials, line);

                } else {
                  // Schedule the test to run
                  int priority = JDScheduler.PRIORITY_REGRESSION_TC;
                  if (jdPriority > 0)
                    priority = jdPriority;

                  JDScheduler.add(out, jdSchedulerId, priority, initials, test);
                }
              }
            }
          } else {
            out.println("Error could not find space");
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      out.print("Enter <INITIALS> <TEST> [<VARIATIONS>]");
      out.flush();
      try {
        line = reader.readLine();
      } catch (IOException ioex) {
        line = null;
      }
    }

  }

  public static void runRegression(String initials) throws Exception {
    if (on400) {
      Date d = new Date();
      JDJobName.sendProgramMessage("LD " +Thread.currentThread().getName()+" " + d.toString()
          + " Running REGRESSION " + initials);
    }

    // Check for host specific regression list. This list is used for
    // Smaller testing runs (i.e. EUT / PITT )
    String localHost = InetAddress.getLocalHost().getHostName().toLowerCase();
    int dotIndex = localHost.indexOf(".");
    if (dotIndex >= 0) {
      localHost = localHost.substring(0, dotIndex);
    }

    String regressionFilename = "ini/regression" + initials + "." + localHost
        + ".ini";
    String hostRegressionFilename = regressionFilename;
    if (debug) {
      System.out.println("JDRunit:  looking for regressionFilename "
          + regressionFilename);
    }

    File regressionFile = new File(regressionFilename);
    if (!regressionFile.exists()) {

      regressionFilename = "ini/regression" + initials + ".ini";
      regressionFile = new File(regressionFilename);
      if (!regressionFile.exists()) {
        String baseRegressionFilename = "ini/regressionBase"
            + initials.charAt(initials.length() - 1) + ".ini";
        regressionFilename = baseRegressionFilename;
        regressionFile = new File(regressionFilename);
        if (!regressionFile.exists()) {

          throw new Exception("Error:  Regression file " + regressionFilename
              + " or " + hostRegressionFilename + " or "
              + baseRegressionFilename + " does not exist");
        }

      }
    }
    System.out.println("Running regression instructions from "
        + regressionFilename);

    FileInputStream is = new FileInputStream(regressionFile);
    runInterpreter(System.out, is, initials);
  }

  public static String removeDoubleSpaces(String s) {
    int doubleSpaceIndex = s.indexOf("  ");
    if (doubleSpaceIndex > 0) {
      return s.substring(0, doubleSpaceIndex + 1)
          + removeDoubleSpaces(s.substring(doubleSpaceIndex + 2).trim());
    } else {
      return s;
    }
  }

  public static void rerunFailed(String initials, boolean report)
      throws Exception {
    StringBuffer rerunCommands = new StringBuffer();

    String htmlFile = JDReport.getReportName(initials);
    Hashtable addedTestcaseHashtable = new Hashtable();

    BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
    String line = reader.readLine();
    while (line != null) {
      int runitIndex = line.indexOf("test.JDRunit");
      if (runitIndex > 0) {
        int lessIndex = line.indexOf("<", runitIndex);
        if (lessIndex > 0) {
          String command = line.substring(runitIndex + 13, lessIndex).trim();
          command = removeDoubleSpaces(command) + "\n";
          if (addedTestcaseHashtable.get(command) == null) {
            if (command.indexOf("RERUNFAILED") >= 0) {
              // Dont add it
            } else if (command.indexOf("REPORT") >= 0) {
              // Dont add it
            } else {
              rerunCommands.append(command);
              addedTestcaseHashtable.put(command, command);
              if (report) {
                rerunCommands.append(initials + " REPORT\n");
              }
            }
          }
        }
      }
      line = reader.readLine();
    }
    reader.close();

    if (debug) {
      System.out
          .println("-----------RerunCommands----------------------------");
      System.out.println(rerunCommands.toString());
      System.out
          .println("====================================================");
    }

    StringBufferInputStream is = new StringBufferInputStream(
        rerunCommands.toString());

    runInterpreter(System.out, is, null);

  }

  public static String cleanupHtmlLine(String in) {
    StringBuffer sb = new StringBuffer();
    int unprocessedStartIndex = 0;
    int lessThanIndex = in.indexOf("<");
    while (lessThanIndex >= unprocessedStartIndex) {
      if (lessThanIndex == unprocessedStartIndex) {
        // Do not need to add anything
      } else {
        sb.append(in.substring(unprocessedStartIndex, lessThanIndex));
        sb.append(" ");
      }
      unprocessedStartIndex = in.indexOf(">", lessThanIndex);
      if (unprocessedStartIndex > 0) {
        unprocessedStartIndex++;
        lessThanIndex = in.indexOf("<", unprocessedStartIndex);
      } else {
        lessThanIndex = -1;
      }
    }
    if (unprocessedStartIndex > 0) {
      sb.append(in.substring(unprocessedStartIndex));
    }

    return sb.toString();

  }

  static volatile Properties domainProperties = null;

  public static String getDomain(String systemName) {
    String domain = "rch.STGLabs.IBM.COM";

    if (domainProperties == null) {
      try {
        FileInputStream fileInputStream = new FileInputStream("ini/DOMAIN.ini");
        domainProperties = new Properties();
        domainProperties.load(fileInputStream);
        fileInputStream.close();
      } catch (Exception e) {
        e.printStackTrace(System.out);
        domainProperties = null;
      }

    }
    systemName = systemName.toLowerCase();
    if (domainProperties != null) {
      String lookup = (String) domainProperties.get(systemName);
      if (lookup != null) {
        domain = lookup;
      }
    }
    return domain;
  }

  public static void email(String initials, String toAddress) throws Exception {

    StringBuffer body = new StringBuffer();
    String htmlFile = JDReport.getReportName(initials);
    Vector regressedTestcases = new Vector();
    Vector notattTestcases = new Vector();
    Vector failedTestcases = new Vector();

    // Process the report
    BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
    String line = reader.readLine();
    int parsedRegressionCount = -1;
    int parsedSuccessCount = -1;
    int parsedNotattCount = -1;
    int regressionCount = 0;
    int notattCount = 0;
    int failedCount = 0;
    while (line != null) {

      int regressionCountIndex = line.indexOf("REGRESSED_COUNT=");
      if (regressionCountIndex >= 0) {
        String left = line.substring(regressionCountIndex + 16);
        int endIndex = left.indexOf("<");
        if (endIndex > 0) {
          left = left.substring(0, endIndex);
          parsedRegressionCount = Integer.parseInt(left);
        }
      }

      int notattCountIndex = line.indexOf("NOTATT_COUNT=");
      if (notattCountIndex >= 0) {
        String left = line.substring(notattCountIndex + 13);
        int endIndex = left.indexOf("<");
        if (endIndex > 0) {
          left = left.substring(0, endIndex);
          parsedNotattCount = Integer.parseInt(left);
        }
      }

      int successCountIndex = line.indexOf("SUCCESS_COUNT=");
      if (successCountIndex >= 0) {
        String left = line.substring(successCountIndex + 14);
        int endIndex = left.indexOf("<");
        if (endIndex > 0) {
          left = left.substring(0, endIndex);
          parsedSuccessCount = Integer.parseInt(left);
        }
      }

      int regressionIndex = line.indexOf("<td>RG");
      if (regressionIndex >= 0) {

        int endLinkIndex = line.indexOf("</a>");
        if (endLinkIndex > 0) {
          // Move to the second </A>
          endLinkIndex = line.indexOf("</a>", endLinkIndex + 4);
          if (endLinkIndex > 0) {
            line = line.substring(endLinkIndex + 4);
          }
        }
        regressedTestcases.add(cleanupHtmlLine(line));
        regressionCount++;
      }

      int notattIndex = line.indexOf("<td>NAT");
      if (notattIndex >= 0) {

        int endLinkIndex = line.indexOf("</a>");
        if (endLinkIndex > 0) {
          line = line.substring(endLinkIndex + 4);
        }
        notattTestcases.add(cleanupHtmlLine(line));
        notattCount++;
      }

      int failedIndex = line.indexOf("<td>FD<");
      if (failedIndex >= 0) {
        int endLinkIndex = line.indexOf("</a>");
        if (endLinkIndex > 0) {
          line = line.substring(endLinkIndex + 4);
        }
        failedTestcases.add(cleanupHtmlLine(line));
        failedCount++;
      }

      line = reader.readLine();
    }
    reader.close();

    Properties iniProperties = getIniProperties(initials);

    String AS400 = getAS400(iniProperties, initials);

    AS400 = iniProperties.getProperty("SYSTEM", AS400);
    if (AS400 == null) {
      AS400 = "AS400_NOT_SET";
    }

    // Send the e-mail -- spawning new e-mail program as needed.
    String subject;

    String localHost = InetAddress.getLocalHost().getHostName().toUpperCase();
    int dotIndex = localHost.indexOf(".");
    if (dotIndex >= 0) {
      localHost = localHost.substring(0, dotIndex);
    }
    String testLocation = "";
    if (localHost.equals(AS400.toUpperCase()) || localHost.equals("LOCALHOST")
        || AS400.equalsIgnoreCase("localhost")) {
      if (AS400.equalsIgnoreCase("localhost")) {
        testLocation = localHost;
      } else {
        testLocation = AS400;
      }
    } else {
      testLocation = localHost + "/" + AS400;
    }

    if (initials.endsWith("A") || initials.endsWith("B")) {
      subject = "JDRunit Regression " + testLocation + " " + initials
          + " PTBX complete";
    } else if (initials.endsWith("N")) {
      subject = "JDRunit Regression " + testLocation + " " + initials
          + " NJDBC complete";
    } else if (initials.endsWith("T") || initials.endsWith("U")) {
      subject = "JDRunit Regression " + testLocation + " " + initials
          + " TJDBC complete";
    } else if (initials.endsWith("O")) {
      subject = "JDRunit Regression " + testLocation + " " + initials
          + " JSTP complete";
    } else {
      subject = "JDRunit Regression " + testLocation + " " + initials
          + " complete";
    }

    body.append(subject + "\n");
    if (parsedRegressionCount == -1) {
      body.append("\n");
      body.append("***********************************************\n");
      body.append("ERROR:  REGRESSED_COUNT not found\n");
      body.append("***********************************************\n");
    }
    body.append("\n");
    body.append("REGRESSED_COUNT=" + parsedRegressionCount + " ");
    if (regressionCount == 0 && parsedRegressionCount == 0
        && parsedNotattCount == 0) {
      subject = "JDRunit Regression " + initials + " NO REGRESSION "
          + testLocation;
    }
    if (notattCount > 0 && parsedNotattCount > 0) {
      subject = "JDRunit Regression " + initials + " NOT ATTEMPTED "
          + testLocation;
    }
    if (parsedNotattCount > 0) {
      body.append("Notatt=" + parsedNotattCount + "  ");
    }
    if (failedCount > 0) {
      body.append("Failed=" + failedCount + " ");
    }
    body.append("SUCCESS_COUNT=" + parsedSuccessCount + "\n");

    String hostname = InetAddress.getLocalHost().getCanonicalHostName();
    hostname = hostname.toUpperCase();
    int rchlandIndex = hostname.indexOf(".RCHLAND");
    if (rchlandIndex > 0) {
      hostname = hostname.substring(0, rchlandIndex);
    }
    if (hostname.indexOf('.') < 0) {

      hostname = hostname + "." + getDomain(hostname);
    }
    String URLBASE = "http://" + hostname + ":6050/";
    if (htmlFile.indexOf("ct/") == 0)
      htmlFile = htmlFile.substring(3);

    body.append("\n\nSee details at " + URLBASE + htmlFile + "\n");

    body.append("REGRESSIONPLUS " + URLBASE
        + "cgi-pase/JDRunit.acgi?PRIORITY=1&INITIALS=" + initials
        + "&TESTCASE=REGRESSIONPLUS\n");
    body.append("RERUNFAILEDPLUS " + URLBASE
        + "cgi-pase/JDRunit.acgi?PRIORITY=1&INITIALS=" + initials
        + "&TESTCASE=RERUNFAILEDPLUS\n");
    body.append("Regenerate14 at " + URLBASE
        + "cgi-pase/JDReport.acgi?INITIALS=" + initials + "&DAYS=14\n");
    if (regressionCount > 0 && regressionCount < 10) {
      body.append("\n-------------REGRESSION DETAILS-------------\n");
      Enumeration enumeration = regressedTestcases.elements();
      while (enumeration.hasMoreElements()) {
        body.append(enumeration.nextElement() + "\n");
      }

      body.append("\n-------------FAILED DETAILS-------------\n");
      enumeration = failedTestcases.elements();
      while (enumeration.hasMoreElements()) {
        body.append(enumeration.nextElement() + "\n");
      }

    }
    if (notattCount > 0) {

      body.append("\nNOTATT DETAILS\n");
      Enumeration enumeration = notattTestcases.elements();
      while (enumeration.hasMoreElements()) {
        body.append(enumeration.nextElement() + "\n");
      }
    }

    // Now mail the message. Look for email of the form
    // 7163N-EMAIL
    // 71N-EMAIL
    // N-EMAIL

    if (toAddress == null) {
      // Look for test specific test
      toAddress = iniProperties.getProperty(initials + "-EMAIL");
      if (toAddress == null) {
        // Look for release / type specific test
        String releaseType = initials.substring(0, 2)
            + initials.charAt(initials.length() - 1);
        toAddress = iniProperties.getProperty(releaseType + "-EMAIL");
        if (toAddress == null) {
          // Look for type specific test
          String testType = initials.substring(initials.length() - 1);
          toAddress = iniProperties.getProperty(testType + "-EMAIL");
          if (toAddress == null) {
            toAddress = iniProperties.getProperty("EMAIL");
          }
        }
      }

    }
    String fromAddress = iniProperties.getProperty("EMAIL");

    sendEMail(toAddress, fromAddress, subject, body);

  }

  public static void sendEMail(String initials, String subject, StringBuffer body) throws Exception {

    Properties iniProperties = getIniProperties(initials);

    String fromAddress = iniProperties.getProperty("EMAIL");
    String toAddress = iniProperties.getProperty(initials + "-EMAIL");
    if (toAddress == null) {
      // Look for release / type specific test
      String releaseType = initials.substring(0, 2)
          + initials.charAt(initials.length() - 1);
      toAddress = iniProperties.getProperty(releaseType + "-EMAIL");
      if (toAddress == null) {
        // Look for type specific test
        String testType = initials.substring(initials.length() - 1);
        toAddress = iniProperties.getProperty(testType + "-EMAIL");
        if (toAddress == null) {
          toAddress = iniProperties.getProperty("EMAIL");
        }
      }
    }

    sendEMail(toAddress, fromAddress, subject, body);

  }
  
  public static void sendEMail(String toAddress, String fromAddress,
      String subject, StringBuffer body) throws Exception {

    java.util.Properties p = System.getProperties();
    p.put("mail.smtp.host", "us.ibm.com");

    /*
     * need mail.jar and activation.jar
     * 
     * import javax.mail.*;
     * 
     * import javax.mail.internet.*;
     * 
     * import javax.activation.*; \
     */
    Class sessionClass = null;
    Class mimeMessageClass = null;
    Class internetAddressClass = null;
    Class javaxMailMessageClass = null;
    Class transportClass = null;
    Class addressClass = null;
    Class addressArrayClass = null;
    // Class mailcapCommandMapClass = null;
    // Class commandMapClass = null;
    try {
      sessionClass = Class.forName("javax.mail.Session");
      mimeMessageClass = Class.forName("java.mail.internet.MimeMessage");
      internetAddressClass = Class
          .forName("javax.mail.internet.InternetAddress");
      javaxMailMessageClass = Class.forName("javax.mail.Message");
      transportClass = Class.forName("javax.mail.Transport");
      addressClass = Class.forName("javax.mail.Address");
      // mailcapCommandMapClass =
      // Class.forName("javax.activation.MailcapCommandMap");
      // commandMapClass = Class.forName("javax.activation.CommandMap");
    } catch (ClassNotFoundException cnfe) {
      // Add a classloader to the system to find the classes..
      // Looks for the classes in known locations
      String userDir = System.getProperty("user.dir");
      String sep = System.getProperty("file.separator");

      URL[] urls = new URL[2];
      // Look for activation.jar
      int foundLocations = 0;
      StringBuffer locations = new StringBuffer();
      String[] activationJarLocations = {
          "/home/jdbctest/jars/activation.jar",
          "/home/jdbctest/jars/activation.jar",
          "/home/jdbctest/jars/activation.jar",
          "/afs/rchland.ibm.com/lande/shadow/dev2000/as400/v7r1m0t.jacl/cur/cmvc/java.pgm/yjac.jacl/jars/activation.jar",
          "/qibm/proddata/os400/java400/ext/activation.jar",
          "C:\\Documents and Settings\\Administrator\\workspace\\lib\\activation.jar", };
      activationJarLocations[0] = userDir + sep + "jars" + sep
          + "activation.jar";
      activationJarLocations[1] = "C:" + sep + "activation.jar";

      for (int i = 0; i < activationJarLocations.length && urls[0] == null; i++) {
        if (i > 0) {
          locations.append(" ");
        }
        locations.append(activationJarLocations[i]);

        File tryFile = new File(activationJarLocations[i]);
        if (tryFile.exists()) {
          urls[0] = new URL("file:" + activationJarLocations[i]);
          foundLocations++;
        }
      }
      if (foundLocations == 0) {
        throw new Exception("Unable to find activation jar in " + locations);
      }

      foundLocations = 0;
      locations = new StringBuffer();
      // Look for mail.jar
      String[] mailJarLocations = {
          "/home/jdbctest/jars/mail.jar",
          "/home/jdbctest/jars/mail.jar",
          "/home/jdbctest/jars/mail.jar",
          "/afs/rchland.ibm.com/lande/shadow/dev2000/as400/v7r1m0t.jacl/cur/cmvc/java.pgm/yjac.jacl/jars/mail.jar",
          "/qibm/proddata/os400/java400/ext/mail.jar",
          "C:\\Documents and Settings\\Administrator\\workspace\\lib\\mail.jar", };
      mailJarLocations[0] = userDir + sep + "jars" + sep + "mail.jar";
      mailJarLocations[1] = "C:" + sep + "mail.jar";
      for (int i = 0; i < mailJarLocations.length && urls[1] == null; i++) {
        if (i > 0) {
          locations.append(" ");
        }
        locations.append(mailJarLocations[i]);
        File tryFile = new File(mailJarLocations[i]);
        if (tryFile.exists()) {
          urls[1] = new URL("file:" + mailJarLocations[i]);
          foundLocations++;
        }
      }

      if (foundLocations == 0) {
        throw new Exception("Unable to find mail.jar in " + locations);
      }

      ClassLoader loader = new URLClassLoader(urls);
      sessionClass = loader.loadClass("javax.mail.Session");
      mimeMessageClass = loader.loadClass("javax.mail.internet.MimeMessage");
      internetAddressClass = loader
          .loadClass("javax.mail.internet.InternetAddress");
      javaxMailMessageClass = loader.loadClass("javax.mail.Message");
      transportClass = loader.loadClass("javax.mail.Transport");
      addressClass = loader.loadClass("javax.mail.Address");
      // mailcapCommandMapClass =
      // loader.loadClass("javax.activation.MailcapCommandMap");
      // commandMapClass = loader.loadClass("javax.activation.CommandMap");

    }

    Class[] parameterTypes;
    Object[] parms;

    addressArrayClass = Array.newInstance(addressClass, 0).getClass();

    /* javax.mail.Session session = javax.mail.Session.getDefaultInstance(p); */

    parameterTypes = new Class[1];
    parameterTypes[0] = Class.forName("java.util.Properties");
    Method getDefaultInstanceMethod = sessionClass.getMethod(
        "getDefaultInstance", parameterTypes);

    parms = new Object[1];
    parms[0] = p;

    Object session = getDefaultInstanceMethod.invoke(null, parms);

    /* java.mail.Message message = new java.mail.internet.MimeMessage(session); */
    parameterTypes[0] = sessionClass;
    Constructor mimeMessageConstructor = mimeMessageClass
        .getConstructor(parameterTypes);
    parms[0] = session;
    Object message = mimeMessageConstructor.newInstance(parms);

    /* message.setFrom(new javax.mail.internet.InternetAddress(fromAddress)); */
    parameterTypes[0] = fromAddress.getClass();
    Constructor internetAddressConstructor = internetAddressClass
        .getConstructor(parameterTypes);
    parms[0] = fromAddress;
    Object fromAddressObject = internetAddressConstructor.newInstance(parms);
    parameterTypes[0] = addressClass;
    Method setFromMethod = javaxMailMessageClass.getMethod("setFrom",
        parameterTypes);
    parms[0] = fromAddressObject;
    setFromMethod.invoke(message, parms);

    /*
     * message.setRecipients(java.mail.Message.RecipientType.TO,
     * javax.mail.internet.InternetAddress.parse(toAddress, false));
     */

    Object[] setRecipientsArgs = new Object[2];
    Class[] innerClasses = javaxMailMessageClass.getDeclaredClasses();
    Class innerClass = null;
    for (int i = 0; i < innerClasses.length && innerClass == null; i++) {
      if (innerClasses[i].getName().indexOf("RecipientType") > 0) {
        innerClass = innerClasses[i];
      }
    }
    if (innerClass == null)
      throw new Exception("Unable to find inner class");
    Field field = innerClass.getDeclaredField("TO");
    setRecipientsArgs[0] = field.get(null);

    Class[] parseParameterTypes = new Class[2];
    parseParameterTypes[0] = toAddress.getClass();
    parseParameterTypes[1] = Boolean.TYPE;
    Method parseMethod = internetAddressClass.getMethod("parse",
        parseParameterTypes);

    Object[] parseArgs = new Object[2];
    parseArgs[0] = toAddress;
    parseArgs[1] = new Boolean(false);

    setRecipientsArgs[1] = parseMethod.invoke(null, parseArgs);
    parameterTypes = new Class[2];
    parameterTypes[0] = innerClass;
    /* Array of javax.mail.Address */
    parameterTypes[1] = addressArrayClass;
    Method setRecipientsMethod = mimeMessageClass.getMethod("setRecipients",
        parameterTypes);
    setRecipientsMethod.invoke(message, setRecipientsArgs);

    /* message.setSubject(subject); */
    parameterTypes = new Class[1];
    parameterTypes[0] = subject.getClass();
    Method setSubjectMethod = mimeMessageClass.getMethod("setSubject",
        parameterTypes);
    parms = new Object[1];
    parms[0] = subject;
    setSubjectMethod.invoke(message, parms);

    /* message.setText(body.toString()); */
    parameterTypes = new Class[1];
    parameterTypes[0] = "".getClass();
    Method setTextMethod = mimeMessageClass
        .getMethod("setText", parameterTypes);
    parms = new Object[1];
    parms[0] = body.toString();
    setTextMethod.invoke(message, parms);

    /* javax.mail.Transport.send(message); */

    parameterTypes[0] = javaxMailMessageClass;
    Method sendMethod = transportClass.getMethod("send", parameterTypes);
    parms[0] = message;
    // Retry 10 times.
    int retryCount = 10;
    while (retryCount > 0) {
      try {
        sendMethod.invoke(null, parms);
        retryCount = 0;
      } catch (Exception e) {
        System.out.println("Exception caught sending mail");
        if (retryCount <= 1) {
          throw e;
        }
        System.out.println("Sleeping for 2 seconds before retrying");
        Thread.sleep(2000);

        retryCount--;
      }
    }

    System.out.println("Message sent to " + toAddress);

  }

  /* We allow one * in the name */
  public static String[] getInitialsList(String initials) {
    String[] returnString = null;
    int starIndex = initials.indexOf("*");
    if (starIndex < 0) {
      returnString = new String[1];
      returnString[0] = initials;
    } else {
      String prefixCheck = null;
      String suffixCheck = null;
      if ((starIndex == 0) && (initials.length() == 1)) {
        // We use neither check to filter out results
      } else if (starIndex == 0) {
        suffixCheck = initials.substring(1);
      } else if (starIndex == initials.length() - 1) {
        prefixCheck = initials.substring(0, starIndex);
      } else {
        prefixCheck = initials.substring(0, starIndex);
        suffixCheck = initials.substring(starIndex + 1);
      }
      Vector initialsVector = new Vector();

      File outDirectory = new File("ct");

      if (outDirectory.isDirectory()) {
        String[] files = outDirectory.list();
        for (int i = 0; (files != null) && (i < files.length); i++) {
          System.out.println(files[i]);
          String outInitials = files[i];
          boolean found = true;
          if (outInitials.indexOf("runit") == 0) {
            if (outInitials.endsWith(".out")) {
              outInitials = outInitials.substring(5, outInitials.length() - 4);
              System.out.println("checking " + outInitials);
              if (prefixCheck != null) {
                found = outInitials.startsWith(prefixCheck);
              }
              if (found && suffixCheck != null) {
                found = outInitials.endsWith(suffixCheck);
              }

            } else {
              found = false;
            }
          } else {
            found = false;
          }

          if (found) {
            initialsVector.addElement(outInitials);
          }
        }
      }

      int returnSize = initialsVector.size();
      if (returnSize == 0) {
        System.out.println("Warning:  initials '" + initials
            + "' returned no matches");
      }
      returnString = new String[returnSize];
      for (int i = 0; i < returnSize; i++) {
        returnString[i] = (String) initialsVector.elementAt(i);
      }

    }
    return returnString;
  }

  public static void main(String args[]) {

    JDReport.noExit = true;

    Thread.currentThread().setName("JDRunit");
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

    try {
      if (args.length == 1 && args[0].equals("SHELL")) {
        runInterpreter(System.out, System.in, null);
      } else if (args[1].equals("REGRESSION") || args[1].equals("RERUNFAILED")
          || args[1].equals("RERUNFAILEDSTEPREPORT")
           || args[1].equals("REPORT")
          || args[1].equals("EMAIL") || (args[1].indexOf("EMAILTO-") == 0)) {
        String initials = args[0];
        if (on400) {
          Date d = new Date();
          JDJobName.sendProgramMessage("LD "+Thread.currentThread().getName()+" "  + d.toString()
              + " Running JDRunit " + initials);
        }

        for (int i = 1; i < args.length; i++) {
          String action = args[i];
          String[] initialsList = getInitialsList(initials);
          for (int j = 0; j < initialsList.length; j++) {

            System.out
                .println("\n-------------------------------------------------------");
            System.out.println("Action:  " + initials + " " + action);
            System.out
                .println("-------------------------------------------------------");
            initials = initialsList[j];

            if (action.equals("REGRESSION")) {
              runRegression(initials);
            } else if (action.equals("RERUNFAILED")) {
              rerunFailed(initials, false);
            } else if (action.equals("RERUNFAILEDSTEPREPORT")) {
              rerunFailed(initials, true);
            } else if (action.equals("EMAIL")) {
              email(initials, null);
            } else if (action.equals("REPORT")) {
              String[] reportArgs = new String[1];
              reportArgs[0] = initials;
              JDReport.main(reportArgs);
            } else if (args[1].indexOf("EMAILTO-") == 0) {
              String address = args[1].substring(8);
              email(initials, address);
            } else {
              System.out
                  .println("Warning:  Did not recognize action " + action);
            }
          } /* for j - initials list */
        } /* for i */

      } else if (args.length < 2) {
        usage();
        System.out.println("JDRunit:  Exit 2. Calling System.exit(2)");
        if (on400) {
          JDJobName
              .sendProgramMessage("JDRunit:  Exit 2. Calling System.exit(2)");
        }
        System.exit(2);
      } else { /* args.length < 2 */

        try {
          String initials = args[0];
          String[] initialsList = getInitialsList(initials);
          for (int i = 0; i < initialsList.length; i++) {
            initials = initialsList[i];
            System.out.println("JDRunit:  Running test using initials "
                + initials);

            JDRunit runit;
            String outputFile = TMP + "/runit" + initials + ".out." + pid + "."
                + nextRunNumber();
            if (args.length == 3) {
              runit = new JDRunit(initials, args[1], args[2], outputFile);
            } else {
              runit = new JDRunit(initials, args[1], null, outputFile);
            }

            runit.go();

          }
        } catch (Exception e) {
          e.printStackTrace();
          usage();
          System.out.println("JDRunit:  Exit 2. Calling System.exit(3)");
          if (on400) {
            JDJobName
                .sendProgramMessage("JDRunit:  Exit 2. Calling System.exit(3)");
          }
          System.exit(3);
        }
      }
      System.out.println("JDRunit:  DONE.. Calling System.exit(0)");
      if (on400) {
        JDJobName.sendProgramMessage("JDRunit:  DONE. Calling System.exit(0)");
      }
      /*
       * Call system exit to make sure we quit... We could have hung threads
       * still hanging around
       */

      System.exit(0);

    } catch (Throwable e) {
      System.out.println("JDRunit:  Exit 1. Calling System.exit(1)");
      e.printStackTrace();
      usage();
      System.out.println("JDRunit:  Exit 1. Calling System.exit(1)");
      if (on400) {
        JDJobName
            .sendProgramMessage("JDRunit:  Exit 1. Calling System.exit(1)");
      }
      System.exit(1);
    }

  }

  /*
   * LOCAL VARIABLES
   */

  String initials;
  String test;
  String vars;
  String outputFile;
  String driver;
  String jaCoCo;
  boolean useJaCoCo=false; 
  Properties iniProperties;
  String defaultLibrary;
  String javaHome;

  String javaArgs = "";
  String testTypeJavaArgs = "";
  String timeoutJavaArgs = " -Dtimeout=" + globalTimeout+" "; 
  String defaultJavaArgs = " -DshowVariationTimes -DshowVariationEndTime -Dos400.stdio.convert=Y -Dcom.ibm.as400.access.AS400.guiAvailable=false ";

  String extraJavaArgs = "";
  String testArgs = "";
  String finalArgs = "";
  String iaspArgs = "";
  String toolsJar = "";
  String toolboxJar = "/qibm/proddata/http/public/jt400/lib/jt400.jar";
  String jtopenliteJar = "";
  String defaultSecondary = "MEMEMEM";
  String otherSecondary = "MEMEMEM";
  String release;
  long  timeout = 0; 
  String[] cmdArray;

  String AS400;
  String SYSTEM;
  String SRDB;
  String USERID;
  String PASSWORD;
  String MASTERUSERID;
  String MASTERPASSWORD;
  String CLIWHDSN;
  String CLIWHUID;
  String CLIWHPWD;

  String CLIWGDSN;
  String CLIWGUID;
  String CLIWGPWD;

  String CLIWLDSN;
  String CLIWLUID;
  String CLIWLPWD;

  String CLIWZDSN;
  String CLIWZUID;
  String CLIWZPWD;

  String RDB;
  String testBase = "";

  Properties testBaseProperties = null;
  Properties dropAuthorityProperties = null;

  // Properties systemDriveProperties = null;

  public JDRunit(String initials, String test, String vars, String outputFile)
      throws Exception {
    this.initials = initials;
    this.outputFile = outputFile;
    this.test = test;
    this.vars = vars;
    this.timeout = globalTimeout; 
    iniInfo = new StringBuffer();

    initialize();


    if (test.indexOf("Limits") >= 0) {
	System.out.println("JDRunit increasing timeout for Limits test"); 
	timeout =  globalTimeout * 3;
	timeoutJavaArgs = " -Dtimeout=" + timeout+" ";
	System.out.println("timeoutJavaArgs="+timeoutJavaArgs); 
    }

    readIni();
    // Build the test args.

    int commaIndex;

    //
    // Determine the base test.. This is read from testbase.ini
    //
    FileInputStream fileInputStream = new FileInputStream("ini/testbase.ini");
    testBaseProperties = new Properties();
    testBaseProperties.load(fileInputStream);
    fileInputStream.close();

    //
    // Get the list of testcases that will drop authority
    //
    fileInputStream = new FileInputStream("ini/dropAuthority.ini");
    iniInfo.append("echo Loading ini from ini/dropAuthority.ini\n");
    dropAuthorityProperties = new Properties();
    dropAuthorityProperties.load(fileInputStream);
    fileInputStream.close();

    //
    // Get the list of system drives to use for IFS testcases on windows
    //

    // fileInputStream = new FileInputStream("ini/SYSTEMDRIVE.ini");
    // systemDriveProperties = new Properties();
    // systemDriveProperties.load(fileInputStream);
    // fileInputStream.close();

    String firstTest = test;
    commaIndex = firstTest.indexOf(",");
    if (commaIndex > 0) {
      firstTest = firstTest.substring(0, commaIndex);
    }

    testBase = testBaseProperties.getProperty(firstTest);
    if (testBase == null) {
      throw new Exception(
          "Could not get testbase from "
              + test
              + ".  Check ini/testbase.ini to make sure base test of variation exists");
    }

    // System.out.println("XXXX comparing testBase("+testBase+") to test("+test+")");
    if (testBase.equals(test)) {
      if (testBase.indexOf("test.util.") == 0) {
        testArgs = testBase;
      } else {
        testArgs = "test." + testBase;
      }
      // Increase the timeout for large tests
      timeout =  globalTimeout * 3;
      timeoutJavaArgs = " -Dtimeout=" + timeout+" "; 

    } else {
      
      if (testBase.indexOf("test.util.") == 0) {
        testArgs = testBase;
      } else {
        testArgs = "test." + testBase;
      }

      // Get the different testcases (we could have multiple)
      String left = test;
      commaIndex = left.indexOf(',');
      while (commaIndex > 0) {

        testArgs += " -tc " + left.substring(0, commaIndex).trim();
        left = left.substring(commaIndex + 1);
        commaIndex = left.indexOf(',');
      }
      testArgs += " -tc " + left.trim();

      if (vars != null) {
        testArgs += " -vars " + vars;
      }
    }

  }

  public static Properties getIniProperties() throws Exception {
    Properties iniProperties = new Properties();

    if (iniInfo == null)
      iniInfo = new StringBuffer();

    {
      iniInfo.append("echo Loading ini from ini/netrc.ini\n");
      String filename = "ini/netrc.ini";
      FileInputStream fileInputStream = new FileInputStream(filename);
      iniProperties.load(fileInputStream);
      fileInputStream.close();
    }

    {
      iniInfo.append("echo Loading ini from ini/notification.ini\n");
      String filename = "ini/notification.ini";
      FileInputStream fileInputStream = new FileInputStream(filename);
      iniProperties.load(fileInputStream);
      fileInputStream.close();
    }

    return iniProperties;

  }

  public static Properties getIniProperties(String initials) throws Exception {

    Properties iniProperties = getIniProperties();

    if (iniInfo == null)
      iniInfo = new StringBuffer();

    String localHost = InetAddress.getLocalHost().getHostName().toLowerCase();
    int dotIndex = localHost.indexOf(".");
    if (dotIndex >= 0) {
      localHost = localHost.substring(0, dotIndex);
    }

    // Use host specific file if necessary
    String filename = "ini/runit" + initials + "." + localHost + ".ini";
    File existFile = new File(filename);
    if (!existFile.exists()) {
      filename = "ini/runit" + initials + ".ini";
    }

    existFile = new File(filename);
    if (existFile.exists()) {
      iniInfo.append("echo Loading ini from " + filename + "\n");
      FileInputStream fileInputStream = new FileInputStream(filename);
      iniProperties.load(fileInputStream);
      fileInputStream.close();
    } else {
      // Load from the pieces of the initals
      filename = "ini/runit" + initials.substring(0, 2) + "xxx.ini";
      {
        iniInfo.append("echo Loading ini from " + filename + "\n");
        FileInputStream fileInputStream = new FileInputStream(filename);
        iniProperties.load(fileInputStream);
        fileInputStream.close();
      }

      filename = "ini/runitxx" + initials.substring(2, 4) + "x.ini";
      {
        iniInfo.append("echo Loading ini from " + filename + "\n");
        FileInputStream fileInputStream = new FileInputStream(filename);
        iniProperties.load(fileInputStream);
        fileInputStream.close();
      }

      filename = "ini/runitxxxx" + initials.substring(4, 5) + ".ini";
      {
        iniInfo.append("echo Loading ini from " + filename + "\n");
        FileInputStream fileInputStream = new FileInputStream(filename);
        iniProperties.load(fileInputStream);
        fileInputStream.close();
      }

      String description = iniProperties.getProperty("descriptionRelease")
          + " " + iniProperties.getProperty("descriptionJVM") + " "
          + iniProperties.getProperty("descriptionTest") + " ";

      iniProperties.put("description", description);
      
      String libraryPrefix = iniProperties.getProperty("libraryPrefix", "JDT");
      iniProperties.put("defaultLibrary", libraryPrefix + initials);

      // Override library prefix with variable
      libraryPrefix = System.getProperty("JDRunit.libraryPrefix");
      if (libraryPrefix != null) {
        iniProperties.put("defaultLibrary", libraryPrefix + initials);
      } else {
        try {
          libraryPrefix = (String) JDReflectionUtil.callStaticMethod_O(
              "java.lang.System", "getEnv", "JDRunit.libraryPrefix");
          if (libraryPrefix != null) {
            iniProperties.put("defaultLibrary", libraryPrefix + initials);
          }
        } catch (Exception e) {
          // Just ignore
        }
      }

      String overrideToolboxJar = iniProperties
          .getProperty("overrideToolboxJar");
      // System.out.println("overrideToolboxJar is "+overrideToolboxJar); 
      if (overrideToolboxJar != null) {
        String override = iniProperties.getProperty(overrideToolboxJar);
        if (override == null) { 
          throw new Exception("overrideToolboxJar="+overrideToolboxJar+" new property not found"); 
        }
        // System.out.println("...overridden to "+override); 
        iniProperties.put("toolboxJar", override);
      }

    } /* no ini file */

    {
      filename = "ini/systems.ini";
      iniInfo.append("echo Loading ini from " + filename + "\n");
      FileInputStream fileInputStream = new FileInputStream(filename);
      iniProperties.load(fileInputStream);
      fileInputStream.close();
    }

    String AS400 = iniProperties.getProperty("AS400");
    if (AS400 == null) {
      if (System.getProperty("os.name").indexOf("400") >= 0) {
        AS400 = InetAddress.getLocalHost().getHostName().toUpperCase();
        dotIndex = AS400.indexOf(".");
        if (dotIndex >= 0) {
          AS400 = AS400.substring(0, dotIndex);
        }
        iniProperties.put("AS400", AS400);
      }
    }

    return iniProperties;
  }

  public static String createProfile(String SYSTEM, String newUserid,
      String newPassword, String userid, String password, Vector inputVector) {
    if (newPassword == null)
      newPassword = "abc212cb";
    String sql = "notSet";
    try {
      String url = "jdbc:as400:" + SYSTEM;

      String[] jarLocations = { "jars/jt400.jar" };

      URL[] urls = new URL[1];
      for (int i = 0; i < jarLocations.length && urls[0] == null; i++) {
        File tryFile = new File(jarLocations[i]);
        if (tryFile.exists()) {
          try {
            urls[0] = new URL("file:" + jarLocations[i]);
          } catch (Exception e3) {
            e3.printStackTrace();
          }
        }
      }

      ClassLoader loader = new URLClassLoader(urls);
      Class c = loader.loadClass("com.ibm.as400.access.AS400JDBCDriver");
      if (debug) {
        System.out.println("Debug: Class loaded " + c);
      }
      Driver driver = (java.sql.Driver) c.newInstance();

      Properties conProperties = new Properties();
      conProperties.put("user", userid);
      conProperties.put("password", password);

      Connection con;
      try {
        con = driver.connect(url, conProperties);
      } catch (Exception e) {
        String message = e.toString();
        if (message.indexOf("cannot establish") >= 0) {
          url = "jdbc:as400:" + SYSTEM + ".rch.stglabs.ibm.com";
          con = driver.connect(url, conProperties);
        } else {
          throw e;
        }
      }

      Statement stmt = con.createStatement();

      try {
        /* Note CCSID must be 65535 for DDDMSQLCompatibility to pass */
        stmt.executeUpdate("CALL QSYS.QCMDEXC('"
            + "CRTUSRPRF USRPRF("
            + newUserid
            + ") PASSWORD("
            + newPassword
            + ") TEXT(''Toolbox testing profile'') ACGCDE(514648897) CCSID(65535)                                                                 ',   0000000160.00000)");
      } catch (Exception e) {
        // ignore an error from the create
      }
      sql = "CALL QSYS.QCMDEXC('"
          + "CHGUSRPRF USRPRF("
          + newUserid
          + ") PASSWORD(BOGUS7)                                                                        ',   0000000060.00000)";
      stmt.executeUpdate(sql);

      sql = "CALL QSYS.QCMDEXC('"
          + "CHGUSRPRF USRPRF("
          + newUserid
          + ") PASSWORD("
          + newPassword
          + ")  STATUS(*ENABLED) PWDEXPITV(90)                                                                                      ',   0000000100.00000)";
      stmt.executeUpdate(sql);

      con.close();
      rdbToCreatedUserid.put(SYSTEM, newUserid);
      rdbToCreatedPassword.put(SYSTEM, newPassword);

      System.out.println("Profile " + newUserid + " created");
      return newPassword;
    } catch (Exception e) {
      inputVector.addElement("echo Exception thrown creating profile "
          + newUserid);
      System.out.println("Error creating profile " + newUserid + " on "
          + SYSTEM);
      System.out.println("SQL='" + sql + "'");
      e.printStackTrace();
      inputVector.addElement("echo SQL='" + sql + "'");
      inputVector.addElement("echo exception = " + e);

      StringBuffer sb = new StringBuffer();

      JDTestcase.printStackTraceToStringBuffer(e, sb);

      int newlineIndex = sb.indexOf("\n");
      while (newlineIndex > 0) {
        sb.replace(newlineIndex, newlineIndex + 1, "\necho ");
        newlineIndex = sb.indexOf("\n", newlineIndex + 1);
      }
      inputVector.addElement("echo " + sb.toString());
      return null;
    }
  }

  public static String getAS400(Properties iniProperties, String initials) {
    String AS400 = iniProperties.getProperty("AS400");
    if (AS400 == null) {
      AS400 = iniProperties.getProperty(initials);
      if (AS400 == null) {
        if (System.getProperty("os.name").indexOf("400") >= 0) {
          try {
            AS400 = InetAddress.getLocalHost().getHostName().toUpperCase();
          } catch (Exception e) {
            AS400 = "localhost";
          }
          int dotIndex = AS400.indexOf(".");
          if (dotIndex >= 0) {
            AS400 = AS400.substring(0, dotIndex);
          }
        } else {
          AS400 = iniProperties.getProperty(initials.substring(0, 4));
          if (AS400 == null) {
            AS400 = iniProperties.getProperty(initials.substring(0, 2));
          }
        }
      }
    }
    if (AS400 == null) {
      System.out.println("Warning:  AS400 is null ");
      System.out.println(iniInfo.toString());

    }
    if ((AS400 != null) && (AS400.equals("UT51P43"))) {
      AS400 = "QQ740";
    }

    return AS400;
  }

  public void readIni() throws Exception {
    String filename = "ini/runit" + initials + ".ini";

    iniProperties = getIniProperties(initials);
    //
    // Verify the properties
    //
    StringBuffer sb = new StringBuffer();
    StringBuffer optional = new StringBuffer();
    boolean error = false;
    for (int i = 0; i < propertyInfo.length; i++) {
      if (propertyInfo[i][1].equals("REQUIRED")) {
        String prop = iniProperties.getProperty(propertyInfo[i][0]);
        if (prop == null) {
          sb.append("DID NOT FIND REQUIRED PROPERTY '" + propertyInfo[i][0]
              + "','" + propertyInfo[i][1] + "','" + propertyInfo[i][2] + "'\n");
          error = true;
        }
      } else if (propertyInfo[i][1].equals("OPTIONAL")) {
        String prop = iniProperties.getProperty(propertyInfo[i][0]);
        if (prop == null) {
          optional.append("WARNING: DID NOT FIND OPTIONAL PROPERTY  '"
              + propertyInfo[i][0] + "','" + propertyInfo[i][1] + "','"
              + propertyInfo[i][2] + "'\n");
        }

      } else {
        error = true;
        sb.append("ERROR with PROPERTY '" + propertyInfo[i][0] + "','"
            + propertyInfo[i][1] + "','" + propertyInfo[i][2] + "'\n");

      }
    }
    if (error) {
      System.out.println("JDRunit:  Configuration missing from " + filename);
      System.out.println(sb.toString());
      System.out.println(optional.toString());
      throw new Exception("JDRunit:  Configuration missing from " + filename);
    }

    //
    // get the variables we care about
    //

    javaHome = iniProperties.getProperty("javaHome");

    defaultLibrary = iniProperties.getProperty("defaultLibrary", "JDTESTLIB");

    driver = iniProperties.getProperty("driver", "native");
    jaCoCo = iniProperties.getProperty("JaCoCo", "false");
    if (jaCoCo.equals("true")) {
	useJaCoCo=true; 
    } 


    defaultSecondary = iniProperties.getProperty("defaultSecondary", "MEMEMEM");
    if (defaultSecondary == null)
      defaultSecondary = "MEMEMEM";
    otherSecondary = iniProperties.getProperty("otherSecondary", "MEMEMEM");
    if (otherSecondary == null)
      otherSecondary = "MEMEMEM";

    finalArgs = iniProperties.getProperty("finalArgs");

    AS400 = getAS400(iniProperties, initials);

    SYSTEM = iniProperties.getProperty("SYSTEM", AS400);
    SYSTEM = SYSTEM.toUpperCase();

    if (driver.equals("native")) {
	SYSTEM = JDDatabaseOverride.getDatabaseNameFromSystemName(SYSTEM); 
    } 


    String iasp = iniProperties.getProperty("IASP_" + SYSTEM);
    if (iasp != null) {
      iaspArgs = " -asp " + iasp + " ";
    } else {
      iaspArgs = " -asp IASP_NOT_DEFINED_IN_INI ";
    }

    if (on400) {
      if (SYSTEM.equals("DB2MB1P4")
|| SYSTEM.equals("B2PAR")
|| SYSTEM.equals("B1PT")
|| SYSTEM.equals("B1PX")
          || SYSTEM.equals("UT50P14")) {
        SYSTEM = "DB2M_LCL";
      }
    }
    RDB = iniProperties.getProperty("RDB");

    javaArgs += iniProperties.getProperty("javaArgs", "");
    if (javaArgs.length() == 0) {
      javaArgs = timeoutJavaArgs + defaultJavaArgs;
    } else {
	System.out.println("Adding timeoutJavaArgs("+timeoutJavaArgs+")"); 
      javaArgs += timeoutJavaArgs;
    }

    testTypeJavaArgs = iniProperties.getProperty("testTypeJavaArgs", "");
    if (testTypeJavaArgs.length() > 0) {
      javaArgs += " " + testTypeJavaArgs + " ";
    }

    /* IPv6 fix */
    javaArgs += " -Dcom.ibm.cacheLocalHost=true -Djava.net.preferIPv4Stack=true  -Djava.net.preferIPv6Addresses=false ";

    if (driver.equals("jcc")) {
      String jccPort = iniProperties.getProperty("jccPort", "50002");
      String jccDatabase = iniProperties.getProperty("jccDatabase", "XMLTEST");
      if (jccDatabase.equals("AS400")) {
        jccDatabase = iniProperties.getProperty("SYSTEM", AS400).toUpperCase();
      }
      String databaseType = iniProperties.getProperty("databaseType", "I");
      javaArgs = "-Xmx480m -Xms320m  -DdatabaseType=" + databaseType
          + "  -DjccPort=" + jccPort + " -DjccDatabase=" + jccDatabase + " "
          + javaArgs + " ";
    }

    release = iniProperties.getProperty("release");
    USERID = iniProperties.getProperty("USERID");
    PASSWORD = iniProperties.getProperty("PASSWORD");
    MASTERUSERID = iniProperties.getProperty("MASTERUSERID");
    MASTERPASSWORD = iniProperties.getProperty("MASTERPASSWORD");
    CLIWHDSN = iniProperties.getProperty("CLIWHDSN", "MEMEMEM"); //
    SRDB = CLIWHDSN.toUpperCase();
    CLIWHUID = iniProperties.getProperty("CLIWHUID", "DB2TEST");
    CLIWHPWD = iniProperties.getProperty("CLIWHPWD", "PASS2DB");
    javaArgs += "-DCLIWHDSN=" + CLIWHDSN + " -DCLIWHUID=" + CLIWHUID
        + " -DCLIWHPWD=" + CLIWHPWD + " ";

    CLIWGDSN = iniProperties.getProperty("CLIWGDSN", "MEMEMEM");
    CLIWGUID = iniProperties.getProperty("CLIWGUID", "DB2TEST");
    CLIWGPWD = iniProperties.getProperty("CLIWGPWD", "PASS2DB");
    javaArgs += "-DCLIWGDSN=" + CLIWGDSN + " -DCLIWGUID=" + CLIWGUID
        + " -DCLIWGPWD=" + CLIWGPWD + " ";

    CLIWLDSN = iniProperties.getProperty("CLIWLDSN", "CLIDB");
    CLIWLUID = iniProperties.getProperty("CLIWLUID", "DB2TEST");
    CLIWLPWD = iniProperties.getProperty("CLIWLPWD", "PASS2DB");
    javaArgs += "-DCLIWLDSN=" + CLIWLDSN + " -DCLIWLUID=" + CLIWLUID
        + " -DCLIWLPWD=" + CLIWLPWD + " ";

    CLIWZDSN = iniProperties.getProperty("CLIWZDSN", "STLEC2");
    CLIWZUID = iniProperties.getProperty("CLIWZUID", "admf001");
    CLIWZPWD = iniProperties.getProperty("CLIWZPWD", "n1cetest");
    javaArgs += "-DCLIWZDSN=" + CLIWZDSN + " -DCLIWZUID=" + CLIWZUID
        + " -DCLIWZPWD=" + CLIWZPWD + " ";

    if (AS400.equalsIgnoreCase(defaultSecondary)) {
      javaArgs += /* " -DAS400was="+AS400+ */ " -Djta.secondary.system=" + otherSecondary;
    } else {
      javaArgs += /* " -DAS400was="+AS400+ */   " -Djta.secondary.system=" + defaultSecondary;
    }

    String debugDriverPath = iniProperties.getProperty("debugDriverPath");
    if (debugDriverPath != null) {
      javaArgs += "-Dsun.boot.class.path=" + debugDriverPath + ":"
          + iniProperties.getProperty("sunBoot");
    }

    javaHome = iniProperties.getProperty("javaHome");
    toolsJar = iniProperties.getProperty("toolsJar");
    // try to determine from java.home
    if (toolsJar == null) {
      if (javaHome.indexOf("/QOpenSys") >= 0) {
        toolsJar = javaHome + "/lib/tools.jar";
      } else {
        toolsJar = "/qibm/proddata/Java400/jdk14/lib/tools.jar";
      }
    }
    if (debug)
      System.out.println("JDRunit: toolsJar is " + toolsJar);

    toolboxJar = iniProperties.getProperty("toolboxJar");
    if (toolboxJar == null) {
      toolboxJar = "/qibm/proddata/http/public/jt400/lib/jt400.jar";
    }

    if (useJaCoCo) {
      if (toolboxJar.indexOf("java6") > 0) {
        toolboxJar = "/home/jdbctest/jacoco/java6/jt400.jar";
      } else if (toolboxJar.indexOf("java8") > 0) {
        toolboxJar = "/home/jdbctest/jacoco/java8/jt400.jar";
      } else if (toolboxJar.indexOf("java9") > 0) {
        toolboxJar = "/home/jdbctest/jacoco/java9/jt400.jar";
      } else {
        toolboxJar = "/home/jdbctest/jacoco/java0/jt400.jar";
      }
    }


    jtopenliteJar = iniProperties.getProperty("jtopenliteJar");
    if (jtopenliteJar == null) {
      jtopenliteJar = "";
    }

    //
    // Determine if inherited properties need to be set
    //
    Properties systemProperties = System.getProperties();
    Enumeration keys = systemProperties.keys();
    while (keys.hasMoreElements()) {
      String property = (String) keys.nextElement();
      for (int i = 0; i < inheritedProperties.length; i++) {
        if (property.indexOf(inheritedProperties[i]) == 0) {
          String value = System.getProperty(property);
          if (value != null) {
            if (property.equals("com.sun.management.jmxremote.port")) {
              int port = Integer.parseInt(value);
              port++;
              value = "" + port;
            }
            javaArgs += " -D" + property + "='" + value+"'";

          }
        }

      }

      /* Use -D-D to indicate that a property should be set for the next level */
      // Just pass through any - properties
      if (property.indexOf("-") == 0) {
        // System.out.println("Processing property"+property);
        String value = System.getProperty(property);
        if (value != null && value.length() > 0) {
          javaArgs += " " + property + "=" + value;
        } else {
          javaArgs += " " + property;
        }
      } else {
        // System.out.println("Not processing property:"+property);
      }

      for (int i = 0; i < convertToXProperties.length; i++) {
        if (property.indexOf(convertToXProperties[i]) == 0) {
          String value = System.getProperty(property);
          if (value != null) {
            if (value.length() == 0) {
              javaArgs += " -X" + property;
            } else {
              javaArgs += " -X" + property + "=" + value;
            }
          }
        }
      }

      if (property.equals("startDebugger")) {
        javaArgs += " -Xrunjdwp:transport=dt_socket,server=y,suspend=n ";
      }
      if (property.equals("startDebuggerSuspend")) {
        javaArgs += " -Xrunjdwp:transport=dt_socket,server=y,suspend=y ";
      }

    } /* keys.hasMoreElements() */

  } /* readIni */

  public void setExtraJavaArgs(String extraJavaArgs) {
    if (extraJavaArgs == null) {
      this.extraJavaArgs = "";
    } else {
      this.extraJavaArgs = extraJavaArgs;
    }
  }

  public static void dualPrintln(PrintStream ps, PrintWriter pw, String stuff) {
    ps.println(stuff);
    pw.println(stuff);
  }

  /*
   * Go will run the test in a separate process and store the output in the
   * appropriate location.  Returns the number of failures. 
   */
  public int go() throws Exception {
     
    int failedCount = 0; 
    boolean forceReport = false;
    String osname = System.getProperty("os.name");
    long thisRunNumber = nextRunNumber();
    String runitInputFile = TMP + "/runit" + initials + ".in." + pid + "."
        + thisRunNumber;
    String runitOutputFile;
    if (outputFile != null) {
      runitOutputFile = outputFile;
    } else {
      runitOutputFile = TMP + "/runit" + initials + ".out." + pid + "."
          + thisRunNumber;
    }
    String results = "ct/runit" + initials + ".out";
    boolean fatalError = false;
    String fatalErrorString = "";
    String fatalErrorMessage = "";
    boolean isClassic = System.getProperty("java.vm.name").indexOf("Classic") >= 0;
    boolean isV5R4 = System.getProperty("os.version").equals("V5R4M0");
    boolean isV6R1 = System.getProperty("os.version").equals("V6R1M0");
    String display = ":8";

    Vector inputVector = new Vector();

    if (debug) {
      System.out.println("JDRunit: Building the script");
    }

    PrintWriter writer = new PrintWriter(new FileWriter(runitInputFile));

    inputVector.addElement("echo Running " + initials + " " + test);
    inputVector.addElement("echo input is " + runitInputFile);
    inputVector.addElement("echo output is " + runitOutputFile);
    inputVector.addElement(iniInfo.toString());
    inputVector.addElement("echo cd /home/jdbctest");
    inputVector.addElement("cd /home/jdbctest");

    String javaExecPath = javaHome;
    //
    // Fixup the path when running in CYGWIN
    //
    if (javaExecPath.indexOf(":") == 1) {
      javaExecPath = "/" + javaExecPath.charAt(0) + javaExecPath.substring(2);
    }

    inputVector.addElement("PATH=\"" + javaExecPath + "/bin:$PATH\"");

    String classpath = iniProperties.getProperty("classpath");
    if (classpath != null) {
	    inputVector.addElement("CLASSPATH=" + classpath);
    } else {
      inputVector.addElement("CLASSPATH=.");
    }

    inputVector.addElement("echo Setting JAVA_HOME to " + javaHome);
    inputVector.addElement("JAVA_HOME=\"" + javaHome + "\"");
    inputVector.addElement("export JAVA_HOME");
    if (javaHome.indexOf("/QOpenSys/pkgs/lib/jvm") >=0) {
	inputVector.addElement("PATH="+javaHome+"/bin:$PATH");
	inputVector.addElement("export PATH"); 
    }


    String paseTranslate =iniProperties.getProperty("QIBM_JDBC_PASE_TRANSLATE");
    if (paseTranslate != null) {
	inputVector.addElement("echo Setting QIBM_JDBC_PASE_TRANSLATE to " + paseTranslate);
	inputVector.addElement("QIBM_JDBC_PASE_TRANSLATE=\"" + paseTranslate + "\"");
	inputVector.addElement("export QIBM_JDBC_PASE_TRANSLATE");
    }

    String newTestJar = iniProperties.getProperty("testJar");
    if (newTestJar != null) {
      testcaseCode = newTestJar; 
    }

    

    /* Note: sslightx.zip still needed for JDK 1.3 tests */
    String setClasspath = "CLASSPATH="+testcaseCode+":"
        + toolboxJar
        + ":/qibm/proddata/java400/ext/translator.zip:"
        + toolsJar
        + ":/qibm/proddata/VE2/EWLMMS/classes/arm4.jar:jars/jt400Servlet.jar:jars/servlet.jar:jars/sslightx.zip:jars/jcifs.jar:jars/fscontext.jar:jars/providerutil.jar";

    /* Only add jcc jars if addJccJars is defined */

    if (iniProperties.getProperty("addJccJars") != null) {
      String jccJars = iniProperties.getProperty("jccJars");
      if (jccJars != null) {
        setClasspath += ":" + jccJars;
      }
    }

    if (jtopenliteJar.length() > 0) {
      setClasspath += ":" + jtopenliteJar;
    }

    if (iniProperties.getProperty("CLIENTOS", "AS400").equals("WINDOWS")) {

      /* On windows, we copy the toolbox jar so that the original can be updated */
      /*
       * On windows, the JVM put a lock on the jar file which prevents it from
       * being updated
       */
      /* To get around this,we make a copy in C:\\runningJars */
      /*
       * We will attempt to delete this extra copies each time this program is
       * run
       */

      File runningJarsDirectory = new File("C:\\runningjars");
      if (!runningJarsDirectory.exists()) {
        runningJarsDirectory.mkdir();
      }

      File[] files = runningJarsDirectory.listFiles();
      if (files != null) {
        for (int i = 0; i < files.length; i++) {
          String filename = files[i].getName();
          if (filename.indexOf(initials) > 0) {
            try {
              files[i].delete();
            } catch (Exception e) {
              System.out.println("Exception deleting " + files[i]);
              e.printStackTrace();
            }
          }
        }
      }

      /* Copy the file */
      String newToolboxJar = "C:\\runningjars\\jt400."
          + (System.currentTimeMillis() / 1000) + "." + initials + ".jar";
      File newToolboxJarFile = new File(newToolboxJar);

      FileInputStream inStream = new FileInputStream(toolboxJar);
      FileOutputStream outStream = new FileOutputStream(newToolboxJarFile);

      byte[] buffer = new byte[4096];

      int length;
      while ((length = inStream.read(buffer)) > 0) {
        outStream.write(buffer, 0, length);
      }

      inStream.close();
      outStream.close();

      toolboxJar = newToolboxJar;

      toolsJar = iniProperties.getProperty("toolsJar");
      if (toolsJar == null) {
        toolsJar = javaHome.replace('/', '\\') + "\\lib\\tools.jar";
      }
      String jccJars = "";
      if (iniProperties.getProperty("addJccJars") != null) {
        jccJars = iniProperties.getProperty("jccJars");
        if (jccJars == null) {
          jccJars = "";
        }
      }

      String currentClasspath = System.getProperty("java.class.path");
      
      if (testcaseCode.indexOf(".jar") < 0) {
        testcaseCode=System.getProperty("user.dir"); 
      } else {
        testcaseCode=System.getProperty("user.dir") +"\\"+testcaseCode; 
      }
      setClasspath = "CLASSPATH=\"" +  testcaseCode+ ";"
    	  + currentClasspath+";"
          + toolboxJar + ";" + toolsJar + ";" + System.getProperty("user.dir")
          + "\\jars\\db2_classes.jar;" + System.getProperty("user.dir")
          + "\\jars\\fscontext.jar;" + System.getProperty("user.dir")
          + "\\jars\\providerutil.jar;" + System.getProperty("user.dir")
          + "\\jars\\jt400Servlet.jar;" + System.getProperty("user.dir")
          + "\\jars\\servlet.jar;" + System.getProperty("user.dir")
          + "\\jars\\jcifs.jar;" + System.getProperty("user.dir")
          + "\\jars\\sslightx.zip;" +

          jccJars + ";" +

          /*
           * iniProperties.get("SQLLIB")+"\\java\\db2jcc4.jar;" +
           * iniProperties.get("SQLLIB")+"\\java\\db2jcc_license_cisuz.jar;"+
           * iniProperties.get("SQLLIB")+"\\java\\db2jcc_license_cu.jar;"+
           */

          jtopenliteJar + "\"";

      /* NOT WINDOWS */
    } else if (iniProperties.getProperty("CLIENTOS", "AS400").equals("LINUX")) {

      toolsJar = iniProperties.getProperty("toolsJar");
      if (toolsJar == null) {
        toolsJar = javaHome + "/lib/tools.jar";
      }

      setClasspath = "CLASSPATH=\""+testcaseCode+":" + toolboxJar + ":"
          + toolsJar + ":" + System.getProperty("user.dir")
          + "/jars/db2_classes.jar:" + System.getProperty("user.dir")
          + "/jars/fscontext.jar:" + System.getProperty("user.dir")
          + "/jars/providerutil.jar:" + System.getProperty("user.dir")
          + "/jars/jt400Servlet.jar:" + System.getProperty("user.dir")
          + "/jars/servlet.jar:" + System.getProperty("user.dir")
          + "/jars/sslightx.zip:" + System.getProperty("user.dir")
          + "/jars/jcifs.jar" + "\"";

      if (System.getProperty("user.name").equalsIgnoreCase("JAVA")) {
        display = ":9";
      }
      inputVector.addElement("echo Starting vncserver   -SecurityTypes=VncAuth" + display);
      inputVector.addElement("vncserver   -SecurityTypes=VncAuth " + display);

      //

      inputVector.addElement("DISPLAY=" + display);
      inputVector.addElement("export DISPLAY=" + display);
      inputVector.addElement("echo DISPLAY is $DISPLAY");

    } else { /* NOT LINUX */
      /* This causes Applet to blow up with headless exception */
      /* javaArgs += " -Djava.awt.headless=true "; */

      /* Not LINUX or WINDOWS -- must be OS400 */
      /* inputVector.addElement("echo SYSTEM WRKSYSSTS"); */
      /* inputVector.addElement("system wrksyssts"); */
	if (javaHome.indexOf("/QOpenSys/pkgs/lib/jvm") >=0) {
	    setClasspath=setClasspath+"\":/QIBM/ProdData/OS400/Java400/ext/db2_classes11.jar\"";
	} 


      /* Setup VNC and use it */

      // vncPort = 5900 + $displayNumber;
      int vncPort = 5908;
      if (System.getProperty("user.name").equalsIgnoreCase("JAVA")) {
        display = ":9";
        vncPort = 5909;
      }
      // Determine if VNC needs to be started
      try {
        Socket s = new Socket("localhost", vncPort);
        s.close();
      } catch (Exception e) {
        // socket not available so start server

        inputVector.addElement("echo LOGNAME=$LOGNAME HOME=$HOME");

        inputVector.addElement("mkdir -p ~/.vnc");
        inputVector.addElement("touch -C 819 ~/.vnc/passwd");
        inputVector.addElement("echo 'pas4t8st7' >> ~/.vnc/passwd");
        inputVector.addElement("chmod 700 ~/.vnc/passwd");

        inputVector.addElement("touch " + TMP + "/vncstart.out");
	String vncServerCommand = "/QOpenSys/QIBM/ProdData/DeveloperTools/vnc/vncserver "
                + display + "  -SecurityTypes=VncAuth  > " + TMP + "/vncstart.out 2>&1";
	inputVector.addElement("Starting VNC server with "+vncServerCommand); 
        inputVector
            .addElement(vncServerCommand);

        // Note: In some rare cases, the VNC server may fail to start with this
        // message:
        // Warning: FOWGAI2.RCHLAND.IBM.COM:9 is taken because of
        // /tmp/.X11-unix/X9
        // Remove this file if there is no X server FOWGAI2.RCHLAND.IBM.COM:9

        // Detect the error, remove the file, and restart the server
        inputVector.addElement("cat " + TMP + "/vncstart.out");
        inputVector.addElement("grep '" + TMP + "/.X11-unix' " + TMP
            + "/vncstart.out | sed 's%^.*" + TMP + "\\(.*\\)%rm -f " + TMP
            + "\\1; /QOpenSys/QIBM/ProdData/DeveloperTools/vnc/vncserver   -SecurityTypes=VncAuth"
            + display + "%' | sh ");
        inputVector.addElement("rm -f " + TMP + "/vncstart.out");

      }

      inputVector.addElement("DISPLAY=" + display);
      inputVector.addElement("export DISPLAY=" + display);
      inputVector.addElement("echo DISPLAY is $DISPLAY");

      inputVector.addElement("echo qsh -c 'SYSTEM WRKSYSSTS'");
      inputVector.addElement("qsh -c 'system wrksyssts' < /dev/null");
      // Needed for PASE tests on 7.4 and 7.5
      inputVector.addElement("echo export PASE_DEFAULT_UTF8=N");
      inputVector.addElement("PASE_DEFAULT_UTF8=N");
      inputVector.addElement("export PASE_DEFAULT_UTF8=N");
      inputVector.addElement("echo PASE_DEFAULT_UTF8=$PASE_DEFAULT_UTF8"); 

    } /* not linux or windows */

    inputVector.addElement("echo " + setClasspath);
    inputVector.addElement(setClasspath);
    inputVector.addElement("export CLASSPATH");
    inputVector.addElement("date");
    inputVector.addElement("echo CLASSPATH is $CLASSPATH");
    inputVector.addElement("echo PATH is $PATH");
    String shellBinary = iniProperties.getProperty("shell");
    inputVector.addElement("echo SHELL should be " + shellBinary);
    inputVector.addElement("echo SHELL is $SHELL");
    /*
     * Didn't help STDIO problem // // Add code to fix SHELL if is lying // if
     * (shellBinary.indexOf("/QOpenSys") >= 0) {
     * inputVector.addElement("if [ \"$SHELL\" = \"/usr/bin/qsh\" ] ; then ");
     * inputVector.addElement("PID=$$"); inputVector.addElement(
     * "ps -ef | grep $PID | grep -v ps | grep -v grep | grep -v sed | sed 's/^.* \\([^ ][^ ]*\\) *$/\\1/' > /tmp/shell.$$"
     * ); inputVector.addElement("CALCSHELL=`cat /tmp/shell.$$`");
     * inputVector.addElement("CALCSHELL=$CALCSHELL");
     * inputVector.addElement("rm /tmp/shell.$$"); inputVector.addElement(
     * "if [ \"$CALCSHELL\" = \"/QOpenSys/usr/bin/sh\" ] ; then");
     * inputVector.addElement("SHELL=\"/QOpenSys/usr/bin/sh\"");
     * inputVector.addElement("echo FIXED SHELL TO $SHELL");
     * inputVector.addElement("fi"); inputVector.addElement("fi");
     * inputVector.addElement("echo calling which java");
     * inputVector.addElement("which java"); }
     */
    inputVector.addElement("echo PID is $$");
    inputVector.addElement("echo PPID is $PPID");
    inputVector.addElement("ps -ef | grep $PPID");
    if (debug) {
      inputVector.addElement("echo variables are ");
      inputVector.addElement("echo '------------------------'");
      inputVector.addElement("set");
      inputVector.addElement("echo '------------------------'");
    }
    inputVector.addElement("echo which java");
    inputVector.addElement("which java");
    inputVector.addElement("echo java -version");
    inputVector.addElement("java -version");
    String extraCommand = iniProperties.getProperty("extraCommand");
    if (extraCommand != null) {
      inputVector.addElement("echo " + extraCommand);
      inputVector.addElement(extraCommand);
    }

    /* determine if more memory is needed. This is the case for the Sun */
    /* JVM on linux */
    if (javaHome.indexOf("sun-jdk15") >= 0) {
      javaArgs += " -Xmx500m ";
    }

    javaArgs += " " + extraJavaArgs;

    /* Determine if a lower level user id is needed */
    String testUserid = USERID;
    String testPassword = PASSWORD;
    String newUserid = (String) dropAuthorityProperties.get(testBase);
    if (newUserid == null) {
      newUserid = (String) dropAuthorityProperties.get(test);
    }
    if (newUserid != null) {
      String newPassword = "j8vateam";
      int semicolonIndex = newUserid.indexOf(';');
      if (semicolonIndex > 0) {
        newPassword = newUserid.substring(semicolonIndex + 1);
        newUserid = newUserid.substring(0, semicolonIndex);
      }
      /* Check to see if profile created */
      String createdUserid = (String) rdbToCreatedUserid.get(SYSTEM);
      if (createdUserid == null || !createdUserid.equals(newUserid)) {

        /* create the profile for the test on the system */
        testUserid = newUserid;
        testPassword = createProfile(SYSTEM, newUserid, newPassword, USERID,
            PASSWORD, inputVector);
        if (testPassword == null) {
          inputVector
              .addElement("Unable to create profile. setting password to new anyway");
          testPassword = newPassword;
        }

      } else {
        /* Use what was already set up */
        testUserid = newUserid;
        testPassword = (String) rdbToCreatedPassword.get(SYSTEM);
      }

    }

    /* -directory is used by the ftp testcase and tell where to find */
    /* file on the AS/400. If the FTP testcase are first run on the */
    /* AS/400, then the file will be there when the Windows/linux */
    /* clients run these tests. */

    /* In V7R1 on 3/7/2013 on lp01ut18 -- the testcases no longer run unless */
    /* -Dos400.stdio.convert=Y. Rather than fix all the runit.ini file */
    /* the fix will be done here */

    /* In V7R1 on 8/5/2013 on lp01ut18 -- testcases now fail with */
    /* -Dos400.stdio.convert=Y */
    if (release.equals("v7r1m0")) {
      int convertIndex = javaArgs.indexOf("-Dos400.stdio.convert=");
      if (convertIndex >= 0) {
        int spaceIndex = javaArgs.indexOf(" ", convertIndex);
        if (spaceIndex < 0) {

          javaArgs = javaArgs.substring(0, convertIndex);

        } else {
          if (convertIndex > 0) {
            javaArgs = javaArgs.substring(0, convertIndex)
                + javaArgs.substring(spaceIndex);
          } else {
            javaArgs = javaArgs.substring(spaceIndex);
          }
        }
      }
      /* Note: 3/21/2013 -- x1423p1 is failing.. Change back if */
      /* that system. */
      if (SYSTEM.toUpperCase().indexOf("X1423P1") >= 0) {
        javaArgs += "-DsystemIsX1423p1 -Dos400.stdio.convert=N";
      } else {
        /* 8/5/2013 change back to N */
        javaArgs += "-Dos400.stdio.convert=N";
      }

    }

    /* Move this earlier so that it can be added to the job log */

    String AS400lc = AS400.toLowerCase();
    int dotIndex = AS400lc.indexOf('.');
    if (dotIndex > 0) {
      AS400lc = AS400lc.substring(0, dotIndex);
    }

    String sep = System.getProperty("file.separator");

    Date date = getTestDate();

    Calendar calendar = new GregorianCalendar(
        TimeZone.getTimeZone("America/Chicago"));
    calendar = new GregorianCalendar();
    calendar.setTime(date);

    String dateString = calendar.get(Calendar.YEAR) + "-"
        + twoDigitNumber((1 + calendar.get(Calendar.MONTH))) + "-"
        + twoDigitNumber(calendar.get(Calendar.DAY_OF_MONTH)) + " "
        + twoDigitNumber(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
        + twoDigitNumber(calendar.get(Calendar.MINUTE)) + ":"
        + twoDigitNumber(calendar.get(Calendar.SECOND));

    /* Dont use colons in this date because Window doesn't like colons */
    String dateStringNoSpace = calendar.get(Calendar.YEAR) + "-"
        + twoDigitNumber((1 + calendar.get(Calendar.MONTH))) + "-"
        + twoDigitNumber(calendar.get(Calendar.DAY_OF_MONTH)) + "-"
        + twoDigitNumber(calendar.get(Calendar.HOUR_OF_DAY)) + "."
        + twoDigitNumber(calendar.get(Calendar.MINUTE)) + "."
        + twoDigitNumber(calendar.get(Calendar.SECOND));

    String savefileName = "ct" + sep + "out" + sep + initials + sep + "runit."
        + dateStringNoSpace;
    if (osname.indexOf("Windows") >= 0) {
      savefileName = savefileName.replace(':', '.');
    }

    String parentJob = "UNKNOWN";
    if (on400) {
      parentJob = JDJobName.getJobName();
    }

    // Specify the output file to the test
    javaArgs = " -Dtest.testcase=" + initials + "." + test
        + " -Dtest.outputFile=" + runitOutputFile + " -Dtest.saveFile="
        + savefileName + " -Dtest.parentJob=" + parentJob + " " + javaArgs;

    if (useJaCoCo) {
	File destDir = new File("/home/jdbctest/jacoco/"+initials);
	if (!destDir.exists()) {
	    destDir.mkdir(); 
	}
	javaArgs = "-javaagent:/home/jdbctest/jacoco/lib/jacocoagent.jar=destfile=/home/jdbctest/jacoco/"+initials+"/"+test+".exec,includes=com.ibm.as400.access.* " + javaArgs; 
    } 

    String javaCommand = "java " + javaArgs + " " + testArgs + " -lib "
        + defaultLibrary + " -system " + SYSTEM;
    if (RDB != null) {
      javaCommand += " -rdb " + RDB;
    }
    javaCommand += " -uid  " + testUserid + " -pwd " + testPassword
        + " -pwrSys " + USERID + "," + PASSWORD + " -directory /  -misc "
        + driver + "," + release + iaspArgs;
    if (finalArgs != null && finalArgs.length() > 0) {
      javaCommand = javaCommand + " " + finalArgs;
    }

    if (finalArgs != null) {
      int proxyIndex = finalArgs.indexOf("-proxy");
      if (proxyIndex >= 0) {
        String loopbackInfo = finalArgs.substring(proxyIndex + 6).trim();
        if (loopbackInfo.indexOf("loopback") == 0) {
	    
          startLoopbackProxy(toolboxJar);
        } else {
          System.out.println("WARNING.  DID NOT HANDLE PROXY in " + finalArgs
              + " loopbackInfo=" + loopbackInfo);
        }
      }
    }

    inputVector.addElement("echo " + javaCommand);
    inputVector.addElement(javaCommand);

    inputVector.addElement("\n\n\n\n");
    inputVector.addElement("echo DONE");
    inputVector.addElement("echo exit");
    inputVector.addElement("exit");

    Enumeration enumeration = inputVector.elements();
    while (enumeration.hasMoreElements()) {
      String line = (String) enumeration.nextElement();
      writer.println(line);
    }

    writer.flush();
    writer.close();

    if (debug) {
      System.out.println("JDRunit: INPUT SCRIPT IS ");
      System.out.println("-------------------------------------------------");
      JDJSTPTestcase.cat(runitInputFile);
      System.out.println("-------------------------------------------------");
    }

    /* Run the script in a shell process */
    String[] cmdArray1;
    String shellCommand;
    String shellArgs = iniProperties.getProperty("shellArgs");
    if (shellArgs == null) {
      cmdArray1 = new String[1];
      cmdArray1[0] = shellBinary;
      System.out.println("Shell binary1 is " + shellBinary);
      shellCommand = cmdArray1[0];
      /* Fix the command line if running on windows in */
      if (debug)
        System.out.println("JDRunit: os.name=" + osname);
      if (osname.indexOf("Windows") >= 0 && cmdArray1[0].indexOf("/bin") >= 0) {
        //
        // Get CYGWIN location from user.dir
        //
        String userDir = System.getProperty("user.dir");
        int homeIndex = userDir.indexOf("\\home\\");
        if (homeIndex >= 0) { 
             cmdArray1[0] = userDir.substring(0, homeIndex)
                + cmdArray1[0].replace('/', '\\');
        } else {
        	 cmdArray1[0] = "C:\\etlocalinst\\cygwin"+  cmdArray1[0].replace('/', '\\');
        }
      }

      if (debug)
        System.out.println("JDRunit: Shell command is: " + cmdArray1[0]);

    } else {

      cmdArray1 = new String[2];
      cmdArray1[0] = shellBinary;
      System.out.println("Shell binary2 is " + shellBinary);
      shellCommand = cmdArray1[0];
      cmdArray1[1] = shellArgs;
      if (debug)
        System.out.println("JDRunit: Shell command (with args) is: "
            + cmdArray1[0] + " " + cmdArray1[1]);
    }
    if (on400) {
      JDJobName.sendProgramMessage("GD "+Thread.currentThread().getName()+" " + date.toString() + " Running "
          + initials + " " + testArgs);
    } 

    Process shellTestProcess;
    String shellTestProcessInfo = "";
    // If current JVM is PASE, but target JVM is classic. Don't inherit
    // any environment variables.
    // This scenario should only typically happened on V6R1
    if ((System.getProperty("java.home").indexOf("QOpenSys") > 0)
        && (javaHome.indexOf("QOpenSys") == -1)) {
      String[] envp = { "PATH=/usr/bin:", "HOME=/home/jdbctest", };
      if (debug)
        System.out.println("Calling exec1 with envp and cmdArray[0]="
            + cmdArray1[0]);
      shellTestProcess = runtime.exec(cmdArray1, envp);
      shellTestProcessInfo = "Running with envp cmdArray1[" + cmdArray1.length
          + "] ";
      for (int i = 0; i < cmdArray1.length; i++) {
        shellTestProcessInfo += cmdArray1[i] + " ";
      }
    } else {
      if (debug)
        System.out.println("Calling exec2 with cmdArray[0]=" + cmdArray1[0]);
      shellTestProcess = runtime.exec(cmdArray1);
      shellTestProcessInfo = "Running  cmdArray1[" + cmdArray1.length + "]";
      for (int i = 0; i < cmdArray1.length; i++) {
        shellTestProcessInfo += " " + cmdArray1[i];
      }

    }

    /* Setup the input */
    OutputStream stdin = shellTestProcess.getOutputStream();
    JDJSTPInputThread inputThread;
    int encoding = JDJSTPOutputThread.ENCODING_UNKNOWN;
    if (on400 && isClassic && isV5R4) {
      // Need to translate input stream to EBCIDC
      if (debug)
        System.out.println("InputStream is EBCDIC");
      inputThread = new JDJSTPInputThread(stdin, runitInputFile, "IBM037");
      encoding = JDJSTPOutputThread.ENCODING_EBCDIC;
    } else {
      if (on400 && isV6R1 && shellCommand.equals("/usr/bin/sh")) {
        if (debug)
          System.out.println("InputStream is EBCDIC");
        inputThread = new JDJSTPInputThread(stdin, runitInputFile, "IBM037");
        encoding = JDJSTPOutputThread.ENCODING_EBCDIC;
      } else {
        if (debug)
          System.out.println("InputStream is ASCII");
        inputThread = new JDJSTPInputThread(stdin, runitInputFile);
        encoding = JDJSTPOutputThread.ENCODING_ASCII;
      }
    }

    if (debug)
      JDJSTPTestcase.debug = true;

    /* Assynchronously start the process output threads */
    Vector hangMessagesFound = new Vector();

    JDJSTPOutputThread stdoutThread = JDJSTPTestcase.startProcessOutput(
        shellTestProcess, runitOutputFile, true, hangMessages, hangMessagesException,
        hangMessagesFound, encoding);

    /* start the input after output is ready */
    inputThread.start();

    /* Set the timeout for the process */
    long startTime = System.currentTimeMillis();
    long endTime = startTime + timeout;

    /* Wait for the process to end */
    // System.out.println("JDEBUG: waiting for process to end");
    long currentTime = System.currentTimeMillis();
    // System.out.println("JDEBUG: process checking "+currentTime+"<"+endTime);

    boolean processCompleted = false;
    // There is not a wait mechanism for the process.
    // So loop and wait for process completion
    currentTime = System.currentTimeMillis();
    while (!processCompleted && currentTime < endTime
        && hangMessagesFound.size() == 0) {
      // System.out.println("JDEBUG: process checked " + currentTime + "<"+
      // endTime);
      try {
        int rc = shellTestProcess.exitValue();
        processCompleted = true;
        System.out.println("Process completed with rc=" + rc);
        if (rc != 0) {
          System.out.println("Running info: " + shellTestProcessInfo);
          if (on400) {
            Date d = new Date();
            JDJobName.sendProgramMessage("LD " +Thread.currentThread().getName()+" " + d.toString() + " Rc=" + rc
                + " from " + initials + " " + testArgs);

          }

        }
      } catch (IllegalThreadStateException itse) {
        /* Process not done.. Delay for 1 second */
        try {
          Thread.sleep(1000);
        } catch (Exception sleepEx) {
          /* ignore */
        }
      }
      currentTime = System.currentTimeMillis();
    }

    stdoutThread.addOutputString("Done at " + (new Date()) + "\n");
    if (hangMessagesFound.size() > 0) {
      StringBuffer body = new StringBuffer();
      String outString = " Severe Error Running  " + initials + " " + testArgs
          + "\n";
      stdoutThread.addOutputString(outString);
      body.append(outString);
      if (on400) {
        Date d = new Date();
        JDJobName.sendProgramMessage("LD " +Thread.currentThread().getName()+" " + d.toString() + outString);
      }

      String hangMessage = (String) hangMessagesFound.elementAt(0);
      outString = "ERROR:  JDRunit: Hang message found : " + hangMessage + "\n";
      stdoutThread.addOutputString(outString);
      body.append(outString);
      System.out.print(outString);
      if (on400) {
        Date d = new Date();
        JDJobName.sendProgramMessage("LD "+Thread.currentThread().getName()+" "  + d.toString() + outString);
      }

      //
      // Check to see if we can recover
      //
      boolean recovered = false;
      if (on400) {
        if ((hangMessage.indexOf("sun.awt.X11GraphicsEnvironment") > 0)
            || (hangMessage.indexOf("connection to \":9.0\"") > 0)) {
          /* Something went wrong with the VNC server */
          /* kill it */
          String command = "SBMJOB CMD(QSH CMD('system wrkactjob | grep vnc | "
              + "grep -i "
              + System.getProperty("user.name")
              + " | "
              + " sed ''s%^ *\\([^ ][^ ]*\\) *\\([^ ][^ ]*\\) *\\([^ ][^ ]*\\) .*%system endjob \"job(\\3/\\2/\\1)\"%'' |"
              + " sh'))";
          System.out.println("Attempting kill using " + command);
          stdoutThread.addOutputString("Attempting kill using " + command
              + "\n");

          JDJobName.system(command);
          recovered = true;
        }
      }

      if (onLinux) {
        System.out.println("on linux");
        if ((hangMessage.indexOf("sun.awt.X11GraphicsEnvironment") > 0)
            || (hangMessage.indexOf("connection to \":9.0\"") > 0)) {
          /* Something went wrong with the VNC server */
          /* kill it */
          String commands[] = new String[4];
          commands[0] = "/bin/bash";
          commands[1] = "--verbose";
          commands[2] = "-c";
          commands[3] = "/bin/ps -ef | grep Xvnc | grep -v grep | sed 's%^\\([^ ][^ ]*\\)  *\\([^ ][^ ]*\\) .*%kill -9 \\2%' | sh ";
          System.out.println("Attempting kill using " + commands[3]);
          Process shellProcess = runtime.exec(commands);
          StringBuffer outputBuffer = new StringBuffer();
          JDJSTPOutputThread cmdStdoutThread = new JDJSTPOutputThread(
              shellProcess.getInputStream(), outputBuffer, null,
              JDJSTPOutputThread.ENCODING_ASCII);
          JDJSTPOutputThread stderrThread = new JDJSTPOutputThread(
              shellProcess.getErrorStream(), outputBuffer, null,
              JDJSTPOutputThread.ENCODING_ASCII);
          cmdStdoutThread.start();
          stderrThread.start();
          cmdStdoutThread.join();
          stderrThread.join();

          shellProcess.waitFor();
          System.out.println("Process complete exitValue = "
              + shellProcess.exitValue());
          System.out.println(outputBuffer.toString());

          recovered = true;
        }

      }
      if (!recovered) {
        // Try to dump the job log

        String jobname = stdoutThread.getParsedJobName();
        outString = "Job name was " + jobname + "\n";
        stdoutThread.addOutputString(outString);
        System.out.println(outString);
        if (jobname != null) {
          if (on400) {
            String command = "DSPJOBLOG JOB(" + jobname + ") OUTPUT(*PRINT)";
            outString = "Dumping job log to *PRINT using " + command + "\n";
            stdoutThread.addOutputString(outString);
            System.out.println(outString);
            JDJobName.system(command);
          }
        }
        System.out
            .println("ERROR:  JDRunit: Generating report and sleeping for 60 seconds");
        if (on400) {
          Date d = new Date();
          JDJobName
              .sendProgramMessage("LD "+Thread.currentThread().getName()+" " 
                  + d.toString()
                  + "ERROR:  JDRunit: Generating report and sleeping for 60 seconds");
        }

        /* Wait for 1 minutes to allow dump output to occur */
        fatalError = true;
        try {
          long waitDumpStartTime = System.currentTimeMillis();
          String[] reportArgs = new String[1];
          reportArgs[0] = initials;
          JDReport.main(reportArgs);
          long waitDumpEndTime = System.currentTimeMillis();
          while ((waitDumpEndTime - waitDumpStartTime) < 60000) {
            Thread.sleep(1000);
            waitDumpEndTime = System.currentTimeMillis();
          }
          forceReport = true;
        } catch (Exception sleepEx) {
          /* ignore */
        }

        // If the hang messages include JVMDUMP -- wait for processing to be
        // complete (another 5 minutes).

        int MAXRETRIES = 15;
        if (vectorContainsString(hangMessagesFound, "JVM requested System dump")) {
          int retryCount = 1;
          boolean shellTestProcessAlive = true;
          while (shellTestProcessAlive
              && retryCount <= MAXRETRIES
              && (!vectorContainsString(hangMessagesFound, "Snap dump written"))) {
            System.out
                .println("JVM requested System dump, waiting for dump to complete.  RetryCount="
                    + retryCount
                    + "/"
                    + MAXRETRIES
                    + " Sleeping for 60 seconds ");
            if (on400) {
              Date d = new Date();
              JDJobName
                  .sendProgramMessage("LD "+Thread.currentThread().getName()+" " 
                      + d.toString()
                      + "JVM requested System dump, waiting for dump to complete.  RetryCount="
                      + retryCount + "/" + MAXRETRIES
                      + " Sleeping for 60 seconds ");
            }

            retryCount++;
            try {
              Thread.sleep(60000);
            } catch (Exception sleepEx) {
              /* ignore */
            }
            try {
              shellTestProcess.exitValue();
              shellTestProcessAlive = false;
            } catch (IllegalThreadStateException itse) {
              // Process is still alive.
            }
          }
        }

        //
        // Send an e-mail about the hung message
        //
        try {
          String toAddress = iniProperties.getProperty("EMAIL");
          String fromAddress = iniProperties.getProperty("EMAIL");
          String subject = " Severe Error Running  " + initials + " "
              + testArgs;

          String hostname = InetAddress.getLocalHost().getCanonicalHostName();
          hostname = hostname.toUpperCase();
          int rchlandIndex = hostname.indexOf(".RCHLAND");
          if (rchlandIndex > 0) {
            hostname = hostname.substring(0, rchlandIndex);
          }

          if (hostname.indexOf('.') < 0) {
            hostname = hostname + "." + getDomain(hostname);
          }

          String URLBASE = "http://" + hostname + ":6050/";
          String htmlFile = JDReport.getReportName(initials);

          if (htmlFile.indexOf("ct/") == 0)
            htmlFile = htmlFile.substring(3);
          body.append("\n\nSee details at " + URLBASE + htmlFile + "\n");
          body.append("Test output " + URLBASE + "/out/" + initials + "/runit."
              + dateStringNoSpace + "\n");

          sendEMail(toAddress, fromAddress, subject, body);
        } catch (Exception e) {
          System.out.println("Error sending mail");
          e.printStackTrace();
        }
      }
    } /* hangMessages found */

    if (!processCompleted) {
      stdoutThread.addOutputString("Process did not complete successfully\n");

      if (hangMessagesFound.size() == 0) {
        String outString = "ERROR:  JDRunit Process timed out destroying at "
            + (new Timestamp(System.currentTimeMillis())) + "\n";
        // Be sure to process as fatal error
        fatalError = true;
        stdoutThread.addOutputString(outString);
        System.out.print(outString);
        if (on400) {
          Date d = new Date();
          JDJobName.sendProgramMessage("LD "+Thread.currentThread().getName()+" "  + d.toString() + " " + outString);
        }

        //
        // On windows, under CYWGIN, the process that is killed by p.destroy()
        // is the
        // shell process. This does not successfully kill the child Java
        // process, so
        // JDRunit will hang after all tests complete.
        // To remedy this, we get the PID of the shell and find the child job so
        // that it can
        // can be destroyed also.
        //

        String pid1 = stdoutThread.getParsedPid();
        if (pid1 != null) {
          outString = "PID of shell is " + pid1 + "\n";
          stdoutThread.addOutputString(outString);
          System.out.println(outString);

          // The logic should also work with Linux

          if ((osname.indexOf("Windows") >= 0)
              || (osname.indexOf("Linux") >= 0)) {

            // Start a process to run shell commands..
            // Note: We control the shell by passing in commands to the shell.
            if (cmdArray1.length > 1) {
              stdoutThread.addOutputString("Kill path:  starting shell with "
                  + cmdArray1[0] + " " + cmdArray1[1] + "\n");
              System.out.println("Kill path:  starting shell with "
                  + cmdArray1[0] + " " + cmdArray1[1]);
            } else {
              stdoutThread.addOutputString("Kill path:  starting shell with "
                  + cmdArray1[0] + "\n");
              System.out.println("Kill path:  starting shell with "
                  + cmdArray1[0]);
            }
            Process shellProcess = runtime.exec(cmdArray1);

            OutputStream shellProcessOutputStream = shellProcess
                .getOutputStream();
            outString = "ps -ef | grep ' "
                + pid1
                + " ' | grep java | sed 's/^ *\\([^ ][^ ]*\\)  *\\([^ ][^ ]*\\) .*/\\2/'\n";
            stdoutThread.addOutputString("executing: " + outString);
            System.out.println("executing: " + outString);
            shellProcessOutputStream.write(outString.getBytes());
            shellProcessOutputStream.flush();
            String shellOutputFile = runitOutputFile + ".shell";
            Vector shellHangMessagesFound = new Vector();
            JDJSTPOutputThread stdoutThread2 = JDJSTPTestcase
                .startProcessOutput(shellProcess, shellOutputFile, true,
                    hangMessages, hangMessagesException, shellHangMessagesFound, encoding);
            Thread.sleep(500);

            String lines = stdoutThread2.getOutput();
            stdoutThread.addOutputString("received: " + lines + "\n");
            System.out.println("received: " + lines);
            String line;
            lines = lines.trim();
            int lastNewlineIndex = lines.lastIndexOf('\n');
            if (lastNewlineIndex >= 0) {
              line = lines.substring(lastNewlineIndex + 1);
            } else {
              line = lines;
            }
            try {
              if (Integer.parseInt(line) > 1) {
                outString = "kill -9 " + line;
                stdoutThread.addOutputString("executing: " + outString);
                System.out.println("executing: " + outString);
                shellProcessOutputStream.write(outString.getBytes());
                Thread.sleep(500);
                lines = stdoutThread2.getOutput();
                stdoutThread.addOutputString("received: " + line + "\n");
                System.out.println("received: " + line + "\n");
              }
            } catch (NumberFormatException nfe) {
              outString = "Warning:  Unable to parse " + line + "\n";
              stdoutThread.addOutputString(outString);
              System.out.println(outString);
            }

            outString = "exit";
            stdoutThread.addOutputString("executing: " + outString + "\n");
            System.out.println("executing: " + outString);
            shellProcessOutputStream.write(outString.getBytes());

            Thread.sleep(500);

            try {
              System.out.println("Calling shellProcess.exitValue()");
              shellProcess.exitValue();
            } catch (IllegalThreadStateException itse) {
              stdoutThread.addOutputString("Calling shellProcess.destroy\n");
              System.out.println("Calling shellProcess.destroy");
              shellProcess.destroy();
            }
          }
        }

        stdoutThread.addOutputString("Calling shellTestProcess.destroy\n");
        System.out.println("Calling shellTestProcess.destroy");

        shellTestProcess.destroy();

        if (on400) {
          String info = "Parsed job name: " + stdoutThread.getParsedJobName();
          stdoutThread.addOutputString(info + "\n");
          System.out.println(info);

          // If the job is active, go ahead and kill it!!!!
          String killCommand = "call QSYS.QCMDEXC('ENDJOB JOB("
              + info
              + ") OPTION(*IMMED)                                                                   ' , 0000000060.00000)";

          try {
            System.out.println("Attempting to call " + killCommand);
            Connection conn = DriverManager.getConnection("jdbc:db2:localhost",
                USERID, PASSWORD);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(killCommand);
            conn.close();
          } catch (Exception e) {
            System.out.println("Exception trying to kill job " + info);
            e.printStackTrace(System.out);
          }

        }

      } else {

        String outString = "ERROR:  JDRunit Not killing hung process\n";
        stdoutThread.addOutputString(outString);
        System.out.println(outString);

      }
      // Force the output thread to end and attempt to join with it
      stdoutThread.stopReading();
      stdoutThread.join(1000);

    } else {
      shellTestProcess.waitFor();
      // Join with the output thread to make sure all the data makes it out
      stdoutThread.join();
    }

    // Make sure the writer is closed for the output thread
    // Otherwise the data may not get read below.
    // if (runitOutputFile != null) { // file cannot be null at this time
    if (runitOutputFile.equals("stdout")) {
    } else {
      stdoutThread.closeWriter();
    }
    // }

    /* Process the output of the test */
    // Need to write resultVector to runit.out

    File directory = new File("ct" + sep + "out" + sep + initials);
    if (!directory.exists()) {
      directory.mkdirs();
    }

    PrintWriter resultsWriter = new PrintWriter(new FileWriter(results, true));

    String disabledProfile = null;

    System.out.println("Writing results to " + savefileName);

    File savefile = new File(savefileName);

    writer = new PrintWriter(new FileWriter(savefile));

    enumeration = inputVector.elements();
    while (enumeration.hasMoreElements()) {
      String line = (String) enumeration.nextElement();

      writer.println(line);
    }

    /* Read the important results into memory */
    /*
     * Build the results as the test is running so less post processing needs to
     * be done
     */
    Vector testcasesVector = new Vector();
    Vector failedVector = new Vector();
    Vector rxpVector = new Vector();
    Hashtable rerunHashtable = new Hashtable();
    int resultsWritten = 0;
    boolean noOutput = true;
    int successfulCount = 0;
    BufferedReader outputReader = new BufferedReader(new FileReader(
        runitOutputFile));
    // System.out.println("JDEBUG: reading output "+output);
    String line = outputReader.readLine();
    while (line != null) {
      line = cleanupLine(line);
      writer.println(line);
      for (int i = 0; i < hangMessages.length; i++) {
        if (line.indexOf(hangMessages[i]) > 0) {
          fatalError = true;
          fatalErrorString = line;
          fatalErrorMessage = hangMessages[i];
        }
      }

      int disableIndex = line.indexOf("User ID is disabled.:");
      if (disableIndex >= 0) {
        disabledProfile = line.substring(disableIndex + 21);
      }

      if (line.indexOf("________________________________") > 0) {
        noOutput = false;
      }
      if (line
          .matches("^[^ ][^ ]*  *[\\-0-9][0-9]*  *[\\-0-9][0-9]*  *[\\-0-9][0-9]*  *[\\-0-9][0-9]*  *[0-9][0-9]*\\.[0-9][0-9]* *$")) {
        if (debug)
          System.out.println("JDRunit: MATCHED LINE: " + dateString + " "
              + AS400lc + " " + line);

        String testcase = line.trim();
        int spaceIndex = line.indexOf(' ');
        testcase = testcase.substring(0, spaceIndex);
        testcasesVector.addElement(testcase);

        resultsWriter.println(dateString + " " + AS400lc + " " + line);
        noOutput = false;
        resultsWritten++;
      }

      if (line.matches("^[^ ][^ ]* *[0-9][0-9]*  *FAILED.*")) {

        int failedIndex = line.indexOf("FAILED");
        failedVector.addElement(line.substring(0, failedIndex).trim());

        int spaceIndex = line.indexOf(' ');
        String testcase = line.substring(0, spaceIndex).trim();
        String variation = line.substring(spaceIndex + 1, failedIndex).trim();

        Vector variationsVector = (Vector) rerunHashtable.get(testcase);
        if (variationsVector == null) {
          variationsVector = new Vector();
          rerunHashtable.put(testcase, variationsVector);
        }
        variationsVector.addElement(variation);
        noOutput = false;
      }

      if (line.matches("^[^ ][^ ]* *[0-9][0-9]*  *SUCCESSFUL.*")) {
        successfulCount++;
      }

      if (line.matches("^[^ ][^ ]* *[0-9][0-9]*  *NOT ATTEMPTED.*")) {

        int failedIndex = line.indexOf("NOT ATTEMPTED");
        failedVector.addElement(line.substring(0, failedIndex).trim());

        int spaceIndex = line.indexOf(' ');
        String testcase = line.substring(0, spaceIndex).trim();
        String variation = line.substring(spaceIndex + 1, failedIndex).trim();

        Vector variationsVector = (Vector) rerunHashtable.get(testcase);
        if (variationsVector == null) {
          variationsVector = new Vector();
          rerunHashtable.put(testcase, variationsVector);
        }
        variationsVector.addElement(variation);
        noOutput = false;
      }

      if (line.matches(".*java.io.FileNotFoundException: .*.rxp")) {
        String formattedLine = line.replaceAll(
            "^.*NotFoundException: *\\(.*\\.rxp\\).*$", "\\1");
        rxpVector.addElement(formattedLine);
        noOutput = false;
      }

      if (line.matches(".*java.lang.Exception: Files.*are different.*")) {
        String formattedLine = line.replaceAll(
            "^.*Files.*'\\([^']*/exp[^']*\\)' .*$", "\\1");
        rxpVector.addElement(formattedLine);
      }

      line = outputReader.readLine();
    }
    outputReader.close();

    if (disabledProfile != null) {
      try {
        if (disabledProfile.equals(USERID)) {
          System.out.println("JDRunit: Reset profile " + USERID);
          resetId("jdbc:db2:localhost", MASTERUSERID, MASTERPASSWORD, USERID,
              PASSWORD);
        } else if (disabledProfile.equals(testUserid)) {
          System.out.println("JDRunit: Reset profile " + testUserid);
          resetId("jdbc:db2:localhost", MASTERUSERID, MASTERPASSWORD,
              testUserid, testPassword);

        } else {
          System.out.println("JDRunit: UNABLE TO RESET PROFILE "
              + disabledProfile);
        }
      } catch (Exception e) {
        System.out.println("JDRunit: Re-enabling profile failed");
        e.printStackTrace(System.out);
      }
    }
    if (resultsWritten == 0) {
      System.out.println("JDRunit: ERROR:  No results were written");
      /*
       * Did at least one variation run.. If so, still record a problem, even
       * though no results were written
       */
      if (fatalError || noOutput || (successfulCount > 0)
          || (failedVector.size() > 0)) {
        failedCount++; 
        System.out.println("JDRunit: ERROR:  fatal error occurred");
        System.out.println("JDRunit: ERROR: fatal error String="
            + fatalErrorString);
        System.out.println("JDRunit: ERROR: fatal error Message="
            + fatalErrorMessage);
        Vector badTestVector = new Vector();
        String testBase1 = testBaseProperties.getProperty(test);
        if (!testBase1.equals(test)) {
          badTestVector.add(test);
        } else {
          System.out.println("JDRunit:  Did not add test " + test
              + " because testbase is " + testBase1);

          // Find all the tests that go with this testBase
          Enumeration keys = testBaseProperties.keys();
          while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (!key.equals(test)) {
              String value = testBaseProperties.getProperty(key);
              if (value.equals(test)) {
                badTestVector.add(key);
              }
            }
          }

        }

        //
        // Add bad results records
        //

        Hashtable todoTests = readTodo();
        enumeration = badTestVector.elements();
        while (enumeration.hasMoreElements()) {
          String testcase = (String) enumeration.nextElement();
          if (todoTests.get(testcase) == null) {
            resultsWriter.println(dateString + " " + AS400lc + " " + testcase
                + " 0 1 0 1 66.666");
            failedCount++; 
          }
        }

      }
    }

    //
    // List the failed Variations
    //
    System.out.println("JDRunit: Failed vars are ");
    System.out.println("------------------");
    failedCount += failedVector.size();
    enumeration = failedVector.elements();
    while (enumeration.hasMoreElements()) {
      String failedTest = (String) enumeration.nextElement();
      System.out.println(failedTest);
    }
    System.out.println("------------------");

    //
    // Create the rerun commands
    //
    dualPrintln(System.out, writer, "---------------------------------------");
    dualPrintln(System.out, writer, "--  RERUN COMMANDS    -----------------");
    dualPrintln(System.out, writer, "---------------------------------------");

    enumeration = rerunHashtable.keys();
    int commandCount = 0;
    while (enumeration.hasMoreElements()) {
      String testcase = (String) enumeration.nextElement();
      String rerunCommand = "java test.JDRunit " + initials + " " + testcase
          + " ";
      Vector variationsVector = (Vector) rerunHashtable.get(testcase);
      Enumeration varEnum = variationsVector.elements();
      boolean first = true;
      while (varEnum.hasMoreElements()) {
        String variation = (String) varEnum.nextElement();
        if (first) {
          rerunCommand += variation.trim();
          first = false;
        } else {
          rerunCommand += "," + variation.trim();
        }
      }
      dualPrintln(System.out, writer, rerunCommand);
      commandCount++;
    }

    if (commandCount == 0) {
      // Just print out what we ran
      String rerunCommand = "java test.JDRunit " + initials + " " + test;
      dualPrintln(System.out, writer, rerunCommand);
    }

    dualPrintln(System.out, writer, "---------------------------------------");

    writer.close();
    resultsWriter.close();

    //
    // #
    // # Dump output files that failed
    // #
    System.out.println("JDRunit: Dumping the output files that failed");
    enumeration = rxpVector.elements();
    while (enumeration.hasMoreElements()) {
      String rxpFile = (String) enumeration.nextElement();
      System.out.println("Processing file " + rxpFile);
    }

    System.out.println("JDRunit: Cleaning up output files");

    //
    // Cleanup output files
    //
    File deleteFile = new File(runitInputFile);
    if (deleteFile.exists())
      deleteFile.delete();
    deleteFile = new File(runitOutputFile);
    if (deleteFile.exists())
      deleteFile.delete();

    String doReport = System.getenv("REPORT");
    if (doReport !=  null) {
	System.out.println("Running report since REPORT envvar set");
	forceReport=true; 
    }

    if (forceReport) {
      String[] reportArgs = new String[1];
      reportArgs[0] = initials;
      JDReport.main(reportArgs);
    }


    return failedCount; 
  } 

  public static long runNumber = 0L;
  public static Object runNumberSync = new Object();

  public static long nextRunNumber() {
    synchronized (runNumberSync) {
      runNumber++;
      return runNumber;
    }
  }

  public static Hashtable readTodo() {
    Hashtable returnHashtable = new Hashtable();

    try {
      FileInputStream fileInputStream = new FileInputStream("ini/TODO.ini");
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          fileInputStream));
      String line = reader.readLine();
      while (line != null) {
        line = line.trim();
        if (line.indexOf("#") == 0) {
          // skip comment
        } else {
          returnHashtable.put(line, line);
        }
        line = reader.readLine();
      }
      fileInputStream.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return returnHashtable;
  }

  public static boolean vectorContainsString(Vector hangMessagesFound,
      String string) {
    synchronized (hangMessagesFound) {
      Enumeration enumeration = hangMessagesFound.elements();
      while (enumeration.hasMoreElements()) {
        String line = (String) enumeration.nextElement();
        if (line.indexOf(string) >= 0) {
          return true;
        }
      }

    }
    return false;
  }

  public static boolean hasBadCharacters(char[] chars) {
    int l = chars.length;
    for (int i = 0; i < l; i++) {
      // if (chars[i] < 0) return true; -- not needed chars[i] is never negative
      if (chars[i] > 0x7f)
        return true;
    }
    return false;
  }

  volatile static StringBuffer cleanupSb = null;
  private static String jdSchedulerId = null;
  private static int jdPriority = -1;

  // Cleanup any odd characters in the line
  public static String cleanupLine(String line) {
    char[] chars = line.toCharArray();
    if (hasBadCharacters(chars)) {
      if (cleanupSb == null) {
        cleanupSb = new StringBuffer();
      }
      cleanupSb.setLength(0);
      StringBuffer sb = cleanupSb;
      int l = chars.length;
      boolean lastBad = false;
      int badCount = 0;
      for (int i = 0; i < l; i++) {

        if ((chars[i] >= 0x20 && chars[i] < 0x7f) || (chars[i] == 0x09)
            || (chars[i] == 0x0a)) {
          if (lastBad) {
            sb.append(">");
            lastBad = false;
          }
          sb.append(chars[i]);
        } else {
          if (!lastBad) {
            sb.append("<");
          }
          sb.append("x");
          badCount++;
          sb.append(Integer.toHexString((int) chars[i] & 0xFFFF));
          lastBad = true;
        }
      }

      // Print a Warning
      if (badCount > 20) {
        System.out.println("EBCDIC Warning: " + sb.toString());
        System.out.println("EBCDIC =======: " + ebcdicString(line));
      }

      return sb.toString();
    } else {
      return line;
    }
  }

  static String ebcdicString(String line) {
    char[] chars = line.toCharArray();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] >= 0x40 && chars[i] < 0xff) {
        sb.append(JDJSTPTestcase.EtoChar[(int) chars[i]]);
      } else {
        sb.append('.');
      }
    }
    return sb.toString();
  }

  static void startLoopbackProxy(String jt400jar) {
    // Check to see if port 3470 is already up
    try {
      Socket socket1 = new Socket("localhost", 3470);

      // If this worked then just close the socket
      socket1.close();
      System.out.println("port 3470 is already up"); 
    } catch (Exception e) {
      String exInfo = e.toString();
      if (exInfo.indexOf("remote host refused") >= 0) {
        // If not up, start the proxy in the background
        startProxyServer(jt400jar);

      } else {
        e.printStackTrace();
      }
    }

  }

  /* Wait for the proxy to become available */
  static void waitForProxy() throws Exception {
    boolean up = false;
    long endTime = System.currentTimeMillis() + 60000;
    while (!up && System.currentTimeMillis() < endTime) {
      try {
        Socket socket1 = new Socket("localhost", 3470);
        // If this worked then just close the socket
        socket1.close();
        up = true;
      } catch (Exception e) {
        String exInfo = e.toString();
        if (exInfo.indexOf("remote host refused") >= 0) {
          Thread.sleep(250);

        } else {
          System.out.println("Warning: unexpected exception " + e);
          e.printStackTrace();
        }
      }

    }
    if (!up) {
      throw new Exception("proxy on port 3470 did not start withing 60 seconds"); 
    }

  }

  static void startProxyServer(String jt400jar) {
    Runtime runtime1 = Runtime.getRuntime();
    try {
       
      
	String command = "java -classpath "+testcaseCode+":"+jt400jar+" com.ibm.as400.access.ProxyServer"; 
	System.out.println("JDRunit: starting ProxyServer on default port using: "+command); 
      // Need to include /home/jdbctest because the source for the serialized
      // lobs is located there.
      runtime1
          .exec(command);
      waitForProxy();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /* Set the scheduler id. Null means not to use the scheduler */
  public static void setJDSchedulerId(String id) {
    jdSchedulerId = id;
  }

  /* Set the priority for the schedule. -1 means to use */
  /* the default priorities */
  public static void setJDPriority(int newPriority) {
    jdPriority = newPriority;
  }

  public static void resetId(String jdbcUrl, String adminUserid,
      String adminPassword, String testUserid, String testPassword) {

    try {
      Connection con = DriverManager.getConnection(jdbcUrl, adminUserid,
          adminPassword);
      Statement stmt = con.createStatement();

      /* Check the profile. If the password has expired, */
      /* reset it by temporarily chaning to something else */
      /* while locking the profile */

      stmt.executeUpdate("CALL QSYS2.QCMDEXC(' DSPUSRPRF USRPRF("+testUserid+") OUTPUT(*OUTFILE) OUTFILE(QTEMP/DSPUSRPRF)  ')");

      ResultSet rs = stmt
	.executeQuery("select UPSTAT, UPPWEX, " +
             "TIMESTAMP('20'|| SUBSTRING(UPEXPD,1,2) || '-' || SUBSTRING(UPEXPD,3,2) || '-' || SUBSTRING(UPEXPD,5,2)),"+
              "CURRENT TIMESTAMP from QTEMP.DSPUSRPRF");
      rs.next();
      String status = rs.getString(1).trim();
      String expired = rs.getString(2).trim();
      Timestamp expirationDate = rs.getTimestamp(3); 
     Timestamp currentTimestamp = rs.getTimestamp(4); 
      boolean expiredPassword  = (!"*NO".equals(expired)) || currentTimestamp.after(expirationDate); 
      System.out.println("In resetPassword:  For profile "+testUserid+" status="+status+" expired="+expired+" exipirationDate="+expirationDate ); 
      /* Only change the password if expired */
      if (expiredPassword  ) {
        try {
          System.out.println("Setting password to garbage so it can be reset"); 
          stmt.executeUpdate("CALL QSYS2.QCMDEXC('CHGUSRPRF USRPRF("
              + testUserid + ") PASSWORD(GARBAGE) STATUS(*ENABLED) ')");
        } catch (Exception e) {
          // Ignore first
        }
      }
      /* It should always be safe to blindly enable the profile */
      stmt.executeUpdate("CALL QSYS2.QCMDEXC('CHGUSRPRF USRPRF(" + testUserid
          + ") PASSWORD(" + testPassword + ") STATUS(*ENABLED) ')");

      con.close();
    } catch (Exception e) {
      System.out.println("Reset failed adminUserid=" + adminUserid);
      e.printStackTrace(System.out);
      if (on400) {
        JDJobName.sendProgramMessage("Reset of profile " + testUserid
            + " failed with " + e.toString());
      }

    }
  }
}
