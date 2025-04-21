///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RIFSTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceListener;

import test.RIFS.RIFSFileBasicTestcase;
import test.RIFS.RIFSFileBeanInfoTestcase;
import test.RIFS.RIFSFileBufferedResourceTestcase;
import test.RIFS.RIFSFileDeleteTestcase;
import test.RIFS.RIFSFileGenericAttributeTestcase;
import test.RIFS.RIFSFileListBasicTestcase;
import test.RIFS.RIFSFileListBeanInfoTestcase;
import test.RIFS.RIFSFileListResourceListTestcase;
import test.RIFS.RIFSFileListSelectionTestcase;
import test.RIFS.RIFSFileListSortTestcase;
import test.RIFS.RIFSFileSpecificAttributeAtoITestcase;
import test.RIFS.RIFSFileSpecificAttributeLtoPTestcase;

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
Test driver for the RIFS* classes.  These are in a different
test driver than the com.ibm.as400.access IFS classes, since
these don't need a network drive.
**/
@SuppressWarnings("deprecation")
public class RIFSTest
extends TestDriver
{


    // Private data.
    private static final String serializeFilename_      = "RIFSTest.ser";




/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public static void main(String args[])
        throws Exception
    {
        runApplication(new RIFSTest(args));
    }



/**
Constructs an object for applets.
 @exception  Exception  If an exception occurs.
**/
    public RIFSTest()
        throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
   @exception  Exception  If an exception occurs.
**/
    public RIFSTest(String[] args)
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
      		  		systemObject_.connectService(AS400.FILE);
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
      	     	    Job[] jobs = systemObject_.getJobs(AS400.FILE);
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
    	   

        // Test the RIFSFile class.
        addTestcase(new RIFSFileBasicTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RIFSFileBeanInfoTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RIFSFileBufferedResourceTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RIFSFileDeleteTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RIFSFileGenericAttributeTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RIFSFileSpecificAttributeAtoITestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RIFSFileSpecificAttributeLtoPTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));

        // Test the RIFSFileList class.
        addTestcase(new RIFSFileListBasicTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RIFSFileListBeanInfoTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RIFSFileListResourceListTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RIFSFileListSelectionTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RIFSFileListSortTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
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
        try {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFilename_));
            object2 = in.readObject ();
            in.close(); 
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
        public Vector<ResourceListEvent>              events_ = new Vector<ResourceListEvent>();

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
            Vector<ResourceListEvent> events2 = new Vector<ResourceListEvent>();
            Enumeration<ResourceListEvent> enumeration = events_.elements();
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


