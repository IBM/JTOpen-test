///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ProgramParameterBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.misc;

import com.ibm.as400.access.ProgramParameterBeanInfo;

import test.Testcase;

/**
 The ProgramParameterBeanInfoTestcase class tests the methods of ProgramParameterBeanInfo.

 <p>This tests the following ProgramParameterBeanInfo methods:
 <ul>
 <li>getBeanDescriptor()
 <li>getDefaultEventIndex()
 <li>getDefaultPropertyIndex()
 <li>getEventSetDescriptors()
 <li>getIcon()
 <li>getPropertyDescriptors()
 </ul>
 **/
public class ProgramParameterBeanInfoTestcase extends Testcase
{

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
    }

    /**
     Method tested:ProgramParameterBeanInfo().
     -Ensure that the default constructor runs well.
     **/
    public void Var001()
    {
        try {
            ProgramParameterBeanInfo f = new ProgramParameterBeanInfo();
            assertCondition(true, "created "+f); 
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getBeanDescriptor().
     -Ensure that getBeanDescriptor() returns bean descriptor.
     **/
    public void Var002()
    {
        try {
            ProgramParameterBeanInfo f = new ProgramParameterBeanInfo();
            assertCondition(f.getBeanDescriptor() != null);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getDefaultEventIndex().
     -Ensure that getDefaultEventIndex() always returns 1.
     **/
    public void Var003()
    {
        try {
            ProgramParameterBeanInfo f = new ProgramParameterBeanInfo();
            assertCondition(f.getDefaultEventIndex() == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getDefaultPropertyIndex().
     -Ensure that getDefaultPropertIndex() always returns 0.
     **/
    public void Var004()
    {
        try {
            ProgramParameterBeanInfo f = new ProgramParameterBeanInfo();
            assertCondition(f.getDefaultPropertyIndex() == 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getEventSetDescriptors().
     -Ensure that getEventSetDescriptors() returns the descriptors for all events.
     **/
    public void Var005()
    {
        try {
            ProgramParameterBeanInfo f = new ProgramParameterBeanInfo();
            assertCondition(f.getEventSetDescriptors().length > 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     Method tested:getIcon().
     -Ensure that getIcon() returns a color icon when size 32 is specifed.
     **/
    public void Var006()
    {
        if (onAS400_)
        {
            notApplicable("No GUI for icon test.");
            return;
        }
        try
        {
            ProgramParameterBeanInfo f = new ProgramParameterBeanInfo();
            // Icons / GUI components no longer available in JTOpen 20.0.X

            assertCondition(true, "created "+f); 
        }
        catch (Error e)
        {
            failed("Unexpected Exception");
            e.printStackTrace();
        }
    }

    /**
     Method tested:getIcon().
     -Ensure that getIcon() returns a color icon when size 16 is specifed.
     **/
    public void Var007()
    {
        if (onAS400_)
        {
            notApplicable("No GUI for icon test.");
            return;
        }
        try
        {
            ProgramParameterBeanInfo f = new ProgramParameterBeanInfo();
            // Icons / GUI components no longer available in JTOpen 20.0.X
            assertCondition(true, "created "+f); 
        }
        catch (Error e)
        {
            failed("Unexpected Exception");
            e.printStackTrace();
        }
    }

    /**
     Method tested:getIcon().
     - Ensure that getIcon() returns a monochrome icon when size 32 is specifed.
     **/
    public void Var008()
    {
        if (onAS400_)
        {
            notApplicable("No GUI for icon test.");
            return;
        }
        try
        {
            ProgramParameterBeanInfo f = new ProgramParameterBeanInfo();
            assertCondition(true, "created "+f); 
        }
        catch (Error e)
        {
            failed("Unexpected Exception");
            e.printStackTrace();
        }
    }

    /**
     Method tested:getIcon().
     - Ensure that getIcon() returns a monochrome icon when size 16 is specifed.
     **/
    public void Var009()
    {
        if (onAS400_)
        {
            notApplicable("No GUI for icon test.");
            return;
        }
        try
        {
            ProgramParameterBeanInfo f = new ProgramParameterBeanInfo();
            assertCondition(true, "created "+f); 
        }
        catch (Error e)
        {
            failed("Unexpected Exception");
            e.printStackTrace();
        }
    }

    /**
     Method tested:getIcon().
     -Ensure that getIcon() returns null when an invalid size is specifed.
     **/
    public void Var010()
    {
        try {
            ProgramParameterBeanInfo f = new ProgramParameterBeanInfo();
            assertCondition(f.getIcon(-1) == null);
        }
        catch (Error e) {
            failed("Unexpected Exception");
            e.printStackTrace();
        }
    }

    /**
     Method tested:getPropertyDescriptors().
     -Ensure that getPropertyDescriptors() returns the descriptors for all properties.
     **/
    public void Var011()
    {
        try {
            ProgramParameterBeanInfo f = new ProgramParameterBeanInfo();
            assertCondition(f.getPropertyDescriptors().length > 0);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }
}
