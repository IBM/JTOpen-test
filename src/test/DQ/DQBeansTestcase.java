///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQBeansTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DQ;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.DataQueueAttributes;
import com.ibm.as400.access.DataQueueEvent;
import com.ibm.as400.access.DataQueueListener;
import com.ibm.as400.access.KeyedDataQueue;
import com.ibm.as400.access.ObjectEvent;
import com.ibm.as400.access.ObjectListener;

import test.PasswordVault;
import test.Testcase;

/**
 Testcase DQBeansTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>DataQueue::addDataQueueListener()
 <li>KeyedDataQueue::addDataQueueListener()
 <li>DataQueue::addObjectListener()
 <li>KeyedDataQueue::addObjectListener()
 <li>DataQueue::addPropertyChangeListener()
 <li>KeyedDataQueue::addPropertyChangeListener()
 <li>DataQueue::addVetoableChangeListener()
 <li>KeyedDataQueue::addVetoableChangeListener()
 <li>DataQueue::removeDataQueueListener()
 <li>KeyedDataQueue::removeDataQueueListener()
 <li>DataQueue::removeObjectListener()
 <li>KeyedDataQueue::removeObjectListener()
 <li>DataQueue::removePropertyChangeListener()
 <li>KeyedDataQueue::removePropertyChangeListener()
 <li>DataQueue::removeVetoableChangeListener()
 <li>KeyedDataQueue::removeVetoableChangeListener()
 <li>DataQueueAttributes::addPropertyChangeListener()
 <li>DataQueueAttributes::addVetoableChangeListener()
 <li>DataQueueAttributes::removePropertyChangeListener()
 <li>DataQueueAttributes::removeVetoableChangeListener()
 </ul>
 <p>The following methods are used to generate events:
 <ul>
 <li>DataQueue::clear()
 <li>KeyedDataQueue::clear()
 <li>DataQueue::create()
 <li>KeyedDataQueue::create()
 <li>DataQueue::delete()
 <li>KeyedDataQueue::delete()
 <li>DataQueue::getDescription()
 <li>KeyedDataQueue::getDescription()
 <li>DataQueue::getForceToAuxiliaryStorage()
 <li>KeyedDataQueue::getForceToAuxiliaryStorage()
 <li>KeyedDataQueue::getKeyLength()
 <li>DataQueue::getMaxEntryLength()
 <li>KeyedDataQueue::getMaxEntryLength()
 <li>DataQueue::getSaveSenderInformation()
 <li>KeyedDataQueue::getSaveSenderInformation()
 <li>DataQueue::isFIFO()
 <li>KeyedDataQueue::isFIFO()
 <li>DataQueue::peek()
 <li>KeyedDataQueue::peek()
 <li>DataQueue::read()
 <li>KeyedDataQueue::read()
 <li>DataQueue::refreshAttributes()
 <li>KeyedDataQueue::refreshAttributes()
 <li>DataQueue::setCcsid()
 <li>KeyedDataQueue::setCcsid()
 <li>DataQueue::setPath()
 <li>KeyedDataQueue::setPath()
 <li>DataQueue::setSystem()
 <li>KeyedDataQueue::setSystem()
 <li>DataQueue::write()
 <li>KeyedDataQueue::write()
 <li>DataQueueAttributes::setAuthority()
 <li>DataQueueAttributes::setDescription()
 <li>DataQueueAttributes::setEntryLength()
 <li>DataQueueAttributes::setFIFO()
 <li>DataQueueAttributes::setForceToAuxiliaryStorage()
 <li>DataQueueAttributes::setKeyLength()
 <li>DataQueueAttributes::setSaveSenderInfo()
 </ul>
 **/
