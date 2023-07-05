///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  TraceTests.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.io.*;
import java.util.Vector;
import java.util.Enumeration;


public class TraceTests extends TestDriver
{
  public static void main(String[] args)
  {
    try
    {
      TraceTests tests = new TraceTests(args);
      tests.init();
      tests.start();
      tests.stop();
      tests.destroy();
    }
    catch(Exception e)
    {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    }

       System.exit(0);
  }
  public TraceTests()
    throws Exception
  {
    super();
  }


  public TraceTests(String[] args) throws Exception
  {
    super(args);
  }

  public void createTestcases()
  {
    boolean allTestcases = (namesAndVars_.size() == 0);
    if (allTestcases || namesAndVars_.containsKey("TraceMiscTestcase"))
    {
      try
      {
        TraceMiscTestcase test =
          new TraceMiscTestcase(systemObject_,
                              (Vector) namesAndVars_.get("TraceMiscTestcase"),
                              runMode_, fileOutputStream_);
        testcases_.addElement(test);
      }
      catch(Exception e)
      {
        System.out.println(e);
      }
      namesAndVars_.remove("TraceMiscTestcase");
    }


    for (Enumeration e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + "unknown.");
    }
  }
}




