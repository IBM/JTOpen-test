///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NetServerBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.NetServer;

import java.io.FileOutputStream;

import java.util.Hashtable;
import java.util.Vector;
import java.beans.FeatureDescriptor;
import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.NetServerBeanInfo;

import test.Testcase;

/**
NetServerBeanInfoTestcase.  Verify the following public interfaces:
<ul>
<li>NetServerBeanInfo()
   <ul>
   <li>NetServerBeanInfo::getBeanDescriptor()
   <li>NetServerBeanInfo::getDefaultEventIndex()
   <li>NetServerBeanInfo::getDefaultPropertyIndex()
   <li>NetServerBeanInfo::getEventSetDescriptors()
   <li>NetServerBeanInfo::getXIcon(int)
   <li>NetServerBeanInfo::getPropertyDescriptors()
   </ul>
</ul>
**/
@SuppressWarnings("deprecation")
public class NetServerBeanInfoTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NetServerBeanInfoTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.INetServerTest.main(newArgs); 
   }

   /**
    Constructor.  This is called from the NetServerTest::createTestcases method.
   **/
   public NetServerBeanInfoTestcase(AS400 systemObject,
                                          Hashtable<String,Vector<String>> variationsToRun,
                                          int runMode,
                                          FileOutputStream fileOutputStream)
   {
      super(systemObject, "NetServerBeanInfoTestcase", variationsToRun,
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

   protected void setup() throws Exception {
     lockSystem("NETSVR", 600);
     super.setup();
   }

   protected void cleanup() throws Exception {
     unlockSystem();
     super.cleanup();

   }

   /**
   Verify the constructor, NetServerBeanInfo().
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
         NetServerBeanInfo bi = new NetServerBeanInfo();
         assertCondition(true, "bi created ="+bi); 
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
   }

   /**
   Verify NetServerBeanInfo::getBeanDescriptor().
   <br>
   Expected results:
   <ul>
   <li>BeanDescriptor.getBeanClass() will return
   "com.ibm.as400.util.html.NetServer".
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
         NetServerBeanInfo bi = new NetServerBeanInfo();
         BeanDescriptor bd = bi.getBeanDescriptor();
         if (!bd.getBeanClass().getName().equals("com.ibm.as400.access.NetServer") || bd.getCustomizerClass() != null)
         {
            failed("getBeanDescriptor");
            return;
         }
         if (!verifyFeatureDescriptor(bd, "NetServer", "NetServer", false, false))
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
   Verify NetServerBeanInfo::getDefaultEventIndex().
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
         NetServerBeanInfo bi = new NetServerBeanInfo();
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
   Verify NetServerBeanInfo::getDefaultPropertyIndex().
   <br>
   Expected results:
   <ul>
   <li>-1 will be returned.
   </ul>
   **/
   public void Var004()
   {
      try
      {
         NetServerBeanInfo bi = new NetServerBeanInfo();
         int index = bi.getDefaultPropertyIndex();
         if (index != -1)
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
   Verify NetServerBeanInfo::getEventSetDescriptors().
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
         NetServerBeanInfo bi = new NetServerBeanInfo();
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
   Verify NetServerBeanInfo::getPropertyDescriptors().
   <br>
   Expected results:
   <ul>
   <li>An array of size 0 will be returned.
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
         NetServerBeanInfo bi = new NetServerBeanInfo();
         PropertyDescriptor[] pd = bi.getPropertyDescriptors();
         if (pd != null && pd.length != 0)
         {
            failed("Wrong number of property descriptors returned: " + String.valueOf(pd.length));
            return;
         }
         /*if (pd[0].getPropertyEditorClass() != null ||
             pd[1].getPropertyEditorClass() != null ||
             pd[2].getPropertyEditorClass() != null)
         {
            failed("Property editor class not null");
            return;
         }
         if (!pd[0].getPropertyType().getName().equals("java.lang.String") ||
             !pd[1].getPropertyType().getName().equals("java.lang.String") ||
             !pd[2].getPropertyType().getName().equals("java.lang.String"))
         {
            failed("Wrong property types returned: " +
                   pd[0].getPropertyType().toString() + ", " +
                   pd[1].getPropertyType().toString() + ", " +
                   pd[2].getPropertyType().toString());
            return;
         }
         if (!pd[0].getReadMethod().getName().equals("getAlign") ||
             !pd[1].getReadMethod().getName().equals("getLanguage") ||
             !pd[2].getReadMethod().getName().equals("getDirection"))
         {
            failed("Wrong read methods: " +
                   pd[0].getReadMethod().getName() + ", " +
                   pd[1].getReadMethod().getName() + ", " +
                   pd[2].getReadMethod().getName());
            return;
         }
         if (!pd[0].getWriteMethod().getName().equals("setAlign") ||
             !pd[1].getWriteMethod().getName().equals("setLanguage") ||
             !pd[2].getWriteMethod().getName().equals("setDirection"))
         {
            failed("Wrong write methods: " +
                   pd[0].getWriteMethod().getName() + ", " +
                   pd[1].getWriteMethod().getName() + ", " +
                   pd[2].getWriteMethod().getName());
            return;
         }
         if (!pd[0].isBound() || !pd[1].isBound() || !pd[2].isBound())
         {
            failed("Bound problem");
            return;
         }
         if (pd[0].isConstrained() || pd[1].isConstrained() || pd[2].isConstrained())
         {
            failed("Constrained problem");
            return;
         }
         if (!verifyFeatureDescriptor(pd[0], "align", "The horizontal alignment for a block of HTML.", false, false))
         {
            return; // failed msg issued from verifyFeatureDescriptor()
         }
         if (!verifyFeatureDescriptor(pd[1], "language", "The primary language to be used by the contents of the element.", false, false))
         {
            return; // failed msg issued from verifyFeatureDescriptor()
         }
         if (!verifyFeatureDescriptor(pd[2], "direction", "The direction of the text interpretation.", false, false))
         {
            return; // failed msg issued from verifyFeatureDescriptor()
         }
         */
         
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }


   /**
   Verify NetServerBeanInfo::getXIcon(int).
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
         // NetServerBeanInfo bi = new NetServerBeanInfo();
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
