///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalSerialization.java
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
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Vector;
import com.ibm.as400.access.*;

/**
 *Testcase SysvalSerialization.  This testcase verifies the ability
 *to serialize and deserialize SystemValue and SystemValueList objects.
**/
public class SysvalSerialization extends Testcase
{
//  static final File CURRENT_DIR = new File (System.getProperty("user.dir"));
  CommandCall cmd_;
  FileInputStream ris = null;
  ObjectInputStream rin = null;
  String fileName_ = null;

  ///String testLib_ = null;

/**
  Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);
/*
    try
    {
      systemObject_.connectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      System.out.println("Unable to connect to the AS400.");
      e.printStackTrace(output_);
    }
*/
    try
    {
      setup();
    }
    catch (Exception e)
    {
      System.out.println("Unable to run setup.");
      ///systemObject_.disconnectService(AS400.RECORDACCESS);
      return;
    }

    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }
    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)
    {
      setVariation(2);
      Var002();
    }
    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      Var003();
    }
    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED)
    {
      setVariation(4);
      Var004();
    }
    if ((allVariations || variationsToRun_.contains("5")) &&
        runMode_ != ATTENDED)
    {
      setVariation(5);
      Var005();
    }
/*
    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != UNATTENDED)
    {
      setVariation(6);
      Var006();
    }
    if ((allVariations || variationsToRun_.contains("7")) &&
        runMode_ != UNATTENDED)
    {
      setVariation(7);
      Var007();
    }
    if ((allVariations || variationsToRun_.contains("8")) &&
        runMode_ != ATTENDED)
    {
      setVariation(8);
      Var008();
    }
    if ((allVariations || variationsToRun_.contains("9")) &&
        runMode_ != UNATTENDED)
    {
      setVariation(9);
      Var009();
    }
*/

    try
    {
      cleanup();
    }
    catch (Exception e)
    {
      System.out.println("Cleanup failed.");
      return;
    }
    ///systemObject_.disconnectService(AS400.RECORDACCESS);
  }

/*
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
*/

    /**
     @exception  Exception  If an exception occurs.
     **/
  protected void setup()
    throws Exception
  {
/* TBD - jlee
    try
    {
    // Create library DDMTEST
    String msg = runCommand("CRTLIB LIB(" + testLib_ + ") AUT(*ALL)");
    if (msg != null && !msg.equals("CPF2111"))
    {
      output_.println("Failure executing 'CRTLIB LIB(" + testLib_ + ") AUT(*ALL)'");
      output_.println(msg);
      throw new Exception("");
    }

      // Create the necessary file
      SequentialFile f1 = new SequentialFile(systemObject_, fileName_);
      f1.create(new DDMChar10NoKeyFormat(), "SysvalSerialization testcase file");

      // Populate the file
      Record[] records = new Record[50];
      RecordFormat rf = f1.getRecordFormat();
      for (short i = 0; i < 50; ++i)
      {
        records[i] = rf.getNewRecord();
        records[i].setField(0, "RECORD " + String.valueOf(i));
      }
      f1.open(AS400File.WRITE_ONLY, 50, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records);
      f1.close();

    // Create journal receiver and journal if it does not already exist
    msg = runCommand("CRTJRNRCV JRNRCV(QGPL/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')");
    if (msg != null)
    {
      output_.println("Failure executing 'CRTJRNRCV JRNRCV(QGPL/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')'");
      output_.println(msg);
      throw new Exception("");
    }
    msg = runCommand("CRTJRN JRN(QGPL/JT4DDMJRN) JRNRCV(QGPL/JT4DDMRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM test case journal')");
    if (msg != null)
    {
      output_.println("Failure executing 'CRTJRN JRN(QGPL/JT4DDMJRN) JRNRCV(QGPL/JT4DDMRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM test case journal')'");
      output_.println(msg);
      throw new Exception("");
    }

    // Start journaling
    msg = runCommand("STRJRNPF FILE(" + testLib_ + "/DDMSER) JRN(QGPL/JT4DDMJRN)");
    if (msg != null)
    {
      output_.println("Failure executing 'STRJRNPF FILE(" + testLib_ + "/DDMSER) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      throw new Exception("");
    }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      throw e;
    }
*/
  }

    /**
     @exception  Exception  If an exception occurs.
     **/
  protected void cleanup()
    throws Exception
  {
    boolean success = true;
/* TBD  - jlee
    try
    {
      // Stop journaling
      String msg = runCommand("ENDJRNPF FILE(" + testLib_ + "/DDMSER) JRN(QGPL/JT4DDMJRN)");
      if (msg != null)
      {
        output_.println("Failure executing 'ENDJRNPF FILE(" + testLib_ + "/DDMSER) JRN(QGPL/JT4DDMJRN)'");
        output_.println(msg);
        success = false;
      }

      // Delete files created
      SequentialFile f1 = new SequentialFile(systemObject_, fileName_);
      f1.delete();

      // Delete the DDMTest library and the journal file.  The journal
      // receive should be automatically deleted due to the way that the
      // journal and receiver were deleted.
      msg = runCommand("DLTxLIB " + testLib_ + "");
      if (msg != null)
      {
        output_.println("Failure executing 'DLxTLIB " + testLib_ + "'");
        output_.println(msg);
        success = false;
      }
      msg = runCommand("DLTJRN QGPL/JT4DDMJRN");
      if (msg != null)
      {
        output_.println("Failure executing 'DLTJRN QGPL/JT4DDMJRN'");
        output_.println(msg);
        success = false;
      }
    }
    catch(Exception e)
    {
      System.out.println("Cleanup unsuccessful.");
      e.printStackTrace(output_);
      throw e;
    }
*/

    if (!success)
    {
      throw new Exception("Cleanup was unsuccessful");
    }
  }

