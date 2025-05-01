///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtDListPrinterFilterTestcase.java
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
import java.util.Enumeration;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.Printer;
import com.ibm.as400.access.PrinterList;

import test.Testcase;

/**
 Testcase NPPrtDListPrinterFilterTestcase.
 **/
public class NPPrtDListPrinterFilterTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtDListPrinterFilterTestcase";
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
    public NPPrtDListPrinterFilterTestcase(AS400            systemObject,
                                           Vector<String> variationsToRun,
                                           int              runMode,
                                           FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPPrtDListPrinterFilterTestcase", 14,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtDListPrinterFilterTestcase");
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
     * Tests setting then getting the PrinterList printerFilter
     **/
    public void Var001()
    {
        try
            {
            // create an Printer List object using default constructor
            PrinterList list = new PrinterList();

            // Set the printerFilter
            list.setPrinterFilter("JAVAPRINT");

            if (list.getPrinterFilter().trim().equals("JAVAPRINT")) succeeded();
            else failed("Could not set/retrive PrinterList printerFilter.");
            list.close(); 
            } 
        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting the PrinterList printerFilter
     * to an invalid value, length of name greater than 10.
     **/
    public void Var002()
    {
        try
            {
            // create an Printer List object using default constructor
            PrinterList list = new PrinterList();

            // Set the printerFilter.
            // Greater than 10 characters is invalid.
            list.setPrinterFilter("01234567890");
            list.close(); 
            failed("Could set an invalid printerFilter");
            }

        catch (ExtendedIllegalArgumentException e)
            {
            succeeded();
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting the PrinterList printerFilter,
     * then removing the filter using an empty string.
     **/
    public void Var003()
    {
        try
            {
            // create an Printer List object using default constructor
            PrinterList list = new PrinterList();

            // Set the printerFilter
            list.setPrinterFilter("JAVAPRINT");

            // Now remove the filter
            list.setPrinterFilter("");

            if (list.getPrinterFilter().trim().equals("")) succeeded();
            else failed("Could not remove PrinterList printerFilter.");
            list.close(); 
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests setting the Printer List's printerFilter to null.
     **/
    public void Var004()
    {
        try
            {
            // create an Printer List object using default constructor
            PrinterList list = new PrinterList();

            list.setPrinterFilter(null);
            list.close(); 
            failed("Could set the printerFilter to null");
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
     * Tests retrieving the Printer List's printerFilter
     * before it is set.
     **/
    public void Var005()
    {
        try
            {
            // create an Printer List object using default constructor
            PrinterList list = new PrinterList();

            if( list.getPrinterFilter().length() == 0 )
                {
              list.close(); 
                succeeded();
                }
            else
                {
                failed("printerFilter was not set, expecting empty string");
                }
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests listing a specific printer synchronously.
     **/
    public void Var006()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            prtDList.setPrinterFilter("JAVAPRINT");

            prtDList.openSynchronously();

            // retrieve the first printer
            Printer prtD = (Printer)prtDList.getObject(0);

            // check to see if we got one printer and verify name
            if ( (prtDList.size() == 1) &&
                 (prtD.getName().trim().equals("JAVAPRINT")) )
                {
                // indicate how many printer devices were listed
                output_.println(prtDList.size() + " Printer Device(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list by a specific printer device name. Output Queue was " + prtD.getName()
                       + " Only "+prtDList.size()+" printer(s) listed.");
                }

            prtDList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var006

    /**
     * Tests listing a generic printer synchronously.
     **/
    public void Var007()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            prtDList.setPrinterFilter("J*");

            prtDList.openSynchronously();

            Enumeration<Printer> e = prtDList.getObjects();
            String prtDName = null;

            // check to see if we got some printers
            if (prtDList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    Printer prtD = (Printer)e.nextElement();
                    prtDName = prtD.getName();
                    if (prtDName.startsWith("J"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad printer:"+prtDName);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    // indicate how many printers were listed
                    output_.println(prtDList.size() + " printer(s) listed.");

                    succeeded();
                    }
                else
                    {
                    failed("Could not list printer(s) by generic name");
                    }
                } // if size
            else 
                {
                failed("No printers found.");
                }

            prtDList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var007

    /**
     * Lists all printers synchronously.
     **/
    public void Var008()
    {
	try
	{
	    // create a printer list object
	    PrinterList prtDList = new PrinterList(systemObject_);

	    prtDList.setPrinterFilter("*ALL");

	    prtDList.openSynchronously();

            // check to see if we got some printers
            // since there is at least 1 printer on the system we should have gotten
            // at least 1 back in the list
            if (prtDList.size() >= 1)
                {
                // indicate how many resources were listed
                output_.println(prtDList.size() + " Printer(s) listed.");

                succeeded();
                }
            else
                {
                failed("Could not list printers by *ALL printer name. Only "
                       +prtDList.size() + " queues listed.");
                }

	    prtDList.close();
	    System.gc();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var008

    /**
     * Tests listing a printer with an invalid printerFilter.
     **/
    public void Var009()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            prtDList.setPrinterFilter("ThisIsAnInvalidFilter");
            prtDList.close();
            failed("Could set invalid printerFilter.");

            } // end try block

        catch (Exception e)
            {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))  succeeded();  
            else failed (e, "Unexpected exception");
            }

    } // end Var009

    /**
     * Tests that a property change event is fired when the
     * PrinterList's printerFilter is set.
     **/
    public void Var010()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                listenerInvoked = true; 
                boolean succeeded = true;
                String reason = "";

                if( !(evt.getPropertyName().equals("printerFilter")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(((String)evt.getOldValue()).equals("")) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(((String)evt.getNewValue()).equals("JAVAPRINT")) )
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
            // create an Printer Listobject using the default constructor
            PrinterList list = new PrinterList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // set the printerFilter
            list.setPrinterFilter("JAVAPRINT");

            if( !listenerInvoked )
                {
                failed("PropertyChange Listener was not invoked");
                }

            list.removePropertyChangeListener(propertyListener);
            list.close(); 

            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var010

    /**
     * Tests that a vetoable change event is fired when the
     * PrinterList's printerFilter is set.
     **/
    public void Var011()
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

                if( !(evt.getPropertyName().equals("printerFilter")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(((String)evt.getOldValue()).equals("")) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(((String)evt.getNewValue()).equals("JAVAPRINT")) )
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
            // create an Printer Listobject using the default constructor
            PrinterList list = new PrinterList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the printerFilter
            list.setPrinterFilter("JAVAPRINT");

            if( !listenerInvoked )
                {
                failed("VetoableChange Listener was not invoked");
                }

            list.removeVetoableChangeListener(vetoableListener);  
            list.close(); 

            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var011

    /**
     * Tests that a property change event is NOT fired when the
     * PrinterList's printerFilter property change is vetoed.
     **/
    public void Var012()
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
                if( evt.getPropertyName().equals("printerFilter") )
                    {
                    throw new PropertyVetoException("I veto", evt);
                    }
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        try
            {
            // create an Printer Listobject using the default constructor
            PrinterList list = new PrinterList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the printerFilter, consume PropertyVetoException
            try
                {
                list.setPrinterFilter("JAVAPRINT");
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
            list.close(); 

            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
            }

    } // end Var012

    /**
     * Tests that a PropertyVetoException is thrown when the 
     * PrinterList's printerFilter property change is vetoed.
     **/
    public void Var013()
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

        PrinterList list = null;
        try
            {
            // create an Printer Listobject using the default constructor
            list = new PrinterList();

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the system, a PropertyVetoException should be thrown
            list.setPrinterFilter("JAVAPRINT");

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
            list.close(); 

            }

    } // end Var013

    /**
     * Tests that the property change and vetoable change listeners               
     * are actually removed.
     **/
    public void Var014()
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

        PrinterList list = null;
        try
            {
            // create an Printer Listobject using the default constructor
            list = new PrinterList();

            // reset our flag
            listenerInvoked = false;

            // add the property change and vetoable change listeners
            list.addPropertyChangeListener(propertyListener);
            list.addVetoableChangeListener(vetoableListener);

            // remove the property change and vetoable change listeners
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);

            // set the printerFilter 
            list.setPrinterFilter("JAVAPRINT");

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
            failed(e, "Unexpected exception" );
            }

    } // end Var014

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

} // end NPPrtDListPrinterFilterTestcase class


