///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtDListOpenAsyncTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.Printer;
import com.ibm.as400.access.PrinterList;

import test.Testcase;

/**
 Testcase NPPrtDListOpenAsyncTestcase.
 **/
public class NPPrtDListOpenAsyncTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtDListOpenAsyncTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtDListOpenAsyncTestcase(AS400            systemObject,
                                       Vector<String> variationsToRun,
                                       int              runMode,
                                       FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPPrtDListOpenAsyncTestcase", 4,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtDListOpenAsyncTestcase");
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
     * Lists printer device(s) asynchronously using the waitForListToComplete method
     **/
    public void Var001()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            prtDList.setPrinterFilter("JAVAPRINT");

            prtDList.openAsynchronously();

            // wait for the list to complete
            prtDList.waitForListToComplete();

            int     listed = 0, size;
            size = prtDList.size();
            String prtDName = null;
            boolean fSuccess = false;

            while (listed < size)
                {
                Printer prtD = (Printer)prtDList.getObject(listed++);
                prtDName = prtD.getName();

                if (prtDName.equals("JAVAPRINT"))
                    {
                    fSuccess = true;
                    }
                else
                    {
                    output_.println("Bad printer device:"+prtDName);
                    fSuccess = false;
                    break;
                    }
                } // end while 

            // indicate how many printer devices were listed
            output_.println(listed + " printer device(s) listed.");

            // check to see if we were successful
            if (fSuccess == true)
                {
                succeeded();
                }
            else
                {
                failed("Incorrect printer device listed: " + prtDName);
                }

            prtDList.close();
            System.gc();

            } // end try block

        catch (ExtendedIllegalStateException e)                          // @A1A
            {
            if( (systemObject_.isThreadUsed() == false) &&
                (e.getReturnCode() == 
                   ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
                {
                // for no threads, openAsynchronous() can not be used.
                succeeded();
                }
            else
                {
                failed(e, "Unexpected exception");
                }
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Lists printer devices(s) asynchronously using the waitForItem method
     **/
    public void Var002()
    {
	try
	{
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            prtDList.setPrinterFilter("JAVAPRINT");

            prtDList.openAsynchronously();

            // wait for the 1st item on the list to complete
            prtDList.waitForItem(1);

            int listed = 0, size;
            size = prtDList.size();
            String prtDName = null;

            // check to see if we got at least one printer device
            if (size >= 1)
                {
                boolean fSuccess = false;

                // check the printer device we go back
                while (listed < size)
                    {
                    Printer prtD = (Printer)prtDList.getObject(listed++);
                    prtDName = prtD.getName();
                    if (prtDName.equals("JAVAPRINT"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many printer devices were listed
                output_.println(listed + " printer device(s) listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect printer device listed: " + prtDName);
                    }
                } // end if size
            else 
                {
                failed("No printer devices found.");
                }

            // close the list
            prtDList.close();
	    System.gc();

	} // end try block

   catch (ExtendedIllegalStateException e)                               // @A1A
   {
       if( (systemObject_.isThreadUsed() == false) &&
           (e.getReturnCode() == 
              ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
       {
           // for no threads, openAsynchronous() can not be used.
           succeeded();
       }
       else
       {
           failed(e, "Unexpected exception");
       }
   }

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var002

    /**
     * Lists printer devices(s) asynchronously using the waitForItem method.
     * Wait for an item number greater than what is available.
     **/
    public void Var003()
    {
	try
	{
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            prtDList.setPrinterFilter("JAVAPRINT");

            prtDList.openAsynchronously();

            // wait for the 100th item on the list to complete,
            // there should not be 100 items on the list.
            prtDList.waitForItem(100);

            int listed = 0, size;
            size = prtDList.size();
            String prtDName = null;

            // check to see if we got at least one printer device
            if (size >= 1)
                {
                boolean fSuccess = false;

                // check the printer device we go back
                while (listed < size)
                    {
                    Printer prtD = (Printer)prtDList.getObject(listed++);
                    prtDName = prtD.getName();
                    if (prtDName.equals("JAVAPRINT"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many printer devices were listed
                output_.println(listed + " printer device(s) listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect printer device listed: " + prtDName);
                    }
                } // end if size
            else 
                {
                failed("No printer devices found.");
                }

            // close the list
            prtDList.close();
	    System.gc();

	} // end try block

   catch (ExtendedIllegalStateException e)                               // @A1A
   {
       if( (systemObject_.isThreadUsed() == false) &&
           (e.getReturnCode() == 
              ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
       {
           // for no threads, openAsynchronous() can not be used.
           succeeded();
       }
       else
       {
           failed(e, "Unexpected exception");
       }
   }

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var003

    /**
     * Lists Printer Device(s) asynchronously using the waitForItem method with invalid number.
     * Expects ExtendedIllegalArgumentException
     **/
    public void Var004()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList = new PrinterList(systemObject_);

            prtDList.setPrinterFilter("JAVAPRINT");

            prtDList.openAsynchronously();

            try
                {
                // use an invalid number to wait for
                prtDList.waitForItem(0);

                failed("Could use an invalid number to wait for.");
                }
            catch (ExtendedIllegalArgumentException e)
                {
                succeeded();
                }

            // close the list
            prtDList.close();
            System.gc();

            } // end try block

        catch (ExtendedIllegalStateException e)                          // @A1A
            {
            if( (systemObject_.isThreadUsed() == false) &&
                (e.getReturnCode() == 
                   ExtendedIllegalStateException.OBJECT_CAN_NOT_START_THREADS) )
                {
                // for no threads, openAsynchronous() can not be used.
                succeeded();
                }
            else
                {
                failed(e, "Unexpected exception");
                }
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

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

} // end NPPrtDListOpenAsyncTestcase class


