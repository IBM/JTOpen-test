///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQListQueueFilterTestcase.java
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase NPOutQListQueueFilterTestcase.
 **/
public class NPOutQListQueueFilterTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPOutQListQueueFilterTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    // was the listener invoked?
    boolean listenerInvoked;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQListQueueFilterTestcase(AS400            systemObject,
                                        Vector<String> variationsToRun,
                                        int              runMode,
                                        FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPOutQListQueueFilterTestcase", 17,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQListQueueFilterTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
            {
            // create LISTQTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/LISTQTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/LISTQTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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

            // run variation 12
            if ((allVariations || variationsToRun_.contains("12")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(12);
                Var012();
                }

            // run variation 13
            if ((allVariations || variationsToRun_.contains("13")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(13);
                Var013();
                }

            // run variation 14
            if ((allVariations || variationsToRun_.contains("14")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(14);
                Var014();
                }

            // run variation 15
            if ((allVariations || variationsToRun_.contains("15")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(15);
                Var015();
                }

            // run variation 16
            if ((allVariations || variationsToRun_.contains("16")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(16);
                Var016();
                }

            // run variation 17
            if ((allVariations || variationsToRun_.contains("17")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(17);
                Var017();
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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/LISTQTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/LISTQTST)") == false)
                {
                output_.println("Could not delete output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

	} // end try block

	catch( Exception e )
	{
	    failed(e, "Unexpected exception");
	}

    } // end run method

    /**
     * Tests setting the OutputQueueList queueFilter
     **/
    public void Var001()
    {
        try
            {
            // create an OutputQueueList object using default constructor
            OutputQueueList list = new OutputQueueList();

            // Set the queueFilter
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTQTST.OUTQ");

            if (list.getQueueFilter().trim().equals("/QSYS.LIB/NPJAVA.LIB/LISTQTST.OUTQ")) succeeded();
            else failed("Could not set/get OutputQueueList queueFilter");
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting the OutputQueueList queueFilter
     * to an invalid resource type.
     **/
    public void Var002()
    {
        try
            {
            // create an OutputQueueList object using default constructor
            OutputQueueList list = new OutputQueueList();

            // Set the queueFilter, FNTRSC is not valid, should be OUTQ.
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTQTST.FNTRSC");
            failed("Was able to set invalid type in filter");
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
     * Tests removing an OutputQueueList queueFilter by setting              
     * with an empty string.
     **/
    public void Var003()
    {
        try
            {
            // create an OutputQueueList object using default constructor
            OutputQueueList list = new OutputQueueList();

            // Set the queueFilter
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTQTST.OUTQ");

            // Now remove the filter
            list.setQueueFilter("");

            if (list.getQueueFilter().trim().equals("")) succeeded();
            else failed("Could not remove OutputQueueList queueFilter");
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests setting the OutputQueueList's queueFilter to null.
     **/
    public void Var004()
    {
        try
            {
            // create an OutputQueueList object using default constructor
            OutputQueueList list = new OutputQueueList();

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
     * Tests retrieving the OutputQueueList's queueFilter
     * before it is set.
     **/
    public void Var005()
    {
        try
            {
            // create an OutputQueueList object using default constructor
            OutputQueueList list = new OutputQueueList();

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
     * Tests listing all output queues on all libraries synchronously
     **/
    public void Var006()
    {
	try
	{
	    // create a output queue list object
	    OutputQueueList outQList = new OutputQueueList(systemObject_);

	    outQList.setQueueFilter("/QSYS.LIB/%ALL%.LIB/%ALL%.OUTQ");
	    outQList.openSynchronously();

            Enumeration e = outQList.getObjects();

            // check to see if we got some output queues
            // since there is at least 1 output queue on the system we should have gotten
            // at least 1 back in the list
            if (outQList.size() >= 1)
                {
                // indicate how many resources were listed
                output_.println(outQList.size() + " Output Queue(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list output queues by %ALL% library name. Only "+outQList.size()+" queues listed.");
                }

	    outQList.close();
	    System.gc();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var006

    /**
     * Tests listing a specific output queue synchronously.
     **/
    public void Var007()
    {
	try
	{
	    // create a printer list object
	    OutputQueueList outQList = new OutputQueueList(systemObject_);

	    outQList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTQTST.OUTQ");

	    outQList.openSynchronously();

            // retrieve the first output queue
            OutputQueue outQ = (OutputQueue)outQList.getObject(0);

            // check to see if we got one output queue and verify name
            if ( (outQList.size() == 1) &&
                 (outQ.getPath().trim().equals("/QSYS.LIB/NPJAVA.LIB/LISTQTST.OUTQ")) )
                {
                // indicate how many output queues were listed
                output_.println(outQList.size() + " Output Queue(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list by a specific output queue name. Output Queue was " + outQ.getPath()
                       + " Only "+outQList.size()+" output queue(s) listed.");
                }

	    outQList.close();
	    System.gc();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var007

    /**
     * Tests listing a generic output queue synchronously.
     **/
    public void Var008()
    {
	try
	{
	    // create a printer list object
	    OutputQueueList outQList = new OutputQueueList(systemObject_);

	    outQList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/L*.OUTQ");

	    outQList.openSynchronously();

            Enumeration e = outQList.getObjects();
            String outQPath = null;

            // check to see if we got some output queues
            if (outQList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    OutputQueue outQ = (OutputQueue)e.nextElement();
                    outQPath = outQ.getPath();
                    if (outQPath.startsWith("/QSYS.LIB/NPJAVA.LIB/L"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad output queue:"+outQPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    // indicate how many output queues were listed
                    output_.println(outQList.size() + " output queue(s) listed.");

                    succeeded();
                    }
                else
                    {
                    failed("Could not list output queues by generic queue name");
                    }
                } // if size
            else 
                {
                failed("No output queues found in NPJAVA Library.");
                }

	    outQList.close();
	    System.gc();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var008

    /**
     * Tests listing output queues on all user libraries synchronously.
     **/
    public void Var009()
    {
	try
	{
	    // create a printer list object
	    OutputQueueList outQList = new OutputQueueList(systemObject_);

	    outQList.setQueueFilter("/QSYS.LIB/%ALLUSR%.LIB/%ALL%.OUTQ");

	    outQList.openSynchronously();

            Enumeration e = outQList.getObjects();

            // check to see if we got some output queues
            // since there is at least 1 output queue on the system we should have gotten
            // at least 1 back in the list
            if (outQList.size() >= 1)
                {
                // indicate how many resources were listed
                output_.println(outQList.size() + " Output Queue(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list output queues by %ALLUSR% library name. Only "
                       +outQList.size() + " queues listed.");
                }

	    outQList.close();
	    System.gc();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var009

   /**
     * Tests listing output queues on current library synchronously.
     * ASSUMPTION: QGPL is CURLIB, by default job description.  If someone
     * has changed the default job description to not have curlib be QGPL then
     * this variation will fail.
     **/
    public void Var010()
    {
	try
	{
 	    // create a printer list object
	    OutputQueueList outQList = new OutputQueueList(systemObject_);

            // set the output queueFilter 
	    outQList.setQueueFilter("/QSYS.LIB/%CURLIB%.LIB/%ALL%.OUTQ");

            // now try to build list synchrously
	    outQList.openSynchronously();

            Enumeration e = outQList.getObjects();
            String outQPath = null;

            // check to see if we got some output queues
            if (outQList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    OutputQueue outQ = (OutputQueue)e.nextElement();
                    outQPath = outQ.getPath();
                    if (outQPath.startsWith("/QSYS.LIB/QGPL.LIB/"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad output queue:"+outQPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    // indicate how many output queues were listed
                    output_.println(outQList.size() + " output queue(s) listed.");

                    succeeded();
                    }
                else
                    {
                    failed("Could not list output queues by %CURLIB% library name");
                    }
                } // if size
            else 
                {
                failed("No output queues found in NPJAVA Library.");
                }

	    outQList.close();
	    System.gc();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var010

    /**
     * Tests listing output queues on library list synchronously.
     **/
    public void Var011()
    {
	try
	{
	    // create a printer list object
	    OutputQueueList outQList = new OutputQueueList(systemObject_);

	    outQList.setQueueFilter("/QSYS.LIB/%LIBL%.LIB/%ALL%.OUTQ");

	    outQList.openSynchronously();

	    Enumeration e = outQList.getObjects();

            // check to see if we got some output queues
            // since there is at least 1 output queue on the system we should have gotten
            // at least 1 back in the list
            if (outQList.size() >= 1)
                {
                // indicate how many resources were listed
                output_.println(outQList.size() + " Output Queue(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list output queues by %LIBL% library name. Only "
                       +outQList.size() + " queues listed.");
                }

	    outQList.close();
	    System.gc();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var011

    /**
     * Tests listing output queues on user library list synchronously.
     **/
    public void Var012()
    {
	try
	{
	    // create a printer list object
	    OutputQueueList outQList = new OutputQueueList(systemObject_);

	    outQList.setQueueFilter("/QSYS.LIB/%USRLIBL%.LIB/%ALL%.OUTQ");

	    outQList.openSynchronously();

	    Enumeration e = outQList.getObjects();

            // check to see if we got some output queues
            // since there is at least 1 output queue on the system we should have gotten
            // at least 1 back in the list
            if (outQList.size() >= 1)
                {
                // indicate how many resources were listed
                output_.println(outQList.size() + " Output Queue(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list output queues by %USRLIBL% library name. Only "
                       +outQList.size() + " queues listed.");
                }

	    outQList.close();
	    System.gc();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var012

    /**
     * Tests that a property change event is fired when the
     * OutputQueueList's queueFilter is set.
     **/
    public void Var013()
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
                                  "/QSYS.LIB/NPJAVA.LIB/LISTQTST.OUTQ")) )
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
            // create an Output Queue List object using the default constructor
            OutputQueueList list = new OutputQueueList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // set the queueFilter
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTQTST.OUTQ");

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

    } // end Var013

    /**
     * Tests that a vetoable change event is fired when the
     * OutputQueueList's queueFilter is set.
     **/
    public void Var014()
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
                                  "/QSYS.LIB/NPJAVA.LIB/LISTQTST.OUTQ")) )
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
            // create an Output Queue List object using the default constructor
            OutputQueueList list = new OutputQueueList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the queueFilter
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTQTST.OUTQ");

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

    } // end Var014

    /**
     * Tests that a property change event is NOT fired when the
     * OutputQueueList's queueFilter property change is vetoed.
     **/
    public void Var015()
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
            // create an Output Queue List object using the default constructor
            OutputQueueList list = new OutputQueueList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the queueFilter, consume PropertyVetoException
            try
                {
                list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTQTST.OUTQ");
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

    } // end Var015

    /**
     * Tests that a PropertyVetoException is thrown when the 
     * OutputQueueList's queueFilter property change is vetoed.
     **/
    public void Var016()
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

        OutputQueueList list = null;
        try
            {
            // create an Output Queue List object using the default constructor
            list = new OutputQueueList();

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the system, a PropertyVetoException should be thrown
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTQTST.OUTQ");

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

    } // end Var016

    /**
     * Tests that the property change and vetoable change listeners               
     * are actually removed.
     **/
    public void Var017()
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

        OutputQueueList list = null;
        try
            {
            // create an Output Queue List object using the default constructor
            list = new OutputQueueList();

            // reset our flag
            listenerInvoked = false;

            // add the property change and vetoable change listeners
            list.addPropertyChangeListener(propertyListener);
            list.addVetoableChangeListener(vetoableListener);

            // remove the property change and vetoable change listeners
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);

            // set the queueFilter 
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LISTQTST.OUTQ");

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

    } // end Var017

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

} // end NPOutQListQueueFilterTestcase class


