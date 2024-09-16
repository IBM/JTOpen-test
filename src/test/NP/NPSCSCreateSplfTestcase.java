///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSCSCreateSplfTestcase.java
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
import java.io.IOException;

import java.util.Vector;
import java.util.Enumeration;
import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase NPSCSCreateSplfTestcase.
 **/
public class NPSCSCreateSplfTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSCSCreateSplfTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSCSCreateSplfTestcase(AS400            systemObject,
				 Vector           variationsToRun,
				 int              runMode,
				 FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPSCSCreateSplfTestcase", 1,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
	boolean allVariations = (variationsToRun_.size() == 0);

	try
	{
	    // run variation 1
	    if ((allVariations || variationsToRun_.contains("1")) &&
		runMode_ != UNATTENDED) // Note: This is an attended variation.
	    {
		setVariation(1);
		Var001();
	    }


	    // $$$ TO DO $$$
	    // Add an 'if' block using the following as a template for each variation.
	    // Make sure you correctly set the variation number
	    // and runmode in the 'if' condition, and the
	    // variation number in the setVariation call.
	    //
	    // replace the X with the variation number you are adding
	    //
/* $$$ TO DO $$$ - delete this line
	    if ((allVariations || variationsToRun_.contains("X")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(X);
		VarXXX();
	    }
$$$ TO DO $$$ - delete this line */

	} // end try block

	catch( Exception e )
	{
	    failed(e, "Unexpected exception");
	}

    } // end run method

    /**
     Tests sending a SpooledFile with good required attributes.
     **/
    public void Var001()
    {
	try
	{
          // Create a spooled file to send
          SpooledFileOutputStream out = new SpooledFileOutputStream(systemObject_,
                                                                    null, null, null);
          SCS3812Writer wtr = new SCS3812Writer(out, systemObject_.getCcsid(), systemObject_);

          // Write the contents of the spool file.
          wtr.setLeftMargin(1.0);
          wtr.absoluteVerticalPosition(6);
          wtr.setFont(wtr.FONT_COURIER_BOLD_5);
          wtr.write("          Java Printing");
          wtr.newLine();
          wtr.newLine();
          wtr.setCPI(10);
          wtr.write("This document was created using the AS/400 Java Toolbox.");
          wtr.newLine();
          wtr.write("The rest of this document shows some of the things that");
          wtr.newLine();
          wtr.write("can be done with the SCS3812Writer class.");
          wtr.newLine();
          wtr.newLine();
          wtr.setUnderline(true); wtr.write("Setting fonts:"); wtr.setUnderline(false);
          wtr.newLine();
          wtr.setFont(wtr.FONT_COURIER_10); wtr.write("Courier font ");
          wtr.setFont(wtr.FONT_COURIER_BOLD_10); wtr.write(" Courier bold font ");
          wtr.setFont(wtr.FONT_COURIER_ITALIC_10); wtr.write(" Courier italic font ");
          wtr.newLine();
          wtr.setBold(true); wtr.write("Courier bold italic font ");
          wtr.setBold(false);
          wtr.setCPI(10);
          wtr.newLine();
          wtr.newLine();
          wtr.setUnderline(true); wtr.write("Lines per inch:"); wtr.setUnderline(false);
          wtr.newLine();
          wtr.write("The following lines should print at 8 lines per inch.");
          wtr.newLine();
          wtr.newLine();
          wtr.setLPI(8);
          wtr.write("Line one"); wtr.newLine();
          wtr.write("Line two"); wtr.newLine();
          wtr.write("Line three"); wtr.newLine();
          wtr.write("Line four"); wtr.newLine();
          wtr.write("Line five"); wtr.newLine();
          wtr.write("Line six"); wtr.newLine();
          wtr.write("Line seven"); wtr.newLine();
          wtr.write("Line eight"); wtr.newLine();
          wtr.endPage();
          wtr.setLPI(6);
          wtr.setSourceDrawer(1);
          wtr.setTextOrientation(0);
          wtr.absoluteVerticalPosition(6);
          wtr.write("This page should print in portrait orientation from drawer 1.");
          wtr.endPage();
          wtr.setSourceDrawer(2);
          wtr.setTextOrientation(90);
          wtr.absoluteVerticalPosition(6);
          wtr.write("This page should print in landscape orientation from drawer 2.");
          wtr.endPage();
          wtr.close();

	    succeeded();  // Note: This variation will be successful.
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}

    } // end Var001


} // end NPSCSCreateSplfTestcase class


