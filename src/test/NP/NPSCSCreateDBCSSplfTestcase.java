///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSCSCreateDBCSSplfTestcase.java
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
 Testcase NPSCSCreateDBCSSplfTestcase.
 **/
public class NPSCSCreateDBCSSplfTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSCSCreateDBCSSplfTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSCSCreateDBCSSplfTestcase(AS400            systemObject,
				 Vector           variationsToRun,
				 int              runMode,
				 FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPSCSCreateDBCSSplfTestcase", 1,
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
          double positionArray[] = {2.0, 3.0};
          // Create a spooled file to send
          SpooledFileOutputStream out = new SpooledFileOutputStream(systemObject_,
                                                                    null, null, null);
          SCS5553Writer wtr = new SCS5553Writer(out, systemObject_.getCcsid(), systemObject_);

          // Write the contents of the spool file.
          //wtr.setLeftMargin(1.0);   // this method was eliminated from this class
          wtr.absoluteVerticalPosition(6);
          wtr.write("This document was created using the AS/400 Java Toolbox.");
          wtr.newLine();
          wtr.write("The rest of this document shows some of the things that");
          wtr.newLine();
          wtr.write("can be done with the SCS5553Writer class.");
          wtr.newLine();
          wtr.newLine();
          wtr.setFontScaling(wtr.SCALE_DOUBLE_HORIZONTAL); 
          wtr.write("This line should be in double horizontal scale.");
          wtr.newLine();
          wtr.setFontScaling(wtr.SCALE_REGULAR ); 
          wtr.write("This line should be in regular scale.");
          wtr.newLine();
          wtr.newLine();
          wtr.write("The following grid lines should form a 1 inch square box.");
          wtr.printHorizontalGridLine(2.0, 3.0);
          wtr.startVerticalGridLines(positionArray);
          wtr.newLine();
          wtr.newLine();
          wtr.newLine();
          wtr.newLine();
          wtr.newLine();
          wtr.newLine();
          wtr.printHorizontalGridLine(2.0, 3.0);
          wtr.stopVerticalGridLines();
          wtr.endPage();
          wtr.setCharacterRotation(wtr.CHARACTER_ROTATED); 
          wtr.write("This page should be rotated."); 
          wtr.newLine();
          wtr.endPage();
          wtr.setCharacterRotation(wtr.CHARACTER_NORMAL);
          wtr.write("The page should be normal rotation.");
          wtr.endPage();
          wtr.close();

	    succeeded();  // Note: This variation will be successful.
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}

    } // end Var001


} // end NPSCSCreateDBCSSplfTestcase class


