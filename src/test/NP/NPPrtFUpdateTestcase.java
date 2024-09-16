///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtFUpdateTestcase.java
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
import com.ibm.as400.access.*;

import test.Testcase;

/**
 * Testcase NPPrtFUpdateTestcase.
 **/
public class NPPrtFUpdateTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtFUpdateTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtFUpdateTestcase(AS400            systemObject,
                                Vector           variationsToRun,
                                int              runMode,
                                FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPPrtFUpdateTestcase", 2,
              variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtFUpdateTestcase");
        boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
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

            } // end try block

        catch( Exception e )
            {
            failed(e, "Unexpected exception");
            }

    } // end run method

    /**
     * Tests updating the attributes of an output queue.
     **/
    public void Var001()
    {
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

            // create a printer file object using valid system name and printer file name
            PrinterFile prtF = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // create a PrintParameterList object
            PrintParameterList pList = new PrintParameterList();

            // set a float attribute
            pList.setParameter(PrintObject.ATTR_CPI,(float)10.0);

            // set the attributes
            prtF.setAttributes(pList);

            // call a method to ensure we have a good object
            prtF.update();

            // verify it actually got set
            if (prtF.getFloatAttribute(PrintObject.ATTR_CPI).floatValue() == 10.0) succeeded();
            else failed("Could not set a float attribute.");

            // delete the printer file that we created
            if (cmd.run("DLTF FILE(NPJAVA/JAVAPRINT)") == false)
                {
                output_.println("Could not delete the printer file we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests throwing of ExtendedIllegalStateException. A Printer File is
     * constructed with no parameters and then used without setting the
     * system and the IFS path.
     **/
    public void Var002()
    {
        try
            {
            // create a printer file object using no parameters
            PrinterFile prtF = new PrinterFile();

            // call a method to force check of object's state
            prtF.update();

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

} // end NPPrtFUpdateTestcase class


