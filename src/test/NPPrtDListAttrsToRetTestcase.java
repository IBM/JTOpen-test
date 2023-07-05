///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtDListAttrsToRetTestcase.java
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
import java.io.IOException;

import java.util.Vector;
import java.util.Enumeration;
import com.ibm.as400.access.*;

/**
 Testcase NPPrtDListAttrsToRetTestcase.
 **/
public class NPPrtDListAttrsToRetTestcase extends Testcase

{
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtDListAttrsToRetTestcase(AS400            systemObject,
                                        Vector           variationsToRun,
                                        int              runMode,
                                        FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPPrtDListAttrsToRetTestcase", 5,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtDListAttrsToRetTestcase");
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
     * Tests setting valid attributes to retrieve on a Printer Device List.
     **/
    public void Var001()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            // set the printer filter 
            prtDList.setPrinterFilter("JAVAPRINT");

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_DEVCLASS;
            attrs[1] = PrintObject.ATTR_DEVMODEL;
            attrs[2] = PrintObject.ATTR_DEVTYPE;

            // set the attributes to retrieve
            prtDList.setAttributesToRetrieve(attrs);

            // list the printer device
            prtDList.openSynchronously();

            Enumeration e = prtDList.getObjects();

            // check to see if we got our printer device
            if (prtDList.size() == 1)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    Printer prtD = (Printer)e.nextElement();
                    if ( (prtD.getStringAttribute(PrintObject.ATTR_DEVCLASS) != null) &&
                         (prtD.getStringAttribute(PrintObject.ATTR_DEVMODEL) != null) &&
                         (prtD.getStringAttribute(PrintObject.ATTR_DEVTYPE) != null) )
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        output_.println("Bad attributes on "+prtD.getName());
                        break;
                        }
                    } // end while

                // indicate how many printer devices were listed
                output_.println(prtDList.size() + " Printer Device(s) listed.");

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
                failed("Incorrect number of printer device(s) found in list.");
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
     * Tests setting null attributes to retrieve on a Printer Device List.
     * Expects NullPointerException
     **/
    public void Var002()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                prtDList.openAsynchronously();
            }
            else {
                prtDList.openSynchronously();
            }

            // set the attributes to retrieve
            prtDList.setAttributesToRetrieve(null);

            failed("Could set null attributes to retrieve.");

            prtDList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back a null pointer exception
            if (exceptionIs(e, "NullPointerException"))  succeeded();
            else failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting the attributes to retrieve on an open Printer Device List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var003()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                prtDList.openAsynchronously();
            }
            else {
                prtDList.openSynchronously();
            }

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_DEVCLASS;
            attrs[1] = PrintObject.ATTR_DEVMODEL;
            attrs[2] = PrintObject.ATTR_DEVTYPE;

            // set the attributes to retrieve
            prtDList.setAttributesToRetrieve(attrs);

            failed("Could set attributes to retrieve on an opened list.");

            prtDList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back an extened illegal state exception
            if (exceptionIs(e, "ExtendedIllegalStateException"))  succeeded();
            else failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests resetting attributes to retrieve on a Printer Device List.
     **/
    public void Var004()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            // set the printer filter 
            prtDList.setPrinterFilter("JAVAPRINT");

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_DEVCLASS;
            attrs[1] = PrintObject.ATTR_DEVMODEL;
            attrs[2] = PrintObject.ATTR_DEVTYPE;

            // set the attributes to retrieve
            prtDList.setAttributesToRetrieve(attrs);

            // list the printer device
            prtDList.openSynchronously();

            // close the list
            prtDList.close();

            // reset the attributes 
            prtDList.resetAttributesToRetrieve();

            // set the printer filter 
            prtDList.setPrinterFilter("JAVAPRINT");
            
            // list the printer devices
            prtDList.openSynchronously();

            Enumeration e = prtDList.getObjects();

            // check to see if we got some printer devices
            if (prtDList.size() == 1)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    Printer prtD = (Printer)e.nextElement();

                    if ( (prtD.getStringAttribute(PrintObject.ATTR_AFP) != null) &&
                         (prtD.getStringAttribute(PrintObject.ATTR_FORMFEED) != null) )
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        output_.println("Bad attributes on "+prtD.getName());
                        break;
                        }
                    } // end while

                // indicate how many printer devices were listed
                output_.println(prtDList.size() + " Printer Device(s) listed.");

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
                failed("Incorrect number of printer devices listed.");
                }

            // close the list
            prtDList.close();

            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests resetting the attributes to retrieve on an open Printer Device List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var005()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                prtDList.openAsynchronously();
            }
            else {
                prtDList.openSynchronously();
            }

            // set the attributes to retrieve
            prtDList.resetAttributesToRetrieve();

            failed("Could reset attributes to retrieve on an opened list.");

            prtDList.close();

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

} // end NPPrtDListAttrsToRetTestcase class


