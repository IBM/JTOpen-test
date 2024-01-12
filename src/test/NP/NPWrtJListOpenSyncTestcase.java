///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPWrtJListOpenSyncTestcase.java
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
 Testcase NPWrtJListOpenSyncTestcase.
 **/
public class NPWrtJListOpenSyncTestcase extends Testcase
{
    // the printer device name
    String printer_ = null;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPWrtJListOpenSyncTestcase(AS400            systemObject,
                                      Vector           variationsToRun,
                                      int              runMode,
                                      FileOutputStream fileOutputStream,
                                      
                                      String           printer)
      throws IOException
    {
        // $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPWrtJListOpenSyncTestcase", 2,
	      variationsToRun, runMode, fileOutputStream);

        if (printer != null)
            {
            // set the printer variable
            printer_ = printer;
            }
        else
            {
            throw new IOException("The -misc flag must be set to a valid printer device name.");
            }
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPWrtJListOpenSyncTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);
        boolean fWriterEnded = false;

        try
            {

            // create OPNSTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/OPNSTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/OPNSTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
                {
                failed("Could not grant *public authority to output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            // end the normal writer to printer
            if (cmd.run("ENDWTR WTR("+printer_+") OPTION(*IMMED)") == false)
                {
                // if the error we go back was not no active writer message
                // exit
                if (!cmd.getMessageList()[0].getID().trim().equals("CPF3313"))
                    {
                    failed("Could not end the writer to "+printer_+". "
                           + cmd.getMessageList()[0].getID()
                           + ": " + cmd.getMessageList()[0].getText());
                    return;
                    }
                }

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(2000,0);

            fWriterEnded = true;

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

            // clear the output queue we created.
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/OPNSTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/OPNSTST)") == false)
                {
                output_.println("Could not delete output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // if we stopped a writer then we will restart it.
            if (fWriterEnded == true)
                {
                if (cmd.run("STRPRTWTR DEV("+printer_+")") == false)
                    {
                    output_.println("Could not start writer that we ended. "
                                    + cmd.getMessageList()[0].getID()
                                    + ": " + cmd.getMessageList()[0].getText());
                    }
                }
            } // end try block

        catch( Exception e )
            {
            failed(e, "Unexpected exception");
            }

    } // end run method

    /**
     * Tests listing writer job synchronously.
     **/
    public void Var001()
    {
        try
            {
            // start a writer job
            WriterJob wrtJ = startWriter();

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            // create a Writer Job List object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // set the output queue filter 
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/OPNSTST.OUTQ");

            // now try to build list synchrously
            wrtJList.openSynchronously();

            int listed = 0, size;
            size = wrtJList.size();
            String wrtJName = null;

            // check to see if we got at least one writer job
            if (size >= 1)
                {
                boolean fSuccess = false;

                // check the writer job we go back
                while (listed < size)
                    {
                    WriterJob wrtJob = (WriterJob)wrtJList.getObject(listed++);
                    wrtJName = wrtJob.getName();

                    if (wrtJName.equals("JAVAPRINT"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad writer job:"+wrtJName);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many job(s) were listed
                output_.println(listed + " writer job(s) listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect writer job listed: " + wrtJName);
                    }

                } // end if size
            else 
                {
                failed("No writer job(s) found.");
                }

            // close the list
            wrtJList.close();

            // end the writer job we started
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(2000,0);

            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests listing writer job synchronously with a
     * WriterJobList object in an invalid state
     **/
    public void Var002()
    {
        try
            {
            // create a Writer Job List object
            WriterJobList wrtJList = new WriterJobList();

            // set the output queue filter 
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/OPNSTST.OUTQ");

            // now try to build list synchrously
            wrtJList.openSynchronously();
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

    ////////////////////
    // Private method //
    ////////////////////

    // This method starts a writer job
    private WriterJob startWriter()
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException
    {
        // create a printer device object using valid system name and printer name
        Printer prtD = new Printer(systemObject_, printer_);

        // create a print parm list
        PrintParameterList pList = new PrintParameterList();

        // set some parms
        pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");

        // create an output queue object using valid system name and output queue name
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/OPNSTST.OUTQ");

        // start a writer job
        return WriterJob.start(systemObject_, prtD, pList, outQ);

    } // end startWriter

} // end NPWrtJListOpenSyncTestcase class


