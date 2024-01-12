///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQListOpenAsyncTestcase.java
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
 * Testcase NPOutQListOpenAsyncTestcase.
 * Tests the listing of output queues.
 **/
public class NPOutQListOpenAsyncTestcase extends Testcase
{
    /**
     * Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQListOpenAsyncTestcase(AS400            systemObject,
                                       Vector           variationsToRun,
                                       int              runMode,
                                       FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPOutQListOpenAsyncTestcase", 4,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQListOpenAsyncTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
	{
            // create LSTOATST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/LSTOATST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/LSTOATST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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

            if ((allVariations || variationsToRun_.contains("2")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(2);
                Var002();
                }

            if ((allVariations || variationsToRun_.contains("3")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(3);
                Var003();
                }

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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/LSTOATST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/LSTOATST)") == false)
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
     * Lists output queue(s) asynchronously using the waitForListToComplete method
     **/
    public void Var001()
    {
	try
	{
	    // create a output queue list object
	    OutputQueueList outQList = new OutputQueueList(systemObject_);

	    outQList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.OUTQ");

	    // open the output queue list
	    outQList.openAsynchronously();

            // wait for the list to complete
            outQList.waitForListToComplete();

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

            // close the list
            outQList.close();
	    System.gc();

	} // end try block
    
   catch (ExtendedIllegalStateException e)                               // @A1A
   {
       if ( (systemObject_.isThreadUsed() == false) &&
            (e.getReturnCode() ==
               ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
       {
           // for no threads, openAsynchronous() can not be used.
           succeeded();
       }
       else
       {
           failed(e, "Unexpected exception");
       }
   }

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var001

    /**
     * Lists output queue(s) asynchronously using the waitForItem method
     **/
    public void Var002()
    {
	try
	{
	    // create a output queue list object
	    OutputQueueList outQList = new OutputQueueList(systemObject_);

	    outQList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.OUTQ");

	    // open the output queue list
	    outQList.openAsynchronously();

            // wait for the 1st item on the list to complete
            outQList.waitForItem(1);

            int listed = 0, size;
            size = outQList.size();
            String outQPath = null;

            // check to see if we got at least one output queue
            if (size >= 1)
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

            // close the list
            outQList.close();
	    System.gc();

	} // end try block

   catch (ExtendedIllegalStateException e)                               // @A1A
   {
       if ( (systemObject_.isThreadUsed() == false) &&
            (e.getReturnCode() ==
               ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
       {
           // for no threads, openAsynchronous() can not be used.
           succeeded();
       }
       else
       {
           failed(e, "Unexpected exception");
       }
   }

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var002

    /**
     * Lists output queue(s) asynchronously using the waitForItem method.
     * Wait for an item number greater than what is available.
     **/
    public void Var003()
    {
	try
	{
	    // create a output queue list object
	    OutputQueueList outQList = new OutputQueueList(systemObject_);

	    outQList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.OUTQ");

	    // open the output queue list
	    outQList.openAsynchronously();

            // wait for the 100th item on the list to complete, there
            // should not be 100 items on the list.
            outQList.waitForItem(100);

            int listed = 0, size;
            size = outQList.size();
            String outQPath = null;

            // check to see if we got at least one output queue
            if (size >= 1)
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

            // close the list
            outQList.close();
	    System.gc();

	} // end try block

   catch (ExtendedIllegalStateException e)                               // @A1A
   {
       if ( (systemObject_.isThreadUsed() == false) &&
            (e.getReturnCode() ==
               ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
       {
           // for no threads, openAsynchronous() can not be used.
           succeeded();
       }
       else
       {
           failed(e, "Unexpected exception");
       }
   }

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var003

    /**
     * Lists Output Queue(s) asynchronously using the waitForItem method with invalid number.
     * Expects ExtendedIllegalArgumentException
     **/
    public void Var004()
    {
        try
            {
            // create a output queue list object
            OutputQueueList outQList = new OutputQueueList(systemObject_);

            outQList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.OUTQ");

            // now try to build output queue list asynchrously
            outQList.openAsynchronously();

            try
                {
                // use an invalid number to wait for
                outQList.waitForItem(0);

                failed("Could use an invalid number to wait for.");
                }
            catch (ExtendedIllegalArgumentException e)
                {
                succeeded();
                }

            // close the list
            outQList.close();
            System.gc();

            } // end try block

        catch (ExtendedIllegalStateException e)                               // @A1A
            {
            if ( (systemObject_.isThreadUsed() == false) &&
                 (e.getReturnCode() ==
                    ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
                {
                // for no threads, openAsynchronous() can not be used.
                succeeded();
                }
            else
                {
                failed(e, "Unexpected exception");
                }
            }

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


} // end NPOutQListOpenAsyncTestcase class


