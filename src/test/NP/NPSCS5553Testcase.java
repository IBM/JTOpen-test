//////////////////////////////////////////////////////////////////////
 //
 ///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSCS5553Testcase.java
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
  Testcase NPSCS5553Testcase.
  Attended test for SCS5256Writer class. This testcase creates three
  spooled files that must be printed on a 5256 (or better) continuous-
  forms type printer.  The output is self-explanatory as to the
  results of the test.  Note: It helps to vertically align the paper
  in the printer before printing this testcase.
  **/
public class NPSCS5553Testcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSCS5553Testcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
  // the printer device name
  String printer_ = null;
  AS400  system_  = null;

/**
  Constructor.  This is called from NPPrintTest::createTestcases().
  **/
  public NPSCS5553Testcase(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String           printer)
      throws IOException
  {
    // $$$ TO DO $$$
    // Replace the third parameter with the total number of variations
    // in this testcase.
    super(systemObject, "NPSCS5553Testcase", 1,
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
  Test #1 creates four pages of text.  Two of the pages will be mis-aligned
  vertically but the last page should end up w/ perfect vertical alignment.
  10 and 15 CPI are tested as are 4, 6, 8, 9, and 12 LPI.  This test does NOT
  output any DBCS characters.

  This test will create a spooled file on the AS/400 in the queue for the
  printer that was specified w/ the -misc command line option.  The user data
  of this spooled file will contain the text "SCS5553".  This test will also
  create a binary file (5553scs.out) on the test client that contains the
  exact printstream that was sent to the host.
  **/
  public void Var001()
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
 //   byte [] DBCSBytes = {(byte)0x95, 0x57, (byte)0x99, (byte)0xc3, (byte)0x96,
 //                               (byte)0xbe, (byte)0x92, (byte)0xa9, 0x00, 0x00};
 //   String DbcsData = new String(DBCSBytes);

    try
    {
        SCS5553Writer writer = new SCS5553Writer(out, systemObject_.getCcsid(), systemObject_);
        FileOutputStream outFile = new FileOutputStream("5553scs.out", false);
        // need these to get actual printer output on the AS/400.
        PrintParameterList parmList = new PrintParameterList();
        parmList.setParameter(PrintObject.ATTR_USERDATA, "SCS5553");
        OutputQueue outq = new OutputQueue(system_, "/QSYS.LIB/QUSRSYS.LIB/"+printer_+".OUTQ");
        SpooledFileOutputStream spooledFile = new SpooledFileOutputStream(system_,
                                                                            parmList,
                                                                            null,
                                                                            outq);
        writer.setVerticalFormat(66);
        writer.absoluteHorizontalPosition(11);
        writer.absoluteVerticalPosition(6);
        writer.write("SCS 5553 CLASS: THIS TEXT IS 1IN DOWN & 1IN LEFT OF PAPER ORIGIN.");
        writer.absoluteHorizontalPosition(71);
        writer.write("THIS TEXT IS 1 INCH DOWN AND 7 INCHES LEFT OF PAPER ORIGIN.");
        writer.carriageReturn();
        writer.absoluteVerticalPosition(11);
        writer.write("2 INCHES DOWN AND AT LEFT MARGIN (COLUMN 1) BUT ...");
        writer.lineFeed();
        writer.write("... CONTINUES ONE LINE LOWER.");
        writer.newLine();
        writer.newLine();
        writer.write("A NEW LINE AT LEFT MARGIN 15 LINES (2.5 INCHES) FROM TOP.");
        writer.newLine();
        writer.write("THESE X'S SHOULD FILL ONE INCH AT 10 CPI >XXXXXXXXXX<.");
        writer.newLine();
        writer.absoluteVerticalPosition(20);
        writer.write("SHOULD BE 2 HORIZONTAL INCHES BETWEEN THIS X > X");
        writer.relativeHorizontalPosition(20);
        writer.write("X < AND THIS X.");
        writer.newLine();
        writer.newLine();
        writer.write("SHOULD BE 2 VERTICAL INCHES BETWEEN THIS X > X");
        writer.carriageReturn();
        writer.absoluteHorizontalPosition(48);
        writer.relativeVerticalPosition(13);
        writer.write("X < AND THIS X.");
        writer.relativeVerticalPosition(4);
        writer.newLine();
        writer.write("COUNT THE 6 LPI:  LINE 1.");
        writer.newLine();
        writer.write("COUNT THE 6 LPI:  LINE 2.");
        writer.newLine();
        writer.write("COUNT THE 6 LPI:  LINE 3.");
        writer.newLine();
        writer.write("COUNT THE 6 LPI:  LINE 4.");
        writer.newLine();
        writer.write("COUNT THE 6 LPI:  LINE 5.");
        writer.newLine();
        writer.write("COUNT THE 6 LPI:  LINE 6.");
        writer.newLine();
        writer.write("COUNT THE 6 LPI:  LINE 7.");
        writer.relativeVerticalPosition(4);
        writer.newLine();
        writer.write("THE CURRENT CODEPAGE IS - ");
        writer.write(writer.getEncoding());
        writer.newLine();
        writer.write("DBCS DATA: ");
   //     writer.write(DbcsData);
        writer.endPage();

        //Start of second page
        writer.setVerticalFormat(33);  // This sets the page length to 5.5 inches
        writer.write("THIS LINE SHOULD BE AT THE TOP LEFT CORNER OF THE SECOND PAGE.");
        writer.newLine();
        writer.write("(JUST BELOW THE PERFORATION)");
        writer.newLine();
        writer.absoluteVerticalPosition(4);
        writer.write("THIS PAGE WILL BE JUST 5.5 INCHES (33 LINES) LONG.");
        writer.newLine();
        writer.newLine();
        writer.setCPI(15);
        writer.write("THESE X's SHOULD FILL ONE INCH AT 15 CPI >XXXXXXXXXXXXXXX<");
        writer.newLine();
        writer.newLine();
        writer.setCPI(10);
        writer.write("THE FOLLOWING LINES SHOULD DEMONSTRATE 4 LPI.");
        writer.setLPI(4);
        writer.newLine();
        writer.write("LPI TEST LINE 1.");
        writer.newLine();
        writer.write("LPI TEST LINE 2.");
        writer.newLine();
        writer.write("LPI TEST LINE 3.");
        writer.newLine();
        writer.write("LPI TEST LINE 4.");
        writer.newLine();
        writer.write("LPI TEST LINE 5.");
        writer.newLine();
        writer.write("THE FOLLOWING LINES SHOULD DEMONSTRATE 8 LPI.");
        writer.setLPI(8);
        writer.newLine();
        writer.write("LPI TEST LINE 1.");
        writer.newLine();
        writer.write("LPI TEST LINE 2.");
        writer.newLine();
        writer.write("LPI TEST LINE 3.");
        writer.newLine();
        writer.write("LPI TEST LINE 4.");
        writer.newLine();
        writer.write("LPI TEST LINE 5.");
        writer.newLine();
        writer.write("LPI TEST LINE 6.");
        writer.newLine();
        writer.write("LPI TEST LINE 7.");
        writer.newLine();
        writer.write("LPI TEST LINE 8.");
        writer.newLine();
        writer.write("LPI TEST LINE 9.");
        writer.newLine();
        writer.setLPI(6);  // don't forget to reset LPI
        writer.newLine();
        writer.write("DBCS DATA: ");
  //      writer.write(dbcsData);
        // We changed the LPI so we have to recalculate how many lines we have left on the page.
        // We printed 8 lines at 6 LPI = 1.33 inches.  6 lines at 4 LPI = 1.5.
        // 10 lines at 8 LPI = 1.25.  Total space = 1.33 +1.5 +1.25= 4.075 inches
        // That means we have 1.425 inches left at 6 LPI = 8.55 lines.
        writer.setVerticalFormat(8);
        writer.relativeVerticalPosition(7);
        writer.write("THIS LINE IS AT THE BOTTOM OF THE SECOND PAGE.");
        writer.endPage();

        //Start of Third logical page which is on second physical page
        writer.setVerticalFormat(33);
        writer.relativeHorizontalPosition(10);
        writer.write("THIS LINE IS 1INCH RIGHT OF MARGIN AT THE TOP OF THIRD (SHORT) PAGE.");
        writer.newLine();
        writer.absoluteVerticalPosition(4);
        writer.write("THIS PAGE IS ALSO JUST 5.5 INCHES (33 LINES) LONG.");
        writer.newLine();
        writer.write("THE FOLLOWING LINES SHOULD DEMONSTRATE 9 LPI.");
        writer.setLPI(9);
        writer.newLine();
        writer.write("LPI TEST LINE 1.");
        writer.newLine();
        writer.write("LPI TEST LINE 2.");
        writer.newLine();
        writer.write("LPI TEST LINE 3.");
        writer.newLine();
        writer.write("LPI TEST LINE 4.");
        writer.newLine();
        writer.write("LPI TEST LINE 5.");
        writer.newLine();
        writer.write("LPI TEST LINE 6.");
        writer.newLine();
        writer.write("LPI TEST LINE 7.");
        writer.newLine();
        writer.write("LPI TEST LINE 8.");
        writer.newLine();
        writer.write("LPI TEST LINE 9.");
        writer.newLine();
        writer.write("LPI TEST LINE 10.");
        writer.newLine();
        writer.write("LPI TEST LINE 11.");
        writer.newLine();
        writer.write("LPI TEST LINE 12.");
        writer.newLine();
        writer.write("LPI TEST LINE 13.");
        writer.newLine();
        writer.write("LPI TEST LINE 14.");
        writer.newLine();
        writer.write("LPI TEST LINE 15.");
        writer.newLine();
        writer.write("LPI TEST LINE 16.");
        writer.newLine();
        writer.write("LPI TEST LINE 17.");
        writer.newLine();
        writer.write("LPI TEST LINE 18.");
        writer.newLine();
        writer.write("LPI TEST LINE 19.");
        writer.newLine();
        writer.setLPI(6);  // don't forget to reset LPI.
        // We have used 1.875 inches.  Reset Vertical Format to make it easier
        // to move to the bottom
        // 5.5 - 2.875 = 3.625 * 6LPI = 15.75 lines = 16.
        writer.setVerticalFormat(16);
        writer.relativeVerticalPosition(15);
        writer.write("THIS LINE IS AT BOTTOM OF THIRD LOGICAL PAGE WHICH IS BOTTOM OF SECOND PHYSICAL PAGE.");
        writer.endPage();

        // Start of Fourth Logical page which is on Third physical page.
        writer.setVerticalFormat(66);
        writer.relativeHorizontalPosition(10);
        writer.write("THIS LINE IS 1INCH RIGHT OF MARGIN AT THE TOP OF THIRD PHYSICAL PAGE.");
        writer.newLine();
        writer.relativeVerticalPosition(15);
        writer.write("THIS PAGE IS 11 INCHES (66 LINES) LONG.");
        writer.newLine();
        writer.write("THE FOLLOWING LINES SHOULD DEMONSTRATE 12 LPI.");
        writer.setLPI(12);
        writer.newLine();
        writer.write("LPI TEST LINE 1.");
        writer.newLine();
        writer.write("LPI TEST LINE 2.");
        writer.newLine();
        writer.write("LPI TEST LINE 3.");
        writer.newLine();
        writer.write("LPI TEST LINE 4.");
        writer.newLine();
        writer.write("LPI TEST LINE 5.");
        writer.newLine();
        writer.write("LPI TEST LINE 6.");
        writer.newLine();
        writer.write("LPI TEST LINE 7.");
        writer.newLine();
        writer.write("LPI TEST LINE 8.");
        writer.newLine();
        writer.write("LPI TEST LINE 9.");
        writer.newLine();
        writer.write("LPI TEST LINE 10.");
        writer.newLine();
        writer.write("LPI TEST LINE 11.");
        writer.newLine();
        writer.write("LPI TEST LINE 12.");
        writer.newLine();
        writer.write("LPI TEST LINE 13.");
        writer.newLine();
        writer.write("LPI TEST LINE 14.");
        writer.newLine();
        writer.write("LPI TEST LINE 15.");
        writer.newLine();
        writer.write("LPI TEST LINE 16.");
        writer.newLine();
        writer.write("LPI TEST LINE 17.");
        writer.newLine();
        writer.write("LPI TEST LINE 18.");
        writer.newLine();
        writer.write("LPI TEST LINE 19.");
        writer.newLine();
        writer.write("LPI TEST LINE 20.");
        writer.newLine();
        writer.write("LPI TEST LINE 21.");
        writer.newLine();
        writer.write("LPI TEST LINE 22.");
        writer.newLine();
        writer.write("LPI TEST LINE 23.");
        writer.newLine();
        writer.write("LPI TEST LINE 24.");
        writer.newLine();
        writer.write("LPI TEST LINE 25.");
        writer.newLine();
        writer.setLPI(6);  // don't forget to reset LPI so the FormFeed works right.
        writer.newLine();
        writer.write("DBCS DATA: ");
  //      writer.write(dbcsData);
        // We have used 5.3125 inches so we have 5.6875 left.  Reset Vertical Format
        // to make it easier to get to the bottom. 5.6875*6LPI = 34.125 lines
        writer.setVerticalFormat(35);
        writer.relativeVerticalPosition(34);
        writer.write("THIS LINE IS AT BOTTOM OF FOURTH LOGICAL PAGE WHICH IS BOTTOM OF THIRD PHYSICAL PAGE.");
        writer.endPage();

        writer.setVerticalFormat(66);
        writer.write("THIS PAGE INTENTIONALLY LEFT BLANK SO THAT VERTICAL PAGE ALIGNMENT");
        writer.newLine();
        writer.write("IS NOT MESSED UP FOR THE NEXT DOCUMENT.");
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


