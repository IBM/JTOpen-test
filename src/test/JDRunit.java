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
// Class to run a testcase and record the results.
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
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
import java.util.concurrent.TimeUnit;


import test.JD.DataSource.JDDatabaseOverride;
import test.JTA.JTACleanupTx;

import java.sql.*;

@SuppressWarnings("deprecation")
public class JDRunit {

  public static final String TMP = "/tmp";
  public static String testcaseCode = JTOpenTestEnvironment.testcaseHomeDirectory;

  static Hashtable<String, String> rdbToCreatedUserid = new Hashtable<String, String>();
  static Hashtable<String, String> rdbToCreatedPassword = new Hashtable<String, String>();
  static boolean debug = false;
  static boolean passwordDebug = false; 
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
      "com.ibm.jtopenlite.", "jta.", "test.", "debug",
      "interactive", "os400.stdio", "com.ibm.cacheLocalHost", "java.net.",
      "com.sun.management.", "https.",  };
  /* Removed jdk. as this cause problems by passing on unsupported JDK options from */
  /* the source JVM to the target JVM */ 

  static String[] convertToXProperties = { "runjdwp:", /* "debug", */  "rs",
      "healthcenter", };

  static String[] hangMessages = {
      "Type=Segmentation error",
      "java.lang.Unknown",
      "java.lang.OutOfMemoryError",
      "FATAL ERROR",
      "JVMDUMP",
      "Exception in thread \"main\" java.lang.NoClassDefFoundError:",
      "IncompatibleClassChangeError",
      "connection to \":9.0\" refused by server", 
      TestDriver.RUN_COMPLETED};

  static String[] hangMessagesException = { "User requested", "JVMDUMP034I","/tmp/passwordLeakCoreDump"};

  static long globalTimeout = 3600000;

  static boolean on400 = false;
  static boolean on400open = false;
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

    property = System.getProperty("passwordDebug");
    if (property != null) {
      passwordDebug = true;
      System.out.println("JDRunit: passwordDebug is on");
    }

    if (debug) {
      System.out.println("JDRunit:  file.encoding is "
          + System.getProperty("file.encoding"));
    }
 
    on400 = JTOpenTestEnvironment.isOS400; 
    on400open = JTOpenTestEnvironment.isOS400open; 
    onLinux = JTOpenTestEnvironment.isLinux;
    
    property = System.getProperty("useTestJar"); 
    if (property != null && (property.toUpperCase().indexOf('N')< 0)) {
      testcaseCode = "JTOpen-test.jar"; 
    }
  }

  //
  // Prevent same date from being used
  //
  static Object getTestDateLock = new Object();
  static long nextTime = 0;
  private static JDDataAreaLock dataAreaLock_;
  private static Connection connection_;

  private static Date getTestDate() {
    Statement s = null;
    long thisTime = 0;
    synchronized (getTestDateLock) {
      try {
        if (on400) {
          try {
            String jobname = JDJobName.getJobName();
            connection_ = DriverManager.getConnection("jdbc:db2:localhost");
            JTOpenTestEnvironment.checkSystem(connection_);
            s = connection_.createStatement();
            dataAreaLock_ = new JDDataAreaLock(s, "JDRUNITLCK");
            Thread currentThread = Thread.currentThread();
            currentThread.getName();
            dataAreaLock_.lock("        JDRunit picking date job=" + jobname + " thread=" + currentThread, 3600);

            Thread.sleep(1000);

          } catch (Exception e) {
            e.printStackTrace();
            if (connection_ != null) {
              try {
                connection_.close();
              } catch (SQLException e1) {
                e1.printStackTrace();
              }
              connection_ = null;
            }
          }
        }
        thisTime = System.currentTimeMillis();
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
      } finally {
        if (s != null) {
          try {
            Timestamp ts = new Timestamp(thisTime);
            dataAreaLock_.unlock("    JDRunit end picking date", ts);
            s.close();
            connection_.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }

      }
    }
    return new Date(thisTime);

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
                if (pid == 0) {
                  initialize(); 
                }
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
    if (on400 && !on400open) {
      Date d = new Date();
      JDJobName.sendProgramMessage("LD " +Thread.currentThread().getName()+" " + d.toString()
          + " Running REGRESSION " + initials);
    }

    // Check for host specific regression list. This list is used for
    // Smaller testing runs (i.e. EUT / PITT )
    String localHost = JDHostName.getHostName().toLowerCase();

    String regressionFilename = "ini/regression" + initials + "." + localHost
        + ".ini";
    String hostRegressionFilename = regressionFilename;
    if (debug) {
      System.out.println("JDRunit:  looking for regressionFilename "
          + regressionFilename);
    }

    InputStream is = loadResourceIfExists(regressionFilename, iniInfo); 
    if (is == null) { 
      regressionFilename = "ini/regression" + initials + ".ini";
      is = loadResourceIfExists(regressionFilename, iniInfo);
      if (is == null) {
        String baseRegressionFilename = "ini/regressionBase"
            + initials.charAt(initials.length() - 1) + ".ini";
        regressionFilename = baseRegressionFilename;
        is = loadResourceIfExists(regressionFilename, iniInfo);
        if (is == null) { 
          throw new Exception("Error:  Regression file " + regressionFilename
              + " or " + hostRegressionFilename + " or "
              + baseRegressionFilename + " does not exist "+iniInfo);
        }
      }
    }
    System.out.println("Running regression instructions from "
        + regressionFilename);
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
    Hashtable<String, String> addedTestcaseHashtable = new Hashtable<String, String>();

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

  public static String getDomain(String systemName) throws Exception {
    String domain = JTOpenTestEnvironment.getDefaultClientDomain(); 

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
    Vector<String> regressedTestcases = new Vector<String>();
    Vector<String> notattTestcases = new Vector<String>();
    Vector<String> failedTestcases = new Vector<String>();

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

    Properties iniProperties = getIniProperties(initials, iniInfo);

    String AS400 = getAS400(iniProperties, initials);

    AS400 = iniProperties.getProperty("SYSTEM", AS400);
    if (AS400 == null) {
      AS400 = "AS400_NOT_SET";
    }

    // Send the e-mail -- spawning new e-mail program as needed.
    String subject;

    String localHost = JDHostName.getHostName().toUpperCase();
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

    String hostname = JDHostName.getHostName();
    hostname = hostname.toUpperCase();
    if (hostname.indexOf('.') < 0) {

      hostname = hostname + "." + getDomain(hostname);
    }
    String URLBASE = "http://" + hostname + ":6050/";
    int ctIndex = htmlFile.indexOf("ct/");
    if ( ctIndex > 0 )
      htmlFile = htmlFile.substring(ctIndex+3);

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
      Enumeration<String> enumeration = regressedTestcases.elements();
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
      Enumeration<String> enumeration = notattTestcases.elements();
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

    sendEMail(toAddress, fromAddress, subject, body, iniProperties.getProperty("mail.smtp.host"));
  }

  public static void sendEMail(String initials, String subject, StringBuffer body) throws Exception {

    Properties iniProperties = getIniProperties(initials, null);

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
    
    sendEMail(toAddress, fromAddress, subject, body,iniProperties.getProperty("mail.smtp.host"));

  }
  
  public static void sendEMail(String toAddress, String fromAddress,
      String subject, StringBuffer body, String mailSmtpHost) throws Exception {
          if (mailSmtpHost == null ) { 
                  System.out.println("Not sending e-mail.  mail.smtp.host not set"); 
                  return; 
          }
          
    java.util.Properties p = System.getProperties();
    p.put("mail.smtp.host", mailSmtpHost);

    /*
     * need mail.jar and activation.jar
     * 
     * import javax.mail.*;
     * 
     * import javax.mail.internet.*;
     * 
     * import javax.activation.*; \
     */
    Class<?> sessionClass = null;
    Class<?> mimeMessageClass = null;
    Class<?> internetAddressClass = null;
    Class<?> javaxMailMessageClass = null;
    Class<?> transportClass = null;
    Class<?> addressClass = null;
    Class<?> addressArrayClass = null;
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
          userDir + sep + "jars" + sep    + "activation.jar",
          "C:" + sep + "activation.jar",
          testcaseCode+"/jars/activation.jar",
          "/qibm/proddata/os400/java400/ext/activation.jar",
          "C:\\Documents and Settings\\Administrator\\workspace\\lib\\activation.jar", };

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
          userDir + sep + "jars" + sep + "mail.jar",
          "C:" + sep + "mail.jar", 
          testcaseCode+"/jars/mail.jar",
          "/qibm/proddata/os400/java400/ext/mail.jar",
          "C:\\Documents and Settings\\Administrator\\workspace\\lib\\mail.jar", };
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

      @SuppressWarnings("resource")
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

    Class<?>[] parameterTypes;
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
    Constructor<?> mimeMessageConstructor = mimeMessageClass
        .getConstructor(parameterTypes);
    parms[0] = session;
    Object message = mimeMessageConstructor.newInstance(parms);

    /* message.setFrom(new javax.mail.internet.InternetAddress(fromAddress)); */
    parameterTypes[0] = fromAddress.getClass();
    Constructor<?> internetAddressConstructor = internetAddressClass
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
    Class<?>[] innerClasses = javaxMailMessageClass.getDeclaredClasses();
    Class<?> innerClass = null;
    for (int i = 0; i < innerClasses.length && innerClass == null; i++) {
      if (innerClasses[i].getName().indexOf("RecipientType") > 0) {
        innerClass = innerClasses[i];
      }
    }
    if (innerClass == null)
      throw new Exception("Unable to find inner class");
    Field field = innerClass.getDeclaredField("TO");
    setRecipientsArgs[0] = field.get(null);

    Class<?>[] parseParameterTypes = new Class[2];
    parseParameterTypes[0] = toAddress.getClass();
    parseParameterTypes[1] = Boolean.TYPE;
    Method parseMethod = internetAddressClass.getMethod("parse",
        parseParameterTypes);

    Object[] parseArgs = new Object[2];
    parseArgs[0] = toAddress;
    parseArgs[1] = Boolean.valueOf(false);

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
      Vector<String> initialsVector = new Vector<String>();

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
        returnString[i] = initialsVector.elementAt(i);
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
    if (JTOpenTestEnvironment.isOS400) {
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
        if (on400 && !on400open) {
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
        if (on400 && !on400open) {
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
            if (pid == 0) initialize(); 
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
          if (on400 && !on400open) {
            JDJobName
                .sendProgramMessage("JDRunit:  Exit 2. Calling System.exit(3)");
          }
          System.exit(3);
        }
      }
      System.out.println("JDRunit:  DONE.. Calling System.exit(0)");
      if (on400 && !on400open) {
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
      if (on400 && !on400open) {
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
  String TEXT_PASSWORD; 
  char[] ENCRYPTED_PASSWORD;
  
  String MASTERUSERID;
  String TEXT_MASTERPASSWORD; 
  char[] ENCRYPTED_MASTERPASSWORD;
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

    readIni(iniInfo);
    // Build the test args.

    int commaIndex;

    //
    // Determine the base test.. This is read from testbase.ini
    //
    InputStream fileInputStream = loadResource("ini/testbase.ini" , iniInfo);
    testBaseProperties = new Properties();
    testBaseProperties.load(fileInputStream);
    fileInputStream.close();

    File testbase2File = new File("ini/testbase2.ini");
    if (testbase2File.exists()) { 
      fileInputStream = JDRunit.loadResource("ini/testbase2.ini" , iniInfo);
      testBaseProperties.load(fileInputStream);
      fileInputStream.close();
    }

    //
    // Get the list of testcases that will drop authority
    //
    
    fileInputStream = loadResource("ini/dropAuthority.ini", iniInfo);
    dropAuthorityProperties = new Properties();
    dropAuthorityProperties.load(fileInputStream);
    fileInputStream.close();


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
              + ".  Check ini/testbase.ini to make sure base test of variation exists "+iniInfo.toString()); 
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

  /* Load a resource.  The default is to load from the filesystem, then load using the classloader */ 
  /* If not found, then an empty input stream is returned */ 
  
  public static InputStream loadResource(String iniFile, StringBuffer iniInfo) throws FileNotFoundException {
    InputStream inputStream ;
    File file = new File(iniFile); 
    String userDir=System.getProperty("user.dir"); 
    
    if (file.exists()) { 
        inputStream=    new FileInputStream(iniFile);
        if (iniInfo != null) iniInfo.append("echo loaded "+iniFile+" from file system user.dir="+userDir+"\n"); 
    } else {
      String fullPathIniFile = JTOpenTestEnvironment.testcaseHomeDirectory+File.separator+iniFile; 
      file = new File(fullPathIniFile); 
      if (file.exists()) { 
        inputStream=    new FileInputStream(fullPathIniFile);
        if (iniInfo != null) iniInfo.append("echo loaded "+iniFile+" from file system user.dir="+userDir+"\n"); 
      } else {       
        inputStream = JDRunit.class.getClassLoader().getResourceAsStream(iniFile);
        if (inputStream != null) { 
                if (iniInfo!=null) iniInfo.append("echo loaded "+iniFile+" from classloader\n"); 
        } else {
          JTOpenTestEnvironment.loadSystemsIni();
                if (iniInfo!=null) iniInfo.append("echo unable to load "+iniFile+" from classloader\n"); 
                byte[] emptyBuffer = new byte[0];
                inputStream = new ByteArrayInputStream(emptyBuffer);
        }
      }
    }
        return inputStream;
}


  /* Load a resource.  The default is to load from the filesystem, then load using the classloader */ 
  /* If not found, then an error is returned */ 
  
  public static Reader loadReaderResource(String filename) throws FileNotFoundException {
    InputStream inputStream;
    File file = new File(filename);
    if (file.exists()) {
      inputStream = new FileInputStream(filename);
    } else {
      if (filename.indexOf('\\') >= 0) {
        filename = filename.replace('\\','/'); 
      }
      inputStream = JDRunit.class.getClassLoader().getResourceAsStream(filename);
      if (inputStream == null) {
        throw new FileNotFoundException(filename);
      }
    }
    return new InputStreamReader(inputStream);
  }

  /* Load a resource.  The default is to load from the filesystem, then load using the classloader */ 
  /* If not found, then null is returned */ 
  
  public static InputStream loadResourceIfExists(String iniFile, StringBuffer iniInfo) throws FileNotFoundException {
    InputStream inputStream ;
    File file = new File(iniFile); 
    if (file.exists()) { 
        inputStream=    new FileInputStream(iniFile);
        if (iniInfo != null) iniInfo.append("echo loaded "+iniFile+" from file system\n"); 
    } else {
        inputStream = JDRunit.class.getClassLoader().getResourceAsStream(iniFile);
        if (inputStream != null) { 
                if (iniInfo!=null) iniInfo.append("echo loaded "+iniFile+" from classloader\n"); 
        } else {
                if (iniInfo!=null) iniInfo.append("echo unable to load "+iniFile+" from classloader\n"); 
                inputStream = null;
        }
    }
    return inputStream;
}

/** 
   * Reads the default ini files from the ini directory.  The files are the following
   * <ul>
   * <li> netrc.ini -- userids and passwords</li> 
   * <li> notification.ini -- e-mail address to notify of status of tests</li>
   * </ul>
   * @return Properties read from the default ini files 
   * @throws Exception
   */
  public static Properties getIniProperties(StringBuffer iniInfo) throws Exception {
    Properties iniProperties = new Properties();

    if (iniInfo == null)
      iniInfo = new StringBuffer();

    {
      String filename = "ini/netrc.ini";
      InputStream fileInputStream = loadResource(filename, iniInfo);
      iniProperties.load(fileInputStream);
      fileInputStream.close();
    }

    {
      String filename = "ini/notification.ini";
      InputStream fileInputStream = loadResource(filename, iniInfo);
      iniProperties.load(fileInputStream);
      fileInputStream.close();
    }
    if (debug) { 
        System.out.println(iniInfo.toString()); 
    }
    return iniProperties;

  }

  /** 
   * Reads the properties files needed to configure JDRunit.
   * This reads the following config files from the ini directory, where
   * <RR><JJ><T> are the initialis of the test 
   * <ul>
   * <li> netrc.ini        -- userids and passwords</li> 
   * <li> notification.ini -- e-mail address to notify of status of tests</li>
   * <li> systems.ini  -- IBM i systems to test to </li> 
   * <li> runit<RR>xxx.ini -- release specific settings</li>
   * <li> runitxx<JJ>x.ini -- JVM specific settings </li>
   * <li> runitxxxx<T>.ini -- test type specific settings </li> 
   * 
   * <ul>  
   * </ul>
   * The generic settings can be overwritten by providing a runit file
   * of the following two formats.
   * <ul>
   * <li> runitRRJJT.hostname.ini  -- override for specific setting for the specified host
   * <li> runitRRJJT.ini           -- override for specific RRJJT setting  
   * </ul>
   * @param initials   Initials of the testcase to use.  The format is
   *                   rrjjt where rr is the release (i.e. 74,75), 
   *                   jj is the jvm (i.e. 8S meaning JDK 8 Sun JVM)
   *                   t  is the type of test (A meaning toolbox T meaning toolbox JDBC).  
   * @return
   * @throws Exception
   */
  public static Properties getIniProperties(String initials, StringBuffer iniInfo) throws Exception {

            if (iniInfo == null)
                iniInfo = new StringBuffer();

    Properties iniProperties = getIniProperties(iniInfo);


    String localHost = JDHostName.getHostName().toLowerCase();

    // Use host specific file if necessary
    String filename = "ini/runit" + initials + "." + localHost + ".ini";
    File existFile = new File(filename);
    if (!existFile.exists()) {
      filename = "ini/runit" + initials + ".ini";
    }

    existFile = new File(filename);
    if (existFile.exists()) {
      InputStream fileInputStream = loadResource(filename, iniInfo); 
      iniProperties.load(fileInputStream);
      fileInputStream.close();
    } else {
      // Load from the classpath the pieces of the initals
      filename = "ini/runit" + initials.substring(0, 2) + "xxx.ini";
      {
        InputStream fileInputStream = loadResource(filename,iniInfo);
        iniProperties.load(fileInputStream);
        fileInputStream.close();
      }

      filename = "ini/runitxx" + initials.substring(2, 4) + "x.ini";
      {
        InputStream fileInputStream = loadResource(filename,iniInfo);
        iniProperties.load(fileInputStream);
        fileInputStream.close();
      }

      filename = "ini/runitxxxx" + initials.substring(4, 5) + ".ini";
      {
          InputStream fileInputStream = loadResource(filename,iniInfo);
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
      InputStream fileInputStream = loadResource(filename,iniInfo);

      iniProperties.load(fileInputStream);
      fileInputStream.close();
    }

    String AS400 = iniProperties.getProperty("AS400");
    if (AS400 == null) {
      if (JTOpenTestEnvironment.isOS400) {
        AS400 = JDHostName.getHostName().toUpperCase();
        iniProperties.put("AS400", AS400);
      }
    }

    if (debug) {
        System.out.println("iniInfo contains: "+iniInfo.toString()); 
    } 
    return iniProperties;
  }

  public static ClassLoader getJt400JarClassLoader() {

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
      return loader; 
  }
  @SuppressWarnings("resource")
  public static String createProfile(String SYSTEM, String newUserid,
      String newPassword, String userid, char[] encryptedPassword, Vector<String> inputVector) {
    if (newPassword == null)
      newPassword = "abc212cb";
    String sql = "notSet";
    try {
      String url = "jdbc:as400:" + SYSTEM;

      ClassLoader loader = getJt400JarClassLoader();
      
      Class<?> c = loader.loadClass("com.ibm.as400.access.AS400JDBCDriver");
      if (debug) {
        System.out.println("Debug: Class loaded " + c);
      }
      Object driver =  c.newInstance();


      Connection con;
      char[] password = PasswordVault.decryptPassword(encryptedPassword); 
      try {
        con = (Connection) JDReflectionUtil.callMethod_O(driver, "connect", url, userid, password);
        
      } catch (Exception e) {
        String message = e.toString();
        if (message.indexOf("cannot establish") >= 0) {
          url = "jdbc:as400:" + SYSTEM + "."+JTOpenTestEnvironment.getDefaultServerDomain();
          con = (Connection) JDReflectionUtil.callMethod_O(driver, "connect", url, userid, password);

        } else {
          throw e;
        }
      } finally {
        PasswordVault.clearPassword(password);
      }

      Statement stmt = con.createStatement();
      // Disable password leak check
      try { 
          TestDriver.checkPasswordLeak = false;
      } catch (NoClassDefFoundError error) {
          // Just ignore error 
      }
      // Make sure to follow any password indirection
      if(passwordDebug) System.out.println("passwordDebug: Calling PasswordVault.getEncryptedPassword("+newPassword+")"); 
      char[] encryptedNewPassword = PasswordVault.getEncryptedPassword(newPassword);
      String rawPassword=PasswordVault.decryptPasswordLeak(encryptedNewPassword); 
      try {
        /* Note CCSID must be 65535 for DDDMSQLCompatibility to pass */
        sql="CALL QSYS.QCMDEXC('"
            + "QSYS/CRTUSRPRF USRPRF("
            + newUserid
            + ") PASSWORD("
            + rawPassword
            + ") TEXT(''Toolbox testing profile'') ACGCDE(514648897) CCSID(65535)                                                                 ',   0000000160.00000)";
      if (passwordDebug) System.out.println("passwordDebug: SQL="+sql); 
        stmt.executeUpdate(sql);
      } catch (Exception e) {
        // ignore an error from the create
      }
      sql = "CALL QSYS.QCMDEXC('"
          + "QSYS/CHGUSRPRF USRPRF("
          + newUserid
          + ") PASSWORD(BOGUS7)                                                                        ',   0000000060.00000)";
      if (passwordDebug) System.out.println("passwordDebug: SQL="+sql); 
      stmt.executeUpdate(sql);

      sql = "CALL QSYS.QCMDEXC('"
          + "QSYS/CHGUSRPRF USRPRF("
          + newUserid
          + ") PASSWORD("
          + rawPassword
          + ")  STATUS(*ENABLED) PWDEXPITV(90)                                                                                      ',   0000000100.00000)";
      if (passwordDebug) System.out.println("passwordDebug: SQL="+sql);
      stmt.executeUpdate(sql);

      con.close();
      rdbToCreatedUserid.put(SYSTEM, newUserid);
      rdbToCreatedPassword.put(SYSTEM, newPassword);

      
      System.out.println("Profile " + newUserid + " created");
      
      con.close();
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
        if (JTOpenTestEnvironment.isOS400) {
          try {
            AS400 = JDHostName.getHostName().toUpperCase();
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

  public void readIni(StringBuffer iniInfo) throws Exception {
    String filename = "ini/runit" + initials + ".ini";

    iniProperties = getIniProperties(initials, iniInfo);
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
      System.out.println("--------------Ini Info ---------------");
      System.out.println(iniInfo.toString()); 
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
    if (SYSTEM == null) { 
      throw new Exception("Unable to determine system.  Please check systems.ini"); 
    }
    SYSTEM = SYSTEM.toUpperCase();

    if (driver.equals("native")) {
        PrintWriter printWriter = new PrintWriter(System.out) ;
        SYSTEM = JDDatabaseOverride.getDatabaseNameFromSystemName(SYSTEM, printWriter); 
    } 



    String iasp = iniProperties.getProperty("IASP_" + SYSTEM);
    if (iasp != null) {
      iaspArgs = " -asp " + iasp + " ";
    } else {
      iaspArgs = " -asp IASP_NOT_DEFINED_IN_INI ";
    }

    if (on400 && !on400open) {
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
    // See if the password is overwritten by a JVM property
    
    TEXT_PASSWORD = getPropertyPassword(USERID); 
    if (TEXT_PASSWORD == null) TEXT_PASSWORD = iniProperties.getProperty("PASSWORD");
    ENCRYPTED_PASSWORD = PasswordVault.getEncryptedPassword(TEXT_PASSWORD); 
    
    MASTERUSERID = iniProperties.getProperty("MASTERUSERID");
    TEXT_MASTERPASSWORD = getPropertyPassword(MASTERUSERID); 
    if (TEXT_MASTERPASSWORD == null) TEXT_MASTERPASSWORD = iniProperties.getProperty("MASTERPASSWORD");
    ENCRYPTED_MASTERPASSWORD = PasswordVault.getEncryptedPassword(TEXT_MASTERPASSWORD); 
    
    CLIWHDSN = iniProperties.getProperty("CLIWHDSN", "MEMEMEM"); //
    SRDB = CLIWHDSN.toUpperCase();
    CLIWHUID = iniProperties.getProperty("CLIWHUID", "DB2TEST");
    CLIWHPWD = iniProperties.getProperty("CLIWHPWD", "PASS2DB");
    if (CLIWHDSN != null) { 
        javaArgs += "-DCLIWHDSN=" + CLIWHDSN + " -DCLIWHUID=" + CLIWHUID
        + " -DCLIWHPWD=" + CLIWHPWD + " ";
    }
    CLIWGDSN = iniProperties.getProperty("CLIWGDSN", "MEMEMEM");
    CLIWGUID = iniProperties.getProperty("CLIWGUID", "DB2TEST");
    CLIWGPWD = iniProperties.getProperty("CLIWGPWD", "PASS2DB");
    if (CLIWGDSN != null) { 
    javaArgs += "-DCLIWGDSN=" + CLIWGDSN + " -DCLIWGUID=" + CLIWGUID
        + " -DCLIWGPWD=" + CLIWGPWD + " ";
    }
    CLIWLDSN = iniProperties.getProperty("CLIWLDSN", "CLIDB");
    CLIWLUID = iniProperties.getProperty("CLIWLUID", "DB2TEST");
    CLIWLPWD = iniProperties.getProperty("CLIWLPWD", "PASS2DB");
    if (CLIWLDSN != null) { 
        javaArgs += "-DCLIWLDSN=" + CLIWLDSN + " -DCLIWLUID=" + CLIWLUID
        + " -DCLIWLPWD=" + CLIWLPWD + " ";
    }
    CLIWZDSN = iniProperties.getProperty("CLIWZDSN", "STLEC2");
    CLIWZUID = iniProperties.getProperty("CLIWZUID", "admf001");
    CLIWZPWD = iniProperties.getProperty("CLIWZPWD", "n1cetest");
    if (CLIWZDSN != null) {
    javaArgs += "-DCLIWZDSN=" + CLIWZDSN + " -DCLIWZUID=" + CLIWZUID
        + " -DCLIWZPWD=" + CLIWZPWD + " ";
    }
    
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
        toolboxJar = testcaseCode+"/jacoco/java6/jt400.jar";
      } else if (toolboxJar.indexOf("java8") > 0) {
        toolboxJar = testcaseCode+"/jacoco/java8/jt400.jar";
      } else if (toolboxJar.indexOf("java9") > 0) {
        toolboxJar = testcaseCode+"/jacoco/java9/jt400.jar";
      } else {
        toolboxJar = testcaseCode+"/jacoco/java0/jt400.jar";
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
    Enumeration<?> keys = systemProperties.keys();
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

  /* Read the property from the JVM or environment */ 
  static public String getPropertyPassword(String userid) {
          String password = null; 
          password = System.getProperty(userid+".password"); 
          if (password == null) { 
                  password = System.getenv(userid+".password"); 
          }
          return password; 
  }

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
  public JDRunitGoOutput  go() throws Exception {
    
    int failedCount = 0; 
    int successfulCount = 0; 
    boolean runNativeTestFromWindows  = false; 
    boolean forceReport = false;
    long thisRunNumber = nextRunNumber();
    if (pid == 0) initialize(); 
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
    String codeLevel = "ct/codeLevel" + initials + ".out";
    boolean fatalError = false;
    String fatalErrorString = "";
    String fatalErrorMessage = "";
    String display = ":8";

    Vector<String> inputVector = new Vector<String>();

    if (debug) {
      System.out.println("JDRunit: Building the script");
    }

    /* See if we are trying to run a native test from a PC */ 
    /* In this case, ssh is used to connect to the system and run the test */
    /* This assumes the test are updated in /home/jdbctest */ 
    
    String vmInitials = initials.substring(2,4);

    if (JTOpenTestEnvironment.isWindows && (vmInitials.endsWith("3") || vmInitials.endsWith("6"))) {
       runNativeTestFromWindows = true; 
       testcaseCode="/home/jdbctest"; 
    }


    PrintWriter writer = new PrintWriter(new FileWriter(runitInputFile));

    inputVector.addElement("echo Running " + initials + " " + test);
    inputVector.addElement("echo input is " + runitInputFile);
    inputVector.addElement("echo output is " + runitOutputFile);
    inputVector.addElement(iniInfo.toString());
    inputVector.addElement("echo cd "+testcaseCode);
    inputVector.addElement("cd "+testcaseCode);

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
        if (JTOpenTestEnvironment.isWindows && !runNativeTestFromWindows) { 
                inputVector.addElement("echo setting classpath using \"CLASSPATH='" + classpath+"'\"");
                inputVector.addElement("CLASSPATH='" + classpath+"'");
        } else {
                inputVector.addElement("echo setting classpath using CLASSPATH="+classpath);
                inputVector.addElement("CLASSPATH=" + classpath);
        }
    } else {
        if (JTOpenTestEnvironment.isWindows && !runNativeTestFromWindows) { 
                /* Just inherit the classpath.  */ 
                classpath = System.getProperty("java.class.path"); 
                inputVector.addElement("echo setting classpath using \"CLASSPATH='" + classpath+"'\"");
                inputVector.addElement("CLASSPATH='.;" + classpath+"'");
                
        } else { 
           inputVector.addElement("echo setting classpath using CLASSPATH=.");
           inputVector.addElement("CLASSPATH=.");
        }
    }

    inputVector.addElement("echo Setting JAVA_HOME to " + javaHome);
    inputVector.addElement("JAVA_HOME=\"" + javaHome + "\"");
    inputVector.addElement("export JAVA_HOME");
    if (javaHome.indexOf("/QOpenSys/pkgs/lib/jvm") >=0) {
        inputVector.addElement("PATH="+javaHome+"/bin:$PATH");
        inputVector.addElement("export PATH"); 
    }


    String newTestJar = iniProperties.getProperty("testJar");
    if (newTestJar != null) {
      testcaseCode = newTestJar; 
    }

    /* Use JTOpen-test.jar if exists in the classpath. */ 
    /* Assume that the JTOpen-test.jar is in the current directory */ 
    String currentClassPath=System.getProperty("java.class.path"); 
    if (currentClassPath.indexOf("JTOpen-test.jar") >= 0) { 
        testcaseCode = "JTOpen-test.jar"; 
    }
    

    String setClasspath = "CLASSPATH="+toolboxJar+":"+testcaseCode+":"
        + ":/qibm/proddata/java400/ext/translator.zip:"
        + toolsJar
        + ":jars/googleauth-1.5.0.jar:jars/commons-codec-1.16.0.jar"
        + ":jars/servlet.jar:jars/jcifs.jar:jars/fscontext.jar:jars/providerutil.jar";

    
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

    
    if (JTOpenTestEnvironment.isWindows && !runNativeTestFromWindows) {

      
      if (!runNativeTestFromWindows) { 
      /* On windows, we copy the toolbox jar so that the original can be updated */
      /*
       * On windows, the JVM puts a lock on the jar file which prevents it from
       * being updated.
       */
      /* To get around this,we make a copy in C:\\runningJars */
      /*
       * We will attempt to delete the extra copies each time this program is
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
        testcaseCode=System.getProperty("user.dir") +File.separator+testcaseCode; 
      }
      setClasspath = "CLASSPATH=\"" + toolboxJar + ";" +  testcaseCode+ ";"
          + currentClasspath+";"
          + toolsJar + ";" + 
          System.getProperty("user.dir") + ""+File.separator+"jars"+File.separator+"db2_classes.jar;" + 
          System.getProperty("user.dir") + ""+File.separator+"jars"+File.separator+"fscontext.jar;" + 
          System.getProperty("user.dir") + ""+File.separator+"jars"+File.separator+"providerutil.jar;" + 
          System.getProperty("user.dir") + ""+File.separator+"jars"+File.separator+"servlet.jar;" + 
          System.getProperty("user.dir") + ""+File.separator+"jars"+File.separator+"jcifs.jar;" + 
          System.getProperty("user.dir") + ""+File.separator+"jars"+File.separator+"googleauth-1.5.0.jar;"+
          System.getProperty("user.dir") + ""+File.separator+"jars"+File.separator+"commons-codec-1.16.0.jar;" +
          jccJars + ";" +

          /*
           * iniProperties.get("SQLLIB")+"\\java\\db2jcc4.jar;" +
           * iniProperties.get("SQLLIB")+"\\java\\db2jcc_license_cisuz.jar;"+
           * iniProperties.get("SQLLIB")+"\\java\\db2jcc_license_cu.jar;"+
           */

          jtopenliteJar + "\"";
      }
      /* NOT WINDOWS */
    } else if (iniProperties.getProperty("CLIENTOS", "AS400").equals("LINUX")) {

      toolsJar = iniProperties.getProperty("toolsJar");
      if (toolsJar == null) {
        toolsJar = javaHome + "/lib/tools.jar";
      }

      setClasspath = "CLASSPATH=\""+testcaseCode+":" + toolboxJar + ":"
          + toolsJar + ":"  
          + System.getProperty("user.dir") + "/jars/fscontext.jar:" 
          + System.getProperty("user.dir") + "/jars/providerutil.jar:" 
          + System.getProperty("user.dir") + "/jars/servlet.jar:" 
          + System.getProperty("user.dir") + "/jars/googleauth-1.5.0.jar:"
          + System.getProperty("user.dir") + "/jars/commons-codec-1.16.0.jar:"
          + System.getProperty("user.dir") + "/jars/jcifs.jar" + "\"";

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
        /* include native JDBC driver on classpath if Open JVM */ 
        if (javaHome.indexOf("/QOpenSys/pkgs/lib/jvm") >= 0) {
          if (javaHome.indexOf("openjdk-11") > 0) {
            /* classes11.jar is compiled for Java 17 on 7.6.  Use the 1.8 jar */ 
            setClasspath=setClasspath+"\":/QIBM/ProdData/OS400/Java400/ext/db2_classes18.jar\"";
          } else { 
            setClasspath=setClasspath+"\":/QIBM/ProdData/OS400/Java400/ext/db2_classes11.jar\"";
          }
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
        // Warning: SYSTEM.DOMAIN.COM:9 is taken because of
        // /tmp/.X11-unix/X9
        // Remove this file if there is no X server SYSTEM.DOMAIN.COM:9

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

    inputVector.addElement("echo '" + setClasspath+"'");
    inputVector.addElement(setClasspath);
    inputVector.addElement("export CLASSPATH");
    inputVector.addElement("date");
    inputVector.addElement("echo CLASSPATH is \"$CLASSPATH\"");
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
    if (runNativeTestFromWindows) {
      /* The java -version appears to eating the incoming command */ 
      /* Feed it something else instead */ 
      inputVector.addElement("echo 'hi' | java -version");
      inputVector.addElement(""); 
    } else { 
       inputVector.addElement("java -version");
    }
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
    String testPassword = TEXT_PASSWORD;
    char[] encryptedTestPassword = ENCRYPTED_PASSWORD; 
    String newUserid = (String) dropAuthorityProperties.get(testBase);
    if (newUserid == null) {
      newUserid = (String) dropAuthorityProperties.get(test);
    }
    if (newUserid != null) {
      String newPassword = "javaPassword.txt";
      int semicolonIndex = newUserid.indexOf(';');
      if (semicolonIndex > 0) {
        newPassword = newUserid.substring(semicolonIndex + 1);
        if (passwordDebug) System.out.println("passwordDebug: newPassword("+newPassword+") set from ; in "+newUserid); 
        newUserid = newUserid.substring(0, semicolonIndex);
      } else {
         /* Check if password is in ini files or set from environment */ 
        String configTestUserId = iniProperties.getProperty("TESTUSERID"); 
        if (newUserid.equalsIgnoreCase(configTestUserId)) {
          newPassword = getPropertyPassword(newUserid); 
          if (newPassword == null) {
              newPassword = iniProperties.getProperty("TESTPASSWORD");
              if (passwordDebug) System.out.println("passwordDebug: newPassword("+newPassword+") set from TESTPASSWORD"); 
          } else { 
              if (passwordDebug) System.out.println("passwordDebug: newPassword("+newPassword+") set from TESTUSERID="+newUserid);
          }
        }
      }
     
      
      /* Check to see if profile created */
      String createdUserid = rdbToCreatedUserid.get(SYSTEM);
      if (createdUserid == null || !createdUserid.equals(newUserid)) {

        /* create the profile for the test on the system */
        testUserid = newUserid;
        testPassword = createProfile(SYSTEM, newUserid, newPassword, USERID,
            ENCRYPTED_PASSWORD, inputVector);
        if (testPassword == null) {
          inputVector
              .addElement("Unable to create profile. setting password to new anyway");
          testPassword = newPassword;
        }
        encryptedTestPassword = PasswordVault.getEncryptedPassword(newPassword); 

      } else {
        /* Use what was already set up */
        testUserid = newUserid;
        testPassword = rdbToCreatedPassword.get(SYSTEM);
        encryptedTestPassword = PasswordVault.getEncryptedPassword(testPassword); 
      }

    }

    /* -directory is used by the ftp testcase and tell where to find */
    /* file on the AS/400. If the FTP testcase are first run on the */
    /* AS/400, then the file will be there when the Windows/linux */
    /* clients run these tests. */

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
    if (JTOpenTestEnvironment.isWindows && !runNativeTestFromWindows) {
      savefileName = savefileName.replace(':', '.');
    }

    String parentJob = "UNKNOWN";
    if (on400 && !on400open) {
      parentJob = JDJobName.getJobName();
    }

    // Specify the output file to the test
    javaArgs = " -Dtest.testcase=" + initials + "." + test
        + " -Dtest.outputFile=" + runitOutputFile + " -Dtest.saveFile="
        + savefileName + " -Dtest.parentJob=" + parentJob + " " + javaArgs;

    if (useJaCoCo) {
        File destDir = new File(testcaseCode+"/jacoco/"+initials);
        if (!destDir.exists()) {
            destDir.mkdir(); 
        }
        javaArgs = "-javaagent:"+testcaseCode+"/jacoco/lib/jacocoagent.jar=destfile="+testcaseCode+"/jacoco/"+initials+"/"+test+".exec,includes=com.ibm.as400.access.* " + javaArgs; 
    } 

    String javaCommand = "java " + javaArgs + " " + testArgs + " -lib "
        + defaultLibrary + " -system " + SYSTEM;
    if (RDB != null) {
      javaCommand += " -rdb " + RDB;
    }
    javaCommand += " -uid  " + testUserid + " -pwd " + testPassword
        + " -pwrSys " + USERID + "," + TEXT_PASSWORD + " -directory /  -misc "
        + driver + "," + release + iaspArgs;
    if (finalArgs != null && finalArgs.length() > 0) {
      javaCommand = javaCommand + " " + finalArgs;
    }
   
    if (finalArgs != null) {
      int proxy5Index = finalArgs.indexOf("-proxy5");
      if (proxy5Index >= 0) {
        String proxyInfo = finalArgs.substring(proxy5Index + 7).trim();
        startSock5Proxy(proxyInfo, USERID, SYSTEM, USERID, TEXT_PASSWORD);
      } else {
        int proxyIndex = finalArgs.indexOf("-proxy");
        if (proxyIndex >= 0) {
          String loopbackInfo = finalArgs.substring(proxyIndex + 6).trim();
          if (loopbackInfo.indexOf("loopback") == 0) {

            startLoopbackProxy(toolboxJar);
          } else {
            System.out.println("WARNING.  DID NOT HANDLE PROXY in " + finalArgs + " loopbackInfo=" + loopbackInfo);
          }
        }
      }

    }

    inputVector.addElement("echo Here is the Java command "); 
    inputVector.addElement("echo " + javaCommand);
    inputVector.addElement(""); 
    inputVector.addElement(javaCommand);
    inputVector.addElement(""); 
    inputVector.addElement("echo this is after the Java command"); 
    inputVector.addElement("\n\n\n\n");
    inputVector.addElement("echo DONE");
    inputVector.addElement("echo exit");
    inputVector.addElement("exit");

    Enumeration<String> enumeration = inputVector.elements();
    while (enumeration.hasMoreElements()) {
      String line = enumeration.nextElement();
      writer.println(line);
    }

    writer.flush();
    writer.close();
    JDJSTPRunFlag runFlag = new JDJSTPRunFlag();

    if (true) {   /* was debug */ 
      System.out.println("JDRunit: INPUT SCRIPT IS ");
      System.out.println("-------------------------------------------------");
      JDJSTPTestcase.cat(runitInputFile);
      System.out.println("-------------------------------------------------");
    }

    
    OutputStream stdin = null; 
    JDJSTPOutputThread stdoutThread = null ; 
    /* Assynchronously start the process output threads */
    Vector<String> hangMessagesFound = new Vector<String>();
    int encoding; 
    Process shellTestProcess = null; 
    String shellTestProcessInfo = "";
    String[] cmdArray1 = null;
    String shellArgs = iniProperties.getProperty("shellArgs");
    String shellArgs2 = null; 
    String shellArgs3 = null; 
    if (runNativeTestFromWindows) { 
      System.out.println("Running native test from Windows"); 
      shellBinary = "ssh";
      shellArgs = "-e";
      shellArgs2 = "none";
      shellArgs3 = USERID + "@"+AS400; 
      
      /* Make sure that the ssh server is active */ 
      try (Socket socket = new Socket()) {
        socket.connect(new InetSocketAddress(AS400,22), 500);
      } catch (IOException e) {
        // Connection failed, start the server
        char[] encryptedPassword = PasswordVault.getEncryptedPassword(TEXT_PASSWORD); 
        char[] clearPassword = PasswordVault.decryptPassword(encryptedPassword);
        ClassLoader loader = getJt400JarClassLoader();

        Object startSshAs400=getAS400(loader, AS400,USERID,new String(clearPassword));
        
        Class<?> cmdCallClass = loader.loadClass("com.ibm.as400.access.CommandCall");
        
        Object cmdCall = cmdCallClass.newInstance();
        JDReflectionUtil.callMethod_V(cmdCall,"setSystem",startSshAs400);
        JDReflectionUtil.callMethod_B(cmdCall,"run","STRTCPSVR SERVER(*SSHD)");

        JDReflectionUtil.callMethod_V(startSshAs400,"close"); 
        Thread.sleep(1000); 
      } finally {
        socket.close();
      }

      /* Make sure SSH is configured on the system for the user */
      /*
       * This means that a /home/$user/.ssh/authorized_keys file exists and permission
       * is set correctly for the file and all parent directories 
       */
      if (true) {
        char[] encryptedPassword = PasswordVault.getEncryptedPassword(TEXT_PASSWORD);
        char[] clearPassword = PasswordVault.decryptPassword(encryptedPassword);
        ClassLoader loader = getJt400JarClassLoader();

        Object checkIfAs400=getAS400(loader, AS400,USERID,new String(clearPassword));

        Class as400Class=loader.loadClass("com.ibm.as400.access.AS400");
        Class<?>[] argTypes = new Class<?>[2]; 
        Object[] args = new Object[2]; 
        argTypes[0]=as400Class;
        argTypes[1]="".getClass(); 
        args[0] = checkIfAs400; 
        args[1] = "/home/" + USERID;
        JDReflectionUtil.setClassLoader(loader); 
        Object homeDir = JDReflectionUtil.createObject("com.ibm.as400.access.IFSFile",argTypes,args); 

        if (JDReflectionUtil.callMethod_B(homeDir, "exists")) {
          Object permission = JDReflectionUtil.callMethod_O(homeDir, "getPermission");
          Object userPermission = JDReflectionUtil.callMethod_O(permission, "getUserPermission", "*PUBLIC");
          String dataAuthority = JDReflectionUtil.callMethod_S(userPermission, "getDataAuthority");
          if (dataAuthority.indexOf("W") < 0) {
            args[0] = checkIfAs400;
            args[1] = "/home/" + USERID + "/.ssh";
            Object sshDir = JDReflectionUtil.createObject("com.ibm.as400.access.IFSFile", argTypes, args);
            if (JDReflectionUtil.callMethod_B(sshDir, "exists")) {
              permission = JDReflectionUtil.callMethod_O(sshDir, "getPermission");
              userPermission = JDReflectionUtil.callMethod_O(permission, "getUserPermission", "*PUBLIC");
              dataAuthority = (String) JDReflectionUtil.callMethod_O(userPermission, "getDataAuthority");
              if (dataAuthority.indexOf("W") < 0) {
                args[0] = checkIfAs400;
                args[1] = "/home/" + USERID + "/.ssh/authorized_keys";
                Object authorizedKeysFile = JDReflectionUtil.createObject("com.ibm.as400.access.IFSFile", argTypes,
                    args);
                if (JDReflectionUtil.callMethod_B(authorizedKeysFile, "exists")) {
                  permission = JDReflectionUtil.callMethod_O(authorizedKeysFile, "getPermission");
                  userPermission = JDReflectionUtil.callMethod_O(permission, "getUserPermission", "*PUBLIC");
                  dataAuthority = (String) JDReflectionUtil.callMethod_O(userPermission, "getDataAuthority");
                  if (dataAuthority.indexOf("W") < 0) {
                  } else {
                    throw new Exception(
                        "Cannot use ssh since user permissions on " + authorizedKeysFile + " are " + dataAuthority);
                  }
                } else {
                  throw new Exception("Cannot use ssh since " + authorizedKeysFile + " does not exist");
                }
              } else {
                throw new Exception("Cannot use ssh since user permissions on " + sshDir + " are " + dataAuthority);
              }
            } else {
              throw new Exception("Cannot use ssh since " + sshDir + " does not exist");
            }
          } else {
            throw new Exception("Cannot use ssh since user permissions on " + homeDir + " are " + dataAuthority);
          }
        } else {
          throw new Exception("Cannot use ssh since " + homeDir + " does not exist");
        }
        JDReflectionUtil.callMethod_V(checkIfAs400,"close");
      }
    }

    if (true) {
      /* Run the script in a shell process */
      if (shellArgs == null) {
        cmdArray1 = new String[1];
        cmdArray1[0] = shellBinary;
        System.out.println("----------- starting shell -----------------------------");
        System.out.println("Shell binary1 is " + shellBinary);
        System.out.println("--------------------------------------------------------");
        /* Fix the command line if running on windows in */
        if (debug)
          System.out.println("JDRunit: osVersion=" + JTOpenTestEnvironment.osVersion);
        if (JTOpenTestEnvironment.isWindows && !runNativeTestFromWindows && cmdArray1[0].indexOf("/bin") >= 0) {
          cmdArray1[0] = JTOpenTestEnvironment.cygwinBase + cmdArray1[0].replace('/', '\\');
        }

        if (debug)
          System.out.println("JDRunit: Shell command is: " + cmdArray1[0]);

      } else {
        if (shellArgs3 != null) {
          cmdArray1 = new String[4];
        } else  if (shellArgs2 != null) {
          cmdArray1 = new String[3];
        } else { 
          cmdArray1 = new String[2];
        }
        cmdArray1[0] = shellBinary;
        System.out.println("----------- starting shell -----------------------------");
        System.out.println("Shell binary2 is " + shellBinary);
        System.out.println("--------------------------------------------------------");
        cmdArray1[1] = shellArgs;
        if (shellArgs2 != null) {
          cmdArray1[2] = shellArgs2; 
        }
        if (shellArgs3 != null) {
          cmdArray1[3] = shellArgs3; 
        }
        if (debug)
          System.out.println("JDRunit: Shell command (with args) is: " + cmdArray1[0] + " " + cmdArray1[1]);
      }
      if (on400 && !on400open) {
        JDJobName.sendProgramMessage(
            "GD " + Thread.currentThread().getName() + " " + date.toString() + " Running " + initials + " " + testArgs);
      }

      // If current JVM is PASE, but target JVM is classic. Don't inherit
      // any environment variables.
      // This scenario should only typically happened on V6R1
      if ((System.getProperty("java.home").indexOf("QOpenSys") > 0) && (javaHome.indexOf("QOpenSys") == -1)) {
        String[] envp = { "PATH=/usr/bin:", "HOME=" + testcaseCode, };
        if (debug)
          System.out.println("Calling exec1 with envp and cmdArray[0]=" + cmdArray1[0]);
        shellTestProcess = runtime.exec(cmdArray1, envp);
        shellTestProcessInfo = "Running with envp cmdArray1[" + cmdArray1.length + "] ";
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

      encoding = JDJSTPOutputThread.ENCODING_UNKNOWN;
      
      stdin = shellTestProcess.getOutputStream();
      stdoutThread = JDJSTPTestcase.startProcessOutput(shellTestProcess, runitOutputFile, true, hangMessages,
          hangMessagesException, hangMessagesFound, encoding, runFlag);
    }
    
    /* Setup the input */
    
    JDJSTPInputThread inputThread;
    if (debug)
      System.out.println("InputStream is ASCII");
    long sleepTime = 0; 
    if (runNativeTestFromWindows) {
      sleepTime=500; 
    }
    inputThread = new JDJSTPInputThread(stdin, runitInputFile,shellTestProcess,runFlag, sleepTime );
    encoding = JDJSTPOutputThread.ENCODING_ASCII;

    if (debug)
      JDJSTPTestcase.debug = true;


    

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
    while (!processCompleted && currentTime < endTime && hangMessagesFound.size() == 0) {
      // System.out.println("JDEBUG: process checked " + currentTime + "<"+
      // endTime);
      try {
        int rc;
        if (shellTestProcess != null) { 
          rc = shellTestProcess.exitValue();
        } else {
          rc = -1; 
        }
        processCompleted = true;
        /* Sleep to allow output to finish */ 
        
        try {
          Thread.sleep(1000);
        } catch (Exception sleepEx) {
          /* ignore */
        }
        System.out.println("---------------------------------------------"); 
        System.out.println("Process completed with rc=" + rc);
        System.out.println("---------------------------------------------"); 
        if (rc != 0) {
          System.out.println("Running info: " + shellTestProcessInfo);
          if (on400 && !on400open) {
            Date d = new Date();
            JDJobName.sendProgramMessage("LD " + Thread.currentThread().getName() + " " + d.toString() + " Rc=" + rc
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
      String hangMessage = (String) hangMessagesFound.elementAt(0);
      if (hangMessage.indexOf(TestDriver.RUN_COMPLETED) >= 0) {
        /*
         * We can exit this test. If the job is hung after this point, it will need to
         * be manually cleaned up
         */
        processCompleted = true; 
      } else {
        StringBuffer body = new StringBuffer();
        String outString = " Severe Error Running  " + initials + " " + testArgs + "\n";
        stdoutThread.addOutputString(outString);
        body.append(outString);
        if (on400 && !on400open) {
          Date d = new Date();
          JDJobName.sendProgramMessage("LD " + Thread.currentThread().getName() + " " + d.toString() + outString);
        }

        outString = "ERROR:  JDRunit: Hang message found : " + hangMessage + "\n";
        stdoutThread.addOutputString(outString);
        body.append(outString);
        System.out.print(outString);
        if (on400 && !on400open) {
          Date d = new Date();
          JDJobName.sendProgramMessage("LD " + Thread.currentThread().getName() + " " + d.toString() + outString);
        }

        //
        // Check to see if we can recover
        //
        boolean recovered = false;
        if (on400 && !on400open) {
          if ((hangMessage.indexOf("sun.awt.X11GraphicsEnvironment") > 0)
              || (hangMessage.indexOf("connection to \":9.0\"") > 0)) {
            /* Something went wrong with the VNC server */
            /* kill it */
            String command = "QSYS/SBMJOB CMD(QSH CMD('system wrkactjob | grep vnc | " + "grep -i "
                + System.getProperty("user.name") + " | "
                + " sed ''s%^ *\\([^ ][^ ]*\\) *\\([^ ][^ ]*\\) *\\([^ ][^ ]*\\) .*%system endjob \"job(\\3/\\2/\\1)\"%'' |"
                + " sh'))";
            System.out.println("Attempting kill using " + command);
            stdoutThread.addOutputString("Attempting kill using " + command + "\n");

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
            JDJSTPOutputThread cmdStdoutThread = new JDJSTPOutputThread(shellProcess.getInputStream(), outputBuffer,
                null, JDJSTPOutputThread.ENCODING_ASCII,null);
            JDJSTPOutputThread stderrThread = new JDJSTPOutputThread(shellProcess.getErrorStream(), outputBuffer, null,
                JDJSTPOutputThread.ENCODING_ASCII,null);
            cmdStdoutThread.start();
            stderrThread.start();
            cmdStdoutThread.join();
            stderrThread.join();

            shellProcess.waitFor();
            System.out.println("Process complete exitValue = " + shellProcess.exitValue());
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
            if (on400 && !on400open) {
              String command = "DSPJOBLOG JOB(" + jobname + ") OUTPUT(*PRINT)";
              outString = "Dumping job log to *PRINT using " + command + "\n";
              stdoutThread.addOutputString(outString);
              System.out.println(outString);
              JDJobName.system(command);
            }
          }
          System.out.println("ERROR:  JDRunit: Generating report and sleeping for 60 seconds");
          if (on400 && !on400open) {
            Date d = new Date();
            JDJobName.sendProgramMessage("LD " + Thread.currentThread().getName() + " " + d.toString()
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
            while (shellTestProcessAlive && retryCount <= MAXRETRIES
                && (!vectorContainsString(hangMessagesFound, "Snap dump written"))) {
              System.out.println("JVM requested System dump, waiting for dump to complete.  RetryCount=" + retryCount
                  + "/" + MAXRETRIES + " Sleeping for 60 seconds ");
              if (on400 && !on400open) {
                Date d = new Date();
                JDJobName.sendProgramMessage("LD " + Thread.currentThread().getName() + " " + d.toString()
                    + "JVM requested System dump, waiting for dump to complete.  RetryCount=" + retryCount + "/"
                    + MAXRETRIES + " Sleeping for 60 seconds ");
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
            if (!toAddress.equals("xxx@github.com")) {
              String fromAddress = iniProperties.getProperty("EMAIL");
              String subject = " Severe Error Running  " + initials + " " + testArgs;

              String hostname = JDHostName.getHostName();
              hostname = hostname.toUpperCase();

              if (hostname.indexOf('.') < 0) {
                hostname = hostname + "." + getDomain(hostname);
              }

              String URLBASE = "http://" + hostname + ":6050/";
              String htmlFile = JDReport.getReportName(initials);

              int ctIndex = htmlFile.indexOf("ct/");
              if ( ctIndex > 0 )
                htmlFile = htmlFile.substring(ctIndex+3);
              body.append("\n\nSee details at " + URLBASE + htmlFile + "\n");
              body.append("Test output " + URLBASE + "/out/" + initials + "/runit." + dateStringNoSpace + "\n");

              sendEMail(toAddress, fromAddress, subject, body, iniProperties.getProperty("mail.smtp.host"));
            }
          } catch (Exception e) {
            System.out.println("Error sending mail");
            e.printStackTrace();
          }
        }
      } /* RUN_COMPLETED_ NOT FOUND */
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
        if (on400 && !on400open) {
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

          if (JTOpenTestEnvironment.isWindows
              || JTOpenTestEnvironment.isLinux) {

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
            Vector<String> shellHangMessagesFound = new Vector<String>();
            JDJSTPOutputThread stdoutThread2 = JDJSTPTestcase
                .startProcessOutput(shellProcess, shellOutputFile, true,
                    hangMessages, hangMessagesException, shellHangMessagesFound, encoding,null);
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

        if (on400 && !on400open) {
          String info = "Parsed job name: " + stdoutThread.getParsedJobName();
          stdoutThread.addOutputString(info + "\n");
          System.out.println(info);

          // If the job is active, go ahead and kill it!!!!
          String killCommand = "call QSYS.QCMDEXC('ENDJOB JOB("
              + info
              + ") OPTION(*IMMED)                                                                   ' , 0000000060.00000)";

          try {
            System.out.println("Attempting to call " + killCommand);
            String password = PasswordVault.decryptPasswordLeak(ENCRYPTED_PASSWORD);
            Connection conn = DriverManager.getConnection("jdbc:db2:localhost",
                USERID, password);
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
      String line = enumeration.nextElement();

      writer.println(line);
    }

    /* Read the important results into memory */
    /*
     * Build the results as the test is running so less post processing needs to
     * be done
     */
    Vector<String> testcasesVector = new Vector<String>();
    Vector<String> failedVector = new Vector<String>();
    Vector<String> rxpVector = new Vector<String>();
    Hashtable<String, Vector<String>> rerunHashtable = new Hashtable<String, Vector<String>>();
    int resultsWritten = 0;
    boolean noOutput = true;
    BufferedReader outputReader = new BufferedReader(new FileReader(
        runitOutputFile));
    // System.out.println("JDEBUG: reading output "+output);
    String toolboxVersion = "NA";
    String toolboxBuildDate = "NA";
    String nativeVersion = "NA"; 
    String nativeBuildDate = "NA"; 
    
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

        Vector<String> variationsVector = rerunHashtable.get(testcase);
        if (variationsVector == null) {
          variationsVector = new Vector<String>();
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

        Vector<String> variationsVector = rerunHashtable.get(testcase);
        if (variationsVector == null) {
          variationsVector = new Vector<String>();
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
      if (line.indexOf("TOOLBOX VERSION") >= 0) { 
          int equalsIndex = line.indexOf("="); 
          if (equalsIndex > 0) { 
            toolboxVersion = line.substring(equalsIndex+1).trim().replace(' ','_'); 
          }
      }
      if (line.indexOf("TOOLBOX BUILD DATE") >= 0) { 
        int equalsIndex = line.indexOf("="); 
        if (equalsIndex > 0) { 
          toolboxBuildDate = line.substring(equalsIndex+1).trim().replace(' ','_'); 
        }
      }
      
      if (line.indexOf("NATIVE BUILD DATE") >= 0) { 
        int equalsIndex = line.indexOf("="); 
        if (equalsIndex > 0) { 
          nativeBuildDate = line.substring(equalsIndex+1).trim().replace(' ','_'); 
        }
      }

      if (line.indexOf("NATIVE JDBC VERSION") >= 0) { 
        int equalsIndex = line.indexOf("="); 
        if (equalsIndex > 0) { 
          nativeVersion = line.substring(equalsIndex+1).trim().replace(' ','_'); 
        }
      }


      line = outputReader.readLine();
    }
    outputReader.close();

    
    // Write out the codeLevel as well
    PrintWriter codeLevelWriter = new PrintWriter(new FileWriter(codeLevel, true));
    codeLevelWriter.println(dateString + " " + AS400lc + " " + toolboxVersion+" "+toolboxBuildDate+" "+nativeVersion+" "+nativeBuildDate);
    codeLevelWriter.close(); 
    
    if (disabledProfile != null) {
      try {
        if (disabledProfile.equals(USERID)) {
          System.out.println("JDRunit: Reset profile " + USERID);
          
          resetId("jdbc:db2:localhost", MASTERUSERID, ENCRYPTED_MASTERPASSWORD, USERID,
              ENCRYPTED_PASSWORD);
        } else if (disabledProfile.equals(testUserid)) {
          System.out.println("JDRunit: Reset profile " + testUserid);
          resetId("jdbc:db2:localhost", MASTERUSERID, ENCRYPTED_MASTERPASSWORD,
              testUserid, encryptedTestPassword);

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
        Vector<String> badTestVector = new Vector<String>();
        String testBase1 = testBaseProperties.getProperty(test);
        if (!testBase1.equals(test)) {
          badTestVector.add(test);
        } else {
          System.out.println("JDRunit:  Did not add test " + test
              + " because testbase is " + testBase1);

          // Find all the tests that go with this testBase
          Enumeration<?> keys = testBaseProperties.keys();
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

        Hashtable<String, String> todoTests = readTodo();
        enumeration = badTestVector.elements();
        while (enumeration.hasMoreElements()) {
          String testcase = enumeration.nextElement();
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
      String failedTest = enumeration.nextElement();
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
      String testcase = enumeration.nextElement();
      String rerunCommand = "java test.JDRunit " + initials + " " + testcase
          + " ";
      Vector<String> variationsVector = rerunHashtable.get(testcase);
      Enumeration<?> varEnum = variationsVector.elements();
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
    savefile.setReadable(true,  false);   /* Make sure the output is readable */ 
    resultsWriter.close();
  
    //
    // #
    // # Dump output files that failed
    // #
    System.out.println("JDRunit: Dumping the output files that failed");
    enumeration = rxpVector.elements();
    while (enumeration.hasMoreElements()) {
      String rxpFile = enumeration.nextElement();
      System.out.println("Processing file " + rxpFile);
      if (runNativeTestFromWindows) {
        int startIndex = rxpFile.indexOf("Files '/tmp/");
        if (startIndex >= 0) {
          startIndex += 12;
          int endIndex = rxpFile.indexOf("'", startIndex);
          if (endIndex > 0) {
            // Save the output file locally so that we can run a diff between the file and
            // expected files
            String systemFile = rxpFile.substring(startIndex,endIndex); 
            String localFile = systemFile; 
            // See if we can choose a destination close to the file to compare
            String destinationDir = "tmp";
            int expectedFileIndex = rxpFile.indexOf("' '");
            if (expectedFileIndex > 0) { 
              expectedFileIndex+=3;
              int expectedFileEnd = rxpFile.indexOf("' ",expectedFileIndex);
              if (expectedFileEnd > 0) { 
                String expectedFileName = rxpFile.substring(expectedFileIndex, expectedFileEnd); 
                int slashIndex = expectedFileName.lastIndexOf('/'); 
                if (slashIndex > 0) { 
                  String expectedDirectory=expectedFileName.substring(0,slashIndex); 
                  File expectedDirFile = new File(expectedDirectory) ;
                  if (expectedDirFile.exists()) {
                    destinationDir = expectedDirectory; 
                    localFile = systemFile+".deleteMe.txt";
                  }
                }
              }
            }
            
            // Look for a replacement file.
            Hashtable<String,String> replaceStrings = getReplaceStrings(destinationDir); 
            
            File tmpDir = new File(destinationDir);
            if (!tmpDir.exists()) {
              tmpDir.mkdir();
            }
            System.out.println("Storing files in "+tmpDir.getCanonicalPath());
            char[] encryptedPassword = PasswordVault.getEncryptedPassword(TEXT_PASSWORD); 
            char[] clearPassword = PasswordVault.decryptPassword(encryptedPassword);
            //
            // Note: reflection is used so that jt400.jar does not need to be in the classpath for JDRunit
            // 
            Object myAS400 = JDReflectionUtil.createObject("com.ibm.as400.access.AS400",AS400, USERID, clearPassword,null);
            PasswordVault.clearPassword(clearPassword);
            JDReflectionUtil.callMethod_V(myAS400,"setGuiAvailable",false); 
            Class<?>[] argTypes = new Class<?>[2]; 
            Object[] args = new Object[2]; 
            argTypes[0]=Class.forName("com.ibm.as400.access.AS400");
            argTypes[1]=systemFile.getClass(); 
            args[0] = myAS400; 
            args[1] = "/tmp/"+systemFile;
            Object ifsFile = JDReflectionUtil.createObject("com.ibm.as400.access.IFSFile",argTypes,args); 
            if (JDReflectionUtil.callMethod_B(ifsFile,"exists")) { 
              Reader ifsFileReader = (Reader) JDReflectionUtil.createObject("com.ibm.as400.access.IFSFileReader",ifsFile);
              BufferedReader  bufferedReader = new BufferedReader(ifsFileReader); 
              FileWriter fileWriter = new FileWriter(destinationDir+"/"+localFile); 
              BufferedWriter bufferedWriter = new BufferedWriter(fileWriter); 
              String readLine = bufferedReader.readLine();
              while (readLine != null) { 
                // Do the regularExpresionProcessing on the line
                readLine = doReplacement(readLine, replaceStrings); 
                bufferedWriter.write(readLine); 
                bufferedWriter.write("\n"); 
                readLine = bufferedReader.readLine();
              }
              bufferedReader.close(); 
              bufferedWriter.close(); 
            } else {
              System.out.println("Warning IFSfile "+ifsFile+" does not exist"); 
            }
          }
        }

      }
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

    return new JDRunitGoOutput(failedCount, successfulCount); 
  } 

  private String doReplacement(String readLine, Hashtable<String, String> replaceStrings) {
    Enumeration<String> keysEnum = replaceStrings.keys(); 
    while(keysEnum.hasMoreElements()) {
      String from = keysEnum.nextElement(); 
      String to = replaceStrings.get(from); 
      readLine=readLine.replaceAll(from, to);
    }
    
    return readLine; 
  }

  private Hashtable<String, String> getReplaceStrings(String destinationDir) {
    Hashtable<String, String> replaceStrings = new Hashtable<String,String>() ;
    
    String replaceStringsFilename = destinationDir+"/aaReplaceStrings.txt"; 
    File replaceStringsFile = new File(replaceStringsFilename);
    if (replaceStringsFile.exists()) {
      try { 
      BufferedReader bufferedReader = new BufferedReader(new FileReader(replaceStringsFile)); 
      String line = bufferedReader.readLine();
      while (line != null) {
        if (line.length() > 0) {
          String separatorString = line.substring(0, 1);
          String lastSeparator = line.substring(line.length() - 1);
          if (separatorString.equals(lastSeparator)) {
            int separatorIndex = line.indexOf(separatorString, 1);
            if (separatorIndex > 0) {
              String from = line.substring(1, separatorIndex);
              String to = line.substring(separatorIndex + 1, line.length() - 1);
              replaceStrings.put(from, to);
            } else {
              System.out.println("In " + replaceStringsFilename + " did not process line : " + line);
            }
          } else {
            System.out.println("In " + replaceStringsFilename + " begin/end separator mismatch for line : " + line);
          }
        }
        line = bufferedReader.readLine();
      }
      bufferedReader.close(); 
      } catch (Exception e) { 
        System.out.println("Exception processing "+replaceStringsFilename);
        e.printStackTrace(System.out); 
      }
    } else {
      String cannonicalPath; 
      try {
        cannonicalPath = replaceStringsFile.getCanonicalPath(); 
      } catch (IOException e) {
        cannonicalPath = e.toString(); 
      } 
      System.out.println("No replacement because "+replaceStringsFilename+" .. "+
          cannonicalPath+" does not exist");
    }
    
    return replaceStrings;
  }

  public static long runNumber = 0L;
  public static Object runNumberSync = new Object();

  public static long nextRunNumber() {
    synchronized (runNumberSync) {
      runNumber++;
      return runNumber;
    }
  }

        public static Hashtable<String, String> readTodo() {
                Hashtable<String, String> returnHashtable = new Hashtable<String, String>();

                try {
                        File todoFile = new File("ini/TODO.ini");
                        if (todoFile.exists()) {
                                FileInputStream fileInputStream = new FileInputStream("ini/TODO.ini");
                                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
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
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return returnHashtable;
        }

  public static boolean vectorContainsString(Vector<?> hangMessagesFound,
      String string) {
    synchronized (hangMessagesFound) {
      Enumeration<?> enumeration = hangMessagesFound.elements();
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

  static Object getAS400(ClassLoader loader, String system, String pwrUsr, String pwrPass) throws Exception  { 
    
    Class<?> as400Class = loader.loadClass("com.ibm.as400.access.AS400");
    Object as400 = as400Class.newInstance();
    JDReflectionUtil.callMethod_V(as400,"setSystemName",system);
    JDReflectionUtil.callMethod_V(as400,"setUserId",pwrUsr);
    char[] encryptedPassword = PasswordVault.getEncryptedPassword(pwrPass);
    char[] decryptedPassword = PasswordVault.decryptPassword(encryptedPassword);
    JDReflectionUtil.callMethod_V(as400,"setPassword",decryptedPassword);
    PasswordVault.clearPassword(decryptedPassword);
    
    return as400; 
    
  }
  static void checkSshSetup(String system, String pwrUsr, String pwrPass) throws Exception {
    // Check for local id_rsa and id_rsa.pub
    Runtime runtime1 = Runtime.getRuntime();

    String userHome = System.getProperty("user.home"); 
    File userHomeFile = new File(userHome); 
    if (!userHomeFile.exists()) {
      userHomeFile.mkdirs(); 
    }
    File sshDir = new File (userHome+File.separator+".ssh"); 
    if (!sshDir.exists()) {
      sshDir.mkdirs(); 
    }
    String path=""; 
    String osName = System.getProperty("os.name"); 
    if (osName.indexOf("400")>=0) { 
      path="/QOpenSys/QIBM/ProdData/SC1/OpenSSH/bin/"; 
    }
    File rsaFile = new File(userHome+File.separator+".ssh"+File.separator+"id_rsa");
    File rsapubFile = new File(userHome+File.separator+".ssh"+File.separator+"id_rsa.pub");
    if (!rsaFile.exists() || !rsapubFile.exists()) { 
      System.out.println("RSA file does not exist.. creating"); 
      String localUser=System.getProperty("user.name"); 
      InetAddress localMachine = InetAddress.getLocalHost();
      String hostname = localMachine.getHostName();
      
      String command = path+"ssh-keygen  -t rsa -b 4096  -f "+rsaFile+" -C "+localUser+"@"+hostname+" -N '' "; 
      String[] cmdArray = new String[11];
      cmdArray[0] = path+"ssh-keygen"; 
      cmdArray[1] = "-t";
      cmdArray[2] = "rsa";
      cmdArray[3] = "-b";
      cmdArray[4] = "4096";
      cmdArray[5] = "-f";
      cmdArray[6] = rsaFile.toString();
      cmdArray[7] = "-C";
      cmdArray[8] = localUser+"@"+hostname;
      cmdArray[9] = "-N";
      cmdArray[10] = "";
      System.out.println("JDRunit: generating keys using : "+command); 
      Process process = runtime1        .exec(cmdArray);
      boolean completed = process.waitFor(60, TimeUnit.SECONDS);
      if (!completed) { 
        throw new Exception("process failed:"+command); 
      }
    }
    if (!rsaFile.exists() || !rsapubFile.exists()) { 
      throw new Exception ("A rsFile does not exist at "+rsaFile+" or "+rsapubFile); 
    }    

    FileReader fileReader = new FileReader(rsapubFile); 
    BufferedReader bufferedReader1 = new BufferedReader(fileReader); 
    String rsaKey = bufferedReader1.readLine(); 
    bufferedReader1.close(); 
    
    // Now to make sure the host has the approriate keys
    ClassLoader loader = getJt400JarClassLoader();
    Object as400 = getAS400(loader, system, pwrUsr, pwrPass);
    
    // Check the SSH config on the target
    // Get the home directory. 
    Class<?> userClass = loader.loadClass("com.ibm.as400.access.User");
    boolean rsaKeyFound = false; 
    
    Object hostUser = userClass.newInstance(); 
    JDReflectionUtil.callMethod_V(hostUser,"setSystem",as400);
    JDReflectionUtil.callMethod_V(hostUser,"setName",pwrUsr);
    String homeDir = JDReflectionUtil.callMethod_S(hostUser,"getHomeDirectory"); 
    String authorizedKeysPath = homeDir+"/.ssh/authorized_keys";
    
    Class<?>[] argTypes = new Class<?>[2]; 
    Object[] args = new Object[2]; 
    argTypes[0]=Class.forName("com.ibm.as400.access.AS400");
    argTypes[1]="".getClass(); 
    args[0] = as400; 
    args[1] = authorizedKeysPath;
    Object authorizedKeysFile = JDReflectionUtil.createObject("com.ibm.as400.access.IFSFile",argTypes,args); 

    
    Vector<String> authorizedKeysVector = new Vector<String>(); 
    if (JDReflectionUtil.callMethod_B ( authorizedKeysFile,"exists")) {
      Object ifsFileReader = JDReflectionUtil.createObject("com.ibm.as400.access.IFSFileReader",authorizedKeysFile);
      BufferedReader bufferedReader = new BufferedReader((Reader)ifsFileReader); 
      String line = bufferedReader.readLine(); 
      while (line != null) { 
        if (rsaKey.equals(line) && !rsaKeyFound) { 
          rsaKeyFound = true; 
        }
        line = bufferedReader.readLine(); 
      }
      bufferedReader.close(); 
    } else {
      JDReflectionUtil.callMethod_V( authorizedKeysFile,"createNewFile");
    }
    // Make sure the permissions are correct only the file. 
    Object permission = JDReflectionUtil.createObject("com.ibm.as400.access.Permission", authorizedKeysFile); 
    Enumeration<?> enumeration = (Enumeration<?>) JDReflectionUtil.callMethod_O(  permission,"getUserPermissions");
    while (enumeration.hasMoreElements()) {
      Object rootPermission =  enumeration.nextElement();
      String permissionUser = JDReflectionUtil.callMethod_S(rootPermission,"getUserID");
      String dataAuthority = JDReflectionUtil.callMethod_S(rootPermission,"getDataAuthority"); 
      System.out.println("found permission "+permissionUser+" "+dataAuthority); 
      // found permission *PUBLIC *R
      // found permission JDPWRSYS *RW
    }

    if (!rsaKeyFound) {
      Class<?>[] argTypes1 = new Class<?>[2];
      Object[] args1 = new Object[2]; 
      argTypes1[0]=authorizedKeysFile.getClass(); 
      argTypes1[1]=Boolean.TYPE;
      args1[0]=authorizedKeysFile;
      args1[1] = new Boolean(true); 
      Object fileWriter = JDReflectionUtil.createObject("com.ibm.as400.access.IFSFileWriter",argTypes1,args1);
      JDReflectionUtil.callMethod_V(fileWriter,"write",rsaKey+"\n");
      JDReflectionUtil.callMethod_V(fileWriter,"close"); 
      System.out.println("To "+authorizedKeysFile+" wrote "+rsaKey); 
      
      // Make sure the host list is added to the local host
      String command = path+"ssh -vvv -o StrictHostKeyChecking=accept-new "+pwrUsr+"@"+system+" pwd";  
      System.out.println("To add host list running "+command); 
      Process process = runtime1.exec(command);     
      boolean completed = process.waitFor(60, TimeUnit.SECONDS);
      if (!completed) { 
        throw new Exception("process failed:"+command); 
      }
      
    }
  }

  static void startSshServer(String system, String pwrUsr, String pwrPass) throws Exception{
    ClassLoader loader = getJt400JarClassLoader();
    checkSshSetup(system, pwrUsr, pwrPass);
    // Check to see if port 22 is already up
    try {
      Socket socket1 = new Socket(system, 22);

      // If this worked then just close the socket
      socket1.close();
      System.out.println("port 22 is already up"); 
    } catch (Exception e) {
      String exInfo = e.toString();
      if (exInfo.indexOf("remote host refused") >= 0) {
        // Use a classloader so that jt400.jar does not need to be on the classpath 
        Object as400 = getAS400(loader, system, pwrUsr, pwrPass);
        
        Class<?> cmdCallClass = loader.loadClass("com.ibm.as400.access.CommandCall");
        
        Object cmdCall = cmdCallClass.newInstance();
        JDReflectionUtil.callMethod_V(cmdCall,"setSystem",as400);
        boolean done = JDReflectionUtil.callMethod_B(cmdCall,"run","STRTCPSVR SERVER(*SSHD)");
        if (!done) { 
          throw new Exception("STRTCPSVR SERVER(*SSHD) failed");
        }
        waitForPort(system, 22); 
      } else {
        e.printStackTrace();
      }
    }

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

  static void startSock5Proxy(String proxyInfo, String user, String system, String pwrUser, String pwrPass) throws Exception {
    // Make sure SSH is up
    startSshServer(system,pwrUser,pwrPass);
    // Check to see if port is already up
    // Parse the port and make sure host is localhost 
    String server = proxyInfo.toLowerCase().trim(); 
    int colonIndex=server.indexOf(":"); 
    int port = 5005; 
    if (colonIndex > 0) { 
      port = Integer.parseInt(server.substring(colonIndex+1));
      server = server.substring(0,colonIndex); 
    }
    if (!server.equals("localhost")) { 
      throw new Exception("Only localhost supported:  invalid proxy: "+proxyInfo); 
    }
    try {
      Socket socket1 = new Socket("localhost", port);

      // If this worked then just close the socket
      socket1.close();
      System.out.println("port "+port+" is already up"); 
    } catch (Exception e) {
      String exInfo = e.toString();
      if (exInfo.indexOf("remote host refused") >= 0) {
        // If not up, start the proxy in the background
        startProxy5Server(port, user, system);

      } else {
        e.printStackTrace();
      }
    }

  }


  
  
  /* Wait for the proxy to become available */
  static void waitForPort(String system, int port) throws Exception {
    boolean up = false;
    long endTime = System.currentTimeMillis() + 60000;
    while (!up && System.currentTimeMillis() < endTime) {
      try {
        Socket socket1 = new Socket(system, port);
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
      throw new Exception("host:"+system+" port "+port+" did not start withing 60 seconds"); 
    }

  }

  static void startProxyServer(String jt400jar) {
    Runtime runtime1 = Runtime.getRuntime();
    try {
       
      
        String command = "java -classpath "+testcaseCode+":"+jt400jar+" com.ibm.as400.access.ProxyServer"; 
        System.out.println("JDRunit: starting ProxyServer on default port using: "+command); 
      // Need to include testcaseCode because the source for the serialized
      // lobs is located there.
      runtime1
          .exec(command);
      waitForPort("localhost",3470);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  
  static void startProxy5Server(int port, String user, String system) {
    Runtime runtime1 = Runtime.getRuntime();
    try {
        String path=""; 
        String osName = System.getProperty("os.name"); 
        if (osName.indexOf("400")>=0) { 
          path="/QOpenSys/QIBM/ProdData/SC1/OpenSSH/bin/"; 
        }
      
        String command = path+"ssh -D "+port+" "+user+"@"+system+" -p22 -gfTN"; 
        System.out.println("JDRunit: starting ssh using: "+command); 
      // Need to include testcaseCode because the source for the serialized
      // lobs is located there.
      runtime1
          .exec(command);
      waitForPort("localhost",port);

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
      char[] encryptedAdminPassword, String testUserid, char[] encryptedTestPassword) {
    String adminPassword = null; 
    try {
      adminPassword = PasswordVault.decryptPasswordLeak(encryptedAdminPassword);
      Connection con = DriverManager.getConnection(jdbcUrl, adminUserid,
          adminPassword);
      Statement stmt = con.createStatement();

      /* Check the profile. If the password has expired, */
      /* reset it by temporarily changing to something else */
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
          String sql="CALL QSYS2.QCMDEXC('CHGUSRPRF USRPRF("
              + testUserid + ") PASSWORD(GARBAGE) STATUS(*ENABLED) ')";
          if (passwordDebug) System.out.println("passwordDebug: SQL="+sql); 
          stmt.executeUpdate(sql);
        } catch (Exception e) {
          // Ignore first
        }
      }
      /* It should always be safe to blindly enable the profile */
      String testPassword = PasswordVault.decryptPasswordLeak(encryptedTestPassword);
      String sql = "CALL QSYS2.QCMDEXC('CHGUSRPRF USRPRF(" + testUserid
          + ") PASSWORD(" + testPassword + ") STATUS(*ENABLED) ')";
      if (passwordDebug) System.out.println("passwordDebug: SQL="+sql); 
      stmt.executeUpdate(sql);

      con.close();
    } catch (Exception e) {
      System.out.println("Reset failed using "+jdbcUrl+" adminUserid=" + adminUserid+" password="+adminPassword);
      e.printStackTrace(System.out);
      if (on400 && !on400open) {
        JDJobName.sendProgramMessage("Reset of profile " + testUserid
            + " failed with " + e.toString());
      }

    }
  }
}