/**
Listens for property change events.
**/
    private class PropertyChangeListener_
    implements PropertyChangeListener
    {
        private PropertyChangeEvent lastEvent_ = null;
        private Vector events_ = new Vector ();

        public void propertyChange (PropertyChangeEvent event)
        {
          lastEvent_ = event;
          events_.addElement (event);
        }


        public void clearEvents () { events_.removeAllElements (); }

        public Vector getEvents () { return events_; }
    }

/**
Listens for system value change events.
**/

    private class SystemValueListener_ implements SystemValueListener
    {
      private Vector changed_ = new Vector ();

      /**
       * Invoked when an AS/400 system value is changed by this object.
       * @param event The event.
       * 
       **/
      public void systemValueChanged (SystemValueEvent event)
      {
        changed_.addElement (event);
      }

      public void clearEvents () { changed_.removeAllElements (); }

      public Vector getEvents () { return changed_; }

      boolean listContainsEvent (Vector eventList, SystemValueEvent event)
      {
        Enumeration e = eventList.elements ();
        while (e.hasMoreElements ()) {
          SystemValueEvent listElement = (SystemValueEvent)e.nextElement ();
          if (listElement.getNewValue ().equals (event.getNewValue ()) &&
              listElement.getOldValue ().equals (event.getOldValue ()))
            return true;
        }
        return false;
      }
    }

