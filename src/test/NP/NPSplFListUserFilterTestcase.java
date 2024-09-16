///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFListUserFilterTestcase.java
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase NPSplFListUserFilterTestcase.
 **/
public class NPSplFListUserFilterTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFListUserFilterTestcase";
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
    public NPSplFListUserFilterTestcase(AS400            systemObject,
                                        Vector           variationsToRun,
                                        int              runMode,
                                        FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPSplFListUserFilterTestcase", 13,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFListUserFilterTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
            {
            // create USRTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/USRTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/USRTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/USRTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/USRTST)") == false)
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
     * Tests setting then getting the SpooledFileList's userFilter
     **/
    public void Var001()
    {
        try
            {
            // create an SpooledFileList object using default constructor
            SpooledFileList list = new SpooledFileList();

            // Set the userFilter
            list.setUserFilter("JAVA");

            if (list.getUserFilter().trim().equals("JAVA")) succeeded();
            else failed("Could not set/get SpooledFileList userFilter.");
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting the SpooledFileList's userFilter
     * to an invalid value.
     **/
    public void Var002()
    {
        try
            {
            // create an SpooledFileList object using default constructor
            SpooledFileList list = new SpooledFileList();

            // Set the userFilter, > 10 characters is not valid.
            list.setUserFilter("01234567890");
            failed("Could set an invalid SpooledFile userFilter");
            }

        catch (ExtendedIllegalArgumentException e)
            {
            succeeded();
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting the SpooledFileList's userFilter,
     * then removing the filter using an empty string.
     **/
    public void Var003()
    {
        try
            {
            // create an Spooled File List object using default constructor
            SpooledFileList list = new SpooledFileList();

            // Set the userFilter
            list.setUserFilter("JAVA");

            // Now remove the filter
            list.setUserFilter("");

            if (list.getUserFilter().trim().equals("")) succeeded();
            else failed("Could not remove SpooledFileList userFilter.");
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests setting the SpooledFileList's userFilter to null.
     **/
    public void Var004()
    {
        try
            {
            // create an Spooled File List object using default constructor
            SpooledFileList list = new SpooledFileList();

            list.setUserFilter(null);
            failed("Could set the userFilter to null");
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
     * Tests retrieving the SpooledFileList's userFilter
     * before it is set.
     **/
    public void Var005()
    {
        try
            {
            // create an Spooled File List object using default constructor
            SpooledFileList list = new SpooledFileList();

            if( list.getUserFilter().length() == 0 )
                {
                succeeded();
                }
            else
                {
                failed("userFilter was not set, expecting empty string");
                }
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Tests listing spooled files using a specific userFilter
     **/
    public void Var006()
    {
        try
            {
            // create 10 spooled files on the output queue
            createSpooledFiles();

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queue filter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/USRTST.OUTQ");

            // set the userFilter 
            splFList.setUserFilter(systemObject_.getUserId());

            // now try to build list synchrously
            splFList.openSynchronously();

            int listed = 0, size;
            size = splFList.size();
            String splFName = null;
            SpooledFile splFile = null;
            String userName = systemObject_.getUserId();

            // check to see if we got at least five spooled file
            if (size >= 5)
                {
                boolean fSuccess = false;

                // check the spooled files we go back
                while (listed < size)
                    {
                    splFile = (SpooledFile)splFList.getObject(listed++);
                    splFName = splFile.getName();
                    if (splFile.getStringAttribute(PrintObject.ATTR_JOBUSER).trim().equals(userName))
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
                    failed("Incorrect spooled file listed: " + splFName +
                           " user:"+splFile.getStringAttribute(PrintObject.ATTR_JOBUSER));

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
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/USRTST.OUTQ");

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
     * Tests listing spooled files using *ALL userFilter
     **/
    public void Var007()
    {
        try
            {
            // create 10 spooled files on the output queue
            createSpooledFiles();

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queue filter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/USRTST.OUTQ");

            // set the userFilter 
            splFList.setUserFilter("*ALL");

            // now try to build list synchrously
            splFList.openSynchronously();

            int listed = 0, size;
            size = splFList.size();

            // check to see tbat we got at 10 spooled files
            if (size == 10)
                {

                // indicate how many file(s) were listed
                output_.println(size + " spooled file(s) listed.");

                succeeded();
                } // end if size
            else 
                {
                failed("Not enought spooled files found.");
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
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/USRTST.OUTQ");

            // clear the output queue
            outQ.clear(pList);

            System.gc();
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var007

    /**
     * Tests listing spooled files using *CURRENT userFilter
     **/
    public void Var008()
    {
        try
            {
            // create 10 spooled files on the output queue
            createSpooledFiles();

            // create a spooled file list object
            SpooledFileList splFList = new SpooledFileList(systemObject_);

            // set the output queue filter 
            splFList.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/USRTST.OUTQ");

            // set the userFilter 
            splFList.setUserFilter("*CURRENT");

            // now try to build list synchrously
            splFList.openSynchronously();

            int listed = 0, size;
            size = splFList.size();
            String splFName = null;
            SpooledFile splFile = null;
            String userName = systemObject_.getUserId();

            // check to see if we got at least five spooled file
            if (size >= 5)
                {
                boolean fSuccess = false;

                // check the spooled files we go back
                while (listed < size)
                    {
                    splFile = (SpooledFile)splFList.getObject(listed++);
                    splFName = splFile.getName();
                    if (splFile.getStringAttribute(PrintObject.ATTR_JOBUSER).trim().equals(userName))
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
                    failed("Incorrect spooled file listed: " + splFName +
                           " user:"+splFile.getStringAttribute(PrintObject.ATTR_JOBUSER));

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
            OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/USRTST.OUTQ");

            // clear the output queue
            outQ.clear(pList);

            System.gc();
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var008

    /**
     * Tests that a property change event is fired when the
     * SpooledFileList's userFilter is set.
     **/
    public void Var009()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                listenerInvoked = true; 
                boolean succeeded = true;
                String reason = "";

                if( !(evt.getPropertyName().equals("userFilter")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(((String)evt.getOldValue()).equals("")) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(((String)evt.getNewValue()).equals("JAVA")) )
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

            // set the userFilter
            list.setUserFilter("JAVA");

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

    } // end Var009

    /**
     * Tests that a vetoable change event is fired when the
     * SpooledFileList's userFilter is set.
     **/
    public void Var010()
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

                if( !(evt.getPropertyName().equals("userFilter")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(((String)evt.getOldValue()).equals("")) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(((String)evt.getNewValue()).equals("JAVA")) )
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

            // set the userFilter
            list.setUserFilter("JAVA");

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

    } // end Var010

    /**
     * Tests that a property change event is NOT fired when the
     * SpooledFileList's userFilter property change is vetoed.
     **/
    public void Var011()
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
                if( evt.getPropertyName().equals("userFilter") )
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

            // set the userFilter, consume PropertyVetoException
            try
                {
                list.setUserFilter("JAVA");
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

    } // end Var011

    /**
     * Tests that a PropertyVetoException is thrown when the 
     * SpooledFileList's userFilter property change is vetoed.
     **/
    public void Var012()
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

            // set the userFilter, a PropertyVetoException should be thrown
            list.setUserFilter("JAVA");

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

    } // end Var012

    /**
     * Tests that the property change and vetoable change listeners               
     * are actually removed.
     **/
    public void Var013()
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

            // set the userFilter 
            list.setUserFilter("JAVA");

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

    } // end Var013

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
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/USRTST.OUTQ");

        // save the number of files on the output queue
        int prevNum = outQ.getIntegerAttribute(PrintObject.ATTR_NUMFILES).intValue();

        // create the spooled files
        for (int i=0; i<5; i++)
            {
            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, outQ);

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

        // create the five spooled files under different userid
        AS400 sys = new AS400(systemObject_.getSystemName(), "javateam", "jteam1");

        // create a reference to the output queue with the new system object           // @A1A
        OutputQueue outQ2 = new OutputQueue(sys, "/QSYS.LIB/NPJAVA.LIB/USRTST.OUTQ");  // @A1A

        for (int i=0; i<5; i++)
            {
            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(sys, null, null, outQ2);  // @A1A

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

} // end NPSplFListUserFilterTestcase class


