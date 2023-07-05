///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RIFSFileListBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceListListener;
import com.ibm.as400.resource.RIFSFileList;
import com.ibm.as400.resource.RIFSFileListBeanInfo;
import java.awt.Image;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.VetoableChangeListener;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Hashtable;



/**
Testcase RIFSFileListBeanInfoTestcase.  This tests the methods
of the RIFSFileListBeanInfo class:
**/
public class RIFSFileListBeanInfoTestcase
extends Testcase {



    // Constants.



    // Private data.
    private boolean         runningOnOS400_;



/**
Constructor.
**/
    public RIFSFileListBeanInfoTestcase (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RIFSFileListBeanInfoTestcase",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        // See if we're running on OS/400.
        if (System.getProperty("os.name").indexOf("OS/400") >= 0) {
          runningOnOS400_ = true;
        }

    }



/**
getAdditionalBeanInfo().
**/
    public void Var001()
    {
        try {
            RIFSFileListBeanInfo ubi = new RIFSFileListBeanInfo();
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
            RIFSFileListBeanInfo ubi = new RIFSFileListBeanInfo();
            BeanDescriptor bd = ubi.getBeanDescriptor();
            assertCondition ((bd.getBeanClass() == RIFSFileList.class) && (bd.getCustomizerClass() == null));
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
            RIFSFileListBeanInfo ubi = new RIFSFileListBeanInfo();
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
            RIFSFileListBeanInfo ubi = new RIFSFileListBeanInfo();
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
            RIFSFileListBeanInfo ubi = new RIFSFileListBeanInfo();
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
        if (onAS400_) {
          notApplicable("no GUI"); // need AWT in the environment for this variation
          return;
        }
        try {
            RIFSFileListBeanInfo ubi = new RIFSFileListBeanInfo();
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
        if (onAS400_) {
          notApplicable("no GUI"); // need AWT in the environment for this variation
          return;
        }
        try {
            RIFSFileListBeanInfo ubi = new RIFSFileListBeanInfo();
            Image icon = ubi.getIcon(BeanInfo.ICON_COLOR_16x16);
            // Note: jt400Native jar file has fewer gif's.
            assertCondition (runningOnOS400_ || icon != null);
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
        if (onAS400_) {
          notApplicable("no GUI"); // need AWT in the environment for this variation
          return;
        }
        try {
            RIFSFileListBeanInfo ubi = new RIFSFileListBeanInfo();
            Image icon = ubi.getIcon(BeanInfo.ICON_COLOR_32x32);
            // Note: jt400Native jar file has fewer gif's.
            assertCondition (runningOnOS400_ || icon != null);
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
        if (onAS400_) {
          notApplicable("no GUI"); // need AWT in the environment for this variation
          return;
        }
        try {
            RIFSFileListBeanInfo ubi = new RIFSFileListBeanInfo();
            Image icon = ubi.getIcon(BeanInfo.ICON_MONO_16x16);
            // Note: jt400Native jar file has fewer gif's.
            assertCondition (runningOnOS400_ || icon != null);
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
        if (onAS400_) {
          notApplicable("no GUI"); // need AWT in the environment for this variation
          return;
        }
        try {
            RIFSFileListBeanInfo ubi = new RIFSFileListBeanInfo();
            Image icon = ubi.getIcon(BeanInfo.ICON_MONO_32x32);
            // Note: jt400Native jar file has fewer gif's.
            assertCondition (runningOnOS400_ || icon != null);
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
            RIFSFileListBeanInfo ubi = new RIFSFileListBeanInfo();
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
            RIFSFileListBeanInfo ubi = new RIFSFileListBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            PropertyDescriptor[] pd1 = ubi.getPropertyDescriptors();
            PropertyDescriptor[] pd2 = abi[0].getPropertyDescriptors();
            assertCondition ((pd1.length == 1) 
                    && (pd1[0].getName().equals("path"))
                    && (pd2.length == 1)
                    && (pd2[0].getName().equals("system")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




