///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMConnect.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.FileOutputStream;

import java.util.Vector;



import com.ibm.as400.access.AS400;

import test.Testcase;


/**
 * Testcase DDMConnect.
 **/
public class DDMConnect extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMConnect";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }

  /**
   * Constructor. This is called from the DDMTest constructor.
   **/
  public DDMConnect(AS400 systemObject, Vector variationsToRun, int runMode,
      FileOutputStream fileOutputStream,  String testLib) {

    /* replace third parameter with the number of variations */
    super(systemObject, "DDMConnect", 3, variationsToRun, runMode,
        fileOutputStream);
  }

  /**
   * Verify simple connection
   **/
  public void Var001() {
    StringBuffer failMsg = new StringBuffer();
    try {
      AS400 as400 = new AS400(systemObject_);
      as400.connectService(AS400.RECORDACCESS);
      as400.disconnectService(AS400.RECORDACCESS);

    } catch (Exception e) {
      failMsg.append("Unexpected exception");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

    if (failMsg.length() == 0) {
      succeeded();
    } else {
      failed(failMsg.toString());
    }
  }


  /**
   * Force ENCUSRPWD and connect
   **/
  public void Var002() {
    StringBuffer failMsg = new StringBuffer();
    try {
      com.ibm.as400.access.ClassDecoupler.forceENCUSRPWD = true; 
      AS400 as400 = new AS400(systemObject_);
      as400.connectService(AS400.RECORDACCESS);
      as400.disconnectService(AS400.RECORDACCESS);

    } catch (Exception e) {
      failMsg.append("Unexpected exception");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    com.ibm.as400.access.ClassDecoupler.forceENCUSRPWD = false; 

    if (failMsg.length() == 0) {
      succeeded();
    } else {
      failed(failMsg.toString());
    }
  }

  /**
   * Force ENCUSRPWD and AES and  connect
   **/
  public void Var003() {
    StringBuffer failMsg = new StringBuffer();
    try {
      com.ibm.as400.access.ClassDecoupler.forceENCUSRPWD = true; 
      com.ibm.as400.access.ClassDecoupler.forceAES = true; 
      AS400 as400 = new AS400(systemObject_);
      as400.connectService(AS400.RECORDACCESS);
      as400.disconnectService(AS400.RECORDACCESS);

    } catch (Exception e) {
      failMsg.append("Unexpected exception");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    com.ibm.as400.access.ClassDecoupler.forceENCUSRPWD = false; 
    com.ibm.as400.access.ClassDecoupler.forceAES = false; 

    if (failMsg.length() == 0) {
      succeeded();
    } else {
      failed(failMsg.toString());
    }
  }




}
