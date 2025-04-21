///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConnectionPoolBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.MiscAH;

import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.FeatureDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnectionPoolBeanInfo;
import com.ibm.as400.access.ConnectionPoolBeanInfo;

import test.Testcase;

/**
  Testcase AS400JDBCConnectionPoolBeanInfoTestcase.
 **/
public class ConnectionPoolBeanInfoTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConnectionPoolBeanInfoTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.AS400ConnectionPoolTest.main(newArgs); 
   }

   /**
     Constructor.  This is called from the AS400JDBCConnectionPoolTest constructor.
    **/
   public ConnectionPoolBeanInfoTestcase(AS400 systemObject,
                                 Vector<String> variationsToRun,
                                 int runMode,
                                 FileOutputStream fileOutputStream,
                                 String  password)
   {
      super(systemObject, "ConnectionPoolBeanInfoTestcase", variationsToRun,
            runMode, fileOutputStream, password);
   }


   private boolean verifyFeatureDescriptor(FeatureDescriptor fd, String dspName, String shortDesc, boolean exp, boolean hid, StringBuffer sb)
   {
      boolean passed=true; 
      if (!fd.getDisplayName().equals(dspName))
      {
         sb.append("Wrong display name: " + fd.getDisplayName()+"\n");
         passed = false;
      }
      if (!fd.getShortDescription().equals(shortDesc))
      {
         sb.append("Wrong short description: " + fd.getShortDescription()+"\n");
         passed = false;
      }
      if (fd.isExpert() != exp)
      {
         sb.append("Wrong isExpert(): " + String.valueOf(fd.isExpert())+"\n");
         passed = false;
      }
      if (fd.isHidden() != hid)
      {
         sb.append("Wrong isHidden(): " + String.valueOf(fd.isHidden())+"\n");
         passed = false;
      }
      return passed;
   }

   /**
   Verify the constructor, ConnectionPoolBeanInfo().
   <br>
   Expected results:
   <ul>
   <li>The bean info object will be constructed..
   </ul>
   **/
   public void Var001()
   {
     ConnectionPoolBeanInfo bi=null; 
      try
      {
         AS400JDBCConnectionPoolBeanInfo cpbi = new AS400JDBCConnectionPoolBeanInfo();
         bi = (ConnectionPoolBeanInfo)cpbi.getAdditionalBeanInfo()[0];
         assertCondition(bi != null); 
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
   }

   /**
   Verify ConnectionPoolBeanInfo::getBeanDescriptor().
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
       StringBuffer sb = new StringBuffer();
       boolean passed=true; 
      try
      {
         AS400JDBCConnectionPoolBeanInfo cpbi = new AS400JDBCConnectionPoolBeanInfo();
         ConnectionPoolBeanInfo bi = (ConnectionPoolBeanInfo)cpbi.getAdditionalBeanInfo()[0];
         BeanDescriptor bd = bi.getBeanDescriptor();
         if (!bd.getBeanClass().getName().equals("com.ibm.as400.access.ConnectionPool") || bd.getCustomizerClass() != null)
         {
            failed("getBeanDescriptor");
            return;
         }
         if (!verifyFeatureDescriptor(bd, "ConnectionPool", "ConnectionPool", false, false, sb))
         {
            passed = false; 
         }
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      assertCondition(passed, sb.toString()); 
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
         AS400JDBCConnectionPoolBeanInfo cpbi = new AS400JDBCConnectionPoolBeanInfo();
         ConnectionPoolBeanInfo bi = (ConnectionPoolBeanInfo)cpbi.getAdditionalBeanInfo()[0];
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
         AS400JDBCConnectionPoolBeanInfo cpbi = new AS400JDBCConnectionPoolBeanInfo();
         ConnectionPoolBeanInfo bi = (ConnectionPoolBeanInfo)cpbi.getAdditionalBeanInfo()[0];
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
   Verify ConnectionPoolBeanInfo::getEventSetDescriptors().
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
       StringBuffer sb = new StringBuffer();
       boolean passed = true; 
      try
      {
         AS400JDBCConnectionPoolBeanInfo cpbi = new AS400JDBCConnectionPoolBeanInfo();
         ConnectionPoolBeanInfo bi = (ConnectionPoolBeanInfo)cpbi.getAdditionalBeanInfo()[0];
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
         if (!verifyFeatureDescriptor(ed[0], "propertyChange", "A bound property has changed.", false, false, sb))
         {
	     passed=false; 
         }
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      assertCondition(passed, sb.toString()); 


   }

   /**
   Verify ConnectionPoolBeanInfo::getPropertyDescriptors().
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
       StringBuffer sb = new StringBuffer();
       boolean passed = true; 
      try
      {
         AS400JDBCConnectionPoolBeanInfo cpbi = new AS400JDBCConnectionPoolBeanInfo();
         ConnectionPoolBeanInfo bi = (ConnectionPoolBeanInfo)cpbi.getAdditionalBeanInfo()[0];

         PropertyDescriptor[] pd = bi.getPropertyDescriptors();
         ///if (pd.length != 8)  //@A1C
         if (pd.length != 9)  //@A1C
         {
            sb.append("Wrong number of property descriptors returned: " + String.valueOf(pd.length)+" expected 9\n");
            passed=false; 
         }

         for (int i=0; i< pd.length; i++) 
         {
            if (pd[i].getPropertyEditorClass() != null) 
            {
               sb.append("Property editor class for "+i+" not null\n");
               passed=false; 
            }
         }

         Properties propertyTypes = new Properties();
         propertyTypes.put("runMaintenance", "boolean");
         propertyTypes.put("threadUsed", "boolean");
	 propertyTypes.put("cleanupInterval", "long");  //@A1A
	 propertyTypes.put("maxConnections", "int");    //@A1A
	 propertyTypes.put("maxInactivity", "long");    //@A1A
	 propertyTypes.put("maxLifetime", "long");      //@A1A
	 propertyTypes.put("maxUseCount", "int");       //@A1A
	 propertyTypes.put("maxUseTime", "long");       //@A1A
         propertyTypes.put("pretestConnections", "boolean");

         for (int i=0; i< pd.length; i++) 
         {
            String value = (String)propertyTypes.get(pd[i].getName());
            if (!pd[i].getPropertyType().getName().equals(value)) 
            {
               sb.append("Wrong property types returned: [" + i + "] " + pd[i].getPropertyType().toString()+"\n"); //@A1C
               passed=false;             
            }
         }

         propertyTypes.put("runMaintenance", "isRunMaintenance");
         propertyTypes.put("threadUsed", "isThreadUsed");
	 propertyTypes.put("cleanupInterval", "getCleanupInterval");  //@A1A
	 propertyTypes.put("maxConnections", "getMaxConnections");    //@A1A
	 propertyTypes.put("maxInactivity", "getMaxInactivity");      //@A1A
	 propertyTypes.put("maxLifetime", "getMaxLifetime");          //@A1A
	 propertyTypes.put("maxUseCount", "getMaxUseCount");          //@A1A
	 propertyTypes.put("maxUseTime", "getMaxUseTime");            //@A1A
         propertyTypes.put("pretestConnections", "isPretestConnections");

         for (int i=0; i< pd.length; i++) 
         {
            if (pd[i].getName().equals("password"))         // password.
            {
               if (pd[i].getReadMethod() != null) 
               {
                  sb.append("Wrong read method: " + pd[i].getReadMethod().getName()+"\n");
                  passed=false; 
               }
            }
            else 
            {
               String value = (String)propertyTypes.get(pd[i].getName());
               if (!pd[i].getReadMethod().getName().equals(value))
               {
                  sb.append("Wrong read method: " + pd[i].getReadMethod().getName()+"\n");
                  passed=false; 
               }
            }
         }

         propertyTypes.put("runMaintenance", "setRunMaintenance");
         propertyTypes.put("threadUsed", "setThreadUsed");
	 propertyTypes.put("cleanupInterval", "setCleanupInterval");  //@A1A
	 propertyTypes.put("maxConnections", "setMaxConnections");    //@A1A
	 propertyTypes.put("maxInactivity", "setMaxInactivity");      //@A1A
	 propertyTypes.put("maxLifetime", "setMaxLifetime");          //@A1A
	 propertyTypes.put("maxUseCount", "setMaxUseCount");          //@A1A
	 propertyTypes.put("maxUseTime", "setMaxUseTime");            //@A1A
         propertyTypes.put("pretestConnections", "setPretestConnections");

         for (int i=0; i< pd.length; i++) 
         {
            String value = (String)propertyTypes.get(pd[i].getName());
            if (!pd[i].getWriteMethod().getName().equals(value))
            {
               sb.append("Wrong write method: " + pd[i].getWriteMethod().getName()+"\n");
               passed=false; 
            }
         }

         for (int i=0; i< pd.length; i++) 
         {
            if (!pd[i].isBound()) 
            {
               sb.append("Bound problem for "+i+"\n");
               passed = false; 
            }
         }
         
         for (int i=0; i< pd.length; i++) 
         {
            if (pd[i].isConstrained()) 
            {
               sb.append("Constrained problem for "+i+"\n");
               passed=false; 
            }
         }

         // Set the property short descriptions to validate.
         propertyTypes.put("runMaintenance", "Specifies whether the maintenance daemon is used.");
         propertyTypes.put("threadUsed", "Specifies whether threads are used.");
	 propertyTypes.put("cleanupInterval", "The cleanup time interval for the connection pool.");   //@A1A
	 propertyTypes.put("maxConnections", "The maximum number of connections a pool can have.");    //@A1A
	 propertyTypes.put("maxInactivity", "The maximum amount of time a connection can be inactive."); //@A1A
	 propertyTypes.put("maxLifetime", "The maximum amount of time a connection can exist.");    //@A1A
	 propertyTypes.put("maxUseCount", "The maximum number of times a connection can be used."); //@A1A
	 propertyTypes.put("maxUseTime", "The maximum amount of time a connection can be used.");  //@A1A
         propertyTypes.put("pretestConnections", "Specifies whether connections are pretested.");

         for (int i=0; i< pd.length; i++) 
         {
            if (!verifyFeatureDescriptor(pd[i], pd[i].getName(), (String)propertyTypes.get(pd[i].getName()), false, false, sb))
               passed=false; 
         }
         assertCondition(passed, sb.toString());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
   }
   
}

