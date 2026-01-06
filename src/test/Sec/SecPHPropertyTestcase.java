///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecPHPropertyTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sec;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Random;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.security.auth.AS400Credential;
import com.ibm.as400.security.auth.ProfileHandleCredential;
import com.ibm.as400.security.auth.UserProfilePrincipal;

import test.SecAuthTest;
import test.Testcase;

/**
 SecPHPropertyTestcase contains test variations for the following properties of the ProfileHandleCredential object:
 <ul>
 <li>principal
 <li>system
 <li>private
 <li>renewable
 <li>timed
 <li>handle
 <li>current
 <li>destroyed
 <li>timeToExpiration
 <li>automaticRefreshStatus
 <li>automaticRefreshFailure
 </ul>
 **/
public class SecPHPropertyTestcase extends Testcase implements PropertyChangeListener, VetoableChangeListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SecPHPropertyTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SecAuthTest.main(newArgs); 
   }
    private PropertyChangeEvent pce_ = null;
    private boolean vetoChange_ = false;
    AS400 nativeSystemObject = null;
    private boolean profileHandleImplNativeAvailable = false; 
    /**
     This method gets called when a bound property is changed.
     @param  evt  A PropertyChangeEvent object describing the event source and the property that has changed.
     **/
    public void propertyChange(PropertyChangeEvent evt)
    {
        pce_ = evt;
    }

    /**
     This method gets called when a constrained property is changed.
     @param  evt  a <code>PropertyChangeEvent</code> object describing the event source and the property that has changed.
     @exception  PropertyVetoException  if the recipient wishes the property change to be rolled back.
     **/
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException
    {
        if (vetoChange_) throw new PropertyVetoException("test", evt);
        pce_ = evt;
    }

    public void setup() {
	if (isNative_) {
	    nativeSystemObject = new AS400(); 
	    output_.println("nativeSystemObject created"); 
	}

	try {
	    Class.forName("com.ibm.as400.access.ProfileHandleImplNative"); 
	    profileHandleImplNativeAvailable=true; 
	} catch (Exception e) {
	    profileHandleImplNativeAvailable=false; 
	} 
    } 
    /**
     Test default value for principal.
     **/
    public void Var001()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            assertCondition(ph.getPrincipal() == null, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test property change for principal.
     **/
    public void Var002()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            UserProfilePrincipal up = new UserProfilePrincipal(systemObject_, "TEST");
            // Set up property change listener and event.
            ph.addPropertyChangeListener(this);
            pce_ = null;
            // Assign the property value.
            ph.setPrincipal(up);
            // Test the result.
            if (pce_ != null)
            {
                assertCondition(up.equals(pce_.getNewValue()) && up.equals(ph.getPrincipal()), "Property change failed.");
            }
            else
            {
                failed("Property change event not received.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test vetoed change for principal.
     **/
    public void Var003()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            UserProfilePrincipal up = new UserProfilePrincipal(systemObject_, "TEST");
            // Set up vetoable change listener and event.
            ph.addVetoableChangeListener(this);
            vetoChange_ = true;
            // Assign the property value.
            try
            {
                ph.setPrincipal(up);
                failed("PropertyVetoException not signalled.");
            }
            catch (PropertyVetoException pve)
            {
                // Test the result.
                assertCondition(up.equals(pve.getPropertyChangeEvent().getNewValue()) && pve.getPropertyChangeEvent().getOldValue() == null, "Veto of property change failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for system.
     **/
    public void Var004()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            assertCondition(ph.getSystem() == null, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test property change for system.
     **/
    public void Var005()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            // Set up property change listener and event.
            ph.addPropertyChangeListener(this);
            pce_ = null;
            // Assign the property value.
            ph.setSystem(systemObject_);
            // Test the result.
            if (pce_ != null)
            {
                assertCondition(systemObject_.equals(pce_.getNewValue()) && systemObject_.equals(ph.getSystem()), "Property change failed.");
            }
            else
            {
                failed("Property change event not received.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test vetoed change for system.
     **/
    public void Var006()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            // Set up vetoable change listener and event.
            ph.addVetoableChangeListener(this);
            vetoChange_ = true;
            // Assign the property value.
            try
            {
                ph.setSystem(systemObject_);
                failed("PropertyVetoException not signalled.");
            }
            catch (PropertyVetoException pve)
            {
                // Test the result.
                assertCondition(systemObject_.equals(pve.getPropertyChangeEvent().getNewValue()) && pve.getPropertyChangeEvent().getOldValue() == null, "Veto of property change failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for private.
     **/
    public void Var007()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            assertCondition(ph.isPrivate(), "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for renewable.
     **/
    public void Var008()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            assertCondition(!ph.isRenewable(), "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for timed.
     **/
    public void Var009()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            assertCondition(!ph.isTimed(), "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for handle.
     **/
    public void Var010()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            assertCondition(ph.getHandle() == null, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test property change for handle (direct assignment).
     **/
    public void Var011()
    {
        try
        {
            // Create a bogus handle value.
            byte[] hBytes = new byte[ProfileHandleCredential.HANDLE_LENGTH];
            new Random().nextBytes(hBytes);
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            // Set up property change listener and event.
            ph.addPropertyChangeListener(this);
            pce_ = null;
            // Assign the property value.
            ph.setHandle(hBytes);
            // Test the result.
            if (pce_ != null)
            {
                assertCondition(SecAuthTest.compareBytes(hBytes, (byte[])pce_.getNewValue()) && SecAuthTest.compareBytes(hBytes, ph.getHandle()), "Property change failed.");
            }
            else
            {
                failed("Property change event not received.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test vetoed change for handle (direct assignment).
     **/
    public void Var012()
    {
        try
        {
            // Create a bogus handle value.
            byte[] hBytes = new byte[ProfileHandleCredential.HANDLE_LENGTH];
            new Random().nextBytes(hBytes);
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            // Set up vetoable change listener and event.
            ph.addVetoableChangeListener(this);
            vetoChange_ = true;
            // Assign the property value.
            try
            {
                ph.setHandle(hBytes);
                failed("PropertyVetoException not signalled.");
            }
            catch (PropertyVetoException pve)
            {
                // Test the result.
                assertCondition(SecAuthTest.compareBytes(hBytes, (byte[])pve.getPropertyChangeEvent().getNewValue()) && pve.getPropertyChangeEvent().getOldValue() == null, "Veto of property change failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test property change for handle (assigned from current thread).
     Only applicable if running on the local host.
     Only works if the object is the same as the current user 
     **/
    public void Var013()
    {
        try
        {
            if (!isNative_ || !profileHandleImplNativeAvailable)
            {
                notApplicable("Not native or profileHandleImplNative not available -- must use jt400native.jar");
                return;
            }
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setSystem(nativeSystemObject);
            // Set up property change listener and event.
            ph.addPropertyChangeListener(this);
            pce_ = null;
            // Assign the property value.
            ph.setHandle();
            // Test the result.
            if (pce_ != null)
            {
                assertCondition(pce_.getNewValue() != null && SecAuthTest.compareBytes((byte[])pce_.getNewValue(), ph.getHandle()), "Property change failed.");
            }
            else
            {
                failed("Property change event not received.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test vetoed change for token (assigned from current thread).
     Only applicable if running on the local host.
     **/
    public void Var014()
    {
        try
        {
            if (!isNative_  || !profileHandleImplNativeAvailable)
            {
                notApplicable("Not native or profileHandleImplNative not available -- must use jt400native.jar");
                return;
            }
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setSystem(sameSys_);
            // Set up vetoable change listener and event.
            ph.addVetoableChangeListener(this);
            vetoChange_ = true;
            // Assign the property value.
            try
            {
                ph.setHandle();
                failed("PropertyVetoException not signalled.");
            }
            catch (PropertyVetoException pve)
            {
                // Test the result.
                assertCondition(pve.getPropertyChangeEvent().getNewValue() != null && (ph.getHandle() == pve.getPropertyChangeEvent().getOldValue()), "Veto of property change failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test illegal property change for principal based on state.
     **/
    public void Var015()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            UserProfilePrincipal up = new UserProfilePrincipal(systemObject_, "TEST");
            ph.setHandle(new byte[ProfileHandleCredential.HANDLE_LENGTH]);
            ph.setSystem(systemObject_);
            // Force a connection (associate an impl).
            try
            {
                ph.swap();
            }
            catch (Exception se)
            {
            }
            // Assign the property value.
            try
            {
                ph.setPrincipal(up);
                failed("Illegal property change not disallowed.");
            }
            catch (ExtendedIllegalStateException ise)
            {
                // Test the result.
                assertCondition(ise.getReturnCode() == ExtendedIllegalStateException.PROPERTY_NOT_CHANGED, "Unexpected return code for ExtendedIllegalStateException >> " + ise.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test illegal property change for system based on state.
     **/
    public void Var016()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setHandle(new byte[ProfileHandleCredential.HANDLE_LENGTH]);
            ph.setSystem(systemObject_);
            // Force a connection (associate an impl).
            try
            {
                ph.swap();
            }
            catch (Exception se)
            {
            }
            // Assign the property value.
            try
            {
                ph.setSystem(new AS400());
                failed("Illegal property change not disallowed.");
            }
            catch (ExtendedIllegalStateException ise)
            {
                // Test the result.
                assertCondition(ise.getReturnCode() == ExtendedIllegalStateException.PROPERTY_NOT_CHANGED, "Unexpected return code for ExtendedIllegalStateException >> " + ise.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test illegal property change for handle based on state.
     **/
    public void Var017()
    {
        try
        {
            // Create a bogus handle value.
            byte[] hBytes = new byte[ProfileHandleCredential.HANDLE_LENGTH];
            new Random().nextBytes(hBytes);
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setHandle(new byte[ProfileHandleCredential.HANDLE_LENGTH]);
            ph.setSystem(systemObject_);
            // Force a connection (associate an impl).
            try
            {
                ph.swap();
            }
            catch (Exception se)
            {
            }
            // Assign the property value.
            try
            {
                ph.setHandle(hBytes);
                failed("Illegal property change not disallowed.");
            }
            catch (ExtendedIllegalStateException ise)
            {
                // Test the result.
                assertCondition(ise.getReturnCode() == ExtendedIllegalStateException.PROPERTY_NOT_CHANGED, "Unexpected return code for ExtendedIllegalStateException >> " + ise.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for current.
     **/
    public void Var018()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            // Perform the test.
            assertCondition(!ph.isCurrent(), "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test currency of valid handle.
     Only applicable if running on the local host.
     **/
    public void Var019()
    {
        try
        {
            if (!isNative_ || !profileHandleImplNativeAvailable)
            {
                notApplicable("Not native or profileHandleImplNative not available -- must use jt400native.jar");
                return;
            }
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            // Activate the credential.
            ph.setSystem(sameSys_);
            ph.setHandle();
            // Test the result.
            assertCondition(ph.isCurrent(), "Incorrect value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test currency of a non-valid handle.
     **/
    public void Var020()
    {
        try
        {
            // Create a bogus handle value.
            byte[] hBytes = new byte[ProfileHandleCredential.HANDLE_LENGTH];
            new Random().nextBytes(hBytes);
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            // Activate the credential.
            ph.setSystem(systemObject_);
            ph.setHandle(hBytes);
            // Test the result.
            assertCondition(ph.isCurrent(), "Incorrect value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for destroyed.
     **/
    public void Var021()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            assertCondition(ph.isDestroyed(), "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test destroyed status of valid handle.
     Only applicable if running on the local host.
     **/
    public void Var022()
    {
        try
        {
            if (!isNative_ || !profileHandleImplNativeAvailable)
            {
                notApplicable("Not native or profileHandleImplNative not available -- must use jt400native.jar");
                return;
            }
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            // Activate the credential.
            ph.setSystem(sameSys_);
            ph.setHandle();
            // Test the result.
            assertCondition(!ph.isDestroyed(), "Incorrect value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test destroyed status of destroyed handle.
     Only applicable if running on the local host.
     **/
    public void Var023()
    {
        try
        {
            if (!isNative_ || !profileHandleImplNativeAvailable)
            {
                notApplicable("Not native or profileHandleImplNative not available -- must use jt400native.jar");
                return;
            }
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            // Activate the credential.
            ph.setSystem(sameSys_);
            ph.setHandle();
            // Destroy the credential.
            ph.destroy();
            // Test the result.
            assertCondition(ph.isDestroyed(), "Incorrect value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test timeToExpiration of handle.
     Only applicable if running on the local host.
     **/
    public void Var024()
    {
        try
        {
            if (!isNative_ || !profileHandleImplNativeAvailable)
            {
                notApplicable("Not native or profileHandleImplNative not available -- must use jt400native.jar");
                return;
            }
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setSystem(sameSys_);
            ph.setHandle();
            assertCondition(ph.getTimeToExpiration() == 0, "Unexpected time to expiration.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for automaticRefreshStatus.
     **/
    public void Var025()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            assertCondition(ph.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_NOT_VALID, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for automaticRefreshFailure.
     **/
    public void Var026()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            assertCondition(ph.getAutomaticRefreshFailure() == null, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test assignment of non-valid handle value (too small).
     **/
    public void Var027()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            byte[] hBytes = new byte[ProfileHandleCredential.HANDLE_LENGTH - 1];
            new Random().nextBytes(hBytes);
            try
            {
                ph.setHandle(hBytes);
                failed("Expected exception not signalled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.LENGTH_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test assignment of non-valid handle value (too large).
     **/
    public void Var028()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            byte[] hBytes = new byte[ProfileHandleCredential.HANDLE_LENGTH + 1];
            new Random().nextBytes(hBytes);
            try
            {
                ph.setHandle(hBytes);
                failed("Expected exception not signalled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.LENGTH_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
