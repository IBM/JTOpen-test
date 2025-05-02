///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPAFPRUpdateTestcase.java
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

import com.ibm.as400.access.AFPResource;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.PrintObject;

import test.Testcase;

/**
 * Testcase NPAFPRUpdateTestcase.
 **/
public class NPAFPRUpdateTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPAFPRUpdateTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPAFPRUpdateTestcase(AS400            systemObject,
                                Vector<String> variationsToRun,
                                int              runMode,
                                FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPAFPRUpdateTestcase", 2,
              variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPAFPRUpdateTestcase");
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
     * Tests updating the attributes of an AFPResource.
     **/
    public void Var001()
    {
        try
            {
            // create an AFP Resource object using valid system name and resource name
            AFPResource res = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            // change the text description of the AFP resource
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("QSYS/CHGOBJD OBJ(NPJAVA/C0D0GB10) OBJTYPE(*FNTRSC) TEXT('JAVAPRINT')") == false)
                {
                failed("Could not change a AFP resource description. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            // call a method to ensure we have a good object
            res.update();

            // verify it actually got updated
            if (res.getStringAttribute(PrintObject.ATTR_DESCRIPTION).trim().equals("JAVAPRINT")) succeeded();
            else failed("Could not update a printer device description.");

            // set the description back to the original value
            if (cmd.run("QSYS/CHGOBJD OBJ(NPJAVA/C0D0GB10) OBJTYPE(*FNTRSC) TEXT('GOTHIC ROMAN BOLD 100 10-PT')") == false)
                {
                output_.println("Could not change a AFP resource description back. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests throwing of ExtendedIllegalStateException. An AFPResource is
     * constructed with no parameters and then used without setting the
     * system and the IFS path.
     **/
    public void Var002()
    {
        try
            {
            // create an AFP Resource object using no parameters.
            AFPResource res = new AFPResource();

            // call a method to force check of object's state
            res.update();

            failed("Should have gotten ExtendedIllegalStateException");
            }

        catch (ExtendedIllegalStateException e)
            {
            // We got the exception we were expecting.
            succeeded(); 
            }

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
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

} // end NPAFPRUpdateTestcase class


