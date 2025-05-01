///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPAFPRListSystemTestcase.java
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

import com.ibm.as400.access.AFPResourceList;
import com.ibm.as400.access.AS400;

import test.Testcase;

/**
 Testcase NPAFPRListSystemTestcase.
 **/
public class NPAFPRListSystemTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPAFPRListSystemTestcase";
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
    public NPAFPRListSystemTestcase(AS400            systemObject,
                                    Vector<String> variationsToRun,
                                    int              runMode,
                                    FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPAFPRListSystemTestcase", 8,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPAFPRListSystemTestcase");
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

	} // end try block

	catch( Exception e )
	{
	    failed(e, "Unexpected exception");
	}

    } // end run method

    /**
     * Tests setting the AFPResourceList's system to null.
     **/
    public void Var001()
    {
        try
            {
            // create an AFP Resource List object using default constructor
            AFPResourceList list = new AFPResourceList();

            list.setSystem(null);
            list.close(); 
            failed("Could set system to null");
            } 

        catch (NullPointerException e)
            {
            succeeded();
            }

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests retrieving the AFPResourceList's system before it is set.
     **/
    public void Var002()
    {
        try
            {
            // create an AFP Resource List object using default constructor
            AFPResourceList list = new AFPResourceList();

            if( list.getSystem() == null )
                {
              list.close(); 

                succeeded();
                }
            else
                {
                failed("system was not set, expecting null");
                }
            } 

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests retrieving the AFPResourceList's system.
     **/
    public void Var003()
    {
        try
            {
            // create an AFP Resource List object using valid system name 
            AFPResourceList resList = new AFPResourceList(systemObject_);

            if (resList.getSystem().equals(systemObject_)) succeeded();
            else failed("Could not retrive AFP Resource List system.");
            resList.close(); 

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests that a property change event is fired when the
     * AFPResourceList's system is set.
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
            // create an AFP Resource List object using the default constructor
            AFPResourceList list = new AFPResourceList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // set the system
            list.setSystem(systemObject_);

            // The listener will mark success/failure if invoked.
            if( !listenerInvoked )
                {
                failed("PropertyChange Listener was not invoked");
                }

            list.removePropertyChangeListener(propertyListener);
            list.close(); 

            }

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else
                failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests that a vetoable change event is fired when the
     * AFPResourceList's system is set.
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
            // create an AFP Resource List object using the default constructor
            AFPResourceList list = new AFPResourceList();

            // reset our flag
            listenerInvoked = false;

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the system
            list.setSystem(systemObject_);

            // The listener will mark success/failure if invoked.
            if( !listenerInvoked )
                {
                failed("VetoableChange Listener was not invoked");
                }

            list.removeVetoableChangeListener(vetoableListener);
            list.close(); 

            } 

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else
                failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests that a property change event is NOT fired when the
     * AFPResourceList's system property change is vetoed.
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
            // create an AFP Resource List object using the default constructor
            AFPResourceList list = new AFPResourceList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the system, consume PropertyVetoException
            // in this variation.
            try
                {
                list.setSystem(systemObject_);
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

            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);
            list.close(); 

            }

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else
                {
                failed(e, "Unexpected exception" );
                }
            }

    } // end Var006

    /**
     * Tests that a PropertyVetoException is thrown when the 
     * AFPResourceList's system property change is vetoed.
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
            // create an AFP Resource List object using the default constructor
            AFPResourceList list = new AFPResourceList();

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the system, a PropertyVetoException should be thrown
            try
                {
                list.setSystem(systemObject_);
                failed("PropertyVetoException not thrown");
                }

            catch( PropertyVetoException e )
                {
                succeeded();
                }

            // remove the listener
            list.removeVetoableChangeListener(vetoableListener);
            list.close(); 

            }


        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else
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
            // create an AFP Resource List object using the default constructor
            AFPResourceList list = new AFPResourceList();

            // reset our flag
            listenerInvoked = false;

            // add the property change and vetoable change listeners
            list.addPropertyChangeListener(propertyListener);
            list.addVetoableChangeListener(vetoableListener);

            // remove the property change and vetoable change listeners
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);

            // set the system
            list.setSystem(systemObject_);

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
            list.close(); 

            } 

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else
                {
                failed(e, "Unexpected exception" );
                }
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

} // end NPAFPRListSystemTestcase class


