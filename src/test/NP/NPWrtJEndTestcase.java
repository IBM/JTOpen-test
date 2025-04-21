///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPWrtJEndTestcase.java
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
import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase NPWrtJEndTestcase.
 **/
public class NPWrtJEndTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPWrtJEndTestcase";
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
    public NPWrtJEndTestcase(AS400            systemObject,
                             Vector<String> variationsToRun,
                             int              runMode,
                             FileOutputStream fileOutputStream,
                             
                             String           printer)
      throws IOException
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPWrtJEndTestcase", 5,
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
        output_.println("NPPrintTest::NPWrtJEndTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);
        boolean fWriterEnded = false;

        try
            {
            // end the normal writer to printer
            CommandCall cmd = new CommandCall(systemObject_);
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
            Thread.sleep(5000,0);

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

            // if we stopped a writer then we will restart it.
           if (fWriterEnded == true)
                {
        	   
                if (cmd.run("STRPRTWTR DEV("+printer_+")") == false)
                    {
                	System.out.println("Entre aqui STRPRTWTR");
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
     * Tests ending a writer job with default parameters.
     **/
    public void Var001()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, null, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(4000,0);

            // end the writer we started
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(5000,0);
            //Thread.sleep(5000*10,0);

            // to verify that the writer job has really ended we will
            // try to update the attributes and should get back
            // an exception
            try
                {
                wrtJ.update();

                failed("Writer job did not end.");

                wrtJ.end("*IMMED");
                }
            catch (ErrorCompletingRequestException e)
                {
                if (e.getReturnCode() == ErrorCompletingRequestException.WRITER_JOB_ENDED)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not end writer, unexpected return code:"+e.getReturnCode());

                    wrtJ.end("*IMMED");

                    // go to sleep for a bit to allow the writer to stop
                    Thread.sleep(5000,0);

                    }
                } // end catch
            } // end try

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests ending a writer job with invalid parameters.
     * Expects ExtendedIllegalArgumentException
     **/
    public void Var002()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, null, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(4000,0);

            try
                {
                // end the writer we started
                wrtJ.end("*NOTVALID");

                failed("Could end writer job with invalid parameters");

                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(5000,0);

                }
            catch (AS400Exception e)
                {
                AS400Message msg = e.getAS400Message();
                if (!msg.getID().trim().equals("CPF0006"))
                    {
                    failed("Incorrect message received for setting an end type. ID="+msg.getID());
                    }
                else
                    {
                    succeeded();  // Note: This variation will be successful.

                    // end the writer job we started
                    wrtJ.end("*IMMED");

                    // go to sleep for a bit to allow the writer to stop
                    Thread.sleep(5000,0);

                    } // end else
                } // end catch
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests ending a writer job with *CNTRLD endtype parameter.
     **/
    public void Var003()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);
            
            CommandCall cmd = new CommandCall(systemObject_);
            cmd.run("ENDWTR WTR("+printer_+") OPTION(*IMMED)");
            Thread.sleep(5000,0);
  
            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, null, null);
            

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(4000,0);

            // end the writer we started
            wrtJ.end("*CNTRLD");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(5000,0);

            // to verify that the writer job has really ended we will
            // try to update the attributes and should get back
            // an exception
            try
                {
                wrtJ.update();

                failed("Writer job did not end with *CNTRLD end type.");

                wrtJ.end("*IMMED");
                }
            catch (ErrorCompletingRequestException e)
                {
                if (e.getReturnCode() == ErrorCompletingRequestException.WRITER_JOB_ENDED)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not end writer, unexpected return code:"+e.getReturnCode());

                    wrtJ.end("*IMMED");

                    // go to sleep for a bit to allow the writer to stop
                    Thread.sleep(5000,0);

                    }
                } // end catch
            } // end try

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var003

    /**
     * Tests ending a writer job with *IMMED endtype parameter.
     **/
    public void Var004()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, null, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(4000,0);

            // end the writer we started
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(5000,0);

            // to verify that the writer job has really ended we will
            // try to update the attributes and should get back
            // an exception
            try
                {
                wrtJ.update();

                failed("Writer job did not end.");

                wrtJ.end("*IMMED");
                }
            catch (ErrorCompletingRequestException e)
                {
                if (e.getReturnCode() == ErrorCompletingRequestException.WRITER_JOB_ENDED)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not end writer, unexpected return code:"+e.getReturnCode());

                    wrtJ.end("*IMMED");

                    // go to sleep for a bit to allow the writer to stop
                    Thread.sleep(5000,0);

                    }
                } // end catch
            } // end try

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests ending a writer job with *PAGEEND endtype parameter.
     **/
    public void Var005()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, null, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(4000,0);

            // end the writer we started
            wrtJ.end("*PAGEEND");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(5000,0);

            // to verify that the writer job has really ended we will
            // try to update the attributes and should get back
            // an exception
            try
                {
                wrtJ.update();

                failed("Writer job did not end.");

                wrtJ.end("*PAGEEND");
                }
            catch (ErrorCompletingRequestException e)
                {
                if (e.getReturnCode() == ErrorCompletingRequestException.WRITER_JOB_ENDED)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not end writer, unexpected return code:"+e.getReturnCode());

                    wrtJ.end("*IMMED");

                    // go to sleep for a bit to allow the writer to stop
                    Thread.sleep(5000,0);

                    }
                } // end catch
            } // end try

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

} // end NPWrtJEndTestcase class


