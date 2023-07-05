///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQUpdateTestcase.java
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
 * Testcase NPOutQUpdateTestcase.
 **/
public class NPOutQUpdateTestcase extends Testcase
{

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQUpdateTestcase(AS400            systemObject,
                                Vector           variationsToRun,
                                int              runMode,
                                FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPOutQUpdateTestcase", 2,
              variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQUpdateTestcase");
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
            // create UPDATTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/UPDATTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/UPDATTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
                {
                failed("Could not grant *public authority to output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

                // now we are going to create a spooled file on the output queue to see if update changes
                // the number of files

                // create a output queue object
                OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/UPDATTST.OUTQ");

                // save the number of files on the output queue
                int prevNum = outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

                // buffer for data
                byte[] buf = new byte[2048];

                // fill the buffer
                for (int i=0; i<2048; i++) buf[i] = (byte)'9';

                // create a spooled file output stream
                SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, outQ);

                // write some data
                outStream.write(buf);

                // close the spooled file
                outStream.close();

                // update the output queue
                outQ.update();
                
                // query for number of files on output queue and it should be at least 1 more
                if (outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() < prevNum+1)
                    {
                    failed("Could not update attributes on output queue");
                    }
                else succeeded();

	    // clear the output queue we created.
	    if (cmd.run("CLROUTQ OUTQ(NPJAVA/UPDATTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
               }

	    // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/UPDATTST)") == false)
                {
                output_.println("Could not delete output queue we created. "
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
     * Tests throwing of ExtendedIllegalStateException. An OutputQueue is
     * constructed with no parameters and then used without setting the
     * system and the IFS path.
     **/
    public void Var002()
    {
        try
            {
            // create an output queue object using no parameters,
            // the default constructor for JavaBeans.
            OutputQueue outQ = new OutputQueue();

            // call a method to force check of object's state
            outQ.update();
            failed("Should have gotten ExtendedIllegalStateException");
            } 

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

} // end NPOutQUpdateTestcase class


