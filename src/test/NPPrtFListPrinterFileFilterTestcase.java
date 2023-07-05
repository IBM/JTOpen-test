///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtFListPrinterFileFilterTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import java.util.Enumeration;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import com.ibm.as400.access.*;

/**
 Testcase NPPrtFListPrinterFileFilterTestcase.
 **/
public class NPPrtFListPrinterFileFilterTestcase extends Testcase
{
    // was the listener invoked?
    boolean listenerInvoked;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtFListPrinterFileFilterTestcase(AS400            systemObject,
                                               Vector           variationsToRun,
                                               int              runMode,
                                               FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPPrtFListPrinterFileFilterTestcase", 18,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtFListPrinterFileFilterTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create JAVAPRINT printer file
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTPRTF FILE(NPJAVA/JAVAPRINT)") == false)
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

            // run variation 18
            if ((allVariations || variationsToRun_.contains("18")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(18);
                Var018();
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
            if (cmd.run("DLTF FILE(NPJAVA/JAVAPRINT)") == false)
                {
                output_.println("Could not delete the printer file we created. "
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
     * Tests setting then getting the PrinterFileList printerFileFilter
     **/
    public void Var001()
    {
        try
            {
            // create an PrinterFileList object using default constructor
            PrinterFileList list = new PrinterFileList();

            // Set the printerFileFilter
            list.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            if (list.getPrinterFileFilter().trim().equals("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE")) succeeded();
            else failed("Could not set/retrive PrinterFileList printerFileFilter.");
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting the PrinterFileList printerFileFilter
     * to an invalid type.
     **/
    public void Var002()
    {
        try
            {
            // create an PrinterFileList object using default constructor
            PrinterFileList list = new PrinterFileList();

            // Set the printerFileFilter, OUTQ is not valid
            list.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.OUTQ");
            failed("Could set an invalid Printer File Type");
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
     * Tests setting the PrinterFileList printerFileFilter,
     * then removing the filter using an empty string.
     **/
    public void Var003()
    {
        try
            {
            // create an PrinterFileList object using default constructor
            PrinterFileList list = new PrinterFileList();

            // Set the printerFileFilter
            list.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // Now remove the filter
            list.setPrinterFileFilter("");

            if (list.getPrinterFileFilter().trim().equals("")) succeeded();
            else failed("Could not remove PrinterFileList printerFileFilter.");
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests setting the Printer File List's printerFileFilter to null.
     **/
    public void Var004()
    {
        try
            {
            // create an Printer File List object using default constructor
            PrinterFileList list = new PrinterFileList();

            list.setPrinterFileFilter(null);
            failed("Could set the printerFileFilter to null");
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
     * Tests retrieving the PrinterFileList's printerFileFilter
     * before it is set.
     **/
    public void Var005()
    {
        try
            {
            // create an PrinterFileList object using default constructor
            PrinterFileList list = new PrinterFileList();

            if( list.getPrinterFileFilter().length() == 0 )
                {
                succeeded();
                }
            else
                {
                failed("printerFileFilter was not set, expecting empty string");
                }
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     Lists printer file(s) synchronously filter by %ALL% libraries
     **/
    public void Var006()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.setPrinterFileFilter("/QSYS.LIB/%ALL%.LIB/%ALL%.FILE");

            prtFList.openSynchronously();

            // check to see if we got some printer filess
            // since there is at least 1 printer file on the system we should have gotten
            // at least 1 back in the list
            if (prtFList.size() >= 1)
                {
                // indicate how many printer files were listed
                output_.println(prtFList.size() + " Printer File(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list printer file(s) by %ALL% library name. Only "
                       +prtFList.size() + " printer file(s) listed.");
                }

            prtFList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var006

    /**
     Lists printer file(s) synchronously filter by %ALLUSR% libraries
     **/
    public void Var007()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.setPrinterFileFilter("/QSYS.LIB/%ALLUSR%.LIB/%ALL%.FILE");

            prtFList.openSynchronously();

            // check to see if we got some printer filess
            // since there is at least 1 printer file on the system we should have gotten
            // at least 1 back in the list
            if (prtFList.size() >= 1)
                {
                // indicate how many printer files were listed
                output_.println(prtFList.size() + " Printer File(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list printer file(s) by %ALLUSR% library name. Only "
                       +prtFList.size() + " printer file(s) listed.");
                }

            prtFList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var007

    /**
     Lists printer file(s) synchronously filter by %CURLIB% libraries
     **/
    public void Var008()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.setPrinterFileFilter("/QSYS.LIB/%CURLIB%.LIB/%ALL%.FILE");

            prtFList.openSynchronously();

            // check to see if we got some printer filess
            // since there is at least 1 printer file on the system we should have gotten
            // at least 1 back in the list
            if (prtFList.size() >= 1)
                {
                // indicate how many printer files were listed
                output_.println(prtFList.size() + " Printer File(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list printer file(s) by %CURLIB% library name. Only "
                       +prtFList.size() + " printer file(s) listed.");
                }

            prtFList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var008

    /**
     Lists printer file(s) synchronously filter by %LIBL% libraries
     **/
    public void Var009()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.setPrinterFileFilter("/QSYS.LIB/%LIBL%.LIB/%ALL%.FILE");

            prtFList.openSynchronously();

            // check to see if we got some printer filess
            // since there is at least 1 printer file on the system we should have gotten
            // at least 1 back in the list
            if (prtFList.size() >= 1)
                {
                // indicate how many printer files were listed
                output_.println(prtFList.size() + " Printer File(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list printer file(s) by %LIBL% library name. Only "
                       +prtFList.size() + " printer file(s) listed.");
                }

            prtFList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var009

    /**
     Lists printer file(s) synchronously filter by %USRLIBL% libraries
     **/
    public void Var010()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.setPrinterFileFilter("/QSYS.LIB/%USRLIBL%.LIB/%ALL%.FILE");

            prtFList.openSynchronously();

            // check to see if we got some printer filess
            // since there is at least 1 printer file on the system we should have gotten
            // at least 1 back in the list
            if (prtFList.size() >= 1)
                {
                // indicate how many printer files were listed
                output_.println(prtFList.size() + " Printer File(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list printer file(s) by %USRLIBL% library name. Only "
                       +prtFList.size() + " printer file(s) listed.");
                }

            prtFList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var010

    /**
     Lists printer file(s) synchronously filter by specific printer file name
     **/
    public void Var011()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            prtFList.openSynchronously();

            // retrieve the first printer file
            PrinterFile prtF = (PrinterFile)prtFList.getObject(0);

            // check to see if we got one printer file and verify name
            if ( (prtFList.size() == 1) &&
                 (prtF.getPath().trim().equals("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE")) )
                {
                // indicate how many printer files were listed
                output_.println(prtFList.size() + " Printer File(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list by a specific printer file name. Printer File was " + prtF.getPath()
                       + " Only "+prtFList.size()+" printer file(s) listed.");
                }

            prtFList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var011

    /**
     Lists printer file(s) synchronously filter by generic printer file name
     **/
    public void Var012()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/J*.FILE");

            prtFList.openSynchronously();

            Enumeration e = prtFList.getObjects();
            String prtFName = null;

            // check to see if we got some printer files
            if (prtFList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    PrinterFile prtF = (PrinterFile)e.nextElement();
                    prtFName = prtF.getPath();
                    if (prtFName.startsWith("/QSYS.LIB/NPJAVA.LIB/J"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad printer file:"+prtFName);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    // indicate how many printer files were listed
                    output_.println(prtFList.size() + " printer file(s) listed.");

                    succeeded();
                    }
                else
                    {
                    failed("Could not list printer file(s) by generic name");
                    }
                } // if size
            else 
                {
                failed("No printer files found.");
                }

            prtFList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var012

    /**
     Lists printer file(s) synchronously filter by %ALL% printer file name
     **/
    public void Var013()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.FILE");

            prtFList.openSynchronously();

            // check to see if we got some printer filess
            // since there is at least 1 printer file on the system we should have gotten
            // at least 1 back in the list
            if (prtFList.size() >= 1)
                {
                // indicate how many printer files were listed
                output_.println(prtFList.size() + " Printer File(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list printer file(s) by %ALL% printer file name. Only "
                       +prtFList.size() + " printer file(s) listed.");
                }

            prtFList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var013

    /**
     * Tests that a property change event is fired when the
     * PrinterFileList's printerFileFilter is set.
     **/
    public void Var014()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                listenerInvoked = true; 
                boolean succeeded = true;
                String reason = "";

                if( !(evt.getPropertyName().equals("printerFileFilter")) )
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
                                  "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE")) )
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
            // create an Printer File List object using the default constructor
            PrinterFileList list = new PrinterFileList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // set the printerFileFilter
            list.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

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

    } // end Var014

    /**
     * Tests that a vetoable change event is fired when the
     * PrinterFileList's printerFileFilter is set.
     **/
    public void Var015()
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

                if( !(evt.getPropertyName().equals("printerFileFilter")) )
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
                                  "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE")) )
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
            // create an Printer File List object using the default constructor
            PrinterFileList list = new PrinterFileList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the printerFileFilter
            list.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

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

    } // end Var015

    /**
     * Tests that a property change event is NOT fired when the
     * PrinterFileList's printerFileFilter property change is vetoed.
     **/
    public void Var016()
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
                if( evt.getPropertyName().equals("printerFileFilter") )
                    {
                    throw new PropertyVetoException("I veto", evt);
                    }
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        try
            {
            // create an Printer File List object using the default constructor
            PrinterFileList list = new PrinterFileList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the printerFileFilter, consume PropertyVetoException
            try
                {
                list.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");
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

    } // end Var016

    /**
     * Tests that a PropertyVetoException is thrown when the 
     * PrinterFileList's printerFileFilter property change is vetoed.
     **/
    public void Var017()
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

        PrinterFileList list = null;
        try
            {
            // create an Printer File List object using the default constructor
            list = new PrinterFileList();

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the system, a PropertyVetoException should be thrown
            list.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

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

    } // end Var017

    /**
     * Tests that the property change and vetoable change listeners               
     * are actually removed.
     **/
    public void Var018()
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

        PrinterFileList list = null;
        try
            {
            // create an Printer File List object using the default constructor
            list = new PrinterFileList();

            // reset our flag
            listenerInvoked = false;

            // add the property change and vetoable change listeners
            list.addPropertyChangeListener(propertyListener);
            list.addVetoableChangeListener(vetoableListener);

            // remove the property change and vetoable change listeners
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);

            // set the printerFileFilter 
            list.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

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

    } // end Var018

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

} // end NPPrtFListPrinterFileFilterTestcase class


