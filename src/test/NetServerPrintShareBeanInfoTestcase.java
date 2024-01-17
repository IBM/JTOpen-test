///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NetServerPrintShareBeanInfoTestcase.java
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
import com.ibm.as400.access.NetServerPrintShare;
import com.ibm.as400.access.NetServerPrintShareBeanInfo;

/**
NetServerPrintShareBeanInfoTestcase.  Verify the following public interfaces:
<ul>
<li>NetServerPrintShareBeanInfo()
   <ul>
   <li>NetServerPrintShareBeanInfo::getBeanDescriptor()
   <li>NetServerPrintShareBeanInfo::getDefaultEventIndex()
   <li>NetServerPrintShareBeanInfo::getDefaultPropertyIndex()
   <li>NetServerPrintShareBeanInfo::getEventSetDescriptors()
   <li>NetServerPrintShareBeanInfo::getIcon(int)
   <li>NetServerPrintShareBeanInfo::getPropertyDescriptors()
   </ul>
</ul>
**/
public class NetServerPrintShareBeanInfoTestcase extends Testcase
{

   /**
    Constructor.  This is called from the NetServerPrintShareTest::createTestcases method.
   **/
   public NetServerPrintShareBeanInfoTestcase(AS400 systemObject,
                                          Hashtable variationsToRun,
                                          int runMode,
                                          FileOutputStream fileOutputStream)
   {
      super(systemObject, "NetServerPrintShareBeanInfoTestcase", variationsToRun,
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
   Verify the constructor, NetServerPrintShareBeanInfo().
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
         NetServerPrintShareBeanInfo bi = new NetServerPrintShareBeanInfo();
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }

   /**
   Verify NetServerPrintShareBeanInfo::getBeanDescriptor().
   <br>
   Expected results:
   <ul>
   <li>BeanDescriptor.getBeanClass() will return
   "com.ibm.as400.util.html.NetServerPrintShare".
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
         NetServerPrintShareBeanInfo bi = new NetServerPrintShareBeanInfo();
         BeanDescriptor bd = bi.getBeanDescriptor();
         if (!bd.getBeanClass().getName().equals("com.ibm.as400.access.NetServerPrintShare") || bd.getCustomizerClass() != null)
         {
            failed("getBeanDescriptor");
            return;
         }
         if (!verifyFeatureDescriptor(bd, "NetServerPrintShare", "NetServerPrintShare", false, false))
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
   Verify NetServerPrintShareBeanInfo::getDefaultEventIndex().
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
         NetServerPrintShareBeanInfo bi = new NetServerPrintShareBeanInfo();
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
   Verify NetServerPrintShareBeanInfo::getDefaultPropertyIndex().
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
         NetServerPrintShareBeanInfo bi = new NetServerPrintShareBeanInfo();
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
   Verify NetServerPrintShareBeanInfo::getEventSetDescriptors().
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
         NetServerPrintShareBeanInfo bi = new NetServerPrintShareBeanInfo();
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
   Verify NetServerPrintShareBeanInfo::getPropertyDescriptors().
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
         NetServerPrintShareBeanInfo bi = new NetServerPrintShareBeanInfo();
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
   Verify NetServerPrintShareBeanInfo::getIcon(int).
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
         NetServerPrintShareBeanInfo bi = new NetServerPrintShareBeanInfo();
         // Icons / GUI components no longer available in JTOpen 20.0.X
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }

}


