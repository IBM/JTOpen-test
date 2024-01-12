///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtFCtorTestcase.java
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
 Testcase NPPrtFCtorTestcase.
 **/
public class NPPrtFCtorTestcase extends Testcase
{

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtFCtorTestcase(AS400            systemObject,
                     Vector           variationsToRun,
                     int              runMode,
                     FileOutputStream fileOutputStream)
    {
     // $$$ TO DO $$$
     // Replace the third parameter with the total number of variations
     // in this testcase.
     super(systemObject, "NPPrtFCtorTestcase", 5,
           variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtFCtorTestcase");
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
     * Tests construction of PrinterFile object with good parameters.
     **/
    public void Var001()
    {
        try
            {
            // create a printer file object using valid system name and printer file name
            PrinterFile prtF = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // call a method to ensure we have a good object
            prtF.update();

            succeeded();  // Note: This variation will be successful.
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests construction of PrinterFile object with a null system parameter.
     *      A NullPointerException should be thrown.
     **/
    public void Var002()
    {
        try
            {
            // create a printer file object using null system name and valid printer file name
            PrinterFile prtF = new PrinterFile(null, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            failed("Could use null system name.");

            } // end try block

        catch (Exception e)
            {
            if (exceptionIs(e, "NullPointerException"))  succeeded();
            else failed (e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests construction of PrinterFile object with a null printer file name parameter.
     *      A NullPointerException should be thrown.
     **/
    public void Var003()
    {
        try
            {
            // create a printer file object using valid system name and null printer file name
            PrinterFile prtF = new PrinterFile(systemObject_, null);

            failed("Could use null printer name.");

            } // end try block

        catch (Exception e)
            {
            if (exceptionIs(e, "NullPointerException"))  succeeded();
            else failed (e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests construction of PrinterFile object with an invalid printer file name parameter.
     *      A IllegalPathNameException should be thrown.
     **/
    public void Var004()
    {
        try
            {
            // create a printer file object using valid system name and invalid printer file name
            PrinterFile prtF = new PrinterFile(systemObject_, "ThisIsAnInvalidName");

            failed("Could use invalid printer name.");

            } // end try block

        catch (Exception e)
            {
            // Check for IllegalPathNameException
            if (exceptionIs(e, "IllegalPathNameException"))  succeeded();
            else failed (e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests construction of Printer File object with no parameters. This
     * default constructor is provide for visual application builders
     * that support JavaBeans.
     **/
    public void Var005()
    {
        try
            {
            // create a printer file object using no parameters.
            PrinterFile prtF = new PrinterFile();

            succeeded();  // Note: This variation will be successful.
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
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

} // end NPPrtFCtorTestcase class


