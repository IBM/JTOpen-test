///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFOutStrGetSplFTestcase.java
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
 Testcase NPSplFOutStrGetSplFTestcase.
 **/
public class NPSplFOutStrGetSplFTestcase extends Testcase
{
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFOutStrGetSplFTestcase(AS400            systemObject,
                                       Vector           variationsToRun,
	           		       int              runMode,
				       FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPSplFOutStrGetSplFTestcase", 2,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFOutStrGetSplFTestcase");
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
     * Tests retrieving the spooled file from a spooled file output stream.
     **/
    public void Var001()
    {
        try
            {
            // buffer for data
            byte[] buf = new byte[2048];

            // fill the buffer
            for (int i=0; i<2048; i++) buf[i] = (byte)'9';

            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, null);

            // write some data
            outStream.write(buf);

            // flush the spooled file output stream
            outStream.flush();

            // close the spooled file output stream
            outStream.close();

            // retrieve the spooled file
            SpooledFile splF = outStream.getSpooledFile();

            // call the update method to make sure it is a good
            // object.
            splF.update();

            succeeded();
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests retrieving the spooled file from a spooled file output stream, before closing the stream.
     **/
    public void Var002()
    {
        try
            {
            // buffer for data
            byte[] buf = new byte[2048];

            // fill the buffer
            for (int i=0; i<2048; i++) buf[i] = (byte)'9';

            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, null);

            // write some data
            outStream.write(buf);

            // retrieve the spooled file
            SpooledFile splF = outStream.getSpooledFile();

            // flush the spooled file output stream
            outStream.flush();

            // close the spooled file output stream
            outStream.close();

            // retrieve the spooled file
            SpooledFile splF1 = outStream.getSpooledFile();

	    // @A1C - Changed '==' to '.equals' for string comparison
            if ( (splF.getName().equals(splF1.getName())) &&
                 (splF.getNumber() == splF1.getNumber()) &&
                 (splF.getJobUser().equals(splF1.getJobUser())) &&
                 (splF.getJobNumber().equals(splF1.getJobNumber())) &&
                 (splF.getJobName().equals(splF1.getJobName()))) succeeded();
            else failed("Spooled Files don't match.");
                
            } // end try block

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
    
} // end NPSplFOutStrGetSplFTestcase class


