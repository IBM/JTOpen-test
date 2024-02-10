///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetTime2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetTime2.java
//
// Classes:      JDCSGetTime2.java
//
////////////////////////////////////////////////////////////////////////
//
//
//                              
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDLobTest;
import test.JDTestcase;
import test.JDLobTest.JDTestBlob;
import test.JDLobTest.JDTestClob;

import java.awt.TextArea;
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
Testcase JDCSGetTime2.  This tests the following
method of the JDBC CallableStatement class:

     getTime()

**/
public class JDCSGetTime2
extends JDTestcase
{

    // Private data.
    private Connection          connection;
 
/**
Constructor.
**/
    public JDCSGetTime2 (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetTime2",
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
        String url = baseURL_
            
            ;
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
getTime() - getTime on a type registered as Time, INOUT SMALLINT
**/
    public void Var001()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT INTEGER
**/
    public void Var002()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10INT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setInt(1, 313);
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT REAL
**/
    public void Var003()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10REAL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setFloat(1, 20.34f);
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT FLOAT
**/
    public void Var004()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10FLOAT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setDouble(1, 123.456);
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT DOUBLE
**/
    public void Var005()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10DOUBLE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setDouble(1, 120.312);
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT DECIMAL
**/
    public void Var006()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10DEC50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setBigDecimal(1,new BigDecimal ("12435"));
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10DEC105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setBigDecimal(1, new BigDecimal(-94732.12345));
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10NUM50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setBigDecimal(1, new BigDecimal("-1112"));
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getTime() - getTime on a type registered as Time, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10NUM105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setBigDecimal(1, new BigDecimal(19.98765));
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT CHAR
**/
    public void Var010()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNCHAR1");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR1 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setString(1,"3");
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT CHAR(50)
**/
    public void Var011()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setString(1,"12:02:34");
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    assertCondition((check.toString()).equalsIgnoreCase("12:02:34"), " check = "+check.toString()+" and SB 12:02:34");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNVARCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setString(1,"12:12:12");
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    assertCondition((check.toString()).equalsIgnoreCase("12:12:12"));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getTime() - getTime on a type registered as Time, INOUT BINARY(20)
**/
    public void Var013()
    {
	
	try{
	   
	    byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89};

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }
	    
	
    
/**
getTime() - getTime on a type registered as Time, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	
	try{
	   
	    byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNVBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVINB20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT DATE
**/
    public void Var015()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setDate(1, Date.valueOf("1923-10-10"));
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT TIME
**/
    public void Var016()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNTIME");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setTime(1,new Time (22, 33, 44));
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    assertCondition((check.toString()).equalsIgnoreCase("22:33:44"));
	}
	catch (SQLException e){
	    failed(e, "Unexpected Exception");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT TIMESTAMP
**/
    public void Var017()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNTS");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setTimestamp(1, new Timestamp (1913,23,45,11,11,11,0));
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    assertCondition((check.toString()).equalsIgnoreCase("11:11:11"), "check = "+check+" and SB 11:11:11");
	}
	catch (SQLException e){
	    failed ("Unexpected Exception");
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT DATALINK
**/
    public void Var018()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNDL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setString(1,"http://www.sony.com/pix.html");
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getTime() - getTime on a type registered as Time, INOUT BLOB(200)
**/
    public void Var019()
    {
	if (checkJdbc20()) { 
	    try{

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.TIME);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		Time check = cstmt.getTime(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getTime() - getTime on a type registered as Time, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.TIME);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		Time check = cstmt.getTime(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getTime() - getTime on a type registered as Time, INOUT BIGINT
**/
    public void Var021()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10BINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setInt(1, 2650);
	    cstmt.execute();

	    Time check = cstmt.getTime(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }
}
