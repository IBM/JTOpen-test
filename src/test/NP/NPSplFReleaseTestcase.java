///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFReleaseTestcase.java
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
 Testcase NPSplFReleaseTestcase.
 **/
public class NPSplFReleaseTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFReleaseTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFReleaseTestcase(AS400            systemObject,
				 Vector           variationsToRun,
				 int              runMode,
				 FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPSplFReleaseTestcase", 2,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFReleaseTestcase");
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
		runMode_ != ATTENDED)  // Note: This is an unattended variation.
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
     * Tests releasing of a SpooledFile.
     **/
    public void Var001()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();                

            // hold the spooled file
            splF.hold(null);

            // release the spooled file
            splF.release();

            // check the status of the spooled file to ensure it is *ready
            if (splF.getStringAttribute(PrintObject.ATTR_SPLFSTATUS).trim().equals("*READY"))
                {
                succeeded();  // Note: This variation will be successful.
                }
            else
                {
                failed("Could not release the spooled file.");
                }

            // delete the spooled file we created
            splF.delete();
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests releasing a spooled file that is not held.
     * Expects CPF3322
     **/
    public void Var002()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();                

            // check the status of the spooled file to ensure it is *ready
            if (!splF.getStringAttribute(PrintObject.ATTR_SPLFSTATUS).trim().equals("*READY"))
                {
                // release the spooled file
                splF.release();
                }

            // hold the spooled file
            splF.hold(null);

            // release the spooled file
            splF.release();

            // try to release the spooled file again
            try
                {
                splF.release();

                failed("Was able to release a released spooled file.");

                }

            catch (AS400Exception e)
                {
                AS400Message msg = e.getAS400Message();
                if (!msg.getID().trim().equals("CPF3322"))
                    {
                    failed("Incorrect message received for releasing a released spooled file. ID= "+msg.getID());
                    }
                else
                    {
                    succeeded();  // Note: This variation will be successful.
                    }
                }

            // delete the spooled file we created
            splF.delete();
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

    // This method creates a spooled file
    private SpooledFile createSpooledFile()
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException
    {
        // buffer for data
        byte[] buf = new byte[2048];

        // fill the buffer
        for (int i=0; i<2048; i++) buf[i] = (byte)'9';

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, null);

        // write some data
        outStream.write(buf);

        // close the spooled file
        outStream.close();

        // return the new SpooledFile
        return outStream.getSpooledFile();
    } // end createSpooledFile

} // end NPSplFReleaseTestcase class


