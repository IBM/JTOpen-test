///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SystemPoolUnattendedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.SystemStatus;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SystemPool;

import test.Testcase;

/**
 The SystemPoolUnattendedTestcase class tests the methods of SystemPool.
 <p>This tests the following SystemPool methods:
 <ul>
 <li>constructor
 <li>addPropertyChangeListener()
 <li>addVatoableChangeListener()
 <li>getActiveToIneligible()
 <li>getActiveToWait()
 <li>getDatabaseFaults()
 <li>getDatabasePages()
 <li>getDescription()
 <li>getMaximumActiveThreads()
 <li>getNonDatabaseFaults()
 <li>getNonDatabasePages()
 <li>getPagingOption()
 <li>getPoolName()
 <li>getPoolsize()
 <li>getPoolIdentifier()
 <li>getReservedSize()
 <li>getSubsystemName()
 <li>loadInformation()
 <li>getSystem()
 <li>getWaitToIneeligible()
 <li>removePropertyChangeListener()
 <li>removeVatoableChangeListener()
 <li>setFaults()
 <li>setMinAndMaxPoolSize()
 <li>setMaximumFaults()
 <li>setMaximumPoolSize()
 <li>setMessageLogging()
 <li>setMinimumFaults()
 <li>setMinimumPoolSize()
 <li>setPagingOption()
 <li>setPerThreadsFaults()
 <li>setPoolActivityLevel()
 <li>setPoolName()
 <li>setPoolSize()
 <li>setPriority()
 <li>setSystem()
 <li>toString()
 </ul>
 **/
public class SystemPoolUnattendedTestcase extends Testcase
{
    private String subsystemName = "TSTSBS1";

    protected void setup() throws Exception
    {
        //cmdRun("ENDSBS SBS(" + subsystemName + ") OPTION(*IMMED)");
        //cmdRun("DLTSBSD SBSD(QGPL/" + subsystemName + ")");

        //cmdRun("ENDSBS SBS(zwxsbs2) OPTION(*IMMED)");
        //cmdRun("DLTSBSD SBSD(QGPL/zwxsbs2)");

        cmdRun("CHGSHRPOOL POOL(*SHRPOOL6) SIZE(300) ACTLVL(6)");
        cmdRun("CRTSBSD SBSD(QGPL/" + subsystemName + ") POOLS((7 *SHRPOOL6))");
        cmdRun("STRSBS SBSD(QGPL/" + subsystemName + ")");

        cmdRun("CHGSHRPOOL POOL(*SHRPOOL7) SIZE(300) ACTLVL(6)");
        cmdRun("CRTSBSD SBSD(QGPL/zwxsbs2) POOLS((7 *SHRPOOL7))");
        cmdRun("STRSBS SBSD(QGPL/zwxsbs2)");
    }

    protected void cleanup() throws Exception
    {
        cmdRun("ENDSBS SBS(" + subsystemName + ") OPTION(*IMMED)");
        cmdRun("DLTSBSD SBSD(QGPL/" + subsystemName + ")");

        cmdRun("ENDSBS SBS(zwxsbs2) OPTION(*IMMED)");
        cmdRun("DLTSBSD SBSD(QGPL/zwxsbs2)");
    }

