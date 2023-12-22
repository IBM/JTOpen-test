///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDReflectionUtil.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.reflect.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;

import java.util.Enumeration;

/**
Utility Calls JDReflectionUtil.
This is used to call new methods of changed classes.  This will
permit the testcases to compile using older jar files.

**/
public class JDReflectionUtil {
    static Object dummyObject = null;
    static Class objectClass = null;
    static int jdk;
    static {
	dummyObject = new Object();
	objectClass = dummyObject.getClass();
	jdk = JVMInfo.getJDK(); 
    }

    public static void handleIte(java.lang.reflect.InvocationTargetException ite) throws Exception  {
	Throwable target = ite.getTargetException();
	if (target instanceof Exception) {
	    throw (Exception) target;
	} else {
	    target.printStackTrace();
	    throw new Exception("Throwable "+target.toString()+" encountered.  See STDOUT for stack trace ");
	}

    }

    /**
     * call a method which returns an Object
     *
     * Examples
     *
     *  Object o = callMethod_O(ds, ... ");
     */
     public static Object callMethod_O(Object o, String methodName, Class argType, Object p1 ) throws Exception {
        java.lang.reflect.Method method;

        Class thisClass = o.getClass();
        // System.out.println("Class of object is "+thisClass);
        Class argTypes[] = new Class[1];
        argTypes[0] = argType;
        method = thisClass.getMethod(methodName, argTypes);
	// In JDK 11, we cannot call setAccesible without causing a package error 
       if (jdk  < JVMInfo.JDK_V11)  method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[1];
        args[0] = p1;
        try {
            // System.out.println("Calling method");
            Object outObject = method.invoke(o, args);
            // System.out.println("outObject is "+outObject);
            return  outObject;
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);
            return "";
        }


     }



   /**
    * call a method which returns an Object
    *
    * Examples
    *
    *  Object o = callMethod_O(ds, ... ");
    */
    public static Object callMethod_O(Object o, String methodName, Class[] argTypes, Object p1, Object p2 ) throws Exception {
	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	// System.out.println("Class of object is "+thisClass);

	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[2];
	args[0] = p1;
	args[1] = p2;
	try {
	    // System.out.println("Calling method");
	    Object outObject = method.invoke(o, args);
	    // System.out.println("outObject is "+outObject);
	    return  outObject;
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	    return "";
	}


    }

    public static Object  callMethod_O(Object  o, String methodName, int p1, Class p2) throws Exception {
      java.lang.reflect.Method method;

      Class thisClass = o.getClass();
      Class[] argTypes = new Class[2];
      argTypes[0] = Integer.TYPE;
      argTypes[1] = Class.class;

      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked
      Object[] args = new Object[2];
      args[0] = new Integer(p1);
      args[1] = p2;
      try {
          Object outObject = method.invoke(o, args);
          return  outObject;
      } catch (java.lang.reflect.InvocationTargetException ite) {
          handleIte(ite);
          return "";
      }

    }
    
    public static Object callMethod_O(Object o, String methodName,
        String p1) throws Exception {
      java.lang.reflect.Method method;

      Class thisClass = o.getClass();
      Class[] argTypes = new Class[1];
      argTypes[0] = String.class; 

      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked
      Object[] args = new Object[1];
      args[0] = p1;
      try {
          Object outObject = method.invoke(o, args);
          return  outObject;
      } catch (java.lang.reflect.InvocationTargetException ite) {
          handleIte(ite);
          return "";
      }

      
    }
    

   /**
    * call a method which returns an Object
    *
    * Examples
    *
    *  Object o = callMethod_O(ds, ... ");
    */
    
	public static Object callMethod_O(Object o, String methodName, String p1,
			char[] p2, char[] p3) throws Exception {
		Class[] argTypes = new Class[3]; 
		argTypes[0]=Class.forName("java.lang.String"); 
		argTypes[1]=Class.forName("[C"); 
		argTypes[2]=Class.forName("[C"); 
		return callMethod_O(o, methodName, argTypes, p1,p2,p3); 
	}

    public static Object callMethod_O(Object o, String methodName, Class[] argTypes, Object p1, Object p2, Object p3 ) throws Exception {
	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	// System.out.println("Class of object is "+thisClass);

	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked

	Object[] args = new Object[3];
	args[0] = p1;
	args[1] = p2;
	args[2] = p3;
	try {
	    // System.out.println("Calling method");
	    Object outObject = method.invoke(o, args);
	    // System.out.println("outObject is "+outObject);
	    return outObject;
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	    return "";
	}


    }


   /**
    * call a method which returns an Object
    *
    * Examples
    *
    *  Object o = callMethod_O(ds, ... ");
    */
    public static Object callMethod_O(Object o, String methodName, Class[] argTypes, Object p1, Object p2, Object p3, Object p4 ) throws Exception {
	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	// System.out.println("Class of object is "+thisClass);

	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[4];
	args[0] = p1;
	args[1] = p2;
	args[2] = p3;
	args[3] = p4;
	try {
	    // System.out.println("Calling method");
	    Object outObject = method.invoke(o, args);
	    // System.out.println("outObject is "+outObject);
	    return outObject;
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	    return "";
	}


    }

   /**
    * call a method which returns an Object
    *
    * Examples
    *
    *  Object o = callMethod_O(ds, ... ");
    */
    public static Object callMethod_O(Object o, String methodName)  throws Exception {
	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	// System.out.println("Class of object is "+thisClass);
	Class[] argTypes = new Class[0];
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[0];
	try {
	    // System.out.println("Calling method");
	    Object outObject = method.invoke(o, args);
	    // System.out.println("outObject is "+outObject);
	    return outObject;
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	    return "";
	}

    }


    /**
     * call a method which returns an Object
     *
     * Examples
     *
     *  Object o = callMethod_O(ds, ... ");
     */
     public static Object callMethod_O(Object o, String methodName, int i )  throws Exception {
        java.lang.reflect.Method method;

        Class thisClass = o.getClass();
        // System.out.println("Class of object is "+thisClass);
        Class[] argTypes = new Class[1];
        argTypes[0] = Integer.TYPE ;
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[1];
        args[0] = new Integer(i);
        try {
            // System.out.println("Calling method");
            Object outObject = method.invoke(o, args);
            // System.out.println("outObject is "+outObject);
            return outObject;
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);
            return "";
        }

     }


     public static Object callMethod_O(Object o, String methodName, long i )  throws Exception {
       java.lang.reflect.Method method;

       Class thisClass = o.getClass();
       // System.out.println("Class of object is "+thisClass);
       Class[] argTypes = new Class[1];
       argTypes[0] = Long.TYPE ;
       method = thisClass.getMethod(methodName, argTypes);
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[1];
       args[0] = new Long(i);
       try {
           // System.out.println("Calling method");
           Object outObject = method.invoke(o, args);
           // System.out.println("outObject is "+outObject);
           return outObject;
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);
           return "";
       }

    }

     



   /**
    * call a method which returns an Object
    *
    * Examples
    *
    *  Object o = callMethod_O(ds, ... ");
    */
    public static Object callMethod_O(Object o, String methodName, Class c)  throws Exception {

	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	// System.out.println("Class of object is "+thisClass);
	Class[] argTypes = new Class[1];
	argTypes[0] = Class.forName("java.lang.Class");
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[1];
	args[0] = c;

	try {
	    // System.out.println("Calling method");
	    Object outObject = method.invoke(o, args);
	    // System.out.println("outObject is "+outObject);
	    return outObject;
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	    return "";
	}

    }

    /**
     * call a method which returns an Object
     *
     * Examples
     *
     *  Object o = callMethod_O(ds, ... ");
     */
     public static Object callMethod_OS(Object o, String methodName, String s)  throws Exception {

        java.lang.reflect.Method method;

        Class thisClass = o.getClass();
        // System.out.println("Class of object is "+thisClass);
        Class[] argTypes = new Class[1];
        argTypes[0] = Class.forName("java.lang.String");
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[1];
        args[0] = s;

        try {
            // System.out.println("Calling method");
            Object outObject = method.invoke(o, args);
            // System.out.println("outObject is "+outObject);
            return outObject;
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);
            return "";
        }

     }

     public static Object callMethod_OSS(Object o, String methodName, String s1, String s2)  throws Exception {

       java.lang.reflect.Method method;

       Class thisClass = o.getClass();
       // System.out.println("Class of object is "+thisClass);
       Class[] argTypes = new Class[2];
       argTypes[0] = Class.forName("java.lang.String");
       argTypes[1] = argTypes[0];
       method = thisClass.getMethod(methodName, argTypes);
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[2];
       args[0] = s1;
       args[1] = s2;
       try {
           // System.out.println("Calling method");
           Object outObject = method.invoke(o, args);
           // System.out.println("outObject is "+outObject);
           return outObject;
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);
           return "";
       }

    }

     //created for Connection.createArrayOf()
     public static Object callMethod_OSA(Object o, String methodName, String s1, Object[] o2)  throws Exception {

       java.lang.reflect.Method method;

       Class thisClass = o.getClass();
       // System.out.println("Class of object is "+thisClass);
       Class[] argTypes = new Class[2];
       argTypes[0] = Class.forName("java.lang.String");
       argTypes[1] = Object[].class; //parm type is Object[]

       method = thisClass.getMethod(methodName, argTypes);
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[2];
       args[0] = s1;
       args[1] = o2;
       try {
           // System.out.println("Calling method");
           Object outObject = method.invoke(o, args);
           // System.out.println("outObject is "+outObject);
           return outObject;
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);
           return "";
       }

    }

     public static Object callMethod_OSSS(Object o, String methodName, String s1, String s2, String s3)  throws Exception {

       java.lang.reflect.Method method;

       Class thisClass = o.getClass();
       // System.out.println("Class of object is "+thisClass);
       Class[] argTypes = new Class[3];
       argTypes[0] = Class.forName("java.lang.String");
       argTypes[1] = argTypes[0];
       argTypes[2] = argTypes[0];
       method = thisClass.getMethod(methodName, argTypes);
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[3];
       args[0] = s1;
       args[1] = s2;
       args[2] = s3;
       try {
           // System.out.println("Calling method");
           Object outObject = method.invoke(o, args);
           // System.out.println("outObject is "+outObject);
           return outObject;
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);
           return "";
       }

    }

     public static Object callMethod_OSSSS(Object o, String methodName, String s1, String s2, String s3, String s4)  throws Exception {

       java.lang.reflect.Method method;

       Class thisClass = o.getClass();
       // System.out.println("Class of object is "+thisClass);
       Class[] argTypes = new Class[4];
       argTypes[0] = Class.forName("java.lang.String");
       argTypes[1] = argTypes[0];
       argTypes[2] = argTypes[0];
       argTypes[3] = argTypes[0];
       method = thisClass.getMethod(methodName, argTypes);
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[4];
       args[0] = s1;
       args[1] = s2;
       args[2] = s3;
       args[3] = s4;
       try {
           // System.out.println("Calling method");
           Object outObject = method.invoke(o, args);
           // System.out.println("outObject is "+outObject);
           return outObject;
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);
           return "";
       }

    }




     /**
      * call a method which returns an Object
      *
      * Examples
      *
      *  Object o = callMethod_O(ds, ... ");
      */
      public static Object callMethod_O(Object o, String methodName, String s, Object parm2)  throws Exception {

         java.lang.reflect.Method method;

         Class thisClass = o.getClass();
         // System.out.println("Class of object is "+thisClass);
         Class[] argTypes = new Class[2];
         argTypes[0] = Class.forName("java.lang.String");
         method = null;
         Object[] args = new Object[2];
         args[0] = s;
         args[1] = parm2;


         if (parm2 == null) {
           throw new Exception("Unable to handle null parameter");
         }
         Class checkClass = parm2.getClass();
         String tryArgs = "";
         while (method == null && checkClass != null) {
           try {
             tryArgs += "(Ljava/lang/String;" + checkClass.getName() + ") ";
             argTypes[1] = checkClass;
             method = thisClass.getMethod(methodName, argTypes);
             method.setAccessible(true); //allow toolbox proxy methods to be invoked

           } catch (Exception e) {

           }
           if (method == null) {
             if (checkClass.getName().equals("java.lang.Object")) {
               checkClass = null;

             } else {
               checkClass = checkClass.getSuperclass();
             }

           } else {
             // System.out.println("Found method "+method);
           }
         }

         if (checkClass == null) {
           checkClass = parm2.getClass();
           // Did not find a base class.. Now look for implements
           while (method == null && checkClass != null) {
             try {

               // Find the implementes for the class
               Class[] interfaces = checkClass.getInterfaces();
               for (int j = 0; method == null && j < interfaces.length; j++) {
                 tryArgs += "(Ljava/lang/String;" + interfaces[j].getName() + ") ";
		 argTypes[1] = interfaces[j];
		 try {
		     method = thisClass.getMethod(methodName, argTypes);
		     method.setAccessible(true); //allow toolbox proxy methods to be invoked
		 } catch (NoSuchMethodException e) {
		     // keep going
		 }
               }
             } catch (Exception e) {

               e.printStackTrace();
             }
             if (method == null) {
               if (checkClass.getName().equals("java.lang.Object")) {
                 checkClass = null;

               } else {
                 checkClass = checkClass.getSuperclass();
               }
             } else {
               // System.out.println("Found method "+method);
             }

           }

         }

         if (method == null) {
           throw new Exception("Unable to find method:  tried "+tryArgs);
         }


         try {
             // System.out.println("Calling method");
             Object outObject = method.invoke(o, args);
             // System.out.println("outObject is "+outObject);
             return outObject;
         } catch (java.lang.reflect.InvocationTargetException ite) {
             handleIte(ite);
             return "";
         }

      }

      /**
       * call a method which returns an Object
       *
       * Examples
       *
       *  Object o = callMethod_O(ds, ... ");
       */
      public static Object callMethod_O(Object o, String methodName, long i, int j )  throws Exception {
        java.lang.reflect.Method method;

        Class thisClass = o.getClass();
        // System.out.println("Class of object is "+thisClass);
        Class[] argTypes = new Class[2];
        argTypes[0] = Long.TYPE ;
        argTypes[1] = Integer.TYPE ;
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[2];
        args[0] = new Long(i);
        args[1] = new Integer(j);
        try {
            // System.out.println("Calling method");
            Object outObject = method.invoke(o, args);
            // System.out.println("outObject is "+outObject);
            return outObject;
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);
            return "";
        }

     }

      public static Object callMethod_O(Object o, String methodName, long i, long j )  throws Exception {
          java.lang.reflect.Method method;

          Class thisClass = o.getClass();
          // System.out.println("Class of object is "+thisClass);
          Class[] argTypes = new Class[2];
          argTypes[0] = Long.TYPE ;
          argTypes[1] = Long.TYPE ;
          method = thisClass.getMethod(methodName, argTypes);
          method.setAccessible(true); //allow toolbox proxy methods to be invoked
          Object[] args = new Object[2];
          args[0] = new Long(i);
          args[1] = new Long(j);
          try {
              // System.out.println("Calling method");
              Object outObject = method.invoke(o, args);
              // System.out.println("outObject is "+outObject);
              return outObject;
          } catch (java.lang.reflect.InvocationTargetException ite) {
              handleIte(ite);
              return "";
          }

       }





   /**
    * call a method which returns a string
    *
    * Examples
    *
    *  String property = callMethod_S(ds, "getTranslateHex");
    */
    public static String callMethod_S(Object o, String methodName) throws Exception {
	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	// System.out.println("Class of object is "+thisClass);
	Class[] argTypes = new Class[0];
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[0];
	try {
	    // System.out.println("Calling method");
	    Object outObject = method.invoke(o, args);
	    // System.out.println("outObject is "+outObject);
	    return (String) outObject;
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	    return "";
	}


    }

    /**
     * call a method which returns a string
     *
     * Examples
     *
     *  String property = callMethod_S(ds, "getTranslateHex");
     */
     public static String callMethod_S(Object o, String methodName, int i) throws Exception {
        java.lang.reflect.Method method;

        Class thisClass = o.getClass();
        // System.out.println("Class of object is "+thisClass);
        Class[] argTypes = new Class[1];
        argTypes[0] = java.lang.Integer.TYPE;
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[1];
        args[0] = new Integer(i);
        try {
            // System.out.println("Calling method");
            Object outObject = method.invoke(o, args);
            // System.out.println("outObject is "+outObject);
            return (String) outObject;
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);
            return "";
        }


     }





    /**
     * call a method which returns a string
     *
     * Examples
     *
     *  String property = JDReflectionUtil.callMethod_S(outNClob, "getSubString", 1, outLength);
     */
     public static String callMethod_S(Object o, String methodName, long l, int j) throws Exception {
        java.lang.reflect.Method method;

        Class thisClass = o.getClass();
        // System.out.println("Class of object is "+thisClass);
        Class[] argTypes = new Class[2];
        argTypes[0] = java.lang.Long.TYPE;
        argTypes[1] = java.lang.Integer.TYPE;
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[2];
        args[0] = new Long(l);
        args[1] = new Integer(j);
        try {
            // System.out.println("Calling method");
            Object outObject = method.invoke(o, args);
            // System.out.println("outObject is "+outObject);
            return (String) outObject;
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);
            return "";
        }


     }


   /**
    * call a method which returns an integer
    *
    * Examples
    *
    *  int value = callMethod_I(ds, "getMaximumPrecision");
    */
    public static int callMethod_I(Object o, String methodName) throws Exception {
	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	Class[] argTypes = new Class[0];
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[0];
	try {
	    Integer i = (Integer) method.invoke(o, args);
	    return i.intValue();
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	    return 0;
	}

    }

   /**
    * call a method which returns an integer
    *
    * Examples
    *
    *  int value = callMethod_I(ds, "getMaximumPrecision");
    */
    public static int callMethod_I(Object o, String methodName, int parm) throws Exception {
	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	Class[] argTypes = new Class[1];
	argTypes[0] = java.lang.Integer.TYPE;
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[1];
	args[0] = new Integer(parm);
	try {
	    Integer i = (Integer) method.invoke(o, args);
	    return i.intValue();
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	    return 0;
	}

    }

    public static int callMethod_I(Object o, String methodName, long parm1,
        String parm2) throws Exception {
      java.lang.reflect.Method method;

      Class thisClass = o.getClass();
      Class[] argTypes = new Class[2];
      argTypes[0] = java.lang.Long.TYPE;
      argTypes[1] = String.class; 
      method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
      Object[] args = new Object[2];
      args[0] = new Long(parm1);
      args[1] = parm2; 
      try {
          Integer i = (Integer) method.invoke(o, args);
          return i.intValue();
      } catch (java.lang.reflect.InvocationTargetException ite) {
          handleIte(ite);
          return 0;
      }
    }

    public static int callMethod_I(Object o, String methodName, long parm1,
        String parm2, int parm3, int parm4) throws Exception {
      java.lang.reflect.Method method;

      Class thisClass = o.getClass();
      Class[] argTypes = new Class[4];
      argTypes[0] = java.lang.Long.TYPE;
      argTypes[1] = String.class; 
      argTypes[2] = java.lang.Integer.TYPE; 
      argTypes[3] = java.lang.Integer.TYPE; 
      method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
      Object[] args = new Object[4];
      args[0] = new Long(parm1);
      args[1] = parm2; 
      args[2] = new Integer(parm3); 
      args[3] = new Integer(parm4); 
      try {
          Integer i = (Integer) method.invoke(o, args);
          return i.intValue();
      } catch (java.lang.reflect.InvocationTargetException ite) {
          handleIte(ite);
          return 0;
      }
    }


    
    public static int callMethod_I(Object o, String methodName, Object parm1) throws Exception {
      java.lang.reflect.Method method = null;

      Class thisClass = o.getClass();
      Class[] argTypes = new Class[1];

      if (parm1 == null) {
        throw new Exception("Unable to handle null parameter");
      }
      Class checkClass = parm1.getClass();
      String tryArgs = "";
      while (method == null && checkClass != null) {
        try {
          tryArgs += "(" + checkClass.getName() + ") ";
          argTypes[0] = checkClass;
          method = thisClass.getMethod(methodName, argTypes);
          method.setAccessible(true); //allow toolbox proxy methods to be invoked

        } catch (Exception e) {

        }
        if (method == null) {
          if (checkClass.getName().equals("java.lang.Object")) {
            checkClass = null;

          } else {
            checkClass = checkClass.getSuperclass();
          }

        }
      }

      if (checkClass == null) {
        checkClass = parm1.getClass();
        // Did not find a base class.. Now look for implements
        while (method == null && checkClass != null) {
          try {

            // Find the implementes for the class
            Class[] interfaces = checkClass.getInterfaces();
            for (int j = 0; method == null && j < interfaces.length; j++) {
              tryArgs += "(" + interfaces[j].getName() + ") ";
              argTypes[0] = interfaces[j];
	      try {
		  method = thisClass.getMethod(methodName, argTypes);
		  method.setAccessible(true); //allow toolbox proxy methods to be invoked
	      } catch (NoSuchMethodException e) {
	      }
            }
          } catch (Exception e) {

            e.printStackTrace();
          }
          if (method == null) {
            if (checkClass.getName().equals("java.lang.Object")) {
              checkClass = null;

            } else {
              checkClass = checkClass.getSuperclass();
            }

          }
        }

      }

      if (method == null) {
        throw new Exception("Unable to find method:  tried "+tryArgs);
      }
      Object[] args = new Object[1];
      args[0] = parm1;
      try {
        Integer i = (Integer) method.invoke(o, args);
        return i.intValue();
      } catch (java.lang.reflect.InvocationTargetException ite) {
        handleIte(ite);

      }
      return 0;

    }



   /**
    * call a method which returns an integer
    *
    * Examples
    *
    *  int value = callMethod_I(ds, "getMaximumPrecision");
    */
    public static int callMethod_I(Object o, String methodName, Class[] argTypes, Object[] args) throws Exception {
	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	try {
	    Integer i = (Integer) method.invoke(o, args);
	    return i.intValue();
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	    return 0;
	}

    }



    /**
     * call a method which returns an long
     *
     * Examples
     *
     *  long value = callMethod_L(ds, "length");
     */
     public static long callMethod_L(Object o, String methodName) throws Exception {
        java.lang.reflect.Method method;

        Class thisClass = o.getClass();
        Class[] argTypes = new Class[0];
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[0];
        try {
            Long l = (Long) method.invoke(o, args);
            return l.longValue();
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);
            return 0;
        }

     }

     /**
      * Call a method that returns a long 
      * @param o
      * @param methodName
      * @param parm1
      * @return
      * @throws Exception
      */
     public static long callMethod_L(Object o, String methodName, String parm1) throws Exception {
       java.lang.reflect.Method method = null;

       Class thisClass = o.getClass();
       Class[] argTypes = new Class[1];
       argTypes[0] = java.lang.String.class; 
       method = thisClass.getMethod(methodName, argTypes);

       if (method == null) {
         throw new Exception("Unable to find method:");
       }
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[1];
       args[0] = parm1;
       try {
         Long l = (Long) method.invoke(o, args);
         return l.longValue();
       } catch (java.lang.reflect.InvocationTargetException ite) {
         handleIte(ite);

       }
       return 0;

     }


     /**
      * Call a method that returns a long 
      * @param o
      * @param methodName
      * @param parm1
      * @param parm2
      * @return
      * @throws Exception
      */
     public static long callMethod_L(Object o, String methodName, String parm1, int parm2) throws Exception {
       java.lang.reflect.Method method = null;

       Class thisClass = o.getClass();
       Class[] argTypes = new Class[2];
       argTypes[0] = java.lang.String.class;
       argTypes[1] = Integer.TYPE; 
       method = thisClass.getMethod(methodName, argTypes);

       if (method == null) {
         throw new Exception("Unable to find method:");
       }
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[2];
       args[0] = parm1;
       args[1] = new Integer(parm2); 
       try {
         Long l = (Long) method.invoke(o, args);
         return l.longValue();
       } catch (java.lang.reflect.InvocationTargetException ite) {
         handleIte(ite);

       }
       return 0;

     }

     public static long callMethod_L(Object o, String methodName,
         String parm1, long parm2) throws Exception {
       java.lang.reflect.Method method = null;

       Class thisClass = o.getClass();
       Class[] argTypes = new Class[2];
       argTypes[0] = java.lang.String.class;
       argTypes[1] = Long.TYPE; 
       method = thisClass.getMethod(methodName, argTypes);

       if (method == null) {
         throw new Exception("Unable to find method:");
       }
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[2];
       args[0] = parm1;
       args[1] = new Long(parm2); 
       try {
         Long l = (Long) method.invoke(o, args);
         return l.longValue();
       } catch (java.lang.reflect.InvocationTargetException ite) {
         handleIte(ite);

       }
       return 0;

     
     }

     /**
      * Call a method that returns a long 
      * @param o
      * @param methodName
      * @param parm1
      * @param parm2
      * @return
      * @throws Exception
      */
     public static long callMethod_L(Object o, String methodName, String parm1, int[] parm2) throws Exception {
       java.lang.reflect.Method method = null;

       Class thisClass = o.getClass();
       Class[] argTypes = new Class[2];
       argTypes[0] = java.lang.String.class;
       argTypes[1] = parm2.getClass(); 
       method = thisClass.getMethod(methodName, argTypes);

       if (method == null) {
         throw new Exception("Unable to find method:");
       }
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[2];
       args[0] = parm1;
       args[1] = parm2; 
       try {
         Long l = (Long) method.invoke(o, args);
         return l.longValue();
       } catch (java.lang.reflect.InvocationTargetException ite) {
         handleIte(ite);

       }
       return 0;

     }


     /**
      * Call a method that returns a long 
      * @param o
      * @param methodName
      * @param parm1
      * @param parm2
      * @return
      * @throws Exception
      */
     public static long callMethod_L(Object o, String methodName, String parm1, String[] parm2) throws Exception {
       java.lang.reflect.Method method = null;

       Class thisClass = o.getClass();
       Class[] argTypes = new Class[2];
       argTypes[0] = java.lang.String.class;
       argTypes[1] = parm2.getClass(); 
       method = thisClass.getMethod(methodName, argTypes);

       if (method == null) {
         throw new Exception("Unable to find method:");
       }
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[2];
       args[0] = parm1;
       args[1] = parm2; 
       try {
         Long l = (Long) method.invoke(o, args);
         return l.longValue();
       } catch (java.lang.reflect.InvocationTargetException ite) {
         handleIte(ite);

       }
       return 0;

     }

     
     public static long callMethod_L(Object o, String methodName, Class[] argTypes, Object[] args) throws Exception {
       java.lang.reflect.Method method = null;

       Class thisClass = o.getClass();
       method = thisClass.getMethod(methodName, argTypes);

       if (method == null) {
         throw new Exception("Unable to find method:");
       }
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       try {
         Long l = (Long) method.invoke(o, args);
         return l.longValue();
       } catch (java.lang.reflect.InvocationTargetException ite) {
         handleIte(ite);

       }
       return 0;

     }

     
     
     public static long callMethod_L(Object o, String methodName, Object parm1, long parm2) throws Exception {
       java.lang.reflect.Method method = null;

       Class thisClass = o.getClass();
       Class[] argTypes = new Class[2];
       argTypes[1] = Long.TYPE;

       if (parm1 == null) {
         throw new Exception("Unable to handle null parameter");
       }
       Class[] check1Class = getParameterClasses(parm1); 
       String tryArgs = "";
       for (int i = 0; method == null && i < check1Class.length; i++) {
           try {
             tryArgs += "(" + check1Class[i].getName() + ",long ";
             argTypes[0] = check1Class[i];
             method = thisClass.getMethod(methodName, argTypes);
             method.setAccessible(true); //allow toolbox proxy methods to be invoked
           } catch (Exception e) {

           }
       }
       


       if (method == null) {
         throw new Exception("Unable to find method:  tried "+tryArgs);
       }
       Object[] args = new Object[2];
       args[0] = parm1; 
       args[1] = new Long(parm2);
       try {
         Long l = (Long) method.invoke(o, args);
         return l.longValue(); 
       } catch (java.lang.reflect.InvocationTargetException ite) {
         handleIte(ite);

       }
       return 0; 
       
     }


   /**
    * call a method which returns a boolean
    *
    * Examples
    *
    *  boolean value = callMethod_B(ds, "getReturnExtendedMetaData");
    */
    public static boolean callMethod_B(Object o, String methodName) throws Exception {
	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	Class[] argTypes = new Class[0];
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[0];
	try {
	    Boolean b = (Boolean) method.invoke(o, args);
	    return b.booleanValue();
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	    return false;
	}

    }


    /**
     * call a method which returns a boolean
     *
     * Examples
     *
     *  boolean value = callMethod_B(connection, "isValid", 60)
     *
     */
     public static boolean callMethod_B(Object o, String methodName, int i ) throws Exception {
        java.lang.reflect.Method method;

        Class thisClass = o.getClass();
        Class[] argTypes = new Class[1];
        argTypes[0] = Integer.TYPE;
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[1];
        args[0] = new Integer(i);
        try {
            Boolean b = (Boolean) method.invoke(o, args);
            return b.booleanValue();
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);
            return false;
        }
     }


   /**
    * call a method which returns a boolean
    *
    * Examples
    *
    *  boolean value = callMethod_B(ds, "isWrapperFor", Class.forName("java.lang.String");
    */
    public static boolean callMethod_B(Object o, String methodName, Class x ) throws Exception {
	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	Class[] argTypes = new Class[1];
	argTypes[0] = Class.forName("java.lang.Class");
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[1];
	args[0] = x;
	try {
	    Boolean b = (Boolean) method.invoke(o, args);
	    return b.booleanValue();
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	    return false;
	}
    }

    public static boolean callMethod_B(Object o, String methodName, Object parm1) throws Exception {
      java.lang.reflect.Method method = null;

      Class thisClass = o.getClass();
      Class[] argTypes = new Class[1];

      if (parm1 == null) {
        throw new Exception("Unable to handle null parameter");
      }
      Class checkClass = parm1.getClass();
      String tryArgs = "";
      while (method == null && checkClass != null) {
        try {
          tryArgs += "(" + checkClass.getName() + ") ";
          argTypes[0] = checkClass;
          method = thisClass.getMethod(methodName, argTypes);
          method.setAccessible(true); //allow toolbox proxy methods to be invoked

        } catch (Exception e) {

        }
        if (method == null) {
          if (checkClass.getName().equals("java.lang.Object")) {
            checkClass = null;

          } else {
            checkClass = checkClass.getSuperclass();
          }

        }
      }

      if (checkClass == null) {
        checkClass = parm1.getClass();
        // Did not find a base class.. Now look for implements
        while (method == null && checkClass != null) {
          try {

            // Find the implementes for the class
            Class[] interfaces = checkClass.getInterfaces();
            for (int j = 0; method == null && j < interfaces.length; j++) {
              tryArgs += "(" + interfaces[j].getName() + ") ";
              argTypes[0] = interfaces[j];
	      try {
		  method = thisClass.getMethod(methodName, argTypes);
		  method.setAccessible(true); //allow toolbox proxy methods to be invoked
	      } catch (NoSuchMethodException e) {
		     // keep going
	      }

            }
          } catch (Exception e) {

            e.printStackTrace();
          }
          if (method == null) {
            if (checkClass.getName().equals("java.lang.Object")) {
              checkClass = null;

            } else {
              checkClass = checkClass.getSuperclass();
            }

          }
        }

      }

      if (method == null) {
        throw new Exception("Unable to find method:  tried "+tryArgs);
      }
      Object[] args = new Object[1];
      args[0] = parm1;
      try {
        Boolean b = (Boolean) method.invoke(o, args);
        return b.booleanValue();
      } catch (java.lang.reflect.InvocationTargetException ite) {
        handleIte(ite);

      }
      return false;

    }



   /**
    * call a method which returns nothing.  The parameter types and values are passed.
    *
    * Examples
    *
    *  callMethod_V(ds, "setReturnExtendedMetaData", argTypes, args);
    */

    public static void callMethod_V(Object o, String methodName, Class[] argTypes, Object[] args  ) throws Exception {
	java.lang.reflect.Method method;
        Class thisClass = o.getClass();

	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	try {
	    method.invoke(o, args);
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	}
    }




   /**
    * call a method which returns nothing and is passed nothing
    *
    * Examples
    *
    *  callMethod_V(ds, "close");
    */

    public static void callMethod_V(Object o, String methodName) throws Exception {

	java.lang.reflect.Method method;
        Class thisClass = o.getClass();

	Class[] argTypes = new Class[0];
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[0];
	try {
	    method.invoke(o, args);
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);

	}
    }



   /**
    * call a method which returns nothing, but is passed an int
    *
    * Examples
    *
    *  callMethod_V(ds, "setMaximumPrecision", 34);
    */

    public static void callMethod_V(Object o, String methodName, int parm1) throws Exception {

	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	Class[] argTypes = new Class[1];
	argTypes[0] = java.lang.Integer.TYPE;
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[1];
	args[0] = new Integer(parm1);
	try {
	    method.invoke(o, args);
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	}

    }

    /**
     * call a method which returns nothing, but is passed an long
     *
     * Examples
     *
     *  callMethod_V(ds, "setMaximumPrecision", 34);
     */

     public static void callMethod_V(Object o, String methodName, long parm1) throws Exception {

   java.lang.reflect.Method method;

   Class thisClass = o.getClass();
   Class[] argTypes = new Class[1];
   argTypes[0] = java.lang.Long.TYPE;
   method = thisClass.getMethod(methodName, argTypes);
     method.setAccessible(true); //allow toolbox proxy methods to be invoked
   Object[] args = new Object[1];
   args[0] = new Long(parm1);
   try {
       method.invoke(o, args);
   } catch (java.lang.reflect.InvocationTargetException ite) {
       handleIte(ite);
   }

     }


    
    
    /**
     * call a method which returns nothing, but is passed an int and a string  String
     *
     * Examples
     *
     *  callMethod_V(ps, "setNString", 1, "character");
     */

     public static void callMethod_V(Object o, String methodName, int parm1, String parm2) throws Exception {

        java.lang.reflect.Method method;

        Class thisClass = o.getClass();
        Class[] argTypes = new Class[2];
        argTypes[0] = java.lang.Integer.TYPE;
        argTypes[1] = String.class;
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[2];
        args[0] = new Integer(parm1);
        args[1] = parm2;
        try {
            method.invoke(o, args);
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);

        }
     }

     
     
     /**
      * call a method which returns nothing, but is passed an int and a int  String
      *
      * Examples
      *
      *  callMethod_V(ps, "setInt, 1, 1);
      */

      public static void callMethod_V(Object o, String methodName, int parm1, int parm2) throws Exception {

         java.lang.reflect.Method method;

         Class thisClass = o.getClass();
         Class[] argTypes = new Class[2];
         argTypes[0] = java.lang.Integer.TYPE;
         argTypes[1] = java.lang.Integer.TYPE;
         method = thisClass.getMethod(methodName, argTypes);
         method.setAccessible(true); //allow toolbox proxy methods to be invoked
         Object[] args = new Object[2];
         args[0] = new Integer(parm1);
         args[1] = new Integer(parm2);
         try {
             method.invoke(o, args);
         } catch (java.lang.reflect.InvocationTargetException ite) {
             handleIte(ite);

         }
      }


     
     
     /**
      * call a method which returns nothing, but is passed an string and a string  String
      *
      * Examples
      *
      *  callMethod_V(ps, "setNString", "col1", "character");
      */

      public static void callMethod_V(Object o, String methodName, String parm1, String parm2) throws Exception {

         java.lang.reflect.Method method;

         Class thisClass = o.getClass();
         Class[] argTypes = new Class[2];
         argTypes[0] = String.class;
         argTypes[1] = String.class;
         method = thisClass.getMethod(methodName, argTypes);
         method.setAccessible(true); //allow toolbox proxy methods to be invoked
         Object[] args = new Object[2];
         args[0] = parm1;
         args[1] = parm2;
         try {
             method.invoke(o, args);
         } catch (java.lang.reflect.InvocationTargetException ite) {
             handleIte(ite);

         }
      }




   /**
    * call a method which returns nothing, but is passed an boolean
    *
    * Examples
    *
    *  callMethod_V(ds, "setReturnExtendedMetaData", true);
    */

    public static void callMethod_V(Object o, String methodName, boolean parm1) throws Exception {

	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	Class[] argTypes = new Class[1];
	argTypes[0] = java.lang.Boolean.TYPE;
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[1];
	args[0] = new Boolean(parm1);
	try {
	    method.invoke(o, args);
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);
	}

    }


   /**
    * call a method which returns nothing, but is passed a byte array
    *
    * Examples
    *
    *  callMethod_V(ds, "setTranslateHex", "character");
    */

    public static void callMethod_V(Object o, String methodName, byte[] parm1) throws Exception {

	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	Class[] argTypes = new Class[1];
	if (parm1 == null) {
	    byte[] dummy = new byte[0];
	    argTypes[0] = dummy.getClass();
	} else {
	    argTypes[0] = parm1.getClass();
	}
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[1];
	args[0] = parm1;
	try {
	    method.invoke(o, args);
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);

	}
    }


    /**
     * call a method which returns nothing, but is passed an integer and object
     * The method to be called is dynamically resolved
     *
     * Examples
     *
     *  callMethod_V(ps, "psSetNClob", 1, "character");
     */

  public static void callMethod_V(Object o, String methodName, int i, Object parm2) throws Exception {
    java.lang.reflect.Method method = null;

    Class thisClass = o.getClass();
    Class[] argTypes = new Class[2];
    argTypes[0] = Integer.TYPE;

    if (parm2 == null) {
      throw new Exception("Unable to handle null parameter");
    }
    Class checkClass = parm2.getClass();
    String tryArgs = "";
    while (method == null && checkClass != null) {
      try {
        tryArgs += "(int, " + checkClass.getName() + ") ";
        argTypes[1] = checkClass;
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked

      } catch (Exception e) {

      }
      if (method == null) {
        if (checkClass.getName().equals("java.lang.Object")) {
          checkClass = null;

        } else {
          checkClass = checkClass.getSuperclass();
        }

      }
    }

    if (checkClass == null) {
      checkClass = parm2.getClass();
      // Did not find a base class.. Now look for implements
      while (method == null && checkClass != null) {
        try {

          // Find the implementes for the class
          Class[] interfaces = checkClass.getInterfaces();
          for (int j = 0; method == null && j < interfaces.length; j++) {
            tryArgs += "(int, " + interfaces[j].getName() + ") ";
	    argTypes[1] = interfaces[j];
	    try {
		method = thisClass.getMethod(methodName, argTypes);
		method.setAccessible(true); //allow toolbox proxy methods to be invoked
	    } catch (NoSuchMethodException e) {
		     // keep going
	    }

          }
        } catch (Exception e) {

          e.printStackTrace();
        }
        if (method == null) {
          if (checkClass.getName().equals("java.lang.Object")) {
            checkClass = null;

          } else {
            checkClass = checkClass.getSuperclass();
          }

        }
      }

    }

    if (method == null) {
      throw new Exception("Unable to find method:  tried "+tryArgs);
    }
    Object[] args = new Object[2];
    args[0] = new Integer(i);
    args[1] = parm2;
    try {
      method.invoke(o, args);
    } catch (java.lang.reflect.InvocationTargetException ite) {
      handleIte(ite);

    }

  }

  
  
  


  /* Return the list of parameter classes to check for the specifed parameter */
  /* Use a cached list if possible */
  static Hashtable classToParameterClassesHashtable = new Hashtable();

  public static Class[] getParameterClasses(Object parm) {
    Hashtable existingInterfaceHashtable = new Hashtable(); 
    Class[] parameterClasses = null;
    Class currentClass = parm.getClass();
    Class topClass = currentClass; 
    parameterClasses = (Class[]) classToParameterClassesHashtable
        .get(topClass);
    if (parameterClasses == null) {
      ArrayList list = new ArrayList();
      String currentClassName = currentClass.getName(); 
      if ("java.lang.Long".equals(currentClassName) ) {
        list.add(java.lang.Long.TYPE); 
      }
      if ("java.lang.Integer".equals(currentClassName) ) {
        list.add(java.lang.Integer.TYPE); 
      }
      while (currentClass != null) {
        list.add(currentClass);

        // Find the implements for the class
        Class[] interfaces = currentClass.getInterfaces();
        for (int j = 0;  j < interfaces.length; j++) {
          Class thisInterface = interfaces[j]; 
          Object found = existingInterfaceHashtable.get(thisInterface); 
          if (found == null) { 
            list.add(thisInterface); 
            existingInterfaceHashtable.put(thisInterface,thisInterface); 
          }
        }
        
        if (currentClass.getName().equals("java.lang.Object")) {
          currentClass = null;
        } else {
          currentClass = currentClass.getSuperclass();
        } /* if java.lang.Object */ 
      } /* while currentClass != null */ 

      // Convert the arraylist to an array
      parameterClasses = new Class[list.size()]; 
      for (int i = 0; i < parameterClasses.length; i++) { 
        parameterClasses[i] = (Class) list.get(i); 
      }
      
      // Add to hash table
      classToParameterClassesHashtable.put(topClass, parameterClasses);
    } /* not found in hashtable */ 

    return parameterClasses;
    
  }
  /**
   * call a method which returns nothing, but is passed an integer, an object, and an object
   * The method to be called is dynamically resolved
   *
   * Examples
   *
   *  callMethod_V(ps, "psSetObject", 1, someObject, SQLType);
   */

