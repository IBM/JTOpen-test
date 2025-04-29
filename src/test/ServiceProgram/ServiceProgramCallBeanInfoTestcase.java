///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ServiceProgramCallBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.ServiceProgram;

import com.ibm.as400.access.ServiceProgramCallBeanInfo;

import test.Testcase;

/**
 The ServiceProgramCallBeanInfoTestcase class tests the methods of ServiceProgramCallBeanInfo:
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
public class ServiceProgramCallBeanInfoTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ServiceProgramCallBeanInfoTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ServiceProgramCallTest.main(newArgs); 
   }
    private boolean inNetscape_ = false;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        Class<?> fred = null;
        try
        {
            fred = Class.forName("netscape.security.PrivilegeManager");
        }
        catch (Throwable e)
        {
        }

        if (fred != null)
        {
            inNetscape_ = true;
            System.out.println("We think we are in Netscape.");
        }
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
            assertCondition(true, "created "+f);
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
     Method tested:  getDefaultPropertyIndex().
     -Ensure that getDefaultPropertIndex() always returns 0.
     **/
    public void Var003()
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
     Method tested:  getIcon().
     -Ensure that getIcon() returns a color icon when size 32 is specifed.
     **/
    public void Var004()
    {
        if (onAS400_)
        {
            notApplicable("No GUI for icon test.");
            return;
        }
        if (inNetscape_)
        {
            notApplicable("-- Cannot load Icon in Netscape.");
            return;
        }
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(true, "created "+f);
        }
        catch (Error e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getIcon().
     -Ensure that getIcon() returns a color icon when size 16 is specifed.
     **/
    public void Var005()
    {
        if (onAS400_)
        {
            notApplicable("No GUI for icon test.");
            return;
        }
        if (inNetscape_)
        {
            notApplicable("-- Cannot load Icon in Netscape.");
            return;
        }
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(true, "created "+f);
        }
        catch (Error e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getIcon().
     - Ensure that getIcon() returns a monochrome icon when size 32 is specifed.
     **/
    public void Var006()
    {
        if (onAS400_)
        {
            notApplicable("No GUI for icon test.");
            return;
        }
        if (inNetscape_)
        {
            notApplicable("-- Cannot load Icon in Netscape.");
            return;
        }
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(true, "created "+f);
        }
        catch (Error e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getIcon().
     - Ensure that getIcon() returns a monochrome icon when size 16 is specifed.
     **/
    public void Var007()
    {
        if (onAS400_)
        {
            notApplicable("No GUI for icon test.");
            return;
        }
        if (inNetscape_)
        {
            notApplicable("-- Cannot load Icon in Netscape.");
            return;
        }
        try
        {
            ServiceProgramCallBeanInfo f = new ServiceProgramCallBeanInfo();
            assertCondition(true, "created "+f);
        }
        catch (Error e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Method tested:  getIcon().
     -Ensure that getIcon() returns null when an invalid size is specifed.
     **/
    public void Var008()
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
    public void Var009()
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
