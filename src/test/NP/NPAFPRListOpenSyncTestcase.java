///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPAFPRListOpenSyncTestcase.java
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
import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase NPAFPRListOpenSyncTestcase.  Test the synchronous listing of AFP Resources.
 **/
public class NPAFPRListOpenSyncTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPAFPRListOpenSyncTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPAFPRListOpenSyncTestcase(AS400            systemObject,
                                  Vector           variationsToRun,
                                  int              runMode,
                                  FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPAFPRListOpenSyncTestcase", 2,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPAFPRListOpenSyncTestcase");
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
     * Lists AFP Resource(s) synchronously.
    **/
    public void Var001()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter the list for the NPJAVA library
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.%ALL%");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration e = resList.getObjects();
            String resPath = null;

            // check to see if we got some resources
            if (resList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();
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
                output_.println(resList.size() + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect resource listed: " + resPath);
                    }
                } // if size
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
     * Try to list AFP Resource(s) with a AFPResourceList object
     * in an invalid state.
    **/
    public void Var002()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList();

            // filter the list for the NPJAVA library
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.%ALL%");

            // now try to build resource list synchronously
            resList.openSynchronously();
            failed("Should have gotten ExtendedIllegalStateException");
            }

        catch (ExtendedIllegalStateException e)
            {
            // We got the exception we were expecting.
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

    } // end Var002

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

} // end NPAFPRListOpenSyncTestcase class


