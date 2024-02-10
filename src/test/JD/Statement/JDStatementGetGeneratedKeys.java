///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementGetGeneratedKeys.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementGetGeneratedKeys.java
//
// Classes:      JDStatementGetGeneratedKeys
//
////////////////////////////////////////////////////////////////////////
// NOTE:  problems with JCC driver (on 2/7/08)
// 1.  getGeneratedKeys sometimes retruns null (never should)
// 2.  getGeneratedKeys returns a rs with a value for var 12 (which took an exception).
// 3.  
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDStatementTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDStatementGetGeneratedKeys.  This tests the following methods
of the JDBC Statement class:

<ul>
<li>execute(...., RETURN_GENERATED_KEYS)
<li>executeUpdate(...., RETURN_GENERATED_KEYS)
<li>executeLargeUpdate(...., RETURN_GENERATED_KEYS)
<li>executeLargeUpdate(...., columnNames)
<li>executeLargeUpdate(...., columnIndices)
<li>getGeneratedKeys()</li>
</ul>

The following methods of the JDBC Connection class are also tested
<ul>
<li>prepare(...., RETURN_GENERATED_KEYS) 
</ul>
The following methods of the JDBC PreparedStatement class are also tested
<ul>
<li>execute(...)
<li>executeUpdate(...)
</ul> 
**/
public class JDStatementGetGeneratedKeys
extends JDTestcase {


    // Private data.
    private static  String table_        = JDStatementTest.COLLECTION + ".JDGENERATEDKEYS";
    private static  String systemTable_  = JDStatementTest.COLLECTION + "/JDGENERATEDKEYS";

    private static  String table2_       = JDStatementTest.COLLECTION + ".JDGENERATEDKEYS2";
    private static  String systemTable2_ = JDStatementTest.COLLECTION + "/JDGENERATEDKEYS2";

    private static  String table3_       = JDStatementTest.COLLECTION + ".JDSTMTGK3";
    private static  String systemTable3_ = JDStatementTest.COLLECTION + "/JDSTMTGK3";
    private static  String trigger3_     = JDStatementTest.COLLECTION + ".JDSTMTGT3";

    private static  String table4_       = JDStatementTest.COLLECTION + ".JDSTMTGK4";
    private static  String systemTable4_ = JDStatementTest.COLLECTION + "/JDSTMTGK4";
    private static  String trigger4_     = JDStatementTest.COLLECTION + ".JDSTMTGT4";
    private static  String table4a_      = JDStatementTest.COLLECTION + ".JDSTMTGK4A";
    private static  String trigger4a_    = JDStatementTest.COLLECTION + ".JDSTMTGT4A";

    private Connection      connection2_;
    private Connection      connectionSystemNaming_;


/**
Constructor.
**/
    public JDStatementGetGeneratedKeys (AS400 systemObject,
                                        Hashtable namesAndVars,
                                        int runMode,
                                        FileOutputStream fileOutputStream,
                                        
                                        String password)
    {
        super (systemObject, "JDStatementGetGeneratedKeys",
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
	String sql=""; 
	try { 
	//
	// reset table names to pick up new collection is specified later
	//

	    table_  = JDStatementTest.COLLECTION + ".JDGENERATEDKEYS";
	    systemTable_  = JDStatementTest.COLLECTION + "/JDGENERATEDKEYS";
	    table2_ = JDStatementTest.COLLECTION + ".JDGENERATEDKEYS2";
	    systemTable2_ = JDStatementTest.COLLECTION + "/JDGENERATEDKEYS2";

	    table3_       = JDStatementTest.COLLECTION + ".JDSTMTGK3";
	    systemTable3_ = JDStatementTest.COLLECTION + "/JDSTMTGK3";
	    trigger3_     = JDStatementTest.COLLECTION + ".JDSTMTGT3";

	    table4_       = JDStatementTest.COLLECTION + ".JDSTMTGK4";
	    systemTable4_ = JDStatementTest.COLLECTION + "/JDSTMTGK4";
	    table4a_      = JDStatementTest.COLLECTION + ".JDSTMTGK4A";
	    trigger4_     = JDStatementTest.COLLECTION + ".JDSTMTGT4";
	    trigger4a_    = JDStatementTest.COLLECTION + ".JDSTMTGT4A";



	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    // JCC doesn't have these properties

		connection_ = testDriver_.getConnection (baseURL_ , 
							 userId_, encryptedPassword_);

		connection2_ = testDriver_.getConnection (baseURL_ ,
							  userId_, encryptedPassword_);


	    } else { 

		connection_ = testDriver_.getConnection (baseURL_ + ";errors=full", 
							 userId_, encryptedPassword_);

		connection2_ = testDriver_.getConnection (baseURL_ + ";errors=full;reuse objects=false", 
							  userId_, encryptedPassword_);
	    }

	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
	  // jcc doesn't support system naming
		connectionSystemNaming_ = testDriver_.getConnection (baseURL_ , 
							  userId_, encryptedPassword_);
	    } else { 
		connectionSystemNaming_ = testDriver_.getConnection (baseURL_ + ";errors=full;naming=system", 
							  userId_, encryptedPassword_);
	    }


	    Statement s = connection_.createStatement ();

	    initTable(s,  table_, " (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY)");

	    if (JDTestDriver.isLUW()) {
	    // LUW requires AS IDENTITY .. Also ROWID not working
		initTable(s,  table2_, " (NAME VARCHAR(10), GENERATEID INT GENERATED ALWAYS AS IDENTITY)");
	    } else { 
		initTable(s,  table2_, " (NAME VARCHAR(10), GENERATEID ROWID GENERATED ALWAYS)");
	    }


	    sql = "INSERT INTO " + table2_
			     + " (NAME) VALUES ('terry')";
	    s.executeUpdate(sql); 


	    initTable(s, table3_,"(NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY, NAMECOMMENT VARCHAR(10) DEFAULT '')");

	    initTrigger(s, trigger3_," AFTER INSERT on "+table3_+" REFERENCING NEW ROW AS post " +
	    		"FOR EACH ROW MODE DB2ROW  " +
	    		"UPDATE "+table3_+" " +
	    				"SET NAMECOMMENT = 'NEW NAME' WHERE POST.GENID=GENID"); 

	    initTable(s, table4_,"(NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY, NAMECOMMENT VARCHAR(10)	DEFAULT '')");

	    initTable(s, table4a_,"(GENID2 INT, COMMENT VARCHAR(10))");

	    initTrigger(s, trigger4_," AFTER INSERT on "+table4_+" REFERENCING NEW ROW AS POST " +
	    		"FOR EACH ROW MODE DB2ROW INSERT INTO "+table4a_+" VALUES(GENID, 'NEWROW')");


	    initTrigger(s, trigger4a_,
	        " AFTER INSERT on "+table4a_+" REFERENCING NEW ROW AS POST " +
	        		"FOR EACH ROW MODE DB2ROW " +
	        		"UPDATE "+table4_+" SET NAMECOMMENT = POST.COMMENT WHERE GENID=POST.GENID2");
	    s.executeUpdate(sql); 


		sql="setup STP_RS0"; 
		JDSetupProcedure.create (systemObject_,connection_,
					 JDSetupProcedure.STP_RS0, supportedFeatures_, collection_);
		sql="setup STP_RS2"; 
		JDSetupProcedure.create (systemObject_,connection_,
					 JDSetupProcedure.STP_RS1, supportedFeatures_, collection_);
		sql="setup STP_RS3"; 
		JDSetupProcedure.create (systemObject_,connection_,
					 JDSetupProcedure.STP_RS3, supportedFeatures_, collection_);

	    if (getDriver() != JDTestDriver.DRIVER_JCC) {
		connection_.commit(); // for xa
	    }

	    s.close ();
	} catch (Exception e) {
	    System.out.println("ERROR in SETUP SQL="+sql);
	    e.printStackTrace(); 
	} 
    }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        Statement s = connection_.createStatement ();
        cleanupTable(s,  table_);
        cleanupTable(s,  table2_);
        cleanupTable(s,  table3_);
        cleanupTable(s,  table4a_);
        cleanupTable(s,  table4_);
        s.close ();

        cleanupConnections(); 
	System.gc(); 
    }

    public void cleanupConnections() throws SQLException {
      if (getDriver() != JDTestDriver.DRIVER_JCC) {
        connection_.commit(); // for xa
    }
          connection_.close ();

          connection2_.close ();
      
    }
    
/**
getGeneratedKeys() - If we are running on a release before v5r2, we should get a not 
function not supported exception.
**/
    public void Var001()
    {
        if (getRelease() >= JDTestDriver.RELEASE_V5R2M0 &&  getJdbcLevel() >= 2)
        {
            notApplicable("v5r1 or earlier variation");
            return;
        }
        try
        {
            PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + table_
                                                                + " (NAME) VALUES ('susan')", Statement.RETURN_GENERATED_KEYS);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            failed("Didn't throw SQLException "+rs);
        }
        catch (Throwable e)
        {
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
                //
                // native driver gets a no such method exception
                // 
                assertCondition(true); 

	    } else {
		if (e instanceof Exception) { 
		    assertExceptionIs((Exception) e, "SQLException", "The driver does not support this function.");
		} else {
		    e.printStackTrace();
		    failed("Unexpected throwable"); 
		} 
        }
    }
    }



/**
getGeneratedKeys() - If a prepared statement has not been executed, should get a
null result set returned from getGeneratedKeys().
**/
    public void Var002()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }

        try
        {
            PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + table_
                                                                + " (NAME) VALUES ('dave')");
            ResultSet rs = ps.getGeneratedKeys();
            assertCondition(rs == null);
            ps.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


/**
getGeneratedKeys() - If no prepared statements have been executed, should get a 
null result set from getGeneratedKeys().
**/
    public void Var003()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try
        {
            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + table_
                                                                 + " (NAME) VALUES ('chris')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = ps.getGeneratedKeys();
            assertCondition(rs == null);
            ps.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


/**
getGeneratedKeys() - Since we didn't specify we wanted generated keys when we prepared
this statement, getGeneratedKeys() should return null.
**/
    public void Var004()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }

        try {

            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + table_
                                                                 + " (NAME) VALUES ('jeff')");
            ps.execute ();
            ResultSet rs = ps.getGeneratedKeys();
            assertCondition(rs == null);
            ps.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


    /**
getGeneratedKeys() - Since we didn't specify we wanted generated keys when we prepared this
statement, getGeneratedKeys() should return null.
**/
    public void Var005()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {

            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + table_
                                                                 + " (NAME) VALUES ('jim')", Statement.NO_GENERATED_KEYS);
            ps.execute ();
            ResultSet rs = ps.getGeneratedKeys();
            assertCondition(rs== null);
            ps.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
getGeneratedKeys() - Make sure that an empty result set is returned from an empty table
(no rows).
**/
    public void Var006()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {
            Statement s = connection_.createStatement ();
            s.execute ("SELECT * FROM " + table_, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
	    if (rs == null) {
		assertCondition(false, "Error:  getGeneratedKeys returned null."); 
	    } else { 
		boolean check = rs.next();
		assertCondition(!check);
	    }
            s.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getGeneratedKeys() - Verify that a generated key is returned w/ execute and INSERT.
**/
    public void Var007()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('sue')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            String generatedKey = rs.getString(1);
            boolean check2 = rs.next();
            assertCondition(check && !check2 && generatedKey.length() >= 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally{
            try{
              if (s != null) s.close();
            }
            catch(Exception e){
            }
        }
    }


/**
getGeneratedKeys() - Verify that a generated key is returned w/ executeUpdate and INSERT.
**/
    public void Var008()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try
        {
            s = connection_.createStatement();
            int rowCount = s.executeUpdate ("INSERT INTO " + table_
                             + " (NAME) VALUES ('sally')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            String generatedKey = rs.getString(1);
            boolean check2 = rs.next();
            assertCondition(rowCount==1 && check && !check2 && generatedKey.length() >= 1,
			    "rowCount="+rowCount+" sb 1\n"+
			    "check="+check+" sb true\n"+
			    "check2="+check2+" sb false\n"+
			    "generatedKey.length()="+generatedKey.length()+"sb  >= 1");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception");
        }
        finally{
            try{
              if (s != null) s.close();
            }
            catch(Exception e){
            }
        }
    }


    /**
getGeneratedKeys() - Verify an empty result set is returned from getGeneratedKeys() as auto
generated keys aren't supported from executeQuery() statements.
**/
    public void Var009()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        PreparedStatement s = null;
        try {
            s = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT", Statement.RETURN_GENERATED_KEYS);
            s.executeQuery ();
            ResultSet rs = s.getGeneratedKeys();
	    if (rs == null) {
		assertCondition(false, "Error:  getGeneratedKeys returned null."); 
	    } else { 

		boolean check = rs.next();
		String info = null; 
		if (check) info = "rs.next returned "+check+" value is "+rs.getObject(1);
		assertCondition(!check, info  );
	    }
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally{
            try{
              if (s != null) s.close();
            }
            catch(Exception e){
            }
        }
    }




/**
getGeneratedKeys() - Verify that an exception is thrown if the user tries
to access generated keys once the statement is closed.
**/
    public void Var010()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try
        {
            Statement s = connection_.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                             + " (NAME) VALUES ('michelle')", 
                             Statement.RETURN_GENERATED_KEYS);
            s.close ();
            s.getGeneratedKeys();
            failed ("Didn't throw SQLException"); 
        }
        catch (SQLException e) //@A1
        {
	    assertClosedException(e, "");

        }
    }