public static void callMethod_V(Object o, String methodName, int parm1, Object parm2, Object parm3) throws Exception {
  java.lang.reflect.Method method = null;

  Class thisClass = o.getClass();
  Class[] argTypes = new Class[3];
  argTypes[0] = Integer.TYPE;

  if (parm2 == null) {
    throw new Exception("Unable to handle null parameter");
  }
  if (parm3 == null) {
    throw new Exception("Unable to handle null parameter");
  }
  Class[] check2Class = getParameterClasses(parm2); 
  Class[] check3Class = getParameterClasses(parm3); 
  String tryArgs = "";
  for (int i = 0; method == null && i < check2Class.length; i++) {
    for (int j = 0; method == null && j < check3Class.length; j++) {
      try {
        tryArgs += "(int, " + check2Class[i].getName() + ","+check3Class[j].getName()+ ") ";
        argTypes[1] = check2Class[i];
        argTypes[2] = check3Class[j]; 
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked

      } catch (Exception e) {

      }
    }
  }
  


  if (method == null) {
    throw new Exception("Unable to find method:  tried "+tryArgs);
  }
  Object[] args = new Object[3];
  args[0] = new Integer(parm1);
  args[1] = parm2;
  args[2] = parm3;
  try {
    method.invoke(o, args);
  } catch (java.lang.reflect.InvocationTargetException ite) {
    handleIte(ite);

  }

}






