///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RJavaProgramGenericAttributeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RJava;

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import test.Testcase;

/**
 * Testcase RJavaProgramGenericAttributeTestcase. This tests the following
 * methods of the RJavaProgram class, inherited from BufferedResource:
 *
 * <ul>
 * <li>cancelAttributeChanges()
 * <li>commitAttributeChanges()
 * <li>createResoure()
 * <li>delete()
 * <li>getAttributeMetaData()
 * <li>getAttributeMetaData()
 * <li>getAttributeUnchangedValue()
 * <li>getAttributeValue()
 * <li>hasUncommittedAttributeChanges()
 * <li>refreshAttributeValues()
 * <li>setAttributeValue()
 * </ul>
 **/
@SuppressWarnings("deprecation")
public class RJavaProgramGenericAttributeTestcase extends Testcase {

  /**
   * Constructor.
   **/
  public RJavaProgramGenericAttributeTestcase(AS400 systemObject, Hashtable<String, Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream,

      String password, AS400 pwrSys) {
    super(systemObject, "RJavaProgramGenericAttributeTestcase", namesAndVars, runMode, fileOutputStream, password);
    pwrSys_ = pwrSys;

    if (pwrSys == null) {
      throw new IllegalStateException("ERROR: Please specify a power system.");
    }
  }

  /**
   * Performs setup needed before running variations.
   *
   * @exception Exception If an exception occurs.
   **/
  @Override
  protected void setup() throws Exception {
    super.setup();
  }

  /**
   * Performs cleanup needed after running variations.
   *
   * @exception Exception If an exception occurs.
   **/
  @Override
  protected void cleanup() throws Exception {
    super.setup();
  }


  /**
   * cancelAttributeChanges() - Should do nothing when there is no system or path
   * set.
   **/
  public void Var001() {
    notApplicable("Java program objects not supported after V7R1");
    return;
  }



}
