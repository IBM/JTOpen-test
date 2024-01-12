///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQListResetFilterTestcase.java
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
 Testcase NPOutQListResetFilterTestcase.
 **/
public class NPOutQListResetFilterTestcase extends Testcase

{
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQListResetFilterTestcase(AS400            systemObject,
                                         Vector           variationsToRun,
                                         int              runMode,
                                         FileOutputStream fileOutputStream)    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPOutQListResetFilterTestcase", 2,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQListResetFilterTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
	{
            // create LISTRTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/LISTRTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/LISTRTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
                {
                failed("Could not grant *public authority to output queue. "
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
            // run variation X
            if ((allVariations || variationsToRun_.contains("X")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(X);
                VarXXX();
                }
$$$ TO DO $$$ - delete this line */

            // clear the output queue we created.
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/LISTRTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/LISTRTST)") == false)
                {
                output_.println("Could not delete output queue we created. "
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
     * Tests resetting the filter on an Output Queue List.
     **/
    public void Var001()
    {
        try
            {
            // create a printer list object
            OutputQueueList outQList = new OutputQueueList(systemObject_);

            outQList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTRTST.OUTQ");

            outQList.openSynchronously();

            // get the number of listed output queues
            int prevNum = outQList.size();

            // close the list
            outQList.close();

            // reset the output queue filter
            outQList.resetFilter();

            // set the resource filter to receive all resources in NPJAVA
            outQList.setQueueFilter("/QSYS.LIB/%LIBL%.LIB/%ALL%.OUTQ");
            
            // list the output queues
            outQList.openSynchronously();

            // get the number of listed output queues
            int postNum = outQList.size();

            // verify that we received more output queues the second time
            if (postNum > prevNum)
                {
                succeeded();
                }
            else
                {
                failed("Reset of filter did not work.");
                }

            // close the list
            outQList.close();

            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests resetting the filter on an open Output Queue List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var002()
    {
        try
            {
            // create a printer list object
            OutputQueueList outQList = new OutputQueueList(systemObject_);

            // open the list
            outQList.openAsynchronously();

            // reset the filter
            outQList.resetFilter();

            failed("Could reset filter on an opened list.");

            outQList.close();

            } // end try block

        catch (Exception e)
            {
            // verify that we go back the right exception
            if (exceptionIs(e, "ExtendedIllegalStateException"))  succeeded();
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

} // end NPOutQListResetFilterTestcase class


