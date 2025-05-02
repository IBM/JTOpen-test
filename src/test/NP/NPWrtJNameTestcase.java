///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPWrtJNameTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.PrintParameterList;
import com.ibm.as400.access.Printer;
import com.ibm.as400.access.WriterJob;

import test.Testcase;

/**
 Testcase NPWrtJNameTestcase.
 **/
public class NPWrtJNameTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPWrtJNameTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPWrtJNameTestcase(AS400            systemObject,
                              Vector<String> variationsToRun,
                              int              runMode,
                              FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPWrtJNameTestcase", 1,
	      variationsToRun, runMode, fileOutputStream);

    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPWrtJNameTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
            {
            // make sure JAVAPRINT printer device description exists
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("QSYS/CRTDEVPRT DEVD(JAVAPRINT) DEVCLS(*LCL) TYPE(*IPDS) MODEL(0) PORT(5) SWTSET(0) ONLINE(*NO) FONT(011)") == false)
                {
                failed("Could not create a printer device description. "
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

            // delete the printer device description that we created
            if (cmd.run("QSYS/DLTDEVD JAVAPRINT") == false)
                {
                output_.println("Could not delete the printer device description we created. "
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
     * Tests the getName method on WriterJob class.
     **/
    public void Var001()
    {
       try
           {
           // create a printer device object using valid system name and printer name
           Printer prtD = new Printer(systemObject_, "JAVAPRINT");

           // create a print parm list
           PrintParameterList pList = new PrintParameterList();

           // set some parms
           pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");

           // start a writer job using valid system name and printer name
           WriterJob wrtJ = WriterJob.start(systemObject_, prtD, pList, null);

           // go to sleep for a bit to allow the writer to start
           Thread.sleep(2000,0);

           // call a method to ensure we have a good object
           wrtJ.update();

           if (wrtJ.getName().trim().equals("JAVAPRINT")) succeeded();
           else failed("Could not retrive writer job name.  Retrieved name:"+wrtJ.getName() + ". Looking for: JAVAPRINT");

           // end the writer we started
           wrtJ.end("*IMMED");

           // go to sleep for a bit to allow the writer to stop
           Thread.sleep(2000,0);

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

} // end NPWrtJNameTestcase class


