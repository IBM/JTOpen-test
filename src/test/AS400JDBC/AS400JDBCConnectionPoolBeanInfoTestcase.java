///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCConnectionPoolBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.AS400JDBC;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.FeatureDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnectionPoolBeanInfo;

import test.Testcase;

/**
  Testcase AS400JDBCConnectionPoolBeanInfoTestcase.
 **/
public class AS400JDBCConnectionPoolBeanInfoTestcase extends Testcase
{
   // private AS400JDBCConnectionPool pool_;

   /**
     Constructor.  This is called from the AS400JDBCConnectionPoolTest constructor.
    **/
   public AS400JDBCConnectionPoolBeanInfoTestcase(AS400 systemObject,
                                 Vector variationsToRun,
                                 int runMode,
                                 FileOutputStream fileOutputStream,
                                 String password)
   {
      super(systemObject, "AS400JDBCConnectionPoolBeanInfoTestcase", variationsToRun,
            runMode, fileOutputStream,  password);
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
   Verify the constructor, AS400JDBCConnectionPoolBeanInfo().
   <br>
   Expected results:
   <ul>
   <li>The bean info object will be constructed..
   </ul>
   **/
   public void Var001()
   {
     AS400JDBCConnectionPoolBeanInfo bi = null; 
      try
      {
         bi = new AS400JDBCConnectionPoolBeanInfo();
      }
      catch(Throwable e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      assertCondition(true, "bi="+bi); 
   }

   /**
   Verify AS400JDBCConnectionPoolBeanInfo::getBeanDescriptor().
   <br>
   Expected results:
   <ul>
   <li>BeanDescriptor.getBeanClass() will return
   "com.ibm.as400.AS400JDBCConnectionPool".
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
         AS400JDBCConnectionPoolBeanInfo bi = new AS400JDBCConnectionPoolBeanInfo();
         BeanDescriptor bd = bi.getBeanDescriptor();
         if (!bd.getBeanClass().getName().equals("com.ibm.as400.access.AS400JDBCConnectionPool") || bd.getCustomizerClass() != null)
         {
            failed("getBeanDescriptor");
            return;
         }
         if (!verifyFeatureDescriptor(bd, "AS400JDBCConnectionPool", "AS400JDBCConnectionPool", false, false))
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
   Verify AS400JDBCConnectionPoolBeanInfo::getDefaultEventIndex().
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
         AS400JDBCConnectionPoolBeanInfo bi = new AS400JDBCConnectionPoolBeanInfo();
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
   Verify AS400JDBCConnectionPoolBeanInfo::getDefaultPropertyIndex().
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
         AS400JDBCConnectionPoolBeanInfo bi = new AS400JDBCConnectionPoolBeanInfo();
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
   Verify AS400JDBCConnectionPoolBeanInfo::getEventSetDescriptors().
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
         AS400JDBCConnectionPoolBeanInfo bi = new AS400JDBCConnectionPoolBeanInfo();
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
   Verify AS400JDBCConnectionPoolBeanInfo::getXIcon(int).
   <br>
   Expected results:
   <ul>
   <li>No exceptions occur when any of the valid constants are provided.
   <li>Null is returned when an invalid constant is provded.
   </ul>
   **/
   public void Var006()
   {
       // This is no longer tested
        assertCondition(true);
   }
   
   /**
   Verify AS400JDBCConnectionPoolBeanInfo::getPropertyDescriptors().
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
   public void Var007()
   {
      try
      {
         AS400JDBCConnectionPoolBeanInfo bi = new AS400JDBCConnectionPoolBeanInfo();
         PropertyDescriptor[] pd = bi.getPropertyDescriptors();
         if (pd.length != 1)
         {
            failed("Wrong number of property descriptors returned: " + String.valueOf(pd.length));
            return;
         }

         for (int i=0; i< pd.length; i++) 
         {
            if (pd[i].getPropertyEditorClass() != null) 
            {
               failed("Property editor class not null");
               return;
            }
         }

         Properties propertyTypes = new Properties();
         propertyTypes.put("dataSource", "com.ibm.as400.access.AS400JDBCConnectionPoolDataSource");

         for (int i=0; i< pd.length; i++) 
         {
            String value = (String)propertyTypes.get(pd[i].getName());
            if (!pd[i].getPropertyType().getName().equals(value)) 
            {
               failed("Wrong property types returned: [" + i + "] " + pd[0].getPropertyType().toString());
               return;            
            }
         }

         propertyTypes.put("dataSource", "getDataSource");

         for (int i=0; i< pd.length; i++) 
         {
            if (pd[i].getName().equals("password"))         // password.
            {
               if (pd[i].getReadMethod() != null) 
               {
                  failed("Wrong read method: " + pd[i].getReadMethod().getName());
                  return;
               }
            }
            else 
            {
               String value = (String)propertyTypes.get(pd[i].getName());
               if (!pd[i].getReadMethod().getName().equals(value))
               {
                  failed("Wrong read method: " + pd[i].getReadMethod().getName());
                  return;
               }
            }
         }

         propertyTypes.put("dataSource", "setDataSource");

         for (int i=0; i< pd.length; i++) 
         {
            String value = (String)propertyTypes.get(pd[i].getName());
            if (!pd[i].getWriteMethod().getName().equals(value))
            {
               failed("Wrong write method: " + pd[i].getWriteMethod().getName());
               return;
            }
         }

         for (int i=0; i< pd.length; i++) 
         {
            if (!pd[i].isBound()) 
            {
               failed("Bound problem");
               return;
            }
         }
         
         for (int i=0; i< pd.length; i++) 
         {
            if (pd[i].isConstrained()) 
            {
               failed("Constrained problem");
               return;
            }
         }

         // Set the property short descriptions to validate.
         propertyTypes.put("dataSource", "The data source used to make JDBC connections.");

         for (int i=0; i< pd.length; i++) 
         {
            if (!verifyFeatureDescriptor(pd[i], pd[i].getName(), (String)propertyTypes.get(pd[i].getName()), false, false))
               return;
         }
         succeeded();
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
   }
   
}

