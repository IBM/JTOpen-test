///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFGetMessageTestcase.java
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
import java.beans.PropertyVetoException;
import com.ibm.as400.access.*;

/**
 Testcase NPSplFGetMessageTestcase.
 **/
public class NPSplFGetMessageTestcase extends Testcase
{
    // the printer device name
    String printer_ = null;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFGetMessageTestcase(AS400            systemObject,
                                    Vector           variationsToRun,
                                    int              runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String           printer)
      throws IOException
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPSplFGetMessageTestcase", 2,
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
        output_.println("NPPrintTest::NPSplFGetMessageTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);
        boolean fWriterEnded = false;

        try
        {
          // create MSGTST output queue
          CommandCall cmd = new CommandCall(systemObject_);
          if (cmd.run("CRTOUTQ OUTQ(NPJAVA/MSGTST) AUTCHK(*DTAAUT)") == false)
          {
            failed("Could not create an output queue. "
                + cmd.getMessageList()[0].getID()
                + ": " + cmd.getMessageList()[0].getText());

            if (cmd.getMessageList()[0].getID().trim().equals("CPF3353"))
            {
              failed("You will need to manually delete output queue MSGTST in library NPJAVA and then" +
              " restart the test.  ");
            }
            return;
            
          }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/MSGTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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
            
            //End JAVAPRINT writer
            if (cmd.run("ENDWTR WTR(JAVAPRINT) OPTION(*IMMED)") == false)
            {
            // if the error we go back was not no active writer message
            // exit
            if (!cmd.getMessageList()[0].getID().trim().equals("CPF3313"))
                {
                failed("Could not end the writer to JAVAPRINT."
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }
            }
            
            // clear the output queue we created.
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/MSGTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/MSGTST)") == false)
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
     * Tests retrieving a spooled file message.
     **/
    public void Var001()
    {
        try
            {
            // create a output queue object
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/MSGTST.OUTQ");

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms to clear the entire output queue
            pList.setParameter(PrintObject.ATTR_JOBUSER, "*ALL");
            pList.setParameter(PrintObject.ATTR_FORMTYPE, "*ALL");
            pList.setParameter(PrintObject.ATTR_USERDATA, "*ALL");

            // clear the output queue
            outQ.clear(pList);

            // create a spooled file to our output queue with a funky form definition
            SpooledFile splF = createSpooledFile(outQ);

            // start a writer to our output queue which will cause a message to be
            // send to the spooled file because of the funky form definition
            WriterJob wrtJ = startWriter(splF);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            boolean fMsgWaiting = false;

            if (!splF.getStringAttribute(PrintObject.ATTR_SPLFSTATUS).trim().equals("*MESSAGE"))
                {
                int i = 0;
                output_.println("Waiting for a message.");
                while ((!splF.getStringAttribute(PrintObject.ATTR_SPLFSTATUS).trim().equals("*MESSAGE")) &&
                       (i<2000))
                    {
                    splF.update();                              
                    Thread.sleep(200, 0);
                    output_.print(".");
                    output_.flush();
                    i++;
                    }
                if (i<2000) fMsgWaiting = true;
                output_.println(" ");
               }
            else fMsgWaiting = true;

            // if we were able to get the spooled file to msg waiting status
            if (fMsgWaiting != false)
                {

                // get the message from the spooled file
                AS400Message msg = splF.getMessage();

                // answer the spooled file message with default reply
                // which is to cancel the writer
                splF.answerMessage(msg.getDefaultReply());

                succeeded();
                } // end if fMsgWaiting
            else failed("Unable to cause msg waiting status on spooled file.");

            // delete the spooled file we created
            splF.delete();

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(8000,0);

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }
    } // end Var001

    /**
     * Tests retrieving a spooled file message when there is not one waiting.
     * Expects ErrorCompletingRequestException.SPOOLED_FILE_NO_MESSAGE_WAITING
     **/
    public void Var002()
    {
        try
            {
            // create a output queue object
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/MSGTST.OUTQ");

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms to clear the entire output queue
            pList.setParameter(PrintObject.ATTR_JOBUSER, "*ALL");
            pList.setParameter(PrintObject.ATTR_FORMTYPE, "*ALL");
            pList.setParameter(PrintObject.ATTR_USERDATA, "*ALL");

            // clear the output queue
            outQ.clear(pList);

            // create a spooled file to our output queue with a funky form definition
            SpooledFile splF = createSpooledFile(outQ);

            try
                {
                // get the message from the spooled file
                AS400Message msg = splF.getMessage();
                }
            catch (ErrorCompletingRequestException e)
                {
                if (e.getReturnCode() == ErrorCompletingRequestException.SPOOLED_FILE_NO_MESSAGE_WAITING)
                    succeeded();
                else failed("Could retrieve message when one was not waiting.");
                }

            // delete the spooled file we created
            splF.delete();

            } // end try block

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

    /////////////////////
    // Private methods //
    /////////////////////

    private WriterJob startWriter(SpooledFile splF)
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException,
             RequestNotSupportedException
    {
        // create a printer device object using valid system name and printer name
        Printer prtD = new Printer(systemObject_, printer_);

        // create an output queue object using valid system name and output queue name
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/MSGTST.OUTQ");

        // create a print parm list
        PrintParameterList pList = new PrintParameterList();

        // set some parms
        pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");
        pList.setParameter(PrintObject.ATTR_SPOOLFILE,splF.getName());
        pList.setParameter(PrintObject.ATTR_SPLFNUM,splF.getNumber());
        pList.setParameter(PrintObject.ATTR_JOBNAME,splF.getJobName());
        pList.setParameter(PrintObject.ATTR_JOBNUMBER,splF.getJobNumber());
        pList.setParameter(PrintObject.ATTR_JOBUSER,splF.getJobUser());
        pList.setParameter(PrintObject.ATTR_FORMTYPE, "JAVA");

        // start a writer job using valid system name and printer name
        WriterJob wrtJ = WriterJob.start(systemObject_, prtD, pList, outQ);

        // call a method to ensure we have a good object
        wrtJ.update();

        return wrtJ;
    }

    // This method creates a spooled file
    private SpooledFile createSpooledFile(OutputQueue outQ)
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException,
             RequestNotSupportedException
    {
        if (outQ != null)
            {
            // create an empty print parameter list
            PrintParameterList pList = new PrintParameterList();

            // create a PrintParameterList with the a specific form type
            pList.setParameter(PrintObject.ATTR_FORMTYPE, "JAVA");

            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, pList, null, outQ);
            SCS5256Writer wtr = new SCS5256Writer(outStream, 37, systemObject_); // @B1C

            wtr.write("This is a test spooled file to send.");
            wtr.close();

            // update the attributes on the output queue
            outQ.update();

            // return the new SpooledFile
            return outStream.getSpooledFile();
            }
        else
            {
            throw new IOException("null output queue reference");
            }

    } // end createSpooledFile


} // end NPSplFGetMessageTestcase class


