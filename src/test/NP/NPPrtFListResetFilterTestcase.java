///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtFListResetFilterTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.PrinterFileList;

import test.Testcase;

/**
 Testcase NPPrtFListResetFilterTestcase.
 **/
public class NPPrtFListResetFilterTestcase extends Testcase

{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtFListResetFilterTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtFListResetFilterTestcase(AS400            systemObject,
                                         Vector<String> variationsToRun,
                                         int              runMode,
                                         FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPPrtFListResetFilterTestcase", 2,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtFListResetFilterTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
	{
            // create JAVAPRINT printer file
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("QSYS/CRTPRTF FILE(NPJAVA/JAVAPRINT)") == false)
                {
                failed("Could not create a printer file. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            // run variation 1
            if ((allVariations || variationsToRun_.contains("1")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(1);
                Var001();
                }

            // run variation 2
            if ((allVariations || variationsToRun_.contains("2")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(2);
                Var002();
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
            // run variation X
            if ((allVariations || variationsToRun_.contains("X")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(X);
                VarXXX();
                }
$$$ TO DO $$$ - delete this line */

            // delete the printer file that we created
            if (cmd.run("QSYS/DLTF FILE(NPJAVA/JAVAPRINT)") == false)
                {
                output_.println("Could not delete the printer file we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }
            } // end try block

	catch( Exception e )
	{
	    failed(e, "Unexpected exception");
	}

    } // end run method

    /**
     * Tests resetting the filter on a Printer File List.
     **/
    public void Var001()
    {
        try
            {
            // create a PrinterFile list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // set the printer file filter 
            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            prtFList.openSynchronously();

            // get the number of listed printer files
            int prevNum = prtFList.size();

            // close the list
            prtFList.close();

            // reset the PrinterFile filter
            prtFList.resetFilter();

            // set the PrinterFile filter to receive all printer file
            prtFList.setPrinterFileFilter("/QSYS.LIB/%ALL%.LIB/%ALL%.FILE");
            
            // list the printers
            prtFList.openSynchronously();

            // get the number of listed printers
            int postNum = prtFList.size();

            // verify that we received more printer files the second time
            if (postNum > prevNum)
                {
                succeeded();
                }
            else
                {
                failed("Reset of filter did not work.");
                }

            // close the list
            prtFList.close();

            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests resetting the filter on an open Printer file List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var002()
    {
        try
            {
            // create a PrinterFile list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.openSynchronously();

            // reset the filter
            prtFList.resetFilter();

            failed("Could reset filter on an opened list.");

            prtFList.close();

            } // end try block

        catch (Exception e)
            {
            // verify that we go back the right exception
            if (exceptionIs(e, "ExtendedIllegalStateException"))  succeeded();
            else failed(e, "Unexpected exception");
            }

    } // end Var002

    // $$$ TO DO $$$
    // Create a VarXXX() method using the following as a template.
    // You should have a method for each variation in your testcase.

    /**
     Some description here.  This will becaome part of the testplan,
     so be descriptive and complete!  Include high-level description
     of what is being tested and expected results.
     **/

/* $$$ TO DO $$$ - delete this line
    public void VarXXX()
    {
	try
	{
	    // $$$ TO DO $$$
	    // Put your test variation code here.
	    succeeded();  // Note: This variation will be successful.
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end VarXXX
$$$ TO DO $$$ - delete this line */

} // end NPPrtFListResetFilterTestcase class


