///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetString2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetString2.java
//
// Classes:      JDCSGetString2.java
//
////////////////////////////////////////////////////////////////////////
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
Testcase JDCSGetString2.  This tests the following
method of the JDBC CallableStatement class:

     getString()

**/
public class JDCSGetString2
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetString2";
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
    public JDCSGetString2 (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,

                          String password)
    {
        super (systemObject, "JDCSGetString2",
               namesAndVars, runMode, fileOutputStream,
               password);
    }

/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    @Override
    protected void setup ()
        throws Exception
    {
        String url = baseURL_

            ;
        if(isToolboxDriver())
         { //@B1A
        	url = url + ";date format=iso";                     //@B1A
        }
        connection = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

    }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    @Override
    protected void cleanup ()
    throws Exception
    {
        connection.close ();
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT SMALLINT
**/
    public void Var001()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("30"));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT INTEGER
**/
    public void Var002()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setInt(1, 13);
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("23"));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT REAL
**/
    public void Var003()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setFloat(1, 20.34f);
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("30.34"));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT FLOAT
**/

// since java can not store a float to exact precision we allow this minor precision loss

    public void Var004()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setDouble(1, 123.456);
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("133.45600000000002"), "check = "+check+" SB 133.45600000000002");

	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT DOUBLE
**/
    public void Var005()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setDouble(1, 120.312);
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("130.312"));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT DECIMAL
**/
    public void Var006()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setBigDecimal(1,new BigDecimal ("12435"));
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("12445"));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setBigDecimal(1, new BigDecimal("-94732.12345"));
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("-94722.12345"), "check = "+check+" SB -92722.12345");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setBigDecimal(1, new BigDecimal("-1112"));
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("-1102"));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getString() - getString on a type registered as VARCHAR, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setBigDecimal(1, new BigDecimal("19.98765"));
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("29.98765"), "check = "+check+" SB 29.98765");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT CHAR
**/
    public void Var010()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR1");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR1 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setString(1,"Z");
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("Z"), "output (\""+check+"\") != \"z\"");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT CHAR(50)
**/
    public void Var011()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setString(1,"TestString");
	    cstmt.execute();

	    String check = cstmt.getString(1);
	 assertCondition(check.equalsIgnoreCase("TestString                                        "));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT VARCHAR(50)
**/
    public void Var012()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setString(1,"Test VarChar 50");
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("Test VarChar 50"));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getString() - getString on a type registered as VARCHAR, INOUT BINARY(20)
**/
    public void Var013()
    {
	
	    try{


		byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 87,
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 87,
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 87,
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 87 };

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
		cstmt.setBytes(1,b);
		cstmt.execute();

		String check = cstmt.getString(1);
		byte[] test = check.getBytes();
                if(isToolboxDriver()) { //@B1A
                	assertCondition(areEqual(b, test, true));       //@B1A
                } else { //@B1A
                	assertCondition(areEqual(test, b));
                }
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception");
	    }
	}
    



/**
getString() - getString on a type registered as VARCHAR, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	
	    try{

		byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
		cstmt.setBytes(1,b);
		cstmt.execute();

		String check = cstmt.getString(1);
		byte[] test = check.getBytes();
                if(isToolboxDriver()) { //@B1A
                	assertCondition(areEqual(b, test, true));           //@B1A
                } else { //@B1A
                	assertCondition(areEqual(test, b));
                }
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception");
	    }
	}
    

/**
getString() - getString on a type registered as VARCHAR, INOUT DATE
**/
    public void Var015()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setDate(1, Date.valueOf("1910-11-23"));
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("1910-11-23"), "got "+check+" sb 1910-11-23");
	}
	catch (SQLException e){
	    failed(e, "Unexpected Exception");
	}
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT TIME
**/
    public void Var016()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setTime(1,Time.valueOf ("22:33:44"));
	    cstmt.execute();

	    String check = cstmt.getString(1);
            if(isToolboxDriver()) { //@B1A
            	assertCondition(check.equalsIgnoreCase("22:33:44"), "check = " + check + " SB 22:33:44");   //@B1A
            } else { //@B1A
            	assertCondition(check.equalsIgnoreCase("22.33.44"), "check = " + check + " SB 22.33.44");
            }
	}
	catch (SQLException e){
	    failed(e, "Unexpected Exception");
	}
    }

/**
getString() - getString on a type registered as VARCHAR, INOUT TIMESTAMP
**/
    public void Var017()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                Timestamp t = new Timestamp(444);
		cstmt.setTimestamp(1, t);
		cstmt.execute();

		String check = cstmt.getString(1);
		String expected = t.toString() + "000";
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) { /* Native returns timestamp in OS/400 format */
		    expected = expected.replace(' ','-');
		    expected = expected.replace(':','.');
		}
		assertCondition(check.equalsIgnoreCase(expected), "check"+check+" sb "+expected);

	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    