/**
getGeneratedKeys() - getGeneratedKeys() returns an empty result set on an UPDATE because no
auto-generated key will be returned.
**/
    public void Var011()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try
        {
            Statement s = connection_.createStatement ();
            int rowCount = s.executeUpdate ("UPDATE " + table_
                             + " SET NAME='Molly' WHERE NAME = 'Janet'",  
                             Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
	    if (rs == null) {
		assertCondition(false, "Error:  getGeneratedKeys returned null."); 
	    } else { 

		boolean check = rs.next();
		assertCondition(rowCount==0 && !check,
				"rowCount="+rowCount+" sb 0\n"+
				"rs.next returned "+check+" instead of false" );
	    }
            s.close ();        
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getGeneratedKeys() - getGeneratedKeys() should return null if the user caught an exception.
**/
    public void Var012()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try
        {
            boolean exception = false;
            Statement s = connection_.createStatement ();
            try {
                //Statement is missing a paranthesis 
                s.execute ("INSERT INTO " + table_
                           + " NAME) VALUES ('michelle')", 
                           Statement.RETURN_GENERATED_KEYS);                
            }
            catch (SQLException sqe)
            {
                exception = true;
            }
            ResultSet rs = s.getGeneratedKeys();

	    // The JDBC doesn't say null is permitted but some driver do return it.
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		boolean rsNext = false;
		String rsGetString = ""; 
		if (rs != null) {
		    rsNext = rs.next();
		    if (rsNext) {
			rsGetString = rs.getString(1); 
		    } 
		} 
		assertCondition (exception && (rs == null || rsNext==false ),
                    "rs ="+rs+" exception="+exception+" rsNext="+rsNext+" rsGetString="+rsGetString);
	    } else if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		       getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
		assertCondition (exception && rs != null, "rs sb not-null but is ="+rs+" exception="+exception);

	    } else { 
		assertCondition (exception && rs == null, "rs sb null but is ="+rs+" exception="+exception);
	    }
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getGeneratedKeys() - Check that if the user calls getGeneratedKeys() on a CALL statement,
getGeneratedKeys() returns an empty result set as CALLs do not return generated keys.
**/
    public void Var013()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }

        try
        {
            PreparedStatement ps = connection_.prepareStatement ("CALL "
                                                                 + JDSetupProcedure.STP_RS0,
                                                                 Statement.RETURN_GENERATED_KEYS);
            ps.execute ();
            ResultSet rs = ps.getGeneratedKeys();
	    if (rs == null) {
		assertCondition(false, "Error:  getGeneratedKeys returned null."); 
	    } else { 
		boolean check = rs.next();
		assertCondition(!check, "rs.next returned true instead of false");
	    }
            ps.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getGeneratedKeys() - getGeneratedKeys() should return a result set on a prepared statement
and execute.
**/
    public void Var014()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        PreparedStatement ps = null;
        try {

            ps = connection_.prepareStatement ("INSERT INTO " + table_
                                                                 + " (NAME) VALUES ('Holly')", Statement.RETURN_GENERATED_KEYS);
            ps.execute ();
            ResultSet rs = ps.getGeneratedKeys();
            boolean nextWorked = rs.next();
            String key = rs.getString(1);
            boolean nextWorkedAgain = rs.next();
            assertCondition (key.length() >= 1 && nextWorked && !nextWorkedAgain);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally{
            try{
              if (ps != null) ps.close();
            }
            catch(Exception e){
            }
        }
    }


    /**
getGeneratedKeys() - getGeneratedKeys() should return a result set on a prepared statement
and executeUpdate.
**/
    public void Var015()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        PreparedStatement ps = null;
        try {

            ps = connection_.prepareStatement ("INSERT INTO " + table_
                                                                 + " (NAME) VALUES ('Jeffrey')", Statement.RETURN_GENERATED_KEYS);
            int updateCount = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            boolean nextWorked = rs.next();
            String key = rs.getString(1);
            boolean nextWorkedAgain = rs.next();
            assertCondition (updateCount==1 && key.length() >= 1 && nextWorked && !nextWorkedAgain,
			     "updateCount="+updateCount+" sb 1\n"+
			     "key.length()="+key.length()+" sb >= 1\n"+
			     "nextWorked="+nextWorked+" sb true\n"+
			     "nextWorkedAgain="+nextWorkedAgain+" sb false\n");

        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally{
            try{
              if (ps != null) ps.close();
            }
            catch(Exception e){
            }
        }
    }


    /**
getGeneratedKeys() - getGeneratedKeys() should an empty result set on a prepared statement
and executeQuery.
**/
    public void Var016()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {

            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT", Statement.RETURN_GENERATED_KEYS);
            ps.executeQuery ();
            ResultSet rs = ps.getGeneratedKeys();
	    if (rs == null) {
		assertCondition(false, "Error:  getGeneratedKeys returned null."); 
	    } else { 

		boolean check = rs.next();
		assertCondition(!check, "rs.next returned true instead of false");
	    }
            ps.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


    /**
getGeneratedKeys() - Verify that a generated key is returned w/ execute and DELETE.
**/
    public void Var017()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
	    //
	    // Make sure a unique entry for sue is there
	    //
	    try {
		s.executeUpdate("DELETE FROM "+table_+" WHERE NAME='sue'");
	    } catch (Exception e) {
		// ignore error 
	    } 
            s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES ('sue')"); 

	    //
	    // Now test the delete
	    //
            s.execute ("DELETE FROM " + table_
                       + " WHERE NAME='sue'", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
	    if (rs == null) {
		assertCondition(false, "Error:  getGeneratedKeys returned null."); 
	    } else { 
		boolean check = rs.next();
		String generatedKey = rs.getString(1);
		boolean check2 = rs.next();
		assertCondition(check && !check2 && generatedKey.length() >= 1);
	    }
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally{
            try{
              if (s != null) s.close();
            }
            catch(Exception e){
            }
        }
    }


    /**
getGeneratedKeys() - Verify that an empty result set is returned w/ execute and a DELETE
that doesn't match a row in the table.
**/
    public void Var018()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
            s.execute ("DELETE FROM " + table_
                       + " WHERE NAME='john'", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
	    if (rs == null) {
		assertCondition(false, "Error:  getGeneratedKeys returned null."); 
	    } else { 

		boolean check = rs.next();
		assertCondition(!check, "rs.next returned true instead of false");
	    }
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally{
            try{
              if (s != null) s.close();
            }
            catch(Exception e){
            }
        }
    }


    /**
getGeneratedKeys() - Since we requested no generated keys, getGeneratedKeys() should
return null.
**/
    public void Var019()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {
            Statement s = connection_.createStatement ();
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('ivy')", Statement.NO_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
		assertCondition (rs != null, "rs sb not-null but is ="+rs); 
	    } else { 
		assertCondition(rs == null);
	    }
            s.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


    /**
getGeneratedKeys() - Since we requested no generated keys, getGeneratedKeys() should
return null.
**/
    public void Var020()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {
            CallableStatement cs = connection_.prepareCall ("CALL " + JDSetupProcedure.STP_RS0);
            cs.execute ();
            ResultSet rs = cs.getGeneratedKeys();
            assertCondition(rs == null);
            cs.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


    /**
getGeneratedKeys() - CallableStatements never support getGeneratedKeys(), so it should
return null.
**/
    public void Var021()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {
            CallableStatement cs = connection_.prepareCall ("CALL " + JDSetupProcedure.STP_RS0);
            cs.execute ();
            ResultSet rs = cs.getGeneratedKeys();
            assertCondition(rs == null);
            cs.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getGeneratedKeys() - You shouldn't be able to call the new execute(String, int) methods on
a PreparedStatement.
**/
    public void Var022()
    {
	if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
	{
	    notApplicable("v5r2/JDK 1.4 variation");
	    return;
	}
	try {
	    PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + table_
								+ " (NAME) VALUES ('sarah')");
	    ps.execute();
	    ps.execute ("INSERT INTO " + table_
			+ " (NAME) VALUES ('polly')", Statement.RETURN_GENERATED_KEYS);
	    failed("Didn't throw SQLException");
	}
	//@A1
	  catch (SQLException e) {
	      if (isToolboxDriver()) {
		  assertExceptionIs(e, "SQLException", "Function sequence error.");
	      }	else if (getDriver () == JDTestDriver.DRIVER_JCC) {
		  assertCondition(e.getMessage().indexOf("cannot be called") >= 0, "Exception was "+e);

	      } else {
		  assertCondition(e.getMessage().indexOf("Statement interface execution methods can't be used through a PreparedStatement object.") >= 0, "Exception was "+e);
	      }
	  }
    }


    /**
getGeneratedKeys() - You shouldn't be able to call the new executeUpdate(String, int) methods on
a PreparedStatement.
**/
    public void Var023()
    {
	if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
	{
	    notApplicable("v5r2/JDK 1.4 variation");
	    return;
	}
	try {
	    PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + table_
								+ " (NAME) VALUES ('nick')");
	    ps.execute();
	    ps.executeUpdate ("INSERT INTO " + table_
			      + " (NAME) VALUES ('eric')", Statement.RETURN_GENERATED_KEYS);
	    failed("Didn't throw SQLException");
	}
	//@A1
	  catch (SQLException e) {
	      if (isToolboxDriver()) {
		  assertExceptionIs(e, "SQLException", "Function sequence error.");
	      }	else if (getDriver () == JDTestDriver.DRIVER_JCC) {
		  assertCondition(e.getMessage().indexOf("cannot be called") >= 0, "Exception was "+e);
	      }
	      else
	      {
		  assertCondition(e.getMessage().indexOf("Statement interface execution methods can't be used through a PreparedStatement object.") >= 0, "Exception is "+e);
	      }
	  }
    }


    /**
getGeneratedKeys() - Since we requested no generated keys, getGeneratedKeys() should
return null.
**/
    public void Var024()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {
            Statement s = connection_.createStatement ();
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('molly')");
            ResultSet rs = s.getGeneratedKeys();
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
		assertCondition (rs != null, "rs sb not-null but is ="+rs); 
	    } else { 
		assertCondition(rs == null);
	    }
            s.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


    /**
getGeneratedKeys() - Since table2 does not have an identity column column, getGeneratedKeys() 
should return an empty result set.
**/
    public void Var025()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s= null;
        try {
	    String generatedKey=""; 
            s = connection_.createStatement ();
            s.execute ("INSERT INTO " + table2_
                       + " (NAME) VALUES ('jackie')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
	    if (check) {
		generatedKey=rs.getString(1); 
	    } 
            if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
               // In V5R5 we can get generated information from non-identity columns
              assertCondition(check, "rs.next returned false instead of true");
            } else { 
               assertCondition(!check, "rs.next returned true instead of false and value is '"+generatedKey+"'");
            }
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally{
            try{
              if (s != null) s.close();
            }
            catch(Exception e){
            }
        }
    }


/**
getGeneratedKeys() - Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute will
return generated keys, but the second won't, so getGeneratedKeys() should return an
empty result set.
**/
    public void Var026()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('hannah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check1 = rs.next();
            String key = rs.getString(1);
            // Call close to make sure that getGeneratedKeys() isn't returning the same
            // result set twice.
            rs.close();
            s.execute ("SELECT * FROM " + table_, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
	    if (rs2 == null) {
		assertCondition(false, "Error:  getGeneratedKeys returned null."); 
	    } else { 
		boolean check2 = rs2.next();
		assertCondition(check1 && key.length() >= 1 && !check2);
	    }
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
            try
            {
              if (s != null) s.close(); 
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


    /**
getGeneratedKeys() - Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute won't
return generated keys, but the second will, so getGeneratedKeys() should return a result set.
**/
    public void Var027()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
            s.execute ("SELECT * FROM " + table_, Statement.RETURN_GENERATED_KEYS);
            s.execute ("INSERT INTO " + table_ + " (NAME) VALUES ('ben')", 
                       Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            String generatedKey = rs.getString(1);
            boolean check2 = rs.next();
            assertCondition(check && !check2 && generatedKey.length() >= 1);            
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally{
            try{
              if (s != null) s.close();
            }
            catch(Exception e){
                //Ignore at this point
            }
        }
    }



/**
getGeneratedKeys() - Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute will
return generated keys, but the second won't, so getGeneratedKeys() should return an
empty result set.
**/
    public void Var028()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('hannah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check1 = rs.next();
            String key = rs.getString(1);
            // Without the rs.close() from above.
            s.execute ("SELECT * FROM " + table_, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
	    if (rs2 == null) {
		assertCondition(false, "Error:  getGeneratedKeys returned null."); 
	    } else { 

		boolean check2 = rs2.next();
		assertCondition(check1 && key.length() >= 1 && !check2);
	    }
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
            try
            {
              if (s != null) s.close(); 
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


    /**
getGeneratedKeys() - Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute won't
return generated keys, and the second won't, so getGeneratedKeys() should return an
empty result set in both cases.
**/
    public void Var029()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
            s.execute ("SELECT * FROM " + table_, Statement.RETURN_GENERATED_KEYS);            
            ResultSet rs = s.getGeneratedKeys();
	    if (rs == null) {
		assertCondition(false, "Error:  getGeneratedKeys returned null."); 
	    } else { 
		boolean check1 = rs.next();
		s.execute ("SELECT * FROM " + table_, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs2 = s.getGeneratedKeys();
		if (rs2 == null) {
		    assertCondition(false, "Error:  getGeneratedKeys returned null."); 
		} else { 

		    boolean check2 = rs2.next();
		    assertCondition(!check1 && !check2);
		}
	    }
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
            try
            {
              if (s != null) s.close(); 
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


/**
getGeneratedKeys() - Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute will
return generated keys, and the second will, so getGeneratedKeys() should return a
different result set in both cases.
**/
    public void Var030()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('bobby')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check1 = rs.next();
            String key1 = rs.getString(1);
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            String key2 = rs2.getString(1);
            assertCondition(check1 && key1.length() >= 1 && check2
                            && key2.length() >= 1 && (!(key1.equals(key2))));
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
            try
            {
              if (s != null) s.close(); 
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


    /**
  getGeneratedKeys() - getGeneratedKeys() should recover if the user caught an 
  exception and work the second time.
  **/
    public void Var031()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try
        {
            boolean exception = false;
            s = connection_.createStatement ();
            try {
                //Statement is missing a paranthesis 
                s.execute ("INSERT INTO " + table_
                           + " NAME) VALUES ('michelle')", 
                           Statement.RETURN_GENERATED_KEYS);                
            }
            catch (SQLException sqe)
            {
                exception = true;
            }
            ResultSet rs = s.getGeneratedKeys();
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            String key2 = rs2.getString(1);
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		// For JCC rs isn't null (like it should be). 
		assertCondition(exception && rs != null && check2
				&& key2.length() >=1, "exception = "+exception+" rs = "+rs+" check2="+check2+" key2.length()="+key2.length());
	    } else if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
		assertCondition(exception && rs != null && check2
				&& key2.length() >=1, "exception = "+exception+" rs = "+rs+" check2="+check2+" key2.length()="+key2.length());
	    } else { 
		assertCondition(exception && rs == null && check2
				&& key2.length() >=1, "exception = "+exception+" rs = "+rs+" check2="+check2+" key2.length()="+key2.length());
	    }
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally{
            try{
              if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }


    /**
getGeneratedKeys() - getGeneratedKeys() return an empty result set if the user caught an 
exception even if the first run was successful.
**/
    public void Var032()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try
        {
            boolean exception = false;
            s = connection_.createStatement ();
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs1 = s.getGeneratedKeys();
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            try {
                //Statement is missing a parenthesis 
                s.execute ("INSERT INTO " + table_
                           + " NAME) VALUES ('michelle')", 
                           Statement.RETURN_GENERATED_KEYS);                
            }
            catch (SQLException sqe)
            {
                exception = true;
            }
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            assertCondition(check1 && key1.length() >= 1 && exception && !check2,
			    "check1="+check1+" key1.length()="+key1.length()+" exception="+exception+" rs.next on failed="+check2);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally{
            try{
              if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }


/**
getGeneratedKeys() - getGeneratedKeys() won't return the same key even though we didn't specify
that the key had to be unique on the table.
**/
    public void Var033()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try
        {
            s = connection_.createStatement ();
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs1 = s.getGeneratedKeys();
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS); 
            /* ResultSet rs2 = */  s.getGeneratedKeys();
            ResultSet rs3 = s.getGeneratedKeys();
            boolean check2 = rs3.next();
            String key2 = rs3.getString(1);
            assertCondition(check1 && key1.length() >= 1 && check2 && key2.length() >= 1
                            && !(key1.equals(key2)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally{
            try{
              if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }



    /**
getGeneratedKeys() - Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute will
return generated keys, and the second won't only because we
specify that, so getGeneratedKeys() should return no result set the second time.
**/
    public void Var034()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('bobby')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check1 = rs.next();
            String key1 = rs.getString(1);
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('deborah')", Statement.NO_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2;
            if(rs2 == null){ //toolbox clears out previous generatedKeysRS
                check2 = false;
            }else {
                check2 = rs2.next();
            }
             
            assertCondition(check1 && key1.length() >= 1 && !check2);
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


    /**
getGeneratedKeys() - Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute won't
return generated keys, and the second will, but getGeneratedKeys() should return a result set 
the second time.
**/
    public void Var035()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('bobby')", Statement.NO_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            String key2 = rs2.getString(1);
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
		assertCondition(rs != null && check2 && key2.length() >= 1);
	    } else { 
		assertCondition(rs == null && check2 && key2.length() >= 1);
	    }
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


/**
 * genGeneratedKeys() -- Make sure the keys are what were promised
 */
 

    public void Var036()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
	    String table =  JDStatementTest.COLLECTION + ".JDGENKEYSV36"; 
            s = connection_.createStatement ();
            JDTestDriver.dropTable(s, table); 

	    initTable(s, table," (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY ( START WITH 10, increment BY 3 ))");

            s.execute ("INSERT INTO " + table
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs1 = s.getGeneratedKeys();
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);

            s.execute ("INSERT INTO " + table
                       + " (NAME) VALUES ('frank')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            String key2 = rs2.getString(1);
	    boolean condition = check1 && key1.equals("10") && check2 && key2.equals("13");
	    if (!condition) {
		System.out.println("  check1 should be true but is "+check1);
		System.out.println("  key1 should be 10 but is "+key1);
		System.out.println("  check2 should be true but is "+check2);
		System.out.println("  key2 should be 10 but is "+key2);
		System.out.println("  check table using select * from "+table); 
	    } else { 
		cleanupTable(s, table);
	    } 
            assertCondition(condition);
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

/**
 * genGeneratedKeys() -- obtain multiple keys -- multiple rows
 * not supported in V5R2. 
 * This is supported in V5R5
 */


    public void Var037()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support insert / select");
	    return; 
	}
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + ".JDGENKEYSV37A";
	    String table2 = JDStatementTest.COLLECTION + ".JDGENKEYSV37B";
            s = connection_.createStatement ();
	    initTable(s, table1," (NAME VARCHAR(10))");
	    s.executeUpdate("insert into "+table1+" values('Abraham')");
	    s.executeUpdate("insert into "+table1+" values('John')");
	    s.executeUpdate("insert into "+table1+" values('Zedekiah')");

      JDTestDriver.dropTable(s, table2); 

	    initTable(s, table2, " (NAME VARCHAR(10), "+
                "GENID INT GENERATED ALWAYS AS IDENTITY ( START WITH 10, increment BY 3 ))");

            s.execute ("INSERT INTO " + table2
                       + " (NAME) SELECT * from "+table1, Statement.RETURN_GENERATED_KEYS); 
            ResultSet rs1 = s.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean condition = true; 
            if (getRelease()  >= JDTestDriver.RELEASE_V5R5M0) {
              // For V5R5, we should be able to get all the values because the
              // implementation uses SELECT from INSERT 
              boolean check2 = rs1.next();
              String key2="NOTFOUND"; 
              if (check2) { 
                key2 = rs1.getString(1);
              }
              boolean check3 = rs1.next();
              String key3 = "NOTFOUND"; 
              if (check3) { 
                key3 = rs1.getString(1);
              }
              condition = check1 && key1.equals("10") &&  
              check2 && key2.equals("13") &&  
              check3 && key3.equals("16") ;   
              if (!condition) {
                System.out.println("  check1 should be true but is "+check1);
                System.out.println("  key1 should be 10 but is "+key1);
                System.out.println("  check2 should be true but is "+check2);
                System.out.println("  key2 should be 13 but is "+key2);
                System.out.println("  check3 should be true but is "+check3);
                System.out.println("  key3 should be 16 but is "+key3);
              }
              
            } else { 
              // Prior to V5R5, we just get the last value. 
              condition = check1 && key1.equals("16"); 
              if (!condition) {
                System.out.println("  check1 should be true but is "+check1);
                System.out.println("  key1 should be 16 but is "+key1);
              }
            }
	    if (condition) {
		cleanupTable(s, table1);
		cleanupTable(s, table2);
	    } 
            assertCondition(condition);
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


   /**
    * execute(sql, columnIndexes) not supported pre-V5R5
    */

    public void Var038()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support column names");
	    return; 
	}

        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3 || isToolboxDriver())
        {
            notApplicable("v5r2/JDK 1.4 native variation");
            return;
        }
	boolean expectException = true;
	if (getDriver() == JDTestDriver.DRIVER_JCC || getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
	    expectException = false; 
	} 
        Statement s = null;
        try {
            s = connection_.createStatement ();
	    int[] columnIndexes = {2}; 
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('bobby')", columnIndexes);
	    if ( ! expectException) {

		ResultSet rs = s.getGeneratedKeys();
		boolean check = rs.next();
		String generatedKey = rs.getString(1);
		boolean check2 = rs.next();
		assertCondition(check && !check2 && generatedKey.length() >= 1,
                      "check = "+check+" check2="+check2+" generatedKey.length()="+generatedKey.length());
		s.close ();

	    } else { 
		failed("No exception thrown by execute(sql, columnIndexes)");
	    }
        }
        catch (Exception e) {
	    if (expectException) {
		// Exception changed with JDK 1.6 PTF 
		if (e.getClass().getName().indexOf("DB2JDBCFeatureNotSupportedException") >= 0 ) {
		    assertExceptionIs(e, "DB2JDBCFeatureNotSupportedException", "The driver does not support this function.");
		} else { 
		    assertExceptionIs(e, "DB2JDBCException", "The driver does not support this function.");
		}
	    } else {
		failed(e); 
	    } 
        }
    }

   /**
    * execute(sql, columnNames) not supported pre-v5r5
    */
    public void Var039()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support column names");
	    return; 
	}

        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3 || 
            isToolboxDriver())
        {
            notApplicable("v5r2/JDK 1.4 native variation");
            return;
        }
	boolean expectException = true;
	if (getDriver() == JDTestDriver.DRIVER_JCC || getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
	    expectException = false; 
	} 

        Statement s = null;
        try {
            s = connection_.createStatement ();
	    String[] columnNames = {"GENID"}; 
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('harold')", columnNames);
	    if ( ! expectException) {

		ResultSet rs = s.getGeneratedKeys();
		boolean check = rs.next();
		String generatedKey = rs.getString(1);
		boolean check2 = rs.next();
		assertCondition(check && !check2 && generatedKey.length() >= 1);
		s.close ();

	    } else { 

		failed("No exception thrown by execute(sql, columnNames)");
	    }
        }
        catch (Exception e) {

	    if (expectException) { 
		// Exception changed with JDK 1.6 PTF 
		if (e.getClass().getName().indexOf("DB2JDBCFeatureNotSupportedException") >= 0 ) {
		    assertExceptionIs(e, "DB2JDBCFeatureNotSupportedException", "The driver does not support this function.");
		} else { 
		    assertExceptionIs(e, "DB2JDBCException", "The driver does not support this function.");
		}
	    } else {
		failed(e); 
	    } 

        }
    }


   /**
    * executeUpdate(sql, columnIndexes) not supported pre-v5r5
    */

    public void Var040()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3 || getDriver() != JDTestDriver.DRIVER_NATIVE)
        {
            notApplicable("v5r2/JDK 1.4 native variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
	    int[] columnIndexes = {2}; 
            int rowCount = s.executeUpdate ("INSERT INTO " + table_
                       + " (NAME) VALUES ('bobby')", columnIndexes);
            if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
              
                ResultSet rs = s.getGeneratedKeys();
                boolean check = rs.next();
                String generatedKey = rs.getString(1);
                boolean check2 = rs.next();
                s.close ();
                assertCondition(rowCount==1 && check && !check2 && generatedKey.length() >= 1,
				"rowCount="+rowCount+" sb 1\n"+
				"check="+check+" sb true\n"+
				"check2="+check2+"sb false\n"+
				"generatedKey.length()="+generatedKey.length()+"sb >= 1"); 
              
            } else {
              failed("No exception thrown by execute(sqlUpdate, columnIndexes)");
            }
        }
        catch (Exception e) {
          if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
              failed(e); 
          } else { 
		// Exception changed with JDK 1.6 PTF 
		if (e.getClass().getName().indexOf("DB2JDBCFeatureNotSupportedException") >= 0 ) {
		    assertExceptionIs(e, "DB2JDBCFeatureNotSupportedException", "The driver does not support this function.");
		} else { 
		    assertExceptionIs(e, "DB2JDBCException", "The driver does not support this function.");
		}
          }
        }
    }

   /**
    * executeUpdate(sql, columnNames) not supported
    */

    public void Var041()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3 || getDriver() != JDTestDriver.DRIVER_NATIVE)
        {
            notApplicable("v5r2/JDK 1.4 native variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
	    String[] columnNames = {"GENID"}; 
	    int rowCount = s.executeUpdate ("INSERT INTO " + table_
                       + " (NAME) VALUES ('bobby')", columnNames);
            if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
              ResultSet rs = s.getGeneratedKeys();
              boolean check = rs.next();
              String generatedKey = rs.getString(1);
              boolean check2 = rs.next();
              s.close ();
	      assertCondition(rowCount==1 && check && !check2 && generatedKey.length() >= 1,
			      "rowCount="+rowCount+" sb 1\n"+
			      "check="+check+" sb true\n"+
			      "check2="+check2+"sb false\n"+
			      "generatedKey.length()="+generatedKey.length()+"sb >= 1");
           
            } else {             
	       failed("No exception thrown by executeUpdate(sql, columnNames)");
            }
        }
        catch (Exception e) {
          if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
            failed(e); 
	  } else { 
		// Exception changed with JDK 1.6 PTF 
	      if (e.getClass().getName().indexOf("DB2JDBCFeatureNotSupportedException") >= 0 ) {
		  assertExceptionIs(e, "DB2JDBCFeatureNotSupportedException", "The driver does not support this function.");
	      } else { 
		  assertExceptionIs(e, "DB2JDBCException", "The driver does not support this function.");
	      }
	  }
        }
    }

    /**
     * prepareStatement( columnIndexes) not supported pre-V5R5
     */ 
    public void Var042()
    {
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3 || getDriver() != JDTestDriver.DRIVER_NATIVE)
        {
            notApplicable("v5r2/JDK 1.4 native variation");
            return;
        }
        try {
	    int[] columnIndexes = {2}; 

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + table_
                + " (NAME) VALUES ('george')", columnIndexes);
            if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
              ps.execute(); 
              ResultSet rs = ps.getGeneratedKeys();
              boolean check = (rs != null); 
              if (check && (rs != null)) check = rs.next();
              assertCondition(check, "check = "+check+" rs = "+rs); 
              
            } else {            
              failed("No exception thrown by prepareStatement(sql, columnIndexes) ps="+ps);
            }
        }
        catch (Exception e) {
          if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
            failed(e); 
	  } else {
		// Exception changed with JDK 1.6 PTF 
	      if (e.getClass().getName().indexOf("DB2JDBCFeatureNotSupportedException") >= 0 ) {
		  assertExceptionIs(e, "DB2JDBCFeatureNotSupportedException", "The driver does not support this function.");
	      } else { 

		  assertExceptionIs(e, "DB2JDBCException", "The driver does not support this function.");
	      }
	  }
        }
    }


    /**
     * prepareStatement( columnNames) not supported
     */ 

    public void Var043()
    {
      if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3 || getDriver() != JDTestDriver.DRIVER_NATIVE)
      {
        notApplicable("v5r2/JDK 1.4 native variation");
        return;
      }
      try {
        String[] columnNames = {"GENID"}; 
        
        PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + table_
            + " (NAME) VALUES ('harold')", columnNames);
        if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
          ps.execute(); 
          ResultSet rs = ps.getGeneratedKeys();
          boolean check = (rs != null); 
          if (check && (rs != null) ) check = rs.next();
          assertCondition(check, "check = "+check+" rs = "+rs); 
          
        } else {            
          failed("No exception thrown by prepareStatement(sql, columnNames) "+ps);
        }
      }
      catch (Exception e) {
        if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
          failed(e); 
	} else  {
		// Exception changed with JDK 1.6 PTF 
	    if (e.getClass().getName().indexOf("DB2JDBCFeatureNotSupportedException") >= 0 ) {
		assertExceptionIs(e, "DB2JDBCFeatureNotSupportedException", "The driver does not support this function.");
	    } else { 

		assertExceptionIs(e, "DB2JDBCException", "The driver does not support this function.");
	    }
	}
      }
    }



