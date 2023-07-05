///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  FDTest.java
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

/**
Test driver for the FieldDescription component.
The following testcases can be run:
<ul compact>
<li>FDConstructAndGet
<br>
This test class verifes valid usage of the
constructors and getters for the FieldDescription subclasses.  This test class also
verifies the isVariableLength() and setVariableLength() methods for those
classes which implement the VariableLengthFieldDescription interface.
<li>FDInvUsage
<br>
This class verifies invalid usage of the
constructors of the FieldDescription subclasses. Note: There is no
invalid usage of the getters for the FieldDescription subclasses.
Invalid usage of the setters is verified in FDSet.
<li>FDSet
<br>
This test class verifes valid and invalid usage of the
setters for the FieldDescription subclasses.  Note: This test class does not
verify the isVariableLength() and setVariableLength() methods for those
classes which implement the VariableLengthFieldDescription interface.  These
methods are verified by the FDConstructAndGet test class.
<li>FDSerialization
<br>
This test class verifies the abillity to
serialize and deserialize FieldDescription objects.
</ul>
**/
public class FDTest extends TestDriver
{

/**
Main for running standalone application tests.
**/
  public static void main(String args[])
  {
    try {
      FDTest fd = new FDTest(args);
      fd.init();
      fd.start();
      fd.stop();
      fd.destroy();

         System.exit(0);
    }
    catch (Exception e)
    {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    }
  }

/**
This ctor used for applets.
@exception Exception Initialization errors may cause an exception.
**/
  public FDTest()
       throws Exception
  {
    super();
  }

/**
This ctor used for applications.
@param args the array of command line arguments
@exception Exception Incorrect arguments will cause an exception
**/
  public FDTest(String[] args)
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
    // Replace all "fd" occurances with the testcase name.
    // Replace the FDConstructAndGet with the name of your testcase.
    if (allTestcases || namesAndVars_.containsKey("FDConstructAndGet"))
    {
      FDConstructAndGet tc =
        new FDConstructAndGet(systemObject_,
                     (Vector) namesAndVars_.get("FDConstructAndGet"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("FDConstructAndGet");
    }

    if (allTestcases || namesAndVars_.containsKey("FDInvUsage"))
    {
      FDInvUsage tc =
        new FDInvUsage(systemObject_,
                     (Vector) namesAndVars_.get("FDInvUsage"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("FDInvUsage");
    }

    if (allTestcases || namesAndVars_.containsKey("FDSet"))
    {
      FDSet tc =
        new FDSet(systemObject_,
                     (Vector) namesAndVars_.get("FDSet"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("FDSet");
    }

    if (allTestcases || namesAndVars_.containsKey("FDSerialization"))
    {
      FDSerialization tc =
        new FDSerialization(systemObject_,
                     (Vector) namesAndVars_.get("FDSerialization"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("FDSerialization");
    }

    // Put out error message for each invalid testcase name.
    for (Enumeration e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + " not found.");
    }
  }
}
