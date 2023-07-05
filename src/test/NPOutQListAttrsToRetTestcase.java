///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQListAttrsToRetTestcase.java
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
 Testcase NPOutQListAttrsToRetTestcase.
 **/
public class NPOutQListAttrsToRetTestcase extends Testcase

{
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQListAttrsToRetTestcase(AS400            systemObject,
                                        Vector           variationsToRun,
                                        int              runMode,
                                        FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPOutQListAttrsToRetTestcase", 5,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQListAttrsToRetTestcase");
        boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create LISTATST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/LISTATST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/LISTATST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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

            // run variation 5
            if ((allVariations || variationsToRun_.contains("5")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
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
            // run variation X
           if ((allVariations || variationsToRun_.contains("X")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(X);
                VarXXX();
                }
$$$ TO DO $$$ - delete this line */

            // clear the output queue we created.
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/LISTATST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/LISTATST)") == false)
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
     * Tests setting valid attributes to retrieve on an Output Queue List.
     **/
    public void Var001()
    {
        try
            {
            // create an Output Queue list object
            OutputQueueList outQList = new OutputQueueList(systemObject_);

            // set the queue filter 
            outQList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTATST.OUTQ");

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_OUTQSTS;
            attrs[1] = PrintObject.ATTR_NUMFILES;
            attrs[2] = PrintObject.ATTR_DESCRIPTION;

            // set the attributes to retrieve
            outQList.setAttributesToRetrieve(attrs);

            // list the output queue
            outQList.openSynchronously();

            Enumeration e = outQList.getObjects();

            // check to see if we got our output queue
            if (outQList.size() == 1)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    OutputQueue outQ = (OutputQueue)e.nextElement();
                    if ( (outQ.getStringAttribute(PrintObject.ATTR_OUTQSTS) != null) &&
                         (outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES) != null) &&
                         (outQ.getStringAttribute(PrintObject.ATTR_DESCRIPTION) != null) )
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        output_.println("Bad attributes on "+outQ.getPath()+" :"+
                                        outQ.getStringAttribute(PrintObject.ATTR_OUTQSTS)+
                                        ", "+outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES)+
                                        ", "+outQ.getStringAttribute(PrintObject.ATTR_DESCRIPTION));
                        break;
                        }
                    } // end while

                // indicate how many output queues were listed
                output_.println(outQList.size() + " Output Queues listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Failed to retrieve appropriate attributes");
                    }
                } // if size
            else 
                {
                failed("Incorrect number of Output queues found in list.");
                }

            // close the list
            outQList.close();

            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting null attributes to retrieve on an Output Queue List.
     * Expects NullPointerException
     **/
    public void Var002()
    {
        try
            {
            // create an Output Queue list object
            OutputQueueList outQList = new OutputQueueList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                outQList.openAsynchronously();
            }
            else {
                outQList.openSynchronously();
            }

            // set the attributes to retrieve
            outQList.setAttributesToRetrieve(null);

            failed("Could set null attributes to retrieve.");

            outQList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back a null pointer exception
            if (exceptionIs(e, "NullPointerException"))  succeeded();
            else failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting the attributes to retrieve on an open Output Queue List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var003()
    {
        try
            {
            // create an Output Queue list object
            OutputQueueList outQList = new OutputQueueList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                outQList.openAsynchronously();
            }
            else {
                outQList.openSynchronously();
            }

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_OUTQSTS;
            attrs[1] = PrintObject.ATTR_NUMFILES;
            attrs[2] = PrintObject.ATTR_DESCRIPTION;

            // set the attributes to retrieve
            outQList.setAttributesToRetrieve(attrs);

            failed("Could set attributes to retrieve on an opened list.");

            outQList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back an extened illegal state exception
            if (exceptionIs(e, "ExtendedIllegalStateException"))  succeeded();
            else failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests resetting attributes to retrieve on an Output Queue List.
     **/
    public void Var004()
    {
        try
            {
            // create an Output Queue list object
            OutputQueueList outQList = new OutputQueueList(systemObject_);

            // set the queue filter 
            outQList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTATST.OUTQ");

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_OUTQSTS;
            attrs[1] = PrintObject.ATTR_NUMFILES;
            attrs[2] = PrintObject.ATTR_DESCRIPTION;

            // set the attributes to retrieve
            outQList.setAttributesToRetrieve(attrs);

            // list the output queue
            outQList.openSynchronously();

            // close the list
            outQList.close();

            // reset the attributes 
            outQList.resetAttributesToRetrieve();

            // set the queue filter 
            outQList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTATST.OUTQ");
            
            // list the output queue
            outQList.openSynchronously();

            Enumeration e = outQList.getObjects();

            // check to see if we got some output queue
            if (outQList.size() == 1)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    OutputQueue outQ = (OutputQueue)e.nextElement();

                    if ( (outQ.getStringAttribute(PrintObject.ATTR_AUTHCHCK) != null) &&
                         (outQ.getStringAttribute(PrintObject.ATTR_DISPLAYANY) != null) )
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        output_.println("Bad attributes on "+outQ.getPath()+" :"+
                                        outQ.getStringAttribute(PrintObject.ATTR_AUTHCHCK)+
                                        ", "+outQ.getStringAttribute(PrintObject.ATTR_DISPLAYANY));
                        break;
                        }
                    } // end while

                // indicate how many output queues were listed
                output_.println(outQList.size() + " Output Queues listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Failed to reset attributes");
                    }
                } // if size
            else 
                {
                failed("Incorrect number of output queues listed.");
                }

            // close the list
            outQList.close();

            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests resetting the attributes to retrieve on an open Output Queue List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var005()
    {
        try
            {
            // create an Output Queue list object
            OutputQueueList outQList = new OutputQueueList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                outQList.openAsynchronously();
            }
            else {
                outQList.openSynchronously();
            }

            // set the attributes to retrieve
            outQList.resetAttributesToRetrieve();

            failed("Could reset attributes to retrieve on an opened list.");

            outQList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back an extended illegal state exception
            if (exceptionIs(e, "ExtendedIllegalStateException"))  succeeded();
            else failed(e, "Unexpected exception");
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

} // end NPOutQListAttrsToRetTestcase class


