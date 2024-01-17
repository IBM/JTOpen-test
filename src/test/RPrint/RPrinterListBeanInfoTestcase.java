///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrinterListBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RPrint;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceListListener;

import test.Testcase;

import com.ibm.as400.resource.RPrinterList;
import com.ibm.as400.resource.RPrinterListBeanInfo;
import java.awt.Image;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.VetoableChangeListener;
import java.io.FileOutputStream;
import java.util.Hashtable;

  

/**
Testcase RPrinterListBeanInfoTestcase.  This tests the methods
of the RPrinterListBeanInfo class:
**/
public class RPrinterListBeanInfoTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String printerName_;



/**
Constructor.
**/
    public RPrinterListBeanInfoTestcase (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys,
                              String misc)
    {
        super (systemObject, "RPrinterListBeanInfoTestcase",
               namesAndVars, runMode, fileOutputStream,
               password);
        pwrSys_ = pwrSys;
        printerName_ = misc;

        if (pwrSys == null)
            throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");
        if (misc == null)
            throw new IllegalStateException("ERROR: Please specify a printer via -misc.");

    }



/**
getAdditionalBeanInfo().
**/
    public void Var001()
    {
        try {
            RPrinterListBeanInfo ubi = new RPrinterListBeanInfo();
            BeanInfo abi[] = ubi.getAdditionalBeanInfo();
            assertCondition (abi.length == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBeanDescriptor().
**/
    public void Var002()
    {
        try {
            RPrinterListBeanInfo ubi = new RPrinterListBeanInfo();
            BeanDescriptor bd = ubi.getBeanDescriptor();
            assertCondition ((bd.getBeanClass() == RPrinterList.class) && (bd.getCustomizerClass() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDefaultEventIndex().
**/
    public void Var003()
    {
        try {
            RPrinterListBeanInfo ubi = new RPrinterListBeanInfo();
            BeanInfo abi[] = ubi.getAdditionalBeanInfo();
            assertCondition ((ubi.getDefaultEventIndex() == -1) && (abi[0].getDefaultEventIndex() == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDefaultPropertyIndex().
**/
    public void Var004()
    {
        try {
            RPrinterListBeanInfo ubi = new RPrinterListBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            assertCondition ((ubi.getDefaultPropertyIndex() == -1) && (abi[0].getDefaultPropertyIndex() == -1));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getEventSetDescriptors().
**/
    public void Var005()
    {
        try {
            RPrinterListBeanInfo ubi = new RPrinterListBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            EventSetDescriptor[] esd = abi[0].getEventSetDescriptors();
            assertCondition ((ubi.getEventSetDescriptors() == null) 
                    && (esd.length == 3)
                    && (esd[0].getListenerType() == PropertyChangeListener.class)
                    && (esd[1].getListenerType() == ResourceListListener.class)
                    && (esd[2].getListenerType() == VetoableChangeListener.class));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIcon().  Pass an invalid value.
**/
    public void Var006()
    {
        try {
            RPrinterListBeanInfo ubi = new RPrinterListBeanInfo();
            Image icon = ubi.getIcon(-546);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getIcon().  Pass ICON_COLOR_16x16.
**/
    public void Var007()
    {
        try {
            RPrinterListBeanInfo ubi = new RPrinterListBeanInfo();
            assertCondition (true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIcon().  Pass ICON_COLOR_32x32.
**/
    public void Var008()
    {
        try {
            RPrinterListBeanInfo ubi = new RPrinterListBeanInfo();
            assertCondition (true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIcon().  Pass ICON_MONO_16x16.
**/
    public void Var009()
    {
        try {
            RPrinterListBeanInfo ubi = new RPrinterListBeanInfo();
            assertCondition (true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getIcon().  Pass ICON_MONO_32x32.
**/
    public void Var010()
    {
        try {
            RPrinterListBeanInfo ubi = new RPrinterListBeanInfo();
            assertCondition (true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMethodDescriptors().
**/
    public void Var011()
    {
        try {
            RPrinterListBeanInfo ubi = new RPrinterListBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            assertCondition ((ubi.getMethodDescriptors() == null) && (abi[0].getMethodDescriptors() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPropertyDescriptors().
**/
    public void Var012()
    {
        try {
            RPrinterListBeanInfo ubi = new RPrinterListBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            PropertyDescriptor[] pd1 = ubi.getPropertyDescriptors();
            PropertyDescriptor[] pd2 = abi[0].getPropertyDescriptors();
            assertCondition ((pd1 == null) 
                    && (pd2.length == 1)
                    && (pd2[0].getName().equals("system")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




