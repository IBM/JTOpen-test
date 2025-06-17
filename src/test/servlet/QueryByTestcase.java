///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  QueryByTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.servlet;

import java.io.*;
import java.util.*;
import java.sql.*;

public class QueryByTestcase extends BaseHandler {

  public static Hashtable<String, String> history = new Hashtable<String, String>();

  public static boolean initialsMatch(String initials, String endInitial) {
    try {
      if (endInitial == null)
        return true;

      int checkLength = endInitial.length();
      for (int i = 0; i < checkLength; i++) {
        String check = endInitial.substring(i, i + 1);
        if (initials.endsWith(check)) {
          return true;
        }

      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
    return false;
  }

  public static void execute(PrintWriter printWriter, Map<String, String[]> parameterMap) throws Throwable {
    StringBuffer sb = new StringBuffer();
    try {

      sb.append("Content-Type: text/html\r\n\r\n");
      sb.append("<html><head><TITLE>QueryByTestcase</TITLE></head><body><h1>QueryByTestcase</h1>\n");

      String testcase = getArg("TESTCASE", parameterMap);
      String endInitial = getArg("ENDINITIAL", parameterMap);
      if (endInitial != null) {
        endInitial = endInitial.trim();
        if (endInitial.length() == 0) {
          endInitial = null;
        }
      }
      if (testcase == null) {
        testcase = "";
        sb.append("<form>\n");
        sb.append("<table>\n");
        sb.append("<td>TESTCASE <br><td><INPUT NAME=\"TESTCASE\" VALUE=\"" + testcase + "\" size = 10><tr>\n");
        sb.append("<td>ENDINITIALS <br><td><INPUT NAME=\"ENDINITIAL\"  size = 10><tr>\n");
        sb.append("<tr>\n");
        sb.append("</table>\n");
        sb.append("<INPUT TYPE=\"submit\" VALUE=\"GO\"><p>\n");
        sb.append("</form>\n");
      } else {

        // System.out.println("In QueryByteTestcaseEngine :: getting statement ");
        Statement statement = SchedulerManager.getStatement(sb);
        // System.out.println("In QueryByteTestcaseEngine :: printing header ");
        // print the header
        sb.append("<table border>\n");
        sb.append(
            "<th>FINISHTIME<th>SYSTEM<th>INITIALS<th>TESTCASE<th>SUCC<th>FAILED<th>NOTAPPL<th>NOTATT<th>TIME<tr>\n");

        // Assume all the status files are up to date run the query directly

        // Get the list of all the run initials
        // Find all runit.out files
        // System.out.println("In QueryByteTestcaseEngine :: listing files ");
        File curdir = new File(".");
        File[] files = curdir.listFiles();
        Arrays.sort(files);
        StringBuffer errorSb = new StringBuffer();
        Vector<String> failedTestcases = new Vector<String>();
        // System.out.println("In QueryByteTestcaseEngine: Looping through tests");
        for (int i = 0; i < files.length; i++) {
          String filename = files[i].getName();
          if (filename.startsWith("runit")) {
            if (filename.endsWith(".out")) {
              // Run the query for each.

              String initials = filename.substring(5, filename.length() - 4);

              if (endInitial == null || initialsMatch(initials, endInitial)) {
                String query;
                if (testcase.indexOf("*") > 0) {
                  query = "select FINISHTIME,SYSTEM,TESTCASE,SUCCEEDED,FAILED,NOTAPPL,NOTATT,TIME FROM JDTESTINFO.LT"
                      + initials + " WHERE TESTCASE like '" + testcase.replace('*', '%') + "' order by TESTCASE";

                } else {
                  query = "select FINISHTIME,SYSTEM,TESTCASE,SUCCEEDED,FAILED,NOTAPPL,NOTATT,TIME FROM JDTESTINFO.LT"
                      + initials + " WHERE TESTCASE= '" + testcase + "'";
                }
                try {
                  ResultSet rs = statement.executeQuery(query);
                  while (rs.next()) {
                    String time = rs.getString(1);
                    time = time.replace(' ', '-').replace(':', '.');
                    time = time.substring(0, 19);
                    sb.append("<tr>\n");
                    sb.append(" <td><a href=\"/out/" + initials + "/runit." + time + "\">" + time + "</a>\n");
                    sb.append(" <td>" + rs.getString(2) + "\n");
                    sb.append(" <td>" + initials + "\n");
                    sb.append(" <td>" + rs.getString(3) + "\n");
                    sb.append(" <td>" + rs.getString(4) + "\n");
                    int failed = rs.getInt(5);
                    boolean added = false;
                    if (failed != 0) {
                      sb.append(" <td bgcolor=\"red\">" + failed + "\n");
                      failedTestcases.add(initials + " " + rs.getString(3));
                      added = true;
                    } else {
                      sb.append(" <td>0\n");
                    }
                    sb.append(" <td>" + rs.getString(6) + "\n");

                    int notAtt = rs.getInt(7);
                    if (notAtt != 0) {
                      sb.append(" <td bgcolor=\"red\">" + notAtt + "\n");
                      if (!added) {
                        failedTestcases.add(initials + " " + rs.getString(3));
                      }

                    } else {
                      sb.append(" <td>0\n");
                    }

                    sb.append(" <td>" + rs.getString(8) + "\n");
                    sb.append("</tr>\n");

                  }
                  rs.close();

                  printWriter.println(sb.toString());
                  printWriter.flush();
                  sb.setLength(0);

                } catch (Exception e) {
                  String eString = e.toString();
                  errorSb.append("<h3>Error " + eString + " caught</h3>\n");
                  errorSb.append("<pre>\n");
                  errorSb.append("Query = " + query + "\n");
                  errorSb.append("</pre>\n");
                }
              } /* filename.endswith(endInitial)) */
            } /* ends with .out */
          } /* starts with runit */
        } /* for */
        // System.out.println("In QueryByteTestcaseEngine: done with loop ");
        statement.close();
        sb.append("</table>\n");

        String historyInfo = "TESTCASE=" + testcase;
        if (endInitial != null) {
          historyInfo += "&ENDINITIAL=" + endInitial;
        }
        if (!failedTestcases.isEmpty()) {
          history.put(historyInfo, historyInfo);
          sb.append("<h2> Rerun failed commands</h2>\n");
          sb.append("<pre>\n");
          Enumeration<String> e = failedTestcases.elements();
          while (e.hasMoreElements()) {
            String runTestcase = e.nextElement();
            sb.append("java test.JDRunit " + runTestcase + "\n");
            int spaceIndex = runTestcase.indexOf(' ');
            if (spaceIndex > 0) {
              sb.append("java test.JDRunit " + runTestcase.substring(0, spaceIndex) + " REPORT\n");
            }
          }

          sb.append("</pre>\n");

        } else {
          history.remove(historyInfo);
        }

        String errorInfo = errorSb.toString();
        if (errorInfo.length() > 0) {
          sb.append("<h2>ERROR INFO</h2>\n");
          sb.append(errorInfo);
        }

      }

      sb.append("<HR>FAILED HISTORY<HR>\n");
      Enumeration<String> e = history.keys();
      while (e.hasMoreElements()) {
        String historyInfo = (String) e.nextElement();
        sb.append("<a href=\"QueryByTestcase.acgi?");
        sb.append(historyInfo);
        sb.append("\">");
        sb.append(historyInfo);
        sb.append("</a><br>\n");
      }
      sb.append("<HR>DONE<HR></body></html>");

      printWriter.println(sb.toString());
      printWriter.flush();
    } catch (Throwable e) {
      if (sb != null) {
        printWriter.println(sb.toString());
      }
      printWriter.println("<h2>Exception occurred in update note engine</h2>");
      printWriter.println("<pre>");
      e.printStackTrace(printWriter);
      printWriter.println("</pre>");
      printWriter.println("<HR>DONE<HR></body></html>");
      printWriter.flush();

    }

  }

}
