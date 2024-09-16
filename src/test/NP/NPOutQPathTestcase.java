///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQPathTestcase.java
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
 * Testcase NPOutQPathTestcase.
 **/
public class NPOutQPathTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPOutQPathTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     * Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQPathTestcase(AS400            systemObject,
                              Vector           variationsToRun,
                              int              runMode,
                              FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPOutQPathTestcase", 4,
              variationsToRun, runMode, fileOutputStream);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQPathTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
            {
            // create PATHTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/PATHTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/PATHTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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

            // run variation 3
            if ((allVariations || variationsToRun_.contains("3")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(3);
                Var003();
                }

            // run variation 4
            if ((allVariations || variationsToRun_.contains("4")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
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
	    // and uncomment the block of code
	    //
/* $$$ TO DO $$$ - delete this line
	    if ((allVariations || variationsToRun_.contains("X")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	        {
		setVariation(X);
		VarXXX();
	        }
$$$ TO DO $$$ - delete this line */

            // clear the output queue we created.
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/PATHTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/PATHTST)") == false)
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
     Tests retrieving the output queue Integrated File System pathname.
     **/
    public void Var001()
    {
        try
            {
	    // create an output queue object using valid system name and queue name
	    OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/PATHTST.OUTQ");

	    if (outQ.getPath().trim().equals("/QSYS.LIB/NPJAVA.LIB/PATHTST.OUTQ")) succeeded();
	    else failed("Could not retrive output queue Integrated File System pathname");

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting the output queue Integrated File System pathname.
     **/
    public void Var002()
    {
	try
	{
            // create an output queue object using default constructor
            OutputQueue outQ = new OutputQueue();

            outQ.setSystem(systemObject_);
            outQ.setPath("/QSYS.LIB/NPJAVA.LIB/PATHTST.OUTQ");

	    if (outQ.getPath().trim().equals("/QSYS.LIB/NPJAVA.LIB/PATHTST.OUTQ")) succeeded();
            else failed("Could not set/get output queue IFS pathname");
	    
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var002

    /**
     * Tests setting the output queue Integrated File System pathname
     * to null.
     **/
    public void Var003()
    {
	try
	{
            // create an output queue object using default constructor
            OutputQueue outQ = new OutputQueue();

            outQ.setPath(null);
            failed("Could set IFS pathname to null");

	} // end try block

        catch (NullPointerException e)
	{
            succeeded();
	}

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var003

    /**
     * Tests getting the output queue IFS path name before setting it.
     **/
    public void Var004()
    {
	try
	{
            // create an output queue object using the default constructor
            OutputQueue outQ = new OutputQueue();

            if( outQ.getPath().length() == 0 )
            {
                succeeded();
            }
            else
            {
                failed("IFS pathname was not set, expecting empty string");
            }

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

} // end NPOutQPathTestcase class


