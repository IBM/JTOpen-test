///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetDouble2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetDouble2.java
//
// Classes:      JDCSGetDouble2.java
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
Testcase JDCSGetDouble2.  This tests the following
method of the JDBC CallableStatement class:

     getDouble()

**/
public class JDCSGetDouble2
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetDouble2";
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
    public JDCSGetDouble2 (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetDouble2",
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
getDouble() - getDouble on a type registered as Double, INOUT SMALLINT
**/
    public void Var001()
    {
	
	try{ 
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    assertCondition(check == 30.0);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT INTEGER
**/
    public void Var002()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setInt(1, 2320);
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    assertCondition(check == 2330.0);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT REAL
**/
    public void Var003()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setFloat(1, 20.34f);
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    assertCondition(check == 30.34000015258789, " check == "+check+" and SB 30.34000015258789");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT FLOAT
**/
    public void Var004()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setDouble(1, 123.456);
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    assertCondition(check == 133.45600000000002, " check == "+check+" and SB 133.45600000000002");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT DOUBLE
**/
    public void Var005()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setDouble(1, 120.312);
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    assertCondition(check == 130.312);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT DECIMAL
**/
    public void Var006()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setBigDecimal(1,new BigDecimal ("12435"));
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    assertCondition(check == 12445);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {

	 { 
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
		cstmt.setBigDecimal(1, new BigDecimal(-94732.1234));
		cstmt.execute();

		double check = 0.0;
		check = cstmt.getDouble(1);
                if(isToolboxDriver())
                    assertCondition(check == -94722.12339, " check == "+check+" and SB -94722.12339");      //account for BigDecimal.toString()
                else
                    assertCondition(check == -94722.12339, " check == "+check+" and SB -94722.1234");
	    }		// account for BigDecimal Constructor @ JDK doc: public BigDecimal(double val)
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	
	} 
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setBigDecimal(1, new BigDecimal("-1112"));
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    assertCondition(check == -1102.0);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getDouble() - getDouble on a type registered as Double, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
 { 
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
		cstmt.setBigDecimal(1, new BigDecimal(19.98764));
		cstmt.execute();

		double check = 0.0;
		check = cstmt.getDouble(1);
                if(isToolboxDriver())
                    assertCondition(check == 29.98763, " check == "+check+" and SB 29.98763"); //Account for BigDecimal.toString()
                else
                    assertCondition(check == 29.98763, " check == "+check+" and SB 29.98763"); //Account for BigDecimal() constructor error @ JDK doc - public BigDecimal(double val)
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	} 
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT CHAR
**/
    public void Var010()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setString(1,"3");
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    assertCondition(check == 3.0);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT CHAR(50)
**/
    public void Var011()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setString(1,"43262243");
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    assertCondition(check == 43262243);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setString(1,"50");
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    assertCondition(check == 50);
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getDouble() - getDouble on a type registered as Double, INOUT BINARY(20)
**/
    public void Var013()
    {
	
	try{
	   
	    byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89};

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }
	    
	
    
/**
getDouble() - getDouble on a type registered as Double, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	
	try{
	   
	    byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT DATE
**/
    public void Var015()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setDate(1, new Date (33));
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT TIME
**/
    @SuppressWarnings("deprecation")
    public void Var016()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setTime(1,new Time (22, 33, 44));
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT TIMESTAMP
**/
    public void Var017()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setTimestamp(1, new Timestamp (444));
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT DATALINK
**/
    public void Var018()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setString(1,"http://www.sony.com/pix.html");
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    failed ("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getDouble() - getDouble on a type registered as Double, INOUT BLOB(200)
**/
    public void Var019()
    {
	if (checkJdbc20()) { 
	    try{

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		double check = 0.0;
		check = cstmt.getDouble(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		double check = 0.0;
		check = cstmt.getDouble(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getDouble() - getDouble on a type registered as Double, INOUT BIGINT
**/
    public void Var021()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
	    cstmt.setInt(1, 201432);
	    cstmt.execute();

	    double check = 0.0;
	    check = cstmt.getDouble(1);
	    assertCondition(check == 201442.0, " check == "+check+" and SB 201442.0");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }
}
