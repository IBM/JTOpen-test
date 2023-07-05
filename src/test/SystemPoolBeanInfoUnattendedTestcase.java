///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SystemPoolBeanInfoUnattendedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.beans.BeanInfo;

import com.ibm.as400.access.SystemPoolBeanInfo;

/**
 The SystemPoolBeanInfoUnattendedTestcase class tests the methods of SystemPoolBeanInfo.
 <p>This tests the following SystemPoolBeanInfo methods:
 <ul>
 <li>getBeanDescriptor()
 <li>getDefaultEventIndex()
 <li>getDefaultPropertyIndex()
 <li>getEventSetDescriptors()
 <li>getIcon()
 <li>getPropertyDescriptors()
 </ul>
 **/
public class SystemPoolBeanInfoUnattendedTestcase extends Testcase
{
    /**
     Method tested:SystemPoolBeanInfo().
     -Ensure that the default constructor runs well.
     **/
    public void Var001()
    {
        try
        {
            SystemPoolBeanInfo f = new SystemPoolBeanInfo();
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
            SystemPoolBeanInfo f = new SystemPoolBeanInfo();
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
            SystemPoolBeanInfo f = new SystemPoolBeanInfo();
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
            SystemPoolBeanInfo f = new SystemPoolBeanInfo();
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
            SystemPoolBeanInfo f = new SystemPoolBeanInfo();
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
            SystemPoolBeanInfo f = new SystemPoolBeanInfo();
            if (onAS400_)
            {
                notApplicable("No GUI for icon test.");
            }
            else
            {
                assertCondition(f.getIcon(BeanInfo.ICON_COLOR_32x32) != null);
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
            SystemPoolBeanInfo f = new SystemPoolBeanInfo();
            if (onAS400_)
            {
                notApplicable("No GUI for icon test.");
            }
            else
            {
                assertCondition(f.getIcon(BeanInfo.ICON_COLOR_16x16) != null);
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
            SystemPoolBeanInfo f = new SystemPoolBeanInfo();
            if (onAS400_)
            {
                notApplicable("No GUI for icon test.");
            }
            else
            {
                assertCondition(f.getIcon(BeanInfo.ICON_MONO_32x32) != null);
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
            SystemPoolBeanInfo f = new SystemPoolBeanInfo();
            if (onAS400_)
            {
                notApplicable("No GUI for icon test.");
            }
            else
            {
                assertCondition(f.getIcon(BeanInfo.ICON_MONO_16x16) != null);
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
            SystemPoolBeanInfo f = new SystemPoolBeanInfo();
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
            SystemPoolBeanInfo f = new SystemPoolBeanInfo();
            assertCondition(f.getPropertyDescriptors().length > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
