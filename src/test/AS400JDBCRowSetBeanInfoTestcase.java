
///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCRowSetBeanInfoTestcase.java
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyVetoException;
import java.beans.SimpleBeanInfo;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Properties;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCRowSet;
import com.ibm.as400.access.AS400JDBCRowSetBeanInfo;
import com.ibm.as400.access.ExtendedIllegalArgumentException;

/**
  Testcase AS400JDBCRowSetBeanInfoTestcase.
 **/
public class AS400JDBCRowSetBeanInfoTestcase extends Testcase
{
   private AS400JDBCRowSet rowset_;

   /**
     Constructor.  This is called from the AS400JDBCConnectionPoolTest constructor.
    **/
   public AS400JDBCRowSetBeanInfoTestcase(AS400 systemObject,
                                          Vector variationsToRun,
                                          int runMode,
                                          FileOutputStream fileOutputStream,
                                          
                                          String password)
   {
      super(systemObject, "AS400JDBCRowSetBeanInfoTestcase", variationsToRun,
            runMode, fileOutputStream, password);
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
   Verify the constructor, AS400JDBCRowSetBeanInfo().
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
         AS400JDBCRowSetBeanInfo bi = new AS400JDBCRowSetBeanInfo();
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }

   /**
   Verify AS400JDBCRowSetBeanInfo::getBeanDescriptor().
   <br>
   Expected results:
   <ul>
   <li>BeanDescriptor.getBeanClass() will return
   "com.ibm.as400.AS400JDBCRowSet".
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
         AS400JDBCRowSetBeanInfo bi = new AS400JDBCRowSetBeanInfo();
         BeanDescriptor bd = bi.getBeanDescriptor();
         if (!bd.getBeanClass().getName().equals("com.ibm.as400.access.AS400JDBCRowSet") || bd.getCustomizerClass() != null)
         {
            failed("getBeanDescriptor");
            return;
         }
         if (!verifyFeatureDescriptor(bd, "AS400JDBCRowSet", "AS400JDBCRowSet", false, false))
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
   Verify AS400JDBCRowSetBeanInfo::getDefaultEventIndex().
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
         AS400JDBCRowSetBeanInfo bi = new AS400JDBCRowSetBeanInfo();
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
   Verify AS400JDBCRowSetBeanInfo::getDefaultPropertyIndex().
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
         AS400JDBCRowSetBeanInfo bi = new AS400JDBCRowSetBeanInfo();
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
   Verify AS400JDBCRowSetBeanInfo::getEventSetDescriptors().
   <br>
   Expected results:
   <ul>
   <li>An array of size 2 will be returned.
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
         AS400JDBCRowSetBeanInfo bi = new AS400JDBCRowSetBeanInfo();
         EventSetDescriptor[] ed = bi.getEventSetDescriptors();
         if (ed.length != 1)
         {
            failed("Wrong number of event descriptors returned: " + String.valueOf(ed.length));
            return;
         }
         if (!ed[0].getAddListenerMethod().getName().equals("addPropertyChangeListener"))
         {
            failed("Wrong add listener method returned: " + ed[0].getAddListenerMethod().getName());
            return;
         }
         MethodDescriptor[] md0 = ed[0].getListenerMethodDescriptors();
         if (md0.length != 1)
         {
            failed("Wrong number of listener method descriptors returned: " + String.valueOf(md0.length));
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
            failed("Wrong number of methods returned from getListenerMethods: " + String.valueOf(ed[0].getListenerMethods().length));
            return;
         }
         if (!ed[0].getListenerMethods()[0].getName().equals("propertyChange"))
         {
            failed("Wrong methods returned from getListenerMethods: " + ed[0].getListenerMethods()[0].getName());
            return;
         }
         if (!ed[0].getListenerType().getName().equals("java.beans.PropertyChangeListener"))
         {
            failed("Wrong listener type returned: " + ed[0].getListenerType().getName());
            return;
         }
         if (!ed[0].isInDefaultEventSet())
         {
            failed("Wrong value returned from isInDefaultEventSet: " + String.valueOf(ed[0].isInDefaultEventSet()));
            return;
         }
         if (ed[0].isUnicast())
         {
            failed("Wrong value returned from isInDefaultEventSet: " + String.valueOf(ed[0].isUnicast()));
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
   Verify AS400JDBCRowSetBeanInfo::getIcon(int).
   <br>
   Expected results:
   <ul>
   <li>No exceptions occur when any of the valid constants are provided.
   <li>Null is returned when an invalid constant is provded.
   </ul>
   **/
   public void Var006()
   {

       if (System.getProperty("os.name").indexOf("400") >= 0) {
	   notApplicable("Icon testcase not working on OS/400");
	   return; 
       } 


      try
      {
         AS400JDBCRowSetBeanInfo bi = new AS400JDBCRowSetBeanInfo();
         Image ic = bi.getIcon(BeanInfo.ICON_MONO_16x16);
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
   Verify AS400JDBCRowSetBeanInfo::getPropertyDescriptors().
   <br>
   Expected results:
   <ul>
   <li>An array of size 6 will be returned.
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
         AS400JDBCRowSetBeanInfo bi = new AS400JDBCRowSetBeanInfo();
         PropertyDescriptor[] pd = bi.getPropertyDescriptors();
         if (pd.length != 16)
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
         propertyTypes.put("command", "java.lang.String");
         propertyTypes.put("concurrency", "int");
         propertyTypes.put("dataSourceName", "java.lang.String");
         propertyTypes.put("escapeProcessing", "boolean");
         propertyTypes.put("fetchDirection", "int");
         propertyTypes.put("fetchSize", "int");
         propertyTypes.put("maxFieldSize", "int");
         propertyTypes.put("maxRows", "int");
         propertyTypes.put("password", "java.lang.String");
         propertyTypes.put("queryTimeout", "int");
         propertyTypes.put("readOnly", "boolean");
         propertyTypes.put("transactionIsolation", "int");
         propertyTypes.put("type", "int");
         propertyTypes.put("url", "java.lang.String");
         propertyTypes.put("useDataSource", "boolean");
         propertyTypes.put("username", "java.lang.String");

         for (int i=0; i< pd.length; i++) 
         {
            String value = (String)propertyTypes.get(pd[i].getName());
            if (!pd[i].getPropertyType().getName().equals(value)) 
            {
               failed("Wrong property types returned: [" + i + "] " + pd[0].getPropertyType().toString());
               return;            
            }
         }

         propertyTypes.put("command", "getCommand");
         propertyTypes.put("concurrency", "getConcurrency");
         propertyTypes.put("dataSourceName", "getDataSourceName");
         propertyTypes.put("escapeProcessing", "getEscapeProcessing");
         propertyTypes.put("fetchDirection", "getFetchDirection");
         propertyTypes.put("fetchSize", "getFetchSize");
         propertyTypes.put("maxFieldSize", "getMaxFieldSize");
         propertyTypes.put("maxRows", "getMaxRows");
         propertyTypes.put("password", "getPassword");
         propertyTypes.put("queryTimeout", "getQueryTimeout");
         propertyTypes.put("readOnly", "isReadOnly");
         propertyTypes.put("transactionIsolation", "getTransactionIsolation");
         propertyTypes.put("type", "getType");
         propertyTypes.put("url", "getUrl");
         propertyTypes.put("useDataSource", "isUseDataSource");
         propertyTypes.put("username", "getUsername");

         for (int i=0; i< pd.length; i++) 
         {
            String value = (String)propertyTypes.get(pd[i].getName());
            if (!pd[i].getReadMethod().getName().equals(value))
            {
               failed("Wrong read method: " + pd[i].getReadMethod().getName());
               return;
            }
         }

         propertyTypes.put("command", "setCommand");
         propertyTypes.put("concurrency", "setConcurrency");
         propertyTypes.put("dataSourceName", "setDataSourceName");
         propertyTypes.put("escapeProcessing", "setEscapeProcessing");
         propertyTypes.put("fetchDirection", "setFetchDirection");
         propertyTypes.put("fetchSize", "setFetchSize");
         propertyTypes.put("maxFieldSize", "setMaxFieldSize");
         propertyTypes.put("maxRows", "setMaxRows");
         propertyTypes.put("password", "setPassword");
         propertyTypes.put("queryTimeout", "setQueryTimeout");
         propertyTypes.put("readOnly", "setReadOnly");
         propertyTypes.put("transactionIsolation", "setTransactionIsolation");
         propertyTypes.put("type", "setType");
         propertyTypes.put("url", "setUrl");
         propertyTypes.put("useDataSource", "setUseDataSource");
         propertyTypes.put("username", "setUsername");

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
         propertyTypes.put("command", "Specifies the command used to populate the rowset.");
         propertyTypes.put("concurrency", "Specifies the result set concurrency type.");
         propertyTypes.put("dataSourceName", "Specifies the name of the data source.");
         propertyTypes.put("escapeProcessing", "Specifies whether the escape scanning is enabled for escape substitution processing.");
         propertyTypes.put("fetchDirection", "Specifies the direction in which the rows in a result set are processed.");
         propertyTypes.put("fetchSize", "Specifies the number of rows to be fetched from the database.");
         propertyTypes.put("maxFieldSize", "Specifies the maximum column size for the database field.");
         propertyTypes.put("maxRows", "Specifies the maximum row limit for the rowset.");
         propertyTypes.put("password", "Specifies the password for connecting to the system.");	 //@A1C @B1C
         propertyTypes.put("queryTimeout", "Specifies the maximum wait time in seconds for the statement to execute.");
         propertyTypes.put("readOnly", "Specifies whether the rowset is read-only.");
         propertyTypes.put("transactionIsolation", "Specifies the default transaction isolation.");
         propertyTypes.put("type", "Specifies the result set type.");
         propertyTypes.put("url", "Specifies the URL used for getting a connection.");
         propertyTypes.put("useDataSource", "Specifies whether the data source is used to make a connection to the database.");
         propertyTypes.put("username", "Specifies the user name for connecting to the system.");  //@A1C @B1C

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
