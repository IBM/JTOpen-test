///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQClearTestcase.java
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
 * Testcase NPOutQClearTestcase.
 * Tests the clear method on OutputQueue class.
 **/
public class NPOutQClearTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPOutQClearTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    // listener invoked?
    boolean clearedInvoked;
    boolean heldInvoked;
    boolean releasedInvoked;

    /**
     * Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQClearTestcase(AS400            systemObject,
				 Vector           variationsToRun,
				 int              runMode,
				 FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPOutQClearTestcase", 16,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQClearTestcase");
        boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create CLEARTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/CLEARTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/CLEARTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(2);
                Var002();
                }

            // run variation 3
            if ((allVariations || variationsToRun_.contains("3")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(3);
                Var003();
                }

            // run variation 4
            if ((allVariations || variationsToRun_.contains("4")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(4);
                Var004();
                }

            // run variation 5
            if ((allVariations || variationsToRun_.contains("5")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(5);
                Var005();
                }

            // run variation 6
            if ((allVariations || variationsToRun_.contains("6")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(6);
                Var006();
                }

            // run variation 7
            if ((allVariations || variationsToRun_.contains("7")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(7);
                Var007();
                }

            // run variation 8
            if ((allVariations || variationsToRun_.contains("8")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(8);
                Var008();
                }

            // run variation 9
            if ((allVariations || variationsToRun_.contains("9")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(9);
                Var009();
                }

            // run variation 10
            if ((allVariations || variationsToRun_.contains("10")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(10);
                Var010();
                }

            // run variation 11
            if ((allVariations || variationsToRun_.contains("11")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(11);
                Var011();
                }

            // run variation 12
            if ((allVariations || variationsToRun_.contains("12")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(12);
                Var012();
                }

            // run variation 13
            if ((allVariations || variationsToRun_.contains("13")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(13);
                Var013();
                }

            // run variation 14
            if ((allVariations || variationsToRun_.contains("14")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(14);
                Var014();
                }

            // run variation 15
            if ((allVariations || variationsToRun_.contains("15")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(15);
                Var015();
                }

            // run variation 16
            if ((allVariations || variationsToRun_.contains("16")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(16);
                Var016();
                }

	    // $$$ TO DO $$$
	    // Add an 'if' block using the following as a template for each variation.
	    // Make sure you correctly set the variation number
	    // and runmode in the 'if' condition, and the
	    // variation number in the setVariation call.
	    //
	    // replace the X with the variation number you are adding
	    // and uncomment the block of code
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
	    if (cmd.run("CLROUTQ OUTQ(NPJAVA/CLEARTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

	    // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/CLEARTST)") == false)
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
     * clears an output queue without a print parameter list.
     **/
    public void Var001()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // Create spooled files 
	    createSpooledFiles(null);

	    // clear the output queue
	    outQ.clear(null);

	    // query for number of files on output queue and it should be empty
	    if (outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() != 0)
	    {
		failed("Could not clear output queue");
	    }
	    else
		succeeded();  // Note: This variation will be successful.

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var001

    /**
     * clears an output queue without a print parameter list, and
     * the output queue object state is not valid
     **/
    public void Var002()
    {
	try
            {
            // create a output queue object with default constructor
            OutputQueue outQ = new OutputQueue();

	    // clear the output queue
	    outQ.clear(null);
            failed("Should have gotten ExtendedIllegalStateException");
            }

        catch (ExtendedIllegalStateException e)
            {
            // We got the exception we were expecting.
            succeeded(); 
            }

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

    } // end Var002
             
    /**
     * Tests clearing an output queue with a print parameter list with Job user set to a specific userid.
     **/
    public void Var003()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // create an empty print parameter list
	    PrintParameterList pList = new PrintParameterList();

	    // Create spooled files with a specific userid
	    createSpooledFiles(pList);

	    // create a PrintParameterList with the user 
	    pList.setParameter(PrintObject.ATTR_JOBUSER, systemObject_.getUserId());

	    // query for number of files on output queue
	    int prevNum = outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();
	    
	    // clear the output queue
	    outQ.clear(pList);

	    // query for number of files on output queue and
	    // it should be at least 5 less than before
	    if (outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() > prevNum-5)
	    {
		failed("Could not clear output queue by specific user");
	    }
	    else
		succeeded();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var003

    /**
     * Tests clearing an output queue with a print parameter list with Job user set to *ALL.
     **/
    public void Var004()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ1 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // create an empty print parameter list
	    PrintParameterList pList1 = new PrintParameterList();

	    // create a PrintParameterList with the user set to *ALL
	    pList1.setParameter(PrintObject.ATTR_JOBUSER, "*ALL");

	    // Create spooled files with *ALL userid
	    createSpooledFiles(pList1);

	    // clear the output queue
	    outQ1.clear(pList1);

	    // query for number of files on output queue and it should be 0
	    if (outQ1.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() != 0)
	    {
		failed("Did not clear output queue with user set to *ALL");
	    }
	    else
		succeeded();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var004

    /**
     * Tests clearing an output queue with a print parameter list with Job user set to *CURRENT.
     **/
    public void Var005()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ2 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // create an empty print parameter list
	    PrintParameterList pList2 = new PrintParameterList();

	    // create a PrintParameterList with the user set to *CURRENT
	    pList2.setParameter(PrintObject.ATTR_JOBUSER, "*CURRENT");

	    // Create spooled files with a *CURRENT userid
	    createSpooledFiles(pList2);

	    // query for number of files on output queue
	    int prevNum2 = outQ2.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

	    // clear the output queue
	    outQ2.clear(pList2);

	    // query for number of files on output queue and
	    // it should be at least 5 less than before
	    if (outQ2.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() > prevNum2-5)
	    {
		failed("Did not clear output queue with user set to *CURRENT");
	    }
	    else
		succeeded();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var005

    /**
     * Tests clearing an output queue with a print parameter list with Job user set to an invalid userid.
     **/
    public void Var006()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ3 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // create an empty print parameter list
	    PrintParameterList pList3 = new PrintParameterList();

	    // create a PrintParameterList with the user set to an invalid user id
	    pList3.setParameter(PrintObject.ATTR_JOBUSER, "INVALIDUSERID");

	    // Create spooled files 
	    createSpooledFiles(null);

	    // query for number of files on output queue
	    int prevNum3 = outQ3.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

	    // clear the output queue
	    outQ3.clear(pList3);

	    // query for number of files on output queue and
	    // it should equal the same number as before
	    if (outQ3.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() != prevNum3)
	    {
		failed("Cleared output queue by with invalid user id");
	    }
	    else
		succeeded();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var006

    /**
     * Tests clearing an output queue with a print parameter list with formtype set to a specific form.
     **/
    public void Var007()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // create an empty print parameter list
	    PrintParameterList pList = new PrintParameterList();

	    // create a PrintParameterList with the a specific form type
	    pList.setParameter(PrintObject.ATTR_FORMTYPE, "JAVA");

	    // Create spooled files with a specific form type
	    createSpooledFiles(pList);

	    // query for number of files on output queue
	    int prevNum = outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

	    // clear the output queue
	    outQ.clear(pList);

	    // query for number of files on output queue and
	    // it should be at least 5 less than before
	    if (outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() > prevNum-5)
	    {
		failed("Did not clear output queue with form type set to specific form");
	    }
	    else
		succeeded();  // Note: This variation will be successful.

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var007

    /**
     * Tests clearing an output queue with a print parameter list with formtype set to *ALL.
     **/
    public void Var008()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ1 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // create an empty print parameter list
	    PrintParameterList pList1 = new PrintParameterList();

	    // create a PrintParameterList with *ALL form type
	    pList1.setParameter(PrintObject.ATTR_FORMTYPE, "*ALL");

	    // Create spooled files with a *ALL form type
	    createSpooledFiles(pList1);

	    // query for number of files on output queue
	    int prevNum = outQ1.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

	    // clear the output queue
	    outQ1.clear(pList1);

	    // query for number of files on output queue and
	    // it should be at least 5 less than before
	    if (outQ1.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() > prevNum-5)
	    {
		failed("Did not clear output queue with form type set *ALL");
	    }
	    else
		succeeded();  // Note: This variation will be successful.

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var008

    /**
     * Tests clearing an output queue with a print parameter list with formtype set to *STD.
     **/
    public void Var009()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ2 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // create an empty print parameter list
	    PrintParameterList pList2 = new PrintParameterList();

	    // create a PrintParameterList with the *STD form type
	    pList2.setParameter(PrintObject.ATTR_FORMTYPE, "*STD");

	    // Create spooled files with a *STD
	    createSpooledFiles(pList2);

	    // query for number of files on output queue
	    int prevNum2 = outQ2.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

	    // clear the output queue
	    outQ2.clear(pList2);

	    // query for number of files on output queue and
	    // it should be at least 5 less than before
	    if (outQ2.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() > prevNum2-5)
	    {
		failed("Did not clear output queue with form type set to *ALL");
	    }
	    else
		succeeded();  // Note: This variation will be successful.
	    
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var009

    /**
     * Tests clearing an output queue with a print parameter list with formtype set to an invalid value.
     **/
    public void Var010()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ3 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // Create spooled files
	    createSpooledFiles(null);

	    // query for number of files on output queue
	    int prevNum3 = outQ3.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

	    // create an empty print parameter list
	    PrintParameterList pList3 = new PrintParameterList();

	    // create a PrintParameterList with an invalid form type
	    pList3.setParameter(PrintObject.ATTR_FORMTYPE, "*XXX");

	    // clear the output queue
	    outQ3.clear(pList3);

	    // query for number of files on output queue and equal the previous number
	    if (outQ3.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() != prevNum3)
	    {
		failed("Cleared output queue with invalid form type");
	    }
	    else
		succeeded();  // Note: This variation will be successful.

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var010

    /**
     * Tests clearing an output queue with a print parameter list with User Data set to a specific value.
     **/
    public void Var011()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // create an empty print parameter list
	    PrintParameterList pList = new PrintParameterList();

	    // create a PrintParameterList with the user data 
	    pList.setParameter(PrintObject.ATTR_USERDATA, "Java");

	    // Create spooled files with a specific user data
	    createSpooledFiles(pList);

	    // query for number of files on output queue
	    int prevNum = outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

	    // clear the output queue
	    outQ.clear(pList);

	    // query for number of files on output queue and
	    // it should be at least 5 less than before
	    if (outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() > prevNum-5)
	    {
		failed("Could not clear output queue by specific user");
	    }
	    else
		succeeded();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var011

    /**
     * Tests clearing an output queue with a print parameter list with User Data set to *ALL.
     **/
    public void Var012()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ1 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // Create spooled files
	    createSpooledFiles(null);

	    // create an empty print parameter list
	    PrintParameterList pList1 = new PrintParameterList();

	    // create a PrintParameterList with the user data set to *ALL
	    pList1.setParameter(PrintObject.ATTR_USERDATA, "JAVAUSR");

	    // Create spooled files
	    createSpooledFiles(pList1);

	    // create a PrintParameterList with the user data set to *ALL
	    pList1.setParameter(PrintObject.ATTR_USERDATA, "*ALL");

	    // query for number of files on output queue
	    int prevNum = outQ1.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

	    // clear the output queue
	    outQ1.clear(pList1);

	    // query for number of files on output queue and
            // it should be at least 10 less than before
	    if (outQ1.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue()  > prevNum-10)
	    {
		failed("Did not clear output queue with user data set to *ALL");
	    }
	    else
		succeeded();

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var012

    /**
     * Tests clearing an output queue with a print parameter list with User Data set to an invalid value.
     **/
    public void Var013()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ2 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // create an empty print parameter list
	    PrintParameterList pList2 = new PrintParameterList();

	    // create a PrintParameterList with the user set to an invalid user data value
	    pList2.setParameter(PrintObject.ATTR_USERDATA, "*XXXXX");

	    // Create spooled files 
	    createSpooledFiles(null);

	    // query for number of files on output queue
	    int prevNum2 = outQ2.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

	    // clear the output queue
	    outQ2.clear(pList2);

	    // query for number of files on output queue and
	    // it should equal the same number as before
	    if (outQ2.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() != prevNum2)
	    {
		failed("Cleared output queue by with invalid user id");
	    }
	    else
		succeeded();
	    
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var013

    /**
     * Tests clearing an output queue with a print parameter list with formtype set to an invalid value.
     **/
    public void Var014()
    {
	try
	{
	    // create a output queue object
	    OutputQueue outQ3 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // Create spooled files
	    createSpooledFiles(null);

	    // query for number of files on output queue
	    int prevNum3 = outQ3.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

	    // create an empty print parameter list
	    PrintParameterList pList3 = new PrintParameterList();

	    // create a PrintParameterList with an invalid form type
	    pList3.setParameter(PrintObject.ATTR_FORMTYPE, "PRINT");

	    // clear the output queue
	    outQ3.clear(pList3);

	    // query for number of files on output queue and equal the previous number
	    if (outQ3.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() != prevNum3)
	    {
		failed("Cleared output queue with invalid form type");
	    }
	    else
		succeeded();  // Note: This variation will be successful.

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var014

    /**
     * Tests that a output queue cleared event is fired when the
     * output queue is cleared.
     **/
    public void Var015()
    {
        // Define inner class to listen for output queue events
        class OutQListener implements OutputQueueListener
        {
            public void outputQueueCleared( OutputQueueEvent evt )
            {
                // make sure the event ID is correct
                if( evt.getID() == OutputQueueEvent.CLEARED )
                    {
                    clearedInvoked = true;
                    }
            }

            public void outputQueueHeld( OutputQueueEvent evt )
            {
                // allow any event ID to catch mistakes
                heldInvoked = true;
            }

            public void outputQueueReleased( OutputQueueEvent evt )
            {
                // allow any event ID to catch mistakes
                releasedInvoked = true;
            }
        }
        OutQListener outQListener = new OutQListener();

	try
	{
	    // create a output queue object
	    OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // Create spooled files 
	    createSpooledFiles(null);

            // reset our flags
            clearedInvoked = false;
            heldInvoked = false;
            releasedInvoked = false;

            // add the listener
            outQ.addOutputQueueListener(outQListener);

	    // clear the output queue
	    outQ.clear(null);

            if( clearedInvoked & !heldInvoked & !releasedInvoked )
                {
                succeeded(); 
                }
            else
                {
                failed("output queue cleared event was not fired");
                }

            // remove the listener
            outQ.removeOutputQueueListener(outQListener);
        } 

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var015

    /**
     * Tests that an output queue listener is actually removed
     **/
    public void Var016()
    {
        // Define inner class to listen for output queue events
        class OutQListener implements OutputQueueListener
        {
            public void outputQueueCleared( OutputQueueEvent evt )
            {
                // allow any event ID to catch mistakes
                clearedInvoked = true;
            }

            public void outputQueueHeld( OutputQueueEvent evt )
            {
                // allow any event ID to catch mistakes
                heldInvoked = true;
            }

            public void outputQueueReleased( OutputQueueEvent evt )
            {
                // allow any event ID to catch mistakes
                releasedInvoked = true;
            }
        }
        OutQListener outQListener = new OutQListener();

	try
	{
	    // create a output queue object
	    OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	    // Create spooled files 
	    createSpooledFiles(null);

            // reset our flags
            clearedInvoked = false;
            heldInvoked = false;
            releasedInvoked = false;

            // add the listener
            outQ.addOutputQueueListener(outQListener);

            // remove the listener
            outQ.removeOutputQueueListener(outQListener);

	    // clear the output queue
	    outQ.clear(null);

            if( heldInvoked | clearedInvoked | releasedInvoked )
                {
                failed("Output queue listener was not removed");
                }
            else
                {
                succeeded();
                }

            // remove the listener again, should be OK.
            outQ.removeOutputQueueListener(outQListener);
        } 

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var016

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


///////////////////
// Private methods
///////////////////

    // This method creates spooled files and moves them to the NPJAVA/TESTOUTQ.
    private void createSpooledFiles(PrintParameterList parms)
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException,
             RequestNotSupportedException
    {
	// create a output queue object
	OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");

	// buffer for data
	byte[] buf = new byte[2048];

	// fill the buffer
	for (int i=0; i<2048; i++) buf[i] = (byte)'9';

	// save the number of files on the output queue
	int prevNum = outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

	// create the five spooled files under current userid
	for (int i=0; i<5; i++)
	{
	    // create a spooled file output stream
	    SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, parms, null, outQ);

	    // write some data
	    outStream.write(buf);
	    
	    // close the spooled file
	    outStream.close();
	}

        // update the output queue so it's attributes are valid
        outQ.update();

	// query for number of files on output queue and it should be at least 5 more
	if (outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() < prevNum+5)
	{
	    throw new IOException("Could not create spooled files on output queue");
	}

	// create the five spooled files under different userid
	AS400 sys = new AS400(systemObject_.getSystemName(), "javateam", "jteam1");

	// create another output queue object with the new system			// @A1A
	OutputQueue outQ2 = new OutputQueue(sys, "/QSYS.LIB/NPJAVA.LIB/CLEARTST.OUTQ");	// @A1A

	for (int i=0; i<5; i++)
	{
	    // create a spooled file output stream
	    SpooledFileOutputStream outStream = new SpooledFileOutputStream(sys, parms, null, outQ2);

	    // write some data
	    outStream.write(buf);
	    
	    // close the spooled file
	    outStream.close();
	}

        // update the output queue so it's attributes are valid
        outQ.update();

	// query for number of files on output queue and it should be at least 10 more
	if (outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() < prevNum+10)
	{
	    throw new IOException("Could not create spooled files on output queue");
	}

   } // end createSpooledFiles

} // end NPOutQClearTestcase class


