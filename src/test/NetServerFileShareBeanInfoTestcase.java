///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NetServerFileShareBeanInfoTestcase.java
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
import java.util.Hashtable;
import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;

import test.Testcase;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.NetServerFileShare;
import com.ibm.as400.access.NetServerFileShareBeanInfo;

/**
NetServerFileShareBeanInfoTestcase.  Verify the following public interfaces:
<ul>
<li>NetServerFileShareBeanInfo()
   <ul>
   <li>NetServerFileShareBeanInfo::getBeanDescriptor()
   <li>NetServerFileShareBeanInfo::getDefaultEventIndex()
   <li>NetServerFileShareBeanInfo::getDefaultPropertyIndex()
   <li>NetServerFileShareBeanInfo::getEventSetDescriptors()
   <li>NetServerFileShareBeanInfo::getIcon(int)
   <li>NetServerFileShareBeanInfo::getPropertyDescriptors()
   </ul>
</ul>
**/
public class NetServerFileShareBeanInfoTestcase extends Testcase
{

   /**
    Constructor.  This is called from the NetServerFileShareTest::createTestcases method.
   **/
   public NetServerFileShareBeanInfoTestcase(AS400 systemObject,
                                          Hashtable variationsToRun,
                                          int runMode,
                                          FileOutputStream fileOutputStream)
   {
      super(systemObject, "NetServerFileShareBeanInfoTestcase", variationsToRun,
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
   Verify the constructor, NetServerFileShareBeanInfo().
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
         NetServerFileShareBeanInfo bi = new NetServerFileShareBeanInfo();
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }

   /**
   Verify NetServerFileShareBeanInfo::getBeanDescriptor().
   <br>
   Expected results:
   <ul>
   <li>BeanDescriptor.getBeanClass() will return
   "com.ibm.as400.util.html.NetServerFileShare".
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
         NetServerFileShareBeanInfo bi = new NetServerFileShareBeanInfo();
         BeanDescriptor bd = bi.getBeanDescriptor();
         if (!bd.getBeanClass().getName().equals("com.ibm.as400.access.NetServerFileShare") || bd.getCustomizerClass() != null)
         {
            failed("getBeanDescriptor");
            return;
         }
         if (!verifyFeatureDescriptor(bd, "NetServerFileShare", "NetServerFileShare", false, false))
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
   Verify NetServerFileShareBeanInfo::getDefaultEventIndex().
   <br>
   Expected results:
   <ul>
   <li>-1 will be returned.
   </ul>
   **/
   public void Var003()
   {
      try
      {
         NetServerFileShareBeanInfo bi = new NetServerFileShareBeanInfo();
         int index = bi.getDefaultEventIndex();
         if (index != -1)
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
   Verify NetServerFileShareBeanInfo::getDefaultPropertyIndex().
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
         NetServerFileShareBeanInfo bi = new NetServerFileShareBeanInfo();
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
   Verify NetServerFileShareBeanInfo::getEventSetDescriptors().
   <br>
   Expected results:
   <ul>
   <li>A NULL array will be returned.
   </ul>
   **/
   public void Var005()
   {
      try
      {
         NetServerFileShareBeanInfo bi = new NetServerFileShareBeanInfo();
         EventSetDescriptor[] ed = bi.getEventSetDescriptors();
         if (ed != null)
         {
            failed("Event descriptors were not NULL");
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
   Verify NetServerFileShareBeanInfo::getPropertyDescriptors().
   <br>
   Expected results:
   <ul>
   <li>An array of size 1 will be returned.
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
         NetServerFileShareBeanInfo bi = new NetServerFileShareBeanInfo();
         PropertyDescriptor[] pd = bi.getPropertyDescriptors();
         if (pd.length != 1)
         {
            failed("Wrong number of property descriptors returned: " + String.valueOf(pd.length));
            return;
         }
         if (pd[0].getPropertyEditorClass() != null)
         {
            failed("Property editor class not null");
            return;
         }
         if (!pd[0].getPropertyType().getName().equals("java.lang.String"))
         {
            failed("Wrong property types returned: " + pd[0].getPropertyType().toString());
            return;
         }
         if (!pd[0].getReadMethod().getName().equals("getName"))
         {
            failed("Wrong read methods: " + pd[0].getReadMethod().getName());
            return;
         }
         if (!pd[0].getWriteMethod().getName().equals("setName"))
         {
            failed("Wrong write methods: " + pd[0].getWriteMethod().getName());
            return;
         }
         if (!pd[0].isBound())
         {
            failed("Bound problem");
            return;
         }
         if (pd[0].isConstrained())
         {
            failed("Constrained problem");
            return;
         }
         if (!verifyFeatureDescriptor(pd[0], "name", "The name of the object.", false, false))
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
   Verify NetServerFileShareBeanInfo::getIcon(int).
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
         NetServerFileShareBeanInfo bi = new NetServerFileShareBeanInfo();
         Image ic = bi.getIcon(BeanInfo.ICON_MONO_16x16);
         if (ic == null)
         {
            /*failed("Null image returned. Probably the .gif is not created yet or not in your path.  Check that .gif exists and is in correct path.");
            return;
            */
         }
         ic = bi.getIcon(BeanInfo.ICON_COLOR_16x16);
         if (ic == null)
         {  /*
            failed("Null image returned.");
            return;
            */
         }
         ic = bi.getIcon(BeanInfo.ICON_MONO_32x32);
         if (ic == null)
         {  /*
            failed("Null image returned.");
            return;
            */
         }
         ic = bi.getIcon(BeanInfo.ICON_COLOR_32x32);
         if (ic == null)
         {  /*
            failed("Null image returned.");
            return;
            */
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

