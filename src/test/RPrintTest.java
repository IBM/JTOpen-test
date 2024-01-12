///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrintTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Job;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceListener;

import test.RPrint.RPrinterBasicTestcase;
import test.RPrint.RPrinterBeanInfoTestcase;
import test.RPrint.RPrinterBufferedResourceTestcase;
import test.RPrint.RPrinterGenericAttributeTestcase;
import test.RPrint.RPrinterListBasicTestcase;
import test.RPrint.RPrinterListBeanInfoTestcase;
import test.RPrint.RPrinterListBufferedResourceListTestcase;
import test.RPrint.RPrinterListSelectionTestcase;
import test.RPrint.RPrinterListSortTestcase;
import test.RPrint.RPrinterSpecificAttributeAtoDTestcase;
import test.RPrint.RPrinterSpecificAttributeEtoMTestcase;
import test.RPrint.RPrinterSpecificAttributeNtoSTestcase;
import test.RPrint.RPrinterSpecificAttributeTtoZTestcase;

import com.ibm.as400.resource.ResourceListEvent;
import com.ibm.as400.resource.ResourceListListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Vector;



/**
Test driver for the RPrint* classes.  These are in a different
test driver than the com.ibm.as400.access print classes, since
there is really not much in common.
**/
public class RPrintTest
extends TestDriver
{


    // Private data.
    private static final String serializeFilename_      = "RPrintTest.ser";




/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public static void main(String args[])
        throws Exception
    {
        runApplication(new RPrintTest(args));
    }



/**
Constructs an object for applets.
 @exception  Exception  If an exception occurs.
**/
    public RPrintTest()
        throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public RPrintTest(String[] args)
        throws Exception
    {
        super(args);
    }



/**
Creates the testcases.
**/
    public void createTestcases ()
    {
    	 	
    	if(TestDriverStatic.pause_)
    	{ 
      		  	try 
      		  	{						
      		  		systemObject_.connectService(AS400.PRINT);
      			}
      	     	catch (AS400SecurityException e) 
      	     	{
      	     		// TODO Auto-generated catch block
      				e.printStackTrace();
      			} 
      	     	catch (IOException e) 
      	     	{
      				// TODO Auto-generated catch block
      	     	    e.printStackTrace();
      			}
      				 	 	   
      	     	try
      	     	{
      	     	    Job[] jobs = systemObject_.getJobs(AS400.PRINT);
      	     	    System.out.println("Host Server job(s): ");

      	     	    	for(int i = 0 ; i< jobs.length; i++)
      	     	    	{   	    	
      	     	    		System.out.println(jobs[i]);
      	     	    	}    	    
      	     	 }
      	     	 catch(Exception exc){}
      	     	    
      	     	 try 
      	     	 {
      	     	    	System.out.println ("Toolbox is paused. Press ENTER to continue.");
      	     	    	System.in.read ();
      	     	 } 
      	     	 catch (Exception exc) {};   	   
    	} 
    	
    	
        boolean allTestcases = (namesAndVars_.size() == 0);

        // Test the RPrinter and RPrinterBeanInfo classes.
        addTestcase(new RPrinterBasicTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_));
        addTestcase(new RPrinterBeanInfoTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_));
        addTestcase(new RPrinterBufferedResourceTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_)); 
        addTestcase(new RPrinterGenericAttributeTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_));
        addTestcase(new RPrinterSpecificAttributeAtoDTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_));
        addTestcase(new RPrinterSpecificAttributeEtoMTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_));
        addTestcase(new RPrinterSpecificAttributeNtoSTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_));
        addTestcase(new RPrinterSpecificAttributeTtoZTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_));

        // Test the RPrinterList and RPrinterListBeanInfo class.
        addTestcase(new RPrinterListBasicTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_));
        addTestcase(new RPrinterListBeanInfoTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_));
        addTestcase(new RPrinterListBufferedResourceListTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_));
        addTestcase(new RPrinterListSelectionTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_));
        addTestcase(new RPrinterListSortTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_, printer_));
    }



    /*
    public static void createPrinter(AS400 system, String printerName, String textDescription)
        throws Exception
    {
        CommandCall cc = new CommandCall();
        cc.setSystem(system);
        boolean success = cc.run("CRTDEVPRT DEVD(" + printerName + ") DEVCLS(*LCL) TYPE(*IPDS) MODEL(0) PORT(5) SWTSET(0) ONLINE(*NO) FONT(011) TEXT('" + textDescription + "')");
        AS400Message[] messageList = cc.getMessageList();
        if (messageList != null) {
            for(int i = 0; i < messageList.length; ++i)
                System.out.println(messageList[i]);
        }

        success = cc.run("STRPRTWTR DEV(" + printerName + ")");
        messageList = cc.getMessageList();
        if (messageList != null) {
            for(int i = 0; i < messageList.length; ++i)
                System.out.println(messageList[i]);
        }
    }
    */



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
        try {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFilename_));
            object2 = in.readObject ();
        }
   	    finally {
       		File f = new File (serializeFilename_);
        	f.delete();
   	    }

   	    return object2;
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



/**
Sample resource list listener.
**/
    public static class ResourceListListener_
    implements ResourceListListener
    {
        public Vector               events_ = new Vector();

        public void lengthChanged(ResourceListEvent event)
        {
            events_.addElement(event);
        }

        public void listClosed(ResourceListEvent event)
        {
            events_.addElement(event);
        }

        public void listCompleted(ResourceListEvent event)
        {
            events_.addElement(event);
        }

        public void listInError(ResourceListEvent event)
        {
            events_.addElement(event);
        }

        public void listOpened(ResourceListEvent event)
        {
            events_.addElement(event);
        }

        public void resourceAdded(ResourceListEvent event)
        {
            events_.addElement(event);
        }

        public ResourceListEvent[] getEvents(int id)
        {
            // Weed out events of a given id.
            Vector events2 = new Vector();
            Enumeration enumeration = events_.elements();
            while(enumeration.hasMoreElements()) {
                ResourceListEvent event = (ResourceListEvent)enumeration.nextElement();
                if (event.getID() == id)
                    events2.addElement(event);
            }
            ResourceListEvent[] asArray = new ResourceListEvent[events2.size()];
            events2.copyInto(asArray);
            return asArray;
        }

    }



/**
Sample vetoable change listener.
**/
    public static class VetoableChangeListener_
    implements VetoableChangeListener
    {
        public int                  eventCount_     = 0;
        public PropertyChangeEvent  event_          = null;
        private boolean             veto_           = false;

        public VetoableChangeListener_()
        {
            veto_ = false;
        }

        public VetoableChangeListener_(boolean veto)
        {
            veto_ = veto;
        }

        public void vetoableChange(PropertyChangeEvent event)
        throws PropertyVetoException
        {
            if (veto_)
                throw new PropertyVetoException("VETO", event);
            ++eventCount_;
            event_ = event;
        }
    }



}


