///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtDSerializeTestcase.java
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
import java.util.Enumeration;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import com.ibm.as400.access.*;

/**
 * Testcase NPPrtDSerializeTestcase.
 **/
public class NPPrtDSerializeTestcase extends Testcase
{
    /**
     * Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtDSerializeTestcase(AS400            systemObject,
                                   Vector           variationsToRun,
                                   int              runMode,
                                   FileOutputStream fileOutputStream,
                                   
                                   String           password)
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPPrtDSerializeTestcase", 5,
	      variationsToRun, runMode, fileOutputStream,
               password);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtDSerializeTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // make sure JAVAPRINT printer device description exists
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("CRTDEVPRT DEVD(JAVAPRINT) DEVCLS(*LCL) TYPE(*IPDS) MODEL(0) PORT(5) SWTSET(0) ONLINE(*NO) FONT(011)") == false)
                {
                failed("Could not create a printer device description. "
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
                runMode_ != UNATTENDED) // Note: This is an attended variation.  // @A1C
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

            // delete the printer device description that we created
            if (cmd.run("DLTDEVD JAVAPRINT") == false)
                {
                output_.println("Could not delete the printer device description we created. "
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
     * Tests than Printer's serialize/de-serialize and
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

            // create an printer object using valid system name and printer name
            Printer prtD1 = new Printer(systemObject_, "JAVAPRINT");

            // serialize the printer object
            FileOutputStream fos = new FileOutputStream("Printer.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtD1);
            oos.flush();

            // de-serialize the printer object
            FileInputStream fis = new FileInputStream("Printer.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Printer prtD2 = (Printer)ois.readObject();

            // add/remove listeners
            prtD2.addPropertyChangeListener(propertyListener);
            prtD2.removePropertyChangeListener(propertyListener);

            prtD2.addVetoableChangeListener(vetoableListener);
            prtD2.removeVetoableChangeListener(vetoableListener);

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
                File f = new File("Printer.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var001

    /**
     * Tests that Printer's system serializes/de-serializes.
     **/
    public void Var002()
    {
        try
            {
            // create an printer  object using valid system name and printer name
            Printer prtD1 = new Printer(systemObject_, "JAVAPRINT");

            // serialize the printer 
            FileOutputStream fos = new FileOutputStream("Printer.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtD1);
            oos.flush();

            // de-serialize the printer 
            FileInputStream fis = new FileInputStream("Printer.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Printer prtD2 = (Printer)ois.readObject();

            String system1 = prtD1.getSystem().getSystemName();
            String system2 = prtD2.getSystem().getSystemName();
            String user1 = prtD1.getSystem().getUserId();
            String user2 = prtD2.getSystem().getUserId();

            if( (system1.equals(system2)) && (user1.equals(user2)) )
                {
                succeeded();
                }
            else
                {
                failed("Printer's system did not serialize/de-serialize");
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
                File f = new File("Printer.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var002

    /**
     * Tests that Printer's name serializes/de-serializes.
     **/
    public void Var003()
    {
        try
            {
            // create an printer  object using valid system name and printer name
            Printer prtD1 = new Printer(systemObject_, "JAVAPRINT");

            // serialize the printer 
            FileOutputStream fos = new FileOutputStream("Printer.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtD1);
            oos.flush();

            // de-serialize the printer 
            FileInputStream fis = new FileInputStream("Printer.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Printer prtD2 = (Printer)ois.readObject();

            if( prtD1.getName().equals(prtD2.getName()) )
                {
                succeeded();
                }
            else
                {
                failed("Printer's name did not serialize/de-serialize");
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
                File f = new File("Printer.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var003

    /**
     * Tests that Printer's attributes serializes/de-serializes.
     * We check for one attribute, printer  status.
     **/
    public void Var004()
    {
        try
            {
            // create an printer  object using valid system name and printer name
            Printer prtD1 = new Printer(systemObject_, "JAVAPRINT");

            // force the object to cache the attributes
            prtD1.update();

            // serialize the printer 
            FileOutputStream fos = new FileOutputStream("Printer.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtD1);
            oos.flush();

            // de-serialize the printer 
            FileInputStream fis = new FileInputStream("Printer.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Printer prtD2 = (Printer)ois.readObject();

            // get the printer name from both printers.
            String s1 = prtD1.getStringAttribute(PrintObject.ATTR_PRINTER);
            String s2 = prtD2.getStringAttribute(PrintObject.ATTR_PRINTER);

            // @A1A - with proxification, connection will be made to AS/400...
            // Make sure we have not connected to the AS/400 to get
            // the attributes from the serialized printer.
            if(/* @A1D !(prtD2.getSystem().isConnected()) && */(s1.equals(s2)) )
                {
                succeeded();
                }
            else
                {
                failed("Printer's attributes did not serialize/de-serialize");
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
                File f = new File("Printer.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var004

    /**
     * Tests that a de-serialized Printer is functional.
     **/
    public void Var005()
    {
        try
            {
            // create an printer  object using valid system name and     printer name
            Printer prtD1 = new Printer(systemObject_, "JAVAPRINT");

            // serialize the printer
            FileOutputStream fos = new FileOutputStream("Printer.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtD1);
            oos.flush();

            // de-serialize the printer
            FileInputStream fis = new FileInputStream("Printer.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Printer prtD2 = (Printer)ois.readObject();

            // Connect to AS/400 get attributes. We have to set the
            // password because that is not serialized.
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            prtD2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            prtD2.update();

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
                File f = new File("Printer.ser");
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

} // end NPPrtDSerializeTestcase class


