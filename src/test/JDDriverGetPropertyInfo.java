///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDriverGetPropertyInfo.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDriverGetPropertyInfo.java
//
// Classes:      JDDriverGetPropertyInfo
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Testcase JDDriverGetPropertyInfo. This tests the following method of the JDBC
 * Driver class:
 * 
 * <ul>
 * <li>getPropertyInfo()
 * </ul>
 **/
public class JDDriverGetPropertyInfo extends JDTestcase {

  // Private data.
  private int NUMBER_OF_PROPERTIES = 0;

  private Driver driver_;
  private Properties properties_;

  /**
   * Constructor.
   **/
  public JDDriverGetPropertyInfo(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDDriverGetPropertyInfo", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    driver_ = DriverManager.getDriver(baseURL_);

    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      NUMBER_OF_PROPERTIES = 103;  // stay alive 
      
    } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      int vrm_ = testDriver_.getRelease();

      if (vrm_ == 550 || vrm_ == 610) {
        NUMBER_OF_PROPERTIES = 41; // V5R5 adds decfloat rounding mode,
                                   // qaqqinilib, ignore warnings, commit hold,
                                   // maximum blocked input rows (#40), lob
                                   // block size(#41)
      } else {
        NUMBER_OF_PROPERTIES = 44; // V7R1 added servermode subsystem,
                                   // concurrent access resolution, maximum
                                   // blocked input rows (#42), lob block size
                                   // (#43) (query replace truncated parm) #44

      }
    } else if (getDriver() == JDTestDriver.DRIVER_JCC) {
      NUMBER_OF_PROPERTIES = 2;
    }
    properties_ = new Properties();
    properties_.put("user", userId_);
    properties_.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverGetPropertyInfo.s"));
  }

  /**
   * getPropertyInfo() - Verify that the number of properties returned is
   * correct.
   **/
  public void Var001() {

    try {
      DriverPropertyInfo[] propertyInfo = driver_.getPropertyInfo(baseURL_,
          properties_);
      boolean passed = (propertyInfo.length == NUMBER_OF_PROPERTIES)
          || (propertyInfo.length == NUMBER_OF_PROPERTIES + 1);
      assertCondition(passed, "propertyInfo.length = " + propertyInfo.length
          + " AND SHOULD BE equal to NUMBER_OF_PROPERTIES = "
          + NUMBER_OF_PROPERTIES);
      if (!passed) {

        System.out.println("Property Count = " + propertyInfo.length);

        for (int i = 0; i < propertyInfo.length; i++) {
          System.out.println("[" + i + "]<" + propertyInfo[i].name + "><"
              + propertyInfo[i].description + "><" + propertyInfo[i].value
              + ">");
        }
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getPropertyInfo () - Check that a specific property passed in is returned
   * in the list of properties.
   **/
  public void Var002() {
    try {
      DriverPropertyInfo[] propertyInfo = driver_.getPropertyInfo(baseURL_,
          properties_);

      boolean found = false;
      for (int i = 0; i < propertyInfo.length; ++i)
        if (propertyInfo[i].name.equals("user"))
          if (propertyInfo[i].value.equals(userId_))
            found = true;

      assertCondition(found);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getPropertyInfo() - Check that choices are returned.
   **/
  public void Var003() {
    try {
      DriverPropertyInfo[] propertyInfo = driver_.getPropertyInfo(baseURL_,
          properties_);

      boolean found = false;
      if (getDriver() == JDTestDriver.DRIVER_JCC) {

        for (int i = 0; i < propertyInfo.length; ++i) {
          System.out.println(propertyInfo[i].name);
          if (propertyInfo[i].choices != null) {
            System.out.println(propertyInfo[i].choices.length);
          }

        }
        succeeded("JCC Driver -- Does not give all possible properties see http://publib.boulder.ibm.com/infocenter/db2luw/v8/topic/com.ibm.db2.udb.doc/ad/rjvdsprp.htm");
      } else {
        for (int i = 0; i < propertyInfo.length; ++i) {
          if (propertyInfo[i].name.equals("naming")) {
            if ((propertyInfo[i].choices.length == 2)
                && (propertyInfo[i].choices[0].equals("sql"))
                && (propertyInfo[i].choices[1].equals("system")))
              found = true;
          }
        }
        assertCondition(found);
      }

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getPropertyInfo() - Should return null when the URL is not ours.
   */
  public void Var004() {
    try {
      DriverPropertyInfo[] propertyInfo = driver_.getPropertyInfo("jdbc:odbc:"
          + system_, properties_);
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        if (propertyInfo != null) {
          succeeded("JCC Driver returned properties for odbc URL");
        } else {
          assertCondition(false, "propertyInfo == null");
        }
      } else {
        assertCondition(propertyInfo == null);
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getPropertyInfo() - Should return null when the URL is null.
   **/
  public void Var005() {
    try {
      DriverPropertyInfo[] propertyInfo = driver_.getPropertyInfo(null,
          properties_);
      if (getDriver() == JDTestDriver.DRIVER_JCC) {

        if (propertyInfo != null) {
          succeeded("JCC Driver returned properties for null URL");
        } else {
          assertCondition(false, "propertyInfo == null");
        }
      } else {
        assertCondition(propertyInfo == null);
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getPropertyInfo() - Check that all properties are returned when the input
   * properties are null.
   **/
  public void Var006() {
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
        && getRelease() == JDTestDriver.RELEASE_V5R4M0
        && "OS/400".equals(System.getProperty("os.name"))) {
      notApplicable("Toolbox on V5R4 on OS/400 not working");
      return;
    }
    try {
      DriverPropertyInfo[] propertyInfo = driver_.getPropertyInfo(baseURL_,
          null);
      boolean passed = (propertyInfo.length == NUMBER_OF_PROPERTIES)
          || (propertyInfo.length == NUMBER_OF_PROPERTIES + 1);
      assertCondition(passed, "propertyInfo.length = " + propertyInfo.length
          + " AND SHOULD BE equal to NUMBER_OF_PROPERTIES = "
          + NUMBER_OF_PROPERTIES);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getPropertyInfo() - Check that all properties are returned when the bogus
   * properties are set.
   **/
  public void Var007() {
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX
        && getRelease() == JDTestDriver.RELEASE_V5R4M0
        && "OS/400".equals(System.getProperty("os.name"))) {
      notApplicable("Toolbox on V5R4 on OS/400 not working");
      return;
    }
    try {
      Properties properties = new Properties();
      properties.put("bogus", "property");
      DriverPropertyInfo[] propertyInfo = driver_.getPropertyInfo(baseURL_,
          properties);
      boolean passed = (propertyInfo.length == NUMBER_OF_PROPERTIES)
          || (propertyInfo.length == NUMBER_OF_PROPERTIES + 1);

      assertCondition(passed, "propertyInfo.length = " + propertyInfo.length
          + " AND SHOULD BE equal to NUMBER_OF_PROPERTIES = "
          + NUMBER_OF_PROPERTIES);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getPropertyInfo () - Check DECFLOAT properties
   **/
  public void Var008() {
    if (checkDecFloatSupport()) {
      String expectedValues[] = { "round half even", "round half up",
          "round down", "round ceiling", "round floor", "round half down",
          "round up", };
      String toolboxExpectedValues[] = { // @H4A
      "half even", "half up", "down", "ceiling", "floor", "half down", "up", };
      boolean expectedFound[] = { false, false, false, false, false, false,
          false, };
      StringBuffer sb = new StringBuffer();
      boolean success = true;
      try {
        DriverPropertyInfo[] propertyInfo = driver_.getPropertyInfo(baseURL_,
            properties_);

        boolean found = false;
        for (int i = 0; i < propertyInfo.length; ++i) {
          sb.append("Property " + i + " = " + propertyInfo[i] + "\n");
          // System.out.println("property name = "+ propertyInfo[i].name);
          if (propertyInfo[i].name.equals("decfloat rounding mode")) {
            found = true;
            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) // @H4A
            {
              if (!propertyInfo[i].value.equals("half even")) {
                success = false;
                sb.append("Expected value 'half even' but found "
                    + propertyInfo[i].value);
              }
            } else {
              if (!propertyInfo[i].value.equals("round half even")) {
                success = false;
                sb.append("Expected value 'round half even' but found "
                    + propertyInfo[i].value);
              }
            }
            String[] choices = propertyInfo[i].choices;
            for (int j = 0; j < choices.length; j++) {
              boolean foundOne = false;
              if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) // @H4A
              {
                for (int k = 0; k < toolboxExpectedValues.length && !foundOne; k++) {
                  if (choices[j].equals(toolboxExpectedValues[k])) {
                    foundOne = true;
                    expectedFound[k] = true;
                  }
                }
              }
              for (int k = 0; k < expectedValues.length && !foundOne; k++) {
                if (choices[j].equals(expectedValues[k])) {
                  foundOne = true;
                  expectedFound[k] = true;
                }
              }
              if (!foundOne) {
                success = false;
                sb.append("\nDid not find returned value " + choices[j]
                    + " as a valid value");
              }
            }
            for (int k = 0; k < expectedValues.length; k++) {
              if (!expectedFound[k]) {
                sb.append("\nDid not find expected value " + expectedValues[k]);
              }
            }

          }
        }
        assertCondition(found && success,
            "found=" + found + " " + sb.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  public void Var009() {

    if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
      notApplicable("Toolbox only testcase");
      return;
    }

    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    try {
      DriverPropertyInfo[] propertyInfo = driver_.getPropertyInfo(baseURL_,
          null);

      Hashtable as400JdbcDataSourceMethods = getMethods("com.ibm.as400.access.AS400JDBCDataSource");
      Hashtable as400JdbcManagedDataSourceMethods = getMethods("com.ibm.as400.access.AS400JDBCManagedDataSource");
      Hashtable as400JdbcDataSourceBeanMethods = getBeanMethods("com.ibm.as400.access.AS400JDBCDataSource");

      // Add methods we do not care about
      String[] ignoreMethods = { "getPassword", "getKeyRingName",
          "setKeyRingName", "getKeyRingPassword", "setKeyRingPassword",
          "getREMOVED", "setREMOVED"

      };
      for (int i = 0; i < ignoreMethods.length; i++) {
        as400JdbcDataSourceMethods.put(ignoreMethods[i], ignoreMethods[i]);
        as400JdbcManagedDataSourceMethods.put(ignoreMethods[i],
            ignoreMethods[i]);
        as400JdbcDataSourceBeanMethods.put(ignoreMethods[i], ignoreMethods[i]);
      }

      for (int i = 0; i < propertyInfo.length; i++) {
        DriverPropertyInfo thisProperty = propertyInfo[i];
        String property = thisProperty.name;
        // System.out.println("looking at "+property);
        String getMethodName = generateGetMethodName(property);
        String setMethodName = "s" + getMethodName.substring(1);
        String[] choices = thisProperty.choices;
        if (choices.length == 2) {
          if (((choices[0].equals("true")) && (choices[1].equals("false")))
              || ((choices[1].equals("true")) && (choices[0].equals("false")))) {
            getMethodName = "is" + getMethodName.substring(3);
          }
        }
        // System.out.println("  get="+getMethodName);
        // System.out.println("  set="+setMethodName);

        if (!methodExists(getMethodName, as400JdbcDataSourceMethods)) {
          sb.append("\nMethod " + getMethodName
              + " does not exist in as400JdbcDataSource");
          passed = false;
        }

        if (!methodExists(setMethodName, as400JdbcDataSourceMethods)) {
          sb.append("\nMethod " + setMethodName
              + " does not exist in as400JdbcDataSource");
          passed = false;
        }

        if (!methodExists(getMethodName, as400JdbcManagedDataSourceMethods)) {
          sb.append("\nMethod " + getMethodName
              + " does not exist in as400JdbcManagedDataSource");
          passed = false;
        }

        if (!methodExists(setMethodName, as400JdbcManagedDataSourceMethods)) {
          sb.append("\nMethod " + setMethodName
              + " does not exist in as400JdbcManagedDataSource");
          passed = false;
        }

        if (!methodExists(getMethodName, as400JdbcDataSourceBeanMethods)) {
          sb.append("\nMethod " + getMethodName
              + " does not exist in As400JdbcDataSourceBeanInfo");
          passed = false;
        }

        if (!methodExists(setMethodName, as400JdbcDataSourceBeanMethods)) {
          sb.append("\nMethod " + setMethodName
              + " does not exist in As400JdbcDataSourceBeanInfo");
          passed = false;
        }

      }

      assertCondition(passed,sb);
    } catch (Throwable e) {
      failed(e, "Unexpected Exception");
    }
  }

  private boolean methodExists(String methodName, Hashtable hashtable) {

    if (hashtable.get(methodName) != null) {
      return true;
    } else {
      return false;
    }
  }

  private Hashtable getBeanMethods(String string)
      throws ClassNotFoundException, IntrospectionException {
    Class thisClass = Class.forName(string);
    Hashtable returnMethods = new Hashtable();

    // Also check out the bean information
    BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(thisClass);
    PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

    for (int i = 0; i < descriptors.length; i++) {
      PropertyDescriptor descriptor = descriptors[i];
      // System.out.println(descriptor.getName());
      Method readMethod = descriptor.getReadMethod();
      if (readMethod != null) {
        String name = readMethod.getName();
        returnMethods.put(name, name);
        // System.out.println(name);
      } else {
        // System.out.println("READ METHOD IS NULL");
      }
      Method writeMethod = descriptor.getWriteMethod();
      if (writeMethod != null) {
        String name = writeMethod.getName();
        // System.out.println(writeMethod.getName());
        returnMethods.put(name, name);
      } else {
        // System.out.println("READ METHOD IS NULL");
      }
    }

    return returnMethods;
  }

  private Hashtable getMethods(String string) throws ClassNotFoundException {
    Class thisClass = Class.forName(string);
    Method[] methods = thisClass.getMethods();
    Hashtable returnMethods = new Hashtable();
    for (int i = 0; i < methods.length; i++) {
      String name = methods[i].getName();
      returnMethods.put(name, name);
    }
    return returnMethods;
  }

  private String generateGetMethodName(String property) {
    StringBuffer getMethodName = new StringBuffer("get");
    int leftIndex = 0;
    int spaceIndex = property.indexOf(" ", leftIndex);
    while (spaceIndex >= 0) {
      getMethodName.append(property.substring(leftIndex, leftIndex + 1)
          .toUpperCase());
      getMethodName.append(property.substring(leftIndex + 1, spaceIndex));
      leftIndex = spaceIndex + 1;
      spaceIndex = property.indexOf(" ", leftIndex);
    }
    getMethodName.append(property.substring(leftIndex, leftIndex + 1)
        .toUpperCase());
    getMethodName.append(property.substring(leftIndex + 1));

    return getMethodName.toString();
  }

}
