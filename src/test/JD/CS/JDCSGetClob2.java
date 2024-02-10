///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetClob2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetClob2.java
//
// Classes:      JDCSGetClob2.java
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
import java.util.TimeZone;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;

import java.sql.Clob;


/**
Testcase JDCSGetClob2.  This tests the following
method of the JDBC CallableStatement class:

     getBlob()

**/
public class JDCSGetClob2
extends JDTestcase
{

    // Private data.
    private Connection          connection;

/**
Constructor.
**/
    public JDCSGetClob2 (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetClob2",
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
        if(isToolboxDriver())      //@B1A
            url = url + ";date format=iso";                 //@B1A
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
        return i.getSubString (1, (int) i.length ()).equals (b);            
    }

    /**
    Compares a Clob with a String.
    **/
        private boolean compare (Clob i, String b, StringBuffer sb)
            throws SQLException
        {
            String clobContents = i.getSubString (1, (int) i.length ()); 
            boolean passed = clobContents.equals(b);
            if (!passed) {sb.append("Got '"+clobContents+"' sb '"+b+"'\n"); } 
            return passed;             
        }



/**
getClob() - getClob on a type registered as CLOB, INOUT SMALLINT
**/
    public void Var001()
    {
	if (checkJdbc20()) { 
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setInt(1, 20);
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test"); 
		check = cstmt.getClob(1);
		// We can get a string from a small int, we should be able to also get a clob
		String expected = "30"; 
		assertCondition(compare(check,expected), "Got "+check+" sb "+expected+" Changed 11/17/2011"); 
	    }
	    catch (SQLException e){
                    failed(e, "Unexpected Exception  changed 11/17/2011");           
	    }
	}
    }
/**
getClob() - getClob on a type registered as Clob, INOUT INTEGER
**/
    public void Var002()
    {
	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setInt(1, 20);
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test"); 
		check = cstmt.getClob(1);
		String expected = "30"; 
		assertCondition(compare(check,expected), "Got "+check+" sb "+expected+" Changed 11/17/2011"); 
	    }
	    catch (SQLException e){
                    failed(e, "Unexpected Exception  changed 11/17/2011");           

	    }
	}
    }
/**
getClob() - getClob on a type registered as Clob, INOUT REAL
**/
    public void Var003()
    {
	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setFloat(1, 20.34f);
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		check = cstmt.getClob(1);
		String expected = "30.34"; 
		assertCondition(compare(check,expected), "Got "+check+" sb "+expected+" Changed 11/17/2011"); 


	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception  changed 11/17/2011");           
	    }
	}
    }
/**
getClob() - getClob on a type registered as Clob, INOUT FLOAT
**/
    public void Var004()
    {
	if (checkJdbc20()) { 	
	    try{
		StringBuffer sb = new StringBuffer(); 
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setDouble(1, 123.456);
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		check = cstmt.getClob(1);
		String expected = "133.45600000000002"; 
		assertCondition(compare(check,expected, sb), sb.toString()+" Changed 11/17/2011"); 

	    }
	    catch (SQLException e){
                    failed(e, "Unexpected Exception  changed 11/17/2011");           
	    }
	}
    }

/**
getClob() - getClob on a type registered as Clob, INOUT DOUBLE
**/
    public void Var005()
    {
	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setDouble(1, 120.312);
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		check = cstmt.getClob(1);

		String expected = "130.312"; 
		assertCondition(compare(check,expected), "Got "+check+" sb "+expected+" Changed 11/17/2011"); 
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception  changed 11/17/2011");           
	    }
	}
    }

/**
getClob() - getClob on a type registered as Clob, INOUT DECIMAL
**/
    public void Var006()  {
      String info = " -- Updated 1/3/2012 for native JDBC driver"; 
      if (checkJdbc20()) { 	
        try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setBigDecimal(1,new BigDecimal ("12435"));
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test"); 
		check = cstmt.getClob(1);
                     assertCondition(compare(check, "12445"), info);      
 	    }  catch (SQLException e) {
                     failed(e, "Unexpected Exception"+info);            
             
 	    }
 	    
      }
    }