/**
getString() - getString on a type registered as VARCHAR, INOUT DATALINK
**/
// @B2 this variation should fail for native as well..  its a crappy test ... for now we will expect failure
    public void Var018()
    {
        //Note:  It is illegal to register a DATALINK as an INOUT parameter according to the DB2 SQL Reference  //@B1A
	if (true && !(isToolboxDriver()) ) {   //@B1C
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
		cstmt.setString(1,"http://www.ibm.com/us/");
		cstmt.execute();

		String check = cstmt.getString(1);

		failed ("Didn't throw SQLException"+check);                                   /*@B2*/
		// assertCondition(check.equalsIgnoreCase("http://www.ibm.com/us/"));   /*@B2*/
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");               /*@B2*/
	    }
	} else {
	    notApplicable();
	}
    }


/**
getString() - getString on a type registered as VARCHAR, INOUT BLOB(200)
**/
    public void Var019()
    {
	
	    try{

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		String check = cstmt.getString(1);
                String expected = "17415758";                   //@B1A      Each individual byte in HEX
		if(getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    expected = "\u0017\u0041\u0057\u0058";
		}
                    assertCondition(check.equals(expected), "got "+check+" sb "+expected+ " updated 11/11/2011 for native JDBC 4.1");    //@B1A
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception Updated 11/11/2011 for native JDBC 4.1");          //@B1A
	    }
	}
    

/**
getString() - getString on a type registered as VARCHAR, INOUT CLOB(200)
**/
    public void Var020()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		String check = cstmt.getString(1);
                if(isToolboxDriver()) { //@B1A
                	assertCondition(check.equals("Test Clob"), "check = " + check + " SB Test Clob");   //@B1A
                } else { //@B1A
                	// failed ("Didn't throw SQLException");  // @B2A  This should be expected to work
                	assertCondition(check.equals("Test Clob"), "check = "+check+" SB Test Clob");
                }
	    }
	    catch (SQLException e){
                if(isToolboxDriver()) { //@B1A
                	failed(e, "Unexpected Exception");                  //@B1A
                } else { //@B1A
                	assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
	    }
	}
    


/**
getString() - getString on a type registered as VARCHAR, INOUT BIGINT
**/
    public void Var021()
    {

	try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    String check = cstmt.getString(1);
	    assertCondition(check.equalsIgnoreCase("30"));
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
   }

// Register the output parameter as a CHAR

/**
getString() - getString on a type registered  as char, INOUT SMALLINT
**/
    public void Var022()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setInt(1, 20);
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("30"), "string is "+check+" but should be 30");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    

/**
getString() - getString on a type registered  as char, INOUT INTEGER
**/
    public void Var023()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setInt(1, 13);
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("23"), "string is "+check+" but should be 23");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    

/**
getString() - getString on a type registered  as char, INOUT REAL
**/
    public void Var024()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setFloat(1, 20.34f);
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("30.34"), "string is "+check+" but should be 30.34");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    

/**
getString() - getString on a type registered  as char, INOUT FLOAT
**/
    public void Var025()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setDouble(1, 123.456);
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("133.45600000000002"), "check = "+check+" SB 133.45600000000002");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
	}
    

/**
getString() - getString on a type registered  as char, INOUT DOUBLE
**/
    public void Var026()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setDouble(1, 120.312);
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("130.312"));
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
    }

/**
getString() - getString on a type registered  as char, INOUT DECIMAL
**/
    public void Var027()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setBigDecimal(1,new BigDecimal ("12435"));
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("12445"));
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
    }

/**
getString() - getString on a type registered  as char, INOUT DECIMAL(10,5)
**/
    public void Var028()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setBigDecimal(1, new BigDecimal("-94732.12345"));
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("-94722.12345"), "check = "+check+" SB -92722.12345");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
    }

/**
getString() - getString on a type registered  as char, INOUT NUMERIC(5,0)
**/
    public void Var029()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setBigDecimal(1, new BigDecimal("-1112"));
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("-1102"));
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
    }


/**
getString() - getString on a type registered  as char, INOUT NUMERIC(10,5)
**/
    public void Var030()
    {
	
	    try{
		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setBigDecimal(1, new BigDecimal("19.98765"));
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("29.98765"), "check = "+check+" SB 29.98765");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
    }

/**
getString() - getString on a type registered  as char, INOUT CHAR
**/
    public void Var031()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR1");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR1 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setString(1,"W");
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("W"), "check =  "+check+" SB W");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
    }

/**
getString() - getString on a type registered  as char, INOUT CHAR(50)
**/
    public void Var032()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setString(1,"TestString");
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("TestString                                        "), "check = " +check+ " and SB TestString -- w/50 chars (char 50)");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
    }

/**
getString() - getString on a type registered  as char, INOUT VARCHAR(50)
**/
    public void Var033()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setString(1,"Test VarChar 50");
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("Test VarChar 50"), "check = "+check+ " and SB Test VarChar 50");
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
    }


