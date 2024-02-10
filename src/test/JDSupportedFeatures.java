///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSupportedFeatures.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

public class JDSupportedFeatures {
 
  public boolean lobSupport = false; 
  public boolean bigintSupport = false; 
  public boolean decfloatSupport = false; 
  public boolean booleanSupport = false; 
  public boolean arraySupport = false; 
  
  
  public JDSupportedFeatures(JDTestcase testcase) {
    lobSupport = testcase.areLobsSupported();
    bigintSupport = testcase.areBigintsSupported();
    decfloatSupport = testcase.areDecfloatsSupported();
    booleanSupport = testcase.areBooleansSupported();
    arraySupport = testcase.areArraysSupported();
  }  

  
  
  public JDSupportedFeatures(JDTestDriver driver) {
    lobSupport = driver.areLobsSupported();
    bigintSupport = driver.areBigintsSupported();
    decfloatSupport = driver.areDecfloatsSupported();
    booleanSupport = driver.areBooleansSupported();
    arraySupport = driver.areArraysSupported();
  }



  public JDSupportedFeatures() {
    // Use default setting of all false. 
    
  }


  static JDSupportedFeatures defaultValue = new JDSupportedFeatures(); 
  
  public static JDSupportedFeatures getDefault() {
      return defaultValue; 
  }  

}
