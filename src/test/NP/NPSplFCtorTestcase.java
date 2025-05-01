///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFCtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.access.SpooledFileOutputStream;

import test.Testcase;

/**
 Testcase NPSplFCtorTestcase.
 **/
public class NPSplFCtorTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFCtorTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFCtorTestcase(AS400            systemObject,
			      Vector<String> variationsToRun,
			      int              runMode,
			      FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPSplFCtorTestcase", 7,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFCtorTestcase");
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
		runMode_ != ATTENDED) // Note: This is an unattended variation.
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

	    // run variation 5
	    if ((allVariations || variationsToRun_.contains("5")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(5);
		Var005();
	    }

	    // run variation 6
	    if ((allVariations || variationsToRun_.contains("6")) &&
		runMode_ != ATTENDED)  // Note: This is an unattended variation.
	    {
		setVariation(6);
		Var006();
	    }

	    // run variation 7
	    if ((allVariations || variationsToRun_.contains("7")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(7);
		Var007();
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
     Tests construction of a SpooledFile object with good parameters.
     **/
    public void Var001()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();                

            // create another reference to the spooled file 
            SpooledFile anotherSplF =  new SpooledFile(systemObject_,
                                                       splF.getName(),
                                                       splF.getNumber(),
                                                       splF.getJobName(),
                                                       splF.getJobUser(),
                                                       splF.getJobNumber());

            // call a method to ensure we have a good object
            anotherSplF.update();

            succeeded();  // Note: This variation will be successful.

            // delete the spooled file we created
            splF.delete();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests construction of a SpooledFile object with null system parameter.
     *      A NullPointerException should be thrown.
     **/
    public void Var002()
    {
        try
            {
            // create another reference to the spooled file 
            SpooledFile splF = new SpooledFile(null,
                                               "A Name",
                                               1,
                                               "A Job Name",
                                               "A Job User",
                                               "A Job Number");
            failed("Could use null system name."+splF);
            }
        catch (Exception e)
            {
            // Check for NullPointerException
            if (exceptionIs(e, "NullPointerException"))  succeeded();  // Note: This variation will be successful.
            else failed (e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests construction of a SpooledFile object with null name parameter.
     *      A NullPointerException should be thrown.
     **/
    public void Var003()
    {
        try
            {
            // create another reference to the spooled file 
            SpooledFile splF = new SpooledFile(systemObject_,
                                           null,
                                           1,
                                           "A Job Name",
                                           "A Job User",
                                           "A Job Number");
            failed("Could use null name."+splF);
            }
        catch (Exception e)
            {
            // Check for NullPointerException
            if (exceptionIs(e, "NullPointerException"))  succeeded();  // Note: This variation will be successful.
            else failed (e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests construction of a SpooledFile object with 0 number parameter.
     *      A ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var004()
    {
        try
            {
            // create another reference to the spooled file 
            SpooledFile splF = new SpooledFile(systemObject_,
                                           "A Name",
                                           0,
                                           "A Job Name",
                                           "A Job User",
                                           "A Job Number");
            assertCondition(true, "Created "+splF); 
            
            
            }
        catch (Exception e)
            {
            // Check for ExtendedIllegalArgumentException
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))  succeeded();  // Note: This variation will be successful.
            else failed (e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests construction of a SpooledFile object with null job name parameter.
     *      A NullPointerException should be thrown.
     **/
    public void Var005()
    {
        try
            {
            // create another reference to the spooled file 
            SpooledFile splF = new SpooledFile(systemObject_,
                                           "A Name",
                                           1,
                                           null,
                                           "A Job User",
                                           "A Job Number");
            failed("Could use null job name."+splF);
            }
        catch (Exception e)
            {
            // Check for NullPointerException
            if (exceptionIs(e, "NullPointerException"))  succeeded();  // Note: This variation will be successful.
            else failed (e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests construction of a SpooledFile object with null job user parameter.
     *      A NullPointerException should be thrown.
     **/
    public void Var006()
    {
        try
            {
            // create another reference to the spooled file 
            SpooledFile splF = new SpooledFile(systemObject_,
                                           "A Name",
                                           1,
                                           "A Job Name",
                                           null,
                                           "A Job Number");
            failed("Could use null job user."+splF);
            }
        catch (Exception e)
            {
            // Check for NullPointerException
            if (exceptionIs(e, "NullPointerException"))  succeeded();  // Note: This variation will be successful.
            else failed (e, "Unexpected exception");
            }

    } // end Var006

    /**
     * Tests construction of a SpooledFile object with null job number parameter.
     *      A NullPointerException should be thrown.
     **/
    public void Var007()
    {
        try
            {
            // create another reference to the spooled file 
            SpooledFile splF = new SpooledFile(systemObject_,
                                           "A Name",
                                           1,
                                           "A Job Name",
                                           "A Job User",
                                           null);
            failed("Could use null job number."+splF);
            }
        catch (Exception e)
            {
            // Check for NullPointerException
            if (exceptionIs(e, "NullPointerException"))  succeeded();  // Note: This variation will be successful.
            else failed (e, "Unexpected exception");
            }

    } // end Var007

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

    /////////////////////
    // Private methods //
    /////////////////////

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

} // end NPSplFCtorTestcase class


