///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetBytes2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetBytes2.java
//
// Classes:      JDCSGetBytes2.java
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDLobTest;
import test.JDTestcase;



/**
Testcase JDCSGetBytes2.  This tests the following
method of the JDBC CallableStatement class:

     getBytes()

**/
public class JDCSGetBytes2
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetBytes2";
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
    public JDCSGetBytes2 (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetBytes2",
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

/** Utility Function **/
    static String DumpBytes(byte[] b)
    {
	String s = "";
	for (int i = 0; i < b.length; i++){
	    String ns = Integer.toHexString(((int) b[i]) & 0xFF);
	    if(ns.length() == 1)
		s += "0"+ns;
	    else
		s += ns;
	}
	return s;
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
getBytes() - getBytes on a type registered as BINARY, INOUT SMALLINT
**/
    public void Var001()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT INTEGER
**/
    public void Var002()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT REAL
**/
    public void Var003()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setFloat(1, 20.34f);
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT FLOAT
**/
    public void Var004()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setDouble(1, 123.456);
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT DOUBLE
**/
    public void Var005()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setDouble(1, 120.312);
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT DECIMAL
**/
    public void Var006()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setBigDecimal(1,new BigDecimal ("12435"));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setBigDecimal(1, new BigDecimal(-94732.12345));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setBigDecimal(1, new BigDecimal("-1112"));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getBytes() - getBytes on a type registered as BINARY, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setBigDecimal(1, new BigDecimal(19.98765));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT CHAR
**/
    public void Var010()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setString(1,"3");
	    cstmt.execute();

	    byte [] check = cstmt.getBytes(1);                                  //@B1C
            byte [] expected = new byte[] {(byte)3};                            //@B1A
            if(isToolboxDriver())                      //@B1A
                assertCondition(areEqual(check, expected));                      //@B1A
	    else                                                                //@B1A
                failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
            if(isToolboxDriver())                      //@B1A
                failed(e, "Unexpected Exception");                              //@b1A
            else                                                                //@B1A
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT CHAR(50)
**/
    public void Var011()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setString(1,"43262243");
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setString(1,"50");        //50 in hex is 80 in ascii decimal
	    cstmt.execute();

	    byte [] check = cstmt.getBytes(1);                  //@B1C
            byte [] expected = new byte[] {(byte)80};                            //@B1A
            if(isToolboxDriver())                      //@B1A
                assertCondition(areEqual(check, expected));                      //@B1A
            else                                                                //@B1A
                failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
            if(isToolboxDriver())                      //@B1A
                failed(e, "Unexpected Exceptin");                               //@B1A
            else                                                                //@B1A
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getBytes() - getBytes on a type registered as BINARY, INOUT BINARY(20)
**/
    public void Var013()
    {
	if (true) {
	    try{

		byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 43,
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 43,
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 43,
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 43};

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BINARY);
		cstmt.setBytes(1,b);
		cstmt.execute();

		byte [] check = b; 
		check = cstmt.getBytes(1);
		StringBuffer sb = new StringBuffer();
		boolean passed = areEqual(check, b, sb); 
		assertCondition(passed,sb); 
	    }
	    catch (SQLException e){
		failed("Unexpected Exception");
		e.printStackTrace(); 
	    }
	} 
    }
	    
	
    
/**
getBytes() - getBytes on a type registered as BINARY, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	if (true) {
	    try{


		byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BINARY);
		cstmt.setBytes(1,b);
		cstmt.execute();

		byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		check = cstmt.getBytes(1);
		assertCondition(areEqual(check,b));
	    }
	    catch (SQLException e){
		failed("Unexpected Exception");
	    }
	} 
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT DATE
**/
    public void Var015()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setDate(1, new Date (33));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT TIME
**/
    public void Var016()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setTime(1,new Time (22));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT TIMESTAMP
**/
    public void Var017()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setTimestamp(1, new Timestamp (444));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT DATALINK
**/
    public void Var018()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setString(1,"http://www.sony.com/pix.html");
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getBytes() - getBytes on a type registered as BINARY, INOUT BLOB(200)
**/
    public void Var019()
    {
	if (true) {	
	    try{


		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BINARY);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		check = cstmt.getBytes(1);
		assertCondition(areEqual(check, b));         //@B1A
                
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception");          //@B1A
	    }
	} 
    }

