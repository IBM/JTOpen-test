///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCConnectionPoolDataSourceBeanInfoTestcase.java
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
import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.Properties;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnectionPoolDataSourceBeanInfo;

/**
  Testcase AS400JDBCConnectionPoolDataSourceBeanInfoTestcase.
 **/
public class AS400JDBCConnectionPoolDataSourceBeanInfoTestcase extends Testcase
{

   /**
     Constructor.  This is called from the AS400JDBCDataSourceTest constructor.
    **/
   public AS400JDBCConnectionPoolDataSourceBeanInfoTestcase(AS400 systemObject,
                                 Vector variationsToRun,
                                 int runMode,
                                 FileOutputStream fileOutputStream,
                                 
                                 String password)
   {
      super(systemObject, "AS400JDBCConnectionPoolDataSourceBeanInfoTestcase", variationsToRun,     //@A1C
            runMode, fileOutputStream, password);
   }


   private boolean verifyFeatureDescriptor(FeatureDescriptor fd, String dspName, String shortDesc, boolean exp, boolean hid) {
       StringBuffer sb = new StringBuffer(); 
       boolean passed = verifyFeatureDescriptor(fd, dspName, shortDesc, exp, hid, sb);
       if (!passed) {
	   failed(sb.toString()); 
       }
       return passed; 
   } 
 
   private boolean verifyFeatureDescriptor(FeatureDescriptor fd, String dspName, String shortDesc, boolean exp, boolean hid, StringBuffer sb )
   {
      boolean passed = true; 
      if (!fd.getDisplayName().equals(dspName) && !(dspName.equals("serverTraceCategories") && fd.getDisplayName().equals("serverTrace")))      //Note:  serverTraceCategories  maps to serverTrace
      {
         sb.append("Wrong display name: " + fd.getDisplayName()+" sb "+dspName+"\n");
         passed = false;
      }
      if (!fd.getShortDescription().equals(shortDesc))
      {
         sb.append("Wrong short description "+dspName+": " + fd.getShortDescription()+" expected "+shortDesc+"\n");
         passed = false; 
      }
      if (fd.isExpert() != exp)
      {
         sb.append("Wrong isExpert(): " + String.valueOf(fd.isExpert())+"\n");
         passed=false; 
      }
      if (fd.isHidden() != hid)
      {
         sb.append("Wrong isHidden(): " + String.valueOf(fd.isHidden())+"\n");
         passed =  false;
      }
      return passed ;
   }



