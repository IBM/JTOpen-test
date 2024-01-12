///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtDNameTestcase.java
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
 Testcase NPPrtDNameTestcase.
 **/
public class NPPrtDNameTestcase extends Testcase
{

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtDNameTestcase(AS400            systemObject,
                              Vector           variationsToRun,
                              int              runMode,
                              FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
	super(systemObject, "NPPrtDNameTestcase", 6,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtDNameTestcase");
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

            // run variation 6
            if ((allVariations || variationsToRun_.contains("6")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(6);
                Var006();
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
     * Tests retrieving the printer name.
     **/
    public void Var001()
    {
	try
	{

	    // create a printer device object
	    Printer prtD = new Printer(systemObject_, "JAVAPRINT");

	    if (prtD.getName().trim().equals("JAVAPRINT")) succeeded();
	    else failed("Could not retrive printer name");

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var001

    /**
     * Tests setting the printer name.
     **/
    public void Var002()
    {
	try
	{
            // create a printer device object using default constructor
            Printer prtD = new Printer();

            prtD.setSystem(systemObject_);
            prtD.setName("JAVAPRINT");

	    if (prtD.getName().trim().equals("JAVAPRINT")) succeeded();
            else failed("Could not set/get printer name");

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var002

    /**
     * Tests setting the printer's system name to null.
     **/
    public void Var003()
    {
	try
	{
            // create a printer device object using default constructor
            Printer prtD = new Printer();

            prtD.setSystem(null);
            failed("Could set system name to null");

	} // end try block

        catch (NullPointerException e)
	{
            succeeded();
	}

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var003

    /**
     * Tests setting the printer's name to null.
     **/
    public void Var004()
    {
	try
	{
            // create a printer device object using default constructor
            Printer prtD = new Printer();

            prtD.setName(null);
            failed("Could set printer name to null");

	} // end try block

        catch (NullPointerException e)
	{
            succeeded();
	}

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var004

    /**
     * Tests getting the printer's system name before setting it.
     **/
    public void Var005()
    {
	try
	{
            // create a printer device object using default constructor
            Printer prtD = new Printer();

            if (prtD != null)
                {  
                if( prtD.getSystem() == null )
                    {
                    succeeded();
                    }
                else
                    {
                    failed("System name was not set, expecting null");  
                    }
                }
            else
                {
                failed("Could not create a Printer reference");
                }

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var005

    /**
     * Tests getting the printer name before setting it.
     **/
    public void Var006()
    {
	try
	{
            // create a printer device object using default constructor
            Printer prtD = new Printer();

            if (prtD != null)
                {    
                if( prtD.getName().length() == 0 )
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Printer name was not set, expecting empty string");
                    }
                }
            else
                {
                failed("Could not create a Printer Reference");
                }

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var006

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

} // end NPPrtDNameTestcase class


