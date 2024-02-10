///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetByte2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetByte2.java
//
// Classes:      JDCSGetByte2.java
//
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDLobTest;
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
Testcase JDCSGetByte2.  This tests the following
method of the JDBC CallableStatement class:

     getByte()

**/
public class JDCSGetByte2
extends JDTestcase
{

    // Private data.
    private Connection          connection;
 
/**
Constructor.
**/
    public JDCSGetByte2 (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetByte2",
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
getByte() - getByte on a type registered as SMALLINT, INOUT SMALLINT
**/
    public void Var001()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == 30);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT INTEGER
**/
    public void Var002()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setInt(1, -45);
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == -35);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT REAL
**/
    public void Var003()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setFloat(1, 20.34f);
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == 30, "check = "+check+" and SB 30");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT FLOAT
**/
    public void Var004()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setDouble(1, 103.456);
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == 113, "check = "+check+" and SB 133");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT DOUBLE
**/
    public void Var005()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setDouble(1, 76.312);
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == 86, "check = "+check+" and SB 86");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT DECIMAL
**/
    public void Var006()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setBigDecimal(1,new BigDecimal ("79"));
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == 89, "check = "+check+" and SB 89");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setBigDecimal(1, new BigDecimal(-73.12345));
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == -63, "check = "+check+" and SB -63");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setBigDecimal(1, new BigDecimal("-12"));
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == -2, "check = "+check+" and SB -2");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getByte() - getByte on a type registered as SMALLINT, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setBigDecimal(1, new BigDecimal(19.98765));
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == 29, "check = "+check+" and SB 29");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT CHAR
**/
    public void Var010()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setString(1,"3");
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == 3, "check = "+check+" and SB 3");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");	
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT CHAR(50)
**/
    public void Var011()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setString(1,"43");
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == 43, "check = "+check+" and SB 43");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setString(1,"50");
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == 50);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getByte() - getByte on a type registered as SMALLINT, INOUT BINARY(20)
**/
    public void Var013()
    {
	
	try{
	   
	    byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89};

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }
	    
	
    
/**
getByte() - getByte on a type registered as SMALLINT, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	
	try{
	   
	    byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT DATE
**/
    public void Var015()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setDate(1, new Date (33));
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT TIME
**/
    public void Var016()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setTime(1,new Time (22));
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT TIMESTAMP
**/
    public void Var017()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setTimestamp(1, new Timestamp (444));
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT DATALINK
**/
    public void Var018()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setString(1,"http://www.sony.com/pix.html");
	    cstmt.execute();

	    byte check = (byte) 0;
	    check = cstmt.getByte(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getByte() - getByte on a type registered as SMALLINT, INOUT BLOB(200)
**/
    public void Var019()
    {
	if (checkJdbc20()) {
	    try{

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLO200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		byte check = (byte) 0;
		check = cstmt.getByte(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getByte() - getByte on a type registered as SMALLINT, INOUT CLOB(200)
**/
    public void Var020()
    {

	if (checkJdbc20()) { 
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		byte  check = (byte) 0;
		check = cstmt.getByte(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getByte() - getByte on a type registered as SMALLINT, INOUT BIGINT
**/
    public void Var021()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
	    cstmt.setInt(1, 80);
	    cstmt.execute();

	    byte  check = (byte) 0;
	    check = cstmt.getByte(1);
	    assertCondition(check == 90, "check = "+check+" and SB 90");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }
}
