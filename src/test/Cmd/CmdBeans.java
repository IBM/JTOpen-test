///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CmdBeans.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Cmd;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.ActionCompletedEvent;
import com.ibm.as400.access.ActionCompletedListener;
import com.ibm.as400.access.CommandCall;

import test.Testcase;

/**
 The CmdBeans class tests the following methods of the CommandCall class:
 <li>serialization,
 <li>add/remove listeners.
 **/
public class CmdBeans extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "CmdBeans";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.CmdTest.main(newArgs); 
   }
    /**
     Serializes and deserializes the object.
     **/
    private CommandCall serialize(CommandCall cmd) throws Exception
    {
        // Serialize.
        String serializeFileName = "cmdbeans.ser";
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(serializeFileName));
        out.writeObject(cmd);
        out.flush();
        out.close(); 
        // Deserialize.
        CommandCall cmd2 = null;
        try
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(serializeFileName));
            cmd2 = (CommandCall)in.readObject();
            in.close(); 
        }
        finally
        {
            File f = new File(serializeFileName);
            f.delete();
        }

        return cmd2;
    }

    /**
     Serialization.  Verify that the object is properly serialized when neither the system or command are set.
     **/
    public void Var001()
    {
        try
        {
            CommandCall cmd = new CommandCall();
            CommandCall cmd2 = serialize(cmd);
            assertCondition((cmd2.getSystem() == null) && (cmd2.getCommand().length() == 0) && (cmd2.getMessageList().length == 0));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        catch (NoClassDefFoundError e)
        { // Tolerate missing RJob class.
          if (e.getMessage().indexOf("RJob") != -1) {
            failed("Class not found: RJob");
          }
          else failed(e, "Unexpected exception");
        }
    }

    /**
     Serialization.  Verify that the object is properly serialized when the system and command are set.
     **/
    public void Var002()
    {
        try
        {
            String command = "Hi Mom";
            CommandCall cmd = new CommandCall(systemObject_, command);
            CommandCall cmd2 = serialize(cmd);
            AS400 newSystem = cmd2.getSystem();
            assertCondition((newSystem.getSystemName().equals(systemObject_.getSystemName())) && (newSystem.getUserId().equals(newSystem.getUserId())) && (cmd2.getCommand().equals(command)) && (cmd2.getMessageList().length == 0));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        catch (NoClassDefFoundError e)
        { // Tolerate missing RJob class.
          if (e.getMessage().indexOf("RJob") != -1) {
            failed("Class not found: RJob");
          }
          else failed(e, "Unexpected exception");
        }
    }

    /**
     Serialization.  Verify that the object is properly serialized when the system and command are set, and the command has been run.
     **/
    public void Var003()
    {
        try
        {
            String command = "PING " + systemObject_.getSystemName();
            CommandCall cmd = new CommandCall(systemObject_, command);
            cmd.run();
            CommandCall cmd2 = serialize(cmd);
            AS400 newSystem = cmd2.getSystem();
            AS400Message[] messageList = cmd2.getMessageList();
            assertCondition((newSystem.getSystemName().equals(systemObject_.getSystemName())) && (newSystem.getUserId().equals(newSystem.getUserId())) && (cmd2.getCommand().equals(command)) && (messageList.length > 0) && (messageList[0] != null));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        catch (NoClassDefFoundError e)
        { // Tolerate missing RJob class.
          if (e.getMessage().indexOf("RJob") != -1) {
            failed("Class not found: RJob");
          }
          else failed(e, "Unexpected exception");
        }
    }

    /**
     The ActionCompletedListener_ class is for testing.
     **/
    private class ActionCompletedListener_ implements ActionCompletedListener
    {
        private ActionCompletedEvent lastEvent_;

        public void actionCompleted(ActionCompletedEvent event)
        {
            lastEvent_ = event;
        }

        public ActionCompletedEvent getLastEvent()
        {
            return lastEvent_;
        }
    }

    /**
     addActionCompletedListener().  Add null.
     **/
    public void Var004()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            cmd.addActionCompletedListener(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     addActionCompletedListener().  Add a valid listener.  Verify that an event gets fired when run() with no parameters is called.
     **/
    public void Var005()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            ActionCompletedListener_ l = new ActionCompletedListener_();
            cmd.addActionCompletedListener(l);
            ActionCompletedEvent before = l.getLastEvent();
            cmd.run();
            ActionCompletedEvent after = l.getLastEvent();
            assertCondition((before == null) && (after.getSource() == cmd));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     addActionCompletedListener().  Add a valid listener.  Verify that an event gets fired when run() with 1 parameter is called.
     **/
    public void Var006()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            ActionCompletedListener_ l = new ActionCompletedListener_();
            cmd.addActionCompletedListener(l);
            ActionCompletedEvent before = l.getLastEvent();
            cmd.run("PING " + systemObject_.getSystemName());
            ActionCompletedEvent after = l.getLastEvent();
            assertCondition((before == null) && (after.getSource() == cmd));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     removeActionCompletedListener().  Add null.
     **/
    public void Var007()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            cmd.removeActionCompletedListener(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     removeActionCompletedListener().  Removes a valid listener. Verify that no events get fired when run() with no parameters is called.
     **/
    public void Var008()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            ActionCompletedListener_ l = new ActionCompletedListener_();
            cmd.addActionCompletedListener(l);
            cmd.removeActionCompletedListener(l);
            ActionCompletedEvent before = l.getLastEvent();
            cmd.run();
            ActionCompletedEvent after = l.getLastEvent();
            assertCondition((before == null) && (after == null));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     removeActionCompletedListener().  Removes a valid listener. Verify that no events get fired when run() with 1 parameter is called.
     **/
    public void Var009()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            ActionCompletedListener_ l = new ActionCompletedListener_();
            cmd.addActionCompletedListener(l);
            cmd.removeActionCompletedListener(l);
            ActionCompletedEvent before = l.getLastEvent();
            cmd.run("PING " + systemObject_.getSystemName());
            ActionCompletedEvent after = l.getLastEvent();
            assertCondition((before == null) && (after == null));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     The PropertyChangeListener_ class is for testing.
     **/
    private class PropertyChangeListener_ implements PropertyChangeListener
    {
        private PropertyChangeEvent lastEvent_;

        public void propertyChange(PropertyChangeEvent event)
        {
            lastEvent_ = event;
        }

        public PropertyChangeEvent getLastEvent()
        {
            return lastEvent_;
        }
    }

    /**
     addPropertyChangeListener().  Add null.
     **/
    public void Var010()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            cmd.addPropertyChangeListener(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     addPropertyChangeListener().  Add a valid listener.  Verify that an event gets fired when setCommand() is called.
     **/
    public void Var011()
    {
        try
        {
            String command1 = "YO DAD";
            String command2 = "HI MOM";
            CommandCall cmd = new CommandCall(systemObject_, command1);
            PropertyChangeListener_ l = new PropertyChangeListener_();
            cmd.addPropertyChangeListener(l);
            PropertyChangeEvent before = l.getLastEvent();
            cmd.setCommand(command2);
            PropertyChangeEvent after = l.getLastEvent();
            assertCondition((before == null) && (after.getSource() == cmd) && (after.getPropertyName().equals("command")) && (after.getOldValue().equals(command1)) && (after.getNewValue().equals(command2)));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     addPropertyChangeListener().  Add a valid listener. Verify that an event gets fired when setCommand() is called.
     **/
    public void Var012()
    {
        try
        {
            AS400 system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId());
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            PropertyChangeListener_ l = new PropertyChangeListener_();
            cmd.addPropertyChangeListener(l);
            PropertyChangeEvent before = l.getLastEvent();
            cmd.setSystem(system);
            PropertyChangeEvent after = l.getLastEvent();
            assertCondition((before == null) && (after.getSource() == cmd) && (after.getPropertyName().equals("system")) && (after.getOldValue() == systemObject_) && (after.getNewValue() == system));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     removePropertyChangeListener().  Add null.
     **/
    public void Var013()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            cmd.removePropertyChangeListener(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     removePropertyChangeListener().  Removes a valid listener.  Verify that no events get fired when setCommand() is called.
     **/
    public void Var014()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            PropertyChangeListener_ l = new PropertyChangeListener_();
            cmd.addPropertyChangeListener(l);
            cmd.removePropertyChangeListener(l);
            PropertyChangeEvent before = l.getLastEvent();
            cmd.setCommand("DSPDRVLVL");
            PropertyChangeEvent after = l.getLastEvent();
            assertCondition((before == null) && (after == null));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     removePropertyChangeListener().  Removes a valid listener.  Verify that no events get fired when setSystem() is called.
     **/
    public void Var015()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            PropertyChangeListener_ l = new PropertyChangeListener_();
            cmd.addPropertyChangeListener(l);
            cmd.removePropertyChangeListener(l);
            PropertyChangeEvent before = l.getLastEvent();
            cmd.setSystem(new AS400());
            PropertyChangeEvent after = l.getLastEvent();
            assertCondition((before == null) && (after == null));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     The VetoableChangeListener_ class is for testing.
     **/
    private class VetoableChangeListener_ implements VetoableChangeListener
    {
        private PropertyChangeEvent lastEvent_;

        public void vetoableChange(PropertyChangeEvent event)
        {
            lastEvent_ = event;
        }

        public PropertyChangeEvent getLastEvent()
        {
            return lastEvent_;
        }
    }

    /**
     addVetoableChangeListener().  Add null.
     **/
    public void Var016()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            cmd.addVetoableChangeListener(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     addVetoableChangeListener().  Add a valid listener.
     Verify that an event gets fired when setCommand() is called.
     **/
    public void Var017()
    {
        try
        {
            String command1 = "YO DAD";
            String command2 = "HI MOM";
            CommandCall cmd = new CommandCall(systemObject_, command1);
            VetoableChangeListener_ l = new VetoableChangeListener_();
            cmd.addVetoableChangeListener(l);
            PropertyChangeEvent before = l.getLastEvent();
            cmd.setCommand(command2);
            PropertyChangeEvent after = l.getLastEvent();
            assertCondition((before == null) && (after.getSource() == cmd) && (after.getPropertyName().equals("command")) && (after.getOldValue().equals(command1)) && (after.getNewValue().equals(command2)));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     addVetoableChangeListener().  Add a valid listener.  Verify that an event gets fired when setCommand() is called.
     **/
    public void Var018()
    {
        try
        {
            AS400 system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId());
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            VetoableChangeListener_ l = new VetoableChangeListener_();
            cmd.addVetoableChangeListener(l);
            PropertyChangeEvent before = l.getLastEvent();
            cmd.setSystem(system);
            PropertyChangeEvent after = l.getLastEvent();
            assertCondition((before == null) && (after.getSource() == cmd) && (after.getPropertyName().equals("system")) && (after.getOldValue() == systemObject_) && (after.getNewValue() == system));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     removeVetoableChangeListener().  Add null.
     **/
    public void Var019()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            cmd.removeVetoableChangeListener(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     removeVetoableChangeListener().  Removes a valid listener.  Verify that no events get fired when setCommand() is called.
     **/
    public void Var020()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_, "PING " + systemObject_.getSystemName());
            VetoableChangeListener_ l = new VetoableChangeListener_();
            cmd.addVetoableChangeListener(l);
            cmd.removeVetoableChangeListener(l);
            PropertyChangeEvent before = l.getLastEvent();
            cmd.setCommand("DSPDRVLVL");
            PropertyChangeEvent after = l.getLastEvent();
            assertCondition((before == null) && (after == null));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     removeVetoableChangeListener().  Removes a valid listener.  Verify that no events get fired when setSystem() is called.
     **/
    public void Var021()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            VetoableChangeListener_ l = new VetoableChangeListener_();
            cmd.addVetoableChangeListener(l);
            cmd.removeVetoableChangeListener(l);
            PropertyChangeEvent before = l.getLastEvent();
            cmd.setSystem(new AS400());
            PropertyChangeEvent after = l.getLastEvent();
            assertCondition((before == null) && (after == null));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
