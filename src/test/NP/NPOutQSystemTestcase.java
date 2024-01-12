///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQSystemTestcase.java
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
 * Testcase NPOutQSystemTestcase.
 * Tests the setSystem method on OutputQueue class.
 **/
public class NPOutQSystemTestcase extends Testcase
{
    // was the listener invoked?
    boolean listenerInvoked;

    /**
     * Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQSystemTestcase(AS400            systemObject,
				 Vector           variationsToRun,
				 int              runMode,
				 FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPOutQSystemTestcase", 8,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQSystemTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create SYSTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/SYSTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/SYSTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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

	    // $$$ TO DO $$$
	    // Add an 'if' block using the following as a template for each variation.
	    // Make sure you correctly set the variation number
	    // and runmode in the 'if' condition, and the
	    // variation number in the setVariation call.
	    //
	    // replace the X with the variation number you are adding
	    // and uncomment the block of code
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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/SYSTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/SYSTST)") == false)
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
     * Tests retrieving the OutputQueue's system.
     **/
    public void Var001()
    {
        try
            {
            // create an output queue object using valid system name and queue name
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/SYSTST.OUTQ");

            if (outQ.getSystem().equals(systemObject_)) succeeded();
            else failed("Could not retrive OutputQueue system.");

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting the OutputQueue's system to null.
     **/
    public void Var002()
    {
	try
	{
            // create an output queue object using default constructor
            OutputQueue outQ = new OutputQueue();

            outQ.setSystem(null);
            failed("Could set OutputQueue system to null");
	    
	} // end try block

        catch (NullPointerException e)
	{
            succeeded();
	}

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var002

    /**
     * Tests getting the OutputQueue system before setting it.
     **/
    public void Var003()
    {
	try
	{
            // create an output queue object using the default constructor
            OutputQueue outQ = new OutputQueue();

            if( outQ.getSystem() == null )
                {
                succeeded();
                }
            else
                {
                failed("system was not set, expecting null");
                }

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var003

    /**
     * Tests that a property change event is fired when the
     * OutputQueue's system is set.
     **/

    public void Var004()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                listenerInvoked = true;
                boolean succeeded = true;
                String reason = "";

                if( !(evt.getPropertyName().equals("system")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(evt.getOldValue() == null) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(evt.getNewValue().equals(systemObject_)) )
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
            // create an Output Queue object using the default constructor
            OutputQueue outQ = new OutputQueue();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            outQ.addPropertyChangeListener(propertyListener);

            // set the system
            outQ.setSystem(systemObject_);

            // The listener will mark success/failure if invoked.
            if( !listenerInvoked )
                {
                failed("PropertyChange Listener was not invoked");
                }

            outQ.removePropertyChangeListener(propertyListener);
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests that a vetoable change event is fired when the
     * OutputQueue's system is set.
     **/

    public void Var005()
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

                if( !(evt.getPropertyName().equals("system")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(evt.getOldValue() == null) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(evt.getNewValue().equals(systemObject_)) )
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
            // create an Output Queue object using the default constructor
            OutputQueue outQ = new OutputQueue();

            // reset our flag
            listenerInvoked = false;

            // add the vetoable change listener
            outQ.addVetoableChangeListener(vetoableListener);

            // set the system
            outQ.setSystem(systemObject_);

            // The listener will mark success/failure if invoked.
            if( !listenerInvoked )
                {
                failed("VetoableChange Listener was not invoked");
                }

            outQ.removeVetoableChangeListener(vetoableListener);
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests that a property change event is NOT fired when the
     * OutputQueue's system property change is vetoed.
     **/
    public void Var006()
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

        // Define inner class to listen for vetoable change events
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException
            {
                // Always veto the property change
                throw new PropertyVetoException("I veto", evt);
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        try
            {
            // create an Output Queue object using the default constructor
            OutputQueue outQ = new OutputQueue();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            outQ.addPropertyChangeListener(propertyListener);

            // add the vetoable change listener
            outQ.addVetoableChangeListener(vetoableListener);

            // set the system, consume PropertyVetoException
            try
                {
                outQ.setSystem(systemObject_);
                }
            catch(PropertyVetoException e)
                {
                // For this variation just consume the PropertyVetoException
                }

            if( listenerInvoked )
                {
                failed("property change event fired after property change vetoed" );
                }
            else
                {
                succeeded();
                }

            outQ.removePropertyChangeListener(propertyListener);
            outQ.removeVetoableChangeListener(vetoableListener);
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
            }

    } // end Var006

    /**
     * Tests that a PropertyVetoException is thrown when the 
     * OutputQueue's system property change is vetoed.
     **/
    public void Var007()
    {
        // Define inner class to listen for vetoable change events
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException
            {
                // Always veto the property change
                throw new PropertyVetoException("I veto", evt);
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        try
            {
            // create an Output Queue object using the default constructor
            OutputQueue outQ = new OutputQueue();

            // add the vetoable change listener
            outQ.addVetoableChangeListener(vetoableListener);

            // set the system, a PropertyVetoException should be thrown
            try
                {
                outQ.setSystem(systemObject_);
                failed("PropertyVetoException not thrown");
                }

            catch( PropertyVetoException e )
                {
                succeeded();
                }

            // remove the listener
            outQ.removeVetoableChangeListener(vetoableListener);
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
            }

    } // end Var007

    /**
     * Tests that the property change and vetoable change listeners               
     * are actually removed.
     **/
    public void Var008()
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

        try
            {
            // create an Output Queue object using the default constructor
            OutputQueue outQ = new OutputQueue();

            // reset our flag
            listenerInvoked = false;

            // add the property change and vetoable change listeners
            outQ.addPropertyChangeListener(propertyListener);
            outQ.addVetoableChangeListener(vetoableListener);

            // remove the property change and vetoable change listeners
            outQ.removePropertyChangeListener(propertyListener);
            outQ.removeVetoableChangeListener(vetoableListener);

            // set the system
            outQ.setSystem(systemObject_);

            if( listenerInvoked )
                {
                failed("Listener was not removed");
                }
            else
                {
                succeeded();
                }

            // remove the listeners again, this should be OK.
            outQ.removePropertyChangeListener(propertyListener);
            outQ.removeVetoableChangeListener(vetoableListener);
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
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

} // end NPOutQSystemTestcase class


