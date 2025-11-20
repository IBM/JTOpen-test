///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ManifestTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.Enumeration;
import java.util.Vector;


/**
 * Tests for the build-generated jar files and their manifests and package information.
 **/
public class ManifestTest extends TestDriver
{

  /**
   * Main for running standalone application tests.
   **/
  public static void main(String args[])
  {
    try
    {
      ManifestTest example = new ManifestTest(args);
      example.init();
      example.start();
      example.stop();
      example.destroy();
    }
    catch (Exception e)
    {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    }

    // Needed to make the virtual machine quit.
      System.exit(0);
  }

  /**
   * This ctor used for applets.
   * @exception Exception Initialization errors may cause an exception.
   **/
  public ManifestTest() throws Exception
  {
    super();
  }

  /**
   * This ctor used for applications.
   * @param args the array of command line arguments
   * @exception Exception Incorrect arguments will cause an exception
   **/
  public ManifestTest(String[] args) throws Exception
  {
    super(args);
  }

  /**
   * Creates Testcase objects for all the testcases in this component.
   **/
  public void createTestcases()
  {
    // Instantiate all testcases to be run.
    boolean allTestcases = (namesAndVars_.size() == 0);

    // Repeat the following 'if' block for each testcase.
    if (allTestcases || namesAndVars_.containsKey("ManifestTestcase"))
    {
      ManifestTestcase tc = new ManifestTestcase(systemObject_, (Vector<String>) namesAndVars_.get("ManifestTestcase"), runMode_, fileOutputStream_);
      addTestcase(tc);
      namesAndVars_.remove("ManifestTestcase");
    }
    // Put out error message for each invalid testcase name.
    for (Enumeration<String> e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + " not found.");
    }
  }

  /**
   * Cleanup.
   **/
  public void destroy()
  {
    super.destroy();
  }
}