/**
getGeneratedKeys() - Using system naming - If a prepared statement has not been executed, should get a
null result set returned from getGeneratedKeys().
**/
    public void Var044()
    {

	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }

        try
        {
            PreparedStatement ps = connectionSystemNaming_.prepareStatement("INSERT INTO " + systemTable_
                                                                + " (NAME) VALUES ('dave')");
            ResultSet rs = ps.getGeneratedKeys();
            assertCondition(rs == null, "system naming test added 12/2/2003");
            ps.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception -- system naming test added 12/2/2003");
        }
    }

 
/**
getGeneratedKeys() - Using system naming -If no prepared statements have been executed, should get a 
null result set from getGeneratedKeys().
**/
    public void Var045()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 

        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try
        {
            PreparedStatement ps = connectionSystemNaming_.prepareStatement ("INSERT INTO " + systemTable_
                                                                 + " (NAME) VALUES ('chris')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = ps.getGeneratedKeys();
            assertCondition(rs == null, "system naming test added 12/2/2003");
            ps.close();
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception -- system naming test added 12/2/2003");
        }
    }


/**
getGeneratedKeys() - Using system naming -Since we didn't specify we wanted generated keys when we prepared
this statement, getGeneratedKeys() should return null.
**/
    public void Var046()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 

        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
 
        try {

            PreparedStatement ps = connectionSystemNaming_.prepareStatement ("INSERT INTO " + systemTable_
                                                                 + " (NAME) VALUES ('jeff')");
            ps.execute ();
            ResultSet rs = ps.getGeneratedKeys();
            assertCondition(rs == null, "system naming test added 12/2/2003");
            ps.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception  -- system naming test added 12/2/2003");
        }
    }


    /**
getGeneratedKeys() - Using system naming -Since we didn't specify we wanted generated keys when we prepared this
statement, getGeneratedKeys() should return null.
**/
    public void Var047()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 

        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {

            PreparedStatement ps = connectionSystemNaming_.prepareStatement ("INSERT INTO " + systemTable_
                                                                 + " (NAME) VALUES ('jim')", Statement.NO_GENERATED_KEYS);
            ps.execute ();
            ResultSet rs = ps.getGeneratedKeys();
            assertCondition(rs== null, "system naming test added 12/2/2003");
            ps.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception  -- system naming test added 12/2/2003");
        }
    }