/**
getBytes() - getBytes on a type registered as BINARY, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc20()) { 
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.BINARY);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		check = cstmt.getBytes(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getBytes() - getBytes on a type registered as BINARY, INOUT BIGINT
**/
    public void Var021()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    // Updated 12/22/2011
	    // Exception thrown by both drivers
	    byte [] check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }
// now register the output parameter as VARBINARY

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT SMALLINT
**/
    public void Var022()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT INTEGER
**/
    public void Var023()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT REAL
**/
    public void Var024()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setFloat(1, 20.34f);
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT FLOAT
**/
    public void Var025()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setDouble(1, 123.456);
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT DOUBLE
**/
    public void Var026()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setDouble(1, 120.312);
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT DECIMAL
**/
    public void Var027()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setBigDecimal(1,new BigDecimal ("12435"));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT DECIMAL(10,5)
**/
    public void Var028()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setBigDecimal(1, new BigDecimal(-94732.12345));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT NUMERIC(5,0)
**/
    public void Var029()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setBigDecimal(1, new BigDecimal("-1112"));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT NUMERIC(10,5)
**/
    public void Var030()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setBigDecimal(1, new BigDecimal(19.98765));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT CHAR
**/
    public void Var031()
    {
	
	try{
	   

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setString(1,"3");
	    cstmt.execute();

	    byte [] check = cstmt.getBytes(1);
            byte [] expected = new byte[] {(byte)3};                            //@B1A
            if(isToolboxDriver())                      //@B1A
                assertCondition(areEqual(check, expected));                      //@B1A
            else                                                                //@B1A
                failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
            if(isToolboxDriver())                      //@B1A
                failed(e, "Unexpected Exception");                              //@B1A
            else
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT CHAR(50)
**/
    public void Var032()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setString(1,"43262243");
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT VARCHAR(50)
**/
    public void Var033()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setString(1,"50");
	    cstmt.execute();

	    byte [] check = cstmt.getBytes(1);                  //@B1C
            byte [] expected = new byte[] {(byte)80};                            //@B1A
            if(isToolboxDriver())                      //@B1A
                assertCondition(areEqual(check, expected));                      //@B1A
            else                                                                //@B1A
                failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
            if(isToolboxDriver())                      //@B1A
                failed(e, "Unexpected Exception");                              //@B1A
            else
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT BINARY(20)
**/
    public void Var034()
    {
	if (true) {
	    try{


		byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 43,
		(byte) 12, (byte) 23, (byte) 37, (byte) 89, (byte) 43,
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 43,
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 43};

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
		cstmt.setBytes(1,b);
		cstmt.execute();

		byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		check = cstmt.getBytes(1);
		boolean y = areEqual(b,check);
		assertCondition(y , "check = "+DumpBytes(check)+" b = "+DumpBytes(b));
	    }
	    catch (SQLException e){
		failed("Unexpected Exception");
	    }
	} 
    }
	    
	
    
/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT VARBINARY(20)
**/
    public void Var035()
    {
	if (true) {
	    try{


		byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
		cstmt.setBytes(1,b);
		cstmt.execute();

		byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		check = cstmt.getBytes(1);
		assertCondition(areEqual(check,b));
	    }
	    catch (SQLException e){
		failed("Unexpected Exception");
	    }
	} 
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT DATE
**/
    public void Var036()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setDate(1, new Date (33));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT TIME
**/
    public void Var037()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setTime(1,new Time (22));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT TIMESTAMP
**/
    public void Var038()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setTimestamp(1, new Timestamp (444));
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT DATALINK
**/
    public void Var039()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setString(1,"http://www.sony.com/pix.html");
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT BLOB(200)
**/
    public void Var040()
    {
	if (true) {
	    try{

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		check = cstmt.getBytes(1);
		assertCondition(areEqual(check, b));         //@B2
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception");          //@B1A
	    }
	} 
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT CLOB(200)
**/
    public void Var041()
    {
	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
		check = cstmt.getBytes(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBytes() - getBytes on a type registered as VARBINARY, INOUT BIGINT
**/
    public void Var042()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    // Updated 12/22/2011
	    // Exception thrown by both drivers

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    check = cstmt.getBytes(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }
}
