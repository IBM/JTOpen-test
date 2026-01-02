///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserTest.java
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

import test.User.UserBasicTestcase;
import test.User.UserBeanInfoTestcase;
import test.User.UserBufferedResourceTestcase;
import test.User.UserGenericAttributeTestcase;
import test.User.UserGetSetAtoCTestcase;
import test.User.UserGetSetDtoGTestcase;
import test.User.UserGetSetHtoKTestcase;
import test.User.UserGetSetLtoMTestcase;
import test.User.UserGetSetNtoQTestcase;
import test.User.UserGetSetRtoSTestcase;
import test.User.UserGetSetTtoZTestcase;
import test.User.UserGroupTestcase;
import test.User.UserListBackwardsCompatibilityTestcase;
import test.User.UserListBasicTestcase;
import test.User.UserListBeanInfoTestcase;
import test.User.UserListBufferedResourceListTestcase;
import test.User.UserListSelectionTestcase;
import test.User.UserListSortTestcase;
import test.User.UserObjectsOwnedListEntryTestcase;
import test.User.UserObjectsOwnedListTestcase;
import test.User.UserSpecificAttributeAtoCTestcase;
import test.User.UserSpecificAttributeDtoFTestcase;
import test.User.UserSpecificAttributeGtoITestcase;
import test.User.UserSpecificAttributeJtoLTestcase;
import test.User.UserSpecificAttributeMtoNTestcase;
import test.User.UserSpecificAttributeOtoQTestcase;
import test.User.UserSpecificAttributeRtoSTestcase;
import test.User.UserSpecificAttributeTtoZTestcase;

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
 Test driver for the User, UserGroup, and UserList classes.
 **/
@SuppressWarnings("deprecation")
public class UserTest extends TestDriver
{
    private static final String serializeFilename_ = "UserTest.ser";
    public static String COLLECTION = "USERTEST";

    public void setup() throws Exception {
    	super.setup();
    	COLLECTION = testLib_;
    }
    /**
     Run the test as an application.
     @param  args  The command line arguments.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new UserTest(args));
        }
        catch (Exception e)
        {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
        }

            System.exit(0);
    }

    /**
     Constructs an object for applets.
     @exception  Exception  If an exception occurs.
     **/
    public UserTest() throws Exception
    {
        super();
    }

    /**
     Constructs an object for testing applications.
     @param  args  The command line arguments.
     @exception  Exception  If an exception occurs.
     **/
    public UserTest(String[] args) throws Exception
    {
        super(args);
    }

    /**
     Creates the testcases.
     **/
    public void createTestcases()
    {
    	
    	if(TestDriverStatic.pause_)
    	{ 
      		  	try 
      		  	{						
      		  		systemObject_.connectService(AS400.SIGNON);
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
      	     	    Job[] jobs = systemObject_.getJobs(AS400.SIGNON);
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
    	
    	
        Testcase[] testcases =
        {
            // Test the User class.
            new UserBasicTestcase(),
            new UserBeanInfoTestcase(),
            new UserGetSetAtoCTestcase(),
            new UserGetSetDtoGTestcase(),
            new UserGetSetHtoKTestcase(),
            new UserGetSetLtoMTestcase(),
            new UserGetSetNtoQTestcase(),
            new UserGetSetRtoSTestcase(),
            new UserGetSetTtoZTestcase(),
            new UserBufferedResourceTestcase(),
            new UserGenericAttributeTestcase(),
            new UserSpecificAttributeAtoCTestcase(),
            new UserSpecificAttributeDtoFTestcase(),
            new UserSpecificAttributeGtoITestcase(),
            new UserSpecificAttributeJtoLTestcase(),
            new UserSpecificAttributeMtoNTestcase(),
            new UserSpecificAttributeOtoQTestcase(),
            new UserSpecificAttributeRtoSTestcase(),
            new UserSpecificAttributeTtoZTestcase(),
            // Test the UserGroup class.
            new UserGroupTestcase(),
            // Test the UserList class.
            new UserListBackwardsCompatibilityTestcase(),
            new UserListBasicTestcase(),
            new UserListBeanInfoTestcase(),
            new UserListBufferedResourceListTestcase(),
            new UserListSelectionTestcase(),
            new UserListSortTestcase(),
            //Test the UserObjects class
            new UserObjectsOwnedListTestcase(),
            new UserObjectsOwnedListEntryTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_, pwrSysUserID_, pwrSysPassword_);
            testcases[i].setProxy5(proxy5_); 
            addTestcase(testcases[i]);
        }
    }

    /**
     Serializes and deserializes an object.
     @param  object  The object.
     @return  The deserialized object.
     @exception  Exception  If an exception occurs.
     **/
    public static Object serialize(Object object) throws Exception
    {
        // Serialize.
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(serializeFilename_));
        out.writeObject(object);
        out.flush();
        out.close(); 
        // Deserialize.
        Object object2 = null;
        try
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(serializeFilename_));
            object2 = in.readObject();
            in.close(); 
        }
        finally
        {
            File f = new File(serializeFilename_);
            f.delete();
        }

        return object2;
    }

    /**
     Sample property change listener.
     **/
    public static class PropertyChangeListener_ implements PropertyChangeListener
    {
        public int eventCount_ = 0;
        public PropertyChangeEvent event_ = null;

        public void propertyChange(PropertyChangeEvent event)
        {
            ++eventCount_;
            event_ = event;
        }
    }

    /**
     Sample resource listener.
     **/
    public static class ResourceListener_ implements ResourceListener
    {
        public int eventCount_ = 0;
        public ResourceEvent event_ = null;

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
    public static class ResourceListListener_ implements ResourceListListener
    {
        public Vector<ResourceListEvent> events_ = new Vector<ResourceListEvent>();

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
            while(enumeration.hasMoreElements())
            {
                ResourceListEvent event = enumeration.nextElement();
                if (event.getID() == id)
                {
                    events2.addElement(event);
                }
            }
            ResourceListEvent[] asArray = new ResourceListEvent[events2.size()];
            events2.copyInto(asArray);
            return asArray;
        }
    }

    /**
     Sample vetoable change listener.
     **/
    public static class VetoableChangeListener_ implements VetoableChangeListener
    {
        public int eventCount_ = 0;
        public PropertyChangeEvent event_ = null;
        private boolean veto_ = false;

        public VetoableChangeListener_()
        {
            veto_ = false;
        }

        public VetoableChangeListener_(boolean veto)
        {
            veto_ = veto;
        }

        public void vetoableChange(PropertyChangeEvent event) throws PropertyVetoException
        {
            if (veto_)
            {
                throw new PropertyVetoException("VETO", event);
            }
            ++eventCount_;
            event_ = event;
        }
    }
}