/**
getString() - getString on a type registered  as char, INOUT BINARY(20)
**/
    public void Var034()
    {
	
	    try{

		byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 87,
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 87,
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 87,
		(byte) 12, (byte) 23, (byte) 45, (byte) 89, (byte) 87 };

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setBytes(1,b);
		cstmt.execute();

		String check = cstmt.getString(1);
		byte[] test = check.getBytes();
                if(isToolboxDriver()) { //@B1A
                	assertCondition(areEqual(b, test, true));           //@B1A
                } else { //@B1A
                	assertCondition(areEqual(test, b));
                }
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception");
	    }
    }



/**
getString() - getString on a type registered  as char, INOUT VARBINARY(20)
**/
    public void Var035()
    {
	
	    try{

		byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20(?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setBytes(1,b);
		cstmt.execute();

		String check = cstmt.getString(1);
		byte[] test = check.getBytes();
                if(isToolboxDriver()) { //@B1A
                	assertCondition(areEqual(b, test, true));           //@B1A
                } else { //@B1A
                	assertCondition(areEqual(test, b));
                }
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception");
	    }
    }

/**
getString() - getString on a type registered  as char, INOUT DATE
**/
    public void Var036()
    {
	
	    try{

	    JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setDate(1, Date.valueOf("1910-11-23"));
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("1910-11-23"), "got "+check+" sb 1910-11-23");
	    }
	    catch (SQLException e){
		failed(e, "Unexpected Exception");
	    }
    }

/**
getString() - getString on a type registered  as char, INOUT TIME
**/
    public void Var037()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setTime(1,Time.valueOf ("22:33:44"));
		cstmt.execute();

		String check = cstmt.getString(1);
                if(isToolboxDriver()) { //@B1A
                	assertCondition(check.equalsIgnoreCase("22:33:44"), "check = " + check + " SB 22:33:44");   //@B1A
                } else { //@B1A
                	assertCondition(check.equalsIgnoreCase("22.33.44"));
                }
	    }
	    catch (SQLException e){
		failed(e,"Unexpected Exception");
	    }
    }

/**
getString() - getString on a type registered  as char, INOUT TIMESTAMP
**/
    public void Var038()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
                Timestamp t = new Timestamp(444);
		cstmt.setTimestamp(1, t);
		cstmt.execute();

		String check = cstmt.getString(1);
		String expected = t.toString() + "000";
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) { /* Native returns TIMESTAMP in OS/400 format */
		    expected = expected.replace(' ','-');
		    expected = expected.replace(':','.');
		}

		assertCondition(check.equalsIgnoreCase(expected),"check"+check+" sb "+expected);
	    }
	    catch (SQLException e){
		failed (e,"Unexpected Exception");
	    }
    }

/**
getString() - getString on a type registered  as char, INOUT DATALINK
**/
    public void Var039()
    {
        //note:  It is illegal to register a Datalink as an INOUT parameter according to the DB2 SQL Reference      //@B1A
	if ( !(isToolboxDriver())) {	    //@B1C
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setString(1,"http://www.ibm.com/us/");
		cstmt.execute();

		String check = cstmt.getString(1);

		failed ("Didn't throw SQLException"+check);                                   /*@B2*/
		// assertCondition(check.equalsIgnoreCase("http://www.ibm.com/us/"));   /*@B2*/
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");               /*@B2*/
	    }
	} else {
	  notApplicable("native testcase"); 
	}
    }


/**
getString() - getString on a type registered  as char, INOUT BLOB(200)
**/
    public void Var040()
    {
	
	    try{

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};
		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		String check = cstmt.getString(1);
                String expected = "17415758";                   //@B1A      Each individual byte in HEX
		if(getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    expected = "\u0017\u0041\u0057\u0058";
		}
		assertCondition(check.equals(expected), "got "+check+" sb "+expected+ " updated 11/11/2011 for native JDBC 4.1"       );    //@B1A

	    }
	    catch (SQLException e){
                    failed(e, "Unexpected Exception updated 11/11/2011 for native JDBC 4.1");          //@B1A
	    }
    }

/**
getString() - getString on a type registered  as char, INOUT CLOB(200)
**/
    public void Var041()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		String check = cstmt.getString(1);
                if(isToolboxDriver()) { //@B1A
                	assertCondition(check.equals("Test Clob"), "check = " + check + " SB Test Clob");   //@B1A
                } else { //@B1A
                	// failed ("Didn't throw SQLException"); this should work
                	assertCondition(check.equals("Test Clob"), "check = " + check + " SB Test Clob");
                }
	    }
	    catch (SQLException e){
                if(isToolboxDriver()) { //@B1A
                	failed(e, "Unexpected Exception");                  //@B1A
                } else { //@B1A
                	assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
	    }
    }


/**
getString() - getString on a type registered  as char, INOUT BIGINT
**/
    public void Var042()
    {
	
	    try{

		JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		cstmt.setInt(1, 20);
		cstmt.execute();

		String check = cstmt.getString(1);
		assertCondition(check.equalsIgnoreCase("30"));
	    }
	    catch (SQLException e){
		failed (e, "Unexpected Exception");
	    }
    }

}
