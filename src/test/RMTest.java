///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RMTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.data.Descriptor;
import com.ibm.as400.data.RecordFormatDocument;

import test.RM.RMConstructorTestcase;
import test.RM.RMDescriptorTestcase;
import test.RM.RMGetDoubleTestcase;
import test.RM.RMGetSetValueTestcase;
import test.RM.RMRecordConstructorTestcase;
import test.RM.RMSetValuesTestcase;
import test.RM.RMToByteArrayTestcase;
import test.RM.RMToXmlTestcase;
import test.RM.RMtoRecordFormatTestcase;
import test.RM.RMtoRecordTestcase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
// import java.util.Vector;

/**
Test driver for the RFML classes (RecordFormatDocument, etc).  
**/
public class RMTest
extends TestDriver
{


    // Private data.
    private static final String serializeFilename_      = "RMTest.ser";
    public static boolean jvmIsSunOrIBM_142_ = false;


/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public static void main(String args[])
        throws Exception
    {
        runApplication(new RMTest(args));
    }


/**
Constructs an object for applets.
 @exception  Exception  If an exception occurs.
**/
    public RMTest()
        throws Exception
    {
        super();
    }


/**
Constructs an object for testing applications.

@param      args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public RMTest(String[] args)
        throws Exception
    {
        super(args);
    }


/**
Creates the testcases.
**/
    public void createTestcases ()
    {
      // Print failure warning if we're running on Sun JDK 1.4.2.
      String javaVersion = System.getProperty("java.version");
      if (javaVersion != null &&
          javaVersion.startsWith("1.4.2"))
      {
        String javaVendor  = System.getProperty("java.vm.vendor");
        if ((javaVendor != null && javaVendor.indexOf("Sun") != -1) ||
            onAS400_)
        {
          System.err.println("\nWARNING: RFML is not supported on Sun JDK 1.4.2, or running natively on IBM i JDK 1.4.2. Expect many RFML testcase variation failures on this JVM.\n");
          jvmIsSunOrIBM_142_ = true;
        }
        
      }
      if (jvmIsSunOrIBM_142_)
      {
	// Don't skip... Testcases will detect JVM 1.4.2 
        // out_.println("Skipping RMTest testcases, since we are on a Sun or IBM Classic JDK 1.4.2");
        // return;
      }
        //boolean allTestcases = (namesAndVars_.size() == 0);
/*
RMCTTestcase.java
RMConstructorTestcase.java
RMDescriptorTestcase.java
RMGetDoubleTestcase.java
RMGetSetValueTestcase.java
RMRecordConstructorTestcase.java
RMSetValuesTestcase.java
RMToByteArrayTestcase.java
RMToXmlTestcase.java
RMtoRecordFormatTestcase.java
RMtoRecordTestcase.java
*/

//        addTestcase(new RMCTTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));

        addTestcase(new RMConstructorTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));

        addTestcase(new RMDescriptorTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));

        addTestcase(new RMGetDoubleTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));

        addTestcase(new RMGetSetValueTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));

        addTestcase(new RMRecordConstructorTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));

        addTestcase(new RMSetValuesTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));

        addTestcase(new RMToByteArrayTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));

        addTestcase(new RMToXmlTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));

        addTestcase(new RMtoRecordFormatTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));

        addTestcase(new RMtoRecordTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));
    }

 /**
Serializes and deserializes an object.

@param  object  The object.
@return         The deserialized object.
 @exception  Exception  If an exception occurs.
**/
    public static Object serialize (Object object)
        throws Exception
    {
	    // Serialize.
	    ObjectOutput out = new ObjectOutputStream (new FileOutputStream (serializeFilename_));
	    out.writeObject (object);
	    out.flush ();

        // Deserialize.
        Object object2 = null;
        try 
        {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFilename_));
            object2 = in.readObject ();
        }
        finally 
        {
           File f = new File (serializeFilename_);
           f.delete();
        }
        
        return object2;
   	}

 /**
Determines whether two Descriptor objects are equal.

@param  desc1  The first Descriptor.
@param  desc2  The second Descriptor.
@return  true if the two objects are equal; false otherwise.
**/
    public static boolean areEqual (Descriptor desc1, Descriptor desc2)
    {
      // Match the name, qualified name, tag name.
      if (!desc1.getName().equals(desc2.getName())) {
        System.err.println("Descriptor names mismatch: " +
                           desc1.getName() + "; " +
                           desc2.getName());
        return false;
      }
      if (!desc1.getQualifiedName().equals(desc2.getQualifiedName())) {
        System.err.println("Descriptor qualified names mismatch: " +
                           desc1.getQualifiedName() + "; " +
                           desc2.getQualifiedName());
        return false;
      }
      if (!desc1.getTagName().equals(desc2.getTagName())) {
        System.err.println("Descriptor tag names mismatch: " +
                           desc1.getTagName() + "; " +
                           desc2.getTagName());
        return false;
      }

      // Match the attribute lists.
      String[] attrs1 = desc1.getAttributeList();
      String[] attrs2 = desc2.getAttributeList();
      if (attrs1.length != attrs2.length) {
        System.err.println("Attribute list lengths mismatch: " +
                           desc1.getQualifiedName() + ": " + attrs1.length + "; " +
                           desc2.getQualifiedName() + ": " + attrs2.length);
        return false;
      }
      for (int i=0; i<attrs1.length; i++) {
        if (!attrs1[i].equals(attrs2[i])) {
          System.err.println("Attribute name mismatch: " +
                             attrs1[i] + "; " +
                             attrs2[i]);
          return false;
        }
        String val1 = desc1.getAttributeValue(attrs1[i]);
        String val2 = desc2.getAttributeValue(attrs2[i]);
        if ((val1 != null && !val1.equals(val2)) ||
            (val1 == null && val2 != null)) {
          System.err.println("Attribute value mismatch: " +
                             attrs1[i] + ": " + val1 + "; " +
                             attrs2[i] + ": " + val2);
          return false;
        }
      }

      // Match the children.
      Enumeration children1 = desc1.getChildren();
      Enumeration children2 = desc2.getChildren();
      if (children1==null && children2==null) return true;
      if (!children1.hasMoreElements() && !children2.hasMoreElements()) return true;
      while (children1.hasMoreElements()) {
        if (!children2.hasMoreElements()) {
          System.err.println("Descriptor1 has more children than descriptor2.");
          return false;
        }
        Descriptor child1 = (Descriptor)children1.nextElement();
        Descriptor child2 = (Descriptor)children2.nextElement();
        if (!areEqual(child1, child2)) return false;
      }
      if (children2.hasMoreElements()) {
        System.err.println("Descriptor2 has more children than descriptor1.");
        return false;
      }

      return true;
    }


    public static void displayTree(RecordFormatDocument doc)
    {
      displayTree(doc.getDescriptor(), "");
    }


    public static void displayTree(Descriptor root, String indentation)
    {
      System.out.println(indentation + "_____________________");
      System.out.println(indentation + "Tag Name:   " + root.getTagName());
      System.out.println(indentation + "Name:       " + root.getName());
      System.out.println(indentation + "QualName:   " + root.getQualifiedName());
      System.out.println(indentation + "Attributes: ");
      String[] attList = root.getAttributeList();
      for (int i=0; i<attList.length; i++) {
        System.out.println(indentation + "            " + attList[i] + "=" + root.getAttributeValue(attList[i])); 
      }

      Enumeration enumeration = root.getChildren();
      // Vector children = new Vector();
      while (enumeration.hasMoreElements()) {
        Descriptor child = (Descriptor)(enumeration.nextElement());
        displayTree(child, indentation + "   ");
      }
    }


}


