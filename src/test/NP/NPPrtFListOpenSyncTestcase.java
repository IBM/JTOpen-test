///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtFListOpenSyncTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.PrinterFile;
import com.ibm.as400.access.PrinterFileList;

import test.Testcase;

/**
 Testcase NPPrtFListOpenSyncTestcase.
 **/
public class NPPrtFListOpenSyncTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtFListOpenSyncTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtFListOpenSyncTestcase(AS400            systemObject,
                                      Vector<String> variationsToRun,
                                      int              runMode,
                                      FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPPrtFListOpenSyncTestcase", 2,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtFListOpenSyncTestcase");
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
     * Tests listing printer file synchronously.
     **/
    public void Var001()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // set the printer file filter 
            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            prtFList.openSynchronously();

            @SuppressWarnings("unused")
            Enumeration<PrinterFile> e = prtFList.getObjects();
            String prtFName = null;
            int listed = 0, size;
            size = prtFList.size();

            // check to see if we got at least one printer file
            if (size >= 1)
                {
                boolean fSuccess = false;

                // check the printer file we go back
                while (listed < size)
                    {
                    PrinterFile prtF = (PrinterFile)prtFList.getObject(listed++);
                    prtFName = prtF.getPath();
                    if (prtFName.equals("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many printer files were listed
                output_.println(listed + " printer file(s) listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect printer file listed: " + prtFName);
                    }
                } // end if size
            else 
                {
                failed("No printer files found.");
                }

            prtFList.close();
            System.gc();
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests listing printer file synchronously with a
     * PrinterFileList object in an invalid state.
     **/
    public void Var002()
    {
        try
            {
            // create a printer file list object using a default constructor
            PrinterFileList prtFList = new PrinterFileList();

            // set the printer file filter 
            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            prtFList.openSynchronously();
            prtFList.close(); 
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

} // end NPPrtFListOpenSyncTestcase class