   /**
   Verify the constructor, AS400JDBCConnectionPoolDataSourceBeanInfo().
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
         AS400JDBCConnectionPoolDataSourceBeanInfo bi = new AS400JDBCConnectionPoolDataSourceBeanInfo();
         assertCondition(true, bi.toString()); 
      }
      catch(Throwable e)
      {
         failed(e, "Unexpected exception");
      }
   }

   /**
   Verify AS400JDBCConnectionPoolDataSourceBeanInfo::getBeanDescriptor().
   <br>
   Expected results:
   <ul>
   <li>BeanDescriptor.getBeanClass() will return
   "com.ibm.as400.AS400JDBCConnectionPoolDataSource".
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
         AS400JDBCConnectionPoolDataSourceBeanInfo bi = new AS400JDBCConnectionPoolDataSourceBeanInfo();
         BeanDescriptor bd = bi.getBeanDescriptor();
         if (!bd.getBeanClass().getName().equals("com.ibm.as400.access.AS400JDBCConnectionPoolDataSource") || bd.getCustomizerClass() != null)
         {
            failed("getBeanDescriptor");
            return;
         }
         if (!verifyFeatureDescriptor(bd, "AS400JDBCConnectionPoolDataSource", "AS400JDBCConnectionPoolDataSource", false, false))
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
   *  Verify AS400JDBCConnectionPoolDataSourceBeanInfo::getDefaultEventIndex() is returned
   *  from getAdditionalBeanInfo().
   *  <br>
   *  Expected results:
   *  <ul>
   *    <li>0 will be returned.
   *  </ul>
   **/
   public void Var003()
   {
      try
      {
         AS400JDBCConnectionPoolDataSourceBeanInfo bi = new AS400JDBCConnectionPoolDataSourceBeanInfo();
         BeanInfo[] bis = bi.getAdditionalBeanInfo();

         int index = bis[0].getDefaultEventIndex();
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
   *  Verify AS400JDBCConnectionPoolDataSource::getAdditionBeanInfo().
   *  Expected results:
   *  <UL>
   *  <LI>A AS400JDBCDataSourceBeanInfo object will be returned.
   *  </UL>
   **/
   public void Var004()
   {
      try
      {
         AS400JDBCConnectionPoolDataSourceBeanInfo bi = new AS400JDBCConnectionPoolDataSourceBeanInfo();
         BeanInfo[] bis = bi.getAdditionalBeanInfo();
         if (bis == null || bis.length != 1)
         {
            failed("Wrong beaninfo returned from getAdditionalBeanInfo.");
            return;
         }
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
         return;
      }
   }


   /**
   *  Verify AS400JDBCConnectionPoolDataSource::getBeanDescriptor() returned from getAdditionalBeanInfo().
   *  Expected results:
   *  <UL>
   *  <LI>BeanDescriptor.getBeanClass() will return "com.ibm.as400.access.AS400JDBCDataSource".
   *  <LI>BeanDescriptor.getCustomizerClass() will return null.
   *  <LI>The FeatureDescriptor methods will return correct information:
   *    <UL>
   *    <LI>getDisplayName will return the display name for the obejct.
   *    <LI>getShortDescription will return the short description for the object.
   *    <LI>isExpert() will return false.
   *    <LI>isHidden() will return false.
   *    </UL>
   *  </UL>
   **/
   public void Var005()
   {
      try
      {
         AS400JDBCConnectionPoolDataSourceBeanInfo bi = new AS400JDBCConnectionPoolDataSourceBeanInfo();
         BeanInfo[] bis = bi.getAdditionalBeanInfo();
         BeanDescriptor bd = bis[0].getBeanDescriptor();
         if (!bd.getBeanClass().getName().equals("com.ibm.as400.access.AS400JDBCDataSource")
             || bd.getCustomizerClass() != null)
         {
            failed("getBeanDescriptor " + bd.getBeanClass().getName());
            return;
         }
         if (!verifyFeatureDescriptor(bd, "AS400JDBCDataSource", "AS400JDBCDataSource", false, false))
         {
            return;
         }
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
         return;
      }
   }

   /**
   *  Verify AS400JDBCConnectionPoolDataSourceBeanInfo::getDefaultPropertyIndex()
   *  is returned from getAdditionalBeanInfo().
   *  <br>
   *  Expected results:
   *  <ul>
   *  <li>0 will be returned.
   *  </ul>
   **/
   public void Var006()
   {
      try
      {
         AS400JDBCConnectionPoolDataSourceBeanInfo bi = new AS400JDBCConnectionPoolDataSourceBeanInfo();
         BeanInfo[] bis = bi.getAdditionalBeanInfo();

         int index = bis[0].getDefaultPropertyIndex();
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
   Verify AS400JDBCConnectionPoolDataSourceBeanInfo::getEventSetDescriptors()
   is returned from getAdditionalBeanInfo().
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
   public void Var007()
   {
      try
      {
         AS400JDBCConnectionPoolDataSourceBeanInfo bi = new AS400JDBCConnectionPoolDataSourceBeanInfo();
         BeanInfo[] bis = bi.getAdditionalBeanInfo();

         EventSetDescriptor[] ed = bis[0].getEventSetDescriptors();
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
   Verify AS400JDBCConnectionPoolDataSourceBeanInfo::getXIcon(int)
   is returned from getAdditionalBeanInfo().
   <br>
   Expected results:
   <ul>
   <li>No exceptions occur when any of the valid constants are provided.
   <li>Null is returned when an invalid constant is provded.
   </ul>
   **/
   public void Var008()
   {
       // Icons / GUI components no longer available in JTOpen 20.0.X

      //
      //  Variation does not work on OS/400 
      //
       if (System.getProperty("os.name").indexOf("400") >= 0) {
	   notApplicable("Icon testcase not working on OS/400");
	   return; 
       } 
      try
      {
         AS400JDBCConnectionPoolDataSourceBeanInfo bi = new AS400JDBCConnectionPoolDataSourceBeanInfo();
         BeanInfo[] bis = bi.getAdditionalBeanInfo();

      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception");
         return;
      }
      succeeded();
   }

   /**
   Verify AS400JDBCConnectionPoolDataSourceBeanInfo::getPropertyDescriptors()
   is returned from getAdditionalBeanInfo().
   <br>
   Expected results:
   <ul>
   <li>An array of size 46 will be returned.
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
   public void Var009()
   {
      boolean passed = true;
      StringBuffer failMessage = new StringBuffer(); 
      try
      {
         AS400JDBCConnectionPoolDataSourceBeanInfo bi = new AS400JDBCConnectionPoolDataSourceBeanInfo();
         BeanInfo[] bis = bi.getAdditionalBeanInfo();

         PropertyDescriptor[] pd = bis[0].getPropertyDescriptors();
         if (pd.length != 113)  //@A1C //@A5C  //@A6C    //@B1C     //@C1C //@PDC //@D1C //@PDC //@D3C @ac1 cc1 @dmy 91=query replace truncated parameter
         {
            failMessage.append("Wrong number of property descriptors returned: " + String.valueOf(pd.length)+"\n");
	    passed = false; 
         }

         for (int i=0; i< pd.length; i++)
         {
            if (pd[i].getPropertyEditorClass() != null)
            {
               failMessage.append("Property editor class not null\n");
               passed = false; 
            }
         }

         Properties propertyTypes = new Properties();
         propertyTypes.put("access", "java.lang.String");
         propertyTypes.put("behaviorOverride", "int");  //@A4A
         propertyTypes.put("bidiStringType", "int");  //@A1A  //@A2C
         propertyTypes.put("bigDecimal", "boolean");
         propertyTypes.put("blockCriteria", "int");
         propertyTypes.put("blockSize", "int");
         propertyTypes.put("cursorHold", "boolean");
         propertyTypes.put("cursorSensitivity", "java.lang.String");  //@A6A
         propertyTypes.put("databaseName", "java.lang.String");
         propertyTypes.put("dataCompression", "boolean");
         propertyTypes.put("dataSourceName", "java.lang.String");
         propertyTypes.put("dataTruncation", "boolean");
         propertyTypes.put("dateFormat", "java.lang.String");
         propertyTypes.put("dateSeparator", "java.lang.String");
         propertyTypes.put("decimalSeparator", "java.lang.String");
         propertyTypes.put("description", "java.lang.String");
         propertyTypes.put("driver", "java.lang.String");
         propertyTypes.put("errors", "java.lang.String");
         propertyTypes.put("extendedDynamic", "boolean");
         propertyTypes.put("extendedMetadata", "boolean");  //@A3A
         propertyTypes.put("fullOpen", "boolean");  // @W1a
         propertyTypes.put("lazyClose", "boolean");
         propertyTypes.put("libraries", "java.lang.String");
         propertyTypes.put("lobThreshold", "int");
         propertyTypes.put("naming", "java.lang.String");
         propertyTypes.put("package", "java.lang.String");
         propertyTypes.put("packageAdd", "boolean");
         propertyTypes.put("packageCache", "boolean");
         propertyTypes.put("packageClear", "boolean");
         propertyTypes.put("packageCriteria", "java.lang.String");
         propertyTypes.put("packageError", "java.lang.String");
         propertyTypes.put("packageLibrary", "java.lang.String");
         propertyTypes.put("password", "java.lang.String");
         propertyTypes.put("portNumber", "int");
         propertyTypes.put("prefetch", "boolean");
         propertyTypes.put("prompt", "boolean");
         propertyTypes.put("proxyServer", "java.lang.String");
         propertyTypes.put("remarks", "java.lang.String");
         propertyTypes.put("savePasswordWhenSerialized", "boolean");  //@A3A
         propertyTypes.put("secondaryURL", "java.lang.String");
         propertyTypes.put("secure", "boolean");
         propertyTypes.put("serverName", "java.lang.String");   
         propertyTypes.put("serverTrace", "int");
         propertyTypes.put("sort", "java.lang.String");
         propertyTypes.put("sortLanguage", "java.lang.String");
         propertyTypes.put("sortTable", "java.lang.String");
         propertyTypes.put("sortWeight", "java.lang.String");
         propertyTypes.put("timeFormat", "java.lang.String");
         propertyTypes.put("timeSeparator", "java.lang.String");
         propertyTypes.put("threadUsed", "boolean");
         propertyTypes.put("trace", "boolean");
         propertyTypes.put("transactionIsolation", "java.lang.String");
         propertyTypes.put("translateBinary", "boolean");
         propertyTypes.put("user", "java.lang.String");
         propertyTypes.put("toolboxTrace", "java.lang.String");
         propertyTypes.put("qaqqinilib", "java.lang.String");
         propertyTypes.put("keepAlive", "boolean");
         propertyTypes.put("receiveBufferSize", "int");
         propertyTypes.put("sendBufferSize", "int");
         propertyTypes.put("soLinger", "int");
         propertyTypes.put("soTimeout", "int");
         propertyTypes.put("tcpNoDelay", "boolean");
         propertyTypes.put("packageCcsid", "int");
         propertyTypes.put("minimumDivideScale", "int");
         propertyTypes.put("maximumPrecision", "int");
         propertyTypes.put("maximumScale", "int");
         propertyTypes.put("translateHex", "java.lang.String");
         propertyTypes.put("serverTraceCategories", "int");
         propertyTypes.put("loginTimeout", "int");
         propertyTypes.put("trueAutocommit", "boolean");
         propertyTypes.put("bidiImplicitReordering", "boolean");
         propertyTypes.put("bidiNumericOrdering", "boolean");
         propertyTypes.put("holdInputLocators", "boolean");
         propertyTypes.put("holdStatements", "boolean");
         propertyTypes.put("rollbackCursorHold", "boolean");    //@C1A
         propertyTypes.put("variableFieldCompression", "java.lang.String");  //@C1A
         propertyTypes.put("queryOptimizeGoal", "int"); //@C1A
         propertyTypes.put("xaLooselyCoupledSupport", "int"); //@PDA
         propertyTypes.put("translateBoolean", "boolean"); //@PDA
         propertyTypes.put("metadataSource", "int");   //@D1A
         propertyTypes.put("queryStorageLimit", "int"); //@pda
         propertyTypes.put("decfloatRoundingMode", "java.lang.String"); //@pda
         propertyTypes.put("autocommitException", "boolean"); //@D3A
         propertyTypes.put("autoCommit", "boolean"); //@ac1
         propertyTypes.put("ignoreWarnings", "java.lang.String"); 
         propertyTypes.put("secureCurrentUser", "boolean");
         propertyTypes.put("concurrentAccessResolution", "int"); 
         propertyTypes.put("jvm16Synchronize", "boolean"); //@dmy
         propertyTypes.put("socketTimeout", "int");
	 propertyTypes.put("maximumBlockedInputRows", "int");          
	 propertyTypes.put("queryTimeoutMechanism", "java.lang.String");
	 propertyTypes.put("queryReplaceTruncatedParameter","java.lang.String");

	 propertyTypes.put("numericRangeError","java.lang.String"); 
	 propertyTypes.put("characterTruncation","java.lang.String");
	 propertyTypes.put("useBlockUpdate)","boolean");
	 propertyTypes.put("describeOption","java.lang.String");
	 propertyTypes.put("decimalDataErrors","java.lang.String");
	 propertyTypes.put("timestampFormat","java.lang.String");
	 propertyTypes.put("useDrdaMetadataVersion","boolean");

propertyTypes.put("extendedMetaData","boolean"); 
propertyTypes.put("secondaryUrl","java.lang.String"); 
propertyTypes.put("packageCCSID","int"); 
propertyTypes.put("toolboxTraceCategory","java.lang.String"); 
propertyTypes.put("qaqqiniLibrary","java.lang.String"); 
propertyTypes.put("trueAutoCommit","boolean"); 
propertyTypes.put("metaDataSource","int"); 
propertyTypes.put("useBlockUpdate","boolean"); 
	 
propertyTypes.put("enableClientAffinitiesList", "int"); 
propertyTypes.put("affinityFailbackInterval","int"); 
propertyTypes.put("clientRerouteAlternateServerName","java.lang.String"); 
propertyTypes.put("clientRerouteAlternatePortNumber","java.lang.String"); 
propertyTypes.put("maxRetriesForClientReroute","int"); 
propertyTypes.put("retryIntervalForClientReroute","int"); 
propertyTypes.put("enableSeamlessFailover","int"); 
         
         for (int i=0; i< pd.length; i++)
         {
            String value = (String)propertyTypes.get(pd[i].getName());
            if (!pd[i].getPropertyType().getName().equals(value))
            {
		failMessage.append("Wrong property types returned: [" + i + "]:("+pd[i].getName()+") " + pd[i].getPropertyType().toString()+" sb "+value+"\n");
               passed = false; 
            }
         }

	 Properties getPropertyMethods = new Properties(); 
         getPropertyMethods.put("access", "getAccess");
         getPropertyMethods.put("behaviorOverride", "getBehaviorOverride"); 
         getPropertyMethods.put("bidiStringType", "getBidiStringType");  //@A1A
         getPropertyMethods.put("bigDecimal", "isBigDecimal");
         getPropertyMethods.put("blockCriteria", "getBlockCriteria");
         getPropertyMethods.put("blockSize", "getBlockSize");
         getPropertyMethods.put("cursorHold", "isCursorHold");
         getPropertyMethods.put("cursorSensitivity", "getCursorSensitivity");
         getPropertyMethods.put("databaseName", "getDatabaseName");
         getPropertyMethods.put("dataCompression", "isDataCompression");
         getPropertyMethods.put("dataSourceName", "getDataSourceName");
         getPropertyMethods.put("dataTruncation", "isDataTruncation");
         getPropertyMethods.put("dateFormat", "getDateFormat");
         getPropertyMethods.put("dateSeparator", "getDateSeparator");
         getPropertyMethods.put("decimalSeparator", "getDecimalSeparator");
         getPropertyMethods.put("description", "getDescription");
         getPropertyMethods.put("driver", "getDriver");
         getPropertyMethods.put("errors", "getErrors");
         getPropertyMethods.put("extendedDynamic", "isExtendedDynamic");
         getPropertyMethods.put("extendedMetaData", "isExtendedMetaData"); //@A3A
         getPropertyMethods.put("fullOpen", "isFullOpen");                 //@W1a
         getPropertyMethods.put("lazyClose", "isLazyClose");
         getPropertyMethods.put("libraries", "getLibraries");
         getPropertyMethods.put("lobThreshold", "getLobThreshold");
         getPropertyMethods.put("naming", "getNaming");
         getPropertyMethods.put("package", "getPackage");
         getPropertyMethods.put("packageAdd", "isPackageAdd");
         getPropertyMethods.put("packageCache", "isPackageCache");
         getPropertyMethods.put("packageClear", "isPackageClear");
         getPropertyMethods.put("packageCriteria", "getPackageCriteria");
         getPropertyMethods.put("packageError", "getPackageError");
         getPropertyMethods.put("packageLibrary", "getPackageLibrary");
         getPropertyMethods.remove("password");
         getPropertyMethods.put("portNumber", "getPortNumber"); 
         getPropertyMethods.put("prefetch", "isPrefetch"); 
         getPropertyMethods.put("prompt", "isPrompt");
         getPropertyMethods.put("proxyServer", "getProxyServer");
         getPropertyMethods.put("remarks", "getRemarks");
         getPropertyMethods.put("savePasswordWhenSerialized", "isSavePasswordWhenSerialized"); //@A3A
         getPropertyMethods.put("secondaryUrl", "getSecondaryUrl");
         getPropertyMethods.put("secure", "isSecure");
         getPropertyMethods.put("serverName", "getServerName");     
         getPropertyMethods.put("serverTrace", "getServerTrace");          
         getPropertyMethods.put("sort", "getSort");
         getPropertyMethods.put("sortLanguage", "getSortLanguage");
         getPropertyMethods.put("sortTable", "getSortTable");
         getPropertyMethods.put("sortWeight", "getSortWeight");
         getPropertyMethods.put("timeFormat", "getTimeFormat");
         getPropertyMethods.put("timeSeparator", "getTimeSeparator");
         getPropertyMethods.put("threadUsed", "isThreadUsed");
         getPropertyMethods.put("trace", "isTrace");
         getPropertyMethods.put("transactionIsolation", "getTransactionIsolation");
         getPropertyMethods.put("translateBinary", "isTranslateBinary");
         getPropertyMethods.put("user", "getUser");
         getPropertyMethods.put("toolboxTrace", "getToolboxTraceCategory");
         getPropertyMethods.put("qaqqiniLibrary", "getQaqqiniLibrary");
         getPropertyMethods.put("keepAlive", "isKeepAlive");
         getPropertyMethods.put("receiveBufferSize", "getReceiveBufferSize");
         getPropertyMethods.put("sendBufferSize", "getSendBufferSize");
         getPropertyMethods.put("soLinger", "getSoLinger");
         getPropertyMethods.put("soTimeout", "getSoTimeout");
         getPropertyMethods.put("tcpNoDelay", "getTcpNoDelay");
         getPropertyMethods.put("packageCCSID", "getPackageCCSID");
         getPropertyMethods.put("minimumDivideScale", "getMinimumDivideScale");
         getPropertyMethods.put("maximumPrecision", "getMaximumPrecision");
         getPropertyMethods.put("maximumScale", "getMaximumScale");
         getPropertyMethods.put("translateHex", "getTranslateHex");
         getPropertyMethods.put("serverTraceCategories", "getServerTraceCategories");
         getPropertyMethods.put("loginTimeout", "getLoginTimeout");
         getPropertyMethods.put("trueAutoCommit", "isTrueAutoCommit");
         getPropertyMethods.put("bidiImplicitReordering", "isBidiImplicitReordering");
         getPropertyMethods.put("bidiNumericOrdering", "isBidiNumericOrdering");
         getPropertyMethods.put("holdInputLocators", "isHoldInputLocators");
         getPropertyMethods.put("holdStatements", "isHoldStatements");
         getPropertyMethods.put("rollbackCursorHold", "isRollbackCursorHold");   //@C1A
         getPropertyMethods.put("variableFieldCompression", "getVariableFieldCompression");   //@C1A
         getPropertyMethods.put("queryOptimizeGoal", "getQueryOptimizeGoal");    //@C1A
         getPropertyMethods.put("xaLooselyCoupledSupport", "getXALooselyCoupledSupport"); //@PDA
         getPropertyMethods.put("translateBoolean", "isTranslateBoolean");                //@PDA
         getPropertyMethods.put("metaDataSource", "getMetaDataSource");    //@D1A
         getPropertyMethods.put("queryStorageLimit", "getQueryStorageLimit"); //@pda
         getPropertyMethods.put("decfloatRoundingMode", "getDecfloatRoundingMode"); //@pda
         getPropertyMethods.put("autocommitException", "isAutocommitException");                //@D3A
         getPropertyMethods.put("autoCommit", "isAutoCommit"); 
         getPropertyMethods.put("ignoreWarnings", "getIgnoreWarnings");
         getPropertyMethods.put("secureCurrentUser", "isSecureCurrentUser");
         getPropertyMethods.put("concurrentAccessResolution", "getConcurrentAccessResolution"); 
         getPropertyMethods.put("jvm16Synchronize", "isJvm16Synchronize"); //@dmy
         getPropertyMethods.put("socketTimeout", "getSocketTimeout"); 
         getPropertyMethods.put("maximumBlockedInputRows", "getMaximumBlockedInputRows"); 
	 getPropertyMethods.put("queryTimeoutMechanism", "getQueryTimeoutMechanism");
	 getPropertyMethods.put("queryReplaceTruncatedParameter", "getQueryReplaceTruncatedParameter"); 

getPropertyMethods.put("extendedMetadata","isExtendedMetadata"); 
getPropertyMethods.put("toolboxTraceCategory","getToolboxTraceCategory");
getPropertyMethods.put("numericRangeError","getNumericRangeError");
getPropertyMethods.put("characterTruncation","getCharacterTruncation");
getPropertyMethods.put("secondaryURL","getSecondaryURL");
getPropertyMethods.put("packageCcsid","getPackageCcsid");
getPropertyMethods.put("qaqqinilib","getQaqqinilib");
getPropertyMethods.put("trueAutocommit","isTrueAutocommit");
getPropertyMethods.put("metadataSource","getMetadataSource");
getPropertyMethods.put("useBlockUpdate","isUseBlockUpdate");
getPropertyMethods.put("describeOption","getDescribeOption");
getPropertyMethods.put("decimalDataErrors","getDecimalDataErrors");
getPropertyMethods.put("timestampFormat","getTimestampFormat");
getPropertyMethods.put("useDrdaMetadataVersion","isUseDrdaMetadataVersion");


getPropertyMethods.put("enableClientAffinitiesList", "getEnableClientAffinitiesList"); 
getPropertyMethods.put("affinityFailbackInterval","getAffinityFailbackInterval"); 
getPropertyMethods.put("clientRerouteAlternateServerName","getClientRerouteAlternateServerName"); 
getPropertyMethods.put("clientRerouteAlternatePortNumber","getClientRerouteAlternatePortNumber"); 
getPropertyMethods.put("maxRetriesForClientReroute","getMaxRetriesForClientReroute"); 
getPropertyMethods.put("retryIntervalForClientReroute","getRetryIntervalForClientReroute"); 
getPropertyMethods.put("enableSeamlessFailover","getEnableSeamlessFailover"); 



         for (int i=0; i< pd.length; i++)
         {
            if (pd[i].getName().equals("password"))         // password.
            {
               if (pd[i].getReadMethod() != null)
               {
                  failMessage.append("Wrong read method: " + pd[i].getReadMethod().getName()+"\n");
                  passed = false; 
               }
            }
            else
            {
		String name = pd[i].getName(); 
               String value = (String)getPropertyMethods.get(name);
               if (!pd[i].getReadMethod().getName().equals(value))
               {
		   // Check for weird cases
		   if (name.equals("toolboxTrace") &&
		       pd[i].getReadMethod().getName().equals("getToolboxTrace"))
		   {
		       // OK Skip
		   } else  if (name.equals("serverTrace") &&
		       pd[i].getReadMethod().getName().equals("getServerTraceCategories"))
		   {
		   } else { 

		       if (pd[i].getReadMethod().getName().equals("isTcpNoDelay") &&
			   value.equals("getTcpNoDelay")) {
			   // Ignore this mismatch 
		       } else { 
			   failMessage.append("Wrong read method for "+pd[i].getName()+": " + pd[i].getReadMethod().getName()+" not "+value+"\n");
			   passed=false;
		       }
               }
            }
         }
         }

	 Properties setPropertyMethods = new Properties(); 
         setPropertyMethods.put("access", "setAccess");
         setPropertyMethods.put("behaviorOverride", "setBehaviorOverride"); 
         setPropertyMethods.put("bidiStringType", "setBidiStringType");  //@A1A
         setPropertyMethods.put("bigDecimal", "setBigDecimal");
         setPropertyMethods.put("blockCriteria", "setBlockCriteria");
         setPropertyMethods.put("blockSize", "setBlockSize");
         setPropertyMethods.put("cursorHold", "setCursorHold");
         setPropertyMethods.put("cursorSensitivity", "setCursorSensitivity"); 
         setPropertyMethods.put("databaseName", "setDatabaseName");
         setPropertyMethods.put("dataCompression", "setDataCompression");
         setPropertyMethods.put("dataSourceName", "setDataSourceName");
         setPropertyMethods.put("dataTruncation", "setDataTruncation");
         setPropertyMethods.put("dateFormat", "setDateFormat");
         setPropertyMethods.put("dateSeparator", "setDateSeparator");
         setPropertyMethods.put("decimalSeparator", "setDecimalSeparator");
         setPropertyMethods.put("description", "setDescription");
         setPropertyMethods.put("driver", "setDriver");
         setPropertyMethods.put("errors", "setErrors");
         setPropertyMethods.put("extendedDynamic", "setExtendedDynamic");
         setPropertyMethods.put("extendedMetaData", "setExtendedMetaData");  //@A3A
         setPropertyMethods.put("fullOpen", "setFullOpen");              // @W1a
         setPropertyMethods.put("lazyClose", "setLazyClose");
         setPropertyMethods.put("libraries", "setLibraries");
         setPropertyMethods.put("lobThreshold", "setLobThreshold");
         setPropertyMethods.put("naming", "setNaming");
         setPropertyMethods.put("package", "setPackage");
         setPropertyMethods.put("packageAdd", "setPackageAdd");
         setPropertyMethods.put("packageCache", "setPackageCache");
         setPropertyMethods.put("packageClear", "setPackageClear");
         setPropertyMethods.put("packageCriteria", "setPackageCriteria");
         setPropertyMethods.put("packageError", "setPackageError");
         setPropertyMethods.put("packageLibrary", "setPackageLibrary");
         setPropertyMethods.put("password", "setPassword");
         setPropertyMethods.put("portNumber", "setPortNumber");
         setPropertyMethods.put("prefetch", "setPrefetch");
         setPropertyMethods.put("prompt", "setPrompt");
         setPropertyMethods.put("proxyServer", "setProxyServer");
         setPropertyMethods.put("remarks", "setRemarks");
         setPropertyMethods.put("savePasswordWhenSerialized", "setSavePasswordWhenSerialized"); //@A3A
         setPropertyMethods.put("secondaryUrl", "setSecondaryUrl");
         setPropertyMethods.put("secure", "setSecure");
         setPropertyMethods.put("serverName", "setServerName");
         setPropertyMethods.put("serverTrace", "setServerTraceCategories");          
         setPropertyMethods.put("sort", "setSort");
         setPropertyMethods.put("sortLanguage", "setSortLanguage");
         setPropertyMethods.put("sortTable", "setSortTable");
         setPropertyMethods.put("sortWeight", "setSortWeight");
         setPropertyMethods.put("timeFormat", "setTimeFormat");
         setPropertyMethods.put("timeSeparator", "setTimeSeparator");
         setPropertyMethods.put("threadUsed", "setThreadUsed");
         setPropertyMethods.put("trace", "setTrace");
         setPropertyMethods.put("transactionIsolation", "setTransactionIsolation");
         setPropertyMethods.put("translateBinary", "setTranslateBinary");
         setPropertyMethods.put("user", "setUser");
         setPropertyMethods.put("toolboxTrace", "setToolboxTraceCategory");
         setPropertyMethods.put("qaqqiniLibrary", "setQaqqiniLibrary");
         setPropertyMethods.put("keepAlive", "setKeepAlive");
         setPropertyMethods.put("receiveBufferSize", "setReceiveBufferSize");
         setPropertyMethods.put("sendBufferSize", "setSendBufferSize");
         setPropertyMethods.put("soLinger", "setSoLinger");
         setPropertyMethods.put("soTimeout", "setSoTimeout");
         setPropertyMethods.put("tcpNoDelay", "setTcpNoDelay");
         setPropertyMethods.put("packageCCSID", "setPackageCCSID");
         setPropertyMethods.put("minimumDivideScale", "setMinimumDivideScale");
         setPropertyMethods.put("maximumPrecision", "setMaximumPrecision");
         setPropertyMethods.put("maximumScale", "setMaximumScale");
         setPropertyMethods.put("translateHex", "setTranslateHex");
         setPropertyMethods.put("serverTraceCategories", "setServerTraceCategories");
         setPropertyMethods.put("loginTimeout", "setLoginTimeout");
         setPropertyMethods.put("trueAutoCommit", "setTrueAutoCommit");
         setPropertyMethods.put("bidiImplicitReordering", "setBidiImplicitReordering");
         setPropertyMethods.put("bidiNumericOrdering", "setBidiNumericOrdering");
         setPropertyMethods.put("holdInputLocators", "setHoldInputLocators");
         setPropertyMethods.put("holdStatements", "setHoldStatements");
         setPropertyMethods.put("rollbackCursorHold", "setRollbackCursorHold");  //@C1A
         setPropertyMethods.put("variableFieldCompression", "setVariableFieldCompression");  //@C1A
         setPropertyMethods.put("queryOptimizeGoal", "setQueryOptimizeGoal");    //@C1A
         setPropertyMethods.put("xaLooselyCoupledSupport", "setXALooselyCoupledSupport"); //@PDA
         setPropertyMethods.put("translateBoolean", "setTranslateBoolean");               //@PDA
         setPropertyMethods.put("metaDataSource", "setMetaDataSource"); //@D1A
         setPropertyMethods.put("queryStorageLimit", "setQueryStorageLimit");  //@pda
         setPropertyMethods.put("decfloatRoundingMode", "setDecfloatRoundingMode"); //@pda
         setPropertyMethods.put("autocommitException", "setAutocommitException"); //@D3a
         setPropertyMethods.put("autoCommit", "setAutoCommit"); 
         setPropertyMethods.put("ignoreWarnings", "setIgnoreWarnings"); 
         setPropertyMethods.put("secureCurrentUser", "setSecureCurrentUser");
         setPropertyMethods.put("concurrentAccessResolution", "setConcurrentAccessResolution"); 
         setPropertyMethods.put("jvm16Synchronize", "setJvm16Synchronize"); //@dmy
         setPropertyMethods.put("socketTimeout", "setSocketTimeout");
         setPropertyMethods.put("maximumBlockedInputRows", "setMaximumBlockedInputRows");
	 setPropertyMethods.put("queryTimeoutMechanism", "setQueryTimeoutMechanism");
	 setPropertyMethods.put("queryReplaceTruncatedParameter", "setQueryReplaceTruncatedParameter");


setPropertyMethods.put("extendedMetadata","setExtendedMetadata");
setPropertyMethods.put("numericRangeError","setNumericRangeError");
setPropertyMethods.put("characterTruncation","setCharacterTruncation");
setPropertyMethods.put("secondaryURL","setSecondaryURL");
setPropertyMethods.put("serverTrace","setServerTraceCategories"); 
setPropertyMethods.put("packageCcsid","setPackageCcsid");
setPropertyMethods.put("toolboxTrace","setToolboxTraceCategory");
setPropertyMethods.put("toolboxTraceCategory","setToolboxTraceCategory");
setPropertyMethods.put("qaqqinilib","setQaqqinilib");
setPropertyMethods.put("trueAutocommit","setTrueAutocommit");
setPropertyMethods.put("metadataSource","setMetadataSource");
setPropertyMethods.put("useBlockUpdate","setUseBlockUpdate");
setPropertyMethods.put("describeOption","setDescribeOption");
setPropertyMethods.put("decimalDataErrors","setDecimalDataErrors");
setPropertyMethods.put("timestampFormat","setTimestampFormat");
setPropertyMethods.put("useDrdaMetadataVersion","setUseDrdaMetadataVersion");



setPropertyMethods.put("enableClientAffinitiesList", "setEnableClientAffinitiesList"); 
setPropertyMethods.put("affinityFailbackInterval","setAffinityFailbackInterval"); 
setPropertyMethods.put("clientRerouteAlternateServerName","setClientRerouteAlternateServerName"); 
setPropertyMethods.put("clientRerouteAlternatePortNumber","setClientRerouteAlternatePortNumber"); 
setPropertyMethods.put("maxRetriesForClientReroute","setMaxRetriesForClientReroute"); 
setPropertyMethods.put("retryIntervalForClientReroute","setRetryIntervalForClientReroute"); 
setPropertyMethods.put("enableSeamlessFailover","setEnableSeamlessFailover"); 



Properties setPropertyMethods2 = new Properties()	 ;
setPropertyMethods2.put("toolboxTrace","setToolboxTrace");
setPropertyMethods2.put("serverTrace","setServerTrace");


         for (int i=0; i< pd.length; i++)
         {
            String value = (String)setPropertyMethods.get(pd[i].getName());
            if (!pd[i].getWriteMethod().getName().equals(value))
            {
		String value2 = (String)setPropertyMethods2.get(pd[i].getName());
		if (!pd[i].getWriteMethod().getName().equals(value2))
		{

		    failMessage.append("Wrong write method for "+pd[i].getName()+": " 
				       + pd[i].getWriteMethod().getName()+" expected "+value+"or "+value2+"\n");
		    passed=false;
		}
	    }
         }

         for (int i=0; i< pd.length; i++)
         {
            if (!pd[i].isBound())
            {
               failMessage.append("Bound problem on index "+i+" name="+pd[i].getName()+"\n");
               passed = false; 
            }
         }

         for (int i=0; i< pd.length; i++)
         {
            if (pd[i].isConstrained())
            {
               failMessage.append("Constrained problem on index "+i+"\n");
               passed = false; ;
            }
         }

         // Set the property short descriptions to validate.
	 Properties propertyShortDescs = new Properties(); 
         propertyShortDescs.put("access", "Specifies the level of database access for the connection.");
         propertyShortDescs.put("behaviorOverride", "Specifies the Toolbox JDBC driver behavior to override.");  
         propertyShortDescs.put("bidiStringType", "Specifies the output string type of bidi data.");  //@A1A
         propertyShortDescs.put("bigDecimal", "Specifies whether an intermediate java.math.BigDecimal object is used for packed and zoned decimal conversions.");
         propertyShortDescs.put("blockCriteria", "Specifies the criteria for retrieving data from the system in blocks of records.");   //@A1C //@D2C
         propertyShortDescs.put("blockSize", "Specifies the block size (in kilobytes) to retrieve from the system and cache on the client."); 
         propertyShortDescs.put("cursorHold", "Specifies whether to hold the cursor across transactions.");
         propertyShortDescs.put("cursorSensitivity", "Specifies the cursor sensitivity to request from the database."); 
         propertyShortDescs.put("databaseName", "Specifies the name of the database.");
         propertyShortDescs.put("dataCompression", "Specifies whether result set data is compressed.");
         propertyShortDescs.put("dataSourceName", "Specifies the name of the data source.");
         propertyShortDescs.put("dataTruncation", "Specifies whether data truncation exceptions are thrown.");
         propertyShortDescs.put("dateFormat", "Specifies the date format used in date literals within SQL statements.");
         propertyShortDescs.put("dateSeparator", "Specifies the date separator used in date literals within SQL statements.");
         propertyShortDescs.put("decimalSeparator", "Specifies the decimal separator used in numeric constants within SQL statements.");
         propertyShortDescs.put("description", "Specifies the description of the data source.");
         propertyShortDescs.put("driver", "Specifies the JDBC driver implementation.");
         propertyShortDescs.put("errors", "Specifies the amount of detail to be returned in the message for errors that occur on the system."); 
         propertyShortDescs.put("extendedDynamic", "Specifies whether to use extended dynamic support.");
         propertyShortDescs.put("extendedMetaData", "Specifies whether to request extended metadata from the system.");  //@A3A @D2C
         propertyShortDescs.put("fullOpen", "Specifies whether to use an optimized query." );              //@W1a
         propertyShortDescs.put("lazyClose", "Specifies whether to delay closing cursors until subsequent requests.");
         propertyShortDescs.put("libraries", "Specifies the libraries to add to the server job's library list.");   //@A1C
         propertyShortDescs.put("lobThreshold", "Specifies the maximum LOB (large object) size (in kilobytes) that can be retrieved as part of a result set.");
         propertyShortDescs.put("naming", "Specifies the naming convention used when referring to tables.");
         propertyShortDescs.put("package", "Specifies the name of the SQL package.");
         propertyShortDescs.put("packageAdd", "Specifies whether to add statements to an existing SQL package.");
         propertyShortDescs.put("packageCache", "Specifies whether to cache SQL packages in memory.");
         propertyShortDescs.put("packageClear", "Specifies whether to clear SQL packages when they become full.");
         propertyShortDescs.put("packageCriteria", "Specifies the type of SQL statements to be stored in the SQL package");
         propertyShortDescs.put("packageError", "Specifies the action to take when SQL package errors occur.");
         propertyShortDescs.put("packageLibrary", "Specifies the library for the SQL package.");
         propertyShortDescs.put("password", "Specifies the password for connecting to the system.");  
         propertyShortDescs.put("prefetch", "Specifies whether to prefetch data when running a SELECT statement.");
         propertyShortDescs.put("portNumber", "Specifies the port number used to connect to the ZDA server.");

         propertyShortDescs.put("prompt", "Specifies whether the user should be prompted if a user name or password is needed to connect to the system.");  //@A1C @D2C
         propertyShortDescs.put("proxyServer", "Specifies the host name and (optionally) port number of the middle-tier machine where the proxy server is running.");
         propertyShortDescs.put("remarks", "Specifies the source of the text for REMARKS columns in ResultSet objects returned by DatabaseMetaData methods.");
         propertyShortDescs.put("savePasswordWhenSerialized", "Specifies whether to save the password when the data source object is serialized."); //@A3A
         propertyShortDescs.put("secondaryUrl", "Specifies the secondary URL to be used for a connection on the middle-tier's DriverManager in a multiple tier environment.");
         propertyShortDescs.put("secondaryURL", "Specifies the secondary URL to be used for a connection on the middle-tier's DriverManager in a multiple tier environment.");
         propertyShortDescs.put("secure", "Specifies whether a Secure Sockets Layer (SSL) connection is used to communicate with the system."); //@D2C
         propertyShortDescs.put("serverName", "Specifies the name of the system.");  //@A1C //@D2C
         propertyShortDescs.put("serverTrace", "Specifies whether the job on the system should be traced."); //@D2C
         propertyShortDescs.put("sort", "Specifies how the system sorts records before sending them to the client."); //@D2C
         propertyShortDescs.put("sortLanguage", "Specifies a 3-character language ID to use for selection of a sort sequence.");
         propertyShortDescs.put("sortTable", "Specifies the library and file name of a sort sequence table stored on the system.");   //@A1C @D2C
         propertyShortDescs.put("sortWeight", "Specifies how the system treats case while sorting records."); //@D2C
         propertyShortDescs.put("threadUsed", "Specifies whether to use threads in communication with the host servers.");
         propertyShortDescs.put("timeFormat", "Specifies the time format used in time literals within SQL statements.");
         propertyShortDescs.put("timeSeparator", "Specifies the time separator used in time literals within SQL statements.");
         propertyShortDescs.put("trace", "Specifies whether trace messages should be logged.");
         propertyShortDescs.put("transactionIsolation", "Specifies the default transaction isolation.");
         propertyShortDescs.put("translateBinary", "Specifies whether binary data is translated.");
         propertyShortDescs.put("user", "Specifies the user name for connecting to the system.");   //@A1C @D2C
         propertyShortDescs.put("toolboxTrace", "Specifies what category of a toolbox trace to log.");
         propertyShortDescs.put("toolboxTraceCategory", "Specifies what category of a toolbox trace to log.");
         propertyShortDescs.put("qaqqiniLibrary", "Specifies a QAQQINI library name.");
         propertyShortDescs.put("qaqqinilibrary", "Specifies a QAQQINI library name.");
         propertyShortDescs.put("qaqqinilib", "Specifies a QAQQINI library name.");

         propertyShortDescs.put("keepAlive", "Specifies the socket keep alive value to use when connecting to the system."); //@D2C
         propertyShortDescs.put("receiveBufferSize", "Specifies the socket receive buffer size to use when connecting to the system."); //@D2C
         propertyShortDescs.put("sendBufferSize", "Specifies the socket send buffer size to use when connecting to the system.");  //@D2C
         propertyShortDescs.put("soLinger", "Specifies the socket linger value to use when connecting to the system."); //@D2C
         propertyShortDescs.put("soTimeout", "Specifies the socket timeout value to use when connecting to the system."); //@D2C
         propertyShortDescs.put("tcpNoDelay", "Specifies the socket TCP no delay value to use when connecting to the system."); //@D2C
         propertyShortDescs.put("packageCCSID", "Specifies the character encoding to use for the SQL package and any statements sent to the system.");//@D2C
         propertyShortDescs.put("packageCcsid", "Specifies the character encoding to use for the SQL package and any statements sent to the system."); //@D2C
         propertyShortDescs.put("minimumDivideScale", "Specifies the minimum scale value for the result of decimal division.");
         propertyShortDescs.put("maximumPrecision", "Specifies the maximum decimal precision the database should use.");
         propertyShortDescs.put("maximumScale", "Specifies the maximum scale the database should use.");
         propertyShortDescs.put("translateHex", "Specifies how hexadecimal constants are interpreted.");
         propertyShortDescs.put("serverTraceCategories", "Specifies whether the job on the system should be traced."); //@D2C
         propertyShortDescs.put("loginTimeout", "Specifies the maximum time in seconds that this data source can wait while attempting to connect to a database.");
         propertyShortDescs.put("trueAutoCommit", "Specifies whether the connection should use true auto commit support.");
         propertyShortDescs.put("trueAutocommit", "Specifies whether the connection should use true auto commit support.");
         propertyShortDescs.put("holdInputLocators", "Specifies if input locators should be of type \"hold\" or \"no hold\".");
         propertyShortDescs.put("bidiImplicitReordering", "Specifies if bidi implicit LTR-RTL reordering should be used.");
         propertyShortDescs.put("bidiNumericOrdering", "Specifies if the numeric ordering round trip feature should be used.");
         propertyShortDescs.put("holdStatements", "Specifies if statements should remain open until a transaction boundary.");
         propertyShortDescs.put("rollbackCursorHold", "Specifies whether to hold cursors across a rollback.");     //@C1A
         propertyShortDescs.put("variableFieldCompression", "Specifies whether variable-length fields should be compressed.");     //@C1A
         propertyShortDescs.put("queryOptimizeGoal", "Specifies the goal the system should use with optimization of queries.");    //@C1A @D2C
         propertyShortDescs.put("xaLooselyCoupledSupport", "Specifies whether lock sharing is allowed for loosely coupled transaction branches."); //@PDA
         propertyShortDescs.put("translateBoolean", "Specifies how Boolean objects are interpreted when setting the value for a character field/parameter."); //@PDA
         propertyShortDescs.put("metaDataSource", "Specifies how to retrieve DatabaseMetaData.");   //@D1A
         propertyShortDescs.put("metadataSource", "Specifies how to retrieve DatabaseMetaData.");   //@D1A
         propertyShortDescs.put("queryStorageLimit", "Specifies the query storage limit to be used when statements in a connection are executed.");   //@pda
         propertyShortDescs.put("decfloatRoundingMode", "Specifies the rounding mode to use when working with decfloat data type."); //@pda
         propertyShortDescs.put("autocommitException", "Specifies whether to throw an SQLException when Connection.commit() or Connection.rollback() is called if autocommit is enabled."); //@D3A
         propertyShortDescs.put("autoCommit", "Specifies whether auto-commit mode is the default connection mode for new connections."); //@AC1
         propertyShortDescs.put("ignoreWarnings", "Specifies a list of SQL states for which the driver should not create warning objects.");
         propertyShortDescs.put("secureCurrentUser", "Specifies whether to disallow \"\" and *current as user name and password.");
         propertyShortDescs.put("concurrentAccessResolution", "Specifies whether \"currently committed\" access is used on the connection."); //@cc1
         propertyShortDescs.put("jvm16Synchronize", "Specifies whether to enable temporary workaround fix for JVM 1.6."); //@dmy
         propertyShortDescs.put("socketTimeout", "Specifies the socket timeout value in milliseconds.");
         
         propertyShortDescs.put("maximumBlockedInputRows", "Specifies the maximum number of rows to be sent to the database engine when using a blocked insert or update operation.");
         

         propertyShortDescs.put("queryTimeoutMechanism", "Specifies the method used to implement the query timeout."); 

         propertyShortDescs.put("queryReplaceTruncatedParameter", "Specifies the string value to be used when a query parameter is truncated."); 



propertyShortDescs.put("numericRangeError","Specifies the behavior when a numeric range error occurs.");
propertyShortDescs.put("characterTruncation","Specifies the behavior when character truncation occurs.");







propertyShortDescs.put("useBlockUpdate","Specifies the use of a block update mode when inserting or updating blocks of data into the database.");
propertyShortDescs.put("describeOption","Specifies the type of describe information returned from ther server.");
propertyShortDescs.put("decimalDataErrors","Specifies how decimal data errors are handled.");
propertyShortDescs.put("timestampFormat","Specifies the format for timestamps retrieved via getString.");
propertyShortDescs.put("useDrdaMetadataVersion","Specifies if the DRDA metadata version information should be returned.");

         propertyShortDescs.put("extendedMetadata", "extendedMetadata");

         propertyShortDescs.put("enableClientAffinitiesList", "Specifies if alternate servers should be used."); 
         propertyShortDescs.put("affinityFailbackInterval","Specifies the length, in seconds, of the interval after which the primary connection will be re-established."); 
         propertyShortDescs.put("clientRerouteAlternateServerName","Specifies the list of alternate servers."); 
         propertyShortDescs.put("clientRerouteAlternatePortNumber","Specifies the list of alternate port numbers."); 
         propertyShortDescs.put("maxRetriesForClientReroute","The maximum number of connection retries for automatic client reroute."); 
         propertyShortDescs.put("retryIntervalForClientReroute","The number of seconds between consecutive connection retries."); 
         propertyShortDescs.put("enableSeamlessFailover","Specifies whether the JTOpen JDBC driver uses seamless failover for client reroute."); 

         for (int i=0; i< pd.length; i++)
         {
              
            if (!verifyFeatureDescriptor(pd[i], pd[i].getDisplayName(), (String)propertyShortDescs.get(pd[i].getDisplayName()), false, false, failMessage))
               passed=false; 
         }
	 assertCondition(passed, failMessage.toString());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception FailMessage="+failMessage.toString());
         return;
      }
   }

}
