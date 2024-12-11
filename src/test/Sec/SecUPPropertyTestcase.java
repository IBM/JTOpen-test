///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecUPPropertyTestcase.java
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

import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.User;
import com.ibm.as400.security.auth.UserProfilePrincipal;

import test.SecAuthTest;
import test.Testcase;

/**
 SecUPPropertyTestcase contains test variations for the following properties for the UserProfilePrincipal object:
 <ul>
 <li>system.
 <li>name.
 <li>userProfileName.
 <li>user.
 </ul>
 **/
public class SecUPPropertyTestcase extends Testcase implements PropertyChangeListener, VetoableChangeListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SecUPPropertyTestcase";
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
        pce_ = evt;
    }

    /**
     This method gets called when a constrained property is changed.
     @param  evt  a <code>PropertyChangeEvent</code> object describing the event source and the property that has changed.
     @exception  PropertyVetoException  If the recipient wishes the property change to be rolled back.
     **/
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException
    {
        if (vetoChange_)
        {
            throw new PropertyVetoException("test", evt);
        }
        pce_ = evt;
    }

    public void setup() throws Exception {
      SecAuthTest.createProfiles(pwrSys_); 
    } 

    public void cleanup() throws Exception {
      SecAuthTest.deleteProfiles(pwrSys_); 
    } 

    /**
     Test default value for system.
     **/
    public void Var001()
    {
        try
        {
            UserProfilePrincipal upp = new UserProfilePrincipal();
            assertCondition(upp.getSystem() == null, "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test property change for system.
     **/
    public void Var002()
    {
        try
        {
            // Create objects.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            // Set up property change listener and event.
            upp.addPropertyChangeListener(this);
            pce_ = null;
            // Assign the property value.
            upp.setSystem(systemObject_);
            // Test the result.
            if (pce_ != null)
            {
                assertCondition(systemObject_.equals(pce_.getNewValue()) && systemObject_.equals(upp.getSystem()), "Property change failed.");
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
    public void Var003()
    {
        try
        {
            // Create objects.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            // Set up vetoable change listener and event.
            upp.addVetoableChangeListener(this);
            vetoChange_ = true;
            // Assign the property value.
            try
            {
                upp.setSystem(systemObject_);
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
     Test default value for userProfileName.
     **/
    public void Var004()
    {
        try
        {
            UserProfilePrincipal upp = new UserProfilePrincipal();
            assertCondition(upp.getUserProfileName().equals(""), "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test property change for userProfileName.
     **/
    public void Var005()
    {
        try
        {
            // Create objects.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            // Set up property change listener and event.
            upp.addPropertyChangeListener(this);
            pce_ = null;
            // Assign the property value.
            upp.setUserProfileName(SecAuthTest.uid2);
            // Test the result.
            if (pce_ != null)
            {
                assertCondition(SecAuthTest.uid2.equals(pce_.getNewValue()) && SecAuthTest.uid2.equals(upp.getUserProfileName()), "Property change failed.");
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
     Test vetoed change for userProfileName.
     **/
    public void Var006()
    {
        try
        {
            // Create objects.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            // Set up vetoable change listener and event.
            upp.addVetoableChangeListener(this);
            vetoChange_ = true;
            // Assign the property value.
            try
            {
                upp.setUserProfileName(SecAuthTest.uid2);
                failed("PropertyVetoException not signalled.");
            }
            catch (PropertyVetoException pve)
            {
                // Test the result.
                assertCondition(SecAuthTest.uid2.equals(pve.getPropertyChangeEvent().getNewValue()) && upp.getUserProfileName().equals(pve.getPropertyChangeEvent().getOldValue()), "Veto of property change failed.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test default value for name.
     **/
    public void Var007()
    {
        try
        {
            UserProfilePrincipal upp = new UserProfilePrincipal();
            assertCondition(upp.getName().equals(""), "Incorrect default value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test value of name after assignment of userProfileName.
     **/
    public void Var008()
    {
        try
        {
            // Create objects.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            // Set up property change listener and event.
            upp.addPropertyChangeListener(this);
            pce_ = null;
            // Assign the property value.
            upp.setUserProfileName(SecAuthTest.uid2);
            // Test the result.
            if (pce_ != null)
            {
                assertCondition(SecAuthTest.uid2.equals(pce_.getNewValue()) && SecAuthTest.uid2.equals(upp.getName()), "Property change failed.");
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
     Test retrieving user for uninitialized principal.
     **/
    public void Var009()
    {
        try
        {
            // Create objects.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            // Test the result.
            try
            {
                upp.getUser();
                failed("Unexpected user returned.");
            }
            catch (ExtendedIllegalStateException ise)
            {
                assertCondition(ise.getReturnCode() == ExtendedIllegalStateException.PROPERTY_NOT_SET, "Unexpected return code.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test retrieving user for initialized principal.
     **/
    public void Var010()
    {
        try
        {
          StringBuffer sb = new StringBuffer(); 
          boolean passed = true; 
          
            // Create objects.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            upp.setUserProfileName(SecAuthTest.uid2);
            upp.setSystem(pwrSys_);

            // Test the result.
            User user = upp.getUser();
            if (user == null) { 
              passed= false; sb.append("user is null\n"); 
            } else {
              if (! user.getName().equals(upp.getUserProfileName()) ) {
                passed = false; 
                sb.append("user.getName()="+user.getName()+" sb "+upp.getUserProfileName()+"\n"); 
              }
              if (!user.getSystem().equals(upp.getSystem())) { 
                passed = false; 
                sb.append("user.getSystem()="+user.getSystem()+" sb "+upp.getSystem());
                
              }
              if (!user.getStatus().equals("*ENABLED")) { 
                passed=false; 
                sb.append("user.getStatus()="+user.getStatus()+" sb *ENABLED\n"); 
              }
            }
            assertCondition(passed, sb);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test retrieving user for a principal with only a system specified.
     **/
    public void Var011()
    {
        try
        {
            // Create objects.
            UserProfilePrincipal upp = new UserProfilePrincipal();
            upp.setSystem(systemObject_);
            // Test the result.
            upp.getUser();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
