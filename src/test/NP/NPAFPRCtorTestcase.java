///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPAFPRCtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AFPResource;
import com.ibm.as400.access.AS400;

import test.Testcase;

/**
 * Testcase NPAFPRCtorTestcase.
 **/
public class NPAFPRCtorTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPAFPRCtorTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPAFPRCtorTestcase(AS400            systemObject,
                       Vector<String>           variationsToRun,
                       int              runMode,
                       FileOutputStream fileOutputStream)
    {
     // $$$ TO DO $$$
     // Replace the third parameter with the total number of variations
     // in this testcase.
     super(systemObject, "NPAFPRCtorTestcase", 10,
           variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPAFPRCtorTestcase");
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

            // run variation 9
            if ((allVariations || variationsToRun_.contains("9")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(9);
                Var009();
                }

            // run variation 10
            if ((allVariations || variationsToRun_.contains("10")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
                {
                setVariation(10);
                Var010();
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
     * Tests construction of an AFP Resource object with good FNTRSC parameters.
     **/
    public void Var001()
    {
        try
            {
            // create an AFP Resource object using valid system name and resource name
            AFPResource res = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            // call a method to ensure we have a good object
            res.update();

            succeeded();  // Note: This variation will be successful.
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

    } // end Var001

    /**
     * Tests construction of an AFP Resource object with good FORMDF parameters.
     **/
    public void  Var002()
    {
     try
         {
         // create an AFP Resource object using valid system name and resource name
         AFPResource res = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/F1A000E0.FORMDF");

         // call a method to ensure we have a good object
         res.update();

         succeeded();  // Note: This variation will be successful.
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

    } // end Var002

    /**
     * Tests construction of an AFP Resource object with good OVL parameters.
     **/
    public void Var003()
    {
        try
            {
            // create an AFP Resource object using valid system name and resource name
            AFPResource res = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/QOOD2L.OVL");

            // call a method to ensure we have a good object
            res.update();

            succeeded();  // Note: This variation will be successful.
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

    } // end Var003

    /**
     * Tests construction of an AFP Resource object with good PAGSEG parameters.
     **/
    public void Var004()
    {
        try
            {
            // create an AFP Resource object using valid system name and resource name
            AFPResource res = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/QFCPAGS.PAGSEG");

            // call a method to ensure we have a good object
            res.update();

            succeeded();  // Note: This variation will be successful.
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

    } // end Var004

    /**
     * Tests construction of an AFP Resource object with good PAGDFN parameters.
     **/
    public void Var005()
    {
        try
            {
            // create an AFP Resource object using valid system name and resource name
            AFPResource res = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/P1A06462.PAGDFN");

            // call a method to ensure we have a good object
            res.update();

            succeeded();  // Note: This variation will be successful.
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

    } // end Var005

    /**
     * Tests construction of an AFP Resource object with null system parameter.
     *      A NullPointerException should be thrown.
     **/
    public void Var006()
    {
        try
            {
            // create an AFP Resource object using null system name and valid resource name
            AFPResource res = new AFPResource(null, "/QSYS.LIB/NPJAVA.LIB/P1A06462.PAGDFN");

            failed("Could use null system name."+res);

            } // end try block

        catch (Exception e)
            {
            // Check for NullPointerException
            if (exceptionIs(e, "NullPointerException"))  succeeded();
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            else if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var006

    /**
     * Tests construction of an AFP Resource object with null resource parameter.
     *      A NullPointerException should be thrown.
     **/
    public void Var007()
    {
        try
            {
            // create an AFP Resource object using valid system name and null resource name
            AFPResource res = new AFPResource(systemObject_, null);

            failed("Could use null resource name."+res);

            } // end try block

        catch (Exception e)
            {
            // Check for NullPointerException
            if (exceptionIs(e, "NullPointerException"))  succeeded();
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            else if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var007

    /**
     * Tests construction of an AFP Resource object with invalid resource parameter.
     *      A IllegalPathNameException should be thrown.
     **/
    public void Var008()
    {
        try
            {
            // create an AFP Resource object using valid system name and resource name
            AFPResource res = new AFPResource(systemObject_, "ThisIsAnInvalidName");

            failed("Could use invalid resource name."+res);

            } // end try block

        catch (Exception e)
            {
            // Check for IllegalPathNameException
            if (exceptionIs(e, "IllegalPathNameException"))  succeeded();
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            else if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var008

    /**
     * Tests construction of AFPResource object with no parameters. This
     * default constructor is provide for visual application builders
     * that support JavaBeans.
     **/
    public void Var009()
    {
        try
            {
            // create an AFP Resource object using no parameters
            AFPResource res = new AFPResource();

            assertCondition(true,"res="+res);  // Note: This variation will be successful.
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

    } // end Var009

    /**
     * Tests construction of an AFP Resource object with invalid resource type parameter.
     *      A IllegalPathNameException should be thrown.
     **/
    public void Var010()
    {
        try
            {
            // create an AFP Resource object using valid system name and resource name
            AFPResource res = new AFPResource(systemObject_, "/QSYS.LIB/NPJAVA.LIB/P1A06462.OUTQ");

            failed("Could use invalid resource type name."+res);

            } // end try block

        catch (Exception e)
            {
            // Check for IllegalPathNameException
            if (exceptionIs(e, "IllegalPathNameException"))  succeeded();
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            else if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var010

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

} // end NPAFPRCtorTestcase class


