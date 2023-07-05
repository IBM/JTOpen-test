///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFListSerializeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.util.Vector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import com.ibm.as400.access.*;

/**
 Testcase NPSplFListSerializeTestcase.
 **/
public class NPSplFListSerializeTestcase extends Testcase
{
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFListSerializeTestcase(AS400            systemObject,
                                       Vector           variationsToRun,
                                       int              runMode,
                                       FileOutputStream fileOutputStream,
                                       
                                       String           password)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPSplFListSerializeTestcase", 8,
	      variationsToRun, runMode, fileOutputStream,
               password);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFListSerializeTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
	{
            // create LSERTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTOUTQ OUTQ(NPJAVA/LSERTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("GRTOBJAUT OBJ(NPJAVA/LSERTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
                {
                failed("Could not grant *public authority to output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            // create 10 spooled files on the output queue
            createSpooledFiles();

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
            if (cmd.run("CLROUTQ OUTQ(NPJAVA/LSERTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("DLTOUTQ OUTQ(NPJAVA/LSERTST)") == false)
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
     * Tests that SpooledFileList's can serialize/de-serialize and
     * add/remove listeners after de-serializing.
     **/
    public void Var001()
    {
        try
            {
            // Define inner class to listen for property change events
            class PropertyListener implements java.beans.PropertyChangeListener
            {
                public void propertyChange(PropertyChangeEvent evt){}
            }
            PropertyListener propertyListener = new PropertyListener();

            // Define inner class to listen for vetoable change events
            class VetoableListener implements java.beans.VetoableChangeListener
            {
                public void vetoableChange(PropertyChangeEvent evt){}
            }
            VetoableListener vetoableListener = new VetoableListener();

            // Define inner class to listen for print object list events
            class ListListener implements PrintObjectListListener
            {
                public void listOpened(PrintObjectListEvent evt){}
                public void listClosed(PrintObjectListEvent evt){}
                public void listCompleted(PrintObjectListEvent evt){}
                public void listObjectAdded(PrintObjectListEvent evt){}
                public void listErrorOccurred(PrintObjectListEvent evt){}
            }
            ListListener listListener = new ListListener();

            // create a spooled file list object
            SpooledFileList splFList1 = new SpooledFileList(systemObject_);

            // serialize the SpooledFileList object
            FileOutputStream fos = new FileOutputStream("SpooledFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(splFList1);
            oos.flush();

            // de-serialize the SpooledFileList object
            FileInputStream fis = new FileInputStream("SpooledFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SpooledFileList splFList2 = (SpooledFileList)ois.readObject();

            // add/remove listeners
            splFList2.addPropertyChangeListener(propertyListener);
            splFList2.removePropertyChangeListener(propertyListener);

            splFList2.addVetoableChangeListener(vetoableListener);
            splFList2.removeVetoableChangeListener(vetoableListener);

            splFList2.addPrintObjectListListener(listListener);
            splFList2.removePrintObjectListListener(listListener);

            succeeded();
            } 

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("SpooledFileList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var001

    /**
     * Tests that SpooledFileList's system serializes/de-serializes.
     **/
    public void Var002()
    {
        try
            {
            // create an spooled file list object using valid system name
            SpooledFileList splFList1 = new SpooledFileList(systemObject_);

            // serialize the SpooledFileList object
            FileOutputStream fos = new FileOutputStream("SpooledFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(splFList1);
            oos.flush();

            // de-serialize the SpooledFileList object
            FileInputStream fis = new FileInputStream("SpooledFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SpooledFileList splFList2 = (SpooledFileList)ois.readObject();

            String system1 = splFList1.getSystem().getSystemName();
            String system2 = splFList2.getSystem().getSystemName();
            String user1 = splFList1.getSystem().getUserId();
            String user2 = splFList2.getSystem().getUserId();

            if( (system1.equals(system2)) && (user1.equals(user2)) )
                {
                succeeded();
                }
            else
                {
                failed("SpooledFileList's system did not serialize/de-serialize");
                }

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("SpooledFileList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var002

    /**
     * Tests that SpooledFileList's formTypeFilter can serialize/de-serialize.
     **/
    public void Var003()
    {
        try
            {
            // create a spooled file list object
            SpooledFileList splFList1 = new SpooledFileList(systemObject_);

            // Set the formTypeFilter
            splFList1.setFormTypeFilter("JAVAPRINT");

            // serialize the SpooledFileList object
            FileOutputStream fos = new FileOutputStream("SpooledFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(splFList1);
            oos.flush();

            // de-serialize the SpooledFileList object
            FileInputStream fis = new FileInputStream("SpooledFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SpooledFileList splFList2 = (SpooledFileList)ois.readObject();

            if( splFList1.getFormTypeFilter().equals(splFList2.getFormTypeFilter()) )
                {
                succeeded();
                }
            else
                {
                failed("SpooledFileList formTypeFilter did not serialize/de-serialize");
                }

            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("SpooledFileList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var003

    /**
     * Tests that SpooledFileList's queueFilter can serialize/de-serialize.
     **/
    public void Var004()
    {
        try
            {
            // create a spooled file list object
            SpooledFileList splFList1 = new SpooledFileList(systemObject_);

            // set the queueFilter
            splFList1.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LSERTST.OUTQ");

            // serialize the SpooledFileList object
            FileOutputStream fos = new FileOutputStream("SpooledFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(splFList1);
            oos.flush();

            // de-serialize the SpooledFileList object
            FileInputStream fis = new FileInputStream("SpooledFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SpooledFileList splFList2 = (SpooledFileList)ois.readObject();

            if( splFList1.getQueueFilter().equals(splFList2.getQueueFilter()) )
                {
                succeeded();
                }
            else
                {
                failed("SpooledFileList queueFilter did not serialize/de-serialize");
                }

            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("SpooledFileList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var004

    /**
     * Tests that SpooledFileList's userFilter can serialize/de-serialize.
     **/
    public void Var005()
    {
        try
            {
            // create a spooled file list object
            SpooledFileList splFList1 = new SpooledFileList(systemObject_);

            // Set the userFilter
            splFList1.setUserFilter("JAVA");

            // serialize the SpooledFileList object
            FileOutputStream fos = new FileOutputStream("SpooledFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(splFList1);
            oos.flush();

            // de-serialize the SpooledFileList object
            FileInputStream fis = new FileInputStream("SpooledFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SpooledFileList splFList2 = (SpooledFileList)ois.readObject();

            if( splFList1.getUserFilter().equals(splFList2.getUserFilter()) )
                {
                succeeded();
                }
            else
                {
                failed("SpooledFileList userFilter did not serialize/de-serialize");
                }

            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("SpooledFileList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var005

    /**
     * Tests that SpooledFileList's userDataFilter can serialize/de-serialize.
     **/
    public void Var006()
    {
        try
            {
            // create a spooled file list object
            SpooledFileList splFList1 = new SpooledFileList(systemObject_);

            // Set the userDataFilter
            splFList1.setUserDataFilter("JAVAPRINT");

            // serialize the SpooledFileList object
            FileOutputStream fos = new FileOutputStream("SpooledFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(splFList1);
            oos.flush();

            // de-serialize the SpooledFileList object
            FileInputStream fis = new FileInputStream("SpooledFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SpooledFileList splFList2 = (SpooledFileList)ois.readObject();

            if( splFList1.getUserDataFilter().equals(splFList2.getUserDataFilter()) )
                {
                succeeded();
                }
            else
                {
                failed("SpooledFileList userDataFilter did not serialize/de-serialize");
                }

            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("SpooledFileList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var006

    /**
     * Tests that SpooledFileList's set attributes to retrieve
     * can serialize/de-serialize.
     **/
    public void Var007()
    {
	try
            {
            // create a spooled file list object
            SpooledFileList splFList1 = new SpooledFileList(systemObject_);

            // set the queueFilter
            splFList1.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LSERTST.OUTQ");

            // set the attributes to retrieve, only ask for time.
            int[] attrs = { PrintObject.ATTR_TIME };
            splFList1.setAttributesToRetrieve(attrs);

            // serialize the SpooledFileList object
            FileOutputStream fos = new FileOutputStream("SpooledFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(splFList1);
            oos.flush();

            // de-serialize the SpooledFileList object
            FileInputStream fis = new FileInputStream("SpooledFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SpooledFileList splFList2 = (SpooledFileList)ois.readObject();

            // now build spooled file list synchronously, we have
            // to set the password because that is not serialized.
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            splFList2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            splFList2.openSynchronously();

            boolean fSuccess = true;

            SpooledFile splF = (SpooledFile)splFList2.getObject(0);

            // disconnect the system, if we try to access the
            // AS400 we will get connected again.
            splF.getSystem().disconnectService(AS400.PRINT);

            // ATTR_TIME should be cached in the SpooledFile
            splF.getStringAttribute(PrintObject.ATTR_TIME);
            if( splF.getSystem().isConnected() )
                {
                fSuccess = false;
                }

            // ATTR_DATE should NOT be cached in the SpooledFile
            splF.getStringAttribute(PrintObject.ATTR_DATE);
            if( !(splF.getSystem().isConnected()) )
                {
                fSuccess = false;
                }

            // check to see if we were successful
            if (fSuccess == true)
                {
                succeeded();
                }
            else
                {
                failed("Attributes to retrieve did not serialize/de-serialize");
                }

            splFList2.close();
	    System.gc();

            } 

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("SpooledFileList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var007

    /**
     * Tests that a de-serialized SpooledFileList is functional.
     **/
    public void Var008()
    {
	try
            {
            // create a spooled file list object
            SpooledFileList splFList1 = new SpooledFileList(systemObject_);

            // set the queueFilter
            splFList1.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LSERTST.OUTQ");

            // open the list and serialize in this state to make sure
            // that transient variables are not de-serialized.
            splFList1.openSynchronously();

            // serialize the SpooledFileList object
            FileOutputStream fos = new FileOutputStream("SpooledFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(splFList1);
            oos.flush();

            // de-serialize the SpooledFileList object
            FileInputStream fis = new FileInputStream("SpooledFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SpooledFileList splFList2 = (SpooledFileList)ois.readObject();

            // after de-serializing set the password
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            splFList2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

            // use the de-serialized object 
            splFList2.openSynchronously();

            // check to see if we got ten spooled files
            if ( splFList2.size() == 10 )
                {
                // indicate how many spooled files were listed
                output_.println(" " + splFList2.size() + " Spooled File(s) listed.");
                succeeded();
                }
            else
                {
                failed("De-serialized SpooledFileList could not be used");
                }

            splFList1.close();
            splFList2.close();
	    System.gc();

            } // end try block

	catch (Exception e)
            {
	    failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("SpooledFileList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var008

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
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/LSERTST.OUTQ");

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

} // end NPSplFListSerializeTestcase class


