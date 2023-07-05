///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPWrtJStartTestcase.java
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
import com.ibm.as400.access.*;

/**
 Testcase NPWrtJStartTestcase.
 **/
public class NPWrtJStartTestcase extends Testcase
{
    // the printer device name
    String printer_ = null;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPWrtJStartTestcase(AS400            systemObject,
                               Vector           variationsToRun,
                               int              runMode,
                               FileOutputStream fileOutputStream,
                               
                               String           printer)
      throws IOException
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPWrtJStartTestcase", 8,
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
        output_.println("NPPrintTest::NPWrtJStartTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);
        boolean fWriterEnded = false;

        try
            {
            // create STARTTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/STARTTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/STARTTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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
//            Thread.sleep(400000,0);
            Thread.sleep(4000,0);

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

            // run variation 6
            if ((allVariations || variationsToRun_.contains("6")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(6);
                Var006();
                }

            // run variation 7
            if ((allVariations || variationsToRun_.contains("7")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(7);
                Var007();
                }

            // run variation 8
            if ((allVariations || variationsToRun_.contains("8")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(8);
                Var008();
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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/STARTTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/STARTTST)") == false)
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
     * Tests starting a writer with valid system and printer parameters,
     *                              no print parms list and no output queue.
     **/
    public void Var001()
    {
        try
            {
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
        Thread.sleep(4000,0);
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);

            // start a writer job using valid system name and printer name

            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, null, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(4000,0);

            if (wrtJ.getStringAttribute(PrintObject.ATTR_WTRJOBSTS).trim().equals("*ACTIVE"))
                {
                succeeded();  // Note: This variation will be successful.

                // end the writer we started
                wrtJ.end("*IMMED");

                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(4000,0);
                }
            else failed ("Could not start a writer job.");

            } // end try block

        catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }
        
    } // end Var001

    /**
     * Tests starting a writer with null system and printer parameters,
     *                              no print parms list and no output queue.
     * Expect NullPointerException
     **/
    public void Var002()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);

            try
                {
                // start a writer job using valid system name and printer name
                WriterJob wrtJ = WriterJob.start(null, prtD, null, null);

                failed("Could use null system name.");

                // go to sleep for a bit to allow the writer to start
                Thread.sleep(4000,0);

                // end the writer we started
                wrtJ.end("*IMMED");

                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(4000,0);

                }
            catch (Exception e)
                {
                if (exceptionIs(e, "NullPointerException"))  succeeded();  
                else failed (e, "Unexpected exception");
                }
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests starting a writer with valid system and null printer parameters,
     *                              no print parms list and no output queue.
     * Expect NullPointerException
     **/
    public void Var003()
    {
        try
            {
            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, null, null, null);

            failed("Could use null printer parm.");

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(4000,0);

            // end the writer we started
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(4000,0);

           }
        catch (Exception e)
            {
            if (exceptionIs(e, "NullPointerException"))  succeeded();  
            else
                {
                failed (e, "Unexpected exception");
                }
            }
    } // end Var003

    /**
     * Tests starting a writer with valid system and printer parameters,
     *                              valid print parms list and no output queue.
     **/
    public void Var004()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms
            pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, pList, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(4000,0);

            if (wrtJ.getStringAttribute(PrintObject.ATTR_WTRJOBSTS).trim().equals("*ACTIVE"))
                {
                succeeded();  // Note: This variation will be successful.

                // end the writer we started
                wrtJ.end("*IMMED");

                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(4000,0);
                }
            else failed ("Could not start a writer job.");
          } // end try block

