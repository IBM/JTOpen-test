///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetDate2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetDate2.java
//
// Classes:      JDCSGetDate2.java
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDLobTest;
import test.JDTestcase;
import test.JDLobTest.JDTestBlob;
import test.JDLobTest.JDTestClob;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;


/**
Testcase JDCSGetDate2.  This tests the following
method of the JDBC CallableStatement class:

     getDate()

**/
public class JDCSGetDate2
extends JDTestcase
{

    // Private data.
    private Connection          connection;
 
/**
Constructor.
**/
    public JDCSGetDate2 (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetDate2",
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
getDate() - getDate on a type registered as Date, INOUT SMALLINT
**/
    public void Var001()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10SMINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10SMINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT INTEGER
**/
    public void Var002()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10INT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10INT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setInt(1, 13);
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT REAL
**/
    public void Var003()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10REAL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10REAL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setFloat(1, 20.34f);
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT FLOAT
**/
    public void Var004()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10FLOAT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10FLOAT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setDouble(1, 123.456);
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT DOUBLE
**/
    public void Var005()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DOUBLE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DOUBLE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setDouble(1, 120.312);
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT DECIMAL
**/
    public void Var006()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setBigDecimal(1,new BigDecimal ("12435"));
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT DECIMAL(10,5)
**/
    public void Var007()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10DEC105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10DEC105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setBigDecimal(1, new BigDecimal(-94732.12345));
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT NUMERIC(5,0)
**/
    public void Var008()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setBigDecimal(1, new BigDecimal("-1112"));
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getDate() - getDate on a type registered as Date, INOUT NUMERIC(10,5)
**/
    public void Var009()
    {
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10NUM105");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10NUM105 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setBigDecimal(1, new BigDecimal(19.98765));
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT CHAR
**/
    public void Var010()
    {
	
	try{
	         
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR1");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR1 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setString(1,"Z");
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT CHAR(50)
**/
    public void Var011()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setString(1,"1956-02-21");
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    assertCondition((check.toString()).equalsIgnoreCase("1956-02-21"),"check = "+check.toString()+" and SB 1956-02-21");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT VARCHAR(50)
**/
    public void Var012()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVARCHAR50");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVARCHAR50 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setString(1,"1834-02-24");
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    assertCondition((check.toString()).equalsIgnoreCase("1834-02-24"),"check = "+check.toString()+" and SB 1834-02-24");
	}
	catch (SQLException e){
	    failed (e, "Unexpected Exception");
	}
    }


/**
getDate() - getDate on a type registered as Date, INOUT BINARY(20)
**/
    public void Var013()
    {
	
	try{
	   

	    byte[] b = { (byte) 12, (byte) 23, (byte) 45, (byte) 89};

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }
	    
	
    
/**
getDate() - getDate on a type registered as Date, INOUT VARBINARY(20)
**/
    public void Var014()
    {
	
	try{
	   
	    byte [] b = { (byte) 23, (byte) 45, (byte) 48, (byte) 88};
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNVBIN20");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNVBIN20 (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setBytes(1,b);
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT DATE
**/
    public void Var015()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDATE");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDATE (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setDate(1, Date.valueOf("1910-11-23"));
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    assertCondition((check.toString()).equalsIgnoreCase("1910-11-23"));
	}
	catch (SQLException e){
	    failed("Unexpected Exception");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT TIME
**/
    public void Var016()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTIME");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTIME (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setTime(1,new Time (22));
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");;
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT TIMESTAMP
**/
    public void Var017()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNTS");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNTS (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
            Timestamp t = new Timestamp(444);
	    cstmt.setTimestamp(1, t);
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    assertCondition(check.getYear() == t.getYear() && check.getDay() == t.getDay() && check.getMonth() == t.getMonth(), "Got check="+check.toString()+" expected="+t.toString()); 

	}
	catch (SQLException e){
	    failed ("Unexpected Exception");
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT DATALINK
**/
    public void Var018()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNDL");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNDL (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setString(1,"http://www.sony.com/pix.html");
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getDate() - getDate on a type registered as Date, INOUT BLOB(200)
**/
    public void Var019()
    {
	if (checkJdbc20()) { 	
	    try{

		byte [] b = {(byte) 23, (byte) 65, (byte) 87, (byte) 88};

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNBLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNBLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATE);
		cstmt.setBlob(1,new JDLobTest.JDTestBlob(b));
		cstmt.execute();

		Date check = cstmt.getDate(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getDate() - getDate on a type registered as Date, INOUT CLOB(200)
**/
    public void Var020()
    {
	if (checkJdbc20()) { 	
	    try{

          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"RETURNCLOB200");
		CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".RETURNCLOB200 (?)}");
		cstmt.registerOutParameter(1, java.sql.Types.DATE);
		cstmt.setClob(1,new JDLobTest.JDTestClob("Test Clob"));
		cstmt.execute();

		Date check = cstmt.getDate(1);
		failed ("Didn't throw SQLException "+check);
	    }
	    catch (SQLException e){
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getDate() - getDate on a type registered as Date, INOUT BIGINT
**/
    public void Var021()
    {
	
	try{
	   
          JDCSTest.assureProcedureExists(connection, JDCSTest.COLLECTION,"ADD10BINT");
	    CallableStatement cstmt = connection.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10BINT (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.setInt(1, 20);
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    failed("Didn't throw SQLException "+check);
	}
	catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getDate() - getDate on a type registered as Date, Date format iso, output parameter
**/
    public void Var022()
    {
	String added = " -- added by native driver 5/29/2009 to test date format iso"; 
	try{

	    String url = baseURL_
	      
	      
	      + ";date format=iso";  
	    Connection connection2 = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);	
	    String jdbcExpectedDate = "1910-11-23"; 

	    String procedure = "SETDATE22"; 

	    String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+"."+procedure+
	      " (OUT B DATE) LANGUAGE SQL " +
	      "SPECIFIC "+procedure+" JDCSVAR22: BEGIN DECLARE DUMMY DATE; "+
	      "SET DUMMY='1910-11-23'; SET B = DUMMY ; END JDCSVAR22" ;

	    Statement stmt = connection2.createStatement ();
	    try {
		stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procedure);
	    } catch (Exception e) {}    	    
	    stmt.executeUpdate(sql);
	    CallableStatement cstmt = connection2.prepareCall("{call "+JDCSTest.COLLECTION+"."+procedure+" (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.DATE);
	    cstmt.execute();

	    Date check = cstmt.getDate(1);
	    stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+"."+procedure);

            connection2.close(); 
	    assertCondition((check.toString()).equalsIgnoreCase(jdbcExpectedDate), "got "+check.toString()+" expected "+jdbcExpectedDate+added);
	}
	catch (Exception e){
	    failed("Unexpected Exception");
	}
    }

    
    /**
   getDate() - getDate on a type registered as Date, Date format julian, output parameter
   **/
  public void Var023() {

      if(checkRelease710()) { 
	  String added = " -- added by native driver 5/29/2009 to test date format julian";
	  try {

	      String url = baseURL_ +  ";date format=julian;errors=full";
	      Connection connection2 = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
	      String jdbcExpectedDate = "1990-11-23";

	      String procedure = "SETDATE23";

	      String sql = "CREATE PROCEDURE " + JDCSTest.COLLECTION + "." + procedure
		+ " (OUT B DATE) LANGUAGE SQL " + "SPECIFIC " + procedure
		+ " SET OPTION DATFMT=*JUL  JDCSVAR23: BEGIN DECLARE DUMMY DATE; "
		+ "SET DUMMY='90/328'; SET B = DUMMY ; END JDCSVAR23";

	      Statement stmt = connection2.createStatement();
	      try {
		  stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION + "."
				     + procedure);
	      } catch (Exception e) {
	      }
	      stmt.executeUpdate(sql);
	      CallableStatement cstmt = connection2.prepareCall("{call "
								+ JDCSTest.COLLECTION + "." + procedure + " (?)}");
	      cstmt.registerOutParameter(1, java.sql.Types.DATE);
	      cstmt.execute();

	      Date check = cstmt.getDate(1);
	      stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION + "."
				 + procedure);
	      connection2.close();
	      assertCondition((check.toString()).equalsIgnoreCase(jdbcExpectedDate),
			      "got " + check.toString() + " expected " + jdbcExpectedDate + added);
	  } catch (Exception e) {
	      e.printStackTrace();
	      failed("Unexpected Exception");
	  }
      }
  }

  /**
   getDate() - getDate on a type registered as Date, Date format mdy, output parameter
   **/
  public void Var024() {

 
	  String added = " -- added by native driver 5/29/2009 to test date format mdy";
	  try {

	      String url = baseURL_ +  ";date format=mdy;errors=full";
	      Connection connection2 = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
	      String jdbcExpectedDate = "1987-10-12";

	      String procedure = "SETDATE24";

	      String sql = "CREATE PROCEDURE " + JDCSTest.COLLECTION + "." + procedure
		+ " (OUT B DATE) LANGUAGE SQL " + "SPECIFIC " + procedure
		+ "   JDCSVAR24: BEGIN DECLARE DUMMY DATE; "
		+ "SET DUMMY='10/12/87'; SET B = DUMMY ; END JDCSVAR24";

	      Statement stmt = connection2.createStatement();
	      try {
		  stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION + "."
				     + procedure);
	      } catch (Exception e) {
	      }
	      stmt.executeUpdate(sql);
	      CallableStatement cstmt = connection2.prepareCall("{call "
								+ JDCSTest.COLLECTION + "." + procedure + " (?)}");
	      cstmt.registerOutParameter(1, java.sql.Types.DATE);
	      cstmt.execute();

	      Date check = cstmt.getDate(1);
	      stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION + "."
				 + procedure);
	      connection2.close();
	      assertCondition((check.toString()).equalsIgnoreCase(jdbcExpectedDate),
			      "got " + check.toString() + " expected " + jdbcExpectedDate + added);
	  } catch (Exception e) {
	      e.printStackTrace();
	      failed("Unexpected Exception");
	  }

  }

  /**
   getDate() - getDate on a type registered as Date, Date format dmy, output parameter
   **/
  public void Var025() {

 
	  String added = " -- added by native driver 5/29/2009 to test date format dmy";
	  try {

	      String url = baseURL_  + ";date format=dmy;errors=full";
	      Connection connection2 = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
	      String jdbcExpectedDate = "1987-10-12";

	      String procedure = "SETDATE25";

	      String sql = "CREATE PROCEDURE " + JDCSTest.COLLECTION + "." + procedure
		+ " (OUT B DATE) LANGUAGE SQL " + "SPECIFIC " + procedure
		+ "   JDCSVAR25: BEGIN DECLARE DUMMY DATE; "
		+ "SET DUMMY='12/10/87'; SET B = DUMMY ; END JDCSVAR25";

	      Statement stmt = connection2.createStatement();
	      try {
		  stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION + "."
				     + procedure);
	      } catch (Exception e) {
	      }
	      stmt.executeUpdate(sql);
	      CallableStatement cstmt = connection2.prepareCall("{call "
								+ JDCSTest.COLLECTION + "." + procedure + " (?)}");
	      cstmt.registerOutParameter(1, java.sql.Types.DATE);
	      cstmt.execute();

	      Date check = cstmt.getDate(1);
	      stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION + "."
				 + procedure);
	      connection2.close();
	      assertCondition((check.toString()).equalsIgnoreCase(jdbcExpectedDate),
			      "got " + check.toString() + " expected " + jdbcExpectedDate + added);
	  } catch (Exception e) {
	      e.printStackTrace();
	      failed("Unexpected Exception");
	  }

  }

  /**
   getDate() - getDate on a type registered as Date, Date format ymd, output parameter
   **/
  public void Var026() {

 
	  String added = " -- added by native driver 5/29/2009 to test date format ymd";
	  try {

	      String url = baseURL_ + ";date format=ymd;errors=full";
	      Connection connection2 = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
	      String jdbcExpectedDate = "1987-10-12";

	      String procedure = "SETDATE22";

	      String sql = "CREATE PROCEDURE " + JDCSTest.COLLECTION + "." + procedure
		+ " (OUT B DATE) LANGUAGE SQL " + "SPECIFIC " + procedure
		+ "   JDCSVAR22: BEGIN DECLARE DUMMY DATE; "
		+ "SET DUMMY='87/10/12'; SET B = DUMMY ; END JDCSVAR22";

	      Statement stmt = connection2.createStatement();
	      try {
		  stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION + "."
				     + procedure);
	      } catch (Exception e) {
	      }
	      stmt.executeUpdate(sql);
	      CallableStatement cstmt = connection2.prepareCall("{call "
								+ JDCSTest.COLLECTION + "." + procedure + " (?)}");
	      cstmt.registerOutParameter(1, java.sql.Types.DATE);
	      cstmt.execute();

	      Date check = cstmt.getDate(1);
	      stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION + "."
				 + procedure);
	      connection2.close();
	      assertCondition((check.toString()).equalsIgnoreCase(jdbcExpectedDate),
			      "got " + check.toString() + " expected " + jdbcExpectedDate + added);
	  } catch (Exception e) {
	      e.printStackTrace();
	      failed("Unexpected Exception");
	  }


  }
    
    
    
    


}
