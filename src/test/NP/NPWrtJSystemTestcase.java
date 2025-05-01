///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPWrtJSystemTestcase.java
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
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.PrintParameterList;
import com.ibm.as400.access.Printer;
import com.ibm.as400.access.WriterJob;

import test.Testcase;

/**
 * Testcase NPWrtJSystemTestcase.
 **/
public class NPWrtJSystemTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPWrtJSystemTestcase";
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
    public NPWrtJSystemTestcase(AS400            systemObject,
                                Vector<String> variationsToRun,
                                int              runMode,
                                FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPWrtJSystemTestcase", 7,
	      variationsToRun, runMode, fileOutputStream);

    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPWrtJSystemTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // make sure JAVAPRINT printer device description exists
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTDEVPRT DEVD(JAVAPRINT) DEVCLS(*LCL) TYPE(*IPDS) MODEL(0) PORT(5) SWTSET(0) ONLINE(*NO) FONT(011)") == false)
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
            if (cmd.run("DLTDEVD JAVAPRINT") == false)
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
     * Tests retrieving the WriterJob's system.
     **/
    public void Var001()
    {
        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, "JAVAPRINT");

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms
            pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, pList, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            // @A1C - added 'getSystemName()'
            if (wrtJ.getSystem().getSystemName().equals(systemObject_.getSystemName())) succeeded();
            else failed("Could not retrive writer job system name.");

            // end the writer we started
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(2000,0);

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting the WriterJob's system to null.
     **/
    public void Var002()
    {
	try
	{
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, "JAVAPRINT");

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms
            pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, pList, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            try
                {
                wrtJ.setSystem(null);
                failed("Could set WriterJob system to null");
                }

            catch (NullPointerException e)
                {
                succeeded();
                }

            // end the writer we started
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(2000,0);
        }


	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var002

    /**
     * Tests that a property change event is fired when the
     * WriterJob's system is set.
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
        PropertyListener propertyListener = new PropertyListener();

        try
            {
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, "JAVAPRINT");

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms
            pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, pList, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            wrtJ.addPropertyChangeListener(propertyListener);

            // set the system
            wrtJ.setSystem(anotherSystem);

            // The listener will mark success/failure if invoked.
            if( !listenerInvoked )
                {
                failed("PropertyChange Listener was not invoked");
                }

            wrtJ.removePropertyChangeListener(propertyListener);

            // end the writer we started
            wrtJ.setSystem(systemObject_);
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(2000,0);
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests that a vetoable change event is fired when the
     * WriterJob's system is set.
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
                if( !(as400.getSystemName().equals(systemObject_.getSystemName()))) // @A1C
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
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, "JAVAPRINT");

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms
            pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, pList, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            // reset our flag
            listenerInvoked = false;

            // add the vetoable change listener
            wrtJ.addVetoableChangeListener(vetoableListener);

            // set the system
            wrtJ.setSystem(anotherSystem);

            // The listener will mark success/failure if invoked.
            if( !listenerInvoked )
                {
                failed("VetoableChange Listener was not invoked");
                }

            wrtJ.removeVetoableChangeListener(vetoableListener);

            // end the writer we started
            wrtJ.setSystem(systemObject_);
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(2000,0);
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests that a property change event is NOT fired when the
     * WriterJob's system property change is vetoed.
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
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, "JAVAPRINT");

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms
            pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, pList, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            wrtJ.addPropertyChangeListener(propertyListener);

            // add the vetoable change listener
            wrtJ.addVetoableChangeListener(vetoableListener);

            // set the system, consume PropertyVetoException
            try
                {
                wrtJ.setSystem(anotherSystem);
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

            wrtJ.removePropertyChangeListener(propertyListener);
            wrtJ.removeVetoableChangeListener(vetoableListener);

            // end the writer we started
            wrtJ.setSystem(systemObject_);
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(2000,0);
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
            }

    } // end Var005

    /**
     * Tests that a PropertyVetoException is thrown when the
     * WriterJob's system property change is vetoed.
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
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, "JAVAPRINT");

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms
            pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, pList, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            // add the vetoable change listener
            wrtJ.addVetoableChangeListener(vetoableListener);

            // set the system, a PropertyVetoException should be thrown
            try
                {
                wrtJ.setSystem(anotherSystem);
                failed("PropertyVetoException not thrown");
                }

            catch( PropertyVetoException e )
                {
                succeeded();
                }

            // remove the listener
            wrtJ.removeVetoableChangeListener(vetoableListener);

            // end the writer we started
            wrtJ.setSystem(systemObject_);
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(2000,0);
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
            // create a printer device object using valid system name and printer name
            Printer prtD = new Printer(systemObject_, "JAVAPRINT");

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms
            pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");

            // start a writer job using valid system name and printer name
            WriterJob wrtJ = WriterJob.start(systemObject_, prtD, pList, null);

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(2000,0);

            // reset our flag
            listenerInvoked = false;

            // add the property change and vetoable change listeners
            wrtJ.addPropertyChangeListener(propertyListener);
            wrtJ.addVetoableChangeListener(vetoableListener);

            // remove the property change and vetoable change listeners
            wrtJ.removePropertyChangeListener(propertyListener);
            wrtJ.removeVetoableChangeListener(vetoableListener);

            // set the system
            wrtJ.setSystem(anotherSystem);

            if( listenerInvoked )
                {
                failed("Listener was not removed");
                }
            else
                {
                succeeded();
                }

            // remove the listeners again, this should be OK.
            wrtJ.removePropertyChangeListener(propertyListener);
            wrtJ.removeVetoableChangeListener(vetoableListener);

            // end the writer we started
            wrtJ.setSystem(systemObject_);
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(2000,0);
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

} // end NPWrtJSystemTestcase class


