//////////////////////////////////////////////////////////////////////
 //
 ///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSCS5219Testcase.java
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
import com.ibm.as400.access.*;

import test.Testcase;

/**
  Testcase NPSCS5219Testcase.
  Attended test for SCS5256Writer class. This testcase creates three
  spooled files that must be printed on a 5256 (or better) continuous-
  forms type printer.  The output is self-explanatory as to the
  results of the test.  This testcase assumes the 5219 is setup for
  cut-sheet forms.
  **/
public class NPSCS5219Testcase extends Testcase
{
  // the printer device name
  String printer_ = null;
  AS400  system_ = null;

/**
  Constructor.  This is called from NPPrintTest::createTestcases().
  **/
  public NPSCS5219Testcase(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String           printer)
      throws IOException
  {
    // $$$ TO DO $$$
    // Replace the third parameter with the total number of variations
    // in this testcase.
    super(systemObject, "NPSCS5219Testcase", 1,
          variationsToRun, runMode, fileOutputStream);

    system_ = systemObject;

    if (printer != null)
    {
       // set the printer variable
       printer_ = printer;
    }
    else
    {
       throw new IOException("The -misc flag must be set to a valid printer device name.");
    }
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
        runMode_ == ATTENDED) // Note: This is an attended variation.
    {
        try
        {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, null, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(4000,0);

            if (wrtJ.getStringAttribute(PrintObject.ATTR_WTRJOBSTS).trim().equals("*ACTIVE"))
            {
                // Run the variations
                setVariation(1);
                Var001();

                // go to sleep for a bit to allow the writer to finish printing
                Thread.sleep(10000,0);

                // end the writer we started
                wrtJ.end("*IMMED");

                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(4000,0);
            }
            else failed ("Could not start a writer job.");

        } // end try block

        catch (Exception e)
        {
	        failed(e, "Unexpected exception");
        }
    }   // end if((allVariations...
  } // end of run method

// $$$ TO DO $$$
// Replace the following VarXXX() methods with your own.  You should have
// a method for each variation in your testcase.

