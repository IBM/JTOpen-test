///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPWrtJListOpenAsyncTestcase.java
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
 Testcase NPWrtJListOpenAsyncTestcase.
 **/
public class NPWrtJListOpenAsyncTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPWrtJListOpenAsyncTestcase";
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
    public NPWrtJListOpenAsyncTestcase(AS400            systemObject,
                                       Vector           variationsToRun,
                                       int              runMode,
                                       FileOutputStream fileOutputStream,
                                       
                                       String           printer)
      throws IOException
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPWrtJListOpenAsyncTestcase", 4,
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
        output_.println("NPPrintTest::NPWrtJListOpenAsyncTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);
        boolean fWriterEnded = false;

        try
            {
            // create OPNATST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/OPNATST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/OPNATST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/OPNATST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/OPNATST)") == false)
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
     * Lists writer job(s) asynchronously using the waitForListToComplete method
     **/
    public void Var001()
    {
        WriterJob wrtJ = null;
        WriterJobList wrtJList = null;
        try
            {
            // start a writer job
            wrtJ = startWriter();

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            // create a Writer Job List object
            wrtJList = new WriterJobList(systemObject_);

            // set the output queue filter 
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/OPNATST.OUTQ");

            // now try to build list asynchrously
            wrtJList.openAsynchronously();

            // wait for the list to complete
            wrtJList.waitForListToComplete();

            int listed = 0, size;
            size = wrtJList.size();
            String wrtJName = null;

            // check to see if we got some jobs
            if (size > 0)
                {
                boolean fSuccess = false;

                // check the job we go back
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

            } // end try block

        catch (ExtendedIllegalStateException e)                          // @A1A
            {
            if( (systemObject_.isThreadUsed() == false) &&
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

        finally
            {
            try
                {
                // close the list
                wrtJList.close();

                // end the writer job we started
                wrtJ.end("*IMMED");

                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(2000,0);

                // garbage collect
                System.gc();
                }
            catch (Exception e){}
            }

    } // end Var001

    /**
     * Lists writer job(s) asynchronously using the waitForItem method
     **/
    public void Var002()
    {
        WriterJob wrtJ = null;
        WriterJobList wrtJList = null;
        try
            {
            // start a writer job
            wrtJ = startWriter();

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            // create a Writer Job List object
            wrtJList = new WriterJobList(systemObject_);

            // set the output queue filter 
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/OPNATST.OUTQ");

            // now try to build list asynchrously
            wrtJList.openAsynchronously();

            // wait for the 1st item on the list to complete
            wrtJList.waitForItem(1);

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
            } // end try block

        catch (ExtendedIllegalStateException e)                          // @A1A
            {
            if( (systemObject_.isThreadUsed() == false) &&
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

        finally
            {
            try
                {
                // close the list
                wrtJList.close();

                // end the writer job we started
                wrtJ.end("*IMMED");

                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(2000,0);

                // force garbage collection 
                System.gc();
                }
            catch (Exception e) {}
            }

    } // end Var002

    /**
     * Lists writer job(s) asynchronously using the waitForItem method.
     * Wait for an item number greater than what is available.
     **/
    public void Var003()
    {
        WriterJob wrtJ = null;
        WriterJobList wrtJList = null;
        try
            {
            // start a writer job
            wrtJ = startWriter();

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            // create a Writer Job List object
            wrtJList = new WriterJobList(systemObject_);

            // set the output queue filter 
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/OPNATST.OUTQ");

            // now try to build list asynchrously
            wrtJList.openAsynchronously();

            // wait for the 100th item on the list to complete,
            // there should not be 100 items.
            wrtJList.waitForItem(100);

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

            } // end try block

        catch (ExtendedIllegalStateException e)                          // @A1A
            {
            if( (systemObject_.isThreadUsed() == false) &&
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

        finally
            {
            try 
                {
                // close the list
                wrtJList.close();

                // end the writer job we started
                wrtJ.end("*IMMED");

                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(2000,0);

                // force garbage collection
                System.gc();
                }
            catch (Exception e) {}
            }

    } // end Var003

    /**
     * Lists writer job(s) asynchronously using the waitForItem method with invalid number.
     * Expects ExtendedIllegalArgumentException
     **/
    public void Var004()
    {
        WriterJobList wrtJList = null;
        try
            {
            // create a Writer Job List object
            wrtJList = new WriterJobList(systemObject_);

            // set the output queue filter 
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/OPNATST.OUTQ");

            // now try to build list asynchrously
            wrtJList.openAsynchronously();

            try
                {
                // use an invalid number to wait for
                wrtJList.waitForItem(0);

                failed("Could use an invalid number to wait for.");
                }
            catch (ExtendedIllegalArgumentException e)
                {
                succeeded();
                }

            } // end try block

        catch (ExtendedIllegalStateException e)                          // @A1A
            {
            if( (systemObject_.isThreadUsed() == false) &&
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

        finally 
            {
            try
                {
                // close the list
                wrtJList.close();
   
                // force garbage collection
                System.gc();
                }
            catch (Exception e) {}
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
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/OPNATST.OUTQ");

        // start a writer job
        return WriterJob.start(systemObject_, prtD, pList, outQ);

    } // end startWriter

} // end NPWrtJListOpenAsyncTestcase class


