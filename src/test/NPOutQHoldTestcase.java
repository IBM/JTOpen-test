///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQHoldTestcase.java
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
import java.util.Enumeration;
import com.ibm.as400.access.*;

/**
 * Testcase NPOutQHoldTestcase.
 * Tests the hold method on OutputQueue class.
 **/
public class NPOutQHoldTestcase extends Testcase
{
    // listener invoked?
    boolean clearedInvoked;
    boolean heldInvoked;
    boolean releasedInvoked;

    /**
     * Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQHoldTestcase(AS400            systemObject,
			      Vector           variationsToRun,
			      int              runMode,
			      FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPOutQHoldTestcase", 5,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQHoldTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
	{
            // create HOLDTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/HOLDTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/HOLDTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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
         if (cmd.run("CLROUTQ OUTQ(NPJAVA/HOLDTST)") == false)
             {
             output_.println("Could not clear output queue we created. "
                             + cmd.getMessageList()[0].getID()
                             + ": " + cmd.getMessageList()[0].getText());
             }

         // delete the output queue we created.
         if (cmd.run("DLTOUTQ OUTQ(NPJAVA/HOLDTST)") == false)
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
     * Tests holding of an output queue.
     **/
    public void Var001()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/HOLDTST.OUTQ");

	    // check the output queue status
	    if (!outQ.getStringAttribute(PrintObject.ATTR_OUTQSTS).trim().equals("RELEASED"))
	    {
		// release the output queue
		outQ.release();
	    }

	    // holds the output queue
	    outQ.hold();

	    // check the output queue status to make sure it is held
	    if (!outQ.getStringAttribute(PrintObject.ATTR_OUTQSTS).trim().equals("HELD"))
	    {
		failed("Could not hold output queue");
	    }
	    else
	    {
		// release the output queue
		outQ.release();

		succeeded();  // Note: This variation will be successful.
	    }

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var001

    /**
     * Tests holding a held output queue. CPF3426 message should be received.
     **/
    public void Var002()
    {
	
	try
	{
	    // create a output queue object
	    OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/HOLDTST.OUTQ");

	    // check the output queue status
	    if (!outQ.getStringAttribute(PrintObject.ATTR_OUTQSTS).trim().equals("RELEASED"))
	    {
		// release the output queue
		outQ.release();
	    }

	    // holds the output queue
	    outQ.hold();
            
	    try
	    {
		// attempt to hold an output queue that is already held
		// expect CPF3426
		outQ.hold();

                failed("Could hold a held output queue");

	    }

	    catch (AS400Exception e)
	    {
		AS400Message msg = e.getAS400Message();
		if (!msg.getID().trim().equals("CPF3426"))
		{
		    failed("Incorrect message received for holding a held output queue. ID="+msg.getID());
		}
		else
		{
		    // release the output queue
		    outQ.release();

		    succeeded();  // Note: This variation will be successful.
		}
	    }

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var002

    /**
     * Tests holding of an output queue object that is not in a valid state
     **/
    public void Var003()
    {
	try
	{
            // create a output queue object using default constructor
            OutputQueue outQ = new OutputQueue();

            // hold the output queue
	    outQ.hold();
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

    } // end Var003

    /**
     * Tests that a output queue held event is fired when the
     * output queue is held.
     **/
    public void Var004()
    {
        // Define inner class to listen for output queue events
        class OutQListener implements OutputQueueListener
        {
            public void outputQueueCleared( OutputQueueEvent evt )
            {
                // allow any event ID to catch mistakes
                clearedInvoked = true;
            }

            public void outputQueueHeld( OutputQueueEvent evt )
            {
                // make sure the event ID is correct
                if( evt.getID() == OutputQueueEvent.HELD )
                    {
                    heldInvoked = true;
                    }
            }

            public void outputQueueReleased( OutputQueueEvent evt )
            {
                // allow any event ID to catch mistakes
                releasedInvoked = true;
            }
        }
        OutQListener outQListener = new OutQListener();

	try
	{
	    // create a output queue object
	    OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/HOLDTST.OUTQ");

	    // check the output queue status
	    if (!outQ.getStringAttribute(PrintObject.ATTR_OUTQSTS).trim().equals("RELEASED"))
	    {
		// release the output queue
		outQ.release();
	    }

            // reset our flags
            clearedInvoked = false;
            heldInvoked = false;
            releasedInvoked = false;

            // add the listener
            outQ.addOutputQueueListener(outQListener);

	    // holds the output queue
	    outQ.hold();

            if( heldInvoked & !clearedInvoked & !releasedInvoked )
                {
                succeeded(); 
                }
            else
                {
                failed("output queue held event was not fired");
                }

            // release the output queue
            outQ.release();

            // remove the listener
            outQ.removeOutputQueueListener(outQListener);
        } 

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var004

    /**
     * Tests that an output queue listener is actually removed
     **/
    public void Var005()
    {
        // Define inner class to listen for output queue events
        class OutQListener implements OutputQueueListener
        {
            public void outputQueueCleared( OutputQueueEvent evt )
            {
                // allow any event ID to catch mistakes
                clearedInvoked = true;
            }

            public void outputQueueHeld( OutputQueueEvent evt )
            {
                // allow any event ID to catch mistakes
                heldInvoked = true;
            }

            public void outputQueueReleased( OutputQueueEvent evt )
            {
                // allow any event ID to catch mistakes
                releasedInvoked = true;
            }
        }
        OutQListener outQListener = new OutQListener();

	try
	{
	    // create a output queue object
	    OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/HOLDTST.OUTQ");

	    // check the output queue status
	    if (!outQ.getStringAttribute(PrintObject.ATTR_OUTQSTS).trim().equals("RELEASED"))
	    {
		// release the output queue
		outQ.release();
	    }

            // reset our flags
            clearedInvoked = false;
            heldInvoked = false;
            releasedInvoked = false;

            // add the listener
            outQ.addOutputQueueListener(outQListener);

            // remove the listener
            outQ.removeOutputQueueListener(outQListener);

            // hold the output queue
	    outQ.hold();

            if( heldInvoked | clearedInvoked | releasedInvoked )
                {
                failed("Output queue listener was not removed");
                }
            else
                {
                succeeded();
                }

            // release the output queue
            outQ.release();

            // remove the listener again, should be OK.
            outQ.removeOutputQueueListener(outQListener);
        } 

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var005

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

} // end NPOutQHoldTestcase class


