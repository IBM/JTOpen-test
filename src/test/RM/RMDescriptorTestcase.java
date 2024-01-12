///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RMDescriptorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.RM;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.data.*;

import test.RMTest;
import test.Testcase;

/**
 The RMDescriptorTestcase class tests the following methods of the RecordFormatDocument class:
 <li>getDescriptor(),
 <li>getDescriptor(docName),
 <li>getDescriptor(docName,classLoader).
 <p>
 NOTE: A lot of the getDescriptor() testing will be done in RFMLDocNameConstructorTestcase.
 The method used to validate the parsing of rfml documents read in will be to call 
 getDescriptor(docName) to verify the proper nodes with the proper attributes have 
 been created. 
 **/
public class RMDescriptorTestcase extends Testcase
{

  private static final boolean DEBUG = false;

    /**
     Constructor.
     **/
    public RMDescriptorTestcase(AS400 systemObject, 
                             Hashtable namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream)
    {
        super(systemObject, "RMDescriptorTestcase", namesAndVars, runMode, fileOutputStream);
    }

    /**
     Test getDescriptor().  Verify that getDescriptor() returns null when the current rfml doc is not set.
     **/
    public void Var001()
    {
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument();
            assertCondition((rfmlDoc.getDescriptor() == null) );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Test getDescriptor().  Verify that getDescriptor() causes no error when the
     record format document has been initialized to a valid rfml doc.
     **/
    public void Var002()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }

        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.qcustcdt");
            Descriptor desc = rfmlDoc.getDescriptor();
            assertCondition(desc != null);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Can't do this.  The RFML DTD prohibits empty <rfml> element.
///    /**
///     Test getDescriptor().  Verify that getDescriptor() causes no error when the
///     record format document has been initialized to a valid rfml doc that contains
///     no elements (empty).
///     **/
///    public void Var003()
///    {
///        try
///        {
///            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty");
///            Descriptor desc = rfmlDoc.getDescriptor();
///            assertCondition(desc != null);
///        }
///        catch (Exception e)
///        {
///            failed(e);
///        }
///    }

    /**
     Test getDescriptor(docName). Pass null and expect an exception.
     **/
    public void Var003()
    {
        try
        {
            Descriptor desc = RecordFormatDocument.getDescriptor(null);
            failed("Did not throw exception."+desc);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test getDescriptor(docName). Pass in name that 
     doesn't exist. Exception should be generated.
     **/
    public void Var004()
    {
        try
        {
            Descriptor desc = RecordFormatDocument.getDescriptor("test.rfml.noSuchFile");
            failed("Did not throw exception."+desc);
        }
        catch (XmlException e)
        {
          if (DEBUG) e.printStackTrace();
          Exception e1 = e.getException();
          String expectedMsg = "RFML document source 'test.rfml.noSuchFile' cannot be found";
          String receivedMsg = e1.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e1, "java.util.MissingResourceException"));
        }
        catch (Exception e)
        {
          failed(e);
        }
    }

