///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPAFPRSerializeTestcase.java
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
 Testcase NPAFPRSerializeTestcase.
 **/
public class NPAFPRSerializeTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPAFPRSerializeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPAFPRSerializeTestcase(AS400            systemObject,
                                   Vector<String> variationsToRun,
                                   int              runMode,
                                   FileOutputStream fileOutputStream,
                                   
                                   String           password)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPAFPRSerializeTestcase", 6,
	      variationsToRun, runMode, fileOutputStream,
               password);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPAFPRSerializeTestcase");
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
     * Tests that AFPResource's serialize/de-serialize and
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

            // create an AFP Resource object using valid system name and resource name
            AFPResource res1 = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            // serialize the AFP Resource object
            FileOutputStream fos = new FileOutputStream("AFPResource.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(res1);
            oos.flush();

            // de-serialize the AFPResource object
            FileInputStream fis = new FileInputStream("AFPResource.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            AFPResource res2 = (AFPResource)ois.readObject();

            // add/remove listeners
            res2.addPropertyChangeListener(propertyListener);
            res2.removePropertyChangeListener(propertyListener);

            res2.addVetoableChangeListener(vetoableListener);
            res2.removeVetoableChangeListener(vetoableListener);

            succeeded();

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
                File f = new File("AFPResource.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var001

    /**
     * Tests that AFPResource's system serializes/de-serializes.
     **/
    public void Var002()
    {
        try
            {
            // create an AFP Resource object using valid system name and resource name
            AFPResource res1 = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            // serialize the AFP Resource
            FileOutputStream fos = new FileOutputStream("AFPResource.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(res1);
            oos.flush();

            // de-serialize the AFP Resource
            FileInputStream fis = new FileInputStream("AFPResource.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            AFPResource res2 = (AFPResource)ois.readObject();

            String system1 = res1.getSystem().getSystemName();
            String system2 = res2.getSystem().getSystemName();
            String user1 = res1.getSystem().getUserId();
            String user2 = res2.getSystem().getUserId();

            if( (system1.equals(system2)) && (user1.equals(user2)) )
                {
                succeeded();
                }
            else
                {
                failed("AFPResource's system did not serialize/de-serialize");
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
                File f = new File("AFPResource.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var002

    /**
     * Tests that AFPResource's name serializes/de-serializes.
     **/
    public void Var003()
    {
        try
            {
            // create an AFP Resource object using valid system name and resource name
            AFPResource res1 = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            // serialize the AFP Resource
            FileOutputStream fos = new FileOutputStream("AFPResource.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(res1);
            oos.flush();

            // de-serialize the AFP Resource
            FileInputStream fis = new FileInputStream("AFPResource.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            AFPResource res2 = (AFPResource)ois.readObject();

            if( res1.getName().equals(res2.getName()) )
                {
                succeeded();
                }
            else
                {
                failed("AFPResource's name did not serialize/de-serialize");
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
                File f = new File("AFPResource.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var003

    /**
     * Tests that AFPResource's path serializes/de-serializes.
     **/
    public void Var004()
    {
        try
            {
            // create an AFP Resource object using valid system name and resource name
            AFPResource res1 = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            // serialize the AFP Resource
            FileOutputStream fos = new FileOutputStream("AFPResource.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(res1);
            oos.flush();

            // de-serialize the AFP Resource
            FileInputStream fis = new FileInputStream("AFPResource.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            AFPResource res2 = (AFPResource)ois.readObject();

            if( res1.getPath().equals(res2.getPath()) )
                {
                succeeded();
                }
            else
                {
                failed("AFPResource's path did not serialize/de-serialize");
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
                File f = new File("AFPResource.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var004

    /**
     * Tests that AFPResource's attributes serializes/de-serializes.
     * We check for one attribute, date.
     **/
    public void Var005()
    {
        try
            {
            // create an AFP Resource object using valid system name and resource name
            AFPResource res1 = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            // force the object to cache the attributes
            res1.update();

            // serialize the AFP Resource
            FileOutputStream fos = new FileOutputStream("AFPResource.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(res1);
            oos.flush();

            // de-serialize the AFP Resource
            FileInputStream fis = new FileInputStream("AFPResource.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            AFPResource res2 = (AFPResource)ois.readObject();

            // get the date from both AFP resource objects.
            String s1 = res1.getStringAttribute(PrintObject.ATTR_DATE);
            String s2 = res2.getStringAttribute(PrintObject.ATTR_DATE);

            // @A1A - with proxification, connection will be made to AS/400...
            // Make sure we have not connected to the AS/400 to get
            // the attributes from the serialized AFP resource
            if( /* @A1D !(res2.getSystem().isConnected()) &&  */(s1.equals(s2)) )
                {
                succeeded();
                }
            else
                {
                failed("AFPResource's attributes did not serialize/de-serialize");
                }

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
            else
                {
                failed(e, "Unexpected exception");
                }
            }

        finally
            {
            try
                {
                File f = new File("AFPResource.ser");
                f.delete();
                }
            catch(Exception e){}
            }

    } // end Var005

    /**
     * Tests that a de-serialized AFPResource is functional.
     **/
    public void Var006()
    {
        try
            {
            // create an AFP Resource object using valid system name and resource name
            AFPResource res1 = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            // serialize the AFP resource
            FileOutputStream fos = new FileOutputStream("AFPResource.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(res1);
            oos.flush();

            // de-serialize the AFP resource
            FileInputStream fis = new FileInputStream("AFPResource.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            AFPResource res2 = (AFPResource)ois.readObject();

            // Connect to AS/400 get attributes. We have to set the
            // password because that is not serialized.
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            res2.getSystem().setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            res2.update();

            succeeded();

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
            else
                {
                failed(e, "Unexpected exception");
                }
            }

        finally
            {
            try
                {
                File f = new File("AFPResource.ser");
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

} // end NPAFPRSerializeTestcase class


