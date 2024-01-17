///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  EVBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.EnvironmentVariable;
import com.ibm.as400.access.EnvironmentVariableBeanInfo;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;

/**
 Testcase EVBeanInfoTestcase.  This tests the EnvironmentVariableBeanInfo class.
 **/
public class EVBeanInfoTestcase extends Testcase
{
    /**
     getBeanDescriptor() - Should work.
     **/
    public void Var001()
    {
        try {
            EnvironmentVariableBeanInfo ev = new EnvironmentVariableBeanInfo();
            BeanDescriptor bd = ev.getBeanDescriptor();
            assertCondition((bd.getBeanClass() == EnvironmentVariable.class)
                            && (bd.getCustomizerClass() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getDefaultEventIndex() - Should return 0.
     **/
    public void Var002()
    {
        try {
            EnvironmentVariableBeanInfo ev = new EnvironmentVariableBeanInfo();
            assertCondition(ev.getDefaultEventIndex() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getDefaultPropertyIndex() - Should return 0.
     **/
    public void Var003()
    {
        try {
            EnvironmentVariableBeanInfo ev = new EnvironmentVariableBeanInfo();
            assertCondition(ev.getDefaultPropertyIndex() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getEventSetDescriptors() - Should work.
     **/
    public void Var004()
    {
        try {
            EnvironmentVariableBeanInfo ev = new EnvironmentVariableBeanInfo();
            EventSetDescriptor[] esd = ev.getEventSetDescriptors();
            assertCondition((esd.length == 1)
                            && (esd[0].getListenerType() == PropertyChangeListener.class));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getPropertyDescriptors() - Should work.
     **/
    public void Var005()
    {
        try {
            EnvironmentVariableBeanInfo ev = new EnvironmentVariableBeanInfo();
            PropertyDescriptor[] pd = ev.getPropertyDescriptors();
            assertCondition((pd.length == 2)
                            && (pd[0].getName().equals("name")) && (pd[0].getPropertyType() == String.class)
                            && (pd[1].getName().equals("system")) && (pd[1].getPropertyType() == AS400.class));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getXIcon() - Pass ICON_MONO_16x16.
     **/
    public void Var006()
    {
        try {
            EnvironmentVariableBeanInfo ev = new EnvironmentVariableBeanInfo();
                notApplicable("No GUI for icon test.");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getXIcon() - Pass ICON_COLOR_16x16.
     **/
    public void Var007()
    {
        try {
            EnvironmentVariableBeanInfo ev = new EnvironmentVariableBeanInfo();
                notApplicable("No GUI for icon test.");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getIcon() - Pass ICON_MONO_32x32.
     **/
    public void Var008()
    {
        try {
            EnvironmentVariableBeanInfo ev = new EnvironmentVariableBeanInfo();
            // Icons / GUI components no longer available in JTOpen 20.0.X
                notApplicable("No GUI for icon test.");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getIcon() - Pass ICON_COLOR_32x32.
     **/
    public void Var009()
    {
        try {
            EnvironmentVariableBeanInfo ev = new EnvironmentVariableBeanInfo();
            // Icons / GUI components no longer available in JTOpen 20.0.X
                notApplicable("No GUI for icon test.");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getIcon() - Pass an invalid type.
     **/
    public void Var010()
    {
        try {
            EnvironmentVariableBeanInfo ev = new EnvironmentVariableBeanInfo();
            ev.getIcon(-1);
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }
}
