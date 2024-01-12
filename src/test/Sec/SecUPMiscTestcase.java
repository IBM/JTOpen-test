///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecUPMiscTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sec;

import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.ibm.as400.access.AS400;
import com.ibm.as400.security.auth.UserProfilePrincipal;
import com.ibm.as400.security.auth.UserProfilePrincipalBeanInfo;

import test.SecAuthTest;
import test.Testcase;

/**
 Testcase SecUPMiscTestcase contains miscellaneous tests for the UserProfilePrincipal object.
 <p>Test variations cover the following:
 <ul>
 <li>serialization.
 <li>bean behavior.
 <li>jaas initialize methods.
 <li>alternate ctors.
 </ul>
 **/
public class SecUPMiscTestcase extends Testcase
{
    /**
     Test serialization and restoration of an uninitialized principal.
     **/
    public void Var001()
    {
        try
        {
            UserProfilePrincipal upp1 = null;
            UserProfilePrincipal upp2 = null;

            // Create a UserProfilePrincipal.
            upp1 = new UserProfilePrincipal();

            // Serialize the object.
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("upp.dat"));
            out.writeObject(upp1);
            out.close();

            // Deserialize the object.
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("upp.dat"));
            upp2 = (UserProfilePrincipal)in.readObject();
            in.close();

            // Test attributes.
            assertCondition(upp2.getName().equals("") && upp2.getUserProfileName().equals("") && upp2.getSystem() == null, "Unexpected attribute value.");

            File fd = new File("upp.dat");
            fd.delete();
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test serialization and restoration of an initialized principal.
     **/
    public void Var002()
    {
	StringBuffer sb = new StringBuffer();
	boolean passed = true; 
        AS400 sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1);
        try
        {
            UserProfilePrincipal upp1 = null;
            UserProfilePrincipal upp2 = null;
            String uid = SecAuthTest.uid2;

            // Create a UserProfilePrincipal.
            upp1 = new UserProfilePrincipal();
            upp1.setSystem(sys);
            upp1.setUserProfileName(uid);

            // Serialize the object.
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("upp.dat"));
            out.writeObject(upp1);
            out.close();

            // Deserialize the object.
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("upp.dat"));
            upp2 = (UserProfilePrincipal)in.readObject();
            in.close();

            // Reset the system user and password (not serialized).
            upp2.getSystem().setUserId(SecAuthTest.uid1);
            upp2.getSystem().setPassword(SecAuthTest.pwd1);

	    if ( !(upp2.equals(upp1))) {
		sb.append("upp2("+upp2+") != upp1("+upp1+")\n");
		passed = false; 
	    }
	    if (!(upp2.getName().equals(uid))) {
		sb.append("upp2.getName("+upp2.getName()+") != uid("+uid+")\n");
		passed = false;
	    }
	    if (!(upp2.getUserProfileName().equals(uid))) {
		sb.append("upp2.getUserProfileName("+upp2.getUserProfileName()+") != uid("+uid+")\n");
		passed =false; 
	    }
	    if (!(upp2.getSystem().getSystemName().equals(sys.getSystemName()))) {
		sb.append("upp2.getSystem().getSystemName("+upp2.getSystem().getSystemName()+") != sys.getSystemName("+sys.getSystemName()+")\n");
		passed = false; 

	    } 
            // Test attributes.
            assertCondition(passed, "Unexpected attribute value."+sb.toString());

            File fd = new File("upp.dat");
            fd.delete();
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            sys.disconnectAllServices();
        }
    }

    /**
     Verify the list of bean info events.
     **/
    public void Var003()
    {
        try
        {
            String[] names =
            {
                "propertyChange",
                "propertyChange"
            };
            EventSetDescriptor[] descs = new UserProfilePrincipalBeanInfo().getAdditionalBeanInfo()[0].getEventSetDescriptors();
            assertCondition(SecAuthTest.verifyDescriptors(names, descs), "Expected event not found.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify the list of bean info properties.
     **/
    public void Var004()
    {
        try
        {
            String[] names1 =
            {
                "system",
                "name",
                "user"
            };
            PropertyDescriptor[] descs1 = new UserProfilePrincipalBeanInfo().getAdditionalBeanInfo()[0].getPropertyDescriptors();
            String[] names2 =
            {
                "userProfileName"
            };
            PropertyDescriptor[] descs2 = new UserProfilePrincipalBeanInfo().getPropertyDescriptors();

            assertCondition(SecAuthTest.verifyDescriptors(names1, descs1) && SecAuthTest.verifyDescriptors(names2, descs2), "Expected property not found.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test ctor allowing system and name values.
     **/
    public void Var005()
    {
        try
        {
            // Create the object.
            UserProfilePrincipal upp = new UserProfilePrincipal(systemObject_, SecAuthTest.uid1);
            // Test the values.
            assertCondition(upp.getSystem().equals(systemObject_) && upp.getUserProfileName().equals(SecAuthTest.uid1), "Values specified on constructor not assigned. upp.getSystem()="+upp.getSystem()+" systemObject_="+systemObject_+" upp.getUserPrfileName()="+upp.getUserProfileName()+" SecAuthTest.uid1="+SecAuthTest.uid1);
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful initialization.
     **/
    public void Var006()
    {
        try
        {
            // Create and initialize the principal.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            upp.initialize(SecAuthTest.uid1);
            succeeded();
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed initialization.
     **/
    public void Var007()
    {
        try
        {
            // Create and initialize the principal.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            try
            {
                upp.initialize(null);
                failed("Principal initialization did not fail as expected.");
            }
            catch (NullPointerException npe)
            {
                succeeded();
            }
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
