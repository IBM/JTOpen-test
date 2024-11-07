///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecPHActionTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sec;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.Arrays;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.security.auth.AS400Credential;
import com.ibm.as400.security.auth.AS400CredentialEvent;
import com.ibm.as400.security.auth.AS400CredentialListener;
import com.ibm.as400.security.auth.DestroyFailedException;
import com.ibm.as400.security.auth.ProfileHandleCredential;
import com.ibm.as400.security.auth.ProfileTokenCredential;
import com.ibm.as400.security.auth.RetrieveFailedException;
import com.ibm.as400.security.auth.SwapFailedException;

import test.SecAuthTest;
import test.Testcase;

/**
 Testcase SecPHActionTestcase.
 <p>Test variations for the following actions:
 <ul>
 <li>refresh
 <li>startAutomaticRefresh
 <li>stopAutomaticRefresh
 <li>swap
 <li>swapNoException
 <li>destroy
 </ul>
 <p>Test variations for the following related events:
 <ul>
 <li>created
 <li>refreshed
 <li>swapped
 <li>destroyed
 </ul>
 **/
public class SecPHActionTestcase extends Testcase implements AS400CredentialListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SecPHActionTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SecAuthTest.main(newArgs); 
   }
    private static int refreshCount_ = 0;
    private static AS400CredentialEvent ace_ = null;
    AS400 nativeSystemObject = null;
    private boolean profileHandleImplNativeAvailable = false; 

    /**
     Invoked when a create has been performed.
     @param  event  The credential event.
     **/
    public void created(AS400CredentialEvent event)
    {
        ace_ = event;
    }

    /**
     Invoked when a destroy has been performed.
     @param  event  The credential event.
     **/
    public void destroyed(AS400CredentialEvent event)
    {
        ace_ = event;
    }

    /**
     Invoked when a refresh has been performed.
     @param  event  The credential event.
     **/
    public void refreshed(AS400CredentialEvent event)
    {
        ace_ = event;
        ++refreshCount_;
    }

    /**
     Reset all internally maintained state indicators.
     **/
    private void resetState()
    {
        ace_ = null;
        refreshCount_ = 0;
    }

    /**
     Invoked when a credential has been used to change the OS/400 thread identity.
     @param  event  The credential event.
     **/
    public void swapped(AS400CredentialEvent event)
    {
        ace_ = event;
    }


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
     Test ignored refresh.
     **/
    public void Var001()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1);
            // Reset internal state.
            resetState();
            // Perform test.
            int t = pt.getTimeToExpiration();
            Thread.sleep(2000);
            pt.refresh();
            assertCondition(pt.getTimeToExpiration() < t && ace_ == null, "Time to expiration not decreased.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed autorefresh of a non-renewable token.
     **/
    public void Var002()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            char[] password = SecAuthTest.pwd1.toCharArray();
            pt.setTokenExtended(SecAuthTest.uid1, password);
            Arrays.fill(password, '\0'); 
            // Reset internal state.
            resetState();
            // Perform test.
            try
            {
                pt.startAutomaticRefresh(60, 3);
                failed("Expected exception not signalled.");
            }
            catch (IllegalStateException ise)
            {
                assertCondition(pt.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_NOT_VALID && ace_ == null && refreshCount_ == 0, "Unexpected refresh event signalled.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful swap if running on the local host; otherwise verify the exception. Verify no return credential is returned even if requested.
     **/
    public void Var003()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.setTokenExtended(SecAuthTest.uid2, SecAuthTest.pwd2);
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setSystem(systemObject_);
            ph.addCredentialListener(this);
            if (isNative_  && profileHandleImplNativeAvailable)
            {
                ph.setHandle();
            }
            else
            {
                ph.setHandle(new byte[ProfileHandleCredential.HANDLE_LENGTH]);
            }

            // Reset internal state.
            resetState();

            // Perform test.
            try
            {
                String originalID = null;
                if (isNative_ && profileHandleImplNativeAvailable) originalID = new AS400().getUserId();
                // Swap to alternate ID.
                pt.swap(false);
                // Swap back.
                AS400Credential cr = ph.swap(true);
                assertCondition(originalID.equals(new AS400().getUserId()) && ace_ != null && ace_.getID() == AS400CredentialEvent.CR_SWAP && cr == null, "Swap failed.");
            }
            catch (SwapFailedException sfe)
            {
                assertCondition((!isNative_ || !profileHandleImplNativeAvailable) && sfe.getReturnCode() == AS400SecurityException.REQUEST_NOT_SUPPORTED, "Swap failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful swap (generic/no parms) if running on the local host.
     **/
    public void Var004()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.setTokenExtended(SecAuthTest.uid2, SecAuthTest.pwd2);
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setSystem(systemObject_);
            ph.addCredentialListener(this);
            if (isNative_ && profileHandleImplNativeAvailable)
            {
                ph.setHandle();
            }
            else
            {
                ph.setHandle(new byte[ProfileHandleCredential.HANDLE_LENGTH]);
            }

            // Reset internal state.
            resetState();

            // Perform test.
            String originalID = null;
            if (isNative_ && profileHandleImplNativeAvailable) originalID = new AS400().getUserId();
            try
            {
                // Swap to alternate ID.
                pt.swap();
                if (SecAuthTest.uid2.equals(new AS400().getUserId()))
                {
                    // Swap back.
                    ph.swap();
                    assertCondition((originalID.equals(new AS400().getUserId()) && ace_ != null && ace_.getID() == AS400CredentialEvent.CR_SWAP) || !isNative_ || !profileHandleImplNativeAvailable, "Unexpected result.");
                }
                else
                {
                    failed("Initial swap did not occur.");
                }
            }
            catch (SwapFailedException sfe)
            {
                assertCondition(!isNative_ || !profileHandleImplNativeAvailable, "Unexpected swap exception.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed swap for a credential that was previously destroyed.
     **/
    public void Var005()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setSystem(systemObject_);
            ph.addCredentialListener(this);
            ph.setHandle(new byte[ProfileHandleCredential.HANDLE_LENGTH]);
            ph.destroy();

            // Reset internal state.
            resetState();

            // Perform test.
            String originalID = null;
            if (isNative_ && profileHandleImplNativeAvailable) originalID = new AS400().getUserId();
            try
            {
                ph.swap(false);
                failed("Expected exception not signalled. originalId is "+originalID);
            }
            catch (ExtendedIllegalStateException ise)
            {
                assertCondition(ise.getReturnCode() == ExtendedIllegalStateException.PROPERTY_NOT_SET, "Expected return code not found.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed swap (generic/no parms) for a credential that was previously destroyed.
     **/
    public void Var006()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setSystem(systemObject_);
            ph.addCredentialListener(this);
            ph.setHandle(new byte[ProfileHandleCredential.HANDLE_LENGTH]);
            ph.destroy();

            // Reset internal state.
            resetState();

            // Perform test.
            String originalID = null;
            if (isNative_ && profileHandleImplNativeAvailable) originalID = new AS400().getUserId();
            try
            {
                ph.swap();
            }
            catch (ExtendedIllegalStateException ise)
            {
                assertCondition((!isNative_ || !profileHandleImplNativeAvailable || originalID.equals(new AS400().getUserId())) && ace_ == null && ise.getReturnCode() == ExtendedIllegalStateException.PROPERTY_NOT_SET, "Unexpected exception during swap.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful swap if running on the local host; otherwise not applicable.  Specify the host as "localhost" instead of by name.
     **/
    public void Var007()
    {
        try
        {
            // Check if system where test profiles were created is local.
	    
            if (!isLocal_ || !profileHandleImplNativeAvailable )
            {
                notApplicable("Requires local testcase and jt400Native.jar");
                return;
            }
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ProfileTokenCredential pt = new ProfileTokenCredential();
            AS400 sys = new AS400("localhost", "*CURRENT", "*CURRENT");
            String originalID = new AS400().getUserId();
            ph.setSystem(sys);
            ph.addCredentialListener(this);
            ph.setHandle();
            pt.setSystem(sys);
            pt.setTokenExtended(SecAuthTest.uid2, SecAuthTest.pwd2);

            // Reset internal state.
            resetState();

            // Perform test.
            try
            {
                // Swap.
                pt.swap(false);
                if (SecAuthTest.uid2.equals(new AS400().getUserId()))
                {
                    // Swap back.
                    AS400Credential cr = ph.swap(false);
                    assertCondition(originalID.equals(new AS400().getUserId()) && ace_ != null && ace_.getID() == AS400CredentialEvent.CR_SWAP && cr == null, "Unexpected result.");
                }
                else
                {
                    failed("Initial swap failed.");
                }
            }
            catch (SwapFailedException sfe)
            {
                failed(sfe, "Unexpected swap exception.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful destroy of an existing credential if running on the local host; otherwise verify the exception.
     **/
    public void Var008()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setSystem(systemObject_);
            ph.addCredentialListener(this);
            try
            {
                ph.setHandle();
            }
            catch (RetrieveFailedException rfe)
            {
                // Ignore unsupported retrieve if not running native.
                if ((isNative_ && profileHandleImplNativeAvailable)|| rfe.getReturnCode() != AS400SecurityException.REQUEST_NOT_SUPPORTED)
                {
                    throw rfe;
                }
            }
            // Reset internal state.
            resetState();
            try
            {
                // Perform test.
                ph.destroy();
                assertCondition(ph.isDestroyed() && ace_ != null && ace_.getID() == AS400CredentialEvent.CR_DESTROY, "Unexpected failure.");
            }
            catch (DestroyFailedException dfe)
            {
                assertCondition((!isNative_ || !profileHandleImplNativeAvailable) && dfe.getReturnCode() == AS400SecurityException.REQUEST_NOT_SUPPORTED, "Destroy failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful destroy of previously destroyed credential.
     **/
    public void Var009()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setSystem(systemObject_);
            ph.addCredentialListener(this);
            // Force a connection (associate an impl).
            try
            {
                ph.swap();
            }
            catch (Exception se)
            {
            }

            // Destroy the credential.
            ph.destroy();
            // Reset internal state.
            resetState();
            // Perform test.
            ph.destroy();
            assertCondition(ph.isDestroyed() && ace_ != null && ace_.getID() == AS400CredentialEvent.CR_DESTROY, "Unexpected failure.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful create & corresponding event if running on the local host; otherwise verify the exception.
     **/
    public void Var010()
    {
        try
        {
            // Reset internal state.
            resetState();
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setSystem(systemObject_);
            ph.addCredentialListener(this);
            try
            {
                // Perform test.
                ph.setHandle();
                assertCondition(ace_ != null && ace_.getID() == AS400CredentialEvent.CR_CREATE, "Expected event not signalled.");
            }
            catch (RetrieveFailedException rfe)
            {
                assertCondition((!isNative_ || !profileHandleImplNativeAvailable) && rfe.getReturnCode() == AS400SecurityException.REQUEST_NOT_SUPPORTED, "Create failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test additional of null property change listener.
     **/
    public void Var011()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            PropertyChangeListener l = null;
            try
            {
                ph.addPropertyChangeListener(l);
                failed("Expected exception not signalled.");
            }
            catch (NullPointerException npe)
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test removal of null property change listener.
     **/
    public void Var012()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            PropertyChangeListener l = null;
            ph.removePropertyChangeListener(l);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test additional of null vetoable change listener.
     **/
    public void Var013()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            VetoableChangeListener l = null;
            try
            {
                ph.addVetoableChangeListener(l);
                failed("Expected exception not signalled.");
            }
            catch (NullPointerException npe)
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test removal of null vetoable change listener.
     **/
    public void Var014()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            VetoableChangeListener l = null;
            ph.removeVetoableChangeListener(l);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test addition of null credential listener.
     **/
    public void Var015()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            AS400CredentialListener l = null;
            try
            {
                ph.addCredentialListener(l);
                failed("Expected exception not signalled.");
            }
            catch (NullPointerException npe)
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test removal of null credential listener.
     **/
    public void Var016()
    {
        try
        {
            ProfileHandleCredential ph = new ProfileHandleCredential();
            AS400CredentialListener l = null;
            ph.removeCredentialListener(l);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test unsuccessful destroy of a previously destroyed credential if running on the local host; otherwise verify the exception.
     **/
    public void Var017()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setSystem(systemObject_);
            ph.addCredentialListener(this);
            try
            {
                ph.setHandle();
            }
            catch (RetrieveFailedException rfe)
            {
                // Ignore unsupported retrieve if not running native.
                if ((isNative_  && profileHandleImplNativeAvailable)|| rfe.getReturnCode() != AS400SecurityException.REQUEST_NOT_SUPPORTED)
                {
                    throw rfe;
                }
                notApplicable();
                return;
            }
            // Save the handle bytes.
            byte[] bytes = ph.getHandle();
            // Destroy the handle.
            ph.destroy();
            // Reset internal state.
            resetState();
            // Recreate new credential with destroyed handle bytes.
            ph = new ProfileHandleCredential();
            ph.setSystem(systemObject_);
            ph.setHandle(bytes);
            ph.addCredentialListener(this);
            // Force a connection (associate an impl).
            try
            {
                ph.swap();
            }
            catch (Exception se)
            {
            }
            // Perform test.
            try
            {
                ph.destroy();
                failed("Destroy did not fail as expected.");
            }
            catch (DestroyFailedException dfe)
            {
                AS400Message msg = dfe.getAS400Message();
                if (msg == null)
                {
                    failed("Expected error not signalled.");
                }
                else
                {
                    assertCondition(msg.getID().equals("CPF22E7"), "Error " + msg.getID() + " not expected.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
