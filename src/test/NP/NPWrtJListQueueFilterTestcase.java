///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPWrtJListQueueFilterTestcase.java
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase NPWrtJListQueueFilterTestcase.
 **/
public class NPWrtJListQueueFilterTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPWrtJListQueueFilterTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    // the printer device name
    String printer_ = null;

    // was the listener invoked?
    boolean listenerInvoked;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPWrtJListQueueFilterTestcase(AS400            systemObject,
                                         Vector<String> variationsToRun,
                                         int              runMode,
                                         FileOutputStream fileOutputStream,
                                         
                                         String           printer)
      throws IOException
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPWrtJListQueueFilterTestcase", 11,
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
        output_.println("NPPrintTest::NPWrtJListQueueFilterTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);
        boolean fWriterEnded = false;

	try
            {
            // create QUETST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/QUETST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/QUETST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/QUETST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/QUETST)") == false)
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
     * Tests setting then getting the WriterJobList's queueFilter
     **/
    public void Var001()
    {
        try
            {
            // create an WriterJobList's object using default constructor
            WriterJobList list = new WriterJobList();

            // Set the queueFilter
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            if (list.getQueueFilter().trim().equals("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ")) succeeded();
            else failed("Could not set/get WriterJobList queueFilter.");
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting the WriterJobList's queueFilter
     * to an invalid type.
     **/
    public void Var002()
    {
        try
            {
            // create an WriterJobList object using default constructor
            WriterJobList list = new WriterJobList();

            // Set the queueFilter, FILE is not valid
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.FILE");

            failed("Could set an invalid WriterJobList queueFilter Type");
            } 

        catch (IllegalPathNameException e)
            {
            succeeded();
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting the WriterJobList's queueFilter,
     * then removing the filter using an empty string.
     **/
    public void Var003()
    {
        try
            {
            // create an WriterJobList object using default constructor
            WriterJobList list = new WriterJobList();

            // Set the queueFilter
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            // Now remove the filter
            list.setQueueFilter("");

            if (list.getQueueFilter().trim().equals("")) succeeded();
            else failed("Could not remove WriterJobList queueFilter.");
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests setting the WriterJobList's queueFilter to null.
     **/
    public void Var004()
    {
        try
            {
            // create an WriterJobList object using default constructor
            WriterJobList list = new WriterJobList();

            list.setQueueFilter(null);
            failed("Could set the queueFilter to null");
            } 

        catch (NullPointerException e)
            {
            succeeded();
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests retrieving the WriterJobList's queueFilter
     * before it is set.
     **/
    public void Var005()
    {
        try
            {
            // create an WriterJobList object using default constructor
            WriterJobList list = new WriterJobList();

            if( list.getQueueFilter().length() == 0 )
                {
                succeeded();
                }
            else
                {
                failed("queueFilter was not set, expecting empty string");
                }
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests listing writer job(s) using a specific library queueFilter
     **/
    public void Var006()
    {
        try
            {
            // start a writer job
            WriterJob wrtJ = startWriter();

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            // create a Writer Job List object
            WriterJobList wrtJList = new WriterJobList(systemObject_);

            // set the output queueFilter 
            wrtJList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            // now try to build list synchrously
            wrtJList.openSynchronously();

            int listed = 0, size;
            size = wrtJList.size();
            String wrtJName = null;

            WriterJob wrtJob = null;

            // check to see if we got at least 1 writer job(s)
            if (size >= 1)
                {
                boolean fSuccess = false;

                // check the writer job we go back
                while (listed < size)
                    {
                    wrtJob = (WriterJob)wrtJList.getObject(listed++);
                    wrtJName = wrtJob.getName();
                    if (wrtJName.equals("JAVAPRINT"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
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
                failed("Not enough writer jobs found.");
                }

            // close the list
            wrtJList.close();

            // end the writer job we started
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(2000,0);

            System.gc();
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var006

    /**
     * Tests that a property change event is fired when the
     * WriterJobList's queueFilter is set.
     **/
    public void Var007()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                listenerInvoked = true; 
                boolean succeeded = true;
                String reason = "";

                if( !(evt.getPropertyName().equals("queueFilter")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(((String)evt.getOldValue()).equals("")) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(((String)evt.getNewValue()).equals(
                                  "/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ")) )
                    {
                    succeeded = false;
                    reason = "Problem with new value";
                    }

                if( succeeded )
                    {
                    succeeded();
                    }
                else
                    {
                    failed(reason);
                    }
            }
        }
        PropertyListener propertyListener = new PropertyListener();

        try
            {
            // create an Spooled File List object using the default constructor
            WriterJobList list = new WriterJobList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // set the queueFilter
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            if( !listenerInvoked )
                {
                failed("PropertyChange Listener was not invoked");
                }

            list.removePropertyChangeListener(propertyListener);
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var007

    /**
     * Tests that a vetoable change event is fired when the
     * WriterJobList's queueFilter is set.
     **/
    public void Var008()
    {
        // Define inner class to listen for vetoable change events
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException   
            {
                listenerInvoked = true;
                boolean succeeded = true;
                String reason = "";

                if( !(evt.getPropertyName().equals("queueFilter")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(((String)evt.getOldValue()).equals("")) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(((String)evt.getNewValue()).equals(
                                  "/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ")) )
                    {
                    succeeded = false;
                    reason = "Problem with new value";
                    }

                if( succeeded )
                    {
                    succeeded();
                    }
                else
                    {
                    failed(reason);
                    }
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        try
            {
            // create an Spooled File List object using the default constructor
            WriterJobList list = new WriterJobList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the queueFilter
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            if( !listenerInvoked )
                {
                failed("VetoableChange Listener was not invoked");
                }

            list.removeVetoableChangeListener(vetoableListener);
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var008

    /**
     * Tests that a property change event is NOT fired when the
     * WriterJobList's queueFilter property change is vetoed.
     **/
    public void Var009()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                // If this listener gets invoked the veto was ignored.
                listenerInvoked = true;
            }
        }
        PropertyListener propertyListener = new PropertyListener();

        // Define inner class to listen for vetoable change events.
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException
            {
                // Check the property name.
                if( evt.getPropertyName().equals("queueFilter") )
                    {
                    throw new PropertyVetoException("I veto", evt);
                    }
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        try
            {
            // create an Spooled File List object using the default constructor
            WriterJobList list = new WriterJobList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the queueFilter, consume PropertyVetoException
            try
                {
                list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");
                }
            catch(PropertyVetoException e)
                {
                // For this variation, consume the PropertyVetoException
                }

            if( listenerInvoked )
                {
                failed("PropertyChange event fired after property change vetoed");
                }
            else
                {
                succeeded();
                }

            // remove the listeners
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
            }

    } // end Var009

    /**
     * Tests that a PropertyVetoException is thrown when the 
     * WriterJobList's queueFilter property change is vetoed.
     **/
    public void Var010()
    {
        // Define inner class to listen for vetoable change events
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException
            {
                // always veto the property change
                throw new PropertyVetoException("I veto", evt);
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        WriterJobList list = null;
        try
            {
            // create an Spooled File List object using the default constructor
            list = new WriterJobList();

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the queueFilter, a PropertyVetoException should be thrown
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            failed("Expecting PropertyVetoException");
            }

        catch( PropertyVetoException e )
            {
            succeeded();
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
            }

        finally
            {
            // remove the listener
            list.removeVetoableChangeListener(vetoableListener);
            }

    } // end Var010

    /**
     * Tests that the property change and vetoable change listeners               
     * are actually removed.
     **/
    public void Var011()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                listenerInvoked = true;
            }
        }
        PropertyListener propertyListener = new PropertyListener();

        // Define inner class to listen for vetoable change events 
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException
            {
                listenerInvoked = true;
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        WriterJobList list = null;
        try
            {
            // create an Spooled File List object using the default constructor
            list = new WriterJobList();

            // reset our flag
            listenerInvoked = false;

            // add the property change and vetoable change listeners
            list.addPropertyChangeListener(propertyListener);
            list.addVetoableChangeListener(vetoableListener);

            // remove the property change and vetoable change listeners
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);

            // set the queueFilter 
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            if( listenerInvoked )
                {
                failed("Listener was not removed");
                }
            else
                {
                succeeded();
                }

            // remove the listeners again, this should be OK.
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
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
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

        // start a writer job
        return WriterJob.start(systemObject_, prtD, pList, outQ);

    } // end startWriter
} // end NPWrtJListQueueFilterTestcase class


