///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSCSCharRotateTestcase.java
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
import com.ibm.as400.access.SCS5553Writer;

import test.Testcase;

/**
  Testcase NPSCSCharRotateTestcase.
  Test the setCharacterRotation method of the SCS5553Writer class.
  **/
public class NPSCSCharRotateTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSCSCharRotateTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
/**
  Constructor.  This is called from NPPrintTest::createTestcases().
  **/
  public NPSCSCharRotateTestcase(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    // $$$ TO DO $$$
    // Replace the fourth parameter with the total number of variations
    // in this testcase.
    super(systemObject, "NPSCSCharRotateTestcase", 3,
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

    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED) // Note: This is an unattended variation.
    {
      setVariation(3);
      Var003();
    }

  }


// $$$ TO DO $$$
// Replace the following VarXXX() methods with your own.  You should have
// a method for each variation in your testcase.
/**
  Tests the setCharacterRotation method of the SCS5553Writer class 
  with a valid paramater.
  **/
  public void Var001()
  {
    setVariation(1);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS5553Writer writer = new SCS5553Writer(out, systemObject_.getCcsid(), systemObject_);
        writer.setCharacterRotation(SCS5553Writer.CHARACTER_NORMAL);
        writer.write("This line should print in normal character rotation.");
        writer.endPage();
        writer.close();
        succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception.");
    }
  }


/**
  Tests the setCharacterRotation method of the SCS5553Writer class 
  with a valid parameter.
  **/
  public void Var002()
  {
    setVariation(2);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS5553Writer writer = new SCS5553Writer(out, systemObject_.getCcsid(), systemObject_);
        writer.setCharacterRotation(SCS5553Writer.CHARACTER_ROTATED);
        writer.write("This line should print with rotated characters.");
        writer.endPage();
        writer.close();
        succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception.");
    }
  }


/**
  Tests the setCharacterRotation method of the SCS55534Writer class 
  with an invalid parameter.  An ExtendedIllegalArgumentException
  should be thrown.
  **/
  public void Var003()
  {
    setVariation(3);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS5553Writer writer = new SCS5553Writer(out, systemObject_.getCcsid(), systemObject_);
        writer.setCharacterRotation(4);
        writer.write("Character rotation value is invalid.");
        writer.endPage();
        writer.close();
        failed("Exception not thrown");
    }
    catch (Exception e)
    {
        assertExceptionIs(e, "ExtendedIllegalArgumentException");
    }
  }

}



