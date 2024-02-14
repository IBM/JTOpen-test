///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetBlob2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetBlob2.java
//
// Classes:      JDCSGetBlob2.java
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
import java.sql.Blob;


/**
Testcase JDCSGetBlob2.  This tests the following
method of the JDBC CallableStatement class:

     getBlob()

The purpose of this testcase, as compared to JDCSGetBlob is to test the getBlob
method against other datatypes. 

**/
public class JDCSGetBlob2
extends JDTestcase
{

    // Private data.
    private Connection          connection;

/**
Constructor.
**/
    public JDCSGetBlob2 (AS400 systemObject,
			 Hashtable namesAndVars,
			 int runMode,
			 FileOutputStream fileOutputStream,
			 
			 String password)
    {
	super (systemObject, "JDCSGetBlob2",
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

/** //B1A
Compares a Blob with a byte[].
**/
/* 
    private boolean compare (Blob i, byte[] b)
    throws SQLException
    {
        byte[] iBytes = i.getBytes (1, (int) i.length ()); 
        return isEqual (iBytes, b);
    }
*/

/**
getBlob() - getBlob on a type registered as BLOB, INOUT SMALLINT
**/
    public void Var001()
    {
	if (checkJdbc20()) { 
	    try{
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setInt(1, 20);
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b); 
		check = cstmt.getBlob(1);
		failed("Didn't throw SQLException but got "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT INTEGER
**/
    public void Var002()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setInt(1, 20);
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b); 
		check = cstmt.getBlob(1);
		failed("Didn't throw SQLException but got "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT REAL
**/
    public void Var003()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setFloat(1, 20.34f);
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b);
		check = cstmt.getBlob(1);
		failed("Didn't throw SQLException check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT FLOAT
**/
    public void Var004()
    {
	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setDouble(1, 123.456);
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b);
		check = cstmt.getBlob(1);
		failed("Didn't throw SQLException check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBlob() - getBlob on a type registered as BLOB, INOUT DOUBLE
**/
    public void Var005()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setDouble(1, 120.312);
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b);
		check = cstmt.getBlob(1);
		failed("Didn't throw SQLException check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT DECIMAL
**/
    public void Var006()
    {
	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setBigDecimal(1,new BigDecimal ("12435"));
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b); 
		check = cstmt.getBlob(1);
		failed("Didn't throw SQLException check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setBigDecimal(1, new BigDecimal(-94732.12345));
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b);
		check = cstmt.getBlob(1);
		failed("Didn't throw SQLException check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setBigDecimal(1, new BigDecimal("-1112"));
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b);
		check = cstmt.getBlob(1);
		failed("Didn't throw SQLException  check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBlob() - getBlob on a type registered as BLOB, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	if (checkJdbc20()) {	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setBigDecimal(1, new BigDecimal(19.98765));
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b);
		check = cstmt.getBlob(1);
		failed("Didn't throw SQLException  check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT CHAR
**/
    public void Var010()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setString(1,"3");
		cstmt.execute();

		Blob check = cstmt.getBlob(1);   
                byte[] expected = new byte[] {(byte)3};
		if(isToolboxDriver())  {    //@B1A
		    StringBuffer sb = new StringBuffer();
		    boolean passed = compare(check, expected, sb); 
                    assertCondition(passed,sb);      //@B1A
		} else                                                //@B1A
                    failed("Didn't throw SQLException");
	    }
	    catch (SQLException e){
                if(isToolboxDriver())      //@B1A
                    failed(e, "Unexpected Exception");              //@B1A
                else                                                //@B1A
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT CHAR(50)
**/
    public void Var011()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setString(1,"43262243");
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b); 
		check = cstmt.getBlob(1);
		failed("Didn't throw SQLException  check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setString(1,"50");
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b);
		check = cstmt.getBlob(1);
                byte[] expected = new byte[] {(byte)80};            //@B1A   80 in hex in ascii decimal is 50 in hex
		if(isToolboxDriver())    {   //@B1A
		    StringBuffer sb = new StringBuffer();
		    boolean passed = compare(check, expected, sb); 
		    assertCondition(passed,sb);      //@B1A


		}
                else                                                //@B1A
                    failed("Didn't throw SQLException");
	    }
	    catch (SQLException e){
                if(isToolboxDriver())      //@B1A
                    failed(e, "Unexpected Exception");              //@B1A
                else                                                //@B1A
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBlob() - getBlob on a type registered as BLOB, INOUT BINARY(20)
**/
    public void Var013()
    {
	String info = " -- Updated 01/03/2012 for native driver"; 
	if (checkJdbc20()) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {         //BINARY is valid for V5r3 or higher	
		try{

		    byte [] p = {(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) (10)};

		    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
		    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		    cstmt.setBytes(1,p);
		    cstmt.execute();

		    Blob check = cstmt.getBlob(1);
		    byte [] expected =  {(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) (10), 
		    (byte)00, (byte)00, (byte)00, (byte)00, (byte)00, (byte)00,
		    (byte)00, (byte)00, (byte)00, (byte)00, (byte)00, (byte)00,
		    (byte)00, (byte)00, (byte)00};
		    StringBuffer sb = new StringBuffer(info);
		    sb.append(" -- ");
		    boolean passed = compare(check, expected, sb); 
		    assertCondition(passed, info+sb.toString());             //@B1A
		}
		catch (SQLException e){
			failed(e, "Unexpected Exception"+info);              //@B1A
		}
	    } else { 
		notApplicable("BINARY testcase for >= V5R3");
	    }
	}
    }


/**
getBlob() - getBlob on a type registered as BLOB, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	String info = " -- Updated 01/03/2012 for native driver"; 
	if (checkJdbc20()) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 	    //VARBINARY is valid for V5R3 or greater
		try{

		    byte [] p = {(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) (10)};

		    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
		    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20(?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		    cstmt.setBytes(1,p);
		    cstmt.execute();

		    byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		    Blob check = new JDLobTest.JDTestBlob(b);
		    check = cstmt.getBlob(1);
		    StringBuffer sb = new StringBuffer(); 
		    boolean passed  = compare(check, p, sb);
			assertCondition(passed , info + sb.toString());             //@B1A
		}
		catch (SQLException e){
			failed(e, "Unexpected Exception"+info);              //@B1A
		}
	    } else { 
		notApplicable("VARBINARY testcase for >= V5R3");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT DATE
**/
    public void Var015()
    {
	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setDate(1, new Date (33));
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b);
		check = cstmt.getBlob(1);
		failed ("Didn't throw SQLException  check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT TIME
**/
    public void Var016()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setTime(1,new Time (22));
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b); 
		check = cstmt.getBlob(1);
		failed ("Didn't throw SQLException check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT TIMESTAMP
**/
    public void Var017()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setTimestamp(1, new Timestamp (444));
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b);
		check = cstmt.getBlob(1);
		failed ("Didn't throw SQLException  check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT DATALINK
**/
    public void Var018()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setString(1,"http://www.sony.com/pix.html");
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b);
		check = cstmt.getBlob(1);
		failed ("Didn't throw SQLException check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBlob() - getBlob on a type registered as BLOB, INOUT BLOB(200)
**/
    public void Var019()
    {
	if (checkJdbc20()) { 	
	    try{

		byte [] p = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(p));
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b); 
		check = cstmt.getBlob(1);
		assertCondition (areEqual (p, check.getBytes (1, (int) check.length ())));
	    }
	    catch (SQLException e){
		failed(e,"Unexpected Exception");
	    }
	}
    }
/**
getBlob() - getBlob on a type registered as BLOB, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b); 
		check = cstmt.getBlob(1);
		failed ("Didn't throw SQLException check="+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBlob() - getBlob on a type registered as BLOB, INOUT BIGINT
**/
    public void Var021()
    {
	if (checkJdbc20()) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		cstmt.setInt(1, 20);
		cstmt.execute();

		byte [] b = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		Blob check = new JDLobTest.JDTestBlob(b); 
		check = cstmt.getBlob(1);
                    failed("Didn't throw SQLException but got "+check);
	    }
	    catch (SQLException e){
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
}

