///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtFSetAttrsTestcase.java
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
 Testcase NPPrtFSetAttrsTestcase.
 **/
public class NPPrtFSetAttrsTestcase extends Testcase
{
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtFSetAttrsTestcase(AS400            systemObject,
                                  Vector           variationsToRun,
                                  int              runMode,
                                  FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPPrtFSetAttrsTestcase", 6,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtFSetAttrsTestcase");
        boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create JAVAPRINT printer file
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTPRTF FILE(NPJAVA/JAVAPRINT)") == false)
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
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
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
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(4);
                Var004();
                }

            // run variation 5
            if ((allVariations || variationsToRun_.contains("5")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(5);
                Var005();
                }

            // run variation 6
            if ((allVariations || variationsToRun_.contains("6")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
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

            // delete the printer file that we created
            if (cmd.run("DLTF FILE(NPJAVA/JAVAPRINT)") == false)
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
     * Tests setting a float attribute on a printer file.
     **/
    public void Var001()
    {
        try
            {
            // create a printer file object using valid system name and printer file name
            PrinterFile prtF = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // create a PrintParameterList object
            PrintParameterList pList = new PrintParameterList();

            // set a float attribute
            pList.setParameter(PrintObject.ATTR_CPI,(float)10.0);

            // set the attributes
            prtF.setAttributes(pList);

            // verify it actually got set
            if (prtF.getFloatAttribute(PrintObject.ATTR_CPI).floatValue() == 10.0) succeeded();
            else failed("Could not set a float attribute.");
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting an integer attribute on a printer file.
     **/
    public void Var002()
    {
        try
            {
            // create a printer file object using valid system name and printer file name
            PrinterFile prtF = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // create a PrintParameterList object
            PrintParameterList pList = new PrintParameterList();

            // set an integer attribute
            pList.setParameter(PrintObject.ATTR_COPIES,10);

            // set the attributes
            prtF.setAttributes(pList);

            // verify it actually got set
            if (prtF.getIntegerAttribute(PrintObject.ATTR_COPIES).intValue() == 10) succeeded();
            else failed("Could not set an integer attribute.");
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting a string attribute on a printer file.
     **/
    public void Var003()
    {
        try
            {
            // create a printer file object using valid system name and printer file name
            PrinterFile prtF = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // create a PrintParameterList object
            PrintParameterList pList = new PrintParameterList();

            // set an string attribute
            pList.setParameter(PrintObject.ATTR_FORMTYPE,"JAVAFORM");

            // set the attributes
            prtF.setAttributes(pList);

            // verify it actually got set
            if (prtF.getStringAttribute(PrintObject.ATTR_FORMTYPE).trim().equals("JAVAFORM")) succeeded();
            else failed("Could not set a string attribute.");
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003


    /**
     * Tests setting an invalid attribute on a printer file.
     * Expect CPF0006
     **/
    public void Var004()
    {
        try
            {
            // create a printer file object using valid system name and printer file name
            PrinterFile prtF = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // create a PrintParameterList object
            PrintParameterList pList = new PrintParameterList();

            // set an string attribute that is not valid for a printer file
            pList.setParameter(PrintObject.ATTR_SPOOLFILE,"MySpoolFile");

            try
                {
                // set the attributes
                prtF.setAttributes(pList);

                failed("Could set an invalid attribute.");
                }
            catch(AS400Exception e)
                {
                AS400Message msg = e.getAS400Message();
                if (!msg.getID().trim().equals("CPF0006"))
                    {
                    failed("Incorrect message received for setting an invalid attribute. ID="+msg.getID());
                    }
                else
                    {
                    succeeded();  // Note: This variation will be successful.
                    }
                }
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests setting a null attribute on a printer file.
     * Expect NullPointerException
     **/
    public void Var005()
    {
        try
            {
            // create a printer file object using valid system name and printer file name
            PrinterFile prtF = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            try
                {
                // set the attributes
                prtF.setAttributes(null);

                failed("Could set a null attribute.");
                }
            catch(Exception e)
                {
                if (exceptionIs(e, "NullPointerException"))  succeeded();
                else failed (e, "Unexpected exception");
                }
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests setting multiple attributes on a printer file.
     **/
    public void Var006()
    {
        try
            {
            // create a printer file object using valid system name and printer file name
            PrinterFile prtF = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // create a PrintParameterList object
            PrintParameterList pList = new PrintParameterList();

            // set an string attribute
            pList.setParameter(PrintObject.ATTR_FORMTYPE,"JAVAFORM");

            // set an integer attribute
            pList.setParameter(PrintObject.ATTR_COPIES,10);

            // set a float attribute
            pList.setParameter(PrintObject.ATTR_CPI,(float)10.0);

            // set the attributes
            prtF.setAttributes(pList);

            // verify they actually got set
            if ( (prtF.getStringAttribute(PrintObject.ATTR_FORMTYPE).trim().equals("JAVAFORM")) &&
                 (prtF.getIntegerAttribute(PrintObject.ATTR_COPIES).intValue() == 10) &&
                 (prtF.getFloatAttribute(PrintObject.ATTR_CPI).floatValue() == 10.0) ) succeeded();
            else failed("Could not set a string attribute.");
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

} // end NPPrtFSetAttrsTestcase class


