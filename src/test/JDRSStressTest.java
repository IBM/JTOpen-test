///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSStressTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.JD.RS.JDRSStress;
import test.JD.RS.JDRSStressDirectMap;
import test.JD.RS.JDRSStressNoBlockPSDirectMap;
import test.JD.RS.JDRSStressPS;
import test.JD.RS.JDRSStressPSBatch;
import test.JD.RS.JDRSStressPSDirectMap;
import test.JD.RS.JDRSStressScroll;
import test.JD.RS.JDRSStressScrollDirectMap;
import test.JD.RS.JDRSStressScrollPS;
import test.JD.RS.JDRSStressScrollPSDirectMap;
import test.JD.RS.JDRSStressUpdatePS;
import test.JD.RS.JDRSStressUpdatePSDirectMap;

/**
 * Test driver for the JDBC ResultSet class.
 **/
public class JDRSStressTest extends JDRSTest {

  /**
   * Run the test as an application. This should be called from the test driver's
   * main().
   * 
   * @param args The command line arguments.
   * 
   * @exception Exception If an exception occurs.
   **/
  public static void main(String args[]) throws Exception {
    runApplication(new JDRSStressTest(args));
  }

  /**
   * Constructs an object for applets.
   * 
   * @exception Exception If an exception occurs.
   **/
  public JDRSStressTest() throws Exception {
    super();
  }

  /**
   * Constructs an object for testing applications.
   * 
   * @param args The command line arguments.
   * 
   * @exception Exception If an exception occurs.
   **/
  public JDRSStressTest(String[] args) throws Exception {
    super(args);
  }

  /**
   * Creates the testcases.
   **/
  public void createTestcases2() {

    if (TestDriverStatic.pause_) {
      try {
        systemObject_.connectService(AS400.DATABASE);
      } catch (AS400SecurityException e) {
        // TODO Auto-generated catch block
        System.out.flush();
        e.printStackTrace();
        System.err.flush();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        System.out.flush();
        e.printStackTrace();
        System.err.flush();
      }

      try {
        Job[] jobs = systemObject_.getJobs(AS400.DATABASE);
        System.out.println("Host Server job(s): ");

        for (int i = 0; i < jobs.length; i++) {
          System.out.println(jobs[i]);
        }
      } catch (Exception exc) {
      }

      try {
        System.out.println("Toolbox is paused. Press ENTER to continue.");
        System.in.read();
      } catch (Exception exc) {
      }
      ;
    }

    addTestcase(new JDRSStress(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_));
    addTestcase(new JDRSStressScroll(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_));
    addTestcase(new JDRSStressPS(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_));

    addTestcase(new JDRSStressPSBatch(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_));

    addTestcase(new JDRSStressUpdatePS(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_));

    addTestcase(new JDRSStressScrollPS(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_));

    addTestcase(new JDRSStressDirectMap(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_));
    addTestcase(new JDRSStressScrollDirectMap(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_));

    addTestcase(new JDRSStressPSDirectMap(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_));
    addTestcase(new JDRSStressScrollPSDirectMap(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_));
    addTestcase(new JDRSStressUpdatePSDirectMap(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_));

    addTestcase(new JDRSStressNoBlockPSDirectMap(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_));

  }
}
