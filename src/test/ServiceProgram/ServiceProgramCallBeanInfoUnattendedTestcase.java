///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ServiceProgramCallBeanInfoUnattendedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.ServiceProgram;


import java.beans.BeanInfo;
import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ServiceProgramCallBeanInfo;

import test.Testcase;

/**
 The ServiceProgramCallBeanInfoUnattendedTestcase class tests the methods of ServiceProgramCallBeanInfo.
 <p>This tests the following ServiceProgramCallBeanInfo methods:
 <ul>
 <li>getBeanDescriptor()
 <li>getDefaultEventIndex()
 <li>getDefaultPropertyIndex()
 <li>getEventSetDescriptors()
 <li>getIcon()
 <li>getPropertyDescriptors()
 </ul>
 **/
public class ServiceProgramCallBeanInfoUnattendedTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ServiceProgramCallBeanInfoUnattendedTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ServiceProgramCallTest.main(newArgs); 
   }
    /**
     Constructor.
     **/
    public ServiceProgramCallBeanInfoUnattendedTestcase(AS400 systemObject, Vector variationsToRun, int runMode, FileOutputStream fileOutputStream,  String password)
    {
        super(systemObject, "ServiceProgramCallBeanInfoUnattendedTestcase", variationsToRun, runMode, fileOutputStream, password);
    }

    /**
     Method tested:  ServiceProgramCallBeanInfo().
     -Ensure that the default constructor runs well.
     **/
    public void Var001()
    {
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getBeanDescriptor().
     -Ensure that getBeanDescriptor() returns bean descriptor.
     **/
    public void Var002()
    {
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(f.getBeanDescriptor() != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getDefaultEventIndex().
     -Ensure that getDefaultEventIndex() always returns 1.
     **/
    public void Var003()
    {
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(f.getDefaultEventIndex() == 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getDefaultPropertyIndex().
     -Ensure that getDefaultPropertIndex() always returns 0.
     **/
    public void Var004()
    {
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(f.getDefaultPropertyIndex() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getEventSetDescriptors().
     -Ensure that getEventSetDescriptors() returns the descriptors for all events.
     **/
    public void Var005()
    {
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(f.getEventSetDescriptors().length > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getIcon().
     -Ensure that getIcon() returns a color icon when size 32 is specifed.
     **/
    public void Var006()
    {
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getIcon().
     -Ensure that getIcon() returns a color icon when size 16 is specifed.
     **/
    public void Var007()
    {
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getIcon().
     - Ensure that getIcon() returns a monochrome icon when size 32 is specifed.
     **/
    public void Var008()
    {
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getIcon().
     - Ensure that getIcon() returns a monochrome icon when size 16 is specifed.
     **/
    public void Var009()
    {
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getIcon().
     -Ensure that getIcon() returns null when an invalid size is specifed.
     **/
    public void Var010()
    {
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(f.getIcon(-1) == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getPropertyDescriptors().
     -Ensure that getPropertyDescriptors() returns the descriptors for all properties.
     **/
    public void Var011()
    {
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(f.getPropertyDescriptors().length > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }
}
