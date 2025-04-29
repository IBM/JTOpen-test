///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ExampleTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.Testcase;

/**
  Testcase ExampleTestcase.
  **/
public class ExampleTestcase extends Testcase
{
// $$$ TO DO $$$
// Replace "ExampleDriver" with the name of your test driver program.
  
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ExampleTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ExampleDriver.main(newArgs); 
   }

/**
  Constructor.  This is called from ExampleDriver::createTestcases().
  **/
  public ExampleTestcase(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    // $$$ TO DO $$$
    // Replace the fourth parameter (7) with the total number of variations
    // in this testcase.
    super(systemObject, "ExampleTestcase", 7,
          variationsToRun, runMode, fileOutputStream);
  }

/**
  Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    // $$$ TO DO $$$
    // Replace the following 'if' blocks.  You should have one 'if' block
    // for each variation in your testcase.  Make sure you correctly set
    // the variation number and runmode in the 'if' condition, and the
    // variation number in the setVariation call.
    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != UNATTENDED) // Note: This is an attended variation.
    {
      setVariation(1);
      Var001();
    }

    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)  // Note: This is an unattended variation.
    {
      setVariation(2);
      Var002();
    }

    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      Var003();
    }

    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED)
    {
      setVariation(4);
      Var004();
    }

    if ((allVariations || variationsToRun_.contains("5")) &&
        runMode_ != ATTENDED)
    {
      setVariation(5);
      Var005();
    }

    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != ATTENDED)
    {
      setVariation(6);
      Var006();
    }

    if ((allVariations || variationsToRun_.contains("7")) &&
        runMode_ != ATTENDED)
    {
      setVariation(7);
      Var007();
    }
  }

// $$$ TO DO $$$
// Replace the following VarXXX() methods with your own.  You should have
// a method for each variation in your testcase.
/**
  Some description here.  This will becaome part of the testplan,
  so be descriptive and complete!  Include high-level description
  of what is being tested and expected results.
  **/
  public void Var001()
  {
    // Put your test variation code here.
    succeeded();  // Note: This variation will be successful.
  }

/**
  Tests an invalid value for the authority parameter.  An exception
  should be thrown.
  **/
  public void Var002()
  {
    // Put your test variation code here.
    failed();  // Note: This variation will fail, no message.
  }

/**
  Tests ending the communication connection during an operation.  An
  exceptin should be thrown.
  **/
  public void Var003()
  {
    // Put your test variation code here.
    notApplicable();  // Note: This variation will be not applicable.
  }

/**
  Instantiate a stream with the read-only atttribute. Verify the attribute
  was set correctly.
  **/
  public void Var004()
  {
    // Put your test variation code here.
    failed("Incorrect attribute value.");  // Note: This variation will fail.
  }

/**
  Write to a file using a string with embedded nulls.
  **/
  public void Var005()
  {
    // Put your test variation code here.
    failed(new IOException(), "Write to file failed.");  // Note: This variation will fail.
  }

/**
  Create a data queue with a DBCS text description.  Verify the queue
  is created with the correct text.
  **/
  public void Var006()
  {
    // Put your test variation code here.
    assertCondition(true);  // Note: This variation will be successful.
  }

/**
  Create a file with length = 100.  Verify it is created correctly.
  **/
  public void Var007()
  {
    // Put your test variation code here.
    assertCondition(false == true);  // Note: This variation will fail.
  }
}





