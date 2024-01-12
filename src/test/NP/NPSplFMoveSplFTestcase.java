///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFMoveSplFTestcase.java
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
 Testcase NPSplFMoveSplFTestcase.
 **/
public class NPSplFMoveSplFTestcase extends Testcase
{
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFMoveSplFTestcase(AS400            systemObject,
				  Vector           variationsToRun,
				  int              runMode,
				  FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPSplFMoveSplFTestcase", 1,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFMoveSplFTestcase");
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
     Tests moving a spooled file to a position after a targeted spool file.
     **/
    public void Var001()
    {
        try
            {
            SpooledFile[] splF = new SpooledFile[10];

            // create 10 spooled files
            for (int i=0; i<10; i++)
                {
                // create a spooled file
                splF[i] = createSpooledFile();
                }

            // save the spooled file number of the one we are going to move after
            int prevNum = splF[2].getNumber();

            // move one of the spooled files after spooled file #2 (zero based)
            splF[9].move(splF[2]);

            // create a spooled file list object
            SpooledFileList splFileList = new SpooledFileList(systemObject_);

            // open a spooled file list
            splFileList.openSynchronously();

            // init a temp variable
            int prevNum1 = -1;

            Enumeration e = splFileList.getObjects();
            while(e.hasMoreElements() )
                {
                // get the object in the list
                SpooledFile splF1 = (SpooledFile)e.nextElement();

                // check to see if we found the spooled file we moved
                if (splF1.getNumber() == splF[9].getNumber())
                    {
                    // check to see that the previous spooled file matches
                    // the one we moved after
                    if (prevNum == prevNum1)
                        {
                        succeeded();
                        break;
                        }
                    else
                        {
                        failed("Found our moved spooled but previous number does not match.");
                        break;
                        }
                    }
                else
                    {
                    // save the spooled file number to compare next iteration
                    prevNum1 = splF1.getNumber();
                    }
                } // end while

            // close the spooled file list
            splFileList.close();
            System.gc();

            // delete the spooled files we created
            for (int i=0; i<10; i++)
                {
                // delete the spooled file
                splF[i].delete();
                }

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }
    } // end Var001

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

} // end NPSplFMoveSplFTestcase class


