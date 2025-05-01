///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFSystemTestcase.java
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
import java.io.IOException;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.access.SpooledFileOutputStream;

import test.Testcase;

/**
 * Testcase NPSplFSystemTestcase.
 **/
public class NPSplFSystemTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFSystemTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    // was the listener invoked?
    boolean listenerInvoked;

    // used to test listener
    AS400 anotherSystem = new AS400();

    /**
     * Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFSystemTestcase(AS400            systemObject,
                                Vector<String> variationsToRun,
                                int              runMode,
                                FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPSplFSystemTestcase", 7,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFSystemTestcase");
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

	} // end try block

	catch( Exception e )
	{
	    failed(e, "Unexpected exception");
	}

    } // end run method

    /**
     * Tests retrieving the SpooledFile's system.
     **/
    public void Var001()
    {
        try
            {
            // create a spooled file
            SpooledFile splF = createSpooledFile();

	    // @A1C - added 'getSystemName()'
            if (splF.getSystem().getSystemName().equals(systemObject_.getSystemName())) succeeded(); // @A1C
            else failed("Could not retrive SpooledFile system.");

            // clean up on AS/400
            splF.delete();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting the SpooledFile's system to null.
     **/
    public void Var002()
    {
	try
	{
            // create an Spooled File object using default constructor
            SpooledFile splF = createSpooledFile();

            try
                {
                splF.setSystem(null);
                failed("Could set SpooledFile system to null");
                }

            catch (NullPointerException e)
                {
                succeeded();
                }

            // clean up on AS/400
            splF.delete();
	    
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var002

    /**
     * Tests that a property change event is fired when the
     * SpooledFile's system is set.
     **/

    public void Var003()
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

                // @A1A - cast old value to AS400 to use getSystemName()
                AS400 as400 = (AS400) evt.getOldValue();                             // @A1A
                if( !(as400.getSystemName().equals(systemObject_.getSystemName())) ) // @A1C
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(evt.getNewValue().equals(anotherSystem)) )
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
            // create an Spooled File object using the default constructor
            SpooledFile splF = createSpooledFile();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            splF.addPropertyChangeListener(propertyListener);

            // set the system
            splF.setSystem(anotherSystem);

            // The listener will mark success/failure if invoked.
            if( !listenerInvoked )
                {
                failed("PropertyChange Listener was not invoked");
                }

            splF.removePropertyChangeListener(propertyListener);

            // clean up on AS/400
            splF.setSystem(systemObject_);
            splF.delete();
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests that a vetoable change event is fired when the
     * SpooledFile's system is set.
     **/

    public void Var004()
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

                // @A1A - cast old value to AS400 to use getSystemName()
                AS400 as400 = (AS400) evt.getOldValue();                            // @A1A
                if( !(as400.getSystemName().equals(systemObject_.getSystemName())) )// @A1C
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(evt.getNewValue().equals(anotherSystem)) )
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
            // create an Spooled File object using the default constructor
            SpooledFile splF = createSpooledFile();

            // reset our flag
            listenerInvoked = false;

            // add the vetoable change listener
            splF.addVetoableChangeListener(vetoableListener);

            // set the system
            splF.setSystem(anotherSystem);

            // The listener will mark success/failure if invoked.
            if( !listenerInvoked )
                {
                failed("VetoableChange Listener was not invoked");
                }

            splF.removeVetoableChangeListener(vetoableListener);

            // clean up on AS/400
            splF.setSystem(systemObject_);
            splF.delete();
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests that a property change event is NOT fired when the
     * SpooledFile's system property change is vetoed.
     **/
    public void Var005()
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
            // create an Spooled File object using the default constructor
            SpooledFile splF = createSpooledFile();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            splF.addPropertyChangeListener(propertyListener);

            // add the vetoable change listener
            splF.addVetoableChangeListener(vetoableListener);

            // set the system, consume PropertyVetoException
            try
                {
                splF.setSystem(anotherSystem);
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

            splF.removePropertyChangeListener(propertyListener);
            splF.removeVetoableChangeListener(vetoableListener);

            // clean up on AS/400
            splF.setSystem(systemObject_);
            splF.delete();
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
            }

    } // end Var005

    /**
     * Tests that a PropertyVetoException is thrown when the 
     * SpooledFile's system property change is vetoed.
     **/
    public void Var006()
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
            // create an Spooled File object using the default constructor
            SpooledFile splF = createSpooledFile();

            // add the vetoable change listener
            splF.addVetoableChangeListener(vetoableListener);

            // set the system, a PropertyVetoException should be thrown
            try
                {
                splF.setSystem(anotherSystem);
                failed("PropertyVetoException not thrown");
                }

            catch( PropertyVetoException e )
                {
                succeeded();
                }

            // remove the listener
            splF.removeVetoableChangeListener(vetoableListener);

            // clean up on AS/400
            splF.setSystem(systemObject_);
            splF.delete();
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
            }

    } // end Var006

    /**
     * Tests that the property change and vetoable change listeners               
     * are actually removed.
     **/
    public void Var007()
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
            // create an Spooled File object using the default constructor
            SpooledFile splF = createSpooledFile();

            // reset our flag
            listenerInvoked = false;

            // add the property change and vetoable change listeners
            splF.addPropertyChangeListener(propertyListener);
            splF.addVetoableChangeListener(vetoableListener);

            // remove the property change and vetoable change listeners
            splF.removePropertyChangeListener(propertyListener);
            splF.removeVetoableChangeListener(vetoableListener);

            // set the system
            splF.setSystem(anotherSystem);

            if( listenerInvoked )
                {
                failed("Listener was not removed");
                }
            else
                {
                succeeded();
                }

            // remove the listeners again, this should be OK.
            splF.removePropertyChangeListener(propertyListener);
            splF.removeVetoableChangeListener(vetoableListener);

            // clean up on AS/400
            splF.setSystem(systemObject_);
            splF.delete();
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
            }

    } // end Var007

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

    // This method creates a spooled file
    private SpooledFile createSpooledFile()
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException
    {
        // buffer for data
        byte[] buf = new byte[2048];

        // fill the buffer
        for (int i=0; i<2048; i++) buf[i] = (byte)'9';

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, null);

        // write some data
        outStream.write(buf);

        // close the spooled file
        outStream.close();

        // return the new SpooledFile
        return outStream.getSpooledFile();

    } // end createSpooledFile

} // end NPSplFSystemTestcase class


