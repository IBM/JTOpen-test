///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFOutStrCtorTestcase.java
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
 Testcase NPSplFOutStrCtorTestcase.
 **/
public class NPSplFOutStrCtorTestcase extends Testcase
{
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFOutStrCtorTestcase(AS400            systemObject,
                                    Vector           variationsToRun,
                                    int              runMode,
                                    FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPSplFOutStrCtorTestcase", 6,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFOutStrCtorTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
            {
            // create CTORTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/CTORTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/CTORTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
                {
                failed("Could not grant *public authority to output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            // make sure JAVAPRINT printer file exists
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

            // run variation 5
            if ((allVariations || variationsToRun_.contains("5")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(5);
                Var005();
                }

            // run variation 6
            if ((allVariations || variationsToRun_.contains("6")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(6);
                Var006();
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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/CTORTST)") != true)
                {
                failed("Could not clear the output queue we created."
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/CTORTST)") != true)
                {
                failed("Could not delete the output queue we created."
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            // delete the printer file that we created
            if (cmd.run("DLTF FILE(NPJAVA/JAVAPRINT)") != true)
                {
                failed("Could not delete the printer file that we created."
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            } // end try block

        catch( Exception e )
            {
            failed(e, "Unexpected exception");
            }

    } // end run method

    /**
     * Tests constructing a spooled file output stream with good system parameter.
     **/
    public void Var001()
    {
        try
            {
            // buffer for data
            byte[] buf = new byte[2048];

            // fill the buffer
            for (int i=0; i<2048; i++) buf[i] = (byte)'9';

            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, null);

            // write some data
            outStream.write(buf);

            succeeded();

            // close the spooled file
            outStream.close();

            // retrieve the spooled file
            SpooledFile splF = outStream.getSpooledFile();

            // delete the spooled file we created
            splF.delete();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests constructing a spooled file output stream with null system parameter.
     * Expect NullPointerException
     **/
    public void Var002()
    {
        try
            {
            // buffer for data
            byte[] buf = new byte[2048];

            // fill the buffer
            for (int i=0; i<2048; i++) buf[i] = (byte)'9';

            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(null, null, null, null);

            failed("Could use null system parameter.");
            }
        catch (Exception e)
            {
            if (exceptionIs(e, "NullPointerException"))  succeeded();
            else failed (e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests constructing a spooled file output stream with valid options parameter.
     **/
    public void Var003()
    {
        try
            {
            // buffer for data
            byte[] buf = new byte[2048];

            // fill the buffer
            for (int i=0; i<2048; i++) buf[i] = (byte)'9';

            // create an empty print parameter list
            PrintParameterList pList = new PrintParameterList();

            // create a PrintParameterList with the a specific form type
            pList.setParameter(PrintObject.ATTR_FORMTYPE, "JAVA");
            pList.setParameter(PrintObject.ATTR_SPOOLFILE,"JAVAPRINT");

            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, pList, null, null);

            // write some data
            outStream.write(buf);

            // close the spooled file
            outStream.close();

            // retrieve the spooled file
            SpooledFile splF = outStream.getSpooledFile();

            if ((splF.getStringAttribute(PrintObject.ATTR_FORMTYPE).trim().equals("JAVA")) &&
                (splF.getStringAttribute(PrintObject.ATTR_SPOOLFILE).trim().equals("JAVAPRINT")))
                succeeded();
            else failed("Could not create a spooled file with valid options parameter");

            // delete the spooled file we created
            splF.delete();
            
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests constructing a spooled file output stream with output queue options set.
     * Verifies output queue in options prevails over output queue parameter.
     **/
    public void Var004()
    {
        try
            {
            // create a output queue object
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CTORTST.OUTQ");

            // create another output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/PRTJAVA) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/PRTJAVA) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
                {
                failed("Could not grant *public authority to output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            // create a output queue object
            OutputQueue outQ1 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/PRTJAVA.OUTQ");

            // create an empty print parameter list
            PrintParameterList pList = new PrintParameterList();

            // create a PrintParameterList with the a printer file
            pList.setParameter(PrintObject.ATTR_OUTPUT_QUEUE, outQ1.getPath());

            // buffer for data
            byte[] buf = new byte[2048];

            // fill the buffer
            for (int i=0; i<2048; i++) buf[i] = (byte)'9';

            // save the number of files on the output queues
            int prevNum = outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();
            int prevNum1 = outQ1.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, pList, null, outQ);

            // write some data
            outStream.write(buf);

            // close the spooled file
            outStream.close();

            // update the output queue attributes
            outQ.update();
            outQ1.update();

            // check the number of files on output queues
            if ((outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() == prevNum) &&
                (outQ1.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() == prevNum1+1))
                {
                succeeded();
                }
            else failed("Output queue in parms list did not prevail.");

            // clear the output queue we created.
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/PRTJAVA)") != true)
                {
                output_.println("Could not clear the output queue we created."
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/PRTJAVA)") != true)
                {
                output_.println("Could not delete the output queue we created."
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests constructing a spooled file output stream with valid printerFile parameter.
     **/
    public void Var005()
    {
        try
            {
            // create a printer file object using valid system name and printer file name
            PrinterFile prtF = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // buffer for data
            byte[] buf = new byte[2048];

            // fill the buffer
            for (int i=0; i<2048; i++) buf[i] = (byte)'9';

            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, prtF, null);

            // write some data
            outStream.write(buf);

            // retrieve the spooled file that we created
            SpooledFile splF = outStream.getSpooledFile();

            // check to see what the printer file attribute is set to
            if (splF.getStringAttribute(PrintObject.ATTR_PRINTER_FILE).trim().equals("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE")) succeeded();
            else failed("Printer File in parms list was not set.");

            // close the spooled file
            outStream.close();

            // delete the spooled file we created
            splF.delete();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests constructing a spooled file output stream with valid output queue parameter.
     **/
    public void Var006()
    {
        try
            {
            // create a output queue object
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CTORTST.OUTQ");

            // buffer for data
            byte[] buf = new byte[2048];

            // fill the buffer
            for (int i=0; i<2048; i++) buf[i] = (byte)'9';

            // save the number of files on the output queue
            int prevNum = outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, outQ);

            // write some data
            outStream.write(buf);

            // close the spooled file
            outStream.close();

            // update the attributes on the output queue
            outQ.update();

            // query for number of files on output queue and it should be at least 1 more
            if (outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() < prevNum+1)
                {
                failed("Could not create spooled files on output queue");
                }
            else succeeded();

            // retrieve the spooled file
            SpooledFile splF = outStream.getSpooledFile();

            // delete the spooled file we created
            splF.delete();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var006

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

} // end NPSplFOutStrCtorTestcase class


