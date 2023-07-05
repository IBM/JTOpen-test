///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPWrtJListAttrsToRetTestcase.java
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
 Testcase NPWrtJListAttrsToRetTestcase.
 **/
public class NPWrtJListAttrsToRetTestcase extends Testcase

{
    // the printer device name
    String printer_ = null;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPWrtJListAttrsToRetTestcase(AS400            systemObject,
                                        Vector           variationsToRun,
                                        int              runMode,
                                        FileOutputStream fileOutputStream,
                                        
                                        String           printer)
      throws IOException
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPWrtJListAttrsToRetTestcase", 5,
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
        output_.println("NPPrintTest::NPWrtJListAttrsToRetTestcase");
        boolean allVariations = (variationsToRun_.size() == 0);
        boolean fWriterEnded = false;

        try
            {
            // create ATTRTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/ATTRTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/ATTRTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/ATTRTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/ATTRTST)") == false)
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
     * Tests setting valid attributes to retrieve on a Writer Job List.
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
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/ATTRTST.OUTQ");

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_WTRJOBNAME;
            attrs[1] = PrintObject.ATTR_WTRJOBNUM;
            attrs[2] = PrintObject.ATTR_WTRJOBSTS;

            // set the attributes to retrieve
            wrtJList.setAttributesToRetrieve(attrs);

            // list the writer jobs
            wrtJList.openSynchronously();

            Enumeration e = wrtJList.getObjects();

            // check to see if we got our writer job
            if (wrtJList.size() == 1)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    WriterJob wrtJob = (WriterJob)e.nextElement();
                    if ( (wrtJob.getStringAttribute(PrintObject.ATTR_WTRJOBNAME) != null) &&
                         (wrtJob.getStringAttribute(PrintObject.ATTR_WTRJOBNUM) != null) &&
                         (wrtJob.getStringAttribute(PrintObject.ATTR_WTRJOBSTS) != null) )
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        output_.println("Bad attributes on "+wrtJob.getName());
                        break;
                        }
                    } // end while

                // indicate how many jobs were listed
                output_.println(wrtJList.size() + " Writer Job(s) listed.");

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
                failed("Incorrect number of writer job(s) found in list.");
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
     * Tests setting null attributes to retrieve on a WriterJob List.
     * Expects NullPointerException
     **/
    public void Var002()
    {
        try
            {
            // create a Writer Job List object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                wrtJList.openAsynchronously();
            }
            else {
                wrtJList.openSynchronously();
            }

            // set the attributes to retrieve
            wrtJList.setAttributesToRetrieve(null);

            failed("Could set null attributes to retrieve.");

            wrtJList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back a null pointer exception
            if (exceptionIs(e, "NullPointerException"))  succeeded();
            else failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting the attributes to retrieve on an open Writer Job List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var003()
    {
        try
            {
            // create a Writer Job List object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                wrtJList.openAsynchronously();
            }
            else {
                wrtJList.openSynchronously();
            }

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_WTRJOBNAME;
            attrs[1] = PrintObject.ATTR_WTRJOBNUM;
            attrs[2] = PrintObject.ATTR_WTRJOBSTS;

            // set the attributes to retrieve
            wrtJList.setAttributesToRetrieve(attrs);

            failed("Could set attributes to retrieve on an opened list.");

            wrtJList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back an extened illegal state exception
            if (exceptionIs(e, "ExtendedIllegalStateException"))  succeeded();
            else failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests resetting attributes to retrieve on a Writer Job List.
     **/
    public void Var004()
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
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/ATTRTST.OUTQ");

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_WTRJOBNAME;
            attrs[1] = PrintObject.ATTR_WTRJOBNUM;
            attrs[2] = PrintObject.ATTR_WTRJOBSTS;

            // set the attributes to retrieve
            wrtJList.setAttributesToRetrieve(attrs);

            // list the jobs
            wrtJList.openSynchronously();

            // close the list
            wrtJList.close();

            // reset the attributes 
            wrtJList.resetAttributesToRetrieve();

            // set the output queue filter 
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/ATTRTST.OUTQ");
           
            // list the jobs
            wrtJList.openSynchronously();

            Enumeration e = wrtJList.getObjects();

            // check to see if we got a writer job
            if (wrtJList.size() == 1)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    WriterJob wrtJob = (WriterJob)e.nextElement();

                    if (wrtJob.getStringAttribute(PrintObject.ATTR_WTRJOBUSER) != null)
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        output_.println("Bad attributes on "+wrtJob.getName());
                        break;
                        }
                    } // end while

                // indicate how many jobs were listed
                output_.println(wrtJList.size() + " Writer Job(s) listed.");

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
                failed("Incorrect number of writer job(s) listed.");
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

    } // end Var004

    /**
     * Tests resetting the attributes to retrieve on an open Writer Job List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var005()
    {
        try
            {
            // create a Writer Job List object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                wrtJList.openAsynchronously();
            }
            else {
                wrtJList.openSynchronously();
            }

            // set the attributes to retrieve
            wrtJList.resetAttributesToRetrieve();

            failed("Could reset attributes to retrieve on an opened list.");

            wrtJList.close();

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

    /////////////////////
    // Private methods //
    /////////////////////

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
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/ATTRTST.OUTQ");

        // start a writer job
        return WriterJob.start(systemObject_, prtD, pList, outQ);

    } // end startWriter

} // end NPWrtJListAttrsToRetTestcase class


