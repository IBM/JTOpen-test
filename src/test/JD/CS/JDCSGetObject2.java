///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetObject2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetObject2.java
//
// Classes:      JDCSGetObject2.java
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
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

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDLobTest;
import test.JDTestDriver;
import test.JDTestcase;

/**
Testcase JDCSGetObject2.  This tests the following
method of the JDBC CallableStatement class:

     getObject()

**/
public class JDCSGetObject2
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetObject2";
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
    public JDCSGetObject2 (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetObject2",
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
getObject() - getObject on a type registered as SMALLINT, INOUT SMALLINT
**/
    public void Var001()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
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
getObject() - getObject on a type registered as integer, INOUT INTEGER
**/
    public void Var002()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
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
getObject() - getObject on a type registered as Real, INOUT REAL
**/
    public void Var003()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.REAL);
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
getObject() - getObject on a type registered as Float, INOUT FLOAT
**/
    public void Var004()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
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
getObject() - getObject on a type registered as Double, INOUT DOUBLE
**/
    public void Var005()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DOUBLE);
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
getObject() - getObject on a type registered as Decimal, INOUT DECIMAL
**/
    public void Var006()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
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
getObject() - getObject on a type registered as Decimal, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DECIMAL);
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
getObject() - getObject on a type registered as Numeric, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	
	try{
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
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
getObject() - getObject on a type registered as , INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
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
getObject() - getObject on a type registered as Char, INOUT CHAR
**/
    public void Var010()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
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
getObject() - getObject on a type registered as char, INOUT CHAR(50)
**/
    public void Var011()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
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
getObject() - getObject on a type registered as VARCHAR, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
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
getObject() - getObject on a type registered as String, INOUT BINARY(20)
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
		cstmt.registerOutParameter(1, java.sql.Types.BINARY);
		cstmt.setBytes(1,b);
		cstmt.execute();

		byte [] check = { (byte) 0, (byte) 0, (byte) 0, (byte) 0};
		check = (byte []) cstmt.getObject(1);

		assertCondition(areEqual(check, b));
	    }
	    catch (SQLException e){
		failed(e,"Unexpected Exception");
	    }
	} else {
	    notApplicable(); 
	}
    }
	    
	
    
/**
getObject() - getObject on a type registered as VARBINARY, INOUT VARBINARY(20)
**/
    public void Var014()
    {

	if(getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 
	    try{

		byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
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
getObject() - getObject on a type registered as Date, INOUT DATE
**/
    public void Var015()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setDate(1, Date.valueOf("1910-11-23")); 
	    cstmt.execute();

	    Date check = (Date) cstmt.getObject(1);
	    assertCondition(check.toString().equals("1910-11-23"));
	}
	catch (SQLException e){
	    failed(e,"Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as Time, INOUT TIME
**/
    @SuppressWarnings("deprecation")
    public void Var016()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIME);
	    cstmt.setTime(1,new Time (22, 33, 44));
	    cstmt.execute();

	    Time check = new Time(22,11,22);
	    check = (Time) cstmt.getObject(1);
	    assertCondition((check.toString()).equalsIgnoreCase("22:33:44"), "check = "+check.toString()+ " and SB 22:33:44");
	}
	catch (SQLException e){
	    failed(e,"Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as Timestamp, INOUT TIMESTAMP
**/
    public void Var017()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
                Timestamp t = new Timestamp(444);
		cstmt.setTimestamp(1, t);
		cstmt.execute();

		Timestamp check = new Timestamp(121);
		check = (Timestamp) cstmt.getObject(1);
		assertCondition(check.equals(t), "Got check="+check.toString()+" sb="+t.toString()); 

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable(); 
	} 
    }

/**
getObject() - getObject on a type registered as DataLink, INOUT DATALINK
**/
// we will expect an exception for now..
    public void Var018()
    {
        //note:  illegal to have a Datalink as an inout parameter according to SQL Reference                                //@B1A
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0 && !(isToolboxDriver())) {                //@B1C
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATALINK);
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
getObject() - getObject on a type registered as Blob, INOUT BLOB(200)
**/
    public void Var019()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BLOB);
    byte b[] = { (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03}; 

		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		Blob check = (Blob) cstmt.getObject(1);
		assertCondition(areEqual (b, check.getBytes (1, (int) check.length ())));
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getObject() - getObject on a type registered as Clob, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc20()) { 
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob Test"));
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("afdshlkjadsf");
		check = (Clob) cstmt.getObject(1);
		assertCondition(compare (check, "Test Clob Test"));
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception");
	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception");
	    } 
	}
    }

/**
getObject() - getObject on a type registered as BIGINT, INOUT BIGINT
**/
    public void Var021()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BIGINT);
	    cstmt.setInt(1, 52520);
	    cstmt.execute();
	    
	    Long check = (Long) cstmt.getObject(1);
	    assertCondition(check.longValue() == 52530);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getObject() - getObject on a type registered as boolean, INOUT SMALLINT
