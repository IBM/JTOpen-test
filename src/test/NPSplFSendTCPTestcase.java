///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPSplFSendTCPTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import java.util.Enumeration;
import com.ibm.as400.access.*;

/**
 Testcase NPSplFSendTCPTestcase.
 **/
public class NPSplFSendTCPTestcase extends Testcase
{
    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPSplFSendTCPTestcase(AS400            systemObject,
				 Vector           variationsToRun,
				 int              runMode,
				 FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
	super(systemObject, "NPSplFSendTCPTestcase", 5,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPSplFSendTCPTestcase");
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

          // run variation 5
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
     Tests sending a spooled file with good required attributes.
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
  
          parms.setParameter(PrintObject.ATTR_RMTSYSTEM, systemObject_.getSystemName());
          parms.setParameter(PrintObject.ATTR_RMTPRTQ, "PRT01");
          parms.setParameter(PrintObject.ATTR_SCS2ASCII, "*NO");

          splf.sendTCP(parms);
	    succeeded();  // Note: This variation will be successful.

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
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
  
          parms.setParameter(PrintObject.ATTR_RMTSYSTEM, systemObject_.getSystemName());
          parms.setParameter(PrintObject.ATTR_RMTPRTQ, "PRT01");
          parms.setParameter(PrintObject.ATTR_DELETESPLF, "*NO");
          parms.setParameter(PrintObject.ATTR_DESTOPTION, "-oTITLE=LPR" );
          parms.setParameter(PrintObject.ATTR_DESTINATION, "*AS400");
          parms.setParameter(PrintObject.ATTR_MFGTYPE, "*HP4");
          parms.setParameter(PrintObject.ATTR_SCS2ASCII, "*NO");
          parms.setParameter(PrintObject.ATTR_SEPPAGE, "*NO");

          splf.sendTCP(parms);
	    succeeded();  // Note: This variation will be successful.

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var002

    /**
     Tests sending a SpooledFile with good required and optional attributes.
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
  
          parms.setParameter(PrintObject.ATTR_RMTSYSTEM, systemObject_.getSystemName());
          parms.setParameter(PrintObject.ATTR_RMTPRTQ, "PRT01");
          parms.setParameter(PrintObject.ATTR_DELETESPLF, "*NO");
          parms.setParameter(PrintObject.ATTR_DESTOPTION, "-oTITLE=LPR" );
          parms.setParameter(PrintObject.ATTR_DESTINATION, "*AS400");
          parms.setParameter(PrintObject.ATTR_MFGTYPE, "*HP4");
          parms.setParameter(PrintObject.ATTR_SCS2ASCII, "*NO");
          parms.setParameter(PrintObject.ATTR_SEPPAGE, "*NO");

          splf.sendTCP(parms);
	    succeeded();  // Note: This variation will be successful.

	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end Var003

    /**
     Tests sending a SpooledFile with an invalid attribute value.
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
  
          parms.setParameter(PrintObject.ATTR_RMTSYSTEM, systemObject_.getSystemName());
          parms.setParameter(PrintObject.ATTR_RMTPRTQ, "PRT01");
          parms.setParameter(PrintObject.ATTR_DELETESPLF, "*YES");
          parms.setParameter(PrintObject.ATTR_DESTOPTION, "-oTITLE=LPR" );
          parms.setParameter(PrintObject.ATTR_DESTINATION, "*AS/400"); // Invalid
          parms.setParameter(PrintObject.ATTR_MFGTYPE, "*HP4");
          parms.setParameter(PrintObject.ATTR_SCS2ASCII, "*NO");
          parms.setParameter(PrintObject.ATTR_SEPPAGE, "*NO");

          splf.sendTCP(parms);
	    failed("Exception was not thrown.");  
	} // end try block

	catch (Exception e)
	{
        assertExceptionIs(e, "AS400Exception");
	}

    } // end Var004

    /**
     Tests sending a Spooled File with an invalid attribute.
     **/
    public void Var005()
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
  
          parms.setParameter(PrintObject.ATTR_RMTSYSTEM, systemObject_.getSystemName());
          parms.setParameter(PrintObject.ATTR_RMTPRTQ, "PRT01");
          parms.setParameter(PrintObject.ATTR_DELETESPLF, "*YES");
          parms.setParameter(PrintObject.ATTR_DESTOPTION, "-oTITLE=LPR" );
          parms.setParameter(PrintObject.ATTR_DESTINATION, "*AS400");
          parms.setParameter(PrintObject.ATTR_MFGTYPE, "*HP4");
          parms.setParameter(PrintObject.ATTR_SCS2ASCII, "*NO");
          parms.setParameter(PrintObject.ATTR_SEPPAGE, "*NO");
          parms.setParameter(PrintObject.ATTR_SENDPTY, "*NORMAL"); // Invalid

          splf.sendTCP(parms);
	    failed("Exception was not thrown.");  
	
	} // end try block

	catch (Exception e)
	{
        assertExceptionIs(e, "AS400Exception");
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

} // end NPSplFSendTCPTestcase class


