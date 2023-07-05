///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ExampleDriver.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.Vector;
import java.util.Enumeration;

// $$$ TO DO $$$
// Globally replace "Example" with your component name or description.
/**
Test driver for the Example component.
See TestDriver for calling syntax.
@see TestDriver
**/
public class ExampleDriver extends TestDriver
{

/**
Main for running standalone application tests.
**/
  public static void main(String args[])
  {
    try {
      ExampleDriver example = new ExampleDriver(args);
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
  }

/**
This ctor used for applets.
@exception Exception Initialization errors may cause an exception.
**/
  public ExampleDriver()
       throws Exception
  {
    super();
  }

/**
This ctor used for applications.
@param args the array of command line arguments
@exception Exception Incorrect arguments will cause an exception
**/
  public ExampleDriver(String[] args)
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

    // $$$ TO DO $$$
    // Repeat the following 'if' block for each testcase.
    // Replace all "example" occurances with the testcase name.
    // Replace the ExampleTestcase with the name of your testcase.
    if (allTestcases || namesAndVars_.containsKey("example"))
    {
      ExampleTestcase tc =
        new ExampleTestcase(systemObject_,
                     (Vector) namesAndVars_.get("example"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("example");
    }

    // Put out error message for each invalid testcase name.
    for (Enumeration e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + " not found.");
    }
  }
}

