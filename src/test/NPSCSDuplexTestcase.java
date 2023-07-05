///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSCSDuplexTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SCS3812Writer;

/**
  Testcase NPSCSDuplexTestcase.
  Test the setDuplex method of the SCS3812Writer class.
  **/
public class NPSCSDuplexTestcase extends Testcase
{
/**
  Constructor.  This is called from NPPrintTest::createTestcases().
  **/
  public NPSCSDuplexTestcase(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    // $$$ TO DO $$$
    // Replace the fourth parameter with the total number of variations
    // in this testcase.
    super(systemObject, "NPSCSDuplexTestcase", 4,
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

    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED) // Note: This is an unattended variation.
    {
      setVariation(4);
      Var004();
    }

  }


// $$$ TO DO $$$
// Replace the following VarXXX() methods with your own.  You should have
// a method for each variation in your testcase.
/**
  Tests the setDuplex method of the SCS3812Writer class 
  with a valid paramater.
  **/
  public void Var001()
  {
    setVariation(1);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS3812Writer writer = new SCS3812Writer(out, systemObject_.getCcsid(), systemObject_);
        writer.setDuplex(SCS3812Writer.DUPLEX_SIMPLEX);
        writer.write("This is page one of a simplex job.");
        writer.endPage();
        writer.write("This is page two.");
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
  Tests the setDuplex method of the SCS3812Writer class 
  with a valid parameter.
  **/
  public void Var002()
  {
    setVariation(2);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS3812Writer writer = new SCS3812Writer(out, systemObject_.getCcsid(), systemObject_);
        writer.setDuplex(SCS3812Writer.DUPLEX_DUPLEX);
        writer.write("This is page one of a duplex job.");
        writer.endPage();
        writer.write("This is page two.");
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
  Tests the setDuplex method of the SCS3812Writer class 
  with a valid parameter.
  **/
  public void Var003()
  {
    setVariation(3);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS3812Writer writer = new SCS3812Writer(out, systemObject_.getCcsid(), systemObject_);
        writer.setDuplex(SCS3812Writer.DUPLEX_TUMBLE);
        writer.write("This is page one of a tumble duplex job.");
        writer.endPage();
        writer.write("This is page two.");
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
  Tests the setDuplex method of the SCS38124Writer class 
  with an invalid parameter.  An ExtendedIllegalArgumentException
  should be thrown.
  **/
  public void Var004()
  {
    setVariation(4);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS3812Writer writer = new SCS3812Writer(out, systemObject_.getCcsid(), systemObject_);
        writer.setDuplex(6);
        writer.write("Duplex value is invalid.");
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



