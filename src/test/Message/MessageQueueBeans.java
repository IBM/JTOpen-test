///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  MessageQueueBeans.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Message;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.MessageQueue;
import com.ibm.as400.access.QSYSObjectPathName;

import test.TestUtilities;
import test.Testcase;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

/**
 Testcase MessageQueueBeans.
 <p>This tests the following MessageQueue methods:
 <ul>
 <li>serialization
 <li>addPropertyChangeListener()
 <li>removePropertyChangeListener()
 <li>addVetoableChangeListener()
 <li>removeVetoableChangeListener()
 </ul>
 **/
public class MessageQueueBeans extends Testcase
{
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
     Serialization - when no properties have been set.
     **/
    public void Var001()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            MessageQueue f2 = (MessageQueue)TestUtilities.serialize(f);
            assertCondition(f2.getSystem() == null && f2.getPath().equals("*CURRENT") && f2.getSelection().equals(MessageQueue.ALL) && f2.getSeverity() == 0);
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
            String path = QSYSObjectPathName.toPath("YOURLIB", "YOURMSGQ", "MSGQ");
            String selection = "*ALL";  // One of the required values.
            int severity = 39;
            MessageQueue f = new MessageQueue(systemObject_);
            f.setPath(path);
            f.setSelection(selection);
            f.setSeverity(severity);
            MessageQueue f2 = (MessageQueue)TestUtilities.serialize(f);
            assertCondition(f2.getSystem().getSystemName().equals(systemObject_.getSystemName()) && f2.getPath().equals(path) && f2.getSelection().equals(selection) && f2.getSeverity() == severity);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addPropertyChangeListener() - Test that adding a null listener throws NullPointerException.
     **/
    public void Var003()
    {
        try
        {
            MessageQueue f = new MessageQueue();
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
            MessageQueue f = new MessageQueue(systemObject_);
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
     addPropertyChangeListener() - Test that an event is received when the path property is changed.
     **/
    public void Var005()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            String path = QSYSObjectPathName.toPath("MYLIB", "MYMSGQ", "MSGQ");
            f.setPath(path);
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            f.addPropertyChangeListener(listener);
            String path2 = QSYSObjectPathName.toPath("MYLIB2", "MYMSGQ2", "MSGQ");
            f.setPath(path2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("path") && listener.lastEvent_.getOldValue().equals(path) && listener.lastEvent_.getNewValue().equals(path2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addPropertyChangeListener() - Test that an event is received when the selection property is changed.
     **/
    public void Var006()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            String selection = "*ALL";  // One of the required values.
            f.setSelection(selection);
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            f.addPropertyChangeListener(listener);
            String selection2 = "*MNR";  // One of the required values.
            f.setSelection(selection2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("selection") && listener.lastEvent_.getOldValue().equals(selection) && listener.lastEvent_.getNewValue().equals(selection2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addPropertyChangeListener() - Test that an event is received when the severity property is changed.
     **/
    public void Var007()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            int severity = 46;
            f.setSeverity(severity);
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            f.addPropertyChangeListener(listener);
            int severity2 = 64;
            f.setSeverity(severity2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("severity") && ((Number)listener.lastEvent_.getOldValue()).intValue() == severity && ((Number)listener.lastEvent_.getNewValue()).intValue() == severity2);
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
            MessageQueue f = new MessageQueue();
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
            MessageQueue f = new MessageQueue(systemObject_);
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
     addVetoableChangeListener() - Test that adding a null listener throws NullPointerException.
     **/
    public void Var010()
    {
        try
        {
            MessageQueue f = new MessageQueue();
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
            MessageQueue f = new MessageQueue(systemObject_);
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
     addVetoableChangeListener() - Test that an event is received when the path property is changed.
     **/
    public void Var012()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            String path = QSYSObjectPathName.toPath("MYLIB", "MYMSGQ", "MSGQ");
            f.setPath(path);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            String path2 = QSYSObjectPathName.toPath("MYLIB2", "MYMSGQ2", "MSGQ");
            f.setPath(path2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("path") && listener.lastEvent_.getOldValue().equals(path) && listener.lastEvent_.getNewValue().equals(path2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that an event is received when the selection property is changed.
     **/
    public void Var013()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            String selection = "*ALL";  // One of the required values.
            f.setSelection(selection);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            String selection2 = "*MNR";  // One of the required values.
            f.setSelection(selection2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("selection") && listener.lastEvent_.getOldValue().equals(selection) && listener.lastEvent_.getNewValue().equals(selection2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that an event is received when the severity property is changed.
     **/
    public void Var014()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            int severity = 46;
            f.setSeverity(severity);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            int severity2 = 64;
            f.setSeverity(severity2);
            assertCondition(listener.lastEvent_.getPropertyName().equals("severity") && ((Number)listener.lastEvent_.getOldValue()).intValue() == severity && ((Number)listener.lastEvent_.getNewValue()).intValue() == severity2);
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
            MessageQueue f = new MessageQueue();
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
            MessageQueue f = new MessageQueue(systemObject_);
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
     addVetoableChangeListener() - Test that no event is received when the system property is changed.
     **/
    public void Var017()
    {
        try
        {
            MessageQueue f = new MessageQueue(systemObject_);
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
     addVetoableChangeListener() - Test that no event is received when the path property is changed.
     **/
    public void Var018()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            String path = QSYSObjectPathName.toPath("MYLIB", "MYMSGQ", "MSGQ");
            f.setPath(path);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setPath(path);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no event is received when the selection property is changed.
     **/
    public void Var019()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            String selection = "*ALL";  // One of the required values.
            f.setSelection(selection);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setSelection(selection);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no event is received when the severity property is changed.
     **/
    public void Var020()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            int severity = 46;
            f.setSeverity(severity);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setSeverity(severity);
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
            MessageQueue f = new MessageQueue(systemObject_);
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
     addVetoableChangeListener() - Test that no event is received when the path property is set to the same value.
     **/
    public void Var022()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            String path = QSYSObjectPathName.toPath("MYLIB", "MYMSGQ", "MSGQ");
            f.setPath(path);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setPath(path);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no event is received when the selection property is changed.
     **/
    public void Var023()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            String selection = "*ALL";  // One of the required values.
            f.setSelection(selection);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setSelection(selection);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no event is received when the severity property is set to the same value.
     **/
    public void Var024()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            int severity = 46;
            f.setSeverity(severity);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            f.addVetoableChangeListener(listener);
            f.setSeverity(severity);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no change happens to the system when a change is vetoed.
     **/
    public void Var025()
    {
        try
        {
            MessageQueue f = new MessageQueue(systemObject_);
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
     addVetoableChangeListener() - Test that no change happens to the path when a change is vetoed.
     **/
    public void Var026()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            String path = QSYSObjectPathName.toPath("MYLIB", "MYMSGQ", "MSGQ");
            f.setPath(path);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            listener.rejectChanges();
            f.addVetoableChangeListener(listener);
            String path2 = QSYSObjectPathName.toPath("MYLIB2", "MYMSGQ2", "MSGQ");
            try
            {
                f.setPath(path2);
                failed("Did not throw exception.");
            }
            catch (PropertyVetoException e)
            {
                assertCondition(f.getPath().equals(path));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no change happens to the selection when a change is vetoed.
     **/
    public void Var027()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            String selection = "*ALL";  // One of the required values.
            f.setSelection(selection);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            listener.rejectChanges();
            f.addVetoableChangeListener(listener);
            String selection2 = "*MNR";  // One of the required values.
            try
            {
                f.setSelection(selection2);
                failed("Did not throw exception.");
            }
            catch (PropertyVetoException e)
            {
                assertCondition(f.getSelection().equals(selection));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     addVetoableChangeListener() - Test that no change happens to the severity when a change is vetoed.
     **/
    public void Var028()
    {
        try
        {
            MessageQueue f = new MessageQueue();
            int severity = 46;
            f.setSeverity(severity);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            listener.rejectChanges();
            f.addVetoableChangeListener(listener);
            int severity2 = 64;
            try
            {
                f.setSeverity(severity2);
                failed("Did not throw exception.");
            }
            catch (PropertyVetoException e)
            {
                assertCondition(f.getSeverity() == severity);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }
}
