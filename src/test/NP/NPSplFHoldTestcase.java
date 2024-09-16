///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFHoldTestcase.java
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
 Testcase NPSplFHoldTestcase.
 **/
public class NPSplFHoldTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFHoldTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFHoldTestcase(AS400            systemObject,
			      Vector           variationsToRun,
			      int              runMode,
			      FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPSplFHoldTestcase", 4,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFHoldTestcase");
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

	    // run variation 3
	    if ((allVariations || variationsToRun_.contains("3")) &&
		runMode_ != ATTENDED)  // Note: This is an unattended variation.
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
     Tests holding of a SpooledFile object with null parameters.
     **/
    public void Var001()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();                

            // hold the spooled file
            splF.hold(null);

            // check the status of the spooled file to ensure it is *held
            if (splF.getStringAttribute(PrintObject.ATTR_SPLFSTATUS).trim().equals("*HELD"))
                {
                // release the held spooled file
                splF.release();

                succeeded();  // Note: This variation will be successful.

                }
            else
                {
                failed("Could not hold the spooled file.");
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
     Tests holding of a SpooledFile object with *IMMED parameter.
     **/
    public void Var002()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();                

            // hold the spooled file
            splF.hold("*IMMED");

            // check the status of the spooled file to ensure it is *held
            if (splF.getStringAttribute(PrintObject.ATTR_SPLFSTATUS).trim().equals("*HELD"))
                {
                // release the held spooled file
                splF.release();

                succeeded();  // Note: This variation will be successful.

                }
            else
                {
                failed("Could not hold the *IMMED spooled file.");
                }

            // delete the spooled file we created
            splF.delete();
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     Tests holding of a SpooledFile object with *PAGEEND parameter.
     **/
    public void Var003()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();                

            // hold the spooled file
            splF.hold("*PAGEEND");

            // check the status of the spooled file to ensure it is *held
            if (splF.getStringAttribute(PrintObject.ATTR_SPLFSTATUS).trim().equals("*HELD"))
                {
                // release the held spooled file
                splF.release();

                succeeded();  // Note: This variation will be successful.              
                }
            else
                {
                failed("Could not hold the *PAGEEND spooled file.");
                }

            // delete the spooled file we created
            splF.delete();
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     Tests holding of a held SpooledFile object.
     **/
    public void Var004()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();                

            // hold the spooled file
            splF.hold(null);

            // check the status of the spooled file to ensure it is *held
            if (splF.getStringAttribute(PrintObject.ATTR_SPLFSTATUS).trim().equals("*HELD"))
                {
                try
                    {
                    // hold the held spooled file
                    splF.hold(null);

                    failed("Could hold a held spooled file.");
                    }
                catch (AS400Exception e)
                    {
                    AS400Message msg = e.getAS400Message();
                    if (!msg.getID().trim().equals("CPF3337"))
                        {
                        failed("Incorrect message received for holding a held spooled file. ID="+msg.getID());
                        }
                    else
                        {
                        // release the spooled file
                        splF.release();

                        succeeded();  // Note: This variation will be successful.
                        }
                    } // end catch
                } // end if splf status
            else
                {
                failed("Could not hold the spooled file.");
                }

            // delete the spooled file we created
            splF.delete();
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

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

        // check to see that we got a spooled file output stream reference
        if (outStream != null)
            {

            // write some data
            outStream.write(buf);

            // close the spooled file
            outStream.close();

            // return the new SpooledFile
            return outStream.getSpooledFile();
            }
        else
            {
            return (SpooledFile)null;
            }

    } // end createSpooledFile


} // end NPSplFHoldTestcase class


