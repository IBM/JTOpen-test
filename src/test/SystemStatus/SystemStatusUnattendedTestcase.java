///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SystemStatusUnattendedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.SystemStatus;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SystemStatus;

import test.Testcase;

import com.ibm.as400.access.SystemPool;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.lang.Integer;
import java.lang.String;
import java.text.SimpleDateFormat;

/**
 The SystemStatusUnattendedTestcase class tests the methods of SystemStatus.
 <p>This tests the following SystemStatus methods:
 <ul>
 <li>constructor
 <li>addPropertyChangeListener()
 <li>addVatoableChangeListener()
 <li>getBatchJobsEndedWithPrinterOutputWaitingToPrint()
 <li>getBatchJobsEnding()
 <li>getBatchJobsHeldOnJobQueue()
 <li>getBatchJobsHeldWhileRunning()
 <li>getBatchJobsOnUnassignedJobQueue()
 <li>getBatchJobsRunning()
 <li>getBatchJobsWaitingForMessage()
 <li>getBatchJobsWaitingToRunOrAlreadyScheduled()
 <li>getDateAndTimeStatusGathered()
 <li>getCurrentUnprotectedStorageUsed()
 <li>getElapsedTime()
 <li>getJobsInSystem()
 <li>getMaximumUnprotectedStorageUsed()
 <li>getPercentPermanentAddresses()
 <li>getPercentProcessingUnitUsed() 
 <li>getPercentSystemASPUsed()
 <li>getPercentTemporaryAddresses()
 <li>getPoolsNumber()
 <li>getRestrictedStateFlag()
 <li>getSystem()
 <li>getSystemASP()
 <li>getSystemPool()
 <li>getTotalAuxiliaryStorage()
 <li>getUsersCurrentSignedOn()
 <li>getUsersSignedOffWithPrinterOutputWaitingToPrint()
 <li>getUsersSuspendedBySystemRequest()
 <li>getUsersTemporarilySignedOff()
 <li>loadInformation()
 <li>removePropertyChangeListener()
 <li>removeVatoableChangeListener()
 <li>setSystem()
 <li>toString()
 </ul>
 **/
