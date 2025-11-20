///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionEWLM.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionEWLM.java
//
// Classes:      JDConnectionEWLM
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;
import test.JD.CS.JDCSGetBlob;

import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;
import java.util.Random;
import javax.sql.ConnectionPoolDataSource;

/**
Testcase JDConnectionEWLM.  This tests the following methods
of the JDBC Connection class:

<ul>
<li>setDB2eWLMCorrelator()
</ul>
**/
public class JDConnectionEWLM
extends JDTestcase {

    // Private data.
    private              Connection     closedConnection_;
    private              Connection     pooledConnection_; 


/**
Constructor.
**/
    public JDConnectionEWLM (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password) {
        super (systemObject, "JDConnectionEWLM",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        closedConnection_.close ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        connection_.close ();
        connection_ = null; 

    }



/**
NOTE:  All the ARM API's are hidden by reflection to prevent compile time errors
***/
    Object armTransactionFactory = null;
    Object armApplicationDefinition = null; 
    Object armApplication = null;
    Object armTransactionDefinition =null;
    Class<?>[] newArmTransactionArgs = null; 
    Object armTransaction = null;

    String applicationName = "JDConnectionEWLM"; 
    String transactionName = "TestTransaction";
    Object armProperties = null; 
    Class<?> stringClass = "".getClass(); 
    // 
    // This is shipped on iSeries in /qibm/proddata/WLM/classes/arm.jar
    // I'm not sure what is to be used by toolbox on the PC
    //
    String tranFactoryName = "com.ibm.wlm.arm40SDK.transaction.Arm40TransactionFactory";

    
    protected void setupARM() throws Exception {
	try {

	    if (getRelease() >= JDTestDriver.RELEASE_V7R5M0) {
		System.out.println("Arm tests not working on 7.5 and later"); 
		return; 
	    } 
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {

		//
		// Check to see if the necessary PREREQs are on the system.
		// If not, state what they are so that they can be loaded.
		//
                //
                //
                // Product 5733VE1, options *BASE, 40, 41, 42 and 43.
		//
		try {
		    String options[] = { "0000", "0040", "0041", "0042", "0043" };
		    Statement stmt =  connection_.createStatement();
		    stmt.execute("call QSYS.QCMDEXC('DSPSFWRSC OUTPUT(*OUTFILE) OUTFILE(QGPL/DSPSFWRSC)                         ',  0000000060.00000) ");
		    for (int i = 0; i < options.length; i++) {
			ResultSet rs = stmt.executeQuery("SELECT LCPRDI,LCSFGI  FROM qgpl.dspsfwrsc WHERE LCPRDI='5733VE1' AND LCSFGI='"+options[i]+"'" );
			if (rs.next()) {
			    // result found 
			} else {
			    System.out.println("Warning:  EWLM software not on system:  Please load 5733VE1 option "+options[i]); 
			}
			rs.close(); 
		    } 
		    stmt.close();

		} catch (Exception e) {
		    e.printStackTrace();
		    System.out.println("Warning:  Unable to detemine if product 5733VE1, options *BASE, 40, 41, 42 and 43 are on the system"); 
		} 
                // Add the right library to the library list..
                // Yeah.. This is weird, but they make me do it...
                // This did not work because it must be set before the JVM
                // is created.
		// I use liblist -a QWLM in qshell to add the library list entry. 
		// System.out.println("Calling JDEnableEWLM"); 
		// JDEnableEWLM.enable(); 
		tranFactoryName = "com.ibm.wlm.arm40SDK.transaction.Arm40TransactionFactory";
	    } else {
               	// toolbox needs to put in the factory name
                tranFactoryName = "TOOLBOX need factory"; 
	    } 

	    //
	    // Get the factory
	    // 
	    Class<?> tranFactoryClass = Class.forName(tranFactoryName);
	    Constructor<?> constructor = tranFactoryClass.getDeclaredConstructor();
	    armTransactionFactory = constructor.newInstance();

	    //
	    // Create the application
	    //

	    //  public org.opengroup.arm40.transaction.ArmApplicationDefinition
            //  newArmApplicationDefinition(
            //     java.lang.String,
            //     org.opengroup.arm40.transaction.ArmIdentityProperties,
            //     org.opengroup.arm40.transaction.ArmID);
	    Class<?>[] newArmApplicationDefinitionArgs = new Class[3];
            newArmApplicationDefinitionArgs[0] = stringClass;
	    newArmApplicationDefinitionArgs[1] = Class.forName("org.opengroup.arm40.transaction.ArmIdentityProperties"); 
	    newArmApplicationDefinitionArgs[2] = Class.forName("org.opengroup.arm40.transaction.ArmID");

	    armApplicationDefinition = JDReflectionUtil.callMethod_O(armTransactionFactory, "newArmApplicationDefinition", newArmApplicationDefinitionArgs, applicationName, null, null); 

	    //  public org.opengroup.arm40.transaction.ArmApplication newArmApplication(
            //    org.opengroup.arm40.transaction.ArmApplicationDefinition,
            //    java.lang.String,
            //    java.lang.String,
            //    java.lang.String[]);
	    String[] dummyStringArray = new String[1]; 
	    Class<?>[] newArmApplicationArgs = new Class[4];
	    newArmApplicationArgs[0] = Class.forName("org.opengroup.arm40.transaction.ArmApplicationDefinition");
	    newArmApplicationArgs[1] = stringClass;
	    newArmApplicationArgs[2] = stringClass;
	    newArmApplicationArgs[3] = dummyStringArray.getClass();

	    armApplication = JDReflectionUtil.callMethod_O(armTransactionFactory,
							   "newArmApplication",
							   newArmApplicationArgs,
							   armApplicationDefinition,
							   applicationName,
							   null,
							   null); 

	    //
	    // Create the transaction
	    //

	    // newArmTransactionDefinition(
            //   org.opengroup.arm40.transaction.ArmApplicationDefinition,
            //   java.lang.String,
            //   org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction,
            //   org.opengroup.arm40.transaction.ArmID);

	    Class<?>[] newArmTransactionDefinitionArgs = new Class[4];
	    newArmTransactionDefinitionArgs[0] = Class.forName("org.opengroup.arm40.transaction.ArmApplicationDefinition");
	    newArmTransactionDefinitionArgs[1] = stringClass; 
	    newArmTransactionDefinitionArgs[2] = Class.forName("org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction");
	    newArmTransactionDefinitionArgs[3] = Class.forName("org.opengroup.arm40.transaction.ArmID"); 

	    armTransactionDefinition =
	      JDReflectionUtil.callMethod_O(armTransactionFactory,
					    "newArmTransactionDefinition",
					    newArmTransactionDefinitionArgs,
					    armApplicationDefinition,
					    transactionName,
					    null,
					    null);

								    
	    // public org.opengroup.arm40.transaction.ArmTransaction
            //  newArmTransaction(
            //    org.opengroup.arm40.transaction.ArmApplication,
            //    org.opengroup.arm40.transaction.ArmTransactionDefinition);
	    //    ArmTransaction tran = tranFactory.newArmTransaction(app, tranDef);

	    newArmTransactionArgs = new Class[2];
	    newArmTransactionArgs[0] = Class.forName("org.opengroup.arm40.transaction.ArmApplication");
	    newArmTransactionArgs[1] = Class.forName("org.opengroup.arm40.transaction.ArmTransactionDefinition");

	    armTransaction = JDReflectionUtil.callMethod_O(armTransactionFactory,
							   "newArmTransaction",
							   newArmTransactionArgs,
							   armApplication,
							   armTransactionDefinition); 


	} catch (Exception e) {
	    System.out.println("ERROR:  Exception in setupARM");
	    e.printStackTrace(); 
	    armTransactionFactory = null;
	    throw(e); 
	} 

    } 

    protected void startTransaction() throws Exception {
	if (armTransactionFactory == null) setupARM();

	//
	// Create a new transaction for each iteration
        // 
	armTransaction = JDReflectionUtil.callMethod_O(armTransactionFactory,
						       "newArmTransaction",
						       newArmTransactionArgs,
						       armApplication,
						       armTransactionDefinition); 

	int rc = JDReflectionUtil.callMethod_I(armTransaction, "start");
	System.out.println("ArmTransaction.start() returned "+rc); 
    }

    protected void stopTransaction() throws Exception {
	JDReflectionUtil.callMethod_I(armTransaction, "stop", 0);
	// System.out.println("Stop returned "+rc); 
    }


    
    protected byte[] getCorrelator() throws Exception {
	Object armCorrelator = null; 
	byte[] correlator; 

	// org.opengroup.arm40.transaction.ArmCorrelator getCorrelator();
	armCorrelator = JDReflectionUtil.callMethod_O(armTransaction, "getCorrelator");

	System.out.println("Correlator is "+armCorrelator); 
 	correlator = (byte[]) JDReflectionUtil.callMethod_O(armCorrelator, "getBytes");
	if (correlator == null) {
	    System.out.println("Correlator bytes are null");
	    // Seeing this on V7R1.. create a correlator just so
	    // that we can test the DB2 part
	    correlator = new byte[64];
	    Random random = new Random();
	    for (int i = 0; i < correlator.length; i++) {
		correlator[i] = (byte) ( random.nextInt() % 0xFF); 
	    }
	    System.out.println("Testcase generated Correlator bytes are "+JDCSGetBlob.dumpBytes(correlator));

	} else { 
	    System.out.println("Correlator bytes are "+JDCSGetBlob.dumpBytes(correlator));
	}
	return correlator; 
    } 








/**
setDB2eWLMCorrelator() -- Set a null correlator. Do some work.. Should not be recorded as different transaction.
Sleep for 1 second to identify this transcation. 
**/
    public void Var001() {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R5M0) {
		notApplicable("Arm tests not working on 7.5"); 
		return; 
	    } 

	if ( getDriver() == JDTestDriver.DRIVER_NATIVE) { 
	    try {
		startTransaction(); 

		JDReflectionUtil.callMethod_V(connection_, "setDB2eWLMCorrelator", (byte[]) null);

		Statement stmt = connection_.createStatement();
		ResultSet rs = stmt.executeQuery("Select * from qsys2.qsqptabl");
		rs.close();
		stmt.close(); 

		Thread.sleep(1000); 
		stopTransaction();
		assertCondition(true); 


	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R3 setDB2eWLMCorrelator API -- Added by native 04/01/2004"); 
	}

    }


/**
setDB2eWLMCorrelator() -- Set the correlator and do some work.
**/
    public void Var002() {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R5M0) {
		notApplicable("Arm tests not working on 7.5"); 
		return; 
	    } 

	if ( getDriver() == JDTestDriver.DRIVER_NATIVE) { 
	    try {
		startTransaction(); 
		byte[] correlator = getCorrelator();
		if (correlator == null) {
		    throw new Exception("returned correlator from getCorrelator is null"); 
		} else { 
		    JDReflectionUtil.callMethod_V(connection_, "setDB2eWLMCorrelator", correlator);

		    Statement stmt = connection_.createStatement();
		    ResultSet rs = stmt.executeQuery("Select * from qsys2.qsqptabl");
		    rs.close();
		    stmt.close(); 
		    JDReflectionUtil.callMethod_V(connection_, "setDB2eWLMCorrelator", (byte[]) null);


		    Thread.sleep(2000); 
		    stopTransaction();

		    assertCondition(true); 
		}

	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception -- Added by native 04/01/2004");
	    }
	} else {
	    notApplicable("V5R3 setDB2eWLMCorrelator API"); 
	}


    }


/**
setDB2eWLMCorrelator() -- Set the correlator and do some work.
**/
    public void Var003() {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R5M0) {
		notApplicable("Arm tests not working on 7.5"); 
		return; 
	    } 

	if ( getDriver() == JDTestDriver.DRIVER_NATIVE) { 
	    try {
		startTransaction(); 
		byte[] correlator = getCorrelator();
		if (correlator == null) {
		    throw new Exception("returned correlator is null"); 
		} else { 
		    JDReflectionUtil.callMethod_V(connection_, "setDB2eWLMCorrelator", correlator);

		    Statement stmt = connection_.createStatement();
		    stmt.executeUpdate("CREATE TABLE QTEMP.JDCONNECTIONEWLM (I INT)");
		    stmt.executeUpdate("INSERT INTO  QTEMP.JDCONNECTIONEWLM VALUES(1)");
		    ResultSet rs = stmt.executeQuery("Select * from QTEMP.JDCONNECTIONEWLM");
		    rs.close();
		    stmt.close();

 		    Thread.sleep(3000);
		    stopTransaction();

		    JDReflectionUtil.callMethod_V(connection_, "setDB2eWLMCorrelator", (byte[]) null);
		    assertCondition(true); 
		}

	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception -- Added by native 04/01/2004");
	    }
	} else {
	    notApplicable("V5R3 setDB2eWLMCorrelator API"); 
	}

    }


