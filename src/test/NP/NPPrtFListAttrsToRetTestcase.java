///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtFListAttrsToRetTestcase.java
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
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.PrinterFile;
import com.ibm.as400.access.PrinterFileList;
import com.ibm.as400.access.PrinterList;

import test.Testcase;

/**
 Testcase NPPrtFListAttrsToRetTestcase.
 **/
public class NPPrtFListAttrsToRetTestcase extends Testcase

{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtFListAttrsToRetTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtFListAttrsToRetTestcase(AS400            systemObject,
                                        Vector<String> variationsToRun,
                                        int              runMode,
                                        FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPPrtFListAttrsToRetTestcase", 5,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtFListAttrsToRetTestcase");
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

            // run variation 3
            if ((allVariations || variationsToRun_.contains("3")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(3);
                Var003();
                }

            // run variation 4
            if ((allVariations || variationsToRun_.contains("4")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(4);
                Var004();
                }

            // run variation 5
            if ((allVariations || variationsToRun_.contains("5")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(5);
                Var005();
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
     * Tests setting valid attributes to retrieve on a Printer File List.
     **/
    public void Var001()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // set the printer file filter 
            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_COPIES;
            attrs[1] = PrintObject.ATTR_FORMFEED;
            attrs[2] = PrintObject.ATTR_LPI;

            // set the attributes to retrieve
            prtFList.setAttributesToRetrieve(attrs);

            // list the printer files
            prtFList.openSynchronously();

            Enumeration<PrinterFile> e = prtFList.getObjects();

            // check to see if we got our printer file
            if (prtFList.size() == 1)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    PrinterFile prtF = (PrinterFile)e.nextElement();
                    if ( (prtF.getIntegerAttribute(PrintObject.ATTR_COPIES) != null) &&
                         (prtF.getStringAttribute(PrintObject.ATTR_FORMFEED) != null) &&
                         (prtF.getFloatAttribute(PrintObject.ATTR_LPI) != null) )
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        output_.println("Bad attributes on "+prtF.getPath());
                        break;
                        }
                    } // end while

                // indicate how many printer files were listed
                output_.println(prtFList.size() + " Printer File(s) listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Failed to retrieve appropriate attributes");
                    }
                } // if size
            else 
                {
                failed("Incorrect number of printer file(s) found in list.");
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
     * Tests setting null attributes to retrieve on a Printer File List.
     * Expects NullPointerException
     **/
    public void Var002()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                prtFList.openAsynchronously();
            }
            else {
                prtFList.openSynchronously();
            }

            // set the attributes to retrieve
            prtFList.setAttributesToRetrieve(null);

            failed("Could set null attributes to retrieve.");

            prtFList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back a null pointer exception
            if (exceptionIs(e, "NullPointerException"))  succeeded();
            else failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting the attributes to retrieve on an open Printer File List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var003()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                prtFList.openAsynchronously();
            }
            else {
                prtFList.openSynchronously();
            }

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_COPIES;
            attrs[1] = PrintObject.ATTR_FORMFEED;
            attrs[2] = PrintObject.ATTR_LPI;

            // set the attributes to retrieve
            prtFList.setAttributesToRetrieve(attrs);

            failed("Could set attributes to retrieve on an opened list.");

            prtFList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back an extened illegal state exception
            if (exceptionIs(e, "ExtendedIllegalStateException"))  succeeded();
            else failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests resetting attributes to retrieve on a Printer File List.
     **/
    public void Var004()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // set the printer file filter 
            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_COPIES;
            attrs[1] = PrintObject.ATTR_FORMFEED;
            attrs[2] = PrintObject.ATTR_LPI;

            // set the attributes to retrieve
            prtFList.setAttributesToRetrieve(attrs);

            // list the printer files
            prtFList.openSynchronously();

            // close the list
            prtFList.close();

            // reset the attributes 
            prtFList.resetAttributesToRetrieve();

            // set the printer file filter 
            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");
           
            // list the printer files
            prtFList.openSynchronously();

            Enumeration<PrinterFile> e = prtFList.getObjects();

            // check to see if we got some printer files
            if (prtFList.size() == 1)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    PrinterFile prtF = (PrinterFile)e.nextElement();

                    if ( (prtF.getStringAttribute(PrintObject.ATTR_OUTPTY) != null) &&
                         (prtF.getStringAttribute(PrintObject.ATTR_FONTID) != null) )
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        output_.println("Bad attributes on "+prtF.getPath());
                        break;
                        }
                    } // end while

                // indicate how many printer files were listed
                output_.println(prtFList.size() + " Printer File(s) listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Failed to reset attributes");
                    }
                } // if size
            else 
                {
                failed("Incorrect number of printer files listed.");
                }

            // close the list
            prtFList.close();

            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests resetting the attributes to retrieve on an open Printer File List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var005()
    {
        try
            {
            // create a printer list object
            PrinterList prtFList = new PrinterList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                prtFList.openAsynchronously();
            }
            else {
                prtFList.openSynchronously();
            }

            // set the attributes to retrieve
            prtFList.resetAttributesToRetrieve();

            failed("Could reset attributes to retrieve on an opened list.");

            prtFList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back an extended illegal state exception
            if (exceptionIs(e, "ExtendedIllegalStateException"))  succeeded();
            else failed(e, "Unexpected exception");
            }

    } // end Var005

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

} // end NPPrtFListAttrsToRetTestcase class