public class SystemStatusUnattendedTestcase extends Testcase
{
    /**
     Method tested:SystemStatus()
     - Ensure that the default constructor runs well.
     **/
    public void Var001()
    {
        try
        {
            SystemStatus s = new SystemStatus();
            succeeded();
        }
        catch (Exception e) 
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:SystemStatus(AS400 as400)
     - Ensure that the constructor with arguments runs well.
     **/
    public void Var002()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:SystemStatus(AS400 as400)
     - Ensure that the  NullPointerException is thrown if the as400 is null
     in constructor with arguments.
     **/
    public void Var003()
    {
        try
        {
            SystemStatus s = new SystemStatus(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Listens for property change events.
     **/
    private class PropertyChangeListener_ implements PropertyChangeListener
    {
        private PropertyChangeEvent lastEvent_ = null;
        public void propertyChange (PropertyChangeEvent event)
        {
            lastEvent_ = event;
        }
    }

    /**
     Method tested:addPropertyChangeListener()
     - Ensure that adding a null listener causes an exception.
     **/
    public void Var004()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            s.addPropertyChangeListener(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "listener"))
                succeeded();
            else
                failed("Incorrect exception info.");
        }
    }

    /**
     Method tested:addPropertyChangeListener()
     - Ensure that an event is received when the system property is changed.
     **/
    public void Var005()
    {
        try
        {
            SystemStatus s = new SystemStatus();
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            s.addPropertyChangeListener(listener);            
            s.setSystem(pwrSys_);
            assertCondition(listener.lastEvent_ != null);
        }
        catch (Exception e)
        {
            if (exceptionIs(e,"NullPointerException"))
                failed();
            else
                failed(e, "Unexpected Exception");
        }
    }

    /**
     Listens for vetoable change events.
     **/
    private class VetoableChangeListener_ implements VetoableChangeListener
    {
        private PropertyChangeEvent lastEvent_ = null;
        public void vetoableChange(PropertyChangeEvent event)
        {
            lastEvent_ = event;
        }
    }

    /**
     Method tested:addVetoableChangeListener()
     - Ensure that adding a null listener causes an exception.
     **/
    public void Var006()
    {
        try 
        {
            SystemStatus s = new SystemStatus(systemObject_);
            s.addVetoableChangeListener (null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:addVetoableChangeListener()
     - Ensure that an event is received when the system property is changed.
     **/
    public void Var007()
    {
        try
        {
            SystemStatus s = new SystemStatus();
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            s.addVetoableChangeListener (listener);
            s.setSystem(pwrSys_);
            assertCondition(listener.lastEvent_ != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getBatchJobsEndedWithPrinterOutputWaitingToPrint() 
     - Ensure that getBatchJobsEndedWithPrinterOutputWaitingToPrint() returns correct result.
     **/
    public void Var008()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getBatchJobsEndedWithPrinterOutputWaitingToPrint() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getBatchJobsEnding()
     - Ensure that getBatchJobsEnding() returns correct result.
     **/
    public void Var009()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getBatchJobsEnding() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getBatchJobsHeldOnJobQueue()
     - Ensure that getBatchJobsHeldOnJobQueue() returns correct result.
     **/
    public void Var010()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getBatchJobsHeldOnJobQueue() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getBatchJobsHeldWhileRunning()
     - Ensure that getBatchJobsHeldWhileRunning() returns correct result.
     **/
    public void Var011()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getBatchJobsHeldWhileRunning() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getBatchJobsOnUnassignedJobQueue()
     - Ensure that getBatchJobsOnUnassignedJobQueue() returns correct result.
     **/
    public void Var012()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getBatchJobsOnUnassignedJobQueue() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getBatchJobsRunning()
     - Ensure that getBatchJobsRunning() returns correct result.
     **/
    public void Var013()
    {
        try
        {
            SystemStatus s1 = new SystemStatus(systemObject_);
            int oldValue = s1.getBatchJobsRunning();
            SystemStatus s = new SystemStatus();
            s.setSystem(systemObject_);
            assertCondition((oldValue>=0)&&(s.getBatchJobsRunning()>=0));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getBatchJobsWaitingForMessage()
     - Ensure that getBatchJobsWaitingForMessage() returns correct result.
     **/
    public void Var014()
    {
        try
        {
            SystemStatus s = new SystemStatus();
            s.setSystem(systemObject_);             
            assertCondition(s.getBatchJobsWaitingForMessage() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getBatchJobsWaitingForMessage()
     - Ensure that getBatchJobsWaitingForMessage() returns correct result.
     **/
    public void Var015()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getBatchJobsWaitingForMessage() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getBatchJobsWaitingToRunOrAlreadyScheduled()
     - Ensure that getBatchJobsWaitingToRunOrAlreadyScheduled() returns correct result.
     **/
    public void Var016()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getBatchJobsWaitingToRunOrAlreadyScheduled() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getDateAndTimeStatusGathered()
     - Ensure that getDateAndTimeStatusGathered() returns correct result.
     **/
    public void Var017()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getDateAndTimeStatusGathered() != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getCurrentUnprotectedStorageUsed()
     - Ensure that getCurrentUnprotectedStorageUsed() returns correct result.
     **/
    public void Var018()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getCurrentUnprotectedStorageUsed() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getElapsedTime()
     - Ensure that getElapsedTime() returns correct result.
     **/
    public void Var019()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            int e = s.getElapsedTime();
            assertCondition( e >= 0);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ClassCastException"))
                failed();
            else
                failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getJobsInSystem()
     - Ensure that getJobsInSystem() returns correct result.
     **/
    public void Var020()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getJobsInSystem() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getMaximumUnprotectedStorageUsed()
     - Ensure that getMaximumUnprotectedStorageUsed() returns correct result.
     **/
    public void Var021()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getMaximumUnprotectedStorageUsed() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getPercentPermanentAddresses()
     - Ensure that getPercentPermanentAddresses() returns correct result.
     **/
    public void Var022()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getPercentPermanentAddresses() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getPercentProcessingUnitUsed()
     - Ensure that getPercentProcessingUnitUsed() returns correct result.
     **/
    public void Var023()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getPercentProcessingUnitUsed() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getPercentSystemASPUsed()
     - Ensure that getPercentSystemASPUsed() returns correct result.
     **/
    public void Var024()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getPercentSystemASPUsed() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getPercentTemporaryAddresses()
     - Ensure that getPercentTemporaryAddresses() returns correct result.
     **/
    public void Var025()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getPercentTemporaryAddresses() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getPoolsNumber()
     - Ensure that getPoolsNumber() returns correct result.
     **/
    public void Var026()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getPoolsNumber() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getRestrictedStateFlag()
     - Ensure that getRestrictedStateFlag() returns correct result.
     **/
    public void Var027()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition((s.getRestrictedStateFlag() == true)||(s.getRestrictedStateFlag() == false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getSystem()
     - Ensure that getSystem() returns correct system.
     **/
    public void Var028()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getSystem().equals(systemObject_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getSystem(), and setSystem()
     - Ensure that getSystem() returns which is set by setSystem().
     **/
    public void Var029()
    {
        try
        {
            SystemStatus s = new SystemStatus();            
            s.setSystem(pwrSys_);
            assertCondition(s.getSystem().equals(pwrSys_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getSystemASP()
     - Ensure that getSystemASP() returns correct result.
     **/
    public void Var030()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getSystemASP() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getSystemPools()
     - Ensure that getSystemPools() returns correct system.
     **/
    public void Var031()
    {
        try
        {
            int j;                            
            SystemStatus s = new SystemStatus(systemObject_);                         
            Enumeration en = (Enumeration)s.getSystemPools();            
            for(j=0; en.hasMoreElements(); j++)
            {           
                SystemPool element = (SystemPool) en.nextElement();
                int size = element.getSize();
                System.out.print(size + ",");
            }
            if (j>0) System.out.println();
            assertCondition(j > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getTotalAuxiliaryStorage()
     - Ensure that getTotalAuxiliaryStorage() returns correct result.
     **/
    public void Var032()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getTotalAuxiliaryStorage() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getUsersCurrentSignedOn()
     - Ensure that getUsersCurrentSignedOn() returns correct result.
     **/
    public void Var033()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getUsersCurrentSignedOn() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getUsersSignedOffWithPrinterOutputWaitingToPrint()
     - Ensure that getUsersSignedOffWithPrinterOutputWaitingToPrint() returns correct result.
     **/
    public void Var034()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getUsersSignedOffWithPrinterOutputWaitingToPrint() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getUsersSuspendedBySystemRequest()
     - Ensure that getUsersSuspendedBySystemRequest() returns correct result.
     **/
    public void Var035()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getUsersSuspendedBySystemRequest() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getUsersTemporarilySignedOff()
     - Ensure that getUsersTemporarilySignedOff() returns correct result.
     **/
    public void Var036()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            assertCondition(s.getUsersTemporarilySignedOff() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:removePropertyChangeListener()
     - Ensure that removing a null listener causes an exception.
     **/
    public void Var037()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            s.removePropertyChangeListener(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:removePropertyChangeListener()
     - Ensure that events are no longer received.
     **/
    public void Var038()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            s.addPropertyChangeListener(listener);
            s.removePropertyChangeListener(listener);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:removeVetoableChangeListener()
     - Ensure that removing a null listener causes an exception.
     **/
    public void Var039()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            s.removeVetoableChangeListener (null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:removeVetoableChangeListener()
     - Ensure that events are no longer received.
     **/
    public void Var040()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            s.addVetoableChangeListener (listener);
            s.removeVetoableChangeListener (listener);
            assertCondition(listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: setSystem()
     - Ensure that the system can be set.
     **/
    public void Var041()
    {
        try
        {
            SystemStatus s = new SystemStatus();
            s.setSystem(pwrSys_);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setSystem()
     - Ensure that the NullPointerException is thrown for setting system null after creating a SystemStatus instance.
     **/
    public void Var042()
    {

        try
        {
            SystemStatus s = new SystemStatus();
            s.setSystem(null);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setSystem()
     - Ensure that the System cannot be reset and ExtendedIllegalStateException
     will be thrown after a connection.
     **/
    public void Var043()
    {
        try
        {
            SystemStatus s = new SystemStatus(systemObject_);
            AS400 as400 = new AS400();
            s.getTotalAuxiliaryStorage(); // cause a connection
            s.setSystem(as400);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }
}
