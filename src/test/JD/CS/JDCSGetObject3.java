///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetObject3.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetObject3.java
//
// Classes:      JDCSGetObject3.java
//
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// Here is the coverage matrix for the various types
//
//   Type            Variations
//
//   ARRAY             NA 
//   BIGINT            21
//   BINARY            13
//   BIT               NA 
//   BLOB              19
//   BOOLEAN           22-42
//   CHAR              10,11
//   CLOB              20
//   DATALINK          18
//   DATE              15
//   DECIMAL           6,7
//   DISTINCT          NA 
//   DOUBLE            5
//   FLOAT             4
//   INTEGER           2
//   JAVA_OBJECT       NA
//   LONGVARBINARY     43 
//   LONGVARCHAR       44 
//   NULL              NA
//   NUMERIC           8,9
//   OTHER             NA
//   REAL              3
//   REF               NA
//   SMALLINT          1
//   STRUCT            NA
//   TIME              16
//   TIMESTAMP         17
//   TINYINT           45
//   VARBINARY         14
//   VARCHAR           12





package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDLobTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
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
import java.net.*;
import java.sql.Blob;
import java.sql.Clob;

/**
Testcase JDCSGetObject3.  This tests the following
method of the JDBC CallableStatement class:

     getObject()

This testcases tests when the values are registered using Types.JAVA_OBJECT.
It was derived from JDCSGetObject2

**/
public class JDCSGetObject3
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetObject3";
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
    public JDCSGetObject3 (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetObject3",
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
        if(isToolboxDriver())              //@B1A
            url = url + ";date format=iso";                         //@B1A
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
Compares a Clob with a String.
**/
    private boolean compare (Clob i, String b)
    throws SQLException
    {
        return i.getSubString (1, (int) i.length ()).equals (b);    // @D1C
    }



/**
getObject() - getObject on a type registered as Object, INOUT SMALLINT
**/
    public void Var001()
    {
	
	try{
	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    Integer check = (Integer) cstmt.getObject(1);
	    assertCondition(check.intValue() == 30);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as object, INOUT INTEGER
**/
    public void Var002()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setInt(1, -413);
	    cstmt.execute();
	    
	    Integer check = (Integer) cstmt.getObject(1);
	    assertCondition(check.intValue() == -403);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT REAL
**/
    public void Var003()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setFloat(1, 20.34f);
	    cstmt.execute();

	    Float check = (Float) cstmt.getObject(1);
	    assertCondition(check.floatValue() == 30.34f);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT FLOAT
**/
    public void Var004()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setDouble(1, 123.456);
	    cstmt.execute();

	    Double check = (Double) cstmt.getObject(1);
	    assertCondition(check.doubleValue() == 133.45600000000002, "check = "+check.doubleValue()+" SB 133.45600000000002");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT DOUBLE
**/
    public void Var005()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setDouble(1, 120.312);
	    cstmt.execute();

	    Double check = (Double) cstmt.getObject(1);
	    assertCondition(check.doubleValue() == 130.312);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT DECIMAL
**/
    public void Var006()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setBigDecimal(1,new BigDecimal ("12435"));
	    cstmt.execute();

	    BigDecimal check = new BigDecimal(0.0);
	    check = (BigDecimal) cstmt.getObject(1);
	    assertCondition(check.intValue() == 12445, " check = "+check+ " and SB 12445");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setBigDecimal(1, new BigDecimal("-94732.12345"));
	    cstmt.execute();

	    BigDecimal check = new BigDecimal(0.0);
	    check = (BigDecimal) cstmt.getObject(1);
	    assertCondition(check.doubleValue() == -94722.12345, "check = "+check.doubleValue()+" SB -94722.12345");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setBigDecimal(1, new BigDecimal("-1112"));
	    cstmt.execute();

	    BigDecimal check = new BigDecimal(0.0);
	    check = (BigDecimal) cstmt.getObject(1);
	    assertCondition(check.intValue() == -1102);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setBigDecimal(1, new BigDecimal("19.98765"));
	    cstmt.execute();

	    BigDecimal check = new BigDecimal(0.0);
	    check = (BigDecimal) cstmt.getObject(1);
	    assertCondition(check.doubleValue() == 29.98765, "check = "+check.doubleValue()+" SB 29.98765");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT CHAR
**/
    public void Var010()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR1");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR1(?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setString(1,"Z");
	    cstmt.execute();

	    String check = null;
	    check = (String) cstmt.getObject(1);
	    assertCondition(check.equalsIgnoreCase("Z"));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT CHAR(50)
**/
    public void Var011()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setObject(1,"TestString");
	    cstmt.execute();

	    String check = "";
	    check = (String) cstmt.getObject(1);
	 assertCondition(check.equalsIgnoreCase("TestString                                        "));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setString(1,"Test VarChar 50");
	    cstmt.execute();

	    String check = "";
	    check = (String) cstmt.getObject(1);
	    assertCondition(check.equalsIgnoreCase("Test VarChar 50"));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT BINARY(20)
**/
    public void Var013()
    {
	if(getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 
	    try{
		byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) (10),
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) (10),
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) (10),
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) (10)};

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
		cstmt.setBytes(1,b);
		cstmt.execute();

		byte [] check = { (byte) 0, (byte) 0, (byte) 0, (byte) 0};
		check = (byte []) cstmt.getObject(1);
		StringBuffer sb = new StringBuffer(); 
		boolean passed =areEqual(check, b, sb); 
		assertCondition(passed, sb);
	    }
	    catch (SQLException e){
		failed(e,"Unexpected Exception");
	    }
	} else {
	    notApplicable(); 
	}
    }
	    
	
    