**/
    public void Var022()
    {
	String procName="GO2V22ADD1"; 

	try{
	    String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B SMALLINT) LANGUAGE SQL " +
	      "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY SMALLINT; "+
	      "SET DUMMY=B; SET B = DUMMY + 1; END JDCS"+procName ;

	    Statement stmt = connection.createStatement ();
	    try {
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	    stmt.executeUpdate(sql);
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setInt(1, 0);
	    cstmt.execute();

	    Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
	    assertCondition(check.booleanValue() == true, "Added by native driver  11/10/2004");
	}
	catch (Exception e){
	    failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	}
	try {
	    Statement stmt = connection.createStatement ();
	    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	} catch (Exception e) {}    	    
    }

/**
getObject() - getObject on a type registered as boolean, INOUT INTEGER
**/
    public void Var023()
    {
	String procName="GO2V23ADD1"; 
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 
	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B INTEGER) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY INTEGER; "+
		  "SET DUMMY=B; SET B = DUMMY + 1; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setInt(1, 0);
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		assertCondition(check.booleanValue() == true, "added by native  11/10/2004");
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception added by native 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	} else {
	    notApplicable(); 
	} 
    }

/**
getObject() - getObject on a type registered as boolean, INOUT REAL
**/
    public void Var024()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 
	    String procName="GO2V24ADD1"; 

	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B REAL) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY REAL; "+
		  "SET DUMMY=B; SET B = DUMMY + 1; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setFloat(1, 0.0f);
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		assertCondition(check.booleanValue() == true, "added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	} else {
	    notApplicable();
	}
    }

/**
getObject() - getObject on a type registered as boolean, INOUT FLOAT
**/
    public void Var025()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    String procName="GO2V25ADD1"; 
	    try{


		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B FLOAT) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY FLOAT; "+
		  "SET DUMMY=B; SET B = DUMMY + 1; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setDouble(1, 0.0);
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		assertCondition(check.booleanValue() == true, "added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	} else {
	    notApplicable();
	}
    }

/**
getObject() - getObject on a type registered as boolean, INOUT DOUBLE
**/
    public void Var026()
    {
	if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 
	    String procName="GO2V26ADD1"; 
	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B DOUBLE) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY DOUBLE; "+
		  "SET DUMMY=B; SET B = DUMMY + 1; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setDouble(1, 0.0); 
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		assertCondition(check.booleanValue() == true, "Added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	}  else {
	    notApplicable();
	}
    }

/**
getObject() - getObject on a type registered as boolean, INOUT DECIMAL
**/
    public void Var027()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    String procName="GO2V27ADD1"; 
	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B DECIMAL(5,0)) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY DECIMAL(5,0); "+
		  "SET DUMMY=B; SET B = DUMMY + 1; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setBigDecimal(1,new BigDecimal ("0"));
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		assertCondition(check.booleanValue() == true, "Added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	} else {
	    notApplicable();
	}
    }

/**
getObject() - getObject on a type registered as boolean, INOUT DECIMAL(10,5)
**/
    public void Var028()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    String procName="GO2V28ADD1"; 
	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B DECIMAL(10,5)) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY DECIMAL(10,5); "+
		  "SET DUMMY=B; SET B = DUMMY + 1; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setBigDecimal(1, new BigDecimal(0.0)); 
		cstmt.execute();

        Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		assertCondition(check.booleanValue() == true, "Added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	} else {
	    notApplicable();
	}
    }

/**
getObject() - getObject on a type registered as boolean, INOUT NUMERIC(5,0)
**/
    public void Var029()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    String procName="GO2V29ADD1"; 
	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B NUMERIC(5,0)) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY SMALLINT; "+
		  "SET DUMMY=B; SET B = DUMMY + 1; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setBigDecimal(1, new BigDecimal("0")); 
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		assertCondition(check.booleanValue() == true, "Added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	} else {
	    notApplicable();
	}
    }


/**
getObject() - getObject on a type registered as boolean, INOUT NUMERIC(10,5)
**/
    public void Var030()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    String procName="GO2V30ADD1"; 
	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B NUMERIC(10,5)) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY NUMERIC(10,5); "+
		  "SET DUMMY=B; SET B = DUMMY + 1; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setBigDecimal(1, new BigDecimal(0.0)); 
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		assertCondition(check.booleanValue() == true, "Added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	} else {
	    notApplicable();
	}
    }

/**
getObject() - getObject on a type registered as boolean, INOUT CHAR(1)
**/
    public void Var031()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    String procName="GO2V31ADD1"; 
	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B CHAR(1)) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY CHAR(1); "+
		  "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setString(1,"1");
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		assertCondition(check.booleanValue() == true, "Added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	} else {
	    notApplicable();
	}
    }

