///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtFListSerializeTestcase.java
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
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.PrintObjectListEvent;
import com.ibm.as400.access.PrintObjectListListener;
import com.ibm.as400.access.PrinterFile;
import com.ibm.as400.access.PrinterFileList;

import test.PasswordVault;
import test.Testcase;

/**
 Testcase NPPrtFListSerializeTestcase.
 **/
public class NPPrtFListSerializeTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtFListSerializeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPPrtFListSerializeTestcase(AS400            systemObject,
                                       Vector<String> variationsToRun,
                                       int              runMode,
                                       FileOutputStream fileOutputStream,
                                       
                                       String           password)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPPrtFListSerializeTestcase", 5,
	      variationsToRun, runMode, fileOutputStream,
               password);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPPrtFListSerializeTestcase");
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
     * Tests that PrinterFileList's can serialize/de-serialize and
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
            
            // create a printer file list object
            PrinterFileList prtFList1 = new PrinterFileList(systemObject_);

            // serialize the PrinterFileList object
            FileOutputStream fos = new FileOutputStream("PrinterFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtFList1);
            oos.flush(); oos.close(); 

            // de-serialize the PrinterFileList object
            FileInputStream fis = new FileInputStream("PrinterFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterFileList prtFList2 = (PrinterFileList)ois.readObject();
            ois.close(); 
            
            // add/remove listeners
            prtFList2.addPropertyChangeListener(propertyListener);
            prtFList2.removePropertyChangeListener(propertyListener);

            prtFList2.addVetoableChangeListener(vetoableListener);
            prtFList2.removeVetoableChangeListener(vetoableListener);

            prtFList2.addPrintObjectListListener(listListener);
            prtFList2.removePrintObjectListListener(listListener);

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
                File f = new File("PrinterFileList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var001

    /**
     * Tests that PrinterFileList's system serializes/de-serializes.
     **/
    public void Var002()
    {
        try
            {
            // create an printer file list object using valid system name
            PrinterFileList prtFList1 = new PrinterFileList(systemObject_);

            // serialize the PrinterFileList object
            FileOutputStream fos = new FileOutputStream("PrinterFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtFList1);
            oos.flush(); oos.close(); 

            // de-serialize the PrinterFileList object
            FileInputStream fis = new FileInputStream("PrinterFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterFileList prtFList2 = (PrinterFileList)ois.readObject();
            ois.close(); 

            String system1 = prtFList1.getSystem().getSystemName();
            String system2 = prtFList2.getSystem().getSystemName();
            String user1 = prtFList1.getSystem().getUserId();
            String user2 = prtFList2.getSystem().getUserId();

            if( (system1.equals(system2)) && (user1.equals(user2)) )
                {
                succeeded();
                }
            else
                {
                failed("PrinterFileList's system did not serialize/de-serialize");
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
                File f = new File("PrinterFileList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var002

    /**
     * Tests that PrinterFileList's printerFileFilter can serialize/de-serialize.
     **/
    public void Var003()
    {
        try
            {
            // create a printer file list object
            PrinterFileList prtFList1 = new PrinterFileList(systemObject_);

            // set the printerFileFilter
            prtFList1.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // serialize the PrinterFileList object
            FileOutputStream fos = new FileOutputStream("PrinterFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtFList1);
            oos.flush(); oos.close(); 

            // de-serialize the PrinterFileList object
            FileInputStream fis = new FileInputStream("PrinterFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterFileList prtFList2 = (PrinterFileList)ois.readObject();
            ois.close(); 

            if( prtFList1.getPrinterFileFilter().equals(prtFList2.getPrinterFileFilter()) )
                {
                succeeded();
                }
            else
                {
                failed("PrinterFileList printerFileFilter did not serialize/de-serialize");
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
                File f = new File("PrinterFileList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var003

    /**
     * Tests that PrinterFileList's set attributes to retrieve
     * can serialize/de-serialize.
     **/
    public void Var004()
    {
	try
            {
            // create a printer file list object
            PrinterFileList prtFList1 = new PrinterFileList(systemObject_);

            // set the printerFileFilter
            prtFList1.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // set the attributes to retrieve, only ask for description.
            int[] attrs = { PrintObject.ATTR_DESCRIPTION };
            prtFList1.setAttributesToRetrieve(attrs);

            // serialize the PrinterFileList object
            FileOutputStream fos = new FileOutputStream("PrinterFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtFList1);
            oos.flush(); oos.close(); 

            // de-serialize the PrinterFileList object
            FileInputStream fis = new FileInputStream("PrinterFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterFileList prtFList2 = (PrinterFileList)ois.readObject();
            ois.close(); 

            // now build printer file list synchronously, we have
            // to set the password because that is not serialized.
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            prtFList2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            prtFList2.openSynchronously();

            boolean fSuccess = true;

            PrinterFile prtF = (PrinterFile)prtFList2.getObject(0);

            // disconnect the system, if we try to access the
            // AS400 we will get connected again.
            prtF.getSystem().disconnectService(AS400.PRINT);

            // ATTR_DESCRIPTION should be cached in the PrinterFile
            prtF.getStringAttribute(PrintObject.ATTR_DESCRIPTION);
            if( prtF.getSystem().isConnected() )
                {
                fSuccess = false;
                }

            // ATTR_COPIES should NOT be cached in the PrinterFile
            prtF.getIntegerAttribute(PrintObject.ATTR_COPIES);
            if( !(prtF.getSystem().isConnected()) )
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

            prtFList2.close();
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
                File f = new File("PrinterFileList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var004

    /**
     * Tests that a de-serialized PrinterFileList is functional.
     **/
    public void Var005()
    {
	try
            {
            // create a printer file list object
            PrinterFileList prtFList1 = new PrinterFileList(systemObject_);

            // set the printerFileFilter
            prtFList1.setPrinterFileFilter("/QSYS.LIB/NPJAVA.LIB/JAVAPRINT.FILE");

            // open the list and serialize in this state to make sure
            // that transient variables are not de-serialized.
            prtFList1.openSynchronously();

            // serialize the PrinterFileList object
            FileOutputStream fos = new FileOutputStream("PrinterFileList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prtFList1);
            oos.flush(); oos.close(); 

            // de-serialize the PrinterFileList object
            FileInputStream fis = new FileInputStream("PrinterFileList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrinterFileList prtFList2 = (PrinterFileList)ois.readObject();
            ois.close(); 

            // after de-serializing set the password
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            prtFList2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

            // use the de-serialized object 
            prtFList2.openSynchronously();

            // check to see if we got one printer file 
            if ( prtFList2.size() == 1 ) 
                {
                // indicate how many printer files were listed
                output_.println(" " + prtFList2.size() + " Printer File(s) listed.");
                succeeded();
                }
            else
                {
                failed("De-serialized PrinterFileList could not be used");
                }

            prtFList1.close();
            prtFList2.close();
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
                File f = new File("PrinterFileList.ser");
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

} // end NPPrtFListSerializeTestcase class


