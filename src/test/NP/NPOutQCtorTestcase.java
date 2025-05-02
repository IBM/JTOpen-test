///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPOutQCtorTestcase.java
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

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.OutputQueue;

import test.Testcase;

/**
 * Testcase NPOutQCtorTestcase.
 * Tests the constructor on OutputQueue class.
 **/
public class NPOutQCtorTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPOutQCtorTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     * Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPOutQCtorTestcase(AS400            systemObject,
                     Vector<String> variationsToRun,
                     int              runMode,
                     FileOutputStream fileOutputStream)
    {
     // $$$ TO DO $$$
     // Replace the third parameter with the total number of variations
     // in this testcase.
     super(systemObject, "NPOutQCtorTestcase", 5,
           variationsToRun, runMode, fileOutputStream);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPOutQCtorTestcase");
        boolean allVariations = (variationsToRun_.size() == 0);

        try
            {
            // create CTORTST output queue
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("QSYS/CRTOUTQ OUTQ(NPJAVA/CTORTST) AUTCHK(*DTAAUT)") == false)
                {
                failed("Could not create an output queue. "
                       + cmd.getMessageList()[0].getID()
                       + ": " + cmd.getMessageList()[0].getText());
                return;
                }

            if (cmd.run("QSYS/GRTOBJAUT OBJ(NPJAVA/CTORTST) OBJTYPE(*OUTQ) USER(*PUBLIC)") == false)
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

            if ((allVariations || variationsToRun_.contains("2")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(2);
                Var002();
                }

            if ((allVariations || variationsToRun_.contains("3")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(3);
                Var003();
                }

            if ((allVariations || variationsToRun_.contains("4")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
                {
                setVariation(4);
                Var004();
                }

            if ((allVariations || variationsToRun_.contains("5")) &&
                runMode_ != ATTENDED)  // Note: This is an unattended variation.
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

         // clear the output queue we created.
         if (cmd.run("QSYS/CLROUTQ OUTQ(NPJAVA/CTORTST)") == false)
             {
             output_.println("Could not clear output queue we created. "
                             + cmd.getMessageList()[0].getID()
                             + ": " + cmd.getMessageList()[0].getText());
             }

         // delete the output queue we created.
         if (cmd.run("QSYS/DLTOUTQ OUTQ(NPJAVA/CTORTST)") == false)
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
     * Tests construction of Output Queue object with good parameters.
     **/
    public void Var001()
    {
     try
     {
         // create an output queue object using valid system name and queue name
         OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CTORTST.OUTQ");

         // call a method to ensure we have a good object
         outQ.update();

         succeeded();  // Note: This variation will be successful.
         } // end try block

     catch (Exception e)
     {
         failed(e, "Unexpected exception");
     }

    } // end Var001

    /**
     * Tests construction of Output Queue object with null system name parameter.
     *      A NullPointerException should be thrown.
     **/
    public void Var002()
    {
     try
     {
         // create an output queue object using null system name
         OutputQueue outQ = new OutputQueue(null, "/QSYS.LIB/NPJAVA.LIB/CTORTST.OUTQ");

         failed("Could use null system name."+outQ);
     }

     catch(Exception e)
     {
         if (exceptionIs(e, "NullPointerException"))  succeeded();  // Note: This variation will be successful.
         else failed (e, "Unexpected exception");
     }

    } // end Var002

    /**
     * Tests construction of Output Queue object with null output queue name parameter.
     *      A NullPointerException should be thrown.
     **/
    public void Var003()
    {
     try
     {
         // create an output queue object using null output queue name
         OutputQueue outQ = new OutputQueue(systemObject_, null);

         failed("Could use null output queue name."+outQ);
     }

     catch (Exception e)
     {
         // Check for NullPointerException
         if (exceptionIs(e, "NullPointerException"))  succeeded();  // Note: This variation will be successful.
         else failed (e, "Unexpected exception");
     }

    } // end Var003

    /**
     * Tests construction of Output Queue object with invalid output queue name parameter.
     *      A ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var004()
    {
     try
     {
         // create an output queue object using an invalid output queue name
         OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/NPJAVA.LIB/CTORTST.XXX");

         failed("Could use invalid output queue name."+outQ);
     }

     catch (Exception e)
     {
         // Check for IllegalPathNameException
         if (exceptionIs(e, "IllegalPathNameException")) succeeded();  // Note: This variation will be successful.
         else failed (e, "Unexpected exception");
     }

    } // end Var004

    /**
     * Tests construction of Output Queue object with no parameters. This
     * default constructor is provide for visual application builders
     * that support JavaBeans.
     **/
    public void Var005()
    {
     try
         {
         // create an output queue object with no parameters.
         OutputQueue outQ = new OutputQueue();

         // Note: This variation will be successful.
         assertCondition(true, "outQ="+outQ);  

         } // end try block

     catch (Exception e)
         {
         failed(e, "Unexpected exception");
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

} // end NPOutQCtorTestcase class


