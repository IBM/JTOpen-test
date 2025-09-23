///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JPingTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:  JPingTestcase.java
//
// Class Name:  JPingTestcase
//
////////////////////////////////////////////////////////////////////////

package test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.ibm.as400.util.JPing;

/**
 * Testcase JPingTestcase.
 **/
public class JPingTestcase extends Testcase {

  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JPingTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JPingTest.main(newArgs); 
   }

  protected void setup() { 
    System.setProperty("com.ibm.as400.util.DisableExit","true"); 
  }
  /***
   * Test the JPing command looking for expected arguments
   */

  public void testJPing(String[] args, String[] expectedOutput) {
    PrintStream oldOut = System.out;
    StringBuffer sb = new StringBuffer();
    try {
      boolean passed = true;
      sb.append("Overriding system.Out\n");
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(os);

      System.setOut(ps);
      JPing.main(args);
      String output = os.toString();
      sb.append("ping produced the following output\n" + output + "\n");
      // Find all the output in the string. If not found, then return error
      for (int i = 0; i < expectedOutput.length; i++) {
        if (output.indexOf(expectedOutput[i]) < 0) {
          sb.append("Error:  did not find " + expectedOutput[i] + "\n");
          passed = false;
        }
      }

      sb.append("Restoring system.Out\n");
      System.setOut(oldOut);

      assertCondition(passed, sb.toString());
    } catch (Exception e) {
      failed(e, "Unexpected exception " + sb.toString());
    } finally {
      System.setOut(oldOut);
    }

  }

  /**
   * Successful construction of an JPing using the default ctor. JPing(String)
   **/
  public void Var001() {

    // Ping is not shipped with jt400Native.jar
    if (isNative_) {
      notApplicable("JPing not shipped with jt400Native.jar");
      return;
    }

    String[] args = new String[0];
    String[] expectedOutput = {
        "USAGE: java com.ibm.as400.util.JPing [-h] <systemName> [-service <service>] [-ssl] [-verbose]",
        "[-h | -help]        Help",
        "[-service | -s]     Service: as-file, as-netprt, as-rmtcmd,",
        "as-dtaq, as-database, as-ddm,", "as-central, as-signon",
        "[-ssl]              Use SSL ports",
        "[-timeout | -t]     Timeout period in milliseconds.  Default is 20000 (20 seconds).",
        "[-verbose | -v]     Generate verbose output.", };

    testJPing(args, expectedOutput);
  }

  public void Var002() {
    if (isNative_) {
      notApplicable("JPing not shipped with jt400Native.jar");
      return;
    }
    String[] args = { "-h", };
    String[] expectedOutput = {
        "USAGE: java com.ibm.as400.util.JPing [-h] <systemName> [-service <service>] [-ssl] [-verbose]",
        "[-h | -help]        Help",
        "[-service | -s]     Service: as-file, as-netprt, as-rmtcmd,",
        "as-dtaq, as-database, as-ddm,", "as-central, as-signon",
        "[-ssl]              Use SSL ports",
        "[-timeout | -t]     Timeout period in milliseconds.  Default is 20000 (20 seconds).",
        "[-verbose | -v]     Generate verbose output.", };

    testJPing(args, expectedOutput);
  }

  public void Var003() {
    if (isNative_) {
      notApplicable("JPing not shipped with jt400Native.jar");
      return;
    }
    String[] args = { systemObject_.getSystemName(), };
    String[] expectedOutput = { "Verifying connections to system",
        "Successfully connected to server application:  as-file",
        "Successfully connected to server application:  as-netprt",
        "Successfully connected to server application:  as-rmtcmd",
        "Successfully connected to server application:  as-dtaq",
        "Successfully connected to server application:  as-database",
        "Successfully connected to server application:  as-ddm",
        "Successfully connected to server application:  as-central",
        "Successfully connected to server application:  as-signon",
        "Connection verified", };

    testJPing(args, expectedOutput);
  }

  public void Var004() {
    /* Group test systems may not have ssl configured correctly */
    if (checkNotGroupTest()) {
      if (isNative_) {
        notApplicable("JPing not shipped with jt400Native.jar");
        return;
      }
      String[] args = { systemObject_.getSystemName(), "-ssl", };
      String[] expectedOutput = { "Verifying connections to system",
          "Successfully connected to server application:  as-file-s",
          "Successfully connected to server application:  as-netprt-s",
          "Successfully connected to server application:  as-rmtcmd-s",
          "Successfully connected to server application:  as-dtaq-s",
          "Successfully connected to server application:  as-database-s",
          "Successfully connected to server application:  as-ddm-s",
          "Successfully connected to server application:  as-central-s",
          "Successfully connected to server application:  as-signon-s",
          "Connection verified", };

      testJPing(args, expectedOutput);
    }
  }
}
