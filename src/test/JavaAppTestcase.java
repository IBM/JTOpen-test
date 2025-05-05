///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JavaAppTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.access.*;
import com.ibm.as400.access.JavaApplicationCall;
import java.io.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.Properties;
import java.net.*;

/**
The JavaAppTestcase class tests the methods of JavaApplicationCall.



<p>This tests the following JavaApplicationCall methods:
<ul>
<li>JavaApplicationCall()
<li>JavaApplicationCall( AS400 system)
<li>JavaApplicationCall( AS400 system, String application)
<li>JavaApplicationCall( AS400 system,  String application,  String classPath)
<li>addActionCompletedListener(ActionCompletedListener l)
<li>addPropertyChangeListener( PropertyChangeListener listener )
<li>addVetoableChangeListener( VetoableChangeListener listener )
<li>getClassPath()
<li>getDefaultPort()
<li>getGarbageCollectionFrequency()
<li>getGarbageCollectionInitialSize()
<li>getGarbageCollectionMaximumSize()
<li>getGarbageCollectionPriority()
<li>getInterpret()
<li>getJavaApplication()
<li>getMessageList()
<li>getOptimization()
<li>getOptions()
<li>getParameters()
<li>getProperties()
<li>getSecurityCheckLevel()
<li>getStandardErrorString()
<li>getStandardOutString()
<li>getSystem()
<li>isFindPort()
<li>removeActionCompletedListener()
<li>removePropertyChangeListener()
<li>removeVetoableChangeListener()
<li>run()
<li>setClassPath( String classPath)
<li>setDefaultPort()
<li>setGarbageCollectionFrequency(int frequency)
<li>setGarbageCollectionInitialSize(int size)
<li>setGarbageCollectionMaximumSize(String size)
<li>setGarbageCollectionPriority(int priority)
<li>setInterpret(String interpret)
<li>setJavaApplication( String application)
<li>setOptimization(String opt)
<li>setOptions(String[][ option)
<li>setParameters( Properties parameters)
<li>sendStandardInString(String s)
<li>setSystem( AS400 system)
<li>setSecurityCheckLevel(String chklvl)
<li>setFindPort(boolean search)


</ul>

@see JavaPgmCallTest
@see VJavaPgmCallTest
@see RegressionTest
**/

public class  JavaAppTestcase extends Testcase
{
    private static final boolean DEBUG = false;
    static final int    variations_ = 113;


    /**
    Constructor tested:JavaApplicationCall()
    - Ensure that this method runs well.
    **/
    public void Var001()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            assertCondition(true, "applicationCall is "+s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Constructor tested:JavaApplicationCall(AS400 system)
    - Ensure that this method runs well.
    **/
    public void Var002()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_);
            assertCondition(true, "applicationCall is "+s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Constructor tested:JavaApplicationCall(AS400 system)
    - Ensure that the NullPointerException be thrown when the value of system is null.
    **/
    public void Var003()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(null);
            failed("The constructor JavaApplicationCall(null) error! "+s );
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
    Constructor tested:JavaApplicationCall(AS400 system,String app)
    - Ensure that this method runs well.
    **/
    public void Var004()
    {
        try
        {
            String app = "Test";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,app);
            assertCondition(true, "applicationCall is "+s);
        }
        catch (Exception e)
        {
            failed(e," The constructor JavaApplicationCall(AS400 system,String app) error!");;
        }
    }

    /**
    Constructor tested:JavaApplicationCall(AS400 system,String app)
    - Ensure that an exception is throw when the value of system is null.
    **/
    public void Var005()
    {
        try
        {
            String app = "Test";
            JavaApplicationCall s = new JavaApplicationCall(null,app);
            failed(" The constructor JavaApplicationCall(null,app) error!"+s);
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
    Constructor tested:JavaApplicationCall(AS400 system,String app)
    - Ensure that an exception is throw when the value of the parameter app is null.
    **/
    public void Var006()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,null);
            failed(" The constructor JavaApplicationCall(systemObject_,null) error!"+s);
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
    Constructor tested:JavaApplicationCall(AS400 system,String app, Stirng classPath)
    - Ensure that this method runs well.
    **/
    public void Var007()
    {
        try
        {
            String app = "Test";
            String classPath ="/home/javatest";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,app,classPath);
            assertCondition(true, "applicationCall is "+s);
            
        }
        catch (Exception e)
        {
            failed(e," The constructor JavaApplicationCall(system,app.classpath) error!");
        }
    }

