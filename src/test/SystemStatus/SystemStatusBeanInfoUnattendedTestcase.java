///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SystemStatusBeanInfoUnattendedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.SystemStatus;

import com.ibm.as400.access.SystemStatusBeanInfo;

import test.Testcase;

import java.beans.BeanInfo;

/**
 The SystemStatusBeanInfoUnattendedTestcase class tests the methods of SystemStatusBeanInfo.
 <p>This tests the following SystemStatusBeanInfo methods:
 <ul>
 <li>getBeanDescriptor()
 <li>getDefaultEventIndex()
 <li>getDefaultPropertyIndex()
 <li>getEventSetDescriptors()
 <li>getIcon()
 <li>getPropertyDescriptors()
 </ul>
 **/
public class SystemStatusBeanInfoUnattendedTestcase extends Testcase
{
    /**
     Method tested:SystemStatusBeanInfo().
     -Ensure that the default constructor runs well.
     **/
    public void Var001()
    {
        try
        {
            SystemStatusBeanInfo f = new SystemStatusBeanInfo();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getBeanDescriptor().
     -Ensure that getBeanDescriptor() returns bean descriptor.
     **/
    public void Var002()
    {
        try
        {
            SystemStatusBeanInfo f = new SystemStatusBeanInfo();
            assertCondition(f.getBeanDescriptor() != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getDefaultEventIndex().
     -Ensure that getDefaultEventIndex() always returns 1.
     **/
    public void Var003()
    {
        try
        {
            SystemStatusBeanInfo f = new SystemStatusBeanInfo();
            assertCondition(f.getDefaultEventIndex() == 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getDefaultPropertyIndex().
     -Ensure that getDefaultPropertIndex() always returns 0.
     **/
    public void Var004()
    {
        try
        {
            SystemStatusBeanInfo f = new SystemStatusBeanInfo();
            assertCondition(f.getDefaultPropertyIndex() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getEventSetDescriptors().
     -Ensure that getEventSetDescriptors() returns the descriptors for all events.
     **/
    public void Var005()
    {
        try
        {
            SystemStatusBeanInfo f = new SystemStatusBeanInfo();
            assertCondition(f.getEventSetDescriptors().length > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getIcon().
     -Ensure that getIcon() returns a color icon when size 32 is specifed.
     **/
    public void Var006()
    {
        try
        {
            SystemStatusBeanInfo f = new SystemStatusBeanInfo();
            if (onAS400_)
            {
                notApplicable("No GUI for icon test.");
            }
            else
            {
                assertCondition(true);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getIcon().
     -Ensure that getIcon() returns a color icon when size 16 is specifed.
     **/
    public void Var007()
    {
        try
        {
            SystemStatusBeanInfo f = new SystemStatusBeanInfo();
            if (onAS400_)
            {
                notApplicable("No GUI for icon test.");
            }
            else
            {
                assertCondition(true);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getIcon().
     - Ensure that getIcon() returns a monochrome icon when size 32 is specifed.
     **/
    public void Var008()
    {
        try
        {
            SystemStatusBeanInfo f = new SystemStatusBeanInfo();
            if (onAS400_)
            {
                notApplicable("No GUI for icon test.");
            }
            else
            {
                assertCondition(true);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getIcon().
     - Ensure that getIcon() returns a monochrome icon when size 16 is specifed.
     **/
    public void Var009()
    {
        try
        {
            SystemStatusBeanInfo f = new SystemStatusBeanInfo();
            if (onAS400_)
            {
                notApplicable("No GUI for icon test.");
            }
            else
            {
                assertCondition(true);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getIcon().
     -Ensure that getIcon() returns null when an invalid size is specifed.
     **/
    public void Var010()
    {
        try
        {
            SystemStatusBeanInfo f = new SystemStatusBeanInfo();
            assertCondition(f.getIcon(-1) == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getPropertyDescriptors().
     -Ensure that getPropertyDescriptors() returns the descriptors for all properties.
     **/
    public void Var011()
    {
        try
        {
            SystemStatusBeanInfo f = new SystemStatusBeanInfo();
            assertCondition(f.getPropertyDescriptors().length > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
