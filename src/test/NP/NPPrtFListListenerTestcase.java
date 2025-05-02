///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtFListListenerTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.PrintObjectListEvent;
import com.ibm.as400.access.PrintObjectListListener;
import com.ibm.as400.access.PrinterFile;
import com.ibm.as400.access.PrinterFileList;

import test.Testcase;

/**
 Testcase NPPrtFListListenerTestcase.
 **/
public class NPPrtFListListenerTestcase extends Testcase
                                        implements PrintObjectListListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtFListListenerTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    int listenerInvokedCount;
    PrinterFile objectAdded;
    Exception exception;
    
    // @A1A Added constants below
    private static final int OPENED     = 1;
    private static final int CLOSED     = 2;
    private static final int COMPLETED  = 3;
    private static final int ERROREVENT = 4;
    private static final int OBJECTADDED= 5;
    
    // private boolean fListOpened = false;
    private boolean fListError = false;
    private boolean fListClosed = false;
    private boolean fListCompleted = false;
    private Exception listException = null;
    private int listObjectCount = 0;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtFListListenerTestcase(AS400            systemObject,
                                      Vector<String> variationsToRun,
                                      int              runMode,
                                      FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPPrtFListListenerTestcase", 11,
              variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtFListListenerTestcase");
	    boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create JAVAPRINT printer file
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("QSYS/CRTPRTF FILE(NPJAVA/JAVAPRINT)") == false)
                {
                failed("Could not create a printer file. "
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

            // delete the printer file that we created
            if (cmd.run("QSYS/DLTF FILE(NPJAVA/JAVAPRINT)") == false)
                {
                output_.println(" Could not delete the printer file we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            } // end try block

        catch( Exception e )
            {
            failed(e, "Unexpected exception");
            }

    } //end run method
          
    // @A1A - Added equalPrtF to compare two PrinterFile objects
    public boolean equalsPrtF(PrinterFile pf1, PrinterFile pf2) {
        if ((pf1.getName()).equalsIgnoreCase(pf2.getName()) &&
            (((pf1.getSystem()).getSystemName()).equalsIgnoreCase(
             ((pf2.getSystem()).getSystemName()))) &&
             ((pf1.getPath()).equals(pf2.getPath())))
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
                    objectAdded = (PrinterFile)evt.getObject();
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
     * printer file list is opened. The list was opened
     * synchronously.
     **/
    public void Var001()
    {
	try
            {
            ListListener listListener = new ListListener(OPENED);

            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            prtFList.addPrintObjectListListener(listListener);

            // open the list
	        prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            prtFList.openSynchronously();
            
            // close the list
            prtFList.close();

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
            prtFList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	        failed(e, "Unexpected exception");
            }

    } // end Var001
  
 
    /**
     * Tests that a listClosed event is fired when the
     * printer file list is closed. The list was opened
     * synchronously.
     **/
    public void Var002()
    {
	try
            {
            ListListener listListener = new ListListener(CLOSED);

            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            prtFList.addPrintObjectListListener(listListener);

            // open the list
	        prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");
            prtFList.openSynchronously();

            // close the list
            prtFList.close();

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
            prtFList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	        failed(e, "Unexpected exception");
            }

    } // end Var002


    /**
     * Tests that a listCompleted event is fired when the
     * printer file list is completed. The list was opened
     * synchronously.
     **/
    public void Var003()
    {
	try
            {
            ListListener listListener = new ListListener(COMPLETED);

            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            prtFList.addPrintObjectListListener(listListener);

            // open the list
	        prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");
            prtFList.openSynchronously();

            // close the list
            prtFList.close();

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
            prtFList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	        failed(e, "Unexpected exception");
            }

    } // end Var003


    /**
     * Tests that a listObjectAdded event is fired when an      
     * object is added to the printer file list. The list
     * was opened synchronously.
     **/
    public void Var004()
    {
	try
            {
            ListListener listListener = new ListListener(OBJECTADDED);

            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // there will be only one printer file on the list
            objectAdded = null;

            // add the listener
            prtFList.addPrintObjectListListener(listListener);

            // open the list
	        prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");
            prtFList.openSynchronously();
           
            // get the printer file
            PrinterFile prtF = (PrinterFile)prtFList.getObject(0);

            // close the list
            prtFList.close();

            if( (listenerInvokedCount == 1) && (prtF.equals(objectAdded)) )  
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
            prtFList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	        failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests that a listErrorOccurred event is fired when an      
     * error occurs while retrieving the printer file list.
     * The list was opened synchronously.
     **/
    public void Var005()
    {
	try
            {
            ListListener listListener = new ListListener(ERROREVENT);

            // create a printer file list object using default constructor
            PrinterFileList prtFList = new PrinterFileList();

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // reset the list's exception
            exception = null;

            // add the listener
            prtFList.addPrintObjectListListener(listListener);

            // open the list
	        prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            boolean exceptionThrown = false;
            try
                {
                prtFList.openSynchronously();
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
            prtFList.removePrintObjectListListener(listListener);
            prtFList.close();
            } 

	catch (Exception e)
            {
	        failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests that a listOpened event is fired when the
     * printer file list is opened. The list was opened
     * asynchronously.
     **/
    public void Var006()
    {
	try
            {
            ListListener listListener = new ListListener(OPENED);

            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            prtFList.addPrintObjectListListener(listListener);

            // open the list
	        prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");
            prtFList.openAsynchronously();
            prtFList.waitForListToComplete();

            // close the list
            prtFList.close();

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
            prtFList.removePrintObjectListListener(listListener);
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
     * printer file list is closed. The list was opened
     * asynchronously.
     **/
    public void Var007()
    {
	try
            {
            ListListener listListener = new ListListener(CLOSED);

            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            prtFList.addPrintObjectListListener(listListener);

            // open the list
	        prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");
            prtFList.openAsynchronously();
            prtFList.waitForListToComplete();

            // close the list
            prtFList.close();

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
            prtFList.removePrintObjectListListener(listListener);
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
     * printer file list is completed. The list was opened
     * asynchronously.
     **/
    public void Var008()
    {
	try
            {
            ListListener listListener = new ListListener(COMPLETED);

            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            prtFList.addPrintObjectListListener(listListener);

            // open the list
	        prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");
            prtFList.openAsynchronously();
            prtFList.waitForListToComplete();

            // close the list
            prtFList.close();

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
            prtFList.removePrintObjectListListener(listListener);
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
     * object is added to the printer file list. The list
     * was opened asynchronously.
     **/
    public void Var009()
    {
	try
            {
            ListListener listListener = new ListListener(OBJECTADDED);

            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // there will be only one printer file on the list
            objectAdded = null;

            // add the listener
            prtFList.addPrintObjectListListener(listListener);

            // open the list
	         prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");
            prtFList.openAsynchronously();
            prtFList.waitForListToComplete();

            // get the printer file
            PrinterFile prtF = (PrinterFile)prtFList.getObject(0);

            // close the list
            prtFList.close();

            if( (listenerInvokedCount == 1) && (prtF.equals(objectAdded)) )  
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
            prtFList.removePrintObjectListListener(listListener);
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
     * error occurs while retrieving the printer file list.
     * The list was opened asynchronously.
     **/
    public void Var010()
    {
	try
            {
            ListListener listListener = new ListListener(ERROREVENT);

            // create a printer file list object using default constructor
            PrinterFileList prtFList = new PrinterFileList();

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // reset the list's exception
            exception = null;

            // add the listener
            prtFList.addPrintObjectListListener(listListener);

            // open the list
	        prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            boolean exceptionThrown = false;
            try
                {
                prtFList.openAsynchronously();
                prtFList.waitForListToComplete();
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
            prtFList.removePrintObjectListListener(listListener);
            prtFList.close();
            } 

	catch (Exception e)
            {
	        failed(e, "Unexpected exception");
            }

    } // end Var010

    /**
     Lists printer file(s) asynchronously with a listener
     **/
    public void Var011()
    {
	try
	{
	    // create a printer file list object
	    PrinterFileList prtFList = new PrinterFileList(systemObject_);

        // add the listener
        prtFList.addPrintObjectListListener(this);

	    prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");
	    prtFList.openAsynchronously();

	    boolean fDone = false;
	    int     listed = 0, size;
        boolean fSuccess = false;
        String prtFName = null;
	    do
	    {
	        //waitForWakeUp(); // @A1D
	    
	        // A "wait()" on the client does not accomplish the "polling"
	        // or "method waiting" state needed to receive events from the
	        // impl remote, and thus will not work in the proxied version
	        // of the code.  Instead, invoke waitForListToComplete() so there
	        // is a "method waiting", and events can then be heard on the client.
		    prtFList.waitForListToComplete();	        
		    
            fDone = prtFList.isCompleted();
		    size = prtFList.size();
		    while (listed < size)
		    {
                PrinterFile prtF = (PrinterFile)prtFList.getObject(listed++);
                prtFName = prtF.getPath();

                if (prtFName.equals("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE"))
                {
                   fSuccess = true;
                }
                else
                {
                   output_.println(" Bad printer file:"+prtFName);
                   fSuccess = false;
                   break;
                }

             } // end while 

             if (fListClosed)
             {
                failed(" Printer list Aborted!");
                break;
             }
             if (fListError)
             {
                failed(" Exception on list - " + listException);
                break;
             }

             if (!fSuccess)
             {
                failed("Incorrect Printer file listed: " + prtFName);
                break;
             }

	    } while (!fDone);

        // indicate how many files were listed
        output_.println(" " + listed + " Printer files listed.");

        // if we took an error clean up and get out.
        if ((fListClosed) || (fListError) || (!fSuccess))
        {
            prtFList.close();
            System.gc();
        }
        else
        {
            while (!prtFList.isCompleted())
            {
                Thread.sleep(200, 0);
            }

            prtFList.close();
            System.gc();

            succeeded();  // Note: This variation will be successful.
        }

        // remove the listener
        prtFList.removePrintObjectListListener(this);

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
     synchronized void waitForWakeUp()
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
        // fListOpened = true;
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

} // end NPPrtFListListenerTestcase class


