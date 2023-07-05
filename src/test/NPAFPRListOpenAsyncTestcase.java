///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPAFPRListOpenAsyncTestcase.java
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
import com.ibm.as400.access.*;

/**
 Testcase NPAFPRListOpenAsyncTestcase.  Tests the asynchronous listing of AFP Resources.
 **/
public class NPAFPRListOpenAsyncTestcase extends Testcase

{
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPAFPRListOpenAsyncTestcase(AS400            systemObject,
                                   Vector           variationsToRun,
                                   int              runMode,
                                   FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPAFPRListOpenAsyncTestcase", 4,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPAFPRListOpenAsyncTestcase");
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

            // $$$ TO DO $$$
            // Add an 'if' block using the following as a template for each variation.
            // Make sure you correctly set the variation number
            // and runmode in the 'if' condition, and the
            // variation number in the setVariation call.
            //
            // replace the X with the variation number you are adding
            //
/* $$$ TO DO $$$ - delete this line
            // run variation X
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
     * Lists AFP Resources(s) asynchronously using the waitForListToComplete method
     **/
    public void Var001()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter for the NPJAVA library
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.%ALL%");

            // now try to build resource list asynchrously
            resList.openAsynchronously();

            // wait for the list to complete
            resList.waitForListToComplete();

            int listed = 0, size;
            size = resList.size();
            String resPath = null;

            // check to see if we got some resources
            if (size > 0)
                {
                boolean fSuccess = false;

                // check the resources we go back
                while (listed < size)
                    {
                    AFPResource res = (AFPResource)resList.getObject(listed++);
                    resPath = res.getPath();
                    if (res.getPath().startsWith("/QSYS.LIB/NPJAVA.LIB"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(listed + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect resource listed: " + resPath);
                    }
                } // end if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("No AFP Resources found in NPJAVA Library.");
                }

            // close the list
            resList.close();
            System.gc();

            } // end try block

        catch (ExtendedIllegalStateException e)                          // @A1A
            {
            if ( (systemObject_.isThreadUsed() == false) &&
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
     * Lists AFP Resources(s) asynchronously using the waitForItem method
     **/
    public void Var002()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter the list for the NPJAVA library
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.%ALL%");

            // now try to build resource list asynchrously
            resList.openAsynchronously();

            // wait for the 2nd item on the list to complete
            resList.waitForItem(2);

            int listed = 0, size;
            size = resList.size();
            String resPath = null;

            // check to see if we got two resources
            if (size >= 2)
                {
                boolean fSuccess = false;

                // check the resources we go back
                while (listed < size)
                    {
                    AFPResource res = (AFPResource)resList.getObject(listed++);
                    resPath = res.getPath();
                    if (res.getPath().startsWith("/QSYS.LIB/NPJAVA.LIB"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(listed + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect resource listed: " + resPath);
                    }
                } // end if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("Only "+size+" AFP Resources found in NPJAVA Library.");
                }

            // close the list
            resList.close();
            System.gc();

            } // end try block

        catch (ExtendedIllegalStateException e)                          // @A1A
            {
            if ( (systemObject_.isThreadUsed() == false) &&
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
     * Lists AFP Resources(s) asynchronously using the waitForItem method.
     * Wait for an item number greater than what will be available.
     **/
    public void Var003()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter the list for the NPJAVA library
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.%ALL%");

            // now try to build resource list asynchrously
            resList.openAsynchronously();

            // wait for the 100th item on the list to complete,
            // the list will not have 100 items.
            resList.waitForItem(100);

            int listed = 0, size;
            size = resList.size();
            String resPath = null;

            // check to see if we got resources
            if (size >= 1)
                {
                boolean fSuccess = false;

                // check the resources we go back
                while (listed < size)
                    {
                    AFPResource res = (AFPResource)resList.getObject(listed++);
                    resPath = res.getPath();
                    if (res.getPath().startsWith("/QSYS.LIB/NPJAVA.LIB"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(listed + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect resource listed: " + resPath);
                    }
                } // end if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("Only "+size+" AFP Resources found in NPJAVA Library.");
                }

            // close the list
            resList.close();
            System.gc();

            } // end try block

        catch (ExtendedIllegalStateException e)                          // @A1A
            {
            if ( (systemObject_.isThreadUsed() == false) &&
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
     * Lists AFP Resources(s) asynchronously using the waitForItem method with invalid number.
     * Expects ExtendedIllegalArgumentException
     **/
    public void Var004()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter the list for the NPJAVA library
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.%ALL%");

            // now try to build resource list asynchrously
            resList.openAsynchronously();

            try
                {
                // use an invalid number to wait for
                resList.waitForItem(0);

                failed("Could use an invalid number to wait for.");
                }
            catch (ExtendedIllegalArgumentException e)
                {
                succeeded();
                }

            // close the list
            resList.close();
            System.gc();

            } // end try block

        catch (ExtendedIllegalStateException e)                          // @A1A
            {
            if ( (systemObject_.isThreadUsed() == false) &&
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

} // end NPAFPRListOpenAsyncTestcase class


