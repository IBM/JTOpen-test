///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CommandBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.awt.Image;
import java.awt.AWTError;
import java.util.Vector;
import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;

import test.Testcase;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandBeanInfo;

/**
CommandBeanInfoTestcase.  Verify the following public interfaces:
<ul>
<li>CommandBeanInfo()
   <ul>
   <li>CommandBeanInfo::getBeanDescriptor()
   <li>CommandBeanInfo::getDefaultEventIndex()
   <li>CommandBeanInfo::getDefaultPropertyIndex()
   <li>CommandBeanInfo::getEventSetDescriptors()
   <li>CommandBeanInfo::getIcon(int)
   <li>CommandBeanInfo::getPropertyDescriptors()
   </ul>
</ul>
**/
public class CommandBeanInfoTestcase extends Testcase
{

   /**
    Constructor.  This is called from the CommandTest::createTestcases method.
   **/
   public CommandBeanInfoTestcase(AS400 systemObject,
                                          Vector variationsToRun,
                                          int runMode,
                                          FileOutputStream fileOutputStream)
   {
      super(systemObject, "CommandBeanInfoTestcase", variationsToRun,
            runMode, fileOutputStream);
   }
   
   private boolean verifyFeatureDescriptor(FeatureDescriptor fd, String dspName, String shortDesc, boolean exp, boolean hid)
   {
      if (!fd.getDisplayName().equals(dspName))
      {
         failed("Wrong display name: " + fd.getDisplayName());
         return false;
      }
      if (!fd.getShortDescription().equals(shortDesc))
      {
         failed("Wrong short description: " + fd.getShortDescription());
         return false;
      }
      if (fd.isExpert() != exp)
      {
         failed("Wrong isExpert(): " + String.valueOf(fd.isExpert()));
         return false;
      }
      if (fd.isHidden() != hid)
      {
         failed("Wrong isHidden(): " + String.valueOf(fd.isHidden()));
         return false;
      }
      return true;
   }
   