public class DQBeansTestcase extends Testcase implements DataQueueListener, ObjectListener, PropertyChangeListener, VetoableChangeListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DQBeansTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DQTest.main(newArgs); 
   }
    String goodDQName = "/QSYS.LIB/QTEMP.LIB/BEAN.DTAQ";

    DataQueueEvent dqEvent;
    Object dqSource;
    int dqN;
    int dqCount;

    ObjectEvent objEvent;
    Object objSource;
    int objN;
    int objCount;

    PropertyChangeEvent propChange;
    Object propSource;
    String propertyName;
    Object oldPropValue;
    Object newPropValue;
    int propCount;

    PropertyChangeEvent vetoChange;
    Object vetoSource;
    String vetoPropName;
    Object oldVetoValue;
    Object newVetoValue;
    int vetoCount;
    boolean veto = false;

    public void cleared(DataQueueEvent event)
    {
        dqEvent = event;
        if (event != null)
        {
            dqSource = event.getSource();
            dqN = event.getID();
        }
        ++dqCount;
    }

    public void peeked(DataQueueEvent event)
    {
        dqEvent = event;
        if (event != null)
        {
            dqSource = event.getSource();
            dqN = event.getID();
        }
        ++dqCount;
    }

    public void read(DataQueueEvent event)
    {
        dqEvent = event;
        if (event != null)
        {
            dqSource = event.getSource();
            dqN = event.getID();
        }
        ++dqCount;
    }

    public void written(DataQueueEvent event)
    {
        dqEvent = event;
        if (event != null)
        {
            dqSource = event.getSource();
            dqN = event.getID();
        }
        ++dqCount;
    }

    public void objectClosed(ObjectEvent event)
    {
        objEvent = event;
        if (event != null) objSource = event.getSource();
        objN = 0;
        ++objCount;
    }

    public void objectCreated(ObjectEvent event)
    {
        objEvent = event;
        if (event != null) objSource = event.getSource();
        objN = 1;
        ++objCount;
    }

    public void objectDeleted(ObjectEvent event)
    {
        objEvent = event;
        if (event != null) objSource = event.getSource();
        objN = 2;
        ++objCount;
    }

    public void objectOpened(ObjectEvent event)
    {
        objEvent = event;
        if (event != null) objSource = event.getSource();
        objN = 3;
        ++objCount;
    }

    public void propertyChange(PropertyChangeEvent event)
    {
        propChange = event;
        if (event != null)
        {
            propSource = event.getSource();
            propertyName = event.getPropertyName();
            oldPropValue = event.getOldValue();
            newPropValue = event.getNewValue();
        }
        ++propCount;
    }

    public void vetoableChange(PropertyChangeEvent event) throws PropertyVetoException
    {
        vetoChange = event;
        if (event != null)
        {
            vetoSource = event.getSource();
            vetoPropName = event.getPropertyName();
            oldVetoValue = event.getOldValue();
            newVetoValue = event.getNewValue();
        }
        ++vetoCount;

        if (veto)
        {
            throw new PropertyVetoException("Property vetoed", event);
        }
    }

    void resetValues()
    {
        dqEvent = null;
        dqSource = null;
        dqN = -1;
        dqCount = 0;

        objEvent = null;
        objSource = null;
        objN = -1;
        objCount = 0;

        propChange = null;
        propSource = null;
        propertyName = null;
        oldPropValue = null;
        newPropValue = null;
        propCount = 0;

        vetoChange = null;
        vetoSource = null;
        vetoPropName = null;
        oldVetoValue = null;
        newVetoValue = null;
        vetoCount = 0;
        veto = false;
    }

    String verifyNoEvent()
    {
        String failMsg = "";
        if (dqEvent != null) failMsg += "No Event: dq event received: " + dqEvent + "\n";
        if (dqSource != null) failMsg += "No Event: dq source changed: " + dqSource + "\n";
        if (dqN != -1) failMsg += "No Event: dq number changed: " + dqN + "\n";
        if (dqCount != 0) failMsg += "No Event: dq count changed: " + dqCount + "\n";
        if (objEvent != null) failMsg += "No Event: object event received: " + objEvent + "\n";
        if (objSource != null) failMsg += "No Event: object source changed: " + objSource + "\n";
        if (objN != -1) failMsg += "No Event: object number changed: " + objN + "\n";
        if (objCount != 0) failMsg += "No Event: object count changed: " + objCount + "\n";
        if (propChange != null) failMsg += "No Event: property event received: " + propChange + "\n";
        if (propSource != null) failMsg += "No Event: property source changed: " + propSource + "\n";
        if (propertyName != null) failMsg += "No Event: property name changed: " + propertyName + "\n";
        if (oldPropValue != null) failMsg += "No Event: old prop value changed: " + oldPropValue + "\n";
        if (newPropValue != null) failMsg += "No Event: new prop value changed: " + newPropValue + "\n";
        if (propCount != 0) failMsg += "No Event: property count changed: " + propCount + "\n";
        if (vetoChange != null) failMsg += "No Event: veto event received: " + vetoChange + "\n";
        if (vetoSource != null) failMsg += "No Event: veto source changed: " + vetoSource + "\n";
        if (vetoPropName != null) failMsg += "No Event: veto name changed: " + vetoPropName + "\n";
        if (oldVetoValue != null) failMsg += "No Event: old veto value changed: " + oldVetoValue + "\n";
        if (newVetoValue != null) failMsg += "No Event: new veto value changed: " + newVetoValue + "\n";
        if (vetoCount != 0) failMsg += "No Event: veto count changed: " + vetoCount + "\n";
        return failMsg;
    }

    String verifyDQEvent(Object dq, int number)
    {
        String failMsg = "";
        if (dqEvent == null) failMsg += "DQ Event: dq event not received: " + dqEvent + "\n";
        if (dqSource != dq) failMsg += "DQ Event: dq source not correct: " + dqSource + " Expected: " + dq + "\n";
        if (dqN != number) failMsg += "DQ Event: dq number not correct: " + dqN + " Expected: " + number + "\n";
        if (dqCount != 1) failMsg += "DQ Event: dq count not correct: " + dqCount + "\n";
        if (objEvent != null) failMsg += "DQ Event: object event received: " + objEvent + "\n";
        if (objSource != null) failMsg += "DQ Event: object source changed: " + objSource + "\n";
        if (objN != -1) failMsg += "DQ Event: object number changed: " + objN + "\n";
        if (objCount != 0) failMsg += "DQ Event: object count changed: " + objCount + "\n";
        if (propChange != null) failMsg += "DQ Event: property event received: " + propChange + "\n";
        if (propSource != null) failMsg += "DQ Event: property source changed: " + propSource + "\n";
        if (propertyName != null) failMsg += "DQ Event: property name changed: " + propertyName + "\n";
        if (oldPropValue != null) failMsg += "DQ Event: old prop value changed: " + oldPropValue + "\n";
        if (newPropValue != null) failMsg += "DQ Event: new prop value changed: " + newPropValue + "\n";
        if (propCount != 0) failMsg += "DQ Event: property count changed: " + propCount + "\n";
        if (vetoChange != null) failMsg += "DQ Event: veto event received: " + vetoChange + "\n";
        if (vetoSource != null) failMsg += "DQ Event: veto source changed: " + vetoSource + "\n";
        if (vetoPropName != null) failMsg += "DQ Event: veto name changed: " + vetoPropName + "\n";
        if (oldVetoValue != null) failMsg += "DQ Event: old veto value changed: " + oldVetoValue + "\n";
        if (newVetoValue != null) failMsg += "DQ Event: new veto value changed: " + newVetoValue + "\n";
        if (vetoCount != 0) failMsg += "DQ Event: veto count changed: " + vetoCount + "\n";
        return failMsg;
    }

    String verifyObjEvent(Object dq, int number)
    {
        String failMsg = "";
        if (dqEvent != null) failMsg += "Obj Event: dq event received: " + dqEvent + "\n";
        if (dqSource != null) failMsg += "Obj Event: dq source changed: " + dqSource + "\n";
        if (dqN != -1) failMsg += "Obj Event: dq number changed: " + dqN + "\n";
        if (dqCount != 0) failMsg += "Obj Event: dq count changed: " + dqCount + "\n";
        if (objEvent == null) failMsg += "Obj Event: object event not received: " + objEvent + "\n";
        if (objSource != dq) failMsg += "Obj Event: object source not correct: " + objSource + " Expected: " + dq + "\n";
        if (objN != number) failMsg += "Obj Event: object number not correct: " + objN + " Expected: " + number + "\n";
        if (objCount != 1) failMsg += "Obj Event: object count not correct: " + objCount + "\n";
        if (propChange != null) failMsg += "Obj Event: property event received: " + propChange + "\n";
        if (propSource != null) failMsg += "Obj Event: property source changed: " + propSource + "\n";
        if (propertyName != null) failMsg += "Obj Event: property name changed: " + propertyName + "\n";
        if (oldPropValue != null) failMsg += "Obj Event: old prop value changed: " + oldPropValue + "\n";
        if (newPropValue != null) failMsg += "Obj Event: new prop value changed: " + newPropValue + "\n";
        if (propCount != 0) failMsg += "Obj Event: property count changed: " + propCount + "\n";
        if (vetoChange != null) failMsg += "Obj Event: veto event received: " + vetoChange + "\n";
        if (vetoSource != null) failMsg += "Obj Event: veto source changed: " + vetoSource + "\n";
        if (vetoPropName != null) failMsg += "Obj Event: veto name changed: " + vetoPropName + "\n";
        if (oldVetoValue != null) failMsg += "Obj Event: old veto value changed: " + oldVetoValue + "\n";
        if (newVetoValue != null) failMsg += "Obj Event: new veto value changed: " + newVetoValue + "\n";
        if (vetoCount != 0) failMsg += "Obj Event: veto count changed: " + vetoCount + "\n";

        return failMsg;
    }

    String verifyPropertyChange(Object dq, String name, Object oldValue, Object newValue, Object curValue)
    {
        String failMsg = "";
        if (dqEvent != null) failMsg += "Prop Event: dq event received: " + dqEvent + "\n";
        if (dqSource != null) failMsg += "Prop Event: dq source changed: " + dqSource + "\n";
        if (dqN != -1) failMsg += "Prop Event: dq number changed: " + dqN + "\n";
        if (dqCount != 0) failMsg += "Prop Event: dq count changed: " + dqCount + "\n";
        if (objEvent != null) failMsg += "Prop Event: object event received: " + objEvent + "\n";
        if (objSource != null) failMsg += "Prop Event: object source changed: " + objSource + "\n";
        if (objN != -1) failMsg += "Prop Event: object number changed: " + objN + "\n";
        if (objCount != 0) failMsg += "Prop Event: object count changed: " + objCount + "\n";
        if (propChange == null) failMsg += "Prop Event: property event not received: " + propChange + "\n";
        if (propSource != dq) failMsg += "Prop Event: property source not correct: " + propSource + " Expected: " + dq + "\n";
        if (propertyName == null || !propertyName.equals(name)) failMsg += "Prop Event: property name not correct: " + propertyName + " Expected: " + name + "\n";
        if (oldPropValue == null || !oldPropValue.equals(oldValue)) failMsg += "Prop Event: old prop value not correct: " + oldPropValue + " Expected: " + oldValue + "\n";
        if (newPropValue == null || !newPropValue.equals(newValue)) failMsg += "Prop Event: new prop value not correct: " + newPropValue + " Expected: " + newValue + "\n";
        if (curValue == null || !curValue.equals(newValue)) failMsg += "Prop Event: property not changed: " + curValue + " Expected: " + newValue + "\n";
        if (propCount != 1) failMsg += "Prop Event: property count not correct: " + propCount + "\n";
        if (vetoChange != null) failMsg += "Prop Event: veto event received: " + vetoChange + "\n";
        if (vetoSource != null) failMsg += "Prop Event: veto source changed: " + vetoSource + "\n";
        if (vetoPropName != null) failMsg += "Prop Event: veto name changed: " + vetoPropName + "\n";
        if (oldVetoValue != null) failMsg += "Prop Event: old veto value changed: " + oldVetoValue + "\n";
        if (newVetoValue != null) failMsg += "Prop Event: new veto value changed: " + newVetoValue + "\n";
        if (vetoCount != 0) failMsg += "Prop Event: veto count changed: " + vetoCount + "\n";
        return failMsg;
    }

    String verifyVetoableChange(Object dq, String name, Object oldValue, Object newValue, Object curValue)
    {
        String failMsg = "";
        if (dqEvent != null) failMsg += "Veto Event: dq event received: " + dqEvent + "\n";
        if (dqSource != null) failMsg += "Veto Event: dq source changed: " + dqSource + "\n";
        if (dqN != -1) failMsg += "Veto Event: dq number changed: " + dqN + "\n";
        if (dqCount != 0) failMsg += "Veto Event: dq count changed: " + dqCount + "\n";
        if (objEvent != null) failMsg += "Veto Event: object event received: " + objEvent + "\n";
        if (objSource != null) failMsg += "Veto Event: object source changed: " + objSource + "\n";
        if (objN != -1) failMsg += "Veto Event: object number changed: " + objN + "\n";
        if (objCount != 0) failMsg += "Veto Event: object count changed: " + objCount + "\n";
        if (propChange != null) failMsg += "Veto Event: property event received: " + propChange + "\n";
        if (propSource != null) failMsg += "Veto Event: property source changed: " + propSource + "\n";
        if (propertyName != null) failMsg += "Veto Event: property name changed: " + propertyName + "\n";
        if (oldPropValue != null) failMsg += "Veto Event: old prop value changed: " + oldPropValue + "\n";
        if (newPropValue != null) failMsg += "Veto Event: new prop value changed: " + newPropValue + "\n";
        if (propCount != 0) failMsg += "Veto Event: property count changed: " + propCount + "\n";
        if (vetoChange == null) failMsg += "Veto Event: veto event not received: " + vetoChange + "\n";
        if (vetoSource != dq) failMsg += "Veto Event: veto source not correct: " + vetoSource + " Expected: " + dq + "\n";
        if (vetoPropName == null|| !vetoPropName.equals(name)) failMsg += "Veto Event: veto name not correct: " + vetoPropName + " Expected: " + name + "\n";
        if (oldVetoValue == null || !oldVetoValue.equals(oldValue)) failMsg += "Veto Event: old veto value not correct: " + oldVetoValue + " Expected: " + oldValue + "\n";
        if (newVetoValue == null || !newVetoValue.equals(newValue)) failMsg += "Veto Event: new veto value not correct: " + newVetoValue + " Expected: " + newValue + "\n";
        if (curValue == null || !curValue.equals(newValue)) failMsg += "Veto Event: property not changed: " + curValue + " Expected: " + newValue + "\n";
        if (vetoCount != 1) failMsg += "Veto Event: veto count changed: " + vetoCount + "\n";
        return failMsg;
    }

    String verifyVetoedChange(PropertyChangeEvent event, Object dq, String name, Object oldValue, Object newValue, Object curValue)
    {
        String failMsg = "";
        if (dqEvent != null) failMsg += "Vetoed Event: dq event received: " + dqEvent + "\n";
        if (dqSource != null) failMsg += "Vetoed Event: dq source changed: " + dqSource + "\n";
        if (dqN != -1) failMsg += "Vetoed Event: dq number changed: " + dqN + "\n";
        if (dqCount != 0) failMsg += "Vetoed Event: dq count changed: " + dqCount + "\n";
        if (objEvent != null) failMsg += "Vetoed Event: object event received: " + objEvent + "\n";
        if (objSource != null) failMsg += "Vetoed Event: object source changed: " + objSource + "\n";
        if (objN != -1) failMsg += "Vetoed Event: object number changed: " + objN + "\n";
        if (objCount != 0) failMsg += "Vetoed Event: object count changed: " + objCount + "\n";
        if (propChange != null) failMsg += "Vetoed Event: property event received: " + propChange + "\n";
        if (propSource != null) failMsg += "Vetoed Event: property source changed: " + propSource + "\n";
        if (propertyName != null) failMsg += "Vetoed Event: property name changed: " + propertyName + "\n";
        if (oldPropValue != null) failMsg += "Vetoed Event: old prop value changed: " + oldPropValue + "\n";
        if (newPropValue != null) failMsg += "Vetoed Event: new prop value changed: " + newPropValue + "\n";
        if (propCount != 0) failMsg += "Vetoed Event: property count changed: " + propCount + "\n";
        if (vetoChange == null || !vetoChange.getSource().equals(event.getSource())) failMsg += "Vetoed Event: veto event not correct: " + vetoChange + " Expected: " + event + "\n";
        if (vetoSource != dq) failMsg += "Vetoed Event: veto source not correct: " + vetoSource + " Expected: " + dq + "\n";
        if (vetoPropName == null|| !vetoPropName.equals(name)) failMsg += "Vetoed Event: veto name not correct: " + vetoPropName + " Expected: " + name + "\n";
	if (isAtLeastJDK17) {
	    if (oldVetoValue == null || !oldVetoValue.equals(oldValue)) failMsg += "Vetoed Event: old veto value not correct: " + oldVetoValue + " Expected: " + oldValue + "\n";
	    if (newVetoValue == null || !newVetoValue.equals(newValue)) failMsg += "Vetoed Event: new veto value not correct: " + newVetoValue + " Expected: " + newValue + "\n";

	} else { 
	    if (oldVetoValue == null || !oldVetoValue.equals(newValue)) failMsg += "Vetoed Event: old veto value not correct: " + oldVetoValue + " Expected: " + newValue + "\n";
	    if (newVetoValue == null || !newVetoValue.equals(oldValue)) failMsg += "Vetoed Event: new veto value not correct: " + newVetoValue + " Expected: " + oldValue + "\n";
	}

        if (curValue == null || !curValue.equals(oldValue)) failMsg += "Vetoed Event: property changed: " + curValue + " Expected: " + newValue + "\n";
	if (isAtLeastJDK17) {
            if (vetoCount != 1) failMsg += "Vetoed Event: veto count changed: " + vetoCount + "\n";
	} else { 
            if (vetoCount != 2) failMsg += "Vetoed Event: veto count changed: " + vetoCount + "\n";
	}

        return failMsg;
    }

    /**
     <p>Test:  Call DataQueue::addDataQueueListener() followed by DataQueue::clear().
     <p>Result:  Verify clear data queue event is fired.
     **/
    public void Var001()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            DataQueue dq2 = new DataQueue(systemObject_, goodDQName);
            dq2.create(10);
            try
            {
                // clear.
                dq.addDataQueueListener(this);
                dq.clear();
                dq.removeDataQueueListener(this);

                // Verify event
                failMsg += verifyDQEvent(dq, 0);
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addDataQueueListener() followed by KeyedDataQueue::clear().
     <p>Result:  Verify clear data queue event is fired.
     **/
    public void Var002()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, goodDQName);
            dq2.create(3, 10);
            try
            {
                // clear.
                dq.addDataQueueListener(this);
                dq.clear();
                dq.removeDataQueueListener(this);

                // Verify event
                failMsg += verifyDQEvent(dq, 0);
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::addDataQueueListener() followed by DataQueue::peek().
     <p>Result:  Verify peek data queue event is fired.
     **/
    public void Var003()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            DataQueue dq2 = new DataQueue(systemObject_, goodDQName);
            dq2.create(10);
            try
            {
                // peek.
                dq.write(new byte[] {1,2,3});
                dq.addDataQueueListener(this);
                dq.peek();
                dq.removeDataQueueListener(this);

                // Verify event
                failMsg += verifyDQEvent(dq, 1);
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addDataQueueListener() followed by KeyedDataQueue::peek().
     <p>Result:  Verify peek data queue event is fired.
     **/
    public void Var004()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, goodDQName);
            dq2.create(3, 10);
            try
            {
                // peek.
                dq.write(new byte[] {1,2,3}, new byte[] {1,2,3});
                dq.addDataQueueListener(this);
                dq.peek(new byte[] {1,2,3});
                dq.removeDataQueueListener(this);

                // Verify event
                failMsg += verifyDQEvent(dq, 1);
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::addDataQueueListener() followed by DataQueue::read().
     <p>Result:  Verify read data queue event is fired.
     **/
    public void Var005()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            DataQueue dq2 = new DataQueue(systemObject_, goodDQName);
            dq2.create(10);
            try
            {
                // read no wait.
                dq.write(new byte[] {1,2,3});
                dq.addDataQueueListener(this);
                dq.read();
                dq.removeDataQueueListener(this);

                // Verify event
                failMsg += verifyDQEvent(dq, 2);
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addDataQueueListener() followed by KeyedDataQueue::read().
     <p>Result:  Verify read data queue event is fired.
     **/
    public void Var006()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, goodDQName);
            dq2.create(3, 10);
            try
            {
                // read no wait.
                dq.write(new byte[] {1,2,3}, new byte[] {1,2,3});
                dq.addDataQueueListener(this);
                dq.read(new byte[] {1,2,3});
                dq.removeDataQueueListener(this);

                // Verify event
                failMsg += verifyDQEvent(dq, 2);
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::addDataQueueListener() followed by DataQueue::write().
     <p>Result:  Verify write data queue event is fired.
     **/
    public void Var007()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            DataQueue dq2 = new DataQueue(systemObject_, goodDQName);
            dq2.create(10);
            try
            {
                // write 3 bytes
                dq.addDataQueueListener(this);
                dq.write(new byte[] {1,2,3});
                dq.removeDataQueueListener(this);

                // Verify event
                failMsg += verifyDQEvent(dq, 3);
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addDataQueueListener() followed by KeyedDataQueue::write().
     <p>Result:  Verify write data queue event is fired.
     **/
    public void Var008()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, goodDQName);
            dq2.create(3, 10);
            try
            {
                // write 3 bytes
                dq.addDataQueueListener(this);
                dq.write(new byte[] {1,2,3}, new byte[] {1,2,3});
                dq.removeDataQueueListener(this);

                // Verify event
                failMsg += verifyDQEvent(dq, 3);
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Write failed.");
        }
    }

    /**
     <p>Test:  Call DataQueue::addDataQueueListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var009()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            dq.addDataQueueListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addDataQueueListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var010()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            dq.addDataQueueListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call DataQueue::removeDataQueueListener().
     <p>Result:  Verify no data queue event is fired.
     **/
    public void Var011()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            DataQueue dq2 = new DataQueue(systemObject_, goodDQName);
            dq2.create(10);
            try
            {
                // clear.
                dq.addDataQueueListener(this);
                dq.clear();
                dq.removeDataQueueListener(this);
                // Verify event
                failMsg += verifyDQEvent(dq, 0);

                resetValues();
                dq.clear();
                // Verify no event
                failMsg += verifyNoEvent();
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::removeDataQueueListener().
     <p>Result:  Verify no data queue event is fired.
     **/
    public void Var012()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, goodDQName);
            dq2.create(3, 10);
            try
            {
                // clear.
                dq.addDataQueueListener(this);
                dq.clear();
                dq.removeDataQueueListener(this);
                // Verify event
                failMsg += verifyDQEvent(dq, 0);

                resetValues();
                dq.clear();
                // Verify no event
                failMsg += verifyNoEvent();
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::removeDataQueueListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var013()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            dq.removeDataQueueListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::removeDataQueueListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var014()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            dq.removeDataQueueListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call DataQueue::addObjectListener() followed by DataQueue::create().
     <p>Result:  Verify create object event is fired.
     **/
    public void Var015()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            DataQueue dq2 = new DataQueue(systemObject_, goodDQName);

            // Create with length 1.
            dq.addObjectListener(this);
            dq.create(1);
            try
            {
                dq.removeObjectListener(this);
                // Verify event
                failMsg += verifyObjEvent(dq, 1);
                // Verify create.
                int length = dq2.getMaxEntryLength();
                if (length != 1) failMsg += "Wrong entry length: " + length + "\n";
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addObjectListener() followed by KeyedDataQueue::create().
     <p>Result:  Verify create object event is fired.
     **/
    public void Var016()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, goodDQName);

            // Create with length 1.
            dq.addObjectListener(this);
            dq.create(3, 1);
            try
            {
                dq.removeObjectListener(this);
                // Verify event
                failMsg += verifyObjEvent(dq, 1);
                // Verify create.
                int length = dq2.getMaxEntryLength();
                if (length != 1) failMsg += "Wrong entry length: " + length + "\n";
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::addObjectListener() followed by DataQueue::delete().
     <p>Result:  Verify delete object event is fired.
     **/
    public void Var017()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            DataQueue dq2 = new DataQueue(systemObject_, goodDQName);

            dq.create(10);
            try
            {
                dq2.refreshAttributes();
                // delete.
                dq.addObjectListener(this);
            }
            finally
            {
                dq.delete();
            }
            // Verify event
            dq.removeObjectListener(this);
            failMsg += verifyObjEvent(dq, 2);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addObjectListener() followed by KeyedDataQueue::delete().
     <p>Result:  Verify delete object event is fired.
     **/
    public void Var018()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, goodDQName);

            dq.create(3, 10);
            try
            {
                dq2.refreshAttributes();
                // delete.
                dq.addObjectListener(this);
            }
            finally
            {
                dq.delete();
            }
            // Verify event
            dq.removeObjectListener(this);
            failMsg += verifyObjEvent(dq, 2);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::addObjectListener() followed by DataQueue::refreshAttributes().
     <p>Result:  Verify open object event is fired.
     **/
    public void Var019()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            DataQueue dq2 = new DataQueue(systemObject_, goodDQName);
            dq2.create(10);
            try
            {
                // open with refresh.
                dq.addObjectListener(this);
                dq.refreshAttributes();
                dq.removeObjectListener(this);
                // Verify event
                failMsg += verifyObjEvent(dq, 3);
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addObjectListener() followed by KeyedDataQueue::refreshAttributes().
     <p>Result:  Verify open object event is fired.
     **/
    public void Var020()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, goodDQName);
            dq2.create(3, 10);
            try
            {
                // open with refresh.
                dq.addObjectListener(this);
                dq.refreshAttributes();
                dq.removeObjectListener(this);
                // Verify event
                failMsg += verifyObjEvent(dq, 3);
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::addObjectListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var021()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            dq.addObjectListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addObjectListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var022()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            dq.addObjectListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call DataQueue::removeObjectListener().
     <p>Result:  Verify no data queue event is fired.
     **/
    public void Var023()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            DataQueue dq2 = new DataQueue(systemObject_, goodDQName);
            dq2.create(10);
            try
            {
                // open with refresh.
                dq.addObjectListener(this);
                dq.refreshAttributes();
                dq.removeObjectListener(this);
                // Verify event
                failMsg += verifyObjEvent(dq, 3);

                resetValues();
                dq.refreshAttributes();
                // Verify no event
                failMsg += verifyNoEvent();
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::removeObjectListener().
     <p>Result:  Verify no data queue event is fired.
     **/
    public void Var024()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, goodDQName);
            dq2.create(3, 10);
            try
            {
                String failMsg = "";
                resetValues();

                // open with refresh.
                dq.addObjectListener(this);
                dq.refreshAttributes();
                dq.removeObjectListener(this);
                // Verify event
                failMsg += verifyObjEvent(dq, 3);

                resetValues();
                dq.refreshAttributes();
                // Verify no event
                failMsg += verifyNoEvent();
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::removeObjectListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var025()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            dq.removeObjectListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::removeObjectListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var026()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            dq.removeObjectListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call DataQueue::addPropertyChangeListener() followed by DataQueue::setPath().
     <p>Result:  Verify property change event is fired.
     **/
    public void Var027()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAQ";

            dq.addPropertyChangeListener(this);
            dq.setPath(newPath);
            dq.removePropertyChangeListener(this);

            // Verify event
            String value = dq.getPath();
            failMsg += verifyPropertyChange(dq, "path", goodDQName, newPath, value);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addPropertyChangeListener() followed by KeyedDataQueue::setPath().
     <p>Result:  Verify property change event is fired.
     **/
    public void Var028()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAQ";

            dq.addPropertyChangeListener(this);
            dq.setPath(newPath);
            dq.removePropertyChangeListener(this);

            // Verify event
            String value = dq.getPath();
            failMsg += verifyPropertyChange(dq, "path", goodDQName, newPath, value);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::addPropertyChangeListener() followed by DataQueue::setSystem().
     <p>Result:  Verify property change event is fired.
     **/
    public void Var029()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            AS400 newsys = new AS400();

            dq.addPropertyChangeListener(this);
            dq.setSystem(newsys);
            dq.removePropertyChangeListener(this);

            // Verify event
            AS400 value = dq.getSystem();
            failMsg += verifyPropertyChange(dq, "system", systemObject_, newsys, value);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addPropertyChangeListener() followed by KeyedDataQueue::setSystem().
     <p>Result:  Verify property change event is fired.
     **/
    public void Var030()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            AS400 newsys = new AS400();

            dq.addPropertyChangeListener(this);
            dq.setSystem(newsys);
            dq.removePropertyChangeListener(this);

            // Verify event
            AS400 value = dq.getSystem();
            failMsg += verifyPropertyChange(dq, "system", systemObject_, newsys, value);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::addPropertyChangeListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var031()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            dq.addPropertyChangeListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addPropertyChangeListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var032()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            dq.addPropertyChangeListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call DataQueue::removePropertyChangeListener().
     <p>Result:  Verify no property change event is fired.
     **/
    public void Var033()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAQ";

            dq.addPropertyChangeListener(this);
            dq.setPath(newPath);
            dq.removePropertyChangeListener(this);

            // Verify event
            String value = dq.getPath();
            failMsg += verifyPropertyChange(dq, "path", goodDQName, newPath, value);

            resetValues();
            String newerPath = "/QSYS.LIB/QTEMP.LIB/CHANGE2.DTAQ";
            dq.setPath(newerPath);

            // Verify no event
            failMsg += verifyNoEvent();
            value = dq.getPath();
            if (!newerPath.equals(value)) failMsg += "Verify error after remove: " + value + "\n";
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::removePropertyChangeListener().
     <p>Result:  Verify no property change event is fired.
     **/
    public void Var034()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAQ";

            dq.addPropertyChangeListener(this);
            dq.setPath(newPath);
            dq.removePropertyChangeListener(this);

            // Verify event
            String value = dq.getPath();
            failMsg += verifyPropertyChange(dq, "path", goodDQName, newPath, value);

            resetValues();
            String newerPath = "/QSYS.LIB/QTEMP.LIB/CHANGE2.DTAQ";
            dq.setPath(newerPath);

            // Verify no event
            failMsg += verifyNoEvent();
            value = dq.getPath();
            if (!newerPath.equals(value)) failMsg += "Verify error after remove: " + value + "\n";
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::removePropertyChangeListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var035()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            dq.removePropertyChangeListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::removePropertyChangeListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var036()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            dq.removePropertyChangeListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call DataQueue::addVetoableChangeListener() followed by DataQueue::setPath().
     <p>Result:  Verify vetoable change event is fired.
     **/
    public void Var037()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAQ";

            dq.addVetoableChangeListener(this);
            dq.setPath(newPath);
            dq.removeVetoableChangeListener(this);

            // Verify event
            String value = dq.getPath();
            failMsg += verifyVetoableChange(dq, "path", goodDQName, newPath, value);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addVetoableChangeListener() followed by KeyedDataQueue::setPath().
     <p>Result:  Verify vetoable change event is fired.
     **/
    public void Var038()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAQ";

            dq.addVetoableChangeListener(this);
            dq.setPath(newPath);
            dq.removeVetoableChangeListener(this);

            // Verify event
            String value = dq.getPath();
            failMsg += verifyVetoableChange(dq, "path", goodDQName, newPath, value);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::addVetoableChangeListener() followed by a vetoed DataQueue::setPath().
     <p>Result:  Verify a PropertyVetoException is thrown and vetoable change event is fired.
     **/
    public void Var039()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAQ";

            dq.addVetoableChangeListener(this);
            veto = true;
            try
            {
                dq.setPath(newPath);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                dq.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                String value = dq.getPath();
                failMsg += verifyVetoedChange(event, dq, "path", goodDQName, newPath, value);
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addVetoableChangeListener() followed by a vetoed KeyedDataQueue::setPath().
     <p>Result:  Verify a PropertyVetoException is thrown and vetoable change event is fired.
     **/
    public void Var040()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAQ";

            dq.addVetoableChangeListener(this);
            veto = true;
            try
            {
                dq.setPath(newPath);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                dq.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                String value = dq.getPath();
                failMsg += verifyVetoedChange(event, dq, "path", goodDQName, newPath, value);
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::addVetoableChangeListener() followed by DataQueue::setSystem().
     <p>Result:  Verify vetoable change event is fired.
     **/
    public void Var041()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            AS400 newsys = new AS400();

            dq.addVetoableChangeListener(this);
            dq.setSystem(newsys);
            dq.removeVetoableChangeListener(this);

            // Verify event
            AS400 value = dq.getSystem();
            failMsg += verifyVetoableChange(dq, "system", systemObject_, newsys, value);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::addVetoableChangeListener() followed by DataQueue::setSystem().
     <p>Result:  Verify vetoable change event is fired.
     **/
    public void Var042()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            AS400 newsys = new AS400();

            dq.addVetoableChangeListener(this);
            dq.setSystem(newsys);
            dq.removeVetoableChangeListener(this);

            // Verify event
            AS400 value = dq.getSystem();
            failMsg += verifyVetoableChange(dq, "system", systemObject_, newsys, value);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::addVetoableChangeListener() followed by a vetoed DataQueue::setSystem().
     <p>Result:  Verify a PropertyVetoException is thrown and vetoable change event is fired.
     **/
    public void Var043()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            AS400 newsys = new AS400();

            dq.addVetoableChangeListener(this);
            veto = true;
            try
            {
                dq.setSystem(newsys);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                dq.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                AS400 value = dq.getSystem();
                failMsg += verifyVetoedChange(event, dq, "system", systemObject_, newsys, value);
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addVetoableChangeListener() followed by a vetoed KeyedDataQueue::setSystem().
     <p>Result:  Verify a PropertyVetoException is thrown and vetoable change event is fired.
     **/
    public void Var044()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            AS400 newsys = new AS400();

            dq.addVetoableChangeListener(this);
            veto = true;
            try
            {
                dq.setSystem(newsys);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                dq.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                AS400 value = dq.getSystem();
                failMsg += verifyVetoedChange(event, dq, "system", systemObject_, newsys, value);
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::addVetoableChangeListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var045()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            dq.addVetoableChangeListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::addVetoableChangeListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var046()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            dq.addVetoableChangeListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call DataQueue::removeVetoableChangeListener().
     <p>Result:  Verify no vetoable change event is fired.
     **/
    public void Var047()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAQ";

            dq.addVetoableChangeListener(this);
            veto = true;
            try
            {
                dq.setPath(newPath);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                dq.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                String value = dq.getPath();
                failMsg += verifyVetoedChange(event, dq, "path", goodDQName, newPath, value);

                resetValues();
                String newerPath = "/QSYS.LIB/QTEMP.LIB/CHANGE2.DTAQ";
                dq.setPath(newerPath);
                // Verify no event
                failMsg += verifyNoEvent();
                value = dq.getPath();
                if (!newerPath.equals(value)) failMsg += "Verify error after remove: " + value + "\n";
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::removeVetoableChangeListener().
     <p>Result:  Verify no vetoable change event is fired.
     **/
    public void Var048()
    {
        try
        {
            String failMsg = "";
            resetValues();

            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAQ";

            dq.addVetoableChangeListener(this);
            veto = true;
            try
            {
                dq.setPath(newPath);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                dq.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                String value = dq.getPath();
                failMsg += verifyVetoedChange(event, dq, "path", goodDQName, newPath, value);

                resetValues();
                String newerPath = "/QSYS.LIB/QTEMP.LIB/CHANGE2.DTAQ";
                dq.setPath(newerPath);

                // Verify no event
                failMsg += verifyNoEvent();
                value = dq.getPath();
                if (!newerPath.equals(value)) failMsg += "Verify error after remove: " + value + "\n";
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueue::removeVetoableChangeListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var049()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, goodDQName);
            dq.removeVetoableChangeListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::removeVetoableChangeListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var050()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, goodDQName);
            dq.removeVetoableChangeListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Serialize DataQueue object.
     <p>Result:  Verify object deserializes and is usable.
     **/
    public void Var051()
    {
        try
        {
            String failMsg = "";

            DataQueue dq2 = new DataQueue(systemObject_, goodDQName);
            dq2.create(1);
            ObjectOutput oo = null; 
            try
            {
                // Serialize dq to a file.
                FileOutputStream fos = new FileOutputStream("dq.ser");
                oo =  new ObjectOutputStream(fos);
                oo.writeObject(dq2);
                oo.flush();
                ObjectInputStream ois = null; 
                try
                {
                    // Deserialize dq from a file.
                    FileInputStream fin = new FileInputStream("dq.ser");
                    ois = new ObjectInputStream(fin);
                    DataQueue dq = (DataQueue)ois.readObject();

                    String path = dq.getPath();
                    AS400 system = dq.getSystem();
                    String systemName = system.getSystemName();
                    String userId = system.getUserId();
                    if (!path.equals(goodDQName)) failMsg += "Path changed: " + path + "\n";
                    if (!systemName.equals(systemObject_.getSystemName())) failMsg += "System name changed: " + systemName + "\n";
                    if (!userId.equals(systemObject_.getUserId())) failMsg += "User ID changed: " + userId + "\n";
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                    system.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
                    int length = dq.getMaxEntryLength();
                    if (length != 1) failMsg += "Wrong entry length: " + length + "\n";
                    assertCondition(failMsg.equals(""), "\n" + failMsg);
                }
                finally
                {
                    File fd = new File("dq.ser");
                    fd.delete();
                    if (ois != null) ois.close(); 
                    oo.close(); 
                }
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Serialize KeyedDataQueue object.
     <p>Result:  Verify object deserializes and is usable.
     **/
    public void Var052()
    {
        try
        {
            String failMsg = "";

            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, goodDQName);
            dq2.create(3, 1);
            try
            {
                // Serialize dq to a file.
                FileOutputStream fos = new FileOutputStream("dq.ser");
                ObjectOutput oo  =  new  ObjectOutputStream(fos);
                oo.writeObject(dq2);
                oo.flush();
                oo.close(); 
                try
                {
                    // Deserialize a string and date from a file.
                    FileInputStream fin = new FileInputStream("dq.ser");
                    ObjectInputStream ois = new ObjectInputStream(fin);
                    KeyedDataQueue dq = (KeyedDataQueue)ois.readObject();

                    String path = dq.getPath();
                    AS400 system = dq.getSystem();
                    String systemName = system.getSystemName();
                    String userId = system.getUserId();
                    if (!path.equals(goodDQName))failMsg += "Path changed: " + path + "\n";
                    if (!systemName.equals(systemObject_.getSystemName())) failMsg += "System name changed: " + systemName + "\n";
                    if (!userId.equals(systemObject_.getUserId())) failMsg += "User ID changed: " + userId + "\n";
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                    system.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
                    int length = dq.getMaxEntryLength();
                    if (length != 1) failMsg += "Wrong entry length: " + length + "\n";
                    ois.close(); 
                    assertCondition(failMsg.equals(""), "\n" + failMsg);
                }
                finally
                {
                    File fd = new File("dq.ser");
                    fd.delete();
                }
            }
            finally
            {
                dq2.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addPropertyChangeListener() followed by DataQueueAttributes::setAuthority().
     <p>Result:  Verify property change event is fired.
     **/
    public void Var053()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            String oldValue = "*ALL";
            atts.setAuthority(oldValue);
            failMsg += verifyNoEvent();

            atts.addPropertyChangeListener(this);
            String newValue = "*USE";
            atts.setAuthority(newValue);
            atts.removePropertyChangeListener(this);

            // Verify event
            String value = atts.getAuthority();
            failMsg += verifyPropertyChange(atts, "authority", oldValue, newValue, value);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addPropertyChangeListener() followed by DataQueueAttributes::setDescription().
     <p>Result:  Verify property change event is fired.
     **/
    public void Var054()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            String oldValue = "xxxx";
            atts.setDescription(oldValue);
            failMsg += verifyNoEvent();

            atts.addPropertyChangeListener(this);
            String newValue = "yyyyyy";
            atts.setDescription(newValue);
            atts.removePropertyChangeListener(this);

            // Verify event
            String value = atts.getDescription();
            failMsg += verifyPropertyChange(atts, "description", oldValue, newValue, value);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addPropertyChangeListener() followed by DataQueueAttributes::setEntryLength().
     <p>Result:  Verify property change event is fired.
     **/
    public void Var055()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            int oldValue = 10;
            atts.setEntryLength(oldValue);
            failMsg += verifyNoEvent();

            atts.addPropertyChangeListener(this);
            int newValue = 20;
            atts.setEntryLength(newValue);
            atts.removePropertyChangeListener(this);

            // Verify event
            int value = atts.getEntryLength();
            failMsg += verifyPropertyChange(atts, "entryLength", Integer.valueOf(oldValue), Integer.valueOf(newValue), Integer.valueOf(value));
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addPropertyChangeListener() followed by DataQueueAttributes::setFIFO().
     <p>Result:  Verify property change event is fired.
     **/
    public void Var056()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            boolean oldValue = true;
            atts.setFIFO(oldValue);
            failMsg += verifyNoEvent();

            atts.addPropertyChangeListener(this);
            boolean newValue = false;
            atts.setFIFO(newValue);
            atts.removePropertyChangeListener(this);

            // Verify event
            boolean value = atts.isFIFO();
            failMsg += verifyPropertyChange(atts, "FIFO", Boolean.valueOf(oldValue), Boolean.valueOf(newValue), Boolean.valueOf(value));
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addPropertyChangeListener() followed by DataQueueAttributes::setForceToAuxiliaryStorage().
     <p>Result:  Verify property change event is fired.
     **/
    public void Var057()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            boolean oldValue = true;
            atts.setForceToAuxiliaryStorage(oldValue);
            failMsg += verifyNoEvent();

            atts.addPropertyChangeListener(this);
            boolean newValue = false;
            atts.setForceToAuxiliaryStorage(newValue);
            atts.removePropertyChangeListener(this);

            // Verify event
            boolean value = atts.isForceToAuxiliaryStorage();
            failMsg += verifyPropertyChange(atts, "forceToAuxiliaryStorage", Boolean.valueOf(oldValue), Boolean.valueOf(newValue), Boolean.valueOf(value));
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addPropertyChangeListener() followed by DataQueueAttributes::setKeyLength().
     <p>Result:  Verify property change event is fired.
     **/
    public void Var058()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            int oldValue = 10;
            atts.setKeyLength(oldValue);
            failMsg += verifyNoEvent();

            atts.addPropertyChangeListener(this);
            int newValue = 20;
            atts.setKeyLength(newValue);
            atts.removePropertyChangeListener(this);

            // Verify event
            int value = atts.getKeyLength();
            failMsg += verifyPropertyChange(atts, "keyLength", Integer.valueOf(oldValue), Integer.valueOf(newValue), Integer.valueOf(value));
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addPropertyChangeListener() followed by DataQueueAttributes::setFIFO().
     <p>Result:  Verify property change event is fired.
     **/
    public void Var059()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            boolean oldValue = true;
            atts.setSaveSenderInfo(oldValue);
            failMsg += verifyNoEvent();

            atts.addPropertyChangeListener(this);
            boolean newValue = false;
            atts.setSaveSenderInfo(newValue);
            atts.removePropertyChangeListener(this);

            // Verify event
            boolean value = atts.isSaveSenderInfo();
            failMsg += verifyPropertyChange(atts, "saveSenderInfo", Boolean.valueOf(oldValue), Boolean.valueOf(newValue), Boolean.valueOf(value));
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addPropertyChangeListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var060()
    {
        try
        {
            DataQueueAttributes atts = new DataQueueAttributes();
            atts.addPropertyChangeListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::removePropertyChangeListener().
     <p>Result:  Verify no property change event is fired.
     **/
    public void Var061()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            String oldValue = "xxxx";
            atts.setDescription(oldValue);
            failMsg += verifyNoEvent();

            atts.addPropertyChangeListener(this);
            String newValue = "yyyyyy";
            atts.setDescription(newValue);
            atts.removePropertyChangeListener(this);

            // Verify event
            String value = atts.getDescription();
            failMsg += verifyPropertyChange(atts, "description", oldValue, newValue, value);

            resetValues();
            String newerValue = "zzzzzz";
            atts.setDescription(newerValue);

            failMsg += verifyNoEvent();
            value = atts.getDescription();
            if (value != newerValue) failMsg += "Verify error after remove: " + value + "\n";
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::removePropertyChangeListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var062()
    {
        try
        {
            DataQueueAttributes atts = new DataQueueAttributes();
            atts.removePropertyChangeListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by DataQueueAttributes::setAuthority().
     <p>Result:  Verify vetoable change event is fired.
     **/
    public void Var063()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            String oldValue = "*ALL";
            atts.setAuthority(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            String newValue = "*USE";
            atts.setAuthority(newValue);
            atts.removeVetoableChangeListener(this);

            // Verify event
            String value = atts.getAuthority();
            failMsg += verifyVetoableChange(atts, "authority", oldValue, newValue, value);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by a vetoed DataQueueAttributes::setAuthority().
     <p>Result:  Verify a PropertyVetoException is thrown and vetoable change event is fired.
     **/
    public void Var064()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            String oldValue = "*ALL";
            atts.setAuthority(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            veto = true;
            String newValue = "*USE";
            try
            {
                atts.setAuthority(newValue);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                atts.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                String value = atts.getAuthority();
                failMsg += verifyVetoedChange(event, atts, "authority", oldValue, newValue, value);
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by DataQueueAttributes::setDescription().
     <p>Result:  Verify vetoable change event is fired.
     **/
    public void Var065()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            String oldValue = "xxxx";
            atts.setDescription(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            String newValue = "yyyyyy";
            atts.setDescription(newValue);
            atts.removeVetoableChangeListener(this);

            // Verify event
            String value = atts.getDescription();
            failMsg += verifyVetoableChange(atts, "description", oldValue, newValue, value);
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by a vetoed DataQueueAttributes::setDescription().
     <p>Result:  Verify a PropertyVetoException is thrown and vetoable change event is fired.
     **/
    public void Var066()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            String oldValue = "xxxx";
            atts.setDescription(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            veto = true;
            String newValue = "yyyyyy";
            try
            {
                atts.setDescription(newValue);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                atts.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                String value = atts.getDescription();
                failMsg += verifyVetoedChange(event, atts, "description", oldValue, newValue, value);
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by DataQueueAttributes::setEntryLength().
     <p>Result:  Verify vetoable change event is fired.
     **/
    public void Var067()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            int oldValue = 10;
            atts.setEntryLength(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            int newValue = 20;
            atts.setEntryLength(newValue);
            atts.removeVetoableChangeListener(this);

            // Verify event
            int value = atts.getEntryLength();
            failMsg += verifyVetoableChange(atts, "entryLength", Integer.valueOf(oldValue), Integer.valueOf(newValue), Integer.valueOf(value));
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by a vetoed DataQueueAttributes::setEntryLength().
     <p>Result:  Verify a PropertyVetoException is thrown and vetoable change event is fired.
     **/
    public void Var068()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            int oldValue = 10;
            atts.setEntryLength(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            veto = true;
            int newValue = 20;
            try
            {
                atts.setEntryLength(newValue);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                atts.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                int value = atts.getEntryLength();
                failMsg += verifyVetoedChange(event, atts, "entryLength", Integer.valueOf(oldValue), Integer.valueOf(newValue), Integer.valueOf(value));
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by DataQueueAttributes::setFIFO().
     <p>Result:  Verify vetoable change event is fired.
     **/
    public void Var069()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            boolean oldValue = true;
            atts.setFIFO(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            boolean newValue = false;
            atts.setFIFO(newValue);
            atts.removeVetoableChangeListener(this);

            // Verify event
            boolean value = atts.isFIFO();
            failMsg += verifyVetoableChange(atts, "FIFO", Boolean.valueOf(oldValue), Boolean.valueOf(newValue), Boolean.valueOf(value));
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by a vetoed DataQueueAttributes::setFIFO().
     <p>Result:  Verify a PropertyVetoException is thrown and vetoable change event is fired.
     **/
    public void Var070()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            boolean oldValue = true;
            atts.setFIFO(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            veto = true;
            boolean newValue = false;
            try
            {
                atts.setFIFO(newValue);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                atts.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                boolean value = atts.isFIFO();
                failMsg += verifyVetoedChange(event, atts, "FIFO", Boolean.valueOf(oldValue), Boolean.valueOf(newValue), Boolean.valueOf(value));
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by DataQueueAttributes::setForceToAuxiliaryStorage().
     <p>Result:  Verify vetoable change event is fired.
     **/
    public void Var071()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            boolean oldValue = true;
            atts.setForceToAuxiliaryStorage(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            boolean newValue = false;
            atts.setForceToAuxiliaryStorage(newValue);
            atts.removeVetoableChangeListener(this);

            // Verify event
            boolean value = atts.isForceToAuxiliaryStorage();
            failMsg += verifyVetoableChange(atts, "forceToAuxiliaryStorage", Boolean.valueOf(oldValue), Boolean.valueOf(newValue), Boolean.valueOf(value));
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by a vetoed DataQueueAttributes::setForceToAuxiliaryStorage().
     <p>Result:  Verify a PropertyVetoException is thrown and vetoable change event is fired.
     **/
    public void Var072()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            boolean oldValue = true;
            atts.setForceToAuxiliaryStorage(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            veto = true;
            boolean newValue = false;
            try
            {
                atts.setForceToAuxiliaryStorage(newValue);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                atts.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                boolean value = atts.isForceToAuxiliaryStorage();
                failMsg += verifyVetoedChange(event, atts, "forceToAuxiliaryStorage", Boolean.valueOf(oldValue), Boolean.valueOf(newValue), Boolean.valueOf(value));
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by DataQueueAttributes::setKeyLength().
     <p>Result:  Verify vetoable change event is fired.
     **/
    public void Var073()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            int oldValue = 10;
            atts.setKeyLength(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            int newValue = 20;
            atts.setKeyLength(newValue);
            atts.removeVetoableChangeListener(this);

            // Verify event
            int value = atts.getKeyLength();
            failMsg += verifyVetoableChange(atts, "keyLength", Integer.valueOf(oldValue), Integer.valueOf(newValue), Integer.valueOf(value));
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by a vetoed DataQueueAttributes::setKeyLength().
     <p>Result:  Verify a PropertyVetoException is thrown and vetoable change event is fired.
     **/
    public void Var074()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            int oldValue = 10;
            atts.setKeyLength(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            veto = true;
            int newValue = 20;
            try
            {
                atts.setKeyLength(newValue);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                atts.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                int value = atts.getKeyLength();
                failMsg += verifyVetoedChange(event, atts, "keyLength", Integer.valueOf(oldValue), Integer.valueOf(newValue), Integer.valueOf(value));
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by DataQueueAttributes::setSaveSenderInfo().
     <p>Result:  Verify vetoable change event is fired.
     **/
    public void Var075()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            boolean oldValue = true;
            atts.setSaveSenderInfo(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            boolean newValue = false;
            atts.setSaveSenderInfo(newValue);
            atts.removeVetoableChangeListener(this);

            // Verify event
            boolean value = atts.isSaveSenderInfo();
            failMsg += verifyVetoableChange(atts, "saveSenderInfo", Boolean.valueOf(oldValue), Boolean.valueOf(newValue), Boolean.valueOf(value));
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() followed by a vetoed DataQueueAttributes::setSaveSenderInfo().
     <p>Result:  Verify a PropertyVetoException is thrown and vetoable change event is fired.
     **/
    public void Var076()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            boolean oldValue = true;
            atts.setSaveSenderInfo(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            veto = true;
            boolean newValue = false;
            try
            {
                atts.setSaveSenderInfo(newValue);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                atts.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                boolean value = atts.isSaveSenderInfo();
                failMsg += verifyVetoedChange(event, atts, "saveSenderInfo", Boolean.valueOf(oldValue), Boolean.valueOf(newValue), Boolean.valueOf(value));
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::addVetoableChangeListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var077()
    {
        try
        {
            DataQueueAttributes atts = new DataQueueAttributes();
            atts.addVetoableChangeListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::removeVetoableChangeListener().
     <p>Result:  Verify no vetoable change event is fired.
     **/
    public void Var078()
    {
        try
        {
            String failMsg = "";
            resetValues();

            DataQueueAttributes atts = new DataQueueAttributes();
            String oldValue = "xxxx";
            atts.setDescription(oldValue);
            failMsg += verifyNoEvent();

            atts.addVetoableChangeListener(this);
            veto = true;
            String newValue = "yyyyyy";
            try
            {
                atts.setDescription(newValue);
                failMsg += "Property veto exception not thrown\n";
            }
            catch (PropertyVetoException e)
            {
                atts.removeVetoableChangeListener(this);

                // Verify event
                PropertyChangeEvent event = e.getPropertyChangeEvent();
                String value = atts.getDescription();
                failMsg += verifyVetoedChange(event, atts, "description", oldValue, newValue, value);

                resetValues();
                String newerValue = "zzzzzz";
                atts.setDescription(newerValue);
                failMsg += verifyNoEvent();
                value = atts.getDescription();
                if (value != newerValue) failMsg += "Verify error after remove: " + value + "\n";
            }
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     <p>Test:  Call DataQueueAttributes::removeVetoableChangeListener() passing a null for the listener.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var079()
    {
        try
        {
            DataQueueAttributes atts = new DataQueueAttributes();
            atts.removeVetoableChangeListener(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "listener");
        }
    }

    /**
     <p>Test:  Serialize DataQueueAttributes object.
     <p>Result:  Verify object deserializes and is usable.
     **/
    public void Var080()
    {
        try
        {
            String failMsg = "";

            DataQueueAttributes atts2 = new DataQueueAttributes();
            atts2.setAuthority("*ALL");
            atts2.setDescription("xxxx");
            atts2.setEntryLength(10);
            atts2.setFIFO(false);
            atts2.setForceToAuxiliaryStorage(true);
            atts2.setKeyLength(20);
            atts2.setSaveSenderInfo(true);
            atts2.addPropertyChangeListener(this);

            // Serialize atts to a file.
            FileOutputStream fos = new FileOutputStream("dqatts.ser");
            ObjectOutput oo =  new ObjectOutputStream(fos);
            oo.writeObject(atts2);
            oo.flush();
            oo.close(); 
            try
            {
                // Deserialize atts from a file.
                FileInputStream fin = new FileInputStream("dqatts.ser");
                ObjectInputStream ois = new ObjectInputStream(fin);
                DataQueueAttributes atts = (DataQueueAttributes)ois.readObject();

                String auth = atts.getAuthority();
                String desc = atts.getDescription();
                int len = atts.getEntryLength();
                boolean fifo = atts.isFIFO();
                boolean force = atts.isForceToAuxiliaryStorage();
                int keyLength = atts.getKeyLength();
                boolean save = atts.isSaveSenderInfo();

                if (!auth.equals("*ALL")) failMsg += "Authority changed: " + auth + "\n";
                if (!desc.equals("xxxx")) failMsg += "Description changed: " + desc + "\n";
                if (len != 10) failMsg += "EntryLength changed: " + len + "\n";
                if (fifo != false) failMsg += "FIFO changed: " + fifo + "\n";
                if (force != true) failMsg += "ForceToAuxiliaryStorage changed: " + force + "\n";
                if (keyLength != 20) failMsg += "KeyLength changed: " + keyLength + "\n";
                if (save != true) failMsg += "SaveSenderInfo changed: " + save + "\n";
                ois.close(); 
                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                File fd = new File("dqatts.ser");
                fd.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