/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT VARBINARY(20)
**/
    public void Var014()
    {

	if(getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 
	    try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");

		byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
		cstmt.setBytes(1,b);
		cstmt.execute();

		byte [] check = {(byte) 0, (byte) 0, (byte) 0, (byte) 0};
		check = (byte []) cstmt.getObject(1);

		assertCondition(areEqual(check,b));
	    }
	    catch (SQLException e){
		failed(e,"Unexpected Exception");
	    }
	} else {
	    notApplicable(); 
	} 
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT DATE
**/
    public void Var015()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setDate(1, Date.valueOf("1910-11-23"));
	    cstmt.execute();

	    Date check;
	    check = (Date) cstmt.getObject(1);
	    assertCondition(check.toString().equals("1910-11-23"));
	}
	catch (SQLException e){
	    failed(e,"Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT TIME
**/
    public void Var016()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setTime(1,new Time (22, 33, 44));
	    cstmt.execute();

	    Time check ;
	    check = (Time) cstmt.getObject(1);
	    assertCondition((check.toString()).equalsIgnoreCase("22:33:44"), "check = "+check.toString()+ " and SB 22:33:44");
	}
	catch (SQLException e){
	    failed(e,"Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT TIMESTAMP
**/
    public void Var017()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 
	    try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
                Timestamp t = new Timestamp(444);
		cstmt.setTimestamp(1, t);
		cstmt.execute();

		Timestamp check;
		check = (Timestamp) cstmt.getObject(1);
		// What goes in should go out
		assertCondition(check.equals(t));                   //@B1A
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable(); 
	} 
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT DATALINK
**/
// we will expect an exception for now..
    public void Var018()
    {
        //note:  illegal to have a Datalink as an inout parameter according to SQL Reference                                //@B1A
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0 && !(isToolboxDriver())) {                //@B1C
	    try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
		cstmt.setObject(1,"http://www.ibm.com/us/");
		cstmt.execute();

		URL check = (URL) cstmt.getObject(1);
		failed("Didn't throw and Exeception"+check);                                               /*@B2*/
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");                            /*@B2*/ 
	    }
	} else {
	    notApplicable(); 
	} 
    }