    /**
    Constructor tested:JavaApplicationCall(AS400 system,String app, Stirng classPath)
    - Ensure that an exception is thrown when the value of the parameter classPath is null runs.
    **/
    public void Var008()
    {
        try
        {
            String app = "Test";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,app,null);
            failed(" The constructor JavaApplicationCall(system,app,null) error!"+s);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
               succeeded();
            else
               failed(e, "The constructor JavaApplicationCall(system,app,null) error!");
        }
    }

    /*****************************************************************
     *
     *  Listens for ActionCompletedEvent events.
     *
    ******************************************************************/
    private class ActionCompletedListener_
        implements ActionCompletedListener
    {
        private ActionCompletedEvent lastEvent_ = null;
        public ActionCompletedEvent getActionEvent()
        {
            return lastEvent_;
        }
        public void actionCompleted (ActionCompletedEvent event)
        {
            lastEvent_ =  event;
        }
    }

    /**
    Method tested:addActionCompletedListener()
    - Ensure that the listener is added and received successfully.
    **/
    public void Var009()
    {
        try
        {
            String app = "TestPrg1";
            String classPath ="/home/javatest";
            ActionCompletedListener_ listener = new ActionCompletedListener_();
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,app,classPath);
            s.addActionCompletedListener(listener);
            if (s.run())
               assertCondition(listener.getActionEvent() != null, " addActionCompletedListener() runs incorrectly!");
            else
               failed("s.run() returned false app="+app+" classpath="+classPath);
        }
        catch (Exception e)
        {
            failed(e," addActionCompletedListener() failed!");
        }
    }

    /**
    Method tested:AddActionCompletedListener()
    - Test that adding a null listener causes an exception.
    **/
    public void Var010()
    {
        try
        {
            String app = "TestPrg1";
            String classPath ="/home/javatest";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,app,classPath);
            s.addActionCompletedListener(null);
            failed("addActionCompletedListener(null) runs incorrectly!");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }


    /*********************************************************
     *
     * Listens for property change events.
     *
    ***********************************************************/
    private class PropertyChangeListener_
        implements PropertyChangeListener
    {
        public PropertyChangeEvent lastEvent_ = null;
        public void propertyChange (PropertyChangeEvent event) { lastEvent_ =  event; }
    }

    /**
    Method tested:addPropertyChangeListener()
    - Test that adding a null listener causes an exception.
    **/
    public void Var011()
    {
        try {
            JavaApplicationCall s = new JavaApplicationCall();
            s.addPropertyChangeListener (null);
            failed ("addPropertyChangeListener (null) runs incorrectly!");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
    Method tested:addPropertyChangeListener()
    - Test that an event is received when the system property is changed.
    **/
    public void Var012 ()
    {
        try
        {
            String app = "TestPrg1";
            String classPath ="/home/javatest";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,app,classPath);
            PropertyChangeListener_ listener = new PropertyChangeListener_ ();
            s.addPropertyChangeListener (listener);
            AS400 system2 = new AS400 ();
            s.setSystem (system2);
            assertCondition ((listener.lastEvent_.getPropertyName ().equals ("as400"))
                && (listener.lastEvent_.getOldValue () == systemObject_)
                && (listener.lastEvent_.getNewValue () == system2)," addPropertyChangeListener() runs incorrectly!");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    Method tested:addPropertyChangeListener()
    - Test that an event is received when the classpath property is changed.
    **/
    public void Var013 ()
    {
        try {
            String app = "TestPrg1";
            String classPath ="/home/javatest11";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,app,classPath);
            PropertyChangeListener_ listener = new PropertyChangeListener_ ();
            s.addPropertyChangeListener (listener);
            s.setClassPath ("/home/javatest");
            assertCondition ((listener.lastEvent_.getPropertyName ().equals ("classPath"))
                && (listener.lastEvent_.getOldValue ().equals ("/home/javatest11"))
                && (listener.lastEvent_.getNewValue ().equals ("/home/javatest"))," addPropertyChangeListener() runs incorrectly!");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
   }

    /***************************************************************
     *
     * Listens for vetoable change events.
     *
    *****************************************************************/
    private class VetoableChangeListener_
        implements VetoableChangeListener
    {
        public PropertyChangeEvent lastEvent_ = null;
        public void vetoableChange (PropertyChangeEvent event) { lastEvent_ = event; }
    }

    /**
    Method tested:addVetoableChangeListener()
    - Test that adding a null listener causes an exception.
    **/
    public void Var014 ()
    {
        try {
            JavaApplicationCall s = new JavaApplicationCall();
            s.addVetoableChangeListener (null);
            failed ("addVetoableChangeListener (null) runs incorrectly!");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
    Method tested:addVetoableChangeListener()
    - Test that an event is received when the system property is changed.
    **/
    public void Var015 ()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_);
            VetoableChangeListener_ listener = new VetoableChangeListener_ ();
            s.addVetoableChangeListener (listener);
            AS400 system2 = new AS400 ();
            s.setSystem (system2);
            assertCondition ((listener.lastEvent_.getPropertyName ().equals ("as400"))
                && (listener.lastEvent_.getOldValue () == systemObject_)
                && (listener.lastEvent_.getNewValue () == system2)," addVetoableChangeListener() runs incorrectly.");
        }
        catch (Exception e) {
            failed (e, "addVetoableChangeListener() error.");
        }
    }

    /**
    addVetoableChangeListener()
    - Test that an event is received when the javaApplication property is changed.
    **/
    public void Var016 ()
    {
        try {
            String app = "Test1";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,app);
            VetoableChangeListener_ listener = new VetoableChangeListener_ ();
            s.addVetoableChangeListener (listener);
            s.setJavaApplication("Test");
            assertCondition ((listener.lastEvent_.getPropertyName ().equals ("application"))
                && (listener.lastEvent_.getOldValue ().equals ("Test1"))
                && (listener.lastEvent_.getNewValue ().equals ("Test")),"addVetoableChangeListener (listener) runs incorrectly!");
        }
        catch (Exception e) {
            failed (e, "addVetoableChangeListener() error!");
        }
    }

    /**
    Method tested:getClassPath()
    - Ensure the method runs well.
    **/
    public void Var017()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"app1","classpath11");
            assertCondition(s.getClassPath().equals("classpath11")," getClassPath() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "getClassPath() gets the incorrect value.");

        }
    }

    /**
    Method tested:getClassPath()
    - Ensure the method runs well.
    **/
    public void Var018()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_);
            s.setClassPath("/home/javatest");
            assertCondition(s.getClassPath().equals("/home/javatest")," setClassPath() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "setClassPath() runs incorrectly.");
        }
    }

    /**
    Method tested:getClassPath()
    - Ensure the method gets the default value of classpath.
    **/
    public void Var019()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            if(s.getClassPath() == "")
                succeeded();
            else
                failed(" The default value is not correct.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getDefaultPort()
    - Ensure the method runs well.
    **/
    public void Var020()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_);
            s.setDefaultPort(2222);
            assertCondition(s.getDefaultPort()==2222," setDefaultport() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getDefaultPort()
    - Ensure the method gets the default value of the Default Port.
    **/
    public void Var021()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            if(s.getDefaultPort() == 2850)
                succeeded();
            else failed("getDefaultPort() do not get the correct default value!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

   /**
    Method tested:getGarbageCollectionFrequency()
    - Ensure the method gets the default value.
    **/
    public void Var022()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","/home/javatest");
            assertCondition(s.getGarbageCollectionFrequency()== 50," getGarbageCollectionFrequency() do not get the correct default value!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getGarbageCollectionFrequency(),setGarbageCollectionFrequency
    - Ensure the method gets the default value.
    **/
    public void Var023()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","/home/javatest");
            s.setGarbageCollectionFrequency(60);
            assertCondition(s.getGarbageCollectionFrequency()== 60," setGarbageCollectionFrequency() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }



    /**
    Method tested:getGarbageCollectionInitialSize()
    - Ensure the method gets the correct value.
    **/
    public void Var024()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            assertCondition(s.getGarbageCollectionInitialSize() == 2048," getGarbageCollectionInitialSize() do not get the correct default value!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getGarbageCollectionInitialSize(),setGarbageCollectionInitialSize()
    - Ensure the method gets the correct value.
    **/
    public void Var025()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","/home/javatest");
            s.setGarbageCollectionInitialSize(4096);
            assertCondition(s.getGarbageCollectionInitialSize()==4096," setGarbageCollectionInitialSize() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getGarbageCollectionInitialSize(),setGarbageCollectionInitialSize()
    - Ensure the method gets the correct value.
    **/
    public void Var026()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");
            s.setGarbageCollectionInitialSize(400);
            s.run();
            assertCondition(s.getGarbageCollectionInitialSize()==400," setGarbageCollectionInitialSize() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getGarbageCollectionMaximumSize()
    - Ensure the method gets the default value.
    **/
    public void Var027()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_);
            assertCondition(s.getGarbageCollectionMaximumSize().equals("*NOMAX")," getGarbageCollectionMaximumSize() does not get the correct default value!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getGarbageCollectionMaximumSize() ,setGarbageCollectionMaximumSize()
    - Ensure the method gets the correct value.
    **/
    public void Var028()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","/home/javatest");
            s.setGarbageCollectionMaximumSize("7000");
            assertCondition(s.getGarbageCollectionMaximumSize().equals("7000")," setGarbageCollectionMaximumSize() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getGarbageCollectionPriority()
    - Ensure the method gets the default value.
    **/
    public void Var029()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","/home/javatest");
            assertCondition(s.getGarbageCollectionPriority()==20," getGarbageCollectionPriority() does not get the correct default value!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getGarbageCollectionPriority(),setGarbageCollectionPriority
    - Ensure the method gets the correct value.
    **/
    public void Var030()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","/home/javatest");
            s.setGarbageCollectionFrequency(33);
            assertCondition(s.getGarbageCollectionFrequency()==33," setGarbageCollectionFrequency() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }
    /**
    Method tested:getInterpret(),setInterpret()
    - Ensure the method gets the correct value.("*YES")
    **/
    public void Var031()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","/home/javatest");
            s.setInterpret("*YES");
            assertCondition(s.getInterpret().equals("*YES")," setInterpret(\"*YES\") runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getInterpret(),setInterpret()
    - Ensure the method gets the correct value.("*NO")
    **/
    public void Var032()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","/home/javatest");
            s.setInterpret("*NO");
            assertCondition(s.getInterpret().equals("*NO")," setInterpret(\"*NO\") runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getInterpret(),setInterpret()
    - Ensure the method gets the correct value.
    **/
    public void Var033()
    {
        boolean Continue = false;
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","/home/javatest");
            s.setInterpret("*OPTIMIZE");
            if (s.getInterpret().equals("*OPTIMIZE"))
               Continue = true;
            else
               failed(" setInterpret(\"*OPTIMIZE\") runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }

        if (Continue)
        {
           try
           {
               JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","/home/javatest");
               s.setInterpret("*JIT");
               assertCondition(s.getInterpret().equals("*JIT")," setInterpret(\"*JIT\") runs incorrectly!");
           }
           catch (Exception e)
           {
               failed(e, "Unexpected exception occurred.");
           }
        }
    }

    /**
    Method tested:getJavaApplication()
    - Ensure the method gets the default value.
    **/
    public void Var034()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","/home/javatest");
            assertCondition(s.getJavaApplication().equals("Test"),"  getJavaApplication() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getJavaApplication(),setJavaApplication()
    - Ensure the method gets the default value.
    **/
    public void Var035()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.setJavaApplication("Test");
            assertCondition(s.getJavaApplication().equals("Test")," setJavaApplication() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getMessageList()
    - Ensure the method gets the default value.
    **/
    public void Var036()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","javatest");
            assertCondition(s.getMessageList() ==null," getMessageList() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getMessageList()
    - Ensure the method gets the correct value.
    **/
    public void Var037()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_, "TestPrg1", "/home/javatest");
            if (s.run())
            {
               if ((s.getMessageList() == null) ||
                   (s.getMessageList().length == 0))
                  succeeded();
               else
                  failed("message list not empty");
            }
            else
               failed("run() returned false");

     //     assertCondition(s.getMessageList() == null," getMessageList() does not return null");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getOptimization()
    - Ensure the method gets the default value.
    **/
    public void Var038()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","javatest");
            assertCondition(s.getOptimization().equals("10")," getOptimization() does not get the correct default value!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getOptimization(),setOptimization()
    - Ensure the method gets the correct value.
    **/
    public void Var039()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.setOptimization("12");
            failed("setOptimization() runs incorrectly.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalArgumentException"))
               succeeded();
            else
               failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getOptimization(),setOptimization()
    - Ensure the method gets the correct value.
    **/
    public void Var040()
    {
        boolean Continue = false;
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.setOptimization("*INTERPRET");
            if (s.getOptimization().equals("*INTERPRET"))
               Continue = true;
            else
               failed("Optimization not *INTERPRET.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }

        if (Continue)
        {
           try
           {
               JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
               s.setOptimization("*JIT");
               assertCondition(s.getOptimization().equals("*JIT")," setOptimization() runs incorrectly!");
           }
           catch (Exception e)
           {
               failed(e, "Unexpected exception occurred (2).");
           }
        }
    }

    /**
    Method tested:getOptimization(),setOptimization()
    - Ensure the method gets the correct value.
    **/
    public void Var041()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.setOptimization("20");
            assertCondition(s.getOptimization().equals("20")," setOptimization() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
    Method tested:getOptimization(),setOptimization()
    - Ensure the method gets the correct value.
    **/
    public void Var042()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.setOptimization("30");
            assertCondition(s.getOptimization().equals("30"),"setOptimization() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
    Method tested:getOptimization(),setOptimization()
    - Ensure the method gets the correct value.
    **/
    public void Var043()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.setOptimization("40");
            assertCondition(s.getOptimization().equals("40")," setOptimization() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
    Method tested:getOptions()
    - Ensure the method gets the default value.
    **/
    public void Var044()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test","javatest");
            String [] option = s.getOptions();
            assertCondition(option[0].equals("*NONE")," getOptions() does not get the correct default value!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getOptions(),setOptions()
    - Ensure the method gets the correct value.
    **/
    public void Var045()
    {
        try
        {
            String[] option = new String[1];
            option[0] = "*NONE";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.setOptions(option);
            String [] option1 = s.getOptions();
            assertCondition(option1[0].equals("*NONE")," setOptions() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getOptions(),setOptions()
    - Ensure the method gets the correct value.
    **/
    public void Var046()
    {
        try
        {
            String[] option = new String[1];
            option[0] = "*VERBOSE";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.setOptions(option);
            String [] option1 = s.getOptions();
            assertCondition(option1[0].equals("*VERBOSE")," getOptions() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getOptions(),setOptions()
    - Ensure the method gets the correct value.
    **/
    public void Var047()
    {
        try
        {
            String[] option = new String[2];
            option[0] = "*VERBOSE";
            option[1] = "*DEBUG";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.setOptions(option);
            String [] option1 = s.getOptions();
            assertCondition((option1[0].equals("*VERBOSE"))&&(option1[1].equals("*DEBUG"))," setOptions() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
    Method tested:getOptions(),setOptions()
    - Ensure the method gets the correct value.
    **/
    public void Var048()
    {
        try
        {
            String[] option = new String[1];
            option[0] = "*VERBOSEGC";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.setOptions(option);
            String [] option1 = s.getOptions();
            assertCondition(option1[0].equals("*VERBOSEGC")," setOptions() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
    Method tested:getOptions(),setOptions()
    - Ensure the method gets the correct value.
    **/
    public void Var049()
    {
        try
        {
            String[] option = new String[1];
            option[0] = "*NOCLASSGC";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.setOptions(option);
            String [] option1 = s.getOptions();
            assertCondition(option1[0].equals("*NOCLASSGC")," setOptions() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getParameters()
    - Ensure the method gets the default value.
    **/
    public void Var050()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            if(s.getParameters().length == 0)
                succeeded();
            else failed(" getParameters() gets the incorrect value!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getParameters(),setParameters()
    - Ensure the method gets the correct value.
    **/
    public void Var051()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            String[] param = new String[3];
            param[0] = "p1";
            param[1] = "p2";
            s.setParameters(param);
            String[] param1 = s.getParameters();
            assertCondition((param1[0].equals(param[0]))&&(param1[1].equals(param[1]))," setParameters() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getProperties()
    - Ensure the method gets the default value.
    **/
    public void Var052()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            if(s.getProperties() == null)
                succeeded();
            else
                failed(" getProperties() gets the incorrect value!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getProperties(),setProperties()
    - Ensure the method gets the correct value.
    **/
    public void Var053()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            Properties prop = new Properties();
            prop.put("k1","v1");
            prop.put("k2","v2");
            prop.put("os400.stdin","os400.stdin");
            prop.put("os400.stdout","os400.stdout");
            prop.put("os400.stderr","os400.stderr");
            prop.put("os400.stdio.convert","os400.stdio.convert");

            s.setProperties(prop);

            Properties prop1 = s.getProperties();
            assertCondition((prop1.getProperty("k1").equals("v1"))&&(prop1.getProperty("k2").equals("v2"))," setProperties() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


     /**
    Method tested:getSecurityCheckLevel()
    - Ensure the method gets the default value.
    **/
    public void Var054()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            assertCondition(s.getSecurityCheckLevel().equals("*WARN")," getSecurityCheckLevel() gets the correct default value!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getSecurityCheckLevel(),setSecurityCheckLevel()
    - Ensure the method gets correct value.
    **/
    public void Var055()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setSecurityCheckLevel("*SECURE");
            assertCondition(s.getSecurityCheckLevel().equals("*SECURE")," setSecurityCheckLevel() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getSecurityCheckLevel(),setSecurityCheckLevel()
    - Ensure the method runs well.
    **/
    public void Var056()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");
            s.setSecurityCheckLevel("*SECURE");
            s.run();
            assertCondition(s.getSecurityCheckLevel().equals("*SECURE")," setSecurityCheckLevel() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getSecurityCheckLevel(),setSecurityCheckLevel()
    - Ensure the method runs well.
    **/
    public void Var057()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","javatest");
            s.setSecurityCheckLevel("*invalid");
            failed(" setSecurityCheckLevel() runs incorrectly!");
        }
        catch (Exception e)
        {
            succeeded();
        }
    }


    /**
    Method tested:getStandardErrorString()
    - Ensure the method gets the default value.
    **/
    public void Var058()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestError","javatest");
            assertCondition(s.getStandardErrorString() == null," getStandardErrorString() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getStandardOutString()
    - Ensure the method runs well.
    **/
    @SuppressWarnings("deprecation")
    public void Var059()
    {
        try
        {
	    // Reset the object by calling disconnect.
	    // Call a Java program several times has a glitch in
            // the JAVA command. 
	    systemObject_.disconnectAllServices(); 

            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");

            // Start a port listener thread, before we call the program.
            // That way we avoid missing the stdout message from the program.
            PortListener listener = new PortListener(s);
            if (DEBUG) System.out.println("Starting port listener ...");
            Thread thread1 = new Thread(listener, "PortListener1");
            thread1.setDaemon(true);
            thread1.start();

            if (DEBUG) System.out.println("Calling JavaApplicationCall.run() ...");
            boolean result = s.run();
            if (DEBUG) System.out.println("Called JavaApplicationCall.run() ...");

            String stdout = listener.getStandardOutString();
            String stderr = listener.getStandardErrorString();
            thread1.stop();  // we're done with the thread
            if (DEBUG) {
              System.out.println("listener.getStandardOutString() == |" + stdout + "|");
              System.out.println("listener.getStandardErrorString() == |" + stderr + "|");
            }

            assertCondition((result==true &&
                             stdout != null &&
                             stdout.equals("The end of test program1!") &&
                             stderr == null),
                            " getStandardOutString() runs incorrectly\n"+
			    " result="+result+" sb true\n"+
			    " stdout = "+stdout+" sb 'The end of test program1!'\n"+
			    " stderr = "+stderr+" sb null");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getSystem()
    - Ensure the method runs well.
    **/
    public void Var060()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_);
            assertCondition(s.getSystem().equals(systemObject_)," getSystem() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:getSystem(),setSystem()
    - Ensure the method runs well.
    **/
    public void Var061()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_);
            AS400 as400 = new AS400();
            s.setSystem(as400);
            assertCondition(s.getSystem().equals(as400)," getSystem() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
    Method tested:isFindPort()
    - Gets the default value.
    **/
    public void Var062()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();

            assertCondition(s.isFindPort()," isFindPort() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e);
        }

    }

    /**
    Method tested:isFindPort() and setFindPort()
    **/
    public void Var063()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setFindPort(false);
            assertCondition(!s.isFindPort()," setFindPort(false) runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e);
        }


    }
    /**
    Method tested:isFindPort() and setFindPort()
    **/
    public void Var064()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setFindPort(true);
            assertCondition(s.isFindPort()," setFindPort(true) runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
    Method tested:removeActionCompletedListener()
    - Test that removing a null listener causes an exception.
    **/
    public void Var065()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.removeActionCompletedListener(null);
            failed(" removeActionCompletedListener(null) runs incorrectly!");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
    Method tested:removeActionCompletedListener()
    - Test that events are no longer received.
    **/
    public void Var066()
    {
        try
        {
            String app = "TestPrg1";
            String classPath ="/home/javatest";
            ActionCompletedListener_ listener = new ActionCompletedListener_();
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,app,classPath);
            s.addActionCompletedListener(listener);
            s.removeActionCompletedListener(listener);
            s.run();
            assertCondition(listener.getActionEvent() == null, " removeActionCompletedListener() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
    Method tested:removePropertyChangeListener()
    - Test that removing a null listener causes an exception.
    **/
    public void Var067()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.removePropertyChangeListener(null);
            failed(" removePropertyChangeListener(null) runs incorrectly!");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
    Method tested:removePropertyChangeListener()
    - Test that events are no longer received.
    **/
    public void Var068()
    {
        try
        {
            String app = "Test";
            String classPath ="javatest1";
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,app,classPath);
            s.addPropertyChangeListener(listener);
            s.removePropertyChangeListener(listener);
            s.setClassPath("javatest");
            assertCondition(listener.lastEvent_ == null," removePropertyChangeListener() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e," removePropertyChangeListener() runs incorrectly!");
        }
    }

    /**
    Method tested:removeVetoableChangeListener()
    - Test that removing a null listener causes an exception.
    **/
    public void Var069()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.removeVetoableChangeListener(null);
            failed(" removeVetoableChangeListener(null) runs incorrectly!" );
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

    /**
    Method tested:removeVetoableChangeListener()
    - Test that events are no longer received.
    **/
    public void Var070()
    {
        try
        {
            String app = "Test";
            String classPath ="javatest1";
            VetoableChangeListener_ listener = new VetoableChangeListener_();
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,app,classPath);
            s.addVetoableChangeListener(listener);
            s.removeVetoableChangeListener(listener);
            s.setClassPath("javatest");
            assertCondition(listener.lastEvent_ == null," removeVetoableChangeListener() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed();
        }
    }

    /**
    Method tested:run()
    - An exception is thrown when the value of system is null.
    **/
    public void Var071()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.run();
            failed ("Did not throw exception.");
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
    Method tested:run()
    - An exception is thrown when the value of javaApplication is null.
    **/
    public void Var072()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_);
            s.run();
            failed ("Did not throw exception.");
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
    Method tested:run()
    - Ensure the method runs well.
    **/
    public void Var073()
    {
        try
        {
            String app = "TestPrg1";
            String classpath = "/home/javatest";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,app,classpath);
            boolean ranOK = s.run();
            CommandCall cmd = null;
            Job job = null;
            boolean methodIsDefined = true;  // is the getCommand() method defined
            try { cmd = s.getCommandCall(); }
            catch (NoSuchMethodError e) {
              System.out.println("Method getCommandCall() is not defined."); // method was added after JTOpen 4.6
              methodIsDefined = false;
            }
            if (cmd == null) System.out.println("getCommandCall() returned null.");
            else {
              if (!ranOK) {
                System.out.println("run() returned false");
                AS400Message[] msgs = s.getMessageList();
                for (int i=0; i<msgs.length; i++) {
                  System.out.println(msgs[i].getText());
                }
              }
              job = cmd.getServerJob();
            }
            assertCondition(ranOK &&
                            (!methodIsDefined ||
                             (job != null && job.getName() != null) ),
			    "ranOK = "+ranOK+" sb true\n"+
                            "methodIsDefined = "+methodIsDefined+"\n"+
			    "job="+job+"\n");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


 /**
    Method tested:setGarbageCollectionFrequency()
    - Ensure the bound value can be set.
    **/
    public void Var074()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");
            s.setGarbageCollectionFrequency(0);
            s.run();
            assertCondition(s.getGarbageCollectionFrequency()==0, " setGarbageCollectionFrequency(0) runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:setGarbageCollectionFrequency()
    - Ensure the bound value can be set.
    **/
    public void Var075()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");
            s.setGarbageCollectionFrequency(100);
            s.run();
            assertCondition(s.getGarbageCollectionFrequency()==100," setGarbageCollectionFrequency(100) runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
    Method tested:setGarbageCollectionFrequency()
    - Ensure the value that is larger than upper limit can not be set.
    **/
    public void Var076()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");
            s.setGarbageCollectionFrequency(101);
            s.run();
            failed(" setGarbageCollectionFrequency(101) runs incorrectly!");
        }
        catch (Exception e)
        {
            succeeded();
        }
    }

    /**
    Method tested:setGarbageCollectionFrequency()
    - Ensure the value that is less than lower limit can not be set.
    **/
    public void Var077()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");
            s.setGarbageCollectionFrequency(-1);
            s.run();
            failed(" setGarbageCollectionFrequency(-1) runs incorrectly!");
        }
        catch (Exception e)
        {
            succeeded();
        }
    }

    /**
    Method tested:setGarbageCollectionInitialSize()
    - Ensure the bound value can be set.
    **/
    public void Var078()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setGarbageCollectionInitialSize(256);
            assertCondition(s.getGarbageCollectionInitialSize()==256, " setGarbageCollectionInitialSize(256) runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:setGarbageCollectionInitialSize()
    - Ensure the bound value can be set.
    **/
    public void Var079()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setGarbageCollectionInitialSize(13964000);
            assertCondition(s.getGarbageCollectionInitialSize()==13964000," setGarbageCollectionInitialSize(13964000) runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
    Method tested:setGarbageCollectionInitialSize()
    - Ensure the value can not be set.
    **/
    public void Var080()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","javatest");
            s.setGarbageCollectionMaximumSize("80000");
            s.setGarbageCollectionInitialSize( 85000);
            if (!s.run())
               succeeded();
            else
               failed("GarbageCollectionInitialSize is less than GarbageCollectionMaximumSize!");
        }
        catch (Exception e)
        {
            failed(e, "unexpected exception");
        }
    }

    /**
    Method tested:setGarbageCollectionInitialSize()
    - Ensure the value that is less than lower limit can not be set.
    **/
    public void Var081()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");
            s.setGarbageCollectionInitialSize(-1);
            s.run();
            failed(" setGarbageCollectionInitialSize(-1) runs incorrectly!");
        }
        catch (Exception e)
        {
            succeeded();
        }
    }


    /**
    Method tested:setGarbageCollectionMaximumSize()
    - Ensure the value of GarbageCollectionMaximumSize can not be lower than
    that of GarbageCollectionInitialSize.
    **/
    public void Var082()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","javatest");
            s.setGarbageCollectionMaximumSize(null);
            failed(" setGarbageCollectionMaximumSize(null) runs incorrectly!");
        }
        catch (Exception e)
        {
            succeeded();
        }
    }

    /**
    Method tested:setGarbageCollectionMaximumSize()
    - Ensure the value is larger than upper limit can not be set.
    **/
    public void Var083()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");
            s.setGarbageCollectionMaximumSize("139640000");
            s.run();
            assertCondition(s.getGarbageCollectionMaximumSize().equals("139640000")," setGarbageCollectionMaximumSize(\"139640000\") runs incorrectly!");
            s.setGarbageCollectionMaximumSize("3000");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
    Method tested:setGarbageCollectionMaximumSize()
    - Ensure the value can be set.
    **/
    public void Var084()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");
            s.setGarbageCollectionInitialSize(2500);
            s.setGarbageCollectionMaximumSize("15000");
            s.run();
            assertCondition(s.getGarbageCollectionMaximumSize().equals("15000")," setGarbageCollectionMaximumSize(\"15000\") runs incorrectly!");
        }
        catch (Exception e)
        {
            failed();
        }
    }

    /**
    Method tested:setGarbageCollectionMaximumSize()
    - Ensure the value is *NOMAX.
    **/
    public void Var085()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","javatest");
            s.setGarbageCollectionMaximumSize("*NOMAX");
            assertCondition(s.getGarbageCollectionMaximumSize().equals("*NOMAX"), " setGarbageCollectionMaximumSize(\"*NOMAX\") runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Not expected exception!");
        }
    }

    /**
    Method tested:setGarbageCollectionPriority()
    - Ensure the value that is larger than upper limit can not be set.
    **/
    public void Var086()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","javatest");
            s.setGarbageCollectionPriority(40);
            s.run();
            failed("no exception");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalArgumentException"))
              succeeded();
            else
               failed(e, "Unexpected exception");
        }
    }

    /**
    Method tested:setGarbageCollectionPriority()
    - Ensure the value that is less than lower limit can not be set.
    **/
    public void Var087()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setGarbageCollectionPriority(15);
            s.run();
            failed(" setGarbageCollectionPriority(15) runs incorrectly!");
        }
        catch (Exception e)
        {
            succeeded();
        }
    }

    /**
    Method tested:setClassPath()
    - Ensure the value is null.
    **/
    public void Var088()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setClassPath(null);
            failed(" setClassPath(null) runs incorrectly!");
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
    Method tested:setInterpret()
    - Ensure the value is null.
    **/
    public void Var089()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setInterpret(null);
            failed(" setInterpret(null) runs incorrectly!");
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
    Method tested:setInterpret()
    - Ensure the value is null.
    **/
    public void Var090()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setInterpret("invalid");
            failed(" setInterpret(\"invalid\") runs incorrectly!");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalArgumentException"))
               succeeded();
            else
               failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:setJavaApplication()
    - Ensure the value is null.
    **/
    public void Var091()
    {
        try
        {
            String javaApp = null;
            JavaApplicationCall s = new JavaApplicationCall();
            s.setJavaApplication(javaApp);
            failed(" setJavaApplication(null) runs incorrectly!");
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
    Method tested:setOptimization()
    - Ensure the value is null.
    **/
    public void Var092()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setOptimization("invalid");
            failed(" setOptimization(\"invalid\") runs incorrectly!");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalArgumentException"))
               succeeded();
            else
               failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:setOptimization()
    - Ensure the value is null.
    **/
    public void Var093()
    {
        try
        {
            String opt = null;
            JavaApplicationCall s = new JavaApplicationCall();
            s.setOptimization(opt);
            failed(" setOptimization(null) runs incorrcetly!");
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
    Method tested:setOptions()
    - Ensure the value is null.
    **/
    public void Var094()
    {
        try
        {
            String [] option = null;
            JavaApplicationCall s = new JavaApplicationCall();
            s.setOptions(option);
            failed(" setOptions(null) runs incorrcetly!");
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
    Method tested:setOptions()
    - Ensure the value is null.
    **/
    public void Var095()
    {
        try
        {
            String[] opt = new String[1];
            JavaApplicationCall s = new JavaApplicationCall();
            opt[0] = "invalid";
            s.setOptions(opt);
            failed(" setOptions() with invalid parameter value runs incorrectly!");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalArgumentException"))
               succeeded();
            else
               failed(e, "Unexpected exception occurred.");
        }
    }


    /**
    Method tested:setParameters()
    - Ensure the value is null.
    **/
    public void Var096()
    {
        try
        {
            String[] v = null;
            JavaApplicationCall s = new JavaApplicationCall();
            s.setParameters(v);
            failed(" setParameters(null) runs incorrectly!");
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
    Method tested:setGarbageCollectionInitialSize()
    - Ensure the value is invalid.
    **/
    public void Var097()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setGarbageCollectionInitialSize(-2);
            failed(" setGarbageCollectionInitialSize(-2) runs incorrectly!");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalArgumentException"))
               succeeded();
            else
               failed(e, "Unexpected exception occurred.");
        }
    }

  /**
    Method tested:setGarbageCollectionMaximumSize()
    - Ensure the value is invalid.
    **/
    public void Var098()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setGarbageCollectionMaximumSize("invalid");
            failed(" setGarbageCollectionMaximumSize(\"invalid\") runs incorrectly!");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalArgumentException"))
               succeeded();
            else
               failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:setGarbageCollectionMaximumSize()
    - Ensure the value is null.
    **/
    public void Var099()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setGarbageCollectionMaximumSize(null);
            failed(" setGarbageCollectionMaximumSize(null) runs incorrectly!");
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
    Method tested:setSystem()
    - Ensure the value is null.
    **/
    public void Var100()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall();
            s.setSystem(null);
            failed(" setSystem(null) runs incorrectly!");
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
    Method tested:setFindPort()
    - Ensure the exception is thrown.
    **/
    public void Var101()
    {
        ServerSocket read_ = null;
        try
        {
            read_ = new ServerSocket(7000);
            JavaApplicationCall s1 = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");
            s1.setDefaultPort(7000);
            s1.setFindPort(false);
            s1.run();
            failed(" Did not throw an exception when the reading port is used!");
        }
        catch(Exception e)
        {
            if(exceptionIs(e,"RuntimeException"))
                succeeded();
            else failed("Not expected exception!");
        }
        finally
        {
            try
            {
                read_.close();
            }
            catch(Exception e){}
        }
    }

   /**
    Method tested:setFindPort()
    - Ensure the exception is thrown.
    **/
    public void Var102()
    {
        ServerSocket write=null;
        try
        {
            write = new ServerSocket(7010);

            JavaApplicationCall s1 = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");
            s1.setDefaultPort(7009);
            s1.setFindPort(false);
            s1.run();
            failed(" Did not throw an exception when writing port is used!");
        }
        catch(Exception e)
        {
            if(exceptionIs(e,"RuntimeException"))
                succeeded();
            else failed("Not expected exception!");
        }
        finally
        {
            try
            {
                write.close();
            }
            catch(Exception e) {}
        }
    }

    /**
    Method tested:setFindPort()
    - Ensure the value is null.
    **/
    public void Var103()
    {
        ServerSocket error_ = null;
        try
        {
            error_ = new ServerSocket(7020);
            JavaApplicationCall s1 = new JavaApplicationCall(systemObject_,"TestPrg1","javatest");
            s1.setDefaultPort(7018);
            s1.setFindPort(false);
            s1.run();
            failed(" Did not throw an exception when error port is used!");
        }
        catch(Exception e)
        {
            if(exceptionIs(e,"RuntimeException"))
                succeeded();
            else failed(e,"Not expected exception!");
        }
        finally
        {
            try
            {
                error_.close();
            }
            catch(Exception e) {}
        }
    }

    /**
    Method tested:setDefaultPort()
    - Ensure the method runs well.
    **/
    public void Var104()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_);
            s.setDefaultPort(-1);
        }
        catch (Exception e)
        {
            if(exceptionIs(e,"IllegalArgumentException"))
                succeeded();
            else failed(e,"Not expected exception!");
        }
    }

    /**
    Method tested:setProperties()
    - Ensure the method sets the invalid value.
    **/
    public void Var105()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            Properties p = null;
            s.setProperties(p) ;
            failed(" setProperties() runs incorrectly!");
        }
        catch (Exception e)
        {
            if(exceptionIs(e,"NullPointerException"))
                succeeded();
            else failed("Not expected exception!");
        }
    }

    /**
    Method tested:setSecurityCheckLevel()
    - Ensure the method sets the invalid value.
    **/
    public void Var106()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.setSecurityCheckLevel(null) ;
            failed();
        }
        catch (Exception e)
        {
            if(exceptionIs(e,"NullPointerException"))
                succeeded();
            else failed("Not expected exception!");
        }
    }

 /**
   Serialize and de-serialize ServiceProgramCall.
   **/
   public void Var107()
   {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","javatest");

            FileOutputStream fOut = new FileOutputStream("tmp");
            ObjectOutput  output  =  new  ObjectOutputStream(fOut);
            output.writeObject(s);
            output.flush();

            FileInputStream fIn = new FileInputStream("tmp");
            ObjectInput  input  =  new  ObjectInputStream(fIn);
            JavaApplicationCall sIn = (JavaApplicationCall)input.readObject();

            fOut.close();
            fIn.close();
            File tempFile = new File("tmp");
            if(tempFile.exists())
            {
                if(!tempFile.delete())
                    System.out.println("File not deleted.");
            }
            else System.out.println("file not exists.");
            assertCondition(sIn.getJavaApplication().equals("TestPrg1"), " de-serialize runs incorrectly!");
        }
        catch(Exception e)
        {
            failed(e, "Unexpected exception occured");
        }

   }

     /**
    Method tested:setSystem()
    - Ensure the value can not be set.
    **/
    public void Var108()
    {
        ServerSocket[] serSocket = new ServerSocket[3010];
        int [] index = new int[3010];

           int j = 0;
           while(j< 3010)
           {
               index[j] = 0;
               j +=1;
           }
           j = 0;
           int i = 0;
        try
        {
            while(j < 3010)
            {
                try
                {
                    serSocket[j] = new ServerSocket(2850+j);
                }
                catch(Exception e1)
                {
                    j +=1;
                    continue;
                }
                index[i] = j;
                i +=1;
                j +=1;
            }
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","javatest");
            s.setFindPort(true);
            s.run();
            failed(" setFindPort() runs incorrectly!");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "RuntimeException"))
               succeeded();
            else
               failed(e, "Unexpected exception occurred.");
        }
        i = 0;
        while(i < 3010)
        {
          try
          {
            if(index[i]!=0)
              serSocket[index[i]].close();
          }
          catch(Exception e1)
          {
            i +=1;
            continue;
          }
          i +=1;
        }
    }

    /**
    Method tested:setGarbageCollectionMaximumSize()
    - Ensure the value can not be set if GarbageCollectionInitialSize is greater than it.
    **/
    public void Var109()
    {
      notApplicable("Obsolete variation");
      // We see different results, depending on the i5/OS version.
      // It's not worth validating the various different behaviors.
//        try
//        {
//            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","/home/javatest");
//            s.setGarbageCollectionInitialSize(12500);
//            s.setGarbageCollectionMaximumSize("5000");
//            
//            // Change to expect command to succeed.  The i5/OS has made changes to 
//            // tolerate (GCHINL > GCHMAX) on the JAVA command.  So the JAVA cmd
//            // now succeeds.
//            if (s.run())           //@A1C
//               succeeded();
//            else
//               failed(" GarbageCollectionMaximumSize is not larger than GarbageCollectionInitialSize!");
//        }
//        catch (Exception e)
//        {
//            failed(e, "Unexpected exception occurred.");
//        }
    }

   /**
    Method tested:getProperties(),setProperties()
    - Ensure the method gets the correct value.
    **/
    public void Var110()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            Properties prop = new Properties();
            prop.put("k1","v1");
            prop.put("k2","v2");

            s.setProperties(prop);

            Properties prop1 = s.getProperties();
            assertCondition((prop1.getProperty("k1").equals("v1"))&&(prop1.getProperty("k2").equals("v2"))," setProperties() runs incorrectly!");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
    Method tested:sendStandardInString()
    - Ensure the method can not be executed correctly.
    **/
    public void Var111()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"Test22","javatest");
            s.sendStandardInString("LLL");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
    Method tested:setGarbageCollectionMaximumSize()
    - Ensure the value is invalid.
    **/
    public void Var112()
    {
        try
        {
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPrg1","javatest");
            s.setGarbageCollectionMaximumSize("aa11g");
            failed(" setGarbageCollectionMaximumSize(\"aa11g\") runs incorrectly!" );
        }
        catch (Exception e)
        {
            if(exceptionIs(e,"IllegalArgumentException"))
                succeeded();
            else failed(e,"Not expected exception!");
        }
    }

    /**
    Method tested: various methods
    - Ensure successful execution.
    **/
    public void Var113()
    {
        try
        {

	    // Reset the object by calling disconnect.
	    // Call a Java program several times has a glitch in
	    // the JAVA command. 
	    systemObject_.disconnectAllServices(); 

	    int loop = 1000000;
	    long startLoopTime = System.currentTimeMillis();
	    long endLoopTime = startLoopTime;
	    for (int i = 0; i < loop; i++) {
		endLoopTime =  System.currentTimeMillis();
	    } 
	    long setupLoopMilliseconds =  endLoopTime - startLoopTime; 
            String inputString = "Hello";
            String errorString = "No_Error";
            JavaApplicationCall s = new JavaApplicationCall(systemObject_,"TestPort1","/home/javatest");

            TestPort testPort = new TestPort(s);
            testPort.setInputString(inputString);
            testPort.setErrorString(errorString);

            testPort.play();
            int waitedMilliseconds = 0;
            final int maxWaitSeconds = 300;  // wait up to a total of 10 seconds
            while(!testPort.isOver() && waitedMilliseconds <= maxWaitSeconds*1000)
            {
               try { Thread.sleep(200); } catch (Exception e) {}
               waitedMilliseconds += 200;
            }

            if (waitedMilliseconds > maxWaitSeconds*1000) {
              System.out.println("Test did not complete within " + maxWaitSeconds + " seconds.\n");
            }

	    
	    System.out.println("Info:  Test took "+waitedMilliseconds+" milliseconds ");
	    System.out.println("Setup loop took  "+setupLoopMilliseconds+" milliseconds ");

            if (testPort.getOutputString() == null) {
              System.out.println("testPort.getOutputString() == null");
            }

            if (testPort.getErrorString() == null) {
              System.out.println("testPort.getErrorString() == null");
            }

	    String outputString =  testPort.getOutputString();
	    String outErrorString =  testPort.getErrorString(); 
            assertCondition(
                   (outputString != null) &&
                   (outputString.equals(inputString)) &&
                   (outErrorString != null) &&
                   (outErrorString.equals(errorString)),
                   " Ports output error:  \n"+
		   "     outputString='"+outputString+"' sb '"+inputString+"'\n"+
                   "     errorString ='"+outErrorString+"' sb '"+errorString+"'\n");
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

  /**
   *  This class tests three standard ports : In ,Output and Error .
   **/
  class TestPort implements Runnable
  {
    private JavaApplicationCall javaAppCall_;
    private Thread runThread_;
    private Thread outputThread_;
    private Thread errorThread_;
    private boolean runOver_ = false;
    private String errorStr = null;
    private String receiveStr = null;
    private String inputString = null;
    private String errorString = null;


    public TestPort(JavaApplicationCall javaAppCall)
    {
        javaAppCall_  = javaAppCall;
        runThread_    = new Thread(this);
        outputThread_ = new Thread(this);
        errorThread_  = new Thread(this);
    }
    // Starts three threads.
    public void play()
    {
        outputThread_.start();
        errorThread_.start();
        runThread_.start();
    }
    // Returns the boolean value indicating whether the application runs over.
    public boolean isOver()
    {
        return runOver_;
    }
    // run three threads.
    public void run()
    {
         if(Thread.currentThread() == runThread_)
        {
            runOver_=false;
            try
            {
                javaAppCall_.run();
            }
            catch(Exception e)
            {
                System.out.println(" run app error");
            }
            runOver_ = true;
        }
        else if(Thread.currentThread() == outputThread_)
        {
            // stream used to receive output from server

            while(!runOver_)
            {
                System.out.println("");
                System.out.println(System.currentTimeMillis()+": "+"Java job name is "+javaAppCall_.getJobName());
		System.out.println(System.currentTimeMillis()+": "+"OutputThread sending "+inputString); 
                javaAppCall_.sendStandardInString(inputString);
		System.out.println(System.currentTimeMillis()+": "+"OutputThread calling get"); 
                receiveStr = javaAppCall_.getStandardOutString();
                System.out.println(System.currentTimeMillis()+": "+"OutputThread calling got "+receiveStr); 
                if (receiveStr != null)
                {
                    javaAppCall_.sendStandardInString(errorString);
                    break;
                }
                delay();
            }
        }
        else
        {
           // stream used to receive error message from server
            while(!runOver_)
            {
                System.out.println(System.currentTimeMillis()+": "+"ErrorThread calling getErrorString"); 
                errorStr = javaAppCall_.getStandardErrorString();
                System.out.println(System.currentTimeMillis()+": "+"ErrorThread got "+errorStr); 
                if (errorStr != null)
                {
                     break;
                }
                delay();
            }
        }
    }
    // Returns the input value.
    public String getOutputString()
    {
       return receiveStr;
    }

    // Returns the error string.
    public String getErrorString()
    {
       return errorStr;
    }
    public void setInputString(String inputStr)
    {
        inputString = inputStr;
    }
    public void setErrorString(String errStr)
    {
        errorString = errStr;
    }

    private void delay()
    {
       try { Thread.sleep(100); }
       catch (Exception e) {}
    }
  }


  /**
   *  The class listens on stdin and stderr.
   **/
  class PortListener implements Runnable
  {
    private String stdout_ = null;
    private String stderr_ = null;
    private boolean done_ = false;
    private JavaApplicationCall javaAppCall_;


    public PortListener(JavaApplicationCall javaAppCall)
    {
        javaAppCall_  = javaAppCall;
    }

    // This thread will get stdout and stderr from the IBM i system Java
    // program and print it.  Note the call to sleep.  JavaApplication
    // call returns immediately even if there is no data.
    public void run()
    {
      String s;
      while (true)
      {
        if (DEBUG) System.out.print("x");

        s = javaAppCall_.getStandardOutString();
        if (s != null) {
          stdout_ = s;
          break;
        }

        s = javaAppCall_.getStandardErrorString();
        if (s != null) {
          stderr_ = s;
          break;
        }

        delay();
      }

      done_ = true;
      if (DEBUG) System.out.println("\nExited while loop in PortListener. done="+done_);
    }

    // Returns the stdout string.
    public String getStandardOutString()
    {
       return stdout_;
    }

    // Returns the error string.
    public String getStandardErrorString()
    {
       return stderr_;
    }

    private void delay()
    {
       try { Thread.sleep(100); }
       catch (Exception e) {}
    }
  }

}//end of class





