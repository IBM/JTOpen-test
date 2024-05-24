///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400NewInstance.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.AS400;

import java.io.*;
import java.sql.Connection;

import com.ibm.as400.access.*;
import com.ibm.as400.util.AboutToolbox;

import test.DisableSSL;
import test.JDReflectionUtil;
import test.PasswordVault;
import test.Testcase;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Testcase AS400NewInstance
 * 
 * Tests the following static methods of the AS400 class.
 * 
 * public static AS400 newInstance(boolean useSSL, String systemName)
 * public static AS400 newInstance(boolean useSSL, String systemName, String userId, char[] password, char[] additionalAuthenticationFactor)
 * public static AS400 newInstance(boolean useSSL, String systemName, String userId, char[] password, String proxyServer)
 * public static AS400 newInstance(boolean useSSL, String systemName, ProfileTokenCredential profileToken)
 * public static AS400 newInstance(boolean useSSL, AS400 system)
 **/
public class AS400NewInstance extends Testcase {

  StringBuffer sb = new StringBuffer();
  private Connection connection_; 
  /**
   * Constructor. This is called from the AS400Test constructor.
   **/
  public AS400NewInstance(AS400 systemObject, Hashtable<String, Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, String testLib, String password, AS400 pwrSys) {
    super(systemObject, "AS400NewInstance", namesAndVars, runMode, fileOutputStream, password);
    pwrSys_ = pwrSys;

  }

  protected void setup() throws Exception {
    super.setup();
    connection_ =  new AS400JDBCDriver().connect(systemObject_); 
  }
  protected void cleanup() throws Exception {
    connection_.close(); 
    super.cleanup();
  }
  /**
   * 
   **/
  public void Var001() {
    boolean succeeded = true;
    
    try {
      sb.setLength(0);
      sb.append(AboutToolbox.getVersionDescription()+"\n");
      sb.append("Calling newInstance to "+systemName_+"\n"); 
      AS400 testAs400 = (AS400) JDReflectionUtil.callStaticMethod_O("com.ibm.as400.access.AS400", "newInstance", true, systemName_); 
      testAs400.setUserId(userId_);
      char[] password = PasswordVault.decryptPassword(encryptedPassword_); 
      testAs400.setPassword(password);
      PasswordVault.clearPassword(password);
      testAs400.connectService(AS400.SIGNON);
      Job[] jobs  = testAs400.getJobs(AS400.SIGNON); 
      String serverJobName = jobs[1].getNumber()+"/"+jobs[1].getUser()+"/"+jobs[1].getName();
      sb.append("connected to "+serverJobName); 
      assertCondition(succeeded, sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception: "+sb.toString());
    }
  }

}