/**
getGeneratedKeys() - Using system naming -Make sure that an empty result set is returned from an empty table
(no rows).
**/
    public void Var048()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 

        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s= null;
        try {
            s = connectionSystemNaming_.createStatement ();
            s.execute ("SELECT * FROM " + systemTable_, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            assertCondition(!check, "system naming test added 12/2/2003");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception  -- system naming test added 12/2/2003");
        }
        finally{
            try{
                if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }



/**
getGeneratedKeys() - Using system naming -Verify that a generated key is returned w/ execute and INSERT.
**/
    public void Var049()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 

        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s =null;
        try {
            s = connectionSystemNaming_.createStatement ();
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('sue2')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            String generatedKey = rs.getString(1);
            boolean check2 = rs.next();
            assertCondition(check && !check2 && generatedKey.length() >= 1,  "system naming test added 12/2/2003");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming test added 12/2/2003");
        }
        finally{
            try{
              if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }


/**
getGeneratedKeys() - Using system naming -Verify that a generated key is returned w/ executeUpdate and INSERT.
**/
    public void Var050()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 

        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try
        {
            s = connectionSystemNaming_.createStatement();
            int rowCount = s.executeUpdate ("INSERT INTO " + systemTable_
                             + " (NAME) VALUES ('sally2')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            String generatedKey = rs.getString(1);
            boolean check2 = rs.next();
	    assertCondition(rowCount==1 && check && !check2 && generatedKey.length() >= 1,
			    "rowCount="+rowCount+" sb 1\n"+
			    "check="+check+" sb true\n"+
			    "check2="+check2+"sb false\n"+
			    "generatedKey.length()="+generatedKey.length()+"sb >= 1\n"+
			    "system naming test added 12/2/2003");
        }
        catch (Exception e)
        {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
        finally{
            try{
                if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }


    /**
getGeneratedKeys() - Using system naming -Verify an empty result set is returned from getGeneratedKeys() as auto
generated keys aren't supported from executeQuery() statements.
**/
    public void Var051()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 

        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {
            PreparedStatement s = connectionSystemNaming_.prepareStatement ("SELECT * FROM QIWS/QCUSTCDT", Statement.RETURN_GENERATED_KEYS);
            s.executeQuery ();
            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            assertCondition(!check,  "system naming test added 12/2/2003");
            s.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
    }




/**
getGeneratedKeys() - Using system naming -Verify that an exception is thrown if the user tries
to access generated keys once the statement is closed.
**/
    public void Var052()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 

        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try
        {
            Statement s = connectionSystemNaming_.createStatement ();
            s.executeUpdate ("INSERT INTO " + systemTable_
                             + " (NAME) VALUES ('michelle')", 
                             Statement.RETURN_GENERATED_KEYS);
            s.close ();
            s.getGeneratedKeys();
            failed ("Didn't throw SQLException"); 
        }
        catch (SQLException e) //@A1
        {

	    assertClosedException(e,  "system naming test added 12/2/2003");


        }
    }



/**
getGeneratedKeys() - Using system naming -getGeneratedKeys() returns an empty result set on an UPDATE because no
auto-generated key will be returned.
**/
    public void Var053()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try
        {
            Statement s = connectionSystemNaming_.createStatement ();
            int rowCount = s.executeUpdate ("UPDATE " + systemTable_
                             + " SET NAME='Molly' WHERE NAME = 'Janet'",  
                             Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            assertCondition(rowCount==0 && !check,  "rowCount="+rowCount+"sb 0 \n system naming test added 12/2/2003");
            s.close ();        
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
    }



/**
getGeneratedKeys() - Using system naming -getGeneratedKeys() should return null if the user caught an exception.
**/
    public void Var054()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try
        {
            boolean exception = false;
            Statement s = connectionSystemNaming_.createStatement ();
            try {
                //Statement is missing a paranthesis 
                s.execute ("INSERT INTO " + systemTable_
                           + " NAME) VALUES ('michelle')", 
                           Statement.RETURN_GENERATED_KEYS);                
            }
            catch (SQLException sqe)
            {
                exception = true;
            }
            ResultSet rs = s.getGeneratedKeys();

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
		assertCondition (exception && rs != null,  "system naming test added 12/2/2003");
	    } else { 
		assertCondition (exception && rs == null,  "system naming test added 12/2/2003");
	    }
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
    }


/**
getGeneratedKeys() - Using system naming -Check that if the user calls getGeneratedKeys() on a CALL statement,
getGeneratedKeys() returns an empty result set as CALLs do not return generated keys.
**/
    public void Var055()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }

        try
        {
            PreparedStatement ps = connectionSystemNaming_.prepareStatement ("CALL "
                                                                 + JDSetupProcedure.STP_RS0.replace('.','/'),
                                                                 Statement.RETURN_GENERATED_KEYS);
            ps.execute ();
            ResultSet rs = ps.getGeneratedKeys();
            boolean check = rs.next();
            assertCondition(!check,  "system naming test added 12/2/2003");
            ps.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
    }


/**
getGeneratedKeys() - Using system naming -getGeneratedKeys() should return a result set on a prepared statement
and execute.
**/
    public void Var056()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        PreparedStatement ps = null;
        try {

            ps = connectionSystemNaming_.prepareStatement ("INSERT INTO " + systemTable_
                                                                 + " (NAME) VALUES ('Holly2')", Statement.RETURN_GENERATED_KEYS);
            ps.execute ();
            ResultSet rs = ps.getGeneratedKeys();
            boolean nextWorked = rs.next();
            String key = rs.getString(1);
            boolean nextWorkedAgain = rs.next();

	    Statement stmt = connectionSystemNaming_.createStatement();
	    rs = stmt.executeQuery("SELECT GENID from "+systemTable_+" WHERE NAME='Holly2'");
	    rs.next(); 
	    String fileKey =rs.getString(1); 

            assertCondition (key.equals(fileKey) && nextWorked && !nextWorkedAgain,  "key("+key+")!=fileKey("+fileKey+") -- system naming test added 12/2/2003");
	    stmt.close(); 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
        finally{
            try{
              if (ps != null) ps.close();
            }catch(Exception e){
            }
        }
    }


    /**
getGeneratedKeys() - Using system naming -getGeneratedKeys() should return a result set on a prepared statement
and executeUpdate.
**/
    public void Var057()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        PreparedStatement ps = null;
        try {

            ps = connectionSystemNaming_.prepareStatement ("INSERT INTO " + systemTable_
                                                                 + " (NAME) VALUES ('Jeffrey2')", Statement.RETURN_GENERATED_KEYS);
	    int rowCount = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            boolean nextWorked = rs.next();
            String key = rs.getString(1);
            boolean nextWorkedAgain = rs.next();
            assertCondition (rowCount==1 && key.length() >= 1 && nextWorked && !nextWorkedAgain,
			     "rowCount="+rowCount+" sb 1\n"+
			     "key.length()="+key.length()+"sb >= 1\n"+
			     "nextWorked="+nextWorked+" sb true \n"+
			     "nextWorkedAgain="+nextWorkedAgain+"sb false \n"+
			     "system naming test added 12/2/2003");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
        finally{
            try{
              if (ps != null) ps.close();
            }catch(Exception e){
            }
        }
    }


    /**
getGeneratedKeys() - Using system naming -getGeneratedKeys() should an empty result set on a prepared statement
and executeQuery.
**/
    public void Var058()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {

            PreparedStatement ps = connectionSystemNaming_.prepareStatement ("SELECT * FROM QIWS/QCUSTCDT", Statement.RETURN_GENERATED_KEYS);
            ps.executeQuery ();
            ResultSet rs = ps.getGeneratedKeys();
            boolean check = rs.next();
            assertCondition(!check,  "system naming test added 12/2/2003");
            ps.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
    }


    /**
getGeneratedKeys() - Using system naming -Verify that a generated key is returned w/ execute and DELETE.
**/
    public void Var059()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {
            Statement s = connectionSystemNaming_.createStatement ();
            s.execute ("DELETE FROM " + systemTable_
                       + " WHERE NAME='sue2'", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            String generatedKey = rs.getString(1);
            boolean check2 = rs.next();
            assertCondition(check && !check2 && generatedKey.length() >= 1,  "system naming test added 12/2/2003");
            s.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
    }


    /**
getGeneratedKeys() - Using system naming -Verify that an empty result set is returned w/ execute and a DELETE
that doesn't match a row in the table.
**/
    public void Var060()
    {
 	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
       if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {
            Statement s = connectionSystemNaming_.createStatement ();
            s.execute ("DELETE FROM " + systemTable_
                       + " WHERE NAME='john'", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            assertCondition(!check,  "system naming test added 12/2/2003");
            s.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
    }


    /**
getGeneratedKeys() - Using system naming -Since we requested no generated keys, getGeneratedKeys() should
return null.
**/
    public void Var061()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {
            Statement s = connectionSystemNaming_.createStatement ();
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('ivy')", Statement.NO_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
            assertCondition(rs != null,  "system naming test added 12/2/2003");
	    } else { 
            assertCondition(rs == null,  "system naming test added 12/2/2003");
	    }
            s.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
    }


    /**
getGeneratedKeys() - Using system naming -Since we requested no generated keys, getGeneratedKeys() should
return null.
**/
    public void Var062()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {
            CallableStatement cs = connectionSystemNaming_.prepareCall ("CALL " + JDSetupProcedure.STP_RS0.replace('.','/'));
            cs.execute ();
            ResultSet rs = cs.getGeneratedKeys();
            assertCondition(rs == null,  "system naming test added 12/2/2003");
            cs.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
    }


    /**
getGeneratedKeys() - Using system naming -CallableStatements never support getGeneratedKeys(), so it should
return null.
**/
    public void Var063()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {
            CallableStatement cs = connectionSystemNaming_.prepareCall ("CALL " + JDSetupProcedure.STP_RS0.replace('.','/'));
            cs.execute ();
            ResultSet rs = cs.getGeneratedKeys();
            assertCondition(rs == null,  "system naming test added 12/2/2003");
            cs.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
    }


    /**
getGeneratedKeys() - Using system naming -Since we requested no generated keys, getGeneratedKeys() shouldreturn null.
**/
    public void Var064()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        try {
            Statement s = connectionSystemNaming_.createStatement ();
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('molly')");
            ResultSet rs = s.getGeneratedKeys();
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
		boolean rsNext = true;
		if (rs != null) {
		    rsNext = rs.next(); 
		} 
		assertCondition(rs != null && rsNext == false,
				"rs is "+rs+" should not be null\n"+
				"rsNext = "+rsNext+" should be false\n"+
				"system naming test added 12/2/2003");
	    } else { 
		assertCondition(rs == null,  "system naming test added 12/2/2003");
	    }
            s.close ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
    }


    /**
getGeneratedKeys() - Using system naming -Since table2 does not have an identity column column, getGeneratedKeys() 
should return an empty result set.
**/
    public void Var065()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connectionSystemNaming_.createStatement ();
            s.execute ("INSERT INTO " + systemTable2_
                       + " (NAME) VALUES ('jackiek')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            if (getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
              // In V5R5 we can return autogenerated keys, even from a non-identity column
              assertCondition(check,  "system naming test added 12/2/2003");
            } else {
              assertCondition(!check,  "system naming test added 12/2/2003");
            }
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
        finally{
            try{
              if (s != null)  s.close();
            }catch(Exception e){
            }
        }
    }


/**
getGeneratedKeys() - Using system naming -Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute will
return generated keys, but the second won't, so getGeneratedKeys() should return an
empty result set.
**/
    public void Var066()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connectionSystemNaming_.createStatement ();
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('hannah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check1 = rs.next();
            String key = rs.getString(1);
            // Call close to make sure that getGeneratedKeys() isn't returning the same
            // result set twice.
            rs.close();
            s.execute ("SELECT * FROM " + systemTable_, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            assertCondition(check1 && key.length() >= 1 && !check2,  "system naming test added 12/2/2003");
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


    /**
getGeneratedKeys() - Using system naming -Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute won't
return generated keys, but the second will, so getGeneratedKeys() should return a result set.
**/
    public void Var067()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connectionSystemNaming_.createStatement ();
            s.execute ("SELECT * FROM " + systemTable_, Statement.RETURN_GENERATED_KEYS);
            s.execute ("INSERT INTO " + systemTable_ + " (NAME) VALUES ('ben')", 
                       Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            String generatedKey = rs.getString(1);
            boolean check2 = rs.next();
            assertCondition(check && !check2 && generatedKey.length() >= 1,  "system naming test added 12/2/2003");            
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
        finally{
            try{
              if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }



/**
getGeneratedKeys() - Using system naming -Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute will
return generated keys, but the second won't, so getGeneratedKeys() should return an
empty result set.
**/
    public void Var068()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connectionSystemNaming_.createStatement ();
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('hannah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check1 = rs.next();
            String key = rs.getString(1);
            // Without the rs.close() from above.
            s.execute ("SELECT * FROM " + systemTable_, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            assertCondition(check1 && key.length() >= 1 && !check2,  "system naming test added 12/2/2003");
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


    /**
getGeneratedKeys() - Using system naming -Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute won't
return generated keys, and the second won't, so getGeneratedKeys() should return an
empty result set in both cases.
**/
    public void Var069()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connectionSystemNaming_.createStatement ();
            s.execute ("SELECT * FROM " + systemTable_, Statement.RETURN_GENERATED_KEYS);            
            ResultSet rs = s.getGeneratedKeys();
            boolean check1 = rs.next();
            s.execute ("SELECT * FROM " + systemTable_, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            assertCondition(!check1 && !check2,  "system naming test added 12/2/2003");
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


/**
getGeneratedKeys() - Using system naming -Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute will
return generated keys, and the second will, so getGeneratedKeys() should return a
different result set in both cases.
**/
    public void Var070()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connectionSystemNaming_.createStatement ();
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('bobby')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check1 = rs.next();
            String key1 = rs.getString(1);
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            String key2 = rs2.getString(1);
            assertCondition(check1 && key1.length() >= 1 && check2
                            && key2.length() >= 1 && (!(key1.equals(key2))),  "system naming test added 12/2/2003");
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


    /**
  getGeneratedKeys() - Using system naming -getGeneratedKeys() should recover if the user caught an 
  exception and work the second time.
  **/
    public void Var071()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try
        {
            boolean exception = false;
            s = connectionSystemNaming_.createStatement ();
            try {
                //Statement is missing a paranthesis 
                s.execute ("INSERT INTO " + systemTable_
                           + " NAME) VALUES ('michelle')", 
                           Statement.RETURN_GENERATED_KEYS);                
            }
            catch (SQLException sqe)
            {
                exception = true;
            }
            ResultSet rs = s.getGeneratedKeys();
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            String key2 = rs2.getString(1);
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
		assertCondition(exception && rs != null && check2
				&& key2.length() >= 1);
	    } else { 
		assertCondition(exception && rs == null && check2
				&& key2.length() >= 1);
	    }
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
        finally{
            try{
              if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }


    /**
getGeneratedKeys() - Using system naming -getGeneratedKeys() return an empty result set if the user caught an 
exception even if the first run was successful.
**/
    public void Var072()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try
        {
            boolean exception = false;
            s = connectionSystemNaming_.createStatement ();
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs1 = s.getGeneratedKeys();
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            try {
                //Statement is missing a parenthesis 
                s.execute ("INSERT INTO " + systemTable_
                           + " NAME) VALUES ('michelle')", 
                           Statement.RETURN_GENERATED_KEYS);                
            }
            catch (SQLException sqe)
            {
                exception = true;
            }
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            assertCondition(check1 && key1.length() >= 1 && exception && !check2,  "system naming test added 12/2/2003");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
        finally{
            try{
              if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }


/**
getGeneratedKeys() - Using system naming -getGeneratedKeys() won't return the same key even though we didn't specify
that the key had to be unique on the table.
**/
    public void Var073()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try
        {
            s = connectionSystemNaming_.createStatement ();
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs1 = s.getGeneratedKeys();
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS); 
            ResultSet rs2 = s.getGeneratedKeys();
            ResultSet rs3 = s.getGeneratedKeys();
            boolean check2 = rs3.next();
            String key2 = rs3.getString(1);
            assertCondition(check1 && key1.length() >= 1 && check2 && key2.length() >= 1
                            && !(key1.equals(key2)),  "system naming test added 12/2/2003 rs2="+rs2);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
        }
        finally{
            try{
              if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }



    /**
getGeneratedKeys() - Using system naming -Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute will
return generated keys, and the second won't only because we
specify that, so getGeneratedKeys() should return no result set the second time.
**/
    public void Var074()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connectionSystemNaming_.createStatement ();
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('bobby')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            boolean check1 = rs.next();
            String key1 = rs.getString(1);
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('deborah')", Statement.NO_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2;
            if(rs2 == null){ //toolbox clears out previous generatedKeysRS
                check2 = false;
            }else{
                check2 = rs2.next();
            }
            assertCondition(check1 && key1.length() >= 1 && !check2,  "system naming test added 12/2/2003");
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


    /**
getGeneratedKeys() - Using system naming -Make sure if we execute more than one statement that we get the
right value back from getGeneratedKeys().  In this example, the first execute won't
return generated keys, and the second will, but getGeneratedKeys() should return a result set 
the second time.
**/
    public void Var075()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
            s = connectionSystemNaming_.createStatement ();
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('bobby')", Statement.NO_GENERATED_KEYS);
            ResultSet rs = s.getGeneratedKeys();
            s.execute ("INSERT INTO " + systemTable_
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            String key2 = rs2.getString(1);
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getRelease() >= JDTestDriver.RELEASE_V5R5M0) {
		assertCondition(rs != null && check2 && key2.length() >= 1,  "system naming test added 12/2/2003");
	    } else { 
		assertCondition(rs == null && check2 && key2.length() >= 1,  "system naming test added 12/2/2003");
	    }
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


/**
 * genGeneratedKeys() -- Make sure the keys are what were promised
 */
 

    public void Var076()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
	    String table =  JDStatementTest.COLLECTION + "/JDGENKEYSV36"; 
            s = connectionSystemNaming_.createStatement ();
            // We must drop the table since we are testtng the generated id 
            JDTestDriver.dropTable(s, table); 
	    initTable(s, table," (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY ( START WITH 10, increment BY 3 ))");

            s.execute ("INSERT INTO " + table
                       + " (NAME) VALUES ('deborah')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs1 = s.getGeneratedKeys();
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);

            s.execute ("INSERT INTO " + table
                       + " (NAME) VALUES ('frank')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs2 = s.getGeneratedKeys();
            boolean check2 = rs2.next();
            String key2 = rs2.getString(1);
	    boolean condition = check1 && key1.equals("10") && check2 && key2.equals("13");
	    if (!condition) {
		System.out.println("  check1 should be true but is "+check1);
		System.out.println("  key1 should be 10 but is "+key1);
		System.out.println("  check2 should be true but is "+check2);
		System.out.println("  key2 should be 10 but is "+key2);
		System.out.println("  check table using select * from "+table); 
	    } else { 
		cleanupTable(s, table);
	    } 
            assertCondition(condition,  "system naming test added 12/2/2003");
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

/**
 * genGeneratedKeys() -- obtain multiple keys -- multiple rows
 * not supported in V5R2. 
 */


    public void Var077()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R2M0 || getJdbcLevel() < 3)
        {
            notApplicable("v5r2/JDK 1.4 variation");
            return;
        }
        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV37A";
	    String table2 = JDStatementTest.COLLECTION + "/JDGENKEYSV37B";
            s = connectionSystemNaming_.createStatement ();

	    initTable(s, table1," (NAME VARCHAR(10))");
	    s.executeUpdate("insert into "+table1+" values('Abraham')");
	    s.executeUpdate("insert into "+table1+" values('John')");
	    s.executeUpdate("insert into "+table1+" values('Zedekiah')");

      JDTestDriver.dropTable(s, table2); 

	    initTable(s, table2," (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY ( START WITH 10, increment BY 3 ))");

            s.execute ("INSERT INTO " + table2
                       + " (NAME) SELECT * from "+table1, Statement.RETURN_GENERATED_KEYS); 
            ResultSet rs1 = s.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
	    boolean condition = true ; 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);

            
            if (getRelease()  >= JDTestDriver.RELEASE_V5R5M0) {
              // For V5R5, we should be able to get all the values because the
              // implementation uses SELECT from INSERT 
              boolean check2 = rs1.next();
              String key2="NOTFOUND"; 
              if (check2) { 
                key2 = rs1.getString(1);
              }
              boolean check3 = rs1.next();
              String key3 = "NOTFOUND"; 
              if (check3) { 
                key3 = rs1.getString(1);
              }
              condition = check1 && key1.equals("10") &&  
              check2 && key2.equals("13") &&  
              check3 && key3.equals("16") ;   
              if (!condition) {
                System.out.println("  check1 should be true but is "+check1);
                System.out.println("  key1 should be 10 but is "+key1);
                System.out.println("  check2 should be true but is "+check2);
                System.out.println("  key2 should be 13 but is "+key2);
                System.out.println("  check3 should be true but is "+check3);
                System.out.println("  key3 should be 16 but is "+key3);
              }
              
            } else { 
              // Prior to V5R5, we just get the last value. 
              condition = check1 && key1.equals("16"); 
              if (!condition) {
                System.out.println("  check1 should be true but is "+check1);
                System.out.println("  key1 should be 16 but is "+key1);
              }
            }

            

	    if (condition) {
		cleanupTable(s, table1);
		cleanupTable(s, table2);
	    } 
            assertCondition(condition,  "system naming test added 12/2/2003");
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

    // @B1A
    /**
    * execute(sql, columnIndexes) not supported pre-V5R5
    */
    public void Var078()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support column indexes");
	    return; 
	}

	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
        Statement s = null;
        try {
            s = connection_.createStatement ();
	    int[] columnIndexes = {2}; 
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('bobby')", columnIndexes);
            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            String generatedKey = rs.getString(1);
            boolean check2 = rs.next();
            assertCondition(check && !check2 && generatedKey.length() >= 1, "Added by Toolbox 1/23/2007");
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.  Added by Toolbox 1/23/2007.");
        }
        finally{
            try{
              if (s != null) s.close();
            }catch(Exception e){
            }
        }
        
    }

    //@B1A
   /**
    * execute(sql, columnNames) not supported pe-v5r5
    */
    public void Var079()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support column names");
	    return; 
	}

        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }

	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
            s = connection_.createStatement ();
	    String[] columnNames = {"GENID"}; 
            s.execute ("INSERT INTO " + table_
                       + " (NAME) VALUES ('harold')", columnNames);

            ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            String generatedKey = rs.getString(1);
            boolean check2 = rs.next();
            assertCondition(check && !check2 && generatedKey.length() >= 1, "Added 1/23/2007 by Toolbox.");
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.   Added 1/23/2007 by Toolbox.");
        }
        finally{
            try{
              if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }


    // @B1A
   /**
    * executeUpdate(sql, columnIndexes) not supported pre-v5r5
    */

    public void Var080()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support column indexes");
	    return; 
	}

        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }

	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
            s = connection_.createStatement ();
	    int[] columnIndexes = {2}; 
	    int rowCount = s.executeUpdate ("INSERT INTO " + table_
                       + " (NAME) VALUES ('bobby')", columnIndexes);
	    ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            String generatedKey = rs.getString(1);
            boolean check2 = rs.next();
            assertCondition(rowCount==1 && check && !check2 && generatedKey.length() >= 1, "Added by Toolbox 1/23/2007");
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.  Added 1/23/2007 by Toolbox.");
        }
        finally{
            try{
              if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }

    //@B1A
   /**
    * executeUpdate(sql, columnNames)
    */

    public void Var081()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support columnNames");
	    return; 
	}

        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
            s = connection_.createStatement ();
	    String[] columnNames = {"GENID"}; 
	    int rowCount = s.executeUpdate ("INSERT INTO " + table_
                       + " (NAME) VALUES ('bobby')", columnNames); 
	    ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            String generatedKey = rs.getString(1);
            boolean check2 = rs.next();
            assertCondition(rowCount==1 && check && !check2 && generatedKey.length() >= 1, "Added 1/23/2007 by Toolbox.");
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.  Added 1/23/2007 by Toolbox.");
        }
        finally{
            try{
              if (s != null) s.close();
            }catch(Exception e){
            }
        }
    }

    //@B1A
    /**
     * prepareStatement(sql, columnIndexes) not supported pre-v5r5
     */ 
    public void Var082()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support column indexes");
	    return; 
	}

        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        PreparedStatement ps = null;
        try {
	    int[] columnIndexes = {2}; 

            ps = connection_.prepareStatement ("INSERT INTO " + table_
                                                                 + " (NAME) VALUES ('Holly')", columnIndexes);
            ps.execute ();
            ResultSet rs = ps.getGeneratedKeys();
            boolean nextWorked = rs.next();
            String key = rs.getString(1);
            boolean nextWorkedAgain = rs.next();
            assertCondition (key.length() >= 1 && nextWorked && !nextWorkedAgain);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.  Added 1/23/2007 by Toolbox.");
        }
        finally{
            try{
              if (ps != null) ps.close();
            }catch(Exception e){
            }
        }
    }


    /**
     * prepareStatement(sql, columnNames) not supported pre-v5r5
     */ 

    public void Var083()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support passing column names");
	    return; 
	}

        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        PreparedStatement ps = null;
        try {
	    String[] columnNames = {"GENID"}; 

            ps = connection_.prepareStatement ("INSERT INTO " + table_
                                                                 + " (NAME) VALUES ('Holly')", columnNames);
            ps.execute ();
            ResultSet rs = ps.getGeneratedKeys();
            boolean nextWorked = rs.next();
            String key = rs.getString(1);
            boolean nextWorkedAgain = rs.next();
            assertCondition (key.length() >= 1 && nextWorked && !nextWorkedAgain);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.  Added 1/23/2007 by Toolbox.");
        }
        finally{
            try{
              if (ps != null) ps.close();
            }catch(Exception e){
            }
        }
    }

    // @B1A
    /**
    * genGeneratedKeys() -- obtain multiple keys  -- multiple rows
    * execute(sql, columnIndexes)
    * This is supported in V5R5
    */
    public void Var084()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support INSERT/SELECT");
	    return; 
	}

        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + ".JDGENKEYSV84A";
	    String table2 = JDStatementTest.COLLECTION + ".JDGENKEYSV84B";
            s = connection_.createStatement ();
	    initTable(s, table1," (NAME VARCHAR(10))");
	    s.executeUpdate("insert into "+table1+" values('Abraham')");
	    s.executeUpdate("insert into "+table1+" values('John')");
	    s.executeUpdate("insert into "+table1+" values('Zedekiah')");

      JDTestDriver.dropTable(s, table2); 

	    initTable(s, table2," (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY ( START WITH 10, increment BY 3 ))");

            int columnIndexes[] = {2};
            s.execute ("INSERT INTO " + table2
                       + " (NAME) SELECT * from "+table1, columnIndexes); 
            ResultSet rs1 = s.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean check2 = rs1.next();
            String key2 = rs1.getString(1);
            boolean check3 = rs1.next();
            String key3 = rs1.getString(1);
            boolean condition = check1 && key1.equals("10") && check2 && key2.equals("13") && check3 && key3.equals("16");
            if(!condition){
                System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2 + ", check3 sb true but is " + check3);
                System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 13 but is " + key2 + ", key3 sb 16 but is " + key3);
            }
            else
            {
                cleanupTable(s,  table1);
                cleanupTable(s,  table2);
            }
            assertCondition(condition, "Added 1/23/2007 by Toolbox.");
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception.  Added 1/23/2007 by Toolbox.");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys  -- multiple rows
     * execute(sql, columnNames)
     */
    public void Var085()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV85A";
	    String table2 = JDStatementTest.COLLECTION + "/JDGENKEYSV85B";
            s = connectionSystemNaming_.createStatement ();
	    initTable(s, table1," (NAME VARCHAR(10))");
	    s.executeUpdate("insert into "+table1+" values('Abraham')");
	    s.executeUpdate("insert into "+table1+" values('John')");
	    s.executeUpdate("insert into "+table1+" values('Zedekiah')");

      JDTestDriver.dropTable(s, table2); 

	    initTable(s, table2, " (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY ( START WITH 10, increment BY 3 ))");

            String[] columnNames = new String[] {"GENID"};
            s.execute ("INSERT INTO " + table2
                       + " (NAME) SELECT * from "+table1, columnNames); 
            ResultSet rs1 = s.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 

            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean check2 = rs1.next();
            String key2 = rs1.getString(1);
            boolean check3 = rs1.next();
            String key3 = rs1.getString(1);
            boolean condition = check1 && key1.equals("10") && check2 && key2.equals("13") && check3 && key3.equals("16");
            if(!condition){
                System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2 + ", check3 sb true but is " + check3);
                System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 13 but is " + key2 + ", key3 sb 16 but is " + key3);
            }
            else
            {
                cleanupTable(s,  table1);
                cleanupTable(s,  table2);
            }
            assertCondition(condition);
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

    // @B1A
    /**
    * genGeneratedKeys() -- obtain multiple keys  -- multiple rows
    * executeUpdate(sql, columnIndexes)
    * This is supported in V5R5
    */
    public void Var086()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support INSERT/SELECT");
	    return; 
	}

        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + ".JDGENKEYSV86A";
	    String table2 = JDStatementTest.COLLECTION + ".JDGENKEYSV86B";
            s = connection_.createStatement ();
	    initTable(s, table1," (NAME VARCHAR(10))");
	    s.executeUpdate("insert into "+table1+" values('Abraham')");
	    s.executeUpdate("insert into "+table1+" values('John')");
	    s.executeUpdate("insert into "+table1+" values('Zedekiah')");

      JDTestDriver.dropTable(s, table2); 

	    initTable(s, table2," (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY ( START WITH 10, increment BY 3 ))");

            int columnIndexes[] = {2};
	    int rowCount = s.executeUpdate ("INSERT INTO " + table2
                       + " (NAME) SELECT * from "+table1, columnIndexes); 
            ResultSet rs1 = s.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean check2 = rs1.next();
            String key2 = rs1.getString(1);
            boolean check3 = rs1.next();
            String key3 = rs1.getString(1);
            boolean condition = rowCount==3 && check1 && key1.equals("10") && check2 && key2.equals("13") && check3 && key3.equals("16");
            if(!condition){
                System.out.println("rowCount sb 3 but is "+rowCount+" check1 sb true but is " + check1 + ", check2 sb true but is " + check2 + ", check3 sb true but is " + check3);
                System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 13 but is " + key2 + ", key3 sb 16 but is " + key3);
            }
            else
            {
                cleanupTable(s,  table1);
                cleanupTable(s,  table2);
            }
            assertCondition(condition, "Added 1/23/2007 by Toolbox.");
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception.  Added 1/23/2007 by Toolbox.");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys  -- multiple rows
     * executeUpdate(sql, columnNames)
     */
    public void Var087()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV87A";
	    String table2 = JDStatementTest.COLLECTION + "/JDGENKEYSV87B";
            s = connectionSystemNaming_.createStatement ();
	    initTable(s, table1," (NAME VARCHAR(10))");
	    s.executeUpdate("insert into "+table1+" values('Abraham')");
	    s.executeUpdate("insert into "+table1+" values('John')");
	    s.executeUpdate("insert into "+table1+" values('Zedekiah')");

      JDTestDriver.dropTable(s, table2); 

	    initTable(s, table2," (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY ( START WITH 10, increment BY 3 ))");

            String[] columnNames = new String[] {"GENID"};
	    int rowCount = s.executeUpdate ("INSERT INTO " + table2
                       + " (NAME) SELECT * from "+table1, columnNames); 
            ResultSet rs1 = s.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 

            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean check2 = rs1.next();
            String key2 = rs1.getString(1);
            boolean check3 = rs1.next();
            String key3 = rs1.getString(1);
            boolean condition = rowCount==3 && check1 && key1.equals("10") && check2 && key2.equals("13") && check3 && key3.equals("16");
            if(!condition){
                System.out.println(" rowCount="+rowCount+" sb 3  check1 sb true but is " + check1 + ", check2 sb true but is " + check2 + ", check3 sb true but is " + check3);
                System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 13 but is " + key2 + ", key3 sb 16 but is " + key3);
            }
            else
            {
                cleanupTable(s,  table1);
                cleanupTable(s,  table2);
            }
            assertCondition(condition);
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

    // @B1A
    /**
    * genGeneratedKeys() -- obtain multiple keys  -- multiple rows
    * prepareStatement(sql, columnIndexes)
    * This is supported in V5R5
    */
    public void Var088()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support INSERT/SELECT");
	    return; 
	}
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + ".JDGENKEYSV88A";
	    String table2 = JDStatementTest.COLLECTION + ".JDGENKEYSV88B";
            s = connection_.createStatement ();
	    initTable(s, table1, " (NAME VARCHAR(10))");
	    s.executeUpdate("insert into "+table1+" values('Abraham')");
	    s.executeUpdate("insert into "+table1+" values('John')");
	    s.executeUpdate("insert into "+table1+" values('Zedekiah')");

      JDTestDriver.dropTable(s, table2); 

	    initTable(s, table2," (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY ( START WITH 10, increment BY 3 ))");

            int columnIndexes[] = {2};
            PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + table2
                       + " (NAME) SELECT * from "+table1, columnIndexes);
            ps.execute();
            ResultSet rs1 = ps.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean check2 = rs1.next();
            String key2 = rs1.getString(1);
            boolean check3 = rs1.next();
            String key3 = rs1.getString(1);
            boolean condition = check1 && key1.equals("10") && check2 && key2.equals("13") && check3 && key3.equals("16");
            if(!condition){
                System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2 + ", check3 sb true but is " + check3);
                System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 13 but is " + key2 + ", key3 sb 16 but is " + key3);
            }
            else
            {
                cleanupTable(s,  table1);
                cleanupTable(s,  table2);
            }
            assertCondition(condition, "Added 1/23/2007 by Toolbox.");
            ps.close();
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception.  Added 1/23/2007 by Toolbox.");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys  -- multiple rows
     * prepareStatement(sql, columnNames)
     */
    public void Var089()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV89A";
	    String table2 = JDStatementTest.COLLECTION + "/JDGENKEYSV89B";
            s = connectionSystemNaming_.createStatement ();

	    initTable(s, table1, " (NAME VARCHAR(10))");
	    s.executeUpdate("insert into "+table1+" values('Abraham')");
	    s.executeUpdate("insert into "+table1+" values('John')");
	    s.executeUpdate("insert into "+table1+" values('Zedekiah')");

      JDTestDriver.dropTable(s, table2); 

	    initTable(s, table2," (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY ( START WITH 10, increment BY 3 ))");

            String[] columnNames = new String[] {"GENID"};
            PreparedStatement ps = connectionSystemNaming_.prepareStatement("INSERT INTO " + table2
                       + " (NAME) SELECT * from "+table1, columnNames);
            ps.execute();
            ResultSet rs1 = ps.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 

            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean check2 = rs1.next();
            String key2 = rs1.getString(1);
            boolean check3 = rs1.next();
            String key3 = rs1.getString(1);
            boolean condition = check1 && key1.equals("10") && check2 && key2.equals("13") && check3 && key3.equals("16");
            if(!condition){
                System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2 + ", check3 sb true but is " + check3);
                System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 13 but is " + key2 + ", key3 sb 16 but is " + key3);
            }
            else
            {
                cleanupTable(s,  table1);
                cleanupTable(s,  table2);
            }
            assertCondition(condition);
            ps.close();
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

    //@B1A
    /**
     * genGeneratedKeys() -- return multiple results  -- multiple rows
     * execute(sql, int)
     */
    public void Var090()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV90";
            s = connectionSystemNaming_.createStatement ();
            JDTestDriver.dropTable(s, table1); 

	    initTable(s, table1, " (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3))");
            s.execute("INSERT INTO " + table1 + " (NAME) VALUES('Snickers'), ('Kim')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs1 = s.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean check2 = rs1.next();
            String key2 = rs1.getString(1);
            boolean condition = check1 && key1.equals("10") && check2 && key2.equals("13");
            if(!condition){
                System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2);
                System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 13 but is " + key2);
            }
            else
            {
                cleanupTable(s,  table1);
            }
            assertCondition(condition);
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys  -- multiple rows
     * executeUpdate(sql, int)
     */
    public void Var091()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV91";
            s = connectionSystemNaming_.createStatement ();
            JDTestDriver.dropTable(s, table1); 

	    initTable(s, table1, " (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3))");
            int rowCount = s.executeUpdate("INSERT INTO " + table1 + " (NAME) VALUES('Snickers'), ('Heath')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs1 = s.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean check2 = rs1.next();
            String key2 = rs1.getString(1);
            boolean condition = rowCount==2 && check1 && key1.equals("10") && check2 && key2.equals("13");
            if(!condition){
		System.out.println(" rowCount = "+rowCount+" sb 2"); 
                System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2);
                System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 13 but is " + key2);
            }
            else
            {
                cleanupTable(s,  table1);
            }
            assertCondition(condition);
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys  -- multiple rows
     * execute(sql, columnIndexes)
     */
    public void Var092()
    {
 	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
       if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV92";
            s = connectionSystemNaming_.createStatement ();
            JDTestDriver.dropTable(s, table1); 

	    initTable(s, table1, " (NAME VARCHAR(10), ID ROWID GENERATED ALWAYS, GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3))");
            int[] columnIndexes = {3};
            s.execute("INSERT INTO " + table1 + " (NAME) VALUES('Snickers'), ('Heath')", columnIndexes);
            ResultSet rs1 = s.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean check2 = rs1.next();
            String key2 = rs1.getString(1);
            boolean condition = check1 && key1.equals("10") && check2 && key2.equals("13");
            if(!condition){
                System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2);
                System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 13 but is " + key2);
            }
            else
            {
                cleanupTable(s,  table1);
            }
            assertCondition(condition);
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys  -- multiple rows
     * execute(sql, columnNames)
     */
    public void Var093()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV93";
            s = connectionSystemNaming_.createStatement ();
            JDTestDriver.dropTable(s, table1); 

	    initTable(s, table1, " (NAME VARCHAR(10), ID ROWID GENERATED ALWAYS, GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3))");
            String[] columnNames = new String[] {"GENID"};
            s.execute("INSERT INTO " + table1 + " (NAME) VALUES('Snickers'), ('Heath')", columnNames);
            ResultSet rs1 = s.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean check2 = rs1.next();
            String key2 = rs1.getString(1);
            boolean condition = check1 && key1.equals("10") && check2 && key2.equals("13");
            if(!condition){
                System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2);
                System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 10 but is " + key2);
            }
            else
            {
                cleanupTable(s,  table1);
            }
            assertCondition(condition);
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys  -- multiple rows
     * executeUpdate(sql, columnIndexes)
     */
    public void Var094()
    {
 	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
       if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV94";
            s = connectionSystemNaming_.createStatement ();
            JDTestDriver.dropTable(s, table1); 

	    initTable(s, table1, " (NAME VARCHAR(10), ID ROWID GENERATED ALWAYS, GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3))");
            int[] columnIndexes = {3};
            int rowCount = s.executeUpdate("INSERT INTO " + table1 + " (NAME) VALUES('Snickers'), ('Heath')", columnIndexes);
            ResultSet rs1 = s.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean check2 = rs1.next();
            String key2 = rs1.getString(1);
            boolean condition = rowCount==2 && check1 && key1.equals("10") && check2 && key2.equals("13");
            if(!condition){
		System.out.println(" rowCount = "+rowCount+" sb 2"); 
                System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2);
                System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 10 but is " + key2);
            }
            else
            {
                cleanupTable(s,  table1);
            }
            assertCondition(condition);
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys  -- multiple rows
     * executeUpdate(sql, columnNames)
     */
    public void Var095()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV95";
            s = connectionSystemNaming_.createStatement ();
            JDTestDriver.dropTable(s, table1); 

	    initTable(s, table1, " (NAME VARCHAR(10), ID ROWID GENERATED ALWAYS, GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3))");
            String[] columnNames = new String[] {"GENID"};
            int rowCount = s.executeUpdate("INSERT INTO " + table1 + " (NAME) VALUES('Snickers'), ('Heath')", columnNames);
            ResultSet rs1 = s.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            boolean check2 = rs1.next();
            String key2 = rs1.getString(1);
            boolean condition = rowCount == 2 && check1 && key1.equals("10") && check2 && key2.equals("13");
            if(!condition){
		System.out.println(" rowCount sb 2 but is "+rowCount); 
                System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2);
                System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 10 but is " + key2);
            }
            else
            {
                cleanupTable(s,  table1);
            }
            assertCondition(condition);
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys  -- multiple columns
     * prepareStatement(sql, int)
     */
    public void Var096()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV96";
            s = connectionSystemNaming_.createStatement ();
            JDTestDriver.dropTable(s, table1); 

	    initTable(s, table1, " (NAME VARCHAR(10), ID ROWID GENERATED ALWAYS, GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3))");
            PreparedStatement ps = connectionSystemNaming_.prepareStatement("INSERT INTO " + table1 + " (NAME) VALUES('Snickers')", Statement.RETURN_GENERATED_KEYS);
            ps.execute();
            ResultSet rs1 = ps.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            String key2 = rs1.getString(2);
            boolean condition = check1 && key1.length() > 1 && key2.equals("10");
            if(!condition){
                System.out.println(" check1 sb true but is " + check1);
                System.out.println(" key1.length()>1 sb true but is " + (key1.length()>1) + ", key2 sb 10 but is " + key2);
            }
            else
            {
                cleanupTable(s,  table1);
            }
            assertCondition(condition);
            ps.close();
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys   -- multiple columns
     * prepareStatement(sql, columnIndexes)
     */
    public void Var097()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV97";
            s = connectionSystemNaming_.createStatement ();
            JDTestDriver.dropTable(s, table1); 

	    initTable(s, table1, " (NAME VARCHAR(10), ID ROWID GENERATED ALWAYS, GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3))");
            int[] columnIndexes = {2,3};
            PreparedStatement ps = connectionSystemNaming_.prepareStatement("INSERT INTO " + table1 + " (NAME) VALUES('Snickers')", columnIndexes);
            ps.execute();
            ResultSet rs1 = ps.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            String key2 = rs1.getString(2);
            boolean condition = check1 && key1.length() > 1 && key2.equals("10");
            if(!condition){
                System.out.println(" check1 sb true but is " + check1);
                System.out.println(" key1.length()>1 sb true but is " + (key1.length()>1) + ", key2 sb 10 but is " + key2);
            }
            else
            {
                cleanupTable(s,  table1);
            }
            assertCondition(condition);
            ps.close();
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }

    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys   -- multiple columns
     * prepareStatement(sql, columnNames)
     */
    public void Var098()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV98";
            s = connectionSystemNaming_.createStatement ();
            JDTestDriver.dropTable(s, table1); 

	    initTable(s, table1, " (NAME VARCHAR(10), ID ROWID GENERATED ALWAYS, GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3))");
            String[] columnNames = new String[] {"ID", "GENID"};
            PreparedStatement ps = connectionSystemNaming_.prepareStatement("INSERT INTO " + table1 + " (NAME) VALUES('Snickers')", columnNames);
            ps.execute();
            ResultSet rs1 = ps.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            String key2 = rs1.getString(2);
            boolean condition = check1 && key1.length() > 1 && key2.equals("10");
            if(!condition){
                System.out.println(" check1 sb true but is " + check1);
                System.out.println(" key1.length()>1 sb true but is " + (key1.length()>1) + ", key2 sb 10 but is " + key2);
            }
            else
            {
                cleanupTable(s,  table1);
            }
            assertCondition(condition);
            ps.close();
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


    public void stringTruncationTest(String datatype, String tablename, String insertValue  ) {
        String added = " -- "+datatype+" truncation test added 4/16/2008 by native"; 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation"+added);
            return;
        }
        Statement s = null;
        PreparedStatement ps = null; 
        try {
            String table1 =  JDStatementTest.COLLECTION + "/"+tablename;
            s = connectionSystemNaming_.createStatement ();

            initTable(s, table1, " (NAME "+datatype+", ID INTEGER GENERATED ALWAYS AS IDENTITY)");
            ps = connectionSystemNaming_.prepareStatement("INSERT INTO " + table1 + " (NAME) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            try { 
                ps.setString(1,insertValue); 
                ps.execute();

                failed("Expected truncation from autogenerated key"+added); 
            } catch (Exception e) {
                String expectedException = "Data truncation"; 
                assertCondition(e.getMessage().indexOf(expectedException) >= 0,  "Bad exception "+e+" expected "+expectedException+added);

            }
            try { 
                cleanupTable(s,  table1);
                ps.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception "+added);
            try { if (s != null) s.close(); 
            } catch (SQLException se) { }
            try { if (ps != null) ps.close();}
            catch (SQLException se) {} 
        }

    }


    //@B2A
    /**
     * prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )
     * Test trunction for CHAR parameter 
     */
    public void Var099()
    {
	stringTruncationTest("CHAR(10)","JDSGKYV099","THISISTOOBIG"); 
    }

    /**
     * prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )
     * Test trunction for CHAR parameter 
     */
    public void Var100()
    {
	stringTruncationTest("VARCHAR(10)","JDSGKYV100","THISISTOOBIG"); 
    }



    public void binaryTruncationTest(String datatype, String tablename, byte[] insertValue  ) {
        String added = " -- "+datatype+" truncation test added 4/16/2008 by native"; 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation"+added);
            return;
        }
        Statement s = null;
        PreparedStatement ps = null; 
        try {
            String table1 =  JDStatementTest.COLLECTION + "/"+tablename;
            s = connectionSystemNaming_.createStatement ();
            initTable(s, table1, " (NAME "+datatype+", ID INTEGER GENERATED ALWAYS AS IDENTITY)");
            ps = connectionSystemNaming_.prepareStatement("INSERT INTO " + table1 + " (NAME) VALUES(?)", Statement.RETURN_GENERATED_KEYS);

            try { 
                ps.setBytes(1,insertValue); 
                ps.execute();

                failed("Expected truncation from autogenerated key"+added); 
            } catch (Exception e) {
                String expectedException = "Data truncation"; 
                assertCondition(e.getMessage().indexOf(expectedException) >= 0,  "Bad exception "+e+" expected "+expectedException+added);

            }
            try { 
                cleanupTable(s,  table1);
                ps.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception "+added);
            try { if (s != null) s.close(); 
            } catch (SQLException se) { }
            try { if (ps != null) ps.close();}
            catch (SQLException se) {} 
        }

    }

    /**
     * prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )
     * Test trunction for BINARY parameter 
     */
    public void Var101()
    {
	byte[] stuff = {(byte)'T',(byte)'H',(byte)'I',(byte)'S',(byte)'I',(byte)'S',
	(byte)'T',(byte)'O',(byte)'O',(byte)'B',(byte)'I',(byte)'G'};

	binaryTruncationTest("BINARY(10)","JDSGKYV101", stuff); 
    }


    /**
     * prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )
     * Test trunction for VARBINARY parameter 
     */
    public void Var102()
    {
	byte[] stuff = {(byte)'T',(byte)'H',(byte)'I',(byte)'S',(byte)'I',(byte)'S',
	(byte)'T',(byte)'O',(byte)'O',(byte)'B',(byte)'I',(byte)'G'};

	binaryTruncationTest("VARBINARY(10)","JDSGKYV102", stuff); 
    }

    /**
     * prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )
     * Test trunction for GRAPHIC parameter 
     */
    public void Var103()
    {
	stringTruncationTest("GRAPHIC(10) CCSID 13488","JDSGKYV103","THISISTOOBIG"); 
    }

    /**
     * prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )
     * Test trunction for VARGRAPHIC parameter 
     */
    public void Var104()
    {
	stringTruncationTest("VARGRAPHIC(10) CCSID 13488","JDSGKYV104","THISISTOOBIG");
    }

    /**
     * prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )
     * Test trunction for BLOB parameter 
     */
    public void Var105()
    {
	byte[] stuff = {(byte)'T',(byte)'H',(byte)'I',(byte)'S',(byte)'I',(byte)'S',
	(byte)'T',(byte)'O',(byte)'O',(byte)'B',(byte)'I',(byte)'G'};

	binaryTruncationTest("BLOB(10)","JDSGKYV105", stuff); 
    }

    /**
     * prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )
     * Test trunction for DBCLOB parameter 
     */
    public void Var106()
    {
	stringTruncationTest("DBCLOB(10) CCSID 13488","JDSGKYV106","THISISTOOBIG");
    }

    /**
     * prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )
     * Test trunction for CLOB parameter 
     */
    public void Var107()
    {
	stringTruncationTest("CLOB(10)","JDSGKYV107","THISISTOOBIG");
    }


    /**
     * prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )
     * Test trunction for CLOB UTF-8 parameter 
     */
    public void Var108()
    {
	stringTruncationTest("CLOB(10) CCSID 1208","JDSGKYV108","THISISTOOBIG");
    }


    /**
     * Verify that extra insert doesnt cause problems 
     */

    public void Var109()
    {
        String added = " -- select from insert test Added 04/16/2008 by native driver";
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation"+added);
            return;
        }
        Statement s = null;
        PreparedStatement ps = null; 

        try {
            String table1 =  JDStatementTest.COLLECTION + "/JDSGKYV109";
            s = connectionSystemNaming_.createStatement ();
            initTable(s, table1, " (NAME VARCHAR(80), INSERT VARCHAR(80), ID INTEGER GENERATED ALWAYS AS IDENTITY)");
            s.executeUpdate("insert into "+table1+" (NAME,INSERT) VALUES('one','ONE')");
            s.executeUpdate("insert into "+table1+" (NAME,INSERT) VALUES('two','TWO')");
            s.executeUpdate("insert into "+table1+" (NAME,INSERT) VALUES('three','THREE')");
            
            ps = connectionSystemNaming_.prepareStatement("SELECT NAME, INSERT, ID FROM "+ table1 );
            ResultSet rs = ps.executeQuery();

            int updateCount = ps.getUpdateCount(); 
            
            String answer1="NONE";
            String answer2="NONE";
            String answer3="NONE";
            if (rs.next()) { answer1 = rs.getString(1); } 
            if (rs.next()) { answer2 = rs.getString(2); } 
            if (rs.next()) { answer3 = rs.getString(1); } 
            rs.close(); 
            
            assertCondition(updateCount == -1 && 
                  "one".equals(answer1) && 
                  "TWO".equals(answer2) &&
                  "three".equals(answer3), 
                    "Expected updateCount of -1 got "+updateCount+
                    "\nRow 1 expected one got "+answer1+
                    "\nRow 2 expected TWO got "+answer2+
                    "\nRow 3 expected three got "+answer3+added); 


            try { 
                cleanupTable(s,  table1);
                ps.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch ( Exception e) {
            failed(e, "Unexpected exception "+added);
            try { if (s != null) s.close(); 
            } catch (SQLException se) { }
            try { if (ps != null) ps.close();}
            catch (SQLException se) {} 
        } 
    } 




    /**
     * Verify that update count from custom select from insert statement works
     */

    public void Var110()
    {
	String added = " -- select from insert test Added 04/16/2008 by native driver";
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation"+added);
            return;
        }
        Statement s = null;
	PreparedStatement ps = null; 

	try {
            String table1 =  JDStatementTest.COLLECTION + "/JDSGKYV110";
            s = connectionSystemNaming_.createStatement ();
            initTable(s, table1, " (NAME VARCHAR(80), ID INTEGER GENERATED ALWAYS AS IDENTITY)");
            ps = connectionSystemNaming_.prepareStatement("SELECT NAME,ID FROM FINAL TABLE ( INSERT INTO " + table1 + " (NAME) VALUES(?))");
	    ps.setString(1,"HELLO"); 
	    ResultSet rs = ps.executeQuery();

            int updateCount = ps.getUpdateCount(); 
    
	    String answer="NONE";
	    if (rs.next()) {
		answer = rs.getString(1); 
	    } 
	    rs.close(); 
            int expectedCount = 1;
            if(isToolboxDriver())
                expectedCount = -1; //javadoc: if the result is a ResultSet object or there are no more results, -1 is returned. 
            assertCondition(updateCount == expectedCount && "HELLO".equals(answer), 
                  "Expected updateCount of 1 got "+updateCount+
                    " expected HELLO got "+answer+added); 


            try { 
                cleanupTable(s,  table1);
                ps.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


	} catch ( Exception e) {
	    failed(e, "Unexpected exception "+added);
	    try { if (s != null) s.close(); 
	    } catch (SQLException se) { }
	    try { if (ps != null) ps.close();}
	    catch (SQLException se) {} 
	} 
    } 


    /**
     * Verify that update count from custom select from insert statement works
     */

    public void Var111()
    {
	String added = " -- select from insert test Added 04/16/2008 by native driver";
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation"+added);
            return;
        }
        Statement s = null;
	PreparedStatement ps = null; 

	try {
            String table1 =  JDStatementTest.COLLECTION + "/JDSGKYV111";
            s = connectionSystemNaming_.createStatement ();
            initTable(s, table1, " (NAME VARCHAR(80), ID INTEGER GENERATED ALWAYS AS IDENTITY)");
            ps = connectionSystemNaming_.prepareStatement("SELECT NAME,ID FROM FINAL TABLE (INSERT INTO " + table1 + " (NAME) VALUES(?))");
	    ps.setString(1,"HELLO"); 
	    ResultSet rs = ps.executeQuery();

            int updateCount = ps.getUpdateCount(); 
    
	    String answer="NONE";
	    if (rs.next()) {
		answer = rs.getString(1); 
	    } 
	    rs.close(); 
            
	    int expectedCount = 1;
        if(isToolboxDriver())
            expectedCount = -1; //javadoc: if the result is a ResultSet object or there are no more results, -1 is returned. 
        
            assertCondition(updateCount == expectedCount && "HELLO".equals(answer), 
                  "Expected updateCount of 1 got "+updateCount+
                    " expected HELLO got "+answer+added); 


            try { 
                cleanupTable(s,  table1);
                ps.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


	} catch ( Exception e) {
	    failed(e, "Unexpected exception "+added);
	    try { if (s != null) s.close(); 
	    } catch (SQLException se) { }
	    try { if (ps != null) ps.close();}
	    catch (SQLException se) {} 
	} 
    } 


    /**
     * Verify that update count from custom select from insert statement works
     */

    public void Var112()
    {
	String added = " -- select from insert test Added 04/16/2008 by native driver";
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation"+added);
            return;
        }
        Statement s = null;
	PreparedStatement ps = null; 

	try {
            String table1 =  JDStatementTest.COLLECTION + "/JDSGKYV112";
            s = connectionSystemNaming_.createStatement ();
            initTable(s, table1, " (NAME VARCHAR(80), ID INTEGER GENERATED ALWAYS AS IDENTITY)");
            ps = connectionSystemNaming_.prepareStatement("SELECT NAME,ID FROM FINAL TABLE(INSERT INTO " + table1 + " (NAME) VALUES(?))");
	    ps.setString(1,"HELLO"); 
	    ResultSet rs = ps.executeQuery();

            int updateCount = ps.getUpdateCount(); 
    
	    String answer="NONE";
	    if (rs.next()) {
		answer = rs.getString(1); 
	    } 
	    rs.close(); 
            
	    int expectedCount = 1;
        if(isToolboxDriver())
            expectedCount = -1; //javadoc: if the result is a ResultSet object or there are no more results, -1 is returned. 
        
            assertCondition(updateCount == expectedCount && "HELLO".equals(answer), 
                  "Expected updateCount of 1 got "+updateCount+
                    " expected HELLO got "+answer+added); 


            try { 
                cleanupTable(s,  table1);
                ps.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


	} catch ( Exception e) {
	    failed(e, "Unexpected exception "+added);
	    try { if (s != null) s.close(); 
	    } catch (SQLException se) { }
	    try { if (ps != null) ps.close();}
	    catch (SQLException se) {} 
	} 
    } 


    /**
     * Must sure that select from final table into table3 has an error
     */ 
    public void Var113() {
	String added = " -- Testcase added 12/15/2010 to test insert into table with trigger"; 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation"+added);
            return;
        }
	try {
	    Statement stmt = connection_.createStatement();
	    String sql = "select * from final table ( insert into "+table3_+" (NAME) VALUES('three'))"; 
	    try {
		stmt.executeQuery(sql);
		assertCondition(false, "SQL="+sql+" did not throw exception "+added); 
	    } catch (SQLException e) {
	      int sqlCode = e.getErrorCode(); 
	      String sqlState = e.getSQLState();
	      int expectedSqlCode = -723;
	      String expectedSqlState = "09000";
	      
	      boolean passed = sqlCode == expectedSqlCode && expectedSqlState.equals(sqlState); 
	      if (!passed) {
	        e.printStackTrace(); 
	      }
	      assertCondition(passed, "SQLCode="+sqlCode+" sb "+expectedSqlCode +" SQLState="+sqlState+" sb "+expectedSqlState+added); 
	    } finally {
		stmt.close(); 
	    }
	} catch (Exception e) {
	    failed (e, "Unexpected exception "+added); 
	} 
    }


    /**
     * Verify that insert into table3 works without an error when requesting keys. 
     */
  public void Var114() {
    String added = " -- Testcase added 12/15/2010 to test insert into table with trigger";
    if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
      notApplicable("v5r5 or later variation" + added);
      return;
    }
    try {

      Statement s = connection_.createStatement();
      s.execute("INSERT INTO " + table3_ + " (NAME) VALUES ('sue')",
          Statement.RETURN_GENERATED_KEYS);
      ResultSet rs = s.getGeneratedKeys();
      boolean check = rs.next();
      String generatedKey = rs.getString(1);
      boolean check2 = rs.next();
      assertCondition(check && !check2 && generatedKey.length() >= 1, added);

    } catch (Exception e) {
      failed(e, "Unexpected exception " + added);
    }
  }

    /**
     * Verify that insert into table3 works with out an error when requesting keys by name
     */
  public void Var115() {
    String added = " -- Testcase added 12/15/2010 to test insert into table with trigger";
    if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
      notApplicable("v5r5 or later variation" + added);
      return;
    }
    try {

      Statement s = connection_.createStatement();
      String [] columns = {"GENID"}; 
      s.execute("INSERT INTO " + table3_ + " (NAME) VALUES ('george')",columns); 
      ResultSet rs = s.getGeneratedKeys();
      boolean check = rs.next();
      String generatedKey = rs.getString(1);
      boolean check2 = rs.next();
      assertCondition(check && !check2 && generatedKey.length() >= 1, added);

    } catch (Exception e) {
      failed(e, "Unexpected exception " + added);
    }
  }


    /**
     * Verify that insert into table3 works with out an error when requesting keys by column number
     */

  public void Var116() {
    String added = " -- Testcase added 12/15/2010 to test insert into table with trigger";
    if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
      notApplicable("v5r5 or later variation" + added);
      return;
    }
    try {

      Statement s = connection_.createStatement();
      int [] columns = {1}; 
      s.execute("INSERT INTO " + table3_ + " (NAME) VALUES ('harold')",columns); 
      ResultSet rs = s.getGeneratedKeys();
      boolean check = rs.next();
      String generatedKey = rs.getString(1);
      boolean check2 = rs.next();
      assertCondition(check && !check2 && generatedKey.length() >= 1, added);

    } catch (Exception e) {
      failed(e, "Unexpected exception " + added);
    }
  }

  /**
   * Verify that insert into table3 works without an error when requesting keys. 
   */
