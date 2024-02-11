///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CommandListBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Cmd;

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
import com.ibm.as400.access.CommandListBeanInfo;

/**
CommandListBeanInfoTestcase.  Verify the following public interfaces:
<ul>
<li>CommandListBeanInfo()
   <ul>
   <li>CommandListBeanInfo::getBeanDescriptor()
   <li>CommandListBeanInfo::getDefaultEventIndex()
   <li>CommandListBeanInfo::getDefaultPropertyIndex()
   <li>CommandListBeanInfo::getEventSetDescriptors()
   <li>CommandListBeanInfo::getXIcon(int)
   <li>CommandListBeanInfo::getPropertyDescriptors()
   </ul>
</ul>
**/
public class CommandListBeanInfoTestcase extends Testcase
{

   /**
    Constructor.  This is called from the CommandListTest::createTestcases method.
   **/
   public CommandListBeanInfoTestcase(AS400 systemObject,
                                          Vector variationsToRun,
                                          int runMode,
                                          FileOutputStream fileOutputStream)
   {
      super(systemObject, "CommandListBeanInfoTestcase", variationsToRun,
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
   Verify the constructor, CommandListBeanInfo().
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
         CommandListBeanInfo bi = new CommandListBeanInfo();
      }
      catch(Throwable e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }

   /**
   Verify CommandListBeanInfo::getBeanDescriptor().
   <br>
   Expected results:
   <ul>
   <li>BeanDescriptor.getBeanClass() will return
   "com.ibm.as400.access.CommandList".
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
         CommandListBeanInfo bi = new CommandListBeanInfo();
         BeanDescriptor bd = bi.getBeanDescriptor();
         if (!bd.getBeanClass().getName().equals("com.ibm.as400.access.CommandList") || bd.getCustomizerClass() != null)
         {
            failed("getBeanDescriptor");
            return;
         }
         if (!verifyFeatureDescriptor(bd, "CommandList", "CommandList", false, false))
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
   Verify CommandListBeanInfo::getDefaultEventIndex().
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
         CommandListBeanInfo bi = new CommandListBeanInfo();
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
   Verify CommandListBeanInfo::getDefaultPropertyIndex().
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
         CommandListBeanInfo bi = new CommandListBeanInfo();
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
   Verify CommandListBeanInfo::getEventSetDescriptors().
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
         CommandListBeanInfo bi = new CommandListBeanInfo();
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
   Verify CommandListBeanInfo::getPropertyDescriptors().
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
         CommandListBeanInfo bi = new CommandListBeanInfo();
         PropertyDescriptor[] pd = bi.getPropertyDescriptors();
         if (pd.length != 3)
         {
            failed("Wrong number of property descriptors returned: " + String.valueOf(pd.length));
            return;
         }
         if (pd[0].getPropertyEditorClass() != null ||
             pd[1].getPropertyEditorClass() != null ||
             pd[2].getPropertyEditorClass() != null)
         {
            failed("Property editor class not null");
            return;
         }
         if (!pd[0].getPropertyType().getName().equals("java.lang.String") ||
             !pd[1].getPropertyType().getName().equals("com.ibm.as400.access.AS400") ||
             !pd[2].getPropertyType().getName().equals("java.lang.String"))
         {
            failed("Wrong property types returned: " +
                   pd[0].getPropertyType().toString());
            return;
         }
         if (!pd[0].getReadMethod().getName().equals("getCommand") ||
             !pd[1].getReadMethod().getName().equals("getSystem") ||
             !pd[2].getReadMethod().getName().equals("getLibrary") )
         {
            failed("Wrong read methods: " +
                   pd[0].getReadMethod().getName());
            return;
         }
         if (!pd[0].getWriteMethod().getName().equals("setCommand") ||
             !pd[1].getWriteMethod().getName().equals("setSystem") ||
             !pd[2].getWriteMethod().getName().equals("setLibrary") )
         {
            failed("Wrong write methods: " +
                   pd[0].getWriteMethod().getName());
            return;
         }
         if (!pd[0].isBound() || !pd[1].isBound() || !pd[2].isBound() )
         {
            failed("Bound problem");
            return;
         }
         if (pd[0].isConstrained() || pd[1].isConstrained() || pd[2].isConstrained() )
         {
            failed("Constrained problem");
            return;
         }
         if (!verifyFeatureDescriptor(pd[0], "command", "Command string", false, false))
         {
            return; // failed msg issued from verifyFeatureDescriptor()
         }
         if (!verifyFeatureDescriptor(pd[1], "system", "The system on which the object resides.", false, false))
         {
            return; // failed msg issued from verifyFeatureDescriptor()
         }
         if (!verifyFeatureDescriptor(pd[2], "Library", "The name of the library in which this object resides.", false, false))
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
   Verify CommandListBeanInfo::getXIcon(int).
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
         CommandListBeanInfo bi = new CommandListBeanInfo();
         // Icons / GUI components no longer available in JTOpen 20.0.X

      }
      catch(AWTError e)
      {
        if (onAS400_) notApplicable("No GUI available.");
        else failed(e, "Unexpected exception");
        return;
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }


   /**
   Verify CommandListBeanInfo::getAdditionalBeanInfo().
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
         CommandListBeanInfo bi = new CommandListBeanInfo();
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

