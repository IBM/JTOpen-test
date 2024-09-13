///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetInt2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetInt2.java
//
// Classes:      JDCSGetInt2.java
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
Testcase JDCSGetInt2.  This tests the following
method of the JDBC CallableStatement class:

     getInt()

**/
public class JDCSGetInt2
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetInt2";
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
    public JDCSGetInt2 (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetInt2",
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
getInt() - getInt on a type registered as INTEGER, INOUT SMALLINT
**/
    public void Var001()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == 30);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT INTEGER
**/
    public void Var002()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setInt(1, 420);
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == 430);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT REAL
**/
    public void Var003()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setFloat(1, 20.54f);
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == 30);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT FLOAT
**/
    public void Var004()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setDouble(1, 123.456);
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == 133);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT DOUBLE
**/
    public void Var005()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setDouble(1, 120.312);
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == 130);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT DECIMAL
**/
    public void Var006()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setBigDecimal(1,new BigDecimal ("12435"));
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == 12445);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setBigDecimal(1, new BigDecimal(-4732.12345));
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == -4722);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setBigDecimal(1, new BigDecimal("-1112"));
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == -1102);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getInt() - getInt on a type registered as INTEGER, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setBigDecimal(1, new BigDecimal(19.98765));
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == 29);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT CHAR
**/
    public void Var010()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setString(1,"3");
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == 3);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT CHAR(50)
**/
    public void Var011()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setString(1,"2243");
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == 2243);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setString(1,"50");
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == 50);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getInt() - getInt on a type registered as INTEGER, INOUT BINARY(20)
**/
    public void Var013()
    {
	
	try{
	   
	    byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89};

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }
	    
	
    
/**
getInt() - getInt on a type registered as INTEGER, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	
	try{
	   
	    byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT DATE
**/
    public void Var015()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setDate(1, new Date (33));
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT TIME
**/
    public void Var016()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setTime(1,new Time (22, 33, 44));
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT TIMESTAMP
**/
    public void Var017()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setTimestamp(1, new Timestamp (444));
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT DATALINK
**/
    public void Var018()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setString(1,"http://www.sony.com/pix.html");
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getInt() - getInt on a type registered as INTEGER, INOUT BLOB(200)
**/
    public void Var019()
    {
	if (checkJdbc20()) { 
	    try{


		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		int check = 0;
		check = cstmt.getInt(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getInt() - getInt on a type registered as INTEGER, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		int check = 0;
		check = cstmt.getInt(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getInt() - getInt on a type registered as INTEGER, INOUT BIGINT
**/
    public void Var021()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	    cstmt.setInt(1, 2320);
	    cstmt.execute();

	    int check = 0;
	    check = cstmt.getInt(1);
	    assertCondition(check == 2330);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }
}
