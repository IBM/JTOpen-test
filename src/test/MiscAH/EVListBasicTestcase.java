///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  EVListBasicTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.EnvironmentVariable;
import com.ibm.as400.access.EnvironmentVariableList;
import com.ibm.as400.access.SocketProperties;

import test.EVTest;
import test.Testcase;
import test.EVTest.PropertyChangeListener_;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Vector;

/**
 Testcase EVListBasicTestcase.  This tests the EnvironmentVariableList class.
 **/
public class EVListBasicTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "EVListBasicTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.EVTest.main(newArgs); 
   }
    public static final String EV_PREFIX_  = "TBX";
    private static final int LOGIN_TIMEOUT = 2000; /* 4 seconds */

    private Properties evListProperties_;

    /**
     Setup: store the existing environment variables so we can restore them later.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
        evListProperties_ = ev.getProperties();
    }

    /**
     Creates a list of environment variables on the system.
     @param  count  The number of environment variables to create.
     @exception  Exception  If an exception occurs.
     **/
    private void createList(int count) throws Exception
    {
        // System.out.println("Creating list of  " + count + " environment variables.");

        // Delete all existing environment variables.
        EnvironmentVariableList evl = new EnvironmentVariableList(pwrSys_);
        Enumeration enumeration = evl.getEnvironmentVariables();
        while (enumeration.hasMoreElements())
        {
            EnvironmentVariable ev = (EnvironmentVariable)enumeration.nextElement();
            // System.out.println("Deleting environment variable " + ev.getName() + ".");
            ev.delete();
        }

        // Create new environment variables.
        for (int i = 0; i < count; ++i)
        {
            String evName = EV_PREFIX_ + i;
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, evName);
            // System.out.println("Creating environment variable " + ev.getName() + ".");
            ev.setValue("Testing EnvironmentVariableList class " + i);
        }
    }

    /**
     Tests an enumeration.
     @param enum     The enumeration.
     @param expected The expected number of elements.
     @return         true if the enumeration is valid, false otherwise.
     @exception  Exception  If an exception occurs.
     **/
    private boolean testEnumeration(Enumeration enumeration, int expected) throws Exception
    {
        Vector v = new Vector(expected);
        for (int i = 0; i < expected; ++i)
            v.addElement(EV_PREFIX_ + i);

        boolean success = true;
        int actual = 0;
        while (enumeration.hasMoreElements()) {
            EnvironmentVariable ev = (EnvironmentVariable)enumeration.nextElement();
            String evName = ev.getName();

            if (! v.contains(evName)) {
                System.out.println("EV name mismatch:" + evName);
                success = false;
            }
            else {
                v.removeElement(evName);
            }

            String evValue = ev.getValue();
            int index = Integer.parseInt(evName.substring(EV_PREFIX_.length()));
            if (!evValue.equals("Testing EnvironmentVariableList class " + index)) {
                System.out.println("EV value mismatch:" + evValue + " != " + "Testing EnvironmentVariableList class " + index);
                success = false;
            }

            ++actual;
        }

        if (actual != expected) {
            System.out.println("EV count mismatch:" + actual + " != " + expected);
            success = false;
        }

        if (v.size() != 0) {
            for (int i = 0; i < v.size(); ++i)
                System.out.println("EV name " + v.elementAt(i) + " not found");
            success = false;
        }

        try {
            enumeration.nextElement();
        }
        catch (NoSuchElementException e) {
            return success;
        }

        System.out.println("nextElement() did not throw exception when list was done");
        return false;
    }

    /**
     Tests a properties object.
     @param properties   The properties object.                         
     @param expected     The expected number of elements.
     @return             true if the properties object is valid, false otherwise.
     @exception  Exception  If an exception occurs.
     **/
    private boolean testProperties(Properties properties, int expected) throws Exception
    {
        boolean success = true;
        for (int i = 0; i < expected; ++i) {
            String evName = EV_PREFIX_ + i;
            if (!properties.containsKey(evName)) {
                System.out.println("EV name " + evName + " not found");
                success = false;
            }
            else {
                String evValue = properties.getProperty(evName);
                if (!evValue.equals("Testing EnvironmentVariableList class " + i)) {
                    System.out.println("EV value mismatch:" + evValue + " != " + "Testing EnvironmentVariableList class " + i);
                    success = false;
                }
            }
        }

        if (properties.size() != expected) {
            System.out.println("EV count mismatch:" + properties.size() + " != " + expected);
            success = false;
        }

        return success;
    }

    /**
     Cleanup: restore the system environment variables.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        createList(0); // Force a delete.
        EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
        ev.setProperties(evListProperties_);
    }

    /**
     constructor() with 0 parms - Should work.
     **/
    public void Var001()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList();
            assertCondition (ev.getSystem() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 1 parm - Pass null for system.
     **/
    public void Var002()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList(null);
            failed ("Didn't throw exception but got "+ev);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 1 parms - Pass invalid values.  This should work,
     because the constructor does not check the validity.
     **/
    public void Var003()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            SocketProperties socketProperties = new SocketProperties();
            socketProperties.setLoginTimeout(LOGIN_TIMEOUT); 
            bogus.setSocketProperties(socketProperties ); 
            EnvironmentVariableList ev = new EnvironmentVariableList(bogus);
            assertCondition (ev.getSystem() == bogus);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 1 parms - Pass valid values.
     **/
    public void Var004()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            assertCondition (ev.getSystem() == pwrSys_) ;
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     addPropertyChangeListener() - Pass null.
     **/
    public void Var005()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList();
            ev.addPropertyChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
     addPropertyChangeListener() - Pass a listener.
     **/
    public void Var006()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList();
            EVTest.PropertyChangeListener_ pcl = new EVTest.PropertyChangeListener_();
            ev.addPropertyChangeListener(pcl);
            ev.setSystem(pwrSys_);
            assertCondition (pcl.eventCount_ == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     addPropertyChangeListener() - When the object is deserialized.
     **/
    public void Var007()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            ev.getProperties();
            EnvironmentVariableList ev2 = (EnvironmentVariableList)EVTest.serialize(ev, pwrSysEncryptedPassword_);
            EVTest.PropertyChangeListener_ pcl = new EVTest.PropertyChangeListener_();
            ev2.addPropertyChangeListener(pcl);
            AS400 newSystem = new AS400();
            ev2.setSystem(newSystem);
            assertCondition (pcl.eventCount_ == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getEnvironmentVariables() - When the system has not been set.
     **/
    public void Var008()
    {
        try {
            EnvironmentVariableList evl = new EnvironmentVariableList();
            evl.getEnvironmentVariables();
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }

    /**
     getEnvironmentVariables() - When a bad system has been set.
     **/
    public void Var009()
    {
        try {
            EnvironmentVariableList evl = new EnvironmentVariableList();
            AS400 system = new AS400("bogus", "bogus", "bogus");
            system.setGuiAvailable(false);
            SocketProperties socketProperties = new SocketProperties();
            socketProperties.setLoginTimeout(LOGIN_TIMEOUT); 
            system.setSocketProperties(socketProperties ); 

            evl.setSystem(system);
            evl.getEnvironmentVariables();
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            succeeded();
        }
    }

    /**
     getEnvironmentVariables() - When there are no environment variables.
     **/
    public void Var010()
    {
        try {
            createList(0);
            EnvironmentVariableList evl = new EnvironmentVariableList(pwrSys_);
            assertCondition(testEnumeration(evl.getEnvironmentVariables(), 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getEnvironmentVariables() - When there is 1 environment variable.
     **/
    public void Var011()
    {
        try {
            createList(1);
            EnvironmentVariableList evl = new EnvironmentVariableList(pwrSys_);
            assertCondition(testEnumeration(evl.getEnvironmentVariables(), 1));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getEnvironmentVariables() - When there are 11 environment variables.
     **/
    public void Var012()
    {
        try {
            createList(11);
            EnvironmentVariableList evl = new EnvironmentVariableList(pwrSys_);
            assertCondition(testEnumeration(evl.getEnvironmentVariables(), 11));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getEnvironmentVariables() - When there are 500 environment variables, which
     should force us to make 2 API calls.
     **/
    public void Var013()
    {
        try {
            createList(500);
            EnvironmentVariableList evl = new EnvironmentVariableList(pwrSys_);
            assertCondition(testEnumeration(evl.getEnvironmentVariables(), 500));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getEnvironmentVariables() - When the object is deserialized.
     **/
    public void Var014()
    {
        try {            
            createList(5);
            EnvironmentVariableList evl1 = new EnvironmentVariableList(pwrSys_);
            evl1.getEnvironmentVariables();
            EnvironmentVariableList evl2 = (EnvironmentVariableList)EVTest.serialize(evl1, pwrSysEncryptedPassword_);
            assertCondition(testEnumeration(evl2.getEnvironmentVariables(), 5));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getProperties() - When the system has not been set.
     **/
    public void Var015()
    {
        try {
            EnvironmentVariableList evl = new EnvironmentVariableList();
            evl.getProperties();
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }

    /**
     getProperties() - When a bad system has been set.
     **/
    public void Var016()
    {
        try {
            EnvironmentVariableList evl = new EnvironmentVariableList();
            AS400 system = new AS400("bogus", "bogus", "bogus");
            system.setGuiAvailable(false);
            SocketProperties socketProperties = new SocketProperties();
            socketProperties.setLoginTimeout(LOGIN_TIMEOUT); 
            system.setSocketProperties(socketProperties ); 
            evl.setSystem(system);
            evl.getProperties();
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            succeeded();
        }
    }

    /**
     getProperties() - When there are no environment variables.
     **/
    public void Var017()
    {
        try {
            createList(0);
            EnvironmentVariableList evl = new EnvironmentVariableList(pwrSys_);
            assertCondition(testProperties(evl.getProperties(), 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getProperties() - When there is 1 environment variable.
     **/
    public void Var018()
    {
        try {
            createList(1);
            EnvironmentVariableList evl = new EnvironmentVariableList(pwrSys_);
            assertCondition(testProperties(evl.getProperties(), 1));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getProperties() - When there are 18 environment variables.
     **/
    public void Var019()
    {
        try {
            createList(18);
            EnvironmentVariableList evl = new EnvironmentVariableList(pwrSys_);
            assertCondition(testProperties(evl.getProperties(), 18));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getProperties() - When the object is deserialized.
     **/
    public void Var020()
    {
        try {            
            createList(7);
            EnvironmentVariableList evl1 = new EnvironmentVariableList(pwrSys_);
            evl1.getProperties();
            EnvironmentVariableList evl2 = (EnvironmentVariableList)EVTest.serialize(evl1, pwrSysEncryptedPassword_);
            assertCondition(testProperties(evl2.getProperties(), 7));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getSystem() - When the system has not been set.
     **/
    public void Var021()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList();
            assertCondition(ev.getSystem() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getSystem() - When the system has been set.
     **/
    public void Var022()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            assertCondition((ev.getSystem().getSystemName().equals(pwrSys_.getSystemName()))
                            && (ev.getSystem().getUserId().equals(pwrSys_.getUserId())));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getSystem() - When the object is deserialized.
     **/
    public void Var023()
    {
        try {            
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            ev.getEnvironmentVariables();
            EnvironmentVariableList ev2 = (EnvironmentVariableList)EVTest.serialize(ev, pwrSysEncryptedPassword_);
            AS400 system = ev2.getSystem();

            assertCondition((ev.getSystem().getSystemName().equals(system.getSystemName()))
                            && (ev.getSystem().getUserId().equals(system.getUserId())));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     removePropertyChangeListener() - Pass null.
     **/
    public void Var024()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList();
            ev.removePropertyChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
     removePropertyChangeListener() - Pass a listener.
     **/
    public void Var025()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList();
            EVTest.PropertyChangeListener_ pcl = new EVTest.PropertyChangeListener_();
            ev.addPropertyChangeListener(pcl);
            ev.removePropertyChangeListener(pcl);
            ev.setSystem(pwrSys_);
            assertCondition (pcl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     removePropertyChangeListener() - An object not previously added.
     **/
    public void Var026()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList();
            EVTest.PropertyChangeListener_ pcl = new EVTest.PropertyChangeListener_();
            ev.removePropertyChangeListener(pcl);
            ev.setSystem(pwrSys_);
            assertCondition (pcl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     setProperties() - When the system has not been set.
     **/
    public void Var027()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList();
            Properties p = new Properties();
            p.put(EV_PREFIX_ + 0, "Testing EnvironmentVariableList class " + 0);
            ev.setProperties(p);
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }

    /**
     setProperties() - When a bad system has been set.
     **/
    public void Var028()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList();
            AS400 system = new AS400("bogus", "bogus", "bogus");
            system.setGuiAvailable(false);
            SocketProperties socketProperties = new SocketProperties();
            socketProperties.setLoginTimeout(LOGIN_TIMEOUT); 
            system.setSocketProperties(socketProperties ); 
            ev.setSystem(system);
            Properties p = new Properties();
            p.put(EV_PREFIX_ + 0, "Testing EnvironmentVariableList class " + 0);
            ev.setProperties(p);
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            succeeded();        // @A1C
        }
    }

    /**
     setProperties() - Pass null for the value.
     **/
    public void Var029()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            ev.setProperties(null);
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
     setProperties() - Set a properties object with 0 environment variables.
     **/
    public void Var030()
    {
        try {
            createList(0);
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            Properties p = new Properties();
            ev.setProperties(p);
            assertCondition(testEnumeration(ev.getEnvironmentVariables(), 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     setProperties() - Set a properties object with 1 environment variable.
     **/
    public void Var031()
    {
        try {
            createList(0);
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            Properties p = new Properties();
            p.put(EV_PREFIX_ + 0, "Testing EnvironmentVariableList class " + 0);
            ev.setProperties(p);
            assertCondition(testEnumeration(ev.getEnvironmentVariables(), 1));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     setProperties() - Set a properties object with 6 environment variables.
     **/
    public void Var032()
    {
        try {
            createList(0);
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            Properties p = new Properties();
            p.put(EV_PREFIX_ + 0, "Testing EnvironmentVariableList class " + 0);
            p.put(EV_PREFIX_ + 1, "Testing EnvironmentVariableList class " + 1);
            p.put(EV_PREFIX_ + 2, "Testing EnvironmentVariableList class " + 2);
            p.put(EV_PREFIX_ + 3, "Testing EnvironmentVariableList class " + 3);
            p.put(EV_PREFIX_ + 4, "Testing EnvironmentVariableList class " + 4);
            p.put(EV_PREFIX_ + 5, "Testing EnvironmentVariableList class " + 5);
            ev.setProperties(p);
            assertCondition(testEnumeration(ev.getEnvironmentVariables(), 6));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     setProperties() - When the object is deserialized.
     **/
    public void Var033()
    {
        try {            
            createList(0);
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            ev.getEnvironmentVariables();
            EnvironmentVariableList ev2 = (EnvironmentVariableList)EVTest.serialize(ev, pwrSysEncryptedPassword_);

            Properties p = new Properties();
            p.put(EV_PREFIX_ + 0, "Testing EnvironmentVariableList class " + 0);
            p.put(EV_PREFIX_ + 1, "Testing EnvironmentVariableList class " + 1);
            p.put(EV_PREFIX_ + 2, "Testing EnvironmentVariableList class " + 2);
            p.put(EV_PREFIX_ + 3, "Testing EnvironmentVariableList class " + 3);
            ev2.setProperties(p);
            assertCondition(testEnumeration(ev.getEnvironmentVariables(), 4));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Pass null for system.
     **/
    public void Var034()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            ev.setSystem(null);
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
     setSystem() - Pass a bogus system.  This should succeed, since validation is not yet done.
     **/
    public void Var035()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            ev.setSystem(new AS400("Bogus", "Bogus", "Bogus"));
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - Pass a valid system. 
     **/
    public void Var036()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList();
            ev.setSystem(pwrSys_);
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     setSystem() - After a connection has been established. 
     **/
    public void Var037()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            ev.getEnvironmentVariables();
            ev.setSystem(new AS400());
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }

    /**
     setSystem() - Should fire property change event.
     **/
    public void Var038()
    {
        try {
            EnvironmentVariableList ev = new EnvironmentVariableList(pwrSys_);
            EVTest.PropertyChangeListener_ pcl = new EVTest.PropertyChangeListener_();
            ev.addPropertyChangeListener(pcl);
            AS400 other = new AS400();
            ev.setSystem(other);
            assertCondition((pcl.eventCount_ == 1) 
                            && (pcl.event_.getPropertyName().equals("system"))
                            && (pcl.event_.getOldValue().equals(pwrSys_))
                            && (pcl.event_.getNewValue().equals(other)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
}
