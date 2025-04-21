///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFListQueueFilterTestcase.java
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import java.beans.PropertyVetoException;

/**
 Testcase NPSplFListQueueFilterTestcase.
 **/
public class NPSplFListQueueFilterTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFListQueueFilterTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    // was the listener invoked?
    boolean listenerInvoked;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFListQueueFilterTestcase(AS400            systemObject,
                                         Vector<String> variationsToRun,
                                         int              runMode,
                                         FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPSplFListQueueFilterTestcase", 16,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFListQueueFilterTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create QUETST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/QUETST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/QUETST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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

            // run variation 6
            if ((allVariations || variationsToRun_.contains("6")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(6);
                Var006();
                }

            // run variation 7
            if ((allVariations || variationsToRun_.contains("7")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(7);
                Var007();
                }

            // run variation 8
            if ((allVariations || variationsToRun_.contains("8")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(8);
                Var008();
                }

            // run variation 9
            if ((allVariations || variationsToRun_.contains("9")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(9);
                Var009();
                }

            // run variation 10
            if ((allVariations || variationsToRun_.contains("10")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(10);
                Var010();
                }

            // run variation 11
            if ((allVariations || variationsToRun_.contains("11")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(11);
                Var011();
                }

            // run variation 12
            if ((allVariations || variationsToRun_.contains("12")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(12);
                Var012();
                }

            // run variation 13
            if ((allVariations || variationsToRun_.contains("13")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(13);
                Var013();
                }

            // run variation 14
            if ((allVariations || variationsToRun_.contains("14")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(14);
                Var014();
                }

            // run variation 15
            if ((allVariations || variationsToRun_.contains("15")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(15);
                Var015();
                }

            // run variation 16
            if ((allVariations || variationsToRun_.contains("16")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/QUETST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/QUETST)") == false)
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
     * Tests setting then getting the SpooledFileList queueFilter
     **/
    public void Var001()
    {
        try
            {
            // create an SpooledFileList object using default constructor
            SpooledFileList list = new SpooledFileList();

            // Set the queueFilter
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            if (list.getQueueFilter().trim().equals("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ")) succeeded();
            else failed("Could not set/retrive SpooledFileList queueFilter.");
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting the SpooledFileList queueFilter
     * to an invalid type.
     **/
    public void Var002()
    {
        try
            {
            // create an SpooledFileList object using default constructor
            SpooledFileList list = new SpooledFileList();

            // Set the queueFilter, FILE is not valid
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.FILE");
            failed("Could set an invalid type in the queueFilter");
            } 

        catch (IllegalPathNameException e)
            {
            succeeded();
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting the SpooledFileList queueFilter,
     * then removing the filter using an empty string.
     **/
    public void Var003()
    {
        try
            {
            // create an SpooledFileList object using default constructor
            SpooledFileList list = new SpooledFileList();

            // Set the queueFilter
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            // Now remove the filter
            list.setQueueFilter("");

            if (list.getQueueFilter().trim().equals("")) succeeded();
            else failed("Could not remove SpooledFileList queueFilter.");
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests setting the SpooledFileList's queueFilter to null.
     **/
    public void Var004()
    {
        try
            {
            // create an SpooledFileList object using default constructor
            SpooledFileList list = new SpooledFileList();

            list.setQueueFilter(null);
            failed("Could set the queueFilter to null");
            }

        catch (NullPointerException e)
            {
            succeeded();
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests retrieving the SpooledFileList's queueFilter
     * before it is set.
     **/
    public void Var005()
    {
        try
            {
            // create an SpooledFileList object using default constructor
            SpooledFileList list = new SpooledFileList();

            if( list.getQueueFilter().length() == 0 )
                {
                succeeded();
                }
            else
                {
                failed("queueFilter was not set, expecting empty string");
                }
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests listing spooled file using a specific library queueFilter
     **/
    public void Var006()
    {
        try
            {
            // create 10 spooled files on the output queue
            createSpooledFiles();

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queueFilter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            // now try to build list synchrously
            splFList.openSynchronously();

            int listed = 0, size;
            size = splFList.size();
            String splFName = null;
            String splFOutQ = null;
            SpooledFile splFile = null;

            // check to see if we got at least 10 spooled file
            if (size >= 10)
                {
                boolean fSuccess = false;

                // check the spooled files we go back
                while (listed < size)
                    {
                    splFile = (SpooledFile)splFList.getObject(listed++);
                    splFName = splFile.getName();
                    splFOutQ = splFile.getStringAttribute(PrintObject.ATTR_OUTPUT_QUEUE);
                    if (splFOutQ.equals("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ"))
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
                failed("Not enough spooled files found.");
                }

            // close the list
            splFList.close();

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms to clear the entire output queue
            pList.setParameter(PrintObject.ATTR_JOBUSER, "*ALL");
            pList.setParameter(PrintObject.ATTR_FORMTYPE, "*ALL");
            pList.setParameter(PrintObject.ATTR_USERDATA, "*ALL");

            // create a reference to the output queue
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            // clear the output queue
            outQ.clear(pList);

            System.gc();
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var006

    /**
     * Tests listing spooled file using %CURLIB% library queueFilter
     * ASSUMPTION: QGPL is CURLIB, by default job description.  If someone
     * has changed the default job description to not have curlib be QGPL then
     * this variation will fail.
     **/
    public void Var007()
    {
        try
            {
            // create 10 spooled files on the output queue
            createCurLibSpooledFiles();

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queueFilter 
            splFList.setQueueFilter("/QSYS.LIB/%CURLIB%.LIB/QUETST.OUTQ");

            // now try to build list synchrously
            splFList.openSynchronously();

            int listed = 0, size;
            size = splFList.size();
            String splFName = null;
            String splFOutQ = null;
            SpooledFile splFile = null;

            boolean fSuccess = false;

            // check the spooled files we go back
            while (listed < size)
                {
                splFile = (SpooledFile)splFList.getObject(listed++);
                splFName = splFile.getName();
                splFOutQ = splFile.getStringAttribute(PrintObject.ATTR_OUTPUT_QUEUE);
                if (splFOutQ.startsWith("/QSYS.LIB/QGPL.LIB/"))
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

            // close the list
            splFList.close();

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms to clear the entire output queue
            pList.setParameter(PrintObject.ATTR_JOBUSER, "*ALL");
            pList.setParameter(PrintObject.ATTR_FORMTYPE, "*ALL");
            pList.setParameter(PrintObject.ATTR_USERDATA, "*ALL");

            // create a reference to the output queue
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/QUETST.OUTQ");

            // clear the output queue
            outQ.clear(pList);

            // delete the output queue we created.
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("DLTOUTQ OUTQ(QGPL/QUETST)") == false)
                {
                output_.println("Could not delete output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            System.gc();
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception. Note: this variation expects that QGPL is curlib from "+
                   "the default job description.  Check to verify that is correct.");
            }

    } // end Var007

    /**
     * Tests listing spooled file using %LIBL% library queueFilter
     * ASSUMPTION: QGPL is CURLIB, by default job description.  If someone
     * has changed the default job description to not have curlib be QGPL then
     * this variation will fail.
     **/
    public void Var008()
    {
        try
            {
            // create 10 spooled files on the output queue
            createCurLibSpooledFiles();

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queueFilter 
            splFList.setQueueFilter("/QSYS.LIB/%LIBL%.LIB/QUETST.OUTQ");

            // now try to build list synchrously
            splFList.openSynchronously();

            int listed = 0, size;
            size = splFList.size();
            String splFName = null;
            String splFOutQ = null;
            SpooledFile splFile = null;

            boolean fSuccess = false;

            // check the spooled files we go back
            while (listed < size)
                {
                splFile = (SpooledFile)splFList.getObject(listed++);
                splFName = splFile.getName();
                splFOutQ = splFile.getStringAttribute(PrintObject.ATTR_OUTPUT_QUEUE);
                if (splFOutQ.startsWith("/QSYS.LIB/QGPL.LIB/"))
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

            // close the list
            splFList.close();

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms to clear the entire output queue
            pList.setParameter(PrintObject.ATTR_JOBUSER, "*ALL");
            pList.setParameter(PrintObject.ATTR_FORMTYPE, "*ALL");
            pList.setParameter(PrintObject.ATTR_USERDATA, "*ALL");

            // create a reference to the output queue
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/QUETST.OUTQ");

            // clear the output queue
            outQ.clear(pList);

            // delete the output queue we created.
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("DLTOUTQ OUTQ(QGPL/QUETST)") == false)
                {
                output_.println("Could not delete output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            System.gc();
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception. Note: this variation expects that QGPL is curlib from "+
                   "the default job description.  Check to verify that is correct.");
            }

    } // end Var008

    /**
     * Tests listing spooled file using %ALL% library and %ALL% queueFilter
     **/
    public void Var009()
    {
        try
            {
            // create 10 spooled files on the output queue
            createSpooledFiles();

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queueFilter 
            splFList.setQueueFilter("/QSYS.LIB/%ALL%.LIB/%ALL%.OUTQ");

            // now try to build list synchrously
            splFList.openSynchronously();

            int listed = 0, size;
            size = splFList.size();
            
            // indicate how many file(s) were listed
            output_.println(size + " spooled file(s) listed.");

            // check to see if we got at least 10 spooled file
            if (size >= 10)
                {
                succeeded();
                }
            else 
                {
                failed("Not enough spooled files found.");
                } // end if size

            // close the list
            splFList.close();

            // create a print parm list
            PrintParameterList pList = new PrintParameterList();

            // set some parms to clear the entire output queue
            pList.setParameter(PrintObject.ATTR_JOBUSER, "*ALL");
            pList.setParameter(PrintObject.ATTR_FORMTYPE, "*ALL");
            pList.setParameter(PrintObject.ATTR_USERDATA, "*ALL");

            // create a reference to the output queue
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            // clear the output queue
            outQ.clear(pList);

            System.gc();
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var009

    /**
     * Tests listing spooled file using %ALL% library and not %ALL% queueFilter.
     * Expects CPF9810
     **/
    public void Var010()
    {
        try
            {
            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queueFilter 
            splFList.setQueueFilter("/QSYS.LIB/%ALL%.LIB/QUETST.OUTQ");

            try
                {
                // now try to build list synchrously
                splFList.openSynchronously();

                failed("Could set library to %ALL% and not have queue to %ALL%.");

                }

            catch (AS400Exception e)
                {
                AS400Message msg = e.getAS400Message();
                if (!msg.getID().trim().equals("CPF9810"))
                    {
                    failed("Incorrect message received for setting an invalid queueFilter. ID="+msg.getID()+" "+msg.getText());
                    }
                else
                    {
                    succeeded();  // Note: This variation will be successful.
                    }
                }

            // close the list
            splFList.close();

            System.gc();
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var010

    /**
     * Tests listing spooled file using not a%ALL% library and %ALL% queueFilter.
     * Expects CPF9810
     **/
    public void Var011()
    {
        try
            {
            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queueFilter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.OUTQ");

            try
                {
                // now try to build list synchrously
                splFList.openSynchronously();

                failed("Could set specific library and have queue to %ALL%.");

                }

            catch (AS400Exception e)
                {
                AS400Message msg = e.getAS400Message();
                if (!msg.getID().trim().equals("CPF3C30"))
                    {
                    failed("Incorrect message received for setting an invalid queueFilter. ID="+msg.getID()+" "+msg.getText());
                    }
                else
                    {
                    succeeded();  // Note: This variation will be successful.
                    }
                }

            // close the list
            splFList.close();

            System.gc();
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var011

    /**
     * Tests that a property change event is fired when the
     * SpooledFileList's queueFilter is set.
     **/
    public void Var012()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                listenerInvoked = true; 
                boolean succeeded = true;
                String reason = "";

                if( !(evt.getPropertyName().equals("queueFilter")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(((String)evt.getOldValue()).equals("")) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(((String)evt.getNewValue()).equals(
                                  "/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ")) )
                    {
                    succeeded = false;
                    reason = "Problem with new value";
                    }

                if( succeeded )
                    {
                    succeeded();
                    }
                else
                    {
                    failed(reason);
                    }
            }
        }
        PropertyListener propertyListener = new PropertyListener();

        try
            {
            // create an Spooled File List object using the default constructor
            SpooledFileList list = new SpooledFileList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // set the queueFilter
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            if( !listenerInvoked )
                {
                failed("PropertyChange Listener was not invoked");
                }

            list.removePropertyChangeListener(propertyListener);
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var012

    /**
     * Tests that a vetoable change event is fired when the
     * SpooledFileList's queueFilter is set.
     **/
    public void Var013()
    {
        // Define inner class to listen for vetoable change events
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException   
            {
                listenerInvoked = true;
                boolean succeeded = true;
                String reason = "";

                if( !(evt.getPropertyName().equals("queueFilter")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(((String)evt.getOldValue()).equals("")) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(((String)evt.getNewValue()).equals(
                                  "/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ")) )
                    {
                    succeeded = false;
                    reason = "Problem with new value";
                    }

                if( succeeded )
                    {
                    succeeded();
                    }
                else
                    {
                    failed(reason);
                    }
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        try
            {
            // create an Spooled File List object using the default constructor
            SpooledFileList list = new SpooledFileList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the queueFilter
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            if( !listenerInvoked )
                {
                failed("VetoableChange Listener was not invoked");
                }

            list.removeVetoableChangeListener(vetoableListener);
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var013

    /**
     * Tests that a property change event is NOT fired when the
     * SpooledFileList's queueFilter property change is vetoed.
     **/
    public void Var014()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                // If this listener gets invoked the veto was ignored.
                listenerInvoked = true;
            }
        }
        PropertyListener propertyListener = new PropertyListener();

        // Define inner class to listen for vetoable change events.
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException
            {
                // Check the property name.
                if( evt.getPropertyName().equals("queueFilter") )
                    {
                    throw new PropertyVetoException("I veto", evt);
                    }
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        try
            {
            // create an Spooled File List object using the default constructor
            SpooledFileList list = new SpooledFileList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the queueFilter, consume PropertyVetoException
            try
                {
                list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");
                }
            catch(PropertyVetoException e)
                {
                // For this variation, consume the PropertyVetoException
                }

            if( listenerInvoked )
                {
                failed("PropertyChange event fired after property change vetoed");
                }
            else
                {
                succeeded();
                }

            // remove the listeners
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
            }

    } // end Var014

    /**
     * Tests that a PropertyVetoException is thrown when the 
     * SpooledFileList's queueFilter property change is vetoed.
     **/
    public void Var015()
    {
        // Define inner class to listen for vetoable change events
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException
            {
                // always veto the property change
                throw new PropertyVetoException("I veto", evt);
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        SpooledFileList list = null;
        try
            {
            // create an Spooled File List object using the default constructor
            list = new SpooledFileList();

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the system, a PropertyVetoException should be thrown
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            failed("Expecting PropertyVetoException");
            }

        catch( PropertyVetoException e )
            {
            succeeded();
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
            }

        finally
            {
            // remove the listener
            list.removeVetoableChangeListener(vetoableListener);
            }

    } // end Var015

    /**
     * Tests that the property change and vetoable change listeners               
     * are actually removed.
     **/
    public void Var016()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                listenerInvoked = true;
            }
        }
        PropertyListener propertyListener = new PropertyListener();

        // Define inner class to listen for vetoable change events 
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException
            {
                listenerInvoked = true;
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        SpooledFileList list = null;
        try
            {
            // create an Spooled File List object using the default constructor
            list = new SpooledFileList();

            // reset our flag
            listenerInvoked = false;

            // add the property change and vetoable change listeners
            list.addPropertyChangeListener(propertyListener);
            list.addVetoableChangeListener(vetoableListener);

            // remove the property change and vetoable change listeners
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);

            // set the queueFilter 
            list.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

            if( listenerInvoked )
                {
                failed("Listener was not removed");
                }
            else
                {
                succeeded();
                }

            // remove the listeners again, this should be OK.
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception" );
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

    // This method creates 10 spooled files
    private void createSpooledFiles()
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
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/QUETST.OUTQ");

        // save the number of files on the output queue
        int prevNum = outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

        // create the spooled files
        for (int i=0; i<5; i++)
            {
            // create a print parameter list for the spooled file name
            PrintParameterList pList = new PrintParameterList();

            // set the name
            pList.setParameter(PrintObject.ATTR_SPOOLFILE,"JAVAPRINT");

            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, pList, null, outQ);

            // write some data
            outStream.write(buf);

            // close the spooled file
            outStream.close();

            } // end for

        // update the output queue attributes
        outQ.update();

        // query for number of files on output queue and it should be at least 5 more
        if (outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() < prevNum+5)
            {
            throw new IOException("Could not create spooled files on output queue");
            }

        for (int i=0; i<5; i++)
            {
            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, outQ);

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

    // This method creates 10 spooled files on curlib output queue
    private void createCurLibSpooledFiles()
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException,
             PropertyVetoException,
             RequestNotSupportedException
    {
        // create QUETST output queue
        CommandCall cmd = new CommandCall(systemObject_);
        if (cmd.run("CRTOUTQ OUTQ(QGPL/QUETST) AUTCHK(*DTAAUT)") == false)
            {
            failed("Could not create an output queue. "
                   + cmd.getMessageList()[0].getID()
                   + ": " + cmd.getMessageList()[0].getText());
            return;
            }

        if (cmd.run("GRTOBJAUT OBJ(QGPL/QUETST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
            {
            failed("Could not grant *public authority to output queue. "
                   + cmd.getMessageList()[0].getID()
                   + ": " + cmd.getMessageList()[0].getText());
            return;
            }

        // create a reference to the output queue
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/QUETST.OUTQ");

        // buffer for data
        byte[] buf = new byte[2048];

        // fill the buffer
        for (int i=0; i<2048; i++) buf[i] = (byte)'9';

        // save the number of files on the output queue
        int prevNum = outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

        // create the spooled files
        for (int i=0; i<10; i++)
            {
            // create a print parameter list for the spooled file name
            PrintParameterList pList = new PrintParameterList();

            // set the name
            pList.setParameter(PrintObject.ATTR_SPOOLFILE,"JAVAPRINT");

            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, pList, null, outQ);

            // write some data
            outStream.write(buf);

            // close the spooled file
            outStream.close();

            } // end for

        // update the output queue attributes
        outQ.update();

        // query for number of files on output queue and it should be at least 10 more
        if (outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue() < prevNum+10)
            {
            throw new IOException("Could not create spooled files on output queue");
            }

    } // end createSpooledFiles

} // end NPSplFListQueueFilterTestcase class


