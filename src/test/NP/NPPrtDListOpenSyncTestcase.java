///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtDListOpenSyncTestcase.java
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
 Testcase NPPrtDListOpenSyncTestcase.
 **/
public class NPPrtDListOpenSyncTestcase extends Testcase
{   
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtDListOpenSyncTestcase(AS400            systemObject,
                                      Vector           variationsToRun,
                                      int              runMode,
                                      FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPPrtDListOpenSyncTestcase",2,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtDListOpenSyncTestcase");
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
     * Tests listing printer devices synchronously.
     **/
    public void Var001()
    {
	try
	{
	    // create a printer list object
	    PrinterList prtDList = new PrinterList(systemObject_);

	    prtDList.setPrinterFilter("JAVAPRINT");

	    prtDList.openSynchronously();

	    Enumeration e = prtDList.getObjects();
            String prtDName = null;
            int listed = 0, size;
            size = prtDList.size();

            // check to see if we got at least one printer device
            if (size >= 1)
                {
                boolean fSuccess = false;

                // check the printer device we go back
                while (listed < size)
                    {
                    Printer prtD = (Printer)prtDList.getObject(listed++);
                    prtDName = prtD.getName();
                    if (prtDName.equals("JAVAPRINT"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many printer devices were listed
                output_.println(listed + " printer device(s) listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect printer device listed: " + prtDName);
                    }
                } // end if size
            else 
                {
                failed("No printer devices found.");
                }

	    prtDList.close();
	    System.gc();
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var001

     /**
     * Tests listing printer devices synchronously with
     * a PrinterList object in an invalid state.
     **/
    public void Var002()
    {
	try
	{
            // create a printer list object using default constructor
            PrinterList prtDList = new PrinterList();

	    prtDList.setPrinterFilter("JAVAPRINT");

	    prtDList.openSynchronously();

            failed("Should have gotten ExtendedIllegalStateException");
            } // end try block

        catch (ExtendedIllegalStateException e)
            {
            // We got the exception we were expecting.
            succeeded(); 
            }

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
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

} // end NPPrtDListOpenSyncTestcase class


