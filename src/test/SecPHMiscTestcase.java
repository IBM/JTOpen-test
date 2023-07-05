///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecPHMiscTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.ibm.as400.access.AS400;
import com.ibm.as400.security.auth.ProfileHandleCredential;
import com.ibm.as400.security.auth.ProfileHandleCredentialBeanInfo;

/**
 Testcase SecPHMiscTestcase contains miscellaneous tests for the ProfileHandleCredential object.
 <p>Test variations cover the following:
 <ul>
 <li>serialization.
 <li>bean behavior.
 </ul>
 **/
public class SecPHMiscTestcase extends Testcase
{
    AS400 nativeSystemObject = null;
    private boolean profileHandleImplNativeAvailable = false; 

    public void setup() {
	if (isNative_) {
	    nativeSystemObject = new AS400(); 
	    System.out.println("nativeSystemObject created"); 
	}

	try {
	    Class.forName("com.ibm.as400.access.ProfileHandleImplNative"); 
	    profileHandleImplNativeAvailable=true; 
	} catch (Exception e) {
	    profileHandleImplNativeAvailable=false; 
	} 
    } 

    /**
     Test serialization and restoration of an uninitialized profile handle.
     **/
    public void Var001()
    {
        try
        {
            ProfileHandleCredential ph1 = null;
            ProfileHandleCredential ph2 = null;

            // Create a ProfileHandleCredential.
            ph1 = new ProfileHandleCredential();

            // Serialize the handle.
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("phandle.dat"));
            out.writeObject(ph1);
            out.close();

            // Deserialize the token.
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("phandle.dat"));
            ph2 = (ProfileHandleCredential)in.readObject();
            in.close();

            // Test token attributes.
            assertCondition(ph2.equals(ph1) && ph2.getSystem() == null && ph2.getPrincipal() == null && ph2.getHandle() == null, "Unexpected attribute value.");

            File fd = new File("phandle.dat");
            fd.delete();
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test serialization and restoration of an active profile handle.
     Only applicable if running on the local host.
     **/
    public void Var002()
    {
        AS400 sys = null;
        if (isLocal_)
        {
            sys = new AS400("localhost", "*CURRENT", "*CURRENT");
        }
        else
        {
            sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1);
        }
        try
        {
            if (!isNative_ || !profileHandleImplNativeAvailable )
            {
                notApplicable();
                return;
            }
            // Create objects.
            ProfileHandleCredential ph1 = null;
            ProfileHandleCredential ph2 = null;

            // Create a ProfileHandleCredential.
            ph1 = new ProfileHandleCredential();
            ph1.setSystem(sys);
            ph1.setHandle();

            // Serialize the handle.
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("phandle.dat"));
            out.writeObject(ph1);
            out.close();

            // Deserialize the handle.
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("phandle.dat"));
            ph2 = (ProfileHandleCredential)in.readObject();
            in.close();

            // Reset the system user and password (not serialized).
            if (isLocal_)
            {
                ph2.getSystem().setUserId("*CURRENT");
                ph2.getSystem().setPassword("*CURRENT");
            }
            else
            {
                ph2.getSystem().setUserId(SecAuthTest.uid1);
                ph2.getSystem().setPassword(SecAuthTest.pwd1);
            }

            // Test validity; compare the handle bytes.
            assertCondition(ph2.equals(ph1), "Unexpected attribute value.");

            File fd = new File("phandle.dat");
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
                "propertyChange",
                "as400Credential"
            };
            EventSetDescriptor[] descs = new ProfileHandleCredentialBeanInfo().getAdditionalBeanInfo()[0].getEventSetDescriptors();
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
                "current",
                "destroyed",
                "renewable",
                "timed",
                "timeToExpiration",
                "system",
                "principal"
            };
            PropertyDescriptor[] descs1 = new ProfileHandleCredentialBeanInfo().getAdditionalBeanInfo()[0].getPropertyDescriptors();
            String[] names2 =
            {
                "handle"
            };
            PropertyDescriptor[] descs2 = new ProfileHandleCredentialBeanInfo().getPropertyDescriptors();

            assertCondition(SecAuthTest.verifyDescriptors(names1, descs1) && SecAuthTest.verifyDescriptors(names2, descs2), "Expected property not found.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