public void Var117() {
  String added = " -- Testcase added 12/15/2010 to test insert into table with trigger";
  if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
    notApplicable("v5r5 or later variation" + added);
    return;
  }
  try {

    Statement s = connectionSystemNaming_.createStatement();
    s.execute("INSERT INTO " + systemTable3_ + " (NAME) VALUES ('sue')",
        Statement.RETURN_GENERATED_KEYS);
    ResultSet rs = s.getGeneratedKeys();
    boolean check = rs.next();
    String generatedKey = rs.getString(1);
    boolean check2 = rs.next();
    assertCondition(check && !check2 && generatedKey.length() >= 1, added);

  } catch (Exception e) {
    failed(e, "Unexpected exception " + added);
  }
}

  /**
   * Verify that insert into table3 works with out an error when requesting keys by name
   */
public void Var118() {
  String added = " -- Testcase added 12/15/2010 to test insert into table with trigger";
  if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
    notApplicable("v5r5 or later variation" + added);
    return;
  }
  try {

    Statement s = connectionSystemNaming_.createStatement();
    String [] columns = {"GENID"}; 
    s.execute("INSERT INTO " + systemTable3_ + " (NAME) VALUES ('george')",columns); 
    ResultSet rs = s.getGeneratedKeys();
    boolean check = rs.next();
    String generatedKey = rs.getString(1);
    boolean check2 = rs.next();
    assertCondition(check && !check2 && generatedKey.length() >= 1, added);

  } catch (Exception e) {
    failed(e, "Unexpected exception " + added);
  }
}


  /**
   * Verify that insert into table3 works with out an error when requesting keys by column number
   */