/**
getClob() - getClob on a type registered as Clob, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	String info = " -- Updated 1/3/2012 for native JDBC driver"; 


	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setBigDecimal(1, new BigDecimal(-94732.12345));
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		check = cstmt.getClob(1);
		assertCondition(compare(check, "-94722.12344"), info );       //@B1A
	    }
	    catch (SQLException e){
                    failed(e, "Unexpected Exception" + info);              //@B1A
	    }
	}
    }
/**
getClob() - getClob on a type registered as Clob, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
 	String info = " -- Updated 1/3/2012 for native JDBC driver"; 


	if (checkJdbc20()) {
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setBigDecimal(1, new BigDecimal("-1112"));
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		check = cstmt.getClob(1);
                
                    assertCondition(compare(check, "-1102"), info);       //@B1A
                
	    }
	    catch (SQLException e){
                    failed(e, "Unexpected Exception"+info);              //@B1a
	    }
	}
    }

/**
getClob() - getClob on a type registered as Clob, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	String info = " -- Updated 1/3/2012 for native JDBC driver"; 

	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setBigDecimal(1, new BigDecimal(19.98765));
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		check = cstmt.getClob(1);
                    assertCondition(compare(check, "29.98764"), info);       //@B1A
	    }
	    catch (SQLException e){
                    failed(e, "Unexpected Exception"+info);              //@B1a
	    }
	}
    }
/**
getClob() - getClob on a type registered as Clob, INOUT CHAR
**/
    public void Var010()
    {
	String info = " -- Updated 1/3/2012 for native JDBC driver"; 

	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR(?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setString(1,"3");
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		check = cstmt.getClob(1);
                    assertCondition(compare(check, "3"), info);       //@B1A

	    }
	    catch (SQLException e){

                    failed(e, "Unexpected Exception"+info);              //@B1A
	    }
	}
    }

/**
getClob() - getClob on a type registered as Clob, INOUT CHAR(50)
**/
    public void Var011()
    {
	String info = " -- Updated 1/3/2012 for native JDBC driver"; 

	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setString(1,"43262243");
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test"); 
		check = cstmt.getClob(1);
                    assertCondition(compare(check, "43262243                                          "), info);       //@B1A
	    }
	    catch (SQLException e){
                    failed(e, "Unexpected Exception"+info);              //@B1A
	    }
	}
    }
/**
getClob() - getClob on a type registered as Clob, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	String info = " -- Updated 1/3/2012 for native JDBC driver"; 

	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setString(1,"50");
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		check = cstmt.getClob(1);
                    assertCondition(compare(check, "50"),info);       //@B1A
	    }
	    catch (SQLException e){
                    failed(e, "Unexpected Exception"+info);              //@B1A
	    }
	}
    }

/**
getClob() - getClob on a type registered as Clob, INOUT BINARY(20)
**/
    public void Var013()
    {
	StringBuffer info = new StringBuffer( " -- Updated 1/3/2012 for native JDBC driver"); 

	if (checkJdbc20()) {
	    if (getRelease() >= JDTestDriver.RELEASE_V5R3M0) { 	
		try{

		    byte [] p = {(byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35};
		    byte [] pToolbox = {(byte) 0x0c, (byte) 0x17, (byte) 0x2e, (byte) 0x59, (byte) 0x0a};


		    if(isToolboxDriver())  {
			p = pToolbox; 
		    }
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
		    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		    cstmt.setBytes(1,p);
		    cstmt.execute();

		    Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		    check = cstmt.getClob(1);
		    if(isToolboxDriver()) {      //@B1A
			String expected = "0C172E590A000000000000000000000000000000";
			assertCondition(compare(check, expected, info), "got "+check+" sb "+expected + info.toString());   //@B1A
		    } else   {
			String expected = "12345\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"; 
			assertCondition(compare(check, expected,info ), "got "+check+" sb "+expected + info.toString());
		    }
		}
		catch (SQLException e){
			failed(e, "Unexpected Exception"+info);              //@B1A
		}
	    }  else { 
		notApplicable("BINARY testcase for >= V5R3");
	    }
	}
    }
	
    
/**
getClob() - getClob on a type registered as Clob, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	StringBuffer info = new StringBuffer( " -- Updated 1/3/2012 for native JDBC driver"); 

	if (checkJdbc20()) {
	    if (getRelease() >= JDTestDriver.RELEASE_V5R3M0) { 	
		try{


		    byte [] p = {(byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35};
		    byte [] pToolbox = {(byte) 0x0c, (byte) 0x17, (byte) 0x2e, (byte) 0x59, (byte) 0x0a};


		    if(isToolboxDriver())  {
			p = pToolbox; 
		    }
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
		    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		    cstmt.setBytes(1,p);
		    cstmt.execute();

		    Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		    check = cstmt.getClob(1);
		    if(isToolboxDriver()) {     //@B1A
			String expected = "0C172E590A"; 
			assertCondition(compare(check, expected, info), "got "+check+" sb "+ expected + info);   //@B1A
		    } else            {
			String expected = "12345"; 
			assertCondition(compare(check, expected, info), "got "+check+" sb "+ expected + info);

		    }
		}
		catch (SQLException e){
		    failed(e, "Unexpected Exception");              //@B1A
		}
	    } else { 
		notApplicable("VARBINARY testcase for >= V5R3");
	    }
	}
    }

/**
getClob() - getClob on a type registered as Clob, INOUT DATE
**/
    public void Var015()
    {
	String info = " -- Updated 1/3/2012 for native JDBC driver"; 

	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
                Date d = new Date(3388248002L);
		cstmt.setDate(1, d);
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		check = cstmt.getClob(1);
                    assertCondition(compare(check, d.toString()), info);   //@B1A
	    }
	    catch (SQLException e){
                    failed(e, "Unexpected Exception"+info);              //@B1A
	    }
	}
    }

