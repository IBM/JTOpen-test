///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtObjectInStrReadTestcase.java
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
 Testcase NPPrtObjectInStrReadTestcase.
 **/
public class NPPrtObjectInStrReadTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtObjectInStrReadTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtObjectInStrReadTestcase(AS400            systemObject,
				  Vector<String> variationsToRun,
				  int              runMode,
				  FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
          super(systemObject, "NPPrtObjectInStrReadTestcase", 5,
	            variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtObjectInStrReadTestcase");
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
     Tests read() method on a 1234 byte spooled file
     This test will only work on V3R2, V3R7, V4R1+ systems (not on V3R6) and will
     only work with a fix for the Network print server on these releases.  We get
     RequestNotSupported on older systems or systems without the fix.
     **/
    public void Var001()
    {
        try
        {
                // create 4343 byte spooled file
                SpooledFile splF = createSpooledFile(1234);

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
                        if ( avail != 1234 )
                        {
                            failed(" Did not get correct available amount of 1234; got " + avail);
                        } else {
                            // read 1 byte
                            int i = is.read();
                            if (i == (int)'R')
                            {
                                succeeded();
                            } else {
                                failed(" Did not read the correct byte");
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
     Tests read(byte[]) method on an AFP resource input stream
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
                        // get the size of this resource
                        int avail = is.available();
                        byte[] buf = new byte[avail+avail];
                        int read;

                        // read the entire resource
                        read = is.read(buf);

                        // close the stream
                        is.close();
                        if (read != avail)
                        {
                            failed(" Did not read " + avail + " bytes as expected. Read " + read + " bytes instead");
                        } else {
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

   /**
     Tests read(byte[], int, int ) method on a spooled file
     This test will only work on V3R2, V3R7, V4R1+ systems (not  V3R6) and will
     only work with a fix for the Network print server on these releases.  We get
     RequestNotSupported on older systems or systems without the fix.
     **/
    public void Var003()
    {
        try
        {
            // create 2323 byte spooled file
            SpooledFile splF = createSpooledFile(2323);

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
                    if ( avail != 2323 )
                    {
                        failed(" Did not get correct available amount of 2323; got " + avail);
                    } else {
                        // read 32 bytes into middle of buffer
                        byte[] buf = new byte[96];

                        int read = is.read(buf, 10, 32);
                        is.close();
                        boolean fSuccess = true;

                        if (read != 32)
                        {
                            failed(" Did not read 32 bytes as expected! Read " + read + " bytes instead!");
                            fSuccess = false;
                        }
                        for (int i=0; fSuccess && i<10; i++)
                        {
                            if (buf[i] != 0)
                            {
                                failed(" Buffer overwritten at byte " + i + " with character " + buf[i] );
                                fSuccess = false;
                                break;
                            }
                        }
                        for (int i=10+read; fSuccess && i<buf.length; i++)
                        {
                            if (buf[i] != 0)
                            {
                                failed(" Buffer overwritten at byte " + i + " with character " + buf[i] );
                                fSuccess = false;
                                break;
                            }
                        }

                        if (fSuccess)
                        {
                            succeeded();
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
     Tests all 3 read() methods return -1 at end of file
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
                         // get an input stream for it
                        PrintObjectInputStream is = resource.getInputStream();
                        // get the size of this resource
                        int avail = is.available();
                        byte[] buf = new byte[avail];
                        int read;
                        // read the entire resource
                        read = is.read(buf);
                        read = is.read();
                        if (read == -1)
                        {
                            read = is.read(buf);
                            if (read == -1)
                            {
                                read = is.read(buf, 1, 1);
                            }
                        }

                        if (read != -1)
                        {
                            failed(" Expected to read -1 but got back " + read + " instead");
                        }  else {
                            succeeded();
                        }
                        is.close();

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

   /**
     Tests all 3 read methods throw IOException after file has been closed.
     Uses a spooled file for this
     This test will only work on V3R2, V3R7, V4R1+ systems (not  V3R6) and will
     only work with a fix for the Network print server on these releases.  We get
     RequestNotSupported on older systems or systems without the fix.
     **/
    public void Var005()
    {
        try
        {
            // create 1111 byte spooled file
            SpooledFile splF = createSpooledFile(1111);

            if (splF == null)
            {
                failed("Cannot create spooled file");
            } else {
                try
                {
                    byte[] buf = new byte[20];
                    // get an input stream for it
                    PrintObjectInputStream is = splF.getInputStream();
                    // get the number of bytes available now
                    int avail = is.available();
                    if ( avail != 1111 )
                    {
                        failed(" Did not get correct available amount of 1111; got " + avail);
                    } else {
                        // close the input stream
                        is.close();
                        try
                        {

                            is.read();
                            failed(" IOException not thrown in read() after close()" );
                        }
                        catch (IOException e)
                        {
                            try
                            {
                               is.read(buf);
                               failed(" IOException not thrown in read(byte[]) after close()");
                            }
                            catch(IOException e2)
                            {
                                try
                                {
                                    is.read(buf,1,10);
                                    failed(" IOException not thown in read(byte[], int, int) after close()");
                                }
                                catch(IOException e3)
                                {
                                    succeeded();
                                }
                            }
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
        for (int i=0; i<size; i++) buf[i] = (byte)'R';
       
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


} // end NPPrtObjectInStrReadTestcase class


