///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JobListBeans.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//
//
//
///////////////////////////////////////////////////////////////////////////////
//
// File Name:  JobListBeans.java
//
// Class Name:  JobListBeans
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.JobList;

/**
 Testcase JobListBeans.
 <p>This tests the following JobList methods:
 <ul>
 <li>serialization
 <li>addPropertyChangeListener()
 <li>removePropertyChangeListener()
 <li>addVetoableChangeListener()
 <li>removeVetoableChangeListener()
 </ul>
 **/
public class JobListBeans extends Testcase
{
    /**
     Serialization - when no properties have been set.
     **/
    public void Var001()
    {
        try
        {
            JobList f = new JobList();
            JobList f2 = (JobList)TestUtilities.serialize(f);
            assertCondition(f2.getSystem() == null && f2.getName().equals(JobList.ALL) && f2.getNumber().equals(JobList.ALL) && f2.getUser().equals(JobList.ALL));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Serialization - when the properties have been set.
     **/
    public void Var002()
    {
        try
        {
            String Name = "name";
            String Number = "*FIRST";
            String User = "Alfred";
            JobList f = new JobList(systemObject_);
            f.setName(Name);
            f.setNumber(Number);
            f.setUser(User);
            JobList f2 = (JobList)TestUtilities.serialize(f);
            assertCondition(f2.getSystem().getSystemName().equals(systemObject_.getSystemName()) && f2.getName().equals(Name) && f2.getNumber().equals(Number) && f2.getUser().equals(User));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Listens for property change events.
     **/
    private class PropertyChangeListener_ implements PropertyChangeListener
    {
        private PropertyChangeEvent lastEvent_ = null;
        public void propertyChange(PropertyChangeEvent event)
        {
            lastEvent_ = event;
        }
    }

    /**
     addPropertyChangeListener() - Test that adding a null listener throws NullPointerException.
     **/
    public void Var003()
    {
        try
        {
            JobList f = new JobList();
            f.addPropertyChangeListener(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     addPropertyChangeListener() - Test that an event is received when the system property is changed.
     **/
    public void Var004()
    {
        try
        {
            JobList f = new JobList(systemObject_);
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            f.addPropertyChangeListener(listener);
            AS400 system2 = new AS400();
            f.setSystem(system2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("system") && listener.lastEvent_.getOldValue() == systemObject_ && listener.lastEvent_.getNewValue() == system2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addPropertyChangeListener() - Test that an event is received when the name property is changed.
     **/
    public void Var005()
    {
        try
        {
            JobList f = new JobList();
            String Name = "Albert";
            f.setName(Name);
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            f.addPropertyChangeListener(listener);
            String Name2 = "Sanchez";
            f.setName(Name2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("name") && listener.lastEvent_.getOldValue().equals(Name) && listener.lastEvent_.getNewValue().equals(Name2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addPropertyChangeListener() - Test that an event is received when the number property is changed.
     **/
    public void Var006()
    {
        try
        {
            JobList f = new JobList();
            String Number = "*ALL";  // One of the required values.
            f.setNumber(Number);
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            f.addPropertyChangeListener(listener);
            String Number2 = "*MNR";  // One of the required values.
            f.setNumber(Number2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("number") && listener.lastEvent_.getOldValue().equals(Number) && listener.lastEvent_.getNewValue().equals(Number2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addPropertyChangeListener() - Test that an event is received when the user property is changed.
     **/
    public void Var007()
    {
        try
        {
            JobList f = new JobList();
            String User = "Melvin";
            f.setUser(User);
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            f.addPropertyChangeListener(listener);
            String User2 = "Wonders";
            f.setUser(User2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("user") && listener.lastEvent_.getOldValue().equals(User) && listener.lastEvent_.getNewValue().equals(User2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     removePropertyChangeListener() - Test that removing a null listener throws NullPointerException.
     **/
    public void Var008()
    {
        try
        {
            JobList f = new JobList();
            f.removePropertyChangeListener(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     removePropertyChangeListener() - Test that events are no longer received.
     **/
    public void Var009()
    {
        try
        {
            JobList f = new JobList(systemObject_);
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            f.addPropertyChangeListener(listener);
            f.removePropertyChangeListener(listener);
            AS400 system2 = new AS400();
            f.setSystem(system2);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Listens for vetoable change events.
     **/
    private class VetoableChangeListener_ implements VetoableChangeListener
    {
        private PropertyChangeEvent lastEvent_ = null;
        private boolean reject_ = false;
        public void vetoableChange(PropertyChangeEvent event) throws PropertyVetoException
        {
            lastEvent_ = event;
            if (reject_)
            {
                throw new PropertyVetoException("no way joe", event);
            }
        }
        public void rejectChanges()
        {
            reject_ = true;
        }
        public void acceptChanges()
        {
            reject_ = false;
        }
    }

    /**
     addVetoableChangeListener() - Test that adding a null listener throws NullPointerException.
     **/
    public void Var010()
    {
        try
        {
            JobList f = new JobList();
            f.addVetoableChangeListener(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     addVetoableChangeListener() - Test that an event is received when the system property is changed.
     **/
    public void Var011()
    {
        try
        {
            JobList f = new JobList(systemObject_);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            AS400 system2 = new AS400();
            f.setSystem(system2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("system") && listener.lastEvent_.getOldValue() == systemObject_ && listener.lastEvent_.getNewValue() == system2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that an event is received when the name property is changed.
     **/
    public void Var012()
    {
        try
        {
            JobList f = new JobList();
            String Name = "Wally";
            f.setName(Name);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            String Name2 = "Beaver";
            f.setName(Name2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("name") && listener.lastEvent_.getOldValue().equals(Name) && listener.lastEvent_.getNewValue().equals(Name2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that an event is received when the number property is changed.
     **/
    public void Var013()
    {
        try
        {
            JobList f = new JobList();
            String Number = "*ALL";  // One of the required values.
            f.setNumber(Number);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            String Number2 = "*MNR";  // One of the required values.
            f.setNumber(Number2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("number") && listener.lastEvent_.getOldValue().equals(Number) && listener.lastEvent_.getNewValue().equals(Number2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that an event is received when the user property is changed.
     **/
    public void Var014()
    {
        try
        {
            JobList f = new JobList();
            String User = "Silverman";
            f.setUser(User);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            String User2 = "Ralph";
            f.setUser(User2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("user") && listener.lastEvent_.getOldValue().equals(User) && listener.lastEvent_.getNewValue().equals(User2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     removeVetoableChangeListener() - Test that removing a null listener throws NullPointerException.
     **/
    public void Var015()
    {
        try
        {
            JobList f = new JobList();
            f.removeVetoableChangeListener(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     removeVetoableChangeListener() - Test that events are no longer received.
     **/
    public void Var016()
    {
        try
        {
            JobList f = new JobList(systemObject_);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.removeVetoableChangeListener(listener);
            AS400 system2 = new AS400();
            f.setSystem(system2);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no event is received when the system property is set to the same value.
     **/
    public void Var017()
    {
        try
        {
            JobList f = new JobList(systemObject_);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setSystem(systemObject_);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no event is received when the name property is set to the same value.
     **/
    public void Var018()
    {
        try
        {
            JobList f = new JobList();
            String Name = "Hal";
            f.setName(Name);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setName(Name);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no event is received when the number property is set to the same value.
     **/
    public void Var019()
    {
        try
        {
            JobList f = new JobList();
            String Number = "*ALL";  // One of the required values.
            f.setNumber(Number);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setNumber(Number);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no event is received when the user property is set to the same value.
     **/
    public void Var020()
    {
        try
        {
            JobList f = new JobList();
            String User = "Eggbert";
            f.setUser(User);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setUser(User);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no event is received when the system property is set to the same value.
     **/
    public void Var021()
    {
        try
        {
            JobList f = new JobList(systemObject_);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setSystem(systemObject_);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no event is received when the name property is set to the same value.
     **/
    public void Var022()
    {
        try
        {
            JobList f = new JobList();
            String Name = "Brenden";
            f.setName(Name);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setName(Name);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no event is received when the number property is set to the same value.
     **/
    public void Var023()
    {
        try
        {
            JobList f = new JobList();
            String Number = "*ALL";  // One of the required values.
            f.setNumber(Number);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setNumber(Number);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no event is received when the user property is set to the same value.
     **/
    public void Var024()
    {
        try
        {
            JobList f = new JobList();
            String User = "Dilbert";
            f.setUser(User);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setUser(User);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no change happens to the system property when a change is vetoed.
     **/
    public void Var025()
    {
        try
        {
            JobList f = new JobList(systemObject_);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            listener.rejectChanges();
            f.addVetoableChangeListener(listener);
            AS400 system2 = new AS400();
            try
            {
                f.setSystem(system2);
                failed("Did not throw exception.");
            }
            catch (PropertyVetoException e)
            {
                assertCondition(f.getSystem() == systemObject_);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no change happens to the name property when a change is vetoed.
     **/
    public void Var026()
    {
        try
        {
            JobList f = new JobList();
            String Name = "Brandish";
            f.setName(Name);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            listener.rejectChanges();
            f.addVetoableChangeListener(listener);
            String Name2 = "Fanks";
            try
            {
                f.setName(Name2);
                failed("Did not throw exception.");
            }
            catch (PropertyVetoException e)
            {
                assertCondition(f.getName().equals(Name));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no change happens to the number property when a change is vetoed.
     **/
    public void Var027()
    {
        try
        {
            JobList f = new JobList();
            String Number = "*ALL";  // One of the required values.
            f.setNumber(Number);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            listener.rejectChanges();
            f.addVetoableChangeListener(listener);
            String Number2 = "*MNR";  // One of the required values.
            try
            {
                f.setNumber(Number2);
                failed("Did not throw exception.");
            }
            catch (PropertyVetoException e)
            {
                assertCondition(f.getNumber().equals(Number));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no change happens to the user property when a change is vetoed.
     **/
    public void Var028()
    {
        try
        {
            JobList f = new JobList();
            String User = "Dogbert";
            f.setUser(User);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            listener.rejectChanges();
            f.addVetoableChangeListener(listener);
            String User2 = "Marvin";
            try
            {
                f.setUser(User2);
                failed("Did not throw exception.");
            }
            catch (PropertyVetoException e)
            {
                assertCondition(f.getUser().equals(User));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }
}
