///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFListOpenAsyncTestcase.java
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
 Testcase NPSplFListOpenAsyncTestcase.
 **/
public class NPSplFListOpenAsyncTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFListOpenAsyncTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFListOpenAsyncTestcase(AS400            systemObject,
                                               Vector           variationsToRun,
                                               int              runMode,
                                               FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPSplFListOpenAsyncTestcase", 4,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFListOpenAsyncTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create OPNATST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/OPNATST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/OPNATST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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

            // clear the output queue we created.
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/OPNATST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/OPNATST)") == false)
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
     * Lists spooled file(s) asynchronously using the waitForListToComplete method
     **/
    public void Var001()
    {
        try
            {
            // create a spooled file on the output queue
            SpooledFile splF = createSpooledFile();

	    // create a spooled file list object
	    SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queue filter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/OPNATST.OUTQ");

            // now try to build list asynchrously
            splFList.openAsynchronously();

            // wait for the list to complete
            splFList.waitForListToComplete();

            int     listed = 0, size;
            size = splFList.size();
            String splFName = null;
            boolean fSuccess = false;

            while (listed < size)
                {
                SpooledFile splFile = (SpooledFile)splFList.getObject(listed++);
                splFName = splFile.getName();

                if (splFName.equals("JAVAPRINT"))
                    {
                    fSuccess = true;
                    }
                else
                    {
                    output_.println("Bad spooled file:"+splFName);
                    fSuccess = false;
                    break;
                    }
                } // end while 

            // indicate how many file(s) were listed
            output_.println(listed + " spooled file(s) listed.");

            // check to see if we were successful
            if (fSuccess == true)
                {
                succeeded();
                }
            else
                {
                failed("Incorrect spooled file listed: " + splFName);
                }

            splFList.close();

            // delete the spooled file we created
            splF.delete();

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
     * Lists spooled file(s) asynchronously using the waitForItem method
     **/
    public void Var002()
    {
        try
            {
            // create a spooled file on the output queue
            SpooledFile splF = createSpooledFile();

	    // create a spooled file list object
	    SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queue filter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/OPNATST.OUTQ");

            // now try to build list asynchrously
            splFList.openAsynchronously();

            // wait for the 1st item on the list to complete
            splFList.waitForItem(1);

            int listed = 0, size;
            size = splFList.size();
            String splFName = null;

            // check to see if we got at least one spooled file
            if (size >= 1)
                {
                boolean fSuccess = false;

                // check the spooled file we go back
                while (listed < size)
                    {
                    SpooledFile splFile = (SpooledFile)splFList.getObject(listed++);
                    splFName = splFile.getName();
                    if (splFName.equals("JAVAPRINT"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many file(s) were listed
                output_.println(listed + " spooled file(s) listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect spooled file listed: " + splFName);
                    }
                } // end if size
            else 
                {
                failed("No spooled files found.");
                }

            // close the list
            splFList.close();

            // delete the spooled file we created
            splF.delete();

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
     * Lists spooled file(s) asynchronously using the waitForItem method.
     * Wait for an item number greater than what is available.
     **/
    public void Var003()
    {
        try
            {
            // create a spooled file on the output queue
            SpooledFile splF = createSpooledFile();

	    // create a spooled file list object
	    SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queue filter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/OPNATST.OUTQ");

            // now try to build list asynchrously
            splFList.openAsynchronously();

            // wait for the 100th item on the list to complete,
            // there will not be 100 items on the list.
            splFList.waitForItem(100);

            int listed = 0, size;
            size = splFList.size();
            String splFName = null;

            // check to see if we got at least one spooled file
            if (size >= 1)
                {
                boolean fSuccess = false;

                // check the spooled file we go back
                while (listed < size)
                    {
                    SpooledFile splFile = (SpooledFile)splFList.getObject(listed++);
                    splFName = splFile.getName();
                    if (splFName.equals("JAVAPRINT"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many file(s) were listed
                output_.println(listed + " spooled file(s) listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Incorrect spooled file listed: " + splFName);
                    }
                } // end if size
            else 
                {
                failed("No spooled files found.");
                }

            // close the list
            splFList.close();

            // delete the spooled file we created
            splF.delete();

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
     * Lists Spooled File(s) asynchronously using the waitForItem method with invalid number.
     * Expects ExtendedIllegalArgumentException
     **/
    public void Var004()
    {
        try
            {
            // create a spooled list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queue filter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/OPNATST.OUTQ");

            // now try to build list asynchrously
            splFList.openAsynchronously();

            try
                {
                // use an invalid number to wait for
                splFList.waitForItem(0);

                failed("Could use an invalid number to wait for.");
                }
            catch (ExtendedIllegalArgumentException e)
                {
                succeeded();
                }

            // close the list
            splFList.close();
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
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/OPNATST.OUTQ");

        // clear the output queue
        outQ.clear(null);

        // create a print parameter list for the spooled file name
        PrintParameterList pList = new PrintParameterList();

        // set the spooled file name
        pList.setParameter(PrintObject.ATTR_SPOOLFILE,"JAVAPRINT");

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, pList, null, outQ);

        // write some data
        outStream.write(buf);

        // close the spooled file
        outStream.close();

        // update the output queue attributes
        outQ.update();

        // return the new SpooledFile
        return outStream.getSpooledFile();
    } // end createSpooledFile

} // end NPSplFListOpenAsyncTestcase class