/**
Listens for vetoable change events.
**/
    private class VetoableChangeListener_
    implements VetoableChangeListener
    {
        private PropertyChangeEvent lastEvent_ = null;
        private Vector events_ = new Vector ();
        private boolean vetoAll_;  // whether or not to veto all changes

        public VetoableChangeListener_ () {}
        public VetoableChangeListener_ (boolean vetoAll)
        {
          vetoAll_ = vetoAll;
        }
        public void vetoableChange (PropertyChangeEvent event)
          throws PropertyVetoException
        {
          if (vetoAll_) throw new PropertyVetoException("I veto everything",
                                                        event);
          lastEvent_ = event;
          events_.addElement (event);
        }


        public void clearEvents () { events_.removeAllElements (); }

        public Vector getEvents () { return events_; }
    }

  /**
   *Verify the ability to serialize and deserialize a SystemValue object.
   *<ul compact>
   *<li>When the object was constructed with the null constructor and
   *no other methods have been called.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The SystemValue object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The system and name can be set for the object.
   *</ul>
  **/
  public void Var001()
  {
    SystemValue sv1 = null;
    SystemValue sv2 = null;
    File serFile = null;
    boolean success = true;
    try
    {
      sv1 = new SystemValue();

      // Add some listeners
      PropertyChangeListener_ pcListener1 = new PropertyChangeListener_();
      VetoableChangeListener_ vcListener1 = new VetoableChangeListener_();
      SystemValueListener_ svListener1 = new SystemValueListener_();

      sv1.addPropertyChangeListener(pcListener1);
      sv1.addVetoableChangeListener(vcListener1);
      sv1.addSystemValueListener(svListener1);

      // Serialize
//      serFile = new File (CURRENT_DIR, "sysval001.ser");
      serFile = new File ("sysval001.ser");
      FileOutputStream ros = new FileOutputStream(serFile);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(sv1);
      rout.flush();
      rout.close();
      ros.close();

      pcListener1.clearEvents();
      vcListener1.clearEvents();
      svListener1.clearEvents();

      // Deserialize
      ris = new FileInputStream(serFile);
      rin = new ObjectInputStream(ris);
      sv2 = (SystemValue)rin.readObject();
      rin.close();
      ris.close();

      // Add some listeners to the new object.
      PropertyChangeListener_ pcListener2 = new PropertyChangeListener_();
      VetoableChangeListener_ vcListener2 = new VetoableChangeListener_();
      SystemValueListener_ svListener2 = new SystemValueListener_();

      sv2.addPropertyChangeListener(pcListener2);
      sv2.addVetoableChangeListener(vcListener2);
      sv2.addSystemValueListener(svListener2);

      // Verify usability
      sv2.setSystem(pwrSys_);
      sv2.setName("QSECOND");
      Object obj = sv2.getValue();
      String toSet = "30";
      sv2.setValue(toSet);
      Object check = sv2.getValue();
      if (!check.equals(toSet))
      {
        output_.println("Object not set correctly: "+(String)check+" != "+(String)toSet);
        success = false;
      }        
      sv2.setValue(obj);
      Object ret = sv2.getValue();
      if (!ret.equals(obj))
      {
        output_.println("Object not reset correctly: "+(String)ret+" != "+(String)obj);
        success = false;
      }
       
      // Verify that the old listeners get no events,
      // and the new listeners get events.
      if (pcListener1.getEvents().size() != 0) {
        output_.println("PropertyChangeListener maintained " +
                        "from pre-serialize.");
        success = false;
      }
      if (vcListener1.getEvents().size() != 0) {
        output_.println("VetoableChangeListener maintained " +
                        "from pre-serialize.");
        success = false;
      }
      if (svListener1.getEvents().size() != 0) {
        output_.println("SystemValueListener maintained " +
                        "from pre-serialize.");
        success = false;
      }
      if (pcListener2.getEvents().size() == 0) {
        output_.println("PropertyChangeListener added after " +
                        "deserialization not getting events.");
        success = false;
      }
      if (vcListener2.getEvents().size() == 0) {
        output_.println("VetoableChangeListener added after " +
                        "deserialization not getting events.");
        success = false;
      }
      if (svListener2.getEvents().size() == 0) {
        output_.println("SystemValueListener added after " +
                        "deserialization not getting events.");
        success = false;
      }

    }
    catch (Exception e)
    {
      output_.println("Unexpected exception");
      e.printStackTrace(output_);
      success = false;
    }
    finally
    {
      if (serFile != null) {
        try { serFile.delete(); }
        catch(Exception e)
        {
          output_.println("Unable to delete file");
          e.printStackTrace(output_);
          success = false;
        }
      }
      if (success) succeeded();
      else failed();
    }
  }

  /**
   *Verify the ability to serialize and deserialize a SystemValue object.
   *<ul compact>
   *<li>When the object was constructed with the non-null constructor
   *and no other methods have been called.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The SystemValue object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The system and name can be set for the object.
   *</ul>
  **/
  public void Var002()
  {
    SystemValue sv1 = null;
    SystemValue sv2 = null;
    File serFile = null;
    boolean success = true;
    try
    {
      sv1 = new SystemValue(pwrSys_,"QDATE");

      // Add some listeners
      PropertyChangeListener_ pcListener1 = new PropertyChangeListener_();
      SystemValueListener_ svListener1 = new SystemValueListener_();
      sv1.addPropertyChangeListener(pcListener1);
      sv1.addSystemValueListener(svListener1);

      // Verify that the listeners are getting events
      sv1.setName("QCCSID");
      Object oldValue = sv1.getValue();
      Integer intVal37 = new Integer (37);
      sv1.setValue(intVal37);
      sv1.setValue(oldValue);

//      sv1.setName("QDATE");

      if (pcListener1.getEvents().size() == 0) {
        output_.println("Listeners are not receiving PropertyChangeEvents.");
        success = false;
      }
      if (svListener1.getEvents().size() == 0) {
        output_.println("Listeners are not receiving SystemValueEvents.");
        success = false;
      }
      pcListener1.clearEvents ();
      svListener1.clearEvents ();

      // Serialize
//      serFile = new File (CURRENT_DIR, "sysval002.ser");
      serFile = new File ("sysval002.ser");
      FileOutputStream ros = new FileOutputStream(serFile);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(sv1);
      rout.flush();
      rout.close();
      ros.close();
      // Deserialize
      ris = new FileInputStream(serFile);
      rin = new ObjectInputStream(ris);
      sv2 = (SystemValue)rin.readObject();
      rin.close();
      ris.close();

      // Add some listeners to the new object.
      PropertyChangeListener_ pcListener2 = new PropertyChangeListener_();
      SystemValueListener_ svListener2 = new SystemValueListener_();
      sv2.addPropertyChangeListener(pcListener2);
      sv2.addSystemValueListener(svListener2);

      // Verify state
/*      if (sv2.TYPE_ARRAY != SystemValue.TYPE_ARRAY ||
          sv2.TYPE_CHARACTER != SystemValue.TYPE_CHARACTER ||
          sv2.TYPE_DATE != SystemValue.TYPE_DATE ||
          sv2.TYPE_DECIMAL != SystemValue.TYPE_DECIMAL ||
          sv2.TYPE_LOGICAL != SystemValue.TYPE_LOGICAL)
      {
        output_.println("Constants not correct for deserialized object.");
        success = false;
      }
*/
/*      if (!sv2.getDescription().equals ("System date"))
      {
        output_.println("getDescription(): Expected: System date");
        output_.println("                  Got:      " + sv2.getDescription());
        success = false;
      }

      if (!sv2.getName().equals ("QDATE"))
      {
        output_.println("getDescription(): Expected: QDATE");
        output_.println("                  Got:      " + sv2.getName());
        success = false;
      }

      if (sv2.getSystem() != pwrSys_)
      {
        output_.println("getSystem(): Expected: " +
                        pwrSys_.getSystemName());
        output_.println("             Got:      " +
                        sv2.getSystem().getSystemName());
        success = false;
      }
*/
      if (!sv2.getDescription().equals ("Coded character set identifier"))
      {
        output_.println("getDescription(): Expected: Coded character set identifier");
        output_.println("                  Got:      " + sv2.getDescription());
        success = false;
      }

      if (!sv2.getName().equals ("QCCSID"))
      {
        output_.println("getDescription(): Expected: QCCSID");
        output_.println("                  Got:      " + sv2.getName());
        success = false;
      }

      if (!sv2.getSystem().getSystemName().equals(pwrSys_.getSystemName()))
      {
        output_.println("getSystem(): Expected: " +
                        pwrSys_.getSystemName());
        output_.println("             Got:      " +
                        sv2.getSystem().getSystemName());
        success = false;
      }
      if (!sv2.getSystem().getUserId().equals(pwrSys_.getUserId()))
      {
        output_.println("getUserId(): Expected: " +
                        pwrSys_.getUserId());
        output_.println("             Got:      " +
                        sv2.getSystem().getUserId());
        success = false;
      }

/*
      if (sv2.getValue() != null)
      {
        output_.println("getValue() != null");
        success = false;
      }
*/
/*      if (sv2.getValueType() != SystemValue.TYPE_DATE)
      {
        output_.println("getValueType() != SystemValue.TYPE_DATE");
        success = false;
      }
*/

      // Verify usability

      sv2.setSystem(pwrSys_);  // sets the password
      sv2.setName("QCCSID");
      /*Object*/ oldValue = sv2.getValue();
      /*Integer*/ intVal37 = new Integer (37);
      sv2.setValue(intVal37);
      sv2.setValue(oldValue);

      sv2.addPropertyChangeListener(new PropertyChangeListener_());
      sv2.addSystemValueListener(new SystemValueListener_());

      // Verify that the old listeners get no events,
      // and the new listeners get events.
      if (pcListener1.getEvents().size() != 0) {
        output_.println("PropertyChangeListener maintained " +
                        "from pre-serialize.");
        success = false;
      }
      if (svListener1.getEvents().size() != 0) {
        output_.println("SystemValueListener maintained " +
                        "from pre-serialize.");
        success = false;
      }
      if (pcListener2.getEvents().size() == 0) {
        output_.println("PropertyChangeListener added after " +
                        "deserialization not getting events.");
        success = false;
      }
      if (svListener2.getEvents().size() == 0) {
        output_.println("SystemValueListener added after " +
                        "deserialization not getting events.");
        success = false;
      }
    }
    catch (Exception e)
    {
      output_.println("Unexpected exception");
      e.printStackTrace(output_);
      success = false;
    }
    finally
    {
      if (serFile != null) {
        try { serFile.delete(); }
        catch(Exception e)
        {
          output_.println("Unable to delete file");
          e.printStackTrace(output_);
          success = false;
        }
      }
      if (success) succeeded();
      else failed();
    }
  }

  /**
   *Verify the ability to serialize and deserialize a SystemValueList object.
   *<ul compact>
   *<li>When the object was constructed with the null constructor and
   *no other methods have been called.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The SystemValueList object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The system and name can be set for the object.
   *</ul>
  **/
  public void Var003()
  {
    SystemValueList sv1 = null;
    SystemValueList sv2 = null;
    File serFile = null;
    boolean success = true;
    try
    {
      sv1 = new SystemValueList();

      // Add some listeners
      PropertyChangeListener_ pcListener1 = new PropertyChangeListener_();
      VetoableChangeListener_ vcListener1 = new VetoableChangeListener_();
      // The 'try's are temporary - jlee
      try { sv1.addPropertyChangeListener(pcListener1); }
      catch (NullPointerException e) { e.printStackTrace (); success = false; }
      try { sv1.addVetoableChangeListener(vcListener1); }
      catch (NullPointerException e) { e.printStackTrace (); success = false; }

      // Serialize
//      serFile = new File (CURRENT_DIR, "sysval003.ser");
      serFile = new File ("sysval003.ser");
      FileOutputStream ros = new FileOutputStream(serFile);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(sv1);
      rout.flush();
      rout.close();
      ros.close();
      pcListener1.clearEvents ();
      vcListener1.clearEvents ();

      // Deserialize
      ris = new FileInputStream(serFile);
      rin = new ObjectInputStream(ris);
      sv2 = (SystemValueList)rin.readObject();
      rin.close();
      ris.close();

      // Add some listeners to the new object.
      PropertyChangeListener_ pcListener2 = new PropertyChangeListener_();
      VetoableChangeListener_ vcListener2 = new VetoableChangeListener_();
      // The 'try's are temporary - jlee
      try { sv2.addPropertyChangeListener(pcListener2); }
      catch (NullPointerException e) { e.printStackTrace (); success = false; }
      try { sv2.addVetoableChangeListener(vcListener2); }
      catch (NullPointerException e) { e.printStackTrace (); success = false; }

      // Verify state
/*      if (sv2.GROUP_ALL != SystemValueList.GROUP_ALL ||
          sv2.GROUP_ALLOCATION != SystemValueList.GROUP_ALLOCATION ||
          sv2.GROUP_DATEANDTIME != SystemValueList.GROUP_DATEANDTIME ||
          sv2.GROUP_EDITING != SystemValueList.GROUP_EDITING ||
          sv2.GROUP_LIBRARYLIST != SystemValueList.GROUP_LIBRARYLIST ||
          sv2.GROUP_MESSAGEANDLOGGING != SystemValueList.GROUP_MESSAGEANDLOGGING ||
          sv2.GROUP_NETATTRIBUTE != SystemValueList.GROUP_NETATTRIBUTE ||
          sv2.GROUP_SECURITY != SystemValueList.GROUP_SECURITY ||
          sv2.GROUP_STORAGE != SystemValueList.GROUP_STORAGE ||
          sv2.GROUP_SYSTEMCONTROL != SystemValueList.GROUP_SYSTEMCONTROL )
      {
        output_.println("Constants not correct for deserialized object.");
        success = false;
      }

      // Verify usability
      sv2.setGroupInfo(SystemValueList.GROUP_ALL);
      if (sv2.getGroupInfo() != SystemValueList.GROUP_ALL) {
        output_.println("getGroupInfo(): Expected: " +
                        SystemValueList.GROUP_ALL);
        output_.println("                Got:      " +
                        sv2.getGroupInfo());
        success = false;
      }
*/
      sv2.setSystem(pwrSys_);
      if (!sv2.getSystem().getSystemName().equals
          (pwrSys_.getSystemName())) {
        output_.println("getSystem(): Expected: " +
                        pwrSys_.getSystemName());
        output_.println("             Got:      " +
                        sv2.getSystem().getSystemName());
        success = false;
      }

      // Verify that the old listeners get no events,
      // and the new listeners get events.
      if (pcListener1.getEvents().size() != 0) {
        output_.println("PropertyChangeListener maintained " +
                        "from pre-serialize.");
        success = false;
      }
      if (vcListener1.getEvents().size() != 0) {
        output_.println("VetoableChangeListener maintained " +
                        "from pre-serialize.");
        success = false;
      }
      if (pcListener2.getEvents().size() == 0) {
        output_.println("PropertyChangeListener added after " +
                        "deserialization not getting events.");
        success = false;
      }
      if (vcListener2.getEvents().size() == 0) {
        output_.println("VetoableChangeListener added after " +
                        "deserialization not getting events.");
        success = false;
      }
    }
    catch (Exception e)
    {
      output_.println("Unexpected exception");
      e.printStackTrace(output_);
      success = false;
    }
    finally
    {
      if (serFile != null) {
        try { serFile.delete(); }
        catch(Exception e)
        {
          output_.println("Unable to delete file");
          e.printStackTrace(output_);
          success = false;
        }
      }
      if (success) succeeded();
      else failed();
    }
  }

  /**
   *Verify the ability to serialize and deserialize a SystemValueList object.
   *<ul compact>
   *<li>When the object was constructed with the 1-arg constructor and
   *no other methods have been called.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The SystemValueList object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The system and name can be set for the object.
   *</ul>
  **/
  public void Var004()
  {
    SystemValueList sv1 = null;
    SystemValueList sv2 = null;
    File serFile = null;
    boolean success = true;
    try
    {
      sv1 = new SystemValueList(systemObject_);

      // Add some listeners
      PropertyChangeListener_ pcListener1 = new PropertyChangeListener_();
      VetoableChangeListener_ vcListener1 = new VetoableChangeListener_();
      // The 'try's are temporary - jlee
      try { sv1.addPropertyChangeListener(pcListener1); }
      catch (NullPointerException e) { e.printStackTrace (); success = false; }
      try { sv1.addVetoableChangeListener(vcListener1); }
      catch (NullPointerException e) { e.printStackTrace (); success = false; }

      // Serialize
//      serFile = new File (CURRENT_DIR, "sysval004.ser");
      serFile = new File ("sysval004.ser");
      FileOutputStream ros = new FileOutputStream(serFile);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(sv1);
      rout.flush();
      rout.close();
      ros.close();
      pcListener1.clearEvents ();
      vcListener1.clearEvents ();

      // Deserialize
      ris = new FileInputStream(serFile);
      rin = new ObjectInputStream(ris);
      sv2 = (SystemValueList)rin.readObject();
      rin.close();
      ris.close();

      // Add some listeners to the new object.
      PropertyChangeListener_ pcListener2 = new PropertyChangeListener_();
      VetoableChangeListener_ vcListener2 = new VetoableChangeListener_();
      // The 'try's are temporary - jlee
      try { sv2.addPropertyChangeListener(pcListener2); }
      catch (NullPointerException e) { e.printStackTrace (); success = false; }
      try { sv2.addVetoableChangeListener(vcListener2); }
      catch (NullPointerException e) { e.printStackTrace (); success = false; }

      // Verify state
/*      if (sv2.GROUP_ALL != SystemValueList.GROUP_ALL ||
          sv2.GROUP_ALLOCATION != SystemValueList.GROUP_ALLOCATION ||
          sv2.GROUP_DATEANDTIME != SystemValueList.GROUP_DATEANDTIME ||
          sv2.GROUP_EDITING != SystemValueList.GROUP_EDITING ||
          sv2.GROUP_LIBRARYLIST != SystemValueList.GROUP_LIBRARYLIST ||
          sv2.GROUP_MESSAGEANDLOGGING != SystemValueList.GROUP_MESSAGEANDLOGGING ||
          sv2.GROUP_NETATTRIBUTE != SystemValueList.GROUP_NETATTRIBUTE ||
          sv2.GROUP_SECURITY != SystemValueList.GROUP_SECURITY ||
          sv2.GROUP_STORAGE != SystemValueList.GROUP_STORAGE ||
          sv2.GROUP_SYSTEMCONTROL != SystemValueList.GROUP_SYSTEMCONTROL )
      {
        output_.println("Constants not correct for deserialized object.");
        success = false;
      }
*/
      if (!sv2.getSystem().getSystemName().equals
          (systemObject_.getSystemName()))
      {
        output_.println("System not preserved in deserialized object.");
        output_.println("getSystem(): Expected: " +
                        systemObject_.getSystemName());
        output_.println("             Got:      " +
                        sv2.getSystem().getSystemName());
        success = false;
      }

      // Verify usability
/*      sv2.setGroupInfo(SystemValueList.GROUP_ALL);
      if (sv2.getGroupInfo() != SystemValueList.GROUP_ALL) {
        output_.println("getGroupInfo(): Expected: " +
                        SystemValueList.GROUP_ALL);
        output_.println("                Got:      " +
                        sv2.getGroupInfo());
        success = false;
      }
*/
      AS400 as400 = new AS400();
      systemObject_.disconnectService(AS400.COMMAND);
      sv2.setSystem (as400);

      // Verify that the old listeners get no events,
      // and the new listeners get events.
      if (pcListener1.getEvents().size() != 0) {
        output_.println("PropertyChangeListener maintained " +
                        "from pre-serialize.");
        success = false;
      }
      if (vcListener1.getEvents().size() != 0) {
        output_.println("VetoableChangeListener maintained " +
                        "from pre-serialize.");
        success = false;
      }
      if (pcListener2.getEvents().size() == 0) {
        output_.println("PropertyChangeListener added after " +
                        "deserialization not getting events.");
        success = false;
      }
      if (vcListener2.getEvents().size() == 0) {
        output_.println("VetoableChangeListener added after " +
                        "deserialization not getting events.");
        success = false;
      }
    }
    catch (Exception e)
    {
      output_.println("Unexpected exception");
      e.printStackTrace(output_);
      success = false;
    }
    finally
    {
      if (serFile != null) {
        try { serFile.delete(); }
        catch(Exception e)
        {
          output_.println("Unable to delete file");
          e.printStackTrace(output_);
          success = false;
        }
      }
      if (success) succeeded();
      else failed();
    }
  }

  /**
   *Verify the ability to serialize and deserialize a SystemValueList object.
   *<ul compact>
   *<li>When the object was constructed with the 2-arg constructor and
   *no other methods have been called.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The SystemValueList object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The system and name can be set for the object.
   *</ul>
  **/
  public void Var005()
  {
    SystemValueList sv1 = null;
    SystemValueList sv2 = null;
    File serFile = null;
    boolean success = true;
    try
    {
      sv1 = new SystemValueList(systemObject_);
//                                SystemValueList.GROUP_DATEANDTIME);

      // Add some listeners
      PropertyChangeListener_ pcListener1 = new PropertyChangeListener_();
      VetoableChangeListener_ vcListener1 = new VetoableChangeListener_();
      // The 'try's are temporary - jlee
      try { sv1.addPropertyChangeListener(pcListener1); }
      catch (NullPointerException e) { e.printStackTrace (); success = false; }
      try { sv1.addVetoableChangeListener(vcListener1); }
      catch (NullPointerException e) { e.printStackTrace (); success = false; }

      // Serialize
//      serFile = new File (CURRENT_DIR, "sysval005.ser");
      serFile = new File ("sysval005.ser");
      FileOutputStream ros = new FileOutputStream(serFile);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(sv1);
      rout.flush();
      rout.close();
      ros.close();
      pcListener1.clearEvents ();
      vcListener1.clearEvents ();

      // Deserialize
      ris = new FileInputStream(serFile);
      rin = new ObjectInputStream(ris);
      sv2 = (SystemValueList)rin.readObject();
      rin.close();
      ris.close();

      // Add some listeners to the new object.
      PropertyChangeListener_ pcListener2 = new PropertyChangeListener_();
      VetoableChangeListener_ vcListener2 = new VetoableChangeListener_();
      sv2.addPropertyChangeListener(pcListener2);
      sv2.addVetoableChangeListener(vcListener2);

      // Cause a property change event
      sv2.setSystem(pwrSys_);

      // Verify state
/*      if (sv2.GROUP_ALL != SystemValueList.GROUP_ALL ||
          sv2.GROUP_ALLOCATION != SystemValueList.GROUP_ALLOCATION ||
          sv2.GROUP_DATEANDTIME != SystemValueList.GROUP_DATEANDTIME ||
          sv2.GROUP_EDITING != SystemValueList.GROUP_EDITING ||
          sv2.GROUP_LIBRARYLIST != SystemValueList.GROUP_LIBRARYLIST ||
          sv2.GROUP_MESSAGEANDLOGGING != SystemValueList.GROUP_MESSAGEANDLOGGING ||
          sv2.GROUP_NETATTRIBUTE != SystemValueList.GROUP_NETATTRIBUTE ||
          sv2.GROUP_SECURITY != SystemValueList.GROUP_SECURITY ||
          sv2.GROUP_STORAGE != SystemValueList.GROUP_STORAGE ||
          sv2.GROUP_SYSTEMCONTROL != SystemValueList.GROUP_SYSTEMCONTROL )
      {
        output_.println("Constants not correct for deserialized object.");
        success = false;
      }
*/
      if (!sv2.getSystem().getSystemName().equals
          (pwrSys_.getSystemName()))
      {
        output_.println("System not preserved in deserialized object.");
        output_.println("getSystem(): Expected: " +
                        pwrSys_.getSystemName());
        output_.println("             Got:      " +
                        sv2.getSystem().getSystemName());
        success = false;
      }
/*      if (sv2.getGroupInfo() != SystemValueList.GROUP_DATEANDTIME)
      {
        output_.println("GroupInfo not preserved in deserialized object.");
        output_.println("getSystem(): Expected: " +
                        SystemValueList.GROUP_DATEANDTIME);
        output_.println("             Got:      " +
                        sv2.getGroupInfo());
        success = false;
      }

      // Verify usability
      sv2.setGroupInfo(SystemValueList.GROUP_ALL);
      if (sv2.getGroupInfo() != SystemValueList.GROUP_ALL) {
        output_.println("getGroupInfo(): Expected: " +
                        SystemValueList.GROUP_ALL);
        output_.println("                Got:      " +
                        sv2.getGroupInfo());
        success = false;
      }
*/
/*
      sv2.setSystem(systemObject_);
      if (!sv2.getSystem().getSystemName().equals
          (systemObject_.getSystemName())) {
        output_.println("getSystem(): Expected: " +
                        systemObject_.getSystemName());
        output_.println("             Got:      " +
                        sv2.getSystem().getSystemName());
        success = false;
      }
*/

      // Verify that the old listeners get no events,
      // and the new listeners get events.
      if (pcListener1.getEvents().size() != 0) {
        output_.println("PropertyChangeListener maintained " +
                        "from pre-serialize.");
        success = false;
      }
      if (vcListener1.getEvents().size() != 0) {
        output_.println("VetoableChangeListener maintained " +
                        "from pre-serialize.");
        success = false;
      }
      if (pcListener2.getEvents().size() == 0) {
        output_.println("PropertyChangeListener added after " +
                        "deserialization not getting events.");
        success = false;
      }
      if (vcListener2.getEvents().size() == 0) {
        output_.println("VetoableChangeListener added after " +
                        "deserialization not getting events.");
        success = false;
      }
    }
    catch (Exception e)
    {
      output_.println("Unexpected exception");
      e.printStackTrace(output_);
      success = false;
    }
    finally
    {
      if (serFile != null) {
        try { serFile.delete(); }
        catch(Exception e)
        {
          output_.println("Unable to delete file");
          e.printStackTrace(output_);
          success = false;
        }
      }
      if (success) succeeded();
      else failed();
    }
  }
}
