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

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDriverGetPropertyInfo.java
//
// Classes:      JDDriverGetPropertyInfo
//
////////////////////////////////////////////////////////////////////////

package test.JD.Driver;

import com.ibm.as400.access.AS400;

import test.JDDriverTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.util.Hashtable; import java.util.Vector;
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
  public static void main(String args[]) throws Exception { 
    // Note:  reflection is used to get the classname, so this can be pasted easily into other Testcase classes
    String[] newArgs = new String[args.length+2];
    newArgs[0] = "-tc"; 
    newArgs[1] = new Object() { }.getClass().getEnclosingClass().getSimpleName();
    for (int i = 0; i < args.length; i++) { 
      newArgs[2+i]=args[i]; 
    }
    JDDriverTest.main(newArgs); 
  }

  // Private data.
  private int NUMBER_OF_PROPERTIES = 0;

  private Driver driver_;
  private Properties properties_;

  private String leakedPassword_ = null; 

  /**
   * Constructor.
   **/
  public JDDriverGetPropertyInfo(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
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
      NUMBER_OF_PROPERTIES = 106;  // useSock5
      
    } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      int vrm_ = testDriver_.getRelease();

      if (vrm_ == 550 || vrm_ == 610) {
        NUMBER_OF_PROPERTIES = 41; // V5R5 adds decfloat rounding mode,
                                   // qaqqinilib, ignore warnings, commit hold,
                                   // maximum blocked input rows (#40), lob
                                   // block size(#41)
      } else if (vrm_ <= 750) {
        NUMBER_OF_PROPERTIES = 44; // V7R1 added servermode subsystem,
                                   // concurrent access resolution, maximum
                                   // blocked input rows (#42), lob block size
                                   // (#43) (query replace truncated parm) #44

      } else {
        NUMBER_OF_PROPERTIES = 50; // 6 more properties for MFA
      }
    } else if (getDriver() == JDTestDriver.DRIVER_JCC) {
      NUMBER_OF_PROPERTIES = 2;
    }
    properties_ = new Properties();
    properties_.put("user", userId_);
    leakedPassword_ = PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverGetPropertyInfo.s"); 
    properties_.put("password", leakedPassword_);
  }

  /**
   * getPropertyInfo() - Verify that the number of properties returned is
   * correct. Check userid password are correct. Also check the specified 
   * property
   **/
  public void Var001() {
    testProperty(null,null); 
  }
  
  public void testProperty(String prop, String value) {
    testProperty(prop,value,value); 
  }
  public void testProperty(String prop, String value, String inExpectedValue) {
    StringBuffer sb = new StringBuffer(); 

    try {

      String url = baseURL_;
      if (prop != null) { 
        url = url +";"+prop+"="+value; 
      }
      Properties properties = new Properties(properties_); 
      if (prop != null) { 
        properties.setProperty(prop, inExpectedValue);
      } 
      DriverPropertyInfo[] propertyInfo = driver_.getPropertyInfo(url,
          properties_);

      boolean passed = true; 
      sb.append("\nurl="+url); 
      sb.append("\nDriver is "+driver_+" class="+driver_.getClass()) ;
      sb.append("\ndriver_.getPropertyInfo called"); 
      sb.append("\nProperty Count = " + propertyInfo.length);
      
      for (int i = 0; i < propertyInfo.length; i++) {
        if (propertyInfo[i] == null) {
          passed = false;
          sb.append("\n *** propertyInfo[" + i + "] = null ");
        } else {
          sb.append("\n[" + i + "]<" + propertyInfo[i].name + "><" + propertyInfo[i].description + "><"
            + propertyInfo[i].value + ">");
          
          // Check the expected values 
          String expectedValue =  properties.getProperty(propertyInfo[i].name); 
          if (propertyInfo[i].name.equals("password") && (getDriver() == JDTestDriver.DRIVER_TOOLBOX)) {
            expectedValue = "";          /* Toolbox does not return password */ 
          }
          if (expectedValue != null) { 
             if (! expectedValue.equals(propertyInfo[i].value)) {
               passed = false; 
               sb.append("\n *** for property "+propertyInfo[i].name+" expected "+expectedValue+" but got "+propertyInfo[i].value) ;
             }
          } else {
            // Check that password and userid do not show up elsewhere
            if (userId_.equals(propertyInfo[i].value)) { 
              passed = false; 
              sb.append("\n *** found userId_="+userId_+" for property "+propertyInfo[i].name); 
            }
            if (leakedPassword_.equals(propertyInfo[i].value)) {
              passed = false; 
              sb.append("\n *** found leakedPassword_="+leakedPassword_+" for property "+propertyInfo[i].name); 
            }
          }
          if (propertyInfo[i].description == null) {
            /* If not set, do not do check.  May not be set for native Driver */ 
          } else {
            if (propertyInfo[i].description.indexOf("999") >= 0) {
              if (!"REMOVED".equals(propertyInfo[i].name)) {
                passed = false;
                sb.append("\n **** Bad property description  found");
              }
            }
          }
        }
      }

      if (propertyInfo.length != NUMBER_OF_PROPERTIES) { 
        passed = false; 
        sb.append("\npropertyInfo.length = " + propertyInfo.length
            + " AND SHOULD BE equal to NUMBER_OF_PROPERTIES = "
            + NUMBER_OF_PROPERTIES);
      }
      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, sb);
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
          output_.println(propertyInfo[i].name);
          if (propertyInfo[i].choices != null) {
            output_.println(propertyInfo[i].choices.length);
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
          // output_.println("property name = "+ propertyInfo[i].name);
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

      Hashtable<String,String> as400JdbcDataSourceMethods = getMethods("com.ibm.as400.access.AS400JDBCDataSource");
      Hashtable<String,String> as400JdbcManagedDataSourceMethods = getMethods("com.ibm.as400.access.AS400JDBCManagedDataSource");
      Hashtable<String,String> as400JdbcDataSourceBeanMethods = getBeanMethods("com.ibm.as400.access.AS400JDBCDataSource");

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
        // output_.println("looking at "+property);
        String getMethodName = generateGetMethodName(property);
        String setMethodName = "s" + getMethodName.substring(1);
        String[] choices = thisProperty.choices;
        if (choices.length == 2) {
          if (((choices[0].equals("true")) && (choices[1].equals("false")))
              || ((choices[1].equals("true")) && (choices[0].equals("false")))) {
            getMethodName = "is" + getMethodName.substring(3);
          }
        }
        // output_.println("  get="+getMethodName);
        // output_.println("  set="+setMethodName);

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
              + " does not exist in AS400JDBCDataSource.getBeanMethods");
          passed = false;
        }

        if (!methodExists(setMethodName, as400JdbcDataSourceBeanMethods)) {
          sb.append("\nMethod " + setMethodName
              + " does not exist in AS400JDBCDataSource.getBeanMethods");
          passed = false;
        }

      }

      assertCondition(passed,sb);
    } catch (Throwable e) {
      failed(e, "Unexpected Exception");
    }
  }

  private boolean methodExists(String methodName, Hashtable<String,String> hashtable) {

    if (hashtable.get(methodName) != null) {
      return true;
    } else {
      return false;
    }
  }

  private Hashtable<String,String> getBeanMethods(String string)
      throws ClassNotFoundException, IntrospectionException {
    Class<?> thisClass = Class.forName(string);
    Hashtable<String,String> returnMethods = new Hashtable<String,String>();

    // Also check out the bean information
    BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(thisClass);
    PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

    for (int i = 0; i < descriptors.length; i++) {
      PropertyDescriptor descriptor = descriptors[i];
      // output_.println(descriptor.getName());
      Method readMethod = descriptor.getReadMethod();
      if (readMethod != null) {
        String name = readMethod.getName();
        returnMethods.put(name, name);
        // output_.println(name);
      } else {
        // output_.println("READ METHOD IS NULL");
      }
      Method writeMethod = descriptor.getWriteMethod();
      if (writeMethod != null) {
        String name = writeMethod.getName();
        // output_.println(writeMethod.getName());
        returnMethods.put(name, name);
      } else {
        // output_.println("READ METHOD IS NULL");
      }
    }

    return returnMethods;
  }

  private Hashtable<String,String> getMethods(String string) throws ClassNotFoundException {
    Class<?> thisClass = Class.forName(string);
    Method[] methods = thisClass.getMethods();
    Hashtable<String,String> returnMethods = new Hashtable<String,String>();
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

  
  public void Var010() { testProperty("access","read only"); }
  public void Var011() { testProperty("batch style","2.1"); }
  public void Var012() { testProperty("block size","512"); }
  public void Var013() { testProperty("blocking enabled","true"); }
  public void Var014() { testProperty("cursor hold","false"); }
  public void Var015() { testProperty("data truncation","true"); }
  public void Var016() { testProperty("date format","jis"); }
  public void Var017() { testProperty("date separator","."); }
  public void Var018() { testProperty("decimal separator",","); }
  public void Var019() { testProperty("do escape processing","false"); }
  public void Var020() { testProperty("errors","full"); }
  public void Var021() { testProperty("extended metadata","true"); }
  public void Var022() { testProperty("libraries","QGPL,QJVA"); }
  public void Var023() { testProperty("lob threshold","500000"); }
  public void Var024() { testProperty("naming","system"); }
  public void Var025() { notApplicable("password test");  }
  public void Var026() { testProperty("prefetch","false"); }
  public void Var027() { testProperty("reuse objects","false"); }
  public void Var028() { testProperty("time format","jis"); }
  public void Var029() { testProperty("time separator","."); }
  public void Var030() { testProperty("trace","true"); }
  public void Var031() { testProperty("server trace","true"); }
  public void Var032() { testProperty("transaction isolation","read committed"); }
  public void Var033() { testProperty("translate binary","true"); }
  public void Var034() { testProperty("use block insert","true"); }
  public void Var035() { testProperty("user",userId_); }
  public void Var036() { testProperty("behavior override","1"); }
  public void Var037() { testProperty("auto commit","false"); }
  public void Var038() { testProperty("maximum precision","63"); }
  public void Var039() { testProperty("maximum scale","63"); }
  public void Var040() { testProperty("minimum divide scale","9"); }
  public void Var041() { testProperty("translate hex","binary"); }
  public void Var042() { testProperty("cursor sensitivity","sensitive"); }
  public void Var043() { if (checkNative()) testProperty("direct map","false"); }
  public void Var044() { testProperty("query optimize goal","1"); }
  public void Var045() { testProperty("decfloat rounding mode","round ceiling"); }
  public void Var046() { if (checkNative()) testProperty("qaqqinilib","QGPL"); }
  public void Var047() { if (checkNative()) testProperty("ignore warnings","0100C"); }
  public void Var048() { testProperty("commit hold","false"); }
  public void Var049() { if (checkNative()) testProperty("servermode subsystem","QUSRWRK"); }
  public void Var050() { if (checkNative()) testProperty("concurrent access resolution","3"); }
  public void Var051() { if (checkNative()) testProperty("maximum blocked input rows","16000"); }
  public void Var052() { testProperty("lob block size","4194304"); }
  public void Var053() { if (checkNative())  testProperty("query replace truncated parameter","XXXXX"); }
  public void Var054() { testProperty("additionalAuthenticationFactor","123456"); }
  public void Var055() { testProperty("authenticationVerificationId","VerificationId"); }
  public void Var056() { testProperty("authenticationLocalIP","1.2.3.4"); }
  public void Var057() { testProperty("authenticationLocalPort","30"); }
  public void Var058() { testProperty("authenticationRemoteIP","2.3.4.5"); }
  public void Var059() { testProperty("authenticationRemotePort","50"); }

}
