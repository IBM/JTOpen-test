///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecPTPropertyTestcase.java
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
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.security.auth.AS400Credential;
import com.ibm.as400.security.auth.ProfileTokenCredential;
import com.ibm.as400.security.auth.RefreshFailedException;
import com.ibm.as400.security.auth.RetrieveFailedException;
import com.ibm.as400.security.auth.UserProfilePrincipal;

import test.SecAuthTest;
import test.Testcase;

/**
 SecPTPropertyTestcase contains test variations for the following properties of the ProfileTokenCredential object:
 <ul>
 <li>principal.
 <li>system.
 <li>timeoutInterval.
 <li>private.
 <li>renewable.
 <li>timed.
 <li>token.
 <li>tokenType.
 <li>current.
 <li>destroyed.
 <li>timeToExpiration.
 <li>automaticRefreshStatus.
 <li>automaticRefreshFailure.
 </ul>
 **/
public class SecPTPropertyTestcase extends Testcase implements PropertyChangeListener, VetoableChangeListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SecPTPropertyTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SecAuthTest.main(newArgs); 
   }
    private PropertyChangeEvent pce_ = null;
    private boolean vetoChange_ = false;

    /**
     This method gets called when a bound property is changed.
     @param  evt  A PropertyChangeEvent object describing the event source and the property that has changed.
     **/
    public void propertyChange(PropertyChangeEvent evt)
    {
      String propertyName = evt.getPropertyName();
      if ("tokenCreator".equals(propertyName) || "remoteIPAddress".equals(propertyName)) {
        // Ignore the reordered events
      } else {
        pce_ = evt;
      }
    }

    /**
     This method gets called when a constrained property is changed.
     @param  evt  A <code>PropertyChangeEvent</code> object describing the event source and the property that has changed.
     @exception  PropertyVetoException  If the recipient wishes the property change to be rolled back.
     **/
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
      String propertyName = evt.getPropertyName();
      if ("tokenCreator".equals(propertyName) || "remoteIPAddress".equals(propertyName)) {

        // Ignore the reordered events
      } else {
        if (vetoChange_) {
          throw new PropertyVetoException("test", evt);
        }
        pce_ = evt;
      }
    }

    /**
     Reset all internally maintained state indicators.
     **/
    private void resetState()
    {
        pce_ = null;
        vetoChange_ = false;
    }

    /**
     Test default value for principal.
     **/
    public void Var001()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            assertCondition(pt.getPrincipal() == null, "Incorrect default value.");
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
            ProfileTokenCredential pt = new ProfileTokenCredential();
            UserProfilePrincipal up = new UserProfilePrincipal(systemObject_, "TEST");
            // Set up property change listener.
            pt.addPropertyChangeListener(this);
            // Reset state info.
            resetState();
            // Assign the property value.
            pt.setPrincipal(up);
            // Test the result.
            if (pce_ != null)
            {
                assertCondition(up.equals(pce_.getNewValue()) && up.equals(pt.getPrincipal()), "Property change failed.");
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
            ProfileTokenCredential pt = new ProfileTokenCredential();
            UserProfilePrincipal up = new UserProfilePrincipal(systemObject_, "TEST");
            // Reset state info.
            resetState();
            // Set up vetoable change listener.
            pt.addVetoableChangeListener(this);
            // Indicate to veto the change.
            vetoChange_ = true;
            // Assign the property value.
            try
            {
                pt.setPrincipal(up);
                failed("PropertyVetoException not signaled.");
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
     Test illegal property change for principal based on state.
     **/
    public void Var004()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            UserProfilePrincipal up = new UserProfilePrincipal(systemObject_, "TEST");
            pt.setSystem(systemObject_);
            // Force a connection.
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Assign the property value.
            try
            {
                pt.setPrincipal(up);
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
     Test default value for system.
     **/
    public void Var005()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            assertCondition(pt.getSystem() == null, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test property change for system.
     **/
    public void Var006()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Set up property change listener.
            pt.addPropertyChangeListener(this);
            // Reset state info.
            resetState();
            // Assign the property value.
            pt.setSystem(systemObject_);
            // Test the result.
            if (pce_ != null)
            {
                assertCondition(systemObject_.equals(pce_.getNewValue()) && systemObject_.equals(pt.getSystem()), "Property change failed.");
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
    public void Var007()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Reset state info.
            resetState();
            // Set up vetoable change listener.
            pt.addVetoableChangeListener(this);
            // Indicate to veto the change.
            vetoChange_ = true;
            // Assign the property value.
            try
            {
                pt.setSystem(systemObject_);
                failed("PropertyVetoException not signaled.");
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
     Test illegal property change for system based on state.
     **/
    public void Var008()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            // Force a connection.
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Assign the property value.
            try
            {
                pt.setSystem(new AS400());
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
     Test default value for timeoutInterval.
     **/
    public void Var009()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            assertCondition(pt.getTimeoutInterval() == 3600, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test property change for timeoutInterval.
     **/
    public void Var010()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Set up property change listener.
            pt.addPropertyChangeListener(this);
            // Reset state info.
            resetState();
            // Assign the property value.
            int ti = 100;
            pt.setTimeoutInterval(ti);
            // Test the result.
            if (pce_ != null)
            {
                assertCondition(Integer.valueOf(ti).equals(pce_.getNewValue()) && ti == pt.getTimeoutInterval(), "Property change failed.");
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
     Test vetoed change for timeoutInterval.
     **/
    public void Var011()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Reset state info.
            resetState();
            // Set up vetoable change listener.
            pt.addVetoableChangeListener(this);
            // Indicate to veto the change.
            vetoChange_ = true;
            // Assign the property value.
            int ti = 100;
            try
            {
                pt.setTimeoutInterval(ti);
                failed("PropertyVetoException not signaled.");
            }
            catch (PropertyVetoException pve)
            {
                // Test the result.
                assertCondition(Integer.valueOf(ti).equals(pve.getPropertyChangeEvent().getNewValue()) && Integer.valueOf(pt.getTimeoutInterval()).equals(pve.getPropertyChangeEvent().getOldValue()), "Veto of property change failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test illegal property change for timeoutInterval based on state.
     **/
    public void Var012()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            // Force a connection.
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Assign the property value.
            try
            {
                pt.setTimeoutInterval(100);
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
     Test default value for private.
     **/
    public void Var013()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            assertCondition(pt.isPrivate(), "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for renewable.
     **/
    public void Var014()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            assertCondition(!pt.isRenewable(), "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for timed.
     **/
    public void Var015()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            assertCondition(pt.isTimed(), "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for token.
     **/
    public void Var016()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            assertCondition(pt.getToken() == null, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test property change for token (direct assignment).
     **/
    public void Var017()
    {
        try
        {
            // Create a bogus token value.
            byte[] tBytes = new byte[ProfileTokenCredential.TOKEN_LENGTH];
            new Random().nextBytes(tBytes);
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Set up property change listener.
            pt.addPropertyChangeListener(this);
            // Reset state info.
            resetState();
            // Assign the property value.
            pt.setToken(tBytes);
            // Test the result.
            if (pce_ != null)
            {
                assertCondition(SecAuthTest.compareBytes(tBytes, (byte[])pce_.getNewValue()) && SecAuthTest.compareBytes(tBytes, pt.getToken()), "Property change failed.");
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
     Test vetoed change for token (direct assignment).
     **/
    public void Var018()
    {
        try
        {
            // Create a bogus token value.
            byte[] tBytes = new byte[ProfileTokenCredential.TOKEN_LENGTH];
            new Random().nextBytes(tBytes);
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Reset state info.
            resetState();
            // Set up vetoable change listener.
            pt.addVetoableChangeListener(this);
            // Indicate to veto the change.
            vetoChange_ = true;
            // Assign the property value.
            try
            {
                pt.setToken(tBytes);
                failed("PropertyVetoException not signaled.");
            }
            catch (PropertyVetoException pve)
            {
                // Test the result.
                assertCondition(SecAuthTest.compareBytes(tBytes, (byte[])pve.getPropertyChangeEvent().getNewValue()) && pve.getPropertyChangeEvent().getOldValue() == null, "Veto of property change failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test property change for token (assigned via uid/pwd).
     **/
    public void Var019()
    {
        String propertyName=""; 
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            // Set up property change listener.
            pt.addPropertyChangeListener(this);
            // Reset state info.
            resetState();
            // Assign the property value.
            pce_ = null; 
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Test the result.
            if (pce_ != null)
            {
                propertyName = pce_.getPropertyName(); 
                Object newValue = pce_.getNewValue(); 
                Class<? extends Object> newValueClass = null; 
                if (newValue != null) newValueClass = newValue.getClass(); 
                assertCondition(newValue != null && SecAuthTest.compareBytes((byte[])newValue, pt.getToken()), "Property change failed. newValue="+newValue+" newValueClass="+newValueClass);
            }
            else
            {
                failed("Property change event not received.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception. propertyName="+propertyName);
        }
    }

    /**
     Test vetoed change for token (assigned via uid/pwd).
     **/
    public void Var020()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            // Reset state info.
            resetState();
            // Set up vetoable change listener.
            pt.addVetoableChangeListener(this);
            // Indicate to veto the change.
            vetoChange_ = true;
            // Assign the property value.
            try
            {
                pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
                failed("PropertyVetoException not signaled.");
            }
            catch (PropertyVetoException pve)
            {
                // Test the result.
                PropertyChangeEvent changeEvent = pve.getPropertyChangeEvent(); 
                assertCondition(changeEvent.getNewValue() != null && changeEvent.getOldValue() == null, "Veto of property change failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test illegal property change for token based on state.
     **/
    public void Var021()
    {
        try
        {
            // Create a bogus token value.
            byte[] tBytes = new byte[ProfileTokenCredential.TOKEN_LENGTH];
            new Random().nextBytes(tBytes);
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            // Force a connection.
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Assign the property value.
            try
            {
                pt.setToken(tBytes);
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
     Test default value for tokenType.
     **/
    public void Var022()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            assertCondition(pt.getTokenType() == ProfileTokenCredential.TYPE_SINGLE_USE, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test property change for tokenType.
     **/
    public void Var023()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Set up property change listener.
            pt.addPropertyChangeListener(this);
            // Reset state info
            resetState();
            // Assign the property value.
            int tt = ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE;
            pt.setTokenType(tt);
            // Test the result.
            if (pce_ != null)
            {
                assertCondition(Integer.valueOf(tt).equals(pce_.getNewValue()) && tt == pt.getTokenType(), "Property change failed.");
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
     Test vetoed change for tokenType.
     **/
    public void Var024()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Reset state info.
            resetState();
            // Set up vetoable change listener.
            pt.addVetoableChangeListener(this);
            // Indicate to veto the change.
            vetoChange_ = true;
            // Assign the property value.
            int tt = ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE;
            try
            {
                pt.setTokenType(tt);
                failed("PropertyVetoException not signaled.");
            }
            catch (PropertyVetoException pve)
            {
                // Test the result.
                assertCondition(Integer.valueOf(tt).equals(pve.getPropertyChangeEvent().getNewValue()) && Integer.valueOf(pt.getTokenType()).equals(pve.getPropertyChangeEvent().getOldValue()), "Veto of property change failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test illegal property change for tokenType based on state.
     **/
    public void Var025()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            // Force a connection.
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Assign the property value.
            try
            {
                pt.setTimeoutInterval(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
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
    public void Var026()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            assertCondition(!pt.isCurrent(), "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test currency of valid token.
     **/
    public void Var027()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Activate the credential.
            pt.setSystem(systemObject_);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Test the result.
            assertCondition(pt.isCurrent(), "Incorrect value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test currency of expired token.
     **/
    public void Var028()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Activate the credential.
            pt.setSystem(systemObject_);
            pt.setTimeoutInterval(1);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Sleep until credential is expired.
            Thread.sleep(2000);
            // Test the result.
            assertCondition(!pt.isCurrent(), "Incorrect value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for destroyed.
     **/
    public void Var029()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            assertCondition(pt.isDestroyed(), "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test destroyed status of valid token.
     **/
    public void Var030()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Activate the credential.
            pt.setSystem(systemObject_);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Test the result.
            assertCondition(!pt.isDestroyed(), "Incorrect value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test destroyed status of destroyed token.
     **/
    public void Var031()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Activate the credential.
            pt.setSystem(systemObject_);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Destroy the credential.
            pt.destroy();
            // Test the result.
            assertCondition(pt.isDestroyed(), "Incorrect value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test timeToExpiration of invalid token.
     **/
    public void Var032()
    {
        try
        {
            // Create a bogus token value.
            byte[] tBytes = new byte[ProfileTokenCredential.TOKEN_LENGTH];
            new Random().nextBytes(tBytes);
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.setToken(tBytes);
            try
            {
                pt.getTimeToExpiration();
                failed("Expected exception not signaled.");
            }
            catch (RetrieveFailedException rfe)
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
     Test timeToExpiration of valid token.
     **/
    public void Var033()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Activate the credential.
            pt.setSystem(systemObject_);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Test the result.
            assertCondition(pt.getTimeToExpiration() > 0, "Incorrect value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test timeToExpiration of expired token.
     **/
    public void Var034()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Activate the credential.
            pt.setSystem(systemObject_);
            pt.setTimeoutInterval(1);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Sleep until credential is expired.
            Thread.sleep(2000);
            // Test the result.
            assertCondition(pt.getTimeToExpiration() == 0, "Incorrect value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for automaticRefreshStatus.
     **/
    public void Var035()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            assertCondition(pt.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_NOT_VALID, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for automaticRefreshFailure.
     **/
    public void Var036()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            assertCondition(pt.getAutomaticRefreshFailure() == null, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test for started automaticRefreshStatus.
     **/
    public void Var037()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Activate the credential.
            pt.setSystem(systemObject_);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Start automatic refresh.
            pt.startAutomaticRefresh(60, 3);
            // Test the result.
            assertCondition(pt.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_STARTED, "Incorrect value.");
            // Stop automatic refresh.
            pt.stopAutomaticRefresh();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test for stopped automaticRefreshStatus.
     **/
    public void Var038()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Activate the credential.
            pt.setSystem(systemObject_);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Start automatic refresh.
            pt.startAutomaticRefresh(60, 3);
            // Stop automatic refresh.
            pt.stopAutomaticRefresh();
            // Test the result.
            assertCondition(pt.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_STOPPED, "Incorrect value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test for non-valid automaticRefreshStatus.
     **/
    public void Var039()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Activate the credential.
            pt.setSystem(systemObject_);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Test the result.
            assertCondition(pt.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_NOT_VALID, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test for failed automaticRefreshStatus.
     **/
    public void Var040()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Activate the credential.
            pt.setSystem(systemObject_);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Destroy the credential.
            SecAuthTest.removeToken(pt.getSystem(), pt.getToken());
            // Start automatic refresh.
            pt.startAutomaticRefresh(1, 3);
            // Sleep until automatic refresh has been attempted.
            Thread.sleep(5000);
            // Test the result.
            assertCondition(pt.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_FAILED, "Incorrect value >> " + pt.getAutomaticRefreshStatus());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test for automaticRefreshFailure after failure.
     **/
    public void Var041()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            // Activate the credential.
            pt.setSystem(systemObject_);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray());
            // Destroy the credential.
            SecAuthTest.removeToken(pt.getSystem(), pt.getToken());
            // Start automatic refresh.
            pt.startAutomaticRefresh(1, 3);
            // Sleep until automatic refresh has been attempted.
            Thread.sleep(5000);
            // Test the result.
            assertCondition(pt.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_FAILED && pt.getAutomaticRefreshFailure() instanceof RefreshFailedException && ((RefreshFailedException)pt.getAutomaticRefreshFailure()).getAS400Message().getID().equals("CPF2274"), "Incorrect value >> " + pt.getAutomaticRefreshStatus());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test out of range timeout interval (low end).
     **/
    public void Var042()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            try
            {
                pt.setTimeoutInterval(0);
                failed("Expected exception not signaled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.RANGE_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test out of range timeout interval (high end).
     **/
    public void Var043()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            try
            {
                pt.setTimeoutInterval(3601);
                failed("Expected exception not signaled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.RANGE_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test out of range token type (low end).
     **/
    public void Var044()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            try
            {
                pt.setTokenType(0);
                failed("Expected exception not signaled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.RANGE_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test out of range token type (high end).
     **/
    public void Var045()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            try
            {
                pt.setTokenType(4);
                failed("Expected exception not signaled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.RANGE_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test assignment of non-valid token value (too small).
     **/
    public void Var046()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            byte[] tBytes = new byte[ProfileTokenCredential.TOKEN_LENGTH - 1];
            new Random().nextBytes(tBytes);
            try
            {
                pt.setToken(tBytes);
                failed("Expected exception not signaled.");
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
     Test assignment of non-valid token value (too large).
     **/
    public void Var047()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            byte[] tBytes = new byte[ProfileTokenCredential.TOKEN_LENGTH + 1];
            new Random().nextBytes(tBytes);
            try
            {
                pt.setToken(tBytes);
                failed("Expected exception not signaled.");
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
     Test assignment of token value based on a bad profile name (null).
     **/
    public void Var048()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            try
            {
                String s = null;
                pt.setTokenExtended(s, "PWD".toCharArray());
                failed("Expected exception not signaled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test assignment of token value based on a bad profile name (exceeds max length).
     **/
    public void Var049()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            try
            {
                pt.setTokenExtended("ABCDEFGHIJK", "PWD".toCharArray());
                failed("Expected exception not signaled.");
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
     Test assignment of token value based on a bad password (null).
     **/
    public void Var050()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            try
            {
                char[] s = null;
                pt.setTokenExtended("UID", s);
                failed("Expected exception not signaled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test assignment of token value based on a bad password (exceeds max length).
     **/
    public void Var051()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            try
            {
                pt.setTokenExtended("UID", "ABCDEFGHIJK".toCharArray());
                failed("Expected exception not signaled.");
            }
            catch (RetrieveFailedException e)
            {
                if (isLocal_)
                {
                    succeeded();
                }
                else
                {
		    /* For 7.5 always return PASSWORD_INCORRECT */ 
                    assertCondition(
                     (e.getReturnCode() == AS400SecurityException.PASSWORD_LENGTH_NOT_VALID) ||
                     (e.getReturnCode() == AS400SecurityException.USERID_UNKNOWN)  ||
                     (e.getReturnCode() == AS400SecurityException.PASSWORD_INCORRECT), "Unexpected return code >> " + e.getReturnCode());
                }
                
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
