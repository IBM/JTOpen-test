///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetURL2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetURL2.java
//
// Classes:      JDCSGetURL2.java
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
import java.net.URL;


/**
Testcase JDCSGetURL2.  This tests the following
method of the JDBC CallableStatement class:

     getURL()

**/
public class JDCSGetURL2
extends JDTestcase
{

    // Private data.
    private Connection          connection;
 
/**
Constructor.
**/
    public JDCSGetURL2 (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetURL2",
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
getURL() - getURL on a type registered as DATALINK, INOUT SMALLINT
**/
    public void Var001()
    {
	if (checkJdbc30()) { 
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10SMINT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setInt(1, 20);
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT INTEGER
**/
    public void Var002()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10INT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setInt(1, 20);
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT REAL
**/
    public void Var003()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10REAL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setFloat(1, 20.34f);
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT FLOAT
**/
    public void Var004()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10FLOAT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setDouble(1, 123.456);
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT DOUBLE
**/
    public void Var005()
    {
	if (checkJdbc30()) { 

	    try{
		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10DOUBLE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setDouble(1, 120.312);
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT DECIMAL
**/
    public void Var006()
    {
	if (checkJdbc30()) { 

	    try{
		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10DEC50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setBigDecimal(1,new BigDecimal ("12435"));
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10DEC105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setBigDecimal(1, new BigDecimal(-94732.12345));
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10NUM50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setBigDecimal(1, new BigDecimal("-1112"));
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }

/**
getURL() - getURL on a type registered as DATALINK, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10NUM105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setBigDecimal(1, new BigDecimal(19.98765));
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT CHAR(1)
**/
    public void Var010()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNCHAR1");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR1 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setString(1,"3");
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT CHAR(50)
**/
    public void Var011()
    {

	if (checkJdbc30()) { 
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setString(1,"43262243");
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNVARCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setString(1,"50");
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }

/**
getURL() - getURL on a type registered as DATALINK, INOUT BINARY(20)
**/
    public void Var013()
    {
	if (checkJdbc30()) { 

	    try{


		byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89};

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setBytes(1,b);
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }    
	
    
/**
getURL() - getURL on a type registered as DATALINK, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	if (checkJdbc30()) { 

	    try{

		byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNVBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setBytes(1,b);
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT DATE
**/
    public void Var015()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNDATE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setDate(1, new Date (33));
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT TIME
**/
    public void Var016()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNTIME");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setTime(1,new Time (22, 33, 44));
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT TIMESTAMP
**/
    public void Var017()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNTS");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setTimestamp(1, new Timestamp (444));
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
/**
getURL() - getURL on a type registered as DATALINK, INOUT DATALINK
**/
// @B2 We will agree with toolbox and expect the exception for now....
    public void Var018()
    {
        //Note:  It is illegal to have a DATALINK as an OUT/INOUT parameter according the the DB2 SQL Reference         //@B1A
	if ( getRelease() >=  JDTestDriver.RELEASE_V5R3M0 && !(isToolboxDriver())) {           //@B1C
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNDL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setString(1,"http://www.ibm.com/us/");
		System.out.println("After setString");
		cstmt.execute();
		System.out.println("After execute");

		URL check = cstmt.getURL(1);
		failed ("Didn't throw SQLException "+check);		
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	} else {
	    notApplicable(); 
	} 
    }


/**
getURL() - getURL on a type registered as DATALINK, INOUT BLOB(200)
**/
    public void Var019()
    {
	if(checkJdbc30()) /* $B4 named parameters need jdbc 3.0 */
	{
	    if ( getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {
		try{

		    byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

		    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNBLOB200");
		    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		    cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		    cstmt.execute();

		    URL check = new URL("http://www.ibm.com/us");
		    check = cstmt.getURL(1);
		    failed ("Didn't throw SQLException "+check);
		}
		catch (SQLException e){
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
		catch(Exception e){
		    failed(e, "Unexpected Exception"); 
		}
	    } else {
		notApplicable(); 
	    }
	} 
    }

/**
getURL() - getURL on a type registered as DATALINK, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }


/**
getURL() - getURL on a type registered as DATALINK, INOUT BIGINT
**/
    public void Var021()
    {
	if (checkJdbc30()) { 

	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10BINT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
		cstmt.setInt(1, 20);
		cstmt.execute();

		URL check = new URL("http://www.ibm.com/us");
		check = cstmt.getURL(1);
		failed("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    catch(Exception e){
		failed(e, "Unexpected Exception"); 
	    }
	}
    }
}
