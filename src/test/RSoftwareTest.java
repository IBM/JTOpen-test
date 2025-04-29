///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RSoftwareTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceListener;

import test.RSoftware.RSoftwareResourceBasicTestcase;
import test.RSoftware.RSoftwareResourceBeanInfoTestcase;
import test.RSoftware.RSoftwareResourceBufferedResourceTestcase;
import test.RSoftware.RSoftwareResourceGenericAttributeTestcase;
import test.RSoftware.RSoftwareResourceSpecificAttributeTestcase;



/**
Test driver for the RSoftwareResource classes.
**/
@SuppressWarnings("deprecation")
public class RSoftwareTest
extends TestDriver
{



    private static final String serializeFilename_ = "RSoftwareTest.ser";
    public static String JC1ProductID_ = null;




/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public static void main(String args[])
        throws Exception
    {
        runApplication(new RSoftwareTest(args));
    }



/**
Constructs an object for applets.
 @exception  Exception  If an exception occurs.
**/
    public RSoftwareTest()
        throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public RSoftwareTest(String[] args)
        throws Exception
    {
        super(args);
    }



/**
Creates the testcases.
**/
    public void createTestcases ()
    {

        // Test the RSoftwareResource class.
        addTestcase(new RSoftwareResourceBasicTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RSoftwareResourceBeanInfoTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RSoftwareResourceBufferedResourceTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RSoftwareResourceGenericAttributeTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RSoftwareResourceSpecificAttributeTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));

        try {
            if (systemObject_.getVRM() < 0x00060100) JC1ProductID_ = "5722JC1";
            else if (systemObject_.getVRM() < 0x00070100) JC1ProductID_ = "5761JC1";
            else JC1ProductID_ = "5770SS1";
          }
    
        catch (Exception e) {
          out_.println("Caught exception from AS400.getVRM:");
          e.printStackTrace(out_);
        }
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
	    out.close(); 
        // Deserialize.
        Object object2 = null;
        ObjectInputStream in = null; 
        try {
            in = new ObjectInputStream (new FileInputStream (serializeFilename_));
            object2 = in.readObject ();
        }
   	    finally {
   	      if (in != null) in.close(); 
       		File f = new File (serializeFilename_);
        	f.delete();
   	    }

   	    return object2;
   	}



    /**
     @exception  Exception  If an exception occurs.
     **/
    public void setup()
    throws Exception
    {

    }


/**
Sample property change listener.
**/
    public static class PropertyChangeListener_
    implements PropertyChangeListener
    {
        public int                  eventCount_ = 0;
        public PropertyChangeEvent  event_ = null;

        public void propertyChange(PropertyChangeEvent event)
        {
            ++eventCount_;
            event_ = event;
        }
    }



/**
Sample resource listener.
**/
    public static class ResourceListener_
    implements ResourceListener
    {
        public int                  eventCount_ = 0;
        public ResourceEvent        event_ = null;

        public void attributeChangesCanceled(ResourceEvent event)
        {
            ++eventCount_;
            event_ = event;
        }

        public void attributeChangesCommitted(ResourceEvent event)
        {
            ++eventCount_;
            event_ = event;
        }

        public void attributeValuesRefreshed(ResourceEvent event)
        {
            ++eventCount_;
            event_ = event;
        }

        public void attributeValueChanged(ResourceEvent event)
        {
            ++eventCount_;
            event_ = event;
        }

        public void resourceCreated(ResourceEvent event)
        {
            ++eventCount_;
            event_ = event;
        }

        public void resourceDeleted(ResourceEvent event)
        {
            ++eventCount_;
            event_ = event;
        }
    }



}


