///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtParmGetStrTestcase.java
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
 Testcase NPPrtParmGetStrTestcase.
 **/
public class NPPrtParmGetStrTestcase extends Testcase
{

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtParmGetStrTestcase(AS400            systemObject,
				   Vector           variationsToRun,
				   int              runMode,
				   FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPPrtParmGetStrTestcase", 2,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtParmGetStrTestcase");
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
     Tests getting a string parameter value.
     **/
    public void Var001()
    {
	try
	{
	    // create the print parameter list
	    PrintParameterList pList = new PrintParameterList();

	    // set a string attribute
	    pList.setParameter(PrintObject.ATTR_DEVTYPE,"6380");

	    // get an integer attribute
	    String attr = pList.getStringParameter(PrintObject.ATTR_DEVTYPE);

	    // compare
	    if (attr.trim().equals("6380"))
	    {
		succeeded();  // Note: This variation will be successful.
	    }
	    else
	    {
		failed("Could not get a string attribute");
	    }
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var001

    /**
     Tests getting an invalid string parameter value.
     Expects ExtendedIllegalArgumentException
     **/
    public void Var002()
    {
	try
	{
	    // create the print parameter list
	    PrintParameterList pList = new PrintParameterList();

	    // get the invalid string attribute
	    String attr = pList.getStringParameter(0xFFFF);

	    failed("Could use invalid string attribute");

	} // end try block

	catch (Exception e)
	{
	    // Check for ExtendedIllegalArgumentException
	    if (exceptionIs(e, "ExtendedIllegalArgumentException")) succeeded();  // Note: This variation will be successful.
	    else failed (e, "Unexpected exception");
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

} // end NPPrtParmGetStrTestcase class


