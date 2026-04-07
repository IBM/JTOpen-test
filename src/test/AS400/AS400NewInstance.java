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
import java.sql.ResultSet;
import java.sql.Statement;

import com.ibm.as400.access.*;
import com.ibm.as400.util.AboutToolbox;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.PasswordVault;
import test.Testcase;
import test.Sec.AuthExit;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Testcase AS400NewInstance
 * 
 * Tests the following static methods of the AS400 class.
 * 
 * public static AS400 newInstance(boolean useSSL, String systemName) public
 * static AS400 newInstance(boolean useSSL, String systemName, String userId,
 * char[] password, char[] additionalAuthenticationFactor) public static AS400
 * newInstance(boolean useSSL, String systemName, String userId, char[]
 * password, String proxyServer) public static AS400 newInstance(boolean useSSL,
 * String systemName, ProfileTokenCredential profileToken) public static AS400
 * newInstance(boolean useSSL, AS400 system)
 **/
public class AS400NewInstance extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "AS400NewInstance";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.AS400Test.main(newArgs);
  }

  StringBuffer sb = new StringBuffer();
  private Connection connection_;
  private boolean skipExitCleanup_ = false;
  private boolean exitProgramChecked_  = false;
  private boolean exitProgramEnabled_ = false;
  private Connection pwrConnection_;
  String testUserid_; 
  String testPassword_; 

  /**
   * Constructor. This is called from the AS400Test constructor.
   **/
  public AS400NewInstance(AS400 systemObject, Hashtable<String, Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, String testLib, String password, AS400 pwrSys) {
    super(systemObject, "AS400NewInstance", namesAndVars, runMode, fileOutputStream, password);
    pwrSys_ = pwrSys;

  }

  protected void setup() throws Exception {
    connection_ = new AS400JDBCDriver().connect(systemObject_);
    AS400JDBCDriver driver = new AS400JDBCDriver(); 
    pwrConnection_ = driver.connect(pwrSys_); 
    
    testUserid_ = "JDAS400NI";
    testPassword_ = "A1B2c3D4E5F6G7H8";
    
    Statement stmt = pwrConnection_.createStatement(); 
    
    String sql = "CALL QSYS2.QCMDEXC('CRTUSRPRF "+testUserid_+" PASSWORD("+testPassword_+")')";
    try {
      stmt.execute(sql); 
    } catch (Exception e) { 
      System.out.println("Warrning "+e.toString()+" on "+sql); 
    }
    sql = "CALL QSYS2.QCMDEXC('CHGUSRPRF "+testUserid_+" PASSWORD("+testPassword_+")')";
    stmt.execute(sql); 
    
    stmt.close(); 
  }

  protected void cleanup() throws Exception {
    connection_.close();
    if (!skipExitCleanup_ ) {
      AuthExit.cleanupExitProgram(pwrConnection_);
      AuthExit.cleanupExitProgramFiles(pwrConnection_);
      Statement stmt = pwrConnection_.createStatement(); 
      String sql= "CALL QSYS2.QCMDEXC('DLTUSRPRF "+testUserid_+" OWNOBJOPT(*CHGOWN QPGMR) ')";
      stmt.executeUpdate(sql);
      stmt.close(); 

    }
    pwrConnection_.close();

    super.cleanup();
  }

  /**
   * 
   **/
  public void Var001() {
    boolean succeeded = true;

    try {
      sb.setLength(0);
      sb.append("Java home is " + System.getProperty("java.home"));
      sb.append(AboutToolbox.getVersionDescription() + "\n");
      sb.append("Calling newInstance to " + systemName_ + "\n");
      AS400 testAs400 = (AS400) JDReflectionUtil.callStaticMethod_O("com.ibm.as400.access.AS400", "newInstance", true,
          systemName_);
      testAs400.setGuiAvailable(false);
      testAs400.setUserId(userId_);
      char[] password = PasswordVault.decryptPassword(encryptedPassword_);
      testAs400.setPassword(password);
      testAs400.connectService(AS400.SIGNON);
      testAs400.connectService(AS400.RECORDACCESS);
      PasswordVault.clearPassword(password);
      Job[] jobs = testAs400.getJobs(AS400.SIGNON);
      if (jobs.length == 0) { 
        sb.append("Error: no jobs for SIGNON"); 
        succeeded = false; 
      }
      for (int i = 0; i < jobs.length; i++) {
        String serverJobName = jobs[i].getNumber() + "/" + jobs[i].getUser() + "/" + jobs[i].getName();
        sb.append("connected to " + serverJobName);
      }
      Job[] jobs2 = testAs400.getJobs(AS400.RECORDACCESS);
      if (jobs2.length == 0) { 
        sb.append("Error: no jobs for RECORDACCESS"); 
        succeeded = false; 
      }
      for (int i = 0; i < jobs2.length; i++) {
        String serverJobName = jobs2[i].getNumber() + "/" + jobs2[i].getUser() + "/" + jobs2[i].getName();
        sb.append("connected to " + serverJobName);
      }

      assertCondition(succeeded, sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception: " + sb.toString());
    }
  }

  public void Var002() {
    boolean succeeded = true;
    if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
      try {
        sb.setLength(0);
        sb.append(AboutToolbox.getVersionDescription() + "\n");
        sb.append("Calling newInstance to " + systemName_ + "\n");
        SecureAS400 testAs400 = (SecureAS400) JDReflectionUtil.callStaticMethod_O("com.ibm.as400.access.SecureAS400",
            "newInstance", true, systemName_);

        testAs400.setGuiAvailable(false);
        char[] password = PasswordVault.decryptPassword(encryptedPassword_);
        testAs400.setUserId(userId_);
        testAs400.setPassword(password);
        JDReflectionUtil.callMethod_V(testAs400, "authenticate");
        boolean isAlive = testAs400.isConnectionAlive(8); /* 8 is AS400.HOSTCNN */
        PasswordVault.clearPassword(password);
        Job[] jobs = testAs400.getJobs(AS400.SIGNON);
        for (int i = 0; i < jobs.length; i++) {
          String serverJobName = jobs[i].getNumber() + "/" + jobs[i].getUser() + "/" + jobs[i].getName();
          sb.append("connected to " + serverJobName);
        }
        Job[] jobsHostcnn = testAs400.getJobs(8);
        for (int i = 0; i < jobsHostcnn.length; i++) {
          String serverJobName = jobsHostcnn[i].getNumber() + "/" + jobsHostcnn[i].getUser() + "/"
              + jobsHostcnn[i].getName();
          sb.append("connected to " + serverJobName);
        }
        assertCondition(isAlive && jobsHostcnn.length > 0 && succeeded, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception: " + sb.toString());
      }
    } else {
      notApplicable("later than V7R5 test");
    }
  }

  /* Save as Var001 but with MFA */ 
  
  public void Var003() {
    boolean succeeded = true;
    if (checkAdditionalAuthenticationFactor(systemName_)) {

    try {

      sb.setLength(0);
      sb.append("Java home is " + System.getProperty("java.home"));
      sb.append(AboutToolbox.getVersionDescription() + "\n");
      sb.append("Calling newInstance to " + systemName_ + "\n");
      AS400 testAs400 = (AS400) JDReflectionUtil.callStaticMethod_O("com.ibm.as400.access.AS400", "newInstance", true,
          systemName_);
      testAs400.setGuiAvailable(false);
      initMfaUser();
      testAs400.setUserId(mfaUserid_);
      char[] mfaPassword = PasswordVault.decryptPassword(mfaEncryptedPassword_);;
      testAs400.setPassword(mfaPassword);
      testAs400.setAdditionalAuthenticationFactor(mfaFactor_);
      sb.append("Connecting to SIGNON with "+mfaUserid_+"/... and factor "+new String(mfaFactor_)+"\n"); 
      testAs400.connectService(AS400.SIGNON);
      testAs400.connectService(AS400.RECORDACCESS);
      sb.append("Connecting to RECORD with "+mfaUserid_+"/... and factor "+ new String(mfaFactor_)+"\n"); 

      Job[] jobs = testAs400.getJobs(AS400.SIGNON);
      if (jobs.length == 0) { 
        sb.append("Error: no jobs for SIGNON"); 
        succeeded = false; 
      }
      for (int i = 0; i < jobs.length; i++) {
        String serverJobName = jobs[i].getNumber() + "/" + jobs[i].getUser() + "/" + jobs[i].getName();
        sb.append("connected to " + serverJobName);
      }
      Job[] jobs2 = testAs400.getJobs(AS400.RECORDACCESS);
      if (jobs2.length == 0) { 
        sb.append("Error: no jobs for RECORDACCESS"); 
        succeeded = false; 
      }
      for (int i = 0; i < jobs2.length; i++) {
        String serverJobName = jobs2[i].getNumber() + "/" + jobs2[i].getUser() + "/" + jobs2[i].getName();
        sb.append("connected to " + serverJobName);
      }
      Arrays.fill(mfaPassword, ' '); 
      assertCondition(succeeded, sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception: " + sb.toString());
    }
    }
  }
  
  
  public boolean checkExitProgram() throws Exception {
    if (!exitProgramChecked_) {
      String systemName = systemObject_.getSystemName();
      if (checkAdditionalAuthenticationFactor(systemName)) {

        AuthExit.assureExitProgramExists(pwrConnection_, testUserid_, false);
        exitProgramChecked_ = true; 
        exitProgramEnabled_ = true;
        skipExitCleanup_ = false; 
      } else {
        exitProgramChecked_ = true;
        exitProgramEnabled_ = false;
        return false; /* notApplicable already issued */
      }
    } /* exitProgramChecked_ */
    if (!exitProgramEnabled_) {
      notApplicable("Exit program not possible on this system");
    }
    return exitProgramEnabled_;
  }

  /*
   * Test that the exit program information is correct Modified from
   * JDDriverConnect Var071
   */
  public void Var004() {
    try {

      StringBuffer sb = new StringBuffer();
      boolean successful = true;
      if (checkExitProgram()) {
        Statement stmt = pwrConnection_.createStatement();
        sb.setLength(0);
        sb.append("Java home is " + System.getProperty("java.home"));
        sb.append(AboutToolbox.getVersionDescription() + "\n");
        sb.append("Calling newInstance to " + systemName_ + "\n");
        /* Note: false is specified below so that the host connection server is not used */
        /* When the host connection server is used, the exit program will only be called for */
        /* the host connect server and the DRDA server */ 
        AS400 testAs400 = (AS400) JDReflectionUtil.callStaticMethod_O("com.ibm.as400.access.AS400", "newInstance", false,
            systemName_);
        testAs400.setGuiAvailable(false);
        testAs400.setUserId(testUserid_);
        testAs400.setPassword(testPassword_.toCharArray());
        /* Connect all the services */
        Vector<String> joblist = new Vector<String>();
        testAs400.connectService(AS400.FILE);
        Job[] fileJobs = testAs400.getJobs(AS400.FILE);
        joblist.add(fileJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.PRINT);
        Job[] printJobs = testAs400.getJobs(AS400.PRINT);
        joblist.add(printJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.COMMAND);
        Job[] commandJobs = testAs400.getJobs(AS400.COMMAND);
        joblist.add(commandJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.DATAQUEUE);
        Job[] dataqueueJobs = testAs400.getJobs(AS400.DATAQUEUE);
        joblist.add(dataqueueJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.DATABASE);
        Job[] databaseJobs = testAs400.getJobs(AS400.DATABASE);
        joblist.add(databaseJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.RECORDACCESS);
        Job[] recordaccessJobs = testAs400.getJobs(AS400.RECORDACCESS);
        joblist.add(recordaccessJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.CENTRAL);
        Job[] centralJobs = testAs400.getJobs(AS400.CENTRAL);
        joblist.add(centralJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.SIGNON);
        Job[] signonJobs = testAs400.getJobs(AS400.SIGNON);
        joblist.add(signonJobs[0].getQualifiedJobName());

        Enumeration<String> jobListEnum = joblist.elements();
        while (jobListEnum.hasMoreElements()) {
          String jobName = jobListEnum.nextElement();

          String expectedVerificationId;
          String expectedRemotePort = "NOTSET";
          String expectedLocalPort = "NOTSET";
          String expectedRemoteIp = "NOTSET";
          String expectedLocalIp = "NOTSET";

          /* Find the TCP/IP information */
          String sql = "select REMOTE_ADDRESS, REMOTE_PORT, LOCAL_ADDRESS, LOCAL_PORT from qsys2.netstat_job_info where JOB_NAME='"
              + jobName + "'";
          ResultSet rs = stmt.executeQuery(sql);
          int count = 1; 
          while (rs.next()) {
            expectedRemoteIp = "Remote_IPAddress=" + rs.getString(1);
            expectedRemotePort = "Remote_Port=" + rs.getString(2);
            expectedLocalIp = "Local_IPAddress=" + rs.getString(3);
            expectedLocalPort = "Local_Port=" + rs.getString(4);
            sb.append("["+count+"] For job "+jobName+" query returned "+expectedRemoteIp+" "+expectedRemotePort+" "+expectedLocalIp+" "+expectedLocalPort);
            count++; 
          }
          rs.close();
          expectedVerificationId = "Verification_ID=QIBM_OS400_JT400";
          if (jobName.indexOf("QRWTSRVR") > 0) { 
            expectedVerificationId = "Verification_ID=QIBM_OS400_QRW_SVR_DDM_DRDA";
          }
          
          if (jobName.indexOf("QZSOSIGN") > 0) { 
            expectedVerificationId = "Verification_ID=QIBM_OS400_QZBS_SVR_SIGNON";
            // The remote port has changed since the signin 
            expectedRemotePort = "Remote_Port=IGNORE";
          }
          
          if (!AuthExit.checkResult(connection_, jobName.replace('/', '.'), testUserid_, sb, expectedVerificationId,
              expectedRemotePort, expectedLocalPort, expectedRemoteIp, expectedLocalIp)) {
            successful = false;
          }
        }
        stmt.close();
        testAs400.close();
        if (!successful)
          skipExitCleanup_ = true;
        assertCondition(successful, sb);
      }

    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  
  /*
   * Use a customer verification ID
   * 
   */
  public void Var005() {
    try {

      StringBuffer sb = new StringBuffer();
      boolean successful = true;
      if (checkExitProgram()) {
        Statement stmt = pwrConnection_.createStatement();
        sb.setLength(0);
        sb.append("Java home is " + System.getProperty("java.home"));
        sb.append(AboutToolbox.getVersionDescription() + "\n");
        sb.append("Calling newInstance to " + systemName_ + "\n");
        /* Note: false is specified below so that the host connection server is not used */
        /* When the host connection server is used, the exit program will only be called for */
        /* the host connect server and the DRDA server */ 
        AS400 testAs400 = (AS400) JDReflectionUtil.callStaticMethod_O("com.ibm.as400.access.AS400", "newInstance", false,
            systemName_);
        testAs400.setGuiAvailable(false);
        testAs400.setUserId(testUserid_);
        testAs400.setPassword(testPassword_.toCharArray());
        JDReflectionUtil.callMethod_V(testAs400,"setVerificationId","JOHNAPP");
        
        /* Connect all the services */
        Vector<String> joblist = new Vector<String>();
        testAs400.connectService(AS400.FILE);
        Job[] fileJobs = testAs400.getJobs(AS400.FILE);
        joblist.add(fileJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.PRINT);
        Job[] printJobs = testAs400.getJobs(AS400.PRINT);
        joblist.add(printJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.COMMAND);
        Job[] commandJobs = testAs400.getJobs(AS400.COMMAND);
        joblist.add(commandJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.DATAQUEUE);
        Job[] dataqueueJobs = testAs400.getJobs(AS400.DATAQUEUE);
        joblist.add(dataqueueJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.DATABASE);
        Job[] databaseJobs = testAs400.getJobs(AS400.DATABASE);
        joblist.add(databaseJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.RECORDACCESS);
        Job[] recordaccessJobs = testAs400.getJobs(AS400.RECORDACCESS);
        joblist.add(recordaccessJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.CENTRAL);
        Job[] centralJobs = testAs400.getJobs(AS400.CENTRAL);
        joblist.add(centralJobs[0].getQualifiedJobName());

        testAs400.connectService(AS400.SIGNON);
        Job[] signonJobs = testAs400.getJobs(AS400.SIGNON);
        joblist.add(signonJobs[0].getQualifiedJobName());

        Enumeration<String> jobListEnum = joblist.elements();
        while (jobListEnum.hasMoreElements()) {
          String jobName = jobListEnum.nextElement();

          String expectedVerificationId;
          String expectedRemotePort = "NOTSET";
          String expectedLocalPort = "NOTSET";
          String expectedRemoteIp = "NOTSET";
          String expectedLocalIp = "NOTSET";

          /* Find the TCP/IP information */
          String sql = "select REMOTE_ADDRESS, REMOTE_PORT, LOCAL_ADDRESS, LOCAL_PORT from qsys2.netstat_job_info where JOB_NAME='"
              + jobName + "'";
          ResultSet rs = stmt.executeQuery(sql);
          int count = 1; 
          while (rs.next()) {
            expectedRemoteIp = "Remote_IPAddress=" + rs.getString(1);
            expectedRemotePort = "Remote_Port=" + rs.getString(2);
            expectedLocalIp = "Local_IPAddress=" + rs.getString(3);
            expectedLocalPort = "Local_Port=" + rs.getString(4);
            sb.append("["+count+"] For job "+jobName+" query returned "+expectedRemoteIp+" "+expectedRemotePort+" "+expectedLocalIp+" "+expectedLocalPort);
            count++; 
          }
          rs.close();
          expectedVerificationId = "Verification_ID=JOHNAPP";
          if (jobName.indexOf("QRWTSRVR") > 0) { 
            expectedVerificationId = "Verification_ID=QIBM_OS400_QRW_SVR_DDM_DRDA";
          }
          
          if (jobName.indexOf("QZSOSIGN") > 0) { 
            expectedVerificationId = "Verification_ID=QIBM_OS400_QZBS_SVR_SIGNON";
            // The remote port has changed since the signin 
            expectedRemotePort = "Remote_Port=IGNORE";
          }
          
          if (!AuthExit.checkResult(connection_, jobName.replace('/', '.'), testUserid_, sb, expectedVerificationId,
              expectedRemotePort, expectedLocalPort, expectedRemoteIp, expectedLocalIp)) {
            successful = false;
          }
        }
        stmt.close();
        testAs400.close();
        if (!successful)
          skipExitCleanup_ = true;
        assertCondition(successful, sb);
      }

    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }

  
  
}
