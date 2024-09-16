///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFOutStrFlushTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase NPSplFOutStrFlushTestcase.
 **/
public class NPSplFOutStrFlushTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFOutStrFlushTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFOutStrFlushTestcase(AS400            systemObject,
                                     Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
    {
     // $$$ TO DO $$$
     // Replace the third parameter with the total number of variations
     // in this testcase.
     super(systemObject, "NPSplFOutStrFlushTestcase", 1,
           variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFOutStrFlushTestcase");
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
     * Tests flushing a spooled file output stream.
     **/
    public void Var001()
    {
        try
            {
            // buffer for data
            byte[] buf = new byte[2048];

            // fill the buffer
            for (int i=0; i<2048; i++) buf[i] = (byte)'9';

            // create a spooled file output stream
            SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, null);

            // write some data
            outStream.write(buf);

            // flush the spooled file output stream
            outStream.flush();

            // close the spooled file output stream
            outStream.close();

            succeeded();
            } // end try block

        catch (Exception e)
            {
            failed(e, "Unexpected exception");
            }

    } // end Var001

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

} // end NPSplFOutStrFlushTestcase class



