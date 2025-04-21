///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQListSerializeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

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

import test.PasswordVault;
import test.Testcase;

/**
 Testcase NPOutQListSerializeTestcase.
 **/
public class NPOutQListSerializeTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPOutQListSerializeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQListSerializeTestcase(AS400            systemObject,
                                       Vector<String> variationsToRun,
                                       int              runMode,
                                       FileOutputStream fileOutputStream,
                                       
                                       String           password)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPOutQListSerializeTestcase", 5,
	      variationsToRun, runMode, fileOutputStream,
               password);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQListSerializeTestcase");
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
     * Tests that OutputQueueList's can serialize/de-serialize and
     * add/remove listeners after de-serialization.
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

            // create a output queue list object
            OutputQueueList outQList1 = new OutputQueueList(systemObject_);

            // serialize the OutputQueueList object
            FileOutputStream fos = new FileOutputStream("OutputQueueList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(outQList1);
            oos.flush();

            // de-serialize the OutputQueueList object
            FileInputStream fis = new FileInputStream("OutputQueueList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            OutputQueueList outQList2 = (OutputQueueList)ois.readObject();

            // add/remove listeners
            outQList2.addPropertyChangeListener(propertyListener);
            outQList2.removePropertyChangeListener(propertyListener);

            outQList2.addVetoableChangeListener(vetoableListener);
            outQList2.removeVetoableChangeListener(vetoableListener);

            outQList2.addPrintObjectListListener(listListener);
            outQList2.removePrintObjectListListener(listListener);

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
                File f = new File("OutputQueueList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var001

    /**
     * Tests that OutputQueueList's system serializes/de-serializes.
     **/
    public void Var002()
    {
        try
            {
            // create an output queue list object using valid system name
            OutputQueueList outQList1 = new OutputQueueList(systemObject_);

            // serialize the OutputQueueList object
            FileOutputStream fos = new FileOutputStream("OutputQueueList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(outQList1);
            oos.flush();

            // de-serialize the OutputQueueList object
            FileInputStream fis = new FileInputStream("OutputQueueList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            OutputQueueList outQList2 = (OutputQueueList)ois.readObject();

            String system1 = outQList1.getSystem().getSystemName();
            String system2 = outQList2.getSystem().getSystemName();
            String user1 = outQList1.getSystem().getUserId();
            String user2 = outQList2.getSystem().getUserId();

            if( (system1.equals(system2)) && (user1.equals(user2)) )
                {
                succeeded();
                }
            else
                {
                failed("OutputQueueList's system did not serialize/de-serialize");
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
                File f = new File("OutputQueueList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var002

    /**
     * Tests that OutputQueueList's queueFilter can serialize/de-serialize.
     **/
    public void Var003()
    {
        try
            {
            // create a output queue list object
            OutputQueueList outQList1 = new OutputQueueList(systemObject_);

            // set the queueFilter
            outQList1.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LSERTST.OUTQ");

            // serialize the OutputQueueList object
            FileOutputStream fos = new FileOutputStream("OutputQueueList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(outQList1);
            oos.flush();

            // de-serialize the OutputQueueList object
            FileInputStream fis = new FileInputStream("OutputQueueList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            OutputQueueList outQList2 = (OutputQueueList)ois.readObject();

            if( outQList1.getQueueFilter().equals(outQList2.getQueueFilter()) )
                {
                succeeded();
                }
            else
                {
                failed("OutputQueueList queueFilter did not serialize/de-serialize");
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
                File f = new File("OutputQueueList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var003

    /**
     * Tests that OutputQueueList's set attributes to retrieve
     * can serialize/de-serialize.
     **/
    public void Var004()
    {
	try
            {
            // create a output queue list object
            OutputQueueList outQList1 = new OutputQueueList(systemObject_);

            // set the queueFilter
            outQList1.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LSERTST.OUTQ");

            // set the attributes to retrieve, only ask for status.
            int[] attrs = { PrintObject.ATTR_OUTQSTS };
            outQList1.setAttributesToRetrieve(attrs);

            // serialize the OutputQueueList object
            FileOutputStream fos = new FileOutputStream("OutputQueueList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(outQList1);
            oos.flush();

            // de-serialize the OutputQueueList object
            FileInputStream fis = new FileInputStream("OutputQueueList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            OutputQueueList outQList2 = (OutputQueueList)ois.readObject();

            // now build output queue list synchronously, we have
            // to set the password because that is not serialized.
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            outQList2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            outQList2.openSynchronously();

            boolean fSuccess = true;

            OutputQueue outQ = (OutputQueue)outQList2.getObject(0);

            // disconnect the system, if we try to access the
            // AS400 we will get connected again.
            outQ.getSystem().disconnectService(AS400.PRINT);

            // ATTR_OUTQSTS should be cached in the OutputQueue
            outQ.getStringAttribute(PrintObject.ATTR_OUTQSTS);
            if( outQ.getSystem().isConnected() )
                {
                fSuccess = false;
                }

            // ATTR_ORDER should NOT be cached in the OutputQueue
            outQ.getStringAttribute(PrintObject.ATTR_ORDER);
            if( !(outQ.getSystem().isConnected()) )
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

            outQList2.close();
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
                File f = new File("OutputQueueList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var004

    /**
     * Tests that a de-serialized OutputQueueList is functional.
     **/
    public void Var005()
    {
	try
            {
            // create a output queue list object
            OutputQueueList outQList1 = new OutputQueueList(systemObject_);

            // set the queueFilter
            outQList1.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LSERTST.OUTQ");

            // open the list and serialize in this state to make sure
            // that transient variables are not de-serialized.
            outQList1.openSynchronously();

            // serialize the OutputQueueList object
            FileOutputStream fos = new FileOutputStream("OutputQueueList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(outQList1);
            oos.flush();

            // de-serialize the OutputQueueList object
            FileInputStream fis = new FileInputStream("OutputQueueList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            OutputQueueList outQList2 = (OutputQueueList)ois.readObject();

            // after de-serializing set the password
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            outQList2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

            // use the de-serialized object 
            outQList2.openSynchronously();

            // retrieve the first output queue
            OutputQueue outQ = (OutputQueue)outQList2.getObject(0);

            // check to see if we got one output queue and verify name
            if ( (outQList2.size() == 1) &&
                 (outQ.getPath().trim().equals("/QSYS.LIB/NPJAVA.LIB/LSERTST.OUTQ")) )
                {
                // indicate how many output queues were listed
                output_.println(" " + outQList1.size() + " Output Queue(s) listed.");

                succeeded();
                }
            else
                {
                failed("De-serialized OutputQueueList could not be used");
                }

            outQList1.close();
            outQList2.close();
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
                File f = new File("OutputQueueList.ser");
                f.delete();
                }
            catch(Exception e){}
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

} // end NPOutQListSerializeTestcase class


