///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFListAttrsToRetTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.OutputQueue;
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.RequestNotSupportedException;
import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.access.SpooledFileList;
import com.ibm.as400.access.SpooledFileOutputStream;

import test.Testcase;

/**
 Testcase NPSplFListAttrsToRetTestcase.
 **/
public class NPSplFListAttrsToRetTestcase extends Testcase

{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFListAttrsToRetTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFListAttrsToRetTestcase(AS400            systemObject,
                                        Vector<String> variationsToRun,
                                        int              runMode,
                                        FileOutputStream fileOutputStream)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPSplFListAttrsToRetTestcase", 5,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFListAttrsToRetTestcase");
        boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create ATTRTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/ATTRTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/ATTRTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
                {
                failed("Could not grant *public authority to output queue. "
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

            // clear the output queue we created.
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/ATTRTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/ATTRTST)") == false)
                {
                output_.println("Could not delete output queue we created. "
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
     * Tests setting valid attributes to retrieve on a Spooled File List.
     **/
    public void Var001()
    {
        try
            {
            // create a spooled file on the output queue
            SpooledFile splF = createSpooledFile();

            // create a Spooled File List object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queue filter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/ATTRTST.OUTQ");

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_JOBUSER;
            attrs[1] = PrintObject.ATTR_JOBNUMBER;
            attrs[2] = PrintObject.ATTR_JOBNAME;

            // set the attributes to retrieve
            splFList.setAttributesToRetrieve(attrs);

            // list the spooled files
            splFList.openSynchronously();

            Enumeration<SpooledFile> e = splFList.getObjects();

            // check to see if we got our spooled file
            if (splFList.size() == 1)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    SpooledFile splFile = (SpooledFile)e.nextElement();
                    if ( (splFile.getStringAttribute(PrintObject.ATTR_JOBUSER) != null) &&
                         (splFile.getStringAttribute(PrintObject.ATTR_JOBNUMBER) != null) &&
                         (splFile.getStringAttribute(PrintObject.ATTR_JOBNAME) != null) )
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        output_.println("Bad attributes on "+splFile.getName());
                        break;
                        }
                    } // end while

                // indicate how many spooled files were listed
                output_.println(splFList.size() + " Spooled File(s) listed.");

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
            else 
                {
                failed("Incorrect number of spooled file(s) found in list.");
                }

            // close the list
            splFList.close();

            // delete the spooled file we created.
            splF.delete();

            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting null attributes to retrieve on a Spooled File List.
     * Expects NullPointerException
     **/
    public void Var002()
    {
        try
            {
            // create a Spooled File List object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                splFList.openAsynchronously();
            }
            else {
                splFList.openSynchronously();
            }

            // set the attributes to retrieve
            splFList.setAttributesToRetrieve(null);

            failed("Could set null attributes to retrieve.");

            splFList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back a null pointer exception
            if (exceptionIs(e, "NullPointerException"))  succeeded();
            else failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting the attributes to retrieve on an open Spooled File List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var003()
    {
        try
            {
            // create a Spooled File List object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                splFList.openAsynchronously();
            }
            else {
                splFList.openSynchronously();
            }

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_JOBUSER;
            attrs[1] = PrintObject.ATTR_JOBNUMBER;
            attrs[2] = PrintObject.ATTR_JOBNAME;

            // set the attributes to retrieve
            splFList.setAttributesToRetrieve(attrs);

            failed("Could set attributes to retrieve on an opened list.");

            splFList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back an extened illegal state exception
            if (exceptionIs(e, "ExtendedIllegalStateException"))  succeeded();
            else failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests resetting attributes to retrieve on a Spooled File List.
     **/
    public void Var004()
    {
        try
            {
            // create a spooled file on the output queue
            SpooledFile splF = createSpooledFile();

            // create a Spooled File List object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queue filter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/ATTRTST.OUTQ");

            // make an int array of the attributes we want to retrieve
            int[] attrs = new int[3];

            attrs[0] = PrintObject.ATTR_JOBUSER;
            attrs[1] = PrintObject.ATTR_JOBNUMBER;
            attrs[2] = PrintObject.ATTR_JOBNAME;

            // set the attributes to retrieve
            splFList.setAttributesToRetrieve(attrs);

            // list the spooled files
            splFList.openSynchronously();

            // close the list
            splFList.close();

            // reset the attributes 
            splFList.resetAttributesToRetrieve();

            // set the output queue filter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/ATTRTST.OUTQ");
           
            // list the spooled files
            splFList.openSynchronously();

            Enumeration<SpooledFile> e = splFList.getObjects();

            // check to see if we got some spooled files
            if (splFList.size() == 1)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    SpooledFile splFile = (SpooledFile)e.nextElement();

                    if ( (splFile.getStringAttribute(PrintObject.ATTR_OUTPTY) != null) &&
                         (splFile.getStringAttribute(PrintObject.ATTR_FONTID) != null) )
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        output_.println("Bad attributes on "+splFile.getName());
                        break;
                        }
                    } // end while

                // indicate how many spooled files were listed
                output_.println(splFList.size() + " Spooled File(s) listed.");

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
            else 
                {
                failed("Incorrect number of spooled files listed.");
                }

            // close the list
            splFList.close();

            // delete the spooled file we created
            splF.delete();

            System.gc();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests resetting the attributes to retrieve on an open Spooled File List.
     * Expects ExtendedIllegalStateException
     **/
    public void Var005()
    {
        try
            {
            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // open the list
            if (systemObject_.isThreadUsed()) {                          // @A1A
                splFList.openAsynchronously();
            }
            else {
                splFList.openSynchronously();
            }

            // set the attributes to retrieve
            splFList.resetAttributesToRetrieve();

            failed("Could reset attributes to retrieve on an opened list.");

            splFList.close();

            } // end try block

        catch (Exception e)
            {
            // verify we got back an extended illegal state exception
            if (exceptionIs(e, "ExtendedIllegalStateException"))  succeeded();
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

    /////////////////////
    // Private methods //
    /////////////////////

    // This method creates a spooled file
    private SpooledFile createSpooledFile()
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException,
             RequestNotSupportedException
    {
        // buffer for data
        byte[] buf = new byte[2048];

        // fill the buffer
        for (int i=0; i<2048; i++) buf[i] = (byte)'9';

        // create a reference to the output queue
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/ATTRTST.OUTQ");

        // clear the output queue
        outQ.clear(null);

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, outQ);

        // write some data
        outStream.write(buf);

        // close the spooled file
        outStream.close();

        // update the output queue attributes
        outQ.update();

        // return the new SpooledFile
        return outStream.getSpooledFile();
    } // end createSpooledFile

} // end NPSplFListAttrsToRetTestcase class