/**
getObject() - getObject on a type registered as boolean, INOUT CHAR(50)
**/
    public void Var032()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    String procName="GO2V32ADD1"; 
	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B CHAR(50)) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY CHAR(50); "+
		  "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setString(1,"432");
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		assertCondition(check.booleanValue() == true, "Added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	} else {
	    notApplicable();
	}
    }

/**
getObject() - getObject on a type registered as boolean, INOUT VARCHAR(50)
**/
    public void Var033()
    {
	String procName="GO2V33ADD1"; 
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B VARCHAR(50)) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY VARCHAR(50); "+
		  "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setString(1,"50");
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		assertCondition(check.booleanValue() == true, "Added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    

	} else {
	    notApplicable();
	}
    }


/**
getObject() - getObject on a type registered as boolean, INOUT BINARY(20)
**/
    public void Var034()
    {
	String procName="GO2V34ADD1"; 
	
	try{

	    String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B BINARY(20)) LANGUAGE SQL " +
             "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY BINARY(20); "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

	    byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89};

	    Statement stmt = connection.createStatement ();
	    try {
              stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
            } catch (Exception e) {}    	    
	    stmt.executeUpdate(sql);
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
	    failed ("Didn't throw SQLException -- added by native driver 11/10/2004"+check);
	}
	catch (Exception e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	try {
	    Statement stmt = connection.createStatement ();
	    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	} catch (Exception e) {}    	    
   }
	    
	
    
/**
getObject() - getObject on a type registered as boolean, INOUT VARBINARY(20)
**/
    public void Var035()
    {
	String procName="GO2V35ADD1"; 
	
	try{

	    String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B VARBINARY(20)) LANGUAGE SQL " +"SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY VARBINARY(20); "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

	    byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
	    Statement stmt = connection.createStatement ();
	    try {
              stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
            } catch (Exception e) {}    	    
	    stmt.executeUpdate(sql);
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
	    failed ("Didn't throw SQLException -- added by native driver 11/10/2004"+check);
	}
	catch (Exception e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	try {
	    Statement stmt = connection.createStatement ();
	    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	} catch (Exception e) {}    	    
  }

/**
getObject() - getObject on a type registered as boolean, INOUT DATE
**/
    public void Var036()
    {

	String procName="GO2V36ADD1"; 
	
	try{
   
	    String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B DATE) LANGUAGE SQL " +
             "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY DATE; "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

	    Statement stmt = connection.createStatement ();
	    try {
              stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
            } catch (Exception e) {}    	    
	    stmt.executeUpdate(sql);
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setDate(1, new Date (33000));
	    cstmt.execute();

	    Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
	    failed ("Didn't throw SQLException -- added by native driver 11/10/2004"+check);
	}
	catch (Exception e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	try {
	    Statement stmt = connection.createStatement ();
	    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	} catch (Exception e) {}    	    
    }

/**
getObject() - getObject on a type registered as boolean, INOUT TIME
**/
    @SuppressWarnings("deprecation")
    public void Var037()
    {
	String procName="GO2V37ADD1"; 
	
	try{

	    String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B TIME) LANGUAGE SQL " +
             "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY TIME; "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

	    Statement stmt = connection.createStatement ();
	    try {
              stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
            } catch (Exception e) {}    	    
	    stmt.executeUpdate(sql);
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setTime(1,new Time (22, 33, 44));
	    cstmt.execute();

	    Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
	    failed ("Didn't throw SQLException -- added by native driver 11/10/2004"+check);
	}
	catch (Exception e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	try {
	    Statement stmt = connection.createStatement ();
	    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	} catch (Exception e) {}    	    
    }

/**
getObject() - getObject on a type registered as boolean, INOUT TIMESTAMP
**/
    public void Var038()
    {
	String procName="GO2V38ADD1"; 
	
	try{

	    String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B TIMESTAMP) LANGUAGE SQL " +
             "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY TIMESTAMP; "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

	    Statement stmt = connection.createStatement ();
	    try {
              stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
            } catch (Exception e) {}    	    
	    stmt.executeUpdate(sql);
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setTimestamp(1, new Timestamp (444));
	    cstmt.execute();

	    Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
	    failed ("Didn't throw SQLException -- added by native driver 11/10/2004"+check);
	}
	catch (Exception e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	try {
	    Statement stmt = connection.createStatement ();
	    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	} catch (Exception e) {}    	    
    }