/**
setDB2eWLMCorrelator() -- Set the correlator on a DB2ConnectionHandle and do some work.
         This comes from a DB2 connection pool. 
**/

    public void Var004() {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R5M0) {
		notApplicable("Arm tests not working on 7.5"); 
		return; 
	    } 

	if ( getDriver() == JDTestDriver.DRIVER_NATIVE) { 
	    try {
		Object dataSource =  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
		
		JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionEWLM.4"));
		pooledConnection_ =  (Connection) JDReflectionUtil.callMethod_O(dataSource, "getConnection");

		startTransaction(); 
		byte[] correlator = getCorrelator();
		if (correlator == null) {
		    throw new Exception("returned correlator is null"); 
		} else { 
		    JDReflectionUtil.callMethod_V(pooledConnection_, "setDB2eWLMCorrelator", correlator);

		    Statement stmt = pooledConnection_.createStatement();
		    stmt.executeUpdate("CREATE TABLE QTEMP.JDCONNECTIONEWLM (I INT)");
		    stmt.executeUpdate("INSERT INTO  QTEMP.JDCONNECTIONEWLM VALUES(1)");
		    ResultSet rs = stmt.executeQuery("Select * from QTEMP.JDCONNECTIONEWLM");
		    rs.close();
		    stmt.close();

 		    Thread.sleep(3000);
		    stopTransaction();

		    JDReflectionUtil.callMethod_V(pooledConnection_, "setDB2eWLMCorrelator", (byte[]) null);
		    assertCondition(true); 
		}

	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception -- Added by native 04/01/2004");
	    }
	} else {
	    notApplicable("V5R3 setDB2eWLMCorrelator API -- native variation "); 
	}

    }






