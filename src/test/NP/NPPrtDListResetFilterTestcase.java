///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtDListResetFilterTestcase.java
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
import com.ibm.as400.access.PrinterList;

import test.Testcase;

/**
 Testcase NPPrtDListResetFilterTestcase.
 **/
public class NPPrtDListResetFilterTestcase extends Testcase

{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtDListResetFilterTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtDListResetFilterTestcase(AS400            systemObject,
                                         Vector<String> variationsToRun,
                                         int              runMode,
                                         FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPPrtDListResetFilterTestcase", 2,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtDListResetFilterTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
	{
            // make sure JAVAPRINT printer device description exists
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTDEVPRT DEVD(JAVAPRINT) DEVCLS(*LCL) TYPE(*IPDS) MODEL(0) PORT(5) SWTSET(0) ONLINE(*NO) FONT(011)") == false)
                {
                failed("Could not create a printer device description. "
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

            // delete the printer device description that we created
            if (cmd.run("DLTDEVD JAVAPRINT") == false)
                {
                output_.println("Could not delete the printer device description we created. "
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
     * Tests resetting the filter on a Printer Device List.
     **/
    public void Var001()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            prtDList.setPrinterFilter("JAVAPRINT");

            prtDList.openSynchronously();

            // get the number of listed printer devices
            int prevNum = prtDList.size();

            // close the list
            prtDList.close();

            // reset the printer filter
            prtDList.resetFilter();

            // set the printer filter to receive all printers
            prtDList.setPrinterFilter("*ALL");
            
            // list the printers
            prtDList.openSynchronously();

            // get the number of listed printers
            int postNum = prtDList.size();

            // verify that we received more printers the second time
            if (postNum > prevNum)
                {
                succeeded();
                }
            else
                {
                failed("Reset of filter did not work.");
                }

            // close the list
            prtDList.close();

            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests resetting the filter on an open Printer Device List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var002()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            prtDList.openSynchronously();

            // reset the filter
            prtDList.resetFilter();

            failed("Could reset filter on an opened list.");

            prtDList.close();

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

} // end NPPrtDListResetFilterTestcase class


