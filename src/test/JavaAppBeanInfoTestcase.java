///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JavaAppBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JavaAppBeanInfoTestcase.java
 //
 // Classes:      JavaAppBeanInfoTestcase
 //
 ////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.JavaApplicationCallBeanInfo;
import java.beans.BeanInfo;

/**
The JavaAppBeanInfoTestcase class tests the methods of JavaApplicationCallBeanInfo.

<p>This tests the following JavaApplicationCallBeanInfo methods:
<ul>
<li>getBeanDescriptor()
<li>getDefaultEventIndex()
<li>getDefaultPropertyIndex()
<li>getEventSetDescriptors()
<li>getIcon()
<li>getPropertyDescriptors()
</ul>
**/
public class JavaAppBeanInfoTestcase
extends Testcase
{

    // Private data.
    static final int    variations_ = 11;


/**
Runs the variations.
**/
    public void run ()
    {
        boolean allVariations = (variationsToRun_.size () == 0);

    

        if ((allVariations || variationsToRun_.contains ("1")) && runMode_ != ATTENDED) {
            setVariation (1);
            Var001 ();
        }

        if ((allVariations || variationsToRun_.contains ("2")) && runMode_ != ATTENDED) {
            setVariation (2);
            Var002 ();
        }

        if ((allVariations || variationsToRun_.contains ("3")) && runMode_ != ATTENDED) {
            setVariation (3);
            Var003 ();
        }

        if ((allVariations || variationsToRun_.contains ("4")) && runMode_ != ATTENDED) {
            setVariation (4);
            Var004 ();
        }

        if ((allVariations || variationsToRun_.contains ("5")) && runMode_ != ATTENDED) {
            setVariation (5);
            Var005 ();
        }

        if ((allVariations || variationsToRun_.contains ("6")) && runMode_ != ATTENDED) {
            setVariation (6);
            Var006 ();
        }

        if ((allVariations || variationsToRun_.contains ("7")) && runMode_ != ATTENDED) {
            setVariation (7);
            Var007 ();
        }

        if ((allVariations || variationsToRun_.contains ("8")) && runMode_ != ATTENDED) {
            setVariation (8);
            Var008 ();
        }
        if ((allVariations || variationsToRun_.contains ("9")) && runMode_ != ATTENDED) {
            setVariation (9);
            Var009 ();
        }

        if ((allVariations || variationsToRun_.contains ("10")) && runMode_ != ATTENDED) {
            setVariation (10);
            Var010 ();
        }

        if ((allVariations || variationsToRun_.contains ("11")) && runMode_ != ATTENDED) {
            setVariation (11);
            Var011 ();
        }
 }

/**
Method tested:JavaApplicationCallBeanInfo().
 -Ensure that the default constructor runs well.
**/
    public void Var001 ()
    {
        try {
            JavaApplicationCallBeanInfo f = new JavaApplicationCallBeanInfo();
            succeeded("bean info is "+f);
        }
        catch (Throwable t) {
            failed (t, "Unexpected Exception");
        }
    }

/**
Method tested:getBeanDescriptor().
 -Ensure that getBeanDescriptor() returns bean descriptor.
**/
    public void Var002 ()
    {
        try {
            JavaApplicationCallBeanInfo f = new JavaApplicationCallBeanInfo();
            assertCondition (f.getBeanDescriptor() != null);
        }
        catch (Throwable t) {
            failed (t, "Unexpected Exception");
        }
    }

/**
Method tested:getDefaultEventIndex().
 -Ensure that getDefaultEventIndex() always returns 1.
**/
   public void Var003 ()
    {
        try {
            JavaApplicationCallBeanInfo f = new JavaApplicationCallBeanInfo();
            assertCondition (f.getDefaultEventIndex() == 1);
            }
        catch (Throwable t) {
            failed (t, "Unexpected Exception");
        }
    }

/**
Method tested:getDefaultPropertyIndex().
 -Ensure that getDefaultPropertIndex() always returns 0.
**/
   public void Var004 ()
    {
        try {
            JavaApplicationCallBeanInfo f = new JavaApplicationCallBeanInfo();
            assertCondition (f.getDefaultPropertyIndex() == 0);
            }
        catch (Throwable t) {
            failed (t, "Unexpected Exception");
        }
    }

/**
Method tested:getEventSetDescriptors().
 -Ensure that getEventSetDescriptors() returns the descriptors for all events.
**/
    public void Var005 ()
    {
        try {
            JavaApplicationCallBeanInfo f = new JavaApplicationCallBeanInfo();
            assertCondition (f.getEventSetDescriptors().length > 0);
        }
        catch (Throwable t) {
            failed (t, "Unexpected Exception");
        }
    }

/**
Method tested:getIcon().
 -Ensure that getIcon() returns a color icon when size 32 is specifed.
**/
     public void Var006 ()
    {
       if (!JTOpenTestEnvironment.isGuiAvailable) {
          notApplicable("-- Cannot load Icon when running natively");
        }
        else
        {
           try
           {
              JavaApplicationCallBeanInfo f = new JavaApplicationCallBeanInfo();
              assertCondition (f.getIcon (BeanInfo.ICON_COLOR_32x32) != null);
           }
           catch (Error e)
           {
              failed ("Unexpected Exception");
	      e.printStackTrace();
           }
        }
    }

/**
Method tested:getIcon().
 -Ensure that getIcon() returns a color icon when size 16 is specifed.
**/
    public void Var007 ()
    {
        if (!JTOpenTestEnvironment.isGuiAvailable) {
          notApplicable("-- Cannot load Icon when running natively");
        }
        else
        {
           try
           {
              JavaApplicationCallBeanInfo f = new JavaApplicationCallBeanInfo();
              assertCondition (f.getIcon (BeanInfo.ICON_COLOR_16x16) != null);
           }
           catch (Error e)
           {
               failed ("Unexpected Exception");
	       e.printStackTrace();
           }
        }
    }

/**
Method tested:getIcon().
- Ensure that getIcon() returns a monochrome icon when size 32 is specifed.
**/
     public void Var008 ()
    {
        if (!JTOpenTestEnvironment.isGuiAvailable) {
          notApplicable("-- Cannot load Icon when running natively");
        }
        else
        {
           try
           {
              JavaApplicationCallBeanInfo f = new JavaApplicationCallBeanInfo();
              assertCondition (f.getIcon (BeanInfo.ICON_MONO_32x32) != null);
           }
           catch (Error e)
           {
               failed ("Unexpected Exception");
               e.printStackTrace();
	   }
        }
    }



/**
Method tested:getIcon().
- Ensure that getIcon() returns a monochrome icon when size 16 is specifed.
**/
    public void Var009 ()
    {
        if (!JTOpenTestEnvironment.isGuiAvailable) {
          notApplicable("-- Cannot load Icon when running natively");
        }
        else
        {
           try
           {
              JavaApplicationCallBeanInfo f = new JavaApplicationCallBeanInfo();
              assertCondition (f.getIcon (BeanInfo.ICON_MONO_16x16) != null);
           }
           catch (Error e)
           {
               failed ("Unexpected Exception");
               e.printStackTrace();
           }
        }
    }


/**
Method tested:getIcon().
 -Ensure that getIcon() returns null when an invalid size is specifed.
**/
    public void Var010 ()
    {
        try {
            JavaApplicationCallBeanInfo f = new JavaApplicationCallBeanInfo();
            assertCondition (f.getIcon (-1) == null);
        }
        catch (Throwable t) {
            failed ("Unexpected Exception");
            t.printStackTrace();
        }
    }

/**
Method tested:getPropertyDescriptors().
 -Ensure that getPropertyDescriptors() returns the descriptors for all properties.
**/
    public void Var011 ()
    {
        try {
            JavaApplicationCallBeanInfo f = new JavaApplicationCallBeanInfo();
            assertCondition (f.getPropertyDescriptors().length > 0);
        }
        catch (Throwable t) {
            failed (t, "Unexpected Exception");
        }
    }


}



