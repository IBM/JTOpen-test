///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtObjectInStrCloseTestcase.java
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
 Testcase NPPrtObjectInStrCloseTestcase.
 **/
public class NPPrtObjectInStrCloseTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtObjectInStrCloseTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtObjectInStrCloseTestcase(AS400            systemObject,
				  Vector<String> variationsToRun,
				  int              runMode,
				  FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPPrtObjectInStrCloseTestcase", 2,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtObjectInStrCloseTestcase");
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
     Tests close() method on a 12000 byte spooled file
     This test will only work on V3R2, V3R7, V4R1+ systems (not on V3R6) and will
     only work with a fix for the Network print server on these releases.  We get
     RequestNotSupported on older systems or systems without the fix.
     **/
    public void Var001()
    {
        try
        {

                // create 12000 byte spooled file
                SpooledFile splF = createSpooledFile(12000);

                if (splF == null)
                {
                    failed("Cannot create spooled file");
                } else {
                    try
                    {
                        // get an input stream for it
                        PrintObjectInputStream is = splF.getInputStream();
                        // close it
                        is.close();
                        succeeded();
                    }
                    catch (RequestNotSupportedException e)
                    {
                        failed(e, "Network Print Server doesn't support getInputStream() at this level");
                    }
                    finally
                    {
                        splF.delete();      // cleanup
                    }
                }


        } // end try block

        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }

    } // end Var001

    /**
     Tests close() method on any AFP resource 2 to generate the IOException
     This test will only work on V3R7, V4R1+ systems (not on V3R2 or V3R6) and will
     only work with a fix for the Network print server on these releases.  We get
     RequestNotSupported on older systems or systems without the fix.
     This test requires atleast one AFP resource be in the system library on the
     target system.
     **/
    public void Var002()
    {
        try
        {

                // get an AFP resource object
                AFPResource resource = getAFPResource();

                if (resource == null)
                {
                    failed(" No AFP Resources available on system ");
                } else {

                    try
                    {
                        // get an input stream for it
                        PrintObjectInputStream is = resource.getInputStream();
                        // close the input stream
                        is.close();
                        // try to close the input stream again, should get an IOException
                        try
                        {
                            is.close();
                            failed("No IOException when close() called twice");

                        }
                        catch (IOException e)
                        {
                            succeeded();
                        }
                    }
                    catch (RequestNotSupportedException e)
                    {
                        failed(e, "Network Print Server doesn't support getInputStream() at this level; Needs PTF; ");
                    }
                }


        } // end try block

        catch (RequestNotSupportedException e)
        {
            failed(e, "Network Print Server doesn't support AFP resources at this level; Must be V3R7+; ");
        }


        catch (Exception e)
        {
            failed(e, "Unexpected exception");
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

    // This method creates a spooled file
    private SpooledFile createSpooledFile(int size)
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException
    {
        // buffer for data
        byte[] buf = new byte[size];

        // fill the buffer
        for (int i=0; i<size; i++) buf[i] = (byte)'Z';

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, null);

        // check to see that we got a spooled file output stream reference
        if (outStream != null)
            {

            // write some data
            outStream.write(buf);

            // close the spooled file
            outStream.close();

            // return the new SpooledFile
            return outStream.getSpooledFile();
            }
        else
            {
            return (SpooledFile)null;
            }

    } // end createSpooledFile

    // This method gets an AFP resource object
    private AFPResource getAFPResource()
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException,
             RequestNotSupportedException
    {
        AFPResource resource = null;
        // Get the list of AFP resource from da system
        AFPResourceList resourceList = new AFPResourceList(systemObject_);
        resourceList.openSynchronously();
        int size = resourceList.size();
        if (size > 0)
        {
            resource = (AFPResource)(resourceList.getObject(0));
        }
        resourceList.close();
        return resource;
    }


} // end NPPrtObjectInStrCloseTestcase class


