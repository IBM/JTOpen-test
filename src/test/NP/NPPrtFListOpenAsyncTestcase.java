///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtFListOpenAsyncTestcase.java
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
import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase NPPrtFListOpenAsyncTestcase.
 **/
public class NPPrtFListOpenAsyncTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtFListOpenAsyncTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtFListOpenAsyncTestcase(AS400            systemObject,
                                               Vector           variationsToRun,
                                               int              runMode,
                                               FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPPrtFListOpenAsyncTestcase", 4,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtFListOpenAsyncTestcase");
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
     * Lists printer file(s) asynchronously using the waitForListToComplete method
     **/
    public void Var001()
    {
        try
            {
            // create a printer list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            prtFList.openAsynchronously();

            // wait for the list to complete
            prtFList.waitForListToComplete();

            int     listed = 0, size;
            size = prtFList.size();
            String prtFName = null;
            boolean fSuccess = false;

            while (listed < size)
                {
                PrinterFile prtF = (PrinterFile)prtFList.getObject(listed++);
                prtFName = prtF.getPath();

                if (prtFName.equals("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE"))
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

            // indicate how many printer file(s) were listed
            output_.println(listed + " printer file(s) listed.");

            // check to see if we were successful
            if (fSuccess == true)
                {
                succeeded();
                }
            else
                {
                failed("Incorrect printer file listed: " + prtFName);
                }

            prtFList.close();
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
     * Lists printer file(s) asynchronously using the waitForItem method
     **/
    public void Var002()
    {
        try
            {
            // create a printer list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            prtFList.openAsynchronously();

            // wait for the 1st item on the list to complete
            prtFList.waitForItem(1);

            int listed = 0, size;
            size = prtFList.size();
            String prtFName = null;

            // check to see if we got at least one printer file
            if (size >= 1)
                {
                boolean fSuccess = false;

                // check the printer file we go back
                while (listed < size)
                    {
                    PrinterFile prtF = (PrinterFile)prtFList.getObject(listed++);
                    prtFName = prtF.getPath();
                    if (prtFName.equals("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many printer file(s) were listed
                output_.println(listed + " printer file(s) listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect printer file listed: " + prtFName);
                    }
                } // end if size
            else 
                {
                failed("No printer files found.");
                }

            // close the list
            prtFList.close();
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

    } // end Var002

    /**
     * Lists printer file(s) asynchronously using the waitForItem method.
     * Wait for an item number greater than what is available.
     **/
    public void Var003()
    {
        try
            {
            // create a printer list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            prtFList.openAsynchronously();

            // wait for the 100th item on the list to complete,
            // there should not be 100 items on the list.
            prtFList.waitForItem(100);

            int listed = 0, size;
            size = prtFList.size();
            String prtFName = null;

            // check to see if we got at least one printer file
            if (size >= 1)
                {
                boolean fSuccess = false;

                // check the printer file we go back
                while (listed < size)
                    {
                    PrinterFile prtF = (PrinterFile)prtFList.getObject(listed++);
                    prtFName = prtF.getPath();
                    if (prtFName.equals("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many printer file(s) were listed
                output_.println(listed + " printer file(s) listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect printer file listed: " + prtFName);
                    }
                } // end if size
            else 
                {
                failed("No printer files found.");
                }

            // close the list
            prtFList.close();
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

    } // end Var003

    /**
     * Lists Printer File(s) asynchronously using the waitForItem method with invalid number.
     * Expects ExtendedIllegalArgumentException
     **/
    public void Var004()
    {
        try
            {
            // create a printer list object
            PrinterFileList prtFList = new PrinterFileList(systemObject_);

            prtFList.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            prtFList.openAsynchronously();

            try
                {
                // use an invalid number to wait for
                prtFList.waitForItem(0);

                failed("Could use an invalid number to wait for.");
                }
            catch (ExtendedIllegalArgumentException e)
                {
                succeeded();
                }

            // close the list
            prtFList.close();
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

} // end NPPrtFListOpenAsyncTestcase class


