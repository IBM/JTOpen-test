///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPWrtJListSerializeTestcase.java
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
 Testcase NPWrtJListSerializeTestcase.
 **/
public class NPWrtJListSerializeTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPWrtJListSerializeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    // the printer device name
    String printer_ = null;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPWrtJListSerializeTestcase(AS400            systemObject,
                                       Vector<String> variationsToRun,
                                       int              runMode,
                                       FileOutputStream fileOutputStream,
                                       
                                       String           printer,
                                       String           password)
      throws IOException
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPWrtJListSerializeTestcase", 6,
	      variationsToRun, runMode, fileOutputStream,
               password);

        if (printer != null)
            {
            // set the printer variable
            printer_ = printer;
            }
        else
            {
            throw new IOException("The -misc flag must be set to a valid printer device name.");
            }
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPWrtJListSerializeTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);
        boolean fWriterEnded = false;

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

            // end the normal writer to printer
            if (cmd.run("ENDWTR WTR("+printer_+") OPTION(*IMMED)") == false)
                {
                // if the error we go back was not no active writer message
                // exit
                if (!cmd.getMessageList()[0].getID().trim().equals("CPF3313"))
                    {
                    failed("Could not end the writer to "+printer_+". "
                           + cmd.getMessageList()[0].getID()
                           + ": " + cmd.getMessageList()[0].getText());
                    return;
                    }
                }

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(3000,0);

            fWriterEnded = true;

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

            // if we stopped a writer then we will restart it.
            if (fWriterEnded == true)
                {
                if (cmd.run("STRPRTWTR DEV("+printer_+")") == false)
                    {
                    output_.println("Could not start writer that we ended. "
                                    + cmd.getMessageList()[0].getID()
                                    + ": " + cmd.getMessageList()[0].getText());
                    }
                }

            } // end try block

        catch( Exception e )
            {
            failed(e, "Unexpected exception");
            }

    } // end run method

    /**
     * Tests that WriterJobList's can serialize/de-serialize and
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

            // create a Writer Job List object
            WriterJobList wrtJList1 = new WriterJobList(systemObject_);

            // serialize the WriterJobList object
            FileOutputStream fos = new FileOutputStream("WriterJobList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(wrtJList1);
            oos.flush();

            // de-serialize the WriterJobList object
            FileInputStream fis = new FileInputStream("WriterJobList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            WriterJobList wrtJList2 = (WriterJobList)ois.readObject();

            // add/remove listeners
            wrtJList2.addPropertyChangeListener(propertyListener);
            wrtJList2.removePropertyChangeListener(propertyListener);

            wrtJList2.addVetoableChangeListener(vetoableListener);
            wrtJList2.removeVetoableChangeListener(vetoableListener);

            wrtJList2.addPrintObjectListListener(listListener);
            wrtJList2.removePrintObjectListListener(listListener);

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
                File f = new File("WriterJobList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var001

    /**
     * Tests that WriterJobList's system serializes/de-serializes.
     **/
    public void Var002()
    {
        try
            {
            // create an writer job list object using valid system name
            WriterJobList wrtJList1 = new WriterJobList(systemObject_);

            // serialize the WriterJobList object
            FileOutputStream fos = new FileOutputStream("WriterJobList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(wrtJList1);
            oos.flush();

            // de-serialize the WriterJobList object
            FileInputStream fis = new FileInputStream("WriterJobList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            WriterJobList wrtJList2 = (WriterJobList)ois.readObject();

            String system1 = wrtJList1.getSystem().getSystemName();
            String system2 = wrtJList2.getSystem().getSystemName();
            String user1 = wrtJList1.getSystem().getUserId();
            String user2 = wrtJList2.getSystem().getUserId();

            if( (system1.equals(system2)) && (user1.equals(user2)) )
                {
                succeeded();
                }
            else
                {
                failed("WriterJobList's system did not serialize/de-serialize");
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
                File f = new File("WriterJobList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var002

    /**
     * Tests that WriterJobList's queueFilter can serialize/de-serialize.
     **/
    public void Var003()
    {
        try
            {
            // create a writer job list object
            WriterJobList wrtJList1 = new WriterJobList(systemObject_);

            // set the queueFilter
            wrtJList1.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LSERTST.OUTQ");

            // serialize the WriterJobList object
            FileOutputStream fos = new FileOutputStream("WriterJobList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(wrtJList1);
            oos.flush();

            // de-serialize the WriterJobList object
            FileInputStream fis = new FileInputStream("WriterJobList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            WriterJobList wrtJList2 = (WriterJobList)ois.readObject();

            if( wrtJList1.getQueueFilter().equals(wrtJList2.getQueueFilter()) )
                {
                succeeded();
                }
            else
                {
                failed("WriterJobList queueFilter did not serialize/de-serialize");
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
                File f = new File("WriterJobList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var003

    /**
     * Tests that WriterJobList's writerFilter can serialize/de-serialize.
     **/
    public void Var004()
    {
        try
            {
            // create a writer job list object
            WriterJobList wrtJList1 = new WriterJobList(systemObject_);

            // set the queueFilter
            wrtJList1.setWriterFilter("JAVAPRINT");

            // serialize the WriterJobList object
            FileOutputStream fos = new FileOutputStream("WriterJobList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(wrtJList1);
            oos.flush();

            // de-serialize the WriterJobList object
            FileInputStream fis = new FileInputStream("WriterJobList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            WriterJobList wrtJList2 = (WriterJobList)ois.readObject();

            if( wrtJList1.getWriterFilter().equals(wrtJList2.getWriterFilter()) )
                {
                succeeded();
                }
            else
                {
                failed("WriterJobList writerFilter did not serialize/de-serialize");
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
                File f = new File("WriterJobList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var004

    /**
     * Tests that WriterJobList's set attributes to retrieve
     * can serialize/de-serialize.
     **/
    public void Var005()
    {
	try
            {
            // start a writer job
            startWriter();

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(3000,0);

            // create a writer job list object
            WriterJobList wrtJList1 = new WriterJobList(systemObject_);

            // set the queueFilter
            wrtJList1.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LSERTST.OUTQ");

            // set the attributes to retrieve, only ask for status.
            int[] attrs = { PrintObject.ATTR_WTRJOBSTS };
            wrtJList1.setAttributesToRetrieve(attrs);

            // serialize the WriterJobList object
            FileOutputStream fos = new FileOutputStream("WriterJobList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(wrtJList1);
            oos.flush();

            // de-serialize the WriterJobList object
            FileInputStream fis = new FileInputStream("WriterJobList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            WriterJobList wrtJList2 = (WriterJobList)ois.readObject();

            // now build writer job list synchronously, we have
            // to set the password because that is not serialized.
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            wrtJList2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            wrtJList2.openSynchronously();

            boolean fSuccess = true;

            WriterJob wrtJ = (WriterJob)wrtJList2.getObject(0);

            // disconnect the system, if we try to access the
            // AS400 we will get connected again.
            wrtJ.getSystem().disconnectService(AS400.PRINT);

            // ATTR_WTRJOBSTS should be cached in the WriterJob
            wrtJ.getStringAttribute(PrintObject.ATTR_WTRJOBSTS);
            if( wrtJ.getSystem().isConnected() )
                {
                fSuccess = false;
                }

            // ATTR_WTRJOBUSER should NOT be cached in the WriterJob
            wrtJ.getStringAttribute(PrintObject.ATTR_WTRJOBUSER);
            if( !(wrtJ.getSystem().isConnected()) )
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

            // end the writer job we started.
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(3000,0);

            wrtJList2.close();
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
                File f = new File("WriterJobList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var005

    /**
     * Tests that a de-serialized WriterJobList is functional.
     **/
    public void Var006()
    {
	try
            {
            // start a writer job
            startWriter();

            // go to sleep for a bit to allow the writer to start
            Thread.sleep(3000,0);

            // create a writer job list object
            WriterJobList wrtJList1 = new WriterJobList(systemObject_);

            // set the queueFilter
            wrtJList1.setQueueFilter("/QSYS.LIB/NPJAVA.LIB/LSERTST.OUTQ");

            // open the list and serialize in this state to make sure
            // that transient variables are not de-serialized.
            wrtJList1.openSynchronously();

            // serialize the WriterJobList object
            FileOutputStream fos = new FileOutputStream("WriterJobList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(wrtJList1);
            oos.flush();

            // de-serialize the WriterJobList object
            FileInputStream fis = new FileInputStream("WriterJobList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            WriterJobList wrtJList2 = (WriterJobList)ois.readObject();

            // after de-serializing set the password
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            wrtJList2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

            // use the de-serialized object 
            wrtJList2.openSynchronously();

            // check to see if we got one writer job
            if ( wrtJList2.size() == 1 ) 
                {
                // indicate how many output queues were listed
                output_.println(" " + wrtJList2.size() + " Writer Job(s) listed.");
                succeeded();
                }
            else
                {
                failed("De-serialized WriterJobList could not be used");
                }

            // end the writer job we started
            WriterJob wrtJ = (WriterJob)wrtJList2.getObject(0);
            wrtJ.end("*IMMED");

            // go to sleep for a bit to allow the writer to stop
            Thread.sleep(3000,0);

            wrtJList1.close();
            wrtJList2.close();
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
                File f = new File("WriterJobList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var006

    // This method starts a writer job
    private WriterJob startWriter()
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException
    {
        // create a printer device object using valid system name and printer name
        Printer prtD = new Printer(systemObject_, printer_);

        // create a print parm list
        PrintParameterList pList = new PrintParameterList();

        // set some parms
        pList.setParameter(PrintObject.ATTR_WTRJOBNAME,"JAVAPRINT");

        // create an output queue object using valid system name and output queue name
        OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/LSERTST.OUTQ");

        // start a writer job
        return WriterJob.start(systemObject_, prtD, pList, outQ);

    } // end startWriter

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

} // end NPWrtJListSerializeTestcase class