/**
 * call a method which returns nothing, but is passed an integer, an object, and an object
 * The method to be called is dynamically resolved
 *
 * Examples
 *
 *  callMethod_V(ps, "psSetObject", 1, someObject, SQLType);
 */

public static void callMethod_V(Object o, String methodName, String parm1, Object parm2, Object parm3) throws Exception {
java.lang.reflect.Method method = null;

Class thisClass = o.getClass();
Class[] argTypes = new Class[3];
argTypes[0] = String.class;

if (parm2 == null) {
  throw new Exception("Unable to handle null parameter");
}
if (parm3 == null) {
  throw new Exception("Unable to handle null parameter");
}
Class[] check2Class = getParameterClasses(parm2); 
Class[] check3Class = getParameterClasses(parm3); 
String tryArgs = "";
for (int i = 0; method == null && i < check2Class.length; i++) {
  for (int j = 0; method == null && j < check3Class.length; j++) {
    try {
      tryArgs += "(String, " + check2Class[i].getName() + ","+check3Class[j].getName()+ ") ";
      argTypes[1] = check2Class[i];
      argTypes[2] = check3Class[j]; 
      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked

    } catch (Exception e) {

    }
  }
}



if (method == null) {
  throw new Exception("Unable to find method:  tried "+tryArgs);
}
Object[] args = new Object[3];
args[0] = parm1;
args[1] = parm2;
args[2] = parm3;
try {
  method.invoke(o, args);
} catch (java.lang.reflect.InvocationTargetException ite) {
  handleIte(ite);

}

}


