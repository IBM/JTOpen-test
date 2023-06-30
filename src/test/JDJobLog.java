///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDJobLog.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.BufferedReader;
import java.io.PrintStream;

public class JDJobLog {


    public static String[][] filters = {
        /* lines, regex */ 
	{ "1", ".*SS1.*Display Job Log.*" },
	{ "1", ".*Job name . . .*User  . . .*Number . . ..*"},
	{ "1", ".*Job description  . ..*Library . . ..*"},
	{ "1", ".*MSGID .* TYPE .*SEV.*DATE.*TIME.*"},
	{ "WHILESTARTS                                  "," *CPF1124.*"},
	{ "WHILESTARTS                                  "," *CPIAD02.*"},
	{ "WHILESTARTS                                  "," *CPF1301.*"},
	{ "WHILESTARTS                                  "," *SQL0601.*"},
	{ "WHILESTARTS                                  "," *CPC73.*"},
	{ "WHILESTARTS                                  "," *CZS0.*"},
	{ "WHILESTARTS                                  "," *CPC5D07.*"},
	{ "WHILESTARTS                                  "," *CPC2191.*"},
	{ "WHILESTARTS                                  "," *CPF5813.*"},
	{ "WHILESTARTS                                  "," *CPF5812.*"},


  };

  static String whileStarts = null;

  public static void filterJobLog(PrintStream out, BufferedReader reader) {
    try {
      String line = reader.readLine();
      while (line != null) {
        boolean print = true;
        // Check for a match
        for (int i = 0; print && i < filters.length; i++) {
          if (line.matches(filters[i][1])) {
            String action = filters[i][0];
            if (action.equals("1")) {
              print = false;
            } else if (action.indexOf("WHILESTARTS") == 0) {
              whileStarts = action.substring(11);
              print = false;
            } else {
              out.println("JDJobLog.ERROR: Unknown action = " + action);
            }
          }
        } /* for i */
        if (print) {
          if (whileStarts != null) {
            if (line.indexOf(whileStarts) == 0) {
              print = false;
            } else {
              // Turn off the starts with
              whileStarts = null;
            }
          }
        }
        if (print) {
          out.println(line);
        }
        line = reader.readLine();
      }
    } catch (Exception e) {
      e.printStackTrace(out);
    }

  }

}
