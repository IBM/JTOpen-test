///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetBigDecimal3.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetBigDecimal3.java
//
// Classes:      JDCSGetBigDecimal3.java
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDLobTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JDLobTest.JDTestBlob;
import test.JDLobTest.JDTestClob;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;


/**
Testcase JDCSGetBigDecimal3.  This tests the following
method of the JDBC CallableStatement class:

     getBigDecimal()

**/
public class JDCSGetBigDecimal3
extends JDTestcase
{

    // Private data.
    private Connection          connection;

/**
Constructor.
**/
    public JDCSGetBigDecimal3 (AS400 systemObject,
			       Hashtable namesAndVars,
			       int runMode,
			       FileOutputStream fileOutputStream,
			       
			       String password)
    {
	super (systemObject, "JDCSGetBigDecimal3",
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
	String url = baseURL_;
	connection = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

    }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
      throws Exception
    {
	connection.close (); 
    }

/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT SMALLINT
**/
    public void Var001()
    {
	if (checkJdbc20()) { 
	    try{


		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10SMINT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setInt(1, 20);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 30.0);

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT INTEGER
**/
    public void Var002()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10INT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setInt(1, 2043);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 2053.0);

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT REAL
**/
    public void Var003()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10REAL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setFloat(1, 20.34f);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 30.34, "Check = "+check+ " and SB 30.34");

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT FLOAT
**/
    public void Var004()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10FLOAT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setDouble(1, 123.456);
		cstmt.execute();

		BigDecimal check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 133.45600000000002, "Check = "+check.doubleValue()+ " and SB 133.45600000000002");

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT DOUBLE
**/
    public void Var005()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10DOUBLE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setDouble(1, 120.312);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 130.312, "Check = "+check+ " and SB 130.0");

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT DECIMAL
**/
    public void Var006()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10DEC50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setBigDecimal(1,new BigDecimal ("12435"));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 12445.0);

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10DEC105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setBigDecimal(1, new BigDecimal("-94732.12345"));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == -94722.12345, "Check = "+check.doubleValue()+ " and SB -94722.12345");

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10NUM50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setBigDecimal(1, new BigDecimal("-1112"));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == -1102);

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}

    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10NUM105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setBigDecimal(1, new BigDecimal("19.98765"));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 29.98765, "Check = "+check.doubleValue()+ " and SB 29.98765");

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT CHAR
**/
    public void Var010()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNCHAR");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setString(1,"3");
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 3.0);

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT CHAR(50)
**/
    public void Var011()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setString(1,"43262243");
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 43262243.0);

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNVARCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setString(1,"50");
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 50.0);

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT BINARY(20)
**/
    public void Var013()
    {
	if (checkJdbc20()) { 
	    try{

		byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89};


		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETRUNBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setBytes(1,b);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	if (checkJdbc20()) { 
	    try{


		byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNVBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setBytes(1,b);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT DATE
**/
    public void Var015()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNDATE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setDate(1, new Date (33));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT TIME
**/
    public void Var016()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNTIME");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setTime(1,new Time (22));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT TIMESTAMP
**/
    public void Var017()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNTS");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setTimestamp(1, new Timestamp (444));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT DATALINK
**/
    public void Var018()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNDL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setString(1,"http://www.sony.com/pix.html");
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT BLOB(200)
**/
    public void Var019()
    {
	if (checkJdbc20()) { 	
	    try{

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBigDecimal() - getBigDecimal on a type registered as DECIMAL, INOUT BIGINT
**/
    public void Var021()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10BINT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
		cstmt.setInt(1, 27850);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 27860.0, "Check = "+check+ " and SB 27860.0");

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
// Now register the Output paramter to be type NUMERIC, should have same results
/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT SMALLINT
**/
    public void Var022()
    {
	if (checkJdbc20()) { 
	    if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 
		try{

		    JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10SMINT");
		    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		    cstmt.setInt(1, 20);
		    cstmt.execute();

		    BigDecimal check = new BigDecimal ("0.0");
		    check = cstmt.getBigDecimal(1);
		    assertCondition(check.doubleValue() == 30.0, check.doubleValue() + " != 30.0" );
		}
		catch (SQLException e){
		    failed (e, "Unexpected Exception");
		}
	    } else {
		notApplicable(); 
	    }
	} 
    }
/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT INTEGER
**/
    public void Var023()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10INT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setInt(1, 2043);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 2053.0, check.doubleValue()+" == 2053.0");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	} 
    }

/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT REAL
**/
    public void Var024()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10REAL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setFloat(1, 20.34f);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 30.34, "Check = "+check+ " and SB 30.34");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	} 
    }

/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT FLOAT
**/
    public void Var025()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10FLOAT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setDouble(1, 123.456);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 133.45600000000002, "Check = "+check+ " and SB 133.45600000000002");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	} 
    }

/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT DOUBLE
**/
    public void Var026()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10DOUBLE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setDouble(1, 120.312);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 130.312, "Check = "+check+ " and SB 130.0");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	} 
    }

/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT DECIMAL
**/
    public void Var027()
    {

	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) {
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10DEC50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setBigDecimal(1,new BigDecimal ("12435"));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 12445.0);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	} 
    }

/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT DECIMAL(10,5)
**/
    public void Var028()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10DEC105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setBigDecimal(1, new BigDecimal("-94732.12345"));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == -94722.12345, "Check = "+check+ " and SB -94722.12345");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	} 
    }

/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT NUMERIC(5,0)
**/
    public void Var029()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10NUM50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setBigDecimal(1, new BigDecimal("-1112"));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == -1102);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	} 
    }


/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT NUMERIC(10,5)
**/
    public void Var030()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10NUM105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setBigDecimal(1, new BigDecimal("19.98765"));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 29.98765, "Check = "+check+ " and SB 29.98765");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	} 
    }

/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT CHAR
**/
    public void Var031()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNCHAR");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setString(1,"3");
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 3.0);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT CHAR(50)
**/
    public void Var032()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setString(1,"43262243");
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 43262243.0);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	} 
    }

/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT VARCHAR(50)
**/
    public void Var033()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNVARCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setString(1,"50");
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 50.0);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}

    }
/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT BINARY(20)
**/
    public void Var034()
    {
	if (checkJdbc20()) { 	
	    try{

		byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89};

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setBytes(1,b);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT VARBINARY(20)
**/
    public void Var035()
    {
	if (checkJdbc20()) { 	
	    try{

		byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNVBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setBytes(1,b);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT DATE
**/
    public void Var036()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNDATE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setDate(1, new Date (33));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT TIME
**/
    public void Var037()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNTIME");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setTime(1,new Time (22));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT TIMESTAMP
**/
    public void Var038()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNTS");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setTimestamp(1, new Timestamp (444));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT DATALINK
**/
    public void Var039()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNDL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setString(1,"http://www.sony.com/pix.html");
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT BLOB(200)
**/
    public void Var040()
    {
	if (checkJdbc20()) { 	
	    try{

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};


		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT CLOB(200)
**/
    public void Var041()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBigDecimal() - getBigDecimal on a type registered as Numeric, INOUT BIGINT
**/
    public void Var042()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection,JDCSTest.COLLECTION,"ADD10BINT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
		cstmt.setInt(1, 27850);
		cstmt.execute();

		BigDecimal check = new BigDecimal ("0.0");
		check = cstmt.getBigDecimal(1);
		assertCondition(check.doubleValue() == 27860.0, "Check = "+check+ " and SB 27860.0");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
}
