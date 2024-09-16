///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPAFPRListSpooledFileFilterTestcase.java
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
 Testcase NPAFPRListSpooledFileFilterTestcase.
 **/
public class NPAFPRListSpooledFileFilterTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPAFPRListSpooledFileFilterTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    // was the listener invoked?
    boolean listenerInvoked;

    // Define as a class variable so that the inner classes can reference
    ///private SpooledFile splF;  // the AS/400 native JVM won't let inner classes access private members
    SpooledFile splF;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPAFPRListSpooledFileFilterTestcase(AS400            systemObject,
                                            Vector           variationsToRun,
                                            int              runMode,
                                            FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPAFPRListSpooledFileFilterTestcase", 10,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPAFPRListSpooledFileFilterTestcase");
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
		runMode_ != UNATTENDED) // Note: This is an ATTENDED variation.
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
     * Tests setting then getting the AFPResourceList's spooledFileFilter
     **/
    public void Var001()
    {
        try
            {
            // create an AFP Resource List object using default constructor
            AFPResourceList list = new AFPResourceList();

            // Set the spooledFileFilter
            splF = createSpooledFile();
            list.setSpooledFileFilter( splF );

            if( list.getSpooledFileFilter().equals(splF) )
                {
                succeeded();
                }
            else
                {
                failed("Could not set/get the spooledFileFilter");
                }

            // clean up on AS/400
            splF.delete();
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
     * Tests setting the AFPResourceList's spooledFileFilter,
     * then removing the filter using null.
     **/
    public void Var002()
    {
        try
            {
            // create an AFP Resource List object using default constructor
            AFPResourceList list = new AFPResourceList();

            // Set the spooledFileFilter
            splF = createSpooledFile();
            list.setSpooledFileFilter( splF );

            // Now remove the filter
            list.setSpooledFileFilter(null);

            if (list.getSpooledFileFilter()==null) succeeded();
            else failed("Could not remove AFPResourceList spooledFileFilter.");

            // clean up on AS/400
            splF.delete();
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
     * Tests setting the AFPResourceList's spooledFileFilter to null.
     * This is valid and will remove the filter.
     **/
    public void Var003()
    {
        try
            {
            // create an AFP Resource List object using default constructor
            AFPResourceList list = new AFPResourceList();

            list.setSpooledFileFilter(null);
            succeeded();
            }

        catch (NullPointerException e)
            {
            failed("Could not set the spooledFileFilter to null");
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

    } // end Var003

    /**
     * Tests retrieving the AFPResourceList's spooledFileFilter
     * before it is set.
     **/
    public void Var004()
    {
        try
            {
            // create an AFP Resource List object using default constructor
            AFPResourceList list = new AFPResourceList();

            if( list.getSpooledFileFilter() == null )
                {
                succeeded();
                }
            else
                {
                failed("spooledFileFilter was not set, expecting null");
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

    } // end Var004

    /**
     * Lists AFP Resource(s) filtering by spooled file.
     **/
    public void Var005()
    {
        try
            {
            // ask the user to override the printer file and generate some output
            // to create a spooled file with a resource attached.
            // This has to be manually done, since the override is removed after each
            // command.
        	if (true) { 
        	throw new Exception("Testcase change needed"); 
        	/* 
            TestInstructions instructions =
              new TestInstructions("Please perform the following two commands on the same AS400 using the same userid:\n\nOVRPRTF FILE(QSYSPRT) DEVTYPE(*AFPDS) FRONTOVL(NPJAVA/QOOD2L) SPLFNAME(JAVASPOOL)\nPRTIPSCFG\n\nPress OK when complete.");

            instructions.display();
            */ 
        	}
           // retrieve the spooled file that was created
	    SpooledFileList splFileList = new SpooledFileList(systemObject_);
            splFileList.openSynchronously();

	    Enumeration enumeration = splFileList.getObjects();
            splF = null;

	    while( enumeration.hasMoreElements() )
	    {
		splF = (SpooledFile)enumeration.nextElement();
                if (splF.getName().equals("JAVASPOOL")) break;               
	    }

            // close the list we opened
            splFileList.close();

            if (splF == null)
                {
                failed("Could not find out spooled file that was created.");
                }
            else
                {
                // create an AFP Resource list object
                AFPResourceList resList = new AFPResourceList(systemObject_);

                // filter by spooled file
                resList.setSpooledFileFilter(splF);

                // now try to build resource list synchronously
                resList.openSynchronously();

                enumeration =resList.getObjects();
                AFPResource res = null;

                while( enumeration.hasMoreElements() )
                    {
                    res = (AFPResource)enumeration.nextElement();
                    if (res.getPath().trim().equals("/QSYS.LIB/NPJAVA.LIB/QOOD2L.OVL"))
                        {
                        // indicate how many resources were listed
                        output_.println(resList.size() + " AFP Resources listed.");

                        succeeded();
                        break;
                        }
                    } // while

                // check to see if we got any resources back
                if (res == null)
                    {
                    failed("Could not list by spooledFileFilter.");
                    }

                // delete the spooled file we created
                splF.delete();
                
                resList.close();
                System.gc();
               
                } // end else splF was not null

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

    } // end Var005

    /**
     * Tests that a property change event is fired when the
     * AFPResourceList's spooledFileFilter is set.
     **/
    public void Var006()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                listenerInvoked = true;
                boolean succeeded = true;
                String reason = "";

                if( !(evt.getPropertyName().equals("spooledFileFilter")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(evt.getOldValue() == null) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(((SpooledFile)evt.getNewValue()).equals(splF)) )
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
            // create an AFP Resource object using the default constructor
            AFPResourceList list = new AFPResourceList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // set the spooledFileFilter
            splF = createSpooledFile();
            list.setSpooledFileFilter(splF);

            // The listener will mark success/failure if invoked.
            if( !listenerInvoked )
                {
                failed("PropertyChange Listener was not invoked");
                }

            // clean up on AS/400
            splF.delete();

            // remove the listener
            list.removePropertyChangeListener(propertyListener);
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

    } // end Var006

    /**
     * Tests that a vetoable change event is fired when the
     * AFPResourceList's spooledFileFilter is set.
     **/
    public void Var007()
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

                if( !(evt.getPropertyName().equals("spooledFileFilter")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(evt.getOldValue() == null) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(((SpooledFile)evt.getNewValue()).equals(splF)) )
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
            // create an AFP Resource object using the default constructor
            AFPResourceList list = new AFPResourceList();

            // reset our flag
            listenerInvoked = false;

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the spooledFileFilter
            splF = createSpooledFile();
            list.setSpooledFileFilter(splF);

            // The listener will mark success/failure if invoked.
            if( !listenerInvoked )
                {
                failed("PropertyChange Listener was not invoked");
                }

            // clean up on AS/400
            splF.delete();

            // remove the listener 
            list.removeVetoableChangeListener(vetoableListener);
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

    } // end Var007

    /**
     * Tests that a property change event is NOT fired when the
     * AFPResoureList's spooledFileFilter property change is vetoed.
     **/
    public void Var008()
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
                // always veto the property change
                throw new PropertyVetoException("I veto", evt);
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        try
            {
            // create an AFP Resource object using the default constructor
            AFPResourceList list = new AFPResourceList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // Set the spooledFileFilter, consume PropertyVetoException
            splF = createSpooledFile();
            try
                {
                list.setSpooledFileFilter(splF);
                }
            catch(PropertyVetoException e)
                {
                // For this variation just
                // consume the PropertyVetoException
                }

            if( listenerInvoked )
                {
                failed("property change event fired after property change vetoed");
                }
            else
                {
                succeeded();
                }

            // clean up on AS/400
            splF.delete();

            // remove the listeners
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);
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

    /**
     * Tests that a PropertyVetoException is thrown when the 
     * AFPResourceList's spooledFileFilter property change is vetoed.
     **/
    public void Var009()
    {
        // Define inner class to listen for vetoable change events.
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

        AFPResourceList list = null;
        try
            {
            // create an AFP Resource object using the default constructor
            list = new AFPResourceList();

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the spooledFileFilter,
            // a PropertyVetoException should be thrown
            splF = createSpooledFile();
            try
                {
                list.setSpooledFileFilter(splF);
                failed("PropertyVetoException not thrown");
                }
            catch(PropertyVetoException e)
                {
                succeeded();
                }

            // remove the listener
            list.removeVetoableChangeListener(vetoableListener);

            // clean up on AS/400
            splF.delete();
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

    } // end Var009

    /**
     * Tests that the property change and property veto listeners               
     * are actually removed.
     **/
    public void Var010()
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

        // Define inner class to listen for vetoable property
        // change events.
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
            // create an AFP Resource object using the default constructor
            AFPResourceList list = new AFPResourceList();

            // add the property change and vetoable change listeners
            list.addPropertyChangeListener(propertyListener);
            list.addVetoableChangeListener(vetoableListener);

            // remove the property change and vetoable change listeners
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);

            // set the spooledFileFilter
            splF = createSpooledFile();
            list.setSpooledFileFilter(splF);

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

            // clean up on AS/400
            splF.delete();
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

    } // end Var010

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

} // end NPAFPRListSpooledFileFilterTestcase class