public void Var119() {
  String added = " -- Testcase added 12/15/2010 to test insert into table with trigger";
  if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
    notApplicable("v5r5 or later variation" + added);
    return;
  }
  try {

    Statement s = connectionSystemNaming_.createStatement();
    int [] columns = {1}; 
    s.execute("INSERT INTO " + systemTable3_ + " (NAME) VALUES ('harold')",columns); 
    ResultSet rs = s.getGeneratedKeys();
    boolean check = rs.next();
    String generatedKey = rs.getString(1);
    boolean check2 = rs.next();
    assertCondition(check && !check2 && generatedKey.length() >= 1, added);

  } catch (Exception e) {
    failed(e, "Unexpected exception " + added);
  }
}




/**
 * Must sure that select from final table into table3 has an error
 */ 
public void Var120() {
    String added = " -- Testcase added 12/15/2010 to test insert into table with trigger"; 
    if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
    {
        notApplicable("v5r5 or later variation"+added);
        return;
    }
    try {
        Statement stmt = connection_.createStatement();
        String sql = "select * from final table ( insert into "+table4_+" (NAME) VALUES('three'))"; 
        try {
            stmt.executeQuery(sql);
            assertCondition(false, "SQL="+sql+" did not throw exception "+added); 
        } catch (SQLException e) {
          int sqlCode = e.getErrorCode(); 
          String sqlState = e.getSQLState();
          int expectedSqlCode = -723;
          String expectedSqlState = "09000";
          
          boolean passed = sqlCode == expectedSqlCode && expectedSqlState.equals(sqlState); 
          if (!passed) {
            e.printStackTrace(); 
          }
          assertCondition(passed, "SQLCode="+sqlCode+" sb "+expectedSqlCode +" SQLState="+sqlState+" sb "+expectedSqlState+added); 
        } finally {
            stmt.close(); 
        }
    } catch (Exception e) {
        failed (e, "Unexpected exception "+added); 
    } 
}


