//////////////////////////////////////////////////////////////////////
 //
 ///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSCS3812Testcase.java
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
import com.ibm.as400.access.*;

/**
  Testcase NPSCS3812Testcase.
  Attended test for SCS5256Writer class. This testcase creates three
  spooled files that must be printed on a 5256 (or better) continuous-
  forms type printer.  The output is self-explanatory as to the
  results of the test.
  **/
public class NPSCS3812Testcase extends Testcase
{
  // the printer device name
  String printer_ = null;
  AS400  system_  = null;
/**
  Constructor.  This is called from NPPrintTest::createTestcases().
  **/
  public NPSCS3812Testcase(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String           printer)
      throws IOException
  {
    // $$$ TO DO $$$
    // Replace the third parameter with the total number of variations
    // in this testcase.
    super(systemObject, "NPSCS3812Testcase", 1,
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
  Test #1 creates four pages of text. It will print out text using all of the
  fonts that are supported by the 3812.  The first two pages will be duplexed,
  the second two pages are tumble duplexed.  The third page has text of
  various orientations on it. 10 and 15 CPI are tested as are 4, 6, 8, 9, and
  12 LPI.
  This test will create a spooled file on the AS/400 in the queue for the
  printer that was specified w/ the -misc command line option.  The user data
  of this spooled file will contain the text "SCS3812".  This test will also
  create a binary file (3812scs.out) on the test client that contains the
  exact printstream that was sent to the host.
  **/
  public void Var001()
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS3812Writer writer = new SCS3812Writer(out, systemObject_.getCcsid(), systemObject_);
        FileOutputStream outFile = new FileOutputStream("3812scs.out", false);
        // need these to get actual printer output on the AS/400.
        PrintParameterList parmList = new PrintParameterList();
        parmList.setParameter(PrintObject.ATTR_USERDATA, "SCS3812");
        OutputQueue outq = new OutputQueue(system_, "/QSYS.LIB/QUSRSYS.LIB/"+printer_+".OUTQ");
        SpooledFileOutputStream spooledFile = new SpooledFileOutputStream(system_,
                                                                            parmList,
                                                                            null,
                                                                            outq);
        // Initialize page settings
        writer.setDuplex(SCS3812Writer.DUPLEX_DUPLEX);
        writer.setCPI(10);
        writer.setLPI(6);

        // Begin document
        writer.absoluteHorizontalPosition(11);
        writer.write("SCS 3812 CLASS:  This is the top of the first page.");
        writer.newLine();
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_COURIER_10);
        writer.write("This is FONT_COURIER_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_COURIER_12);
        writer.write("This is FONT_COURIER_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_COURIER_15);
        writer.write("This is FONT_COURIER_15.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_COURIER_17);
        writer.write("This is FONT_COURIER_17.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_COURIER_5);
        writer.write("This is FONT_COURIER_5.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_COURIER_BOLD_10);
        writer.write("This is FONT_COURIER_BOLD_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_COURIER_BOLD_17);
        writer.write("This is FONT_COURIER_BOLD_17.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_COURIER_BOLD_5);
        writer.write("This is FONT_COURIER_BOLD_5.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_COURIER_ITALIC_10);
        writer.write("This is FONT_COURIER_ITALIC_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_COURIER_ITALIC_12);
        writer.write("This is FONT_COURIER_ITALIC_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_GOTHIC_10);
        writer.write("This is FONT_GOTHIC_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_GOTHIC_12);
        writer.write("This is FONT_GOTHIC_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_GOTHIC_13);
        writer.write("This is FONT_GOTHIC_13.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_GOTHIC_15);
        writer.write("This is FONT_GOTHIC_15.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_GOTHIC_20);
        writer.write("This is FONT_GOTHIC_20.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_GOTHIC_27);
        writer.write("This is FONT_GOTHIC_27.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_GOTHIC_BOLD_10);
        writer.write("This is FONT_GOTHIC_BOLD_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_GOTHIC_BOLD_12);
        writer.write("This is FONT_GOTHIC_BOLD_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_GOTHIC_ITALIC_12);
        writer.write("This is FONT_GOTHIC_ITALIC_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_LETTER_GOTHIC_12);
        writer.write("This is FONT_LETTER_GOTHIC_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_LETTER_GOTHIC_BOLD_12);
        writer.write("This is FONT_LETTER_GOTHIC_BOLD_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_OCR_A_10);
        writer.write("This is FONT_OCR_A_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_OCR_B_10);
        writer.write("This is FONT_OCR_B_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_ORATOR_10);
        writer.write("This is FONT_ORATOR_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_ORATOR_BOLD_10);
        writer.write("This is FONT_ORATOR_BOLD_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_PRESTIGE_10);
        writer.write("This is FONT_PRESTIGE_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_PRESTIGE_12);
        writer.write("This is FONT_PRESTIGE_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_PRESTIGE_15);
        writer.write("This is FONT_PRESTIGE_15.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_PRESTIGE_BOLD_12);
        writer.write("This is FONT_PRESTIGE_BOLD_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_PRESTIGE_ITALIC_12);
        writer.write("This is FONT_PRESTIGE_ITALIC_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_ROMAN_10);
        writer.write("This is FONT_ROMAN_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_SCRIPT_12);
        writer.write("This is FONT_SCRIPT_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_SERIF_10);
        writer.write("This is FONT_SERIF_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_SERIF_12);
        writer.write("This is FONT_SERIF_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_SERIF_15);
        writer.write("This is FONT_SERIF_15.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_SERIF_BOLD_12);
        writer.write("This is FONT_SERIF_BOLD_12.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_SERIF_ITALIC_10);
        writer.write("This is FONT_SERIF_ITALIC_10.");
        writer.newLine();
        writer.setFont(SCS3812Writer.FONT_SERIF_ITALIC_12);
        writer.write("This is FONT_SERIF_ITALIC_12.");
        writer.newLine();
        writer.absoluteVerticalPosition(64);
        writer.setFont(SCS3812Writer.FONT_SERIF_12);
        writer.write("This is the bottom of the first page.");
        writer.endPage();

        //Start of second page
        writer.setTextOrientation(180);
        writer.absoluteHorizontalPosition(11);
        writer.write("This is the top of the second page. It's duplexed and rotated 180 degrees");
        writer.newLine();
        writer.newLine();
        writer.setCPI(15);
        writer.write("These X's should fill one inch at 15 CPI >XXXXXXXXXXXXXXX<");
        writer.newLine();
        writer.newLine();
        writer.setCPI(12);
        writer.write("The following lines should demonstrate 4 LPI (12CPI).");
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
        writer.write("The following lines should be ");
        writer.setBold(true);
        writer.write("BOLD");
        writer.setBold(false);
        writer.write("at 8 LPI.");
        writer.setLPI(8);
        writer.setBold(true);
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
        writer.setBold(false);
        writer.setLPI(6);  // don't forget to reset LPI
        writer.relativeVerticalPosition(42);
        writer.write("This line is the bottom of the second page.");
        writer.endPage();

        //Start of Third page.  This page will be
        writer.setDuplex(SCS3812Writer.DUPLEX_TUMBLE);
        writer.setTextOrientation(90);
        writer.absoluteHorizontalPosition(11);
        writer.write("This is the top of the Third page that is rotated 90 degrees. ");
        writer.newLine();
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
        writer.absoluteVerticalPosition(48);
        writer.write("This line is at bottom of third page.");
        writer.endPage();

        // Start of Fourth page
        writer.setTextOrientation(0);
        writer.relativeHorizontalPosition(11);
        writer.write("This is the top of the fourth page and it is TUMBLE duplexed!");
        writer.newLine();
        writer.relativeVerticalPosition(15);
        writer.write("This page is oriented the same as the second page.");
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
        writer.setLPI(6);  // don't forget to reset LPI
        writer.absoluteVerticalPosition(64);
        writer.setUnderline(true);
        writer.write("This underlined line is at bottom of fourth physical page.");
        writer.endPage();

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


