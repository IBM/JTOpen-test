///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSCS5256CtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SCS5256Writer;

import test.Testcase;

/**
  Testcase NPSCS5256CtorTestcase.
  Test the constructors of the SCS5256Writer class.
  **/
public class NPSCS5256CtorTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSCS5256CtorTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
/**
  Constructor.  This is called from NPPrintTest::createTestcases().
  **/
  public NPSCS5256CtorTestcase(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    // $$$ TO DO $$$
    // Replace the fourth parameter with the total number of variations
    // in this testcase.
    super(systemObject, "NPSCS5256CtorTestcase", 5,
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
        runMode_ != ATTENDED)      {
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

  }

// $$$ TO DO $$$
// Replace the following VarXXX() methods with your own.  You should have
// a method for each variation in your testcase.
/**
  Tests creating a SCS5256Writer with default encoding.
  **/
  public void Var001()
  {
    setVariation(1);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS5256Writer writer = new SCS5256Writer(out, 37, systemObject_); // @B1C
        writer.close();
        succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception.");
    }
  }

/**
  Tests creating a SCS5256Writer with a valid CCSID.
  **/
  public void Var002()
  {
    setVariation(2);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS5256Writer writer = new SCS5256Writer(out, 37, systemObject_); // @B1C
        writer.close();
        succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception.");
    }
  }

/**
  Tests creating a SCS5256Writer with a valid encoding string.
  **/
  public void Var003()
  {
    setVariation(3);
    /* @B1D
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String enc = System.getProperty("file.encoding");

    try
    {
        SCS5256Writer writer = new SCS5256Writer(out, enc);
        writer.close();
        succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception.");
    }
    */
    notApplicable(); // @B1A
  }

/**
  Tests creating a SCS5256Writer with an invalid CCSID.
  An UnsupportedEncodingException should be thrown.
  **/
  public void Var004()
  {
    setVariation(4);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS5256Writer writer = new SCS5256Writer(out, -1, systemObject_); // @B1C
        writer.close();
        failed("Exception was not thrown.");
    }
    catch (Exception e)
    {
        assertExceptionIs(e, "UnsupportedEncodingException");
    }
  }

/**
  Tests creating a SCS5256Writer with an invalid encoding string.
  An UnsupportedEncodingException should be thrown.
  **/
  public void Var005()
  {
      /*
    setVariation(5);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String enc = "Cp000";
    try
    {
        SCS5256Writer writer = new SCS5256Writer(out, enc);
        writer.close();
        failed("Exception was not thrown.");
    }
    catch (Exception e)
    {
        assertExceptionIs(e, "UnsupportedEncodingException");
    }
    */
      notApplicable();
  }

}