public static void callMethod_V(Object o, String methodName, String parm1, Object parm2, long parm3) throws Exception {
java.lang.reflect.Method method = null;

Class thisClass = o.getClass();
Class[] argTypes = new Class[3];
argTypes[0] = String.class;

if (parm2 == null) {
  throw new Exception("Unable to handle null parameter");
}
Class[] check2Class = getParameterClasses(parm2); 
argTypes[2] = Long.TYPE; 
String tryArgs = "";
for (int i = 0; method == null && i < check2Class.length; i++) {
    try {
      tryArgs += "(String, " + check2Class[i].getName() + ",long) ";
      argTypes[1] = check2Class[i];
      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked

    } catch (Exception e) {

      
    }
}



if (method == null) {
  throw new Exception("Unable to find method:  tried "+tryArgs);
}
Object[] args = new Object[3];
args[0] = parm1;
args[1] = parm2;
args[2] = new Long(parm3);
try {
  method.invoke(o, args);
} catch (java.lang.reflect.InvocationTargetException ite) {
  handleIte(ite);

}

}




/**
 * call a method which returns nothing, but is passed an integer, an object, and an object
 * The method to be called is dynamically resolved
 *
 * Examples
 *
 *  callMethod_V(ps, "psSetObject", 1, someObject, SQLType);
 */

