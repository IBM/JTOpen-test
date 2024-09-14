///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetFloat2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetFloat2.java
//
// Classes:      JDCSGetFloat2.java
//
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
Testcase JDCSGetFloat2.  This tests the following
method of the JDBC CallableStatement class:

     getFloat()

**/
public class JDCSGetFloat2
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetFloat2";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }

    // Private data.
    private Connection          connection;
 
/**
Constructor.
**/
    public JDCSGetFloat2 (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetFloat2",
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
getFloat() - getFloat on a type registered as REAL, INOUT SMALLINT
**/
    public void Var001()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == 30.0f);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT INTEGER
**/
    public void Var002()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setInt(1, 444);
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == 454.0f);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT REAL
**/
    public void Var003()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setFloat(1, 20.34f);
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == 30.34f, " check = "+check+" and SB 30.34");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT FLOAT
**/
    public void Var004()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setDouble(1, 123.456);
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == 133.456f, " check = "+check+" and SB 133.456");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT DOUBLE
**/
    public void Var005()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setDouble(1, 120.312);
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == 130.312f, " check = "+check+" and SB 130.312");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT DECIMAL
**/
    public void Var006()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setBigDecimal(1,new BigDecimal ("12435"));
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == 12445.0f);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setBigDecimal(1, new BigDecimal("-94732.12345"));
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == -94722.12345f, " check = "+check+" and SB -94722.12345");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setBigDecimal(1, new BigDecimal("-1112"));
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == -1102.0f);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getFloat() - getFloat on a type registered as REAL, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setBigDecimal(1, new BigDecimal("19.98765"));
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == 29.98765f, " check = "+check+" and SB 29.98765");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT CHAR
**/
    public void Var010()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setString(1,"3");
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == 3.0f);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT CHAR(50)
**/
    public void Var011()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setString(1,"43262.243");
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == 43262.243f," check = "+check+" and SB 43262.243");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setString(1,"50");
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == 50f);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getFloat() - getFloat on a type registered as REAL, INOUT BINARY(20)
**/
    public void Var013()
    {
	
	try{
	   
	    byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89};

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }
	    
	
    
/**
getFloat() - getFloat on a type registered as REAL, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	
	try{
	   
	    byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT DATE
**/
    public void Var015()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setDate(1, new Date (33));
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT TIME
**/
    public void Var016()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setTime(1,new Time (22, 33, 44));
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT TIMESTAMP
**/
    public void Var017()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setTimestamp(1, new Timestamp (444));
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT DATALINK
**/
    public void Var018()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setString(1,"http://www.sony.com/pix.html");
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getFloat() - getFloat on a type registered as REAL, INOUT BLOB(200)
**/
    public void Var019()
    {
	if (checkJdbc20()) { 
	    try{

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.REAL);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		float check = cstmt.getFloat(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.REAL);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		float check = cstmt.getFloat(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getFloat() - getFloat on a type registered as REAL, INOUT BIGINT
**/
    public void Var021()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
	    cstmt.setInt(1, 212434);
	    cstmt.execute();

	    float check = cstmt.getFloat(1);
	    assertCondition(check == 212444.0f, " check = "+check+" and SB 212444.0");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }
}
