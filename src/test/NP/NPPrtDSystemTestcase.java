///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtDSystemTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Printer;

import test.Testcase;

/**
 * Testcase NPPrtDSystemTestcase.
 **/
public class NPPrtDSystemTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtDSystemTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    // was the listener invoked?
    boolean listenerInvoked;

    /**
     * Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtDSystemTestcase(AS400            systemObject,
                                Vector<String> variationsToRun,
                                int              runMode,
                                FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPPrtDSystemTestcase", 8,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtDSystemTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // make sure JAVAPRINT printer device description exists
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("QSYS/CRTDEVPRT DEVD(JAVAPRINT) DEVCLS(*LCL) TYPE(*IPDS) MODEL(0) PORT(5) SWTSET(0) ONLINE(*NO) FONT(011)") == false)
                {
                failed("Could not create a printer device description. "
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

            // delete the printer device description that we created
            if (cmd.run("QSYS/DLTDEVD JAVAPRINT") == false)
                {
                output_.println("Could not delete the printer device description we created. "
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
     * Tests retrieving the Printer's system.
     **/
    public void Var001()
    {
        try
            {
            // create an Printer object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, "JAVAPRINT");

            if (prtD.getSystem().equals(systemObject_)) succeeded();
            else failed("Could not retrive printer system.");

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting the Printer's system to null.
     **/
    public void Var002()
    {
	try
	{
            // create an Printer object using default constructor
            Printer prtD = new Printer();

            prtD.setSystem(null);
            failed("Could set Printer system to null");
	    
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
     * Tests getting the Printer's system before setting it.
     **/
    public void Var003()
    {
	try
	{
            // create an Printer object using the default constructor
            Printer prtD = new Printer();

            if( prtD.getSystem() == null )
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
     * Printer's system is set.
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
            // create an Printer object using the default constructor
            Printer prtD = new Printer();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            prtD.addPropertyChangeListener(propertyListener);

            // set the system
            prtD.setSystem(systemObject_);

            // The listener will mark success/failure if invoked.
            if( !listenerInvoked )
                {
                failed("PropertyChange Listener was not invoked");
                }

            prtD.removePropertyChangeListener(propertyListener);
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests that a vetoable change event is fired when the
     * Printer's system is set.
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
            // create an Printer object using the default constructor
            Printer prtD = new Printer();

            // reset our flag
            listenerInvoked = false;

            // add the vetoable change listener
            prtD.addVetoableChangeListener(vetoableListener);

            // set the system
            prtD.setSystem(systemObject_);

            // The listener will mark success/failure if invoked.
            if( !listenerInvoked )
                {
                failed("VetoableChange Listener was not invoked");
                }

            prtD.removeVetoableChangeListener(vetoableListener);
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests that a property change event is NOT fired when the
     * Printer's system property change is vetoed.
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
            // create an Printer object using the default constructor
            Printer prtD = new Printer();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            prtD.addPropertyChangeListener(propertyListener);

            // add the vetoable change listener
            prtD.addVetoableChangeListener(vetoableListener);

            // set the system, consume PropertyVetoException
            try
                {
                prtD.setSystem(systemObject_);
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

            prtD.removePropertyChangeListener(propertyListener);
            prtD.removeVetoableChangeListener(vetoableListener);
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
            }

    } // end Var006

    /**
     * Tests that a PropertyVetoException is thrown when the 
     * Printer's system property change is vetoed.
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
            // create an Printer object using the default constructor
            Printer prtD = new Printer();

            // add the vetoable change listener
            prtD.addVetoableChangeListener(vetoableListener);

            // set the system, a PropertyVetoException should be thrown
            try
                {
                prtD.setSystem(systemObject_);
                failed("PropertyVetoException not thrown");
                }

            catch( PropertyVetoException e )
                {
                succeeded();
                }

            // remove the listener
            prtD.removeVetoableChangeListener(vetoableListener);
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
            // create an Printer object using the default constructor
            Printer prtD = new Printer();

            // reset our flag
            listenerInvoked = false;

            // add the property change and vetoable change listeners
            prtD.addPropertyChangeListener(propertyListener);
            prtD.addVetoableChangeListener(vetoableListener);

            // remove the property change and vetoable change listeners
            prtD.removePropertyChangeListener(propertyListener);
            prtD.removeVetoableChangeListener(vetoableListener);

            // set the system
            prtD.setSystem(systemObject_);

            if( listenerInvoked )
                {
                failed("Listener was not removed");
                }
            else
                {
                succeeded();
                }

            // remove the listeners again, this should be OK.
            prtD.removePropertyChangeListener(propertyListener);
            prtD.removeVetoableChangeListener(vetoableListener);
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

} // end NPPrtDSystemTestcase class


