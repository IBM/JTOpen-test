///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtDCtorTestcase.java
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
import com.ibm.as400.access.*;

/**
 Testcase NPPrtDCtorTestcase.
 **/
public class NPPrtDCtorTestcase extends Testcase
{

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtDCtorTestcase(AS400            systemObject,
			      Vector           variationsToRun,
			      int              runMode,
			      FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPPrtDCtorTestcase", 7,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtDCtorTestcase");
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

            // run variation 7
            if ((allVariations || variationsToRun_.contains("7")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
                setVariation(7);
                Var007();
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
     * Tests construction of Printer object with good parameters.
     **/
    public void Var001()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, "JAVAPRINT");

            // call a method to ensure we have a good object
            prtD.update();

            succeeded();  // Note: This variation will be successful.
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests construction of Printer object with a null system parameter.
     * Expects NullPointerException
     **/
    public void Var002()
    {
        try
            {

            // create a printer device object using null system name and valid printer name
            Printer prtD = new Printer(null, "JAVAPRINT");

            failed("Could use null system name.");

            } // end try block

        catch (Exception e)
            {
            if (exceptionIs(e, "NullPointerException"))  succeeded();  
            else failed (e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests construction of Printer object with a null printer name parameter.
     * Expects NullPointerException
     **/
    public void Var003()
    {
        try
            {

            // create a printer device object using valid system name and null printer name
            Printer prtD = new Printer(systemObject_, null);

            failed("Could use null printer name.");

            } // end try block

        catch (Exception e)
            {
            if (exceptionIs(e, "NullPointerException"))  succeeded();  
            else failed (e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests construction of Printer object with an invalid printer name parameter (too long).
     * Expects ExtendedIllegalArgumentException
     **/
    public void Var004()
    {
        try
            {

            // create a printer device object using valid system name and invalid printer name
            Printer prtD = new Printer(systemObject_, "ThisIsAnInvalidName");

            failed("Could use invalid printer name.");

            } // end try block

        catch (Exception e)
            {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))  succeeded();  
            else failed (e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests construction of Printer device object with no parameters. This
     * default constructor is provide for visual application builders
     * that support JavaBeans.
     **/
    public void Var005()
    {
	try
	{
            // create a printer device object with no parameters. 
            Printer prtD = new Printer();

            succeeded();  // Note: This variation will be successful.
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests construction of Printer object with an invalid printer name parameter (too short).
     * Expects ExtendedIllegalArgumentException
     **/
    public void Var006()
    {
        try
            {

            // create a printer device object using valid system name and invalid printer name
            Printer prtD = new Printer(systemObject_, "");

            failed("Could use invalid printer name.");

            } // end try block

        catch (Exception e)
            {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))  succeeded();  
            else failed (e, "Unexpected exception");
            }

    } // end Var006


    /**
     * Tests construction of Printer object with an invalid printer name parameter
     * Expects ExtendedIllegalArgumentException
     **/
    public void Var007()
    {
        try
            {

            // create a printer device object using valid system name and invalid printer name
            Printer prtD = new Printer(systemObject_, "NPJAVANP");

            try
                {
                // attempt to call a method on the object that does not represent
                // a real print device description
                prtD.update();

                failed("Could use invalid printer name.");
                }
            catch (AS400Exception e)
                {
                AS400Message msg = e.getAS400Message();
                if (!msg.getID().trim().equals("CPF2702"))
                    {
                    failed("Incorrect message received for using a non existent printer. ID="+msg.getID());
                    }
                else
                    {
                    succeeded();  // Note: This variation will be successful.
                    }
                }

            } // end try block

        catch (Exception e)
            {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))  succeeded();  
            else failed (e, "Unexpected exception");
            }

    } // end Var007


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

} // end NPPrtDCtorTestcase class


