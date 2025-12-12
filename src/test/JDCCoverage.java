///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCCoverage.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCCoverage.java
//
// Classes:      JDCCoverage
//
////////////////////////////////////////////////////////////////////////

package test;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JD.JDSetupCollection;


public class JDCCoverage extends JDJSTPTestcase {

  // Private data.

  static final String sourcepath1 = "jdbc/client/test";
  static final String exppath1 = "jdbc/client/exp";
  String userId = null;
  String collection = "QJAVASTP";
  String collection2 = "QJAVASTP";

  static final String testUser = "newtonjudf";
  static char[]  encryptedTestPass = PasswordVault.getEncryptedPassword("newtonp1");


  static {

  }

  /**
   * Constructor.
   **/
  public JDCCoverage(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,  String password) {
    super(systemObject, "JDCCoverage", namesAndVars, runMode, fileOutputStream,
 password);
  }

  public JDCCoverage(AS400 systemObject, String testcaseName,
      Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, testcaseName, namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {

    super.setup();

    String[] diffIgnores1 = { "Jdbc:.*Cleanup Thread Running",
                                };

    setDiffIgnores(diffIgnores1, "test.JDCCoverage");

    setupUserProfile(testUser, encryptedTestPass);

    setSourcepath(sourcepath1);
    setExppath(exppath1);
    collection = JDCTest.COLLECTION;
    collection2 = "JDC"+collection.substring(3); 
    
    Properties replaceProperties=new Properties();
    replaceProperties.put("REPLACE_COLLECTION", collection);
    replaceProperties.put("REPLACE_JDJSTPENV", JDJSTPTestcase.envLibrary );
    replaceProperties.put("REPLACE_jdjstpenv", JDJSTPTestcase.envLibrary );
    replaceProperties.put("REPLACE_TESTUSER", testUser);
    replaceProperties.put("REPLACE_TESTPASS", PasswordVault.decryptPasswordLeak(encryptedTestPass));
    replaceProperties.put("REPLACE_PWRUSER", userId_);
    String password = "passwordLeak:JDCCoverage";
    char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
    // For now use the string.. In the future, this will be removed.
    password = new String(charPassword);
    PasswordVault.clearPassword(charPassword);
     

    replaceProperties.put("REPLACE_PWRPASS", password);
    replaceProperties.put("REPLACE_BASEURL", baseURL_);

    setJdbcReplace("COLLECTION", collection2); 
    setJdbcReplace("TESTUSER", testUser); 
    setJdbcReplace("TESTPASS", PasswordVault.decryptPasswordLeak(encryptedTestPass)); 
    setJdbcReplace("BASEURL", baseURL_); 

    JDSQL400.setProperties(replaceProperties);

    //
    // Make sure the collection exists
    //
    {
	Connection setupConnection_ =    (Connection) testDriver_.getConnection(baseURL_ , userId_, encryptedPassword_);
	JDSetupCollection.create(setupConnection_,collection,false);
  JDSetupCollection.create(setupConnection_,collection2,false);
	setupConnection_.close();
    }


    //
    // open a connection to the schema
    // this connection remains open until the testcase ends and is
    // used by the variations of the testcase
    //

    connection_ = (Connection) testDriver_.getConnection(baseURL_ + "/"
        + collection, userId_, encryptedPassword_);


    connectionConnect(connection_, "JDCCoverage");

    try {
	JDSetupCollection.create(connection_, collection, false);

      connection_ = (Connection) testDriver_.getConnection(baseURL_ + "/"
          + collection, userId_, encryptedPassword_);

      connectionConnect(connection_, "JDCCoverage");

    } catch (Exception e) {
     
      // Already exists
    }

    setLibrary(collection);

    //
    // Make sure STPJOBLOG is available
    //
    assureSTPJOBLOGisAvailable(connection_);

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {

    //
    // Close the connection we just opened
    //
    connectionDisconnect(connection_, "JDCCoverage");

    if (connection_ != null) {
      connection_.close();
    }
    connection_ = null;

  }

  public void runTest(String variation) {
      try {

          RasUser(variation, testUser, encryptedTestPass);
          assertCondition(true);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } catch (Throwable e) {
      failed(e, "Unexpected Throwable");
    }
  }


  public void Var001() {       
    if (checkNative()) { 
      runTest("Coverage1.jdbc");   
    }
  }
  // Running with exception on first line
  public void Var002() {       runTest("Coverage2.jdbc");   }
  // Running with an empty file 
  public void Var003() {       runTest("Coverage3.jdbc");   }
  // output format HTML
  public void Var004() {       runTest("Coverage4.jdbc");   }
  // output format XML 
  public void Var005() {       runTest("Coverage5.jdbc");   }
  // printstacktrace coverage
  public void Var006() {       runTest("Coverage6.jdbc");   }
  // Threading
  public void Var007() {       runTest("Coverage7.jdbc");   }
// Getschema
  public void Var008() {       runTest("Coverage8.jdbc");   }

//debug and more drivers
  public void Var009() {   
    System.setProperty("com.ibm.as400.access.jdbcClient.drivers",
       "com.ibm.as400.access.AS400JDBCDriver:us.jdpwrsys.MasterDriver:jdpwrsys.us.otherDriver"); 
    System.setProperty("com.ibm.as400.access.jdbcClient.debug",  "true")  ; 
    runTest("Coverage9.jdbc");  
    System.setProperty("com.ibm.as400.access.jdbcClient.debug",  "false")  ; 
    System.setProperty("com.ibm.as400.access.jdbcClient.drivers","com.ibm.as400.access.AS400JDBCDriver");
    
  }
  
  // Higher level paths. 
  public void Var010() {       runTest("Coverage10.jdbc");   }
  // connection pool tests 
  public void Var011() {       runTest("Coverage11.jdbc");   }
  // call tests
  public void Var012() {       runTest("Coverage12.jdbc");   }
  // Reflection testing
  public void Var013() {       runTest("Coverage13.jdbc");   }
  

 }
