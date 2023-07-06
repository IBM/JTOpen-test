///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetBoolean2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetBoolean2.java
//
// Classes:      JDCSGetBoolean2.java
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
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
Testcase JDCSGetBoolean2.  This tests the following
method of the JDBC CallableStatement class:

     getBoolean()

**/
public class JDCSGetBoolean2
extends JDTestcase
{

    // Private data.
    private Connection          connection;

/**
Constructor.
**/
    public JDCSGetBoolean2 (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetBoolean2",
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
getBoolean() - getBoolean on a type registered as boolean, INOUT SMALLINT
**/
    public void Var001()
    {

	try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    boolean check = false;
	    check = cstmt.getBoolean(1);
	    assertCondition(check == true);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT INTEGER
**/
    public void Var002()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) { 
	    try{
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setInt(1, 20);
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		assertCondition(check == true);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable(); 
	} 
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT REAL
**/
    public void Var003()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) { 

	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setFloat(1, 20.34f);
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		assertCondition(check == true);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT FLOAT
**/
    public void Var004()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setDouble(1, 123.456);
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		assertCondition(check == true);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT DOUBLE
**/
    public void Var005()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) { 
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setDouble(1, 120.312);
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		assertCondition(check == true);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}  else {
	    notApplicable();
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT DECIMAL
**/
    public void Var006()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setBigDecimal(1,new BigDecimal ("12435"));
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		assertCondition(check == true);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setBigDecimal(1, new BigDecimal(-9472.12345));
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		assertCondition(check == true);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setBigDecimal(1, new BigDecimal("-1112"));
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		assertCondition(check == true);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	}
    }


/**
getBoolean() - getBoolean on a type registered as boolean, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setBigDecimal(1, new BigDecimal(19.98765));
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		assertCondition(check == true);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT CHAR(1)
**/
    public void Var010()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR1");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR1 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setString(1,"3");
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		assertCondition(check == true);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT CHAR(50)
**/
    public void Var011()
    {
	if (getRelease() ==  JDTestDriver.RELEASE_V5R3M0) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setString(1,"43262243");
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		assertCondition(check == true);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setString(1,"50");
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		assertCondition(check == true);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable();
	}
    }


/**
getBoolean() - getBoolean on a type registered as boolean, INOUT BINARY(20)
**/
    public void Var013()
    {
	
	try{
	   
	    byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89};

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    boolean check = false;
	    check = cstmt.getBoolean(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }
	    
	
    
/**
getBoolean() - getBoolean on a type registered as boolean, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	
	try{
	   
	    
	    byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    boolean check = false;
	    check = cstmt.getBoolean(1);
	    failed ("Didn't throw SQLException"+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT DATE
**/
    public void Var015()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setDate(1, new Date (33));
	    cstmt.execute();

	    boolean check = false;
	    check = cstmt.getBoolean(1);
	    failed ("Didn't throw SQLException"+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT TIME
**/
    public void Var016()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setTime(1,new Time (22));
	    cstmt.execute();

	    boolean check = false;
	    check = cstmt.getBoolean(1);
	    failed ("Didn't throw SQLException"+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT TIMESTAMP
**/
    public void Var017()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setTimestamp(1, new Timestamp (444));
	    cstmt.execute();

	    boolean check = false;
	    check = cstmt.getBoolean(1);
	    failed ("Didn't throw SQLException"+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT DATALINK
**/
    public void Var018()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setString(1,"http://www.sony.com/pix.html");
	    cstmt.execute();

	    boolean check = false;
	    check = cstmt.getBoolean(1);
	    failed ("Didn't throw SQLException"+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getBoolean() - getBoolean on a type registered as boolean, INOUT BLOB(200)
**/
    public void Var019()
    {

	if (checkJdbc20()) { 
	    try{

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBoolean() - getBoolean on a type registered as boolean, INOUT CLOB(200)
**/
    public void Var020()
    {

	if (checkJdbc20()) { 
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		failed ("Didn't throw SQLException"+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getBoolean() - getBoolean on a type registered as boolean, INOUT BIGINT
**/
    public void Var021()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) { 	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setInt(1, 20);
		cstmt.execute();

		boolean check = false;
		check = cstmt.getBoolean(1);
		assertCondition(check == true);
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable(); 
	} 
    }
}
