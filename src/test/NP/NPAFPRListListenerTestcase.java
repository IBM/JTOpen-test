///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPAFPRListListenerTestcase.java
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
 Testcase NPAFPRListListenerTestcase.
 **/
public class NPAFPRListListenerTestcase extends Testcase
implements PrintObjectListListener
{
    int listenerInvokedCount;
    AFPResource objectAdded;
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
    public NPAFPRListListenerTestcase(AS400            systemObject,
                                      Vector           variationsToRun,
                                      int              runMode,
                                      FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPAFPRListListenerTestcase", 11,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPAFPRListListenerTestcase");
        boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
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
            // run variation X
            if ((allVariations || variationsToRun_.contains("X")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(X);
                VarXXX();
                }
$$$ TO DO $$$ - delete this line */

            } // end try block

        catch( Exception e )
            {
            failed(e, "Unexpected exception");
            }

    } // end run method
    // @A1A - Added equalAFPR to compare two AFPResource objects
    public boolean equalsAFPR(AFPResource pf1, AFPResource pf2) {
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
                    objectAdded = (AFPResource)evt.getObject();
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
     * AFP resource list is opened. The list was opened
     * synchronously.
     **/
    public void Var001()
    {
	try
            {
            ListListener listListener = new ListListener(OPENED);

            // create a AFP resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            resList.addPrintObjectListListener(listListener);

            // open the list
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");
            resList.openSynchronously();

            // close the list
            resList.close();

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
            resList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests that a listClosed event is fired when the
     * AFP resource list is closed. The list was opened
     * synchronously.
     **/
    public void Var002()
    {
	try
            {
            ListListener listListener = new ListListener(CLOSED);

            // create a AFP resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            resList.addPrintObjectListListener(listListener);

            // open the list
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");
            resList.openSynchronously();

            // close the list
            resList.close();

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
            resList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests that a listCompleted event is fired when the
     * AFP resource list is completed. The list was opened
     * synchronously.
     **/
    public void Var003()
    {
	try
            {
            ListListener listListener = new ListListener(COMPLETED);

            // create a AFP resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            resList.addPrintObjectListListener(listListener);

            // open the list
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");
            resList.openSynchronously();

            // close the list
            resList.close();

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
            resList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests that a listObjectAdded event is fired when an      
     * object is added to the AFP resource list. The list
     * was opened synchronously.
     **/
    public void Var004()
    {
	try
            {
            ListListener listListener = new ListListener(OBJECTADDED);

            // create a AFP resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // there will be only one AFP resource on the list
            objectAdded = null;

            // add the listener
            resList.addPrintObjectListListener(listListener);

            // open the list
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");
            resList.openSynchronously();

            // get the AFP resource
            AFPResource res = (AFPResource)resList.getObject(0);

            // close the list
            resList.close();

            if( (listenerInvokedCount == 1) && (res.equals(objectAdded)) )
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
            resList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests that a listErrorOccurred event is fired when an      
     * error occurs while retrieving the AFP resource list.
     * The list was opened synchronously.
     **/
    public void Var005()
    {
	try
            {
            ListListener listListener = new ListListener(ERROREVENT);

            // create a AFP resource list object using default constructor
            AFPResourceList resList = new AFPResourceList();

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // reset the list's exception
            exception = null;

            // add the listener
            resList.addPrintObjectListListener(listListener);

            // open the list
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            boolean exceptionThrown = false;
            try
                {
                resList.openSynchronously();
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
            resList.removePrintObjectListListener(listListener);
            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests that a listOpened event is fired when the
     * AFP resource list is opened. The list was opened
     * asynchronously.
     **/
    public void Var006()
    {
	try
            {
            ListListener listListener = new ListListener(OPENED);

            // create a AFP resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            resList.addPrintObjectListListener(listListener);

            // open the list
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");
            resList.openAsynchronously();
            resList.waitForListToComplete();

            // close the list
            resList.close();

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
            resList.removePrintObjectListListener(listListener);
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
     * AFP resource list is closed. The list was opened
     * asynchronously.
     **/
    public void Var007()
    {
    try
            {
            ListListener listListener = new ListListener(CLOSED);

            // create a AFP resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            resList.addPrintObjectListListener(listListener);

            // open the list
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");
            resList.openAsynchronously();
            resList.waitForListToComplete();

            // close the list
            resList.close();

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
            resList.removePrintObjectListListener(listListener);
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
     * AFP resource list is completed. The list was opened
     * asynchronously.
     **/
    public void Var008()
    {
	try
            {
            ListListener listListener = new ListListener(COMPLETED);

            // create a AFP resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // add the listener
            resList.addPrintObjectListListener(listListener);

            // open the list
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");
            resList.openAsynchronously();
            resList.waitForListToComplete();

            // close the list
            resList.close();

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
            resList.removePrintObjectListListener(listListener);
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
     * object is added to the AFP resource list. The list
     * was opened asynchronously.
     **/
    public void Var009()
    {
	try
            {
            ListListener listListener = new ListListener(OBJECTADDED);

            // create a AFP resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // there will be only one AFP resource on the list
            objectAdded = null;

            // add the listener
            resList.addPrintObjectListListener(listListener);

            // open the list
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");
            resList.openAsynchronously();
            resList.waitForListToComplete();

            // get the AFP resource
            AFPResource res = (AFPResource)resList.getObject(0);

            // close the list
            resList.close();

            if( (listenerInvokedCount == 1) && (res.equals(objectAdded)) )
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
            resList.removePrintObjectListListener(listListener);
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
     * error occurs while retrieving the AFP resource list.
     * The list was opened asynchronously.
     **/
    public void Var010()
    {
	try
            {
            ListListener listListener = new ListListener(ERROREVENT);

            // create a AFP resource list object using default constructor
            AFPResourceList resList = new AFPResourceList();

            // reset the listener invoked count
            listenerInvokedCount = 0;

            // reset the list's exception
            exception = null;

            // add the listener
            resList.addPrintObjectListListener(listListener);

            // open the list
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            boolean exceptionThrown = false;
            try
                {
                resList.openAsynchronously();
                resList.waitForListToComplete();
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
            resList.removePrintObjectListListener(listListener);
            } 

    catch (Exception e)
        {
	     failed(e, "Unexpected exception");
        }

    } // end Var010


    /**
     Lists AFP Resources(s) asynchronously with a PrintObjectListListener
     **/
    public void Var011()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // add the listener
            resList.addPrintObjectListListener(this);

            // filter for the NPJAVA library
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.%ALL%");

            resList.openAsynchronously();

            boolean fDone = false;
            boolean fSuccess = false;
            int     listed = 0, size;
            String resPath = null;

            do
                { 
                //waitForWakeUp(); // @A1D
	    
	            // A "wait()" on the client does not accomplish the "polling"
	            // or "method waiting" state needed to receive events from the
	            // impl remote, and thus will not work in the proxied version
	            // of the code.  Instead, invoke waitForListToComplete() so there
	            // is a "method waiting", and events can then be heard on the client.
		        resList.waitForListToComplete();	
                
                fDone = resList.isCompleted();
                size = resList.size();
                while (listed < size)
                    {
                    AFPResource res = (AFPResource)resList.getObject(listed++);
                    resPath = res.getPath();
                    if (resPath.startsWith("/QSYS.LIB/NPJAVA.LIB"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println(" Bad resource:"+resPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while listed < size

                if (fListClosed)
                    {
                    failed(" AFPResourceList Aborted!");
                    break;
                    }

                if (fListError)
                    {
                    failed(" Exception on list - " + listException);
                    break;
                    }

                if (!fSuccess)
                    {
                    failed("Incorrect resource listed: " + resPath);
                    break;
                    }

                } while (!fDone);

                // indicate how many resources were listed
                output_.println(" " + listed + " AFP Resources listed.");

                // if we took an error clean up and get out.
                if ((fListClosed) || (fListError) || (!fSuccess))
                    {
                    resList.close();
                    System.gc();
                    }
                else
                    {
                    while (!resList.isCompleted())
                        {
                        Thread.sleep(200, 0);
                        }

                    resList.close();
                    System.gc();

                    succeeded();  // Note: This variation will be successful.
                    }

                // remove the listener
                resList.removePrintObjectListListener(this);

            } // end try block

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
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println(" Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
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

    /////////////////////
    // Private methods //
    /////////////////////

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

} // end NPAFPRListListenerTestcase class