/**
 * Verify that insert into table4 works without an error when requesting keys. 
 */
public void Var121() {
String added = " -- Testcase added 12/15/2010 to test insert into table with trigger";
if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
  notApplicable("v5r5 or later variation" + added);
  return;
}
try {

  Statement s = connection_.createStatement();
  s.execute("INSERT INTO " + table4_ + " (NAME) VALUES ('sue')",
      Statement.RETURN_GENERATED_KEYS);
  ResultSet rs = s.getGeneratedKeys();
  boolean check = rs.next();
  String generatedKey = rs.getString(1);
  boolean check2 = rs.next();
  assertCondition(check && !check2 && generatedKey.length() >= 1, added);

} catch (Exception e) {
  failed(e, "Unexpected exception " + added);
}
}

/**
 * Verify that insert into table4 works with out an error when requesting keys by name
 */
public void Var122() {
String added = " -- Testcase added 12/15/2010 to test insert into table with trigger";
if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
  notApplicable("v5r5 or later variation" + added);
  return;
}
try {

  Statement s = connection_.createStatement();
  String [] columns = {"GENID"}; 
  s.execute("INSERT INTO " + table4_ + " (NAME) VALUES ('george')",columns); 
  ResultSet rs = s.getGeneratedKeys();
  boolean check = rs.next();
  String generatedKey = rs.getString(1);
  boolean check2 = rs.next();
  assertCondition(check && !check2 && generatedKey.length() >= 1, added);

} catch (Exception e) {
  failed(e, "Unexpected exception " + added);
}
}


/**
 * Verify that insert into table4 works with out an error when requesting keys by column number
 */

public void Var123() {
String added = " -- Testcase added 12/15/2010 to test insert into table with trigger";
if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
  notApplicable("v5r5 or later variation" + added);
  return;
}
try {

  Statement s = connection_.createStatement();
  int [] columns = {1}; 
  s.execute("INSERT INTO " + table4_ + " (NAME) VALUES ('harold')",columns); 
  ResultSet rs = s.getGeneratedKeys();
  boolean check = rs.next();
  String generatedKey = rs.getString(1);
  boolean check2 = rs.next();
  assertCondition(check && !check2 && generatedKey.length() >= 1, added);

} catch (Exception e) {
  failed(e, "Unexpected exception " + added);
}
}

/**
* Verify that insert into table4 works without an error when requesting keys. 
*/
public void Var124() {
String added = " -- Testcase added 12/15/2010 to test insert into table with trigger";
if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
notApplicable("v5r5 or later variation" + added);
return;
}
try {

Statement s = connectionSystemNaming_.createStatement();
s.execute("INSERT INTO " + systemTable4_ + " (NAME) VALUES ('sue')",
    Statement.RETURN_GENERATED_KEYS);
ResultSet rs = s.getGeneratedKeys();
boolean check = rs.next();
String generatedKey = rs.getString(1);
boolean check2 = rs.next();
assertCondition(check && !check2 && generatedKey.length() >= 1, added);

} catch (Exception e) {
failed(e, "Unexpected exception " + added);
}
}

/**
* Verify that insert into table4 works with out an error when requesting keys by name
*/
public void Var125() {
String added = " -- Testcase added 12/15/2010 to test insert into table with trigger";
if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
notApplicable("v5r5 or later variation" + added);
return;
}
try {

Statement s = connectionSystemNaming_.createStatement();
String [] columns = {"GENID"}; 
s.execute("INSERT INTO " + systemTable4_ + " (NAME) VALUES ('george')",columns); 
ResultSet rs = s.getGeneratedKeys();
boolean check = rs.next();
String generatedKey = rs.getString(1);
boolean check2 = rs.next();
assertCondition(check && !check2 && generatedKey.length() >= 1, added);

} catch (Exception e) {
failed(e, "Unexpected exception " + added);
}
}


/**
* Verify that insert into table4 works with out an error when requesting keys by column number
*/

public void Var126() {
String added = " -- Testcase added 12/15/2010 to test insert into table with trigger";
if (getRelease() < JDTestDriver.RELEASE_V5R5M0) {
notApplicable("v5r5 or later variation" + added);
return;
}
try {

Statement s = connectionSystemNaming_.createStatement();
int [] columns = {1}; 
s.execute("INSERT INTO " + systemTable4_ + " (NAME) VALUES ('harold')",columns); 
ResultSet rs = s.getGeneratedKeys();
boolean check = rs.next();
String generatedKey = rs.getString(1);
boolean check2 = rs.next();
assertCondition(check && !check2 && generatedKey.length() >= 1, added);

} catch (Exception e) {
failed(e, "Unexpected exception " + added);
}
}

//
// START of JDBC 4.2 tests for executeLargeUpdate
//


/**
getGeneratedKeys() - Verify that a generated key is returned w/ executeUpdate and INSERT.
**/
    public void Var127()
    {
	if (checkJdbc42()) {
	    Statement s = null;
	    try
	    {
		s = connection_.createStatement();
		long rowCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","INSERT INTO " + table_
							      + " (NAME) VALUES ('sally')", Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = s.getGeneratedKeys();
		boolean check = rs.next();
		String generatedKey = rs.getString(1);
		boolean check2 = rs.next();
		assertCondition(rowCount==1 && check && !check2 && generatedKey.length() >= 1,
				"rowCount="+rowCount+" sb 1\n"+
				"check="+check+" sb true\n"+
				"check2="+check2+" sb false\n"+
				"generatedKey.length()="+generatedKey.length()+"sb  >= 1");
	    }
	    catch (Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	    finally{
		try{
		  if (s != null) s.close();
		}
		catch(Exception e){
		}
	    }
	}
    }





 

/**
getGeneratedKeys() - Verify that an exception is thrown if the user tries
to access generated keys once the statement is closed.
**/
    public void Var128()
    {
	if (checkJdbc42()) {
	    try
	    {
		Statement s = connection_.createStatement ();
		JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","INSERT INTO " + table_
					      + " (NAME) VALUES ('michelle')", 
					      Statement.RETURN_GENERATED_KEYS);
		s.close ();
		s.getGeneratedKeys();
		failed ("Didn't throw SQLException"); 
	    }
	    catch (Exception e) //@A1
	    {
		assertClosedException(e, "");
	    }
	}


    }