public static void callMethod_V(Object o, String methodName, String parm1, Object parm2, Object parm3, int parm4) throws Exception {
java.lang.reflect.Method method = null;

Class thisClass = o.getClass();
Class[] argTypes = new Class[4];
argTypes[0] = String.class;
argTypes[3] = Integer.TYPE; 
if (parm2 == null) {
  throw new Exception("Unable to handle null parameter");
}
if (parm3 == null) {
  throw new Exception("Unable to handle null parameter");
}
Class[] check2Class = getParameterClasses(parm2); 
Class[] check3Class = getParameterClasses(parm3); 
String tryArgs = "";
for (int i = 0; method == null && i < check2Class.length; i++) {
  for (int j = 0; method == null && j < check3Class.length; j++) {
    try {
      tryArgs += "(int, " + check2Class[i].getName() + ","+check3Class[j].getName()+ ") ";
      argTypes[1] = check2Class[i];
      argTypes[2] = check3Class[j]; 
      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked

    } catch (Exception e) {

    }
  }
}



if (method == null) {
  throw new Exception("Unable to find method:  tried "+tryArgs);
}
Object[] args = new Object[4];
args[0] = parm1;
args[1] = parm2;
args[2] = parm3;
args[3] = new Integer(parm4);
try {
  method.invoke(o, args);
} catch (java.lang.reflect.InvocationTargetException ite) {
  handleIte(ite);

}

}




/**
 * call a method which returns nothing, but is passed an integer, an object, and an integer
 * The method to be called is dynamically resolved
 *
 * Examples
 *
 *  callMethod_V(ps, "psSetObject", 1, someObject, SQLType);
 */

public static void callMethod_V(Object o, String methodName, int parm1, Object parm2, int parm3) throws Exception {
java.lang.reflect.Method method = null;

Class thisClass = o.getClass();
Class[] argTypes = new Class[3];
argTypes[0] = Integer.TYPE;

if (parm2 == null) {
  throw new Exception("Unable to handle null parameter");
}
argTypes[2] = Integer.TYPE;

Class[] check2Class = getParameterClasses(parm2); 
String tryArgs = "";
for (int i = 0; method == null && i < check2Class.length; i++) {
    try {
      tryArgs += "(int, " + check2Class[i].getName() + ", int) ";
      argTypes[1] = check2Class[i];
      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked

    } catch (Exception e) {

    }
}



if (method == null) {
  throw new Exception("Unable to find method:  tried "+tryArgs);
}
Object[] args = new Object[3];
args[0] = new Integer(parm1);
args[1] = parm2;
args[2] = new Integer(parm3);
try {
  method.invoke(o, args);
} catch (java.lang.reflect.InvocationTargetException ite) {
  handleIte(ite);

}

}

public static void callMethod_V(Object o, String methodName, int parm1, Object parm2, int parm3, int parm4) throws Exception {
java.lang.reflect.Method method = null;

Class thisClass = o.getClass();
Class[] argTypes = new Class[4];
argTypes[0] = Integer.TYPE;
if (parm2 == null) {
  throw new Exception("Unable to handle null parameter");
}
argTypes[2] = Integer.TYPE;
argTypes[3] = Integer.TYPE; 

Class[] check2Class = getParameterClasses(parm2); 
String tryArgs = "";
for (int i = 0; method == null && i < check2Class.length; i++) {
    try {
      tryArgs += "(int, " + check2Class[i].getName() + ", int) ";
      argTypes[1] = check2Class[i];
      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked

    } catch (Exception e) {

    }
}



if (method == null) {
  throw new Exception("Unable to find method:  tried "+tryArgs);
}
Object[] args = new Object[3];
args[0] = new Integer(parm1);
args[1] = parm2;
args[2] = new Integer(parm3);
args[3] = new Integer(parm4);
try {
  method.invoke(o, args);
} catch (java.lang.reflect.InvocationTargetException ite) {
  handleIte(ite);

}

}


/**
 * call a method which returns nothing, but is passed an integer, an object, and a long
 * The method to be called is dynamically resolved
 *
 * Examples
 *
 *  callMethod_V(ps, "psSetObject", 1, someObject, SQLType);
 */

public static void callMethod_V(Object o, String methodName, int parm1, Object parm2, long parm3) throws Exception {
java.lang.reflect.Method method = null;

Class thisClass = o.getClass();
Class[] argTypes = new Class[3];
argTypes[0] = Integer.TYPE;

if (parm2 == null) {
  throw new Exception("Unable to handle null parameter");
}
argTypes[2] = Long.TYPE;

Class[] check2Class = getParameterClasses(parm2); 
String tryArgs = "";
for (int i = 0; method == null && i < check2Class.length; i++) {
    try {
      tryArgs += "(int, " + check2Class[i].getName() + ", long) ";
      argTypes[1] = check2Class[i];
      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked

    } catch (Exception e) {

    }
}



if (method == null) {
  throw new Exception("Unable to find method:  tried "+tryArgs);
}
Object[] args = new Object[3];
args[0] = new Integer(parm1);
args[1] = parm2;
args[2] = new Long(parm3);
try {
  method.invoke(o, args);
} catch (java.lang.reflect.InvocationTargetException ite) {
  handleIte(ite);

}

}



/**
 * call a method which returns nothing, but is passed an integer, an object, an object, and an integer
 * The method to be called is dynamically resolved
 *
 * Examples
 *
 *  callMethod_V(ps, "psSetObject", 1, someObject, SQLType,1);
 */

