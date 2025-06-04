///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400ConnectionPoolSerializationTestcase
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import com.ibm.as400.access.AS400ConnectionPool;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ConnectionPoolListener;
import com.ibm.as400.access.EventLog;

import test.PasswordVault;
import test.Testcase;

import com.ibm.as400.access.ConnectionPoolEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 *Testcase AS400ConnectionPoolSerializationTestcase.  This testcase verifies the ability
 *to serialize and deserialize AS400ConnectionPool objects.
**/
public class AS400ConnectionPoolSerializationTestcase extends Testcase
{
  
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "AS400ConnectionPoolSerializationTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.AS400ConnectionPoolTest.main(newArgs); 
   }

  FileInputStream ris = null;
  ObjectInputStream rin = null;
  EventLog exampleLog_ = null;
  CommandCall cmd_;

  /**
   @exception  Exception  If an exception occurs.
   **/
  public void setup()
    throws Exception
  {
    exampleLog_ = new EventLog();
    cmd_ = new CommandCall(systemObject_);
  }

/**
  Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    try
    {
      systemObject_.connectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      System.out.println("Unable to connect to the AS400.");
      e.printStackTrace(output_);
    }

    if ((allVariations || variationsToRun_.contains("1")))
    {
      setVariation(1);
      Var001();
    }
    if ((allVariations || variationsToRun_.contains("2")))
    {
      setVariation(2);
      Var002();
    }
    if ((allVariations || variationsToRun_.contains("3")) )
    {
      setVariation(3);
      Var003();
    }
    if ((allVariations || variationsToRun_.contains("4")) )
    {
      setVariation(4);
      Var004();
    }
    if ((allVariations || variationsToRun_.contains("5")) )
    {
      setVariation(5);
      Var005();
    }

    try
    {
      cleanup();
    }
    catch (Exception e)
    {
      System.out.println("Cleanup failed.");
      return;
    }
    systemObject_.disconnectService(AS400.RECORDACCESS);
  }

String runCommand(String command)
{
  String msg = null;

  try
  {
    // Run the command.
    cmd_.run(command);

    // If there are any messages then save the ones that potentially
    // indicate problems.
    AS400Message[] msgs = cmd_.getMessageList();
    if (msgs.length > 0 && msgs[0].getID().toUpperCase().startsWith("CPF"))
    {
      msg = msgs[0].getID().toUpperCase();
    }
  }
  catch(Exception e)
  {
    msg = e.toString();
    e.printStackTrace(output_);
  }

  return msg;
}

/**
 @exception  Exception  If an exception occurs.
 **/
  protected void cleanup()
    throws Exception
  {
    boolean success = true;
    try
    {
      File f = new File("as400cpser.ser");
      f.delete();
    }
    catch(Exception e) {}

    if (!success)
    {
      throw new Exception("Cleanup was unsuccessful");
    }
  }

  /**
   *Verify the ability to serialize and deserialize an AS400ConnectionPool object.
   *<ul compact>
   *<li>When the object was constructed with the null constructor and
   *no other methods have been called.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The AS400ConnectionPool object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The pool can be used.
   *</ul>
  **/
  public void Var001()
  {
    if (runMode_ == ATTENDED) {
	notApplicable("non attended testcase");
	return; 
    }

    AS400ConnectionPool p = null;
    AS400ConnectionPool deserp = null;
    try
    {
      p = new AS400ConnectionPool();
      // Serialize
      FileOutputStream ros = new FileOutputStream("as400cpser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(p);
      rout.flush();
      ros.close();
      // Deserialize
      ris = new FileInputStream("as400cpser.ser");
      rin = new ObjectInputStream(ris);
      deserp = (AS400ConnectionPool)rin.readObject();
      rin.close();
      ris.close();
      // Verify state
      if (deserp.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getActiveConnectionCount() != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getAvailableConnectionCount() != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getCleanupInterval() != 300000)
      {
        failed("getCleanupInterval() != 300000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxConnections() != -1)
      {
        failed("getMaxConnections() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxInactivity() != 3600000)
      {
        failed("getMaxInactivity() != 3600000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxLifetime() != 86400000)
      {
        failed("getMaxLifetime() != 86400000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxUseCount() != -1)
      {
        failed("getMaxUseCount() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxUseTime() != -1)
      {
        failed("getMaxUseTime() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0");
        p.close();
        deserp.close();
        return;
      }
      if (!deserp.isRunMaintenance())
      {
        failed("isRunMaintenance()");
        p.close();
        deserp.close();
        return;
      }
      if (!deserp.isThreadUsed())
      {
        failed("isThreadUsed()");
        p.close();
        deserp.close();
        return;
      }
      // Verify usability
      deserp.addConnectionPoolListener(new ExampleListener());
      deserp.addPropertyChangeListener(new ExamplePropertyChangeListener());
      AS400 o = deserp.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), 
          PasswordVault.decryptPassword(encryptedPassword_));
      deserp.returnConnectionToPool(o);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      e.printStackTrace();
      try
      {
        p.close();
        deserp.close();
      }
      catch(Exception e1) {}
      return;
    }
    try
    {
      p.close();
      deserp.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      e.printStackTrace();
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an AS400ConnectionPool object.
   *<br><b>Note:</b> This is an attended variation.
   *<ul compact>
   *<li>When the object was constructed with the null constructor, the file
   *has not been opened, the log has been set and other commands run.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The AS400ConnectionPool object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The pool can be used.
   *</ul>
  **/
  public void Var002()
  {
    if (runMode_ == UNATTENDED) {
	notApplicable("Attended testcase");
	return; 
    } 
    AS400ConnectionPool p = null;
    AS400ConnectionPool deserp = null;
    try
    {
      p = new AS400ConnectionPool();
      // Set some of the state
      p.setLog(exampleLog_);

      // Serialize
      FileOutputStream ros = new FileOutputStream("as400cpser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(p);
      rout.flush();
      ros.close();
      // Deserialize
      ris = new FileInputStream("as400cpser.ser");
      rin = new ObjectInputStream(ris);
      deserp = (AS400ConnectionPool)rin.readObject();
      rin.close();
      ris.close();
      // Verify state
      if (deserp.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getActiveConnectionCount() != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getAvailableConnectionCount() != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getCleanupInterval() != 300000)
      {
        failed("getCleanupInterval() != 300000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxConnections() != -1)
      {
        failed("getMaxConnections() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxInactivity() != 3600000)
      {
        failed("getMaxInactivity() != 3600000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxLifetime() != 86400000)
      {
        failed("getMaxLifetime() != 86400000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxUseCount() != -1)
      {
        failed("getMaxUseCount() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxUseTime() != -1)
      {
        failed("getMaxUseTime() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0");
        p.close();
        deserp.close();
        return;
      }
      if (!deserp.isRunMaintenance())
      {
        failed("isRunMaintenance()");
        p.close();
        deserp.close();
        return;
      }
      if (!deserp.isThreadUsed())
      {
        failed("isThreadUsed()");
        p.close();
        deserp.close();
        return;
      }

      // Verify usability
      deserp.addConnectionPoolListener(new ExampleListener());
      deserp.addPropertyChangeListener((PropertyChangeListener) new ExamplePropertyChangeListener());
      deserp.fill(systemObject_.getSystemName(), systemObject_.getUserId(), PasswordVault.decryptPassword(encryptedPassword_), AS400.FILE, 5);
      deserp.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), PasswordVault.decryptPassword(encryptedPassword_));
      deserp.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId());
      deserp.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId());
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      e.printStackTrace();
      try
      {
        p.close();
        deserp.close();
      }
      catch(Exception e1) {}
      return;
    }
    try
    {
      p.close();
      deserp.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      e.printStackTrace();
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an AS400ConnectionPool object.
   *<br><b>Note:</b> This is an attended variation.
   *<ul compact>
   *<li>When the object was constructed with the null constructor and
   *the file has been opened.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The AS400ConnectionPool object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to opening the file.
   *<li>Any listeners added prior to serialization are no longer getting notified of
   *when an event is fired.
   *<li>Listeners can be added to the object.
   *<li>Commitment control can be started on the object.
   *<li>The file can be locked.
   *<li>The file can be opened and used.
   *</ul>
  **/
  public void Var003()
  {
    if (runMode_ == UNATTENDED) {
	notApplicable("Attended testcase");
	return; 
    } 

    AS400ConnectionPool p = null;
    AS400ConnectionPool deserp = null;
    try
    {
      ExampleListener l1 = new ExampleListener();
      ExamplePropertyChangeListener l2 = new ExamplePropertyChangeListener();
      p = new AS400ConnectionPool();
      // Set some of the state and open the file
      p.setLog(exampleLog_);
      p.addConnectionPoolListener(l1);
      p.addPropertyChangeListener((PropertyChangeListener) l2);
      // Serialize
      FileOutputStream ros = new FileOutputStream("as400cpser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(p);
      rout.flush();
      ros.close();
      p.close();
      // Reset the listener variables to false
      l1.closed_ = false;
      l1.created_ = false;
      l1.expired_ = false;
      l1.maintenance_ = false;
      l1.released_ = false;
      l1.returned_ = false;
      l2.setChanged(false);
      // Deserialize
      ris = new FileInputStream("as400cpser.ser");
      rin = new ObjectInputStream(ris);
      deserp = (AS400ConnectionPool)rin.readObject();
      rin.close();
      ris.close();
       // Verify state
      if (deserp.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getActiveConnectionCount() != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getAvailableConnectionCount() != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getCleanupInterval() != 300000)
      {
        failed("getCleanupInterval() != 300000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxConnections() != -1)
      {
        failed("getMaxConnections() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxInactivity() != 3600000)
      {
        failed("getMaxInactivity() != 3600000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxLifetime() != 86400000)
      {
        failed("getMaxLifetime() != 86400000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxUseCount() != -1)
      {
        failed("getMaxUseCount() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxUseTime() != -1)
      {
        failed("getMaxUseTime() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0");
        p.close();
        deserp.close();
        return;
      }
      if (!deserp.isRunMaintenance())
      {
        failed("isRunMaintenance()");
        p.close();
        deserp.close();
        return;
      }
      if (!deserp.isThreadUsed())
      {
        failed("isThreadUsed()");
        p.close();
        deserp.close();
        return;
      }

      // Verify usability
      deserp.addConnectionPoolListener(new ExampleListener());
      deserp.addPropertyChangeListener((PropertyChangeListener) new ExamplePropertyChangeListener());

      ExampleListener dl1 = new ExampleListener();
      ExamplePropertyChangeListener dl2 = new ExamplePropertyChangeListener();
      deserp.addConnectionPoolListener(dl1);
      deserp.addPropertyChangeListener(dl2);
      deserp.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), (char[]) null );
      // Verify file listener added after deserialization
      if (!dl1.created_)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      // Verify pre-serialized FileListener no longer affected
      if (l1.created_)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
      deserp.setCleanupInterval(200);
      // Verify current listeners
      if (!dl2.isChanged())
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      // Verify no one listening from pre-serialization
      if (l2.isChanged())
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      e.printStackTrace();
      try
      {
        p.close();
        deserp.close();
      }
      catch(Exception e1) {}
      return;
    }
    try
    {
      p.close();
      deserp.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      e.printStackTrace();
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an AS400ConnectionPool object.
   *<br><b>Note:</b> This is an attended variation.
   *<ul compact>
   *<li>When the object was constructed with the constructor
   *that takes an AS400 and String and no other methods have been called.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The AS400ConnectionPool object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The file can be opened and used.
   *</ul>
  **/
  public void Var004()
  {
    if (runMode_ == UNATTENDED) {
	notApplicable("Attended testcase");
	return; 
    } 

    AS400ConnectionPool p = null;
    AS400ConnectionPool deserp = null;
    try
    {
      p = new AS400ConnectionPool();
      // Serialize
      FileOutputStream ros = new FileOutputStream("as400cpser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(p);
      rout.flush();
      ros.close();
      // Deserialize
      ris = new FileInputStream("as400cpser.ser");
      rin = new ObjectInputStream(ris);
      deserp = (AS400ConnectionPool)rin.readObject();
      rin.close();
      ris.close();
       // Verify state
      if (deserp.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getActiveConnectionCount() != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getAvailableConnectionCount() != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getCleanupInterval() != 300000)
      {
        failed("getCleanupInterval() != 300000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxConnections() != -1)
      {
        failed("getMaxConnections() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxInactivity() != 3600000)
      {
        failed("getMaxInactivity() != 3600000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxLifetime() != 86400000)
      {
        failed("getMaxLifetime() != 86400000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxUseCount() != -1)
      {
        failed("getMaxUseCount() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxUseTime() != -1)
      {
        failed("getMaxUseTime() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0");
        p.close();
        deserp.close();
        return;
      }
      if (!deserp.isRunMaintenance())
      {
        failed("isRunMaintenance()");
        p.close();
        deserp.close();
        return;
      }
      if (!deserp.isThreadUsed())
      {
        failed("isThreadUsed()");
        p.close();
        deserp.close();
        return;
      }

      // Verify usability
      deserp.addConnectionPoolListener(new ExampleListener());
      deserp.addPropertyChangeListener((PropertyChangeListener) new ExamplePropertyChangeListener());
      deserp.fill(systemObject_.getSystemName(), systemObject_.getUserId(), PasswordVault.decryptPassword(encryptedPassword_), AS400.FILE, 5);
      deserp.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), PasswordVault.decryptPassword(encryptedPassword_));
      deserp.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId());
      deserp.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId());
      deserp.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      e.printStackTrace();
      try
      {
        p.close();
        deserp.close();
      }
      catch(Exception e1) {}
      return;
    }
    try
    {
      p.close();
      deserp.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      e.printStackTrace();
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an AS400ConnectionPool object.
   *<br><b>Note:</b> This is an attended variation.
   *<ul compact>
   *<li>When the object was constructed with the constructor
   *that takes an AS400 and String and the file has been opened.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The AS400ConnectionPool object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to opening the file.
   *<li>The file can be opened and used.
   *</ul>
  **/
  public void Var005()
  {
    if (runMode_ == UNATTENDED) {
	notApplicable("Attended testcase");
	return; 
    } 

    AS400ConnectionPool p = null;
    AS400ConnectionPool deserp = null;
    try
    {
      ExampleListener l1 = new ExampleListener();
      ExamplePropertyChangeListener l2 = new ExamplePropertyChangeListener();
      p = new AS400ConnectionPool();
      // Set some of the state and open the file
      p.addConnectionPoolListener(l1);
      p.addPropertyChangeListener(l2);
      // Serialize
      FileOutputStream ros = new FileOutputStream("as400cpser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(p);
      rout.flush();
      ros.close();
      p.close();
      // Reset the listener variables to false
      l1.closed_ = false;
      l1.created_ = false;
      l1.expired_ = false;
      l1.maintenance_ = false;
      l1.released_ = false;
      l1.returned_ = false;
      l2.setChanged(false);
      // Deserialize
      ris = new FileInputStream("as400cpser.ser");
      rin = new ObjectInputStream(ris);
      deserp = (AS400ConnectionPool)rin.readObject();
      rin.close();
      ris.close();
       // Verify state
      if (deserp.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getActiveConnectionCount() != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getAvailableConnectionCount() != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getCleanupInterval() != 300000)
      {
        failed("getCleanupInterval() != 300000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxConnections() != -1)
      {
        failed("getMaxConnections() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxInactivity() != 3600000)
      {
        failed("getMaxInactivity() != 3600000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxLifetime() != 86400000)
      {
        failed("getMaxLifetime() != 86400000");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxUseCount() != -1)
      {
        failed("getMaxUseCount() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getMaxUseTime() != -1)
      {
        failed("getMaxUseTime() != -1");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getActiveConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0");
        p.close();
        deserp.close();
        return;
      }
      if (deserp.getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0)
      {
        failed("getAvailableConnectionCount(systemObject_.getSystemName(), systemObject_.getUserId()) != 0");
        p.close();
        deserp.close();
        return;
      }
      if (!deserp.isRunMaintenance())
      {
        failed("isRunMaintenance()");
        p.close();
        deserp.close();
        return;
      }
      if (!deserp.isThreadUsed())
      {
        failed("isThreadUsed()");
        p.close();
        deserp.close();
        return;
      }

      // Verify usability
      deserp.addConnectionPoolListener(new ExampleListener());
      deserp.addPropertyChangeListener((PropertyChangeListener) new ExamplePropertyChangeListener());
      ExampleListener dl1 = new ExampleListener();
      ExamplePropertyChangeListener dl2 = new ExamplePropertyChangeListener();
      deserp.addConnectionPoolListener(dl1);
      deserp.addPropertyChangeListener(dl2);
      deserp.getConnection(systemObject_.getSystemName(), systemObject_.getUserId(), PasswordVault.decryptPassword(encryptedPassword_));
      // Verify file listener added after deserialization
      if (!dl1.created_)
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      // Verify pre-serialized FileListener no longer affected
      if (l1.created_)
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
      deserp.setCleanupInterval(200);
      // Verify current listeners
      if (!dl2.isChanged())
      {
        failed("Listeners added after deserialization not getting events.");
        return;
      }
      // Verify no one listening from pre-serialization
      if (l2.isChanged())
      {
        failed("Listeners maintained from pre-serialize.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      e.printStackTrace();
      try
      {
        p.close();
        deserp.close();
      }
      catch(Exception e1) {}
      return;
    }
    try
    {
      p.close();
      deserp.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      e.printStackTrace();
      return;
    }
    succeeded();
  }

  class ExamplePropertyChangeListener implements PropertyChangeListener
   {
      private boolean change_ = false;

      public ExamplePropertyChangeListener()
      {
      }
      public boolean isChanged()
      {
         boolean value = change_;
         change_ = false;    // reset.
         return value;
      }
      public void propertyChange(PropertyChangeEvent event)
      {
         change_ = true;
      }
      public void setChanged(boolean change)
      {
         change_ = change; 
      }
   }

  class ExampleListener implements ConnectionPoolListener
   {
      private boolean closed_ = false;
      private boolean created_ = false;
      private boolean expired_ = false;
      private boolean released_ = false;
      private boolean returned_ = false;
      private boolean maintenance_ = false;

      public ExampleListener()
      {
         
      }
      public void connectionPoolClosed(ConnectionPoolEvent event)
      {
         closed_ = true;
      }
      public void connectionCreated(ConnectionPoolEvent event)
      {
         created_ = true;
      }
      public void connectionExpired(ConnectionPoolEvent event)
      {
         expired_ = true;
      }
      public void connectionReleased(ConnectionPoolEvent event)
      {
         released_ = true;
      }
      public void connectionReturned(ConnectionPoolEvent event)
      {
         returned_ = true;
      }
      public void maintenanceThreadRun(ConnectionPoolEvent event)
      {
         maintenance_ = true;
      }
      public boolean wasClosed()
      {
         return closed_;
      }
      public boolean wasCreated()
      {
         return created_;
      }
      public boolean wasExpired()
      {
         return expired_;
      }
      public boolean wasReleased()
      {
         return released_;
      }
      public boolean wasReturned()
      {
         return returned_;
      }
      public boolean wasMaintenance()
      {
         return maintenance_;
      }
   }
}

