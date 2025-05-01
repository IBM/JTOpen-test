///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RIFSFileBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RIFS;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.VetoableChangeListener;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.RIFSFile;
import com.ibm.as400.resource.RIFSFileBeanInfo;
import com.ibm.as400.resource.ResourceListener;

import test.Testcase;



/**
Testcase RIFSFileBeanInfoTestcase.  This tests the methods
of the RIFSFileBeanInfo class:
**/
@SuppressWarnings("deprecation")
public class RIFSFileBeanInfoTestcase
extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RIFSFileBeanInfoTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.RIFSTest.main(newArgs); 
   }



    // Constants.



    // Private data.



/**
Constructor.
**/
    public RIFSFileBeanInfoTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RIFSFileBeanInfoTestcase",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
getAdditionalBeanInfo().
**/
    public void Var001()
    {
        try {
            RIFSFileBeanInfo ubi = new RIFSFileBeanInfo();
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
            RIFSFileBeanInfo ubi = new RIFSFileBeanInfo();
            BeanDescriptor bd = ubi.getBeanDescriptor();
            assertCondition ((bd.getBeanClass() == RIFSFile.class) && (bd.getCustomizerClass() == null));
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
            RIFSFileBeanInfo ubi = new RIFSFileBeanInfo();
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
            RIFSFileBeanInfo ubi = new RIFSFileBeanInfo();
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
            RIFSFileBeanInfo ubi = new RIFSFileBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            EventSetDescriptor[] esd = abi[0].getEventSetDescriptors();
            assertCondition ((ubi.getEventSetDescriptors() == null) 
                    && (esd.length == 3)
                    && (esd[0].getListenerType() == PropertyChangeListener.class)
                    && (esd[1].getListenerType() == ResourceListener.class)
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
            RIFSFileBeanInfo ubi = new RIFSFileBeanInfo();
            Image icon = ubi.getIcon(-546);
            failed ("Didn't throw exception"+icon);
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
            RIFSFileBeanInfo ubi = new RIFSFileBeanInfo();
            assertCondition (true, "ubi="+ubi);
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
            RIFSFileBeanInfo ubi = new RIFSFileBeanInfo();
            assertCondition (true, "ubi="+ubi);
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
            RIFSFileBeanInfo ubi = new RIFSFileBeanInfo();
            assertCondition (true, "ubi="+ubi);
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
            RIFSFileBeanInfo ubi = new RIFSFileBeanInfo();
            assertCondition (true, "ubi="+ubi);
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
            RIFSFileBeanInfo ubi = new RIFSFileBeanInfo();
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
            RIFSFileBeanInfo ubi = new RIFSFileBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            PropertyDescriptor[] pd1 = ubi.getPropertyDescriptors();
            PropertyDescriptor[] pd2 = abi[0].getPropertyDescriptors();
            assertCondition ((pd1.length == 1) 
                    && (pd2.length == 1)
                    && (pd2[0].getName().equals("system"))
                    && (pd1[0].getName().equals("path")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




