///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ThreadTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.Enumeration;

import test.Thread.CmdCallThreadTestcase;
import test.Thread.DDMThreadTestcase;
import test.Thread.DQThreadTestcase;
import test.Thread.IFSThreadTestcase;
import test.Thread.PgmCallThreadTestcase;

/**
  Test driver for the AS400 component thread testing.
  Refer to TestDriver for calling syntax.
  **/
public class ThreadTest extends TestDriver
{

/**
  Main for running standalone application tests.
  **/
  public static void main(String args[])
  {
    try {
      ThreadTest example = new ThreadTest(args);
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

    // In case not all Connections closed cleanly,
    // this causes all threads to end.
       System.exit(0);
  }

/**
  This ctor used for applets.
  @exception Exception Initialization errors may cause an exception.
  **/
  public ThreadTest()
       throws Exception
  {
    super();
  }

/**
  This ctor used for applications.
  @param args the array of command line arguments
  @exception Exception Incorrect arguments will cause an exception
  **/
  public ThreadTest(String[] args)
       throws Exception
  {
    super(args);
  }

/**
  Creates Testcase objects for all the testcases in this component.
  **/
  public void createTestcases()
  {
    // Instantiate all testcases to be run.
    boolean allTestcases = (namesAndVars_.size() == 0);

    if (allTestcases || namesAndVars_.containsKey("IFSThreadTestcase"))
    {
      IFSThreadTestcase tc =
        new IFSThreadTestcase(systemObject_,
                      namesAndVars_.get("IFSThreadTestcase"), runMode_,
                     fileOutputStream_, password_);
      addTestcase(tc);
      namesAndVars_.remove("IFSThreadTestcase");
    }
    if (allTestcases || namesAndVars_.containsKey("DQThreadTestcase"))
    {
      DQThreadTestcase tc =
        new DQThreadTestcase(systemObject_,
                     namesAndVars_.get("DQThreadTestcase"), runMode_,
                     fileOutputStream_, password_);
      addTestcase(tc);
      namesAndVars_.remove("DQThreadTestcase");
    }
    if (allTestcases || namesAndVars_.containsKey("CmdCallThreadTestcase"))
    {
      CmdCallThreadTestcase tc =
        new CmdCallThreadTestcase(systemObject_,
                    namesAndVars_.get("CmdCallThreadTestcase"), runMode_,
                     fileOutputStream_, password_);
      addTestcase(tc);
      namesAndVars_.remove("CmdCallThreadTestcase");
    }
///    if (allTestcases || namesAndVars_.containsKey("JDBCThreadTestcase"))
///    {
///      JDBCThreadTestcase tc =
///        new JDBCThreadTestcase(systemObject_,
///                     (Vector) namesAndVars_.get("JDBCThreadTestcase"), runMode_,
///                     fileOutputStream_, password_);
///      addTestcase(tc);
///      namesAndVars_.remove("JDBCThreadTestcase");
///    }
    if (allTestcases || namesAndVars_.containsKey("PgmCallThreadTestcase"))
    {
      PgmCallThreadTestcase tc =
        new PgmCallThreadTestcase(systemObject_,
                     namesAndVars_.get("PgmCallThreadTestcase"), runMode_,
                     fileOutputStream_, password_);
      addTestcase(tc);
      namesAndVars_.remove("PgmCallThreadTestcase");
    }
    if (allTestcases || namesAndVars_.containsKey("DDMThreadTestcase"))
    {
      DDMThreadTestcase tc =
        new DDMThreadTestcase(systemObject_,
                     namesAndVars_.get("DDMThreadTestcase"), runMode_,
                     fileOutputStream_, password_);
      addTestcase(tc);
      namesAndVars_.remove("DDMThreadTestcase");
    }

    // Put out error message for each invalid testcase name.
    for (Enumeration<String> e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + " not found.");
    }
  }
}