/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT BLOB(200)
**/
    public void Var019()
    {
	if (checkJdbc20()) { 
	    try{
		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		byte [] b = {(byte) 23, (byte) 65, (byte) 43, (byte) 45};
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		Blob check = (Blob) cstmt.getObject(1);
		assertCondition(areEqual (b, check.getBytes (1, (int) check.length ())));
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc20()) { 
	    try{
		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob Test"));
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("afdshlkjadsf");
		check = (Clob) cstmt.getObject(1);
		assertCondition(compare (check, "Test Clob Test"));
	    }
	    catch (Exception e){
		failed(e, "Unexpected Exception");
	    }
	}
    }

/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT BIGINT
**/
    public void Var021()
    {
	
	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setInt(1, 52520);
	    cstmt.execute();
	    
	    Long check = (Long) cstmt.getObject(1);
	    assertCondition(check.longValue() == 52530);
	}
	catch (Exception e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT VARBINARY(20)
**/
    public void Var022()
    {

	if(getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 
	    try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
	   byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
		cstmt.setBytes(1,b);
		cstmt.execute();

		byte [] check = {(byte) 0, (byte) 0, (byte) 0, (byte) 0};
		check = (byte []) cstmt.getObject(1);

		assertCondition(areEqual(check,b), "added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed(e,"Unexpected Exception added by native driver 11/10/2004");
	    }
	} else {
	    notApplicable(); 
	} 
    }


/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT VARCHAR(30)
**/
    public void Var023()
    {

	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR30");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR30 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setString(1,"Test Long VarChar");
	    cstmt.execute();

	    String check = "";
	    check = (String) cstmt.getObject(1);
	    assertCondition(check.equalsIgnoreCase("Test Long VarChar"),"added by native driver 11/10/2004");
	}
	catch (Exception e){
	    failed (e, "Unexpected Exception added by native driver 11/10/2004");
	}

    }


/**
getObject() - getObject on a type registered as JAVA_OBJECT, INOUT SMALLINT
**/
    public void Var024()
    {

	try{

	   JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    Integer check = (Integer) cstmt.getObject(1);
	    assertCondition(check.intValue() == 30, "added by native driver 11/10/2004");
	}
	catch (Exception e){
	    failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	}

    }



/**
getObject() - getObject on a type registered as JAVA_OBJECT, XML
**/
    public void Var025()
    {
       String info = " -- added by native driver 11/23/2009 -- Pass output XML paramter.  Fails for native JDBC driver.  Need to fix in future.";
        if (checkXmlSupport()) { 
            try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNXML");
                CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNXML (?)}");
                cstmt.registerOutParameter(1, java.sql.Types.JAVA_OBJECT);
                cstmt.setString(1, "<test>var</test>");
                cstmt.execute();

		String expectedClass = "com.ibm.db2.jdbc.app.DB2Clob";
		String expectedValue = "<test>var</test>";
		
		if (isJdbc40() && getDriver() == JDTestDriver.DRIVER_NATIVE) { /* Expected native class */ 
		  expectedClass = "com.ibm.db2.jdbc.app.DB2SQLXML"; 
		}else if (isToolboxDriver()) {
		    if (isJdbc40()) {
			// Fixed 03/10/2017 -- the XML locator use to have a bug where it just returned the input
			expectedClass = "com.ibm.as400.access.AS400JDBCSQLXMLLocator"; 
		    } else { 
			expectedClass = "com.ibm.as400.access.AS400JDBCClobLocator"; 
		    }
		}
                Object check =  cstmt.getObject(1);
		if (check == null) {
		    check = "NULL OBJECT RETURNED"; 
		} 
		boolean passed = true;
		String message = ""; 
		String checkClass = check.getClass().getName(); 
		if (!checkClass.equals(expectedClass)) {
		    message+= "expected "+expectedClass+" got "+checkClass;
		    passed = false;

		    // Returns clob prior to JDBC 4.0 
		    if (check instanceof java.sql.Clob) {
			String checkValue = ((Clob) check).getSubString(1, 16);
			if (!checkValue.equals(expectedValue)) {
			    message+= "expected "+expectedValue+" got "+checkValue;
			    passed = false;
			} 
		    } else if (expectedClass.equals("com.ibm.db2.jdbc.app.DB2SQLXML")) {
                      String checkValue = JDReflectionUtil.callMethod_S(check, "getString");
                      if (!checkValue.equals(expectedValue)) {
                          message+= "expected "+expectedValue+" got "+checkValue;
                          passed = false;
                      } 
	      
		    } else {
		      message += " unexpected output type "; 
		      passed =false; 
		    }
		} 

		
                assertCondition(passed,  message + " "+info);
            }
            catch (Exception e){
		failed (e, "Unexpected Exception "+info);
            }

        }
    }







}
