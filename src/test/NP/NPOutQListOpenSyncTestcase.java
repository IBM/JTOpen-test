///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQListOpenSyncTestcase.java
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
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.OutputQueue;
import com.ibm.as400.access.OutputQueueList;

import test.Testcase;

/**
 * Testcase NPOutQListOpenSyncTestcase.
 **/
public class NPOutQListOpenSyncTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPOutQListOpenSyncTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     * Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQListOpenSyncTestcase(AS400            systemObject,
                                      Vector<String> variationsToRun,
                                      int              runMode,
                                      FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPOutQListOpenSyncTestcase", 2,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQListOpenSyncTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
	{
            // create LSTOSTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("QSYS/CRTOUTQ OUTQ(NPJAVA/LSTOSTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("QSYS/GRTOBJAUT OBJ(NPJAVA/LSTOSTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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
            if (cmd.run("QSYS/CLROUTQ OUTQ(NPJAVA/LSTOSTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("QSYS/DLTOUTQ OUTQ(NPJAVA/LSTOSTST)") == false)
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
     * Tests listing a output queue synchronously.
     **/
    public void Var001()
    {
	try
	{
	    // create a printer list object
	    OutputQueueList outQList = new OutputQueueList(systemObject_);

	    outQList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.OUTQ");
	    outQList.openSynchronously();

            int listed = 0, size;
            size = outQList.size();
            String outQPath = null;

            // check to see if we got some output queues
            if (size > 0)
                {
                boolean fSuccess = false;

                // check the output queues we go back
                while (listed < size)
                    {
                    OutputQueue outQ = (OutputQueue)outQList.getObject(listed++);
                    outQPath = outQ.getPath();
                    if (outQPath.startsWith("/QSYS.LIB/NPJAVA.LIB"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many output queues were listed
                output_.println(listed + " Output queues listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect output queue listed: " + outQPath);
                    }
                } // end if size
            else 
                {
                failed("No output queues found in NPJAVA Library.");
                }

	    outQList.close();
	    System.gc();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var001

    /**
     * Tests listing a output queue synchronously with the
     * OutputQueueList object in an invalid state
     **/
    public void Var002()
    {
	try
            {
            // create a output queue list object using default constructor
            OutputQueueList outQList = new OutputQueueList();

	    outQList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.OUTQ");
	    outQList.openSynchronously();
	    outQList.close();
            failed("Should have gotten ExtendedIllegalStateException");
            }

        catch (ExtendedIllegalStateException e)
            {
            // We got the exception we were expecting.
            succeeded(); 
            }

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

} // end NPOutQListOpenSyncTestcase class


