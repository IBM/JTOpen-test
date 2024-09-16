///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtDListSerializeTestcase.java
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
 Testcase NPPrtDListSerializeTestcase.
 **/
public class NPPrtDListSerializeTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtDListSerializeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtDListSerializeTestcase(AS400            systemObject,
                                       Vector           variationsToRun,
                                       int              runMode,
                                       FileOutputStream fileOutputStream,
                                       
                                       String           password)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPPrtDListSerializeTestcase", 5,
	      variationsToRun, runMode, fileOutputStream,
               password);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtDListSerializeTestcase");
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
     * Tests that PrinterList's can serialize/de-serialize and
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

            // create a printer list object
            PrinterList prtDList1 = new PrinterList(systemObject_);

            // serialize the PrinterList object
            FileOutputStream fos = new FileOutputStream("PrinterList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtDList1);
            oos.flush();

            // de-serialize the PrinterList object
            FileInputStream fis = new FileInputStream("PrinterList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterList prtDList2 = (PrinterList)ois.readObject();

            // add/remove listeners
            prtDList2.addPropertyChangeListener(propertyListener);
            prtDList2.removePropertyChangeListener(propertyListener);

            prtDList2.addVetoableChangeListener(vetoableListener);
            prtDList2.removeVetoableChangeListener(vetoableListener);

            prtDList2.addPrintObjectListListener(listListener);
            prtDList2.removePrintObjectListListener(listListener);

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
                File f = new File("PrinterList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var001

    /**
     * Tests that PrinterList's system serializes/de-serializes.
     **/
    public void Var002()
    {
        try
            {
            // create an printer list object using valid system name
            PrinterList prtDList1 = new PrinterList(systemObject_);

            // serialize the PrinterList object
            FileOutputStream fos = new FileOutputStream("PrinterList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtDList1);
            oos.flush();

            // de-serialize the PrinterList object
            FileInputStream fis = new FileInputStream("PrinterList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterList prtDList2 = (PrinterList)ois.readObject();

            String system1 = prtDList1.getSystem().getSystemName();
            String system2 = prtDList2.getSystem().getSystemName();
            String user1 = prtDList1.getSystem().getUserId();
            String user2 = prtDList2.getSystem().getUserId();

            if( (system1.equals(system2)) && (user1.equals(user2)) )
                {
                succeeded();
                }
            else
                {
                failed("PrinterList's system did not serialize/de-serialize");
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
                File f = new File("PrinterList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var002

    /**
     * Tests that PrinterList's printerFilter can serialize/de-serialize.
     **/
    public void Var003()
    {
        try
            {
            // create a printer list object
            PrinterList prtDList1 = new PrinterList(systemObject_);

            // Set the printerFilter
            prtDList1.setPrinterFilter("JAVAPRINT");

            // serialize the PrinterList object
            FileOutputStream fos = new FileOutputStream("PrinterList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtDList1);
            oos.flush();

            // de-serialize the PrinterList object
            FileInputStream fis = new FileInputStream("PrinterList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterList prtDList2 = (PrinterList)ois.readObject();

            if( prtDList1.getPrinterFilter().equals(prtDList2.getPrinterFilter()) )
                {
                succeeded();
                }
            else
                {
                failed("PrinterList printerFilter did not serialize/de-serialize");
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
                File f = new File("PrinterList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var003

    /**
     * Tests that PrinterList's set attributes to retrieve
     * can serialize/de-serialize.
     **/
    public void Var004()
    {
	try
            {
            // create a printer list object
            PrinterList prtDList1 = new PrinterList(systemObject_);

            // set the printerFilter
            prtDList1.setPrinterFilter("JAVAPRINT");

            // set the attributes to retrieve, only ask for device type.
            int[] attrs = { PrintObject.ATTR_DEVTYPE };
            prtDList1.setAttributesToRetrieve(attrs);

            // serialize the PrinterList object
            FileOutputStream fos = new FileOutputStream("PrinterList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtDList1);
            oos.flush();

            // de-serialize the PrinterList object
            FileInputStream fis = new FileInputStream("PrinterList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterList prtDList2 = (PrinterList)ois.readObject();

            // now build printer list synchronously, we have
            // to set the password because that is not serialized.
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            prtDList2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            prtDList2.openSynchronously();

            boolean fSuccess = true;

            Printer prtD = (Printer)prtDList2.getObject(0);

            // disconnect the system, if we try to access the
            // AS400 we will get connected again.
            prtD.getSystem().disconnectService(AS400.PRINT);

            // ATTR_DEVTYPE should be cached in the Printer
            prtD.getStringAttribute(PrintObject.ATTR_DEVTYPE);
            if( prtD.getSystem().isConnected() )
                {
                fSuccess = false;
                }

            // ATTR_DEVMODEL should NOT be cached in the Printer
            prtD.getStringAttribute(PrintObject.ATTR_DEVMODEL);
            if( !(prtD.getSystem().isConnected()) )
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

            prtDList2.close();
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
                File f = new File("PrinterList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var004

    /**
     * Tests that a de-serialized PrinterList is functional.
     **/
    public void Var005()
    {
	try
            {
            // create a printer list object
            PrinterList prtDList1 = new PrinterList(systemObject_);

            // set the printerFilter
            prtDList1.setPrinterFilter("JAVAPRINT");

            // open the list and serialize in this state to make sure
            // that transient variables are not de-serialized.
            prtDList1.openSynchronously();

            // serialize the PrinterList object
            FileOutputStream fos = new FileOutputStream("PrinterList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtDList1);
            oos.flush();

            // de-serialize the PrinterList object
            FileInputStream fis = new FileInputStream("PrinterList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterList prtDList2 = (PrinterList)ois.readObject();

            // after de-serializing set the password
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            prtDList2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

            // use the de-serialized object 
            prtDList2.openSynchronously();

            // retrieve the first printer
            Printer prtD = (Printer)prtDList2.getObject(0);

            // check to see if we got one printer and verify name
            if ( (prtDList2.size() == 1) &&
                 (prtD.getName().trim().equals("JAVAPRINT")) )
                {
                // indicate how many printers were listed
                output_.println(" " + prtDList1.size() + " Printer(s) listed.");

                succeeded();
                }
            else
                {
                failed("De-serialized PrinterList could not be used");
                }

            prtDList1.close();
            prtDList2.close();
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
                File f = new File("PrinterList.ser");
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

} // end NPPrtDListSerializeTestcase class