    /**
     Test getDescriptor(docName).  Verify that getDescriptor(docName) causes causes an exception when the record format document passed in is an empty document.
     **/
    public void Var005()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }

        try
        {
            Descriptor desc = RecordFormatDocument.getDescriptor("test.rfml.empty");
            failed("Did not throw exception."+desc);
        }
        catch (Exception e)
        {
          ///e.printStackTrace();
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }



    /**
     Test getDescriptor(docName,classLoader) passing null for docName.
    Expect an exception.
     **/
    public void Var006()
    {
        try
        {
            Descriptor desc = RecordFormatDocument.getDescriptor(null, ClassLoader.getSystemClassLoader());
            failed("Did not throw exception."+desc);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }


    /**
     Test getDescriptor(docName,classLoader) passing null for classLoader.
    Expect an exception.
     **/
    public void Var007()
    {
        try
        {
            Descriptor desc = RecordFormatDocument.getDescriptor("test.rfml.char1", null);
            failed("Did not throw exception."+desc);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }


/// Note: I don't know how to get an invalid class loader.  - jlee
///    /**
///     Test getDescriptor(docName,classLoader) passing an invalid classLoader.
///    Expect an exception.
///     **/
///    public void Var009()
///    {
///        try
///        {
///            RecordFormatDocument rfmlDoc = new RecordFormatDocument(null);
///            failed("Did not throw exception.");
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test getDescriptor(docName,classLoader) passing a valid doc and classLoader.
    Ensure no errors.
     **/
    public void Var008()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }

        try
        {
            Descriptor desc = RecordFormatDocument.getDescriptor("test.rfml.qcustcdt", ClassLoader.getSystemClassLoader());
            assertCondition(desc != null);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: The RFML DTD prohibits empty documents.
///    /**
///     getDescriptor(docName,classLoader).  Verify that getDescriptor(docName,classLoader) causes no error when the
///     record format document passed in is an empty document.
///     **/
///    public void Var011()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            failed(e, "Unexpected exception");
///        }
///    }


/// Note: The following scenarios are covered by variations in testcase RFMLConstructorTestcase.

///    /**
///     getDescriptor().  Verify that getDescriptor() returns valid RfmlDescriptor and
///     RecordFormatDescriptor objects when called on a valid rfml document with just an empty &lt;rfml&gt; element and an empty &lt;recordformat&gt; element with no attributes.  Call the methods of the Descriptor interface to ensure the proper values are returned for the RMDescriptor and for the RecordFormatDescriptor.
///     **/
///    public void Var012()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            failed(e, "Unexpected exception");
///        }
///    }
///
///    /**
///     getDescriptor().  Verify that getDescriptor() returns valid RfmlDescriptor and
///     RecordFormatDescriptor objects when called on a valid rfml document with just an
///     &lt;rfml&gt; element and an empty &lt;recordformat&gt; element that contain proper 
///     attribute values.  Call the methods of the Descriptor interface to ensure the proper values 
///     are returned for the RMDescriptor and for the RecordFormatDescriptor.
///     **/
///    public void Var013()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            failed(e, "Unexpected exception");
///        }
///    }


/// Note: This scenario is invalid, since both the <rfml> and <recordformat> elements have required attributes.
///    /**
///     getDescriptor().  Verify that getDescriptor() returns valid RfmlDescriptor and
///     RecordFormatDescriptor objects when called on a valid serialized rfml document with just an &lt;rfml&gt; element with no attributes and an empty &lt;recordformat&gt; element with no attributes.  Call the methods 
///     of the Descriptor interface to ensure the proper values are returned for the
///     RfmlDescriptor and for the RecordFormatDescriptor.
///     **/
///    public void Var014()
///    {
///    }

    /**
     getDescriptor().  Verify that getDescriptor() returns valid RfmlDescriptor and
     RecordFormatDescriptor objects when called on a valid serialized rfml document with just an &lt;rfml&gt; element and an empty &lt;recordformat&gt; element that contain proper attribute values. Call the methods of the Descriptor interface to ensure the proper values are returned for the RfmlDescriptor and for the RecordFormatDescriptor.
     **/
    public void Var009()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
      File serFile = null;
        try
        {
          RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty1");
          Descriptor desc0 = rfmlDoc.getDescriptor();
          serFile = new File("empty1ser.rfml.ser");
          if (serFile.exists()) serFile.delete();
          rfmlDoc.serialize(serFile);
          Descriptor desc1 = RecordFormatDocument.getDescriptor("empty1ser");
          assertCondition(desc1 != null &&
                 RMTest.areEqual(desc1, desc0));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        finally
        {
          try {
            if (serFile != null && serFile.exists()) { serFile.delete(); }
          }
          catch (Exception e) {}
        }
    }

/// Note: This scenario is tested by a variation in RMConstructorTestcase.
///    /**
///     Test getDescriptor(docName).  Verify that getDescriptor(docName) returns valid RfmlDescriptor, StructDescriptor and DataDescriptor objects when called on a valid serialized rfml document with just a &lt;struct&gt; element containing one &lt;data&gt; element.
///     Call the methods of the Descriptor interface to ensure the proper values 
///     are returned for the RfmlDescriptor, StructDescriptor and DataDescriptor.
///     **/
///    public void Var016()
///    {
///    }

}
