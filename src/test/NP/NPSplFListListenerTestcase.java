///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFListListenerTestcase.java
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
 Testcase NPSplFListListenerTestcase.
 **/
public class NPSplFListListenerTestcase extends Testcase
                                        implements PrintObjectListListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFListListenerTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    int listenerInvokedCount;
    SpooledFile objectAdded;
    Exception exception;
    
    // @A1A Added constants below
    private static final int OPENED     = 1;
    private static final int CLOSED     = 2;
    private static final int COMPLETED  = 3;
    private static final int ERROREVENT = 4;
    private static final int OBJECTADDED= 5;
    
    private boolean fListError = false;
    private boolean fListClosed = false;
    private boolean fListCompleted = false;
    private Exception listException = null;
    private int listObjectCount = 0;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFListListenerTestcase(AS400            systemObject,
                                      Vector           variationsToRun,
                                      int              runMode,
                                      FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPSplFListListenerTestcase", 11,
              variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFListListenerTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create LISTLTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/LISTLTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/LISTLTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/LISTLTST)") == false)
                {
                output_.println(" Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/LISTLTST)") == false)
                {
                output_.println(" Could not delete output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            } // end try block

        catch( Exception e )
            {
            failed(e, "Unexpected exception");
            }

    } // end run method
    
    
    // @A1A - Added equalSplF to compare two SpooledFile objects
    public boolean equalsSplF(SpooledFile sf1, SpooledFile sf2) {
        if ((sf1.getName()).equalsIgnoreCase(sf2.getName()) &&
            (((sf1.getSystem()).getSystemName()).equalsIgnoreCase(
             ((sf2.getSystem()).getSystemName())))&&
             (sf1.getJobName().equalsIgnoreCase(sf2.getJobName())) &&
             (sf1.getJobNumber().equalsIgnoreCase(sf2.getJobNumber())) &&
             (sf1.getJobUser().equalsIgnoreCase(sf2.getJobUser())) &&
             (sf1.getNumber() == sf2.getNumber())) {
            return true;
             }
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
                    objectAdded = (SpooledFile)evt.getObject();
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
     * spooled file list is opened. The list was opened
     * synchronously.
     **/
    public void Var001()
    {
	try
            {
            ListListener listListener = new ListListener(OPENED);

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            splFList.addPrintObjectListListener(listListener);

            // open the list
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            splFList.openSynchronously();

            // close the list
            splFList.close();

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
            splFList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests that a listClosed event is fired when the
     * spooled file list is closed. The list was opened
     * synchronously.
     **/
    public void Var002()
    {
	try
            {
            ListListener listListener = new ListListener(CLOSED);

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            splFList.addPrintObjectListListener(listListener);

            // open the list
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            splFList.openSynchronously();

            // close the list
            splFList.close();

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
            splFList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests that a listCompleted event is fired when the
     * spooled file list is completed. The list was opened
     * synchronously.
     **/
    public void Var003()
    {
	try
            {
            ListListener listListener = new ListListener(COMPLETED);

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            splFList.addPrintObjectListListener(listListener);

            // open the list
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            splFList.openSynchronously();

            // close the list
            splFList.close();

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
            splFList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests that a listObjectAdded event is fired when an      
     * object is added to the spooled file list. The list
     * was opened synchronously.
     **/
    public void Var004()
    {
	try
            {
            ListListener listListener = new ListListener(OBJECTADDED);

            // create a spooled file on the output queue
            createSpooledFile();

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // there will be only one spooled file on the list
            objectAdded = null;

            // add the listener
            splFList.addPrintObjectListListener(listListener);

            // open the list
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            splFList.openSynchronously();

            // get the spooled file
            SpooledFile splF = (SpooledFile)splFList.getObject(0);

            // close the list
            splFList.close();

            if( (listenerInvokedCount == 1) && (splF.equals(objectAdded)) )
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
            splFList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests that a listErrorOccurred event is fired when an      
     * error occurs while retrieving the spooled file list.
     * The list was opened synchronously.
     **/
    public void Var005()
    {
	try
            {
            ListListener listListener = new ListListener(ERROREVENT);

            // create a spooled file list object using default constructor
            SpooledFileList splFList = new SpooledFileList();

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // reset the list's exception
            exception = null;

            // add the listener
            splFList.addPrintObjectListListener(listListener);

            // open the list
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");

            boolean exceptionThrown = false;
            try
                {
                splFList.openSynchronously();
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
            splFList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests that a listOpened event is fired when the
     * spooled file list is opened. The list was opened
     * asynchronously.
     **/
    public void Var006()
    {
	try
            {
            ListListener listListener = new ListListener(OPENED);

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            splFList.addPrintObjectListListener(listListener);

            // open the list
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            splFList.openAsynchronously();
            splFList.waitForListToComplete();

            // close the list
            splFList.close();

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
            splFList.removePrintObjectListListener(listListener);
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
     * spooled file list is closed. The list was opened
     * asynchronously.
     **/
    public void Var007()
    {
	try
            {
            ListListener listListener = new ListListener(CLOSED);

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            splFList.addPrintObjectListListener(listListener);

            // open the list
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            splFList.openAsynchronously();
            splFList.waitForListToComplete();

            // close the list
            splFList.close();

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
            splFList.removePrintObjectListListener(listListener);
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
     * spooled file list is completed. The list was opened
     * asynchronously.
     **/
    public void Var008()
    {
	try
            {
            ListListener listListener = new ListListener(COMPLETED);

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            splFList.addPrintObjectListListener(listListener);

            // open the list
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            splFList.openAsynchronously();
            splFList.waitForListToComplete();

            // close the list
            splFList.close();

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
            splFList.removePrintObjectListListener(listListener);
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
     * object is added to the spooled file list. The list
     * was opened asynchronously.
     **/
    public void Var009()
    {
	try
            {
            ListListener listListener = new ListListener(OBJECTADDED);

            // create a spooled file on the output queue
            createSpooledFile();

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // there will be only one spooled file on the list
            objectAdded = null;

            // add the listener
            splFList.addPrintObjectListListener(listListener);

            // open the list
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
            splFList.openAsynchronously();
            splFList.waitForListToComplete();

            // get the spooled file
            SpooledFile splF = (SpooledFile)splFList.getObject(0);

            // close the list
            splFList.close();

            if( (listenerInvokedCount == 1) && (splF.equals(objectAdded)) )
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
            splFList.removePrintObjectListListener(listListener);
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

    } // end Var009

    /**
     * Tests that a listErrorOccurred event is fired when an      
     * error occurs while retrieving the spooled file list.
     * The list was opened asynchronously.
     **/
    public void Var010()
    {
	try
            {
            ListListener listListener = new ListListener(ERROREVENT);

            // create a spooled file list object using default constructor
            SpooledFileList splFList = new SpooledFileList();

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // reset the list's exception
            exception = null;

            // add the listener
            splFList.addPrintObjectListListener(listListener);

            // open the list
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");

            boolean exceptionThrown = false;
            try
                {
                splFList.openAsynchronously();
                splFList.waitForListToComplete();
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
            splFList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var010

    /**
     Lists spooled file(s) asynchronously with the a listener
     **/
    public void Var011()
    {
	try
	{
        // create a spooled file on the output queue
        SpooledFile splF = createSpooledFile();

	    // create a spooled file list object
	    SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set up the listener
        splFList.addPrintObjectListListener(this);

            // set the queue filter 
        splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");
	    splFList.openAsynchronously();

	    boolean fDone = false;
	    int     listed = 0, size;
        boolean fSuccess = false;
        String splFName = null;
	    do
	    { 
	        //waitForWakeUp(); // @A1D
	    
	        // A "wait()" on the client does not accomplish the "polling"
	        // or "method waiting" state needed to receive events from the
	        // impl remote, and thus will not work in the proxied version
	        // of the code.  Instead, invoke waitForListToComplete() so there
	        // is a "method waiting", and events can then be heard on the client.
		    splFList.waitForListToComplete();	
		    
            fDone = splFList.isCompleted();
		    size = splFList.size();
		    while (listed < size)
		    {
                    SpooledFile splFile = (SpooledFile)splFList.getObject(listed++);
                    splFName = splFile.getName();

                    if (splFName.equals("JAVAPRINT"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println(" Bad spooled file:"+splFName);
                        fSuccess = false;
                        break;
                        }

            } // end while 

            if (fListClosed)
                {
                failed(" Spooled File List Aborted!");
                break;
                }

            if (fListError)
                {
                failed(" Exception on list - " + listException);
                break;
                }

            if (!fSuccess)
                {
                failed("Incorrect Spooled file listed: " + splFName);
                break;
                }

	    } while (!fDone);

            // indicate how many files were listed
            output_.println(" " + listed + " Spooled files listed.");

            // if we took an error clean up and get out.
            if ((fListClosed) || (fListError) || (!fSuccess))
                {
                splFList.close();
                System.gc();
                }
            else
                {
                while (!splFList.isCompleted())
                    {
                    Thread.sleep(200, 0);
                    }

                splFList.close();
                System.gc();
                succeeded();  // Note: This variation will be successful.
                }

            // remove the listener
            splFList.removePrintObjectListListener(this);

            // delete the spooled file we created
            splF.delete();

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


///////////////////
// Private methods
///////////////////

    // This is where the foreground thread waits for to be awaken by the
    // the background thread when the list is updated or it ends.
    private synchronized void waitForWakeUp()
      throws InterruptedException
    {
        // don''t go back to sleep if the listener says the list is done
        if (!fListCompleted)
	{
	    wait();
	}
    }

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
            fListCompleted = true;
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
            fListCompleted = true;
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
            fListCompleted = true;
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

//////////////////////////////////////////
// This method creates a spooled file
//////////////////////////////////////////

    private SpooledFile createSpooledFile()
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException,
             RequestNotSupportedException
    {
        // buffer for data
        byte[] buf = new byte[2048];

        // fill the buffer
        for (int i=0; i<2048; i++) buf[i] = (byte)'9';

        // create a reference to the output queue
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/LISTLTST.OUTQ");

        // clear the output queue
        outQ.clear(null);

        // create a print parameter list for the spooled file name
        PrintParameterList pList = new PrintParameterList();

        // set the spooled file name
        pList.setParameter(PrintObject.ATTR_SPOOLFILE,"JAVAPRINT");

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, pList, null, outQ);

        // write some data
        outStream.write(buf);

        // close the spooled file
        outStream.close();

        // update the output queue attributes
        outQ.update();

        // return the new SpooledFile
        return outStream.getSpooledFile();
    } // end createSpooledFile

} // end NPSplFListListenerTestcase class


