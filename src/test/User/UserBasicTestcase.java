///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserBasicTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.User;
import com.ibm.as400.resource.RUser;

import test.Testcase;
import test.UserTest;

/**
 Testcase UserBasicTestcase.  This tests the following methods of the User class:
 <ul>
 <li>constructors
 <li>serialization
 <li>getName()
 <li>getSystem()
 <li>loadUserInformation()
 <li>setName()
 <li>setSystem()
 </ul>
 <p>...and the following methods of the RUser class:
 <ul>
 <li>constructors
 <li>serialization
 <li>getName()
 <li>getSystem()
 <li>setName()
 <li>setSystem()
 </ul>
 **/
@SuppressWarnings("deprecation")
public class UserBasicTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserBasicTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }

    String testruser1 = "TESTRUSER1";
    private UserSandbox sandbox_;
    private static boolean areV7R1MethodsDefined_ = true;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {

      String testLib = baseTestDriver_.getTestLib();
      String letter = testLib.substring(testLib.length() - 1);

	testruser1 = generateClientUser("TBTU"+letter); 

        sandbox_ = new UserSandbox(pwrSys_, "UBT", UserTest.COLLECTION.substring(UserTest.COLLECTION.length() - 1));
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        sandbox_.cleanup();
    }

    /**
     constructor() with 0 parms - Should work.
     **/
    public void Var001()
    {
        try
        {
            User u = new User();
            assertCondition(u.getSystem() == null && u.getName() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 2 parms - Pass null for system.
     **/
    public void Var002()
    {
        try
        {
            User u = new User(null, systemObject_.getUserId());
            failed("Didn't throw exception but got "+u);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 2 parms - Pass null for name.
     **/
    public void Var003()
    {
        try
        {
            User u = new User(systemObject_, null);
            failed("Didn't throw exception but got "+u);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 2 parms - Pass invalid values.  This should work, because the constructor does not check the validity.
     **/
    public void Var004()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            User u = new User(bogus, "BadUser");
            assertCondition(u.getSystem() == bogus && u.getName().equals("BADUSER"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 2 parms - Pass valid values.  Verify that the system and name are used.
     **/
    public void Var005()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String textDescription = u.getDescription();
            assertCondition(u.getSystem() == pwrSys_ && u.getName().equals(userName) && textDescription.startsWith(userName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 2 parms - Pass a lowercase user name.  Verify that it is used.
     **/
    public void Var006()
    {
        try
        {
            String userName = sandbox_.createUser().toLowerCase();
            User u = new User(pwrSys_, userName);
            String textDescription = u.getDescription();
            assertCondition(u.getSystem() == pwrSys_ && u.getName().equalsIgnoreCase(userName), "error -- description is "+textDescription);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     serialization - Verify that the properties are set and verify that its usable.
     **/
    public void Var007()
    {
        try
        {
          java.util.Date expDate = null; 
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String textDescription = u.getDescription();
            u.loadUserInformation();
            if (areV7R1MethodsDefined_) {
              expDate = u.getUserExpirationDate();
            }
            User u2 = (User)UserTest.serialize(u);
            String textDescription2 = u2.getDescription();
            assertCondition(u2.getSystem().getSystemName().equals(pwrSys_.getSystemName()) && u2.getSystem().getUserId().equals(pwrSys_.getUserId()) && u2.getName().equals(userName) && textDescription.equals(textDescription2), "expDate is "+expDate);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getName() - Should return null when the name has not been set.
     **/
    public void Var008()
    {
        try
        {
            User u = new User();
            assertCondition(u.getName() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getName() - Should return the name when the name has been set.
     **/
    public void Var009()
    {
        try
        {
            User u = new User(systemObject_, "maine");
            assertCondition(u.getName().equals("MAINE"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSystem() - Should return null when the system has not been set.
     **/
    public void Var010()
    {
        try
        {
            User u = new User();
            assertCondition(u.getSystem() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSystem() - Should return the system when the system has been set.
     **/
    public void Var011()
    {
        try
        {
            User u = new User(systemObject_, "newhampshi");
            assertCondition(u.getSystem().equals(systemObject_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     loadUserInformation() - When no system or name has been specified.
     **/
    public void Var012()
    {
        try
        {
            User u = new User();
            u.loadUserInformation();
            // This should throw a system-isn't-set exception.
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system", ExtendedIllegalStateException.PROPERTY_NOT_SET);
        }
    }

    /**
     loadUserInformation() - When a bogus system has been specified.
     **/
    public void Var013()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            User u = new User(bogus, "javactl");
            assertCondition(true, "user is "+u); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     loadUserInformation() - When a bogus name has been specified.
     **/
    public void Var014()
    {
        try
        {
            User u = new User(pwrSys_, "maryland");
            u.loadUserInformation();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertCondition(e instanceof AS400Exception && ((AS400Exception)e).getAS400Message().getID().equals("CPF9801"), "Incorrect exception info.");
        }
    }

    /**
     loadUserInformation() - Should refresh attributes if they change.
     **/
    public void Var015()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String textDescription = u.getDescription();

            CommandCall cmd = new CommandCall(pwrSys_);
            cmd.run("QSYS/CHGUSRPRF USRPRF(" + userName + ") TEXT('YOU THERE')");

            u.loadUserInformation();
            textDescription = u.getDescription();
            assertCondition(textDescription.equals("YOU THERE"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setName() - Should throw an exception if null is passed.
     **/
    public void Var016()
    {
        try
        {
            User u = new User(systemObject_, "massachuse");
            u.setName(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setName() - Set to an invalid name.  Should be reflected by getName(), since the validity is not checked here.
     **/
    public void Var017()
    {
        try
        {
            User u = new User(systemObject_, "connecticu");
            u.setName("rhodeislan");
            assertCondition(u.getName().equals("RHODEISLAN"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setName() - Set to a valid name.  Should be reflected by getName() and
     verify that it is used.
     **/
    public void Var018()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, "vermont");
            u.setName(userName);
            String textDescription = u.getDescription();
            assertCondition(u.getName().equals(userName) && textDescription.startsWith(userName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setName() - Set to a valid lowercase name.  Should be reflected by getName() and verify that it is used.
     **/
    public void Var019()
    {
        try
        {
            String userName = sandbox_.createUser().toLowerCase();
            User u = new User(pwrSys_, "vermont");
            u.setName(userName);
            String textDescription = u.getDescription();
            assertCondition(u.getName().equalsIgnoreCase(userName), "textDescription="+textDescription);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setName() - Set to a valid name after the User has made a connection.
     **/
    public void Var020()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String textDescription = u.getDescription();
            u.setName("alabama");
            failed("Didn't throw exception "+textDescription);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     setName() - Should fire a property change event.
     **/
    public void Var021()
    {
        try
        {
            User u = new User(systemObject_, "massachuse");
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setName("newyork");
            assertCondition(pcl.eventCount_ == 1 && pcl.event_.getPropertyName().equals("name") && pcl.event_.getOldValue().equals("MASSACHUSE") && pcl.event_.getNewValue().equals("NEWYORK"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setName() - Should fire a vetoable change event.
     **/
    public void Var022()
    {
        try
        {
            User u = new User(systemObject_, "massachuse");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.setName("newyork");
            assertCondition(vcl.eventCount_ == 1 && vcl.event_.getPropertyName().equals("name") && vcl.event_.getOldValue().equals("MASSACHUSE") && vcl.event_.getNewValue().equals("NEWYORK"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setName() - Should throw a PropertyVetoException if the change is vetoed.
     **/
    public void Var023()
    {
        try
        {
            User u = new User(systemObject_, "massachuse");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_(true);
            u.addVetoableChangeListener(vcl);
            u.setName("newyork");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.beans.PropertyVetoException");
        }
    }

    /**
     setSystem() - Should throw an exception if null is passed.
     **/
    public void Var024()
    {
        try
        {
            User u = new User(systemObject_, "virginia");
            u.setSystem(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setSystem() - Set to an invalid system.  Should be reflected by getSystem(), since the validity is not checked here.
     **/
    public void Var025()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            User u = new User(systemObject_, "scarolina");
            u.setSystem(bogus);
            assertCondition(u.getSystem().getSystemName().equals("bogus") && u.getSystem().getUserId().equalsIgnoreCase("bogus"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Set to a valid name.  Should be reflected by getSystem() and verify that it is used.
     **/
    public void Var026()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            String userName = sandbox_.createUser();
            User u = new User(bogus, userName);
            u.setSystem(pwrSys_);
            String textDescription = u.getDescription();
            assertCondition(u.getSystem().getSystemName().equals(pwrSys_.getSystemName()) && u.getSystem().getUserId().equals(pwrSys_.getUserId()) && textDescription.startsWith(userName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Set to a valid name after the User object has made a connection.
     **/
    public void Var027()
    {
        try
        {
            String userName = sandbox_.createUser();
            User u = new User(pwrSys_, userName);
            String textDescription = u.getDescription();
            u.setSystem(systemObject_);
            failed("Didn't throw exception "+textDescription);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     setSystem() - Should fire a property change event.
     **/
    public void Var028()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            User u = new User(temp1, "ncarolina");
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setSystem(temp2);
            assertCondition(pcl.eventCount_ == 1 && pcl.event_.getPropertyName().equals("system") && pcl.event_.getOldValue().equals(temp1) && pcl.event_.getNewValue().equals(temp2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Should fire a vetoable change event.
     **/
    public void Var029()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            User u = new User(temp1, "georgia");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.setSystem(temp2);
            assertCondition(vcl.eventCount_ == 1 && vcl.event_.getPropertyName().equals("system") && vcl.event_.getOldValue().equals(temp1) && vcl.event_.getNewValue().equals(temp2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Should throw a PropertyVetoException if the change is vetoed.
     **/
    public void Var030()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            User u = new User(temp1, "florida");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_(true);
            u.addVetoableChangeListener(vcl);
            u.setSystem(temp2);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.beans.PropertyVetoException");
        }
    }

    /**
     constructor() with 0 parms - Should work.
     **/
    public void Var031()
    {
        try
        {
            RUser u = new RUser();
            assertCondition(u.getSystem() == null && u.getName() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 2 parms - Pass null for system.
     **/
    public void Var032()
    {
        try
        {
            RUser u = new RUser(null, systemObject_.getUserId());
            failed("Didn't throw exception "+u);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 2 parms - Pass null for name.
     **/
    public void Var033()
    {
        try
        {
            RUser u = new RUser(systemObject_, null);
            failed("Didn't throw exception "+u);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 2 parms - Pass invalid values.  This should work, because the constructor does not check the validity.
     **/
    public void Var034()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RUser u = new RUser(bogus, "BadRUser");
            assertCondition(u.getSystem() == bogus && u.getName().equals("BadRUser"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 2 parms - Pass valid values.  Verify that the system and name are used.
     **/
    public void Var035()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(u.getSystem() == pwrSys_ && u.getName().equals(userName) && textDescription.startsWith(userName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 2 parms - Pass a lowercase user name.  Verify that it is used.
     **/
    public void Var036()
    {
        try
        {
            String userName = sandbox_.createUser().toLowerCase();
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(u.getSystem() == pwrSys_ && u.getName().equalsIgnoreCase(userName), "textDescription="+textDescription);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     serialization - Verify that the properties are set and verify that its usable.
     **/
    public void Var037()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            RUser u2 = (RUser)UserTest.serialize(u);
            String textDescription2 = (String)u2.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(u2.getSystem().getSystemName().equals(pwrSys_.getSystemName()) && u2.getSystem().getUserId().equals(pwrSys_.getUserId()) && u2.getName().equals(userName) && textDescription.equals(textDescription2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getName() - Should return null when the name has not been set.
     **/
    public void Var038()
    {
        try
        {
            RUser u = new RUser();
            assertCondition(u.getName() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getName() - Should return the name when the name has been set.
     **/
    public void Var039()
    {
        try
        {
            RUser u = new RUser(systemObject_, "maine");
            assertCondition(u.getName().equals("maine"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSystem() - Should return null when the system has not been set.
     **/
    public void Var040()
    {
        try
        {
            RUser u = new RUser();
            assertCondition(u.getSystem() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSystem() - Should return the system when the system has been set.
     **/
    public void Var041()
    {
        try
        {
            RUser u = new RUser(systemObject_, "newhampshire");
            assertCondition(u.getSystem().equals(systemObject_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setName() - Should throw an exception if null is passed.
     **/
    public void Var042()
    {
        try
        {
            RUser u = new RUser(systemObject_, "massachusetts");
            u.setName(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setName() - Set to an invalid name.  Should be reflected by getName(), since the validity is not checked here.
     **/
    public void Var043()
    {
        try
        {
            RUser u = new RUser(systemObject_, "connecticut");
            u.setName("rhodeisland");
            assertCondition(u.getName().equals("rhodeisland"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setName() - Set to a valid name.  Should be reflected by getName() and verify that it is used.
     **/
    public void Var044()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, "vermont");
            u.setName(userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(u.getName().equals(userName) && textDescription.startsWith(userName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setName() - Set to a valid lowercase name.  Should be reflected by getName() and verify that it is used.
     **/
    public void Var045()
    {
        try
        {
            String userName = sandbox_.createUser().toLowerCase();
            RUser u = new RUser(pwrSys_, "vermont");
            u.setName(userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(u.getName().equalsIgnoreCase(userName), "textDescription="+textDescription);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setName() - Set to a valid name after the RUser has made a connection.
     **/
    public void Var046()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            u.setName("alabama");
            failed("Didn't throw exception "+textDescription);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     setName() - Should fire a property change event.
     **/
    public void Var047()
    {
        try
        {
            RUser u = new RUser(systemObject_, "massachusetts");
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setName("newyork");
            assertCondition(pcl.eventCount_ == 1 && pcl.event_.getPropertyName().equals("name") && pcl.event_.getOldValue().equals("massachusetts") && pcl.event_.getNewValue().equals("newyork"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setName() - Should fire a vetoable change event.
     **/
    public void Var048()
    {
        try
        {
            RUser u = new RUser(systemObject_, "massachusetts");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.setName("newyork");
            assertCondition(vcl.eventCount_ == 1 && vcl.event_.getPropertyName().equals("name") && vcl.event_.getOldValue().equals("massachusetts") && vcl.event_.getNewValue().equals("newyork"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setName() - Should throw a PropertyVetoException if the change is vetoed.
     **/
    public void Var049()
    {
        try
        {
            RUser u = new RUser(systemObject_, "massachusetts");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_(true);
            u.addVetoableChangeListener(vcl);
            u.setName("newyork");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.beans.PropertyVetoException");
        }
    }

    /**
     setSystem() - Should throw an exception if null is passed.
     **/
    public void Var050()
    {
        try
        {
            RUser u = new RUser(systemObject_, "virginia");
            u.setSystem(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setSystem() - Set to an invalid system.  Should be reflected by getSystem(), since the validity is not checked here.
     **/
    public void Var051()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RUser u = new RUser(systemObject_, "scarolina");
            u.setSystem(bogus);
            assertCondition(u.getSystem().getSystemName().equals("bogus") && u.getSystem().getUserId().equalsIgnoreCase("bogus"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Set to a valid name.  Should be reflected by getSystem() and verify that it is used.
     **/
    public void Var052()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            String userName = sandbox_.createUser();
            RUser u = new RUser(bogus, userName);
            u.setSystem(pwrSys_);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            assertCondition(u.getSystem().getSystemName().equals(pwrSys_.getSystemName()) && u.getSystem().getUserId().equals(pwrSys_.getUserId()) && textDescription.startsWith(userName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Set to a valid name after the RUser object has made a connection.
     **/
    public void Var053()
    {
        try
        {
            String userName = sandbox_.createUser();
            RUser u = new RUser(pwrSys_, userName);
            String textDescription = (String)u.getAttributeValue(RUser.TEXT_DESCRIPTION);
            u.setSystem(systemObject_);
            failed("Didn't throw exception "+textDescription);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     setSystem() - Should fire a property change event.
     **/
    public void Var054()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUser u = new RUser(temp1, "ncarolina");
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setSystem(temp2);
            assertCondition(pcl.eventCount_ == 1 && pcl.event_.getPropertyName().equals("system") && pcl.event_.getOldValue().equals(temp1) && pcl.event_.getNewValue().equals(temp2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Should fire a vetoable change event.
     **/
    public void Var055()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUser u = new RUser(temp1, "georgia");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.setSystem(temp2);
            assertCondition(vcl.eventCount_ == 1 && vcl.event_.getPropertyName().equals("system") && vcl.event_.getOldValue().equals(temp1) && vcl.event_.getNewValue().equals(temp2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Should throw a PropertyVetoException if the change is vetoed.
     **/
    public void Var056()
    {
        try
        {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RUser u = new RUser(temp1, "florida");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_(true);
            u.addVetoableChangeListener(vcl);
            u.setSystem(temp2);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.beans.PropertyVetoException");
        }
    }

    public static final String[] authorities_ = new String[]
    {
        RUser.SPECIAL_AUTHORITIES_ALL_OBJECT,
        RUser.SPECIAL_AUTHORITIES_SECURITY_ADMINISTRATOR,
        RUser.SPECIAL_AUTHORITIES_JOB_CONTROL,
        RUser.SPECIAL_AUTHORITIES_SPOOL_CONTROL,
        RUser.SPECIAL_AUTHORITIES_SAVE_SYSTEM,
        RUser.SPECIAL_AUTHORITIES_SERVICE,
        RUser.SPECIAL_AUTHORITIES_AUDIT,
        RUser.SPECIAL_AUTHORITIES_IO_SYSTEM_CONFIGURATION
    };

    /**
     hasSpecialAuthority() - Should return false for a default user profile.
     **/
    public void Var057()
    {
        try
        {
            output_.println("Setting up profiles...");
            CommandCall cc = new CommandCall(pwrSys_);
            System.out.println("system = " + pwrSys_.getSystemName() + ", user = " + pwrSys_.getUserId());
            boolean dltResult = cc.run("QSYS/DLTUSRPRF "+testruser1+"");
            System.out.println("Delete sucessful?" + dltResult);
            boolean crtResult = cc.run("QSYS/CRTUSRPRF "+testruser1+" *NONE");
            System.out.println("Create sucessful?" + crtResult);

            RUser ruser = new RUser(pwrSys_, ""+testruser1+"");
            for (int i = 0; i < authorities_.length; ++i)
            {
                if (ruser.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should not have authority " + i + ".");
                    return;
                }
            }
            User user = new User(pwrSys_, ""+testruser1+"");
            for (int i = 0; i < authorities_.length; ++i)
            {
                if (user.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should not have authority " + i + ".");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            output_.println("Cleaning up...");
            try
            {
                CommandCall cc = new CommandCall(pwrSys_);
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
            }
            catch (Throwable t)
            {
            }
        }
    }

    /**
     hasSpecialAuthority() - Should return false for all authorities except the authority that was specified when the user profile was created.
     **/
    public void Var058()
    {
        try
        {
            for (int i = 0; i < authorities_.length; ++i)
            {
                output_.println("Setting up profiles with " + authorities_[i] + " authority...");
                CommandCall cc = new CommandCall(pwrSys_);
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                cc.run("QSYS/CRTUSRPRF USRPRF("+testruser1+") PASSWORD(*NONE) SPCAUT(" + authorities_[i] + ")");
                RUser ruser = new RUser(pwrSys_, ""+testruser1+"");
                for (int j = 0; j < authorities_.length; ++j)
                {
                    if (j != i && ruser.hasSpecialAuthority(authorities_[j]))
                    {
                        failed("User "+testruser1+" should not have authority " + j + ".");
                        return;
                    }
                }
                if (!ruser.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have authority "+i+".");
                    return;
                }
                User user = new User(pwrSys_, ""+testruser1+"");
                for (int j = 0; j < authorities_.length; ++j)
                {
                    if (j != i && user.hasSpecialAuthority(authorities_[j]))
                    {
                        failed("User "+testruser1+" should not have authority " + j + ".");
                        return;
                    }
                }
                if (!user.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have authority "+i+".");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            output_.println("Cleaning up...");
            try
            {
                CommandCall cc = new CommandCall(pwrSys_);
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
            }
            catch (Throwable t)
            {
            }
        }
    }

    /**
     hasSpecialAuthority() - Should return false when the user does not have authority and the group it belongs to does not have authority.
     **/
    public void Var059()
    {
        try
        {
            output_.println("Setting up profiles...");
            CommandCall cc = new CommandCall(pwrSys_);
            cc.run("QSYS/DLTUSRPRF "+testruser1+"");
            cc.run("QSYS/DLTUSRPRF TESTRGRP1");
            cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP1) PASSWORD(*NONE) GID(*GEN)");
            cc.run("QSYS/CRTUSRPRF USRPRF("+testruser1+") PASSWORD(*NONE) GRPPRF(TESTRGRP1)");
            RUser ruser = new RUser(pwrSys_, "TESTRGRP1");
            for (int j = 0; j < authorities_.length; ++j)
            {
                if (ruser.hasSpecialAuthority(authorities_[j]))
                {
                    failed("Group TESTRGRP1 should not have authority " + j + ".");
                    return;
                }
            }
            User user = new User(pwrSys_, "TESTRGRP1");
            for (int j = 0; j < authorities_.length; ++j)
            {
                if (user.hasSpecialAuthority(authorities_[j]))
                {
                    failed("Group TESTRGRP1 should not have authority " + j + ".");
                    return;
                }
            }
            ruser = new RUser(pwrSys_, ""+testruser1+"");
            for (int j=0; j<authorities_.length; ++j)
            {
                if (ruser.hasSpecialAuthority(authorities_[j]))
                {
                    failed("User "+testruser1+" should not have authority " + j + ".");
                    return;
                }
            }
            user = new User(pwrSys_, ""+testruser1+"");
            for (int j=0; j<authorities_.length; ++j)
            {
                if (user.hasSpecialAuthority(authorities_[j]))
                {
                    failed("User "+testruser1+" should not have authority " + j + ".");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            output_.println("Cleaning up...");
            try
            {
                CommandCall cc = new CommandCall(pwrSys_);
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                cc.run("QSYS/DLTUSRPRF TESTRGRP1");
            }
            catch (Throwable t)
            {
            }
        }
    }

    /**
     hasSpecialAuthority() - Should return true when the user has authority and the group it belongs to does not have authority.
     **/
    public void Var060()
    {
        try
        {
            output_.println("Setting up profiles...");
            CommandCall cc = new CommandCall(pwrSys_);
            cc.run("QSYS/DLTUSRPRF TESTRGRP1");
            cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP1) PASSWORD(*NONE) GID(*GEN)");
            for (int i = 0; i < authorities_.length; ++i)
            {
                output_.println("Setting up profiles with " + authorities_[i] + " authority...");
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                cc.run("QSYS/CRTUSRPRF USRPRF("+testruser1+") PASSWORD(*NONE) GRPPRF(TESTRGRP1) SPCAUT(" + authorities_[i] + ")");
                RUser ruser = new RUser(pwrSys_, ""+testruser1+"");
                for (int j = 0; j < authorities_.length; ++j)
                {
                    if (j != i && ruser.hasSpecialAuthority(authorities_[j]))
                    {
                        failed("User "+testruser1+" should not have authority " + j + ".");
                        return;
                    }
                }
                if (!ruser.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have authority " + i + ".");
                    return;
                }
                User user = new User(pwrSys_, ""+testruser1+"");
                for (int j = 0; j < authorities_.length; ++j)
                {
                    if (j != i && user.hasSpecialAuthority(authorities_[j]))
                    {
                        failed("User "+testruser1+" should not have authority " + j + ".");
                        return;
                    }
                }
                if (!user.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have authority " + i + ".");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            output_.println("Cleaning up...");
            try
            {
                CommandCall cc = new CommandCall(pwrSys_);
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                cc.run("QSYS/DLTUSRPRF TESTRGRP1");
            }
            catch (Throwable t)
            {
            }
        }
    }

    /**
     hasSpecialAuthority() - Should return true when the user does not have authority and the group it belongs to does have authority.
     **/
    public void Var061()
    {
        try
        {
            for (int i = 0; i < authorities_.length; ++i)
            {
                output_.println("Setting up profiles with " + authorities_[i] + " authority...");
                CommandCall cc = new CommandCall(pwrSys_);
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                cc.run("QSYS/DLTUSRPRF TESTRGRP1");
                cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP1) PASSWORD(*NONE) GID(*GEN) SPCAUT(" + authorities_[i] + ")");
                cc.run("QSYS/CRTUSRPRF USRPRF("+testruser1+") PASSWORD(*NONE) GRPPRF(TESTRGRP1)");
                RUser ruser = new RUser(pwrSys_, ""+testruser1+"");
                for (int j = 0; j < authorities_.length; ++j)
                {
                    if (j != i && ruser.hasSpecialAuthority(authorities_[j]))
                    {
                        failed("User "+testruser1+" should not have authority " + j + ".");
                        return;
                    }
                }
                if (!ruser.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have authority " + i + ".");
                    return;
                }
                User user = new User(pwrSys_, ""+testruser1+"");
                for (int j = 0; j < authorities_.length; ++j)
                {
                    if (j != i && user.hasSpecialAuthority(authorities_[j]))
                    {
                        failed("User "+testruser1+" should not have authority " + j + ".");
                        return;
                    }
                }
                if (!user.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have authority " + i + ".");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            output_.println("Cleaning up...");
            try
            {
                CommandCall cc = new CommandCall(pwrSys_);
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                cc.run("QSYS/DLTUSRPRF TESTRGRP1");
            }
            catch (Throwable t)
            {
            }
        }
    }

    /**
     hasSpecialAuthority() - Should return true when the user does not have authority and the primary group it belongs to does not have authority and a supplemental group does have authority.
     **/
    public void Var062()
    {
        try
        {
            output_.println("Setting up profiles...");
            CommandCall cc = new CommandCall(pwrSys_);
            cc.run("QSYS/DLTUSRPRF TESTRGRP1");
            cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP1) PASSWORD(*NONE) GID(*GEN)");
            for (int i = 0; i < authorities_.length; ++i)
            {
                output_.println("Setting up profiles with " + authorities_[i] + " authority...");
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                cc.run("QSYS/DLTUSRPRF TESTRGRP2");
                cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP2) PASSWORD(*NONE) GID(*GEN) SPCAUT(" + authorities_[i] + ")");
                cc.run("QSYS/CRTUSRPRF USRPRF("+testruser1+") PASSWORD(*NONE) GRPPRF(TESTRGRP1) SUPGRPPRF(TESTRGRP2)");
                RUser ruser = new RUser(pwrSys_, ""+testruser1+"");
                for (int j = 0; j < authorities_.length; ++j)
                {
                    if (j != i && ruser.hasSpecialAuthority(authorities_[j]))
                    {
                        failed("User "+testruser1+" should not have authority " + j + ".");
                        return;
                    }
                }
                if (!ruser.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have authority " + i + ".");
                    return;
                }
                User user = new User(pwrSys_, ""+testruser1+"");
                for (int j = 0; j < authorities_.length; ++j)
                {
                    if (j != i && user.hasSpecialAuthority(authorities_[j]))
                    {
                        failed("User "+testruser1+" should not have authority " + j + ".");
                        return;
                    }
                }
                if (!user.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have authority " + i + ".");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            output_.println("Cleaning up...");
            try
            {
                CommandCall cc = new CommandCall(pwrSys_);
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                cc.run("QSYS/DLTUSRPRF TESTRGRP1");
                cc.run("QSYS/DLTUSRPRF TESTRGRP2");
            }
            catch (Throwable t)
            {
            }
        }
    }

    /**
     hasSpecialAuthority() - Should return true when the user does not have authority and the primary group it belongs to does have authority and its supplemental groups do not have authority.
     **/
    public void Var063()
    {
        try
        {
            output_.println("Setting up profiles...");
            CommandCall cc = new CommandCall(pwrSys_);
            cc.run("QSYS/DLTUSRPRF TESTRGRP2");
            cc.run("QSYS/DLTUSRPRF TESTRGRP3");
            cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP2) PASSWORD(*NONE) GID(*GEN)");
            cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP3) PASSWORD(*NONE) GID(*GEN)");
            for (int i = 0; i < authorities_.length; ++i)
            {
                output_.println("Setting up profiles with " + authorities_[i] + " authority...");
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                cc.run("QSYS/DLTUSRPRF TESTRGRP1");
                cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP1) PASSWORD(*NONE) GID(*GEN) SPCAUT(" + authorities_[i] + ")");
                cc.run("QSYS/CRTUSRPRF USRPRF("+testruser1+") PASSWORD(*NONE) GRPPRF(TESTRGRP1) SUPGRPPRF(TESTRGRP2 TESTRGRP3)");
                RUser ruser = new RUser(pwrSys_, ""+testruser1+"");
                for (int j = 0; j < authorities_.length; ++j)
                {
                    if (j != i && ruser.hasSpecialAuthority(authorities_[j]))
                    {
                        failed("User "+testruser1+" should not have authority " + j + ".");
                        return;
                    }
                }
                if (!ruser.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have authority " + i + ".");
                    return;
                }
                User user = new User(pwrSys_, ""+testruser1+"");
                for (int j = 0; j < authorities_.length; ++j)
                {
                    if (j != i && user.hasSpecialAuthority(authorities_[j]))
                    {
                        failed("User "+testruser1+" should not have authority " + j + ".");
                        return;
                    }
                }
                if (!user.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have authority " + i + ".");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            output_.println("Cleaning up...");
            try
            {
                CommandCall cc = new CommandCall(pwrSys_);
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                cc.run("QSYS/DLTUSRPRF TESTRGRP1");
                cc.run("QSYS/DLTUSRPRF TESTRGRP2");
                cc.run("QSYS/DLTUSRPRF TESTRGRP3");
            }
            catch (Throwable t)
            {
            }
        }
    }

    /**
     hasSpecialAuthority() - Should return true when the user does not have authority and the primary group it belongs to does not have authority and one of its supplemental groups has authority.
     **/
    public void Var064()
    {
        try
        {
            output_.println("Setting up profiles...");
            CommandCall cc = new CommandCall(pwrSys_);
            cc.run("QSYS/DLTUSRPRF TESTRGRP1");
            cc.run("QSYS/DLTUSRPRF TESTRGRP2");
            cc.run("QSYS/DLTUSRPRF TESTRGRP3");
            cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP1) PASSWORD(*NONE) GID(*GEN)");
            cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP2) PASSWORD(*NONE) GID(*GEN)");
            cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP3) PASSWORD(*NONE) GID(*GEN)");
            for (int i = 0; i < authorities_.length; ++i)
            {
                output_.println("Setting up profiles with " + authorities_[i] + " authority...");
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                cc.run("QSYS/DLTUSRPRF TESTRGRP4");
                cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP4) PASSWORD(*NONE) GID(*GEN) SPCAUT(" + authorities_[i] + ")");
                cc.run("QSYS/CRTUSRPRF USRPRF("+testruser1+") PASSWORD(*NONE) GRPPRF(TESTRGRP1) SUPGRPPRF(TESTRGRP2 TESTRGRP3 TESTRGRP4)");
                RUser ruser = new RUser(pwrSys_, ""+testruser1+"");
                for (int j = 0; j < authorities_.length; ++j)
                {
                    if (j != i && ruser.hasSpecialAuthority(authorities_[j]))
                    {
                        failed("User "+testruser1+" should not have authority " + j + ".");
                        return;
                    }
                }
                if (!ruser.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have authority " + i + ".");
                    return;
                }
                User user = new User(pwrSys_, ""+testruser1+"");
                for (int j = 0; j < authorities_.length; ++j)
                {
                    if (j != i && user.hasSpecialAuthority(authorities_[j]))
                    {
                        failed("User "+testruser1+" should not have authority " + j + ".");
                        return;
                    }
                }
                if (!user.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have authority " + i + ".");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            output_.println("Cleaning up...");
            try
            {
                CommandCall cc = new CommandCall(pwrSys_);
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                cc.run("QSYS/DLTUSRPRF TESTRGRP1");
                cc.run("QSYS/DLTUSRPRF TESTRGRP2");
                cc.run("QSYS/DLTUSRPRF TESTRGRP3");
                cc.run("QSYS/DLTUSRPRF TESTRGRP4");
            }
            catch (Throwable t)
            {
            }
        }
    }

    /**
     hasSpecialAuthority() - Should return true when the user does not have authority and one group has one authority and another group has a different authority.  (Note that group profiles cannot be deleted if they have members.)
     **/
    public void Var065()
    {
        try
        {
            output_.println("Setting up profiles...");
            CommandCall cc = new CommandCall(pwrSys_);
            cc.run("QSYS/DLTUSRPRF "+testruser1+"");
            for (int i = 0; i < authorities_.length; ++i)
            {
                cc.run("QSYS/DLTUSRPRF TESTRGRP" + (i + 1));
                cc.run("QSYS/CRTUSRPRF USRPRF(TESTRGRP" + (i + 1) + ") PASSWORD(*NONE) GID(*GEN) SPCAUT(" + authorities_[i] + ")");
            }
            String create = "QSYS/CRTUSRPRF USRPRF("+testruser1+") PASSWORD(*NONE) GRPPRF(TESTRGRP1) SUPGRPPRF(";
            for (int i = 0; i < authorities_.length; ++i)
            {
                create += "TESTRGRP" + (i + 1) + " ";
            }
            create.trim();
            create += ")";
            cc.run(create);
            RUser ruser = new RUser(pwrSys_, ""+testruser1+"");
            for (int i=0; i<authorities_.length; ++i)
            {
                if (!ruser.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have " + authorities_[i] + " authority.");
                    return;
                }
            }
            User user = new User(pwrSys_, ""+testruser1+"");
            for (int i=0; i<authorities_.length; ++i)
            {
                if (!user.hasSpecialAuthority(authorities_[i]))
                {
                    failed("User "+testruser1+" should have " + authorities_[i] + " authority.");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            output_.println("Cleaning up...");
            try
            {
                CommandCall cc = new CommandCall(pwrSys_);
                cc.run("QSYS/DLTUSRPRF "+testruser1+"");
                for (int i = 0; i < authorities_.length; ++i)
                {
                    cc.run("QSYS/DLTUSRPRF TESTRGRP" + (i + 1));
                }
            }
            catch (Throwable t)
            {
            }
        }
    }

    /**
     exists() - When no system or name has been specified.
     **/
    public void Var066()
    {
        try
        {
            User u = new User();
            boolean result = u.exists();
            // This should throw a system-isn't-set exception.
            failed("Expected exception did not occur."+result );
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system", ExtendedIllegalStateException.PROPERTY_NOT_SET);
        }
    }

    /**
     exists() - When no name has been specified.
     **/
    public void Var067()
    {
        try
        {
            User u = new User();
            u.setSystem(systemObject_);
            boolean result = u.exists();
            // This should throw a system-isn't-set exception.
            failed("Expected exception did not occur."+result);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "name", ExtendedIllegalStateException.PROPERTY_NOT_SET);
        }
    }

    /**
     exists() - Non-existent profile, and existing profile.
     **/
    public void Var068()
    {
        try
        {
            User u1 = new User(systemObject_, "NOTEXIST");
            User u2 = new User(systemObject_, "QSECOFR");
            boolean exists1 = u1.exists();
            boolean exists2 = u2.exists();
            assertCondition((exists1 == false) && (exists2 == true));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



}
