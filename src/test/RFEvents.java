///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RFEvents.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Vector;
import java.beans.PropertyVetoException;
import com.ibm.as400.access.*;

/**
 *Testcase RFEvents.  This test class verifies valid and invalid
 *usage of:
 *<ul compact>
 *<li>RecordFormat.addRecordDescriptionListener()
 *<li>RecordFormat.addPropertyChangeListener()
 *<li>RecordFormat.addVetoableChangeListener()
 *<li>RecordFormat.removeRecordDescriptionListener()
 *<li>RecordFormat.removePropertyChangeListener()
 *<li>RecordFormat.removeVetoableChangeListener()
 *<li>Record.addRecordDescriptionListener()
 *<li>Record.addPropertyChangeListener()
 *<li>Record.addVetoableChangeListener()
 *<li>Record.removeRecordDescriptionListener()
 *<li>Record.removePropertyChangeListener()
 *<li>Record.removeVetoableChangeListener()
 *</ul>
 *This test class also verifies that the following events are fired
 *from the specified methods:
 *<ul compact>
 *<li>RecordFormat.setName() - PropertyChangeEvent, VetoablePropertyChangeEvent
 *<li>RecordFormat.addFieldDescription() - RecordDescriptionEvent.fieldDescriptionAdded()
 *<li>RecordFormat.addKeyFieldDescription() - RecordDescriptionEvent.keyFieldDescriptionAdded()
 *<li>Record.setRecordFormat() - PropertyChangeEvent, VetoablePropertyChangeEvent
 *<li>Record.setRecordName() - PropertyChangeEvent, VetoablePropertyChangeEvent
 *<li>Record.setRecordNumber() - PropertyChangeEvent, VetoablePropertyChangeEvent
 *<li>Record.getField() - RecordDescriptionEvent.fieldModified()
 *<li>Record.setContents() - RecordDescriptionEvent.fieldModified()
 *<li>Record.setField() - RecordDescriptionEvent.fieldModified()
 *<li>Record.setRecordFormat() - RecordDescriptionEvent.fieldModified()
 *</ul>
**/
public class RFEvents extends Testcase
{
  /**
   *Constructor.  This is called from RFTest::createTestcases().
  **/
  public RFEvents(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    super(systemObject, "RFEvents", 34,
          variationsToRun, runMode, fileOutputStream);
  }

/**
  Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

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
    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != ATTENDED)
    {
      setVariation(6);
      Var006();
    }
    if ((allVariations || variationsToRun_.contains("7")) &&
        runMode_ != ATTENDED)
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
        runMode_ != ATTENDED)
    {
      setVariation(9);
      Var009();
    }
    if ((allVariations || variationsToRun_.contains("10")) &&
        runMode_ != ATTENDED)
    {
      setVariation(10);
      Var010();
    }
    if ((allVariations || variationsToRun_.contains("11")) &&
        runMode_ != ATTENDED)
    {
      setVariation(11);
      Var011();
    }
    if ((allVariations || variationsToRun_.contains("12")) &&
        runMode_ != ATTENDED)
    {
      setVariation(12);
      Var012();
    }
    if ((allVariations || variationsToRun_.contains("13")) &&
        runMode_ != ATTENDED)
    {
      setVariation(13);
      Var013();
    }
    if ((allVariations || variationsToRun_.contains("14")) &&
        runMode_ != ATTENDED)
    {
      setVariation(14);
      Var014();
    }
    if ((allVariations || variationsToRun_.contains("15")) &&
        runMode_ != ATTENDED)
    {
      setVariation(15);
      Var015();
    }
    if ((allVariations || variationsToRun_.contains("16")) &&
        runMode_ != ATTENDED)
    {
      setVariation(16);
      Var016();
    }
    if ((allVariations || variationsToRun_.contains("17")) &&
        runMode_ != ATTENDED)
    {
      setVariation(17);
      Var017();
    }
    if ((allVariations || variationsToRun_.contains("18")) &&
        runMode_ != ATTENDED)
    {
      setVariation(18);
      Var018();
    }
    if ((allVariations || variationsToRun_.contains("19")) &&
        runMode_ != ATTENDED)
    {
      setVariation(19);
      Var019();
    }
    if ((allVariations || variationsToRun_.contains("20")) &&
        runMode_ != ATTENDED)
    {
      setVariation(20);
      Var020();
    }
    if ((allVariations || variationsToRun_.contains("21")) &&
        runMode_ != ATTENDED)
    {
      setVariation(21);
      Var021();
    }
    if ((allVariations || variationsToRun_.contains("22")) &&
        runMode_ != ATTENDED)
    {
      setVariation(22);
      Var022();
    }
    if ((allVariations || variationsToRun_.contains("23")) &&
        runMode_ != ATTENDED)
    {
      setVariation(23);
      Var023();
    }
    if ((allVariations || variationsToRun_.contains("24")) &&
        runMode_ != ATTENDED)
    {
      setVariation(24);
      Var024();
    }
    if ((allVariations || variationsToRun_.contains("25")) &&
        runMode_ != ATTENDED)
    {
      setVariation(25);
      Var025();
    }
    if ((allVariations || variationsToRun_.contains("26")) &&
        runMode_ != ATTENDED)
    {
      setVariation(26);
      Var026();
    }
    if ((allVariations || variationsToRun_.contains("27")) &&
        runMode_ != ATTENDED)
    {
      setVariation(27);
      Var027();
    }
    if ((allVariations || variationsToRun_.contains("28")) &&
        runMode_ != ATTENDED)
    {
      setVariation(28);
      Var028();
    }
    if ((allVariations || variationsToRun_.contains("29")) &&
        runMode_ != ATTENDED)
    {
      setVariation(29);
      Var029();
    }
    if ((allVariations || variationsToRun_.contains("30")) &&
        runMode_ != ATTENDED)
    {
      setVariation(30);
      Var030();
    }
    if ((allVariations || variationsToRun_.contains("31")) &&
        runMode_ != ATTENDED)
    {
      setVariation(31);
      Var031();
    }
    if ((allVariations || variationsToRun_.contains("32")) &&
        runMode_ != ATTENDED)
    {
      setVariation(32);
      Var032();
    }
    if ((allVariations || variationsToRun_.contains("33")) &&
        runMode_ != ATTENDED)
    {
      setVariation(33);
      Var033();
    }
    if ((allVariations || variationsToRun_.contains("34")) &&
        runMode_ != ATTENDED)
    {
      setVariation(34);
      Var034();
    }
  }


  void rfFireRecordDescriptionEvents(RecordFormat r)
  {
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "name"));
    r.addKeyFieldDescription(0);
  }

  void rFireRecordDescriptionEvents(Record r)
  {
    r.setField(0, new Integer(5));
  }

  void rfFirePropertyChangeEvents(RecordFormat r, String name)
    throws PropertyVetoException
  {
    r.setName(name);
  }

  void rFirePropertyChangeEvents(Record r, String name)
    throws PropertyVetoException
  {
    r.setRecordName(name);
  }

  void rfFireVetoableChangeEvents(RecordFormat r, String name)
    throws PropertyVetoException
  {
    r.setName(name);
  }

  void rFireVetoableChangeEvents(Record r, String name)
    throws PropertyVetoException
  {
    r.setRecordName(name);
  }

  /**
   *Verify valid usage RecordFormat.addRecordDescriptionListener().
   *<ul compact>
   *<li>For one, two and three RecordDescriptionListeners
   *<ul compact>
   *<li>Add the RecordDescriptionListener(s)
   *<li>Invoke a function which fires a RecordDescriptionEvent
   *<li>Verify that the listener(s) got the event
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will be received by the listeners
   *</ul>
  **/
  public void Var001()
  {
    try
    {
      // Create our record description listeners
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl2 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl3 = new RFRecordDescriptionListener();
      // Create our record format object
      RecordFormat r = new RecordFormat();

      // Verify one listener
      r.addRecordDescriptionListener(rl1);
      // Cause a RecordDescriptionEvent to be fired
      rfFireRecordDescriptionEvents(r);
      if (!(rl1.fieldDescriptionAddedFired_ &&
          rl1.keyFieldDescriptionAddedFired_) || (rl1.fieldModifiedFired_))
      {
        failed("Single listener");
        return;
      }
      // Reset listener
      rl1.reset();

      // Verify two listeners
      r.addRecordDescriptionListener(rl2);
      // Cause a RecordDescriptionEvent to be fired
      rfFireRecordDescriptionEvents(r);
      if (!(rl1.fieldDescriptionAddedFired_ &&
          rl1.keyFieldDescriptionAddedFired_ && rl2.fieldDescriptionAddedFired_ &&
          rl2.keyFieldDescriptionAddedFired_) || (rl1.fieldModifiedFired_) || (rl2.fieldModifiedFired_))
      {
        failed("two listeners");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();

      // Verify three listeners
      r.addRecordDescriptionListener(rl3);
      // Cause a RecordDescriptionEvent to be fired
      rfFireRecordDescriptionEvents(r);
    if (!(rl1.fieldDescriptionAddedFired_ &&
	  rl1.keyFieldDescriptionAddedFired_ && rl2.fieldDescriptionAddedFired_ &&
	  rl2.keyFieldDescriptionAddedFired_ && rl3.fieldDescriptionAddedFired_ &&
	  rl3.keyFieldDescriptionAddedFired_) || (rl1.fieldModifiedFired_) || (rl2.fieldModifiedFired_) || (rl3.fieldModifiedFired_))
    {
	failed("three listeners");
	return;
    }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage RecordFormat.addPropertyChangeListener().
   *<ul compact>
   *<li>For one, two and three PropertyChangeListeners
   *<ul compact>
   *<li>Add the PropertyChangeListener(s)
   *<li>Invoke a function which fires a PropertyChangeEvent
   *<li>Verify that the listener(s) got the event
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will be received by the listeners
   *</ul>
  **/
  public void Var002()
  {
    try
    {
      // Create our property change listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl3 = new DDMPropertyChangeListener();
      // Create our record format object
      RecordFormat r = new RecordFormat();

      // Verify one listener
      r.addPropertyChangeListener(rl1);
      // Cause a PropertyChangeEvent to be fired
      rfFirePropertyChangeEvents(r, "name1");
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals("NAME1") ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("name"))
      {
        failed("Single listener");
        return;
      }
      // Reset listener
      rl1.reset();;

      // Verify two listeners
      r.addPropertyChangeListener(rl2);
      // Cause a PropertyChangeEvent to be fired
      rfFirePropertyChangeEvents(r, "name2");
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals("NAME2") ||
          !rl1.e_.getOldValue().equals("NAME1") ||
          !rl1.e_.getPropertyName().equals("name") ||
          !rl2.propertyChangeFired_ ||
          !rl2.e_.getNewValue().equals("NAME2") ||
          !rl2.e_.getOldValue().equals("NAME1") ||
          !rl2.e_.getPropertyName().equals("name"))
      {
        failed("two listeners");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();

      // Verify three listeners
      r.addPropertyChangeListener(rl3);
      // Cause a PropertyChangeEvent to be fired
      rfFirePropertyChangeEvents(r, "name3");
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals("NAME3") ||
          !rl1.e_.getOldValue().equals("NAME2") ||
          !rl1.e_.getPropertyName().equals("name") ||
          !rl2.propertyChangeFired_ ||
          !rl2.e_.getNewValue().equals("NAME3") ||
          !rl2.e_.getOldValue().equals("NAME2") ||
          !rl2.e_.getPropertyName().equals("name") ||
          !rl3.propertyChangeFired_ ||
          !rl3.e_.getNewValue().equals("NAME3") ||
          !rl3.e_.getOldValue().equals("NAME2") ||
          !rl3.e_.getPropertyName().equals("name"))
      {
        failed("three listeners");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage RecordFormat.addVetoableChangeListener().
   *<ul compact>
   *<li>For one, two and three VetoableChangeListeners
   *<ul compact>
   *<li>Add the PropertyChangeListener(s)
   *<li>Invoke a function which fires a VetoableChangeEvent
   *<li>Verify that at least one listener can veto the event
   *<li>Verify that the listener(s) got the event
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will be received by the listeners
   *<li>Vetoed property changes will be changed back to the original value
   *</ul>
  **/
  public void Var003()
  {
    try
    {
      // Create our vetoable change listeners
      DDMVetoableChangeListener rl1 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl2 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      // Create our record format object
      RecordFormat r = new RecordFormat();

      // Verify one listener, no veto
      r.addVetoableChangeListener(rl1);
      // Cause a VetoableChangeEvent to be fired
      rfFireVetoableChangeEvents(r, "name1");
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals("NAME1") ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("name"))
      {
        failed("Single listener");
        return;
      }
      // Reset listener
      rl1.reset();

      // Verify two listeners, no veto
      r.addVetoableChangeListener(rl2);
      // Cause a VetoableChangeEvent to be fired
      rfFireVetoableChangeEvents(r, "name2");
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals("NAME2") ||
          !rl1.e_.getOldValue().equals("NAME1") ||
          !rl1.e_.getPropertyName().equals("name") ||
          !rl2.vetoableChangeFired_ ||
          !rl2.e_.getNewValue().equals("NAME2") ||
          !rl2.e_.getOldValue().equals("NAME1") ||
          !rl2.e_.getPropertyName().equals("name"))
      {
        failed("two listeners");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();

      // Verify three listeners, no veto
      r.addVetoableChangeListener(rl3);
      // Cause a VetoableChangeEvent to be fired
      rfFireVetoableChangeEvents(r, "name3");
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals("NAME3") ||
          !rl1.e_.getOldValue().equals("NAME2") ||
          !rl1.e_.getPropertyName().equals("name") ||
          !rl2.vetoableChangeFired_ ||
          !rl2.e_.getNewValue().equals("NAME3") ||
          !rl2.e_.getOldValue().equals("NAME2") ||
          !rl2.e_.getPropertyName().equals("name") ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getNewValue().equals("NAME3") ||
          !rl3.e_.getOldValue().equals("NAME2") ||
          !rl3.e_.getPropertyName().equals("name"))
      {
        failed("three listeners");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      // Verify three listeners, with a veto from rl2
      rl2.vetoTheChange = true;
      // Cause a VetoableChangeEvent to be fired
      try
      {
        rfFireVetoableChangeEvents(r, "name4");
      }
      catch(PropertyVetoException p)
      {
	  Object oldValue =  "NAME4";
	  Object newValue = "NAME3";

if (isAtLeastJDK17) {
    if (!rl1.vetoableChangeFired_ ||
	!rl1.e_.getNewValue().equals(newValue) ||
	!rl1.e_.getOldValue().equals(oldValue) ||
	!rl1.e_.getPropertyName().equals("name") ||
	!rl2.vetoed_ ||
	rl2.e_ != null ||
	rl3.vetoableChangeFired_ )
    {
	failed("JDK 17 three listeners with veto");
	return;
    }

} else { 
    if (!rl1.vetoableChangeFired_ ||
	!rl1.e_.getNewValue().equals(newValue) ||
	!rl1.e_.getOldValue().equals(oldValue) ||
	!rl1.e_.getPropertyName().equals("name") ||
	!rl2.vetoed_ ||
	rl2.e_ != null ||
	!rl3.vetoableChangeFired_ ||
	!rl3.e_.getNewValue().equals(newValue) ||
	!rl3.e_.getOldValue().equals(oldValue) ||
	!rl3.e_.getPropertyName().equals("name"))
    {
	failed("three listeners with veto");
	return;
    }
}
          succeeded();
          return;
      }
      failed("No propertyVetoException");
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }

  /**
   *Verify invalid usage RecordFormat.addRecordDescriptionListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var004()
  {
    try
    {
      RecordFormat r = new RecordFormat();
      r.addRecordDescriptionListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage RecordFormat.addPropertyChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var005()
  {
    try
    {
      RecordFormat r = new RecordFormat();
      r.addPropertyChangeListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage RecordFormat.addVetoableChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var006()
  {
    try
    {
      RecordFormat r = new RecordFormat();
      r.addVetoableChangeListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage RecordFormat.removeRecordDescriptionListener().
   *<ul compact>
   *<li>Add a several RecordDescriptionListeners
   *<li>Remove one RecordDescriptionListener
   *<li>Invoke a function which fires a RecordDescriptionEvent
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
  public void Var007()
  {
    try
    {
      // Create our record description listeners
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl2 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl3 = new RFRecordDescriptionListener();
      // Create our record format object
      RecordFormat r = new RecordFormat();

      // Verify three listeners
      r.addRecordDescriptionListener(rl1);
      r.addRecordDescriptionListener(rl2);
      r.addRecordDescriptionListener(rl3);
      // Cause a RecordDescriptionEvent to be fired
      rfFireRecordDescriptionEvents(r);
      if (!(rl1.fieldDescriptionAddedFired_ &&
          rl1.keyFieldDescriptionAddedFired_ && rl2.fieldDescriptionAddedFired_ &&
          rl2.keyFieldDescriptionAddedFired_ && rl3.fieldDescriptionAddedFired_ &&
          rl3.keyFieldDescriptionAddedFired_) || (rl1.fieldModifiedFired_) || (rl2.fieldModifiedFired_) || (rl3.fieldModifiedFired_))
      {
        failed("three listeners");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();

      // Remove rl2 from the listener list
      r.removeRecordDescriptionListener(rl2);
      // Cause a RecordDescriptionEvent to be fired
      rfFireRecordDescriptionEvents(r);
      if (!(rl1.fieldDescriptionAddedFired_ &&
          rl1.keyFieldDescriptionAddedFired_ && !rl2.fieldDescriptionAddedFired_ &&
          !rl2.keyFieldDescriptionAddedFired_ && rl3.fieldDescriptionAddedFired_ &&
          rl3.keyFieldDescriptionAddedFired_) || (rl1.fieldModifiedFired_) || (rl2.fieldModifiedFired_) || (rl3.fieldModifiedFired_))
      {
        failed("after removing a listener");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage RecordFormat.removePropertyChangeListener().
   *<ul compact>
   *<li>Add a several PropertyChangeListeners
   *<li>Remove one PropertyChangeListener
   *<li>Invoke a function which fires a PropertyChangeEvent
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
  public void Var008()
  {
    try
    {
      // Create our property change listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl3 = new DDMPropertyChangeListener();
      // Create our record format object
      RecordFormat r = new RecordFormat();

      // Verify three listeners
      r.addPropertyChangeListener(rl1);
      r.addPropertyChangeListener(rl2);
      r.addPropertyChangeListener(rl3);
      // Cause a PropertyChangeEvent to be fired
      rfFirePropertyChangeEvents(r, "name3");
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals("NAME3") ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("name") ||
          !rl2.propertyChangeFired_ ||
          !rl2.e_.getNewValue().equals("NAME3") ||
          !rl2.e_.getOldValue().equals("") ||
          !rl2.e_.getPropertyName().equals("name") ||
          !rl3.propertyChangeFired_ ||
          !rl3.e_.getNewValue().equals("NAME3") ||
          !rl3.e_.getOldValue().equals("") ||
          !rl3.e_.getPropertyName().equals("name"))
      {
        failed("three listeners");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      // Remove rl2 for list of listeners
      r.removePropertyChangeListener(rl2);
      // Cause a PropertyChangeEvent to be fired
      rfFirePropertyChangeEvents(r, "name4");
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals("NAME4") ||
          !rl1.e_.getOldValue().equals("NAME3") ||
          !rl1.e_.getPropertyName().equals("name") ||
          rl2.propertyChangeFired_ ||
          rl2.e_ != null ||
          !rl3.propertyChangeFired_ ||
          !rl3.e_.getNewValue().equals("NAME4") ||
          !rl3.e_.getOldValue().equals("NAME3") ||
          !rl3.e_.getPropertyName().equals("name"))
      {
        failed("three listeners with veto");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage RecordFormat.removeVetoableChangeListener().
   *<ul compact>
   *<li>Add a several VetoableChangeListeners
   *<li>Remove one VetoableChangeListener
   *<li>Invoke a function which fires a VetoableChangeEvent
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
  public void Var009()
  {
    try
    {
      // Create our vetoable change listeners
      DDMVetoableChangeListener rl1 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl2 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      // Create our record format object
      RecordFormat r = new RecordFormat();

      // Verify three listeners, no veto
      r.addVetoableChangeListener(rl1);
      r.addVetoableChangeListener(rl2);
      r.addVetoableChangeListener(rl3);
      // Cause a VetoableChangeEvent to be fired
      rfFireVetoableChangeEvents(r, "name3");
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals("NAME3") ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("name") ||
          !rl2.vetoableChangeFired_ ||
          !rl2.e_.getNewValue().equals("NAME3") ||
          !rl2.e_.getOldValue().equals("") ||
          !rl2.e_.getPropertyName().equals("name") ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getNewValue().equals("NAME3") ||
          !rl3.e_.getOldValue().equals("") ||
          !rl3.e_.getPropertyName().equals("name"))
      {
        failed("three listeners");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      // Remove listener rl2
      r.removeVetoableChangeListener(rl2);
      // Cause a VetoableChangeEvent to be fired
      rfFireVetoableChangeEvents(r, "name4");
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals("NAME4") ||
          !rl1.e_.getOldValue().equals("NAME3") ||
          !rl1.e_.getPropertyName().equals("name") ||
          rl2.vetoableChangeFired_ ||
          rl2.e_ != null ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getNewValue().equals("NAME4") ||
          !rl3.e_.getOldValue().equals("NAME3") ||
          !rl3.e_.getPropertyName().equals("name"))
      {
        failed("three listeners with veto");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage RecordFormat.removeRecordDescriptionListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var010()
  {
    try
    {
      RecordFormat r = new RecordFormat();
      r.removeRecordDescriptionListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage RecordFormat.removePropertyChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var011()
  {
    try
    {
      RecordFormat r = new RecordFormat();
      r.removePropertyChangeListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage RecordFormat.removeVetoableChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var012()
  {
    try
    {
      RecordFormat r = new RecordFormat();
      r.removeVetoableChangeListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage Record.addRecordDescriptionListener().
   *<ul compact>
   *<li>For one, two and three RecordDescriptionListeners
   *<ul compact>
   *<li>Add the RecordDescriptionListener(s)
   *<li>Invoke a function which fires a RecordDescriptionEvent
   *<li>Verify that the listener(s) got the event
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will be received by the listeners
   *</ul>
  **/
  public void Var013()
  {
    try
    {
      // Create our record description listeners
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl2 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl3 = new RFRecordDescriptionListener();
      // Create our record  object
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "field1"));
      Record r = new Record(rf);

      // Verify one listener
      r.addRecordDescriptionListener(rl1);
      // Cause a RecordDescriptionEvent to be fired
      rFireRecordDescriptionEvents(r);
      if (!rl1.fieldModifiedFired_ ||
          rl1.keyFieldDescriptionAddedFired_ || rl1.fieldDescriptionAddedFired_)
      {
        failed("Single listener");
        return;
      }
      // Reset listener
      rl1.reset();

      // Verify two listeners
      r.addRecordDescriptionListener(rl2);
      // Cause a RecordDescriptionEvent to be fired
      rFireRecordDescriptionEvents(r);
      if (!rl1.fieldModifiedFired_ ||
          !rl2.fieldModifiedFired_ ||
          rl1.keyFieldDescriptionAddedFired_ ||
          rl1.fieldDescriptionAddedFired_ ||
          rl2.fieldDescriptionAddedFired_ ||
          rl2.keyFieldDescriptionAddedFired_)
      {
        failed("two listeners");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();

      // Verify three listeners
      r.addRecordDescriptionListener(rl3);
      // Cause a RecordDescriptionEvent to be fired
      rFireRecordDescriptionEvents(r);
      if (!rl1.fieldModifiedFired_ ||
          !rl2.fieldModifiedFired_ ||
          !rl3.fieldModifiedFired_ ||
          rl1.keyFieldDescriptionAddedFired_ ||
          rl1.fieldDescriptionAddedFired_ ||
          rl2.fieldDescriptionAddedFired_ ||
          rl2.keyFieldDescriptionAddedFired_ ||
          rl3.keyFieldDescriptionAddedFired_ ||
          rl3.fieldDescriptionAddedFired_)
      {
        failed("three listeners");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage Record.addPropertyChangeListener().
   *<ul compact>
   *<li>For one, two and three PropertyChangeListeners
   *<ul compact>
   *<li>Add the PropertyChangeListener(s)
   *<li>Invoke a function which fires a PropertyChangeEvent
   *<li>Verify that the listener(s) got the event
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will be received by the listeners
   *</ul>
  **/
  public void Var014()
  {
    try
    {
      // Create our property change listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl3 = new DDMPropertyChangeListener();
      // Create our record object
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "field1"));
      Record r = new Record(rf);

      // Verify one listener
      r.addPropertyChangeListener(rl1);
      // Cause a PropertyChangeEvent to be fired
      rFirePropertyChangeEvents(r, "name1");
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals("name1") ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("recordName"))
      {
        failed("Single listener");
        return;
      }
      // Reset listener
      rl1.reset();

      // Verify two listeners
      r.addPropertyChangeListener(rl2);
      // Cause a PropertyChangeEvent to be fired
      rFirePropertyChangeEvents(r, "name2");
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals("name2") ||
          !rl1.e_.getOldValue().equals("name1") ||
          !rl1.e_.getPropertyName().equals("recordName") ||
          !rl2.propertyChangeFired_ ||
          !rl2.e_.getNewValue().equals("name2") ||
          !rl2.e_.getOldValue().equals("name1") ||
          !rl2.e_.getPropertyName().equals("recordName"))
      {
        failed("two listeners");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();

      // Verify three listeners
      r.addPropertyChangeListener(rl3);
      // Cause a PropertyChangeEvent to be fired
      rFirePropertyChangeEvents(r, "name3");
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals("name3") ||
          !rl1.e_.getOldValue().equals("name2") ||
          !rl1.e_.getPropertyName().equals("recordName") ||
          !rl2.propertyChangeFired_ ||
          !rl2.e_.getNewValue().equals("name3") ||
          !rl2.e_.getOldValue().equals("name2") ||
          !rl2.e_.getPropertyName().equals("recordName") ||
          !rl3.propertyChangeFired_ ||
          !rl3.e_.getNewValue().equals("name3") ||
          !rl3.e_.getOldValue().equals("name2") ||
          !rl3.e_.getPropertyName().equals("recordName"))
      {
        failed("three listeners");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage Record.addVetoableChangeListener().
   *<ul compact>
   *<li>For one, two and three VetoableChangeListeners
   *<ul compact>
   *<li>Add the PropertyChangeListener(s)
   *<li>Invoke a function which fires a VetoableChangeEvent
   *<li>Verify that at least one listener can veto the event
   *<li>Verify that the listener(s) got the event
   *<li>Verify that the listener(s) notified of event that was vetoed are
   *notified of the change back.
   *<li>Verify that any vetos of a veto are ignored.
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will be received by the listeners
   *<li>Vetoed property changes will be changed back to the original value
   *<li>Vetoed vetos will be ignored
   *</ul>
  **/
  public void Var015()
  {
    try
    {
      // Create our vetoable change listeners
      DDMVetoableChangeListener rl1 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl2 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      // Create our record object
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "field1"));
      Record r = new Record(rf);

      // Verify one listener, no veto
      r.addVetoableChangeListener(rl1);
      // Cause a VetoableChangeEvent to be fired
      rFireVetoableChangeEvents(r, "name1");
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals("name1") ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("recordName"))
      {
        failed("Single listener");
        return;
      }
      // Reset listener
      rl1.reset();

      // Verify two listeners, no veto
      r.addVetoableChangeListener(rl2);
      // Cause a VetoableChangeEvent to be fired
      rFireVetoableChangeEvents(r, "name2");
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals("name2") ||
          !rl1.e_.getOldValue().equals("name1") ||
          !rl1.e_.getPropertyName().equals("recordName") ||
          !rl2.vetoableChangeFired_ ||
          !rl2.e_.getNewValue().equals("name2") ||
          !rl2.e_.getOldValue().equals("name1") ||
          !rl2.e_.getPropertyName().equals("recordName"))
      {
        failed("two listeners");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();

      // Verify three listeners, no veto
      r.addVetoableChangeListener(rl3);
      // Cause a VetoableChangeEvent to be fired
      rFireVetoableChangeEvents(r, "name3");
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals("name3") ||
          !rl1.e_.getOldValue().equals("name2") ||
          !rl1.e_.getPropertyName().equals("recordName") ||
          !rl2.vetoableChangeFired_ ||
          !rl2.e_.getNewValue().equals("name3") ||
          !rl2.e_.getOldValue().equals("name2") ||
          !rl2.e_.getPropertyName().equals("recordName") ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getNewValue().equals("name3") ||
          !rl3.e_.getOldValue().equals("name2") ||
          !rl3.e_.getPropertyName().equals("recordName"))
      {
        failed("three listeners");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      // Verify three listeners, with a veto from rl2
      rl2.vetoTheChange = true;
      // Cause a VetoableChangeEvent to be fired
      try
      {
        rFireVetoableChangeEvents(r, "name4");
      }
      catch(PropertyVetoException p)
      {
	    Object oldValue = "name4"; 
	    Object newValue = "name3";

	    if (isAtLeastJDK17) {
		if (!rl1.vetoableChangeFired_ ||
		    !rl1.e_.getNewValue().equals(newValue) ||
		    !rl1.e_.getOldValue().equals(oldValue) ||
		    !rl1.e_.getPropertyName().equals("recordName") ||
		    !rl2.vetoed_ ||
		    rl2.e_ != null ||
		    rl3.vetoableChangeFired_)
		{
		    failed("three listeners with veto");
		    return;
		}
	    } else { 
		if (!rl1.vetoableChangeFired_ ||
		    !rl1.e_.getNewValue().equals(newValue) ||
		    !rl1.e_.getOldValue().equals(oldValue) ||
		    !rl1.e_.getPropertyName().equals("recordName") ||
		    !rl2.vetoed_ ||
		    rl2.e_ != null ||
		    !rl3.vetoableChangeFired_ ||
		    !rl3.e_.getNewValue().equals(newValue) ||
		    !rl3.e_.getOldValue().equals(oldValue) ||
		    !rl3.e_.getPropertyName().equals("recordName"))
		{
		    failed("three listeners with veto");
		    return;
		}
	    }

          succeeded();
          return;

      }
      failed("No propertyVetoException");
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }

  /**
   *Verify invalid usage Record.addRecordDescriptionListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var016()
  {
    try
    {
      Record r = new Record();
      r.addRecordDescriptionListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage Record.addPropertyChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var017()
  {
    try
    {
      Record r = new Record();
      r.addPropertyChangeListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage Record.addVetoableChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var018()
  {
    try
    {
      Record r = new Record();
      r.addVetoableChangeListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage Record.removeRecordDescriptionListener().
   *<ul compact>
   *<li>Add a several RecordDescriptionListeners
   *<li>Remove one RecordDescriptionListener
   *<li>Invoke a function which fires a RecordDescriptionEvent
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
  public void Var019()
  {
    try
    {
      // Create our record description listeners
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl2 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl3 = new RFRecordDescriptionListener();
      // Create our record object
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "field1"));
      Record r = new Record(rf);

      // Verify three listeners
      r.addRecordDescriptionListener(rl1);
      r.addRecordDescriptionListener(rl2);
      r.addRecordDescriptionListener(rl3);
      // Cause a RecordDescriptionEvent to be fired
      rFireRecordDescriptionEvents(r);
      if (!rl1.fieldModifiedFired_ ||
          !rl2.fieldModifiedFired_ ||
          !rl3.fieldModifiedFired_ ||
          rl1.keyFieldDescriptionAddedFired_ ||
          rl1.fieldDescriptionAddedFired_ ||
          rl2.fieldDescriptionAddedFired_ ||
          rl2.keyFieldDescriptionAddedFired_ ||
          rl3.keyFieldDescriptionAddedFired_ ||
          rl3.fieldDescriptionAddedFired_)
      {
        failed("three listeners");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();

      // Remove rl2 from the listener list
      r.removeRecordDescriptionListener(rl2);
      // Cause a RecordDescriptionEvent to be fired
      rFireRecordDescriptionEvents(r);
      if (!rl1.fieldModifiedFired_ ||
          rl2.fieldModifiedFired_ ||
          !rl3.fieldModifiedFired_ ||
          rl1.keyFieldDescriptionAddedFired_ ||
          rl1.fieldDescriptionAddedFired_ ||
          rl2.fieldDescriptionAddedFired_ ||
          rl2.keyFieldDescriptionAddedFired_ ||
          rl3.keyFieldDescriptionAddedFired_ ||
          rl3.fieldDescriptionAddedFired_)
      {
        failed("after removing a listener");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage Record.removePropertyChangeListener().
   *<ul compact>
   *<li>Add a several PropertyChangeListeners
   *<li>Remove one PropertyChangeListener
   *<li>Invoke a function which fires a PropertyChangeEvent
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
  public void Var020()
  {
    try
    {
      // Create our property change listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl3 = new DDMPropertyChangeListener();
      // Create our record object
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "field1"));
      Record r = new Record(rf);

      // Verify three listeners
      r.addPropertyChangeListener(rl1);
      r.addPropertyChangeListener(rl2);
      r.addPropertyChangeListener(rl3);
      // Cause a PropertyChangeEvent to be fired
      rFirePropertyChangeEvents(r, "name3");
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals("name3") ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("recordName") ||
          !rl2.propertyChangeFired_ ||
          !rl2.e_.getNewValue().equals("name3") ||
          !rl2.e_.getOldValue().equals("") ||
          !rl2.e_.getPropertyName().equals("recordName") ||
          !rl3.propertyChangeFired_ ||
          !rl3.e_.getNewValue().equals("name3") ||
          !rl3.e_.getOldValue().equals("") ||
          !rl3.e_.getPropertyName().equals("recordName"))
      {
        failed("three listeners");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      // Remove rl2 for list of listeners
      r.removePropertyChangeListener(rl2);
      // Cause a PropertyChangeEvent to be fired
      rFirePropertyChangeEvents(r, "name4");
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getNewValue().equals("name4") ||
          !rl1.e_.getOldValue().equals("name3") ||
          !rl1.e_.getPropertyName().equals("recordName") ||
          rl2.propertyChangeFired_ ||
          rl2.e_ != null ||
          !rl3.propertyChangeFired_ ||
          !rl3.e_.getNewValue().equals("name4") ||
          !rl3.e_.getOldValue().equals("name3") ||
          !rl3.e_.getPropertyName().equals("recordName"))
      {
        failed("three listeners with veto");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage Record.removeVetoableChangeListener().
   *<ul compact>
   *<li>Add a several VetoableChangeListeners
   *<li>Remove one VetoableChangeListener
   *<li>Invoke a function which fires a VetoableChangeEvent
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
  public void Var021()
  {
    try
    {
      // Create our vetoable change listeners
      DDMVetoableChangeListener rl1 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl2 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      // Create our record object
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "field1"));
      Record r = new Record(rf);

      // Verify three listeners, no veto
      r.addVetoableChangeListener(rl1);
      r.addVetoableChangeListener(rl2);
      r.addVetoableChangeListener(rl3);
      // Cause a VetoableChangeEvent to be fired
      rFireVetoableChangeEvents(r, "name3");
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals("name3") ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getPropertyName().equals("recordName") ||
          !rl2.vetoableChangeFired_ ||
          !rl2.e_.getNewValue().equals("name3") ||
          !rl2.e_.getOldValue().equals("") ||
          !rl2.e_.getPropertyName().equals("recordName") ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getNewValue().equals("name3") ||
          !rl3.e_.getOldValue().equals("") ||
          !rl3.e_.getPropertyName().equals("recordName"))
      {
        failed("three listeners");
        return;
      }
      // Reset the listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      // Remove listener rl2
      r.removeVetoableChangeListener(rl2);
      // Cause a VetoableChangeEvent to be fired
      rFireVetoableChangeEvents(r, "name4");
      if (!rl1.vetoableChangeFired_ ||
          !rl1.e_.getNewValue().equals("name4") ||
          !rl1.e_.getOldValue().equals("name3") ||
          !rl1.e_.getPropertyName().equals("recordName") ||
          rl2.vetoableChangeFired_ ||
          rl2.e_ != null ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getNewValue().equals("name4") ||
          !rl3.e_.getOldValue().equals("name3") ||
          !rl3.e_.getPropertyName().equals("recordName"))
      {
        failed("three listeners with veto");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage Record.removeRecordDescriptionListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var022()
  {
    try
    {
      Record r = new Record();
      r.removeRecordDescriptionListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage Record.removePropertyChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var023()
  {
    try
    {
      Record r = new Record();
      r.removePropertyChangeListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage Record.removeVetoableChangeListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var024()
  {
    try
    {
      Record r = new Record();
      r.removeVetoableChangeListener(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "listener"))
      {
        failed(e, "wrong exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify that RecordFormat.addFieldDescription() fires a
   *a RecordDescriptionEvent invoking the fieldDescriptionAdded()
   *method on the RecordDescriptionListener object.
   *<ul compact>
   *<li>Add listeners and then add a field description to the
   *the record format being listened to.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fieldDescriptionAdded() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var025()
  {
    try
    {
      // Create the listeners
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl2 = new RFRecordDescriptionListener();

      // Create the record format object and add the listeners
      RecordFormat r = new RecordFormat();
      r.addRecordDescriptionListener(rl1);
      r.addRecordDescriptionListener(rl2);

      // Add a field desciption
      r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "fld"));

      // Verify listeners notified of the event and only that event
      if (!rl1.fieldDescriptionAddedFired_ || rl1.keyFieldDescriptionAddedFired_ ||
          !rl2.fieldDescriptionAddedFired_ || rl2.keyFieldDescriptionAddedFired_)
      {
        failed("Error in notification of feildDescriptionAdded");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify that RecordFormat.addKeyFieldDescription() fires a
   *a RecordDescriptionEvent invoking the keyFieldDescriptionAdded()
   *method on the RecordDescriptionListener object.
   *<ul compact>
   *<li>Add listeners and then add a key field description to the
   *the record format being listened to.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The keyFieldDescriptionAdded() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var026()
  {
    try
    {
      // Create the listeners
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl2 = new RFRecordDescriptionListener();

      // Create the record format object and add a field description
      RecordFormat r = new RecordFormat();
      r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "fld"));

      // Add listeners
      r.addRecordDescriptionListener(rl1);
      r.addRecordDescriptionListener(rl2);

      // Add key field description
      r.addKeyFieldDescription(0);

      // Verify listeners notified of the event and only that event
      if (rl1.fieldDescriptionAddedFired_ || !rl1.keyFieldDescriptionAddedFired_ ||
          rl2.fieldDescriptionAddedFired_ || !rl2.keyFieldDescriptionAddedFired_)
      {
        failed("Error in notification of keyFieldDescriptionAdded");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify that RecordFormat.setName() fires a
   *a VetoableChangeEvent and a PropertyChangeEvent invoking
   *the vetoable/propertyChange method on the Vetoable/PropertyChangeListener object.
   *<ul compact>
   *<li>Add listeners and then set the name of the
   *the record format being listened to.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The propertyChange() method will be invoked on the
   *the property change listener objects.
   *<li>The vetoableChange() method will be invoked on the
   *the vetoable change listener objects.
   *</ul>
  **/
  public void Var027()
  {
    try
    {
      // Create the listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl4 = new DDMVetoableChangeListener();

      // Create the record format object
      RecordFormat r = new RecordFormat();

      // Add listeners
      r.addPropertyChangeListener(rl1);
      r.addPropertyChangeListener(rl2);
      r.addVetoableChangeListener(rl3);
      r.addVetoableChangeListener(rl4);

      // Set the name
      r.setName("name");

      // Verify listeners notified of the event
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getNewValue().equals("NAME") ||
          !rl1.e_.getPropertyName().equals("name") ||
          !rl2.propertyChangeFired_ ||
          !rl2.e_.getOldValue().equals("") ||
          !rl2.e_.getNewValue().equals("NAME") ||
          !rl2.e_.getPropertyName().equals("name") ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getOldValue().equals("") ||
          !rl3.e_.getNewValue().equals("NAME") ||
          !rl3.e_.getPropertyName().equals("name") ||
          !rl4.vetoableChangeFired_ ||
          !rl4.e_.getOldValue().equals("") ||
          !rl4.e_.getNewValue().equals("NAME") ||
          !rl4.e_.getPropertyName().equals("name"))
      {
        failed("Error in notification of property change and vetoable change");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      rl4.reset();
      // Verify listeners, with a veto from rl4
      rl4.vetoTheChange = true;
      // Cause a VetoableChangeEvent to be fired
      try
      {
        r.setName("name4");
      }
      catch(PropertyVetoException p)
      {
        if (!rl3.vetoableChangeFired_ ||
            !rl3.e_.getNewValue().equals("NAME") ||
            !rl3.e_.getOldValue().equals("NAME4") ||
            !rl3.e_.getPropertyName().equals("name") ||
            !rl4.vetoed_ ||
            rl4.e_ != null ||
            rl1.propertyChangeFired_ ||
            rl2.propertyChangeFired_)
        {
          failed("listeners with veto");
          return;
        }
        else
        {
          succeeded();
          return;
        }
      }
      failed("No propertyVetoException");
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }

  /**
   *Verify that Record.getField() fires a
   *a RecordDescriptionEvent invoking the fieldModified()
   *method on the RecordDescriptionListener object.
   *The event will be fired when the record has no dependent
   *fields and the contents have been set via setContents or on the constructor.
   *<ul compact>
   *<li>Add listeners and then invoke getField().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fieldModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var028()
  {
    try
    {
      // Create the listeners
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl2 = new RFRecordDescriptionListener();

      // Create the record object and add the listeners
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "fld"));
      // Set the contents with a byte array so that getField will cause the
      // actual field value to be set prior to returning.
      Record r = new Record(rf, new AS400Bin4().toBytes(new Integer(5)));
      r.addRecordDescriptionListener(rl1);
      r.addRecordDescriptionListener(rl2);

      // Get a field
      r.getField(0);

      // Verify listeners notified of the event and only that event
      if (rl1.fieldDescriptionAddedFired_ ||
          rl1.keyFieldDescriptionAddedFired_ ||
          !rl1.fieldModifiedFired_ ||
          rl2.fieldDescriptionAddedFired_ ||
          rl2.keyFieldDescriptionAddedFired_ ||
          !rl2.fieldModifiedFired_)
      {
        failed("Error in notification of fieldModified");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify that Record.setField() fires a
   *a RecordDescriptionEvent invoking the fieldModified()
   *method on the RecordDescriptionListener object.
   *<ul compact>
   *<li>Add listeners and then invoke setField().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fieldModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var029()
  {
    try
    {
      // Create the listeners
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl2 = new RFRecordDescriptionListener();

      // Create the record object and add the listeners
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "fld"));
      Record r = new Record(rf);
      r.addRecordDescriptionListener(rl1);
      r.addRecordDescriptionListener(rl2);

      // Set a field
      r.setField(0, new Integer(5));

      // Verify listeners notified of the event and only that event
      if (rl1.fieldDescriptionAddedFired_ ||
          rl1.keyFieldDescriptionAddedFired_ ||
          !rl1.fieldModifiedFired_ ||
          rl2.fieldDescriptionAddedFired_ ||
          rl2.keyFieldDescriptionAddedFired_ ||
          !rl2.fieldModifiedFired_)
      {
        failed("Error in notification of fieldModified");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify that Record.setContents() fires a
   *a RecordDescriptionEvent invoking the fieldModified()
   *method on the RecordDescriptionListener object.
   *This will only happen if the record has dependent fields.
   *<ul compact>
   *<li>Add listeners and then invoke setContents().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fieldModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var030()
  {
    try
    {
      // Create the listeners
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl2 = new RFRecordDescriptionListener();

      // Create the record object and add the listeners
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "fld"));
      rf.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "charfld"));
      // The record format must has dependent fields for the event to be fired on setContents.
      rf.setLengthDependency(1,0);
      Record r = new Record(rf);
      r.addRecordDescriptionListener(rl1);
      r.addRecordDescriptionListener(rl2);

      // Set the contents
      byte[] b = new byte[14];
      AS400Bin4 b4 = new AS400Bin4();
      AS400Text t10 = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      b4.toBytes(new Integer(10), b);
      t10.toBytes("AAAAAAAAAA", b, 4);
      r.setContents(b);

      // Verify listeners notified of the event and only that event
      if (rl1.fieldDescriptionAddedFired_ ||
          rl1.keyFieldDescriptionAddedFired_ ||
          !rl1.fieldModifiedFired_ ||
          rl2.fieldDescriptionAddedFired_ ||
          rl2.keyFieldDescriptionAddedFired_ ||
          !rl2.fieldModifiedFired_)
      {
        failed("Error in notification of fieldModified");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify that Record.setRecordFormat() fires a
   *a RecordDescriptionEvent invoking the fieldModified()
   *method on the RecordDescriptionListener object.
   *<ul compact>
   *<li>Add listeners and then invoke setRecordFormat().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The fieldModified() method will be invoked on the
   *the listener objects.
   *</ul>
  **/
  public void Var031()
  {
    try
    {
      // Create the listeners
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      RFRecordDescriptionListener rl2 = new RFRecordDescriptionListener();

      // Create the record object and add the listeners
      Record r = new Record();
      r.addRecordDescriptionListener(rl1);
      r.addRecordDescriptionListener(rl2);

      // Set the record format
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "fld"));
      r.setRecordFormat(rf);

      // Verify listeners notified of the event and only that event
      if (rl1.fieldDescriptionAddedFired_ ||
          rl1.keyFieldDescriptionAddedFired_ ||
          !rl1.fieldModifiedFired_ ||
          rl2.fieldDescriptionAddedFired_ ||
          rl2.keyFieldDescriptionAddedFired_ ||
          !rl2.fieldModifiedFired_)
      {
        failed("Error in notification of fieldModified");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify that Record.setRecordFormat() fires a
   *a VetoableChangeEvent and a PropertyChangeEvent invoking
   *the vetoable/propertyChange method on the Vetoable/PropertyChangeListener object.
   *<ul compact>
   *<li>Add listeners and then set the 
   *record format of the record being listened to.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The propertyChange() method will be invoked on the
   *the property change listener objects.
   *<li>The vetoableChange() method will be invoked on the
   *the vetoable change listener objects.
   *</ul>
  **/
  public void Var032()
  {
    try
    {
      // Create the listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl4 = new DDMVetoableChangeListener();

      // Create the record object
      Record r = new Record();

      // Add listeners
      r.addPropertyChangeListener(rl1);
      r.addPropertyChangeListener(rl2);
      r.addVetoableChangeListener(rl3);
      r.addVetoableChangeListener(rl4);

      // Set the record format
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "F"));
      r.setRecordFormat(rf);

      // Verify listeners notified of the event
      if (!rl1.propertyChangeFired_ ||
          rl1.e_.getOldValue() != null ||
          rl1.e_.getNewValue() != rf ||
          !rl1.e_.getPropertyName().equals("recordFormat") ||
          !rl2.propertyChangeFired_ ||
          rl2.e_.getOldValue() != null ||
          rl2.e_.getNewValue() != rf ||
          !rl2.e_.getPropertyName().equals("recordFormat") ||
          !rl3.vetoableChangeFired_ ||
          rl3.e_.getOldValue() != null ||
          rl3.e_.getNewValue() != rf ||
          !rl3.e_.getPropertyName().equals("recordFormat") ||
          !rl4.vetoableChangeFired_ ||
          rl4.e_.getOldValue() != null ||
          rl4.e_.getNewValue() != rf ||
          !rl4.e_.getPropertyName().equals("recordFormat"))
      {
        failed("Error in notification of property change and vetoable change");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      rl4.reset();
      // Verify listeners, with a veto from rl4
      rl4.vetoTheChange = true;
      // Cause a VetoableChangeEvent to be fired
      RecordFormat rf2 = new RecordFormat();
      rf2.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "F"));
      try
      {
        r.setRecordFormat(rf2);
      }
      catch(PropertyVetoException p)
      {
        if (!rl3.vetoableChangeFired_ ||
            rl3.e_.getNewValue() != rf ||
            rl3.e_.getOldValue() != rf2 ||
            !rl3.e_.getPropertyName().equals("recordFormat") ||
            !rl4.vetoed_ ||
            rl4.e_ != null ||
            rl1.propertyChangeFired_ ||
            rl2.propertyChangeFired_)
        {
          failed("listeners with veto");
          return;
        }
        else
        {
          succeeded();
          return;
        }
      }
      failed("No propertyVetoException");
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }

  /**
   *Verify that Record.setRecordName() fires a
   *a VetoableChangeEvent and a PropertyChangeEvent invoking
   *the vetoable/propertyChange method on the Vetoable/PropertyChangeListener object.
   *<ul compact>
   *<li>Add listeners and then set the name of the
   *the record being listened to.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The propertyChange() method will be invoked on the
   *the property change listener objects.
   *<li>The vetoableChange() method will be invoked on the
   *the vetoable change listener objects.
   *</ul>
  **/
  public void Var033()
  {
    try
    {
      // Create the listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl4 = new DDMVetoableChangeListener();

      // Create the record object
      Record r = new Record();

      // Add listeners
      r.addPropertyChangeListener(rl1);
      r.addPropertyChangeListener(rl2);
      r.addVetoableChangeListener(rl3);
      r.addVetoableChangeListener(rl4);

      // Set the name
      r.setRecordName("NAME");

      // Verify listeners notified of the event
      if (!rl1.propertyChangeFired_ ||
          !rl1.e_.getOldValue().equals("") ||
          !rl1.e_.getNewValue().equals("NAME") ||
          !rl1.e_.getPropertyName().equals("recordName") ||
          !rl2.propertyChangeFired_ ||
          !rl2.e_.getOldValue().equals("") ||
          !rl2.e_.getNewValue().equals("NAME") ||
          !rl2.e_.getPropertyName().equals("recordName") ||
          !rl3.vetoableChangeFired_ ||
          !rl3.e_.getOldValue().equals("") ||
          !rl3.e_.getNewValue().equals("NAME") ||
          !rl3.e_.getPropertyName().equals("recordName") ||
          !rl4.vetoableChangeFired_ ||
          !rl4.e_.getOldValue().equals("") ||
          !rl4.e_.getNewValue().equals("NAME") ||
          !rl4.e_.getPropertyName().equals("recordName"))
      {
        failed("Error in notification of property change and vetoable change");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      rl4.reset();
      // Verify listeners, with a veto from rl4
      rl4.vetoTheChange = true;
      // Cause a VetoableChangeEvent to be fired
      try
      {
        r.setRecordName("name4");
      }
      catch(PropertyVetoException p)
      {
        if (!rl3.vetoableChangeFired_ ||
            !rl3.e_.getNewValue().equals("NAME") ||
            !rl3.e_.getOldValue().equals("name4") ||
            !rl3.e_.getPropertyName().equals("recordName") ||
            !rl4.vetoed_ ||
            rl4.e_ != null ||
            rl1.propertyChangeFired_ ||
            rl2.propertyChangeFired_)
        {
          failed("listeners with veto");
          return;
        }
        else
        {
          succeeded();
          return;
        }
      }
      failed("No propertyVetoException");
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }

  /**
   *Verify that Record.setRecordNumber() fires a
   *a VetoableChangeEvent and a PropertyChangeEvent invoking
   *the vetoable/propertyChange method on the Vetoable/PropertyChangeListener object.
   *<ul compact>
   *<li>Add listeners and then set the number of the
   *the record being listened to.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The propertyChange() method will be invoked on the
   *the property change listener objects.
   *<li>The vetoableChange() method will be invoked on the
   *the vetoable change listener objects.
   *</ul>
  **/
  public void Var034()
  {
    try
    {
      // Create the listeners
      DDMPropertyChangeListener rl1 = new DDMPropertyChangeListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      DDMVetoableChangeListener rl4 = new DDMVetoableChangeListener();

      // Create the record object
      Record r = new Record();

      // Add listeners
      r.addPropertyChangeListener(rl1);
      r.addPropertyChangeListener(rl2);
      r.addVetoableChangeListener(rl3);
      r.addVetoableChangeListener(rl4);

      // Set the number
      r.setRecordNumber(5);

      // Verify listeners notified of the event
      if (!rl1.propertyChangeFired_ ||
          ((Integer)rl1.e_.getOldValue()).intValue() != 0 ||
          ((Integer)rl1.e_.getNewValue()).intValue() != 5 ||
          !rl1.e_.getPropertyName().equals("recordNumber") ||
          !rl2.propertyChangeFired_ ||
          ((Integer)rl2.e_.getOldValue()).intValue() != 0 ||
          ((Integer)rl2.e_.getNewValue()).intValue() != 5 ||
          !rl2.e_.getPropertyName().equals("recordNumber") ||
          !rl3.vetoableChangeFired_ ||
          ((Integer)rl3.e_.getOldValue()).intValue() != 0 ||
          ((Integer)rl3.e_.getNewValue()).intValue() != 5 ||
          !rl3.e_.getPropertyName().equals("recordNumber") ||
          !rl4.vetoableChangeFired_ ||
          ((Integer)rl4.e_.getOldValue()).intValue() != 0 ||
          ((Integer)rl4.e_.getNewValue()).intValue() != 5 ||
          !rl4.e_.getPropertyName().equals("recordNumber"))
      {
        failed("Error in notification of property change and vetoable change");
        return;
      }
      // Reset listeners
      rl1.reset();
      rl2.reset();
      rl3.reset();
      rl4.reset();
      // Verify listeners, with a veto from rl4
      rl4.vetoTheChange = true;
      // Cause a VetoableChangeEvent to be fired
      try
      {
        r.setRecordNumber(6);
      }
      catch(PropertyVetoException p)
      {
        if (!rl3.vetoableChangeFired_ ||
            ((Integer)rl3.e_.getNewValue()).intValue() != 5 ||
            ((Integer)rl3.e_.getOldValue()).intValue() != 6 ||
            !rl3.e_.getPropertyName().equals("recordNumber") ||
            !rl4.vetoed_ ||
            rl4.e_ != null ||
            rl1.propertyChangeFired_ ||
            rl2.propertyChangeFired_)
        {
          failed("listeners with veto");
          return;
        }
        else
        {
          succeeded();
          return;
        }
      }
      failed("No propertyVetoException");
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
  }
}