/**
getClob() - getClob on a type registered as Clob, INOUT TIME
**/
    public void Var016()
    {
	String info = " -- Updated 1/3/2012 for native JDBC driver"; 

	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setTime(1,Time.valueOf("22:33:44"));
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test"); 
		check = cstmt.getClob(1);
		String expected = "22:33:44"; 
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  expected = "22.33.44"; 
		}
		assertCondition(compare(check,expected), "Got "+check+" sb "+expected+info); 
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception "+info);           
	    }
	}
    }

/**
getClob() - getClob on a type registered as Clob, INOUT TIMESTAMP
**/
    public void Var017()
    {
	String info = " -- Updated 1/3/2012 for native JDBC driver"; 

	if (checkJdbc20()) { 	
	    try{

		StringBuffer sb = new StringBuffer(); 
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);


		long utcTime = 444 - 21600000;
		TimeZone currentTimeZone = TimeZone.getDefault(); 
		long offset = currentTimeZone.getOffset(utcTime);
		
		cstmt.setTimestamp(1, new Timestamp (utcTime - offset ));
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		check = cstmt.getClob(1);
		String expected = "1969-12-31 18:00:00.444000"; 
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		  expected = "1969-12-31-18.00.00.444000"; 
		}
		assertCondition(compare(check,expected, sb), sb.toString() + info); 
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception"+info);           
	    }
	}
    }

/**
getClob() - getClob on a type registered as Clob, INOUT DATALINK
**/
    public void Var018()
    {
	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setString(1,"http://www.sony.com/pix.html");
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test");
		check = cstmt.getClob(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getClob() - getClob on a type registered as Clob, INOUT BLOB(200)
**/
    public void Var019()
    {
	StringBuffer info = new StringBuffer( " -- Updated 1/3/2012 for native JDBC driver"); 

	if (checkJdbc20()) { 	
	    try{


    byte [] p = {(byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35};
    byte [] pToolbox = {(byte) 0x0c, (byte) 0x17, (byte) 0x2e, (byte) 0x59, (byte) 0x0a};


    if(isToolboxDriver())  {
  p = pToolbox; 
    }

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(p));
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test"); 
		check = cstmt.getClob(1);
		if(isToolboxDriver())  {    //@B1A  Each byte in HEX
		    String expected = "0C172E590A"; 
                    assertCondition(compare(check, expected, info), "got "+check+" sb "+expected +info);    //@B1A
		} else   {
                  String expected = "12345"; 
                  assertCondition(compare(check, expected,info ), "got "+check+" sb "+expected + info);
                    }
	    }
	    catch (SQLException e){
                    failed(e, "Unexpected Exception"+info);              //@B1A
	    }
	}
    }

/**
getClob() - getClob on a type registered as Clob, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Default"); 
		check = cstmt.getClob(1);
		assertCondition(compare (check, "Test Clob"));
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception");
	    }
	}
    }

/**
getClob() - getClob on a type registered as Clob, INOUT BIGINT
**/
    public void Var021()
    {
	String info = " -- Updated 1/3/2012 for native JDBC driver"; 

	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		cstmt.setInt(1, 42443);
		cstmt.execute();

		Clob check = new JDLobTest.JDTestClob("Test Clob Test"); 
		check = cstmt.getClob(1);
                    assertCondition(compare(check, "42453"), info);           //@B1A
	    }
	    catch (SQLException e){
                    failed(e, "Unexpected Exception"+info);                  //@B1A
	    }
	}
    }
}
