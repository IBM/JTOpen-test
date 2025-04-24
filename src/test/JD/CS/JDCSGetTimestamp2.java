///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetTimestamp2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetTimestamp2.java
//
// Classes:      JDCSGetTimestamp2.java
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
import test.JDTestDriver;
import test.JDTestcase;


/**
Testcase JDCSGetTimestamp2.  This tests the following
method of the JDBC CallableStatement class:

     getTimestamp()

**/
public class JDCSGetTimestamp2
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetTimestamp2";
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
    public JDCSGetTimestamp2 (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetTimestamp2",
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
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT SMALLINT
**/
    public void Var001()
    {
	
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10SMINT"); 
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT INTEGER
**/
    public void Var002()
    {
	
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10INT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setInt(1, 313);
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT REAL
**/
    public void Var003()
    {
	
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10REAL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setFloat(1, 20.34f);
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT FLOAT
**/
    public void Var004()
    {
	
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10FLOAT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setDouble(1, 123.456);
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT DOUBLE
**/
    public void Var005()
    {
	
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10DOUBLE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setDouble(1, 120.312);
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT DECIMAL
**/
    public void Var006()
    {
	
	try{
	   
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10DEC50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setBigDecimal(1,new BigDecimal ("12435"));
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10DEC105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setBigDecimal(1, new BigDecimal(-94732.12345));
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	
	try{
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10NUM50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setBigDecimal(1, new BigDecimal("-1112"));
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10NUM105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setBigDecimal(1, new BigDecimal(19.98765));
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT CHAR
**/
    public void Var010()
    {
	
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNCHAR1");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR1 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setString(1,"3");
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT CHAR(50)
**/
    public void Var011()
    {
	
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNCHAR50"); 
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setString(1,"1970-02-20 12:02:34");
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    assertCondition((check.toString()).equalsIgnoreCase("1970-02-20 12:02:34.0"), " check = "+check.toString()+" and SB 1970-02-20 12:02:34.0");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNVARCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setString(1,"1987-02-04 12:12:12");
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    assertCondition((check.toString()).equalsIgnoreCase("1987-02-04 12:12:12.0"), "check = "+check+" and SB 1987-02-04 12:12:12.0");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT BINARY(20)
**/
    public void Var013()
    {
	
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89};
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }
	    
	
    
/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	
	try{
	    byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNVBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10VBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT DATE
**/
    public void Var015()
    {
	
	try{


	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setDate(1, Date.valueOf("1910-11-23"));
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    assertCondition((check.toString()).equalsIgnoreCase("1910-11-23 00:00:00.0"),  "check = "+check.toString()+" and SB 1910-11-23 00:00:00");
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT TIME
**/
    public void Var016()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) {	
	    try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNTIME");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
		cstmt.setTime(1, Time.valueOf ("22:33:44"));
		cstmt.execute();

		Timestamp check = cstmt.getTimestamp(1);
                //@C1D  if(isToolboxDriver())              //@B1A
                //@C1D      failed("Didn't throw SQLException "+check);                    //@B1A
                //@C1D  else                                                        //@B1A
                    assertCondition((check.toString()).equalsIgnoreCase("1970-01-01 22:33:44.0"),
				    check.toString()+" SB be 1970-01-01 22:33:44.0");
	    }
	    catch (SQLException e){
                //@C1D  if(isToolboxDriver())              //@B1A
                //@C1D      assertExceptionIsInstanceOf(e, "java.sql.SQLException");    //@B1A
                //@C1D  else                                                        //@B1A
		    failed(e, "Unexpected Exception");
	    }
	} else {
	    notApplicable(); 
	} 
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT TIMESTAMP
**/
    public void Var017()
    {
	
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNTS");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
            Timestamp t = new Timestamp(444);
	    cstmt.setTimestamp(1, t);
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    assertCondition(check.equals(t));             
	}
	catch (SQLException e){
	    failed ("Unexpected Exception");
	}
    }

/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT DATALINK
**/
    public void Var018()
    {
	
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNDL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setString(1,"http://www.sony.com/pix.html");
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT BLOB(200)
**/
    public void Var019()
    {
	if (checkJdbc20()) { 
	    try{

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};
		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		Timestamp check = cstmt.getTimestamp(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc20()) { 	
	    try{
		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		Timestamp check = cstmt.getTimestamp(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getTimestamp() - getTimestamp on a type registered as Timestamp, INOUT BIGINT
**/
    public void Var021()
    {
	
	try{
	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION, "ADD10BINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
	    cstmt.setInt(1, 2650);
	    cstmt.execute();

	    Timestamp check = cstmt.getTimestamp(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }
}