    /**
     Method tested:SystemPool()
     - Ensure that the default constructor runs well.
     **/
    public void Var001()
    {
        try
        {
            SystemPool s = new SystemPool();
            succeeded("s="+s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:SystemPool(AS400 as400,String poolName)
     - Ensure that the constructor runs well.
     **/
    public void Var002()
    {

        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            succeeded("s="+s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:SystemPool(AS400 as400,String poolName)
     - Ensure that the when the parameter poolname is an invalid parameter,
     system will use a default poolname as its parameter to construct itself.
     **/
    public void Var003()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"QQ");
            s.getActiveToWait(); // cause a connection
            failed("Exception didn't occur.");

        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:SystemPool(AS400 as400,String poolName)
     - Ensure that the NullPointerException is thrown if the name is null
     in constructor with arguments.
     **/
    public void Var004()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_, (String) null);
            failed("Exception didn't occur."+s);
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
     Method tested:SystemPool(AS400 as400,String poolName)
     - Ensure that the  NullPointerException is thrown if the as400 is null
     in constructor with arguments.
     **/
    public void Var005()
    {
        try
        {
            SystemPool s = new SystemPool(null,"*SHRPOOL6");
            failed("Exception didn't occur."+s);
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
    public void Var006()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.addPropertyChangeListener (null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
     Method tested:addPropertyChangeListener()
     - Ensure that an event is received when the system property is changed.
     **/
    public void Var007()
    {
        try
        {
            SystemPool s = new SystemPool();
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            s.addPropertyChangeListener (listener);
            AS400 as400 = new AS400();
            s.setSystem(as400);
            assertCondition ((listener.lastEvent_.getPropertyName().equals("system"))
                             && (listener.lastEvent_.getOldValue()==null)
                             && (listener.lastEvent_.getNewValue().equals(as400)));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Listens for vetoable change events.
     **/
    private class VetoableChangeListener_ implements VetoableChangeListener
    {
        private PropertyChangeEvent lastEvent_ = null;
        public void vetoableChange (PropertyChangeEvent event)
        {
            lastEvent_ = event;
        }
    }

    /**
     Method tested:addVetoableChangeListener()
     - Ensure that adding a null listener causes an exception.
     **/
    public void Var008()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
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
    public void Var009()
    {
        try
        {
            SystemPool s = new SystemPool();
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            s.addVetoableChangeListener (listener);
            s.setSystem(systemObject_);
            assertCondition (listener.lastEvent_ != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getActiveToIneligible()
     - Ensure that getActiveToIneligible() returns correct result.
     **/
    public void Var010()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getActiveToIneligible() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getActiveToWait()
     - Ensure that getActiveToWait() returns correct result.
     **/
    public void Var011()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getActiveToWait() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getDatabaseFaults()
     - Ensure that getDatabaseFaults() returns correct result.
     **/
    public void Var012()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getDatabaseFaults() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getDatabasePages()
     - Ensure that getDatabasePages() returns correct result.
     **/
    public void Var013()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getDatabasePages() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getDescription()
     - Ensure that getDescription() returns correct result.
     **/
    public void Var014()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getDescription() != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getMaximumActiveThreads()
     - Ensure that getMaximumActiveThreads() returns correct result.
     **/
    public void Var015()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getActivityLevel() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getNonDatabaseFaults()
     - Ensure that getNonDatabaseFaults() returns correct result.
     **/
    public void Var016()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getNonDatabaseFaults() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getNonDatabasePages()
     - Ensure that getNonDatabasePages() returns correct result.
     **/
    public void Var017()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getNonDatabasePages() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getPagingOption()
     - Ensure that getPagingOption() returns correct result.
     **/
    public void Var018()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition((s.getPagingOption().trim().equals("*FIXED"))||
                            (s.getPagingOption().trim().equals("*CALC"))||
                            (s.getPagingOption().trim().equals("USRDFN")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getPagingOption(), and setPagingOption()
     - Ensure that getPagingOption() returns that is set by setPagingOption().
     **/
    public void Var019()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            String str = s.getPagingOption();
            String str1 = "*CALC";
            s.setPagingOption(str1);
            String ret = s.getPagingOption().trim();
            if (ret.equals(str1))
            {
                succeeded();
            }
            else
            {
                failed("Paging options not equal: '"+ret+"' != '"+str1+"'");
            }
            s.setPagingOption(str);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getPoolIdentifier()
     - Ensure that getPoolIdentifier() returns correct result.
     **/
    public void Var020()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition(s.getIdentifier()>=0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
     Method tested: getPoolName()
     - Ensure that getPoolName() returns correct result.
     **/
    public void Var021()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition(s.getName().trim().equals("*SHRPOOL6"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getPoolSize()
     - Ensure that getPoolSize() returns correct result.
     **/
    public void Var022()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getSize() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getPoolSize(), and setPoolSize()
     - Ensure that getPoolSize() returns that is set by setPoolSize().
     **/
    public void Var023()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            // First, make sure the system doesn't auto-adjust the value for us.
            String opt = s.getPagingOption();
            s.setPagingOption("*FIXED");
            int oldSize = s.getSize();
            int newSize = oldSize - 16;
            s.setSize(newSize);
            int ret = s.getSize();
            if (ret == newSize)
                succeeded();
            else
                failed("Pool sizes not equal: "+ret+" != "+newSize);
            s.setSize(oldSize);
            s.setPagingOption(opt);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getReservedSize()
     - Ensure that getReservedSize() returns correct result.
     **/
    public void Var024()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getReservedSize() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
     Method tested: getSubsystemName()
     - Ensure that getSubsystemName() returns correct subsystem name.
     **/
    public void Var025()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getSubsystemName().trim() instanceof String/*.equals("TSTSBS1")*/);
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
    public void Var026()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getSystem().equals(systemObject_));
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
    public void Var027()
    {
        try
        {
            SystemPool s = new SystemPool();
            AS400 as400 = new AS400();
            s.setSystem(as400);
            assertCondition (s.getSystem().equals(as400));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: getWaitToIneligible()
     - Ensure that getWaitToIneligible() returns correct result.
     **/
    public void Var028()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.getWaitToIneligible() >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:loadInformation()
     - Ensure that the NullPointerException is thrown if the system name is null.
     **/
    public void Var029()
    {
        try
        {
            SystemPool s = new SystemPool(null,"*SHRPOOL6");
            s.loadInformation();
            failed("Excepted exception didn't occur");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
     Method tested:loadInformation()
     - Ensure that the NullPointerException is thrown if the pool name is null.
     **/
    public void Var030()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_, (String) null);
            s.loadInformation();
            failed("Excepted exception didn't occur");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
     Method tested:removePropertyChangeListener()
     - Ensure that the NullPointerException is thrown if the listener is null.
     **/
    public void Var031()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            PropertyChangeListener_ listener = null;
            s.removePropertyChangeListener (listener);
            failed("Excepted exception didn't occur");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
     Method tested:removePropertyChangeListener()
     - Ensure that events are no longer received.
     **/
    public void Var032()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            s.addPropertyChangeListener (listener);
            s.removePropertyChangeListener (listener);
            assertCondition (listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:removeVetoableChangeListener()
     - Ensure that the NullPointerException is thrown if the listener is null.
     **/
    public void Var033()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            VetoableChangeListener_ listener = null;
            s.removeVetoableChangeListener (listener);
            failed("Excepted exception didn't occur");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
     Method tested:removeVetoableChangeListener()
     - Ensure that events are no longer received.
     **/
    public void Var034()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            s.addVetoableChangeListener (listener);
            s.removeVetoableChangeListener (listener);
            assertCondition (listener.lastEvent_ == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: seFaults()
     - Ensure that the values can be set when the sum of minimum faults and per-thread
     faults is less than that of maximum.
     **/
    public void Var035()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setFaults(12,10,22);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: seFaults()
     - Ensure that the values can be set when the sum of minimum faults and per-thread
     faults is less than that of maximum.
     **/
    public void Var036()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setFaults(5,10,100);
            succeeded();
        }
        catch (Exception e)
        {
            failed("No exception occurred.");
        }
    }

    /**
     Method tested: seFaults()
     - Ensure that the values can be set when the sum of minimum faults and per-thread
     faults is greater than that of maximum.
     **/
    public void Var037()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setFaults(5,10,10);
            failed("No exception occurred.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception") &&
                e.getMessage().equalsIgnoreCase("CPF113A Sum of MINFAULT and JOBFAULT parameters exceeds MAXFAULT parameter."))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setMaximumFaults()
     - Ensure that the maximum faults can be set.
     **/
    public void Var038()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setMaximumFaults(32000);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setMaximumFaults()
     - Ensure that the AS400Exception will be thrown for specifying an invalid value.
     **/
    public void Var039()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setMaximumFaults(1);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setMaximumPoolSize()
     - Ensure that the maximum pool size can be set.
     **/
    @SuppressWarnings("deprecation")
    public void Var040()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setMaximumPoolSize(SystemPool.CALCULATE);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setMaximumPoolSize()
     - Ensure that the AS400Exception will be thrown for specifying an invalid value.
     **/
    public void Var041()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setMaximumPoolSize(-3);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setMessageLogging()
     - Ensure that the message logging can be set correctly.
     **/
    public void Var042()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setMessageLogging(true);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: setMessageLogging()
     - Ensure that the message logging can be set correctly.
     **/
    public void Var043()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setMessageLogging(false);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: setMinimumFaults()
     - Ensure that the minimum faults can be set.
     **/
    public void Var044()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setMinimumFaults(1);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setMinimumFaults()
     - Ensure that the ExtendedIllegalArgumentException will be thrown for a specified invalid value.
     **/
    public void Var045()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setMinimumFaults(-4);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setMinAndMaxPoolSize()
     - Ensure that the minimum pool and maximum size can be set.
     **/
    public void Var046()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setMinAndMaxPoolSize(12,100);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setMinAndMaxPoolSize()
     - Ensure that the minimum pool and maximum size can not be set when
     the value of minimum is greater than that of maximum.
     **/
    public void Var047()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setMinAndMaxPoolSize(56,10);
            failed();
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setMinimumPoolSize()
     - Ensure that the minimum pool size can be set.
     **/
    public void Var048()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setMinimumPoolSize(2);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setMinimumPoolSize()
     - Ensure that the ExtendedIllegalArgumentException will be thrown for a specified invalid value.
     **/
    public void Var049()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setMinimumPoolSize(-4);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPagingOption()
     - Ensure that the AS400Exception will be thrown for a specified invalid value.
     **/
    public void Var050()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setPagingOption("A&U**##GF");
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPagingOption()
     - Ensure that the NullPointerException will be thrown for specifying null.
     **/
    public void Var051()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setPagingOption(null);
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
     Method tested: setPerThreadFaults()
     - Ensure that the every thread faults can be set.
     **/
    public void Var052()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setPerThreadFaults(3);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPerThreadFaults()
     - Ensure that the ExtendedIllegalArgumentException will be thrown for a specified invalid value.
     **/
    public void Var053()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setPerThreadFaults(-3);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPoolActivityLevel()
     - Ensure that the pool activity level can be set.
     **/
    public void Var054()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setActivityLevel(3);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPoolActivityLevel()
     - Ensure that the AS400Exception will be thrown for a specified invalid value.
     **/
    public void Var055()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setActivityLevel(0);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPoolName() and loadInformation()
     - Ensure that getPoolName() returns which is set by setPoolName().
     **/
    public void Var056()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setName("*SHRPOOL7");
            s.loadInformation();
            assertCondition (s.getName().trim().equals("*SHRPOOL7"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPoolSize()
     - Ensure that the AS400Exception will be thrown for specifying an invalid value.
     **/
    public void Var057()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setSize(1);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPriority()
     - Ensure that the priority can be set.
     **/
    public void Var058()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setPriority(-2);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPriority()
     - Ensure that the priority can be set.
     **/
    public void Var059()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setPriority(2);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPriority()
     - Ensure that the AS400Exception will be thrown for a specified invalid value.
     **/
    public void Var060()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.setPriority(0);
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setSystem()
     - Ensure that the system can be set.
     **/
    public void Var061()
    {
        try
        {
            SystemPool s = new SystemPool();
            AS400 as400 = new AS400();
            s.setSystem(as400);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested: setSystem()
     - Ensure that the NullsPointerException is thrown for setting system null after creating a SystemPool instance.
     **/
    public void Var062()
    {
        try
        {
            SystemPool s = new SystemPool();
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
    public void Var063()
    {
        systemObject_.disconnectService(AS400.COMMAND);
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            s.getPagingOption(); // cause a connection
            AS400 as400 = new AS400();
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

    /**
     Method tested:toString()
     - Ensure that toString() returns a String object.
     **/
    public void Var064()
    {
        try
        {
            SystemPool s = new SystemPool(systemObject_,"*SHRPOOL6");
            assertCondition (s.toString() instanceof String);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