/**
  Test #1 assumes that the 5219 is setup as a cut-sheet printer.  This test
  creates four pages of text.  10 and 15 CPI are tested as are 4, 6, 8, 9,
  and 12 LPI.
  This test will create a spooled file on the AS/400 in the queue for the
  printer that was specified w/ the -misc command line option.  The user data
  of this spooled file will contain the text "SCS5219".  This test will also
  create a binary file (5219scs.out) on the test client that contains the
  exact printstream that was sent to the host.
  **/
  public void Var001()
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS5219Writer writer = new SCS5219Writer(out, systemObject_.getCcsid(), systemObject_);
        FileOutputStream outFile = new FileOutputStream("5219scs.out", false);
        // need these to get actual printer output on the AS/400.
        PrintParameterList parmList = new PrintParameterList();
        parmList.setParameter(PrintObject.ATTR_USERDATA, "SCS5219");
        OutputQueue outq = new OutputQueue(system_, "/QSYS.LIB/QUSRSYS.LIB/"+printer_+".OUTQ");
        SpooledFileOutputStream spooledFile = new SpooledFileOutputStream(system_,
                                                                            parmList,
                                                                            null,
                                                                            outq);
        // Initialize page settings
        writer.setVerticalFormat(66);
        writer.setCPI(10);
        writer.setLPI(6);

        // Begin document
        writer.setQuality(SCS5219Writer.QUALITY_NEAR_LETTER);
        writer.absoluteHorizontalPosition(16);
        writer.absoluteVerticalPosition(6);
        writer.setCPI(15);
        writer.write("SCS 5219 CLASS: This text is 1in down & 1in left of paper origin.");
        writer.carriageReturn();
        writer.absoluteVerticalPosition(12);
        writer.setCPI(10);
        writer.write("2 inches down and at left margin (column 1) but ...");
        writer.lineFeed();
        writer.write("... continues one line lower.");
        writer.newLine();
        writer.newLine();
        writer.write("A new line at left margin 15 lines (2.5 inches) from top.");
        writer.newLine();
        writer.setCPI(10);
        writer.write("These X's should fill one inch at 10 CPI >XXXXXXXXXX<.");
        writer.newLine();
        writer.absoluteVerticalPosition(20);
        writer.setCPI(15);
        writer.write("Should be 2 HORIZONTAL inches between this X > X");
        writer.relativeHorizontalPosition(30);
        writer.write("X < and this X.");
        writer.setCPI(10);
        writer.newLine();
        writer.newLine();
        writer.write("Should be 2 VERTICAL inches between this X > X");
        writer.carriageReturn();
        writer.absoluteHorizontalPosition(49);
        writer.relativeVerticalPosition(13);
        writer.write("X < and this X.");
        writer.relativeVerticalPosition(4);
        writer.newLine();
        writer.write("Count the 6 LPI:  Line 1.");
        writer.newLine();
        writer.write("Count the 6 LPI:  Line 2.");
        writer.newLine();
        writer.write("Count the 6 LPI:  Line 3.");
        writer.newLine();
        writer.write("Count the 6 LPI:  Line 4.");
        writer.newLine();
        writer.write("Count the 6 LPI:  Line 5.");
        writer.newLine();
        writer.write("Count the 6 LPI:  Line 6.");
        writer.newLine();
        writer.write("Count the 6 LPI:  Line 7.");
        writer.relativeVerticalPosition(4);
        writer.newLine();
        writer.write("The current codepage is - ");
        writer.write(writer.getEncoding());
        writer.endPage();

        //Start of second page
        writer.setVerticalFormat(66);
        writer.write("This line should be at the top left corner of the second page.");
        writer.newLine();
        writer.setCPI(15);
        writer.write("(just below the edge but depends on the printers unprintable borders)");
        writer.newLine();
        writer.write("(If the printer has'em, the top line may be one line lower.)");
        writer.absoluteVerticalPosition(5);
        writer.newLine();
        writer.write("These X's should fill one inch at 15 CPI >XXXXXXXXXXXXXXX<");
        writer.newLine();
        writer.newLine();
        writer.setCPI(10);
        writer.write("The following lines should demonstrate 4 LPI.");
        writer.setLPI(4);
        writer.newLine();
        writer.write("LPI Test Line 1.");
        writer.newLine();
        writer.write("LPI Test Line 2.");
        writer.newLine();
        writer.write("LPI Test Line 3.");
        writer.newLine();
        writer.write("LPI Test Line 4.");
        writer.newLine();
        writer.write("LPI Test Line 5.");
        writer.newLine();
        writer.write("The following lines should demonstrate 8 LPI.");
        writer.setLPI(8);
        writer.newLine();
        writer.write("LPI Test Line 1.");
        writer.newLine();
        writer.write("LPI Test Line 2.");
        writer.newLine();
        writer.write("LPI Test Line 3.");
        writer.newLine();
        writer.write("LPI Test Line 4.");
        writer.newLine();
        writer.write("LPI Test Line 5.");
        writer.newLine();
        writer.write("LPI Test Line 6.");
        writer.newLine();
        writer.write("LPI Test Line 7.");
        writer.newLine();
        writer.write("LPI Test Line 8.");
        writer.newLine();
        writer.write("LPI Test Line 9.");
        writer.newLine();
        writer.setLPI(6);  // don't forget to reset LPI.
        writer.relativeVerticalPosition(39);
        writer.write("This is second to last line above the bottom of the second page.");
        writer.endPage();

        //Start of Third page
        writer.setVerticalFormat(66);
        writer.relativeHorizontalPosition(10);
        writer.write("This line is 1inch right of margin at the top of third page.");
        writer.newLine();
        writer.write("(remember unprintable borders)");
        writer.absoluteVerticalPosition(4);
        writer.carriageReturn();
        writer.write("The following lines should demonstrate 9 LPI.");
        writer.setLPI(9);
        writer.newLine();
        writer.write("LPI Test Line 1.");
        writer.newLine();
        writer.write("LPI Test Line 2.");
        writer.newLine();
        writer.write("LPI Test Line 3.");
        writer.newLine();
        writer.write("LPI Test Line 4.");
        writer.newLine();
        writer.write("LPI Test Line 5.");
        writer.newLine();
        writer.write("LPI Test Line 6.");
        writer.newLine();
        writer.write("LPI Test Line 7.");
        writer.newLine();
        writer.write("LPI Test Line 8.");
        writer.newLine();
        writer.write("LPI Test Line 9.");
        writer.newLine();
        writer.write("LPI Test Line 10.");
        writer.newLine();
        writer.write("LPI Test Line 11.");
        writer.newLine();
        writer.write("LPI Test Line 12.");
        writer.newLine();
        writer.write("LPI Test Line 13.");
        writer.newLine();
        writer.write("LPI Test Line 14.");
        writer.newLine();
        writer.write("LPI Test Line 15.");
        writer.newLine();
        writer.write("LPI Test Line 16.");
        writer.newLine();
        writer.write("LPI Test Line 17.");
        writer.newLine();
        writer.write("LPI Test Line 18.");
        writer.newLine();
        writer.write("LPI Test Line 19.");
        writer.newLine();
        writer.setLPI(6);  // don't forget to reset LPI
        writer.relativeVerticalPosition(47);
        writer.write("This is second to last line above the bottom of the third page.");
        writer.endPage();

        // Start of Fourth page
        writer.setVerticalFormat(66);
        writer.setQuality(SCS5219Writer.QUALITY_DRAFT);
        writer.relativeHorizontalPosition(10);
        writer.write("This line is 1inch right of margin at the top of fourth page.");
        writer.newLine();
        writer.relativeVerticalPosition(15);
        writer.write("This page is 11 inches (66 lines) long & all text on it is in DRAFT mode.");
        writer.newLine();
        writer.write("The following lines should demonstrate 12 LPI.");
        writer.setLPI(12);
        writer.newLine();
        writer.write("LPI Test Line 1.");
        writer.newLine();
        writer.write("LPI Test Line 2.");
        writer.newLine();
        writer.write("LPI Test Line 3.");
        writer.newLine();
        writer.write("LPI Test Line 4.");
        writer.newLine();
        writer.write("LPI Test Line 5.");
        writer.newLine();
        writer.write("LPI Test Line 6.");
        writer.newLine();
        writer.write("LPI Test Line 7.");
        writer.newLine();
        writer.write("LPI Test Line 8.");
        writer.newLine();
        writer.write("LPI Test Line 9.");
        writer.newLine();
        writer.write("LPI Test Line 10.");
        writer.newLine();
        writer.write("LPI Test Line 11.");
        writer.newLine();
        writer.write("LPI Test Line 12.");
        writer.newLine();
        writer.write("LPI Test Line 13.");
        writer.newLine();
        writer.write("LPI Test Line 14.");
        writer.newLine();
        writer.write("LPI Test Line 15.");
        writer.newLine();
        writer.write("LPI Test Line 16.");
        writer.newLine();
        writer.write("LPI Test Line 17.");
        writer.newLine();
        writer.write("LPI Test Line 18.");
        writer.newLine();
        writer.write("LPI Test Line 19.");
        writer.newLine();
        writer.write("LPI Test Line 20.");
        writer.newLine();
        writer.write("LPI Test Line 21.");
        writer.newLine();
        writer.write("LPI Test Line 22.");
        writer.newLine();
        writer.write("LPI Test Line 23.");
        writer.newLine();
        writer.write("LPI Test Line 24.");
        writer.newLine();
        writer.write("LPI Test Line 25.");
        writer.newLine();
        writer.setLPI(6);  // don't forget to reset LPI.
        //We have used up 5.125 inches so we have 5.875 left.  At 6 LPI that's
        // 35 lines.
        writer.relativeVerticalPosition(33);
        writer.setUnderline(true);
        writer.write("This underlined line is second to last line before bottom of fourth page.");
        writer.endPage();

        // If the 5219 is using continuous forms, we should add another page that leaves the
        // vertical format set at 66 in order to maintain vertical page alignment.

        writer.close();
        out.writeTo(outFile);
        out.writeTo(spooledFile);
        outFile.close();
        spooledFile.close();
        succeeded();
    }
    catch (IOException IOe)
    {
        failed(IOe, "Unexpected IO exception");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception.");
    }
  }

}



