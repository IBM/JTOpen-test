///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  EventLogTest.java
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


public class EventLogTest extends TestDriver
{
  public static void main(String[] args)
  {
    try
    {
      EventLogTest tests = new EventLogTest(args);
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
  public EventLogTest()
    throws Exception
  {
    super();
  }


  public EventLogTest(String[] args) throws Exception
  {
    super(args);
  }

  public void createTestcases()
  {
    boolean allTestcases = (namesAndVars_.size() == 0);
    if (allTestcases || namesAndVars_.containsKey("EventLogTestcase"))
    {
      try
      {
        EventLogTestcase test =
          new EventLogTestcase(systemObject_,
                              (Vector) namesAndVars_.get("EventLogTestcase"),
                              runMode_, fileOutputStream_);
        testcases_.addElement(test);
      }
      catch(Exception e)
      {
        System.out.println(e);
      }
      namesAndVars_.remove("EventLogTestcase");
    }


    for (Enumeration e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + "unknown.");
    }
  }
}