public static void callMethod_V(Object o, String methodName, int parm1, Object parm2, Object parm3, int parm4) throws Exception {
java.lang.reflect.Method method = null;

Class thisClass = o.getClass();
Class[] argTypes = new Class[4];
argTypes[0] = Integer.TYPE;
argTypes[3] = Integer.TYPE;

if (parm2 == null) {
  throw new Exception("Unable to handle null parameter");
}
if (parm3 == null) {
  throw new Exception("Unable to handle null parameter");
}
Class[] check2Class = getParameterClasses(parm2); 
Class[] check3Class = getParameterClasses(parm3); 
String tryArgs = "";
for (int i = 0; method == null && i < check2Class.length; i++) {
  for (int j = 0; method == null && j < check3Class.length; j++) {
    try {
      tryArgs += "(int, " + check2Class[i].getName() + ","+check3Class[j].getName()+ ") ";
      argTypes[1] = check2Class[i];
      argTypes[2] = check3Class[j]; 
      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked

    } catch (Exception e) {

    }
  }
}



if (method == null) {
  throw new Exception("Unable to find method:  tried "+tryArgs);
}
Object[] args = new Object[4];
args[0] = new Integer(parm1);
args[1] = parm2;
args[2] = parm3;
args[3] = new Integer(parm4);
try {
  method.invoke(o, args);
} catch (java.lang.reflect.InvocationTargetException ite) {
  handleIte(ite);

}

}



  
  public static void callMethod_V(Object o, String methodName, Object parm1, int i) throws Exception {
    java.lang.reflect.Method method = null;

    Class thisClass = o.getClass();
    Class[] argTypes = new Class[2];
    argTypes[1] = Integer.TYPE;

    if (parm1 == null) {
      throw new Exception("Unable to handle null parameter");
    }
    Class checkClass = parm1.getClass();
    String tryArgs = "";
    while (method == null && checkClass != null) {
      try {
        tryArgs += "(" + checkClass.getName() + ",int) ";
        argTypes[0] = checkClass;
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked

      } catch (Exception e) {

      }
      if (method == null) {
        if (checkClass.getName().equals("java.lang.Object")) {
          checkClass = null;

        } else {
          checkClass = checkClass.getSuperclass();
        }

      }
    }

    if (checkClass == null) {
      checkClass = parm1.getClass();
      // Did not find a base class.. Now look for implements
      while (method == null && checkClass != null) {
        try {

          // Find the implementes for the class
          Class[] interfaces = checkClass.getInterfaces();
          for (int j = 0; method == null && j < interfaces.length; j++) {
            tryArgs += "(" + interfaces[j].getName() + ",int) ";
            argTypes[0] = interfaces[j];
	    try {
              method = thisClass.getMethod(methodName, argTypes);
	      method.setAccessible(true); //allow toolbox proxy methods to be invoked
	    } catch (NoSuchMethodException e) {
	      // keep going
	    }

          }
        } catch (Exception e) {

          e.printStackTrace();
        }
        if (method == null) {
          if (checkClass.getName().equals("java.lang.Object")) {
            checkClass = null;

          } else {
            checkClass = checkClass.getSuperclass();
          }

        }
      }

    }

    if (method == null) {
      throw new Exception("Unable to find method:  tried "+tryArgs);
    }
    Object[] args = new Object[2];
    args[0] = parm1;
    args[1] = new Integer(i);
    try {
      method.invoke(o, args);
    } catch (java.lang.reflect.InvocationTargetException ite) {
      handleIte(ite);

    }

  }

  public static void callMethod_V(Object o, String methodName, Object parm1, boolean b) throws Exception {
    java.lang.reflect.Method method = null;

    Class thisClass = o.getClass();
    Class[] argTypes = new Class[2];
    argTypes[1] = Boolean.TYPE;

    if (parm1 == null) {
      throw new Exception("Unable to handle null parameter");
    }
    Class checkClass = parm1.getClass();
    String tryArgs = "";
    while (method == null && checkClass != null) {
      try {
        tryArgs += "(" + checkClass.getName() + ",int) ";
        argTypes[0] = checkClass;
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked

      } catch (Exception e) {

      }
      if (method == null) {
        if (checkClass.getName().equals("java.lang.Object")) {
          checkClass = null;

        } else {
          checkClass = checkClass.getSuperclass();
        }

      }
    }

    if (checkClass == null) {
      checkClass = parm1.getClass();
      // Did not find a base class.. Now look for implements
      while (method == null && checkClass != null) {
        try {

          // Find the implementes for the class
          Class[] interfaces = checkClass.getInterfaces();
          for (int j = 0; method == null && j < interfaces.length; j++) {
            tryArgs += "(" + interfaces[j].getName() + ",int) ";
            argTypes[0] = interfaces[j];
	    try {
		method = thisClass.getMethod(methodName, argTypes);
		method.setAccessible(true); //allow toolbox proxy methods to be invoked
	    } catch (NoSuchMethodException e) {
		     // keep going
	    }

          }
        } catch (Exception e) {

          e.printStackTrace();
        }
        if (method == null) {
          if (checkClass.getName().equals("java.lang.Object")) {
            checkClass = null;

          } else {
            checkClass = checkClass.getSuperclass();
          }

        }
      }

    }

    if (method == null) {
      throw new Exception("Unable to find method:  tried "+tryArgs);
    }
    Object[] args = new Object[2];
    args[0] = parm1;
    args[1] = new Boolean(b);
    try {
      method.invoke(o, args);
    } catch (java.lang.reflect.InvocationTargetException ite) {
      handleIte(ite);

    }

  }

  public static void callMethod_V(Object o, String methodName, boolean a, boolean b) throws Exception {
    java.lang.reflect.Method method = null;

    Class thisClass = o.getClass();
    Class[] argTypes = new Class[2];
    argTypes[0] = Boolean.TYPE;
    argTypes[1] = Boolean.TYPE;

    method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked

    Object[] args = new Object[2];
    args[0] = new Boolean(a); 
    args[1] = new Boolean(b);
    try {
      method.invoke(o, args);
    } catch (java.lang.reflect.InvocationTargetException ite) {
      handleIte(ite);

    }

  }


  
  

  public static void callMethod_V(Object o, String methodName, Object parm1) throws Exception {
    java.lang.reflect.Method method = null;

    Class thisClass = o.getClass();
    Class[] argTypes = new Class[1];

    if (parm1 == null) {
      throw new Exception("Unable to handle null parameter");
    }
    Class checkClass = parm1.getClass();
    String tryArgs = "";
    while (method == null && checkClass != null) {
      try {
        tryArgs += "(" + checkClass.getName() + ",int) ";
        argTypes[0] = checkClass;
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked

      } catch (Exception e) {

      }
      if (method == null) {
        if (checkClass.getName().equals("java.lang.Object")) {
          checkClass = null;

        } else {
          checkClass = checkClass.getSuperclass();
        }

      }
    }

    if (checkClass == null) {
      checkClass = parm1.getClass();
      // Did not find a base class.. Now look for implements
      while (method == null && checkClass != null) {
        try {

          // Find the implementes for the class
          Class[] interfaces = checkClass.getInterfaces();
          for (int j = 0; method == null && j < interfaces.length; j++) {
            tryArgs += "(" + interfaces[j].getName() + ",int) ";
            argTypes[0] = interfaces[j];
	    try {
		method = thisClass.getMethod(methodName, argTypes);
		method.setAccessible(true); //allow toolbox proxy methods to be invoked
	    } catch (NoSuchMethodException e) {
		     // keep going
	    }

          }
        } catch (Exception e) {

          e.printStackTrace();
        }
        if (method == null) {
          if (checkClass.getName().equals("java.lang.Object")) {
            checkClass = null;

          } else {
            checkClass = checkClass.getSuperclass();
          }

        }
      }

    }

    if (method == null) {
      throw new Exception("Unable to find method:  tried "+tryArgs);
    }
    Object[] args = new Object[1];
    args[0] = parm1;
    try {
      method.invoke(o, args);
    } catch (java.lang.reflect.InvocationTargetException ite) {
      handleIte(ite);

    }

  }


  public static void addInterfacesToHashtable(Hashtable interfacesHashtable, Class checkClass) {
      if (checkClass.isInterface()) {
	  interfacesHashtable.put(checkClass, checkClass);
      }
      Class[] interfaces = checkClass.getInterfaces();
      for (int j = 0;  j < interfaces.length; j++) {
	  addInterfacesToHashtable(interfacesHashtable, interfaces[j]);
      }
  }


  public static void callMethod_V(Object o, String methodName, String parm1, Object parm2) throws Exception {
    java.lang.reflect.Method method = null;

    Class thisClass = o.getClass();
    Class[] argTypes = new Class[2];
    argTypes[0] = String.class;

    if (parm2 == null) {
      throw new Exception("Unable to handle null parameter");
    }
    Class checkClass = parm2.getClass();
    String tryArgs = "";
    while (method == null && checkClass != null) {
	try {
	    tryArgs += "(String, Class:" + checkClass.getName() + " ) ";
	    argTypes[1] = checkClass;
	    method = thisClass.getMethod(methodName, argTypes);
	    method.setAccessible(true); //allow toolbox proxy methods to be invoked

	} catch (Exception e) {

	}
	if (method == null) {
	    if (checkClass.getName().equals("java.lang.Object")) {
		checkClass = null;

	    } else {
		checkClass = checkClass.getSuperclass();
	    }

	}
    }

    if (checkClass == null) {
      checkClass = parm2.getClass();
      // Did not find a base class.. Now look for implements
      while (method == null && checkClass != null) {
        try {

          // Find the implementes for the class
	  Hashtable interfacesHashtable = new Hashtable();
	  addInterfacesToHashtable(interfacesHashtable, checkClass);

	  Enumeration keys = interfacesHashtable.keys();


	  while(method == null && keys.hasMoreElements()) {
	      Class x = (Class) keys.nextElement();
	      tryArgs += "(String, Interface:" + x.getName() + ") ";
	      argTypes[1] = x;
	      try {
		  method = thisClass.getMethod(methodName, argTypes);
		  method.setAccessible(true); //allow toolbox proxy methods to be invoked
	      } catch (NoSuchMethodException e) {
		     // keep going
	      }

	  }


        } catch (Exception e) {

          e.printStackTrace();
        }
        if (method == null) {
          if (checkClass.getName().equals("java.lang.Object")) {
            checkClass = null;

          } else {
            checkClass = checkClass.getSuperclass();
          }

        }
      }

    }

    if (method == null) {
      throw new Exception("Unable to find method with name "+methodName+":  tried "+tryArgs);
    }
    Object[] args = new Object[2];
    args[0] = parm1;
    args[1] = parm2;
    try {
      method.invoke(o, args);
    } catch (java.lang.reflect.InvocationTargetException ite) {
      handleIte(ite);

    }

  }



  public static void callMethod_V(Object o, String methodName, String parm1, Object parm2, int parm3) throws Exception {
    java.lang.reflect.Method method = null;

    Class thisClass = o.getClass();
    Class[] argTypes = new Class[3];
    argTypes[0] = String.class;
    argTypes[2] = Integer.TYPE; 
    if (parm2 == null) {
      throw new Exception("Unable to handle null parameter");
    }
    Class checkClass = parm2.getClass();
    String tryArgs = "";
    while (method == null && checkClass != null) {
  try {
      tryArgs += "(String, Class:" + checkClass.getName() + " ) ";
      argTypes[1] = checkClass;
      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked

  } catch (Exception e) {

  }
  if (method == null) {
      if (checkClass.getName().equals("java.lang.Object")) {
    checkClass = null;

      } else {
    checkClass = checkClass.getSuperclass();
      }

  }
    }

    if (checkClass == null) {
      checkClass = parm2.getClass();
      // Did not find a base class.. Now look for implements
      while (method == null && checkClass != null) {
        try {

          // Find the implementes for the class
    Hashtable interfacesHashtable = new Hashtable();
    addInterfacesToHashtable(interfacesHashtable, checkClass);

    Enumeration keys = interfacesHashtable.keys();


    while(method == null && keys.hasMoreElements()) {
        Class x = (Class) keys.nextElement();
        tryArgs += "(String, Interface:" + x.getName() + ") ";
        argTypes[1] = x;
        try {
      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked
        } catch (NoSuchMethodException e) {
         // keep going
        }

    }


        } catch (Exception e) {

          e.printStackTrace();
        }
        if (method == null) {
          if (checkClass.getName().equals("java.lang.Object")) {
            checkClass = null;

          } else {
            checkClass = checkClass.getSuperclass();
          }

        }
      }

    }

    if (method == null) {
      throw new Exception("Unable to find method with name "+methodName+":  tried "+tryArgs);
    }
    Object[] args = new Object[3];
    args[0] = parm1;
    args[1] = parm2;
    args[2] = new Integer(parm3); 
    try {
      method.invoke(o, args);
    } catch (java.lang.reflect.InvocationTargetException ite) {
      handleIte(ite);

    }

  }


  public static void callMethod_V(Object o, String methodName, String parm1, Object parm2, int parm3, int parm4) throws Exception {
    java.lang.reflect.Method method = null;

    Class thisClass = o.getClass();
    Class[] argTypes = new Class[4];
    argTypes[0] = String.class;
    argTypes[2] = Integer.TYPE; 
    argTypes[3] = Integer.TYPE; 
    if (parm2 == null) {
      throw new Exception("Unable to handle null parameter");
    }
    Class checkClass = parm2.getClass();
    String tryArgs = "";
    while (method == null && checkClass != null) {
  try {
      tryArgs += "(String, Class:" + checkClass.getName() + " ) ";
      argTypes[1] = checkClass;
      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked

  } catch (Exception e) {

  }
  if (method == null) {
      if (checkClass.getName().equals("java.lang.Object")) {
    checkClass = null;

      } else {
    checkClass = checkClass.getSuperclass();
      }

  }
    }

    if (checkClass == null) {
      checkClass = parm2.getClass();
      // Did not find a base class.. Now look for implements
      while (method == null && checkClass != null) {
        try {

          // Find the implementes for the class
    Hashtable interfacesHashtable = new Hashtable();
    addInterfacesToHashtable(interfacesHashtable, checkClass);

    Enumeration keys = interfacesHashtable.keys();


    while(method == null && keys.hasMoreElements()) {
        Class x = (Class) keys.nextElement();
        tryArgs += "(String, Interface:" + x.getName() + ") ";
        argTypes[1] = x;
        try {
      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked
        } catch (NoSuchMethodException e) {
         // keep going
        }

    }


        } catch (Exception e) {

          e.printStackTrace();
        }
        if (method == null) {
          if (checkClass.getName().equals("java.lang.Object")) {
            checkClass = null;

          } else {
            checkClass = checkClass.getSuperclass();
          }

        }
      }

    }

    if (method == null) {
      throw new Exception("Unable to find method with name "+methodName+":  tried "+tryArgs);
    }
    Object[] args = new Object[4];
    args[0] = parm1;
    args[1] = parm2;
    args[2] = new Integer(parm3); 
    args[3] = new Integer(parm4); 
    try {
      method.invoke(o, args);
    } catch (java.lang.reflect.InvocationTargetException ite) {
      handleIte(ite);

    }

  }





  /**
   * call a method which returns nothing, but is passed a long and object
   * The method to be called is dynamically resolved
   *
   * Examples
   *
   *  callMethod_V(nclob, "setString", 1, "character");
   */

