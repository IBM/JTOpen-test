///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPWrtJListListenerTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.OutputQueue;
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.PrintObjectListEvent;
import com.ibm.as400.access.PrintObjectListListener;
import com.ibm.as400.access.PrintParameterList;
import com.ibm.as400.access.Printer;
import com.ibm.as400.access.WriterJob;
import com.ibm.as400.access.WriterJobList;

import test.Testcase;

/**
 Testcase NPWrtJListListenerTestcase.
 **/
public class NPWrtJListListenerTestcase extends Testcase
                                        implements PrintObjectListListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPWrtJListListenerTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    int listenerInvokedCount;
    WriterJob objectAdded;
    Exception exception;
  
    // @A1A Added constants below
    private static final int OPENED     = 1;
    private static final int CLOSED     = 2;
    private static final int COMPLETED  = 3;
    private static final int ERROREVENT = 4;
    private static final int OBJECTADDED= 5;
    
    private boolean fListError = false;
    private boolean fListClosed = false;
    // private boolean fListCompleted = false;
    private Exception listException = null;
    private int listObjectCount = 0;
    // the printer device name
    String printer_ = null;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPWrtJListListenerTestcase(AS400            systemObject,
                                      Vector<String> variationsToRun,
                                      int              runMode,
                                      FileOutputStream fileOutputStream,
                                      
                                      String           printer)
      throws IOException
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPWrtJListListenerTestcase", 11,
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
        output_.println("NPPrintTest::NPWrtJListListenerTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);
        boolean fWriterEnded = false;

        try
            {
            // create LISTTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("QSYS/CRTOUTQ OUTQ(NPJAVA/LISTLTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("QSYS/GRTOBJAUT OBJ(NPJAVA/LISTLTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
                {
                failed("Could not grant *public authority to output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            // end the normal writer to printer
            if (cmd.run("QSYS/ENDWTR WTR("+printer_+") OPTION(*IMMED)") == false)
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
            Thread.sleep(3000,0);

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

            // run variation 9
            if ((allVariations || variationsToRun_.contains("9")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(9);
                Var009();
                }

            // run variation 10
            if ((allVariations || variationsToRun_.contains("10")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(10);
                Var010();
                }

            // run variation 11
            if ((allVariations || variationsToRun_.contains("11")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(11);
                Var011();
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
            if (cmd.run("QSYS/CLROUTQ OUTQ(NPJAVA/LISTLTST)") == false)
                {
                output_.println(" Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("QSYS/DLTOUTQ OUTQ(NPJAVA/LISTLTST)") == false)
                {
                output_.println(" Could not delete output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // if we stopped a writer then we will restart it.
            if (fWriterEnded == true)
                {
                if (cmd.run("QSYS/STRPRTWTR DEV("+printer_+")") == false)
                    {
                    output_.println(" Could not start writer that we ended. "
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
  // @A1A - Added equalWrtJ to compare two WriterJob objects
    public boolean equalsWrtJ(WriterJob wj1, WriterJob wj2) {
        if ((wj1.getName()).equalsIgnoreCase(wj2.getName()) &&
            (((wj1.getSystem()).getSystemName()).equalsIgnoreCase(
             ((wj2.getSystem()).getSystemName()))))
            return true;
        else
            return false;
    }
    
          
    // @A1A - Added generic ListListener which focuses on the particular
    //        PrintObjectList event being monitored.  The event to monitor
    //        is set by the constructor when the ListListener is created.
    public class ListListener implements PrintObjectListListener
    {
        int testingFocus_;  // Determines which event is actually being monitored.
        
        public ListListener(int focus) {
            testingFocus_ = focus;
        }
        
        public void listOpened(PrintObjectListEvent evt) 
        {
            if (testingFocus_ == OPENED) {           // check the event ID.
                if( evt.getID() == PrintObjectListEvent.OPENED ){
                    listenerInvokedCount++;
                }
            }
        }
            
        public void listClosed(PrintObjectListEvent evt)
        {
            if (testingFocus_ == CLOSED) {
                if ( evt.getID() == PrintObjectListEvent.CLOSED ) {
                    listenerInvokedCount++;
                }
            }
        }
        
        public void listCompleted(PrintObjectListEvent evt)
        {
            if (testingFocus_ == COMPLETED) {
                if( evt.getID() == PrintObjectListEvent.COMPLETED ) {
                    // delay, waitForListToComplete() should not    
                    // return control until after the listCompleted()
                    // event is fired.
                    try
                    {
                        Thread.sleep(3000,0);
                    }
                    catch( Exception e ) {}
                    listenerInvokedCount++;
                }
            }
        }
        
        public void listObjectAdded(PrintObjectListEvent evt)
        {
            if (testingFocus_ == OBJECTADDED) {   
                // check the event ID.
                if( evt.getID() == PrintObjectListEvent.OBJECT_ADDED ){
                    listenerInvokedCount++;
                    objectAdded = (WriterJob)evt.getObject();
                }
            }
        }
        
        public void listErrorOccurred(PrintObjectListEvent evt)
        {
            if (testingFocus_ == ERROREVENT) {
                // check the event ID.
                if( evt.getID() == PrintObjectListEvent.ERROR_OCCURRED ) {
                    listenerInvokedCount++;
                    exception = evt.getException();
                } 
            }
        }
    }     
 
    
    /**
     * Tests that a listOpened event is fired when the
     * writer job list is opened. The list was opened
     * synchronously.
     **/
    public void Var001()
    {
	try
            {
            ListListener listListener = new ListListener(OPENED);

            // create a writer job list object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            wrtJList.addPrintObjectListListener(listListener);

            // open the list
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            wrtJList.openSynchronously();

            // close the list
            wrtJList.close();

            if( listenerInvokedCount == 1 )
                {
                succeeded(); 
                }
            else if( listenerInvokedCount == 0 )
                {
                failed("listOpened event was not fired");
                }
            else
                {
                failed("listOpened event fired more than once");
                }

            // remove the listener
            wrtJList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests that a listClosed event is fired when the
     * writer job list is closed. The list was opened
     * synchronously.
     **/
    public void Var002()
    {
	try
            {
            ListListener listListener = new ListListener(CLOSED);

            // create a writer job list object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            wrtJList.addPrintObjectListListener(listListener);

            // open the list
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            wrtJList.openSynchronously();

            // close the list
            wrtJList.close();

            if( listenerInvokedCount == 1 )
                {
                succeeded(); 
                }
            else if( listenerInvokedCount == 0 )
                {
                failed("listClosed event was not fired");
                }
            else
                {
                failed("listClosed event fired more than once");
                }

            // remove the listener
            wrtJList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests that a listCompleted event is fired when the
     * writer job list is completed. The list was opened
     * synchronously.
     **/
    public void Var003()
    {
	try
            {
            ListListener listListener = new ListListener(COMPLETED);

            // create a writer job list object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            wrtJList.addPrintObjectListListener(listListener);

            // open the list
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            wrtJList.openSynchronously();

            // close the list
            wrtJList.close();

            if( listenerInvokedCount == 1 )
                {
                succeeded(); 
                }
            else if( listenerInvokedCount == 0 )
                {
                failed("listCompleted event was not fired");
                }
            else
                {
                failed("listCompleted event fired more than once");
                }

            // remove the listener
            wrtJList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests that a listObjectAdded event is fired when an      
     * object is added to the writer job list. The list was
     * opened synchronously.
     **/
    public void Var004()
    {
	try
            {
            ListListener listListener = new ListListener(OBJECTADDED);

            // start a writer job
            startWriter();

            // create a writer job list object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // there will be only one writer job on the list
            objectAdded = null;

            // add the listener
            wrtJList.addPrintObjectListListener(listListener);

            // open the list
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            wrtJList.openSynchronously();

            // get the writer job
            WriterJob wrtJ = (WriterJob)wrtJList.getObject(0);

            // close the list
            wrtJList.close();

            if( (listenerInvokedCount == 1) && (wrtJ.equals(objectAdded)) )
                {
                succeeded(); 
                }
            else if( listenerInvokedCount == 0 )
                {
                failed("listObjectAdded event was not fired");
                }
            else
                {
                failed("listObjectAdded event fired more than necessary");
                }

            // remove the listener
            wrtJList.removePrintObjectListListener(listListener);

            // end the writer job we started
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(3000,0);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests that a listErrorOccurred event is fired when an      
     * error occurs while retrieving the writer job list.
     * The list was opened synchronously.
     **/
    public void Var005()
    {
	try
            {
            ListListener listListener = new ListListener(ERROREVENT);

            // create a writer job list object using default constructor
            WriterJobList wrtJList = new WriterJobList();

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // reset the list's exception
            exception = null;

            // add the listener
            wrtJList.addPrintObjectListListener(listListener);

            // open the list
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");

            boolean exceptionThrown = false;
            try
                {
                wrtJList.openSynchronously();
                }
            catch( ExtendedIllegalStateException e )
                {
                //consume the expected ExtendedIllegalStateException
                exceptionThrown = true;
                }

            if( exceptionThrown == false )
                {
                failed("Expecting ExtendedIllegalStateException");
                } 
            else if( (listenerInvokedCount == 1) &&
                     (exception instanceof ExtendedIllegalStateException) )
                {
                succeeded(); 
                }
            else if( listenerInvokedCount == 0 )
                {
                failed("listErrorOccurred event was not fired");
                }
            else if( listenerInvokedCount > 1 )
                {
                failed("listErrorOccurred event fired more than necessary");
                }
            else
                {
                failed("PrintObjectListEvent's exception is not correct.");
                }

            // remove the listener
            wrtJList.removePrintObjectListListener(listListener);
            wrtJList.close();
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests that a listOpened event is fired when the
     * writer job list is opened. The list was opened
     * asynchronously.
     **/
    public void Var006()
    {
	try
            {
            ListListener listListener = new ListListener(OPENED);

            // create a writer job list object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            wrtJList.addPrintObjectListListener(listListener);

            // open the list
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            wrtJList.openAsynchronously();
            wrtJList.waitForListToComplete();

            // close the list
            wrtJList.close();

            if( listenerInvokedCount == 1 )
                {
                succeeded(); 
                }
            else if( listenerInvokedCount == 0 )
                {
                failed("listOpened event was not fired");
                }
            else
                {
                failed("listOpened event fired more than once");
                }

            // remove the listener
            wrtJList.removePrintObjectListListener(listListener);
            }

    catch (ExtendedIllegalStateException e)                              // @A2A
        {
        if ( (systemObject_.isThreadUsed() == false) && 
             (e.getReturnCode() == 
                ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
            {
            // with no threads, can not openAsynchronous()
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

    } // end Var006

    /**
     * Tests that a listClosed event is fired when the
     * writer job list is closed. The list was opened
     * asynchronously.
     **/
    public void Var007()
    {
	try
            {
            ListListener listListener = new ListListener(CLOSED);

            // create a writer job list object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            wrtJList.addPrintObjectListListener(listListener);

            // open the list
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            wrtJList.openAsynchronously();
            wrtJList.waitForListToComplete();

            // close the list
            wrtJList.close();

            if( listenerInvokedCount == 1 )
                {
                succeeded(); 
                }
            else if( listenerInvokedCount == 0 )
                {
                failed("listClosed event was not fired");
                }
            else
                {
                failed("listClosed event fired more than once");
                }

            // remove the listener
            wrtJList.removePrintObjectListListener(listListener);
            } 

    catch (ExtendedIllegalStateException e)                              // @A2A
        {
        if ( (systemObject_.isThreadUsed() == false) && 
             (e.getReturnCode() == 
                ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
            {
            // with no threads, can not openAsynchronous()
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

    } // end Var007

    /**
     * Tests that a listCompleted event is fired when the
     * writer job list is completed. The list was opened
     * asynchronously.
     **/
    public void Var008()
    {
	try
            {
            ListListener listListener = new ListListener(COMPLETED);

            // create a writer job list object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            wrtJList.addPrintObjectListListener(listListener);

            // open the list
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            wrtJList.openAsynchronously();
            wrtJList.waitForListToComplete();

            // close the list
            wrtJList.close();

            if( listenerInvokedCount == 1 )
                {
                succeeded(); 
                }
            else if( listenerInvokedCount == 0 )
                {
                failed("listCompleted event was not fired");
                }
            else
                {
                failed("listCompleted event fired more than once");
                }

            // remove the listener
            wrtJList.removePrintObjectListListener(listListener);
            } 


    catch (ExtendedIllegalStateException e)                              // @A2A
        {
        if ( (systemObject_.isThreadUsed() == false) && 
             (e.getReturnCode() == 
                ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
            {
            // with no threads, can not openAsynchronous()
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

    } // end Var008

    /**
     * Tests that a listObjectAdded event is fired when an      
     * object is added to the writer job list. The list was
     * opened asynchronously.
     **/
    public void Var009()
    {
        WriterJob wrtJ = null;
        try
            {
            ListListener listListener = new ListListener(OBJECTADDED);

            // start a writer job
            wrtJ = startWriter();

            // create a writer job list object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // there will be only one writer job on the list
            objectAdded = null;

            // add the listener
            wrtJList.addPrintObjectListListener(listListener);

            // open the list
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            wrtJList.openAsynchronously();
            wrtJList.waitForListToComplete();

            // use the list to get the writer job
            wrtJ = (WriterJob)wrtJList.getObject(0);

            // close the list
            wrtJList.close();

            if( (listenerInvokedCount == 1) && (wrtJ.equals(objectAdded)) )
                {
                succeeded(); 
                }
            else if( listenerInvokedCount == 0 )
                {
                failed("listObjectAdded event was not fired");
                }
            else
                {
                failed("listObjectAdded event fired more than necessary");
                }

            // remove the listener
            wrtJList.removePrintObjectListListener(listListener);
            } 

        catch (ExtendedIllegalStateException e)                          // @A2A
            {
            if ( (systemObject_.isThreadUsed() == false) && 
                 (e.getReturnCode() == 
                    ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
                {
                // with no threads, can not openAsynchronous()
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
                // end the writer job we started
                wrtJ.end("*IMMED");
  
                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(3000,0);
                }
            catch (Exception e) {}
            }

    } // end Var009

    /**
     * Tests that a listErrorOccurred event is fired when an      
     * error occurs while retrieving the writer job list.
     * The list was opened asynchronously.
     **/
    public void Var010()
    {
        try
            {
            ListListener listListener = new ListListener(ERROREVENT);

            // create a writer job list object using default constructor
            WriterJobList wrtJList = new WriterJobList();

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // reset the list's exception
            exception = null;

            // add the listener
            wrtJList.addPrintObjectListListener(listListener);

            // open the list
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");

            boolean exceptionThrown = false;
            try
                {
                wrtJList.openAsynchronously();
                wrtJList.waitForListToComplete();
                }
            catch( ExtendedIllegalStateException e )
                {
                //consume the expected ExtendedIllegalStateException
                exceptionThrown = true;
                }

            if( exceptionThrown == false )
                {
                failed("Expecting ExtendedIllegalStateException");
                } 
            else if( (listenerInvokedCount == 1) &&
                     (exception instanceof ExtendedIllegalStateException) )
                {
                succeeded(); 
                }
            else if( listenerInvokedCount == 0 )
                {
                failed("listErrorOccurred event was not fired");
                }
            else if( listenerInvokedCount > 1 )
                {
                failed("listErrorOccurred event fired more than necessary");
                }
            else
                {
                failed("PrintObjectListEvent's exception is not correct.");
                }

            // remove the listener
            wrtJList.removePrintObjectListListener(listListener);
            wrtJList.close();
            } 

        catch (Exception e)
            {
	         failed(e, "Unexpected exception");
            }

    } // end Var010

    /**
     Lists writer job(s) asynchronously with the various listeners
     **/
    public void Var011()
    {
        WriterJob wrtJ = null;
        try
            {
            // start a writer job
            wrtJ = startWriter();

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(3000,0);

            // create a Writer Job List object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // set the queue filter 
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");

            // add the listener
            wrtJList.addPrintObjectListListener(this);

            // open the list
	         wrtJList.openAsynchronously();

	         boolean fDone = false;
	         int     listed = 0, size;
            boolean fSuccess = false;
            String wrtJName = null;
            do
                { 
                //waitForWakeUp(); // @A1D
	    
	            // A "wait()" on the client does not accomplish the "polling"
	            // or "method waiting" state needed to receive events from the
	            // impl remote, and thus will not work in the proxied version
	            // of the code.  Instead, invoke waitForListToComplete() so there
	            // is a "method waiting", and events can then be heard on the client.
		        wrtJList.waitForListToComplete();	
       
                fDone = wrtJList.isCompleted();
                size = wrtJList.size();

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
                        output_.println(" Bad writer job:"+wrtJName);
                        fSuccess = false;
                        break;
                        }

                } // end while 

                if (fListClosed)
                    {
                    failed(" Writer Job List Aborted!");
                    break;
                    }

                if (fListError)
                    {
                    failed(" Exception on list - " + listException);
                    break;
                    }

                if (!fSuccess)
                    {
                    failed("Incorrect Writer Job listed: " + wrtJName);
                    break;
                    }

                } while (!fDone);

                // indicate how many jobs were listed
                output_.println(" " + listed + " Writer Job(s) listed.");

                // if we took an error clean up and get out.
                if ((fListClosed) || (fListError) || (!fSuccess))
                    {
                    wrtJList.close();
                    System.gc();
                    }
                else
                    {
                    while (!wrtJList.isCompleted())
                        {
                        Thread.sleep(200, 0);
                        }

                    wrtJList.close();
                    System.gc();
                    succeeded();  // Note: This variation will be successful.
                    }

                // remove the listener
                wrtJList.removePrintObjectListListener(this);

            } // end try block

        catch (ExtendedIllegalStateException e)                              // @A2A
            {
            if ( (systemObject_.isThreadUsed() == false) && 
                 (e.getReturnCode() == 
                    ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
                {
                // with no threads, can not openAsynchronous()
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
                // end the writer job we started
                wrtJ.end("*IMMED");
             
                // go to sleep for a bit to allow the writer to stop
                Thread.sleep(3000,0);
                }
            catch (Exception e) {}
            }

    } // end Var011

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



///////////////////////////////////////////////////////////////////////
// Methods needed to implement the PrintObjectListListener interface
///////////////////////////////////////////////////////////////////////

    // This method is invoked when the list is closed.
    public void listClosed(PrintObjectListEvent event)
    {
        fListClosed = true;
        synchronized(this)
        {
            // Set flag to indicate that the list has
            // completed and wake up foreground thread.
            //fListCompleted = true;
            notifyAll();
        }
    }

    // This method is invoked when the list is completed.
    public void listCompleted(PrintObjectListEvent event)
    {
        synchronized (this)
        {
            // Set flag to indicate that the list has
            // completed and wake up foreground thread.
            //fListCompleted = true;
            notifyAll();
        }
    }

    // This method is invoked if an error occurs while retrieving
    // the list.
    public void listErrorOccurred(PrintObjectListEvent event)
    {
        fListError = true;
        listException = event.getException();
        synchronized(this)
        {
            // Set flag to indicate that the list has
            // completed and wake up foreground thread.
            // fListCompleted = true;
            notifyAll();
        }
    }

    // This method is invoked when the list is opened.
    public void listOpened(PrintObjectListEvent event)
    {
        listObjectCount = 0;
    }

    // This method is invoked when an object is added to the list.
    public void listObjectAdded(PrintObjectListEvent event)
    {
        // every 25 objects we'll wake up the foreground
        // thread to get the latest objects...
        if( (++listObjectCount % 25) == 0 )
        {
            synchronized (this)
            {
                // wake up foreground thread
                notifyAll();
            }
        }
    }

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
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");

        // start a writer job
        return WriterJob.start(systemObject_, prtD, pList, outQ);

    } // end startWriter

} // end NPWrtJListListenerTestcase class