/**
setDB2eWLMCorrelator() -- Set the correlator on a UDBConnectionHandle and do some work.
**/

    public void Var005() {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R5M0) {
		notApplicable("Arm tests not working on 7.5"); 
		return; 
	    } 

	if ( getDriver() == JDTestDriver.DRIVER_NATIVE) { 
	    try {
			ConnectionPoolDataSource dataSource = (ConnectionPoolDataSource) JDReflectionUtil
					.createObject("com.ibm.db2.jdbc.app.UDBConnectionPoolDataSource");
			JDReflectionUtil.callMethod_V(dataSource, "setDatabaseName", system_);
			JDReflectionUtil.callMethod_V(dataSource, "setUser", userId_);
			JDReflectionUtil.callMethod_V(dataSource, "setPassword",
					PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionEWLM.5"));
			pooledConnection_ = dataSource.getPooledConnection().getConnection();

		startTransaction(); 
		byte[] correlator = getCorrelator();
		if (correlator == null) {
		    throw new Exception("returned correlator is null"); 
		} else { 
		    JDReflectionUtil.callMethod_V(pooledConnection_, "setDB2eWLMCorrelator", correlator);

		    Statement stmt = pooledConnection_.createStatement();
		    stmt.executeUpdate("CREATE TABLE QTEMP.JDCONNECTIONEWLM (I INT)");
		    stmt.executeUpdate("INSERT INTO  QTEMP.JDCONNECTIONEWLM VALUES(1)");
		    ResultSet rs = stmt.executeQuery("Select * from QTEMP.JDCONNECTIONEWLM");
		    rs.close();
		    stmt.close();

 		    Thread.sleep(3000);
		    stopTransaction();

		    JDReflectionUtil.callMethod_V(pooledConnection_, "setDB2eWLMCorrelator", (byte[]) null);
		    assertCondition(true); 
		}

	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception -- Added by native 04/01/2004");
	    }
	} else {
	    notApplicable("V5R3 setDB2eWLMCorrelator API -- native variation "); 
	}

    }





}