public static void callMethod_V(Object o, String methodName, long l, Object parm2) throws Exception {
  java.lang.reflect.Method method = null;

  Class thisClass = o.getClass();
  Class[] argTypes = new Class[2];
  argTypes[0] = Long.TYPE;

  if (parm2 == null) {
    throw new Exception("Unable to handle null parameter");
  }
  Class checkClass = parm2.getClass();
  String tryArgs = "";
  while (method == null && checkClass != null) {
    try {
      tryArgs += "(int, " + checkClass.getName() + ") ";
      argTypes[1] = checkClass;
      method = thisClass.getMethod(methodName, argTypes);
      method.setAccessible(true); //allow toolbox proxy methods to be invoked

    } catch (Exception e) {

    }
    if (method == null) {
      if (checkClass.getName().equals("java.lang.Object")) {
        checkClass = null;

      } else {
        checkClass = checkClass.getSuperclass();
      }

    }
  }

  if (method == null) {
    checkClass = parm2.getClass();
    // Did not find a base class.. Now look for implements
    while (method == null && checkClass != null) {
      try {

        // Find the implements for the class
        Class[] interfaces = checkClass.getInterfaces();
        for (int j = 0; method == null && j < interfaces.length; j++) {
          tryArgs += "(int, " + interfaces[j].getName() + ") ";
          argTypes[1] = interfaces[j];
	  try {
	      method = thisClass.getMethod(methodName, argTypes);
	      method.setAccessible(true); //allow toolbox proxy methods to be invoked
	  } catch (NoSuchMethodException e) {
		     // keep going
	  }

        }
      } catch (Exception e) {

      }
      if (method == null) {
        if (checkClass.getName().equals("java.lang.Object")) {
          checkClass = null;

        } else {
          checkClass = checkClass.getSuperclass();
        }

      }
    }

  }

  if (method == null) {
    throw new Exception("Unable to find method:  tried "+tryArgs);
  }
  Object[] args = new Object[2];
  args[0] = new Long(l);
  args[1] = parm2;
  try {
    method.invoke(o, args);
  } catch (java.lang.reflect.InvocationTargetException ite) {
    handleIte(ite);

  }

}



   /**
     * call a method which returns nothing, but is passed int, InputStream, long
     *
     * Examples
     *
     * JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
     */

    public static void callMethod_V(Object o, String methodName, int parameterIndex, InputStream inputStream, long length) throws Exception {

	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	Class[] argTypes = new Class[3];
	argTypes[0] = java.lang.Integer.TYPE;
	argTypes[1] = Class.forName("java.io.InputStream");
	argTypes[2] = java.lang.Long.TYPE;
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[3];
	args[0] = new Integer(parameterIndex);
	args[1] = inputStream;
	args[2] = new Long(length);
	try {
	    method.invoke(o, args);
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);

	}
    }


    /**
      * call a method which returns nothing, but is passed String, InputStream, long
      *
      * Examples
      *
      * JDReflectionUtil.callMethod_V(ps, "setBlob", "col1", is, (long) 4);
      */
    //@pdc adding _IS (inputStream) to method name so that abiguity errors get fixed on calls like callMethod_V(o, "aa", "bb", null, 1) //null type is not known.
    public static void callMethod_V_IS(Object o, String methodName, String parameterName, InputStream inputStream, long length) throws Exception {

        java.lang.reflect.Method method;

        Class thisClass = o.getClass();
        Class[] argTypes = new Class[3];
        argTypes[0] = String.class;
        argTypes[1] = Class.forName("java.io.InputStream");
        argTypes[2] = java.lang.Long.TYPE;
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[3];
        args[0] = parameterName;
        args[1] = inputStream;
        args[2] = new Long(length);
        try {
            method.invoke(o, args);
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);

        }
    }

  /**
    * call a method which returns nothing, but is passed int, Reader, long
    *
    * Examples
    *
    *  JDReflectionUtil.callMethod_V(ps, "setClob", 1, r, (long) 4);
    */

    public static void callMethod_V(Object o, String methodName, int parameterIndex, Reader reader, long length) throws Exception {

	java.lang.reflect.Method method;

	Class thisClass = o.getClass();
	Class[] argTypes = new Class[3];
	argTypes[0] = java.lang.Integer.TYPE;
	argTypes[1] = Class.forName("java.io.Reader");
	argTypes[2] = java.lang.Long.TYPE;
	method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
	Object[] args = new Object[3];
	args[0] = new Integer(parameterIndex);
	args[1] = reader;
	args[2] = new Long(length);
	try {
	    method.invoke(o, args);
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);

	}
    }

    /**
     * call a method which returns nothing, but is passed String, Reader, long
     *
     * Examples
     *
     *  JDReflectionUtil.callMethod_V(ps, "setClob", "C1", r, (long) 4);
     */

     public static void callMethod_V(Object o, String methodName, String parameterName, Reader reader, long length) throws Exception {

        java.lang.reflect.Method method;

        Class thisClass = o.getClass();
        Class[] argTypes = new Class[3];
        argTypes[0] = String.class;
        argTypes[1] = Class.forName("java.io.Reader");
        argTypes[2] = java.lang.Long.TYPE;
        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[3];
        args[0] = parameterName;
        args[1] = reader;
        args[2] = new Long(length);
        try {
            method.invoke(o, args);
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);

        }
     }


    /**
     * call a static method whihc returns an object.
     *
     * Examples
     *
     *  JDReflectionUtil.callStaticMethod_O("", "newInstance");
     * @throws ClassNotFoundException 
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
     
 	public static Object callStaticMethod_O(String classname, String methodName, String p1, int p2) 
 			throws Exception {

        Object returnObject = null;
	Class thisClass =  Class.forName(classname);
    Class[] argTypes = new Class[2];
    argTypes[0] = Class.forName("java.lang.String"); 
    argTypes[1] = Integer.class; 
    java.lang.reflect.Method method;

    method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
    Object[] args = new Object[2];
    args[0] = p1; 
    args[1] = new Integer[p2]; 
    try {
        returnObject =  method.invoke(null, args);
    } catch (java.lang.reflect.InvocationTargetException ite) {
        handleIte(ite);

    }
    return returnObject;
	
 	
 	}

	public static Object callStaticMethod_O(String classname, String methodName, 
			int p1, int p2, int p3,
			String p4, int p5) throws Exception {

		

        Object returnObject = null;
	Class thisClass =  Class.forName(classname);
    Class[] argTypes = new Class[5];
    argTypes[0] = Integer.TYPE; 
    argTypes[1] = Integer.TYPE; 
    argTypes[2] = Integer.TYPE; 
    argTypes[3] = Class.forName("java.lang.String"); 
    argTypes[4] = Integer.TYPE; 
    java.lang.reflect.Method method;

    method = thisClass.getMethod(methodName, argTypes);
    method.setAccessible(true); //allow toolbox proxy methods to be invoked
    Object[] args = new Object[5];
    args[0] = new Integer(p1); 
    args[1] = new Integer(p2); 
    args[2] = new Integer(p3); 
    args[3] = p4; 
    args[4] = new Integer(p5); 
    try {
        returnObject =  method.invoke(null, args);
    } catch (java.lang.reflect.InvocationTargetException ite) {
        handleIte(ite);

    }
    return returnObject;
	
 	
	}

     public static Object callStaticMethod_O(String classname, String methodName) throws Exception {

        Object returnObject = null;
	Class thisClass =  Class.forName(classname);

        java.lang.reflect.Method method;

        Class[] argTypes = new Class[0];

        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[0];
        try {
            returnObject =  method.invoke(null, args);
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);

        }
        return returnObject;
     }

     public static Object callStaticMethod_O(String classname, String methodName, String parm1) throws Exception {

       Object returnObject = null;
 Class thisClass =  Class.forName(classname);

       java.lang.reflect.Method method;

       Class[] argTypes = new Class[1];
       argTypes[0] = Class.forName("java.lang.String"); 

       method = thisClass.getMethod(methodName, argTypes);
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[1];
       args[0] = parm1; 
       try {
           returnObject =  method.invoke(null, args);
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);

       }
       return returnObject;
    }

     
     
     public static Object callStaticMethod_O(String classname, String methodName, Object parm1) throws Exception {

       Object returnObject = null;
 Class thisClass =  Class.forName(classname);

       java.lang.reflect.Method method = null ;

       Class[] argTypes = new Class[1];

       Class[] check1Class = getParameterClasses(parm1); 
       String tryArgs = "";
       for (int i = 0; method == null && i < check1Class.length; i++) {
           try {
             tryArgs += "(" + check1Class[i].getName() + ") ";
             argTypes[0] = check1Class[i];
             method = thisClass.getMethod(methodName, argTypes);
             method.setAccessible(true); //allow toolbox proxy methods to be invoked

           } catch (Exception e) {

           }
       }

       if (method == null) {
         throw new Exception("Unable to find method:  tried "+tryArgs);
       }

       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       
       Object[] args = new Object[1];
       args[0] = parm1; 
       try {
           returnObject =  method.invoke(null, args);
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);

       }
       return returnObject;
    }

     
     
     
     public static Object callStaticMethod_O(String classname, String methodName, int parm1) throws Exception {

       Object returnObject = null;
       Class thisClass =  Class.forName(classname);

       java.lang.reflect.Method method;

       Class[] argTypes = new Class[1];
       argTypes[0] = Integer.TYPE; 

       method = thisClass.getMethod(methodName, argTypes);
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[1];
       args[0] = new Integer(parm1); 
       try {
           returnObject =  method.invoke(null, args);
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);
       }
       return returnObject;
    }

     
          public static Object callStaticMethod_O(String classname, String methodName, int parm1, int parm2, int parm3) throws Exception {

       Object returnObject = null;
       Class thisClass =  Class.forName(classname);

       java.lang.reflect.Method method;

       Class[] argTypes = new Class[3];
       argTypes[0] = Integer.TYPE; 
       argTypes[1] = Integer.TYPE; 
       argTypes[2] = Integer.TYPE; 

       method = thisClass.getMethod(methodName, argTypes);
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[3];
       args[0] = new Integer(parm1); 
       args[1] = new Integer(parm2); 
       args[2] = new Integer(parm3); 
       try {
           returnObject =  method.invoke(null, args);
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);
       }
       return returnObject;
    }

     
     public static Object callStaticMethod_O(String classname, String methodName, int parm1, int parm2, int parm3, int parm4, int parm5, int parm6) throws Exception {

       Object returnObject = null;
       Class thisClass =  Class.forName(classname);

       java.lang.reflect.Method method;

       Class[] argTypes = new Class[6];
       argTypes[0] = Integer.TYPE; 
       argTypes[1] = Integer.TYPE; 
       argTypes[2] = Integer.TYPE; 
       argTypes[3] = Integer.TYPE; 
       argTypes[4] = Integer.TYPE; 
       argTypes[5] = Integer.TYPE; 

       method = thisClass.getMethod(methodName, argTypes);
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[6];
       args[0] = new Integer(parm1); 
       args[1] = new Integer(parm2); 
       args[2] = new Integer(parm3); 
       args[3] = new Integer(parm4); 
       args[4] = new Integer(parm5); 
       args[5] = new Integer(parm6); 
       try {
           returnObject =  method.invoke(null, args);
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);
       }
       return returnObject;
    }

     public static Object callStaticMethod_O(String classname, String methodName, int parm1, int parm2, int parm3, int parm4, int parm5, int parm6, int parm7) throws Exception {

       Object returnObject = null;
       Class thisClass =  Class.forName(classname);

       java.lang.reflect.Method method;

       Class[] argTypes = new Class[7];
       argTypes[0] = Integer.TYPE; 
       argTypes[1] = Integer.TYPE; 
       argTypes[2] = Integer.TYPE; 
       argTypes[3] = Integer.TYPE; 
       argTypes[4] = Integer.TYPE; 
       argTypes[5] = Integer.TYPE; 
       argTypes[6] = Integer.TYPE; 

       method = thisClass.getMethod(methodName, argTypes);
       method.setAccessible(true); //allow toolbox proxy methods to be invoked
       Object[] args = new Object[7];
       args[0] = new Integer(parm1); 
       args[1] = new Integer(parm2); 
       args[2] = new Integer(parm3); 
       args[3] = new Integer(parm4); 
       args[4] = new Integer(parm5); 
       args[5] = new Integer(parm6); 
        args[6] = new Integer(parm7); 
       try {
           returnObject =  method.invoke(null, args);
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);
       }
       return returnObject;
    }

     
     

    /**
     * call a static method which returns an int.
     *
     * Examples
     *
     *  JDReflectionUtil.callStaticMethod_I("", "newInstance");
     */

     public static int callStaticMethod_I(String classname, String methodName) throws Exception {

        Object returnObject = null;
	Class thisClass =  Class.forName(classname);

        java.lang.reflect.Method method;

        Class[] argTypes = new Class[0];

        method = thisClass.getMethod(methodName, argTypes);
        method.setAccessible(true); //allow toolbox proxy methods to be invoked
        Object[] args = new Object[0];
        try {
            returnObject =  method.invoke(null, args);
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);

        }
        if (returnObject != null) { 
          return ((Integer)returnObject).intValue();
        } else {
          return 0; 
        }
     }

     /**
      * call a static method which returns an boolean.
      *
      * Examples
      *
      *  JDReflectionUtil.callStaticMethod_B("", "newInstance");
      */

      public static boolean callStaticMethod_B(String classname, String methodName, String arg) throws Exception {

         Object returnObject = null;
         Class thisClass =  Class.forName(classname);

         java.lang.reflect.Method method;

         Class[] argTypes = new Class[1];
         argTypes[0] = Class.forName("java.lang.String"); 
         method = thisClass.getMethod(methodName, argTypes);
         method.setAccessible(true); //allow toolbox proxy methods to be invoked
         Object[] args = new Object[1];
         args[0] = arg; 
         try {
             returnObject =  method.invoke(null, args);
         } catch (java.lang.reflect.InvocationTargetException ite) {
             handleIte(ite);

         }
         if (returnObject != null) { 
           return ((Boolean)returnObject).booleanValue();
         } else {
           return false; 
         }
      }





   /**
    * create an object using reflection
    * Examples
    *
    *  JDReflectionUtil.createObject("com.ibm.db2.jcc.DB2XADataSource")
    *
    *  callMethod_V(ds, "setTranslateHex", "character");
    */

    public static Object createObject(String classname) throws Exception {

	Class objectClass1 = Class.forName(classname);
	Class[] noArgTypes = new Class[0];
	Object[] noArgs    = new Object[0];
	Object newObject =null;
	try {
	    Constructor constructor = objectClass1.getConstructor(noArgTypes);

	    newObject = constructor.newInstance(noArgs);
	} catch (java.lang.reflect.InvocationTargetException ite) {
	    handleIte(ite);

	}
	return newObject;
    }


    /**
     * create an object using reflection
     * Examples
     *
     *  JDReflectionUtil.createObject("com.ibm.db2.app.DB2RowId", testArray)
     *
     */

     public static Object createObject(String classname, byte[] arg) throws Exception {

        Class objectClass11 = Class.forName(classname);
        Class[] oneArgTypes = new Class[1];
        oneArgTypes[0] = Class.forName("[B");
        Object[] oneArgs    = new Object[1];
        oneArgs[0] = arg;
        Object newObject =null;
        try {
            Constructor constructor = objectClass11.getDeclaredConstructor(oneArgTypes); //@pdc find protected constructor also

            constructor.setAccessible(true);  //@PDA allo call of protected.
            newObject = constructor.newInstance(oneArgs);
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);

        }
        return newObject;
     }

     public static Object createObject(String classname,
         long arg) throws Exception {


       Class objectClass11 = Class.forName(classname);
       Class[] oneArgTypes = new Class[1];
       oneArgTypes[0] =  Long.TYPE;
       Object[] oneArgs    = new Object[1];
       oneArgs[0] = new Long(arg);
       Object newObject =null;
       try {
           Constructor constructor = objectClass11.getDeclaredConstructor(oneArgTypes); //@pdc find protected constructor also

           constructor.setAccessible(true);  //@PDA allo call of protected.
           newObject = constructor.newInstance(oneArgs);
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);

       }
       return newObject;

     
     
     }


     public static Object createObject(String classname,
         String arg) throws Exception {


       Class objectClass11 = Class.forName(classname);
       Class[] oneArgTypes = new Class[1];
       oneArgTypes[0] = arg.getClass();
       Object[] oneArgs    = new Object[1];
       oneArgs[0] = arg;
       Object newObject =null;
       try {
           Constructor constructor = objectClass11.getDeclaredConstructor(oneArgTypes); //@pdc find protected constructor also

           constructor.setAccessible(true);  //@PDA allo call of protected.
           newObject = constructor.newInstance(oneArgs);
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);

       }
       return newObject;

     
     
     }

 	public static Object createObject(String classname, int arg1, Object arg2) throws Exception  {
        Class objectClass11 = Class.forName(classname);
        Class[] twoArgTypes = new Class[2];
        twoArgTypes[0] = Integer.TYPE;
        twoArgTypes[1] = arg2.getClass();
        Object[] twoArgs    = new Object[2];
        twoArgs[0] = Integer.valueOf(arg1);
        twoArgs[1] = arg2; 
        
        Object newObject =null;
        try {
            Constructor constructor = objectClass11.getDeclaredConstructor(twoArgTypes);

            constructor.setAccessible(true);  
            newObject = constructor.newInstance(twoArgs);
        } catch (java.lang.reflect.InvocationTargetException ite) {
            handleIte(ite);

        }
        return newObject;
		
 	}



     
     public static Object createObject(String classname,
         long arg1, int arg2 ) throws Exception {


       Class objectClass11 = Class.forName(classname);
       Class[] twoArgsTypes = new Class[2];
       twoArgsTypes[0] =  Long.TYPE;
       twoArgsTypes[1] =  Integer.TYPE;
       Object[] twoArgs    = new Object[2];
       twoArgs[0] = new Long(arg1);
       twoArgs[1] = new Integer(arg2); 
       Object newObject =null;
       try {
           Constructor constructor = objectClass11.getDeclaredConstructor(twoArgsTypes); //@pdc find protected constructor also

           constructor.setAccessible(true);  //@PDA allo call of protected.
           newObject = constructor.newInstance(twoArgs);
       } catch (java.lang.reflect.InvocationTargetException ite) {
           handleIte(ite);

       }
       return newObject;

     
     
     }

     /**
      * create an object using reflection
      * Examples
      *
      *  JDReflectionUtil.createObject("javax.xml.transform.stax.StAXSource", "javax.xml.stream.XMLStreamReader", xmlStreamReader);
      *
      */

      public static Object createObject(String classname, String parameterClass, Object arg) throws Exception {

         Class objectClass1 = Class.forName(classname);
         Class[] oneArgTypes = new Class[1];
         oneArgTypes[0] = Class.forName(parameterClass);
         Object[] oneArgs    = new Object[1];
         oneArgs[0] = arg;
         Object newObject =null;
         try {
             Constructor constructor = objectClass1.getDeclaredConstructor(oneArgTypes);

             constructor.setAccessible(true);
             newObject = constructor.newInstance(oneArgs);
         } catch (java.lang.reflect.InvocationTargetException ite) {
             handleIte(ite);

         }
         return newObject;
      }

  	public static Object  createObject(String classname, String p1, String p2, char[] p3,
			char[] p4) throws Exception {
  		Class[] argTypes = new Class[4]; 
  		argTypes[0]= Class.forName("java.lang.String");	
  		argTypes[1]= Class.forName("java.lang.String");	
  		argTypes[2]= Class.forName("[C");	
  		argTypes[3]= Class.forName("[C");	
  		Object[] args = new Object[4]; 
  		args[0]=p1;
  		args[1]=p2;
  		args[2]=p3;
  		args[3]=p4;
  		
  		return createObject(classname, argTypes, args); 
	}



     /**
      * create an object using reflection
      *
      */

      public static Object createObject(String classname, Class[] argTypes, Object[] args)  throws Exception {

        Class objectClass1 = Class.forName(classname);
         Object newObject =null;
         try {
             Constructor constructor = objectClass1.getDeclaredConstructor(argTypes);

             constructor.setAccessible(true);
             newObject = constructor.newInstance(args);
         } catch (java.lang.reflect.InvocationTargetException ite) {
             handleIte(ite);

         } catch (NoSuchMethodException nsme) {
           System.out.println("Caught "+nsme.toString());
           System.out.println("Declared constructors are the following");
           Constructor[] constructors = objectClass1.getDeclaredConstructors();
           for (int i = 0 ; i < constructors.length;i++) {
             System.out.println(constructors[i].toString());
           }
           throw nsme;
         }
         return newObject;
      }



    /**
     * get an integer  field
     *
     * Examples
     *
     *  int value = getField_I(ds, "getMaximumPrecision");
     */
     public static int getField_I(Object o, String fieldName) throws Exception {
        java.lang.reflect.Field field;
        Class thisClass = o.getClass();
        field = thisClass.getField(fieldName);
        return         field.getInt(o);
     }


     public static Object getStaticField_O(String className, String fieldName) throws Exception {
      Object returnObject;
      Class thisClass = Class.forName(className);
      java.lang.reflect.Field field=thisClass.getField(fieldName);
      returnObject = field.get(null);
      return returnObject;
     }
     
     public static int getStaticField_I(String className, String fieldName) throws Exception {
       Object returnObject;
       Class thisClass = Class.forName(className);
       java.lang.reflect.Field field=thisClass.getField(fieldName);
       returnObject = field.get(null);
       return ((Integer)returnObject).intValue();
      }

    /** 
     * return the same results as ts1 instanceof classname 
     * 
     * @param ts1
     * @param classname
     * @return
     * @throws Exception 
     */
    public static boolean instanceOf(Object ts1,
        String classname) throws Exception {
      Class c = Class.forName(classname);
      return c.isInstance(ts1); 
    }











}