/**
getObject() - getObject on a type registered as boolean, INOUT DATALINK
**/
    public void Var039()
    {

	String procName="GO2V39ADD1"; 
	
	try{


	    String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B DATALINK) LANGUAGE SQL " +
             "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY DATALINK; "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

	    Statement stmt = connection.createStatement ();
	    try {
              stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
            } catch (Exception e) {}    	    
	    stmt.executeUpdate(sql);
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
	    cstmt.setString(1,"http://www.sony.com/pix.html");
	    cstmt.execute();

	    Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
	    failed ("Didn't throw SQLException -- added by native driver 11/10/2004"+check);
	}
	catch (Exception e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	try {
	    Statement stmt = connection.createStatement ();
	    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	} catch (Exception e) {}    	    
    }


/**
getObject() - getObject on a type registered as boolean, INOUT BLOB(200)
**/
    public void Var040()
    {

	String procName="GO2V40ADD1"; 
	if (checkJdbc20()) { 
	    try{


		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B BLOB(200)) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY BLOB(200); "+
		  "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		failed ("Didn't throw SQLException -- added by native driver 11/10/2004"+check);
	    }
	    catch (Exception e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	}
    }

/**
getObject() - getObject on a type registered as boolean, INOUT CLOB(200)
**/
    public void Var041()
    {
	String procName="GO2V41ADD1"; 

	if (checkJdbc20()) { 
	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B CLOB(200)) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY CLOB(200); "+
		  "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		failed ("Didn't throw SQLException -- added by native driver 11/10/2004"+check);
	    }
	    catch (Exception e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    

	}
    }


/**
getObject() - getObject on a type registered as boolean, INOUT BIGINT
**/
    public void Var042()
    {
	String procName="GO2V42ADD1"; 

	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) { 	
	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B BIGINT) LANGUAGE SQL " +
		  "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY BIGINT; "+
		  "SET DUMMY=B; SET B = DUMMY + 1; END JDCS"+procName ;

		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
		cstmt.setInt(1, 20);
		cstmt.execute();

		Boolean check;
        if(isToolboxDriver()) {                
            check = Boolean.valueOf((boolean)cstmt.getBoolean(1));
        } else {
            check = (Boolean) cstmt.getObject(1);
        }
		assertCondition(check.booleanValue() == true, "Added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    
	} else {
	    notApplicable(); 
	} 
    }

/**
getObject() - getObject on a type registered as LONGVARBINARY, INOUT VARBINARY(20)
**/
    public void Var043()
    {
	String procName="GO2V43ADD1";

	if(getRelease() >= JDTestDriver.RELEASE_V7R1M0) { 
	    try{

		String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B VARBINARY(20)) LANGUAGE SQL " +"SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY VARBINARY(20); "+
		  "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

		byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
		} catch (Exception e) {}    	    
		stmt.executeUpdate(sql);
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.LONGVARBINARY);
		cstmt.setBytes(1,b);
		cstmt.execute();

		byte [] check = {(byte) 0, (byte) 0, (byte) 0, (byte) 0};
		check = (byte []) cstmt.getObject(1);

		assertCondition(areEqual(check,b), "added by native driver 11/10/2004");
	    }
	    catch (Exception e){
		failed(e,"Unexpected Exception added by native driver 11/10/2004");
	    }
	    try {
		Statement stmt = connection.createStatement ();
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	    } catch (Exception e) {}    	    

	} else {
	    notApplicable(); 
	} 
    }


/**
getObject() - getObject on a type registered as LONGVARCHAR, INOUT VARCHAR(30)
**/
    public void Var044()
    {
	String procName="GO2V44ADD1"; 
	try{
	   
	    String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procName+" (INOUT B VARCHAR(30)) LANGUAGE SQL " +
             "SPECIFIC "+JDCSTest.COLLECTION+"."+procName+" JDCS"+procName+": BEGIN DECLARE DUMMY VARCHAR(30); "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCS"+procName ;

	    Statement stmt = connection.createStatement ();
	    try {
              stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
            } catch (Exception e) {}    	    
	    stmt.executeUpdate(sql);
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+"."+procName+" (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.LONGVARCHAR);
	    cstmt.setString(1,"Test Long VarChar");
	    cstmt.execute();

	    String check = "";
	    check = (String) cstmt.getObject(1);
	    assertCondition(check.equalsIgnoreCase("Test Long VarChar"),"added by native driver 11/10/2004");
	}
	catch (Exception e){
	    failed (e, "Unexpected Exception added by native driver 11/10/2004");
	}
	try {
	    Statement stmt = connection.createStatement ();
	    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procName);
	} catch (Exception e) {}    	    

    }


/**
getObject() - getObject on a type registered as TINYINT, INOUT SMALLINT
**/
    public void Var045()
    {
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");

	    cstmt.registerOutParameter(1, java.sql.Types.TINYINT);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    Integer check = (Integer) cstmt.getObject(1);
	    assertCondition(check.intValue() == 30, "added by native driver 11/10/2004");
	}
	catch (Exception e){
	    failed (e, "Unexpected Exception -- added by native driver 11/10/2004");
	}

    }

}
