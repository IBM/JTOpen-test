///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtObjectInStrResetTestcase.java
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
 Testcase NPPrtObjectInStrResetTestcase.
 **/
public class NPPrtObjectInStrResetTestcase extends Testcase
{
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtObjectInStrResetTestcase(AS400            systemObject,
				  Vector           variationsToRun,
				  int              runMode,
				  FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPPrtObjectInStrResetTestcase", 4,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtObjectInStrResetTestcase");
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
     Tests reset() method on a 4343 byte spooled file
     This test will only work on V3R2, V3R7, V4R1+ systems (not on V3R6) and will
     only work with a fix for the Network print server on these releases.  We get
     RequestNotSupported on older systems or systems without the fix.
     **/
    public void Var001()
    {
        try
        {
                // create 4343 byte spooled file
                SpooledFile splF = createSpooledFile(4343);

                if (splF == null)
                {
                    failed("Cannot create spooled file");
                } else {
                    try
                    {
                        // get an input stream for it
                        PrintObjectInputStream is = splF.getInputStream();
                        // get the number of bytes available now
                        int avail = is.available();
                        if ( avail != 4343 )
                        {
                            failed(" Did not get correct available amount of 4343; got " + avail);
                        } else {
                            // mark the stream here
                            is.mark(64);
                            // read 64 bytes
                            byte[] buf = new byte[64];
                            is.read(buf);
                            // reset back to beginning
                            is.reset();
                            // check available is back to what it was
                            int availNow = is.available();
                            if (availNow != avail)
                            {
                                failed(" Did not get back same value for available!");
                            } else {
                                succeeded();
                            }

                        }
                        is.close();

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
     Tests reset() method to generate IOException by calling it after close() method on
     any AFP resource.
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
                        byte[] buf = new byte[20];

                        // get an input stream for it
                        PrintObjectInputStream is = resource.getInputStream();
                        // get the size of this resource
                        int avail = is.available();
                        int read;
                        // mark it here
                        is.mark(avail);
                        // read the first 20 bytes
                        read = is.read(buf);
                        // close the stream
                        is.close();
                        try
                        {
                           // reset the read pointer - should give us an IO Exception here
                           is.reset();
                           failed(" No IOException generated on reset() after close()");
                        }
                        catch (IOException e)
                        {
                            succeeded(); // " Got this expected IOException: " + e);
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

   /**
     Tests reset() method to generate IOException by calling it after mark has been invalidated.
     Uses a Spooled file for this.
     This test will only work on V3R2, V3R7, V4R1+ systems (not  V3R6) and will
     only work with a fix for the Network print server on these releases.  We get
     RequestNotSupported on older systems or systems without the fix.
     **/
    public void Var003()
    {
        try
        {
            // create 2520 byte spooled file
            SpooledFile splF = createSpooledFile(2520);

            if (splF == null)
            {
                failed("Cannot create spooled file");
            } else {
                try
                {
                    // get an input stream for it
                    PrintObjectInputStream is = splF.getInputStream();
                    // get the number of bytes available now
                    int avail = is.available();
                    if ( avail != 2520 )
                    {
                        failed(" Did not get correct available amount of 2520; got " + avail);
                    } else {
                        // mark the stream here
                        is.mark(40);
                        // read 64 bytes
                        byte[] buf = new byte[64];
                        is.read(buf);
                        try
                        {
                           // reset back to beginning - should give an IOException
                           is.reset();
                           failed(" No IOException on reset() when mark was invalidated!");
                        }
                        catch (IOException e)
                        {
                            succeeded(); // " Got this expected IOException: " + e);
                        }
                        finally
                        {
                            is.close();
                        }
                    }

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

    } // end Var003

    /**
     Tests reset() method to generate IOException by calling it before a mark has been set.
     Uses an AFP resource.
     This test will only work on V3R7, V4R1+ systems (not on V3R2 or V3R6) and will
     only work with a fix for the Network print server on these releases.  We get
     RequestNotSupported on older systems or systems without the fix.
     This test requires atleast one AFP resource be in the system library on the
     target system.
     **/
    public void Var004()
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
                        byte[] buf = new byte[20];

                        // get an input stream for it
                        PrintObjectInputStream is = resource.getInputStream();
                        // get the size of this resource
                        int avail = is.available();
                        int read;
                        // read the first 20 bytes
                        read = is.read(buf);

                        try
                        {
                           // reset the read pointer - should give us an IO Exception here
                           is.reset();
                           failed(" No IOException generated on reset() after close()");
                        }
                        catch (IOException e)
                        {
                            succeeded(); // " Got this expected IOException: " + e);
                        }
                        finally
                        {
                            // close the stream
                            is.close();
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
       
        // create a print parameter list for the spooled file
        PrintParameterList pList = new PrintParameterList();    // @A1A

        // set the printer device type
        pList.setParameter(PrintObject.ATTR_PRTDEVTYPE,"*USERASCII"); // @A1A

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, pList, null, null);

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


} // end NPPrtObjectInStrResetTestcase class


