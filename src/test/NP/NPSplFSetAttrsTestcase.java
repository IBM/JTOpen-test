///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFSetAttrsTestcase.java
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
 Testcase NPSplFSetAttrsTestcase.
 **/
public class NPSplFSetAttrsTestcase extends Testcase
{
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFSetAttrsTestcase(AS400            systemObject,
				  Vector           variationsToRun,
				  int              runMode,
				  FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPSplFSetAttrsTestcase", 5,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFSetAttrsTestcase");
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
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
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

	} // end try block

	catch( Exception e )
	{
	    failed(e, "Unexpected exception");
	}

    } // end run method

    /**
     * Tests setting a float attribute on a spooled file.
     **/
    public void Var001()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();

            // create a PrintParameterList object
            PrintParameterList pList = new PrintParameterList();

            // set a float attribute
            pList.setParameter(PrintObject.ATTR_BKOVL_DWN,(float)10.0);

            // set the attributes
            splF.setAttributes(pList);

            // verify it actually got set
            if (splF.getFloatAttribute(PrintObject.ATTR_BKOVL_DWN).floatValue() == 10.0)
                {
                succeeded();
                }
            else failed("Could not set a float attribute.");

            // delete the spooled file we created
            splF.delete();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting an integer attribute on a spooled file.
     **/
    public void Var002()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();

            // create a PrintParameterList object
            PrintParameterList pList = new PrintParameterList();

            // set a integer attribute
            pList.setParameter(PrintObject.ATTR_COPIES,10);

            // set the attributes
            splF.setAttributes(pList);

            // verify it actually got set
            if (splF.getIntegerAttribute(PrintObject.ATTR_COPIES).intValue() == 10)
                {
                succeeded();
                }
            else failed("Could not set an integer attribute.");

            // delete the spooled file we created
            splF.delete();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting a string attribute on a spooled file.
     **/
    public void Var003()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();

            // create a PrintParameterList object
            PrintParameterList pList = new PrintParameterList();

            // set a float attribute
            pList.setParameter(PrintObject.ATTR_FORMTYPE,"JAVAFORM");

            // set the attributes
            splF.setAttributes(pList);

            // verify it actually got set
            if (splF.getStringAttribute(PrintObject.ATTR_FORMTYPE).trim().equals("JAVAFORM"))
                {
                succeeded();
                }
            else failed("Could not set a string attribute.");

            // delete the spooled file we created
            splF.delete();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests setting a null attribute on a spooled file.
     * Expect NullPointerException
     **/
    public void Var004()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();

            try
                {
                // set the attributes
                splF.setAttributes(null);

                failed("Could set a null attribute.");
                }
            catch(Exception e)
                {
                if (exceptionIs(e, "NullPointerException"))
                    {
                    succeeded();
                    }
                else failed (e, "Unexpected exception");
                }

            // delete the spooled file we created
            splF.delete();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests setting a string, float, and integer attributes on a spooled file.
     **/
    public void Var005()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();

            // create a PrintParameterList object
            PrintParameterList pList = new PrintParameterList();

            // set a string attribute
            pList.setParameter(PrintObject.ATTR_FORMTYPE,"JAVAFORM");

            // set a integer attribute
            pList.setParameter(PrintObject.ATTR_COPIES,10);

            // set a float attribute
            pList.setParameter(PrintObject.ATTR_BKOVL_DWN,(float)10.0);

            // set the attributes
            splF.setAttributes(pList);

            // verify it actually got set
            if ( (splF.getStringAttribute(PrintObject.ATTR_FORMTYPE).trim().equals("JAVAFORM")) &&
                 (splF.getIntegerAttribute(PrintObject.ATTR_COPIES).intValue() == 10) &&
                 (splF.getFloatAttribute(PrintObject.ATTR_BKOVL_DWN).floatValue() == 10.0) )
                {
                succeeded();
                }
            else failed("Could not set multiple attributes on spooled file.");

            // delete the spooled file we created
            splF.delete();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

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

} // end NPSplFSetAttrsTestcase class


