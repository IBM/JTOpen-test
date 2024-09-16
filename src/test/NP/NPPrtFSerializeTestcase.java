///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtFSerializeTestcase.java
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
import java.util.Enumeration;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import com.ibm.as400.access.*;

import test.PasswordVault;
import test.Testcase;

/**
 * Testcase NPPrtFSerializeTestcase.
 **/
public class NPPrtFSerializeTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtFSerializeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     * Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtFSerializeTestcase(AS400            systemObject,
                                   Vector           variationsToRun,
                                   int              runMode,
                                   FileOutputStream fileOutputStream,
                                   
                                   String           password)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPPrtFSerializeTestcase", 6,
	      variationsToRun, runMode, fileOutputStream,
               password);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtFSerializeTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create JAVAPRINT printer file
            CommandCall cmd = new CommandCall(systemObject_);
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

            // delete the printer file that we created
            if (cmd.run("DLTF FILE(NPJAVA/JAVAPRINT)") == false)
                {
                output_.println("Could not delete the printer file we created. "
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
     * Tests that PrinterFile's can serialize/de-serialize and
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

            // create a printer file object using valid system name and printer file name
            PrinterFile prtF1 = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // serialize the printer file object 
            FileOutputStream fos = new FileOutputStream("PrinterFile.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtF1);
            oos.flush();

            // de-serialize the printer file object
            FileInputStream fis = new FileInputStream("PrinterFile.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterFile prtF2 = (PrinterFile)ois.readObject();

            // add/remove listeners
            prtF2.addPropertyChangeListener(propertyListener);
            prtF2.removePropertyChangeListener(propertyListener);

            prtF2.addVetoableChangeListener(vetoableListener);
            prtF2.removeVetoableChangeListener(vetoableListener);

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
                File f = new File("PrinterFile.ser");
                f.delete();
                }
            catch(Exception e){}
            }


    } // end Var001

    /**
     * Tests that PrinterFile's system serializes/de-serializes.
     **/
    public void Var002()
    {
        try
            {
            // create an printer file object using valid system name and queue name
            PrinterFile prtF1 = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // serialize the printer file
            FileOutputStream fos = new FileOutputStream("PrinterFile.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtF1);
            oos.flush();

            // de-serialize the printer file
            FileInputStream fis = new FileInputStream("PrinterFile.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterFile prtF2 = (PrinterFile)ois.readObject();

            String system1 = prtF1.getSystem().getSystemName();
            String system2 = prtF2.getSystem().getSystemName();
            String user1 = prtF1.getSystem().getUserId();
            String user2 = prtF2.getSystem().getUserId();

            if( (system1.equals(system2)) && (user1.equals(user2)) )
                {
                succeeded();
                }
            else
                {
                failed("PrinterFile's system did not serialize/de-serialize");
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
                File f = new File("PrinterFile.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var002

    /**
     * Tests that PrinterFile's name serializes/de-serializes.
     **/
    public void Var003()
    {
        try
            {
            // create an printer file object using valid system name and queue name
            PrinterFile prtF1 = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // serialize the printer file
            FileOutputStream fos = new FileOutputStream("PrinterFile.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtF1);
            oos.flush();

            // de-serialize the printer file
            FileInputStream fis = new FileInputStream("PrinterFile.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterFile prtF2 = (PrinterFile)ois.readObject();

            if( prtF1.getName().equals(prtF2.getName()) )
                {
                succeeded();
                }
            else
                {
                failed("PrinterFile's name did not serialize/de-serialize");
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
                File f = new File("PrinterFile.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var003

    /**
     * Tests that PrinterFile's path serializes/de-serializes.
     **/
    public void Var004()
    {
        try
            {
            // create an printer file object using valid system name and queue name
            PrinterFile prtF1 = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // serialize the printer file
            FileOutputStream fos = new FileOutputStream("PrinterFile.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtF1);
            oos.flush();

            // de-serialize the printer file
            FileInputStream fis = new FileInputStream("PrinterFile.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterFile prtF2 = (PrinterFile)ois.readObject();

            if( prtF1.getPath().equals(prtF2.getPath()) )
                {
                succeeded();
                }
            else
                {
                failed("PrinterFile's path did not serialize/de-serialize");
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
                File f = new File("PrinterFile.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var004

    /**
     * Tests that PrinterFile's attributes serializes/de-serializes.
     * We check for one attribute, printer file status.
     **/
    public void Var005()
    {
        try
            {
            // create an printer file object using valid system name and queue name
            PrinterFile prtF1 = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // force the object to cache the attributes
            prtF1.update();

            // serialize the printer file
            FileOutputStream fos = new FileOutputStream("PrinterFile.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtF1);
            oos.flush();

            // de-serialize the printer file
            FileInputStream fis = new FileInputStream("PrinterFile.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterFile prtF2 = (PrinterFile)ois.readObject();

            // get the printer name from both printer files.
            String s1 = prtF1.getStringAttribute(PrintObject.ATTR_PRINTER);
            String s2 = prtF2.getStringAttribute(PrintObject.ATTR_PRINTER);

            // @A1A - with proxification, connection will be made to AS/400...
            // Make sure we have not connected to the AS/400 to get
            // the attributes from the serialized printer file.
            if( /* @A1D !(prtF2.getSystem().isConnected()) &&  */(s1.equals(s2)) )
                {
                succeeded();
                }
            else
                {
                failed("PrinterFile's attributes did not serialize/de-serialize");
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
                File f = new File("PrinterFile.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var005

    /**
     * Tests that a de-serialized PrinterFile is functional.
     **/
    public void Var006()
    {
        try
            {
            // create an printer file object using valid system name and queue name
            PrinterFile prtF1 = new PrinterFile(systemObject_, "/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // serialize the printer file
            FileOutputStream fos = new FileOutputStream("PrinterFile.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtF1);
            oos.flush();

            // de-serialize the printer file
            FileInputStream fis = new FileInputStream("PrinterFile.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterFile prtF2 = (PrinterFile)ois.readObject();

            // Connect to AS/400 get attributes. We have to set the
            // password because that is not serialized.
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            prtF2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            prtF2.update();

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
                File f = new File("PrinterFile.ser");
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

} // end NPPrtFSerializeTestcase class



