///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JVMInfo.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

public class JVMInfo {

  /**
   *
   */
  static final long serialVersionUID = 1L;

  public static final int JDK_16 =   160;
  public static final int JDK_17 =   170;
  public static final int JDK_18 =   180;
  public static final int JDK_19  =  190;
  public static final int JDK_V11 = 1100;
  public static final int JDK_V17 = 1700;


  static int jdk_ = 0;
  static String javaVersionString_ = null;
  static {

    javaVersionString_ = System.getProperty("java.version");
    if (javaVersionString_ != null) {
	if (javaVersionString_.charAt(0) == '1' && javaVersionString_.charAt(2) == '6') {
	    jdk_ = JDK_16;
	} else if (javaVersionString_.charAt(0) == '1' && javaVersionString_.charAt(2) == '7') {
	    jdk_ = JDK_17;
	} else if (javaVersionString_.charAt(0) == '1' && javaVersionString_.charAt(2) == '8') {
	    jdk_ = JDK_18;
	} else if (javaVersionString_.charAt(0) == '9') {
	    jdk_ = JDK_19;
	} else if ((javaVersionString_.charAt(0) == '1') &&
	  (javaVersionString_.charAt(1) == '1')){
	    jdk_ = JDK_V11;
	} else if ((javaVersionString_.charAt(0) == '1') &&
	  (javaVersionString_.charAt(1) == '7')){
	    jdk_ = JDK_V17;
	} else {
	    System.out
	      .println("WARNING:  test.JVMInfo unable to determine jdk from java.version="
		       + javaVersionString_);
	}
    } else {
	System.out.println("WARNING:  java.version not set");
    }

  } 






  /**
   * Returns the JDK level of the environment 
   * 
   * @return the JDK level
   **/
  public static int getJDK() {
    return jdk_;
  }
  
  public static String getJavaVersionString() {
    return javaVersionString_; 
  }


}
