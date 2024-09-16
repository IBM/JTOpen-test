///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPAFPRListAttrsToRetTestcase.java
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
 Testcase NPAFPRListAttrsToRetTestcase.
 **/
public class NPAFPRListAttrsToRetTestcase extends Testcase

{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPAFPRListAttrsToRetTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPAFPRListAttrsToRetTestcase(AS400            systemObject,
                                        Vector           variationsToRun,
                                        int              runMode,
                                        FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPAFPRListAttrsToRetTestcase", 5,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPAFPRListAttrsToRetTestcase");
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
     * Tests setting valid attributes to retrieve on an AFP Resource List.
     **/
    public void Var001()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // set the resource filter to receive all *FNTRSC resources in NPJAVA
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.FNTRSC");

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_AFP_RESOURCE;
            attrs[1] = PrintObject.ATTR_DATE;
            attrs[2] = PrintObject.ATTR_TIME;

            // set the attributes to retrieve
            resList.setAttributesToRetrieve(attrs);

            // list the resources
            resList.openSynchronously();

            Enumeration e = resList.getObjects();

            // check to see if we got some resources
            if (resList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();
                    if ( (res.getStringAttribute(PrintObject.ATTR_DATE) != null) &&
                         (res.getStringAttribute(PrintObject.ATTR_TIME) != null) &&
                         (res.getPath() != null) )
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
                    failed("Failed to retrieve appropriate attributes");
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
     * Tests setting null attributes to retrieve on an AFP Resource List.
     * Expects NullPointerException
     **/
    public void Var002()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                resList.openAsynchronously();
            }
            else {
                resList.openSynchronously();
            }

            // set the attributes to retrieve
            resList.setAttributesToRetrieve(null);

            failed("Could set null attributes to retrieve.");

            resList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back a null pointer exception
            if (exceptionIs(e, "NullPointerException"))  succeeded();
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            else if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting the attributes to retrieve on an open AFP Resource List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var003()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                resList.openAsynchronously();
            }
            else {
                resList.openSynchronously();
            }

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_AFP_RESOURCE;
            attrs[1] = PrintObject.ATTR_DATE;
            attrs[2] = PrintObject.ATTR_TIME;

            // set the attributes to retrieve
            resList.setAttributesToRetrieve(attrs);

            failed("Could set attributes to retrieve on an opened list.");

            resList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back an extened illegal state exception
            if (exceptionIs(e, "ExtendedIllegalStateException"))  succeeded();
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            else if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests resetting attributes to retrieve on an AFP Resource List.
     **/
    public void Var004()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // set the resource filter to receive all *FNTRSC resources in NPJAVA
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.FNTRSC");

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_AFP_RESOURCE;
            attrs[1] = PrintObject.ATTR_DATE;
            attrs[2] = PrintObject.ATTR_TIME;

            // set the attributes to retrieve
            resList.setAttributesToRetrieve(attrs);

            // list the resources
            resList.openSynchronously();

            // close the list
            resList.close();

            // reset the attributes 
            resList.resetAttributesToRetrieve();

            // set the resource filter to receive all *FNTRSC resources in NPJAVA
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.FNTRSC");
            
            // list the resources
            resList.openSynchronously();

            Enumeration e = resList.getObjects();

            // check to see if we got some resources
            if (resList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();

                    // this time the other attributes on AFR resource should not be null
                    if ( (res.getStringAttribute(PrintObject.ATTR_OBJEXTATTR) != null) &&
                         (res.getStringAttribute(PrintObject.ATTR_DESCRIPTION) != null) &&
                         (res.getPath() != null) )
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad resource:"+res.getPath());
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
                    failed("Failed to reset attributes");
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

    } // end Var004

    /**
     * Tests resetting the attributes to retrieve on an open AFP Resource List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var005()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                resList.openAsynchronously();
            }
            else {
                resList.openSynchronously();
            }

            // set the attributes to retrieve
            resList.resetAttributesToRetrieve();

            failed("Could reset attributes to retrieve on an opened list.");

            resList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back an extended illegal state exception
            if (exceptionIs(e, "ExtendedIllegalStateException"))  succeeded();
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            else if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var005

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

} // end NPAFPRListAttrsToRetTestcase class


