///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFAnsMsgTestcase.java
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
import java.beans.PropertyVetoException;
import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase NPSplFAnsMsgTestcase.
 **/
public class NPSplFAnsMsgTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFAnsMsgTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    // the printer device name
    String printer_ = null;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFAnsMsgTestcase(AS400            systemObject,
                                Vector<String> variationsToRun,
                                int              runMode,
                                FileOutputStream fileOutputStream,
                                
                                String           printer)
      throws IOException
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPSplFAnsMsgTestcase", 5,
              variationsToRun, runMode, fileOutputStream);

        if (printer != null)
        {
          CommandCall cmd = new CommandCall(systemObject_);
          
          //  varify that the printer is a valid printer device
          try
          {
            // we want to see if we can start the default writer without error 
            // so need to end any writers already started
            if (cmd.run("ENDWTR WTR("+printer+") OPTION(*IMMED)") == false)
            {
              if (!cmd.getMessageList()[0].getID().trim().equals("CPF3313"))
              {
                throw new IOException("The -printer flag must be set to an active printer device name.");
              }
            }
            
            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(8000,0);

            // now try to start the writer
            if (cmd.run("STRPRTWTR DEV("+printer+")") == false)
            {
              throw new IOException("The -printer flag must be set to an active printer device name.");
            }
            
            // go to sleep for a bit to allow the writer to start
            Thread.sleep(8000,0);
            
            //check to see if it's in an error state
            WriterJobList wtrJobList = new WriterJobList(systemObject_);
            wtrJobList.setWriterFilter(printer);
            wtrJobList.openSynchronously();
            
            if (wtrJobList.size() == 1)
            {
              WriterJob wtrJob = (WriterJob)wtrJobList.getObject(0);
              String wtrStatus = wtrJob.getStringAttribute(PrintObject.ATTR_WTRJOBSTS);
              if (!wtrStatus.equals("*ACTIVE"))
              {
                throw new IOException("The -printer flag must be set to an active printer device name.");
              }
            }
            else
            {
              throw new IOException("The -printer flag must be set to an active printer device name.");
            }
          } 
          
          catch( Exception e )
          {
            throw new IOException("The -printer flag must be set to an active printer device name.");
          }
          
          // set the printer variable
          printer_ = printer;
        }
        else
        {
          throw new IOException("The -printer flag must be set to an active printer device name.");
        }
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFAnsMsgTestcase");
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
                // if the error we go back was anything but no active writer message
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
            Thread.sleep(8000,0);

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
     * Tests answering a spooled file message with the default reply.
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
            startWriter(splF);

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

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(8000,0);

            // delete the spooled file we created
            splF.delete();
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests answering a spooled file message with the cancel reply
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

                // answer the spooled file message with cancel reply
                // which is to cancel the writer
                splF.answerMessage("C");

                succeeded();
                } // end if fMsgWaiting
            else failed("Unable to cause msg waiting status on spooled file.");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(8000,0);

            // delete the spooled file we created
            splF.delete();
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests answering a spooled file message with an invalid reply
     * Expects CPF2422
     **/
    public void Var003()
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
                try
                    {

                    // answer the spooled file message with invalid reply
                    splF.answerMessage("X");

                    failed("Could answer message with invalid reply.");
                    }
                catch (AS400Exception e)
                    {
                    AS400Message msg = e.getAS400Message();
                    if (!msg.getID().trim().equals("CPF2422"))
                        {
                        failed("Incorrect message received for setting an invalid reply. ID="+msg.getID());
                        }
                    else
                        {
                        succeeded();  // Note: This variation will be successful.
                        }
                    }
                } // end if fMsgWaiting
            else failed("Unable to cause msg waiting status on spooled file.");

            // end the writer we started
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(8000,0);

            // delete the spooled file we created
            splF.delete();
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests answering a spooled file message with a null reply.
     * Expects NullPointerException
     **/
    public void Var004()
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
                try
                    {

                    // answer the spooled file message with null reply
                    splF.answerMessage(null);

                    failed("Could answer message with invalid reply.");
                    }
                catch (Exception e)
                    {
                    if (exceptionIs(e, "NullPointerException"))  succeeded();  
                    else failed (e, "Unexpected exception");
                    }
                } // end if fMsgWaiting
            else failed("Unable to cause msg waiting status on spooled file.");

            // end the writer we started
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(8000,0);

            // delete the spooled file we created
            splF.delete();
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests answering a spooled file message when there is not one waiting.
     * Expects ErrorCompletingRequestException.SPOOLED_FILE_NO_MESSAGE_WAITING
     **/
    public void Var005()
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
                // answer the message 
                splF.answerMessage("C");
                }
            catch (ErrorCompletingRequestException e)
                {
                if (e.getReturnCode() == ErrorCompletingRequestException.SPOOLED_FILE_NO_MESSAGE_WAITING)
                    succeeded();
                else failed("Could answer message when one was not waiting.");
                }

            // delete the spooled file we created
            splF.delete();
            } // end try block

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

} // end NPSplFAnsMsgTestcase class


