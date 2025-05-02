///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQSerializeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.OutputQueue;
import com.ibm.as400.access.PrintObject;

import test.PasswordVault;
import test.Testcase;

/**
 * Testcase NPOutQSerializeTestcase.
 **/
public class NPOutQSerializeTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPOutQSerializeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     * Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQSerializeTestcase(AS400            systemObject,
                                   Vector<String> variationsToRun,
                                   int              runMode,
                                   FileOutputStream fileOutputStream,
                                   
                                   String           password)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPOutQSerializeTestcase", 6,
	      variationsToRun, runMode, fileOutputStream,
               password);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQSerializeTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create SERTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("QSYS/CRTOUTQ OUTQ(NPJAVA/SERTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("QSYS/GRTOBJAUT OBJ(NPJAVA/SERTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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
                runMode_ != UNATTENDED) // Note: This is an attended variation.  // @A1C
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
            if (cmd.run("QSYS/CLROUTQ OUTQ(NPJAVA/SERTST)") == false)
                {
                output_.println("Could not clear output queue we created. "
                                + cmd.getMessageList()[0].getID()
                                + ": " + cmd.getMessageList()[0].getText());
                }

            // delete the output queue we created.
            if (cmd.run("QSYS/DLTOUTQ OUTQ(NPJAVA/SERTST)") == false)
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
     * Tests that OutputQueue's serializes/de-serializes and
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

            // create an output queue object using valid system name and queue name
            OutputQueue outQ1 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/SERTST.OUTQ");

            // serialize the output queue
            FileOutputStream fos = new FileOutputStream("OutputQueue.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(outQ1);
            oos.flush(); oos.close(); 

            // de-serialize the output queue
            FileInputStream fis = new FileInputStream("OutputQueue.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            OutputQueue outQ2 = (OutputQueue)ois.readObject();
            ois.close(); 

            // add/remove listeners
            outQ2.addPropertyChangeListener(propertyListener);
            outQ2.removePropertyChangeListener(propertyListener);

            outQ2.addVetoableChangeListener(vetoableListener);
            outQ2.removeVetoableChangeListener(vetoableListener);

            succeeded();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("OutputQueue.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var001

    /**
     * Tests that OutputQueue's system serializes/de-serializes.
     **/
    public void Var002()
    {
        try
            {
            // create an output queue object using valid system name and queue name
            OutputQueue outQ1 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/SERTST.OUTQ");

            // serialize the output queue
            FileOutputStream fos = new FileOutputStream("OutputQueue.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(outQ1);
            oos.flush(); oos.close(); 

            // de-serialize the output queue
            FileInputStream fis = new FileInputStream("OutputQueue.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            OutputQueue outQ2 = (OutputQueue)ois.readObject();
            ois.close(); 

            String system1 = outQ1.getSystem().getSystemName();
            String system2 = outQ2.getSystem().getSystemName();
            String user1 = outQ1.getSystem().getUserId();
            String user2 = outQ2.getSystem().getUserId();

            if( (system1.equals(system2)) && (user1.equals(user2)) )
                {
                succeeded();
                }
            else
                {
                failed("OutputQueue's system did not serialize/de-serialize");
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
                File f = new File("OutputQueue.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var002

    /**
     * Tests that OutputQueue's name serializes/de-serializes.
     **/
    public void Var003()
    {
        try
            {
            // create an output queue object using valid system name and queue name
            OutputQueue outQ1 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/SERTST.OUTQ");

            // serialize the output queue
            FileOutputStream fos = new FileOutputStream("OutputQueue.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(outQ1);
            oos.flush(); oos.close(); 

            // de-serialize the output queue
            FileInputStream fis = new FileInputStream("OutputQueue.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            OutputQueue outQ2 = (OutputQueue)ois.readObject();
            ois.close(); 

            if( outQ1.getName().equals(outQ2.getName()) )
                {
                succeeded();
                }
            else
                {
                failed("OutputQueue's name did not serialize/de-serialize");
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
                File f = new File("OutputQueue.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var003

    /**
     * Tests that OutputQueue's path serializes/de-serializes.
     **/
    public void Var004()
    {
        try
            {
            // create an output queue object using valid system name and queue name
            OutputQueue outQ1 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/SERTST.OUTQ");

            // serialize the output queue
            FileOutputStream fos = new FileOutputStream("OutputQueue.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(outQ1);
            oos.flush(); oos.close(); 

            // de-serialize the output queue
            FileInputStream fis = new FileInputStream("OutputQueue.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            OutputQueue outQ2 = (OutputQueue)ois.readObject();
            ois.close(); 

            if( outQ1.getPath().equals(outQ2.getPath()) )
                {
                succeeded();
                }
            else
                {
                failed("OutputQueue's path did not serialize/de-serialize");
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
                File f = new File("OutputQueue.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var004

    /**
     * Tests that OutputQueue's attributes serializes/de-serializes.
     * We check for one attribute, output queue status.
     **/
    public void Var005()
    {
        try
            {
            // create an output queue object using valid system name and queue name
            OutputQueue outQ1 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/SERTST.OUTQ");

            // force the object to cache the attributes
            outQ1.update();

            // serialize the output queue
            FileOutputStream fos = new FileOutputStream("OutputQueue.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(outQ1);
            oos.flush(); oos.close(); 

            // de-serialize the output queue
            FileInputStream fis = new FileInputStream("OutputQueue.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            OutputQueue outQ2 = (OutputQueue)ois.readObject();
            ois.close(); 

            // get the output queue status from both output queues.                                                    
            String s1 = outQ1.getStringAttribute(PrintObject.ATTR_OUTQSTS);
            String s2 = outQ2.getStringAttribute(PrintObject.ATTR_OUTQSTS);

            // @A1A - with proxification, connection will be made to AS/400...
            // Make sure we have not connected to the AS/400 to get
            // the attributes from the serialized output queue.
            if( /* @A1D !(outQ2.getSystem().isConnected()) && */ (s1.equals(s2)) )
                {
                succeeded();
                }
            else
                {
                failed("OutputQueue's attributes did not serialize/de-serialize");
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
                File f = new File("OutputQueue.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var005

    /**
     * Tests that a de-serialized OutputQueue is functional.
     **/
    public void Var006()
    {
        try
            {
            // create an output queue object using valid system name and queue name
            OutputQueue outQ1 = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/SERTST.OUTQ");

            // serialize the output queue
            FileOutputStream fos = new FileOutputStream("OutputQueue.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(outQ1);
            oos.flush(); oos.close(); 

            // de-serialize the output queue
            FileInputStream fis = new FileInputStream("OutputQueue.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            OutputQueue outQ2 = (OutputQueue)ois.readObject();
            ois.close(); 

            // Connect to AS/400 get attributes. We have to set the
            // password because that is not serialized.
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            outQ2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            outQ2.update();

            succeeded();

            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("OutputQueue.ser");
                f.delete();
                }
            catch(Exception e){}
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

} // end NPOutQSerializeTestcase class






