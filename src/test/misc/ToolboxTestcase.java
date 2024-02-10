///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ToolboxTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.misc;

import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.Long;
import java.net.URL;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.FileAttributes;
import com.ibm.as400.access.Trace;

import test.JDTestcase;
import test.Testcase;

/**
 Test cases for the miscellaneous toolbox problems. 
 **/
public class ToolboxTestcase extends Testcase
{


    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {

      super.setup();
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {

      super.cleanup();
    }

    
  public void testBeanInfo(String packageName) {
    StringBuffer sb = new StringBuffer(); 
    boolean passed = true; 
    try {
        String[] list = { "NOTFOUND"}; 
      // Find the jar file.. 
          String thisFileName = "com/ibm/as400/access/AS400.class";
      String CLASSNAME = "com.ibm.as400.access.AS400";
      ClassLoader thisLoader = Class.forName(CLASSNAME).getClassLoader();
      if (thisLoader != null) {
        URL thisUrl = thisLoader.getResource(thisFileName);
        if (thisUrl != null) {
          String thisPath = thisUrl.getPath();
          int bangIndex = thisPath.indexOf("!");
          if ( bangIndex > 0) {
            String jarFile = thisPath.substring(5,bangIndex); 
              
            ZipFile zipFile = new ZipFile(jarFile); 
            // "BeanInfo.class
            String slashPackage = packageName.replace('.', '/'); 
            Enumeration entries = zipFile.entries(); 
            LinkedList linkedList = new LinkedList();  
            while (entries.hasMoreElements()) {
              ZipEntry entry = (ZipEntry) entries.nextElement();
              String name = entry.getName();
              if (name.indexOf("BeanInfo.class") > 0) { 
                // Check for package also 
                if (name.indexOf(slashPackage) == 0) {
                    String shortName = name.substring(slashPackage.length()+1); 
                    if (shortName.indexOf("/") < 0) { 
                      linkedList.add(shortName); 
                    }
                }
              }
            }
            list = (String[]) linkedList.toArray(new String[0]); 
          } else {
            System.out.println("Path of AS400 class:" + thisPath);

            int as400ClassIndex = thisPath.indexOf(thisFileName);
            if (as400ClassIndex > 0) {
              // not in a jar file. Look for classes using File interface
              String basePath = thisPath.substring(0, as400ClassIndex - 1);
              String directoryPath = basePath + File.separator
                  + packageName.replace('.', File.separatorChar);
              System.out.println("directoryPath is " + directoryPath);
              File directory = new File(directoryPath);
              FilenameFilter classFilter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                  if (name.endsWith("BeanInfo.class")) {
                    return true;
                  } else {
                    return false;
                  }
                }
              };

              list = directory.list(classFilter);

            } else {
              // TODO: Process jar file
              System.out.println("jar file is " + thisFileName);

            }

          }
        }
      }

      for (int i = 0; i < list.length; i++) { 
        String beanInfoClass = list[i]; 
        System.out.println("Checking "+beanInfoClass); 
        int beanInfoIndex = beanInfoClass.indexOf("BeanInfo");
        String className = beanInfoClass.substring(0,beanInfoIndex); 
        try { 
          Class beanClass = Class.forName(packageName+"."+className); 
          java.beans.Introspector.getBeanInfo(beanClass); 
        } catch (Throwable e) { 
          passed = false; 
          sb.append("\n"); 
          sb.append("Class "+className+" failed with \n"); 
          JDTestcase.printStackTraceToStringBuffer(e, sb); 
        }
      }
          
      
      assertCondition(passed, sb); 
    } catch (Exception e) {
      failed(e, "Unexpected Exception.");
    }

  }

    /**
     * Test that we can call java.beans.Introspector.getBeanInfo for all the
     * beans in the com/ibm/as400 package. 
     **/
  public void Var001() {
    testBeanInfo("com.ibm.as400.access");

  }

  
  
  /**
   * Test that we can call java.beans.Introspector.getBeanInfo for all the
   * beans in the com/ibm/resource package. 
   **/
public void Var002() {
  testBeanInfo("com.ibm.as400.resource");

}

/**
 * Test that we can call java.beans.Introspector.getBeanInfo for all the
 * beans in the com/ibm/security/auth package. 
 **/
public void Var003() {
testBeanInfo("com.ibm.as400.security.auth");

}
  
/**
 * Test that we can call java.beans.Introspector.getBeanInfo for all the
 * beans in the com/ibm/util/html package. 
 **/
public void Var004() {
testBeanInfo("com.ibm.as400.util.html");

}

/**
 * Test that we can call java.beans.Introspector.getBeanInfo for all the
 * beans in the com/ibm/access/util/servlet. 
 **/
public void Var005() {
testBeanInfo("com.ibm.as400.util.servlet");

}

/**
 * Test that we can call java.beans.Introspector.getBeanInfo for all the
 * beans in the com/ibm/access/vaccess. 
 **/
public void Var006() {
testBeanInfo("com.ibm.as400.vaccess");

}

  
  
}
