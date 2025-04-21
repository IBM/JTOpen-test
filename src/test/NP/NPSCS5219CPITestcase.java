///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSCS5219CPITestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SCS5219Writer;

import test.Testcase;

/**
  Testcase NPSCS5219CPITestcase.
  Test the setCPI method of the SCS5219Writer class.
  **/
public class NPSCS5219CPITestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSCS5219CPITestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
/**
  Constructor.  This is called from NPPrintTest::createTestcases().
  **/
  public NPSCS5219CPITestcase(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    // $$$ TO DO $$$
    // Replace the fourth parameter with the total number of variations
    // in this testcase.
    super(systemObject, "NPSCS5219CPITestcase", 2,
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
        runMode_ != ATTENDED) // Note: This is an unattended variation.
    {
      setVariation(1);
      Var001();
    }

    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED) // Note: This is an unattended variation.
    {
      setVariation(2);
      Var002();
    }

  }

// $$$ TO DO $$$
// Replace the following VarXXX() methods with your own.  You should have
// a method for each variation in your testcase.
/**
  Tests the setCPI method of the SCS5219Writer class with valid paramaters.
  **/
  public void Var001()
  {
    setVariation(1);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS5219Writer writer = new SCS5219Writer(out, systemObject_.getCcsid(), systemObject_);
        writer.setCPI(10);
        writer.setCPI(12);
        writer.setCPI(15);
        writer.close();
        succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception.");
    }
  }

/**
  Tests the setCPI method of the SCS5219Writer class with an invalid 
  parameter.  ExtendedIllegalArgumentException should be thrown.
  **/
  public void Var002()
  {
    setVariation(2);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS5219Writer writer = new SCS5219Writer(out, systemObject_.getCcsid(), systemObject_);
        writer.setCPI(1);
        writer.close();
        failed("Exception not thrown");
    }
    catch (Exception e)
    {
        assertExceptionIs(e, "ExtendedIllegalArgumentException");
    }
  }

}