/**
getGeneratedKeys() - getGeneratedKeys() returns an empty result set on an UPDATE because no
auto-generated key will be returned.
**/
    public void Var129()
    {
	if (checkJdbc42()) {

	    try
	    {
		Statement s = connection_.createStatement ();
		long rowCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","UPDATE " + table_
							     + " SET NAME='Molly' WHERE NAME = 'Janet'",  
							     Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = s.getGeneratedKeys();
		if (rs == null) {
		    assertCondition(false, "Error:  getGeneratedKeys returned null."); 
		} else { 

		    boolean check = rs.next();
		    assertCondition(rowCount==0 && !check,
				    "rowCount="+rowCount+" sb 0\n"+
				    "rs.next returned "+check+" instead of false" );
		}
		s.close ();        
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }








    /**
getGeneratedKeys() - You shouldn't be able to call the new executeUpdate(String, int) methods on
a PreparedStatement.
**/
    public void Var130()
    {
	if (checkJdbc42()) {

	    try {
		PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + table_
								    + " (NAME) VALUES ('nick')");
		ps.execute();
		JDReflectionUtil.callMethod_L(ps,"executeLargeUpdate","INSERT INTO " + table_
					       + " (NAME) VALUES ('eric')", Statement.RETURN_GENERATED_KEYS);
		failed("Didn't throw SQLException");
	    }
	//@A1
	    catch (Exception e) {
		if (isToolboxDriver()) {
		    assertExceptionIs(e, "SQLException", "Function sequence error.");
		}	else if (getDriver () == JDTestDriver.DRIVER_JCC) {
		    assertCondition(e.getMessage().indexOf("cannot be called") >= 0, "Exception was "+e);
		}
		else
		{
		    assertCondition(e.getMessage().indexOf("Statement interface execution methods can't be used through a PreparedStatement object.") >= 0, "Exception is "+e);
		}
	    }
	}
    }


    

/**
getGeneratedKeys() - Using system naming -Verify that a generated key is returned w/ executeUpdate and INSERT.
**/
    public void Var131()
    {
	if (checkJdbc42()) {
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		notApplicable("System naming version NA for JCC");
		return; 
	    } 

	    Statement s = null;
	    try
	    {
		s = connectionSystemNaming_.createStatement();
		long rowCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","INSERT INTO " + systemTable_
							     + " (NAME) VALUES ('sally2')", Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = s.getGeneratedKeys();
		boolean check = rs.next();
		String generatedKey = rs.getString(1);
		boolean check2 = rs.next();
		assertCondition(rowCount==1 && check && !check2 && generatedKey.length() >= 1,
				"rowCount="+rowCount+" sb 1\n"+
				"check="+check+" sb true\n"+
				"check2="+check2+"sb false\n"+
				"generatedKey.length()="+generatedKey.length()+"sb >= 1\n"+
				"system naming test added 12/2/2003");
	    }
	    catch (Exception e)
	    {
		failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
	    }
	    finally{
		try{
		  if (s != null) s.close();
		}catch(Exception e){
		}
	    }
	}


    }







/**
getGeneratedKeys() - Using system naming -Verify that an exception is thrown if the user tries
to access generated keys once the statement is closed.
**/
    public void Var132()
    {
	if (checkJdbc42()) {
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		notApplicable("System naming version NA for JCC");
		return; 
	    } 

	    try
	    {
		Statement s = connectionSystemNaming_.createStatement ();
		JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","INSERT INTO " + systemTable_
					      + " (NAME) VALUES ('michelle')", 
					      Statement.RETURN_GENERATED_KEYS);
		s.close ();
		s.getGeneratedKeys();
		failed ("Didn't throw SQLException"); 
	    }
	    catch ( Exception e) //@A1
	    {
		assertClosedException(e, "");
	    }
	}
    }


/**
getGeneratedKeys() - Using system naming -getGeneratedKeys() returns an empty result set on an UPDATE because no
auto-generated key will be returned.
**/
    public void Var133()
    {
	if (checkJdbc42()) {
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		notApplicable("System naming version NA for JCC");
		return; 
	    } 
	    try
	    {
		Statement s = connectionSystemNaming_.createStatement ();
		long rowCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","UPDATE " + systemTable_
							     + " SET NAME='Molly' WHERE NAME = 'Janet'",  
							     Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = s.getGeneratedKeys();
		boolean check = rs.next();
		assertCondition(rowCount==0 && !check,  "rowCount="+rowCount+"sb 0 \n system naming test added 12/2/2003");
		s.close ();        
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
	    }
	}
    }


    //@B1A
   /**
    * executeUpdate(sql, columnNames)
    */

    public void Var134()
    {
	if (checkJdbc42()) {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC doesn't support columnNames");
	    return; 
	}

        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }
	if (isProxy()) {
	    notApplicable("proxy doesn't support column indexes");
	    return; 
	}

        Statement s = null;
        try {
            s = connection_.createStatement ();
	    String[] columnNames = {"GENID"}; 
	    long rowCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","INSERT INTO " + table_
                       + " (NAME) VALUES ('bobby')", columnNames); 
	    ResultSet rs = s.getGeneratedKeys();
            boolean check = rs.next();
            String generatedKey = rs.getString(1);
            boolean check2 = rs.next();
            assertCondition(rowCount==1 && check && !check2 && generatedKey.length() >= 1, "Added 1/23/2007 by Toolbox.");
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.  Added 1/23/2007 by Toolbox.");
        }
        finally{
            try{
              if (s != null)  s.close();
            }catch(Exception e){
            }
        }
    }

    }

    // @B1A
    /**
    * genGeneratedKeys() -- obtain multiple keys   -- multiple rows 
    * executeUpdate(sql, columnIndexes)
    * This is supported in V5R5
    */
    public void Var135()
    {
	if (checkJdbc42()) {

	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		notApplicable("JCC doesn't support INSERT/SELECT");
		return; 
	    }

	    if (isProxy()) {
		notApplicable("proxy doesn't support column indexes");
		return; 
	    }

	    Statement s = null;
	    try {
		String table1 =  JDStatementTest.COLLECTION + ".JDGENKEYSV86A";
		String table2 = JDStatementTest.COLLECTION + ".JDGENKEYSV86B";
		s = connection_.createStatement ();
		initTable(s, table1, " (NAME VARCHAR(10))");
		s.executeUpdate("insert into "+table1+" values('Abraham')");
		s.executeUpdate("insert into "+table1+" values('John')");
		s.executeUpdate("insert into "+table1+" values('Zedekiah')");

    JDTestDriver.dropTable(s, table2); 

		initTable(s, table2," (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY ( START WITH 10, increment BY 3 ))");

		int columnIndexes[] = {2};
		long rowCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","INSERT INTO " + table2
							     + " (NAME) SELECT * from "+table1, columnIndexes); 
		ResultSet rs1 = s.getGeneratedKeys();
		if (rs1 == null) throw new SQLException("no keys returned"); 
		boolean check1 = rs1.next();
		String key1 = rs1.getString(1);
		boolean check2 = rs1.next();
		String key2 = rs1.getString(1);
		boolean check3 = rs1.next();
		String key3 = rs1.getString(1);
		boolean condition = rowCount==3 && check1 && key1.equals("10") && check2 && key2.equals("13") && check3 && key3.equals("16");
		if(!condition){
		    System.out.println("rowCount sb 3 but is "+rowCount+" check1 sb true but is " + check1 + ", check2 sb true but is " + check2 + ", check3 sb true but is " + check3);
		    System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 13 but is " + key2 + ", key3 sb 16 but is " + key3);
		}
		else
		{
		    cleanupTable(s,  table1);
		    cleanupTable(s,  table2);
		}
		assertCondition(condition, "Added 1/23/2007 by Toolbox.");
		s.close();
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception.  Added 1/23/2007 by Toolbox.");
		try
		{
		  if (s != null)  s.close(); 
		}
		catch (SQLException se)
		{
		//ignore at this point
		}
	    }
	}

    }
    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys
     * executeUpdate(sql, columnNames)
     */
    public void Var136()
    {
	if (checkJdbc42()) {
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		notApplicable("System naming version NA for JCC");
		return; 
	    } 
	    if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
	    {
		notApplicable("v5r5 or later variation");
		return;
	    }
	    if (isProxy()) {
		notApplicable("proxy doesn't support column indexes");
		return; 
	    }

	    Statement s = null;
	    try {
		String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV87A";
		String table2 = JDStatementTest.COLLECTION + "/JDGENKEYSV87B";
		s = connectionSystemNaming_.createStatement ();
		initTable(s, table1, " (NAME VARCHAR(10))");
		s.executeUpdate("insert into "+table1+" values('Abraham')");
		s.executeUpdate("insert into "+table1+" values('John')");
		s.executeUpdate("insert into "+table1+" values('Zedekiah')");

    JDTestDriver.dropTable(s, table2); 

		initTable(s, table2," (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY ( START WITH 10, increment BY 3 ))");

		String[] columnNames = new String[] {"GENID"};
		long rowCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","INSERT INTO " + table2
							     + " (NAME) SELECT * from "+table1, columnNames); 
		ResultSet rs1 = s.getGeneratedKeys();
		if (rs1 == null) throw new SQLException("no keys returned"); 

		boolean check1 = rs1.next();
		String key1 = rs1.getString(1);
		boolean check2 = rs1.next();
		String key2 = rs1.getString(1);
		boolean check3 = rs1.next();
		String key3 = rs1.getString(1);
		boolean condition = rowCount==3 && check1 && key1.equals("10") && check2 && key2.equals("13") && check3 && key3.equals("16");
		if(!condition){
		    System.out.println(" rowCount="+rowCount+" sb 3  check1 sb true but is " + check1 + ", check2 sb true but is " + check2 + ", check3 sb true but is " + check3);
		    System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 13 but is " + key2 + ", key3 sb 16 but is " + key3);
		}
		else
		{
		    cleanupTable(s,  table1);
		    cleanupTable(s,  table2);
		}
		assertCondition(condition);
		s.close();
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
		try
		{
		  if (s != null) s.close(); 
		}
		catch (SQLException se)
		{
		//ignore at this point
		}
	    }
	}
    }
    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys
     * executeUpdate(sql, int)
     */
    public void Var137()
    {
	if (checkJdbc42()) {
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		notApplicable("System naming version NA for JCC");
		return; 
	    } 
	    if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
	    {
		notApplicable("v5r5 or later variation");
		return;
	    }
	    Statement s = null;
	    try {
		String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV91";
		s = connectionSystemNaming_.createStatement ();
    JDTestDriver.dropTable(s, table1); 

		initTable(s, table1, " (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3))");
		long rowCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate", "INSERT INTO " + table1 + " (NAME) VALUES('Snickers'), ('Heath')", Statement.RETURN_GENERATED_KEYS);
		ResultSet rs1 = s.getGeneratedKeys();
		if (rs1 == null) throw new SQLException("no keys returned"); 
		boolean check1 = rs1.next();
		String key1 = rs1.getString(1);
		boolean check2 = rs1.next();
		String key2 = rs1.getString(1);
		boolean condition = rowCount==2 && check1 && key1.equals("10") && check2 && key2.equals("13");
		if(!condition){
		    System.out.println(" rowCount = "+rowCount+" sb 2"); 
		    System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2);
		    System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 13 but is " + key2);
		}
		else
		{
		    cleanupTable(s,  table1);
		}
		assertCondition(condition);
		s.close();
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
		try
		{
		  if (s != null) s.close(); 
		}
		catch (SQLException se)
		{
		//ignore at this point
		}
	    }
	}

    }

    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys
     * executeUpdate(sql, columnIndexes)
     */
    public void Var138()
    {
	if (checkJdbc42()) {
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		notApplicable("System naming version NA for JCC");
		return; 
	    } 
	    if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
	    {
		notApplicable("v5r5 or later variation");
		return;
	    }
	    if (isProxy()) {
		notApplicable("proxy doesn't support column indexes");
		return; 
	    }

	    Statement s = null;
	    try {
		String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV94";
		s = connectionSystemNaming_.createStatement ();
    JDTestDriver.dropTable(s, table1); 

		initTable(s, table1, " (NAME VARCHAR(10), ID ROWID GENERATED ALWAYS, GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3))");
		int[] columnIndexes = {3};
		long rowCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","INSERT INTO " + table1 + " (NAME) VALUES('Snickers'), ('Heath')", columnIndexes);
		ResultSet rs1 = s.getGeneratedKeys();
		if (rs1 == null) throw new SQLException("no keys returned"); 
		boolean check1 = rs1.next();
		String key1 = rs1.getString(1);
		boolean check2 = rs1.next();
		String key2 = rs1.getString(1);
		boolean condition = rowCount==2 && check1 && key1.equals("10") && check2 && key2.equals("13");
		if(!condition){
		    System.out.println(" rowCount = "+rowCount+" sb 2"); 
		    System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2);
		    System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 10 but is " + key2);
		}
		else
		{
		    cleanupTable(s,  table1);
		}
		assertCondition(condition);
		s.close();
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
		try
		{
		  if (s != null) s.close(); 
		}
		catch (SQLException se)
		{
		//ignore at this point
		}
	    }
	}
    }
    //@B1A
    /**
     * genGeneratedKeys() -- obtain multiple keys
     * executeUpdate(sql, columnNames)
     */
    public void Var139()
    {
	if (checkJdbc42()) {
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		notApplicable("System naming version NA for JCC");
		return; 
	    } 
	    if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
	    {
		notApplicable("v5r5 or later variation");
		return;
	    }
	    if (isProxy()) {
		notApplicable("proxy doesn't support column indexes");
		return; 
	    }

	    Statement s = null;
	    try {
		String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV95";
		s = connectionSystemNaming_.createStatement ();
    JDTestDriver.dropTable(s, table1); 

		initTable(s, table1, " (NAME VARCHAR(10), ID ROWID GENERATED ALWAYS, GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3))");
		String[] columnNames = new String[] {"GENID"};
		long rowCount = JDReflectionUtil.callMethod_L(s,"executeLargeUpdate","INSERT INTO " + table1 + " (NAME) VALUES('Snickers'), ('Heath')", columnNames);
		ResultSet rs1 = s.getGeneratedKeys();
		if (rs1 == null) throw new SQLException("no keys returned"); 
		boolean check1 = rs1.next();
		String key1 = rs1.getString(1);
		boolean check2 = rs1.next();
		String key2 = rs1.getString(1);
		boolean condition = rowCount == 2 && check1 && key1.equals("10") && check2 && key2.equals("13");
		if(!condition){
		    System.out.println(" rowCount sb 2 but is "+rowCount); 
		    System.out.println(" check1 sb true but is " + check1 + ", check2 sb true but is " + check2);
		    System.out.println(" key1 sb 10 but is " + key1 + ", key2 sb 10 but is " + key2);
		}
		else
		{
		    cleanupTable(s,  table1);
		}
		assertCondition(condition);
		s.close();
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
		try
		{
		  if (s != null) s.close(); 
		}
		catch (SQLException se)
		{
		//ignore at this point
		}
	    }
	}

    }


    /**
     * genGeneratedKeys() -- obtain multiple keys  -- multiple columns , with row change timestamp
     * prepareStatement(sql, int)
     */
    public void Var140()
    {
	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("System naming version NA for JCC");
	    return; 
	} 
        if (getRelease() < JDTestDriver.RELEASE_V5R5M0)
        {
            notApplicable("v5r5 or later variation");
            return;
        }

        Statement s = null;
        try {
	    String table1 =  JDStatementTest.COLLECTION + "/JDGENKEYSV140";
            s = connectionSystemNaming_.createStatement ();
            JDTestDriver.dropTable(s, table1); 

	    initTable(s, table1, " (NAME VARCHAR(10), ID ROWID GENERATED ALWAYS, GENID INT GENERATED ALWAYS AS IDENTITY(START WITH 10, increment BY 3), GENTS TIMESTAMP FOR EACH ROW ON UPDATE AS ROW CHANGE TIMESTAMP NOT NULL )");
            PreparedStatement ps = connectionSystemNaming_.prepareStatement("INSERT INTO " + table1 + " (NAME) VALUES('Snickers')", Statement.RETURN_GENERATED_KEYS);
            ps.execute();
            ResultSet rs1 = ps.getGeneratedKeys();
	    if (rs1 == null) throw new SQLException("no keys returned"); 
            boolean check1 = rs1.next();
            String key1 = rs1.getString(1);
            String key2 = rs1.getString(2);
	    java.sql.Timestamp key3 = rs1.getTimestamp(3);

            boolean condition = check1 && key1.length() > 1 
                && key2.equals("10") && key3 != null ;
            if(!condition){
                System.out.println(" check1 sb true but is " + check1);
                System.out.println(" key1.length()>1 sb true but is " + 
                (key1.length()>1) + ", key2 sb 10 but is " + key2 +" key3 should not be null");
            }
            else
            {
                cleanupTable(s,  table1);
            }
            assertCondition(condition);
            ps.close();
            s.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- system naming testing added 12/2/2003");
            try
            {
                if (s != null) s.close();
            }
            catch (SQLException se)
            {
                //ignore at this point
            }
        }
    }


}


