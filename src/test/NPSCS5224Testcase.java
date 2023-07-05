///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSCS5224Testcase.java
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
  Testcase NPSCS5224Testcase.
  Attended test for SCS5256Writer class. This testcase creates three
  spooled files that must be printed on a 5256 (or better) continuous-
  forms type printer.  The output is self-explanatory as to the
  results of the test.  Note: It helps to vertically align the paper
  in the printer before printing this testcase.
  **/
public class NPSCS5224Testcase extends Testcase
{
  // the printer device name
  String printer_ = null;
  AS400  system_  = null;

/**
  Constructor.  This is called from NPPrintTest::createTestcases().
  **/
  public NPSCS5224Testcase(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String           printer)
      throws IOException
  {
    // $$$ TO DO $$$
    // Replace the third parameter with the total number of variations
    // in this testcase.
    super(systemObject, "NPSCS5224Testcase", 1,
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
  Test #1 creates five pages of text.  Two of the pages will be mis-aligned
  vertically but the last page should end up w/ perfect vertical alignment.
  10 and 15 CPI are tested as are 4, 6, 8, 9, and 12 LPI.
  This test will create a spooled file on the AS/400 in the queue for the
  printer that was specified w/ the -misc command line option.  The user data
  of this spooled file will contain the text "SCS5224".  This test will also
  create a binary file (5224scs.out) on the test client that contains the
  exact printstream that was sent to the host.
  **/
  public void Var001()
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
        SCS5224Writer writer = new SCS5224Writer(out, systemObject_.getCcsid(), systemObject_);
        FileOutputStream outFile = new FileOutputStream("5224scs.out", false);
        // need these to get actual printer output on the AS/400.
        PrintParameterList parmList = new PrintParameterList();
        parmList.setParameter(PrintObject.ATTR_USERDATA, "SCS5224");
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
        writer.absoluteHorizontalPosition(11);
        writer.absoluteVerticalPosition(6);
        writer.write("SCS 5224 CLASS: This text is 1in down & 1in left of paper origin.");
        writer.absoluteHorizontalPosition(71);
        writer.write("This text is 1 inch down and 7 inches left of paper origin.");
        writer.carriageReturn();
        writer.absoluteVerticalPosition(11);
        writer.write("2 inches down and at left margin (column 1) but ...");
        writer.lineFeed();
        writer.write("... continues one line lower.");
        writer.newLine();
        writer.newLine();
        writer.write("A new line at left margin 15 lines (2.5 inches) from top.");
        writer.newLine();
        writer.write("These X's should fill one inch at 10 CPI >XXXXXXXXXX<.");
        writer.newLine();
        writer.absoluteVerticalPosition(20);
        writer.write("Should be 2 HORIZONTAL inches between this X > X");
        writer.relativeHorizontalPosition(20);
        writer.write("X < and this X.");
        writer.newLine();
        writer.newLine();
        writer.write("Should be 2 VERTICAL inches between this X > X");
        writer.carriageReturn();
        writer.absoluteHorizontalPosition(46);
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
        writer.setVerticalFormat(33);  // This sets the page length to 5.5 inches
        writer.write("This line should be at the top left corner of the second page.");
        writer.newLine();
        writer.write("(just below the perforation)");
        writer.newLine();
        writer.absoluteVerticalPosition(4);
        writer.write("This page will be just 5.5 inches (33 lines) long.");
        writer.newLine();
        writer.newLine();
        writer.setCPI(15);
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
        writer.setLPI(6);  // don't forget to reset LPI
        // We changed the LPI so we have to recalculate how many lines we have left on the page.
        // We printed 8 lines at 6 LPI = 1.33 inches.  6 lines at 4 LPI = 1.5.
        // 9 lines at 8 LPI = 1.125.  Total space = 1.33 +1.5 +1.125= 3.95 inches
        // That means we have 1.55 inches left at 6 LPI = 9.3 lines.
        writer.setVerticalFormat(9);
        writer.carriageReturn();
        // Move to the bottom but don't use AVPP because it doesn't recognize the changed SVF.
        // This is an undocumented feature...
        writer.relativeVerticalPosition(8);
        writer.write("This line is at the bottom of the second page.");
        writer.endPage();

        //Start of Third logical page which is on second physical page
        writer.setVerticalFormat(33);  // Reset the page length back to 5.5 inches
        writer.relativeHorizontalPosition(11);
        writer.write("This line is 1inch right of margin at the top of third (short) page.");
        writer.newLine();
        writer.absoluteVerticalPosition(4);
        writer.write("This page is also just 5.5 inches (33 lines) long.");
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
        writer.setLPI(6);  // don't forget to reset LPI.
        // We have used 2.875 inches.  Reset Vertical Format to make it easier
        // to move to the bottom
        // 5.5 - 2.875 = 2.625 * 6LPI = 15.75 lines = 16.
        writer.setVerticalFormat(16);
        writer.carriageReturn();
        writer.relativeVerticalPosition(15);
        writer.write("This line is at bottom of third logical page which is bottom of second physical page.");
        writer.endPage();

        // Start of Fourth Logical page which is on Third physical page.
        writer.setVerticalFormat(66);
        writer.relativeHorizontalPosition(10);
        writer.write("This line is 1inch right of margin at the top of third physical page.");
        writer.newLine();
        writer.relativeVerticalPosition(15);
        writer.write("This page is 11 inches (66 lines) long.");
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
        // We have used 5 inches so we have 6 left.  Reset Vertical Format
        // to make it easier to get to the bottom. 6*6LPI = 36 lines
        writer.setVerticalFormat(36);
        writer.carriageReturn();
        writer.relativeVerticalPosition(35);
        writer.write("This line is at bottom of fourth logical page which is bottom of third physical page.");
        writer.endPage();

        writer.setVerticalFormat(66);  //This is necessary only for continuous forms printers
        writer.write("This page intentionally left blank so that vertical page alignment");
        writer.newLine();
        writer.write("is not messed up for the next document.");
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

