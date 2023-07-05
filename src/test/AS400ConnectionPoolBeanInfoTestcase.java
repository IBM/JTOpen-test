///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400ConnectionPoolBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.FeatureDescriptor;
import java.beans.MethodDescriptor;

import com.ibm.as400.access.AS400ConnectionPoolBeanInfo;

/**
  Testcase AS400ConnectionPoolBeanInfoTestcase.
 **/
public class AS400ConnectionPoolBeanInfoTestcase extends Testcase
{

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
   Verify the constructor, AS400ConnectionPoolBeanInfo().
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
         AS400ConnectionPoolBeanInfo bi = new AS400ConnectionPoolBeanInfo();
         assertCondition(bi != null); 
      }
      catch(Throwable e)
      {
         failed(e, "Unexpected exception");
         return;
      }
   }

   /**
   Verify AS400ConnectionPoolBeanInfo::getBeanDescriptor().
   <br>
   Expected results:
   <ul>
   <li>BeanDescriptor.getBeanClass() will return
   "com.ibm.as400.AS400ConnectionPool".
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
         AS400ConnectionPoolBeanInfo bi = new AS400ConnectionPoolBeanInfo();
         BeanDescriptor bd = bi.getBeanDescriptor();
         if (!bd.getBeanClass().getName().equals("com.ibm.as400.access.AS400ConnectionPool") || bd.getCustomizerClass() != null)
         {
            failed("getBeanDescriptor");
            return;
         }
         if (!verifyFeatureDescriptor(bd, "AS400ConnectionPool", "AS400ConnectionPool", false, false))
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
   Verify AS400ConnectionPoolBeanInfo::getDefaultEventIndex().
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
         AS400ConnectionPoolBeanInfo bi = new AS400ConnectionPoolBeanInfo();
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
   Verify AS400ConnectionPoolBeanInfo::getDefaultPropertyIndex().
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
         AS400ConnectionPoolBeanInfo bi = new AS400ConnectionPoolBeanInfo();
	 int index = bi.getDefaultPropertyIndex();
         if (index != -1)    //@A2C Undeleted these lines and changed to -1.
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
   Verify AS400ConnectionPoolBeanInfo::getEventSetDescriptors().
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
         AS400ConnectionPoolBeanInfo bi = new AS400ConnectionPoolBeanInfo();
         EventSetDescriptor[] ed = bi.getEventSetDescriptors();
         if (ed.length != 1)
         {
            failed("Wrong number of event descriptors returned: " + String.valueOf(ed.length));
            return;
         }
         if (!ed[0].getAddListenerMethod().getName().equals("addPropertyChangeListener"))
         {
            failed("Wrong add listener method returned: " +
                   ed[0].getAddListenerMethod().getName());
            return;
         }
         MethodDescriptor[] md0 = ed[0].getListenerMethodDescriptors();
         if (md0.length != 1)
         {
            failed("Wrong number of listener method descriptors returned: " +
                   String.valueOf(md0.length));
            return;
         }
         if (!md0[0].getMethod().getName().equals("propertyChange"))
         {
            failed("Wrong methods returned from the method descriptors from getListenerMethodDescriptors: " +
                   md0[0].getMethod().getName());
            return;
         }
         if (ed[0].getListenerMethods().length != 1)
         {
            failed("Wrong number of methods returned from getListenerMethods: " +
                   String.valueOf(ed[0].getListenerMethods().length));
            return;
         }
         if (!ed[0].getListenerMethods()[0].getName().equals("propertyChange"))
         {
            failed("Wrong methods returned from getListenerMethods: " +
                   ed[0].getListenerMethods()[0].getName());
            return;
         }
         if (!ed[0].getListenerType().getName().equals("java.beans.PropertyChangeListener"))
         {
            failed("Wrong listener type returned: " +
                   ed[0].getListenerType().getName());
            return;
         }
         if (!ed[0].getRemoveListenerMethod().getName().equals("removePropertyChangeListener"))
         {
            failed("Wrong Remove listener method returned: " +
                   ed[0].getRemoveListenerMethod().getName());
            return;
         }
         if (!ed[0].isInDefaultEventSet())
         {
            failed("Wrong value returned from isInDefaultEventSet: " +
                   String.valueOf(ed[0].isInDefaultEventSet()));
            return;
         }
         if (ed[0].isUnicast())
         {
            failed("Wrong value returned from isInDefaultEventSet: " +
                   String.valueOf(ed[0].isUnicast()));
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
   Verify AS400ConnectionPoolBeanInfo::getIcon(int).
   <br>
   Expected results:
   <ul>
   <li>No exceptions occur when any of the valid constants are provided.
   <li>Null is returned when an invalid constant is provded.
   </ul>
   **/
   public void Var006()
   {
      try
      {
         AS400ConnectionPoolBeanInfo bi = new AS400ConnectionPoolBeanInfo();
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
}