   /**
   Verify the constructor, CommandBeanInfo().
   <br>
   Expected results:
   <ul>
   <li>The bean info object will be constructed..
   </ul>
   **/
   public void Var001()
   {
      try
      {
         CommandBeanInfo bi = new CommandBeanInfo();
      }
      catch(Throwable e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }

   /**
   Verify CommandBeanInfo::getBeanDescriptor().
   <br>
   Expected results:
   <ul>
   <li>BeanDescriptor.getBeanClass() will return
   "com.ibm.as400.access.Command".
   <li>BeanDescriptor.getCustomizerClass() will return null.
   <li>The FeatureDescriptor methods will return correct information:
   <ul>
   <li>getDisplayName will return the display name for the object.
   <li>getShortDescription will return the short description for the object.
   <li>isExpert() will return false.
   <li>isHidden() will return false.
   </ul>
   </ul>
   **/
   public void Var002()
   {
      try
      {
         CommandBeanInfo bi = new CommandBeanInfo();
         BeanDescriptor bd = bi.getBeanDescriptor();
         if (!bd.getBeanClass().getName().equals("com.ibm.as400.access.Command") || bd.getCustomizerClass() != null)
         {
            failed("getBeanDescriptor");
            return;
         }
         if (!verifyFeatureDescriptor(bd, "Command", "Command", false, false))
         {
            return; // failed msg issued from verifyFeatureDescriptor()
         }
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }

   /**
   Verify CommandBeanInfo::getDefaultEventIndex().
   <br>
   Expected results:
   <ul>
   <li>0 will be returned.
   </ul>
   **/
   public void Var003()
   {
      try
      {
         CommandBeanInfo bi = new CommandBeanInfo();
         int index = bi.getDefaultEventIndex();
         if (index != 0)
         {
            failed("Wrong default event index returned: " + String.valueOf(index));
            return;
         }
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }
   
   /**
   Verify CommandBeanInfo::getDefaultPropertyIndex().
   <br>
   Expected results:
   <ul>
   <li>0 will be returned.
   </ul>
   **/
   public void Var004()
   {
      try
      {
         CommandBeanInfo bi = new CommandBeanInfo();
         int index = bi.getDefaultPropertyIndex();
         if (index != 0)
         {
            failed("Wrong default property index returned: " + String.valueOf(index));
            return;
         }
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }
   
   /**
   Verify CommandBeanInfo::getEventSetDescriptors().
   <br>
   Expected results:
   <ul>
   <li>An array of size 1 will be returned.
   <li>The appropriate EventDescriptor.getAddListenerMethod() will be returned
   for each EventDescriptor.
   <li>The appropriate EventDescriptor.getListenerMethodsDescriptors() will be
   returned for each EventDescriptor.
   <li>The appropriate EventDescriptor.getListenerMethods() will be
   returned for each EventDescriptor.
   <li>The appropriate EventDescriptor.getRemoveListenerMethod() will be returned
   for each EventDescriptor.
   <li>EventDescriptor.isInDefaultEventSet() will return true.
   <li>EventDescriptor.isUnicast() will return false.
   <li>The FeatureDescriptor methods will return correct information:
   <ul>
   <li>getDisplayName will return the display name for the object.
   <li>getShortDescription will return the short description for the object.
   <li>isExpert() will return false.
   <li>isHidden() will return false.
   </ul>
   </ul>
   </ul>
   **/
   public void Var005()
   {
      try
      {
         CommandBeanInfo bi = new CommandBeanInfo();
         EventSetDescriptor[] ed = bi.getEventSetDescriptors();
         if (ed.length != 1)
         {
            failed("Wrong number of event descriptors returned: " + String.valueOf(ed.length));
            return;
         }
         if (!ed[0].getAddListenerMethod().getName().equals("addPropertyChangeListener") )
         {
            failed("Wrong add listener method returned: " +
                   ed[0].getAddListenerMethod().getName() );
            return;
         }
         MethodDescriptor[] md0 = ed[0].getListenerMethodDescriptors();
         
         if (md0.length != 1 )
         {
            failed("Wrong number of listener method descriptors returned: " +
                   String.valueOf(md0.length) );
            return;
         }
         if (!md0[0].getMethod().getName().equals("propertyChange") )
         {
            failed("Wrong methods returned from the method descriptors from getListenerMethodDescriptors: " +
                   md0[0].getMethod().getName() );
            return;
         }
         if (ed[0].getListenerMethods().length != 1 )
         {
            failed("Wrong number of methods returned from getListenerMethods: " +
                   String.valueOf(ed[0].getListenerMethods().length) );
            return;
         }
         if (!ed[0].getListenerMethods()[0].getName().equals("propertyChange") )
         {
            failed("Wrong methods returned from getListenerMethods: " +
                   ed[0].getListenerMethods()[0].getName() );
            return;
         }
         if (!ed[0].getListenerType().getName().equals("java.beans.PropertyChangeListener") )
         {
            failed("Wrong listener type returned: " +
                   ed[0].getListenerType().getName() );
            return;
         }
         if (!ed[0].getRemoveListenerMethod().getName().equals("removePropertyChangeListener") )
         {
            failed("Wrong Remove listener method returned: " +
                   ed[0].getRemoveListenerMethod().getName() );
            return;
         }
         if (!ed[0].isInDefaultEventSet() )
         {
            failed("Wrong value returned from isInDefaultEventSet: " +
                   String.valueOf(ed[0].isInDefaultEventSet()) );
            return;
         }
         if (ed[0].isUnicast() )
         {
            failed("Wrong value returned from isInDefaultEventSet: " +
                   String.valueOf(ed[0].isUnicast()) );
            return;
         }
         if (!verifyFeatureDescriptor(ed[0], "propertyChange", "A bound property has changed.", false, false))
         {
            return; // failed msg issued from verifyFeatureDescriptor()
         }
         
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }

   
   
   /**
   Verify CommandBeanInfo::getPropertyDescriptors().
   <br>
   Expected results:
   <ul>
   <li>An array of size 2 will be returned.
   <li>The PropertyDescriptor.getPropertyEditorClass() will return correct information::
   <li>The PropertyDescriptor.getPropertyType() will return correct information:
   <li>The PropertyDescriptor.getReadMethod() will return correct information:
   <li>The PropertyDescriptor.getWriteMethod() will return correct information:
   <li>The PropertyDescriptor.isBound() will return true.
   <li>The PropertyDescriptor.isConstrained() will return true.
   <li>The FeatureDescriptor methods will return correct information:
   <ul>
   <li>getDisplayName will return the display name for the object.
   <li>getShortDescription will return the short description for the object.
   <li>isExpert() will return false.
   <li>isHidden() will return false.
   </ul>
   </ul>
   </ul>
   **/
   public void Var006()
   {
      try
      {
         CommandBeanInfo bi = new CommandBeanInfo();
         PropertyDescriptor[] pd = bi.getPropertyDescriptors();
         if (pd.length != 2)
         {
            failed("Wrong number of property descriptors returned: " + String.valueOf(pd.length));
            return;
         }
         if (pd[0].getPropertyEditorClass() != null ||
             pd[1].getPropertyEditorClass() != null)
         {
            failed("Property editor class not null");
            return;
         }
         if (!pd[0].getPropertyType().getName().equals("java.lang.String") ||
             !pd[1].getPropertyType().getName().equals("com.ibm.as400.access.AS400"))
         {
            failed("Wrong property types returned: " +
                   pd[0].getPropertyType().toString());
            return;
         }
         if (!pd[0].getReadMethod().getName().equals("getPath") ||
             !pd[1].getReadMethod().getName().equals("getSystem"))
         {
            failed("Wrong read methods: " +
                   pd[0].getReadMethod().getName());
            return;
         }
         if (!pd[0].getWriteMethod().getName().equals("setPath") ||
             !pd[1].getWriteMethod().getName().equals("setSystem"))
         {
            failed("Wrong write methods: " +
                   pd[0].getWriteMethod().getName());
            return;
         }
         if (!pd[0].isBound() || !pd[1].isBound())
         {
            failed("Bound problem");
            return;
         }
         if (pd[0].isConstrained() || pd[1].isConstrained())
         {
            failed("Constrained problem");
            return;
         }
         if (!verifyFeatureDescriptor(pd[0], "path", "The integrated file system name of the object.", false, false))
         {
            return; // failed msg issued from verifyFeatureDescriptor()
         }
         if (!verifyFeatureDescriptor(pd[1], "system", "The system on which the object resides."
                                      , false, false))
         {
            return; // failed msg issued from verifyFeatureDescriptor()
         }
         
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }


   /**
   Verify CommandBeanInfo::getIcon(int).
   <br>
   Expected results:
   <ul>
   <li>No exceptions occur when any of the valid constants are provided.
   <li>Null is returned when an invalid constant is provded.
   </ul>
   **/
   public void Var007()
   {
      try
      {
         CommandBeanInfo bi = new CommandBeanInfo();
         Image ic = null;
         try { ic = bi.getIcon(BeanInfo.ICON_MONO_16x16); }
         catch (Throwable e) {
           //e.printStackTrace();
           notApplicable("Unable to create instance of java.awt.Image.  Assuming that no GUI is available.");
           return;
         }
         if (ic == null)
         {
            failed("Null image returned. Probably the .gif is not created yet or not in your path.  Check that .gif exists and is in correct path.");
            return;
         }
         ic = bi.getIcon(BeanInfo.ICON_COLOR_16x16);
         if (ic == null)
         {
            failed("Null image returned.");
            return;
         }
         ic = bi.getIcon(BeanInfo.ICON_MONO_32x32);
         if (ic == null)
         {
            failed("Null image returned.");
            return;
         }
         ic = bi.getIcon(BeanInfo.ICON_COLOR_32x32);
         if (ic == null)
         {
            failed("Null image returned.");
            return;
         }
         ic = bi.getIcon(-22);
         if (ic != null)
         {
            failed("Null image not returned.");
            return;
         }
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }


   /**
   Verify CommandBeanInfo::getAdditionalBeanInfo().
   <br>
   Expected results:
   <ul>
   <li>Null object will be returned.
   </ul>
   **/
   public void Var008()             // @B1A
   {
      try
      {
         CommandBeanInfo bi = new CommandBeanInfo();
         BeanInfo[] bis = bi.getAdditionalBeanInfo();
         if (bis == null)
         {
             succeeded();
         }
         else
         {
             failed("Wrong additional BeanInfo");
         }
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
   }
}

