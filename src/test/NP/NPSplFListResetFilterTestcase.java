///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFListResetFilterTestcase.java
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
 Testcase NPSplFListResetFilterTestcase.
 **/
public class NPSplFListResetFilterTestcase extends Testcase

{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFListResetFilterTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFListResetFilterTestcase(AS400            systemObject,
                                         Vector           variationsToRun,
                                         int              runMode,
                                         FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPSplFListResetFilterTestcase", 2,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFListResetFilterTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
	{
            // create RSTTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/RSTTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/RSTTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/RSTTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/RSTTST)") == false)
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
     * Tests resetting the filter on a Spooled File List.
     **/
    public void Var001()
    {
        try
            {
            // create a spooled file on the output queue
            SpooledFile splF = createSpooledFile();

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queue filter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/RSTTST.OUTQ");

            // now try to build list synchrously
            splFList.openSynchronously();

            // get the number of listed files
            int prevNum = splFList.size();

            // close the list
            splFList.close();

            // reset the SpooledFileList filter
            splFList.resetFilter();

            // set the queue filter to receive all spooled files
            splFList.setQueueFilter("/QSYS.LIB/%ALL%.LIB/%ALL%.OUTQ");
            
            // list the files
            splFList.openSynchronously();

            // get the number of listed spooled
            int postNum = splFList.size();

            // verify that we received more spooled files the second time
            if (postNum > prevNum)
                {
                succeeded();
                }
            else
                {
                failed("Reset of filter did not work.");
                }

            // close the list
            splFList.close();

            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests resetting the filter on an open Spooled File List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var002()
    {
        try
            {
            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queue filter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/RSTTST.OUTQ");

            // now try to build list synchrously
            splFList.openSynchronously();

            // reset the filter
            splFList.resetFilter();

            failed("Could reset filter on an opened list.");

            splFList.close();

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

    // This method creates a spooled file
    private SpooledFile createSpooledFile()
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException,
             RequestNotSupportedException
    {
        // buffer for data
        byte[] buf = new byte[2048];

        // fill the buffer
        for (int i=0; i<2048; i++) buf[i] = (byte)'9';

        // create a reference to the output queue
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/RSTTST.OUTQ");

        // clear the output queue
        outQ.clear(null);

        // create a print parameter list for the spooled file name
        PrintParameterList pList = new PrintParameterList();

        // set the spooled file name
        pList.setParameter(PrintObject.ATTR_SPOOLFILE,"JAVAPRINT");

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, pList, null, outQ);

        // write some data
        outStream.write(buf);

        // close the spooled file
        outStream.close();

        // update the output queue attributes
        outQ.update();

        // return the new SpooledFile
        return outStream.getSpooledFile();
    } // end createSpooledFile

} // end NPSplFListResetFilterTestcase class


