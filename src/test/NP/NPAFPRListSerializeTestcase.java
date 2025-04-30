///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPAFPRListSerializeTestcase.java
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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Vector;

import com.ibm.as400.access.AFPResource;
import com.ibm.as400.access.AFPResourceList;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.PrintObjectListEvent;
import com.ibm.as400.access.PrintObjectListListener;
import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.access.SpooledFileOutputStream;

import test.PasswordVault;
import test.Testcase;

/**
 Testcase NPAFPRListSerializeTestcase.
 **/
public class NPAFPRListSerializeTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPAFPRListSerializeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPAFPRListSerializeTestcase(AS400            systemObject,
                                       Vector<String> variationsToRun,
                                       int              runMode,
                                       FileOutputStream fileOutputStream,
                                       
                                       String           password)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPAFPRListSerializeTestcase", 6,
	      variationsToRun, runMode, fileOutputStream,
               password);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPAFPRListSerializeTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
	{
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

	} // end try block

	catch( Exception e )
	{
	    failed(e, "Unexpected exception");
	}

    } // end run method

    /**
     * Tests that AFPResourceList's can serialize/de-serialize and
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

            // create an AFP Resource List object using valid system name 
            AFPResourceList resList1 = new AFPResourceList(systemObject_);

            // serialize the AFPResourceList object
            FileOutputStream fos = new FileOutputStream("AFPResourceList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(resList1);
            oos.flush();
            oos.close(); 
            
            // de-serialize the AFPResourceList object
            FileInputStream fis = new FileInputStream("AFPResourceList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            AFPResourceList resList2 = (AFPResourceList)ois.readObject();
            ois.close(); 

            // add/remove listeners
            resList2.addPropertyChangeListener(propertyListener);
            resList2.removePropertyChangeListener(propertyListener);

            resList2.addVetoableChangeListener(vetoableListener);
            resList2.removeVetoableChangeListener(vetoableListener);

            resList2.addPrintObjectListListener(listListener);
            resList2.removePrintObjectListListener(listListener);

            succeeded();
            } 

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("AFPResourceList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var001

    /**
     * Tests that AFPResourceList's system serializes/de-serializes.
     **/
    public void Var002()
    {
        try
            {
            // create an AFP resource list object using valid system name
            AFPResourceList resList1 = new AFPResourceList(systemObject_);

            // serialize the AFPResourceList object
            FileOutputStream fos = new FileOutputStream("AFPResourceList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(resList1);
            oos.flush();
            oos.close(); 

            // de-serialize the AFPResourceList object
            FileInputStream fis = new FileInputStream("AFPResourceList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            AFPResourceList resList2 = (AFPResourceList)ois.readObject();
            ois.close(); 

            String system1 = resList1.getSystem().getSystemName();
            String system2 = resList2.getSystem().getSystemName();
            String user1 = resList1.getSystem().getUserId();
            String user2 = resList2.getSystem().getUserId();

            if( (system1.equals(system2)) && (user1.equals(user2)) )
                {
                succeeded();
                }
            else
                {
                failed("AFPResourceList's system did not serialize/de-serialize");
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
                File f = new File("AFPResourceList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var002

    /**
     * Tests that AFPResourceList's resourceFilter can serialize/de-serialize.
     **/
    public void Var003()
    {
        try
            {
            // create a AFP resource list object
            AFPResourceList resList1 = new AFPResourceList(systemObject_);

            // Set the resourceFilter
            resList1.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");
            
            // serialize the AFPResourceList object
            FileOutputStream fos = new FileOutputStream("AFPResourceList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(resList1);
            oos.flush();
            oos.close(); 

            // de-serialize the AFPResourceList object
            FileInputStream fis = new FileInputStream("AFPResourceList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            AFPResourceList resList2 = (AFPResourceList)ois.readObject();
            ois.close(); 

            if( resList1.getResourceFilter().equals(resList2.getResourceFilter()) )
                {
                succeeded();
                }
            else
                {
                failed("AFPResourceList resourceFilter did not serialize/de-serialize");
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
                File f = new File("AFPResourceList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var003

    /**
     * Tests that AFPResourceList's spooledFileFilter can serialize/de-serialize.
     **/
    public void Var004()
    {
        try
            {
            // create a AFP resource list object
            AFPResourceList resList1 = new AFPResourceList(systemObject_);

            // Set the spooledFileFilter
            SpooledFile splF = createSpooledFile();
            resList1.setSpooledFileFilter( splF );
            
            // serialize the AFPResourceList object
            FileOutputStream fos = new FileOutputStream("AFPResourceList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(resList1);
            oos.flush();
            oos.close(); 

            // de-serialize the AFPResourceList object
            FileInputStream fis = new FileInputStream("AFPResourceList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            AFPResourceList resList2 = (AFPResourceList)ois.readObject();
            ois.close(); 

            String jobNumber1 = resList1.getSpooledFileFilter().getJobNumber();
            String jobNumber2 = resList2.getSpooledFileFilter().getJobNumber();
            String name1 = resList1.getSpooledFileFilter().getName();
            String name2 = resList1.getSpooledFileFilter().getName();

            if( (jobNumber1.equals(jobNumber2)) && (name1.equals(name2)) )
                {
                succeeded();
                }
            else
                {
                failed("AFPResourceList spooledFileFilter did not serialize/de-serialize");
                }

            // clean up on AS/400
            splF.delete();
            }

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("AFPResourceList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var004

    /**
     * Tests that AFPResourceList's attributes to retrieve
     * serialize/de-serialize.
     **/
    public void Var005()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList1 = new AFPResourceList(systemObject_);

            // set the resource filter to receive all *FNTRSC resources in NPJAVA
            resList1.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.FNTRSC");

            // set the attributes to retrieve, only ask for time
            int[] attrs = { PrintObject.ATTR_TIME };
            resList1.setAttributesToRetrieve(attrs);

            // serialize the AFPResourceList object
            FileOutputStream fos = new FileOutputStream("AFPResourceList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(resList1);
            oos.flush();
            oos.close(); 

            // de-serialize the AFPResourceList object
            FileInputStream fis = new FileInputStream("AFPResourceList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            AFPResourceList resList2 = (AFPResourceList)ois.readObject();
            ois.close(); 

            // now build resource list synchronously, we have
            // to set the password because that is not serialized.
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            resList2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            resList2.openSynchronously();

            // check to see if we got some resources
            if( resList2.size() > 0 )
                {
                boolean fSuccess = true;

                AFPResource res = (AFPResource)resList2.getObject(0);

                // disconnect the system, if we try to access the
                // AS400 we will get connected again.
                res.getSystem().disconnectService(AS400.PRINT);

                // ATTR_TIME should be cached in the AFPResource
                res.getStringAttribute(PrintObject.ATTR_TIME);
                if( res.getSystem().isConnected() )
                    {
                    fSuccess = false;
                    }

                // ATTR_DATE should NOT be cached in the AFPResource
                res.getStringAttribute(PrintObject.ATTR_DATE);
                if( !(res.getSystem().isConnected()) )
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

            }
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("No AFP Resources found in NPJAVA Library.");
                }

            // close the list
            resList2.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("AFPResourceList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var006

    /**
     * Tests that de-serialized AFPResourceList is functional
     **/
    public void Var006()
    {
        try
            {
            // create an AFP Resource List object
            AFPResourceList resList1 = new AFPResourceList(systemObject_);

            // filter by a specific library name
            resList1.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.%ALL%");

            // serialize the AFPResourceList object
            FileOutputStream fos = new FileOutputStream("AFPResourceList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(resList1);
            oos.flush();
            oos.close(); 
            // de-serialize the AFPResourceList object
            FileInputStream fis = new FileInputStream("AFPResourceList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            AFPResourceList resList2 = (AFPResourceList)ois.readObject();
            ois.close(); 
            // now try to build resource list synchronously, we have
            // to set the password because that is not serialized.
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            resList2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            resList2.openSynchronously();

            Enumeration<AFPResource> e = resList2.getObjects();
            String resPath = null;

            // check to see if we got some resources
            if (resList2.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();
                    resPath = res.getPath();
                    if (res.getPath().startsWith("/QSYS.LIB/NPJAVA.LIB"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad resource:"+resPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(resList2.size() + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not list resources by specific library name");
                    }
                } // if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("No AFP Resources found in NPJAVA Library.");
                }

            resList2.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

        finally
            {
            try
                {
                File f = new File("AFPResourceList.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var006

    // This method creates a spooled file
    private SpooledFile createSpooledFile()
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException
    {
        // buffer for data
        byte[] buf = new byte[2048];

        // fill the buffer
        for (int i=0; i<2048; i++) buf[i] = (byte)'9';

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, null);

        // write some data
        outStream.write(buf);

        // close the spooled file
        outStream.close();

        // return the new SpooledFile
        return outStream.getSpooledFile();

    } // end createSpooledFile

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

} // end NPAFPRListSerializeTestcase class


