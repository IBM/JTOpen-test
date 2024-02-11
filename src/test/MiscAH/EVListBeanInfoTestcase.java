///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  EVListBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.EnvironmentVariableList;
import com.ibm.as400.access.EnvironmentVariableListBeanInfo;

import test.Testcase;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;

/**
 Testcase EVListBeanInfoTestcase.  This tests the EnvironmentVariableListBeanInfo class.
 **/
public class EVListBeanInfoTestcase extends Testcase
{
    /**
     getBeanDescriptor() - Should work.
     **/
    public void Var001()
    {
        try {
            EnvironmentVariableListBeanInfo ev = new EnvironmentVariableListBeanInfo();
            BeanDescriptor bd = ev.getBeanDescriptor();
            assertCondition((bd.getBeanClass() == EnvironmentVariableList.class)
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
            EnvironmentVariableListBeanInfo ev = new EnvironmentVariableListBeanInfo();
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
            EnvironmentVariableListBeanInfo ev = new EnvironmentVariableListBeanInfo();
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
            EnvironmentVariableListBeanInfo ev = new EnvironmentVariableListBeanInfo();
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
            EnvironmentVariableListBeanInfo ev = new EnvironmentVariableListBeanInfo();
            PropertyDescriptor[] pd = ev.getPropertyDescriptors();
            assertCondition((pd.length == 1)
                            && (pd[0].getName().equals("system")) && (pd[0].getPropertyType() == AS400.class));
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
            EnvironmentVariableListBeanInfo ev = new EnvironmentVariableListBeanInfo();
                notApplicable("Icons / GUI components no longer available in JTOpen 20.0.X"); 
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
            EnvironmentVariableListBeanInfo ev = new EnvironmentVariableListBeanInfo();
            notApplicable("Icons / GUI components no longer available in JTOpen 20.0.X"); 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
     getXIcon() - Pass ICON_MONO_32x32.
     **/
    public void Var008()
    {
        try {
            EnvironmentVariableListBeanInfo ev = new EnvironmentVariableListBeanInfo();
            notApplicable("Icons / GUI components no longer available in JTOpen 20.0.X"); 
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
            EnvironmentVariableListBeanInfo ev = new EnvironmentVariableListBeanInfo();
            notApplicable("Icons / GUI components no longer available in JTOpen 20.0.X"); 
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
            EnvironmentVariableListBeanInfo ev = new EnvironmentVariableListBeanInfo();
            ev.getIcon(-1);
            failed ("Didn't throw exception");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }
}
