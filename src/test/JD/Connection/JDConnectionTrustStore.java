/////// ////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionTrustStore.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2025 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JDTestcase;

/**
 * Testcase JDConnectionTruststore. This tests the following methods of the JDBC
 * Connection class:
 * 
 * <ul>
 * <li>tls truststore property
 * <li>tls truststore password property
 * <li>DriverManager.getConnection()
 * </ul>
 * 
 * The file cacerts should exist in the current directory (normally
 * /home/jdbctest) and should contain the certificates need for SSL to use the
 * target system. The password for the cacerts file should be the default
 * password changeit.
 **/
public class JDConnectionTrustStore extends JDTestcase {

  StringBuffer sb = new StringBuffer();

  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "JDConnectionTrustStore";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.JDConnectionTest.main(newArgs);
  }

  /**
   * Constructor.
   **/
  public JDConnectionTrustStore(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, String password) {
    super(systemObject, "JDConnectionTrustStore", namesAndVars, runMode, fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {
     sb.setLength(0);; 
     JDTestDriver.expectException = true; 
     try { 
       sb.append("\nChecking for cacerts");
       File f = new File ("cacerts"); 
       if (!f.exists()) { 
         output_.println("WARNING: cacerts does not exist in the current directory"); 
         output_.println(" * The file cacerts should exist in the current directory (normally\n"
             + " * /home/jdbctest) and should contain the certificates need for SSL to the\n"
             + " * target system. The password for the cacerts file should be the default\n"
             + " * password changeit.\n"
             + " **");
         output_.println("The current directory is "+System.getProperty("user.dir"));
       }
       
       /* Create an empty cacerts file */ 
         String storePassword = "changeit";
         String storeName = "emptyCacerts";
         String storeType = "jks";
         try  {
           FileOutputStream fileOutputStream = new FileOutputStream(storeName);
           KeyStore keystore = KeyStore.getInstance(storeType);
           keystore.load(null, storePassword.toCharArray());
           
           
           String encodedString = 
           "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";
           
           Decoder decoder = Base64.getDecoder();
           byte[] decodedData = decoder.decode(encodedString);

           
           ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedData);

           CertificateFactory cf = CertificateFactory.getInstance("X.509");

           Certificate cert = cf.generateCertificate(inputStream);
           
          keystore.setCertificateEntry("BASE",cert); 
           keystore.store(fileOutputStream, storePassword.toCharArray());
         } catch (Exception e) {
           output_.println("Warning: Creating emptyCacerts failed"); 
           e.printStackTrace();
       }
       
     } catch (Exception e) { 
       output_.println("WARNING: Exception during setup"+sb.toString()); 
       e.printStackTrace(output_); 
     }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    JDTestDriver.expectException = false; 
    
  }

  /**
   * tls truststore / tls truststore Verify that *ANY/*ANY works.
   **/
  public void Var001() {
    if (checkToolbox()) {
      if (checkNotNative()) { 
      try {
        boolean passed = true;
        sb.setLength(0);
        
        String testURL = baseURL_ + ";secure=true;tls truststore=*ANY;tls truststore password=*ANY";
        sb.append("\nConnecting with URL=" + testURL);
        Connection c = testDriver_.getConnection(testURL, userId_, encryptedPassword_);
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("Select JOB_NAME from sysibm.sysdummy1");
        rs.next();
        String jobname = rs.getString(1);
        String expectedJobname = "QZDASSINIT";
        if (isNative_) {
          expectedJobname = "QZDASOINIT";
        }
        if (jobname.indexOf(expectedJobname) < 0) {
          passed = false;
          sb.append("\nJob name was " + jobname + " expected to see " + expectedJobname);
        }
        rs.close();
        s.close();
        c.close();
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
      }
    }
  }

  /**
   * tls truststore / tls truststore Verify that *ANY / GARBAGE  fails
   **/
  public void Var002() {
    if (checkToolbox() ) {
      String[] expectedErrorMessages = {
          "The filename, directory name, or volume label syntax is incorrect",
          "A file or directory in the path name does not exist.",
          "No such file or directory"
      };
      if (isNative_) {
        assertCondition(true, "Native does not use SSL"); 
        return; 
      }
      try {
        boolean passed = false;
        sb.setLength(0);
        String testURL = baseURL_ + ";secure=true;tls truststore=*ANY;tls truststore password=GARBAGE";
        sb.append("\nConnecting with URL=" + testURL);
        Connection c = testDriver_.getConnection(testURL, userId_, encryptedPassword_);
        passed = false;
        sb.append("\nExpcted exception but got "+c);
        c.close();
        assertCondition(passed, sb);
      } catch (Exception e) {
        assertExceptionContains(e, expectedErrorMessages, sb.toString());
      }
    }
  }
  
  /**
   * tls truststore / tls truststore Verify that *ANY / not setfails
   **/
  public void Var003() {
    if (checkToolbox() && checkNotNative()) {
      String[] expectedErrorMessages = {
          "The filename, directory name, or volume label syntax is incorrect",
          "A file or directory in the path name does not exist.",
          "No such file or directory"
      };
      if (isNative_) {
        assertCondition(true, "Native does not use SSL"); 
        return; 
      }

      try {
        boolean passed = false;
        sb.setLength(0);
        String testURL = baseURL_ + ";secure=true;tls truststore=*ANY";
        sb.append("\nConnecting with URL=" + testURL);
        Connection c = testDriver_.getConnection(testURL, userId_, encryptedPassword_);
        passed = false;
        sb.append("\nExpcted exception but got "+c);
        c.close();
        assertCondition(passed, sb);
      } catch (Exception e) {
        assertExceptionContains(e, expectedErrorMessages, sb.toString());
      }
    }
  }
  

  /**
   * tls truststore / tls truststore Verify that cacerts / no password  works.
   **/
  public void Var004() {
    if (checkToolbox() && checkNotNative()) {
      try {
        boolean passed = true;
        sb.setLength(0);
        
        String testURL = baseURL_ + ";secure=true;tls truststore=cacerts";
        sb.append("\nConnecting with URL=" + testURL);
        Connection c = testDriver_.getConnection(testURL, userId_, encryptedPassword_);
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("Select JOB_NAME from sysibm.sysdummy1");
        rs.next();
        String jobname = rs.getString(1);
        String expectedJobname = "QZDASSINIT";
        if (isNative_) {
          expectedJobname = "QZDASOINIT";
        }
        if (jobname.indexOf(expectedJobname) < 0) {
          passed = false;
          sb.append("\nJob name was " + jobname + " expected to see " + expectedJobname);
        }
        rs.close();
        s.close();
        c.close();
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * tls truststore / tls truststore Verify that cacerts / changeit  works.
   **/
  public void Var005() {
    if (checkToolbox() && checkNotNative()) {
      try {
        boolean passed = true;
        sb.setLength(0);
        
        String testURL = baseURL_ + ";secure=true;tls truststore=cacerts;tls truststore password=changeit";
        sb.append("\nConnecting with URL=" + testURL);
        Connection c = testDriver_.getConnection(testURL, userId_, encryptedPassword_);
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("Select JOB_NAME from sysibm.sysdummy1");
        rs.next();
        String jobname = rs.getString(1);
        String expectedJobname = "QZDASSINIT";
        if (isNative_) {
          expectedJobname = "QZDASOINIT";
        }
        if (jobname.indexOf(expectedJobname) < 0) {
          passed = false;
          sb.append("\nJob name was " + jobname + " expected to see " + expectedJobname);
        }
        rs.close();
        s.close();
        c.close();
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * tls truststore / tls truststore Verify that cacerts / bad password  fails
   **/
  public void Var006() {
    if (checkToolbox() ) {
      String expectedErrorMessage = "Keystore was tampered with, or password was incorrect"; 
      if (isNative_) {
        assertCondition(true, "Native does not use SSL"); 
        return; 
      }
      try {
        boolean passed = false;
        sb.setLength(0);
        String testURL = baseURL_ + ";secure=true;tls truststore=cacerts;tls truststore password=GARBAGE";
        sb.append("\nConnecting with URL=" + testURL);
        Connection c = testDriver_.getConnection(testURL, userId_, encryptedPassword_);
        passed = false;
        sb.append("\nExpcted exception but got "+c);
        c.close();
        assertCondition(passed, sb);
      } catch (Exception e) {
        assertExceptionContains(e, expectedErrorMessage, sb);
      }
    }
  }
  
  /**
   * tls truststore / tls truststore Verify that emptycacerts / good password  fails
   **/
  public void Var007() {
    if (checkToolbox()) {
      if (isNative_) {
        assertCondition(true, "Native does not use SSL"); 
        return; 
      }
      String expectedErrorMessage = "PKIX path building failed"; 
      try {
        boolean passed = false;
        sb.setLength(0);
        String testURL = baseURL_ + ";secure=true;tls truststore=emptyCacerts;tls truststore password=changeit";
        sb.append("\nConnecting with URL=" + testURL);
        Connection c = testDriver_.getConnection(testURL, userId_, encryptedPassword_);
        passed = false;
        sb.append("\nExpcted exception but got "+c);
        c.close();
        assertCondition(passed, sb);
      } catch (Exception e) {
        assertExceptionContains(e, expectedErrorMessage, sb);
      }
    }
  }

  /**
   * tls truststore / tls truststore Verify that emptycacerts / no password  fails
   **/
  public void Var008() {
    if (checkToolbox()) {
      if (isNative_) {
        assertCondition(true, "Native does not use SSL"); 
        return; 
      }
      String expectedErrorMessage = "PKIX path building failed"; 
      try {
        boolean passed = false;
        sb.setLength(0);
        String testURL = baseURL_ + ";secure=true;tls truststore=emptyCacerts";
        sb.append("\nConnecting with URL=" + testURL);
        Connection c = testDriver_.getConnection(testURL, userId_, encryptedPassword_);
        passed = false;
        sb.append("\nExpcted exception but got "+c);
        c.close();
        assertCondition(passed, sb);
      } catch (Exception e) {
        assertExceptionContains(e, expectedErrorMessage, sb);
      }
    }
  }

  
}