        catch (Exception e)
            {
	    failed(e, "Unexpected exception");
	}

    } // end Var004

    /**
     * Tests starting a writer with valid system and printer parameters,
     *                              valid print parms list and output queue.
     **/
    public void Var005()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms
            pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");
            pList.setParameter(PrintObject.ATTR_OUTPUT_QUEUE,"/QSYS.LIB/NPJAVA.LIB/STARTTST.OUTQ");

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, pList, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(8000,0);

            if (wrtJ.getStringAttribute(PrintObject.ATTR_WTRJOBSTS).trim().equals("*ACTIVE"))
                {
                succeeded();  // Note: This variation will be successful.

                // end the writer we started
                wrtJ.end("*IMMED");

                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(8000,0);
                }
            else failed ("Could not start a writer job.");
            } // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var005

    /**
     * Tests starting a writer with valid system and printer parameters,
     *                              no print parms list and output queue.
     **/
    public void Var006()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);

            // create an output queue object using valid system name and output queue name
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/STARTTST.OUTQ");

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, null, outQ);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(4000,0);

            if (wrtJ.getStringAttribute(PrintObject.ATTR_WTRJOBSTS).trim().equals("*ACTIVE"))
                {
                succeeded();  // Note: This variation will be successful.

                // end the writer we started
                wrtJ.end("*IMMED");

                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(4000,0);
                }
            else failed ("Could not start a writer job.");
            } // end try block

        catch (Exception e)
            {
	    failed(e, "Unexpected exception");
	}

    } // end Var006

    /**
     * Tests starting a writer with valid system and printer parameters,
     *                              no print parms list and invalid output queue.
     * Expects CPF3357
     **/
    public void Var007()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);

            // create an output queue object using valid system name and output queue name
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/XXXX.OUTQ");

            try
                {
                // start a writer job using valid system name and printer name
                WriterJob wrtJ = WriterJob.start(systemObject_, prtD, null, outQ);

                // go to sleep for a bit to allow the writer to start
                Thread.sleep(4000,0);

                failed("Could use an invalid output queue to start a writer.");

                // end the writer we started
                wrtJ.end("*IMMED");

                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(4000,0);

                }
            catch (AS400Exception e)
                {
                AS400Message msg = e.getAS400Message();
                if (!msg.getID().trim().equals("CPF3357"))
                    {
                    failed("Incorrect message received for starting a writer job with an invalid output queue. ID="+msg.getID());
                    }
                else succeeded();  // Note: This variation will be successful.
                }

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var007

    /**
     * Tests starting a writer with valid system and printer parameters,
     *                               print parms list with output queue and output queue.
     **/
    public void Var008()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, printer_);

            // create JAVAOUTQ output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/JAVAOUTQ) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/JAVAOUTQ) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
                {
                failed("Could not grant *public authority to output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

                // create an output queue object using valid system name and output queue name
                OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/STARTTST.OUTQ");

                // create a print parm list
                PrintParameterList pList = new PrintParameterList();

                // set some parms
                pList.setParameter(PrintObject.ATTR_OUTPUT_QUEUE,"/QSYS.LIB/NPJAVA.LIB/JAVAOUTQ.OUTQ");

                // start a writer job using valid system name and printer name
                WriterJob wrtJ = WriterJob.start(systemObject_, prtD, pList, outQ);

                // go to sleep for a bit to allow the writer to start
                Thread.sleep(8000,0);

                if (wrtJ.getStringAttribute(PrintObject.ATTR_WTRJOBSTS).trim().equals("*ACTIVE"))
                    {
                    succeeded();  // Note: This variation will be successful.

                    // end the writer we started
                    wrtJ.end("*IMMED");

                    // go to sleep for a bit to allow the writer to stop
                    Thread.sleep(8000,0);
                    }
                else failed ("Could not start a writer job.");

                // clear the output queue we created.
                if (cmd.run("CLROUTQ OUTQ(NPJAVA/JAVAOUTQ)") == false)
                    {
                    output_.println("Could not clear output queue we created. "
                                    + cmd.getMessageList()[0].getID()
                                    + ": " + cmd.getMessageList()[0].getText());
                    }

                // delete the output queue we created.
                if (cmd.run("DLTOUTQ OUTQ(NPJAVA/JAVAOUTQ)") == false)
                    {
                    output_.println("Could not delete output queue we created. "
                                    + cmd.getMessageList()[0].getID()
                                    + ": " + cmd.getMessageList()[0].getText());
                    }


            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var008

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

} // end NPWrtJStartTestcase class


