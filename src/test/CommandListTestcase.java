///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CommandListTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Properties;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.Command;
import com.ibm.as400.access.CommandList;


import test.Testcase;

/**
  Testcase CommandListTestcase.
 **/
public class CommandListTestcase extends Testcase
{
   private boolean rejectChange = false;
   
   /**
     Constructor.  This is called from the HTMLTest constructor.
    **/
   public CommandListTestcase(AS400 systemObject,
                                 Vector variationsToRun,
                                 int runMode,
                                 FileOutputStream fileOutputStream)
   {
      super(systemObject, "CommandListTestcase", variationsToRun,
            runMode, fileOutputStream);
   }

   
   /**
     The PropertyChangeListener_ class is for testing.
    **/
   private class PropertyChangeListener_
   implements PropertyChangeListener
   {
      private PropertyChangeEvent lastEvent_;
      
      public void propertyChange (PropertyChangeEvent event)
      {
         lastEvent_ = event;
      }
      
      public PropertyChangeEvent getLastEvent ()
      {
         return lastEvent_;
      }
   }

   
   /**
     Successful construction of an CommandList using the default ctor.

     CommandList()
    **/
   public void Var001()
   {
      try
      {
         CommandList cmdList = new CommandList();
         succeeded();
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
     Successful construction of an CommandList using the system, library, and command parameters..

     CommandList(AS400, String, String)
    **/
   public void Var002()
   {
      try
      {
         CommandList cmdList = new CommandList(systemObject_, "qsys", "crtlib");
         succeeded();
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
     Construct an CommandList with the default ctor.
     Set the command and rretrieve it to verify setting.
     
     CommandList()
     CommandList::setCommand(String)
     CommandList::getCommand()
    **/
   public void Var003()
   {
      try
      {
         CommandList cmdList = new CommandList();
         cmdList.setCommand("CRTLIB");
         if (cmdList.getCommand().equals("CRTLIB"))
         {
            succeeded();
         }
         else
            failed("Incorrect command returned.");
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
     Construct an CommandList with the default ctor.
     Set the system and retrieve it to verify setting.
     
     CommandList()
     CommandList::setSystem(AS400)
     CommandList::getSystem()
    **/
   public void Var004()
   {
      try
      {
         CommandList cmdList = new CommandList();
         cmdList.setSystem(new AS400("test", "uid", "pwd"));
         if (cmdList.getSystem().getSystemName().equals("test"))
         {
            succeeded();
         }
         else
            failed("Incorrect system returned.");
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
     Construct an CommandList with the default ctor.
     Set the library and retrieve it to verify setting.
     
     CommandList()
     CommandList::setLibrary(String)
     CommandList::getLibrary()
    **/
   public void Var005()
   {
      try
      {
         CommandList cmdList = new CommandList();
         cmdList.setLibrary("qsys");
         if (cmdList.getLibrary().equals("qsys"))
         {
            succeeded();
         }
         else
            failed("Incorrect library returned.");
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }
   
   
   /**
     Construct an CommandList with the default ctor.
     Set the command with a null cmdList parm.
     A NullPointerException should be thrown
     
     CommandList()
     CommandList::setCommand(null)
     **/
   public void Var006()
   {
      try
      {
         CommandList cmdList = new CommandList();
         cmdList.setCommand(null);
         failed("No Exception.");
      }
      catch(Exception e)
      {
         if (exceptionIs(e, "NullPointerException", "command"))
         {
            succeeded();
         }
         else
         {
            failed(e, "Wrong exception info.");
         }
      }
   }

   /**
     Construct an CommandList with the default ctor.
     Set the system with a null value.
     A NullPointerException should be thrown
     
     CommandList()
     CommandList::setSystem(null)
    **/
   public void Var007()
   {
      try
      {
         CommandList cmdList = new CommandList();
         cmdList.setSystem(null);
         failed("No Exception.");
      }
      catch(Exception e)
      {
         if (exceptionIs(e, "NullPointerException", "system"))
         {
            succeeded();
         }
         else
         {
            failed(e, "Wrong exception info.");
         }
      }
   }

   /**
     Construct an CommandList with the default ctor.
     Set the library with a null parm.
     A NullPointerException should be thrown
     
     CommandList()
     CommandList::setLibrary(null)
     **/
   public void Var008()
   {
      try
      {
         CommandList cmdList = new CommandList();
         cmdList.setLibrary(null);
         failed("No Exception.");
      }
      catch(Exception e)
      {
         if (exceptionIs(e, "NullPointerException", "library"))
         {
            succeeded();
         }
         else
         {
            failed(e, "Wrong exception info.");
         }
      }
   }

   
   /**
     Construct an CommandList with the data and cmdList parm.
     
     CommandList(AS400, String, String)
     CommandList::generateList()
    **/
   public void Var009()
   {
      try
      {
         // create a CommandList
         CommandList cmdList = new CommandList(systemObject_, "qsys", "crt*");
         Command[] cmds = cmdList.generateList();

         if (cmds != null && cmds.length != 0)
             succeeded();
         else
             failed("Command list is empty or null");
         
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }


   /**
     Construct an CommandList.
     
     CommandList(AS400, String, String)
     CommandList::toString()
    **/
   public void Var010()
   {
      try
      {
         // create a CommandList
         CommandList cmdList = new CommandList(systemObject_, "qsys", "crt*");
         String cmds = cmdList.toString();

         if (cmds != null && cmds.length() != 0)
             succeeded();
         else
             failed("Command list is empty or null");
         
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }


   /**
     Serialization.  Verify that the object is properly serialized
     when the data, cmdList, are not set.
    
     CommandList
    **/
   public void Var011()                            
   {
      try
      {
         CommandList cmdList = new CommandList ();
         CommandList cmdList2 = (CommandList) serialize (cmdList);
         assertCondition ((cmdList2.getCommand() == null)
                 && (cmdList.getLibrary() == null)
                 && (cmdList.getSystem() == null));
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception");
      }
   }

   /**
     Serialization.  Verify that the object is properly serialized
     when the data, cmdList, language, direction, and attributes are set.
     
     CommandList
    **/
   public void Var012()            
   {
      try
      {
         CommandList cmdList = new CommandList();
         cmdList.setCommand("crtlib");
         cmdList.setLibrary("qsys");
         cmdList.setSystem(new AS400("blah", "blah", "blah"));
         CommandList cmdList2 = (CommandList) serialize (cmdList);
         assertCondition ((cmdList2.getCommand().equals("crtlib"))
                 && (cmdList2.getLibrary().equals("qsys"))
                 && (cmdList2.getSystem().getSystemName().equals("blah"))  );
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception");
      }
   }                 
   


   /**
     CommandList::addPropertyChangeListener().  Add null.
     Verify a NullPointerException is thrown.
    **/
   public void Var013()
   {
      try
      {
         CommandList cmdList = new CommandList ();
         cmdList.addPropertyChangeListener(null);
         failed ("Did not throw exception.");
      }
      catch (Exception e)
      {
         assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
      }
   }

   

   /**
     CommandList::addPropertyChangeListener().  Add a valid listener.
     Verify that an event gets fired when setCommand() is called.
    **/
   public void Var014()
   {
      try
      {
         CommandList cmdList = new CommandList ();
         PropertyChangeListener_ l = new PropertyChangeListener_ ();
         cmdList.addPropertyChangeListener (l);
         PropertyChangeEvent before = l.getLastEvent ();
         cmdList.setCommand("crtlib");
         PropertyChangeEvent after = l.getLastEvent ();
         assertCondition ((before == null) && (after.getSource () == cmdList)
                 && (after.getPropertyName ().equals ("command"))
                 && (after.getOldValue () == null)
                 && (after.getNewValue ().equals("crtlib") ));
      }
      catch (Exception e) {
         failed(e, "Unexpected exception");
      }
   }

   /**
     CommandList::addPropertyChangeListener().  Add a valid listener.
     Verify that an event gets fired when setLibrary() is called.
    **/
   public void Var015()
   {
      try
      {
         CommandList cmdList = new CommandList ();
         PropertyChangeListener_ l = new PropertyChangeListener_ ();
         cmdList.addPropertyChangeListener (l);
         PropertyChangeEvent before = l.getLastEvent ();
         cmdList.setLibrary("qsys");
         PropertyChangeEvent after = l.getLastEvent ();
         assertCondition ((before == null) && (after.getSource () == cmdList)
                 && (after.getPropertyName ().equals ("library"))
                 && (after.getOldValue () == null)
                 && (after.getNewValue ().equals("qsys")) );
      }
      catch (Exception e) {
         failed(e, "Unexpected exception");
      }
   }

   /**
     CommandList::addPropertyChangeListener().  Add a valid listener.
     Verify that an event gets fired when setSystem() is called.
    **/
   public void Var016()
   {
      try
      {
         CommandList cmdList = new CommandList ();
         PropertyChangeListener_ l = new PropertyChangeListener_ ();
         cmdList.addPropertyChangeListener (l);
         PropertyChangeEvent before = l.getLastEvent ();
         AS400 sys = new AS400("blah", "blah", "blah");
         cmdList.setSystem(sys);
         PropertyChangeEvent after = l.getLastEvent ();
         assertCondition ((before == null) && (after.getSource () == cmdList)
                 && (after.getPropertyName ().equals ("system"))
                 && (after.getOldValue () == null)
                 && (after.getNewValue () == sys) );
      }
      catch (Exception e) {
         failed(e, "Unexpected exception");
      }
   }

   
   /**
     CommandList::removePropertyChangeListener().  Remove null.
    **/
   public void Var017()
   {
      try {
         CommandList cmdList = new CommandList ();
         cmdList.removePropertyChangeListener (null);
         failed ("Did not throw exception.");
      }
      catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
      }
   }

   

   /**
     CommandList::removePropertyChangeListener().  Remove a valid listener.
     Verify that no events get fired when setCommand() is called.
    **/
   public void Var018()
   {
      try {
         CommandList cmdList = new CommandList ();
         PropertyChangeListener_ l = new PropertyChangeListener_ ();
         cmdList.addPropertyChangeListener (l);
         cmdList.removePropertyChangeListener (l);
         PropertyChangeEvent before = l.getLastEvent ();
         cmdList.setCommand("crtlib");
         PropertyChangeEvent after = l.getLastEvent ();
         assertCondition ((before == null) && (after == null));
      }
      catch (Exception e) {
         failed(e, "Unexpected exception");
      }
   }


   /**
     CommandList::removePropertyChangeListener().  Remove a valid listener.
     Verify that no events get fired when setLibrary() is called.
    **/
   public void Var019()
   {
      try {
         CommandList cmdList = new CommandList ();
         PropertyChangeListener_ l = new PropertyChangeListener_ ();
         cmdList.addPropertyChangeListener (l);
         cmdList.removePropertyChangeListener (l);
         PropertyChangeEvent before = l.getLastEvent ();
         cmdList.setLibrary("qsys");
         PropertyChangeEvent after = l.getLastEvent ();
         assertCondition ((before == null) && (after == null));
      }
      catch (Exception e) {
         failed(e, "Unexpected exception");
      }
   }

   /**
     CommandList::removePropertyChangeListener().  Remove a valid listener.
     Verify that no events get fired when setSystem() is called.
    **/
   public void Var020()
   {
      try {
         CommandList cmdList = new CommandList ();
         PropertyChangeListener_ l = new PropertyChangeListener_ ();
         cmdList.addPropertyChangeListener (l);
         cmdList.removePropertyChangeListener (l);
         PropertyChangeEvent before = l.getLastEvent ();
         cmdList.setSystem(new AS400("blah", "blah", "blah"));
         PropertyChangeEvent after = l.getLastEvent ();
         assertCondition ((before == null) && (after == null));
      }
      catch (Exception e) {
         failed(e, "Unexpected exception");
      }
   }

   

   /**
     Construct an CommandList with the default ctor.
     Get the list of commands, without setting the parms.
     A ExtendedIllegalStateException should be thrown.
     
     CommandList()
    **/
   public void Var021()
   {
      try
      {
         CommandList cmdList = new CommandList();
         cmdList.generateList();
         failed("No Exception.");
      }
      catch(Exception e)
      {
         if (exceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set."))
         {
            succeeded();
         }
         else
         {
            failed(e, "Wrong exception info.");
         }
      }
   }


   /**
     Construct an CommandList with the default ctor.
     Get the list of commands, without setting the parms.
     A ExtendedIllegalStateException should be thrown.
     
     CommandList()
    **/
   public void Var022()
   {
      try
      {
         CommandList cmdList = new CommandList();
         cmdList.setSystem(systemObject_);
         cmdList.generateList();
         failed("No Exception.");
      }
      catch(Exception e)
      {
         if (exceptionIs(e, "ExtendedIllegalStateException", "library: Property is not set."))
         {
            succeeded();
         }
         else
         {
            failed(e, "Wrong exception info.");
         }
      }
   }


   /**
     Construct an CommandList with the default ctor.
     Get the list of commands, without setting the parms.
     A ExtendedIllegalStateException should be thrown.
     
     CommandList()
    **/
   public void Var023()
   {
      try
      {
         CommandList cmdList = new CommandList();
         cmdList.setSystem(systemObject_);
         cmdList.setLibrary("qsys");
         cmdList.generateList();
         failed("No Exception.");
      }
      catch(Exception e)
      {
         if (exceptionIs(e, "ExtendedIllegalStateException", "command: Property is not set."))
         {
            succeeded();
         }
         else
         {
            failed(e, "Wrong exception info.");
         }
      }
   }

   /**
     Construct an CommandList with the data and cmdList parm.
     
     CommandList(AS400, String, String)
     CommandList::generateList()
    **/
   public void Var024()
   {
      try
      {
         // create a CommandList
         CommandList cmdList = new CommandList(systemObject_, "qsys", "*ALL");
         Command[] cmds = cmdList.generateList();

         if (cmds != null && cmds.length != 0)
             succeeded();
         else
             failed("Command list is empty or null");
         
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }


   /**
     Construct an CommandList.
     
     CommandList(AS400, String, String)
     CommandList::generateList()
    **/
   public void Var025()
   {
      try
      {
         // create a CommandList
         CommandList cmdList = new CommandList(systemObject_, "qsys", "crtlib");
         Command[] cmds = cmdList.generateList();

         if (cmds != null && cmds.length == 1)
             succeeded();
         else
             failed("Command list is empty or null");
         
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }


   /**
     Construct an CommandList with the data and cmdList parm.
     
     CommandList(AS400, String, String)
     CommandList::generateList()
    **/
   public void Var026()
   {
      try
      {
         // create a CommandList
         CommandList cmdList = new CommandList(systemObject_, "qsys", "blah");
         Command[] cmds = cmdList.generateList();

         if (cmds != null && cmds.length == 0)
             succeeded();
         else
             failed("Command list is empty or null");
         
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }


   /**
     Serializes and deserializes the CommandList object.
    **/
   private CommandList serialize (CommandList cmdList)
   throws Exception
   {
      // Serialize.
      String serializeFileName = "CommandListbeans.ser";
      ObjectOutput out = new ObjectOutputStream (new FileOutputStream (serializeFileName));
      out.writeObject (cmdList);
      out.flush ();
      
      // Deserialize.
      CommandList head2 = null;
      try {
         ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFileName));
         head2 = (CommandList) in.readObject ();
      }
      finally {
         File f = new File (serializeFileName);
         f.delete();
      }
      return head2;
   }
}


