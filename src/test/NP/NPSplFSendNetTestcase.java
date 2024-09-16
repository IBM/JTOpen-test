///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFSendNetTestcase.java
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
import java.util.Enumeration;
import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase NPSplFSendNetTestcase.
 **/
public class NPSplFSendNetTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPSplFSendNetTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFSendNetTestcase(AS400            systemObject,
				 Vector           variationsToRun,
				 int              runMode,
				 FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPSplFSendNetTestcase", 4,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFSendNetTestcase");
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
		runMode_ != ATTENDED)  // Note: This is an unattended variation.
	    {
		setVariation(2);
		Var002();
	    }

          // run variation 3
	    if ((allVariations || variationsToRun_.contains("3")) &&
		runMode_ != ATTENDED)  // Note: This is an unattended variation.
	    {
		setVariation(3);
		Var003();
	    }

          // run variation 4
	    if ((allVariations || variationsToRun_.contains("4")) &&
		runMode_ != ATTENDED)  // Note: This is an unattended variation.
	    {
		setVariation(4);
		Var004();
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
     Tests sending a SpooledFile with good required attributes.
     **/
    public void Var001()
    {
	try
	{
          // Create a spooled file to send
          SpooledFileOutputStream out = new SpooledFileOutputStream(systemObject_,
                                                                    null, null, null);
          SCS3812Writer wtr = new SCS3812Writer(out, 37, systemObject_); // @B1C

          wtr.write("This is a test spooled file to send.");
          wtr.close();

          SpooledFile splf = out.getSpooledFile();

          // Create the required send attributes.
          PrintParameterList parms = new PrintParameterList();
  
          parms.setParameter(PrintObject.ATTR_TOUSERID, systemObject_.getUserId());
          parms.setParameter(PrintObject.ATTR_TOADDRESS, systemObject_.getSystemName());
          parms.setParameter(PrintObject.ATTR_DATAFORMAT, "*ALLDATA");

          splf.sendNet(parms);
	    succeeded();  // Note: This variation will be successful.
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}

    } // end Var001


    /**
     Tests sending a SpooledFile with good required and optional attributes.
     **/
    public void Var002()
    {
	try
	{
          // Create a spooled file to send
          SpooledFileOutputStream out = new SpooledFileOutputStream(systemObject_,
                                                                    null, null, null);
          SCS3812Writer wtr = new SCS3812Writer(out, 37, systemObject_); // @B1C

          wtr.write("This is a test spooled file to send.");
          wtr.close();
  
          SpooledFile splf = out.getSpooledFile();

          // Create the required send attributes.
          PrintParameterList parms = new PrintParameterList();

          parms.setParameter(PrintObject.ATTR_TOUSERID, systemObject_.getUserId());
          parms.setParameter(PrintObject.ATTR_TOADDRESS, systemObject_.getSystemName());
          parms.setParameter(PrintObject.ATTR_DATAFORMAT, "*ALLDATA");
          parms.setParameter(PrintObject.ATTR_VMMVSCLASS, "A");
          parms.setParameter(PrintObject.ATTR_SENDPTY, "*NORMAL");

          splf.sendNet(parms);
	    succeeded();  // Note: This variation will be successful.
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}

    } // end Var002


    /**
     Tests sending a SpooledFile with an invalid attribute value.
     **/
    public void Var003()
    {
	try
	{
          // Create a spooled file to send
          SpooledFileOutputStream out = new SpooledFileOutputStream(systemObject_,
                                                                    null, null, null);
          SCS3812Writer wtr = new SCS3812Writer(out, 37, systemObject_); // @B1C

          wtr.write("This is a test spooled file to send.");
          wtr.close();

          SpooledFile splf = out.getSpooledFile();

          // Create the required send attributes.
          PrintParameterList parms = new PrintParameterList();

          parms.setParameter(PrintObject.ATTR_TOUSERID, systemObject_.getUserId());
          parms.setParameter(PrintObject.ATTR_TOADDRESS, systemObject_.getSystemName());
          parms.setParameter(PrintObject.ATTR_DATAFORMAT, "*ALLDATA");
          parms.setParameter(PrintObject.ATTR_VMMVSCLASS, "A");
          parms.setParameter(PrintObject.ATTR_SENDPTY, "*LOW"); // Invalid

          splf.sendNet(parms);
	    failed("Exception was not thrown.");  
	} // end try block

	catch (Exception e)
	{
        assertExceptionIs(e, "AS400Exception");
	}

    } // end Var003

    /**
     Tests sending a SpooledFile with an invalid attribute.
     **/
    public void Var004()
    {
	try
	{
          // Create a spooled file to send
          SpooledFileOutputStream out = new SpooledFileOutputStream(systemObject_,
                                                                    null, null, null);
          SCS3812Writer wtr = new SCS3812Writer(out, 37, systemObject_); // @B1C

          wtr.write("This is a test spooled file to send.");
          wtr.close();

          SpooledFile splf = out.getSpooledFile();

          // Create the required send attributes.
          PrintParameterList parms = new PrintParameterList();

          parms.setParameter(PrintObject.ATTR_TOUSERID, systemObject_.getUserId());
          parms.setParameter(PrintObject.ATTR_TOADDRESS, systemObject_.getSystemName());
          parms.setParameter(PrintObject.ATTR_DATAFORMAT, "*RCDDATA");
          parms.setParameter(PrintObject.ATTR_VMMVSCLASS, "A");
          parms.setParameter(PrintObject.ATTR_SENDPTY, "*NORMAL");
          parms.setParameter(PrintObject.ATTR_RMTPRTQ, "JAVATEAM"); // Invalid attribute

          splf.sendNet(parms);
	    failed("Exception was not thrown.");  
	} // end try block

	catch (Exception e)
	{
        assertExceptionIs(e, "AS400Exception");
	}

    } // end Var004

} // end NPSplFSendNetTestcase class


